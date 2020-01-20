package com.inn.foresight.module.nv.service;

import java.util.List;
import java.util.Map;

import com.inn.commons.maps.LatLng;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.SiteDataWrapper;
import com.inn.foresight.core.infra.wrapper.WifiWrapper;
import com.inn.foresight.module.nv.coverage.wrapper.SiteDetailWrapper;
import com.inn.foresight.module.nv.layer.wrapper.CoverageLayerWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;

public interface ISiteDetailService {

	List<Object[]> getLocationForCellIdMncMcc();

	List<RANDetail> findAllRANDetail();

	List<SiteDataWrapper> getNECellsBySapIds(List<String> var1);

	LatLng getLocationByCGIAndPci(Integer var1, Integer var2);

	List<NetworkElement> getWifiAPDetailByFloorId(List<Integer> var1);

	List<Map<String, String>> getSiteLocationByCGI(Integer var1);

	List<SiteInformationWrapper> getMacroSiteDetailsForCellLevelForReport(Map<String, Double> viewPortMap,
			List<String> technologyList, List<String> neNamesList, Map<String, List<String>> geographyMap,
			boolean isNeighbourSite, boolean needOnlyMacroCells, boolean needOnlyOnAirSites);

	List<SiteDetailWrapper> getSiteDetailByCGI(Integer cgi, Boolean isCountRequired);

	List<Integer> getDistinctPCIBySiteIdAndBand(String neName, List<String> bandList);

	SiteInformationWrapper getNECellDetailData(List<String> neNamesList, SiteInformationWrapper wrapper);

	List<NetworkElementWrapper> getSitesFromViewPort(Map<String, Double> viewPortMap, List<String> nestatusList,
			List<String> neTypeList, Integer limit);

	List<SiteDetailWrapper> getSitesDetail(Long timestamp);
	
	WifiWrapper getWIFIAPDetail(String macAddress);
	
	SiteInformationWrapper getSiteInformationFromCgi(Integer cgi);
	
	
	List<SiteDetailWrapper> getSitesDetailFromViewPort(CoverageLayerWrapper wrapper);


	SiteInformationWrapper getNetworkElementByNename(String nename);
}
