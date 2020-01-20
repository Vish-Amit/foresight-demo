package com.inn.foresight.module.nv.app.utils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTimeConstants;

import com.inn.commons.lang.StringUtils;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.model.DeviceInfo;

/** The Class AppUtils. */

public class AppUtils extends AppConstants {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(AppUtils.class);

	/**
	 * Getting 15 digit Unique Id from Id with given prefix.
	 *
	 * @param id     the id
	 * @param prefix the prefix
	 * @return 15 digit Unique Id with given prefix added
	 */
	public static String generateUniqueIdWithPrefix(Integer id, String prefix) {
		return prefix + StringUtils.leftPad(Integer.toString(id), DEVICE_ID_LENGTH, DEVICE_ID_PADDING_CONSTANT);
	}

	
	public static boolean isValidLicense(Integer maxLicenseValidity, Date creationTime) {
		long difference = new Date().getTime() - creationTime.getTime();
		long days = (difference / DateTimeConstants.MILLIS_PER_DAY);
		return maxLicenseValidity > days;
	}

	/**
	 * Gets the license days to expiry.
	 *
	 * @param maxLicenseValidity the max license validity
	 * @param creationTime       the creation time
	 * @return No. of days remaining to expire a license
	 */
	public static Integer getLicenseDaysToExpiry(Integer maxLicenseValidity, Date creationTime) {
		long difference = new Date().getTime() - creationTime.getTime();
		int days = (int) (difference / DateTimeConstants.MILLIS_PER_DAY);
		return maxLicenseValidity - days;
	}

	/**
	 * Gets the device id list.
	 *
	 * @param deviceInfoList the device info list
	 * @return the device id list
	 */
	public static List<Integer> getDeviceIdList(List<DeviceInfo> deviceInfoList) {
		return deviceInfoList.parallelStream().map(DeviceInfo::getId).collect(Collectors.toList());
	}
}