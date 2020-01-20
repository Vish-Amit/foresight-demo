package com.inn.foresight.module.nv.device.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.app.dao.IDeviceInfoDao;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.dao.INVDeviceDataDao;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.device.service.INVDeviceDataTransactionService;
import com.inn.foresight.module.nv.device.util.NVDeviceUtil;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.profile.constants.NVProfileConstants;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.ModuleName;
import com.inn.foresight.module.nv.pushnotification.service.IPushNotificationService;
import com.inn.foresight.module.nv.pushnotification.wrapper.PushNotificationWrapper;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;
import com.inn.product.um.geography.dao.GeographyL1Dao;
import com.inn.product.um.geography.dao.GeographyL2Dao;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

@Service("NVDeviceDataTransactionServiceImpl")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class NVDeviceDataTransactionServiceImpl implements INVDeviceDataTransactionService {

	/** The i NV device data dao. */
	@Autowired
	INVDeviceDataDao iNVDeviceDataDao;

	/** The device info dao. */
	@Autowired
	private IDeviceInfoDao deviceInfoDao;
	/** The geography L 1 dao. */
	@Autowired
	private GeographyL1Dao geographyL1Dao;

	/** The geography L 2 dao. */
	@Autowired
	private GeographyL2Dao geographyL2Dao;

	/** The geography L 3 dao. */
	@Autowired
	private GeographyL3Dao geographyL3Dao;

	/** The geography L 4 dao. */
	@Autowired
	private GeographyL4Dao geographyL4Dao;
	/** The system configuration dao. */
	@Autowired
	private SystemConfigurationDao systemConfigurationDao;
	
	/** The notification service. */
	@Autowired
	private IPushNotificationService notificationService;


	/** The logger. */
	private static Logger logger = LogManager.getLogger(NVDeviceDataTransactionServiceImpl.class);

	@Override
	public NVDeviceData createNVDevice(NVDeviceDataWrapper wrapper, String[] geographies, String operator,
			NVDeviceData nvDevice) {

		DeviceInfo deviceInfo = deviceInfoDao.getDeviceInfoByDeviceId(wrapper.getDeviceId());
		if (deviceInfo != null) {
			nvDevice.setImei(deviceInfo.getImei());
			nvDevice.setImsi(deviceInfo.getImsi());
			nvDevice.setDeviceInfo(deviceInfo);
			nvDevice.setType((String) isValidDeviceData(wrapper.getDeviceType(), nvDevice.getType()));
			nvDevice.setOperator((String) isValidDeviceData(operator, nvDevice.getOperator()));
			nvDevice.setLatitude(wrapper.getLatitude());
			nvDevice.setLongitude(wrapper.getLongitude());
			nvDevice.setBatteryLevel((Double) isValidDeviceData(wrapper.getBatteryLevel(), nvDevice.getBatteryLevel()));
			nvDevice.setMsisdn((String) isValidDeviceData(wrapper.getMsisdn(), nvDevice.getMsisdn()));
			nvDevice.setMake((String) isValidDeviceData(wrapper.getMake(), nvDevice.getMake()));
			nvDevice.setModel((String) isValidDeviceData(wrapper.getModel(), nvDevice.getModel()));
			nvDevice.setOs((String) isValidDeviceData(wrapper.getDeviceOs(), nvDevice.getOs()));
			nvDevice.setModificationTime(new Date());
			nvDevice.setAppVersion((String) isValidDeviceData(wrapper.getAppVersion(), nvDevice.getAppVersion()));
			nvDevice.setModule((String) isValidDeviceData(wrapper.getModule(), nvDevice.getModule()));
			nvDevice.setLocationType((String) isValidDeviceData(wrapper.getLocationType(), nvDevice.getLocationType()));
			nvDevice.setLocationInfo((String) isValidDeviceData(wrapper.getLocationInfo(), nvDevice.getLocationInfo()));
			nvDevice.setRechargePlan((String) isValidDeviceData(wrapper.getRechargePlan(), nvDevice.getRechargePlan()));
			nvDevice.setValidity((String) isValidDeviceData(wrapper.getValidity(), nvDevice.getValidity()));
			nvDevice.setLastRechargeDate(
					(Date) isValidDeviceData(wrapper.getLastRechargeDate(), nvDevice.getLastRechargeDate()));
			nvDevice.setNextRechargeDate(
					(Date) isValidDeviceData(wrapper.getNextRechargeDate(), nvDevice.getNextRechargeDate()));

			if (geographies != null && ArrayUtils.isNotEmpty(geographies)) {
				updateDeviceGeography(geographies, nvDevice);
			}
		} else {
			logger.info("Device Info not found for device id:  {} , deviceInfo :  {} ", wrapper.getDeviceId(),
					deviceInfo);
		}
		return nvDevice;
	}
	
  
	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public NVDeviceData create(NVDeviceData nvDevice) {
	logger.info("creating NVDevice Entity");
		return iNVDeviceDataDao.create(nvDevice);

	}
	
	private Object isValidDeviceData(Object newData, Object oldData) {
		if (newData instanceof Long) {
			newData = new Date((long) newData);
		}
		return newData != null ? newData : oldData;
	}

	private void updateDeviceGeography(String[] geographies, NVDeviceData nvDevice) {

		if (geographies[DeviceConstant.GEOGRAPHY_L4_INDEX] != null&&!geographies[DeviceConstant.GEOGRAPHY_L4_INDEX].equalsIgnoreCase("null")) {
			updateDeviceGeographyForL4(geographies, nvDevice);
			return;
		}
		if (geographies[DeviceConstant.GEOGRAPHYL3_INDEX] != null&& !geographies[DeviceConstant.GEOGRAPHYL3_INDEX].equalsIgnoreCase("null")) {
			updateDeviceGeographyForL3(geographies, nvDevice);
			return;
		}
		if (geographies[DeviceConstant.GEOGRAPHYL2_INDEX] != null&& !geographies[DeviceConstant.GEOGRAPHYL2_INDEX] .equalsIgnoreCase("null")) {
			updateDeviceGeographyForL2(geographies, nvDevice);
			return;
		}
		if (geographies[DeviceConstant.GEOGRAPHYL1_INDEX] != null && !geographies[DeviceConstant.GEOGRAPHYL1_INDEX].equalsIgnoreCase("null")) {
			nvDevice.setGeographyL1(geographyL1Dao.getGeographyL1ByName(geographies[DeviceConstant.GEOGRAPHYL1_INDEX]));
		}
	}


	private void updateDeviceGeographyForL2(String[] geographies, NVDeviceData nvDevice) {
		GeographyL2 geographyL2 = geographyL2Dao.getGeographyL2ByName(
				geographies[DeviceConstant.GEOGRAPHYL2_INDEX]);
		nvDevice.setGeographyL2(geographyL2);
		nvDevice.setGeographyL1(geographyL2.getGeographyL1());
	}


	private void updateDeviceGeographyForL3(String[] geographies, NVDeviceData nvDevice) {
		GeographyL3 geographyL3 = geographyL3Dao.getGeographyL3ByName(
				geographies[DeviceConstant.GEOGRAPHYL3_INDEX]);
		nvDevice.setGeographyL3(geographyL3);
		nvDevice.setGeographyL2(geographyL3.getGeographyL2());
		if (geographyL3.getGeographyL2() != null) {
			nvDevice.setGeographyL1(geographyL3	.getGeographyL2()
												.getGeographyL1());
		}
	}


	private void updateDeviceGeographyForL4(String[] geographies, NVDeviceData nvDevice) {
		GeographyL4 geographyL4 = geographyL4Dao.getGeographyL4ByName(
				geographies[DeviceConstant.GEOGRAPHY_L4_INDEX]);
		nvDevice.setGeographyL4(geographyL4);
		nvDevice.setGeographyL3(geographyL4.getGeographyL3());
		if (geographyL4.getGeographyL3() != null) {
			nvDevice.setGeographyL2(geographyL4	.getGeographyL3()
												.getGeographyL2());
			if (geographyL4	.getGeographyL3()
							.getGeographyL2() != null) {
				nvDevice.setGeographyL1(geographyL4	.getGeographyL3()
													.getGeographyL2()
													.getGeographyL1());
			}
		}
	}

	@Override
	public void updateNVDeviceDataAndGetGeographies(NVDeviceDataWrapper nvDataWrapper, NVDeviceData nvDeviceData,
			String[] geographies,PushNotification pushNotification) {
		try {
			if (nvDeviceData != null) {
				checkValidateGeography(nvDataWrapper, nvDeviceData, geographies, pushNotification);
			} else {
				logger.error("nvDeviceData not found for device id: {} ", nvDataWrapper.getDeviceId());
			}
		} catch (Exception e) {
			logger.error("Exception in updateNVDeviceData: {} ", ExceptionUtils.getStackTrace(e));
		}

	}


	private void checkValidateGeography(NVDeviceDataWrapper nvDataWrapper, NVDeviceData nvDeviceData,
			String[] geographies, PushNotification pushNotification) {
		if (isValidGeography(geographies)) {
			nvDeviceData.setLatitude(nvDataWrapper.getLatitude());
			nvDeviceData.setLongitude(nvDataWrapper.getLongitude());
			String operator = getProfileRequestOperator(nvDataWrapper);
			nvDeviceData.setOperator(operator);
			setKPIFieldsToDeviceData(nvDataWrapper, nvDeviceData);
			nvDeviceData.setSinr(nvDataWrapper.getSinr());
			nvDeviceData.setCgi(nvDataWrapper.getCellId());
			updateDeviceGeography(geographies, nvDeviceData);
			nvDeviceData.setModificationTime(pushNotification.getExecutionTime());
			logger.info("Going to update nvDeviceData : {}", nvDeviceData);
			iNVDeviceDataDao.update(nvDeviceData);
		}else {
			logger.error("Invalid geography ,nv data not updated for device id {} ,Pushnotificaion ID {} ",nvDataWrapper.getId(),nvDataWrapper.getPushNotificationId());
		}
	}


	private void setKPIFieldsToDeviceData(NVDeviceDataWrapper nvDataWrapper, NVDeviceData nvDeviceData) {
		if (nvDataWrapper.getRssi() != null)
			nvDeviceData.setRssi(Double.valueOf(nvDataWrapper.getRssi()));
		if (nvDataWrapper.getRsrp() != null)
			nvDeviceData.setRsrp(Double.valueOf(nvDataWrapper.getRsrp()));
		if (nvDataWrapper.getRsrq() != null)
			nvDeviceData.setRsrq(Double.valueOf(nvDataWrapper.getRsrq()));
	}

	public static boolean isValidGeography(String[] geographies) {
		return geographies != null && ArrayUtils.isNotEmpty(geographies) && geographies.length > ForesightConstants.ZERO &&!isGeographyContainsNull(geographies);
	}

	private static boolean isGeographyContainsNull(String[] geographies) {
		return Arrays.asList(geographies).stream().allMatch(s->s==null||(s!=null&&s.equalsIgnoreCase("null")));
	}

	private String getProfileRequestOperator(NVDeviceDataWrapper wrapper) {
		List<SystemConfiguration> systemConfigurationList = systemConfigurationDao.getSystemConfigurationByNameAndType(
				NVProfileConstants.OPERATOR, Arrays.asList(NVProfileConstants.NV_PROFILE_DATA));
		SystemConfiguration configuration = systemConfigurationList.get(NVProfileConstants.INDEX_ZERO);
		Map<String, String> operators = new Gson().fromJson(configuration.getValue(),
				new TypeToken<Map<String, String>>() {
				}.getType());
		return operators.get(wrapper.getMcc() + Symbol.UNDERSCORE_STRING + wrapper.getMnc());
	}

	/**
	 * Creates the notification for device removal.
	 *
	 * @param deviceData the device data
	 * @throws RestException the rest exception
	 */
	private void createNotificationForDeviceRemoval(NVDeviceData deviceData) {
		PushNotificationWrapper notificationWrapper = new PushNotificationWrapper();

		notificationWrapper.setDeviceId(deviceData.getDeviceInfo().getDeviceId());
		notificationWrapper.setImsi(deviceData.getImsi());
		notificationWrapper.setImei(deviceData.getImei());
		notificationWrapper.setModuleName(ModuleName.PULSE);
		notificationWrapper.setNotificationPayLoad(DeviceConstant.PAYLOAD_FOR_PROBE_DEVICES_REMOVE_NOTIFICATION);
		notificationService.createNotification(notificationWrapper);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
    public Integer removeDeviceRefrencesFromGroup(String id, String devices) throws  IOException {
		Integer groupId = Integer.parseInt(id);
		Map<String, List> filter = getDeviceIdFilterMap(devices);
		List<NVDeviceData> deviceList = iNVDeviceDataDao.getDeviceDataByGroupId(groupId, filter);
		
		for (NVDeviceData deviceData : deviceList) {
			deviceData.setDeviceGroup(null);
			deviceData.setModificationTime(new Date());
			iNVDeviceDataDao.update(deviceData);
			createNotificationForDeviceRemoval(deviceData);
		}
		return groupId;
	}

	/**
	 * Gets the device id filter map.
	 *
	 * @param devices the devices
	 * @return the device id filter map
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JsonParseException the json parse exception
	 * @throws JsonMappingException the json mapping exception
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, List> getDeviceIdFilterMap(String devices) throws IOException {
		List<String> deviceIds;
		Map<String, List> filter = new HashMap<>();
		if (devices != null) {
			deviceIds = new ObjectMapper().readValue(devices, new TypeReference<List<String>>() {
			});

			if (deviceIds != null && !deviceIds.isEmpty()) {
				filter = NVDeviceUtil.getFilterValueMap(deviceIds);
			}
		}
		return filter;
	}
}
