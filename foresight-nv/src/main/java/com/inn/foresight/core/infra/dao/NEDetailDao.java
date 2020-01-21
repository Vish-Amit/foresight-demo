package com.inn.foresight.core.infra.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.NEDetail;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.NEDetailDataWrapper;

/**
 * The Interface NEDetailDao.
 *
 */
@Transactional(readOnly = true)
public interface NEDetailDao extends IGenericDao<Integer, NEDetail>{

	List<String> getDistinctVIPCategories();

	String getMorphologyByCode(String btsName);

	List<String> getListOfVIPNetworkElement(List<String> neNameList, String domain,
			List<Vendor> vendorList);

	NEDetailDataWrapper getSiteDetailByNeName(String siteName);

	List<NEDetailDataWrapper> getSiteParameterList(String searchValue, Integer llimit, Integer ulimit);

	Boolean updateSiteClassByNeName(String neName, String category);

	List<Object[]> getClutterAndRadiusByNeName(String neName);

	
}