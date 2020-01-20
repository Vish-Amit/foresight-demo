package com.inn.foresight.module.nv.dashboard.rest;

import javax.ws.rs.core.Response;

/** The Interface INVDashboardRest. */
public interface INVDashboardRest {

	/**
	 * Gets the nv dashboard data by date and location.
	 *
	 * @param advancedSearchId the advanced search id
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param callTypeParam the call type param
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @return the nv dashboard data by date and location
	 */
	Response getNvDashboardDataByDateAndLocation(Integer advancedSearchId,
			String startDate, String endDate,String callTypeParam,String band,String technology,String operator);

	/**
	 * Gets the top seven data by location.
	 *
	 * @param advancedSearchId the advanced search id
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param callTypeParam the call type param
	 * @param band the band
	 * @return the top seven data by location
	 */
	String getTopSevenDataByLocation(Integer advancedSearchId,
			String startDate, String endDate,String callTypeParam,String band);

	/**
	 * Gets the all user count.
	 *
	 * @param advancedSearchId the advanced search id
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param callType the call type
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @return the all user count
	 */
	String getAllUserCount(Integer advancedSearchId, String startDate, String endDate, String callType, String band,
			String technology, String operator);

}
