package com.inn.foresight.core.infra.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IFloorPlanMappingDao;
import com.inn.foresight.core.infra.model.FloorPlanMapping;

@Repository("FloorPlanMappingDaoImpl")
public class FloorPlanMappingDaoImpl extends HibernateGenericDao<Integer, FloorPlanMapping>
		implements IFloorPlanMappingDao {
	private Logger logger = LogManager.getLogger(FloorPlanMappingDaoImpl.class);

	public FloorPlanMappingDaoImpl() {
		super(FloorPlanMapping.class);
	}

	@Override
	public FloorPlanMapping getFloorplanMappingByUnitIdAndTemplate(Integer unitId, String template) {
		logger.info("Going to getFloorplanMappingByUnitIdAndTemplate {} ,template {}",unitId,template);
		try {
			Query query = this	.getEntityManager()
								.createNamedQuery("getFloorplanMappingByUnitIdAndTemplate")
								.setParameter("unitId", unitId)
								.setParameter("templateType", template);
			return (FloorPlanMapping) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("NoResultException inside getFloorplanMappingByUnitIdAndTemplate {}", Utils.getStackTrace(e));
			throw e;
		}catch(Exception e) {
			logger.error("Exception inside getFloorplanMappingByUnitIdAndTemplate {}", Utils.getStackTrace(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FloorPlanMapping> getFloorplanMappingByUnitId(Integer unitId) {
		logger.info("Going to getFloorplanMappingByUnitId {} ",unitId);
		try {
			Query query = this	.getEntityManager()
								.createNamedQuery("getFloorplanMappingByUnitId")
								.setParameter("unitId", unitId);
			return query.getResultList();
		} catch (NoResultException e) {
			logger.error("NoResultException inside getFloorplanMappingByUnitId {}", Utils.getStackTrace(e));
			throw e;
		}catch(Exception e) {
			logger.error("Exception inside getFloorplanMappingByUnitId {}", Utils.getStackTrace(e));
		}
		return null;
	}

}
