package com.inn.foresight.core.infra.model;

import java.awt.Polygon;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.constants.InBuildingConstants.BuildingType;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.model.User;

/** The Class PushNotification. */

@NamedQueries({
		@NamedQuery(name = InBuildingConstants.GET_BUILDING_BY_ID, query = "select new com.inn.foresight.core.infra.wrapper.BuildingWrapper (b.id,b.buildingName,b.address,b.buildingType,b.creationTime,b.createdBy,b.latitude,b.longitude,b.modificationTime,b.geographyL4.id,b.geographyL4.name) from Building b left join b.createdBy  where b.id=:id"),
		@NamedQuery(name = InBuildingConstants.GET_BUILDINGS_BY_CLUSTER, query = "select new com.inn.foresight.core.infra.wrapper.BuildingWrapper (b.id,b.buildingName,b.address,b.buildingType,b.creationTime,b.createdBy,b.latitude,b.longitude,b.modificationTime) from Building b left join b.createdBy where b.geographyL4=:geographyL4PlaceHolder"),
		@NamedQuery(name = InBuildingConstants.GET_BUILDINGS_BY_NAME, query = "select new com.inn.foresight.core.infra.wrapper.BuildingWrapper (b.id,b.buildingName,b.address,b.buildingType,b.creationTime,b.createdBy,b.latitude,b.longitude,b.modificationTime) from Building b left join b.createdBy where b.isDeleted=false and b.buildingName like CONCAT('%',:name,'%')"),
		@NamedQuery(name = InBuildingConstants.GET_ALL_BUILDINGS, query = "select new com.inn.foresight.core.infra.wrapper.BuildingWrapper (b.id,b.buildingName,b.address,b.buildingType,b.creationTime,b.createdBy,b.latitude,b.longitude,b.modificationTime) from Building b left join b.createdBy where b.isDeleted=false"),
		@NamedQuery(name = InBuildingConstants.GET_BUILDING_BY_NAME_AND_ADDRESS, query = "select b from Building b where b.buildingName=:buildingName and b.address=:address"),
		@NamedQuery(name = "getBuildingsToTagGeoL4", query = "select b from Building b where b.geographyL4 is null and b.latitude is not null and b.longitude is not null"),
		@NamedQuery(name = InBuildingConstants.GET_BUILDING_BY_NAME_AND_LATLONG, query = "select b from Building b where b.buildingName=:buildingName and b.latitude=:latitude and b.longitude=:longitude"),
		@NamedQuery(name = InBuildingConstants.GET_BUILDING_BY_BUILDING_ID, query = "select b from Building b where b.buildingId=:buildingId"),
		@NamedQuery(name = "getBuildingPolygonByViewPort", query = "SELECT new com.inn.foresight.core.infra.wrapper.BuildingPolygonWrapper(b.id,b.buildingId,CASE WHEN (b.buildingPolygon) IS NOT NULL THEN CAST(AsText(b.buildingPolygon)as string) ELSE b.buildingPolygon END,b.status,b.buildingName,b.buildingPriority,b.latitude,b.longitude) FROM Building b WHERE b.longitude > :swLng AND b.longitude < :neLng AND b.latitude > :swLat AND b.latitude < :neLat AND b.isDeleted=false"),
	    @NamedQuery(name = "getGeographyDetailByBuildingId", query = "select new com.inn.foresight.core.infra.wrapper.GeographyDetailWrapper(geographyL4.geographyL3.geographyL2.geographyL1.name,geographyL4.geographyL3.geographyL2.name,geographyL4.geographyL3.name,geographyL4.name) from Building b where b.buildingId=:buildingId and b.isDeleted=false"),
	    @NamedQuery(name = "getGeographyL4Id", query = "select b.id from GeographyL4 b where lower(b.name)=lower(:name)"),
	    @NamedQuery(name = "getBuildingDetailByBuildingId", query = "SELECT new com.inn.foresight.core.infra.wrapper.BuildingWrapper(b.buildingType,b.buildingPriority,b.height,b.area,b.address,b.buildingName) FROM Building b WHERE b.buildingId=:buildingId and b.isDeleted=false"),
	    @NamedQuery(name = "getBuildingCoverage", query = "SELECT new com.inn.foresight.core.infra.wrapper.BuildingWrapper(b.coverage) FROM Building b WHERE b.buildingId=:buildingId and b.isDeleted=false")
		
})

@Entity
@XmlRootElement(name = "Building")
@Table(name = "Building")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Building implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "buildingid_pk")
	private Integer id;

	/** The building name. */
	@Column(name = "name")
	private String buildingName;

	/** The address. */
	@Column(name = "address")
	private String address;

	/** The latitude. */
	@Column(name = "latitude")
	private Double latitude;

	/** The longitude. */
	@Column(name = "longitude")
	private Double longitude;

	/** The building type. */
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private BuildingType buildingType;

	/** The is deleted. */
	@Column(name = "deleted")
	private boolean isDeleted;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The creation time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The created by. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk", nullable = true)
	private User createdBy;

	/** The cluster. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl4id_fk", nullable = true)
	private GeographyL4 geographyL4;

	/** The is buildingId. */
	@Column(name = "buildingid")
	private String buildingId;

	/** The BuildingPolygon. */
	@Column(name = "buildingpolygon")
	private Polygon buildingPolygon;

	/** The status. */
	@Column(name = "status")
	private String status;

	/** The Height. */
	@Column(name = "height")
	private Double height;

	/** The Area. */
	@Column(name = "area")
	private Double area;

	/** The BuildingPriority. */
	@Column(name = "buildingpriority")
	private String buildingPriority;

	/** The clutter. */
	@Column(name = "clutter")
	private String clutter;
	
	@Column(name = "coverage")
	@Lob
	private String coverage;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
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
	 * Gets the created by.
	 *
	 * @return the createdBy
	 */
	public User getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the created by.
	 *
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets the building name.
	 *
	 * @return the buildingName
	 */
	public String getBuildingName() {
		return buildingName;
	}

	/**
	 * Sets the building name.
	 *
	 * @param buildingName the buildingName to set
	 */
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * @param latitude the latitude to set
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
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the building type.
	 *
	 * @return the buildingType
	 */
	public BuildingType getBuildingType() {
		return buildingType;
	}

	/**
	 * Sets the building type.
	 *
	 * @param buildingType the buildingType to set
	 */
	public void setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the checks if is deleted.
	 *
	 * @return the isDeleted
	 */
	public boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * Sets the checks if is deleted.
	 *
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * Gets the geography L 4.
	 *
	 * @return the geographyL4
	 */
	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4 the geographyL4 to set
	 */
	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}

	/**
	 * @return the modificationTime
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * @param modificationTime the modificationTime to set
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String isBuildingId() {
		return buildingId;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public Polygon getBuildingPolygon() {
		return buildingPolygon;
	}

	public void setBuildingPolygon(Polygon buildingPolygon) {
		this.buildingPolygon = buildingPolygon;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public String getBuildingPriority() {
		return buildingPriority;
	}

	public void setBuildingPriority(String buildingPriority) {
		this.buildingPriority = buildingPriority;
	}

	public String getClutter() {
		return clutter;
	}

	public void setClutter(String clutter) {
		this.clutter = clutter;
	}
	
    public String getCoverage() {
        return coverage;
    }

    
    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Building [id=");
        builder.append(id);
        builder.append(", buildingName=");
        builder.append(buildingName);
        builder.append(", address=");
        builder.append(address);
        builder.append(", latitude=");
        builder.append(latitude);
        builder.append(", longitude=");
        builder.append(longitude);
        builder.append(", buildingType=");
        builder.append(buildingType);
        builder.append(", isDeleted=");
        builder.append(isDeleted);
        builder.append(", creationTime=");
        builder.append(creationTime);
        builder.append(", modificationTime=");
        builder.append(modificationTime);
        builder.append(", createdBy=");
        builder.append(createdBy);
        builder.append(", geographyL4=");
        builder.append(geographyL4);
        builder.append(", buildingId=");
        builder.append(buildingId);
        builder.append(", buildingPolygon=");
        builder.append(buildingPolygon);
        builder.append(", status=");
        builder.append(status);
        builder.append(", height=");
        builder.append(height);
        builder.append(", area=");
        builder.append(area);
        builder.append(", buildingPriority=");
        builder.append(buildingPriority);
        builder.append(", clutter=");
        builder.append(clutter);
        builder.append(", coverage=");
        builder.append(coverage);
        builder.append("]");
        return builder.toString();
    }
}
