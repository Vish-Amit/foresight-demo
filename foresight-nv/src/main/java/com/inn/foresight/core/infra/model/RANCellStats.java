package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
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

/**
 * The Class RANCellStats.
 * 
 * 
 * 
 */
@XmlRootElement(name = "RANCellStats")
@Entity
@Table(name = "RANCellStats")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class RANCellStats implements Serializable {



  /**
   * The Constant serialVersionUID
   */
  private static final long serialVersionUID = 1L;

  /** The id. */
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column(name = "rancellstatsid_pk")
  private Integer id;

  /** The emslivecounter */
  @Basic
  @Column(name = "emslivecounter")
  private Integer emsLiveCounter;


  @Basic
  @Column(name = "nonradiatingcounter")
  private Integer nonRadiatingCounter;

  /** The emslivedate */
  @Basic
  @Column(name = "emslivedate")
  private Date emsLiveDate;


  /** The nonradiatingdate */
  @Basic
  @Column(name = "nonradiatingdate")
  private Date nonRadiatingDate;


  /** The emsstatus */
  @Basic
  @Column(name = "emsstatus")
  private String emsStatus;

  /** The emscounterdate */
  @Basic
  @Column(name = "emscounterdate")
  private Date emsCounterDate;


  /** The nonradiatingcounterdate */
  @Basic
  @Column(name = "nonradiatingcounterdate")
  private Date nonRadiatingCounterDate;


  /** The cmavailabilitydate */
  @Basic
  @Column(name = "cmavailabilitydate")
  private String cmAvailabilityDate;

  /** The pmavailabilitydate */
  @Basic
  @Column(name = "pmavailabilitydate")
  private String pmAvailabilityDate;
  
  /** The fmavailabilitydate */
  @Basic
  @Column(name = "fmavailabilitydate")
  private String fmAvailabilityDate;
  
  /** The randetailid_fk */
  @JoinColumn(name = "randetailid_fk", nullable = true)
  @OneToOne(fetch = FetchType.LAZY)
  private RANDetail ranDetail;
  
  
  /** The decommissioningdate */
  @Basic
  @Column(name = "decommissioningdate")
  private Date decommissioningDate;
  
  
  /** The decommissioningstatus */
  @Basic
  @Column(name = "decommissioningstatus")
  private String decommissioningStatus;
  
  /** The decommissioningcounter */
  @Basic
  @Column(name = "decommissioningcounter")
  private Integer decommissioningCounter;
  
  /** The created time. */
  @Basic
  @Column(name = "creationtime")
  private Date createdTime;

  /** The modified time. */
  @Basic
  @Column(name = "modificationtime")
  private Date modifiedTime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getEmsLiveCounter() {
    return emsLiveCounter;
  }

  public void setEmsLiveCounter(Integer emsLiveCounter) {
    this.emsLiveCounter = emsLiveCounter;
  }

  public Integer getNonRadiatingCounter() {
    return nonRadiatingCounter;
  }

  public void setNonRadiatingCounter(Integer nonRadiatingCounter) {
    this.nonRadiatingCounter = nonRadiatingCounter;
  }

  public Date getEmsLiveDate() {
    return emsLiveDate;
  }

  public void setEmsLiveDate(Date emsLiveDate) {
    this.emsLiveDate = emsLiveDate;
  }

  public Date getNonRadiatingDate() {
    return nonRadiatingDate;
  }

  public void setNonRadiatingDate(Date nonRadiatingDate) {
    this.nonRadiatingDate = nonRadiatingDate;
  }

  public String getEmsStatus() {
    return emsStatus;
  }

  public void setEmsStatus(String emsStatus) {
    this.emsStatus = emsStatus;
  }

  public Date getEmsCounterDate() {
    return emsCounterDate;
  }

  public void setEmsCounterDate(Date emsCounterDate) {
    this.emsCounterDate = emsCounterDate;
  }

  public Date getNonRadiatingCounterDate() {
    return nonRadiatingCounterDate;
  }

  public void setNonRadiatingCounterDate(Date nonRadiatingCounterDate) {
    this.nonRadiatingCounterDate = nonRadiatingCounterDate;
  }

  public String getCmAvailabilityDate() {
    return cmAvailabilityDate;
  }

  public void setCmAvailabilityDate(String cmAvailabilityDate) {
    this.cmAvailabilityDate = cmAvailabilityDate;
  }

  public String getPmAvailabilityDate() {
    return pmAvailabilityDate;
  }

  public void setPmAvailabilityDate(String pmAvailabilityDate) {
    this.pmAvailabilityDate = pmAvailabilityDate;
  }

  public String getFmAvailabilityDate() {
    return fmAvailabilityDate;
  }

  public void setFmAvailabilityDate(String fmAvailabilityDate) {
    this.fmAvailabilityDate = fmAvailabilityDate;
  }


  public RANDetail getRanDetail() {
	  return ranDetail;
  }

  public void setRanDetail(RANDetail ranDetail) {
	  this.ranDetail = ranDetail;
  }

public Date getDecommissioningDate() {
    return decommissioningDate;
  }

  public void setDecommissioningDate(Date decommissioningDate) {
    this.decommissioningDate = decommissioningDate;
  }

  public String getDecommissioningStatus() {
    return decommissioningStatus;
  }

  public void setDecommissioningStatus(String decommissioningStatus) {
    this.decommissioningStatus = decommissioningStatus;
  }

  public Integer getDecommissioningCounter() {
    return decommissioningCounter;
  }

  public void setDecommissioningCounter(Integer decommissioningCounter) {
    this.decommissioningCounter = decommissioningCounter;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public Date getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(Date modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

  @Override
  public String toString() {
	  return "RANCellStats [id=" + id + ", emsLiveCounter=" + emsLiveCounter + ", nonRadiatingCounter="
			  + nonRadiatingCounter + ", emsLiveDate=" + emsLiveDate + ", nonRadiatingDate=" + nonRadiatingDate
			  + ", emsStatus=" + emsStatus + ", emsCounterDate=" + emsCounterDate + ", nonRadiatingCounterDate="
			  + nonRadiatingCounterDate + ", cmAvailabilityDate=" + cmAvailabilityDate + ", pmAvailabilityDate="
			  + pmAvailabilityDate + ", fmAvailabilityDate=" + fmAvailabilityDate + ", ranDetail=" + ranDetail
			  + ", decommissioningDate=" + decommissioningDate + ", decommissioningStatus=" + decommissioningStatus
			  + ", decommissioningCounter=" + decommissioningCounter + ", createdTime=" + createdTime + ", modifiedTime="
			  + modifiedTime + "]";
  }


  
}
