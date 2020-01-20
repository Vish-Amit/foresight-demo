package com.inn.foresight.module.nv.dashboard.dao.impl;

import com.inn.commons.lang.DateUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.dashboard.dao.INVDashboardDao;
import com.inn.foresight.module.nv.dashboard.model.NVDashboard;
import com.inn.foresight.module.nv.dashboard.utils.NVDashboardConstants;
import com.inn.foresight.module.nv.dashboard.utils.NvDashboardUtils;
import com.inn.foresight.module.nv.dashboard.wrapper.NVDashboardWrapper;
import com.inn.foresight.module.nv.dashboard.wrapper.NVDistributionWrapper;
import com.inn.product.um.geography.dao.GeographyL1Dao;
import com.inn.product.um.geography.dao.GeographyL2Dao;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The Class NVDashboardDaoImpl.
 *
 * @author ist
 */
@Repository("NVDashboardDaoImpl")
public class NVDashboardDaoImpl extends HibernateGenericDao<Integer, NVDashboard> implements INVDashboardDao {

    /**
     * The logger.
     */
    private Logger logger = LogManager.getLogger(NVDashboardDaoImpl.class);

    /**
     * The i geography L 1 dao.
     */
    @Autowired
    private GeographyL1Dao iGeographyL1Dao;

    /**
     * The i geography L 2 dao.
     */
    @Autowired
    private GeographyL2Dao iGeographyL2Dao;

    /**
     * The i geography L 3 dao.
     */
    @Autowired
    private GeographyL3Dao iGeographyL3Dao;

    /**
     * The i geography L 4 dao.
     */
    @Autowired
    private GeographyL4Dao iGeographyL4Dao;

    /**
     * Instantiates a new NV dashboard dao impl.
     */
    public NVDashboardDaoImpl() {
        super(NVDashboard.class);
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
    @SuppressWarnings("unchecked")
    @Override
    public List<NVDashboard> getNVDashboardDataByDate(String geographyType, String geographyName, Date startDate, Date endDate, String band, String technology, String operator, String type, String name, String country) {
        logger.info("In getNVDashboardDataByDate : geographyType: {},geographyName: {}, startDate: {},"
                        + "endDate: {}, band: {}, technology: {},operator: {},country: {}", geographyType, geographyName,
                startDate, endDate, band, technology, operator, country);

        List<NVDashboard> nvDashboardList = null;
        Query query = null;

        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_NVDASHBOARD_DATA_BY_DATE_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.FIRST_DATE, startDate);
            endDate = DateUtils.addDays(endDate, ForesightConstants.ONE);
            query.setParameter(NVDashboardConstants.LAST_DATE, endDate);
            enableGeographyOrCountryFilter(geographyType, geographyName, country, operator);
            enableTypeNameFilterForNvDashboardData(type, name);

            nvDashboardList = query.getResultList();
        } catch (NoResultException e) {
            logger.error("NoResultException in getNVDashboardDataByDate :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception in getNVDashboardDataByDate:{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return nvDashboardList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NVDashboard> getNVDashboardDataByDateForReport(String geographyType, String geographyName, Date startDate, Date endDate, String band, String technology, String operator, String type, String name, String country) {
        logger.info("In getNVDashboardDataByDate : geographyType: {},geographyName: {}, startDate: {},"
                        + "endDate: {}, band: {}, technology: {},operator: {}", geographyType, geographyName,
                startDate, endDate, band, technology, operator);

        List<NVDashboard> nvDashboardList = null;
        Query query = null;

        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_ALL_OS_DEV_COUNT_BY_DATE_FOR_REPORT_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.FIRST_DATE, startDate);
            endDate = DateUtils.addDays(endDate, ForesightConstants.ONE);
            query.setParameter(NVDashboardConstants.LAST_DATE, endDate);
            enableGeographyOrCountryFilter(geographyType, geographyName, country, operator);
            Session s = (Session) getEntityManager().getDelegate();
            s.enableFilter(NVDashboardConstants.TYPE_FILTER_FOR_REPORT).setParameter(NVDashboardConstants.TYPE, NVDashboardConstants.OS);

            nvDashboardList = query.getResultList();
        } catch (NoResultException e) {
            logger.error("Exception while fetching user count data by date :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception while fetching user count data by date :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return nvDashboardList;
    }

    /**
     * Gets the NV dashboard data by week.
     *
     * @param geoGraphyType the geo graphy type
     * @param geographyName the geography name
     * @param firstWeek     the first week
     * @param lastWeek      the last week
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @param type          the type
     * @param name          the name
     * @return the NV dashboard data by week
     * @throws DaoException the dao exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NVDashboard> getNVDashboardDataByWeek(String geoGraphyType, String geographyName, Integer firstWeek, Integer lastWeek, String band, String technology, String operator, String type, String name, String country) {
        logger.info("In getNVDashboardDataByWeek : geoGraphyType: {},geographyName: {}, startWeek: {},"
                        + "lastWeek: {}, band: {}, technology: {},operator: {},country: {}", geoGraphyType, geographyName,
                firstWeek, lastWeek, band, technology, operator, country);

        List<NVDashboard> nvDashboardList = null;
        Query query = null;
        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_NVDASHBOARD_DATA_BY_WEEK_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.FIRST_WEEK, firstWeek);
            query.setParameter(NVDashboardConstants.LAST_WEEK, lastWeek + ForesightConstants.ONE);
            enableGeographyOrCountryFilter(geoGraphyType, geographyName, country, operator);
            enableTypeNameFilterForNvDashboardData(type, name);

            nvDashboardList = query.getResultList();
        } catch (NoResultException e) {
            logger.error("Exception while fetching  user count data by week :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception while fetching user count data data by week :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return nvDashboardList;
    }


    /**
     * Gets the NV dashboard data by month.
     *
     * @param geoGraphyType the geo graphy type
     * @param geographyName the geography name
     * @param firstMonth    the first month
     * @param lastMonth     the last month
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @param type          the type
     * @param name          the name
     * @return the NV dashboard data by month
     * @throws DaoException the dao exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NVDashboard> getNVDashboardDataByMonth(String geoGraphyType, String geographyName, Integer firstMonth, Integer lastMonth, String band, String technology, String operator, String type, String name, String country) {
        logger.info("In getNVDashboardDataByMonth : geoGraphyType: {},geographyName: {}, startMonth: {},"
                        + "lastMonth: {}, band: {}, technology: {},operator: {},country: {}", geoGraphyType, geographyName,
                firstMonth, lastMonth, band, technology, operator, country);

        List<NVDashboard> nvDashboardList = null;
        Query query = null;

        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_NVDASHBOARD_DATA_BY_MONTH_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.FIRST_MONTH, firstMonth);
            query.setParameter(NVDashboardConstants.LAST_MONTH, lastMonth + 1);

            enableGeographyOrCountryFilter(geoGraphyType, geographyName, country, operator);
            enableTypeNameFilterForNvDashboardData(type, name);

            nvDashboardList = query.getResultList();
        } catch (NoResultException e) {
            logger.error("Exception while fetching user count data by month :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception while fetching user count data by month :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return nvDashboardList;
    }

    /***UserCount Methods********************************************.
     *
     * @param geoGraphyType the geo graphy type
     * @param geographyName the geography name
     * @param endDate the end date
     * @param band the band
     * @param technology the technology
     * @param operator the operator
     * @return the user count data by date
     * @throws DaoException the dao exception
     */

    @Override
    public NVDashboardWrapper getUserCountDataByDate(String geoGraphyType, String geographyName, Date endDate, String band, String technology, String operator, String country) {
        logger.info("In getUserCountDataByDate : geoGraphyType: {},geographyName: {}, band: {}, technology: {},operator: {}",
                geoGraphyType, geographyName, band, technology, operator);

        NVDashboardWrapper nvDashboardWrapper = null;
        Query query = null;


        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_UC_DATA_BY_DATE_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.LAST_DATE, endDate);
            enableGeographyOrCountryFilter(geoGraphyType, geographyName, country, operator);

            nvDashboardWrapper = (NVDashboardWrapper) query.getSingleResult();

        } catch (NoResultException e) {
            logger.error("Exception while fetching  User Count data by date :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception while fetching  User Count data by date :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }

        return nvDashboardWrapper;
    }


    /**
     * Gets the user count data by week.
     *
     * @param geoGraphyType the geo graphy type
     * @param geographyName the geography name
     * @param lastWeek      the last week
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @return the user count data by week
     * @throws DaoException the dao exception
     */
    @Override
    public NVDashboardWrapper getUserCountDataByWeek(String geoGraphyType, String geographyName, Integer lastWeek, String band, String technology, String operator, String country) {
        NVDashboardWrapper nvDashboardWrapper = null;
        Query query = null;
        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_UC_DATA_BY_WEEK_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.LAST_WEEK, lastWeek);

            enableGeographyOrCountryFilter(geoGraphyType, geographyName, country, operator);
            nvDashboardWrapper = (NVDashboardWrapper) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error("Exception while fetching  User Count data by week :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception while fetching  User Count data by week :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return nvDashboardWrapper;
    }


    /**
     * Gets the user count data by month.
     *
     * @param geoGraphyType the geo graphy type
     * @param geographyName the geography name
     * @param lastMonth     the last month
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @return the user count data by month
     * @throws DaoException the dao exception
     */
    @Override
    public NVDashboardWrapper getUserCountDataByMonth(String geoGraphyType, String geographyName, Integer lastMonth, String band, String technology, String operator, String country) {
        NVDashboardWrapper nvDashboardWrapper = null;
        Query query = null;
        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_UC_DATA_BY_MONTH_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.LAST_MONTH, lastMonth);
            enableGeographyOrCountryFilter(geoGraphyType, geographyName, country, operator);
            nvDashboardWrapper = (NVDashboardWrapper) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error("Exception while fetching  User Count data by month :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception while fetching  User Count data by month :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return nvDashboardWrapper;
    }

    /***UserCount Methods********************************************.
     *
     * @param geoGraphyType the geo graphy type
     * @param geographyName the geography name
     * @param endDate the end date
     * @param band the band
     * @param technology the technology
     * @return the all operator count by date
     * @throws DaoException the dao exception
     */

    /**
     * All Operator Count Detail Methods.
     */


    @SuppressWarnings("unchecked")
    @Override
    public List<NVDistributionWrapper> getAllOperatorCountByDate(String geoGraphyType, String geographyName, Date endDate, String band, String technology, String country) {
        logger.info("LastDate===> " + endDate);
        List<NVDistributionWrapper> allOperatorCountList = null;
        Query query = null;
        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_ALL_OPERATOR_COUNT_BY_DATE_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.LAST_DATE, endDate);

            enableGeographyOrCountryFilter(geoGraphyType, geographyName, country, null);

            allOperatorCountList = query.getResultList();

        } catch (NoResultException e) {
            logger.error("NoResultException inside getAllOperatorCountByDate :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception inside getAllOperatorCountByDate :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return allOperatorCountList;
    }


    /**
     * Gets the all operator count by week.
     *
     * @param geoGraphyType the geo graphy type
     * @param geographyName the geography name
     * @param lastWeek      the last week
     * @param band          the band
     * @param technology    the technology
     * @return the all operator count by week
     * @throws DaoException the dao exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NVDistributionWrapper> getAllOperatorCountByWeek(String geoGraphyType, String geographyName, Integer lastWeek, String band, String technology, String country) {
        List<NVDistributionWrapper> allOperatorCountList = null;
        Query query = null;
        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_ALL_OPERATOR_COUNT_BY_WEEK_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.LAST_WEEK, lastWeek);

            enableGeographyOrCountryFilter(geoGraphyType, geographyName, country, null);
            allOperatorCountList = query.getResultList();
        } catch (NoResultException e) {
            logger.error("NoResultException inside getAllOperatorCountByWeek:{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception inside getAllOperatorCountByWeek :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return allOperatorCountList;
    }


    /**
     * Gets the all operator count by month.
     *
     * @param geoGraphyType the geo graphy type
     * @param geographyName the geography name
     * @param lastMonth     the last month
     * @param band          the band
     * @param technology    the technology
     * @return the all operator count by month
     * @throws DaoException the dao exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NVDistributionWrapper> getAllOperatorCountByMonth(String geoGraphyType, String geographyName, Integer lastMonth, String band, String technology, String country) {
        List<NVDistributionWrapper> allOperatorCountList = null;
        Query query = null;
        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_ALL_OPERATOR_COUNT_BY_MONTH_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.LAST_MONTH, lastMonth);
            enableGeographyOrCountryFilter(geoGraphyType, geographyName, country, null);
            allOperatorCountList = query.getResultList();
        } catch (NoResultException e) {
            logger.error("Exception while fetching  all operator share detail by month :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception while fetching  all operator share detail by month :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return allOperatorCountList;
    }

    /***All Operating Systems Count Detail Methods ***************************************.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param endDate the end date
     * @param band the band
     * @param technology the technology
     * @param operator the operator
     * @param countType the count type
     * @return the all OSOR device count by date
     * @throws DaoException the dao exception
     */

    @SuppressWarnings("unchecked")
    @Override
    public List<NVDistributionWrapper> getAllOSORDeviceCountByDate(String geographyType, String geographyName, Date endDate, String band, String technology, String operator, String countType, String country) {
        List<NVDistributionWrapper> allOperatorCountList = null;
        Query query = null;
        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_ALL_OS_DEV_COUNT_BY_DATE_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.LAST_DATE, endDate);
            query.setParameter(NVDashboardConstants.TYPE, NVDashboardConstants.OS);

            if (NVDashboardConstants.IOS.equalsIgnoreCase(countType)) {
                enableNameLikeIosFilter();
            } else if (NVDashboardConstants.ANDROID.equalsIgnoreCase(countType)) {
                enableNameLikeAndroidFilter();
            } else {
                query.setParameter(NVDashboardConstants.TYPE, NVDashboardConstants.DEVICE);
            }
            enableGeographyOrCountryFilter(geographyType, geographyName, country, operator);
            allOperatorCountList = query.getResultList();
        } catch (NoResultException e) {
            logger.error("Exception while extracting all operator share detail data by date :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception while extracting all operator share detail by date :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return allOperatorCountList;
    }


    /**
     * Gets the all OSOR device count by week.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param lastWeek      the last week
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @param countType     the count type
     * @return the all OSOR device count by week
     * @throws DaoException the dao exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NVDistributionWrapper> getAllOSORDeviceCountByWeek(String geographyType, String geographyName, Integer lastWeek, String band, String technology, String operator, String countType, String country) {
        List<NVDistributionWrapper> allOperatorCountList = null;
        Query query = null;
        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_ALL_OS_DEV_COUNT_BY_WEEK_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.LAST_WEEK, lastWeek);
            query.setParameter(NVDashboardConstants.TYPE, NVDashboardConstants.OS);

            if (NVDashboardConstants.IOS.equalsIgnoreCase(countType)) {
                enableNameLikeIosFilter();
            } else if (NVDashboardConstants.ANDROID.equalsIgnoreCase(countType)) {
                enableNameLikeAndroidFilter();
            } else {
                query.setParameter(NVDashboardConstants.TYPE, NVDashboardConstants.DEVICE);
            }

            enableGeographyOrCountryFilter(geographyType, geographyName, country, operator);
            allOperatorCountList = query.getResultList();
        } catch (NoResultException e) {
            logger.error("Exception while extracting all os or device share detail data by week :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception while extracting all operator share detail by week :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return allOperatorCountList;
    }


    /**
     * Gets the all OSOR device count by month.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param lastMonth     the last month
     * @param band          the band
     * @param technology    the technology
     * @param operator      the operator
     * @param countType     the count type
     * @return the all OSOR device count by month
     * @throws DaoException the dao exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NVDistributionWrapper> getAllOSORDeviceCountByMonth(String geographyType, String geographyName, Integer lastMonth, String band, String technology, String operator, String countType, String country) {
        List<NVDistributionWrapper> allOperatorCountList = null;
        Query query = null;
        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_ALL_OS_DEV_COUNT_BY_MONTH_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.LAST_MONTH, lastMonth);
            query.setParameter(NVDashboardConstants.TYPE, NVDashboardConstants.OS);

            if (NVDashboardConstants.IOS.equalsIgnoreCase(countType)) {
                enableNameLikeIosFilter();
            } else if (NVDashboardConstants.ANDROID.equalsIgnoreCase(countType)) {
                enableNameLikeAndroidFilter();
            } else {
                query.setParameter(NVDashboardConstants.TYPE, NVDashboardConstants.DEVICE);
            }

            enableGeographyOrCountryFilter(geographyType, geographyName, country, operator);
            allOperatorCountList = query.getResultList();
        } catch (NoResultException e) {
            logger.error("Exception while extracting all os or device share detail data by month :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception while extracting all operator share detail by month :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return allOperatorCountList;
    }


    /**
     * Gets the top geography for kpi.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param callType      the call type
     * @param band          the band
     * @param operator      the operator
     * @param technology    the technology
     * @param kpi           the kpi
     * @param lastDate      the last date
     * @return the top geography for kpi
     * @throws DaoException the dao exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NVDashboard> getTopGeographyForKpi(String geographyType, String geographyName, String callType, String band, String operator, String technology, String kpi, Date lastDate, String country) {
        logger.info("In getTopGeographyForKpi : geographyType: {},geographyName: {}, lastDate: {},"
                        + "band: {}, technology: {},operator: {} ,kpi: {},callType: {},country: {}", geographyType, geographyName,
                lastDate, band, technology, operator, kpi, callType, country);


        List<NVDashboard> dashboards = null;
        try {
            Query query = getTopGeographyNamedQueryByKpi(band, operator, technology, kpi);
            enableTimeRangeFilterByCallType(callType, lastDate);
            enableGeographyOrCountryFilter(geographyType, geographyName, country, operator);
            query.setMaxResults(ForesightConstants.SEVEN);
            dashboards = query.getResultList();
            logger.info("Found dashboard list for top geogaphy of size {}", dashboards.size());
        } catch (NoResultException e) {
            logger.error("Exception while extracting top geography detail :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception while extracting top geography detail :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return dashboards;
    }


    /***All Operating Systems Count Detail Methods ***************************************.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     * @param endDate the end date
     * @param band the band
     * @param technology the technology
     * @param operator the operator
     * @param countType the count type
     * @return the all OSOR device count by date
     * @throws DaoException the dao exception
     */

    @SuppressWarnings("unchecked")
    @Override
    public List<NVDashboard> getAllOSORDeviceCountByDateForReport(String geographyType, String geographyName, Date startDate, Date endDate, String band, String technology, String operator, String countType, String country) {
        List<NVDashboard> allOperatorCountList = null;
        Query query = null;
        try {
            query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_ALL_OS_DEV_COUNT_BY_DATE_FOR_REPORT_QUERY);
            query.setParameter(NVDashboardConstants.BAND, band);
            query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
            query.setParameter(NVDashboardConstants.FIRST_DATE, startDate);
            endDate = DateUtils.addDays(endDate, ForesightConstants.ONE);
            query.setParameter(NVDashboardConstants.LAST_DATE, endDate);
            query.setParameter(NVDashboardConstants.TYPE, NVDashboardConstants.DEVICE);
            enableGeographyOrCountryFilter(geographyType, geographyName, country, operator);
            allOperatorCountList = query.getResultList();
        } catch (NoResultException e) {
            logger.error("Exception while extracting all operator share detail data by date :{}", e.getMessage());
            throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
        } catch (Exception e) {
            logger.error("Exception while extracting all operator share detail by date :{}", Utils.getStackTrace(e));
            throw new DaoException(e.getMessage());
        }
        return allOperatorCountList;
    }

    /**
     * Enable time range filter by call type.
     *
     * @param callType the call type
     * @param lastDate the last date
     */
    private void enableTimeRangeFilterByCallType(String callType, Date lastDate) {
        if (NVDashboardConstants.DAY.equalsIgnoreCase(callType)) {
            enableDateFilter(lastDate);
        } else if (NVDashboardConstants.WEEK.equalsIgnoreCase(callType)) {
            enableWeekFilter(lastDate);
        } else if (NVDashboardConstants.MONTH.equalsIgnoreCase(callType)) {
            enableMonthFilter(lastDate);
        }
    }

    /**
     * Enable date filter.
     *
     * @param lastDate the last date
     */
    private void enableDateFilter(Date lastDate) {
        logger.info("Enabling date filter for date :- {}", lastDate);
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.DATE_FILTER).setParameter(NVDashboardConstants.CREATION_TIME, lastDate);
    }

    /**
     * Enable week filter.
     *
     * @param lastDate the last date
     */
    private void enableWeekFilter(Date lastDate) {
        logger.info("In enableWeekFilter for date :- {}", lastDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastDate);
        Integer weekNumber = Integer.parseInt(NvDashboardUtils.getWeekAndYearByDate(cal));
        logger.info("Enabling week filter for weekNumber :- {}", weekNumber);
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.WEEK_FILTER).setParameter(NVDashboardConstants.WEEK_NUMBER, weekNumber);
    }

    /**
     * Enable month filter.
     *
     * @param lastDate the last date
     */
    private void enableMonthFilter(Date lastDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastDate);
        Integer monthNumber = Integer.parseInt(NvDashboardUtils.getMonthAndYearByDate(cal));
        logger.info("Enabling month filter for monthNumber :- {}", monthNumber);
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.MONTH_FILTER).setParameter(NVDashboardConstants.MONTH_NUMBER, monthNumber);
    }

    private void enableCountryFilter(String country) {
        logger.info("Enabling country filter :- {}", country);
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.COUNTRY_FILTER).setParameter(NVDashboardConstants.COUNTRY, country);
    }

    private void enableOperatorFilter(String operator) {
        logger.info("Enabling country filter :- {}", operator);
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.OPERATOR_FILTER).setParameter(NVDashboardConstants.OPERATOR, operator);
    }

    /**
     * Gets the top geography named query by kpi.
     *
     * @param band       the band
     * @param operator   the operator
     * @param technology the technology
     * @param kpi        the kpi
     * @return the top geography named query by kpi
     */
    private Query getTopGeographyNamedQueryByKpi(String band, String operator, String technology, String kpi) {

        String queryName = ForesightConstants.BLANK_STRING;
        logger.info("In getTopGeographyNamedQueryByKpi kpi {} ", kpi);
        if (NVDashboardConstants.AVG_DL_RATE.equalsIgnoreCase(kpi))
            queryName = NVDashboardConstants.GET_TOP_DL_GEOGRAPHY_QUERY;
        else if (NVDashboardConstants.SIGNALSTRENGTH.equalsIgnoreCase(kpi))
            queryName = NVDashboardConstants.GET_TOP_SIGNAL_STRENGTH_GEOGRAPHY_QUERY;
        else if (NVDashboardConstants.TOTAL_UC.equalsIgnoreCase(kpi))
            queryName = NVDashboardConstants.GET_TOP_UC_GEOGRAPHY_QUERY;
        Query query = getEntityManager().createNamedQuery(queryName);
        query.setParameter(NVDashboardConstants.BAND, band);
        query.setParameter(NVDashboardConstants.TECHNOLOGY, technology);
        return query;
    }


    /**
     * Enable georaphic filter for nv dashboard data.
     *
     * @param geoGraphyType the geo graphy type
     * @param geographyName the geography name
     */
    private void enableGeoraphicFilterForNvDashboardData(String geoGraphyType, String geographyName) {
        try {
            logger.info("In enableGeoraphicFilterForNvDashboardData method, geoGraphyType {} and geographyName {}", geoGraphyType, geographyName);
            if (NVDashboardConstants.GEOGRAPHYL1.equalsIgnoreCase(geoGraphyType)) {
                GeographyL1 geographyL1 = iGeographyL1Dao.getGeographyL1ByName(geographyName);
                enableGeographyL1ForNvDashboardData(geographyL1);
            } else if (NVDashboardConstants.GEOGRAPHYL2.equalsIgnoreCase(geoGraphyType)) {
                GeographyL2 geographyL2 = iGeographyL2Dao.getGeographyL2ByName(geographyName);
                enableGeographyL2ForNvDashboardData(geographyL2);
            } else if (NVDashboardConstants.GEOGRAPHYL3.equalsIgnoreCase(geoGraphyType)) {
                GeographyL3 geographyL3 = iGeographyL3Dao.getGeographyL3ByName(geographyName);
                enableGeographyL3ForNvDashboardData(geographyL3);
            } else if (NVDashboardConstants.GEOGRAPHYL4.equalsIgnoreCase(geoGraphyType)) {
                GeographyL4 geographyL4 = iGeographyL4Dao.getGeographyL4ByName(geographyName);
                enableGeographyL4ForNvDashboardData(geographyL4);
            } else {
                enableGeographyPANLForNvDashboardData();
            }
        } catch (Exception e) {
            logger.error("Exception : {}", Utils.getStackTrace(e));
        }

    }

    private void enableGeoraphicFilterForNvDashboardDataForReport(String geoGraphyType, String geographyName) {
        try {
            logger.info("In enableGeoraphicFilterForNvDashboardData method, geoGraphyType {} and geographyName {}", geoGraphyType, geographyName);
            if (NVDashboardConstants.GEOGRAPHYL1.equalsIgnoreCase(geoGraphyType)) {
                GeographyL1 geographyL1 = iGeographyL1Dao.getGeographyL1ByName(geographyName);
                enableGeographyL1ForNvDashboardDataForReport(geographyL1);
            } else if (NVDashboardConstants.GEOGRAPHYL2.equalsIgnoreCase(geoGraphyType)) {
                GeographyL2 geographyL2 = iGeographyL2Dao.getGeographyL2ByName(geographyName);
                enableGeographyL2ForNvDashboardDataForReport(geographyL2);
            } else if (NVDashboardConstants.GEOGRAPHYL3.equalsIgnoreCase(geoGraphyType)) {
                GeographyL3 geographyL3 = iGeographyL3Dao.getGeographyL3ByName(geographyName);
                enableGeographyL3ForNvDashboardDataForReport(geographyL3);
            } else if (NVDashboardConstants.GEOGRAPHYL4.equalsIgnoreCase(geoGraphyType)) {
                GeographyL4 geographyL4 = iGeographyL4Dao.getGeographyL4ByName(geographyName);
                enableGeographyL4ForNvDashboardDataForReport(geographyL4);
            } else {
                enableGeographyPANLForNvDashboardDataForReport();
            }
        } catch (Exception e) {
            logger.error("Exception : {}", Utils.getStackTrace(e));
        }

    }


    /**
     * Enable top georaphy filter.
     *
     * @param geographyType the geography type
     * @param geographyName the geography name
     */
    private void enableTopGeoraphyFilter(String geographyType, String geographyName) {
        try {
            if (NVDashboardConstants.GEOGRAPHYL1.equalsIgnoreCase(geographyType)) {
                GeographyL1 geographyL1 = iGeographyL1Dao.getGeographyL1ByName(geographyName);
                enableFilterForTopGL2(geographyL1);
            } else if (NVDashboardConstants.GEOGRAPHYL2.equalsIgnoreCase(geographyType)) {
                GeographyL2 geographyL2 = iGeographyL2Dao.getGeographyL2ByName(geographyName);
                enableFilterForTopGL3(geographyL2);
            } else if (NVDashboardConstants.GEOGRAPHYL3.equalsIgnoreCase(geographyType)) {
                GeographyL3 geographyL3 = iGeographyL3Dao.getGeographyL3ByName(geographyName);
                enableFilterForTopGL4(geographyL3);
            } else if (NVDashboardConstants.GEOGRAPHYL4.equalsIgnoreCase(geographyType)) {
                GeographyL4 geographyL4 = iGeographyL4Dao.getGeographyL4ByName(geographyName);
                enableGeographyL4ForNvDashboardData(geographyL4);
            } else {
                enableFilterForTopL1();
            }
        } catch (Exception e) {
            logger.error("Exception while enabling geography filter: {}", Utils.getStackTrace(e));
        }
    }

    /**
     * Enable filter for top L 1.
     */
    private void enableFilterForTopL1() {
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.TOP_GEOGRAPHYL1_FILTER);
    }

    /**
     * Enable type name filter for nv dashboard data.
     *
     * @param type the type
     * @param name the name
     */
    private void enableTypeNameFilterForNvDashboardData(String type, String name) {
        logger.info("in enableTypeNameFilterForNvDashboardData: type:{} and name :{}", type, name);
        try {
            if (type != null && name != null) {
                if (NVDashboardConstants.OPERATOR.equalsIgnoreCase(type)) {
                    enableNameFilter(name);
                } else {
                    enableTypeAndNameFilter(type, name);
                }
            } else {
                enableTypeAndNameNullFilter();
            }
        } catch (Exception e) {
            logger.error("Exception while enabling type and name filter", Utils.getStackTrace(e));
        }
    }

    /**
     * Enable type and name null filter.
     */
    private void enableTypeAndNameNullFilter() {
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.TYPE_NAME_NULL_FILTER);
    }

    /**
     * Enable name filter.
     *
     * @param name the name
     */
    private void enableNameFilter(String name) {
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.NAME_FILTER).setParameter(NVDashboardConstants.NAME, name);
    }

    /**
     * Enable type and name filter.
     *
     * @param type the type
     * @param name the name
     */
    private void enableTypeAndNameFilter(String type, String name) {
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.TYPE_NAME_FILTER).setParameter(NVDashboardConstants.TYPE, type).setParameter(NVDashboardConstants.NAME, name);
    }

    /**
     * Enable geography PANL for nv dashboard data.
     */
    private void enableGeographyPANLForNvDashboardData() {
        logger.info("In enableGeographyPANLForNvDashboardData");
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.GEOGRAPHY_PANL_FILTER);
    }

    private void enableGeographyPANLForNvDashboardDataForReport() {
        logger.info("In enableGeographyPANLForNvDashboardData");
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.GEOGRAPHY_PANL_FILTER_FOR_REPORT);
    }

    /**
     * Enable geography L 1 for nv dashboard data.
     *
     * @param geographyL1 the geography L 1
     */
    private void enableGeographyL1ForNvDashboardData(GeographyL1 geographyL1) {
        logger.info("In enableGeographyL1ForNvDashboardData geographyL1 id is {} ", geographyL1.getId());
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.GEOGRAPHYL1_FILTER).setParameter(NVDashboardConstants.GEOGRAPHY, geographyL1.getId());
    }

    private void enableGeographyL1ForNvDashboardDataForReport(GeographyL1 geographyL1) {
        logger.info("In enableGeographyL1ForNvDashboardData geographyL1 id is {} ", geographyL1.getId());
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.GEOGRAPHYL1_FILTER_FOR_REPORT).setParameter(NVDashboardConstants.GEOGRAPHY, geographyL1.getId());
    }

    /**
     * Enable geography L 2 for nv dashboard data.
     *
     * @param geographyL2 the geography L 2
     */
    private void enableGeographyL2ForNvDashboardData(GeographyL2 geographyL2) {
        logger.info("In enableGeographyL2ForNvDashboardData geographyL2 id is {} ", geographyL2.getId());
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.GEOGRAPHYL2_FILTER).setParameter(NVDashboardConstants.GEOGRAPHY, geographyL2.getId());
    }

    private void enableGeographyL2ForNvDashboardDataForReport(GeographyL2 geographyL2) {
        logger.info("In enableGeographyL2ForNvDashboardData geographyL2 id is {} ", geographyL2.getId());
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.GEOGRAPHYL2_FILTER_FOR_REPORT).setParameter(NVDashboardConstants.GEOGRAPHY, geographyL2.getId());
    }

    /**
     * Enable geography L 3 for nv dashboard data.
     *
     * @param geographyL3 the geography L 3
     */
    private void enableGeographyL3ForNvDashboardData(GeographyL3 geographyL3) {
        logger.info("In enableGeographyL3ForNvDashboardData geographyL3 id is {} ", geographyL3.getId());
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.GEOGRAPHYL3_FILTER).setParameter(NVDashboardConstants.GEOGRAPHY, geographyL3.getId());
    }

    private void enableGeographyL3ForNvDashboardDataForReport(GeographyL3 geographyL3) {
        logger.info("In enableGeographyL3ForNvDashboardData geographyL3 id is {} ", geographyL3.getId());
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.GEOGRAPHYL3_FILTER_FOR_REPORT).setParameter(NVDashboardConstants.GEOGRAPHY, geographyL3.getId());
    }

    /**
     * Enable geography L 4 for nv dashboard data.
     *
     * @param geographyL4 the geography L 4
     */
    private void enableGeographyL4ForNvDashboardData(GeographyL4 geographyL4) {
        logger.info("In enableGeographyL4ForNvDashboardData geographyL4 id is {} ", geographyL4.getId());
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.GEOGRAPHYL4_FILTER).setParameter(NVDashboardConstants.GEOGRAPHY, geographyL4.getId());
    }

    private void enableGeographyL4ForNvDashboardDataForReport(GeographyL4 geographyL4) {
        logger.info("In enableGeographyL4ForNvDashboardData geographyL4 id is {} ", geographyL4.getId());
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.GEOGRAPHYL4_FILTER_FOR_REPORT).setParameter(NVDashboardConstants.GEOGRAPHY, geographyL4.getId());
    }


    /**
     * Enable filter for top GL 2.
     *
     * @param geographyL1 the geography L 1
     */
    private void enableFilterForTopGL2(GeographyL1 geographyL1) {
        logger.info("In enableFilterForTopGL2 geographyL1 id is {} ", geographyL1.getId());
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.TOP_GEOGRAPHYL2_FILTER).setParameter(NVDashboardConstants.GEOGRAPHY, geographyL1.getId());
    }

    /**
     * Enable filter for top GL 3.
     *
     * @param geographyL2 the geography L 2
     */
    private void enableFilterForTopGL3(GeographyL2 geographyL2) {
        logger.info("In enableFilterForTopGL3 geographyL2 id is {} ", geographyL2.getId());
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.TOP_GEOGRAPHYL3_FILTER).setParameter(NVDashboardConstants.GEOGRAPHY, geographyL2.getId());
    }

    /**
     * Enable filter for top GL 4.
     *
     * @param geographyL3 the geography L 3
     */
    private void enableFilterForTopGL4(GeographyL3 geographyL3) {
        logger.info("In enableFilterForTopGL4 geographyL3 id is {} ", geographyL3.getId());
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.TOP_GEOGRAPHYL4_FILTER).setParameter(NVDashboardConstants.GEOGRAPHY, geographyL3.getId());
    }

    /**
     * Enable name like android filter.
     */
    private void enableNameLikeAndroidFilter() {
        logger.info("In enableNameLikeAndroidFilter");
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.NAME_LIKE_ANDROID_FILTER);
    }

    /**
     * Enable name like ios filter.
     */
    private void enableNameLikeIosFilter() {
        logger.info("In enableNameLikeIosFilter");
        Session s = (Session) getEntityManager().getDelegate();
        s.enableFilter(NVDashboardConstants.NAME_LIKE_IOS_FILTER);
    }

    /*
    enable geography filter or county filter foe roaming countries
     */
    private void enableGeographyOrCountryFilter(String geographyType, String geographyName, String country, String operator) {
        if (operator != null) {
            enableCountryFilter(operator);
        }
        if (geographyType != null && geographyName != null) {
            enableGeoraphicFilterForNvDashboardData(geographyType, geographyName);
        }
        if (country != null) {
            enableCountryFilter(country);
        }

    }
}
