package com.inn.foresight.core.report.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.report.model.ReportTemplate;
import com.inn.foresight.core.report.wrapper.ReportTemplateWrapper;

public interface IReportTemplateDao extends IGenericDao<Integer, ReportTemplate> {
	
	List<ReportTemplate> findAll() ;

	List<String> getAllMeasure();

	List<String> getAllReportTypeByMeasure(String reportMeasure);

	List<String> getAllTemplateReportType();

	void deleteByPk(Integer reportWidgetPk);

	List<ReportTemplate> getAllReportTemplate();
	
	ReportTemplate update(ReportTemplate reportTemplate) ;

	void deleteReportTemplateById(Integer id);

	List<ReportTemplateWrapper> getReportTemplateByFilter(String module, Integer userId, String date, Integer lowerLimit, Integer upperLimit);

	Long getReportTemplateCountByFilter(String module, Integer userId, String date);

	ReportTemplate getReportTemplateByReportType(String reportType);

	ReportTemplate getAllReportTemplateByID(Integer id);

	List<ReportTemplate> getReportTemplateByModule(String moduleName);

	List<String> getReportMeasureByModule(String reportModule);

	List<String> getReportCategoryByModuleAndMeasure(String reportModule, String reportMeasure);

	ReportTemplateWrapper getReportTemplateByModuleMeasureAndCategory(String reportModule, String reportMeasure, String reportCategory);
		
}
