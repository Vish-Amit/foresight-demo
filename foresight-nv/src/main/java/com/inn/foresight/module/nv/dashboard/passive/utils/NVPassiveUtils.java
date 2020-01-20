package com.inn.foresight.module.nv.dashboard.passive.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NVPassiveUtils {

	public enum GEOGRAPHY {
		L1, L2, L3, L4
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, List> getFilterMetaDataForGeography(String geographyLevel, Integer geography) {
		if (geographyLevel != null || geography != null) {
			String filter = getFilterNameForGeography(geographyLevel, geography);
			if (filter != null) {
				Map<String, List> map = new HashMap<>();
				map.put(NVPassiveConstants.FILTER_NAME, Arrays.asList(filter));
				if (geographyLevel != null && !geographyLevel.equalsIgnoreCase(NVPassiveConstants.ALL)) {
					map.put(NVPassiveConstants.FILTER_PARAM,
							Arrays.asList(Arrays.asList(NVPassiveConstants.GEOGRAPHY_ID)));
					map.put(NVPassiveConstants.FILTER_VALUE, Arrays.asList(Arrays.asList(geography)));
				}
				return map;
			}
		}
		return null;
	}

	private static String getFilterNameForGeography(String geographyLevel, Integer geography) {
		String filterName = NVPassiveConstants.GEOGRAPHY_ALL_FILTER;
		if (geographyLevel != null && geography != null) {
			if (geographyLevel.equalsIgnoreCase(GEOGRAPHY.L4.name())) {
				filterName = NVPassiveConstants.GEOGRAPHYL4_FILTER;
			} else if (geographyLevel.equalsIgnoreCase(GEOGRAPHY.L3.name())) {
				filterName = NVPassiveConstants.GEOGRAPHYL3_FILTER;
			} else if (geographyLevel.equalsIgnoreCase(GEOGRAPHY.L2.name())) {
				filterName = NVPassiveConstants.GEOGRAPHYL2_FILTER;
			} else if (geographyLevel.equalsIgnoreCase(GEOGRAPHY.L1.name())) {
				filterName = NVPassiveConstants.GEOGRAPHYL1_FILTER;
			} else if (geographyLevel.equalsIgnoreCase(NVPassiveConstants.ALL)) {
				filterName = NVPassiveConstants.GEOGRAPHY_ALL_FILTER;
			}
		}
		return filterName;
	}

	public static Map<String, Integer> aggregateResult(List<String> results) {
		Map<String, Integer> aggResult = new HashMap<>();
		for (String result : results) {
			if (result != null) {
				Map<String, Integer> map = new Gson().fromJson(result, new TypeToken<Map<String, Integer>>() {
				}.getType());
				map.forEach((key, value) -> aggResult.put(key,
						aggResult.containsKey(key) ? aggResult.get(key) + value : value));
			}
		}
		return aggResult;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, List> getBasicFilterMetaDataForPassiveDashboard(String recordtime, String recordtype,
			String source, Integer geography, String geographyLevel) {
		Map<String, List> filterMetaData = new HashMap<>();
		if (recordtime != null && recordtype != null && source != null && geography != null && geographyLevel != null) {
			String filterName = getFilterNameForNVPassiveDeviceCounts(geographyLevel);
			filterMetaData.put(NVPassiveConstants.FILTER_NAME, Arrays.asList(filterName));

			if (geographyLevel.equalsIgnoreCase(NVPassiveConstants.ALL)) {
				filterMetaData.put(NVPassiveConstants.FILTER_PARAM,
						Arrays.asList(Arrays.asList(NVPassiveConstants.RECORD_TIME, NVPassiveConstants.RECORD_TYPE,
								NVPassiveConstants.SOURCE)));
				filterMetaData.put(NVPassiveConstants.FILTER_VALUE,
						Arrays.asList(Arrays.asList(recordtime, recordtype, source)));
			} else {
				filterMetaData.put(NVPassiveConstants.FILTER_PARAM,
						Arrays.asList(Arrays.asList(NVPassiveConstants.RECORD_TIME, NVPassiveConstants.RECORD_TYPE,
								NVPassiveConstants.GEOGRAPHY_ID, NVPassiveConstants.SOURCE)));
				filterMetaData.put(NVPassiveConstants.FILTER_VALUE,
						Arrays.asList(Arrays.asList(recordtime, recordtype, geography, source)));
			}
		}
		return filterMetaData;
	}

	private static String getFilterNameForNVPassiveDeviceCounts(String geographyLevel) {
		String filterName = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_ALL;
		if (geographyLevel.equalsIgnoreCase(NVPassiveUtils.GEOGRAPHY.L4.name())) {
			filterName = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_L4;
		} else if (geographyLevel.equalsIgnoreCase(NVPassiveUtils.GEOGRAPHY.L3.name())) {
			filterName = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_L3;
		} else if (geographyLevel.equalsIgnoreCase(NVPassiveUtils.GEOGRAPHY.L2.name())) {
			filterName = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_L2;
		} else if (geographyLevel.equalsIgnoreCase(NVPassiveUtils.GEOGRAPHY.L1.name())) {
			filterName = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_L1;
		} else if (geographyLevel.equalsIgnoreCase(NVPassiveConstants.ALL)) {
			filterName = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_ALL;
		}
		return filterName;
	}

}
