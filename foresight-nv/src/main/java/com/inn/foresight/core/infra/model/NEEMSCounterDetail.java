package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@XmlRootElement(name = "NEEMSCounterDetail")
@Entity
@Table(name = "NEEMSCounterDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NEEMSCounterDetail implements Serializable{

	private static final long serialVersionUID = 8970083934776851271L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "neemscounterdetailid_pk")
	private Integer id;

	/** The ne location. */
	@JoinColumn(name = "networkelementid_fk", nullable = true)
	@OneToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;
	
	@Column(name = "nonradiatingcounter")
	private Integer nonradiatingCounter;

	@Column(name = "creationtime")
	private Date creationTime;

	@Column(name = "modificationtime")
	private Date modificationTime;
	
	@Column(name = "nonradiatingdate")
	private Date nonradiatingDate;
	
	@Column(name = "nonradiatingcounterdate")
	private Date nonradiatingcounterdate;
	
	/**
	 * @return the networkElement
	 */
	public NetworkElement getNetworkElement() {
		return networkElement;
	}
	/**
	 * @param networkElement the networkElement to set
	 */
	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}

	/**
	 * @return the nonradiatingCounter
	 */
	public Integer getNonradiatingCounter() {
		return nonradiatingCounter;
	}

	/**
	 * @param nonradiatingCounter the nonradiatingCounter to set
	 */
	public void setNonradiatingCounter(Integer nonradiatingCounter) {
		this.nonradiatingCounter = nonradiatingCounter;
	}

	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
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
	 * @return the nonradiatingDate
	 */
	public Date getNonradiatingDate() {
		return nonradiatingDate;
	}

	/**
	 * @param nonradiatingDate the nonradiatingDate to set
	 */
	public void setNonradiatingDate(Date nonradiatingDate) {
		this.nonradiatingDate = nonradiatingDate;
	}

	/**
	 * @return the nonradiatingcounterdate
	 */
	public Date getNonradiatingcounterdate() {
		return nonradiatingcounterdate;
	}

	/**
	 * @param nonradiatingcounterdate the nonradiatingcounterdate to set
	 */
	public void setNonradiatingcounterdate(Date nonradiatingcounterdate) {
		this.nonradiatingcounterdate = nonradiatingcounterdate;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "NEEMSCounterDetail [id=" + id + ", networkElement=" + networkElement + ", nonradiatingCounter="
				+ nonradiatingCounter + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime
				+ ", nonradiatingDate=" + nonradiatingDate + ", nonradiatingcounterdate=" + nonradiatingcounterdate
				+ "]";
	}

	
}
