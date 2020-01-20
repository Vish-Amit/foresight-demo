
package com.inn.foresight.module.nv.layer3.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.hadoop.hbase.client.Get;

import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.nns.NNS;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3ReportWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.RecipeMappingWrapper;
import com.inn.foresight.module.nv.report.wrapper.RemarkDataWrapper;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;
import com.inn.product.systemconfiguration.model.SystemConfiguration;

/**
 * The Interface INVLayer3DashboardService.
 *
 * @author innoeye
 * date - 08-Jan-2018 2:30:51 PM
 */
public interface INVLayer3DashboardService {
	
	/** Public String getDriveRecipeDetail(Integer workrorderId);. */

	String getDynamicKpiAndEvents(Integer workrorderId, List<Integer> recipeList);

	List<SystemConfiguration> getNVLayer3SystemConfigurationByNameAndType(List<String> nameList, String paramPrefix);

	String getSignalMessageDetail(String rowKey, String rowKey2);

	String getKpiStatsData(Integer workorderId, String kpi);

	String getDriveSummaryReceipeWise(Integer workorderId, List<String> recipeId, List<String> operatorList);

	String getDriveDetailReceipeWise(Integer workorderId, List<String> recipeId, List<String> operatorList);

	String getKpiStatsRecipeData(Integer workorderId, String kpi, List<String> recipeId,
			List<String> operatorList);

	String processWOReportDump(Integer workorderId, List<String> recipeId, List<String> operatorList);

	Layer3ReportWrapper getWOReportDump(String fileName);


	Map<String, List<String>> getDriveRecipeDetail(Integer workrorderId);

	String getSignalMessageRecipeWise(Integer workorderId, String lastRowKey, String direction, List<String> recipeId,
			List<String> operatorList, String message);

	String getKpiStatsRecipeDataForReport(Integer workorderId, String kpi, List<String> recipeId,
			List<String> operatorList);

	DriveDataWrapper getFloorplanDataFromLayer3Report(Integer workorderId, String operator, String recipeId);

	Map<String, List<String>> getRecipeDetailByWorkorderList(List<Integer> workorderIdList);


	String getQuickTestDataFromLayer3Report(Integer workorderId, String operator, String recipeId);

	String getYoutubeDataFromLayer3Report(Integer workorderId, String operator, String recipeId);

	Map<String, Double> getSummaryPeakValueReceipeWise(Integer workorderId, List<String> list, List<String> list2);

	Map<String, RecipeMappingWrapper> getDriveRecipeDetailForMasterReport(List<Integer> workrorderIds);

	String getDriveSummaryReceipeWiseNew(Integer workorderId, List<String> recipLise, List<String> operatorlist);


	String createSignalMessageRecipeWise(Integer workOrderId, List<String> recipeId, List<String> operatorList, String workorderName);

	String getHttpDownLinkDataFromLayer3Report(Integer workorderId, String operator, String recipeId);

	String getHandoverDataFromHbase(Integer workorderId, String operator, String recipeId);

	String getAggrigationDataByLatLong(Integer workorderId, List<String> recipeId, List<String> operatorList);

	List<String> getColumnListForDriveDetail();

	List<Get> getListForSummary(Integer workorderId, List<String> recipeId, List<String> operatorList,
			List<String> columnList);

	String getDriveDetailDataForWoIds(List<Integer> woIds);

	String getDriveSummaryForWoIds(List<Integer> woIds);

	List<Get> getRowkeyListForWoIds(List<Integer> woIds);

	List<Get> getListForKpiStats(Integer workorderId, List<String> recipeId, List<String> operatorList, String kpi);

	List<String> getRowkeyListForWoIds(Integer workorderId, List<String> recipeId, List<String> operatorList);

	String getKpiStatsDataForGetListOfKpi(List<Get> getList, String kpi);

	Map<String, String> getSummaryMapCategoryWiseForWoIds(List<Integer> woIds);
	
	String getSignalMessagesForBin(Long startTime, Long endTime, String rowPrefix);
	
	String searchLayer3SignalMessage(Integer workorderId,List<String> recipeId, List<String> operatorList, String searchTerm);


	Map<String, String> getBandWiseDataMapForCategory(List<Integer> list, String recipeCategoryDrive, boolean bool);



	List<Get> getListForgetRemarkTestSkip(Integer workorderId, List<String> recipeId, List<String> operatorList);

	DriveDataWrapper getFloorplanDataFromLayer3ReportForFramework(Integer workorderId, String recipeId);

	List<RemarkDataWrapper> getRemarkTestSkipFromLayer3Report(Integer workorderId, List<String> recipeId,
			List<String> operatorList);
	
	Map<String, List<String[]>> getSmsDataForWoIds(List<Integer> woIds);

	String getNeighbourForBin(Long startTime, String rowPrefix);

	List<Map> getWorkorderListByGeographyOfPeriod(String geographyLevel, List<Integer> geographyId,
			List<Status> statusList, List<TemplateType> templateList, List<Integer> quarterList, List<Integer> yearList);

	Layer3ReportWrapper getWOReportDumpForStealth(String workorderId, String taskId, String date);


	String getDriveRecipeDetailJson(Integer workrorderId);



	String getDriveRecipeDetailForLayer3(Integer workrorderId);
	
	String getLogFileForRecipe(String recipeId, String fileId);



	List<WOFileDetail> getFileDetailsListForWorkOrderId(Integer workorderId);

	Map<String, List<String>> getDriveRecipeDetail(List<Integer> workrorderIdList);

	List<Get> getListForKpiStats(List<Integer> workorderIds, List<String> recipeIdList, List<String> operatorList,String kpi);

	String getKpiStatsRecipeDataForReportForWoList(List<Integer> workorderIds, String kpi, List<String> recipeId,List<String> operatorList);

	String getDriveDetailReceipeWiseforWoIds(List<Integer> workorderIds, List<String> recipeId,List<String> operatorList);

	String getDriveSummaryReceipeWiseForWoIds(List<Integer> workorderIds, List<String> recipeId,List<String> operatorList);

	String getDriveSummaryReceipeWiseOld(Integer workorderId, List<String> recipeId, List<String> operatorList);

	Map<Integer, NNS<LatLng>> getPCIWiseNNSMapForRanSite();

	LatLng getNearestCellByPCILatLong(Integer pci, Double latitude, Double longitude);

	
	
	Map<String, String> getNeighbourDataRecipeWise(Integer workorderId, List<String> recipeId, List<String> operatorList);

	Response clearPCIWiseNNSMapForMacroSite();

	Response checkPCIWiseNNSMapSizeForMacroSite();

	
	String getDriveDetailForStatisticData(List<String> kpiIndexList, Integer workorderId, List<String> recipes,
			List<String> operators, String fileName, Boolean isInbuilding, List<String> kpiNeighbourList);

       Response updateDLFLogFileStatusToReprocess(Integer workOrderId,Integer recipeId);

	Map<String, List<RecipeWrapper>> getRecipeMappingWrapperByWOId(Integer workrorderId);

	Map<String, List<String>> getDriveRecipeDetailByRecipeId(Integer recipeId);

	
	
}
