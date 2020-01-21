package com.inn.foresight.core.infra.dao.impl;

import java.util.List;

import javax.persistence.Query;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.INeighbourCellDetailDao;
import com.inn.foresight.core.infra.model.NeighbourCellDetail;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.Vendor;

@Repository("NeighbourCellDetailDaoImpl")
public class NeighbourCellDetailDaoImpl extends  HibernateGenericDao<Integer, NeighbourCellDetail> 
		implements INeighbourCellDetailDao  {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NeighbourCellDetailDaoImpl.class);
	
	/**
	 * Instantiates a new neighbourCellDetail dao.
	 */
	public NeighbourCellDetailDaoImpl() {
		super(NeighbourCellDetail.class);
	}

	@Override
	public NeighbourCellDetail update(@Valid NeighbourCellDetail neighbourCellDetail) {
		logger.info("update record by an neighbourCellDetail :{}", neighbourCellDetail);
		try {
			return super.update(neighbourCellDetail);
		} catch (DaoException e) {
			logger.error("Exception while update record");
		}
		return neighbourCellDetail;
	}

	@Override
	public void delete(NeighbourCellDetail neighbourCellDetail) {
		logger.info("Deleting record by an neighbourCellDetail :{}", neighbourCellDetail);
		try {
			super.delete(neighbourCellDetail);
		} catch (DaoException e) {
			logger.error("Exception while deleting records:{}", Utils.getStackTrace(e));
		}
	}

	@Override
	public void deleteByPk(Integer widgetPk) {
		logger.info("Deleting record by primary key :{}", widgetPk);
		try {
			super.deleteByPk(widgetPk);
		} catch (DaoException e) {
			logger.error("Exception while delete record by pk:{}", Utils.getStackTrace(e));
		}
	}

	@Override
	public List<NeighbourCellDetail> findAll() {
		try {
			return super.findAll();
		} catch (DaoException e) {
			logger.error("Exception while getting all records:{}", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public NeighbourCellDetail findByPk(Integer pk) {
		logger.info("Find record by Primary Key :{}", pk);
		try {
			return super.findByPk(pk);
		} catch (DaoException e) {
			logger.error("Exception while getting records by pk :{}", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<NeighbourCellDetail> getNeighbourCellDetailsForSourceCells(List<String> cellName,
			Integer weekNo) {
		Query query = getEntityManager().createNamedQuery("getNeighbourCellDetailsForSourceCells");
		query.setParameter("weekNo", weekNo);
		query.setParameter("cellName", cellName);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getNeighbourCellDetailIdByNEId(Integer networkelementid_fk) {
		logger.info("Going to get Neighbourcell detail by Network Element id.");
		try {
		Query query = getEntityManager().createNamedQuery("getNeighbourCellDetailIdByNEId");
		query.setParameter("networkelementid_fk", networkelementid_fk);
		return (List<Integer>)query.getResultList();
		}catch(Exception exception) {
			logger.warn("Error while getting NeighbourCell Details by Networkelement id.,err msg{}", Utils.getStackTrace(exception));
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<RANDetail> getNeighbourCellDetails(List<Integer> networkElementidList,String vendor, String domain) {
		logger.info("going to NeighbourCell Detail Data.");
		 List<RANDetail> networkElementList=null;
		try {
			Query query = getEntityManager().createNamedQuery("getNeighbourCellDetails");
			query.setParameter("networkElementidList", networkElementidList);
			query.setParameter("domain", Domain.valueOf(domain));
			networkElementList = query.getResultList();
			logger.info("networkElementList: {}",networkElementList.size());
		} catch (Exception exception) {
			logger.warn("Error while getting NeighbourCell Details ,err msg{}", Utils.getStackTrace(exception));
		}
		return networkElementList;
	}

}
