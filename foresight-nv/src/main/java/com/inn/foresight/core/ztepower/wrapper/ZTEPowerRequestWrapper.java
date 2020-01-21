package com.inn.foresight.core.ztepower.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;
@RestWrapper
public class ZTEPowerRequestWrapper {
	private String type;
	private List<String>areaName;
	private List<String>stationName;
	private List<String>deviceName;
	private List<String>meteName;
	private List<String>areaId;
	private List<String>stationId;
	private List<String>deviceId;
	private List<String>meteId;
	private List<String>meteKind;
	private List<String>lscId;
	private List<String>lscName;
	private List<String>geographyL4;
	private List<String>geographyL3;
	private List<String>geographyL2;
	private List<String>geographyL1;
	private Integer limit;
	private Integer offset;
	private Boolean isCount=Boolean.FALSE;
	private String name;
	private Boolean isForDescripency=Boolean.FALSE;
	private List<Integer>idList;
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the areaName
	 */
	public List<String> getAreaName() {
		return areaName;
	}

	/**
	 * @param areaName the areaName to set
	 */
	public void setAreaName(List<String> areaName) {
		this.areaName = areaName;
	}

	/**
	 * @return the stationName
	 */
	public List<String> getStationName() {
		return stationName;
	}

	/**
	 * @param stationName the stationName to set
	 */
	public void setStationName(List<String> stationName) {
		this.stationName = stationName;
	}

	/**
	 * @return the deviceName
	 */
	public List<String> getDeviceName() {
		return deviceName;
	}

	/**
	 * @param deviceName the deviceName to set
	 */
	public void setDeviceName(List<String> deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the meteName
	 */
	public List<String> getMeteName() {
		return meteName;
	}

	/**
	 * @param meteName the meteName to set
	 */
	public void setMeteName(List<String> meteName) {
		this.meteName = meteName;
	}

	/**
	 * @return the geographyL4
	 */
	public List<String> getGeographyL4() {
		return geographyL4;
	}

	/**
	 * @param geographyL4 the geographyL4 to set
	 */
	public void setGeographyL4(List<String> geographyL4) {
		this.geographyL4 = geographyL4;
	}

	/**
	 * @return the geographyL3
	 */
	public List<String> getGeographyL3() {
		return geographyL3;
	}

	/**
	 * @param geographyL3 the geographyL3 to set
	 */
	public void setGeographyL3(List<String> geographyL3) {
		this.geographyL3 = geographyL3;
	}

	/**
	 * @return the geographyL2
	 */
	public List<String> getGeographyL2() {
		return geographyL2;
	}

	/**
	 * @param geographyL2 the geographyL2 to set
	 */
	public void setGeographyL2(List<String> geographyL2) {
		this.geographyL2 = geographyL2;
	}

	/**
	 * @return the geographyL1
	 */
	public List<String> getGeographyL1() {
		return geographyL1;
	}

	/**
	 * @param geographyL1 the geographyL1 to set
	 */
	public void setGeographyL1(List<String> geographyL1) {
		this.geographyL1 = geographyL1;
	}

	/**
	 * @return the limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * @return the offset
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * @return the isCount
	 */
	public Boolean getIsCount() {
		return isCount;
	}

	/**
	 * @param isCount the isCount to set
	 */
	public void setIsCount(Boolean isCount) {
		this.isCount = isCount;
	}

	/**
	 * @return the areaId
	 */
	public List<String> getAreaId() {
		return areaId;
	}

	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(List<String> areaId) {
		this.areaId = areaId;
	}

	/**
	 * @return the stationId
	 */
	public List<String> getStationId() {
		return stationId;
	}

	/**
	 * @param stationId the stationId to set
	 */
	public void setStationId(List<String> stationId) {
		this.stationId = stationId;
	}

	/**
	 * @return the deviceId
	 */
	public List<String> getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(List<String> deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the meteId
	 */
	public List<String> getMeteId() {
		return meteId;
	}

	/**
	 * @param meteId the meteId to set
	 */
	public void setMeteId(List<String> meteId) {
		this.meteId = meteId;
	}

	/**
	 * @return the meteKind
	 */
	public List<String> getMeteKind() {
		return meteKind;
	}

	/**
	 * @param meteKind the meteKind to set
	 */
	public void setMeteKind(List<String> meteKind) {
		this.meteKind = meteKind;
	}

	/**
	 * @return the lscId
	 */
	public List<String> getLscId() {
		return lscId;
	}

	/**
	 * @param lscId the lscId to set
	 */
	public void setLscId(List<String> lscId) {
		this.lscId = lscId;
	}

	/**
	 * @return the lscName
	 */
	public List<String> getLscName() {
		return lscName;
	}

	/**
	 * @param lscName the lscName to set
	 */
	public void setLscName(List<String> lscName) {
		this.lscName = lscName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isForDescripency
	 */
	public Boolean getIsForDescripency() {
		return isForDescripency;
	}

	/**
	 * @param isForDescripency the isForDescripency to set
	 */
	public void setIsForDescripency(Boolean isForDescripency) {
		this.isForDescripency = isForDescripency;
	}

	/**
	 * @return the idList
	 */
	public List<Integer> getIdList() {
		return idList;
	}

	/**
	 * @param idList the idList to set
	 */
	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}
	
	}