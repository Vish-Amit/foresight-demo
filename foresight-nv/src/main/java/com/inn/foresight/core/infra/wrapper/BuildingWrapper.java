package com.inn.foresight.core.infra.wrapper;

import java.util.Date;
import java.util.List;

import com.inn.commons.maps.LatLng;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.core.infra.constants.InBuildingConstants.BuildingType;
import com.inn.product.um.user.model.User;

/** The Class BuildingWrapper. */
@JpaWrapper
@RestWrapper
public class BuildingWrapper extends LatLng {

	/** The building id. */
	private Integer buildingId;

	/** The building name. */
	private String buildingName;

	/** The address. */
	private String address;

	/** The building type. */
	private BuildingType buildingType;

	/** The creation time. */
	private Long creationTime;

	private Long modificationTime;

	/** The user id. */
	private Integer userId;

	/** The wing list. */
	private List<WingWrapper> wingList;
	private Integer geographyId;
	private String geographyName;

	private String buildingIdentifier;
	
	private String buildingPriority;
	
	private Double height;

	private Double area;
	
	private Integer floorNumber;
	
	private String coverage;
	
	private String buildingTypeName;

	/**
	 * Instantiates a new Building wrapper.
	 *
	 * @param buildingId
	 *            the building Id
	 * @param buildingName
	 *            the building Name
	 * @param address
	 *            the address
	 * @param buildingType
	 *            the building Type
	 * @param creationTime
	 *            the creation Time
	 * @param userId
	 *            the user Id
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 */


	public BuildingWrapper(Integer buildingId, String buildingName, String address, BuildingType buildingType,
			Date creationTime, User user, Double latitude, Double longitude, Date modificationTime) {
		super();
		this.buildingId = buildingId;
		this.buildingName = buildingName;
		this.address = address;
		this.buildingType = buildingType;
		this.creationTime = creationTime.getTime();
		if(user!=null) {
		this.userId = user.getUserid();
		}
		this.latitude = latitude;
		this.longitude = longitude;
		if (modificationTime != null) {
			this.modificationTime = modificationTime.getTime();
		}
	}

	public BuildingWrapper(BuildingType buildingType, String buildingPriority, Double height, Double area,String address,String buildingName) {
		super();
		this.buildingPriority = buildingPriority;
		this.height = height;
		this.area = area;
		this.address = address;
		this.buildingName = buildingName;
		this.buildingTypeName = buildingType.displayName();
	}
	
	public BuildingWrapper(String coverage) {
		this.coverage = coverage;
	}

	public BuildingWrapper(Integer buildingId, String buildingName, String address, BuildingType buildingType,
			Date creationTime, User user, Double latitude, Double longitude, Date modificationTime,Integer geographyId,String geographyName) {
		super();
		this.buildingId = buildingId;
		this.buildingName = buildingName;
		this.address = address;
		this.buildingType = buildingType;
		this.creationTime = creationTime.getTime();
		if(user!=null) {
		this.userId = user.getUserid();
		}
		this.latitude = latitude;
		this.longitude = longitude;
		if (modificationTime != null) {
			this.modificationTime = modificationTime.getTime();
		}
		this.geographyId=geographyId;
		this.geographyName=geographyName;
	}
	/** Instantiates a new Building wrapper. */
	public BuildingWrapper() {
	}

	/**
	 * Gets the building id.
	 *
	 * @return the buildingId
	 */
	public Integer getBuildingId() {
		return buildingId;
	}

	/**
	 * Sets the building id.
	 *
	 * @param buildingId
	 *            the buildingId to set
	 */
	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
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
	 * @param buildingName
	 *            the buildingName to set
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
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * @param buildingType
	 *            the buildingType to set
	 */
	public void setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creationTime
	 */
	public Long getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime
	 *            the creationTime to set
	 */
	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * Gets the wing list.
	 *
	 * @return the wingList
	 */
	public List<WingWrapper> getWingList() {
		return wingList;
	}

	/**
	 * Sets the wing list.
	 *
	 * @param wingList
	 *            the wingList to set
	 */
	public void setWingList(List<WingWrapper> wingList) {
		this.wingList = wingList;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude
	 *            the latitude to set
	 * 
	 * @return LatLng for current instance
	 */
	@Override
	public LatLng setLatitude(Double latitude) {
		this.latitude = latitude;
		return null;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	@Override
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude
	 *            the longitude to set
	 * 
	 * @return LatLng for current instance
	 */
	@Override
	public LatLng setLongitude(Double longitude) {
		this.longitude = longitude;
		return null;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	@Override
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @return the modificationTime
	 */
	public Long getModificationTime() {
		return modificationTime;
	}

	/**
	 * @param modificationTime
	 *            the modificationTime to set
	 */
	public void setModificationTime(Long modificationTime) {
		this.modificationTime = modificationTime;
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

	/**
	 * @return the geographyName
	 */
	public String getGeographyName() {
		return geographyName;
	}

	/**
	 * @param geographyName the geographyName to set
	 */
	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
	}
	public String getBuildingIdentifier() {
		return buildingIdentifier;
	}

	public void setBuildingIdentifier(String buildingIdentifier) {
		this.buildingIdentifier = buildingIdentifier;
	}
	public String getBuildingPriority() {
		return buildingPriority;
	}

	public void setBuildingPriority(String buildingPriority) {
		this.buildingPriority = buildingPriority;
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
	
	public Integer getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(Integer floorNumber) {
		this.floorNumber = floorNumber;
	}
	

	  public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	
    public String getBuildingTypeName() {
        return buildingTypeName;
    }

    
    public void setBuildingTypeName(String buildingTypeName) {
        this.buildingTypeName = buildingTypeName;
    }


  
	@Override
	public String toString() {
		return "BuildingWrapper [buildingId=" + buildingId + ", buildingName=" + buildingName + ", address=" + address
				+ ", buildingType=" + buildingType + ", creationTime=" + creationTime + ", modificationTime="
				+ modificationTime + ", userId=" + userId + ", wingList=" + wingList + ", geographyId=" + geographyId
				+ ", geographyName=" + geographyName + ", buildingIdentifier=" + buildingIdentifier
				+ ", buildingPriority=" + buildingPriority + ", height=" + height + ", area=" + area + ", floorNumber="
				+ floorNumber + ", coverage=" + coverage + ", buildingTypeName=" + buildingTypeName +"]";
	}
}
