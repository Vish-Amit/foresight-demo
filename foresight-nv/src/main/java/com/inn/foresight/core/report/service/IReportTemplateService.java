package com.inn.foresight.core.report.service;

import java.util.List;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.report.model.ReportTemplate;
import com.inn.foresight.core.report.wrapper.ReportTemplateWrapper;

public interface IReportTemplateService extends IGenericService<Integer, ReportTemplate> {
	
	List<ReportTemplate> findAll() ;

	List<String> getAllReportMeasure();

	List<String> getAllReportTypeByReportMeasure(String reportMeasure);

	List<String> getAllTemplateReportType();
	
	void removeById(Integer primaryKey);

	List<ReportTemplate> getAllReportTemplate();
	
	ReportTemplate update(ReportTemplate reportTemplate) ;

	void deleteReportTemplateById(Integer primaryKey);

	List<ReportTemplateWrapper> getReportTemplateByFilter(String module,Long date, Integer lowerLimit, Integer upperLimit);

	Long getReportTemplateCountByFilter(String module, Long date);

	ReportTemplate getReportTemplateByReportType(String reportType);

	List<String> getReportMeasureByModule(String reportModule);

	List<String> getReportCategoryByModuleAndMeasure(String reportModule, String reportMeasure);

	ReportTemplateWrapper getReportTemplateByModuleMeasureAndCategory(String reportModule, String reportMeasure, String reportCategory);
		
}