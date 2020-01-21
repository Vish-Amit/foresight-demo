package com.inn.foresight.core.report.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.product.um.user.model.User;

public interface IAnalyticsRepositoryService extends IGenericService<Integer, AnalyticsRepository> {

	AnalyticsRepository createCustomReportRepository(Integer reportTemplateId, AnalyticsRepository anEntity);

	String hdfsFileUpload(HttpServletRequest request, User user);

	List<AnalyticsRepository> getReportInfoByName(String FileName);
}