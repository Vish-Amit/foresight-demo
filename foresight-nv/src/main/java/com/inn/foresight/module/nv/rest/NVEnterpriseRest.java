package com.inn.foresight.module.nv.rest;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import com.inn.foresight.module.nv.livedrive.wrapper.TrackSpanWrapper;

public interface NVEnterpriseRest {

	Response getWorkorderDetails(String mapStr);

	Response completeWOTask(String strTaskId);

	Response getGeographyBoundryForMobile(String strGeoData);

	Response createWorkorderForMobile(String strWorkorder);

	Response getSitesFromLatLong(String encLatLongMap);

	Response completeRecipe(String strWoRecipeMappingId);

	Response startRecipe(String strWoRecipeMappingId, String taskId);

	String syncRecipeFiles(String encrWORecipeMappingId, Boolean isRetried, String fileType, InputStream inputFile,
			String fileName, String taskId);

	String syncWorkOrderFileForBenchMarking(String encrWORecipeMappingId, Boolean isRetried, String fileType,
			InputStream inputFile, String fileName);

	String syncLiveDriveJsonFileToKafka(TrackSpanWrapper trackSpanWrapper);

	String syncRemainingLiveDriveData(InputStream inputFile, String fileName);

	Response getNearestBuildingsFromLocation(Double latitude, Double longitude);

	Response getBuildingListByName(String name);

	Response createBuildingData(String buildingData);

	Response uploadFloorPlanData(Integer unitId, InputStream inputStream, String fileName);

	String appLogout(HttpServletRequest request, HttpServletResponse response);

	Response downloadFloorPlanData(Integer unitId);

	Response getReportById(Integer analyticsRepositoryId, HttpServletRequest request);

	Response downloadFloorPlan(Integer unitId);



	Response processWOReportDump(Integer workorderId, Map<String, List<String>> map, HttpServletRequest request);

	Response addRecipeIntoWO(String encryptedString);

	Response getWOReportDump(String fileName, HttpServletRequest request);

	Response getReportByAnalyticsrepositoryId(Integer analyticsRepositoryId);

	Response geFilePartByRecipMappingId(String woRecipeMappingId);

	Response deleteFileByRecipeMappingId(String woRecipeMappingId);

	Response getWorkorderStatusByTaskIdList(String encrTaskIdList);

	Response getDeviceListByUserName(List<String> userList, Integer lLimit, Integer uLimit, Boolean isInstalled);

	Response getDeviceListCountByUserName(List<String> userList, Boolean isInstalled);

	@PreAuthorize("hasRole('ROLE_NV_WO_DEVICE_downloadCSV')")
	@Secured("ROLE_NV_WO_DEVICE_downloadCSV")
	Response getWOReportDumpForStealth(String workorderId, String taskId, String date, HttpServletRequest request);

	@PreAuthorize("hasRole('ROLE_NV_WO_DEVICE_downloadPPTX')")
	@Secured("ROLE_NV_WO_DEVICE_downloadPPTX")
	Response getDeviceReportForStealth(String workorderId, String taskId, String date, HttpServletRequest request);

	Response getDeviceList(Integer lLimit, Integer uLimit);

	Response getDeviceListCount();

	Response getPDFReportForStealthWO(String workorderId, HttpServletRequest request);

	Response getStealthReportForLayerVisualization(String workorderId, String deviceId, String date,
			HttpServletRequest request);

	Response generateNvDashboardReport(String geographyName, String geographyType, String band, String technology,
			String operator, String startTime, String endTiime, HttpServletRequest request);

	Response syncMultipleWorkorderFiles(HttpServletRequest request);

	Response processStealthWOCsvDump(String workorderId, HttpServletRequest request);

	Response getStealthWOCsvDump(String filePath, HttpServletRequest request);

	Response processStealthWOPdfForTaskId(String workorderId, Long startDate, Long endDate, List<Integer> taskIdList,
			HttpServletRequest request);

	Response getStealthWOPdfForTaskId(String filePath, HttpServletRequest request);

	@PreAuthorize("hasRole('ROLE_NV_WO_DEVICE_downloadLOG')")
	@Secured("ROLE_NV_WO_DEVICE_downloadLOG")
	Response getLogFileForRecipe(String recipeId, String fileId, HttpServletRequest request);

	String updateWOStatusOnHold(String encrWOId, String encrRemark, String encrFilename);

	String syncStealthWOFile(String woTaskResultId, String fileType, InputStream inputFile, String fileName);

	String updateOccuranceOfEvent(String encryptedJson);

	Response getRecipeWiseSummary(String encryptedRecipeId);

	Response reopenRecipeById(String woRecipeId, String workOrderId);

	Response getInbuildingReportPdf(Integer recipeId, String operator, Integer inbuildingid, Integer workorderId,
			String technology, HttpServletRequest request);

	Response getInBuildingReportFloorWise(Integer floorId, Integer inbuildingid, String technology,
			HttpServletRequest request);



}
