package com.inn.foresight.module.nv.device.dao.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.dao.IDeviceDao;
import com.inn.foresight.module.nv.device.model.Device;

@Repository("DeviceDaoImpl")
public class DeviceDaoImpl extends HibernateGenericDao<Integer, Device> implements IDeviceDao {
	/** The logger. */
	private Logger logger = LogManager.getLogger(DeviceDaoImpl.class);

	/** Construct NVProfileDataDaoImpl object. */
	public DeviceDaoImpl() {
		super(Device.class);
	}

	@Override
	public Device getDeviceSpecificationByModel(String model)  {
		logger.info("Inside getDeviceSpecificationByModel");
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_SPECIFICATION_BY_MODEL);
			query.setParameter(DeviceConstant.MODEL_PLACE_HOLDER, model);
			query.setMaxResults(DeviceConstant.MAX_RESULT);
			Device deviceData = (Device) query.getSingleResult();
			logger.info("Done getDeviceByDeviceId");
			return deviceData;
		} catch (NoResultException e) {
			throw e;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Gets the map of model and device.
	 *
	 * @return the map of model and device
	 */
	@Override
	public Map<String, Device> getMapOfModelAndDevice() {
		Map<String, Device> deviceMap = new HashMap<>();
		try {
			List<Device> deviceList = getAllDeviceList();
			deviceList	.parallelStream()
						.forEach(device -> {
							String modelCode = device.getModelCode();
							if (modelCode != null) {
								List<String> modelCodeList = Arrays	.stream(modelCode.split(","))
																	.collect(Collectors.toList());
								modelCodeList	.parallelStream()
												.forEach(singleModelCode -> deviceMap.put(singleModelCode, device));
							}
						});

		} catch (Exception e) {
			logger.error("Error in getting brand and model from table : {}", ExceptionUtils.getStackTrace(e));
		}

		return deviceMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getAllDeviceList() {
		logger.info("Inside getAllDeviceList");
		List<Device> deviceList;
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_ALL_DEVICE_LIST);
			deviceList = query.getResultList();
			logger.info("Done getAllDeviceList");
		} catch (NoResultException e) {
			throw e;
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return deviceList;
	}
	
	@Override
	public Device getDeviceDetailByDeviceName(String modelName)  {
		logger.info("Inside getDeviceDetailByDeviceName");
		Device device = null;
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_DETAIL_BY_MODEL_NAME)
					.setParameter(DeviceConstant.MODEL_PLACE_HOLDER, modelName);
			device = (Device) query.getSingleResult();
		} catch (Exception e) {
			logger.error("Error in getting device detail : {}", Utils.getStackTrace(e));
		}
		return device;
	}
	
}
