package com.inn.foresight.core.infra.wrapper;

public class MacroSitesDetailWrapper {

	private String ecgi;

	private LocationDetailWrapper locationDetailWrapper;

	private Double latitude;

	private Double longitude;

	public MacroSitesDetailWrapper(String ecgi, Double latitude, Double longitude) {
		this.ecgi = ecgi;
		this.locationDetailWrapper = new LocationDetailWrapper(latitude, longitude);
	}

	public String getEcgi() {
		return ecgi;
	}

	public void setEcgi(String ecgi) {
		this.ecgi = ecgi;
	}

	public Double getLattitude() {
		return latitude;
	}

	public void setLattitude(Double lattitude) {
		this.latitude = lattitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public LocationDetailWrapper getLocationDetailWrapper() {
		return locationDetailWrapper;
	}

	public void setLocationDetailWrapper(LocationDetailWrapper locationDetailWrapper) {
		this.locationDetailWrapper = locationDetailWrapper;
	}

	@Override
	public String toString() {
		return "MacroSiteDetailWrapper [ecgi=" + ecgi + ", locationDetailWrapper=" + locationDetailWrapper
				+ ", lattitude=" + latitude + ", longitude=" + longitude + "]";
	}



}
