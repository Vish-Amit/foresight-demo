package com.inn.foresight.module.nv.layer.wrapper;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inn.core.generic.wrapper.RestWrapper;

@JsonInclude(Include.NON_EMPTY)
@RestWrapper
public class CoverageLayerWrapper {
	
	Integer zoom;
	String postFix;
	List<String> prefixList;
	List<String> columnList;
	String date;
	String tableName;
	Boolean isSiteCountAvailable;
	Boolean isTiReadyRequired;
	Boolean isAlarmRequired;
	List<String> neStatusList;
	String alarmCategory;
    Double nELat;
    Double nELng;
    Double sWLat;
    Double sWLng;
    String geographyLevel;
	             
	public Boolean getIsAlarmRequired() {
		return isAlarmRequired;
	}
	public void setIsAlarmRequired(Boolean isAlarmRequired) {
		this.isAlarmRequired = isAlarmRequired;
	}
	public Boolean getIsTiReadyRequired() {
		return isTiReadyRequired;
	}
	public void setIsTiReadyRequired(Boolean isTiReadyRequired) {
		this.isTiReadyRequired = isTiReadyRequired;
	}
	public List<String> getNeStatusList() {
		return neStatusList;
	}
	public void setNeStatusList(List<String> neStatusList) {
		this.neStatusList = neStatusList;
	}
	public String getAlarmCategory() {
		return alarmCategory;
	}
	public void setAlarmCategory(String alarmCategory) {
		this.alarmCategory = alarmCategory;
	}
	public Double getnELat() {
		return nELat;
	}
	public void setnELat(Double nELat) {
		this.nELat = nELat;
	}
	public Double getnELng() {
		return nELng;
	}
	public void setnELng(Double nELng) {
		this.nELng = nELng;
	}
	public Double getsWLat() {
		return sWLat;
	}
	public void setsWLat(Double sWLat) {
		this.sWLat = sWLat;
	}
	public Double getsWLng() {
		return sWLng;
	}
	public void setsWLng(Double sWLng) {
		this.sWLng = sWLng;
	}
	public Integer getZoom() {
		return zoom;
	}
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	public String getPostFix() {
		return postFix;
	}
	public void setPostFix(String postFix) {
		this.postFix = postFix;
	}
	public List<String> getPrefixList() {
		return prefixList;
	}
	public void setPrefixList(List<String> prefixList) {
		this.prefixList = prefixList;
	}
	public List<String> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public Boolean getIsSiteCountAvailable() {
		return isSiteCountAvailable;
	}
	public void setIsSiteCountAvailable(Boolean isSiteCountAvailable) {
		this.isSiteCountAvailable = isSiteCountAvailable;
	}
	
	public String getGeographyLevel() {
		return geographyLevel;
	}
	public void setGeographyLevel(String geographyLevel) {
		this.geographyLevel = geographyLevel;
	}
	@Override
	public String toString() {
		return "CoverageLayerWrapper [zoom=" + zoom + ", postFix=" + postFix + ", prefixList=" + prefixList
				+ ", columnList=" + columnList + ", date=" + date + ", tableName=" + tableName
				+ ", isSiteCountAvailable=" + isSiteCountAvailable + ", isTiReadyRequired=" + isTiReadyRequired
				+ ", isAlarmRequired=" + isAlarmRequired + ", neStatusList=" + neStatusList + ", alarmCategory="
				+ alarmCategory + ", nELat=" + nELat + ", nELng=" + nELng + ", sWLat=" + sWLat + ", sWLng=" + sWLng
				+ ", geographyLevel=" + geographyLevel + "]";
	}
	
}
