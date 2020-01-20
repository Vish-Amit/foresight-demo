package com.inn.foresight.module.nv.workorder.service;

import java.util.List;
import java.util.Map;

import com.inn.foresight.module.nv.core.workorder.wrapper.WorkorderCountWrapper;
import com.inn.foresight.module.nv.reportgeneration.wrapper.NVReportWrapper;

public interface IWorkorderDashboardService {
	Map<String, Object> getWOCount(String type,Long currentDate, String geographyLevel, Integer geographyId);
	Map<String, Object> getAllWorkorderCount(Long currentDate, String geographyLevel, Integer geographyId);
	Map<String, Object> getADHOCWorkorderDayWiseCount(Long currentDate, String geographyLevel, Integer geographyId);
	Map<String, Map<String, Long>> getWorkorderCountByStatus(String type,String templateType,Long currentDate, String geographyLevel, Integer geographyId);
    Map<String, Object> getWorkorderCountByAssignedType(String assignedType,Long currentDate, String geographyLevel, Integer geographyId);
    Map<String, Long> getDueWorkorderDayWiseCount(Long currentDate, String geographyLevel, Integer geographyId);
    List<WorkorderCountWrapper>getDueWorkorderList(Long currentDate, String geographyLevel, Integer geographyId, Integer llimit, Integer ulimit);
	NVReportWrapper getWorkorderReportData(Long currentDate,String geographyLevel, Integer geographyId);
}
