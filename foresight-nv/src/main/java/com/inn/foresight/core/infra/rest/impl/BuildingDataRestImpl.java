package com.inn.foresight.core.infra.rest.impl;

import static com.inn.core.generic.utils.Preconditions.checkNotEmpty;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpGetRequest;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.Preconditions;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.service.IBuildingDataService;
import com.inn.foresight.core.infra.wrapper.BuildingPolygonWrapper;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.core.infra.wrapper.FloorWrapper;
import com.inn.foresight.core.infra.wrapper.GeographyDetailWrapper;
import com.inn.foresight.core.infra.wrapper.UnitWrapper;
import com.inn.product.security.utils.AuthenticationCommonUtil;


@Path("/NVInBuilding")
@Service("BuildingDataRestImpl")
public class BuildingDataRestImpl {
	/** The logger. */
	private Logger logger = LogManager.getLogger(BuildingDataRestImpl.class);

	@Autowired
	IBuildingDataService iBuildingDataService;

	/**
	 * Get by Building List by Cluster name .
	 *
	 * @param cluster the cluster
	 * @return the response
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getBuildingListByCluster/{cluster}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response getBuildingListByCluster(@PathParam("cluster") String cluster) {
		Object objectToReturn;
		try {

			List<BuildingWrapper> buildingList = iBuildingDataService.getBuildingListByCluster(cluster);
			if (!Utils.isValidList(buildingList)) {
				objectToReturn = new ArrayList<>();
			} else {
				objectToReturn = buildingList;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return Response.ok(objectToReturn).build();
	}

	/**
	 * Get by Building Details by id .
	 *
	 * @param id the id
	 * @return the response
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getBuildingDetailsById/{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response getBuildingDetailsById(@PathParam("id") Integer id) {
		Response response;
		try {
			BuildingWrapper buildingWrapper = iBuildingDataService.getBuildingDetailsByPk(id);
			if (buildingWrapper != null) {
				response = Response.ok(buildingWrapper).build();
			} else {
				response = Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getBuildingDetailsById {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return response;
	}

	/**
	 * Get Nearest Building From Location.
	 *
	 * @return the response, nearest six building's data
	 * @throws RestException the rest exception
	 * @param latitude  latitude of the location
	 * @param longitude longitude of the location
	 */
	@GET
	@Path("getNearestBuildingsFromLocation/{latitude}/{longitude}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response getNearestBuildingsFromLocation(@PathParam("latitude") Double latitude,
			@PathParam("longitude") Double longitude) {
		Response response = null;
		try {
			if (Utils.isValidDouble(latitude) && Utils.isValidDouble(longitude)) {
				response = Response.ok(iBuildingDataService.getNearestBuildingsFromLocation(latitude, longitude))
						.build();
			} else {
				response = Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}

		return response;
	}

	/**
	 * Creates the building structure.
	 *
	 * @param request the request
	 * @return the response
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("createBuildingStructure")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)

	public Response createBuildingStructure(@Context HttpServletRequest request) {
		
		Response response;
		try {
			request.setCharacterEncoding("UTF-8");
			
			BuildingWrapper buildingWrapper = iBuildingDataService.createBuildingAlongWithFloorPlan(request);
			if (buildingWrapper != null) {

				response = Response.ok(new Gson().toJson(buildingWrapper)).build();
			} else {
				response = Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
			}
		} catch (RestException | UnsupportedEncodingException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return response;
	}

	/**
	 * Gets the building list by name.
	 *
	 * @param name name to search buildings.
	 * @return the building list by name
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getBuildingListByName/{name}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response getBuildingListByName(@PathParam("name") String name) {
		Response response = null;
		try {
			if (Utils.isValidString(name)) {
				response = Response.ok(iBuildingDataService.getBuildingListByName(name)).build();
			} else {
				response = Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return response;
	}

	/**
	 * Creates the building from app.
	 *
	 * @param buildingData encrypted building wrapper json
	 * @return the response
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("createBuildingData")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response createBuildingData(String buildingData) {
		Response response = null;
		try {
			if (Utils.isValidString(buildingData)) {
				
				response = Response
						.ok(AuthenticationCommonUtil.checkForValueEncryption(iBuildingDataService.createBuildingData(buildingData),null))
						.build();
			}
		} catch (Exception e) {
			logger.error("Exception createBuildingData in REST:{}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return response;
	}

	@GET
	@Path("getGeographyDetailByBuildingId/{buildingId}")
	public GeographyDetailWrapper getGeographyDetailByBuildingId(@PathParam("buildingId") String buildingId) {
		checkNotEmpty(buildingId);
		return iBuildingDataService.getGeographyDetailByBuildingId(buildingId);
	}
	
	@GET
	@Path("getAllBuilding")
	public List<BuildingPolygonWrapper> getAllBuildingByViewPort(@QueryParam("SWLat") Double swLat,
			@QueryParam("SWLng") Double swLng, @QueryParam("NELat") Double neLat, @QueryParam("NELng") Double neLng) {
		Preconditions.checkNoneNull(swLat,swLng,neLat,neLng);
		try {
			return iBuildingDataService.getBuildingPolygonByViewPort(swLat, swLng, neLat, neLng);
		} catch (Exception e) {
			logger.error("Exception in getting buildingPolygon viewport", e);
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}

	}
	
	@GET
	@Path("getFloorDetails/{buildingId}")
	public List<FloorWrapper> getFloorDetailsByBuildingId(@PathParam("buildingId") String buildingId) {
		try {
			return iBuildingDataService.getFloorDetailsByBuildingId(buildingId);
		} catch (Exception e) {
			logger.error("Exception in getting floor details", e);
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}

	}
	
	@GET
	@Path("getUnitDetails/{floorId}")
	public List<UnitWrapper> getUnitDetailsByFloorId(@PathParam("floorId") Integer floorId) {
		try {
			return iBuildingDataService.getUnitDetailsByFloorId(floorId);
		} catch (Exception e) {
			logger.error("Exception in getting unit details", e);
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}

	}
	
	@GET
	@Path("getBuildingDetailByBuildingId/{buildingId}")
	public BuildingWrapper getBuildingDetailByBuildingId(@PathParam("buildingId") String buildingId) {
		checkNotEmpty(buildingId);
		return iBuildingDataService.getBuildingDetailByBuildingId(buildingId);
	}

	@GET
	@Path("getCoverageDetails/{buildingId}")
	public BuildingWrapper getCoverageDetails(@PathParam("buildingId") String buildingId,
			@QueryParam("floorId") Integer floorId) {
		checkNotEmpty(buildingId);
		return iBuildingDataService.getCoverageDetails(buildingId, floorId);
	}
	
	@GET
	@Path("getFloorPlan/{unitId}")
	public Map<String, byte[]> getFloorPlanByUnitId(@PathParam("unitId") Integer unitId, @QueryParam("kpi") String kpi,
			@Context HttpServletRequest request) {
		try {
			String url = Utils.getDropwizardUrlWithPrefix(request);
			logger.debug("url: {}", url);
			Map<String, byte[]> response = new HashedMap<>();
			response.put("image", new HttpGetRequest(url).getByteArray());
			return response;
		} catch (HttpException e) {
			logger.error("Error in getting floor plan due to : ", e);
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getFloorLegend/{unitId}/{kpi}")
	public Map<String, String> getFloorLegendByKpi(@PathParam("unitId") Integer unitId, @PathParam("kpi") String kpi,
			@Context HttpServletRequest request) {
		checkNotEmpty(kpi);
		try {
			String url = Utils.getDropwizardUrlWithPrefix(request);
			logger.debug("url: {}", url);
			Map<String, String> response = new HashedMap<>();
			response.put("legend", new HttpGetRequest(url).getString());
			return response;
		} catch (HttpException e) {
			logger.error("Error in getting legend due to : ", e);
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getNumberOfFloors/{buildingId}")
	public Long getNumberOfFloorsByBuildingId(@PathParam("buildingId") String buildingId) {
		try {
			return iBuildingDataService.getNumberOfFloorsByBuildingId(buildingId);
		} catch (Exception e) {
			logger.error("Exception in getting no. of floors", e);
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}

	}
	
	@GET
	@Path("getPredictionKpi/{unitId}")
	public Map<String, String> getPredictionKpiByUnitId(@PathParam("unitId") Integer unitId, @Context HttpServletRequest request) {
		try {
			String url = Utils.getDropwizardUrlWithPrefix(request);
			logger.debug("url: {}", url);
			Map<String, String> response = new HashedMap<>();
			response.put("predictionKpi", new HttpGetRequest(url).getString());
			return response;
		} catch (HttpException e) {
			logger.error("Error in getting prediction kpi due to : ", e);
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getBounds/{unitId}")
	public Map<String, String> getBoundsByUnitId(@PathParam("unitId") Integer unitId, @Context HttpServletRequest request) {
		try {
			String url = Utils.getDropwizardUrlWithPrefix(request);
			logger.debug("url: {}", url);
			Map<String, String> response = new HashedMap<>();
			response.put("bounds", new HttpGetRequest(url).getString());
			return response;
		} catch (HttpException e) {
			logger.error("Error in getting bounds due to : ", e);
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
}
