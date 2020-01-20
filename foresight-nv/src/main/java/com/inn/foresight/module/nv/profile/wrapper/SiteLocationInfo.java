package com.inn.foresight.module.nv.profile.wrapper;

public class SiteLocationInfo {
	private String geographyL4Name;
	private String geographyL3Name;
	private String geographyL2Name;
	private String geographyL1Name;
	private Double latitude;
	private Double longitude;
	/**
	 * @return the geographyL4Name
	 */
	public String getGeographyL4Name() {
		return geographyL4Name;
	}
	/**
	 * @param geographyL4Name the geographyL4Name to set
	 */
	public void setGeographyL4Name(String geographyL4Name) {
		this.geographyL4Name = geographyL4Name;
	}
	/**
	 * @return the geographyL3Name
	 */
	public String getGeographyL3Name() {
		return geographyL3Name;
	}
	/**
	 * @param geographyL3Name the geographyL3Name to set
	 */
	public void setGeographyL3Name(String geographyL3Name) {
		this.geographyL3Name = geographyL3Name;
	}
	/**
	 * @return the geographyL2Name
	 */
	public String getGeographyL2Name() {
		return geographyL2Name;
	}
	/**
	 * @param geographyL2Name the geographyL2Name to set
	 */
	public void setGeographyL2Name(String geographyL2Name) {
		this.geographyL2Name = geographyL2Name;
	}
	/**
	 * @return the geographyL1Name
	 */
	public String getGeographyL1Name() {
		return geographyL1Name;
	}
	/**
	 * @param geographyL1Name the geographyL1Name to set
	 */
	public void setGeographyL1Name(String geographyL1Name) {
		this.geographyL1Name = geographyL1Name;
	}
	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return "SiteLocationInfo [geographyL4Name=" + geographyL4Name + ", geographyL3Name=" + geographyL3Name
				+ ", geographyL2Name=" + geographyL2Name + ", geographyL1Name=" + geographyL1Name + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}

}
