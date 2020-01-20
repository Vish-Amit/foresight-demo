package com.inn.foresight.module.nv.workorder.rest;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.workorder.wrapper.NVWorkorderWrapper;

public interface NVWorkorderRest {

	@PreAuthorize("hasRole('ROLE_NV_WO_createWO')")
	@Secured("ROLE_NV_WO_createWO")
	Response createWorkorder(NVWorkorderWrapper wrapper);

	@PreAuthorize("hasRole('ROLE_NV_WO_createWO')")
	@Secured("ROLE_NV_WO_createWO")
	Response createWorkorderForDevice(NVWorkorderWrapper wrapper);

	@PreAuthorize("hasRole('ROLE_NV_WO_viewWO')")
	@Secured("ROLE_NV_WO_viewWO")
	Response getTotalWorkorderCount(String searchString, Boolean isArchived, Map<String, List<String>> filterMap);

	@PreAuthorize("hasRole('ROLE_NV_WO_viewWO')")
	@Secured("ROLE_NV_WO_viewWO")
	Response findAllWorkorder(Map<String, List<String>> filterMap, Integer lLimit, Integer uLimit, String searchString,
			Boolean isArchive);

	@PreAuthorize("hasRole('ROLE_NV_WO_viewWO')")
	@Secured("ROLE_NV_WO_viewWO")
	Response getWorkorderDetailsByWOId(Integer workorderId, Integer taskId);

	Response getWorkorderDetails(String mapStr);

	@PreAuthorize("hasRole('ROLE_NV_WO_archiveWO')")
	@Secured("ROLE_NV_WO_archiveWO")
	Response updateWorkorderArchivedStatus(Integer workorderId, Boolean isArchive);

	Response getWorkorderDetailsByWOIdAndDeviceId(Integer workorderId, String deviceId, Long date);

	@PreAuthorize("hasRole('ROLE_NV_WO_completeWO')")
	@Secured("ROLE_NV_WO_completeWO")
	Response completeWOTask(Integer taskId);

	@PreAuthorize("hasRole('ROLE_NV_WO_viewWO')")
	@Secured("ROLE_NV_WO_viewWO")
	Response findFilteredWO(NVWorkorderWrapper wrapper, Integer lLimit, Integer uLimit, Boolean isArchive);

	Response getGeographyBoundryByLevel(String tableName, String strGeoData);

	Response getWorkorderListByGeography(String geographyLevel, Integer geographyId, String remark,
			Map<String, List<String>> filterMap);

	Response uploadKML(InputStream inputStream, String fileName);

	Response getWorkOrderDetailByImei(String imei, Long startTime, Long endTime, TemplateType templateType,
			HttpServletRequest request);

	Response getWorkorderListByGeographyOfPeriod(String geographyLevel, Integer geographyId,
			Map<String, List<String>> filterMap, Integer quarter, Integer year);

	Response updateWorkorder(NVWorkorderWrapper wrapper);

	Response getWOListByGeographyListOfPeriod(String geographyLevel, Map<String, List<String>> filterMap,
			Integer quarter, Integer year, String technology);

	@PreAuthorize("hasRole('ROLE_NV_WO_reassignWO')")
	@Secured("ROLE_NV_WO_reassignWO")
	Response reassignWorkorder(Integer taskId, String userName, String remark);

	@PreAuthorize("hasRole('ROLE_NV_WO_deleteWO')")
	@Secured("ROLE_NV_WO_deleteWO")
	Response deleteWorkorder(Integer taskId);

	Response deleteFiles(List<Integer> idList);

	Response updatePresetIdForWorkorder(Integer presetId, Integer workorderId);

	Response checkIfReportAvailabel(String workorderId);

	@PreAuthorize("hasRole('ROLE_NV_WO_reopenWO')")
	@Secured("ROLE_NV_WO_reopenWO")
	Response reopenRecipeById(String workorderId);

}
