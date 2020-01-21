package com.inn.foresight.core.mylayer.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.model.User;


/**
 *  This class is used for MyLayer for SitePlanning Activity functionality.
 *
 * @author innoeye
 */
@NamedQueries({
	@NamedQuery(name="getGroupNamesByUserId",query="select distinct p.groupName from PushPin p where p.user.userid =:userId and p.groupName is not null"),
	@NamedQuery(name="deleteGroupName",query="delete from PushPin p where upper(p.groupName) =:groupName and p.user.userid =:userId"),
	@NamedQuery(name="deletePinFromGroup",query="delete from PushPin p where p.id =:id and p.user.userid =:userId"),
	@NamedQuery(name="getPinNamesForGroupNames",query="select p.pinName from PushPin p where p.user.userid=:userId and upper(p.groupName) in (:groupNames) order by lower(p.pinName)"),
	@NamedQuery(name="deletePinsByPinStatus",query="delete from PushPin p where p.user.userid =:userId and upper(p.groupName) =:groupName and upper(p.pinStatus) =:pinStatus"),
	@NamedQuery(name="getGroupNameByUserId",query="select p.groupName from PushPin p where p.user.userid =:userid group by p.groupName order by  max(p.modifiedTime) desc"),
	@NamedQuery(name="getPushPinById",query="select p from PushPin p where p.id =:id"),
	@NamedQuery(name="getPinGroupData",query="select p from PushPin p where p.groupName =:groupName and p.user.userid =:userId"),
	@NamedQuery(name="searchPinDetails",query="select p from PushPin p where p.user.userid =:userId and p.pinName =:pinName and p.groupName in(:groupName)"),
	@NamedQuery(name="getPinStatusList",query="select distinct p.pinStatus from PushPin p where p.user.userid =:userId and p.pinStatus is not null "),
	@NamedQuery(name="getPinGroupNameList",query="select distinct p.groupName from PushPin p where p.user.userid =:userId and p.groupName is not null order by p.modifiedTime desc"),
	@NamedQuery(name="isPinExistInGroup",query="select count(distinct p.pinName) from PushPin p where p.pinName =:pinName and p.groupName =:groupName and p.user.userid =:userid"),
	@NamedQuery(name="getPinNameByGroupName",query="select distinct p.pinName from PushPin p where p.user.userid =:userId and p.groupName in(:groupName)"),
	@NamedQuery(name="getPinDetailsForGroupName",query="select p from PushPin p where p.user.userid =:userid and p.groupName =:groupName and p.id=:id "),
	@NamedQuery(name="isPinExistForGroupName",query="select count(distinct p.pinName) from PushPin p where p.user.userid =:userid and p.groupName =:groupName and p.pinName =:pinName and p.id !=:id"),
	@NamedQuery(name="isGroupExistForUser",query="select count(distinct p.groupName) from PushPin p where p.user.userid =:userid and p.groupName =:groupName "),
	@NamedQuery(name="updatePinDetailsByGroupName",query="update PushPin p set p.pinStatus =:newPinStatus,p.opacity =:opacity ,p.colorCode =:colorCode  where p.groupName =:newGroupName and p.user.userid =:userid and p.pinStatus =:oldPinStatus"),
	@NamedQuery(name="getPinStatusByGroupName",query="select new com.inn.foresight.core.mylayer.wrapper.DataWrapper(p.pinStatus,p.colorCode,p.imageName) from PushPin p where p.user.userid =:userId and p.groupName =:groupName and p.pinStatus is not null group by p.pinStatus,p.colorCode,p.imageName"),
	@NamedQuery(name="getNumberOfPinsInGroup",query="select count(p.id) from PushPin p where p.user.userid =:userid and p.groupName =:groupName"),
	@NamedQuery(name="getPinData",query="select p from PushPin p where p.user.userid =:userId and p.pinName =:pinName and p.groupName =:groupName"),
	@NamedQuery(name="getColorCodeAndImageNameByPinStatus",query="select new com.inn.foresight.core.mylayer.wrapper.DataWrapper(p.pinStatus,p.colorCode,p.imageName) from PushPin p where p.groupName =:groupName and p.user.userid =:userId and p.pinStatus is not null and p.colorCode is not null and p.imageName is not null  group by p.pinStatus,p.colorCode,p.imageName "),
	@NamedQuery(name="getNumberOfPinStatusInGroup",query="select count(distinct p.pinStatus) from PushPin p where p.user.userid =:userid and p.groupName =:groupName"),
	@NamedQuery(name="getPinStatusByGroupNameAndUserid",query="select distinct p.pinStatus from PushPin p where p.user.userid =:userid and p.groupName =:groupName and p.pinStatus is not null "),

})

@Entity
@Table(name = "PushPin")
@XmlRootElement(name = "PushPin")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class PushPin implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7544751248981722485L;
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "pushpinid_pk")
	private Integer id;
	
	/** The name. */
	@Basic
	@Column(name = "name")
	private String pinName;
	
	/** The latitude. */
	@Basic
	@Column(name = "latitude")
	private Double latitude;
	
	/** The longitude. */
	@Basic
	@Column(name = "longitude")
	private Double longitude;
	
	/** The comments. */
	@Basic
	@Column(name = "comments")
	private String comments;
	
	/** The group name. */
	@Basic
	@Column(name = "groupName")
	private String groupName;
	
	/** The isdeleted. */
	@Basic
	@Column(name = "deleted")
	private Boolean isdeleted;
	
	/** The color code. */
	@Basic
	@Column(name = "colorCode")
	private String colorCode;
	
	/** The opacity. */
	@Basic
	@Column(name = "opacity")
	private Double opacity;
	
	/** The created time. */
	@Basic
	@Column(name="creationtime")
	private Date createdTime;
	
	/** The modified time. */
	@Basic
	@Column(name="modificationtime")
	private Date modifiedTime;
	
	/** The user. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USERID_FK", nullable = true)	
	private User user;

	/** The pin status. */
	@Basic
	@Column(name = "pinStatus")
	private String pinStatus;
	
	/** The image name. */
	@Basic
	@Column(name = "imageName")
	private String imageName;
	
	/** The shared by. */
	@Basic
	@Column(name = "sharedBy")
	private String sharedBy;
	
	/** The additional info. */
	@Basic
	@Column(name = "additionalInfo")
	private String additionalInfo;
	
	/** The shared date. */
	@Basic
	@Column(name = "sharedDate")
	private Date sharedDate;
	
	@Basic
	@Column(name = "zoomlevel")
	private Integer zoomLevel;
	
	/** The status. */
	@Transient
	private String status;
	
    @Basic
	@Column(name = "labelProperty")
	private String labelProperty;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl4id_fk", nullable = true)
	private GeographyL4 geographyL4;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId()
	{
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id)
	{
		this.id = id;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude()
	{
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Double latitude)
	{
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude()
	{
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(Double longitude)
	{
		this.longitude = longitude;
	}

	/**
	 * Gets the comments.
	 *
	 * @return the comments
	 */
	public String getComments()
	{
		return comments;
	}

	/**
	 * Sets the comments.
	 *
	 * @param comments the new comments
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
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
	 * Gets the isdeleted.
	 *
	 * @return the isdeleted
	 */
	public Boolean getIsdeleted()
	{
		return isdeleted;
	}

	/**
	 * Sets the isdeleted.
	 *
	 * @param isdeleted the new isdeleted
	 */
	public void setIsdeleted(Boolean isdeleted)
	{
		this.isdeleted = isdeleted;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user)
	{
		this.user = user;
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
	
	/**
	 * Gets the modified time.
	 *
	 * @return the modified time
	 */
	public Date getModifiedTime()
	{
		return modifiedTime;
	}

	/**
	 * Sets the modified time.
	 *
	 * @param modifiedTime the new modified time
	 */
	public void setModifiedTime(Date modifiedTime)
	{
		this.modifiedTime = modifiedTime;
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
	 * Gets the shared by.
	 *
	 * @return the shared by
	 */
	public String getSharedBy()
	{
		return sharedBy;
	}

	/**
	 * Sets the shared by.
	 *
	 * @param sharedBy the new shared by
	 */
	public void setSharedBy(String sharedBy)
	{
		this.sharedBy = sharedBy;
	}

	/**
	 * Gets the shared date.
	 *
	 * @return the shared date
	 */
	public Date getSharedDate()
	{
		return sharedDate;
	}

	/**
	 * Sets the shared date.
	 *
	 * @param sharedDate the new shared date
	 */
	public void setSharedDate(Date sharedDate)
	{
		this.sharedDate = sharedDate;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status)
	{
		this.status = status;
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

	public String getPinName() {
		return pinName;
	}

	public void setPinName(String pinName) {
		this.pinName = pinName;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(Integer zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
   public String getLabelProperty() {
		return labelProperty;
	}

	public void setLabelProperty(String labelProperty) {
		this.labelProperty = labelProperty;
	}

	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}

	@Override
	public String toString() {
		return "PushPin [id=" + id + ", pinName=" + pinName + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", comments=" + comments + ", groupName=" + groupName + ", isdeleted=" + isdeleted + ", colorCode="
				+ colorCode + ", opacity=" + opacity + ", createdTime=" + createdTime + ", modifiedTime=" + modifiedTime
				+ ", user=" + user + ", pinStatus=" + pinStatus + ", imageName=" + imageName + ", sharedBy=" + sharedBy
				+ ", additionalInfo=" + additionalInfo + ", sharedDate=" + sharedDate + ", zoomLevel=" + zoomLevel
				+ ", status=" + status + ", labelProperty=" + labelProperty + ", geographyL4=" + geographyL4 + "]";
	}

}
