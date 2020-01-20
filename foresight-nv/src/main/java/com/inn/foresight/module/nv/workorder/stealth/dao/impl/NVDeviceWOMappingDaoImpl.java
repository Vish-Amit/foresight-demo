package com.inn.foresight.module.nv.workorder.stealth.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.inn.commons.lang.NumberUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.workorder.dao.INVDeviceWOMappingDao;
import com.inn.foresight.module.nv.workorder.stealth.model.NVDeviceWOMapping;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;

@Repository("NVDeviceWOMappingDaoImpl")
public class NVDeviceWOMappingDaoImpl extends HibernateGenericDao<Integer, NVDeviceWOMapping>  implements INVDeviceWOMappingDao{

	public NVDeviceWOMappingDaoImpl() {
		super(NVDeviceWOMapping.class);
	}

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVDeviceWOMappingDaoImpl.class);

	@Override
	public List<NVDeviceDataWrapper> getDeviceListForWorkOrder(Date woStartTime, Date woEndTime,
			Map<String,List> filters, Integer lLimit, Integer uLimit){
		logger.info("Going to getDeviceListForWO startTime : {} endtime : {} ", woStartTime, woEndTime);
		List<NVDeviceDataWrapper> deviceDatas = null;
		try {
			Query query = null;
			query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_LIST_FOR_WORK_ORDER);
			query.setParameter(DeviceConstant.WO_START_TIME, woStartTime);
			query.setParameter(DeviceConstant.WO_END_TIME, woEndTime);
			enableFilters(filters);
			setPaginationLimits(uLimit, lLimit, query);
			deviceDatas = query.getResultList();
			disableFilters(filters);
			logger.info("Done getDeviceListForWO size : {}", deviceDatas.size());
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return deviceDatas;
	}
	
	/**
	 * Enable filters.
	 *
	 * @param filters
	 *            the filters
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void enableFilters(Map<String, List> filters) {
		List<String> filterNames = filters.get(WPTAnalyticsConstants.FILTER_NAME);
		List<String> filterParams = filters.get(WPTAnalyticsConstants.FILTER_PARAM);
		List<Object> filterValues = filters.get(WPTAnalyticsConstants.FILTER_VALUE);
		int listSize = filterNames.size();
		for (int count = NumberUtils.INTEGER_ZERO; count < listSize; count++) {
			if (filterValues.get(count) instanceof List) {
				enableFilter(filterNames.get(count), filterParams.get(count), (List) filterValues.get(count));
			} else {
				enableFilter(filterNames.get(count), filterParams.get(count), filterValues.get(count));
			}
		}
	}

	/**
	 * Disable filters.
	 *
	 * @param filters
	 *            the filters
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void disableFilters(Map<String, List> filters) {
		List<String> filterNames = filters.get(WPTAnalyticsConstants.FILTER_NAME);
		int listSize = filterNames.size();
		for (int count = NumberUtils.INTEGER_ZERO; count < listSize; count++) {
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
	 * @param filterName
	 *            the filter name
	 */
	private void disableFilter(String filterName) {
		Session s = (Session) getEntityManager().getDelegate();
		s.disableFilter(filterName);
	}
	
	/**
	 * Sets the pagination limits.
	 *
	 * @param ulimit
	 *            the ulimit
	 * @param llimit
	 *            the llimit
	 * @param query
	 *            the query
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
	 * Gets the device list count for work order.
	 *
	 * @param date the date
	 * @param date2 the date 2
	 * @param filterAndValueMap the filter and value map
	 * @return the device list count for work order
	 * @throws DaoException the dao exception
	 */
	@Override
	public Long getDeviceListCountForWorkOrder(Date woStartTime, Date woEndTime, Map<String, List> filters){
		logger.info("Going to getDeviceListForWO startTime : {} endtime : {} ", woStartTime, woEndTime);
		Long deviceDataCount = null;
		try {
			Query query = null;
			query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_LIST_COUNT_FOR_WORK_ORDER);
			query.setParameter(DeviceConstant.WO_START_TIME, woStartTime);
			query.setParameter(DeviceConstant.WO_END_TIME, woEndTime);
			enableFilters(filters);
			deviceDataCount = (Long)query.getSingleResult();
			disableFilters(filters);
			logger.info("Done getDeviceListCountForWorkOrder : {}", deviceDataCount);
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return deviceDataCount;
	}
	

}
