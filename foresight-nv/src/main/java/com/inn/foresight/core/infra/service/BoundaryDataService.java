
package com.inn.foresight.core.infra.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.inn.commons.maps.LatLng;

/**
 * The Interface BoundaryDataService.
 */
public interface BoundaryDataService {

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
    Map<String, String> findGeographyNameByLatLong(Double lat, Double lng, Integer zoomLevel) throws IOException;

    /**
     * Gets the geography data by row key prefix.
     *
     * @param geoLevel the geo level
     * @param rowKeyPrefix the row key prefix
     * @return the geography data by row key prefix
     */
    Map<String, String> getGeographyDataByRowKeyPrefix(String geoLevel, String rowKeyPrefix);

    /**
     * Gets the geography data by row key prefix.
     *
     * @param geoLevel the geo level
     * @param rowKeyPrefix the row key prefix
     * @param zoom the zoom
     * @return the geography data by row key prefix
     */
    List<Map<String, String>> getGeographyDataByRowKeyPrefix(String geoLevel, String rowKeyPrefix, Integer zoom);

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
    
    String refreshGeographyMap(String geographyType);

	void refreshGeographyMapByDate();
}