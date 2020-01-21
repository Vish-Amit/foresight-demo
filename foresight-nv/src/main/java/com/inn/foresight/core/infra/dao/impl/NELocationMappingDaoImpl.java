package com.inn.foresight.core.infra.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.infra.dao.INELocationMappingDao;
import com.inn.foresight.core.infra.model.NELocation;
import com.inn.foresight.core.infra.model.NELocationMapping;
import com.inn.foresight.core.infra.utils.enums.NELType;

/**
 * The Class NELocationMappingDaoImpl.
 */
@Repository("NELocationMappingDaoImpl")
public class NELocationMappingDaoImpl extends HibernateGenericDao<Integer, NELocationMapping> implements INELocationMappingDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NELocationMappingDaoImpl.class);

	/**
	 * Instantiates a new NE location dao impl.
	 */
	public NELocationMappingDaoImpl() {
		super(NELocationMapping.class);
	}
	
	@Override
	public NELocation getParentNELocationByNELType(Integer nelocationid_fk, String neltype) {
		logger.info("inside @getParentNELocationByNELType method nelocationid :{}", nelocationid_fk);
		try {
			TypedQuery<NELocation> query = getEntityManager().createNamedQuery("getParentNELocationByNELType",
					NELocation.class);
			query.setParameter("nelocationid_fk", nelocationid_fk);
			query.setParameter("nelType", NELType.valueOf(neltype));
			return query.getSingleResult();
		} catch (NoResultException e) {
			logger.warn("No result found for nelocationid_fk :{}", nelocationid_fk);
			return null;
		} catch (Exception e) {
			logger.error("error while getting NELocation :{}", ExceptionUtils.getStackTrace(e));
			throw new BusinessException(ExceptionUtils.getMessage(e));
		}
	}

}
