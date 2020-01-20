package com.inn.foresight.module.nv.bbm.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.unit.Duration;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.bbm.constant.BBMConstants;

public class BBMUtils extends BBMConstants {
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(BBMUtils.class);

	/**
	 * Send http post request.
	 *
	 * @param url
	 *            the url
	 * @param reqEntity
	 *            the http entity
	 * @param isToEnableTimeOut
	 *            the is to enable time out
	 * @param duration
	 *            the duration
	 * @return the http post request
	 */
	public static HttpPostRequest sendHttpPostRequest(String url, HttpEntity reqEntity, boolean isToEnableTimeOut,
			Duration duration) {
		HttpPostRequest httpPostRequest = new HttpPostRequest(url, reqEntity);
		if (duration != null) {
			httpPostRequest.setConnectionTimeout(duration);
			httpPostRequest.setEnableTimeout(isToEnableTimeOut);
		}
		return httpPostRequest;
	}

	public static ByteArrayOutputStream getByteArrayOutputStream(InputStream inputFile) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputFile.read(buffer)) > BBMConstants.MINUS_ONE) {
				baos.write(buffer, BBMConstants.ZERO, len);
			}
			baos.flush();
		} catch (IOException e) {
			logger.error("Err in getByteArrayOutputStream : {} ", ExceptionUtils.getStackTrace(e));
		}
		return baos;
	}

	public static boolean isValidDeviceRegistrationFile(InputStream inputStream, String fileName) {
		boolean isValid = Boolean.FALSE;
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		try {
			while ((line = br.readLine()) != null) {
				isValid = Boolean.TRUE;
				break;
			}
		} catch (IOException e) {
			logger.error("getCsvRowsFromInputStream IOException {}", ExceptionUtils.getStackTrace(e));
		}
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error(
						"Exception inside isValidDeviceRegistrationFile while closing the InputStream  Stream  : {}",
						ExceptionUtils.getStackTrace(e));
			}
		}
		return isValid && isValidFileName(fileName) && isFileContainsRegHeader(line);
	}

	public static boolean isValidDeviceDeRegistrationFile(InputStream inputStream, String fileName) {
		boolean isValid = Boolean.FALSE;
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		try {
			while ((line = br.readLine()) != null) {
				isValid = Boolean.TRUE;
				break;
			}
		} catch (IOException e) {
			logger.error("getCsvRowsFromInputStream IOException {}", ExceptionUtils.getStackTrace(e));
		}
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error("error closing the InputStream  Stream  : {}", ExceptionUtils.getStackTrace(e));
			}
		}
		return isValid && isValidFileName(fileName) && isFileContainsDeRegHeader(line);
	}

	private static boolean isValidFileName(String fileName) {
		return fileName.contains(CSV) || fileName.contains(CSV_CAPS);
	}

	private static boolean isFileContainsRegHeader(String line) {
		if (line != null && line.split(CSV_SEPARATOR).length > MIN_HEADER_LENGTH) {
			return BBM_DEVICE_REG_HEADER.contains(line.split(CSV_SEPARATOR)[FIRST_INDEX].trim())
					&& BBM_DEVICE_REG_HEADER.contains(line.split(CSV_SEPARATOR)[SECOND_INDEX].trim())
					&& BBM_DEVICE_REG_HEADER.contains(line.split(CSV_SEPARATOR)[THIRD_INDEX].trim())
					&& BBM_DEVICE_REG_HEADER.contains(line.split(CSV_SEPARATOR)[FOURTH_INDEX].trim());
		}
		return false;
	}

	private static boolean isFileContainsDeRegHeader(String line) {
		if (line != null && line.split(CSV_SEPARATOR).length > MIN_HEADER_LENGTH) {
			return (BBM_DEVICE_DE_REG_HEADER.contains(line.split(CSV_SEPARATOR)[FIRST_INDEX].trim())
					&& BBM_DEVICE_DE_REG_HEADER.contains(line.split(CSV_SEPARATOR)[SECOND_INDEX].trim()))
					&& (BBM_DEVICE_DE_REG_HEADER.contains(line.split(CSV_SEPARATOR)[THIRD_INDEX].trim())
					&& BBM_DEVICE_DE_REG_HEADER.contains(line.split(CSV_SEPARATOR)[FOURTH_INDEX].trim()))
					&& isValidDeRegCauseHeader(line);
		}
		return false;
	}

	private static boolean isValidDeRegCauseHeader(String line) {
		if (line.split(CSV_SEPARATOR).length > 4) {
			String causeHeader = line.split(CSV_SEPARATOR)[FIFTH_INDEX].trim();
			if (causeHeader != null && !"".equalsIgnoreCase(causeHeader) && !"null".equalsIgnoreCase(causeHeader)) {
				return BBM_DEVICE_DE_REG_HEADER.contains(causeHeader);
			}
		}

		return true;
	}

	public static List<DeviceInfo> getDeviceData(InputStream inputStream) {
		List<DeviceInfo> deviceInfoList = new ArrayList<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		String csvHeader = null;
		try {
			while ((line = br.readLine()) != null) {
				if (csvHeader != null) {
					deviceInfoList.add( getDeviceInfoObject(line, csvHeader));
				} else {
					csvHeader = line;
				}
			}
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (IOException e) {
			logger.error("IOException in BBM getDeviceData : {}", ExceptionUtils.getStackTrace(e));
		}
		return deviceInfoList;
	}

	private static DeviceInfo getDeviceInfoObject(String line, String csvHeader) {
		DeviceInfo deviceInfo = new DeviceInfo();
		String[] dataArray = line.split(CSV_SEPARATOR);
		String[] headerArray = csvHeader.split(CSV_SEPARATOR);
		if (dataArray.length > MIN_HEADER_LENGTH) {
			for (int index = 0; index < headerArray.length; index++) {
				setDeviceInfo(deviceInfo, dataArray, headerArray, index);
			}
			if ((StringUtils.isNotEmpty(deviceInfo.getDeviceId()) && StringUtils.isNotEmpty(deviceInfo.getImei())
					&& StringUtils.isNotEmpty(deviceInfo.getMsisdn()))
					&& (deviceInfo.getRegistrationDate() != null || deviceInfo.getDeregistrationDate() != null)) {
				return deviceInfo;
			}
		}
		return null;

	}

	private static void setDeviceInfo(DeviceInfo deviceInfo, String[] dataArray, String[] headerArray, int index) {
		switch (headerArray[index].trim()) {
		case HEADER_DEVICE_ID:
			deviceInfo.setDeviceId(dataArray[index]);
			break;
		case HEADER_BBMID:
			deviceInfo.setImei(dataArray[index]);
			break;
		case HEADER_MDN:
			deviceInfo.setMsisdn(dataArray[index]);
			break;
		case HEADER_REG_DATE:
			setRegistrationDate(deviceInfo, dataArray, index);
			break;
		case HEADER_DE_REG_DATE:
			setDeregistrationDate(deviceInfo, dataArray, index);
			break;
		case HEADER_DE_REG_CAUSE:
			deviceInfo.setRemark(dataArray[index]);
			break;
		default:
		}
	}

	private static void setDeregistrationDate(DeviceInfo deviceInfo, String[] dataArray, int i) {
		String deregDate = dataArray[i];
		if (deregDate != null && !deregDate.equalsIgnoreCase("")) {
			try {
				deviceInfo.setDeregistrationDate(new Date(Long.parseLong(deregDate) * MILLISECONDS));
			} catch (Exception e) {
				logger.error("error when set DeRegistrationDate  : {}", ExceptionUtils.getStackTrace(e));
			}
		}
	}

	private static void setRegistrationDate(DeviceInfo deviceInfo, String[] dataArray, int i) {
		String regDate = dataArray[i];
		if (regDate != null && !regDate.equalsIgnoreCase("")) {
			try {
				deviceInfo.setRegistrationDate(new Date(Long.parseLong(regDate) * MILLISECONDS));
			} catch (Exception e) {
				logger.error("error when set RegistrationDate  : {}", ExceptionUtils.getStackTrace(e));
			}
		}
	}

	public static void writeFileToDisk(InputStream inputStream, String fileName) {
		String filePath = getFilePath(fileName);
		logger.info("Going to write BBM file on disk Path: {}", filePath);
		File file = new File(filePath);
		Boolean isFileCreated = false;
		try {
			if (!file	.getParentFile()
						.exists()) {
				file.getParentFile()
					.mkdirs();
			}
			if (!file.exists()) {
				isFileCreated = file.createNewFile();
			}
			if (!isFileCreated.booleanValue()) {
				return;
			}
			try (FileOutputStream outputStream = new FileOutputStream(file)) {
				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
			}
		} catch (IOException e) {
			logger.error("IOException in writeFileToDisk:{}", ExceptionUtils.getStackTrace(e));
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("IOException in writeFileToDisk while closing inputStream:{}",
							ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}

	public static String getFilePath(String fileName) {
		return ConfigUtils.getString(ConfigUtil.BBM_FILE_PATH) + getDate() + "_" + fileName;
	}

	private static String getDate() {
		return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
	}
}
