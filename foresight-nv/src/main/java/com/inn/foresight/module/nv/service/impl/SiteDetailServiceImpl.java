package com.inn.foresight.module.nv.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import com.inn.commons.lang.NumberUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.LatLng;
import com.inn.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.INEBandDetailDao;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.dao.IRANDetailDao;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.utils.CriteriaUtils;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.SiteDataWrapper;
import com.inn.foresight.core.infra.wrapper.WifiWrapper;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.coverage.wrapper.CellDetailWrapper;
import com.inn.foresight.module.nv.coverage.wrapper.SiteDetailWrapper;
import com.inn.foresight.module.nv.dao.SiteDetailDao;
import com.inn.foresight.module.nv.layer.wrapper.CoverageLayerWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.service.ISiteDetailService;
import com.inn.foresight.module.nv.utils.SiteDetailConstant;
import com.inn.foresight.module.nv.utils.SiteDetailUtils;

@Service("SiteDetailServiceImpl")
public class SiteDetailServiceImpl implements ISiteDetailService {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(SiteDetailServiceImpl.class);

	@Autowired
	private INetworkElementDao networkElementDao;

	@Autowired
	private IRANDetailDao ranDetailDao;

	@Autowired
	private INEBandDetailDao bandDetailDao;

	@Autowired
	private SiteDetailDao siteDetailDao;
	

	@Override
	public List<SiteInformationWrapper> getMacroSiteDetailsForCellLevelForReport(Map<String, Double> viewPortMap,
			List<String> technologyList, List<String> neNamesList, Map<String, List<String>> geographyMap,
			boolean isNeighbourSite, boolean needOnlyMacroCells, boolean needOnlyOnAirSites) {

		logger.info("Inside Method getMacroSiteDetailsForCellLevelForReport(), viewportMap: {}",
				viewPortMap != null ? new Gson().toJson(viewPortMap) : viewPortMap);
		Map<String, List<Map>> filterMap = new HashMap<>();
		List<Map> neFilterList = new ArrayList<>();

		List<Object[]> cellDataAray = null;
		if (Utils.isValidList(neNamesList)) {
			List<Map> neParentFilterList = new ArrayList<>();
			neParentFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.FILTER_COLUMNS_NE_NAME, SiteDetailConstant.FILTER_OPERATION_IN,
					SiteDetailConstant.FILTER_DATA_TYPE_STRING, neNamesList));
			filterMap.put(InfraConstants.PARENT_NETWORK_ELEMENT, neParentFilterList);
			logger.info("Going to invoke method getNECellDetailData: {}",neNamesList != null ? new Gson().toJson(neNamesList) : neNamesList);
			SiteInformationWrapper siteInformationWrapper = getNECellDetailData(neNamesList,
					new SiteInformationWrapper());
			cellDataAray = siteInformationWrapper.getListofObject();

		}
		if (Utils.isValidList(technologyList)) {
			neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.FILTER_COLUMNS_NE_TECHNOLOGY, SiteDetailConstant.FILTER_OPERATION_IN,
					SiteDetailConstant.FILTER_DATA_TYPE_STRING, technologyList));
		}

		if (geographyMap != null && !geographyMap.isEmpty()) {
			filterMap.putAll(SiteDetailUtils.getGeographyFilterFromGeographyMap(geographyMap));
		}

		if (needOnlyOnAirSites) {
			Map neStatusFilter = new Gson().fromJson(SiteDetailConstant.NE_FILTER_NE_STATUS_ON_AIR, Map.class);
			neFilterList.add(neStatusFilter);
		}

		if (needOnlyMacroCells) {
			Map neTypeFilter = new Gson().fromJson(SiteDetailConstant.NE_FILTER_NE_TYPE_MACRO_CELL, Map.class);
			neFilterList.add(neTypeFilter);
		}

		filterMap.put(SiteDetailConstant.TN_NETWORK_ELEMENT, neFilterList);

		Map<String, List<String>> projection = new HashMap<>();
		projection.put(SiteDetailConstant.TN_NETWORK_ELEMENT, SiteDetailUtils.getNeProjectionsForReports());
		projection.put(SiteDetailConstant.TN_RAN_DETAIL, SiteDetailUtils.getRANProjectionsForReports());
		projection.put(SiteDetailConstant.TN_GEOGRAPHY_L4, SiteDetailUtils.getGeographyNameProjection());
		projection.put(SiteDetailConstant.TN_GEOGRAPHY_L3, SiteDetailUtils.getGeographyNameProjection());
		projection.put(SiteDetailConstant.TN_GEOGRAPHY_L2, SiteDetailUtils.getGeographyNameProjection());
		projection.put(SiteDetailConstant.TN_GEOGRAPHY_L1, SiteDetailUtils.getGeographyNameProjection());

		List<Tuple> siteData = ranDetailDao.getCellsDataForRAN(viewPortMap, filterMap, projection, false);

		return SiteDetailUtils.convertTupleToSiteInformationWrapper(InfraUtils.getMapFromTupleList(siteData),
				isNeighbourSite, cellDataAray);
	}

	@Override
	public SiteInformationWrapper getNECellDetailData(List<String> neNamesList, SiteInformationWrapper wrapper) {
		try {
			logger.info("SiteDataWrapper ===== {}", wrapper);
			return wrapper;
		} catch (Exception e) {
			logger.error("Exception inside the method  getNECellDetailData {}", Utils.getStackTrace(e));
		}
		return null;

	}

	@Override
	public List<Object[]> getLocationForCellIdMncMcc() {
		return networkElementDao.getLocationForCellIdMncMcc();
	}

	@Override
	public List<RANDetail> findAllRANDetail() {
		 return ranDetailDao.findAll();
	}

	@Override
	public List<SiteDataWrapper> getNECellsBySapIds(List<String> var1) {

		logger.info("inside method getNECellsBySapIds with neNames: {}",var1 != null ? new Gson().toJson(var1) : var1);

		Map<String, List<Map>> filterMap = new HashMap<>();
		List<Map> neFilterList = new ArrayList<>();
		neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_NE_NAME,
				SiteDetailConstant.FILTER_OPERATION_IN, SiteDetailConstant.FILTER_DATA_TYPE_STRING, var1));

		filterMap.put(InfraConstants.PARENT_NETWORK_ELEMENT, neFilterList);

		Map<String, List<String>> projection = new HashMap<>();
		List<String> neProjectionList = new ArrayList<>();
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_ENBID);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_FREQUENCY);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_NAME);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_ID);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_CELL_ID);
		projection.put(SiteDetailConstant.TN_NETWORK_ELEMENT, neProjectionList);

		return SiteDetailUtils.convertTupleListToSiteDataWrapper(
				InfraUtils.getMapFromTupleList(ranDetailDao.getCellsDataForRAN(null, filterMap, projection, false)));
	}

	@Override
	public LatLng getLocationByCGIAndPci(Integer var1, Integer var2) {
		logger.info("inside method getLocationByCGIAndPci, CGI: {}, PCI: {}", var1, var2);
		Map<String, List<String>> projection = new HashMap<>();
		List<String> neProjectionList = new ArrayList<>();
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_LATITUDE);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_LONGITUDE);
		projection.put(SiteDetailConstant.TN_NETWORK_ELEMENT, neProjectionList);

		Map<String, List<Map>> filterMap = new HashMap<>();
		List<Map> ranFilterList = new ArrayList<>();
		ranFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_RAN_CGI,
				SiteDetailConstant.FILTER_OPERATION_EQUALS, SiteDetailConstant.FILTER_DATA_TYPE_INTEGER,
				String.valueOf(var1)));
		ranFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_RAN_PCI,
				SiteDetailConstant.FILTER_OPERATION_EQUALS, SiteDetailConstant.FILTER_DATA_TYPE_INTEGER,
				String.valueOf(var2)));
		filterMap.put(SiteDetailConstant.TN_RAN_DETAIL, ranFilterList);

		List<Tuple> tuples = ranDetailDao.getCellsDataForRAN(null, filterMap, projection, false);
		if (tuples != null && !tuples.isEmpty()) {
			Tuple t1 = tuples.get(0);
			return SiteDetailUtils.getLatLngIfExists(t1);
		}
		return new LatLng();
	}

	@Override
	public List<NetworkElement> getWifiAPDetailByFloorId(List<Integer> var1) {
		return networkElementDao.getWifiAPDetailByFloorId(var1);
	}

	@Override
	public List<Map<String, String>> getSiteLocationByCGI(Integer var1) {

		Map<String, List<String>> projection = new HashMap<>();
		List<String> neProjectionList = new ArrayList<>();
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_LATITUDE);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_LONGITUDE);
		projection.put(SiteDetailConstant.TN_NETWORK_ELEMENT, neProjectionList);
		List<String> ranprojectionList = new ArrayList<>();
		ranprojectionList.add(SiteDetailConstant.RAN_PROJECTION_AZIMUTH);
		projection.put(SiteDetailConstant.TN_RAN_DETAIL, ranprojectionList);

		Map<String, List<Map>> filterMap = new HashMap<>();
		List<Map> ranFilterList = new ArrayList<>();
		ranFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_RAN_CGI,
				SiteDetailConstant.FILTER_OPERATION_EQUALS, SiteDetailConstant.FILTER_DATA_TYPE_INTEGER,
				String.valueOf(var1)));

		filterMap.put(SiteDetailConstant.TN_RAN_DETAIL, ranFilterList);
		List<Tuple> tuples = ranDetailDao.getCellsDataForRAN(null, filterMap, projection, true);
		if (tuples != null && !tuples.isEmpty()) {
			List<Map<String, String>> response = new ArrayList<>();
			for (Tuple tuple : tuples) {
				Map<String, String> macroDetail = new HashMap<>();
				macroDetail.put(ForesightConstants.LATITUDE,
						String.valueOf(tuple.get(SiteDetailConstant.NE_PROJECTION_LATITUDE, Double.class)));
				macroDetail.put(ForesightConstants.LONGITUDE,
						String.valueOf(tuple.get(SiteDetailConstant.NE_PROJECTION_LONGITUDE, Double.class)));
				macroDetail.put(ForesightConstants.AZIMUTH,
						String.valueOf(tuple.get(SiteDetailConstant.RAN_PROJECTION_AZIMUTH, Integer.class)));
				if (!macroDetail.isEmpty()) {
					response.add(macroDetail);
				}
			}
			return response;
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<SiteDetailWrapper> getSiteDetailByCGI(Integer cgi,Boolean isCountRequired) {

		Map<String, String> responseMap = siteDetailDao.getNENameByCgi(cgi);
		if (MapUtils.isNotEmpty(responseMap)) {
			
			String siteName = responseMap.get("siteName");
			String parentName = responseMap.get("parentName");
			
			Map<String, List<Map>> neFilterForCell = SiteDetailUtils.getNEFilter(null,
					null, Arrays.asList(siteName));
			
			Map<String, List<String>> projectionForCell = new HashMap<>();
			projectionForCell.put(SiteDetailConstant.TN_NETWORK_ELEMENT, SiteDetailUtils.getNEProjectionsForCoverage(true));
			projectionForCell.put(SiteDetailConstant.TN_RAN_DETAIL, SiteDetailUtils.getRANProjectionsForCoverage());
			
			List<Tuple> tuples =  ranDetailDao.getCellsDataForRAN(null, neFilterForCell,
					projectionForCell, false);
			
			List<Map<Object, Object>> tupleList = InfraUtils.getMapFromTupleList(tuples);

			Map<String, SiteDetailWrapper> siteDetailMap = SiteDetailUtils.convertTupleToSiteDetailWrapper(tupleList,responseMap);

			if (isCountRequired != null && isCountRequired) {
				try {
					Optional<Entry<String, SiteDetailWrapper>> siteEntry = siteDetailMap.entrySet().stream()
							.findFirst();
					if (siteEntry.isPresent()) {
						logger.info("siteDetail {}", siteEntry.get() );
						Entry<String, SiteDetailWrapper> entry = siteEntry.get();
						List<CellDetailWrapper> cells = entry.getValue().getCells();
						Optional<CellDetailWrapper> cellWrapper = cells.stream()
								.filter(c -> c.getCgi().equals(cgi)).findFirst();
						if (cellWrapper.isPresent()) {
							logger.info("cellDetail {}", cellWrapper.get() );
							CellDetailWrapper cellDetail = cellWrapper.get();
							logger.info("Going to get Site for parent nename {} and cellnum {}",parentName,cellDetail.getCellNum());
							Map<String, String> cellKPICounters = getCellKPICountersFromVendor(parentName,
									cellDetail.getCellNum());
							cellDetail.setActiveUsers(cellKPICounters.get("au"));
						}
					}
				} catch (Exception exception) {
					logger.info("Exception while getting active user count {}", Utils.getStackTrace(exception));
				}
			}
			
				
			return new ArrayList(siteDetailMap.values());
			}
		return Collections.emptyList();
	}
	
	

	@Override
	public List<Integer> getDistinctPCIBySiteIdAndBand(String neName, List<String> bandList) {

		Map<String, List<Map>> filterMap = new HashMap<>();
		List<Map> neFilterList = new ArrayList<>();
		List<Map> neParentFilterList = new ArrayList<>();
		Map<String, List<String>> projection = new HashMap<>();

		if (!StringUtils.isBlank(neName)) {
			neParentFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.FILTER_COLUMNS_NE_NAME, SiteDetailConstant.FILTER_OPERATION_EQUALS,
					SiteDetailConstant.FILTER_DATA_TYPE_STRING, neName));
		}
		if (Utils.isValidList(bandList)) {
			neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.FILTER_COLUMNS_NE_FREQUENCY, SiteDetailConstant.FILTER_OPERATION_IN,
					SiteDetailConstant.FILTER_DATA_TYPE_STRING, bandList));
		}
		filterMap.put(InfraConstants.NETWORKELEMENT_TABLE, neParentFilterList);
		filterMap.put(InfraConstants.NEBANDDETAIL_TABLE, neFilterList);

		List<String> ranprojectionList = new ArrayList<>();
		ranprojectionList.add(SiteDetailConstant.RAN_PROJECTION_PCI);
		projection.put(SiteDetailConstant.TN_RAN_DETAIL, ranprojectionList);

		List<Map<String, Object>> siteDataList = InfraUtils.getMapFromTupleList(
				bandDetailDao.getSiteBandDetail(null, filterMap, projection, new ArrayList<>(), false, null, true));

		logger.info("Returned PCI Data from Tuple: {}", siteDataList != null ? new Gson().toJson(siteDataList):siteDataList );

		List<Integer> pciList = new ArrayList<>();
		for (Map<String, Object> mapData : siteDataList) {
			if (mapData != null && !mapData.isEmpty() && mapData.containsKey(SiteDetailConstant.RAN_PROJECTION_PCI)) {
				pciList.add((Integer) mapData.get(SiteDetailConstant.RAN_PROJECTION_PCI));
				if (Utils.isValidList(pciList)) {
					pciList = pciList.stream().distinct().collect(Collectors.toList());
				}
			}
		}

		return pciList;
	}

	@Override
	public List<NetworkElementWrapper> getSitesFromViewPort(Map<String, Double> viewPortMap, List<String> nestatusList,
			List<String> neTypeList, Integer limit) {

		logger.info("Inside Method getSitesFromViewPort and  viewportMap: {}", viewPortMap != null ? new Gson().toJson(viewPortMap): viewPortMap);

		Map<String, List<Map>> filterMap = new HashMap<>();
		List<Map> neFilterList = new ArrayList<>();

		List<Tuple> siteData =  getCellsDataFromViewport(nestatusList, neTypeList, filterMap,
				neFilterList,viewPortMap,false);

		return SiteDetailUtils.convertTupleToWrapperForVisualization(InfraUtils.getMapFromTupleList(siteData));
	}

	private List<Tuple> getCellsDataFromViewport(List<String> nestatusList, List<String> neTypeList,
			Map<String, List<Map>> filterMap, List<Map> neFilterList,Map<String,Double> viewPortMap,Boolean isBandOnairDate) {
		if (CollectionUtils.isEmpty(nestatusList)) {
			nestatusList = Arrays.asList(NEStatus.ONAIR.name());

		}
		if (CollectionUtils.isEmpty(neTypeList)) {
			neTypeList = Arrays.asList(NEType.MACRO_CELL.name());

		}

		neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_NE_STATUS,
				SiteDetailConstant.FILTER_OPERATION_IN, SiteDetailConstant.FILTER_COLUMNS_DATATYPE_NE_STATUS,
				nestatusList));

		neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_NE_TYPE,
				SiteDetailConstant.FILTER_OPERATION_IN, SiteDetailConstant.FILTER_COLUMNS_DATATYPE_NE_TYPE,
				neTypeList));

		filterMap.put(SiteDetailConstant.TN_NETWORK_ELEMENT, neFilterList);

		Map<String, List<String>> projection = new HashMap<>();
		projection.put(SiteDetailConstant.TN_NETWORK_ELEMENT, SiteDetailUtils.getNeProjectionsForReports());
		projection.put(SiteDetailConstant.TN_RAN_DETAIL, SiteDetailUtils.getRANProjectionsForReports());
		if(isBandOnairDate.booleanValue()) {
		projection.put(SiteDetailConstant.TN_NEBAND_DETAIL, SiteDetailUtils.getProjectionNEBandDetail());
		}
		return  ranDetailDao.getCellsDataForRAN(viewPortMap, filterMap, projection, false);
	}

	@Override
	public List<SiteDetailWrapper> getSitesDetail(Long timestamp) {
		List<String> neType = Arrays.asList(NEType.MACRO.name());
		List<String> neStatus = Arrays.asList(NEStatus.ONAIR.name(), NEStatus.PLANNED.name());

		Map<String, List<Map>> neFilterForCoverage = SiteDetailUtils.getNEFilterForCoverage(timestamp, neType, neStatus,
				null,false);

		Map<String, List<String>> projection = new HashMap<>();
		projection.put(SiteDetailConstant.TN_NETWORK_ELEMENT, SiteDetailUtils.getNEProjectionsForCoverage(false));
		List<Tuple> neData = networkElementDao.searchNEDetail(neFilterForCoverage, projection, null, null, false, null);
		neType = Arrays.asList(NEType.MACRO_CELL.name());
		Map<String, List<Map>> neFilterForCell = SiteDetailUtils.getNEFilterForCoverage(timestamp, neType, neStatus,
				null,false);
		Map<String, List<String>> projectionForCell = new HashMap<>();
		projectionForCell.put(SiteDetailConstant.TN_NETWORK_ELEMENT, SiteDetailUtils.getNEProjectionsForCoverage(false));
		projectionForCell.put(SiteDetailConstant.TN_RAN_DETAIL, SiteDetailUtils.getRANProjectionsForCoverage());
		List macroCellList = (List<Tuple>) ranDetailDao.getCellsDataForRAN(null, neFilterForCell, projectionForCell,
				false);
		

		List<Tuple> customNEDetail = getTIReadyCells(timestamp, neType, projectionForCell,null);
		neData.addAll(customNEDetail);
		logger.info("Data Found For Cusotm TI READY Cell {}, " ,customNEDetail.size());
		logger.info(macroCellList.size());
		neData.addAll(macroCellList);
		getCellsFromRANDetail(timestamp, neType, neStatus, neData);

		Map<String, SiteDetailWrapper> siteDetailMap = SiteDetailUtils
				.convertTupleToSiteDetailWrapper(InfraUtils.getMapFromTupleList(neData),null);

		updateCellsForSites(timestamp, neStatus, siteDetailMap);
		
		
		return new ArrayList(siteDetailMap.values());

	}

	private List<Tuple> getTIReadyCells(Long timestamp, List<String> neType, Map<String, List<String>> projectionForCell,Map<String,Double> viewPortMap) {
		Map<String, List<Map>> customNEFilterForCoverage = SiteDetailUtils.getNEFilterForCoverage(timestamp, neType, Arrays.asList(NEStatus.PLANNED.name()),
				null,true);
		projectionForCell.put(SiteDetailConstant.CUSTOM_NETWORK_ELEMENT, Arrays.asList(SiteDetailConstant.CUSTOM_NE_PROJECTION_TIREADY,SiteDetailConstant.TI_READY_DATE));
		projectionForCell.put(SiteDetailConstant.TN_NETWORK_ELEMENT, SiteDetailUtils.getNEProjectionsForCoverage(false));
		projectionForCell.put(SiteDetailConstant.TN_RAN_DETAIL, SiteDetailUtils.getRANProjectionsForCoverage());
		return ranDetailDao.getCellsDataForRAN(viewPortMap, customNEFilterForCoverage, projectionForCell,
				false);
	}


	private void getCellsFromRANDetail(Long timestamp, List<String> neType, List<String> neStatus, List<Tuple> neData) {
		if (timestamp != 0L) {
			Map<String, List<String>> projectionForRAN = new HashMap<>();
			projectionForRAN.put(SiteDetailConstant.TN_NETWORK_ELEMENT, SiteDetailUtils.getNEProjectionsForCoverage(false));
			projectionForRAN.put(SiteDetailConstant.TN_RAN_DETAIL, SiteDetailUtils.getRANProjectionsForCoverage());
			Map<String, List<Map>> neFilterRAN = SiteDetailUtils.getRANFilterForCoverage(timestamp, neType, neStatus);

			List<Tuple> macroRANList = ranDetailDao.getCellsDataForRAN(null, neFilterRAN, projectionForRAN,
					false);
			
			neData.addAll(macroRANList);
		}
	}

	private void updateCellsForSites(Long timestamp, List<String> neStatus,
			Map<String, SiteDetailWrapper> siteDetailMap) {

		if (timestamp != 0L) {
			Map<String, List<String>> projection = new HashMap();
			List<String> neNamesList = new ArrayList(siteDetailMap.keySet());
			List<String> neType = Arrays.asList(new String[] { NEType.MACRO_CELL.name() });
			if (CollectionUtils.isNotEmpty(neNamesList)) {
				Map<String, List<Map>> neFilter = SiteDetailUtils.getNEFilterForCoverage(timestamp, neType, neStatus,
						neNamesList,false);

				projection.put(SiteDetailConstant.TN_RAN_DETAIL, SiteDetailUtils.getRANProjectionsForCoverage());
				projection.put(SiteDetailConstant.TN_NETWORK_ELEMENT, SiteDetailUtils.getNEProjectionsForCoverage(false));

				List cellList = (List<Tuple>) ranDetailDao.getCellsDataForRAN(null, neFilter, projection, false);

				logger.info("CELL LIST SIZE {}",cellList.size());
				Map<String, SiteDetailWrapper> siteData = SiteDetailUtils
						.convertTupleToSiteDetailWrapper(InfraUtils.getMapFromTupleList(cellList),null);

				for (Entry<String, SiteDetailWrapper> siteEntry : siteData.entrySet()) {
					String neName = siteEntry.getKey();

					if (siteDetailMap.containsKey(neName)) {
						List<CellDetailWrapper> cells = siteDetailMap.get(neName).getCells();
						for (CellDetailWrapper cellWrapper : siteEntry.getValue().getCells()) {
							if (cells.stream().noneMatch(c -> c.getCgi() != null && cellWrapper.getCgi() != null
									&& c.getCgi().compareTo(cellWrapper.getCgi()) == 0)) {
								cells.add(cellWrapper);
							}
						}
					} else {
						siteDetailMap.put(neName, siteEntry.getValue());
					}
				}
			}
		}
	}

	@Override
	public WifiWrapper getWIFIAPDetail(String macAddress) {

		return  ranDetailDao.getWIFIAPDetail(macAddress);

	}

	@Override
	public SiteInformationWrapper getSiteInformationFromCgi(Integer cgi) {
		SiteInformationWrapper wrapper = new SiteInformationWrapper();
		if (cgi != null) {
			Map<String, List<String>> projection = new HashMap<>();
			List<String> ranProjectionList = new ArrayList<>();
			List<String> neProjectionList = new ArrayList<>();
			Map<String, List<Map>> filterMap = new HashMap<>();

			ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_CGI);
			neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_NAME);
			projection.put(SiteDetailConstant.TN_RAN_DETAIL, ranProjectionList);
			projection.put(SiteDetailConstant.TN_NETWORK_ELEMENT, neProjectionList);

			List<Map> ranFilterList = new ArrayList<>();
			ranFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_RAN_CGI,
					SiteDetailConstant.FILTER_OPERATION_EQUALS, SiteDetailConstant.FILTER_DATA_TYPE_INTEGER,
					String.valueOf(cgi)));
			wrapper.setCgi(cgi);
			filterMap.put(SiteDetailConstant.TN_RAN_DETAIL, ranFilterList);
			List<Tuple> tuples = ranDetailDao.getCellsDataForRAN(null, filterMap, projection, true);
			if (tuples != null && !tuples.isEmpty()) {
				for (Tuple tuple : tuples) {
					String siteName = (String) tuple.get(SiteDetailConstant.NE_PROJECTION_PARENT_NE_NAME);
					if (siteName != null) {
						wrapper.setSiteName(siteName);
						break;
					}
				}

			}
			return wrapper;
		}
		return null;
	}
	
	
	
	

	private void getData(Integer cgi){
		siteDetailDao.getNENameByCgi(cgi);
	}
	
	public Map<String,String> getCellKPICountersFromVendor(String nename,Integer cellNum){
		Map<String,String> map=new HashMap<>();
		try {		
		String microserviceUrl=ConfigUtil.getConfigProp(ConfigEnum.MICRO_SERVICE_BASE_URL.name())
				+NVConstant.ONDEMAND_KPI_COUNTER_API+ForesightConstants.QUESTIONMARK+"nename="+
				nename+ForesightConstants.AMPERSAND+"timestamp="
				+new Date().getTime()
				+ForesightConstants.AMPERSAND+"cellNum="+cellNum;
		
		logger.info("microservice url for on Demand kpis {}" ,microserviceUrl);
		HttpGetRequest get=new HttpGetRequest(microserviceUrl);
		
			String response=get.getString();
			if(StringUtils.isNotBlank(response)) {
				logger.info("response from OnDemand KPI {}", response);
				 map= new Gson().fromJson(response, new TypeToken<Map<String,String>>(){}.
						getType());
				return map;
			}
		} catch (HttpException e) {
			logger.info("Exception inside getCellKPICountersFromVendor{}",Utils.getStackTrace(e));
		}
		return map;
		
		
	}

	@Override
	public List<SiteDetailWrapper> getSitesDetailFromViewPort(CoverageLayerWrapper wrapper) {
		Map<String, List<Map>> filterMap = new HashMap<>();
		List<Map> neFilterList = new ArrayList<>();
		List<String> neType = Arrays.asList(new String[] {NEType.MACRO_CELL.name()});
		Map<String, Double> viewPortMap = SiteDetailUtils.getViewPortMap(wrapper);
		List<Tuple> cellsData = getCellsDataFromViewport(wrapper.getNeStatusList(),neType,filterMap, neFilterList,viewPortMap,true);
		
		Map<String, List<String>> projection = new HashMap<>();
		projection.put(SiteDetailConstant.TN_NETWORK_ELEMENT, SiteDetailUtils.getNeProjectionsForReports());
		projection.put(SiteDetailConstant.TN_RAN_DETAIL, SiteDetailUtils.getRANProjectionsForReports());
		if(wrapper.getIsTiReadyRequired()!=null&&wrapper.getIsTiReadyRequired()) {
			List<Tuple> tiReadyCells = getTIReadyCells(0L, neType, projection,viewPortMap);
			cellsData.addAll(tiReadyCells);
			}	
		Map<String, SiteDetailWrapper> siteDetailWrapperMap = SiteDetailUtils.getSitesDataFromTupleMap(InfraUtils.getMapFromTupleList(cellsData),null);
		return new ArrayList(siteDetailWrapperMap.values());
		

		 
	}


	@Override
	public SiteInformationWrapper getNetworkElementByNename(String nename){
		logger.error("Going to get Information for nename {}",nename);
		Map<String, List<Map>> neFilterByNename = SiteDetailUtils.getNEFilterForNename(nename);
		Map<String, List<String>> projection = new HashMap<>();
		projection.put(SiteDetailConstant.TN_NETWORK_ELEMENT, SiteDetailUtils.getNeProjectionsForReports());
		projection.put(SiteDetailConstant.TN_GEOGRAPHY_L4, SiteDetailUtils.getGeographyNameProjection());
		List<Tuple> neData = networkElementDao.searchNEDetail(neFilterByNename, projection, null, null, false, null);

		List<SiteInformationWrapper> siteInformationWrappers=SiteDetailUtils.convertTupleToSiteInformationWrapper(InfraUtils.getMapFromTupleList(neData),
				false, null);

		return siteInformationWrappers.get(NumberUtils.INTEGER_ZERO);
	}
	
	
}
