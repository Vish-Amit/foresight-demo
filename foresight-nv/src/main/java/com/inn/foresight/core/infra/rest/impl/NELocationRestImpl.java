package com.inn.foresight.core.infra.rest.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.model.NELocation;
import com.inn.foresight.core.infra.service.INELocationParameterService;
import com.inn.foresight.core.infra.service.INELocationService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.NELType;
import com.inn.foresight.core.infra.wrapper.NELocationWrapper;

/**
 * The Class NELocationRestImpl.
 */

@Path("NELocation")
@Produces("application/json")
@Consumes("application/json")
public class NELocationRestImpl extends AbstractCXFRestService<Integer, NELocation> {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NELocationRestImpl.class);

	/**
	 * Instantiates a new NELocation rest impl.
	 */
	public NELocationRestImpl() {
		super(NELocation.class);
	}

	/** The NE location service. */
	@Autowired
	INELocationService neLocationService;

	@Autowired
	INELocationParameterService neLocationParameterService;

	/** The context. */
	@Context
	private SearchContext context;

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NELocation> findAll() {
		return null;
	}

	/**
	 * Returns the NELocation finding by id.
	 * 
	 * @param id the id
	 * @return the NELocation
	 * @throws RestException the business exception
	 * @parameter id of type Integer
	 * @returns a NELocation record
	 */
	@Override
	public NELocation findById(@QueryParam("") Integer id) {
		logger.info(" Inside  @class :" + this.getClass().getName() + " @Method :findById @Param: id " + id);
		return null;
	}

	/**
	 * Returns the record by searching NELocation name.
	 *
	 * @param NELocationInformation the NE location information
	 * @return the list
	 * @throws RestException the business exception
	 * @parameter NELocation of typecellSite
	 * @returns a list of NELocation record
	 */
	@Override
	@GET
	public List<NELocation> search(@QueryParam("") NELocation NELocationInformation) {
		logger.info(" Inside  @class :" + this.getClass().getName() + " @Method :search @Param: NELocationInformation "
				+ NELocationInformation);
		return new ArrayList<>();
	}

	/**
	 * Creates the.
	 *
	 * @param NELocationInformation the NE location information
	 * @return the NE location
	 * @throws RestException the rest exception
	 */
	@Override
	@POST
	@Path("create")
	public NELocation create(@Valid NELocation NELocationInformation) {
		logger.info("Inside class :" + this.getClass().getName() + " @Method :create @Param: NELocationInformation "
				+ NELocationInformation);
		try {
			NELocationInformation = neLocationService.create(NELocationInformation);
		} catch (Exception e) {
			logger.error("Error occurred : @class" + this.getClass().getName() + " @Method :create", e);
		}
		logger.info("Inside class name :" + this.getClass().getName() + " @Method :create @Return: "
				+ NELocationInformation);
		return NELocationInformation;

	}

	/**
	 * Update.
	 *
	 * @param NELocationInformation the NE location information
	 * @return the NE location
	 * @throws RestException the rest exception
	 */
	@Override
	@POST
	@Path("update")
	public NELocation update(@Valid NELocation NELocationInformation) {
		logger.info("Inside  @class ," + this.getClass().getName() + " @Method :update @Param: NELocationInformation "
				+ NELocationInformation);
		try {
			NELocationInformation = neLocationService.update(NELocationInformation);
		} catch (Exception e) {
			logger.error("Error occurred  @class" + this.getClass().getName() + " @Method :update", e);
		}
		logger.info("Inside class," + this.getClass().getName() + " @Method :update @Return: " + NELocationInformation);
		return NELocationInformation;
	}

	/**
	 * Returns the removed NELocationInformation record get path and delete
	 * NELocationInformation record .
	 *
	 * @param asitetaskInformation the asitetask information
	 * @return true, if successful
	 * @throws RestException the business exception
	 * @parameter valid NELocationInformation entity
	 * @returns a removed NELocationInformation record
	 */

	@Override
	@Path("delete")
	public boolean remove(NELocation asitetaskInformation) {
		logger.info("Inside " + this.getClass().getName() + " @Method :remove @Param: asitetaskInformation "
				+ asitetaskInformation);
		try {
			neLocationService.remove(asitetaskInformation);
		} catch (Exception e) {
			logger.error("Error occurred  @class" + this.getClass().getName() + " @Method :remove", e);
		}
		logger.info("Inside  @class= " + this.getClass().getName() + " @Method :remove @Return: " + true);
		return true;
	}

	/**
	 * method remove audit action get path to remove audit action.
	 * 
	 * @param primaryKey the primary key
	 * @throws RestException the business exception
	 * @parameter id of type Integer in path param
	 */
	@DELETE
	@Override
	@Path("delete/{id}")
	public void removeById(@PathParam("id") Integer primaryKey) {
		try {
			neLocationService.removeById(primaryKey);
		} catch (Exception e) {
			throw new RestException(ExceptionUtil.generateExceptionCode(InfraConstants.REST, "NELocation", e));
		}
	}

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	@Override
	public IGenericService<Integer, NELocation> getService() {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :getService");
		return neLocationService;
	}

	/**
	 * Gets the search context.
	 *
	 * @return the search context
	 */
	@Override
	public SearchContext getSearchContext() {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :getSearchContext");
		return context;
	}

	@POST
	@Path("searchNELocationByNELIdAndNELType")
	public List<NELocation> searchNELocationByNELIdAndNELType(@QueryParam("searchNELId") String searchNELId,
			List<NELType> nelTypeList, @QueryParam("upperLimit") Integer upperLimit,
			@QueryParam("lowerLimit") Integer lowerLimit) {
		logger.info("Going to search NELocation by nelId : {} or nelType list : {}", searchNELId, nelTypeList);
		try {
			if (nelTypeList != null && !nelTypeList.isEmpty()) {
				return neLocationService.searchNELocationByNELIdAndNELType(searchNELId, nelTypeList, upperLimit,
						lowerLimit);
			}
		} catch (Exception e) {
			logger.error("Error in searchNELocationByNELIdAndNELType : {}", Utils.getStackTrace(e));
		}
		return null;
	}

	@GET
	@Path("getNELocationsOnViewPort")
	public List<NELocationWrapper> getNELocationsOnViewPort(@QueryParam("nelType") String nelType,
			@QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("NELat") Double northEastLat) {
		try {
			if (southWestLong != null && southWestLat != null && northEastLong != null && northEastLat != null) {
				logger.info("Going to get getNELocationsOnViewPort nelType={} ", nelType);
				return neLocationService.getNELocationDataByNELType(nelType, southWestLong, southWestLat, northEastLong,
						northEastLat);

			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (BusinessException e) {
			logger.error("BusinessException Occured while invoking getNELocationsOnViewPort for nelType: {}, Cause: {}",
					nelType, Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("error in getNELocationsOnViewPort, err = {}", Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@GET
	@Path("getNELocationsDataAndCountOnViewPort")
	public List<NELocationWrapper> getNELocationsDataAndCountOnViewPort(@QueryParam("nelType") String nelType,
			@QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("NELat") Double northEastLat,
			@QueryParam("zoomLevel") Integer zoomLevel) {
		try {
			if (southWestLong != null && southWestLat != null && northEastLong != null && northEastLat != null
					&& zoomLevel != null && nelType != null) {
				logger.info("Going to get InventoryLocation data and count on view port by zoomLevel={} , nelType={} ",
						zoomLevel, nelType);
				return neLocationService.getNELocationsDataAndCountOnViewPort(nelType, southWestLong, southWestLat,
						northEastLong, northEastLat, zoomLevel);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (BusinessException e) {
			logger.error(
					"BusinessException Occured while invoking getInventoryLocationsDataAndCountByGeographyWise for location type: {}, Cause: {}",
					nelType, Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("error in getInventoryLocationsDataAndCountOnViewPort, err = {}", Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@GET
	@Path("getNELocationsDataAndCountByGeographyWise")
	public List<NELocationWrapper> getNELocationsDataAndCountByGeographyWise(@QueryParam("nelType") String nelType,
			@QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("NELat") Double northEastLat,
			@QueryParam("zoomLevel") Integer zoomLevel) {

		try {
			if (southWestLong != null && southWestLat != null && northEastLong != null && northEastLat != null
					&& zoomLevel != null && nelType != null) {
				logger.info("Going to get InventoryLocations data And count by zoomLevel={} , nelType={} ", zoomLevel,
						nelType);
				return neLocationService.getNELocationsDataAndCountByGeographyWise(nelType, southWestLong, southWestLat,
						northEastLong, northEastLat, zoomLevel);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (BusinessException e) {
			logger.error(
					"BusinessException Occured while invoking getInventoryLocationsDataAndCountByGeographyWise for location type: {}, Cause: {}",
					nelType, Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("error in getInventoryLocationsDataAndCountByGeographyWise, err = {}", Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Gets the network service list.
	 *
	 * @param lowerLimit the lower limit
	 * @param upperLimit the upper limit
	 * @param orderBy    the order by
	 * @param orderType  the order type
	 * @return the network service list
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getNELocationListByNELType")
	public List<Map<String, Object>> getNELocationListByNELType(@QueryParam("nelType") String nelType) {
		try {
			logger.info("Going to fetch NetworkService List for nelType {}", nelType);
			if (nelType != null) {
				return neLocationService.getNELocationListByNELType(nelType);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETER);
			}
		} catch (BusinessException e) {
			logger.error(
					"BusinessException Occured while invoking getInventoryLocationListByLocationType for location Type: {}, Cause: {}",
					nelType, Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("error in getInventoryLocationListByLocationType, err = {}", Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Search.
	 *
	 * @param lowerLimit the lower limit
	 * @param upperLimit the upper limit
	 * @param orderBy    the order by
	 * @param orderType  the order type
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("search")
	@Produces("application/json")
	public List<NELocation> search(@QueryParam("llimit") Integer lowerLimit, @QueryParam("ulimit") Integer upperLimit,
			@QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType) {
		logger.info("Inside @Method :search @Param: lowerLimit {}{}", lowerLimit, " ,upperLimit {}{}", upperLimit,
				" ,orderBy {}{}", orderBy, " ,orderType {}", orderType);
		try {
			return neLocationService.searchWithFilter(context, upperLimit, lowerLimit, orderBy, orderType);
		} catch (Exception ex) {
			logger.error("Error  occurred  @class :ImageRestImpl @method:search ", ex);
			throw new RestException(ex.getMessage());
		}

	}

	@POST
	@Path("getSearchNELocationName")
	public List<Map<String, Object>> getSearchNELocationName(@QueryParam("locationName") String locationName,
			List<String> nelType) {
		try {
			logger.info("Going to fetch NetworkService List for locationName={}, nelType={}",locationName, nelType);
			if (nelType != null && locationName != null) {
				return neLocationService.getSearchNELocationName(locationName, nelType);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETER);
			}
		} catch (BusinessException e) {
			logger.error(
					"BusinessException Occured while invoking getSearchLocationName for location Name: {} ,  location Type : {} , Cause: {}",
					locationName, nelType, Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("error in getSearchLocationName, err = {}", Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Returns the NELocation finding by id.
	 * 
	 * @param id the id
	 * @return the NELocation
	 * @throws RestException the business exception
	 * @parameter id of type Integer
	 * @returns a NELocation record
	 */
	@GET
	@Path("getNeLocationWithParametersById")
	public Map<String, Object> getNeLocationWithParametersById(@QueryParam("nelId") Integer nelId) {
		logger.info(" Inside  @class :" + this.getClass().getName()
				+ " @Method :getNeLocationWithParametersById @Param: nelId " + nelId);
		return neLocationService.getNeLocationWithParametersById(nelId);
	}

	@GET
	@Path("getAllGCList")
	public List<Map<String, Object>> getAllGCList(@QueryParam("name") String name, @QueryParam("status") String status,
			@QueryParam("ulimit") Integer ulimit, @QueryParam("llimit") Integer llimit,
			@QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType,
			@QueryParam("creationTime") String creationTime, @QueryParam("modificationTime") String modificationTime,
			@QueryParam("likeFlag") Boolean likeFlag) {
		try {
			logger.info("Going to fetch GC List for name :{},status:{},ulimit:{},llimit:{},orderBy:{},orderType:{},likeFlag:{}",
					name, status, ulimit, llimit, orderBy, orderType,likeFlag);
			return neLocationService.searchGC(name, status, null, ulimit, llimit, orderBy, orderType, creationTime,
					modificationTime,likeFlag);
		} catch (BusinessException e) {
			logger.error(
					"BusinessException Occured while invoking getSearchLocationName for location Name: {} ,  location Type : {} , Cause: {}",
					Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error in getSearchLocationName, err = {}", Utils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@GET
	@Path("getAllGCTotalCount")
	public Long getAllGCTotalCount(@QueryParam("name") String name, @QueryParam("status") String status,
			@QueryParam("ulimit") Integer ulimit, @QueryParam("llimit") Integer llimit,
			@QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType,
			@QueryParam("creationTime") String creationTime, @QueryParam("modificationTime") String modificationTime) {
		try {
			logger.info("Going to fetch GC List for name :{},status:{},ulimit:{},llimit:{},orderBy:{},orderType:{}",
					name, status, ulimit, llimit, orderBy, orderType);
			List<Map<String, Object>> nelocationListMap = neLocationService.searchGC(name, status, null, ulimit, llimit,
					orderBy, orderType, creationTime, modificationTime,null);
			if (CollectionUtils.isNotEmpty(nelocationListMap)) {
				return Long.valueOf(nelocationListMap.size());
			} else {
				return Long.valueOf(ForesightConstants.ZERO_INT);
			}
		} catch (Exception e) {
			logger.error("error in getSearchLocationName, err = {}", Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}

	}

	@GET
	@Path("getNELocationDetailByNELType")
	public List<Map<String, Object>> getNELocationDetailByNELType(@QueryParam("nelType") String nelType) {
		try {
			logger.info("Going to fetch NetworkService List for nelType {}", nelType);
			if (nelType != null) {
				return neLocationService.getNELocationDetailByNELType(nelType);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETER);
			}
		} catch (BusinessException e) {
			logger.error(
					"BusinessException Occured while invoking getInventoryLocationListByLocationType for location Type: {}, Cause: {}",
					nelType, Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("error in getInventoryLocationListByLocationType, err = {}", Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getNELocationByNelId")
	public NELocation getNELocationByNelId(@QueryParam("nelId") String nelId) {
		try {
			return neLocationService.getNELocationByNelId(nelId);
		} catch (Exception e) {
			logger.error("Exception in getNELocationByNelId: nelId={} error={}",nelId,e.getMessage());
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getNeLocationAndParamterByType/{nelType}")
	public Map<String, Object> getNeLocationAndParamterByType(@PathParam("nelType") String nelType,@QueryParam("llimit") Integer llimit,@QueryParam("ulimit") Integer ulimit) {
		try {
			return neLocationService.getNeLocationAndParamterByType(nelType,llimit,ulimit);
		} catch (Exception e) {
			logger.error("Exception in getNELocationBynelType: nelType={} error={}", nelType, e.getMessage());
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("filterByLocationCode")
	public List<String> filterByLocationCode(@QueryParam("locationcode") String locationcode) {
		List<String> FilterByLocationCode = new ArrayList<>();
		try {
			logger.info("Going to getLocationCode By LocationCode : {}", locationcode);
			if (locationcode != null) {
				FilterByLocationCode = neLocationService.filterByLocationCode(locationcode);
			}
		} catch (Exception e) {
			logger.error("Error in getting Location :{}", Utils.getStackTrace(e));
		}
		return FilterByLocationCode;
	}
	
	@GET
	@Path("getNELocationByGeographyId")
	public List<NELocationWrapper> getNELocationByGeographyId(@QueryParam("geoId") Integer geoId,
			@QueryParam("geoType") String geoType, @QueryParam("nelType") NELType nelType) {
		try {
			return neLocationService.getNELocationByGeographyId(geoId, geoType, nelType);
		} catch (Exception e) {
			logger.error("Exception in getNELocationBynelType: geoId={}, geoType={},nelType={} error={}", geoId, geoType,nelType,e.getMessage());
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getGeographyWiseNELocationCountByGeographyId")
	public List<NELocationWrapper> getGeographyWiseNELocationCountByGeographyId(@QueryParam("nelType") String nelType,@QueryParam("geoId") Integer geoId,@QueryParam("geoType") String geoType) {
		try {
			return neLocationService.getGeographyWiseNELocationCountByGeographyId(nelType, geoId, geoType);
		} catch (Exception e) {
			logger.error("Exception in getGeographyWiseNELocationCountByGeographyId: nelType={}, geoId={}, geoType={} error={}",nelType, geoId, geoType, e.getMessage());
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getNELocationCountByGeographyId")
	public Integer getNELocationCountByGeographyId(@QueryParam("geoId") Integer geoId,
			@QueryParam("geoType") String geoType, @QueryParam("nelType") NELType nelType) {
		try {
			return neLocationService.getNELocationCountByGeographyId(geoId, geoType, nelType);
		} catch (Exception e) {
			logger.error("Exception in getNELocationCountByGeographyId: geoId={}, geoType={},nelType={} error={}", geoId, geoType,nelType,e.getMessage());
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getNELocationInfoByNelidAndNeltype")
	public Map<String, Object> getNELocationInfoByNelidAndNeltype(@QueryParam("nelId") String nelId,@QueryParam("nelType") NELType nelType){
		try {
			return neLocationService.getNELocationInfoByNelidAndNeltype(nelId, nelType);
		} catch (Exception e) {
			logger.error("Exception in getNELocationInfoByNelidAndNeltype: nelId={}, nelType={}",nelId,nelType,e.getMessage());
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	

}