package com.inn.foresight.core.infra.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.hbase.client.Scan;
import com.inn.commons.maps.LatLng;
import com.inn.foresight.core.infra.wrapper.Geography;

/**
 * The Interface BoundaryDataDao.
 */
public interface BoundaryDataDao {

    /**
     * Find geography name by lat long.
     *
     * @param location the location
     * @param zoomLevel the zoom is used for decide Geography Level. Zoom Level 5-6 (L1),7-8 (L2),9-10 (L3),11-18 (L4)
     * @return the map.Possible value {L1=gn},{L2=gn},{L3=gn},{L4=gn}
     * @throws IOException Signals that an I/O exception has occurred.
     * @deprecated replaced by {@link BoundaryDataDao#findGeography(LatLng, int)}
     */
	@SuppressWarnings("all")
	@Deprecated
    Map<String, String> findGeographyNameByLatLong(LatLng location, int zoomLevel) throws IOException;

    /**
     * Gets the geography data by row key. This call is used in Advance Search API for search geography data and return
     * single boundary.
     *
     * @param geoLevel the geo level
     * @param rowKey the row key
     * @return the geography data (large boundary) by row key
     */
    Map<String, String> getGeographyDataByRowKey(String geoLevel, String rowKey);

    /**
     * Gets the geography data by row key prefix. This call is also used in Advance Search API for search geography data
     * and return multiple boundary.
     *
     * @param geoLevel the geo level
     * @param rowKeyPrefix the row key prefix
     * @param zoom the zoom
     * @return the geography data by row key prefix
     */
    List<Map<String, String>> getGeographyDataByRowKeyPrefix(String geoLevel, String rowKeyPrefix, Integer zoom);

    /**
     * Gets the customer care geography.
     *
     * @param tableName the table name
     * @param location the location
     * @return the customer care geography
     */
    String getCustomerCareGeography(String tableName, LatLng location);

    /**
     * Find geography L 1 by location.
     *
     * @param location the location
     * @return the string
     */
    String findGeographyL1(LatLng location);

    /**
     * Find geography L 2 by location.
     *
     * @param location the location
     * @return the string
     */
    String findGeographyL2(LatLng location);

    /**
     * Find geography L 3 by location.
     *
     * @param location the location
     * @return the string
     */
    String findGeographyL3(LatLng location);

    /**
     * Find geography L 4 by location.
     *
     * @param location the location
     * @return the string
     */
    String findGeographyL4(LatLng location);

    /**
     * Find geography.
     *
     * @param location the location
     * @param geoLevel the geo level
     * @return the string
     */
    String findGeography(LatLng location, String geoLevel);

    /**
     * Find geography by location.
     *
     * @param location the location
     * @param zoomLevel the zoom is used for decide Geography Level. Zoom Level 5-6 (L1),7-8 (L2),9-10 (L3),11-18 (L4)
     * @return the map o/p {geoName=gn, geoLevel=GeographyL1} the map
     */
    Map<String, String> findGeography(LatLng location, int zoomLevel);

    /**
     * Gets the geographies.
     *
     * @param scan the scan
     * @param tableName the table name
     * @return the geographies
     */
    List<Geography> getGeographies(Scan scan, String tableName);
    
    /**
     * Gets the nearest geography locations.
     *
     * @param location the location
     * @param geoLevel the geo level
     * @return the nearest geography locations
     */
    String getNearestGeographyLocations(LatLng location, String geoLevel);
    
    /**
     * Gets the nearest geography locations.
     *
     * @param location the location
     * @param zoomLevel the zoom level
     * @return the nearest geography locations
     */
    public String getNearestGeographyLocations(LatLng location, int zoomLevel);

    String getTableName(String geoLevel);

	/**
	 * Find sales geography L 1.
	 *
	 * @param location the location
	 * @return the string
	 */
	String findSalesL1(LatLng location);

	/**
	 * Find sales geography L 2.
	 *
	 * @param location the location
	 * @return the string
	 */
	String findSalesL2(LatLng location);

	/**
	 * Find sales geography L 3.
	 *
	 * @param location the location
	 * @return the string
	 */
	String findSalesL3(LatLng location);

	/**
	 * Find sales geography L 4.
	 *
	 * @param location the location
	 * @return the string
	 */
	String findSalesL4(LatLng location);

	String getLatestBoundaryDate(String tableName, String geoLevel);
	
	void refreshGeographyMap(String tableName, String dateType, String mapType);

	void refreshGeographyMapByDate();

  Geography findL2(LatLng location);

  Geography findL1(LatLng location);

  Geography findL3(LatLng location);

  Geography findL4(LatLng location);
}