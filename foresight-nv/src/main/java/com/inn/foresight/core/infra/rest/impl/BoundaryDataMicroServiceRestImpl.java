
package com.inn.foresight.core.infra.rest.impl;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.inn.commons.maps.LatLng;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.infra.service.BoundaryDataService;
import com.inn.foresight.core.infra.utils.InfraConstants;

/**
 * The Class BoundaryDataRestImpl.
 */
@Path("/ms/BoundaryData")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoundaryDataMicroServiceRestImpl {

  /** The logger. */
  private Logger logger = LogManager.getLogger(BoundaryDataMicroServiceRestImpl.class);

  /** The BoundaryDataService service. */
  @Autowired
  private BoundaryDataService boundaryDataService;

  /**
   * Find geography name by lat long.
   *
   * @param lat the lat
   * @param lng the lng
   * @param zoomLevel the zoom level
   * @return the map
   * @throws RestException the rest exception
   * @deprecated replaced by
   *             {@link BoundaryDataMicroServiceRestImpl#findGeography(Double, Double, Integer)}
   */
  @Deprecated
  @SuppressWarnings("all")
  @GET
  @Path("findGeographyNameByLatLong")
  public Map<String, String> findGeographyNameByLatLong(@QueryParam("lat") Double lat,
      @QueryParam("lng") Double lng, @QueryParam("zoomLevel") Integer zoomLevel) {
    logger.info("Going to findGeographyNameByLatLong for lat: {} , lng : {}, zoomLevel: {}   ", lat,
        lng, zoomLevel);
    try {
      if (lat == null && lng == null && zoomLevel == null) {
        logger.info("provide values for lat: {} ,lng : {} ,zoomLevel: {}", lat, lng, zoomLevel);
        throw new RestException(InfraConstants.EXCEPTION_INVALID_PARAMS);
      }
      return boundaryDataService.findGeographyNameByLatLong(lat, lng, zoomLevel);
    } catch (RestException e) {
      throw e;
    } catch (Exception e) {
      logger.error(
          "Exception while getting findGeographyNameByLatLong for lat: {} ,lng : {}, zoomLevel: {}, Error: {}   ",
          lat, lng, zoomLevel, ExceptionUtils.getStackTrace(e));
      throw new RestException(InfraConstants.EXCEPTION_INVALID_PARAMS);
    }
  }

  /**
   * Gets the geography data by row key prefix.
   *
   * @param geoLevel the geo level
   * @param rowKeyPrefix the row key prefix
   * @return the geography data by row key prefix
   */
  @GET
  @Path("getGeographyDataByRowKeyPrefix/{geoLevel}/{rowKeyPrefix}")
  public Map<String, String> getGeographyDataByRowKeyPrefix(@PathParam("geoLevel") String geoLevel,
      @PathParam("rowKeyPrefix") String rowKeyPrefix) {
    logger.info("Inside getGeographyDataByRowKeyPrefix method GeoLevel {}, rowKeyPrefix {}",
        geoLevel, rowKeyPrefix);
    return boundaryDataService.getGeographyDataByRowKeyPrefix(geoLevel, rowKeyPrefix);
  }

  /**
   * Gets the multi geography data by row key prefix.
   *
   * @param geoLevel the geo level
   * @param rowKeyPrefix the row key prefix
   * @param zoom the zoom
   * @return the multi geography data by row key prefix
   */
  @GET
  @Path("getGeographyDataByRowKeyPrefix/{geoLevel}/{rowKeyPrefix}/{zoom}")
  public List<Map<String, String>> getMultiGeographyDataByRowKeyPrefix(
      @PathParam("geoLevel") String geoLevel, @PathParam("rowKeyPrefix") String rowKeyPrefix,
      @PathParam("zoom") Integer zoom) {
    logger.info(
        "Inside getGeographyDataByRowKeyPrefix method GeoLevel {}, rowKeyPrefix {},zoom {} ",
        geoLevel, rowKeyPrefix, zoom);
    return boundaryDataService.getGeographyDataByRowKeyPrefix(geoLevel, rowKeyPrefix, zoom);
  }

  /**
   * Find geography L 1.
   *
   * @param lat the lat
   * @param lng the lng
   * @return the string
   */
  @GET
  @Path("findGeographyL1")
  public String findGeographyL1(@NotNull @QueryParam("lat") Double lat,
      @NotNull @QueryParam("lng") Double lng) {
    logger.info("findGeographyL1 for lat {}, lng {}", lat, lng);
    return boundaryDataService.findGeographyL1(new LatLng(lat, lng));
  }

  /**
   * Find geography L 2.
   *
   * @param lat the lat
   * @param lng the lng
   * @return the string
   */
  @GET
  @Path("findGeographyL2")
  public String findGeographyL2(@NotNull @QueryParam("lat") Double lat,
      @NotNull @QueryParam("lng") Double lng) {
    logger.info("findGeographyL2 for lat {}, lng {}", lat, lng);
    return boundaryDataService.findGeographyL2(new LatLng(lat, lng));
  }

  /**
   * Find geography L 3.
   *
   * @param lat the lat
   * @param lng the lng
   * @return the string
   */
  @GET
  @Path("findGeographyL3")
  public String findGeographyL3(@NotNull @QueryParam("lat") Double lat,
      @NotNull @QueryParam("lng") Double lng) {
    logger.info("findGeographyL3 for lat {}, lng {}", lat, lng);
    return boundaryDataService.findGeographyL3(new LatLng(lat, lng));
  }

  /**
   * Find geography L 4.
   *
   * @param lat the lat
   * @param lng the lng
   * @return the string
   */
  @GET
  @Path("findGeographyL4")
  public String findGeographyL4(@QueryParam("lat") Double lat, @QueryParam("lng") Double lng) {
    logger.info("findGeographyL4 for lat {}, lng {}", lat, lng);
    return boundaryDataService.findGeographyL4(new LatLng(lat, lng));
  }

  /**
   * Find geography.
   *
   * @param lat the lat
   * @param lng the lng
   * @param geoLevel the geo level
   * @return the string
   */
  @GET
  @Path("findGeography")
  public String findGeography(@NotNull @QueryParam("lat") Double lat,
      @NotNull @QueryParam("lng") Double lng, @NotNull @QueryParam("geoLevel") String geoLevel) {
    logger.info("findGeography for lat {}, lng {}, geoLevel {}", lat, lng, geoLevel);
    return boundaryDataService.findGeography(new LatLng(lat, lng), geoLevel);
  }

  /**
   * Find geography.
   *
   * @param lat the lat
   * @param lng the lng
   * @param zoomLevel the zoom level
   * @return the map
   */
  @GET
  @Path("findGeographyLocation")
  public Map<String, String> findGeography(@NotNull @QueryParam("lat") Double lat,
      @NotNull @QueryParam("lng") Double lng, @NotNull @QueryParam("zoomLevel") Integer zoomLevel) {
    logger.info("findGeography for lat {}, lng {}, zoomLevel {}", lat, lng, zoomLevel);
    return boundaryDataService.findGeography(new LatLng(lat, lng), zoomLevel);
  }

  @GET
  @Path("refreshGeographyMap")
  public String refreshGeographyMap(@NotNull @QueryParam("geographyType") String geographyType) {
    logger.info("Inside refreshGeographyMap rest for geographyType {}", geographyType);
    return boundaryDataService.refreshGeographyMap(geographyType);
  }

  @GET
  @Path("refreshGeographyMapByDate")
  public void refreshGeographyMapByDate() {
    boundaryDataService.refreshGeographyMapByDate();
  }
}
