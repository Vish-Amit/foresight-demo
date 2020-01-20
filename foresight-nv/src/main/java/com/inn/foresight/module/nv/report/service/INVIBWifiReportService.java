package com.inn.foresight.module.nv.report.service;

import java.io.File;
import java.util.List;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.inbuilding.model.NVIBUnitResult;

public interface INVIBWifiReportService {

	Response execute(String json);

	File generateIBWIFIFloorLevelReport(List<NVIBUnitResult> nvIbunitResultList, String fileName) throws Exception;

	File generateIBWIFIBuildingLevelReport(Integer buildingId, String fileName);

	File generateIBWIFITaskLevelReport(Integer recipeId, Integer workorderId, String fileName);

}