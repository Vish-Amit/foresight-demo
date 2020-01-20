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

public interface NVFilesDownloadUploadRest {

	@PreAuthorize("hasRole('ROLE_NV_WO_DEVICE_downloadLOG')")
	@Secured("ROLE_NV_WO_DEVICE_downloadLOG")
	Response getLogFileForRecipe(String recipeId, String fileId, HttpServletRequest request);

	Response createSignalMessageRecipeWiseCsv(Integer workorderId, Map<String, List<String>> map,
			HttpServletRequest request, String workorderName);

	Response processWOReportDump(Integer workorderId, Map<String, List<String>> map, HttpServletRequest request);

	Response getDriveDetailForStatisticDataXls(Integer workorderId, String fileName, Boolean isInBuilding,
			Map<String, List<String>> map, HttpServletRequest request);

	

}
