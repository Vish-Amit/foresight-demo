package com.inn.foresight.module.nv.dashboard.wrapper;

import com.inn.core.generic.wrapper.RestWrapper;

/** The Class TopGeographyWrapper. */
@RestWrapper
public class TopGeographyWrapper {

/** The geography name. */
private String geographyName;

/** The kpi value. */
private Double kpiValue;

/** The lat. */
private Double lat;

/** The lon. */
private Double lon;

/**
 * Gets the kpi value.
 *
 * @return the kpiValue
 */
public Double getKpiValue() {
	return kpiValue;
}

/**
 * Sets the kpi value.
 *
 * @param kpiValue the kpiValue to set
 */
public void setKpiValue(Double kpiValue) {
	this.kpiValue = kpiValue;
}

/**
 * Gets the geography name.
 *
 * @return the geographyName
 */
public String getGeographyName() {
	return geographyName;
}

/**
 * Sets the geography name.
 *
 * @param geographyName the geographyName to set
 */
public void setGeographyName(String geographyName) {
	this.geographyName = geographyName;
}

/**
 * Gets the lat.
 *
 * @return the lat
 */
public Double getLat() {
	return lat;
}

/**
 * Sets the lat.
 *
 * @param lat the lat to set
 */
public void setLat(Double lat) {
	this.lat = lat;
}

/**
 * Gets the lon.
 *
 * @return the lon
 */
public Double getLon() {
	return lon;
}

/**
 * Sets the lon.
 *
 * @param lon the lon to set
 */
public void setLon(Double lon) {
	this.lon = lon;
}

/**
 * To string.
 *
 * @return the string
 */
@Override
public String toString() {
	return "TopGeographyWrapper [kpiValue=" + kpiValue + ", geographyName=" + geographyName + ", lat=" + lat + ", lon="
			+ lon + "]";
}

}
