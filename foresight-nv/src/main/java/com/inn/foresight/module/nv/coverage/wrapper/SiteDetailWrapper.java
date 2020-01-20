package com.inn.foresight.module.nv.coverage.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SiteDetailWrapper {

	private String siteName;
	private Double lat;
	private Double lon;
	private String neStatus;
	private Long modificationTime;
	private String softwareVersion;
	private String neType;
	private Integer tiReady;
	private Long onAirDate;
	
	private Long tiReadyDate;
	
	
	private String floorName;
	private String buildingName;
	
	


	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public Long getTiReadyDate() {
		return tiReadyDate;
	}

	public void setTiReadyDate(Long tiReadyDate) {
		this.tiReadyDate = tiReadyDate;
	}

	public Long getOnAirDate() {
		return onAirDate;
	}

	public void setOnAirDate(Long onAirDate) {
		this.onAirDate = onAirDate;
	}

	public Integer getTiReady() {
		return tiReady;
	}

	public void setTiReady(Integer tiReady) {
		this.tiReady = tiReady;
	}

	private List<CellDetailWrapper> cells=new ArrayList<>();
	
	
	public List<CellDetailWrapper> getCells() {
		return cells;
	}

	public void setCells(List<CellDetailWrapper> cells) {
		this.cells = cells;
	}

	public String getNeType() {
		return neType;
	}

	public void setNeType(String neType) {
		this.neType = neType;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public Long getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Long modificationTime) {
		this.modificationTime = modificationTime;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public String getNeStatus() {
		return neStatus;
	}

	public void setNeStatus(String neStatus) {
		this.neStatus = neStatus;
	}

	@Override
	public String toString() {
		return "SiteDetailWrapper [siteName=" + siteName + ", lat=" + lat + ", lon=" + lon + ", neStatus=" + neStatus
				+ ", modificationTime=" + modificationTime + ", softwareVersion=" + softwareVersion + ", neType="
				+ neType + ", tiReady=" + tiReady + ", onAirDate=" + onAirDate + ", tiReadyDate=" + tiReadyDate
				+ ", floorName=" + floorName + ", buildingName=" + buildingName + ", cells=" + cells + "]";
	}

	

}
