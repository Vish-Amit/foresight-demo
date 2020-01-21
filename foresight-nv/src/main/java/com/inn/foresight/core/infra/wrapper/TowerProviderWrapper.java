package com.inn.foresight.core.infra.wrapper;

import java.util.Arrays;
import java.util.Date;

import com.inn.core.generic.wrapper.JpaWrapper;
@JpaWrapper
public class TowerProviderWrapper {
	private Integer id;
	private String towerId;
	private String towerName;
	private Double latitude;
	private Double longitude;
	private Double towerHeight;
	private String towerProviderName;
	private String serviceType;
	private String legendColor;
	private Integer providerRank;
	private String geographyL4;
	private String geographyL3;
	private String geographyL2;
	private String geographyL1;
	private Date creationTime;
	private Date modificationTime;
	private Long totalCount;
	private String geographyName;
	private Double[] centroid;
	public TowerProviderWrapper(Integer id,String towerId,String towerName,Double latitude,Double longitude,Double towerHeight,String towerProviderName,String serviceType,String legendColor,Integer providerRank,Date creationTime,Date modificationTime,String geographyL4,String geographyL3,String geographyL2,String geographyL1) {
		super();
        this.id=id;
        this.towerId=towerId;
        this.towerName=towerName;
        this.latitude=latitude;
        this.longitude=longitude;
        this.towerHeight=towerHeight;
        this.towerProviderName=towerProviderName;
        this.serviceType=serviceType;
        this.legendColor=legendColor;
        this.providerRank=providerRank;
        this.creationTime=creationTime;
        this.modificationTime=modificationTime;
        this.geographyL4=geographyL4;
        this.geographyL3=geographyL3;
        this.geographyL2=geographyL2;
        this.geographyL1=geographyL1;
	}
	public TowerProviderWrapper(String towerName,String legendColor) {
		 this.towerName=towerName;
		 this.legendColor=legendColor;
	}
	public TowerProviderWrapper(String geographyName, Long totalCount,Double latitude,Double longitude,Date modificationTime) {
		this.geographyName = geographyName;
		this.totalCount = totalCount;
		this.centroid = new Double[] { latitude, longitude };
		this.modificationTime=modificationTime;
	}
	public TowerProviderWrapper() {
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTowerId() {
		return towerId;
	}
	public void setTowerId(String towerId) {
		this.towerId = towerId;
	}
	public String getTowerName() {
		return towerName;
	}
	public void setTowerName(String towerName) {
		this.towerName = towerName;
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
	public String getTowerProviderName() {
		return towerProviderName;
	}
	public void setTowerProviderName(String towerProviderName) {
		this.towerProviderName = towerProviderName;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getLegendColor() {
		return legendColor;
	}
	public void setLegendColor(String legendColor) {
		this.legendColor = legendColor;
	}
	public Integer getProviderRank() {
		return providerRank;
	}
	public void setProviderRank(Integer providerRank) {
		this.providerRank = providerRank;
	}
	public String getGeographyL4() {
		return geographyL4;
	}
	public void setGeographyL4(String geographyL4) {
		this.geographyL4 = geographyL4;
	}
	public String getGeographyL3() {
		return geographyL3;
	}
	public void setGeographyL3(String geographyL3) {
		this.geographyL3 = geographyL3;
	}
	public String getGeographyL2() {
		return geographyL2;
	}
	public void setGeographyL2(String geographyL2) {
		this.geographyL2 = geographyL2;
	}
	public String getGeographyL1() {
		return geographyL1;
	}
	public void setGeographyL1(String geographyL1) {
		this.geographyL1 = geographyL1;
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
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public String getGeographyName() {
		return geographyName;
	}
	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
	}
	public Double[] getCentroid() {
		return centroid;
	}
	public void setCentroid(Double[] centroid) {
		this.centroid = centroid;
	}
	
	public Double getTowerHeight() {
		return towerHeight;
	}
	public void setTowerHeight(Double towerHeight) {
		this.towerHeight = towerHeight;
	}
	@Override
	public String toString() {
		return "TowerProviderWrapper [id=" + id + ", towerId=" + towerId + ", towerName=" + towerName + ", latitude="
				+ latitude + ", longitude=" + longitude + ", towerHeight=" + towerHeight + ", towerProviderName="
				+ towerProviderName + ", serviceType=" + serviceType + ", legendColor=" + legendColor
				+ ", providerRank=" + providerRank + ", geographyL4=" + geographyL4 + ", geographyL3=" + geographyL3
				+ ", geographyL2=" + geographyL2 + ", geographyL1=" + geographyL1 + ", creationTime=" + creationTime
				+ ", modificationTime=" + modificationTime + ", totalCount=" + totalCount + ", geographyName="
				+ geographyName + ", centroid=" + Arrays.toString(centroid) + "]";
	}
	
}
