package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.HbaseRowPrefix;

@Deprecated
public interface HbaseRowPrefixDao extends IGenericDao<Integer, HbaseRowPrefix>{

	List<HbaseRowPrefix> getAllHbaseRowPrefixData() throws Exception;

	List<String> getAlphaNumericCodeByVendorDomainAndGeographyType(String domain, String vendor, String geographyType,
			String geographyValue);

	String getAlphaNumericCodeByDomain(String domain);

	List<String> getAlphaNumericCodeByVendorDomainAndGeographyList(String domain, String vendor, String geographyType,
			List<String> geographyValue);

}
