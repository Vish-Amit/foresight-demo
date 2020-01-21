package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpGetRequest;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.ApplicationContextProvider;
import com.inn.foresight.core.gallery.service.IGalleryDetailService;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IAdvanceSearchConfigurationDao;
import com.inn.foresight.core.infra.dao.IAdvanceSearchDao;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.model.AdvanceSearchConfiguration;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.service.IAdvanceSearchProvider;
import com.inn.foresight.core.infra.service.IAdvanceSearchService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.product.security.spring.userdetails.CustomerInfo;
import com.inn.product.um.geography.dao.GeographyL1Dao;
import com.inn.product.um.geography.dao.GeographyL2Dao;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.dao.OtherGeographyDao;
import com.inn.product.um.user.utils.UmConstants;

/**
 * The Class AdvanceSearchServiceImpl.
 */
@Service("AdvanceSearchServiceImpl")
@Primary
public class AdvanceSearchServiceImpl extends AbstractService<Integer, AdvanceSearch>
		implements IAdvanceSearchService, IAdvanceSearchProvider {

	/** The logger. */
	private Logger logger = LogManager.getLogger(AdvanceSearchServiceImpl.class);

	/** The advance search dao. */
	@Autowired
	private IAdvanceSearchDao advanceSearchDao;

	/** The geography L 1 dao. */
	@Autowired
	private GeographyL1Dao geographyL1Dao;

	/** The geography L 2 dao. */
	@Autowired
	private GeographyL2Dao geographyL2Dao;

	/** The geography L 3 dao. */
	@Autowired
	private GeographyL3Dao geographyL3Dao;

	/** The geography L 4 dao. */
	@Autowired
	private GeographyL4Dao geographyL4Dao;

	/** The i network element dao. */
	@Autowired
	private INetworkElementDao iNetworkElementDao;

	@Autowired(required = false)
	@Qualifier("CustomAdvanceSearchServiceImpl")
	private IAdvanceSearchService iAdvanceSearchService;

	@Autowired(required = false)
	@Qualifier("ModuleSearchServiceImpl")
	private IAdvanceSearchService iModuleSearch;

	@Autowired(required = false)
	private IGalleryDetailService iGalleryDetailService;

	@Autowired
	private OtherGeographyDao iOtherGeographyDao;

	@Autowired
	private CustomerInfo customerInfo;

	@Autowired
	IAdvanceSearchConfigurationDao iAdvanceSearchConfigurationDao;

	/**
	 * Sets the dao.
	 *
	 * @param dao the new dao
	 */
	@Autowired
	public void setDao(IAdvanceSearchDao dao) {
		super.setDao(dao);
		advanceSearchDao = dao;
	}

	/**
	 * Search.
	 *
	 * @param entity the entity
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<AdvanceSearch> search(AdvanceSearch entity) {
		return null;
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<AdvanceSearch> findAll() {
		try {
			return advanceSearchDao.findAll();
		} catch (DaoException e) {
			logger.error(e.getMessage());
			throw new RestException(e);
		}
	}

	/**
	 * Creates the.
	 *
	 * @param anEntity the an entity
	 * @return the advance search
	 * @throws RestException the rest exception
	 */
	@Override
	public AdvanceSearch create(AdvanceSearch anEntity) {
		try {
			return advanceSearchDao.create(anEntity);
		} catch (DaoException e) {
			logger.error(e.getMessage());
			throw new RestException(e);
		}
	}

	/**
	 * Update.
	 *
	 * @param anEntity the an entity
	 * @return the advance search
	 * @throws RestException the rest exception
	 */
	@Override
	public AdvanceSearch update(AdvanceSearch anEntity) {
		try {
			return advanceSearchDao.update(anEntity);
		} catch (DaoException e) {
			logger.error(e.getMessage());
			throw new RestException(e);
		}
	}

	/**
	 * Removes the.
	 *
	 * @param anEntity the an entity
	 * @throws RestException the rest exception
	 */
	@Override
	public void remove(AdvanceSearch anEntity) {
		try {
			advanceSearchDao.delete(anEntity);
		} catch (DaoException e) {
			logger.error(e.getMessage());
			throw new RestException(e);
		}
	}

	/**
	 * Removes the by id.
	 *
	 * @param primaryKey the primary key
	 * @throws RestException the rest exception
	 */
	@Override
	public void removeById(Integer primaryKey) {
		try {
			advanceSearchDao.deleteByPk(primaryKey);
		} catch (DaoException e) {
			logger.error(e.getMessage());
			throw new RestException(e);
		}
	}

	/**
	 * Gets the advance search by name.
	 *
	 * @param name the name
	 * @return the advance search by name
	 * @throws RestException the rest exception
	 */
	@Override
	public List<AdvanceSearch> getAdvanceSearchByName(String name) {
		try {
			logger.info("Getting advance search data for name {} ", name);
			return advanceSearchDao.getAdvanceSearchByName(name);
		} catch (DaoException e) {
			logger.error("DBException in @getAdvanceSearchByName: err={}", e.getMessage());
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Gets the advance search by type list.
	 *
	 * @param name the name
	 * @param type the type
	 * @return the advance search by type list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<AdvanceSearch> getAdvanceSearchByTypeList(String name, List<String> type) {
		try {
			logger.info("Inside getAdvanceSearchByTypeList");
			logger.info("Getting advance search data for name: {} ,type: {} ", name, type);
			return getAdvanceSearchList(name, type);
		} catch (RestException restException) {
			logger.error("RestException while getAdvanceSearchByTypeList {}", restException.getMessage());
			throw restException;
		} catch (Exception e) {
			logger.error("Error while getAdvanceSearchByTypeList {}", Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Search for map data.
	 *
	 * @param id the id
	 * @return the map
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional
	@Override
	public Object searchForMapData(Integer id, Integer zoom) {
		try {
			Map map = new HashMap();
			AdvanceSearch advanceSearch = advanceSearchDao.findByPk(id);
			if (advanceSearch != null) {
				logger.info("advanceSearch fieldType:{}, fieldName:{}",
						advanceSearch.getAdvanceSearchConfiguration().getTypeCategory(), advanceSearch.getName());
				String type = advanceSearch.getAdvanceSearchConfiguration().getTypeCategory().toUpperCase();
				switch (type) {
				case InfraConstants.GEOGRAPHYL1:
				case InfraConstants.GEOGRAPHYL2:
				case InfraConstants.GEOGRAPHYL3:
				case InfraConstants.GEOGRAPHYL4:
					return getGeographyDataByRowKeyPrefix(
							advanceSearch.getAdvanceSearchConfiguration().getTypeCategory(),
							advanceSearch.getRowKeyPrefix(), zoom);
				case ForesightConstants.MACRO:
				case InfraConstants.PICO:
				case InfraConstants.SHOOTER:
				case InfraConstants.IBS:
				case InfraConstants.ODSC:
				case InfraConstants.CORESITE:
					return getSearchedSiteDetailByName(advanceSearch.getName());
				case InfraConstants.NENAME_CAPS:
					return getAdvanceSearchForIPTopology(advanceSearch.getName());
				case InfraConstants.COREGEOGRAPHY:
					if (advanceSearch.getTypereference() != null)
						return iOtherGeographyDao.findByPk(advanceSearch.getTypereference());
					else
						throw new RestException("Invalid type refrence");

				case ForesightConstants.OUTLET:
					if (iAdvanceSearchService != null) {
						return iAdvanceSearchService.searchToMapData(advanceSearch.getName(),
								advanceSearch.getAdvanceSearchConfiguration().getTypeCategory());
					} else {
						return null;
					}
				case ForesightConstants.BUILDING_SEARCH_KEY:
					if (iModuleSearch != null) {
						return iModuleSearch.searchToMapData(advanceSearch.getTypereference());
					} else {
						return null;
					}
				case ForesightConstants.GALLERY_DETAIL_SEARCH_KEY:
				case ForesightConstants.SMILE_SEARCH_KEY:
					if (iGalleryDetailService != null) {
						return iGalleryDetailService
								.searchGallerySmileForAdvanceSearch(advanceSearch.getTypereference());
					}

					break;
				default:
					throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
				}

			} else {
				throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
			}
			return map;
		} catch (Exception e) {
			logger.error("Exception in @searchForMapData : err={}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Gets the searched site detail by name.
	 *
	 * @param neName the ne name
	 * @return the searched site detail by name
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map getSearchedSiteDetailByName(String neName) {
		try {
			NetworkElement networkElement = iNetworkElementDao.searchSiteByName(neName);

			if (networkElement != null && networkElement.getLatitude() != null && networkElement.getLongitude() != null
					&& networkElement.getNeType() != null && networkElement.getNeStatus() != null) {
				Map siteDetailMap = new HashMap();
				siteDetailMap.put(ForesightConstants.NE_NAME, networkElement.getNeName());
				siteDetailMap.put(ForesightConstants.NE_TYPE, networkElement.getNeType().name());
				siteDetailMap.put(ForesightConstants.NE_STATUS, networkElement.getNeStatus());
				siteDetailMap.put(ForesightConstants.LATITUDE, networkElement.getLatitude());
				siteDetailMap.put(ForesightConstants.LONGITUDE, networkElement.getLongitude());
				return siteDetailMap;
			} else {
				throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
			}
		} catch (Exception exception) {
			logger.error("Error while getting site detail. Exception : ", ExceptionUtils.getStackTrace(exception));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Gets the address from google.
	 *
	 * @param searchStr the search str
	 * @return the address from google
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getAddressFromGoogle(String searchStr) {
		logger.info("going to get address from google for string : {}", searchStr);
		return advanceSearchDao.getGoogleResponse(searchStr);
	}

	/**
	 * Search to map data.
	 *
	 * @param id the id
	 * @return the object
	 * @throws RestException the rest exception
	 */
	@Override
	public Object searchToMapData(Integer id) {
		Object object = new Object();
		try {
			AdvanceSearch advanceSearch = advanceSearchDao.findByPk(id);
			if (advanceSearch != null) {
				if (InfraConstants.GEOGRAPHYL1_TABLE
						.equalsIgnoreCase(advanceSearch.getAdvanceSearchConfiguration().getType())) {
					object = geographyL1Dao.getGeographyL1ByName(advanceSearch.getName());
				} else if (InfraConstants.GEOGRAPHYL2_TABLE
						.equalsIgnoreCase(advanceSearch.getAdvanceSearchConfiguration().getType())) {
					object = geographyL2Dao.getGeographyL2ByName(advanceSearch.getName());
				} else if (InfraConstants.GEOGRAPHYL3_TABLE
						.equalsIgnoreCase(advanceSearch.getAdvanceSearchConfiguration().getType())) {
					object = geographyL3Dao.getGeographyL3ByName(advanceSearch.getName());
				} else if (InfraConstants.GEOGRAPHYL4_TABLE
						.equalsIgnoreCase(advanceSearch.getAdvanceSearchConfiguration().getType())) {
					object = geographyL4Dao.getGeographyL4ByName(advanceSearch.getName());
				} else if (ForesightConstants.BTS
						.equalsIgnoreCase(advanceSearch.getAdvanceSearchConfiguration().getType())
						|| ForesightConstants.OUTLET
								.equalsIgnoreCase(advanceSearch.getAdvanceSearchConfiguration().getType())) {
					if (iAdvanceSearchService != null) {
						object = iAdvanceSearchService.searchToMapData(advanceSearch.getName(),
								advanceSearch.getAdvanceSearchConfiguration().getType());
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error while search to map data err msg {}", ExceptionUtils.getStackTrace(e));
		}
		return object;
	}

	@Override
	public List<AdvanceSearch> getAdvanceSearchForIPTopology(String name) {
		try {
			logger.info("Getting advance search data for IP Topology By Name: {} ", name);
			List<String> typeListForIPTopology = new ArrayList<>();
			typeListForIPTopology.add(ForesightConstants.NENAME_UPPERCASE);
			typeListForIPTopology.add(ForesightConstants.SITE);
			typeListForIPTopology.add(ForesightConstants.GEOGRAPHY_L3);
			typeListForIPTopology.add(ForesightConstants.IPV4);
			typeListForIPTopology.add("MACRO_ONAIR");
			return advanceSearchDao.getAdvanceSearchByTypeList(name, typeListForIPTopology, null, null);
		} catch (DaoException e) {
			logger.error("DBException in @getAdvanceSearchForIPTopology: err={}", e.getMessage());
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	private String getGeographyDataByRowKeyPrefix(String geoLevel, String rowKeyPrefix, Integer zoom) {
		try {
			logger.info("Going to fetch Geography data from HBase geoLevel {}, rowKeyPrefix {}, zoom {} ", geoLevel,
					rowKeyPrefix, zoom);
			String URI = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
					+ ConfigUtils.getString(ConfigEnum.DROPWIZARD_BOUNDARY_GEOGRAPHY_URL.getValue())
					+ ForesightConstants.FORWARD_SLASH + geoLevel + ForesightConstants.FORWARD_SLASH + rowKeyPrefix;

			if (zoom != null) {
				URI += ForesightConstants.FORWARD_SLASH + zoom;
			}

			logger.info("URI for getting data from the dropwizard  {}", URI);
			return new HttpGetRequest(URI).getString();
		} catch (Exception e) {
			logger.error("Exception inside getGeographyDataByRowKeyPrefix due to {}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<String> searchNeIdByNeType(String neName, String neType) {
		NEType neTypeValue = NEType.valueOf(neType);
		return iNetworkElementDao.searchNeNameByNeType(neName, neTypeValue);
	}

	private List<AdvanceSearch> getAdvanceSearchList(String name, List<String> type) {
		logger.info("Inside getAdvanceSearchList");
		List<AdvanceSearch> list = new ArrayList<>();
		try {
			String levelType = customerInfo.getFilterGeography().getLevelType();
			/** getting search field type by permission */
			List<String> searchFieldTypes = getSearchFieldTypesByPermission();
			/** finding intersection b/w searchFieldTypes and type */
			List<String> intersect = searchFieldTypes.stream().filter(type::contains).collect(Collectors.toList());
			logger.info("intersect: {} ", intersect);
			if (intersect != null && !intersect.isEmpty()) {
				if (ConfigUtils.getString(InfraConstants.ENABLE_ADVANCESEARCH_AUTH) != null
						&& ConfigUtils.getString(InfraConstants.ENABLE_ADVANCESEARCH_AUTH).equalsIgnoreCase("false")) {
					levelType = UmConstants.NHQ;
				}
				List<Integer> geoId = new ArrayList<>();
				if (levelType.equalsIgnoreCase(UmConstants.SUPER_ADMIN) || levelType.equalsIgnoreCase(UmConstants.NHQ)
						|| levelType.equalsIgnoreCase(UmConstants.SALES_NHQ)) {
					return advanceSearchDao.getAdvanceSearchByTypeList(name, intersect, null, null);
				} else {
					geoId.addAll(customerInfo.getFilterGeography().getGeographyMap().keySet());
					list = advanceSearchDao.getAdvanceSearchByTypeList(name, intersect, levelType, geoId);
				}

			} else {
				return list;
			}

		} catch (RestException restException) {
			logger.error("RestException in getAdvanceSearchList: {}  ", restException.getMessage());
		} catch (Exception e) {
			logger.error("Error in getAdvanceSearchList: {}  ", Utils.getStackTrace(e));
		}
		logger.info("advanceSearch list  :{} ", list);
		return list;
	}

	private List<String> getSearchFieldTypesByPermission() {
		logger.info("Inside getSearchFieldTypesByPermission");
		List<String> searchFieldTypes = new ArrayList<>();

		Set<String> permissionSet = customerInfo.getPermissions();
		if (permissionSet != null && !permissionSet.isEmpty()) {
			List<String> distinctTypeList = advanceSearchDao.getDistinctTypeList();
			if (distinctTypeList != null && !distinctTypeList.isEmpty()) {
				if (ConfigUtils.getString(InfraConstants.ENABLE_ADVANCESEARCH_AUTH) != null
						&& ConfigUtils.getString(InfraConstants.ENABLE_ADVANCESEARCH_AUTH).equalsIgnoreCase("true")) {
					for (String type : distinctTypeList) {
						if (permissionSet.contains(InfraConstants.ROLE_CORE_ADVANCE_SEARCH_CANSEARCH + type))
							searchFieldTypes.add(type);
						else
							logger.info("User don't have permission:  {} ",
									InfraConstants.ROLE_CORE_ADVANCE_SEARCH_CANSEARCH + type);
					}
				} else {
					searchFieldTypes = distinctTypeList;
				}

			} else {
				throw new RestException("There is no advance search type");
			}

		} else {
			throw new RestException("User don't have permission");
		}
		logger.info("searchFieldTypes : {} ", searchFieldTypes);
		return searchFieldTypes;
	}

	@Override
	public Object searchDetails(Map<String, Object> map) {
		logger.info("Inside searchDetails ");
		logger.info("map : {} ", map);
		Object response = null;
		try {
			if (map != null && map.get(InfraConstants.ID) != null) {

				AdvanceSearch advanceSearch = advanceSearchDao.findByPk((Integer) map.get(InfraConstants.ID));

				if (advanceSearch != null) {
					logger.info("advanceSearch fieldType:{}, fieldName:{}",
							advanceSearch.getAdvanceSearchConfiguration().getType(), advanceSearch.getName());
					String beanName = advanceSearch.getAdvanceSearchConfiguration().getBean();
					if (beanName == null || beanName.trim().isEmpty())
						throw new RestException(InfraConstants.DATA_CAN_NOT_SEARCH);

					IAdvanceSearchProvider iadvanceSearchProvider = (IAdvanceSearchProvider) ApplicationContextProvider
							.getApplicationContext().getBean(Class.forName(beanName));
					map.put(InfraConstants.ADVANCESEARCH, advanceSearch);
					if (iadvanceSearchProvider != null) {
						response = iadvanceSearchProvider.getSearchData(map);
					} else {
						throw new RestException(InfraConstants.DATA_CAN_NOT_SEARCH);
					}

				} else {
					throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
				}

			} else {
				throw new RestException(InfraConstants.INVALID_ID);
			}

		} catch (Exception e) {
			logger.error("Error while search data in advance search{}", Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
		return response;
	}

	@Override
	public Object getSearchData(Map<String, Object> map) {
		String response = null;
		try {
			if (map != null && map.get("advanceSearch") != null) {
				AdvanceSearch search = (AdvanceSearch) map.get("advanceSearch");
				response = getGeographyDataByRowKeyPrefix(search.getAdvanceSearchConfiguration().getType(),
						search.getRowKeyPrefix(), (Integer) map.get("zoom"));
			}
		} catch (Exception e) {
			logger.error("Exception while getSearchData : {}", Utils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public List<AdvanceSearch> getAdvanceSearchByTypeListAndVendor(String name, Map<String, List<String>> map)
			throws RestException {
		logger.info("Inside @Class : " + this.getClass().getName() + " {}  ", map);
		List<AdvanceSearch> list = new ArrayList<>();
		try {
			if (map != null) {
				if (map.get("vendor") != null) {
					list = getAdvanceSearchListByVendorName(name, map);
					return list;
				}
			} else {
				logger.info("Inside @Class :" + this.getClass().getName() + " Vendor is not vailable ");
				list = getAdvanceSearchList(name, map.get("type"));
				return list;
			}

		} catch (RestException restException) {
			logger.error("RestException while getAdvanceSearchByTypeList {}", restException.getMessage());
			throw restException;
		} catch (Exception e) {
			logger.error("Error while getAdvanceSearchByTypeList {}", Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
		return list;

	}

	private List<AdvanceSearch> getAdvanceSearchListByVendorName(String name, Map<String, List<String>> map)
			throws RestException, DaoException {
		logger.info("Inside @Class : " + this.getClass().getName() + "name {} :, type {} :, vendor {} :", name);
		List<AdvanceSearch> list = new ArrayList<>();
		List<String> intersection = null;
		List<String> type = new ArrayList<>();
		List<String> vendor = new ArrayList<>();
		try {
			type = map.get("type");
			vendor = map.get("vendor");
			List<String> userOrganizationType = new ArrayList<>();
			userOrganizationType.add(customerInfo.getVendor());
			logger.info("User Organization : {} ", userOrganizationType);
			if (ConfigUtils.getBoolean("VENDOR_CHECK").equals(true)) {
				intersection = vendor.stream().filter(userOrganizationType::contains).collect(Collectors.toList());
			} else {
				intersection = new ArrayList<>(vendor);
			}
			if (intersection != null && !intersection.isEmpty()) {
				String levelType = customerInfo.getFilterGeography().getLevelType();
				List<String> searchFieldTypes = getSearchFieldTypesByPermission();
				logger.info(" levelType {} , searchFieldTypes {} ", levelType, searchFieldTypes);
				List<String> intersect = searchFieldTypes.stream().filter(type::contains).collect(Collectors.toList());
				logger.info("intersect b/w searchFieldTypes and type: {} ", intersect);
				if (intersect != null && !intersect.isEmpty()) {
					List<Integer> geoId = new ArrayList<>();
					if (levelType.equalsIgnoreCase(UmConstants.SUPER_ADMIN)
							|| levelType.equalsIgnoreCase(UmConstants.NHQ)
							|| levelType.equalsIgnoreCase(UmConstants.SALES_NHQ)) {
						list = advanceSearchDao.getAdvanceSearchByTypeListAndVendorType(name, intersect, null, null,
								intersection, map);
					} else {
						geoId.addAll(customerInfo.getFilterGeography().getGeographyMap().keySet());
						list = advanceSearchDao.getAdvanceSearchByTypeListAndVendorType(name, intersect, levelType,
								geoId, intersection, map);
					}

				} else {
					logger.info("intersection b/w searchFieldTypes and type not match");
					throw new RestException("Invalid search field type");
				}
			} else {
				logger.info("Intersection b/w vendor and userOrganizationType is null");
				return list;
			}
		} catch (Exception e) {
			logger.error("Error in getAdvanceSearchList: {}  ", Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return list;
	}

	@Override
	public void createAdvanceSearch(NetworkElement networkElement, String advanceSearchType, String vendor,
			String domain) {
		logger.info("inside createAdvaneSearch method advanceSearchType:{},vendor :{},domain :{}", advanceSearchType,
				vendor, domain);
		try {
//			List<AdvanceSearch> advancesSerachlist = advanceSearchDao.getAdvanceSearchByTypeListAndVendorType(
//					networkElement.getNeName(), Arrays.asList(advanceSearchType), null, null, null);

			AdvanceSearch advancesSerach = advanceSearchDao.getAdvanceSearchBySearchFieldNameAndTypeList(
					networkElement.getNeName(), Arrays.asList(advanceSearchType));
			if (networkElement != null && advanceSearchType != null) {
				if (advancesSerach != null) {
					logger.info("advance Search  already created for serach field name :{}",
							networkElement.getNeName());
				} else {
					logger.info("going to create entry");
					AdvanceSearch advSearch = new AdvanceSearch();
					AdvanceSearchConfiguration advanceSerachConfigObject = iAdvanceSearchConfigurationDao
							.getAdvanceSearchConfigurationByType(advanceSearchType);
					advSearch.setName(networkElement.getNeName());
					advSearch.setGeographyL4(networkElement.getGeographyL4());
					advSearch.setAdvanceSearchConfiguration(advanceSerachConfigObject);
					advSearch.setPriorityValue(InfraConstants.ADVANCE_SEARCH_PRIORITY_VALUE);
					advSearch.setVendor(vendor);
					advSearch.setDomain(domain);
					advSearch.setTypereference(networkElement.getId());
					advSearch.setCreationTime(new Date());
					advSearch.setDisplayName(networkElement.getNeName());
					advanceSearchDao.create(advSearch);
				}
			} else {
				logger.info("getting empty value for advanceSearchType:{}", advanceSearchType);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred while creating AdvanceSearch");
			throw new DaoException("Exception Occurred while creating AdvanceSearch");
		}
	}

	@Override
	public void createAdvanceSearch(NetworkElement networkElement, String advanceSearchType) {
		// TODO Auto-generated method stub

	}
}
