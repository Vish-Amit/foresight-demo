package com.inn.foresight.module.nv.device.service;

import java.io.IOException;

import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;

public interface INVDeviceDataTransactionService {

	NVDeviceData createNVDevice(NVDeviceDataWrapper wrapper, String[] geographies, String operator,
			NVDeviceData nvDevice);

	void updateNVDeviceDataAndGetGeographies(NVDeviceDataWrapper nvDataWrapper, NVDeviceData nvDeviceData,
			String[] geographies, PushNotification pushNotification);

	Integer removeDeviceRefrencesFromGroup(String id, String devices) throws IOException;

	NVDeviceData create(NVDeviceData nvDevice);

}
