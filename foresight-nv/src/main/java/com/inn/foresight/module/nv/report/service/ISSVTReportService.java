package com.inn.foresight.module.nv.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.fm.core.wrapper.AlarmDataWrapper;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.ReportDataHolder;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;

public interface ISSVTReportService {

	List<KPIWrapper> goingToSetKpiStats(List<KPIWrapper> kpiList, Integer workOrderId,
			List<String> recepiList, List<String> operatorList);

	String getDriveData(Integer workorderId, Map<String, List<String>> map);

	String getSummaryData(Integer workorderId, Map<String, List<String>> map);


	Response execute(String json);


	List<SiteInformationWrapper> getFirstTierNieghbourSites(List<String> neNameCellIdList);


	Map<String, Map<String, List<String>>> getRecipeMapForRecipeId(Integer workorderId, Map<String, String> recipeMap);


	SSVTReportWrapper setSiteAuditImageInWrapper(GenericWorkorder workorderObj, SSVTReportWrapper mainWrapper,
			Map<String, Object> imageMap);

	HashMap<String, String> getCoveragePredictionImage(DriveImageWrapper driveImageWrapper);

	SSVTReportWrapper preparedWrapperForJasper(GenericWorkorder genricWorkOrder,
			List<SiteInformationWrapper> siteInfoList, List<KPISummaryDataWrapper> kpiInfoList,
			SSVTReportSubWrapper subWrapper, SSVTReportWrapper mainWrapper, String username,
			List<AlarmDataWrapper> listOfNeHaveAlarm);


	List<KPIWrapper> getKpiStatsList(GenericWorkorder workorderObj, Map<String, List<String>> driveRecipeDetailMap,
			Map<String, Integer> indexMap);

	

	Map<String, String[]> getRecipeWiseSummary(Map<String, Map<String, List<String>>> recipeWiseIDMap,
			Integer workorderId, List<String> fetchSummaryKPIList);


	List<KPISummaryDataWrapper> getKPISummaryDataForWorkOrderId(Integer workOrderID, List<String[]> dataKPIs,
			List<KPIWrapper> listOfKPidata, String[] summaryData, String reportType, String operator,
			List<Double> dlSpeedtestList, Map<String, List<String>> map, ReportDataHolder reportDataHolder);



	List<KPISummaryDataWrapper> getkpiSummaryListAutomationReport(Map<String, List<String>> driveRecipeDetailMap,
			String[] summaryData, GenericWorkorder genericWorkorder, List<DriveDataWrapper> imageDataList,
			List<KPIWrapper> kpiList, String reportTestType, ReportDataHolder reportDataHolder);

}
