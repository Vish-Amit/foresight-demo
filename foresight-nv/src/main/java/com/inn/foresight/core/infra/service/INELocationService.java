package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxrs.ext.search.SearchContext;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.NELocation;
import com.inn.foresight.core.infra.utils.enums.NELType;
import com.inn.foresight.core.infra.wrapper.NELocationWrapper;

/**
 * The Interface INELocationService.
 */
public interface INELocationService extends IGenericService<Integer, NELocation> {

	List<NELocation> searchNELocationByNELIdAndNELType(String searchNELId, List<NELType> nelTypeList,
			Integer upperLimit, Integer lowerLimit);

	List<NELocationWrapper> getNELocationDataByNELType(String nelType, Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat);

	List<NELocationWrapper> getNELocationsDataAndCountByGeographyWise(String nelType, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel);

	List<NELocationWrapper> getNELocationsDataAndCountOnViewPort(String nelType, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel);

	List<Map<String, Object>> getNELocationListByNELType(String nelType);

	List<Map<String, Object>> getSearchNELocationName(String locationName, List<String> nelType);

	List<NELocation> searchWithFilter(SearchContext context, Integer upperLimit, Integer lowerLimit, String orderBy,
			String orderType);

	Map<String, Object> getNeLocationWithParametersById(Integer nelId);

	List<Map<String, Object>> getNELocationDetailByNELType(String nelType);

	List<Map<String, Object>> searchGC(String gcName, String nelStatus, String locationCode, Integer ulimit,
			Integer llimit, String orderBy, String orderType, String creationTime, String modificationTime, Boolean likeFlag);

	NELocation getNELocationByNelId(String nelId);

	Map<String,Object>  getNeLocationAndParamterByType(String nelType,Integer llimit,Integer ulimit);

	NELocation getNELocationByLocationCode(String locationCode);

	List<String> filterByLocationCode(String locationcode);

	List<NELocationWrapper> getNELocationByGeographyId(Integer geoId, String geoType,NELType nelType);

	List<NELocationWrapper> getGeographyWiseNELocationCountByGeographyId(String nelType, Integer geoId, String geoType);

	Integer getNELocationCountByGeographyId(Integer geoId, String geoType, NELType nelType);

	Map<String, Object> getNELocationInfoByNelidAndNeltype(String nelId, NELType nelType);

}
