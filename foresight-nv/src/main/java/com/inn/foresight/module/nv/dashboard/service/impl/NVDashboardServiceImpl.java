package com.inn.foresight.module.nv.dashboard.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.lang.MapUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.model.OperatorDetail;
import com.inn.foresight.core.generic.service.IOperatorDetailService;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IAdvanceSearchDao;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.module.nv.dashboard.dao.INVDashboardDao;
import com.inn.foresight.module.nv.dashboard.model.NVDashboard;
import com.inn.foresight.module.nv.dashboard.service.INVDashboardService;
import com.inn.foresight.module.nv.dashboard.utils.NVDashboardConstants;
import com.inn.foresight.module.nv.dashboard.utils.NvDashboardUtils;
import com.inn.foresight.module.nv.dashboard.wrapper.NVDashboardWrapper;
import com.inn.foresight.module.nv.dashboard.wrapper.NVDistributionWrapper;
import com.inn.foresight.module.nv.dashboard.wrapper.TopGeographyWrapper;
import com.inn.foresight.module.nv.dashboard.wrapper.UserCountWrapper;

/**
 * The Class NVDashboardServiceImpl.
 */
@Service("NVDashboardServiceImpl")
@Transactional
public class NVDashboardServiceImpl extends AbstractService<Integer, NVDashboard> implements INVDashboardService {

    /**
     * The logger.
     */
    private Logger logger = LogManager.getLogger(NVDashboardServiceImpl.class);

    /**
     * The inv dashboard dao.
     */
    @Autowired
    private INVDashboardDao invDashboardDao;

    /**
     * The advance search dao.
     */
    @Autowired
    IAdvanceSearchDao advanceSearchDao;

    @Autowired
    private IOperatorDetailService operatorService;

    /**
     * Sets the dao.
     *
     * @param dao the new dao
     */
    @Autowired
    public void setDao(INVDashboardDao dao) {
        super.setDao(dao);
        invDashboardDao = dao;
    }

    /***
     * NVDashboardData for Chart
     * Methods******************************************.
     *
     * @param advanceSearchId
     *            the advance search id
     * @param stringEndDate
     *            the string end date
     * @param callType
     *            the call type
     * @param band
     *            the band
     * @param technology
     *            the technology
     * @param operator
     *            the operator
     * @param kpiList
     *            the kpi list
     * @param type
     *            the type
     * @param name
     *            the name
     * @return the nv dashboard data by date and location
     * @throws RestException
     *             the rest exception
     */

    @Override
    @Cacheable(value = "NvDashboardDataCache", condition = "#result!= null")
    public Map<String, Map<String, String>> getNvDashboardDataByDateAndLocation(Integer advanceSearchId,
                                                                                String stringEndDate, String callType, String band, String technology, String operator,
                                                                                List<String> kpiList, String type, String name, String country) {
        try {
            Date endDate = NvDashboardUtils.getDateFromString(stringEndDate);
            return getNVDashboardData(advanceSearchId, endDate, callType, band, technology, operator, kpiList, type,
                    name,country);
        } catch (Exception e) {
            throw new RestException(e.getMessage());
        }
    }

    @Override
    public Map<String, Map<String, String>> getNVDashboardDataForTribe(Integer advanceSearchId,
                                                               String stringEndDate, String callType, String band, String technology, String operator,
                                                               List<String> kpiList, String type, String name, String  country) {
        try {
            Date endDate = NvDashboardUtils.getDateFromString(stringEndDate);
            return getNVDashboardData(advanceSearchId, endDate, callType, band, technology, operator, kpiList, type,
                    name, country);
        } catch (Exception e) {
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Gets the NV dashboard data.
     *
     * @param advancedSearchId the advanced search id
     * @param endDate          the end date
     * @param callType         the call type
     * @param band             the band
     * @param technology       the technology
     * @param operator         the operator
     * @param kpiList          the kpi list
     * @param type             the type
     * @param name             the name
     * @return the NV dashboard data
     * @throws RestException the rest exception
     */
    @Override
    public Map<String, Map<String, String>> getNVDashboardData(Integer advancedSearchId, Date endDate, String callType,
                                                               String band, String technology, String operator, List<String> kpiList, String type, String name,String country) {
        List<NVDashboard> dashboardAggregatedDataList = new ArrayList<>();
        logger.info("In getNVDashboardData method for {}", callType);
        try {
            String[] geographyNameAndType = null;
            String geographyType = null;
            String geographyName = null;

            if (advancedSearchId != null) {
                geographyNameAndType = getGeographyNameAndType(advancedSearchId);
                geographyType = geographyNameAndType[ForesightConstants.ZERO_VALUE];
                geographyName = geographyNameAndType[ForesightConstants.ONE];
            }

            if (NVDashboardConstants.DAY.equalsIgnoreCase(callType)) {
                Date startDate = DateUtils.addDays(endDate, NVDashboardConstants.MINUSSIX);
                dashboardAggregatedDataList = getNVDashboardDataByDate(geographyType, geographyName, startDate, endDate,
                        band, technology, operator, type, name,country);
            } else if (NVDashboardConstants.WEEK.equalsIgnoreCase(callType)) {
                dashboardAggregatedDataList = getNVDashboardDataByWeek(geographyType, geographyName, endDate, band,
                        technology, operator, type, name, country);
            } else if (NVDashboardConstants.MONTH.equalsIgnoreCase(callType)) {
                dashboardAggregatedDataList = getNVDashboardDataByMonth(geographyType, geographyName, endDate, band,
                        technology, operator, type, name,country);
            }
            logger.info("NVDashboard data result found of size {} ", dashboardAggregatedDataList.size());
            return getNvDashbordDataMap(callType, endDate, dashboardAggregatedDataList, kpiList);
        } catch (Exception e) {
            logger.error("Exception in NVDashboard Data : {}", Utils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Gets the nv dashbord data map.
     *
     * @param callType                    the call type
     * @param endDate                     the end date
     * @param dashboardAggregatedDataList the dashboard aggregated data list
     * @param kpiList                     the kpi list
     * @return the nv dashbord data map
     */
    private Map<String, Map<String, String>> getNvDashbordDataMap(String callType, Date endDate,
                                                                  List<NVDashboard> dashboardAggregatedDataList, List<String> kpiList) {

        List<String> dateKeyList = NvDashboardUtils.getDateKeyListByCallType(callType, endDate);
        Map<String, NVDashboard> dateDataMap = getMapFromList(dashboardAggregatedDataList, callType);
        Map<String, Map<String, String>> dashboardValueMap = new HashMap<>();
        int counter = ForesightConstants.SEVEN;

        for (String key : dateKeyList) {
            NVDashboard dashboard = new NVDashboard();
            if (dateDataMap.containsKey(key)) {
                dashboard = dateDataMap.get(key);
            }
            if (kpiList.isEmpty())
                setAllKpiValuesForDashboardData(dashboard, dashboardValueMap, counter);
            else {
                setValuesForKpiListForDashboardData(dashboard, dashboardValueMap, counter, kpiList);
            }
            counter--;
        }
        if (MapUtils.isNotEmpty(dashboardValueMap)) {
            return dashboardValueMap;
        } else {
            return null;
        }
    }

    /**
     * Gets the NV dashboard data by date.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param startDate     the start date
     * @param endDate       the end date
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @param type          the type
     * @param name          the name
     * @return the NV dashboard data by date
     * @throws DaoException the dao exception
     */

    @Override
    public List<NVDashboard> getNVDashboardDataByDate(String geographyType, String geographyName, Date startDate,
                                                      Date endDate, String band, String technology, String operator, String type, String name, String country) {
        return invDashboardDao.getNVDashboardDataByDate(geographyType, geographyName, startDate, endDate, band,
                technology, operator, type, name, country);
    }

    @Override
    public List<NVDashboard> getNVDashboardDataByDateForReport(String geographyType, String geographyName,
                                                               Date startDate, Date endDate, String band, String technology, String operator, String type, String name, String country) {
        return invDashboardDao.getNVDashboardDataByDateForReport(geographyType, geographyName, startDate, endDate, band,
                technology, operator, type, name, country);
    }

    /**
     * Gets the NV dashboard data by week.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param endDate       the end date
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @param type          the type
     * @param name          the name
     * @return the NV dashboard data by week
     * @throws DaoException the dao exception
     */
    private List<NVDashboard> getNVDashboardDataByWeek(String geographyType, String geographyName, Date endDate,
                                                       String band, String technology, String operator, String type, String name, String country) {
        Integer[] firstAndLastWeekByDate = NvDashboardUtils.getFirstAndLastWeekByDate(endDate,
                NVDashboardConstants.MINUSSIX);
        Integer firstWeek = firstAndLastWeekByDate[ForesightConstants.ZERO_VALUE];
        Integer lastWeek = firstAndLastWeekByDate[ForesightConstants.ONE];
        return invDashboardDao.getNVDashboardDataByWeek(geographyType, geographyName, firstWeek, lastWeek, band,
                technology, operator, type, name,country);
    }

    /**
     * Gets the NV dashboard data by month.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param endDate       the end date
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @param type          the type
     * @param name          the name
     * @return the NV dashboard data by month
     * @throws DaoException the dao exception
     */
    private List<NVDashboard> getNVDashboardDataByMonth(String geographyType, String geographyName, Date endDate,
                                                        String band, String technology, String operator, String type, String name, String country) {
        Integer[] firstAndLastMonthByDate = NvDashboardUtils.getFirstAndLastMonthByDate(endDate,
                NVDashboardConstants.MINUSSIX);
        Integer firstMonth = firstAndLastMonthByDate[ForesightConstants.ZERO_VALUE];
        Integer lastMonth = firstAndLastMonthByDate[ForesightConstants.ONE];
        return invDashboardDao.getNVDashboardDataByMonth(geographyType, geographyName, firstMonth, lastMonth, band,
                technology, operator, type, name, country);
    }

    /***
     * User Count Methods******************************************.
     *
     * @param advancedSearchId
     *            the advanced search id
     * @param strEndDate
     *            the str end date
     * @param callType
     *            the call type
     * @param band
     *            the band
     * @param technology
     *            the technology
     * @param operator
     *            the operator
     * @return the nv dashboard user count by date and location
     * @throws RestException
     *             the rest exception
     */

    @Override
    public Map<String, UserCountWrapper> getNvDashboardUserCountByDateAndLocation(Integer advancedSearchId,
                                                                                  String strEndDate, String callType, String band, String technology, String operator, String country) {
        try {
            Date endDate = NvDashboardUtils.getDateFromString(strEndDate);
            return getUserCountData(advancedSearchId, endDate, callType, band, technology, operator, country);
        } catch (Exception e) {
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Gets the user count data.
     *
     * @param advancedSearchId the advanced search id
     * @param endDate          the end date
     * @param callType         the call type
     * @param band             the band
     * @param technology       the technology
     * @param operator         the operator
     * @return the user count data
     */
    @Override
    public Map<String, UserCountWrapper> getUserCountData(Integer advancedSearchId, Date endDate, String callType,
                                                          String band, String technology, String operator, String country) {

        NVDashboardWrapper dashboardUCWrapper = null;

        try {
            String[] geographyNameAndType = null;
            String geographyType = null;
            String geographyName = null;

            if (advancedSearchId != null) {
                geographyNameAndType = getGeographyNameAndType(advancedSearchId);
                geographyType = geographyNameAndType[ForesightConstants.ZERO_VALUE];
                geographyName = geographyNameAndType[ForesightConstants.ONE];
            }

            if (NVDashboardConstants.DAY.equalsIgnoreCase(callType)) {
                dashboardUCWrapper = getUserCountDataByDate(geographyType, geographyName, endDate, band, technology,
                        operator, country);
            } else if (NVDashboardConstants.WEEK.equalsIgnoreCase(callType)) {
                dashboardUCWrapper = getUserCountDataByWeek(geographyType, geographyName, endDate, band, technology,
                        operator, country);
            } else if (NVDashboardConstants.MONTH.equalsIgnoreCase(callType)) {
                dashboardUCWrapper = getUserCountDataByMonth(geographyType, geographyName, endDate, band, technology,
                        operator, country);
            }

        } catch (Exception e) {
            logger.error("Error in getUserCountData : {}", Utils.getStackTrace(e));
        }
        return getUserCountMapFromWrapper(dashboardUCWrapper);
    }

    /**
     * Gets the user count data by date.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param endDate       the end date
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @return the user count data by date
     * @throws DaoException the dao exception
     */
    private NVDashboardWrapper getUserCountDataByDate(String geographyType, String geographyName, Date endDate,
                                                      String band, String technology, String operator, String country) {
        return invDashboardDao.getUserCountDataByDate(geographyType, geographyName, endDate, band, technology,
                operator, country);
    }

    /**
     * Gets the user count data by week.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param endDate       the end date
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @return the user count data by week
     * @throws DaoException the dao exception
     */
    private NVDashboardWrapper getUserCountDataByWeek(String geographyType, String geographyName, Date endDate,
                                                      String band, String technology, String operator, String country) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        Integer lastWeek = Integer.parseInt(NvDashboardUtils.getWeekAndYearByDate(cal));
        return invDashboardDao.getUserCountDataByWeek(geographyType, geographyName, lastWeek, band, technology,
                operator, country);
    }

    /**
     * Gets the user count data by month.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param endDate       the end date
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @return the user count data by month
     * @throws DaoException the dao exception
     */
    private NVDashboardWrapper getUserCountDataByMonth(String geographyType, String geographyName, Date endDate,
                                                       String band, String technology, String operator, String country) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        Integer lastMonth = Integer.parseInt(NvDashboardUtils.getMonthAndYearByDate(cal));
        return invDashboardDao.getUserCountDataByMonth(geographyType, geographyName, lastMonth, band, technology,
                operator, country);
    }

    /**
     * Gets the user count map from wrapper.
     *
     * @param dashboardUCWrapper the dashboard UC wrapper
     * @return the user count map from wrapper
     */
    private Map<String, UserCountWrapper> getUserCountMapFromWrapper(NVDashboardWrapper dashboardUCWrapper) {
        Map<String, UserCountWrapper> userCountMap = new HashMap<>();
        userCountMap.put(NVDashboardConstants.ANDROID_IOS_UC, getAndroidiOSUC(dashboardUCWrapper));
        userCountMap.put(NVDashboardConstants.ACTIVE_PASSIVE_UC, getActivePassiveUC(dashboardUCWrapper));
        userCountMap.put(NVDashboardConstants.CONS_ENTER_UC, getCosumerEnterpriseUC(dashboardUCWrapper));

        if (MapUtils.isNotEmpty(userCountMap)) {
            return userCountMap;
        } else {
            return null;
        }
    }

    /**
     * Gets the cosumer enterprise UC.
     *
     * @param dashboardUCWrapper the dashboard UC wrapper
     * @return the cosumer enterprise UC
     */
    private UserCountWrapper getCosumerEnterpriseUC(NVDashboardWrapper dashboardUCWrapper) {
        UserCountWrapper consumerEnterpriseUC = new UserCountWrapper();
        if (dashboardUCWrapper != null) {
            consumerEnterpriseUC.setTotalUser(dashboardUCWrapper.getTotalUC());
            consumerEnterpriseUC.setUser1(dashboardUCWrapper.getEnterpriseUC());
            consumerEnterpriseUC.setUser1Percent(NvDashboardUtils.getPercentage(
                    dashboardUCWrapper.getEnterpriseUC() * NVDashboardConstants.ONE_DOUBLE,
                    dashboardUCWrapper.getTotalUC() * NVDashboardConstants.ONE_DOUBLE));
            consumerEnterpriseUC.setUser2(dashboardUCWrapper.getConsumerUC());
            consumerEnterpriseUC.setUser2Percent(
                    NvDashboardUtils.getPercentage(dashboardUCWrapper.getConsumerUC() * NVDashboardConstants.ONE_DOUBLE,
                            dashboardUCWrapper.getTotalUC() * NVDashboardConstants.ONE_DOUBLE));
        }
        return consumerEnterpriseUC;
    }

    /**
     * Gets the active passive UC.
     *
     * @param dashboardUCWrapper the dashboard UC wrapper
     * @return the active passive UC
     */
    private UserCountWrapper getActivePassiveUC(NVDashboardWrapper dashboardUCWrapper) {
        UserCountWrapper activePassiveUC = new UserCountWrapper();
        if (dashboardUCWrapper != null) {
            activePassiveUC.setTotalUser(dashboardUCWrapper.getTotalUC());
            activePassiveUC.setUser1(dashboardUCWrapper.getActiveUC());
            activePassiveUC.setUser1Percent(
                    NvDashboardUtils.getPercentage(dashboardUCWrapper.getActiveUC() * NVDashboardConstants.ONE_DOUBLE,
                            dashboardUCWrapper.getTotalUC() * NVDashboardConstants.ONE_DOUBLE));
            activePassiveUC.setUser2(dashboardUCWrapper.getPassiveUC());
            activePassiveUC.setUser2Percent(
                    NvDashboardUtils.getPercentage(dashboardUCWrapper.getPassiveUC() * NVDashboardConstants.ONE_DOUBLE,
                            dashboardUCWrapper.getTotalUC() * NVDashboardConstants.ONE_DOUBLE));
        }
        return activePassiveUC;
    }

    /**
     * Gets the androidi OSUC.
     *
     * @param dashboardUCWrapper the dashboard UC wrapper
     * @return the androidi OSUC
     */
    private UserCountWrapper getAndroidiOSUC(NVDashboardWrapper dashboardUCWrapper) {
        UserCountWrapper osWiseWrapper = new UserCountWrapper();
        if (dashboardUCWrapper != null) {
            osWiseWrapper.setTotalUser(dashboardUCWrapper.getTotalUC());
            osWiseWrapper.setUser1(dashboardUCWrapper.getIosUC());
            osWiseWrapper.setUser1Percent(
                    NvDashboardUtils.getPercentage(dashboardUCWrapper.getIosUC() * NVDashboardConstants.ONE_DOUBLE,
                            dashboardUCWrapper.getTotalUC() * NVDashboardConstants.ONE_DOUBLE));
            osWiseWrapper.setUser2(dashboardUCWrapper.getAndroidUC());
            osWiseWrapper.setUser2Percent(
                    NvDashboardUtils.getPercentage(dashboardUCWrapper.getAndroidUC() * NVDashboardConstants.ONE_DOUBLE,
                            dashboardUCWrapper.getTotalUC() * NVDashboardConstants.ONE_DOUBLE));
        }
        return osWiseWrapper;
    }

    /***
     * User Count Methods End******************************************.
     *
     * @param advancedSearchId
     *            the advanced search id
     * @param callType
     *            the call type
     * @param band
     *            the band
     * @param operator
     *            the operator
     * @param technology
     *            the technology
     * @param kpi
     *            the kpi
     * @param stringEndDate
     *            the string end date
     * @return the top geography by kpi
     * @throws RestException
     *             the rest exception
     */

    @Override
    public List<TopGeographyWrapper> getTopGeographyByKpi(Integer advancedSearchId, String callType, String band,
                                                          String operator, String technology, String kpi, String stringEndDate, String country) {

        List<TopGeographyWrapper> topGeographyWrapper = null;
        try {
            Date endDate = NvDashboardUtils.getDateFromString(stringEndDate);
            String[] geographyNameAndType = null;
            String geographyType = null;
            String geographyName = null;

            if (advancedSearchId != null) {
                geographyNameAndType = getGeographyNameAndType(advancedSearchId);
                geographyType = geographyNameAndType[ForesightConstants.ZERO_VALUE];
                geographyName = geographyNameAndType[ForesightConstants.ONE];
            }

            List<NVDashboard> topGeographyForKpi = invDashboardDao.getTopGeographyForKpi(geographyType, geographyName,
                    callType, band, operator, technology, kpi, endDate, country);
            if (topGeographyForKpi == null || topGeographyForKpi.isEmpty()) {
                throw new RestException(NVDashboardConstants.NO_DATA_MESSAGE);
            }
            topGeographyWrapper = getTopGeographyWrapper(topGeographyForKpi, kpi, geographyType);
        } catch (DaoException e) {
            throw new RestException(e.getMessage());
        }
        return topGeographyWrapper;
    }

    /**
     * Gets the top geography wrapper.
     *
     * @param topGeographyForKpi the top geography for kpi
     * @param kpi                the kpi
     * @param geographyType      the geography type
     * @return the top geography wrapper
     */
    public List<TopGeographyWrapper> getTopGeographyWrapper(List<NVDashboard> topGeographyForKpi, String kpi,
                                                            String geographyType) {
        List<TopGeographyWrapper> topGeographyWrapperList = new ArrayList<>();
        for (NVDashboard dashboard : topGeographyForKpi) {
            TopGeographyWrapper topGeographyWrapper = setTopGeographyWrapper(geographyType, dashboard);

            if (NVDashboardConstants.SIGNALSTRENGTH.equalsIgnoreCase(kpi)) {
                topGeographyWrapper.setKpiValue(dashboard.getSignalStrength());
            } else if (NVDashboardConstants.TOTAL_UC.equalsIgnoreCase(kpi)) {
                topGeographyWrapper.setKpiValue(dashboard.getTotalUC().doubleValue());
            } else if (NVDashboardConstants.AVG_DL_RATE.equalsIgnoreCase(kpi)) {
                topGeographyWrapper.setKpiValue(dashboard.getAvgDlRate());
            }
            topGeographyWrapperList.add(topGeographyWrapper);
        } // end of for
        return topGeographyWrapperList;
    }

	private TopGeographyWrapper setTopGeographyWrapper(String geographyType, NVDashboard dashboard) {
		TopGeographyWrapper topGeographyWrapper = new TopGeographyWrapper();
		if (NVDashboardConstants.GEOGRAPHYL1.equalsIgnoreCase(geographyType)) {
		    if (dashboard.getGeographyL2() != null) {
		        topGeographyWrapper.setGeographyName(dashboard.getGeographyL2().getName());
		        topGeographyWrapper.setLat(dashboard.getGeographyL2().getLatitude());
		        topGeographyWrapper.setLon(dashboard.getGeographyL2().getLongitude());
		    }
		} else if (NVDashboardConstants.GEOGRAPHYL2.equalsIgnoreCase(geographyType)) {
		    if (dashboard.getGeographyL3() != null) {
		        topGeographyWrapper.setGeographyName(dashboard.getGeographyL3().getName());
		        topGeographyWrapper.setLat(dashboard.getGeographyL3().getLatitude());
		        topGeographyWrapper.setLon(dashboard.getGeographyL3().getLongitude());
		    }
		} else if (NVDashboardConstants.GEOGRAPHYL3.equalsIgnoreCase(geographyType)
		        || NVDashboardConstants.GEOGRAPHYL4.equalsIgnoreCase(geographyType)) {
		    if (dashboard.getGeographyL4() != null) {
		        topGeographyWrapper.setGeographyName(dashboard.getGeographyL4().getName());
		        topGeographyWrapper.setLat(dashboard.getGeographyL4().getLatitude());
		        topGeographyWrapper.setLon(dashboard.getGeographyL4().getLongitude());
		    }
		} else {
		    if (dashboard.getGeographyL1() != null) {
		        topGeographyWrapper.setGeographyName(dashboard.getGeographyL1().getName());
		        topGeographyWrapper.setLat(dashboard.getGeographyL1().getLatitude());
		        topGeographyWrapper.setLon(dashboard.getGeographyL1().getLongitude());
		    }
		}
		return topGeographyWrapper;
	}

    /***
     * All Operator Count Methods ******************************************.
     *
     * @param advancedSearchId
     *            the advanced search id
     * @param stringEndDate
     *            the string end date
     * @param callType
     *            the call type
     * @param band
     *            the band
     * @param technology
     *            the technology
     * @param operator
     *            the operator
     * @param distType
     *            the dist type
     * @return the nv distribution data by type
     * @throws RestException
     *             the rest exception
     */

    @Override
    public List<NVDistributionWrapper> getNvDistributionDataByType(Integer advancedSearchId, String stringEndDate,
                                                                   String callType, String band, String technology, String operator, String distType, String kpi,String country) {
        try {
            List<NVDistributionWrapper> partialList = null;
            List<NVDistributionWrapper> finalList = new ArrayList<>();
            Date endDate = NvDashboardUtils.getDateFromString(stringEndDate);
            if (NVDashboardConstants.OPERATOR.equalsIgnoreCase(distType)) {
                partialList = getAllOperatorDistribution(advancedSearchId, endDate, callType, band, technology, kpi, country);
            } else {
                partialList = getAllOSOrDeviceDistribution(advancedSearchId, endDate, callType, band, technology,
                        operator, distType, kpi, country);
            }
            return prepareFinalList(distType, kpi, partialList, finalList);
        } catch (Exception e) {
            throw new RestException(e.getMessage());
        }
    }

	private List<NVDistributionWrapper> prepareFinalList(String distType, String kpi,
			List<NVDistributionWrapper> partialList, List<NVDistributionWrapper> finalList) {
		if (checkKPIName(kpi)) {
		    for (NVDistributionWrapper nvDistribution : partialList) {
		        if (nvDistribution.getSampleCount() > ForesightConstants.ZERO) {
		            finalList.add(nvDistribution);
		        }
		    }
		    if (checkDistType(distType)) {
		        Map<String, NVDistributionWrapper> tempMap = new HashMap<>();
		        List<NVDistributionWrapper> responseList = new ArrayList<>();
		        for (NVDistributionWrapper wrapper : finalList) {
		            setNVDistributionWrapper(tempMap, wrapper);
		        }
		        Collection<NVDistributionWrapper> values = tempMap.values();
		        responseList.addAll(values);
		        return responseList;
		    }
		    return finalList;
		} else {
		    return partialList;
		}
	}

	private void setNVDistributionWrapper(Map<String, NVDistributionWrapper> tempMap, NVDistributionWrapper wrapper) {
		if (tempMap.containsKey(wrapper.getName())) {
		    NVDistributionWrapper nvDistributionWrapper = tempMap.get(wrapper.getName());
		    nvDistributionWrapper.setActiveSampleCount(
		            nvDistributionWrapper.getActiveSampleCount() + wrapper.getActiveSampleCount());
		    nvDistributionWrapper
		            .setSampleCount(nvDistributionWrapper.getSampleCount() + wrapper.getSampleCount());
		    nvDistributionWrapper.setPassiveSampleCount(
		            nvDistributionWrapper.getPassiveSampleCount() + wrapper.getPassiveSampleCount());
		    nvDistributionWrapper.setShare(nvDistributionWrapper.getShare() + wrapper.getShare());
		    tempMap.put(wrapper.getName(), nvDistributionWrapper);
		} else {
		    tempMap.put(wrapper.getName(), wrapper);
		}
	}

    private boolean checkDistType(String distType) {
        return (distType.equalsIgnoreCase(NVDashboardConstants.DEVICE)
                || distType.equalsIgnoreCase(NVDashboardConstants.ANDROID)
                || distType.equalsIgnoreCase(NVDashboardConstants.IOS));
    }

    private boolean checkKPIName(String kpi) {
        return (NVDashboardConstants.AVG_DL_RATE.equalsIgnoreCase(kpi)
                || NVDashboardConstants.AVG_UL_RATE.equalsIgnoreCase(kpi)
                || NVDashboardConstants.LATENCY.equalsIgnoreCase(kpi) || NVDashboardConstants.SINR.equalsIgnoreCase(kpi)
                || NVDashboardConstants.QUALITY.equalsIgnoreCase(kpi)
                || NVDashboardConstants.STARRATING.equalsIgnoreCase(kpi)
                || NVDashboardConstants.SIGNALSTRENGTH.equalsIgnoreCase(kpi)
                || NVDashboardConstants.URL1_BROWSE_TIME.equalsIgnoreCase(kpi));

    }

    /**
     * Gets the all operator distribution.
     *
     * @param advancedSearchId the advanced search id
     * @param endDate          the end date
     * @param callType         the call type
     * @param band             the band
     * @param technology       the technology
     * @param kpi
     * @return the all operator distribution
     * @throws RestException the rest exception
     */
    private List<NVDistributionWrapper> getAllOperatorDistribution(Integer advancedSearchId, Date endDate,
                                                                   String callType, String band, String technology, String kpi,String country) {
        List<NVDistributionWrapper> userCountMap = null;
        List<NVDistributionWrapper> allOperatorDistributionList = null;

        try {
            String[] geographyNameAndType = null;
            String geographyType = null;
            String geographyName = null;

            if (advancedSearchId != null) {
                geographyNameAndType = getGeographyNameAndType(advancedSearchId);
                geographyType = geographyNameAndType[ForesightConstants.ZERO_VALUE];
                geographyName = geographyNameAndType[ForesightConstants.ONE];
            }

            if (NVDashboardConstants.DAY.equalsIgnoreCase(callType)) {
                allOperatorDistributionList = getAllOperatorCountDataByDate(geographyType, geographyName, endDate, band,
                        technology, country);
            } else if (NVDashboardConstants.WEEK.equalsIgnoreCase(callType)) {
                allOperatorDistributionList = getAllOperatorCountDataByWeek(geographyType, geographyName, endDate, band,
                        technology,country);
            } else if (NVDashboardConstants.MONTH.equalsIgnoreCase(callType)) {
                allOperatorDistributionList = getAllOperatorCountDataByMonth(geographyType, geographyName, endDate,
                        band, technology,country);
            }
            if (allOperatorDistributionList == null || allOperatorDistributionList.isEmpty()) {
                throw new RestException(NVDashboardConstants.NO_DATA_MESSAGE);
            } else {
                userCountMap = getDistributionListFromCountList(allOperatorDistributionList, kpi);
            }
            return userCountMap;
        } catch (Exception e) {
            logger.error("Error in NVDashboard Data::: {}", Utils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Gets the all operator count data by date.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param endDate       the end date
     * @param band          the band
     * @param technology    the technology
     * @return the all operator count data by date
     * @throws DaoException the dao exception
     */
    private List<NVDistributionWrapper> getAllOperatorCountDataByDate(String geographyType, String geographyName,
                                                                      Date endDate, String band, String technology,String country) {
        return invDashboardDao.getAllOperatorCountByDate(geographyType, geographyName, endDate, band, technology,country);
    }

    /**
     * Gets the all operator count data by week.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param endDate       the end date
     * @param band          the band
     * @param technology    the technology
     * @return the all operator count data by week
     * @throws DaoException the dao exception
     */
    private List<NVDistributionWrapper> getAllOperatorCountDataByWeek(String geographyType, String geographyName,
                                                                      Date endDate, String band, String technology, String country) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        Integer lastWeek = Integer.parseInt(NvDashboardUtils.getWeekAndYearByDate(cal));
        return invDashboardDao.getAllOperatorCountByWeek(geographyType, geographyName, lastWeek, band, technology,country);
    }

    /**
     * Gets the all operator count data by month.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param endDate       the end date
     * @param band          the band
     * @param technology    the technology
     * @return the all operator count data by month
     * @throws DaoException the dao exception
     */
    private List<NVDistributionWrapper> getAllOperatorCountDataByMonth(String geographyType, String geographyName,
                                                                       Date endDate, String band, String technology,String country) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        Integer lastMonth = Integer.parseInt(NvDashboardUtils.getMonthAndYearByDate(cal));
        return invDashboardDao.getAllOperatorCountByMonth(geographyType, geographyName, lastMonth, band, technology, country);
    }

    /***
     * All Operator Count Methods ******************************************.
     *
     * @param geographyType
     *            the geography type
     * @param geographyName
     *            the geography name
     * @param endDate
     *            the end date
     * @param band
     *            the band
     * @param technology
     *            the technology
     * @param operator
     *            the operator
     * @param countType
     *            the count type
     * @return the all OS or device count by date
     * @throws DaoException
     *             the dao exception
     */

    private List<NVDistributionWrapper> getAllOSOrDeviceCountByDate(String geographyType, String geographyName,
                                                                    Date endDate, String band, String technology, String operator, String countType, String country) {
        return invDashboardDao.getAllOSORDeviceCountByDate(geographyType, geographyName, endDate, band, technology,
                operator, countType,country);
    }

    /***
     * All Operator Count Methods ******************************************.
     *
     * @param geographyType
     *            the geography type
     * @param geographyName
     *            the geography name
     * @param endDate
     *            the end date
     * @param band
     *            the band
     * @param technology
     *            the technology
     * @param operator
     *            the operator
     * @param countType
     *            the count type
     * @return the all OS or device count by date
     * @throws DaoException
     *             the dao exception
     */

    private List<NVDashboard> getAllOSOrDeviceCountByDateForReport(String geographyType, String geographyName,
                                                                   Date startDate, Date endDate, String band, String technology, String operator, String countType, String country) {
        return invDashboardDao.getAllOSORDeviceCountByDateForReport(geographyType, geographyName, startDate, endDate,
                band, technology, operator, countType,country);
    }

    /**
     * Gets the all OS or device count by week.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param endDate       the end date
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @param countType     the count type
     * @return the all OS or device count by week
     * @throws DaoException the dao exception
     */
    private List<NVDistributionWrapper> getAllOSOrDeviceCountByWeek(String geographyType, String geographyName,
                                                                    Date endDate, String band, String technology, String operator, String countType, String country) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        Integer lastWeek = Integer.parseInt(NvDashboardUtils.getWeekAndYearByDate(cal));
        return invDashboardDao.getAllOSORDeviceCountByWeek(geographyType, geographyName, lastWeek, band, technology,
                operator, countType, country);
    }

    /**
     * Gets the all OS or device count by month.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param endDate       the end date
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @param countType     the count type
     * @return the all OS or device count by month
     * @throws DaoException the dao exception
     */
    private List<NVDistributionWrapper> getAllOSOrDeviceCountByMonth(String geographyType, String geographyName,
                                                                     Date endDate, String band, String technology, String operator, String countType, String country) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        Integer lastMonth = Integer.parseInt(NvDashboardUtils.getMonthAndYearByDate(cal));
        return invDashboardDao.getAllOSORDeviceCountByMonth(geographyType, geographyName, lastMonth, band, technology,
                operator, countType, country);
    }

    /**
     * Gets the all OS or device distribution.
     *
     * @param advancedSearchId the advanced search id
     * @param endDate          the end date
     * @param callType         the call type
     * @param band             the band
     * @param technology       the technology
     * @param operator         the operator
     * @param countType        the count type
     * @param kpi
     * @return the all OS or device distribution
     * @throws RestException the rest exception
     */
    private List<NVDistributionWrapper> getAllOSOrDeviceDistribution(Integer advancedSearchId, Date endDate,
                                                                     String callType, String band, String technology, String operator, String countType, String kpi,String country) {
        List<NVDistributionWrapper> userCountMap = null;
        List<NVDistributionWrapper> allOperatorDistributionList = null;

        try {
            String[] geographyNameAndType = null;
            String geographyType = null;
            String geographyName = null;

            if (advancedSearchId != null) {
                geographyNameAndType = getGeographyNameAndType(advancedSearchId);
                geographyType = geographyNameAndType[ForesightConstants.ZERO_VALUE];
                geographyName = geographyNameAndType[ForesightConstants.ONE];
            }

            if (NVDashboardConstants.DAY.equalsIgnoreCase(callType)) {
                allOperatorDistributionList = getAllOSOrDeviceCountByDate(geographyType, geographyName, endDate, band,
                        technology, operator, countType,country);
            } else if (NVDashboardConstants.WEEK.equalsIgnoreCase(callType)) {
                allOperatorDistributionList = getAllOSOrDeviceCountByWeek(geographyType, geographyName, endDate, band,
                        technology, operator, countType,country);
            } else if (NVDashboardConstants.MONTH.equalsIgnoreCase(callType)) {
                allOperatorDistributionList = getAllOSOrDeviceCountByMonth(geographyType, geographyName, endDate, band,
                        technology, operator, countType,country);
            }
            if (allOperatorDistributionList == null || allOperatorDistributionList.isEmpty()) {
                throw new RestException(NVDashboardConstants.NO_DATA_MESSAGE);
            } else {
                userCountMap = getDistributionListFromCountList(allOperatorDistributionList, kpi);
            }
            return userCountMap;
        } catch (Exception e) {
            logger.error("Error in NVDashboard Data : {}", Utils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    /**
     * Gets the geography name and type.
     *
     * @param advanceSearchId the advance search id
     * @return the geography name and type
     */
    private String[] getGeographyNameAndType(Integer advanceSearchId) {
        logger.info("Seraching advancesearch data for id {} ", advanceSearchId);
        String[] geoTypeName = new String[2];
        String geoGraphytype = NVDashboardConstants.EMPTY_STRING;
        String geographyName = NVDashboardConstants.EMPTY_STRING;
        try {
            AdvanceSearch advanceSearch = getAdvanceSearchById(advanceSearchId);
            if (advanceSearch != null) {
                geoGraphytype = advanceSearch.getAdvanceSearchConfiguration().getType();
                geographyName = advanceSearch.getName();
            }
            logger.info("Found geoGraphytype:- {} and geoGraphyName:- {} ", geoGraphytype, geographyName);
            geoTypeName[ForesightConstants.ZERO_VALUE] = geoGraphytype;
            geoTypeName[ForesightConstants.ONE] = geographyName;
        } catch (Exception e) {
            logger.info("Exception while fetching AdvancedSearch data {}", Utils.getStackTrace(e));
        }
        return geoTypeName;
    }

    /**
     * Sets the all kpi values for dashboard data.
     *
     * @param dashboard         the dashboard
     * @param dashboardValueMap the dashboard value map
     * @param counter           the counter
     */
    private void setAllKpiValuesForDashboardData(NVDashboard dashboard,
                                                 Map<String, Map<String, String>> dashboardValueMap, int counter) {
        setValue(dashboardValueMap, NVDashboardConstants.AVG_DL_RATE, counter, dashboard.getAvgDlRate());
        setValue(dashboardValueMap, NVDashboardConstants.AVG_UL_RATE, counter, dashboard.getAvgUlRate());
        setValue(dashboardValueMap, NVDashboardConstants.MAX_DL_RATE, counter, dashboard.getMaxDlRate());
        setValue(dashboardValueMap, NVDashboardConstants.MAX_UL_RATE, counter, dashboard.getMaxUlRate());
        setValue(dashboardValueMap, NVDashboardConstants.LATENCY, counter, dashboard.getLatency());
        setValue(dashboardValueMap, NVDashboardConstants.JITTER, counter, dashboard.getJitter());
        setValue(dashboardValueMap, NVDashboardConstants.URL1_BROWSE_TIME, counter, dashboard.getUrl1BrowseTime());
        setValue(dashboardValueMap, NVDashboardConstants.URL2_BROWSE_TIME, counter, dashboard.getUrl2BrowseTime());
        setValue(dashboardValueMap, NVDashboardConstants.URL3_BROWSE_TIME, counter, dashboard.getUrl3BrowseTime());
        setValue(dashboardValueMap, NVDashboardConstants.PACKETLOSS, counter, dashboard.getPacketLoss());
        setValue(dashboardValueMap, NVDashboardConstants.STARRATING, counter, dashboard.getStarRating());
        setValue(dashboardValueMap, NVDashboardConstants.SINR, counter, dashboard.getSinr());
        setValue(dashboardValueMap, NVDashboardConstants.QUALITY, counter, dashboard.getQuality());
        setValue(dashboardValueMap, NVDashboardConstants.SIGNALSTRENGTH, counter, dashboard.getSignalStrength());
        setValue(dashboardValueMap, NVDashboardConstants.TOTAL_UC, counter, dashboard.getTotalUC());
        setValue(dashboardValueMap, NVDashboardConstants.ENTERPRISE_UC, counter, dashboard.getEnterpriseUC());
        setValue(dashboardValueMap, NVDashboardConstants.CONSUMER_UC, counter, dashboard.getConsumerUC());
    }

    /**
     * Sets the values for kpi list for dashboard data.
     *
     * @param dashboard         the dashboard
     * @param dashboardValueMap the dashboard value map
     * @param counter           the counter
     * @param kpiList           the kpi list
     */
    private void setValuesForKpiListForDashboardData(NVDashboard dashboard,
                                                     Map<String, Map<String, String>> dashboardValueMap, int counter, List<String> kpiList) {
        if (kpiList.contains(NVDashboardConstants.AVG_DL_RATE))
            setValue(dashboardValueMap, NVDashboardConstants.AVG_DL_RATE, counter, dashboard.getAvgDlRate());
        if (kpiList.contains(NVDashboardConstants.AVG_UL_RATE))
            setValue(dashboardValueMap, NVDashboardConstants.AVG_UL_RATE, counter, dashboard.getAvgUlRate());
        if (kpiList.contains(NVDashboardConstants.MAX_DL_RATE))
            setValue(dashboardValueMap, NVDashboardConstants.MAX_DL_RATE, counter, dashboard.getMaxDlRate());
        if (kpiList.contains(NVDashboardConstants.MAX_UL_RATE))
            setValue(dashboardValueMap, NVDashboardConstants.MAX_UL_RATE, counter, dashboard.getMaxUlRate());
        if (kpiList.contains(NVDashboardConstants.LATENCY))
            setValue(dashboardValueMap, NVDashboardConstants.LATENCY, counter, dashboard.getLatency());
        if (kpiList.contains(NVDashboardConstants.JITTER))
            setValue(dashboardValueMap, NVDashboardConstants.JITTER, counter, dashboard.getJitter());
        if (kpiList.contains(NVDashboardConstants.URL1_BROWSE_TIME))
            setValue(dashboardValueMap, NVDashboardConstants.URL1_BROWSE_TIME, counter, dashboard.getUrl1BrowseTime());
        if (kpiList.contains(NVDashboardConstants.URL2_BROWSE_TIME))
            setValue(dashboardValueMap, NVDashboardConstants.URL2_BROWSE_TIME, counter, dashboard.getUrl2BrowseTime());
        if (kpiList.contains(NVDashboardConstants.URL3_BROWSE_TIME))
            setValue(dashboardValueMap, NVDashboardConstants.URL3_BROWSE_TIME, counter, dashboard.getUrl3BrowseTime());
        if (kpiList.contains(NVDashboardConstants.PACKETLOSS))
            setValue(dashboardValueMap, NVDashboardConstants.PACKETLOSS, counter, dashboard.getPacketLoss());
        if (kpiList.contains(NVDashboardConstants.STARRATING))
            setValue(dashboardValueMap, NVDashboardConstants.STARRATING, counter, dashboard.getStarRating());
        if (kpiList.contains(NVDashboardConstants.SINR))
            setValue(dashboardValueMap, NVDashboardConstants.SINR, counter, dashboard.getSinr());
        if (kpiList.contains(NVDashboardConstants.QUALITY))
            setValue(dashboardValueMap, NVDashboardConstants.QUALITY, counter, dashboard.getQuality());
        if (kpiList.contains(NVDashboardConstants.SIGNALSTRENGTH))
            setValue(dashboardValueMap, NVDashboardConstants.SIGNALSTRENGTH, counter, dashboard.getSignalStrength());
        if (kpiList.contains(NVDashboardConstants.TOTAL_UC))
            setValue(dashboardValueMap, NVDashboardConstants.TOTAL_UC, counter, dashboard.getTotalUC());
        if (kpiList.contains(NVDashboardConstants.ENTERPRISE_UC))
            setValue(dashboardValueMap, NVDashboardConstants.ENTERPRISE_UC, counter, dashboard.getEnterpriseUC());
        if (kpiList.contains(NVDashboardConstants.CONSUMER_UC))
            setValue(dashboardValueMap, NVDashboardConstants.CONSUMER_UC, counter, dashboard.getConsumerUC());
    }

    /**
     * Sets the value.
     *
     * @param dashboardValueMap the dashboard value map
     * @param kpi               the kpi
     * @param counter           the counter
     * @param value             the value
     */
    private void setValue(Map<String, Map<String, String>> dashboardValueMap, String kpi, int counter, Object value) {
        String valueKey = NVDashboardConstants.VALUE + counter;
        String kpiValue = null;
        if (value != null)
            kpiValue = value.toString();

        if (dashboardValueMap.containsKey(kpi)) {
            Map<String, String> map = dashboardValueMap.get(kpi);
            map.put(valueKey, kpiValue);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put(valueKey, kpiValue);
            dashboardValueMap.put(kpi, map);
        }
    }

    /**
     * Gets the map from list.
     *
     * @param dashboardUserCountList the dashboard user count list
     * @param callType               the call type
     * @return the map from list
     */
    public Map<String, NVDashboard> getMapFromList(List<NVDashboard> dashboardUserCountList, String callType) {
        Map<String, NVDashboard> dataMap = new HashMap<>();
        for (NVDashboard dashboard : dashboardUserCountList) {
            if (callType.equalsIgnoreCase(NVDashboardConstants.DAY)) {
                dataMap.put(DateUtil.parseDateToString(NVDashboardConstants.DD_MM_YYYY, dashboard.getCreationTime()),
                        dashboard);
            } else if (callType.equalsIgnoreCase(NVDashboardConstants.WEEK)) {
                dataMap.put(dashboard.getWeekNumber().toString(), dashboard);
            } else {
                dataMap.put(dashboard.getMonthNumber().toString(), dashboard);
            }
        }
        return dataMap;
    }

    /**
     * Gets the distribution list from count list.
     *
     * @param allOperatorDistributionList the all operator distribution list
     * @param kpi
     * @return the distribution list from count list
     */
    private List<NVDistributionWrapper> getDistributionListFromCountList(
            List<NVDistributionWrapper> allOperatorDistributionList, String kpi) {

        Long totalSampleCount = getTotalSampleCount(allOperatorDistributionList, kpi);
        updateSampleCount(allOperatorDistributionList, kpi);
        for (NVDistributionWrapper distributionWrapper : allOperatorDistributionList) {
            distributionWrapper.setShare(NvDashboardUtils.getPercentage(
                    distributionWrapper.getSampleCount() * NVDashboardConstants.ONE_DOUBLE,
                    totalSampleCount * NVDashboardConstants.ONE_DOUBLE));
        }
        return allOperatorDistributionList;
    }

    private void updateSampleCount(List<NVDistributionWrapper> allOperatorDistributionList, String kpi) {
        if (isOnlyActiveKPI(kpi)) {
            for (NVDistributionWrapper distributionWrapper : allOperatorDistributionList) {
                distributionWrapper.setSampleCount(distributionWrapper.getActiveSampleCount());
            }
        } else {
            for (NVDistributionWrapper distributionWrapper : allOperatorDistributionList) {
                distributionWrapper.setSampleCount(
                        distributionWrapper.getActiveSampleCount() + distributionWrapper.getPassiveSampleCount());
            }
        }

    }

    /**
     * Gets the total sample count.
     *
     * @param allOperatorDistributionList the all operator distribution list
     * @param kpi
     * @return the total sample count
     */
    private Long getTotalSampleCount(List<NVDistributionWrapper> allOperatorDistributionList, String kpi) {
        Long totalSampleCount = NVDashboardConstants.ZERO_LONG;

        if (isOnlyActiveKPI(kpi)) {
            for (NVDistributionWrapper distributionWrapper : allOperatorDistributionList) {
                totalSampleCount += distributionWrapper.getActiveSampleCount();
            }
        } else {
            for (NVDistributionWrapper distributionWrapper : allOperatorDistributionList) {
                totalSampleCount += (distributionWrapper.getActiveSampleCount()
                        + distributionWrapper.getPassiveSampleCount());
            }
        }
        return totalSampleCount;
    }


    private boolean isOnlyActiveKPI(String kpi) {
        boolean isActiveKPI = Boolean.FALSE;

        switch (kpi) {

            case NVDashboardConstants.AVG_DL_RATE:
            case NVDashboardConstants.AVG_UL_RATE:
            case NVDashboardConstants.LATENCY:
            case NVDashboardConstants.JITTER:
            case NVDashboardConstants.PACKETLOSS:
            case NVDashboardConstants.URL1_BROWSE_TIME:
            case NVDashboardConstants.URL2_BROWSE_TIME:
            case NVDashboardConstants.URL3_BROWSE_TIME:
            case NVDashboardConstants.STARRATING:
                isActiveKPI = Boolean.TRUE;
            default:
        }
        return isActiveKPI;
    }


    /**
     * Gets the advance search by id.
     *
     * @param advanceSearchId the advance search id
     * @return the advance search by id
     * @throws DaoException the dao exception
     */
    public AdvanceSearch getAdvanceSearchById(Integer advanceSearchId) {
        AdvanceSearch advanceSearch = null;
        if (advanceSearchId != null) {
            advanceSearch = advanceSearchDao.findByPk(advanceSearchId);
        }
        return advanceSearch;
    }

    @Override
    public List<NVDashboard> getDeviceDistributionForReport(String geographyType, String geographyName, Date startDate,
                                                            Date endDate, String band, String technology, String operator, String countType, String country) {
        List<NVDashboard> allOperatorDistributionList = null;

        try {

            allOperatorDistributionList = getAllOSOrDeviceCountByDateForReport(geographyType, geographyName, startDate,
                    endDate, band, technology, operator, countType, country);
            if (allOperatorDistributionList == null || allOperatorDistributionList.isEmpty()) {
                throw new RestException(NVDashboardConstants.NO_DATA_MESSAGE);
            }
            return allOperatorDistributionList;
        } catch (Exception e) {
            logger.error("Error in NVDashboard Data : {}", Utils.getStackTrace(e));
            throw new RestException(e.getMessage());
        }
    }

    @Override
    public Map<String, List<String>> getRoamingCountries() {
        List<OperatorDetail> operatorList = operatorService.getRoamingCountries();
        return operatorList.stream().
                collect(Collectors.groupingBy(OperatorDetail::getCountry, Collectors.mapping(OperatorDetail::getOperator, Collectors.toList())));
    }


}
