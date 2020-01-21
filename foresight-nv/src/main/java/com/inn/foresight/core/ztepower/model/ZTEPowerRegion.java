/**
 */
package com.inn.foresight.core.ztepower.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@NamedQuery(name = "getZTEPowerAreaWiseCount", query="select new com.inn.foresight.core.ztepower.wrapper.ZTEPowerResultWrapper(r.areaName,count(distinct s.id),count(distinct sd.networkElement.neId),count(dm.networkElement.neId),r.id,r.modificationTime) from ZTEPowerRegion r left join ZTEPowerStation s on r.id=s.ztePowerRegion.id left join ZTEPowerStationNEMapping sd on s.id=sd.ztePowerStation.id left join ZTEPowerNEMeteMapping dm  on sd.networkElement.id=dm.networkElement.id and (s.latitude is not null and s.longitude is not null and s.geographyL4 is not null) and (s.isDeleted is null or s.isDeleted=0) and (sd.networkElement.isDeleted is null or sd.networkElement.isDeleted=0) and r.areaName is not null group by r.areaName")
@NamedQuery(name = "getZTEPowerAreaWiseCountForDescripency", query="select new com.inn.foresight.core.ztepower.wrapper.ZTEPowerResultWrapper(r.areaName,count(distinct s.id),count(distinct sd.networkElement.neId),count(dm.networkElement.neId),r.id,r.modificationTime) from ZTEPowerRegion r left join ZTEPowerStation s on r.id=s.ztePowerRegion.id left join ZTEPowerStationNEMapping sd on s.id=sd.ztePowerStation.id left join ZTEPowerNEMeteMapping dm  on sd.networkElement.id=dm.networkElement.id and (s.latitude is null or s.longitude is null or s.geographyL4 is null) and (s.isDeleted is null or s.isDeleted=0) and (sd.networkElement.isDeleted is null or sd.networkElement.isDeleted=0) and r.areaName is not null group by r.areaName")

@NamedQuery(name = "getZTEPowerStationWiseCount", query="select new com.inn.foresight.core.ztepower.wrapper.ZTEPowerResultWrapper(s.stationName,count(distinct sd.networkElement.id),count(dm.networkElement.id),s.neId,s.latitude,s.longitude,s.id,s.enbSubnetId,s.subnetId,s.oldVendorId,s.sfId,s.stationId,s.modificationTime) from ZTEPowerStation s left join ZTEPowerStationNEMapping sd on s.id=sd.ztePowerStation.id  left join ZTEPowerNEMeteMapping dm  on sd.networkElement.id=dm.networkElement.id where s.ztePowerRegion.areaName in(:areaName) and (s.latitude is not null and s.longitude is not null and s.geographyL4 is not null) and (s.isDeleted is null or s.isDeleted=0) and (sd.networkElement.isDeleted is null or sd.networkElement.isDeleted=0) group by s.stationName")
@NamedQuery(name = "getZTEPowerStationWiseCountForDescripency", query="select new com.inn.foresight.core.ztepower.wrapper.ZTEPowerResultWrapper(s.stationName,count(distinct sd.networkElement.id),count(dm.networkElement.id),s.neId,s.latitude,s.longitude,s.id,s.enbSubnetId,s.subnetId,s.oldVendorId,s.sfId,s.stationId,s.modificationTime) from ZTEPowerStation s left join ZTEPowerStationNEMapping sd on s.id=sd.ztePowerStation.id  left join ZTEPowerNEMeteMapping dm  on sd.networkElement.id=dm.networkElement.id where s.ztePowerRegion.areaName in(:areaName) and (s.latitude is null or s.longitude is null or s.geographyL4 is null) and (s.isDeleted is null or s.isDeleted=0) and (sd.networkElement.isDeleted is null or sd.networkElement.isDeleted=0) group by s.stationName")

@NamedQuery(name = "getZTEPowerDeviceWiseCount", query="select new com.inn.foresight.core.ztepower.wrapper.ZTEPowerResultWrapper(sd.networkElement.neName,count(dm.networkElement.id),sd.networkElement.id,dm.ztePowerMeteInfo.id,sd.networkElement.neId,sd.networkElement.modifiedTime) from ZTEPowerStation s left join ZTEPowerStationNEMapping sd on s.id=sd.ztePowerStation.id  left join ZTEPowerNEMeteMapping dm  on sd.networkElement.id=dm.networkElement.id where s.stationName in(:stationName) and (s.isDeleted is null or s.isDeleted=0) and (sd.networkElement.isDeleted is null or sd.networkElement.isDeleted=0) group by dm.networkElement.id")



@Entity
@Table(name = "ZTEPowerRegion")
@XmlRootElement(name = "ZTEPowerRegion")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})

public class ZTEPowerRegion implements Serializable {

	private static final long serialVersionUID = 9189095096824747884L;


  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column(name = "ztepowerregionid_pk")
  private Integer id;


  @Column(name = "lscid")
  private String lscId;
  
  @Column(name = "lscname")
  private String lscName;

  @Column(name = "areaid ")
  private String areaId ;
  
  @Column(name = "areaname")
  private String areaName;
  
  @Column(name = "creationtime")
  private Date creationTime;

  @Column(name = "modificationtime")
  private Date modificationTime;
  

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

/**
 * @return the lscId
 */
public String getLscId() {
	return lscId;
}

/**
 * @param lscId the lscId to set
 */
public void setLscId(String lscId) {
	this.lscId = lscId;
}

/**
 * @return the lscName
 */
public String getLscName() {
	return lscName;
}

/**
 * @param lscName the lscName to set
 */
public void setLscName(String lscName) {
	this.lscName = lscName;
}

/**
 * @return the areaId
 */
public String getAreaId() {
	return areaId;
}

/**
 * @param areaId the areaId to set
 */
public void setAreaId(String areaId) {
	this.areaId = areaId;
}

/**
 * @return the areaName
 */
public String getAreaName() {
	return areaName;
}

/**
 * @param areaName the areaName to set
 */
public void setAreaName(String areaName) {
	this.areaName = areaName;
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


}
