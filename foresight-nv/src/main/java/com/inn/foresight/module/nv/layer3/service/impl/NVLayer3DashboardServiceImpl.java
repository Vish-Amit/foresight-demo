package com.inn.foresight.module.nv.layer3.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.nns.NNS;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.maplayer.dao.IGenericMapDao;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HbaseDao;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;
import com.inn.foresight.module.nv.layer3.service.INVHbaseService;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.wrapper.EventWrapper;
import com.inn.foresight.module.nv.layer3.wrapper.KpiAdvanceWrapper;
import com.inn.foresight.module.nv.layer3.wrapper.KpiEventsWrapper;
import com.inn.foresight.module.nv.layer3.wrapper.KpiWrapper;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3ReportWrapper;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3StatisticsWrapper;
import com.inn.foresight.module.nv.layer3.wrapper.ListWrapper;
import com.inn.foresight.module.nv.report.RangeSlab;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ExcelReportUtils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.utils.Statistics;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.report.utils.ZipUtils;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.RecipeMappingWrapper;
import com.inn.foresight.module.nv.report.wrapper.RemarkDataWrapper;
import com.inn.foresight.module.nv.service.ISiteDetailService;
import com.inn.foresight.module.nv.workorder.dao.IWOFileDetailDao;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping.Status;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;
import com.inn.foresight.module.nv.workorder.service.INVWorkorderService;
import com.inn.foresight.module.nv.workorder.service.IWOFileDetailService;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;

@Service("NVLayer3DashboardService")
public class NVLayer3DashboardServiceImpl extends NVLayer3Utils implements INVLayer3DashboardService {

	@Autowired
	private IWOFileDetailService woFileDetailService;

	@Autowired
	private IWOFileDetailDao woFileDetailDao;

	@Autowired
	private INVWorkorderService iNVWorkorderService;

	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;
	@Autowired
	private INVLayer3HbaseDao nvLayer3HbaseDao;

	@Autowired
	private IGenericWorkorderDao genericWorkorderDao;

	@Autowired
	private INVHbaseService nvHbaseService;

	@Autowired
	private ILegendRangeDao legendRangeDao;

	@Autowired
	private INVLayer3HDFSDao nvLayer3HdfsDao;

	@Autowired
	private ISiteDetailService siteDetailService;

	@Autowired
	private IGenericMapService iGenericMapService;

	@Autowired
	private IGenericMapDao genericMapServiceDao;

	@Autowired
	private IWORecipeMappingDao woRecipeMappingdao;

	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;

	private static Map<Integer, NNS<LatLng>> nnsMap = new HashMap<>();

	NVLayer3DashboardServiceImpl() {
	}

	private Logger logger = LogManager.getLogger(NVLayer3DashboardServiceImpl.class);

	@Override
	@Transactional
	public String getDriveRecipeDetailJson(Integer workrorderId) {
		try {
			List<WOFileDetail> woFileList = woFileDetailService.getFileDetailByWorkOrderId(workrorderId);
			return new Gson().toJson(getMappingDataFromWoFile(woFileList));
		} catch (RestException e) {
			logger.error("Getting Exception in getDriveRecipeDetailJson   {} ", workrorderId, Utils.getStackTrace(e));
		}
		return QMDLConstant.NO_RESULT_FOUND;
	}

	private void setDriveDetailIntoMap(Map<String, Map<String, String>> recipeMapping,
			Map<String, String> operatorMapping, WOFileDetail woFile, List<Integer> unqueId) {
		String recipeMappingId = woFile.getWoRecipeMapping().getRecipe().getName();
		if ((unqueId == null || !unqueId.contains(woFile.getWoRecipeMapping().getId())) && recipeMappingId != null) {

			addRecipeIntoMap(recipeMapping, woFile, recipeMappingId);
			if (unqueId != null) {
				unqueId.add(woFile.getWoRecipeMapping().getId());
			}

		}
		addOperatorIntoMap(operatorMapping, woFile);
	}

	private void addOperatorIntoMap(Map<String, String> operatorMapping, WOFileDetail woFile) {
		String operatorName = NVWorkorderUtils.getOperatorNameFromFileName(woFile.getFileName());
		if (operatorName != null) {
			operatorMapping.put(operatorName, operatorName);
		}
	}

	private void addRecipeIntoMap(Map<String, Map<String, String>> recipeMapping, WOFileDetail woFile,
			String recipeName) {
		if (NVLayer3Utils.isInBuidlingRecord(woFile)) {
			getRecipeMapForInbuilding(recipeMapping, woFile, recipeName);
		} else if (isSSVTRecord(woFile)) {
			getRecipeMapForSSVT(recipeMapping, woFile, recipeName);
		} else if (isBenchmarkRecord(woFile)) {
			putRecipeMapForDriveBenchmark(recipeMapping, woFile, recipeName);
		} else {
			putRecipeIntoMap(recipeMapping, woFile, recipeName);
		}
	}

	private void putRecipeIntoMap(Map<String, Map<String, String>> recipeMapping, WOFileDetail woFile,
			String recipeName) {
		if (recipeMapping.containsKey(recipeName)) {
			Map<String, String> recipeOperatorMap = new HashMap<>();
			recipeOperatorMap.put(String.valueOf(woFile.getWoRecipeMapping().getId()),
					NVWorkorderUtils.getOperatorNameFromFileName(woFile.getFileName()));
			recipeMapping.put(NVLayer3Utils.getPaddedKeyForMapForLayer3(recipeMapping, recipeName + Symbol.UNDERSCORE,
					QMDLConstant.PADDING_VALUE_STR), recipeOperatorMap);
		} else {
			Map<String, String> recipeOperatorMap = new HashMap<>();
			recipeOperatorMap.put(String.valueOf(woFile.getWoRecipeMapping().getId()),
					NVWorkorderUtils.getOperatorNameFromFileName(woFile.getFileName()));
			recipeMapping.put(recipeName, recipeOperatorMap);
		}
	}

	private void getRecipeMapForSSVT(Map<String, Map<String, String>> recipeMapping, WOFileDetail woFile,
			String recipeName) {
		Integer pci = getValueFromGWOMap(woFile.getWoRecipeMapping(), QMDLConstant.SSVT_MAP_KEY);
		if (pci != null) {
			Map<String, String> recipeOperatorMap = new HashMap<>();
			recipeOperatorMap.put(String.valueOf(woFile.getWoRecipeMapping().getId()),
					NVWorkorderUtils.getOperatorNameFromFileName(woFile.getFileName()));
			recipeMapping.put(recipeName + Symbol.UNDERSCORE_STRING + pci, recipeOperatorMap);
		} else {
			putRecipeIntoMap(recipeMapping, woFile, recipeName);
		}
	}

	private void getRecipeMapForInbuilding(Map<String, Map<String, String>> recipeMapping, WOFileDetail woFile,
			String recipeName) {
		Integer unitid = getValueFromGWOMap(woFile.getWoRecipeMapping(), QMDLConstant.IN_BUILDING_MAP_KEY);
		if (unitid != null) {
			Map<String, String> recipeOperatorMap = new HashMap<>();
			recipeOperatorMap.put(String.valueOf(woFile.getWoRecipeMapping().getId()),
					NVWorkorderUtils.getOperatorNameFromFileName(woFile.getFileName()));
			recipeMapping.put(recipeName + Symbol.UNDERSCORE_STRING + unitid, recipeOperatorMap);
		} else {
			putRecipeIntoMap(recipeMapping, woFile, recipeName);
		}
	}

	private Integer getValueFromGWOMap(WORecipeMapping woRecipeMapping, String ssvtMapKey) {
		if (woRecipeMapping.getGenericWorkorder().getGwoMeta() != null) {
			Map<String, String> gwoMap = woRecipeMapping.getGenericWorkorder().getGwoMeta();
			String inBuildingMapJson = gwoMap.get(ssvtMapKey);
			if (inBuildingMapJson != null) {
				Map<Integer, Integer> inBuildingMap = new Gson().fromJson(inBuildingMapJson,
						new TypeToken<HashMap<Integer, Integer>>() {
						}.getType());
				return inBuildingMap.get(woRecipeMapping.getId());

			}
		}
		return null;
	}

	private boolean isSSVTRecord(WOFileDetail woFile) {
		return woFile.getWoRecipeMapping().getGenericWorkorder().getTemplateType().name()
				.equals(TemplateType.NV_SSVT.name());
	}
	
	private boolean isScannerRecord(WOFileDetail woFile) {
		return woFile.getWoRecipeMapping().getGenericWorkorder().getTemplateType().name()
				.equals(TemplateType.SCANNING_RECEIVER.name());
	}

	

	private boolean setDefaultDetailIntoMap(Map<String, Object> fileMapping,
			Map<String, Map<String, String>> recipeMappingDefault, Map<String, String> operatorMappingDefault,
			boolean isFirstRecord, WOFileDetail woFile, List<Integer> uniqueId) {
		if (isFirstRecord && woFile.getIsProcessed()) {
			setDriveDetailIntoMap(recipeMappingDefault, operatorMappingDefault, woFile, uniqueId);
			fileMapping.put(QMDLConstant.RECIPE_DEFAULT, recipeMappingDefault);
			fileMapping.put(QMDLConstant.OPERATOR_DEFAULT, operatorMappingDefault);
			isFirstRecord = Boolean.FALSE;
		}
		return isFirstRecord;
	}

	@Override
	@Transactional
	public String getDriveRecipeDetailForLayer3(Integer workrorderId) {
		try {
			List<WOFileDetail> woFileList = woFileDetailService.getFileDetailByWorkOrderId(workrorderId);
			return new Gson().toJson(getMappingDataFromWoFileForLayer3(woFileList));
		} catch (RestException e) {
			logger.error("Getting Exception in getDriveRecipeDetailForLayer3 {}   {} ", workrorderId,
					Utils.getStackTrace(e));
		}
		return QMDLConstant.NO_RESULT_FOUND;
	}

	@Override
	@Transactional
	public String getDynamicKpiAndEvents(Integer workrorderId, List<Integer> recipeList) {
		try {
			List<String> kpiEventsList = iNVWorkorderService.geKpiAndEventString(workrorderId, recipeList);
			kpiEventsList.add(NVLayer3Constants.COMMON);

			if (!kpiEventsList.isEmpty()) {
				logger.debug("list of kpiEventsList {}", kpiEventsList.toString());
				List<SystemConfiguration> sysConnfigList = getNVLayer3SystemConfigurationByNameAndType(kpiEventsList,
						NVLayer3Constants.TYPE_PREFIX);
				if (sysConnfigList != null && !sysConnfigList.isEmpty()) {
					Map<String, List<String>> kpiEventsMap = setKpiEventsInMap(sysConnfigList);
					logger.debug("Getting the  kpiEventsMap {}", new Gson().toJson(kpiEventsMap));
					return getJsonFromKpiMap(kpiEventsMap);
				}
			} else {
				return QMDLConstant.NO_RESULT_FOUND;
			}
		} catch (RestException e) {
			logger.error("Getting Exception in getDynamicKpiAndEvents   {} ", workrorderId, Utils.getStackTrace(e));
			return QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG;
		}
		return QMDLConstant.NO_RESULT_FOUND;

	}

	public String getJsonFromKpiMap(Map<String, List<String>> kpiEventsMap) {
		KpiEventsWrapper kpieventwrapper = new KpiEventsWrapper();
		List<KpiAdvanceWrapper> kpiAdvanceWrapperList = new ArrayList<>();
		List<EventWrapper> eventwrapperList = new ArrayList<>();
		List<KpiWrapper> kpiwrapper3glist = new ArrayList<>();
		List<KpiWrapper> kpiwrapper2glist = new ArrayList<>();

		for (Map.Entry<String, List<String>> entry : kpiEventsMap.entrySet()) {
			setkpiintojson(kpiAdvanceWrapperList, entry, NVLayer3Constants.KPI);
			setkpiintojson(kpiAdvanceWrapperList, entry, NVLayer3Constants.LINK_ADAPTATION);
			setkpiintojson(kpiAdvanceWrapperList, entry, NVLayer3Constants.DOMINANT);
			setkpiintojson(kpiAdvanceWrapperList, entry, NVLayer3Constants.MCS_INFO);
			setkpiintojson(kpiAdvanceWrapperList, entry, NVLayer3Constants.DATA_THROUGHPUT);
			setkpiintojson(kpiAdvanceWrapperList, entry, NVLayer3Constants.STATE_INFO);
			setkpiintojson(kpiAdvanceWrapperList, entry, NVLayer3Constants.VOLTE);

			setkpiintojsonFor3G(kpieventwrapper, kpiwrapper3glist, entry, NVLayer3Constants.KPI_3G);
			setkpiintojsonFor2G(kpieventwrapper, kpiwrapper2glist, entry, NVLayer3Constants.KPI_2G);
			setEventIntoJson(eventwrapperList, entry, NVLayer3Constants.VOLTE_CALL);
			setEventIntoJson(eventwrapperList, entry, NVLayer3Constants.MOBILITY);
			setEventIntoJson(eventwrapperList, entry, NVLayer3Constants.SMS);
			setEventIntoJson(eventwrapperList, entry, NVLayer3Constants.TAU);
			setEventIntoJson(eventwrapperList, entry, NVLayer3Constants.RRC);
			setEventIntoJson(eventwrapperList, entry, NVLayer3Constants.CSFB);

		}
		kpieventwrapper.setKpiAdvanceWrapperlist(kpiAdvanceWrapperList);
		kpieventwrapper.setEventwrapperlist(eventwrapperList);
		return addCommonValuesIntoJson(kpiEventsMap, kpieventwrapper);
	}

	private void setkpiintojson(List<KpiAdvanceWrapper> kpiAdvanceWrapperList, Entry<String, List<String>> entry,
			String kpiName) {
		if (entry.getKey().contains(kpiName) && !entry.getKey().contains("3G") && !entry.getKey().contains("2G")) {
			KpiAdvanceWrapper kpiAdvanceWrapper = new KpiAdvanceWrapper();
			List<KpiWrapper> kpiWrapperList = new ArrayList<>();
			for (String ss : entry.getValue()) {
				KpiWrapper kpiWrapper = new Gson().fromJson(ss, KpiWrapper.class);
				kpiWrapperList.add(kpiWrapper);
			}
			kpiAdvanceWrapper.setKpiListwrapper(kpiWrapperList);
			kpiAdvanceWrapper.setName(kpiName);
			kpiAdvanceWrapperList.add(kpiAdvanceWrapper);
		}

	}

	public String addCommonValuesIntoJson(Map<String, List<String>> kpiEventsMap, KpiEventsWrapper kpieventwrapper) {
		String kpiEventString = new Gson().toJson(kpieventwrapper);

		if (kpiEventsMap.containsKey(NVLayer3Constants.COMMON)) {
			String common = kpiEventsMap.get(NVLayer3Constants.COMMON).get(0);

			StringBuilder finalstring = new StringBuilder();
			finalstring.append(kpiEventString);
			finalstring.setLength(finalstring.length() - QMDLConstant.STRING_EXTRA_LENTH);
			finalstring.append(Symbol.COMMA);
			finalstring.append(common);
			finalstring.append(Symbol.BRACE_CLOSE_STRING);
			return finalstring.toString();
		} else {
			return kpiEventString;
		}
	}

	public void setEventIntoJson(List<EventWrapper> eventwrapperList, Map.Entry<String, List<String>> entry,
			String eventname) {
		if (entry.getKey().contains(NVLayer3Constants.EVENT) && entry.getKey().contains(eventname)) {
			EventWrapper eventwrapper = new EventWrapper();
			List<ListWrapper> listwrapperList = new ArrayList<>();
			for (String ss : entry.getValue()) {
				ListWrapper listwrapper = new Gson().fromJson(ss, ListWrapper.class);
				listwrapperList.add(listwrapper);
			}
			eventwrapper.setListwrapper(listwrapperList);
			eventwrapper.setName(eventname);
			eventwrapperList.add(eventwrapper);
		}
	}

	public void setkpiintojsonFor3G(KpiEventsWrapper kpieventwrapper, List<KpiWrapper> kpiwrapperlist,
			Map.Entry<String, List<String>> entry, String kpiname) {
		if (entry.getKey().contains(kpiname)) {
			for (String ss : entry.getValue()) {
				KpiWrapper kpiwrapper = new Gson().fromJson(ss, KpiWrapper.class);
				kpiwrapperlist.add(kpiwrapper);
			}

			kpieventwrapper.setKpi3gwrapperlist(kpiwrapperlist);
		}
	}

	public void setkpiintojsonFor2G(KpiEventsWrapper kpieventwrapper, List<KpiWrapper> kpiwrapperlist,
			Map.Entry<String, List<String>> entry, String kpiname) {
		if (entry.getKey().contains(kpiname)) {
			for (String ss : entry.getValue()) {
				KpiWrapper kpiwrapper = new Gson().fromJson(ss, KpiWrapper.class);
				kpiwrapperlist.add(kpiwrapper);
			}
			kpieventwrapper.setKpi2gwrapperlist(kpiwrapperlist);
		}
	}

	public Map<String, List<String>> setKpiEventsInMap(List<SystemConfiguration> sysConnfigList) {
		Map<String, List<String>> kpiEventsMap = new HashMap<>();
		for (SystemConfiguration sysConnfig : sysConnfigList) {

			String[] kpiType = sysConnfig.getType().split(Symbol.UNDERSCORE_STRING, QMDLConstant.KPI_SPLIT_COUNT);
			String kpivalue = sysConnfig.getValue();

			List<String> kpiValueList = kpiEventsMap.containsKey(kpiType[QMDLConstant.KPI_NAME_INDEX])
					? kpiEventsMap.get(kpiType[QMDLConstant.KPI_NAME_INDEX])
					: new ArrayList<>();
			kpiValueList.add(kpivalue);
			kpiEventsMap.put(kpiType[QMDLConstant.KPI_NAME_INDEX], kpiValueList);
		}
		return kpiEventsMap;
	}

	@Override
	@Transactional
	public List<SystemConfiguration> getNVLayer3SystemConfigurationByNameAndType(List<String> nameList,
			String paramPrefix) {
		try {
			logger.debug("getting List of SystemConfiguration");
			if (paramPrefix != null && nameList != null && !nameList.isEmpty()) {
				return iSystemConfigurationDao.getSystemConfigurationByTypePrefixAndNameList(nameList, paramPrefix);
			}

		} catch (Exception e) {
			logger.error("Getting Exception in file information {} ", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	private String getParsedSignalMessageData(HBaseResult result, String searchTerm) {
		StringBuilder json = new StringBuilder();
		json.append(Symbol.BRACKET_OPEN_STRING);
		addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
		addValueToStringBuilder(json, result.getString(QMDLConstant.SIP_EVENT));
		addValueToStringBuilder(json, result.getString(QMDLConstant.EMM_MESSAGE));
		addValueToStringBuilder(json, result.getString(QMDLConstant.EMS_MESSAGE));
		addValueToStringBuilder(json, result.getString(QMDLConstant.B0C0_MSG_TYPE));
		addValueToStringBuilder(json, result.getRowKey());
		if (isSearchTermAvailableInHBaseResult(result, searchTerm)) {
			addValueToStringBuilder(json, ForesightConstants.ONE_IN_STRING);
		} else {
			addValueToStringBuilder(json, ForesightConstants.ZERO_IN_STRING);
		}
		addValueToStringBuilder(json, result.getString(QMDLConstant.DIRECTION));
		json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
		json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		return json.toString();
	}

	@Override
	public String getSignalMessageDetail(String rowKey, String msgType) {
		try {
			String tableName = ConfigUtils.getString(NVLayer3Constants.QMDL_SIGNALING_DATA_TABLE);
			HBaseResult hbaseResult = nvLayer3HbaseDao.getSignalMessageDetail(tableName, msgType, rowKey);
			if (hbaseResult != null) {
				return getJsonFromDetailMsg(hbaseResult.getString(msgType));
			}
		} catch (Exception e) {
			logger.error("Error in Getting getSignalMessageDetail from hbase {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
		return QMDLConstant.NO_RESULT_FOUND;
	}

	private String getJsonFromDetailMsg(String stringValue) {
		return QMDLConstant.RESULT + stringValue + QMDLConstant.MSG_POSTFIX;
	}

	@Override
	public String getKpiStatsData(Integer workorderId, String kpi) {
		try {
			String tableName = ConfigUtils.getString(NVLayer3Constants.QMDL_KPI_TABLE);
			HBaseResult hbaseResult = nvLayer3HbaseDao.getKpiStatsData(tableName, QMDLConstant.RANGE_STATS,
					NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId), kpi);
			if (hbaseResult != null) {
				return getJsonFromDetailMsg(hbaseResult.getString(QMDLConstant.RANGE_STATS));
			}
		} catch (Exception e) {
			logger.error("Error in Getting getKpiStatsData from hbase {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
		return QMDLConstant.NO_RESULT_FOUND;
	}

	@Override
	public String getDriveSummaryReceipeWise(Integer workorderId, List<String> recipeId, List<String> operatorList) {
		List<String> columnList = getSummaryColumnList();
		List<Get> getList = getListForSummary(workorderId, recipeId, operatorList, columnList);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_DATA_TABLE);
		logger.info("going to get data for getList: {}",getList);
		try {
			List<HBaseResult> hbaseResultList = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			logger.info("hbaseResultList : {}", hbaseResultList);
			String summaryJsonLTE = getFinalSummaryFromResult(hbaseResultList);
			String summaryJson3G = get3GSummaryFromResult(hbaseResultList);
			String summaryJson2G = get2GSummaryFromResult(hbaseResultList);
			Map<String, String> summaryMap = new HashMap<>();
			logger.info("summaryJsonLTE : {}", summaryJsonLTE);
			summaryMap.put("summary", summaryJsonLTE);
			summaryMap.put("summary_3G", summaryJson3G);
			summaryMap.put("summary_2G", summaryJson2G);
			return getFinalJsonForSummary(new Gson().toJson(summaryMap));
		} catch (IOException e) {
			logger.error("IOException in Getting   getDriveSummaryReceipeWise  Result {}  ", e.getMessage());
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("BusinessException in Getting getDriveSummaryReceipeWise Result {}  ", e.getMessage());
			return QMDLConstant.INVALID_ARGUMENT;
		}
	}

	private String getFinalJsonForSummary(String stringValue) {
		if (stringValue != null) {
			StringBuilder finalJson = getInitialJsonForSummary();
			finalJson.append(stringValue);
			addPostFixOfcsvIntoJsonForSummary(finalJson);
			return finalJson.toString();
		}
		return QMDLConstant.NO_RESULT_FOUND;
	}

	private void addPostFixOfcsvIntoJsonForSummary(StringBuilder finalJsonString) {

		if (finalJsonString.length() > QMDLConstant.EMPTY_JSON_LENGTH) {
			finalJsonString.setLength(finalJsonString.length() - QMDLConstant.STRING_EXTRA_LENTH);
			finalJsonString.append(Symbol.BRACE_CLOSE_STRING).append(Symbol.BRACE_CLOSE_STRING);
		} else {
			finalJsonString.setLength(QMDLConstant.EMPTY_ARRAY_SIZE);
			finalJsonString.append(QMDLConstant.NO_RESULT_FOUND);
		}
	}

	private String get2GSummaryFromResult(List<HBaseResult> hbaseResultList) {
		if (hbaseResultList != null) {
			String[][] values = new String[hbaseResultList.size()][];
			int count = NVConstant.ZERO_INT;
			for (HBaseResult result : hbaseResultList) {
				String json = result.getString(QMDLConstant.SUMMARY_JSON_2G);
				logger.info("Inside @method get2GSummaryFromResult {} ", json);
				getStringArrayFromJson(json, values, count);
				count++;
			}
			return calculateFinalResultFromArrayFor2G(values);
		}
		return null;
	}

	private String calculateFinalResultFromArrayFor2G(String[][] values) {

		String[] result = getFinalAggrigateArrayFromValuesFor2G(values);
		if (result != null) {
			return createJsonForSummary2G(result);
		} else {
			return QMDLConstant.EMPTY_STRING;
		}
	}

	private String[] getFinalAggrigateArrayFromValuesFor2G(String[][] values) {

		String[] result = null;
		for (String[] row : values) {
			if (result != null) {
				result = mergeSummaryData2G(result, row);
			} else {
				result = row;
			}
		}
		return result;
	}

	private String[] mergeSummaryData2G(String[] result, String[] row) {
		try {
			aggrigateRowForSummary2G(result, row);
		} catch (RestException e) {
			if (getLongValueFromStringArray(result[QMDLConstant.START_TIME_INDEX]) == null
					|| getLongValueFromStringArray(result[QMDLConstant.END_TIME_INDEX]) == null) {
				result = row;
			}
			logger.error("Exception in processing row  {}  and result {}", row, result);
		}
		return result;
	}

	private void aggrigateRowForSummary2G(String[] result, String[] row) {
		update2GKpiValuesIntoResult(result, row);
	}

	private void update2GKpiValuesIntoResult(String[] result, String[] row) {
		for (int i = QMDLConstant.KPI_2G_START_INDEX; i < QMDLConstant.KPI_2G_END_INDEX; i++) {
			addKpiValues(result, row, i);
		}
	}

	private String createJsonForSummary2G(String[] result) {
		StringBuilder json = new StringBuilder();
		json.append(Symbol.BRACKET_OPEN_STRING);
		int count = ForesightConstants.ZERO;
		Double kpiValues = null;
		boolean isCalculate = false;
		for (String value : result) {
			count++;
			if (isValidFor2GKpiValues(count)) {
				logger.debug("createJsonForSummary2G count {}   kpiValues {} value {} ", count, kpiValues, value);
				kpiValues = getKpiValues(kpiValues, isCalculate, value);
				isCalculate = addKpiValuesIntoSummary(json, kpiValues, isCalculate);
				continue;
			}
			addResultValueIntoStringBuilder(json, count, value);
		}
		json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
		json.append(Symbol.BRACKET_CLOSE_STRING);
		return json.toString();
	}

	private String get3GSummaryFromResult(List<HBaseResult> hbaseResultList) {
		if (hbaseResultList != null) {
			String[][] values = new String[hbaseResultList.size()][];
			int count = ForesightConstants.ZERO;
			for (HBaseResult result : hbaseResultList) {
				String json = result.getString(QMDLConstant.SUMMARY_JSON_3G);
				getStringArrayFromJson(json, values, count);
				count++;
			}
			return calculateFinalResultFromArrayFor3G(values);
		}
		return null;
	}

	private List<String> getSummaryColumnList() {
		List<String> columnList = new ArrayList<>();
		columnList.add(QMDLConstant.SUMMARY_JSON);
		columnList.add(QMDLConstant.SUMMARY_JSON_3G);
		columnList.add(QMDLConstant.SUMMARY_JSON_2G);
		return columnList;
	}

	private String getFinalSummaryFromResult(List<HBaseResult> hbaseResultList) {
		if (hbaseResultList != null) {
			String[][] values = new String[hbaseResultList.size()][];
			int count = ForesightConstants.ZERO;
			for (HBaseResult result : hbaseResultList) {
				String json = result.getString(QMDLConstant.SUMMARY_JSON);
				getStringArrayFromJson(json, values, count);
				count++;
			}
			return calculateFinalResultFromArray(values);
		}
		return null;
	}

	private String calculateFinalResultFromArray(String[][] values) {
		String[] result = getFinalAggrigateArrayFromValues(values);
		if (result != null) {
			return createJsonForSummary(result);
		} else {
			return QMDLConstant.EMPTY_STRING;
		}
	}

	private String calculateFinalResultFromArrayFor3G(String[][] values) {
		String[] result = getFinalAggrigateArrayFromValuesFor3G(values);
		if (result != null) {
			return createJsonForSummary3G(result);
		} else {
			return QMDLConstant.EMPTY_STRING;
		}
	}

	private String createJsonForSummary(String[] result) {
		StringBuilder json = new StringBuilder();
		json.append(Symbol.BRACKET_OPEN_STRING);
		int count = ForesightConstants.ZERO;
		Double kpiValues = null;
		boolean isCalculate = false;
		for (String value : result) {
			count++;
			if (isValidForKpiValues(count)) {
				logger.debug("Getting count {}   kpiValues {} value {} ", count, kpiValues, value);
				kpiValues = getKpiValues(kpiValues, isCalculate, value);
				isCalculate = addKpiValuesIntoSummary(json, kpiValues, isCalculate);
				continue;
			}
			addResultValueIntoStringBuilder(json, count, value);
		}
		json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
		json.append(Symbol.BRACKET_CLOSE_STRING);
		return json.toString();
	}

	private String createJsonForSummary3G(String[] result) {
		StringBuilder json = new StringBuilder();
		json.append(Symbol.BRACKET_OPEN_STRING);
		int count = ForesightConstants.ZERO;
		Double kpiValues = null;
		boolean isCalculate = false;
		for (String value : result) {
			count++;
			if (isValidFor3GKpiValues(count)) {
				logger.debug("Getting count {}   kpiValues {} value {} ", count, kpiValues, value);
				kpiValues = getKpiValues(kpiValues, isCalculate, value);
				isCalculate = addKpiValuesIntoSummary(json, kpiValues, isCalculate);
				continue;
			}
			addResultValueIntoStringBuilder(json, count, value);
		}
		json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
		json.append(Symbol.BRACKET_CLOSE_STRING);
		return json.toString();
	}

	private boolean isValidFor2GKpiValues(int count) {
		return count > QMDLConstant.KPI_2G_START_INDEX && count <= QMDLConstant.KPI_2G_END_INDEX;
	}

	private boolean isValidForKpiValues(int count) {
		return ((count > QMDLConstant.KPI_START_INDEX && count <= QMDLConstant.KPI_END_INDEX)
				|| (count > QMDLConstant.JIITER_KPI_START_INDEX && count <= QMDLConstant.MOS_COUNT_INDEX + 1)
				|| (count > QMDLConstant.CALL_CONNECTION_SETUP_SUM
						&& count <= QMDLConstant.HTTP_DOWNLOAD_TIME_COUNT + 1)
				|| (count > QMDLConstant.KPI_RATE_START_INDEX && count <= QMDLConstant.KPI_RATE_END_INDEX + 1)
				|| (count > QMDLConstant.INST_MOS_START_INDEX && count <= QMDLConstant.INST_MOS_START_INDEX + 2)
				|| (count > QMDLConstant.PDSCH_SUM_INDEX && count <= QMDLConstant.PDSCH_COUNT_INDEX + 1)
				|| (count > QMDLConstant.MAC_SUM_INDEX && count <= QMDLConstant.MAC_COUNT_INDEX + 1)
				|| (count > QMDLConstant.PUSCH_SUM_INDEX && count <= QMDLConstant.PUSCH_COUNT_INDEX + 1)
				|| (count > QMDLConstant.CQICWO_SUM_INDEX && count <= QMDLConstant.CQICWO_COUNT_INDEX + 1)
				|| (count > QMDLConstant.CQICW1_SUM_INDEX && count <= QMDLConstant.CQICW1_COUNT_INDEX + 1)
				|| (count > QMDLConstant.HANDOVER_INTERRUPTION_AVG_TIME_INDEX_SUM
						&& count <= QMDLConstant.HANDOVER_INTERRUPTION_AVG_TIME_INDEX_COUNT + 1)
				|| (count > QMDLConstant.LATENCY_BUFFER_SIZE_32BYTES_SUM_INDEX
						&& count <= QMDLConstant.LATENCY_BUFFER_SIZE_32BYTES_COUNT_INDEX + 1)
				|| (count > QMDLConstant.LATENCY_BUFFER_SIZE_1000BYTES_SUM_INDEX
						&& count <= QMDLConstant.LATENCY_BUFFER_SIZE_1000BYTES_COUNT_INDEX + 1)
				|| (count > QMDLConstant.LATENCY_BUFFER_SIZE_1500BYTES_SUM_INDEX
						&& count <= QMDLConstant.LATENCY_BUFFER_SIZE_1500BYTES_COUNT_INDEX + 1)
				|| (count > QMDLConstant.DL_PRB_SUM_INDEX && count <= QMDLConstant.DL_PRB_COUNT_INDEX + 1)
				|| (count > QMDLConstant.IMS_REGISTRATION_SETUPTIME_SUM_INDEX
						&& count <= QMDLConstant.IMS_REGISTRATION_SETUPTIME_COUNT_INDEX + 1)
				|| (count > QMDLConstant.MAC_DL_SUM_INDEX && count <= QMDLConstant.MAC_DL_COUNT_INDEX + 1)
				|| (count > QMDLConstant.JITTER_NEW_SUM_INDEX && count <= QMDLConstant.JITTER_NEW_COUNT_INDEX + 1)
				|| (count > QMDLConstant.PACKET_LOSS_SUM_INDEX && count <= QMDLConstant.PACKET_LOSS_COUNT_INDEX + 1)
				|| (count > QMDLConstant.UL_PRB_SUM_INDEX && count <= QMDLConstant.UL_PRB_COUNT_INDEX + 1)
				|| (count > QMDLConstant.DL_PRB_UTILIZATION_SUM_INDEX
						&& count <= QMDLConstant.DL_PRB_UTILIZATION_COUNT_INDEX + 1)
				|| (count > QMDLConstant.UL_PRB_UTILIZATION_SUM_INDEX
						&& count <= QMDLConstant.UL_PRB_UTILIZATION_COUNT_INDEX + 1)
				|| (count > QMDLConstant.PMOS_SUM_INDEX && count <= QMDLConstant.PMOS_COUNT_INDEX + 1)
				|| (count > QMDLConstant.HITQCI_START_INDEX && count <= QMDLConstant.HITQCI_END_INDEX)
				|| (count > QMDLConstant.PDCP_THROUGHPUT_SUM_INDEX && count <= QMDLConstant.PDCP_THROUGHPUT_COUNT_INDEX + 1)
				|| (count > QMDLConstant.RLC_THROUGHPUT_SUM_INDEX && count <= QMDLConstant.RLC_THROUGHPUT_COUNT_INDEX + 1)
				|| (count > QMDLConstant.CALL_SETUP_SUCCESS_TIME_SUM_INDEX && count <= QMDLConstant.CALL_SETUP_SUCCESS_TIME_COUNT_INDEX + 1));


	}

	private boolean isValidFor3GKpiValues(int count) {
		return count > QMDLConstant.KPI_3G_START_INDEX && count <= QMDLConstant.KPI_3G_END_INDEX;
	}

	private void addResultValueIntoStringBuilder(StringBuilder json, int count, String value) {
		if (value.contains(Symbol.UNDERSCORE_STRING) && count != QMDLConstant.ADDRESS_INDEX) {
			value = getUniqueValuesFromString(value.split(Symbol.UNDERSCORE_STRING));
		} else if (value.contains(QMDLConstant.DELIMETER_HASH)) {
			value = getUniqueValuesFromStringWithHash(value.split(QMDLConstant.DELIMETER_HASH));
		}
		NVLayer3Utils.addValueToStringBuilder(json, value);
	}

	private String getUniqueValuesFromStringWithHash(String[] split) {
		Set<String> setValues = new HashSet<>(Arrays.asList(split));
		return NVLayer3Utils.getStringHashSeperatedFromSetValues(setValues);
	}

	private String getUniqueValuesFromString(String[] split) {
		Set<String> setValues = new HashSet<>(Arrays.asList(split));
		return NVLayer3Utils.getStringFromSetValues(setValues);
	}

	private boolean addKpiValuesIntoSummary(StringBuilder json, Double kpiValues, boolean isCalculate) {
		isCalculate = !isCalculate;
		if (!isCalculate) {
			NVLayer3Utils.addValueToStringBuilder(json, kpiValues);
		}
		return isCalculate;
	}

	private Double getKpiValues(Double kpiValues, boolean isCalculate, String value) {
		if (!StringUtils.isEmpty(value)) {
			Double intermediateValue = Double.valueOf(value);
			if (isCalculate && intermediateValue != 0) {
				try {
					kpiValues = roundOffDouble(QMDLConstant.DOUBLEVALUE_ROUND, kpiValues / intermediateValue);
				} catch (ParseException e) {
					logger.error("Error in rounding off kpi value {}", Utils.getStackTrace(e));
				}
			} else {
				kpiValues = intermediateValue;
			}
			return kpiValues;
		}
		return null;
	}

	private String[] getFinalAggrigateArrayFromValues(String[][] values) {
		String[] result = null;
		for (String[] row : values) {
			if (result != null) {
				result = mergeSummaryData(result, row);
			} else {
				result = row;
			}
		}
		return result;
	}

	private String[] getFinalAggrigateArrayFromValuesFor3G(String[][] values) {
		String[] result = null;
		for (String[] row : values) {
			if (result != null) {
				result = mergeSummaryData3G(result, row);
			} else {
				result = row;
			}
		}
		return result;
	}

	private String[] mergeSummaryData3G(String[] result, String[] row) {
		try {
			aggrigateRowForSummary3G(result, row);
		} catch (RestException e) {
			if (getLongValueFromStringArray(result[QMDLConstant.START_TIME_INDEX]) == null
					|| getLongValueFromStringArray(result[QMDLConstant.END_TIME_INDEX]) == null) {
				result = row;
			}
			logger.error("Error in processing row  {}  and result {}", row, result);
		}
		return result;
	}

	private String[] mergeSummaryData(String[] result, String[] row) {
		try {
			aggrigateRowForSummary(result, row);
		} catch (RestException e) {
			if (getLongValueFromStringArray(result[QMDLConstant.START_TIME_INDEX]) == null
					|| getLongValueFromStringArray(result[QMDLConstant.END_TIME_INDEX]) == null) {
				result = row;
			}
			logger.error("Error in processing row  {}  and result {}", row, result);
		}
		return result;
	}

	private void aggrigateRowForSummary(String[] result, String[] row) {
		updateStartEndTime(result, row);
		updateKpiValuesIntoResult(result, row);
		updateEventValuesIntoResult(result, row);
		updateDeviceIntoIntoResult(result, row);
		updateDriveInfoIntoResult(result, row);
		addGeographyFromArray(result, row);
		updateDLUlEarfcnValue(result, row);
		updateStationaryReportValues(result, row);
		updateBRTIReportValues(result, row);
		updateTauEventValue(result, row);
		updateMosKpiValue(result, row);
		updateHttpEventValuesIntoResult(result, row);
		updateCSFBEventValuesIntoResult(result, row);
		updateSuccesRateKpiIntoResult(result, row);
		updatePdschThroughputvalue(result, row);
		updatePuschThroughputvalue(result, row);
		updateAttachAccept(result, row);
		updateAttachRequest(result, row);
		updateDetachRequest(result, row);
		updateReselectionSuccess(result, row);
		updateHandoverInterruption(result, row);
		updatePingLatencyBuffersize32Value(result, row);
		updatePingLatencyBuffersize1000Value(result, row);
		updatePingLatencyBuffersize1500Value(result, row);
		updateAttachCompleteValue(result, row);
		updateDlUlModulationandPrbValue(result, row);
		updateRRCReestablishmentsEventValue(result, row);
		updateImsRegistrationSetupTimeValue(result, row);
		updateVoltePagingEventValue(result, row);
		updateMoMtCallValuesIntoResult(result, row);
		updatePdschMinMaxValue(result, row);
		updateMacThroughputValue(result, row);
		updateJitterAndPacket(result, row);
		updateULPRBKPIValues(result, row);
		updateDLULPRBUtilizationKPIValues(result, row);
		updatePolqaMosValues(result, row);
		updatePuschMinMaxValue(result, row);
		updateHandoverInterruptionQciValue(result, row);
		updateNewCallEventValuesIntoResult(result, row);
		updatePDCPThroughputValue(result, row);
		updateRLCThroughputValue(result, row);
		updateMsg1AndMsg3Value(result, row);
		updateRTPPacketLossAndPacketCountValue(result, row);
		updateCallSetupCountValue(result, row);		
		updateCallSetupSuccessTimeValue(result, row);		
	}
		


	private void updateCallSetupSuccessTimeValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.CALL_SETUP_SUCCESS_TIME_SUM_INDEX
				&& result.length > QMDLConstant.CALL_SETUP_SUCCESS_TIME_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.CALL_SETUP_SUCCESS_TIME_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.CALL_SETUP_SUCCESS_TIME_COUNT_INDEX);
		}		
	}

	private void updateCallSetupCountValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.CALL_SETUP_INDEX
				&& result.length > QMDLConstant.CALL_SETUP_INDEX) {
			addEventsValues(result, row, QMDLConstant.CALL_SETUP_INDEX);
			addEventsValues(result, row, QMDLConstant.VOLTE_MT_CALL_SETUP_INDEX);
			addEventsValues(result, row, QMDLConstant.VOLTE_MO_CALL_SETUP_INDEX);
		}
		
	}

	private void updateRTPPacketLossAndPacketCountValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.NUMBER_OF_RTP_PACKETS_LOST_INDEX
				&& result.length > QMDLConstant.NUMBER_OF_RTP_PACKETS_LOST_INDEX) {
			addKpiValues(result, row, QMDLConstant.NUMBER_OF_RTP_PACKETS_LOST_INDEX);
			addKpiValues(result, row, QMDLConstant.TOTAL_PACKET_COUNT_INDEX);
		}
	}

	private void updateMsg1AndMsg3Value(String[] result, String[] row) {
		if (row.length > QMDLConstant.MSG3_INDEX
				&& result.length > QMDLConstant.MSG3_INDEX) {
			addEventsValues(result, row, QMDLConstant.MSG3_INDEX);
			addEventsValues(result, row, QMDLConstant.MSG1_INDEX);
		}
	}

	private void updateRLCThroughputValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.RLC_THROUGHPUT_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.RLC_THROUGHPUT_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.RLC_THROUGHPUT_COUNT_INDEX);
		}
	}

	private void updatePDCPThroughputValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.PDCP_THROUGHPUT_SUM_INDEX && 
			result.length > QMDLConstant.PDCP_THROUGHPUT_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.PDCP_THROUGHPUT_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.PDCP_THROUGHPUT_COUNT_INDEX);
		}
	}
	
	private void updateNewCallEventValuesIntoResult(String[] result, String[] row) {
		if (row.length > QMDLConstant.NEW_CALL_EVENT_START_INDEX
				&& result.length > QMDLConstant.NEW_CALL_EVENT_START_INDEX) {

			for (int i = QMDLConstant.NEW_CALL_EVENT_START_INDEX; i < QMDLConstant.NEW_CALL_EVENT_END_INDEX; i++) {
				addEventsValues(result, row, i);
			}
		}
	}

	private void updateHandoverInterruptionQciValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.HITQCI_START_INDEX && result.length > QMDLConstant.HITQCI_START_INDEX) {

			for (int i = QMDLConstant.HITQCI_START_INDEX; i < QMDLConstant.HITQCI_END_INDEX; i++) {
				addKpiValues(result, row, i);
			}
		}
	}

	public void updatePuschMinMaxValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.PUSCH_MIN_INDEX && result.length > QMDLConstant.PUSCH_MIN_INDEX) {
			result[QMDLConstant.PUSCH_MIN_INDEX] = getMinValueFromArray(result, QMDLConstant.PUSCH_MIN_INDEX, row);
		}
		if (row.length > QMDLConstant.PUSCH_MAX_INDEX && result.length > QMDLConstant.PUSCH_MAX_INDEX) {
			result[QMDLConstant.PUSCH_MAX_INDEX] = getMaxValueFromArray(result, QMDLConstant.PUSCH_MAX_INDEX, row);
		}
	}

	private void updatePolqaMosValues(String[] result, String[] row) {
		if (row.length > QMDLConstant.PMOS_SUM_INDEX && result.length > QMDLConstant.PMOS_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.PMOS_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.PMOS_COUNT_INDEX);
		}

	}

	private void updateDLULPRBUtilizationKPIValues(String[] result, String[] row) {
		if (row.length > QMDLConstant.DL_PRB_UTILIZATION_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.DL_PRB_UTILIZATION_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.DL_PRB_UTILIZATION_COUNT_INDEX);
		}
		if (row.length > QMDLConstant.UL_PRB_UTILIZATION_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.UL_PRB_UTILIZATION_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.UL_PRB_UTILIZATION_COUNT_INDEX);
		}

	}

	private void updateULPRBKPIValues(String[] result, String[] row) {

		if (row.length > QMDLConstant.UL_PRB_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.UL_PRB_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.UL_PRB_COUNT_INDEX);
		}
	}

	private void updatePuschThroughputvalue(String[] result, String[] row) {
		if (row.length > QMDLConstant.PUSCH_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.PUSCH_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.PUSCH_COUNT_INDEX);
		}

	}

	private void updateJitterAndPacket(String[] result, String[] row) {
		if (row.length > QMDLConstant.JITTER_NEW_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.JITTER_NEW_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.JITTER_NEW_COUNT_INDEX);
		}
		if (row.length > QMDLConstant.PACKET_LOSS_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.PACKET_LOSS_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.PACKET_LOSS_COUNT_INDEX);
		}
	}

	private void updateMacThroughputValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.MAC_DL_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.MAC_DL_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.MAC_DL_COUNT_INDEX);
		}
	}

	private void updatePdschThroughputvalue(String[] result, String[] row) {
		if (row.length > QMDLConstant.PDSCH_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.PDSCH_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.PDSCH_COUNT_INDEX);
		}
	}

	private void updateMoMtCallValuesIntoResult(String[] result, String[] row) {
		if (row.length > QMDLConstant.VOLTE_MT_CALL_ATTEMPT_INDEX) {
			for (int i = QMDLConstant.VOLTE_MT_CALL_ATTEMPT_INDEX; i <= QMDLConstant.VOLTE_MO_CALL_SUCCESS_INDEX; i++) {
				addEventsValues(result, row, i);
			}
		}
	}

	public void updatePdschMinMaxValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.PDSCH_MIN_INDEX) {
			result[QMDLConstant.PDSCH_MIN_INDEX] = getMinValueFromArray(result, QMDLConstant.PDSCH_MIN_INDEX, row);
		}
		if (row.length > QMDLConstant.PDSCH_MAX_INDEX) {
			result[QMDLConstant.PDSCH_MAX_INDEX] = getMaxValueFromArray(result, QMDLConstant.PDSCH_MAX_INDEX, row);
		}
	}

	private void updateVoltePagingEventValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.VOLTE_PAGING_ATTEMPT_INDEX) {
			addEventsValues(result, row, QMDLConstant.VOLTE_PAGING_ATTEMPT_INDEX);
			addEventsValues(result, row, QMDLConstant.VOLTE_PAGING_SUCCESS_INDEX);
		}
	}

	private void updateRRCReestablishmentsEventValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.RRC_CONNECTION_REESTABLISHMENT_REQUEST_INDEX
				&& result.length > QMDLConstant.RRC_CONNECTION_REESTABLISHMENT_REQUEST_INDEX) {
			addEventsValues(result, row, QMDLConstant.RRC_CONNECTION_REESTABLISHMENT_REQUEST_INDEX);
			addEventsValues(result, row, QMDLConstant.RRC_CONNECTION_REESTABLISHMENT_REJECT_INDEX);
			addEventsValues(result, row, QMDLConstant.RRC_CONNECTION_REESTABLISHMENT_COMPLETE_INDEX);
		}
	}

	private void updateImsRegistrationSetupTimeValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.IMS_REGISTRATION_SETUPTIME_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.IMS_REGISTRATION_SETUPTIME_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.IMS_REGISTRATION_SETUPTIME_COUNT_INDEX);
		}
	}

	private void updateDlUlModulationandPrbValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.DL_PRB_SUM_INDEX) {
			addKpiValues(result, row, QMDLConstant.DL_PRB_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.DL_PRB_COUNT_INDEX);
		}
	}

	public void updateAttachCompleteValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.ATTACH_COMPLETE_VALUE_INDEX) {
			addEventsValues(result, row, QMDLConstant.ATTACH_COMPLETE_VALUE_INDEX);
		}
	}

	private void updatePingLatencyBuffersize1500Value(String[] result, String[] row) {
		if (row.length > QMDLConstant.MIN_LATENCY_BUFFER_SIZE_1500BYTES_INDEX) {
			result[QMDLConstant.MIN_LATENCY_BUFFER_SIZE_1500BYTES_INDEX] = getMinValueFromArray(result,
					QMDLConstant.MIN_LATENCY_BUFFER_SIZE_1500BYTES_INDEX, row);
			result[QMDLConstant.MAX_LATENCY_BUFFER_SIZE_1500BYTES_INDEX] = getMaxValueFromArray(result,
					QMDLConstant.MAX_LATENCY_BUFFER_SIZE_1500BYTES_INDEX, row);
			addKpiValues(result, row, QMDLConstant.LATENCY_BUFFER_SIZE_1500BYTES_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.LATENCY_BUFFER_SIZE_1500BYTES_COUNT_INDEX);
		}
	}

	private void updatePingLatencyBuffersize1000Value(String[] result, String[] row) {
		if (row.length > QMDLConstant.MIN_LATENCY_BUFFER_SIZE_1000BYTES_INDEX) {
			result[QMDLConstant.MIN_LATENCY_BUFFER_SIZE_1000BYTES_INDEX] = getMinValueFromArray(result,
					QMDLConstant.MIN_LATENCY_BUFFER_SIZE_1000BYTES_INDEX, row);
			result[QMDLConstant.MAX_LATENCY_BUFFER_SIZE_1000BYTES_INDEX] = getMaxValueFromArray(result,
					QMDLConstant.MAX_LATENCY_BUFFER_SIZE_1000BYTES_INDEX, row);
			addKpiValues(result, row, QMDLConstant.LATENCY_BUFFER_SIZE_1000BYTES_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.LATENCY_BUFFER_SIZE_1000BYTES_COUNT_INDEX);
		}
	}

	private void updatePingLatencyBuffersize32Value(String[] result, String[] row) {
		if (row.length > QMDLConstant.MIN_LATENCY_BUFFER_SIZE_32BYTES_INDEX) {
			result[QMDLConstant.MIN_LATENCY_BUFFER_SIZE_32BYTES_INDEX] = getMinValueFromArray(result,
					QMDLConstant.MIN_LATENCY_BUFFER_SIZE_32BYTES_INDEX, row);
			result[QMDLConstant.MAX_LATENCY_BUFFER_SIZE_32BYTES_INDEX] = getMaxValueFromArray(result,
					QMDLConstant.MAX_LATENCY_BUFFER_SIZE_32BYTES_INDEX, row);
			addKpiValues(result, row, QMDLConstant.LATENCY_BUFFER_SIZE_32BYTES_SUM_INDEX);
			addKpiValues(result, row, QMDLConstant.LATENCY_BUFFER_SIZE_32BYTES_COUNT_INDEX);
		}
	}

	private void updateHandoverInterruption(String[] result, String[] row) {
		if (row.length > QMDLConstant.HANDOVER_INTERRUPTION_MIN_TIME_INDEX) {
			result[QMDLConstant.HANDOVER_INTERRUPTION_MIN_TIME_INDEX] = getMinValueFromArray(result,
					QMDLConstant.HANDOVER_INTERRUPTION_MIN_TIME_INDEX, row);
			result[QMDLConstant.HANDOVER_INTERRUPTION_MAX_TIME_INDEX] = getMaxValueFromArray(result,
					QMDLConstant.HANDOVER_INTERRUPTION_MAX_TIME_INDEX, row);
			addKpiValues(result, row, QMDLConstant.HANDOVER_INTERRUPTION_AVG_TIME_INDEX_SUM);
			addKpiValues(result, row, QMDLConstant.HANDOVER_INTERRUPTION_AVG_TIME_INDEX_COUNT);
		}
	}

	private void updateReselectionSuccess(String[] result, String[] row) {
		addEventsValues(result, row, QMDLConstant.RESELECTION_SUCCESS_INDEX_VALUE);
	}

	private void updateDetachRequest(String[] result, String[] row) {
		addEventsValues(result, row, QMDLConstant.DETACH_REQUEST_INDEX_VALUE);
	}

	private void updateAttachRequest(String[] result, String[] row) {
		addEventsValues(result, row, QMDLConstant.ATTACH_REQUEST_INDEX_VALUE);
	}

	private void updateAttachAccept(String[] result, String[] row) {
		addEventsValues(result, row, QMDLConstant.ATTACH_ACCEPT_INDEX_VALUE);
	}

	private void updateCSFBEventValuesIntoResult(String[] result, String[] row) {
		for (int i = QMDLConstant.CSFB_EVENT_START_INDEX; i < QMDLConstant.CSFB_EVENT_END_INDEX; i++) {
			addEventsValues(result, row, i);
		}
	}

	private void updateHttpEventValuesIntoResult(String[] result, String[] row) {
		addEventsValues(result, row, QMDLConstant.HTTP_DROP);
	}

	private void updateMosKpiValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.INST_MOS_START_INDEX) {
			for (int i = QMDLConstant.INST_MOS_START_INDEX; i <= QMDLConstant.INST_MOS_END_INDEX + 1; i++) {
				addKpiValues(result, row, i);
			}
		}
	}

	private void updateSuccesRateKpiIntoResult(String[] result, String[] row) {
		for (int i = QMDLConstant.KPI_RATE_START_INDEX; i < QMDLConstant.KPI_RATE_END_INDEX; i++) {
			addKpiValues(result, row, i);
		}
	}

	private void aggrigateRowForSummary3G(String[] result, String[] row) {
		update3GKpiValuesIntoResult(result, row);
	}

	private void update3GKpiValuesIntoResult(String[] result, String[] row) {
		for (int i = QMDLConstant.KPI_3G_START_INDEX; i < QMDLConstant.KPI_3G_END_INDEX; i++) {
			addKpiValues(result, row, i);
		}

	}

	private void updateBRTIReportValues(String[] result, String[] row) {
		if (row.length > QMDLConstant.PACKET_LOSS_SUM) {
			addKpiValues(result, row, QMDLConstant.PACKET_LOSS_SUM);
			addKpiValues(result, row, QMDLConstant.PACKET_LOSS_COUNT);
			addKpiValues(result, row, QMDLConstant.HTTP_DOWNLOAD_TIME_SUM);
			addKpiValues(result, row, QMDLConstant.HTTP_DOWNLOAD_TIME_COUNT);
			addEventsValues(result, row, QMDLConstant.HTTP_ATTEMPT);
			addEventsValues(result, row, QMDLConstant.HTTP_SUCCESS);
		}
		if (row.length > QMDLConstant.HTTP_FAILURE) {
			addEventsValues(result, row, QMDLConstant.HTTP_FAILURE);
			addEventsValues(result, row, QMDLConstant.SMS_FAILURE_INDEX);
			addKpiValues(result, row, QMDLConstant.TOTAL_DISTANCE);

		}

	}

	public void updateTauEventValue(String[] result, String[] row) {
		if (row.length > QMDLConstant.TAU_ATTEMPT_INDEX) {
			addEventsValues(result, row, QMDLConstant.TAU_ATTEMPT_INDEX);
			addEventsValues(result, row, QMDLConstant.TAU_SUCCESS_INDEX);
			addEventsValues(result, row, QMDLConstant.TAU_FAILURE_INDEX);
		}
	}

	private void updateDLUlEarfcnValue(String[] result, String[] row) {
		addDriveInfomation(result, row, QMDLConstant.EARFCN_SUMMARY_INDEX);
	}

	private void updateStationaryReportValues(String[] result, String[] row) {
		aggrigateMinMaxValue(result, row);
		aggrigateKpiValue(result, row);
		addEventDetailIntoResult(result, row);
		addCallConnectionSetupTime(result, row);
	}

	private void addCallConnectionSetupTime(String[] result, String[] row) {
		if (row.length > QMDLConstant.CALL_CONNECTION_SETUP_COUNT) {
			addKpiValues(result, row, QMDLConstant.CALL_CONNECTION_SETUP_SUM);
			addKpiValues(result, row, QMDLConstant.CALL_CONNECTION_SETUP_COUNT);
		}
		if (row.length > QMDLConstant.CALL_CONNECTION_SETUP_ONNET_COUNT) {
			addKpiValues(result, row, QMDLConstant.CALL_CONNECTION_SETUP_ONNET_SUM);
			addKpiValues(result, row, QMDLConstant.CALL_CONNECTION_SETUP_ONNET_COUNT);
			addKpiValues(result, row, QMDLConstant.CALL_CONNECTION_SETUP_OFFNET_SUM);
			addKpiValues(result, row, QMDLConstant.CALL_CONNECTION_SETUP_OFFNET_COUNT);
			addKpiValues(result, row, QMDLConstant.FINAL_MOS_G711_ONNET_SUM);
			addKpiValues(result, row, QMDLConstant.FINAL_MOS_G711_ONNET_COUNT);
			addKpiValues(result, row, QMDLConstant.FINAL_MOS_G711_OFFNET_SUM);
			addKpiValues(result, row, QMDLConstant.FINAL_MOS_G711_OFFNET_COUNT);
			addKpiValues(result, row, QMDLConstant.FINAL_MOS_ILBC_ONNET_SUM);
			addKpiValues(result, row, QMDLConstant.FINAL_MOS_ILBC_ONNET_COUNT);
			addKpiValues(result, row, QMDLConstant.FINAL_MOS_ILBC_OFFNET_SUM);
			addKpiValues(result, row, QMDLConstant.FINAL_MOS_ILBC_OFFNET_COUNT);
		}

	}

	private void addEventDetailIntoResult(String[] result, String[] row) {
		for (int index = QMDLConstant.SMS_ATTEMPT_SUMMARY_INDEX; index <= QMDLConstant.HANDOVER_INTRA_SUMMARY_INDEX; index++) {
			addEventsValues(result, row, index);
		}
	}

	private void aggrigateKpiValue(String[] result, String[] row) {
		for (int index = QMDLConstant.JITTER_SUM_INDEX; index <= QMDLConstant.MOS_COUNT_INDEX; index++) {
			addKpiValues(result, row, index);
		}
	}

	private void aggrigateMinMaxValue(String[] result, String[] row) {
		for (int index = QMDLConstant.MIN_LATENCY_INDEX; index <= QMDLConstant.MIN_RSSI_INDEX; index++) {
			result[index] = getMinValueFromArray(result, index, row);
		}
		for (int index = QMDLConstant.MAX_LATENCY_INDEX; index <= QMDLConstant.MAX_RSSI_INDEX; index++) {
			result[index] = getMaxValueFromArray(result, index, row);
		}

	}

	private String getMinValueFromArray(String[] result, int index, String[] row) {
		if (!StringUtils.isEmpty(row[index]) && !StringUtils.isEmpty(result[index])) {
			return String.valueOf(NVLayer3Utils.getMinValue(Double.valueOf(row[index]), Double.valueOf(result[index])));
		} else if (!StringUtils.isEmpty(row[index])) {
			return row[index];
		}
		return result[index];
	}

	private String getMaxValueFromArray(String[] result, int index, String[] row) {
		if (!StringUtils.isEmpty(row[index]) && !StringUtils.isEmpty(result[index])) {
			return String.valueOf(NVLayer3Utils.getMaxValue(Double.valueOf(row[index]), Double.valueOf(result[index])));
		} else if (!StringUtils.isEmpty(row[index])) {
			return row[index];
		}
		return result[index];
	}

	private void updateStartEndTime(String[] result, String[] row) {
		try {
			if (getLongValueFromStringArray(result[QMDLConstant.START_TIME_INDEX]) > getLongValueFromStringArray(
					row[QMDLConstant.START_TIME_INDEX])) {
				result[QMDLConstant.START_TIME_INDEX] = row[QMDLConstant.START_TIME_INDEX];
				result[QMDLConstant.ADDRESS_INDEX] = row[QMDLConstant.ADDRESS_INDEX];
				result[QMDLConstant.START_LATITUDE_INDEX] = row[QMDLConstant.START_LATITUDE_INDEX];
				result[QMDLConstant.START_LONGITUDE_INDEX] = row[QMDLConstant.START_LONGITUDE_INDEX];
			}
			if (getLongValueFromStringArray(result[QMDLConstant.END_TIME_INDEX]) < getLongValueFromStringArray(
					row[QMDLConstant.END_TIME_INDEX])) {
				result[QMDLConstant.END_TIME_INDEX] = row[QMDLConstant.END_TIME_INDEX];
			}
		} catch (Exception e) {
			logger.warn("Start and End time is null Record discarded {}", Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}

	}

	private void updateDriveInfoIntoResult(String[] result, String[] row) {
		for (int i = QMDLConstant.DERIVEINFO_START_INDEX; i < QMDLConstant.DERIVEINFO_END_INDEX; i++) {
			addDriveInfomation(result, row, i);
		}
	}

	private void updateDeviceIntoIntoResult(String[] result, String[] row) {
		for (int i = QMDLConstant.DEVICEINFO_START_INDEX; i < QMDLConstant.DEVICEINFO_END_INDEX; i++) {
			addDeviceInfomation(result, row, i);
		}
	}

	private void updateEventValuesIntoResult(String[] result, String[] row) {
		for (int i = QMDLConstant.EVENT_START_INDEX; i < QMDLConstant.EVENT_END_INDEX; i++) {
			if (i == QMDLConstant.ADDRESS_INDEX) {
				continue;
			}
			addEventsValues(result, row, i);
		}
	}

	private void updateKpiValuesIntoResult(String[] result, String[] row) {
		for (int i = QMDLConstant.KPI_START_INDEX; i < QMDLConstant.KPI_END_INDEX; i++) {
			addKpiValues(result, row, i);
		}
	}
	
	
	
	

	private void addGeographyFromArray(String[] result, String[] row) {
		if (result[QMDLConstant.GEOGRAPHY_NAME_INDEX]!=null &&
				row[QMDLConstant.GEOGRAPHY_NAME_INDEX]!=null &&
				!StringUtils.isEmpty(result[QMDLConstant.GEOGRAPHY_NAME_INDEX])
				&& !StringUtils.isEmpty(row[QMDLConstant.GEOGRAPHY_NAME_INDEX])) {
			if (result[QMDLConstant.GEOGRAPHY_LEVEL_INDEX].equals(row[QMDLConstant.GEOGRAPHY_LEVEL_INDEX])) {

				Set<String> geographySet = new HashSet<>(
						Arrays.asList(result[QMDLConstant.GEOGRAPHY_NAME_INDEX].split(Symbol.UNDERSCORE_STRING)));
				geographySet.add(row[QMDLConstant.GEOGRAPHY_NAME_INDEX]);
				result[QMDLConstant.GEOGRAPHY_NAME_INDEX] = NVLayer3Utils.getStringFromSetValues(geographySet);
			} else if (getIntegerValue(result[QMDLConstant.GEOGRAPHY_LEVEL_INDEX]) < getIntegerValue(
					row[QMDLConstant.GEOGRAPHY_LEVEL_INDEX])) {
				result[QMDLConstant.GEOGRAPHY_NAME_INDEX] = row[QMDLConstant.GEOGRAPHY_NAME_INDEX];
				result[QMDLConstant.GEOGRAPHY_LEVEL_INDEX] = row[QMDLConstant.GEOGRAPHY_LEVEL_INDEX];
			}
		} else if (row[QMDLConstant.GEOGRAPHY_NAME_INDEX]!=null && !StringUtils.isEmpty(row[QMDLConstant.GEOGRAPHY_NAME_INDEX])) {
			result[QMDLConstant.GEOGRAPHY_NAME_INDEX] = row[QMDLConstant.GEOGRAPHY_NAME_INDEX];
		}
	}

	private Integer getIntegerValue(String value) {
		if (!StringUtils.isEmpty(value)) {
			return Integer.parseInt(value);
		}
		return null;
	}

	private Double getDoubleValue(String value) {
		if (!StringUtils.isEmpty(value) && NumberUtils.isCreatable(value)) {
			return Utils.roundOff(Double.parseDouble(value), 0);
		}
		return null;
	}

	private void addDriveInfomation(String[] result, String[] row, int index) {
		if (index == QMDLConstant.SCRIPT_INDEX) {
			processScript(result, row, index);
		} else {
			if (!StringUtils.isEmpty(row[index]) && !StringUtils.isEmpty(result[index])) {
				result[index] = result[index] + QMDLConstant.DELIMETER + row[index];
			} else if (!StringUtils.isEmpty(row[index])) {
				result[index] = row[index];
			}
		}
	}

	private void processScript(String[] result, String[] row, int index) {
		if (!StringUtils.isEmpty(row[index]) && !StringUtils.isEmpty(result[index])) {
			result[index] = result[index] + QMDLConstant.DELIMETER_HASH + row[index];
		} else if (!StringUtils.isEmpty(row[index])) {
			result[index] = row[index];
		}
	}

	private void addDeviceInfomation(String[] result, String[] row, int index) {
		if (!StringUtils.isEmpty(row[index])) {
			result[index] = row[index];
		}
	}

	private void addEventsValues(String[] result, String[] row, int index) {
		try {
			if (result != null && result.length > index && row != null && row.length > index
					&& !StringUtils.isEmpty(result[index]) && !StringUtils.isEmpty(row[index])) {
				result[index] = String.valueOf(Integer.valueOf(result[index]) + Integer.valueOf(row[index]));
			} else if (result != null && result.length >= index && row != null && row.length > index
					&& !StringUtils.isEmpty(row[index])) {
				result[index] = row[index];
			}
		} catch (Exception e) {
			logger.error("Exception in addEventsValues {}", Utils.getStackTrace(e));
		}
	}

	private void addKpiValues(String[] result, String[] row, int index) {
		try {
			if (result != null && result.length >= index && !StringUtils.isEmpty(result[index])
					&& !StringUtils.isEmpty(row[index])) {
				result[index] = String.valueOf(Double.valueOf(result[index]) + Double.valueOf(row[index]));
			} else if (result != null && result.length >= index && !StringUtils.isEmpty(row[index])) {
				result[index] = row[index];
			}
		} catch (Exception e) {
			logger.error("Exception in addKpiValues {}", Utils.getStackTrace(e));
		}
	}

	private Long getLongValueFromStringArray(String valueString) {
		if (!StringUtils.isEmpty(valueString)) {
			return Long.valueOf(valueString);
		}
		return null;
	}

	private void getStringArrayFromJson(String json, String[][] values, int count) {
		try {
			if (json != null) {
				json = (json.replace(Symbol.BRACKET_OPEN_STRING, Symbol.EMPTY_STRING))
						.replace(Symbol.BRACKET_CLOSE_STRING, Symbol.EMPTY_STRING);
				String[] innerRow = json.split(Symbol.COMMA_STRING, -1);
				values[count] = innerRow;
			}
		} catch (Exception e) {
			logger.error("Exception in method getStringArrayFromJson {} ", e.getMessage());
		}
	}

	@Override
	public List<Get> getListForSummary(Integer workorderId, List<String> recipeId, List<String> operatorList,
			List<String> columnList) {
		List<Get> getList = new ArrayList<>();
		Set<String> operatorSet = new HashSet<>(operatorList);
		Set<String> recipeIdSet = new HashSet<>(recipeId);

		for (String receipeId : recipeIdSet) {
			for (String operator : operatorSet) {
				logger.info("row key to get data from Layer3Drive:  {}",getRowKeyForLayer3(workorderId, operator, receipeId));
				Get get = new Get(Bytes.toBytes(getRowKeyForLayer3(workorderId, operator, receipeId)));
				addColumnsToGet(columnList, get);
				getList.add(get);
			}
		}
		return getList;
	}

	private void addColumnsToGet(List<String> columnList, Get get) {
		for (String column : columnList) {
			get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), column.getBytes());
		}
	}

	@Override
	public String getDriveDetailReceipeWise(Integer workorderId, List<String> recipeId, List<String> operatorList) {
		logger.info("Getting wo ID {}  recipeId {}  operatorList {}", workorderId, recipeId, operatorList);

		List<String> columnList = getColumnListForDriveDetail();
		List<Get> getList = getListForSummary(workorderId, recipeId, operatorList, columnList);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_DATA_TABLE);
		try {
			List<HBaseResult> hbaseResults = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			String json = getFinalJsonFromListResult(hbaseResults);
			return getFinalJson(json);
		} catch (IOException e) {
			logger.error("IOException occured inside method getDriveDetailReceipeWise , Result {}  ", e.getMessage());
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("Error in Getting getDriveDetailReceipeWise Result {}  ", e.getMessage());
			return QMDLConstant.INVALID_ARGUMENT;
		}
	}

	private String getFinalJsonFromListResult(List<HBaseResult> hbaseResults) {
		StringBuilder finalString = new StringBuilder();
		Long oldtime = null;
		for (HBaseResult result : hbaseResults) {
			if (result != null) {
				if (finalString.length() > QMDLConstant.EMPTY_STRING_BUILDER_LENGTH) {
					finalString = mergerRecordForDriveDetail(finalString, oldtime, result);
					oldtime = updateLatestTimeStamp(oldtime, result);
				} else {
					oldtime = processFirstRecord(finalString, oldtime, result);
				}
			}
		}
		return finalString.toString();
	}

	private Long updateLatestTimeStamp(Long oldtime, HBaseResult result) {
		if (result.getString(QMDLConstant.TIMESTAMP) != null) {
			Long time = Long.valueOf(result.getString(QMDLConstant.TIMESTAMP));
			if (oldtime < time) {
				oldtime = time;
			}
		}
		return oldtime;
	}

	private Long processFirstRecord(StringBuilder finalString, Long oldtime, HBaseResult result) {
		if (result.getString(QMDLConstant.JSONSTRING) != null) {
			finalString.append(result.getString(QMDLConstant.JSONSTRING));
		}
		if (result.getString(QMDLConstant.TIMESTAMP) != null) {
			oldtime = Long.valueOf(result.getString(QMDLConstant.TIMESTAMP));
		}
		return oldtime;
	}

	private StringBuilder mergerRecordForDriveDetail(StringBuilder finalString, Long oldtime, HBaseResult result) {
		if (result.getString(QMDLConstant.TIMESTAMP) != null) {
			Long time = Long.valueOf(result.getString(QMDLConstant.TIMESTAMP));
			if (oldtime < time) {
				finalString = getTwoStringMergeForDrive(finalString.toString(),
						result.getString(QMDLConstant.JSONSTRING));
			} else {
				finalString = getTwoStringMergeForDrive(result.getString(QMDLConstant.JSONSTRING),
						finalString.toString());
			}
		} else {
			if (result.getString(QMDLConstant.JSONSTRING) != null) {
				finalString = getTwoStringMergeForDrive(finalString.toString(),
						result.getString(QMDLConstant.JSONSTRING));
			}
		}
		return finalString;
	}

	private StringBuilder getTwoStringMergeForDrive(String firstString, String ndString) {
		StringBuilder finalResponse = new StringBuilder();
		if (firstString != null && ndString != null) {
			finalResponse.append(firstString);
			finalResponse.setLength(finalResponse.length() - QMDLConstant.STRING_EXTRA_LENTH);
			finalResponse.append(Symbol.COMMA);
			ndString = ndString.substring(QMDLConstant.STRING_EXTRA_LENTH, ndString.length());
			finalResponse.append(ndString);
			return finalResponse;
		} else if (firstString != null) {
			finalResponse.append(firstString);
		} else if (ndString != null) {
			finalResponse.append(ndString);
		}
		return finalResponse;
	}

	@Override
	public List<String> getColumnListForDriveDetail() {
		List<String> columnList = new ArrayList<>();
		columnList.add(QMDLConstant.JSONSTRING);
		columnList.add(QMDLConstant.TIMESTAMP);
		return columnList;
	}

	@Override
	public String getKpiStatsRecipeData(Integer workorderId, String kpi, List<String> recipeId,
			List<String> operatorList) {
		List<Get> getList = getListForKpiStats(workorderId, recipeId, operatorList, kpi);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_KPI_TABLE);
		try {
			List<HBaseResult> hbaseResults = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			String json = getFinalKpiStatsFromResult(hbaseResults, kpi);
			return getJsonFromDetailMsg(json);
		} catch (IOException e) {
			logger.error("Error in Getting getKpiStatsRecipeData Result -- {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("Error in Getting getKpiStatsRecipeData Result {}  ", Utils.getStackTrace(e));
			return QMDLConstant.INVALID_ARGUMENT;
		}
	}

	private String getFinalKpiStatsFromResult(List<HBaseResult> hbaseResults, String kpi) {
		List<String> kpiValueMap = NVLayer3Utils.getKpiListForValueMapping();
		if (kpiValueMap.contains(kpi)) {
			return new Gson().toJson(processKpiMapFromResult(hbaseResults));
		} else {
			return processRangeWiseKpiFromResult(hbaseResults, kpi);
		}
	}

	private String processRangeWiseKpiFromResult(List<HBaseResult> hbaseResults, String kpi) {
		boolean flag = Boolean.TRUE;
		List<Long> statsArray = new ArrayList<>();
		for (HBaseResult result : hbaseResults) {
			if (result != null && result.getString(QMDLConstant.RANGE_STATS) != null) {
				String[] finalStats = getArrayFromString(result);
				if (flag) {
					statsArray.addAll(Lists.transform(Arrays.asList(finalStats), Longs.stringConverter()));
					flag = Boolean.FALSE;
				} else {
					getmergeArrayForKpi(statsArray, finalStats);
				}
			}
		}
		if (!statsArray.isEmpty()
				&& (kpi.equals(QMDLConstant.UL_THROUGHTPUT) || kpi.equals(QMDLConstant.DL_THROUGHTPUT))) {
			statsArray = statsArray.subList(ForesightConstants.ZERO, QMDLConstant.THROUGHTPUT_RANGE);
		}
		return statsArray.toString();
	}

	private String[] getArrayFromString(HBaseResult result) {
		String resutString = result.getString(QMDLConstant.RANGE_STATS)
				.replace(Symbol.BRACKET_OPEN_STRING, Symbol.EMPTY_STRING)
				.replace(Symbol.BRACKET_CLOSE_STRING, Symbol.EMPTY_STRING)
				.replace(Symbol.SPACE_STRING, Symbol.EMPTY_STRING);
		return resutString.split(Symbol.COMMA_STRING, ForesightConstants.MINUS_ONE);
	}

	private void getmergeArrayForKpi(List<Long> statsArray, String[] finalStats) {
		int count = ForesightConstants.ZERO;
		for (String value : finalStats) {
			if (!StringUtils.isEmpty(value)) {
				statsArray.set(count, statsArray.get(count) + Long.valueOf(value.trim()));
			}
			count++;
		}
	}

	private TreeMap<String, Long> processKpiMapFromResult(List<HBaseResult> hbaseResults) {
		TreeMap<String, Long> statsMap = new TreeMap<>();
		for (HBaseResult result : hbaseResults) {
			if (result != null && result.getString(QMDLConstant.RANGE_STATS) != null) {
				Gson gson = new Gson();
				Type type = new TypeToken<TreeMap<String, Long>>() {
					private static final long serialVersionUID = 8730335931025268651L;
				}.getType();
				TreeMap<String, Long> recipeMap = gson.fromJson(result.getString(QMDLConstant.RANGE_STATS), type);
				mergeStatsMap(statsMap, recipeMap);
			}
		}
		return statsMap;
	}

	private void mergeStatsMap(TreeMap<String, Long> statsMap, TreeMap<String, Long> recipeMap) {
		if (statsMap.isEmpty()) {
			statsMap.putAll(recipeMap);
		} else {
			for (Entry<String, Long> entry : recipeMap.entrySet()) {
				if (statsMap.containsKey(entry.getKey())) {
					statsMap.put(entry.getKey(), statsMap.get(entry.getKey()) + entry.getValue());
				} else {
					statsMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	@Override
	public List<Get> getListForKpiStats(Integer workorderId, List<String> recipeId, List<String> operatorList,
			String kpi) {
		List<Get> getList = new ArrayList<>();
		Set<String> operatorset = new HashSet<>(operatorList);
		for (String receipeId : recipeId) {
			for (String operator : operatorset) {
				Get get = new Get(Bytes.toBytes(getRowKeyForLayer3(workorderId, operator, receipeId) + kpi));
				get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.RANGE_STATS.getBytes());
				getList.add(get);
			}
		}
		return getList;
	}

	@Override
	public String getAggrigationDataByLatLong(Integer workorderId, List<String> recipeId, List<String> operatorList) {

		if (recipeId != null && operatorList != null) {
			String result = getDriveDetailReceipeWise(workorderId, recipeId, operatorList);
			if (result != null && result.equals(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON)
					&& result.equals(QMDLConstant.INVALID_ARGUMENT) && !result.contains("No Result Found")) {
				return getJsonStringForLatLng(result);
			} else {
				logger.info("result not found");
				return QMDLConstant.NO_RESULT_FOUND;
			}
		} else {
			logger.info("Invalid parameter   recipeId {}  operatorList  {}  ", recipeId, operatorList);
			return QMDLConstant.INVALID_ARGUMENT;
		}

	}

	private String getJsonStringForLatLng(String result) {
		String[] allrecords = removeUnwantedStringFromResult(result);
		Map<LatLng, QMDLLogCodeWrapper> getAggrigatedMap = processJsonDataForGridAggrigation(allrecords);
		logger.info("getAggrigatedMap size:{}", getAggrigatedMap.size());
		StringBuilder builder = getJsonFromQmdlAggregateData(getAggrigatedMap);
		return builder.toString();
	}

	public String[] removeUnwantedStringFromResult(String result) {
		result = result.replace(DRIVE_START_STRING, DRIVE_NULL_STRING);
		result = result.replaceFirst(DRIVE_FIRST_STRING, DRIVE_NULL_STRING);
		result = result.replace(DRIVE_END_STRING, DRIVE_NULL_STRING);
		return result.split(DRIVE_RECORD_SEPERATOR);
	}

	public String[] removeUnwantedStringFromNeighbourResult(String result) {
		if (result.length() > 4) {
			String temp = result.substring(TWO, result.length() - TWO);

			return temp.split(DRIVE_RECORD_SEPERATOR);
		}
		return null;
	}

	public StringBuilder getJsonFromQmdlAggregateData(Map<LatLng, QMDLLogCodeWrapper> getAggrigatedMap) {
		StringBuilder builder = new StringBuilder();
		builder.append(DRIVE_START_STRING);
		for (QMDLLogCodeWrapper key : getAggrigatedMap.values()) {
			builder.append(DRIVE_OPENING_BRACKET_SEPRATOR);
			getValuesFromQmdlWrapper(builder, key);
			builder.setLength(builder.length() - 1);
			builder.append(DRIVE_CLOSING_BRACKET_COMMA_SEPRATOR);
		}
		builder.setLength(builder.length() - 1);
		builder.append(DRIVE_CLOSING_BRACKET_PARENTHESI_SEPRATOR);
		return builder;
	}

	private void getValuesFromQmdlWrapper(StringBuilder builder, QMDLLogCodeWrapper key) {
		addLatLongAndKpiIntoJson(builder, key);
		addLayer3KpiIntoJson(builder, key);
		addHandoverEventsIntoJson(builder, key);
		addCallEventsIntoJson(builder, key);
		addAddressAndCellIDBandIntoJson(builder, key);
		addInbuildingParamIntoJson(builder, key);
		addDriveInfoAndNetworkInfoIntoJson(builder, key);
		addHandoverDetailIntoJson(builder, key);
		addPacketLossIntoJson(builder, key);
	}

	public void addPacketLossIntoJson(StringBuilder builder, QMDLLogCodeWrapper key) {
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getPacketLoss()));
	}

	private void addHandoverDetailIntoJson(StringBuilder builder, QMDLLogCodeWrapper key) {
		NVLayer3Utils.addValueToStringBuilder(builder, key.getSourcePci());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getTargetPci());
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getHandoverLatencyTime()));
	}

	public void addDriveInfoAndNetworkInfoIntoJson(StringBuilder builder, QMDLLogCodeWrapper key) {
		NVLayer3Utils.addValueToStringBuilder(builder, key.getCoverage());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getTestType());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getMnc());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getMcc());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getCellid());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getSectorId());
		NVLayer3Utils.addValueToStringBuilder(builder, key.geteNodebId());
	}

	public void addInbuildingParamIntoJson(StringBuilder builder, QMDLLogCodeWrapper key) {
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getXpoint()));
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getYpoint()));
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getAvgJitter()));
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getAvgReponseTime()));
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getWifiRssi()));
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getWifiSnr()));
		NVLayer3Utils.addValueToStringBuilder(builder, key.getSsid());
	}

	public void addAddressAndCellIDBandIntoJson(StringBuilder builder, QMDLLogCodeWrapper key) {
		NVLayer3Utils.addValueToStringBuilder(builder, key.getAddress());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getBand());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getTechnologyBand());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getCellid());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getMcs());
	}

	public void addCallEventsIntoJson(StringBuilder builder, QMDLLogCodeWrapper key) {
		NVLayer3Utils.addValueToStringBuilder(builder, key.getCallInitiateCount());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getCallDropCount());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getCallFailureCount());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getCallSuccessCount());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getCellChangeCount());
	}

	public void addHandoverEventsIntoJson(StringBuilder builder, QMDLLogCodeWrapper key) {
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getOutOfSyncBler()));
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getDlThroughPut()));
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getUlThroughPut()));
		NVLayer3Utils.addValueToStringBuilder(builder, key.getLatency());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getNewHandOverIntiateCount());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getNewHandOverFailureCount());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getNewHandOverSuccessCount());
	}

	public void addLayer3KpiIntoJson(StringBuilder builder, QMDLLogCodeWrapper key) {
		NVLayer3Utils.addValueToStringBuilder(builder, key.getDlEarfcn());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getUlEarfcn());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getDl_Bandwidth());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getUl_Bandwidth());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getSpatialRank());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getIndexPMI());
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getNumRB()));
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getPuschTxPower()));
		NVLayer3Utils.addValueToStringBuilder(builder, key.getRankIndex());
		NVLayer3Utils.addValueToStringBuilder(builder,
				NVLayer3Utils.getAvgFromArrayAndReturnIntegerValue(key.getCqiCwo()));
		NVLayer3Utils.addValueToStringBuilder(builder, key.getCarrierIndex());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getTimingAdvance());
	}

	public void addLatLongAndKpiIntoJson(StringBuilder builder, QMDLLogCodeWrapper key) {
		NVLayer3Utils.addValueToStringBuilder(builder, key.getLat());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getLon());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getTimeStamp());
		NVLayer3Utils.addValueToStringBuilder(builder, key.getPci());
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getrSSIData()));
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getMeasureRSRPData()));
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getMeasureRSRQData()));
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getAvgFromArray(key.getsINRData()));
	}

	private Map<LatLng, QMDLLogCodeWrapper> processJsonDataForGridAggrigation(String[] allrecords) {
		Map<LatLng, QMDLLogCodeWrapper> getAggrigatedMap = new HashMap<>();
		for (String records : allrecords) {
			String[] singlerecord = records.split(DRIVE_COMMA_SEPRATOR, ForesightConstants.MINUS_ONE);
			LatLng myLatLng = NVLayer3Utils.getGridLatLong(getDoubleValueForDriveDetail(singlerecord[GRIDLATINDEX]),
					getDoubleValueForDriveDetail(singlerecord[GRIDLNGINDEX]),
					ConfigUtils.getInteger(NVLayer3Constants.LAYER3_GRID_SIZE));
			QMDLLogCodeWrapper qmdlLogCodeWrapper = getAggrigatedMap.containsKey(myLatLng)
					? getAggrigatedMap.get(myLatLng)
					: new QMDLLogCodeWrapper();
			setValuesIntoQmdlWrapper(singlerecord, myLatLng, qmdlLogCodeWrapper);
			getAggrigatedMap.put(myLatLng, qmdlLogCodeWrapper);
		}
		return getAggrigatedMap;
	}

	public static Long getLatestValue(Long oldValue, Long newValue) {
		if (oldValue == null) {
			return newValue;
		} else if (newValue == null) {
			return oldValue;
		}
		return oldValue;
	}

	public static Double getLatestValue(Double oldValue, Double newValue) {
		if (oldValue == null) {
			return newValue;
		} else if (newValue == null) {
			return oldValue;
		}
		return oldValue;
	}

	public static Double[] getLatestValue(Double[] oldValue, Double[] newValue) {
		if (oldValue == null) {
			return newValue;
		} else if (newValue == null) {
			return oldValue;
		}
		return newValue;
	}

	public static Integer getLatestValue(Integer oldValue, Integer newValue) {
		if (oldValue == null) {
			return newValue;
		} else if (newValue == null) {
			return oldValue;
		}
		return oldValue;
	}

	public static String getLatestValue(String oldValue, String newValue) {
		if (oldValue == null) {
			return newValue;
		} else if (newValue == null) {
			return oldValue;
		}
		return oldValue;
	}

	private void setValuesIntoQmdlWrapper(String[] singlerecord, LatLng myLatLng,
			QMDLLogCodeWrapper qmdlLogCodeWrapper) {
		int srindex = 1;
		srindex = setLatLongAndKpiIntoQmdlWrapper(singlerecord, myLatLng, qmdlLogCodeWrapper, srindex);
		srindex = setLayer3KpiIntoQmdlWrapper(singlerecord, qmdlLogCodeWrapper, srindex);
		srindex = setHandoverEventsIntoQmdlWrapper(singlerecord, qmdlLogCodeWrapper, srindex);
		srindex = setCallEventsIntoQmdlWrapper(singlerecord, qmdlLogCodeWrapper, srindex);
		srindex = setAddressAndCellIDBandIntoQmdlWrapper(singlerecord, qmdlLogCodeWrapper, srindex);
		srindex = setInbuildingParamIntoQmdlWrapper(singlerecord, qmdlLogCodeWrapper, srindex);
		srindex = setDriveInfoAndNetworkInfoIntoQmdlWrapper(singlerecord, qmdlLogCodeWrapper, srindex);
		srindex = setHandoverDetailIntoQmdlWrapper(singlerecord, qmdlLogCodeWrapper, srindex);
		setPacketLossIntoQmdlWrapper(singlerecord, qmdlLogCodeWrapper, srindex);
	}

	public void setPacketLossIntoQmdlWrapper(String[] singlerecord, QMDLLogCodeWrapper qmdlLogCodeWrapper,
			int srindex) {
		qmdlLogCodeWrapper.setPacketLoss(getLatestValue(qmdlLogCodeWrapper.getPacketLoss(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
	}

	public int setHandoverDetailIntoQmdlWrapper(String[] singlerecord, QMDLLogCodeWrapper qmdlLogCodeWrapper,
			int srindex) {
		qmdlLogCodeWrapper.setSourcePci(
				getLatestValue(qmdlLogCodeWrapper.getSourcePci(), getIntegerValue(singlerecord[++srindex])));
		qmdlLogCodeWrapper.setTargetPci(
				getLatestValue(qmdlLogCodeWrapper.getTargetPci(), getIntegerValue(singlerecord[++srindex])));
		qmdlLogCodeWrapper.setHandoverLatencyTime(getLatestValue(qmdlLogCodeWrapper.getHandoverLatencyTime(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		return srindex;
	}

	public int setDriveInfoAndNetworkInfoIntoQmdlWrapper(String[] singlerecord, QMDLLogCodeWrapper qmdlLogCodeWrapper,
			int srindex) {
		qmdlLogCodeWrapper.setCoverage(getLatestValue(qmdlLogCodeWrapper.getCoverage(), singlerecord[++srindex]));
		qmdlLogCodeWrapper.setTestType(getLatestValue(qmdlLogCodeWrapper.getTestType(), singlerecord[++srindex]));
		qmdlLogCodeWrapper
				.setMnc(getLatestValue(qmdlLogCodeWrapper.getMnc(), getIntegerValue(singlerecord[++srindex])));
		qmdlLogCodeWrapper
				.setMcc(getLatestValue(qmdlLogCodeWrapper.getMcc(), getIntegerValue(singlerecord[++srindex])));
		qmdlLogCodeWrapper
				.setCellid(getLatestValue(qmdlLogCodeWrapper.getCellid(), getIntegerValue(singlerecord[++srindex])));
		qmdlLogCodeWrapper.setSectorId(
				getLatestValue(qmdlLogCodeWrapper.getSectorId(), getIntegerValue(singlerecord[++srindex])));
		qmdlLogCodeWrapper.seteNodebId(
				getLatestValue(qmdlLogCodeWrapper.geteNodebId(), getIntegerValue(singlerecord[++srindex])));
		return srindex;
	}

	public int setInbuildingParamIntoQmdlWrapper(String[] singlerecord, QMDLLogCodeWrapper qmdlLogCodeWrapper,
			int srindex) {
		qmdlLogCodeWrapper.setXpoint(getLatestValue(qmdlLogCodeWrapper.getXpoint(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setYpoint(getLatestValue(qmdlLogCodeWrapper.getYpoint(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setAvgJitter(getLatestValue(qmdlLogCodeWrapper.getAvgJitter(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setAvgReponseTime(getLatestValue(qmdlLogCodeWrapper.getAvgReponseTime(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setWifiRssi(getLatestValue(qmdlLogCodeWrapper.getWifiRssi(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setWifiSnr(getLatestValue(qmdlLogCodeWrapper.getWifiSnr(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setSsid(getLatestValue(qmdlLogCodeWrapper.getSsid(), singlerecord[++srindex]));
		return srindex;
	}

	public int setAddressAndCellIDBandIntoQmdlWrapper(String[] singlerecord, QMDLLogCodeWrapper qmdlLogCodeWrapper,
			int srindex) {
		qmdlLogCodeWrapper.setAddress(getLatestValue(qmdlLogCodeWrapper.getAddress(), singlerecord[++srindex]));
		qmdlLogCodeWrapper.setBand(getLatestValue(qmdlLogCodeWrapper.getBand(), (singlerecord[++srindex])));
		qmdlLogCodeWrapper
				.setTechnologyBand(getLatestValue(qmdlLogCodeWrapper.getTechnologyBand(), singlerecord[++srindex]));
		qmdlLogCodeWrapper
				.setCellid(getLatestValue(qmdlLogCodeWrapper.getCellid(), getIntegerValue(singlerecord[++srindex])));
		qmdlLogCodeWrapper
				.setMcs(getLatestValue(qmdlLogCodeWrapper.getMcs(), getIntegerValue(singlerecord[++srindex])));
		return srindex;
	}

	public int setCallEventsIntoQmdlWrapper(String[] singlerecord, QMDLLogCodeWrapper qmdlLogCodeWrapper, int srindex) {
		qmdlLogCodeWrapper.setCallInitiateCount(getLatestValue(qmdlLogCodeWrapper.getCallInitiateCount(),
				addIntegerValue(qmdlLogCodeWrapper.getCallInitiateCount(), getIntegerValue(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setCallDropCount(getLatestValue(qmdlLogCodeWrapper.getCallDropCount(),
				addIntegerValue(qmdlLogCodeWrapper.getCallDropCount(), getIntegerValue(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setCallFailureCount(getLatestValue(qmdlLogCodeWrapper.getCallFailureCount(),
				addIntegerValue(qmdlLogCodeWrapper.getCallFailureCount(), getIntegerValue(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setCallSuccessCount(getLatestValue(qmdlLogCodeWrapper.getCallSuccessCount(),
				addIntegerValue(qmdlLogCodeWrapper.getCallSuccessCount(), getIntegerValue(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setCellChangeCount(getLatestValue(qmdlLogCodeWrapper.getCellChangeCount(),
				addIntegerValue(qmdlLogCodeWrapper.getCellChangeCount(), getIntegerValue(singlerecord[++srindex]))));
		return srindex;
	}

	public int setHandoverEventsIntoQmdlWrapper(String[] singlerecord, QMDLLogCodeWrapper qmdlLogCodeWrapper,
			int srindex) {
		qmdlLogCodeWrapper.setOutOfSyncBler(getLatestValue(qmdlLogCodeWrapper.getOutOfSyncBler(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setDlThroughPut(getLatestValue(qmdlLogCodeWrapper.getDlThroughPut(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setUlThroughPut(getLatestValue(qmdlLogCodeWrapper.getUlThroughPut(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setLatency(getLatestValue(qmdlLogCodeWrapper.getLatency(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setNewHandOverIntiateCount(
				getLatestValue(qmdlLogCodeWrapper.getNewHandOverIntiateInterCount(), addIntegerValue(
						qmdlLogCodeWrapper.getNewHandOverIntiateCount(), getIntegerValue(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setNewHandOverFailureCount(
				getLatestValue(qmdlLogCodeWrapper.getNewHandOverFailureCount(), addIntegerValue(
						qmdlLogCodeWrapper.getNewHandOverFailureCount(), getIntegerValue(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setNewHandOverSuccessCount(
				getLatestValue(qmdlLogCodeWrapper.getNewHandOverSuccessCount(), addIntegerValue(
						qmdlLogCodeWrapper.getNewHandOverSuccessCount(), getIntegerValue(singlerecord[++srindex]))));
		return srindex;
	}

	public int setLayer3KpiIntoQmdlWrapper(String[] singlerecord, QMDLLogCodeWrapper qmdlLogCodeWrapper, int srindex) {
		qmdlLogCodeWrapper.setDlEarfcn(
				getLatestValue(qmdlLogCodeWrapper.getDlEarfcn(), getLongValueForDriveDetail(singlerecord[++srindex])));
		qmdlLogCodeWrapper.setUlEarfcn(
				getLatestValue(qmdlLogCodeWrapper.getUlEarfcn(), getLongValueForDriveDetail(singlerecord[++srindex])));
		qmdlLogCodeWrapper
				.setDl_Bandwidth(getLatestValue(qmdlLogCodeWrapper.getDl_Bandwidth(), singlerecord[++srindex]));
		qmdlLogCodeWrapper
				.setUl_Bandwidth(getLatestValue(qmdlLogCodeWrapper.getUl_Bandwidth(), singlerecord[++srindex]));
		qmdlLogCodeWrapper.setSpatialRank(getLatestValue(qmdlLogCodeWrapper.getSpatialRank(), singlerecord[++srindex]));
		qmdlLogCodeWrapper.setIndexPMI(
				getLatestValue(qmdlLogCodeWrapper.getIndexPMI(), getIntegerValue(singlerecord[++srindex])));
		qmdlLogCodeWrapper.setNumRB(getLatestValue(qmdlLogCodeWrapper.getNumRB(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setPuschTxPower(getLatestValue(qmdlLogCodeWrapper.getPuschTxPower(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setRankIndex(getLatestValue(qmdlLogCodeWrapper.getRankIndex(), singlerecord[++srindex]));
		Double cqiCwo = getDoubleValue(singlerecord[++srindex]);
		qmdlLogCodeWrapper.setCqiCwo(
				getDoubleArrayValue(qmdlLogCodeWrapper.getCqiCwo(), cqiCwo != null ? cqiCwo.intValue() : null));
		qmdlLogCodeWrapper
				.setCarrierIndex(getLatestValue(qmdlLogCodeWrapper.getCarrierIndex(), singlerecord[++srindex]));
		qmdlLogCodeWrapper.setTimingAdvance(
				getLatestValue(qmdlLogCodeWrapper.getTimingAdvance(), getIntegerValue(singlerecord[++srindex])));
		return srindex;
	}

	public int setLatLongAndKpiIntoQmdlWrapper(String[] singlerecord, LatLng myLatLng,
			QMDLLogCodeWrapper qmdlLogCodeWrapper, int srindex) {
		qmdlLogCodeWrapper.setLat(myLatLng.getLatitude());
		qmdlLogCodeWrapper.setLon(myLatLng.getLongitude());
		qmdlLogCodeWrapper.setTimeStamp(
				getLatestValue(qmdlLogCodeWrapper.getTimeStamp(), getLongValueForDriveDetail(singlerecord[++srindex])));
		qmdlLogCodeWrapper
				.setPci(getLatestValue(qmdlLogCodeWrapper.getPci(), getIntegerValue((singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setrSSIData(getLatestValue(qmdlLogCodeWrapper.getrSSIData(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setMeasureRSRPData(getLatestValue(qmdlLogCodeWrapper.getMeasureRSRPData(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setMeasureRSRQData(getLatestValue(qmdlLogCodeWrapper.getMeasureRSRQData(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		qmdlLogCodeWrapper.setsINRData(getLatestValue(qmdlLogCodeWrapper.getsINRData(),
				NVLayer3Utils.getDoubleArrayValue(getDoubleValueForDriveDetail(singlerecord[++srindex]))));
		return srindex;
	}

	@Override
	public String processWOReportDump(Integer workorderId, List<String> recipeId, List<String> operatorList) {
		try {
			logger.info("Going to process WO Report Dump: recipeId {}  operatorList {} ", recipeId, operatorList);
			String csvHeaders = ConfigUtils.getString(NVConfigUtil.WO_REPORT_HEADERS);
			csvHeaders = csvHeaders.replaceAll(CONFIG_HEADER_SEPERATOR, CSV_HEADER_SEPERATOR);
			logger.info("Process WO Report Dump CSV Headers: {}", csvHeaders);
			logger.debug("Getting data {}", getDriveDetailReceipeWise(workorderId, recipeId, operatorList));
			String csvContent = processJsonToCsv(csvHeaders,
					getDriveDetailReceipeWise(workorderId, recipeId, operatorList));
			if (csvContent.contains(CSV_NO_DATA_IDENTIFIER) || csvContent.contains(QMDLConstant.NO_RESULT_FOUND)) {
				return WO_REPORT_NO_DATA_AVAILABLE_JSON;
			}
			String fileName = REPORT_NAME_PREFIX + workorderId + Symbol.UNDERSCORE_STRING + System.currentTimeMillis()
					+ REPORT_FILE_EXTENSION;
			String filePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH) + fileName;
			InputStream stream = IOUtils.toInputStream(csvContent, ENCODING_UTF_8);
			boolean isFileCopied = NVLayer3Utils.copyFileToLocalPath(filePath, stream);
			if (isFileCopied) {
				Map<String, String> responseMap = new HashMap<>();
				responseMap.put(FILE_NAME, fileName);
				return new Gson().toJson(responseMap);
			} else {
				return null;
			}
		} catch (IOException e) {
			logger.error("Error in executing processWOReportDump {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	private String processJsonToCsv(String csvHeaders, String json) {
		StringBuilder builder = new StringBuilder();
		builder.append(csvHeaders);
		builder.append(NEW_LINE_SEPERATOR);
		json = json.replace(REPORT_JSON_START_STRING, Symbol.EMPTY_STRING);
		json = json.replaceAll(REPORT_JSON_LINE_SEPERATOR, Symbol.LINE_SEPARATOR_STRING);
		json = json.replace(REPORT_JSON_END_STRING, Symbol.EMPTY_STRING);
		String[] csvRows = json.split(Symbol.LINE_SEPARATOR_STRING);
		// sortCsvTimesatmpWise(csvRows);
		for (String csvRow : csvRows) {
			csvRow = removeRowKeyFromDumpData(csvRow);
			String date = getDateFromDumpCSV(csvRow);
			StringBuilder sb = new StringBuilder();
			sb.append(date + CSV_COLUMN_SEPERATOR);
			sb.append(csvRow + NEW_LINE_SEPERATOR);
			builder.append(sb.toString());
		}
		return builder.toString();
	}

	private void sortCsvTimesatmpWise(String[] csvRows) {
		try {
			Arrays.sort(csvRows, new Comparator<String>() {
				public int compare(String oldRow, String newRow) {
					try {
						if (oldRow == null && newRow == null) {
							return NVConstant.ZERO_INT;
						} else if (oldRow == null && newRow != null) {
							return NVConstant.MINUS_ONE_INT;
						} else if (oldRow != null && newRow == null) {
							return NVConstant.ONE_INT;
						} else if (oldRow != null && newRow != null) {
							String oldValue = oldRow.split(Symbol.COMMA_STRING)[QMDLConstant.CSV_TIMESTAMP_INDEX];
							String newValue = newRow.split(Symbol.COMMA_STRING)[QMDLConstant.CSV_TIMESTAMP_INDEX];
							if (Utils.hasValidValue(oldValue) && Utils.hasValidValue(newValue)) {
								return Long.valueOf(oldValue).compareTo(Long.valueOf(newValue));
							} else {
								return NVConstant.ZERO_INT;
							}
						} else {
							return NVConstant.ZERO_INT;
						}
					} catch (Exception e) {
						return NVConstant.ZERO_INT;
					}
				}
			});
		} catch (Exception e) {
			logger.error("Error while sorting csv data based on timestamp: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	private static String getDateFromDumpCSV(String csv) {
		String[] dumpData = csv.split(CSV_COLUMN_SEPERATOR);
		if (dumpData != null && dumpData.length > DUMP_INDEX_TIMESTAMP) {
			String timeStamp = dumpData[DUMP_INDEX_TIMESTAMP];
			return getDateFromTimestamp(timeStamp);
		}
		return Symbol.EMPTY_STRING;
	}

	private static String removeRowKeyFromDumpData(String csv) {
		String[] dumpData = csv.split(CSV_COLUMN_SEPERATOR);
		if (dumpData != null && dumpData.length > DUMP_INDEX_ROWKEY) {
			String rowKey = dumpData[DUMP_INDEX_ROWKEY];
			return csv.replaceAll(REGEX_CSV_ITEM_REMOVE_PART_1 + rowKey + REGEX_CSV_ITEM_REMOVE_PART_2 + rowKey
					+ REGEX_CSV_ITEM_REMOVE_PART_3, Symbol.EMPTY_STRING);
		}
		return csv;
	}

	@Override
	public Map<String, RecipeMappingWrapper> getDriveRecipeDetailForMasterReport(List<Integer> workrorderIds) {
		try {
			List<WOFileDetail> woFileList = woFileDetailService.getFileDetailByWorkOrderIdList(workrorderIds);
			return getMappingDataForMaster(woFileList);
		} catch (RestException e) {
			logger.error("Getting Exception in getDriveRecipeDetailForMasterReport {}   {} ", workrorderIds,
					Utils.getStackTrace(e));
		}
		return null;
	}

	private Map<String, RecipeMappingWrapper> getMappingDataForMaster(List<WOFileDetail> woFileList) {
		Map<String, RecipeMappingWrapper> finalMap = new HashMap<>();
		logger.info("woFileList {}", woFileList.size());
		for (WOFileDetail woFile : woFileList) {
			try {
				if (woFile.getWoRecipeMapping().getRecipe().getCategory() != null) {
					if (finalMap.containsKey(woFile.getWoRecipeMapping().getRecipe().getCategory())) {
						RecipeMappingWrapper mappingWrapper = finalMap
								.get(woFile.getWoRecipeMapping().getRecipe().getCategory());
						List<String> recipeList = mappingWrapper.getRecpiList();
						if (!recipeList.contains(woFile.getWoRecipeMapping().getId().toString())) {
							recipeList.add(woFile.getWoRecipeMapping().getId().toString());
							mappingWrapper.setRecpiList(recipeList);
							List<String> operatorList = mappingWrapper.getOperatorList();
							operatorList.add(NVWorkorderUtils.getOperatorNameFromFileName(woFile.getFileName()));
							mappingWrapper.setOperatorList(operatorList);
						}
					} else {
						List<String> recipeList = new ArrayList<>();
						recipeList.add(woFile.getWoRecipeMapping().getId().toString());
						List<String> operatorList = new ArrayList<>();
						operatorList.add(NVWorkorderUtils.getOperatorNameFromFileName(woFile.getFileName()));
						RecipeMappingWrapper recipeMappingWrapper = new RecipeMappingWrapper();
						recipeMappingWrapper.setOperatorList(operatorList);
						recipeMappingWrapper.setRecpiList(recipeList);
						finalMap.put(woFile.getWoRecipeMapping().getRecipe().getCategory(), recipeMappingWrapper);
					}
				}
			} catch (Exception e) {
				logger.error("exception in method getMappingDataForMaster {} ", Utils.getStackTrace(e));
			}
		}
		logger.info("finalMap is ==>{}", finalMap);
		return finalMap;
	}

	private Map<String, Object> getMappingDataFromWoFileForLayer3(List<WOFileDetail> woFileList) {
		Map<String, Object> fileMapping = new HashMap<>();
		List<Integer> unqueId = new ArrayList<>();
		Map<String, Map<String, String>> recipeMapping = new HashMap<>();
		Map<String, String> operatorMapping = new HashMap<>();
		Map<String, Map<String, String>> recipeMappingDefault = new HashMap<>();
		Map<String, String> operatorMappingDefault = new HashMap<>();
		boolean isFirstRecord = Boolean.TRUE;
		for (WOFileDetail woFile : woFileList) {
			if (!woFile.getFileName().contains(WO_FILE_NAME_PREFIX_FLOOR_PLAN)) {
				isFirstRecord = setDefaultDetailIntoMap(fileMapping, recipeMappingDefault, operatorMappingDefault,
						isFirstRecord, woFile, null);
				setDriveDetailIntoMap(recipeMapping, operatorMapping, woFile, unqueId);
			}
		}
		fileMapping.put(QMDLConstant.RECIPE, recipeMapping);
		fileMapping.put(QMDLConstant.OPERATOR, operatorMapping);

		return fileMapping;
	}
	
	

	private TreeMap<String, List<String>> getOperatorWiseRecipeMappingListMapFromWoId(Integer woId) {

		List<WORecipeMapping> woRecipeMappingList = woRecipeMappingdao.getWORecipeByGWOId(woId);
		TreeMap<String, List<String>> woRecipeMap = new TreeMap<>();

		for (WORecipeMapping woRecipe : woRecipeMappingList) {
			if (woRecipe.getOperator() != null && woRecipe.getStatus().equals(WORecipeMapping.Status.COMPLETED)) {
				List<String> recipeListTemp = new ArrayList<>();
				if (woRecipeMap.containsKey(woRecipe.getOperator())) {
					recipeListTemp = woRecipeMap.get(woRecipe.getOperator());
				}
				recipeListTemp.add(woRecipe.getId().toString());
				woRecipeMap.put(woRecipe.getOperator(), recipeListTemp);
			}
		}

		return woRecipeMap;
	}

	private Map<String, List<String>> getMappingDataFromWoFile(List<WOFileDetail> woFileList) {
		Map<String, List<String>> finalMap = new HashMap<>();
		List<String> recpiList = new ArrayList<>();
		List<String> operatorList = new ArrayList<>();
		Set<String> recipeCategoryList = new HashSet<>();
		for (WOFileDetail woFile : woFileList) {
			if (!recpiList.contains(woFile.getWoRecipeMapping().getId().toString())
					&& isValidOperatorName(woFile.getFileName())) {
				recpiList.add(woFile.getWoRecipeMapping().getId().toString());
				operatorList.add(NVWorkorderUtils.getOperatorNameFromFileName(woFile.getFileName()));
				recipeCategoryList.add(woFile.getWoRecipeMapping().getRecipe().getCategory());
			}
		}
		finalMap.put(QMDLConstant.RECIPE, recpiList);
		finalMap.put(QMDLConstant.OPERATOR, operatorList);
		finalMap.put(QMDLConstant.RECIPE_CATEGORY, recipeCategoryList.stream().collect(Collectors.toList()));
		return finalMap;
	}

	private boolean isValidOperatorName(String fileName) {
		String operatorName = NVWorkorderUtils.getOperatorNameFromFileName(fileName);
		return operatorName != null && !operatorName.equalsIgnoreCase(OPERATOR_NAME_BENCHMARK);
	}

	@Override
	public Layer3ReportWrapper getWOReportDump(String fileName) {
		Layer3ReportWrapper layer3Wrapper = null;
		try {
			
			String filePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH) + fileName;
		
			NVLayer3Utils.validateFilePath(filePath);
			
			File file = new File(filePath);
			try (FileInputStream fileInputStream = new FileInputStream(file)) {
				layer3Wrapper = new Layer3ReportWrapper();
				layer3Wrapper.setFileName(file.getName());
				layer3Wrapper.setFile(IOUtils.toByteArray(fileInputStream));
			}
		} catch (IOException e) {
			logger.error("Error in creating getWOReportDump Result {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (Exception e) {
			logger.error("Error in creating getWOReportDump Result {}  ", Utils.getStackTrace(e));			
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);			
		}
		return layer3Wrapper;
	}

	@Override
	public String getSignalMessageRecipeWise(Integer workorderId, String lastRowKey, String direction,
			List<String> recipeId, List<String> operatorList, String message) {
		StringBuilder json = getIntialStringForJson();
		Boolean isData = (lastRowKey == null || lastRowKey.equalsIgnoreCase("null")) ? Boolean.FALSE : Boolean.TRUE;
		try {
			String tableName = ConfigUtils.getString(NVLayer3Constants.QMDL_SIGNALING_DATA_TABLE);
			String prefix = getPrefixFromRecipeAndOperatorList(workorderId, recipeId, operatorList);
			List<HBaseResult> resultList = nvLayer3HbaseDao.scanQMDLDataFromHBaseWithPagination(tableName, prefix,
					direction, lastRowKey);

			for (HBaseResult result : resultList) {
				isData = Boolean.TRUE;
				json.append(getParsedSignalMessageData(result, message));

			}
		} catch (Exception e) {
			logger.error("Exception in Getting Result from hbase {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
		return getSignalingMsgFinalResponse(json, isData);
	}

	@Override
	public Map<String, String> getNeighbourDataRecipeWise(Integer workorderId, List<String> recipeId,
			List<String> operatorList) {
		Map<String, String> neighbourMap = new HashMap<>();
		List<String> timestampList = new ArrayList<>();
		try {
			String jsonDriveData = getDriveDetailReceipeWise(workorderId, recipeId, operatorList);
			String[] driveDataArrayList = removeUnwantedStringFromResult(jsonDriveData);

			if (driveDataArrayList.length > 0) {
				for (String driveDataArray : driveDataArrayList) {
					String[] singlerecord = driveDataArray.split(DRIVE_COMMA_SEPRATOR, ForesightConstants.MINUS_ONE);
					if (singlerecord.length > NVLayer3Constants.DUMP_INDEX_TIMESTAMP) {
						timestampList.add(singlerecord[NVLayer3Constants.DUMP_INDEX_TIMESTAMP]);
					}
				}

				String tableName = ConfigUtils.getString(NVLayer3Constants.LAYER3_NEIGHBOUR_TABLE);
				List<String> prefixList = getPrefixListForNeighbourData(workorderId, recipeId, operatorList);
				for (String prefix : prefixList) {
					List<HBaseResult> resultList = nvLayer3HbaseDao.scanNeighbourDataFromHBase(tableName, prefix);
					for (HBaseResult result : resultList) {
						getNeighbourData(result, neighbourMap, prefix, timestampList);
					}
				}
			}
			logger.info("Getting Neighbour Data Map size is:{}", neighbourMap.size());
		} catch (Exception e) {
			logger.error("Exception in Getting Result from hbase {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
		if (neighbourMap.size() == 0) {
			neighbourMap.put(NVLayer3Constants.RESULT, NVLayer3Constants.NO_DATA_FOUND);
		}
		return neighbourMap;
	}

	public void getNeighbourData(HBaseResult result, Map<String, String> neighbourMap, String prefix,
			List<String> timestampList) {
		String[] timestamp = result.getRowKey().split(prefix);

		if (timestampList.contains(timestamp[QMDLConstant.ONE])) {
			neighbourMap.put(timestamp[QMDLConstant.ONE], result.getString(QMDLConstant.NEIGHBOUR));
		}
	}

	private String getSignalingMsgFinalResponse(StringBuilder json, Boolean isData) {
		if (isData) {
			addPostFixOfcsvIntoJson(json);
			return json.toString();
		} else {
			return QMDLConstant.NO_DATA_FOUND;
		}
	}

	public List<String> getPrefixListForNeighbourData(Integer workorderId, List<String> recipeId,
			List<String> operatorList) {
		List<String> prefixList = new ArrayList<>();
		Set<String> operatorSet = new HashSet<>(operatorList);
		Set<String> recipeIdSet = new HashSet<>(recipeId);

		for (String receipeId : recipeIdSet) {
			for (String operator : operatorSet) {
				String rowkey = getRowKeyForLayer3(workorderId, operator, receipeId);

				prefixList.add(rowkey);
			}
		}
		return prefixList;
	}

	@Override
	public String getKpiStatsRecipeDataForReport(Integer workorderId, String kpi, List<String> recipeId,
			List<String> operatorList) {
		List<Get> getList = getListForKpiStats(workorderId, recipeId, operatorList, kpi);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_KPI_TABLE);
		try {
			List<HBaseResult> hbaseResults = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			String json = getFinalKpiStatsFromResultForReport(hbaseResults, kpi);
			return getJsonFromDetailMsg(json);
		} catch (IOException e) {
			logger.error("IOException in Getting getKpiStatsRecipeDataForReport Result {}  ", e.getMessage());
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.debug("BusinessException in Getting getKpiStatsRecipeDataForReport Result {}  ", e.getMessage());
			return QMDLConstant.INVALID_ARGUMENT;
		}
	}

	private String getFinalKpiStatsFromResultForReport(List<HBaseResult> hbaseResults, String kpi) {
		List<String> kpiValueMap = NVLayer3Utils.getKpiListForValueMapping();
		if (kpiValueMap.contains(kpi)) {
			return convertKpiMapToStats(processKpiMapFromResult(hbaseResults), kpi);
		} else {
			return processRangeWiseKpiFromResult(hbaseResults, kpi);
		}
	}

	private String convertKpiMapToStats(TreeMap<String, Long> processKpiMapFromResult, String kpi) {
		switch (kpi) {
		case QMDLConstant.CQI:
			return createStatsByRange(processKpiMapFromResult, QMDLConstant.CQI_MIN, QMDLConstant.CQI_MAX);
		case QMDLConstant.UL_EARFCN:
			return createStatsByRange(processKpiMapFromResult, QMDLConstant.UL_EARFCN_MIN, QMDLConstant.UL_EARFCN_MAX);
		case QMDLConstant.DL_EARFCN:
			return createStatsByRange(processKpiMapFromResult, QMDLConstant.DL_EARFCN_MIN, QMDLConstant.DL_EARFCN_MAX);
		case QMDLConstant.SPATIAL_RANK:
			return createStatsByRange(processKpiMapFromResult, QMDLConstant.SPATIALRANK_MIN,
					QMDLConstant.SPATIALRANK_MAX);
		case QMDLConstant.OUT_OF_SYNC_BLER:
			return createStatsByRange(processKpiMapFromResult, QMDLConstant.OUT_OF_SYNC_BLER_MIN,
					QMDLConstant.OUT_OF_SYNC_BLER_MAX);
		case QMDLConstant.RANK_INDEX:
			return createStatsByRange(processKpiMapFromResult, QMDLConstant.RANK_INDEX_MIN,
					QMDLConstant.RANK_INDEX_MAX);

		case QMDLConstant.TRACKING_AREA_CODE:
			return createStatsByRange(processKpiMapFromResult, QMDLConstant.TRACKING_AREA_CODE_MIN,
					QMDLConstant.TRACKING_AREA_CODE_MAX);
		case QMDLConstant.PHYSICAL_CELL_ID:
			return createStatsByRange(processKpiMapFromResult, QMDLConstant.PCI_MIN, QMDLConstant.PCI_MAX);

		case QMDLConstant.PMI_INDEX:
			return createStatsByRange(processKpiMapFromResult, QMDLConstant.PMI_MIN, QMDLConstant.PMI_MAX);

		case QMDLConstant.NUM_RBS:
			return createStatsByRange(processKpiMapFromResult, QMDLConstant.NUMRB_MIN, QMDLConstant.NUMRB_MAX);

		case QMDLConstant.MCS:
			return createStatsByRange(processKpiMapFromResult, QMDLConstant.MCS_MIN, QMDLConstant.MCS_MAX);

		case QMDLConstant.CARRIER_INDEX:
			return createStatsByRange(processKpiMapFromResult, QMDLConstant.CARRIER_INDEX_MIN,
					QMDLConstant.CARRIER_INDEX_MAX);
		default:
			break;
		}
		return Arrays.toString(new Long[QMDLConstant.EMPTY_ARRAY_SIZE]);
	}

	private String createStatsByRange(TreeMap<String, Long> processKpiMapFromResult, Integer cqiMin, Integer cqiMax) {
		Long[] longStats = new Long[Math.abs(cqiMax - cqiMin) + 1];
		int count = 0;
		for (long index = cqiMin; index <= cqiMax; index++) {
			String value = String.valueOf(index);
			if (processKpiMapFromResult.containsKey(value)) {
				longStats[count++] = processKpiMapFromResult.get(value);
			} else {
				longStats[count++] = 0L;
			}
		}
		return Arrays.toString(longStats);
	}
	
	
	@Override
	public DriveDataWrapper getFloorplanDataFromLayer3Report(Integer workorderId, String operator, String recipeId) {
		try {
			
			String isEnabled = ConfigUtils.getString(NVLayer3Constants.IS_LAYER3_FRAMEWORK_ENABLED);
			String rowKey = null;
			if (ReportConstants.TRUE.equalsIgnoreCase(isEnabled)) {
				rowKey = NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId)
						+ NVLayer3Utils.getRowkeyFromWorkOrderId(recipeId);
			} else {
				rowKey = NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId) + operator
						+ NVLayer3Utils.getRowkeyFromWorkOrderId(recipeId);
			}			
			
			logger.info("rowKey is =============={}", rowKey);
			return getFloorPlanDataListFromHBaseResult(nvLayer3HbaseDao.getQMDLDataFromHBase(
					getFloorPlanDataGet(rowKey), ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE)));
		} catch (Exception e) {
			logger.error("Error in getFloorplanDataFromLayer3Report while Getting Result from hbase {}  ",
					Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	@Override
	public DriveDataWrapper getFloorplanDataFromLayer3ReportForFramework(Integer workorderId, String recipeId) {
		try {
			String rowKey = NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId)
					+ NVLayer3Utils.getRowkeyFromWorkOrderId(recipeId);
			logger.info("rowKey is =============={}", rowKey);
			return getFloorPlanDataListFromHBaseResult(nvLayer3HbaseDao.getQMDLDataFromHBase(
					getFloorPlanDataGet(rowKey), ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE)));
		} catch (Exception e) {
			logger.error("Error in getFloorplanDataFromLayer3Report while Getting Result from hbase {}  ",
					Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	private Get getFloorPlanDataGet(String rowKey) {
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.JSON.getBytes());
		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.IMAGE_FILE.getBytes());
		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.FILE_PATH.getBytes());
		return get;
	}

	@Override
	public List<RemarkDataWrapper> getRemarkTestSkipFromLayer3Report(Integer workorderId, List<String> recipeId,
			List<String> operatorList) {
		logger.info("inside the method getRemarkTestSkipFromLayer3Report workorderId{} ", workorderId);
		List<Get> getList = getListForgetRemarkTestSkip(workorderId, recipeId, operatorList);
		String table = ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE);
		try {
			List<HBaseResult> hbaseResults = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);

			return getListForGetRemarkTestSkipHBaseResult(hbaseResults);
		} catch (IOException e) {
			logger.error("IOException  in Getting getRemarkTestSkipFromLayer3Report Result {}  ", e.getMessage());
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("BusinessException in Getting getRemarkTestSkipFromLayer3Report Result {}  ", e.getMessage());
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);

		}
	}

	@Override
	public List<Get> getListForgetRemarkTestSkip(Integer workorderId, List<String> recipeId,
			List<String> operatorList) {
		List<Get> getList = new ArrayList<>();
		for (String receipeId : recipeId) {
			for (String operator : operatorList) {
				Get get = new Get(Bytes.toBytes(getRowKeyForLayer3(workorderId, operator, receipeId)));
				get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.REMARK.getBytes());
				get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.TEST_SKIP.getBytes());
				getList.add(get);
			}
		}
		return getList;
	}

	private List<RemarkDataWrapper> getListForGetRemarkTestSkipHBaseResult(List<HBaseResult> hbaseResults) {
		List<RemarkDataWrapper> dataList = new ArrayList<>();
		for (HBaseResult result : hbaseResults) {
			RemarkDataWrapper remarkDataWrapper = new RemarkDataWrapper();
			if (result != null) {
				if (result.getString(QMDLConstant.TEST_SKIP) != null) {
					remarkDataWrapper.setTestSkipData(result.getString(QMDLConstant.TEST_SKIP));
				}
				if (result.getString(QMDLConstant.REMARK) != null) {
					remarkDataWrapper.setRemarkData(result.getString(QMDLConstant.REMARK));
				}

			}
			dataList.add(remarkDataWrapper);
		}

		return dataList;
	}

	private DriveDataWrapper getFloorPlanDataListFromHBaseResult(HBaseResult result) {
		DriveDataWrapper driveDataWrapper = new DriveDataWrapper();

		if (result != null) {
			if (result.getString(QMDLConstant.JSON) != null) {
				driveDataWrapper.setJson((result.getString(QMDLConstant.JSON)));
			}
			if (result.getValue(QMDLConstant.IMAGE_FILE.getBytes()) != null) {
				driveDataWrapper.setImg((result.getValue(QMDLConstant.IMAGE_FILE.getBytes())));
			}
			if (result.getValue(QMDLConstant.FILE_PATH.getBytes()) != null) {
				driveDataWrapper.setFilePath((result.getString(QMDLConstant.FILE_PATH)));
			}
		}

		return driveDataWrapper;
	}

	@Override
	public Map<String, List<String>> getRecipeDetailByWorkorderList(List<Integer> workorderIdList) {
		try {
			List<WOFileDetail> woFileList = woFileDetailService.getFileDetailByWorkOrderIdList(workorderIdList);
			return getMappingDataFromWoFile(woFileList);
		} catch (Exception e) {
			logger.error("Getting Exception in getRecipeDetailByWorkorderList {}   {} ", workorderIdList,
					Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String getYoutubeDataFromLayer3Report(Integer workorderId, String operator, String recipeId) {
		try {
			String rowKey = NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId) + operator
					+ NVLayer3Utils.getRowkeyFromWorkOrderId(recipeId);
			return getKPIDataFromHbaseResult(
					nvLayer3HbaseDao.getQMDLDataFromHBase(getLayer3ReportDataGet(rowKey, QMDLConstant.YOUTUBE_TEST),
							ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE)),
					QMDLConstant.YOUTUBE_TEST);
		} catch (Exception e) {
			logger.error("Error in getYoutubeDataFromLayer3Report while Getting Result from hbase {}  ",
					Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	@Override
	public String getHttpDownLinkDataFromLayer3Report(Integer workorderId, String operator, String recipeId) {
		try {
			String rowKey = NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId) + operator
					+ NVLayer3Utils.getRowkeyFromWorkOrderId(recipeId);
			return getKPIDataFromHbaseResult(
					nvLayer3HbaseDao.getQMDLDataFromHBase(
							getLayer3ReportDataGet(rowKey, QMDLConstant.HTTP_LINK_DOWNLOAD),
							ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE)),
					QMDLConstant.HTTP_LINK_DOWNLOAD);
		} catch (Exception e) {
			logger.error("Error in getHttpDownLinkDataFromLayer3Report while Getting Result from hbase {}  ",
					Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	private Get getLayer3ReportDataGet(String rowKey, String testType) {
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), testType.getBytes());
		return get;
	}

	private String getKPIDataFromHbaseResult(HBaseResult result, String testType) {
		String data = null;
		if (result != null && result.getString(testType) != null) {
			data = result.getString(testType);
		}
		return data;
	}

	@Override
	public String getQuickTestDataFromLayer3Report(Integer workorderId, String operator, String recipeId) {
		try {
			String rowKey = NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId) + operator
					+ NVLayer3Utils.getRowkeyFromWorkOrderId(recipeId);
			return getKPIDataFromHbaseResult(
					nvLayer3HbaseDao.getQMDLDataFromHBase(getLayer3ReportDataGet(rowKey, QMDLConstant.QUICK_TEST),
							ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE)),
					QMDLConstant.QUICK_TEST);
		} catch (Exception e) {
			logger.error("Error in Getting Result from hbase {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	@Override
	public Map<String, Double> getSummaryPeakValueReceipeWise(Integer workorderId, List<String> recipeId,
			List<String> operatorList) {
		List<String> columnList = Arrays.asList(QMDLConstant.DL_TECHNOLOGY_WISE_PEAK_VALUE);
		logger.info("recipeId temp  {} ", recipeId);
		List<Get> getList = getListForSummary(workorderId, recipeId, operatorList, columnList);

		String table = ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE);
		Map<String, Double> peakValueMap = null;
		try {
			List<HBaseResult> hbaseResultList = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			peakValueMap = getPeakValueMap(hbaseResultList);
		} catch (IOException e) {
			logger.error("IOException in Getting   getSummaryPeakValueReceipeWise  Result {}  ",
					Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("BusinessException in Getting getSummaryPeakValueReceipeWise Result {}  ",
					Utils.getStackTrace(e));
			return peakValueMap;
		}
		return peakValueMap;
	}

	private Map<String, Double> getPeakValueMap(List<HBaseResult> hbaseResultList) throws IOException {
		logger.info("Inside method getPeakValueMap for hbaseResultList of Size {} ",
				hbaseResultList != null ? hbaseResultList.size() : null);
		Map<String, Double> peakDLvaluesMap = new HashMap<>();
		int count = 0;
		for (HBaseResult result : hbaseResultList) {
			try {
				String json = result.getString(QMDLConstant.DL_TECHNOLOGY_WISE_PEAK_VALUE);
				logger.info("Json Data {}, is {} ", ++count, json);
				if (json != null) {
					Map<String, Double> peakDLMap = new ObjectMapper().readValue(json, HashMap.class);
					logger.info("peakDLMap Data {} ", peakDLMap);
					peakDLMap.forEach((k, v) -> peakDLvaluesMap.merge(k, v, Double::max));
				}
				logger.info("peakDLvaluesMap Data {} ", peakDLvaluesMap);
			} catch (Exception e) {
				logger.error("Exception insdie method getPeakValueMap {} ", Utils.getStackTrace(e));
			}
		}
		return peakDLvaluesMap;

	}

	@Override
	public String getDriveSummaryReceipeWiseNew(Integer workorderId, List<String> recipList,
			List<String> operatorlist) {
		List<String> columnList = getSummaryColumnList();
		List<Get> getList = getListForSummaryNew(workorderId, recipList, operatorlist, columnList);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_DATA_TABLE);
		try {
			List<HBaseResult> hbaseResultList = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			String summaryJson = getFinalSummaryFromResult(hbaseResultList);

			return getFinalJson(summaryJson);
		} catch (IOException e) {
			logger.error("IOException in Getting   getDriveSummaryReceipeWise  Result {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("BusinessException in Getting getDriveSummaryReceipeWise Result {}  ", Utils.getStackTrace(e));
			return QMDLConstant.INVALID_ARGUMENT;
		}
	}

	private List<Get> getListForSummaryNew(Integer workorderId, List<String> recipeId, List<String> operatorList,
			List<String> columnList) {
		List<Get> getList = new ArrayList<>();
		int index = 0;
		for (String receipeId : recipeId) {
			try {
				Get get = new Get(Bytes.toBytes(getRowKeyForLayer3(workorderId, operatorList.get(index), receipeId)));
				addColumnsToGet(columnList, get);
				getList.add(get);
				index++;
			} catch (Exception e) {
				logger.error("Exception inside method getListForSummaryNew {} ", e.getMessage());
			}
		}
		return getList;
	}

	@Override
	public String createSignalMessageRecipeWise(Integer workOrderId, List<String> recipeId, List<String> operatorList,
			String woName) {
		try {
			String tableName = ConfigUtils.getString(NVLayer3Constants.QMDL_SIGNALING_DATA_TABLE);
			Set<String> prefixList = getPrefixListFromParamList(workOrderId, recipeId, operatorList);
			List<HBaseResult> resultList = nvLayer3HbaseDao.scanQMDLDataFromHBaseForMSGCsv(tableName, prefixList);
			StringBuilder csv = getCsvFromHbaseResult(resultList);
			return QMDLConstant.PATH_PREFIX_JSON + writeCsvToLocalDisk(csv,
					woName.replace(Symbol.SPACE_STRING, Symbol.EMPTY_STRING) + QMDLConstant.CSV_EXTENSION,
					ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH)) + QMDLConstant.PATH_POSTFIX_JSON;
		} catch (Exception e) {
			logger.error("Error in Getting getSignalingMessagesData from hbase {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	private String writeCsvToLocalDisk(StringBuilder csv, String fileName, String filePath) throws IOException {
		String fileAbsoultePath = filePath + Symbol.SLASH_FORWARD_STRING + fileName;
		try {
			File file = new File(fileAbsoultePath);
			try (FileWriter fileWriter = new FileWriter(file)) {

				fileWriter.write(csv.toString());
				fileWriter.flush();
			}
		} catch (Exception e) {
			logger.error("Error in writeCsvToLocalDisk{}  ", Utils.getStackTrace(e));
		}
		return fileName;
	}

	private StringBuilder getCsvFromHbaseResult(List<HBaseResult> resultList) {
		StringBuilder csv = new StringBuilder();
		String delimeter = ConfigUtils.getString(QMDLConstant.LAYER3_MSG_CSV_DELIMETER);
		addSignalingMsgHeader(csv, delimeter);
		for (HBaseResult result : resultList) {
			addValueToCsv(csv, getDateFromTimestamp(result.getString(QMDLConstant.TIMESTAMP)), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.TIMESTAMP), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.SIP_EVENT), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.SIP_EVENT_MSG), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.EMM_MESSAGE), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.EMM_BEAN), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.EMS_MESSAGE), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.EMS_BEAN), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.B0C0_MSG_TYPE), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.B0C0_MSG), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.DIRECTION), delimeter);
			
			csv.setLength(csv.length() - QMDLConstant.STRING_EXTRA_LENTH);
			csv.append(QMDLConstant.NEXT_LINE);
		}
		return csv;
	}

	private void addValueToCsv(StringBuilder csv, String stringValue, String delimeter) {
		if (stringValue != null) {
			if (stringValue.contains(delimeter)) {
				stringValue = stringValue.replace(delimeter, Symbol.EMPTY_STRING);
			}
			csv.append(stringValue);
		}
		csv.append(delimeter);
	}

	private void addSignalingMsgHeader(StringBuilder csv, String delimeter) {
		String header = ConfigUtils.getString(QMDLConstant.LAYER3_MSG_CSV_HEADER);
		csv.append(header.replace(Symbol.UNDERSCORE_STRING, delimeter));
		csv.append(QMDLConstant.NEXT_LINE);
	}

	private Set<String> getPrefixListFromParamList(Integer workOrderId, List<String> recipeId,
			List<String> operatorList) {
		Set<String> prefixList = new HashSet<>();
		if (recipeId.isEmpty() || operatorList.isEmpty()) {
			prefixList.add(NVLayer3Utils.getRowkeyFromWorkOrderId(workOrderId));
		} else {
			for (String receipeId : recipeId) {
				for (String operator : operatorList) {
					prefixList.add(getRowKeyForLayer3(workOrderId, operator, receipeId));
				}
			}
		}
		return prefixList;
	}

	public String getHandoverDataFromHbase(Integer workorderId, String operator, String recipeId) {
		try {
			String rowKey = NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId) + operator
					+ NVLayer3Utils.getRowkeyFromWorkOrderId(recipeId);
			return getKPIDataFromHbaseResult(
					nvLayer3HbaseDao.getQMDLDataFromHBase(
							getLayer3ReportDataGet(rowKey, QMDLConstant.IDLE_HANDOVER_JSON),
							ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE)),
					QMDLConstant.IDLE_HANDOVER_JSON);
		} catch (Exception e) {
			logger.error("Error in Getting Result from hbase {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}

	}

	@Override
	public String getDriveDetailDataForWoIds(List<Integer> woIds) {
		List<Get> getList = getRowkeyListForWoIds(woIds);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_DATA_TABLE);
		try {
			List<HBaseResult> hbaseResults = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			String json = getFinalJsonFromListResult(hbaseResults);
			String data = getFinalJson(json);
			Map<String, String> dataMap = ReportUtil.convertCSVStringToMap(data);
			return dataMap.get(ReportConstants.RESULT);
		} catch (IOException e) {
			logger.error("IOException in Getting getDriveDetailData Result {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("BusinessException in Getting getDriveDetailData Result {}  ", Utils.getStackTrace(e));
			return QMDLConstant.INVALID_ARGUMENT;
		}
	}

	@Override
	public String getDriveSummaryForWoIds(List<Integer> woIds) {
		List<Get> getList = getRowkeyListForWoIds(woIds);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_DATA_TABLE);
		try {
			List<HBaseResult> hbaseResults = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			logger.info("inside getDriveSummaryForWoIds hbaseResults Size for woIds {} ",
					hbaseResults != null ? hbaseResults.size() : null);
			String summaryJson = getFinalSummaryFromResult(hbaseResults);
			String data = getFinalJson(summaryJson);
			Map<String, String> dataMap = ReportUtil.convertCSVStringToMap(data);
			logger.info("getSummaryData : {} , {}", data, dataMap);
			return dataMap.get(ReportConstants.RESULT);
		} catch (IOException e) {
			logger.error("IOException in Getting   getDriveSummary  Result {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("BusinessException in Getting getDriveSummary Result {}  ", Utils.getStackTrace(e));
			return QMDLConstant.INVALID_ARGUMENT;
		}
	}

	@Override
	public List<Get> getRowkeyListForWoIds(List<Integer> woIds) {
		List<String> columnList = getColumnListForDriveDetail();
		List<Get> getList = new ArrayList<>();
		for (Integer workOrderId : woIds) {
			Map<String, List<String>> recipeOperatorListMap = getDriveRecipeDetail(workOrderId);
			if (recipeOperatorListMap != null) {
				getList.addAll(getListForSummary(workOrderId, recipeOperatorListMap.get(QMDLConstant.RECIPE),
						recipeOperatorListMap.get(QMDLConstant.OPERATOR), columnList));
			}
		}
		return getList;
	}

	@Override
	public List<String> getRowkeyListForWoIds(Integer workorderId, List<String> recipeId, List<String> operatorList) {
		List<String> rowkeyList = new ArrayList<>();
		operatorList = operatorList.stream().distinct().collect(Collectors.toList());
		for (String receipeId : recipeId) {
			for (String operator : operatorList) {
				String rowKey = getRowKeyForLayer3(workorderId, operator, receipeId);
				rowkeyList.add(rowKey);
			}
		}
		return rowkeyList;
	}

	@Override
	public String getKpiStatsDataForGetListOfKpi(List<Get> getList, String kpi) {
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_KPI_TABLE);
		try {
			List<HBaseResult> hbaseResults = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			String json = getFinalKpiStatsFromResultForReport(hbaseResults, kpi);
			return getJsonFromDetailMsg(json);
		} catch (IOException e) {
			logger.error("IOException in Getting getKpiStatsRecipeDataForReport Result {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("BusinessException in Getting getKpiStatsRecipeDataForReport Result {}  ",
					Utils.getStackTrace(e));
			return QMDLConstant.INVALID_ARGUMENT;
		}
	}

	@Override
	public Map<String, String> getSummaryMapCategoryWiseForWoIds(List<Integer> woIds) {
		Map<String, String> categoryWiseSmmry = new HashMap<>();
		try {
			List<String> catgoryList = Arrays.asList(ReportConstants.RECIPE_CATEGORY_DRIVE,
					ReportConstants.RECIPE_CATEGORY_STATIONARY);
			List<String> columnList = getSummaryColumnList();
			for (String category : catgoryList) {
				List<String> singleCategory = new ArrayList<>();
				singleCategory.add(category);
				List<Get> getList = getRowkeyListForWoIds(woIds, singleCategory, columnList);
				String table = ConfigUtils.getString(NVLayer3Constants.QMDL_DATA_TABLE);
				List<HBaseResult> hbaseResults = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
				logger.debug("inside getSummaryMapCategoryWiseForWoIds hbaseResults Size for woIds {} ",
						hbaseResults != null ? hbaseResults.size() : null);
				String summaryJson = getFinalSummaryFromResult(hbaseResults);
				logger.debug("summaryJsin for category  {} , {}  ", category, summaryJson);
				String data = getFinalJson(summaryJson);
				Map<String, String> dataMap = ReportUtil.convertCSVStringToMap(data);
				categoryWiseSmmry.put(category, dataMap.get(ReportConstants.RESULT));
			}
			return categoryWiseSmmry;
		} catch (IOException e) {
			logger.error("IOException in Getting   getDriveSummary  Result {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("BusinessException in Getting getDriveSummary Result {}  ", e.getMessage());
			return categoryWiseSmmry;
		}
	}

	public List<Get> getRowkeyListForWoIds(List<Integer> woIds, List<String> category, List<String> columnList) {
		List<Get> getList = new ArrayList<>();
		for (Integer workOrderId : woIds) {
			Map<String, List<String>> recipeOperatorListMap = getDriveRecipeDetailCategoryWise(workOrderId, category);
			if (recipeOperatorListMap != null) {
				getList.addAll(getListForSummary(workOrderId, recipeOperatorListMap.get(QMDLConstant.RECIPE),
						recipeOperatorListMap.get(QMDLConstant.OPERATOR), columnList));
			}
		}
		return getList;
	}

	private Map<String, List<String>> getDriveRecipeDetailCategoryWise(Integer workrorderId, List<String> category) {
		try {
			List<WOFileDetail> woFileList = woFileDetailService.getFileDetailByWorkOrderId(workrorderId, category);
			return getMappingDataFromWoFile(woFileList);
		} catch (RestException e) {
			logger.error("Getting Exception in file information {}   {} ", workrorderId, Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public Map<String, String> getBandWiseDataMapForCategory(List<Integer> woIds, String category, boolean bool) {
		List<String> columnList = getSummaryColumnList();
		Map<String, String> bandWiseSummary = new HashMap<>();
		List<Get> getList = getRowkeyListForWoIds(woIds, Arrays.asList(category), columnList);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_DATA_TABLE);
		List<HBaseResult> hbaseResults;
		try {
			if (!getList.isEmpty()) {
				hbaseResults = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
				logger.info("hbaseResults Size for woIds {} ", hbaseResults != null ? hbaseResults.size() : null);
				return getBandWiseMapFromHbase(bool, bandWiseSummary, hbaseResults);
			}
		} catch (Exception e) {
			logger.error("Exception inside the method getBandWiseDataMapForCategory   {}", e.getMessage());
		}
		return null;

	}

	public Map<String, String> getBandWiseMapFromHbase(boolean bool, Map<String, String> bandWiseSummary,
			List<HBaseResult> hbaseResults) {
		if (hbaseResults != null && !hbaseResults.isEmpty()) {
			if (bool) {
				return getBandWiseData(bandWiseSummary, hbaseResults);
			} else {
				String summry = getFinalSummaryFromResult(hbaseResults);
				bandWiseSummary.put(ReportConstants.SUMMARY, summry);
				return bandWiseSummary;
			}
		}
		return null;
	}

	private Map<String, String> getBandWiseData(Map<String, String> bandWiseSummary, List<HBaseResult> hbaseResults) {
		Map<String, List<HBaseResult>> eafcnWiseMapData = getEarfcnWiseMapFromHbaseResult(hbaseResults);
		for (Entry<String, List<HBaseResult>> entry : eafcnWiseMapData.entrySet()) {
			logger.info(" entry.getValue() is ===={}", entry.getValue());
			String summry = getFinalSummaryFromResult(entry.getValue());
			logger.info(" entry.getValue() summry is ===={}", summry);

			bandWiseSummary.put(entry.getKey(), summry);
		}
		return bandWiseSummary;
	}

	private Map<String, List<HBaseResult>> getEarfcnWiseMapFromHbaseResult(List<HBaseResult> hbaseResults) {
		Map<String, List<HBaseResult>> map = new HashMap<>();
		try {
			for (HBaseResult hBaseResult : hbaseResults) {
				String summary = hBaseResult.getString(QMDLConstant.SUMMARY_JSON);
				logger.info("summary is {} ", summary);
				if (summary != null && !summary.isEmpty()) {
					Integer band = getBandFromsummary(summary);
					if (band != null && band != 0) {
						Integer freQuency = NVLayer3Utils.getFrequencyFromBand(band);
						String earfcn = String.valueOf(freQuency);
						if (map.containsKey(earfcn)) {
							List<HBaseResult> resultList = map.get(earfcn);
							resultList.add(hBaseResult);
							map.put(earfcn, resultList);
						} else {
							List<HBaseResult> list = new ArrayList<>();
							list.add(hBaseResult);
							map.put(earfcn, list);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside the method getEarfcnWiseMapFromHbaseResult {}", e.getMessage());
		}

		return map;
	}

	private Integer getBandFromsummary(String summary) {
		String band = null;
		String[] summaryArr = summary.split(QMDLConstant.COMMA, ForesightConstants.MINUS_ONE);
		if (summaryArr.length >= DriveHeaderConstants.HBASE_SUMMARY_BAND_INDEX) {
			band = summaryArr[DriveHeaderConstants.HBASE_SUMMARY_BAND_INDEX];

			if (band != null && !band.isEmpty()) {
				if (band.contains(ReportConstants.UNDERSCORE)) {
					String[] ar = band.split(ReportConstants.UNDERSCORE);
					return Integer.valueOf(ar[0]);
				} else {
					return Integer.valueOf(band);
				}
			}
		}
		return null;
	}

	@Override
	public String getSignalMessagesForBin(Long startTime, Long endTime, String rowPrefix) {
		try {
			StringBuilder json = getIntialStringForJson();
			String tableName = ConfigUtils.getString(NVLayer3Constants.QMDL_SIGNALING_DATA_TABLE);

			Scan scan = NVLayer3Utils.getScanForSignalMessageForBinByStartAndEndTime(startTime, endTime, rowPrefix,
					SCAN_ROW_SEPERATOR);
			List<HBaseResult> hbaseResultsBWTwotime = nvLayer3HbaseDao.scanQMDLDataFromHbase(tableName, scan);

			Scan scan1 = NVLayer3Utils.getScanForSignalMessageForBin(startTime, endTime, rowPrefix, SCAN_ROW_SEPERATOR);
			List<HBaseResult> hbaseResultsUptoEndTime = nvLayer3HbaseDao.scanQMDLDataFromHbase(tableName, scan1);
			logger.info("Getting hbase hbaseResultsBWTwotime {} hbaseResultsUptoEndTime{}",
					hbaseResultsBWTwotime.size(), hbaseResultsUptoEndTime.size());

			if (hbaseResultsBWTwotime.size() > 10) {
				Scan scan3 = NVLayer3Utils.getScanForSignalMessageForBin(startTime, endTime, rowPrefix,
						SCAN_ROW_SEPERATOR);
				hbaseResultsBWTwotime = nvLayer3HbaseDao.scanQMDLDataFromHbase(tableName, scan3);
				logger.info("Getting hbaseResultsBWTwotime size >10 as:{}", hbaseResultsBWTwotime.size());
				for (HBaseResult result : hbaseResultsBWTwotime) {
					json.append(getParsedSignalMessageData(result, startTime, endTime));
				}

			} else if (hbaseResultsBWTwotime.isEmpty()) {
				Scan scan2 = NVLayer3Utils.getScanForSignalMessageForBinByStartTime(startTime, endTime, rowPrefix,
						SCAN_ROW_SEPERATOR);
				FilterList filter = NVLayer3Utils.addFilterToLayer3SignalMsgForBin(rowPrefix);
				scan2.setFilter(filter);
				List<HBaseResult> hbaseResults2 = nvLayer3HbaseDao.scanQMDLDataFromHbase(tableName, scan2);

				logger.info("Getting hbase result size as inside empty hbaseresult:{}", hbaseResultsBWTwotime.size());
				for (HBaseResult result : hbaseResultsUptoEndTime) {
					json.append(getParsedSignalMessageData(result, startTime, endTime));
				}
				Boolean firstMsg = true;

				for (HBaseResult result : hbaseResults2) {
					json.append(getParsedSignalMessageDataAndHighLightFirst(result, startTime, endTime, firstMsg));
					firstMsg = false;
				}
			} else if (hbaseResultsBWTwotime.size() <= 10) {
				Scan scan2 = NVLayer3Utils.getScanForSignalMessageForBinByStartTime(startTime, endTime, rowPrefix,
						SCAN_ROW_SEPERATOR);
				FilterList filter = NVLayer3Utils.addFilterToLayer3SignalMsgForBin(rowPrefix);
				scan2.setFilter(filter);
				List<HBaseResult> hbaseResults2 = nvLayer3HbaseDao.scanQMDLDataFromHbase(tableName, scan2);

				hbaseResultsUptoEndTime.addAll(hbaseResults2);
				logger.info("Getting hbase result size as inside less than 10 msg get:{}",
						hbaseResultsUptoEndTime.size());

				for (HBaseResult result : hbaseResultsUptoEndTime) {
					json.append(getParsedSignalMessageData(result, startTime, endTime));
				}
			}

			addPostFixOfcsvIntoJson(json);
			return json.toString();
		} catch (Exception e) {
			logger.error("Exception in getSignalMessagesForBin {}	{}	{}	{}", startTime, endTime, rowPrefix,
					Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	private String getParsedSignalMessageDataAndHighLightFirst(HBaseResult result, Long startTime, Long endTime,
			Boolean firstMsg) {
		StringBuilder json = new StringBuilder();
		json.append(Symbol.BRACKET_OPEN_STRING);
		addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
		addValueToStringBuilder(json, result.getString(QMDLConstant.SIP_EVENT));
		addValueToStringBuilder(json, result.getString(QMDLConstant.EMM_MESSAGE));
		addValueToStringBuilder(json, result.getString(QMDLConstant.EMS_MESSAGE));
		addValueToStringBuilder(json, result.getString(QMDLConstant.B0C0_MSG_TYPE));
		addValueToStringBuilder(json, result.getRowKey());
		if (firstMsg) {
			addValueToStringBuilder(json, "1");
		} else {
			addValueToStringBuilder(json, "0");
		}
		addValueToStringBuilder(json, result.getString(QMDLConstant.DIRECTION));
		json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
		json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		return json.toString();
	}

	private String getParsedSignalMessageData(HBaseResult result, Long startTime, Long endTime) {
		StringBuilder json = new StringBuilder();
		json.append(Symbol.BRACKET_OPEN_STRING);
		addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
		addValueToStringBuilder(json, result.getString(QMDLConstant.SIP_EVENT));
		addValueToStringBuilder(json, result.getString(QMDLConstant.EMM_MESSAGE));
		addValueToStringBuilder(json, result.getString(QMDLConstant.EMS_MESSAGE));
		addValueToStringBuilder(json, result.getString(QMDLConstant.B0C0_MSG_TYPE));
		addValueToStringBuilder(json, result.getRowKey());
		Long tempTimestamp = Long.parseLong(result.getString(QMDLConstant.TIMESTAMP));
		if (tempTimestamp >= startTime && tempTimestamp <= endTime) {
			addValueToStringBuilder(json, "1");
		} else {
			addValueToStringBuilder(json, "0");
		}
		addValueToStringBuilder(json, result.getString(QMDLConstant.DIRECTION));
		json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
		json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		return json.toString();
	}

	@Override
	public String searchLayer3SignalMessage(Integer workorderId, List<String> recipeId, List<String> operatorList,
			String searchTerm) {
		StringBuilder json = getIntialStringForJson();
		try {
			String tableName = ConfigUtils.getString(NVLayer3Constants.QMDL_SIGNALING_DATA_TABLE);
			String prefix = getPrefixFromRecipeAndOperatorList(workorderId, recipeId, operatorList);
			Scan scan = NVLayer3Utils.getScanForSearchSignalMessage(prefix);
			List<HBaseResult> resultList = nvLayer3HbaseDao.scanQMDLDataFromHbase(tableName, scan);
			logger.info("Getting result size from Hbase for searching {}  ", resultList.size());
			return getResultFromHBaseResultList(searchTerm, resultList, json);
		} catch (Exception e) {
			logger.error("Error in method searchLayer3SignalMessage {} {} {} {} {}", workorderId, recipeId,
					operatorList, searchTerm, Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	private String getResultFromHBaseResultList(String searchTerm, List<HBaseResult> resultList, StringBuilder json) {
		if (resultList != null && !resultList.isEmpty()) {
			for (HBaseResult result : resultList) {
				json.append(getParsedSignalMessageData(result, searchTerm));
			}
		}
		addPostFixOfcsvIntoJson(json);
		return json.toString();
	}

	private boolean isSearchTermAvailableInHBaseResult(HBaseResult result, String searchTerm) {
		return isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.B0C0_MSG), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.B0C0_MSG_TYPE), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.EMM_MESSAGE), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.EMM_BEAN), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.EMS_MESSAGE), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.EMS_BEAN), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.SIP_EVENT_MSG), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.SIP_EVENT), searchTerm);
	}

	private boolean isValidHbaseColumnWithSearchTerm(String result, String searchTerm) {
		return result != null && searchTerm != null && !searchTerm.equalsIgnoreCase("null")
				&& result.toUpperCase().contains(searchTerm.toUpperCase());
	}

	@Override
	public Map<String, List<String[]>> getSmsDataForWoIds(List<Integer> woIds) {
		Map<String, List<String[]>> categoryWiseSmSMap = new HashMap<>();
		List<String> catgoryList = Arrays.asList(ReportConstants.RECIPE_CATEGORY_DRIVE,
				ReportConstants.RECIPE_CATEGORY_STATIONARY);
		for (String category : catgoryList) {
			try {
				List<Get> getList = getRowkeyListForWoIds(woIds, Arrays.asList(category),
						Arrays.asList(QMDLConstant.SMS));
				String table = ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE);
				List<HBaseResult> hbaseResults = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
				logger.debug("hbaseResults Size for category , {}  woIds {} ", category,
						hbaseResults != null ? hbaseResults.size() : null);
				List<String[]> dataList = getFinalListOfSmsData(hbaseResults);
				if (dataList != null) {
					categoryWiseSmSMap.put(category, dataList);
				}
			} catch (IOException e) {
				logger.error("Error in Getting   getDriveSummary  Result {}  ", Utils.getStackTrace(e));
				throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
			} catch (BusinessException e) {
				logger.warn("Error in Getting getDriveSummary Result {}  ", e.getMessage());
			}
		}
		logger.debug("categoryWiseSmSMap data {} ", categoryWiseSmSMap);
		return categoryWiseSmSMap;
	}

	private List<String[]> getFinalListOfSmsData(List<HBaseResult> hbaseResultList) {
		List<String[]> values = new ArrayList<>();
		for (HBaseResult result : hbaseResultList) {
			String json = result.getString(QMDLConstant.SMS);
			try {
				List<String[]> smsDataList = ReportUtil.convertCSVStringToDataList(json);
				values.addAll(smsDataList);
			} catch (Exception e) {
				logger.warn("Unable to cast json to List of array {} ", e.getMessage());
			}
		}
		return values;
	}

	@Override
	public String getNeighbourForBin(Long startTime, String rowPrefix) {
		Get get = new Get((rowPrefix + startTime).getBytes());
		try {
			HBaseResult hbaseResult = nvLayer3HbaseDao.getQMDLDataFromHBase(get,
					ConfigUtils.getString(QMDLConstant.LAYER3_NEIGHBOUR_TABLE));
			if (hbaseResult.getString(QMDLConstant.NEIGHBOUR) != null) {
				StringBuilder jsonString = new StringBuilder();
				jsonString.append(QMDLConstant.RESPONSE_START_JSON);
				jsonString.append(hbaseResult.getString(QMDLConstant.NEIGHBOUR));
				jsonString.append("\"}");

				return jsonString.toString();
			}
		} catch (IOException | BusinessException e) {
			logger.error("Getting error in fecthing neighbour data {} ", Utils.getStackTrace(e));
		}
		return QMDLConstant.EMPTY_STRING_RESPONSE;
	}

	@Override
	@Transactional
	public List<Map> getWorkorderListByGeographyOfPeriod(String geographyLevel, List<Integer> geographyId,
			List<GenericWorkorder.Status> statusList, List<TemplateType> templateList, List<Integer> quarterList,
			List<Integer> yearList) {
		List<Map> list = new ArrayList<>();
		try {
			Map<String, Long> startEndTimeMap = getStartEndTimeFromPeriodInfo(quarterList, yearList);
			logger.info("startEndTimeMap Data {} ", startEndTimeMap);

			List<GenericWorkorder> workorderList = genericWorkorderDao.findAllWorkorder(null, null, templateList,
					statusList, null, geographyLevel, geographyId, null, startEndTimeMap.get(START_TIMESTAMP),
					startEndTimeMap.get(END_TIMESTAMP));
			logger.info("workorderList Size {} ", workorderList != null ? workorderList.size() : null);
			for (GenericWorkorder genericWorkorder : workorderList) {
				list.add(getWorkorderIdJSON(genericWorkorder));
			}
			return list;
		} catch (Exception e) {
			logger.error("Getting error in fecthing  data from getWorkorderListByGeographyOfPeriod {} ",
					Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	@Override
	public Layer3ReportWrapper getWOReportDumpForStealth(String workorderId, String taskId, String strDate) {
		logger.info("Inside getWOReportDumpForStealth with WorkorderId {}, taskId {} ", workorderId, taskId);

		SimpleDateFormat sdf = new SimpleDateFormat(ReportConstants.DATE_FORMAT_STEALTH_HBASE);
		String date = null;
		Date testDate = null;
		if(!StringUtils.isBlank(strDate)) {
			testDate = new Date(Long.parseLong(strDate));
			date = sdf.format(testDate);
		}
		Layer3ReportWrapper layer3Wrapper = new Layer3ReportWrapper();
		String fileName = REPORT_NAME_PREFIX + workorderId + Symbol.UNDERSCORE_STRING + taskId
				+ Symbol.UNDERSCORE_STRING + System.currentTimeMillis()
				+ REPORT_FILE_EXTENSION;
		layer3Wrapper.setFileName(fileName);
		try {

			String clmName = ConfigUtils.getString(QMDLConstant.STEALTH_DATA_DUMP_COLUMN_NAME);

			String rawData = nvHbaseService.getStealthDataByWoIdAndTaskId(workorderId, taskId, date, clmName,
					testDate != null ? StealthUtils.getHourFromTimestamp(testDate.getTime()) : null);

			if (rawData != null && !rawData.trim().isEmpty()) {

				String filePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH);
				ReportUtil.createDirectory(filePath);
				InputStream stream = IOUtils.toInputStream(rawData, ENCODING_UTF_8);
				NVLayer3Utils.copyFileToLocalPath(filePath + fileName, stream);
				File file = new File(filePath + fileName);

				FileInputStream fileInputStream = new FileInputStream(file);
				layer3Wrapper.setFile(IOUtils.toByteArray(fileInputStream));
			}
		} catch (Exception e) {
			logger.error("Error Inside getWOReportDumpForStealth : {} ", Utils.getStackTrace(e));
		}
		return layer3Wrapper;
	}

	@Override
	public String getDriveDetailForStatisticData(List<String> kpiIndexList, Integer workorderId, List<String> recipes,
			List<String> operators, String fileName, Boolean isInbuilding, List<String> kpiNeighbourList) {
		GenericWorkorder genericworkorder = genericWorkorderDao.findByPk(workorderId);
		Boolean isBenachmarkWO = genericworkorder.getTemplateType().name().equals(TemplateType.NV_BENCHMARK.name())
				|| genericworkorder.getTemplateType().name().equals(TemplateType.NV_IB_BENCHMARK.name());
		logger.info("Is Benachmark WO {}", isBenachmarkWO);
		logger.info("Inside method getDriveDetailForStatisticData for workOrder Id {} , recipes {} , operators",
				recipes, operators);

		if (operators != null && !operators.isEmpty()) {
			if (isBenachmarkWO && operators.size() > ReportConstants.INDEX_ONE) {
				return getoperatorWiseZipFilePath(kpiIndexList, workorderId, recipes, operators, fileName, isInbuilding,
						kpiNeighbourList);
			} else {
				XSSFWorkbook workbook = createDyanmicXlsReportForoperator(kpiIndexList, workorderId, recipes,
						isInbuilding, operators, kpiNeighbourList);
				String newFileName = getFileNameForDynamicReport(workorderId, recipes, fileName,
						operators.get(ReportConstants.INDEX_ZER0));

				return writeWorksheetToExcel(workbook, newFileName);
			}

		} else {
			logger.debug("Operators are null {} ", operators);
			return null;
		}

	}

	private String getoperatorWiseZipFilePath(List<String> kpiIndexList, Integer workorderId, List<String> recipes,
			List<String> operators, String fileName, Boolean isInbuilding, List<String> kpiNeighbourList) {
		String localDirPath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH) + Symbol.SLASH_FORWARD_STRING
				+ new Date().getTime() + Symbol.SLASH_FORWARD_STRING;
		ReportUtil.createDirectory(localDirPath);
		
		
		Map<String, List<String>> OperatorRecipelistMap =getOperatorWiseRecipeMappingListMapFromWoId(workorderId);
		
		
		for (Entry<String, List<String>> entry : OperatorRecipelistMap.entrySet()) {
			
			String newFileName = getFileNameForDynamicReport(workorderId, entry.getValue(), fileName, entry.getKey());

			try {
				XSSFWorkbook workbook = createDyanmicXlsReportForoperator(kpiIndexList, workorderId, entry.getValue(),
						isInbuilding,operators, kpiNeighbourList);
				String absolutefilenamewithpath = localDirPath + Symbol.SLASH_FORWARD_STRING + newFileName;
				ExcelReportUtils.exportExcel(absolutefilenamewithpath, workbook);
			} catch (Exception e) {
				logger.error("Error while getting Json Data: {}" + Utils.getStackTrace(e));

			}
		}

		String zipFilePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH) + Symbol.SLASH_FORWARD_STRING
				+ fileName + ReportConstants.DOT_ZIP;
		logger.info("zipFilePath {}", zipFilePath);
		try {
			ZipUtils.zip(new File(localDirPath).listFiles(), zipFilePath);
			return QMDLConstant.PATH_PREFIX_JSON + fileName + ReportConstants.DOT_ZIP + QMDLConstant.PATH_POSTFIX_JSON;
		} catch (IOException e) {
			logger.info("IOException inside the method getoperatorWiseZipFilePath{}", Utils.getStackTrace(e));
		}
		return ForesightConstants.FAILURE_JSON;
	}

	private String getFileNameForDynamicReport(Integer workorderId, List<String> recipes, String fileName,
			String operator) {
		String newFileName = null;
		if (fileName.isEmpty()) {
			newFileName = EXCELDATA + Symbol.UNDERSCORE_STRING + workorderId + Symbol.UNDERSCORE_STRING
					+ NVLayer3Utils.getStringFromSetValues(recipes) + Symbol.DOT + XLS_EXTENSION;
		} else {
			newFileName = fileName + Symbol.UNDERSCORE_STRING + operator + Symbol.DOT + XLS_EXTENSION;
		}
		return newFileName;
	}

	private XSSFWorkbook createDyanmicXlsReportForoperator(List<String> kpiIndexList, Integer workorderId,
			List<String> recipes, Boolean isInbuilding, List<String> operators, List<String> neighbourIndexList) {

		Map<String, String> neighbourDataMap = new HashMap<>();
		Map<Integer, ArrayList<Double>> kpiMap = new TreeMap<>();
		Map<Integer, List<String>> commonKpiMap = new TreeMap<>();
		Map<String, Layer3StatisticsWrapper> kpiCsvMap = new TreeMap<>();

		logger.info("Getting neighbourIndexList{}", new Gson().toJson(neighbourIndexList));

		String jsonDriveData = getDriveDetailReceipeWise(workorderId, recipes, operators);
		String[] driveDataArray = removeUnwantedStringFromResult(jsonDriveData);

		if (!neighbourIndexList.isEmpty()) {
			neighbourDataMap = getNeighbourDataRecipeWise(workorderId, recipes, operators);
		}
		putDataIntoKpiMapFromNeighbourData(neighbourIndexList, neighbourDataMap, kpiMap);
		putDataIntoKpiMapDriveDetail(kpiIndexList, driveDataArray, kpiMap);

		if (isInbuilding != null && isInbuilding) {
			getCommonKpiMapForInbuilding(kpiIndexList, driveDataArray, commonKpiMap, neighbourIndexList,
					neighbourDataMap);

		} else {
			getCommonKpiMapFromDriveDetail(kpiIndexList, driveDataArray, commonKpiMap, neighbourIndexList,
					neighbourDataMap);
		}
		getKpiCsvMapFromDriveDetail(kpiMap, kpiCsvMap);
		return createExcelForStatistics(kpiMap, kpiCsvMap, commonKpiMap, kpiIndexList, isInbuilding,
				neighbourIndexList);

	}

	public void putDataIntoKpiMapFromNeighbourData(List<String> neighbourIndexList,
			Map<String, String> neighbourDataMap, Map<Integer, ArrayList<Double>> kpiMap) {
		for (String neighbourIn : neighbourIndexList) {
			ArrayList<Double> templist = new ArrayList<>();
			String[] neighbourIndex = neighbourIn.split(Symbol.UNDERSCORE_STRING);

			for (Map.Entry<String, String> entry : neighbourDataMap.entrySet()) {
				try {
					String[] neighbourDataArrayList = removeUnwantedStringFromNeighbourResult(entry.getValue());
					if (neighbourDataArrayList.length > (getIntegerValue(neighbourIndex[NEIGHBOUR_NO_INDEX])) - 1) {
						String[] neighbourData = neighbourDataArrayList[((getIntegerValue(
								neighbourIndex[NEIGHBOUR_NO_INDEX])) - 1)].split(DRIVE_COMMA_SEPRATOR,
										ForesightConstants.MINUS_ONE);

						Double tempValue = NVLayer3Utils.getDoubleFromCsv(neighbourData,
								getIntegerValue(neighbourIndex[NEIGHBOUR_KPI_INDEX]));

						if (tempValue != null) {
							templist.add(tempValue);
						}
					}
				} catch (Exception e) {
					logger.error("Error in Getting getKpiMapFromDriveDetail {}  ", Utils.getStackTrace(e));
				}

			}

			kpiMap.put((getIntegerValue(neighbourIndex[NEIGHBOUR_NO_INDEX]) * HUNDRED)
					+ getIntegerValue(neighbourIndex[NEIGHBOUR_KPI_INDEX]) + MAX_KPI_OFFSET, templist);

		}
	}

	public void getKpiCsvMapFromDriveDetail(Map<Integer, ArrayList<Double>> kpiMap,
			Map<String, Layer3StatisticsWrapper> kpiCsvMap) {
		if (kpiMap != null && !kpiMap.isEmpty()) {
			for (Entry<Integer, ArrayList<Double>> entry : kpiMap.entrySet()) {
				Layer3StatisticsWrapper layer3statisticswrapper = new Layer3StatisticsWrapper();
				Statistics statistics = new Statistics(entry.getValue());
				layer3statisticswrapper.setMean(statistics.getMean());
				layer3statisticswrapper.setMedian(statistics.getMedian());
				layer3statisticswrapper.setMinimum(statistics.getMin());
				layer3statisticswrapper.setMaximum(statistics.getMax());
				layer3statisticswrapper.setCount(statistics.getSize());
				layer3statisticswrapper.setStandardDeviation(statistics.getStdDev());
				layer3statisticswrapper.setVariance(statistics.getVariance());
				layer3statisticswrapper.setKpiName(getKpiNameByIndex(entry.getKey()));

				kpiCsvMap.put(entry.getKey().toString(), layer3statisticswrapper);
			}
		}
	}

	public void getCommonKpiMapFromDriveDetail(List<String> kpiIndexList, String[] driveDataArrayList,
			Map<Integer, List<String>> commonKpiMap, List<String> neighbourIndexList,
			Map<String, String> neighbourDataMap) {
		Integer count = ForesightConstants.ONE;
		String lat = "";
		String lng = "";

		for (String DriveDataArray : driveDataArrayList) {
			try {
				String[] singlerecord = DriveDataArray.split(DRIVE_COMMA_SEPRATOR, ForesightConstants.MINUS_ONE);

				String lat2 = NVLayer3Utils.getStringFromCsv(singlerecord, ForesightConstants.ZERO);
				String lng2 = NVLayer3Utils.getStringFromCsv(singlerecord, ForesightConstants.ONE);

				if (count == ForesightConstants.ONE) {
					lat = NVLayer3Utils.getStringFromCsv(singlerecord, ForesightConstants.ZERO);
					lng = NVLayer3Utils.getStringFromCsv(singlerecord, ForesightConstants.ONE);
				}
				Double disbwpoint = NVLayer3Utils.getDistanceFromLatLng(getDoubleFromJson(lat), getDoubleFromJson(lng),
						getDoubleFromJson(lat2), getDoubleFromJson(lng2));

				if (count != ForesightConstants.ONE) {
					lat = NVLayer3Utils.getStringFromCsv(singlerecord, ForesightConstants.ZERO);
					lng = NVLayer3Utils.getStringFromCsv(singlerecord, ForesightConstants.ONE);
				}

				String timestamp = NVLayer3Utils.getStringFromCsv(singlerecord, ForesightConstants.TWO);

				ArrayList<String> templist = new ArrayList<>();
				templist.add(count.toString());
				templist.add(NVLayer3Utils.getDateFromTimestampNew(timestamp));
				templist.add(disbwpoint.toString());
				templist.add(lat2);
				templist.add(lng2);

				for (String KpiIndex : kpiIndexList) {
					String temp = NVLayer3Utils.getStringFromCsv(singlerecord, getIntegerValue(KpiIndex));
					templist.add(getStringValueForExcel(temp));
				}

				filterNeighbourDataAndInsertIntoList(neighbourIndexList, neighbourDataMap, timestamp, templist);

				commonKpiMap.put(count, templist);
				count++;

			} catch (Exception e) {
				logger.error("Error in Getting getCommonKpiMapFromDriveDetail {}  ", Utils.getStackTrace(e));
			}

		}
	}

	public void filterNeighbourDataAndInsertIntoList(List<String> neighbourIndexList,
			Map<String, String> neighbourDataMap, String timestamp, List<String> templist) {
		for (String neighbourIn : neighbourIndexList) {
			if (neighbourDataMap.containsKey(timestamp)) {

				String[] neighbourIndex = neighbourIn.split(Symbol.UNDERSCORE_STRING);
				String[] neighbourDataArrayList = removeUnwantedStringFromNeighbourResult(
						neighbourDataMap.get(timestamp));
				if (neighbourDataArrayList.length > (getIntegerValue(neighbourIndex[NEIGHBOUR_NO_INDEX])) - 1) {
					String[] neighbourData = neighbourDataArrayList[((getIntegerValue(
							neighbourIndex[NEIGHBOUR_NO_INDEX])) - 1)].split(DRIVE_COMMA_SEPRATOR,
									ForesightConstants.MINUS_ONE);

					String value = getStringValueForExcel(
							neighbourData[getIntegerValue(neighbourIndex[NEIGHBOUR_KPI_INDEX])]);

					if (value != null) {
						templist.add(value);

					} else {
						templist.add(Symbol.HYPHEN_STRING);
					}

				} else {
					templist.add(Symbol.HYPHEN_STRING);
				}
			} else {
				templist.add(Symbol.HYPHEN_STRING);
			}
		}
	}

	public void putDataIntoKpiMapDriveDetail(List<String> kpiIndexList, String[] driveDataArrayList,
			Map<Integer, ArrayList<Double>> kpiMap) {
		for (String KpiIndex : kpiIndexList) {
			ArrayList<Double> templist = new ArrayList<>();
			for (String DriveDataArray : driveDataArrayList) {
				String[] singlerecord = DriveDataArray.split(DRIVE_COMMA_SEPRATOR, ForesightConstants.MINUS_ONE);

				try {
					Double temp = NVLayer3Utils.getDoubleFromString(singlerecord, getIntegerValue(KpiIndex));
					if (temp != null) {
						templist.add(temp);
					}

				} catch (Exception e) {
					logger.error("Error in Getting getKpiMapFromDriveDetail {}  ", Utils.getStackTrace(e));
				}

			}
			kpiMap.put(getIntegerValue(KpiIndex), templist);
		}
	}

	public XSSFWorkbook createExcelForStatistics(Map<Integer, ArrayList<Double>> kpiMap,
			Map<String, Layer3StatisticsWrapper> kpiCsvMap, Map<Integer, List<String>> commonKpiMap,
			List<String> kpiIndexList, Boolean isInbuilding, List<String> neighbourIndexList) {
		logger.info("Started writing excel");

		XSSFWorkbook workbook = new XSSFWorkbook();

		// Series Formatted Data SHEET
		insertDataForSeriesIntoSheet(commonKpiMap, workbook, kpiIndexList, isInbuilding, neighbourIndexList);
		// Statistic Formatted Data SHEET
		insertDataForStatisticsIntoSheet(kpiCsvMap, workbook);
		// Histogram Formatted Data SHEET
		insertDataForHistogramSheet(kpiMap, workbook, kpiIndexList);
		return workbook;
		// Write the output to a file
	}

	public String writeWorksheetToExcel(XSSFWorkbook workbook, String filename) {
		try {
			String destinationFilePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH);
			String absolutefilenamewithpath = destinationFilePath + Symbol.SLASH_FORWARD_STRING + filename;
			ExcelReportUtils.exportExcel(absolutefilenamewithpath, workbook);
			logger.info("Finished writing excel at{}", filename);
			return QMDLConstant.PATH_PREFIX_JSON + filename + QMDLConstant.PATH_POSTFIX_JSON;
		} catch (IOException | RestException e) {
			logger.error("Error in writeWorksheetToExcel {}  ", Utils.getStackTrace(e));

		}
		return ForesightConstants.FAILURE_JSON;
	}

	public void setColumnSizeToAutoForExcelReport(Sheet sheet, int colstrt) {
		for (int i = NVConstant.ZERO_INT; i < colstrt + NVConstant.THIRTY; i++) {
			sheet.autoSizeColumn(i);

		}
	}

	private XSSFWorkbook insertDataForStatisticsIntoSheet(Map<String, Layer3StatisticsWrapper> kpiCsvMap,
			XSSFWorkbook workbook) {
		int colstrt = ReportConstants.INDEX_ZER0;
		Sheet sheet = workbook.createSheet(STATISTIC_FORMATTED_DATA);
		ExcelReportUtils.getCellStyleForNetVelocityReport(workbook, true, false);
		ExcelReportUtils.getCellStyleForNetVelocityReport(workbook, false, false);

		for (Entry<String, Layer3StatisticsWrapper> entry : kpiCsvMap.entrySet()) {
			Layer3StatisticsWrapper dataList = entry.getValue();
			if (dataList != null) {
				Object[] values = {
						// dataList.getKpiName(),
						dataList.getMean(),
						// dataList.getMode(),
						dataList.getMedian(), dataList.getMaximum(), dataList.getMinimum(), dataList.getCount(),
						dataList.getStandardDeviation(), dataList.getVariance() };
				String[] header1 = { STATISTIC };
				String[] header2 = { dataList.getKpiName() };

				workbook = ExcelReportUtils.writeDataColumnWise(sheet, workbook, colstrt, header1, true,
						ForesightConstants.ZERO);
				workbook = ExcelReportUtils.writeDataColumnWise(sheet, workbook, colstrt++, STATISTICS_SHEET_HEADER,
						false, ForesightConstants.ONE);

				workbook = ExcelReportUtils.writeDataColumnWise(sheet, workbook, colstrt, header2, true,
						ForesightConstants.ZERO);
				workbook = ExcelReportUtils.writeDataColumnWise(sheet, workbook, colstrt++, values, false,
						ForesightConstants.ONE);
				colstrt++;
			}

		}

		setColumnSizeToAutoForExcelReport(sheet, colstrt);
		return workbook;
	}

	private XSSFWorkbook insertDataForSeriesIntoSheet(Map<Integer, List<String>> commonKpiMap, XSSFWorkbook workbook,
			List<String> kpiIndexList, Boolean isInbuilding, List<String> neighbourIndexList) {
		int colstrt = ReportConstants.INDEX_ZER0;
		Sheet sheet = workbook.createSheet(SERIES_FORMATTED_DATA);

		CellStyle styleHeader = ExcelReportUtils.getCellStyleForNetVelocityReport(workbook, true, false);
		CellStyle styleNormal = ExcelReportUtils.getCellStyleForNetVelocityReport(workbook, false, false);

		Row headerRow2 = sheet.getRow(colstrt) != null ? sheet.getRow(colstrt) : sheet.createRow(colstrt);
		StringBuilder header2 = new StringBuilder();
		if (isInbuilding != null && isInbuilding) {
			header2.append(SERIES_SHEET_INBUILDING_HEADER);
		} else {
			header2.append(SERIES_SHEET_HEADER);

		}

		for (String KpiIndex : kpiIndexList) {
			header2.append(getKpiNameByIndex(getIntegerValue(KpiIndex)) + Symbol.UNDERSCORE_STRING);
		}

		for (String neighbourIn : neighbourIndexList) {
			String[] neighbourIndex = neighbourIn.split(Symbol.UNDERSCORE_STRING);
			header2.append("NB" + Symbol.SPACE_STRING + (getIntegerValue(neighbourIndex[ForesightConstants.ZERO]))
					+ Symbol.SPACE_STRING
					+ getNeighbourNameByIndex(getIntegerValue(neighbourIndex[ForesightConstants.ONE]))
					+ Symbol.UNDERSCORE_STRING);
		}

		String[] headerseries = header2.toString().split(Symbol.UNDERSCORE_STRING);
		ExcelReportUtils.prepareColumnValue(headerRow2, colstrt, styleHeader, headerseries);

		colstrt = ForesightConstants.ZERO;
		int rowNum = ForesightConstants.ONE;
		for (Entry<Integer, List<String>> entry : commonKpiMap.entrySet()) {
			List<String> dataList = entry.getValue();
			Object[] data2 = dataList.toArray();

			Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
			ExcelReportUtils.prepareColumnValue(row, colstrt, styleNormal, data2);
			rowNum++;

		}
		setColumnSizeToAutoForExcelReport(sheet, colstrt);
		return workbook;
	}

	private XSSFWorkbook insertDataForHistogramSheet(Map<Integer, ArrayList<Double>> kpiMap, XSSFWorkbook workbook,
			List<String> kpiIndexList) {

		int colstrt = ForesightConstants.ZERO;
		Sheet sheet = workbook.createSheet(HISTOGRAM_FORMATTED_DATA);
		CellStyle styleHeader = ExcelReportUtils.getCellStyleForNetVelocityReport(workbook, true, false);
		CellStyle styleNormal = ExcelReportUtils.getCellStyleForNetVelocityReport(workbook, false, false);
		Row headerRow2 = sheet.getRow(colstrt) != null ? sheet.getRow(colstrt) : sheet.createRow(colstrt);

		if (kpiMap.size() > 0) {
			for (Entry<Integer, ArrayList<Double>> entry : kpiMap.entrySet()) {
				int rowNum = ForesightConstants.ONE;
				Boolean key = entry.getKey() < MAX_KPI_OFFSET ? KPI_HAVING_RANGES_INDEX.contains(entry.getKey())
						: (NEIGHBOUR_KPI_HAVING_RANGES_INDEX.contains((entry.getKey() - MAX_KPI_OFFSET) % HUNDRED));

				Boolean key2 = entry.getKey() < MAX_KPI_OFFSET ? KPI_HAVING_COUNT_INDEX.contains(entry.getKey())
						: (NEIGHBOUR_KPI_HAVING_COUNT_INDEX.contains((entry.getKey() - MAX_KPI_OFFSET) % HUNDRED));

				if (key) {
					String[] headerseries = { RANGE, (getKpiNameByIndex(entry.getKey()) + Symbol.PARENTHESIS_OPEN
							+ COUNT + Symbol.PARENTHESIS_CLOSE) };
					ExcelReportUtils.prepareColumnValue(headerRow2, colstrt, styleHeader, headerseries);
					getPrintRangeWithCount(colstrt, sheet, styleNormal, entry, rowNum, entry.getKey());
					colstrt = colstrt + ForesightConstants.THREE;
				} else if (key2) {
					String[] headerseries = { RANGE, (getKpiNameByIndex(entry.getKey()) + Symbol.PARENTHESIS_OPEN
							+ COUNT + Symbol.PARENTHESIS_CLOSE) };
					ExcelReportUtils.prepareColumnValue(headerRow2, colstrt, styleHeader, headerseries);
					getPrintValueswithCount(colstrt, sheet, styleNormal, entry, rowNum);
					colstrt = colstrt + ForesightConstants.THREE;
				}

			}

			setColumnSizeToAutoForExcelReport(sheet, colstrt);
		}
		return workbook;
	}

	public void getPrintRangeWithCount(int colstrt, Sheet sheet, CellStyle styleNormal,
			Entry<Integer, ArrayList<Double>> entry, int rowNum, Integer kpiIndex) {

		Map<String, Integer> namevsKpiindexMap = new TreeMap<>();
		namevsKpiindexMap.put(getNeighbourNameByIndexForMap(kpiIndex).substring(ReportConstants.INDEX_ZER0,
				getNeighbourNameByIndexForMap(kpiIndex).indexOf(Symbol.PARENTHESIS_OPEN_STRING)), kpiIndex);
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, namevsKpiindexMap);

		if (!kpiList.isEmpty()) {
			List<Double> kpiwisevalues = entry.getValue();
			for (Double value : kpiwisevalues) {
				for (RangeSlab rangeSlab : kpiList.get(0).getRangeSlabs()) {
					if (value > rangeSlab.getLowerLimit() && value < rangeSlab.getUpperLimit()) {

						if (rangeSlab.getCount() != null) {
							rangeSlab.setCount(rangeSlab.getCount() + 1);
						} else {
							rangeSlab.setCount(1);
						}

					}
				}

			}

			for (RangeSlab rangeSlab : kpiList.get(0).getRangeSlabs()) {
				Object[] data2 = { rangeSlab.getLowerLimit() + " to " + rangeSlab.getUpperLimit(),
						rangeSlab.getCount() };

				Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
				ExcelReportUtils.prepareColumnValue(row, colstrt, styleNormal, data2);
				rowNum++;
			}
		}
	}

	public void getPrintValueswithCount(int colstrt, Sheet sheet, CellStyle styleNormal,
			Entry<Integer, ArrayList<Double>> entry, int rowNum) {

		Map<Object, Long> rangeMap = entry.getValue().stream()
				.collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		for (Entry<Object, Long> entry2 : rangeMap.entrySet()) {

			Object[] data2 = { entry2.getKey(), entry2.getValue() };

			Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
			ExcelReportUtils.prepareColumnValue(row, colstrt, styleNormal, data2);
			rowNum++;

		}

	}

	public String getKpiNameByIndex(Integer kpiIndex) {
		String[] header = null;
		if (kpiIndex < MAX_KPI_OFFSET) {
			header = getHeaderForExcelsReport();
			return getStringFromCsv(header, kpiIndex);
		} else {
			header = NVLayer3Constants.HEADER_NEIGHBOUR_DETAIL.split(Symbol.UNDERSCORE_STRING);
			return "NB" + Symbol.SPACE_STRING + ((kpiIndex - MAX_KPI_OFFSET) / HUNDRED) + Symbol.SPACE_STRING
					+ getStringFromCsv(header, (kpiIndex - MAX_KPI_OFFSET) % HUNDRED);
		}
	}

	public String[] getHeaderForExcelsReport() {
		String[] header = null;
		String csvHeaders = ConfigUtils.getString(NVConfigUtil.WO_REPORT_HEADERS);
		if (csvHeaders != null) {
			csvHeaders = csvHeaders.replace("Date & Time_", "");
			csvHeaders = csvHeaders.replace("Handover Intra-Success", "Handover Intra-Success_RowKey Prefix");
			header = csvHeaders.split(Symbol.UNDERSCORE_STRING);
		}
		return header;
	}

	public String getNeighbourNameByIndex(Integer kpiIndex) {
		String[] header = NVLayer3Constants.HEADER_NEIGHBOUR_DETAIL.split(Symbol.UNDERSCORE_STRING);
		return getStringFromCsv(header, kpiIndex);
	}

	public String getNeighbourNameByIndexForMap(Integer kpiIndex) {
		if (kpiIndex < MAX_KPI_OFFSET) {
			String[] header = getHeaderForExcelsReport();
			return getStringFromCsv(header, kpiIndex);
		} else {
			String[] header = NVLayer3Constants.HEADER_NEIGHBOUR_DETAIL.split(Symbol.UNDERSCORE_STRING);
			return getStringFromCsv(header, (kpiIndex - MAX_KPI_OFFSET) % HUNDRED);
		}
	}

	public String getStringValueForExcel(String value) {
		if (value == null) {
			value = "-";
		}
		return value;
	}

	private boolean isBenchmarkRecord(WOFileDetail woFile) {
		return woFile.getWoRecipeMapping().getGenericWorkorder().getTemplateType().name()
				.equals(TemplateType.NV_BENCHMARK.name())
				|| woFile.getWoRecipeMapping().getGenericWorkorder().getTemplateType().name()
						.equals(TemplateType.NV_IB_BENCHMARK.name());
	}

	private boolean isInBuildingBenchmarkRecord(WOFileDetail woFile) {
		return woFile.getWoRecipeMapping().getGenericWorkorder().getTemplateType().name()
				.equals(TemplateType.NV_IB_BENCHMARK.name());
	}

	private void putRecipeMapForDriveBenchmark(Map<String, Map<String, String>> recipeMapping, WOFileDetail woFile,
			String recipeName) {
		if (isInBuildingBenchmarkRecord(woFile)) {
			Integer unitId = getValueFromGWOMap(woFile.getWoRecipeMapping(), QMDLConstant.IN_BUILDING_MAP_KEY);
			recipeName = recipeName + Symbol.UNDERSCORE_STRING + unitId;
		}
		if (recipeMapping.containsKey(recipeName)) {
			Map<String, String> recipeOperatorMapping = recipeMapping.get(recipeName);
			recipeOperatorMapping.put(woFile.getWoRecipeMapping().getId().toString(),
					NVWorkorderUtils.getOperatorNameFromFileName(woFile.getFileName()));
			recipeMapping.replace(recipeName, recipeOperatorMapping);
		} else {
			Map<String, String> recipeOperatorMapping = new HashMap<>();
			recipeOperatorMapping.put(woFile.getWoRecipeMapping().getId().toString(),
					NVWorkorderUtils.getOperatorNameFromFileName(woFile.getFileName()));
			recipeMapping.put(recipeName, recipeOperatorMapping);
		}
	}

	@Override
	public Map<String, List<String>> getDriveRecipeDetail(Integer workrorderId) {
		try {
			List<WOFileDetail> woFileList = woFileDetailService.getFileDetailByWorkOrderId(workrorderId);
			if (woFileList != null && woFileList.size() > ReportConstants.INDEX_ZER0) {
				return getMappingDataFromWoFile(woFileList);
			}
		} catch (RestException e) {
			logger.error("Getting Exception in file information {}   {} ", workrorderId, Utils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public Map<String, List<String>> getDriveRecipeDetailByRecipeId(Integer recipeId) {
		try {
			List<WOFileDetail> woFileList = woFileDetailService.getFileDetailByRecipeMappingId(recipeId);
			if (woFileList != null && woFileList.size() > ReportConstants.INDEX_ZER0) {
				return getMappingDataFromWoFile(woFileList);
			}
		} catch (RestException e) {
			logger.error("Getting Exception in file information {}   {} ", recipeId, Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String getLogFileForRecipe(String recipeId, String fileId) {
		List<WOFileDetail> woFileDetailList = new ArrayList<>();
		if (recipeId != null && fileId==null) {
			woFileDetailList = woFileDetailDao.findFileByRecipeMappingId(Integer.parseInt(recipeId)).stream().filter(f ->(f.getIsDeleted() == null || !(f.getIsDeleted()))).collect(Collectors.toList());
			TemplateType templateType = woFileDetailList.get(ForesightConstants.ZERO).getWoRecipeMapping()
					.getGenericWorkorder().getTemplateType();
			Integer genericworkorderId = woFileDetailList.get(ForesightConstants.ZERO).getWoRecipeMapping()
					.getGenericWorkorder().getId();
			String workorderId = woFileDetailList.get(ForesightConstants.ZERO).getWoRecipeMapping()
			.getGenericWorkorder().getWorkorderId();
			if (templateType.equals(GenericWorkorder.TemplateType.SCANNING_RECEIVER)) {
				String localFilePath = copyScannerZipFilesToLocal(recipeId, woFileDetailList, genericworkorderId);
				String zipFile = null;
				try {
					
					zipFile = createScannerZipFileFromLocalFolder(localFilePath);					
					return zipFile;
					
				} catch (Exception e) {
					logger.error("Exception while download scanner zip file {}",ExceptionUtils.getStackTrace(e));
				}				
			} 
		}
		if (fileId != null && !fileId.isEmpty()) {
			WOFileDetail woFileDetail = woFileDetailDao.findByPk(Integer.valueOf(fileId));
			if (woFileDetail != null) {
				woFileDetailList.add(woFileDetail);
			}
		} else if (recipeId != null && !recipeId.isEmpty()) {
			woFileDetailList = woFileDetailDao.findFileByRecipeMappingId(Integer.parseInt(recipeId));
		}

		if (woFileDetailList != null && !woFileDetailList.isEmpty()) {
			woFileDetailList = woFileDetailList.stream()
					.filter(fileDetail -> fileDetail.getProcessedLogFilePath() != null
					&& !fileDetail.getFileName().contains("floor_plan"))
					.collect(Collectors.toList());
		}
		if (woFileDetailList != null && !woFileDetailList.isEmpty()) {
			logger.info("woFileList {}", woFileDetailList.size());

			return getZipResponseForMultipleFiles(
					woFileDetailList.get(woFileDetailList.size()-QMDLConstant.ONE).getProcessedLogFilePath(),
					woFileDetailList.get(woFileDetailList.size()-QMDLConstant.ONE).getFileName());
		}
		return (NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
	}

	private Response.ResponseBuilder createScannerZipFileBuilder(String recipeId, Integer genericworkorderId,
			String zipFile, String workorderId) throws IOException {
		Response.ResponseBuilder builder = Response.status(NVConstant.STATUS_CODE_200);

		logger.info("Getting final file path : {}",
				 zipFile);
		String downloadFileName  = String.valueOf(workorderId + ForesightConstants.UNDERSCORE + genericworkorderId + ForesightConstants.UNDERSCORE + recipeId) +".zip";
		builder = builder.entity(Files.readAllBytes(
				new File(zipFile).toPath()))
				.header(ForesightConstants.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
				.header(ForesightConstants.CONTENT_DISPOSITION,
						NVLayer3Constants.ATTACHMENT_FILE + downloadFileName + "\"");
		return builder;
	}

	private String createScannerZipFileFromLocalFolder(String localFilePath) throws Exception {
		String zipFile;
		zipFile = localFilePath +Symbol.DOT_STRING+QMDLConstant.ZIP_FILE_TYPE;
		zipFolder(Paths.get(localFilePath), Paths.get(zipFile));
		return zipFile;
	}

	private String copyScannerZipFilesToLocal(String recipeId, List<WOFileDetail> woFileDetailList,
			Integer genericworkorderId) {
		String filepath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH);
		logger.info("filepath is {}", filepath);
		String localFilePath = filepath + String.valueOf(genericworkorderId + ForesightConstants.UNDERSCORE + recipeId);
		for (WOFileDetail wf : woFileDetailList) {
		
			File file = new File(localFilePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			
			nvLayer3HdfsDao.copyFileFromHdfsToLocalPath(wf.getFilePath(), localFilePath + ForesightConstants.FORWARD_SLASH,
					 wf.getFileName());
			 new NVLayer3DashboardServiceImpl().removeCRCFile(localFilePath);
		}
		return localFilePath;
	}
	
	 private static void  zipFolder(Path sourceFolderPath, Path zipPath) throws Exception {
	        try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
				Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
						Files.copy(file, zos);
						//
						zos.closeEntry();
						return FileVisitResult.CONTINUE;
					}
				});
			}
	    }

	private String getZipResponseForMultipleFiles(String processedFilePath, String fileName) {
		
		if(fileName.contains(Symbol.PLUS_STRING)) {
			fileName=fileName.replace(Symbol.PLUS_STRING, Symbol.HYPHEN_STRING);
		}
		
		logger.info("Going to return zip with files from hdfs path: {}, fileName: {}", processedFilePath, fileName);
		String filepath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH);
		logger.info("filepath is {}", filepath);
		String localFilePath = String.valueOf(new Date().getTime());
		try {
			List<FileStatus> fileStatusList = nvLayer3HdfsDao.getAllFilesFromDirectory(processedFilePath);
			for (FileStatus filestatus : fileStatusList) {
				if (filestatus.getPath().getName().contains(QMDLConstant.ZIP_FILE_TYPE)) {
					logger.info("Getting zip inside file Path  {}", filestatus.getPath());

					File file = new File(filepath + localFilePath);
					if (!file.exists()) {
						file.mkdirs();
					}

					nvLayer3HdfsDao.copyFileFromHdfsToLocalPath(filestatus.getPath().toString(),
							filepath + localFilePath + Symbol.SLASH_FORWARD_STRING, fileName);
					
					logger.info("Getting final file path : {}",
							filepath + localFilePath + Symbol.SLASH_FORWARD_STRING + fileName);


					return (filepath + localFilePath + Symbol.SLASH_FORWARD_STRING + fileName);

				}
			}

			return getlogFileMakingZip(processedFilePath, fileName, filepath,localFilePath);

		} catch (Exception e) {
			logger.error("Exception inside method getZipResponseForMultipleFiles {} ", Utils.getStackTrace(e));
		}

		return NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON	;
	}

	public String getlogFileMakingZip(String processedFilePath, String fileName, String filepath,
			String localFilePath) throws IOException {
		nvLayer3HdfsDao.copyFileFromHdfsToLocalPath(processedFilePath, filepath, localFilePath);

		String zipFilePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH) + localFilePath
				+ Symbol.SLASH_FORWARD_STRING + fileName;
		logger.info("zipFilePath {}", zipFilePath);
		removeCRCFile(filepath + localFilePath);
		ZipUtils.zip(new File(filepath + localFilePath).listFiles(), zipFilePath);		
		nvLayer3HdfsDao.copyFileFromLocalToHDFS(zipFilePath, processedFilePath);		
		return zipFilePath;	
	}

	private void removeCRCFile(String localFilePath) {
		try {
			File files = new File(localFilePath);
			for (File file : files.listFiles()) {
				if (file.getName().contains(NVConstant.CRC_EXTENSION)) {
					boolean delete = file.delete();
					logger.info("delete {}",delete);
				}
			}
		} catch (Exception e) {
			logger.error("Exception to removeCRCFile {} ", Utils.getStackTrace(e));
		}

	}

	private String createProcessedDirectoryIfNotExists() {
		String filesDirectory = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH)
				+ LAYER3_PROCESSED_LOG_FILE_DIRECTORY_NAME + Symbol.SLASH_FORWARD_STRING;
		File file = new File(filesDirectory);
		if (!file.exists()) {
			file.mkdirs();
		}
		return filesDirectory;
	}

	@Override
	public List<WOFileDetail> getFileDetailsListForWorkOrderId(Integer workorderId) {
		return woFileDetailService.getFileDetailByWorkOrderId(workorderId);
	}

	@Override
	public Map<String, List<String>> getDriveRecipeDetail(List<Integer> workrorderIdList) {
		try {
			List<WOFileDetail> woFileList = woFileDetailService.getFileDetailByWorkOrderIdList(workrorderIdList);
			if (woFileList != null && woFileList.size() > ReportConstants.INDEX_ZER0) {
				return getMappingDataFromWoFile(woFileList);
			}
		} catch (RestException e) {
			logger.error("Exception occured inside method getDriveRecipeDetail {} , {} ", workrorderIdList,
					Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<Get> getListForKpiStats(List<Integer> workorderIds, List<String> recipeId, List<String> operatorList,
			String kpi) {
		List<Get> getList = new ArrayList<>();
		Set<String> operatorset = new HashSet<>(operatorList);
		for (Integer workorderId : workorderIds) {
			for (String receipeId : recipeId) {
				for (String operator : operatorset) {
					Get get = new Get(Bytes.toBytes(getRowKeyForLayer3(workorderId, operator, receipeId) + kpi));
					get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.RANGE_STATS.getBytes());
					getList.add(get);
				}
			}
		}
		return getList;
	}

	@Override
	public String getKpiStatsRecipeDataForReportForWoList(List<Integer> workorderIds, String kpi, List<String> recipeId,
			List<String> operatorList) {
		List<Get> getList = getListForKpiStats(workorderIds, recipeId, operatorList, kpi);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_KPI_TABLE);
		try {
			List<HBaseResult> hbaseResults = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			String json = getFinalKpiStatsFromResultForReport(hbaseResults, kpi);
			return getJsonFromDetailMsg(json);
		} catch (IOException e) {
			logger.error("Error in Getting getKpiStatsRecipeDataForReport Result {}  ", e.getMessage());
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.debug("Error in Getting getKpiStatsRecipeDataForReport Result {}  ", e.getMessage());
			return QMDLConstant.INVALID_ARGUMENT;
		}
	}

	@Override
	public String getDriveDetailReceipeWiseforWoIds(List<Integer> workorderIds, List<String> recipeId,
			List<String> operatorList) {
		logger.info("Getting workorderIds {}  recipeId {}  operatorList {}", workorderIds, recipeId, operatorList);

		List<String> columnList = getColumnListForDriveDetail();
		List<Get> getList = getListForSummary(workorderIds, recipeId, operatorList, columnList);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_DATA_TABLE);
		try {
			List<HBaseResult> hbaseResults = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			String json = getFinalJsonFromListResult(hbaseResults);
			return getFinalJson(json);
		} catch (IOException e) {
			logger.error("IOException occured inside method getDriveDetailReceipeWise , Result {}  ", e.getMessage());
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("Error in Getting getDriveDetailReceipeWise Result {}  ", e.getMessage());
			return QMDLConstant.INVALID_ARGUMENT;
		}
	}

	private List<Get> getListForSummary(List<Integer> workorderIds, List<String> recipeId, List<String> operatorList,
			List<String> columnList) {
		List<Get> getList = new ArrayList<>();
		Set<String> operatorSet = new HashSet<>(operatorList);
		Set<String> recipeIdSet = new HashSet<>(recipeId);

		for (Integer workorderId : workorderIds) {
			for (String receipeId : recipeIdSet) {
				for (String operator : operatorSet) {
					Get get = new Get(Bytes.toBytes(getRowKeyForLayer3(workorderId, operator, receipeId)));
					addColumnsToGet(columnList, get);
					getList.add(get);
				}
			}
		}
		return getList;
	}

	@Override
	public String getDriveSummaryReceipeWiseForWoIds(List<Integer> workorderIds, List<String> recipeId,
			List<String> operatorList) {
		List<String> columnList = getSummaryColumnList();
		List<Get> getList = getListForSummary(workorderIds, recipeId, operatorList, columnList);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_DATA_TABLE);
		try {
			List<HBaseResult> hbaseResultList = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			String summaryJson = getFinalSummaryFromResult(hbaseResultList);
			return getFinalJson(summaryJson);
		} catch (IOException e) {
			logger.error("Error in Getting   getDriveSummaryReceipeWise  Result {}  ", e.getMessage());
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("Error in Getting getDriveSummaryReceipeWise Result {}  ", e.getMessage());
			return QMDLConstant.INVALID_ARGUMENT;
		}
	}

	@Override
	public String getDriveSummaryReceipeWiseOld(Integer workorderId, List<String> recipeId, List<String> operatorList) {
		List<String> columnList = getSummaryColumnList();
		List<Get> getList = getListForSummary(workorderId, recipeId, operatorList, columnList);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_DATA_TABLE);
		try {
			List<HBaseResult> hbaseResultList = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			String summaryJson = getFinalSummaryFromResult(hbaseResultList);
			return getFinalJson(summaryJson);
		} catch (IOException e) {
			logger.error("Error in Getting   getDriveSummaryReceipeWise  Result {}  ", e.getMessage());
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (BusinessException e) {
			logger.warn("Error in Getting getDriveSummaryReceipeWise Result {}  ", e.getMessage());
			return QMDLConstant.INVALID_ARGUMENT;
		}
	}

	private void getCommonKpiMapForInbuilding(List<String> kpiIndexList, String[] driveDataArrayList,
			Map<Integer, List<String>> commonKpiMap, List<String> neighbourIndexList,
			Map<String, String> neighbourDataMap) {
		Integer count = ForesightConstants.ONE;
		for (String DriveDataArray : driveDataArrayList) {
			try {
				String[] singlerecord = DriveDataArray.split(DRIVE_COMMA_SEPRATOR, ForesightConstants.MINUS_ONE);

				String timestamp = NVLayer3Utils.getStringFromCsv(singlerecord, ForesightConstants.TWO);

				ArrayList<String> templist = new ArrayList<>();
				templist.add(count.toString());
				templist.add(NVLayer3Utils.getDateFromTimestampNew(timestamp));
				for (String KpiIndex : kpiIndexList) {
					String temp = NVLayer3Utils.getStringFromCsv(singlerecord, getIntegerValue(KpiIndex));
					templist.add(getStringValueForExcel(temp));
				}

				filterNeighbourDataAndInsertIntoList(neighbourIndexList, neighbourDataMap, timestamp, templist);

				commonKpiMap.put(count, templist);
				count++;

			} catch (Exception e) {
				logger.error("Error in Getting getCommonKpiMapFromDriveDetail {}  ", Utils.getStackTrace(e));
			}

		}
	}

	@Override
	//@Scheduled(cron = "0 0 20 * * *")
	//@Transactional(readOnly=true)
	public Map<Integer, NNS<LatLng>> getPCIWiseNNSMapForRanSite() {
		if (Utils.isTestBuild()) {
			return nnsMap;
		}
		Map<Integer, List<LatLng>> pciWiseLocationList = new HashMap<>();
		logger.info("Going to get pci wise ran site NNS map");
		try {
			List<RANDetail> list = siteDetailService.findAllRANDetail();
			List<LatLng> locationList = null;

			for (RANDetail ranDetail : list) {
				extractPCILocationFromRANDetail(pciWiseLocationList, ranDetail);
			}

			for (Entry<Integer, List<LatLng>> entry : pciWiseLocationList.entrySet()) {
				nnsMap.put(entry.getKey(), new NNS(entry.getValue()));
			}
		} catch (Exception e) {
			logger.error("Error while getting pci wise ran detail");
		}
		logger.info("pci wise NNS map size: {}", nnsMap.size());
		return nnsMap;
	}

	private void extractPCILocationFromRANDetail(Map<Integer, List<LatLng>> pciWiseLocationList, RANDetail ranDetail) {
		try {
			List<LatLng> locationList;
			locationList = pciWiseLocationList.get(ranDetail.getPci());
			if (locationList == null) {
				locationList = new ArrayList<>();
				if (ranDetail.getNetworkElement() != null) {
					locationList.add(new LatLng(ranDetail.getNetworkElement().getLatitude(),
							ranDetail.getNetworkElement().getLongitude()));
				}
				pciWiseLocationList.put(ranDetail.getPci(), locationList);
			} else {
				if (ranDetail.getNetworkElement() != null) {
					locationList.add(new LatLng(ranDetail.getNetworkElement().getLatitude(),
							ranDetail.getNetworkElement().getLongitude()));
				}
				pciWiseLocationList.put(ranDetail.getPci(), locationList);
			}
		} catch (Exception e) {
			logger.warn("Error while fetching pci detail: {}", e.getMessage());
		}
	}

	@Override
	public LatLng getNearestCellByPCILatLong(Integer pci, Double latitude, Double longitude) {
		LatLng nearestLocation = new LatLng();
		logger.debug("going to get nearest location for pci: {}, latitude: {}, longitude: {}", pci, latitude,
				longitude);
		try {
			if (nnsMap.size() == ForesightConstants.ZERO) {
				this.getPCIWiseNNSMapForRanSite();
			}
			NNS<LatLng> nnsForPCI = nnsMap.get(pci);
			nearestLocation = nnsForPCI.getNearestSingleLocation(new LatLng(latitude, longitude));
		} catch (Exception e) {
			logger.error("Error while getting nearest location for pci: {}, latitude: {}, longitude: {}", pci, latitude,
					longitude);
		}
		return nearestLocation;
	}

	@Override
	public Response clearPCIWiseNNSMapForMacroSite() {
		logger.info("Going to reset / clear PCI wise NNS map for macro site");
		nnsMap = new HashMap<>();
		return Response.ok(SUCCESS_JSON).build();
	}

	@Override
	public Response checkPCIWiseNNSMapSizeForMacroSite() {
		logger.info(" Going to check PCI wise NNS map size for macro site  ");
		try {
			logger.info("PCI wise NNS map size for macro site is: {}", nnsMap.size());
			return Response.ok(nnsMap.size()).build();
		} catch (Exception e) {
			logger.error("Error while checking pci wise NNS map size: {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	public List<String> getListOfRowKeyPrefixList(Integer workorderId, List<String> recipeId,
			List<String> operatorList) {
		List<String> getRowKeyList = new ArrayList<>();
		Set<String> operatorSet = new HashSet<>(operatorList);
		Set<String> recipeIdSet = new HashSet<>(recipeId);

		for (String receipeId : recipeIdSet) {
			for (String operator : operatorSet) {
				getRowKeyList.add(getRowKeyForLayer3(workorderId, operator, receipeId));
			}
		}
		return getRowKeyList;
	}

	public String getDataFromHBaseRowkeyWise(List<String> hbaseColumns, String tableName, List<String> rowKeyPrefixList)
			throws IOException {
		Scan scan = new Scan();
		FilterList filter = new FilterList(FilterList.Operator.MUST_PASS_ONE);
		for (String rowKeyPrefix : rowKeyPrefixList) {
			filter.addFilter(new PrefixFilter((rowKeyPrefix).getBytes()));
		}
		scan.setFilter(filter);
		for (String col : hbaseColumns) {
			scan.addColumn((Bytes.toBytes(ForesightConstants.HBASE_COLUMN_FAMILY)), Bytes.toBytes(col));
		}
		scan.addColumn((Bytes.toBytes(ForesightConstants.HBASE_COLUMN_FAMILY)), Bytes.toBytes("latlngTimestamp"));

		List<HBaseResult> resultList = genericMapServiceDao.getResultListForScan(scan, tableName,
				ForesightConstants.HBASE_COLUMN_FAMILY);
		return convertResultToColumnDataRowkeyWise(resultList, hbaseColumns);
	}

	public String convertResultToColumnDataRowkeyWise(List<HBaseResult> results, List<String> columnList) {
		SortedMap<Long, String> sortedataMap = new TreeMap<>();
		for (HBaseResult result : results) {
			List<String> resultData = new ArrayList<>();
			for (String clms : columnList) {
				String value = null;
				if (NVLayer3Constants.NEIGHBOUR_COLUMN.equalsIgnoreCase(clms)) {
					value = result.getString(clms);
					if (value == null || NVLayer3Constants.EMPTY_ARRAY.equals(value)) {
						value = Symbol.EMPTY_STRING;
					} else {
						value = value.replaceAll(NEIGHBOUR_ARRAY_OPENING, NEIGHBOUR_ARRAY_OPENING_CLOSING_REPLACEMENT)
								.replaceAll(NEIGHBOUR_ARRAY_CLOSING, NEIGHBOUR_ARRAY_OPENING_CLOSING_REPLACEMENT)
								.replaceAll(NEIGHBOUR_ARRAY_SEPERATOR, NEIGHBOUR_ARRAY_SEPERATOR_REPLACEMENT);
					}
				} else {
					value = result.getString(clms);
					if (value == null) {
						value = Symbol.EMPTY_STRING;
					}
				}
				resultData.add(value);
			}

			sortedataMap.put(Long.parseLong(result.getString("latlngTimestamp")),
					resultData.stream().collect(Collectors.joining(Symbol.COMMA_STRING)));

		}
		StringBuilder finalResult = new StringBuilder();
		finalResult.append(Symbol.BRACKET_OPEN_STRING);
		for (SortedMap.Entry<Long, String> entry : sortedataMap.entrySet()) {
			finalResult.append(Symbol.BRACKET_OPEN_STRING);
			finalResult.append(entry.getValue());
			finalResult.append(Symbol.BRACKET_CLOSE_STRING);
			finalResult.append(Symbol.COMMA_STRING);
		}

		finalResult.append(Symbol.BRACKET_CLOSE_STRING);
		finalResult.deleteCharAt(finalResult.length() - TWO);
		return finalResult.toString();
	}

	@Override
	public Map<String, List<RecipeWrapper>> getRecipeMappingWrapperByWOId(Integer workrorderId) {
		Map<String, List<RecipeWrapper>> map = new HashMap();
		List<WORecipeMapping> woRecipeMappings = woRecipeMappingdao.getWORecipeByGWOId(workrorderId);
		for (WORecipeMapping recipeMapping : woRecipeMappings) {
			
			List<RecipeWrapper> recipeList = new ArrayList<>();

			String recipeName = recipeMapping.getRecipe().getName();
			if (recipeMapping.getStatus().equals(Status.COMPLETED)) {
				RecipeWrapper recipeWrapper = new RecipeWrapper();
				setScannerMeta(recipeMapping, recipeWrapper);
				List<WOFileDetail> fileDetails = woFileDetailDao
						.getFileDetailByWORecipeMappingId(recipeMapping.getId());
				if (fileDetails != null) {

					recipeName = getRecipeNameForWorkorder(recipeWrapper, fileDetails.get(0), map);
					recipeWrapper.setDisplayOperatorName(recipeMapping.getOperator());

					Optional<WOFileDetail> optional=fileDetails.stream().filter(c -> !c.getFileName().contains(WO_FILE_NAME_PREFIX_FLOOR_PLAN))
							.findFirst();
					if(optional.isPresent()) {
					recipeWrapper.setOperator(NVWorkorderUtils.getOperatorNameFromFileName(optional.get().getFileName()));
					}
				}
				recipeWrapper.setRecipeId(Integer.toString(recipeMapping.getId()));
				recipeWrapper.setCategory(recipeMapping.getRecipe().getCategory());
				recipeList.add(recipeWrapper);

				if (map.containsKey(recipeName)) {
					map.get(recipeName).addAll(recipeList);
				} else {
					map.put(recipeName, recipeList);
				}

			}

		}
		return map;
	}

	private void setScannerMeta(WORecipeMapping recipeMapping, RecipeWrapper recipeWrapper) {
		if(recipeMapping.getGenericWorkorder().getTemplateType().equals(GenericWorkorder.TemplateType.SCANNING_RECEIVER)) {
			String channelSet = "channelSet";
			String channelSetString = recipeMapping.getGenericWorkorder().getGwoMeta().get(channelSet);
			recipeWrapper.setChannelSet(channelSetString);
			
			String signalRecipe = "SIGNAL";
			if (recipeMapping.getRecipe().getScriptJson().split(ForesightConstants.COLON)[1]
					.contains(signalRecipe)) {
				recipeWrapper.setType(signalRecipe);
				
			} else {
				String rssiRecipe = "RSSI";
				if (recipeMapping.getRecipe().getScriptJson().split(ForesightConstants.COLON)[1]
						.contains(rssiRecipe)) {
					recipeWrapper.setType(rssiRecipe);
				} else {
					String epsRecipe = "EPS";
					if (recipeMapping.getRecipe().getScriptJson().split(ForesightConstants.COLON)[1]
							.contains(epsRecipe)) {
						recipeWrapper.setType(epsRecipe);
					}
				}
			}
			
			
		}
	}

	public String getRecipeNameForWorkorder(RecipeWrapper recipeWrapper, WOFileDetail woFile,
			Map<String, List<RecipeWrapper>> map) {
		String recipeName = woFile.getWoRecipeMapping().getRecipe().getName();
		if (NVLayer3Utils.isInBuidlingRecord(woFile)) {
			Integer unitid = getValueFromGWOMap(woFile.getWoRecipeMapping(), QMDLConstant.IN_BUILDING_MAP_KEY);
			if (unitid != null) {
				recipeWrapper.setUnitId(unitid);
				return recipeName.concat(Symbol.UNDERSCORE_STRING +woFile.getWoRecipeMapping().getId()+Symbol.UNDERSCORE_STRING+unitid);
			} else {
				if (map.containsKey(recipeName)) {
					return NVLayer3Utils.getPaddedKeyForLayer3(map, recipeName + Symbol.UNDERSCORE,
							QMDLConstant.PADDING_VALUE_STR);
				}
			}
		} else if (isSSVTRecord(woFile)) {
			Integer pci = getValueFromGWOMap(woFile.getWoRecipeMapping(), QMDLConstant.SSVT_MAP_KEY);
			if (pci != null) {
				return recipeName.concat(Symbol.UNDERSCORE_STRING + pci);
			} else {
				if (map.containsKey(recipeName)) {
					return NVLayer3Utils.getPaddedKeyForLayer3(map, recipeName + Symbol.UNDERSCORE,
							QMDLConstant.PADDING_VALUE_STR);
				}
			}
		} else if (isBenchmarkRecord(woFile)) {
			if (isInBuildingBenchmarkRecord(woFile)) {

				Integer unitId = getValueFromGWOMap(woFile.getWoRecipeMapping(), QMDLConstant.IN_BUILDING_MAP_KEY);
				recipeWrapper.setUnitId(unitId);

				return recipeName.concat(Symbol.UNDERSCORE_STRING + unitId);
			}
			return recipeName;
		}
		
		else if (isScannerRecord(woFile)) {
			recipeName = recipeName.replaceAll(ForesightConstants.SPACE, ForesightConstants.UNDERSCORE);
			return recipeName;
			
		} 
		
		else {
			if (map.containsKey(recipeName)) {
				return NVLayer3Utils.getPaddedKeyForLayer3(map, recipeName + Symbol.UNDERSCORE,
						QMDLConstant.PADDING_VALUE_STR);
			}
		}
		return recipeName;
	}

	@Override
	@Transactional
	public Response updateDLFLogFileStatusToReprocess(Integer workorderId, Integer recipeId) {
		try {
			int processedCount = 0;
			List<WOFileDetail> woFileDetails = new ArrayList<>();
			List<WORecipeMapping> woRecipeMappingList = new ArrayList<>();


			if (recipeId != null) {
				woFileDetails = woFileDetailDao.getCompletedFileByRecipeMappingId(recipeId);
				woRecipeMappingList=woRecipeMappingdao.getNonDeletedWOByRecipeId(recipeId);
				logger.info("updateDLFLogFileStatusToReprocess : " + woFileDetails.size());
			} else {
				woFileDetails = woFileDetailDao.getFileDetailListByWorkOrderId(workorderId);
				woRecipeMappingList=woRecipeMappingdao.getWoRecipeMappingByWorkOrderId(workorderId);
			}

			logger.info("updateDLFLogFileStatusToReprocess :woFileDetails size " + woFileDetails.size());
			logger.info("updateDLFLogFileStatusToReprocess : woRecipeMapping size" + woRecipeMappingList.size());


			if (woFileDetails != null && woRecipeMappingList != null) {
				processedCount = updateProcessedStatusInWOFileDetail(processedCount, woFileDetails);
				logger.info("successfully update status in WOFileDetail table");
				processedCount = updateFlowProcessedStatusInWORecipeMapping(processedCount, woRecipeMappingList);
				logger.info("successfully update status in WORecipeMapping table");
				nvHbaseService.deletePreviousDataForLayer3(workorderId,recipeId);
				logger.info("successfully delete data from hbase table");
				updateFlowStatusByWOId(workorderId,false);
				logger.info("successfully update status in GenericWorkorder table");
				List<AnalyticsRepository> analyticsRepositories = woFileDetailDao.getAnalyticsForWOAndRecipeId(workorderId,
						recipeId);
				if (analyticsRepositories != null) {
					for (AnalyticsRepository analyticsRepository : analyticsRepositories) {
						if (!String.valueOf(analyticsRepository.getProgress())
								.equalsIgnoreCase(String.valueOf(AnalyticsRepository.progress.In_Progress))
							&&	!analyticsRepository.getDeleted()) {
							analyticsRepository.setProgress(AnalyticsRepository.progress.In_Progress);
							analyticsRepositoryDao.update(analyticsRepository);
						}
					}
					logger.info("successfully update status in AnalyticsRepository table");

				}
			}
			if (processedCount > 0) {
				return Response.ok(SUCCESS_JSON).build();
			} else {
				return Response.ok(INPROGRESS_FILE_JSON).build();
			}

		} catch (Exception e) {
			logger.error("error while updateDLFLogFileStatusToReprocess for WOId {} AND RecipeId {} Exception {}: ", workorderId,recipeId, ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	private int updateFlowProcessedStatusInWORecipeMapping(int processedCount,
			List<WORecipeMapping> woRecipeMappingList) {
		for (WORecipeMapping woRecipeMapping : woRecipeMappingList) {
			if(woRecipeMapping.getFlowProcessStatus() != null && woRecipeMapping.getFlowProcessStatus()) {
				woRecipeMapping.setFlowProcessStatus(false);
				woRecipeMapping.setProcessStartTime(null);
				woRecipeMapping.setProcessEndTime(null);				
				++processedCount;
				woRecipeMappingdao.update(woRecipeMapping);	
			}
		}
		return processedCount;
	}

	private int updateProcessedStatusInWOFileDetail(int processedCount, List<WOFileDetail> woFileDetails) {
		for (WOFileDetail woFileDetail : woFileDetails) {
			if (woFileDetail.getIsProcessed() != null && woFileDetail.getIsProcessed()) {
				woFileDetail.setIsProcessed(false);
				++processedCount;
				woFileDetailDao.update(woFileDetail);
			}
		}
		return processedCount;
	}

	private void updateFlowStatusByWOId(Integer workorderId,Boolean flowStatus) {
		logger.info("Going to update flow status for workorderId {} status {}",workorderId,flowStatus);
		GenericWorkorder genericWorkorder = genericWorkorderDao.findByPk(workorderId);
		genericWorkorder.setFlowStatus(flowStatus);
		genericWorkorderDao.update(genericWorkorder);
	}
	
}
