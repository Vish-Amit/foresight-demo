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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;

@NamedQueries({ @NamedQuery(name = "getKmlData", query = "select new com.inn.foresight.core.mylayer.utils.KmlProcessorWrapper(k.id,k.colorCode,k.comment,k.createdTime,k.fileSize,k.kmlName,k.kmlPath,k.kmlType,k.latitude,k.longitude,k.modifiedTime,k.zoomLevel,k.property) from KmlProcessor k where k.userid.userid=:userId order by k.modifiedTime desc"),
	@NamedQuery(name="deleteKMLDetails",query="delete from KmlProcessor k where k.id =:id and k.userid.userid =:userId"),
	@NamedQuery(name="getKmlDataById",query="select k from KmlProcessor k where k.id =:id "),
	@NamedQuery(name="getKMLById",query="select k from KmlProcessor k where k.id =:id and k.userid.userid =:userId"),
	@NamedQuery(name="isKMLExist",query="select count(distinct k.kmlName) from KmlProcessor k where k.userid.userid =:userId and k.kmlName=:kmlName"),
	@NamedQuery(name="getMaxVersionOfKMLFile",query="select max(k.version) from KmlProcessor k where k.generatedKmlName =:kmlName and k.userid.userid =:userid"),
	@NamedQuery(name="getAllKMLNames",query="select distinct k.kmlName from KmlProcessor k where k.userid.userid =:userId  and k.id !=(:id) "),
	
})

@Entity
@Table(name = "KMLProcessor")
@XmlRootElement(name = "KMLProcessor")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class KmlProcessor implements Serializable {

	private static final long serialVersionUID = 3755935165060399277L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "kmlprocessorid_pk")
	private Integer id;

	/** The userid. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid_fk", nullable = false)
	private User userid;

	/** The kml type. */
	@Basic
	@Column(name = "type")
	private String kmlType;

	/** The generated kml name. */
	@Basic
	@Column(name = "generatedkmlname")
	private String generatedKmlName;

	/** The kml path. */
	@Basic
	@Column(name = "path")
	private String kmlPath;

	/** The created time. */
	@Basic
	@Column(name = "creationtime")
	private Date createdTime;
	
	/** The color code. */
	@Basic
	@Column(name = "colorcode")
	private String colorCode;
	
	/** The kml name. */
	@Basic
	@Column(name = "name")
	private String kmlName;
	
	/* The modified time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modifiedTime;
	
	/** The latitude. */
	@Basic 
	@Column(name = "latitude")
	private Double latitude;
	
	/** The longitude. */
	@Basic 
	@Column(name = "longitude")
	private Double longitude;
	
	/** The zoom level. */
	@Basic
	@Column(name = "zoomlevel")
	private Integer zoomLevel;
	
	@Basic
	@Column(name = "filesize")
	private Long fileSize;

	@Basic
	@Column(name = "filecontent")
	private String fileContent;
	
	@Basic
	@Column(name = "comment")
	private String comment;
	
	@Basic
	@Column(name = "version")
	private Integer version;
	
	@Basic
	@Column(name = "property")
	private String property;
	
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
	 * Gets the userid.
	 *
	 * @return the userid
	 */
	@JsonIgnore
	public User getUserid() {
		return userid;
	}

	/**
	 * Sets the userid.
	 *
	 * @param userid the new userid
	 */
	public void setUserid(User userid) {
		this.userid = userid;
	}

	/**
	 * Gets the kml type.
	 *
	 * @return the kml type
	 */
	public String getKmlType() {
		return kmlType;
	}

	/**
	 * Sets the kml type.
	 *
	 * @param kmlType the new kml type
	 */
	public void setKmlType(String kmlType) {
		this.kmlType = kmlType;
	}

	/**
	 * Gets the kml name.
	 *
	 * @return the kml name
	 */
	public String getKmlName() {
		return kmlName;
	}

	/**
	 * Sets the kml name.
	 *
	 * @param kmlName the new kml name
	 */
	public void setKmlName(String kmlName) {
		this.kmlName = kmlName;
	}

	/**
	 * Gets the kml path.
	 *
	 * @return the kml path
	 */
	public String getKmlPath() {
		return kmlPath;
	}

	/**
	 * Sets the kml path.
	 *
	 * @param kmlPath the new kml path
	 */
	public void setKmlPath(String kmlPath) {
		this.kmlPath = kmlPath;
	}

	/**
	 * Gets the created time.
	 *
	 * @return the created time
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * Sets the created time.
	 *
	 * @param createdTime the new created time
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
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
	 * Gets the generated kml name.
	 *
	 * @return the generated kml name
	 */
	public String getGeneratedKmlName()
	{
		return generatedKmlName;
	}

	/**
	 * Sets the generated kml name.
	 *
	 * @param generatedKmlName the new generated kml name
	 */
	public void setGeneratedKmlName(String generatedKmlName)
	{
		this.generatedKmlName = generatedKmlName;
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

	public Integer getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(Integer zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@Override
	public String toString() {
		return "KmlProcessor [id=" + id + ", userid=" + userid + ", kmlType=" + kmlType + ", generatedKmlName="
				+ generatedKmlName + ", kmlPath=" + kmlPath + ", createdTime=" + createdTime + ", colorCode="
				+ colorCode + ", kmlName=" + kmlName + ", modifiedTime=" + modifiedTime + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", zoomLevel=" + zoomLevel + ", fileSize=" + fileSize + ", fileContent="
				+ fileContent + ", comment=" + comment + ", version=" + version + ", property=" + property + "]";
	}

}
