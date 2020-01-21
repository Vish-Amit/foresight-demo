package com.inn.foresight.core.infra.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IAdvanceSearchConfigurationDao;
import com.inn.foresight.core.infra.model.AdvanceSearchConfiguration;

@Repository("AdvanceSearchConfigurationDaoImpl")
public class AdvanceSearchConfigurationDaoImpl  extends HibernateGenericDao<Integer, AdvanceSearchConfiguration> implements IAdvanceSearchConfigurationDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(AdvanceSearchConfigurationDaoImpl.class);

	/**
	 * Instantiates a new team dao impl.
	 */
	public AdvanceSearchConfigurationDaoImpl() {
		super(AdvanceSearchConfiguration.class);
	}

	@Override
	public String getBeanNameByType(String type) 
	{
		logger.info("Inside getBeanNameByType : {}", type);
		String beanName = ForesightConstants.BLANK_STRING;
		try {
			Query query = getEntityManager().createNamedQuery("getBeanNameByType");
			query.setParameter("type", type);
			beanName =  (String)query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("NoResultException while getting bean name by type {} ", noResultException.getMessage());
			
		} catch (NonUniqueResultException noUniqueResultException) {
			logger.error("NonUniqueResultException while getting bean name by type {} ", noUniqueResultException.getMessage());
			
		} catch (Exception e) {
			logger.error("Exception while getting bean name by type {} ", Utils.getStackTrace(e));
			
		}

		return beanName;
	}

	@Override
	public AdvanceSearchConfiguration getAdvanceSearchConfigurationByType(String searchFieldType) {
		logger.info("Going to get AdvanceSearchConfiguration by type: {} ",searchFieldType);
		try {
			Query query = getEntityManager().createNamedQuery("getAdvanceSearchConfigurationByType");
			query.setParameter("type", searchFieldType);
			AdvanceSearchConfiguration advanceSearchConfiguration = (AdvanceSearchConfiguration) query.getSingleResult();
			logger.info("advanceSearchConfiguration: {}",advanceSearchConfiguration);
			return advanceSearchConfiguration;
		}catch (NoResultException noResultException) {
			logger.error("NoResultException while getting AdvanceSearchConfiguration name by type {} ", noResultException.getMessage());
		} catch (NonUniqueResultException noUniqueResultException) {
			logger.error("NonUniqueResultException while getting AdvanceSearchConfiguration name by type {} ", noUniqueResultException.getMessage());
		} catch (Exception e) {
			logger.error("Exception while getting AdvanceSearchConfiguration name by type {} ", Utils.getStackTrace(e));
		}
		return null;
	}

}
