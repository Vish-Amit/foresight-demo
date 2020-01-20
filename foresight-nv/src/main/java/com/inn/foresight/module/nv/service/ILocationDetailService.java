package com.inn.foresight.module.nv.service;

import java.util.List;
import java.util.Map;

import com.inn.foresight.module.nv.model.LocationDetailWrapper;

/** The Interface ILocationDetailService. */
public interface ILocationDetailService {

	/**
	 * Gets the location detail for cell id mnc mcc.
	 *
	 * @return the location detail for cell id mnc mcc
	 */
	Map<String, LocationDetailWrapper> getLocationDetailForCellIdMncMcc();
	
	
	
	/**
	 * Gets the location detail for CGI.
	 *
	 * @return the latitude , longitude & Azimuth
	 */
	List<Map<String, String>> getLocationbyCGI(Integer cgi);

}
