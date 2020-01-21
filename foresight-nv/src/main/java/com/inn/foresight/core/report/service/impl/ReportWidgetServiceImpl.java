package com.inn.foresight.core.report.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.exceptions.application.ValidationFailedException;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IReportWidgetDao;
import com.inn.foresight.core.report.model.ReportWidget;
import com.inn.foresight.core.report.service.IReportWidgetService;
import com.inn.foresight.core.report.wrapper.ReportWidgetWrapper;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/**
 * The Class ReportWidgetServiceImpl.
 */
@Service("ReportWidgetServiceImpl")
@Transactional
public class ReportWidgetServiceImpl extends AbstractService<Integer, ReportWidget> implements IReportWidgetService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(ReportWidgetServiceImpl.class);

	/** The i reportwidget dao. */
	@Autowired
	private IReportWidgetDao dao;
	
	/**
	 * Instantiates a new report widget service impl.
	 */
	public ReportWidgetServiceImpl() {
		super();
	}

	/**
	 * Instantiates a new report widget service impl.
	 *
	 * @param dao the dao
	 */
	@Autowired
	public ReportWidgetServiceImpl(IReportWidgetDao dao) {
		super();
		super.setDao(dao);
		this.dao = dao;
	}
	
	/**
	 * Search.
	 *
	 * @param entity the entity
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<ReportWidget> search(ReportWidget entity) {
		logger.info("Finding  widget list by widget :{} ", entity);
		return super.search(entity);
	}

	/**
	 * Find by id.
	 *
	 * @param primaryKey the primary key
	 * @return the report widget
	 * @throws RestException the rest exception
	 */
	@Override
	public ReportWidget findById(Integer primaryKey) {
		return super.findById(primaryKey);
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<ReportWidget> findAll() {
		try {
			return super.findAll();
		} catch (EmptyResultDataAccessException ex) {
			logger.error(ex.getMessage());
			throw new RestException(ex);
		} 
	}

	/**
	 * Creates the.
	 *
	 * @param anEntity the an entity
	 * @return the report widget
	 * @throws RestException the rest exception
	 */
	@Override
	public ReportWidget create(ReportWidget anEntity) {
		logger.info("Creating  widget by an widget :{} ", anEntity);
		try {
			return super.create(anEntity);
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			throw new ValidationFailedException(ex);

		} catch (ConstraintViolationException ex) {
			logger.error(ex.getMessage());
			throw new RestException(ex);
		}
	}

	/**
	 * Update.
	 *
	 * @param anEntity the an entity
	 * @return the report widget
	 * @throws RestException the rest exception
	 */
	@Override
	public ReportWidget update(ReportWidget anEntity) {
		logger.info("Updating  reportwidget by an widget :{} ", anEntity);
		try {
			if(anEntity!=null && anEntity.getConfiguration()!=null) {
				anEntity.setConfiguration(new Gson().toJson(anEntity.getConfiguration()));
			}
			anEntity.setModifiedTime(new Date());
			return super.update(anEntity);
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			throw new ValidationFailedException(ex);
		} catch (ConstraintViolationException ex) {
			logger.error(ex.getMessage());
			throw new RestException(ex);
		}
	}

	/**
	 * Removes the.
	 *
	 * @param anEntity the an entity
	 * @throws RestException the rest exception
	 */
	@Override
	public void remove(ReportWidget anEntity) {
		logger.info("Removing widget by an widget :{} ",anEntity);
		super.remove(anEntity);

	}

	/**
	 * Removes the by id.
	 *
	 * @param primaryKey the primary key
	 * @throws RestException the rest exception
	 */
	@Override
	public void removeById(Integer primaryKey) {
		logger.info("Removing  Widget by primaryKey {} ", + primaryKey);
		try {
			dao.deleteByPk(primaryKey);
		}catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			throw new ValidationFailedException(ex);
		} catch (DaoException | ConstraintViolationException ex) {
			logger.error(ex.getMessage());
			throw new RestException(ex);
		}

	}

	/**
	 * Search with limit.
	 *
	 * @param context the context
	 * @param maxLimit the max limit
	 * @param minLimit the min limit
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<ReportWidget> searchWithLimit(SearchContext context,
			Integer maxLimit, Integer minLimit) {
		return dao.search(context, maxLimit, minLimit);
	}

	/**
	 * Search with limit and order by.
	 *
	 * @param ctx the ctx
	 * @param maxLimit the max limit
	 * @param minLimit the min limit
	 * @param orderby the orderby
	 * @param orderType the order type
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<ReportWidget> searchWithLimitAndOrderBy(SearchContext ctx,
			Integer maxLimit, Integer minLimit, String orderby, String orderType) {
		return dao.search(ctx, maxLimit, minLimit, orderby,
				orderType);

	}

	/**
	 * Find audit.
	 *
	 * @param pk the pk
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<JSONObject> findAudit(Integer pk) {
		return dao.findAudit(pk);
	}
	
	/**
	 * Save CM comparision report.
	 *
	 * @param reportWidgetWrapper the report widget wrapper
	 * @return the report widget wrapper
	 * @throws RestException the rest exception
	 */
	@Override
	public ReportWidgetWrapper saveCMComparisionReport(ReportWidgetWrapper reportWidgetWrapper) {
		try {
			ReportWidget reportWidget = reportWidgetWrapper.toReportWidget();
			/*reportWidget.setCreationTime(DateUtils.currentDate());
			reportWidget.setModifiedTime(DateUtils.currentDate());
			reportWidget.setCreator(UserContextServiceImpl.getUserInContext());
			ReportInfoWrapper info = ReportUtils.parseConfigToWrapper(reportWidget.getConfiguration());
			logger.info("Going to save report for save CM Comparision Report report : {}", reportWidget);
			reportWidget.setIsDeleted(false);
			ReportWidgetWrapper widgetWrapper = dao.create(reportWidget).toReportWidgetWrapper();
			logger.info("going to create cm comparision report, for widget id {}", widgetWrapper.getId());
			info.setReportWidgetId(widgetWrapper.getId());*/
			return null;
		} catch (Exception e) {
			logger.error("Error in saving report {}",Utils.getStackTrace(e));
			throw new RestException("Error in saving report");
		}
	}
	
	/**
	 * Soft delete widget by id.
	 *
	 * @param id the id
	 * @return the string
	 * @throws RestException the rest exception
	 */
	@Override
	public String softDeleteWidgetById(Integer id) {
		logger.info("Inside @class {} , @method softDeleteWidgetById  For Id {}"
				+ this.getClass().getName() + id);
		Integer noOfRecordsDeleted = dao.softDeleteWidgetById(id);
		if (noOfRecordsDeleted != null && noOfRecordsDeleted > 0) {
			return "Report Deleted Successfully";
		}
		return "No Report Present to delete";
	}

	/**
	 * Creates the report.
	 *
	 * @param reportWidgetWrapper the report widget wrapper
	 * @return the report widget wrapper
	 * @throws RestException the rest exception
	 */
	@Override
	public ReportWidgetWrapper createReport(ReportWidgetWrapper reportWidgetWrapper) {
		try {
			ReportWidget reportWidget = reportWidgetWrapper.toReportWidget();
			logger.info("createReport  ReportWidget : {} ",reportWidget.toString());
			reportWidget.setCreationTime(new Date());
			reportWidget.setModifiedTime(new Date());
			reportWidget.setIsDeleted(false);
			reportWidget.setCreator(UserContextServiceImpl.getUserInContext());
			if(reportWidgetWrapper!=null && reportWidgetWrapper.getConfiguration()!=null) {
				reportWidget.setConfiguration(new Gson().toJson(reportWidgetWrapper.getConfiguration()));
			}
			logger.info("Going to save report : {}", reportWidget);
			ReportWidgetWrapper widgetWrapper = dao.create(reportWidget).toReportWidgetWrapper();
			logger.info("create report  for widget id {}", widgetWrapper.getId());
		return widgetWrapper;
		} catch (DaoException e) {
			logger.error("DaoException in saving report {}",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_UNABLE_TO_CREATE_REPORT);
		} catch (Exception e) {
			logger.error("Exception in saving report {}",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_UNABLE_TO_CREATE_REPORT);
		}
	}
	
	@Override
	public List<ReportWidget> getReportWidgetByFilter(String reportName,String reportMeasure,String reportType,Long date,Integer lowerLimit,Integer upperLimit){
		List<ReportWidget> reportList = new ArrayList<>();
		String dateStr= null;
		try {
			logger.info("going to get report by reportName {} reportMeasure {} reportType {} date {}",reportName, reportMeasure, reportType, date);
			User user = UserContextServiceImpl.getUserInContext();
			if(date!=null) {
				dateStr= DateUtil.parseDateToString("dd-MM-yyyy", new Date(date));
			}
			reportList = dao.getReportWidgetByFilter(reportName,reportMeasure,reportType,user.getUserid(),dateStr,lowerLimit,upperLimit);
		} catch (RestException e){
			throw new RestException(e.getMessage());
		} catch (Exception e){
			logger.error("Exception in getReportWidgetByFilter :reportName {} reportMeasure {} reportType {} date {}  err:{}",reportName, reportMeasure, reportType, date,e.getMessage());
		}
		return reportList;
	}

	@Override
	public List<String> getAllReportMeasure(){
		try {
			logger.info("Going to getAllReportMeasure ");
			return dao.getAllReportMeasure();
		} catch (Exception e) {
			throw new RestException("unable to get All ReportMeasure err={} ",e.getMessage());
		}
	}

	@Override
	public List<String> getAllReportTypeByReportMeasure(String reportMeasure) {
		try {
			logger.info("Going to getAllReportTypeByReportMeasure :reportMeasure={} ",reportMeasure);
			return dao.getAllReportTypeByReportMeasure(reportMeasure);
		} catch (Exception e) {
			throw new RestException("unable to get All ReportType By ReportMeasure err={} ",e.getMessage());
		}
	}	
	
	@Override
	public Long getReportWidgetCountByFilter(String reportName,String reportMeasure,String reportType,Long date){
		Long count = 0L ;
		try {
			String dateStr=null;
			logger.info("going to get report by reportName {} reportMeasure {} reportType {} date {}",reportName, reportMeasure, reportType, date);
			User user = UserContextServiceImpl.getUserInContext();
			if(date!=null) {
				dateStr= DateUtil.parseDateToString("dd-MM-yyyy", new Date(date));
			}
			count = dao.getReportWidgetCountByFilter(reportName,reportMeasure,reportType,user.getUserid(),dateStr);
		} catch (RestException e){
			throw new RestException(e.getMessage());
		} catch (Exception e){
			logger.error("Exception in getReportWidgetCountByFilter :reportName {} reportMeasure {} reportType {} date {}  err:{}",reportName, reportMeasure, reportType, date,e.getMessage());
		}
		return count;
	}

	@Override
	public List<String> getAllWidgetReportType() {
		try {
			logger.info("Going to getAllWidgetReportType " );
			return dao.getAllWidgetReportType();
		} catch (Exception e) {
			throw new RestException("unable to get All Widget ReportType err={} ",e.getMessage());
		}
	}
}