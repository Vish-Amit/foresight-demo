package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.commons.maps.LatLng;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.MacroSiteDetail;

/**
 * The Interface IMacroSiteDetailService.
 */
public interface IMacroSiteDetailService extends IGenericService<Integer, MacroSiteDetail> {

	Map getEcgiLocation();

	LatLng getLocationByCGIAndPci(Integer cgi, Integer pci);

	List<MacroSiteDetail> getMacroSiteDetailByListNename(List<String> nename);

	String validateEnbid(Integer enbId);

}
