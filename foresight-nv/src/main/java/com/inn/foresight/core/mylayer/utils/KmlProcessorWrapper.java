package com.inn.foresight.core.mylayer.utils;

import java.util.Date;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class KmlProcessorWrapper {

	private Integer id;
	private String colorCode;
	private String comment;
	private Date createdTime;
	private Long fileSize;
	private String kmlName;
	private String kmlPath;
	private String kmlType;
	private Double latitude;
	private Double longitude;
	private Date modifiedTime;
	private Integer zoomLevel;
	private String property;
	/** The min lat. */
	Double min_lat = 90.0;
	
	/** The max lat. */
	Double max_lat = -90.0;
	
	/** The min lon. */
	Double min_lon = 180.0;
	
	/** The max lon. */
	Double max_lon = -180.0;
	public KmlProcessorWrapper() {
	}
	public KmlProcessorWrapper(Integer id,String colorCode,String comment,Date createdTime,Long fileSize,String kmlName,String kmlPath,
			String kmlType,Double latitude,Double longitude,Date modifiedTime,Integer zoomLevel,String property)
	{
		this.id=id;
		this.colorCode=colorCode;
		this.comment=comment;
		this.createdTime=createdTime;
		this.fileSize=fileSize;
		this.kmlName=kmlName;
		this.kmlPath=kmlPath;
		this.kmlType=kmlType;
		this.latitude=latitude;
		this.longitude=longitude;
		this.modifiedTime=modifiedTime;
		this.zoomLevel=zoomLevel;
		this.property=property;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getKmlName() {
		return kmlName;
	}
	public void setKmlName(String kmlName) {
		this.kmlName = kmlName;
	}
	public String getKmlPath() {
		return kmlPath;
	}
	public void setKmlPath(String kmlPath) {
		this.kmlPath = kmlPath;
	}
	public String getKmlType() {
		return kmlType;
	}
	public void setKmlType(String kmlType) {
		this.kmlType = kmlType;
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
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public Integer getZoomLevel() {
		return zoomLevel;
	}
	public void setZoomLevel(Integer zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
	public Double getMin_lat() {
		return min_lat;
	}
	public void setMin_lat(Double min_lat) {
		this.min_lat = min_lat;
	}
	public Double getMax_lat() {
		return max_lat;
	}
	public void setMax_lat(Double max_lat) {
		this.max_lat = max_lat;
	}
	public Double getMin_lon() {
		return min_lon;
	}
	public void setMin_lon(Double min_lon) {
		this.min_lon = min_lon;
	}
	public Double getMax_lon() {
		return max_lon;
	}
	public void setMax_lon(Double max_lon) {
		this.max_lon = max_lon;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	@Override
	public String toString() {
		return "KmlProcessorWrapper [id=" + id + ", colorCode=" + colorCode + ", comment=" + comment + ", createdTime="
				+ createdTime + ", fileSize=" + fileSize + ", kmlName=" + kmlName + ", kmlPath=" + kmlPath
				+ ", kmlType=" + kmlType + ", latitude=" + latitude + ", longitude=" + longitude + ", modifiedTime="
				+ modifiedTime + ", zoomLevel=" + zoomLevel + ", property=" + property + ", min_lat=" + min_lat
				+ ", max_lat=" + max_lat + ", min_lon=" + min_lon + ", max_lon=" + max_lon + "]";
	}
	
}
