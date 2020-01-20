package com.inn.foresight.module.nv.bbm.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.app.service.IDeviceInfoService;
import com.inn.foresight.module.nv.bbm.constant.BBMConstants;
import com.inn.foresight.module.nv.bbm.dao.IBBMDao;
import com.inn.foresight.module.nv.bbm.service.IBBMService;
import com.inn.foresight.module.nv.bbm.utils.BBMUtils;
import com.inn.foresight.module.nv.bbm.utils.wrapper.BBMDetailWrapper;

@Service("BBMServiceImpl")
public class BBMServiceImpl extends BBMConstants implements IBBMService {
	/** The logger. */
	private Logger logger = LogManager.getLogger(BBMServiceImpl.class);
	@Autowired
	private IDeviceInfoService iDeviceInfoService;
	@Autowired
	private IBBMDao iBBMDao;
	@Override
	@Transactional
	public String deviceRegistration(String fileName, InputStream inputStream) {
		String responseToReturn = SUCCESS_JSON;
		if (fileName != null && inputStream != null) {
			ByteArrayOutputStream baos = BBMUtils.getByteArrayOutputStream(inputStream);
			InputStream streamToValidateFile = new ByteArrayInputStream(baos.toByteArray());
			InputStream streamToConsume = new ByteArrayInputStream(baos.toByteArray());
			if (BBMUtils.isValidDeviceRegistrationFile(streamToValidateFile, fileName)) {
				InputStream streamToSaveCopy = new ByteArrayInputStream(baos.toByteArray());
				saveFileInSystem(fileName, streamToSaveCopy);
				List<DeviceInfo> deviceInfoList = BBMUtils.getDeviceData(streamToConsume);
				if (!deviceInfoList.isEmpty()) {
					createDevice(deviceInfoList);
				} else {
					responseToReturn = INVALID_PARAMETER_JSON;
				}
			} else {
				responseToReturn = INVALID_PARAMETER_JSON;
			}
		} else {
			responseToReturn = INVALID_PARAMETER_JSON;
		}

		return responseToReturn;
	}

	@Override
	@Transactional
	public String deviceDeregistration(String fileName, InputStream inputStream) {
		String responseToReturn = SUCCESS_JSON;
		if (fileName != null && inputStream != null) {
			ByteArrayOutputStream baos = BBMUtils.getByteArrayOutputStream(inputStream);
			InputStream streamToValidateFile = new ByteArrayInputStream(baos.toByteArray());
			InputStream streamToConsume = new ByteArrayInputStream(baos.toByteArray());
			if (BBMUtils.isValidDeviceDeRegistrationFile(streamToValidateFile, fileName)) {
				InputStream streamToSaveCopy = new ByteArrayInputStream(baos.toByteArray());
				saveFileInSystem(fileName, streamToSaveCopy);
				List<DeviceInfo> deviceInfoList = BBMUtils.getDeviceData(streamToConsume);
				if (!deviceInfoList.isEmpty()) {
					updateDevice(deviceInfoList);
				} else {
					responseToReturn = INVALID_PARAMETER_JSON;
				}
			} else {
				responseToReturn = INVALID_PARAMETER_JSON;
			}
		} else {
			responseToReturn = INVALID_PARAMETER_JSON;
		}

		return responseToReturn;
	}

	private void createDevice(List<DeviceInfo> deviceInfoList) {
		logger.info("deviceInfoList size when create BBM device :{}", deviceInfoList.size());
		iDeviceInfoService.createBBMDevice(deviceInfoList);
	}

	private void updateDevice(List<DeviceInfo> deviceInfoList) {
		logger.info("deviceInfoList size when update BBM device :{}", deviceInfoList.size());
		iDeviceInfoService.updateBBMDevice(deviceInfoList);
	}
	private static void saveFileInSystem(String fileName, InputStream streamToSaveCopy) {
		BBMUtils.writeFileToDisk(streamToSaveCopy, fileName);
	}

    @Override
	public List<BBMDetailWrapper> getBBMDetailByDeviceIdPrefix(String deviceIdPrefix, Long minTimeRange, Long maxTimeRange) {
		logger.info("Going to get BBM details by device id : {} minTimeRange : {} maxTimeRange : {} ", deviceIdPrefix, minTimeRange, maxTimeRange);
		List<BBMDetailWrapper> list = new ArrayList<>();
		try {
			list = iBBMDao.getBBMDetailByDeviceIdPrefix(deviceIdPrefix, minTimeRange, maxTimeRange);
		} catch (Exception e) {
			logger.error("Error in getting BBM Detail by device id , minTimeRange and maxTimeRange : {}", Utils.getStackTrace(e));
		}
		return list;
	}
	

}