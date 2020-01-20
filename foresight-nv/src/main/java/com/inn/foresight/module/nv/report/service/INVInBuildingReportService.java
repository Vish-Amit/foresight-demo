package com.inn.foresight.module.nv.report.service;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;

public interface INVInBuildingReportService {

	Response createIBReport(String json);

	Response execute(String json);

	Response createIBReport(Integer recipeId, String operator, Integer inbuildingid, Integer workorderId,  String technology);

	File createIBReportForRecipeId(Object recipeList, Object OperatorList, Object workOrderid, String level,
			Integer reportinstanceId);

	Response createIBReportByFloor(Integer inbuildingid, Integer floorId, String technology);

	String getFloorplanImg(Integer recepiMappingId, String opName, DriveDataWrapper driveDataWrapper,
			String floorplanJson, String localDirPath) throws IOException;

	DriveDataWrapper getDataWrapperForRecipeId(Integer woId, Integer recipeId);
}
