package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQueries({
	@NamedQuery(name = "getRANEquipmentDetailByNEName", query = "select new com.inn.foresight.core.infra.wrapper.RANEquipmentDetailWrapper(re.name,re.racknumber,re.shelfnumber,re.slotnumber,re.position,re.equipmenttype,re.model,re.serialnumber,re.manufacturer,re.iddescription,re.description,re.enodbname) from RANEquipmentDetail re where re.networkElement.id=:neId and re.deleted=0 "),
})


@XmlRootElement(name = "RANEquipmentDetail")
@Entity
@Table(name = "RANEquipmentDetail")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class RANEquipmentDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1171713735586393067L;
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "ranequipmentdetailid_pk")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "racknumber")
	private Integer racknumber;
	
	@Column(name = "shelfnumber")
	private Integer shelfnumber;
	
	@Column(name = "slotnumber")
	private Integer slotnumber;
	
	@Column(name = "position")
	private String position;
	
	@Column(name = "equipmenttype")
	private String equipmenttype;
	
	@Column(name = "model")
	private String model;
	
	@Column(name = "serialnumber")
	private String serialnumber;
	
	@Column(name = "manufacturer")
	private String manufacturer;
	
	@Column(name = "iddescription")
	private String iddescription;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "enodbname")
	private String enodbname;
	
	@Column(name = "deleted")
	private Boolean deleted;
	
	/** The network element. */
	@JoinColumn(name = "networkelementid_fk", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;
	
	@Column(name = "creationtime")
	private Date createdTime;

	@Column(name = "modificationtime")
	private Date modifiedTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public NetworkElement getNetworkElement() {
		return networkElement;
	}

	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
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

	@Override
	public String toString() {
		return "RANEquipmentDetail [id=" + id + ", name=" + name + ", racknumber=" + racknumber + ", shelfnumber="
				+ shelfnumber + ", slotnumber=" + slotnumber + ", position=" + position + ", equipmenttype="
				+ equipmenttype + ", model=" + model + ", serialnumber=" + serialnumber + ", manufacturer="
				+ manufacturer + ", iddescription=" + iddescription + ", description=" + description + ", enodbname="
				+ enodbname + ", deleted=" + deleted + ", networkElement=" + networkElement + ", createdTime="
				+ createdTime + ", modifiedTime=" + modifiedTime + "]";
	}
	
}
