package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.Vendor;



@NamedQueries({
	@NamedQuery(name = "getPhysicalAndLogicalPortsByRouter", query="select pi.physicalInterface,pi.logicalGroup from PortInventory pi where DATE(pi.modifiedTime)=(select DATE(max(modifiedTime)) from PortInventory p where p.networkElement.neId =:neId and p.isDeleted=false)and pi.networkElement.neId =:neId"),
	@NamedQuery(name = "getAllPortsByRouterName", query="select distinct physicalInterface from PortInventory where DATE(modifiedTime)=(select DATE(max(modifiedTime)) from PortInventory p where networkElement.neId =:neId and p.isDeleted=false)and networkElement.neId =:neId"),
	@NamedQuery(name = "getAllActivePortsByRouterName", query ="select distinct physicalInterface from PortInventory where domain=:domain and vendor=:vendor and DATE(modifiedTime)=(select DATE(max(modifiedTime)) from PortInventory p where p.domain=:domain and p.vendor=:vendor and networkElement.neId =:neId and p.isDeleted=false and p.operationStatus='Up' and (physicalInterface like 'ge%' or physicalInterface like 'xe%' or physicalInterface like 'et%' or physicalInterface like 'gei%' or physicalInterface like 'xgei%' or  physicalInterface like 'xlgei%' or  physicalInterface like 'cgei%' or  physicalInterface like 'fei%') ) and networkElement.neId =:neId and (physicalInterface like 'ge%' or physicalInterface like 'xe%' or physicalInterface like 'et%' or physicalInterface like 'gei%' or physicalInterface like 'xgei%' or  physicalInterface like 'xlgei%' or  physicalInterface like 'cgei%' or  physicalInterface like 'fei%') and isDeleted=false and operationStatus='Up' order by physicalInterface"),
	@NamedQuery(name = "getAllActivePortsByRouterNameAndPortType", query ="select distinct physicalInterface from PortInventory where domain=:domain and vendor=:vendor and DATE(modifiedTime)=(select DATE(max(modifiedTime)) from PortInventory p where p.domain=:domain and p.vendor=:vendor and networkElement.neId =:neId and p.isDeleted=false and p.operationStatus='Up' and physicalInterface like :portType) and networkElement.neId =:neId and physicalInterface like :portType and isDeleted=false and operationStatus='Up' order by physicalInterface"),
	@NamedQuery(name = "getAllActivePhysicalPortByRouters", query ="select networkElement.neName, physicalInterface,networkElement.geographyL4.geographyL3.name  from PortInventory where domain=:domain and vendor=:vendor and DATE(modifiedTime)=(select DATE(max(modifiedTime)) from PortInventory p where p.domain=:domain and p.vendor=:vendor and networkElement.neName in (:neName) and p.isDeleted=false and p.operationStatus='Up' and (physicalInterface like 'ge%' or physicalInterface like 'xe%' or physicalInterface like 'et%' or physicalInterface like 'gei%' or physicalInterface like 'xgei%' or  physicalInterface like 'xlgei%' or  physicalInterface like 'cgei%' or  physicalInterface like 'fei%') ) and networkElement.neName in (:neName) and (physicalInterface like 'ge%' or physicalInterface like 'xe%' or physicalInterface like 'et%' or physicalInterface like 'gei%' or physicalInterface like 'xgei%' or  physicalInterface like 'xlgei%' or  physicalInterface like 'cgei%' or  physicalInterface like 'fei%') and isDeleted=false and operationStatus='Up' order by physicalInterface"),
})

@XmlRootElement(name = "PortInventory")
@Entity
@Table(name = "PortInventory")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class PortInventory implements Serializable {
	
	/**
	 * 
	 * 
	 * 
	 */
	private static final long serialVersionUID = 6763183204062191158L;

	/** The id. */
	@Id
	@Column(name = "portinventoryid_pk")
	private Integer id;

	@Column(name = "physicalinterface")
	private String physicalInterface;

	@Column(name = "logicalinterface")
	private String logicalInterface;

	@Column(name = "logicalgroup")
	private String logicalGroup;

	@Column(name = "vlanid")
	private String vlanId;

	@Column(name = "interfacestatus")
	private String interfaceStatus;

	/** The created time. */

	@Column(name = "creationtime")
	private Date createdTime;

	/** The modified time. */

	@Column(name = "modificationtime")
	private Date modifiedTime;

	/** The domain. */
	@Basic
	@Enumerated(EnumType.STRING)
	private Domain domain;

	/** The vendor. */
	@Basic
	@Enumerated(EnumType.STRING)
	private Vendor vendor;

	/** The is deleted. */
	@Basic
	@Column(name = "deleted")
	private Boolean isDeleted;

	/** The type */
	@Basic
	private String type;

	/**
	 * The ip address
	 */
	@Column(name = "ipAddress")
	private String ipaddress;

	/**
	 * The admin status
	 */
	@Column(name = "adminstatus")
	private String adminStatus;

	/**
	 * The operation status
	 */
	@Column(name = "operationstatus")
	private String operationStatus;

	/**
	 * The description
	 */
	@Basic
	private String description;

	/**
	 * The loadsharing bandwidth
	 */
	@Column(name = "loadsharingbandwidth")
	private Double loadsharingBandwidth;

	/**
	 * The loadsharing priority
	 */
	@Column(name = "loadsharingpriority")
	private Integer loadsharingPriority;
	/**
	 * The Network Element
	 */
	@JoinColumn(name = "networkelementid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPhysicalInterface() {
		return physicalInterface;
	}
	public void setPhysicalInterface(String physicalInterface) {
		this.physicalInterface = physicalInterface;
	}
	public String getLogicalInterface() {
		return logicalInterface;
	}
	public void setLogicalInterface(String logicalInterface) {
		this.logicalInterface = logicalInterface;
	}
	public String getLogicalGroup() {
		return logicalGroup;
	}
	public void setLogicalGroup(String logicalGroup) {
		this.logicalGroup = logicalGroup;
	}
	public String getVlanId() {
		return vlanId;
	}
	public void setVlanId(String vlanId) {
		this.vlanId = vlanId;
	}
	public String getInterfaceStatus() {
		return interfaceStatus;
	}
	public void setInterfaceStatus(String interfaceStatus) {
		this.interfaceStatus = interfaceStatus;
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
	public Domain getDomain() {
		return domain;
	}
	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getAdminStatus() {
		return adminStatus;
	}
	public void setAdminStatus(String adminStatus) {
		this.adminStatus = adminStatus;
	}
	public String getOperationStatus() {
		return operationStatus;
	}
	public void setOperationStatus(String operationStatus) {
		this.operationStatus = operationStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getLoadsharingBandwidth() {
		return loadsharingBandwidth;
	}
	public void setLoadsharingBandwidth(Double loadsharingBandwidth) {
		this.loadsharingBandwidth = loadsharingBandwidth;
	}
	public Integer getLoadsharingPriority() {
		return loadsharingPriority;
	}
	public void setLoadsharingPriority(Integer loadsharingPriority) {
		this.loadsharingPriority = loadsharingPriority;
	}
	public NetworkElement getNetworkElement() {
		return networkElement;
	}
	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}
	

	
}
