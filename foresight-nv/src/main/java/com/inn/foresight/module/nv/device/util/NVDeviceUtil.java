package com.inn.foresight.module.nv.device.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.grid.DegreeGrid;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.profile.model.NVProfileData;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;

/** The Class NVDeviceUtil. */
public class NVDeviceUtil extends DeviceConstant {

	/** Instantiates a new NV device util. */
	protected NVDeviceUtil() {
	}

	/** The logger. */
	private static Logger logger = LogManager.getLogger(NVDeviceUtil.class);

	/**
	 * Gets the filter and value map.
	 *
	 * @param operator            the operator
	 * @param geography the geography
	 * @param geographyId the geography id
	 * @return the filter and value map
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, List> getFilterValueMap(String operator, String geography, String geographyId,String userType) {
		Map<String, List> map = new HashMap<>();

		List<String> filterList = null;
		try {
			filterList = getFilterNameList(operator, geography,userType);
		} catch (RestException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		List<String> paramList = new ArrayList<>();
		List<Object> valueList = new ArrayList<>();

		for (String fiterName : filterList) {
			if (fiterName.equalsIgnoreCase(DeviceConstant.NVDEVICE_OPERATOR_FILTER)) {
				paramList.add(DeviceConstant.OPERATOR_PARAM);
				valueList.add(operator);
			}
			if (fiterName.equals(GEOGRAPHYL1_FILTER) || fiterName.equals(GEOGRAPHYL2_FILTER)
					|| fiterName.equals(GEOGRAPHYL3_FILTER) || fiterName.equals(GEOGRAPHYL4_FILTER)) {
				paramList.add(GEOGRAPHY_ID);
				valueList.add(Integer.parseInt(geographyId));
			}
		}
		map.put(FILTER_NAME, filterList);
		map.put(FILTER_PARAM, paramList);
		map.put(FILTER_VALUE, valueList);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, List> getFilterValueMap(List<String> deviceIdList) {
		Map<String, List> map = new HashMap<>();
		map.put(FILTER_NAME, Arrays.asList(DEVICE_ID_FILTER));
		map.put(FILTER_PARAM, Arrays.asList(DeviceConstant.DEVICE_ID_PARAM));
		map.put(FILTER_VALUE, Arrays.asList(deviceIdList));
		return map;
	}

	
	
	/**
	 * Gets the filter name list.
	 *
	 * @param operator            the operator
	 * @param geographyLevel the geography level
	 * @return the filter name list
	 * @throws RestException the rest exception
	 */
	public static List<String> getFilterNameList(String operator, String geographyLevel,String userType) {
		List<String> list = new ArrayList<>();

		if (operator != null && !OPERATOR_ALL.equalsIgnoreCase(operator)) {
			list.add(NVDEVICE_OPERATOR_FILTER);
		}
		if (geographyLevel != null) {
			list.add(getGeographyFilter(geographyLevel));
		}
		
		if(userType != null && !USER_TYPE_ALL.equalsIgnoreCase(userType)) {
			if(ENTERPRISE_USER.equalsIgnoreCase(userType)) {
				list.add(ENTERPRISE_FILTER);
			}else if(CONSUMER_USER.equalsIgnoreCase(userType)) {
				list.add(CONSUMER_FILTER);
			}
		}
		return list;
	}

	/**
	 * Gets the filter name list for device info.
	 *
	 * @param imei the imei
	 * @param imsi the imsi
	 * @return the filter name list for device info
	 * @throws RestException the rest exception
	 */
	public static List<String> getFilterNameListForDeviceInfo(String imei, String imsi) {
		List<String> list = new ArrayList<>();
		if (imei != null) {
			list.add(DEVICE_INFO_IMEI_FILTER);
		}
		if (imsi != null) {
			list.add(DEVICE_INFO_IMSI_FILTER);
		}
		return list;
	}

	/**
	 * Gets the filter and value map.
	 *
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param profileData            the profile data
	 * @return the filter and value map
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, List> getFilterAndValueMap(String geographyLevel, Integer geographyId,
			NVProfileData profileData,boolean isToAddDefaultOperatorFilter) {
		Map<String, List> filterMap = new HashMap<>();

		List<String> filterList = getFilterNameList(geographyLevel, profileData,isToAddDefaultOperatorFilter);
		List<String> paramList = new ArrayList<>();
		List<Object> valueList = new ArrayList<>();

		for (String fiterName : filterList) {
			if (fiterName.equals(GEOGRAPHYL1_FILTER) || fiterName.equals(GEOGRAPHYL2_FILTER)
					|| fiterName.equals(GEOGRAPHYL3_FILTER) || fiterName.equals(GEOGRAPHYL4_FILTER)) {
				paramList.add(GEOGRAPHY_ID);
				valueList.add(geographyId);
			} else if (profileData != null) {
				if (fiterName.equals(MAKE_FILTER)) {
					paramList.add(MAKE);
					valueList.add(profileData.getMake());
				} else if (fiterName.equals(MODEL_FILTER)) {
					paramList.add(MODEL);
					valueList.add(profileData.getModel());
				} else if (fiterName.equals(OS_FILTER)) {
					paramList.add(OS);
					valueList.add(profileData.getOs());
				}
			}
		}
		filterMap.put(FILTER_NAME, filterList);
		filterMap.put(FILTER_PARAM, paramList);
		filterMap.put(FILTER_VALUE, valueList);
		logger.info("FilterMap for getFilterAndValueMap : {}", filterMap != null ? new Gson().toJson(filterMap) : filterMap);
		return filterMap;
	}

	/**
	 * Gets the filter and value map for device info.
	 *
	 * @param imei the imei
	 * @param imsi the imsi
	 * @return the filter and value map for device info
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, List> getFilterAndValueMapForDeviceInfo(String imei, String imsi) {
		Map<String, List> filterMap = new HashMap<>();

		List<String> filterList = getFilterNameListForDeviceInfo(imei, imsi);
		List<String> paramList = new ArrayList<>();
		List<Object> valueList = new ArrayList<>();

		for (String fiterName : filterList) {
			if (fiterName.equalsIgnoreCase(DEVICE_INFO_IMEI_FILTER)) {
				paramList.add(IMEI_PARAM);
				valueList.add(imei);
			}
			if (fiterName.equalsIgnoreCase(DEVICE_INFO_IMSI_FILTER)) {
				paramList.add(IMSI_PARAM);
				valueList.add(imsi);
			}

		}
		filterMap.put(FILTER_NAME, filterList);
		filterMap.put(FILTER_PARAM, paramList);
		filterMap.put(FILTER_VALUE, valueList);
		logger.info("FilterMap for getFilterAndValueMapForDeviceInfo : {}",! filterMap.isEmpty() ? new Gson().toJson(filterMap) : filterMap);
		return filterMap;
	}

	/**
	 * Gets the filter name list.
	 *
	 * @param geographyLevel the geography level
	 * @param profileData            the profile data
	 * @return the filter name list
	 * @throws RestException the rest exception
	 */
	public static List<String> getFilterNameList(String geographyLevel, NVProfileData profileData,boolean isToAddDefaultOperatorFilter) {
		List<String> list = new ArrayList<>();
		if (profileData != null) {
			if (profileData.getMake() != null) {
				list.add(MAKE_FILTER);
			} else if (profileData.getModel() != null) {
				list.add(MODEL_FILTER);
			} else if (profileData.getOs() != null) {
				list.add(OS_FILTER);
			}
		}
		String geographyFilter = getGeographyFilter(geographyLevel);
		if (geographyFilter != null) {
			list.add(geographyFilter);
		}
		if(isToAddDefaultOperatorFilter) {
			list.add(DEFAULT_OPERATOR_FILTER);
		}
		return list;
	}

	/**
	 * Gets the geography filter.
	 *
	 * @param geographyLevel
	 *            the geography level
	 * @return the geography filter
	 * @throws RestException
	 *             the rest exception
	 */
	public static String getGeographyFilter(String geographyLevel) {
		if (geographyLevel.equalsIgnoreCase(GEOGRAPHYL1)) {
			return GEOGRAPHYL1_FILTER;
		} else if (geographyLevel.equalsIgnoreCase(GEOGRAPHYL2)) {
			return GEOGRAPHYL2_FILTER;
		} else if (geographyLevel.equalsIgnoreCase(GEOGRAPHYL3)) {
			return GEOGRAPHYL3_FILTER;
		} else if (geographyLevel.equalsIgnoreCase(GEOGRAPHYL4)) {
			return GEOGRAPHYL4_FILTER;
		} else {
			throw new RestException(INVALID_GEOGRAPHY_LEVEL);
		}
	}

	/**
	 * Gets the geography filter.
	 *
	 * @param geographyFilter
	 *            the geography filter
	 * @param profileData
	 *            the profile data
	 */
	public static Integer getGeographyId(String geographyFilter, NVProfileData profileData) {
		if (geographyFilter.equals(GEOGRAPHYL4_FILTER)) {
			return profileData	.getGeographyL4()
								.getId();
		} else if (geographyFilter.equals(GEOGRAPHYL3_FILTER)) {
			return profileData	.getGeographyL3()
								.getId();
		} else if (geographyFilter.equals(GEOGRAPHYL2_FILTER)) {
			return profileData	.getGeographyL2()
								.getId();
		} else {
			return profileData	.getGeographyL1()
								.getId();
		}
	}
	
	/**
	 * Gets the view port by center lat long.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param distance the distance
	 * @return the view port by center lat long
	 * @throws RestException the rest exception
	 */
	public static Double[][] getViewPortByCenterLatLong(Double latitude,Double longitude,Double distance) {
		logger.info("Going to get View Port By Center Lat : {} Long : {} dist : {}",latitude,longitude,distance);
		if (NumberUtils.isValidNumber(latitude) && NumberUtils.isValidNumber(longitude) && NumberUtils.isValidNumber(distance)) {
			double neLong = longitude - Math.toDegrees(distance / EARTH_RADIUS / Math.cos(180 - Math.toRadians(latitude)));
			double swLong = longitude + Math.toDegrees(distance / EARTH_RADIUS / Math.cos(180 - Math.toRadians(latitude)));
			double neLat = latitude + Math.toDegrees(distance / EARTH_RADIUS);
			double swLat = latitude - Math.toDegrees(distance / EARTH_RADIUS);
			logger.info("View Port neLat : {} neLng : {} swLat : {} swLng : {}",neLat,neLong,swLat,swLong);
			return new Double[][] {{neLat,neLong},{swLat,swLong}};
		} else {
			logger.error("Invalid View Port Center lat : {} long : {} distance : {}",latitude,longitude,distance);
			throw new RestException("Invalid View Port Details");
		}
	}

	
	/**
	 * Checks if is valid number.
	 *
	 * @param value the value
	 * @return true, if is valid number
	 */
	public static boolean isValidNumber(Double value) {
		return NumberUtils.isValidNumber(value) && !NumberUtils.DOUBLE_ZERO.equals(value);
	}

	public static List<NVDeviceDataWrapper> getGridWiseDevices(List<NVDeviceDataWrapper> nvDeviceDataWrapperList,
			Integer zoomLevel) {
		logger.info("Inside getGridWiseDevices zoomLevel  : {} ,nvDeviceDataWrapperList : {}",zoomLevel,nvDeviceDataWrapperList.size());
		if (zoomLevel.equals(ZOOM_LEVEL_16)) {
			return getGridWiseDeviceCount(nvDeviceDataWrapperList, GRID_LENGHT_FOR_ZOOM16);
		} else if (zoomLevel.equals(ZOOM_LEVEL_17)) {
			return getGridWiseDeviceCount(nvDeviceDataWrapperList, GRID_LENGHT_FOR_ZOOM17);
		} else if (zoomLevel.equals(ZOOM_LEVEL_18)) {
			return getGridWiseDeviceCount(nvDeviceDataWrapperList, GRID_LENGHT_FOR_ZOOM18);
		} else {
			return nvDeviceDataWrapperList;
		}
	}
	
	/** 
	 * @param nvDeviceDataWrapperList
	 * @param grid length 
	 * @return
	 */
	private static List<NVDeviceDataWrapper> getGridWiseDeviceCount(List<NVDeviceDataWrapper> nvDeviceDataWrapperList,
			int length) {
		logger.info("Going to get gridwise nvdevice count for grid lenght : {}  , nvDeviceListWrapper size : {}",length,nvDeviceDataWrapperList.size());
		if (nvDeviceDataWrapperList.isEmpty()) {
			return nvDeviceDataWrapperList;
		}
		Double refefrenceLatitude=Double.parseDouble(SystemConfigurationUtils.systemConfMap.get(REFERENCE_LAT_KEY));
		Double refefrenceLongitude=Double.parseDouble(SystemConfigurationUtils.systemConfMap.get(REFERENCE_LONG_KEY));
		nvDeviceDataWrapperList.stream().map(nvDevice->updateGrid(nvDevice,length,refefrenceLatitude,refefrenceLongitude)).collect(Collectors.toList());
		return applyGridWiseGrouping(nvDeviceDataWrapperList);
	}
    /** 
     * @param nvDeviceDataWrapperList
     * @return grouped count of devices
     */
	private static List<NVDeviceDataWrapper> applyGridWiseGrouping(List<NVDeviceDataWrapper> nvDeviceDataWrapperList) {
		logger.info("Applying grouping after updating grid lat & lng");
		List<NVDeviceDataWrapper>finalList=new ArrayList<>();
		Map<Double, Map<Double, List<NVDeviceDataWrapper>>>list = nvDeviceDataWrapperList.stream().collect(Collectors.groupingBy(NVDeviceDataWrapper::getLatitude,Collectors.groupingBy(NVDeviceDataWrapper::getLongitude)));
		list.forEach((latKey,value)->
			value.forEach((longKey,nvDeviceList)->
				finalList.add(new NVDeviceDataWrapper(Long.valueOf(nvDeviceList.size()),latKey,longKey,nvDeviceList))
			)
		);
		return finalList;
	}
   
	/**
	 * Update NVDevice wrapper's latitude & longitude by grid latitude & longitude.
	 * @param nvDeviceWrapper
	 * @param length
	 * @param refefrenceLongitude 
	 * @param refefrenceLatitude 
	 * @return 
	 */
	private static NVDeviceDataWrapper updateGrid(NVDeviceDataWrapper nvDeviceWrapper,Integer length, Double refefrenceLatitude, Double refefrenceLongitude) {
		DegreeGrid degreeGrid= new DegreeGrid(length, new LatLng(refefrenceLatitude,refefrenceLongitude));
		LatLng grid=degreeGrid.getGrid(new LatLng(nvDeviceWrapper.getLatitude(), nvDeviceWrapper.getLongitude()));
		nvDeviceWrapper.setLatitude(grid.getLatitude());
		nvDeviceWrapper.setLongitude(grid.getLongitude());
		return nvDeviceWrapper;
	}
	public static long getExpiryDateInMillis(Integer daytoExpiry) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, daytoExpiry);
		return cal.getTimeInMillis();
	}

	public static String getLiveLocationJson(NVDeviceDataWrapper nvDataWrapper) {
		return new Gson().toJson(new NVDeviceDataWrapper(nvDataWrapper.getLatitude(), nvDataWrapper.getLongitude(), nvDataWrapper.getLocationReason(), nvDataWrapper.getCellId(),nvDataWrapper.getMcc(),nvDataWrapper.getMnc()));
	}

	public static List<String> getMSISDNList(List<DeviceInfo> deviceInfoList) {
		return deviceInfoList.stream().map(DeviceInfo::getMsisdn).collect(Collectors.toList());
	}

	public static DeviceInfo getDeviceByMSISDN(DeviceInfo persistDevice, List<DeviceInfo> deviceInfoList) {
		Optional<DeviceInfo> optional = deviceInfoList.stream().filter(d->d.getMsisdn().equalsIgnoreCase(persistDevice.getMsisdn())).findFirst();
		if(optional.isPresent()) {
		return optional.get();
		}
		return null;
	}
	public static List<String> getDeviceIdList(List<DeviceInfo> deviceInfoList) {
		return deviceInfoList.stream().map(DeviceInfo::getDeviceId).collect(Collectors.toList());
	}
	public static DeviceInfo getDeviceByDeviceID(DeviceInfo persistDevice, List<DeviceInfo> deviceInfoList) {
		Optional<DeviceInfo> optional = deviceInfoList.stream().filter(d->d.getDeviceId().equalsIgnoreCase(persistDevice.getDeviceId())).findFirst();
		if(optional.isPresent()) {
		return optional.get();
		}
		return null;
	}
}
