package com.inn.foresight.core.ztepower.wrapper;

import java.util.Date;

import java.util.Map;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.Vendor;
@JpaWrapper
@RestWrapper
public class ZTEPowerResultWrapper {
	private String lscId;
	private String lscName;
	private String areaName;
	private String stationName;
	private String areaId;
	private String stationId;
	private String deviceId;
	private String deviceName;
	private String meteId;
	private String meteName;
	private String meteKind;
	private String geographyL4;
	private String geographyL3;
	private String geographyL2;
	private String geographyL1;
	private String ipAddress;
	private String vendor;
	private String domain;
	
	private Long stationCount;
	private Long deviceCount;
	private Long meteCount;
	
	private String siteId;
	private Double latitude;
	private Double longitude;
	private Integer id;
	private Integer meteInfoId;
	private Map<String, Long> ztePowerSummaryCount;
	private String enbSubnetId;
	private String stationSubnetId;
	private String oldVendorId;
	private String sfId;
	private Date createdTime;
	private Date modificationTime;
	private Boolean isDeleted;

	
	public ZTEPowerResultWrapper(String areaName,Long stationCount,Long deviceCount,Long meteCount,Integer id,Date modificationTime) {
		super();
		this.areaName=areaName;
		this.stationCount=stationCount;
		this.deviceCount=deviceCount;
		this.meteCount=meteCount;
		this.id=id;
		this.modificationTime=modificationTime;
	}
	public ZTEPowerResultWrapper(String stationName,Long deviceCount,Long meteCount,String siteId,Double latitude,Double longitude,Integer id,String enbSubnetId,String subnetId,String oldVendorId,String sfId,String stationId,Date modificationTime) {
		super();
		this.stationName=stationName;
		this.deviceCount=deviceCount;
		this.meteCount=meteCount;
		this.siteId=siteId;
		this.latitude=latitude;
		this.longitude=longitude;
		this.id=id;
		this.enbSubnetId=enbSubnetId;
		this.stationSubnetId=subnetId;
		this.oldVendorId=oldVendorId;
		this.sfId=sfId;
		this.stationId=stationId;
		this.modificationTime=modificationTime;
	}
	public ZTEPowerResultWrapper(String deviceName,Long meteCount,Integer nePk,Integer metePk,String deviceId,Date modificationTime) {
		super();
		this.deviceName=deviceName;
		this.meteCount=meteCount;
		this.id=nePk;
		this.meteInfoId=metePk;
		this.deviceId=deviceId;
		this.modificationTime=modificationTime;
	}
	
	public ZTEPowerResultWrapper(String lscId,String lscName,String areaId,String areaName,String stationId,String stationName,String deviceId,String deviceName,Vendor vendor,Domain domain,String meteId,String meteName,String meteKind,String ipAddress) {
		this.lscId=lscId;
		this.lscName=lscName;
		this.areaId=areaId;
		this.areaName=areaName;
		this.stationId=stationId;
		this.stationName=stationName;
		this.deviceId=deviceId;
		this.deviceName=deviceName;
		this.vendor=vendor!=null?vendor.toString():null;
		this.domain=domain!=null?domain.toString():null;
		this.meteId=meteId;
		this.meteName=meteName;
		this.meteKind=meteKind;
//		if (geographyL4 != null) {
//			this.geographyL4 = geographyL4.getName();
//			if (geographyL4.getGeographyL3() != null) {
//				this.geographyL3 = geographyL4.getGeographyL3()
//											.getName();
//				if (geographyL4	.getGeographyL3()
//							.getGeographyL2() != null) {
//					this.geographyL2 = geographyL4.getGeographyL3()
//												.getGeographyL2()
//												.getName();
//					if (geographyL4	.getGeographyL3()
//								.getGeographyL2()
//								.getGeographyL1() != null)
//						this.geographyL1 = geographyL4.getGeographyL3()
//													.getGeographyL2()
//													.getGeographyL1()
//													.getName();
//				}
//			}
//		}
		this.ipAddress=ipAddress;
	}
	
	public ZTEPowerResultWrapper() {
		super();
	}
	/**
	 * @return the ztePowerSummaryCount
	 */
	public Map<String, Long> getZtePowerSummaryCount() {
		return ztePowerSummaryCount;
	}

	/**
	 * @param ztePowerSummaryCount the ztePowerSummaryCount to set
	 */
	public void setZtePowerSummaryCount(Map<String, Long> ztePowerSummaryCount) {
		this.ztePowerSummaryCount = ztePowerSummaryCount;
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
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * @param deviceName the deviceName to set
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the meteId
	 */
	public String getMeteId() {
		return meteId;
	}

	/**
	 * @param meteId the meteId to set
	 */
	public void setMeteId(String meteId) {
		this.meteId = meteId;
	}

	/**
	 * @return the meteName
	 */
	public String getMeteName() {
		return meteName;
	}

	/**
	 * @param meteName the meteName to set
	 */
	public void setMeteName(String meteName) {
		this.meteName = meteName;
	}

	/**
	 * @return the meteKind
	 */
	public String getMeteKind() {
		return meteKind;
	}

	/**
	 * @param meteKind the meteKind to set
	 */
	public void setMeteKind(String meteKind) {
		this.meteKind = meteKind;
	}

	/**
	 * @return the geographyL4
	 */
	public String getGeographyL4() {
		return geographyL4;
	}

	/**
	 * @param geographyL4 the geographyL4 to set
	 */
	public void setGeographyL4(String geographyL4) {
		this.geographyL4 = geographyL4;
	}

	/**
	 * @return the geographyL3
	 */
	public String getGeographyL3() {
		return geographyL3;
	}

	/**
	 * @param geographyL3 the geographyL3 to set
	 */
	public void setGeographyL3(String geographyL3) {
		this.geographyL3 = geographyL3;
	}

	/**
	 * @return the geographyL2
	 */
	public String getGeographyL2() {
		return geographyL2;
	}

	/**
	 * @param geographyL2 the geographyL2 to set
	 */
	public void setGeographyL2(String geographyL2) {
		this.geographyL2 = geographyL2;
	}

	/**
	 * @return the geographyL1
	 */
	public String getGeographyL1() {
		return geographyL1;
	}

	/**
	 * @param geographyL1 the geographyL1 to set
	 */
	public void setGeographyL1(String geographyL1) {
		this.geographyL1 = geographyL1;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the stationCount
	 */
	public Long getStationCount() {
		return stationCount;
	}

	/**
	 * @param stationCount the stationCount to set
	 */
	public void setStationCount(Long stationCount) {
		this.stationCount = stationCount;
	}


	/**
	 * @return the meteCount
	 */
	public Long getMeteCount() {
		return meteCount;
	}

	/**
	 * @param meteCount the meteCount to set
	 */
	public void setMeteCount(Long meteCount) {
		this.meteCount = meteCount;
	}


	/**
	 * @return the deviceCount
	 */
	public Long getDeviceCount() {
		return deviceCount;
	}


	/**
	 * @param deviceCount the deviceCount to set
	 */
	public void setDeviceCount(Long deviceCount) {
		this.deviceCount = deviceCount;
	}
	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}
	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
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
	 * @return the meteInfoId
	 */
	public Integer getMeteInfoId() {
		return meteInfoId;
	}
	/**
	 * @param meteInfoId the meteInfoId to set
	 */
	public void setMeteInfoId(Integer meteInfoId) {
		this.meteInfoId = meteInfoId;
	}
	
	/**
	 * @return the stationSubnetId
	 */
	public String getStationSubnetId() {
		return stationSubnetId;
	}
	/**
	 * @param stationSubnetId the stationSubnetId to set
	 */
	public void setStationSubnetId(String stationSubnetId) {
		this.stationSubnetId = stationSubnetId;
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
	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}
	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
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
	
}