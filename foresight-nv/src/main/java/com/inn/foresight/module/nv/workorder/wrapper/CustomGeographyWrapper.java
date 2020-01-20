package com.inn.foresight.module.nv.workorder.wrapper;

import java.util.Date;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.module.nv.workorder.model.CustomGeography;
import com.inn.foresight.module.nv.workorder.model.CustomGeography.GeographyType;

/** The Class CustomBoundaryWrapper. */
@RestWrapper
@JpaWrapper
public class CustomGeographyWrapper {
	
	
	private Integer id;

	/** The child geography. */
	private CustomGeography childGeography;

	/** The creation time. */
	private Date creationTime;

	/** The modified time. */
	private Date modifiedTime;
	
	/** The name. */
	private String name;
	
	/** The type. */
	private GeographyType type;

	/** The type. */
	private String routeType;
	
	/** The landmark. */
	private String landmark;
	
	/** The landmark. */
	private Double startLatitude;
	
	/** The landmark. */
	private Double startLongitude;
	
	/** The landmark. */
	private Double endLatitude;
	
	/** The landmark. */
	private Double endLongitude;
	
	/** The boundary. */
	private String boundary;
	
	/** The points between routes. */
	private String routePoints;
	
	private String boundaryPoints;
	
	

	public CustomGeographyWrapper(Integer id, Date creationTime, Date modifiedTime, String name, GeographyType type,
			String routeType, String landmark, Double startLatitude, Double startLongitude, Double endLatitude,
			Double endLongitude) {
		super();
		this.id = id;
		this.creationTime = creationTime;
		this.modifiedTime = modifiedTime;
		this.name = name;
		this.type = type;
		this.routeType = routeType;
		this.landmark = landmark;
		this.startLatitude = startLatitude;
		this.startLongitude = startLongitude;
		this.endLatitude = endLatitude;
		this.endLongitude = endLongitude;
	}

	public CustomGeographyWrapper() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GeographyType getType() {
		return type;
	}

	public void setType(GeographyType type) {
		this.type = type;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getBoundary() {
		return boundary;
	}
	
	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CustomGeography getChildGeography() {
		return childGeography;
	}

	public void setChildGeography(CustomGeography childGeography) {
		this.childGeography = childGeography;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Double getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(Double startLatitude) {
		this.startLatitude = startLatitude;
	}

	public Double getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(Double startLongitude) {
		this.startLongitude = startLongitude;
	}

	public Double getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(Double endLatitude) {
		this.endLatitude = endLatitude;
	}

	public Double getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(Double endLongitude) {
		this.endLongitude = endLongitude;
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	public String getRoutePoints() {
		return routePoints;
	}

	public void setRoutePoints(String routePoints) {
		this.routePoints = routePoints;
	}

	/**
	 * @return the boundaryPoints
	 */
	public String getBoundaryPoints() {
		return boundaryPoints;
	}

	/**
	 * @param boundaryPoints the boundaryPoints to set
	 */
	public void setBoundaryPoints(String boundaryPoints) {
		this.boundaryPoints = boundaryPoints;
	}

	@Override
	public String toString() {
		return "CustomGeographyWrapper [id=" + id + ", childGeography=" + childGeography + ", creationTime="
				+ creationTime + ", modifiedTime=" + modifiedTime + ", name=" + name + ", type=" + type + ", routeType="
				+ routeType + ", landmark=" + landmark + ", startLatitude=" + startLatitude + ", startLongitude="
				+ startLongitude + ", endLatitude=" + endLatitude + ", endLongitude=" + endLongitude + ", boundary="
				+ boundary + ", routePoints=" + routePoints + ", boundaryPoints=" + boundaryPoints + "]";
	}

	
}
