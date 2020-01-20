package com.inn.foresight.module.nv.device.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.inn.foresight.module.nv.app.model.APKDetail;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;

@Entity
@Table(name = "DeviceDashboard")
public class DeviceDashboard {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name="devicedashboardid_pk")
	private Integer id;
	
	@Column(name="deviceid")
	private String deviceId;
	
	@Column(name="totaltest")
	private Integer totalTest;
	
	@Column(name="consumption")
	private Double consumption;
	
	@Column(name="badge")
	private String badge;
	
	@Column(name="dlthroughput")
	private Double dlThroughput;
	
	@Column(name="ulthroughput")
	private Double ulThroughput;
	

	
	@Column(name="make")
	private String make;
	
	@Column(name="model")
	private String model;
	
	@Column(name="modificationtime")
	private Date modificationTime;
	
   
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "apkdetailid_fk", nullable = true)
	private APKDetail apkDetail;
	
	

	@Column(name="creationtime")
	private Date creationTime;

	@Column(name="latitude")
	private Double latitude;
	
	@Column(name="longitude")
	private Double longitude;


	@Column(name="operator")
	private String operator;

	@Column(name="cellserved")
	private Integer cellServed;
	
	@Column(name="badgeformula")
	private String badgeFormula;

	@Column(name="userrank")
	private Integer rank;


	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}



	public String getBadgeFormula() {
		return badgeFormula;
	}

	public void setBadgeFormula(String badgeFormula) {
		this.badgeFormula = badgeFormula;
	}
	
	public Integer getCellServed() {
		return cellServed;
	}

	public void setCellServed(Integer cellServed) {
		this.cellServed = cellServed;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}
	public Double  getLatitude() {
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

	public APKDetail getApkDetail() {
		return apkDetail;
	}

	public void setApkDetail(APKDetail apkDetail) {
		this.apkDetail = apkDetail;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getTotalTest() {
		return totalTest;
	}

	public void setTotalTest(Integer totalTest) {
		this.totalTest = totalTest;
	}

	public Double getConsumption() {
		return consumption;
	}

	public void setConsumption(Double consumption) {
		this.consumption = consumption;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}


	public Double getDlThroughput() {
		return dlThroughput;
	}

	public void setDlThroughput(Double dlThroughput) {
		this.dlThroughput = dlThroughput;
	}
	
	

	public Double getUlThroughput() {
		return ulThroughput;
	}

	public void setUlThroughput(Double ulThroughput) {
		this.ulThroughput = ulThroughput;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public String toString() {
		return "DeviceDashboard{" +
				"id=" + id +
				", deviceId='" + deviceId + '\'' +
				", totalTest=" + totalTest +
				", consumption=" + consumption +
				", badge='" + badge + '\'' +
				", dlThroughput=" + dlThroughput +
				", ulThroughput=" + ulThroughput +
				", make='" + make + '\'' +
				", model='" + model + '\'' +
				", modificationTime=" + modificationTime +
				", apkDetail=" + apkDetail +
				", creationTime=" + creationTime +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", operator='" + operator + '\'' +
				", cellServed=" + cellServed +
				", badgeFormula='" + badgeFormula + '\'' +
				", rank=" + rank +
				'}';
	}
}
