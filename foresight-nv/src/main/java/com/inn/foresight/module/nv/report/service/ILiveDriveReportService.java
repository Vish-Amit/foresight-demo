package com.inn.foresight.module.nv.report.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.xerces.impl.io.MalformedByteSequenceException;

import com.inn.foresight.module.nv.report.wrapper.inbuilding.HandOverDataWrappr;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;

public interface ILiveDriveReportService {

	/**
	 *Public Response createReportForWorkOrderID(Integer workorderId,Integer reportIntanceId) throws RestException;

 *	public Response createReportForWorkOrderID(Integer workorderId, Integer reportIntanceId, String userName)
 *			throws RestException;

	 * Response execute(String json) throws RestException;.
	 */

	File getLiveDriveReportForMasterReport(List<Integer> workorderIds, Map<String, List<String>> driveRecipedetailsMap, boolean isMasterRepport) 
			throws MalformedByteSequenceException, IOException;

	//List<HandOverDataWrappr> getHandoverCauseData(Integer workorderId, Map<String, List<String>> map);

	List<HandOverDataWrappr> getHandoverCauseData(List<WORecipeMapping> mappings, Map<String, List<String>> map);
	
}
