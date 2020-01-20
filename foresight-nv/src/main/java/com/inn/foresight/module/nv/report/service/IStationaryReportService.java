package com.inn.foresight.module.nv.report.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;

public interface IStationaryReportService {


	File getStationaryTestReportForMasterReport(List<Integer> workorderIds, GenericWorkorder genericWorkorder, Map<String, List<String>> driveRecipedetailsMap);

}
