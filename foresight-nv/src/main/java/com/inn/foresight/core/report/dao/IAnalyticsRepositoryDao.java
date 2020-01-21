package com.inn.foresight.core.report.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;


public interface IAnalyticsRepositoryDao extends IGenericDao<Integer, AnalyticsRepository> {

  AnalyticsRepository updateStatusById(Integer analyticsrepositoryId);

  AnalyticsRepository updateStatusInAnalyticsRepository(Integer analyticsReportId, String filePath,
      String type, progress progressType, String downLoadFileName);

  List<Object[]> getReportDetailsByUserQuery(String userQuery);

List<AnalyticsRepository> getReportInfoByName(String FileName);
}
