package com.inn.foresight.core.ztepower.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.ztepower.dao.IZTEPowerStationDao;
import com.inn.foresight.core.ztepower.model.ZTEPowerStation;

@Repository("ZTEPowerStationDaoImpl")
public class ZTEPowerStationDaoImpl extends  HibernateGenericDao<Integer, ZTEPowerStation> implements IZTEPowerStationDao {

	Logger logger = LogManager.getLogger(ZTEPowerStationDaoImpl.class);
	public ZTEPowerStationDaoImpl() {
		super(ZTEPowerStation.class);
	}
	@Override
	public ZTEPowerStation getZTEPowerStationByStationId(Integer id) {
		logger.info("Going to get ZTEPowerStation data for station id :{}", id);
		try {
			Query query = getEntityManager().createNamedQuery("getZTEPowerStationByStationId").setParameter("id", id);
			return (ZTEPowerStation) query.getSingleResult();
		} catch (NoResultException | IllegalStateException | QueryTimeoutException	| TransactionRequiredException e) {
			logger.warn("Error while getZTEPowerStationByStationId,err msg{}", Utils.getStackTrace(e));
		}
		return null;
	}
	@Override
	public List<Integer> getNEIdListByStationId(Integer id) {
		logger.info("Going to get NetworkElement Id list for station id :{}", id);
		try {
			Query query = getEntityManager().createNamedQuery("getNEIdListByStationId").setParameter("id", id);
			return (List<Integer>) query.getResultList();
		} catch (NoResultException | IllegalStateException | QueryTimeoutException	| TransactionRequiredException e) {
			logger.warn("Error while getNEIdListByStationId,err msg{}", Utils.getStackTrace(e));
		}
		return null;
	}
}
