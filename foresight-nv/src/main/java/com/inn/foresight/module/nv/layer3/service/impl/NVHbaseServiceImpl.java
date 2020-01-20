package com.inn.foresight.module.nv.layer3.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.app.dao.IDeviceInfoDao;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVHbaseDao;
import com.inn.foresight.module.nv.layer3.service.INVHbaseService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.wrapper.RawDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOKpiAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOKpiItemWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOPingAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOPingItemWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOWPTAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOWPTItemWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOYoutubeAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOYoutubeItemWrapper;

@Service("NVHbaseServiceImpl")
public class NVHbaseServiceImpl extends AbstractHBaseDao implements INVHbaseService {
	/** The logger. */
	private static Logger logger = LogManager.getLogger(NVHbaseServiceImpl.class);
	@Autowired	
	private INVHbaseDao nvHbaseDao;
	@Autowired	
	private IDeviceInfoDao deviceInfoDao;
	
		@Override
	public String getLiveDriveReportData(Map<String, Map<String, List<String>>> valueMap) {
		return null;
	}
	
	@Override
	public List<RawDataWrapper> getHbaseDataForTimeStamp(Long startTime, Long endTime, String imei) {
		try {
			List<RawDataWrapper> listRawData = new ArrayList<>();
			DeviceInfo deviceInfo = deviceInfoDao.getDeviceInfoByImei(imei);
			if(deviceInfo!=null&& deviceInfo.getDeviceId()!=null){
			 setWptData(startTime, endTime, deviceInfo.getDeviceId(), listRawData);
			 setYouTubeData(startTime, endTime, deviceInfo.getDeviceId(), listRawData);
			 setActiveData(startTime, endTime, deviceInfo.getDeviceId(), listRawData);
			}

			return listRawData;
		} catch (Exception e) {
			logger.error("Exception in getSignalMessagesForBin {}	{}	{}	{}", startTime, endTime, imei,
					Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	private void setYouTubeData(Long startTime, Long endTime, String deviceId, List<RawDataWrapper> listRawData) {
		List<HBaseResult> wptResult = nvHbaseDao.getHbaseDataForTimeStamp(deviceId,
				startTime, endTime, ConfigUtils.getString(QMDLConstant.NV_YOUTUBE_RAW_TABLE_NAME));
		setYouTubeDataFromHbaseResult(listRawData, wptResult, ReportConstants.YOUTUBE_TEST);
	}

	private void setYouTubeDataFromHbaseResult(List<RawDataWrapper> listRawData, List<HBaseResult> wptResult, String testType) {
		for (HBaseResult result : wptResult) {
			try {
				RawDataWrapper wrapper = new RawDataWrapper();
				wrapper.setDlThroughput(result.getStringAsDouble(QMDLConstant.AVG_DL_RATE));
				wrapper.setBufferTime(result.getStringAsDouble(QMDLConstant.BUFFER_TIME));
				wrapper.setUrl(result.getString(QMDLConstant.WEB_URL));
				wrapper.setTestType(testType);
				wrapper.setKey(result.getRowKey());
				listRawData.add(wrapper);
			} catch (Exception e) {
				logger.error("Exception in setYouTubeDataFromHbaseResult {} ",Utils.getStackTrace(e));
			}
		}
	}

	private void setActiveData(Long startTime, Long endTime, String deviceId, List<RawDataWrapper> listRawData) {
		List<HBaseResult> wptResult = nvHbaseDao.getHbaseDataForTimeStamp(deviceId,
				startTime, endTime, ConfigUtils.getString(QMDLConstant.NV_ACTIVE_RAW_TABLE_NAME));
		setActiveDataFromHbaseResult(listRawData, wptResult, ReportConstants.ACTIVE_TEST);
	}

	private void setActiveDataFromHbaseResult(List<RawDataWrapper> listRawData, List<HBaseResult> wptResult,
			String testType) {
		for (HBaseResult result : wptResult) {
			try {
				RawDataWrapper wrapper = new RawDataWrapper();
				wrapper.setDlThroughput(result.getStringAsDouble(QMDLConstant.AVG_DL_RATE));
				wrapper.setUlThroughput(result.getStringAsDouble(QMDLConstant.AVG_UL_RATE));
				wrapper.setLatency(result.getStringAsDouble(QMDLConstant.AVG_LATENCY));
				wrapper.setPacketLoss(result.getStringAsDouble(QMDLConstant.PACKET_LOSS));
				wrapper.setTestType(testType);
				wrapper.setKey(result.getRowKey());
				listRawData.add(wrapper);
			} catch (Exception e) {
				logger.error("Exception in setActiveDataFromHbaseResult {}", Utils.getStackTrace(e));
			}
		}
	}

	private void setWptData(Long startTime, Long endTime, String deviceId, List<RawDataWrapper> listRawData) {
		List<HBaseResult> wptResult = nvHbaseDao.getHbaseDataForTimeStamp(deviceId,
				startTime, endTime, ConfigUtils.getString(QMDLConstant.WPT_RAW_TABLE_NAME));
		setWPTDataFromHbaseResult(listRawData, wptResult);
	}

	private void setWPTDataFromHbaseResult(List<RawDataWrapper> listRawData, List<HBaseResult> wptResult) {
		for (HBaseResult result : wptResult) {
			try {
				RawDataWrapper wrapper = new RawDataWrapper();
				wrapper.setUrl(result.getString(QMDLConstant.WEB_URL));
				setCommonParamIntoRawWrapper(result, wrapper,ReportConstants.WPT_TEST);
				listRawData.add(wrapper);
			} catch (Exception e) {
				logger.error("Exception in setWPTDataFromHbaseResult {}", Utils.getStackTrace(e));
			}
		}
	}

	private void setCommonParamIntoRawWrapper(HBaseResult result, RawDataWrapper wrapper, String testType) {
		wrapper.setTestType(testType );
		wrapper.setKey(result.getRowKey());
		wrapper.setCapturedTime(result.getStringAsLong(QMDLConstant.CAPTURED_ON));
	}
	
	@Override
	public List<RawDataWrapper> getAggregatedHbaseDataForRowKey(
			Map<String, Map<String, List<String>>> locationTestTypeKeyMap) {
		logger.info("Inside  getAggregatedHbaseDataForRowKey with Data {} ",new Gson().toJson(locationTestTypeKeyMap));
		List<RawDataWrapper> listRawDataWrapper = new ArrayList<>();
		
		for (Entry<String, Map<String, List<String>>> locationEntry : locationTestTypeKeyMap.entrySet()) {
			Map<String, List<String>> loationMap = locationEntry.getValue();
			if (loationMap != null) {	
				listRawDataWrapper.add(getDataForAllTestType(loationMap, locationEntry.getKey()));
			}
		}
		return listRawDataWrapper;
	}
	
	public RawDataWrapper getDataForAllTestType(Map<String, List<String>> locationMap, String locationName) {
		logger.info("Inside  getDataForAllTestType with Data locationMap {}  locationName {} ",new Gson().toJson(locationMap),locationName);
		RawDataWrapper wrapper =new RawDataWrapper();
			setActiveDataFromHbase(locationMap,wrapper, locationName);
			setYoutubeDataFromHbase(locationMap,wrapper, locationName);
			setWptDataFromHbase(locationMap,wrapper, locationName);
		
		return wrapper;
	}

	private void setWptDataFromHbase(Map<String, List<String>> locationMap, RawDataWrapper wrapper, String locationName) {
		logger.info("Inside  setWptDataFromHbase with Data locationMap {}  wrapper {}  locationName {} ",new Gson().toJson(locationMap),wrapper,locationName);
		try {
			if (locationMap.get(ReportConstants.WPT_TEST) != null
					&& ! locationMap.get(ReportConstants.WPT_TEST).isEmpty()) {
				List<HBaseResult> wptList = nvHbaseDao.getReportDataByRowKeys(locationMap.get(ReportConstants.WPT_TEST),ConfigUtils.getString(QMDLConstant.WPT_RAW_TABLE_NAME));
				logger.info("Wpt data List Size {} ",wptList.size());
				if (!wptList.isEmpty()) {
					setWptAggDataFromHbaseResult(wptList, locationName, wrapper);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getWptTestWrapperList {}", Utils.getStackTrace(e));
		}
	}

	private void setYoutubeDataFromHbase(Map<String, List<String>> locationMap, RawDataWrapper wrapper, String locationName) {
		logger.info("Inside  setYoutubeDataFromHbase with Data locationMap {}  wrapper {}  locationName {} ",new Gson().toJson(locationMap),wrapper,locationName);
		try {
			if (locationMap.get(ReportConstants.YOUTUBE_TEST) != null
					&& locationMap.get(ReportConstants.YOUTUBE_TEST).isEmpty()) {
				List<HBaseResult> youTubeList = nvHbaseDao.getReportDataByRowKeys(locationMap.get(ReportConstants.YOUTUBE_TEST),ConfigUtils.getString(QMDLConstant.NV_YOUTUBE_RAW_TABLE_NAME));
				logger.info("YouTube data List Size {} ",youTubeList.size());
				if ( youTubeList.isEmpty()) {
					setyoutubeAggDataFromHbaseResult(youTubeList, locationName,wrapper);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getYouTubeTestWapperList {}", Utils.getStackTrace(e));
		}
	}

	private void setActiveDataFromHbase(Map<String, List<String>> locationMap, RawDataWrapper wrapper, String locationName) {
		logger.info("Inside  setActiveDataFromHbase with Data locationMap {}  wrapper {}  locationName {} ",new Gson().toJson(locationMap),wrapper,locationName);
		try {
			if (locationMap.get(ReportConstants.ACTIVE_TEST) != null
					&& !locationMap.get(ReportConstants.ACTIVE_TEST).isEmpty()) {
				List<HBaseResult> activeList = nvHbaseDao.getReportDataByRowKeys(locationMap.get(ReportConstants.ACTIVE_TEST),ConfigUtils.getString(QMDLConstant.NV_ACTIVE_RAW_TABLE_NAME));
				logger.info("Active data List Size {} ",activeList.size());
				if (!activeList.isEmpty()) {
					setActiveAggDataFromHbaseResult(activeList, locationName,wrapper);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getActiveTestWrapperList {}", Utils.getStackTrace(e));
		}
	}

	private void setyoutubeAggDataFromHbaseResult(List<HBaseResult> youTubeList, String locationName, RawDataWrapper wrapper) {
		logger.info("Inside  setyoutubeAggDataFromHbaseResult with Data wptList {}  locationName {} ",new Gson().toJson(youTubeList),locationName);
		Double dlThroughput = null;
		Double bufferTime = null;
		int count=ForesightConstants.ZERO;
		for (HBaseResult result : youTubeList) {
			try {
				count++;
				Double dlSpeed = result.getStringAsDouble(QMDLConstant.DL_SPEED);
				Double bufferTimeValue = result.getStringAsDouble(QMDLConstant.BUFFER_TIME);
				logger.debug("Data in setyoutubeAggDataFromHbaseResult method Dl speed is {}  and buffer time is {}",dlSpeed,bufferTimeValue);
				if(dlSpeed != null){
				dlThroughput = dlThroughput !=null ? dlThroughput + dlSpeed : dlSpeed;
				}
				if(bufferTimeValue != null){
				bufferTime = bufferTime != null ? bufferTime + bufferTimeValue : bufferTimeValue;
				}
				
				wrapper.setLocationName(locationName);
				setCommonParamIntoRawWrapper(result, wrapper,ReportConstants.YOUTUBE_TEST);
				wrapper.setUrl(result.getString(QMDLConstant.WEB_URL));
			} catch (Exception e) {
				logger.error("Exception in setyoutubeAggDataFromHbaseResult {} ", Utils.getStackTrace(e));
			}
			setCommonParamIntoRawWrapper(result, wrapper,ReportConstants.YOUTUBE_TEST);
			wrapper.setUrl(result.getString(QMDLConstant.WEB_URL));
		}
		wrapper.setLocationName(locationName);
		if (bufferTime != null) {
			wrapper.setBufferTime(bufferTime != null ? bufferTime / count : bufferTime);
		}
		wrapper.setYtDlThroughput(dlThroughput!=null? dlThroughput/count : null);
		logger.debug("Final Youtube Wrapper {} ",wrapper);
	}
	private void setWptAggDataFromHbaseResult(List<HBaseResult> wptList, String locationName, RawDataWrapper wrapper) {
		logger.info("Inside  setWptAggDataFromHbaseResult with Data wptList {}  locationName {} ",new Gson().toJson(wptList),locationName);
		for (HBaseResult result : wptList) {
			try {
				logger.debug("getting value in setWptAggDataFromHbaseResult method Web URL {} ",result.getString(QMDLConstant.WEB_URL));
				wrapper.setUrl(result.getString(QMDLConstant.WEB_URL));
				setCommonParamIntoRawWrapper(result, wrapper,ReportConstants.WPT_TEST);
			} catch (Exception e) {
				logger.error("Exception in setWptAggDataFromHbaseResult {}",Utils.getStackTrace(e));
			}
			wrapper.setLocationName(locationName);
		}
		logger.debug("Final WPT Wrapper {} ",wrapper);
	}

	private void setActiveAggDataFromHbaseResult(List<HBaseResult> activeList, String locationName, RawDataWrapper wrapper) {
		logger.info("Inside  setActiveAggDataFromHbaseResult with Data activeList {}  locationName {} ",new Gson().toJson(activeList),locationName);
		int count = ForesightConstants.ZERO;
		Double dlThroughput = null;
		Double ulThroughput = null;
		Double latency = null;
		Double packetLoss = null;
		for (HBaseResult result : activeList) {
			try {
				count++;
				Double avgDlRate = result.getStringAsDouble(QMDLConstant.AVG_DL_RATE);
				Double avgUlRate = result.getStringAsDouble(QMDLConstant.AVG_UL_RATE);
				Double avgLatency = result.getStringAsDouble(QMDLConstant.AVG_LATENCY);
				Double pktLoss = result.getStringAsDouble(QMDLConstant.PACKET_LOSS);
				
				logger.debug("Data in setActiveAggDataFromHbaseResult dl {} ul {} latency {} packetloss {} ",avgDlRate,avgUlRate,avgLatency,pktLoss);
				
				if(avgDlRate != null){
					dlThroughput =dlThroughput!=null?dlThroughput+ avgDlRate : avgDlRate;
				}
				if(avgUlRate!=null) {
					ulThroughput = ulThroughput!=null?ulThroughput + avgUlRate : avgUlRate;
				}
				if(avgLatency!=null){
					latency = latency != null ? latency + avgLatency : avgLatency;
				}
				if(packetLoss!=null){
					packetLoss = packetLoss != null ? packetLoss + packetLoss: packetLoss;
				}
				setCommonParamIntoRawWrapper(result, wrapper,ReportConstants.ACTIVE_TEST);
		} catch (Exception e) {
			logger.error("Exception in setActiveAggDataFromHbaseResult {}", Utils.getStackTrace(e));
		}
		}
		wrapper.setDlThroughput(dlThroughput!=null?dlThroughput/count:null);
		wrapper.setUlThroughput(ulThroughput!=null?ulThroughput/count:null);
		wrapper.setLatency(latency!=null?latency/count:null);
		wrapper.setPacketLoss(packetLoss!=null?packetLoss/count:null);
		wrapper.setLocationName(locationName);
		logger.debug("Final Active Wrapper {} ",wrapper);
	}
	
	@Override
	public RawDataWrapper getSautDataWrapper(List<HBaseResult> hbaseResultList, String locationName) {
		logger.info("Inside  method get Saut Data Wrappper for location{} ", locationName);
		RawDataWrapper rawData = null;
		for (HBaseResult result : hbaseResultList) {
			try {
				String sautdata = result.getString(QMDLConstant.ADHOC_REPORT_DATA);
				String[] data = ReportUtil.convertCSVStringToDataListStationary(sautdata);
				rawData = new RawDataWrapper();
				if (data != null && data.length > ReportConstants.INDEX_ZER0) {
					rawData.setDlThroughput(ReportUtil.getDoubleValue(data[ReportConstants.INDEX_ZER0]));
					rawData.setUlThroughput(ReportUtil.getDoubleValue(data[ReportConstants.INDEX_ONE]));
					rawData.setLatency(ReportUtil.getDoubleValue(data[ReportConstants.INDEX_TWO]));
					rawData.setPacketLoss(ReportUtil.getDoubleValue(data[ReportConstants.INDEX_THREE]));
					rawData.setYtDlThroughput(ReportUtil.getDoubleValue(data[ReportConstants.INDEX_FOUR]));
					rawData.setBufferTime(ReportUtil.getDoubleValue(data[ReportConstants.INDEX_FIVE]));
					rawData.setDns(ReportUtil.getDoubleValue(data[ReportConstants.INDEX_SIX]));
					rawData.setUrlResponseTime(ReportUtil.getDoubleValue(data[ReportConstants.INDEX_SEVEN]));
					rawData.setLocationName(locationName);
				}
				return rawData;
			} catch (Exception e) {
				logger.error("unable to Set Data in Wrapper {} ", Utils.getStackTrace(e));
			}
		}
		return rawData;
	}
	
	@Override
	public String getStealthDataByWoIdAndTaskId(String woId, String taskId, String date, String clmName, Integer hour) {
		StringBuilder sb = new StringBuilder();
		String rowKey = woId;
		if (taskId != null) {
			rowKey += taskId;
		}
		if (date != null) {
			rowKey += date;
		}

		List<HBaseResult> listResults = filterHBaseResultByHour(hour,
				nvHbaseDao.getStealthDataFromHbase(rowKey, ConfigUtils.getString(QMDLConstant.STEALTH_DATA_TABLE)));

		if (listResults != null && !listResults.isEmpty()) {
			appendHbaseResultDataToStringBuilder(clmName, sb, listResults);
		}
		return sb.toString();
	}

	private void appendHbaseResultDataToStringBuilder(String clmName, StringBuilder sb, List<HBaseResult> listResults) {
		boolean isheaderAdded = false;
		for (HBaseResult result : listResults) {
			if (clmName == null || QMDLConstant.RAW_DATA.equalsIgnoreCase(clmName)) {
				String strResult = result.getString(QMDLConstant.RAW_DATA.getBytes());
				if (strResult != null && !strResult.isEmpty()) {
					sb	.append(strResult)
						.append(QMDLConstant.NEXT_LINE);
				}
			} else {
				String strResult = result.getString(clmName.getBytes());
				if (strResult != null && !strResult.isEmpty()) {
					if (!isheaderAdded) {
						isheaderAdded = addHeaderToStealthData(sb);
					}
					String headers = result.getString(QMDLConstant.STEALTH_HEADERS_COLUMNS_NAME.getBytes());
					String csvContent = ReportUtil.converToCSVStringFromArrayString(strResult);
					csvContent = ReportUtil.addHeadersDataToCsv(headers, csvContent);
					csvContent = ReportUtil.addDateToStealthCSVData(csvContent);
					sb.append(csvContent).append(QMDLConstant.NEXT_LINE);
				}
			}
		}
	}

	private boolean addHeaderToStealthData(StringBuilder sb) {
		String csvHeaders = ConfigUtils	.getString(QMDLConstant.STEALTH_DUMP_HEADERS)
										.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.COMMA_STRING);
		sb	.append(csvHeaders)
			.append(NVLayer3Constants.NEW_LINE_SEPERATOR);
		return true;
	}
	
	private List<HBaseResult> filterHBaseResultByHour(Integer hour, List<HBaseResult> hBaseResultList){
		if (hour != null) {
			List<HBaseResult> filteredResultList = new ArrayList<>();
			if (hBaseResultList != null && !hBaseResultList.isEmpty()) {
				addHbaseResultListDataToFilteredData(hour, hBaseResultList, filteredResultList);
			}
			return filteredResultList;
		}
		return hBaseResultList;
	}

	private void addHbaseResultListDataToFilteredData(Integer hour, List<HBaseResult> hBaseResultList,
			List<HBaseResult> filteredResultList) {
		for (HBaseResult hBaseResult : hBaseResultList) {
			if (hBaseResult.getString(ReportConstants.HOUR.getBytes()) == null) {
				filteredResultList.add(hBaseResult);
			} else {
				String hbaseHour = hBaseResult.getString(ReportConstants.HOUR.getBytes());
				if (NumberUtils.isCreatable(hbaseHour) && Integer.parseInt(hbaseHour) == hour) {
					filteredResultList.add(hBaseResult);
				}
			}
		}
	}
	
	private Map<String, Double>  functionByKey(String key, Object value, Map<String, Double> finalJsonDataMap) {
		if(ReportConstants.R1.equalsIgnoreCase(key) || ReportConstants.R2.equalsIgnoreCase(key) ||
				ReportConstants.R3.equalsIgnoreCase(key) || ReportConstants.R4.equalsIgnoreCase(key) ||
				ReportConstants.R5.equalsIgnoreCase(key) || ReportConstants.R6.equalsIgnoreCase(key) ||
				ReportConstants.R7.equalsIgnoreCase(key)){
			finalJsonDataMap.merge(key, value!=null?Double.parseDouble(value.toString()):null, Double::sum);
		}else if(ReportConstants.MIN.equalsIgnoreCase(key)){
			finalJsonDataMap.merge(key, value!=null?Double.parseDouble(value.toString()):null, Double::min);
		}else if(ReportConstants.MAX.equalsIgnoreCase(key)){
			finalJsonDataMap.merge(key, value!=null?Double.parseDouble(value.toString()):null, Double::max);
		}else if(ReportConstants.AVG.equalsIgnoreCase(key)){
			finalJsonDataMap.put(key, finalJsonDataMap.get(key)!=null?(finalJsonDataMap.get(key)+Double.parseDouble(value.toString()))/2.0:Double.parseDouble(value.toString()));	
		}else if(ReportConstants.COUNT.equalsIgnoreCase(key) || ReportConstants.SUM.equalsIgnoreCase(key)){
			finalJsonDataMap.merge(key, value!=null?Double.parseDouble(value.toString()):null, Double::sum);
		}
		return finalJsonDataMap;
	}

	private String getRowKeyByWoIdTaskId(String woId, String taskId, String date) {
		String rowKey=woId;
		if(taskId!=null){
			rowKey+= taskId;
		}
		if(date!=null){
			rowKey+=date;
		}
		return rowKey;
	}
	
	private List<String> getRowKeyByWoIdTaskId(String woId, List<Integer> taskIdList, String date) {
		List<String> rowKeyList = new ArrayList<>();
		if (taskIdList != null && !taskIdList.isEmpty()) {
			for (Integer taskId : taskIdList) {
				String rowKey = woId;
				if (taskId != null) {
					rowKey += taskId.toString();
				}
				if (date != null) {
					rowKey += date;
				}
				rowKeyList.add(rowKey);
			}
		} else {
			String rowKey = woId;
			rowKeyList.add(rowKey);
		}
		return rowKeyList;
	}
	
		@Override
	public String getStealthDataByWoId(String woId, String taskId, String date,String columnName) {
		StringBuilder sb = new StringBuilder();
		String rowKey = getRowKeyByWoIdTaskId(woId,taskId,date);
		List<HBaseResult> listResults = nvHbaseDao.getStealthDataFromHbase(rowKey, ConfigUtils.getString(QMDLConstant.STEALTH_DATA_TABLE));
		if (listResults != null && !listResults.isEmpty()) {
			for (HBaseResult result : listResults) {
				String strResult = result.getString(columnName.getBytes());
				if (strResult != null && !strResult.isEmpty()) {
					sb.append(strResult).append(QMDLConstant.COMMA);
				}
			}
		}
		return sb.toString();
	}
		
	@Override
	public List<HBaseResult> getHbaseResultListforStealth(String woId, List<Integer> taskIdList, String date,Long startDate, Long endDate) {
		List<String> rowKeylist = getRowKeyByWoIdTaskId(woId,taskIdList,date);
		List<HBaseResult> hbaseResultList = nvHbaseDao.getStealthDataFromHbase(rowKeylist, ConfigUtils.getString(QMDLConstant.STEALTH_DATA_TABLE));
		hbaseResultList = ReportUtil.filterStealthDataByDate(startDate, endDate, hbaseResultList);
		logger.info("Rowkey {} , listResults Size {} ",new Gson().toJson(rowKeylist),hbaseResultList!=null?hbaseResultList.size():null);
		return hbaseResultList;
	}
	
	@Override
	public List<StealthWOWPTAnalysisWrapper> getWptAnalysisDataList(List<HBaseResult> hbaseResultList,String woId, List<Integer> taskIdList, String date, Long startDate, Long endDate, String type) {
		List<StealthWOWPTAnalysisWrapper> wptDataAnalysisList = new ArrayList<>();
		if (hbaseResultList != null && !hbaseResultList.isEmpty()) {
			Map<String, List<StealthWOWPTItemWrapper>> mapperbyDate = convertToWptWrpperandGroupByDate(hbaseResultList, type);
			logger.info("Map data in getWptAnalysisDataList method {} ",mapperbyDate);
			List<StealthWOWPTItemWrapper> wptAnalysisItemList  = getWptAnalysisByDate(mapperbyDate, type);
			if(wptAnalysisItemList!=null && !wptAnalysisItemList.isEmpty()){
				StealthWOWPTAnalysisWrapper wrapper = new StealthWOWPTAnalysisWrapper();
				wrapper.setWptAnalysisDataList(wptAnalysisItemList);
				setWPTSummaryDataToWrapper(wrapper, mapperbyDate);
				wptDataAnalysisList.add(wrapper);
				logger.info("Going to return wptDataAnalysisList {} ",new Gson().toJson(wptDataAnalysisList));
				return wptDataAnalysisList;
			}
		}
		return wptDataAnalysisList;
	}
	
	private void setWPTSummaryDataToWrapper(StealthWOWPTAnalysisWrapper wrapper, Map<String, List<StealthWOWPTItemWrapper>> mapperbyDate) {
		if(mapperbyDate != null && !mapperbyDate.isEmpty()) {
			wrapper.setDaysCount(String.valueOf(mapperbyDate.size()));

			Double sumTtl = NumberUtils.DOUBLE_ZERO, sumTtfb = NumberUtils.DOUBLE_ZERO, sumDns = NumberUtils.DOUBLE_ZERO, sumTotalDns = NumberUtils.DOUBLE_ZERO;
			Double countTtl = NumberUtils.DOUBLE_ZERO, countTtfb = NumberUtils.DOUBLE_ZERO, countDns = NumberUtils.DOUBLE_ZERO, countTotalDns = NumberUtils.DOUBLE_ZERO;


			for (Entry<String, List<StealthWOWPTItemWrapper>> wptAnalysisItemList : mapperbyDate.entrySet()) {
				for (StealthWOWPTItemWrapper itemData : wptAnalysisItemList.getValue()) {
					sumTtl += (Double) getValueFromJsonForStealthData(itemData.getTtl(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getTtl(), ReportConstants.SUM): 0.0D;
					sumTtfb += (Double) getValueFromJsonForStealthData(itemData.getTtfb(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getTtfb(), ReportConstants.SUM): 0.0D;
					sumDns += (Double) getValueFromJsonForStealthData(itemData.getDns(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getDns(), ReportConstants.SUM) : 0.0D;
					sumTotalDns += (Double) getValueFromJsonForStealthData(itemData.getTotalDns(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getTotalDns(), ReportConstants.SUM) : 0.0D;
					countTtl += (Double) getValueFromJsonForStealthData(itemData.getTtl(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getTtl(), ReportConstants.COUNT) : 0;
					countTtfb += (Double) getValueFromJsonForStealthData(itemData.getTtfb(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getTtfb(), ReportConstants.COUNT) : 0;
					countDns += (Double) getValueFromJsonForStealthData(itemData.getDns(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getDns(), ReportConstants.COUNT) : 0;
					countTotalDns += (Double) getValueFromJsonForStealthData(itemData.getTotalDns(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getTotalDns(), ReportConstants.COUNT) : 0;
				}
			}
			if (countTtl > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgTtl(
						ReportUtil.parseToFixedDecimalPlace(ReportUtil.getAverage(sumTtl, countTtl.intValue()), 2).toString());
			}
			if (countTtfb > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgTtfb(
						ReportUtil.parseToFixedDecimalPlace(ReportUtil.getAverage(sumTtfb, countTtfb.intValue()), 2).toString());
			}
			if (countDns > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgDns(
						ReportUtil.parseToFixedDecimalPlace(ReportUtil.getAverage(sumDns, countDns.intValue()), 2).toString());
			}
			if (countTotalDns > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgTotalDns(ReportUtil
						.parseToFixedDecimalPlace(ReportUtil.getAverage(sumTotalDns, countTotalDns.intValue()), 2).toString());
			}
		}
	}
	
	private Object getValueFromJsonForStealthData(String json, String key) {
		try {
			if (!StringUtils.isBlank(json) && !StringUtils.isBlank(key)) {
				Type type = new TypeToken<HashMap<String, Object>>() {
					private static final long serialVersionUID = 1L;
				}.getType();
				Map<String, Object> jsonMap = new Gson().fromJson(json, type);
				return jsonMap.get(key);
			}
		} catch (Exception e) {
			logger.error("Exception inside method getDoubleValueFromJsonForStealthData {} ", Utils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public List<StealthWOKpiAnalysisWrapper> getKpiAnalysisDataList(List<HBaseResult> hbaseResultList,String woId, List<Integer> taskIdList, String date, Long startDate, Long endDate, String type) {
		List<StealthWOKpiAnalysisWrapper> kpiDataAnalysisList = new ArrayList<>();
		
		if (hbaseResultList != null && !hbaseResultList.isEmpty()) {
			Map<String, List<StealthWOKpiItemWrapper>> mapperbyDate = convertToKpiWrapperandGroupByDate(hbaseResultList, type);
			logger.info("mapperbyDate {} ",mapperbyDate);
			List<StealthWOKpiItemWrapper> kpiAnalysisItemList  = getKpiAnalysisByDate(mapperbyDate, type);
			if(kpiAnalysisItemList!=null && !kpiAnalysisItemList.isEmpty()){
				StealthWOKpiAnalysisWrapper wrapper = new StealthWOKpiAnalysisWrapper();
				wrapper.setKpiAnalysisDataList(kpiAnalysisItemList);
				setKpiSummaryDataToWrapper(wrapper, mapperbyDate);
				kpiDataAnalysisList.add(wrapper);
				logger.info("Going to return wptDataAnalysisList {} ",new Gson().toJson(kpiDataAnalysisList));
				return kpiDataAnalysisList;
			}
		}
		return kpiDataAnalysisList;
	}
	
	private void setKpiSummaryDataToWrapper(StealthWOKpiAnalysisWrapper wrapper, Map<String, List<StealthWOKpiItemWrapper>> mapperbyDate) {
		if(mapperbyDate != null && !mapperbyDate.isEmpty()) {
			wrapper.setDaysCount(String.valueOf(mapperbyDate.size()));
			Double sumRsrp = 0.0D;
			Double sumRsrq = 0.0D;
			Double sumSinr = 0.0D;
			Double sumDl = 0.0D;
			Double sumUl = 0.0D;
			Double countRsrp = 0.0D;
			Double countRsrq = 0.0D;
			Double countSinr = 0.0D;
			Double countDl = 0.0D;
			Double countUl = 0.0D;
			for (Entry<String, List<StealthWOKpiItemWrapper>> kpiAnalysisItemList : mapperbyDate.entrySet()) {
				for (StealthWOKpiItemWrapper itemData : kpiAnalysisItemList.getValue()) {
					sumRsrp += (Double) getValueFromJsonForStealthData(itemData.getRsrp(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getRsrp(), ReportConstants.SUM): 0.0D;
					sumRsrq += (Double) getValueFromJsonForStealthData(itemData.getRsrq(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getRsrq(), ReportConstants.SUM): 0.0D;
					sumSinr += (Double) getValueFromJsonForStealthData(itemData.getSinr(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getSinr(), ReportConstants.SUM) : 0.0D;
					sumDl += (Double) getValueFromJsonForStealthData(itemData.getDl(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getDl(), ReportConstants.SUM) : 0.0D;
					sumUl += (Double) getValueFromJsonForStealthData(itemData.getUl(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getUl(), ReportConstants.SUM) : 0.0D;
					countRsrp += (Double) getValueFromJsonForStealthData(itemData.getRsrp(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getRsrp(), ReportConstants.COUNT) : 0;
					countRsrq += (Double) getValueFromJsonForStealthData(itemData.getRsrq(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getRsrq(), ReportConstants.COUNT) : 0;
					countSinr += (Double) getValueFromJsonForStealthData(itemData.getSinr(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getSinr(), ReportConstants.COUNT) : 0;
					countDl += (Double) getValueFromJsonForStealthData(itemData.getDl(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getDl(), ReportConstants.COUNT) : 0;
					countUl += (Double) getValueFromJsonForStealthData(itemData.getUl(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getUl(), ReportConstants.COUNT) : 0;
				}
			}
			if (countRsrp > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgRsrp(ReportUtil.parseToFixedDecimalPlace(ReportUtil.getAverage(sumRsrp, countRsrp.intValue()), ForesightConstants.TWO).toString());
			}
			if (countRsrq > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgRsrq(ReportUtil.parseToFixedDecimalPlace(ReportUtil.getAverage(sumRsrq, countRsrq.intValue()), ForesightConstants.TWO).toString());
			}
			if (countSinr > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgSinr(ReportUtil.parseToFixedDecimalPlace(ReportUtil.getAverage(sumSinr, countSinr.intValue()), ForesightConstants.TWO).toString());
			}
			if (countDl > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgDl(ReportUtil.parseToFixedDecimalPlace(ReportUtil.getAverage(sumDl, countDl.intValue()), ForesightConstants.TWO).toString());
			}
			if (countUl > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgUl(ReportUtil.parseToFixedDecimalPlace(ReportUtil.getAverage(sumUl, countUl.intValue()), ForesightConstants.TWO).toString());
			}
		}
	}
	
	@Override
	public List<StealthWOPingAnalysisWrapper> getPingAnalysisDataList(List<HBaseResult> hbaseResultList,String woId, List<Integer> taskIdList, String date, Long startDate, Long endDate, String type) {
		List<StealthWOPingAnalysisWrapper> pingDataAnalysisList = new ArrayList<>();
		if (hbaseResultList != null && !hbaseResultList.isEmpty()) {
			Map<String, List<StealthWOPingItemWrapper>> mapperbyDate = convertToPingWrapperandGroupByDate(hbaseResultList, type);
			logger.info("mapperbyDate {} ",mapperbyDate);
			List<StealthWOPingItemWrapper> pingAnalysisItemList  = getPingAnalysisByDate(mapperbyDate, type);
			if(pingAnalysisItemList!=null && !pingAnalysisItemList.isEmpty()){
				StealthWOPingAnalysisWrapper wrapper = new StealthWOPingAnalysisWrapper();
				wrapper.setPingAnalysisDataList(pingAnalysisItemList);
				setPingSummaryDataToWrapper(wrapper, mapperbyDate);
				pingDataAnalysisList.add(wrapper);
				logger.info("Going to return pingDataAnalysisList {} ",new Gson().toJson(pingDataAnalysisList));
				return pingDataAnalysisList;
			}
		}
		return pingDataAnalysisList;
	}
	
	private void setPingSummaryDataToWrapper(StealthWOPingAnalysisWrapper wrapper, Map<String, List<StealthWOPingItemWrapper>> mapperbyDate) {
		if(mapperbyDate != null && !mapperbyDate.isEmpty()) {
			wrapper.setDaysCount(String.valueOf(mapperbyDate.size()));

			Double sumLatency = NumberUtils.DOUBLE_ZERO, sumJitter = NumberUtils.DOUBLE_ZERO, sumPacketLoss = NumberUtils.DOUBLE_ZERO;
			Double countLatency = NumberUtils.DOUBLE_ZERO, countJitter = NumberUtils.DOUBLE_ZERO, countPacketLoss = NumberUtils.DOUBLE_ZERO;


			for (Entry<String, List<StealthWOPingItemWrapper>> pingAnalysisItemList : mapperbyDate.entrySet()) {
				for (StealthWOPingItemWrapper itemData : pingAnalysisItemList.getValue()) {
					sumLatency += (Double) getValueFromJsonForStealthData(itemData.getPingLatency(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getPingLatency(), ReportConstants.SUM): NumberUtils.DOUBLE_ZERO;
					sumJitter += (Double) getValueFromJsonForStealthData(itemData.getPingJitter(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getPingJitter(), ReportConstants.SUM): NumberUtils.DOUBLE_ZERO;
					sumPacketLoss += (Double) getValueFromJsonForStealthData(itemData.getPingPacketLoss(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getPingPacketLoss(), ReportConstants.SUM) : NumberUtils.DOUBLE_ZERO;
					countLatency += (Double) getValueFromJsonForStealthData(itemData.getPingLatency(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getPingLatency(), ReportConstants.COUNT) : ForesightConstants.ZERO;
					countJitter += (Double) getValueFromJsonForStealthData(itemData.getPingJitter(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getPingJitter(), ReportConstants.COUNT) : ForesightConstants.ZERO;
					countPacketLoss += (Double) getValueFromJsonForStealthData(itemData.getPingPacketLoss(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getPingPacketLoss(), ReportConstants.COUNT) : ForesightConstants.ZERO;
				}
			}
			if (countLatency > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgPingLatency(ReportUtil
						.parseToFixedDecimalPlace(ReportUtil.getAverage(sumLatency, countLatency.intValue()), ForesightConstants.TWO).toString());
			}
			if (countJitter > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgPingJitter(ReportUtil
						.parseToFixedDecimalPlace(ReportUtil.getAverage(sumJitter, countJitter.intValue()), ForesightConstants.TWO).toString());
			}
			if (countPacketLoss > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgPingPacketLoss(ReportUtil
						.parseToFixedDecimalPlace(ReportUtil.getAverage(sumPacketLoss, countPacketLoss.intValue()), ForesightConstants.TWO).toString());
			}
		}
	}

	public List<StealthWOWPTItemWrapper> getWptAnalysisByDate(
			Map<String, List<StealthWOWPTItemWrapper>> mapperbyDate, String type) {
		List<StealthWOWPTItemWrapper> listOfWrapper = new ArrayList<>();
		if (mapperbyDate != null && !mapperbyDate.isEmpty()) {
			mapperbyDate.forEach((key,value)->{
				StealthWOWPTItemWrapper wrapper = new StealthWOWPTItemWrapper();
				wrapper.setDate(key!=null?Utils.parseDateToString(Utils.parseStringToDate(key, ReportUtil.getDateFormatForStealthReport(key, false, type)), ReportUtil.getDateFormatForStealthReport(key, true, type)):key);
				Map<String, Double>  aggregatedValueMap = getAggregatedValuesMapfromListData(mapperbyDate.get(key),ReportConstants.DNS);
				wrapper.setDns(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				aggregatedValueMap = getAggregatedValuesMapfromListData(mapperbyDate.get(key),ReportConstants.TOTAL_DNS);
				wrapper.setTotalDns(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				aggregatedValueMap = getAggregatedValuesMapfromListData(mapperbyDate.get(key),ReportConstants.TTFB);
				wrapper.setTtfb(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				aggregatedValueMap = getAggregatedValuesMapfromListData(mapperbyDate.get(key),ReportConstants.TTL);
				wrapper.setTtl(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				if(wrapper.getDns()!=null || wrapper.getTotalDns()!=null || wrapper.getTtfb()!=null || wrapper.getTtl()!=null){
				listOfWrapper.add(wrapper);
				}
			});
		}
		Comparator<StealthWOWPTItemWrapper> dateComparator = getWoptDateComparator(type);
		Collections.sort(listOfWrapper, dateComparator);
		return listOfWrapper;
	}
	
	public String getAverageFromAggrigatedValueMap(Map<String, Double> aggregatedValueMap) {
		if (aggregatedValueMap.get(ReportConstants.SUM) != null
				&& aggregatedValueMap.get(ReportConstants.COUNT) != null) {
			return ReportUtil
					.parseToFixedDecimalPlace(ReportUtil.getAverage(aggregatedValueMap.get(ReportConstants.SUM),
							aggregatedValueMap.get(ReportConstants.COUNT).intValue()), ForesightConstants.TWO)
					.toString();
		} else {
			return aggregatedValueMap.get(ReportConstants.AVG) != null
					? ReportUtil.parseToFixedDecimalPlace(aggregatedValueMap.get(ReportConstants.AVG), ForesightConstants.TWO).toString()
					: null;
		}
	}
	
	public List<StealthWOKpiItemWrapper> getKpiAnalysisByDate(
			Map<String, List<StealthWOKpiItemWrapper>> mapperbyDate, String type) {
		List<StealthWOKpiItemWrapper> listOfWrapper = new ArrayList<>();
		if (mapperbyDate != null && !mapperbyDate.isEmpty()) {
			mapperbyDate.forEach((key,value)->{
				StealthWOKpiItemWrapper wrapper = new StealthWOKpiItemWrapper();
				wrapper.setDate(key!=null?Utils.parseDateToString(Utils.parseStringToDate(key, ReportUtil.getDateFormatForStealthReport(key, false, type)), ReportUtil.getDateFormatForStealthReport(key, true, type)):key);
				Map<String, Double>  aggregatedValueMap = getAggregatedValuesMapfromKpiListData(mapperbyDate.get(key),ReportConstants.STEALTH_DATA_RSRP_JSON_COLUMN_NAME);
				wrapper.setRsrp(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				aggregatedValueMap = getAggregatedValuesMapfromKpiListData(mapperbyDate.get(key),ReportConstants.STEALTH_DATA_RSRQ_JSON_COLUMN_NAME);
				wrapper.setRsrq(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				aggregatedValueMap = getAggregatedValuesMapfromKpiListData(mapperbyDate.get(key),ReportConstants.STEALTH_DATA_SINR_JSON_COLUMN_NAME);
				wrapper.setSinr(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				aggregatedValueMap = getAggregatedValuesMapfromKpiListData(mapperbyDate.get(key),ReportConstants.STEALTH_DATA_DL_JSON_COLUMN_NAME);
				wrapper.setDl(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				aggregatedValueMap = getAggregatedValuesMapfromKpiListData(mapperbyDate.get(key),ReportConstants.STEALTH_DATA_UL_JSON_COLUMN_NAME);
				wrapper.setUl(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				if(wrapper.getRsrp()!=null || wrapper.getRsrq()!=null || wrapper.getSinr()!=null){
				listOfWrapper.add(wrapper);
				}
			});
		}
		Comparator<StealthWOKpiItemWrapper> dateComparator = getKpiDateComparator(type);
		Collections.sort(listOfWrapper, dateComparator);
		return listOfWrapper;
	}
	
	public List<StealthWOPingItemWrapper> getPingAnalysisByDate(
			Map<String, List<StealthWOPingItemWrapper>> mapperbyDate, String type) {
		List<StealthWOPingItemWrapper> listOfWrapper = new ArrayList<>();
		if (mapperbyDate != null && !mapperbyDate.isEmpty()) {
			mapperbyDate.forEach((key,value)->{
				StealthWOPingItemWrapper wrapper = new StealthWOPingItemWrapper();
				wrapper.setDate(key!=null?Utils.parseDateToString(Utils.parseStringToDate(key, ReportUtil.getDateFormatForStealthReport(key, false, type)), ReportUtil.getDateFormatForStealthReport(key, true, type)):key);
				Map<String, Double>  aggregatedValueMap = getAggregatedValuesMapfromPingListData(mapperbyDate.get(key),ReportConstants.PING_LATENCY);
				wrapper.setPingLatency(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				aggregatedValueMap = getAggregatedValuesMapfromPingListData(mapperbyDate.get(key),ReportConstants.PING_JITTER);
				wrapper.setPingJitter(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				aggregatedValueMap = getAggregatedValuesMapfromPingListData(mapperbyDate.get(key),ReportConstants.PING_PACKET_LOSS);
				wrapper.setPingPacketLoss(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				if(wrapper.getPingLatency()!=null || wrapper.getPingJitter()!=null || wrapper.getPingPacketLoss()!=null){
					listOfWrapper.add(wrapper);
				}
			});
		}
		Comparator<StealthWOPingItemWrapper> dateComparator = getPingDateComparator(type);
		Collections.sort(listOfWrapper, dateComparator);
		return listOfWrapper;
	}

	private Comparator<StealthWOWPTItemWrapper> getWoptDateComparator(String type) {
			return (o1, o2) -> Utils.parseStringToDate(o1.getDate(),ReportUtil.getDateFormatForStealthReport(o1.getDate(), true, type)).compareTo(Utils.parseStringToDate(o2.getDate(),ReportUtil.getDateFormatForStealthReport(o2.getDate(), true, type)));
	}

	private Comparator<StealthWOPingItemWrapper> getPingDateComparator(String type) {
		return (o1, o2) -> Utils.parseStringToDate(o1.getDate(),ReportUtil.getDateFormatForStealthReport(o1.getDate(), true, type)).compareTo(Utils.parseStringToDate(o2.getDate(),ReportUtil.getDateFormatForStealthReport(o2.getDate(), true, type)));
	}
	
	private Comparator<StealthWOKpiItemWrapper> getKpiDateComparator(String type) {
		return (o1, o2) -> Utils.parseStringToDate(o1.getDate(),ReportUtil.getDateFormatForStealthReport(o1.getDate(), true, type)).compareTo(Utils.parseStringToDate(o2.getDate(),ReportUtil.getDateFormatForStealthReport(o2.getDate(), true, type)));
	}
	
	private Comparator<StealthWOYoutubeItemWrapper> getDateComaprator(String type){
		return (o1, o2) -> Utils.parseStringToDate(o1.getDate(),ReportUtil.getDateFormatForStealthReport(o1.getDate(), true, type)).compareTo(Utils.parseStringToDate(o2.getDate(),ReportUtil.getDateFormatForStealthReport(o2.getDate(), true, type)));
	}

	private Map<String, Double> getAggregatedValuesMapfromListData(List<StealthWOWPTItemWrapper> list, String column) {
		Map<String, Double> finalJsonDataMap = new HashMap<>();
		if(!list.isEmpty()){
			if(ReportConstants.DNS.equalsIgnoreCase(column)){
				list.forEach(wrapper->
				 populateData(wrapper.getDns(),finalJsonDataMap)
				);
			}else if(ReportConstants.TTL.equalsIgnoreCase(column)){
				list.forEach(wrapper->
					 populateData(wrapper.getTtl(),finalJsonDataMap)
					);
			}else if(ReportConstants.TTFB.equalsIgnoreCase(column)){
				list.forEach(wrapper->
					 populateData(wrapper.getTtfb(),finalJsonDataMap)
					);
			}else if(ReportConstants.TOTAL_DNS.equalsIgnoreCase(column)){
				list.forEach(wrapper->
					 populateData(wrapper.getTotalDns(),finalJsonDataMap)
					);
			}
		}
		logger.info("result of getAggregatedValuesMapfromListData{}  ",finalJsonDataMap);
		return finalJsonDataMap;
	}

	private Map<String, Double> getAggregatedValuesMapfromPingListData(List<StealthWOPingItemWrapper> list, String column) {
		Map<String, Double> finalJsonDataMap = new HashMap<>();
		if(!list.isEmpty()){
			if(ReportConstants.PING_LATENCY.equalsIgnoreCase(column)){
				list.forEach(wrapper->
				 populateData(wrapper.getPingLatency(),finalJsonDataMap)
				);
			}else if(ReportConstants.PING_PACKET_LOSS.equalsIgnoreCase(column)){
				list.forEach(wrapper->
					 populateData(wrapper.getPingPacketLoss(),finalJsonDataMap)
					);
			}else if(ReportConstants.PING_JITTER.equalsIgnoreCase(column)){
				list.forEach(wrapper->
					 populateData(wrapper.getPingJitter(),finalJsonDataMap)
					);
			}
		}
		logger.info("result of getAggregatedValuesMapfromPingListData method {}  ",finalJsonDataMap);
		return finalJsonDataMap;
	}
	
	private Map<String, Double> getAggregatedValuesMapfromKpiListData(List<StealthWOKpiItemWrapper> list, String column) {
		Map<String, Double> finalJsonDataMap = new HashMap<>();
		if(!list.isEmpty()){
			if(ReportConstants.STEALTH_DATA_RSRP_JSON_COLUMN_NAME.equalsIgnoreCase(column)){
				list.forEach(wrapper->
				 populateData(wrapper.getRsrp(),finalJsonDataMap)
				);
			}else if(ReportConstants.STEALTH_DATA_RSRQ_JSON_COLUMN_NAME.equalsIgnoreCase(column)){
				list.forEach(wrapper->
					 populateData(wrapper.getRsrq(),finalJsonDataMap)
					);
			}else if(ReportConstants.STEALTH_DATA_SINR_JSON_COLUMN_NAME.equalsIgnoreCase(column)){
				list.forEach(wrapper->
					 populateData(wrapper.getSinr(),finalJsonDataMap)
					);
			}
		}
		logger.info("result of getAggregatedValuesMapfromKpiListData method {}  ",finalJsonDataMap);
		return finalJsonDataMap;
	}

	private Map<String, Double> populateData(String json, Map<String, Double> finalJsonDataMap) {
		try {
			if(json!=null){
				Map<String, Object> jsonMap = 	new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {
					private static final long serialVersionUID = 1L;
				}.getType());
				jsonMap.forEach((k, v) -> functionByKey(k,v,finalJsonDataMap));
			}
			logger.info("populateData final Data Map {} ",finalJsonDataMap);
		} catch (Exception e) {
			logger.error("Exception insdie method populateData {} ",Utils.getStackTrace(e));
		}
		return finalJsonDataMap;
	}

	private Map<String, List<StealthWOWPTItemWrapper>> convertToWptWrpperandGroupByDate(
			List<HBaseResult> hbaseResultList, String type) {
		List<StealthWOWPTItemWrapper> listOfWptItemWrapper = new ArrayList<>();
		StealthWOWPTItemWrapper wrapper = null;
		logger.info("hbaseResultList 1111111 Size {} ",hbaseResultList!=null?hbaseResultList.size():null);
		for (HBaseResult result : hbaseResultList) {
			try {
				wrapper = new StealthWOWPTItemWrapper();
				wrapper.setDate(ReportUtil.getStealthFormattedDate(result.getString(ReportConstants.DATE.getBytes()), result.getString(ReportConstants.HOUR.getBytes()), type));
				wrapper.setTtl(result.getString(ReportConstants.TTL.getBytes()));
				wrapper.setTtfb(result.getString(ReportConstants.TTFB.getBytes()));
				wrapper.setDns(result.getString(ReportConstants.DNS.getBytes()));
				wrapper.setTotalDns(result.getString(ReportConstants.TOTAL_DNS.getBytes()));
				listOfWptItemWrapper.add(wrapper);
			} catch (Exception e) {
				logger.error("Exception in convertToWptWrpperandGroupByDate during group by date of stealth data {} ", Utils.getStackTrace(e));
			}
		}
		if (listOfWptItemWrapper != null && !listOfWptItemWrapper.isEmpty()) {
			return listOfWptItemWrapper.stream()
					.filter(l->(l.getDate()!=null && !l.getDate().isEmpty()))
							.collect(Collectors.groupingBy(StealthWOWPTItemWrapper::getDate));
		}
		return null;
	}
	
	private Map<String, List<StealthWOKpiItemWrapper>> convertToKpiWrapperandGroupByDate(
			List<HBaseResult> hbaseResultList, String type) {
		List<StealthWOKpiItemWrapper> listOfKpiItemWrapper = new ArrayList<>();
		StealthWOKpiItemWrapper wrapper = null;
		logger.info("hbaseResultList 1111111 Size {} ",hbaseResultList!=null?hbaseResultList.size():null);
		for (HBaseResult result : hbaseResultList) {
			try {
				wrapper = new StealthWOKpiItemWrapper();
				wrapper.setDate(ReportUtil.getStealthFormattedDate(result.getString(ReportConstants.DATE.getBytes()), result.getString(ReportConstants.HOUR.getBytes()), type));
				//wrapper.setDate("250618");        // Need to remove for testing
				wrapper.setRsrp(result.getString(ReportConstants.STEALTH_DATA_RSRP_JSON_COLUMN_NAME.getBytes()));
				wrapper.setRsrq(result.getString(ReportConstants.STEALTH_DATA_RSRQ_JSON_COLUMN_NAME.getBytes()));
				wrapper.setSinr(result.getString(ReportConstants.STEALTH_DATA_SINR_JSON_COLUMN_NAME.getBytes()));
				wrapper.setDl(result.getString(ReportConstants.STEALTH_DATA_DL_JSON_COLUMN_NAME.getBytes()));
				wrapper.setUl(result.getString(ReportConstants.STEALTH_DATA_UL_JSON_COLUMN_NAME.getBytes()));
				listOfKpiItemWrapper.add(wrapper);
			} catch (Exception e) {
				logger.error("Exception in convertToKpiWrapperandGroupByDate during group by date of stealth data {} ", Utils.getStackTrace(e));
			}
		}
		if (listOfKpiItemWrapper != null && !listOfKpiItemWrapper.isEmpty()) {
			return listOfKpiItemWrapper.stream()
					.filter(l->(l.getDate()!=null && !l.getDate().isEmpty()))
							.collect(Collectors.groupingBy(StealthWOKpiItemWrapper::getDate));
		}
		return null;
	}
	
	private Map<String, List<StealthWOPingItemWrapper>> convertToPingWrapperandGroupByDate(
			List<HBaseResult> hbaseResultList, String type) {
		List<StealthWOPingItemWrapper> listOfPingItemWrapper = new ArrayList<>();
		StealthWOPingItemWrapper wrapper = null;
		for (HBaseResult result : hbaseResultList) {
			try {
				wrapper = new StealthWOPingItemWrapper();
				wrapper.setDate(ReportUtil.getStealthFormattedDate(result.getString(ReportConstants.DATE.getBytes()), result.getString(ReportConstants.HOUR.getBytes()), type));
				wrapper.setPingLatency(result.getString(ReportConstants.PING_LATENCY.getBytes()));
				wrapper.setPingPacketLoss(result.getString(ReportConstants.PING_PACKET_LOSS.getBytes()));
				wrapper.setPingJitter(result.getString(ReportConstants.PING_JITTER.getBytes()));
				listOfPingItemWrapper.add(wrapper);
			} catch (Exception e) {
				logger.error("Exception during group by date of stealth data {} ", Utils.getStackTrace(e));
			}
		}
		if (! listOfPingItemWrapper.isEmpty() ) {
			return listOfPingItemWrapper.stream()
					.filter(l->(l.getDate()!=null && !l.getDate().isEmpty()))
							.collect(Collectors.groupingBy(StealthWOPingItemWrapper::getDate));
		}
		return null;
	}

	@Override
	public List<StealthWOYoutubeAnalysisWrapper> getYoutubeAnalysisData(List<HBaseResult> hbaseResultList,Integer workorderId, List<Integer> taskIdList, Long startDate, Long endDate, String type) {
		List<StealthWOYoutubeAnalysisWrapper> youtubeDataList = new ArrayList<>();
		if (hbaseResultList != null && !hbaseResultList.isEmpty()) {
			Map<String, List<StealthWOYoutubeItemWrapper>> mapperbyDate = convertToYoutubeWrapperData(hbaseResultList, type);
			List<StealthWOYoutubeItemWrapper> youtubeWrapperList  = getYoutubeAnalysisByDate(mapperbyDate, type);
			youtubeWrapperList.stream().forEach(this::modifyValues);
			if(! youtubeWrapperList.isEmpty()){
				StealthWOYoutubeAnalysisWrapper wrapper = new StealthWOYoutubeAnalysisWrapper();
				wrapper.setYoutubeDataAnalysisList(youtubeWrapperList);
				setYoutubeSummaryDataToWrapper(wrapper, mapperbyDate);
				youtubeDataList.add(wrapper);
				logger.info("Going to return youtubeDataList {} ",new Gson().toJson(youtubeDataList));
				return youtubeDataList;
			}
		}
		return youtubeDataList;
	}
	
	private void setYoutubeSummaryDataToWrapper(StealthWOYoutubeAnalysisWrapper wrapper, Map<String, List<StealthWOYoutubeItemWrapper>> mapperbyDate) {
		if(mapperbyDate != null && !mapperbyDate.isEmpty()) {
			wrapper.setDaysCount(String.valueOf(mapperbyDate.size()));

			Double sumBuffering = NumberUtils.DOUBLE_ZERO, sumLoadTime = NumberUtils.DOUBLE_ZERO, sumFreezingRatio = NumberUtils.DOUBLE_ZERO, sumStalling = NumberUtils.DOUBLE_ZERO;
			Double countBuffering = NumberUtils.DOUBLE_ZERO, countLoadTime = NumberUtils.DOUBLE_ZERO, countFreezingRatio = NumberUtils.DOUBLE_ZERO, countStalling = NumberUtils.DOUBLE_ZERO;

			for (Entry<String, List<StealthWOYoutubeItemWrapper>> youtubeAnalysisItemList : mapperbyDate.entrySet()) {
				for (StealthWOYoutubeItemWrapper itemData : youtubeAnalysisItemList.getValue()) {
					sumBuffering += (Double) getValueFromJsonForStealthData(itemData.getBuffering(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getBuffering(), ReportConstants.SUM): NumberUtils.DOUBLE_ZERO;
					sumLoadTime += (Double) getValueFromJsonForStealthData(itemData.getLoadTime(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getLoadTime(), ReportConstants.SUM): NumberUtils.DOUBLE_ZERO;
					sumFreezingRatio += (Double) getValueFromJsonForStealthData(itemData.getFreezingRatio(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getFreezingRatio(), ReportConstants.SUM) : NumberUtils.DOUBLE_ZERO;
					sumStalling += (Double) getValueFromJsonForStealthData(itemData.getStalling(), ReportConstants.SUM) != null ? (Double) getValueFromJsonForStealthData(itemData.getStalling(), ReportConstants.SUM) : NumberUtils.DOUBLE_ZERO;
					countBuffering += (Double) getValueFromJsonForStealthData(itemData.getBuffering(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getBuffering(), ReportConstants.COUNT) : ForesightConstants.ZERO;
					countLoadTime += (Double) getValueFromJsonForStealthData(itemData.getLoadTime(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getLoadTime(), ReportConstants.COUNT) : ForesightConstants.ZERO;
					countFreezingRatio += (Double) getValueFromJsonForStealthData(itemData.getFreezingRatio(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getFreezingRatio(), ReportConstants.COUNT) : ForesightConstants.ZERO;
					countStalling += (Double) getValueFromJsonForStealthData(itemData.getStalling(), ReportConstants.COUNT) != null ? (Double) getValueFromJsonForStealthData(itemData.getStalling(), ReportConstants.COUNT) : ForesightConstants.ZERO;
				}
			}
			if (sumBuffering > ReportConstants.INDEX_ZER0 && countBuffering > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgBuffering(ReportUtil
						.parseToFixedDecimalPlace(ReportUtil.getAverage(sumBuffering, countBuffering.intValue()) / NVConstant.DOUBLE_THOUSAND, ForesightConstants.TWO)
						.toString());
			} else{
				wrapper.setAvgBuffering(NumberUtils.DOUBLE_ZERO.toString());
			}
			if (countLoadTime > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgLoadTime(ReportUtil
						.parseToFixedDecimalPlace(ReportUtil.getAverage(sumLoadTime, countLoadTime.intValue()) / NVConstant.DOUBLE_THOUSAND, ForesightConstants.TWO)
						.toString());
			}
			if (countFreezingRatio > ReportConstants.INDEX_ZER0) {
				wrapper.setAvgFreezingRatio(ReportUtil
						.parseToFixedDecimalPlace(ReportUtil.getAverage(sumFreezingRatio, countFreezingRatio.intValue()), ForesightConstants.TWO)
						.toString());
			}
			if (countStalling > ReportConstants.INDEX_ZER0) {
				Integer stalling = (int) Math.round(ReportUtil.getAverage(sumStalling, countStalling.intValue()));
				wrapper.setAvgStalling(stalling.toString());
			}
		}
	}
	
	private StealthWOYoutubeItemWrapper modifyValues(StealthWOYoutubeItemWrapper wrapper) {
		Double val =null;
			try {
				 val = wrapper.getLoadTime()!=null?(Double.parseDouble(wrapper.getLoadTime())/NVConstant.DOUBLE_THOUSAND):null;
				wrapper.setLoadTime(val!=null?ReportUtil.parseToFixedDecimalPlace(val, ReportConstants.TWO_DECIMAL_PLACES).toString():null);
				 val = wrapper.getBuffering()!=null?(Double.parseDouble(wrapper.getBuffering())/NVConstant.DOUBLE_THOUSAND):null;
				wrapper.setBuffering(val!=null?ReportUtil.parseToFixedDecimalPlace(val, ReportConstants.TWO_DECIMAL_PLACES).toString():NumberUtils.DOUBLE_ZERO.toString());
			} catch (Exception e) {
				logger.info("Number format Exception occured inside method changeLoadvalueToSec ");
			}
		return wrapper;
	}
	
		private Map<String, List<StealthWOYoutubeItemWrapper>> convertToYoutubeWrapperData(List<HBaseResult> hbaseResultList, String type) {
		List<StealthWOYoutubeItemWrapper>  listOfToutubeItemWrapper = new ArrayList<>();
		StealthWOYoutubeItemWrapper wrapper = null;
		for (HBaseResult result : hbaseResultList) {
			try {
				wrapper =new StealthWOYoutubeItemWrapper();
				wrapper.setDate(ReportUtil.getStealthFormattedDate(result.getString(ReportConstants.DATE.getBytes()), result.getString(ReportConstants.HOUR.getBytes()), type));
				wrapper.setBuffering(result.getString(ReportConstants.BUFFERING.getBytes()));
				wrapper.setFreezingRatio(result.getString(ReportConstants.FREEZING_RATIO.getBytes()));
				wrapper.setLoadTime(result.getString(ReportConstants.LOAD_TIME.getBytes()));
				wrapper.setStalling(result.getString(ReportConstants.STALLING.getBytes()));
				if(wrapper.getBuffering()!=null || wrapper.getFreezingRatio()!=null ||
						wrapper.getLoadTime()!=null || wrapper.getStalling()!=null){
					listOfToutubeItemWrapper.add(wrapper);
				}
			} catch (Exception e) {
				logger.error("Exception during group by date of stealth data {} ",Utils.getStackTrace(e));
			}
		}
		if (listOfToutubeItemWrapper != null && !listOfToutubeItemWrapper.isEmpty()) {
			return listOfToutubeItemWrapper
					.stream()
					.filter(l->(l.getDate()!=null && !l.getDate().isEmpty()))
					.collect(Collectors.groupingBy(StealthWOYoutubeItemWrapper::getDate));
		}
		return null;
	}
	
			public List<StealthWOYoutubeItemWrapper> getYoutubeAnalysisByDate(Map<String, List<StealthWOYoutubeItemWrapper>> mapperbyDate, String type) {
		List<StealthWOYoutubeItemWrapper> listOfWrapper = new ArrayList<>();
		if (mapperbyDate != null && !mapperbyDate.isEmpty()) {
			mapperbyDate.forEach((key,value)->{
				StealthWOYoutubeItemWrapper wrapper = new StealthWOYoutubeItemWrapper();
				wrapper.setDate(key!=null?Utils.parseDateToString(Utils.parseStringToDate(key, ReportUtil.getDateFormatForStealthReport(key, false, type)), ReportUtil.getDateFormatForStealthReport(key, true, type)):key);
				Map<String, Double>  aggregatedValueMap = getAggregatedYoutubeValues(value,ReportConstants.BUFFERING);
				wrapper.setBuffering(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				aggregatedValueMap = getAggregatedYoutubeValues(value,ReportConstants.FREEZING_RATIO);
				wrapper.setFreezingRatio(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				aggregatedValueMap = getAggregatedYoutubeValues(value,ReportConstants.LOAD_TIME);
				wrapper.setLoadTime(getAverageFromAggrigatedValueMap(aggregatedValueMap));
				aggregatedValueMap = getAggregatedYoutubeValues(value,ReportConstants.STALLING);
				Double stallingCount =getAverageFromAggrigatedValueMap(aggregatedValueMap) != null ? Double.parseDouble(getAverageFromAggrigatedValueMap(aggregatedValueMap)) : null;
				Long stallCount = Math.round(stallingCount);
				wrapper.setStalling((stallCount!=null)?stallCount.toString():null);
				listOfWrapper.add(wrapper);
			});
		}
		Comparator<StealthWOYoutubeItemWrapper> dateComparator = getDateComaprator(type);
		Collections.sort(listOfWrapper, dateComparator);
		return listOfWrapper;
	}

	private Map<String, Double> getAggregatedYoutubeValues(List<StealthWOYoutubeItemWrapper> list,String column) {
		Map<String, Double> finalJsonDataMap = new HashMap<>();
		if(!list.isEmpty()){
			if(ReportConstants.BUFFERING.equalsIgnoreCase(column)){
				list.forEach(wrapper->
				 populateData(wrapper.getBuffering(),finalJsonDataMap)
				);
			} else if(ReportConstants.FREEZING_RATIO.equalsIgnoreCase(column)){
				list.forEach(wrapper->
					 populateData(wrapper.getFreezingRatio(),finalJsonDataMap)
					);
			}else if(ReportConstants.LOAD_TIME.equalsIgnoreCase(column)){
				list.forEach(wrapper->
					 populateData(wrapper.getLoadTime(),finalJsonDataMap)
					);
			}else if(ReportConstants.STALLING.equalsIgnoreCase(column)){
				list.forEach(wrapper->
					 populateData(wrapper.getStalling(),finalJsonDataMap)
					);
			}
		}
		logger.info("Going to return the final json youtube data {} ",finalJsonDataMap);
		return finalJsonDataMap;
	}
	
		@Override
	public Map<String,List<String[]>> getGeographyWiseData(List<HBaseResult> hbaseResultList,String woId, List<Integer> taskIdList, String date,String columnName, Long startDate, Long endDate) {
		Map<String,List<String[]>> geographyWiseMap = null;
		hbaseResultList = ReportUtil.filterStealthDataByDate(startDate, endDate, hbaseResultList);
		logger.info("hbaseResultList Size for woId is {} , {} ",hbaseResultList!=null?hbaseResultList.size():null,woId);
		Map<String,List<HBaseResult>> geographyWiseHbaseResult = getGeaographyWiseResultList(hbaseResultList);
		logger.info("geographyWiseHbaseResult Size for woId is {} , {} ",geographyWiseHbaseResult!=null?geographyWiseHbaseResult.size():null,woId);
		if(geographyWiseHbaseResult!=null && !geographyWiseHbaseResult.isEmpty()){
			return getGeographyWiseMappeData(geographyWiseHbaseResult,columnName);
		}
		return geographyWiseMap;
	}

	private Map<String, List<String[]>> getGeographyWiseMappeData(
			Map<String, List<HBaseResult>> geographyWiseHbaseResult, String columnName) {
		Map<String, List<String[]>> geographyWiseMap = new HashMap<>();
		Map<String, String> geographyWiseCombinedData = getGeographyWiseCombinedData(geographyWiseHbaseResult,
				columnName);
		if (!geographyWiseCombinedData.isEmpty()) {
			geographyWiseCombinedData.forEach((key, value) -> {
				List<String[]> dataArray = convertStringToDataList(value);
				List<String[]> filteredDataArray = dataArray.stream()
															.filter(x -> x != null && x.length
																	> ReportConstants.STEALTH_JSON_NETWORK_TYPE_INDEX
																	&& !StringUtils.isBlank(
																	x[ReportConstants.STEALTH_JSON_NETWORK_TYPE_INDEX])
																	&& ReportConstants.TECHNOLOGY_LTE.equalsIgnoreCase(
																	x[ReportConstants.STEALTH_JSON_NETWORK_TYPE_INDEX]))
															.collect(Collectors.toList());
				geographyWiseMap.put(key, filteredDataArray);
			});
		}
		return geographyWiseMap;
	}

	private Map<String, String> getGeographyWiseCombinedData(Map<String, List<HBaseResult>> geographyWiseHbaseResult,
			String columnName) {
		Map<String,String> geographyWiseCombinedData = new HashMap<>();
		geographyWiseHbaseResult.forEach((key,value)->{
			if (value != null && !value.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (HBaseResult result : value) {
					String strResult = result.getString(columnName.getBytes());
					if (strResult != null && !strResult.isEmpty()) {
						sb.append(strResult).append(QMDLConstant.COMMA);
					}
				}
				geographyWiseCombinedData.put(key, sb.toString());
			}
		});
		return geographyWiseCombinedData;
	}

	private Map<String, List<HBaseResult>> getGeaographyWiseResultList(List<HBaseResult> hbaseResultList) {
		Map<String, List<HBaseResult>> geographyWiseResultMap  =  null;
		if (hbaseResultList != null && !hbaseResultList.isEmpty()) {
			return hbaseResultList.stream()
					.filter(hbaseResult->hbaseResult.getString(ReportConstants.GEOGRAPHY_COLUMN_GL4.getBytes())!=null)
					.collect(Collectors.groupingBy(hbaseResult->hbaseResult.getString(ReportConstants.GEOGRAPHY_COLUMN_GL4.getBytes())));
		}
		return geographyWiseResultMap;
	}
	
		private List<String[]> convertStringToDataList(String data) {
		logger.info("insde the method convertCSVStringToDataList");
		List<String[]> arlist = new ArrayList<>();
		try {
			if (!StringUtils.isBlank(data) && !"[]".equalsIgnoreCase(data)) {
				String[] ar = data.split("]\\,\\[", -1);
				List<String> list = Arrays.asList(ar);
				for (String string : list) {
					String newStr = string.replace("//]", "").replace("[\\", "").replace("\\[", "");
					newStr = newStr.replace("//]", "").replace("[\\", "").replace("\\[", "");
					String[] arr = newStr.split(",", -1);
					arlist.add(arr);
				}
			}
		} catch (Exception e) {
			logger.info("error to cast string to String Array{}", Utils.getStackTrace(e));
		}
		return arlist;
	}
	
	@Override
	public List<HBaseResult> getHBaseResultListForStealthReport(String woId, List<Integer> taskIdList, String date, String columnName) {
		Set<String> rowKeySet = new HashSet<>(getRowKeyByWoIdTaskId(woId,taskIdList,date));
		List<String> rowKeyList = new ArrayList<>(rowKeySet);
		return nvHbaseDao.getStealthDataFromHbase(rowKeyList, ConfigUtils.getString(QMDLConstant.STEALTH_DATA_TABLE));
	}
	
	@Override
	public void deletePreviousDataForLayer3(Integer workorderId, Integer recipeId) {
		try {
			List<String> tableNameList  = new ArrayList<>();
			String rowPrefix = NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId);
			if(recipeId != null) {
				rowPrefix = rowPrefix + NVLayer3Utils.getRowkeyFromWorkOrderId(recipeId);
			}
			
			tableNameList.add(ConfigUtils.getString(NVLayer3Constants.LAYER3_DRIVE_DETAIL_TABLE));
			tableNameList.add(ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE));
			tableNameList.add(ConfigUtils.getString(NVLayer3Constants.LAYER3_MESSAGES_DATA_TABLE));
			tableNameList.add(ConfigUtils.getString(NVLayer3Constants.LAYER3_DRIVE_SUMMAARY_TABLE));
			tableNameList.add(ConfigUtils.getString(NVLayer3Constants.LAYER3_EVENT_TABLE));
			
			
			 for (String tableName : tableNameList) {
				logger.info("table names for layer 3 {}",tableName);
				deleteDataUsingRowPrefix(rowPrefix, tableName);
			}
		} catch (Exception e) {
		logger.error("Exception while delete data from hbase for WO id {} and recipeId {}",workorderId,recipeId);
		}
	}
	
	

	@Override
	public Boolean deleteDataUsingRowPrefix(String rowPrefix, String tableName) {
		return  deleteFromRowPreFilter(tableName,rowPrefix);

	}
	
}
