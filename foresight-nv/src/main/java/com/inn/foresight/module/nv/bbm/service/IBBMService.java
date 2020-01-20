package com.inn.foresight.module.nv.bbm.service;

import java.io.InputStream;
import java.util.List;

import com.inn.foresight.module.nv.bbm.utils.wrapper.BBMDetailWrapper;

public interface IBBMService {

	List<BBMDetailWrapper> getBBMDetailByDeviceIdPrefix(String deviceIdPrefix, Long minTimeRange, Long maxTimeRange);

	String deviceRegistration(String fileName,InputStream inputStream);
	String deviceDeregistration(String fileName,InputStream inputStream);
}
