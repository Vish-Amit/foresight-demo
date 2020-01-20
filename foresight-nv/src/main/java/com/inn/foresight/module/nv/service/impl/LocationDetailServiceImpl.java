package com.inn.foresight.module.nv.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.model.LocationDetailWrapper;
import com.inn.foresight.module.nv.service.ILocationDetailService;
import com.inn.foresight.module.nv.service.ISiteDetailService;

/** The Class LocationDetailServiceImpl. */
@Service("LocationDetailServiceImpl")
public class LocationDetailServiceImpl implements ILocationDetailService{
	
	/** The logger. */
	static Logger logger = LogManager.getLogger(LocationDetailServiceImpl.class);

	/** The location detail map. */
	private Map<String, LocationDetailWrapper> locationDetailMap = null;
	
	/** The site detail service. */
	@Autowired
	private ISiteDetailService siteDetailService;
	
	/**
	 * Gets the location detail for cell id mnc mcc.
	 *
	 * @return the location detail for cell id mnc mcc
	 */
	@Override
	public Map<String, LocationDetailWrapper> getLocationDetailForCellIdMncMcc() {
		
		try {
			if(locationDetailMap == null || locationDetailMap.isEmpty()){
				List<Object[]> resultList = siteDetailService.getLocationForCellIdMncMcc();
				if(resultList != null && !resultList.isEmpty()){
					logger.info("resultList size : {}",resultList.size());
					locationDetailMap = createLocationMapFromResult(resultList);
				}				
			}
		} catch (Exception e) {
			logger.error("Err in getting location detail Map : {} ",e.getMessage());
		}
		if(locationDetailMap != null){
			logger.info("locationDetailMap size : {} ",locationDetailMap.size());
		}
		return locationDetailMap;
	}

	/**
	 * Creates the location map from result.
	 *
	 * @param resultList the result list
	 * @return the map
	 */
	private  Map<String, LocationDetailWrapper> createLocationMapFromResult(List<Object[]> resultList) {
		
		logger.info("Going to create location Map");
		
		locationDetailMap = new HashMap<>();
		
		Integer cellIdInt;
		Integer mncInt;
		Integer mccInt;
		Double latDouble;	
		Double lonDouble;
		String geoL4String;
		String geoL3String;
		String geoL2String;
		String geoL1String;
		
		
		for (Object[] result : resultList) {

			Object cellId = result[ForesightConstants.ZERO];
			Object mnc= result[ForesightConstants.ONE];
			Object mcc= result[ForesightConstants.TWO];
			Object latitude = result[ForesightConstants.THREE];
			Object longitude = result[ForesightConstants.FOUR];
			Object geoL4 = result[ForesightConstants.FIVE];
			Object geoL3 = result[ForesightConstants.SIX];
			Object geoL2 = result[ForesightConstants.SEVEN];
			Object geoL1 = result[NVConstant.EIGHT];
			
			try {
				cellIdInt = Integer.valueOf(String.valueOf(cellId));
				mncInt = Integer.valueOf(String.valueOf(mnc));
				mccInt = Integer.valueOf(String.valueOf(mcc));
				latDouble = Double.valueOf(String.valueOf(latitude));
				lonDouble = Double.valueOf(String.valueOf(longitude));
				geoL4String = String.valueOf(geoL4);
				geoL3String = String.valueOf(geoL3);
				geoL2String = String.valueOf(geoL2);
				geoL1String = String.valueOf(geoL1);
			} catch (Exception e) {
				continue;
			}
			
		
			if(cellIdInt != null && mncInt != null && mccInt != null && latDouble != null && lonDouble != null){
				
				LocationDetailWrapper locationWrapper = new LocationDetailWrapper();
				
				locationWrapper.setCellId(cellIdInt);
				locationWrapper.setMnc(mncInt);
				locationWrapper.setMcc(mccInt);
				locationWrapper.setLatitude(latDouble);
				locationWrapper.setLongitude(lonDouble);
				locationWrapper.setGeoL4(geoL4String);
				locationWrapper.setGeoL3(geoL3String);
				locationWrapper.setGeoL2(geoL2String);
				locationWrapper.setGeoL1(geoL1String);
				
				locationDetailMap.put(cellIdInt+"_"+mncInt+"_"+mccInt, locationWrapper);
			}
			
		}
		
		return locationDetailMap;
	}

	@Override
	public List<Map<String, String>> getLocationbyCGI(Integer cgi) {
		return siteDetailService.getSiteLocationByCGI(cgi);
	}
}
