package com.inn.foresight.module.nv.nps.dao;

import java.util.List;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.nps.model.NPSRawDetail;


/**
 * The Class INetPromoterDao.
 * 	
 * @author innoeye
 */

public interface INetPromoterRawDao {

	/**
	 * Creates the NPS raw data.
	 *
	 * @param wrapper the wrapper
	 * @return the NPS raw detail
	 * @throws DaoException the dao exception
	 */
	NPSRawDetail createNPSRawData(NPSRawDetail wrapper);

	List<Object[]> getNPSRawData(String date, String geographyL1, String geographyL2, String geographyL3, String geographyL4,
			List<String> operators);


}
