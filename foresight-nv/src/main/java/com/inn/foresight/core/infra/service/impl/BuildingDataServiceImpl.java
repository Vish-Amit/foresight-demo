package com.inn.foresight.core.infra.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpException;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.nns.NNS;
import com.inn.commons.unit.Duration;
import com.inn.commons.unit.Length;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.dao.BuildingHBaseDao;
import com.inn.foresight.core.infra.dao.IAdvanceSearchConfigurationDao;
import com.inn.foresight.core.infra.dao.IBuildingDataDao;
import com.inn.foresight.core.infra.dao.IFloorDataDao;
import com.inn.foresight.core.infra.dao.IUnitDataDao;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.model.AdvanceSearchConfiguration;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.core.infra.service.IAdvanceSearchProvider;
import com.inn.foresight.core.infra.service.IAdvanceSearchService;
import com.inn.foresight.core.infra.service.IBuildingDataService;
import com.inn.foresight.core.infra.service.IFloorPlanMappingService;
import com.inn.foresight.core.infra.service.IUnitDataService;
import com.inn.foresight.core.infra.service.IWingDataService;
import com.inn.foresight.core.infra.utils.InBuildingUtils;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.wrapper.BuildingPolygonWrapper;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.core.infra.wrapper.FloorWrapper;
import com.inn.foresight.core.infra.wrapper.GeographyDetailWrapper;
import com.inn.foresight.core.infra.wrapper.UnitWrapper;
import com.inn.foresight.core.infra.wrapper.WingWrapper;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.geography.service.GeographyL4Service;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/** The Class BuildingDataServiceImpl. */
@Service("BuildingDataServiceImpl")
public class BuildingDataServiceImpl extends InBuildingConstants
		implements IBuildingDataService, IAdvanceSearchProvider {
	/** The logger. */
	private Logger logger = LogManager.getLogger(BuildingDataServiceImpl.class);
	/** The service. */
	@Autowired
	private GeographyL4Service iGeographyL4Service;

	/** The i generic map service. */
	@Autowired
	private IGenericMapService iGenericMapService;

	/** The i building data dao. */
	@Autowired
	private IBuildingDataDao iBuildingDataDao;

	/** The i wing data service. */
	@Autowired
	private IWingDataService iWingDataService;

	/** The i unit data service. */
	@Autowired
	private IUnitDataService iUnitDataService;

	/** The advance search service. */
	@Autowired
	private IAdvanceSearchService advanceSearchService;

	@Autowired
	private GeographyL4Service l4dao;

	@Autowired
	private IAdvanceSearchConfigurationDao iAdvanceSearchConfigurationDao;

	@Autowired
	private IFloorPlanMappingService iFloorPlanMappingService;

	@Autowired
	private IFloorDataDao iFloorDataDao;

	@Autowired
	private BuildingHBaseDao buildingHBaseDao;

	@Autowired
	private IUnitDataDao iUnitDataDao;

	/**
	 * Creates the building.
	 *
	 * @param buildingWrapper the building wrapper
	 * @return the building wrapper
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public BuildingWrapper createBuilding(BuildingWrapper buildingWrapper) {
		if (buildingWrapper != null) {
			if (buildingWrapper.getBuildingId() != null) {
				Building buildingData = updateExistingBuilding(buildingWrapper);
				iWingDataService.createOrUpdateWingList(buildingData, buildingWrapper);
			} else {
				createNewBuilding(buildingWrapper);
			}
		}
		return buildingWrapper;
	}

	/**
	 * Creates the new building.
	 *
	 * @param buildingWrapper the building wrapper
	 * @throws RestException the rest exception
	 */
	private void createNewBuilding(BuildingWrapper buildingWrapper) {
		logger.info("Going createNewBuilding");
		try {
			if (isBuildingAlreadyExist(buildingWrapper)) {
				throw new RestException(BUILDING_NAME_ADDRESS_ALREADY_EXIST);
			}
			User loggedInUser = UserContextServiceImpl.getUserInContext();
//			if (loggedInUser == null) {
//				throw new RestException(USER_NOT_FOUND);
//			}
			GeographyL4 geographyL4 = getGeographyL4(buildingWrapper);
			if (geographyL4 == null) {
				throw new RestException(CLUSTER_NOT_FOUND);
			}
			Building buildingData = new Building();
			buildingData = InBuildingUtils.getBuildingData(buildingWrapper, buildingData);
			buildingData.setGeographyL4(geographyL4);
			if (loggedInUser != null) {
				buildingData.setCreatedBy(loggedInUser);
			}
			buildingData = iBuildingDataDao.createBuilding(buildingData);
			persistAdvanceSearchDetails(buildingData);
			buildingWrapper.setBuildingId(buildingData.getId());
			iWingDataService.createOrUpdateWingList(buildingData, buildingWrapper);
		} catch (DaoException e) {
			logger.error("DaoException in createNewBuilding:{}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		} catch (Exception e) {
			logger.error("Exception in createNewBuilding:{}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	private boolean isBuildingAlreadyExist(BuildingWrapper buildingWrapper) {
		Building building = null;
		try {
			building = iBuildingDataDao.getBuildingByNameAndAddress(buildingWrapper.getBuildingName(),
					buildingWrapper.getAddress());
		} catch (DaoException e) {
			logger.error("DaoException in isBuildingAlreadyExist:{}", ExceptionUtils.getStackTrace(e));
		}
		return building != null;
	}

	/**
	 * Persist advance search details.
	 *
	 * @param buildingData the building data
	 * @throws RestException the rest exception
	 */
	@Override
	public void persistAdvanceSearchDetails(Building buildingData) {
		logger.info("Going to create advanceSearch entry for building id : {} name : {}", buildingData.getId(),
				buildingData.getBuildingName());
		AdvanceSearch advanceSearch = new AdvanceSearch();
		advanceSearch.setName(
				buildingData.getBuildingName() + Symbol.COMMA_STRING + Symbol.SPACE_STRING + buildingData.getAddress());
		AdvanceSearchConfiguration advanceSearchConfiguration = iAdvanceSearchConfigurationDao
				.getAdvanceSearchConfigurationByType(BUILDING);
		advanceSearch.setAdvanceSearchConfiguration(advanceSearchConfiguration);
		advanceSearch.setTypereference(buildingData.getId());
		advanceSearch.setPriorityValue(BUILDING_ADVANCE_SEARCH_PRIORITY_VALUE);
		advanceSearch.setCreationTime(new Date());
		advanceSearch.setModificationTime(new Date());
		advanceSearch.setDisplayName(advanceSearch.getName());		
		advanceSearch = advanceSearchService.create(advanceSearch);
		logger.info("Created advanceSearch entry for building id : {}", advanceSearch.getId());
	}

	public void updateAdvanceSearchDetails(Building buildingData, String oldBuildingName, String oldAddress) {
		logger.info("Going to update advanceSearch entry for building id : {} name : {}", buildingData.getId(),
				buildingData.getBuildingName());
		List<AdvanceSearch> list = advanceSearchService
				.getAdvanceSearchByName(oldBuildingName + Symbol.COMMA_STRING + Symbol.SPACE_STRING + oldAddress);
		if (list != null && !list.isEmpty()) {
			AdvanceSearch advanceSearch = list.get(FIRST_INDEX);
			advanceSearch.setName(buildingData.getBuildingName() + Symbol.COMMA_STRING + Symbol.SPACE_STRING
					+ buildingData.getAddress());
			AdvanceSearchConfiguration advanceSearchConfiguration = iAdvanceSearchConfigurationDao
					.getAdvanceSearchConfigurationByType(BUILDING);
			advanceSearch.setAdvanceSearchConfiguration(advanceSearchConfiguration);
			advanceSearch.setModificationTime(new Date());
			advanceSearch = advanceSearchService.update(advanceSearch);
			logger.info("Updated advanceSearch entry for  advanceSearch id : {}", advanceSearch.getId());
		}
	}

	/**
	 * Update existing building.
	 *
	 * @param buildingWrapper the building wrapper
	 * @return the building
	 * @throws RestException the rest exception
	 */
	private Building updateExistingBuilding(BuildingWrapper buildingWrapper) {
		Building buildingData = null;
		String oldBuildingName;
		String oldAddress;
		try {
			buildingData = iBuildingDataDao.findByPk(buildingWrapper.getBuildingId());
			oldBuildingName = buildingData.getBuildingName();
			oldAddress = buildingData.getAddress();
			buildingData = iBuildingDataDao
					.updateBuilding(InBuildingUtils.getBuildingData(buildingWrapper, buildingData));
			updateAdvanceSearchDetails(buildingData, oldBuildingName, oldAddress);
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return buildingData;
	}

	/**
	 * Gets the building list by cluster.
	 *
	 * @param id the id
	 * @return the building list by cluster
	 * @throws RestException the rest exception
	 */

	@Override
	public BuildingWrapper getBuildingDetailsByPk(Integer id) {
		logger.info("Going to get getBuildingDetailsByPk id : {} ", id);
		BuildingWrapper buildingWrapper;
		try {
			buildingWrapper = iBuildingDataDao.findBuildingWrapperByPk(id);
			List<WingWrapper> wingWrapperList = iWingDataService
					.getAllWingWrapperForBuilding(buildingWrapper.getBuildingId());
			buildingWrapper.setWingList(wingWrapperList);
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return buildingWrapper;
	}

	/**
	 * Gets the building details by pk.
	 *
	 * @param cluster the cluster
	 * @return the building details by pk
	 * @throws RestException the rest exception
	 */
	@Override
	public List<BuildingWrapper> getBuildingListByCluster(String cluster) {
		logger.info("Going to get getBuildingListByCluster cluster : {} ", cluster);
		List<BuildingWrapper> buildingList = null;
		if (cluster != null) {
			GeographyL4 geographyL4 = iGeographyL4Service.getGeographyL4ByName(cluster);
			if (geographyL4 != null) {
				try {
					buildingList = iBuildingDataDao.getBuildingListByCluster(geographyL4);
				} catch (DaoException e) {
					logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
				}
			} else {
				throw new RestException(CLUSTER_NOT_FOUND);
			}
		}
		return buildingList;
	}

	/**
	 * Get GeographyL4.
	 *
	 * @param building the building
	 * @return the geography L 4
	 * @throws RestException
	 */
	public GeographyL4 getGeographyL4(BuildingWrapper building) {
		try {
			String geographyL4Name = getGeographyName(building);
			logger.info("geographyL4Name:{}", geographyL4Name);
			if (geographyL4Name == null || "".equalsIgnoreCase(geographyL4Name)) {
				throw new RestException(ForesightConstants.GEOGRAPHY_NOT_FOUND);
			}

			Integer geographyL4Id = iBuildingDataDao.getGeographyL4Id(geographyL4Name);
			GeographyL4 geographyL4 = new GeographyL4();
			geographyL4.setId(geographyL4Id);
			return geographyL4;

		} catch (RestException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Gets the geography name.
	 * 
	 * @param building the building
	 * @return the geography name
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("serial")
	private String getGeographyName(BuildingWrapper building) {
		String geograpghy = iGenericMapService.getGeographyDataByPoint(GenericMapUtils.getGeoColumnList(),
				GenericMapUtils.GEOGRAPHY_TABLE_NAME, building.getLatitude(), building.getLongitude(), false,
				GenericMapUtils.L4_TYPE);
		logger.info("response from dropwizard:{}", geograpghy);
		List<List<String>> list = new Gson().fromJson(geograpghy, new TypeToken<List<List<String>>>() {
		}.getType());
		if (list == null || list.isEmpty()) {
			throw new RestException(ForesightConstants.GEOGRAPHY_NOT_FOUND);
		}
		return list.get(0).get(0);
	}

	/**
	 * Get Nearest Building From Location.
	 *
	 * @param latitude  latitude of the location
	 * @param longitude longitude of the location
	 * @return list of BuildingWrapper with nearest six buildings from location
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public List<BuildingWrapper> getNearestBuildingsFromLocation(Double latitude, Double longitude) {
		try {
			List<BuildingWrapper> buildingWrapperList = iBuildingDataDao.getAllBuildings();
			List<BuildingWrapper> nearestBuildingFromLocation = processBuildingWrapperForNearestBuildings(
					buildingWrapperList, latitude, longitude);
			return addWingListToBuildingWrapperData(nearestBuildingFromLocation);
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Processes Building Wrapper For Nearest Buildings.
	 *
	 * @param buildingWrapperList list of all BuildingWrapper from DB
	 * @param latitude            latitude of the location
	 * @param longitude           longitude of the location
	 * @return list of BuildingWrapper with nearest six buildings from location
	 */
	private List<BuildingWrapper> processBuildingWrapperForNearestBuildings(List<BuildingWrapper> buildingWrapperList,
			Double latitude, Double longitude) {

		NNS<BuildingWrapper> jsi = new NNS<>(buildingWrapperList);
		LatLng targetLatLng = new LatLng();
		targetLatLng.setLatitude(latitude);
		targetLatLng.setLongitude(longitude);
		return jsi.getNearestLocations(targetLatLng, Length.meter(NEAREST_BUILDING_RADIUS), NEAREST_BUILDING_COUNT);
	}

	/**
	 * Add Wing List To Building Wrapper Data.
	 *
	 * @param buildingWrapperList list of BuildingWrapper
	 * @return list of BuildingWrapper
	 */
	private List<BuildingWrapper> addWingListToBuildingWrapperData(List<BuildingWrapper> buildingWrapperList) {
		try {
			for (BuildingWrapper buildingWrapper : buildingWrapperList) {
				List<WingWrapper> wingWrapperList = iWingDataService
						.getAllWingWrapperForBuilding(buildingWrapper.getBuildingId());
				buildingWrapper.setWingList(wingWrapperList);
			}
		} catch (RestException e) {
			logger.error("Error while adding Wing data to building: {}", ExceptionUtils.getMessage(e));
		}
		return buildingWrapperList;
	}

	/**
	 * Creates the building along with floor plan.
	 *
	 * @param httpServletRequest the request
	 * @return the building wrapper
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public BuildingWrapper createBuildingAlongWithFloorPlan(HttpServletRequest httpServletRequest) {
		try {
			httpServletRequest.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException {}", Utils.getStackTrace(e));
		}

		logger.info("Going to create building structure along with floorplan");
		BuildingWrapper buildingWrapper = null;
		Boolean isToSaveBuilding = true;
		logger.info("createBuildingAlongWithFloorPlan isToSaveBuilding {} ",
				httpServletRequest.getParameter(QUERY_PARAM_IS_TO_SAVE_BUILDING));
		if (httpServletRequest.getParameter(QUERY_PARAM_IS_TO_SAVE_BUILDING) != null) {
			isToSaveBuilding = Boolean.valueOf(httpServletRequest.getParameter(QUERY_PARAM_IS_TO_SAVE_BUILDING));
			logger.error("isToSaveBuilding {}", isToSaveBuilding);
		} else {
			isToSaveBuilding = true;
		}
		try {
			List<FileItem> fileItemList = InBuildingUtils.extractFileItemsFromHttpRequest(httpServletRequest);
			if (!fileItemList.isEmpty()) {
				logger.info("Multi part list size {}", fileItemList.size());

				Optional<FileItem> request = fileItemList.stream()
						.filter(fileItem -> KEY_DATA.equalsIgnoreCase(fileItem.getFieldName())).findFirst();

				FileItem fileItem = null;
				if (request.isPresent()) {
					fileItem = request.get();
				}
				if (fileItem != null) {
					buildingWrapper = getBuildingWrapperFromRequest(fileItem);
					if (isToSaveBuilding) {
						buildingWrapper = createBuilding(buildingWrapper);
					}
					buildingWrapper = uploadFloorPlan(buildingWrapper, fileItemList);
				} else {
					logger.info(InBuildingConstants.BUILDING_WRAPPER_NOT_EXIST);
					throw new RestException(InBuildingConstants.BUILDING_WRAPPER_NOT_EXIST);
				}
			}
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return buildingWrapper;
	}

	/**
	 * Gets the building wrapper from request.
	 *
	 * @param response the response
	 * @return the building wrapper from request
	 * @throws UnsupportedEncodingException
	 */
	private BuildingWrapper getBuildingWrapperFromRequest(FileItem response) throws UnsupportedEncodingException {
		BuildingWrapper buildingWrapper = null;
		try {
			buildingWrapper = new Gson().fromJson(response.getString("UTF-8").trim(), BuildingWrapper.class);
		} catch (JsonSyntaxException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return buildingWrapper;
	}

	/**
	 * Update unit status.
	 *
	 * @param buildingWrapper the building wrapper
	 */
	private void updateUnitStatus(BuildingWrapper buildingWrapper) {
		for (WingWrapper wingWrapper : buildingWrapper.getWingList()) {
			for (FloorWrapper floorWrapper : wingWrapper.getFloorList()) {
				for (UnitWrapper unitWrapper : floorWrapper.getUnitList()) {
					try {
						Unit unitData = iUnitDataService.findByPk(unitWrapper.getUnitId());
						logger.info("unitData inside updateUnitStatus :  {}", unitData);
						if (unitWrapper.isFloorPlanAvailable()) {
							unitData.setFloorPlanAvailable(unitWrapper.isFloorPlanAvailable());
							iUnitDataService.updateUnit(unitData);
							iFloorPlanMappingService.updateFloorPlanMapping(unitData.getId(),
									InBuildingConstants.TEMPLATE_WORKORDER);
						}
					} catch (RestException e) {
						logger.error("RestException in updateUnitStatus {}", ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
	}

	/**
	 * Gets the building wrapper from result.
	 *
	 * @param result the result
	 * @return the building wrapper from result
	 */
	private BuildingWrapper getBuildingWrapperFromResult(String result) {
		return new Gson().fromJson(result, BuildingWrapper.class);
	}

	/**
	 * Upload floor plan.
	 *
	 * @param buildingWrapper the building wrapper
	 * @param fileItem        the file item
	 * @return the string
	 */
	private BuildingWrapper uploadFloorPlan(BuildingWrapper buildingWrapper, List<FileItem> fileItem) {
		if (fileItem.size() > MULTIPART_LIST_MINIMUM_LENGTH) {
			logger.info("Going to upload floorplan");
			String result = null;
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
			MultipartEntityBuilder multipartEntity = InBuildingUtils.getMultiPartEntity(buildingWrapper, fileItem);
			try {
				String url = InBuildingUtils.getDropwizardUrl(GET_UPLOAD_MULTIPLE_FLOOR_PLAN_URI);
				logger.info("uploadFloorPlan :{} ", url);
				result = InBuildingUtils.sendHttpPostRequest(url, multipartEntity.build(), true, duration).getString();
				logger.info(" floorplan upload result from dropwizard --{}", result);
				if (Utils.hasValidValue(result)) {
					buildingWrapper = getBuildingWrapperFromResult(result);
					updateUnitStatus(buildingWrapper);
				}
			} catch (HttpException e) {
				logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			}
		}
		return buildingWrapper;
	}

	/**
	 * Get Building List By Name.
	 *
	 * @param name name to search buildings
	 * @return list of BuildingWrapper
	 * @throws RestException the rest exception
	 */
	@Override
	public List<BuildingWrapper> getBuildingListByName(String name) {
		List<BuildingWrapper> buildingWrapperListByName;
		logger.info("Going to getBuildingListByName {} ", name);
		try {
			buildingWrapperListByName = iBuildingDataDao.getBuildingsByName(name);
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return addWingListToBuildingWrapperData(buildingWrapperListByName);
	}

	/**
	 * Find by pk.
	 *
	 * @param id the id
	 * @return the building
	 */
	@Override
	public Building findByPk(Integer id) {
		Building building = null;
		try {
			building = iBuildingDataDao.findByPk(id);
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return building;
	}

	/**
	 * Creates the building from app.
	 *
	 * @param buildingData encrypted building wrapper json
	 * @return the building wrapper
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public BuildingWrapper createBuildingData(String buildingData) {
		BuildingWrapper buildingWrapper;
		try {
			logger.info("Going to create new Building: {}", buildingData);
			buildingWrapper = createBuilding(new Gson()
					.fromJson(AuthenticationCommonUtil.checkForValueDecryption(buildingData), BuildingWrapper.class));
		} catch (Exception e) {
			logger.error("Exception in createBuildingData:{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return buildingWrapper;
	}

	/**
	 * Gets the building details for advance search.
	 *
	 * @param buildingId the buildingId
	 * @return Map with building lat, long, address, name.
	 * @throws RestException the rest exception
	 */

	@SuppressWarnings("rawtypes")
	@Override
	public Map getBuildingDetailsForAdvanceSearch(Integer buildingId) {
		logger.info("Going to get getBuildingDetailsByPk id : {} ", buildingId);
		try {
			BuildingWrapper buildingWrapper;
			buildingWrapper = iBuildingDataDao.findBuildingWrapperByPk(buildingId);
			List<WingWrapper> wingWrapperList = iWingDataService
					.getAllWingWrapperForBuilding(buildingWrapper.getBuildingId());
			buildingWrapper.setWingList(wingWrapperList);

			return getBuildingMapFromWrapper(buildingWrapper);

		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	private Map<String, Map<String, String>> getBuildingMapFromWrapper(BuildingWrapper buildingWrapper) {
		Map<String, Map<String, String>> buildingDataMap = new HashMap<>();
		Map<String, String> buildingMap = new HashMap<>();
		buildingMap.put(KEY_NAME, buildingWrapper.getBuildingName());
		buildingMap.put(KEY_ADDRESS, buildingWrapper.getAddress());
		buildingMap.put(KEY_LATITUDE, String.valueOf(buildingWrapper.getLatitude()));
		buildingMap.put(KEY_LONGITUDE, String.valueOf(buildingWrapper.getLongitude()));
		buildingMap.put(KEY_TYPE, String.valueOf(buildingWrapper.getBuildingType()));
		buildingDataMap.put(KEY_BUILDING, buildingMap);
		return buildingDataMap;
	}

	@SuppressWarnings("serial")
	@Override
	@Transactional
	public String tagBuildingGeographyL4() {
		logger.info("Going to tagBuildingGeographyL4");
		String response = InBuildingConstants.SUCCESS_JSON;
		try {
			List<Building> buildings = iBuildingDataDao.tagBuildingGeographyL4();
			logger.info("buildings size to tag geographyL4 : {} ", buildings.size());
			buildings.stream().forEach(building -> {
				try {
					String geograpghy = iGenericMapService.getGeographyDataByPoint(GenericMapUtils.getGeoColumnList(),
							GenericMapUtils.GEOGRAPHY_TABLE_NAME, building.getLatitude(), building.getLongitude(),
							false, GenericMapUtils.L4_TYPE);
					logger.info("geograpghy :{}", geograpghy);
					List<List<String>> list = new Gson().fromJson(geograpghy, new TypeToken<List<List<String>>>() {
					}.getType());
					geograpghy = list.get(0).get(0);
					logger.info("geograpghy name :{}", geograpghy);
					GeographyL4 l4 = l4dao.getGeographyL4ByName(geograpghy);
					building.setGeographyL4(l4);
					iBuildingDataDao.updateBuilding(building);
				} catch (Exception e) {
					logger.error(
							"Exception in taging l4 for building {}, building.getLongitude() {},building.getLongitude() {}",
							building.getBuildingName(), building.getLongitude(), building.getLongitude());
				}
			});
		} catch (Exception e) {
			response = InBuildingConstants.FAILURE_JSON;
			logger.error("Exception in tagBuildingGeographyL4 {}", e.getMessage());
		}
		return response;
	}

	@Override
	public Object getSearchData(Map<String, Object> map) {
		if (map != null && !map.isEmpty()) {
			AdvanceSearch advanceSearch = (AdvanceSearch) map.get(InfraConstants.ADVANCESEARCH);
			if (advanceSearch != null && advanceSearch.getTypereference() != null) {
				return getBuildingDetailsForAdvanceSearch(advanceSearch.getTypereference());
			} else {
				throw new RestException("Invalid Reference");
			}
		} else {
			throw new RestException("Invalid Parameter ");
		}
	}

	@Override
	public GeographyDetailWrapper getGeographyDetailByBuildingId(String buildingId) {
		return iBuildingDataDao.getGeographyDetailByBuildingId(buildingId);
	}

	@Override
	public List<BuildingPolygonWrapper> getBuildingPolygonByViewPort(Double swLat, Double swLng, Double neLat,
			Double neLng) {
		List<BuildingPolygonWrapper> buildingPolygonByViewPort = iBuildingDataDao.getBuildingPolygonByViewPort(swLat,
				swLng, neLat, neLng);
		for (BuildingPolygonWrapper buildingPolygonWrapper : buildingPolygonByViewPort) {
			List<List<List<Double>>> polygonAsArrayList = getPolygonAsArrayList(
					buildingPolygonWrapper.getBuildingPolygon());
			buildingPolygonWrapper.setPolygon(polygonAsArrayList);
		}
		return buildingPolygonByViewPort;
	}

	@Override
	public List<FloorWrapper> getFloorDetailsByBuildingId(String buildingId) {
		return iFloorDataDao.getFloorDetailsByBuildingId(buildingId);
	}

	@Override
	public List<UnitWrapper> getUnitDetailsByFloorId(Integer floorId) {
		return iUnitDataDao.getUnitDetailsByFloorId(floorId);
	}

	@Override
	public BuildingWrapper getBuildingDetailByBuildingId(String buildingId) {
		return iBuildingDataDao.getBuildingDetailByBuildingId(buildingId);
	}

	@Override
	public BuildingWrapper getCoverageDetails(String buildingId, Integer floorId) {
		if (floorId != null) {
			return iFloorDataDao.getFloorCoverage(buildingId, floorId);
		} else {
			return iBuildingDataDao.getBuildingCoverage(buildingId);
		}
	}

	@Override
	public byte[] getFloorPlanByUnitId(Integer unitId, String kpi) {
		String rowkey = Utils.getFloorPlanRowKeyWithTemplateType(unitId, InBuildingConstants.TEMPLATE_WORKORDER);
		byte[] floorPlanByRowkey = buildingHBaseDao.getFloorPlanByRowkey(rowkey, kpi);
		if (floorPlanByRowkey != null) {
			return floorPlanByRowkey;
		}
		throw new RestException("data not found for rowkey " + rowkey);
	}

	@Override
	public String getFloorLegendByKpi(Integer unitId, String kpi) {
		String rowkey = Utils.getFloorPlanRowKeyWithTemplateType(unitId, InBuildingConstants.TEMPLATE_WORKORDER);
		String floorLegendByKpi = buildingHBaseDao.getFloorLegendByKpi(rowkey, kpi);
		if (StringUtils.isNotEmpty(floorLegendByKpi)) {
			return floorLegendByKpi;
		}
		throw new RestException("data not found for rowkey " + rowkey);
	}

	@Override
	public Long getNumberOfFloorsByBuildingId(String buildingId) {
		return iFloorDataDao.getNumberOfFloorsByBuildingId(buildingId);
	}

	@Override
	public String getPredictionKpiByUnitId(Integer unitId) {
		String rowkey = Utils.getFloorPlanRowKeyWithTemplateType(unitId, InBuildingConstants.TEMPLATE_WORKORDER);
		String predictionKpi = buildingHBaseDao.getPredictionKpiByRowkey(rowkey);
		logger.info("predictionKpi:{}",predictionKpi);
		if (StringUtils.isNotEmpty(predictionKpi)) {
			return predictionKpi;
		}
		throw new RestException("data not found for rowkey " + rowkey);
	}

	@Override
	public String getBoundsByUnitId(Integer unitId) {
		String rowkey = Utils.getFloorPlanRowKeyWithTemplateType(unitId, InBuildingConstants.TEMPLATE_WORKORDER);
		String bounds = buildingHBaseDao.getBoundsByRowkey(rowkey);
		if (StringUtils.isNotEmpty(bounds)) {
			return bounds;
		}
		throw new RestException("data not found for rowkey " + rowkey);
	}

	@Override
	public List<List<List<Double>>> getPolygonAsArrayList(String polygon) {
		List<List<List<Double>>> polygonAsArrayList = new ArrayList<>();
		if (StringUtils.isNotEmpty(polygon)) {
			String poly = StringUtils.substringAfter(
					StringUtils.substringAfter(StringUtils.substringBeforeLast(polygon, ")"), "("), "(");
			String[] points = poly.split(",\\(");
			for (String point : points) {
				String[] coordinates = StringUtils.substringBefore(point, ")").split(",");
				List<List<Double>> buildingPolygon = new ArrayList<>();
				for (String coordinate : coordinates) {
					List<Double> cordinates = new ArrayList<>();
					String[] latLong = coordinate.split(" ");
					String[] longLat = { latLong[1], latLong[0] };
					for (String value : longLat) {
						cordinates.add(Double.valueOf(value));
					}
					buildingPolygon.add(cordinates);
				}
				polygonAsArrayList.add(buildingPolygon);
			}
		}
		return polygonAsArrayList;
	}

}
