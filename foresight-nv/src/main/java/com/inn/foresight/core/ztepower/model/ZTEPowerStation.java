package com.inn.foresight.core.ztepower.model;

import java.io.Serializable;
import java.util.Date;

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
import com.inn.product.um.geography.model.GeographyL4;

@NamedQueries({
	@NamedQuery(name="getZTEPowerStationByStationId",query="select z from ZTEPowerStation z where z.id=:id"),
	@NamedQuery(name="getNEIdListByStationId",query="select ze.networkElement.id from ZTEPowerStationNEMapping ze where ze.ztePowerStation.id=:id")

})

@Entity
@Table(name = "ZTEPowerStation")
@XmlRootElement(name = "ZTEPowerStation")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class ZTEPowerStation implements Serializable{
	
	private static final long serialVersionUID = 8905158308123323064L;


	  @Id
	  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	  @Column(name = "ztepowerstationid_pk")
	  private Integer id;


	  @Column(name = "stationid")
	  private String stationId;
	  
	  @Column(name = "stationname")
	  private String stationName;

	  @Column(name = "neid ")
	  private String neId ;
	  
	  @JoinColumn(name = "ztepowerregionid_fk", nullable = true)
  	  @ManyToOne(fetch = FetchType.LAZY)
      private ZTEPowerRegion ztePowerRegion;
      
      @JoinColumn(name = "geographyl4id_fk", nullable = true)
  	  @ManyToOne(fetch = FetchType.LAZY)
      private GeographyL4 geographyL4;
	  
	  @Column(name = "creationtime")
	  private Date creationTime;

	  @Column(name = "modificationtime")
	  private Date modificationTime;
	  
	  @Column(name = "latitude")
	  private Double latitude;

	  @Column(name = "longitude")
	  private Double longitude;
	  
	  @Column(name="deleted")
	  private Boolean isDeleted;
	  
	  @Column(name = "enbsubnetid")
	  private String enbSubnetId;
	  
	  @Column(name = "subnetid")
	  private String subnetId;
	  
	  @Column(name = "oldvendorid")
	  private String oldVendorId;
	  
	  @Column(name = "sfid")
	  private String sfId;
	  
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
	 * @return the stationId
	 */
	public String getStationId() {
		return stationId;
	}

	/**
	 * @param stationId the stationId to set
	 */
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	/**
	 * @return the stationName
	 */
	public String getStationName() {
		return stationName;
	}

	/**
	 * @param stationName the stationName to set
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	/**
	 * @return the neId
	 */
	public String getNeId() {
		return neId;
	}

	/**
	 * @param neId the neId to set
	 */
	public void setNeId(String neId) {
		this.neId = neId;
	}

	/**
	 * @return the ztePowerRegion
	 */
	public ZTEPowerRegion getZtePowerRegion() {
		return ztePowerRegion;
	}

	/**
	 * @param ztePowerRegion the ztePowerRegion to set
	 */
	public void setZtePowerRegion(ZTEPowerRegion ztePowerRegion) {
		this.ztePowerRegion = ztePowerRegion;
	}

	/**
	 * @return the geographyL4
	 */
	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	/**
	 * @param geographyL4 the geographyL4 to set
	 */
	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
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
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the isDeleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the enbSubnetId
	 */
	public String getEnbSubnetId() {
		return enbSubnetId;
	}

	/**
	 * @param enbSubnetId the enbSubnetId to set
	 */
	public void setEnbSubnetId(String enbSubnetId) {
		this.enbSubnetId = enbSubnetId;
	}

	/**
	 * @return the subnetId
	 */
	public String getSubnetId() {
		return subnetId;
	}

	/**
	 * @param subnetId the subnetId to set
	 */
	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}

	/**
	 * @return the oldVendorId
	 */
	public String getOldVendorId() {
		return oldVendorId;
	}

	/**
	 * @param oldVendorId the oldVendorId to set
	 */
	public void setOldVendorId(String oldVendorId) {
		this.oldVendorId = oldVendorId;
	}

	/**
	 * @return the sfId
	 */
	public String getSfId() {
		return sfId;
	}

	/**
	 * @param sfId the sfId to set
	 */
	public void setSfId(String sfId) {
		this.sfId = sfId;
	}

	
}
