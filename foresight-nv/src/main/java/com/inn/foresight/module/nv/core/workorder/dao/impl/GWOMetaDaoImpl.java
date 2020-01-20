package com.inn.foresight.module.nv.core.workorder.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.core.workorder.dao.IGWOMetaDao;
import com.inn.foresight.module.nv.core.workorder.model.GWOMeta;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;


@Repository("GWOMetaDaoImpl")
public class GWOMetaDaoImpl  extends HibernateGenericDao<Integer,GWOMeta> implements IGWOMetaDao {

	public GWOMetaDaoImpl() {
		super(GWOMeta.class);
	}

	private static final Logger logger = LogManager.getLogger(GWOMetaDaoImpl.class);


	@Override
	public GWOMeta getGwoMetaDataByGenericWorkorderId(Integer genericWorkorderId,String entityType) {
		try {
			Query query = getEntityManager().createNamedQuery("findMetaByGenericWorkOrderId").setParameter("genericWorkOrderId", genericWorkorderId).setParameter("entityType", entityType);
			return (GWOMeta) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No result found for Genericworkorder {} ",genericWorkorderId);
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		} 
	}
	
	@Override
	public GWOMeta getGwoMetaDataByGenericWorkorderIdforReport(Integer genericWorkorderId,String entityType) {
		GWOMeta meta = null;
		try {
			Query query = getEntityManager().createNamedQuery("findMetaByGenericWorkOrderId").setParameter("genericWorkOrderId", genericWorkorderId).setParameter("entityType", entityType);
			return (GWOMeta) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No result found for Genericworkorder {} ",genericWorkorderId);
			return meta;
		} 
	}



	
	
}
