package com.inn.foresight.core.infra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.dao.IGeographyMappingDao;
import com.inn.foresight.core.infra.model.GeographyMapping;
import com.inn.foresight.core.infra.utils.InfraConstants;

/**
 * The Class GeographyMappingDaoImpl.
 */
@Repository("GeographyMappingDaoImpl")
public class GeographyMappingDaoImpl extends HibernateGenericDao<Integer, GeographyMapping> implements IGeographyMappingDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(GeographyMappingDaoImpl.class);

	/**
	 * Instantiates a new geography mapping dao impl.
	 */
	public GeographyMappingDaoImpl() {
		super(GeographyMapping.class);
	}

	/**
	 * Insert geography mapping data in oracle.
	 *
	 * @param geographyName the geography name
	 * @param duplicateName the duplicate name
	 * @param type the type
	 * @throws DaoException the dao exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void insertGeographyMappingDataInOracle(String geographyName, String duplicateName, String type) {
		logger.info("Going to insert data Into GeographyMapping table for geographyName {} duplicateName {} and type {} ", geographyName, duplicateName, type);
		GeographyMapping geographyMapping = null;
		try {
			if (geographyName != null && duplicateName != null && type != null) {
				geographyMapping = new GeographyMapping();
				geographyMapping.setGeographyName(geographyName);
				geographyMapping.setDuplicateName(duplicateName);
				geographyMapping.setType(type);
				create(geographyMapping);
			} else {
				throw new DaoException(InfraConstants.EXCEPTION_SOMETHING_WENT_WRONG);
			}
		} catch (Exception exception) {
			logger.error("Error while inserting data Into GeographyMapping table for geographyName {} duplicateName {} and type {} Exception {} ", geographyName, duplicateName, type,
					ExceptionUtils.getStackTrace(exception));
			throw new DaoException(exception.getMessage());
		}
	}

	/**
	 * Gets the all geography name by geography name.
	 *
	 * @param geographyName the geography name
	 * @return the all geography name by geography name
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllGeographyNameByGeographyName(String geographyName) {
		logger.info("Going to fetch data from GeographyMapping table for geographyName {} ", geographyName);
		List<String> geographyNameList = null;
		try {
			if (geographyName != null) {
				Query query = getEntityManager().createNamedQuery("getAllGeographyNameByGeographyName");
				query.setParameter(InfraConstants.GEOGRAPHYNAME, geographyName);
				geographyNameList = query.getResultList();
				logger.info("geographyNameList {} ", geographyNameList);
			} else {
				throw new DaoException("Unable to fetch data from GeographyMapping table for geographyName " + geographyName);
			}
		} catch (Exception exception) {
			logger.error("Error while fetching data from GeographyMapping for geographyName {}  Exception {} ", geographyName, ExceptionUtils.getStackTrace(exception));
			throw new DaoException(exception.getMessage());
		}
		return geographyNameList;
	}

}
