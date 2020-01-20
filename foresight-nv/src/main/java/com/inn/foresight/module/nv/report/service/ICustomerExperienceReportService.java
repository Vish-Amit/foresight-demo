package com.inn.foresight.module.nv.report.service;

import java.util.List;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.layer3.wrapper.Layer3ReportWrapper;
import com.inn.foresight.module.nv.report.smf.wrapper.SmfStationaryWidget;
import com.inn.foresight.module.nv.report.smf.wrapper.SmfWidgetWrapper;
import com.inn.foresight.module.nv.report.wrapper.RawDataWrapper;

public interface ICustomerExperienceReportService {

	Response execute(String json);

	List<RawDataWrapper> getLocationWiseDataForStationry(List<SmfStationaryWidget> locationWiseList, String filePath, String opName);

	String getReportName(SmfWidgetWrapper wrapper);
	
	Layer3ReportWrapper getDeviceReportForStealth (String taskId, String workorderId, String date);
	
}
