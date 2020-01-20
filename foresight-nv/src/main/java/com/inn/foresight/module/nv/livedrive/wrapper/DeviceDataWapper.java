package com.inn.foresight.module.nv.livedrive.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class DeviceDataWapper {
	private Integer oldCellId;
	private Integer cellId;
	private String status;
	private Integer pci;
	private Integer oldPci;

	private String imei;
	private String scriptName;
	private String location;

	public DeviceDataWapper() {
		super();
	}

	public DeviceDataWapper(Integer oldCellId, Integer cellId, String status, Integer oldPci, Integer pci) {
		super();
		this.oldCellId = oldCellId;
		this.cellId = cellId;
		this.status = status;
		this.oldPci = oldPci;
		this.pci = pci;
	}


	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getPci() {
		return pci;
	}

	public void setPci(Integer pci) {
		this.pci = pci;
	}

	public Integer getOldPci() {
		return oldPci;
	}

	public void setOldPci(Integer oldPci) {
		this.oldPci = oldPci;
	}

	public Integer getOldCellId() {
		return oldCellId;
	}

	public void setOldCellId(Integer oldCellId) {
		this.oldCellId = oldCellId;
	}

	public Integer getCellId() {
		return cellId;
	}

	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "DeviceDataWapper [oldCellId=" + oldCellId + ", cellId=" + cellId + ", status=" + status + ", pci=" + pci
				+ ", oldPci=" + oldPci + ", imei=" + imei + ", scriptName=" + scriptName + ", location=" + location
				+ "]";
	}
	

}
