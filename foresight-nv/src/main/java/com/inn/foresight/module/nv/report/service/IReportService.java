package com.inn.foresight.module.nv.report.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.springframework.transaction.annotation.Transactional;

import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.CustomerComplaintSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.MessageDetailWrapper;
import com.inn.foresight.module.nv.report.wrapper.PSDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.RecipeMappingWrapper;
import com.inn.foresight.module.nv.report.wrapper.RemarkReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.ReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.QuickTestWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.YoutubeTestWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper;

public interface IReportService {

	List<KPIWrapper> setKpiStatesIntokpiList(List<KPIWrapper> kpiList, Integer workOrderId, List<String> recepiList,
			List<String> operatorList);

	String getDriveData(Integer workorderId, Map<String, List<String>> map);

	List<KPISummaryDataWrapper> getKPISummaryDataForWorkOrderId(List<Integer> workOrderIds, List<String[]> dataKPIs,
			List<KPIWrapper> listOfKPidata, String[] summaryData, String reportType, String operator);

	String getDriveData(Integer workOrderId, List<String> recipeList, List<String> asList);

	String getSummaryData(Integer workorderId, Map<String, List<String>> map);

	String getSummaryData(Integer workOrderId, List<String> recipeList, List<String> list);

/** HashMap<String, BufferedImage> getLegendImages(List<KPIWrapper> list);. 
 * @param summaryKpiIndexMap */

	Map<String, Object> getJsonDataMap(String json);

	/** Boolean getStatusOfQmdlFileProcessing(Integer previousWoId, Integer currentWoId);. */

	Response saveFile(Integer analyticsrepoId, String filePath, File file);

	List<KPIWrapper> getKPiStatsDataList(Integer workorderId, Map<String, List<String>> map, Map<String, Integer> kpiIndexMap, 
			String legendAppliedto);

	HashMap<String, BufferedImage> getLegendImages(List<KPIWrapper> list, List<String[]> dataKPIs,
			Integer testTypeIndex);
	
    List<SiteInformationWrapper> getSiteDataByDataList(List<String[]> arlist);
	
	List<SiteInformationWrapper> getSiteDataForReportByDataList(List<String[]> arlist, Map<String, Integer> kpiIndexMap);

	List<YoutubeTestWrapper> getYouTubeData(Integer workorderId, Map<String, List<String>> map);

	List<YoutubeTestWrapper> getYouTubeDataByRecipeWise(Integer workorderId, String recipe, String operator);

	List<QuickTestWrapper> getQuickTestDataRecipeWise(Integer workorderId, String recipe, String operator);

	List<YoutubeTestWrapper> getYouTubeDataByRecipeForListOfOperators(Integer workorderId, String recipe,
			List<String> operatorList);

	List<QuickTestWrapper> getQuickTestDataRecipeForListOfOperators(Integer workorderId, String recipe,
			List<String> operatorList);

	Map<String, List<String>> getRecipeOperatorMap(String category, RecipeMappingWrapper wrapper);

	List<KPIWrapper> modifyIndexOfCustomKpis(List<KPIWrapper> kpiList);

	Response saveFileAndUpdateStatus(Integer analyticsrepoId, String filePath, GenericWorkorder genericWorkorder,
			File file, String downLoadFileName ,String reportInstanceKey);

	boolean deleteFileIfExist(String absoluteFilePath);

	File getRecipeWiseKpiFile(String reportType, Integer workOrderId, String recipeId, String operator, SSVTReportWrapper mainRecipeWrapper, String appliedto)
			throws MalformedByteSequenceException, IOException, IllegalAccessException, NoSuchFieldException;

	List<KPIWrapper> getListOfKpisWithRanges(Map<String, Integer> kpiIndexMap, String legendAppliedto);

	List<KPIWrapper> setKpiStatesIntokpiList(List<KPIWrapper> kpiList, List<Integer> woIds);

	ReportSubWrapper getGraphDataWrapper(List<KPIWrapper> kpiList, List<String[]> arlist);

	void genrateDocxReport(String hdfsPath, String file, String docFilePath);
	
    Map<String, Integer> getMergedMapData(Integer workorderId, String kpi,List<String> recepiList, List<String> operatorList);

	ReportSubWrapper getRemarkDataForReport(Integer workorderId, Map<String, List<String>> map);
	

	File generateRemarkReport(Integer workorderId, String filePath, List<File> files, String jasperPath,
			RemarkReportWrapper mainWrapper);

	List<SiteInformationWrapper> getSiteDatabyViewPortForTechnology(List<String[]> arlist, String technology);

	List<String> getGegraphyNameList(String geoType, List<Integer> geoIds);

	List<SiteInformationWrapper> getFirstTierNieghbourSites(List<String> neNameCellIdList);

	List<PSDataWrapper> getPsDataWrapperFromSummaryData(String[] summaryArray);

	Map<String, Object> getSiteDataForReport(GenericWorkorder genericWorkorder,Long startTimeStamp,Long endTimeStamp, List<String[]> csvDataArray, boolean needOnlyOnAirSites, boolean needOnlyMacroCells);

	Map<String,Object> getSiteDataForReport(GenericWorkorder genericWorkorder,Long startTimeStamp,Long endTimeStamp,List<String[]> csvDataArray, boolean needOnlyOnAirSites, boolean needOnlyMacroCells, Map<String, Integer> kpiIndexMap);

	Map<String, Integer> getMergedMapDataForWoList(List<Integer> workorderIds, String kpi, List<String> recepiList,
			List<String> operatorList);

	List<KPIWrapper> setKpiStatesIntokpiListForWoList(List<KPIWrapper> kpiList, List<Integer> workOrderIds,
			List<String> recepiList, List<String> operatorList);

	String getDriveData(List<Integer> workOrderIds, List<String> recipeList, List<String> operatorList);

	String getSummaryDataForWoIds(List<Integer> workOrderIds, List<String> recipeList, List<String> operatorList);

	String getFloorplanImg(Integer workorderId, String recipeId, String opName,
			DriveDataWrapper driveDataWrapper) throws IOException;
	
	List<KPIWrapper> getKpiWrapperList(Integer workorderId, List<String> recipe, String opName, String appliedTo);

    List<KPIWrapper> getKpiWrapperListForReport(Integer workorderId, List<String> recipe, String opName, String appliedTo, Map<String, Integer> kpiIndexMap);

    List<KPIWrapper> getKPiStatsDataList(List<Integer> workorderId, Map<String, List<String>> map,
                                         Map<String, Integer> kpiIndexMapFORSsvT, String applidto);

	String getGeographyBoundaryByLevelAndName(String name, String level);

	CustomGeographyWrapper getCustomGeographyRoute(String recipeCustomGeoMap);

	CustomerComplaintSubWrapper setSiteRelatedInformation(CustomerComplaintSubWrapper wrapper, Map<String, Object> jsonMap);

	void saveKpiSummaryData(GenericWorkorder workorderObj, List<KPISummaryDataWrapper> kpiSummaryDataList);
		
	boolean getFileProcessedStatusForWorkorders(List<Integer> workorderIdList);



	List<List<List<Double>>> getAllCustomRoutesOfPrePost(List<Integer> previousWoId, List<Integer> currentWoId);

	List<KPIWrapper> modifyIndexOfCustomKpisForReport(List<KPIWrapper> kpiList, Map<String, Integer> kpiIndexMap);

	List<KPISummaryDataWrapper> getKPISummaryDataForWorkOrderId(Integer workOrderID, List<String[]> dataKPIs,
			List<KPIWrapper> listOfKPidata, String[] summaryData, String reportType, String operator,
			List<Double> dlSpeedtestList, Map<String, List<String>> map, Map<String, Integer> kpiIndexMap, Map<String, Integer> summaryKpiIndexMap);




	String[] getSummaryDataForReport(Integer workorderId, List<String> recipeList, List<String> columnsList);

	List<GraphDataWrapper> setDataForEarfcnGraph(List<String[]> arlist, Map<String, Integer> kpiIndexMap);

	List<PSDataWrapper> getPsDataWrapperFromSummaryDataForReport(String[] summaryDataArray,
			Map<String, Integer> summaryKpiIndexMap);

	List<SiteInformationWrapper> getSiteDataForWorkorder(GenericWorkorder genericWorkorder);

	List<Integer> getPciListFromWoMetaData(Map<String, String> geoMetaDataMap);

	List<String[]> getDriveDataForReport(Integer workorderId, List<String> recipeList, List<String> kpiList);

	@Transactional(readOnly  = true)
	List<SiteInformationWrapper> getSiteDataForSSVTReport(GenericWorkorder genericWorkorder);

	Set<String> getDynamicKpiName(List<Integer> workorderIds, String recipeId, String layerType);

	HashMap<String, String> getImagesForReport(List<KPIWrapper> kpiList, DriveImageWrapper driveImageWrapper,
			List<DriveDataWrapper> imageDataList, String operator, String reportType) throws IOException;

	List<KPIDataWrapper> populateKPiAggData(String smryData, List<KPIWrapper> listOfKpiWrapper);

	List<KPIDataWrapper> populateKPiAggData(String[] summaryData, List<KPIWrapper> kpiWrapperList,
			Map<String, Integer> summaryKpiIndexMap);

	HashMap<String, String> getImagesForReport(List<KPIWrapper> kpiList, DriveImageWrapper driveImageWrapper,
			List<DriveDataWrapper> imageDataList, String operator, String reportType, Map<String, Integer> kpiIndexMap)
			throws IOException;

	boolean getFileProcessedStatusForRecipeAndWorkorder(Integer workorderId, Integer recipeId);

   List<String[]> getDriveDataForReport(List<Integer> workorderIds,List<String> recipeList, List<String> kpiList);

String[] getSummaryDataForReport(List<Integer> workorderIds, List<String> recipeList, List<String> columnsList);

	List<String[]> getDriveDataRecipeWiseTaggedForReport(List<Integer> workorderIds,List<String> recipeList, List<String> kpiList, Map<String, Integer> kpiIndexMap);

List<YoutubeTestWrapper> getYouTubeDataForReport(List<String[]> driveData, Map<String, Integer> kpiIndexMap);

	List<String[]> getDriveDataForScanner(List<Integer> workorderIds,List<String> recipeList, List<String> kpiList);
	
	HashMap<String, BufferedImage> getLegendImagesForReport(List<KPIWrapper> list, List<String[]> dataKPIs,
			Map<String, Integer> kpiIndexMap);

	List<KPIWrapper> setKpiStatesIntokpiListForReport(List<KPIWrapper> kpiList, Integer workOrderId,
			List<String> recepiList, List<String> operatorList);

	List<KPIWrapper> getKPIList(Map<String, Integer> kpiIndexMap, String reportType);
	
	 List<DriveDataWrapper> getSpeedTestDatafromHbase(Integer workorderId, List<String> receipeList);

	boolean getFileDetailByRecipeMappingId(Integer recipeId);


	List<MessageDetailWrapper> getLayer3MessagesDataForReport(Integer workorderid, List<String> recipeList, List<String> kpiList);

	List<KPISummaryDataWrapper> getDlPeakValuesBandWise(Integer workorderId, Map<String, List<String>> map);

	Map<String, Double> getPeakValueData(Integer workorderId, Map<String, List<String>> map);

	KPISummaryDataWrapper getPeakValueRow(String testName, String source, Entry<String, Double> peakDLvalueMap);

	List<String[]> getHandoverDataFromHbase(Integer workorderId, List<String> recipeList);

}
