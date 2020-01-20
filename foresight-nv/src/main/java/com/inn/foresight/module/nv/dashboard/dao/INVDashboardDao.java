package com.inn.foresight.module.nv.dashboard.dao;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.dashboard.model.NVDashboard;
import com.inn.foresight.module.nv.dashboard.wrapper.NVDashboardWrapper;
import com.inn.foresight.module.nv.dashboard.wrapper.NVDistributionWrapper;

/** The Interface INVDashboardDao. */
public interface INVDashboardDao extends IGenericDao<Integer, NVDashboard> {

	/**
	 * Gets the NV dashboard data by date.
	 *
	 * @param geographyType the geography type
	 * @param geographyName the geography name
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @param type the type
	 * @param name the name
	 * @return the NV dashboard data by date
	 * @throws DaoException the dao exception
	 */
	/* Dashboard Data Methods */
	List<NVDashboard> getNVDashboardDataByDate(String geographyType, String geographyName, Date startDate,
			Date endDate, String band, String technology, String operator, String type, String name,String country);


	List<NVDashboard> getNVDashboardDataByDateForReport(String geographyType, String geographyName , Date startDate, Date endDate, String band, String technology, String operator, String type, String name, String country);

	/**
	 * Gets the NV dashboard data by week.
	 *
	 * @param geographyType the geography type
	 * @param geographyName the geography name
	 * @param firstWeek the first week
	 * @param lastWeek the last week
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @param type the type
	 * @param name the name
	 * @return the NV dashboard data by week
	 * @throws DaoException the dao exception
	 */
	List<NVDashboard> getNVDashboardDataByWeek(String geographyType, String geographyName, Integer firstWeek,
			Integer lastWeek, String band, String technology, String operator, String type, String name,String country);

	/**
	 * Gets the NV dashboard data by month.
	 *
	 * @param geographyType the geography type
	 * @param geographyName the geography name
	 * @param firstMonth the first month
	 * @param lastMonth the last month
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @param type the type
	 * @param name the name
	 * @return the NV dashboard data by month
	 * @throws DaoException the dao exception
	 */
	List<NVDashboard> getNVDashboardDataByMonth(String geographyType, String geographyName, Integer firstMonth,
			Integer lastMonth, String band, String technology, String operator, String type, String name,String country);


	/**
	 * Gets the user count data by date.
	 *
	 * @param geographyType the geography type
	 * @param geographyName the geography name
	 * @param date the date
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @return the user count data by date
	 * @throws DaoException the dao exception
	 */
	/* UserCount Methods */
	NVDashboardWrapper getUserCountDataByDate(String geographyType, String geographyName, Date date, String band,
			String technology, String operator,String country);

	/**
	 * Gets the user count data by week.
	 *
	 * @param geographyType the geography type
	 * @param geographyName the geography name
	 * @param week the week
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @return the user count data by week
	 * @throws DaoException the dao exception
	 */
	NVDashboardWrapper getUserCountDataByWeek(String geographyType, String geographyName, Integer week,
			String band, String technology, String operator,String country);

	/**
	 * Gets the user count data by month.
	 *
	 * @param geographyType the geography type
	 * @param geographyName the geography name
	 * @param month the month
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @return the user count data by month
	 * @throws DaoException the dao exception
	 */
	NVDashboardWrapper getUserCountDataByMonth(String geographyType, String geographyName, Integer month,
			String band, String technology, String operator,String country);

	/**
	 * Gets the all operator count by date.
	 *
	 * @param geographyType the geography type
	 * @param geographyName the geography name
	 * @param endDate the end date
	 * @param band the band
	 * @param technology the technology
	 * @return the all operator count by date
	 * @throws DaoException the dao exception
	 */
	/* AllOperatorCount Methods */
	List<NVDistributionWrapper> getAllOperatorCountByDate(String geographyType, String geographyName,
			Date endDate, String band, String technology, String country);


	/**
	 * Gets the all operator count by week.
	 *
	 * @param geographyType the geography type
	 * @param geographyName the geography name
	 * @param lastWeek the last week
	 * @param band the band
	 * @param technology the technology
	 * @return the all operator count by week
	 * @throws DaoException the dao exception
	 */
	List<NVDistributionWrapper> getAllOperatorCountByWeek(String geographyType, String geographyName,
			Integer lastWeek, String band, String technology, String  country);



	/**
	 * Gets the all operator count by month.
	 *
	 * @param geographyType the geography type
	 * @param geographyName the geography name
	 * @param lastMonth the last month
	 * @param band the band
	 * @param technology the technology
	 * @return the all operator count by month
	 * @throws DaoException the dao exception
	 */
	List<NVDistributionWrapper> getAllOperatorCountByMonth(String geographyType, String geographyName,
			Integer lastMonth, String band, String technology, String country);

	/**
	 * Gets the all OSOR device count by date.
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
	/* AllOSDviceCount Methods */
	List<NVDistributionWrapper> getAllOSORDeviceCountByDate(String geographyType, String geographyName,
			Date endDate, String band, String technology, String operator, String countType, String country);

	/**
	 * Gets the all OSOR device count by week.
	 *
	 * @param geographyType the geography type
	 * @param geographyName the geography name
	 * @param lastWeek the last week
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @param countType the count type
	 * @return the all OSOR device count by week
	 * @throws DaoException the dao exception
	 */
	List<NVDistributionWrapper> getAllOSORDeviceCountByWeek(String geographyType, String geographyName,
			Integer lastWeek, String band, String technology, String operator, String countType, String country);

	/**
	 * Gets the all OSOR device count by month.
	 *
	 * @param geographyType the geography type
	 * @param geographyName the geography name
	 * @param lastMonth the last month
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @param countType the count type
	 * @return the all OSOR device count by month
	 * @throws DaoException the dao exception
	 */
	List<NVDistributionWrapper> getAllOSORDeviceCountByMonth(String geographyType, String geographyName,
			Integer lastMonth, String band, String technology, String operator, String countType, String country);

	/**
	 * Gets the top geography for kpi.
	 *
	 * @param geographyType the geography type
	 * @param geographyName the geography name
	 * @param callType the call type
	 * @param band the band
	 * @param operator the operator
	 * @param technology the technology
	 * @param kpi the kpi
	 * @param lastDate the last date
	 * @return the top geography for kpi
	 * @throws DaoException the dao exception
	 */
	List<NVDashboard> getTopGeographyForKpi(String geographyType, String geographyName, String callType,
			String band, String operator, String technology, String kpi, Date lastDate, String country);


	List<NVDashboard> getAllOSORDeviceCountByDateForReport(String geographyType, String geographyName, Date startDate,
														   Date endDate, String band, String technology, String operator, String countType, String country);


}
