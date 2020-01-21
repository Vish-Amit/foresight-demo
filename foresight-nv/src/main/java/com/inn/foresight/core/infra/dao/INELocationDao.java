package com.inn.foresight.core.infra.dao;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.NELocation;
import com.inn.foresight.core.infra.utils.enums.NELType;
import com.inn.foresight.core.infra.wrapper.NELocationWrapper;

/**
 * The Interface INELocationDao.
 */
public interface INELocationDao extends IGenericDao<Integer, NELocation> {

	public NELocation getNELocationByNEId(String neId);

	public List<NELocation> searchNELocationByNELIdAndNELType(String searchNELId, List<NELType> nelTypeList,
			Integer upperLimit, Integer lowerLimit);

	NELocation getNELocationDetailsByName(String name);

	List<Object[]> getNELocationListByNELType(String nelType);

	List<Map<String, Object>> getSearchNELocationName(String locationName, List<String> nelType);

	List<NELocationWrapper> getNELocationsDataAndCountGeographyWise(String nelType, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, String geographyLevel);

	List<NELocationWrapper> getNELocationDataByNELType(Double southWestLong, Double southWestLat, Double northEastLong,
			Double northEastLat, String nelType);

	List<NELocationWrapper> getNELocationDataAndCount(String geograhyLevel, String nelType, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat);

	List<NELocation> getCDCList(String type);

	NELocation getNELocationDetailsByNameAndType(String name, NELType type);

	Long checkUniqueLocationCode(String locationCode, Integer nelPk);

	Long checkUniqueLocationName(String nelId, Integer nelPk);

	List<NELocation> getNeLocationByNelidListAndType(List<String> nelIdList, NELType nelType);

	public List<Object[]> getNELocationDetailByNELType(String nelType);

	List<NELocation> getNeLocationByLocationCodeListAndType(List<String> locationCodeList, NELType nelType);
	List<NELocation> filterNELocationByNameAndStatus(String gcName, String nelStatus, String locationCode,
			Integer ulimit, Integer llimit, String orderBy, String orderType, String creationTime,
			String modificationTime, Boolean likeFlag);

	public Long getGCOnAirCount(String nelType, String nelStatus);

	List<Object[]> getNeLocationAndParamterByType(String nelType,Integer llimit,Integer ulimit);

	Long getTotalNelocationCountByType(String neltype);

	NELocation getNELocationByLocationCode(String locationCode);


	public List<String> filterByLocationCode(String locationcode);

	List<NELocationWrapper> getGCCountByStatusAndGeography(List<String> nelType, List<String> nelStatus);

	List<NELocationWrapper> getGCDetailsByStatusAndGeography(List<String> nelType, List<String> nelStatus,
			String vcuNEType, String vduNEType, String macroNEType, String geographyName, Integer llimit,
			Integer ulimit);

	List<NELocationWrapper> getNELocationByGeographyId(Integer geoId, String geoType,NELType nelType);

	List<NELocationWrapper> getGeographyWiseNELocationCountByGeographyId(String nelType, Integer geoId, String geoType);

}
