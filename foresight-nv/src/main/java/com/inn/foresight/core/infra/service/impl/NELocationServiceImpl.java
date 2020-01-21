package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.INELocationDao;
import com.inn.foresight.core.infra.dao.INELocationParameterDao;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.model.NELocation;
import com.inn.foresight.core.infra.model.NELocationParameter;
import com.inn.foresight.core.infra.service.IAdvanceSearchProvider;
import com.inn.foresight.core.infra.service.INELocationParameterService;
import com.inn.foresight.core.infra.service.INELocationService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.NELType;
import com.inn.foresight.core.infra.wrapper.NELocationWrapper;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;

/**
 * The Class NELocationServiceImpl.
 */
@Service("NELocationServiceImpl")
public class NELocationServiceImpl extends AbstractService<Integer, NELocation>
		implements INELocationService, IAdvanceSearchProvider {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(NELocationServiceImpl.class);

	/** The n E location dao. */
	@Autowired
	private INELocationDao nELocationDao;

	@Autowired
	private INELocationParameterService nELocationParameterService;
	@Autowired
	private INELocationParameterDao neLocationParameterDao;

	/** The geography L 1 zoom. */
	static Integer geographyL1Zoom = 0;

	/** The geography L 2 zoom. */
	static Integer geographyL2Zoom = 0;

	/** The geography L 3 zoom. */
	static Integer geographyL3Zoom = 0;

	/** The geography L 4 zoom. */
	static Integer geographyL4Zoom = 0;

	static Map<String, String> systemConfigMap = SystemConfigurationUtils.systemConfMap;

	/**
	 * Sets the dao.
	 *
	 * @param nELocationDao the new dao
	 */
	public void setDao(INELocationDao nELocationDao) {
		super.setDao(nELocationDao);
		this.nELocationDao = nELocationDao;
	}

	@Override
	public List<NELocation> searchNELocationByNELIdAndNELType(String searchNELId, List<NELType> nelTypeList,
			Integer upperLimit, Integer lowerLimit) {
		return nELocationDao.searchNELocationByNELIdAndNELType(searchNELId, nelTypeList, upperLimit, lowerLimit);
	}

	@Override
	public List<NELocationWrapper> getNELocationDataByNELType(String nelType, Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat) {
		List<NELocationWrapper> neLocationData = new ArrayList<>();
		try {
			logger.info("Going top get distinct NELocation for nelType {} ", nelType);
			neLocationData = nELocationDao.getNELocationDataByNELType(southWestLong, southWestLat, northEastLong,
					northEastLat, nelType);

		} catch (Exception exception) {
			logger.error("Unable to get NELocation For nelType {} due to exception : {}", nelType,
					Utils.getStackTrace(exception));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}

		return neLocationData;
	}

	@Override
	public List<NELocationWrapper> getNELocationsDataAndCountByGeographyWise(String nelType, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel) {

		try {
			String geographyLevel = getGeographyForAggregation(zoomLevel);

			logger.debug("geographyLevel {}", geographyLevel);
			return nELocationDao.getNELocationsDataAndCountGeographyWise(nelType, southWestLong, southWestLat,
					northEastLong, northEastLat, geographyLevel);

		} catch (Exception exception) {
			logger.error(
					"Unable to get getInventoryLocationsDataAndCountByGeographyWise For locationPk {} due to exception {}",
					nelType, Utils.getStackTrace(exception));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public List<NELocationWrapper> getNELocationsDataAndCountOnViewPort(String nelType, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, Integer zoomLevel) {
		try {
			String geographyLevel = getGeographyForAggregation(zoomLevel);

			logger.debug("geographyLevel {}", geographyLevel);
			return getNELocationDataAndCount(geographyLevel, nelType, southWestLong, southWestLat, northEastLong,
					northEastLat);
		} catch (Exception exception) {
			logger.error(
					"Unable to get getInventoryLocationsDataAndCountByGeographyWise For locationPk {} due to exception {}",
					nelType, Utils.getStackTrace(exception));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Gets the geography for aggregation.
	 *
	 * @param zoomLevel the zoom level
	 * @return the geography for aggregation
	 * @throws RestException the rest exception
	 */
	public static String getGeographyForAggregation(Integer zoomLevel) {

		logger.info("systemConfigMap : {}   ", systemConfigMap);

		try {
			geographyL1Zoom = Utils.toInteger(systemConfigMap.get(ForesightConstants.GEOGRAPHYL1_ZOOM));
			geographyL2Zoom = Utils.toInteger(systemConfigMap.get(ForesightConstants.GEOGRAPHYL2_ZOOM));
			geographyL3Zoom = Utils.toInteger(systemConfigMap.get(ForesightConstants.GEOGRAPHYL3_ZOOM));
			geographyL4Zoom = Utils.toInteger(systemConfigMap.get(ForesightConstants.GEOGRAPHYL4_ZOOM));

			/*
			 * geographyL1Zoom = Utils.toInteger("5"); geographyL2Zoom =
			 * Utils.toInteger("7"); geographyL3Zoom = Utils.toInteger("9"); geographyL4Zoom
			 * = Utils.toInteger("11");
			 */

			logger.info("geographyL1Zoom  {} : " + geographyL1Zoom);
			logger.info("geographyL2Zoom  {} : " + geographyL2Zoom);
			logger.info("geographyL3Zoom  {} : " + geographyL3Zoom);
			logger.info("geographyL4Zoom  {} : " + geographyL4Zoom);

			if (zoomLevel >= geographyL1Zoom && zoomLevel < geographyL2Zoom) {
				return InfraConstants.GEOGRAPHYL1;
			}
			if (zoomLevel >= geographyL2Zoom && zoomLevel < geographyL3Zoom) {
				return InfraConstants.GEOGRAPHYL2;
			}
			if (zoomLevel >= geographyL3Zoom && zoomLevel < geographyL4Zoom) {
				return InfraConstants.GEOGRAPHYL3;
			}
			if (zoomLevel >= geographyL4Zoom) {
				return InfraConstants.GEOGRAPHYL4;
			}

			logger.info("ForesightConstants.BLANK_STRING {} ", ForesightConstants.BLANK_STRING);
			return ForesightConstants.BLANK_STRING;
		} catch (Exception exception) {
			throw new BusinessException(exception.getMessage());
		}
	}

	private List<NELocationWrapper> getNELocationDataAndCount(String geographyLevel, String locationtype,
			Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat) {

		try {
			logger.info("Going top get distinct Geography for geographyLevel {} ", geographyLevel);
			return nELocationDao.getNELocationDataAndCount(geographyLevel, locationtype, southWestLong, southWestLat,
					northEastLong, northEastLat);

		} catch (Exception exception) {
			logger.error("Unable to get geography For geogrphylevel {} due to exception {}", geographyLevel,
					Utils.getStackTrace(exception));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Gets the network inventory list.
	 *
	 * @param maxLimit  the max limit
	 * @param minLimit  the min limit
	 * @param orderby   the orderby
	 * @param orderType the order type
	 * @return the network inventory list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<Map<String, Object>> getNELocationListByNELType(String nelType) {
		logger.info("Inside getInventoryLocationList");
		List<Map<String, Object>> neLocationList = new ArrayList<>();
		try {
			List<Object[]> neLocationObjList = nELocationDao.getNELocationListByNELType(nelType);
			for (Object[] objects : neLocationObjList) {
				Map<String, Object> map = new HashMap<>();
				map.put(ForesightConstants.ID, objects[0]);
				map.put(ForesightConstants.NAME, objects[1]);

				neLocationList.add(map);
			}

		} catch (Exception ex) {
			logger.error("Unable to get InventoryLocation For locationType {} due to exception {}", nelType,
					Utils.getStackTrace(ex));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
		return neLocationList;
	}

	@Override
	public List<Map<String, Object>> getNELocationDetailByNELType(String nelType) {
		logger.info("Inside getInventoryLocationList");
		List<Map<String, Object>> neLocationList = new ArrayList<>();
		try {
			List<Object[]> neLocationObjList = nELocationDao.getNELocationDetailByNELType(nelType);
			for (Object[] objects : neLocationObjList) {
				Map<String, Object> map = new HashMap<>();
				map.put(ForesightConstants.ID, objects[0]);
				map.put(ForesightConstants.NAME, objects[1]);

				neLocationList.add(map);
			}

		} catch (Exception ex) {
			logger.error("Unable to get InventoryLocation For locationType {} due to exception {}", nelType,
					Utils.getStackTrace(ex));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
		return neLocationList;
	}

	@Override
	public Object getSearchData(Map<String, Object> map) {
		Map<String, Object> siteDetailMap = new HashMap<>();
		try {
			AdvanceSearch search = (AdvanceSearch) map.get(InfraConstants.ADVANCESEARCH);
			NELocation neLocationSearchObj = nELocationDao.getNELocationDetailsByNameAndType(search.getName(),
					NELType.valueOf(search.getAdvanceSearchConfiguration().getType()));
			if (neLocationSearchObj != null && neLocationSearchObj.getNelId() != null
					&& neLocationSearchObj.getLongitude() != null && neLocationSearchObj.getLatitude() != null) {
				siteDetailMap.put(ForesightConstants.NEL_ID, neLocationSearchObj.getNelId());
				siteDetailMap.put(ForesightConstants.NEL_TYPE, neLocationSearchObj.getNelType());
				siteDetailMap.put(ForesightConstants.LATITUDE, neLocationSearchObj.getLatitude());
				siteDetailMap.put(ForesightConstants.LONGITUDE, neLocationSearchObj.getLongitude());
			} else {
				throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
			}
		} catch (BusinessException ex) {
			logger.info("Error while getting Invenory data detail. Exception :{}", ExceptionUtils.getStackTrace(ex));
			throw new BusinessException(ex.getMessage());
		} catch (Exception exception) {
			logger.error("Error while getting Invenory data detail. Exception :{} ",
					ExceptionUtils.getStackTrace(exception));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}

		return siteDetailMap;
	}

	@Override
	public List<Map<String, Object>> getSearchNELocationName(String locationName, List<String> nelType) {
		logger.error("going to get Search locationName = {} and nelType ={}", locationName, nelType);
		try {
			return nELocationDao.getSearchNELocationName(locationName, nelType);
		} catch (Exception ex) {
			logger.error("Unable to Search locationName due to exception {}", ExceptionUtils.getStackTrace(ex));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Search with filter.
	 *
	 * @param ctx       the ctx
	 * @param maxLimit  the max limit
	 * @param minLimit  the min limit
	 * @param orderby   the orderby
	 * @param orderType the order type
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NELocation> searchWithFilter(SearchContext ctx, Integer maxLimit, Integer minLimit, String orderby,
			String orderType) {
		try {
			logger.info("Inside @searchWithFilter @Param: Maxlimit: " + maxLimit + " minlimit: " + minLimit
					+ " orderby: " + orderby + " ordertype: " + orderType);
			return nELocationDao.search(ctx, maxLimit, minLimit, orderby, orderType);
		} catch (Exception ex) {
			logger.error("Inside @Method: searchWithFilter Error  occurred", ex);
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Map<String, Object> getNeLocationWithParametersById(Integer nelId) {
		Map<String, Object> dataMap = new HashMap<>();
		try {
			if (nelId == null)
				throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
			else {
				NELocation neLocation = nELocationDao.findByPk(nelId);
				if (neLocation == null)
					throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
				else {
					dataMap.put(ForesightConstants.NELOCATION, neLocation);
					dataMap.put(ForesightConstants.PARAMETER,
							nELocationParameterService.getParameterAndKeyByNeLocationId(nelId));
				}
			}
		} catch (BusinessException ex) {
			logger.error("Inside @Method: getNeLocationWithParametersById Error occurred",
					ExceptionUtils.getStackTrace(ex));
			throw new BusinessException(ex.getMessage());
		} catch (Exception ex) {
			logger.error("Inside @Method: getNeLocationWithParametersById Error:{}  occurred for nelId:{}",
					ExceptionUtils.getStackTrace(ex), nelId);
		}
		return dataMap;
	}

	@Override
	public Map<String,Object> getNeLocationAndParamterByType(String nelType,Integer llimit,Integer ulimit) {
		logger.info("Going to get NELocation and Parameter By Type:{}", nelType);
		Map<String,Object> finalMap = new HashMap<>();
		List<Map<String, Object>> finalData = new ArrayList<>();
		Long total = null;
		try {
			List<Object[]> neLocationList = nELocationDao.getNeLocationAndParamterByType(nelType,llimit,ulimit);
			if (!neLocationList.isEmpty() && llimit != null && llimit.equals(0)) {
				total = nELocationDao.getTotalNelocationCountByType(nelType);
			}
			Map<Integer, Map<String, Object>> dataMap = new HashMap<>();
			for (Object[] obj : neLocationList) {
				Map<String, Object> innerMap = new HashMap<>();
				Integer pk = (Integer) obj[0];
				Date creationtime = (Date) obj[6];
				Date modificationTime = (Date) obj[7];
				if (dataMap.containsKey(pk)) {
					dataMap.get(pk).put("locationId", obj[0]);
					dataMap.get(pk).put(ForesightConstants.NEL_ID, obj[1]);
					dataMap.get(pk).put(ForesightConstants.LOCATION_CODE, obj[2]);
					if (obj[3] != null && StringUtils.equalsIgnoreCase("floorNumber", obj[3].toString())) {
						dataMap.get(pk).put("floorNumber", obj[4]);
					}
					if (obj[3] != null && StringUtils.equalsIgnoreCase("managedByGc", obj[3].toString())) {
						dataMap.get(pk).put("managedByGc", obj[4]);
					}
					if(obj[3] != null && StringUtils.equalsIgnoreCase("networkserviceid", obj[3].toString())) {
						dataMap.get(pk).put("id", obj[4]);
					}
					dataMap.get(pk).put(ForesightConstants.STATUS, obj[5]);
					dataMap.get(pk).put(ForesightConstants.CREATED_TIME,creationtime!=null?creationtime.getTime():creationtime);
					dataMap.get(pk).put(ForesightConstants.MODIFIEDTIME, modificationTime!=null?modificationTime.getTime():modificationTime);
				} else {
					innerMap.put("locationId", obj[0]);
					innerMap.put(ForesightConstants.NEL_ID, obj[1]);
					innerMap.put(ForesightConstants.LOCATION_CODE, obj[2]);
					if (obj[3] != null && StringUtils.equalsIgnoreCase("floorNumber", obj[3].toString())) {
						innerMap.put("floorNumber", obj[4]);
					}
					if (obj[3] != null && StringUtils.equalsIgnoreCase("managedByGc", obj[3].toString())) {
						innerMap.put("managedByGc", obj[4]);
					}
					if(obj[3] != null && StringUtils.equalsIgnoreCase("networkserviceid", obj[3].toString())) {
						innerMap.put("id", obj[4]);
					}
					innerMap.put(ForesightConstants.STATUS, obj[5]);
					innerMap.put(ForesightConstants.CREATED_TIME,creationtime!=null?creationtime.getTime():creationtime);
					innerMap.put(ForesightConstants.MODIFIEDTIME, modificationTime!=null?modificationTime.getTime():modificationTime);
					dataMap.put(pk, innerMap);
				}
			}
			finalData = new ArrayList<>(dataMap.values());
			finalMap.put("data", finalData);
			finalMap.put("count", total);
			return finalMap;
		} catch (Exception exception) {
			logger.error("Exception caught while getting Nelocation Data,Exception:{}", exception.getMessage());
		}
		return finalMap;
	}
	
	@Override
	@Transactional
	public List<Map<String, Object>> searchGC(String gcName, String nelStatus, String locationCode, Integer ulimit,
			Integer llimit, String orderBy, String orderType, String creationTime, String modificationTime,Boolean likeFlag) {
		logger.debug(
				"inside @searchGC method having :gcName:{},nelStatus:{},locationCode:{},ulimit:{},llimit:{},orderBy:{},orderType:{}",
				gcName, nelStatus, locationCode, ulimit, llimit, orderBy, orderType);
		List<Map<String, Object>> gcListOfMap = new ArrayList<>();
		try {
			List<NELocation> neLocationList = nELocationDao.filterNELocationByNameAndStatus(gcName, nelStatus,
					locationCode, ulimit, llimit, orderBy, orderType, creationTime, modificationTime, checkLikeFlag(likeFlag));
			if (CollectionUtils.isNotEmpty(neLocationList)) {
				for (NELocation neLocation : neLocationList) {
					List<NELocationParameter> neLocationParameterList = neLocationParameterDao
							.getNELocationParamByNELId(neLocation.getId());
					Map<String, Object> gcListMap = new HashMap<>();
					gcListMap.put(ForesightConstants.NEL_ID, neLocation.getNelId());
					gcListMap.put(ForesightConstants.LOCATION_CODE, neLocation.getLocationCode());
					gcListMap.put(ForesightConstants.STATUS, neLocation.getNelStatus());
					gcListMap.put(ForesightConstants.ID, neLocation.getId());
					if (neLocation.getGeographyL4() != null) {
						gcListMap.put(ForesightConstants.CLUSTER, neLocation.getGeographyL4().getDisplayName());
					}
					gcListMap.put(ForesightConstants.CREATED_TIME, neLocation.getCreatedTime());
					gcListMap.put(ForesightConstants.MODIFIEDTIME, neLocation.getModifiedTime());
					if (CollectionUtils.isNotEmpty(neLocationParameterList)) {
						for (NELocationParameter neLocationParameter : neLocationParameterList) {
							if (neLocationParameter != null) {
								if ("gcType".equalsIgnoreCase(neLocationParameter.getParameterName())) {
									gcListMap.put("gcType", neLocationParameter.getValue());
								} else if ("managed-by-cdc".equalsIgnoreCase(neLocationParameter.getParameterName())) {
									gcListMap.put("managedbycdc", neLocationParameter.getValue());
								} else if ("gcCompletionTime"
										.equalsIgnoreCase(neLocationParameter.getParameterName())) {
									gcListMap.put("gcCompletionTime", neLocationParameter.getValue());
								} else if ("locationId".equalsIgnoreCase(neLocationParameter.getParameterName())) {
									gcListMap.put("locationId", neLocationParameter.getValue());
								}
							}
						}
					}
					gcListMap.put(ForesightConstants.LATITUDE, neLocation.getLatitude());
					gcListMap.put(ForesightConstants.LONGITUDE, neLocation.getLongitude());
					gcListMap.put(ForesightConstants.ADDRESS, neLocation.getAddress());
					gcListOfMap.add(gcListMap);
				}
				return gcListOfMap;
			} else {
				logger.info("NO GC Found For GCname :{} and nelStatus :{}", gcName, nelStatus);
				return Collections.emptyList();
			}
		} catch (Exception exception) {
			logger.error("Exception Occured while searching GC for gcname :{} and nelstatus :{} Exception is :{}",
					gcName, nelStatus, ExceptionUtils.getStackTrace(exception));
			throw new RestException("No Record Found For GC ");
		}

	}

	private Boolean checkLikeFlag(Boolean likeFlag) {
		return likeFlag != null ? likeFlag : false;
	}

	@Override
	public NELocation getNELocationByNelId(String nelId) {
		logger.info("inside @getNELocationByNEId method nelocationid :{}", nelId);
		NELocation neLocation = new NELocation();
		try {
			neLocation = nELocationDao.getNELocationByNEId(nelId);
		} catch (Exception e) {
			logger.error("Exception in getNELocationByNEId: error={}", Utils.getStackTrace(e));
		}
		return neLocation;
	}
	
	@Override
	public NELocation getNELocationByLocationCode(String locationCode) {
		logger.info("inside @getNELocationByLocationCode method locationCode :{}", locationCode);
		NELocation neLocation = new NELocation();
		try {
			neLocation = nELocationDao.getNELocationByLocationCode(locationCode);
		} catch (Exception e) {
			logger.error("Exception in getNELocationByLocationCode: error={}", Utils.getStackTrace(e));
		}
		return neLocation;
	}

	@Override
	public List<String> filterByLocationCode(String locationcode) {
		logger.info("inside filterByLocationCode method and locationcode is :{}", locationcode);
		List<String> filterLCode = new ArrayList<>();
		try {
			filterLCode = nELocationDao.filterByLocationCode(locationcode);
		} catch (Exception e) {
			logger.error("Exception in filterByLocationCode: error={}", Utils.getStackTrace(e));
		}
		return filterLCode;
	}

	@Override
	public List<NELocationWrapper> getNELocationByGeographyId(Integer geoId, String geoType,NELType nelType) {
		List<NELocationWrapper> dataList=new ArrayList<NELocationWrapper>();
		try {
			dataList=nELocationDao.getNELocationByGeographyId(geoId, geoType,nelType);
		} catch (Exception e) {
			logger.error("Exception in getNELocationByGeographyId geoId={} ,geoType={} nelType={},error={}",geoId,geoType,nelType,e.getMessage());
		}
		return dataList;
	}

	@Override
	public List<NELocationWrapper> getGeographyWiseNELocationCountByGeographyId(String nelType, Integer geoId,
			String geoType) {
		List<NELocationWrapper> dataList=new ArrayList<NELocationWrapper>();
		try {
			dataList=nELocationDao.getGeographyWiseNELocationCountByGeographyId(nelType, geoId, geoType);
		} catch (Exception e) {
			logger.error("Exception in getGeographyWiseNELocationCountByGeographyId: nelType={}, geoId={}, geoType={}, error={}",nelType, geoId, geoType,e.getMessage());
		}
		return dataList;
	}
	
	@Override
	public Integer getNELocationCountByGeographyId(Integer geoId, String geoType,NELType nelType) {
		Integer dataList=0;
		try {
			List<NELocationWrapper> neLocationByGeographyId = nELocationDao.getNELocationByGeographyId(geoId, geoType,nelType);
			if(neLocationByGeographyId!=null && !neLocationByGeographyId.isEmpty()){
				dataList = neLocationByGeographyId.size();
			}
		} catch (Exception e) {
			logger.error("Exception in getNELocationCountByGeographyId geoId={} ,geoType={} nelType={},error={}",geoId,geoType,nelType,ExceptionUtils.getStackTrace(e));
		}
		return dataList;
	}

	@Override
	public Map<String, Object> getNELocationInfoByNelidAndNeltype(String nelId, NELType nelType) {
		Map<String, Object> map = new HashMap<>();
		try {
			if ((nelId != null) && (nelType != null)) {
				NELocation neLocationDetailsByNameAndType = nELocationDao.getNELocationDetailsByNameAndType(nelId,
						nelType);
				if (neLocationDetailsByNameAndType != null) {
					map.put("geographyL3Name", neLocationDetailsByNameAndType.getGeographyL3() != null
							? neLocationDetailsByNameAndType.getGeographyL3().getDisplayName() : null);
					map.put("geographyL2Name", neLocationDetailsByNameAndType.getGeographyL2() != null
							? neLocationDetailsByNameAndType.getGeographyL2().getDisplayName() : null);
					map.put("geographyL1Name", neLocationDetailsByNameAndType.getGeographyL1() != null
							? neLocationDetailsByNameAndType.getGeographyL1().getDisplayName() : null);
					map.put("geographyL4Name", neLocationDetailsByNameAndType.getGeographyL4() != null
							? neLocationDetailsByNameAndType.getGeographyL4().getDisplayName() : null);
					map.put("nelId", neLocationDetailsByNameAndType.getNelId());
					map.put("latitude", neLocationDetailsByNameAndType.getLatitude());
					map.put("longitude", neLocationDetailsByNameAndType.getLongitude());
					map.put("nelType", neLocationDetailsByNameAndType.getNelType());
					map.put("locationCode", neLocationDetailsByNameAndType.getLocationCode() != null
							? neLocationDetailsByNameAndType.getLocationCode() : null);
					map.put("address", neLocationDetailsByNameAndType.getAddress() != null
							? neLocationDetailsByNameAndType.getAddress() : null);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getNELocationInfoByNelidAndNeltype {}", Utils.getStackTrace(e));
		}
		return map;
	}


}
