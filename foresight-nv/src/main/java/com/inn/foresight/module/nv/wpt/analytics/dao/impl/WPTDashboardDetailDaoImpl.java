package com.inn.foresight.module.nv.wpt.analytics.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.lang.NumberUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;
import com.inn.foresight.module.nv.wpt.analytics.dao.IWPTDashboardDetailDao;
import com.inn.foresight.module.nv.wpt.analytics.model.WPTDashboardDetail;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.TopURLDetailWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDashboardKpiWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDayWiseCountWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTHTTPStatsWrapper;

/** The Class WPTDashboardDetailDaoImpl. */
@Repository("WPTDashboardDetailDaoImpl")
@Transactional
public class WPTDashboardDetailDaoImpl extends HibernateGenericDao<Integer, WPTDashboardDetail> implements IWPTDashboardDetailDao{

	/** The logger. */
	private Logger logger = LogManager.getLogger(WPTDashboardDetailDaoImpl.class);
	
	/** Construct WPTDashboardDetailDaoImpl object/instance. */
	public WPTDashboardDetailDaoImpl() {
		super(WPTDashboardDetail.class);
	}
	
	/**
	 * Enable filters.
	 *
	 * @param filters the filters
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void enableFilters(Map<String,List> filters){
		List<String> filterNames = filters.get(WPTAnalyticsConstants.FILTER_NAME);
		List<String> filterParams = filters.get(WPTAnalyticsConstants.FILTER_PARAM);
		List<Object> filterValues = filters.get(WPTAnalyticsConstants.FILTER_VALUE);
		for (int count = NumberUtils.INTEGER_ZERO; count < filterNames.size(); count++) {
			if (filterValues.get(count) instanceof List) {
				enableFilter(filterNames.get(count), filterParams.get(count), (List)filterValues.get(count));
			} else {
				enableFilter(filterNames.get(count), filterParams.get(count), filterValues.get(count));
			}
		}
	}
	
	/**
	 * Disable filters.
	 *
	 * @param filters the filters
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void disableFilters(Map<String,List> filters){
		List<String> filterNames = filters.get(WPTAnalyticsConstants.FILTER_NAME);
		for (int count = NumberUtils.INTEGER_ZERO; count < filterNames.size(); count++) {
			disableFilter(filterNames.get(count));
		}
	}
	
	/**
	 * Enable filter.
	 *
	 * @param filterName the filter name
	 * @param filterParam the filter param
	 * @param filterValue the filter value
	 */
	private void enableFilter(String filterName, String filterParam, List<String> filterValue) {
		Session s = (Session) getEntityManager().getDelegate();
		Filter filter = s.enableFilter(filterName);
		if(filterParam != null && filterValue != null) {
			filter.setParameterList(filterParam, filterValue);
		}
	}
	
	/**
	 * Enable filter.
	 *
	 * @param filterName the filter name
	 * @param filterParam the filter param
	 * @param filterValue the filter value
	 */
	private void enableFilter(String filterName, String filterParam, Object filterValue) {
		Session s = (Session) getEntityManager().getDelegate();
		Filter filter = s.enableFilter(filterName);
		if(filterParam != null && filterValue != null) {
			filter.setParameter(filterParam, filterValue);
		}
	}
	
	/**
	 * Disable filter.
	 *
	 * @param filterName the filter name
	 */
	private void disableFilter(String filterName) {
		Session s = (Session) getEntityManager().getDelegate();
		s.disableFilter(filterName);
	}

	/**
	 * Gets the target ip version count.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the target ip version count
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Long> getTargetIpVersionCount(String date, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology) {
		logger.info("Going to get Count for Ipv4 and Ipv6 for date {}, processType {} , filterMap {}, geographyId {}, operator {}, technology {}", date, processType, filterMap, geographyId, operator, technology);
		Map<String, Long> map = null;
		try {
		
			Query query = getEntityManager().createNamedQuery("getTargetIpVersionCount");
			Object[] values = {operator, technology, null, processType, date};
			setQueryParameters(query, values);
		
			enableFilters(filterMap);
			map = getResultForTargetIpVersion((List<Object[]>) query.getResultList());
			disableFilters(filterMap);
			
		} catch (NoResultException e) {
			logger.error(WPTAnalyticsConstants.NO_RESULT_EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return map;
	}
	
	/**
	 * Gets the result for target ip version.
	 *
	 * @param result the result
	 * @return the result for target ip version
	 */
	private Map<String, Long> getResultForTargetIpVersion(List<Object[]> results) {
		Map<String, Long> map = new HashMap<>();
		Object[] result = getMaxResult(results);
		logger.info("Getting Result {}", ArrayUtils.toString(result));
		Long ipv4Count = (Long) result[ForesightConstants.INDEX_ZERO];
		Long ipv6Count = (Long) result[ForesightConstants.INDEX_ONE];
		map.put(WPTAnalyticsConstants.IPV4, ipv4Count);
		map.put(WPTAnalyticsConstants.IPV6, ipv6Count);
		map.put(WPTAnalyticsConstants.TOTAL, (ipv4Count + ipv6Count));
		return map;
	}
	
	private Object[] getMaxResult(List<Object[]> results) {
		int maxIndex = ForesightConstants.INDEX_ZERO;
		int index = ForesightConstants.INDEX_ZERO;
		for(Object[] result : results) {
			logger.info("Getting Result {} at index {}", ArrayUtils.toString(result),index);
			Long currentCount = (Long) result[ForesightConstants.INDEX_TWO];
			Long maxCount = (Long) results.get(maxIndex)[ForesightConstants.INDEX_TWO];
			if(currentCount > maxCount) {
				maxIndex = index;
			}
			index++;
		}
		return results.get(maxIndex);
	}

	/**
	 * Gets the total record count.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the total record count
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<WPTDayWiseCountWrapper> getTotalRecordCount(List<String> timeConstantList, String processType,
			Map<String, List> filterMap, Integer geographyId, String operator, String technology) {
	
		logger.info("Going Day Wise Total Records Counts for timeConstantList {}, processType {} , filterMap {}, geographyId {},operator {}, technology {}", timeConstantList, processType, filterMap, geographyId, operator, technology);
		List<WPTDayWiseCountWrapper> list = null;
		
		try {			
			Query query = getEntityManager().createNamedQuery("getTotalRecordsCounts");
			Object[] values = {operator, technology, timeConstantList, processType, null};
			setQueryParameters(query, values);
		
			enableFilters(filterMap);
			list = query.getResultList();
			disableFilters(filterMap);
			
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
		return list;
	}
	
	/**
	 * Gets the total device count.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the total device count
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<WPTDayWiseCountWrapper> getTotalDeviceCount(List<String> timeConstantList, String processType,
			Map<String, List> filterMap, Integer geographyId, String operator, String technology) {
		logger.info("Going Day Wise Total Device Counts for timeConstantList {}, processType {} , filterName {}, geographyId {},operator {}, technology {}", timeConstantList, processType, filterMap, geographyId, operator, technology);
		List<WPTDayWiseCountWrapper> list = null;
		
		try {			
			Query query = getEntityManager().createNamedQuery("getTotalDeviceCounts");
			Object[] values = {operator, technology, timeConstantList, processType, null};
			setQueryParameters(query, values);
		
			enableFilters(filterMap);
			list = query.getResultList();
			disableFilters(filterMap);
			
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
		return list;
	}

	/**
	 * Gets the total url count.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the total url count
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<WPTDayWiseCountWrapper> getTotalUrlCount(List<String> timeConstantList, String processType,
			Map<String, List> filterMap, Integer geographyId, String operator, String technology) {
		logger.info("Going Day Wise Total URL Counts for timeConstantList {}, processType {} , filterMap {}, geographyId {}, operator {}, technology {}", timeConstantList, processType, filterMap, geographyId, operator, technology);
		List<WPTDayWiseCountWrapper> list = null;
		
		try {			
			Query query = getEntityManager().createNamedQuery("getTestedURLCounts");
			Object[] values = {operator, technology, timeConstantList, processType, null};
			setQueryParameters(query, values);
		
			enableFilters(filterMap);
			list = query.getResultList();
			disableFilters(filterMap);
			
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
		return list;
	}

	/**
	 * Sets the query parameters.
	 *
	 * @param query the query
	 * @param values the values
	 */
	private void setQueryParameters(Query query, Object[] values) {
		String[] parameters = { WPTAnalyticsConstants.OPERATOR, WPTAnalyticsConstants.TECHNOLOGY,
				WPTAnalyticsConstants.TIME_CONSTANT_LIST, WPTAnalyticsConstants.PROCESS_TYPE,
				WPTAnalyticsConstants.DATE, WPTAnalyticsConstants.CATEGORY, WPTAnalyticsConstants.LOCATION };
		for (int count = ForesightConstants.ZERO_INT; count < values.length; count++) {
			setParameterIntoQuery(query, parameters[count], values[count]);
		}
	}

	/**
	 * Sets the parameter into query.
	 *
	 * @param query the query
	 * @param param the param
	 * @param value the value
	 */
	private void setParameterIntoQuery(Query query, String param, Object value) {
		if(value != null) {
			query.setParameter(param, value);
		}
	}

	/**
	 * Gets the top tested URL counts.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the top tested URL counts
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<TopURLDetailWrapper> getTopTestedURLCounts(String date, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology) throws ValueNotFoundException {
		logger.info("Inside @method : getTopTestedURLCounts()");
		try {
			Query query = getEntityManager().createNamedQuery("getTopTestedURLCounts");
			Object[] values = {operator, technology, null, processType, date};
			setQueryParameters(query, values);
			query.setMaxResults(ForesightConstants.FOUR);
			
			enableFilters(filterMap);
			List<TopURLDetailWrapper> list =  query.getResultList();
			disableFilters(filterMap);
			
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	/**
	 * Gets the top ping counts.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the top ping counts
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<TopURLDetailWrapper> getTopPingCounts(String date, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology) throws ValueNotFoundException {
		logger.info("Inside @method : getTopPingCounts()");
		try {
			Query query = getEntityManager().createNamedQuery("getTopPingCounts");
			Object[] values = {operator, technology, null, processType, date};
			setQueryParameters(query, values);
			query.setMaxResults(ForesightConstants.FOUR);
			
			enableFilters(filterMap);
			List<TopURLDetailWrapper> list = query.getResultList();
			disableFilters(filterMap);

			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}
	
	/**
	 * Gets the top tested URL details.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the top tested URL details
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<TopURLDetailWrapper> getTopTestedURLDetails(String date, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology,Integer ulimit,Integer llimit) 
			throws ValueNotFoundException {
		logger.info("Inside @method : getTopTestedURLDetails()");
		try {
			Query query = getEntityManager().createNamedQuery("getTopTestedURLDetails");
			Object[] values = {operator, technology, null, processType, date};
			setQueryParameters(query, values);
		
			enableFilters(filterMap);
			setPaginationLimits(ulimit, llimit, query);
			List<TopURLDetailWrapper> list = query.getResultList();
			disableFilters(filterMap);
			
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
			return list;
		}  catch (Exception e) {
			throw new RestException(e);
		}
	}

	/**
	 * Sets the pagination limits.
	 *
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @param query the query
	 */
	private void setPaginationLimits(Integer ulimit, Integer llimit, Query query) {
		if (ulimit != null && llimit != null) {
			if (ulimit >= NumberUtils.INTEGER_ZERO) {
				query.setMaxResults(ulimit - llimit + NumberUtils.INTEGER_ONE);
			}

			if (llimit >= NumberUtils.INTEGER_ZERO) {
				query.setFirstResult(llimit);
			}
		}
	}
	
	/**
	 * Gets the top ping details.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the top ping details
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<TopURLDetailWrapper> getTopPingDetails(String date, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology,Integer ulimit,Integer llimit) 
			throws ValueNotFoundException {
		logger.info("Inside @method : getTopPingDetails()");
		try {
			Query query = getEntityManager().createNamedQuery("getTopPingDetails");
			Object[] values = {operator, technology, null, processType, date};
			setQueryParameters(query, values);
			
			enableFilters(filterMap);
			setPaginationLimits(ulimit, llimit, query);
			List<TopURLDetailWrapper> list = query.getResultList();
			disableFilters(filterMap);
			
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	/**
	 * Gets the avg TTFB detail.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the avg TTFB detail
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<WPTDashboardKpiWrapper> getAvgTTFBDetail(List<String> timeConstantList, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology) throws ValueNotFoundException {
		
		logger.info("Going to get Time to First Byte Count for timeConstantList {}, processType {}, filterMap {}, geographyId {}, "
				+ "operator {}, technology {}", timeConstantList, processType, filterMap, geographyId, operator, technology);
		
		try {
			Query query = getEntityManager().createNamedQuery("getAvgTTFBDetail");
			Object[] values = {operator, technology, timeConstantList, processType, null};
			setQueryParameters(query, values);
			
			enableFilters(filterMap);
			List<WPTDashboardKpiWrapper> list = query.getResultList();
			disableFilters(filterMap);
			
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	/**
	 * Gets the total tested URL count.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the total tested URL count
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Long getTotalTestedURLCount(String date, String processType, Map<String, List> filterMap, Integer geographyId,
			String operator, String technology) throws ValueNotFoundException {
		logger.info("Inside @method : getTotalTestedURLCount()");
		try {
			Query query = getEntityManager().createNamedQuery("getTotalTestedURLCount");
			Object[] values = {operator, technology, null, processType, date};
			setQueryParameters(query, values);
	
			enableFilters(filterMap);
			Long totalCount = (Long) query.getSingleResult();
			disableFilters(filterMap);
			
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, totalCount);
			return totalCount;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	/**
	 * Gets the total ping count.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the total ping count
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Long getTotalPingCount(String date, String processType, Map<String, List> filterMap, Integer geographyId,
			String operator, String technology) throws ValueNotFoundException {
		logger.info("Inside @method : getTotalPingCount()");
		try {
			Query query = getEntityManager().createNamedQuery("getTotalPingCount");
			Object[] values = {operator, technology, null, processType, date};
			setQueryParameters(query, values);
	
			enableFilters(filterMap);
			Long totalCount = (Long) query.getSingleResult();
			disableFilters(filterMap);
			
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, totalCount);
			return totalCount;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}
	
	/**
	 * Gets the HTTP and HTTPS count.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the HTTP and HTTPS count
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<WPTHTTPStatsWrapper> getHTTPAndHTTPSCount(List<String> timeConstantList, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology) {
		
		logger.info("Going to get Http and Https Count for timeConstantList {}, processType {}, filterMap {}, geographyId {}, "
				+ "operator {}, technology {}", timeConstantList, processType, filterMap, geographyId, operator, technology);
		
		try {
			Query query = getEntityManager().createNamedQuery("getHTTPAndHTTPSCount");
			Object[] values = {operator, technology, timeConstantList, processType, null};
			setQueryParameters(query, values);
		
			enableFilters(filterMap);
			List<WPTHTTPStatsWrapper> list = query.getResultList();
			disableFilters(filterMap);
			
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}
	
	/**
	 * Gets the analysis graph data.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param categories the categories
	 * @param locations the locations
	 * @param filters the filters
	 * @param technology the technology
	 * @return the analysis graph data
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<TopURLDetailWrapper> getAnalysisGraphData(List<String> timeConstantList, String processType,
			List<Object> categories, List<Object> locations, Map<String , List> filters, String technology) {
		logger.info("Inside @method : getAnalysisGraphData()");
		try {
			Query query = getEntityManager().createNamedQuery("getAnalysisData");
			Object[] values = {null, technology, timeConstantList, processType, null, categories, locations};
			setQueryParameters(query, values);
			enableFilters(filters);
			List<TopURLDetailWrapper> list = query.getResultList();
			disableFilters(filters);			
			return list;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}
	
	/**
	 * Gets the trace route and TTFB data.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param comparatorTechList the comparator tech list
	 * @param categoryIdList the category id list
	 * @param locationIdList the location id list
	 * @param technology the technology
	 * @param operator the operator
	 * @param geographyId the geography id
	 * @param filterMap the filter map
	 * @return the trace route and TTFB data
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<WPTDashboardKpiWrapper> getTraceRouteAndTTFBData(List<String> timeConstantList, String processType, 
			List<Object> comparatorTechList,
			List<Object> categoryIdList, List<Object> locationIdList, String technology, String operator,
			Integer geographyId, Map<String, List> filterMap) {
	
		logger.info("Going Trace Route And TTFB Data for timeConstantList {}, processType {} , comparatorTechList {}, categoryIdList {}, locationIdList {}, technology {}, operator {}, filterMap {}, geographyId {}", timeConstantList, processType, comparatorTechList, 
				categoryIdList, locationIdList, technology, operator, filterMap, geographyId);
		
		List<WPTDashboardKpiWrapper> list = null;
		
		try {			
			Query query = getEntityManager().createNamedQuery("getTraceRouteAndTTFBData");		
			Object[] queryParamArray = {null, technology, timeConstantList, processType, null, categoryIdList, locationIdList};
			setQueryParameters(query, queryParamArray);
			
			enableFilters(filterMap);
			list = query.getResultList();
			disableFilters(filterMap);
			
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		
		logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
		return list;
	}
	
	/**
	 * Gets the TTFB and TDNS data.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param comparatorTechList the comparator tech list
	 * @param categoryIdList the category id list
	 * @param locationIdList the location id list
	 * @param technology the technology
	 * @param operator the operator
	 * @param geographyId the geography id
	 * @param filterMap the filter map
	 * @return the TTFB and TDNS data
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<WPTDashboardKpiWrapper> getTTFBAndTDNSData(List<String> timeConstantList, String processType,
			List<Object> comparatorTechList, List<Object> categoryIdList, List<Object> locationIdList, String technology,
			String operator, Integer geographyId, Map<String, List> filterMap) {
		
		logger.info("Going TTFB and TDNS Resolution Data for timeConstantList {}, processType {} , comparatorTechList {}, categoryIdList {}, "
				+ "locationIdList {}, technology {}, operator {}, filterMap {}, geographyId {}", timeConstantList, processType, comparatorTechList, 
				categoryIdList, locationIdList, technology, operator, filterMap, geographyId);
		
		List<WPTDashboardKpiWrapper> list = null;
		
		try {			
			Query query = getEntityManager().createNamedQuery("getTTFBAndTDNSData");		
			Object[] queryParamArray = {null, technology, timeConstantList, processType, null, categoryIdList, locationIdList};
			setQueryParameters(query, queryParamArray);
			
			enableFilters(filterMap);
			list = query.getResultList();
			disableFilters(filterMap);
			
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
		return list;
	}
	
	/**
	 * Gets the TTFB category wise data.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param comparatorTechList the comparator tech list
	 * @param categoryIdList the category id list
	 * @param locationIdList the location id list
	 * @param technology the technology
	 * @param operator the operator
	 * @param geographyId the geography id
	 * @param filterMap the filter map
	 * @return the TTFB category wise data
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<WPTDashboardKpiWrapper> getTTFBCategoryWiseData(List<String> timeConstantList, String processType,
			List<Object> comparatorTechList, List<Object> categoryIdList, List<Object> locationIdList,
			String technology, String operator, Integer geographyId, Map<String, List> filterMap) {
		
		logger.info("Going Category Wise Data Min and Max TTFB for timeConstantList {}, processType {} , comparatorTechList {}, categoryIdList {}, "
				+ "locationIdList {}, technology {}, operator {}, filterMap {}, geographyId {}", timeConstantList, processType, comparatorTechList, 
				categoryIdList, locationIdList, technology, operator, filterMap, geographyId);
		
		List<WPTDashboardKpiWrapper> list = null;
		
		try {			
			Query query = getEntityManager().createNamedQuery("getTTFBCategoryWiseData");		
			Object[] queryParamArray = {null, technology, timeConstantList, processType, null, categoryIdList, locationIdList};
			setQueryParameters(query, queryParamArray);
			
			enableFilters(filterMap);
			list = query.getResultList();
			disableFilters(filterMap);
			
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		
		logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
		return list;
	}
	
}
