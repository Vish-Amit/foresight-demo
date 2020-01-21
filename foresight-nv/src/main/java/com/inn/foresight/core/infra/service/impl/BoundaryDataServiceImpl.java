
package com.inn.foresight.core.infra.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.inn.commons.maps.LatLng;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.BoundaryDataDao;
import com.inn.foresight.core.infra.service.BoundaryDataService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;

/**
 * The Class BoundaryDataServiceImpl.
 */
@Service("BoundaryDataServiceImpl")
public class BoundaryDataServiceImpl implements BoundaryDataService {

    /** The Constant IS_LARGE_POLYGON_ID. */
    private static final int IS_LARGE_POLYGON_ID = 1;
    
    private Logger logger = LogManager.getLogger(BoundaryDataServiceImpl.class);

    /** The boundary data dao. */
    @Autowired
    private BoundaryDataDao boundaryDataDao;

    /**
     * Find geography name by lat long.
     *
     * @param lat the lat
     * @param lng the lng
     * @param zoomLevel the zoom level
     * @return the map
     * @throws IOException Signals that an I/O exception has occurred.
     * @deprecated replaced by {@link BoundaryDataService#findGeography(LatLng, int)}
     */
    @SuppressWarnings("all")
    @Deprecated
    @Override
    public Map<String, String> findGeographyNameByLatLong(Double lat, Double lng, Integer zoomLevel)
            throws IOException {
        LatLng location = new LatLng();
        location.setLatitude(lat);
        location.setLongitude(lng);
        return boundaryDataDao.findGeographyNameByLatLong(location, zoomLevel);
    }

    /**
     * Gets the geography data by row key prefix.
     *
     * @param geoLevel the geo level
     * @param rowKeyPrefix the row key prefix
     * @return the geography data by row key prefix
     */
    @Override
    public Map<String, String> getGeographyDataByRowKeyPrefix(String geoLevel, String rowKeyPrefix) {
        return boundaryDataDao.getGeographyDataByRowKey(geoLevel, rowKeyPrefix + IS_LARGE_POLYGON_ID);
    }

    /**
     * Gets the geography data by row key prefix.
     *
     * @param geoLevel the geo level
     * @param rowKeyPrefix the row key prefix
     * @param zoom the zoom
     * @return the geography data by row key prefix
     */
    @Override
    public List<Map<String, String>> getGeographyDataByRowKeyPrefix(String geoLevel, String rowKeyPrefix, Integer zoom) {
        return boundaryDataDao.getGeographyDataByRowKeyPrefix(geoLevel, rowKeyPrefix, zoom);
    }

    /**
     * Find geography L 1.
     *
     * @param location the location
     * @return the string
     */
    @Override
    public String findGeographyL1(LatLng location) {
        return boundaryDataDao.findGeographyL1(location);
    }

    /**
     * Find geography L 2.
     *
     * @param location the location
     * @return the string
     */
    @Override
    public String findGeographyL2(LatLng location) {
        return boundaryDataDao.findGeographyL2(location);
    }

    /**
     * Find geography L 3.
     *
     * @param location the location
     * @return the string
     */
    @Override
    public String findGeographyL3(LatLng location) {
        return boundaryDataDao.findGeographyL3(location);
    }

    /**
     * Find geography L 4.
     *
     * @param location the location
     * @return the string
     */
    @Override
    public String findGeographyL4(LatLng location) {
        return boundaryDataDao.findGeographyL4(location);
    }

    /**
     * Find geography.
     *
     * @param location the location
     * @param geoLevel the geo level
     * @return the string
     */
    @Override
    public String findGeography(LatLng location, String geoLevel) {
        return boundaryDataDao.findGeography(location, geoLevel);
    }

    /**
     * Find geography.
     *
     * @param location the location
     * @param zoomLevel the zoom level
     * @return the map
     */
    @Override
    public Map<String, String> findGeography(LatLng location, int zoomLevel) {
        return boundaryDataDao.findGeography(location, zoomLevel);
    }
    
    @Override
    public String refreshGeographyMap(String geographyType) {
       try {
        if(StringUtils.isNotEmpty(geographyType)) {
          if(GenericMapUtils.NW_GEOGRAPHY_TYPE.equalsIgnoreCase(geographyType)) {
          boundaryDataDao.refreshGeographyMap(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L1_TYPE, GenericMapUtils.L1_TYPE);
          boundaryDataDao.refreshGeographyMap(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L2_TYPE, GenericMapUtils.L2_TYPE);
          }else if(GenericMapUtils.SALES_GEOGRAPHY_TYPE.equalsIgnoreCase(geographyType)) {
          boundaryDataDao.refreshGeographyMap(GenericMapUtils.SALES_GEOGRAPHY_TABLE, GenericMapUtils.L1_TYPE, ForesightConstants.L1_SALES);
          boundaryDataDao.refreshGeographyMap(GenericMapUtils.SALES_GEOGRAPHY_TABLE, GenericMapUtils.L2_TYPE, ForesightConstants.L2_SALES);
          }
          return ForesightConstants.SUCCESS_JSON;
        }else {
          throw new RestException("Error in refreshing geographiesMap");
        }
      } catch (Exception e) {
        logger.error("Exception inside refreshGeographyMap due to {}",ExceptionUtils.getStackTrace(e));
      }
       throw new RestException("Unable to refresh geography map"); 
    }

    @Override
    public void refreshGeographyMapByDate() {
      boundaryDataDao.refreshGeographyMapByDate();
    }
}
