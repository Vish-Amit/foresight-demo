package com.inn.foresight.module.nv.report.customreport.ssvt.service.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.core.report.utils.ReportUtils;
import com.inn.foresight.module.nv.core.workorder.dao.IGWOMetaDao;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GWOMeta;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.feedback.dao.IConsumerFeedbackDao;
import com.inn.foresight.module.nv.feedback.model.ConsumerFeedback;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HbaseDao;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3UpdationService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.RangeSlab;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.customreport.reports.constants.NVReportConstants;
import com.inn.foresight.module.nv.report.customreport.ssvt.constants.SSVTConstants;
import com.inn.foresight.module.nv.report.customreport.ssvt.service.ISSVTService;
import com.inn.foresight.module.nv.report.customreport.ssvt.util.SSVTReportUtils;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.CallAnalysisDataWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.CallAnalysisItemWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.HOItemWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.HandoverDataWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.KpiDataWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.KpiGraphWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.KpiMapWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.SSVTReportWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.SectorWiseWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.SiteInfoWrapper;
import com.inn.foresight.module.nv.report.dao.INVReportHbaseDao;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.SectorSwapWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.service.INVWorkorderService;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("SSVTServiceImpl")
public class SSVTServiceImpl extends SSVTConstants implements ISSVTService {

    /**
     * The logger.
     */
    private Logger logger = LogManager.getLogger(SSVTServiceImpl.class);

    @Autowired
    private IGenericWorkorderDao iGenericWorkorderDao;
    @Autowired
    private IAnalyticsRepositoryDao analyticsrepositoryDao;

    @Autowired
    private INVLayer3DashboardService nvLayer3DashboardService;

    @Autowired
    private IReportService reportService;

    @Autowired
    private IMapImagesService mapImageService;

    @Autowired
    private INVReportHbaseDao nVReportHbaseDao;

    @Autowired
    private ILegendRangeDao legendRangeDao;

    @Autowired
    private INVLayer3HbaseDao nvLayer3HbaseDao;

    @Autowired
    private IWORecipeMappingDao woRecipeMappingDao;

    @Autowired
    private INVL3UpdationService nvl3UpdateService;

    @Autowired
    private IConsumerFeedbackDao consumerFeedbackDao;

    @Autowired
    private IGWOMetaDao igwoMetaDao;
    
    @Autowired
    private INVWorkorderService workorderService;

    public SSVTServiceImpl() {
        super();
    }

    @Override
    @Transactional
    public Response execute(String json) {
        logger.info("Inside execute method to create SSVT Report with {} ", json);
        Integer analyticsrepoId = null;
        try {
            Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
            Integer workorderId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
            analyticsrepoId = (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID);
            //String username = SSVTReportUtils.findUserName(jsonMap);
            boolean qmdlStatus = reportService.getFileProcessedStatusForWorkorders(Arrays.asList(workorderId));
            if (qmdlStatus) {
                return generateReport(jsonMap, workorderId, analyticsrepoId);
            }
        } catch (IOException | ForbiddenException e) {
            logger.error("Error Inside createReportForWorkOrderID for json {} , {} ", json, Utils.getStackTrace(e));
            analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
                    progress.Failed, null);
        } catch (Exception e1) {
            logger.error("Error inside the method createReportForWorkOrderID for json {} , {} ", json,
                    Utils.getStackTrace(e1));
            analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
                    progress.Failed, null);
        }
        return Response.ok(ForesightConstants.FAILURE_JSON).build();
    }

    private Response generateReport(Map<String, Object> jsonDataMap, Integer workorderId, Integer analyticsrepoId)
            throws IOException {
        logger.info("Inside method generate Report with workorderId {} ", workorderId);
        try {
            GenericWorkorder workorderObj = iGenericWorkorderDao.findByPk(workorderId);
            jsonDataMap.put(ReportConstants.TEST_TYPE, workorderObj.getTemplateType().name());
            boolean iSiteAcceptance = false;
            if (SSVTReportUtils.validateMap(workorderObj.getGwoMeta(), SSVTConstants.IS_SITE_ACCEPTANCE)) {
                iSiteAcceptance = Boolean.parseBoolean(workorderObj.getGwoMeta().get(SSVTConstants.IS_SITE_ACCEPTANCE));
            }
            Map<String, List<String>> driveRecipeDetailMap = nvLayer3DashboardService
                    .getDriveRecipeDetail(workorderObj.getId());
            logger.info("drive detail map is: {}", new Gson().toJson(driveRecipeDetailMap));
            Map<String, List<WORecipeMapping>> driveWiseRecipeMap = SSVTReportUtils
                    .getDriveTypeWiseRecipeMapping(woRecipeMappingDao.getWORecipeByGWOId(workorderId));
            if (SSVTReportUtils.validateMap(driveRecipeDetailMap, QMDLConstant.RECIPE)) {
                Map<String, String> mobilityTestTypeMap = getMobilityTestTypeMap(driveWiseRecipeMap);
                Set<String> dynamicKpis = reportService.getDynamicKpiName(Arrays.asList(workorderId), null, Layer3PPEConstant.ADVANCE);
                List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs().stream().filter(k -> dynamicKpis.contains(k)).collect(Collectors.toList());
                Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
                fetchKPIList.add(ReportConstants.NEIGHBOUR_DATA);
                kpiIndexMap.put(ReportConstants.NEIGHBOUR_DATA, fetchKPIList.size() - NumberUtils.INTEGER_ONE);
                logger.info("kpiIndexMap {}", kpiIndexMap);
                List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
                Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper
                        .getLiveDriveKPIIndexMap(fetchSummaryKPIList);
                Map<String, List<String[]>> csvDataMap = processData(workorderObj,
                        driveRecipeDetailMap.get(QMDLConstant.OPERATOR), driveWiseRecipeMap, mobilityTestTypeMap,
                        fetchKPIList, kpiIndexMap);
//				csvDataMap=SSVTReportUtils.tagRecordsWithTestType(csvDataMap,kpiIndexMap);
                String[] summaryData = getSummaryData(workorderObj.getId(), driveRecipeDetailMap, fetchSummaryKPIList);
                return generateSSVTReport(jsonDataMap, workorderObj, driveRecipeDetailMap, csvDataMap, summaryData,
                        iSiteAcceptance, mobilityTestTypeMap, kpiIndexMap, summaryKpiIndexMap);
            } else {
                analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Data is Not Available",
                        progress.Failed, null);
            }
        } catch (Exception e) {
            logger.error("Exception In generateReport {} ", Utils.getStackTrace(e));
        }
        return Response.ok(ForesightConstants.FAILURE_JSON).build();
    }

    private Map<String, String> getMobilityTestTypeMap(Map<String, List<WORecipeMapping>> driveWiseRecipeMap) {
        Map<String, String> mobilityTestTypeMap = new HashMap<>();
        if (SSVTReportUtils.validateMap(driveWiseRecipeMap, SSVTConstants.DRIVE_DATA_TYPE_DRIVE)) {
            List<WORecipeMapping> mobilityRecipeList = driveWiseRecipeMap.get(SSVTConstants.DRIVE_DATA_TYPE_DRIVE);
            for (WORecipeMapping woRecipeMapping : mobilityRecipeList) {
                String recipeId = String.valueOf(woRecipeMapping.getId());
                String scriptJson = woRecipeMapping.getRecipe().getScriptJson();
                List<Map<String, Object>> scriptMapList = new Gson().fromJson(scriptJson, ArrayList.class);
                if (Utils.isValidList(scriptMapList)) {
                    for (Map<String, Object> scriptMap : scriptMapList) {
                        String scriptName = (String) scriptMap.get(SSVTConstants.KEY_RECIPE_SCRIPT_NAME);
                        if ((SSVTConstants.RECIPE_SCRIPT_NAME_DOWNLOAD.equalsIgnoreCase(scriptName)
                                || SSVTConstants.RECIPE_SCRIPT_NAME_UPLOAD.equalsIgnoreCase(scriptName))
                                && !mobilityTestTypeMap.containsKey(scriptName)) {
                            mobilityTestTypeMap.put(scriptName, recipeId);
                        } else {
                            mobilityTestTypeMap.put(SSVTConstants.KEY_RECIPE_OTHERS, recipeId);
                        }
                    }
                }
            }
        }
        logger.info("Going to return mobility recipe map with data: {}", new Gson().toJson(mobilityTestTypeMap));
        return mobilityTestTypeMap;
    }

    @SuppressWarnings("unchecked")
    private Response generateSSVTReport(Map<String, Object> jsonDataMap, GenericWorkorder workorderObj,
                                        Map<String, List<String>> driveRecipeDetailMap, Map<String, List<String[]>> csvDataMap,
                                        String[] summaryData, boolean isSiteAcceptance, Map<String, String> mobilityTestTypeMap,
                                        Map<String, Integer> kpiIndexMap, Map<String, Integer> summaryKpiIndexMap) throws IOException {
        SSVTReportWrapper mainWrapper = new SSVTReportWrapper();
        try {
            Map<String, Long> driveTimeStampMap = ReportUtil.getDriveTimeStampMapForReports(
                    csvDataMap.get(SSVTConstants.DRIVE_DATA_TYPE_COMBINED), kpiIndexMap);
            List<SiteInformationWrapper> siteData = getSiteData(workorderObj, driveTimeStampMap,
                    csvDataMap.get(SSVTConstants.DRIVE_DATA_TYPE_COMBINED), mainWrapper, kpiIndexMap);
            logger.info("Final Site Data SIze {}", siteData.size());
            return updateMainWrapper(mainWrapper, siteData, workorderObj, summaryData, jsonDataMap, csvDataMap,
                    driveRecipeDetailMap, isSiteAcceptance, mobilityTestTypeMap, kpiIndexMap, summaryKpiIndexMap);
        } catch (Exception e) {
            logger.error("Error While Processing SSVT Report {} ", Utils.getStackTrace(e));
        }
        return Response.ok(ForesightConstants.FAILURE_JSON).build();
    }

    @Override
    public String[] getSummaryData(Integer workorderId, Map<String, List<String>> map,
                                   List<String> fetchSummaryKPIList) {
        try {
            logger.info("WORKORDER_ID {} , RECIPE LIST {} , DYNAMIC KPI LIST {} ", workorderId,
                    map.get(QMDLConstant.RECIPE), fetchSummaryKPIList);
            return reportService.getSummaryDataForReport(workorderId, map.get(QMDLConstant.RECIPE),
                    fetchSummaryKPIList);
        } catch (Exception e) {
            logger.error("Error inside the method getSummaryData {}", Utils.getStackTrace(e));
        }
        return null;
    }

    private Response updateMainWrapper(SSVTReportWrapper mainWrapper, List<SiteInformationWrapper> siteInfoList,
                                       GenericWorkorder genricWorkOrder, String[] summary, Map<String, Object> jsonDataMap,
                                       Map<String, List<String[]>> csvDataMap, Map<String, List<String>> driveRecipeDetailMap,
                                       boolean isSiteAcceptance, Map<String, String> mobilityTestTypeMap, Map<String, Integer> kpiIndexMap,
                                       Map<String, Integer> summaryKpiIndexMap) throws IOException {

        updateSiteDetail(mainWrapper, siteInfoList, genricWorkOrder, jsonDataMap, summary, summaryKpiIndexMap);
        String band = findBandDetailByGWOMetaData(genricWorkOrder);
        List<SiteInformationWrapper> filteredSiteInfo = filterNEDataByBand(siteInfoList, band);
        logger.info("Found the Final Response {}", filteredSiteInfo.toString());

        String siteName = null;
        if (!filteredSiteInfo.isEmpty()) {
            SiteInfoWrapper siteInfo = new SiteInfoWrapper();
            updateBasicSiteInfo(siteInfo, filteredSiteInfo);
            siteName = siteInfo.getSiteName();
            prepareSectorWiseData(siteInfo, filteredSiteInfo, csvDataMap, isSiteAcceptance, kpiIndexMap, genricWorkOrder);
            logger.info("Site Info wrapper is: {}", new Gson().toJson(siteInfo));
            if (!isSiteAcceptance) {
                mainWrapper.setSiteInfo(Arrays.asList(siteInfo));
            } else {
                mainWrapper.setSiteDataList(Arrays.asList(siteInfo));
            }
        }
        nvl3UpdateService.updateSectorWiseSummaryIntoGeoMetaData(mainWrapper.getSiteInfo(), genricWorkOrder);
        List<DriveDataWrapper> imageDataList = new ArrayList<>();
        if (!isSiteAcceptance) {
            imageDataList.addAll(getSpeedTestImagesFromHbase(genricWorkOrder, driveRecipeDetailMap));
        }
        List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
        HashMap<String, Object> imagesMap = getImagesDataForDrive(siteInfoList, genricWorkOrder, csvDataMap,
                driveRecipeDetailMap, isSiteAcceptance, band, imageDataList, mobilityTestTypeMap, legendList,
                kpiIndexMap);
        logger.info("imagesMap  ==== {}", imagesMap);
        if (!isSiteAcceptance) {
            setHandoverDataToMainWrapper(mainWrapper, imagesMap,
                    getHandoverDataFromHbase(genricWorkOrder.getId(), new ArrayList<>(mobilityTestTypeMap.values()),
                            driveRecipeDetailMap.get(QMDLConstant.OPERATOR)),
                    SSVTReportUtils.getAllPossibleIntraHOCombinations(filteredSiteInfo));
            setCallDataToMainWrapper(mainWrapper, imagesMap, csvDataMap.get(SSVTConstants.DRIVE_DATA_TYPE_DRIVE),
                    filteredSiteInfo, kpiIndexMap);
            setKpiDataToMainWrapper(mainWrapper, csvDataMap, imagesMap, legendList, kpiIndexMap);
        }
        Map<String, Object> paramMap = new HashMap<>();
        setPlotImagesToParamMap(paramMap, imagesMap, isSiteAcceptance);
        putSFIntegrationDataToJsonMap(jsonDataMap, isSiteAcceptance, siteName, genricWorkOrder.getGwoMeta());
        return proceedToCreateSSVTReport(mainWrapper, genricWorkOrder, jsonDataMap, paramMap);
    }

    private void putSFIntegrationDataToJsonMap(Map<String, Object> jsonDataMap, boolean isSiteAcceptance,
            String siteName, Map<String, String> gwoMeta) {
        if (gwoMeta != null && !gwoMeta.isEmpty() && gwoMeta.containsKey(SSVTConstants.KEY_TASK_NAME)) {
            jsonDataMap.put(SSVTConstants.KEY_TASK_NAME, gwoMeta.get(SSVTConstants.KEY_TASK_NAME));
        } else {
            jsonDataMap.put(SSVTConstants.KEY_TASK_NAME, isSiteAcceptance ?
                    SSVTConstants.SF_INTEGRATION_TASK_ATP_1C :
                    SSVTConstants.SF_INTEGRATION_TASK_ATP_1E);
        }
        if (!StringUtils.isBlank(siteName)) {
            jsonDataMap.put(SSVTConstants.KEY_SITE_NAME, siteName);
        }
    }

    private HashMap<String, Object> getImagesDataForDrive(List<SiteInformationWrapper> siteInfoList,
                                                          GenericWorkorder genricWorkOrder, Map<String, List<String[]>> csvDataMap,
                                                          Map<String, List<String>> driveRecipeDetailMap, boolean isSiteAcceptance, String band,
                                                          List<DriveDataWrapper> imageDataList, Map<String, String> mobilityTestTypeMap,
                                                          List<LegendWrapper> legendList, Map<String, Integer> kpiIndexMap) throws IOException {
        HashMap<String, String> imagesMap = new HashMap<>();
        if (!isSiteAcceptance) {
            if (mobilityTestTypeMap.containsKey(SSVTConstants.RECIPE_SCRIPT_NAME_DOWNLOAD) && csvDataMap.containsKey(
                    SSVTConstants.KEY_RECIPE_MOBILITY_PREFIX + SSVTConstants.RECIPE_SCRIPT_NAME_DOWNLOAD)) {
                List<KPIWrapper> kpiListDownload = ReportUtil.convertLegendsListToKpiWrapperList(legendList,
                        SSVTConstants.getKpiIndexMapForDownload(kpiIndexMap));
                imagesMap.putAll(getImageMapOfCombinedData(
                        csvDataMap.get(
                                SSVTConstants.KEY_RECIPE_MOBILITY_PREFIX + SSVTConstants.RECIPE_SCRIPT_NAME_DOWNLOAD),
                        siteInfoList, imageDataList, kpiListDownload, null, band, kpiIndexMap));
            }
            if (mobilityTestTypeMap.containsKey(SSVTConstants.RECIPE_SCRIPT_NAME_UPLOAD) && csvDataMap
                    .containsKey(SSVTConstants.KEY_RECIPE_MOBILITY_PREFIX + SSVTConstants.RECIPE_SCRIPT_NAME_UPLOAD)) {
                List<KPIWrapper> kpiListUpload = ReportUtil.convertLegendsListToKpiWrapperList(legendList,
                        SSVTConstants.getKpiIndexMapForUpload(kpiIndexMap));
                imagesMap.putAll(getImageMapOfCombinedData(
                        csvDataMap.get(
                                SSVTConstants.KEY_RECIPE_MOBILITY_PREFIX + SSVTConstants.RECIPE_SCRIPT_NAME_UPLOAD),
                        siteInfoList, imageDataList, kpiListUpload, null, band, kpiIndexMap));
            }
        }

        List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList,
                SSVTConstants.getKpiIndexMapForMobility(isSiteAcceptance, kpiIndexMap));
        if (!isSiteAcceptance) {
            imagesMap.putAll(getImageMapOfCombinedData(csvDataMap.get(SSVTConstants.DRIVE_DATA_TYPE_DRIVE),
                    siteInfoList, imageDataList, kpiList, null, band, kpiIndexMap));
        } else {
            imagesMap.putAll(getImageMapOfCombinedData(csvDataMap.get(SSVTConstants.DRIVE_DATA_TYPE_COMBINED),
                    siteInfoList, imageDataList, kpiList, null, band, kpiIndexMap));
        }
        return prepareImageMap(imagesMap, kpiIndexMap);
    }

    private void setKpiDataToMainWrapper(SSVTReportWrapper mainWrapper, Map<String, List<String[]>> csvDataMap,
                                         HashMap<String, Object> imagesMap, List<LegendWrapper> legendList, Map<String, Integer> kpiIndexMap) {
        KpiDataWrapper kpiDataWrapper = new KpiDataWrapper();
        setMapDataToMainWrapper(kpiDataWrapper, imagesMap);
        setGraphDataToMainWrapper(kpiDataWrapper, csvDataMap, legendList, kpiIndexMap);
        mainWrapper.setKpiDataList(Arrays.asList(kpiDataWrapper));
    }

    private void setMapDataToMainWrapper(KpiDataWrapper kpiDataWrapper, HashMap<String, Object> imagesMap) {
        List<KpiMapWrapper> kpiMapWrapperList = new ArrayList<>();
        if (!StringUtils.isBlank((String) imagesMap.get(ReportConstants.IMAGE_RSRP))) {
            kpiMapWrapperList.add(getKpiMapWrapperForKpi(ReportConstants.RSRP,
                    imagesMap.get(ReportConstants.IMAGE_RSRP), imagesMap.get(ReportConstants.IMAGE_RSRP_LEGEND)));
        }
        if (!StringUtils.isBlank((String) imagesMap.get(ReportConstants.IMAGE_RSRQ))) {
            kpiMapWrapperList.add(getKpiMapWrapperForKpi(ReportConstants.RSRQ,
                    imagesMap.get(ReportConstants.IMAGE_RSRQ), imagesMap.get(ReportConstants.IMAGE_RSRQ_LEGEND)));
        }
        if (!StringUtils.isBlank((String) imagesMap.get(ReportConstants.IMAGE_SINR))) {
            kpiMapWrapperList.add(getKpiMapWrapperForKpi(ReportConstants.SINR,
                    imagesMap.get(ReportConstants.IMAGE_SINR), imagesMap.get(ReportConstants.IMAGE_SINR_LEGEND)));
        }
        if (!StringUtils.isBlank((String) imagesMap.get(ReportConstants.IMAGE_DL))) {
            kpiMapWrapperList.add(getKpiMapWrapperForKpi(ReportConstants.MAC_DL_THROUGHPUT,
                    imagesMap.get(ReportConstants.IMAGE_DL), imagesMap.get(ReportConstants.IMAGE_DL_LEGEND)));
        }
        if (!StringUtils.isBlank((String) imagesMap.get(ReportConstants.IMAGE_UL))) {
            kpiMapWrapperList.add(getKpiMapWrapperForKpi(ReportConstants.MAC_UL_THROUGHPUT,
                    imagesMap.get(ReportConstants.IMAGE_UL), imagesMap.get(ReportConstants.IMAGE_UL_LEGEND)));
        }
        logger.info("KPI Map Wrapper list size: {}", kpiMapWrapperList.size());
        kpiDataWrapper.setKpiPlotList(kpiMapWrapperList);
    }

    private KpiMapWrapper getKpiMapWrapperForKpi(String kpiName, Object plotImage, Object legendImage) {
        KpiMapWrapper kpiMapWrapper = new KpiMapWrapper();
        kpiMapWrapper.setKpiName(kpiName.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING) + Symbol.SPACE_STRING
                + Symbol.PARENTHESIS_OPEN_STRING + ReportUtil.getUnitByKPiName(kpiName)
                + Symbol.PARENTHESIS_CLOSE_STRING);
        kpiMapWrapper.setKpiPlot((String) plotImage);
        kpiMapWrapper.setKpiLegend((String) legendImage);
        logger.info("Kpi Map Wrapper for {} is: {}", kpiName, kpiMapWrapper.toString());
        return kpiMapWrapper;
    }

    private void setGraphDataToMainWrapper(KpiDataWrapper kpiDataWrapper, Map<String, List<String[]>> csvDataMap,
                                           List<LegendWrapper> legendList, Map<String, Integer> kpiMap) {
        List<KpiGraphWrapper> kpiGraphWrapperList = new ArrayList<>();
        Map<String, Integer> kpiIndexMap = SSVTConstants.getKpiIndexMapForDownload(kpiMap);
        kpiIndexMap.putAll(SSVTConstants.getKpiIndexMapForUpload(kpiMap));
        List<KPIWrapper> kpiWrapperList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, kpiIndexMap);
        for (KPIWrapper kpiWrapper : kpiWrapperList) {
            KpiGraphWrapper kpiGraphWrapper = new KpiGraphWrapper();
            kpiGraphWrapper
                    .setKpiName(kpiWrapper.getKpiName().replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING));
            kpiGraphWrapper.setUnit(Symbol.PARENTHESIS_OPEN_STRING
                    + ReportUtil.getUnitByKPiName(kpiWrapper.getKpiName()) + Symbol.PARENTHESIS_CLOSE_STRING);
            List<Double> kpiDataList = new ArrayList<>();
            if (!kpiWrapper.getKpiName().equalsIgnoreCase(ReportConstants.MAC_UL_THROUGHPUT)) {
                kpiDataList.addAll(ReportUtil.convetArrayToList(
                        csvDataMap.get(
                                SSVTConstants.KEY_RECIPE_MOBILITY_PREFIX + SSVTConstants.RECIPE_SCRIPT_NAME_DOWNLOAD),
                        kpiWrapper.getIndexKPI()));
            } else {
                kpiDataList.addAll(ReportUtil.convetArrayToList(
                        csvDataMap.get(
                                SSVTConstants.KEY_RECIPE_MOBILITY_PREFIX + SSVTConstants.RECIPE_SCRIPT_NAME_UPLOAD),
                        kpiWrapper.getIndexKPI()));
            }
            if (Utils.isValidList(kpiDataList)) {
                kpiGraphWrapper.setMean(ReportUtil.round(
                        kpiDataList.stream().filter(x -> x != null).mapToDouble(x -> x).average().getAsDouble(),
                        ReportConstants.TWO_DECIMAL_PLACES));
                kpiGraphWrapper.setMin(ReportUtil.round(
                        kpiDataList.stream().filter(x -> x != null).mapToDouble(x -> x).min().getAsDouble(),
                        ReportConstants.TWO_DECIMAL_PLACES));
                kpiGraphWrapper.setMax(ReportUtil.round(
                        kpiDataList.stream().filter(x -> x != null).mapToDouble(x -> x).max().getAsDouble(),
                        ReportConstants.TWO_DECIMAL_PLACES));
                kpiGraphWrapper
                        .setCount(kpiDataList.stream().filter(x -> x != null).collect(Collectors.toList()).size());
                kpiGraphWrapper.setGraphDataList(getGraphDataWrapperForKpi(kpiWrapper, kpiDataList));
                kpiGraphWrapperList.add(kpiGraphWrapper);
            }
        }
        logger.info("KPI Graph Wrapper list size: {}", kpiGraphWrapperList.size());
        kpiDataWrapper.setKpiGraphList(kpiGraphWrapperList);
    }

    private List<GraphDataWrapper> getGraphDataWrapperForKpi(KPIWrapper kpiWrapper, List<Double> kpiDataList) {
        List<GraphDataWrapper> graphDataWrapperList = new ArrayList<>();
        double cdfValue = 0.0;
        Integer index = 0;
        List<RangeSlab> rangeList = kpiWrapper.getRangeSlabs();
        rangeList.sort(Comparator.comparing(RangeSlab::getLowerLimit).reversed());
        for (RangeSlab range : rangeList) {
            GraphDataWrapper graphDataWrapper = new GraphDataWrapper();
            graphDataWrapper.setFrom(range.getLowerLimit());
            graphDataWrapper.setTo(range.getUpperLimit());
            Integer dataInRange;
            if (index == rangeList.size() - 1) {
                dataInRange = kpiDataList.stream()
                        .filter(x -> x != null && x >= range.getLowerLimit() && x <= range.getUpperLimit())
                        .collect(Collectors.toList()).size();
            } else {
                dataInRange = kpiDataList.stream()
                        .filter(x -> x != null && x > range.getLowerLimit() && x <= range.getUpperLimit())
                        .collect(Collectors.toList()).size();
            }
            graphDataWrapper.setCount(dataInRange);
            Double pdfValue = ReportUtil.getPercentage(dataInRange, kpiDataList.size());
            graphDataWrapper.setPdfValue(pdfValue);
            cdfValue += pdfValue;
            graphDataWrapper.setCdfValue(cdfValue);
            graphDataWrapperList.add(graphDataWrapper);
            index++;
        }
        return graphDataWrapperList;
    }

    private List<DriveDataWrapper> getSpeedTestImagesFromHbase(GenericWorkorder genricWorkOrder,
                                                               Map<String, List<String>> driveRecipeDetailMap) {
        return nVReportHbaseDao.getSpeedTestDatafromHbase(genricWorkOrder.getId(),
                driveRecipeDetailMap.get(QMDLConstant.RECIPE), driveRecipeDetailMap.get(QMDLConstant.OPERATOR));
    }

    private List<SiteInformationWrapper> filterNEDataByBand(List<SiteInformationWrapper> siteInfoList, String band) {
        return siteInfoList.stream()
                .filter(c -> c.getNeFrequency() != null && c.getNeFrequency().equalsIgnoreCase(band))
                .collect(Collectors.toList());
    }

    private String findBandDetailByGWOMetaData(GenericWorkorder genricWorkOrder) {
        return genricWorkOrder.getGwoMeta().get(NVWorkorderConstant.BAND) != null
                ? findBandByGWOMeta(genricWorkOrder.getGwoMeta().get(NVWorkorderConstant.BAND))
                : null;
    }

    private Response proceedToCreateSSVTReport(SSVTReportWrapper mainWrapper, GenericWorkorder workorderObj,
                                               Map<String, Object> jsonMap, Map<String, Object> imageMap) {
        logger.info("Going to create SSVT Report");
        String reportAssetRepo = ConfigUtils.getString(ReportConstants.SSVTREPORT_JASPER_FOLDER_PATH);
        List<SSVTReportWrapper> dataSourceList = new ArrayList<>();
        dataSourceList.add(mainWrapper);
        JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
        imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
        imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
        imageMap.put(NVReportConstants.PARAM_KEY_NV_LOGO, reportAssetRepo + ReportConstants.LOGO_NV_IMG);

        logger.info("Found Parameter map: {}", new Gson().toJson(imageMap));

        String filePath = ConfigUtils.getString(ReportConstants.SSVT_REPORT_PATH);
        String fileName = ReportUtil.getFileName(workorderObj.getWorkorderId(),
                (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID), filePath);
        try {
            logger.info("Going to save report on path {}", fileName);
            JasperRunManager.runReportToPdfFile(reportAssetRepo + ReportConstants.MAIN_JASPER, fileName, imageMap,
                    rfbeanColDataSource);
            return updateStatusAndPersistReport(workorderObj, jsonMap, filePath, imageMap);

        } catch (JRException e) {
            logger.info("Exception while processing Jasper on path {} trace ==> {}", reportAssetRepo,
                    Utils.getStackTrace(e));
        } catch (UnsupportedEncodingException e) {
            logger.info("Exception while sending Request to SiteForge {} trace ==> {}", reportAssetRepo,
                    Utils.getStackTrace(e));
        }
        return Response.ok(ForesightConstants.FAILURE_JSON).build();
    }

	private void updateBasicSiteInfo(SiteInfoWrapper siteInfo, List<SiteInformationWrapper> filteredSiteInfo) {
		logger.info("Going to update Basic Site Infomation");
		SiteInformationWrapper wrapper = filteredSiteInfo.get(0);
		siteInfo.setLat(wrapper.getLat().toString());
		siteInfo.setLng(wrapper.getLon().toString());
		siteInfo.setSiteId(wrapper.getNename());
		siteInfo.setSiteName(wrapper.getSiteName());
		siteInfo.setTac(wrapper.getTac());
		siteInfo.setMcc(wrapper.getMcc().toString());
		siteInfo.setMnc(wrapper.getMnc().toString());
		siteInfo.setCellId(wrapper.getCellId());
	}

    private Response updateStatusAndPersistReport(GenericWorkorder workorderObj, Map<String, Object> jsonMap,
                                                  String filePath, Map<String, Object> imageMap) throws UnsupportedEncodingException {
        Integer analyticsrepoId = (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID);
        String hdfsPath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.SSVT
                + ReportConstants.FORWARD_SLASH + workorderObj.getTemplateType().name() + ReportConstants.FORWARD_SLASH
                + workorderObj.getGwoMeta().get(ReportConstants.SITE_ID) + ReportConstants.FORWARD_SLASH;
        String fileName = ReportUtil.getFileName(workorderObj.getWorkorderId(), analyticsrepoId, filePath);
        Response response = reportService.saveFileAndUpdateStatus(analyticsrepoId, hdfsPath, workorderObj,
                new File(fileName), SSVTReportUtils.getFileNameFromFilePath(fileName),
                NVWorkorderConstant.REPORT_INSTACE_ID); // Last parameter File Name
        String responseJson = (String) response.getEntity();
        if (SSVTReportUtils.validateMap(workorderObj.getGwoMeta(), SSVTConstants.META_KEY_PROJECT_ID)) {
            Map<String, Object> payLoadJsonMap = new HashMap<>();
            payLoadJsonMap.put(SSVTConstants.META_KEY_PROJECT_ID,
                    workorderObj.getGwoMeta().get(SSVTConstants.META_KEY_PROJECT_ID));
            payLoadJsonMap.put(SSVTConstants.KEY_REPORT_ID, analyticsrepoId);
            payLoadJsonMap.put(SSVTConstants.KEY_TASK_NAME, jsonMap.get(SSVTConstants.KEY_TASK_NAME));
            if (ForesightConstants.SUCCESS_JSON.equalsIgnoreCase(responseJson)
                    && SSVTReportUtils.validateMap(jsonMap, SSVTConstants.KEY_SITE_NAME)) {
                try {
                    SSVTReportUtils.sendPostRequestWithoutTimeOut(
                            ConfigUtils.getString(SSVTConstants.NV_SSVT_SF_INTEGRATION_URL)
                                    + jsonMap.get(SSVTConstants.KEY_SITE_NAME),
                            new StringEntity(new Gson().toJson(payLoadJsonMap), ContentType.APPLICATION_JSON));
                } catch (Exception e) {
                    logger.info("Going to print stacktrace {}", Utils.getStackTrace(e));
                }
            }
        }
        if (imageMap.get(ForesightConstants.TO_DELETED_KEY) != null) {
            logger.info("Going to deleted file of folder {}", imageMap.get(ForesightConstants.TO_DELETED_KEY));
            ReportUtils.deleteGeneratedFile(imageMap.get(ForesightConstants.TO_DELETED_KEY).toString());
        } else {
            logger.info("Nothing to delete");
        }
        logger.info("Report Created successfully");
        return response;
    }

    private void prepareSectorWiseData(SiteInfoWrapper siteInfo, List<SiteInformationWrapper> filteredSiteInfo,
                                       Map<String, List<String[]>> csvDataMap, boolean isSiteAcceptance, Map<String, Integer> kpiIndexMap, GenericWorkorder genericWorkorder) {
        logger.info("Going to update Sector Wise Info");
        Map<Integer, List<SectorWiseWrapper>> genericMap = new HashMap<>();
        List<Integer> pciList = reportService.getPciListFromWoMetaData(genericWorkorder.getGwoMeta());
        addPciToSiteInfoList(pciList, filteredSiteInfo);
        Collections.sort(filteredSiteInfo, (s1, s2) -> s1.getSector().compareTo(s2.getSector()));
        int sectorValue = 1;
        Map<Integer, String> sectorBandwidthMap = new HashMap<>();
        Map<Integer, Integer[]> sectorPciCellIdMap = new HashMap<>();
        for (SiteInformationWrapper siteObj : filteredSiteInfo) {
            if (siteObj.getSector() != null) {
                siteObj.setSector(sectorValue);
                sectorBandwidthMap.put(sectorValue, getBandWidthNumber(siteObj.getBandwidth()));
                sectorPciCellIdMap.put(sectorValue, new Integer[]{siteObj.getPci(), siteObj.getCellId()});
                updateSectorWiseData(siteObj, siteInfo, csvDataMap, genericMap, kpiIndexMap);
                sectorValue++;
            }
        }
        siteInfo.setBandwidth(getAllBandwidthsFromSectorBandwidthMap(sectorBandwidthMap));
        siteInfo.setOverallStatus(SSVTConstants.TEST_STATUS_PASS);
        if (genericMap.get(ForesightConstants.ONE) != null) {
            List<SectorWiseWrapper> sector1Wrapper = calculateLogicalSection(genericMap.get(ForesightConstants.ONE),
                    sectorBandwidthMap.get(ForesightConstants.ONE), true, true , true);
//			addFeedbackDataToSectorWrapper(sector1Wrapper, sectorPciCellIdMap.get(ForesightConstants.ONE));
            if (Utils.isValidList(sector1Wrapper) && sector1Wrapper.get(0).getOverallStatus() != null && sector1Wrapper.get(0).getOverallStatus().contains(SSVTConstants.TEST_STATUS_FAIL)) {
                siteInfo.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
            siteInfo.setModelAlpha(sector1Wrapper);
        }
        if (genericMap.get(ForesightConstants.TWO) != null) {
            List<SectorWiseWrapper> sector2Wrapper = calculateLogicalSection(genericMap.get(ForesightConstants.TWO),
                    sectorBandwidthMap.get(ForesightConstants.TWO), true, true , true);
//			addFeedbackDataToSectorWrapper(sector2Wrapper, sectorPciCellIdMap.get(ForesightConstants.TWO));
            if (Utils.isValidList(sector2Wrapper) && sector2Wrapper.get(0).getOverallStatus() != null && sector2Wrapper.get(0).getOverallStatus().contains(SSVTConstants.TEST_STATUS_FAIL)) {
                siteInfo.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
            siteInfo.setModelBeta(sector2Wrapper);
        }
        if (genericMap.get(ForesightConstants.THREE) != null) {
            List<SectorWiseWrapper> sector3Wrapper = calculateLogicalSection(genericMap.get(ForesightConstants.THREE),
                    sectorBandwidthMap.get(ForesightConstants.THREE), true, true , true);
//			addFeedbackDataToSectorWrapper(sector3Wrapper, sectorPciCellIdMap.get(ForesightConstants.THREE));
            if (Utils.isValidList(sector3Wrapper) && sector3Wrapper.get(0).getOverallStatus() != null && sector3Wrapper.get(0).getOverallStatus().contains(SSVTConstants.TEST_STATUS_FAIL)) {
                siteInfo.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
            siteInfo.setModelGamma(sector3Wrapper);
        }

        if (!isSiteAcceptance) {
            SSVTReportUtils.setMobilityDataToWrapper(siteInfo, csvDataMap, kpiIndexMap);
            updateSiteStatusToGenericWorkorder(genericWorkorder, siteInfo.getOverallStatus());
        }
    }

    private void addPciToSiteInfoList(List<Integer> pciList, List<SiteInformationWrapper> filteredSiteInfo) {
        for (SiteInformationWrapper siteInformationWrapper : filteredSiteInfo) {
            if (siteInformationWrapper.getPci() != null && Utils.isValidList(pciList) && pciList.contains(siteInformationWrapper.getPci())) {
                pciList.remove(siteInformationWrapper.getPci());
            } else {
                siteInformationWrapper.setPci(pciList.get(0));
                pciList.remove(0);
            }
        }
    }

    private void addFeedbackDataToSectorWrapper(List<SectorWiseWrapper> sector1Wrapper, Integer[] pciCellId) {
        if (Utils.isValidList(sector1Wrapper) && pciCellId != null && pciCellId[0] != null && pciCellId[1] != null) {
            List<ConsumerFeedback> pciWiseRatingList = consumerFeedbackDao.getPciWiseRating(pciCellId[0], pciCellId[1]);
            if (Utils.isValidList(pciWiseRatingList)) {
                ConsumerFeedback pciWiseRating = pciWiseRatingList.get(0);
                if (pciWiseRating != null) {
                    SectorWiseWrapper sectorWiseWrapper = sector1Wrapper.get(0);
                    sectorWiseWrapper.setRcsVoice(getFeedbackResultForValue(sectorWiseWrapper, pciWiseRating.getStarRatingVoiceRcs()));
                    sectorWiseWrapper.setRcsVideo(getFeedbackResultForValue(sectorWiseWrapper, pciWiseRating.getStarRatingVideoRcs()));
                    sectorWiseWrapper.setRcsMessaging(getFeedbackResultForValue(sectorWiseWrapper, pciWiseRating.getStarRatingMessagingRcs()));
                }
            }
        }
    }

    private String getFeedbackResultForValue(SectorWiseWrapper sectorWiseWrapper, Float stars) {
        if (stars != null) {
            if (stars == ForesightConstants.FIVE) {
                return SSVTConstants.TEST_STATUS_PASS.concat(ForesightConstants.OPEN_BRACKET)
                        .concat(String.valueOf(stars.intValue()))
                        .concat(ForesightConstants.CLOSED_BRACKET);
            } else {
                sectorWiseWrapper.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                return SSVTConstants.TEST_STATUS_FAIL.concat(ForesightConstants.OPEN_BRACKET)
                        .concat(String.valueOf(stars.intValue()))
                        .concat(ForesightConstants.CLOSED_BRACKET);
            }
        } else {
            sectorWiseWrapper.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            return SSVTConstants.TEST_STATUS_FAIL.concat(ForesightConstants.OPEN_BRACKET)
                    .concat(Symbol.HYPHEN_STRING)
                    .concat(ForesightConstants.CLOSED_BRACKET);
        }
    }

    private void updateSiteStatusToGenericWorkorder(GenericWorkorder genericWorkorder, String overallStatus) {
        if (!StringUtils.isBlank(overallStatus)) {
            String remark = genericWorkorder.getRemark();
            if (!StringUtils.isBlank(remark)) {
                if (!remark.contains("Overall Site Status")) {
                    remark = remark.concat(" Overall Site Status: " + overallStatus);
                    genericWorkorder.setRemark(remark);
                    iGenericWorkorderDao.update(genericWorkorder);
                } else if (!remark.contains(overallStatus)) {
                    remark = "Overall Site Status: " + overallStatus;
                    genericWorkorder.setRemark(remark);
                    iGenericWorkorderDao.update(genericWorkorder);
                }
            } else {
                genericWorkorder.setRemark("Overall Site Status: " + overallStatus);
                iGenericWorkorderDao.update(genericWorkorder);
            }
        }
    }

    private String getAllBandwidthsFromSectorBandwidthMap(Map<Integer, String> sectorBandwidthMap) {
        Set<String> bandwidthSet = new HashSet<>(sectorBandwidthMap.values());
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (String bandwidth : bandwidthSet) {
            if (!StringUtils.isBlank(bandwidth)) {
                String bandwidthNumber = getBandWidthNumber(bandwidth);
                if(!builder.toString().contains(bandwidthNumber)) {
                    builder.append(bandwidthNumber);
                    if (count < bandwidthSet.size() - 1) {
                        builder.append(Symbol.COMMA_STRING);
                    }
                }
            }
            count++;
        }
        return !StringUtils.isBlank(builder.toString().trim()) ? builder.toString() + " MHz" : null;
    }

	private String getBandWidthNumber(String bandwidth) {
		String dBandwidth = "5";
		if (bandwidth != null && !bandwidth.isEmpty()) {
			bandwidth = bandwidth.toLowerCase();
			if (bandwidth.contains("twenty") || bandwidth.contains(SSVTConstants.SITE_BANDWIDTH_20)) {
				return "20";
			} else if (bandwidth.contains("five") || bandwidth.contains(SSVTConstants.SITE_BANDWIDTH_5)) {
				return dBandwidth;
			}
		} else {
			return dBandwidth;
		}
		return dBandwidth;
	}

    private List<SectorWiseWrapper> calculateLogicalSection(List<SectorWiseWrapper> genericList, String bandwidth,
                        boolean isMobility, boolean isSiteAcceptance, boolean isWorkorderWiseSummary) {
        SectorWiseWrapper sector = new SectorWiseWrapper();
        Double latency = null;
        Double peakDl = null;
        Double peakUl = null;
        Double jitter = null;
        Double mobilityRsrp = null;
        Double mobilitySinr = null;
        Double packetLoss = null;
        sector.setOverallStatus(SSVTConstants.TEST_STATUS_PASS);
        int attachRequest = genericList.stream().filter(x -> !x.isMobility() && x.getAttachRequest() != null)
                .mapToInt(SectorWiseWrapper::getAttachRequest).sum();
        int attachSuccess = genericList.stream().filter(x -> !x.isMobility() && x.getAttachSucess() != null)
                .mapToInt(SectorWiseWrapper::getAttachSucess).sum();
        if (isWorkorderWiseSummary || (!isMobility && !isSiteAcceptance)) {
            findSucessRate(attachRequest, attachSuccess, sector, "ATTACH");
        }

        int rachAttempt = genericList.stream().filter(x -> !x.isMobility() && x.getRachAttempt() != null)
                .mapToInt(SectorWiseWrapper::getRachAttempt).sum();
        int rachSucess = genericList.stream().filter(x -> !x.isMobility() && x.getRachSucess() != null)
                .mapToInt(SectorWiseWrapper::getRachSucess).sum();
        if (isWorkorderWiseSummary || (!isMobility && !isSiteAcceptance)) {
            findSucessRate(rachAttempt, rachSucess, sector, "RACH");
        }

        int detachAttempt = genericList.stream().filter(x -> !x.isMobility() && x.getDetachRequest() != null)
                .mapToInt(SectorWiseWrapper::getDetachRequest).sum();
        int detachSuccess = genericList.stream().filter(x -> !x.isMobility() && x.getDetachSuccess() != null)
                .mapToInt(SectorWiseWrapper::getDetachSuccess).sum();
        if (isWorkorderWiseSummary || (!isMobility && !isSiteAcceptance)) {
            findSucessRate(detachAttempt, detachSuccess, sector, "DETACH");
        }

        int callSetupAttempt = genericList.stream().filter(x -> !x.isMobility() && x.getCallSetupAttempt() != null)
                .mapToInt(SectorWiseWrapper::getCallSetupAttempt).sum();
        int callSetupSuccess = genericList.stream().filter(x -> !x.isMobility() && x.getCallSetupSuccess() != null)
                .mapToInt(SectorWiseWrapper::getCallSetupSuccess).sum();
        if (isWorkorderWiseSummary || (!isMobility && !isSiteAcceptance)) {
            findSucessRate(callSetupAttempt, callSetupSuccess, sector, "CALL_SETUP");
        }

        int mtCallAttempt = genericList.stream().filter(x -> !x.isMobility() && x.getMtCallAttempt() != null)
                .mapToInt(SectorWiseWrapper::getMtCallAttempt).sum();
        int mtCallSuccess = genericList.stream().filter(x -> !x.isMobility() && x.getMtCallSuccess() != null)
                .mapToInt(SectorWiseWrapper::getMtCallSuccess).sum();
        if (isWorkorderWiseSummary || (!isMobility && !isSiteAcceptance)) {
            findSucessRate(mtCallAttempt, mtCallSuccess, sector, "MT_CALL");
        }

        OptionalDouble peakDLConditional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getAvgDLSample() != null)
                .mapToDouble(SectorWiseWrapper::getAvgDLSample).max();

        OptionalDouble peakULConditional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getAvgULSample() != null)
                .mapToDouble(SectorWiseWrapper::getAvgULSample).max();

        OptionalDouble avgRsrpOptional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getAvgRsrp() != null && NumberUtils.isCreatable(x.getAvgRsrp()))
                .mapToDouble(x -> Double.parseDouble(x.getAvgRsrp())).average();

        OptionalDouble avgSinrOptional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getAvgSinr() != null && NumberUtils.isCreatable(x.getAvgSinr()))
                .mapToDouble(x -> Double.parseDouble(x.getAvgSinr())).average();

        OptionalDouble avgRsrqOptional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getAvgRsrq() != null && NumberUtils.isCreatable(x.getAvgRsrq()))
                .mapToDouble(x -> Double.parseDouble(x.getAvgRsrq())).average();

        OptionalDouble avgDlOptional = genericList.stream().filter(x -> !x.isMobility() && x.getAvgDLSample() != null)
                .mapToDouble(SectorWiseWrapper::getAvgDLSample).average();

        OptionalDouble avgULOptional = genericList.stream().filter(x -> !x.isMobility() && x.getAvgULSample() != null)
                .mapToDouble(SectorWiseWrapper::getAvgULSample).average();

        OptionalDouble avgCallSetupTimeOptional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getCallSetupTime() != null && NumberUtils.isCreatable(x.getCallSetupTime()))
                .mapToDouble(x -> Double.parseDouble(x.getCallSetupTime()) / 1000).average();

        OptionalDouble avgLatency32Optional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getLatency32Bytes() != null && NumberUtils.isCreatable(x.getLatency32Bytes()))
                .mapToDouble(x -> Double.parseDouble(x.getLatency32Bytes())).average();

        OptionalDouble avgLatency1000Optional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getLatency1000Bytes() != null && NumberUtils.isCreatable(x.getLatency1000Bytes()))
                .mapToDouble(x -> Double.parseDouble(x.getLatency1000Bytes())).average();

        OptionalDouble avgLatency1500Optional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getLatency1500Bytes() != null && NumberUtils.isCreatable(x.getLatency1500Bytes()))
                .mapToDouble(x -> Double.parseDouble(x.getLatency1500Bytes())).average();

        OptionalDouble avgResponseTimeOptional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getPageLoadTime() != null && NumberUtils.isCreatable(x.getPageLoadTime()))
                .mapToDouble(x -> Double.parseDouble(x.getPageLoadTime())).average();

        Map<Double, List<Double>> stallingCountMap = genericList.stream().filter(x -> !x.isMobility() && x.getStallingCountSample() != null).collect(Collectors.groupingBy(x -> x.getStallingCountSample()[1], Collectors.mapping(x -> x.getStallingCountSample()[0], Collectors.toList())));

        List<Double> filteredLatencyList = calculateLatencyFor1CReport(genericList);
        OptionalDouble latencyOptional = filteredLatencyList.stream()
                .mapToDouble(x -> x).average();

        OptionalDouble jitterOptional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getAvgJitterSample() != null)
                .mapToDouble(SectorWiseWrapper::getAvgJitterSample).average();

        OptionalDouble attachLatencyOptional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getAttachLatencySample() != null)
                .mapToDouble(SectorWiseWrapper::getAttachLatencySample).average();

        OptionalDouble imsRegistrationOptional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getImsRegistrationTimeSample() != null)
                .mapToDouble(SectorWiseWrapper::getImsRegistrationTimeSample).average();

        OptionalDouble packetLossOptional = genericList.stream()
                .filter(x -> !x.isMobility() && x.getRtpPacketLossSample() != null)
                .mapToDouble(SectorWiseWrapper::getRtpPacketLossSample).average();

        OptionalDouble mobilityRsrpOptional = genericList.stream()
                .filter(x -> x.isMobility() && x.getAvgRsrpSample() != null)
                .mapToDouble(SectorWiseWrapper::getAvgRsrpSample).average();

        OptionalDouble mobilitySinrOptional = genericList.stream()
                .filter(x -> x.isMobility() && x.getAvgSinrSample() != null)
                .mapToDouble(SectorWiseWrapper::getAvgSinrSample).average();

        OptionalDouble mobilityMinMacDlOptional = genericList.stream()
                .filter(x -> x.isMobility() && x.getMacDlSample() != null)
                .mapToDouble(SectorWiseWrapper::getMacDlSample).min();

        OptionalDouble mobilityAvgMacDlOptional = genericList.stream()
                .filter(x -> x.isMobility() && x.getMacDlSample() != null)
                .mapToDouble(SectorWiseWrapper::getMacDlSample).average();

        OptionalDouble mobilityMaxMacDlOptional = genericList.stream()
                .filter(x -> x.isMobility() && x.getMacDlSample() != null)
                .mapToDouble(SectorWiseWrapper::getMacDlSample).max();

        OptionalDouble mobilityMinMacUlOptional = genericList.stream()
                .filter(x -> x.isMobility() && x.getMacUlSample() != null)
                .mapToDouble(SectorWiseWrapper::getMacUlSample).min();

        OptionalDouble mobilityAvgMacUlOptional = genericList.stream()
                .filter(x -> x.isMobility() && x.getMacUlSample() != null)
                .mapToDouble(SectorWiseWrapper::getMacUlSample).average();

        OptionalDouble mobilityMaxMacUlOptional = genericList.stream()
                .filter(x -> x.isMobility() && x.getMacUlSample() != null)
                .mapToDouble(SectorWiseWrapper::getMacUlSample).max();

        calculateConditionalKPIValues(sector, latency, peakDl, peakUl, jitter, mobilityRsrp, mobilitySinr,
                packetLoss, peakDLConditional, peakULConditional, latencyOptional, jitterOptional,
                mobilityRsrpOptional, mobilitySinrOptional, packetLossOptional, bandwidth,
                isMobility, isSiteAcceptance, isWorkorderWiseSummary);
        setCalculatedKPIValue(sector, avgDlOptional, avgULOptional, avgRsrpOptional, avgRsrqOptional, avgSinrOptional,
                attachLatencyOptional, imsRegistrationOptional, mobilityMinMacDlOptional, mobilityMaxMacDlOptional,
                mobilityAvgMacDlOptional, mobilityMinMacUlOptional, mobilityMaxMacUlOptional, mobilityAvgMacUlOptional);
        setMobilityThroughputDataToWrapper(sector, genericList, bandwidth, isMobility, isSiteAcceptance, isWorkorderWiseSummary);
        setOverlapingServerDataToWrapper(sector, genericList, bandwidth);
        setStationaryPingYoutubeCallDataToWrapper(sector, avgCallSetupTimeOptional, avgLatency32Optional, avgLatency1000Optional,
                avgLatency1500Optional, avgResponseTimeOptional, stallingCountMap, isMobility, isSiteAcceptance, isWorkorderWiseSummary);
        return Arrays.asList(sector);
    }

    private void setStationaryPingYoutubeCallDataToWrapper(SectorWiseWrapper sector,
                                                           OptionalDouble avgCallSetupTimeOptional, OptionalDouble avgLatency32Optional,
                                                           OptionalDouble avgLatency1000Optional, OptionalDouble avgLatency1500Optional,
                                                           OptionalDouble avgResponseTimeOptional, Map<Double, List<Double>> stallingCountMap,
                                                           boolean isMobility, boolean isSiteAcceptance, boolean isWorkorderWiseSummary) {
        if (isWorkorderWiseSummary || (!isMobility && !isSiteAcceptance)) {
			sector.setCallSetupTime(getFinalResultForKpi(avgCallSetupTimeOptional, 4.8));
			sector.setLatency32Bytes(getFinalResultForKpi(avgLatency32Optional, 100.0));
			//		sector.setLatency1000Bytes(getFinalResultForKpi(avgLatency1000Optional, 100.0));
			//		sector.setLatency1500Bytes(getFinalResultForKpi(avgLatency1500Optional, 100.0));
			String latencyOverallStatus = getLatencyOverallStatus(sector.getLatency32Bytes(),
					sector.getLatency1000Bytes(), sector.getLatency1500Bytes());
			sector.setPageLoadTime(getFinalResultForKpi(avgResponseTimeOptional, 10.0));
			sector.setYoutubeStalling(getYoutubeStallingResult(stallingCountMap));
			if (sector	.getCallSetupTime()
						.contains(SSVTConstants.TEST_STATUS_FAIL)
					|| latencyOverallStatus.contains(SSVTConstants.TEST_STATUS_FAIL) || sector	.getPageLoadTime()
																								.contains(
																										SSVTConstants.TEST_STATUS_FAIL)
					|| sector	.getYoutubeStalling()
								.contains(SSVTConstants.TEST_STATUS_FAIL)) {
				sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
			} 
		}
    }

    private String getLatencyOverallStatus(String latency32Bytes, String latency1000Bytes, String latency1500Bytes) {
//		if(latency32Bytes.contains(Symbol.HYPHEN_STRING) && latency1000Bytes.contains(Symbol.HYPHEN_STRING) && latency1500Bytes.contains(Symbol.HYPHEN_STRING)){
//			return SSVTConstants.TEST_STATUS_FAIL;
//		}
//		if ((!latency32Bytes.contains(Symbol.HYPHEN_STRING) && latency32Bytes.contains(SSVTConstants.TEST_STATUS_FAIL))
//				|| (!latency1000Bytes.contains(Symbol.HYPHEN_STRING) && latency1000Bytes.contains(
//				SSVTConstants.TEST_STATUS_FAIL)) || (!latency1500Bytes.contains(Symbol.HYPHEN_STRING)
//				&& latency1500Bytes.contains(SSVTConstants.TEST_STATUS_FAIL))) {
//			return SSVTConstants.TEST_STATUS_FAIL;
//		}
        return latency32Bytes;
    }

    private String getFinalResultForKpi(OptionalDouble doubleValue, Double threshold) {
        if (doubleValue != null && doubleValue.isPresent()) {
            Double kpiValue = ReportUtil.round(doubleValue.getAsDouble(), 2);
            if (kpiValue <= threshold) {
                return SSVTConstants.TEST_STATUS_PASS + Symbol.PARENTHESIS_OPEN_STRING + kpiValue
                        + Symbol.PARENTHESIS_CLOSE_STRING;
            } else {
                return SSVTConstants.TEST_STATUS_FAIL + Symbol.PARENTHESIS_OPEN_STRING + kpiValue
                        + Symbol.PARENTHESIS_CLOSE_STRING;
            }
        }
        return SSVTConstants.TEST_STATUS_FAIL + Symbol.PARENTHESIS_OPEN_STRING + Symbol.HYPHEN_STRING
                + Symbol.PARENTHESIS_CLOSE_STRING;
    }

    private String getYoutubeStallingResult(Map<Double, List<Double>> stallingCountMap) {
        if (!stallingCountMap.isEmpty()) {
            Double stallingCount = 0.0;
            for (Entry<Double, List<Double>> stallingEntry : stallingCountMap.entrySet()) {
                OptionalDouble stallingOptional = stallingEntry.getValue().stream().mapToDouble(x -> x).max();
                if (stallingOptional.isPresent()) {
                    stallingCount += stallingOptional.getAsDouble();
                }
            }
            if (stallingCount == 0) {
                return SSVTConstants.TEST_STATUS_PASS + Symbol.PARENTHESIS_OPEN_STRING + stallingCount
                        + Symbol.PARENTHESIS_CLOSE_STRING;
            } else {
                return SSVTConstants.TEST_STATUS_FAIL + Symbol.PARENTHESIS_OPEN_STRING + stallingCount
                        + Symbol.PARENTHESIS_CLOSE_STRING;
            }
        } else {
            return SSVTConstants.TEST_STATUS_FAIL + Symbol.PARENTHESIS_OPEN_STRING + Symbol.HYPHEN_STRING
                    + Symbol.PARENTHESIS_CLOSE_STRING;
        }
    }

    private List<Double> calculateLatencyFor1CReport(List<SectorWiseWrapper> genericList) {
        List<Double> filteredList = genericList.stream()
                .filter(x -> !x.isMobility() && x.getAvgLatencySample() != null)
                .map(SectorWiseWrapper::getAvgLatencySample)
                .sorted()
                .collect(Collectors.toList());
        if (Utils.isValidList(filteredList)) {
            return filteredList.size() > 5 ? filteredList.subList(0, 5) : filteredList;
        }
        return Collections.emptyList();
    }

    private void setOverlapingServerDataToWrapper(SectorWiseWrapper sector, List<SectorWiseWrapper> genericList,
                                                  String bandwidth) {
        List<SectorWiseWrapper> overlappingServerList = genericList.stream()
                .filter(x -> x.getOverlappingServerSample() != null).collect(Collectors.toList());
        Integer serverInCriteria = overlappingServerList.stream().filter(x -> x.getOverlappingServerSample() <= 3)
                .collect(Collectors.toList()).size();
        Double overlappingPercentage = ReportUtil.round(
                ReportUtil.getPercentage(serverInCriteria, overlappingServerList.size()),
                ReportConstants.TWO_DECIMAL_PLACES);
        if (overlappingPercentage != null) {
            sector.setOverlappingServers(overlappingPercentage + Symbol.PERCENT_STRING);
        }
    }

    private void setMobilityThroughputDataToWrapper(SectorWiseWrapper sector, List<SectorWiseWrapper> genericList,
                                                    String bandwidth, boolean isMobility,
                                                    boolean isSiteAcceptance, boolean isWorkorderWiseSummary) {
        Double dlCriteria;
        Double ulCriteria;
        if (SSVTConstants.SITE_BANDWIDTH_20.equalsIgnoreCase(bandwidth)) {
            dlCriteria = 4.5;
            ulCriteria = 0.512;
        } else {
            dlCriteria = 1.5;
            ulCriteria = 0.128;
        }
        List<Double> macDlSamples = genericList.stream().filter(x -> x.getMacDlSample() != null)
                .map(SectorWiseWrapper::getMacDlSample).collect(Collectors.toList());
        List<Double> macUlSamples = genericList.stream().filter(x -> x.getMacUlSample() != null)
                .map(SectorWiseWrapper::getMacUlSample).collect(Collectors.toList());

        Integer dlSamplesInCriteria = macDlSamples.stream().filter(x -> x >= dlCriteria).collect(Collectors.toList())
                .size();
        Integer ulSamplesInCriteria = macUlSamples.stream().filter(x -> x >= ulCriteria).collect(Collectors.toList())
                .size();

        Double dlPercentage = ReportUtil.round(ReportUtil.getPercentage(dlSamplesInCriteria, macDlSamples.size()),
                ReportConstants.TWO_DECIMAL_PLACES);
        Double ulPercentage = ReportUtil.round(ReportUtil.getPercentage(ulSamplesInCriteria, macUlSamples.size()),
                ReportConstants.TWO_DECIMAL_PLACES);
        if (isWorkorderWiseSummary || (isMobility && !isSiteAcceptance)) {
			sector.setMacDlPercentage(dlPercentage != null ? String.valueOf(dlPercentage) : null);
			sector.setMacUlPercentage(ulPercentage != null ? String.valueOf(ulPercentage) : null);
			sector.setMacDlStatus(dlPercentage != null && dlPercentage >= 95 ? SSVTConstants.TEST_STATUS_PASS
					: SSVTConstants.TEST_STATUS_FAIL);
			sector.setMacUlStatus(ulPercentage != null && ulPercentage >= 95 ? SSVTConstants.TEST_STATUS_PASS
					: SSVTConstants.TEST_STATUS_FAIL);
			if (sector	.getMacDlStatus()
						.contains(SSVTConstants.TEST_STATUS_FAIL)
					|| sector	.getMacUlStatus()
								.contains(SSVTConstants.TEST_STATUS_FAIL)) {
				sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
			} 
		}
    }

    private void calculateConditionalKPIValues(SectorWiseWrapper sector, Double latency, Double peakDl,
                                               Double peakUl, Double jitter, Double mobilityRsrp, Double mobilitySinr, Double packetLoss, OptionalDouble peakDlOptional, OptionalDouble peakULOptional,
                                               OptionalDouble latencyOptional, OptionalDouble jitterOptional, OptionalDouble mobilityRsrpOptional,
                                               OptionalDouble mobilitySinrOptional, OptionalDouble packetLossOptional, String bandwidth,
                                               boolean isMobility, boolean isSiteAcceptance, boolean isWorkorderWiseSummary) {

        if (peakDlOptional.isPresent()) {
            peakDl = (double) ReportUtil.round(peakDlOptional.getAsDouble(), 2);
        }

        if (peakULOptional.isPresent()) {
            peakUl = (double) ReportUtil.round(peakULOptional.getAsDouble(), 2);
        }

        if (latencyOptional.isPresent()) {
            latency = (double) ReportUtil.round(latencyOptional.getAsDouble(), 2);
        }

        if (jitterOptional.isPresent()) {
            jitter = (double) ReportUtil.round(jitterOptional.getAsDouble(), 2);
        }

        if (mobilityRsrpOptional.isPresent()) {
            mobilityRsrp = (double) ReportUtil.round(mobilityRsrpOptional.getAsDouble(), 2);
        }

        if (mobilitySinrOptional.isPresent()) {
            mobilitySinr = (double) ReportUtil.round(mobilitySinrOptional.getAsDouble(), 2);
        }
        if (packetLossOptional.isPresent()) {
            packetLoss = packetLossOptional.getAsDouble();
        }
        checkKPIValueResult(sector, peakDl, peakUl, latency, jitter, mobilityRsrp, mobilitySinr, packetLoss,
                bandwidth, isMobility, isSiteAcceptance, isWorkorderWiseSummary);
    }

    private void setCalculatedKPIValue(SectorWiseWrapper sector, OptionalDouble avgDLConditional,
                                       OptionalDouble avgULConditional, OptionalDouble avgRsrpOptional, OptionalDouble avgRsrqOptional,
                                       OptionalDouble avgSinrOptional, OptionalDouble attachLatencyOptional,
                                       OptionalDouble imsRegistrationOptional, OptionalDouble minMacDLConditional,
                                       OptionalDouble maxMacDLConditional, OptionalDouble avgMacDLOptional, OptionalDouble minMacULConditional,
                                       OptionalDouble maxMacULConditional, OptionalDouble avgMacULOptional) {
        if (avgDLConditional.isPresent()) {
            sector.setAvgDL(String.valueOf((double) ReportUtil.round(avgDLConditional.getAsDouble(), 2)));
        }

        if (avgULConditional.isPresent()) {
            sector.setAvgUL(String.valueOf((double) ReportUtil.round(avgULConditional.getAsDouble(), 2)));
        }

        if (avgRsrpOptional.isPresent()) {
            sector.setAvgRsrp(String.valueOf((double) ReportUtil.round(avgRsrpOptional.getAsDouble(), 2)));
        }

        if (avgRsrqOptional.isPresent()) {
            sector.setAvgRsrq(String.valueOf((double) ReportUtil.round(avgRsrqOptional.getAsDouble(), 2)));
        }

        if (attachLatencyOptional.isPresent()) {
            sector.setAttachLatency(String.valueOf((double) ReportUtil.round(attachLatencyOptional.getAsDouble(), 2)));
        }

        if (imsRegistrationOptional.isPresent()) {
            sector.setImsRegistration(
                    String.valueOf((double) ReportUtil.round(imsRegistrationOptional.getAsDouble(), 2)));
        }

        if (minMacDLConditional.isPresent()) {
            sector.setMinMacDl(String.valueOf((double) ReportUtil.round(minMacDLConditional.getAsDouble(), 2)));
        }

        if (maxMacDLConditional.isPresent()) {
            sector.setMaxMacDl(String.valueOf((double) ReportUtil.round(maxMacDLConditional.getAsDouble(), 2)));
        }

        if (avgMacDLOptional.isPresent()) {
            sector.setAvgMacDl(String.valueOf((double) ReportUtil.round(avgMacDLOptional.getAsDouble(), 2)));
        }

        if (minMacULConditional.isPresent()) {
            sector.setMinMacUl(String.valueOf((double) ReportUtil.round(minMacULConditional.getAsDouble(), 2)));
        }

        if (maxMacULConditional.isPresent()) {
            sector.setMaxMacUl(String.valueOf((double) ReportUtil.round(maxMacULConditional.getAsDouble(), 2)));
        }

        if (avgMacULOptional.isPresent()) {
            sector.setAvgMacUl(String.valueOf((double) ReportUtil.round(avgMacULOptional.getAsDouble(), 2)));
        }

        if (avgSinrOptional.isPresent()) {
            sector.setAvgSinr(String.valueOf((double) ReportUtil.round(avgSinrOptional.getAsDouble(), 2)));
        }

    }

    private void findSucessRate(Integer request, Integer response, SectorWiseWrapper sector, String key) {
        if (key.equalsIgnoreCase("ATTACH")) {
            if (request != null && response != null) {
                if (request <= response && request != 0 && response != 0) {
                    sector.setAttachSuccessRate(SSVTConstants.TEST_STATUS_PASS.concat(ForesightConstants.OPEN_BRACKET)
                            .concat(response.toString())
                            .concat(ForesightConstants.FORWARD_SLASH)
                            .concat(request.toString())
                            .concat(ForesightConstants.CLOSED_BRACKET));
                } else {
                    sector.setAttachSuccessRate(SSVTConstants.TEST_STATUS_FAIL.concat(ForesightConstants.OPEN_BRACKET)
                            .concat(response.toString())
                            .concat(ForesightConstants.FORWARD_SLASH)
                            .concat(request.toString())
                            .concat(ForesightConstants.CLOSED_BRACKET));
                    sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setAttachSuccessRate(SSVTConstants.TEST_STATUS_FAIL.concat(ForesightConstants.OPEN_BRACKET)
                        .concat(Symbol.HYPHEN_STRING)
                        .concat(ForesightConstants.CLOSED_BRACKET));
                sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        }
        if (key.equalsIgnoreCase("RACH")) {
            if (request != null && response != null) {
                if (request <= response && request != 0 && response != 0) {
                    sector.setRachSuccessRate(SSVTConstants.TEST_STATUS_PASS.concat(ForesightConstants.OPEN_BRACKET)
                            .concat(response.toString())
                            .concat(ForesightConstants.FORWARD_SLASH)
                            .concat(request.toString())
                            .concat(ForesightConstants.CLOSED_BRACKET));
                } else {
                    sector.setRachSuccessRate(SSVTConstants.TEST_STATUS_FAIL.concat(ForesightConstants.OPEN_BRACKET)
                            .concat(response.toString())
                            .concat(ForesightConstants.FORWARD_SLASH)
                            .concat(request.toString())
                            .concat(ForesightConstants.CLOSED_BRACKET));
                    sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setRachSuccessRate(SSVTConstants.TEST_STATUS_FAIL.concat(ForesightConstants.OPEN_BRACKET)
                        .concat(Symbol.HYPHEN_STRING)
                        .concat(ForesightConstants.CLOSED_BRACKET));
                sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        }

        if (key.equalsIgnoreCase("DETACH")) {
            if (request != null && response != null) {
                if (request <= response && request != 0 && response != 0) {
                    sector.setDetachSuccessRate(SSVTConstants.TEST_STATUS_PASS.concat(ForesightConstants.OPEN_BRACKET)
                            .concat(response.toString())
                            .concat(ForesightConstants.FORWARD_SLASH)
                            .concat(request.toString())
                            .concat(ForesightConstants.CLOSED_BRACKET));
                } else {
                    sector.setDetachSuccessRate(SSVTConstants.TEST_STATUS_FAIL.concat(ForesightConstants.OPEN_BRACKET)
                            .concat(response.toString())
                            .concat(ForesightConstants.FORWARD_SLASH)
                            .concat(request.toString())
                            .concat(ForesightConstants.CLOSED_BRACKET));
                    sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setDetachSuccessRate(SSVTConstants.TEST_STATUS_FAIL.concat(ForesightConstants.OPEN_BRACKET)
                        .concat(Symbol.HYPHEN_STRING)
                        .concat(ForesightConstants.CLOSED_BRACKET));
                sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        }

        if (key.equalsIgnoreCase("CALL_SETUP")) {
            if (request != null && response != null) {
                if (request <= response && request != 0 && response != 0) {
                    sector.setVoLTECallSetup(SSVTConstants.TEST_STATUS_PASS.concat(ForesightConstants.OPEN_BRACKET)
                            .concat(response.toString())
                            .concat(ForesightConstants.FORWARD_SLASH)
                            .concat(request.toString())
                            .concat(ForesightConstants.CLOSED_BRACKET));
                } else {
                    sector.setVoLTECallSetup(SSVTConstants.TEST_STATUS_FAIL.concat(ForesightConstants.OPEN_BRACKET)
                            .concat(response.toString())
                            .concat(ForesightConstants.FORWARD_SLASH)
                            .concat(request.toString())
                            .concat(ForesightConstants.CLOSED_BRACKET));
                    sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setVoLTECallSetup(SSVTConstants.TEST_STATUS_FAIL.concat(ForesightConstants.OPEN_BRACKET)
                        .concat(Symbol.HYPHEN_STRING)
                        .concat(ForesightConstants.CLOSED_BRACKET));
                sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        }

        if (key.equalsIgnoreCase("MT_CALL")) {
            if (request != null && response != null) {
                if (request <= response && request != 0 && response != 0) {
                    sector.setPagingSucessRate(SSVTConstants.TEST_STATUS_PASS.concat(ForesightConstants.OPEN_BRACKET)
                            .concat(response.toString())
                            .concat(ForesightConstants.FORWARD_SLASH)
                            .concat(request.toString())
                            .concat(ForesightConstants.CLOSED_BRACKET));
                } else {
                    sector.setPagingSucessRate(SSVTConstants.TEST_STATUS_FAIL.concat(ForesightConstants.OPEN_BRACKET)
                            .concat(response.toString())
                            .concat(ForesightConstants.FORWARD_SLASH)
                            .concat(request.toString())
                            .concat(ForesightConstants.CLOSED_BRACKET));
//					sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setPagingSucessRate(SSVTConstants.TEST_STATUS_FAIL.concat(ForesightConstants.OPEN_BRACKET)
                        .concat(Symbol.HYPHEN_STRING)
                        .concat(ForesightConstants.CLOSED_BRACKET));
//				sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        }
    }

    private void checkKPIValueResult(SectorWiseWrapper sector, Double peakDl, Double peakUl,
                                     Double latency, Double jitter, Double mobilityRsrp, Double mobilitySinr, Double packetLoss,
                                     String bandwidth, boolean isMobility, boolean isSiteAcceptance, boolean isWorkorderWiseSummary) {
        if (isWorkorderWiseSummary || (isSiteAcceptance && !isMobility)) {
            findKPIResultStatus(peakDl, ForesightConstants.DL_PREFIX, sector, bandwidth);
            findKPIResultStatus(peakUl, ForesightConstants.UL_PREFIX, sector, bandwidth);
            findKPIResultStatus(latency, ReportConstants.LATENCY, sector, bandwidth);
            findKPIResultStatus(jitter, ReportConstants.JITTER, sector, bandwidth);
        }
        if (isWorkorderWiseSummary || (!isSiteAcceptance && !isMobility)) {
            findKPIResultStatus(packetLoss, SSVTConstants.PACKET_LOSS, sector, bandwidth);
        }

        if (isWorkorderWiseSummary || (!isSiteAcceptance && isMobility)) {
            findKPIResultStatus(mobilityRsrp, SSVTConstants.MOBILITY_RSRP, sector, bandwidth);
            findKPIResultStatus(mobilitySinr, SSVTConstants.MOBILITY_SINR, sector, bandwidth);
        }
    }

    private void findKPIResultStatus(Double kpiValue, String kpiName, SectorWiseWrapper sector, String bandwidth) {
        String success = SSVTConstants.TEST_STATUS_PASS;
        String fail = SSVTConstants.TEST_STATUS_FAIL;
        String openBracket = Symbol.PARENTHESIS_OPEN_STRING;
        String closeBracket = Symbol.PARENTHESIS_CLOSE_STRING;

        checkForSinr(kpiValue, kpiName, sector, success, fail, openBracket, closeBracket);
        checkForDL(kpiValue, kpiName, sector, success, fail, openBracket, closeBracket, bandwidth);
        checkForUL(kpiValue, kpiName, sector, success, fail, openBracket, closeBracket, bandwidth);
        checkForLatency(kpiValue, kpiName, sector, success, fail, openBracket, closeBracket);
        checkForJitter(kpiValue, kpiName, sector, success, fail, openBracket, closeBracket);
        checkForPacketLoss(kpiValue, kpiName, sector, success, fail, openBracket, closeBracket);
        checkForMobilityKpi(kpiValue, kpiName, sector, success, fail, openBracket, closeBracket);
    }

    private void checkForPacketLoss(Double kpiValue, String kpiName, SectorWiseWrapper sector, String success,
                                    String fail, String openBracket, String closeBracket) {
        if (kpiName.equalsIgnoreCase(SSVTConstants.PACKET_LOSS)) {
            if (kpiValue != null) {
                if (kpiValue < 1.0) {
                    sector.setVoLTERTRPktLoss(success.concat(openBracket)
                            .concat(ReportUtil.round(kpiValue, 4).toString())
                            .concat(closeBracket));
                } else {
                    sector.setVoLTERTRPktLoss(fail.concat(openBracket).concat(ReportUtil.round(kpiValue, 4).toString()).concat(closeBracket));
                    sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setVoLTERTRPktLoss(fail.concat(openBracket).concat(Symbol.HYPHEN_STRING).concat(closeBracket));
                sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        }
    }

    private void checkForMobilityKpi(Double kpiValue, String kpiName, SectorWiseWrapper sector, String success,
                                     String fail, String openBracket, String closeBracket) {
        if (kpiName.equalsIgnoreCase(SSVTConstants.MOBILITY_RSRP)) {
            if (kpiValue != null) {
                if (kpiValue >= -102) {
                    sector.setMobilityRsrp(success.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
                } else {
                    sector.setMobilityRsrp(fail.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
                    sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setMobilityRsrp(fail.concat(openBracket).concat(Symbol.HYPHEN_STRING).concat(closeBracket));
                sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        } else if (kpiName.equalsIgnoreCase(SSVTConstants.MOBILITY_SINR)) {
            if (kpiValue != null) {
                if (kpiValue >= 2) {
                    sector.setMobilitySinr(success.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
                } else {
                    sector.setMobilitySinr(fail.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
                    sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setMobilitySinr(fail.concat(openBracket).concat(Symbol.HYPHEN_STRING).concat(closeBracket));
                sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        }
    }

    private void checkForJitter(Double kpiValue, String kpiName, SectorWiseWrapper sector, String success, String fail,
                                String openBracket, String closeBracket) {
        if (kpiName.equalsIgnoreCase(ReportConstants.JITTER)) {
            if (kpiValue != null) {
                if (kpiValue < 40) {
                    sector.setJitter(success.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
                } else {
                    sector.setJitter(fail.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
//					sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setJitter(fail.concat(openBracket).concat(Symbol.HYPHEN_STRING).concat(closeBracket));
//				sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        }
    }

    private void checkForLatency(Double kpiValue, String kpiName, SectorWiseWrapper sector, String success, String fail,
                                 String openBracket, String closeBracket) {
        if (kpiName.equalsIgnoreCase(ReportConstants.LATENCY)) {
            if (kpiValue != null) {
                if (kpiValue < 50) {
                    sector.setLatency(success.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
                } else {
                    sector.setLatency(fail.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
//					sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setLatency(fail.concat(openBracket).concat(Symbol.HYPHEN_STRING).concat(closeBracket));
//				sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        }
    }

    private void checkForUL(Double kpiValue, String kpiName, SectorWiseWrapper sector, String success, String fail,
                            String openBracket, String closeBracket, String bandwidth) {
        if (kpiName.equalsIgnoreCase(ForesightConstants.UL_PREFIX)) {
            if (kpiValue != null) {
                Integer kpiThreshold = SSVTConstants.SITE_BANDWIDTH_20.equalsIgnoreCase(bandwidth) ?
                        SSVTConstants.PEAK_UL_THRESHOLD_20_MHZ :
                        SSVTConstants.PEAK_UL_THRESHOLD_5_MHZ;
                if (kpiValue > kpiThreshold) {
                    sector.setPeakUL(success.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
                } else {
                    sector.setPeakUL(fail.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
//					sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setPeakUL(fail.concat(openBracket).concat(Symbol.HYPHEN_STRING).concat(closeBracket));
//				sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        }
    }

    private void checkForDL(Double kpiValue, String kpiName, SectorWiseWrapper sector, String success, String fail,
                            String openBracket, String closeBracket, String bandwidth) {
        if (kpiName.equalsIgnoreCase(ForesightConstants.DL_PREFIX)) {
            if (kpiValue != null) {
                Integer kpiThreshold = SSVTConstants.SITE_BANDWIDTH_20.equalsIgnoreCase(bandwidth) ?
                        SSVTConstants.PEAK_DL_THRESHOLD_20_MHZ :
                        SSVTConstants.PEAK_DL_THRESHOLD_5_MHZ;
                if (kpiValue > kpiThreshold) {
                    sector.setPeakDL(success.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
                } else {
                    sector.setPeakDL(fail.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
//					sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setPeakDL(fail.concat(openBracket).concat(Symbol.HYPHEN_STRING).concat(closeBracket));
//				sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        }
    }

    private void checkForSinr(Double kpiValue, String kpiName, SectorWiseWrapper sector, String success, String fail,
                              String openBracket, String closeBracket) {
        if (kpiName.equalsIgnoreCase(ForesightConstants.SINR)) {
            if (kpiValue != null) {
                if (kpiValue > 18) {
                    sector.setAvgSinr(success.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
                } else {
                    sector.setAvgSinr(fail.concat(openBracket).concat(kpiValue.toString()).concat(closeBracket));
                    sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
            } else {
                sector.setAvgSinr(fail.concat(openBracket).concat(Symbol.HYPHEN_STRING).concat(closeBracket));
                sector.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }
        }
    }

    private void populateKPIResponse(String[] record, List<SectorWiseWrapper> genericList, boolean isMobility,
                                     Map<String, Integer> kpiIndexMap) {

        SectorWiseWrapper sectorWraper = new SectorWiseWrapper();

        Integer indexTestType = kpiIndexMap.get(ReportConstants.TEST_TYPE);
        String testType = record[indexTestType];
        sectorWraper.setMobility(isMobility);
        Integer indexRsrp = kpiIndexMap.get(ReportConstants.RSRP);
        Integer indexSinr = kpiIndexMap.get(ReportConstants.SINR);
        
        if(kpiIndexMap.get(ReportConstants.PCI_PLOT)!= null && SSVTReportUtils.checkValidation(
                kpiIndexMap.get(ReportConstants.PCI_PLOT), record)){
            sectorWraper.setPci(record[kpiIndexMap.get(ReportConstants.PCI_PLOT)]); // pci
        }
        
        if (!isMobility) {
            if (kpiIndexMap.get(ReportConstants.MSG1_COUNT) != null && SSVTReportUtils.checkValidation(
                    kpiIndexMap.get(ReportConstants.MSG1_COUNT), record)) {
                Integer indexRachAttempt = kpiIndexMap.get(ReportConstants.MSG1_COUNT);
                sectorWraper.setRachAttempt(Integer.parseInt(record[indexRachAttempt]));// RACH Attempt
            }
            if (kpiIndexMap.get(ReportConstants.MSG3_COUNT) != null && SSVTReportUtils.checkValidation(
                    kpiIndexMap.get(ReportConstants.MSG3_COUNT), record)) {
                Integer rachSuccess = kpiIndexMap.get(ReportConstants.MSG3_COUNT);
                sectorWraper.setRachSucess(Integer.parseInt(record[rachSuccess])); // RACH SUCESS
            }
            Integer indexAttachRequest = kpiIndexMap.get(ReportConstants.ATTACH_REQUEST);
            if (indexAttachRequest != null && SSVTReportUtils.checkValidation(indexAttachRequest, record)) {
                sectorWraper.setAttachRequest(Integer.parseInt(record[indexAttachRequest])); // attach request
            }
            Integer indexAttachComplete = kpiIndexMap.get(ReportConstants.ATTACH_COMPLETE);
            if (indexAttachComplete != null && SSVTReportUtils.checkValidation(indexAttachComplete, record)) {
                sectorWraper.setAttachSucess(Integer.parseInt(record[indexAttachComplete])); // attach acept
            }
            Integer indexDetachRequest = kpiIndexMap.get(ReportConstants.DETACH_REQUEST);
            if (indexDetachRequest != null && SSVTReportUtils.checkValidation(indexDetachRequest, record)) {
                sectorWraper.setDetachRequest(Integer.parseInt(record[indexDetachRequest]));
                sectorWraper.setDetachSuccess(Integer.parseInt(record[indexDetachRequest]));
            }
            if (indexTestType != null && SSVTReportUtils.checkValidation(indexTestType, record)) {
                if (indexRsrp != null && SSVTReportUtils.checkValidation(indexRsrp, record) && (
                        testType.equalsIgnoreCase(SSVTConstants.DL_FTP_CALL) || testType.equalsIgnoreCase(
                                SSVTConstants.DL_HTTP_CALL))) {
                    sectorWraper.setAvgRsrp(record[indexRsrp]); // avgRsrp
                }
                Integer indexRsrq = kpiIndexMap.get(ReportConstants.RSRQ);
                if (indexRsrq != null && SSVTReportUtils.checkValidation(indexRsrq, record) && (
                        testType.equalsIgnoreCase(SSVTConstants.DL_FTP_CALL) || testType.equalsIgnoreCase(
                                SSVTConstants.DL_HTTP_CALL))) {
                    sectorWraper.setAvgRsrq(record[indexRsrq]); // avgRsrq
                }
                if (indexSinr != null && SSVTReportUtils.checkValidation(indexSinr, record) && (
                        testType.equalsIgnoreCase(SSVTConstants.DL_FTP_CALL) || testType.equalsIgnoreCase(
                                SSVTConstants.DL_HTTP_CALL))) {
                    sectorWraper.setAvgSinr(record[indexSinr]); // avgSinr
                }
                Integer indexDl = kpiIndexMap.get(ReportConstants.DL_THROUGHPUT);
                if (indexDl != null && SSVTReportUtils.checkValidation(indexDl, record) && (
                        testType.equalsIgnoreCase(SSVTConstants.DL_FTP_CALL) || testType.equalsIgnoreCase(
                                SSVTConstants.DL_HTTP_CALL))) {
                    sectorWraper.setAvgDLSample(Double.parseDouble(record[indexDl])); // avgDl
                }
                Integer indexUl = kpiIndexMap.get(ReportConstants.UL_THROUGHPUT);
                if (indexUl != null && SSVTReportUtils.checkValidation(indexUl, record) && (
                        testType.equalsIgnoreCase(SSVTConstants.UL_FTP_CALL) || testType.equalsIgnoreCase(
                                SSVTConstants.UL_HTTP_CALL))) {
                    sectorWraper.setAvgULSample(Double.parseDouble(record[indexUl])); // avgUl
                }
                Integer indexLatency = kpiIndexMap.get(ReportConstants.LATENCY);
                if (indexLatency != null && SSVTReportUtils.checkValidation(indexLatency, record)
                        && testType.equalsIgnoreCase(SSVTConstants.PING)) {
                    sectorWraper.setAvgLatencySample(Double.parseDouble(record[indexLatency])); // latency
                }
                Integer indexJitter = kpiIndexMap.get(ReportConstants.JITTER);
                if (indexJitter != null && SSVTReportUtils.checkValidation(indexJitter, record)
                        && testType.equalsIgnoreCase(SSVTConstants.PING)) {
                    sectorWraper.setAvgJitterSample(Double.parseDouble(record[indexJitter])); // jitter
                }
                Integer indexAttachLatency = kpiIndexMap.get(ReportConstants.ATTACH_LATENCY);
                if (indexAttachLatency != null && SSVTReportUtils.checkValidation(indexAttachLatency, record)) {
                    sectorWraper.setAttachLatencySample(Double.parseDouble(record[indexAttachLatency])); // Attach
                }
                Integer indexCallInitiate = kpiIndexMap.get(ReportConstants.CALL_INITIATE);
                if (indexCallInitiate != null && SSVTReportUtils.checkValidation(indexCallInitiate, record)) {
                    sectorWraper.setCallSetupAttempt(Integer.parseInt(record[indexCallInitiate])); // VoLTE Call Setup
                }
                Integer indexCallSetupSuccess = kpiIndexMap.get(ReportConstants.CALL_SETUP_SUCCESS);
                if (indexCallSetupSuccess != null && SSVTReportUtils.checkValidation(indexCallSetupSuccess, record)) {
                    sectorWraper.setCallSetupSuccess(Integer.parseInt(record[indexCallSetupSuccess])); // VoLTE Call
                }
                Integer indexIMSRegistration = kpiIndexMap.get(ReportConstants.IMS_REGISTRATION_TIME);
                if (indexIMSRegistration != null && SSVTReportUtils.checkValidation(indexIMSRegistration, record)) {
                    Double value = NVLayer3Utils.getDoubleValueForDriveDetail(record[indexIMSRegistration]);
                    if (value != null) {
                        sectorWraper.setImsRegistrationTimeSample(
                                ReportUtil.parseToFixedDecimalPlace(value / ReportConstants.THOUSNAND,
                                        ReportConstants.INDEX_TWO));
                    }
                }
                Integer indexMTCallAttempt = kpiIndexMap.get(ReportConstants.MT_CALL_ATTEMPT);
                if (indexMTCallAttempt != null && SSVTReportUtils.checkValidation(indexMTCallAttempt, record)) {
                    sectorWraper.setMtCallAttempt(Integer.parseInt(record[indexMTCallAttempt])); // MT Call Attempt
                }
                Integer indexMTCallSuccess = kpiIndexMap.get(ReportConstants.MT_CALL_SUCCESS);
                if (indexMTCallSuccess != null && SSVTReportUtils.checkValidation(indexMTCallSuccess, record)) {
                    sectorWraper.setMtCallSuccess(Integer.parseInt(record[indexMTCallSuccess])); // MT Call Success
                }
                Integer indexRTPPacketLoss = kpiIndexMap.get(ReportConstants.VOLTE_PACKET_LOSS);
                if (indexRTPPacketLoss != null && SSVTReportUtils.checkValidation(indexRTPPacketLoss, record)) {
                    sectorWraper.setRtpPacketLossSample(Double.parseDouble(record[indexRTPPacketLoss]));
                }
                Integer indexCallSetupTime = kpiIndexMap.get(ReportConstants.CALL_SETUP_TIME);
                if (indexCallSetupTime != null && SSVTReportUtils.checkValidation(indexCallSetupTime, record) && (
                        testType.equalsIgnoreCase(SSVTConstants.LONG_CALL) || testType.equalsIgnoreCase(
                                SSVTConstants.SHORT_CALL))) {
                    sectorWraper.setCallSetupTime(record[indexCallSetupTime]); // avgCallSetupTime
                }
                Integer indexLatency32 = kpiIndexMap.get(ReportConstants.LATENCY32);
                if (indexLatency32 != null && SSVTReportUtils.checkValidation(indexLatency32, record)
                        && testType.equalsIgnoreCase(SSVTConstants.PING)) {
                    sectorWraper.setLatency32Bytes(record[indexLatency32]); // latency32Bytes
                }
                Integer indexLatency1000 = kpiIndexMap.get(ReportConstants.LATENCY1000);
                if (indexLatency1000 != null && SSVTReportUtils.checkValidation(indexLatency1000, record)
                        && testType.equalsIgnoreCase(SSVTConstants.PING)) {
                    sectorWraper.setLatency1000Bytes(record[indexLatency1000]); // latency1000Bytes
                }
                Integer indexLatency1500 = kpiIndexMap.get(ReportConstants.LATENCY1500);
                if (indexLatency1500 != null && SSVTReportUtils.checkValidation(indexLatency1500, record)
                        && testType.equalsIgnoreCase(SSVTConstants.PING)) {
                    sectorWraper.setLatency1500Bytes(record[indexLatency1500]); // latency1500Bytes
                }
                Integer indexResponseTime = kpiIndexMap.get(ReportConstants.WEB_DOWNLOAD_DELAY);
                if (indexResponseTime != null && SSVTReportUtils.checkValidation(indexResponseTime, record)
                        && testType.equalsIgnoreCase(ReportConstants.TEST_TYPE_WPT_BROWSE)) {
                    sectorWraper.setPageLoadTime(record[indexResponseTime]); // pageLoadTime
                }
                Integer indexStallingCount = kpiIndexMap.get(ReportConstants.NUM_OF_STALLING);
                Integer indexIteration = kpiIndexMap.get(ReportConstants.ITERATION);
                if (indexStallingCount != null && indexIteration != null && SSVTReportUtils.checkValidation(
                        indexStallingCount, record) && SSVTReportUtils.checkValidation(indexIteration, record)
                        && NumberUtils.isCreatable(record[indexStallingCount]) && NumberUtils.isCreatable(
                        record[indexIteration]) && testType.equalsIgnoreCase(ReportConstants.TEST_TYPE_YOUTUBE)) {
                    sectorWraper.setStallingCountSample(new Double[]{Double.parseDouble(record[indexStallingCount]),
                            Double.parseDouble(record[indexIteration])}); // youtubeStalling
                }
            }
        } else {
            if (indexTestType != null && SSVTReportUtils.checkValidation(indexTestType, record)) {
                if (indexRsrp != null && SSVTReportUtils.checkValidation(indexRsrp, record) && (
                        testType.equalsIgnoreCase(SSVTConstants.DL_FTP_CALL) || testType.equalsIgnoreCase(
                                SSVTConstants.DL_HTTP_CALL)) && NumberUtils.isCreatable(record[indexRsrp])) {
                    sectorWraper.setAvgRsrpSample(Double.parseDouble(record[indexRsrp])); // avgRsrp
                }
                if (indexSinr != null && SSVTReportUtils.checkValidation(indexSinr, record) && (
                        testType.equalsIgnoreCase(SSVTConstants.DL_FTP_CALL) || testType.equalsIgnoreCase(
                                SSVTConstants.DL_HTTP_CALL)) && NumberUtils.isCreatable(record[indexSinr])) {
                    sectorWraper.setAvgSinrSample(Double.parseDouble(record[indexSinr])); // avgSinr
                }
                Integer indexMacDl = kpiIndexMap.get(ReportConstants.MAC_DL_THROUGHPUT);

                if (indexMacDl != null && SSVTReportUtils.checkValidation(indexMacDl, record) && (
                        testType.equalsIgnoreCase(SSVTConstants.DL_FTP_CALL) || testType.equalsIgnoreCase(
                                SSVTConstants.DL_HTTP_CALL)) && NumberUtils.isCreatable(record[indexMacDl])) {

                    sectorWraper.setMacDlSample(Double.parseDouble(record[indexMacDl])); // macDlThp

                }
                Integer indexMacUl = kpiIndexMap.get(ReportConstants.MAC_UL_THROUGHPUT);
                if (indexMacUl != null && SSVTReportUtils.checkValidation(indexMacUl, record) && (
                        testType.equalsIgnoreCase(SSVTConstants.UL_FTP_CALL) || testType.equalsIgnoreCase(
                                SSVTConstants.UL_HTTP_CALL)) && NumberUtils.isCreatable(record[indexMacUl])) {
                    sectorWraper.setMacUlSample(Double.parseDouble(record[indexMacUl])); // macUlThp
                }
                Integer indexNeighbourData = kpiIndexMap.get(ReportConstants.NEIGHBOUR_DATA);
                if (indexNeighbourData != null && SSVTReportUtils.checkValidation(indexNeighbourData, record) && (
                        testType.equalsIgnoreCase(SSVTConstants.DL_FTP_CALL) || testType.equalsIgnoreCase(
                                SSVTConstants.DL_HTTP_CALL))) {
                    sectorWraper.setOverlappingServerSample(
                            getNeighbourData(record, indexNeighbourData)); // overlapping Servers
                }
            }
        }
        genericList.add(sectorWraper);
    }

    private boolean checkMultipleTestType(String testType) {
        Boolean flag = false;
        if (testType.equalsIgnoreCase(SSVTConstants.ATTACH_DETACH) || testType.equalsIgnoreCase(SSVTConstants.ATTACH)
                || testType.equalsIgnoreCase(SSVTConstants.DETACH)) {
            flag = true;
        }
        return flag;
    }

    private void updateSectorWiseData(SiteInformationWrapper siteObj, SiteInfoWrapper siteInfo,
                                      Map<String, List<String[]>> csvDataMap, Map<Integer, List<SectorWiseWrapper>> genericMap,
                                      Map<String, Integer> kpiIndexMap) {
        Integer sector = siteObj.getSector();
        SectorWiseWrapper sectorWraper = new SectorWiseWrapper();
        logger.info("Going to update Wrapper for sector Id {}", sector);
        if (sector == ForesightConstants.ONE) {
            commonSectorUpdation(siteObj, sectorWraper);
            prepareModelKpiWiseResponse(siteObj, csvDataMap, ForesightConstants.ONE, genericMap, kpiIndexMap);
            siteInfo.setAlpha(Arrays.asList(sectorWraper));
        }
        if (sector == ForesightConstants.TWO) {
            commonSectorUpdation(siteObj, sectorWraper);
            prepareModelKpiWiseResponse(siteObj, csvDataMap, ForesightConstants.TWO, genericMap, kpiIndexMap);
            siteInfo.setBeta(Arrays.asList(sectorWraper));
        }
        if (ForesightConstants.THREE.equals(sector)) {
            commonSectorUpdation(siteObj, sectorWraper);
            prepareModelKpiWiseResponse(siteObj, csvDataMap, ForesightConstants.THREE, genericMap, kpiIndexMap);
            siteInfo.setGamma(Arrays.asList(sectorWraper));
        }
    }

    private void commonSectorUpdation(SiteInformationWrapper siteObj, SectorWiseWrapper sectorWraper) {
        if (siteObj.getCellId() != null) {
            sectorWraper.setCellId(siteObj.getCellId().toString());
        }
        if (siteObj.getActualAzimuth() != null) {
            sectorWraper.setAzimuth(siteObj.getActualAzimuth().toString());
        }
        if (siteObj.getmTilt() != null) {
            sectorWraper.setmTilt(siteObj.getmTilt());
        }
        if (siteObj.geteTilt() != null) {
            sectorWraper.seteTilt(siteObj.geteTilt());
        }
        if (siteObj.getPci() != null) {
            sectorWraper.setPci(siteObj.getPci().toString());
        }
        if (siteObj.getDlEarfcn() != null) {
            sectorWraper.setEarfcn(siteObj.getDlEarfcn().toString());
        }
    }

    private void prepareModelKpiWiseResponse(SiteInformationWrapper siteObj, Map<String, List<String[]>> csvDataMap,
                                             Integer sectorId, Map<Integer, List<SectorWiseWrapper>> genericMap, Map<String, Integer> kpiIndexMap) {
        Integer pci = siteObj.getPci();
        Integer indexPci = kpiIndexMap.get(ReportConstants.PCI_PLOT);
        logger.info("PCI is: {}", pci);
        if (indexPci != null) {
            List<SectorWiseWrapper> genericList = new ArrayList<>();
            if (pci != null) {
                if (SSVTReportUtils.validateMap(csvDataMap, String.valueOf(pci))) {
                    for (String[] record : csvDataMap.get(String.valueOf(pci))) {
                        logger.info("PCI in record: {}", record[indexPci]);
                        if (SSVTReportUtils.checkValidation(indexPci, record) && pci == Integer.parseInt(record[indexPci])) {
                            populateKPIResponse(record, genericList, false, kpiIndexMap);
                        }
                    }
                }
            }
            if (SSVTReportUtils.validateMap(csvDataMap, SSVTConstants.DRIVE_DATA_TYPE_DRIVE)) {
                for (String[] record : csvDataMap.get(SSVTConstants.DRIVE_DATA_TYPE_DRIVE)) {
                    if (SSVTReportUtils.checkValidation(indexPci, record)
                            && pci == Integer.parseInt(record[indexPci])) {
                        populateKPIResponse(record, genericList, true, kpiIndexMap);
                    }
                }
            }

            logger.info("Sector Id {}  response size {}", sectorId, genericList.size());
            genericMap.put(sectorId, genericList);
        }
    }

    private String findBandByGWOMeta(String band) {
        if (band.equalsIgnoreCase(SSVTConstants.FDD3)) {
            band = ForesightConstants.BAND1800;
        } else if (band.equalsIgnoreCase(SSVTConstants.FDD5)) {
            band = ForesightConstants.BAND850;
        } else if (band.equalsIgnoreCase(SSVTConstants.TDD40)) {
            band = ForesightConstants.BAND2300;
        } else {
            band = null;
        }
        logger.info("Going to return band {}", band);
        return band;
    }

    private void updateSiteDetail(SSVTReportWrapper mainWrapper, List<SiteInformationWrapper> siteInfoList,
                                  GenericWorkorder genricWorkOrder, Map<String, Object> jsonDataMap, String[] summary,
                                  Map<String, Integer> summaryKpiIndexMap) {
        if (genricWorkOrder.getGwoMeta() != null
                && genricWorkOrder.getGwoMeta().get(NVWorkorderConstant.SITE_INFO) != null) {
            mainWrapper.setSiteName((String) ReportUtil
                    .convertStringToJsonObject(genricWorkOrder.getGwoMeta().get(NVWorkorderConstant.SITE_INFO))
                    .get("siteName"));
        }
        mainWrapper.setTestDate(
                ReportUtil.parseDateToString(ReportConstants.DATE_FORMAT_YYYYMMDD, genricWorkOrder.getCreationTime()));
        Integer indexSummaryTechnology = summaryKpiIndexMap.get(DriveHeaderConstants.TECHNOLOGY);
        Integer indexSummaryBand = summaryKpiIndexMap.get(ReportConstants.BAND);
        if (indexSummaryTechnology != null && summary != null && summary.length > indexSummaryTechnology) {
            if (!StringUtils.isBlank(summary[indexSummaryTechnology])) {
                mainWrapper.setTechnology(
                        summary[indexSummaryTechnology].replace(ReportConstants.UNDERSCORE, ReportConstants.COMMA));
            } else if (indexSummaryBand != null && summary.length > indexSummaryBand
                    && !StringUtils.isBlank(summary[indexSummaryBand])) {
                mainWrapper.setTechnology(getTechnologyFromBandType(summary[indexSummaryBand]));
            }
        }
        if (StringUtils.isBlank(mainWrapper.getTechnology())
                && genricWorkOrder.getGwoMeta().get(SSVTConstants.META_KEY_BAND) != null) {
            mainWrapper.setTechnology(genricWorkOrder.getGwoMeta().get(SSVTConstants.META_KEY_BAND).replaceAll("\\d+",
                    Symbol.EMPTY_STRING));
        }

        if (Utils.isValidList(siteInfoList)) {
            mainWrapper.setCityName(siteInfoList.get(ReportConstants.INDEX_ZER0).getCityName());
            mainWrapper.setSiteId(siteInfoList.get(ReportConstants.INDEX_ZER0).getNeId());
        }
        if (jsonDataMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY) != null) {
            mainWrapper.setTestEngineer(jsonDataMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY).toString());
        }
    }

    private static String getTechnologyFromBandType(String band) {
        String[] bands = band.split(Symbol.UNDERSCORE_STRING);
        StringBuilder builder = new StringBuilder();
        for (String singleBand : bands) {
            String technology = getTechnologyForBand(singleBand);
            if (technology != null && !builder.toString().contains(technology)) {
                builder.append(technology);
                builder.append(Symbol.UNDERSCORE_STRING);
            }
        }
        return builder.toString().substring(ReportConstants.INDEX_ZER0, builder.toString().length() - 1);
    }

    private static String getTechnologyForBand(String band) {
        Integer bandValue = null;
        if (band != null && !StringUtils.isBlank(band) && NumberUtils.isCreatable(band)) {
            bandValue = Integer.parseInt(band);
        }
        if (bandValue != null) {
            if (bandValue <= 31) {
                return "FDD";
            } else {
                return "TDD";
            }
        }
        return null;
    }

    private String convertSummaryDataToCSV(String summaryData) {
        summaryData = summaryData.replaceAll("[\\[\\]]", Symbol.EMPTY_STRING);
        return summaryData;
    }

    private List<SiteInformationWrapper> getSiteData(GenericWorkorder workorderObj, Map<String, Long> driveTimeStampMap,
                                                     List<String[]> csvDataArray, SSVTReportWrapper mainWrapper, Map<String, Integer> kpiIndexMap)
            throws IllegalAccessException {
        Map<String, Object> siteDataMap = null;
        List<SiteInformationWrapper> siteInfoList = null;
        siteInfoList = reportService.getSiteDataForSSVTReport(workorderObj);
        List<SectorSwapWrapper> sectorSwapInfo = SSVTReportUtils.getSectorSwapInfo(csvDataArray, siteDataMap,
                kpiIndexMap);
        if (sectorSwapInfo != null) {
            mainWrapper.setSectorSwapList(sectorSwapInfo);
            logger.info("Found the sector Swap Info Size {}", sectorSwapInfo.size());
        }
        return siteInfoList;
    }

    private Map<String, List<String[]>> processData(GenericWorkorder workorderObj, List<String> operatorList,
                                                    Map<String, List<WORecipeMapping>> driveTypeRecipeMap, Map<String, String> mobilityTestTypeMap,
                                                    List<String> fetchKPIList, Map<String, Integer> kpiIndexMap) {
        Map<String, List<String[]>> driveDataMap = new HashMap<>();
        List<String[]> combinedDataList = new ArrayList<>();
        Map<String, List<String>> driveTypemap = driveTypeRecipeMap.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().stream()
                        .collect(Collectors.mapping(data -> String.valueOf(data.getId()), Collectors.toList()))));

        if (driveTypeRecipeMap.containsKey(SSVTConstants.DRIVE_DATA_TYPE_STATIONARY)) {
            Map<String, String> gwoMeta = workorderObj.getGwoMeta();
            String recipePciMapJson = gwoMeta.get("recipePCIMap");
            List<String[]> stationaryDataList = new ArrayList<>();
            if (!StringUtils.isBlank(recipePciMapJson)) {
                Map<String, Double> recipePciMap = new Gson().fromJson(recipePciMapJson, HashMap.class);
                logger.info("recipe PCI map is: {}", new Gson().toJson(recipePciMap));
                List<String> recipeList = driveTypemap.get(SSVTConstants.DRIVE_DATA_TYPE_STATIONARY);
                logger.info("Recipe List is : {}", new Gson().toJson(recipeList));
                for (String recipe : recipeList) {
                    Double pci = recipePciMap.get(recipe);
                    List<String[]> stationaryData = reportService.getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workorderObj.getId()),
                            Arrays.asList(recipe), fetchKPIList, kpiIndexMap);
                    if (pci != null && Utils.isValidList(stationaryData)) {
                        driveDataMap.put(String.valueOf(pci.intValue()), stationaryData);
                        stationaryDataList.addAll(stationaryData);
                        combinedDataList.addAll(stationaryData);
                    }
                }
                driveDataMap.put(SSVTConstants.DRIVE_DATA_TYPE_STATIONARY, stationaryDataList);
            }
        }
        if (driveTypeRecipeMap.containsKey(SSVTConstants.DRIVE_DATA_TYPE_DRIVE)) {
            List<String[]> driveData = new ArrayList<>();
            for (Entry<String, String> testTypeEntry : mobilityTestTypeMap.entrySet()) {
                List<String[]> testTypeWiseData = reportService.getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workorderObj.getId()),
                        Arrays.asList(testTypeEntry.getValue()), fetchKPIList, kpiIndexMap);
                if (Utils.isValidList(testTypeWiseData)) {
                    driveDataMap.put(SSVTConstants.KEY_RECIPE_MOBILITY_PREFIX + testTypeEntry.getKey(),
                            testTypeWiseData);
                    driveData.addAll(testTypeWiseData);
                }
            }
            if (Utils.isValidList(driveData)) {
                driveDataMap.put(SSVTConstants.DRIVE_DATA_TYPE_DRIVE, driveData);
                combinedDataList.addAll(driveData);
            }
        }
        if (Utils.isValidList(combinedDataList)) {
            driveDataMap.put(SSVTConstants.DRIVE_DATA_TYPE_COMBINED, combinedDataList);
        }
        logger.info("CSV data map with keys: {}", new Gson().toJson(driveDataMap.keySet()));
        return driveDataMap;
    }

    private Integer getNeighbourData(String[] driveData, Integer indexNeighbourData) {
        Integer neighbourCount = null;
        try {
            if (driveData.length > indexNeighbourData) {
                String neighbours = driveData[indexNeighbourData];
                if (!StringUtils.isBlank(neighbours)) {
                    List<List<Object>> neighbourList = new Gson().fromJson(neighbours, ArrayList.class);
                    if (Utils.isValidList(neighbourList)) {
                        neighbourCount = neighbourList.size();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception in method getNeighbourData {} ", Utils.getStackTrace(e));
        }
        return neighbourCount;
    }

    /**
     * @param csvDataArray
     * @param siteInfoList
     * @param imageDataList
     * @param kpiList
     * @param driveRoute
     * @param band
     * @param kpiIndexMap
     * @return
     * @throws IOException
     * @throws BusinessException
     * @throws MalformedByteSequenceException
     * @throws ForbiddenException
     */
    private HashMap<String, String> getImageMapOfCombinedData(List<String[]> csvDataArray,
                                                              List<SiteInformationWrapper> siteInfoList, List<DriveDataWrapper> imageDataList, List<KPIWrapper> kpiList,
                                                              List<List<List<Double>>> driveRoute, String band, Map<String, Integer> kpiIndexMap) throws IOException {
        DriveImageWrapper driveImageWrapper = new DriveImageWrapper(csvDataArray,
                kpiIndexMap.get(ReportConstants.LATITUDE), kpiIndexMap.get(ReportConstants.LONGITUDE),
                kpiIndexMap.get(ReportConstants.PCI_PLOT), kpiList, siteInfoList, null, driveRoute, band);
        HashMap<String, String> images = getImagesForReport(kpiList, driveImageWrapper, imageDataList, kpiIndexMap);
        if (Utils.isValidList(imageDataList)) {
            images.putAll(getSectorWiseImageFromList(imageDataList));
        }
        return images;
    }

    private HashMap<String, String> getImagesForReport(List<KPIWrapper> kpiList, DriveImageWrapper driveImageWrapper,
                                                       List<DriveDataWrapper> imageDataList, Map<String, Integer> kpiIndexMap) throws IOException {
        List<Double[]> pinLatLonList = imageDataList.stream().map(
                driveDataWrapper -> new Double[]{driveDataWrapper.getLongitude(), driveDataWrapper.getLatitutde()})
                .collect(Collectors.toList());
//		kpiList = reportService.modifyIndexOfCustomKpis(kpiList);
        HashMap<String, BufferedImage> bufferdImageMap = mapImageService.getLegendImages(kpiList,
                driveImageWrapper.getDataKPIs());
        bufferdImageMap.putAll(mapImageService.getDriveImagesForReport(driveImageWrapper, pinLatLonList, kpiIndexMap));
        String saveImagePath = (ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT) + ReportConstants.SSVT
                + ReportConstants.FORWARD_SLASH
                + ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MM_YY_HH_SS)
                + ReportConstants.FORWARD_SLASH).toString();
        HashMap<String, String> imagemap = mapImageService.saveDriveImages(bufferdImageMap, saveImagePath, false);
        imagemap.put(ForesightConstants.TO_DELETED_KEY, saveImagePath);
        logger.info("Returning images Map with Data: {}", new Gson().toJson(imagemap));
        return imagemap;
    }

    /**
     * Prepare image map.
     *
     * @param imagemap    the imagemap
     * @param kpiIndexMap
     * @return the hash map
     */
    private HashMap<String, Object> prepareImageMap(HashMap<String, String> imagemap,
                                                    Map<String, Integer> kpiIndexMap) {
        logger.info("inside the method prepareImageMap");
        Map<String, Object> map = new HashMap<>();
        try {
            //		logger.info("Going to get KPI Map plot from Image Map: {}", new Gson().toJson(imagemap));
            map.put(ReportConstants.IMAGE_RSRP,
                    imagemap.get(kpiIndexMap.get(ReportConstants.RSRP) + Symbol.EMPTY_STRING));
            map.put(ReportConstants.IMAGE_RSRP_LEGEND, imagemap
                    .get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.RSRP)));
            map.put(ReportConstants.IMAGE_RSRQ,
                    imagemap.get(kpiIndexMap.get(ReportConstants.RSRQ) + Symbol.EMPTY_STRING));
            map.put(ReportConstants.IMAGE_RSRQ_LEGEND, imagemap
                    .get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.RSRQ)));
            map.put(ReportConstants.IMAGE_SINR,
                    imagemap.get(kpiIndexMap.get(ReportConstants.SINR) + Symbol.EMPTY_STRING));
            map.put(ReportConstants.IMAGE_SINR_LEGEND, imagemap
                    .get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.SINR)));
            map.put(ReportConstants.IMAGE_DL,
                    imagemap.get(kpiIndexMap.get(ReportConstants.MAC_DL_THROUGHPUT) + Symbol.EMPTY_STRING));
            map.put(ReportConstants.IMAGE_DL_LEGEND, imagemap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE
                    + kpiIndexMap.get(ReportConstants.MAC_DL_THROUGHPUT)));

            map.put(ReportConstants.IMAGE_UL,
                    imagemap.get(kpiIndexMap.get(ReportConstants.MAC_UL_THROUGHPUT) + Symbol.EMPTY_STRING));
            map.put(ReportConstants.IMAGE_UL_LEGEND, imagemap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE
                    + kpiIndexMap.get(ReportConstants.MAC_UL_THROUGHPUT)));
            map.put(ReportConstants.IMAGE_PCI, imagemap.get(kpiIndexMap.get(ReportConstants.PCI_PLOT) + ""));
            map.put(ReportConstants.IMAGE_SITE, imagemap.get(ReportConstants.SITE_IMAGE));
            map.put(ReportConstants.IMAGE_PCI_LEGEND, imagemap.get(ReportConstants.PCI_LEGEND));
            map.put(ReportConstants.JUSTFICATION_IMG, imagemap.get(ReportConstants.SATELLITE_VIEW3));
            map.put(ReportConstants.HANDOVER_PLOT,
                    imagemap.get(kpiIndexMap.get(ReportConstants.HANDOVER_PLOT) + Symbol.EMPTY_STRING));
            map.put(ReportConstants.CALL_PLOT,
                    imagemap.get(kpiIndexMap.get(ReportConstants.CALL_PLOT) + Symbol.EMPTY_STRING));

            map.put(ForesightConstants.TO_DELETED_KEY, imagemap.get(ForesightConstants.TO_DELETED_KEY));
        } catch (Exception e) {
            logger.error("Error inside the method prepareImageMap{}", e.getMessage());
        }
        logger.info("image map is {}", new Gson().toJson(map));

        return (HashMap<String, Object>) map;
    }

    private Map<String, String> getSectorWiseImageFromList(List<DriveDataWrapper> imageDataList) {
        Map<String, String> sectorImageMap = new HashMap<>();
        logger.info("inside method getSectorWiseImageFromList {} ", imageDataList.size());
        Integer i = 1;
        for (DriveDataWrapper driveData : imageDataList) {
            try {
                InputStream in = new ByteArrayInputStream(driveData.getImg());
                BufferedImage buffredImage = ImageIO.read(in);
                ImageIO.write(buffredImage, ReportConstants.JPG,
                        new File(ConfigUtils.getString(ReportConstants.SSVT_REPORT_PATH) + ReportConstants.SECTOR + i
                                + ReportConstants.DOT_JPG));
                sectorImageMap.put(ReportConstants.SECTOR + i, ConfigUtils.getString(ReportConstants.SSVT_REPORT_PATH)
                        + ReportConstants.SECTOR + i + ReportConstants.DOT_JPG);
                i++;
            } catch (Exception e) {
                logger.warn("Inside method getSectorWiseImageFromList {}", e.getMessage());
            }
        }
        logger.debug("sectorImageMap data {}", sectorImageMap);
        return sectorImageMap;
    }

    private void setHandoverDataToMainWrapper(SSVTReportWrapper mainWrapper, HashMap<String, Object> imagesMap,
                                              List<String[]> csvDataArray, Set<String> pciDataSet) {
        HandoverDataWrapper hoDataWrapper = new HandoverDataWrapper();
        if (SSVTReportUtils.validateMap(imagesMap, String.valueOf(ReportConstants.HANDOVER_PLOT))) {
            hoDataWrapper.setHandoverPlot((String) imagesMap.get(String.valueOf(ReportConstants.HANDOVER_PLOT)));
            hoDataWrapper.setHoInitateLegend(SSVTReportUtils.getLegendColorImage(Color.BLUE));
            hoDataWrapper.setHoSuccessLegend(SSVTReportUtils.getLegendColorImage(Color.GREEN));
            hoDataWrapper.setHoFailureLegend(SSVTReportUtils.getLegendColorImage(Color.RED));
        }
        setHandoverDetailDataToHOWrapper(hoDataWrapper, csvDataArray, pciDataSet);
        //		logger.info("HO data list size: {}", hoDataWrapper.getHoAnalysisList().size());
        mainWrapper.setHandoverDataList(Arrays.asList(hoDataWrapper));
    }

    private void setHandoverDetailDataToHOWrapper(HandoverDataWrapper hoDataWrapper, List<String[]> csvDataArray,
                                                  Set<String> pciDataSet) {
//		logger.info("Inside method setHandoverDetailDataToHOWrapper, hoDataWrapper: {} , pciDataList: {}",
//				hoDataWrapper, pciDataSet);
        List<HOItemWrapper> hoDataList = new ArrayList<>();

        Integer hoInitiated = 0;
        Integer hoSucceeded = 0;
        Integer hoFailed = 0;
        if (Utils.isValidList(csvDataArray)) {
            for (String[] csvData : csvDataArray) {
                if (csvData != null && csvData.length > SSVTConstants.INDEX_HO_DATA_STATUS) {
                    HOItemWrapper itemWrapper = new HOItemWrapper();
                    itemWrapper.setTimestamp(ReportUtil.getFormattedDate(
                            new Date(Long.parseLong(csvData[SSVTConstants.INDEX_HO_DATA_TIMESTAMP])),
                            ReportConstants.DATE_FORMAT_DD_MM_YY_SS_AA));
                    itemWrapper.setLatitude(csvData[SSVTConstants.INDEX_HO_DATA_LATITUDE]);
                    itemWrapper.setLongitude(csvData[SSVTConstants.INDEX_HO_DATA_LONGITUDE]);
                    itemWrapper.setInitRsrp(csvData[SSVTConstants.INDEX_HO_DATA_INIT_RSRP]);
                    itemWrapper.setSourcePci(csvData[SSVTConstants.INDEX_HO_DATA_SOURCE_PCI]);
                    itemWrapper.setTargetPCI(csvData[SSVTConstants.INDEX_HO_DATA_TARGET_PCI]);
                    itemWrapper.setCompleteRsrp(csvData[SSVTConstants.INDEX_HO_DATA_COMPLETE_RSRP]);
                    itemWrapper.setInterruptionTime(csvData[SSVTConstants.INDEX_HO_DATA_INTERRUPTION_TIME]);
                    hoInitiated++;
                    if (!StringUtils.isBlank(csvData[SSVTConstants.INDEX_HO_DATA_STATUS])
                            && SSVTConstants.TEST_STATUS_PASS
                            .equalsIgnoreCase(csvData[SSVTConstants.INDEX_HO_DATA_STATUS])) {
                        itemWrapper.setTestStatus(SSVTConstants.TEST_STATUS_PASS);
                        hoSucceeded++;
                    } else {
                        itemWrapper.setTestStatus(SSVTConstants.TEST_STATUS_FAIL);
                        hoFailed++;
                    }
                    hoDataList.add(itemWrapper);
                }
            }
            hoDataWrapper.setHoInitate(String.valueOf(hoInitiated));
            hoDataWrapper.setHoSuccess(String.valueOf(hoSucceeded));
            hoDataWrapper.setHoFailure(String.valueOf(hoFailed));
            hoDataWrapper.setHoSuccessRate(String.valueOf(ReportUtil
                    .round(ReportUtil.getPercentage(hoSucceeded, hoInitiated), ReportConstants.TWO_DECIMAL_PLACES)));
            hoDataWrapper.setHoFailureRate(String.valueOf(ReportUtil
                    .round(ReportUtil.getPercentage(hoFailed, hoInitiated), ReportConstants.TWO_DECIMAL_PLACES)));
            hoDataWrapper.setHoAnalysisList(hoDataList);
            setHOCriteriaDataToHOWrapper(hoDataWrapper, pciDataSet, hoDataList);
        }
    }

    private void setHOCriteriaDataToHOWrapper(HandoverDataWrapper hoDataWrapper, Set<String> pciDataSet,
                                              List<HOItemWrapper> hoDataList) {
        List<HOItemWrapper> hoCriteriaList = new ArrayList<>();
        for (String requiredPair : pciDataSet) {
            HOItemWrapper criteriaWrapper = new HOItemWrapper();
            criteriaWrapper.setSourcePci(requiredPair);
            String testStatus = SSVTConstants.TEST_STATUS_NOT_OBSERVED;
            for (HOItemWrapper itemWrapper : hoDataList) {
                String currentPair = itemWrapper.getSourcePci() + Symbol.SLASH_FORWARD_STRING
                        + itemWrapper.getTargetPCI();
                if (requiredPair.equalsIgnoreCase(currentPair)) {
                    testStatus = SSVTConstants.TEST_STATUS_OBSERVED;
                }
            }
            criteriaWrapper.setTestStatus(testStatus);
            hoCriteriaList.add(criteriaWrapper);
        }
        hoDataWrapper.setHoCriteriaList(hoCriteriaList);
    }

    private void setPlotImagesToParamMap(Map<String, Object> paramMap, HashMap<String, Object> imagesMap,
                                         boolean iSiteAcceptance) {
        if (SSVTReportUtils.validateMap(imagesMap, ReportConstants.IMAGE_PCI)) {
            paramMap.put(SSVTConstants.PARAM_KEY_PCI_PLOT, imagesMap.get(ReportConstants.IMAGE_PCI));
            paramMap.put(SSVTConstants.PARAM_KEY_PCI_LEGEND, imagesMap.get(ReportConstants.IMAGE_PCI_LEGEND));
        }
        if (!iSiteAcceptance) {
            paramMap.put(SSVTConstants.PARAM_KEY_SECTOR_1_IMAGE,
                    imagesMap.get(ReportConstants.SECTOR + ForesightConstants.ONE));
            paramMap.put(SSVTConstants.PARAM_KEY_SECTOR_2_IMAGE,
                    imagesMap.get(ReportConstants.SECTOR + ForesightConstants.TWO));
            paramMap.put(SSVTConstants.PARAM_KEY_SECTOR_3_IMAGE,
                    imagesMap.get(ReportConstants.SECTOR + ForesightConstants.THREE));
            paramMap.put(SSVTConstants.PARAM_KEY_JUSTIFICATION_IMAGE, imagesMap.get(ReportConstants.JUSTFICATION_IMG));
        }
    }

    private void setCallDataToMainWrapper(SSVTReportWrapper mainWrapper, HashMap<String, Object> imagesMap,
                                          List<String[]> csvDataArray, List<SiteInformationWrapper> filteredSiteInfo,
                                          Map<String, Integer> kpiIndexMap) {
        CallAnalysisDataWrapper callDataWrapper = new CallAnalysisDataWrapper();
        if (SSVTReportUtils.validateMap(imagesMap, ReportConstants.CALL_PLOT)) {
            callDataWrapper.setCallPlot((String) imagesMap.get(ReportConstants.CALL_PLOT));
            callDataWrapper.setInitiateLegend(SSVTReportUtils.getLegendColorImage(Color.BLUE));
            callDataWrapper.setDroppedLegend(SSVTReportUtils.getLegendColorImage(Color.RED));
            callDataWrapper.setFailedLegend(SSVTReportUtils.getLegendColorImage(Color.ORANGE));
            callDataWrapper.setSuccessLegend(SSVTReportUtils.getLegendColorImage(Color.green));
        } else {
            return;
        }

        callDataWrapper
                .setCallInitiated(getKpiSumFromCsvData(kpiIndexMap.get(ReportConstants.CALL_INITIATE), csvDataArray));
        callDataWrapper.setCallDropped(getKpiSumFromCsvData(kpiIndexMap.get(ReportConstants.CALL_DROP), csvDataArray));
        callDataWrapper
                .setCallSuccess(getKpiSumFromCsvData(kpiIndexMap.get(ReportConstants.CALL_SUCCESS), csvDataArray));
        callDataWrapper
                .setCallFailed(getKpiSumFromCsvData(kpiIndexMap.get(ReportConstants.CALL_FAILURE), csvDataArray));

//		Map<Integer, List<String[]>> pciWiseCsvData = getPciWiseCsvData(csvDataArray, filteredSiteInfo,
//				kpiIndexMap.get(ReportConstants.PCI_PLOT));
//		Map<Integer, Integer> pciSectorMap = getSectorPciMap(filteredSiteInfo);
//		logger.info("pci Sector map: {}", pciSectorMap);
        List<String> kpiList = Arrays.asList(SSVTConstants.CALL_ANALYSIS_KPI_ARRAY);
        List<CallAnalysisItemWrapper> callDataList = new ArrayList<>();
        if (csvDataArray != null && !csvDataArray.isEmpty()) {
            for (String kpiName : kpiList) {
                CallAnalysisItemWrapper callItemWrapper = new CallAnalysisItemWrapper();
                callItemWrapper.setKpiName(kpiName);
                callItemWrapper.setTarget(getTargetForKpiName(kpiName));
//				for (Entry<Integer, List<String[]>> pciDataEntry : pciWiseCsvData.entrySet()) {
                setSectorWiseCallAnalysisData(csvDataArray, ForesightConstants.ONE,
                        kpiName, callItemWrapper, kpiIndexMap);
//				}
//				if (SSVTConstants.CALL_KPI_DROPPED_PERCENT.equalsIgnoreCase(kpiName)) {
//					setTargetStatusForKpiName(callItemWrapper, false, 0.0);
//				} else if (SSVTConstants.CALL_KPI_SUCCESS_PERCENT.equalsIgnoreCase(kpiName)) {
//					setTargetStatusForKpiName(callItemWrapper, true, 90.0);
//				} else if (SSVTConstants.CALL_KPI_MOS.equalsIgnoreCase(kpiName)) {
//					setTargetStatusForKpiName(callItemWrapper, true, 3.5);
//				}
                callDataList.add(callItemWrapper);
            }
        }
        callDataWrapper.setCallDataList(callDataList);
        mainWrapper.setCallAnalysisList(Arrays.asList(callDataWrapper));
    }

    private Map<Integer, List<String[]>> getPciWiseCsvData(List<String[]> csvDataArray,
                                                           List<SiteInformationWrapper> filteredSiteInfo, Integer pciIndex) {
        if (pciIndex != null) {
            Set<Integer> filteredPciList = filteredSiteInfo.stream()
                    .filter(data -> data != null && data.getPci() != null).map(SiteInformationWrapper::getPci)
                    .collect(Collectors.toSet());
            return csvDataArray.stream()
                    .filter(data -> data != null && data.length > pciIndex && NumberUtils.isCreatable(data[pciIndex])
                            && filteredPciList.contains(Integer.parseInt(data[pciIndex])))
                    .collect(Collectors.groupingBy(data -> Integer.parseInt(data[pciIndex])));
        }
        return Collections.emptyMap();
    }

    private Map<Integer, Integer> getSectorPciMap(List<SiteInformationWrapper> filteredSiteInfo) {
        Map<Integer, Integer> pciSectorMap = new HashMap<>();
        if (Utils.isValidList(filteredSiteInfo)) {
            for (SiteInformationWrapper siteInfo : filteredSiteInfo) {
                pciSectorMap.put(siteInfo.getPci(), siteInfo.getSector());
            }
        }
        return pciSectorMap;
    }

    private void setSectorWiseCallAnalysisData(List<String[]> csvDataList, Integer sector, String kpiName,
                                               CallAnalysisItemWrapper callItemWrapper, Map<String, Integer> kpiIndexMap) {
        switch (sector) {
            case ReportConstants.INDEX_ONE:
                callItemWrapper.setSector1Value(getkpiDataForCallAnalysis(kpiName, csvDataList, kpiIndexMap)[0]);
                break;
//		case ReportConstants.INDEX_TWO:
//			callItemWrapper.setSector2Value(getkpiDataForCallAnalysis(kpiName, csvDataList, kpiIndexMap)[0]);
//			break;
//		case ReportConstants.INDEX_THREE:
//			callItemWrapper.setSector3Value(getkpiDataForCallAnalysis(kpiName, csvDataList, kpiIndexMap)[0]);
//			break;
            default:
                break;
        }
    }

    private String[] getkpiDataForCallAnalysis(String kpiName, List<String[]> csvData,
                                               Map<String, Integer> kpiIndexMap) {
        String[] kpiDataArray = new String[2];
        switch (kpiName) {
//		case SSVTConstants.CALL_KPI_ATTEMPTS:
//			kpiDataArray[0] = getKpiCountFromCsvData(kpiIndexMap.get(ReportConstants.CALL_INITIATE), csvData);
//			return kpiDataArray;
//		case SSVTConstants.CALL_KPI_FAILED_CALLS:
//			kpiDataArray[0] = getKpiCountFromCsvData(kpiIndexMap.get(ReportConstants.CALL_FAILURE), csvData);
//			return kpiDataArray;
//		case SSVTConstants.CALL_KPI_DROPPED_CALLS:
//			kpiDataArray[0] = getKpiCountFromCsvData(kpiIndexMap.get(ReportConstants.CALL_DROP), csvData);
//			return kpiDataArray;
//		case SSVTConstants.CALL_KPI_DROPPED_PERCENT:
//			kpiDataArray[0] = getKpiPercentageFromCsvData(kpiIndexMap.get(ReportConstants.CALL_DROP),
//					kpiIndexMap.get(ReportConstants.CALL_INITIATE), csvData);
//			return kpiDataArray;
//		case SSVTConstants.CALL_KPI_SUCCESS_PERCENT:
//			kpiDataArray[0] = getKpiPercentageFromCsvData(kpiIndexMap.get(ReportConstants.CALL_SUCCESS),
//					kpiIndexMap.get(ReportConstants.CALL_INITIATE), csvData);
//			return kpiDataArray;
            case SSVTConstants.CALL_KPI_CST:
                kpiDataArray[0] = getKpiAverageFromCsvData(kpiIndexMap.get(ReportConstants.CALL_SETUP_TIME), csvData);
                return kpiDataArray;
            case SSVTConstants.CALL_KPI_MOS:
                kpiDataArray[0] = getKpiAverageFromCsvData(kpiIndexMap.get(ReportConstants.MOS), csvData);
                return kpiDataArray;
            default:
                return kpiDataArray;
        }
    }

    private String getKpiCountFromCsvData(Integer index, List<String[]> csvDataList) {
        Integer count = 0;
        if (index != null && Utils.isValidList(csvDataList)) {
            for (String[] csvData : csvDataList) {
                if (csvData != null && csvData.length > index && NumberUtils.isCreatable(csvData[index])
                        && Integer.parseInt(csvData[index]) != 0) {
                    count++;
                }
            }
        }
        return count != null ? String.valueOf(count) : null;
    }

    private String getKpiSumFromCsvData(Integer index, List<String[]> csvDataList) {
        Integer count = 0;
        if (index != null && Utils.isValidList(csvDataList)) {
            for (String[] csvData : csvDataList) {
                if (csvData != null && csvData.length > index && NumberUtils.isCreatable(csvData[index].trim())) {
                    count = count + Integer.parseInt(csvData[index]);
                }
            }
        }
        return count.toString();
    }

    private String getKpiPercentageFromCsvData(Integer resultIndex, Integer totalIndex, List<String[]> csvDataList) {
        String result = getKpiCountFromCsvData(resultIndex, csvDataList);
        String total = getKpiCountFromCsvData(totalIndex, csvDataList);
        if (NumberUtils.isCreatable(result) && NumberUtils.isCreatable(total)) {
            return String.valueOf(
                    ReportUtil.round(ReportUtil.getPercentage(Double.parseDouble(result), Double.parseDouble(total)),
                            ReportConstants.TWO_DECIMAL_PLACES));
        }
        return null;
    }

    private String getKpiAverageFromCsvData(Integer index, List<String[]> csvDataList) {
        List<Double> kpiDataList = ReportUtil.convetArrayToList(csvDataList, index);
        if (Utils.isValidList(kpiDataList)) {
            OptionalDouble kpiAverage = kpiDataList.stream().filter(x -> x != null).mapToDouble(x -> x).average();
            return kpiAverage != null && kpiAverage.isPresent()
                    ? String.valueOf(ReportUtil.round(kpiAverage.getAsDouble(), ReportConstants.TWO_DECIMAL_PLACES))
                    : null;
        }
        return null;
    }

    private String getTargetForKpiName(String kpiName) {
        switch (kpiName) {
            case SSVTConstants.CALL_KPI_DROPPED_PERCENT:
                return "<=5 %";
            case SSVTConstants.CALL_KPI_SUCCESS_PERCENT:
                return ">=90 %";
            case SSVTConstants.CALL_KPI_MOS:
                return "3.5";
            default:
                return null;
        }
    }

    private void setTargetStatusForKpiName(CallAnalysisItemWrapper callItemWrapper, Boolean isGreaterThanOperator,
                                           Double target) {
        String sector1Result = callItemWrapper.getSector1Value();
        String sector2Result = callItemWrapper.getSector2Value();
        String sector3Result = callItemWrapper.getSector3Value();
        Double sector1Data = null;
        Double sector2Data = null;
        Double sector3Data = null;
        if (NumberUtils.isCreatable(sector1Result) && NumberUtils.isCreatable(sector2Result)
                && NumberUtils.isCreatable(sector3Result) && target != null) {
            sector1Data = Double.parseDouble(sector1Result);
            sector2Data = Double.parseDouble(sector2Result);
            sector3Data = Double.parseDouble(sector3Result);
        }
        if (sector1Data != null && sector2Data != null && sector3Data != null) {
            if (isGreaterThanOperator) {
                callItemWrapper.setStatus(sector1Data >= target && sector2Data >= target && sector3Data >= target
                        ? SSVTConstants.TEST_STATUS_PASS
                        : SSVTConstants.TEST_STATUS_FAIL);
            } else {
                callItemWrapper.setStatus(sector1Data <= target && sector2Data <= target && sector3Data <= target
                        ? SSVTConstants.TEST_STATUS_PASS
                        : SSVTConstants.TEST_STATUS_FAIL);
            }
        } else {
            callItemWrapper.setStatus(SSVTConstants.TEST_STATUS_FAIL);
        }
    }

    private List<String[]> getHandoverDataFromHbase(Integer workorderId, List<String> recipeList,
                                                    List<String> operatorList) throws IOException {
        logger.info(
                "Inside Method getHandoverDataFromHbase with data=> workorderid: {}, recipeList: {}, operatorList: {}",
                workorderId, recipeList, operatorList);
        if (workorderId != null && Utils.isValidList(recipeList) && Utils.isValidList(operatorList)) {
            Set<String> operatorSet = new HashSet<>(operatorList);
            String table = ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE);
            List<Get> getList = SSVTReportUtils.getHandoverQueryList(workorderId, recipeList,
                    new ArrayList<>(operatorSet));
            List<HBaseResult> resultList = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
//			logger.info("resultList ==={}",resultList);
            return SSVTReportUtils.getHandoverDataListFromHBaseResult(resultList);
        }
        return new ArrayList<>();
    }

    @Override
//    @Async("reportTaskExecutor")
    public GWOMeta getRecipeWiseSSVTSummary(Integer recipeId) {

        try {
        	
			boolean qmdlResponse = reportService.getFileDetailByRecipeMappingId(recipeId);
        	logger.info("inside method getRecipeWiseSSVTSummary for recipe id : {}", recipeId);
        	
        	if (true) {
				WORecipeMapping recipeMapping = woRecipeMappingDao.findByPk(recipeId);
	        	logger.info("recipeMapping: {}", recipeMapping);

				GenericWorkorder workorderObj = recipeMapping.getGenericWorkorder();
				
				Set<String> dynamicKpis = reportService.getDynamicKpiName(Arrays.asList(workorderObj.getId()), null,
						Layer3PPEConstant.ADVANCE);
				List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs().stream()
						.filter(k -> dynamicKpis.contains(k)).collect(Collectors.toList());
				Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
				
				Map<String, List<WORecipeMapping>> driveWiseRecipeMap = SSVTReportUtils
						.getDriveTypeWiseRecipeMapping(Arrays.asList(recipeMapping));
				
				Map<String, String> mobilityTestTypeMap = getMobilityTestTypeMap(driveWiseRecipeMap);
				logger.info("mobilityTestTypeMap {}", mobilityTestTypeMap);
				
				Map<String, List<String>> driveRecipeDetailMap = nvLayer3DashboardService
						.getDriveRecipeDetailByRecipeId(recipeId);
				
				logger.info("driveRecipeDetailMap {}", driveRecipeDetailMap);
				
				if (SSVTReportUtils.validateMap(driveRecipeDetailMap, QMDLConstant.RECIPE)) {

					Map<String, List<String[]>> csvDataMap = processData(workorderObj,
							driveRecipeDetailMap.get(QMDLConstant.OPERATOR), driveWiseRecipeMap, mobilityTestTypeMap,
							fetchKPIList, kpiIndexMap);

					List<SiteInformationWrapper> siteData = reportService.getSiteDataForWorkorder(workorderObj);

					String band = findBandDetailByGWOMetaData(workorderObj);
					List<SiteInformationWrapper> filteredSiteInfo = filterNEDataByBand(siteData, band);

					boolean iSiteAcceptance = false;
					if (SSVTReportUtils.validateMap(workorderObj.getGwoMeta(), SSVTConstants.IS_SITE_ACCEPTANCE)) {
						iSiteAcceptance = Boolean
								.parseBoolean(workorderObj.getGwoMeta().get(SSVTConstants.IS_SITE_ACCEPTANCE));
					}

					if (!filteredSiteInfo.isEmpty()) {
						return prepareGWOMetaEntry(filteredSiteInfo, csvDataMap, iSiteAcceptance,
								kpiIndexMap, workorderObj, recipeId);
					}

				} 
			}

        } catch (Exception e) {
            logger.error("unable to generate GWOMeta Entry: {}", Utils.getStackTrace(e));
        }
        return null;
    }

    private GWOMeta prepareGWOMetaEntry(List<SiteInformationWrapper> filteredSiteInfo,
    					Map<String, List<String[]>> csvDataMap, boolean iSiteAcceptance, Map<String, Integer> kpiIndexMap,
    					GenericWorkorder workorderObj, Integer recipeId) {

        Map<Integer, List<SectorWiseWrapper>> genericMap = new HashMap<>();
        int sectorValue = 1;
        
        List<SectorWiseWrapper> sectorWiseWrappers = null;
        logger.info("iSiteAcceptance: {}", iSiteAcceptance);
        
        if (SSVTReportUtils.validateMap(csvDataMap, SSVTConstants.DRIVE_DATA_TYPE_STATIONARY)) {
        	logger.info("inside filtering filteredSiteInfo");
        	Map<String, String> gwoMeta = workorderObj.getGwoMeta();
        	String recipePciMapJson = gwoMeta.get("recipePCIMap");
        	Map<String, Double> recipePciMap = new Gson().fromJson(recipePciMapJson, HashMap.class);
        	Double pci = recipePciMap.get(recipeId.toString());
        	filteredSiteInfo = filteredSiteInfo.stream().filter(f -> f.getPci() == pci.intValue()).collect(Collectors.toList());
        }

        for(SiteInformationWrapper siteInfo: filteredSiteInfo) {
            prepareKPIResponse(siteInfo.getPci(), sectorValue, csvDataMap, genericMap, kpiIndexMap);
            if (SSVTReportUtils.validateMap(csvDataMap, SSVTConstants.DRIVE_DATA_TYPE_DRIVE)) {
                sectorWiseWrappers = calculateLogicalSection(genericMap.get(sectorValue), siteInfo.getBandwidth(), true, iSiteAcceptance, false);
            } else {
                sectorWiseWrappers = calculateLogicalSection(genericMap.get(sectorValue), siteInfo.getBandwidth(), false, iSiteAcceptance, false);
            }
            if (!iSiteAcceptance) {
            	for(SectorWiseWrapper sectorWiseWrapper : sectorWiseWrappers) {
            		sectorWiseWrapper.setPci(siteInfo.getPci().toString());
            		updateHandoverStatus(sectorWiseWrapper, csvDataMap, kpiIndexMap);
            	}
            }
            genericMap.put(sectorValue, sectorWiseWrappers);
            sectorValue++;
        }
        
        JSONObject jsonObject = new JSONObject(genericMap);
        jsonObject.put("recipeId", recipeId);

        GWOMeta gwoMetaResponse = igwoMetaDao.getGwoMetaDataByGenericWorkorderIdforReport(workorderObj.getId(), recipeId+"_recipeSummary");
        
        gwoMetaResponse = workorderService.prepareGWOMetaData(gwoMetaResponse, jsonObject, workorderObj, recipeId);
        
        logger.info("final reponse : {}",jsonObject.toString());
        return gwoMetaResponse;
    }

    private void updateHandoverStatus(SectorWiseWrapper sectorWiseWrapper, Map<String, List<String[]>> csvDataMap, Map<String, Integer> kpiIndexMap) {
        //	logger.info("inside the method csvDataMap setMobilityDataToWrapper {} kpiIndexMap {}",csvDataMap,kpiIndexMap);
        Integer hoInitiate = 0;
        Integer hoSuccess = 0;
        Integer indexHOInitiate = kpiIndexMap.get(ReportConstants.HANDOVER_INITIATE);
        Integer indexHOSuccess = kpiIndexMap.get(ReportConstants.HANDOVER_SUCCESS);
        Integer indexHOInterruptionTime = kpiIndexMap.get(ReportConstants.HO_INTERRUPTION_TIME);
        if (SSVTReportUtils.validateMap(csvDataMap, SSVTConstants.DRIVE_DATA_TYPE_DRIVE)) {
            logger.info("inside valid Drive condition ");
            for (String[] row : csvDataMap.get(SSVTConstants.DRIVE_DATA_TYPE_DRIVE)) {
                if (indexHOInitiate != null && SSVTReportUtils.checkValidation(indexHOInitiate, row)
                        && NumberUtils.isCreatable(row[indexHOInitiate])
                        && Integer.parseInt(row[indexHOInitiate]) != 0) {
                    hoInitiate += Integer.parseInt(row[indexHOInitiate]);
                }
                if (indexHOSuccess != null && SSVTReportUtils.checkValidation(indexHOSuccess, row)
                        && NumberUtils.isCreatable(row[indexHOSuccess]) && Integer.parseInt(row[indexHOSuccess]) != 0) {
                    hoSuccess += Integer.parseInt(row[indexHOSuccess]);
                }
            }
            
            sectorWiseWrapper.setHoInitiateCount(hoInitiate.doubleValue());
            sectorWiseWrapper.setHoSuccessCount(hoSuccess.doubleValue());

            
            String hoStatus = SSVTReportUtils.getHOSuccessStatus(hoInitiate, hoSuccess);
            if(hoStatus.contains(SSVTConstants.TEST_STATUS_FAIL)){
            	sectorWiseWrapper.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
            }

            if (indexHOInterruptionTime != null) {
                List<Double> hoInterruptionList = ReportUtil.convetArrayToList(
                        csvDataMap.get(SSVTConstants.DRIVE_DATA_TYPE_DRIVE), indexHOInterruptionTime);
                if(!hoInterruptionList.isEmpty()) {
                	sectorWiseWrapper.setHoInterruptionTimeSum(hoInterruptionList.stream().filter(i -> i != null).mapToDouble(i -> i).sum());
                	sectorWiseWrapper.setHoInterruptionTimeCount(Double.valueOf(hoInterruptionList.stream().filter(i -> i != null).mapToDouble(i -> i).count()));
                }
                String hoInterruptionTime = SSVTReportUtils.getHOInterruptionTimeInCriteria(hoInterruptionList);
                if(hoInterruptionTime.contains(SSVTConstants.TEST_STATUS_FAIL)){
                	sectorWiseWrapper.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
                }
                sectorWiseWrapper.setHoInterruptionTime(hoInterruptionTime);
            }
        }

    }

    private void prepareKPIResponse(Integer pci, int sectorValue, Map<String, List<String[]>> csvDataMap, Map<Integer, List<SectorWiseWrapper>> genericMap, Map<String, Integer> kpiIndexMap) {

        Integer indexPci = kpiIndexMap.get(ReportConstants.PCI_PLOT);
        if (indexPci != null) {
            List<SectorWiseWrapper> genericList = new ArrayList<>();

            if (pci != null) {
                if (SSVTReportUtils.validateMap(csvDataMap, String.valueOf(pci))) {
                    for (String[] record : csvDataMap.get(String.valueOf(pci))) {
                        if (SSVTReportUtils.checkValidation(indexPci, record) && pci == Integer.parseInt(record[indexPci])) {
                            populateKPIResponse(record, genericList, false,  kpiIndexMap);
                        }
                    }
                }
            }
            if (SSVTReportUtils.validateMap(csvDataMap, SSVTConstants.DRIVE_DATA_TYPE_DRIVE)) {
                for (String[] record : csvDataMap.get(SSVTConstants.DRIVE_DATA_TYPE_DRIVE)) {
                    if (SSVTReportUtils.checkValidation(indexPci, record)
                            && pci == Integer.parseInt(record[indexPci])) {
                        populateKPIResponse(record, genericList, true, kpiIndexMap);
                    }
                }
            }

            genericMap.put(sectorValue, genericList);
        }

    }
    
}
