package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.NEBandDetail;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.wrapper.PBServiceInfoWrapper;

public interface INEBandDetailService extends IGenericService<Integer, NEBandDetail> {

	PBServiceInfoWrapper getMacroSitesForPBService(List<NEType> neTypeList,List<String> neFrequencyList);
	Map<String, Long> getSitesCountNestatusWiseAndGeographyWise(List<NEType> neType, String domain, String vendor, List<String> neStatus,String geographyType,String geographyValue);
	String getMorphologyByCode(String btsName) ;
}
