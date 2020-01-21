package com.inn.foresight.core.report.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.report.model.ReportWidget;
import com.inn.foresight.core.report.wrapper.ReportWidgetWrapper;

/**
 * The Interface IReportWidgetService.
 */
public interface IReportWidgetService extends IGenericService<Integer, ReportWidget> {

	/**
	 * Soft delete widget by id.
	 *
	 * @param id the id
	 * @return the string
	 * @throws RestException the rest exception
	 */
	String softDeleteWidgetById(Integer id);

	/**
	 * Save CM comparision report.
	 *
	 * @param reportWidgetWrapper the report widget wrapper
	 * @return the report widget wrapper
	 * @throws RestException the rest exception
	 */
	ReportWidgetWrapper saveCMComparisionReport(
			ReportWidgetWrapper reportWidgetWrapper);

	/**
	 * Creates the report.
	 *
	 * @param reportWidgetWrapper the report widget wrapper
	 * @return the report widget wrapper
	 * @throws RestException the rest exception
	 */
	ReportWidgetWrapper createReport(ReportWidgetWrapper reportWidgetWrapper);

	List<String> getAllReportMeasure();

	List<String> getAllReportTypeByReportMeasure(String reportMeasure);

	List<ReportWidget> getReportWidgetByFilter(String reportName, String reportMeasure, String reportType, Long date, Integer lowerLimit, Integer upperLimit);

	Long getReportWidgetCountByFilter(String reportName, String reportMeasure, String reportType, Long date);

	List<String> getAllWidgetReportType();

	void removeById(Integer primaryKey);
	
}