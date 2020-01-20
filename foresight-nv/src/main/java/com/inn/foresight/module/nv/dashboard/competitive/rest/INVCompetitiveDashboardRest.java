package com.inn.foresight.module.nv.dashboard.competitive.rest;

import java.util.List;

import javax.ws.rs.core.Response;

/**
 * The Interface INVCompetitiveDashboardRest.
 *
 * @author innoeye
 */
public interface INVCompetitiveDashboardRest {

	/**
	 * Gets the competitive user.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operator the operator
	 * @return the competitive user
	 */
	Response getCompetitiveUser(String date, String geographyL1, String geographyL2, String geographyL3,
			String geographyL4, List<String> operator,String appName);

	/**
	 * Gets the sample count.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operator the operator
	 * @return the sample count
	 */
	Response getSampleCount(String date, String geographyL1, String geographyL2, String geographyL3, String geographyL4,
			List<String> operator,String appName);

	/**
	 * Gets the KPI distibution.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operator the operator
	 * @return the KPI distibution
	 */
	Response getKPIDistribution(String date, String geographyL1, String geographyL2, String geographyL3,
			String geographyL4, List<String> operator,String appName);

	/**
	 * Gets the operator distribution.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operator the operator
	 * @return the operator distribution
	 */
	Response getOperatorDistribution(String date, String geographyL1, String geographyL2, String geographyL3,
			String geographyL4, List<String> operator,String appName);

	/**
	 * Gets the KPI ranges.
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
	Response getKPIRanges(String date, String kpi, String geographyL1, String geographyL2, String geographyL3,
			String geographyL4, List<String> operators,String appName);

	/**
	 * Gets the dl ul distribution.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operators the operators
	 * @return the dl ul distribution
	 */
	Response getDlUlDistribution(String date, String geographyL1, String geographyL2, String geographyL3,
			String geographyL4, List<String> operators,String appName);

	/**
	 * Gets the dl ul ranges.
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
	Response getDlUlRanges(String date, String kpi, String geographyL1, String geographyL2, String geographyL3,
			String geographyL4, List<String> operators,String appName);

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
	Response getNPSData(String date, String geographyL1, String geographyL2, String geographyL3, String geographyL4,
			List<String> operators);
	
	
	/**
	 * @param date
	 * @param kpi
	 * @param geographyL1
	 * @param geographyL2
	 * @param geographyL3
	 * @param geographyL4
	 * @param operators
	 * @param technology
	 * @return
	 */
	Response getPerformanceKPIData(String date, String kpi, String geographyL1, String geographyL2, String geographyL3,
			String geographyL4, List<String> operators,String technology,String appName);

}
