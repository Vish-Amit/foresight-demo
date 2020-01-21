package com.inn.foresight.core.infra.dao;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.Tower;
import com.inn.foresight.core.infra.model.TowerProvider;
import com.inn.foresight.core.infra.wrapper.TowerProviderWrapper;

public interface ITowerProviderDao extends IGenericDao<Integer, TowerProvider>  {

	List<TowerProviderWrapper> getCountOfTowersByProvider(Double southWestLong, Double southWestLat,Double northEastLong, Double northEastLat, String geographyLevel, List<String> geographyList,
			List<String> name);

	List<Tower> getTowerDetailsByTowerId(String towerId);

	List<Tower> getTowerDetailsByProvider(List<String> name, Double minLatitude, Double maxLatitude,Double minLongitude, Double maxLongitude);

	List<TowerProviderWrapper> getTowerDetailsByGeography(List<String> providersList,Map<String, List<String>> geographyNames);

	List<TowerProviderWrapper> getAllTowerProviderDetails();

}
