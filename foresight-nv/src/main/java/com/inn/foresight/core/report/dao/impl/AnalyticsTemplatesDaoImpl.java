package com.inn.foresight.core.report.dao.impl;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.report.dao.IAnalyticsTemplatesDao;
import com.inn.foresight.core.report.model.AnalyticsTemplates;
import com.inn.foresight.core.report.model.AnalyticsTemplates.TemplateType;
@Repository("AnalyticsTemplatesDaoImpl")
public class AnalyticsTemplatesDaoImpl extends HibernateGenericDao<Integer, AnalyticsTemplates> implements IAnalyticsTemplatesDao {

	public AnalyticsTemplatesDaoImpl() {
		super(AnalyticsTemplates.class);
	}

	private Logger logger=LoggerFactory.getLogger(AnalyticsTemplatesDaoImpl.class);
	
	@Override
	public AnalyticsTemplates create(AnalyticsTemplates template) {
		logger.info("Inside  @class :AnalyticsTemplatesDaoImpl @Method :create @Param: template "+template);
			return super.create(template);
	}
	

	@Override
	public AnalyticsTemplates update(AnalyticsTemplates template)   {
		logger.info("Inside  @class :AnalyticsTemplatesDaoImpl @Method :create @Param: template "+template);
		return super.update(template);
	}
	
	@Override
	public AnalyticsTemplates getAnalyticsTemplatebyWidgetidAndType(Integer widgetId,TemplateType type)   {
		logger.info("Inside get Analytics Template by  widgetId {} and type  {}  ",widgetId,type);
		try {
			Query query = getEntityManager().createNamedQuery("getAnalyticsTemplatebyWidgetIdAndType");
			query.setParameter("widgetId", widgetId);
			query.setParameter("type", type);
			return (AnalyticsTemplates)query.getSingleResult();
		}catch(Exception e) {
			logger.error("Exception while find AnalyticsTemplates by  widgetId {} ",e);
		}
		return null;
	}
	
}
