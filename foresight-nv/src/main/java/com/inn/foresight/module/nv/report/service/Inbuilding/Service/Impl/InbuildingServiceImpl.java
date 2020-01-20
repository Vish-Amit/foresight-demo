package com.inn.foresight.module.nv.report.service.Inbuilding.Service.Impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.maps.LatLng;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IBuildingDataDao;
import com.inn.foresight.core.infra.dao.IFloorDataDao;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.dao.IRANDetailDao;
import com.inn.foresight.core.infra.dao.IUnitDataDao;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.core.report.utils.ReportUtils;
import com.inn.foresight.module.nv.core.workorder.dao.IGWOMetaDao;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GWOMeta;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.Inbuilding.Service.IInbuildingService;
import com.inn.foresight.module.nv.report.service.Inbuilding.Utils.InbuildingReportUtil;
import com.inn.foresight.module.nv.report.service.Inbuilding.wrapper.FloorWiseWrapper;
import com.inn.foresight.module.nv.report.service.Inbuilding.wrapper.InbuildingDataWrapper;
import com.inn.foresight.module.nv.report.service.Inbuilding.wrapper.InbuildingWrapper;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("InbuildingServiceImpl")
public class InbuildingServiceImpl implements IInbuildingService, ReportConstants {

	private Logger logger = LogManager.getLogger(InbuildingServiceImpl.class);

	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;

	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;

	@Autowired
	private IBuildingDataDao buildingDataDao;

	@Autowired
	private IReportService reportService;

	@Autowired
	private IMapImagesService mapImagesService;

	@Autowired
	private IUnitDataDao iUnitDao;

	@Autowired
	private IFloorDataDao ifloordao;

	@Autowired
	private IRANDetailDao iranDetail;

	@Autowired
	private IGWOMetaDao gwoMetaDao;

	@Autowired
	private INetworkElementDao iNetworkElementDao;

	public InbuildingServiceImpl() {
		super();
	}

	@Override
	public Response createBuildingLevelInbuildingReport(Integer BuildingId, List<Integer> workorderList,
														Integer analyticsrepoId, String assignto, Integer projectId,Map<String, Object> jsonMap) {
		logger.info("Inside createBuildingLevelInbuildingReport {}  ,  {}", BuildingId, workorderList);
		try {
			if (workorderList != null && !workorderList.isEmpty()) {
				return generateBuildingLevelReport(analyticsrepoId, workorderList, BuildingId, assignto,jsonMap);
			}
		} catch (Exception e) {
			logger.info("exception inside method createBuildingLevelInbuildingReport {}", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public Response createFloorLevelInbuildingReport(Integer workorderId, GenericWorkorder workOrder,
													 Integer analyticsrepoId, Map<String, Object> jsonMap) {
		logger.info("Inside createFloorLevelInbuildingReport {}", workorderId);
		try {
			Map<Integer, String> recipecellmap = getRecipeWiseCellMap(workOrder);
			if (recipecellmap != null && !recipecellmap.isEmpty()) {
				return generateFloorLevelReport(recipecellmap, workorderId, analyticsrepoId, workOrder, jsonMap);
			}
		} catch (Exception e) {
			logger.info("Exception in createFloorLevelInbuildingReport {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private Response generateBuildingLevelReport(Integer analyticsrepoId, List<Integer> filteredWoList,
												 Integer buildingId, String assignto, Map<String, Object> jsonMap) {
		logger.info("Inside method generateBuildingLevelReport with woRecipeMap");
		try {
			InbuildingWrapper mainWrapper = new InbuildingWrapper();
			mainWrapper.setIsFloorLevel(false);
			List<FloorWiseWrapper> floorWiseDataList = new ArrayList<>();
			Map<String, Object> imageMap = new HashMap<>();
			List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs();
			Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
			List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
			Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper.getLiveDriveKPIIndexMap(fetchSummaryKPIList);
			AnalyticsRepository analyticrepositoryObj = analyticsRepositoryDao.findByPk(analyticsrepoId);
			Building building = getBuildingFromWorkOrder(buildingId);
			Map<Integer, Map<Integer, String>> woRecipeMap = getWorkorderRecipeMap(filteredWoList);
			List<String[]> driveData = getDriveData(filteredWoList, woRecipeMap);
			Map<String, String> cellStatusMap = new HashMap<>();
			for (Integer workorderId : filteredWoList) {
				GenericWorkorder filteredworkOrder = iGenericWorkorderDao.findByPk(workorderId);
				Map<Integer, String> recipecellmap = getRecipeWiseCellMap(filteredworkOrder);
				Map<String, String> cellwiseBandwidthMap = getCellwiseBandwidthMap(recipecellmap);
				setBuildingDataToWrapper(building, mainWrapper,recipecellmap,driveData, kpiIndexMap);
				Map<Integer, List<String>> floorwiseRecipeMap = getFloorWiseRecipeMapping(filteredworkOrder);
				setFloorWiseData(floorwiseRecipeMap, recipecellmap, workorderId, fetchSummaryKPIList, summaryKpiIndexMap,
						floorWiseDataList, mainWrapper, cellwiseBandwidthMap, cellStatusMap);
			}
			setMainWrapperBuildingData(mainWrapper, analyticrepositoryObj, assignto,driveData,kpiIndexMap,mainWrapper);
			setBuildingImageToWrapper(imageMap, building);
			mainWrapper.setDataList(floorWiseDataList);
			String destinationFilePath = null;
			if(building != null) {
				destinationFilePath = ReportUtil.getFileName(building.getBuildingId()+UNDERSCORE+"IBS", analyticsrepoId);
				File file = proceedToCreateOneCReport(mainWrapper, imageMap, analyticsrepoId,destinationFilePath);
				jsonMap.put(KEY_BUILDING_NAME, building.getBuildingId());
				return updateStatusAndPersistBuildingReport(jsonMap,file,analyticsrepoId,destinationFilePath);
			}
			//return updateStatus(analyticsrepoId, file);
		} catch (Exception e) {
			logger.info("exception in generateBuildingLevelReport", Utils.getStackTrace(e));
		}
		return null;
	}

	private Response generateFloorLevelReport(Map<Integer, String> recipeWiseCellMap, Integer woID,
											  Integer analyticsrepoId, GenericWorkorder workOrder, Map<String, Object> jsonMap)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		try {
			logger.info("Inside method generateFloorLevelReport");
			InbuildingWrapper mainWrapper = new InbuildingWrapper();
			mainWrapper.setIsFloorLevel(true);
			List<FloorWiseWrapper> floorWiseDataList = new ArrayList<>();
			Map<String, Object> imageMap = new HashMap<>();
			Map<String, String> cellStatusMap = new HashMap<>();
			Map<Integer, List<String>> floorwiseRecipeMap = getFloorWiseRecipeMapping(workOrder);
			List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
			Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper.getLiveDriveKPIIndexMap(fetchSummaryKPIList);
			List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs();
			Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
			logger.info("summaryKpiIndexMap :", summaryKpiIndexMap);
			Map<String, String> cellwiseBandwidthMap = getCellwiseBandwidthMap(recipeWiseCellMap);
			setFloorWiseData(floorwiseRecipeMap, recipeWiseCellMap, woID, fetchSummaryKPIList, summaryKpiIndexMap, floorWiseDataList,
					mainWrapper, cellwiseBandwidthMap, cellStatusMap);
			setMainWrapperData(mainWrapper, workOrder);
			List<String[]> driveData = reportService.getDriveDataForReport(woID, getRecipeListForDriveData(recipeWiseCellMap), fetchKPIList);
			Building building = getBuildingFromWorkOrder(Integer.parseInt(workOrder.getGwoMeta().get(NVWorkorderConstant.BUILDING_ID)));
			setBuildingDataToWrapper(building, mainWrapper,recipeWiseCellMap,driveData,kpiIndexMap);
			setBuildingImageToWrapper(imageMap, building);
			updateIntoGWOMeta(cellStatusMap, workOrder, woID);
			mainWrapper.setDataList(floorWiseDataList);
			setProjectIdFloorName(jsonMap, workOrder);
			if(building != null) {
				String destinationFilePath = ReportUtil.getFileName(building.getBuildingId()+UNDERSCORE+"IBS"+UNDERSCORE+jsonMap.get(KEY_FLOOR_NAME), woID);
				File file = proceedToCreateOneCReport(mainWrapper, imageMap, analyticsrepoId,destinationFilePath);
				return updateStatusAndPersistReport(workOrder,jsonMap,file,analyticsrepoId,destinationFilePath);
			}
		}catch(Exception e) {
			logger.info("Exception inside generateFloorLevelReport {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private File proceedToCreateOneCReport(InbuildingWrapper mainWrapper, Map<String, Object> imageMap,
										   Integer analyticsrepoId, String destinationFilePath) {
		logger.info("inside the method proceedToCreatePlot {} ", new Gson().toJson(mainWrapper));
		try {
			String reportAssets = ConfigUtils.getString(INBUILDING_ONEC_JASPER_PATH);
			List<InbuildingWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			imageMap.put(SUBREPORT_DIR, reportAssets);
			imageMap.put(IMAGE_PARAM_HEADER_LOGO, reportAssets + ReportConstants.IMAGE_HEADER_LOGO);
			imageMap.put(LOGO_NV_KEY, reportAssets + ReportConstants.LOGO_NV_IMG);
			imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssets + ReportConstants.LOGO_CLIENT_IMG);
			JasperRunManager.runReportToPdfFile(reportAssets + ReportConstants.MAIN_JASPER, destinationFilePath,
					imageMap, rfbeanColDataSource);
			logger.info("Report Created successfully  ");

			return ReportUtil.getIfFileExists(destinationFilePath);
		} catch (Exception e) {
			logger.error("@proceedToCreatePlot getting err={}", Utils.getStackTrace(e));
		}
		logger.info("@proceedToCreatePlot going to return null as there has been some problem in generating report");
		return null;

	}

	@Override
	public Response updateStatusAndPersistReport(GenericWorkorder workorderObj, Map<String, Object> jsonMap,
												 File file,Integer analyticsId, String destinationFilePath) throws UnsupportedCharsetException, IOException {
		String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.MASTER
				+ ReportConstants.FORWARD_SLASH;
		Response response=reportService.saveFileAndUpdateStatus(analyticsId,hdfsFilePath,workorderObj, file, ReportUtil.getFileNameFromFilePath(destinationFilePath),REPORT_INSTANCE_ID);
		String responseJson = (String) response.getEntity();
		try {
			if (InbuildingReportUtil.validateMap(workorderObj.getGwoMeta(), ReportConstants.PROJECT_ID)) {
				logger.info("inside method updateStatusAndPersistReport ");
				Map<String, Object> payLoadJsonMap = new HashMap<>();
				payLoadJsonMap.put(ReportConstants.PROJECT_ID, workorderObj.getGwoMeta().get(ReportConstants.PROJECT_ID));
				payLoadJsonMap.put(ReportConstants.KEY_REPORT_ID, analyticsId);
				payLoadJsonMap.put(ReportConstants.KEY_FLOOR_NAME, workorderObj.getGwoMeta().get(ReportConstants.KEY_FLOOR_NAME));
				payLoadJsonMap.put(ReportConstants.KEY_BUILDING_NAME, workorderObj.getGwoMeta().get(ReportConstants.KEY_BUILDING_NAME));
				payLoadJsonMap.put(ReportConstants.KEY_TASK_NAME, workorderObj.getGwoMeta().get(ReportConstants.KEY_TASK_NAME));
				logger.info("payLoadJsonMap {}", new Gson().toJson(payLoadJsonMap));
				if (ForesightConstants.SUCCESS_JSON.equalsIgnoreCase(responseJson)) {
					HttpResponse httpresponse= InbuildingReportUtil.sendPostRequestWithoutTimeOut(
							ConfigUtils.getString(ReportConstants.NV_SSVT_SF_INTEGRATION_URL)+ workorderObj.getGwoMeta().get(ReportConstants.KEY_BUILDING_NAME),
							new StringEntity(new Gson().toJson(payLoadJsonMap), ContentType.APPLICATION_JSON));
					logger.info("http response phrase from API : {} ", httpresponse.getStatusLine().getReasonPhrase());
					logger.info("http response code from API : {} ", httpresponse.getStatusLine().getStatusCode());
				}
			}}catch(Exception e) {
			logger.info("exception inside updateStatusAndPersistReport {} ", Utils.getStackTrace(e));
		}
		logger.info("Report Created successfully");
		return response;
	}

	@Override
	public Response updateStatusAndPersistBuildingReport(Map<String, Object> jsonMap,File file,Integer analyticsId,String destinationFilePath) throws UnsupportedCharsetException, IOException {
		String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.MASTER
				+ ReportConstants.FORWARD_SLASH;
		Response response=reportService.saveFileAndUpdateStatus(analyticsId,hdfsFilePath,null, file, ReportUtil.getFileNameFromFilePath(destinationFilePath),REPORT_INSTANCE_ID);
		String responseJson = (String) response.getEntity();
		if (InbuildingReportUtil.validateMap(jsonMap, ReportConstants.PROJECT_ID)) {
			Map<String, Object> payLoadJsonMap = new HashMap<>();
			payLoadJsonMap.put(ReportConstants.PROJECT_ID, jsonMap.get(ReportConstants.PROJECT_ID));
			payLoadJsonMap.put(ReportConstants.KEY_REPORT_ID, analyticsId);
			payLoadJsonMap.put(ReportConstants.KEY_BUILDING_NAME, jsonMap.get(ReportConstants.KEY_BUILDING_NAME));
			payLoadJsonMap.put(ReportConstants.KEY_TASK_NAME, INBUILDING_TASKNAME_BUILDING_REPORT);
			logger.info("payLoadJsonMap {}", new Gson().toJson(payLoadJsonMap));
			if (ForesightConstants.SUCCESS_JSON.equalsIgnoreCase(responseJson)
					&& InbuildingReportUtil.validateMap(jsonMap, ReportConstants.KEY_BUILDING_NAME)) {
				HttpResponse httpresponse = InbuildingReportUtil.sendPostRequestWithoutTimeOut(
						ConfigUtils.getString(ReportConstants.NV_SSVT_SF_INTEGRATION_URL)
								+ jsonMap.get(ReportConstants.KEY_BUILDING_NAME),
						new StringEntity(new Gson().toJson(payLoadJsonMap), ContentType.APPLICATION_JSON));
				logger.info("http response phrase from API : {} ", httpresponse.getStatusLine().getReasonPhrase());
				logger.info("http response code from API : {} ", httpresponse.getStatusLine().getStatusCode());
			}
		}
		logger.info("Report Created successfully");
		return response;
	}
	private Map<Integer, Map<Integer, String>> getWorkorderRecipeMap(List<Integer> workOrderList) {
		Map<Integer, Map<Integer, String>> woRecipeMap = new HashMap<>();
		if (Utils.isValidList(workOrderList)) {
			for (Integer woId : workOrderList) {
				GenericWorkorder workOrder = iGenericWorkorderDao.findByPk(woId);
				Map<Integer, String> recipcellmap = getRecipeWiseCellMap(workOrder);
				logger.info("recipe cell map with workorder id {} , {}", recipcellmap, woId);
				if (recipcellmap != null && !recipcellmap.isEmpty()) {
					woRecipeMap.put(woId, recipcellmap);
				}
			}
		}
		return woRecipeMap;
	}

	private List<String[]> getDriveData(List<Integer> workorderId, Map<Integer, Map<Integer, String>> woRecipeMap)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		logger.info("inside method getDriveData {}", woRecipeMap);
		List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs();
		List<String[]> driveData = new ArrayList<>();
		for (Entry<Integer, Map<Integer, String>> map : woRecipeMap.entrySet()) {
			if (map.getValue() != null && !map.getValue().isEmpty()) {
				List<String> recipeList = getRecipeListForDriveData(map.getValue());
				driveData = reportService.getDriveDataForReport(map.getKey(), recipeList, fetchKPIList);
				if (Utils.isValidList(driveData))
				{
					break;
				}
			}
		}
		return driveData;
	}

	private void setBuildingDataToWrapper(Building building, InbuildingWrapper mainWrapper,Map<Integer, String> recipeCellMap, List<String[]> driveData, Map<String, Integer> kpiIndexMap) {
		logger.info(" Inside method setBuildingDataToWrapper {}", building);
		if (building != null) {
			mainWrapper.setBuildingId(building.getBuildingId()!=null?building.getBuildingId().toString():"-");
			mainWrapper.setBuildingName(building.getBuildingName());
			mainWrapper.setLatitude(building.getLatitude().toString());
			mainWrapper.setLongitude(building.getLongitude().toString());
			if(building.getGeographyL4()!=null&&building.getGeographyL4().getGeographyL3()!=null) {
				mainWrapper.setCity(building.getGeographyL4().getGeographyL3().getDisplayName());
			}
		}

		setMCCMNC(mainWrapper,driveData, kpiIndexMap);
		if (recipeCellMap != null) {
			for (Entry<Integer, String> recipMap : recipeCellMap.entrySet()) {
				try {
					RANDetail ranData = iranDetail.getRanDetailBySiteName(recipMap.getValue(),
							Arrays.asList(NEType.IDSC_CELL));
					if (ranData != null && ranData.getTrackingArea() != null && !ranData.getTrackingArea().isEmpty()) {
						mainWrapper.setTac(ranData.getTrackingArea());
					}
				} catch (Exception e) {
					logger.info("exception inside method getCellwiseBandwidthMap ", Utils.getStackTrace(e));
				}
			}
		}

	}

	private void setBuildingImageToWrapper(Map<String, Object> imageMap,
										   Building building) {
		List<Double[]> latLongList = new ArrayList<>();
		logger.info("inside method setDriveDataWrapper");
		try {
			if (building != null) {
				Double[] latLong = new Double[2];
				latLong[0] = building.getLongitude();
				latLong[1] = building.getLatitude();
				latLongList.add(latLong);
				LatLng centroid = new LatLng(building.getLatitude(), building.getLongitude());
				HashMap<String, BufferedImage> siteBufferedImageMap = mapImagesService.getBuildingImage(latLongList,
						building.getId().toString(), centroid);
				ImageIO.write(siteBufferedImageMap.get(building.getId().toString()), "PNG",
						new File(ConfigUtils.getString(FINAL_IMAGE_PATH) + building.getId().toString() + ".png"));
				logger.info("site bufferimagemap {}", siteBufferedImageMap);
				if (siteBufferedImageMap != null && siteBufferedImageMap.containsKey(building.getId().toString())) {
					imageMap.put("buildingImg",
							ConfigUtils.getString(FINAL_IMAGE_PATH) + building.getId().toString() + ".png");
				}
			}
		} catch (Exception e) {
			logger.error("Exception in settng buildingImage for inbuilding {}", Utils.getStackTrace(e));
		}
		logger.info("imagemap content {}", imageMap);
	}

	public Map<String, Object> getJsonDataMap(String json) {
		Map<String, Object> jsonMap = null;
		try {
			jsonMap = new ObjectMapper().readValue(json, HashMap.class);
			Integer analyticsrepoId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			AnalyticsRepository analyticrepositoryObj = analyticsRepositoryDao.findByPk(analyticsrepoId);
			logger.info("analyticsrepositoryId {} , {}  ", analyticsrepoId, analyticrepositoryObj.getReportConfig());
			String reportConfig = analyticrepositoryObj.getReportConfig() != null
					? analyticrepositoryObj.getReportConfig().replaceAll("\'", "\"")
					: null;
			Map<String, Object> configMap = new ObjectMapper().readValue(reportConfig, HashMap.class);
			logger.info("AnalyticsRepository jsonObject {} ", configMap);
			Integer workOrderId = (Integer) configMap.get(WORKORDER_ID);
			Integer buildingId = (Integer) configMap.get(NVWorkorderConstant.BUILDING_ID);
			jsonMap.put(BUILDING_ID_KEY, buildingId);
			jsonMap.put(WORKORDER_ID, workOrderId);
			jsonMap.put(ForesightConstants.ANALYTICAL_REPORT_KEY, analyticsrepoId);
		} catch (Exception e) {
			logger.info("exception inside method getJsonDataMap {}", Utils.getStackTrace(e));
		}
		return jsonMap;
	}

	private Map<Integer, List<String>> getFloorWiseRecipeMapping(GenericWorkorder workOrder) {
		Map<Integer, List<String>> unitwiseRecipeMap = new HashMap<>();
		List<Integer> unitlist = new ArrayList<>();
		Map<String, String> gwoMeta = workOrder.getGwoMeta();
		String json = gwoMeta.get("recipeUnitIdMap");
		Map<String, Integer> recipeUnitIdMap = new Gson().fromJson(json, new TypeToken<Map<String, Integer>>() {
		}.getType());
		if (recipeUnitIdMap != null && !recipeUnitIdMap.isEmpty()) {
			for (Entry<String, Integer> map : recipeUnitIdMap.entrySet()) {
				if (unitwiseRecipeMap.containsKey(map.getValue())) {
					List<String> oldData = unitwiseRecipeMap.get(map.getValue());
					oldData.add(map.getKey());
					unitwiseRecipeMap.put(map.getValue(), oldData);
				} else {
					List<String> recipe = new ArrayList<>();
					recipe.add(map.getKey());
					unitwiseRecipeMap.put(map.getValue(), recipe);
				}
			}
		}
		logger.info("unit wise recipeMap {}", unitwiseRecipeMap);
		if (unitwiseRecipeMap != null && !unitwiseRecipeMap.isEmpty()) {
			unitlist = getUnitIdList(unitwiseRecipeMap);
			return getFloorWiseRecipeMap(unitwiseRecipeMap, unitlist);
		}
		return null;
	}

	private Map<Integer, List<String>> getFloorWiseRecipeMap(Map<Integer, List<String>> unitwiseRecipeMap,
															 List<Integer> unitIdList) {
		Map<Integer, List<String>> floorWiseRecipeMap = new HashMap<>();
		if (unitwiseRecipeMap != null && !unitwiseRecipeMap.isEmpty() && Utils.isValidList(unitIdList)) {
			for (Integer unitId : unitIdList) {
				Unit unit = iUnitDao.findByPk(unitId);
				Floor floor = unit.getFloor();
				if (unitwiseRecipeMap.containsKey(unitId)) {
					floorWiseRecipeMap.put(floor.getId(), unitwiseRecipeMap.get(unitId));
				}
			}
		}
		logger.info("floor wise recipeMap {}", floorWiseRecipeMap);
		return floorWiseRecipeMap;
	}

	private void setFloorWiseData(Map<Integer, List<String>> floorwiseRecipeMap, Map<Integer, String> recipeWiseCellMap,
								  Integer woID, List<String> kpiList, Map<String, Integer> kpiIndexMap,
								  List<FloorWiseWrapper> floorWiseDataList, InbuildingWrapper mainWrapper,
								  Map<String, String> cellwiseBandwidthMap, Map<String, String> cellstatusMap) {
		logger.info("Inside method setFloorWiseData");
		Map<Integer, String> overallStatusMap = new HashMap<>();
		try {
			if (floorwiseRecipeMap != null && !floorwiseRecipeMap.isEmpty()) {
				for (Entry<Integer, List<String>> map : floorwiseRecipeMap.entrySet()) {
					Map<Integer, String[]> recipeWisedriveData = getRecipeWiseSummaryData(woID, map.getValue(),
							kpiList);
					Floor floor = ifloordao.findByPk(map.getKey());
					mainWrapper.setFloorName(floor.getFloorName());
					Map<String,Integer>  recipePciMap = getRecipePciMap(woID);
					getBandwidthWiseData(recipeWisedriveData, kpiIndexMap, recipeWiseCellMap, floorWiseDataList,
							floor.getFloorName(), cellwiseBandwidthMap, overallStatusMap,recipePciMap);
				}
			}
			getOverallCellStatusMap(overallStatusMap, recipeWiseCellMap, cellstatusMap);
		} catch (Exception e) {
			logger.info("exception in method setFloorWiseData {}", Utils.getStackTrace(e));
		}
	}

	private void getBandwidthWiseData(Map<Integer,String[]> recipeWiseDriveData,
									  Map<String, Integer> kpiIndexMap, Map<Integer, String> recipeWiseCellMap,
									  List<FloorWiseWrapper> floorWiseDataList, String floorName, Map<String, String> cellwiseBandwidthMap,
									  Map<Integer, String> overallStatusMap, Map<String, Integer> recipePciMap) {
		Map<Integer, String[]> recipeDataForFiveBandwidth = new HashMap<>();
		Map<Integer, String[]> recipeDataForTwentyBandwidth = new HashMap<>();
		logger.info("Inside method getBandwidthWiseData  with recipeWiseDriveData size {}",
				recipeWiseDriveData != null ? recipeWiseDriveData.size() : null);
		Map<Integer, String> recipeBandwidthMap = getRecipeBandwidthMap(recipeWiseCellMap, cellwiseBandwidthMap);
		if (recipeWiseDriveData != null && !recipeWiseDriveData.isEmpty()) {
			for (Entry<Integer, String[]> map : recipeWiseDriveData.entrySet()) {
				if (recipeBandwidthMap.containsKey(map.getKey())) {
					if (recipeBandwidthMap.get(map.getKey()).contains("5")) {
						recipeDataForFiveBandwidth.put(map.getKey(), map.getValue());
					}
					if (recipeBandwidthMap.get(map.getKey()).contains("20")) {
						recipeDataForTwentyBandwidth.put(map.getKey(), map.getValue());
					}
				}
			}
		}
		logger.info(
				"Inside method getBandwidthWiseData with recipeDataForFiveBandwidth {}, recipeDataForTwentyBandwidth {}",
				recipeDataForFiveBandwidth, recipeDataForTwentyBandwidth);
		//////////////////////////////////////////////////////////////////////////////
		setFloorDataBandWise(recipeDataForFiveBandwidth, recipeDataForTwentyBandwidth, recipeWiseCellMap, kpiIndexMap,
				floorWiseDataList, floorName, overallStatusMap,recipePciMap);
		//setOverallStatusForCell(recipeWiseDriveData,overallStatusMap);
	}

	private void setFloorDataBandWise(Map<Integer,String[]> recipeDataForFiveBandwidth,
									  Map<Integer, String[]> recipeDataForTwentyBandwidth, Map<Integer, String> recipeWiseCellMap,
									  Map<String, Integer> kpiIndexMap, List<FloorWiseWrapper> floorWiseDataList, String floorName,
									  Map<Integer, String> overallStatusMap, Map<String, Integer> recipePciMap) {
		logger.info("inside method setFloorDataBandWise");
		if (recipeDataForFiveBandwidth != null && !recipeDataForFiveBandwidth.isEmpty()) {
			FloorWiseWrapper floorwisewrapper = new FloorWiseWrapper();
			floorwisewrapper.setFloorName(floorName);
			floorwisewrapper.setCriteriaPeakDL(">20");
			floorwisewrapper.setCriteriaPeakUL(">7");
			floorwisewrapper.setCriteriaJitter("<40");
			floorwisewrapper.setCriteriaLatency("<50");
			floorwisewrapper.setBandwidth("5 MHz");
			floorwisewrapper.setList(setDriveDataToWrapper(recipeDataForFiveBandwidth, recipeWiseCellMap, kpiIndexMap,
					InbuildingReportUtil.getFiveBandwidthCriteriaMap(), overallStatusMap,recipePciMap));
			floorWiseDataList.add(floorwisewrapper);
		}
		if (recipeDataForTwentyBandwidth != null && !recipeDataForTwentyBandwidth.isEmpty()) {
			FloorWiseWrapper floorwisewrapper = new FloorWiseWrapper();
			floorwisewrapper.setFloorName(floorName);
			floorwisewrapper.setCriteriaPeakDL(">80");
			floorwisewrapper.setCriteriaPeakUL(">25");
			floorwisewrapper.setCriteriaJitter("<40");
			floorwisewrapper.setCriteriaLatency("<50");
			floorwisewrapper.setBandwidth("20 MHz");
			floorwisewrapper.setList(setDriveDataToWrapper(recipeDataForTwentyBandwidth, recipeWiseCellMap, kpiIndexMap,
					InbuildingReportUtil.getTwentyBandwidthCriteriaMap(), overallStatusMap,recipePciMap));
			floorWiseDataList.add(floorwisewrapper);
		}
	}

	private List<InbuildingDataWrapper> setDriveDataToWrapper(Map<Integer, String[]> recipeWiseDriveData,
															  Map<Integer, String> recipeWiseCellMap, Map<String, Integer> summarykpiIndexMap, Map<String, Double> criteriaMap,
															  Map<Integer, String> overallStatusMap,Map<String,Integer> recipePciMap) {
		List<InbuildingDataWrapper> inBuildingDataWrapperList = new ArrayList<>();
		Map<Integer, String> recipeEarfcnMap = getRecipeEarfcnMap(recipeWiseCellMap);
		if (recipeWiseDriveData != null && !recipeWiseDriveData.isEmpty()) {
			try {
				for (Entry<Integer, String[]> map : recipeWiseDriveData.entrySet()) {
					if (map.getValue()!=null && map.getValue().length>0 ) {
						List<String> statusList = new ArrayList<>();
						InbuildingDataWrapper inbuildingDataWrapper = new InbuildingDataWrapper();
						String[] summaryarray = map.getValue();
						if (summarykpiIndexMap.containsKey(RSRP)
								&& ReportUtil.checkIndexValue(summarykpiIndexMap.get(RSRP), map.getValue())) {
							Double value = ReportUtil.round(Double.parseDouble(summaryarray[summarykpiIndexMap.get(RSRP)]), TWO_DECIMAL_PLACES);
							inbuildingDataWrapper.setAvgRSRP(value.toString());
						}
						if (summarykpiIndexMap.containsKey(SINR)
								&& ReportUtil.checkIndexValue(summarykpiIndexMap.get(SINR), map.getValue())) {
							Double value = ReportUtil.round(Double.parseDouble(summaryarray[summarykpiIndexMap.get(SINR)]), TWO_DECIMAL_PLACES);
							inbuildingDataWrapper.setAvgSINR(value.toString());
						}
						if (summarykpiIndexMap.containsKey(RSRQ)
								&& ReportUtil.checkIndexValue(summarykpiIndexMap.get(RSRQ), map.getValue())) {
							Double value = ReportUtil.round(Double.parseDouble(summaryarray[summarykpiIndexMap.get(RSRQ)]), TWO_DECIMAL_PLACES);
							inbuildingDataWrapper.setAvgRSRQ(value.toString());
						}
						if (summarykpiIndexMap.containsKey(DL_THROUGHPUT)
								&& ReportUtil.checkIndexValue(summarykpiIndexMap.get(DL_THROUGHPUT), map.getValue())) {
							Double value = ReportUtil.round(Double.parseDouble(summaryarray[summarykpiIndexMap.get(DL_THROUGHPUT)]), TWO_DECIMAL_PLACES);
							inbuildingDataWrapper.setAvgDL(value.toString());
						}
						if (summarykpiIndexMap.containsKey(UL_THROUGHPUT)
								&& ReportUtil.checkIndexValue(summarykpiIndexMap.get(UL_THROUGHPUT), map.getValue())) {
							Double value = ReportUtil.round(Double.parseDouble(summaryarray[summarykpiIndexMap.get(UL_THROUGHPUT)]), TWO_DECIMAL_PLACES);
							inbuildingDataWrapper.setAvgUl(value.toString());
						}
						if (summarykpiIndexMap.containsKey(MAX_UNDERSCORE+DL_THROUGHPUT)
								&& ReportUtil.checkIndexValue(summarykpiIndexMap.get(MAX_UNDERSCORE+DL_THROUGHPUT), map.getValue())) {
							Double peakDl =  ReportUtil.round(Double.parseDouble(summaryarray[summarykpiIndexMap.get(MAX_UNDERSCORE+DL_THROUGHPUT)]),TWO_DECIMAL_PLACES);
							if (peakDl != null) {
								inbuildingDataWrapper.setPeakDl(InbuildingReportUtil.addPassFailStatusToResult(
										ReportUtil.round(peakDl, TWO_DECIMAL_PLACES), criteriaMap.get(DL_THROUGHPUT),
										GREATER_THAN, statusList));
							}
						}
						if (summarykpiIndexMap.containsKey(MAX_UNDERSCORE+UL_THROUGHPUT)
								&& ReportUtil.checkIndexValue(summarykpiIndexMap.get(MAX_UNDERSCORE+UL_THROUGHPUT), map.getValue())) {
							Double peakUl =  ReportUtil.round(Double.parseDouble(summaryarray[summarykpiIndexMap.get(MAX_UNDERSCORE+UL_THROUGHPUT)]),TWO_DECIMAL_PLACES);
							if (peakUl != null) {
								inbuildingDataWrapper.setPeakUl(InbuildingReportUtil.addPassFailStatusToResult(
										ReportUtil.round(peakUl, TWO_DECIMAL_PLACES), criteriaMap.get(UL_THROUGHPUT),
										GREATER_THAN, statusList));
							}
						}
						if (summarykpiIndexMap.containsKey(MIN_LATENCY_LIST)
								&& summaryarray.length > summarykpiIndexMap.get(MIN_LATENCY_LIST)
								&& summaryarray[summarykpiIndexMap.get(MIN_LATENCY_LIST)] != null
								&& !summaryarray[summarykpiIndexMap.get(MIN_LATENCY_LIST)].isEmpty()) {
							logger.info("going to set min latency {}",summaryarray[summarykpiIndexMap.get(MIN_LATENCY_LIST)]);
							String[] latencies =summaryarray[summarykpiIndexMap.get(MIN_LATENCY_LIST)].split(Symbol.UNDERSCORE_STRING);
							OptionalDouble value = Arrays.asList(latencies).stream().mapToDouble(x -> Double.parseDouble(x)).sorted().limit(5).average();
							if(value!=null) {
								inbuildingDataWrapper.setLatency(InbuildingReportUtil.addPassFailStatusToResult(ReportUtil.round(value.getAsDouble(),TWO_DECIMAL_PLACES),
										criteriaMap.get(LATENCY), LESS_THAN_SYMBOL, statusList));
							}
						}
						if (summarykpiIndexMap.containsKey(JITTER)
								&& ReportUtil.checkIndexValue(summarykpiIndexMap.get(JITTER), map.getValue())) {
							Double value =  ReportUtil.round(Double.parseDouble(summaryarray[summarykpiIndexMap.get(JITTER)]),TWO_DECIMAL_PLACES);
							inbuildingDataWrapper.setJitter(InbuildingReportUtil.addPassFailStatusToResult(value,
									criteriaMap.get(JITTER), LESS_THAN_SYMBOL, statusList));
						}
						setSmallCellData(recipeWiseCellMap, map.getKey(), inbuildingDataWrapper);
						setEarfcnData(inbuildingDataWrapper,map.getKey(),recipeEarfcnMap);
						setPciData(inbuildingDataWrapper, map.getKey(),recipePciMap, summaryarray, summarykpiIndexMap);
						inBuildingDataWrapperList.add(inbuildingDataWrapper);
						setOverallStatusMap(overallStatusMap, map, statusList);
					}else {
						overallStatusMap.put(map.getKey(), FAIL);
					}
				}
			}catch(Exception e) {
				logger.info("exception inside method setDriveDataToWrapper {}", Utils.getStackTrace(e));
			}
		}
		return inBuildingDataWrapperList;
	}

	private void setOverallStatusMap(Map<Integer, String> overallStatusMap, Entry<Integer, String[]> map,
									 List<String> statusList) {
		if (statusList.contains(FAIL)) {
			overallStatusMap.put(map.getKey(), FAIL);
		}
		if(statusList.isEmpty()) {
			overallStatusMap.put(map.getKey(), FAIL);
		}
		if(!statusList.isEmpty() && !statusList.contains(FAIL)) {
			overallStatusMap.put(map.getKey(), PASS);
		}
	}

	private void setMainWrapperData(InbuildingWrapper wrapper, GenericWorkorder workOrder) {
		setTesterName(wrapper, workOrder);
		wrapper.setTestDate(ReportUtils.formatDate(workOrder.getModificationTime(), DATE_FORMAT_DD_MM_YYYY));
		wrapper.setTechnology(workOrder.getGwoMeta().get(ReportConstants.TECHNOLOGY));

	}

	private void setMainWrapperBuildingData(InbuildingWrapper wrapper, AnalyticsRepository analyticsrepository,
											String Assignto, List<String[]> driveData, Map<String, Integer> kpiIndexMap,
											InbuildingWrapper mainWrapper) {
		wrapper.setTestEngineer(Assignto);
		wrapper.setTestDate(ReportUtils.formatDate(analyticsrepository.getModifiedTime(), DATE_FORMAT_DD_MM_YYYY));
		if (Utils.isValidList(driveData)) {
			if (kpiIndexMap.containsKey(ReportConstants.NETWORK_TYPE)) {
				mainWrapper.setTechnology(InbuildingReportUtil.getDistinctKpiValues(driveData,
						kpiIndexMap.get(ReportConstants.NETWORK_TYPE)));
			}
		}
	}

	private void setSmallCellData(Map<Integer, String> recipeWiseCellMap, Integer recipeId,
								  InbuildingDataWrapper inbuildingDataWrapper) {
		if (recipeWiseCellMap.containsKey(recipeId)) {
			inbuildingDataWrapper.setCellId(recipeWiseCellMap.get(recipeId));
		}
	}

	private Response updateStatus(Integer analyticsrepoId, File file) {
		if (file != null) {
			String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.MASTER
					+ ReportConstants.FORWARD_SLASH;
			return reportService.saveFile(analyticsrepoId, hdfsFilePath, file);
		} else {
			analyticsRepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Data is Not Available",
					progress.Failed, null);
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}

	private List<String> getRecipeListForDriveData(Map<Integer, String> recipecellMap) {
		logger.info("inside method getRecipeList {} ", recipecellMap);
		List<String> recipeIdList = new ArrayList<String>();
		if (recipecellMap != null && !recipecellMap.isEmpty()) {
			for (Entry<Integer, String> map : recipecellMap.entrySet()) {
				recipeIdList.add(map.getKey().toString());
			}
		}
		return recipeIdList;
	}

	private Map<Integer, String> getRecipeWiseCellMap(GenericWorkorder workOrder) {
		Map<String, String> map = new HashMap<>();
		Map<Integer, String> recipecellMap = new HashMap<>();
		Map<String, String> gwoMeta = workOrder.getGwoMeta();
		String jsonmap = gwoMeta.get("recipeCellNameMap");
		if (jsonmap != null) {
			map = new Gson().fromJson(jsonmap, new TypeToken<Map<String, String>>() {
			}.getType());
		}
		if (map != null && !map.isEmpty()) {
			for (Entry<String, String> rcmap : map.entrySet()) {
				recipecellMap.put(Integer.parseInt(rcmap.getKey()), rcmap.getValue());
			}
		}
		logger.info("recipecellMap : {}", recipecellMap);
		return recipecellMap;
	}

	private Building getBuildingFromWorkOrder(Integer buildingId) {
		logger.info("getting building id: {}", buildingId);
		if (buildingId != null) {
			return buildingDataDao.findByPk(buildingId);
		}
		return null;
	}

	private List<Integer> getUnitIdList(Map<Integer, List<String>> unitwiseRecipeMap) {
		List<Integer> unitIdList = new ArrayList<>();
		if (unitwiseRecipeMap != null && !unitwiseRecipeMap.isEmpty()) {
			for (Entry<Integer, List<String>> map : unitwiseRecipeMap.entrySet()) {
				unitIdList.add(map.getKey());
			}
		}
		return unitIdList;
	}

	private void setTesterName(InbuildingWrapper wrapper, GenericWorkorder workOrder) {
		String name = "-";
		if (workOrder.getAssignedTo() != null) {
			if (workOrder.getAssignedTo().getFirstName() != null
					&& !workOrder.getAssignedTo().getFirstName().isEmpty()) {
				name = workOrder.getAssignedTo().getFirstName().toString();
				if (workOrder.getAssignedTo().getLastName() != null) {
					name = name + " " + workOrder.getAssignedTo().getLastName().toString();
				}
			}
		}
		wrapper.setTestEngineer(name);
	}

	private Map<String, String> getCellwiseBandwidthMap(Map<Integer, String> recipeWiseCellMap) {
		Map<String, String> cellwiseBandwidthMap = new HashMap<>();
		for (Entry<Integer, String> recipeCellMap : recipeWiseCellMap.entrySet()) {
			try {
				RANDetail ranData = iranDetail.getRanDetailBySiteName(recipeCellMap.getValue(),
						Arrays.asList(NEType.IDSC_CELL));
				if (ranData != null) {
					cellwiseBandwidthMap.put(recipeCellMap.getValue(), ranData.getBandwidth());
				}
			} catch (Exception e) {
				logger.info("exception inside method getCellwiseBandwidthMap ", Utils.getStackTrace(e));
			}
		}
		logger.info("cellwiseBandwidthMap {} ", cellwiseBandwidthMap);
		return cellwiseBandwidthMap;
	}

	private Map<Integer, String> getRecipeBandwidthMap(Map<Integer, String> recipeWiseCellMap,
													   Map<String, String> cellwiseBandwidthMap) {
		Map<Integer, String> recipeBandwidthMap = new HashMap<>();
		if (recipeWiseCellMap != null && !recipeWiseCellMap.isEmpty()) {
			for (Entry<Integer, String> map : recipeWiseCellMap.entrySet()) {
				if (cellwiseBandwidthMap.containsKey(map.getValue())) {
					recipeBandwidthMap.put(map.getKey(),
							cellwiseBandwidthMap.get(map.getValue()) != null ? cellwiseBandwidthMap.get(map.getValue())
									: "20");
				}
			}
		}
		logger.info("recipeBandwidthMap {} ", recipeBandwidthMap);
		return recipeBandwidthMap;
	}

	private void getOverallCellStatusMap(Map<Integer, String> recipeStatusMap, Map<Integer, String> recipeCellMap,
										 Map<String, String> cellstatusMap) {
		if (recipeStatusMap != null && !recipeStatusMap.isEmpty()) {
			for (Entry<Integer, String> recipeStatus : recipeStatusMap.entrySet()) {
				if (recipeCellMap.containsKey(recipeStatus.getKey())) {
					cellstatusMap.put(recipeCellMap.get(recipeStatus.getKey()), recipeStatus.getValue());
				}
			}
		}
	}

	private void updateIntoGWOMeta(Map<String, String> cellStatusMap, GenericWorkorder workorder, Integer woid) {
		try {
			String json = new Gson().toJson(cellStatusMap);
			GWOMeta meta = gwoMetaDao.getGwoMetaDataByGenericWorkorderIdforReport(woid, CELL_STATUS_MAP);
			if (meta != null) {
				meta = setValuesIntoGwoMeta(workorder, json, meta, CELL_STATUS_MAP);
				gwoMetaDao.update(meta);
			} else {
				meta = setValuesIntoGwoMeta(workorder, json, meta, CELL_STATUS_MAP);
				gwoMetaDao.create(meta);
			}
		} catch (Exception e) {
			logger.info("Exception in update in overall status", Utils.getStackTrace(e));
		}
	}

	private GWOMeta setValuesIntoGwoMeta(GenericWorkorder genericWorkOrderId, String value, GWOMeta geoMeta,
										 String key) {
		GWOMeta geoMetafinal = geoMeta != null ? geoMeta : new GWOMeta();
		geoMetafinal.setGenericWorkOrder(genericWorkOrderId);
		geoMetafinal.setEntityType(key);
		geoMetafinal.setEntityValue(value);
		return geoMetafinal;
	}

	private void setProjectIdFloorName(Map<String, Object> jsonMap, GenericWorkorder workorder) {
		Map<String, String> gwoMeta = workorder.getGwoMeta();
		String projectId = gwoMeta.get(ReportConstants.PROJECT_ID);
		String floorName = gwoMeta.get(ReportConstants.KEY_FLOOR_NAME);
		String taskName = gwoMeta.get(ReportConstants.KEY_TASK_NAME);
		jsonMap.put(ReportConstants.PROJECT_ID, projectId);
		jsonMap.put(ReportConstants.KEY_FLOOR_NAME, floorName);
		jsonMap.put(ReportConstants.KEY_TASK_NAME, taskName);

	}

	private Map<Integer, String[]> getRecipeWiseSummaryData(Integer workorderId, List<String> recipeidList,
															List<String> columnList) {
		Map<Integer, String[]> recipeWiseSummaryMap = new HashMap<>();
		logger.info("recipeId list: {}", recipeidList);
		for (String recipe : recipeidList) {
			String[] summaryData = reportService.getSummaryDataForReport(workorderId,
					Arrays.asList(recipe), columnList);
			recipeWiseSummaryMap.put(Integer.parseInt(recipe), summaryData);
		}
		return recipeWiseSummaryMap;
	}

	private Map<String,Integer> getRecipePciMap(Integer workorderID) {
		Map<String,Integer> map = new HashMap<>();
		GenericWorkorder workOrder = iGenericWorkorderDao.findByPk(workorderID);
		Map<String, String> gwoMeta = workOrder.getGwoMeta();
		String jsonmap = gwoMeta.get(RECIPE_PCI_MAP);
		if (jsonmap != null) {
			map = new Gson().fromJson(jsonmap, new TypeToken<Map<String, Integer>>() {
			}.getType());
		}
		return map;
	}

	private Map<Integer, String> getRecipeEarfcnMap(Map<Integer, String> recipeCellMap) {
		Map<Integer, String> recipeEarfcnMap = new HashMap<>();
		for (Entry<Integer, String> map : recipeCellMap.entrySet()) {
			try {
				RANDetail ranData = iranDetail.getRanDetailBySiteName(map.getValue(), Arrays.asList(NEType.IDSC_CELL));
				if (ranData != null) {
					recipeEarfcnMap.put(map.getKey(),
							ranData.getDlearfcn() != null ? ranData.getDlearfcn().toString() : "-");
				}
			} catch (Exception e) {
				logger.info("exception inside method getCellwiseBandwidthMap ", Utils.getStackTrace(e));
			}
		}
		logger.info("recipeEarfcnMap {}", recipeEarfcnMap);
		return recipeEarfcnMap;
	}

	private void setEarfcnData(InbuildingDataWrapper inbuildingDataWrapper, Integer recipeId,
							   Map<Integer, String> recipeEarfcnMap) {
		if (recipeEarfcnMap.containsKey(recipeId)) {
			inbuildingDataWrapper.setEarfcn(recipeEarfcnMap.get(recipeId));
		}
	}

	private void setPciData(InbuildingDataWrapper inbuildingDataWrapper, Integer recipeId,
							Map<String, Integer> recipePciMap, String[] summary, Map<String, Integer> kpiIndexMap) {
		if (recipePciMap.containsKey(recipeId.toString())) {
			inbuildingDataWrapper.setPlannedPci(recipePciMap.get(recipeId.toString()).toString());
		}
		if (kpiIndexMap.containsKey(PCI) && ReportUtil.checkIndexValue(kpiIndexMap.get(PCI), summary)) {
			inbuildingDataWrapper.setActualPci(summary[kpiIndexMap.get(PCI)]);
		}
	}

	private void setMCCMNC(InbuildingWrapper mainWrapper, List<String[]> driveData, Map<String, Integer> kpiIndexMap) {
		String mcc = "";
		String mnc = "";
		logger.info("inside method setMCCMNC");

		Map<String, String> collect = driveData.stream().filter(a -> a[kpiIndexMap.get(MCC)] != null && a[kpiIndexMap.get(MNC)] != null)
				.collect(Collectors.toMap(a -> a[kpiIndexMap.get(MCC)], a -> a[kpiIndexMap.get(MNC)]));

		for(Entry<String, String> e: collect.entrySet()){
			mcc = e.getKey();
			mnc = e.getValue();
		}
		mainWrapper.setMccmnc(mcc + FORWARD_SLASH + mnc);
	}
}
