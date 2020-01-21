package com.inn.foresight.core.infra.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;

/** The Class BuildingPolygonWrapper. */
@JpaWrapper
public class BuildingPolygonWrapper {

	private Integer projectId;

	private String uuid;

	/** The building id. */
	private Integer id;

	/** The building building id. */
	private String buildingId;

	/** The building polygon. */
	private String buildingPolygon;

	private List<List<List<Double>>> polygon;

	private String status;

	private String buildingName;
	
	private String buildingPriority;
	
	private Double latitude;
	
	private Double longitude;

	public BuildingPolygonWrapper(Integer id, String buildingId, String buildingPolygon, String status,
			String buildingName, String buildingPriority, Double latitude, Double longitude) {
		super();
		this.id = id;
		this.buildingId = buildingId;
		this.buildingPolygon = buildingPolygon;
		this.status = status;
		this.buildingName = buildingName;
		this.buildingPriority = buildingPriority;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public BuildingPolygonWrapper(Integer projectId, String uuid, Integer id, String buildingId, String buildingPolygon,
			String status, String buildingName, String buildingPriority, Double latitude, Double longitude) {
		super();
		this.projectId = projectId;
		this.uuid = uuid;
		this.id = id;
		this.buildingId = buildingId;
		this.buildingPolygon = buildingPolygon;
		this.status = status;
		this.buildingName = buildingName;
		this.buildingPriority = buildingPriority;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Gets the building id.
	 *
	 * @return the buildingId
	 */
	public String getBuildingId() {
		return buildingId;
	}

	/**
	 * Gets the building polygon.
	 *
	 * @return the building polygon
	 */
	public String getBuildingPolygon() {
		return buildingPolygon;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Sets the building id.
	 *
	 * @param buildingId the buildingId to set
	 */
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	/**
	 * Sets the building polygon.
	 *
	 * @param buildingPolygon the buildingPolygon to set
	 */
	public void setBuildingPolygon(String buildingPolygon) {
		this.buildingPolygon = buildingPolygon;
	}

	public List<List<List<Double>>> getPolygon() {
		return polygon;
	}

	public void setPolygon(List<List<List<Double>>> polygon) {
		this.polygon = polygon;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getBuildingPriority() {
		return buildingPriority;
	}

	public void setBuildingPriority(String buildingPriority) {
		this.buildingPriority = buildingPriority;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "BuildingPolygonWrapper [projectId=" + projectId + ", uuid=" + uuid + ", id=" + id + ", buildingId="
				+ buildingId + ", buildingPolygon=" + polygon + ", status=" + status + ", buildingName=" + buildingName
				+ ", buildingPriority=" + buildingPriority + ", latitude=" + latitude + ", longitude=" + longitude
				+ "]";
	}

}
