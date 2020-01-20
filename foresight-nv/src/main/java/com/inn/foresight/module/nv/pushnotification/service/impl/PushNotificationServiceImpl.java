package com.inn.foresight.module.nv.pushnotification.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.app.dao.IDeviceInfoDao;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.util.NVDeviceUtil;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.fcm.service.IFCMServerService;
import com.inn.foresight.module.nv.feedback.constants.ConsumerFeedbackConstant;
import com.inn.foresight.module.nv.profile.utils.NVProfileDataUtils;
import com.inn.foresight.module.nv.pushnotification.constants.PushNotificationConstants;
import com.inn.foresight.module.nv.pushnotification.dao.IPushNotificationDao;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.ModuleName;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.Status;
import com.inn.foresight.module.nv.pushnotification.notificationfile.dao.INotificationFileDao;
import com.inn.foresight.module.nv.pushnotification.notificationfile.model.NotificationFile;
import com.inn.foresight.module.nv.pushnotification.service.IPushNotificationAsyncService;
import com.inn.foresight.module.nv.pushnotification.service.IPushNotificationService;
import com.inn.foresight.module.nv.pushnotification.utils.PushNotificationUtils;
import com.inn.foresight.module.nv.pushnotification.wrapper.PushNotificationWrapper;
import com.inn.foresight.module.nv.rest.impl.NVConsumerRestImpl;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/** The Class PushNotificationServiceImpl. */
@Service("PushNotificationServiceImpl")
public class PushNotificationServiceImpl implements IPushNotificationService {
	

	/** The logger. */
	private Logger logger = LogManager.getLogger(PushNotificationServiceImpl.class);

	/** The i push notification dao. */
	@Autowired
	private IPushNotificationDao iPushNotificationDao;

	/** The i notification file service. */
	@Autowired
	private INotificationFileDao iNotificationFileDao;

	/** The i fcm server service. */
	@Autowired
	private IFCMServerService iFcmServerService;

	/** The i device info dao. */
	@Autowired
	private IDeviceInfoDao iDeviceInfoDao;

	
	@Autowired
	IPushNotificationAsyncService pushNotificationAsyncService;


	/**
	 * Process notification.
	 *
	 * @throws RestException the rest exception
	 */
	
	@Override
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
	
	@Override
	@Transactional
	public void processNotification() {
		try {
			if (NVConsumerRestImpl.getNotificationEnable()) {
				List<PushNotification> pushNotifications = iPushNotificationDao.findAllNewAndFailedNotification();
				if (pushNotifications == null || pushNotifications.isEmpty()) {
					logger.info("processNotification{}:", ConsumerFeedbackConstant.PUSHNOTIFICATION_LIST_IS_EMPTY);
				} else {
					for (PushNotification pushNotification : pushNotifications) {
						sendNotification(pushNotification);
					}
				}
			} else {
				logger.info("Notification not enabled:  {}", NVConsumerRestImpl.getNotificationEnable());
			}
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	private void sendNotification(PushNotification pushNotification) {
		try {
			if (isValidNotification(pushNotification)) {
				JSONObject jsonObject = PushNotificationUtils.preparePayLoadJSONObject(pushNotification);
				if (jsonObject != null) {
					logger.info("sendNotification PayLoad {}:", new Gson().toJson(jsonObject));
					pushNotification.setExecutionTime(new Date());
					boolean isSent = iFcmServerService.sendNotification(jsonObject, pushNotification.getOs());
					setStatus(pushNotification, isSent);
					iPushNotificationDao.update(pushNotification);
				}
			}
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
	}

	

	
	/**
	 * Sets the status.
	 *
	 * @param pushNotification the push notification
	 * @param isSent           the is sent
	 */
	private void setStatus(PushNotification pushNotification, boolean isSent) {
		if (isSent) {
			pushNotification.setStatus(Status.SENT);
		} else {
			pushNotification.setStatus(Status.FAILED);
		}
	}

	/**
	 * Checks if is valid notification.
	 *
	 * @param pushNotification the push notification
	 * @return true, if is valid notification
	 */
	private boolean isValidNotification(PushNotification pushNotification) {
		if (pushNotification.getModuleName().equals(ModuleName.NV_FEEDBACK)
				&& (pushNotification.getScheduledTime().after(new Date())

						|| pushNotification.getNotificationFile().isDeleted())) {

			return Boolean.FALSE;

		}
		return Boolean.TRUE;
	}

	/**
	 * Gets the notification file detail by file id.
	 *
	 * @param id     the id
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 * @return the notification file detail by file id
	 * @throws RestException the rest exception
	 */
	@Override
	public List<PushNotification> getNotificationFileDetailByFileId(Integer id, Integer llimit, Integer ulimit) {
		try {
			NotificationFile notificationfile = iNotificationFileDao.findByPK(id);
			return iPushNotificationDao.getNotificationByFileId(notificationfile, llimit, ulimit);
		} catch (DaoException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Gets the feed back notification file detail list count.
	 *
	 * @param id the id
	 * @return the feed back notification file detail list count
	 * @throws RestException the rest exception
	 */
	@Override
	public Long getFeedBackNotificationFileDetailListCount(Integer id) {
		try {
			NotificationFile notificationfile = iNotificationFileDao.findByPK(id);
			return iPushNotificationDao.getFeedBackNotificationFileDetailListCount(notificationfile);
		} catch (DaoException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Creates the notification.
	 *
	 * @param notificationWrapper the notification wrapper
	 * @return the push notification
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public PushNotificationWrapper createNotification(PushNotificationWrapper notificationWrapper) {
		logger.info("Going to createNotification :{}", new Gson().toJson(notificationWrapper));
		PushNotification pushNotification = null;
		try {
			if (notificationWrapper.getPushnotificationId() != null) {
				pushNotification = iPushNotificationDao.findByPk(notificationWrapper.getPushnotificationId());
			} else {
				pushNotification = getPushnotificationObject(notificationWrapper);
				pushNotification = iPushNotificationDao.createNotification(pushNotification);
				pushNotification = updateNotificationId(notificationWrapper.getNotificationPayLoad(), pushNotification);
			}
//			sendNotification(pushNotification);
			
			pushNotificationAsyncService.sendNotificationAndUpdateStatus(pushNotification);
			
//			sendCustomerCareNotification(pushNotification);
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		notificationWrapper.setPushnotificationId(pushNotification.getId());
		notificationWrapper.setStatus(pushNotification.getStatus());
		return notificationWrapper;
	}

	private void sendCustomerCareNotification(PushNotification pushNotification) {
		if (ModuleName.NV_CUSTOMER_CARE.name().equalsIgnoreCase(pushNotification.getModuleName().name())) {
			pushNotificationAsyncService.sendNotificationAndUpdateStatus(pushNotification);
//			sendNotification(pushNotification);
		}
	}

	private PushNotification updateNotificationId(String notificationPayload, PushNotification pushNotification) {
		logger.info("Going to updateNotification Id :{}", pushNotification.getId());
		Map<String, String> payload = getNotificationPayload(notificationPayload);
		payload.put(PushNotificationConstants.KEY_PUSHNOTIFICATION_ID, String.valueOf(pushNotification.getId()));
		pushNotification.setNotificationPayLoad(new Gson().toJson(payload));
		return iPushNotificationDao.update(pushNotification);
	}

	/**
	 * Gets the pushnotification object.
	 *
	 * @param notificationWrapper the notification wrapper
	 * @return the pushnotification object
	 * @throws RestException the rest exception
	 * @throws DaoException  the dao exception
	 */
	private PushNotification getPushnotificationObject(PushNotificationWrapper notificationWrapper) {
		User loggedInUser = UserContextServiceImpl.getUserInContext();
		DeviceInfo deviceInfo = getDeviceInfo(notificationWrapper);
		if (loggedInUser == null) {
			logger.warn("loggedInUser is null ");
		}
		if (deviceInfo == null) {
			throw new RestException(PushNotificationConstants.NO_DEVICE_INFO_FOUND);
		}
		PushNotification pushNotification = new PushNotification();
		pushNotification.setStatus(Status.NEW);
		pushNotification.setCreationTime(new Date());
		pushNotification.setModuleName(notificationWrapper.getModuleName());
		pushNotification.setNotificationPayLoad(notificationWrapper.getNotificationPayLoad());
		pushNotification.setCreatedBy(loggedInUser);
		pushNotification.setDeviceInfo(deviceInfo);
		pushNotification.setRemark(notificationWrapper.getRemark());
		if (notificationWrapper.getMobileOsType() != null) {
			pushNotification.setOs(notificationWrapper.getMobileOsType());
		}
		if (notificationWrapper.getIsToUseClientFCMKey() != null) {
			pushNotification.setUseClientFcmKey(notificationWrapper.getIsToUseClientFCMKey());
		} else {
			pushNotification.setUseClientFcmKey(Boolean.FALSE);
		}
		return pushNotification;
	}

	private DeviceInfo getDeviceInfo(PushNotificationWrapper notificationWrapper) {
		DeviceInfo deviceInfo = null;
		try {
			if (notificationWrapper.getDeviceId() != null) {
				deviceInfo = iDeviceInfoDao.getDeviceInfoByDeviceId(notificationWrapper.getDeviceId());
			} else if (notificationWrapper.getImsi() != null && notificationWrapper.getImei() == null) {
				deviceInfo = iDeviceInfoDao.getDeviceInfoByImsi(notificationWrapper.getImsi());
			} else if (notificationWrapper.getImei() != null) {
				deviceInfo = iDeviceInfoDao.getDeviceInfoByImeiAndImsi(notificationWrapper.getImei(),
						notificationWrapper.getImsi());
			}
			logger.info("Done Get Device Info in getPushnotificationObject");
		} catch (DaoException e) {
			logger.error("DaoException in getPushnotificationObject:{}", ExceptionUtils.getStackTrace(e));
		}
		return deviceInfo;
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

	@Transactional
	@Override
	public String updateNotificationForAcknowledgement(String encrptedNotificationId) {
		String responseToReturn = PushNotificationConstants.FAILURE_JSON;
		PushNotification pushNotification = null;
		Integer notificationId = PushNotificationUtils.decryptAndParseNotificationID(encrptedNotificationId);
		logger.info("Going to updateNotificationForAcknowledgement for notificationId :{}", notificationId);
		try {
			if (notificationId != null) {
				pushNotification = iPushNotificationDao.findByPk(notificationId);
				if (pushNotification != null) {
					pushNotification.setRecieved(Boolean.TRUE);
					pushNotification = iPushNotificationDao.update(pushNotification);
					if (pushNotification != null) {
						responseToReturn = PushNotificationConstants.SUCCESS_JSON;
					} else {
						responseToReturn = PushNotificationConstants.FAILURE_JSON;
					}
				} else {
					logger.info("updateNotificationForAcknowledgement Pushnotification Object not exist for id {}",
							notificationId);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in updateNotificationForAcknowledgement:{}", ExceptionUtils.getStackTrace(e));
		}

		return responseToReturn;
	}

	@Override
	public PushNotification updateNotification(PushNotification notification) {
		try {
			return iPushNotificationDao.update(notification);
		} catch (DaoException e) {
			logger.error("DaoException in updateNotification:{}", ExceptionUtils.getStackTrace(e));

		}
		return null;
	}

	@Override
	public PushNotification findByPk(Integer pushNotificationId) {
		try {
			return iPushNotificationDao.findByPk(pushNotificationId);
		} catch (DaoException e) {
			logger.error("DaoException in findByPk:{}", ExceptionUtils.getStackTrace(e));

		}
		return null;
	}

	@Override
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public PushNotification updateLiveLocationDetail(NVDeviceDataWrapper nvDataWrapper) {
		logger.info("Going to update Live location detail for notification id : {}",
				nvDataWrapper.getPushNotificationId());
		PushNotification pushNotification = findByPk(nvDataWrapper.getPushNotificationId());
		if (pushNotification != null) {
			pushNotification.setRecieved(Boolean.TRUE);
			pushNotification.setLiveLocationDetail(NVDeviceUtil.getLiveLocationJson(nvDataWrapper));
			pushNotification.setExecutionTime(new Date());
			updateNotification(pushNotification);

		}
		return pushNotification;
	}

	@Override
	public String updateLiveLocationPushNotification(NVDeviceDataWrapper nvDataWrapper, String[] geographies,
			PushNotification pushNotification) {
		logger.info("Going to updateLiveLocationPushNotification");
		if (pushNotification != null) {
			if (nvDataWrapper.getLatitude() == null && nvDataWrapper.getLongitude() == null) {
				pushNotification.setRemark(nvDataWrapper.getLocationReason());
			} else if (geographies == null || ArrayUtils.isEmpty(geographies)
					|| geographies.length == ForesightConstants.ZERO) {
				pushNotification.setRemark(DeviceConstant.GEOGRAPHY_NOT_FOUND);
			} else if (NVProfileDataUtils.isValidNumber(nvDataWrapper.getLatitude())
					&& NVProfileDataUtils.isValidNumber(nvDataWrapper.getLongitude())) {
				if (nvDataWrapper.getLocationReason() != null) {
					pushNotification.setRemark(nvDataWrapper.getLocationReason());
				} else {
					pushNotification.setRemark(DeviceConstant.LOCATION_RECIEVED);
				}
			} else {
				if (nvDataWrapper.getLocationReason() != null) {
					pushNotification.setRemark(nvDataWrapper.getLocationReason());
				} else {
					pushNotification.setRemark(DeviceConstant.LOCATION_NOT_RECIEVED);
				}
			}
			logger.info("Going to update Notification wrapper {}", pushNotification);
			updateNotification(pushNotification);
			return DeviceConstant.RESULT_SUCCESS;
		} else {
			logger.error(DeviceConstant.RESULT_PUSHNOTIFICATION_ID_NULL);
			return DeviceConstant.RESULT_PUSHNOTIFICATION_ID_NULL;
		}
	}

	@Override
	@Transactional
	public PushNotificationWrapper createNotificationForCustomercare(PushNotificationWrapper notificationWrapper) {
		
		logger.info("Going to createNotification :{}", new Gson().toJson(notificationWrapper));
		PushNotification pushNotification = null;
		try {
			if (notificationWrapper.getPushnotificationId() != null) {
				pushNotification = iPushNotificationDao.findByPk(notificationWrapper.getPushnotificationId());
			} else {
				pushNotification = getPushnotificationObject(notificationWrapper);
				pushNotification = iPushNotificationDao.createNotification(pushNotification);
				pushNotification = updateNotificationId(notificationWrapper.getNotificationPayLoad(), pushNotification);
			}
			sendNotification(pushNotification);
			
//			sendCustomerCareNotification(pushNotification);
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		notificationWrapper.setPushnotificationId(pushNotification.getId());
		notificationWrapper.setStatus(pushNotification.getStatus());
		return notificationWrapper;
	}
		

}
