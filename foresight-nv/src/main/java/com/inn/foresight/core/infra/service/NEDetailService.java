package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.NEDetail;
import com.inn.foresight.core.infra.wrapper.NEDetailDataWrapper;

/**
 * The Interface NEDetailService.
 */
public interface NEDetailService extends IGenericService<Integer, NEDetail> {

	List<String> getDistinctVIPCategories();

	List<String> getListOfVIPNetworkElement(List<String> neNameList, String domain, String vendor);

	NEDetailDataWrapper getSiteDetailByNeName(String siteName);

	List<NEDetailDataWrapper> getSiteParameterList(String searchValue, Integer llimit, Integer ulimit);

	Map<String, Integer> getSiteParameterCount(String searchValue);

	Map<String, String> updateSiteParameterByNeName(NEDetailDataWrapper dataWrapper);

	List<Object[]> getClutterAndRadiusByNeName(String neName);

	
}