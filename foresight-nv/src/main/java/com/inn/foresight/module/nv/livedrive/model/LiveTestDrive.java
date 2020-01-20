package com.inn.foresight.module.nv.livedrive.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;

/**
 * The Class LiveTestDrive.
 *
 * @author INNOEYE
 * @version 1.0
 */
		@NamedQuery(name = "findLiveDriveByStatusAndImei", query = "select l from LiveTestDrive l where l.status in(:status) and l.imei=(:imei)")
		@NamedQuery(name = "getLiveDriveByUserAndWeek", query = "select l from LiveTestDrive l where l.isDataAvailable=1 and status in :status and l.user.userid=:userid and l.week=:week and l.startTime>=:startdate order by l.startTime desc")
		@NamedQuery(name = "getLiveDriveByUser", query = "select l from LiveTestDrive l where l.isDataAvailable=true and status in :status and l.user.userid=:userid order by l.startTime desc")
		@NamedQuery(name = "getLiveDriveById", query = "select l from LiveTestDrive l where l.driveId=:driveId") 

@Entity
@Table(name = "LiveTestDrive")
@XmlRootElement(name = "LiveTestDrive")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class LiveTestDrive implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -644069516207063649L;

	/** The Enum Status. */
	public enum Status {

		/** The running. */
		RUNNING,
		/** The paused. */
		PAUSED,
		/** The stopped. */
		STOPPED
	}

	/** The Enum DriveStoppedBy. */
	public enum DriveStoppedBy {

		/** The client. */
		CLIENT,
		/** The server. */
		SERVER

	}

	/** The drive id. */
	/* Primary Key */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "livetestdriveid_pk")
	private Integer driveId;

	/** The user. */
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid_fk")
	private User user;

	/** The imei. */
	@Column(name = "imei")
	private String imei;

	/** The status. */
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	/** The type. */
	@Column(name = "type")
	private String type;

	/** The network type. */
	@Column(name = "networktype")
	private String networkType;

	/** The operator name. */
	@Column(name = "operatorname")
	private String operatorName;

	/** The start time. */
	@Column(name = "starttime")
	private Date startTime;

	/** The last modified time. */
	@Column(name = "lastmodifiedtime")
	private Date lastModifiedTime;

	/** The is data available. */
	@Column(name = "dataavailable")
	private Boolean isDataAvailable;

	/** The week. */
	@Column(name = "week")
	private Integer week;

	/** The trav distance. */
	@Column(name = "travdistance")
	private Double travDistance;

	/** The end lat. */
	@Column(name = "endlat")
	private Double endLat;

	/** The end lng. */
	@Column(name = "endlng")
	private Double endLng;

	/** The stop time. */
	@Column(name = "stoptime")
	private Date stopTime;

	/** The stopped by. */
	@Enumerated(EnumType.STRING)
	@Column(name = "stoppedby")
	private DriveStoppedBy stoppedBy;

	/** The area. */
	@Column(name = "area")
	private String area;

	/** The area type. */
	@Column(name = "areatype")
	private String areaType;

	/** The start lat. */
	@Column(name = "startlat")
	private Double startLat;

	/** The start lng. */
	@Column(name = "startlng")
	private Double startLng;

	/**
	 * Gets the drive id.
	 *
	 * @return the drive id
	 */
	public Integer getDriveId() {
		return driveId;
	}

	/**
	 * Sets the drive id.
	 *
	 * @param driveId
	 *            the new drive id
	 */
	public void setDriveId(Integer driveId) {
		this.driveId = driveId;
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
	 * @param user
	 *            the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the imei.
	 *
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * Sets the imei.
	 *
	 * @param imei
	 *            the new imei
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the network type.
	 *
	 * @return the network type
	 */
	public String getNetworkType() {
		return networkType;
	}

	/**
	 * Sets the network type.
	 *
	 * @param networkType
	 *            the new network type
	 */
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	/**
	 * Gets the operator name.
	 *
	 * @return the operator name
	 */
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * Sets the operator name.
	 *
	 * @param operatorName
	 *            the new operator name
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime
	 *            the new start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the last modified time.
	 *
	 * @return the last modified time
	 */
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * Sets the last modified time.
	 *
	 * @param lastModifiedTime
	 *            the new last modified time
	 */
	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	/**
	 * Gets the checks if is data available.
	 *
	 * @return the checks if is data available
	 */
	public Boolean getIsDataAvailable() {
		return isDataAvailable;
	}

	/**
	 * Sets the checks if is data available.
	 *
	 * @param isDataAvailable
	 *            the new checks if is data available
	 */
	public void setIsDataAvailable(Boolean isDataAvailable) {
		this.isDataAvailable = isDataAvailable;
	}

	/**
	 * Gets the week.
	 *
	 * @return the week
	 */
	public Integer getWeek() {
		return week;
	}

	/**
	 * Sets the week.
	 *
	 * @param week
	 *            the new week
	 */
	public void setWeek(Integer week) {
		this.week = week;
	}

	/**
	 * Gets the trav distance.
	 *
	 * @return the trav distance
	 */
	public Double getTravDistance() {
		return travDistance;
	}

	/**
	 * Sets the trav distance.
	 *
	 * @param travDistance
	 *            the new trav distance
	 */
	public void setTravDistance(Double travDistance) {
		this.travDistance = travDistance;
	}

	/**
	 * Gets the end lat.
	 *
	 * @return the end lat
	 */
	public Double getEndLat() {
		return endLat;
	}

	/**
	 * Sets the end lat.
	 *
	 * @param endLat
	 *            the new end lat
	 */
	public void setEndLat(Double endLat) {
		this.endLat = endLat;
	}

	/**
	 * Gets the end lng.
	 *
	 * @return the end lng
	 */
	public Double getEndLng() {
		return endLng;
	}

	/**
	 * Sets the end lng.
	 *
	 * @param endLng
	 *            the new end lng
	 */
	public void setEndLng(Double endLng) {
		this.endLng = endLng;
	}

	/**
	 * Gets the stop time.
	 *
	 * @return the stop time
	 */
	public Date getStopTime() {
		return stopTime;
	}

	/**
	 * Sets the stop time.
	 *
	 * @param stopTime
	 *            the new stop time
	 */
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	/**
	 * Gets the stopped by.
	 *
	 * @return the stopped by
	 */
	public DriveStoppedBy getStoppedBy() {
		return stoppedBy;
	}

	/**
	 * Sets the stopped by.
	 *
	 * @param stoppedBy
	 *            the new stopped by
	 */
	public void setStoppedBy(DriveStoppedBy stoppedBy) {
		this.stoppedBy = stoppedBy;
	}

	/**
	 * Gets the area.
	 *
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * Sets the area.
	 *
	 * @param area
	 *            the new area
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * Gets the area type.
	 *
	 * @return the area type
	 */
	public String getAreaType() {
		return areaType;
	}

	/**
	 * Sets the area type.
	 *
	 * @param areaType
	 *            the new area type
	 */
	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	/**
	 * Gets the start lat.
	 *
	 * @return the start lat
	 */
	public Double getStartLat() {
		return startLat;
	}

	/**
	 * Sets the start lat.
	 *
	 * @param startLat
	 *            the new start lat
	 */
	public void setStartLat(Double startLat) {
		this.startLat = startLat;
	}

	/**
	 * Gets the start lng.
	 *
	 * @return the start lng
	 */
	public Double getStartLng() {
		return startLng;
	}

	/**
	 * Sets the start lng.
	 *
	 * @param startLng
	 *            the new start lng
	 */
	public void setStartLng(Double startLng) {
		this.startLng = startLng;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "LiveTestDrive [driveId=" + driveId + ", user=" + user + ", imei=" + imei + ", status=" + status
				+ ", type=" + type + ", networkType=" + networkType + ", operatorName=" + operatorName + ", startTime="
				+ startTime + ", lastModifiedTime=" + lastModifiedTime + ", isDataAvailable=" + isDataAvailable
				+ ", week=" + week + ", travDistance=" + travDistance + ", endLat=" + endLat + ", endLng=" + endLng
				+ ", stopTime=" + stopTime + ", stoppedBy=" + stoppedBy + ", area=" + area + ", areaType=" + areaType
				+ ", startLat=" + startLat + ", startLng=" + startLng + "]";
	}
}
