package com.inn.foresight.module.nv.inbuilding.service.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.io.FileUtils;
import com.inn.commons.io.IOUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.inbuilding.dao.INVIBUnitResultDao;
import com.inn.foresight.module.nv.inbuilding.service.InBuildingLayer3Service;
import com.inn.foresight.module.nv.inbuilding.utils.NVBuildingUtils;
import com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.utils.IBReportUtils;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.FloorPlanJsonParser;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.impl.InBuildingHeatMapGenerator;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

@Service("InBuildingLayer3ServiceImpl")
public class InBuildingLayer3ServiceImpl implements InBuildingLayer3Service {
	private Logger logger = LogManager.getLogger(InBuildingLayer3ServiceImpl.class);

	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	@Autowired
	private INVLayer3HDFSDao nvLayer3hdfsDao;
	@Autowired
	private IReportService reportService;
	@Autowired
	ILegendRangeDao legendRangeDao;
	@Autowired
	INVIBUnitResultDao nvIBUnitResultDao;
	@Autowired
	IGenericWorkorderDao genricWorkorderDao;

	@Override
	public Response getFloorPlanImage(Integer woId, Integer recipeId, String operatorName, Boolean isDrive,
			HttpServletRequest request) {

		logger.info("Going to getFloorPlanImage URL for woId id :{},recipeId{},operatorName {} ,isDrive {},request {}",
				woId, recipeId, operatorName, isDrive, request.getHeaderNames());
		logger.info("getFloorPlanImage csrfToken is {}", request.getHeader("csrfToken"));
		HttpResponse response = NVBuildingUtils
				.sendGetRequestWithoutTimeOut(ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
						+ ConfigUtils.getString(InBuildingConstants.DOWNLOAD_FLOOR_PLAN_FOR_LAYR3_URL)
								.concat(request.getQueryString()));
		Response responseToReturn = null;
		try {
			InputStream stream = response.getEntity().getContent();
			Response.ResponseBuilder builder = Response.status(NVConstant.STATUS_CODE_200);
			responseToReturn = builder.entity(stream)
					.header(NVConstant.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM).build();

		} catch (Exception e) {
			logger.error("Error in method getFloorPlanImage:{}", ExceptionUtils.getStackTrace(e));
		}
		return responseToReturn;

	}

	@Override
	public Response drawFloorPlanImgForLayer3(Integer woId, Integer recipeId, String operator, Boolean isDrive) {
		logger.info("inside the method drawFloorPlanImgForLayer3 woId {} recipeId {},operator{} isDrive{}", woId,
				recipeId, operator, isDrive);
		String localDirPath = ConfigUtils.getString(ReportConstants.INBUILDING_REPORT_PATH) + new Date().getTime();
		ReportUtil.createDirectory(localDirPath);
		try {
			return drawFloorPlanAndSaveIntoHDFS(woId, recipeId, operator, isDrive, localDirPath);
		} catch (Exception e) {
			logger.error("Error inside the method drawFloorPlanImgForLayer3 {}", Utils.getStackTrace(e));
		} finally {

			try {
				FileUtils.deleteDirectory(new File(localDirPath));
			} catch (IOException e) {
				logger.error("Error in deleting local direocory {} {}", localDirPath, e.getMessage());
			}

		}
		return null;
	}

	private Response drawFloorPlanAndSaveIntoHDFS(Integer woId, Integer recipeId, String operator, Boolean isDrive,
			String localDirPath) {
		String driveImagePath = null;
		DriveDataWrapper driveDataWrapper = getDataWrapperForRecipeId(woId, recipeId, operator);
		logger.info("driveDataWrapper is =={}", driveDataWrapper);
		if (driveDataWrapper != null && driveDataWrapper.getJson() != null) {
			logger.info("getJson is {}", driveDataWrapper.getJson());
			String imgFloorPlan = null;
			try {
				imgFloorPlan = getFloorplanImg(recipeId, operator, driveDataWrapper, driveDataWrapper.getJson(),
						localDirPath, woId, isDrive);
			} catch (IOException e) {
				logger.error("IOException occur inside the method getFloorplanImg {} ", e.getMessage());
			}
			List<LegendWrapper> legendList = new ArrayList<>();
			List<String[]> arlist = new ArrayList<>();
			Map<String, Integer> kpiIndexMap = new HashMap<>();

			if (imgFloorPlan != null) {
				String isEnabled = ConfigUtils.getString(NVLayer3Constants.IS_LAYER3_FRAMEWORK_ENABLED);

				if (ReportConstants.TRUE.equalsIgnoreCase(isEnabled)) {
					try {
						List<String> fetchKPIList = new ArrayList<>();

						kpiIndexMap.put(ReportConstants.DB_TIMESTAMP_KEY, 0);
						kpiIndexMap.put(ReportConstants.X_POINT, 1);
						kpiIndexMap.put(ReportConstants.Y_POINT, 2);
						kpiIndexMap.put(ReportConstants.RSRP, 3);

						fetchKPIList.add(ReportConstants.DB_TIMESTAMP_KEY);
						fetchKPIList.add("xpoint");
						fetchKPIList.add("ypoint");
						fetchKPIList.add("RSRP");

						arlist = getInbuildingDriveDataForFrameWork(woId, recipeId, fetchKPIList);
						legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
						IBReportUtils.getInstance().drawFloorPlan(imgFloorPlan, driveDataWrapper.getJson(),
								 arlist, QMDLConstant.ONE, QMDLConstant.TWO);

					} catch (Exception e) {
						logger.error("exception inside method getInbuildingDriveDataForFrameWork for floorPlanPath {}",
								Utils.getStackTrace(e));
					}

				} else {
					String combineData = nvLayer3DashboardService.getDriveDetailReceipeWise(woId,
							Arrays.asList(String.valueOf(recipeId)), Arrays.asList(operator));
					Map<String, String> dataMap = ReportUtil.convertCSVStringToMap(combineData);
					String data = dataMap.get(ReportConstants.RESULT);
					arlist = ReportUtil.convertCSVStringToDataList(data);

					legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);

					IBReportUtils.getInstance().drawFloorPlan(imgFloorPlan, driveDataWrapper.getJson(),
							ReportConstants.FLOORPLANNAME, arlist);

				}

				putImageInHDFS(imgFloorPlan, InBuildingConstants.NONDRIVE, woId, recipeId, operator);

				if (isDrive) {
					if (ReportConstants.TRUE.equalsIgnoreCase(isEnabled)) {
						// For New Layer3 Framework
						driveImagePath = drawRsrpImageForFramework(woId, recipeId, operator, localDirPath, imgFloorPlan,
								arlist, legendList, kpiIndexMap);
					} else {
						// for old micro service call
						driveImagePath = drawRsrpImage(woId, recipeId, operator, localDirPath, imgFloorPlan, arlist,
								legendList);
					}
					if (driveImagePath != null) {
						putImageInHDFS(driveImagePath, InBuildingConstants.DRIVE, woId, recipeId, operator);
					}
				}
				return getFinalImage(driveImagePath, imgFloorPlan, isDrive);
			}
		}
		return null;
	}

	private List<String[]> getInbuildingDriveDataForFrameWork(Integer workorderId, Integer recipeId,
			List<String> fetchKPIList) {
		List<String> recipeListTemp = new ArrayList<>();
		recipeListTemp.add(String.valueOf(recipeId));
		return reportService.getDriveDataForReport(workorderId, recipeListTemp, fetchKPIList);
	}

	private DriveDataWrapper getDataWrapperForRecipeId(Integer woId, Integer recipeId, String operator) {
		GenericWorkorder workorder = genricWorkorderDao.findByPk(woId);
		DriveDataWrapper driveDataWrapper = null;
		if (GenericWorkorder.TemplateType.NV_IB_BENCHMARK.equals(workorder.getTemplateType())) {
			logger.info("getDataWrapperForRecipeId inside if ");
			driveDataWrapper = nvLayer3DashboardService.getFloorplanDataFromLayer3Report(woId,
					ReportConstants.BENCHMARK_OPERATOR_STRING, String.valueOf(recipeId));
		} else {
			logger.info("getDataWrapperForRecipeId inside else ");

			driveDataWrapper = nvLayer3DashboardService.getFloorplanDataFromLayer3Report(woId, operator,
					String.valueOf(recipeId));
		}
		return driveDataWrapper;
	}

	private Response getFinalImage(String driveImagePath, String imgFloorPlan, Boolean isDrive) {
		FileInputStream inputStream = null;
		String filePath = null;
		try {
			if (driveImagePath != null && isDrive) {
				filePath = driveImagePath;
				inputStream = new FileInputStream(driveImagePath);
			} else if (imgFloorPlan != null) {
				filePath = imgFloorPlan;
				inputStream = new FileInputStream(imgFloorPlan);
			}
			byte[] byteArray = IOUtils.toByteArray(inputStream);
			Response.ResponseBuilder builder = Response.status(NVConstant.STATUS_CODE_200);

			return builder.entity(byteArray).header(NVConstant.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
					.header(NVConstant.CONTENT_DISPOSITION,
							NVConstant.ATTACHMENT_FILE_NAME + ReportUtil.getFileNameFromFilePath(filePath))
					.build();
		} catch (Exception e) {
			logger.error("exception inside method getFinalImage {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private void putImageInHDFS(String imagePathLocal, String drive, Integer woId, Integer recipeId, String operator) {
		logger.info("inside the method putImageInHDFS imagePathLocal {}", imagePathLocal);
		try (FileInputStream inputStream = new FileInputStream(new File(imagePathLocal))) {

			String destination = ConfigUtils.getString(NVConfigUtil.INBUILDING_LAYER3_IMAGE_CONFIG_PATH)
					+ ReportConstants.FORWARD_SLASH + getImageFileName(woId, recipeId, operator, drive);
			nvLayer3hdfsDao.persistWORecipeQMDLFileToHDFS(inputStream, destination);
		} catch (Exception e) {
			logger.info("Exception inside the method putImageInHDFS {}", Utils.getStackTrace(e));
		}

	}

	private static String getImageFileName(Integer woId, Integer recipeId, String operator, String drive) {
		return drive + ReportConstants.FLOORPLANNAME + ReportConstants.UNDERSCORE + woId + ReportConstants.UNDERSCORE
				+ recipeId + ReportConstants.UNDERSCORE + operator + ReportConstants.IMAGE_FILE_EXTENSION;

	}

	private String drawRsrpImageForFramework(Integer woId, Integer recipeId, String operator, String localDirPath,
			String imgFloorPlan, List<String[]> arlist, List<LegendWrapper> legendList,
			Map<String, Integer> kpiIndexMap) {
		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, kpiIndexMap);
		kpiList = kpiList.stream().filter(x -> x.getKpiName().equalsIgnoreCase(ReportConstants.RSRP))
				.collect(Collectors.toList());
		kpiList = reportService.setKpiStatesIntokpiList(kpiList, woId, Arrays.asList(String.valueOf(recipeId)),
				Arrays.asList(operator));

		Map<String, String> heatMaps = InBuildingHeatMapGenerator.getInstance().generateHeatMapsForReport(arlist,
				localDirPath, imgFloorPlan, kpiList, kpiIndexMap);

		BufferedImageEditor editor = new BufferedImageEditor(heatMaps.get(ReportConstants.RSRP));
		File img = editor.finalizeImageToOutput(
				localDirPath + File.separator + getImageFileName(woId, recipeId, operator, InBuildingConstants.DRIVE),
				BufferedImageEditor.OUTPUT_FORMAT_JPEG);
		return img.getAbsolutePath();

	}

	private String drawRsrpImage(Integer woId, Integer recipeId, String operator, String localDirPath,
			String imgFloorPlan, List<String[]> arlist, List<LegendWrapper> legendList) {
		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList,
				DriveHeaderConstants.getKpiIndexMap());
		kpiList = kpiList.stream().filter(x -> x.getKpiName().equalsIgnoreCase(ReportConstants.RSRP))
				.collect(Collectors.toList());
		kpiList = reportService.setKpiStatesIntokpiList(kpiList, woId, Arrays.asList(String.valueOf(recipeId)),
				Arrays.asList(operator));
		Map<String, String> heatMaps = InBuildingHeatMapGenerator.getInstance().generateHeatMaps(arlist, localDirPath,
				imgFloorPlan, kpiList);
		BufferedImageEditor editor = new BufferedImageEditor(heatMaps.get(ReportConstants.RSRP));
		File img = editor.finalizeImageToOutput(
				localDirPath + File.separator + getImageFileName(woId, recipeId, operator, InBuildingConstants.DRIVE),
				BufferedImageEditor.OUTPUT_FORMAT_JPEG);
		return img.getAbsolutePath();

	}

	private String getFloorplanImg(Integer recepiMappingId, String opName, DriveDataWrapper driveDataWrapper,
			String floorplanJson, String localDirPath, Integer woId, Boolean isDrive) throws IOException {
		String floorPlanImagePath = localDirPath + Symbol.SLASH_FORWARD + getImageFileName(woId, recepiMappingId,
				opName, isDrive ? InBuildingConstants.DRIVE : InBuildingConstants.NONDRIVE);
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
						ConfigUtils.getString(ReportConstants.IBREPORT_FLOORPLAN_IMAGE_PATH),
						ReportConstants.FLOORPLANNAME + ReportConstants.DOT_ZIP);
				logger.info("Going to process zip file {}  ", localFilepath);

				try (ZipFile zipFile = new ZipFile(localFilepath)) {
					Enumeration<? extends ZipEntry> entries = zipFile.entries();
					while (entries.hasMoreElements()) {
						ZipEntry entry = entries.nextElement();
						if (entry.getName().contains(ReportConstants.BACKGROUNDIMAGE)) {
							logger.info("inside the FloorPlanImage image  ");
							InputStream is = zipFile.getInputStream(entry);
							imBuff = ImageIO.read(is);
							BufferedImage newBufferedImage = new BufferedImage(imBuff.getWidth(), imBuff.getHeight(),
									BufferedImage.TYPE_INT_RGB);
							newBufferedImage.createGraphics().drawImage(imBuff, 0, 0, Color.WHITE, null);
							return newBufferedImage;
						}
					}
				}
			}
		} catch (JSONException e) {
			logger.error("JSONException inside method  getBgImagePath {} ", e.getMessage());
		} catch (Exception e) {
			logger.error("Exception inside method getBgImagePath {} ", e.getMessage());
		}
		return null;
	}

	@Override
	public NVIBResultWrapper getBuildingInFoByWoId(Integer woId) {
		logger.info("Insside the method getBuildingInFoByWoId {}", woId);
		try {
			return nvIBUnitResultDao.getBuildingInFoByWoId(woId);
		} catch (DaoException e) {
			logger.error("Error insdie The method getBuildingInFoByWoId {}", e.getMessage());
		}
		return null;
	}

	@Override
	public Response getKpiAvgByBuildingId(Integer buildingId, Integer floorId, Long startTime, Long endTime) {
		logger.info(
				"Exception inside the ,method getKpiAvgByBuildingId buildigId {} floorId {}  startTime {} endTime{}",
				buildingId, floorId, startTime, endTime);
		try {
			Date startDate = null;
			Date endDate = null;

			if (startTime != null) {
				startDate = new Date(startTime);
			}
			if (endTime != null) {
				endDate = new Date(endTime);

			}
			List<NVIBResultWrapper> dataList = nvIBUnitResultDao.getNVIbResultByBuildingId(buildingId, floorId,
					startDate, endDate);
			logger.info("dataList {}", new Gson().toJson(dataList));
			return Response.ok(dataList).build();
		} catch (NoResultException ne) {
			return Response.ok(NVWorkorderConstant.NO_DATA_FOUND_JSON).build();
		} catch (Exception e) {
			logger.error("Error in getKpiAvgByBuildingId == : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();

		}

	}

	@Override
	public NVIBResultWrapper getKpiAvgByFloorId(Integer floorId, Integer band, Long startTime, Long endTime) {
		logger.info("inside the method getKpiAvgByFloorId  floorId {} band {} startTime {} endTime{}", floorId, band,
				startTime, endTime);
		try {
			Date startDate = null;
			Date endDate = null;

			if (startTime != null) {
				startDate = new Date(startTime);
			}
			if (endTime != null) {
				endDate = new Date(endTime);

			}
			return nvIBUnitResultDao.getKpiAvgByFloorId(floorId, band, startDate, endDate);
		}

		catch (Exception e) {
			logger.error("Error in getKpiAvgByBuildingId == : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	@Override
	public List<NVIBResultWrapper> getRelativeChangeForRsrp(Integer buildingId, Integer floorId) {
		logger.info("inside the method getRelativeChangeForRsrp buildingId {} floorId {} ", buildingId, floorId);
		try {
			List<NVIBResultWrapper> newDataList = new ArrayList<>();
			List<NVIBResultWrapper> list = nvIBUnitResultDao.getDateWiseTestResultByBuildingId(buildingId, floorId);
			list = list.stream().filter(wrapper -> wrapper.getBand() != null).collect(Collectors.toList());
			Map<Integer, List<NVIBResultWrapper>> bandWiseMap = list.stream()
					.collect(Collectors.groupingBy(NVIBResultWrapper::getBand));
			logger.info("list  size is  ===={}", list != null ? list.size() : null);

			for (Entry<Integer, List<NVIBResultWrapper>> map : bandWiseMap.entrySet()) {
				NVIBResultWrapper wrapper = new NVIBResultWrapper();
				if (map.getValue() != null && !map.getValue().isEmpty()
						&& map.getValue().size() > ForesightConstants.ONE) {
					List<NVIBResultWrapper> subList = map.getValue().subList(ForesightConstants.ZERO,
							ForesightConstants.TWO);
					NVIBResultWrapper current = subList.get(ForesightConstants.ZERO);
					NVIBResultWrapper previous = subList.get(ForesightConstants.ONE);
					Double currentRsrp = current.getRsrp();
					Double prviousRsrp = previous.getRsrp();
					if (currentRsrp != null && prviousRsrp != null) {
						wrapper.setRsrp(((Math.abs(currentRsrp) - Math.abs(prviousRsrp)) / currentRsrp) * 100);
					}

				}
				wrapper.setBand(map.getKey());
				newDataList.add(wrapper);
			}
			return newDataList;

		} catch (Exception ne) {
			logger.error("Error in getRelativeChangeForRsrp == : {} ", ExceptionUtils.getStackTrace(ne));

			throw new RestException(ne);
		}
	}

}
