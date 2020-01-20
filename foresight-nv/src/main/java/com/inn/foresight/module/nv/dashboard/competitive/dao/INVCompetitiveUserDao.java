package com.inn.foresight.module.nv.dashboard.competitive.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.dashboard.competitive.model.NVCompetitiveUser;

/**
 * The Interface INVCompetitiveUserDao.
 */
public interface INVCompetitiveUserDao extends IGenericDao<Integer, NVCompetitiveUser>{

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
	List<NVCompetitiveUser> getCompetitiveUser(String date, String geographyL1, String geographyL2, String geographyL3,
			String geographyL4, List<String> operator, String appName);

}
