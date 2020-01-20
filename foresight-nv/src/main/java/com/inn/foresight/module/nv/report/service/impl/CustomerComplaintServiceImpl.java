package com.inn.foresight.module.nv.report.service.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.ILayer3PPEService;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.service.ICustomerComplaintService;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.LegendUtil;
import com.inn.foresight.module.nv.report.utils.LiveDriveReportUtil;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.utils.Statistics;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.AvrageKpiWrapper;
import com.inn.foresight.module.nv.report.wrapper.CustomerComplaintSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.CustomerComplaintWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPIImgDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("CustomerComplaintServiceImpl")
public class CustomerComplaintServiceImpl implements ICustomerComplaintService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(CustomerComplaintServiceImpl.class);

	/** The map image service. */

	@Autowired
	private IMapImagesService mapImageService;

	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;

	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;

	@Autowired
	private IReportService reportService;

	@Autowired
	private ILayer3PPEService iLayer3PPEService;

	/** The mapper. */
	ObjectMapper mapper = new ObjectMapper();

	public CustomerComplaintServiceImpl() {
		super();
	}

	@Override
	public Response execute(String json) {
		logger.info("Going to execute the method with json {} ", json);
		String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.CUSTOMER_COMPLAINT
				+ ReportConstants.FORWARD_SLASH;
		CustomerComplaintWrapper mainWrapper = new CustomerComplaintWrapper();
		try {
			Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
			Integer previousWoId = (Integer) jsonMap.get(ReportConstants.PREV_WORKORDER_ID);
			Integer currentWoId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
			String assignTo = (String) jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY);
			Integer analyticsrepoId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			GenericWorkorder prevWork = iGenericWorkorderDao.findByPk(previousWoId);
			GenericWorkorder postWork = iGenericWorkorderDao.findByPk(currentWoId);
			List<Integer> workorderIdList = new ArrayList<>();
			if(prevWork != null && postWork != null) {
				workorderIdList.add(prevWork.getId());
				workorderIdList.add(postWork.getId());
			}
			boolean qmdlParsingStatus = reportService.getFileProcessedStatusForWorkorders(workorderIdList);
			if (qmdlParsingStatus) {
				return generateCustomerComplainReport(filePath, mainWrapper, jsonMap, previousWoId, currentWoId,
						analyticsrepoId, prevWork, postWork);
			}
		} catch (Exception e1) {
			logger.error("Error inside the method createReportForWorkOrderID for json {} , {} ", json,
					Utils.getStackTrace(e1));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	private Response generateCustomerComplainReport(String filePath, CustomerComplaintWrapper mainWrapper,
			Map<String, Object> jsonMap, Integer previousWoId, Integer currentWoId, Integer analyticsrepoId,
			GenericWorkorder prevWork, GenericWorkorder postWork) {
		try {
			CustomerComplaintSubWrapper wrapper = getComplaintWrapperData(jsonMap);
			mainWrapper.setFileName(
					ReportUtil.getFileName(postWork != null ? postWork.getWorkorderId() : prevWork.getWorkorderId(),
							analyticsrepoId, filePath));
			mainWrapper.setSubList(populateImageDataInWrapper(wrapper, Arrays.asList(previousWoId, currentWoId)));
			File file = proceedToCreateComplaintReport(new HashMap<String, Object>(), mainWrapper);
			String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.MASTER
					+ ReportConstants.FORWARD_SLASH;
			return reportService.saveFile((Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY), hdfsFilePath,
					file);
		} catch (Exception e) {
			logger.error("Exception inside the method getComplaint Report {}", Utils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}

	private File proceedToCreateComplaintReport(Map<String, Object> imageMap, CustomerComplaintWrapper mainWrapper) {
		logger.info("inside the method proceedToCreateComplaintReport ");
		try {
			String reportAssetRepo = ConfigUtils.getString(ReportConstants.COMPLAINT_REPORT_JASPER_FOLDER_PATH);
			List<CustomerComplaintWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
			imageMap.put(ReportConstants.IMAGE_PARAM_HEADER_BG, reportAssetRepo + ReportConstants.IMAGE_HEADER_BG);
			imageMap.put(ReportConstants.IMAGE_PARAM_HEADER_LOG, reportAssetRepo + ReportConstants.IMAGE_HEADER_LOG);
			imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_LOG, reportAssetRepo + ReportConstants.IMAGE_SCREEN_LOGO);
			imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_BG, reportAssetRepo + ReportConstants.IMAGE_SCREEN_BG);
			String destinationFileName = mainWrapper.getFileName();
			JasperRunManager.runReportToPdfFile(reportAssetRepo + ReportConstants.MAIN_JASPER, destinationFileName,
					imageMap, rfbeanColDataSource);
			logger.info("Report Created successfully  ");

			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@proceedToCreateComplaintReport getting err={}", Utils.getStackTrace(e));
		}
		logger.info(
				"@proceedToCreateComplaintReport going to return null as there has been some problem in generating report");
		return null;
	}

	private List<CustomerComplaintSubWrapper> populateImageDataInWrapper(CustomerComplaintSubWrapper wrapper,
			List<Integer> listWorkorderId)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		logger.info("Inside getImageMapforWorkOrderIds : " + listWorkorderId.toString());
		List<CustomerComplaintSubWrapper> subList = new ArrayList<>();
		List<KPIImgDataWrapper> listofKpiImg = new ArrayList<>();
        
		Boolean isPre = getIsPreDefaultValue(listWorkorderId);
		for (Integer workorderId : listWorkorderId) {
			logger.info("workorderId ===={}", workorderId);
			
			Set<String> dynamicKpis = reportService.getDynamicKpiName(Arrays.asList(workorderId), null, Layer3PPEConstant.ADVANCE);
			List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs().stream().filter(k -> dynamicKpis.contains(k))
					.collect(Collectors.toList());
            Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
			
            getKPiImageDataListForWorkOrderID(workorderId, listofKpiImg, isPre, kpiIndexMap, fetchKPIList);
			getKpiSummaryDataListForWorkOrderID(workorderId, isPre, wrapper);
			if (isPre != null) {
				isPre = true;
			}
		}
		wrapper.setImgList(listofKpiImg);
		subList.add(wrapper);
		return subList;
	}

	private CustomerComplaintSubWrapper getKpiSummaryDataListForWorkOrderID(Integer workorderId, Boolean isPre,
			CustomerComplaintSubWrapper wrapper) {
		Map<String, List<String>> map = nvLayer3DashboardService.getDriveRecipeDetail(workorderId);
		try {
			logger.info("Drive recipe Map {} ", new Gson().toJson(map));

			//Set<String> dynamicKpisForSummary = reportService.getDynamicKpiName(workorderId, null, Layer3PPEConstant.SUMMARY);
			List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
			Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper
					.getLiveDriveKPIIndexMap(fetchSummaryKPIList);

			if (map != null) {
				String[] summaryData = reportService.getSummaryDataForReport(workorderId, map.get(QMDLConstant.RECIPE),
						fetchSummaryKPIList);
				GenericWorkorder workOrder = iGenericWorkorderDao.findByPk(workorderId);
				if (isPre) {
					wrapper.setPreList(setSummaryData(summaryData, workOrder, summaryKpiIndexMap));
				} else {
					wrapper.setPostList(setSummaryData(summaryData, workOrder, summaryKpiIndexMap));
				}
			}
		} catch (Exception e) {
			logger.warn("exception to set summary data {}", e.getMessage());
		}
		return wrapper;
	}

	private List<AvrageKpiWrapper> setSummaryData(String[] summaryData, GenericWorkorder workOrder,
			Map<String, Integer> summaryKpiIndexMap) {
		AvrageKpiWrapper wrapper = new AvrageKpiWrapper();
		List<AvrageKpiWrapper> list = new ArrayList<>();
		try {	
				if (summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.DL_THROUGHPUT) != null && summaryData[summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.DL_THROUGHPUT)] != null
						&& !summaryData[summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.DL_THROUGHPUT)].isEmpty()) {
					wrapper.setAvgDl(ReportUtil.parseToFixedDecimalPlace(
							Double.parseDouble(summaryData[summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.DL_THROUGHPUT)]),
							ReportConstants.INDEX_THREE));
				}
				if (summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.SINR) != null  && summaryData[summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.SINR)] != null
						&& !summaryData[summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.SINR)].isEmpty()) {
					wrapper.setAvgSinr(ReportUtil.parseToFixedDecimalPlace(
							Double.parseDouble(summaryData[summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.SINR)]),
							ReportConstants.INDEX_THREE));
				}
				if (summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.RSRP) != null && summaryData[summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.RSRP)] != null
						&& !summaryData[summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.RSRP)].isEmpty()) {
					wrapper.setAvgRsrp(ReportUtil.parseToFixedDecimalPlace(
							Double.parseDouble(summaryData[summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.RSRP)]),
							ReportConstants.INDEX_THREE));
				}
				if (summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.UL_THROUGHPUT) != null && summaryData[summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.UL_THROUGHPUT)] != null
						&& !summaryData[summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.UL_THROUGHPUT)].isEmpty()) {
					wrapper.setAvgUl(ReportUtil.parseToFixedDecimalPlace(
							Double.parseDouble(summaryData[summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.UL_THROUGHPUT)]),
							ReportConstants.INDEX_THREE));
				}

			if (workOrder != null && workOrder.getStartDate() != null) {
				String startDate = (workOrder != null && workOrder.getStartDate() != null)
						? ReportUtil.parseDateToString(ReportConstants.DATE_FORMAT_DD_SP_MM_SP_YY,
								workOrder.getStartDate())
						: ReportUtil.parseDateToString(ReportConstants.DATE_FORMAT_DD_SP_MM_SP_YY,
								workOrder.getCompletionTime());
				wrapper.setStartDate(startDate);
				wrapper.setWorkorderName(workOrder.getWorkorderId());
			}
			list.add(wrapper);
		} catch (Exception e) {
			logger.warn("Exception inside the method setSummaryData{}", Utils.getStackTrace(e));
		}
		return list;
	}

	private Boolean getIsPreDefaultValue(List<Integer> listWorkorderId) {
		if (listWorkorderId != null && listWorkorderId.size() == ReportConstants.INDEX_TWO) {
			return false;
		} else {
			return null;
		}
	}

	private List<KPIImgDataWrapper> getKPiImageDataListForWorkOrderID(Integer workorderId,
			List<KPIImgDataWrapper> listofKpiImg, Boolean isPre, Map<String, Integer> kpiIndexMap,
			List<String> fetchKPIList) {
		logger.info("Inside method getKPiImageDataListForWorkOrderID for workOrder Id {} ,isPre {} ", workorderId,
				isPre);
		try {
			Map<String, List<String>> map = nvLayer3DashboardService.getDriveRecipeDetail(workorderId);
			if (map != null) {
				logger.debug("Drive recipe Map {} ", new Gson().toJson(map));
				List<KPIWrapper> kpiList = reportService.getKPiStatsDataList(workorderId, map, kpiIndexMap,
						ReportConstants.PREPOST);
				DriveImageWrapper driveImageWrapper = getDriveImageWrapperDataForImageGenearation(workorderId, map,
						kpiList, kpiIndexMap, fetchKPIList);
				return getListofImages(driveImageWrapper, kpiList, listofKpiImg, workorderId, isPre, kpiIndexMap);
			}
		} catch (Exception e) {
			logger.error("Excption in getKPiImageDataListForWorkOrderID : {} ", Utils.getStackTrace(e));
		}
		return listofKpiImg;
	}

	private List<KPIImgDataWrapper> getListofImages(DriveImageWrapper driveImageWrapper, List<KPIWrapper> kpiList,
			List<KPIImgDataWrapper> listofKpiImg, Integer workorderId, Boolean isPre,
			Map<String, Integer> kpiIndexMap) {
		logger.info("Inside method getListofImages for workOrder ID {} ", workorderId);
		try {
			Map<String, BufferedImage> driveImageMap = mapImageService.getDriveImagesForReport(driveImageWrapper, null,
					kpiIndexMap);

			Map<String, BufferedImage> legendImageMap = mapImageService.getLegendImages(kpiList,
					driveImageWrapper.getDataKPIs());

			Map<String, BufferedImage> statisticsImageMap = getStatsImages(kpiList, driveImageWrapper.getDataKPIs(),
					kpiIndexMap);

			logger.info("kpiList data {}  ", new Gson().toJson(kpiList));
			kpiList.stream().forEach(kpiWrapperObj -> {
				try {
					if ((kpiIndexMap.get(ReportConstants.PCI_PLOT)!=null && kpiWrapperObj.getIndexKPI().toString()
							.equalsIgnoreCase((kpiIndexMap.get(ReportConstants.PCI_PLOT)).toString()))
							||(kpiIndexMap.get(ReportConstants.DL_EARFCN)!=null && kpiWrapperObj.getIndexKPI().toString()
									.equalsIgnoreCase((kpiIndexMap.get(ReportConstants.DL_EARFCN)).toString()))) {
						setCustomizedData(isPre, kpiWrapperObj, listofKpiImg, driveImageMap);
					} else {
						setDataInWrapper(isPre, kpiWrapperObj, listofKpiImg, driveImageMap, legendImageMap,
								statisticsImageMap);
					}
				} catch (Exception e) {
					logger.error("Exception inside method getListofImages {} ", Utils.getStackTrace(e));
				}
			});
			return listofKpiImg;
		} catch (Exception e) {
			logger.error("Exception inside method getListofImages {} ", Utils.getStackTrace(e));
		}
		return listofKpiImg;
	}

	private void setCustomizedData(Boolean isPre, KPIWrapper kpiWrapperObj, List<KPIImgDataWrapper> listofKpiImg,
			Map<String, BufferedImage> driveImageMap) {
		logger.info("Inside method setCustomizedData  is Pre {} , kpiwrapperObj {} ", isPre, kpiWrapperObj);
		try {
			if (!isPre) {
				KPIImgDataWrapper kpiImgWrapper = new KPIImgDataWrapper();
				kpiImgWrapper.setKpiName(
						kpiWrapperObj.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE));
				kpiImgWrapper.setBeforImg(getInputStreamFromBufferedImage(
						driveImageMap.get(kpiWrapperObj.getIndexKPI() + ReportConstants.BLANK_STRING)));
				if (kpiWrapperObj.getKpiName().equalsIgnoreCase(ReportConstants.PCI_PLOT)) {
					kpiImgWrapper.setBeforLegendImg(
							getInputStreamFromBufferedImage(driveImageMap.get(ReportConstants.KEY_LEGENDS)));
				} else {
					kpiImgWrapper.setBeforLegendImg(
							getInputStreamFromBufferedImage(driveImageMap.get(kpiWrapperObj.getKpiName())));
				}
				listofKpiImg.add(kpiImgWrapper);
				logger.info("value of listofkpiImg Inside method setCustomizedData {} ", listofKpiImg);
			} else {
				List<KPIImgDataWrapper> kpiImgWrapperList = listofKpiImg.stream()
						.filter(obj -> obj.getKpiName().equalsIgnoreCase(kpiWrapperObj.getKpiName()
								.replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE)))
						.collect(Collectors.toList());
				logger.info("kpiImgWrapperList {}", kpiImgWrapperList);
				if (kpiImgWrapperList != null && !kpiImgWrapperList.isEmpty()) {
					KPIImgDataWrapper kpiImgWrapper = kpiImgWrapperList.get(ReportConstants.INDEX_ZER0);
					kpiImgWrapper.setAfterImg(getInputStreamFromBufferedImage(
							driveImageMap.get(kpiWrapperObj.getIndexKPI() + ReportConstants.BLANK_STRING)));
					if (kpiWrapperObj.getKpiName().equalsIgnoreCase(ReportConstants.PCI_PLOT)) {
						kpiImgWrapper.setAfterLegendImg(
								getInputStreamFromBufferedImage(driveImageMap.get(ReportConstants.KEY_LEGENDS)));
					} else {
						kpiImgWrapper.setAfterLegendImg(
								getInputStreamFromBufferedImage(driveImageMap.get(kpiWrapperObj.getKpiName())));
					}
				}
			}
		} catch (Exception e) {
			logger.info("Exception inside method setDataInWrapper {} ", e.getMessage());
		}
	}

	private void setDataInWrapper(Boolean isPre, KPIWrapper kpiWrapperObj, List<KPIImgDataWrapper> listofKpiImg,
			Map<String, BufferedImage> driveImageMap, Map<String, BufferedImage> legendImageMap,
			Map<String, BufferedImage> statisticsImageMap) {
		logger.info("Inside method setDataInWrapper is Pre {} , kpiwrapperObj {} ", isPre, kpiWrapperObj);
		try {
			if (!isPre) {
				logger.info("inside the if to set image of kpiName {}", kpiWrapperObj.getKpiName());
				InputStream is = getInputStreamFromBufferedImage(
						driveImageMap.get(kpiWrapperObj.getIndexKPI() + ReportConstants.BLANK_STRING));
				if (is != null) {
					KPIImgDataWrapper kpiImgWrapper = new KPIImgDataWrapper();
					kpiImgWrapper.setKpiName(
							kpiWrapperObj.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE));
					kpiImgWrapper.setBeforImg(is);
					kpiImgWrapper.setBeforLegendImg(getInputStreamFromBufferedImage(legendImageMap
							.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiWrapperObj.getIndexKPI())));
					kpiImgWrapper.setBeforStatImg(getInputStreamFromBufferedImage(statisticsImageMap
							.get(ReportConstants.STATS + ReportConstants.UNDERSCORE + kpiWrapperObj.getIndexKPI())));
					listofKpiImg.add(kpiImgWrapper);
					logger.info("value of listofkpiimg {}", listofKpiImg);
				} else {
					logger.info("Input Stream is null for isPre {} , kpi Name {} ", isPre, kpiWrapperObj.getKpiName());
				}
			} else {
				List<KPIImgDataWrapper> kpiImgWrapperList = listofKpiImg.stream()
						.filter(obj -> obj.getKpiName().equalsIgnoreCase(kpiWrapperObj.getKpiName()
								.replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE)))
						.collect(Collectors.toList());
				logger.info("kpiImgWrapperList {}", kpiImgWrapperList);
				if (kpiImgWrapperList != null && !kpiImgWrapperList.isEmpty()) {
					logger.info("inside the else to set image for kpi {}", kpiWrapperObj.getKpiName());
					KPIImgDataWrapper kpiImgWrapper = kpiImgWrapperList.get(ReportConstants.INDEX_ZER0);
					kpiImgWrapper.setAfterImg(getInputStreamFromBufferedImage(
							driveImageMap.get(kpiWrapperObj.getIndexKPI() + ReportConstants.BLANK_STRING)));
					kpiImgWrapper.setAfterLegendImg(getInputStreamFromBufferedImage(legendImageMap
							.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiWrapperObj.getIndexKPI())));
					kpiImgWrapper.setAfterStatImg(getInputStreamFromBufferedImage(statisticsImageMap
							.get(ReportConstants.STATS + ReportConstants.UNDERSCORE + kpiWrapperObj.getIndexKPI())));
				}
			}
		} catch (Exception e) {
			logger.info("Exception inside method setDataInWrapper {} ", e.getMessage());
		}
	}

	private InputStream getInputStreamFromBufferedImage(BufferedImage bufferedImage) {
		if (bufferedImage != null) {
			try {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, ReportConstants.JPG, os);
				return new ByteArrayInputStream(os.toByteArray());
			} catch (Exception e) {
				logger.info("Unable to convert BUfferedImage to inputStream {} ", e.getMessage());
			}
		}
		return null;
	}

	private DriveImageWrapper getDriveImageWrapperDataForImageGenearation(Integer workorderId,
			Map<String, List<String>> map, List<KPIWrapper> kpiList, Map<String, Integer> kpiIndexMap,
			List<String> fetchKPIList) {
		logger.info("Inside method getDriveImageWrapperDataForImageGenearation for workOrder Id {} ", workorderId);
		DriveImageWrapper driveImageWrapper = null;
		try {
			List<String[]> arlist = reportService.getDriveDataForReport(workorderId, map.get(QMDLConstant.RECIPE), fetchKPIList);
		    List<SiteInformationWrapper> siteDataList = reportService.getSiteDataForReportByDataList(arlist,
					kpiIndexMap);
			return new DriveImageWrapper(arlist, kpiIndexMap.get(ReportConstants.LATITUDE),
					kpiIndexMap.get(ReportConstants.LONGITUDE), kpiIndexMap.get(ReportConstants.PCI_PLOT), kpiList,
					siteDataList, null);
		} catch (Exception e) {
			logger.error("Exception inside method getDriveImageWrapperDataForImageGenearation {} ",
					Utils.getStackTrace(e));
		}
		return driveImageWrapper;
	}

	private CustomerComplaintSubWrapper getComplaintWrapperData(Map<String, Object> jsonMap) {
		CustomerComplaintSubWrapper wrapper = new CustomerComplaintSubWrapper();
		Integer previousWoId = (Integer) jsonMap.get(ReportConstants.PREV_WORKORDER_ID);
		GenericWorkorder workorder = null;
		try {
			workorder = iGenericWorkorderDao.findByPk(previousWoId);
		} catch (DaoException e1) {
			logger.error("Error inside the method getComplaintWrapperData{}", e1.getMessage());
		}
		if (workorder != null && workorder.getGwoMeta() != null
				&& workorder.getGwoMeta().size() > ReportConstants.INDEX_ZER0) {
			try {
				Map<String, String> gwoMetaMap = workorder.getGwoMeta();
				wrapper.setComplainerAddress(gwoMetaMap.get(NVWorkorderConstant.COMPLAINER_ADDRESS));
				wrapper.setComplainerName(gwoMetaMap.get(NVWorkorderConstant.COMPLAINER_NAME));
				wrapper.setDateOfComplaint(gwoMetaMap.get(NVWorkorderConstant.COMPLAINT_DATE));
				wrapper.setReasonToComplaint(gwoMetaMap.get(NVWorkorderConstant.COMPLAINT_REASON));
				wrapper.setDateOfComplaint(
						(ReportUtil.parseDateToString(ReportConstants.DATE_FORMAT_DD_SP_MM_SP_YY,
								new Date(Long.parseLong(gwoMetaMap.get(NVWorkorderConstant.COMPLAINT_DATE))))));
				wrapper.setTestDate(ReportUtil.parseDateToString(ReportConstants.DATE_FORMAT_DD_SP_MM_SP_YY,
						workorder.getCompletionTime()));
				return wrapper;
			} catch (Exception e) {
				logger.error("Excetion inside method getComplaintWrapperData {} ", Utils.getStackTrace(e));
			}
		}
		return wrapper;
	}

	
	/**
	 * Sets the kpi states intokpi list.
	 *
	 * @param kpiList
	 *            the kpi list
	 * @param workOrderId
	 *            the work order id
	 * @param recepiList
	 * @param operatorList
	 * @return the list
	 */
	public List<KPIWrapper> setKpiStatesIntokpiList(List<KPIWrapper> kpiList, Integer workOrderId,
			List<String> recepiList, List<String> operatorList) {
		List<KPIWrapper> list = new ArrayList<>();
		kpiList.forEach(kpiWrapper -> {
			kpiWrapper.setKpiStats(getKPiStatsDataFromHbase(workOrderId,
					ReportUtil.getHbaseColumnNameByKpiName(kpiWrapper), recepiList, operatorList));
			list.add(kpiWrapper);
		});
		logger.info("kpiList is ====={}", new Gson().toJson(kpiList));

		return list;
	}

	/**
	 * Gets the k pi stats data from hbase.
	 *
	 * @param workOrderId
	 *            the work order id
	 * @param kpiname
	 *            the kpiname
	 * @return the k pi stats data from hbase
	 */
	private String[] getKPiStatsDataFromHbase(Integer workOrderId, String kpiname, List<String> recepiList,
			List<String> operatorList) {
		String[] kpistats = null;
		try {
			String kpistat = nvLayer3DashboardService.getKpiStatsRecipeDataForReport(workOrderId, kpiname, recepiList,
					operatorList);
			logger.info(" kpiname {} ,kpistat {}", kpiname, kpistat);
			Map<String, String[]> map = mapper.readValue(kpistat, new TypeReference<Map<String, String[]>>() {
			});
			kpistats = map.get(ReportConstants.RESULT);
			logger.info("kpistats {} ", kpistats != null ? Arrays.toString(kpistats) : null);
			return kpistats;
		} catch (IOException | BusinessException e) {
			logger.error("Exception inside method getKPiStatsDataFromHbase {} ", Utils.getStackTrace(e));
		}
		return kpistats;
	}

	private Map<String, BufferedImage> getStatsImages(List<KPIWrapper> kpiList, List<String[]> listArray, Map<String, Integer> kpiIndexMap) {
		logger.info("Inside method getStatsImages for kpiList {} ", kpiList);
		Map<String, BufferedImage> statsImageMap = new HashMap<>();
		try {
			Map<String, List<Double>> kpiWiseValueList = LiveDriveReportUtil.getKPiWiseValueList(listArray, kpiList,
					kpiIndexMap.get(ReportConstants.TEST_TYPE), kpiIndexMap.get(ReportConstants.TIMESTAMP));
			for (int index = ReportConstants.INDEX_ZER0; index < kpiList.size(); index++) {
				KPIWrapper wrapper = kpiList.get(index);
				populateStatsImageMap(statsImageMap, kpiWiseValueList, wrapper);
			}
		} catch (Exception e) {
			logger.info("Exception inside method getStatsImages {} ", Utils.getStackTrace(e));
		}
		logger.info("Stats Map Image Size {}  ", statsImageMap.size());
		return statsImageMap;
	}

	private void populateStatsImageMap(Map<String, BufferedImage> statsImageMap,
			Map<String, List<Double>> kpiWiseValueList, KPIWrapper wrapper) {
		try {
			Statistics statistics = new Statistics(kpiWiseValueList.get(wrapper.getKpiName()));
			statsImageMap.put(ReportConstants.STATS + ReportConstants.UNDERSCORE + wrapper.getIndexKPI(),
					getStatsImage(statistics, wrapper.getKpiName()));
			ImageIO.write(statsImageMap.get(ReportConstants.STATS + ReportConstants.UNDERSCORE + wrapper.getIndexKPI()),
					"jpg",
					new File(ConfigUtils.getString(ReportConstants.FINAL_IMAGE_PATH) + ReportConstants.FORWARD_SLASH
							+ ReportConstants.STATS + ReportConstants.UNDERSCORE + wrapper.getIndexKPI() + ".jpg"));
		} catch (Exception e) {
			logger.error("Exception in calculating Statistics for kpiName {} ", wrapper.getKpiName());
		}
	}

	private BufferedImage getStatsImage(Statistics statistics, String kpiName) {
		logger.info("Inside method getStatsImage for kpiname {} ", kpiName);
		BufferedImage image = new BufferedImage(ReportConstants.TWO_HUNDRED_TWENTY, ReportConstants.TWO_HUNDRED_TWENTY,
				BufferedImage.TYPE_INT_RGB);
		try {
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			LegendUtil.writeHeading(ReportConstants.STATS, ReportConstants.TEN, graphics);
			int j = ReportConstants.INDEX_THIRTY;
			int i = ReportConstants.INDEX_FIVE;
			j += ReportConstants.INDEX_FIVE;
			logger.info("statistics Data {} , for  kpiName {} ", statistics, kpiName);
			graphics.drawString(
					ReportConstants.MEAN + ReportConstants.DOUBLE_SPACE_COLON_DOUBLE_SPACE + statistics.getMean(), i,
					j);
			j += ReportConstants.INDEX_FOURTEEN;
			graphics.drawString(
					ReportConstants.MEDIAN + ReportConstants.DOUBLE_SPACE_COLON_DOUBLE_SPACE + statistics.getMedian(),
					i, j);
			j += ReportConstants.INDEX_FOURTEEN;
			graphics.drawString(
					ReportConstants.MAXIMUM + ReportConstants.DOUBLE_SPACE_COLON_DOUBLE_SPACE + statistics.getMax(), i,
					j);
			j += ReportConstants.INDEX_FOURTEEN;
			graphics.drawString(
					ReportConstants.MINIMUM + ReportConstants.DOUBLE_SPACE_COLON_DOUBLE_SPACE + statistics.getMin(), i,
					j);
			j += ReportConstants.INDEX_FOURTEEN;
			graphics.drawString(ReportConstants.STANDARD_DEVIATION + ReportConstants.DOUBLE_SPACE_COLON_DOUBLE_SPACE
					+ statistics.getStdDev(), i, j);
			j += ReportConstants.INDEX_FOURTEEN;
			graphics.drawString(ReportConstants.VARIANCE + ReportConstants.DOUBLE_SPACE_COLON_DOUBLE_SPACE
					+ ReportUtil.parseToFixedDecimalPlace(statistics.getVariance(), 2), i, j);
			j += ReportConstants.INDEX_FOURTEEN;
			graphics.drawString(
					ReportConstants.COUNT + ReportConstants.DOUBLE_SPACE_COLON_DOUBLE_SPACE + statistics.getSize(), i,
					j);
		} catch (Exception e) {
			logger.error("Error in writing getStatsImage {} ", Utils.getStackTrace(e));
		}
		return image;
	}

}
