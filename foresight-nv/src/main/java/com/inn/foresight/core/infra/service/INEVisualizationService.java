package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.infra.model.MacroSiteDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.core.infra.wrapper.MacroSiteDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NETaskDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.SectorSummaryWrapper;
import com.inn.foresight.core.infra.wrapper.SiteConnectionPointWrapper;
import com.inn.foresight.core.infra.wrapper.SiteEmsDetailsWrapper;
import com.inn.foresight.core.infra.wrapper.SiteGeographicalDetail;
import com.inn.foresight.core.infra.wrapper.SiteLayerSelection;
import com.inn.foresight.core.infra.wrapper.SiteMileStoneDetailWrapper;
import com.inn.foresight.core.infra.wrapper.SiteSelectionWrapper;
import com.inn.foresight.core.infra.wrapper.WifiWrapper;

/**
 * The Interface INEVisualizationService.
 */
public interface INEVisualizationService {


	/**
	 * Gets the site table data.
	 *
	 * @param southWestLong the south west long
	 * @param southWestLat the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat the north east lat
	 * @param zoomLevel the zoom level
	 * @param neType the ne type
	 * @param progressState the progress state
	 * @param band2300 the band 2300
	 * @param band1800 the band 1800
	 * @param band850 the band 850
	 * @param displyaSite the displya site
	 * @return the site table data
	 * @throws RestException the rest exception
	 */
	List<NetworkElementWrapper> getSiteTableData(Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel,
			NEType neType, String progressState, Boolean band2300, Boolean band1800, Boolean band850, Integer displyaSite);

	/**
	 * Gets the site overview detail data.
	 *
	 * @param neName the ne name
	 * @return the site overview detail data
	 * @throws RestException the rest exception
	 */
	SiteGeographicalDetail getSiteOverviewDetailData(String neName);

	/**
	 * Gets the ems detail data for sites.
	 *
	 * @param sapid the sapid
	 * @return the ems detail data for sites
	 * @throws RestException the rest exception
	 */
	SiteEmsDetailsWrapper getEmsDetailDataForSites(String sapid);

	/**
	 * Gets the site overview data.
	 *
	 * @param neName the ne name
	 * @return the site overview data
	 * @throws RestException the rest exception
	 */
	SiteGeographicalDetail getSiteOverviewData(String neName);

	/**
	 * Gets the site summary overview by band.
	 *
	 * @param sapid the sapid
	 * @param band the band
	 * @param progressState the progress state
	 * @return the site summary overview by band
	 * @throws RestException the rest exception
	 */
	Map<String, List<SectorSummaryWrapper>> getSiteSummaryOverviewByBand(String sapid, String band,String progressState);

	/**
	 * Gets the site summary antenna parameters by band.
	 *
	 * @param sapid the sapid
	 * @param band the band
	 * @param progressState the progress state
	 * @return the site summary antenna parameters by band
	 * @throws RestException the rest exception
	 */
	Map<String, List<SectorSummaryWrapper>> getSiteSummaryAntennaParametersByBand(String sapid, String band,String progressState);

	/**
	 * Gets the site geographic detail data.
	 *
	 * @param neName the ne name
	 * @return the site geographic detail data
	 * @throws RestException the rest exception
	 */
	SiteGeographicalDetail getSiteGeographicDetailData(String neName,String neType);

	/**
	 * Gets the site count for layer visualisation.
	 *
	 * @param siteSelectionWrapper the site selection wrapper
	 * @param southWestLong the south west long
	 * @param southWestLat the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat the north east lat
	 * @param zoomLevel the zoom level
	 * @return the site count for layer visualisation
	 * @throws RestException 
	 * @throws DaoException 
	 */
	Map getSiteCountForLayerVisualisation(SiteSelectionWrapper siteSelectionWrapper, Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel);

	List<NetworkElement> getNetworkelementByGeographyLevel(SiteSelectionWrapper siteSelectionWrapper);


	Map<String, Long> getsiteCountsForKPI(Double southWestLong, Double southWestLat, Double northEastLong,Double northEastLat, Integer zoomLevel, KPISummaryDataWrapper filterConfiguration);

	Map<String, List<SectorSummaryWrapper>> getSiteSummaryRadioParametersByBand(String neName, String neFrequency,String neStatus);

	List<MacroSiteDetail> getMacroSiteDetails(MacroSiteDetailWrapper wrapper);

	WifiWrapper getWifiDetailByNEId(String neId);

	List<NetworkElementWrapper> getAggregatedCountByGeographyLevel(SiteSelectionWrapper siteSelectionWrapper, Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat,
			Integer zoomLevel);

	List<SiteMileStoneDetailWrapper> getSiteMilestoneDetails(String neName);

	Map<String, Long> getKPICountForPolygon(KPISummaryDataWrapper filterConfiguration, Integer zoomLevel);

	Map<String, Long> getCustonmCountForKPI(KPISummaryDataWrapper filterConfiguration);

	List<String> getDistinctDomain();

	List<String> getDistinctVendorByDomain(List<String> domainList);

	List<String> getDistinctVendor();

	List<String> getTechnologyByVendor(List<String> vendorList);

	List<String> getVendorsByType(List<String> neTypes);

	SiteGeographicalDetail getSiteOverviewData(String neName, String neType);

	List<NetworkElementWrapper> getSiteDataForPolygon(Map map, List<List<Double>> polygon);

	List<Tuple> getSiteDataInPolygon(SiteLayerSelection layerSelection, List<List<Double>> polygon);

	List<NetworkElement> getSitesByGeography(SiteSelectionWrapper siteSelectionWrapper);

	List<NETaskDetailWrapper> getNETaskDetailData(Map<String, List<Map>> filters, Map<String, List<String>> projection,	Boolean isGroupBy);

	SiteConnectionPointWrapper getSiteConnectionPointDetails(String neName, String neType);

	Map<String, String> getSectorMap(String neName, String neType);

	SiteGeographicalDetail getSectorPropertyDataByBand(String neName, String neFrequency, String neStatus,String neType);

}
