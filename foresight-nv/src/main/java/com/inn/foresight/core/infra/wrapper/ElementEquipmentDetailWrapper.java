package com.inn.foresight.core.infra.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper

public class ElementEquipmentDetailWrapper implements Cloneable {

	private Integer generaedId;

	private String name;

	private String subEquipmentName;
	
	private String subSubEquipmentName;
	
	private String subSubSubEquipmentName;
	
	private String position;

	private String racknumber;

	private String shelfnumber;

	private String slotnumber;

	private String elementname;

	private String holdertype;

	private String holderstate;

	private String displayname;

	private String installedequipmenttype;

	private String installedpartnumber;

	private String installedvendorpartnumber;

	private String installedvendormaterialnumber;

	private String installedserialnumber;

	private String manufacturer;

	private String description;

	private String installedversion;

	private Integer generatedParentId;

	private String elementid;
	
	private String installedModel;
	
	private String chassis;
	
	private String status;
	
	public String getSubEquipmentName() {
		return subEquipmentName;
	}

	public void setSubEquipmentName(String subEquipmentName) {
		this.subEquipmentName = subEquipmentName;
	}

	public String getSubSubEquipmentName() {
		return subSubEquipmentName;
	}

	public void setSubSubEquipmentName(String subSubEquipmentName) {
		this.subSubEquipmentName = subSubEquipmentName;
	}

	public String getSubSubSubEquipmentName() {
		return subSubSubEquipmentName;
	}

	public void setSubSubSubEquipmentName(String subSubSubEquipmentName) {
		this.subSubSubEquipmentName = subSubSubEquipmentName;
	}

	public Integer getGeneraedId() {
		return generaedId;
	}

	public void setGeneraedId(Integer generaedId) {
		this.generaedId = generaedId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRacknumber() {
		return racknumber;
	}

	public void setRacknumber(String racknumber) {
		this.racknumber = racknumber;
	}

	public String getShelfnumber() {
		return shelfnumber;
	}

	public void setShelfnumber(String shelfnumber) {
		this.shelfnumber = shelfnumber;
	}

	public String getSlotnumber() {
		return slotnumber;
	}

	public void setSlotnumber(String slotnumber) {
		this.slotnumber = slotnumber;
	}

	public String getElementname() {
		return elementname;
	}

	public void setElementname(String elementname) {
		this.elementname = elementname;
	}

	public String getHoldertype() {
		return holdertype;
	}

	public void setHoldertype(String holdertype) {
		this.holdertype = holdertype;
	}

	public String getHolderstate() {
		return holderstate;
	}

	public void setHolderstate(String holderstate) {
		this.holderstate = holderstate;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getInstalledequipmenttype() {
		return installedequipmenttype;
	}

	public void setInstalledequipmenttype(String installedequipmenttype) {
		this.installedequipmenttype = installedequipmenttype;
	}

	public String getInstalledpartnumber() {
		return installedpartnumber;
	}

	public void setInstalledpartnumber(String installedpartnumber) {
		this.installedpartnumber = installedpartnumber;
	}

	public String getInstalledvendorpartnumber() {
		return installedvendorpartnumber;
	}

	public void setInstalledvendorpartnumber(String installedvendorpartnumber) {
		this.installedvendorpartnumber = installedvendorpartnumber;
	}

	public String getInstalledvendormaterialnumber() {
		return installedvendormaterialnumber;
	}

	public void setInstalledvendormaterialnumber(String installedvendormaterialnumber) {
		this.installedvendormaterialnumber = installedvendormaterialnumber;
	}

	public String getInstalledserialnumber() {
		return installedserialnumber;
	}

	public void setInstalledserialnumber(String installedserialnumber) {
		this.installedserialnumber = installedserialnumber;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstalledversion() {
		return installedversion;
	}

	public void setInstalledversion(String installedversion) {
		this.installedversion = installedversion;
	}

	public Integer getGeneratedParentId() {
		return generatedParentId;
	}

	public void setGeneratedParentId(Integer generatedParentId) {
		this.generatedParentId = generatedParentId;
	}

	public String getElementid() {
		return elementid;
	}

	public void setElementid(String elementid) {
		this.elementid = elementid;
	}

	public ElementEquipmentDetailWrapper() {
		
	}
	
	public String getInstalledModel() {
		return installedModel;
	}

	public void setInstalledModel(String installedModel) {
		this.installedModel = installedModel;
	}

	public String getChassis() {
		return chassis;
	}

	public void setChassis(String chassis) {
		this.chassis = chassis;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ElementEquipmentDetailWrapper(Integer generaedId, String name, String elementname, String holdertype,
			String holderstate, String displayname, String installedequipmenttype, String installedpartnumber,
			String installedvendorpartnumber, String installedvendormaterialnumber, String installedserialnumber,
			String manufacturer, String description, String installedversion, Integer generatedParentId,
			String elementid) {
		super();
		this.generaedId = generaedId;
		this.name = name;
		this.elementname = elementname;
		this.holdertype = holdertype;
		this.holderstate = holderstate;
		this.displayname = displayname;
		this.installedequipmenttype = installedequipmenttype;
		this.installedpartnumber = installedpartnumber;
		this.installedvendorpartnumber = installedvendorpartnumber;
		this.installedvendormaterialnumber = installedvendormaterialnumber;
		this.installedserialnumber = installedserialnumber;
		this.manufacturer = manufacturer;
		this.description = description;
		this.installedversion = installedversion;
		this.generatedParentId = generatedParentId;
		this.elementid = elementid;
	}
	
	public ElementEquipmentDetailWrapper(Integer generaedId, String name, String elementname, String holdertype,
			String holderstate, String displayname, String installedequipmenttype, String installedpartnumber,
			String installedvendorpartnumber, String installedvendormaterialnumber, String installedserialnumber,
			String manufacturer, String description, String installedversion, Integer generatedParentId,
			String elementid,String chassis,String installedModel) {
		super();
		this.generaedId = generaedId;
		this.name = name;
		this.elementname = elementname;
		this.holdertype = holdertype;
		this.holderstate = holderstate;
		this.displayname = displayname;
		this.installedequipmenttype = installedequipmenttype;
		this.installedpartnumber = installedpartnumber;
		this.installedvendorpartnumber = installedvendorpartnumber;
		this.installedvendormaterialnumber = installedvendormaterialnumber;
		this.installedserialnumber = installedserialnumber;
		this.manufacturer = manufacturer;
		this.description = description;
		this.installedversion = installedversion;
		this.generatedParentId = generatedParentId;
		this.elementid = elementid;
		this.chassis = chassis;
		this.installedModel = installedModel;
	}

	public ElementEquipmentDetailWrapper(Integer generaedId, String name, String elementname, String holdertype,
			String holderstate, String displayname, String installedequipmenttype, String installedpartnumber,
			String installedvendorpartnumber, String installedvendormaterialnumber, String installedserialnumber,
			String manufacturer, String description, String installedversion, Integer generatedParentId,
			String elementid,String chassis,String installedModel,String status) {
		super();
		this.generaedId = generaedId;
		this.name = name;
		this.elementname = elementname;
		this.holdertype = holdertype;
		this.holderstate = holderstate;
		this.displayname = displayname;
		this.installedequipmenttype = installedequipmenttype;
		this.installedpartnumber = installedpartnumber;
		this.installedvendorpartnumber = installedvendorpartnumber;
		this.installedvendormaterialnumber = installedvendormaterialnumber;
		this.installedserialnumber = installedserialnumber;
		this.manufacturer = manufacturer;
		this.description = description;
		this.installedversion = installedversion;
		this.generatedParentId = generatedParentId;
		this.elementid = elementid;
		this.chassis = chassis;
		this.installedModel = installedModel;
		this.status = status;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return "ElementEquipmentDetailWrapper [generaedId=" + generaedId + ", name=" + name + ", subEquipmentName="
				+ subEquipmentName + ", subSubEquipmentName=" + subSubEquipmentName + ", subSubSubEquipmentName="
				+ subSubSubEquipmentName + ", position=" + position + ", racknumber=" + racknumber + ", shelfnumber="
				+ shelfnumber + ", slotnumber=" + slotnumber + ", elementname=" + elementname + ", holdertype="
				+ holdertype + ", holderstate=" + holderstate + ", displayname=" + displayname
				+ ", installedequipmenttype=" + installedequipmenttype + ", installedpartnumber=" + installedpartnumber
				+ ", installedvendorpartnumber=" + installedvendorpartnumber + ", installedvendormaterialnumber="
				+ installedvendormaterialnumber + ", installedserialnumber=" + installedserialnumber + ", manufacturer="
				+ manufacturer + ", description=" + description + ", installedversion=" + installedversion
				+ ", generatedParentId=" + generatedParentId + ", elementid=" + elementid + ", installedModel="
				+ installedModel + ", chassis=" + chassis + ", status=" + status + "]";
	}
}
