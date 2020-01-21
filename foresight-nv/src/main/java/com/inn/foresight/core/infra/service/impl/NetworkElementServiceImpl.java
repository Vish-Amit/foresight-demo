package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.service.IAdvanceSearchProvider;
import com.inn.foresight.core.infra.service.INetworkElementService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.HeirarchyDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.PBServiceInfoWrapper;

/**
 * The Class NetworkElementServiceImpl.
 */
@Service("NetworkElementService")
public class NetworkElementServiceImpl extends AbstractService<Integer, NetworkElement>
		implements INetworkElementService, IAdvanceSearchProvider {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NetworkElementServiceImpl.class);

	/** The network element dao. */
	@Autowired
	private INetworkElementDao networkElementDao;

	/**
	 * Sets the dao.
	 *
	 * @param networkElementDao
	 *            the new dao
	 */
	public void setDao(INetworkElementDao networkElementDao) {
		super.setDao(networkElementDao);
		this.networkElementDao = networkElementDao;
	}

	/**
	 * Gets the network element map by ne name.
	 *
	 * @return the network element map by ne name
	 */
	@Override
	@Transactional
	public Map<String, NetworkElement> getNetworkElementMapByNeName() {
		Map<String, NetworkElement> networkMap = new HashMap<>();
		try {
			List<NetworkElement> networkElementList = networkElementDao.findAll();
			for (NetworkElement network : networkElementList) {
				if (network.getNeName() != null) {
					networkMap.put(network.getNeName(), network);
				}
			}
		} catch (DaoException e) {
			logger.error("getting Error in Data from Network element {} ", Utils.getStackTrace(e));
		}
		return networkMap;
	}

	/**
	 * Gets the distinct info from network element.
	 *
	 * @return the distinct info from network element
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public List<Map<String, Object>> getDistinctInfoFromNetworkElement() {
		logger.info("Going to get Distinct Info From NetworkElement");
		try {
			List<NetworkElementWrapper> wrapper = networkElementDao.getDistinctInfoFromNetworkElement();
			Map<String, List<NetworkElementWrapper>> domainWiseMap = getDomainWiseList(wrapper);
			Map<String, Map<String, List<NetworkElementWrapper>>> finalWrapper = getVendorWiseData(domainWiseMap);
			return getStructuredRsultJson(finalWrapper);
		} catch (Exception e) {
			logger.error("Error while get Distinct Info From NetworkElement ,err msg {}", Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * This method is converting data to reqired json format.
	 *
	 * @param distinctVendorData
	 *            the distinct vendor data
	 * @return the structured rsult json
	 */

	private List<Map<String, Object>> getStructuredRsultJson(
			Map<String, Map<String, List<NetworkElementWrapper>>> distinctVendorData) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		for (Map.Entry<String, Map<String, List<NetworkElementWrapper>>> domainEntry : distinctVendorData.entrySet()) {
			Map<String, Object> outerMap = new HashMap<>();
			outerMap.put("domain", domainEntry.getKey());
			List<HeirarchyDetailWrapper> vendorList = new ArrayList<>();
			for (Map.Entry<String, List<NetworkElementWrapper>> vendorEntry : domainEntry.getValue().entrySet()) {
				List<String> techList = new ArrayList<>();
				HeirarchyDetailWrapper domainVendorWrapper = new HeirarchyDetailWrapper();
				domainVendorWrapper.setKey(vendorEntry.getKey());
				domainVendorWrapper.setValue(vendorEntry.getKey().toUpperCase());
				for (NetworkElementWrapper tech : vendorEntry.getValue()) {
					techList.add(tech.getTechnology());
				}
				domainVendorWrapper.setTechnology(techList);
				vendorList.add(domainVendorWrapper);
			}
			outerMap.put("vendor", vendorList);
			resultList.add(outerMap);
		}
		return resultList;
	}

	/**
	 * Gets the vendor wise data.
	 *
	 * @param domainWiseMap
	 *            the domain wise map
	 * @return the vendor wise data
	 */
	private Map<String, Map<String, List<NetworkElementWrapper>>> getVendorWiseData(
			Map<String, List<NetworkElementWrapper>> domainWiseMap) {
		logger.info("Going to get Vendor Wise Data");
		Map<String, Map<String, List<NetworkElementWrapper>>> finalWrapper = new HashMap<>();
		for (Map.Entry<String, List<NetworkElementWrapper>> entry : domainWiseMap.entrySet()) {
			Map<String, List<NetworkElementWrapper>> vendorMap = new HashMap<>();
			for (NetworkElementWrapper vendor : entry.getValue()) {
				if (!vendorMap.containsKey(vendor.getVendor())) {
					vendorMap.put(vendor.getVendor(), new ArrayList<NetworkElementWrapper>());
				}
				vendorMap.get(vendor.getVendor()).add(vendor);
			}
			finalWrapper.put(entry.getKey(), vendorMap);
		}
		return finalWrapper;
	}

	/**
	 * Gets the domain wise list.
	 *
	 * @param wrapper
	 *            the wrapper
	 * @return the domain wise list
	 */
	private Map<String, List<NetworkElementWrapper>> getDomainWiseList(List<NetworkElementWrapper> wrapper) {
		logger.info("Going to get Domain Wise List");
		Map<String, List<NetworkElementWrapper>> domainWrapper = new HashMap<>();
		for (NetworkElementWrapper networkElementWrapper : wrapper) {
			if (!domainWrapper.containsKey(networkElementWrapper.getDomain())) {
				domainWrapper.put(networkElementWrapper.getDomain(), new ArrayList<NetworkElementWrapper>());
			}
			domainWrapper.get(networkElementWrapper.getDomain()).add(networkElementWrapper);
		}
		return domainWrapper;
	}

	@Override
	public List<Object> getDistinctDataFromNetworkElement(String searchType, String domain, String vendor,
			String technology, String softwareVersion) {
		logger.info("Going to get distinct data from network element by search type {}", searchType);
		try {
			if (ForesightConstants.SEARCH_SOFTWARE_VERSION.equalsIgnoreCase(searchType)) {
				return networkElementDao.getDistinctSWVersionFromNetworkElement(domain, vendor, technology);
			} else if (ForesightConstants.NETYPE_CAMEL_CASE.equalsIgnoreCase(searchType)) {
				return networkElementDao.getDistinctNETypeFromNetworkElement(domain, vendor, technology,
						softwareVersion);
			} else if (ForesightConstants.DOMAIN.equalsIgnoreCase(searchType)) {
				return networkElementDao.getDistinctDomainFromNetworkElement();
			} else if (ForesightConstants.VENDOR.equalsIgnoreCase(searchType)) {
				return networkElementDao.getDistinctVendorFromNetworkElement(domain);
			} else if (ForesightConstants.TECHNOLOGY.equalsIgnoreCase(searchType)) {
				return networkElementDao.getDistinctTechnologyFromNetworkElement(domain, vendor);
			} else {
				logger.info("searchType is not match by search type ");
			}
		} catch (Exception e) {
			logger.error("error in get Distinct data From NetworkElement by searchType {}, err msg {}", searchType,
					Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@Override
	public Map<NEType, Long> getCountByGeoraphyAndVendor(String vendor, String geographyName, String type) {
		logger.info("Going to ne count for vendor according to geography and type {} {} {} ", geographyName, vendor,
				type);
		List<NetworkElementWrapper> newWorkElemet = null;
		Map<NEType, Long> map = null;
		try {
			newWorkElemet = networkElementDao.getCountByGeoraphyAndVendor(Vendor.valueOf(vendor),
					Arrays.asList(geographyName), type);
			if (newWorkElemet != null) {
				map = newWorkElemet.stream().filter(x -> x != null)
						.collect(Collectors.toMap(NetworkElementWrapper::getNeType, NetworkElementWrapper::getCount));
				logger.info("the count map value is {}", new Gson().toJson(map));

			}

		} catch (DaoException e) {
			logger.error("error inside the method  getCountByGeoraphyAndVendor{}", Utils.getStackTrace(e));
		}
		return map;

	}

	@Override
	public Map<NEType, Long> getCountByGeoraphyAndVendor(String vendor, List<String> geographyName, String type) {
		logger.info("Going to ne count for vendor according to geography and type {} {} {} ", geographyName, vendor,
				type);
		List<NetworkElementWrapper> newWorkElemet = null;
		Map<NEType, Long> map = null;
		try {
			newWorkElemet = networkElementDao.getCountByGeoraphyAndVendor(Vendor.valueOf(vendor), geographyName, type);
			if (newWorkElemet != null) {
				map = newWorkElemet.stream().filter(x -> x != null)
						.collect(Collectors.toMap(NetworkElementWrapper::getNeType, NetworkElementWrapper::getCount));
				logger.info("the count map value is {}", new Gson().toJson(map));

			}

		} catch (DaoException e) {
			logger.error("error inside the method  getCountByGeoraphyAndVendor{}", Utils.getStackTrace(e));
		}
		return map;

	}

	@Override
	public List<Map<String, Object>> getNetworkElement(String netype, Integer geographyId, String geoType,
			String domain, String vendor, String technology, String softwareVersion) {
		logger.info("Going to get NetworkElement by netype {}, geographyL4Id {} and geoType {}", netype, geographyId,
				geoType);
		List<Map<String, Object>> neIdList = new ArrayList<>();
		try {
			List<Object[]> neObj = networkElementDao.getNetworkElement(netype, geographyId, geoType, domain, vendor,
					technology, softwareVersion);
			for (Object[] obj : neObj) {
				Map<String, Object> neIdMap = new HashMap<>();
				if (obj[0] != null && obj[1] != null) {
					neIdMap.put(InfraConstants.ID, obj[0]);
					neIdMap.put(InfraConstants.NENAME, obj[1]);
				}
				neIdList.add(neIdMap);
			}
		} catch (Exception e) {
			logger.error("Error in getting NetworkElement error {}", Utils.getStackTrace(e));
			throw new RestException(e);
		}
		logger.info("Returing NetworkElement List size {}", neIdList.size());
		return neIdList;
	}

	@Override
	public NetworkElementWrapper getSiteInfoBySapId(String siteID) {
		NetworkElementWrapper networkElementwrapper = null;
		try {
			networkElementwrapper = networkElementDao.getSiteInfoBySapId(siteID);
		} catch (Exception e) {
			logger.error("Error while getSiteInfo  ,err msg{}", Utils.getStackTrace(e));
		}

		return networkElementwrapper;
	}

	@Override
	public NetworkElementWrapper getGeographyL4BySapId(String siteID) {
		try {
			return networkElementDao.getGeographyL4BySapId(siteID);
		} catch (Exception e) {
			logger.error("Error while getSiteInfo ,err msg{}", Utils.getStackTrace(e));
			throw new RestException("Enable to get data");
		}
	}

	@Override
	public List<String> getNeFrequencyByDomainAndVendor(String domain, String vendor) {
		try {
			return networkElementDao.getNeFrequencyByDomainAndVendor(domain, vendor);
		} catch (Exception e) {
			logger.error("Error while getSiteInfo ,err msg{}", Utils.getStackTrace(e));
		}
		return new ArrayList();
	}

	@Override
	@Transactional
	public NetworkElementWrapper getNEDetailsByNEId(String neId, String isEffectedNodes) {
		NetworkElementWrapper networkElementWrapper = new NetworkElementWrapper();
		try {
			logger.info("Going to get NE Details for NEId {}", neId);
			NetworkElement networkElement = networkElementDao.getNEDetailByNEId(neId);
			if (networkElement != null) {
				networkElementWrapper.setId(networkElement.getId());
				if (networkElement.getLatitude() != null && networkElement.getLongitude() != null) {
					networkElementWrapper.setLatitude(networkElement.getLatitude());
					networkElementWrapper.setLongitude(networkElement.getLongitude());
				}
				if (networkElement.getNeId() != null) {
					networkElementWrapper.setNeId(networkElement.getNeId());
				}
				if (networkElement.getFriendlyname() != null) {
					networkElementWrapper.setSiteName(networkElement.getFriendlyname());
				}
				if (networkElement.getGeographyL4() != null) {
					networkElementWrapper.setGeographyL4(networkElement.getGeographyL4().getName());
					if (networkElement.getGeographyL4().getGeographyL3() != null) {
						networkElementWrapper
								.setGeographyL3(networkElement.getGeographyL4().getGeographyL3().getName());
						if (networkElement.getGeographyL4().getGeographyL3().getGeographyL2() != null) {
							networkElementWrapper.setGeographyL2(
									networkElement.getGeographyL4().getGeographyL3().getGeographyL2().getName());
							if (networkElement.getGeographyL4().getGeographyL3().getGeographyL2()
									.getGeographyL1() != null) {
								networkElementWrapper.setGeographyL1(networkElement.getGeographyL4().getGeographyL3()
										.getGeographyL2().getGeographyL1().getName());
							}
						}
					}
				}
				if (networkElement.getVendor() != null) {
					networkElementWrapper.setVendor(String.valueOf(networkElement.getVendor()));
				}
				if (networkElement.getDomain() != null) {
					networkElementWrapper.setDomain(String.valueOf(networkElement.getDomain()));
				}
				if (networkElement.getNeName() != null) {
					networkElementWrapper.setNeName(String.valueOf(networkElement.getNeName()));
				}
				if (networkElement.getNeType() != null) {
					networkElementWrapper.setNeType(networkElement.getNeType());
				}
				networkElementWrapper.setModel(networkElement.getModel());
			}

			if (StringUtils.equalsIgnoreCase(isEffectedNodes, "TRUE")) {
				try {
					Map<String, Set<String>> allChildbyNeId = networkElementDao.getAllChildbyNeId(neId);
					NEType neType = networkElement.getNeType();
					Set<String> list = allChildbyNeId.get(neType.name());
					if (CollectionUtils.isEmpty(list)) {
						list = new HashSet<>();
					}
					list.add(networkElement.getNeName());
					allChildbyNeId.put(neType.name(), list);
					networkElementWrapper.setEffectedNode(allChildbyNeId);

				} catch (Exception exception) {
					logger.error("Unable to get data {}", Utils.getStackTrace(exception));
				}
			}


			return networkElementWrapper;

		} catch (Exception exception) {
			logger.error("Error in getting NE Details for NEId {} Message {}", neId, exception.getMessage());
		}
		return networkElementWrapper;
	}

	@Override
	public void getAllChildbyNeId() {
		logger.info("Going to get AllChildbyNeId ");
		if(ConfigUtils.getString(ConfigUtil.NETWORK_NEID_MAP) !=null && !ConfigUtils.getString(ConfigUtil.NETWORK_NEID_MAP).isEmpty() && ConfigUtils.getString(ConfigUtil.NETWORK_NEID_MAP).equalsIgnoreCase("true")) 
		{
			logger.info("Going to populate NEId Map from NetworkElement at Scheduled time:{}",new Date());
			 networkElementDao.initializeRelationship();	
		}
		else {
			logger.info("Unable to populate Map from NetworkElement for map :{}",ConfigUtils.getString(ConfigUtil.NETWORK_NEID_MAP));
		}
		
	
		
		
		
	}
	
	@Override
	@Transactional
	public PBServiceInfoWrapper getSmallCellsAndWifiAPForPBService(List<NEType> neTypeList) {
		PBServiceInfoWrapper pbServiceInfoWrapper = new PBServiceInfoWrapper();
		try {
			List<NetworkElement> networkElementList = networkElementDao.getNetworkElementsForCellLevelDetail(neTypeList,
					null, null, null, null, null, null, null, null, null);
			if (networkElementList != null && !networkElementList.isEmpty()) {
				logger.info("Total {} recevied from NetworkElement", networkElementList.size());
				List<String> neNames = networkElementList.stream().map(n -> n.getNeName()).collect(Collectors.toList());
				Integer totalCount = neNames.size();
				pbServiceInfoWrapper.setServiceData(neNames);
				pbServiceInfoWrapper.setCount(totalCount);
			}
			return pbServiceInfoWrapper;
		} catch (DaoException daoException) {
			logger.info("DaoException caught while getting SmallCells for PBService Message : {}",
					daoException.getMessage());
		} catch (Exception exception) {
			logger.info("Error in getting SmallCells for PBService Message : {}", exception.getMessage());
		}
		return pbServiceInfoWrapper;
	}

	@Override
	@Transactional
	public List<NetworkElementWrapper> getNEDetailByNEName(List<String> neIdList) {
		List<NetworkElementWrapper> networkElementWrappers = new ArrayList<>();
		try {
			logger.info("Going to get NEDetail for neNameList");
			List<NetworkElement> networkElementList = networkElementDao.getNEDetailByNEIdList(neIdList);
			if (networkElementList != null && !networkElementList.isEmpty()) {
				logger.info("Total {} network elements received.", networkElementList.size());
				for (NetworkElement networkElement : networkElementList) {
					NetworkElementWrapper networkElementWrapper = getNetworkElementWrapper(networkElement);
					networkElementWrappers.add(networkElementWrapper);
				}
			}
			return networkElementWrappers;
		} catch (DaoException daoException) {
			logger.error("DAOException caught while getting NEDetail for neNameList Message : {}",
					daoException.getMessage());
		} catch (Exception exception) {
			logger.error("Error in getting NEDetail for neNameList Exception : {}", Utils.getStackTrace(exception));
		}
		return networkElementWrappers;
	}

	@Override
	public List<String> getSapidsByGeographyL4(String geographyL4) {
		try {
			return networkElementDao.getSapIdsByGeographyL4(geographyL4);
		} catch (Exception e) {
			logger.error("Exception in gettig neId by geographyL4 ERR : {}", Utils.getStackTrace(e));
			return Collections.emptyList();
		}
	}

	@Override
	public List<String> getCellIdsByGeographyL4(String geographyL4) {
		try {
			return networkElementDao.getCellIdsByGeographyL4(geographyL4);
		} catch (Exception e) {
			logger.error("Exception in gettig neId by geographyL4 ERR : {}", Utils.getStackTrace(e));
			return Collections.emptyList();
		}
	}

	private Boolean getListOfTypeCategoryForSearchByName(AdvanceSearch search) {
		List<Object> list = ConfigUtils.getList(InfraConstants.TYPE_CATEGORY_FOR_SEARCH_BY_NAME);
		for (Object typeCategory : list) {
			if (search.getAdvanceSearchConfiguration().getTypeCategory().equalsIgnoreCase(typeCategory.toString())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public Object getSearchData(Map<String, Object> map) {
		Map<String, Object> siteDetailMap = new HashMap<>();
		logger.info("Inside getSearchData map: {} ", map);
		try {
			AdvanceSearch search = (AdvanceSearch) map.get("advanceSearch");
			NetworkElement networkElement = null;
			if (search != null) {
				if (getListOfTypeCategoryForSearchByName(search)) {

					networkElement = networkElementDao.searchSiteByName(search.getName());
					if (networkElement == null) {
						networkElement = searchSiteByFriendlyName(search.getName(),
								NEType.valueOf(getSiteValue(search.getAdvanceSearchConfiguration().getTypeCategory())));
					}
				} else {
					networkElement = networkElementDao.getNEDataByNeNameAndNeType(search.getName(),
							NEType.valueOf(getSiteValue(search.getAdvanceSearchConfiguration().getTypeCategory())));
				}
				if (networkElement != null && networkElement.getLatitude() != null
						&& networkElement.getLongitude() != null && networkElement.getNeType() != null) {
					logger.info("newtwork element data found by its name : {} ", networkElement.getNeId());
					siteDetailMap.put(ForesightConstants.NE_NAME, networkElement.getNeName());
					siteDetailMap.put(ForesightConstants.NE_TYPE, networkElement.getNeType().name());
					siteDetailMap.put(ForesightConstants.NE_STATUS, networkElement.getNeStatus());
					siteDetailMap.put(ForesightConstants.LATITUDE, networkElement.getLatitude());
					siteDetailMap.put(ForesightConstants.LONGITUDE, networkElement.getLongitude());
					siteDetailMap.put(ForesightConstants.FRIENDLY_NAME, networkElement.getFriendlyname());

				} else {
					throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
				}
			}
		} catch (Exception exception) {
			logger.error("Error while getting site detail. Exception : {} ", Utils.getStackTrace(exception));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}

		return siteDetailMap;
	}

	public NetworkElement searchSiteByFriendlyName(String friendlyname, NEType neType) {
		NetworkElement networkElement = null;
		networkElement = networkElementDao.searchSiteByFriendlyName(friendlyname, neType);
		return networkElement;
	}

	private String getSiteValue(String value) {
		if (value != null) {
			if (value.equalsIgnoreCase("MACRO")) {
				return value;
			} else {
				return value.concat("_SITE");
			}
		}
		return value;
	}

	@Override
	public List<String> getSitesBySalesL4(String othergeographyname) {
		List<String> listOfNE = new ArrayList<>();
		logger.info("Inside getSitesBySalesL4 with name : {}", othergeographyname);
		try {
			List<Object> list = networkElementDao.getSitesBySalesL4(othergeographyname);
			if (list != null && !list.isEmpty()) {
				for (Object object : list) {
					logger.info("Inside getSitesBySalesL4  ---- data = " + object);
					listOfNE.add((String) object);
				}
			} else {
				throw new RestException("No sites found");
			}
		} catch (Exception e) {
			logger.error("Exception while getSitesBySalesL4 : {}", Utils.getStackTrace(e));
			e.printStackTrace();
		}
		return listOfNE;

	}

	@Override
	public List<Map<String, String>> getVendorAndL4WiseNename(Integer L4Id, String vendor) {
		logger.info("Going to get Vendor and L4 Wise Data");
		try {
			return networkElementDao.getVendorAndL4WiseNename(L4Id, vendor);
		} catch (Exception e) {
			logger.info("Exception in getVendorAndL4WiseNename in NetworkElement Service:{}", Utils.getStackTrace(e));
		}
		return new ArrayList<>();

	}

	private NetworkElementWrapper getNetworkElementWrapper(NetworkElement networkElement) {
		NetworkElementWrapper networkElementWrapper = new NetworkElementWrapper();
		try {
			networkElementWrapper.setParentNEId(
					networkElement.getNetworkElement() != null ? networkElement.getNetworkElement().getNeId() : null);
			networkElementWrapper.setNeId(networkElement.getNeId());
			networkElementWrapper
					.setVendor(networkElement.getVendor() != null ? networkElement.getVendor().name() : null);
			networkElementWrapper
					.setDomain(networkElement.getDomain() != null ? networkElement.getDomain().name() : null);
			if (networkElement.getGeographyL4() != null) {
				networkElementWrapper.setGeographyL4(networkElement.getGeographyL4().getName());
				if (networkElement.getGeographyL4().getGeographyL3() != null) {
					networkElementWrapper.setGeographyL3(networkElement.getGeographyL4().getGeographyL3().getName());
					if (networkElement.getGeographyL4().getGeographyL3().getGeographyL2() != null) {
						networkElementWrapper.setGeographyL2(
								networkElement.getGeographyL4().getGeographyL3().getGeographyL2().getName());
						if (networkElement.getGeographyL4().getGeographyL3().getGeographyL2()
								.getGeographyL1() != null) {
							networkElementWrapper.setGeographyL1(networkElement.getGeographyL4().getGeographyL3()
									.getGeographyL2().getGeographyL1().getName());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in putting NEDetail in wrapper for neNameList Message : {}",
					ExceptionUtils.getStackTrace(e));
		}
		return networkElementWrapper;
	}

	@Override
	public List<String> getDistinctMorphology() {
		try {
			return networkElementDao.getDistinctMorphology();
		} catch (DaoException daoException) {
			logger.error("DaoException caught while getting distinct Morphology {}", daoException.getStackTrace());
		} catch (Exception exception) {
			logger.error("Error in getting distinct Morphology {} ", exception.getStackTrace());
		}
		return null;
	}

	/*
	 * private Map<String, List<String>> getParentsChild(List<NEDetailWrapper> list)
	 * { Map<String, List<String>> map = new HashMap<>(); List<String> listneid =
	 * new ArrayList<>(); List<String> valueList = new ArrayList<>(); try { Boolean
	 * flag = false; for (NEDetailWrapper s : list) { if
	 * (map.containsKey(s.getPneId())) { listneid.add(s.getNename());
	 * valueList.add(s.getNeId()); map.put(s.getNeId(), listneid); } else { if (map
	 * != null && !map.isEmpty()) { map.get(s.getPneId()).add(s.getNename());
	 * map.put } } if (!flag) { listneid = new ArrayList<>();
	 * listneid.add(s.getNename()); listneid.add(s.getPneName());
	 * valueList.add(s.getNeId()); valueList.add(s.getPneId());
	 * 
	 * map.put(s.getPneId(), listneid); flag = false; } }
	 * 
	 * } } catch (Exception e) { logger.error("Unable to Populate data"); } return
	 * map; } }
	 */
	@Override
	public void initializeRelationship() {
		logger.info("Going to Initialize Relationship.");
		try {
			networkElementDao.initializeRelationship();
		} catch (Exception exception) {
			logger.error("Error in initializing Relationship. Exception : {}", ExceptionUtils.getStackTrace(exception));
		}
	}

	@Override
	public List<NetworkElementWrapper> getNEStatusCount(List<NEType> neTypeList, List<Domain> domainList,
			Map<String, List<String>> geographyNames) {
		logger.info("Going to get nestatus count");
		List<NetworkElementWrapper> networkElementWrapperList = new ArrayList<NetworkElementWrapper>();
		try {
			networkElementWrapperList = networkElementDao.getNEStatusCount(neTypeList, domainList, geographyNames);
		} catch (Exception exception) {
			logger.error("Exception caught while getting nestatus count. Message : {}", exception.getMessage());
		}
		return networkElementWrapperList;

	}

	@Override
	public List<NetworkElementWrapper> getSitesInfoByGC(Integer locationid) {
		logger.info("Going to get Sites By locationid : {}", locationid);
		List<NetworkElementWrapper> resultList = new ArrayList<>();
		try {
			resultList = networkElementDao.getSitesInfoByGC(locationid);
		} catch (Exception e) {
			logger.error("Exception in getSiteInfoByGC  : {}", e.getMessage());
		}
		return resultList;
	}

	@Override
	public List<String> getfilterByNename(String neName, NEType neType) {
		List<String> hostName = new ArrayList<>();
		hostName = networkElementDao.getfilterByNename(neName, neType);
		return hostName;
	}

	@Override
	public void selectAllFromNE() {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}
