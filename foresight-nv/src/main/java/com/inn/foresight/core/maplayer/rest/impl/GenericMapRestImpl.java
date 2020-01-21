package com.inn.foresight.core.maplayer.rest.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
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
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.maps.tiles.Tile;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.generic.wrapper.HBaseResponse;
import com.inn.foresight.core.infra.wrapper.GeographyWrapper;
import com.inn.foresight.core.maplayer.model.KPISummaryDataWrapper;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

/** The Class GenericMapRestImpl. */
@Path("/map")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Service("GenericMapRestImpl")
public class GenericMapRestImpl {
  /** The logger. */
  private static final Logger logger = LogManager.getLogger(GenericMapRestImpl.class);
  private static final String IS_BIG_TILE = "IS_BIG_TILE";

  /** The generic map service. */
  @Autowired
  private IGenericMapService genericMapService;

  /**
   * Gets the data from hbase.
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
   * @param postFix the post fix
   * @param gridColName the Grid Column Name
   * @param aggType the Aggregation Type
   * @return the data from hbase
   */
  @POST
  @Path("getDataFromHbase")
  public String getDataFromHbase(List<String> columnList, @QueryParam("tableName") String tableName,
      @QueryParam("zoomLevel") Integer zoom, @QueryParam("NELat") Double nELat,
      @QueryParam("NELng") Double nELng, @QueryParam("SWLat") Double sWLat,
      @QueryParam("SWLng") Double sWLng, @QueryParam("minDate") String minDate,
      @QueryParam("maxDate") String maxDate, @QueryParam("postFix") String postFix,
      @QueryParam("gridColName") String gridColName, @QueryParam("aggType") String aggType) {
    try {
      logger.info(
          "Getting parameter getDataFromHbase {}, {}, {}, {}, {}, {}, {}, {}, [{}] ,{} , {} , {}",
          tableName, zoom, nELat, nELng, sWLat, sWLng, minDate, maxDate, columnList, postFix,
          gridColName, aggType);
      String response = genericMapService.getDataFromHbase(columnList, tableName, zoom, nELat,
          nELng, sWLat, sWLng, minDate, maxDate, postFix, gridColName, aggType);
      if (response != null) {
        return response;
      } else {
        return ForesightConstants.ERROR;
      }
    } catch (Exception e) {
      logger.error("Exception inside getDataFromHbase {} ", ExceptionUtils.getStackTrace(e));
      return ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG;
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
   * @throws IOException Signals that an I/O exception has occurred.
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
        "In image tableName {},tilId {}, image {}, kpi {} date {}, siteStatus {}, floor {} band {}",
        tableName, tileId, image, kpi, date, siteStatus, floor, band);
    long d1 = new Date().getTime();
    try {
      if (tableName != null && tileId != null && kpi != null && date != null) {
        String isBigTileConfig = ConfigUtil.getConfigProp(IS_BIG_TILE);
        Boolean isBigTile = null;
        if (isBigTileConfig != null) {
          isBigTile = Boolean.valueOf(isBigTileConfig);
        } else {
          isBigTile = Boolean.TRUE;
        }
        byte[] imageArr = genericMapService.getImageForKpiAndZone(tableName, tileId, image, kpi,
            date, siteStatus, floor, band, isBigTile);
        if (imageArr != null) {
          final ResponseBuilder resBuilder = Response.ok(imageArr);
          resBuilder.header(ForesightConstants.CONTENT_TYPE, ForesightConstants.CONTENT_IMG)
              .header(GenericMapUtils.ACCEPT, MediaType.APPLICATION_OCTET_STREAM);
          long d2 = new Date().getTime();
          logger.info("Total time taken at App Server {}", d2 - d1);
          return resBuilder.build();
        }
      } else {
        logger.info("Parameter are not valid {}, {}, {}, {}, {}, {}", tableName, tileId, image, kpi,
            date, siteStatus, floor);
      }
    } catch (Exception e) {
      logger.error("Error in sending Response {}", ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  /**
   * Gets the image for kpi.
   *
   * @param tableName the table name
   * @param tileId the tile id
   * @param image the image
   * @param kpi the kpi
   * @param date the date
   * @return the image for kpi
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @GET
  @Path("getImageForKpi")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getImageForKpi(@QueryParam("tableName") String tableName,
      @QueryParam("tileId") String tileId, @QueryParam("image") String image,
      @QueryParam("kpi") String kpi, @QueryParam("date") String date) throws IOException {
    logger.info("In image tableName {}, tilId {}, image {}, kpi {}, date {}", tableName, tileId,
        image, kpi, date);
    BufferedImage bufferedImage = null;
    if (tableName != null && tileId != null && kpi != null) {
      bufferedImage = genericMapService.getImageForKpi(tableName, tileId, image, kpi, date);
      if (bufferedImage != null) {
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", jpegOutputStream);
        final ResponseBuilder resBuilder = Response.ok(jpegOutputStream.toByteArray());
        logger.info("Creating ByteArrayOutputStream  {}", jpegOutputStream);
        resBuilder.header(ForesightConstants.CONTENT_TYPE, ForesightConstants.CONTENT_IMG)
            .header("ACCEPT", MediaType.APPLICATION_OCTET_STREAM);
        return resBuilder.build();
      }
    }
    return null;
  }

  /**
   * Invoke Hbase data using query String Gets the hbase rest.
   *
   * @param query the query
   * @return the hbase rest
   * @throws RestException the rest exception
   */
  @GET
  @Path("hbaseRest")
  @Consumes(MediaType.APPLICATION_JSON)
  public HBaseResponse getHbaseRest(@QueryParam("query") String query) {
    logger.info("Hbase rest query [{}]", query);
    return genericMapService.getDataFromHbaseRest(query);
  }

  /**
   * Invokes Geography Data from hbase using lucence api Gets the boundary data
   *
   * @param columnList the column list(column name with columnFamily (eg -> r:cord))
   * @param tableName the table name
   * @param nELat the n E lat
   * @param nELng the n E lng
   * @param sWLat the s W lat
   * @param sWLng the s W lng
   * @param isExact the is exact
   * @param subPath the sub path
   * @param area the area
   * @return the boundary data
   */
  @POST
  @Path("getBoundaryData")
  public String getBoundaryData(List<String> columnList,
      @NotNull @QueryParam("tableName") String tableName,
      @NotNull @QueryParam("NELat") Double nELat, @NotNull @QueryParam("NELng") Double nELng,
      @NotNull @QueryParam("SWLat") Double sWLat, @NotNull @QueryParam("SWLng") Double sWLng,
      @QueryParam("isExact") Boolean isExact, @QueryParam("subPath") String subPath,
      @QueryParam("area") String area) {
    logger.info("Getting parameter getBoundaryData {}, {}, {}, {}, {}, [{}], {}, {}", tableName,
        nELat, nELng, sWLat, sWLng, columnList, isExact, subPath);
    String response = genericMapService.getBoundaryData(columnList, tableName, nELat, nELng, sWLat,
        sWLng, isExact, subPath, area);
    if (response != null) {
      return response;
    } else {
      return ForesightConstants.ERROR;
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
  public List<Map<String, String>> getBoundaryDataByGeographyNames(List<List<String>> combineList,
      @QueryParam("tableName") String tableName, @QueryParam("area") String area,
      @QueryParam("type") String type) {
    List<Map<String, String>> response = null;
    if (CollectionUtils.isNotEmpty(combineList)) {
      logger.info("tableName {} type {}", tableName, type);
      List<String> geoGraphyNameList = combineList.get(0);
      List<String> columnList = combineList.get(1);
      logger.info("Getting parameter getDataByGeographyName [{}], {}, [{}]", geoGraphyNameList,
          tableName, columnList);
      response = genericMapService.getBoundaryDataByGeographyNames(geoGraphyNameList, tableName,
          columnList, area, type);
      if (response != null) {
        return response;
      }
    } else {
      throw new RestException(ForesightConstants.INVALID_PARAMETERS_JSON);
    }
    return response;
  }

  /**
   * Gets the geography data by point.
   *
   * @param columnList the column list
   * @param tableName the table name
   * @param lat the lat
   * @param lon the lon
   * @param isExact the is exact
   * @param subPath the sub path
   * @return the geography data by point
   */
  @POST
  @Path("getGeographyDataByPoint")
  public String getGeographyDataByPoint(List<String> columnList,
      @NotNull @QueryParam("tableName") String tableName, @NotNull @QueryParam("lat") Double lat,
      @NotNull @QueryParam("lng") Double lon, @QueryParam("isExact") Boolean isExact,
      @QueryParam("subPath") String subPath) {
    logger.info(
        "inside getGeographyDataByPoint columnList :{} ,tableName :{} ,lat :{} ,lon :{} ,indexDir {}, subPath {}",
        columnList, tableName, lat, lon, isExact, subPath);
    return genericMapService.getGeographyDataByPoint(columnList, tableName, lat, lon, isExact,
        subPath);
  }

  /**
   * Gets the hbase data by row or time range.
   *
   * @param columnList the column list
   * @param tableName the table name
   * @param startRow the start row
   * @param endRow the end row
   * @param startTime the start time
   * @param endTime the end time
   * @return the hbase data by row or time range
   */
  @POST
  @Path("getHbaseDataByRowOrTimeRange")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String getHbaseDataByRowOrTimeRange(List<String> columnList,
      @QueryParam("tableName") String tableName, @QueryParam("startRow") String startRow,
      @QueryParam("endRow") String endRow, @QueryParam("startTime") Long startTime,
      @QueryParam("endTime") Long endTime) {
    try {
      return genericMapService.getHbaseDataByRowOrTimeRange(columnList, tableName, startRow, endRow,
          startTime, endTime);
    } catch (Exception e) {
      logger.error("Error inside getNearestpoint: {}", ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  /**
   * Gets the geography L 4 lat lng.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the geography L 4 lat lng
   */
  @GET
  @Path("getGeographyL4LatLng")
  @Consumes(MediaType.APPLICATION_JSON)
  public GeographyL4 getGeographyL4LatLng(@QueryParam("latitude") Double latitude,
      @QueryParam("longitude") Double longitude) {
    logger.info(GenericMapUtils.LAT + " {} " + GenericMapUtils.LON + " {}", latitude, longitude);
    GeographyL4 geography = null;
    try {
      geography = genericMapService.getGeographyL4LatLng(latitude, longitude);
    } catch (Exception e) {
      logger.error("Exception in getGeographyL4IdByLatLng : {} ", ExceptionUtils.getStackTrace(e));
    }
    return geography;
  }

  /**
   * Gets the geography L 3 lat lng.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the geography L 3 lat lng
   */
  @GET
  @Path("getGeographyL3LatLng")
  @Consumes(MediaType.APPLICATION_JSON)
  public GeographyL3 getGeographyL3LatLng(@QueryParam("latitude") Double latitude,
      @QueryParam("longitude") Double longitude) {
    logger.info(GenericMapUtils.LAT + " {} " + GenericMapUtils.LON + " {}", latitude, longitude);
    GeographyL3 geography = null;
    try {
      geography = genericMapService.getGeographyL3LatLng(latitude, longitude);
    } catch (Exception e) {
      logger.error("Exception in getGeographyL3LatLng : {} ", ExceptionUtils.getStackTrace(e));
    }
    return geography;
  }

  /**
   * Gets the geography L 2 lat lng.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the geography L 2 lat lng
   */
  @GET
  @Path("getGeographyL2LatLng")
  @Consumes(MediaType.APPLICATION_JSON)
  public GeographyL2 getGeographyL2LatLng(@QueryParam("latitude") Double latitude,
      @QueryParam("longitude") Double longitude) {
    logger.info(GenericMapUtils.LAT + " {} " + GenericMapUtils.LON + " {}", latitude, longitude);
    GeographyL2 geography = null;
    try {
      geography = genericMapService.getGeographyL2LatLng(latitude, longitude);
    } catch (Exception e) {
      logger.error("Exception in getGeographyL2IdByLatLng : {} ", ExceptionUtils.getStackTrace(e));
    }
    return geography;
  }

  /**
   * Gets the geography L 1 lat lng.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the geography L 1 lat lng
   */
  @GET
  @Path("getGeographyL1LatLng")
  @Consumes(MediaType.APPLICATION_JSON)
  public GeographyL1 getGeographyL1LatLng(@QueryParam("latitude") Double latitude,
      @QueryParam("longitude") Double longitude) {
    logger.info(GenericMapUtils.LAT + " {} " + GenericMapUtils.LON + " {}", latitude, longitude);
    GeographyL1 geography = null;
    try {
      geography = genericMapService.getGeographyL1LatLng(latitude, longitude);
    } catch (Exception e) {
      logger.error("Exception in getGeographyL1IdByLatLng : {} ", ExceptionUtils.getStackTrace(e));
    }
    return geography;
  }

  /**
   * Gets the nearest geography by lat lng.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the nearest geography by lat lng
   */
  @GET
  @Path("getNearestGeographyL4ByLatLng")
  @Consumes(MediaType.APPLICATION_JSON)
  public GeographyL4 getNearestGeographyByLatLng(@QueryParam("latitude") Double latitude,
      @QueryParam("longitude") Double longitude) {
    logger.info(GenericMapUtils.LAT + " {} " + GenericMapUtils.LON + " {}", latitude, longitude);
    GeographyL4 geography = null;
    try {
      geography = genericMapService.getNearestGeographyL4ByLatLng(latitude, longitude);
    } catch (Exception e) {
      logger.error("Exception in getGeographyL4IdByLatLng : {} ", ExceptionUtils.getStackTrace(e));
    }
    return geography;
  }

  /**
   * Gets the tile image.
   *
   * @param tableName the table name
   * @param tileId the tile id
   * @param imgColumn the img column
   * @param type the type
   * @param isBigTile the is big tile
   * @return the tile image
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @GET
  @Path("getTileImage")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getTileImage(@QueryParam("tableName") String tableName,
      @QueryParam("tileId") String tileId, @QueryParam("imgColumn") String imgColumn,
      @QueryParam("type") String type, @QueryParam("isBigTile") Boolean isBigTile,
      @QueryParam("baseZoom") Integer baseZoom,
      @QueryParam("isColorUpdation") boolean isColorUpdation) {
    logger.info(
        "Getting parameter for tileImage, tableName: {},tileId: {},imageColumn: {},type: {}, isBigTile: {}, isColorUpdation {}",
        tableName, tileId, imgColumn, type, isBigTile, isColorUpdation);
    if (tableName != null && tileId != null && imgColumn != null) {
      Tile tile = new Tile(tileId);
      int zoom = tile.getTz();

      Double accuracyFactor = genericMapService.getAccuracyFactor(zoom, tableName);

      if (baseZoom == null) {
        baseZoom = GenericMapUtils.TILE_BASE_ZOOM;
      }
      byte[] imageByte = genericMapService.getTileImageData(tableName, tileId, imgColumn, type,
          isBigTile, baseZoom, isColorUpdation, accuracyFactor);
      if (imageByte != null) {
        final ResponseBuilder resBuilder = Response.ok(imageByte);
        resBuilder.header(ForesightConstants.CONTENT_TYPE, ForesightConstants.CONTENT_IMG)
            .header("ACCEPT", MediaType.APPLICATION_OCTET_STREAM);
        return resBuilder.build();
      }
    } else {
      logger.error(
          "Invalid parameters tableName {}, tileId {}, imgColumn {}, type {}, isBigTile {}",
          tableName, tileId, imgColumn, type, isBigTile);
    }
    return null;
  }

  @GET
  @Path("getCustomerCareGeography")
  public String getCustomerCareGeagraphy(@NotNull @QueryParam("latitude") Double latitude,
      @NotNull @QueryParam("longitude") Double longitude) {
    logger.info("Getting parameter latitude {}, longitude {}", latitude, longitude);
    String customerCareGeagraphyData =
        genericMapService.getCustomerCareGeagraphyData(latitude, longitude);
    if (customerCareGeagraphyData == null) {
      throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
    }
    return customerCareGeagraphyData;
  }

  @GET
  @Path("getAllGeographyName")
  public String getAllGeographyName(@QueryParam("latitude") Double latitude,
      @QueryParam("longitude") Double longitude) {
    logger.info(GenericMapUtils.LAT + " {} " + GenericMapUtils.LON + " {}", latitude, longitude);
    String allgeo = genericMapService.getAllGeographyName(latitude, longitude);

    if (allgeo == null) {
      throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
    }
    return allgeo;
  }

  /**
   * Gets the point data.
   *
   * @param columnList the column list
   * @param request the request
   * @return the point data
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @POST
  @Path("getPointData")
  public String getPointData(List<String> columnList, @Context HttpServletRequest request)
      throws IOException {
    logger.info("Getting parameter getPointData columnList size {}", columnList.size());
    try {
      String columns = new Gson().toJson(columnList);
      StringEntity entity = new StringEntity(columns, ContentType.APPLICATION_JSON);
      logger.info("inside method getPointData request uri {}  and query string {}",
          request.getRequestURI(), request.getQueryString());
      String dropwizardUrl = Utils.getDropwizardUrlWithPrefix(request);
      return new HttpPostRequest(dropwizardUrl, entity).getString();
    } catch (HttpException e) {
      logger.error("Exception inside getPointData {} ", ExceptionUtils.getStackTrace(e));
    }
    throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
  }

  /**
   * Gets the population and height.
   *
   * @param request the request
   * @return the population and height
   */
  @GET
  @Path("getPopulationAndHeight")
  @Produces(MediaType.APPLICATION_JSON)
  public String getPopulationAndHeight(@Context HttpServletRequest request) {
    try {
      String dropwizardUrl = Utils.getDropwizardUrlWithPrefix(request);
      return new Gson().toJson(new HttpGetRequest(dropwizardUrl).getString());
      // return Response.ok(new HttpGetRequest(dropwizardUrl).getString()).build();
    } catch (Exception exception) {
      logger.error("Exception inside getPopulationAndHeight {} ",
          ExceptionUtils.getStackTrace(exception));
    }
    throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
  }

  @POST
  @Path("getBoundaryByPk")
  public List<Map<String, String>> getBoundaryByPk(GeographyWrapper geoWrapper,
      @QueryParam("tableName") String tableName, @QueryParam("area") String area,
      @QueryParam("type") String type) {
    if (geoWrapper != null) {
      logger.info("tableName {} type {}", tableName, type);

      return genericMapService.getBoundaryByPk(geoWrapper, tableName, area, type);
    } else {
      throw new RestException(ForesightConstants.INVALID_PARAMETERS_JSON);
    }
  }

  @POST
  @Path("getBoundaryDataByRowkeyPrefix")
  public List<Map<String, String>> getBoundaryDataByRowkeyPrefix(GeographyWrapper geoWrapper,
      @NotNull @QueryParam("tableName") String tableName, @QueryParam("area") String area) {
    if (geoWrapper != null) {
      logger.info("Inside getBoundaryDataByRowkeyPrefix , tableName {}, area {}, geoWrapper {}",
          tableName, area, geoWrapper);
      return genericMapService.getBoundaryDataByRowkeyPrefix(geoWrapper, tableName, area);
    } else {
      throw new RestException(ForesightConstants.INVALID_PARAMETERS_JSON);
    }
  }

  @POST
  @Path("/exportKML")
  @Produces(MediaType.MULTIPART_FORM_DATA)
  public Response exportKML(@QueryParam("fileName") String fileName,
      List<Map<String, Object>> placemarkList) {
    logger.info("Inside exportKML, fileName {}, userId {}", fileName);
    byte[] kmlFile = genericMapService.exportKML(fileName, placemarkList);
    ResponseBuilder response = Response.ok(kmlFile);
    response.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    return response.build();
  }

  @POST
  @Path("/importKML")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public String importKML(@Context HttpServletRequest request) {
    logger.info("Inside importKML");
    return new Gson().toJson(genericMapService.importKML(request));
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

    logger.debug("Inside getFbPopulationForKpiSummary Rest");
    return Response.ok(genericMapService.getFbPopulationForKpiSummary(kpiSummaryDataWrapper,
        northEastLat, northEastLong, southWestLat, southWestLong, geographyType, zoomLevel))
        .build();
  }

  @GET
  @Path("sales/getAllGeographyName")
  public String getAllSalesGeographyName(@QueryParam("latitude") Double latitude,
      @QueryParam("longitude") Double longitude) {
    logger.info(GenericMapUtils.LAT + " {} " + GenericMapUtils.LNG + " {}", latitude, longitude);
    String allgeo = genericMapService.getAllSalesGeographyName(latitude, longitude);

    if (allgeo == null) {
      throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
    }
    return allgeo;
  }

  @GET
  @Path("/getNonComplianceParameterOfSite")
  public Response getNonComplianceParameterOfSite(@QueryParam("rowKey") String rowKey,
      @QueryParam("tableName") String tableName, @QueryParam("columnName") String columnName) {
    logger.info("inside getNonComplianceParameterOfSite rowkey : {} , tableName : {} ", rowKey,
        tableName);
    return Response
        .ok(genericMapService.getNonComplianceParameterOfSite(rowKey, tableName, columnName))
        .build();
  }
  
  @POST
  @Path("getRouteData")
  public Map<String,String> getRouteData(GeographyWrapper geoWrapper) {
    if (geoWrapper != null) {
      logger.info("geoWrapper  {}", geoWrapper);

      return genericMapService.getRouteData(geoWrapper);
    } else {
      throw new RestException(ForesightConstants.INVALID_PARAMETERS_JSON);
    }
  }
  
  @GET
  @Path("getAllGeographyDisplayName")
  public String getAllGeographyDisplayName(@QueryParam("latitude") Double latitude,
      @QueryParam("longitude") Double longitude) {
    logger.info(GenericMapUtils.LAT + " {} " + GenericMapUtils.LON + " {}", latitude, longitude);
    String allgeo = genericMapService.getAllGeographyDisplayName(latitude, longitude);

    if (allgeo == null) {
      throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
    }
    return allgeo;
  }


}
