package com.inn.foresight.core.report.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IReportWidgetDao;
import com.inn.foresight.core.report.model.ReportWidget;

/**
 * The Class ReportWidgetDaoImpl.
 */
@Repository("ReportWidgetDaoImpl")
public class ReportWidgetDaoImpl extends HibernateGenericDao<Integer, ReportWidget> implements IReportWidgetDao {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(ReportWidgetDaoImpl.class);

	/**
	 * Instantiates a new report widget dao impl.
	 */
	public ReportWidgetDaoImpl() {
		super(ReportWidget.class);
	}

	/**
	 * Creates the.
	 *
	 * @param reportWidget the report widget
	 * @return the report widget
	 * @throws DataAccessException the data access exception
	 * @throws DaoException the dao exception
	 */
	@Override
	public ReportWidget create(@Valid ReportWidget reportWidget) {
		return super.create(reportWidget);
	}
	
	/**
	 * Update.
	 *
	 * @param reportWidget the report widget
	 * @return the report widget
	 * @throws DataAccessException the data access exception
	 * @throws DaoException the dao exception
	 */
	@Override
	public ReportWidget update(@Valid ReportWidget reportWidget){

		return super.update(reportWidget);
	}

	/**
	 * Method to remove reportWidget record.
	 *
	 * @param reportWidget the report widget
	 * @throws DataAccessException the data access exception
	 * @throws DaoException the dao exception
	 * @parameter reportWidget of type ReportWidget
	 */

	@Override
	public void delete(ReportWidget reportWidget){
		super.delete(reportWidget);
	}

	/**
	 * Method to remove Widget record by primary key.
	 *
	 * @param reportWidgetPk the report widget pk
	 * @throws DataAccessException the data access exception
	 * @throws DaoException the dao exception
	 * @parameter primary key of type Integer
	 */

	@Override
	public void deleteByPk(Integer reportWidgetPk){
		super.deleteByPk(reportWidgetPk);
	}

	/**
	 * Returns the list of ReportWidget record.
	 *
	 * @return the list
	 * @throws NoResultException the no result exception
	 * @throws EmptyResultDataAccessException the empty result data access exception
	 * @throws DaoException the dao exception
	 * @returns eportWidget record
	 */

	@Override
	public List<ReportWidget> findAll() {
		return super.findAll();
	}

	/**
	 * Find by pk.
	 *
	 * @param reportWidgetPk the report widget pk
	 * @return the report widget
	 * @throws NoResultException the no result exception
	 * @throws EmptyResultDataAccessException the empty result data access exception
	 * @throws DaoException the dao exception
	 */
	@Override
	public ReportWidget findByPk(Integer reportWidgetPk){
		return super.findByPk(reportWidgetPk);
	}
	
	/**
	 * Soft delete widget by id.
	 *
	 * @param id the id
	 * @return the integer
	 * @throws RestException the rest exception
	 */
	@Override
	public Integer softDeleteWidgetById(Integer id) {
		logger.info("Inside @class {} , @method softDeleteWidgetById For Id {}" + this.getClass().getName() + id);
		try {
			Query query = getEntityManager().createNamedQuery("softDeleteWidgetById").setParameter("id", id);
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error("Inside @class {} ,@method softDeleteWidgetById For Id {} Message {} ", this.getClass().getName(), id, e.getMessage());
			throw new RestException("Unable to delete report");
		}
	}

	/**
	 * Soft delete widget by id.
	 *
	 * @param id the id
	 * @return the integer
	 * @throws RestException the rest exception
	 */
	@Override
	public Integer softDeleteWidgetByIds(List<Integer> ids) {
		logger.info("Inside @class {} , @method softDeleteWidgetById For Id {}", ids.size());
		try {
			List<List<Integer>> subLists = Lists.partition(ids, 999);
			int count = 0;
			for(List<Integer> idlist : subLists) {
				Query query = getEntityManager().createNamedQuery("softDeleteWidgetByIds").setParameter("id", idlist);
				count += query.executeUpdate();
			}
			logger.info("Updated reportwidget records {}", count);
			return count;
		} catch (Exception e) {
			logger.error("Inside @class {} ,@method softDeleteWidgetById For Id {} Message {} ", ids, e.getMessage());
			throw new RestException("Unable to delete report");
		}
	}

	@Override
	public List<ReportWidget> getReportWidgetByFilter(String reportName,String reportMeasure,String reportType,Integer userId,String date,Integer lowerLimit,Integer upperLimit){
		try {
				String dynamicQuery = "select r from ReportWidget r where r.creator.userid=:userId";
				if(reportName!=null) {
					dynamicQuery+= " and r.reportName=(:reportName)";
				}
				if(reportMeasure!=null) {
					dynamicQuery+= " and r.reportMeasure=(:reportMeasure)";
				}
				if(reportType!=null) {
					dynamicQuery+= " and r.reportType=(:reportType)";
				}
				if(date!=null) {
					dynamicQuery+= " and date_format(r.creationTime,'dd-MM-yyyy') =(:date)";
				}
				dynamicQuery+= " and r.isDeleted=false order by r.modifiedTime desc";
				Query query =getEntityManager().createQuery(dynamicQuery);
				query.setParameter("userId", userId);
				if(reportName!=null) {
					query.setParameter("reportName", reportName);
				}
				if(reportMeasure!=null) {
					query.setParameter("reportMeasure", reportMeasure);
				}
				if(reportType!=null) {
					query.setParameter("reportType", reportType);
				}
				if(date!=null) {
					query.setParameter("date", date);
				}
				if (upperLimit!=null && upperLimit >= 0) {
					query.setMaxResults(upperLimit - lowerLimit + 1);
				}

				if (lowerLimit!=null && lowerLimit >= 0) {
					query.setFirstResult(lowerLimit);
				}
				return query.getResultList();
		} catch (NoResultException e){
			logger.error("No Data Found for reportName {} ,reportMeasure {},reportType {}, userId{}, date{} ",reportName,reportMeasure,reportType, userId, date);
			throw new RestException("No Data Found") ;
		} catch (Exception e){
			throw new RestException("Unable to get reports {}"+Utils.getStackTrace(e));
		}
	}

	@Override
	public List<ReportWidget> getAllReportByReportMeasure(List<String> reportMeasure) {
		try {
			Query query =getEntityManager().createNamedQuery("getAllReportByReportMeasure");
			query.setParameter("reportMeasure", reportMeasure);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getAllReportByReportMeasure : error={}",e.getMessage());
			throw new RestException("Unable to getAllReportByReportMeasure error:{}"+Utils.getStackTrace(e));
		}
	}
	@Override
	public List<String> getAllReportMeasure() {
		try {
			Query query =getEntityManager().createNamedQuery("getAllReportMeasure");
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getAllReportMeasure : error={}",e.getMessage());
			throw new RestException("Unable to getAllReportMeasure error:{}"+Utils.getStackTrace(e));
		}
	}
	
	@Override
	public List<ReportWidget> getAllPmReportDomainWise(List<String> domain) {
		try {
			Query query =getEntityManager().createNamedQuery("getAllPmReportDomainWise");
			query.setParameter("domain", domain);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getAllReportMeasure : error={}",e.getMessage());
			throw new RestException("Unable to getAllReportMeasure error:{}"+Utils.getStackTrace(e));
		}
	}
	
	@Override
	public List<String> getAllReportTypeByReportMeasure(String reportMeasure) {
		try {
			Query query =getEntityManager().createNamedQuery("getAllReportTypeByReportMeasure");
			query.setParameter("reportMeasure", reportMeasure);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getAllReportTypeByReportMeasure forreportMeasure={}  error={}",reportMeasure,e.getMessage());
			throw new RestException("Unable to getAllReportTypeByReportMeasure error:{}"+Utils.getStackTrace(e));
		}
	}
	@Override
	public Long getReportWidgetCountByFilter(String reportName,String reportMeasure,String reportType,Integer userId,String date){
		try {
				String dynamicQuery = "select count(r.id) from ReportWidget r where r.creator.userid=:userId ";
				if(reportName!=null) {
					dynamicQuery+= " and r.reportName=:reportName";
				}
				if(reportMeasure!=null) {
					dynamicQuery+= " and r.reportMeasure=:reportMeasure";
				}
				if(reportType!=null) {
					dynamicQuery+= " and r.reportType=:reportType";
				}
				if(date!=null) {
					dynamicQuery+= " and date_format(r.creationTime,'dd-MM-yyyy') =(:date)";
				}
				logger.info("getReportWidgetCountByFilter dynamicQuery={} ",dynamicQuery);
				dynamicQuery+= " and r.isDeleted=false ";
				Query query =getEntityManager().createQuery(dynamicQuery);
				query.setParameter("userId", userId);
				if(reportName!=null) {
					query.setParameter("reportName", reportName);
				}
				if(reportMeasure!=null) {
					query.setParameter("reportMeasure", reportMeasure);
				}
				if(reportType!=null) {
					query.setParameter("reportType", reportType);
				}
				if(date!=null) {
					query.setParameter("date", date);
				}
				return (Long) query.getSingleResult();
		} catch (NoResultException e){
			logger.error("No Data Found for reportName {} ,reportMeasure {},reportType {}, userId{}, date{} ",reportName,reportMeasure,reportType, userId, date);
			throw new RestException("No Data Found") ;
		} catch (Exception e){
			throw new RestException("Unable to get reports {}"+Utils.getStackTrace(e));
		}
	}
	
	@Override
	public List<String> getAllWidgetReportType() {
		try {
			Query query =getEntityManager().createNamedQuery("getAllWidgetReportType");
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getAllWidgetReportType ");
			throw new RestException("Unable to getAllWidgetReportType error:{}"+Utils.getStackTrace(e));
		}
	}
	
}