package com.inn.foresight.core.report.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.report.model.ReportWidget;

/**
 * The Interface IReportWidgetDao.
 */
public interface IReportWidgetDao extends IGenericDao<Integer, ReportWidget> {

/**
 * Soft delete widget by id.
 *
 * @param id the id
 * @return the integer
 * @throws RestException the rest exception
 */
Integer softDeleteWidgetById(Integer id);

List<String> getAllReportMeasure();

List<String> getAllReportTypeByReportMeasure(String reportMeasure);

List<ReportWidget> getReportWidgetByFilter(String reportName, String reportMeasure, String reportType, Integer userId, String date, Integer lowerLimit, Integer upperLimit);

Long getReportWidgetCountByFilter(String reportName, String reportMeasure, String reportType, Integer userId, String date);
 
List<ReportWidget> getAllPmReportDomainWise(List<String> domain);
 
List<String> getAllWidgetReportType();
 
void deleteByPk(Integer reportWidgetPk);

List<ReportWidget> getAllReportByReportMeasure(List<String> reportMeasure);

Integer softDeleteWidgetByIds(List<Integer> ids);

}
