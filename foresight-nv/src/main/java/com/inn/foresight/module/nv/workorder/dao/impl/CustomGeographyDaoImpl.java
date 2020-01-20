package com.inn.foresight.module.nv.workorder.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.ICustomGeographyDao;
import com.inn.foresight.module.nv.workorder.model.CustomGeography;
import com.inn.foresight.module.nv.workorder.model.CustomGeography.GeographyType;
import com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper;

/** The Class CustomGeographyDaoImpl. */
@Repository("CustomGeographyDaoImpl")
public class CustomGeographyDaoImpl extends HibernateGenericDao<Integer, CustomGeography>
		implements ICustomGeographyDao {
	
	private Logger logger = LogManager.getLogger(CustomGeographyDaoImpl.class);

	/** Instantiates a new custom geography dao impl. */
	public CustomGeographyDaoImpl() {
		super(CustomGeography.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomGeography> findAllCustomGeographyForUser(Integer userId){
		logger.info("Going to get Custom Geography for user id {}", userId);
		try{
			Query query = getEntityManager().createNamedQuery("findAllCustomGeographyForUser");
			query.setParameter(NVWorkorderConstant.USER_ID, userId);
			return query.getResultList();
		} catch (Exception e){
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomGeographyWrapper> findAllCustomGeography(Integer geographyId, String geographyType,
			List<GeographyType> typeList) {
		logger.info("Going to get Custom Geography for geographyId {}, geographyType {}, typeList {}", geographyId, geographyType, typeList);
		try{ 
			Query query = getNamesQueryFromGeographyType(geographyType, geographyId, typeList);
			return query.getResultList();
		} catch (Exception e){ 
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}

	private Query getNamesQueryFromGeographyType(String geographyType, Integer geographyId, List<GeographyType> typeList) {
		Query query = null;
		if(geographyType != null && geographyId != null) {
			if(geographyType.equals(ForesightConstants.GEOGRAPHY_L1)) {
				query = getEntityManager().createNamedQuery("findAllCustomGeographyL1");
			} else if (geographyType.equals(ForesightConstants.GEOGRAPHY_L2)) {
				query =  getEntityManager().createNamedQuery("findAllCustomGeographyL2");
			} else if (geographyType.equals(ForesightConstants.GEOGRAPHY_L3)) {
				query =  getEntityManager().createNamedQuery("findAllCustomGeographyL3");
			} else {
				query = getEntityManager().createNamedQuery("findAllCustomGeographyL4");
			}
			query.setParameter("geographyId", geographyId);
		} else {
			query = getEntityManager().createNamedQuery("findAllCustomGeography");
		}
		query.setParameter("typeList", typeList);
		return query;
	}

}
