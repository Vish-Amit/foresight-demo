package com.inn.foresight.core.report.dao;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.report.model.AnalyticsTemplates;
import com.inn.foresight.core.report.model.AnalyticsTemplates.TemplateType;

public interface IAnalyticsTemplatesDao extends IGenericDao<Integer, AnalyticsTemplates>{
	AnalyticsTemplates getAnalyticsTemplatebyWidgetidAndType(Integer widgetId, TemplateType custom);

}
