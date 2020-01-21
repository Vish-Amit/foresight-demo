package com.inn.foresight.core.maplayer.rest.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.inn.commons.Symbol;
import com.inn.commons.io.image.ImageUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.gis.GIS2DPolygon;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.wrapper.GeographyWrapper;
import com.inn.foresight.core.maplayer.model.KPISummaryDataWrapper;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;


/** The Class GenericMapRestImpl. */
@Path("/ms/map")
public class GenericMapRestMicroServiceImpl {
  /** The Constant logger. */
  private static final Logger logger = LogManager.getLogger(GenericMapRestMicroServiceImpl.class);
  private static final int TILE_BASE_ZOOM = 15;
  /** The generic map service. */
  @Autowired
  private IGenericMapService genericMapService;

  /**
   * Gets the data from table.
   *
   * @param columnList the column list
   * @param tableName the table name
   * @param zoom the zoom
   * @param NELat the NE lat
   * @param NELng the NE lng
   * @param SWLat the SW lat
   * @param SWLng the SW lng
   * @param minDate the min date
   * @param maxDate the max date
   * @param latestDataDate the latest data date
   * @param postFix the post fix
   * @return the data from table
   */
  @POST
  @Path("getDataFromHbase")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDataFromTable(List<String> columnList,
      @QueryParam("tableName") String tableName, @QueryParam("zoomLevel") Integer zoom,
      @QueryParam("NELat") Double nELat, @QueryParam("NELng") Double nELng,
      @QueryParam("SWLat") Double sWLat, @QueryParam("SWLng") Double sWLng,
      @QueryParam("minDate") String minDate, @QueryParam("maxDate") String maxDate,
      @QueryParam("latestDataDate") Long latestDataDate, @QueryParam("postFix") String postFix,
      @QueryParam("gridColName") String gridColName, @QueryParam("aggType") String aggType) {
    if (columnList == null || columnList.isEmpty()) {
      columnList = new ArrayList<>();
      columnList.add("r:zoom0H");
    }
    logger.info("Hbase REST {},{},{},{},{},{},{},{},{} {} {}", tableName, zoom, nELat, nELng, sWLat,
        sWLng, columnList, minDate, maxDate, latestDataDate, postFix);

    if (tableName != null && zoom != null && nELat != null && nELng != null && sWLat != null
        && sWLng != null) {
      try {
        List<List<String>> dataList =
            genericMapService.getDataForTable(tableName, zoom, columnList, nELat, nELng, sWLat,
                sWLng, minDate, maxDate, latestDataDate, postFix, gridColName, aggType);

        return Response.ok(dataList).build();
      } catch (Exception e) {
        return Response.ok("{\"response\":\"error getting data\"}").build();
      }
    } else {
      return Response.ok("{\"response\":\"invalid params\"}").build();
    }
  }

  /**
   * Gets the image for kpi and zone.
   *
   * @param tableName the table name
   * @param tileId the tile id
   * @param image the image
   * @param kpi the kpi
   * @param date the date
   * @param siteStatus the site status
   * @param floor the floor
   * @param band the band
   * @return the image for kpi and zone
   * @throws IOException
   */
  @GET
  @Path("getImageForKpiAndZone")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getImageForKpiAndZone(@QueryParam("tableName") String tableName,
      @QueryParam("tileId") String tileId, @QueryParam("image") String image,
      @QueryParam("kpi") String kpi, @QueryParam("date") String date,
      @QueryParam("siteStatus") String siteStatus, @QueryParam("floor") String floor,
      @QueryParam("band") String band) throws IOException {
    logger.info(
        "In image tableName {},tilId {}, image {}, kpi {}, date {} , siteStatus{}, floor {}, band {}",
        tableName, tileId, image, kpi, date, siteStatus, floor, band);
    long d1 = new Date().getTime();
    if (tableName != null && tileId != null && kpi != null && date != null) {
      byte[] imageArr = genericMapService.getImageForKpiAndZone(tableName, tileId, image, kpi, date,
          siteStatus, floor, band);
      if (imageArr != null) {
        logger.info("Getting data for tileId getImageForKpiAndZone{}, {}", tileId, tableName);
        final ResponseBuilder resBuilder = Response.ok(imageArr);
        resBuilder.header(ForesightConstants.CONTENT_TYPE, ForesightConstants.CONTENT_IMG)
            .header(GenericMapUtils.ACCEPT, GenericMapUtils.APP_OCTECT_STREM);
        long d2 = new Date().getTime();
        logger.info("Time taken at MicroService {} ms", d2 - d1);
        return resBuilder.build();
      } else {
        final ResponseBuilder resBuilder = Response.ok("No Image Found");
        return resBuilder.build();
      }
    } else {
      final ResponseBuilder resBuilder = Response.ok("Invalid params");
      return resBuilder.build();
    }
  }

  /**
   * Gets the image for kpi.
   *
   * @param tableName the table name
   * @param tileId the tile id
   * @param image the image
   * @param kpi the kpi
   * @param date the date
   * @param siteStatus the site status
   * @param band the band
   * @return the image for kpi
   */
  @GET
  @Path("getImageForKpi")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getImageForKpi(@QueryParam("tableName") String tableName,
      @QueryParam("tileId") String tileId, @QueryParam("image") String image,
      @QueryParam("kpi") String kpi, @QueryParam("date") String date,
      @QueryParam("siteStatus") String siteStatus, @QueryParam("band") String band) {
    logger.info("In image tableName {},tilId {}", tableName, tileId);
    if (tableName != null && tileId != null && kpi != null) {
      byte[] imageArr = genericMapService.getImageForKpiAndZone(tableName, tileId, image, kpi, date,
          siteStatus, null, band);
      if (imageArr != null) {
        final ResponseBuilder resBuilder = Response.ok(imageArr);
        resBuilder.header(ForesightConstants.CONTENT_TYPE, ForesightConstants.CONTENT_IMG)
            .header(GenericMapUtils.ACCEPT, GenericMapUtils.APP_OCTECT_STREM);

        return resBuilder.build();
      } else {
        logger.info("Returing null getImageForKpi");
        return null;
      }
    } else {
      final ResponseBuilder resBuilder = Response.ok("Invalid params");
      return resBuilder.build();
    }
  }

  /**
   * Gets the boundary data.
   *
   * @param columnList the column list
   * @param tableName the table name
   * @param nELat the n E lat
   * @param nELng the n E lng
   * @param sWLat the s W lat
   * @param sWLng the s W lng
   * @param isExact the is exact
   * @param subPath the sub path
   * @param area the area
   * @return the boundary data
   * @throws RestException the rest exception
   */
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

  /**
   * Gets the big image for kpi and zone.
   *
   * @param tableName the table name
   * @param tileId the tile id
   * @param image the image
   * @param kpi the kpi
   * @param date the date
   * @param siteStatus the site status
   * @param floor the floor
   * @param band the band
   * @return the big image for kpi and zone
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @GET
  @Path("getBigImageForKpiAndZone")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getBigImageForKpiAndZone(@QueryParam("tableName") String tableName,
      @QueryParam("tileId") String tileId, @QueryParam("image") String image,
      @QueryParam("kpi") String kpi, @QueryParam("date") String date,
      @QueryParam("siteStatus") String siteStatus, @QueryParam("floor") String floor,
      @QueryParam("band") String band) throws IOException {
    logger.info("In image tableName {},tilId {}, {}, {}, {} , {}, {}", tableName, tileId, image,
        kpi, date, siteStatus, floor);
    if (tableName != null && tileId != null && kpi != null && date != null && band != null
        && siteStatus != null) {
      BufferedImage bufferedImage = genericMapService.getBigImageForKpiAndZone(tableName, tileId,
          image, kpi, date, siteStatus, floor, band);
      if (bufferedImage != null) {
        logger.info("Getting data for tileId {}, {}", tileId, tableName);
        long d1 = new Date().getTime();
        final ResponseBuilder resBuilder = Response.ok(ImageUtils.toBytes(bufferedImage));
        resBuilder.header(ForesightConstants.CONTENT_TYPE, ForesightConstants.CONTENT_IMG)
            .header(GenericMapUtils.ACCEPT, GenericMapUtils.APP_OCTECT_STREM);
        long d2 = new Date().getTime();
        logger.info("Time taken at MicroService{} ms", d2 - d1);
        return resBuilder.build();
      } else {
        final ResponseBuilder resBuilder = Response.ok("No Image Found");
        return resBuilder.build();
      }
    } else {
      final ResponseBuilder resBuilder = Response.ok(ForesightConstants.EXCEPTION_INVALID_PARAMS);
      return resBuilder.build();
    }
  }

  /**
   * Gets the boundary data by geography names.
   *
   * @param combineList the combine list
   * @param tableName the table name
   * @param area the area
   * @param type the type
   * @return the boundary data by geography names
   * @throws RestException the rest exception
   */
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

  /**
   * Gets the geography data by point.
   *
   * @param columnList the column list
   * @param lat the lat
   * @param lon the lon
   * @param tableName the table name
   * @param isExact the is exact
   * @param subPath the sub path
   * @return the geography data by point
   * @throws RestException the rest exception
   */
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

  /**
   * Gets the coverage hole.
   *
   * @param columnList the column list
   * @param tableName the table name
   * @param zoom the zoom
   * @param NWLat the NW lat
   * @param NWLng the NW lng
   * @param SELat the SE lat
   * @param SELng the SE lng
   * @param date the date
   * @return the coverage hole
   */
  @POST
  @Path("getCoverageHole")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCoverageHole(List<String> columnList,
      @QueryParam("tableName") String tableName, @QueryParam("zoomLevel") Integer zoom,
      @QueryParam("nWLat") Double nWLat, @QueryParam("nWLng") Double nWLng,
      @QueryParam("sELat") Double sELat, @QueryParam("sELng") Double sELng,
      @QueryParam("date") String date) {
    logger.info("REST {},{},{},{},{},{},{}", tableName, zoom, nWLat, nWLng, sELat, sELng,
        columnList);

    if (columnList == null || columnList.isEmpty()) {
      columnList = new ArrayList<>();
      columnList.add("raw:zoom");
    }

    if (tableName != null && zoom != null && nWLat != null && nWLng != null && sELat != null
        && sELng != null && date != null) {
      try {
        List<List<String>> dataList = genericMapService.getCoverageHoleData(tableName, zoom,
            columnList, nWLat, nWLng, sELat, sELng, date);
        return Response.ok(dataList).build();
      } catch (Exception e) {
        return Response.ok("{\"response\":\"error getting data\"}").build();
      }
    } else {
      return Response.ok("{\"response\":\"invalid params\"}").build();
    }
  }

  /**
   * Gets the nearest point by lat lng.
   *
   * @param geoLevel the geo level
   * @param lat the lat
   * @param lng the lng
   * @return the nearest point by lat lng
   */
  @GET
  @Path("getNearestPointByLatLng")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getNearestPointByLatLng(@QueryParam("tableName") String geoLevel,
      @QueryParam("lat") Double lat, @QueryParam("long") Double lng) {
    try {
      return genericMapService.getNearestPointByLatLon(new LatLng(lat, lng), geoLevel);
    } catch (Exception e) {
      logger.error("Exception inside getNearestpoint: {}", ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  /**
   * Gets the hbase data by row or time range.
   *
   * @param columnList the column list
   * @param startRow the start row
   * @param endRow the end rowsta
   * @param tableName the table name
   * @param startTime the start time
   * @param endTime the end time
   * @return the hbase data by row or time range
   */
  @POST
  @Path("getHbaseDataByRowOrTimeRange")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getHbaseDataByRowOrTimeRange(List<String> columnList,
      @QueryParam("startRow") String startRow, @QueryParam("endRow") String endRow,
      @QueryParam("tableName") String tableName, @QueryParam("startTime") Long startTime,
      @QueryParam("endTime") Long endTime) {
    try {
      List<Map<String, String>> dataFromHbaseByTimeRange =
          genericMapService.getHbaseDataByRowOrTimeRangeList(columnList, startRow, endRow,
              tableName, startTime, endTime);
      return Response.ok(dataFromHbaseByTimeRange).build();
    } catch (Exception e) {
      logger.error("Error inside getHbaseDataByRowOrTimeRange: {}",
          ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  /**
   * Gets the all geography name.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the all geography name
   * @throws RestException the rest exception
   */
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

  /**
   * Gets the tile image.
   *
   * @param tableName the table name
   * @param tileId the tile id
   * @param imgColumn the img column
   * @param type the type
   * @param isBigTile the is big tile
   * @param baseZoom the base zoom
   * @param isColorUpdation the is color updation
   * @param accuracyFactor the accuracy factor
   * @return the tile image
   */
  @GET
  @Path("getTileImage")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getTileImage(@QueryParam("tableName") String tableName,
      @QueryParam("tileId") String tileId, @QueryParam("imgColumn") String imgColumn,
      @QueryParam("type") String type, @QueryParam("isBigTile") Boolean isBigTile,
      @QueryParam("baseZoom") Integer baseZoom,
      @QueryParam("isColorUpdation") boolean isColorUpdation,
      @QueryParam("accuracyFactor") Double accuracyFactor) {
    logger.info(
        "Inside  getTileImage tableName {},tilId {}, image ColumnName {},type {} isBigTile {}",
        tableName, tileId, imgColumn, type, isBigTile);
    try {
      if (tableName != null && tileId != null && imgColumn != null) {
        type = type != null ? type : Symbol.EMPTY_STRING;
        if (baseZoom == null) {
          baseZoom = TILE_BASE_ZOOM;
        }
        byte[] imageByteArray = genericMapService.getImageByteArray(tableName, tileId, imgColumn,
            type, isBigTile, baseZoom);
        if (imageByteArray != null) {
          if (isColorUpdation) {
            imageByteArray = ImageUtils.toBytes(genericMapService.updatePopulationAndAMSLColor(
                ImageUtils.toBufferedImage(imageByteArray), tableName, accuracyFactor));
          }
          ResponseBuilder resBuilder = Response.ok(imageByteArray);
          return resBuilder.header(ForesightConstants.CONTENT_TYPE, ForesightConstants.CONTENT_IMG)
              .header("ACCEPT", MediaType.APPLICATION_OCTET_STREAM).build();
        } else {
          throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
        }
      } else {
        throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
      }
    } catch (Exception exception) {
      logger.error("Exception Inside getTileImage due to {}",
          ExceptionUtils.getStackTrace(exception));
      throw new RestException(exception.getMessage());
    }
  }

  @GET
  @Path("getCustomerCareGeography")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCustomerCareGeography(@QueryParam("lat") Double lat,
      @QueryParam("long") Double lng) {
    if (lat == null || lng == null) {
      throw new RestException(ForesightConstants.ERROR_INVALIDPARAMS);
    }
    return Response.ok(genericMapService.getCustomerCareGeography(new LatLng(lat, lng))).build();
  }

  /**
   * Gets the population and height.
   *
   * @param lat the lat
   * @param lng the lng
   * @param accFactor the acc factor
   * @param tableName the table name
   * @param zoomLevel the tile zoom
   * @return the population and height
   */
  @GET
  @Path("getPopulationAndHeight")
  @Produces(MediaType.APPLICATION_JSON)
  public String getPopulationAndHeight(@NotNull @QueryParam("lat") Double lat,
      @NotNull @QueryParam("long") Double lng, @QueryParam("accFactor") Double accFactor,
      @NotNull @QueryParam("tableName") String tableName,
      @NotNull @QueryParam("zoomLevel") Integer zoomLevel, @QueryParam("maxRange") Integer maxRange,
      @QueryParam("minRange") Integer minRange) {
    return String.valueOf(genericMapService.getPopulationAndHeight(new LatLng(lat, lng), accFactor,
        tableName, zoomLevel, maxRange, minRange));
  }

  /**
   * Gets the all geography name by polygon.
   *
   * @param polygon the polygon
   * @return the all geography name by polygon
   * @throws RestException the rest exception
   */
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

  /**
   * Gets the all geography name by polygon for sales.
   *
   * @param polygon the polygon
   * @return the all sales geography name by polygon
   * @throws RestException the rest exception
   */
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

  @POST
  @Path("getPointData")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPointData(List<String> columnList, @Context HttpServletRequest request) {
    logger.info("Inside getPointData method columnList size {},", columnList.size());
    try {
      GeographyWrapper geographyWrapper = vaildateInputParams(request);
      List<List<String>> dataList = genericMapService.getPointData(columnList, geographyWrapper);
      return Response.ok(dataList).build();
    } catch (Exception e) {
      logger.error("Exception inside getPointData due to {}", ExceptionUtils.getStackTrace(e));
      throw new RestException(e.getMessage());
    }
  }

  private GeographyWrapper vaildateInputParams(HttpServletRequest request) {
    String tableName = request.getParameter(GenericMapUtils.TABLENAME);
    Double nELat = request.getParameter(GenericMapUtils.NELAT) != null
        ? Double.parseDouble(request.getParameter(GenericMapUtils.NELAT)) : null;
    Double nELng = request.getParameter(GenericMapUtils.NELONG) != null
        ? Double.parseDouble(request.getParameter(GenericMapUtils.NELONG)) : null;
    Double sWLat = request.getParameter(GenericMapUtils.SWLAT) != null
        ? Double.parseDouble(request.getParameter(GenericMapUtils.SWLAT)) : null;
    Double sWLng = request.getParameter(GenericMapUtils.SWLONG) != null
        ? Double.parseDouble(request.getParameter(GenericMapUtils.SWLONG)) : null;
    String subPath = request.getParameter(GenericMapUtils.SUBPATH);
    Boolean isExact = request.getParameter(GenericMapUtils.ISEXACT) != null
        ? Boolean.parseBoolean(request.getParameter(GenericMapUtils.ISEXACT)) : null;
    Double area = request.getParameter(GenericMapUtils.AREA) != null
        ? Double.parseDouble(request.getParameter(GenericMapUtils.AREA)) : null;
    if (StringUtils.isNotEmpty(tableName) && new Corner(sWLat, sWLng, nELat, nELng).isValid()) {
      GeographyWrapper geographyWrapper = new GeographyWrapper();
      geographyWrapper.setTableName(tableName);
      geographyWrapper.setnELat(nELat);
      geographyWrapper.setnELng(nELng);
      geographyWrapper.setsWLat(sWLat);
      geographyWrapper.setsWLng(sWLng);
      geographyWrapper.setSubPath(subPath);
      geographyWrapper.setIsExact(isExact);
      geographyWrapper.setArea(area);
      return geographyWrapper;
    }
    throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
  }

  /**
   * Calculate facebook population.
   *
   * @param polygon the polygon
   * @return the response
   */
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
  @Path("getBoundaryDataByPk")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBoundaryDataByPk(GeographyWrapper geoWrapper,
      @QueryParam("tableName") String tableName, @QueryParam("area") Double area,
      @QueryParam("type") String type) {
    logger.info("Inside getBoundaryDataByPk tableName {}, type {}", tableName, type);
    if (geoWrapper != null) {
      try {
        List<Integer> geoGraphyPkList = geoWrapper.getGeographyPkList();
        List<String> columnList = geoWrapper.getBoundaryColumnList();
        return Response.ok(genericMapService.getBoundaryDataByPKMS(geoGraphyPkList, tableName,
            columnList, area, type)).build();
      } catch (Exception e) {
        logger.error("Exception inside getBoundaryDataByPk {} ", ExceptionUtils.getStackTrace(e));
        throw new RestException(e.getMessage());
      }
    }
    throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
  }


  @POST
  @Path("getBoundaryDataByRowkeyPrefix")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBoundaryDataByRowkeyPrefix(GeographyWrapper geoWrapper,
      @NotNull @QueryParam("tableName") String tableName, @QueryParam("area") Double area) {
    logger.info("Inside getBoundaryDataByRowkeyPrefix tableName {}, geoWrapper {}", tableName,
        geoWrapper);
    if (geoWrapper != null) {
      try {
        List<String> rowKeyPrefix = geoWrapper.getRowKeyPrefixList();
        List<String> columnList = geoWrapper.getBoundaryColumnList();
        return Response.ok(genericMapService.getBoundaryDataByRowkeyPrefix(rowKeyPrefix, tableName,
            columnList, area)).build();
      } catch (Exception e) {
        logger.error("Exception inside getBoundaryDataByPk {} ", ExceptionUtils.getStackTrace(e));
        throw new RestException(e.getMessage());
      }
    }
    throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
  }



  /**
   * Gets the fb population for kpi summary.
   *
   * @param kpiSummaryDataWrapper the kpi summary data wrapper
   * @param northEastLat the north east lat
   * @param northEastLong the north east long
   * @param southWestLat the south west lat
   * @param southWestLong the south west long
   * @param geographyType the geography type
   * @param zoomLevel the zoom level
   * @return the fb population for kpi summary
   */
  @POST
  @Path("/getFbPopulationForKpiSummary")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFbPopulationForKpiSummary(KPISummaryDataWrapper kpiSummaryDataWrapper,
      @QueryParam("NELat") Double northEastLat, @QueryParam("NELng") Double northEastLong,
      @QueryParam("SWLat") Double southWestLat, @QueryParam("SWLng") Double southWestLong,
      @QueryParam("geographyType") String geographyType,
      @QueryParam("zoomLevel") Integer zoomLevel) {

    return Response.ok(genericMapService.getFbPopulationForKpiSummaryMS(kpiSummaryDataWrapper,
        northEastLat, northEastLong, southWestLat, southWestLong, geographyType, zoomLevel))
        .build();

  }


  /**
   * Gets the all geography name.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the all geography name
   * @throws RestException the rest exception
   */
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
  @Path("/getDataFromHbaseByRowkeyPrefix")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response GetDataFromHbase(List<String> hbaseColumns,
      @QueryParam("tableName") String tableName, @QueryParam("rowkeyprefix") String rowkeyPrefix)
      throws IOException {
    List<List<String>> dataFromHBase =
        genericMapService.getDataFromHBase(hbaseColumns, tableName, rowkeyPrefix);
    return Response.ok(dataFromHBase).build();


  }
  

  
  @POST
  @Path("getRouteData")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRouteData(GeographyWrapper geoWrapper) {
    if (geoWrapper != null) {
      try {
    	 logger.info("inside getRouteData ms");
        return Response.ok(genericMapService.getRouteDataMS(geoWrapper)).build();
      } catch (Exception e) {
        logger.error("Exception inside getRouteData {} ", ExceptionUtils.getStackTrace(e));
        throw new RestException(e.getMessage());
      }
    }
    throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
  }

  /**
   * Gets the all geography display name.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the all geography name
   * @throws RestException the rest exception
   */
  @GET
  @Path("getAllGeographyDisplayName")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String[] getAllGeographyDisplayName(@QueryParam("lat") Double latitude,
      @QueryParam("long") Double longitude) {
    logger.info("Inside getAllGeographyDisplayName latitude {}, longitude {}", latitude, longitude);
    try {
      if (latitude != null && longitude != null) {
        return genericMapService.getAllGeographyDisplayName(new LatLng(latitude, longitude));
      } else {
        throw new RestException(ForesightConstants.INVALID_PARAMETERS);
      }
    } catch (Exception e) {
      logger.error("Exception inside getAllGeographyDisplayName due to {}",
          ExceptionUtils.getStackTrace(e));
      throw new RestException(e.getMessage());
    }
  }
}
