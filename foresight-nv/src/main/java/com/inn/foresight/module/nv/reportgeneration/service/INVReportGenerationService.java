package com.inn.foresight.module.nv.reportgeneration.service;

import java.io.IOException;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.reportgeneration.wrapper.NVReportWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.NVWorkorderWrapper;

public interface INVReportGenerationService {

	NVReportWrapper getReportData(Integer analyticsrepositoryid)  throws IOException, Exception ;

	Response getPdfReportByInstanceId(Integer reportInstanceId);

	void generateReport(String json) throws Exception;

	Response getInBuildingReport(String json);

	Response getInBuildingReport(Integer recipeId, String operator, Integer inbuildingid, Integer workorderId, String technology);

	Response getReportById(Integer rowkey, String extension) throws IOException ;

	String saveImagefromGoogleApi();

	Response getInBuildingReportFloorWise(Integer floorId, Integer inbuildingid, String technology);
	boolean removeRejectedData(NVWorkorderWrapper nvWorkorderWrapper);

	String getDownloadFileName(Integer analyticsrepositoryId, String extension);
	

}
