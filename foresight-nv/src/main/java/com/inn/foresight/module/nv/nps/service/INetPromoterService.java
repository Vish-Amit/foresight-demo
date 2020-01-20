package com.inn.foresight.module.nv.nps.service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.nps.model.NPSRawDetail;


/**
 * The Class INetPromoterService.
 * 	
 * @author innoeye
 */

public interface INetPromoterService {

	/**
	 * Persist NPS data.
	 *
	 * @param npsJson the nps json
	 * @return the NPS raw detail
	 * @throws RestException the rest exception
	 */
	NPSRawDetail persistNPSData(String npsJson);
	
	/**
	 * Gets the NPS event score.
	 *
	 * @param geographyId the geography id
	 * @param geographyType the geography type
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param operator the operator
	 * @param technology the technology
	 * @return the NPS event score
	 */
	String getNPSEventScore(Integer geographyId, String geographyType, String startDate, String endDate, String operator, String technology);

	/**
	 * Gets the NPS monthly analysis detail.
	 *
	 * @param geographyId the geography id
	 * @param geographyType the geography type
	 * @param startDate the start date
	 * @param operator the operator
	 * @param technology the technology
	 * @param kpi 
	 * @return the NPS monthly analysis detail
	 */
	String getNPSMonthlyAnalysisDetail(Integer geographyId, String geographyType, String startDate,
			String operator, String technology);

	String getNPSData(Integer geographyId, String geographyType, String startDate,
			String callType, String operator, String technology);
	
	String getNPSKpiWiseData(Integer geographyId, String geographyType, String startDate, String endDate,
			String operator, String technology, String kpi);
	
	

}
