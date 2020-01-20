package com.inn.foresight.module.nv.report.workorder.wrapper;

public class SiteDataWrapper {


	public SiteDataWrapper(Double latitude, Double longitude, Integer azimuth, Integer pci, Integer cellid) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.azimuth = azimuth;
		this.pci = pci;
		this.cellid = cellid;
	}


	private Double latitude;
	private Double longitude;
	private Integer azimuth;
	private Integer pci;
	private Integer cellid;


	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Integer getAzimuth() {
		return azimuth;
	}
	public void setAzimuth(Integer azimuth) {
		this.azimuth = azimuth;
	}
	public Integer getPci() {
		return pci;
	}
	public void setPci(Integer pci) {
		this.pci = pci;
	}
	public Integer getCellid() {
		return cellid;
	}
	public void setCellid(Integer cellid) {
		this.cellid = cellid;
	}


	@Override
	public String toString() {
		return "SiteDataWrapper [latitude=" + latitude + ", longitude=" + longitude + ", azimuth=" + azimuth + ", pci="
				+ pci + ", cellid=" + cellid + "]";
	}

}
