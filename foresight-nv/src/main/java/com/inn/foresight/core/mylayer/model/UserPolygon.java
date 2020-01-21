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
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;

/**
 * The Class UserPolygon.
 * 
 * @author innoeye
 */
@NamedQueries({
	@NamedQuery(name="isPolygonExist",query="select count(distinct u.name) from UserPolygon u where u.user.userid =:userId and u.name=:polygonName"),
	@NamedQuery(name="deletePolygonById",query="delete from UserPolygon u where u.id =:id and u.user.userid =:userId"),
	@NamedQuery(name="getPolygonById",query="select u from UserPolygon u where u.id =:id and u.user.userid =:userId"),
	@NamedQuery(name="isNewPolygonNameAlreadyExist",query="select count(distinct u.name) from UserPolygon u where u.user.userid =:userId and u.name=:polygonName and u.id !=:id"),
})
@Entity
@Table(name = "UserPolygon")
@XmlRootElement(name = "UserPolygon")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class UserPolygon implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8613785568424097507L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "userpolygonid_pk")
	private Integer id;

	/** The name. */
	@Basic
	@Column(name = "name")
	private String name;
	
	/** The isdeleted. */
	@Basic
	@Column(name = "deleted")
	private Boolean isdeleted;
	
	/** The comments. */
	@Basic
	@Column(name = "comments")
	private String comments;
	
	/** The polygon point. */
	@Basic
	@Column(name = "polygonpoint")
	private String polygonPoint;
	
	/** The color code. */
	@Basic
	@Column(name = "colorcode")
	private String colorCode;
	
	/** The user. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid_fk", nullable = true)
	private User user;
	
	/** The modified time. */
	@Basic
	@Column(name = "creationtime")
	private Date createdTime;

	/** The modified time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modifiedTime;

	@Basic
	@Column(name = "zoomlevel")
	private Integer zoomLevel;
	
	/** The kml upload path. */
	@Basic
	@Column(name = "kmluploadpath")
	private String kmlUploadPath;
	
	@Basic
	@Column(name = "polygonproperty")
	private String polygonProperties;
	 
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
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the isdeleted.
	 *
	 * @return the isdeleted
	 */
	public Boolean getIsdeleted() {
		return isdeleted;
	}

	/**
	 * Sets the isdeleted.
	 *
	 * @param isdeleted the new isdeleted
	 */
	public void setIsdeleted(Boolean isdeleted) {
		this.isdeleted = isdeleted;
	}

	/**
	 * Gets the polygon point.
	 *
	 * @return the polygon point
	 */
	public String getPolygonPoint()
	{
		return polygonPoint;
	}

	/**
	 * Sets the polygon point.
	 *
	 * @param polygonPoint the new polygon point
	 */
	public void setPolygonPoint(String polygonPoint)
	{
		this.polygonPoint = polygonPoint;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) {
		this.user = user;
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

	public String getKmlUploadPath() {
		return kmlUploadPath;
	}

	public void setKmlUploadPath(String kmlUploadPath) {
		this.kmlUploadPath = kmlUploadPath;
	}

	public String getPolygonProperties() {
		return polygonProperties;
	}

	public void setPolygonProperties(String polygonProperties) {
		this.polygonProperties = polygonProperties;
	}

	@Override
	public String toString() {
		return "UserPolygon [id=" + id + ", name=" + name + ", isdeleted=" + isdeleted + ", comments=" + comments
				+ ", polygonPoint=" + polygonPoint + ", colorCode=" + colorCode + ", user=" + user + ", createdTime="
				+ createdTime + ", modifiedTime=" + modifiedTime + ", zoomLevel=" + zoomLevel + ", kmlUploadPath="
				+ kmlUploadPath + ", polygonProperties=" + polygonProperties + "]";
	}

}
