package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.EmsServer;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.EMSType;
import com.inn.foresight.core.infra.utils.enums.Vendor;

/**
 * The Interface IEmsServerService.
 */
public interface IEmsServerService extends IGenericService<Integer, EmsServer> {

	List<Map<String, Object>> getEmsServer(Integer geographyId, String geoType,
			String emsType, String domain, String vendor, String technology);

	List<Map<String, String>> getEmsNameAndIpByDomainAndVendor(String domain, String vendor,String technology);

	List<String> getDistinctEmsName();

	List<String> getDistinctEmsIP();

	List<EmsServer> getEmsServerByVendorDomainAndEmsType(Vendor vendor, Domain domain, EMSType emsType);

	String createGCcredential(String username, String password, String ip);
}
