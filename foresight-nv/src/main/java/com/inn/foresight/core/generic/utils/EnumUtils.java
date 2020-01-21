package com.inn.foresight.core.generic.utils;

import java.util.ArrayList;
import java.util.List;

import com.inn.commons.Validate;
import com.inn.commons.exception.ValueNotFoundException;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Preconditions;

/**
 * The Class EnumUtils.
 */
public class EnumUtils {
	
	/**
	 * To enum.
	 *
	 * @param <T> the generic type
	 * @param value the value
	 * @param clz the clz
	 * @return the t
	 */
	public static <T extends Enum<T>> T toEnum(String value, Class<T> clz) {
		List<T> enums = org.apache.commons.lang3.EnumUtils.getEnumList(clz);
		for (T t : enums) {
			if (StringUtils.equalsIgnoreCase(t.name(), value)) {
				return t;
			}
		}
		throw new ValueNotFoundException(Constants.ENUM_NOT_FOUND + value);
	}

	/**
	 * Gets the enum by display name.
	 *
	 * @param <T> the generic type
	 * @param displayName the display name
	 * @param clz the clz
	 * @return the enum by display name
	 */
	public static <T extends Enum<T>> T getEnumByDisplayName(String displayName, Class<T> clz) { 
		Validate.checkNotEmpty(displayName, "Display Name could not be null or empty");

		List<T> enums = org.apache.commons.lang3.EnumUtils.getEnumList(clz);
		for (T t : enums) {
			if(t instanceof DisplayNameProperty) {
				String value = ((DisplayName) t).displayName();
				if (StringUtils.equalsIgnoreCase(value, displayName)) {
					if(!((DisplayNameProperty) t).isDisplayEnabled()) {
						throw new IllegalArgumentException("Enum can not be use as a DisplayName, set flag to true");
					}
					return t;
				}
			
			} else if (t instanceof DisplayName) {
				String value = ((DisplayName) t).displayName();
				if (StringUtils.equalsIgnoreCase(value, displayName)) {
					return t;
				}
			} else {
				throw new IllegalArgumentException("Enum should implement" + DisplayName.class.getSimpleName());
			}
		}
		throw new ValueNotFoundException(Constants.ENUM_NOT_FOUND + displayName);
	}
	
	/**
	 * Gets the enum by display name.
	 *
	 * @param <T> the generic type
	 * @param displayName the display name
	 * @param clz the clz
	 * @param errMsg the err msg
	 * @return the enum by display name
	 * @throws RestException the rest exception
	 */
	public static <T extends Enum<T>> T getEnumByDisplayName(String displayName, Class<T> clz, String errMsg) {
		Preconditions.checkNotEmpty(displayName, StringUtils.isNotEmpty(errMsg) ? errMsg : "Display Name could not be null or empty");

		List<T> enums = org.apache.commons.lang3.EnumUtils.getEnumList(clz);
		for (T t : enums) {
			if(t instanceof DisplayNameProperty) {
				String value = ((DisplayName) t).displayName();
				if (StringUtils.equalsIgnoreCase(value, displayName)) {
					if(!((DisplayNameProperty) t).isDisplayEnabled()) {
						throw new IllegalArgumentException("Enum can not be use as a DisplayName, set flag to true");
					}
					return t;
				}
			
			} else if (t instanceof DisplayName) {
				String value = ((DisplayName) t).displayName();
				if (StringUtils.equalsIgnoreCase(value, displayName)) {
					return t;
				}
			} else {
				throw new IllegalArgumentException(
						StringUtils.isNotEmpty(errMsg) ? errMsg : "Enum should implement " + DisplayName.class.getSimpleName());
			}
		}
		throw new RestException(StringUtils.isNotEmpty(errMsg) ? errMsg : Constants.ENUM_NOT_FOUND + displayName);
	}
	
	/**
	 * Gets the all display names.
	 *
	 * @param <T> the generic type
	 * @param clz the clz
	 * @return the all display names
	 */
	public static <T extends Enum<T>> List<String> getAllDisplayNames(Class<T> clz) {
		List<T> enums = org.apache.commons.lang3.EnumUtils.getEnumList(clz);
		List<String> displayNames = new ArrayList<>();
		for (T t : enums) {
			if (t instanceof DisplayNameProperty) {
				
				if(((DisplayNameProperty) t).isDisplayEnabled()) {
					String value = ((DisplayName) t).displayName();
					displayNames.add(value);
				}
			} else if (t instanceof DisplayName) {
				String value = ((DisplayName) t).displayName();
				
				displayNames.add(value);
			} else {
				throw new IllegalArgumentException("Enum should implement " + DisplayName.class.getSimpleName());
			}
		}
		return displayNames;
	}

}
