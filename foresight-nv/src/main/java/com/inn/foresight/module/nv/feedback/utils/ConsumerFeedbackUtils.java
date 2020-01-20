package com.inn.foresight.module.nv.feedback.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.feedback.constants.ConsumerFeedbackConstant;
import com.inn.foresight.module.nv.feedback.model.ConsumerFeedback;
import com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper;
import com.inn.foresight.module.nv.feedback.wrapper.FeedbackFileWrapper;
import com.inn.foresight.module.nv.pushnotification.constants.PushNotificationConstants;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.ModuleName;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.Status;
import com.inn.foresight.module.nv.pushnotification.notificationfile.model.NotificationFile;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.um.user.model.User;

/**
 * The Class ConsumerFeedbackUtils.
 * 
 * @author innoeye
 */
public class ConsumerFeedbackUtils {
	
	private ConsumerFeedbackUtils() {
		
	}
	
	/** The Buffer size. */
	private static final int BUFFER_SIZE = 1024;
	/** The logger. */
	private static Logger logger = LogManager.getLogger(ConsumerFeedbackUtils.class);

	/**
	 * Gets the csv rows from input stream.
	 *
	 * @param inputStream
	 *            the input stream
	 * @return the csv rows from input stream
	 */
	private static List<String[]> getCsvRowsFromInputStream(InputStream inputStream) {
		List<String[]> rows = new ArrayList<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		String data = null;
		try {
			while ((data = br.readLine()) != null) {
				String[] dataArray = data.split(ConsumerFeedbackConstant.CSV_FILE_SPLITTER);
				if(dataArray!=null&&dataArray.length>ConsumerFeedbackConstant.MINIMUM_LENGTH) {
				rows.add(new String[] { dataArray[ConsumerFeedbackConstant.FIRST_INDEX],
						dataArray[ConsumerFeedbackConstant.SECOND_INDEX] });
				}
			}
		} catch (IOException e) {
			logger.error("getCsvRowsFromInputStream IOException ", ExceptionUtils.getStackTrace(e));
		}
		return rows;
	}

	/**
	 * Removes the header.
	 *
	 * @param imeiAndImsiList
	 *            the imei and imsi list
	 */
	private static void removeHeader(List<String[]> imeiAndImsiList) {
		if (!imeiAndImsiList.isEmpty()) {
			imeiAndImsiList.remove(ConsumerFeedbackConstant.FIRST_INDEX);
		}
	}

	/**
	 * Checks if is valid CSV file.
	 *
	 * @param csvRows
	 *            the csv rows
	 * @return true, if is valid CSV file
	 */
	private static boolean isValidCSVFile(List<String[]> csvRows) {
			if (!csvRows.isEmpty()&&ConsumerFeedbackConstant.CSV_COLUMN_IMEI.equalsIgnoreCase(
					csvRows.get(ConsumerFeedbackConstant.FIRST_INDEX)[ConsumerFeedbackConstant.FIRST_INDEX])
					&& ConsumerFeedbackConstant.CSV_COLUMN_IMSI.equalsIgnoreCase(
							csvRows.get(ConsumerFeedbackConstant.FIRST_INDEX)[ConsumerFeedbackConstant.SECOND_INDEX])) {
				return Boolean.TRUE;
			}
		return Boolean.FALSE;
	}

	/**
	 * Prepare notification file object.
	 *
	 * @param fileName
	 *            the file name
	 * @param hdfsFilePath
	 *            the hdfs file path
	 * @param loggedInUser
	 *            the logged in user
	 * @param createdTime
	 *            the created time
	 * @return the notification file
	 */
	public static NotificationFile prepareNotificationfileObject(String fileName, String hdfsFilePath,
			User loggedInUser, Date createdTime) {
		NotificationFile notificationFiles = new NotificationFile();
		notificationFiles.setFileName(fileName);
		notificationFiles.setFilePath(hdfsFilePath);
		notificationFiles.setCreationTime(createdTime);
		notificationFiles.setCreatedBy(loggedInUser);
		return notificationFiles;
	}

	/**
	 * Gets the byte array output stream.
	 *
	 * @param inputFile
	 *            the input file
	 * @return the byte array output stream
	 */
	public static ByteArrayOutputStream getByteArrayOutputStream(InputStream inputFile) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFFER_SIZE];
			int len;
			while ((len = inputFile.read(buffer)) > -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
		} catch (IOException e) {
			logger.error("Error in getByteArrayOutputStream : {} ", ExceptionUtils.getStackTrace(e));
		}
		return baos;
	}

	/**
	 * Gets the file detail.
	 *
	 * @param notificationList
	 *            the notification list
	 * @return the file detail
	 */
	public static List<FeedbackFileWrapper> getFileDetail(List<PushNotification> notificationList) {
		return notificationList	.stream()
											.map(e -> new FeedbackFileWrapper(e	.getDeviceInfo()
																				.getImei(),
													e	.getDeviceInfo()
														.getImsi(),
													e	.getStatus()
														.name()))
											.collect(Collectors.toList());
	}

	/**
	 * Gets the csv data.
	 *
	 * @param notificationList
	 *            the notification list
	 * @return the csv data
	 */
	public static String getCsvData(List<PushNotification> notificationList) {
		List<FeedbackFileWrapper> feedbackWrapperList = getFileDetail(notificationList);
		String out = ConsumerFeedbackConstant.CSV_COLUMN_IMEI + ConsumerFeedbackConstant.CSV_FILE_SPLITTER
				+ ConsumerFeedbackConstant.CSV_COLUMN_IMSI + ConsumerFeedbackConstant.CSV_FILE_SPLITTER
				+ ConsumerFeedbackConstant.CSV_COLUMN_STATUS + ConsumerFeedbackConstant.NEWL_LINE;
		
		Optional<String> optional = feedbackWrapperList.stream()
		.map(e -> e	.getImei()
					.concat(ConsumerFeedbackConstant.CSV_FILE_SPLITTER)
					.concat(e.getImsi())
					.concat(ConsumerFeedbackConstant.CSV_FILE_SPLITTER)
					.concat(e.getStatus())
					.concat(ConsumerFeedbackConstant.NEWL_LINE))
		.reduce((a, b) -> a.concat(b));
		if(optional.isPresent()) {
			out=out.concat(optional.get());
		}

		return out;
	}

	/**
	 * Gets the imei and imsi list.
	 *
	 * @param inputStream
	 *            the input stream
	 * @return the imei and imsi list
	 * @throws RestException
	 *             the rest exception
	 */
	public static List<String[]> getImeiAndImsiList(InputStream inputStream) {
		List<String[]> csvRows = getCsvRowsFromInputStream(inputStream);
		if (isValidCSVFile(csvRows)) {
			removeHeader(csvRows);
		} else {
			try {
				throw new RestException(ConsumerFeedbackConstant.CSV_DOES_NOT_CONTAIN_IMEI_IMSI);
			} catch (Exception e) {
				logger.error("Exception in getImeiAndImsiList{}",ExceptionUtils.getStackTrace(e));
			}
		}

		return csvRows;
	}

	/**
	 * Gets the feedback template data.
	 *
	 * @return the feedback template data
	 */
	public static String getFeedbackTemplateData() {
		return ConsumerFeedbackConstant.CSV_COLUMN_IMEI	.concat(ConsumerFeedbackConstant.CSV_FILE_SPLITTER)
														.concat(ConsumerFeedbackConstant.CSV_COLUMN_IMSI);
	}

	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public static String getFilePath() {
		return ConfigUtils.getString(ConfigEnum.HDFS_FEEDBACK_FILE_UPLOAD_PATH);
	}

	/**
	 * Validate scheduledtime.
	 *
	 * @param scheduledTime
	 *            the scheduled time
	 * @throws RestException
	 *             the rest exception
	 */
	public static boolean isValidScheduledtime(Date scheduledTime) {
		if (scheduledTime.before(new Date())) {
			throw new RestException(ConsumerFeedbackConstant.SCHEDULED_TIME_MUST_BE_GREATER_THAN_CURRENT_TIME);
		}
		return Boolean.TRUE;
	}

	/**
	 * Gets the consumer feedback wrapper.
	 *
	 * @param decryptedJson
	 *            the decrypted json
	 * @return the consumer feedback wrapper
	 */
	public static ConsumerFeedback getConsumerFeedbackWrapper(ConsumerFeedbackWrapper feedBackWrapper,ConsumerFeedback feedback) {
		
		setTime(feedback, feedBackWrapper);
		feedback.setDeviceOS(feedBackWrapper.getDeviceOS());
		feedback.setImei(feedBackWrapper.getImei());
		feedback.setImsi(feedBackWrapper.getImsi());
		feedback.setMobileNumber(feedBackWrapper.getMobileNumber());
		feedback.setMake(feedBackWrapper.getMake());
		feedback.setModel(feedBackWrapper.getModel());
		feedback.setNotes(feedBackWrapper.getNotes());
		feedback.setTestArea(feedBackWrapper.getTestArea());
		feedback.setProblemType(feedBackWrapper.getProblemType());
		feedback.setProblemSubtype(feedBackWrapper.getProblemSubtype());
		feedback.setFeedbacklocation(feedBackWrapper.getFeedbacklocation());
		feedback.setNetworktype(feedBackWrapper.getNetworkType());
		feedback.setMcc(feedBackWrapper.getMcc());
		feedback.setMnc(feedBackWrapper.getMnc());
		feedback.setLatitude(feedBackWrapper.getLatitude());
		feedback.setLongitude(feedBackWrapper.getLongitude());
		feedback.setFeedBackType(feedBackWrapper.getFeedbackType());
		feedback.setStarRating(feedBackWrapper.getStarRating());
		feedback.setStarRatingData(feedBackWrapper.getStarRatingData());
		feedback.setStarRatingCvg(feedBackWrapper.getStarRatingCoverage());
		feedback.setVcCallDrop(feedBackWrapper.getVoiceCallDrop());
		feedback.setVoiceMute(feedBackWrapper.getVoiceMute());
		feedback.setVcOnewyAudio(feedBackWrapper.getVoiceOneWayAudio());
		feedback.setVcPoorAudio(feedBackWrapper.getVoicePoorAudio());
		feedback.setVcUnableToCall(feedBackWrapper.getVoiceUnableToMakeCall());
		feedback.setDataSlowSpeed(feedBackWrapper.getDataSlowSpeed());
		feedback.setDataUnavleToCon(feedBackWrapper.getDataUnableToConnect());
		feedback.setCvgPoorCvgInd(feedBackWrapper.getCoveragePoorCoverageIndoor());
		feedback.setCvgPoorCvgOut(feedBackWrapper.getCoveragePoorCoverageOutdoor());
		feedback.setPci(feedBackWrapper.getPci());
		feedback.setCellId(feedBackWrapper.getCellId());
		feedback.setTac(feedBackWrapper.getTac());
		feedback.setWificonnected(feedBackWrapper.getWifiConnected());
		feedback.setWifiName(feedBackWrapper.getWifiName());
		feedback.setRssiWifi(feedBackWrapper.getRssiWifi());
		feedback.setSnrWifi(feedBackWrapper.getSnrWifi());
		feedback.setUlRate(feedBackWrapper.getUlRate());
		feedback.setDlRate(feedBackWrapper.getDlRate());
		feedback.setVersionName(feedBackWrapper.getVersionName());
		feedback.setNvModule(feedBackWrapper.getNvModule());
		feedback.setBaseband(feedBackWrapper.getBaseband());
		feedback.setBuildNumber(feedBackWrapper.getBuildNumber());
		feedback.setRsrpLTE(feedBackWrapper.getRsrpLTE());
		feedback.setRsrqLTE(feedBackWrapper.getRsrqLTE());
		feedback.setRssiLTE(feedBackWrapper.getRssiLTE());
		feedback.setSinrLTE(feedBackWrapper.getSinrLTE());
		feedback.setRxLevel2G(feedBackWrapper.getRxLevel2G());
		feedback.setRxQuality2G(feedBackWrapper.getRxQuality2G());
		feedback.setRscp3G(feedBackWrapper.getRscp3G());
		feedback.setEcno3G(feedBackWrapper.getEcno3G());
		feedback.setChargerstatus(feedBackWrapper.getChargerConnectedStatus());
		feedback.setBatteryLevel(feedBackWrapper.getBatteryLevel());
		feedback.setVoltage(feedBackWrapper.getVoltage());
		feedback.setTemperature(feedBackWrapper.getTemperature());
		feedback.setBand(feedBackWrapper.getBand());
		setExtraParameter(feedback, feedBackWrapper);
		return feedback;
	}

	/**
	 * Sets the time.
	 *
	 * @param feedback
	 *            the feedback
	 * @param feedBackWrapper
	 *            the feed back wrapper
	 */
	private static void setTime(ConsumerFeedback feedback, ConsumerFeedbackWrapper feedBackWrapper) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(feedBackWrapper.getFeedbacktime());
		feedback.setFeedbacktime(calendar.getTime());
	}

	/**
	 * Sets the extra parameter.
	 *
	 * @param feedback
	 *            the feedback
	 * @param feedBackWrapper
	 *            the feed back wrapper
	 */
	private static void setExtraParameter(ConsumerFeedback feedback, ConsumerFeedbackWrapper feedBackWrapper) {
		feedback.setDeviceId(feedBackWrapper.getDeviceId());
		feedback.setAndroidId(feedBackWrapper.getAndroidId());
		feedback.setEnterprise(feedBackWrapper.getIsEnterprise());
		feedback.setLayer3enabled(feedBackWrapper.getIsLayer3Enabled());
		feedback.setLocationaccuracy(feedBackWrapper.getLocationAccuracy());
		feedback.setLocationaltitude(feedBackWrapper.getLocationAltitude());
		feedback.setChipset(feedBackWrapper.getChipset());
		feedback.setSerialnumber(feedBackWrapper.getSerialNumber());
		feedback.setSocmodel(feedBackWrapper.getSocModel());
		feedback.setCorearchitecture(feedBackWrapper.getCoreArchitecture());
		feedback.setDevicefingerprint(feedBackWrapper.getDeviceFingerprint());
		feedback.setDualsim(feedBackWrapper.getIsPhoneDualSim());
		feedback.setNetworksubtype(feedBackWrapper.getNetworkSubType());
		feedback.setAutodatetimeenabled(feedBackWrapper.getIsAutoDateTimeEnabled());
		feedback.setGpsenabled(feedBackWrapper.getIsGpsEnabled());
		feedback.setMacaddress(feedBackWrapper.getMacAddress());
		feedback.setBssid(feedBackWrapper.getBssid());
		feedback.setNeighbourinfo(feedBackWrapper.getNeighbourInfo());
		feedback.setCgilte(feedBackWrapper.getCgiLTE());
		feedback.seteNodeblte(feedBackWrapper.geteNodeBLTE());
		feedback.setInternalip(feedBackWrapper.getInternalIp());
		feedback.setAvglinkspeed(feedBackWrapper.getAvgLinkSpeed());
		feedback.setWifinetworktype(feedBackWrapper.getNetworkTypeWhenWifi());
		
		feedback.setStarRatingVoIp(feedBackWrapper.getStarRatingVoIp());
		feedback.setVoIpSkypeUnableToMakeCall(feedBackWrapper.isVoIpSkypeUnableToMakeCall());
		feedback.setVoIpSkypeCallDrop(feedBackWrapper.isVoIpSkypeCallDrop());
		feedback.setVoIpSkypePoorAudio(feedBackWrapper.isVoIpSkypePoorAudio());
		feedback.setVoIpSkypeMute(feedBackWrapper.isVoIpSkypeMute());
		feedback.setVoIpSkypeOneWayAudio(feedBackWrapper.isVoIpSkypeOneWayAudio());
		feedback.setVoIpViberUnableToMakeCall(feedBackWrapper.isVoIpViberUnableToMakeCall());
		feedback.setVoIpViberCallDrop(feedBackWrapper.isVoIpViberCallDrop());
		feedback.setVoIpViberPoorAudio(feedBackWrapper.isVoIpViberPoorAudio());
		feedback.setVoIpViberMute(feedBackWrapper.isVoIpViberMute());
		feedback.setVoIpViberOneWayAudio(feedBackWrapper.isVoIpViberOneWayAudio());
		feedback.setVoIpWhatsAppUnableToMakeCall(feedBackWrapper.isVoIpWhatsAppUnableToMakeCall());
		feedback.setVoIpWhatsAppCallDrop(feedBackWrapper.isVoIpWhatsAppCallDrop());
		feedback.setVoIpWhatsAppPoorAudio(feedBackWrapper.isVoIpWhatsAppPoorAudio());
		feedback.setVoIpWhatsAppMute(feedBackWrapper.isVoIpWhatsAppMute());
		feedback.setVoIpWhatsAppOneWayAudio(feedBackWrapper.isVoIpWhatsAppOneWayAudio());
		feedback.setFloorNo(feedBackWrapper.getFloorNo());
		
		feedback.setVoIpRcsUTMCToRcs(feedBackWrapper.getVoIpRcsUTMCToRcs());
		feedback.setVoIpRcsUTMCToNonRcs(feedBackWrapper.getVoIpRcsUTMCToNonRcs());
		feedback.setVoIpRcsUTMGroupCall(feedBackWrapper.getVoIpRcsUTMGroupCall());
		feedback.setVoIpRcsPoorAudio(feedBackWrapper.getVoIpRcsPoorAudio());
		feedback.setVoIpRcsCallDisconnect(feedBackWrapper.getVoIpRcsCallDisconnect());
		feedback.setVoIpRcsUTSMsgToRcs(feedBackWrapper.getVoIpRcsUTSMsgToRcs());
		feedback.setVoIpRcsUTSMsgToNonRcs(feedBackWrapper.getVoIpRcsUTSMsgToNonRcs());
		feedback.setVoIpRcsUTMGroupChat(feedBackWrapper.getVoIpRcsUTMGroupChat());
		feedback.setVoIpRcsUTSMultimedia(feedBackWrapper.getVoIpRcsUTSMultimedia());
		
		feedback.setHighSpeedData(feedBackWrapper.getHighSpeedData());
		feedback.setExcellentCoverage(feedBackWrapper.getExcellentCoverage());
		feedback.setExcellentAudioQuality(feedBackWrapper.getExcellentAudioQuality());
		feedback.setPoorCoverage(feedBackWrapper.getPoorCoverage());
		feedback.setApnName(feedBackWrapper.getApnName());
		
		feedback.setStarRatingVoiceLine(feedBackWrapper.getStarRatingVoiceLine());
		feedback.setStarRatingVideoLine(feedBackWrapper.getStarRatingVideoLine());
		feedback.setStarRatingMessagingLine(feedBackWrapper.getStarRatingMessagingLine());
		feedback.setStarRatingVoiceRcs(feedBackWrapper.getStarRatingVoiceRcs());
		feedback.setStarRatingVideoRcs(feedBackWrapper.getStarRatingVideoRcs());
		feedback.setStarRatingMessagingRcs(feedBackWrapper.getStarRatingMessagingRcs());
		feedback.setStarRatingVoiceSkype(feedBackWrapper.getStarRatingVoiceSkype());
		feedback.setStarRatingVideoSkype(feedBackWrapper.getStarRatingVideoSkype());
		feedback.setStarRatingMessagingSkype(feedBackWrapper.getStarRatingMessagingSkype());
		feedback.setStarRatingVoiceWhatsapp(feedBackWrapper.getStarRatingVoiceWhatsapp());
		feedback.setStarRatingVideoWhatsapp(feedBackWrapper.getStarRatingVideoWhatsapp());
		feedback.setStarRatingMessagingWhatsapp(feedBackWrapper.getStarRatingMessagingWhatsapp());
		feedback.setStarRatingVoiceViber(feedBackWrapper.getStarRatingVoiceViber());
		feedback.setStarRatingVideoViber(feedBackWrapper.getStarRatingVideoViber());
		feedback.setStarRatingMessagingViber(feedBackWrapper.getStarRatingMessagingViber());
		feedback.setStarRatingRakutenTv(feedBackWrapper.getStarRatingRakutenTv());
		feedback.setStarRatingRakutenLive(feedBackWrapper.getStarRatingRakutenLive());
		feedback.setData(feedBackWrapper.getData());
		feedback.setSdkversion(feedBackWrapper.getSdkVersion());
		
		
	}

	/**
	 * Validate device info list.
	 *
	 * @param deviceInfoList
	 *            the device info list
	 * @throws RestException
	 *             the rest exception
	 */
	public static boolean isValidDeviceInfoList(List<DeviceInfo> deviceInfoList) {
		if (deviceInfoList.isEmpty()) {
			logger.error(ConsumerFeedbackConstant.NO_DEVICE_INFO_FOUND);
			throw new RestException(ConsumerFeedbackConstant.NO_DEVICE_INFO_FOUND);
		}
		return Boolean.TRUE;
	}

	/**
	 * Validate user info.
	 *
	 * @param loggedInUser
	 *            the logged in user
	 * @throws RestException
	 *             the rest exception
	 */
	public static boolean isValidUserInfo(User loggedInUser) {
		if (loggedInUser == null) {
			logger.error("User not found");
			throw new RestException(ConsumerFeedbackConstant.USER_NOT_FOUND);
		} else {
			logger.info("logged in user {} ", loggedInUser.toString());
		}
		return Boolean.TRUE;

	}

	/**
	 * Prepare push notification object.
	 *
	 * @param loggedInUser
	 *            the logged in user
	 * @param scheduledTime
	 *            the scheduled time
	 * @param deviceInfo
	 *            the device info
	 * @param notificationFiles
	 *            the notification files
	 * @param fileUploadTime
	 *            the file upload time
	 * @return the push notification
	 */
	public static PushNotification preparePushNotificationObject(User loggedInUser, Date scheduledTime,
			DeviceInfo deviceInfo, NotificationFile notificationFiles, Date fileUploadTime) {
		PushNotification pushNotification = new PushNotification();
		pushNotification.setCreatedBy(loggedInUser);
		pushNotification.setScheduledTime(scheduledTime);
		pushNotification.setStatus(Status.NEW);
		pushNotification.setCreationTime(fileUploadTime);
		pushNotification.setDeviceInfo(deviceInfo);
		pushNotification.setModuleName(ModuleName.NV_FEEDBACK);
		pushNotification.setNotificationFile(notificationFiles);
		Map<String, String> payLoad = new HashMap<>();
		payLoad.put(NVWorkorderConstant.KEY_MODULE_NAME, ModuleName.NV_FEEDBACK.name());
		payLoad.put(NVWorkorderConstant.KEY_NOTIFICATION_MESSAGE, PushNotificationConstants.RATE_YOUR_EXPERIENCE);
		pushNotification.setNotificationPayLoad(new Gson().toJson(payLoad));
		
		return pushNotification;
	}
}
