package com.inn.foresight.module.nv.pushnotification.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.fcm.service.IFCMServerService;
import com.inn.foresight.module.nv.pushnotification.constants.PushNotificationConstants;
import com.inn.foresight.module.nv.pushnotification.dao.IPushNotificationDao;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.Status;
import com.inn.foresight.module.nv.pushnotification.service.IPushNotificationAsyncService;
import com.inn.foresight.module.nv.pushnotification.utils.PushNotificationUtils;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

@Service("PushNotificationAsyncServiceImpl")
public class PushNotificationAsyncServiceImpl implements IPushNotificationAsyncService  {

	@Autowired
	private IFCMServerService iFcmServerService;

	@Autowired
	private IPushNotificationDao iPushNotificationDao;

	/** The logger. */
	private Logger logger = LogManager.getLogger(PushNotificationAsyncServiceImpl.class);


	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public void sendNotificationAndUpdateStatus(PushNotification pushNotification) {
		if(pushNotification!=null) {
		try {
			JSONObject jsonObject = PushNotificationUtils.preparePayLoadJSONObject(pushNotification);
			if (jsonObject != null) {
				logger.info("sendNotification PayLoad {}:", new Gson().toJson(jsonObject));
				pushNotification.setExecutionTime(new Date());
				boolean isSent = iFcmServerService.sendNotification(jsonObject, pushNotification.getOs());
				if (isSent) {
					logger.info("Notification Sent Successfully to Device {} with Thread Id {}",
							pushNotification.getDeviceInfo().getDeviceId(),Thread.currentThread().getId());
					pushNotification.setStatus(Status.SENT);
					iPushNotificationDao.update(pushNotification);
				} else {
					logger.info("Notification Failed for Device {} with Thread Id {}",
							pushNotification.getDeviceInfo().getDeviceId(),Thread.currentThread().getId());
					pushNotification.setStatus(Status.FAILED);
					iPushNotificationDao.update(pushNotification);
				}

			}

		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		}
	}

	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public void sendNotificationsAndUpdateStatus(List<PushNotification> list) {
		List<PushNotification> listOfPushNotifications = new ArrayList<>(list);
				
			for (PushNotification pushNotification : listOfPushNotifications) {
				try {
				if(pushNotification!=null) {
				JSONObject jsonObject = PushNotificationUtils.preparePayLoadJSONObject(pushNotification);
				if (jsonObject != null) {				
					logger.info("sendNotification PayLoad {}:", new Gson().toJson(jsonObject));
					pushNotification.setExecutionTime(new Date());
					boolean isSent = iFcmServerService.sendNotification(jsonObject, pushNotification.getOs());
					if (isSent) {
						logger.info("Notification Sent Successfully to Device {}",
								pushNotification.getDeviceInfo().getDeviceId());
						pushNotification.setStatus(Status.SENT);
						iPushNotificationDao.update(pushNotification);
					} else {
						pushNotification.setStatus(Status.FAILED);
						iPushNotificationDao.update(pushNotification);
					}

				}
				}

			}
				catch (Exception e) {
					logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
				}
		} 
		
		logger.info("TIME INSIDE {}", System.currentTimeMillis());

	}

	public PushNotification createNotification(PushNotification pushNotification) {
		try {
			pushNotification = iPushNotificationDao.createNotification(pushNotification);
			pushNotification = updateNotificationId(pushNotification.getNotificationPayLoad(), pushNotification);
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return pushNotification;
	}

	private PushNotification updateNotificationId(String notificationPayload, PushNotification pushNotification) {
		logger.info("Going to updateNotification Id :{}", pushNotification.getId());
		Map<String, String> payload = getNotificationPayload(notificationPayload);
		payload.put(PushNotificationConstants.KEY_PUSHNOTIFICATION_ID, String.valueOf(pushNotification.getId()));
		pushNotification.setNotificationPayLoad(new Gson().toJson(payload));
		return iPushNotificationDao.update(pushNotification);
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getNotificationPayload(String payload) {
		Map<String, String> payLoad = new HashMap<>();
		try {
			payLoad = new ObjectMapper().readValue(payload, HashMap.class);
		} catch (Exception e) {
			logger.error("Exception in getNotificationPayload:{}", ExceptionUtils.getStackTrace(e));
		}
		return payLoad;
	}

}
