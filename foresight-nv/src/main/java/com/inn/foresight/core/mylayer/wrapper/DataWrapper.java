package com.inn.foresight.core.mylayer.wrapper;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;

/**
 * The Class DataWrapper.
 */
@JpaWrapper
public class DataWrapper {

	/** The pin name. */
	private String pinName;
	
	/** The pin latitude. */
	private Double pinLatitude;
	
	/** The pin longitude. */
	private Double pinLongitude;
	
	/** The comments. */
	private String comments;
	
	/** The cluster ID. */
	private String geographyL4;
	
	/** The city. */
	private String geographyL3;
	
	/** The circle. */
	private String geographyL2;
	
	/** The r 4 g state. */
	private String geographyL1;
	
	/** The pin status. */
	private String pinStatus;
	
	/** The color code. */
	private String colorCode;
	
	/** The image name. */
	private String imageName;
	
	/** The group names. */
	private List<String> groupNames;
	
	/** The group name. */
	private String groupName;
	
	/** The date. */
	private Date date;
	
	/** The additional info. */
	private String additionalInfo;
	
	/** The opacity. */
	private Double opacity;
	
	private String sharedBy;

	private Date sharedDate;
	
	private Date createdTime;
	
	private Date modifiedTime;
	
	private Integer id;
	
	private Integer zoomLevel;
	
	private String oldPinStatus;
	
	private String newPinStatus;
	
	private String oldGroupName;
	
	private String newGroupName;
	
	private String newComments;
	
	private String oldComments;
	
	private String labelProperty;
	
    private Double latitude;
	
	private Double longitude;
	
	private String status;
	/**
	 * Instantiates a new data wrapper.
	 */
	public DataWrapper() {
		//empty
	}
	public DataWrapper(String pinStatus,String colorCode,String imageName)
	{
		this.pinStatus=pinStatus;
		this.colorCode=colorCode;
		this.imageName=imageName;
	}
	public DataWrapper(Integer id,String pinName,Double pinLatitude,Double pinLongitude,String comments,String groupName,Date createdTime,Date modifiedTime, String pinStatus,String additionalInfo,Integer zoomLevel,String colorCode,String labelProperty)
	{
		this.id=id;
		this.pinName=pinName;
		this.pinLatitude=pinLatitude;
		this.pinLongitude=pinLongitude;
		this.comments=comments;
		this.groupName=groupName;
		this.createdTime=createdTime;
		this.modifiedTime=modifiedTime;
		this.pinStatus=pinStatus;
		this.additionalInfo=additionalInfo;
		this.zoomLevel=zoomLevel;
		this.colorCode=colorCode;
		this.labelProperty=labelProperty;
	}
	public DataWrapper(Double pinLatitude,Double pinLongitude,String comments,String groupName,Date createdTime,Date modifiedTime, String pinStatus,String additionalInfo,Integer zoomLevel,String colorCode,String labelProperty)
	{
		this.pinLatitude=pinLatitude;
		this.pinLongitude=pinLongitude;
		this.comments=comments;
		this.groupName=groupName;
		this.createdTime=createdTime;
		this.modifiedTime=modifiedTime;
		this.pinStatus=pinStatus;
		this.additionalInfo=additionalInfo;
		this.zoomLevel=zoomLevel;
		this.colorCode=colorCode;
		this.labelProperty=labelProperty;
	}
	
	
	public DataWrapper(String groupName,Double opacity)
	{
		this.groupName=groupName;
	}
	
	public DataWrapper(String groupName,String pinStatus,String imageName,String colorCode,Double opacity,Date createdTime,Date modifiedTime,String sharedBy,Date sharedDate,String labelProperty)
	{
		this.groupName=groupName;
		this.pinStatus=pinStatus;
		this.imageName=imageName;
		this.colorCode=colorCode;
		this.opacity=opacity;
		this.createdTime=createdTime;
		this.modifiedTime=modifiedTime;
		this.sharedBy=sharedBy;
		this.sharedDate=sharedDate;
		this.labelProperty=labelProperty;
	}
	public DataWrapper(Integer id,String pinName,Double pinLatitude,Double pinLongitude,String comments,String groupName,Date createdTime,Date modifiedTime, String pinStatus,String additionalInfo,Integer zoomLevel,String colorCode,String imageName,String sharedBy,Date sharedDate,String labelProperty)
	{
		this.id=id;
		this.pinName=pinName;
		this.pinLatitude=pinLatitude;
		this.pinLongitude=pinLongitude;
		this.comments=comments;
		this.groupName=groupName;
		this.createdTime=createdTime;
		this.modifiedTime=modifiedTime;
		this.pinStatus=pinStatus;
		this.additionalInfo=additionalInfo;
		this.zoomLevel=zoomLevel;
		this.colorCode=colorCode;
		this.imageName=imageName;
		this.sharedBy=sharedBy;
		this.sharedDate=sharedDate;
		this.labelProperty=labelProperty;
	}
	
	/**
	 * Gets the pin name.
	 *
	 * @return the pin name
	 */
	public String getPinName() {
		return pinName;
	}

	/**
	 * Sets the pin name.
	 *
	 * @param pinName the new pin name
	 */
	public void setPinName(String pinName) {
		this.pinName = pinName;
	}

	/**
	 * Gets the pin latitude.
	 *
	 * @return the pin latitude
	 */
	public Double getPinLatitude() {
		return pinLatitude;
	}

	/**
	 * Sets the pin latitude.
	 *
	 * @param pinLatitude the new pin latitude
	 */
	public void setPinLatitude(Double pinLatitude) {
		this.pinLatitude = pinLatitude;
	}

	/**
	 * Gets the pin longitude.
	 *
	 * @return the pin longitude
	 */
	public Double getPinLongitude() {
		return pinLongitude;
	}

	/**
	 * Sets the pin longitude.
	 *
	 * @param pinLongitude the new pin longitude
	 */
	public void setPinLongitude(Double pinLongitude) {
		this.pinLongitude = pinLongitude;
	}

	/**
	 * Gets the comments.
	 *
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Sets the comments.
	 *
	 * @param comments the new comments
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Gets the cluster ID.
	 *
	 * @return the cluster ID
	 */
	public String getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the cluster ID.
	 *
	 * @param geographyL4 the new cluster ID
	 */
	public void setGeographyL4(String geographyL4) {
		this.geographyL4 = geographyL4;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getGeographyL3() {
		return geographyL3;
	}

	/**
	 * Sets the city.
	 *
	 * @param geographyL3 the new city
	 */
	public void setGeographyL3(String geographyL3) {
		this.geographyL3 = geographyL3;
	}

	/**
	 * Gets the circle.
	 *
	 * @return the circle
	 */
	public String getGeographyL2() {
		return geographyL2;
	}

	/**
	 * Sets the circle.
	 *
	 * @param geographyL2 the new circle
	 */
	public void setGeographyL2(String geographyL2) {
		this.geographyL2 = geographyL2;
	}

	/**
	 * Gets the r 4 g state.
	 *
	 * @return the r 4 g state
	 */
	public String getGeographyL1() {
		return geographyL1;
	}

	/**
	 * Sets the r 4 g state.
	 *
	 * @param geographyL1 the new r 4 g state
	 */
	public void setGeographyL1(String geographyL1) {
		this.geographyL1 = geographyL1;
	}

	/**
	 * Gets the pin status.
	 *
	 * @return the pin status
	 */
	public String getPinStatus()
	{
		return pinStatus;
	}

	/**
	 * Sets the pin status.
	 *
	 * @param pinStatus the new pin status
	 */
	public void setPinStatus(String pinStatus)
	{
		this.pinStatus = pinStatus;
	}

	/**
	 * Gets the color code.
	 *
	 * @return the color code
	 */
	public String getColorCode()
	{
		return colorCode;
	}

	/**
	 * Sets the color code.
	 *
	 * @param colorCode the new color code
	 */
	public void setColorCode(String colorCode)
	{
		this.colorCode = colorCode;
	}

	/**
	 * Gets the image name.
	 *
	 * @return the image name
	 */
	public String getImageName()
	{
		return imageName;
	}

	/**
	 * Sets the image name.
	 *
	 * @param imageName the new image name
	 */
	public void setImageName(String imageName)
	{
		this.imageName = imageName;
	}
	
	/**
	 * Gets the group names.
	 *
	 * @return the group names
	 */
	public List<String> getGroupNames()
	{
		return groupNames;
	}

	/**
	 * Sets the group names.
	 *
	 * @param groupNames the new group names
	 */
	public void setGroupNames(List<String> groupNames)
	{
		this.groupNames = groupNames;
	}
	
	/**
	 * Gets the group name.
	 *
	 * @return the group name
	 */
	public String getGroupName()
	{
		return groupName;
	}

	/**
	 * Sets the group name.
	 *
	 * @param groupName the new group name
	 */
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	
	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}

	
	/**
	 * Gets the additional info.
	 *
	 * @return the additional info
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	/**
	 * Sets the additional info.
	 *
	 * @param additionalInfo the new additional info
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	/**
	 * Gets the opacity.
	 *
	 * @return the opacity
	 */
	public Double getOpacity()
	{
		return opacity;
	}

	/**
	 * Sets the opacity.
	 *
	 * @param opacity the new opacity
	 */
	public void setOpacity(Double opacity)
	{
		this.opacity = opacity;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	

	public String getSharedBy() {
		return sharedBy;
	}
	public void setSharedBy(String sharedBy) {
		this.sharedBy = sharedBy;
	}
	
	public Date getSharedDate() {
		return sharedDate;
	}

	public void setSharedDate(Date sharedDate) {
		this.sharedDate = sharedDate;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getZoomLevel() {
		return zoomLevel;
	}
	public void setZoomLevel(Integer zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
	public String getOldPinStatus() {
		return oldPinStatus;
	}
	public void setOldPinStatus(String oldPinStatus) {
		this.oldPinStatus = oldPinStatus;
	}
	public String getNewPinStatus() {
		return newPinStatus;
	}
	public void setNewPinStatus(String newPinStatus) {
		this.newPinStatus = newPinStatus;
	}
	public String getOldGroupName() {
		return oldGroupName;
	}
	public void setOldGroupName(String oldGroupName) {
		this.oldGroupName = oldGroupName;
	}
	public String getNewGroupName() {
		return newGroupName;
	}
	public void setNewGroupName(String newGroupName) {
		this.newGroupName = newGroupName;
	}
	public String getNewComments() {
		return newComments;
	}
	public void setNewComments(String newComments) {
		this.newComments = newComments;
	}
	public String getOldComments() {
		return oldComments;
	}
	public void setOldComments(String oldComments) {
		this.oldComments = oldComments;
	}
	
	public String getLabelProperty() {
		return labelProperty;
	}
	public void setLabelProperty(String labelProperty) {
		this.labelProperty = labelProperty;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "DataWrapper [pinName=" + pinName + ", pinLatitude=" + pinLatitude + ", pinLongitude=" + pinLongitude
				+ ", comments=" + comments + ", geographyL4=" + geographyL4 + ", geographyL3=" + geographyL3
				+ ", geographyL2=" + geographyL2 + ", geographyL1=" + geographyL1 + ", pinStatus=" + pinStatus
				+ ", colorCode=" + colorCode + ", imageName=" + imageName + ", groupNames=" + groupNames
				+ ", groupName=" + groupName + ", date=" + date + ", additionalInfo=" + additionalInfo + ", opacity="
				+ opacity + ", sharedBy=" + sharedBy + ", sharedDate=" + sharedDate + ", createdTime=" + createdTime
				+ ", modifiedTime=" + modifiedTime + ", id=" + id + ", zoomLevel=" + zoomLevel + ", oldPinStatus="
				+ oldPinStatus + ", newPinStatus=" + newPinStatus + ", oldGroupName=" + oldGroupName + ", newGroupName="
				+ newGroupName + ", newComments=" + newComments + ", oldComments=" + oldComments + ", labelProperty="
				+ labelProperty + ", latitude=" + latitude + ", longitude=" + longitude + ", status=" + status + "]";
	}
	
}
