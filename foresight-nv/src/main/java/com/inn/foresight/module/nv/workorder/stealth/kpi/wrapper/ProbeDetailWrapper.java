package com.inn.foresight.module.nv.workorder.stealth.kpi.wrapper;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class ProbeDetailWrapper {

	
	private Integer stealthTaskResultId;
	private Double rsrp;

	private Double sinr;

	private Integer pci;

	private Integer cellId;

	private Integer enodeBId;

	private Integer cgi;

	private String kpi;
	private Double value;
	
	private Long  timeStamp;

	private String address;

	private Double latitude;

	private Double longitude;
	
	

	public Integer getStealthTaskResultId() {
		return stealthTaskResultId;
	}

	public void setStealthTaskResultId(Integer stealthTaskResultId) {
		this.stealthTaskResultId = stealthTaskResultId;
	}

	public String getKpi() {
		return kpi;
	}

	public void setKpi(String kpi) {
		this.kpi = kpi;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getRsrp() {
		return rsrp;
	}

	public void setRsrp(Double rsrp) {
		this.rsrp = rsrp;
	}

	public Double getSinr() {
		return sinr;
	}

	public void setSinr(Double sinr) {
		this.sinr = sinr;
	}

	public Integer getPci() {
		return pci;
	}

	public void setPci(Integer pci) {
		this.pci = pci;
	}

	public Integer getCellId() {
		return cellId;
	}

	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	public Integer getEnodeBId() {
		return enodeBId;
	}

	public void setEnodeBId(Integer enodeBId) {
		this.enodeBId = enodeBId;
	}

	public Integer getCgi() {
		return cgi;
	}

	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}

	
	

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	@Override
	public String toString() {
		return "ProbeDetailWrapper [stealthTaskResultId=" + stealthTaskResultId + ", rsrp=" + rsrp + ", sinr=" + sinr
				+ ", pci=" + pci + ", cellId=" + cellId + ", enodeBId=" + enodeBId + ", cgi=" + cgi + ", kpi=" + kpi
				+ ", value=" + value + ", timeStamp=" + timeStamp + ", address=" + address + ", latitude=" + latitude
				+ ", longitude=" + longitude + "]";
	}

	

	
	

}
