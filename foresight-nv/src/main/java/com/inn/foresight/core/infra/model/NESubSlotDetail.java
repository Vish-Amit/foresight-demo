package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@XmlRootElement(name = "NESubSlotDetail")
@Entity
@Table(name = "NESubSlotDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NESubSlotDetail implements Serializable{

	private static final long serialVersionUID = -7488881282702277975L;


	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nesubslotdetailid_pk")
	private Integer id;

	/** The ne location. */
	@JoinColumn(name = "neslotdetailid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NESlotDetail neSlotDetail;
	
	@Column(name = "deleted")
	private Boolean isDeleted;

	@Column(name = "creationtime")
	private Date creationTime;

	@Column(name = "modificationtime")
	private Date modificationTime;

	@Column(name = "oid")
	private String oid;

	@Column(name = "slotname")
	private String slotName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "poid")
	private String poid;
	
	@Column(name = "avstatus")
	private String avStatus;

	@Column(name = "opstatus")
	private String opStatus;
	
	@Column(name="subslotportnumber")
	private String subSlotPortNumber;
	
	@Column(name="subslottype")
	private String subSlotType;
	
	@Column(name="subslotnumber")
	private String subSlotNumber;
	
	@Column(name = "subboardName")
	private String subBoardName;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the neSlotDetail
	 */
	public NESlotDetail getNeSlotDetail() {
		return neSlotDetail;
	}

	/**
	 * @param neSlotDetail the neSlotDetail to set
	 */
	public void setNeSlotDetail(NESlotDetail neSlotDetail) {
		this.neSlotDetail = neSlotDetail;
	}

	/**
	 * @return the isDeleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the modificationTime
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * @param modificationTime the modificationTime to set
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * @return the oid
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * @param oid the oid to set
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * @return the slotName
	 */
	public String getSlotName() {
		return slotName;
	}

	/**
	 * @param slotName the slotName to set
	 */
	public void setSlotName(String slotName) {
		this.slotName = slotName;
	}

	

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the poid
	 */
	public String getPoid() {
		return poid;
	}

	/**
	 * @param poid the poid to set
	 */
	public void setPoid(String poid) {
		this.poid = poid;
	}

	/**
	 * @return the avStatus
	 */
	public String getAvStatus() {
		return avStatus;
	}

	/**
	 * @param avStatus the avStatus to set
	 */
	public void setAvStatus(String avStatus) {
		this.avStatus = avStatus;
	}

	/**
	 * @return the opStatus
	 */
	public String getOpStatus() {
		return opStatus;
	}

	/**
	 * @param opStatus the opStatus to set
	 */
	public void setOpStatus(String opStatus) {
		this.opStatus = opStatus;
	}

	/**
	 * @return the subSlotPortNumber
	 */
	public String getSubSlotPortNumber() {
		return subSlotPortNumber;
	}

	/**
	 * @param subSlotPortNumber the subSlotPortNumber to set
	 */
	public void setSubSlotPortNumber(String subSlotPortNumber) {
		this.subSlotPortNumber = subSlotPortNumber;
	}

	/**
	 * @return the subSlotType
	 */
	public String getSubSlotType() {
		return subSlotType;
	}

	/**
	 * @param subSlotType the subSlotType to set
	 */
	public void setSubSlotType(String subSlotType) {
		this.subSlotType = subSlotType;
	}

	/**
	 * @return the subSlotNumber
	 */
	public String getSubSlotNumber() {
		return subSlotNumber;
	}

	/**
	 * @param subSlotNumber the subSlotNumber to set
	 */
	public void setSubSlotNumber(String subSlotNumber) {
		this.subSlotNumber = subSlotNumber;
	}

	/**
	 * @return the subBoardName
	 */
	public String getSubBoardName() {
		return subBoardName;
	}

	/**
	 * @param subBoardName the subBoardName to set
	 */
	public void setSubBoardName(String subBoardName) {
		this.subBoardName = subBoardName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NESubSlotDetail [id=" + id + ", neSlotDetail=" + neSlotDetail + ", isDeleted=" + isDeleted
				+ ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + ", oid=" + oid
				+ ", slotName=" + slotName + ", description=" + description + ", poid=" + poid + ", avStatus="
				+ avStatus + ", opStatus=" + opStatus + ", subSlotPortNumber=" + subSlotPortNumber + ", subSlotType="
				+ subSlotType + ", subSlotNumber=" + subSlotNumber + ", subBoardName=" + subBoardName + "]";
	}


	
}
