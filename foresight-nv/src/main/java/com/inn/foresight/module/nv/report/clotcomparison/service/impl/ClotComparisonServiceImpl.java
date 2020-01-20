package com.inn.foresight.module.nv.report.clotcomparison.service.impl;

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

import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.report.clotcomparison.service.IClotComparisonService;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.AvrageKpiWrapper;
import com.inn.foresight.module.nv.report.wrapper.CustomerComplaintSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.CustomerComplaintWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPIImgDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
/**
 * The Class ClusterWorkOrderComparisonServiceImpl.
 */
@Service("ClotComparisonServiceImpl")
public class ClotComparisonServiceImpl implements IClotComparisonService{

	/** The logger. */
	private Logger logger = LogManager.getLogger(ClotComparisonServiceImpl.class);

	/** The map image service. */

	@Autowired
	private IMapImagesService mapImageService;
	
	/** The nv layer 3 dashboard service. */
	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	
	/** The report service. */
	@Autowired
	private IReportService reportService;

	
	/** The mapper. */
	ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Instantiates a new cluster work order comparison service impl.
	 */
	public ClotComparisonServiceImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.inn.foresight.module.nv.report.service.IClusterWorkOrderComparisonService#execute(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Response execute(String json){
		logger.info("Going to execute the method with json {} ",json);
		String filePath=ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH)+ReportConstants.CLOT_WO_COMPARISON+ReportConstants.FORWARD_SLASH;
		CustomerComplaintWrapper mainWrapper=new CustomerComplaintWrapper();
		try {
			Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
			logger.info("jsonMap {} ", jsonMap);
			List<Integer> previousWoIds = (ArrayList<Integer>) jsonMap.get(ReportConstants.PREV_WORKORDER_IDS);
			List<Integer> currentWoIds = (ArrayList<Integer>) jsonMap.get(ReportConstants.WORKORDER_IDS);
			Integer analyticsrepoId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			List<Integer> allWOIds = new ArrayList<>();
			allWOIds.addAll(previousWoIds);
			allWOIds.addAll(currentWoIds);
			boolean qmdlParsingStatus = reportService.getFileProcessedStatusForWorkorders(allWOIds);
			if (qmdlParsingStatus) {
				return generateClusterWOCompareReport(filePath, mainWrapper, jsonMap, previousWoIds, currentWoIds,
						analyticsrepoId);
			}
		} catch (Exception e1) {
			logger.error("Error inside the method createReportForWorkOrderID for json {} , {} ",json ,Utils.getStackTrace(e1));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	/**
	 * Generate cluster WO compare report.
	 *
	 * @param filePath the file path
	 * @param mainWrapper the main wrapper
	 * @param jsonMap the json map
	 * @param previousWoId the previous wo id
	 * @param currentWoId the current wo id
	 * @param analyticsrepoId the analyticsrepo id
	 * @return the response
	 */
	private Response generateClusterWOCompareReport(String filePath, CustomerComplaintWrapper mainWrapper,
			Map<String, Object> jsonMap, List<Integer> previousWoId, List<Integer> currentWoId, Integer analyticsrepoId) {
		try {
			CustomerComplaintSubWrapper wrapper = new CustomerComplaintSubWrapper();
			wrapper.setClusterName(jsonMap.get(ReportConstants.GEOGRAPHY_NAME).toString());
			wrapper.setDescription(jsonMap.get(ReportConstants.NAME).toString());
			wrapper = reportService.setSiteRelatedInformation(wrapper,jsonMap);
			List<List<List<Double>>> allCustomRoutes = reportService.getAllCustomRoutesOfPrePost(previousWoId,currentWoId);
			List<List<List<List<Double>>>> boundaryData = getBoundaryData(mapper, wrapper.getClusterName(), jsonMap.get(ReportConstants.GEOGRAPHY_TYPE).toString());
			logger.info("boundaryData Size {} ",boundaryData.size());
			mainWrapper.setFileName(ReportUtil.getFileName(jsonMap.get(ReportConstants.NAME)!=null?jsonMap.get(ReportConstants.NAME).toString():ReportConstants.BLANK_STRING, analyticsrepoId,filePath).replace(ReportConstants.PDF_EXTENSION, ReportConstants.PPTX_EXTENSION));
			mainWrapper.setSubList(populateImageDataInWrapper(wrapper, Arrays.asList(previousWoId, currentWoId),boundaryData,allCustomRoutes));
			File file = proceedToClusterWOCompareReport(new HashMap<String, Object>(), mainWrapper);
			logger.info("Wrapper On Air Site {} , Cell  Count {} ",wrapper.getOnAirSitePercentage(),wrapper.getOnAirCellPercentage());
			String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.MASTER + ReportConstants.FORWARD_SLASH;
			return reportService.saveFile((Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY), hdfsFilePath, file);
		} catch (Exception e) {
			logger.error("Exception inside the method generateClusterWOCompareReport {}", Utils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}

	

	/**
	 * Proceed to cluster WO compare report.
	 *
	 * @param imageMap the image map
	 * @param mainWrapper the main wrapper
	 * @return the file
	 */
	private File proceedToClusterWOCompareReport(Map<String, Object> imageMap, CustomerComplaintWrapper mainWrapper) {
		try {
			String reportAssetRepo = ConfigUtils.getString(ReportConstants.CLUSTER_WO_COMPARISON_REPORT_JASPER_FOLDER_PATH);
			List<CustomerComplaintWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			setParameterForReport(imageMap, mainWrapper, reportAssetRepo);
			String destinationFileName = mainWrapper.getFileName();
			String tempFileNameforPPT = JasperFillManager.fillReportToFile(reportAssetRepo + ReportConstants.MAIN_JASPER,imageMap, rfbeanColDataSource);
			getPPTXFromReport(tempFileNameforPPT,destinationFileName);
			logger.info("Report Created successfully  ");
			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@proceedToCreateComplaintReport getting err={}", Utils.getStackTrace(e));
		}
		logger.info(
				"@proceedToCreateComplaintReport going to return null as there has been some problem in generating report");
		return null;
	}

	private void setParameterForReport(Map<String, Object> imageMap, CustomerComplaintWrapper mainWrapper,
			String reportAssetRepo) {
		imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
		imageMap.put(ReportConstants.IMAGE_PARAM_HEADER_BG, reportAssetRepo + ReportConstants.IMAGE_HEADER_BG);
		imageMap.put(ReportConstants.IMAGE_PARAM_HEADER_LOG, reportAssetRepo + ReportConstants.IMAGE_HEADER_LOG);
		imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_LOG, reportAssetRepo + ReportConstants.IMAGE_SCREEN_LOGO);
		imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_BG, reportAssetRepo + ReportConstants.IMAGE_SCREEN_BG);
		
		if(mainWrapper!=null&&mainWrapper.getSubList()!=null&&mainWrapper.getSubList().get(0).getImgList()!=null) {
			for (KPIImgDataWrapper wrapper : mainWrapper.getSubList().get(0).getImgList()) {
				if(wrapper.getKpiName().equalsIgnoreCase(ReportConstants.ROUTE)) {
					imageMap.put(ReportConstants.IMAGE_ROUTE, wrapper.getBeforImg());
				}
				
			}
			if(mainWrapper.getSubList().get(0).getNoOfSites()!=null&&mainWrapper.getSubList().get(0).getNoOfSites()!=ReportConstants.INDEX_ZER0) {
				imageMap.put("showSitesPage", ReportConstants.TRUE);
			} else {
				imageMap.put("showSitesPage", ReportConstants.FALSE);
			}
		}
	}
	
	/**
	 * Populate image data in wrapper.
	 *
	 * @param wrapper the wrapper
	 * @param list the list
	 * @param boundaryData the boundary data
	 * @param allCustomRoutes 
	 * @return the list
	 */
	private List<CustomerComplaintSubWrapper> populateImageDataInWrapper(CustomerComplaintSubWrapper wrapper, List<List<Integer>> list, List<List<List<List<Double>>>> boundaryData, List<List<List<Double>>> allCustomRoutes) {
		logger.info("Inside getImageMapforWorkOrderIds : "+list.toString());
		List<CustomerComplaintSubWrapper>subList=new ArrayList<>();
		List<KPIImgDataWrapper> lsitofKpiImg = new ArrayList<>();

		Boolean isPre = getIsPreDefaultValue(list);
		for(List<Integer> workorderId : list) {
			logger.info("workorderId ===={}",workorderId);
			 getKPiImageDataListForWorkOrderID(wrapper.getCellWiseSiteList(),workorderId,lsitofKpiImg,isPre,boundaryData,allCustomRoutes);
			getKpiSummaryDataListForWorkOrderID(workorderId,isPre,wrapper);
			if(isPre!=null){
				isPre=true;
			}
		}
		wrapper.setImgList(lsitofKpiImg);
		subList.add(wrapper);
		return subList;
	}

	/**
	 * Gets the kpi summary data list for work order ID.
	 *
	 * @param workorderId the workorder id
	 * @param isPre the is pre
	 * @param wrapper the wrapper
	 * @return the kpi summary data list for work order ID
	 */
	private CustomerComplaintSubWrapper getKpiSummaryDataListForWorkOrderID(List<Integer> workorderId, Boolean isPre,
			CustomerComplaintSubWrapper wrapper) {
		Map<String, List<String>> map = nvLayer3DashboardService.getDriveRecipeDetail(workorderId);
		try {
			logger.info("Drive recipe Map {} ", new Gson().toJson(map));
			if (map != null) {
				for(Integer workorderid : workorderId) {
					List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
					Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper
							.getLiveDriveKPIIndexMap(fetchSummaryKPIList);
				
					String[] summaryData = reportService.getSummaryDataForReport(workorderid, map.get(QMDLConstant.RECIPE),
							fetchSummaryKPIList);
				if (isPre) {
					wrapper.setPreList(setSummaryData(summaryData,summaryKpiIndexMap));
				} else {
					wrapper.setPostList(setSummaryData(summaryData,summaryKpiIndexMap));
				}
			}}
		} catch (Exception e) {
			logger.warn("exception to set summary data {}", e.getMessage());
		}
		return wrapper;
	}

	/**
	 * Sets the summary data.
	 *
	 * @param summaryData the summary data
	 * @param summaryKpiIndexMap 
	 * @return the list
	 */
	private List<AvrageKpiWrapper> setSummaryData(String[] summaryData, Map<String, Integer> summaryKpiIndexMap) {
		AvrageKpiWrapper wrapper=new AvrageKpiWrapper();
		List<AvrageKpiWrapper>list=new ArrayList<>();
		try{
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
			list.add(wrapper);
		} catch (Exception e) {
			logger.warn("Exception inside the method setSummaryData{}", Utils.getStackTrace(e));
		}
		return list;
	}



	/**
	 * Gets the checks if is pre default value.
	 *
	 * @param list the list
	 * @return the checks if is pre default value
	 */
	private Boolean getIsPreDefaultValue(List<List<Integer>> list) {
		if(list!=null && list.size()==ReportConstants.INDEX_TWO){
			return false;
		}else{
			return null;
		}
	}

	/**
	 * Gets the k pi image data list for work order ID.
	 * @param listOfSites 
	 *
	 * @param workorderIds the workorder ids
	 * @param listofKpiImg the listof kpi img
	 * @param isPre the is pre
	 * @param boundaryData the boundary data
	 * @param allCustomRoutes 
	 * @return the k pi image data list for work order ID
	 */
	private List<KPIImgDataWrapper> getKPiImageDataListForWorkOrderID(List<SiteInformationWrapper> listOfSites, List<Integer> workorderIds, List<KPIImgDataWrapper> listofKpiImg, Boolean isPre, List<List<List<List<Double>>>> boundaryData, List<List<List<Double>>> allCustomRoutes) {
		logger.info("Inside method getKPiImageDataListForWorkOrderID for workOrder Id {} ,isPre {} ",workorderIds,isPre);
		try {
			Map<String, List<String>> map = nvLayer3DashboardService.getDriveRecipeDetail(workorderIds);
			if (map != null) {
				for (Integer workorderid : workorderIds) {
					logger.debug("Drive recipe Map {} ", new Gson().toJson(map));

					// for Drive Data
					Set<String> dynamicKpis = reportService.getDynamicKpiName(Arrays.asList(workorderid), null,
							Layer3PPEConstant.ADVANCE);
					List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs().stream()
							.filter(k -> dynamicKpis.contains(k)).collect(Collectors.toList());
					Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);

					List<KPIWrapper> kpiList = reportService.getKPiStatsDataList(workorderid, map, kpiIndexMap,
							ReportConstants.PREPOST);
					DriveImageWrapper driveImageWrapper = getDriveImageWrapperDataForImageGenearation(workorderid, map,
							kpiList, boundaryData, allCustomRoutes, listOfSites, fetchKPIList, kpiIndexMap);

					// for summary data
					List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
					Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper
							.getLiveDriveKPIIndexMap(fetchSummaryKPIList);

					String[] summaryData = reportService.getSummaryDataForReport(workorderid,
							map.get(QMDLConstant.RECIPE), fetchSummaryKPIList);
					 getListofImages(driveImageWrapper, kpiList, listofKpiImg, workorderid, isPre, summaryData,
							summaryKpiIndexMap, kpiIndexMap);
				}}
		} catch (Exception e) {
			logger.error("Excption in getKPiImageDataListForWorkOrderID : {} ", Utils.getStackTrace(e));
		}
		return listofKpiImg;
	}

	/**
	 * Gets the listof images.
	 *
	 * @param driveImageWrapper the drive image wrapper
	 * @param kpiList the kpi list
	 * @param listofKpiImg the listof kpi img
	 * @param workorderid the workorder ids
	 * @param isPre the is pre
	 * @param summaryData 
	 * @param kpiIndexMap 
	 * @param summaryKpiIndexMap 
	 * @return the listof images
	 */
	private List<KPIImgDataWrapper> getListofImages(DriveImageWrapper driveImageWrapper, List<KPIWrapper> kpiList, List<KPIImgDataWrapper> listofKpiImg, Integer workorderid, Boolean isPre, String[] summaryData, Map<String, Integer> summaryKpiIndexMap, Map<String, Integer> kpiIndexMap) {
		logger.info("Inside method getListofImages for workOrderIds {} ",workorderid);
		try {
			HashMap<String,BufferedImage> driveImageMap = mapImageService.getDriveImagesForReport(driveImageWrapper, null,kpiIndexMap);
			HashMap<String,BufferedImage> legendImageMap = mapImageService.getLegendImages(kpiList,driveImageWrapper.getDataKPIs());
			Long time =new Date().getTime();
			logger.info("time12345 {} ",time);
			kpiList.stream().forEach(kpiWrapperObj->{
				try {
					if ((kpiIndexMap.get(ReportConstants.PCI_PLOT)!=null && kpiWrapperObj.getIndexKPI().toString()
							.equalsIgnoreCase((kpiIndexMap.get(ReportConstants.PCI_PLOT)).toString()))
							||(kpiIndexMap.get(ReportConstants.DL_EARFCN)!=null && kpiWrapperObj.getIndexKPI().toString()
									.equalsIgnoreCase((kpiIndexMap.get(ReportConstants.DL_EARFCN)).toString()))) {
						setCustomizedData(isPre, kpiWrapperObj, listofKpiImg, driveImageMap);
					} else {
						setDataInWrapper(isPre, kpiWrapperObj, listofKpiImg, driveImageMap, legendImageMap,
								summaryData,summaryKpiIndexMap);
					}
				} catch (Exception e) {
					logger.error("Exception inside method getListofImages {} ",Utils.getStackTrace(e));
				}
			});
			return listofKpiImg;
		}catch(Exception e){
			logger.error("Exception inside method getListofImages {} ",Utils.getStackTrace(e));
		}
		return listofKpiImg;
	}



	/**
	 * Sets the customized data.
	 *
	 * @param isPre the is pre
	 * @param kpiWrapperObj the kpi wrapper obj
	 * @param listofKpiImg the listof kpi img
	 * @param driveImageMap the drive image map
	 */
	private void setCustomizedData(Boolean isPre, KPIWrapper kpiWrapperObj, List<KPIImgDataWrapper> listofKpiImg, Map<String, BufferedImage> driveImageMap) {
		logger.info("Inside method setCustomizedData  is Pre {} , kpiwrapperObj {} ",isPre,kpiWrapperObj);
		
		try {
			if(!isPre){
				KPIImgDataWrapper kpiImgWrapper = new KPIImgDataWrapper();
				kpiImgWrapper.setKpiName(kpiWrapperObj.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE));
				kpiImgWrapper.setBeforImg(getInputStreamFromBufferedImage(driveImageMap.get(kpiWrapperObj.getIndexKPI()+ReportConstants.BLANK_STRING)));
				if(kpiWrapperObj.getKpiName().equalsIgnoreCase(ReportConstants.PCI_PLOT)){
					kpiImgWrapper.setBeforLegendImg(getInputStreamFromBufferedImage(driveImageMap.get(ReportConstants.KEY_LEGENDS)));
				}else{
					kpiImgWrapper.setBeforLegendImg(getInputStreamFromBufferedImage(driveImageMap.get(kpiWrapperObj.getKpiName())));
				}
				kpiImgWrapper.setAvgBeforeKpi(kpiWrapperObj.getAverageValue());
				listofKpiImg.add(kpiImgWrapper);
			}else{
				List<KPIImgDataWrapper> kpiImgWrapperList = listofKpiImg.stream().filter(obj->obj.getKpiName().equalsIgnoreCase(kpiWrapperObj.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE))).collect(Collectors.toList());
				logger.info("kpiImgWrapperList {}",kpiImgWrapperList);
				if(kpiImgWrapperList!=null && !kpiImgWrapperList.isEmpty()){
					KPIImgDataWrapper kpiImgWrapper = kpiImgWrapperList.get(ReportConstants.INDEX_ZER0);
					kpiImgWrapper.setAfterImg(getInputStreamFromBufferedImage(driveImageMap.get(kpiWrapperObj.getIndexKPI()+ReportConstants.BLANK_STRING)));
					if(kpiWrapperObj.getKpiName().equalsIgnoreCase(ReportConstants.PCI_PLOT)){
						kpiImgWrapper.setAfterLegendImg(getInputStreamFromBufferedImage(driveImageMap.get(ReportConstants.KEY_LEGENDS)));
					}else{
						kpiImgWrapper.setAfterLegendImg(getInputStreamFromBufferedImage(driveImageMap.get(kpiWrapperObj.getKpiName())));
					}
					kpiImgWrapper.setAvgAfterKpi(kpiWrapperObj.getAverageValue());
				}
			}
		} catch (Exception e) {
			logger.info("Exception inside method setDataInWrapper {} ",e.getMessage());
		}
	}

	/**
	 * Sets the data in wrapper.
	 *
	 * @param isPre the is pre
	 * @param kpiWrapperObj the kpi wrapper obj
	 * @param listofKpiImg the listof kpi img
	 * @param driveImageMap the drive image map
	 * @param legendImageMap the legend image map
	 * @param summaryData 
	 * @param summaryKpiIndexMap 
	 */
	private void setDataInWrapper(Boolean isPre, KPIWrapper kpiWrapperObj, List<KPIImgDataWrapper> listofKpiImg, Map<String, BufferedImage> driveImageMap, Map<String, BufferedImage> legendImageMap, String[] summaryData, Map<String, Integer> summaryKpiIndexMap) {
		logger.info("Inside method setDataInWrapper is Pre {} , kpiwrapperObj {} ",isPre,kpiWrapperObj);
		try {
			if(!isPre){
				logger.info("inside the if to set image of kpiName {}",kpiWrapperObj.getKpiName());
				InputStream is = getInputStreamFromBufferedImage(driveImageMap.get(kpiWrapperObj.getIndexKPI()+ReportConstants.BLANK_STRING));
				if(is!=null){
					KPIImgDataWrapper kpiImgWrapper = new KPIImgDataWrapper();
					kpiImgWrapper.setKpiUnit(ReportUtil.getUnitByKPiName(kpiWrapperObj.getKpiName()));
					kpiImgWrapper.setKpiName(kpiWrapperObj.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE));
					kpiImgWrapper.setBeforImg(is);
					kpiImgWrapper.setBeforLegendImg(getInputStreamFromBufferedImage(legendImageMap.get(ReportConstants.LEGEND+ReportConstants.UNDERSCORE+kpiWrapperObj.getIndexKPI())));
					kpiImgWrapper.setAvgBeforeKpi(getAverageValueFromSummaryData(summaryData,getAvgValueIndexofSummary(kpiWrapperObj.getKpiName(),summaryKpiIndexMap)));
					if(kpiWrapperObj.getKpiName().equalsIgnoreCase(ReportConstants.ROUTE)){
						logger.info("Setting Route info");
						kpiImgWrapper.setBeforImg(getInputStreamFromBufferedImage(driveImageMap.get("TERRAIN0")));
					}
					listofKpiImg.add(kpiImgWrapper);
				}else{
					logger.info("Input Stream is null for isPre {} , kpi Name {} ",isPre,kpiWrapperObj.getKpiName());
				}
			}else{
				List<KPIImgDataWrapper> kpiImgWrapperList = listofKpiImg.stream().filter(obj->obj.getKpiName().equalsIgnoreCase(kpiWrapperObj.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE))).collect(Collectors.toList());
				logger.info("kpiImgWrapperList {}",kpiImgWrapperList);
				if(kpiImgWrapperList!=null && !kpiImgWrapperList.isEmpty()){
					logger.info("inside the else to set image for kpi {}",kpiWrapperObj.getKpiName());
					KPIImgDataWrapper kpiImgWrapper = kpiImgWrapperList.get(ReportConstants.INDEX_ZER0);
					kpiImgWrapper.setAfterImg(getInputStreamFromBufferedImage(driveImageMap.get(kpiWrapperObj.getIndexKPI()+ReportConstants.BLANK_STRING)));
					kpiImgWrapper.setAfterLegendImg(getInputStreamFromBufferedImage(legendImageMap.get(ReportConstants.LEGEND+ReportConstants.UNDERSCORE+kpiWrapperObj.getIndexKPI())));
					kpiImgWrapper.setAvgAfterKpi(getAverageValueFromSummaryData(summaryData,getAvgValueIndexofSummary(kpiImgWrapper.getKpiName().replaceAll(ReportConstants.SPACE,ReportConstants.UNDERSCORE), summaryKpiIndexMap)));
					if(kpiWrapperObj.getKpiName().equalsIgnoreCase(ReportConstants.ROUTE)){
						logger.info("Setting Route info");
						kpiImgWrapper.setBeforImg(getInputStreamFromBufferedImage(driveImageMap.get("TERRAIN0")));
					}
				}
			}
		} catch (Exception e) {
			logger.info("Exception inside method setDataInWrapper {} ",e.getMessage());
		}
	}

	private Integer getAvgValueIndexofSummary(String kpiName, Map<String, Integer> summaryKpiIndexMap) {
		switch (kpiName) {
		case ReportConstants.RSRP:
		case ReportConstants.FTP_DL_RSRP:
		case ReportConstants.FTP_UL_RSRP:
		case ReportConstants.HTTP_DL_RSRP:
		case ReportConstants.HTTP_UL_RSRP:
			return summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.RSRP);
		case ReportConstants.SINR:
		case ReportConstants.FTP_DL_SINR:
		case ReportConstants.FTP_UL_SINR:
		case ReportConstants.HTTP_DL_SINR:
		case ReportConstants.HTTP_UL_SINR:
			return summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.SINR);
		case ReportConstants.UL:
		case ReportConstants.HTTP_UL_THROUGHPUT:
		case ReportConstants.FTP_UL_THROUGHPUT:
			return summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.UL_THROUGHPUT);
		case ReportConstants.DL:
		case ReportConstants.HTTP_DL_THROUGHPUT:
		case ReportConstants.FTP_DL_THROUGHPUT:
			return summaryKpiIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.DL_THROUGHPUT);
		default:
			return null;
		}
	}

	private Double getAverageValueFromSummaryData(String[] summary, Integer indexKPI) {
		try {
		//	String[] summary = summaryData.replaceAll("\\]", ReportConstants.BLANK_STRING).replaceAll("\\[", ReportConstants.BLANK_STRING).split(ReportConstants.COMMA);
			if(indexKPI!=null && summary[indexKPI] != null && !summary[indexKPI].isEmpty()){
				Double avgValue = ReportUtil.parseToFixedDecimalPlace(Double.parseDouble(summary[indexKPI]),ReportConstants.INDEX_THREE);
				logger.info("avgValue12345 {} ",avgValue);
				return avgValue;
			}
		} catch (Exception e) {
			logger.error("Unable to find the average value of kpi with summary data {} ,indexKPI {} ",summary,indexKPI);
		}
		return null;
	}

	/**
	 * Gets the input stream from buffered image.
	 *
	 * @param bufferedImage the buffered image
	 * @return the input stream from buffered image
	 */
	private InputStream getInputStreamFromBufferedImage(BufferedImage bufferedImage) {
		if(bufferedImage!=null){
			try {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, ReportConstants.JPG, os);
				return  new ByteArrayInputStream(os.toByteArray());
			} catch (Exception e) {
				logger.info("Unable to convert BUfferedImage to inputStream {} ",e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Gets the drive image wrapper data for image genearation.
	 *
	 * @param workorderid the workorder id
	 * @param map the map
	 * @param kpiList the kpi list
	 * @param boundaryData the boundary data
	 * @param listOfSites 
	 * @param kpiIndexMap 
	 * @param fetchKPIList 
	 * @param allCustomRoutes 
	 * @return the drive image wrapper data for image genearation
	 */
	private DriveImageWrapper getDriveImageWrapperDataForImageGenearation(Integer workorderid, Map<String, List<String>> map, List<KPIWrapper> kpiList, List<List<List<List<Double>>>> boundaryData, List<List<List<Double>>> driveRoute, List<SiteInformationWrapper> listOfSites, List<String> fetchKPIList, Map<String, Integer> kpiIndexMap) {
		logger.info("Inside method getDriveImageWrapperDataForImageGenearation for workOrder Id {} ",workorderid);
		DriveImageWrapper driveImageWrapper =null;
		try {
			
			List<String[]> arlist = reportService.getDriveDataForReport(workorderid, map.get(QMDLConstant.RECIPE), fetchKPIList);
			List<SiteInformationWrapper> siteDataList = listOfSites!=null?listOfSites:new ArrayList<>();
			logger.info("siteDataList Size is {} ",siteDataList.size());
			return new DriveImageWrapper(arlist, kpiIndexMap.get(ReportConstants.LATITUDE),
					kpiIndexMap.get(ReportConstants.LONGITUDE),kpiIndexMap.get(ReportConstants.PCI_PLOT), kpiList, siteDataList, boundaryData,driveRoute);
		} catch (Exception e) {
			logger.error("Exception inside method getDriveImageWrapperDataForImageGenearation {} ",Utils.getStackTrace(e));
		}
		return driveImageWrapper;
	}
	
	

	/**
	 * Gets the drive data.
	 *
	 * @param workorderId the workorder id
	 * @param map the map
	 * @return the drive data
	 */
	public String getDriveData(List<Integer> workorderId, Map<String, List<String>> map){
		String combineData=null;
		try {
			combineData =reportService.getDriveData(workorderId,
					map.get(QMDLConstant.RECIPE), map.get(QMDLConstant.OPERATOR));
		} catch (BusinessException e) {
			logger.error("Exception in method getDriveData {} ",e.getMessage());
		}
		logger.info("combineData is {}",combineData);
		return combineData;
	}

	/**
	 * Sets the kpi states intokpi list.
	 *
	 * @param kpiList            the kpi list
	 * @param workOrderId            the work order id
	 * @param recepiList the recepi list
	 * @param operatorList the operator list
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
	 * @param workOrderId            the work order id
	 * @param kpiname            the kpiname
	 * @param recepiList the recepi list
	 * @param operatorList the operator list
	 * @return the k pi stats data from hbase
	 */
	private String[] getKPiStatsDataFromHbase(Integer workOrderId, String kpiname, List<String> recepiList,
			List<String> operatorList) {
		String[] kpistats = null;
		try {
			String kpistat = nvLayer3DashboardService.getKpiStatsRecipeDataForReport(workOrderId, kpiname, recepiList,
					operatorList);
			logger.info(" kpiname {} ,kpistat {}",kpiname ,kpistat);
			Map<String, String[]> map = mapper.readValue(kpistat, new TypeReference<Map<String, String[]>>() {
			});
			kpistats = map.get(ReportConstants.RESULT);
			logger.info("kpistats {} ", kpistats != null ? Arrays.toString(kpistats):null);
			return kpistats;
		} catch (IOException | BusinessException e) {
			logger.error("Exception inside method getKPiStatsDataFromHbase {} ", Utils.getStackTrace(e));
		}
		return kpistats;
	}
	
	
	/**
	 * Gets the boundary data.
	 *
	 * @param mapper the mapper
	 * @param name the name
	 * @param geographyType the geography type
	 * @return the boundary data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private List<List<List<List<Double>>>> getBoundaryData(ObjectMapper mapper, String name, String geographyType)
			throws IOException {
		String boundaryData = reportService.getGeographyBoundaryByLevelAndName(name,geographyType);
		logger.debug("boundaryData for geographyType {} , name {} is {} ",name,geographyType,boundaryData);
		List<List<List<Double>>> boundarie = null;
		List<List<List<List<Double>>>> boundaries = new ArrayList<>();
		if (boundaryData != null) {
			boundarie = mapper.readValue(boundaryData, new TypeReference<List<List<List<Double>>>>() {
			});
			boundaries.add(boundarie);
		}
		return boundaries;
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

}
