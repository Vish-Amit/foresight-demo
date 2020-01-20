package com.inn.foresight.module.nv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inn.core.generic.wrapper.RestWrapper;

/** The Class LocationDetailWrapper. */
@JsonInclude(Include.NON_NULL)
@RestWrapper
public class LocationDetailWrapper {

	/** The cell id. */
	private Integer cellId;
	
	/** The mnc. */
	private Integer mnc;
	
	/** The mcc. */
	private Integer mcc;
	
	/** The longitude. */
	private Double longitude;
	
	/** The latitude. */
	private Double latitude;
	
	/** The geo L 1. */
	private String geoL1;
	
	/** The geo L 2. */
	private String geoL2;
	
	/** The geo L 3. */
	private String geoL3;
	
	/** The geo L 4. */
	private String geoL4;
	
	
	public LocationDetailWrapper() {
	}
	
	public LocationDetailWrapper(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Gets the mnc.
	 *
	 * @return the mnc
	 */
	public Integer getMnc() {
		return mnc;
	}
	
	/**
	 * Sets the mnc.
	 *
	 * @param mnc the new mnc
	 */
	public void setMnc(Integer mnc) {
		this.mnc = mnc;
	}
	
	/**
	 * Gets the cell id.
	 *
	 * @return the cell id
	 */
	public Integer getCellId() {
		return cellId;
	}
	
	/**
	 * Sets the cell id.
	 *
	 * @param cellId the new cell id
	 */
	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}
	
	/**
	 * Gets the mcc.
	 *
	 * @return the mcc
	 */
	public Integer getMcc() {
		return mcc;
	}
	
	/**
	 * Sets the mcc.
	 *
	 * @param mcc the new mcc
	 */
	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}
	
	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}
	
	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	
	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Gets the geo L 1.
	 *
	 * @return the geo L 1
	 */
	public String getGeoL1() {
		return geoL1;
	}
	
	/**
	 * Sets the geo L 1.
	 *
	 * @param geoL1 the new geo L 1
	 */
	public void setGeoL1(String geoL1) {
		this.geoL1 = geoL1;
	}
	
	/**
	 * Gets the geo L 2.
	 *
	 * @return the geo L 2
	 */
	public String getGeoL2() {
		return geoL2;
	}
	
	/**
	 * Sets the geo L 2.
	 *
	 * @param geoL2 the new geo L 2
	 */
	public void setGeoL2(String geoL2) {
		this.geoL2 = geoL2;
	}
	
	/**
	 * Gets the geo L 3.
	 *
	 * @return the geo L 3
	 */
	public String getGeoL3() {
		return geoL3;
	}
	
	/**
	 * Sets the geo L 3.
	 *
	 * @param geoL3 the new geo L 3
	 */
	public void setGeoL3(String geoL3) {
		this.geoL3 = geoL3;
	}
	
	/**
	 * Gets the geo L 4.
	 *
	 * @return the geo L 4
	 */
	public String getGeoL4() {
		return geoL4;
	}
	
	/**
	 * Sets the geo L 4.
	 *
	 * @param geoL4 the new geo L 4
	 */
	public void setGeoL4(String geoL4) {
		this.geoL4 = geoL4;
	}
	
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "LocationDetailWrapper [mnc=" + mnc + ", cellId=" + cellId + ", mcc=" + mcc + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", geoL1=" + geoL1 + ", geoL2=" + geoL2 + ", geoL3=" + geoL3 + ", geoL4="
				+ geoL4 + "]";
	}
	
	
	
	
	
	
	
}

