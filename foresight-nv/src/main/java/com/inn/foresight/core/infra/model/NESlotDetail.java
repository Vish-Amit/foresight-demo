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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQuery(name = "getSlotAndSubSlotDetail", query = "select new com.inn.foresight.core.infra.wrapper.NESlotDetailWrapper(ns.id,ns.rackNumber,ns.shelfNumber,ns.slotDisplay,ns.portNumber,ns.slotNumber,ns.avStatus,ns.opStatus,ns.oid,ns.slotName,ns.boardtype,ns.boardName,nss.id,nss.oid,nss.slotName,nss.subBoardName,nss.description,nss.poid,nss.avStatus,nss.opStatus,nss.subSlotPortNumber,nss.subSlotType,ns.subSlotNumber,nss.subBoardName,ns.networkElement.neName,ns.networkElement.neType,ns.networkElement.ipv4,ns.networkElement.neId,ns.networkElement.model,ns.networkElement.pmEmsId) from NESlotDetail ns left join NESubSlotDetail nss on ns.id=nss.neSlotDetail.id  where ns.networkElement.neName in(:neName) and  ns.networkElement.domain=:domain")
@XmlRootElement(name = "NESlotDetail")
@Entity
@Table(name = "NESlotDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NESlotDetail implements Serializable {
	
	private static final long serialVersionUID = -6175022302399290415L;


	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "neslotdetailid_pk")
	private Integer id;

	/** The ne location. */
	@JoinColumn(name = "networkelementid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;

	@Column(name = "racknumber")
	private String rackNumber;

	@Column(name = "shelfNumber")
	private String shelfNumber;

	@Column(name = "slotdisplay")
	private String slotDisplay;

	@Column(name = "portnumber")
	private String portNumber;

	@Column(name = "slotnumber")
	private String slotNumber;

	@Column(name = "avstatus")
	private String avStatus;

	@Column(name = "opstatus")
	private String opStatus;

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

	@Column(name = "boardtype")
	private String boardtype;
	
	@Column(name = "boardname")
	private String boardName;
	
	@Column(name="subslotnumber")
	private String subSlotNumber;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the networkElement
	 */
	public NetworkElement getNetworkElement() {
		return networkElement;
	}

	/**
	 * @param networkElement
	 *            the networkElement to set
	 */
	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}

	/**
	 * @return the rackNumber
	 */
	public String getRackNumber() {
		return rackNumber;
	}

	/**
	 * @param rackNumber
	 *            the rackNumber to set
	 */
	public void setRackNumber(String rackNumber) {
		this.rackNumber = rackNumber;
	}

	/**
	 * @return the shelfNumber
	 */
	public String getShelfNumber() {
		return shelfNumber;
	}

	/**
	 * @param shelfNumber
	 *            the shelfNumber to set
	 */
	public void setShelfNumber(String shelfNumber) {
		this.shelfNumber = shelfNumber;
	}

	/**
	 * @return the slotDisplay
	 */
	public String getSlotDisplay() {
		return slotDisplay;
	}

	/**
	 * @param slotDisplay
	 *            the slotDisplay to set
	 */
	public void setSlotDisplay(String slotDisplay) {
		this.slotDisplay = slotDisplay;
	}

	/**
	 * @return the portNumber
	 */
	public String getPortNumber() {
		return portNumber;
	}

	/**
	 * @param portNumber
	 *            the portNumber to set
	 */
	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	/**
	 * @return the slotNumber
	 */
	public String getSlotNumber() {
		return slotNumber;
	}

	/**
	 * @param slotNumber
	 *            the slotNumber to set
	 */
	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

	/**
	 * @return the avStatus
	 */
	public String getAvStatus() {
		return avStatus;
	}

	/**
	 * @param avStatus
	 *            the avStatus to set
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
	 * @param opStatus
	 *            the opStatus to set
	 */
	public void setOpStatus(String opStatus) {
		this.opStatus = opStatus;
	}

	/**
	 * @return the isDeleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted
	 *            the isDeleted to set
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
	 * @param creationTime
	 *            the creationTime to set
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
	 * @param modificationTime
	 *            the modificationTime to set
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
	 * @param oid
	 *            the oid to set
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
	 * @param slotName
	 *            the slotName to set
	 */
	public void setSlotName(String slotName) {
		this.slotName = slotName;
	}

	/**
	 * @return the boardtype
	 */
	public String getBoardtype() {
		return boardtype;
	}

	/**
	 * @param boardtype
	 *            the boardtype to set
	 */
	public void setBoardtype(String boardtype) {
		this.boardtype = boardtype;
	}

	/**
	 * @return the boardName
	 */
	public String getBoardName() {
		return boardName;
	}

	/**
	 * @param boardName the boardName to set
	 */
	public void setBoardName(String boardName) {
		this.boardName = boardName;
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


	@Override
	public String toString() {
		return "NESlotDetail [id=" + id + ", networkElement=" + networkElement + ", rackNumber=" + rackNumber
				+ ", shelfNumber=" + shelfNumber + ", slotDisplay=" + slotDisplay + ", portNumber=" + portNumber
				+ ", slotNumber=" + slotNumber + ", avStatus=" + avStatus + ", opStatus=" + opStatus + ", isDeleted="
				+ isDeleted + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + ", oid="
				+ oid + ", slotName=" + slotName + ", boardtype=" + boardtype + ", boardName=" + boardName
				+ ", subSlotNumber=" + subSlotNumber + "]";
	}

}
