package com.inn.foresight.core.excel.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQuery(name = "writeInToExcel", query = "select ed from ExcelData ed ")

@PersistenceContext(name="myapp")
@XmlRootElement(name = "ExcelData")
@Entity
@Table(name = "userdata")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class ExcelData implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = -6805963961410479230L;

	@Override
	public String toString() {
		return "UserData [ gn=" + gn + ", longitude=" + longitude + ", latitude=" + latitude + ", rowkey=" + rowkey + ", geography=" + geography + ", creationTime=" + creationTime
				+ ", modificationTime=" + modificationTime + "]";
	}

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Basic
	@Column(name="uid")
	private Integer id;

	@Basic
	@Column(name="gn")
	private String gn;

	@Basic
	@Column(name="longitude")
	private Double longitude;


	@Basic
	@Column(name="latitude")
	private Double latitude;


	@Basic
	@Column(name="rowkey")
	private Double rowkey;


	@Basic
	@Column(name="geography")
	private Double geography;


	@Basic
	@Column(name="creationTime")
	private Date creationTime ;
	
	
	@Basic
	@Column(name="modificationTime")
	private Date modificationTime ;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGn() {
		return gn;
	}

	public void setGn(String gn) {
		this.gn = gn;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getRowkey() {
		return rowkey;
	}

	public void setRowkey(Double d) {
		this.rowkey = d;
	}

	public Double getGeography() {
		return geography;
	}

	public void setGeography(Double geography) {
		this.geography = geography;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	
}
