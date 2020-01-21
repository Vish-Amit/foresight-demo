package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Table;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@NamedQuery(name = "insertIntoDb", query = "insert into userdata values()")

@EntityScan
@Table(name = "userdata")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class UserData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "UserData [id=" + id + ", gn=" + gn + ", longitude=" + longitude + ", latitude=" + latitude + ", rowkey=" + rowkey + ", geography=" + geography + ", creationTime=" + creationTime
				+ ", modificationTime=" + modificationTime + "]";
	}

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	LocalDateTime now = LocalDateTime.now();

	private Integer id;

	private String gn;

	private Double longitude;

	private Double latitude;

	private Double rowkey;

	private Double geography;

	private String creationTime = dtf.format(now);

	private String modificationTime;

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

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}

}
