package com.inn.foresight.module.nv.inbuilding.rest.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.http.HttpRequest;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.report.utils.ReportUtils;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.inbuilding.rest.INVIBUnitResultRest;
import com.inn.foresight.module.nv.inbuilding.service.INVIBUnitResultService;

/**
 * The Class NVIBUnitResultRestImpl.
 *
 * @author innoeye 
 * date - 15-Mar-2018 7:59:26 PM
 */
@Path("/nvInBuildingResult/")
@Service("NVIBUnitResultRestImpl")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NVIBUnitResultRestImpl extends InBuildingConstants
		implements INVIBUnitResultRest {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVIBUnitResultRestImpl.class);

	/** The nvib unit result service. */
	@Autowired
	private INVIBUnitResultService nvibUnitResultService;

	/**
	 * Search NVIB buildings.
	 *
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @param technology the technology
	 * @param buildingType the building type
	 * @param zoomLevel the zoom level
	 * @return the response
	 */
	@GET
	@Path("searchNVIBBuildings")
	@Override
	public Response searchNVIBBuildings(@QueryParam(SOUTH_WEST_LAT) Double swLat,
			@QueryParam(SOUTH_WEST_LONG) Double swLng,
			@QueryParam(NORTH_EAST_LAT) Double neLat,
			@QueryParam(NORTH_EAST_LONG) Double neLng,
			@QueryParam(TECHNOLOGY) String technology,
			@QueryParam(BUILDING_TYPE) String buildingType,
			@QueryParam(ZOOMLEVEL) Integer zoomLevel) {
		try {
			logger.info(
					"Going to searchNVIBBuildings for sWLat {},sWLong {},sWLong {},nELong {},technology {}, buildingType {} and zoomLevel {}",
					swLat, swLng, neLat, neLng, technology, buildingType,
					zoomLevel);
			if (swLat == null || swLng == null || neLat == null || neLng == null
					|| technology == null || buildingType == null
					|| zoomLevel == null) {
				logger.info(
						"Parameters are null, SWLat {},SWLng {},NELat {}, NELng {}, technology {}, buildingType {} and zoomLevel {}",
						swLat, swLng, neLat, neLng, technology, buildingType,
						zoomLevel);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			} else
				return Response.ok(nvibUnitResultService.searchNVIBBuildings(
						swLat, swLng, neLat, neLng, technology, buildingType,
						zoomLevel)).build();
		} catch (Exception e) {
			logger.error("Exception in searchNVIBBuildings : {} ",
					ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	/**
	 * Gets the wings.
	 *
	 * @param buildingId the building id
	 * @param technology the technology
	 * @return the wings
	 */
	@GET
	@Path("getWings/{buildingId}/{technology}")
	@Override
	public Response getWings(@PathParam(BUILDING_ID) Integer buildingId,
			@PathParam(TECHNOLOGY) String technology) {
		try {
			logger.info("Going to getWings for buildingId {}",
					buildingId);
			if (buildingId != null) {
				return Response	.ok(
						nvibUnitResultService.getWings(buildingId, technology))
								.build();
			} else {
				logger.info("Parameters are null, buildingId {} ", buildingId);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getWings : {} ",
					ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	/**
	 * Gets the floors.
	 *
	 * @param wingId the wing id
	 * @param technology the technology
	 * @return the floors
	 */
	@GET
	@Path("getFloors/{wingId}/{technology}")
	@Override
	public Response getFloors(@PathParam(WING_ID) Integer wingId,
			@PathParam(TECHNOLOGY) String technology) {
		try {
			logger.info("Going to find Floors by wingId {} , technology {}",
					wingId, technology);
			if (technology == null) {
				logger.info("Parameters are null, wingId {} and technology {} ",
						wingId, technology);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			} else
				return Response	.ok(
						nvibUnitResultService.getFloors(wingId, technology))
								.build();
		} catch (Exception e) {
			logger.error("Exception in getFloors : {} ",
					ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	/**
	 * Gets the units.
	 *
	 * @param floorId the floor id
	 * @param technology the technology
	 * @return the units
	 */
	@GET
	@Path("getUnits/{floorId}/{technology}")
	@Override
	public Response getUnits(@PathParam(FLOOR_ID) Integer floorId,
			@PathParam(TECHNOLOGY) String technology) {
		try {
			logger.info("Going to get Units by floorId {},technology {}",
					floorId, technology);
			if (floorId == null || technology == null) {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			} else {
				return Response	.ok(
						nvibUnitResultService.getUnits(floorId, technology))
								.build();
			}
		} catch (Exception e) {
			logger.error("Exception in getUnits : {} ",
					ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	
	/**
	 * Gets the recipe file name.
	 *
	 * @param unitId the unit id
	 * @param technology the technology
	 * @return the recipe file name
	 */
	@GET
	@Path("getRecipeFileName/{unitId}/{technology}")
	@Override
	public Response getRecipeFileName(@PathParam(UNIT_ID) Integer unitId,
			@PathParam(TECHNOLOGY) String technology) {
		try {
			logger.info("Going to get recipeFileName by unitId {},technology {}",
					unitId, technology);
			if (unitId == null || technology == null) {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			} else {
				return Response	.ok(
						nvibUnitResultService.getRecipeFileName(unitId, technology))
								.build();
			}
		} catch (Exception e) {
			logger.error("Exception in getRecipeFileName : {} ",
					ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	@POST
	@Path("getInbuildingReport/")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getInbuildingReport(String json) {
		logger.info("Going to get report data for json {}", json);
		try {
		     HttpRequest http = new HttpPostRequest(ConfigUtils.getString(ConfigEnum.CREATE_INBUILDING_REPORT_URL, Boolean.TRUE), new StringEntity(json));
		     http.addHeader("Content-Type", "application/json");
			 return Response.ok(http.getString()).build();

		} catch (Exception e) {
			logger.error("Exception in getNVReport : {}", ExceptionUtils.getStackTrace(e));

		}
		return null;
	}
	
	@GET
	@Path("getInbuildingReportPdf/")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInbuildingReportPdf(@QueryParam("recipeId") Integer recipeId,
			@QueryParam("operator") String operator, @QueryParam("inbuildingid") Integer inbuildingid,
			@QueryParam("workorderId") Integer workorderId) {
		try {
			String url = ConfigUtils.getString(ConfigEnum.CREATE_INBUILDING_REPORT_URL, Boolean.TRUE);
			String finalUrl = url + getQueryParamsInUrl(recipeId, operator, inbuildingid, workorderId);
			logger.info(" finalUrl is {}", finalUrl);
			HttpResponse response = ReportUtils.sendGetRequestWithoutTimeOut(finalUrl);
			return String.format(ForesightConstants.FILE_PATH_JSON, Utils.saveAndGetFilePath(response, 
					ConfigUtils.getString(ConfigEnum.NV_REPORT_BASE_PATH.getValue())));

		} catch (Exception e) {
			logger.error("Exception in getInbuildingReportPdf : {}", ExceptionUtils.getStackTrace(e));

		}
		return null;
	}
	
	private static String getQueryParamsInUrl(Integer recipeId, String operator, Integer inbuildingid, Integer workorderId){
		String finalUrl = "";
		String extraCharacter = "?";
		if(recipeId != null){
			finalUrl = finalUrl + extraCharacter + "recipeId=" + recipeId;
			extraCharacter = "&";
		}
		if(operator != null){
			finalUrl = finalUrl + extraCharacter + "operator=" + operator;
			extraCharacter = "&";
		}
		if(inbuildingid != null){
			finalUrl = finalUrl + extraCharacter + "inbuildingid=" + inbuildingid;
			extraCharacter = "&";
		}
		if(workorderId != null){
			finalUrl = finalUrl + extraCharacter + "workorderId=" + workorderId;
		}
		return finalUrl;
	}


	@Override
	@GET
	@Path("getUnitWiseWorkorder")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUnitWiseWorkorder(@QueryParam("unitId") Integer unitId,@QueryParam("technology") String technology) {
		logger.info("Going to getUnitWiseWorkorder for unitId: {} ,technology : {}", unitId,technology);
		try {
			List<GenericWorkorder> list = nvibUnitResultService.getUnitWiseWorkorder(unitId,technology);
			if (list != null) {
				return Response.ok(list).build();
			}
		} catch (Exception e) {
			logger.error("Error while getting unit wise workorder: {}", e.getMessage());
			return Response.ok(FAILURE_JSON).build();
		}
		return Response.ok(FAILURE_JSON).build();

	}
	
	@Override
	@GET
	@Path("getUnitWiseWorkorderCount")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUnitWiseWorkorderCount(@QueryParam("unitId")Integer unitId,@QueryParam("technology") String technology) {
		logger.info("Going to getUnitWiseWorkorderCount for unitId: {} ,technology {} ",unitId,technology);
		try {
			return Response	.ok(nvibUnitResultService.getUnitWiseWorkorderCount(unitId,technology)).build();
		}catch(Exception e) {
			logger.error("Error while getting unit wise workorder count: {}", e.getMessage());
			return Response.ok(FAILURE_JSON).build();
		}
		}
	@Override
	@GET
	@Path("getWingLevelWiseIBUnitResult")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWingLevelWiseIBUnitResult(@QueryParam("id") Integer id, @QueryParam("type") String type,@QueryParam("technology")String technology) {
		logger.info("Going to getWingLevelWiseIBUnitResult for id: {} type : {}", id, type);
		try {
			if (Utils.hasValidValue(String.valueOf(id)) && Utils.hasValidValue(type)) {
				return Response.ok(nvibUnitResultService.getWingLevelWiseIBUnitResult(id, type,technology)).build();
			} else {
				return Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Error while getting unit wise workorder count: {}", Utils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
}
