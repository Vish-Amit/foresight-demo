package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.NEGeographyMapping;

/**
 * The Interface IGeographyMappingDao.
 */
public interface INEGeographyMappingDao extends IGenericDao<Integer, NEGeographyMapping> {

	List<String> getNodeDetailByDomainVendor(String domain, String vendor,
			String nodeType);

	List<String> getNodeDetailByGeography(String domain, String vendor,
			String nodeType, String geography);
	 
	List<String> getDistinctOtherGeographyDetail(String domain, String vendor);

	long getNEMappingCountByNEId(Integer networkElementId, Integer otherGeographyId);

	void createNEGeographyMapping(Integer networkelementId, Integer otherGeographyId);

	
	 
}
