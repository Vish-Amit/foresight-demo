package com.inn.foresight.module.nv.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.maps.LatLng;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.utils.CriteriaUtils;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.SiteDataWrapper;
import com.inn.foresight.module.nv.coverage.wrapper.CellDetailWrapper;
import com.inn.foresight.module.nv.coverage.wrapper.SiteDetailWrapper;
import com.inn.foresight.module.nv.layer.wrapper.CoverageLayerWrapper;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;

public class SiteDetailUtils {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(SiteDetailUtils.class);

	private SiteDetailUtils() {
		// Empty Constructor
	}

	public static List<SiteInformationWrapper> convertTupleToSiteInformationWrapper(List<Map<Object, Object>> siteData,
			boolean isNeighbourSite, List<Object[]> cellDataArray) {
		logger.info("inside the method convertTupleToSiteInformationWrapper");
		List<SiteInformationWrapper> siteInfoList = new ArrayList<>();
		try {
			siteData.forEach(singleSiteData -> {
				if (singleSiteData != null && !singleSiteData.isEmpty()) {
					SiteInformationWrapper siteWrapper = new SiteInformationWrapper();
					siteWrapper.setIsNeighbourSite(isNeighbourSite);
					setMacroSiteDetailIntoSiteWrapper(singleSiteData, siteWrapper);
					setGeographyAndNetworkElementDetail(singleSiteData, siteWrapper);
					setNeCellDataInlist(cellDataArray, siteWrapper);
					siteInfoList.add(siteWrapper);
				}
			});

		} catch (Exception e) {
			logger.error("error inside the method convetMicroSiteDetailToSiteInfo {}", Utils.getStackTrace(e));
		}
		return siteInfoList;
	}

	private static void setNeCellDataInlist(List<Object[]> cellDataArray, SiteInformationWrapper siteWrapper) {
		if (cellDataArray != null && !cellDataArray.isEmpty()) {
			for (Object[] result : cellDataArray) {
				try {
					String neName = (String) result[5];
					logger.info("setNeCellDataInlist  neName {} {}", neName, siteWrapper.getNename());
					if (neName != null && neName.equalsIgnoreCase(siteWrapper.getNename())) {
						siteWrapper.setGroundToRooftopAntHight(result[0] != null ? (Double) result[0] : null);
						siteWrapper.setRooftopToAntAntHight(result[1] != null ? (Double) result[1] : null);
						Double plannedAzumith = result[2] != null ? (Double) result[2] : null;
						if (plannedAzumith != null) {
							siteWrapper.setPlanAzimuth(plannedAzumith.intValue());
						}

						siteWrapper.setDesignedFddETilt(result[3] != null ? String.valueOf(result[3]) : null);
						siteWrapper.setDesignedTddETilt(result[3] != null ? String.valueOf(result[3]) : null);
						siteWrapper.setDesignedMTilt(result[4] != null ? String.valueOf(result[4]) : null);
						siteWrapper.setRooftopToAntAntHight(result[6] != null ? (Double) result[6] : null);
						siteWrapper.setDesignedAntHight(result[7] != null ? (Double) result[7] : null);
					}

				} catch (Exception e) {
					logger.error("Exception inside setNeCellDataInlist {}", Utils.getStackTrace(e));
				}
			}
		}
	}

	private static void setMacroSiteDetailIntoSiteWrapper(Map<Object, Object> siteData,
			SiteInformationWrapper siteWrapper) {
		siteWrapper
				.setId((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_ID));
		siteWrapper
				.setAntHight((Double) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_ANT_HEIGHT));
		siteWrapper.setPci((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_PCI));
		siteWrapper.setCgi((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_CGI));
		siteWrapper.setActualAzimuth(
				(Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_AZIMUTH));
		siteWrapper.setSector((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_SECTOR));
		/// Boolean retStatus = (Boolean) getValueFromMapIfExists(siteData,
		/// SiteDetailConstant.RAN_PROJECTION_RET_STATUS);
		siteWrapper.setCellName(
				getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_ENBID) + Symbol.UNDERSCORE_STRING
						+ getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_CELL_ID));
		siteWrapper.setmTilt(
				String.valueOf(getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_MECH_TILT) != null
						? getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_MECH_TILT)
						: ReportConstants.HYPHEN));
		Object eTilt = getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_ELEC_TILT);
		siteWrapper.seteTilt(String.valueOf(eTilt != null ? eTilt : ReportConstants.HYPHEN));

		siteWrapper.setRet(eTilt != null && !String.valueOf(eTilt).isEmpty() ? "Enabled" : "Disabled");

		siteWrapper.setTac((String) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_TAC));
		siteWrapper.setAntenaModel(
				(String) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_ANT_MODEL));
		siteWrapper
				.setAntennaType((String) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_ANT_TYPE));
		siteWrapper.setCellId((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_CELL_ID));
		siteWrapper.setEcgi((String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_ECGI));
		siteWrapper.setAzimuth(getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_AZIMUTH) != null
				? String.valueOf(getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_AZIMUTH))
				: ReportConstants.HYPHEN);
		String txPower = (String) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_TXPOWER);
		if (NumberUtils.isCreatable(txPower)) {
			siteWrapper.setTxPower(Double.parseDouble(txPower));
		}
		siteWrapper.setHorizontalBeamWidth(
				(Double) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_HORIZONTAL_BEAMWIDTH));
		siteWrapper.setDlEarfcn((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_DL_EARFCN));
		siteWrapper.setEarfcn((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_EARFCN));
		siteWrapper.setBandwidth((String) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_BANDWIDTH));
		siteWrapper.setTechnology(String.valueOf(getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_TECHNOLOGY)));
	}

	private static void setGeographyAndNetworkElementDetail(Map<Object, Object> siteData,
			SiteInformationWrapper siteWrapper) {
		siteWrapper
				.setEnbName(String.valueOf(getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_ENBID)));
		siteWrapper.setNeId((String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_ID));
		NEType siteType = (NEType) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_TYPE);
		if (siteType != null) {
			siteWrapper.setSiteType(siteType.displayName());
		}
		NEStatus neStatus = (NEStatus) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_STATUS);
		if (neStatus != null) {
			siteWrapper.setNeStatus(neStatus.name());
		}
		siteWrapper.setSiteName((String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_NAME));
		siteWrapper.setLat((Double) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_LATITUDE));
		siteWrapper.setLon((Double) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_LONGITUDE));

		siteWrapper.setGeographyId((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.GEOGRAPHY_L4_PROJECTION_ID));
		siteWrapper.setCityName(
				(String) getValueFromMapIfExists(siteData, SiteDetailConstant.GEOGRAPHY_L3_PROJECTION_DISPLAY_NAME));
		siteWrapper.setRegion(
				(String) getValueFromMapIfExists(siteData, SiteDetailConstant.GEOGRAPHY_L2_PROJECTION_DISPLAY_NAME));
		siteWrapper.setCluster(
				(String) getValueFromMapIfExists(siteData, SiteDetailConstant.GEOGRAPHY_L4_PROJECTION_DISPLAY_NAME));
		siteWrapper.setZone(
				(String) getValueFromMapIfExists(siteData, SiteDetailConstant.GEOGRAPHY_L1_PROJECTION_DISPLAY_NAME));
		siteWrapper.setSiteName(
				(String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_PARENT_NE_NAME));
		siteWrapper.setNeFrequency(
				(String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_FREQUENCY));
		siteWrapper.setNename((String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_NAME));
		siteWrapper.setMcc((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_MCC));
		siteWrapper.setMnc((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_MNC));
		siteWrapper.setFriendlyName((String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_PARENT_FRIENDLY_NAME));
	}

	public static List<SiteDataWrapper> convertTupleListToSiteDataWrapper(List<Map<Object, Object>> siteData) {
		List<SiteDataWrapper> siteDataList = new ArrayList<>();
		for (Map<Object, Object> site : siteData) {
			SiteDataWrapper siteDataWrapper = new SiteDataWrapper();
			siteDataWrapper
					.setCellId((Integer) getValueFromMapIfExists(site, SiteDetailConstant.NE_PROJECTION_CELL_ID));
			siteDataWrapper
					.setEnodeBId((Integer) getValueFromMapIfExists(site, SiteDetailConstant.NE_PROJECTION_ENBID));
			siteDataWrapper.setSapId((String) getValueFromMapIfExists(site, SiteDetailConstant.NE_PROJECTION_NE_NAME));
			siteDataWrapper.setBand(Integer
					.parseInt((String) getValueFromMapIfExists(site, SiteDetailConstant.NE_PROJECTION_NE_FREQUENCY)));
			siteDataWrapper.setId((Integer) getValueFromMapIfExists(site, SiteDetailConstant.NE_PROJECTION_ID));
			siteDataList.add(siteDataWrapper);
		}
		return siteDataList;
	}

	public static Map<String, List<Map>> getGeographyFilterFromGeographyMap(Map<String, List<String>> geographyMap) {
		Map<String, List<Map>> geographyFilterMap = new HashMap<>();
		List<Map> geographyFilterList = new ArrayList<>();
		if (geographyMap.get(InfraConstants.GEOGRAPHYL4_TABLE) != null) {
			geographyFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.FILTER_COLUMNS_GEOGRAPHY_NAME, SiteDetailConstant.FILTER_OPERATION_IN,
					SiteDetailConstant.FILTER_DATA_TYPE_STRING, geographyMap.get(InfraConstants.GEOGRAPHYL4_TABLE)));
			geographyFilterMap.put(InfraConstants.GEOGRAPHYL4_TABLE, geographyFilterList);
		}
		if (geographyMap.get(InfraConstants.GEOGRAPHYL3_TABLE) != null) {
			geographyFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.FILTER_COLUMNS_GEOGRAPHY_NAME, SiteDetailConstant.FILTER_OPERATION_IN,
					SiteDetailConstant.FILTER_DATA_TYPE_STRING, geographyMap.get(InfraConstants.GEOGRAPHYL3_TABLE)));
			geographyFilterMap.put(InfraConstants.GEOGRAPHYL3_TABLE, geographyFilterList);
		}
		if (geographyMap.get(InfraConstants.GEOGRAPHYL2_TABLE) != null) {
			geographyFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.FILTER_COLUMNS_GEOGRAPHY_NAME, SiteDetailConstant.FILTER_OPERATION_IN,
					SiteDetailConstant.FILTER_DATA_TYPE_STRING, geographyMap.get(InfraConstants.GEOGRAPHYL2_TABLE)));
			geographyFilterMap.put(InfraConstants.GEOGRAPHYL2_TABLE, geographyFilterList);
		}
		if (geographyMap.get(InfraConstants.GEOGRAPHYL1_TABLE) != null) {
			geographyFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.FILTER_COLUMNS_GEOGRAPHY_NAME, SiteDetailConstant.FILTER_OPERATION_IN,
					SiteDetailConstant.FILTER_DATA_TYPE_STRING, geographyMap.get(InfraConstants.GEOGRAPHYL1_TABLE)));
			geographyFilterMap.put(InfraConstants.GEOGRAPHYL1_TABLE, geographyFilterList);
		}
		return geographyFilterMap;
	}

	private static Object getValueFromMapIfExists(Map<Object, Object> dataMap, String key) {
		return dataMap.containsKey(key) ? dataMap.get(key) : null;
	}

	public static LatLng getLatLngIfExists(Tuple t1) {
		if (t1.get(SiteDetailConstant.NE_PROJECTION_LATITUDE) != null
				&& t1.get(SiteDetailConstant.NE_PROJECTION_LONGITUDE) != null) {
			logger.info("returning Lat long for CGI&PCI, Lat: {}, Long: {}",
					t1.get(SiteDetailConstant.NE_PROJECTION_LATITUDE),
					t1.get(SiteDetailConstant.NE_PROJECTION_LONGITUDE));
			return new LatLng(t1.get(SiteDetailConstant.NE_PROJECTION_LATITUDE, Double.class),
					t1.get(SiteDetailConstant.NE_PROJECTION_LONGITUDE, Double.class));
		}
		return new LatLng();
	}

	public static List<String> getRANProjectionsForReports() {
		List<String> ranProjectionList = new ArrayList<>();
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_ANT_HEIGHT);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_PCI);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_AZIMUTH);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_SECTOR);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_ANT_TYPE);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_ANT_MODEL);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_MECH_TILT);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_ELEC_TILT);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_TAC);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_TXPOWER);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_RET_STATUS);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_CGI);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_HORIZONTAL_BEAMWIDTH);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_EARFCN);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_DL_EARFCN);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_BANDWIDTH);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_ADMIN_STATE);
		return ranProjectionList;
	}
	
	public static List<String> getProjectionNEBandDetail() {
		List<String> neBandProjectionList = new ArrayList<>();
		neBandProjectionList.add(SiteDetailConstant.NEBAND_PROJECTION_BANDONAIRDATE);
		return neBandProjectionList;
	}

	public static List<String> getNeProjectionsForReports() {
		List<String> neProjectionList = new ArrayList<>();
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_ID);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_ENBID);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_ECGI);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_TYPE);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_STATUS);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_NAME);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_LATITUDE);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_LONGITUDE);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_ID);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_CELL_ID);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_FREQUENCY);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_MCC);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_MNC);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_FRIENDLY_NAME);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_TECHNOLOGY);
		return neProjectionList;
	}

	public static List<String> getGeographyNameProjection() {
		List<String> geographyProjectionList = new ArrayList<>();
		geographyProjectionList.add(SiteDetailConstant.GEOGRAPHY_PROJECTION_NAME);
		geographyProjectionList.add(SiteDetailConstant.GEOGRAPHY_PROJECTION_DISPLAY_NAME);
		geographyProjectionList.add(SiteDetailConstant.NE_PROJECTION_ID);
		return geographyProjectionList;
	}

	public static List<NetworkElementWrapper> convertTupleToWrapperForVisualization(
			List<Map<Object, Object>> siteData) {
		logger.info("inside the method convertTupleToWrapperForVisualization ");
		List<NetworkElementWrapper> siteInfoList = new ArrayList<>();
		try {
			siteData.forEach(singleSiteData -> {
				if (singleSiteData != null && !singleSiteData.isEmpty()) {
					NetworkElementWrapper siteWrapper = new NetworkElementWrapper();
					setMacroSiteDetailIntoNEWrapper(singleSiteData, siteWrapper);
					setNetworkElementDetail(singleSiteData, siteWrapper);
					siteInfoList.add(siteWrapper);
				}
			});
		} catch (Exception e) {
			logger.error("error inside the method convetMicroSiteDetailToSiteInfo {}", Utils.getStackTrace(e));
		}
		return siteInfoList;
	}

	private static void setMacroSiteDetailIntoNEWrapper(Map<Object, Object> siteData,
			NetworkElementWrapper siteWrapper) {

		siteWrapper.setAntennaHeight(
				(Double) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_ANT_HEIGHT));
		siteWrapper.setPci((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_PCI));
		siteWrapper.setAzimuth((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_AZIMUTH));
		siteWrapper.setSectorId((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_SECTOR));

		siteWrapper.setCellName(
				getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_ENBID) + Symbol.UNDERSCORE_STRING
						+ getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_CELL_ID));
		siteWrapper
				.setMechTilt((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_MECH_TILT));
		siteWrapper
				.setElecTilt((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_ELEC_TILT));

		siteWrapper
				.setAntennaType((String) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_ANT_TYPE));
		siteWrapper.setCellId((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_CELL_ID));
		siteWrapper.setEcgi((String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_ECGI));
		siteWrapper.setCgi((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_CGI));
		siteWrapper.setEarfcn((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_EARFCN));

	}

	private static void setNetworkElementDetail(Map<Object, Object> siteData, NetworkElementWrapper siteWrapper) {
		siteWrapper.setSapid(String.valueOf(getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_ENBID)));
		siteWrapper.setNeId((String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_ID));
		NEType siteType = (NEType) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_TYPE);
		if (siteType != null) {
			siteWrapper.setNeType(siteType);
		}
		NEStatus neStatus = (NEStatus) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_STATUS);
		if (neStatus != null) {
			siteWrapper.setNeStatus(neStatus.name());
		}

		siteWrapper.setLatitude((Double) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_LATITUDE));
		siteWrapper
				.setLongitude((Double) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_LONGITUDE));
		siteWrapper.setSiteName(
				(String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_PARENT_NE_NAME));
		siteWrapper.setNeFrequency(
				(String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_FREQUENCY));
		siteWrapper.setDisplayName(
				(String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_PARENT_NE_NAME));
	}

	public static Map<String, List<Map>> getNEFilterForCoverage(Long lastModified, List<String> neType,
			List<String> neStatus, List<String> neNamesList,Boolean isTiReady) {
		Map<String, List<Map>> filterMap = new HashMap<>();
		List<Map> neFilterList = new ArrayList<>();
		List<Map> neParentFilterList = new ArrayList<>();
		neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_NE_TYPE,
				SiteDetailConstant.FILTER_OPERATION_IN, SiteDetailConstant.FILTER_COLUMNS_DATATYPE_NE_TYPE, neType));

		neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_NE_STATUS,
				SiteDetailConstant.FILTER_OPERATION_IN, SiteDetailConstant.FILTER_COLUMNS_DATATYPE_NE_STATUS,
				neStatus));

		if (CollectionUtils.isNotEmpty(neNamesList)) {
			neParentFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.NE_PROJECTION_NE_NAME, SiteDetailConstant.FILTER_OPERATION_IN,
					SiteDetailConstant.FILTER_DATA_TYPE_STRING, neNamesList));

			filterMap.put(InfraConstants.PARENT_NETWORK_ELEMENT, neParentFilterList);

		} else {
			if(!isTiReady){
			neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.NE_PROJECTION_MODIFICATIONTIME, InfraConstants.GREATOR_THAN_OPERATOR,
					SiteDetailConstant.FILTER_DATA_TYPE_TIMESTAMP, String.valueOf(lastModified)));
			}

		}
		if(isTiReady) {
			List<Map> customNEFilterList = new ArrayList<>();
			customNEFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.NE_PROJECTION_MODIFICATIONTIME, InfraConstants.GREATOR_THAN_OPERATOR,
					SiteDetailConstant.FILTER_DATA_TYPE_TIMESTAMP, String.valueOf(lastModified)));
			customNEFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.CUSTOM_NE_PROJECTION_TIREADY, InfraConstants.EQUALS_OPERATOR,
					SiteDetailConstant.FILTER_DATA_TYPE_BOOLEAN, String.valueOf(isTiReady)));
			filterMap.put(SiteDetailConstant.CUSTOM_NETWORK_ELEMENT, customNEFilterList);
		}

		filterMap.put(SiteDetailConstant.TN_NETWORK_ELEMENT, neFilterList);
		return filterMap;

	}

	public static Map<String, List<Map>> getRANFilterForCoverage(Long lastModified, List<String> neType,
			List<String> neStatus) {
		Map<String, List<Map>> filterMap = new HashMap<>();
		List<Map> neFilterList = new ArrayList<>();
		List<Map> ranFilterList = new ArrayList<>();
		neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_NE_TYPE,
				SiteDetailConstant.FILTER_OPERATION_IN, SiteDetailConstant.FILTER_COLUMNS_DATATYPE_NE_TYPE, neType));

		neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_NE_STATUS,
				SiteDetailConstant.FILTER_OPERATION_IN, SiteDetailConstant.FILTER_COLUMNS_DATATYPE_NE_STATUS,
				neStatus));

		ranFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
				SiteDetailConstant.RAN_PROJECTION_MODIFICATIONTIME, InfraConstants.GREATOR_THAN_OPERATOR,
				SiteDetailConstant.FILTER_DATA_TYPE_TIMESTAMP, String.valueOf(lastModified)));

		filterMap.put(SiteDetailConstant.TN_RAN_DETAIL, ranFilterList);
		filterMap.put(SiteDetailConstant.TN_NETWORK_ELEMENT, neFilterList);
		return filterMap;
	}

	public static Map<String,SiteDetailWrapper> convertTIReadySitesIntoSiteWrapper(List<Object[]> list) {
		
		Map<String,SiteDetailWrapper> siteMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(list)) {
			logger.info("Total List size from AtollCellDetail {}",list.size());
			for (Object[] dataArr : list) {
				try {
				SiteDetailWrapper siteWrapper = new SiteDetailWrapper();
				siteWrapper.setSiteName((String) dataArr[0]);
				siteWrapper.setLat(Double.valueOf((String)dataArr[1]));
				siteWrapper.setLon(Double.valueOf((String)dataArr[2]));
				siteWrapper.setModificationTime(Long.valueOf((String)dataArr[3]));
				siteWrapper.setNeStatus((String) NEStatus.PLANNED.name());
				if(dataArr[5]!=null) {
					if((Boolean)dataArr[5]) {
					siteWrapper.setTiReady(1);
					}
				}
				
				siteMap.put((String) dataArr[0],siteWrapper);
			}
				catch(Exception e) {
					logger.info("Exception while adding TiReady sites {}",Utils.getStackTrace(e));
				}
			}
				
		}
		return siteMap;
	}

	public static List<String> getNEProjectionsForCoverage(Boolean isCgiOnly) {
		List<String> neProjectionList = new ArrayList<>();
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_TYPE);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_STATUS);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_NAME);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_ID);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_LATITUDE);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_LONGITUDE);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_NE_FREQUENCY);
		neProjectionList.add(SiteDetailConstant.NE_PROJECTION_MODIFICATIONTIME);
		if (isCgiOnly) {
			neProjectionList.add(SiteDetailConstant.NE_PROJECTION_SOFTWAREVERSION);
			neProjectionList.add(SiteDetailConstant.NE_PROJECTION_CELL_ID);
		}
		return neProjectionList;
	}

	public static List<String> getRANProjectionsForCoverage() {
		List<String> ranProjectionList = new ArrayList<>();
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_PCI);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_AZIMUTH);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_CGI);
		ranProjectionList.add(SiteDetailConstant.RAN_PROJECTION_SECTOR);
		return ranProjectionList;
	}

	public static Map<String, SiteDetailWrapper> convertTupleToSiteDetailWrapper(List<Map<Object, Object>> siteData, Map<String, String> responseMap) {
		logger.info("inside the method convertTupleToSiteInformationWrapper of size {}", siteData.size());
		Map<String, SiteDetailWrapper> siteInfoMap = new HashMap<>();

		try {
			siteData.forEach(singleSiteData -> {
				if (singleSiteData != null && !singleSiteData.isEmpty()) {
					SiteDetailWrapper site = new SiteDetailWrapper();
					NEType neType =
							(NEType) getValueFromMapIfExists(singleSiteData, SiteDetailConstant.NE_PROJECTION_NE_TYPE);
					if (neType.compareTo(NEType.MACRO) == 0) {
						if (siteInfoMap.containsKey(site.getSiteName())) {
							site = siteInfoMap.get(site.getSiteName());
							setMacroSiteDetailIntoWrapper(singleSiteData, site);
							siteInfoMap.put(site.getSiteName(), site);
						} else {
							setMacroSiteDetailIntoWrapper(singleSiteData, site);
							siteInfoMap.put(site.getSiteName(), site);
						}
					} else if(neType.compareTo(NEType.IDSC_CELL) == 0 || neType.compareTo(NEType.ODSC_CELL) == 0) {
						setCellDetailForSmallCell(siteInfoMap, singleSiteData, site,responseMap);
					} else {
						setCellDetail(siteInfoMap, singleSiteData, site);
					}
				}
			});

		} catch (Exception e) {
			logger.error("error inside the method convetMicroSiteDetailToSiteInfo {}", Utils.getStackTrace(e));
		}

		return siteInfoMap;

	}

	private static void setCellDetailForSmallCell(Map<String, SiteDetailWrapper> siteInfoMap, Map<Object, Object> siteData, SiteDetailWrapper site, Map<String, String> responseMap) {

		logger.info("inside the method setCellDetailForSmallCell");
		CellDetailWrapper cellWrapper = new CellDetailWrapper();
		NEStatus neStatus = (NEStatus) siteData.get(SiteDetailConstant.NE_PROJECTION_NE_STATUS);
		String neName = (String) siteData.get(SiteDetailConstant.NE_PROJECTION_NE_NAME);

		if (neStatus != null && neStatus.compareTo(NEStatus.ONAIR) == 0) {

			setCellDetailIntoWrapper(siteData, cellWrapper);
			if (cellWrapper.getCgi() != null) {
				site.getCells().add(cellWrapper);
			}
			site.setSiteName((String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_NAME));
			site.setLat((Double) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_LATITUDE));
			site.setLon((Double) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_LONGITUDE));
			site.setNeStatus(String
					.valueOf((NEStatus) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_STATUS)));
			site.setNeType(String
					.valueOf((NEType) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_TYPE)));
			
			if(responseMap!=null) {
			site.setFloorName(responseMap.get(SiteDetailConstant.FLOOR_NAME));
			site.setBuildingName(responseMap.get(SiteDetailConstant.BUILDING_NAME));
			}
			Timestamp date = (Timestamp) (getValueFromMapIfExists(siteData,
					SiteDetailConstant.NE_PROJECTION_MODIFICATIONTIME));
			site.setModificationTime(date != null ? date.getTime() : null);

			siteInfoMap.put(neName, site);
		}
	}

	private static void setCellDetail(Map<String, SiteDetailWrapper> siteInfoMap, Map<Object, Object> singleSiteData,
			SiteDetailWrapper site) {
		CellDetailWrapper cellWrapper = new CellDetailWrapper();
		try {
		String parentneName = (String) singleSiteData.get(SiteDetailConstant.NE_PROJECTION_PARENT_NE_NAME);
		NEStatus parentNeStatus = (NEStatus) singleSiteData.get(SiteDetailConstant.NE_PROJECTION_PARENT_NESTATUS);

			setCellDetailIntoWrapper(singleSiteData, cellWrapper);
			if (siteInfoMap.containsKey(parentneName)) {
				List<CellDetailWrapper> cells = siteInfoMap.get(parentneName).getCells();
				if (!cells.stream().anyMatch(c -> c.getCgi() != null && cellWrapper.getCgi() != null
						&& c.getCgi().compareTo(cellWrapper.getCgi()) == 0)) {
					cells.add(cellWrapper);
				}
					Boolean isTiReady = (Boolean) getValueFromMapIfExists(singleSiteData, SiteDetailConstant.CUSTOM_NE_PROJECTION_TIREADY);
					if(isTiReady!=null&&isTiReady) {
						siteInfoMap.get(parentneName).setTiReady(1);
					}
			} else {
				if (cellWrapper.getCgi() != null) {
					site.getCells().add(cellWrapper);
				}
					site.setSiteName(parentneName);
					site.setLat((Double) getValueFromMapIfExists(singleSiteData,
							SiteDetailConstant.NE_PROJECTION_PARENT_LATITUDE));
					site.setLon((Double) getValueFromMapIfExists(singleSiteData,
							SiteDetailConstant.NE_PROJECTION_PARENT_LONGITUDE));
					Timestamp date = (Timestamp) (getValueFromMapIfExists(singleSiteData,
							SiteDetailConstant.NE_PROJECTION_PARENT_MODIFIEDTIME));
					site.setModificationTime(date != null ? date.getTime() : null);
					site.setNeStatus(parentNeStatus.name());
					site.setSoftwareVersion((String) getValueFromMapIfExists(singleSiteData,
							SiteDetailConstant.NE_PROJECTION_PARENT_SOFTWAREVERSION));
					
					Boolean isTiReady = (Boolean) getValueFromMapIfExists(singleSiteData, SiteDetailConstant.CUSTOM_NE_PROJECTION_TIREADY);
					if(isTiReady!=null&&isTiReady) {
						site.setTiReady(1);
					}
					siteInfoMap.put(parentneName, site);
				}
		}
			catch(Exception exception) {
				logger.info("Exception in setCellDetail ",exception.getMessage());
			}
			}

	static void setMacroSiteDetailIntoWrapper(Map<Object, Object> siteData, SiteDetailWrapper site) {
		site.setSiteName((String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_NAME));
		site.setLat((Double) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_LATITUDE));
		site.setLon((Double) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_LONGITUDE));
		site.setNeStatus(String
				.valueOf((NEStatus) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_STATUS)));
		Timestamp date = (Timestamp) (getValueFromMapIfExists(siteData,
				SiteDetailConstant.NE_PROJECTION_MODIFICATIONTIME));
		site.setModificationTime(date != null ? date.getTime() : null);
	}

	static void setCellDetailIntoWrapper(Map<Object, Object> siteData, CellDetailWrapper cell) {
		
		cell.setCgi((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.FILTER_COLUMNS_RAN_CGI));
		cell.setAzimuth((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_AZIMUTH));
		cell.setNeStatus(String
				.valueOf((NEStatus) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_STATUS)));

		Timestamp date = (Timestamp) (getValueFromMapIfExists(siteData,
				SiteDetailConstant.NE_PROJECTION_MODIFICATIONTIME));
		cell.setModificationTime(date != null ? date.getTime() : null);
		cell.setPci((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_PCI));
		cell.setCellNum((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_CELL_ID));
		cell.setSector((Integer) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_SECTOR));
		cell.setNeId((String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_ID));
		cell.setAdminState((String) getValueFromMapIfExists(siteData, SiteDetailConstant.RAN_PROJECTION_ADMIN_STATE));
		cell.setNeName((String) getValueFromMapIfExists(siteData, SiteDetailConstant.NE_PROJECTION_NE_NAME));
	}

	public static List<String> getUniqueSiteNameFromMap(List<SiteDetailWrapper> siteDetailList) {
		Set<String> siteSet = new HashSet();
		siteDetailList.stream().forEach(c -> siteSet.add(c.getSiteName()));
		return new ArrayList(siteSet);
	}

	public static Map<String, List<Map>> getNEFilter(List<String> neType, List<String> neStatus,
			List<String> neNamesList) {

		Map<String, List<Map>> filterMap = new HashMap<>();
		List<Map> neFilterList = new ArrayList<>();
		List<Map> neParentFilterList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(neType)) {
			neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_NE_TYPE,
					SiteDetailConstant.FILTER_OPERATION_IN, SiteDetailConstant.FILTER_COLUMNS_DATATYPE_NE_TYPE,
					neType));
		}
		if (CollectionUtils.isNotEmpty(neStatus)) {
			neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(SiteDetailConstant.FILTER_COLUMNS_NE_STATUS,
					SiteDetailConstant.FILTER_OPERATION_IN, SiteDetailConstant.FILTER_COLUMNS_DATATYPE_NE_STATUS,
					neStatus));
		}
		if (CollectionUtils.isNotEmpty(neNamesList)) {
			neParentFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
					SiteDetailConstant.NE_PROJECTION_NE_NAME, SiteDetailConstant.FILTER_OPERATION_IN,
					SiteDetailConstant.FILTER_DATA_TYPE_STRING, neNamesList));

			filterMap.put(InfraConstants.PARENT_NETWORK_ELEMENT, neParentFilterList);

		}

		filterMap.put(SiteDetailConstant.TN_NETWORK_ELEMENT, neFilterList);
		return filterMap;
	}
	public static 	Map<String , Double> getViewPortMap(CoverageLayerWrapper wrapper){
		Map<String , Double> viewPortMap=new HashMap<>();
		viewPortMap.put(InfraConstants.SW_LATITUDE, wrapper.getsWLat());
		viewPortMap.put(InfraConstants.NW_LAT, wrapper.getnELat());
		viewPortMap.put(InfraConstants.SW_LONGITUDE, wrapper.getsWLng());
		viewPortMap.put(InfraConstants.NW_LONG, wrapper.getnELng());
	
	logger.info("View Port Map {}", new Gson().toJson(viewPortMap));
	return viewPortMap;	

    }
	

	public static Map<String, SiteDetailWrapper> getSitesDataFromTupleMap(List<Map<Object, Object>> siteData, Map<String,String> responseMap) {
		logger.info("inside the method convertTupleToSiteInformationWrapper of size {}", siteData.size());
		Map<String, SiteDetailWrapper> siteInfoMap = new HashMap<>();

		try {
			siteData.forEach(singleSiteData -> {
				if (singleSiteData != null && !singleSiteData.isEmpty()) {
			
					SiteDetailWrapper site = new SiteDetailWrapper();
					NEType neType =
							(NEType) getValueFromMapIfExists(singleSiteData, SiteDetailConstant.NE_PROJECTION_NE_TYPE);
					if (neType.compareTo(NEType.MACRO) == 0) {
						if (siteInfoMap.containsKey(site.getSiteName())) {
							site = siteInfoMap.get(site.getSiteName());
							setMacroSiteDetailIntoWrapper(singleSiteData, site);
							siteInfoMap.put(site.getSiteName(), site);
						} else {
							setMacroSiteDetailIntoWrapper(singleSiteData, site);
							siteInfoMap.put(site.getSiteName(), site);
						}
					} else if(neType.compareTo(NEType.IDSC_CELL) == 0 || neType.compareTo(NEType.ODSC_CELL) == 0) {
						setCellDetailForSmallCell(siteInfoMap, singleSiteData, site,responseMap);
					} else {
						setCellData(siteInfoMap, singleSiteData, site);
					}
				}
			});

		} catch (Exception e) {
			logger.error("error inside the method convetMicroSiteDetailToSiteInfo {}", Utils.getStackTrace(e));
		}

		return siteInfoMap;

	}

	
	private static void setCellData(Map<String, SiteDetailWrapper> siteInfoMap, Map<Object, Object> singleSiteData,
			SiteDetailWrapper site) {
		CellDetailWrapper cellWrapper = new CellDetailWrapper();
		try {
		String parentneName = (String) singleSiteData.get(SiteDetailConstant.NE_PROJECTION_PARENT_NE_NAME);
		NEStatus parentNeStatus = (NEStatus) singleSiteData.get(SiteDetailConstant.NE_PROJECTION_PARENT_NESTATUS);
			setCellDetailIntoWrapper(singleSiteData, cellWrapper);
			if (siteInfoMap.containsKey(parentneName)) {
				List<CellDetailWrapper> cells = siteInfoMap.get(parentneName).getCells();
				if (!cells.stream().anyMatch(c -> c.getNeId() != null && cellWrapper.getNeId() != null
						&& c.getNeId().compareTo(cellWrapper.getNeId()) == 0)) {
					cells.add(cellWrapper);
				}
					Boolean isTiReady = (Boolean) getValueFromMapIfExists(singleSiteData, SiteDetailConstant.CUSTOM_NE_PROJECTION_TIREADY);
					if(isTiReady!=null&&isTiReady) {
						siteInfoMap.get(parentneName).setTiReady(1);
					}
					Timestamp tiDate = (Timestamp) (getValueFromMapIfExists(singleSiteData,
							SiteDetailConstant.TI_READY_DATE));
					if(tiDate!=null) {
						siteInfoMap.get(parentneName).setTiReadyDate(tiDate.getTime());
					}
			} else {
				if (cellWrapper.getNeId() != null) {
					site.getCells().add(cellWrapper);
				}
					site.setSiteName(parentneName);
					site.setLat((Double) getValueFromMapIfExists(singleSiteData,
							SiteDetailConstant.NE_PROJECTION_PARENT_LATITUDE));
					site.setLon((Double) getValueFromMapIfExists(singleSiteData,
							SiteDetailConstant.NE_PROJECTION_PARENT_LONGITUDE));
					Timestamp date = (Timestamp) (getValueFromMapIfExists(singleSiteData,
							SiteDetailConstant.NE_PROJECTION_PARENT_MODIFIEDTIME));
					site.setModificationTime(date != null ? date.getTime() : null);
					site.setNeStatus(parentNeStatus.name());
					site.setSoftwareVersion((String) getValueFromMapIfExists(singleSiteData,
							SiteDetailConstant.NE_PROJECTION_PARENT_SOFTWAREVERSION));
					Boolean isTiReady = (Boolean) getValueFromMapIfExists(singleSiteData, SiteDetailConstant.CUSTOM_NE_PROJECTION_TIREADY);
					if(isTiReady!=null&&isTiReady) {
						site.setTiReady(1);
					}
					Timestamp onairDate = (Timestamp) (getValueFromMapIfExists(singleSiteData,
							SiteDetailConstant.NEBAND_PROJECTION_BANDONAIRDATE));
					site.setOnAirDate(onairDate!= null ? onairDate.getTime() : null);
					
					Timestamp tiDate = (Timestamp) (getValueFromMapIfExists(singleSiteData,
							SiteDetailConstant.TI_READY_DATE));
					site.setTiReadyDate(tiDate!= null ? tiDate.getTime() : null);
					siteInfoMap.put(parentneName, site);
				}
		}
			catch(Exception exception) {
				logger.info("Exception in setCellDetail ",exception.getMessage());
			}
			}

	public static Map<String, List<Map>> getNEFilterForNename(String neName) {
		Map<String, List<Map>> filterMap = new HashMap<>();
		List<Map> neFilterList = new ArrayList<>();

		neFilterList.add(CriteriaUtils.getFilterMapForInputParameters(
				SiteDetailConstant.NE_PROJECTION_NE_NAME, SiteDetailConstant.FILTER_OPERATION_EQUALS,
				SiteDetailConstant.FILTER_DATA_TYPE_STRING, neName));

		filterMap.put(SiteDetailConstant.TN_NETWORK_ELEMENT, neFilterList);
		return filterMap;

	}
	
}
