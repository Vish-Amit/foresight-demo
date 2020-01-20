package com.inn.foresight.module.nv.coverage.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CellDetailWrapper {
	
	private Integer azimuth;
	private Integer cgi;
	private Integer pci;
	private String neStatus;
	private Long modificationTime;
	private Integer cellNum;
	private String activeUsers;
	private String neId;
    private String alarmCategory;
    private Long startTime;
    private Integer sector;
    private String neName;
    private String adminState;
    private Boolean isPlannedOutage=false;
	
	
	
	public String getNeName() {
		return neName;
	}
	public void setNeName(String neName) {
		this.neName = neName;
	}
	public String getAdminState() {
		return adminState;
	}
	public void setAdminState(String adminState) {
		this.adminState = adminState;
	}
	public Integer getSector() {
		return sector;
	}
	public void setSector(Integer sector) {
		this.sector = sector;
	}
	public String getAlarmCategory() {
		return alarmCategory;
	}
	public void setAlarmCategory(String alarmCategory) {
		this.alarmCategory = alarmCategory;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public String getNeId() {
		return neId;
	}
	public void setNeId(String neId) {
		this.neId = neId;
	}
	public String getActiveUsers() {
		return activeUsers;
	}
	public void setActiveUsers(String activeUsers) {
		this.activeUsers = activeUsers;
	}
	public Integer getCellNum() {
		return cellNum;
	}
	public void setCellNum(Integer cellNum) {
		this.cellNum = cellNum;
	}
	public Integer getAzimuth() {
		return azimuth;
	}
	public void setAzimuth(Integer azimuth) {
		this.azimuth = azimuth;
	}
	public Integer getCgi() {
		return cgi;
	}
	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}
	public Integer getPci() {
		return pci;
	}
	public void setPci(Integer pci) {
		this.pci = pci;
	}
	public String getNeStatus() {
		return neStatus;
	}
	public void setNeStatus(String neStatus) {
		this.neStatus = neStatus;
	}
	public Long getModificationTime() {
		return modificationTime;
	}
	public void setModificationTime(Long modificationTime) {
		this.modificationTime = modificationTime;
	}
	public Boolean getIsPlannedOutage() {
		return isPlannedOutage;
	}
	public void setIsPlannedOutage(Boolean isPlannedOutage) {
		this.isPlannedOutage = isPlannedOutage;
	}

	@Override
	public String toString() {
		return "CellDetailWrapper [azimuth=" + azimuth + ", cgi=" + cgi + ", pci=" + pci + ", neStatus=" + neStatus
				+ ", modificationTime=" + modificationTime + ", cellNum=" + cellNum + ", activeUsers=" + activeUsers
				+ ", neId=" + neId + ", alarmCategory=" + alarmCategory + ", startTime=" + startTime + ", sector="
				+ sector + ", adminState=" + adminState + ", isPlannedOutage=" + isPlannedOutage + "]";
	}

	
	

}
