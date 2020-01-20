package com.inn.foresight.module.nv.profile.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.module.nv.profile.constants.NVProfileConstants;
import com.inn.foresight.module.nv.profile.wrapper.NVProfile;

/**
 * The Class NVProfileDataUtils.
 *
 * @author innoeye
 * date - 26-Feb-2018 2:58:35 PM
 */
public class NVProfileDataUtils extends NVProfileConstants{
	
	/** Instantiates a new NV profile data utils. */
	protected NVProfileDataUtils() {}
	
	/** The logger. */
	private static Logger logger = LogManager.getLogger(NVProfileDataUtils.class);
	
	/**
	 * Getting 15 digit Unique Id from Id with given prefix.
	 *
	 * @param id the id
	 * @param prefix the prefix
	 * @return 15 digit Unique Id with given prefix added
	 */
	public static String generateUniqueIdWithPrefix(Integer id, String prefix) {
		return prefix + StringUtils.leftPad(Integer.toString(id), PROFILE_ID_LENGTH, PROFILE_ID_PADDING_CONSTANT);
	}
	
	/**
	 * Checks if is valid number.
	 *
	 * @param value the value
	 * @return true, if is valid number
	 */
	public static boolean isValidNumber(Double value) {
		return NumberUtils.isValidNumber(value) && NumberUtils.DOUBLE_ZERO.equals(value);
	}
	
	/**
	 * Gets the geography profile level map.
	 *
	 * @return the geography profile level map
	 */
	public static Map<String, String> getGeographyProfileLevelMap(String module,String operator){
		Map<String, String> geographyProfileLevelMap = new HashMap<>();
		geographyProfileLevelMap.put(GenericMapUtils.L4_TYPE, GEOGRAPHYL4 + PROFILE_MAP_KEY_DELIMITER + module + PROFILE_MAP_KEY_DELIMITER + operator);
		geographyProfileLevelMap.put(GenericMapUtils.L3_TYPE, GEOGRAPHYL3 + PROFILE_MAP_KEY_DELIMITER + module + PROFILE_MAP_KEY_DELIMITER + operator);
		geographyProfileLevelMap.put(GenericMapUtils.L2_TYPE, GEOGRAPHYL2 + PROFILE_MAP_KEY_DELIMITER + module + PROFILE_MAP_KEY_DELIMITER + operator);
		geographyProfileLevelMap.put(GenericMapUtils.L1_TYPE, GEOGRAPHYL1 + PROFILE_MAP_KEY_DELIMITER + module + PROFILE_MAP_KEY_DELIMITER + operator);
		return geographyProfileLevelMap;
	}

	/**
	 * Checks if is valid JSON.
	 *
	 * @param json the json
	 * @return true, if is valid JSON
	 */
	public static List<NVProfile> parseNVProfile(String json) {
		List<NVProfile> nvProfile = null;
		if (StringUtils.isNotEmpty(json) && StringUtils.isNotBlank(json)) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				nvProfile = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, NVProfile.class));
			} catch (Exception e) {
				logger.error("Invalid JSON {}", json);
			}
		}
		return nvProfile;
	}
	
}
