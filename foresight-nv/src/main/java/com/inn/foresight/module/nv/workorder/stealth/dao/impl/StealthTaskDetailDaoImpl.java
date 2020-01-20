package com.inn.foresight.module.nv.workorder.stealth.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskDetailDao;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskDetail;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper;

@Repository("StealthTaskDetailDaoImpl")
public class StealthTaskDetailDaoImpl extends HibernateGenericDao<Integer, StealthTaskDetail>
		implements IStealthTaskDetailDao {
	/** The logger. */
	private Logger logger = LogManager.getLogger(StealthTaskDetailDaoImpl.class);

	public StealthTaskDetailDaoImpl() {
		super(StealthTaskDetail.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StealthTaskDetail getStealthTaskDetailByDeviceId(Integer deviceId, Date date) {
		logger.info("Going to get Stealth Task for Device id {}, date: {}", deviceId, date);
		try {
			Query query = getEntityManager().createNamedQuery(StealthConstants.GET_STEALTH_TASK_DETAIL_BY_DEVICE_ID)
					.setParameter(DeviceConstant.DEVICE_ID_PLACE_HOLDER, deviceId)
					.setParameter(ACKNOWLEDGEMENT, StealthConstants.STATUS_PENDING).setParameter(CREATION_TIME, date);
			List<StealthTaskDetail> details = query.getResultList();
			if (details != null && !details.isEmpty())
				return details.get(0);
			else
				return null;
		} catch (Exception exception) {
			logger.error("Unable to get Stealth task detail for device id {} exception {}", deviceId,
					Utils.getStackTrace(exception));
			throw new DaoException("Unable to getStealthTask " + exception.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public StealthTaskDetail getStealthTaskByDeviceAndWOId(Integer deviceId, Integer woId) throws Exception {
		logger.info("Inside getTaskByDeviceAndWOId");
		StealthTaskDetail taskDetail = null;
		try {
			if (deviceId != null && woId != null) {
				Query query = getEntityManager()
						.createNamedQuery(StealthConstants.GET_STEALTH_TASK_BY_DEVICE_AND_WO_ID);
				query.setParameter(StealthConstants.DEVICE_ID, deviceId);
				query.setParameter(StealthConstants.WORKORDER_ID, woId);
				List<StealthTaskDetail> taskList = query.getResultList();
				logger.info("Done getDevicesByWorkOrderId");
				if (taskList != null && !taskList.isEmpty()) {
					taskDetail = taskList.get(ForesightConstants.ZERO_INT);
					
				} else {
					throw new DaoException("No Result found for deviceId: " + deviceId + " & woId: " + woId);
				}
			}
		} catch (NoResultException e) {
			logger.error("No Result found for deviceId: {} & workorderId : {}", deviceId, woId);
			throw new DaoException(e.getMessage());
		}
		return taskDetail;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getNVDeviceListByWOId(Integer woId) {
		logger.info("Going to get NvDevice List for WOID {}", woId);
		try {
			Query query = getEntityManager().createNamedQuery("getNVDeviceListByWOId").setParameter("woId", woId);
			return query.getResultList();
		} catch (Exception exception) {
			logger.error("Error in getting NVDevice List For Work Order {} Exception {} ", woId,
					Utils.getStackTrace(exception));
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GenericWorkorder> getWOListByDeviceIdAndModificationtime(String deviceId,Long modificationTime,Integer userId) {
		try {
			logger.info("Going to get Generic WorkOrder List for DeviceID {} ", deviceId);
			Query query = getEntityManager().createNamedQuery("getWOListByDeviceId");
			query.setParameter("deviceId", deviceId);
			query.setParameter("modificationTime", new Date(modificationTime));
			query.setParameter("userId", userId);
			return query.getResultList();

		} catch (Exception exception) {
			logger.error("Error in getting generic Workorder List for Device id {} Exception {} ", deviceId,
					Utils.getStackTrace(exception));
		}
		return Collections.emptyList();

	}


	@SuppressWarnings("unchecked")
	@Override
	public List<StealthTaskDetail> getStealthTasksByWorkorderId(Integer workorderId) {
		logger.info("Inside getStealthTasksByWorkorderId");
		try {
			Query query = getEntityManager().createNamedQuery("getStealthTasksByWorkorderId");
			query.setParameter(DeviceConstant.WORKORDER_ID, workorderId);
			logger.info("Done getStealthTasksByWorkorderId");
			return query.getResultList();
		} catch (NoResultException e) {
			logger.error("No Result found for workorderId : {}", workorderId);
			throw new DaoException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StealthWOWrapper> getAcknowledgementSummary(Integer woId, String type) {
		List<StealthWOWrapper> taskList = new ArrayList<>();
		try {
			Query query = null;
			if (NVConstant.HOURLY.equalsIgnoreCase(type)) {
				query = getEntityManager().createNamedQuery("getHourlyAcknowledgementSummary").setParameter("woId",
						woId);
			}else {
			query = getEntityManager().createNamedQuery("getAcknowledgementSummary").setParameter("woId", woId);
			}
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getAcknowledgementSummary ", ExceptionUtils.getStackTrace(e));
		}
		return taskList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StealthWOWrapper> getStealthWOWrapperListByWOId(Integer woId) {
		logger.info("Going to get getStealthWOWrapperListByWOId :{} ", woId);
		try {
			Query query = getEntityManager().createNamedQuery(StealthConstants.GET_WORKORDERS_BY_WOID_AND_DATE)
					.setParameter(StealthConstants.WORKORDER_ID, woId);

			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in method getStealthWOWrapperListByWOId {}", Utils.getStackTrace(e));
		}
		logger.info("Empty List is return From getStealthWOWrapperListByWOId {}", woId);
		return Collections.emptyList();

	}

}
