package com.inn.foresight.module.nv.device.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Tuple;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.app.dao.IAPKDetailDao;
import com.inn.foresight.module.nv.app.model.APKDetail;
import com.inn.foresight.module.nv.device.dao.DeviceDashboardDao;
import com.inn.foresight.module.nv.device.model.DeviceDashboard;
import com.inn.foresight.module.nv.device.service.DeviceDashboardService;
import com.inn.foresight.module.nv.device.wrapper.DeviceDashboardWrapper;
import com.inn.foresight.module.nv.profile.utils.NVUtilService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.security.utils.AuthenticationCommonUtil;

@Service("DeviceDashboardServiceImpl")
public class DeviceDashboardServiceImpl implements DeviceDashboardService {

	private static Logger logger = LoggerFactory.getLogger(DeviceDashboardServiceImpl.class);

	@Autowired
	DeviceDashboardDao deviceDashboardDao;
	
	@Autowired
	IAPKDetailDao apkDetailDao;
	
	
	@Override
	@Transactional
	public String persistDeviceDashboardData(String encryptedWrapper) {
		if (!StringUtils.isEmpty(encryptedWrapper)) {
			try {
				DeviceDashboardWrapper wrapper = new Gson().fromJson(AuthenticationCommonUtil.checkForValueDecryption(encryptedWrapper),
						DeviceDashboardWrapper.class);
				if (!StringUtils.isEmpty(wrapper.getDeviceId())) {
					logger.info("Going to persist data for deviceId {}",wrapper.getDeviceId());
					DeviceDashboard deviceData = deviceDashboardDao.findDeviceDataByDeviceId(wrapper.getDeviceId());
					deviceData=getValuesFromWrapper(deviceData, wrapper);
				APKDetail apkDetail =apkDetailDao.getAPKDetailById(wrapper.getApkId(),wrapper.getDeviceOS(), wrapper.getVersionName(),Boolean.FALSE);
				deviceData.setApkDetail(apkDetail);
				deviceDashboardDao.persistDashboardData(deviceData);
					return AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.SUCCESS_JSON, null);
				}
			} catch (Exception e) {
				logger.error("Exception in syncDeviceDashboardData {}", Utils.getStackTrace(e));

			}
		}
		return AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON, null);
	}

	DeviceDashboard getValuesFromWrapper(DeviceDashboard dashboardData, DeviceDashboardWrapper wrapper) {
		if(dashboardData==null) {
			dashboardData=new DeviceDashboard();
			dashboardData.setDeviceId(wrapper.getDeviceId());
			dashboardData.setCreationTime(new Date());
		}
		String operator = getOperatorFromMCCandMNC(wrapper.getMcc(), wrapper.getMnc());
		if(StringUtils.isEmpty(operator)) {
		dashboardData.setOperator(operator);
		}
		dashboardData.setLatitude(wrapper.getLatitude());
		dashboardData.setLongitude(wrapper.getLongitude());
		dashboardData.setBadge(wrapper.getBadge());
		dashboardData.setConsumption(wrapper.getConsumption());
		dashboardData.setDeviceId(wrapper.getDeviceId());
		dashboardData.setDlThroughput(wrapper.getDlThroughput());
		dashboardData.setUlThroughput(wrapper.getUlThroughput());
		dashboardData.setMake(wrapper.getMake());
		dashboardData.setModel(wrapper.getModel());
		dashboardData.setTotalTest(wrapper.getTotalTest());
		dashboardData
				.setModificationTime(wrapper.getTimeStamp() != null ? new Date(wrapper.getTimeStamp()) : new Date());
		dashboardData.setCellServed(wrapper.getCellServed());
		dashboardData.setBadgeFormula(wrapper.getBadgeFormula());
		return dashboardData;
	}
	
	
	@Override
	public List<DeviceDashboard> getDeviceDashbordData(Integer llimit,Integer ulimit,DeviceDashboardWrapper wrapper){
		logger.info("Going to get Device List  for llimit {} ulimit {} {}",llimit,ulimit,wrapper);
		List<DeviceDashboard> deviceList = deviceDashboardDao.findDeviceList(wrapper, llimit, ulimit);
		return deviceList!=null?deviceList:new ArrayList<DeviceDashboard>();
	}

	@Override
	public Long getDeviceDashbordDataCount(DeviceDashboardWrapper wrapper) {
		Long countOfDevice= deviceDashboardDao.findDevicesCount(wrapper);
		return countOfDevice;
	}

	public String getOperatorFromMCCandMNC(Integer mcc,Integer mnc) {
		logger.info("Going to get Operator for mcc {} and mnc {} ",mcc,mnc);
		String operator=null;
		try {
			if(NVUtilService.getMccMncOperatorMap()!=null) {
				operator = NVUtilService.getMccMncOperatorMap().get(mcc + Symbol.UNDERSCORE_STRING + mnc);
				logger.info("Operator Found {}",operator);
				}
			
		}
		catch(Exception e) {
			logger.error("Error Inside getOperatorFromMCCandMNC {}",Utils.getStackTrace(e) );
		}
		return operator;
	}

	@Override
	public List<DeviceDashboardWrapper> getTop10DeviceWithRank(String deviceId){

		List<DeviceDashboardWrapper> top10RankWithDeviceRank = deviceDashboardDao.getTop10DeviceWithRank(deviceId);
		return  top10RankWithDeviceRank;
	}
	

}
