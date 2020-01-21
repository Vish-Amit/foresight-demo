package com.inn.foresight.core.report.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;

/**
 * Dao .
 */
@Repository("AnalyticsRepositoryDaoImpl")
public class AnalyticsRepositoryDaoImpl extends HibernateGenericDao<Integer, AnalyticsRepository>
		implements IAnalyticsRepositoryDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(AnalyticsRepositoryDaoImpl.class);

	/**
	 * Instantiates a new cellDashboard dao impl.
	 */
	public AnalyticsRepositoryDaoImpl() {
		super(AnalyticsRepository.class);
	}

	/**
	 * Returns the new Dashboard record.
	 *
	 * @param Dashboard
	 *            the Dashboard
	 * @return the Dashboard
	 * @throws DataAccessException
	 *             the data access exception
	 * @parameter Dashboard of type Dashboard
	 * @returns a new Dashboard
	 */

	/**
	 * 
	 * Returns the new Dashboard record
	 * 
	 * @throws DaoException
	 * @parameter dashboard of type Dashboard
	 * @returns a new Dashboard
	 * 
	 */

	@Override
	public AnalyticsRepository create(@Valid AnalyticsRepository dashboard) {

		logger.info("Create record by an entity :" + dashboard);
		return super.create(dashboard);
	}

	/**
	 * 
	 * Returns the updated Dashboard record
	 * 
	 * @throws DaoException
	 * @parameter anEntity of type Dashboard
	 * @returns a updated Dashboard record
	 * 
	 */
	@Override
	public AnalyticsRepository update(@Valid AnalyticsRepository dashboard) {
		logger.info("update record by an entity of Id : " + dashboard.getId());
		return super.update(dashboard);
	}

	/**
	 * 
	 * Method to remove Dashboard record
	 * 
	 * @throws DaoException
	 * @parameter dashboard of type Dashboard
	 * 
	 */
	@Override
	public void delete(@Valid AnalyticsRepository dashboard) {
		logger.info("Deleting record by an entity :");
		super.delete(dashboard);
	}

	/**
	 * 
	 * Method to remove Dashboard record by primary key
	 * 
	 * @throws DaoException
	 * @parameter primary key of type Integer
	 * 
	 */
	@Override
	public void deleteByPk(@NotNull Integer integerPk) {
		logger.info("Deleting record by primary key :" + integerPk);
		super.deleteByPk(integerPk);
	}

	/**
	 * 
	 * Returns the list of Dashboard record
	 * 
	 * @throws DaoException
	 * @returns Dashboard record
	 * 
	 */
	@Override
	public List<AnalyticsRepository> findAll() {
		logger.info("Inside FindAll method in AnalyticsRepositoryDao");

		return super.findAll();
	}

	/**
	 * 
	 * Returns the record of Dashboard finding by primary key
	 * 
	 * @throws DaoException
	 * @parameter primary key of type Integer
	 * @returns a Dashboard record
	 * 
	 */
	@Override
	public AnalyticsRepository findByPk(@NotNull Integer integerPk) {
		logger.info("Find record by Primary Key :" + integerPk);

		return super.findByPk(integerPk);
	}

	@Override
	public AnalyticsRepository updateStatusById(Integer analyticsrepositoryId) {
		logger.info("Innside method updateStatusById  {} ", analyticsrepositoryId);
		try {
			AnalyticsRepository analyticsRepository = findByPk(analyticsrepositoryId);
			analyticsRepository.setProgress(progress.In_Progress);
			update(analyticsRepository);
			return analyticsRepository;

		} catch (Exception e) {
			logger.warn("Exception inside method updateStatusById for analyticsRepositoryId Id {} , {} ",
					analyticsrepositoryId, Utils.getStackTrace(e));
		}
		return null;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public AnalyticsRepository updateStatusInAnalyticsRepository(Integer analyticsReportId, String filePath,
			String type, progress progressType, String downLoadFileName) {
		logger.info("Innside method updateStatusInReportInstance for id {} , filePath {}  ", analyticsReportId,
				filePath);
		try {
			AnalyticsRepository analyticsRepository = findByPk(analyticsReportId);
			analyticsRepository.setProgress(progressType);
			if (downLoadFileName != null) {
				analyticsRepository.setDownloadFileName(downLoadFileName);
			}
			analyticsRepository.setFilepath(filePath);
			analyticsRepository.setStorageType(type);
			analyticsRepository.setModifiedTime(new Date());
			analyticsRepository.setQueueStatus(false);
			update(analyticsRepository);
			return analyticsRepository;
		} catch (Exception e) {
			logger.warn("Exception inside method updateStatusById for analyticsRepositoryId Id {} , {} ",
					analyticsReportId, Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<Object[]> getReportDetailsByUserQuery(String userQuery) {
		logger.info("Inside getReportDetailsByUserQuery, query {}", userQuery);
		try {
			Query query = getEntityManager().createNativeQuery(userQuery);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getReportByNameAndTime :{}", ExceptionUtils.getStackTrace(e));
			throw new DaoException(e);
		}
	}

	@Override
	public List<AnalyticsRepository> getReportInfoByName(String FileName) {
		List<AnalyticsRepository> resultList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getReportInfoByName").setParameter("FileName", FileName);
			resultList = query.getResultList();
			logger.info("List Size {}, {}", FileName, resultList.size());
		} catch (Exception e) {
			logger.error("Exception in getReportByName :{}", ExceptionUtils.getStackTrace(e));
			throw new DaoException(e);
		}
		return resultList;
	}



}
