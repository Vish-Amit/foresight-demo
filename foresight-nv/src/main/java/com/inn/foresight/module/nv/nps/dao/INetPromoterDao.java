package com.inn.foresight.module.nv.nps.dao;

import java.util.List;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.nps.model.NPSAggDetail;
import com.inn.foresight.module.nv.nps.model.NetPromoterRaw;
import com.inn.foresight.module.nv.nps.model.NetPromoterWrapper;


/**
 * The Class INetPromoterDao.
 * 	
 * @author innoeye
 */

public interface INetPromoterDao {

	/**
	 * Creates the NPS raw data.
	 *
	 * @param wrapper the wrapper
	 * @return the net promoter raw
	 * @throws DaoException the dao exception
	 */
	NetPromoterRaw createNPSRawData(NetPromoterRaw wrapper);

	/**
	 * Gets the NPS event score data.
	 *
	 * @param geographyId the geography id
	 * @param geographyType the geography type
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param weekno the weekno
	 * @param operator the operator
	 * @param technology the technology
	 * @return the NPS event score data
	 * @throws DaoException the dao exception
	 */
	List<NetPromoterWrapper> getNPSEventScoreData(Integer geographyId, String geographyType, String startDate,
			String endDate, Integer weekno, String operator, String technology);

	/**
	 * Gets the NPS monthly analysis detail.
	 *
	 * @param geographyId the geography id
	 * @param geographyType the geography type
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param operator the operator
	 * @param technology the technology
	 * @return the NPS monthly analysis detail
	 * @throws DaoException the dao exception
	 */
	List<NetPromoterWrapper> getNPSMonthlyAnalysisDetail(Integer geographyId, String geographyType, String startDate,
			String endDate, String operator, String technology);

	List<NetPromoterWrapper> getNPSData(Integer geographyId, String geographyType, String startDate, String endDate, List<Integer> weekNumber,
			String operator, String technology, String callType);

	List<NetPromoterWrapper> getKpiWiseData(Integer geographyId, String geographyType, String startDate, String endDate,
			String operator, String technology, String kpi, List<Integer> weekNumber);

	List<NPSAggDetail> getNPSAggData(String date, List<Integer> geographyL4List, List<String> operators);

	

}
