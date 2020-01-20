package com.inn.foresight.module.nv.pushnotification.notificationfile.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.app.dao.IDeviceInfoDao;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.feedback.constants.ConsumerFeedbackConstant;
import com.inn.foresight.module.nv.feedback.utils.ConsumerFeedbackUtils;
import com.inn.foresight.module.nv.feedback.wrapper.FeedbackFileWrapper;
import com.inn.foresight.module.nv.pushnotification.constants.PushNotificationConstants;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;
import com.inn.foresight.module.nv.pushnotification.notificationfile.dao.INotificationFileDao;
import com.inn.foresight.module.nv.pushnotification.notificationfile.dao.INotificationFileHDFSDao;
import com.inn.foresight.module.nv.pushnotification.notificationfile.model.NotificationFile;
import com.inn.foresight.module.nv.pushnotification.notificationfile.service.INotificationFileService;
import com.inn.foresight.module.nv.pushnotification.service.IPushNotificationAsyncService;
import com.inn.foresight.module.nv.pushnotification.service.IPushNotificationService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/** The Class NotificationFileServiceImpl. */
@Service("NotificationFileServiceImpl")
public class NotificationFileServiceImpl implements INotificationFileService {
	/** The logger. */
	private Logger logger = LogManager.getLogger(NotificationFileServiceImpl.class);

	/** The i notification file dao. */
	@Autowired
	INotificationFileDao iNotificationFileDao;

	/** The i push notification service. */
	@Autowired
	IPushNotificationService iPushNotificationService;
	
	@Autowired
	IPushNotificationAsyncService iPushNotificationAsyncService;

	/** The service. */
	@Autowired
	private IDeviceInfoDao iDeviceInfoDao;

	/** The Notification File HDFS dao. */
	@Autowired(required = false)
	private INotificationFileHDFSDao iNotificationFileHDFSDao;

	/**
	 * Insert notification file.
	 *
	 * @param notificationFiles
	 *            the notification files
	 * @return the notification file
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public NotificationFile insertNotificationFile(NotificationFile notificationFiles) {
		try {
			return iNotificationFileDao.insertNotificationFile(notificationFiles);
		} catch (DaoException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Gets the all notification file list.
	 *
	 * @param lLimit
	 *            the l limit
	 * @param uLimit
	 *            the u limit
	 * @return the all notification file list
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public List<NotificationFile> getAllNotificationFileList(Integer lLimit, Integer uLimit) {
		try {
			return iNotificationFileDao.getAllNotificationFileList(lLimit, uLimit);
		} catch (DaoException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}

	}

	/**
	 * Delete notification file by id.
	 *
	 * @param ids
	 *            the ids
	 * @return true, if successful
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@Transactional
	public boolean deleteNotificationFileById(List<Integer> ids) {
		boolean isDeleted = true;
		try {
			for (Integer id : ids) {
				NotificationFile notificationFile = findByPk(id);
				if (notificationFile != null) {
					notificationFile.setDeleted(true);
					notificationFile.setModificationTime(new Date());
					iNotificationFileDao.update(notificationFile);
				}
			}
		} catch (DaoException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return isDeleted;
	}

	/**
	 * Find by pk.
	 *
	 * @param id
	 *            the id
	 * @return the notification file
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public NotificationFile findByPk(Integer id) {
		try {
			return iNotificationFileDao.findByPK(id);
		} catch (DaoException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Download feed back file by file id.
	 *
	 * @param id
	 *            the id
	 * @return the string
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@Transactional
	public String downloadFeedBackFileByFileId(Integer id) {
		try {
			logger.info("going to downloadFeedBackFileById: {}", id);
			List<PushNotification> notificationList = iPushNotificationService.getNotificationFileDetailByFileId(id,
					PushNotificationConstants.MIN_UPPER_LIMIT, PushNotificationConstants.MIN_LOWER_LIMIT);
			if (notificationList.isEmpty()) {
				throw new RestException(ConsumerFeedbackConstant.FILE_DOES_NOT_EXIST + " for id = " + id);
			}
			return ConsumerFeedbackUtils.getCsvData(notificationList);
		} catch (Exception e) {
			logger.error("error on downloadFeedBackFileById: {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Download feed back file template.
	 *
	 * @return the string
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public String downloadFeedBackFileTemplate() {
		try {
			logger.info("going to downloadFeedBackFileTemplate");
			return ConsumerFeedbackUtils.getFeedbackTemplateData();
		} catch (Exception e) {
			logger.error("error on downloadFeedBackFileById: {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Gets the feed back file detail by id.
	 *
	 * @param id
	 *            the id
	 * @param llimit
	 *            the llimit
	 * @param ulimit
	 *            the ulimit
	 * @return the feed back file detail by id
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@Transactional
	public List<FeedbackFileWrapper> getFeedBackFileDetailById(Integer id, Integer llimit, Integer ulimit) {
		try {
			List<PushNotification> fileDetail = iPushNotificationService.getNotificationFileDetailByFileId(id, llimit,
					ulimit);
			if (fileDetail.isEmpty()) {
				throw new RestException(ConsumerFeedbackConstant.FILE_DETAIL_NOT_EXIST + " for id = " + id);
			}
			return ConsumerFeedbackUtils.getFileDetail(fileDetail);
		} catch (RestException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Upload feedback notification file.
	 *
	 * @param fileName
	 *            the file name
	 * @param date
	 *            the date
	 * @param inputFile
	 *            the input file
	 * @return the string
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@Transactional
	public String uploadFeedbackNotificationFile(String fileName, Date scheduledTime, InputStream inputFile) {
		try {
			ByteArrayOutputStream baos = ConsumerFeedbackUtils.getByteArrayOutputStream(inputFile);
			InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
			InputStream inputStreamCopy = new ByteArrayInputStream(baos.toByteArray());

			if (ConsumerFeedbackUtils.isValidScheduledtime(scheduledTime)) {

				List<DeviceInfo> deviceInfoList = getDeviceInfoList(inputStream);
				User loggedInUser = UserContextServiceImpl.getUserInContext();

				if (ConsumerFeedbackUtils.isValidDeviceInfoList(deviceInfoList)
						&& ConsumerFeedbackUtils.isValidUserInfo(loggedInUser)) {
					// Save file in hdfs
					persistFeedbackCSVFileInHdfs(ConsumerFeedbackUtils.getFilePath()+"_"+fileName, inputStreamCopy);

					Date fileUploadTime = new Date();
					NotificationFile notificationFile = insertNotificationFile(
							ConsumerFeedbackUtils.prepareNotificationfileObject(fileName,
									ConfigUtils.getString(ConfigEnum.HDFS_FEEDBACK_FILE_UPLOAD_PATH), loggedInUser,
									fileUploadTime));

					insertPushNotification(scheduledTime, deviceInfoList, loggedInUser, fileUploadTime,
							notificationFile);
				}
			}
		} catch (RestException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Inside @Method uploadFeedbackNotification Exception:{}" + ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return ForesightConstants.SUCCESS_JSON;
	}

	private void insertPushNotification(Date scheduledTime, List<DeviceInfo> deviceInfoList, User loggedInUser,
			Date fileUploadTime, NotificationFile notificationFile) {
		deviceInfoList.forEach(deviceInfo -> {
			PushNotification pushNotification = ConsumerFeedbackUtils.preparePushNotificationObject(loggedInUser,
					scheduledTime, deviceInfo, notificationFile, fileUploadTime);
			try {
				iPushNotificationAsyncService.sendNotificationAndUpdateStatus(pushNotification);
			} catch (RestException e) {
				logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			}
		});
	}

	/**
	 * Gets the Device Info list.
	 *
	 * @param inputStream
	 *            the input stream
	 * @return the Device Info list
	 * @throws RestException
	 *             the rest exception
	 */
	private List<DeviceInfo> getDeviceInfoList(InputStream inputStream) {
		List<String[]> imeiAndImsiList = ConsumerFeedbackUtils.getImeiAndImsiList(inputStream);

		if (imeiAndImsiList.isEmpty()) {
			throw new RestException(ConsumerFeedbackConstant.FILE_IS_EMPTY);
		}
		return imeiAndImsiList	.stream()
								.map(e -> iDeviceInfoDao.getDeviceInfoByImeiAndImsi(
										e[ConsumerFeedbackConstant.FIRST_INDEX],
										e[ConsumerFeedbackConstant.SECOND_INDEX]))
								.filter(Objects::nonNull)
								.collect(Collectors.toList());

	}

	/**
	 * Persist Feedback Notification files In HDFS.
	 *
	 * @param hdfsFilePath
	 *            Path in HDFS, where file supposed to store
	 * @param inputStream
	 *            the inputStream of file to insert in HDFS
	 * @return file Path where file got save in hdfs
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void persistFeedbackCSVFileInHdfs(String hdfsFilePath, InputStream inputStream) throws IOException {
		logger.info("Going to persist feedback CSV File");
		iNotificationFileHDFSDao.persistNotificationFileToHDFS(inputStream, hdfsFilePath);
		logger.info("Done persist feedback CSV Files hdfs file Path : {}", hdfsFilePath);
	}

	/**
	 * Gets the feedback notification file count.
	 *
	 * @return the feedback notification file count
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public Long getFeedbackNotificationFileCount() {
		try {
			return iNotificationFileDao.getFeedbackNotificationFileCount();
		} catch (DaoException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Gets the feed back notification file detail list count.
	 *
	 * @param id
	 *            the id
	 * @return the feed back notification file detail list count
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@Transactional
	public Long getFeedBackNotificationFileDetailListCount(Integer id) {
		try {
			return iPushNotificationService.getFeedBackNotificationFileDetailListCount(id);
		} catch (RestException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}
}
