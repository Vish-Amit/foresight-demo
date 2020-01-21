package com.inn.foresight.core.maplayer.rest.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.gis.GIS2DPolygon;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.maplayer.service.IGenericMapService;

@Path("/secure/ms/map")
public class SecureGenericMapRestImpl {

  private static final Logger logger = LogManager.getLogger(SecureGenericMapRestImpl.class);

  @Autowired
  private IGenericMapService genericMapService;

  @POST
  @Path("getBoundaryData")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBoundaryData(List<String> columnList,
      @QueryParam("tableName") String tableName, @QueryParam("NELat") Double nELat,
      @QueryParam("NELng") Double nELng, @QueryParam("SWLat") Double sWLat,
      @QueryParam("SWLng") Double sWLng, @QueryParam("isExact") Boolean isExact,
      @QueryParam("subPath") String subPath, @QueryParam("area") Double area) {
    logger.info("Inside getBoundaryData method {},{},{},{},{},{},{}", tableName, nELat, nELng,
        sWLat, sWLng, subPath, area);

    if (tableName != null && nELat != null && nELng != null && sWLat != null && sWLng != null) {
      try {
        List<List<String>> dataList = genericMapService.getBoundaryDataForTable(tableName,
            columnList, nELat, nELng, sWLat, sWLng, isExact, subPath, area);
        return Response.ok(dataList).build();
      } catch (Exception e) {
        logger.error("Exception inside getBoundaryData due to {}", ExceptionUtils.getStackTrace(e));
        throw new RestException(e.getMessage());
      }
    } else {
      throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
    }
  }
  
  @POST
  @Path("calculateFacebookPopulation")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response calculateFacebookPopulation(List<List<Double>> polygon) {
    try {
      if (!polygon.isEmpty()) {
        return Response.ok(genericMapService.getFacebookPopulation(polygon, null)).build();
      } else {
        throw new RestException(ForesightConstants.ERROR_INVALIDPARAMS);
      }
    } catch (Exception exception) {
      logger.error("Exception inside calculateFacebookPopulation due to {}",
          ExceptionUtils.getStackTrace(exception));
      throw new RestException(exception.getMessage());
    }
  }
  
  @POST
  @Path("getAllGeographyNameByPolygon")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllGeographyNameByPolygon(List<List<Double>> polygon) {
    if (!polygon.isEmpty()) {
      Map<String, Set<Object>> allGeographyNameByPolygon =
          genericMapService.getAllNetworkGeography(new GIS2DPolygon(polygon).toString());
      if (allGeographyNameByPolygon == null) {
        throw new RestException(ForesightConstants.ERROR_RESPONSE);
      }
      return Response.ok(allGeographyNameByPolygon).build();
    } else {
      throw new RestException(ForesightConstants.ERROR_INVALIDPARAMS);
    }
  }
  
  @POST
  @Path("sales/getAllGeographyNameByPolygon")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllSalesGeographyNameByPolygon(List<List<Double>> polygon) {
    if (!polygon.isEmpty()) {
      Map<String, Set<Object>> allGeographyNameByPolygon =
          genericMapService.getAllSalesGeography(new GIS2DPolygon(polygon).toString());
      if (allGeographyNameByPolygon == null) {
        throw new RestException(ForesightConstants.ERROR_RESPONSE);
      }
      return Response.ok(allGeographyNameByPolygon).build();
    } else {
      throw new RestException(ForesightConstants.ERROR_INVALIDPARAMS);
    }
  }
  
  @GET
  @Path("getAllGeographyName")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String[] getAllGeographyName(@QueryParam("lat") Double latitude,
      @QueryParam("long") Double longitude) {
    logger.info("Inside getAllGeographyname latitude {}, longitude {}", latitude, longitude);
    try {
      if (latitude != null && longitude != null) {
        return genericMapService.getAllGeographyName(new LatLng(latitude, longitude));
      } else {
        throw new RestException(ForesightConstants.INVALID_PARAMETERS);
      }
    } catch (Exception e) {
      logger.error("Exception inside getAllGeographyName due to {}",
          ExceptionUtils.getStackTrace(e));
      throw new RestException(e.getMessage());
    }
  }
  
  @GET
  @Path("sales/getAllGeographyName")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String[] getAllSalesGeographyName(@QueryParam("lat") Double latitude,
      @QueryParam("lng") Double longitude) {
    logger.info("Inside getAllSalesGeographyName latitude {}, longitude {}", latitude, longitude);
    try {
      if (latitude != null && longitude != null) {
        return genericMapService.getAllSalesGeographyName(new LatLng(latitude, longitude));
      } else {
        throw new RestException("Invalid Parameters");
      }
    } catch (Exception e) {
      logger.error("Exception inside getAllSalesGeographyName due to {}",
          ExceptionUtils.getStackTrace(e));
      throw new RestException(e.getMessage());
    }
  }
  
  @GET
  @Path("getGeographyNames")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGeographyNames(@QueryParam("lat") Double latitude,
      @QueryParam("lng") Double longitude) {
    logger.info("Inside getGeographyNames latitude {}, longitude {}", latitude, longitude);
    try {
      if (latitude != null && longitude != null) {
        Map<String, String[]> geographyNames =
            genericMapService.getGeographyNames(new LatLng(latitude, longitude));
        return Response.ok(geographyNames).build();
      } else {
        throw new RestException("Invalid Parameters");
      }
    } catch (Exception e) {
      logger.error("Exception inside getGeographyNames due to {}", ExceptionUtils.getStackTrace(e));
    }
    throw new RestException(ForesightConstants.UNABLE_TO_PROCESS_REQUEST);
  }
  
  @POST
  @Path("getGeographyDataByPoint")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGeographyDataByPoint(List<String> columnList, @QueryParam("lat") Double lat,
      @QueryParam("long") Double lon, @QueryParam("tableName") String tableName,
      @QueryParam("isExact") Boolean isExact, @QueryParam("subPath") String subPath) {
    logger.info("Inside getGeographyDataByPoint method {},{},{},{},{},{}", tableName, lat, lon,
        columnList, subPath);

    if (tableName != null && lat != null && lon != null) {
      try {
        List<List<String>> dataList = genericMapService.getGeographyDataByPoint(tableName,
            columnList, lat, lon, isExact, subPath);
        return Response.ok(dataList).build();
      } catch (Exception e) {
        throw new RestException(e.getMessage());
      }
    } else {
      throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
    }
  }
    
  @POST
  @Path("getBoundaryDataByGeographyNames")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBoundaryDataByGeographyNamesMS(List<List<String>> combineList,
      @QueryParam("tableName") String tableName, @QueryParam("area") Double area,
      @QueryParam("type") String type) {
    logger.info("Inside getBoundaryDataByGeographyNames tableName {}, type {}", tableName, type);
    if (CollectionUtils.isNotEmpty(combineList)) {
      try {
        List<String> geoGraphyNameList = combineList.get(ForesightConstants.ZERO);
        List<String> columnList = combineList.get(ForesightConstants.ONE);
        List<Map<String, String>> response = genericMapService.getBoundaryDataByGeographyNamesMS(
            geoGraphyNameList, tableName, columnList, area, type);
        if (response != null) {
          return Response.ok(response).build();
        }
      } catch (Exception e) {
        logger.error("Exception inside getDataByGeographyName {} ",
            ExceptionUtils.getStackTrace(e));
        throw new RestException(e.getMessage());
      }
    }
    throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
  }
}