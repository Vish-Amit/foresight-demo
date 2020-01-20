package com.inn.foresight.module.nv.report.service.inbuilding.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.io.FileUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.LatLng;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IBuildingDataDao;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.report.utils.ReportUtils;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.utils.NetworkDataFormats;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.HandoverDataWrapper;
import com.inn.foresight.module.nv.report.ib.utils.IBReportUtils;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.FloorPlanJsonParser;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.INVInBuildingReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.Inbuilding.Service.IInbuildingService;
import com.inn.foresight.module.nv.report.service.Inbuilding.Utils.InbuildingReportUtil;
import com.inn.foresight.module.nv.report.service.Inbuilding.wrapper.FloorWiseWrapper;
import com.inn.foresight.module.nv.report.service.Inbuilding.wrapper.InbuildingWrapper;
import com.inn.foresight.module.nv.report.service.impl.InBuildingHeatMapGenerator;
import com.inn.foresight.module.nv.report.service.inbuilding.service.IB1EReportService;
import com.inn.foresight.module.nv.report.service.inbuilding.wrapper.IB1EReportWrapper;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.BenchMarkOperatorInfo;
import com.inn.foresight.module.nv.report.wrapper.benchmark.VoiceStatsWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.InbuildingPointWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping.Status;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("IB1EReportServiceImpl")
public class IB1EReportServiceImpl implements IB1EReportService {
	private Logger logger = LogManager.getLogger(IB1EReportServiceImpl.class);

	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;

	@Autowired
	private IReportService reportService;

	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;

	@Autowired
	private IWORecipeMappingDao recipeMappingDao;


	@Autowired
	private ILegendRangeDao legendRangeDao;

	@Autowired
	private IMapImagesService mapImageService;
	@Autowired
	private IBuildingDataDao buildingDataDao;
	@Autowired
	private IInbuildingService inbuildingService;
	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	@Autowired
	private INVLayer3HDFSDao nvLayer3hdfsDao;

	ObjectMapper mapper = new ObjectMapper();

	@Override
	public Response createFloorLevelReport(List<Integer> workOrderIds, Integer analyticsrepoId,
			Map<String, Object> jsonMap, boolean isBuildingLevel) {
		logger.info("Inside createFloorLevelInbuildingReport {}", workOrderIds);
		String localDirPath = ConfigUtils.getString(ReportConstants.INBUILDING_REPORT_PATH) + new Date().getTime();
		ReportUtil.createDirectory(localDirPath);
		Map<String, Object> imageMap = new HashMap<>();
		try {
			List<InbuildingWrapper> dataList = new ArrayList<>();
			GenericWorkorder workorderObj = null;
			if (Utils.isValidList(workOrderIds)) {
				for (Integer workorderId : workOrderIds) {
					List<FloorWiseWrapper> floorWiseDataList = new ArrayList<>();
					InbuildingWrapper mainWrapper = new InbuildingWrapper();
					GenericWorkorder genericWorkorder = iGenericWorkorderDao.findByPk(workorderId);
					workorderObj = genericWorkorder;
					Set<String> dynamicKpis = reportService.getDynamicKpiName(workOrderIds, null,
							Layer3PPEConstant.ADVANCE);
					List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs().stream()
							.filter(k -> dynamicKpis.contains(k)).collect(Collectors.toList());
					Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
					List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
					Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper
							.getLiveDriveKPIIndexMap(fetchSummaryKPIList);
					fetchKPIList.add(ReportConstants.NEIGHBOUR_DATA);
					kpiIndexMap.put(ReportConstants.NEIGHBOUR_DATA, fetchKPIList.size() - NumberUtils.INTEGER_ONE);
					Map<String, Map<String, List<String>>> recipeWiseIDMap = getRecipWieIDMap(workorderId);
					logger.info("recipeWiseIDMap == {}", recipeWiseIDMap);
					Map<String, List<String[]>> dataMap = getDataMap(workorderId, fetchKPIList, kpiIndexMap,
							recipeWiseIDMap, fetchSummaryKPIList);
					List<KPIWrapper> kpiList = getKPIList(kpiIndexMap);
					FloorWiseWrapper floorWiseWrapper = preparePlotData(localDirPath, workorderId, kpiIndexMap,
							recipeWiseIDMap, dataMap, kpiList);
					floorWiseWrapper = prepareFloorWiseData(dataMap, kpiIndexMap, floorWiseWrapper, summaryKpiIndexMap);
					setMetaDataToWrapper(mainWrapper, genericWorkorder, kpiIndexMap, dataMap, floorWiseWrapper,
							isBuildingLevel);
					jsonMap.put(ReportConstants.KEY_BUILDING_NAME, mainWrapper.getBuildingId());
					floorWiseDataList.add(floorWiseWrapper);

					mainWrapper.setTestEngineer(jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY) != null
							? jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY).toString()
							: null);
					mainWrapper.setDataList(floorWiseDataList);
					dataList.add(mainWrapper);
				}

				IB1EReportWrapper dataWrapper = prepaerMainWrapper(dataList);
				setBuildingImageToWrapper(imageMap, dataWrapper);

				localDirPath = localDirPath + Symbol.SLASH_FORWARD_STRING;
				String destinationFilePath = getFileName(analyticsrepoId, isBuildingLevel, localDirPath, dataWrapper,
						workorderObj);

				File file = proceedToCreatReport(dataWrapper, imageMap, destinationFilePath);
				if (isBuildingLevel) {
					return inbuildingService.updateStatusAndPersistBuildingReport(jsonMap, file, analyticsrepoId,
							destinationFilePath);
				} else {
					return inbuildingService.updateStatusAndPersistReport(workorderObj, jsonMap, file, analyticsrepoId,
							destinationFilePath);
				}
			}
		} catch (Exception e) {
			logger.info("Exception in createFloorLevelInbuildingReport {}", Utils.getStackTrace(e));
		} finally {
			try {
				FileUtils.deleteDirectory(new File(localDirPath));
			} catch (IOException e) {
				logger.error("Error in deleting file {}", localDirPath);
			}
		}
		return null;
	}

	private IB1EReportWrapper prepaerMainWrapper(List<InbuildingWrapper> dataList) {
		IB1EReportWrapper mainWrapper = new IB1EReportWrapper();
		if (Utils.isValidList(dataList)) {
			mainWrapper.setBuildingId(dataList.get(ReportConstants.INDEX_ZER0).getBuildingId());
			mainWrapper.setTechnology(dataList.get(ReportConstants.INDEX_ZER0).getTechnology());
			mainWrapper.setTestDate(dataList.get(ReportConstants.INDEX_ZER0).getTestDate());
			mainWrapper.setCity(dataList.get(ReportConstants.INDEX_ZER0).getCity());
			mainWrapper.setBuildingName(dataList.get(ReportConstants.INDEX_ZER0).getBuildingName());
			mainWrapper.setFloorName(dataList.get(ReportConstants.INDEX_ZER0).getFloorName());
			//
			mainWrapper.setLatitude(dataList.get(ReportConstants.INDEX_ZER0).getLatitude());
			mainWrapper.setLongitude(dataList.get(ReportConstants.INDEX_ZER0).getLongitude());
			mainWrapper.setMccmnc(dataList.get(ReportConstants.INDEX_ZER0).getMccmnc());
			mainWrapper.setEarfcn(dataList.get(ReportConstants.INDEX_ZER0).getEarfcn());
			mainWrapper.setTac(dataList.get(ReportConstants.INDEX_ZER0).getTac());
		}
		mainWrapper.setFloorWiseList(dataList);
		return mainWrapper;

	}

	private String getFileName(Integer analyticsrepoId, boolean isBuildingLevel, String localDirPath,
			IB1EReportWrapper dataWrapper, GenericWorkorder workorderObj) {
		String destinationFilePath = ReportUtil.getFileName(workorderObj.getWorkorderId(), analyticsrepoId,
				localDirPath);
		try {
			if (isBuildingLevel) {
				destinationFilePath = ReportUtil.getFileName(dataWrapper.getBuildingId() + "IBS", analyticsrepoId,
						localDirPath);
			} else {
				destinationFilePath = ReportUtil
						.getFileName(
								dataWrapper.getBuildingId() + ReportConstants.UNDERSCORE + "IBS"
										+ ReportConstants.UNDERSCORE + dataWrapper.getFloorName(),
								analyticsrepoId, localDirPath);
			}
			return destinationFilePath;
		} catch (Exception e) {
			logger.error("Exception inside The method getFileName {}", Utils.getStackTrace(e));
		}
		return destinationFilePath;
	}

	private void setMetaDataToWrapper(InbuildingWrapper mainWrapper, GenericWorkorder genericWorkorder,
			Map<String, Integer> kpiIndexMap, Map<String, List<String[]>> dataMap, FloorWiseWrapper floorWiseWrapper,
			boolean isBuildingLevel) {
		if (genericWorkorder.getGwoMeta() != null) {
			if (genericWorkorder.getGwoMeta().containsKey(NVWorkorderConstant.FLOOR_NAME)) {
				mainWrapper.setFloorName(genericWorkorder.getGwoMeta().get(NVWorkorderConstant.FLOOR_NAME));
				floorWiseWrapper.setFloorName(genericWorkorder.getGwoMeta().get(NVWorkorderConstant.FLOOR_NAME));
			}
			if (genericWorkorder.getGwoMeta().containsKey(NVWorkorderConstant.BUILDING_ID)) {
				Building building = buildingDataDao
						.findByPk(Integer.parseInt(genericWorkorder.getGwoMeta().get(NVWorkorderConstant.BUILDING_ID)));
				setBuildingDataToWrapper(building, mainWrapper, dataMap.get(ReportConstants.KEY_DATA), kpiIndexMap);
				if (!isBuildingLevel) {
					mainWrapper.setWorkorderName(genericWorkorder.getWorkorderId());
					mainWrapper.setIsFloorLevel(true);

				} else {
					mainWrapper.setIsFloorLevel(false);
					if (building != null) {
						mainWrapper.setWorkorderName(building.getBuildingId());
					}

				}
			}

			mainWrapper.setTestDate(ReportUtils.formatDate(genericWorkorder.getModificationTime(),
					ReportConstants.DATE_FORMAT_DD_MM_YYYY));

		}
	}

	private FloorWiseWrapper preparePlotData(String localDirPath, Integer workorderId, Map<String, Integer> kpiIndexMap,
			Map<String, Map<String, List<String>>> recipeWiseIDMap, Map<String, List<String[]>> dataMap,
			List<KPIWrapper> kpiList) throws IOException {
		FloorWiseWrapper floorWiseWrapper = new FloorWiseWrapper();
		try {
			List<BenchMarkOperatorInfo> imgKpiList = new ArrayList<>();
			creatPlotForDlData(localDirPath, workorderId, kpiIndexMap, recipeWiseIDMap, dataMap, kpiList, imgKpiList);
			creatPlotForUlData(localDirPath, workorderId, kpiIndexMap, recipeWiseIDMap, dataMap, kpiList, imgKpiList);
			creatPlotForVolteData(localDirPath, workorderId, kpiIndexMap, recipeWiseIDMap, dataMap, kpiList,
					imgKpiList);
			floorWiseWrapper.setImgKpiList(imgKpiList);
		} catch (NumberFormatException e) {
			logger.error("NumberFormatException inside the method preparePlotData {}", Utils.getStackTrace(e));
		} catch (DaoException e) {
			logger.error("DaoException inside the method preparePlotData {}", Utils.getStackTrace(e));

		} catch (Exception e) {
			logger.error("Exception inside the method preparePlotData {}", Utils.getStackTrace(e));

		}
		return floorWiseWrapper;
	}

	private void creatPlotForVolteData(String localDirPath, Integer workorderId, Map<String, Integer> kpiIndexMap,
			Map<String, Map<String, List<String>>> recipeWiseIDMap, Map<String, List<String[]>> dataMap,
			List<KPIWrapper> kpiList, List<BenchMarkOperatorInfo> imgKpiList) throws IOException {
		String temFilePath = localDirPath + Symbol.SLASH_FORWARD_STRING + new Date().getTime()
				+ Symbol.SLASH_FORWARD_STRING + ReportConstants.KEY_VOLTE;
		ReportUtil.createDirectory(temFilePath);
		logger.info("inside the method preparePlotData localDirPath {}", localDirPath);
		InbuildingPointWrapper pointWrapper = getInbuildingPointWrapper(workorderId, kpiIndexMap, recipeWiseIDMap,
				dataMap, temFilePath, ReportConstants.KEY_VOLTE);
		if (pointWrapper != null) {
			for (KPIWrapper kpiWrapper : kpiList) {
				try {

					if (Utils.isValidList(pointWrapper.getArlist())
							&& ReportConstants.CALL_PLOT.equalsIgnoreCase(kpiWrapper.getKpiName())) {
						Map<String, String> map = InBuildingHeatMapGenerator.getInstance().generateHeatMapsForCallData(
								pointWrapper.getArlist(), temFilePath, pointWrapper.getImageFloorPalnPath(),
								ReportConstants.CALL_PLOT, new HashMap<String, String>(),
								kpiIndexMap.get(ReportConstants.CALL_INITIATE),
								kpiIndexMap.get(ReportConstants.CALL_FAILURE),
								kpiIndexMap.get(ReportConstants.CALL_SUCCESS),
								kpiIndexMap.get(ReportConstants.CALL_DROP), kpiIndexMap.get(ReportConstants.X_POINT),
								kpiIndexMap.get(ReportConstants.Y_POINT));

						BenchMarkOperatorInfo wrapper = new BenchMarkOperatorInfo();
						wrapper.setKpiImg(map.get(kpiWrapper.getKpiName()));
						wrapper.setKpiName(kpiWrapper.getKpiName());
						wrapper.setCallPlotDataList(getCallData(dataMap.get(ReportConstants.KEY_VOLTE), kpiIndexMap));
						if (wrapper.getKpiImg() != null) {
							imgKpiList.add(wrapper);
						}

					}
				} catch (Exception e) {
					logger.error("exception inside the method creatPlotForUlData {}", Utils.getStackTrace(e));
				}
			}

		}

	}

	private InbuildingPointWrapper getInbuildingPointWrapper(Integer workorderId, Map<String, Integer> kpiIndexMap,
			Map<String, Map<String, List<String>>> recipeWiseIDMap, Map<String, List<String[]>> dataMap,
			String temFilePath, String key) throws IOException {
		try {
			if (recipeWiseIDMap.containsKey(key)) {
				String recipId = recipeWiseIDMap.get(key).get(QMDLConstant.RECIPE).get(ReportConstants.INDEX_ZER0);
				logger.info("recipId is === {}", recipId);

				if (recipId != null) {
					DriveDataWrapper driveDataWrapper = nvLayer3DashboardService.getFloorplanDataFromLayer3ReportForFramework(workorderId,
							recipId);
					WORecipeMapping mapping = recipeMappingDao.findByPk(Integer.parseInt(recipId));
					String imgFloorPlanPath = getFloorplanImg(Integer.parseInt(recipId),
							mapping.getOperator(), driveDataWrapper, driveDataWrapper.getJson(), temFilePath);
					logger.info("imgFloorPlanPath is ==={}", imgFloorPlanPath);
					InbuildingPointWrapper pointWrapper = IBReportUtils.getInstance().drawFloorPlan(imgFloorPlanPath,
							driveDataWrapper.getJson(), dataMap.get(key), kpiIndexMap.get(ReportConstants.X_POINT),
							kpiIndexMap.get(ReportConstants.Y_POINT));
					pointWrapper.setImageFloorPalnPath(imgFloorPlanPath);
					return pointWrapper;

				}
			}
		} catch (Exception e) {
			logger.error("exception inside the method getInbuildingPointWrapper {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private void creatPlotForUlData(String localDirPath, Integer workorderId, Map<String, Integer> kpiIndexMap,
			Map<String, Map<String, List<String>>> recipeWiseIDMap, Map<String, List<String[]>> dataMap,
			List<KPIWrapper> kpiList, List<BenchMarkOperatorInfo> imgKpiList) throws IOException {
		logger.info("inside the method creatPlotForUlData localDirPath {}", localDirPath);
		String temFilePath = localDirPath + Symbol.SLASH_FORWARD_STRING + new Date().getTime()
				+ Symbol.SLASH_FORWARD_STRING + ReportConstants.KEY_DATA_UL;
		ReportUtil.createDirectory(temFilePath);
		InbuildingPointWrapper pointWrapper = getInbuildingPointWrapper(workorderId, kpiIndexMap, recipeWiseIDMap,
				dataMap, temFilePath, ReportConstants.KEY_DATA_UL);
		if (pointWrapper != null) {
			for (KPIWrapper kpiWrapper : kpiList) {
				try {
					logger.info("inside the method setImageData  ====={}", kpiWrapper.getKpiName());
					if (Utils.isValidList(pointWrapper.getArlist())
							&& ReportConstants.MAC_UL_THROUGHPUT.equalsIgnoreCase(kpiWrapper.getKpiName())) {
						BenchMarkOperatorInfo wrapper = getOPeratorImgInfo(kpiIndexMap, temFilePath,
								pointWrapper.getArlist(), kpiWrapper, pointWrapper.getImageFloorPalnPath());
						if (wrapper.getKpiImg() != null) {
							imgKpiList.add(wrapper);
						}

					}
					if (ReportConstants.HANDOVER_PLOT.equalsIgnoreCase(kpiWrapper.getKpiName())
							&& Utils.isValidList(pointWrapper.getArlist())) {
						List<Double> intiateList = ReportUtil.convetArrayToList(pointWrapper.getArlist(),
								kpiIndexMap.get(ReportConstants.HANDOVER_INITIATE));
						if (Utils.isValidList(intiateList)) {
							setHandOverImage(temFilePath, kpiIndexMap, pointWrapper.getImageFloorPalnPath(), imgKpiList,
									kpiWrapper, pointWrapper.getArlist(), "UL");
						}
					}

				} catch (Exception e) {
					logger.error("exception inside the method creatPlotForUlData {}", Utils.getStackTrace(e));
				}
			}

		}
	}

	private void creatPlotForDlData(String localDirPath, Integer workorderId, Map<String, Integer> kpiIndexMap,
			Map<String, Map<String, List<String>>> recipeWiseIDMap, Map<String, List<String[]>> dataMap,
			List<KPIWrapper> kpiList, List<BenchMarkOperatorInfo> imgKpiList) throws IOException {
		logger.info("inside the method preparePlotData localDirPath {}", localDirPath);
		String temFilePath = localDirPath + Symbol.SLASH_FORWARD_STRING + new Date().getTime()
				+ Symbol.SLASH_FORWARD_STRING + ReportConstants.KEY_DATA;
		ReportUtil.createDirectory(temFilePath);
		InbuildingPointWrapper pointWrapper = getInbuildingPointWrapper(workorderId, kpiIndexMap, recipeWiseIDMap,
				dataMap, temFilePath, ReportConstants.KEY_DATA);
		if (pointWrapper != null) {
			for (KPIWrapper kpiWrapper : kpiList) {
				try {
					logger.info("inside the method setImageData {}", kpiWrapper.getKpiName());
					if (Utils.isValidList(pointWrapper.getArlist())
							&& ReportConstants.RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
						BenchMarkOperatorInfo wrapper = getOPeratorImgInfo(kpiIndexMap, temFilePath,
								pointWrapper.getArlist(), kpiWrapper, pointWrapper.getImageFloorPalnPath());
						if (wrapper.getKpiImg() != null) {

							imgKpiList.add(wrapper);
						}
					}
					if (Utils.isValidList(pointWrapper.getArlist())
							&& ReportConstants.SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
						BenchMarkOperatorInfo wrapper = getOPeratorImgInfo(kpiIndexMap, temFilePath,
								pointWrapper.getArlist(), kpiWrapper, pointWrapper.getImageFloorPalnPath());
						if (wrapper.getKpiImg() != null) {
							imgKpiList.add(wrapper);
						}

					}
					if (Utils.isValidList(pointWrapper.getArlist())
							&& ReportConstants.MAC_DL_THROUGHPUT.equalsIgnoreCase(kpiWrapper.getKpiName())) {

						BenchMarkOperatorInfo wrapper = getOPeratorImgInfo(kpiIndexMap, temFilePath,
								pointWrapper.getArlist(), kpiWrapper, pointWrapper.getImageFloorPalnPath());
						if (wrapper.getKpiImg() != null) {
							imgKpiList.add(wrapper);
						}

					}

					if (Utils.isValidList(pointWrapper.getArlist())
							&& ReportConstants.CQI.equalsIgnoreCase(kpiWrapper.getKpiName())) {
						BenchMarkOperatorInfo wrapper = getOPeratorImgInfo(kpiIndexMap, temFilePath,
								pointWrapper.getArlist(), kpiWrapper, pointWrapper.getImageFloorPalnPath());
						if (wrapper.getKpiImg() != null) {
							imgKpiList.add(wrapper);
						}

					}
					if (Utils.isValidList(pointWrapper.getArlist())
							&& ReportConstants.MIMO.equalsIgnoreCase(kpiWrapper.getKpiName())) {

						BenchMarkOperatorInfo wrapper = getOPeratorImgInfo(kpiIndexMap, temFilePath,
								pointWrapper.getArlist(), kpiWrapper, pointWrapper.getImageFloorPalnPath());
						if (wrapper.getKpiImg() != null) {
							imgKpiList.add(wrapper);
						}

					}

					if (ReportConstants.HANDOVER_PLOT.equalsIgnoreCase(kpiWrapper.getKpiName())
							&& Utils.isValidList(pointWrapper.getArlist())) {
						List<Double> intiateList = ReportUtil.convetArrayToList(pointWrapper.getArlist(),
								kpiIndexMap.get(ReportConstants.HANDOVER_INITIATE));
						if (Utils.isValidList(intiateList)) {
							setHandOverImage(temFilePath, kpiIndexMap, pointWrapper.getImageFloorPalnPath(), imgKpiList,
									kpiWrapper, pointWrapper.getArlist(), "DL");
						}

					}

					if (Utils.isValidList(pointWrapper.getArlist())
							&& ReportConstants.PCI_PLOT.equalsIgnoreCase(kpiWrapper.getKpiName())) {
						List<KPIWrapper> newKpiList = new ArrayList<>();
						newKpiList.add(kpiWrapper);
						Map<String, String> map = InBuildingHeatMapGenerator.getInstance().generateHeatMapsForReport(
								pointWrapper.getArlist(), temFilePath, pointWrapper.getImageFloorPalnPath(), newKpiList,
								kpiIndexMap);
						if (map.containsKey(kpiWrapper.getKpiName())) {
							BenchMarkOperatorInfo wrapper = new BenchMarkOperatorInfo();
							wrapper.setKpiImg(map.get(kpiWrapper.getKpiName()));
							wrapper.setKpiName(
									kpiWrapper.getKpiName().replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING));
							wrapper.setKpiLegendImg(map.get(ReportConstants.KEY_LEGENDS));
							if (wrapper.getKpiImg() != null) {
								imgKpiList.add(wrapper);
							}
						}
					}
					if (ReportConstants.DL_EARFCN.equalsIgnoreCase(kpiWrapper.getKpiName())) {
						Map<String, String> map = InBuildingHeatMapGenerator.getInstance().generateHeatMapsForReport(
								pointWrapper.getArlist(), temFilePath, pointWrapper.getImageFloorPalnPath(),
								Arrays.asList(kpiWrapper), kpiIndexMap);
						BenchMarkOperatorInfo wrapper = new BenchMarkOperatorInfo();
						wrapper.setKpiImg(map.get(kpiWrapper.getKpiName()));
						wrapper.setKpiName(
								kpiWrapper.getKpiName().replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING));
						wrapper.setKpiLegendImg(map.get(
								ReportConstants.DL_EARFCN + ReportConstants.UNDERSCORE + ReportConstants.KEY_LEGENDS));
						if (wrapper.getKpiImg() != null) {
							imgKpiList.add(wrapper);
						}

					}
				} catch (Exception e) {
					logger.error("exception inside the method creatPlotForDlData {}", Utils.getStackTrace(e));
				}
			}

		}
	}

	private void setHandOverImage(String localDirPath, Map<String, Integer> kpiIndexMap, String imgFloorPlanPath,
			List<BenchMarkOperatorInfo> imgKpiList, KPIWrapper kpiWrapper, List<String[]> dataList, String heading) {
		Map<String, String> map = InBuildingHeatMapGenerator.getInstance().generateHeatMapsForHandover(dataList,
				localDirPath, imgFloorPlanPath, kpiIndexMap, ReportConstants.HANDOVER_PLOT);

		BenchMarkOperatorInfo wrapper = new BenchMarkOperatorInfo();
		wrapper.setKpiImg(map.get(ReportConstants.HANDOVER_PLOT));
		wrapper.setKpiName(heading + Symbol.SPACE_STRING
				+ kpiWrapper.getKpiName().replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING));
		wrapper.setHoDataList(getHoData(dataList, kpiIndexMap));
		if (wrapper.getKpiImg() != null) {
			imgKpiList.add(wrapper);
		}
	}

	private Map<String, List<String[]>> getDataMap(Integer workorderId, List<String> fetchKPIList,
			Map<String, Integer> kpiIndexMap, Map<String, Map<String, List<String>>> recipeWiseIDMap,
			List<String> fetchSummaryKPIList) {
		Map<String, List<String[]>> dataMap = new HashMap<>();

		for (Entry<String, Map<String, List<String>>> entry : recipeWiseIDMap.entrySet()) {
			try {
				if (ReportConstants.KEY_SPILLAGE_TEST.equalsIgnoreCase(entry.getKey())) {
					String[] summaryData = reportService.getSummaryDataForReport(workorderId,
							entry.getValue().get(ReportConstants.RECIPE), fetchSummaryKPIList);
					if (summaryData != null) {
						List<String[]> summaryList = new ArrayList<>();
						summaryList.add(summaryData);
						dataMap.put(ReportConstants.KEY_SPILLAGE_TEST_SUMMARY, summaryList);
					}
					List<String[]> listArray = reportService.getDriveDataRecipeWiseTaggedForReport(
							Arrays.asList(workorderId), entry.getValue().get(ReportConstants.RECIPE), fetchKPIList,
							kpiIndexMap);
					dataMap.put(entry.getKey(), listArray);
				} else {
					List<String[]> listArray = reportService.getDriveDataRecipeWiseTaggedForReport(
							Arrays.asList(workorderId), entry.getValue().get(ReportConstants.RECIPE), fetchKPIList,
							kpiIndexMap);
					dataMap.put(entry.getKey(), listArray);
				}
			} catch (Exception e) {
				logger.error("Exception inside the {} ", Utils.getStackTrace(e));
			}

		}
		return dataMap;
	}
	

	private FloorWiseWrapper prepareFloorWiseData(Map<String, List<String[]>> dataMap, Map<String, Integer> kpiIndexMap,
			FloorWiseWrapper wrapper, Map<String, Integer> summaryKpiIndexMap) {
		logger.info("inside the method prepareFloorWiseData");
		try {
			for (Entry<String, List<String[]>> entry : dataMap.entrySet()) {
				if (ReportConstants.KEY_IN_OUT.equalsIgnoreCase(entry.getKey())) {
					setInOutDataToWrapper(kpiIndexMap, wrapper, entry.getValue());

				} else if (ReportConstants.KEY_SPILLAGE_TEST.equalsIgnoreCase(entry.getKey())) {
					setSpillageTestDataToWrapper(kpiIndexMap, wrapper, entry.getValue(), summaryKpiIndexMap,
							dataMap.get(ReportConstants.KEY_SPILLAGE_TEST_SUMMARY));
				} else if (ReportConstants.KEY_STAIR_CASE.equalsIgnoreCase(entry.getKey())) {
					setStairCaseDataToWrapper(kpiIndexMap, wrapper, entry.getValue());
				} else if (ReportConstants.KEY_ELEVATOR.equalsIgnoreCase(entry.getKey())) {
					setElevatorDataToWrapper(kpiIndexMap, wrapper, entry.getValue());
				} else if (ReportConstants.KEY_IDLE.equalsIgnoreCase(entry.getKey())) {
					setIdleTestDataToWrapper(kpiIndexMap, wrapper, entry.getValue());

				}

				else if (ReportConstants.KEY_DATA.equalsIgnoreCase(entry.getKey())) {
					List<String[]> dataList = new ArrayList<>();
					dataList.addAll(entry.getValue());
					dataList.addAll(dataMap.get(ReportConstants.KEY_DATA_UL));
					setDataToWrapper(kpiIndexMap, wrapper, dataList, dataMap.get(ReportConstants.KEY_IN_OUT));

				} else if (ReportConstants.KEY_VOLTE.equalsIgnoreCase(entry.getKey())) {
					setVolteTestDataToWrapper(kpiIndexMap, wrapper, entry.getValue(),
							dataMap.get(ReportConstants.KEY_IN_OUT));

				}
			}
		} catch (Exception e) {
			logger.error("Exception inside the method prepareFloorWiseData{}", Utils.getStackTrace(e));
		}
		return wrapper;
	}

	private void setDataToWrapper(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper, List<String[]> dataList,
			List<String[]> inOutDataList) {
		if (Utils.isValidList(dataList)) {
			try {
				String bandwidth = setBandWidth(kpiIndexMap, wrapper, dataList);
				setBlerKpi(kpiIndexMap, wrapper, dataList);
				setMacDlUlInfo(kpiIndexMap, wrapper, dataList, bandwidth);
				setCQIData(kpiIndexMap, wrapper, dataList);
				setSinrData(kpiIndexMap, wrapper, dataList);

				List<String[]> smallCellList = getDataFromRange(dataList, kpiIndexMap.get(ReportConstants.PCI_PLOT),
						403, 480);

				Double smallCellHoRate = getHoSuccessRate(kpiIndexMap, smallCellList);
				if (smallCellHoRate != null) {
					if (smallCellHoRate >= 98.0) {
						wrapper.setScDataHOSuccessRate(Symbol.BRACKET_OPEN_STRING + smallCellHoRate
								+ Symbol.SPACE_STRING + ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
					} else {
						wrapper.setScDataHOSuccessRate(Symbol.BRACKET_OPEN_STRING + smallCellHoRate
								+ Symbol.SPACE_STRING + ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

					}
				}
				inOutDataList = ReportUtil.filterDataByTestType(inOutDataList,
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD,
						NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_UPLOAD);
				if (Utils.isValidList(inOutDataList)) {
					Double macroHoRate = getHoSuccessRate(kpiIndexMap, inOutDataList);
					if (macroHoRate != null) {
						if (macroHoRate >= 98.0) {
							wrapper.setMacroDataHOSuccessRate(Symbol.BRACKET_OPEN_STRING + macroHoRate
									+ Symbol.SPACE_STRING + ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
						} else {
							wrapper.setMacroDataHOSuccessRate(Symbol.BRACKET_OPEN_STRING + macroHoRate
									+ Symbol.SPACE_STRING + ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

						}
					}
				}

				Double hoIntTimeRate = getRateLessThanKpi(kpiIndexMap.get(ReportConstants.HO_INTERRUPTION_TIME),
						dataList, 75.0);
				if (hoIntTimeRate != null) {
					if (hoIntTimeRate >= 95.0) {
						wrapper.setDataHoInterruptionTime(Symbol.BRACKET_OPEN_STRING + hoIntTimeRate
								+ Symbol.SPACE_STRING + ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
					} else {
						wrapper.setDataHoInterruptionTime(Symbol.BRACKET_OPEN_STRING + hoIntTimeRate
								+ Symbol.SPACE_STRING + ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);
					}
				}
				setOverLappingServerData(dataList, kpiIndexMap, wrapper);
			} catch (Exception e) {
				logger.error("Exception inside the method setDataToWrapper {}", Utils.getStackTrace(e));
			}

		}

	}

	private String setBandWidth(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper, List<String[]> dataList) {

		String bandwidth = null;
		try {
			Set<String> bandWidth = getListFromIndex(dataList, kpiIndexMap.get(ReportConstants.DL_BANWIDTH), false);
			bandwidth = (String) bandWidth.toArray()[0];
			if (bandwidth != null) {
				wrapper.setBandwidth(bandwidth);
			} else {
				wrapper.setBandwidth("20 MHz");
			}
		} catch (Exception e) {
			logger.error("error inside the method setBandWidth {}", Utils.getStackTrace(e));
		}
		return bandwidth;
	}

	private void setSinrData(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper, List<String[]> dataList) {
		List<Double> sinrDataList = ReportUtil.convetArrayToList(dataList, kpiIndexMap.get(ReportConstants.SINR));
		List<Double> filterSinrDataList = sinrDataList.stream().filter(sinr -> sinr >= 5.0)
				.collect(Collectors.toList());
		Double sinrRate = ReportUtil.getPercentage(filterSinrDataList.size(), sinrDataList.size());
		if (sinrRate != null) {
			if (sinrRate >= 95.0) {
				wrapper.setQualitySinrRate(Symbol.BRACKET_OPEN_STRING + sinrRate + Symbol.SPACE_STRING
						+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
			} else {
				wrapper.setQualitySinrRate(Symbol.BRACKET_OPEN_STRING + sinrRate + Symbol.SPACE_STRING
						+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);
			}
		}
	}

	private void setCQIData(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper, List<String[]> dataList) {
		List<Double> cqiDataList = ReportUtil.convetArrayToList(dataList, kpiIndexMap.get(ReportConstants.CQI));
		if (Utils.isValidList(cqiDataList)) {
			wrapper.setNoOfCqiGT8(
					cqiDataList.stream().filter(cqi -> cqi > 8.0).collect(Collectors.counting()) + Symbol.SPACE_STRING);
			wrapper.setNoOfCqiGT12(cqiDataList.stream().filter(cqi -> cqi > 12.0).collect(Collectors.counting())
					+ Symbol.SPACE_STRING);

		}
	}

	private void setMacDlUlInfo(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper, List<String[]> dataList,
			String bandwidth) {
		List<String[]> dlDataList = ReportUtil.filterDataByTestType(dataList,
				kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD,
				NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD);
		List<String[]> ulDataList = ReportUtil.filterDataByTestType(dataList,
				kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_FTP_UPLOAD,
				NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD);

		if ("5 MHz".equalsIgnoreCase(bandwidth)) {

			Double avgMacDl = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.MAC_DL_THROUGHPUT), dlDataList);
			Double avgMacUl = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.MAC_UL_THROUGHPUT), ulDataList);

			if (avgMacDl != null) {
				if (avgMacDl >= 10.0) {
					wrapper.setAvgMacDl(Symbol.BRACKET_OPEN_STRING + avgMacDl + Symbol.SPACE_STRING
							+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);

				} else {
					wrapper.setAvgMacDl(Symbol.BRACKET_OPEN_STRING + avgMacDl + Symbol.SPACE_STRING
							+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

				}
			}
			//
			if (avgMacUl != null) {
				if (avgMacUl >= 3.0) {
					wrapper.setAvgMacUl(Symbol.BRACKET_OPEN_STRING + avgMacUl + Symbol.SPACE_STRING
							+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
				}

				else {
					wrapper.setAvgMacUl(Symbol.BRACKET_OPEN_STRING + avgMacUl + Symbol.SPACE_STRING
							+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

				}
			}
			wrapper.setMacDlThp(getMacDlUlRate(dlDataList, 6.0, kpiIndexMap.get(ReportConstants.MAC_DL_THROUGHPUT)));

			wrapper.setMacUlThp(getMacDlUlRate(ulDataList, 2.0, kpiIndexMap.get(ReportConstants.MAC_UL_THROUGHPUT)));

		} else {

			Double avgMacDl = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.MAC_DL_THROUGHPUT), dlDataList);
			Double avgMacUl = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.MAC_UL_THROUGHPUT), ulDataList);
			if (avgMacDl != null) {
				if (avgMacDl >= 40.0) {
					wrapper.setAvgMacDl(Symbol.BRACKET_OPEN_STRING + avgMacDl + Symbol.SPACE_STRING
							+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);

				} else {
					wrapper.setAvgMacDl(Symbol.BRACKET_OPEN_STRING + avgMacDl + Symbol.SPACE_STRING
							+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

				}
			}
			if (avgMacUl != null) {
				if (avgMacUl >= 12.0) {
					wrapper.setAvgMacUl(Symbol.BRACKET_OPEN_STRING + avgMacUl + Symbol.SPACE_STRING
							+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);

				} else {
					wrapper.setAvgMacUl(Symbol.BRACKET_OPEN_STRING + avgMacUl + Symbol.SPACE_STRING
							+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

				}
			}
			wrapper.setMacDlThp(getMacDlUlRate(dlDataList, 24.0, kpiIndexMap.get(ReportConstants.MAC_DL_THROUGHPUT)));

			wrapper.setMacUlThp(getMacDlUlRate(ulDataList, 8.0, kpiIndexMap.get(ReportConstants.MAC_UL_THROUGHPUT)));

		}

	}

	private String getMacDlUlRate(List<String[]> dlDataList, Double criteria, Integer index) {
		try {
			List<Double> macDlList = ReportUtil.convetArrayToList(dlDataList, index);

			Long count = macDlList.stream().filter(dl -> dl >= criteria).collect(Collectors.counting());
			Double dlRate = ReportUtil.getPercentage(count.intValue(), macDlList.size());
			if (dlRate != null) {
				if (dlRate >= 95) {
					return Symbol.BRACKET_OPEN_STRING + dlRate + Symbol.SPACE_STRING + ReportConstants.PASS
							+ Symbol.BRACKET_CLOSE_STRING;
				} else {
					return Symbol.BRACKET_OPEN_STRING + dlRate + Symbol.SPACE_STRING + ReportConstants.FAIL
							+ Symbol.BRACKET_CLOSE_STRING;
				}
			}
		} catch (Exception e) {
			logger.error("Error insdie the getMacDlUlRate {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private void setBlerKpi(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper, List<String[]> dataList) {
		Double avgDlBler = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.DL_BLER), dataList);
		Double avgulBler = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.UL_BLER), dataList);

		if (avgDlBler != null) {
			wrapper.setAvgBlerDl(Symbol.BRACKET_OPEN_STRING + avgDlBler + Symbol.BRACKET_CLOSE_STRING);
		}

		if (avgulBler != null) {
			wrapper.setAvgBlerUl(Symbol.BRACKET_OPEN_STRING + avgulBler + Symbol.BRACKET_CLOSE_STRING);
		}
	}

	@SuppressWarnings("unchecked")
	private Set<String> getListFromIndex(List<String[]> dataList, Integer index, boolean checkValue) {
		Set<String> set = new HashSet();
		if (index != null) {
			for (String[] arr : dataList) {
				if (arr != null && arr.length > index && arr[index] != null && !arr[index].isEmpty()) {
					if (checkValue && arr[index].equalsIgnoreCase(NetworkDataFormats.NONE)) {
						logger.info(" technology is null ");
					} else {
						set.add(arr[index]);

					}
				}
			}
		}
		return set;
	}

	private void setVolteTestDataToWrapper(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> dataList, List<String[]> inOutDataList) {
		try {
			setVolteJitterRate(kpiIndexMap, wrapper, dataList);
			setVolteLatencyRate(kpiIndexMap, wrapper, dataList);
			setVolteSetupRate(kpiIndexMap, wrapper, dataList);
			setVolteCallDropRate(kpiIndexMap, wrapper, dataList);
			setVoltCallSetupTime(kpiIndexMap, wrapper, dataList);
			setRtpPackeLoss(kpiIndexMap, wrapper, dataList);
			setImsRegSetupTime(kpiIndexMap, wrapper, dataList);

			List<String[]> volteSmallCellList = getDataFromRange(dataList, kpiIndexMap.get(ReportConstants.PCI_PLOT),
					403, 480);

			Double volteHORate = getHoSuccessRate(kpiIndexMap, volteSmallCellList);
			if (volteHORate != null) {
				if (volteHORate >= 98.0) {
					wrapper.setScVolteHOSuccessRate(Symbol.BRACKET_OPEN_STRING + volteHORate + Symbol.SPACE_STRING
							+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
				} else {
					wrapper.setScVolteHOSuccessRate(Symbol.BRACKET_OPEN_STRING + volteHORate + Symbol.SPACE_STRING
							+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

				}
			}
			
			
			if (Utils.isValidList(inOutDataList)) {
				inOutDataList = ReportUtil.filterDataByTestType(inOutDataList, kpiIndexMap.get(ReportConstants.TEST_TYPE),
						NetworkDataFormats.SHORT_CALL_TEST, NetworkDataFormats.LONG_CALL_TEST);
				Double macroHoRate = getHoSuccessRate(kpiIndexMap, inOutDataList);
				if (macroHoRate != null) {
					if (macroHoRate >= 98.0) {
						wrapper.setMacroVolteHOSuccessRate(Symbol.BRACKET_OPEN_STRING + macroHoRate
								+ Symbol.SPACE_STRING + ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
					} else {
						wrapper.setMacroVolteHOSuccessRate(Symbol.BRACKET_OPEN_STRING + macroHoRate
								+ Symbol.SPACE_STRING + ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

					}
				}
			}
			if (kpiIndexMap.containsKey(ReportConstants.HO_INTERRUPTION_TIME)) {
				Double hoIntTimeRate = getRateLessThanKpi(kpiIndexMap.get(ReportConstants.HO_INTERRUPTION_TIME),
						dataList, 75.0);
				if (hoIntTimeRate != null) {
					if (hoIntTimeRate >= 95.0) {
						wrapper.setVolteHoInterruptionTime(Symbol.BRACKET_OPEN_STRING + hoIntTimeRate
								+ Symbol.SPACE_STRING + ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
					} else {
						wrapper.setVolteHoInterruptionTime(Symbol.BRACKET_OPEN_STRING + hoIntTimeRate
								+ Symbol.SPACE_STRING + ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside the method setVolteTestDataToWrapper {}", Utils.getStackTrace(e));
		}
	}

	private Double getRateLessThanKpi(Integer index, List<String[]> dataList, Double target) {
		List<Double> hoInterputionTimeList = ReportUtil.convetArrayToList(dataList, index);
		if (Utils.isValidList(hoInterputionTimeList)) {
			List<Double> filterList = hoInterputionTimeList.stream().filter(hoTime -> hoTime < target)
					.collect(Collectors.toList());
			return ReportUtil.getPercentage(filterList.size(), hoInterputionTimeList.size());

		}
		return null;
	}

	private void setVoltCallSetupTime(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> dataList) {
		List<Double> callSetupTimeList = ReportUtil.convetArrayToList(dataList,
				kpiIndexMap.get(ReportConstants.CALL_SETUP_TIME));
		if (Utils.isValidList(callSetupTimeList)) {
			List<Double> filterCallTimeList = callSetupTimeList.stream()
					.filter(callSetupTime -> (callSetupTime / 1000) < 3.5).collect(Collectors.toList());
			Double callSetupRate = ReportUtil.getPercentage(filterCallTimeList.size(), callSetupTimeList.size());
			if (callSetupRate != null) {
				if (callSetupRate >= 95.0) {
					wrapper.setVolteSetupTime(Symbol.BRACKET_OPEN_STRING + callSetupRate + Symbol.SPACE_STRING
							+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
				} else {
					wrapper.setVolteSetupTime(Symbol.BRACKET_OPEN_STRING + callSetupRate + Symbol.SPACE_STRING
							+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

				}
			}

		}
	}

	private void setRtpPackeLoss(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper, List<String[]> dataList) {
		Double rtpPacketLoss = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.VOLTE_PACKET_LOSS), dataList);
		if (rtpPacketLoss != null) {
			if (rtpPacketLoss <= 1) {
				wrapper.setRtpPacketLossRate(Symbol.BRACKET_OPEN_STRING + rtpPacketLoss + Symbol.SPACE_STRING
						+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
			} else {
				wrapper.setRtpPacketLossRate(Symbol.BRACKET_OPEN_STRING + rtpPacketLoss + Symbol.SPACE_STRING
						+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

			}
		}
	}

	private void setImsRegSetupTime(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> dataList) {
		List<Double> imsRegTimeList = ReportUtil.convetArrayToList(dataList,
				kpiIndexMap.get(ReportConstants.IMS_REGISTRATION_TIME));
		if (Utils.isValidList(imsRegTimeList)) {
			List<Double> filterImsRegList = imsRegTimeList.stream().filter(imsRegTime -> (imsRegTime / 1000) < 1.5)
					.collect(Collectors.toList());
			Double imsRegRate = ReportUtil.getPercentage(filterImsRegList.size(), imsRegTimeList.size());

			if (imsRegRate != null) {
				wrapper.setImsRegistrationSetupTime(
						Symbol.BRACKET_OPEN_STRING + imsRegRate + Symbol.BRACKET_CLOSE_STRING);
			}
		}
	}

	private void setVolteCallDropRate(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> dataList) {
		if (kpiIndexMap.containsKey(ReportConstants.CALL_INITIATE)
				&& kpiIndexMap.containsKey(ReportConstants.CALL_DROP)) {
			double volteCallDrop = getKpiSuccessRate(dataList, kpiIndexMap.get(ReportConstants.CALL_INITIATE),
					kpiIndexMap.get(ReportConstants.CALL_DROP));
			if (volteCallDrop <= 1.0) {
				wrapper.setVolteCallDropRate(Symbol.BRACKET_OPEN_STRING + volteCallDrop + Symbol.SPACE_STRING
						+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
			} else {
				wrapper.setVolteCallDropRate(Symbol.BRACKET_OPEN_STRING + volteCallDrop + Symbol.SPACE_STRING
						+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

			}
		}
	}

	private void setVolteSetupRate(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> dataList) {
		if (kpiIndexMap.containsKey(ReportConstants.CALL_INITIATE)) {
			double volteSetupRate = getKpiSuccessRate(dataList, kpiIndexMap.get(ReportConstants.CALL_INITIATE),
					kpiIndexMap.get(ReportConstants.CALL_SETUP_SUCCESS));
			if (volteSetupRate >= 99.0) {
				wrapper.setVolteSetupSuccessRate(Symbol.BRACKET_OPEN_STRING + volteSetupRate + Symbol.SPACE_STRING
						+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
			} else {
				wrapper.setVolteSetupSuccessRate(Symbol.BRACKET_OPEN_STRING + volteSetupRate + Symbol.SPACE_STRING
						+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

			}
		}
	}

	private void setVolteLatencyRate(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> dataList) {
		if (kpiIndexMap.containsKey(ReportConstants.VOLTE_LATENCY)) {
			List<Double> latencyDataList = ReportUtil.convetArrayToList(dataList,
					kpiIndexMap.get(ReportConstants.VOLTE_LATENCY));

			if (Utils.isValidList(latencyDataList)) {
				List<Double> filterLatencyList = latencyDataList.stream().filter(latency -> latency < 50.0)
						.collect(Collectors.toList());
				Double latencyRate = ReportUtil.getPercentage(filterLatencyList.size(), latencyDataList.size());
				if (latencyRate != null) {
					if (latencyRate >= 95.0) {
						wrapper.setVolteLatency(Symbol.BRACKET_OPEN_STRING + latencyRate + Symbol.SPACE_STRING
								+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
					} else {
						wrapper.setVolteLatency(Symbol.BRACKET_OPEN_STRING + latencyRate + Symbol.SPACE_STRING
								+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);
					}
				}
			}
		}
	}

	private void setVolteJitterRate(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> dataList) {
		try {
			List<Double> jitterDataList = ReportUtil.convetArrayToList(dataList,
					kpiIndexMap.get(ReportConstants.VOLTE_JITTER));

			if (Utils.isValidList(jitterDataList)) {
				List<Double> filterJitterList = jitterDataList.stream().filter(jitter -> jitter < 40.0)
						.collect(Collectors.toList());
				Double jitterRate = ReportUtil.getPercentage(filterJitterList.size(), jitterDataList.size());
				if (jitterRate != null) {
					if (jitterRate >= 95.0) {
						wrapper.setVolteJitter(Symbol.BRACKET_OPEN_STRING + jitterRate + Symbol.SPACE_STRING
								+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
					} else {
						wrapper.setVolteJitter(Symbol.BRACKET_OPEN_STRING + jitterRate + Symbol.SPACE_STRING
								+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside the method setVolteJitterRate ");
		}
	}

	private void setIdleTestDataToWrapper(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> arList) {
		try {
			if (Utils.isValidList(arList)) {
				logger.info("inside the method setIdleTestDataToWrapper {}", arList.size());
				setIDELRsrpRate(kpiIndexMap, wrapper, arList);
				setSmallCellPciRate(kpiIndexMap, wrapper, arList);
				if (kpiIndexMap.containsKey(ReportConstants.ATTACH_REQUEST)) {
					Double atRate = getKpiSuccessRate(arList, kpiIndexMap.get(ReportConstants.ATTACH_REQUEST),
							kpiIndexMap.get(ReportConstants.ATTACH_COMPLETE));
					if (atRate != null) {
						if (atRate >= 99.0) {
							wrapper.setAtSuccessRate(Symbol.BRACKET_OPEN_STRING + atRate + Symbol.SPACE_STRING
									+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
						} else {
							wrapper.setAtSuccessRate(Symbol.BRACKET_OPEN_STRING + atRate + Symbol.SPACE_STRING
									+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);
						}
					}
				}

				if (kpiIndexMap.containsKey(ReportConstants.DETACH_REQUEST)) {
					Double dTRate = getKpiSuccessRate(arList, kpiIndexMap.get(ReportConstants.DETACH_REQUEST),
							kpiIndexMap.get(ReportConstants.DETACH_REQUEST));

					if (dTRate != null) {
						if (dTRate >= 99.0) {
							wrapper.setDtSuccessRate(Symbol.BRACKET_OPEN_STRING + dTRate + Symbol.SPACE_STRING
									+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
						} else {
							wrapper.setDtSuccessRate(Symbol.BRACKET_OPEN_STRING + dTRate + Symbol.SPACE_STRING
									+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);
						}
					}
				}

				if (kpiIndexMap.containsKey(ReportConstants.MSG1_COUNT)) {

					Double rachRate = getKpiSuccessRate(arList, kpiIndexMap.get(ReportConstants.MSG1_COUNT),
							kpiIndexMap.get(ReportConstants.MSG3_COUNT));

					if (rachRate != null) {
						if (rachRate >= 95.0) {
							wrapper.setRachSuccessRate(Symbol.BRACKET_OPEN_STRING + rachRate + Symbol.SPACE_STRING
									+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
						} else {
							wrapper.setRachSuccessRate(Symbol.BRACKET_OPEN_STRING + rachRate + Symbol.SPACE_STRING
									+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);
						}
					}
				}
				if (kpiIndexMap.containsKey(ReportConstants.CELL_RESELECTION)) {
					Double cellReselectionRate = getKpiSuccessRate(arList,
							kpiIndexMap.get(ReportConstants.CELL_RESELECTION),
							kpiIndexMap.get(ReportConstants.CELL_RESELECTION));
					if (cellReselectionRate != null) {
						if (cellReselectionRate >= 100.0) {
							wrapper.setCellReSelectionRate(Symbol.BRACKET_OPEN_STRING + cellReselectionRate
									+ Symbol.SPACE_STRING + ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
						} else {
							wrapper.setCellReSelectionRate(Symbol.BRACKET_OPEN_STRING + cellReselectionRate
									+ Symbol.SPACE_STRING + ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside the method setIdleTestDataToWrapper {}", Utils.getStackTrace(e));
		}
	}

	private void setSmallCellPciRate(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> arList) {
		List<Double> pciDataList = ReportUtil.convetArrayToList(arList, kpiIndexMap.get(ReportConstants.PCI_PLOT));
		List<Double> filterPciList = pciDataList.stream().filter(pci -> (pci > 403.0 && pci < 480.0))
				.collect(Collectors.toList());

		Double pciRate = ReportUtil.getPercentage(filterPciList.size(), pciDataList.size());
		if (pciRate != null) {
			if (pciRate == 100.0) {
				wrapper.setPciRateForSc(Symbol.BRACKET_OPEN_STRING + pciRate + Symbol.SPACE_STRING
						+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
			} else {
				wrapper.setPciRateForSc(Symbol.BRACKET_OPEN_STRING + pciRate + Symbol.SPACE_STRING
						+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);
			}
		}
	}

	private void setIDELRsrpRate(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper, List<String[]> arList) {
		List<Double> rsrpDataList = ReportUtil.convetArrayToList(arList, kpiIndexMap.get(ReportConstants.RSRP));
		List<Double> filterList = rsrpDataList.stream().filter(x -> (x >= -95.0)).collect(Collectors.toList());
		Double value = ReportUtil.getPercentage(filterList.size(), rsrpDataList.size());
		if (value != null) {
			if (value >= 95.0) {
				wrapper.setIdleRsrpRate(Symbol.BRACKET_OPEN_STRING + value + Symbol.SPACE_STRING + ReportConstants.PASS
						+ Symbol.BRACKET_CLOSE_STRING);
			} else {
				wrapper.setIdleRsrpRate(Symbol.BRACKET_OPEN_STRING + value + Symbol.SPACE_STRING + ReportConstants.FAIL
						+ Symbol.BRACKET_CLOSE_STRING);
			}
		}
	}

	private void setInOutDataToWrapper(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> list) {
		try {
			if (Utils.isValidList(list)) {
				List<String[]> dataList = ReportUtil.filterDataByTestType(list,
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_UPLOAD);
				List<String[]> volteDataList = ReportUtil.filterDataByTestType(list,
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.SHORT_CALL_TEST,
						NetworkDataFormats.LONG_CALL_TEST);
				Double volteHORate = getHoSuccessRate(kpiIndexMap, volteDataList);
				if (volteHORate != null) {
					wrapper.setVolteInOutHOSuccessRate(volteHORate.toString());
				}
				Double hoRate = getHoSuccessRate(kpiIndexMap, dataList);
				if (hoRate != null) {
					wrapper.setInOutHOSuccessRate(hoRate.toString());
				}

			}
		} catch (Exception e) {
			logger.error("Exception inside the method setInOutDataToWrapper {}", Utils.getStackTrace(e));
		}
	}

	private void setSpillageTestDataToWrapper(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> list, Map<String, Integer> summaryKpiIndexMap, List<String[]> listsummaryData) {
		try {
			if (Utils.isValidList(list)) {
				logger.info("inside the method setSpillageTestDataToWrapper {}", list.size());

				Double avgRsrp = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.RSRP), list);
				if (avgRsrp != null) {
					wrapper.setSpRsrp(avgRsrp.toString());
				}
				Double avgSinr = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.SINR), list);
				if (avgSinr != null) {
					wrapper.setSpSinr(avgSinr.toString());
				}
				if (Utils.isValidList(listsummaryData)) {
					String[] summaryData = listsummaryData.get(ReportConstants.INDEX_ZER0);
					if (summaryKpiIndexMap.containsKey(ReportConstants.PCI_LIST) && summaryData != null) {
						String pci = summaryData[summaryKpiIndexMap.get(ReportConstants.PCI_LIST)];
						if (pci != null) {
							wrapper.setSpPci(pci.replace(Symbol.UNDERSCORE_STRING, Symbol.COMMA_STRING));
						} else {
							Set<String> pciList = getListFromIndex(list, kpiIndexMap.get(ReportConstants.PCI_PLOT),
									false);
							wrapper.setSpPci(String.join(Symbol.SLASH_FORWARD_STRING, pciList));
						}
					} else {
						Set<String> pciList = getListFromIndex(list, kpiIndexMap.get(ReportConstants.PCI_PLOT), false);
						wrapper.setSpPci(String.join(Symbol.SLASH_FORWARD_STRING, pciList));
					}

				} else {
					Set<String> pciList = getListFromIndex(list, kpiIndexMap.get(ReportConstants.PCI_PLOT), false);
					wrapper.setSpPci(String.join(Symbol.SLASH_FORWARD_STRING, pciList));
				}

			}
		} catch (Exception e) {
			logger.error("Exception inside the method setSpillageTestDataToWrapper {}", Utils.getStackTrace(e));
		}
	}

	private void setElevatorDataToWrapper(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> list) {
		if (Utils.isValidList(list)) {
			try {
				List<String[]> dataList = ReportUtil.filterDataByTestType(list,
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_UPLOAD);
				List<String[]> volteDataList = ReportUtil.filterDataByTestType(list,
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.SHORT_CALL_TEST,
						NetworkDataFormats.LONG_CALL_TEST);
				List<String[]> volteSmallCellList = getDataFromRange(volteDataList,
						kpiIndexMap.get(ReportConstants.PCI_PLOT), 403, 480);

				Double volteHORate = getHoSuccessRate(kpiIndexMap, volteSmallCellList);
				if (volteHORate != null) {
					wrapper.setVolteElevHOSuccessRate(volteHORate.toString());
				}
				List<String[]> smallCellList = getDataFromRange(dataList, kpiIndexMap.get(ReportConstants.PCI_PLOT),
						403, 480);
				Double hoRate = getHoSuccessRate(kpiIndexMap, smallCellList);
				if (hoRate != null) {
					wrapper.setElevHOSuccessRate(hoRate.toString());
				}
			} catch (Exception e) {
				logger.error("Exception inside the method setElevatorDataToWrapper {}", Utils.getStackTrace(e));
			}

		}
	}

	private void setStairCaseDataToWrapper(Map<String, Integer> kpiIndexMap, FloorWiseWrapper wrapper,
			List<String[]> list) {
		try {
			if (Utils.isValidList(list)) {
				List<String[]> dataList = ReportUtil.filterDataByTestType(list,
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_UPLOAD);

				List<String[]> volteDataList = ReportUtil.filterDataByTestType(list,
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.SHORT_CALL_TEST,
						NetworkDataFormats.LONG_CALL_TEST);
				List<String[]> volteSmallCellList = getDataFromRange(volteDataList,
						kpiIndexMap.get(ReportConstants.PCI_PLOT), 403, 480);

				Double volteHORate = getHoSuccessRate(kpiIndexMap, volteSmallCellList);
				if (volteHORate != null) {
					if (volteHORate >= 98.0) {
						wrapper.setVolteStHOSuccessRate(Symbol.BRACKET_OPEN_STRING + volteHORate + Symbol.SPACE_STRING
								+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
					} else {
						wrapper.setVolteStHOSuccessRate(Symbol.BRACKET_OPEN_STRING + volteHORate + Symbol.SPACE_STRING
								+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

					}
				}
				List<String[]> smallCellList = getDataFromRange(dataList, kpiIndexMap.get(ReportConstants.PCI_PLOT),
						403, 480);

				Double hoRate = getHoSuccessRate(kpiIndexMap, smallCellList);
				if (hoRate != null) {
					if (hoRate >= 98.0) {
						wrapper.setStHOSuccessRate(Symbol.BRACKET_OPEN_STRING + hoRate + Symbol.SPACE_STRING
								+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
					} else {
						wrapper.setStHOSuccessRate(Symbol.BRACKET_OPEN_STRING + hoRate + Symbol.SPACE_STRING
								+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

					}
				}

			}
		} catch (Exception e) {
			logger.error("Exception inside the method  setStairCaseDataToWrapper {}", Utils.getStackTrace(e));
		}
	}

	private List<String[]> getDataFromRange(List<String[]> dataList, int index, int lowerLimit, int upperLimit) {
		List<String[]> newList = new ArrayList<>();
		for (String[] array : dataList) {
			if (ReportUtil.checkIndexValue(index, array) && (NumberUtils.toInteger(array[index])) > lowerLimit
					&& (NumberUtils.toInteger(array[index]) < upperLimit)) {
				newList.add(array);
			}

		}
		return newList;
	}

	private Double getHoSuccessRate(Map<String, Integer> kpiIndexMap, List<String[]> dataList) {
		if (Utils.isValidList(dataList)) {
			int initateCount = getSumFromIndex(kpiIndexMap.get(ReportConstants.HANDOVER_INITIATE), dataList);
			int successCount = getSumFromIndex(kpiIndexMap.get(ReportConstants.HANDOVER_SUCCESS), dataList);
			return ReportUtil.getPercentage(successCount, initateCount);
		}
		return null;

	}

	private int getSumFromIndex(Integer index, List<String[]> dataList) {
		List<Double> data = ReportUtil.convetArrayToList(dataList, index);
		Double value = data.stream().mapToDouble(Double::valueOf).sum();
		return value != null ? value.intValue() : ForesightConstants.INDEX_ZERO;
	}

	private Map<String, Map<String, List<String>>> getRecipWieIDMap(Integer workorderId) throws IOException {
		String mapJson = iSystemConfigurationDao
				.getSystemConfigurationByName(ReportConstants.NV_IB1E_REPORT_RECIPEID_MAP)
				.get(ReportConstants.INDEX_ZER0).getValue();
		Map<String, List<String>> recipeIDMap = mapper.readValue(mapJson,
				new TypeReference<Map<String, List<String>>>() {
				});
		return getRecipeMapForRecipeId(workorderId, recipeIDMap);

	}

	public Map<String, Map<String, List<String>>> getRecipeMapForRecipeId(Integer workorderId,
			Map<String, List<String>> recipeIDMap) {
		Map<String, Map<String, List<String>>> map = new HashMap<>();
		for (Entry<String, List<String>> entry : recipeIDMap.entrySet()) {
			Map<String, List<String>> driveRecipeMap = new HashMap<>();
			List<String> recipeList = new ArrayList<>();
			List<WORecipeMapping> mappingList = recipeMappingDao.getWORecipeByGWOId(workorderId);
			for (WORecipeMapping woRecipeMapping : mappingList) {
				if (Status.COMPLETED.equals(woRecipeMapping.getStatus()) && woRecipeMapping.getRecipe() != null
						&& entry.getValue().contains(woRecipeMapping.getRecipe().getRecipeId())) {

					recipeList.add(woRecipeMapping.getId().toString());
				}
			}
			if (Utils.isValidList(recipeList)) {
				driveRecipeMap.put(QMDLConstant.RECIPE, recipeList);
				map.put(entry.getKey(), driveRecipeMap);
			}

		}
		return map;
	}

	private Double getAvgOfIndexData(Integer index, List<String[]> dataList) {
		List<Double> data = ReportUtil.convetArrayToList(dataList, index);
		OptionalDouble avg = data.stream().mapToDouble(x -> x).average();
		if (avg.isPresent()) {
			return ReportUtil.parseToFixedDecimalPlace(avg.getAsDouble(), ForesightConstants.INDEX_TWO);
		}
		return null;
	}

	private double getKpiSuccessRate(List<String[]> driveDataForWorkorders, int attemptindex, int sucessindex) {
		double successrate = 0.0;
		List<String> attemptSumList = driveDataForWorkorders.stream()
				.filter(x -> (x[attemptindex] != null && !x[attemptindex].isEmpty())).map(x -> x[attemptindex])
				.collect(Collectors.toList());
		int attemptSum = 0;
		if (Utils.isValidList(attemptSumList)) {
			attemptSum = attemptSumList.stream().mapToInt(i -> Integer.parseInt(i)).sum();
		}

		List<String> successSumList = driveDataForWorkorders.stream()
				.filter(x -> (x[sucessindex] != null && !x[sucessindex].isEmpty())).map(x -> x[sucessindex])
				.collect(Collectors.toList());
		int successSum = 0;
		if (Utils.isValidList(successSumList)) {
			successSum = successSumList.stream().mapToInt(i -> Integer.parseInt(i)).sum();
		}
		logger.info("sum of attempts {}", attemptSum);
		logger.info("sum of success {}", successSum);
		if (attemptSum > 0) {
			return ReportUtil.getPercentage(successSum, attemptSum);
		}
		return successrate;
	}

	private List<KPIWrapper> getKPIList(Map<String, Integer> kpiIndexMap) {
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, kpiIndexMap);
		kpiList = reportService.modifyIndexOfCustomKpisForReport(kpiList, kpiIndexMap);
		return kpiList;
	}

	private BenchMarkOperatorInfo getOPeratorImgInfo(Map<String, Integer> kpiIndexMap, String localDirPath,
			List<String[]> data, KPIWrapper kpiWrapper, String imgFloorPlan) {
		BenchMarkOperatorInfo wrapper = new BenchMarkOperatorInfo();
		Map<String, String> kpiImageMap = InBuildingHeatMapGenerator.getInstance().generateHeatMapsForKpi(data,
				localDirPath, imgFloorPlan, kpiWrapper, kpiIndexMap);
		wrapper.setKpiImg(kpiImageMap.get(kpiWrapper.getKpiName()));
		List<KPIWrapper> legenDList = new ArrayList<>();
		legenDList.add(kpiWrapper);
		HashMap<String, BufferedImage> bufferdImageMap = reportService.getLegendImages(legenDList, data,
				kpiIndexMap.get(ReportConstants.TEST_TYPE));
		HashMap<String, String> legendImgMap = mapImageService.saveDriveImages(bufferdImageMap, localDirPath, false);
		wrapper.setKpiLegendImg(legendImgMap
				.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(kpiWrapper.getKpiName())));
		wrapper.setKpiName(kpiWrapper.getKpiName().replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING));
		return wrapper;
	}

	private void setBuildingDataToWrapper(Building building, InbuildingWrapper mainWrapper, List<String[]> driveData,
			Map<String, Integer> kpiIndexMap) {
		logger.info(" Inside method setBuildingDataToWrapper");
		if (building != null) {
			mainWrapper.setBuildingId(building.getBuildingId());
			mainWrapper.setBuildingName(building.getBuildingName());
			mainWrapper.setLatitude(building.getLatitude().toString());
			mainWrapper.setLongitude(building.getLongitude().toString());
			if (building.getGeographyL4() != null && building.getGeographyL4().getGeographyL3() != null) {
				mainWrapper.setCity(building.getGeographyL4().getGeographyL3().getDisplayName());
			}

		}
		if (Utils.isValidList(driveData)) {
			if (kpiIndexMap.containsKey(ReportConstants.DL_EARFCN)) {
				mainWrapper.setEarfcn(InbuildingReportUtil.getDistinctKpiValues(driveData,
						kpiIndexMap.get(ReportConstants.DL_EARFCN)));
			}
			if (kpiIndexMap.containsKey(ReportConstants.TAC)) {
				mainWrapper.setTac(
						InbuildingReportUtil.getDistinctKpiValues(driveData, kpiIndexMap.get(ReportConstants.TAC)));
			}
			if (kpiIndexMap.containsKey(ReportConstants.MCC) && kpiIndexMap.containsKey(ReportConstants.MNC)) {
				mainWrapper.setMccmnc(InbuildingReportUtil.getDistinctKpiValues(driveData,
						kpiIndexMap.get(ReportConstants.MCC)) + Symbol.SLASH_FORWARD_STRING
						+ InbuildingReportUtil.getDistinctKpiValues(driveData, kpiIndexMap.get(ReportConstants.MNC)));
			}
			if (kpiIndexMap.containsKey(ReportConstants.NETWORK_TYPE)) {
				Set<String> technlogyList = getListFromIndex(driveData, kpiIndexMap.get(ReportConstants.NETWORK_TYPE),
						true);
				mainWrapper.setTechnology(String.join(Symbol.SLASH_FORWARD_STRING, technlogyList));
			}
		}
	}

	private File proceedToCreatReport(IB1EReportWrapper dataWrapper, Map<String, Object> imageMap,
			String destinationFilePath) {
		logger.info("inside the method proceedToCreatePlot {} ", new Gson().toJson(dataWrapper));
		try {

			String reportAssets = ConfigUtils.getString(ReportConstants.INBUILDING_ONEE_JASPER_PATH);
			List<IB1EReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(dataWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssets);
			imageMap.put(ReportConstants.IMAGE_PARAM_HEADER_LOGO, reportAssets + ReportConstants.IMAGE_HEADER_LOGO);
			imageMap.put(ReportConstants.LOGO_NV_KEY, reportAssets + ReportConstants.LOGO_NV_IMG);
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

	private List<VoiceStatsWrapper> getCallData(List<String[]> dataList, Map<String, Integer> kpiIndexMap) {
		List<VoiceStatsWrapper> voicestatslist = new ArrayList<>();
		VoiceStatsWrapper wrapper = new VoiceStatsWrapper();

		try {
			if (Utils.isValidList(dataList)) {
				if (kpiIndexMap.containsKey(ReportConstants.CALL_INITIATE)) {
					wrapper.setCallAttemptCount(
							getSumFromIndex(kpiIndexMap.get(ReportConstants.CALL_INITIATE), dataList)
									+ Symbol.EMPTY_STRING);
				}
				if (kpiIndexMap.containsKey(ReportConstants.CALL_SUCCESS)) {
					wrapper.setCallConnectedCount(
							getSumFromIndex(kpiIndexMap.get(ReportConstants.CALL_SUCCESS), dataList)
									+ Symbol.EMPTY_STRING);
				}
				if (kpiIndexMap.containsKey(ReportConstants.CALL_DROP)) {
					wrapper.setCallDroppedCount(getSumFromIndex(kpiIndexMap.get(ReportConstants.CALL_DROP), dataList)
							+ Symbol.EMPTY_STRING);
				}
				if (kpiIndexMap.containsKey(ReportConstants.CALL_FAILURE)) {
					wrapper.setCallFailCount(getSumFromIndex(kpiIndexMap.get(ReportConstants.CALL_FAILURE), dataList)
							+ Symbol.EMPTY_STRING);
				}
			}
			voicestatslist.add(wrapper);
		} catch (Exception e) {
			logger.error("Exception inside the method getCallData {} ", Utils.getStackTrace(e));
		}
		return voicestatslist;
	}

	private List<HandoverDataWrapper> getHoData(List<String[]> dataList, Map<String, Integer> kpiIndexMap) {
		List<HandoverDataWrapper> hoDataList = new ArrayList<>();
		HandoverDataWrapper wrapper = new HandoverDataWrapper();

		try {
			if (Utils.isValidList(dataList)) {
				if (kpiIndexMap.containsKey(ReportConstants.HANDOVER_INITIATE)) {
					wrapper.setHoInitate(getSumFromIndex(kpiIndexMap.get(ReportConstants.HANDOVER_INITIATE), dataList)
							+ Symbol.EMPTY_STRING);
				}
				if (kpiIndexMap.containsKey(ReportConstants.HANDOVER_SUCCESS)) {
					wrapper.setHoSuccess(getSumFromIndex(kpiIndexMap.get(ReportConstants.HANDOVER_SUCCESS), dataList)
							+ Symbol.EMPTY_STRING);
				}
				if (kpiIndexMap.containsKey(ReportConstants.HANDOVER_FAILURE)) {
					wrapper.setHoFailure(getSumFromIndex(kpiIndexMap.get(ReportConstants.HANDOVER_FAILURE), dataList)
							+ Symbol.EMPTY_STRING);
				}

			}
			hoDataList.add(wrapper);
		} catch (Exception e) {
			logger.error("Exception inside the method getHoData {} ", Utils.getStackTrace(e));
		}
		return hoDataList;
	}

	private void setBuildingImageToWrapper(Map<String, Object> imageMap, IB1EReportWrapper dataWrapper) {
		List<Double[]> latLongList = new ArrayList<>();
		logger.info("inside method setDriveDataWrapper");
		try {
			if (dataWrapper != null) {
				Double[] latLong = new Double[2];
				if (NumberUtils.isParsable(dataWrapper.getLongitude())
						&& NumberUtils.isParsable(dataWrapper.getLatitude())) {
					latLong[0] = NumberUtils.toDouble(dataWrapper.getLongitude());
					latLong[1] = NumberUtils.toDouble(dataWrapper.getLatitude());
					latLongList.add(latLong);

					LatLng centroid = new LatLng(NumberUtils.toDouble(dataWrapper.getLatitude()),
							NumberUtils.toDouble(dataWrapper.getLongitude()));
					HashMap<String, BufferedImage> siteBufferedImageMap = mapImageService.getBuildingImage(latLongList,
							dataWrapper.getBuildingId(), centroid);
					ImageIO.write(siteBufferedImageMap.get(dataWrapper.getBuildingId()), "PNG",
							new File(ConfigUtils.getString(ReportConstants.FINAL_IMAGE_PATH)
									+ dataWrapper.getBuildingId() + ".png"));
					if (siteBufferedImageMap.containsKey(dataWrapper.getBuildingId())) {
						imageMap.put("buildingImg", ConfigUtils.getString(ReportConstants.FINAL_IMAGE_PATH)
								+ dataWrapper.getBuildingId() + ".png");
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in settng buildingImage for inbuilding {}", Utils.getStackTrace(e));
		}
		logger.info("imagemap content {}", imageMap);
	}

	private void setOverLappingServerData(List<String[]> dataList, Map<String, Integer> kpiIndexMap,
			FloorWiseWrapper wrapper) {
		Integer totalCount = ReportConstants.INDEX_ZER0;
		Integer rsrp3Count = ReportConstants.INDEX_ZER0;
		Integer rsrp5Count = ReportConstants.INDEX_ZER0;
		for (String[] ar : dataList) {
			try {
				if (ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.RSRP), ar)) {
					Double rsrp = Double.parseDouble(ar[kpiIndexMap.get(ReportConstants.RSRP)]);
					if (ar.length > kpiIndexMap.get(ReportConstants.NEIGHBOUR_DATA)) {
						String neighbours = ar[kpiIndexMap.get(ReportConstants.NEIGHBOUR_DATA)];
						if (!StringUtils.isBlank(neighbours)) {
							List<List<Object>> neighbourList = new Gson().fromJson(neighbours, ArrayList.class);
							if (neighbourList != null && !neighbourList.isEmpty()
									&& neighbourList.size() > ReportConstants.INDEX_ZER0) {
								Integer conut3DbSample = ReportConstants.INDEX_ZER0;
								Integer conut5DbSample = ReportConstants.INDEX_ZER0;

								for (List<Object> neighbour : neighbourList) {
									if (!neighbours.isEmpty() && neighbour.size() > ForesightConstants.THREE) {
										Double neighbourRsrp = (Double) neighbour.get(ForesightConstants.THREE);
										logger.info("neighbourRsrp  =={} rsrp {}", neighbourRsrp, rsrp);

										if (Math.abs(neighbourRsrp - rsrp) <= 3.0) {
											conut3DbSample = conut3DbSample + ReportConstants.INDEX_ONE;
										}
										if (Math.abs(neighbourRsrp - rsrp) <= 5.0) {
											conut5DbSample = conut5DbSample + ReportConstants.INDEX_ONE;
										}
									}

								}
								totalCount = totalCount + ReportConstants.INDEX_ONE;
								if (conut3DbSample <= ReportConstants.INDEX_ONE) {
									rsrp3Count = rsrp3Count + ReportConstants.INDEX_ONE;
								}
								if (conut5DbSample <= ReportConstants.INDEX_TWO) {
									rsrp5Count = rsrp5Count + ReportConstants.INDEX_ONE;
								}

							} else {
								totalCount = totalCount + ReportConstants.INDEX_ONE;
								rsrp3Count = rsrp3Count + ReportConstants.INDEX_ONE;
								rsrp5Count = rsrp5Count + ReportConstants.INDEX_ONE;

							}

						} else {
							totalCount = totalCount + ReportConstants.INDEX_ONE;
							rsrp3Count = rsrp3Count + ReportConstants.INDEX_ONE;
							rsrp5Count = rsrp5Count + ReportConstants.INDEX_ONE;
						}
					}

				}
			} catch (Exception e) {
				logger.error("Exception in method getNeighbourData {} ", Utils.getStackTrace(e));
			}
		}
		logger.info(" going to setNeighbourData  totalCount {} rsrp3Count {} rsrp5Count {}", totalCount, rsrp3Count,
				rsrp5Count);
		setNeighbourData(wrapper, totalCount, rsrp3Count, rsrp5Count);

	}

	private void setNeighbourData(FloorWiseWrapper wrapper, Integer totalCount, Integer rsrp3Count,
			Integer rsrp5Count) {
		Double rsrp3dbRate = ReportUtil.getPercentage(rsrp3Count, totalCount);
		Double rsrp5dbRate = ReportUtil.getPercentage(rsrp5Count, totalCount);
		if (rsrp3dbRate != null) {
			if (rsrp3dbRate >= 80.0) {
				wrapper.setOpServer3Db(Symbol.BRACKET_OPEN_STRING + rsrp3dbRate + Symbol.SPACE_STRING
						+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
			} else {
				wrapper.setOpServer3Db(Symbol.BRACKET_OPEN_STRING + rsrp3dbRate + Symbol.SPACE_STRING
						+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

			}
		}

		if (rsrp5dbRate != null) {
			if (rsrp5dbRate >= 95.0) {
				wrapper.setOpServer5Db(Symbol.BRACKET_OPEN_STRING + rsrp5dbRate + Symbol.SPACE_STRING
						+ ReportConstants.PASS + Symbol.BRACKET_CLOSE_STRING);
			} else {
				wrapper.setOpServer5Db(Symbol.BRACKET_OPEN_STRING + rsrp5dbRate + Symbol.SPACE_STRING
						+ ReportConstants.FAIL + Symbol.BRACKET_CLOSE_STRING);

			}
		}
	}
	public String getFloorplanImg(Integer recepiMappingId, String opName, DriveDataWrapper driveDataWrapper,
			String floorplanJson, String localDirPath) throws IOException {
		String floorPlanImagePath =
				localDirPath + Symbol.SLASH_FORWARD + ReportConstants.IB_REPORT_IMAGE_NAME_PREFIX + recepiMappingId + ReportConstants.UNDERSCORE
						+ opName + ReportConstants.IMAGE_FILE_EXTENSION;
		logger.info("floorPlanImagePath {}", floorPlanImagePath);

		BufferedImage imgPath = getBgImage(floorplanJson, driveDataWrapper);
		if (imgPath != null) {
			ImageIO.write(imgPath, ReportConstants.JPEG, new File(floorPlanImagePath));
		}
		return floorPlanImagePath;
	}

	private BufferedImage getBgImage(String floorplanJson, DriveDataWrapper driveDataWrapper) {
		try {
			BufferedImage imBuff = null;
			boolean isbgImageAvailable = FloorPlanJsonParser.isImagePickedFromGallery(floorplanJson);
			if (isbgImageAvailable) {
				logger.info("driveDataWrapper.getFilePath() {}", driveDataWrapper.getFilePath());

				String localFilepath = nvLayer3hdfsDao.copyFileFromHdfsToLocalPath(driveDataWrapper.getFilePath(),
						ConfigUtils.getString(ReportConstants.IBREPORT_FLOORPLAN_IMAGE_PATH), ReportConstants.FLOORPLANNAME + ReportConstants.DOT_ZIP);
				logger.info("Going to process zip file {}  ", localFilepath);

				try (ZipFile zipFile = new ZipFile(localFilepath)) {
					Enumeration<? extends ZipEntry> entries = zipFile.entries();
					while (entries.hasMoreElements()) {
						ZipEntry entry = entries.nextElement();
						if (entry.getName().contains(ReportConstants.BACKGROUNDIMAGE)) {
							//						logger.info("inside the FloorPlanImage image  ");
							InputStream is = zipFile.getInputStream(entry);
							imBuff = ImageIO.read(is);
						}
					}
				}
			}
			return imBuff;
		} catch (JSONException e) {
			logger.error("JSONException inside method  getBgImagePath {} ", e.getMessage());
		} catch (Exception e) {
			logger.error("Exception inside method getBgImagePath {} ", e.getMessage());
		}
		return null;
	}

}
