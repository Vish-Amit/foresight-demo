package com.inn.foresight.module.nv.dashboard.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.dashboard.model.NVDashboard;
import com.inn.foresight.module.nv.dashboard.wrapper.NVDistributionWrapper;
import com.inn.foresight.module.nv.dashboard.wrapper.TopGeographyWrapper;
import com.inn.foresight.module.nv.dashboard.wrapper.UserCountWrapper;

/** The Interface INVDashboardService. */
public interface INVDashboardService extends IGenericService<Integer, NVDashboard> {

	/**
	 * Gets the nv dashboard data by date and location.
	 *
	 * @param advanceSearchId the advance search id
	 * @param stringEndDate the string end date
	 * @param callType the call type
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @param kpiList the kpi list
	 * @param type the type
	 * @param name the name
	 * @return the nv dashboard data by date and location
	 * @throws RestException the rest exception
	 * @throws RestException the rest exception
	 */
	Map<String, Map<String, String>> getNvDashboardDataByDateAndLocation(Integer advanceSearchId,
			String stringEndDate, String callType, String band, String technology, String operator,
			List<String> kpiList, String type, String name, String country);

	/**
	 * Gets the nv dashboard user count by date and location.
	 *
	 * @param advancedSearchId the advanced search id
	 * @param endDate the end date
	 * @param callType the call type
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @return the nv dashboard user count by date and location
	 * @throws RestException the rest exception
	 */
	Map<String, UserCountWrapper> getNvDashboardUserCountByDateAndLocation(Integer advancedSearchId,
			String endDate, String callType, String band, String technology, String operator,String country);

	/**
	 * Gets the nv distribution data by type.
	 *
	 * @param advancedSearchId the advanced search id
	 * @param stringEndDate the string end date
	 * @param callType the call type
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @param distType the dist type
	 * @param kpi 
	 * @return the nv distribution data by type
	 * @throws RestException the rest exception
	 */
	List<NVDistributionWrapper> getNvDistributionDataByType(Integer advancedSearchId, String stringEndDate,
			String callType, String band, String technology, String operator, String distType, String kpi,String country);


    Map<String, Map<String, String>> getNVDashboardData(Integer advancedSearchId, Date endDate, String callType,
                                                        String band, String technology, String operator, List<String> kpiList, String type, String name,String country);

    List<NVDashboard> getNVDashboardDataByDate(String geographyType, String geographyName, Date startDate, Date endDate,
                                               String band, String technology, String operator, String type, String name, String country);

	List<NVDashboard> getNVDashboardDataByDateForReport(String geographyType, String geographyName, Date startDate,
			Date endDate, String band, String technology, String operator, String type, String name, String country);

	List<NVDashboard> getDeviceDistributionForReport(String geographyType, String geographyName, Date startDate, Date endDate, String band, String technology, String operator, String countType, String country);

	Map<String, UserCountWrapper> getUserCountData(Integer advancedSearchId, Date endDate, String callType,
												   String band, String technology, String operator,String country);

	/**
	 * Gets the top geography by kpi.
	 *
	 * @param advancedSearchId the advanced search id
	 * @param callType the call type
	 * @param band the band
	 * @param operator the operator
	 * @param technology the technology
	 * @param kpi the kpi
	 * @param stringEndDate the string end date
	 * @return the top geography by kpi
	 * @throws RestException the rest exception
	 */
	List<TopGeographyWrapper> getTopGeographyByKpi(Integer advancedSearchId, String callType, String band,
			String operator, String technology, String kpi, String stringEndDate, String country);

	Map<String, Map<String, String>> getNVDashboardDataForTribe(Integer advanceSearchId, String stringEndDate, String callType,
			String band, String technology, String operator, List<String> kpiList, String type, String name, String country);

	Map<String, List<String>> getRoamingCountries();


}
