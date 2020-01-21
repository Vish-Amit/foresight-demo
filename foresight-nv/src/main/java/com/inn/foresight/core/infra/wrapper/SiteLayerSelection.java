package com.inn.foresight.core.infra.wrapper;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.wrapper.RestWrapper;
@RestWrapper

public class SiteLayerSelection{
		
	private Integer displaySite;
	
	private String nestatus;
    
	private Double radius;
	
	private Double latitude;
	
	private Double longitude;
		
	private Map<String,List<Map>> filters;
	
	private  Map<String,List<String>> projection;
	
	private Map<String,List> groupByColumns;
	
	private boolean isDistinct;
	
	private Map<String,List> geographyMap;
	
	private Map<String,Double> viewportMap;
	
	private Map<String,List> orderByColumns;
	
	private List<String> geography;
	
	private List polygon;
	
	private List vendor;
	
	private boolean isGroupBy;
	
	public Integer getDisplaySite() {
		return displaySite;
	}

	public void setDisplaySite(Integer displaySite) {
		this.displaySite = displaySite;
	}

	public Map<String, List<Map>> getFilters() {
		return filters;
	}
																																																													
	public void setFilters(Map<String, List<Map>> filters) {
		this.filters = filters;
	}

	public Map<String, List<String>> getProjection() {
		return projection;
	}

	public void setProjection(Map<String, List<String>> projection) {
		this.projection = projection;
	}
	
	public Boolean getIsDistinct() {
		return isDistinct;
	}

	public void setIsDistinct(Boolean isDistinct) {
		this.isDistinct = isDistinct;
	}
	
	public Map<String, List> getGroupByColumns() {
		return groupByColumns;
	}

	public void setGroupByColumns(Map<String, List> groupByColumns) {
		this.groupByColumns = groupByColumns;
	}

	public Map<String, List> getGeographyMap() {
		return geographyMap;
	}

	public void setGeographyMap(Map<String, List> geographyMap) {
		this.geographyMap = geographyMap;
	}

	public Map<String, Double> getViewportMap() {
		return viewportMap;
	}

	public void setViewportMap(Map<String, Double> viewportMap) {
		this.viewportMap = viewportMap;
	}

	public Map<String, List> getOrderByColumns() {
		return orderByColumns;
	}

	public void setOrderByColumns(Map<String, List> orderByColumns) {
		this.orderByColumns = orderByColumns;
	}

	public void setDistinct(boolean isDistinct) {
		this.isDistinct = isDistinct;
	}

	public List getGeography() {
		return geography;
	}

	public List getPolygon() {
		return polygon;
	}

	public void setPolygon(List polygon) {
		this.polygon = polygon;
	}

	public List getVendor() {
		return vendor;
	}

	public void setVendor(List vendor) {
		this.vendor = vendor;
	}

	public String getNestatus() {
		return nestatus;
	}

	public void setNestatus(String nestatus) {
		this.nestatus = nestatus;
	}

	public void setGeography(List<String> geography) {
		this.geography = geography;
	}

	public boolean getIsGroupBy() {
		return isGroupBy;
	}

	public void setIsGroupBy(boolean isGroupBy) {
		this.isGroupBy = isGroupBy;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

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

	public void setGroupBy(boolean isGroupBy) {
		this.isGroupBy = isGroupBy;
	}

	@Override
	public String toString() {
		return "SiteLayerSelection [displaySite=" + displaySite + ", nestatus=" + nestatus + ", radius=" + radius + ", latitude=" + latitude + ", longitude=" + longitude + ", filters=" + filters
				+ ", projection=" + projection + ", groupByColumns=" + groupByColumns + ", isDistinct=" + isDistinct + ", geographyMap=" + geographyMap + ", viewportMap=" + viewportMap
				+ ", orderByColumns=" + orderByColumns + ", geography=" + geography + ", polygon=" + polygon + ", vendor=" + vendor + ", isGroupBy=" + isGroupBy + "]";
	}

	

}