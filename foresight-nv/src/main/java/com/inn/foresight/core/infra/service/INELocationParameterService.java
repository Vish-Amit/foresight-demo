package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.NELocationParameter;

/**
 * The Interface INELocationParameterService.
 */
public interface INELocationParameterService extends IGenericService<Integer, NELocationParameter> {

	Map<String, Object> getParameterAndKeyByNeLocationId(Integer nelocationId);
	
	Map<String, NELocationParameter> getNELocationParameterMapByParameterList(Integer id, List<String> parameterList);

	Long getAllGCTotalCount(String name, String status);

	List<Map<String, Object>> getAllGCList(String name, String status, Integer llimit, Integer ulimit);

	
}
