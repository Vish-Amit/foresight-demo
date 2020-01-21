package com.inn.foresight.core.infra.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class RANEquipmentDetailWrapper {
	
	private String name;
	private Integer racknumber;
	private Integer shelfnumber;
	private Integer slotnumber;
	private String position;
	private String equipmenttype;
	private String model;
	private String serialnumber;
	private String manufacturer;
	private String iddescription;
	private String description;
	private String enodbname;
	private Boolean deleted;
	private Integer networkelementid_fk;

	public RANEquipmentDetailWrapper(String name, Integer racknumber, Integer shelfnumber, Integer slotnumber,
			String position, String equipmenttype, String model, String serialnumber, String manufacturer,
			String iddescription, String description, String enodbname) {
		super();
		this.name = name;
		this.racknumber = racknumber;
		this.shelfnumber = shelfnumber;
		this.slotnumber = slotnumber;
		this.position = position;
		this.equipmenttype = equipmenttype;
		this.model = model;
		this.serialnumber = serialnumber;
		this.manufacturer = manufacturer;
		this.iddescription = iddescription;
		this.description = description;
		this.enodbname = enodbname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRacknumber() {
		return racknumber;
	}

	public void setRacknumber(Integer racknumber) {
		this.racknumber = racknumber;
	}

	public Integer getShelfnumber() {
		return shelfnumber;
	}

	public void setShelfnumber(Integer shelfnumber) {
		this.shelfnumber = shelfnumber;
	}

	public Integer getSlotnumber() {
		return slotnumber;
	}

	public void setSlotnumber(Integer slotnumber) {
		this.slotnumber = slotnumber;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEquipmenttype() {
		return equipmenttype;
	}

	public void setEquipmenttype(String equipmenttype) {
		this.equipmenttype = equipmenttype;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getIddescription() {
		return iddescription;
	}

	public void setIddescription(String iddescription) {
		this.iddescription = iddescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnodbname() {
		return enodbname;
	}

	public void setEnodbname(String enodbname) {
		this.enodbname = enodbname;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Integer getNetworkelementid_fk() {
		return networkelementid_fk;
	}

	public void setNetworkelementid_fk(Integer networkelementid_fk) {
		this.networkelementid_fk = networkelementid_fk;
	}

	@Override
	public String toString() {
		return "RANEquipmentDetailWrapper [name=" + name + ", racknumber=" + racknumber + ", shelfnumber=" + shelfnumber
				+ ", slotnumber=" + slotnumber + ", position=" + position + ", equipmenttype=" + equipmenttype
				+ ", model=" + model + ", serialnumber=" + serialnumber + ", manufacturer=" + manufacturer
				+ ", iddescription=" + iddescription + ", description=" + description + ", enodbname=" + enodbname
				+ ", deleted=" + deleted + ", networkelementid_fk=" + networkelementid_fk + "]";
	}

}
