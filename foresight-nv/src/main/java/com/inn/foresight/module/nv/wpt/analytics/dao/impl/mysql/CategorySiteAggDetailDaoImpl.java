package com.inn.foresight.module.nv.wpt.analytics.dao.impl.mysql;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;
import com.inn.foresight.module.nv.wpt.analytics.dao.ICategorySiteAggDetailDao;
import com.inn.foresight.module.nv.wpt.analytics.model.CategorySiteAggDetail;
import com.inn.foresight.module.nv.wpt.analytics.model.CategorySiteAggDetail.AggregationType;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDetailedViewWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTStatesWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTSummaryWrapper;

/** The Class CategorySiteAggDetailDaoImpl. */
@Repository("CategorySiteAggDetailDaoImpl")
public class CategorySiteAggDetailDaoImpl extends HibernateGenericDao<Integer, CategorySiteAggDetail> implements ICategorySiteAggDetailDao{

	/** The logger. */
	private Logger logger = LogManager.getLogger(CategorySiteAggDetailDaoImpl.class);
	
	/** Construct CategorySiteAggDetailDaoImpl object/instance. */
	public CategorySiteAggDetailDaoImpl() {
		super(CategorySiteAggDetail.class);
	}

	/**
	 * Gets the WPT pie chart data.
	 *
	 * @param geograhyL3 the geograhy L 3
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operator the operator
	 * @param network the network
	 * @param configuration the configuration
	 * @return the WPT pie chart data
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WPTStatesWrapper> getWPTPieChartData(String geograhyL3, String fromDate, String toDate, String operator,
			String network, String configuration) {
		logger.info("Inside @method getWPTPieChartData(), Getting Data for geograhyL3 {}, fromDate {}, toDate {}, operator {}, network {}, Configuration {}", geograhyL3, fromDate, toDate, operator, network, configuration);
		List<WPTStatesWrapper> list = null;
		try {				
			Query query = getEntityManager().createNamedQuery("getWPTPieChartData");
			setQueryParameters(query, fromDate, toDate, operator, network, configuration, AggregationType.CATEGORY);

			enableGeograhyL3Filters(geograhyL3);
			
			list = query.getResultList();
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
			
			disableGeograhyL3Filters(geograhyL3);
		} catch (NoResultException e) {
			logger.error(WPTAnalyticsConstants.NO_RESULT_EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw e;
		}  catch (BusinessException e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return list;
	}
	
	/**
	 * Gets the WPT pie chart stats.
	 *
	 * @param geograhyL3 the geograhy L 3
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operator the operator
	 * @param network the network
	 * @param configuration the configuration
	 * @return the WPT pie chart stats
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WPTStatesWrapper> getWPTPieChartStats(String geograhyL3, String fromDate, String toDate, String operator,
			String network, String configuration) {
		logger.info("Inside @method getWPTPieChartStats(), Getting Data for geograhyL3 {}, fromDate {}, toDate {}, operator {}, network {},configuration {}", geograhyL3, fromDate, toDate, operator, network, configuration);
		List<WPTStatesWrapper> list = null;
		try {
			Query query = getEntityManager().createNamedQuery("getWPTPieChartStats");
			setQueryParameters(query, fromDate, toDate, operator, network, configuration, AggregationType.CATEGORY);

			enableGeograhyL3Filters(geograhyL3);			
			
			list = query.getResultList();
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
			
			disableGeograhyL3Filters(geograhyL3);
		} catch (NoResultException e) {
			logger.error(WPTAnalyticsConstants.NO_RESULT_EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw e;
		}  catch (BusinessException e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return list;
	}
	
	/**
	 * Gets the WPT histogram data.
	 *
	 * @param geograhyL3 the geograhy L 3
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operator the operator
	 * @param network the network
	 * @param configuration the configuration
	 * @return the WPT histogram data
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WPTStatesWrapper> getWPTHistogramData(String geograhyL3, String fromDate, String toDate, String operator,
			String network, String configuration){
		logger.info("Inside @method getWPTHistogramData(), Getting Data for geograhyL3 {}, fromDate {}, toDate {}, operator {}, network {},configuration {}", geograhyL3, fromDate, toDate, operator, network, configuration);
		List<WPTStatesWrapper> list = null;
		try {
			Query query = getEntityManager().createNamedQuery("getWPTHistogramData");			
			setQueryParameters(query, fromDate, toDate, operator, network, configuration, AggregationType.CATEGORY);		
			
			enableGeograhyL3Filters(geograhyL3);

			list = query.getResultList();
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
			
			disableGeograhyL3Filters(geograhyL3);
		} catch (NoResultException e) {
			logger.error(WPTAnalyticsConstants.NO_RESULT_EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw e;
		}  catch (BusinessException e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return list;
	}
	
	/**
	 * Gets the WPT scatter plot data.
	 *
	 * @param geograhyL3 the geograhy L 3
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operator the operator
	 * @param network the network
	 * @param configuration the configuration
	 * @return the WPT scatter plot data
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WPTStatesWrapper> getWPTScatterPlotData(String geograhyL3, String fromDate, String toDate, String operator,
			String network, String configuration){
		logger.info("Inside @method getWPTScatterPlotData, Getting Data for geograhyL3 {}, fromDate {}, toDate {}, operator {}, network {},configuration {}", geograhyL3, fromDate, toDate, operator, network, configuration);
		List<WPTStatesWrapper> list = null;
		try {
			Query query = getEntityManager().createNamedQuery("getWPTScatterPlotData");
			
			setQueryParameters(query, fromDate, toDate, operator, network, configuration, AggregationType.CATEGORY);	
			
			enableGeograhyL3Filters(geograhyL3);

			list = query.getResultList();
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
			
			disableGeograhyL3Filters(geograhyL3);
		} catch (NoResultException e) {
			logger.error(WPTAnalyticsConstants.NO_RESULT_EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw e;
		}  catch (BusinessException e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return list;
	}
	
	/**
	 * Disable geograhy L 3 filters.
	 *
	 * @param geograhyL3 the geograhy L 3
	 */
	private void disableGeograhyL3Filters(String geograhyL3) {
		if(geograhyL3.equalsIgnoreCase(WPTAnalyticsConstants.ALL)) {
			disableAllGeograhyL3Filter();
		}else {
			disableGeograhyL3Filter();
		}
	}

	/**
	 * Enable geograhy L 3 filters.
	 *
	 * @param geograhyL3 the geograhy L 3
	 */
	private void enableGeograhyL3Filters(String geograhyL3) {
		if(geograhyL3.equalsIgnoreCase(WPTAnalyticsConstants.ALL)) {
			enableGeograhyL3Filter();
		}else {
			enableGeograhyL3Filter(geograhyL3);
		}
	}
	
	/**
	 * Enable geograhy L 3 filter.
	 *
	 * @param geograhyL3 the geograhy L 3
	 */
	private void enableGeograhyL3Filter(String geograhyL3) {
		Session s = (Session) getEntityManager().getDelegate();
		Filter filter = s.enableFilter("getDataForGivenGeographyL3");
		filter.setParameter(WPTAnalyticsConstants.GEOGRAPHYL3, geograhyL3);
	}

	/** Disable geograhy L 3 filter. */
	private void disableGeograhyL3Filter() {
		Session s = (Session) getEntityManager().getDelegate();
		s.disableFilter("getDataForGivenGeographyL3");
	}

	/** Enable geograhy L 3 filter. */
	private void enableGeograhyL3Filter() {
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter("getDataForALLGeographyL3");
	}

	/** Disable all geograhy L 3 filter. */
	private void disableAllGeograhyL3Filter() {
		Session s = (Session) getEntityManager().getDelegate();
		s.disableFilter("getDataForALLGeographyL3");
	}
	
	/**
	 * Sets the query parameters.
	 *
	 * @param query the query
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operator the operator
	 * @param network the network
	 * @param configuration the configuration
	 * @param identifier the identifier
	 */
	private void setQueryParameters(Query query, String fromDate, String toDate, String operator, String network,
			String configuration, AggregationType identifier) {
		query.setParameter(WPTAnalyticsConstants.FROMDATE, fromDate)
			.setParameter(WPTAnalyticsConstants.TODATE, toDate)
			.setParameter(WPTAnalyticsConstants.OPERATOR, operator)
			.setParameter(WPTAnalyticsConstants.NETWORK, network)
			.setParameter(WPTAnalyticsConstants.CONFIGURATION, configuration)
			.setParameter(WPTAnalyticsConstants.IDENTIFIER, identifier);
	}
	
	/**
	 * Gets the WPT detailed view data category wise.
	 *
	 * @param geographyL3 the geography L 3
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operator the operator
	 * @param network the network
	 * @param configuration the configuration
	 * @return the WPT detailed view data category wise
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WPTDetailedViewWrapper> getWPTDetailedViewDataCategoryWise(String geographyL3, String fromDate, String toDate,
			String operator, String network, String configuration) {
		logger.info("Inside @method getWPTDetailedViewData(), Getting Data for geographyL3 {}, fromDate {}, toDate {}, operator {}, network {}, "
				+ "configuration {}", geographyL3, fromDate, toDate, operator, network, configuration);
		List<WPTDetailedViewWrapper> list = null;
		try {
			enableGeograhyL3Filters(geographyL3);
			
			Query query = getEntityManager().createNamedQuery("getWPTDetailedViewDataCategoryWise");
			setQueryParameters(query,fromDate,toDate,operator,network,configuration,AggregationType.CATEGORY);
			
			list = query.getResultList();
			disableGeograhyL3Filters(geographyL3);
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
		} catch (NoResultException e) {
			logger.error(WPTAnalyticsConstants.NO_RESULT_EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw e;
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return list;
	}
	
	/**
	 * Gets the WPT detailed view data site wise.
	 *
	 * @param geographyL3 the geography L 3
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operator the operator
	 * @param network the network
	 * @param configuration the configuration
	 * @param lowerLimit the lower limit
	 * @param upperLimit the upper limit
	 * @return the WPT detailed view data site wise
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WPTDetailedViewWrapper> getWPTDetailedViewDataSiteWise(String geographyL3, String fromDate, String toDate,
			String operator, String network, String configuration, int lowerLimit, int upperLimit) {
		logger.info("Inside @method getWPTDetailedViewData(), Getting Data for geographyL3 {}, fromDate {}, toDate {}, operator {}, network {}, "
				+ "configuration {}, ulimit {}, llimit {}", geographyL3, fromDate, toDate, operator, network, configuration,
				 upperLimit, lowerLimit);
		List<WPTDetailedViewWrapper> list = null;
		try {
			enableGeograhyL3Filters(geographyL3);
			Query query = getEntityManager().createNamedQuery("getWPTDetailedViewDataSiteWise");
			setQueryParameters(query, fromDate, toDate, operator, network, configuration,AggregationType.SITE);
			if (upperLimit >= 0) {
				query.setMaxResults(upperLimit - lowerLimit + 1);
			}

			if (lowerLimit >= 0) {
				query.setFirstResult(lowerLimit);
			}
			list = query.getResultList();
			disableGeograhyL3Filters(geographyL3);
			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, list.size());
		} catch (NoResultException e) {
			logger.error(WPTAnalyticsConstants.NO_RESULT_EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw e;
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return list;
	}
	
	/**
	 * Gets the WPT summary wrappers.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param site the site
	 * @param lowerLimit the lower limit
	 * @param upperLimit the upper limit
	 * @return the WPT summary wrappers
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WPTSummaryWrapper> getWPTSummaryWrappers(String fromDate, String toDate,
			String site, int lowerLimit, int upperLimit) {
		List<WPTSummaryWrapper> list = null;
		try {
			boolean isEnabled = enableSiteFilter(site.toUpperCase());
			logger.info("Site Filter Status : {}",isEnabled);
			Query query = getEntityManager().createNamedQuery("getWPTSummaryData")
					.setParameter(WPTAnalyticsConstants.FROMDATE, fromDate)
					.setParameter(WPTAnalyticsConstants.TODATE, toDate)
					.setParameter(WPTAnalyticsConstants.IDENTIFIER, AggregationType.SITE);
			if (upperLimit >= 0) {
				query.setMaxResults(upperLimit - lowerLimit + 1);
			}

			if (lowerLimit >= 0) {
				query.setFirstResult(lowerLimit);
			}
			list = setWPTSummaryWrapper((List<Object[]>) query.getResultList());
			disableSiteFilter(isEnabled);
		} catch (NoResultException e) {
			logger.error(WPTAnalyticsConstants.NO_RESULT_EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	/**
	 * Sets the WPT summary wrapper.
	 *
	 * @param resultList the result list
	 * @return the list
	 */
	private List<WPTSummaryWrapper> setWPTSummaryWrapper(List<Object[]> resultList) {
		List<WPTSummaryWrapper> wrappers = new ArrayList<>();
		for (Object[] result : resultList) {
			WPTSummaryWrapper wrapper = new WPTSummaryWrapper();
			wrapper.setCaptureTime((Date) result[ForesightConstants.INDEX_ZERO]);
			wrapper.setSite((String) result[ForesightConstants.INDEX_ONE]);
			Double avgLoadTime = (Double) result[ForesightConstants.INDEX_TWO];
			Double successRate = (Double) result[ForesightConstants.INDEX_THREE];
			wrapper.setIpv((String) result[ForesightConstants.INDEX_FOUR]);
			switch (wrapper.getIpv()) {
				case WPTAnalyticsConstants.IPV4 :
					wrapper.setIpv4LoadTime(avgLoadTime);
					wrapper.setIpv4SuccessRate(successRate);
					break;
				case WPTAnalyticsConstants.IPV6 :
					wrapper.setIpv6LoadTime(avgLoadTime);
					wrapper.setIpv6SuccessRate(successRate);
					break;
				default :
					break;
			}
			wrappers.add(wrapper);
		}
		return wrappers;
	}
	
	/**
	 * Enable site filter.
	 *
	 * @param site the site
	 * @return true, if successful
	 */
	private boolean enableSiteFilter(String site) {
		boolean isEnabled = false;
		if(StringUtils.isNotEmpty(site)) {
		Session s = (Session) getEntityManager().getDelegate();
		Filter filter = s.enableFilter("getDataForGivenSite");
		filter.setParameter(WPTAnalyticsConstants.SITE, site);
		isEnabled = true;
		}
		return isEnabled;
	}

	/**
	 * Disable site filter.
	 *
	 * @param isEnabled the is enabled
	 */
	private void disableSiteFilter(boolean isEnabled) {
		if (isEnabled) {
			Session s = (Session) getEntityManager().getDelegate();
			s.disableFilter("getDataForGivenSite");
		}
	}
	
	
	/**
	 * Gets the location based on cell id mnc.
	 *
	 * @return the location based on cell id mnc
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getLocationBasedOnCellIdMnc() {

		Map<String, String> cellIdMncMap = null;
		String cellId;
		String mnc;
		String geography = null;

		try {
			Query query = getEntityManager().createNativeQuery("");
			List<Object[]> resultList = query.getResultList();
			if (resultList != null) {
				cellIdMncMap = new HashMap<>();

				for (Object[] cellIdMnc : resultList) {
					Object cellIdValue = cellIdMnc[0];
					Object mncValue = cellIdMnc[1];

					try {
						cellId = String.valueOf(cellIdValue);
					} catch (Exception e) {
						continue;
					}
					try {
						mnc = String.valueOf(mncValue);
					} catch (Exception e) {
						continue;
					}

					if (cellId != null && mnc != null) {
						cellIdMncMap.put(cellId + "_" + mnc, geography);
					}

				}

			}

		} catch (Exception e) {
			logger.error("Error in getting geography : {}",Utils.getStackTrace(e));
		}
		return cellIdMncMap;

	}
	
}
