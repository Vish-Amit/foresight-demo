package com.inn.foresight.module.nv.device.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;

/** The Class NVDeviceCountWrapper. */
@JpaWrapper
@RestWrapper
public class NVDeviceCountWrapper {
	/** The count. */
	private Long count;

	/** The geography name. */
	private String geographyName;

	private Integer geographyId;

	/** The latitude. */
	private Double latitude;

	/** The longitude. */
	private Double longitude;

	/**
	 * Instantiates a new NV device count wrapper.
	 *
	 * @param count
	 *            the count
	 * @param geographyName
	 *            the geography name
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 */
	public NVDeviceCountWrapper(Long count, String geographyName, Double latitude, Double longitude,
			Integer geographyId) {
		super();
		this.count = count;
		this.geographyName = geographyName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.geographyId = geographyId;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count
	 *            the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
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
	 * @param geographyName
	 *            the geographyName to set
	 */
	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
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
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
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
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the geographyId
	 */
	public Integer getGeographyId() {
		return geographyId;
	}

	/**
	 * @param geographyId the geographyId to set
	 */
	public void setGeographyId(Integer geographyId) {
		this.geographyId = geographyId;
	}
	

}
