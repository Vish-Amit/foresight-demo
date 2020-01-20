package com.inn.foresight.module.nv.device.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.app.utils.AppUtils;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.dao.INVDeviceDataDao;
import com.inn.foresight.module.nv.device.dao.INVDeviceGroupDao;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.device.model.NVDeviceGroup;
import com.inn.foresight.module.nv.device.service.INVDeviceDataService;
import com.inn.foresight.module.nv.device.service.INVDeviceDataTransactionService;
import com.inn.foresight.module.nv.device.service.INVDeviceGroupService;
import com.inn.foresight.module.nv.device.util.NVDeviceUtil;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.workorder.stealth.kpi.dao.IDriveTestKPIDao;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.model.User;

/**
 * The Class NVDeviceGroupServiceImpl.
 *
 * @author innoeye
 * date - 24-Apr-2018 12:36:27 PM
 */
@Service("NVDeviceGroupServiceImpl")
public class NVDeviceGroupServiceImpl
		extends AbstractService<Integer, NVDeviceGroup>
		implements INVDeviceGroupService {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVDeviceGroupServiceImpl.class);
	
	/** The device group dao. */
	@Autowired
	private INVDeviceGroupDao deviceGroupDao;
	
	/** The i drive test KPI dao. */
	@Autowired
	private IDriveTestKPIDao iDriveTestKPIDao;
	/**
	 * Sets the dao.
	 *
	 * @param deviceGroupDao the new dao
	 */
	@Autowired
	public void setDao(INVDeviceGroupDao deviceGroupDao) {
		super.setDao(deviceGroupDao);
		this.deviceGroupDao = deviceGroupDao;
	}

	/** The geography L 4 dao. */
	@Autowired
	private GeographyL4Dao geographyL4Dao;
	
	/** The device data dao. */
	@Autowired
	private INVDeviceDataDao deviceDataDao;
	
	/** The i generic map service. */
	@Autowired
	private IGenericMapService iGenericMapService;
	
	/** The i nv device service. */
	@Autowired
	private INVDeviceDataService iNvDeviceService;
	
	@Autowired
	private INVDeviceDataTransactionService iNVDeviceDataTransactionService;

	
	/**
	 * Gets the all device groups.
	 *
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the all device groups
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVDeviceGroupWrapper> getAllDeviceGroups(Integer ulimit,Integer llimit)	{
		try {
			logger.info("Inside getAllDeviceGroups");
			List<NVDeviceGroupWrapper> deviceGroups = deviceGroupDao.getAllDeviceGroups(ulimit,llimit);
			for(NVDeviceGroupWrapper deviceGroup : deviceGroups) {
				deviceGroup.setCount(deviceDataDao.getDeviceCountByGroupId(deviceGroup.getId()));
			}
			logger.info("Done getAllDeviceGroups with resultSize : {}",deviceGroups.size());
			return deviceGroups;
		} catch (Exception e) {
			logger.error("Exception in getAllDeviceGroups : {} ",ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	
	/**
	 * Creates the.
	 *
	 * @param deviceGroup the device group
	 * @param creator the creator
	 * @return the NV device group
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public NVDeviceGroup create(NVDeviceGroup deviceGroup, User creator) {
		logger.info("Going to create NVDeviceGroup");
		try {
			NVDeviceGroup nvDeviceGroup = createDeviceGroup(deviceGroup, creator);
			nvDeviceGroup.setGroupId(
					AppUtils.generateUniqueIdWithPrefix(nvDeviceGroup.getId(), DeviceConstant.DEVICE_GROUP_ID_PREFIX));
			nvDeviceGroup = deviceGroupDao.update(nvDeviceGroup);
			logger.info("Done create NVDeviceGroup id : {}", nvDeviceGroup.getId());
			return nvDeviceGroup;
		} catch (Exception e) {
			logger.error("Essception in create NVDeviceGroup : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	
	/**
	 * Creates the device group.
	 *
	 * @param deviceGroup the device group
	 * @param creator the creator
	 * @return the NV device group
	 * @throws DaoException the dao exception
	 */
	private NVDeviceGroup createDeviceGroup(NVDeviceGroup deviceGroup,
			User creator) {
		NVDeviceGroup nvDeviceGroup = new NVDeviceGroup(deviceGroup);
		nvDeviceGroup.setCreator(creator);
		updateGeography(nvDeviceGroup);
		return deviceGroupDao.create(nvDeviceGroup);
	}

	/**
	 * Update geography.
	 *
	 * @param nvDeviceGroup the nv device group
	 * @throws DaoException the dao exception
	 */
	private void updateGeography(
			NVDeviceGroup nvDeviceGroup) {
		String geographyL4Name = getGeographyL4name(nvDeviceGroup.getLatitude(), nvDeviceGroup.getLongitude());
		if (StringUtils.isNotBlank(geographyL4Name) && StringUtils.isNotEmpty(geographyL4Name)) {
			GeographyL4 geographyL4 = geographyL4Dao.getGeographyL4ByName(geographyL4Name);
			nvDeviceGroup.setGeographyL4(geographyL4);
			nvDeviceGroup.setGeographyL3(geographyL4.getGeographyL3());
			nvDeviceGroup.setGeographyL2(
					geographyL4.getGeographyL3().getGeographyL2());
			nvDeviceGroup.setGeographyL1(geographyL4.getGeographyL3()
													.getGeographyL2()
													.getGeographyL1());
		} 
	}
	
	
	/**
	 * Adds the device.
	 *
	 * @param id
	 *            the id
	 * @param devices
	 *            the devices
	 * @param isEncryptEnable
	 *            the is encrypt enable
	 * @return the string
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@Transactional
	public String addDevice(String id, String devices, boolean isEncryptEnable) {
		logger.info("Going to addDevice to NVDeviceGroup");
		try {
			if (isEncryptEnable) {
				id = AuthenticationCommonUtil.checkForValueDecryption(id);
				devices = AuthenticationCommonUtil.checkForValueDecryption(devices);
			}
			Integer deviceGroupId = Integer.parseInt(id);
			addDevicesToGroup(devices, deviceGroupId);
			if (isEncryptEnable)
				return AuthenticationCommonUtil.checkForValueEncryption(DeviceConstant.SUCCESS_JSON,null);
			else
				return DeviceConstant.SUCCESS_JSON;
		} catch (Exception e) {
			logger.error("Essception with addDevice to NVDeviceGroup : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Adds the devices to group.
	 *
	 * @param devices the devices
	 * @param deviceGroupId the device group id
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DaoException the dao exception
	 * @throws RestException the rest exception
	 */
	private void addDevicesToGroup(String devices, Integer deviceGroupId)
			throws IOException {
		List<NVDeviceDataWrapper> deviceIds = new ObjectMapper().readValue(devices, new TypeReference<List<NVDeviceDataWrapper>>() {
		});
		NVDeviceGroup deviceGroup = deviceGroupDao.findByPk(deviceGroupId);
		for (NVDeviceDataWrapper deviceWrapper : deviceIds) {
			NVDeviceData nvDevice = null;
			try {
				nvDevice = deviceDataDao.getDeviceByDeviceId(deviceWrapper.getDeviceId());
				addDeviceToGroup(nvDevice, deviceGroup, deviceWrapper);

			} catch (NoResultException | EmptyResultDataAccessException e) {
				nvDevice = createNewDeviceAndAddToGroup(deviceGroup, deviceWrapper);
			}

			Integer addedDeviceId = deviceDataDao.update(nvDevice).getId();
			logger.info("Device Id: {} is successfully linked with GroupId: {} ", addedDeviceId, deviceGroupId);

		}
		logger.info("Devices Added to NVDeviceGroup for Group Id: {}", deviceGroupId);
	}

	/**
	 * Creates the new device and add to group.
	 *
	 * @param deviceGroup the device group
	 * @param deviceWrapper the device wrapper
	 * @return the NV device data
	 * @throws DaoException the dao exception
	 * @throws RestException the rest exception
	 */
	private NVDeviceData createNewDeviceAndAddToGroup(NVDeviceGroup deviceGroup, NVDeviceDataWrapper deviceWrapper) {
		String[] geographies = iNvDeviceService.getGeographiesForDeviceRequest(deviceWrapper);
		NVDeviceData nvDevice = iNvDeviceService.persistNVDeviceData(deviceWrapper,geographies,null);
		nvDevice.setDeviceGroup(deviceGroup);
		return nvDevice;
	}

	/**
	 * Adds the device to group.
	 *
	 * @param nvDevice the nv device
	 * @param deviceGroup the device group
	 * @param deviceWrapper the device wrapper
	 */
	private void addDeviceToGroup(NVDeviceData nvDevice, NVDeviceGroup deviceGroup, NVDeviceDataWrapper deviceWrapper) {
		nvDevice.setDeviceGroup(deviceGroup);
		nvDevice.setMsisdn(deviceWrapper.getMsisdn());
		nvDevice.setRechargePlan(deviceWrapper.getRechargePlan());
		nvDevice.setValidity(deviceWrapper.getValidity());
		nvDevice.setLastRechargeDate(deviceWrapper.getLastRechargeDate() != null ? new Date(deviceWrapper.getLastRechargeDate()) : null);
		nvDevice.setNextRechargeDate(deviceWrapper.getNextRechargeDate() != null ? new Date(deviceWrapper.getNextRechargeDate()) : null);
		nvDevice.setModificationTime(new Date());
	}
	
	/**
	 * Removes the device.
	 *
	 * @param id the id
	 * @param devices the devices
	 * @param isEncryptEnable the is encrypt enable
	 * @return the string
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public String removeDevice(String id, String devices, boolean isEncryptEnable) {
		logger.info("Going to remove devices or group with groupId: {}", id);
		try {
			if (isEncryptEnable) {
				id = AuthenticationCommonUtil.checkForValueDecryption(id);
				devices = AuthenticationCommonUtil.checkForValueDecryption(devices);
			}
			Integer groupId = iNVDeviceDataTransactionService.removeDeviceRefrencesFromGroup(id, devices);
			removeById(groupId);
			logger.info("Device Removed!!");

			if (isEncryptEnable)
				return AuthenticationCommonUtil.checkForValueEncryption(DeviceConstant.SUCCESS_JSON,null);
			else
				return ForesightConstants.SUCCESS_JSON;
		} catch (Exception e) {
			logger.error("Error while delete device from groupId : {}", id);
			logger.error("{}", ExceptionUtils.getStackTrace(e));
			throw new RestException("Unable to delete Device Group");
		}
	}


	
	/**
	 * Gets the device group.
	 *
	 * @param id the id
	 * @param isEncryptEnable the is encrypt enable
	 * @return the device group
	 * @throws RestException the rest exception
	 */
	@Override
	public String getDeviceGroup(String id, boolean isEncryptEnable) {
		logger.info("Inside getDeviceGroup");
		try {
			if(isEncryptEnable) {
				id = AuthenticationCommonUtil.checkForValueDecryption(id);
			}
			Integer groupId = Integer.parseInt(id);
			NVDeviceGroup deviceGroup = deviceGroupDao.findByPk(groupId);
			NVDeviceGroupWrapper deviceGroupWrapper = new NVDeviceGroupWrapper(deviceGroup);
			deviceGroupWrapper.setDevices(deviceDataDao.getDevicesByGroupId(groupId));
			logger.info("Done getDeviceGroup");
			if (isEncryptEnable)
				return AuthenticationCommonUtil.checkForValueEncryption(deviceGroupWrapper,null);
			else
				return new Gson().toJson(deviceGroupWrapper);
		} catch (Exception e) {
			logger.error("Essception in getDeviceGroup : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}


	/**
	 * Gets the all device groups.
	 *
	 * @param encryptedLat the encrypted lat
	 * @param encryptedLong the encrypted long
	 * @return the all device groups
	 * @throws RestException the rest exception
	 */
	@Override
	public String getAllDeviceGroups(String encryptedLat,
			String encryptedLong) {
		try {
			logger.info("Inside getAllDeviceGroups");
			Double latitude = Double.parseDouble(AuthenticationCommonUtil.checkForValueDecryption(encryptedLat));
			Double longitude = Double.parseDouble(AuthenticationCommonUtil.checkForValueDecryption(encryptedLong));
			Double[][] viewPort = NVDeviceUtil.getViewPortByCenterLatLong(latitude, longitude, NVConstant.DOUBLE_HUNDRED);
			List<NVDeviceGroupWrapper> deviceGroups = deviceGroupDao.getDeviceGroupWithinViewPort(viewPort[1][0], viewPort[1][1], viewPort[0][0], viewPort[0][1]);
			for(NVDeviceGroupWrapper deviceGroup : deviceGroups) {
				deviceGroup.setCount(deviceDataDao.getDeviceCountByGroupId(deviceGroup.getId()));
			}
			logger.info("Done getAllDeviceGroups with resultSize : {}",deviceGroups.size());
			return AuthenticationCommonUtil.checkForValueEncryption(deviceGroups,null);
		} catch (Exception e) {
			logger.error("Essception in getAllDeviceGroups : {} ",ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}
	
	/**
	 * Gets the geographyname.
	 *
	 * @param latitude            the latitude
	 * @param longitude            the longitude
	 * @return the geographyname
	 */
	private String getGeographyL4name(Double latitude,
			Double longitude) {
		String geographyName = null;
		try {
			String response = iGenericMapService.getGeographyDataByPoint(GenericMapUtils.getGeoColumnList(),GenericMapUtils.GEOGRAPHY_TABLE_NAME, latitude , longitude,
					false,GenericMapUtils.L4_TYPE);
			List<List<String>> list = new Gson().fromJson(response, new TypeToken<List<List<String>>>() {
			}.getType());
			if (list.isEmpty()) {
				throw new RestException(ForesightConstants.GEOGRAPHY_NOT_FOUND);
			}
			geographyName = list.get(NVConstant.ZERO_INT)
								.get(NVConstant.ZERO_INT);
		} catch (Exception e) {
			logger.error("Exception in getGeographyname : {} ", ExceptionUtils.getStackTrace(e));
		}
		return geographyName;
	}

	/**
	 * Gets the device details by device id.
	 *
	 * @param deviceId the device id
	 * @return the device details by device id
	 * @throws RestException the rest exception
	 */
	@Override
	public NVDeviceDataWrapper getDeviceDetailsByDeviceId(String deviceId) {
		NVDeviceDataWrapper nvDeviceData = null;
		try {
			nvDeviceData = deviceGroupDao.getNVDeviceDetailsByDeviceId(deviceId);
			
		} catch (DaoException e) {
			logger.error("Exception in getDeviceDetailsByDeviceId : {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return nvDeviceData;
	}

	/**
	 * Gets the all device groups count.
	 *
	 * @return the all device groups count
	 * @throws RestException the rest exception
	 */
	@Override
	public Long getAllDeviceGroupsCount() {
		try {
			logger.info("Inside getAllDeviceGroupsCount");
			Long deviceGroupsCount = deviceGroupDao.getAllDeviceGroupsCount();
			logger.info("Done getAllDeviceGroupsCount with result : {}",deviceGroupsCount);
			return deviceGroupsCount;
		} catch (Exception e) {
			logger.error("Exception in getAllDeviceGroupsCount : {} ",ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Update device group.
	 *
	 * @param deviceGroup the device group
	 * @return the NV device group wrapper
	 * @throws Exception 
	 */
	@Override
	@Transactional
	public NVDeviceGroupWrapper updateDeviceGroup(NVDeviceGroupWrapper wrapper) throws Exception{
		try {
			logger.info("Inside updateDeviceGroup");
			Integer groupId = wrapper.getId();
			String groupName = wrapper.getName();
			NVDeviceGroup nvDeviceGroup = deviceGroupDao.findByPk(groupId);
			nvDeviceGroup.setName(groupName);
			deviceGroupDao.update(nvDeviceGroup);
			logger.info("Done update Device Group");
			return wrapper;
		} catch (Exception e) {
			logger.error("Exception in updateDeviceGroup : {} ",ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Search NV device groups.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @param zoomLevel the zoom level
	 * @param dataType the data type
	 * @param kpi the kpi
	 * @param threshold the threshold
	 * @param resultType the result type
	 * @return the List<NVDeviceGroupWrapper>
	 * @throws RestException 
	 */
	@Transactional
	@Override
	public List<NVDeviceGroupWrapper> searchNVDeviceGroups(Long startDate, Long endDate, Double swLat, Double swLng,
			Double neLat, Double neLng, Integer zoomLevel, String dataType, String kpi, Double threshold,
			String resultType) {
		logger.info(
				"Going to searchNVDeviceGroups for sWLat {},sWLong {},nELat {},nELong{}, zoomLevel {}, dataType {}, kpi {} and threshold {}",
				swLat, swLng, neLat, neLng, zoomLevel, dataType, kpi, threshold);
		try {
			List<NVDeviceGroupWrapper> deviceGroups = null;
			if (dataType.equalsIgnoreCase("ALL")) {
				deviceGroups = deviceGroupDao.searchAllNVDeviceGroups(swLat, swLng, neLat, neLng, zoomLevel,
						new Date(endDate));
			} else {
				deviceGroups = iDriveTestKPIDao.searchNVDeviceGroups(startDate, endDate, swLat, swLng, neLat,
						neLng, zoomLevel, StealthUtils.getFilterAndValueMap(dataType, kpi, threshold,
								new Date(startDate), new Date(endDate), false, resultType));
			}
			Integer deviceGroupZoom = Integer
					.parseInt(SystemConfigurationUtils.systemConfMap.get(DeviceConstant.DEVICE_GROUP_ZOOM));
			if (zoomLevel >= deviceGroupZoom) {
				for (NVDeviceGroupWrapper deviceGroup : deviceGroups) {
					deviceGroup.setCount(deviceDataDao.getDeviceCountByGroupId(deviceGroup.getId()));
				}
			}
			logger.info("Returning result from searchNVDeviceGroups with size {}", deviceGroups.size());
			return deviceGroups;
		} catch (Exception e) {
			logger.error("Exception in searchProbes : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

}
