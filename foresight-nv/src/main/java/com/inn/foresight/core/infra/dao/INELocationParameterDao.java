package com.inn.foresight.core.infra.dao;

import java.math.BigInteger;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.NELocation;
import com.inn.foresight.core.infra.model.NELocationParameter;

/**
 * The Interface INELocationDao.
 */
public interface INELocationParameterDao extends IGenericDao<Integer, NELocationParameter> {

	String getTotalFloorByNeLocationId(Integer nelocationId);

	List<Object[]> getParameterAndKeyByNeLocationId(Integer nelocationId);

    List<NELocationParameter> getNELocationParamByNELId(Integer nelId);

	List<Object[]> getAllGCList(Integer llimit, Integer ulimit);
	
	public Object getParameterValue(Integer gcId,String parameter);

	Long getAllGCTotalCount();

	NELocationParameter getNELocationParamByParameter(Integer gcId, String parameter);

	List<NELocation> getNELocationByParameter(String parameter, String value, String type);

	List<NELocationParameter> getValuesByIdAndParameterList(Integer id, List<String> parameterList);

	List<NELocationParameter> getNELocationParameterByParameterList(Integer id, List<String> parameterList);

	Long getValuesSetByParameterList(List<String> parameterList, String combVal, Integer nelpk);

	List<Object[]> getNELocationGCList(String name, String gcType, String status, String managedByCdc, Integer llimit,
			Integer ulimit);

	Long getGCCount(String name, String status);

	List<Object[]> getGCList(String name, String status, Integer llimit, Integer ulimit);

   }
