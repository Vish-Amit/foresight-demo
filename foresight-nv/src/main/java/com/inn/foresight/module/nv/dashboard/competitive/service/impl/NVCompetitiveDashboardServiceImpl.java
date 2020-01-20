package com.inn.foresight.module.nv.dashboard.competitive.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.lang.MapUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.dashboard.competitive.constant.NVCompetitiveConstant;
import com.inn.foresight.module.nv.dashboard.competitive.dao.INVCompetitiveDashboardDao;
import com.inn.foresight.module.nv.dashboard.competitive.dao.INVCompetitiveUserDao;
import com.inn.foresight.module.nv.dashboard.competitive.model.NVCompetitiveDashboard;
import com.inn.foresight.module.nv.dashboard.competitive.model.NVCompetitiveUser;
import com.inn.foresight.module.nv.dashboard.competitive.service.INVCompetitiveDashboardService;
import com.inn.foresight.module.nv.dashboard.passive.service.INVPassiveDashboardService;
import com.inn.foresight.module.nv.nps.dao.INetPromoterRawDao;
import com.inn.product.um.geography.dao.GeographyL4Dao;

/**
 * The Class NVCompetitiveDashboardServiceImpl.
 *
 * @author innoeye
 */
@Service("NVCompetitiveDashboardServiceImpl")
public class NVCompetitiveDashboardServiceImpl implements INVCompetitiveDashboardService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVCompetitiveDashboardServiceImpl.class);

	/** Create Singleton INVCompetitiveUserDao. */
	@Autowired
	INVCompetitiveUserDao iNVCompetitiveUserDao;

	/** Create Singleton INVCompetitiveDashboardDao. */
	@Autowired
	INVCompetitiveDashboardDao iNVCompetitiveDashboardDao;

	/** The i net promoter dao. */
	@Autowired
	INetPromoterRawDao iNPSRawDao;

	/** The i geography L 4 dao. */
	@Autowired
	GeographyL4Dao iGeographyL4Dao;
	
	/** The i NV passive dashboard service. */
	@Autowired
	INVPassiveDashboardService iNVPassiveDashboardService;
	
	/**
	 * Get Competitive User count by date operator and geographies.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operator the operator
	 * @return the competitive user
	 */
	@Cacheable(value="CompetitiveUserCache", condition="#result != null")
	@Override
	public Map<String,Object> getCompetitiveUser(String date, String geographyL1, String geographyL2,
			String geographyL3, String geographyL4, List<String> operator,String appName) {
		logger.info("Inside the method getCompetitiveUser Service date: {} ,operator: {},application Name {}", date, operator,appName);
		List<NVCompetitiveUser> competitiveUsers = iNVCompetitiveUserDao.getCompetitiveUser(date, geographyL1,
					geographyL2, geographyL3, geographyL4, operator,appName);
		return populateUserDistributionData(competitiveUsers);
	}

	/**
	 * Generate map as <operator, <os, count>>.
	 *
	 * @param competitiveUsers the competitive users
	 * @return the map
	 */
	@Override
	public Map<String,Object> populateUserDistributionData(List<NVCompetitiveUser> competitiveUsers) {
		if (competitiveUsers != null && !competitiveUsers.isEmpty()) {
			Map<String, Object> response = new HashMap<>();
			
			Map<String, Map<String, Long>> userCount = competitiveUsers.stream()
					.filter(f -> f.getOperator() != null && f.getOs() != null)
					.collect(Collectors.groupingBy(NVCompetitiveUser::getOperator, Collectors.groupingBy(
							NVCompetitiveUser::getOs, Collectors.summingLong(NVCompetitiveUser::getUserCount))));
		
			Map<String, Long> dataCount = competitiveUsers.stream()
					.filter(f -> f.getOperator() != null && f.getDataCount() != null).collect(Collectors.groupingBy(
							NVCompetitiveUser::getOperator, Collectors.summingLong(NVCompetitiveUser::getDataCount)));
			
			Map<String, Long> dataVoiceCount = competitiveUsers.stream()
					.filter(f -> f.getOperator() != null && f.getDataVoiceCount() != null)
					.collect(Collectors.groupingBy(NVCompetitiveUser::getOperator,
							Collectors.summingLong(NVCompetitiveUser::getDataVoiceCount)));
			
			response.put(NVCompetitiveConstant.USER, userCount);
			response.put(NVCompetitiveConstant.DATA_COUNT, dataCount);
			response.put(NVCompetitiveConstant.DATA_VOICE_COUNT, dataVoiceCount);

			if (MapUtils.isNotEmpty(response)) {
				return response;
			} else {
				return null;
			}
		}
		return null;
	}

	/**
	 * Get Competitive Sample count by date operator and geographies.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operator the operator
	 * @return the sample count
	 */
	@Cacheable(value = "SampleCountCache", condition = "#result != null")
	@Override
	public Map<String,Map<String,Long>> getSampleCount(String date, String geographyL1, String geographyL2,
			String geographyL3, String geographyL4, List<String> operator,String appName) {
		logger.info("Inside the method getSampleCount Service date: {} ,operator: {},application Name {}", date, operator,appName);
		List<NVCompetitiveDashboard> competitiveSamples = null;
		if (date != null && operator != null) {
			competitiveSamples = iNVCompetitiveDashboardDao.getCompetitiveSamples(date,
					geographyL1, geographyL2, geographyL3, geographyL4, operator,null,appName);
		}
		return populateSampleCountDistribution(competitiveSamples);
	}

	/**
	 * multiple map for 1.coverageCount, 2.NoCovergeCount, 3.WifiCount, 4.AirplaneModeCount
	 *
	 * @param competitiveSamples the competitive samples
	 * @return the list
	 */
	@Override
	public  Map<String,Map<String,Long>> populateSampleCountDistribution(List<NVCompetitiveDashboard> competitiveSamples) {
		Map<String,Map<String,Long>> responceMap =  new HashMap<>();
		if (competitiveSamples != null && !competitiveSamples.isEmpty()) {

			Map<String, Long> coverageMap = competitiveSamples	.stream()
								.filter(f -> f.getOperator() != null && f.getCoverageCount() != null)
								.collect(Collectors.groupingBy(NVCompetitiveDashboard::getOperator,
										Collectors.summingLong(NVCompetitiveDashboard::getCoverageCount)));

			 Map<String, Long> noCoverageMap = competitiveSamples	.stream()
					.filter(f -> f.getOperator() != null && f.getNoCoverageCount() != null)
					.collect(Collectors.groupingBy(NVCompetitiveDashboard::getOperator,
							Collectors.summingLong(NVCompetitiveDashboard::getNoCoverageCount)));

			 Map<String, Long> wifiMap = competitiveSamples	.stream()
					.filter(f -> f.getOperator() != null && f.getWifiCount() != null)
					.collect(Collectors.groupingBy(NVCompetitiveDashboard::getOperator,
							Collectors.summingLong(NVCompetitiveDashboard::getWifiCount)));
			 
			 Set<Entry<String, Long>> entrySet = coverageMap.entrySet();
			 for(Entry<String,Long> e : entrySet) {
				 String key = e.getKey();
				 Map<String,Long> innermap =  new HashMap<>();
				 innermap.put(NVCompetitiveConstant.COVERAGE, e.getValue());
				 innermap.put(NVCompetitiveConstant.NO_COVERAGE, noCoverageMap.get(key));
				 innermap.put(NVCompetitiveConstant.WIFI_COVERAGE, wifiMap.get(key));
				 responceMap.put(key, innermap);
			 }
			 
		} else {
			return null;
		}
		return responceMap;
	}

	
	/**
	 * Get Competitive KPI distribution by date operator and geographies.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operator the operator
	 * @return the KPI distibution
	 */
	@Cacheable(value="KPIDistributionCache", condition="#result != null")
	@Override
	public Map<String,Map<String,Map<String,String>>> getKPIDistribution(String date, String geographyL1, String geographyL2,
			String geographyL3, String geographyL4, List<String> operator,String appName) {
		logger.info("Inside the method getKPIDistibution Service date: {} ,operator: {},application Name {}", date, operator,appName);
		Map<String,Map<String,Map<String,String>>> populateKPIDistribution = null;
		if (date != null && operator != null) {
			List<NVCompetitiveUser> competitiveResponse = iNVCompetitiveUserDao.getCompetitiveUser(date,
					geographyL1, geographyL2, geographyL3, geographyL4, operator,appName);
			populateKPIDistribution = populateKPIDistribution(competitiveResponse);
		}

		if (MapUtils.isNotEmpty(populateKPIDistribution)) {
			return populateKPIDistribution;
		} else {
			return null;
		}
	}

	/**
	 * Generate map as <kpi, <operator, <avg/count, value>>.
	 *
	 * @param competitiveResponse the competitive response
	 * @return the map
	 */
	@Override
	public Map<String, Map<String, Map<String, String>>> populateKPIDistribution(List<NVCompetitiveUser> competitiveResponse) {
		
		Map<String, Map<String, Map<String, String>>> map = new HashMap<>();
       
		try {
			Map<String, Map<String, String>> innermapRsrp = new HashMap<>();
			Map<String, Map<String, String>> innermapSinr = new HashMap<>();
			Map<String, Map<String, String>> innermapRscp = new HashMap<>();
			Map<String, Map<String, String>> innermapRsrq = new HashMap<>();
			Map<String, Map<String, String>> innermapRxLevel = new HashMap<>();

			for (NVCompetitiveUser user : competitiveResponse) {
				if (user.getOperator() != null) {
					setCompititiveUser(map, innermapRsrp, innermapSinr, innermapRscp, innermapRsrq, innermapRxLevel,
							user); 
				}
			}

		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}


		if (MapUtils.isNotEmpty(map)) {
			return map;
		} else {
			return null;
		}


	}

	private void setCompititiveUser(Map<String, Map<String, Map<String, String>>> map,
			Map<String, Map<String, String>> innermapRsrp, Map<String, Map<String, String>> innermapSinr,
			Map<String, Map<String, String>> innermapRscp, Map<String, Map<String, String>> innermapRsrq,
			Map<String, Map<String, String>> innermapRxLevel, NVCompetitiveUser user) {
		if (user.getRsrpJson() != null) {
			Map<String, String> rsrpJson = new Gson().fromJson(user.getRsrpJson(),
					new TypeToken<HashMap<String, String>>() {
					}.getType());
			popluateSameResponse(user, innermapRsrp, rsrpJson);
			map.put(ForesightConstants.RSRP, innermapRsrp);
		}
		if (user.getSinrJson() != null) {
			Map<String, String> sinrJson = new Gson().fromJson(user.getSinrJson(),
					new TypeToken<HashMap<String, String>>() {
					}.getType());
			popluateSameResponse(user, innermapSinr, sinrJson);
			map.put(ForesightConstants.SINR, innermapSinr);
		}
		if (user.getRscpJson() != null) {

			Map<String, String> rscpJson = new Gson().fromJson(user.getRscpJson(),
					new TypeToken<HashMap<String, String>>() {
					}.getType());
			popluateSameResponse(user, innermapRscp, rscpJson);
			map.put(NVCompetitiveConstant.RSCP, innermapRscp);
		}
		if (user.getRsrqJson() != null) {

			Map<String, String> rsrqJson = new Gson().fromJson(user.getRsrqJson(),
					new TypeToken<HashMap<String, String>>() {
					}.getType());
			popluateSameResponse(user, innermapRsrq, rsrqJson);
			map.put(NVCompetitiveConstant.RSRQ, innermapRsrq);
		}
		if (user.getRxLevelJson() != null) {

			Map<String, String> rxLevelJson = new Gson().fromJson(user.getRxLevelJson(),
					new TypeToken<HashMap<String, String>>() {
					}.getType());
			popluateSameResponse(user, innermapRxLevel, rxLevelJson);
			map.put(NVCompetitiveConstant.RXLEVEL, innermapRxLevel);
		}
	}

	private void popluateSameResponse(NVCompetitiveUser user, Map<String, Map<String, String>> genericMap,
			Map<String, String> kpiCountJson) {
		if (genericMap.containsKey(user.getOperator())) {
			Map<String, String> oldAvgOrCountMap = genericMap.get(user.getOperator());
			Map<String, String> iterativeMap = new HashMap<>();
			Double newAvg = kpiCountJson.get(NVCompetitiveConstant.AVG) != null 
					? Double.parseDouble(kpiCountJson.get(NVCompetitiveConstant.AVG))
					: 0.0;
			Integer newCount = kpiCountJson.get(NVCompetitiveConstant.COUNT) != null
					? Integer.parseInt(kpiCountJson.get(NVCompetitiveConstant.COUNT))
					: 0;
			Double oldAvg = oldAvgOrCountMap.get(NVCompetitiveConstant.AVG) != null
					? Double.parseDouble(oldAvgOrCountMap.get(NVCompetitiveConstant.AVG))
					: 0.0;
			Integer oldCount = oldAvgOrCountMap.get(NVCompetitiveConstant.COUNT) != null
					? Integer.parseInt(oldAvgOrCountMap.get(NVCompetitiveConstant.COUNT))
					: 0;
			updateCountAndWeightedAvg(newCount, newAvg, oldAvg, oldCount, iterativeMap);
			genericMap.put(user.getOperator(), iterativeMap);
		} else {
			genericMap.put(user.getOperator(), kpiCountJson);
		}
	}

	private void updateCountAndWeightedAvg(Integer newCount, Double newAvg, Double oldAvg, Integer oldCount,
			Map<String, String> iterativeMap) {
		if ((Utils.hasValue(newCount) && Utils.hasValue(newAvg)) && (Utils.hasValue(oldAvg) && Utils.hasValue(oldCount))) {
			Integer totalCount = newCount + oldCount;
			Double weightedAvg = totalCount > 0 ? (newCount * newAvg + oldAvg * oldCount) / totalCount : 0.0;
			if (weightedAvg != 0.0) {
				iterativeMap.put(NVCompetitiveConstant.AVG, weightedAvg.toString());
			}
			iterativeMap.put(NVCompetitiveConstant.COUNT, totalCount.toString());
		}
	}

	/**
	 * Get Operator distribution count by date operator and geographies.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operator the operator
	 * @return the operator distribution
	 */
	@Cacheable(value="OperatorDistributionCache", condition = "#result != null")
	@Override
	public String getOperatorDistribution(String date, String geographyL1, String geographyL2,
			String geographyL3, String geographyL4, List<String> operator,String appName) {
		logger.info("Inside the method getOperatorDistibution Service date: {} ,operator: {},application Name {}", date, operator,appName);
		Map<String, Map<String, Long>> populateOperatorDistribution = null;
		if (date != null && operator != null) {
			List<NVCompetitiveDashboard> competitiveDashboard = iNVCompetitiveDashboardDao.getCompetitiveSamples(date,
					geographyL1, geographyL2, geographyL3, geographyL4, operator,null,appName);
			populateOperatorDistribution = populateOperatorDistribution(competitiveDashboard);
		}
		return iNVPassiveDashboardService.getTop5MakeAndModel(populateOperatorDistribution);
	}

	/**
	 * Generate map as <operator, <make, count>>.
	 *
	 * @param competitiveDashboard the competitive dashboard
	 * @return the map
	 */
	private Map<String, Map<String, Long>> populateOperatorDistribution(List<NVCompetitiveDashboard> competitiveDashboard) {
		Map<String, Map<String, Long>> responseMap = new HashMap<>();
		Collections.sort(competitiveDashboard, (NVCompetitiveDashboard o1, NVCompetitiveDashboard o2) -> 
	            StringUtils.compare(o1.getOperator(), o2.getOperator()));
		Map<String, List<Map<String, Long>>> newMap = new HashMap<>();
		List<Map<String, Long>> list = new ArrayList<>();
		if (competitiveDashboard != null && !competitiveDashboard.isEmpty()) {
			for(NVCompetitiveDashboard nvCompetitiveDashboard: competitiveDashboard) {
				HashMap<String, Long> hashmap = null;
				if(nvCompetitiveDashboard.getMake() != null) {
					hashmap = new Gson().fromJson(nvCompetitiveDashboard.getMake(),
							new TypeToken<HashMap<String, Long>>() {}.getType());
					if(!newMap.containsKey(nvCompetitiveDashboard.getOperator())) {
						list = new ArrayList<>();
					}
					list.add(hashmap);
					newMap.put(nvCompetitiveDashboard.getOperator(), list);
				}
			}
		}
		newMap.forEach((operator, listOfMap) -> {
			Map<String, Long> maps = listOfMap.stream()
				    .flatMap(entry -> entry.entrySet().stream())
				    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
			responseMap.put(operator, maps);
		});
		
		return responseMap;
	}

	/**
	 * Get KPI ranges by date, operator, geographies and KPI type.
	 *
	 * @param date the date
	 * @param kpi the kpi
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operators the operators
	 * @return the KPI ranges
	 */
	@Cacheable(value="KPIRangesCache", condition="#result != null")
	@Override
	public Map<String, Map<String,Long>> getKPIRanges(String date, String kpi, String geographyL1, String geographyL2, String geographyL3,
			String geographyL4, List<String> operators,String appName) {
		logger.info("Inside the method getKPIRanges Service date: {} ,operator: {},application Name {}", date, operators,appName);
		Map<String, Map<String,Long>> populateKPIRanges = null;
		try {
			if (date != null && operators != null) {
				List<NVCompetitiveDashboard> competitiveDashboard = iNVCompetitiveDashboardDao.getCompetitiveSamples(date,
						geographyL1, geographyL2, geographyL3, geographyL4, operators, null,appName);
				populateKPIRanges = populateKPIRanges(competitiveDashboard, kpi);

			}
		} catch (Exception e) {
			logger.info("Exception inside getKPIRanges: {}",Utils.getStackTrace(e));
		}
		return populateKPIRanges;
	}

	/**
	 * Generate map as <range, count>.
	 *
	 * @param competitiveDashboard the competitive dashboard
	 * @param kpi the kpi
	 * @return the map
	 */
	@Override
	public Map<String, Map<String,Long>> populateKPIRanges(List<NVCompetitiveDashboard> competitiveDashboard, String kpi) {
		
		Map<String, Map<String,Long>> responseMap = new HashMap<>();
		Map<String, List<NVCompetitiveDashboard>> operatorWiseMap = competitiveDashboard.stream()
															.filter(f -> f.getOperator() != null)
															.collect(Collectors.groupingBy(NVCompetitiveDashboard::getOperator));
		
		operatorWiseMap.forEach((operator, nvCompetitiveDashboard) -> {
			
			List<String> list = null;
			switch(kpi) {
			case ForesightConstants.RSRP:
				list = nvCompetitiveDashboard.stream().filter(f -> f.getRsrp() != null).map(NVCompetitiveDashboard::getRsrp).collect(Collectors.toList());
				break;
			case NVCompetitiveConstant.RSRQ:
				list = nvCompetitiveDashboard.stream().filter(f -> f.getRsrq() != null).map(NVCompetitiveDashboard::getRsrq).collect(Collectors.toList());
				break;
			case NVCompetitiveConstant.RSCP:
				list = nvCompetitiveDashboard.stream().filter(f -> f.getRscp() != null).map(NVCompetitiveDashboard::getRscp).collect(Collectors.toList());
				break;
			case ForesightConstants.SINR:
				list = nvCompetitiveDashboard.stream().filter(f -> f.getSinr() != null).map(NVCompetitiveDashboard::getSinr).collect(Collectors.toList());
				break;
			case NVCompetitiveConstant.RXLEVEL:
				list = nvCompetitiveDashboard.stream().filter(f -> f.getRxLevel() != null).map(NVCompetitiveDashboard::getRxLevel).collect(Collectors.toList());
				break;
			default:
				list = new ArrayList<>();
			}

			List<Map<String, Long>> jsonList = new ArrayList<>();
			list.forEach(json -> 
				jsonList.add(new Gson().fromJson(json,
						new TypeToken<HashMap<String, Long>>() {}.getType()))
			);
			
			responseMap.put(operator, jsonList.stream()
		    .flatMap(map -> map.entrySet().stream())
		    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum )));
		});


		if (MapUtils.isNotEmpty(responseMap)) {
			return responseMap;
		} else {
			return null;
		}
	}
	
	/**
	 * Get DL+UL distribution count by date operator and geographies.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operators the operators
	 * @return the dl ul distribution
	 */
	@Cacheable(value="DlUlDistributionCache", condition="#result != null")
	@Override
	public Map<String, Map<String, Map<String, Map<String, String>>>> getDlUlDistribution(String date, String geographyL1, String geographyL2,
			String geographyL3, String geographyL4, List<String> operators,String appName) {
		
		logger.info("Inside the method dlulDistribution Service date: {} ,operator: {},application Name {}", date, operators,appName);
		Map<String, Map<String, Map<String, Map<String, String>>>> populateDlUlDistribution = null;
		if (date != null && operators != null) {
			List<NVCompetitiveUser> competitiveResponse = iNVCompetitiveUserDao.getCompetitiveUser(date,
					geographyL1, geographyL2, geographyL3, geographyL4, operators,appName);
			populateDlUlDistribution = populateDlUlDistribution(competitiveResponse);
		}
		return populateDlUlDistribution;
	}

	/**
	 * Generate map as <kpi, <operator, <technology, <avg/count, value>>>.
	 *
	 * @param competitiveResponse the competitive response
	 * @return the map
	 */
	@Override
	public Map<String, Map<String, Map<String, Map<String, String>>>> populateDlUlDistribution(List<NVCompetitiveUser> competitiveResponse) {
	Map<String, Map<String, Map<String, Map<String, String>>>> map = new HashMap<>();
		
		try {
			Map<String, Map<String, Map<String, String>>> innermapUL = new HashMap<>();
			Map<String, Map<String, Map<String, String>>> innermapDL = new HashMap<>();
			for (NVCompetitiveUser user : competitiveResponse) {
				if (user.getUlJson() != null && user.getOperator() != null) {
					Map<String, Map<String, String>> ulJson = new Gson().fromJson(user.getUlJson(),
							new TypeToken<HashMap<String, Map<String, String>>>() {
							}.getType());
					innermapUL.put(user.getOperator(), ulJson);
					map.put(ForesightConstants.UL_PREFIX, innermapUL);
				}
				
				if (user.getDlJson() != null && user.getOperator() != null) {
					Map<String, Map<String, String>> dlJson = new Gson().fromJson(user.getDlJson(),
							new TypeToken<HashMap<String, Map<String, String>>>() {
							}.getType());
					innermapDL.put(user.getOperator(), dlJson);
					map.put(ForesightConstants.DL_PREFIX, innermapDL);
				}
			}
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}

		if (MapUtils.isNotEmpty(map)) {
			return map;
		} else {
			return null;
		}
	}

	/**
	 * Get DL/UL Ranges by date, operator, geographies and DL/UL type.
	 *
	 * @param date the date
	 * @param kpi the kpi
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operators the operators
	 * @return the dl ul ranges
	 */
	@Cacheable(value="DlUlRangesCache", condition="#result != null")
	@Override
	public Map<String, Map<String, Map<String, Integer>>> getDlUlRanges(String date, String kpi, String geographyL1, String geographyL2,
			String geographyL3, String geographyL4, List<String> operators,String appName) {
		logger.info("Inside the method getDlUlRanges Service date: {} ,operator: {},application Name {}", date, operators,appName);
		Map<String, Map<String, Map<String, Integer>>> populateDlUlRanges = null;
		if (date != null && operators != null) {
			List<NVCompetitiveDashboard> competitiveDashboard = iNVCompetitiveDashboardDao.getCompetitiveSamples(date,
					geographyL1, geographyL2, geographyL3, geographyL4, operators,null,appName);
			populateDlUlRanges = populateDlUlRanges(competitiveDashboard, kpi);
		}
		return populateDlUlRanges;
	}

	/**
	 * Generate map as <operator, <technology, <avg/count, value>>.
	 *
	 * @param competitiveDashboard the competitive dashboard
	 * @param kpi the kpi
	 * @return the map
	 */
	@Override
	public Map<String, Map<String, Map<String, Integer>>> populateDlUlRanges(List<NVCompetitiveDashboard> competitiveDashboard, String kpi) {
		Map<String, Map<String, Map<String, Integer>>> rangeMap = new HashMap<>();
		try {
			//Group by basis of operator technology
			Map<String, Map<String, List<NVCompetitiveDashboard>>> map = competitiveDashboard	.stream()
																			.filter(f -> f.getOperator() != null
																			&& f.getTechnology() != null)
																			.collect(Collectors.groupingBy(
																				NVCompetitiveDashboard::getTechnology,
																			Collectors.groupingBy(
																				NVCompetitiveDashboard::getOperator)));
			//Iterate map on operator
			map.forEach((key, val) -> {
				Map<String, Map<String, Integer>> value = new HashMap<>();
				
				//Iterate inner map on technology
				val.forEach((key1, val1) ->	collectDlUlJson(kpi, key, value, key1, val1));
				if (value != null && !value.isEmpty()) {
					rangeMap.put(key, value);
				}
			});

		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}

		if (MapUtils.isNotEmpty(rangeMap)) {
			return rangeMap;
		} else {
			return null;
		}
	}

	private void collectDlUlJson(String kpi, String key, Map<String, Map<String, Integer>> value, String key1,
			List<NVCompetitiveDashboard> val1) {
		//Collect DL-UL JSON value list
		List<String> collectedList = null;

		switch (key) {
		case NVCompetitiveConstant.TECHNOLOGY_LTE:
			if (kpi.equalsIgnoreCase(ForesightConstants.DL_PREFIX)) {
				collectedList = val1.stream()
									.filter(f -> f.getDlJsonLTE() != null)
									.map(NVCompetitiveDashboard::getDlJsonLTE)
									.collect(Collectors.toList());

			} else if (kpi.equalsIgnoreCase(ForesightConstants.UL_PREFIX)) {
				collectedList = val1.stream()
									.filter(f -> f.getUlJsonLTE() != null)
									.map(NVCompetitiveDashboard::getUlJsonLTE)
									.collect(Collectors.toList());
			}
			break;
		case NVCompetitiveConstant.TECHNOLOGY_2G:
			if (kpi.equalsIgnoreCase(ForesightConstants.DL_PREFIX)) {
				collectedList = val1.stream()
									.filter(f -> f.getDlJson2G() != null)
									.map(NVCompetitiveDashboard::getDlJson2G)
									.collect(Collectors.toList());

			} else if (kpi.equalsIgnoreCase(ForesightConstants.UL_PREFIX)) {
				collectedList = val1.stream()
									.filter(f -> f.getUlJson2G() != null)
									.map(NVCompetitiveDashboard::getUlJson2G)
									.collect(Collectors.toList());
			}
			break;
		case NVCompetitiveConstant.TECHNOLOGY_3G:
			if (kpi.equalsIgnoreCase(ForesightConstants.DL_PREFIX)) {
				collectedList = val1.stream()
									.filter(f -> f.getDlJson3G() != null)
									.map(NVCompetitiveDashboard::getDlJson3G)
									.collect(Collectors.toList());

			} else if (kpi.equalsIgnoreCase(ForesightConstants.UL_PREFIX)) {
				collectedList = val1.stream()
									.filter(f -> f.getUlJson3G() != null)
									.map(NVCompetitiveDashboard::getUlJson3G)
									.collect(Collectors.toList());
			}
			break;

		default:
			collectedList = new ArrayList<>();
			break;
		}

		List<HashMap<String, Integer>> listOfJsonMap;
		if (CollectionUtils.isNotEmpty(collectedList)) {
			listOfJsonMap = iterationPerformOnCollectionList(collectedList);
			Map<String, Integer> maps = listOfJsonMap.stream().flatMap(f -> f.entrySet()
					.stream()).collect(Collectors.toMap(Map.Entry::getKey,
							Map.Entry::getValue, Integer::sum));
			if (!maps.isEmpty()) {
				value.put(key1, maps);
			}
		}
	}

	private List<HashMap<String, Integer>> iterationPerformOnCollectionList(List<String> collectedList) {
		//Iterate collected list
		List<HashMap<String, Integer>> listOfJsonMap = new ArrayList<>();
		if(!collectedList.isEmpty()) {
			for(String string: collectedList) {
				HashMap<String, Integer> jsonMap = null;
				try {
					jsonMap = new Gson().fromJson(string,
							new TypeToken<HashMap<String, Integer>>() {}.getType());
					if(jsonMap != null && !jsonMap.isEmpty()) {
						listOfJsonMap.add(jsonMap);
					}
				} catch (Exception e) {
					logger.error(Utils.getStackTrace(e));
				}
			}
		}
		return listOfJsonMap;
	}

	/**
	 * Gets the NPS data.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operators the operators
	 * @return the NPS data
	 */
	@Override
	public Map<String, Double> getNPSData(String date, String geographyL1, String geographyL2,
			String geographyL3, String geographyL4, List<String> operators) {
		logger.info("Inside service method to find NPS Records");
		List<Object[]> npsRawData = iNPSRawDao.getNPSRawData(date, geographyL1, geographyL2, geographyL3, geographyL4,
				operators);
		Map<String, Double> response = new HashMap<>();
		for (Object[] npsRecord : npsRawData) {
			response.put(npsRecord[0].toString(), Double.parseDouble(npsRecord[1].toString()));
		}

		return response;
	}

	/**
	 * Gets the performance KPI data.
	 *
	 * @param date the date
	 * @param kpi the kpi
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operators the operators
	 * @param technology the technology
	 * @return the performance KPI data
	 */
	@Cacheable(value="PerformanceKPIDataCache", condition="#result!= null")
	@Override
	public Object getPerformanceKPIData(String date, String kpi, String geographyL1, String geographyL2,
			String geographyL3, String geographyL4, List<String> operators, String technology,String appName) {
		logger.info("Inside the method getPerformanceKPIData Service date: {} ,operator: {},kpi: {},technology: {} ,application Name {}", date, operators,kpi,technology,appName);
		if (date != null && operators != null && kpi != null) {
			List<NVCompetitiveDashboard> competitiveDashboard = iNVCompetitiveDashboardDao.getCompetitiveSamples(date,
					geographyL1, geographyL2, geographyL3, geographyL4, operators,technology,appName);
			return populatePerformanceData(competitiveDashboard, kpi);
		}
		return null;
	}

	/**
	 * Populate performance data.
	 *
	 * @param competitiveDashboard the competitive dashboard
	 * @param kpi the kpi
	 * @return the object
	 */
	@Override
	public Object populatePerformanceData(
			List<NVCompetitiveDashboard> competitiveDashboard, String kpi) {
		Type type = new TypeToken<Map<String, Long>>() {
		}.getType();
		Map<String, List<Map<String, Long>>> map = new HashMap<>();
		for (NVCompetitiveDashboard entry : competitiveDashboard) {
			List<Map<String, Long>> list = updateListUsingKPI(kpi, type, entry);
			if (map.containsKey(entry.getOperator().toUpperCase()) && !list.isEmpty()) {
				List<Map<String, Long>> innerList = map.get(entry.getOperator().toUpperCase());
				innerList.addAll(list);
				map.put(entry.getOperator().toUpperCase(), innerList);
			} else if (!list.isEmpty()) {
				map.put(entry.getOperator().toUpperCase(), list);
			}
		}
		return populateCountMap(map);
	}

	private List<Map<String, Long>> updateListUsingKPI(String kpi, Type type, NVCompetitiveDashboard entry) {
		List<Map<String, Long>> list = new ArrayList<>();
		if (kpi.equalsIgnoreCase(NVCompetitiveConstant.TIME_TO_LOAD) && entry.getTtl() != null) {
			list.add(new Gson().fromJson(entry.getTtl(), type));
		} else if (kpi.equalsIgnoreCase(NVCompetitiveConstant.BUFFER_TIME) && entry.getBuffertime() != null) {
			list.add(new Gson().fromJson(entry.getBuffertime(), type));
		} else if (kpi.equalsIgnoreCase(NVCompetitiveConstant.DNS) && entry.getDns() != null) {
			list.add(new Gson().fromJson(entry.getDns(), type));
		} else if (kpi.equalsIgnoreCase(NVCompetitiveConstant.YOUTUBE_DL) && entry.getYoutubedl() != null) {
			list.add(new Gson().fromJson(entry.getYoutubedl(), type));
		} else if (kpi.equalsIgnoreCase(NVCompetitiveConstant.LATENCY) && entry.getLatency() != null) {
			list.add(new Gson().fromJson(entry.getLatency(), type));
		}
		return list;
	}

	/**
	 * Populate count map.
	 * @param operatorWiseMap the Map
	 * @return the object
	 */
	private Object populateCountMap(Map<String,List<Map<String, Long>>> operatorWiseMap) {
			Map<String,Map<String,Long>> response = new HashMap<>();
		if (operatorWiseMap != null && !operatorWiseMap.isEmpty()) {
			for(Entry<String, List<Map<String, Long>>> entry:operatorWiseMap.entrySet())
			{
				Map<String, Long> statusMap = entry.getValue().stream().flatMap(map -> map.entrySet().stream())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
				response.put(entry.getKey(), statusMap);
			}
			return response;
		}
		return null;
	}

	
}
