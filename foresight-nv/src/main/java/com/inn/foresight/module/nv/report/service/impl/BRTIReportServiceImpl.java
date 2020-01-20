package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.utils.NetworkDataFormats;
import com.inn.foresight.module.nv.report.service.IBRTIExcelReportService;
import com.inn.foresight.module.nv.report.service.IBRTIReportService;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.brti.BRTIExcelReport;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ExcelReportUtils;
import com.inn.foresight.module.nv.report.utils.IBWifiReportUtils;
import com.inn.foresight.module.nv.report.utils.LegendUtil;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.BRTIExcelReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.BRTIReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.BRTIReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.BRTIStaionaryDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.BRTIYearlyReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphWrapper;
import com.inn.foresight.module.nv.report.wrapper.ReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.LegendListWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;



@Service("BRTIReportServiceImpl")
public class BRTIReportServiceImpl implements IBRTIReportService{

	/** The logger. */
	private static final Logger logger = LogManager.getLogger(BRTIReportServiceImpl.class);

	@Autowired
	private IReportService reportService;

	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;

	@Autowired
	private IMapImagesService mapImageService;

	@Autowired
	private IBRTIExcelReportService iBRTIExcelReportService;

	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao ;

	@Autowired
	private IGenericWorkorderDao genericWrokOrderDao;
	@Autowired
	private ILegendRangeDao legendRangeDao;
	
	private List<String> filesToDeleteAfterExecution = new ArrayList<>();


	@Override
	public Response execute(String json) {
		logger.info("Going to execute method for json {} ", json);
		String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.BRTI
				+ ReportConstants.FORWARD_SLASH+new Date().getTime()+ReportConstants.FORWARD_SLASH;
		ReportUtil.createDirectory(filePath);
		try {
			JSONObject jsonObj=ReportUtil.convertStringToJsonObject(json);
			Long analyticsrepoId = (Long) jsonObj.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			AnalyticsRepository analyticObj = analyticsrepositoryDao.findByPk(analyticsrepoId.intValue());
			return createBRTIReportsByReportType(analyticObj, jsonObj, filePath);
		} catch (Exception e) {
			logger.info("Unable to Genearate BRTI Quarterly excel Report  reason is {} ", Utils.getStackTrace(e));
			Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}


	private Response createBRTIReportsByReportType(AnalyticsRepository analyticObj, JSONObject jsonMap, String filePath) {
		logger.info("inside method createBRTIReportsByReportType {} ",jsonMap.get(ReportConstants.REPORT_TYPE));
		if(jsonMap.get(ReportConstants.REPORT_TYPE).toString().equalsIgnoreCase(ReportConstants.BRTI_REPORT_TYPE_QUATERLY)){
			Response res = createQuertalyReports(analyticObj, jsonMap, filePath);
			return createzipFileAndSaveToHdfs(res,analyticObj, filePath);
			
		}else if(jsonMap.get(ReportConstants.REPORT_TYPE).toString().equalsIgnoreCase(ReportConstants.BRTI_REPORT_TYPE_QUATERLY_COMBINE)){
			JSONObject jsonObj= (JSONObject) jsonMap.get(ReportConstants.DATA);
			Map<String, List<Integer>> technoLogyWiseMap = getTechNologyMapForWoidsByJsonObject(jsonObj,jsonMap);
			List<Integer>woIds=getWoIdFromMap(technoLogyWiseMap);
			List<Integer> geoIds = getGeographyIdListByJsonObject(jsonObj);
			Response res = createBRTIExcelReport(jsonMap, filePath, woIds, geoIds);
			return createzipFileAndSaveToHdfs(res,analyticObj, filePath);

		}else if(jsonMap.get(ReportConstants.REPORT_TYPE).toString().equalsIgnoreCase(ReportConstants.BRTI_YEARLY_REPORT_TYPE)){
			Response res = genrateBRTIYearlyReports(jsonMap,analyticObj,filePath);
			return createzipFileAndSaveToHdfs(res,analyticObj, filePath);
		}
		
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}


	private List<Integer> getWoIdFromMap(Map<String, List<Integer>> technoLogyWiseMap) {
		List<Integer> woIds = new ArrayList<>();
		for (Entry<String, List<Integer>> map : technoLogyWiseMap.entrySet()) {
			woIds.addAll(map.getValue());
		}
		return woIds;
	}


	private Response createzipFileAndSaveToHdfs(Response response, AnalyticsRepository analyticObj, String filePath) {
		String output = (String) response.getEntity();
		logger.info("output {} ",output);
		if(output.contains(ForesightConstants.SUCCESS.toLowerCase())){
			String zipFilePath = ReportUtil.createZipFileOnPath(analyticObj, filePath);
			String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.MASTER + ReportConstants.FORWARD_SLASH;
			Response responseReturn = reportService.saveFileAndUpdateStatus(analyticObj.getId(), hdfsFilePath, null, new File(zipFilePath),
					analyticObj.getName() + ReportConstants.DOT_ZIP,NVWorkorderConstant.REPORT_INSTACE_ID);
			IBWifiReportUtils.removeJunkFilesFromDisk(filesToDeleteAfterExecution);
			return responseReturn;
		}else{
			logger.info("Unable to generate");
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}


	
	private Response createQuertalyReports(AnalyticsRepository analyticObj, JSONObject jsonMap, String filePath) {
		logger.info("Going To createQuertalyReports For Id {} ",analyticObj.getId());
		try {
			JSONObject jsonObj = (JSONObject) jsonMap.get(ReportConstants.DATA);
			List<Integer> woIds = new ArrayList<>();
			Map<String, List<Integer>> technoLogyWiseMap = getTechNologyMapForWoidsByJsonObject(jsonObj,jsonMap);
			for (Entry<String, List<Integer>> map : technoLogyWiseMap.entrySet()) {
					List<Integer> ids = map.getValue();
					woIds.addAll(ids);
			}
			if (woIds.size() > ReportConstants.INDEX_ZER0) {
					createBRTIQuaterlyPPTReport(analyticObj, jsonMap, filePath);
			}
			return Response.ok(ForesightConstants.SUCCESS_JSON).build();
		}catch(Exception e) {
			logger.error("Exception in method createQuertalyReports {}",Utils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();

	}
	
	
	private ReportSubWrapper getWrapperForReport(JSONObject jsonMap, Map<String, String> smmryMap,
			List<String[]> listArray, List<KPIWrapper> kpiList, List<Integer> geoIdList, String technologyBand) {
		logger.info("Inside getWrapperForReport {} ", new Gson().toJson(jsonMap));

		
		ReportSubWrapper graphWrapper = reportService.getGraphDataWrapper(kpiList, listArray);
		graphWrapper.setCallEventStatsDrive(
				ReportUtil.getCallDataForBRTI(smmryMap.get(ReportConstants.RECIPE_CATEGORY_DRIVE)));
		graphWrapper.setCallEventStatsStationary(
				ReportUtil.getCallDataForBRTI(smmryMap.get(ReportConstants.RECIPE_CATEGORY_STATIONARY)));
		graphWrapper.setQuarter(
				"Q" + jsonMap.get(ReportConstants.QUARTER) + Symbol.HYPHEN + jsonMap.get(ReportConstants.YEAR));

		if (geoIdList != null && !geoIdList.isEmpty()) {
			logger.info("geographyType {} , ids {} ",jsonMap.get(ReportConstants.GEOGRAPHY_TYPE),geoIdList);
			List<String> geoNameList = reportService.getGegraphyNameList(
					jsonMap.get(ReportConstants.GEOGRAPHY_TYPE).toString(), geoIdList);
			if (geoNameList.size() > ReportConstants.INDEX_ZER0) {
				graphWrapper.setTestName(geoNameList.get(ReportConstants.INDEX_ZER0));
				graphWrapper.setCityName(geoNameList.get(ReportConstants.INDEX_ZER0));
				logger.info("City Namew Set In Report Sub Warpper {}",graphWrapper.getCityName());
			}
		}
		graphWrapper.setMonthAndYear(jsonMap.get(ReportConstants.YEAR).toString());
		graphWrapper.setTechnologyType(technologyBand);

		return graphWrapper;
	}
	

	private Map<String, String> getFinalSummaryMap(Map<String, String> driveMap, Map<String, String> stationaryMap) {
		Map<String, String> smmryMap = new HashMap<>();
		if (driveMap != null) {
			smmryMap.put(ReportConstants.RECIPE_CATEGORY_DRIVE, driveMap.get(ReportConstants.SUMMARY));
		}
		if (stationaryMap != null) {
			smmryMap.put(ReportConstants.RECIPE_CATEGORY_STATIONARY, stationaryMap.get(ReportConstants.SUMMARY));
		}
		return smmryMap;
	}


	private List<Integer> getGeographyIdListByJsonObject(JSONObject jsonObj) {
		List<Integer> geoIdsList = null;
		try {
			List<Long>geoIdList= (List<Long>) jsonObj.get(ReportConstants.GEOGRAPHY_ID_LIST);
			geoIdsList = geoIdList.stream()
					.mapToInt(Long::intValue)
					.boxed().collect(Collectors.toList());
			logger.info("getGeographyIdListByJsonObject list {} ",geoIdList);
		} catch (ClassCastException e) {
			logger.error("geogrpahyId list is not available for jsobObj {} ",jsonObj);
			Long geographyId = (Long) jsonObj.get(ReportConstants.GEOGRAPHY_ID_LIST);
			if(geographyId!=null){
				geoIdsList = Arrays.asList(geographyId.intValue());
			}
		}catch (Exception e) {
			logger.error("Exception occured inside method getGeographyIdListByJsonObject {} ",e.getMessage());
		}
		return geoIdsList;
	}


	private Map<String, List<Integer>> getTechNologyMapForWoidsByJsonObject(JSONObject jsonObj, JSONObject jsonMap) {
		Map<String,List<Integer>>technologyWiseMap=new HashMap<>();
		String technology=jsonMap.get(ReportConstants.TECHNOLOGY)!=null?(String) jsonMap.get(ReportConstants.TECHNOLOGY):ReportConstants.ALL;

		try {
			List<String>woIdList= (List<String>) jsonObj.get(ReportConstants.WORKORDER_IDS);
			return getTechnologyWiseMapFromStringList(technologyWiseMap, woIdList,technology);
		}
		catch(ClassCastException ce) {
			logger.error("ClassCastException because woids not contain any Technology {}",ce.getMessage());
			List<Long>woIdList= (List<Long>)  jsonObj.get(ReportConstants.WORKORDER_IDS);
			List<Integer> woids = woIdList.stream()
					.mapToInt(Long::intValue)
					.boxed().collect(Collectors.toList());
			logger.info("woids {} woIdList{}",woids,woIdList);
			technologyWiseMap.put(technology, woids);
		}
		catch(Exception e) {
			logger.error("Exception inside  the method getWoIdsByJsonObject {}",Utils.getStackTrace(e));
		}
		return technologyWiseMap;
	}


	private Map<String, List<Integer>> getTechnologyWiseMapFromStringList(Map<String, List<Integer>> technologyWiseMap,
			List<String> woIdList, String technology) {
		for (String woId : woIdList) {
			if (woId.contains(ReportConstants.UNDERSCORE)) {
				String[] arr = woId.split(ReportConstants.UNDERSCORE);
				if (technologyWiseMap.containsKey(arr[ReportConstants.INDEX_ONE])) {
					List<Integer> ids = technologyWiseMap.get(arr[ReportConstants.INDEX_ONE]);
					ids.add(Integer.valueOf(arr[ReportConstants.INDEX_ZER0]));
					technologyWiseMap.put(arr[ReportConstants.INDEX_ONE], ids);
				} else {
					List<Integer> ids = new ArrayList<>();
					ids.add((Integer.valueOf(arr[ReportConstants.INDEX_ZER0])));
					technologyWiseMap.put(arr[ReportConstants.INDEX_ONE], ids);

				}
			}

			else {
				if (technologyWiseMap.containsKey(technology.toUpperCase())) {
					List<Integer> ids = technologyWiseMap.get(technology.toUpperCase());
					ids.add(Integer.valueOf(woId));
					technologyWiseMap.put(technology.toUpperCase(), ids);
				} else {
					List<Integer> ids = new ArrayList<>();
					ids.add(Integer.valueOf(woId));
					technologyWiseMap.put(technology.toUpperCase(), ids);

				}

			}

		}
		return technologyWiseMap;
	}





	private Response genrateBRTIYearlyReports(JSONObject jsonMap, AnalyticsRepository analyticObj, String filePath) {
		logger.info("Going to generate the yearly Report");
		createYearlyExcelReport(jsonMap, analyticObj,filePath);
		createYearlyPdfReport(jsonMap, analyticObj,filePath);
		return Response.ok(ForesightConstants.SUCCESS_JSON).build();
	}


	private File createYearlyPdfReport(JSONObject jsonMap, AnalyticsRepository analyticObj, String filePath) {
		try {
			logger.info("Going to create BRTI yearly Report For ID {}",analyticObj.getId());
			logger.info("inside the method createYearlyPdfReport  for id {}", analyticObj.getId());
			List<BRTIYearlyReportWrapper> combineList = new ArrayList<>();
			List<String> list = Arrays.asList(ReportConstants.ARRIVAL_LIST, ReportConstants.INTERMEDIATE_LIST,
					ReportConstants.DEPARTURE_LIST);
			for (String key : list) {
				BRTIYearlyReportWrapper wrapper=getBRTIYeralyReportList(jsonMap, key);
				if(wrapper!=null) {
					combineList.add(wrapper);
				}
			}
			BRTIReportWrapper mainWrapper=setDataForBRTIYearlyReport(combineList,jsonMap);
			return proceedToCreateBRTIYearyReport(new HashMap<>(), mainWrapper,filePath);
		}
		catch(Exception e) {
			logger.error("Exception inside the method createYearlyPdfReport {}",Utils.getStackTrace(e));
		}
		return null;
	}


	private BRTIReportWrapper setDataForBRTIYearlyReport(List<BRTIYearlyReportWrapper> combineList, JSONObject jsonMap) {
		BRTIReportWrapper mainWrapper = new BRTIReportWrapper();
		BRTIReportSubWrapper subWrapper = new BRTIReportSubWrapper();
		List<BRTIReportSubWrapper> subList = new ArrayList<>();
		subWrapper.setYearlyDataList(combineList);
		subList.add(subWrapper);
		mainWrapper.setSubList(subList);
		String reportName=getBRTIPdfReportName(jsonMap);
		mainWrapper.setReportName(reportName);
		return mainWrapper;
	}


	private String getBRTIPdfReportName(JSONObject jsonMap) {
		String name =ReportConstants.DRIVE_TEST;
	
		if (jsonMap.get(ReportConstants.YEAR) != null) {
			name += ReportConstants.UNDERSCORE + jsonMap.get(ReportConstants.YEAR);
		}
		
		return name;
	}


	private BRTIYearlyReportWrapper getBRTIYeralyReportList(JSONObject jsonMap, String key) {
		logger.info("inside the method  getBRTIYeralyReportList for key {}",key);
		JSONObject arrivalJson = getJsonByListType(jsonMap,key);
		Map<String, List<Integer>> technologyWiseMap=getTechNologyMapForWoidsByJsonObject(arrivalJson,jsonMap);
		List<Integer> wiIdsList=getWoIdFromMap(technologyWiseMap);
		List<Integer> geoList=getGeographyIdListByJsonObject(arrivalJson);
		if (!wiIdsList.isEmpty()) {
			return getBRTIYearlyDataList(geoList,wiIdsList,jsonMap);
		}
		return null;
	}


	private JSONObject getJsonByListType(JSONObject jsonMap, String key) {
		JSONObject dataJson = (JSONObject) jsonMap.get(ReportConstants.DATA);
		return (JSONObject) dataJson.get(key);
	}


	private BRTIYearlyReportWrapper getBRTIYearlyDataList(List<Integer> geoList, List<Integer> woIdsList, JSONObject jsonMap) {
		logger.info("inside the ,method getBRTIYearlyDataList geoList{} woIdsList{}",geoList,woIdsList);
		HashMap<String, BufferedImage> mobileRsrpImgMap=null;
		HashMap<String, BufferedImage> voiceRsrpImgMap=null;
		BRTIYearlyReportWrapper wrapper=new BRTIYearlyReportWrapper();
		String assignTo = (String) jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY);
		if (reportService.getFileProcessedStatusForWorkorders(woIdsList)) {
			try {
				String jsonDriveData = nvLayer3DashboardService.getDriveDetailDataForWoIds(woIdsList);
				List<String[]> dataList = ReportUtil.convertCSVStringToDataList(jsonDriveData);
				List<KPIWrapper> kpiList = getKpiDataListForYearlyReport();
				kpiList = reportService.setKpiStatesIntokpiList(kpiList, woIdsList);
				kpiList = LegendUtil.populateLegendData(kpiList, dataList, DriveHeaderConstants.INDEX_TEST_TYPE);
				List<String[]> mobileDataList = ReportUtil.filterDataByTestType(dataList,DriveHeaderConstants.INDEX_TEST_TYPE,
						NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD,NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD,NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD,NetworkDataFormats.TEST_TYPE_FTP_UPLOAD);
				List<String[]> voiceDataList = ReportUtil.filterDataByTestType(dataList,DriveHeaderConstants.INDEX_TEST_TYPE,
						NetworkDataFormats.SHORT_CALL_TEST, NetworkDataFormats.LONG_CALL_TEST);
				mobileRsrpImgMap = getImageMapForYearlyReport(kpiList, mobileDataList);
				voiceRsrpImgMap = getImageMapForYearlyReport(kpiList, voiceDataList);
				setRSRPImagesToWrapper(voiceRsrpImgMap, mobileRsrpImgMap, wrapper);
				setDriveSummaryDataToWrapper(geoList, woIdsList, jsonMap, wrapper);
				List<String> citys=reportService.getGegraphyNameList((String)jsonMap.get(ReportConstants.GEOGRAPHY_TYPE), geoList);
				setCityToWrapper(citys,wrapper);
			} catch (Exception e) {
				logger.error("Exception inside the method getBRTIYearlyDataList {}",Utils.getStackTrace(e));
			}

		}
		return wrapper;

	}


	private void setCityToWrapper(List<String> citys, BRTIYearlyReportWrapper wrapper) {
		try {
			StringBuilder cityName= new StringBuilder();
			if(citys!=null&&!citys.isEmpty()) {
				for (String city : citys) {
					if(cityName.toString().equals(ReportConstants.BLANK_STRING))
						cityName.append(ReportConstants.SPACE+ReportConstants.HYPHEN+city);
					else
						cityName.append(city);
				}
			}
			wrapper.setCityName(cityName.toString());
		} catch (Exception e) {
			logger.error("Error Inside setCityToWrapper {} ",e.getMessage());
		}

	}


	private List<KPIWrapper> getKpiDataListForYearlyReport() {
		List<LegendWrapper> legendList = legendRangeDao
				.findAllLegendRangesAppliedTo(ReportConstants.BRTI);
		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList,
				DriveHeaderConstants.getKpiIndexMapFORSsvT());
		return kpiList.stream()
				.filter(kpiWrapper -> kpiWrapper.getKpiName().equalsIgnoreCase(ReportConstants.RSRP))
				.collect(Collectors.toList());
	}


	private void setDriveSummaryDataToWrapper(List<Integer> geoList, List<Integer> woIdsList, JSONObject jsonMap,
			BRTIYearlyReportWrapper wrapper) {
		try {
			List<BRTIExcelReportWrapper>stationarySummaryList=new ArrayList<>();
			List<String> locationList=reportService.getGegraphyNameList((String)jsonMap.get(ReportConstants.GEOGRAPHY_TYPE), geoList);
			List<GenericWorkorder> genricWoList=genericWrokOrderDao.findByIds(woIdsList);
			String technology=jsonMap.get(ReportConstants.TECHNOLOGY)!=null?(String) jsonMap.get(ReportConstants.TECHNOLOGY):ReportConstants.ALL;
			List<BRTIExcelReportWrapper> driveList = iBRTIExcelReportService.getBrtiDriveDataForWoIds(woIdsList, false, genricWoList,locationList.size()>ReportConstants.INDEX_ZER0?locationList.get(ReportConstants.INDEX_ZER0):null, technology);
			stationarySummaryList=iBRTIExcelReportService.getStationaryDataList(false, stationarySummaryList, genricWoList, true);
		    logger.info("stationarySummaryList {}",new Gson().toJson(stationarySummaryList));
			 wrapper.setDriveSummaryList(driveList);
			 setStationaryDataForYearlyReport(stationarySummaryList,wrapper);
		} catch (Exception e) {
			logger.error("Error inside setDriveSummaryDataToWrapper {} ",e.getMessage());
		}
		
	}


	private void setStationaryDataForYearlyReport(List<BRTIExcelReportWrapper> stationarySummaryList,
			BRTIYearlyReportWrapper wrapper) {
		try {
			logger.info("inside the method setStationaryDataForYearlyReport {}", stationarySummaryList.size());
			logger.info("inside the method setStationaryDataForYearlyReport DATA {}", new Gson().toJson(stationarySummaryList));

			List<BRTIStaionaryDataWrapper> stationaryList = new ArrayList<>();
			List<BRTIExcelReportWrapper> dataList = new ArrayList<>();
			int i = 1;
			for (BRTIExcelReportWrapper excelReportWrapper : stationarySummaryList) {
				dataList.add(excelReportWrapper);
				if (i == ReportConstants.INDEX_FIVE) {
					BRTIStaionaryDataWrapper stWrapper = new BRTIStaionaryDataWrapper();
					stWrapper.setStationarySummaryList(dataList);
					stationaryList.add(stWrapper);
					i = 0;
					dataList = new ArrayList<>();

				}
				i++;
			}
			if (!dataList.isEmpty()) {
				BRTIStaionaryDataWrapper stWrapper = new BRTIStaionaryDataWrapper();
				stWrapper.setStationarySummaryList(dataList);
				stationaryList.add(stWrapper);

			}

			wrapper.setStationaryList(stationaryList);
			logger.info("inside the method  Data nsdsadas {}",new Gson().toJson(wrapper.getStationaryList()));
		} catch (Exception e) {
			logger.error("Exception in method setStationaryDataForYearlyReport {}", Utils.getStackTrace(e));
		}

	}


	private void setRSRPImagesToWrapper(HashMap<String, BufferedImage> voiceRsrpImgMap,
			HashMap<String, BufferedImage> mobileRsrpImgMap, BRTIYearlyReportWrapper wrapper) {
		try {
			if (mobileRsrpImgMap != null) {
				wrapper.setMobileRsrpImg(ReportUtil.getInputStreamFromBufferedImage(
						mobileRsrpImgMap.get(DriveHeaderConstants.INDEX_RSRP.toString())));
				wrapper.setMobileRsrpImgLegendImg(ReportUtil.getInputStreamFromBufferedImage(mobileRsrpImgMap.get(
						ReportConstants.LEGEND + ReportConstants.UNDERSCORE + DriveHeaderConstants.INDEX_RSRP)));
			}
			if (voiceRsrpImgMap != null) {
				wrapper.setVoiceRsrpImg(ReportUtil.getInputStreamFromBufferedImage(
						voiceRsrpImgMap.get(DriveHeaderConstants.INDEX_RSRP.toString())));
				wrapper.setVoiceRsrpLegendImg(ReportUtil.getInputStreamFromBufferedImage(voiceRsrpImgMap.get(
						ReportConstants.LEGEND + ReportConstants.UNDERSCORE + DriveHeaderConstants.INDEX_RSRP)));
			}
		} catch (Exception e) {
			logger.error("Error Inside setRSRPImagesToWrapper {} ",e.getMessage());

		}
	}


	private HashMap<String, BufferedImage> getImageMapForYearlyReport(List<KPIWrapper> kpiList, List<String[]> filterList) throws IOException {
		HashMap<String, BufferedImage> bufferdImageMap = new HashMap<>();
		try {
			DriveImageWrapper driveImageWrapper =new DriveImageWrapper(filterList,DriveHeaderConstants.INDEX_LAT,
						DriveHeaderConstants.INDEX_LON, DriveHeaderConstants.INDEX_PCI, kpiList, null, null);
			bufferdImageMap = reportService.getLegendImages(kpiList,driveImageWrapper.getDataKPIs(), DriveHeaderConstants.INDEX_TEST_TYPE);
				bufferdImageMap.putAll(mapImageService.getDriveImages(driveImageWrapper, null));
		} catch (Exception e) {
			logger.error("Inside getImageMapForYearlyReport {}",e.getMessage());
		}
		return bufferdImageMap;
			
	}


	

	private Response createYearlyExcelReport(JSONObject jsonMap, AnalyticsRepository analyticObj, String filePath) {
		try {
			Map<String, List<BRTIExcelReportWrapper>> brtiExcelDataMap = getBrtiYearlyExcelData(jsonMap);
			logger.info("brtiExcelDataMap Data {} ",new Gson().toJson(brtiExcelDataMap));
			String fileName = filePath+ReportConstants.FORWARD_SLASH+analyticObj.getName()+ReportConstants.UNDERSCORE+analyticObj.getId()+ReportConstants.XLSX_EXTENSION;
			BRTIExcelReport.createYearlyExcelReport(brtiExcelDataMap,fileName);
			return Response.ok(ForesightConstants.SUCCESS_JSON).build();
		} catch (Exception e) {
			logger.error("Exception inside method genrateBRTIYearlyReport {} ", Utils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}


	private Map<String, List<BRTIExcelReportWrapper>> getBrtiYearlyExcelData(JSONObject jsonMap) {
		Map<String, List<BRTIExcelReportWrapper>> brtiDataMap = new HashMap<>();
		try {
			JSONObject jsonObject  = (JSONObject) jsonMap.get(ReportConstants.DATA);
			List<String> diffLocationList = Arrays.asList(ReportConstants.DEPARTURE_LIST,ReportConstants.INTERMEDIATE_LIST,ReportConstants.ARRIVAL_LIST);
			for (String locationType : diffLocationList) {
				brtiDataMap.put(locationType, getBrtiExcelDataList(locationType,jsonObject,jsonMap.get(ReportConstants.GEOGRAPHY_TYPE).toString()));
			}
			return brtiDataMap;
		} catch (Exception e) {
			logger.error("Exception inside method getBrtiYearlyExcelData {} ",Utils.getStackTrace(e));
		}
		//-
		return brtiDataMap;
	}


	private List<BRTIExcelReportWrapper> getBrtiExcelDataList(String locationType, JSONObject jsonObject, String geographyType) {
		List<BRTIExcelReportWrapper> brtiWrapperList = new ArrayList<>();
		JSONObject locationTypeObj  = (JSONObject) jsonObject.get(locationType);
		String technology=jsonObject.get(ReportConstants.TECHNOLOGY)!=null?(String) jsonObject.get(ReportConstants.TECHNOLOGY):ReportConstants.ALL;
		Map<String, List<Integer>> technologyWiseMap=getTechNologyMapForWoidsByJsonObject(locationTypeObj,jsonObject);
		List<Integer> woIdList=getWoIdFromMap(technologyWiseMap);
	
		List<Long> geographyIdList = (locationTypeObj.get(ReportConstants.GEOGRAPHY_ID_LIST)!=null?(List<Long>) locationTypeObj.get(ReportConstants.GEOGRAPHY_ID_LIST):null);
		if(woIdList.size()>ReportConstants.INDEX_ZER0 && geographyIdList!=null){
			List<Integer> geoIdList = geographyIdList.stream().mapToInt(Long::intValue).boxed().collect(Collectors.toList());
			List<GenericWorkorder> genricWoList = genericWrokOrderDao.findByIds(woIdList);
			brtiWrapperList = iBRTIExcelReportService.getStationaryDataList(false, brtiWrapperList, genricWoList,true);
			List<String> locationList=reportService.getGegraphyNameList(geographyType, geoIdList);
			List<BRTIExcelReportWrapper> driveList = iBRTIExcelReportService.getBrtiDriveDataForWoIds(woIdList, false, genricWoList,locationList.size()>ReportConstants.INDEX_ZER0?locationList.get(ReportConstants.INDEX_ZER0):null, technology);
			if(driveList!=null && driveList.size()>ReportConstants.INDEX_ZER0){
				brtiWrapperList.addAll(driveList);
			}
			logger.debug("brtiWrapperList Data Size  {} , for locationType {} ",brtiWrapperList!=null?brtiWrapperList.size():null,locationType);
			return brtiWrapperList;
		}
		return brtiWrapperList;
	}



	private List<KPIWrapper> getKPidataWithStats(List<Integer> woIds, List<String[]> listArray) {
		List<KPIWrapper> kpiList = reportService.getListOfKpisWithRanges(DriveHeaderConstants.getKpiIndexMap(), ReportConstants.SSVT_REPORT);
		kpiList = reportService.setKpiStatesIntokpiList(kpiList, woIds);
		return LegendUtil.populateLegendData(kpiList, listArray, DriveHeaderConstants.INDEX_TEST_TYPE);
	}


	/** //////////////////////////////////////////////Excel Report Start/////////////////////////////////////. */

	public Response createBRTIExcelReport(Map<String, Object> jsonMap, String brtiDestinationFilePath, List<Integer> woids, List<Integer> geoIds) {
		Map<String ,   Map<String,BRTIExcelReportWrapper>> cityWiseDriveMap = new HashMap<>();
		Map<String ,   Map<String,BRTIExcelReportWrapper>> cityWiseStstionaryMap = new HashMap<>();
		Map<String ,   Map<String,BRTIExcelReportWrapper>> cityWiseCombinedMap = new HashMap<>();		

		for (Integer geograhyId : geoIds) {
			logger.info("geograhyId is {}",geograhyId);
			List<String> cityList = reportService.getGegraphyNameList((String)jsonMap.get(ReportConstants.GEOGRAPHY_TYPE),
					Arrays.asList(geograhyId));	
			String cityName = null;
			if(cityList!=null && !cityList.isEmpty()) {
				cityName = cityList.get(ReportConstants.INDEX_ZER0);
			}

			logger.info("cityName from method getGegraphyNameList {}",cityName);
			if(jsonMap.get(ReportConstants.QUARTER)!=null && jsonMap.get(ReportConstants.YEAR)!=null) {
				Integer qt=((Long)jsonMap.get(ReportConstants.QUARTER)).intValue();

				Integer yt=((Long)jsonMap.get(ReportConstants.YEAR)).intValue();

				List<Map> maplist=nvLayer3DashboardService.getWorkorderListByGeographyOfPeriod((String)jsonMap.get(ReportConstants.GEOGRAPHY_TYPE), Arrays.asList(geograhyId), Arrays.asList(Status.COMPLETED),Arrays.asList(TemplateType.NV_BRTI,TemplateType.NV_ADHOC_BRTI_DRIVE,TemplateType.NV_ADHOC_BRTI_ST), Arrays.asList(qt), Arrays.asList(yt));

				List<Integer>intList= getListOFIDFromMap(maplist);
				logger.info("intList {} woids====>{}",intList,woids);
				List<Integer>filterList =getFiltertList(woids,intList);
				logger.info("filterList is {}",filterList);

				if(!filterList.isEmpty()) {
					cityWiseDriveMap = getBandWiseOperatorData(cityWiseDriveMap, cityName,
							ReportConstants.RECIPE_CATEGORY_DRIVE, cityWiseCombinedMap,filterList);

					cityWiseStstionaryMap = getBandWiseOperatorData(cityWiseStstionaryMap, cityName,
							ReportConstants.RECIPE_CATEGORY_STATIONARY, cityWiseCombinedMap,filterList);
				}
			}else {

				return Response.ok(ForesightConstants.ERROR_INVALIDPARAMS).build();

			}

		}

		int rowstrt=ReportConstants.INDEX_ZER0;
		int colstrt=ReportConstants.INDEX_ZER0;

		String filename=brtiDestinationFilePath+ReportConstants.BRTI_COMBINE_EXCEL_SHEET_NAME;		


		XSSFWorkbook workbook = new XSSFWorkbook(); 
		Sheet sheet = workbook.createSheet(ReportConstants.BRTI_EXCEL_SHEET_ONE_NAME);
		Sheet sheet2 = workbook.createSheet(ReportConstants.BRTI_EXCEL_SHEET_TWO_NAME);

		int noofrow=geoIds.size()+ReportConstants.INDEX_THREE;


		//sheet2 onnetOffnet data	

		//KPI SHEET
		workbook = insertDataForKpiSheetIntoExcelWorkbook(cityWiseDriveMap, cityWiseStstionaryMap, cityWiseCombinedMap,
				colstrt, workbook, sheet, noofrow);

		//BACKUP SHEET
		workbook = insertDataForBackupSheetIntoExcelWorkbook(geoIds, cityWiseDriveMap, cityWiseStstionaryMap,
				cityWiseCombinedMap, colstrt, workbook, sheet2, rowstrt);

		//AUTOSIZE COLUMNS WIDTH
		setColumnSizeToAutoForExcelReport(workbook, sheet, sheet2);

		// Write the output to a file
		return writeWorksheetToExcel(filename, workbook);

	}


	public Response writeWorksheetToExcel(String filename, XSSFWorkbook workbook) {
		try {
			ExcelReportUtils.exportExcel(filename, workbook);
			return Response.ok(ForesightConstants.SUCCESS_JSON).build();
		} catch (IOException | RestException e) {			
			logger.error("Error in writeWorksheetToExcel {}",Utils.getStackTrace(e));
		}

		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}


	public void setColumnSizeToAutoForExcelReport(XSSFWorkbook workbook, Sheet sheet, Sheet sheet2) {
		for(int i=0;i<30;i++) {
			sheet.autoSizeColumn(i);
			sheet2.autoSizeColumn(i);
		}
		ReportUtil.autoSizeColumns(workbook);

	}


	public XSSFWorkbook insertDataForKpiSheetIntoExcelWorkbook(
			Map<String, Map<String, BRTIExcelReportWrapper>> cityWiseDriveMap,
			Map<String, Map<String, BRTIExcelReportWrapper>> cityWiseStstionaryMap,
			Map<String, Map<String, BRTIExcelReportWrapper>> cityWiseCombinedMap, int colstrt, XSSFWorkbook workbook,
			Sheet sheet, int noofrow) {

		int i=0;
		//Total Data of all city combined
		String[] data1= ReportUtil.BRTI_EXCEL_REPORT__TOTAL_TABLE_HEADER1.stream().toArray(String[]::new);
		String[] data2= ReportUtil.BRTI_EXCEL_REPORT__TOTAL_TABLE_HEADER2.stream().toArray(String[]::new);

		workbook = iBRTIExcelReportService.writeDataColumnWise(sheet,workbook,i, colstrt++,data1);

		for (Entry<String, Map<String, BRTIExcelReportWrapper>> entry : cityWiseCombinedMap.entrySet()) {
			List<BRTIExcelReportWrapper> dataList = ReportUtil.getExcelWrapperList(entry.getValue());
			if(dataList!=null && !dataList.isEmpty()) {
				BRTIExcelReportWrapper berw=getTotalWrappperForBRTIReportWrapper(dataList);
				String[] data3= getCallDataArrayForExcelReport(entry, berw);

				logger.debug("cityWiseCombinedMap dataList {}",new Gson().toJson(berw));
				logger.debug("getTotalWrappperForBRTIReportWrapper berw total wrapper {}",new Gson().toJson(berw));

				workbook = iBRTIExcelReportService.writeDataColumnWise(sheet,workbook,i, colstrt++,data3);	
			}

		}
		workbook = iBRTIExcelReportService.writeDataColumnWise(sheet,workbook,i, colstrt++,data2);


		i=0;++colstrt;
		//drive test
		for (Entry<String, Map<String, BRTIExcelReportWrapper>> entry : cityWiseDriveMap.entrySet()) {
			List<BRTIExcelReportWrapper> dataList = ReportUtil.getExcelWrapperList(entry.getValue());
			if(dataList!=null && !dataList.isEmpty()) {
				workbook = iBRTIExcelReportService.generateCallTableAtGivenRowCol(sheet,workbook,dataList,i, 
						colstrt,ReportConstants.DRIVE_TEST+entry.getKey());		
				i=i+noofrow;
			}
		}

		//stationary test
		for (Entry<String, Map<String, BRTIExcelReportWrapper>> entry : cityWiseStstionaryMap.entrySet()) {
			List<BRTIExcelReportWrapper> dataList = ReportUtil.getExcelWrapperList(entry.getValue());
			if(dataList!=null && !dataList.isEmpty()) {
				workbook = iBRTIExcelReportService.generateCallTableAtGivenRowCol(sheet,workbook,dataList,i, 
						colstrt,ReportConstants.STATIC_CALL_TEST+entry.getKey());		
				i=i+noofrow;
			}
		}

		i=0;
		colstrt=colstrt+7;
		//combined data
		for (Entry<String, Map<String, BRTIExcelReportWrapper>> entry : cityWiseCombinedMap.entrySet()) {
			List<BRTIExcelReportWrapper> dataList = ReportUtil.getExcelWrapperList(entry.getValue());
			if(dataList!=null && !dataList.isEmpty()) {
				getTotalWrappperForBRTIReportWrapper(dataList);	
				workbook = iBRTIExcelReportService.generateCallTableAtGivenRowCol(sheet,workbook,dataList,i, 
						colstrt,ReportConstants.END_POINT_SERVICE_AVAILABILITY+entry.getKey());		
				i=i+noofrow+1;
			}
		}


		i=0;
		colstrt=colstrt+7;
		//smsdata
		for (Entry<String, Map<String, BRTIExcelReportWrapper>> entry : cityWiseCombinedMap.entrySet()) {
			List<BRTIExcelReportWrapper> dataList = ReportUtil.getExcelWrapperList(entry.getValue());
			if(dataList!=null && !dataList.isEmpty()) {
				getTotalWrappperForBRTIReportWrapper(dataList);	
				workbook = iBRTIExcelReportService.generateSMSTableAtGivenRowCol(sheet,workbook,dataList,i,
						colstrt,ReportConstants.SMS_TEST+entry.getKey());		
				i=i+noofrow+1;
			}
		}
		return workbook;
	}

	private String[] getCallDataArrayForExcelReport(Entry<String, Map<String, BRTIExcelReportWrapper>> entry, BRTIExcelReportWrapper berw){
		return new String[]{entry.getKey(),"  ",
				berw.getTotalCall()==null?"-":berw.getTotalCall().toString(),
						berw.getFailCall()==null?"-":berw.getFailCall().toString(),
								berw.getDropcall()==null?"-":berw.getDropcall().toString(),
										berw.getCallEsaRate()==null?"-":berw.getCallEsaRate().toString(),
												berw.getCallDropRate()==null?"-":berw.getCallDropRate().toString(),
														"   ",
														berw.getTotalSms()==null?"-":berw.getTotalSms().toString(),
																berw.getSmsDeliivered()==null?"-":berw.getSmsDeliivered().toString(),
																		berw.getSmsDeliveryRate()==null?"-":berw.getSmsDeliveryRate().toString()};

	}

	public XSSFWorkbook insertDataForBackupSheetIntoExcelWorkbook(List<Integer> geoIds,
			Map<String, Map<String, BRTIExcelReportWrapper>> cityWiseDriveMap,
			Map<String, Map<String, BRTIExcelReportWrapper>> cityWiseStstionaryMap,
			Map<String, Map<String, BRTIExcelReportWrapper>> cityWiseCombinedMap, int colstrt, XSSFWorkbook workbook,
			Sheet sheet2, int i) {
		//sheet2 BACKKUP data	

		//drive data onnetOffnet
		for (Entry<String, Map<String, BRTIExcelReportWrapper>> entry : cityWiseDriveMap.entrySet()) {
			List<BRTIExcelReportWrapper> dataList = ReportUtil.getExcelWrapperList(entry.getValue());
			if(dataList!=null && !dataList.isEmpty()) {
				logger.info("Getting call drive detail {} ",new Gson().toJson(dataList));
				workbook = iBRTIExcelReportService.generateCallTableAtGivenRowColOnNetOffNet(sheet2,workbook,dataList,i,
						colstrt,ReportConstants.DRIVE_TEST+entry.getKey());		
				i=i+geoIds.size()*2+3;
			}

		}		
		//Static data onnetOffnet
		for (Entry<String, Map<String, BRTIExcelReportWrapper>> entry : cityWiseStstionaryMap.entrySet()) {
			List<BRTIExcelReportWrapper> dataList = ReportUtil.getExcelWrapperList(entry.getValue());
			if(dataList!=null && !dataList.isEmpty()) {
				logger.info("Getting call static detail {} ",new Gson().toJson(dataList));

				workbook = iBRTIExcelReportService.generateCallTableAtGivenRowColOnNetOffNet(sheet2,workbook,dataList,i,
						colstrt,ReportConstants.STATIC_CALL_TEST+entry.getKey());		
				i=i+geoIds.size()*2+3;
			}
		}

		i=0;
		//sms data OnnetOfnet
		for (Entry<String, Map<String, BRTIExcelReportWrapper>> entry : cityWiseCombinedMap.entrySet()) {
			List<BRTIExcelReportWrapper> dataList = ReportUtil.getExcelWrapperList(entry.getValue());
			if(dataList!=null && !dataList.isEmpty()) {
				getTotalWrappperForBRTIReportWrapper(dataList);		
				logger.info("Getting sms test detail {} ",new Gson().toJson(dataList));

				workbook = iBRTIExcelReportService.generateSmsTableAtGivenRowColOnnetOffnet(sheet2,workbook,dataList,i,
						8,ReportConstants.SMS_TEST+entry.getKey());		
				i=i+geoIds.size()*2+4;
			}
		}
		return workbook;
	}


	private List<Integer> getFiltertList(List<Integer> woids, List<Integer> intList) {
		List<Integer> list=new ArrayList<>();

		for (Integer id : intList) {
			if(woids.contains(id)) {
				list.add(id);
			}
		}
		return list;
	}


	private List<Integer> getListOFIDFromMap(List<Map> maplist) {
		List<Integer> list=new ArrayList<>();
		for (Map map : maplist) {
			Integer id=(Integer)map.get(ReportConstants.VALUE);
			list.add(id);
		}
		return list;
	}



	public BRTIExcelReportWrapper getTotalWrappperForBRTIReportWrapper(List<BRTIExcelReportWrapper> dataList) {
		BRTIExcelReportWrapper totalWrapper=new BRTIExcelReportWrapper();
		for(BRTIExcelReportWrapper single:dataList) {
			totalWrapper.setTotalCall(ReportUtil.addValues(single.getTotalCall(),totalWrapper.getTotalCall()));
			totalWrapper.setFailCall(ReportUtil.addValues(single.getFailCall(),totalWrapper.getFailCall()));
			totalWrapper.setDropcall(ReportUtil.addValues(single.getDropcall(),totalWrapper.getDropcall()));
			totalWrapper.setCityName(ReportConstants.NATIONAL);
			calculateEsaRateForFinalRow(totalWrapper);
			if(totalWrapper.getTotalCall()!=null&&totalWrapper.getDropcall()!=null) {
				totalWrapper.setCallDropRate(
						ReportUtil.getPercentage(totalWrapper.getDropcall(), totalWrapper.getTotalCall()));
			}


			totalWrapper.setTotalSms(ReportUtil.addValues(single.getTotalSms(),totalWrapper.getTotalSms()));
			totalWrapper.setSmsDeliivered(ReportUtil.addValues(single.getSmsDeliivered(),totalWrapper.getSmsDeliivered()));
			if(totalWrapper.getSmsDeliivered()!=null&&totalWrapper.getTotalSms()!=null) {
				totalWrapper.setSmsDeliveryRate(
						ReportUtil.getPercentage(totalWrapper.getSmsDeliivered(), totalWrapper.getTotalSms())
						);
			}


			totalWrapper.setTotalSmsOffnet(ReportUtil.addValues(single.getTotalSmsOffnet(),totalWrapper.getTotalSmsOffnet()));
			totalWrapper.setSmsDeliiveredOffnet(ReportUtil.addValues(single.getSmsDeliiveredOffnet(),totalWrapper.getSmsDeliiveredOffnet()));

			if(totalWrapper.getSmsDeliiveredOffnet()!=null&&totalWrapper.getTotalSmsOffnet()!=null) {
				totalWrapper.setSmsDeliveryRateOffnet(
						ReportUtil.getPercentage(totalWrapper.getSmsDeliiveredOffnet(), totalWrapper.getTotalSmsOffnet())
						);
			}


			totalWrapper.setTotalSmsOnnet(ReportUtil.addValues(single.getTotalSmsOnnet(),totalWrapper.getTotalSmsOnnet()));
			totalWrapper.setSmsDeliiveredOnnet(ReportUtil.addValues(single.getSmsDeliiveredOnnet(),totalWrapper.getSmsDeliiveredOnnet()));

			if(totalWrapper.getSmsDeliiveredOnnet()!=null&&totalWrapper.getTotalSmsOnnet()!=null) {
				totalWrapper.setSmsDeliveryRateOnnet(
						ReportUtil.getPercentage(totalWrapper.getSmsDeliiveredOnnet(), totalWrapper.getTotalSmsOnnet())
						);
			}


		}
		dataList.add(totalWrapper);
		return totalWrapper;
	}


	public void calculateEsaRateForFinalRow(BRTIExcelReportWrapper totalWrapper) {
		if(totalWrapper.getTotalCall()!=null&&totalWrapper.getFailCall()!=null) {
			totalWrapper.setCallEsaRate(
					ReportUtil.getPercentage(totalWrapper.getTotalCall() - totalWrapper.getFailCall(), totalWrapper.getTotalCall())
					);
		}
	}

	private Map<String, Map<String,BRTIExcelReportWrapper>> getBandWiseOperatorData(Map<String, Map<String,BRTIExcelReportWrapper>> cityWiseDriveMap, String cityName, String category, Map<String, Map<String, BRTIExcelReportWrapper>> cityWiseCombinedMap, List<Integer> woIds) {

		Map<String , BRTIExcelReportWrapper> bandWiseDriveMap=iBRTIExcelReportService.createQuertelyExcelReportByCategory(woIds,category,cityName);
		addDataIntoDriveMap(cityWiseDriveMap, cityName, bandWiseDriveMap);

		addDataIntoCombinMap(cityWiseCombinedMap, cityName, bandWiseDriveMap);
		logger.info("inside the method getBandWiseOperatorData cityWiseDriveMap{}",cityWiseDriveMap);		
		logger.info("inside the method getBandWiseOperatorData cityWiseCombinedMap{}",cityWiseCombinedMap);

		return cityWiseDriveMap;
	}

	private void addDataIntoCombinMap(Map<String, Map<String, BRTIExcelReportWrapper>> cityWiseCombinedMap,
			String cityName, Map<String, BRTIExcelReportWrapper> bandWiseDriveMap) {
		for (Entry<String, BRTIExcelReportWrapper> entry : bandWiseDriveMap.entrySet()) {
			if (entry.getValue() != null) {
				if (cityWiseCombinedMap.containsKey(entry.getKey())) {

					Map<String, BRTIExcelReportWrapper> dataList = cityWiseCombinedMap.get(entry.getKey());
					if (dataList.containsKey(cityName)) {
						BRTIExcelReportWrapper brtiOldData = dataList.get(cityName);
						BRTIExcelReportWrapper brtiNewData = entry.getValue();
						BRTIExcelReportWrapper brtiData = ReportUtil.mergeData(brtiOldData, brtiNewData);
						dataList.put(cityName, brtiData);
					} else {
						dataList.put(cityName, entry.getValue());
						cityWiseCombinedMap.put(entry.getKey(), dataList);
					}
				} else {
					Map<String, BRTIExcelReportWrapper> dataList = new HashMap<>();
					dataList.put(cityName, entry.getValue());
					cityWiseCombinedMap.put(entry.getKey(), dataList);

				}
			}
		}

	}



	private void addDataIntoDriveMap(Map<String, Map<String, BRTIExcelReportWrapper>> cityWiseDriveMap, String cityName,
			Map<String, BRTIExcelReportWrapper> bandWiseDriveMap) {
		for (Entry<String, BRTIExcelReportWrapper> entry : bandWiseDriveMap.entrySet()) {
			if (entry.getValue() != null) {
				if (cityWiseDriveMap.containsKey(entry.getKey())) {
					Map<String, BRTIExcelReportWrapper> dataList = cityWiseDriveMap.get(entry.getKey());
					dataList.put(cityName, entry.getValue());
					cityWiseDriveMap.put(entry.getKey(), dataList);
				} else {
					Map<String, BRTIExcelReportWrapper> dataList = new HashMap<>();
					dataList.put(cityName, entry.getValue());
					cityWiseDriveMap.put(entry.getKey(), dataList);

				}
			}
		}
	}

	/**
	 * ////////////////////////////////////////////Excel Report END/////////////////////////////////////

	 * ////////////////////////////////////////////BRTIQuarterlyPDF Report Start/////////////////////////////////////.
	 */

	private File proceedTocreateBRTIQuarterlyPDFReport(Map<String, Object> imagePathMap, ArrayList<ReportSubWrapper> listForPdf, String brtiDestinationFilePath, String reportAssetPath) {
		logger.info("inside the method proceedTocreateBRTIQuarterlyPDFReport");
		try {
			logger.info("reportAssetPath {} ",reportAssetPath);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(listForPdf);

			setStaticReportImages(imagePathMap, reportAssetPath);

			String destinationFileName = brtiDestinationFilePath + ReportConstants.BRTI_QUARTERLY_REPORT_FILE_NAME;

			String tempFileNameforPPT = JasperFillManager.fillReportToFile(reportAssetPath + ReportConstants.MAIN_JASPER,imagePathMap, rfbeanColDataSource);


			getPPTXFromReport(tempFileNameforPPT,destinationFileName+ReportConstants.PPTX_EXTENSION);
			getPdfFromReport(tempFileNameforPPT, destinationFileName+ReportConstants.PDF_EXTENSION);

			logger.info("BRTIQuarterlyPDF Report Created successfully  ");
			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@createReport getting err={}", Utils.getStackTrace(e));
		}
		logger.info(
				"@createReport going to return null as there has been some problem in generating report");
		return null;
	}


	/**
	 * ////////////////////////////////////////////BRTIQuarterlyPDF Report END/////////////////////////////////////

	 * ////////////////////////////////////////////QuarterlyCrbPDF Report Start/////////////////////////////////////.
	 */


	/** ////////////////////////////////////////////BRTIQuarterlyPPT Report Start/////////////////////////////////////. */
	private File proceedToCreateBRTIYearyReport(Map<String, Object> imageMap, BRTIReportWrapper mainWrapper,
			String filePathForPdf) {
		logger.info("inside the method proceedToCreateBRTIYearyReport ");
		try {
			String reportAssetPath = ConfigUtils.getString(ReportConstants.BRTI_YEARLY_REPORT_JASPER_PATH);
			List<BRTIReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			setStaticReportImages(imageMap, reportAssetPath);

			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetPath);
			imageMap.put(ReportConstants.IMAGE_SECOND_SCREEB,
					reportAssetPath + ReportConstants.IMAGE_PARAM_SCREEN_2 + ReportConstants.DOT_PNG);

			filePathForPdf += mainWrapper.getReportName() + ReportConstants.PDF_EXTENSION;

			String tempFileNameforPPT = JasperFillManager.fillReportToFile(
					reportAssetPath + ReportConstants.MAIN_JASPER, imageMap, rfbeanColDataSource);

			getPPTXFromReport(tempFileNameforPPT,
					filePathForPdf.replace(ReportConstants.PDF_EXTENSION, ReportConstants.PPTX_EXTENSION));
			getPdfFromReport(tempFileNameforPPT, filePathForPdf);

			logger.info("Report Created successfully  ");

			return ReportUtil.getIfFileExists(filePathForPdf);
		} catch (Exception e) {
			logger.error("@proceedToCreateBRTIYearyReport getting err={}", Utils.getStackTrace(e));
		}
		logger.info("@proceedToCreateBRTIYearyReport going to return null as there has been some problem in generating report");
		return null;
	}

	private boolean getPPTXFromReport(String inputFileName, String outputFileName) throws JRException {
		if(!StringUtils.isBlank(inputFileName)) {
			JRPptxExporter exporter = new JRPptxExporter();
			exporter.setExporterInput(new SimpleExporterInput(new File(inputFileName)));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(outputFileName)));
//			exporter.setParameter(JRExporterParameter.INPUT_FILE, new File(inputFileName));
//			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(outputFileName));
			exporter.exportReport();
			return true;
		}
		return false;
	}

	private boolean getPdfFromReport(String inputFileName, String outputFileName) throws JRException {
		if(!StringUtils.isBlank(inputFileName)) {
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(new File(inputFileName)));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(outputFileName)));
//			exporter.setParameter(JRExporterParameter.INPUT_FILE, new File(inputFileName));
//			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(outputFileName));
			exporter.exportReport();
			return true;
		}
		return false;
	}
	private static boolean getPdfFromReport1(String inputFileName, String outputFileName) throws JRException {
		if(!StringUtils.isBlank(inputFileName)) {
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(new File(inputFileName)));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(outputFileName)));
//			exporter.setParameter(JRExporterParameter.INPUT_FILE, new File(inputFileName));
//			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(outputFileName));
			exporter.exportReport();
			return true;
		}
		return false;
	}
	/** ////////////////////////////////////////////BRTIQuarterlyPPT Report END/////////////////////////////////////. */

	private Map<String, String> getImagesMap(List<String[]> listArray, List<KPIWrapper> kpiList) {
		Map<String,String> imagePathMap = new HashMap<>();
		try {
			List<SiteInformationWrapper> silteList = null;
			silteList = reportService.getSiteDataByDataList(listArray);
			kpiList = reportService.modifyIndexOfCustomKpis(kpiList);
			DriveImageWrapper driveImageWrapper = new DriveImageWrapper(listArray, DriveHeaderConstants.INDEX_LAT,
					DriveHeaderConstants.INDEX_LON, DriveHeaderConstants.INDEX_PCI, kpiList, silteList, null);
			HashMap<String, BufferedImage> driveImages = mapImageService.getDriveImages(driveImageWrapper, null);
			Map<String, BufferedImage> legendImages = reportService.getLegendImages(kpiList, listArray, DriveHeaderConstants.INDEX_TEST_TYPE);
			driveImages.putAll(legendImages);
			String imagePath = ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT) + ReportConstants.BRTI
					+ ReportConstants.FORWARD_SLASH
					+ ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MM_YY_HH_SS)
					+ ReportConstants.FORWARD_SLASH;
			imagePathMap = mapImageService.saveDriveImages(driveImages, imagePath, false);
			filesToDeleteAfterExecution.addAll(imagePathMap.values());
			
		}
		catch(Exception e) {
			logger.error("Exception in  getImagesMap {}",e.getMessage());
		}
		return imagePathMap;
	}


	private void setStaticReportImages(Map<String, Object> imagePathMap, String reportAssetRepo) {
		imagePathMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
		imagePathMap.put(ReportConstants.IMAGE_PARAM_HEADER_BG, reportAssetRepo + ReportConstants.IMAGE_PARAM_HEADER_BG+ReportConstants.DOT_PNG);
		imagePathMap.put(ReportConstants.IMAGE_PARAM_SUB_REPORT_SCREEN_BG, reportAssetRepo + ReportConstants.IMAGE_PARAM_SUB_REPORT_SCREEN_BG+ReportConstants.DOT_PNG);
		imagePathMap.put(ReportConstants.IMAGE_PARAM_SCREEN_BG, reportAssetRepo + ReportConstants.IMAGE_PARAM_SCREEN_BG+ReportConstants.DOT_PNG);

		imagePathMap.put(ReportConstants.IMAGE_PARAM_THANK_YOU, reportAssetRepo + ReportConstants.IMAGE_PARAM_THANK_YOU+ReportConstants.DOT_PNG);
	}
	private static void setStaticReportImages1(Map<String, Object> imagePathMap, String reportAssetRepo) {
		imagePathMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
		imagePathMap.put(ReportConstants.IMAGE_PARAM_HEADER_BG, reportAssetRepo + ReportConstants.IMAGE_PARAM_HEADER_BG+ReportConstants.DOT_PNG);
		imagePathMap.put(ReportConstants.IMAGE_PARAM_SUB_REPORT_SCREEN_BG, reportAssetRepo + ReportConstants.IMAGE_PARAM_SUB_REPORT_SCREEN_BG+ReportConstants.DOT_PNG);
		imagePathMap.put(ReportConstants.IMAGE_PARAM_SCREEN_BG, reportAssetRepo + ReportConstants.IMAGE_PARAM_SCREEN_BG+ReportConstants.DOT_PNG);

		imagePathMap.put(ReportConstants.IMAGE_PARAM_THANK_YOU, reportAssetRepo + ReportConstants.IMAGE_PARAM_THANK_YOU+ReportConstants.DOT_PNG);
	}
	
	/** ///////////////////Quaterly Report Changed ////////////////////////////////////////////. */
	private Response createBRTIQuaterlyPPTReport(AnalyticsRepository analyticObj, JSONObject jsonMap, String filePath) {
		logger.info("Going To createBRTIQuaterlyReport For Id {} ",analyticObj.getId());
		try {
			String config1 = analyticObj.getReportConfig();
			String config = config1.replaceAll("'", "\"");
			logger.info("config before replace {} , afetr replace {} ",config1,config);
			JSONObject reportConfigObj= ReportUtil.convertStringToJsonObject(config);
			JSONObject dataFieldObj = (JSONObject) reportConfigObj.get(ReportConstants.DATA);
			Map<String, List<Integer>> technoLogyWiseMap = getTechNologyMapForWoidsByJsonObjectNew(dataFieldObj,dataFieldObj);
			logger.info("technoLogyWiseMap Data {} ",technoLogyWiseMap);
				Map<String, ReportSubWrapper> map = populateBRTIQuaterlyData(reportConfigObj, filePath, technoLogyWiseMap);
				Map<String,List<LegendListWrapper>> tableDataOFKpis = getTableSummaryOfKpis(map);
				Map<String,List<LegendListWrapper>> sortedTableDataOFKpis = getSortedTableDataByRange(tableDataOFKpis);
				Map<String,String> bandKeysMap = getBandTypeMaps(map);
				logger.info("bandKeysMap Data {} ",bandKeysMap);
				return createBRTIQuarterlyPDFReportNew(reportConfigObj, filePath, technoLogyWiseMap, map, sortedTableDataOFKpis,bandKeysMap);
		}catch(Exception e) {
			logger.error("Exception inside method createBRTIQuaterlyReport {}",Utils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}


	private Map<String, List<LegendListWrapper>> getSortedTableDataByRange(Map<String, List<LegendListWrapper>> tableDataOFKpis) {
		Map<String, List<LegendListWrapper>> sortedTableDataOFKpis = new HashMap<>();
		try {
			for (Entry<String, List<LegendListWrapper>> legendListMap : tableDataOFKpis.entrySet()) {
				sortedTableDataOFKpis.put(legendListMap.getKey(),ReportUtil.getSortedKpiLegendList(legendListMap.getKey(), legendListMap.getValue()));
			}
			logger.info("After sorting sortedTableDataOFKpis is Size is {} ",sortedTableDataOFKpis.size());
			return sortedTableDataOFKpis;
		} catch (Exception e) {
			logger.error("Exception inside methid getSortedTableDataByRange {} ",Utils.getStackTrace(e));
		}
		return tableDataOFKpis;
	}


	private Map<String, List<Integer>> getTechNologyMapForWoidsByJsonObjectNew(JSONObject jsonObj, JSONObject jsonMap) {
		Map<String,List<Integer>> technologyWiseMap = new HashMap<>();
		try {
			List<String> workOrderIdList = (List<String>) jsonObj.get("workOrderIds");
			for (String workOrderBand : workOrderIdList) {
				String[] arr = workOrderBand.split(ReportConstants.UNDERSCORE);
				if(technologyWiseMap.containsKey(arr[1])){
					List<Integer> woIdList = technologyWiseMap.get(arr[1]);
					woIdList.add(Integer.parseInt(arr[0]));
					technologyWiseMap.put(arr[1], woIdList);
				}else{
					List<Integer> listofWoIds= new ArrayList<>();
					listofWoIds.add(Integer.parseInt(arr[0]));
					technologyWiseMap.put(arr[1], listofWoIds);
				}
				
			}
			return technologyWiseMap;
		}
		catch(Exception e) {
			logger.error("Exception inside  the method getTechNologyMapForWoidsByJsonObjectNew {}",Utils.getStackTrace(e));
		}
		return technologyWiseMap;
	}

	private Response createBRTIQuarterlyPDFReportNew(JSONObject jsonMap, String filePath,Map<String, List<Integer>> technoLogyWiseMap, Map<String, ReportSubWrapper> finalDataMap, Map<String, List<LegendListWrapper>> tableDataOFKpis, Map<String, String> bandKeysMap) {
		ReportSubWrapper finaWrapper = mergeBrtiQuarterlyData(finalDataMap);
		if (finaWrapper != null) {
			populateRankDataInReportSubWrapper(finalDataMap, finaWrapper);
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.putAll(tableDataOFKpis);
			dataMap.putAll(bandKeysMap);
			dataMap.put(ReportConstants.ROUTE, finaWrapper	.getImageMap()
															.get(DriveHeaderConstants.INDEXROUTE));
			ArrayList<ReportSubWrapper> list = new ArrayList<>();
			list.add(finaWrapper);
			String reportAssetPath = ConfigUtils.getString(
					ReportConstants.BRTI_QUARTERLY_COMPARISON_REPORT_FOLDER_PATH);
			if(dataMap.get("key1")!=null&&dataMap.get("key2")!=null) {
				reportAssetPath+=ReportConstants.BRTI_MULTIPLE_TECHNOLOGY;
			} else if(dataMap.get("key1")!=null||dataMap.get("key2")!=null) {
				reportAssetPath+=ReportConstants.BRTI_SINGLE_TECHNOLOGY;
			} 
			proceedTocreateBRTIQuarterlyPDFReport(dataMap, list, filePath, reportAssetPath+ReportConstants.FORWARD_SLASH);
			return Response	.ok(ForesightConstants.SUCCESS_JSON)
							.build();
		} else {
			return Response.ok(ForesightConstants.FAILURE_JSON).build();	
		}
	}



	private ReportSubWrapper getDataWrapper(List<Integer> woIds, JSONObject jsonMap, String filePath,List<Integer> geoIdList, String techBand) {
		logger.info("Inside method getDataWrapper for WoIds {} ", woIds);
		String jsonDriveData = nvLayer3DashboardService.getDriveDetailDataForWoIds(woIds);
		Map<String,String> driveMap= nvLayer3DashboardService.getBandWiseDataMapForCategory(woIds,ReportConstants.RECIPE_CATEGORY_DRIVE,false);
		Map<String,String> stationaryMap= nvLayer3DashboardService.getBandWiseDataMapForCategory(woIds,ReportConstants.RECIPE_CATEGORY_STATIONARY,false);
		Map<String, String> smmryMap = getFinalSummaryMap(driveMap, stationaryMap);
		if (jsonDriveData != null) {
			List<String[]> listArray = ReportUtil.convertCSVStringToDataList(jsonDriveData);
			if (!listArray.isEmpty()){
				List<KPIWrapper> kpiList = getKPidataWithStats(woIds, listArray);
				Map<String,String> imagePathMap = getImagesMap(listArray,kpiList);
				ReportSubWrapper graphWrapper = getWrapperForReport(jsonMap, smmryMap, listArray, kpiList,geoIdList,techBand);
				setImageLegendAndKpiName(graphWrapper,imagePathMap, techBand);
				kpiList.forEach(wrapper->wrapper.setOperatorName(techBand));
				graphWrapper.setKpiList(kpiList);
				graphWrapper.setImageMap(imagePathMap);
				graphWrapper.setJsonData(listArray);
				return graphWrapper;
			}else{
				throw new BusinessException("data not available to generathe the file ");
			}
		}
		return null;
	}
	
	
	private void setImageLegendAndKpiName(ReportSubWrapper graphWrapper, Map<String, String> imagePathMap, String technology) {
		if(graphWrapper.getRsrpList()!=null&&!graphWrapper.getRsrpList().isEmpty()&&graphWrapper.getRsrpList().get(0)!=null) {
			if(imagePathMap.get(DriveHeaderConstants.INDEX_RSRP.toString())!=null) {
				graphWrapper.getRsrpList().get(0).setTechnology(technology);
				graphWrapper.getRsrpList().get(0).setKpiPlotImg(imagePathMap.get(DriveHeaderConstants.INDEX_RSRP.toString()));
				graphWrapper.getRsrpList().get(0).setKpiLegendImg(imagePathMap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + DriveHeaderConstants.INDEX_RSRP));
			} else {
				graphWrapper.setRsrpList(null);
			}
		}
		if(graphWrapper.getSinrList()!=null&&!graphWrapper.getSinrList().isEmpty()&&graphWrapper.getSinrList().get(0)!=null) {
			if(imagePathMap.get(DriveHeaderConstants.INDEX_SINR.toString())!=null) {
				graphWrapper.getSinrList().get(0).setTechnology(technology);
				graphWrapper.getSinrList().get(0).setKpiPlotImg(imagePathMap.get(DriveHeaderConstants.INDEX_SINR.toString()));
				graphWrapper.getSinrList().get(0).setKpiLegendImg(imagePathMap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + DriveHeaderConstants.INDEX_SINR));
			} else {
				graphWrapper.setSinrList(null);
			}
		}
		if(graphWrapper.getUlList()!=null&&!graphWrapper.getUlList().isEmpty()&&graphWrapper.getUlList().get(0)!=null) {
			if(imagePathMap.get(DriveHeaderConstants.INDEX_UL.toString())!=null) {
				graphWrapper.getUlList().get(0).setTechnology(technology);
				graphWrapper.getUlList().get(0).setKpiPlotImg(imagePathMap.get(DriveHeaderConstants.INDEX_UL.toString()));
				graphWrapper.getUlList().get(0).setKpiLegendImg(imagePathMap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + DriveHeaderConstants.INDEX_UL));
			} else {
				graphWrapper.setUlList(null);
			}
		}
		if(graphWrapper.getDlList()!=null&&!graphWrapper.getDlList().isEmpty()&&graphWrapper.getDlList().get(0)!=null) {
			if(imagePathMap.get(DriveHeaderConstants.INDEX_DL.toString())!=null) {
				graphWrapper.getDlList().get(0).setTechnology(technology);
				graphWrapper.getDlList().get(0).setKpiPlotImg(imagePathMap.get(DriveHeaderConstants.INDEX_DL.toString()));
				graphWrapper.getDlList().get(0).setKpiLegendImg(imagePathMap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + DriveHeaderConstants.INDEX_DL));
			} else {
				graphWrapper.setDlList(null);
			}
		}
		if(graphWrapper.getEarfcnList()!=null&&!graphWrapper.getEarfcnList().isEmpty()&&graphWrapper.getEarfcnList().get(0)!=null) {
			if(imagePathMap.get(DriveHeaderConstants.INDEX_DL_EARFCN.toString())!=null) {
				graphWrapper.getEarfcnList().get(0).setTechnology(technology);
				graphWrapper.getEarfcnList().get(0).setKpiPlotImg(imagePathMap.get(DriveHeaderConstants.INDEX_DL_EARFCN.toString()));
				graphWrapper.getEarfcnList().get(0).setKpiLegendImg(imagePathMap.get(ReportConstants.DL_EARFCN));
			} else {
				graphWrapper.setEarfcnList(null);
			}
		}
		if(graphWrapper.getMosList()!=null&&!graphWrapper.getMosList().isEmpty()&&graphWrapper.getMosList().get(0)!=null) {
			if(imagePathMap.get(DriveHeaderConstants.INDEX_MOS.toString())!=null) {
				graphWrapper.getMosList().get(0).setTechnology(technology);
				graphWrapper.getMosList().get(0).setKpiPlotImg(imagePathMap.get(DriveHeaderConstants.INDEX_MOS.toString()));
				graphWrapper.getMosList().get(0).setKpiLegendImg(imagePathMap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + DriveHeaderConstants.INDEX_MOS));
			} else {
				graphWrapper.setMosList(null);
			}
		}
		if(graphWrapper.getRiList()!=null&&!graphWrapper.getRiList().isEmpty()&&graphWrapper.getRiList().get(0)!=null) {
			if(imagePathMap.get(DriveHeaderConstants.INDEX_MIMO.toString())!=null) {
				graphWrapper.getRiList().get(0).setTechnology(technology);
				graphWrapper.getRiList().get(0).setKpiPlotImg(imagePathMap.get(DriveHeaderConstants.INDEX_MIMO.toString()));
				graphWrapper.getRiList().get(0).setKpiLegendImg(imagePathMap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + DriveHeaderConstants.INDEX_MIMO));
			} else {
				graphWrapper.setRiList(null);
			}
		}
		if(graphWrapper.getCqiList()!=null&&!graphWrapper.getCqiList().isEmpty()&&graphWrapper.getCqiList().get(0)!=null) {
			if(imagePathMap.get(DriveHeaderConstants.INDEXCQI.toString())!=null) {
				graphWrapper.getCqiList().get(0).setTechnology(technology);
				graphWrapper.getCqiList().get(0).setKpiPlotImg(imagePathMap.get(DriveHeaderConstants.INDEXCQI.toString()));
				graphWrapper.getCqiList().get(0).setKpiLegendImg(imagePathMap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + DriveHeaderConstants.INDEXCQI));
			} else {
				graphWrapper.setCqiList(null);
			}
		}
		
	}

	private ReportSubWrapper mergeBrtiQuarterlyData(Map<String, ReportSubWrapper> finalDataMap) {
		boolean first = true;
		ReportSubWrapper oldWrapper = null;
		for (Entry<String, ReportSubWrapper> entry : finalDataMap.entrySet()) {
			if (first) {
				oldWrapper = entry.getValue();
				first=false;
			} else {
				ReportSubWrapper newWrapper = entry.getValue();
				if(oldWrapper!=null&&newWrapper!=null) {
					return mergeReportSubWrapperValues(oldWrapper, entry.getValue());
				}else if(oldWrapper!=null) {
					return oldWrapper;
				} else{
					return newWrapper;
				} 
			}
		}
		return oldWrapper;
	}

	private ReportSubWrapper mergeReportSubWrapperValues(ReportSubWrapper oldWrapper, ReportSubWrapper newWrapper) {
		logger.info("Inside method mergeReportSubWrapperValues oldWrapper Data {} , newWrapper {} ",new Gson().toJson(oldWrapper),new Gson().toJson(newWrapper));
		oldWrapper.setRsrpList(mergeGraphWrapperList(oldWrapper.getRsrpList(),newWrapper.getRsrpList()));
		oldWrapper.setSinrList(mergeGraphWrapperList(oldWrapper.getSinrList(),newWrapper.getSinrList()));
		oldWrapper.setDlList(mergeGraphWrapperList(oldWrapper.getDlList(),newWrapper.getDlList()));
		oldWrapper.setUlList(mergeGraphWrapperList(oldWrapper.getUlList(),newWrapper.getUlList()));
		oldWrapper.setCqiList(mergeGraphWrapperList(oldWrapper.getCqiList(),newWrapper.getCqiList()));
		oldWrapper.setMosList(mergeGraphWrapperList(oldWrapper.getMosList(),newWrapper.getMosList()));
		oldWrapper.setEarfcnList(mergeGraphWrapperList(oldWrapper.getEarfcnList(),newWrapper.getEarfcnList()));
		oldWrapper.setRiList(mergeGraphWrapperList(oldWrapper.getRiList(),newWrapper.getRiList()));
		logger.info("oldWrapper Data {} ",new Gson().toJson(oldWrapper));
		return oldWrapper;
	}


	private List<GraphWrapper> mergeGraphWrapperList(List<GraphWrapper> oldList, List<GraphWrapper> newList) {
		if(oldList!=null && newList!=null) {
			oldList.addAll(newList);
			return oldList;
		} else if(oldList!=null) {
			return oldList;
		} else {
			return newList;
		}
	}


	private Map<String, List<LegendListWrapper>> getTableSummaryOfKpis(Map<String, ReportSubWrapper> map) {
		Map<String,List<LegendListWrapper>> kpiWiseTableMap = new HashMap<>();
		List<String> bandType = new ArrayList<>(map.keySet());
		ReportSubWrapper subWrapper=  map.get(bandType.get(ReportConstants.INDEX_ZER0));
		List<KPIWrapper> kpiListOfBandTypeOne= subWrapper.getKpiList();
		for (KPIWrapper kpiWrapper : kpiListOfBandTypeOne) {
			populateTableSummaryData(map, kpiWiseTableMap, subWrapper, kpiWrapper);
		}
		logger.info("kpiWiseTableMap Data Size is {} ",kpiWiseTableMap.size());
		return kpiWiseTableMap;
	}


	private void populateTableSummaryData(Map<String, ReportSubWrapper> map,
			Map<String, List<LegendListWrapper>> kpiWiseTableMap, ReportSubWrapper subWrapper, KPIWrapper kpiWrapper) {
		try {
			if (map.size() >= ReportConstants.INDEX_TWO) {
				List<KPIWrapper> singleKpilistOfAllTech = new ArrayList<>();
				List<List<KPIWrapper>> listOfListOfKpiWrapper = map	.values()
																	.stream()
																	.filter(Objects::nonNull)
																	.map(ReportSubWrapper::getKpiList)
																	.collect(Collectors.toList());
				listOfListOfKpiWrapper.forEach(
						list -> mergeSameKpiLists(list, singleKpilistOfAllTech, kpiWrapper.getKpiName()));
				logger.debug("kpiWrapperList Size for kpi {} ,size should be {} , is  {} ", kpiWrapper.getKpiName(),
						listOfListOfKpiWrapper.size(),singleKpilistOfAllTech.size());
				kpiWiseTableMap.put(kpiWrapper.getKpiName(), new ArrayList<>(
						ReportUtil.getRangeWiseLegendListWrapperMap(singleKpilistOfAllTech, subWrapper).values()));
			} else {
				List<KPIWrapper> singleKpilist = new ArrayList<>();
				singleKpilist.add(kpiWrapper);
				kpiWiseTableMap.put(kpiWrapper.getKpiName(), new ArrayList<>(
						ReportUtil.getRangeWiseLegendListWrapperMap(singleKpilist, subWrapper).values()));
			}
		} catch (Exception e) {
			logger.debug("Unable to  generate the table summary data for kpi {} , {} ", kpiWrapper.getKpiName(),
					Utils.getStackTrace(e));
		}
	}

	
	private Object mergeSameKpiLists(List<KPIWrapper> list, List<KPIWrapper> kpiWrapperList, String kpiName) {
		kpiWrapperList.addAll(list.stream().filter(wrapper->wrapper.getKpiName().equalsIgnoreCase(kpiName)).collect(Collectors.toList()));
		return list;
	}


	private Map<String, ReportSubWrapper> populateBRTIQuaterlyData(JSONObject jsonMap, String filePath,Map<String, List<Integer>> technoLogyWiseMap) {
		Map<String,ReportSubWrapper> finalDataMap = new HashMap<>();
		for (Entry<String, List<Integer>> map : technoLogyWiseMap.entrySet()) {
//			String assignTo = (String) jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY);
			@SuppressWarnings("unchecked")
			JSONObject dataFieldObj = (JSONObject) jsonMap.get(ReportConstants.DATA);
			List<Long> geoIdList = (List<Long>) dataFieldObj.get(ReportConstants.GEOGRAPHY_ID_LIST);
			logger.info("geoIdList {} ",geoIdList);
			Integer geoIds = geoIdList!=null?geoIdList.get(ReportConstants.INDEX_ZER0)!=null?geoIdList.get(ReportConstants.INDEX_ZER0).intValue():null:null;
			if(true/* reportService.getStatusOfQmdlFileProcessing(assignTo, map.getValue()) */){
				finalDataMap.put(map.getKey(),getDataWrapper(map.getValue(), jsonMap,filePath, Arrays.asList(geoIds), map.getKey()));
			}
		}
		logger.info("finalDataMap for BRTI New Quaterly Report Size is {} ",finalDataMap.size());
		return finalDataMap;
	}

	
	private Map<String, String> getBandTypeMaps(Map<String, ReportSubWrapper> map) {
		Map<String, String> bandKeysMap = new HashMap<>();
		List<String> bandType = new ArrayList<>(map.keySet());
		for(int index=1;index<=bandType.size();index++){
			bandKeysMap.put("key"+index, bandType.get(index-1));
		}
		return bandKeysMap;
	}

	private ReportSubWrapper populateRankDataInReportSubWrapper(Map<String, ReportSubWrapper> finalDataMap,ReportSubWrapper finalWrapper) {
		logger.info("Inside method populateRankDataInReportSubWrapper to fill the Rank Table for kpis ");
		try {
			Map<String, List<String[]>> driveDataByTechWise = new HashMap<>();
			for (Entry<String, ReportSubWrapper> reportDataMap : finalDataMap.entrySet()) {
				driveDataByTechWise.put(reportDataMap.getKey(), reportDataMap.getValue().getJsonData());
			}
			goingToSetRankDataInWrapper(driveDataByTechWise, finalWrapper);
		} catch (Exception e) {
			logger.error("Unable to populate Rank table data {} ",Utils.getStackTrace(e));
		}
		return finalWrapper;
	}
	
	
private void goingToSetRankDataInWrapper(Map<String, List<String[]>> driveDataByTechWise, ReportSubWrapper subWrapper) {
	logger.info("Inside method goingToSetRankDataInWrapper driveDataByTechWise {} ,subWrapper {}  ",driveDataByTechWise!=null?"driveDataByTechWise size : "+driveDataByTechWise.size():"driveDataByTechWise is null",subWrapper!=null?"Subwrapper is not null":"Suberapper is null");	
	Map<String,Integer> kpiIndexMap = DriveHeaderConstants.getKpiIndexMap();
		List<String> kpiList = new ArrayList<>(Arrays.asList(ReportConstants.RSRP,ReportConstants.SINR,ReportConstants.DL,ReportConstants.UL));
		List<Double> comparisonValue = new ArrayList<>(Arrays.asList(-105.0,3.0,5.0,3.0));
		if(subWrapper!=null){
			for (String kpi : kpiList) {
				if (kpi.equalsIgnoreCase(ReportConstants.RSRP)) {
					subWrapper.setRsrpRankList(ReportUtil.getRankListForKpi(comparisonValue, ReportConstants.INDEX_ZER0,
							driveDataByTechWise, kpiIndexMap.get(kpi)));
				} else if (kpi.equalsIgnoreCase(ReportConstants.SINR)) {
					subWrapper.setSinrRankList(ReportUtil.getRankListForKpi(comparisonValue, ReportConstants.INDEX_ONE,
							driveDataByTechWise, kpiIndexMap.get(kpi)));
				} else if (kpi.equalsIgnoreCase(ReportConstants.DL)) {
					subWrapper.setDlRankList(ReportUtil.getRankListForKpi(comparisonValue, ReportConstants.INDEX_TWO,
							driveDataByTechWise, kpiIndexMap.get(kpi)));
				} else if (kpi.equalsIgnoreCase(ReportConstants.UL)) {
					subWrapper.setUlRankList(ReportUtil.getRankListForKpi(comparisonValue, ReportConstants.INDEX_THREE,
							driveDataByTechWise, kpiIndexMap.get(kpi)));
				}
			}
		}
	}
}
