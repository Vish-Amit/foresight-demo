package com.inn.foresight.core.subscriber.service;

import java.util.Map;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.foresight.core.subscriber.utils.SubscriberHistoryConstants;

public interface ISubscriberSearch {

	Map<String, String> getIMSIByMSISDN(String msisdn);

	static <T> Boolean checkNullObject(T t) {
		return t != null;
	}

	static String trimMDNByCountryCode(String msisdn) {
		StringBuilder mdn = new StringBuilder(msisdn);
		String countryCode = ConfigUtils.getString(SubscriberHistoryConstants.CC_MDN_COUNTRY_CODE) != null
				? ConfigUtils.getString(SubscriberHistoryConstants.CC_MDN_COUNTRY_CODE)
				: SubscriberHistoryConstants.COUNTRY_CODE;
		Integer mdnLength = ConfigUtils.getInteger(SubscriberHistoryConstants.CC_MDN_DIGIT_LENGTH) != null
				? ConfigUtils.getInteger(SubscriberHistoryConstants.CC_MDN_DIGIT_LENGTH)
				: SubscriberHistoryConstants.ELEVEN_DIGIT;

		if (mdn.length() <= mdnLength) {
			mdn.insert(0, countryCode);
		}
		return mdn.toString();
	}
}
