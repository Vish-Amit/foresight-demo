package com.inn.foresight.module.nv.dashboard.passive.service.impl;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.dashboard.passive.dao.INVPassiveDashboardDao;
import com.inn.foresight.module.nv.dashboard.passive.model.NVPassiveDashboard;
import com.inn.foresight.module.nv.dashboard.passive.service.INVPassiveDashboardService;
import com.inn.foresight.module.nv.dashboard.passive.utils.NVPassiveConstants;
import com.inn.foresight.module.nv.dashboard.passive.wrapper.NVPassiveDeviceWrapper;

@Service("NVPassiveDashboardServiceImpl")
public class NVPassiveDashboardServiceImpl extends AbstractService<Integer, NVPassiveDashboard>
		implements INVPassiveDashboardService {

	@Autowired
	INVPassiveDashboardDao iNVPassiveDashboardDao;

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVPassiveDashboardServiceImpl.class);

	@Cacheable(value = "PassiveDashboardCache", condition="#result!= null")
	@Override
	public Map<String, Map<String, ?>> getPassiveDashboardData(String date, String level, Integer geographyId,
			String duplexType, String tagType ,String appName) {
		logger.info(
				"Inside the method GetPassiveDashboardData Service date: {} , level : {} ,geography Id : {} ,duplexType: {},"
						+ "tagType: {} , appName {}",
				date, level, geographyId, duplexType, tagType,appName);
		if (date != null && geographyId != null && level != null) {

			List<NVPassiveDashboard> passiveRecords = iNVPassiveDashboardDao.getCombinePassiveRecords(date, level,
					geographyId, duplexType, tagType,appName);
			List<NVPassiveDashboard> lastSevenDaysRecords = iNVPassiveDashboardDao.getLastSevenDaysData(date, level,
					geographyId, duplexType, tagType,appName);
			return computeCombineResultData(passiveRecords, lastSevenDaysRecords);
		}
		return null;
	}

	@Cacheable(value = "PassiveDashboardDeviceDataCache", condition="#result!= null")
	@Override
	public String getDeviceDistributionData(String date, String level, Integer geographyId, String duplexType,
			String tagType ,String appName) {
		logger.info(
				"Inside the method getDeviceDistributionData Service date: {} , level : {} ,geography Id : {} ,duplexType: {},"
						+ "tagType: {} , appName {}",
				date, level, geographyId, duplexType, tagType,appName);

		if (date != null && geographyId != null && level != null) {
			Map<String, Map<String, Long>> deviceDistribution = new HashMap<>();
			List<NVPassiveDashboard> passiveRecords = iNVPassiveDashboardDao.getCombinePassiveRecords(date, level,
					geographyId, duplexType, tagType,appName);
			for (NVPassiveDashboard passiveRecord : passiveRecords) {
				populateOsData(passiveRecord, deviceDistribution, NVPassiveConstants.DEVICE_TYPE);
			}

			return getTop5MakeAndModel(deviceDistribution);
		}
		return null;
	}

	@Override
	public Map<String, Map<String, ?>> computeCombineResultData(List<NVPassiveDashboard> combinePassiveRecords,
																List<NVPassiveDashboard> lastSevenDaysRecords) {
		Map<String, Map<String, ?>> response = new HashMap<>();

		Map<String, Map<String, Long>> gpsDataMap = new HashMap<>();
		Map<String, Long> coverageDataMap = new HashMap<>();
		Map<String, Long> indOutdoorDataMap = new HashMap<>();
		Map<String, Long> rsrpDataMap = new HashMap<>();
		Map<String, Long> sinrDataMap = new HashMap<>();
		Map<String, Long> rsrqDataMap = new HashMap<>();
		Map<String, Map<String, Long>> osDistribution = new HashMap<>();
		Map<String, Long> serviceDataMap = new HashMap<>();
		Map<String, Long> sampleContributerMap = new HashMap<>();
		Map<String, Long> technologyDistributorMap = new HashMap<>();
		Map<String, Long> simIdentificationMap = new HashMap<>();
		Map<String, Long> totalCallContributionMap = new HashMap<>();
		Map<String, Long> mifiTDDFDDMap = new HashMap<>();
		Map<String, Long> mifiRsrpDataMap = new HashMap<>();
		Map<String, Long> mifiSinrDataMap = new HashMap<>();
		Map<String, Long> mifiRsrqDataMap = new HashMap<>();

		if (combinePassiveRecords != null && lastSevenDaysRecords != null) {
			populateGPSCountData(lastSevenDaysRecords, gpsDataMap);
		}

		if (combinePassiveRecords != null) {
			for (NVPassiveDashboard passiveData : combinePassiveRecords) {

				populateCoverageDistribution(passiveData, coverageDataMap);
				populateIndOutdoorData(passiveData, indOutdoorDataMap);
				populateStatsData(passiveData, rsrpDataMap, NVPassiveConstants.RSRP_TYPE);
				populateStatsData(passiveData, sinrDataMap, NVPassiveConstants.SINR_TYPE);
				populateStatsData(passiveData, rsrqDataMap, NVPassiveConstants.RSRQ_TYPE);
				populateOsData(passiveData, osDistribution, NVPassiveConstants.OS_TYPE);
				populateServiceData(passiveData, serviceDataMap);
				populateSampleContributorData(passiveData, sampleContributerMap);
				populateTechnologyDistributorData(passiveData, technologyDistributorMap);
				populateSimIdentificationData(passiveData, simIdentificationMap);
				populateTotalCallContribution(passiveData, totalCallContributionMap);
				populateTDDFDDContribution(passiveData, mifiTDDFDDMap);
				populateStatsData(passiveData, mifiRsrpDataMap, NVPassiveConstants.MIFI_RSRP_TYPE);
				populateStatsData(passiveData, mifiSinrDataMap, NVPassiveConstants.MIFI_SINR_TYPE);
				populateStatsData(passiveData, mifiRsrqDataMap, NVPassiveConstants.MIFI_RSRQ_TYPE);

			}
		}
		return prepareFinalDataResponse(response, gpsDataMap, coverageDataMap, indOutdoorDataMap, rsrpDataMap,
				sinrDataMap, rsrqDataMap, osDistribution, serviceDataMap, sampleContributerMap,
				technologyDistributorMap, simIdentificationMap, totalCallContributionMap, mifiTDDFDDMap,
				mifiRsrpDataMap, mifiSinrDataMap, mifiRsrqDataMap);
	}

	private Map<String, Map<String, ?>> prepareFinalDataResponse(Map<String, Map<String, ?>> response,
			Map<String, Map<String, Long>> gpsDataMap, Map<String, Long> coverageDataMap,
			Map<String, Long> indOutdoorDataMap, Map<String, Long> rsrpDataMap, Map<String, Long> sinrDataMap,
			Map<String, Long> rsrqDataMap, Map<String, Map<String, Long>> osDistribution,
			Map<String, Long> serviceDataMap, Map<String, Long> sampleContributerMap,
			Map<String, Long> technologyDistributorMap, Map<String, Long> simIdentificationMap,
			Map<String, Long> totalCallContributionMap, Map<String, Long> mifiTDDFDDMap,
			Map<String, Long> mifiRsrpDataMap, Map<String, Long> mifiSinrDataMap, Map<String, Long> mifiRsrqDataMap) {

		logger.info("Going to get the final Response For ALL chard");
		if (response != null) {
			response.put(NVPassiveConstants.GPS_CHART, gpsDataMap);
			response.put(NVPassiveConstants.COVERAGE_CHART, coverageDataMap);
			response.put(NVPassiveConstants.INDOOR_OUTDOOR_CHART, indOutdoorDataMap);
			response.put(NVPassiveConstants.RSRP_CHART, rsrpDataMap);
			response.put(NVPassiveConstants.SINR_CHART, sinrDataMap);
			response.put(NVPassiveConstants.RSRQ_CHART, rsrqDataMap);
			response.put(NVPassiveConstants.OS_CHART, osDistribution);
			response.put(NVPassiveConstants.SERVICE_DISTRIBUTION_CHART, serviceDataMap);
			response.put(NVPassiveConstants.SAMPLE_CONTRIBUTOR_CHART, sampleContributerMap);
			response.put(NVPassiveConstants.TECHNOLOGY_CHART, technologyDistributorMap);
			response.put(NVPassiveConstants.SIM_IDENTIFICATION_CHART, simIdentificationMap);
			response.put(NVPassiveConstants.TOTAL_CALL_CONTRIBUTION_CHART, totalCallContributionMap);
			response.put(NVPassiveConstants.TDD_FDD_CHART, mifiTDDFDDMap);
			response.put(NVPassiveConstants.MIFI_RSRP_CHART, mifiRsrpDataMap);
			response.put(NVPassiveConstants.MIFI_SINR_CHART, mifiSinrDataMap);
			response.put(NVPassiveConstants.MIFI_RSRQ_CHART, mifiRsrqDataMap);
		}
		logger.info("Got the final Response :::===> {}", response != null ? new Gson().toJson(response): response);
		return response;
	}

	@Override
	public String getTop5MakeAndModel(Map<String, Map<String, Long>> deviceDistribution2) {
		if (deviceDistribution2 != null) {
			Map<String, Long> globalCountMap = new HashMap<>();
			Map<String, Map<String, Long>> tempMap = new HashMap<>();
			Map<String, Map<String, Long>> top5MakeModelMap = new HashMap<>();
			for (Entry<String, Map<String, Long>> makeMap : deviceDistribution2.entrySet()) {
				Map<String, Long> top5Model = getTop5Records(makeMap.getValue(), false, makeMap.getKey(),
						globalCountMap);
				tempMap.put(makeMap.getKey(), top5Model);
			}
			Map<String, Long> top5Make = getTop5Records(globalCountMap, false, null, globalCountMap);
			Set<String> keySet = top5Make.keySet();
			for (String key : keySet) {
				top5MakeModelMap.put(key, tempMap.get(key));
			}
			List<NVPassiveDeviceWrapper> parentChildList = new ArrayList<>();
			for (Entry<String, Long> parent : top5Make.entrySet()) {
				NVPassiveDeviceWrapper parentWrapper = new NVPassiveDeviceWrapper();
				parentWrapper.setId(parent.getKey());
				parentWrapper.setName(parent.getKey());
				parentWrapper.setValue(parent.getValue());
				parentChildList.add(parentWrapper);
				if (top5MakeModelMap.containsKey(parent.getKey())) {
					Map<String, Long> map = top5MakeModelMap.get(parent.getKey());
					for (Entry<String, Long> children : map.entrySet()) {
						NVPassiveDeviceWrapper childWrapper = new NVPassiveDeviceWrapper();
						childWrapper.setId(children.getKey());
						childWrapper.setName(children.getKey());
						childWrapper.setParent(parent.getKey());
						childWrapper.setValue(children.getValue());
						parentChildList.add(childWrapper);
					}
				}
			}
			logger.info("Response of Top 5 Make and Model===> {}", top5MakeModelMap.toString());
			logger.info("Final Device Distribution Response =====> {}", new Gson().toJson(parentChildList));
			return new Gson().toJson(parentChildList);
		}
		return null;
	}

	private static Map<String, Long> getTop5Records(Map<String, Long> unsortMap, final boolean order, String globalKey,
			Map<String, Long> globalCountMap) {
		List<Entry<String, Long>> list = new LinkedList<>(unsortMap.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Long>>() {
			public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());
				}
			}
		});
		Long totalCount = NumberUtils.LONG_ZERO;
		Integer counter = ForesightConstants.ZERO;
		Map<String, Long> sortedMap = new LinkedHashMap<>();
		for (Entry<String, Long> entry : list) {
			if (counter < ForesightConstants.FIVE) {
				sortedMap.put(entry.getKey(), entry.getValue());
				totalCount = totalCount + entry.getValue();
			} else {
				totalCount = totalCount + entry.getValue();
			}
			counter++;
		}
		if (globalKey != null) {
			globalCountMap.put(globalKey, totalCount);
		}
		return sortedMap;
	}

	private void populateTDDFDDContribution(NVPassiveDashboard passiveData, Map<String, Long> mifiTDDFDDMap) {
		if (mifiTDDFDDMap.size() > ForesightConstants.ZERO) {
			if (passiveData.getMifiTDD() != null) {
				Long oldValue = mifiTDDFDDMap.get(NVPassiveConstants.TDD_COUNT);
				mifiTDDFDDMap.put(NVPassiveConstants.TDD_COUNT,
						oldValue + Long.parseLong(passiveData.getMifiTDD().toString()));
			}
			if (passiveData.getMifiFDD() != null) {
				Long oldValue = mifiTDDFDDMap.get(NVPassiveConstants.FDD_COUNT);
				mifiTDDFDDMap.put(NVPassiveConstants.FDD_COUNT,
						oldValue + Long.parseLong(passiveData.getMifiFDD().toString()));
			}

		} else {
			if (passiveData.getMifiTDD() != null)
				mifiTDDFDDMap.put(NVPassiveConstants.TDD_COUNT, Long.parseLong(passiveData.getMifiTDD().toString()));
			if (passiveData.getMifiFDD() != null)
				mifiTDDFDDMap.put(NVPassiveConstants.FDD_COUNT, Long.parseLong(passiveData.getMifiFDD().toString()));
		}

	}

	private void populateTotalCallContribution(NVPassiveDashboard passiveData,
			Map<String, Long> totalCallContributionMap) {
		if (totalCallContributionMap.size() > ForesightConstants.ZERO) {
			if (passiveData.getInComingCount() != null) {
				Long oldValue = totalCallContributionMap.get(NVPassiveConstants.INCOMING_COUNT);
				totalCallContributionMap.put(NVPassiveConstants.INCOMING_COUNT,
						oldValue + Long.parseLong(passiveData.getInComingCount().toString()));
			}
			if (passiveData.getOutGoingCount() != null) {
				Long oldValue = totalCallContributionMap.get(NVPassiveConstants.OUTGOING_COUNT);
				totalCallContributionMap.put(NVPassiveConstants.OUTGOING_COUNT,
						oldValue + Long.parseLong(passiveData.getOutGoingCount().toString()));
			}

		} else {
			if (passiveData.getInComingCount() != null)
				totalCallContributionMap.put(NVPassiveConstants.INCOMING_COUNT,
						Long.parseLong(passiveData.getInComingCount().toString()));
			if (passiveData.getOutGoingCount() != null)
				totalCallContributionMap.put(NVPassiveConstants.OUTGOING_COUNT,
						Long.parseLong(passiveData.getOutGoingCount().toString()));
		}
	}

	private void populateSimIdentificationData(NVPassiveDashboard passiveData, Map<String, Long> simIdentificationMap) {
		if (simIdentificationMap.size() > ForesightConstants.ZERO) {
			if (passiveData.getSlot1Count() != null) {
				Long oldValue = simIdentificationMap.get(NVPassiveConstants.SLOT1_COUNT);
				simIdentificationMap.put(NVPassiveConstants.SLOT1_COUNT,
						oldValue + Long.parseLong(passiveData.getSlot1Count().toString()));
			}
			if (passiveData.getSlot2Count() != null) {
				Long oldValue = simIdentificationMap.get(NVPassiveConstants.SLOT2_COUNT);
				simIdentificationMap.put(NVPassiveConstants.SLOT2_COUNT,
						oldValue + Long.parseLong(passiveData.getSlot2Count().toString()));
			}

		} else {
			if (passiveData.getSlot1Count() != null)
				simIdentificationMap.put(NVPassiveConstants.SLOT1_COUNT,
						Long.parseLong(passiveData.getSlot1Count().toString()));
			if (passiveData.getSlot2Count() != null)
				simIdentificationMap.put(NVPassiveConstants.SLOT2_COUNT,
						Long.parseLong(passiveData.getSlot2Count().toString()));
		}

	}

	private void populateTechnologyDistributorData(NVPassiveDashboard passiveData,
			Map<String, Long> technologyDistributorMap) {
		if (technologyDistributorMap.size() > ForesightConstants.ZERO) {
			if (passiveData.getLteCount() != null) {
				Long oldValue = technologyDistributorMap.get(NVPassiveConstants.LTE_COUNT);
				technologyDistributorMap.put(NVPassiveConstants.LTE_COUNT,
						oldValue + Long.parseLong(passiveData.getLteCount().toString()));
			}
			if (passiveData.getWificount() != null) {
				Long oldValue = technologyDistributorMap.get(NVPassiveConstants.WIFI_COUNT);
				technologyDistributorMap.put(NVPassiveConstants.WIFI_COUNT,
						oldValue + Long.parseLong(passiveData.getWificount().toString()));
			}

		} else {
			if (passiveData.getLteCount() != null)
				technologyDistributorMap.put(NVPassiveConstants.LTE_COUNT,
						Long.parseLong(passiveData.getLteCount().toString()));
			if (passiveData.getWificount() != null)
				technologyDistributorMap.put(NVPassiveConstants.WIFI_COUNT,
						Long.parseLong(passiveData.getWificount().toString()));
		}
	}

	private void populateSampleContributorData(NVPassiveDashboard passiveData, Map<String, Long> sampleContributerMap) {
		if (Utils.hasValidValue(passiveData.getSampleCount())) {
			Type type = new TypeToken<Map<String, Long>>() {
			}.getType();
			String sampleCount = passiveData.getSampleCount();
			Map<String, Long> appWiseCount = new Gson().fromJson(sampleCount, type);
			if (sampleContributerMap.isEmpty()) {
				sampleContributerMap.putAll(appWiseCount);
			} else {
				for (Entry<String, Long> entry : appWiseCount.entrySet()) {
					if (sampleContributerMap.containsKey(entry.getKey())) {
						Long count = entry.getValue() + sampleContributerMap.get(entry.getKey());
						sampleContributerMap.put(entry.getKey(), count);
					} else {
						sampleContributerMap.put(entry.getKey(), entry.getValue());
					}
				}
			}
		}
	}

	private void populateServiceData(NVPassiveDashboard passiveData, Map<String, Long> serviceDataMap) {
		if (serviceDataMap.size() > ForesightConstants.ZERO) {
			if (passiveData.getDataCount() != null) {
				Long oldValue = serviceDataMap.get(NVPassiveConstants.DATA_COUNT);
				serviceDataMap.put(NVPassiveConstants.DATA_COUNT,
						oldValue + Long.parseLong(passiveData.getDataCount().toString()));
			}
			if (passiveData.getDataVoiceCount() != null) {
				Long oldValue = serviceDataMap.get(NVPassiveConstants.DATA_VOICE_COUNT);
				serviceDataMap.put(NVPassiveConstants.DATA_VOICE_COUNT,
						oldValue + Long.parseLong(passiveData.getDataVoiceCount().toString()));
			}
			if (passiveData.getRoamingCount() != null) {
				Long oldValue = serviceDataMap.get(NVPassiveConstants.ROAMING_COUNT);
				serviceDataMap.put(NVPassiveConstants.ROAMING_COUNT,
						oldValue + Long.parseLong(passiveData.getRoamingCount().toString()));
			}
		} else {
			if (passiveData.getDataCount() != null)
				serviceDataMap.put(NVPassiveConstants.DATA_COUNT,
						Long.parseLong(passiveData.getDataCount().toString()));
			if (passiveData.getDataVoiceCount() != null)
				serviceDataMap.put(NVPassiveConstants.DATA_VOICE_COUNT,
						Long.parseLong(passiveData.getDataVoiceCount().toString()));
			if (passiveData.getRoamingCount() != null)
				serviceDataMap.put(NVPassiveConstants.ROAMING_COUNT,
						Long.parseLong(passiveData.getRoamingCount().toString()));
		}
	}

	private void populateOsData(NVPassiveDashboard passiveData, Map<String, Map<String, Long>> genericMap,
			String osType) {
		Type type = new TypeToken<Map<String, Map<String, Long>>>() {
		}.getType();
		Map<String, Map<String, Long>> rawMap = new HashMap<>();
		if (osType.equalsIgnoreCase(NVPassiveConstants.OS_TYPE) && passiveData.getOsStats() != null) {
			rawMap = new Gson().fromJson(passiveData.getOsStats(), type);
		} else if (osType.equalsIgnoreCase(NVPassiveConstants.DEVICE_TYPE)
				&& passiveData.getDeviceDistribution() != null) {
			rawMap = new Gson().fromJson(passiveData.getDeviceDistribution(), type);
		}
		if (genericMap.size() > ForesightConstants.ZERO) {
			for (Entry<String, Map<String, Long>> outerRawMap : rawMap.entrySet()) {
				populateGenericMap(genericMap, outerRawMap);
			}
		} else {
			genericMap.putAll(rawMap);
		}
	}

	private void populateGenericMap(Map<String, Map<String, Long>> genericMap,
			Entry<String, Map<String, Long>> outerRawMap) {
		String outerRawKey = outerRawMap.getKey();
		if (genericMap.containsKey(outerRawKey)) {
			Map<String, Long> genericInnerMap = genericMap.get(outerRawKey);
			for (Entry<String, Long> innerRawMap : outerRawMap.getValue().entrySet()) {
				if (genericInnerMap.containsKey(innerRawMap.getKey())) {
					Long newValue = innerRawMap.getValue();
					Long oldValue = genericInnerMap.get(innerRawMap.getKey());
					genericInnerMap.put(innerRawMap.getKey(), newValue + oldValue);
				} else {
					genericInnerMap.put(innerRawMap.getKey(), innerRawMap.getValue());
				}
			}
			genericMap.put(outerRawKey, genericInnerMap);
		} else {
			genericMap.put(outerRawKey, outerRawMap.getValue());
		}
	}

	private void populateStatsData(NVPassiveDashboard passiveData, Map<String, Long> genericMap, String mapType) {
		Type type = new TypeToken<Map<String, Long>>() {
		}.getType();
		Map<String, Long> rangeMap = new HashMap<>();
		rangeMap = getMapTypeWiseConversion(passiveData, mapType, type, rangeMap);
		if (genericMap.size() > 0) {
			for (Entry<String, Long> singleRange : rangeMap.entrySet()) {
				if (genericMap.containsKey(singleRange.getKey())) {
					Long newValue = singleRange.getValue();
					Long oldValue = genericMap.get(singleRange.getKey());
					genericMap.put(singleRange.getKey(), newValue + oldValue);
				}
			}
		} else {
			genericMap.putAll(rangeMap);
		}
	}

	private Map<String, Long> getMapTypeWiseConversion(NVPassiveDashboard passiveData, String mapType, Type type,
			Map<String, Long> rangeMap) {
		if (mapType.equalsIgnoreCase(NVPassiveConstants.RSRP_TYPE) && passiveData.getRsrpStats() != null) {
			rangeMap = new Gson().fromJson(passiveData.getRsrpStats(), type);
		} else if (mapType.equalsIgnoreCase(NVPassiveConstants.SINR_TYPE) && passiveData.getSinrStats() != null) {
			rangeMap = new Gson().fromJson(passiveData.getSinrStats(), type);
		} else if (mapType.equalsIgnoreCase(NVPassiveConstants.RSRQ_TYPE) && passiveData.getRsrqStats() != null) {
			rangeMap = new Gson().fromJson(passiveData.getRsrqStats(), type);
		} else if (mapType.equalsIgnoreCase(NVPassiveConstants.MIFI_RSRP_TYPE) && passiveData.getMifiRsrp() != null) {
			rangeMap = new Gson().fromJson(passiveData.getMifiRsrp(), type);
		} else if (mapType.equalsIgnoreCase(NVPassiveConstants.MIFI_SINR_TYPE) && passiveData.getMifiSinr() != null) {
			rangeMap = new Gson().fromJson(passiveData.getMifiSinr(), type);
		} else if (mapType.equalsIgnoreCase(NVPassiveConstants.MIFI_RSRQ_TYPE) && passiveData.getMifiRsrq() != null) {
			rangeMap = new Gson().fromJson(passiveData.getMifiRsrq(), type);
		}
		return rangeMap;
	}

	private void populateIndOutdoorData(NVPassiveDashboard passiveData, Map<String, Long> indOutdoorDataMap) {
		if (indOutdoorDataMap.size() > ForesightConstants.ZERO) {
			if (passiveData.getIndoorCount() != null) {
				Long oldValue = indOutdoorDataMap.get(NVPassiveConstants.INDOOR_COUNT);
				indOutdoorDataMap.put(NVPassiveConstants.INDOOR_COUNT,
						oldValue + Long.parseLong(passiveData.getIndoorCount().toString()));
			}
			if (passiveData.getOutdoorCount() != null) {
				Long oldValue = indOutdoorDataMap.get(NVPassiveConstants.OUTDOOR_COUNT);
				indOutdoorDataMap.put(NVPassiveConstants.OUTDOOR_COUNT,
						oldValue + Long.parseLong(passiveData.getOutdoorCount().toString()));
			}

		} else {
			if (passiveData.getIndoorCount() != null)
				indOutdoorDataMap.put(NVPassiveConstants.INDOOR_COUNT,
						Long.parseLong(passiveData.getIndoorCount().toString()));
			if (passiveData.getOutdoorCount() != null)
				indOutdoorDataMap.put(NVPassiveConstants.OUTDOOR_COUNT,
						Long.parseLong(passiveData.getOutdoorCount().toString()));
		}

	}

	private void populateCoverageDistribution(NVPassiveDashboard passiveData, Map<String, Long> coverageDataMap) {
		if (coverageDataMap.size() > ForesightConstants.ZERO) {
			if (passiveData.getCoverageCount() != null) {
				Long oldValue = coverageDataMap.get(NVPassiveConstants.COVERAGE_COUNT);
				coverageDataMap.put(NVPassiveConstants.COVERAGE_COUNT,
						oldValue + Long.parseLong(passiveData.getCoverageCount().toString()));
			}
			if (passiveData.getNonCoverageCount() != null) {
				Long oldValue = coverageDataMap.get(NVPassiveConstants.NO_COVERAGE_COUNT);
				coverageDataMap.put(NVPassiveConstants.NO_COVERAGE_COUNT,
						oldValue + Long.parseLong(passiveData.getNonCoverageCount().toString()));
			}

		} else {
			if (passiveData.getCoverageCount() != null)
				coverageDataMap.put(NVPassiveConstants.COVERAGE_COUNT,
						Long.parseLong(passiveData.getCoverageCount().toString()));
			if (passiveData.getNonCoverageCount() != null)
				coverageDataMap.put(NVPassiveConstants.NO_COVERAGE_COUNT,
						Long.parseLong(passiveData.getNonCoverageCount().toString()));
		}
	}

	private void populateGPSCountData(List<NVPassiveDashboard> lastSevenDaysRecords,
			Map<String, Map<String, Long>> gpsDataMap) {
		logger.info("Going to get Data for GPS Non GPS and Unique User");
		SimpleDateFormat formatter = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_DDMMYY);
		Map<String, Long> uniqueUC = new HashMap<>();
		Map<String, Long> gpsUniqueUC = new HashMap<>();
		Map<String, Long> gpsCount = new HashMap<>();
		Map<String, Long> nonGpsCount = new HashMap<>();
		for (NVPassiveDashboard passiveData : lastSevenDaysRecords) {
			prefixWiseCount(formatter, uniqueUC, gpsCount, nonGpsCount, passiveData,gpsUniqueUC);
		}
		gpsDataMap.put(NVPassiveConstants.UNIQUE_USER, uniqueUC);
		gpsDataMap.put(NVPassiveConstants.GPS_UNIQUE_USER,gpsUniqueUC);
		gpsDataMap.put(NVPassiveConstants.GPS_COUNT, gpsCount);
		gpsDataMap.put(NVPassiveConstants.NON_GPS_COUNT, nonGpsCount);
	}

	private void prefixWiseCount(SimpleDateFormat formatter, Map<String, Long> uniqueUC, Map<String, Long> gpsCount,
			Map<String, Long> nonGpsCount, NVPassiveDashboard passiveData, Map<String, Long> gpsUniqueUC) {
		String datePrefix = formatter.format(passiveData.getDate());

		if (passiveData.getUniqueUC() != null) {
			if (uniqueUC.containsKey(datePrefix)) {
				uniqueUC.put(datePrefix, uniqueUC.get(datePrefix) + passiveData.getUniqueUC());
			} else {
				uniqueUC.put(datePrefix, 0L + passiveData.getUniqueUC());
			}
		}
		if(passiveData.getGpsUniqueUC() != null) {
			if(gpsUniqueUC.get(datePrefix)!= null) {
				gpsUniqueUC.put(datePrefix, gpsUniqueUC.get(datePrefix) + passiveData.getGpsUniqueUC());
			}else {
				gpsUniqueUC.put(datePrefix,0L + passiveData.getGpsUniqueUC());
			}
		}
		
		getGPSNonGPSCount(gpsCount, nonGpsCount, passiveData, datePrefix);
	}

	private void getGPSNonGPSCount(Map<String, Long> gpsCount, Map<String, Long> nonGpsCount,
			NVPassiveDashboard passiveData, String datePrefix) {
		if (passiveData.getGpsCount() != null) {
			if (gpsCount.containsKey(datePrefix)) {
				gpsCount.put(datePrefix, gpsCount.get(datePrefix) + passiveData.getGpsCount());
			} else {
				gpsCount.put(datePrefix, 0L + passiveData.getGpsCount());
			}
		}
		if (passiveData.getNonGpsCount() != null) {
			if (nonGpsCount.containsKey(datePrefix)) {
				nonGpsCount.put(datePrefix, nonGpsCount.get(datePrefix) + passiveData.getNonGpsCount());
			} else {
				nonGpsCount.put(datePrefix, 0L + passiveData.getNonGpsCount());
			}
		}
	}

}
