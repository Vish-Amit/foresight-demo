package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.foresight.core.infra.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.core.infra.wrapper.TowerProviderWrapper;

public interface ITowerProviderService {

	List<TowerProviderWrapper> getTowerDetailsForProviders(Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, List<String> name, Integer displayTowers, Integer zoomLevel);

	Map<String, Long> getTowerCountsInsidePolygon(KPISummaryDataWrapper filterConfiguration, Integer zoomLevel);

	Map<String, Long> getTowerCountsForProviders(KPISummaryDataWrapper filterConfiguration, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel);

	Map<String, Long> getTowerCountsForGeographies(KPISummaryDataWrapper filterConfiguration);

    Map<String, String> getTowerDetailReport(String towerName);

	List<TowerProviderWrapper> getAllTowerProviderDetails();
}
