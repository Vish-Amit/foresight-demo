package com.inn.foresight.module.nv.layer.service.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ibm.icu.text.SimpleDateFormat;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.geometry.GeometryConstants;
import com.inn.commons.maps.tiles.Tile;
import com.inn.commons.maps.tiles.TileBoundaryUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.module.nv.layer.LayerPlotUtils;
import com.inn.foresight.module.nv.layer.constants.LayerPlotConstants;
import com.inn.foresight.module.nv.layer.dao.LayerPlotDao;
import com.inn.foresight.module.nv.layer.service.LayerPlotService;
import com.inn.foresight.module.nv.layer.wrapper.BoundaryDataWrapper;
import com.inn.foresight.module.nv.layer.wrapper.CoverageLayerWrapper;
import com.inn.foresight.module.nv.service.ISiteDetailService;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;

/**
 * @author ist
 * @author ist
 */
/**
 * @author ist
 *
 */

/**
 * @author ist
 *
 */
@Service("LayerPlotServiceImpl")
public class LayerPlotServiceImpl implements LayerPlotService {
	
	@Autowired
	ISiteDetailService  service;

	static Logger logger = LogManager.getLogger(LayerPlotServiceImpl.class);

	@Autowired
	LayerPlotDao layerDao;

	@Autowired
	SystemConfigurationDao systemConfigDao;

	@Autowired
	IGenericMapService genericMapService;
	
	@Autowired
	INetworkElementDao networkElementDao;

	@Override
	public String getLayerData(List<String> columnList, String tableName, Integer zoom, Double nELat, Double nELng,
							   Double sWLat, Double sWLng, String minDate, String maxDate, String postFix, String gridColName, String
									   recordType, String mapType, String filterColName) {
		try {
			String columns = new Gson().toJson(columnList);
			logger.info("Column name  for layer {}", columns);
			StringEntity entity = new StringEntity(columns, ContentType.APPLICATION_JSON);
			List<String> valueList = new ArrayList<>();
			valueList.add(tableName);
			valueList.add(String.valueOf(zoom));
			valueList.add(String.valueOf(nELat));
			valueList.add(String.valueOf(nELng));
			valueList.add(String.valueOf(sWLat));
			valueList.add(String.valueOf(sWLng));
			valueList.add(minDate);
			valueList.add(maxDate);
			valueList.add(postFix);
			valueList.add(gridColName);
			valueList.add(recordType);
			valueList.add(mapType);
			valueList.add(filterColName);

			String query = GenericMapUtils.createGenericQuery(valueList, LayerPlotConstants.TABLENAME,
					LayerPlotConstants.ZOOM, LayerPlotConstants.NELAT, LayerPlotConstants.NELONG, LayerPlotConstants.SWLAT,
					LayerPlotConstants.SWLONG, LayerPlotConstants.MINDATE, LayerPlotConstants.MAXDATE, LayerPlotConstants.POSTFIX,
					LayerPlotConstants.GRID_COL_NAME, LayerPlotConstants.RECORD_TYPE, LayerPlotConstants.MAP_TYPE,
					LayerPlotConstants.FILTER_COL_NAME);
			logger.info("request parameters for service [{}], [{}], [{}] ,[{}], [{}], [{}], [{}]", columns, query, zoom,
					nELat, nELng, sWLat, sWLng);
			String restCall = LayerPlotConstants.DROPWIZARD_LAYERPLOT_URL;
			String dropwizardUrl = getDropwizardUrl(query, restCall);
			return new HttpPostRequest(dropwizardUrl, entity).getString();
		} catch (Exception e) {
			logger.error("Error while getting data from rest service {} ", ExceptionUtils.getStackTrace(e));
		}

		return Symbol.EMPTY_STRING;
	}

	public String getDropwizardUrl(String query, String restCall) {
		String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue()) + restCall;
		logger.info("Layer map dropwizard URL {} , [{}]", url, query);
		if (query != null && query.length() != 0) {
			url += "?" + query;
		}
		logger.info("Final dropwizard URL for Layer Plot {}", url);
		return url;
	}

	@Override
	public List<List<String>> getLayerPlotData(String tableName, Integer zoom, List<String> columnList, Double nELat,
											   Double nELng, Double sWLat, Double sWLng, String minDate, String maxDate, String postFix, String gridColName,
											   String recordType, String mapType, String filterColName)
			throws ParseException {
		List<String> hbaseColumnList = new ArrayList<>();
		hbaseColumnList.addAll(columnList);
		List<List<List<Double>>> cornerBoundary = new ArrayList<>();

		Corner corner = findViewPortCorner(zoom, nELat, nELng, sWLat, sWLng, mapType);
		cornerBoundary.add(corner.getBoundary());
		Map<String, List<Tile>> scopeWiseTiles = TileBoundaryUtils.getFullynPartiallyCoveredTiles(cornerBoundary, zoom, zoom);
		Map<String, List<String>> prefixMap = LayerPlotUtils.getPrefixMap(minDate, maxDate, scopeWiseTiles, postFix);
		List<HBaseResult> finalResult = findResponseList(tableName, hbaseColumnList, prefixMap, gridColName, corner, filterColName);
		return returnAcknowlegement(columnList, minDate, maxDate, gridColName, recordType, finalResult);
	}

	/**
	 * @param zoom
	 * @param nELat
	 * @param nELng
	 * @param sWLat
	 * @param sWLng
	 * @param mapType
	 * @return
	 */
	private Corner findViewPortCorner(Integer zoom, Double nELat, Double nELng, Double sWLat, Double sWLng,
									  String mapType) {
		logger.info("Found the sWLat {}, sWLng {}, nELat {}, nELng {}", sWLat, sWLng, nELat, nELng);
		if (mapType.equalsIgnoreCase(LayerPlotConstants.GRID_NAME)) {
			SystemConfiguration obj = systemConfigDao.getConfigurationByName(LayerPlotConstants.GRID_DIFFERENCE_KEY);

			if (obj != null && obj.getValue() != null) {
				Type type = new TypeToken<Map<String, Map<String, Double>>>() {
				}.getType();
				Map<String, Map<String, Double>> map = new HashMap<>();
				map = new Gson().fromJson(obj.getValue(), type);
				Double lat = map.get(LayerPlotConstants.ZOOM_GRID_KEY + zoom).get(LayerPlotConstants.LT);
				Double lon = map.get(LayerPlotConstants.ZOOM_GRID_KEY + zoom).get(LayerPlotConstants.LN);
				logger.info("Found the difference latitude {} and longitude {} for zoom Level {}", lat, lon, zoom);
				nELat = nELat + lat;
				sWLng = sWLng - lon;
			} else {
				logger.info("Please make an entry in system configuration");
			}
		}
		logger.info("After Modification Updated Values sWLat {}, sWLng {}, nELat {}, nELng {}", sWLat, sWLng, nELat, nELng);
		return new Corner(sWLat, sWLng, nELat, nELng);
	}

	/**
	 * @param columnList
	 * @param minDate
	 * @param maxDate
	 * @param gridColName
	 * @param recordType
	 * @param finalResult
	 * @return
	 */
	private List<List<String>> returnAcknowlegement(List<String> columnList, String minDate, String maxDate,
													String gridColName, String recordType, List<HBaseResult> finalResult) {
		if (recordType.equalsIgnoreCase(LayerPlotConstants.DAYWISE) && !minDate.equalsIgnoreCase(maxDate)) {
			logger.info("Going to populate DayWise Response for the Result size is {}", finalResult.size());
			Map<String, List<HBaseResult>> gridWiseResult = populateResponse(finalResult, gridColName);
			return getFinalResponseList(gridWiseResult, columnList);
		} else {
			logger.info("Going to populate Latest Response for the Result size is {}", finalResult.size());
			List<List<String>> response = LayerPlotUtils.convertResultToColumnData(finalResult, columnList);
			logger.info("Finish Processing {}", new Date(System.currentTimeMillis()));
			return response;
		}
	}


	/**
	 * @param tableName
	 * @param hbaseColumnList
	 * @param prefixMap
	 * @param gridColName
	 * @param corner
	 * @param filterColName
	 * @return
	 */
	private List<HBaseResult> findResponseList(String tableName, List<String> hbaseColumnList, Map<String, List<String>> prefixMap, String gridColName, Corner corner, String filterColName) {

		List<HBaseResult> finalResult = new ArrayList<>();
		logger.info("Full Prefix List {}", prefixMap.get(GeometryConstants.FULL));
		finalResult.addAll(getPlotWiseDataFromHBase(tableName, hbaseColumnList, prefixMap.get(GeometryConstants.FULL),
				gridColName, false, corner, filterColName));
		logger.info("Partial Prefix List {}", prefixMap.get(GeometryConstants.PARTIAL));
		finalResult.addAll(getPlotWiseDataFromHBase(tableName, hbaseColumnList, prefixMap.get(GeometryConstants.PARTIAL), gridColName,
				true, corner, filterColName));
		return finalResult;

	}


	/**
	 * @param gridWiseResultMap
	 * @param columnList
	 * @return
	 */
	private List<List<String>> getFinalResponseList(Map<String, List<HBaseResult>> gridWiseResultMap, List<String> columnList) {
		logger.info("column List {} and Grid Wise Hbase Result size ====> {}", columnList, gridWiseResultMap.size());
		List<List<String>> response = new ArrayList<>();
		for (Entry<String, List<HBaseResult>> grid : gridWiseResultMap.entrySet()) {
			List<String> singleRecord = new ArrayList<>();
			for (String column : columnList) {
				List<HBaseResult> resultList = grid.getValue();
				String columnName = column.split(ForesightConstants.COLON)[1];
				String aggType = column.split(ForesightConstants.COLON)[2];

				if (aggType.equalsIgnoreCase(LayerPlotConstants.LATEST)) {
					calculateLatest(singleRecord, resultList, columnName);
				} else if (aggType.equalsIgnoreCase(LayerPlotConstants.WEIGHTED_AVERAGE)) {
					calculateWeightedAverage(singleRecord, resultList, columnName);
				} else if (aggType.equalsIgnoreCase(LayerPlotConstants.AVERAGE)) {
					calculateAverage(singleRecord, resultList, columnName);
				} else if (aggType.equalsIgnoreCase(LayerPlotConstants.SUM)) {
					calculateSumming(singleRecord, resultList, columnName);
				}
			}
			response.add(singleRecord);
		}
		logger.info("Response List Size :::::::::::: {} ", response.size());
		return response;
	}


	/**
	 * @param singleRecord
	 * @param resultList
	 * @param columnName
	 */
	private void calculateLatest(List<String> singleRecord, List<HBaseResult> resultList, String columnName) {
		String value = ForesightConstants.EMPTYSPACE;
		Long previousTimeStamp = 0L;
		for (HBaseResult singleResultSet : resultList) {
			Long timestamp = singleResultSet.getTimestamp(Bytes.toBytes(columnName));
			if (timestamp != null && timestamp > previousTimeStamp) {
				value = singleResultSet.getString(columnName);
				previousTimeStamp = timestamp;
			}
		}
		singleRecord.add(value);
	}

	/**
	 * This method is used to calculate sum of total count per grid
	 *
	 * @param singleRecord
	 * @param resultList
	 * @param columnName
	 */
	private void calculateSumming(List<String> singleRecord, List<HBaseResult> resultList, String columnName) {
		String value = ForesightConstants.EMPTYSPACE;
		Long sum = resultList.stream().filter(x -> x.getStringAsLong(columnName) != null)
				.mapToLong(v -> v.getStringAsLong(columnName)).sum();
		singleRecord.add(sum > 0 ? sum.toString() : value);
	}


	/**
	 * This Method is use to calcuate Normal Average of kpi per grid
	 *
	 * @param singleRecord
	 * @param resultList
	 * @param columnName
	 */
	private void calculateAverage(List<String> singleRecord, List<HBaseResult> resultList, String columnName) {
		String value = ForesightConstants.EMPTYSPACE;
		OptionalDouble kpiAverage = resultList.stream().filter(x -> x.getStringAsDouble(columnName) != null)
				.mapToDouble(v -> v.getStringAsDouble(columnName)).average();
		singleRecord.add(kpiAverage.isPresent() ? String.valueOf(kpiAverage.getAsDouble()) : value);
	}

	/**
	 * This method is used to calcuate Weighted Average of kpi per grid
	 *
	 * @param singleRecord
	 * @param resultList
	 * @param columnName
	 */
	private void calculateWeightedAverage(List<String> singleRecord, List<HBaseResult> resultList, String columnName) {
		String value = ForesightConstants.EMPTYSPACE;
		Double kpiAverage = resultList.stream()
				.filter(x -> x.getStringAsDouble(columnName) != null
						&& x.getStringAsDouble(columnName.concat(LayerPlotConstants.COUNT_PREFIX)) != null)
				.mapToDouble(
						v -> v.getStringAsDouble(columnName) * v.getStringAsLong(columnName.concat(LayerPlotConstants.COUNT_PREFIX)))
				.sum();
		Long kpiCount = (long) resultList.stream()
				.filter(x -> x.getStringAsDouble(columnName.concat(LayerPlotConstants.COUNT_PREFIX)) != null)
				.mapToDouble(v -> v.getStringAsLong(columnName.concat(LayerPlotConstants.COUNT_PREFIX))).sum();

		singleRecord.add(Utils.hasValue(kpiAverage) && Utils.hasValue(kpiCount) ? String.valueOf(kpiAverage / kpiCount) : value);
	}

	/**
	 * @param finalResult
	 * @param gridColName
	 * @return
	 */
	private Map<String, List<HBaseResult>> populateResponse(List<HBaseResult> finalResult, String gridColName) {
		logger.info("Going to prepare gridWise Hbase Result");
		Map<String, List<HBaseResult>> gridWiseResult = new HashMap<>();
		if (Utils.hasValue(gridColName)) {
			String[] gridColNames = gridColName.split(ForesightConstants.COMMA);
			String gLat = gridColNames[0];
			String gLon = gridColNames[1];
			gridWiseResult = finalResult.stream().filter(x -> x.getString(gLat) != null && x.getString(gLon) != null)
					.collect(Collectors.groupingBy(
							x -> x.getString(gLat) + ForesightConstants.UNDERSCORE + x.getString(gLon),
							Collectors.toList()));
		}
		return gridWiseResult;
	}


	/**
	 * @param tableName
	 * @param hbaseColumnList
	 * @param prefixList
	 * @param gridColName
	 * @param isPartial
	 * @param corner
	 * @param filterColName
	 * @return
	 */
	private List<HBaseResult> getPlotWiseDataFromHBase(String tableName,
													   List<String> hbaseColumnList, List<String> prefixList, String gridColName, boolean isPartial, Corner corner, String filterColName) {
		List<List<String>> sublist = Lists.partition(prefixList, ForesightConstants.THOUSAND);
		return getDataFromHbaseResult(tableName, hbaseColumnList, sublist, gridColName, isPartial, corner, filterColName);

	}


	/**
	 * @param tableName
	 * @param hbaseColumnList
	 * @param sublist
	 * @param gridColName
	 * @param isPartial
	 * @param corner
	 * @param filterColName
	 * @return
	 */
	private List<HBaseResult> getDataFromHbaseResult(String tableName, List<String> hbaseColumnList,
													 List<List<String>> sublist, String gridColName, boolean isPartial, Corner corner, String filterColName) {
		Scan scan = null;
		List<HBaseResult> finalResult = new ArrayList<>();
		List<HBaseResult> hbaseResultList = new ArrayList<>();
		logger.info("Going to pick up data {}", new Date(System.currentTimeMillis()));
		for (List<String> prefix : sublist) {
			LayerPlotUtils.getGridColumns(hbaseColumnList, gridColName);
			LayerPlotUtils.getGridColumns(hbaseColumnList, filterColName);
			scan = GenericMapUtils.getScanByColumnList(hbaseColumnList);
			scan.setCacheBlocks(true);
			scan.setCaching(10);
			for (String rkPrefix : prefix) {
				scan.setRowPrefixFilter(Bytes.toBytes(rkPrefix));
				try {
					hbaseResultList.addAll(layerDao.getDataFromHbase(scan, tableName, GenericMapUtils.GEOGRAPHY_CF));
				} catch (IOException e) {
					logger.info("Exception while gettting hbase data ::: {}", Utils.getStackTrace(e));
				}
			}
		}
		if (isPartial) {
			finalResult.addAll(LayerPlotUtils.filterViewPortBasisData(hbaseResultList, filterColName, corner));
		} else {
			finalResult.addAll(hbaseResultList);
		}
		logger.info("Finished Picking up Data {}", new Date(System.currentTimeMillis()));
		logger.info("Total Result List size is {}", finalResult.size());
		return finalResult;
	}

	@Override
	public List<List<String>> getBoundaryData(BoundaryDataWrapper wrapper) {
		setZoomLevelWiseColumns(wrapper);
		String geographyString = genericMapService.getBoundaryData(wrapper.getColumnList(), wrapper.getTableName(), wrapper.getnELat(), wrapper.getnELng(), wrapper.getsWLat(), wrapper.getsWLng(), wrapper.getExact(), wrapper.getSubPath(), wrapper.getArea());
     return convertBoundaryData(geographyString);
	}


	private void setZoomLevelWiseColumns(BoundaryDataWrapper wrapper) {
		List<String> columnList = new ArrayList<>();
		Integer zoomLevel = wrapper.getZoomLevel();
		String cordinates = "r:cord" + zoomLevel;
		String pkId = "r:l";
		if (zoomLevel != null) {
			if (zoomLevel >= 5 && zoomLevel < 7) {
				pkId += "1pk";
				wrapper.setSubPath(LayerPlotConstants.GEOGRAPHYL1);
				wrapper.setArea("10");
			} else if (zoomLevel >= 7 && zoomLevel < 9) {
				pkId += "2pk";
				wrapper.setArea("5");
				wrapper.setSubPath(LayerPlotConstants.GEOGRAPHYL2);
			} else if (zoomLevel >= 9 && zoomLevel < 11) {
				pkId += "3pk";
				wrapper.setSubPath(LayerPlotConstants.GEOGRAPHYL3);
			} else if (zoomLevel >= 11 && zoomLevel < 18) {
				pkId += "4pk";
				wrapper.setSubPath(LayerPlotConstants.GEOGRAPHYL4);
			}
			columnList.add(cordinates);
			columnList.add(pkId);
			columnList.add("r:dn");
			columnList.add("r:area");
			columnList.add("r:cent");
			columnList.add("r:type");
			columnList.add("r:gn");
		}
		wrapper.setColumnList(columnList);
	}

	public static List<List<String>> convertBoundaryData(String geographyData) {
		List<List<String>> list=new ArrayList<>();
		if (StringUtils.isNotEmpty(geographyData)) {
			logger.info("Geography Data {}",geographyData);
			list = new Gson().fromJson(geographyData, new TypeToken<List<List<String>>>() {
			}.getType());
			list.stream().forEach(c -> c.add(1, getRowPrefixForGeography(c.get(1), c.get(5))));

			list.forEach(c -> {
				c.remove(2);
			});
		}
		return list;
	}

	public static String getRowPrefixForGeography(String primaryKey, String geoType) {

		if (StringUtils.isNotEmpty(primaryKey)) {
			primaryKey = StringUtils.rightPad(primaryKey, 7, '0');

			switch (geoType) {
			case LayerPlotConstants.GEOGRAPHYL1: {
				return primaryKey+"A";
			}
			case LayerPlotConstants.GEOGRAPHYL2: {
				return primaryKey+"B";
			}
			case LayerPlotConstants.GEOGRAPHYL3: {
				return primaryKey+"C";
			}
			case LayerPlotConstants.GEOGRAPHYL4: {
				return primaryKey+"D";
			}
			default:
				return null;

			}

		}
		return null;
	}

	@Override
	public String getResponseForCluster(String encryptedString) {
		try {
			logger.info("Going to decrypt message {}", encryptedString);
			String decryptedJson = getDecryptedJson(encryptedString);
			CoverageLayerWrapper wrapper = new Gson().fromJson(decryptedJson, CoverageLayerWrapper.class);
			String date = SystemConfigurationUtils.systemConfMap.get(LayerPlotConstants.PARAM_COVERAGE_DASHBOARD);
			
			String dashboardTable = ConfigUtils.getString(LayerPlotConstants.COVERAGE_DASHBOARD_TABLE);
			logger.info("Table name for coverage module {}",dashboardTable);
			wrapper.setTableName(dashboardTable);
			
			logger.info("param {} and value is {}", LayerPlotConstants.PARAM_COVERAGE_DASHBOARD, date);
			if (Utils.hasValue(date)) {
				Map<String, Map<String, Object>> responseMap = new HashMap<>();
				SimpleDateFormat sf = new SimpleDateFormat(ForesightConstants.DDMMYY);
				Long timeStamp = Long.valueOf(date) ;
				Date dateObj = new Date(timeStamp);
				String format = sf.format(dateObj);
				logger.info("Going to find data for Date {}", format);
				wrapper.setDate(format);
				String json = new Gson().toJson(wrapper);
				logger.info("Parameters Found {}", json);
				StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String restCall = LayerPlotConstants.DROPWIZARD_COVERAGE_CLUSTERPLOT_URL;
				String dropwizardUrl = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue()) + restCall;
				logger.info("Found the URL for Micro-service {}", dropwizardUrl);
				String hbaseData = new HttpPostRequest(dropwizardUrl, entity).getString();
				logger.info("Found the data from hbase {}", hbaseData);
				if (Utils.hasValue(hbaseData) && (wrapper.getIsSiteCountAvailable() ==  null || !wrapper.getIsSiteCountAvailable())) {
					Type type = new TypeToken<Map<String, Map<String, Object>>>() {
					}.getType();
					responseMap = new Gson().fromJson(hbaseData, type);
					Map<Object, Object> dataMap = new HashMap<>();
					getGeographyData(wrapper, dataMap);
					Object object = dataMap.get(LayerPlotConstants.GEOGRAPHY_LEVEL_KEY);
					String rowKeyPrefixColumnName = getRowKeyPrefixColumnName(object);
					Map<String, List<Map>> filterMap = new HashMap<>();
					getFilterMap(filterMap, dataMap);
					Map<String, List<String>> projectionMap = new HashMap<>();
					getProjection(projectionMap, dataMap);
					List<Tuple> tuplesList = networkElementDao.getNetworkElementDetails(filterMap, projectionMap, null,
							null, true, true);
					List<Map<String, String>> prefixWiseResponse = InfraUtils.getMapFromTupleList(tuplesList);
					logger.info("Size of the Data is  {} Data Response {}",prefixWiseResponse.size(),prefixWiseResponse);
					if (rowKeyPrefixColumnName != null) {
						getSiteCountWiseResponse(responseMap, rowKeyPrefixColumnName, prefixWiseResponse);
					}
					logger.info("Response is {}",new Gson().toJson(responseMap));
					return new Gson().toJson(responseMap);
				}
				return hbaseData;
			} else {
				logger.info("Kindly insert an entry for the param {} in SystemConfiguration",
						LayerPlotConstants.PARAM_COVERAGE_DASHBOARD);
			}
		} catch (Exception e) {
			logger.info("Error while getting data from hbase {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private void getSiteCountWiseResponse(Map<String, Map<String, Object>> responseMap, String rowKeyPrefixColumnName,
			List<Map<String, String>> prefixWiseResponse) {
		for (Map<String, String> map : prefixWiseResponse) {
			String prefix = map.get(rowKeyPrefixColumnName);
			if (responseMap.containsKey(prefix)) {
				Map<String, Object> innerMap = responseMap.get(prefix);
				if (map.get(LayerPlotConstants.NE_STATUS_KEY) != null
						&& map.get(LayerPlotConstants.TOTAL_COUNT_KEY) != null) {
					if (((Object)map.get(LayerPlotConstants.NE_STATUS_KEY)).toString().equalsIgnoreCase(InfraConstants.ONAIR)) {
						innerMap.put(InfraConstants.ONAIR, Integer.parseInt(map.get(LayerPlotConstants.TOTAL_COUNT_KEY)));
					} else if (((Object)map.get(LayerPlotConstants.NE_STATUS_KEY)).toString().equalsIgnoreCase(InfraConstants.PLANNED)) {
						innerMap.put(InfraConstants.PLANNED, Integer.parseInt(map.get(LayerPlotConstants.TOTAL_COUNT_KEY)));
					}
				}
				responseMap.put(prefix, innerMap);
			}
		}
	}

	private String getRowKeyPrefixColumnName(Object object) {
		return object.toString().equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L4) ? LayerPlotConstants.GEOGRAPHY_L4_KEY
				: object.toString().equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L3) ? LayerPlotConstants.GEOGRAPHY_L3_KEY
						: object.toString().equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L2) ? LayerPlotConstants.GEOGRAPHY_L2_KEY
								: object.toString().equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L1) ? LayerPlotConstants.GEOGRAPHY_L1_KEY
										: null;
	}

	private void getGeographyData(CoverageLayerWrapper wrapper, Map<Object, Object> map) {
		if (wrapper != null) {
			map.put("geographyList",wrapper.getPrefixList());
			map.put("status", Arrays.asList(InfraConstants.ONAIR, InfraConstants.PLANNED));
			map.put("type", Arrays.asList(InfraConstants.MACRO));
			map.put("geographyLevel", getGeographyLevel(wrapper.getZoom()));
		}

	}

	private Object getGeographyLevel(Integer zoomLevel) {
		if (zoomLevel != null) {
			if (zoomLevel >= 5 && zoomLevel < 7) {
				return ForesightConstants.GEOGRAPHY_L1;
			} else if (zoomLevel >= 7 && zoomLevel < 9) {
				return ForesightConstants.GEOGRAPHY_L2;
			} else if (zoomLevel >= 9 && zoomLevel < 11) {
				return ForesightConstants.GEOGRAPHY_L3;
			} else if (zoomLevel >= 11 && zoomLevel < 18) {
				return ForesightConstants.GEOGRAPHY_L4;
			}
		}
		return ForesightConstants.GEOGRAPHY_L1;
	}

	private void getProjection(Map<String, List<String>> projectionMap, Map map) {
		logger.info("Map print is {}", map);
		InfraUtils.setProjectionList(projectionMap, String.valueOf(map.get(LayerPlotConstants.GEOGRAPHY_LEVEL_KEY)),"rowkeyprefix");
		InfraUtils.setProjectionList(projectionMap, InfraConstants.NETWORKELEMENT_TABLE, "neStatus");

	}

	private Map<String, List<Map>> getFilterMap(Map<String, List<Map>> filterMap, Map map) {
		List geographyFilter = new ArrayList<>();
		List neFilter = new ArrayList<>();
		InfraUtils.setFilterMap(filterMap, geographyFilter,String.valueOf(map.get(LayerPlotConstants.GEOGRAPHY_LEVEL_KEY)), "rowkeyprefix",map.get("geographyList"), "String", InfraConstants.IN_CATEGORY);
		InfraUtils.setFilterMap(filterMap, neFilter, "NetworkElement", LayerPlotConstants.NE_STATUS_KEY,map.get("status"), "NEStatus", InfraConstants.IN_CATEGORY);
		InfraUtils.setFilterMap(filterMap, neFilter, "NetworkElement", LayerPlotConstants.NE_TYPE_KEY, map.get("type"),"NEType", InfraConstants.IN_CATEGORY);
		return filterMap;

	}

	private String getDecryptedJson(String encryptedString) {
		String decryptedJson;
		try {
			decryptedJson = AuthenticationCommonUtil.checkForValueDecryption(encryptedString);
//			decryptedJson = MobileEncoder.decryptUsingCBC(encryptedString);
			logger.info("decrypted test {}", decryptedJson);
		} catch (Exception e) {
			logger.error("error in decrypting input string, error = {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return decryptedJson;
	}

	@Override
	public Object getCoverageClusterHbaseData(String json) throws IOException {
		CoverageLayerWrapper wrapper = new Gson().fromJson(json, CoverageLayerWrapper.class);
		Map<String, Map<String, Object>> response = new HashMap<>();
		if (wrapper.getColumnList() != null && wrapper.getPrefixList() != null && wrapper.getTableName() != null
				&& Utils.hasValue(wrapper.getDate())) {
			String date = wrapper.getDate();
			List<String> columnList = wrapper.getColumnList();
			List<String> prefixList = wrapper.getPrefixList();
			String tableName = wrapper.getTableName();

			Scan scan = new Scan();
			for (String prefix : prefixList) {
				Map<String, Object> innerMap = new HashMap<>();
				List<String> resultList = new ArrayList<>();
				String rowKey = prefix
						+ (Utils.hasValue(wrapper.getPostFix()) ? wrapper.getPostFix() : ForesightConstants.EMPTY)
						+ date;
				logger.info("going to find data for rowKey {}", rowKey);
				scan.setRowPrefixFilter(Bytes.toBytes(prefix));
				List<HBaseResult> hbaseResultList = layerDao.getDataFromHbase(scan, tableName,
						ForesightConstants.HBASE_COLUMN_FAMILY);
				logger.info("Size of hbase data is  {}", hbaseResultList.size());
				for (HBaseResult hbaseresult : hbaseResultList) {
					for (String columnName : columnList) {
						String column = columnName.split(ForesightConstants.COLON)[ForesightConstants.ONE];
						String columnValue = Utils.hasValue(hbaseresult.getString(column))
								? hbaseresult.getString(column)
								: ForesightConstants.EMPTY;
						resultList.add(columnValue);
					}
				}
				if (hbaseResultList.isEmpty()) {
					for (String col : columnList) {
						resultList.add(ForesightConstants.EMPTY);
					}
				}
				innerMap.put(LayerPlotConstants.DATA_KEY, resultList);
//				innerMap.put(InfraConstants.ONAIR, 0);
//				innerMap.put(InfraConstants.PLANNED, 0);
				response.put(prefix, innerMap);
			}
		}
		return response;
	}

}
