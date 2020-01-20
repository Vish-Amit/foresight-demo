package com.inn.foresight.module.nv.dashboard.competitive.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.dashboard.competitive.model.NVCompetitiveDashboard;

/**
 * @author innoeye
 *
 */
public interface INVCompetitiveDashboardDao extends IGenericDao<Integer, NVCompetitiveDashboard>{

	/**
	 * @param date
	 * @param geographyL1
	 * @param geographyL2
	 * @param geographyL3
	 * @param geographyL4
	 * @param operator
	 * @param appName 
	 * @return
	 */
	List<NVCompetitiveDashboard> getCompetitiveSamples(String date, String geographyL1, String geographyL2,
			String geographyL3, String geographyL4, List<String> operator,String technology, String appName);

}
