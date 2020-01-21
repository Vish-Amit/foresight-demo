package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.BackhaulDetail;
import com.inn.foresight.core.infra.wrapper.BackHaulDetailWrapper;

public interface IBackhaulDetailDao extends IGenericDao<Integer, BackhaulDetail> {

	BackHaulDetailWrapper getHubSiteDetailByNeName(String neName);

	String getDeviceLocationByNeName(String neName);
	
	List<BackhaulDetail>getBackhaulDetailByNeName(List<String>neNameList);

	List<Object[]> getDeviceLocationByNeNameList(String domain, String vendor,
			List<String> neName);

	List<Object[]> getHopTypeByDomainVendor(String domain, String vendor,
			List<String> hopType);

}
