package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.foresight.core.infra.wrapper.SiteLayerSelection;

public interface ISiteVisualizationService {

	Map<String, List<Map>> searchSite(List<SiteLayerSelection> sitelayerselection, Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel);

	List<Map> getSitesDetailForTableView(List<SiteLayerSelection> siteLayerSelection, Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel);

	Map getKPISummaryCount(List<SiteLayerSelection> siteLayerSelection, Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel);

	List searchNEDetail(List<SiteLayerSelection> siteLayerSelectionList);

	Map getKPISummaryData(List<SiteLayerSelection> siteLayerSelection, Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel);

	List searchNetworkElementDetails(List<SiteLayerSelection> siteLayerSelectionList);

	List sectorPropertiesDetails(List<SiteLayerSelection> siteLayerSelectionList);

	Map getSiteGeographicalData(SiteLayerSelection siteLayerSelection);

	List<Map> getNETaskDetailData(SiteLayerSelection siteLayerSelection);

	List<Map> getSiteSummaryOverviewByBand(SiteLayerSelection siteLayerSelection);

	Map<String, Map> getSiteOverviewData(SiteLayerSelection siteLayerSelection);

	List<Map> getSiteSummaryAntennaParametersByBand(SiteLayerSelection siteLayerSelection);

	Map<String, List<Map>> getCellsDataForRAN(SiteLayerSelection siteLayerSelection);

}
