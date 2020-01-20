package com.inn.foresight.module.nv.wpt.analytics.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;
import com.inn.foresight.module.nv.wpt.analytics.dao.IWPTDashboardDetailDao;
import com.inn.foresight.module.nv.wpt.analytics.dao.IWPTLocationDao;
import com.inn.foresight.module.nv.wpt.analytics.dao.IWebCategoryDao;
import com.inn.foresight.module.nv.wpt.analytics.model.WebCategory;
import com.inn.foresight.module.nv.wpt.analytics.service.IWPTDashboardDetailService;
import com.inn.foresight.module.nv.wpt.analytics.utils.WPTDashboardUtils;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.TopURLCountWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.TopURLDetailWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTAnalysisGraphWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDashboardKpiWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDayWiseCountWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTHTTPStatsWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTOperatorWrapper;
import com.inn.product.um.geography.service.GeographyL1Service;
import com.inn.product.um.geography.service.GeographyL2Service;
import com.inn.product.um.geography.service.GeographyL3Service;
import com.inn.product.um.geography.service.GeographyL4Service;
import com.inn.product.um.geography.utils.wrapper.GeographyWrapper;
import com.inn.product.um.user.model.UserPreference;
import com.inn.product.um.user.service.UserPreferenceService;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/** The Class WPTDashboardDetailServiceImpl. */
@Service("WPTDashboardDetailServiceImpl")
public class WPTDashboardDetailServiceImpl extends WPTDashboardUtils implements IWPTDashboardDetailService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(WPTDashboardDetailServiceImpl.class);
	
	/** GeographyL1Dao - Instance of GeographyL2ServiceImpl. */
	@Autowired
	private GeographyL1Service geographyL1Service;
	
	/** GeographyL2Dao - Instance of GeographyL2ServiceImpl. */
	@Autowired
	private GeographyL2Service geographyL2Service;
	
	/** GeographyL3Dao - Instance of GeographyL3ServiceImpl. */
	@Autowired
	private GeographyL3Service geographyL3Service;
	
	/** GeographyL4Dao - Instance of GeographyL4ServiceImpl. */
	@Autowired
	private GeographyL4Service geographyL4Service;
	
	/** WptLocationDao - Instance of WPTLocationDaoImpl. */
	@Autowired
	private IWPTLocationDao wptLocationDao;
	
	/** WebCategoryDao - Instance of WebCategoryDaoImpl. */
	@Autowired
	private IWebCategoryDao webCategoryDao;
	
	/** Dao - Instance of WPTDashboardDetailDaoImpl. */
	@Autowired
	private IWPTDashboardDetailDao dao;
	
	/** Dao - Instance of UserPreferenceServiceImpl. */
	@Autowired
	private UserPreferenceService userPreferenceService;

	
	/**
	 * Gets the target ip version percent.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the target ip version percent
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Double> getTargetIpVersionPercent(String date, String processType, String geographyLevel,
			Integer geographyId, String operator, String technology) {
		logger.info("Inside @method : getTargetIpVersionPercent()");
		try {
			Map<String, Long> map = dao.getTargetIpVersionCount(date, processType,
					getFilterAndValueMap(geographyLevel, GEOGRAPHY_ONLY, operator, geographyId, null), geographyId,
					operator, technology);
			
			return getIpVersionPercentMap(map);
			
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER , e.getMessage());
		}
		return Collections.emptyMap();
	}
	
	/**
	 * Gets the ip version percent map.
	 *
	 * @param countMap the count map
	 * @return the ip version percent map
	 */
	private static Map<String, Double> getIpVersionPercentMap(Map<String, Long> countMap) {
		Map<String, Double> map = new HashMap<>();
		Long totalCount = countMap.get(TOTAL);
		map.put(IPV4, getPercentage(totalCount, countMap.get(IPV4)));
		map.put(IPV6, getPercentage(totalCount, countMap.get(IPV6)));
		return map;
	}
	
	/**
	 * Gets the day wise counts.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the day wise counts
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Map<String, Long>> getDayWiseCounts(String date, String processType, String geographyLevel,
			Integer geographyId, String operator, String technology) {
		logger.info("Inside @method : getDayWiseCounts() geographyId {}", geographyId);
		
		Map<String, Map<String, Long>> map = new HashMap<>();
		
		try {
			List<String> timeConstantList = getDateConstantList(date, processType);
			Map<String, List> filterMap = getFilterAndValueMap(geographyLevel, GEOGRAPHY_ONLY, operator, geographyId, null);
			
			map.put(TOTAL_RECORD, getDateWiseMap(dao.getTotalRecordCount(timeConstantList, processType, filterMap,
					geographyId, operator, technology), timeConstantList));
			
			map.put(TOTAL_DEVICE, getDateWiseMap(dao.getTotalDeviceCount(timeConstantList, processType, filterMap,
					geographyId, operator, technology), timeConstantList));
			
			map.put(TOTAL_URL, getDateWiseMap(dao.getTotalUrlCount(timeConstantList, processType, filterMap,
					geographyId, operator, technology), timeConstantList));
		
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER , e.getMessage());
		}
		return map;
	}

	/**
	 * Gets the date wise map.
	 *
	 * @param totalRecordCount the total record count
	 * @param timeConstantList the time constant list
	 * @return the date wise map
	 */
	private Map<String, Long> getDateWiseMap(List<WPTDayWiseCountWrapper> totalRecordCount, List<String> timeConstantList) {
		Map<String, Long> map = new HashMap<>();
		for(WPTDayWiseCountWrapper wrapper : totalRecordCount) {
			map.put(wrapper.getDate(), wrapper.getCount());
		}
		return updateLongMapForAllDates(map, timeConstantList);
	}

	/**
	 * Gets the top url counts.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the top url counts
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, TopURLCountWrapper> getTopUrlCounts(String date, String processType, String geographyLevel,
			Integer geographyId, String operator, String technology) {
		logger.info("Inside @method : getTopUrlCounts()");
		Map<String, TopURLCountWrapper> map = new HashMap<>();		
		try {
			Map<String, List> filterMap = getFilterAndValueMap(geographyLevel, GEOGRAPHY_ONLY, operator, geographyId, null);
			TopURLCountWrapper urlCountWrapper = getURLCountWrapper(
					dao.getTopTestedURLCounts(date, processType, filterMap, geographyId, operator, technology),
					dao.getTotalTestedURLCount(date, processType, filterMap, geographyId, operator, technology));
			map.put(TESTED_URL, urlCountWrapper);
			map.put(TRACE_ROUTE_URL, urlCountWrapper);
			map.put(PING_URL, getURLCountWrapper(
					dao.getTopPingCounts(date, processType, filterMap, geographyId, operator, technology),
					dao.getTotalPingCount(date, processType, filterMap, geographyId, operator, technology)));
		} catch (ValueNotFoundException | RestException e) {
			logger.error(EXCEPTION_LOGGER , ExceptionUtils.getStackTrace(e));
		}
		return map;
	}
	
	/**
	 * Gets the URL count wrapper.
	 *
	 * @param topTestedURLs the top tested UR ls
	 * @param total the total
	 * @return the URL count wrapper
	 */
	private TopURLCountWrapper getURLCountWrapper(List<TopURLDetailWrapper> topTestedURLs,Long total) {
		TopURLCountWrapper urlCountWrapper = new TopURLCountWrapper();
		if (CollectionUtils.isNotEmpty(topTestedURLs) && total != null) {
			urlCountWrapper.setUrlWrappers(topTestedURLs);
			urlCountWrapper.setTotalHitCount(total);
		}
		return urlCountWrapper;
	}
	
	/**
	 * Gets the URL details.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @param isPingDataRequired the is ping data required
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the URL details
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> getURLDetails(String date, String processType, String geographyLevel,
			Integer geographyId, String operator, String technology, Boolean isPingDataRequired, Integer ulimit,
			Integer llimit) {
		logger.info("Inside @method : getURLDetails()");
		Map<String, Object> map = new HashMap<>();
		try {
			Map<String, List> filterMap = getFilterAndValueMap(geographyLevel, GEOGRAPHY_ONLY, operator, geographyId, null);
			
			map.put(TOTAL, getTotalCountForAllUrls(isPingDataRequired, date, processType, filterMap,
					geographyId, operator, technology));
			map.put(DATA, getAllUrlDetails(isPingDataRequired, date, processType, filterMap, geographyId, operator,
					technology, ulimit, llimit));
		} catch (ValueNotFoundException | RestException e) {
			logger.error(EXCEPTION_LOGGER , ExceptionUtils.getStackTrace(e));
		}
		return map;
	}
	
	/**
	 * Gets the all url details.
	 *
	 * @param isPingDataRequired the is ping data required
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the all url details
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("rawtypes")
	private List<TopURLDetailWrapper> getAllUrlDetails(Boolean isPingDataRequired, String date, String processType,
			Map<String, List> filterMap, Integer geographyId, String operator, String technology, Integer ulimit,
			Integer llimit) throws ValueNotFoundException {
		
		if (isPingDataRequired) {
			return dao.getTopPingDetails(date, processType, filterMap, geographyId, operator, technology, ulimit,
					llimit);
		} else {
			return dao.getTopTestedURLDetails(date, processType, filterMap, geographyId, operator, technology,
					ulimit, llimit);
		}
	}

	/**
	 * Gets the total count for all urls.
	 *
	 * @param isPingDataRequired the is ping data required
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the total count for all urls
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("rawtypes")
	private Long getTotalCountForAllUrls(Boolean isPingDataRequired, String date, String processType,
			Map<String, List> filterMap, Integer geographyId, String operator, String technology)
			throws ValueNotFoundException {	
		
		if (isPingDataRequired) {
			return dao.getTotalPingCount(date, processType, filterMap, geographyId, operator, technology);
		} else {
			return dao.getTotalTestedURLCount(date, processType, filterMap, geographyId, operator, technology);
		}
	}

	/**
	 * Gets the avg TTFB detail.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the avg TTFB detail
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Double> getAvgTTFBDetail(String date, String processType, String geographyLevel,
			Integer geographyId, String operator, String technology) {
		logger.info("Inside @method : getAvgTTFBDetail()");
				
		try {
			List<String> timeConstantList = getDateConstantList(date, processType);
		
			Map<String, Double> map = getParsedMap(dao.getAvgTTFBDetail(timeConstantList, processType,
					getFilterAndValueMap(geographyLevel, GEOGRAPHY_ONLY, operator, geographyId, null), geographyId,
					operator, technology));
		
			return updateDoubleMapForAllDates(map, timeConstantList);

		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER , ExceptionUtils.getStackTrace(e));
			return Collections.emptyMap();
		}
	}
	
	/**
	 * Gets the parsed map.
	 *
	 * @param list the list
	 * @return the parsed map
	 */
	private Map<String, Double> getParsedMap(List<WPTDashboardKpiWrapper> list) {
		Map<String, Double> map = new HashMap<>();
		for(WPTDashboardKpiWrapper wrapepr : list) {
			map.put(wrapepr.getDate(), wrapepr.getAvgKpiA());
		}
		return map;
	}

	/**
	 * Gets the HTTP and HTTPS count.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the HTTP and HTTPS count
	 */
	@Override
	public Map<String,  Object> getHTTPAndHTTPSCount(String date, String processType, String geographyLevel,
			Integer geographyId, String operator, String technology) {
		logger.info("Inside @method : getHTTPStatsData()");
		Map<String, Object> mapToReturn = new HashMap<>();
		try {
			List<String> timeConstantList = getDateConstantList(date, processType);
			
			List<WPTHTTPStatsWrapper> list = dao.getHTTPAndHTTPSCount(timeConstantList, processType, 
					getFilterAndValueMap(geographyLevel, GEOGRAPHY_ONLY, operator, geographyId, null), 
					geographyId, operator, technology);
			Map<String, Long[]> dayWiseMap = getParsedMap(list, timeConstantList);
			
			mapToReturn.put(DATA, dayWiseMap);
			mapToReturn.put(AVERAGE, getDayWiseCountAverage(dayWiseMap));
		
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return mapToReturn;
	}

	/**
	 * Gets the parsed map.
	 *
	 * @param list the list
	 * @param timeConstantList the time constant list
	 * @return the parsed map
	 */
	private Map<String, Long[]> getParsedMap(List<WPTHTTPStatsWrapper> list, List<String> timeConstantList) {
		Map<String, Long[]> map = new HashMap<>();
		for(WPTHTTPStatsWrapper wrapper : list) {
			String dayWiseKey = wrapper.getDate();
			
			Long[] countArray = getLongArrayFromMap(map, dayWiseKey);
			updateHTTPOrHTTPSCount(countArray, wrapper.getCount(), wrapper.getIsHttps());
		
			map.put(wrapper.getDate(), countArray);
		}
		return updateLongArrayMapForAllDates(map, timeConstantList);
	}

	/**
	 * Update HTTP or HTTPS count.
	 *
	 * @param countArray the count array
	 * @param count the count
	 * @param isHttps the is https
	 */
	private void updateHTTPOrHTTPSCount(Long[] countArray, Long count, Boolean isHttps){
		if(isHttps) {
			countArray[ForesightConstants.ONE] = count;
		} else {
			countArray[ForesightConstants.ZERO] = count;
		}
	}
	
	/**
	 * Gets the day wise count average.
	 *
	 * @param dayWiseMap the day wise map
	 * @return the day wise count average
	 */
	private Map<String, Double> getDayWiseCountAverage(Map<String, Long[]> dayWiseMap) {
		Map<String, Double> map = new HashMap<>();
		Long httpsCount = NumberUtils.LONG_ZERO;
		Long httpCount = NumberUtils.LONG_ZERO;
		int dayCount = DateTimeConstants.DAYS_PER_WEEK;

		for(Entry<String, Long[]> entry : dayWiseMap.entrySet()) {
			Long[] countArray = entry.getValue();
			if(countArray != null ) {
				if(countArray[ForesightConstants.ZERO] != null) {
					httpCount = httpCount + countArray[ForesightConstants.ZERO];
				}
				if(countArray[1] != null) {
					httpsCount = httpsCount + countArray[1];
				}
			}
		}
		
		map.put(AVG_HTTP, getAverage(httpCount, dayCount));
		map.put(AVG_HTTPS, getAverage(httpsCount, dayCount));
		map.put(AVERAGE, getAverage((httpCount + httpsCount), dayCount));
		return map;
	}
	
	/**
	 * Gets the trace route and TTFB data.
	 *
	 * @param filterMap the filter map
	 * @param compareType the compare type
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the trace route and TTFB data
	 */
	@Override
	public Object getTraceRouteAndTTFBData(Map<String, List<Object>> filterMap, String compareType, String date,
			String processType, String geographyLevel, Integer geographyId, String operator, String technology) {

		try {
			List<String> timeConstantList = getDateConstantList(date, processType);
			List<Object> comparIPVList = filterMap.get(COMPARE_IPV);

			List<WPTDashboardKpiWrapper> list = dao.getTraceRouteAndTTFBData(timeConstantList,
					processType, comparIPVList, filterMap.get(CATEGORY), filterMap.get(LOCATION),
					technology, operator, geographyId,
					getFilterAndValueMap(geographyLevel, compareType, operator, geographyId, comparIPVList));

			return getKpiComparisonJson(list, geographyLevel, compareType, timeConstantList, comparIPVList);

		} catch (Exception e) {
			logger.info(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return Collections.emptyMap();
	}

	/**
	 * Gets the TTFB and TDNS data.
	 *
	 * @param filterMap the filter map
	 * @param compareType the compare type
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param ipVersion the ip version
	 * @return the TTFB and TDNS data
	 */
	@Override
	public Object getTTFBAndTDNSData(Map<String, List<Object>> filterMap, String compareType, String date,
			String processType, String geographyLevel, Integer geographyId, String operator, String ipVersion) {

		try {
			List<String> timeConstantList = getDateConstantList(date, processType);
			List<Object> comparIPVList = filterMap.get(COMPARE_IPV);
			
			List<WPTDashboardKpiWrapper> list = dao.getTTFBAndTDNSData(timeConstantList,
					processType, comparIPVList, filterMap.get(CATEGORY), filterMap.get(LOCATION),
					ipVersion, operator, geographyId,
					getFilterAndValueMap(geographyLevel, compareType, operator, geographyId, comparIPVList));

			return getKpiComparisonJson(list, geographyLevel, compareType, timeConstantList, comparIPVList);

		} catch (Exception e) {
			logger.info(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}

		return Collections.emptyMap();
	}
	
	/**
	 * Gets the kpi comparison json.
	 *
	 * @param list the list
	 * @param geographyLevel the geography level
	 * @param compareType the compare type
	 * @param timeConstantList the time constant list
	 * @param comparIPVList the compar IPV list
	 * @return the kpi comparison json
	 * @throws RestException the rest exception
	 */
	private Map<String, Map<String, Double[]>> getKpiComparisonJson(List<WPTDashboardKpiWrapper> list,
			String geographyLevel, String compareType, List<String> timeConstantList, 
			List<Object> comparIPVList) {

		Map<String, Map<String, Double[]>> map = new HashMap<>();

		for (WPTDashboardKpiWrapper wrapper : list) {
			String jsonKey = getJsonKeyForKpiComparisonGraph(geographyLevel, compareType, wrapper);
			Map<String, Double[]> dayWiseMap = getDoubleArrayFromMap(map, jsonKey);

			Double[] kpiArray = { wrapper.getAvgKpiA(), wrapper.getAvgKpiB() };

			dayWiseMap.put(wrapper.getDate(), kpiArray);
			map.put(jsonKey, updateDoubleArrayMapForAllDates(dayWiseMap, timeConstantList));
		}

		logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, map.size());
		updateKpiMapWithAllKeys(map, comparIPVList);
		return map;
	}

	/**
	 * Gets the TTFB category wise data.
	 *
	 * @param filterMap the filter map
	 * @param compareType the compare type
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param ipVersion the ip version
	 * @return the TTFB category wise data
	 */
	@Override
	public Map<String, Map<String, Double[]>> getTTFBCategoryWiseData(Map<String, List<Object>> filterMap, String compareType, String date,
			String processType, String geographyLevel, Integer geographyId, String operator, String ipVersion) {

		try {
			List<Object> categoryIdList = filterMap.get(CATEGORY);
			List<Object> compareIPVList = filterMap.get(COMPARE_IPV);
			List<String> timeConstantList = getDateConstantList(date, processType);

			List<WPTDashboardKpiWrapper> list = dao.getTTFBCategoryWiseData(timeConstantList,
					processType, compareIPVList, categoryIdList, filterMap.get(LOCATION),
					ipVersion, operator, geographyId,
					getFilterAndValueMap(geographyLevel, compareType, operator, geographyId, compareIPVList));

			return getUpdatedMapWithAllCategory(
					getCategoryWiseComparisonJson(list, timeConstantList),
					webCategoryDao.getWebCategoryIdMap(), categoryIdList);

		} catch (Exception e) {
			logger.info(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return Collections.EMPTY_MAP;
	}


	/**
	 * Gets the updated map with all category.
	 *
	 * @param dataMap the data map
	 * @param webCategoryIdMap the web category id map
	 * @param categoryIdList the category id list
	 * @return the updated map with all category
	 */
	private Map<String, Map<String, Double[]>> getUpdatedMapWithAllCategory(Map<String, Map<String, Double[]>> dataMap,
			Map<Integer, WebCategory> webCategoryIdMap, List<Object> categoryIdList) {
		Map<String, Map<String, Double[]>> map = new HashMap<>();
		for(Object id : categoryIdList) {
			String categoryName = webCategoryIdMap.get((Integer) id).getCategoryName();
			map.put(categoryName, dataMap.get(categoryName));
		}
		return map;
	}

	/**
	 * Gets the category wise comparison json.
	 *
	 * @param list the list
	 * @param geographyLevel the geography level
	 * @param compareType the compare type
	 * @param timeConstantList the time constant list
	 * @return the category wise comparison json
	 */
	private Map<String, Map<String, Double[]>> getCategoryWiseComparisonJson(List<WPTDashboardKpiWrapper> list, List<String> timeConstantList) {
		Map<String, Map<String, Double[]>> map = new HashMap<>();

		for (WPTDashboardKpiWrapper wrapper : list) {
			String jsonKey = wrapper.getCategory();
			Map<String, Double[]> dayWiseMap = getDoubleArrayFromMap(map, jsonKey);

			Double[] kpiArray = { wrapper.getMinTTFB(), wrapper.getMaxTTFB() };

			dayWiseMap.put(wrapper.getDate(), kpiArray);
			map.put(jsonKey, updateDoubleArrayMapForAllDates(dayWiseMap, timeConstantList));
		}

		logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, map.size());
		return map;
	}

	/**
	 * Gets the analysis graph data.
	 *
	 * @param compareType the compare type
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param comapererTech the comaperer tech
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param categories the categories
	 * @param locations the locations
	 * @param technology the technology
	 * @return the analysis graph data
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public WPTAnalysisGraphWrapper getAnalysisGraphData(String compareType,String date, String processType, String geographyLevel,
			List<Object> comapererTech, Integer geographyId, String operator, List<Object> categories, List<Object> locations,
			String technology) {
		WPTAnalysisGraphWrapper graphWrapper = new WPTAnalysisGraphWrapper();
		try {
			List<String> timeConstantList = getDateConstantList(date, processType);
			Map<String, List> filters = getFilterAndValueMap(geographyLevel, compareType, operator, geographyId, comapererTech);
	
			List<TopURLDetailWrapper> detailWrappers = dao.getAnalysisGraphData(timeConstantList, processType,
					categories, locations, filters, technology);
			for(TopURLDetailWrapper detailWrapper : detailWrappers) {
				String key = getKeyForAnalysisGraph(compareType, geographyLevel, detailWrapper);
				updateAnalysisGraphWrapper(graphWrapper, detailWrapper, key);
			}
			updateAnalysisWrapper(graphWrapper, timeConstantList, comapererTech);
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return graphWrapper;
	}

	/**
	 * Update analysis wrapper.
	 *
	 * @param graphWrapper the graph wrapper
	 * @param timeConstantList the time constant list
	 * @param comapererTech the comaperer tech
	 */
	private void updateAnalysisWrapper(WPTAnalysisGraphWrapper graphWrapper, List<String> timeConstantList, 
			List<Object> comapererTech) {
		graphWrapper.setFdns(getUpdateAnalysisKpiMap(graphWrapper.getFdns(), timeConstantList, comapererTech));
		graphWrapper.setTdns(getUpdateAnalysisKpiMap(graphWrapper.getTdns(), timeConstantList, comapererTech));
		graphWrapper.setPing(getUpdateAnalysisKpiMap(graphWrapper.getPing(), timeConstantList, comapererTech));
		graphWrapper.setTraceRoute(getUpdateAnalysisKpiMap(graphWrapper.getTraceRoute(), timeConstantList, comapererTech));
		graphWrapper.setTtfb(getUpdateAnalysisKpiMap(graphWrapper.getTtfb(), timeConstantList, comapererTech));
		graphWrapper.setTtl(getUpdateAnalysisKpiMap(graphWrapper.getTtl(), timeConstantList, comapererTech));
	}

	/**
	 * Gets the update analysis kpi map.
	 *
	 * @param kpiMap the kpi map
	 * @param timeConstantList the time constant list
	 * @param comapererTech the comaperer tech
	 * @return the update analysis kpi map
	 */
	private Map<String, Map<String, Object>> getUpdateAnalysisKpiMap(Map<String, Map<String, Object>> kpiMap,
			List<String> timeConstantList, List<Object> comapererTech) {
		Map<String, Map<String, Object>> map = new HashMap<>();
		for(Entry<String, Map<String, Object>> entry : kpiMap.entrySet()) {
			map.put(entry.getKey(), updateAnalysisDayWiseMap(entry.getValue(), timeConstantList));
		}
		updateSingleKpiMapWithAllKeys(map, comapererTech);
		return map;
	}
	
	/**
	 * Update analysis day wise map.
	 *
	 * @param dayWiseMap the day wise map
	 * @param timeConstantList the time constant list
	 * @return the map
	 */
	private Map<String, Object> updateAnalysisDayWiseMap(Map<String, Object> dayWiseMap, List<String> timeConstantList) {
		Map<String, Object> map = new LinkedHashMap<>();
		for(String key : timeConstantList) {
			map.put(key, dayWiseMap.get(key));
		}
		return map;
	}

	/**
	 * Update analysis graph wrapper.
	 *
	 * @param graphWrapper the graph wrapper
	 * @param detailWrapper the detail wrapper
	 * @param key the key
	 */
	private void updateAnalysisGraphWrapper(WPTAnalysisGraphWrapper graphWrapper, TopURLDetailWrapper detailWrapper,
			String key) {
		updateSubMap(key, graphWrapper.getTtl(), detailWrapper.getDate(), detailWrapper.getAvgTTL());
		updateSubMap(key, graphWrapper.getFdns(), detailWrapper.getDate(), detailWrapper.getAvgFDNS());
		updateSubMap(key, graphWrapper.getTdns(), detailWrapper.getDate(), detailWrapper.getAvgTDNS());
		updateSubMap(key, graphWrapper.getTtfb(), detailWrapper.getDate(), detailWrapper.getAvgTTFB());
		updateSubMap(key, graphWrapper.getPing(), detailWrapper.getDate(), detailWrapper.getAvgPing());
		updateSubMap(key, graphWrapper.getTraceRoute(), detailWrapper.getDate(), detailWrapper.getHopCount());
	}
	
	/**
	 * Gets the filter list.
	 *
	 * @param geographyLevel the geography level
	 * @return the filter list
	 */
	@Override
	@Cacheable("WPTDashboardFilterCache")
	public Map<String, Object> getFilterList(String geographyLevel) {
		Map<String, Object> map = new HashMap<>();
		try {
			map.put(GEOGRAPHY, getGeographyList(geographyLevel));
			map.put(OPERATOR, getOperatorsList());
			map.put(LOCATION, updateLocationList(wptLocationDao.findAll()));
			map.put(CATEGORY, updateCategoryList(webCategoryDao.findAll()));
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER , e.getMessage());
		}
		return map;
	}

	/**
	 * Gets the geography list.
	 *
	 * @param geographyLevel the geography level
	 * @return the geography list
	 * @throws RestException the rest exception
	 */
	private List<GeographyWrapper> getGeographyList(String geographyLevel) {
		if(geographyLevel.equals(GEOGRAPHYL1)) {
			return geographyL1Service.getL1WithIdAndName();
		} else if(geographyLevel.equals(GEOGRAPHYL2)) {
			return geographyL2Service.getL2WithIdAndName();
		} else if(geographyLevel.equals(GEOGRAPHYL3)) {
			return geographyL3Service.getL3WithIdAndName();
		} else if(geographyLevel.equals(GEOGRAPHYL4)) {
			return geographyL4Service.getL4WithIdAndName();
		}
		return Collections.emptyList();
	}

	/**
	 * Gets the operators list.
	 *
	 * @return the operators list
	 */
	private List<WPTOperatorWrapper> getOperatorsList() {
		List<WPTOperatorWrapper> list = new ArrayList<>();
		try {
			List<String> operatorList = ConfigUtils.getStringList(
					WPT_ANALATICS_OPERATOR);
			logger.info("WPT Operator list : {}",new Gson().toJson(operatorList));
			if (CollectionUtils.isEmpty(operatorList)) {
				operatorList = new ArrayList<>();
				operatorList.add(ConfigUtils.getString(WPT_ANALATICS_OPERATOR));
			}
			for (String value : operatorList) {
				WPTOperatorWrapper wrapper = getWPTOperatorWrapper(value);
				list.add(wrapper);
			}
		} catch (Exception e) {
			logger.error("Exception in getOperatorsList {}",ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	/**
	 * Gets the WPT operator wrapper.
	 *
	 * @param value the value
	 * @return the WPT operator wrapper
	 */
	private WPTOperatorWrapper getWPTOperatorWrapper(String value) {
		WPTOperatorWrapper wrapper = new WPTOperatorWrapper();
		String operatorFromConfig = value.split(ForesightConstants.UNDERSCORE)[ForesightConstants.ZERO_INT];
		String technology = value.split(ForesightConstants.UNDERSCORE)[ForesightConstants.ONE_INT];
		wrapper.setOperator(operatorFromConfig);
		wrapper.setTechnology(technology);	
		return wrapper;
	}

	/**
	 * Save and get filters.
	 *
	 * @param filterJSON the filter JSON
	 * @return the string
	 */
	@Override
	public String saveAndGetFilters(String filterJSON) {
		try {
			if(filterJSON != null && !filterJSON.isEmpty()) {
				return saveFilterJSON(filterJSON);
			}
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return getSavedFilterJSON();
	}

	/**
	 * Gets the saved filter JSON.
	 *
	 * @return the saved filter JSON
	 */
	private String getSavedFilterJSON() {
		try {
			return userPreferenceService.getPreferenceByKey(UserContextServiceImpl.getUserInContext(), WPT_USER_FAVOURITE_FILTERS)
					.getValue();
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return BLANK_STRING;
		}
	}

	/**
	 * Save filter JSON.
	 *
	 * @param filterJSON the filter JSON
	 * @return the string
	 * @throws RestException the rest exception
	 */
	private String saveFilterJSON(String filterJSON) {
		UserPreference userPreference = new UserPreference();
		userPreference.setParam(WPT_USER_FAVOURITE_FILTERS);
		userPreference.setValue(filterJSON);
		userPreferenceService.savePreference(userPreference);
		return filterJSON;
	}
	
}
