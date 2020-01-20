package com.inn.foresight.module.nv.dashboard.competitive.service;

import com.inn.foresight.module.nv.dashboard.competitive.model.NVCompetitiveDashboard;
import com.inn.foresight.module.nv.dashboard.competitive.model.NVCompetitiveUser;

import java.util.List;
import java.util.Map;

/**
 * The Interface INVCompetitiveDashboardService.
 *
 * @author innoeye
 */
public interface INVCompetitiveDashboardService {


    /**
     * Gets the competitive user.
     *
     * @param date the date
     * @param geographyL1 the geography L 1
     * @param geographyL2 the geography L 2
     * @param geographyL3 the geography L 3
     * @param geographyL4 the geography L 4
     * @param operator the operator
     * @param appType
     * @return the competitive user
     */
    Map<String, Object> getCompetitiveUser(String date, String geographyL1, String geographyL2, String geographyL3,
                                           String geographyL4, List<String> operator, String appType);

    Map<String,Object> populateUserDistributionData(List<NVCompetitiveUser> competitiveUsers);

    /**
     * Gets the sample count.
     *
     * @param date the date
     * @param geographyL1 the geography L 1
     * @param geographyL2 the geography L 2
     * @param geographyL3 the geography L 3
     * @param geographyL4 the geography L 4
     * @param operator the operator
     * @param appType
     * @return the sample count
     */
    Map<String, Map<String, Long>> getSampleCount(String date, String geographyL1, String geographyL2, String geographyL3, String geographyL4,
                                                  List<String> operator, String appType);

    Map<String,Map<String,Long>> populateSampleCountDistribution(List<NVCompetitiveDashboard> competitiveSamples);

    /**
     * Gets the KPI distribution.
     *
     * @param date the date
     * @param geographyL1 the geography L 1
     * @param geographyL2 the geography L 2
     * @param geographyL3 the geography L 3
     * @param geographyL4 the geography L 4
     * @param operator the operator
     * @param appType
     * @return the KPI distribution
     */
    Map<String, Map<String, Map<String, String>>> getKPIDistribution(String date, String geographyL1, String geographyL2, String geographyL3,
                                                                     String geographyL4, List<String> operator, String appType);

    Map<String, Map<String, Map<String, String>>> populateKPIDistribution(List<NVCompetitiveUser> competitiveResponse);

    /**
     * Gets the operator distribution.
     *
     * @param date the date
     * @param geographyL1 the geography L 1
     * @param geographyL2 the geography L 2
     * @param geographyL3 the geography L 3
     * @param geographyL4 the geography L 4
     * @param operator the operator
     * @param appType
     * @return the operator distribution
     */
    String getOperatorDistribution(String date, String geographyL1, String geographyL2, String geographyL3,
                                   String geographyL4, List<String> operator, String appType);

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
     * @param appType
     * @return the KPI ranges
     */
    Map<String, Map<String, Long>> getKPIRanges(String date, String kpi, String geographyL1, String geographyL2, String geographyL3, String geographyL4,
                                                List<String> operators, String appType);

    Map<String, Map<String,Long>> populateKPIRanges(List<NVCompetitiveDashboard> competitiveDashboard, String kpi);

    /**
     * Gets the dl ul distribution.
     *
     * @param date the date
     * @param geographyL1 the geography L 1
     * @param geographyL2 the geography L 2
     * @param geographyL3 the geography L 3
     * @param geographyL4 the geography L 4
     * @param operators the operators
     * @param appType
     * @return the dl ul distribution
     */
    Map<String, Map<String, Map<String, Map<String, String>>>> getDlUlDistribution(String date, String geographyL1, String geographyL2, String geographyL3, String geographyL4,
                                                                                   List<String> operators, String appType);

    Map<String, Map<String, Map<String, Map<String, String>>>> populateDlUlDistribution(List<NVCompetitiveUser> competitiveResponse);

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
     * @param appType
     * @return the dl ul ranges
     */
    Map<String, Map<String, Map<String, Integer>>> getDlUlRanges(String date, String kpi, String geographyL1, String geographyL2, String geographyL3,
                                                                 String geographyL4, List<String> operators, String appType);

    /**
     * Gets the performance KPI data.
     *
     * @param date the date
     * @param kpi the kpi
     * @param geographyL1 the geography L 1
     * @param geographyL2 the geography L 2
     * @param geographyL3 the geography L 3
     * @param geographyL4 the geography L 4
     * @param operators the operators
     * @param technology the technology
     * @param appType
     * @return the performance KPI data
     */
    public Object getPerformanceKPIData(String date, String kpi, String geographyL1, String geographyL2, String geographyL3,
                                        String geographyL4, List<String> operators,String technology, String appType);

    Map<String, Map<String, Map<String, Integer>>> populateDlUlRanges(List<NVCompetitiveDashboard> competitiveDashboard, String kpi);

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
    Map<String, Double> getNPSData(String date, String geographyL1, String geographyL2, String geographyL3, String geographyL4,
                                   List<String> operators);

    Object populatePerformanceData(
            List<NVCompetitiveDashboard> competitiveDashboard, String kpi);
}
