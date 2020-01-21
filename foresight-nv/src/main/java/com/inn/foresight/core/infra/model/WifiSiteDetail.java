package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQueries({
	@NamedQuery(name = "getWifiDetailByNEId", query = "select new com.inn.foresight.core.infra.wrapper.WifiWrapper(w.networkElement.neId, w.networkElement.neName,w.port,w.ip,w.channel,w.serial,w.networkElement.neStatus,w.networkElement.latitude,w.networkElement.longitude,w.address) from WifiSiteDetail w where w.networkElement.neId=:neId"),
})
/**
 * The Class WifiSiteDetail.
 */
@Entity
@Table(name = "WIFISiteDetail")
@XmlRootElement(name = "WIFISiteDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class WifiSiteDetail implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1642137271943536020L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "wifisitedetailid_pk")
	private Integer id;

	/** The network element. */
	@JoinColumn(name = "networkelementid_fk", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;

	/** The rj network entity id. */
	@Basic
	@Column(name = "rjnetworkentityid")
	private String rjNetworkEntityId;

	/** The address. */
	@Basic
	@Column(name = "address")
	private String address;

	/** The site location. */
	@Basic
	@Column(name = "sitelocation")
	private String siteLocation;

	/** The decommission date. */
	@Basic
	@Column(name = "decommissiondate")
	private Date decommissionDate;

	/** The ems live date. */
	@Basic
	@Column(name = "emslivedate")
	private Date emsLiveDate;

	/** The ems live counter. */
	@Basic
	@Column(name = "emslivecounter")
	private Long emsLiveCounter;

	/** The buidling RJID. */
	@Basic
	@Column(name = "buildingrjid")
	private String buildingRJID;

	/** The ems I pv 6 address. */
	@Basic
	@Column(name = "emsipv6address")
	private String emsIPv6Address;

	/** The ems hostname. */
	@Basic
	@Column(name = "emshostname")
	private String emsHostname;

	/** The equipment status. */
	@Basic
	@Column(name = "equipmentstatus")
	private String equipmentStatus;

	/** The onair AP count. */
	@Basic
	@Column(name = "onairapcount")
	private Long onairAPCount = 0L;

	/** The planned AP count. */
	@Basic
	@Column(name = "plannedapcount")
	private Long plannedAPCount = 0L;

	/** The non rad AP count. */
	@Basic
	@Column(name = "nonradapcount")
	private Long nonRadAPCount = 0L;

	/** The decommissioned AP count. */
	@Basic
	@Column(name = "decommissionedapcount")
	private Long decommissionedAPCount = 0L;

	/** The building name. */
	@Basic
	@Column(name = "buildingname")
	private String buildingName;
	
	@Basic
	@Column(name = "ip")
	private String ip;
	
	@Basic
	@Column(name = "port")
	private String port;
	
	@Basic
	@Column(name = "channel")
	private String channel;
	
	@Basic
	@Column(name = "serial")
	private String serial;
	
	@Basic
	@Column(name = "externalip")
	private String externalIp;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the network element.
	 *
	 * @return the network element
	 */
	public NetworkElement getNetworkElement() {
		return networkElement;
	}

	/**
	 * Sets the network element.
	 *
	 * @param networkElement the new network element
	 */
	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}

	/**
	 * Gets the rj network entity id.
	 *
	 * @return the rj network entity id
	 */
	public String getRjNetworkEntityId() {
		return rjNetworkEntityId;
	}

	/**
	 * Sets the rj network entity id.
	 *
	 * @param rjNetworkEntityId the new rj network entity id
	 */
	public void setRjNetworkEntityId(String rjNetworkEntityId) {
		this.rjNetworkEntityId = rjNetworkEntityId;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the site location.
	 *
	 * @return the site location
	 */
	public String getSiteLocation() {
		return siteLocation;
	}

	/**
	 * Sets the site location.
	 *
	 * @param siteLocation the new site location
	 */
	public void setSiteLocation(String siteLocation) {
		this.siteLocation = siteLocation;
	}

	/**
	 * Gets the decommission date.
	 *
	 * @return the decommission date
	 */
	public Date getDecommissionDate() {
		return decommissionDate;
	}

	/**
	 * Sets the decommission date.
	 *
	 * @param decommissionDate the new decommission date
	 */
	public void setDecommissionDate(Date decommissionDate) {
		this.decommissionDate = decommissionDate;
	}

	/**
	 * Gets the ems live date.
	 *
	 * @return the ems live date
	 */
	public Date getEmsLiveDate() {
		return emsLiveDate;
	}

	/**
	 * Sets the ems live date.
	 *
	 * @param emsLiveDate the new ems live date
	 */
	public void setEmsLiveDate(Date emsLiveDate) {
		this.emsLiveDate = emsLiveDate;
	}

	/**
	 * Gets the ems live counter.
	 *
	 * @return the ems live counter
	 */
	public Long getEmsLiveCounter() {
		return emsLiveCounter;
	}

	/**
	 * Sets the ems live counter.
	 *
	 * @param emsLiveCounter the new ems live counter
	 */
	public void setEmsLiveCounter(Long emsLiveCounter) {
		this.emsLiveCounter = emsLiveCounter;
	}

	/**
	 * Gets the ems I pv 6 address.
	 *
	 * @return the ems I pv 6 address
	 */
	public String getEmsIPv6Address() {
		return emsIPv6Address;
	}

	/**
	 * Sets the ems I pv 6 address.
	 *
	 * @param emsIPv6Address the new ems I pv 6 address
	 */
	public void setEmsIPv6Address(String emsIPv6Address) {
		this.emsIPv6Address = emsIPv6Address;
	}

	/**
	 * Gets the ems hostname.
	 *
	 * @return the ems hostname
	 */
	public String getEmsHostname() {
		return emsHostname;
	}

	/**
	 * Sets the ems hostname.
	 *
	 * @param emsHostname the new ems hostname
	 */
	public void setEmsHostname(String emsHostname) {
		this.emsHostname = emsHostname;
	}

	/**
	 * Gets the equipment status.
	 *
	 * @return the equipment status
	 */
	public String getEquipmentStatus() {
		return equipmentStatus;
	}

	/**
	 * Sets the equipment status.
	 *
	 * @param equipmentStatus the new equipment status
	 */
	public void setEquipmentStatus(String equipmentStatus) {
		this.equipmentStatus = equipmentStatus;
	}

	/**
	 * Gets the onair AP count.
	 *
	 * @return the onair AP count
	 */
	public Long getOnairAPCount() {
		return onairAPCount;
	}

	/**
	 * Sets the onair AP count.
	 *
	 * @param onairAPCount the new onair AP count
	 */
	public void setOnairAPCount(Long onairAPCount) {
		this.onairAPCount = onairAPCount;
	}

	/**
	 * Gets the planned AP count.
	 *
	 * @return the planned AP count
	 */
	public Long getPlannedAPCount() {
		return plannedAPCount;
	}

	/**
	 * Sets the planned AP count.
	 *
	 * @param plannedAPCount the new planned AP count
	 */
	public void setPlannedAPCount(Long plannedAPCount) {
		this.plannedAPCount = plannedAPCount;
	}

	/**
	 * Gets the non rad AP count.
	 *
	 * @return the non rad AP count
	 */
	public Long getNonRadAPCount() {
		return nonRadAPCount;
	}

	/**
	 * Sets the non rad AP count.
	 *
	 * @param nonRadAPCount the new non rad AP count
	 */
	public void setNonRadAPCount(Long nonRadAPCount) {
		this.nonRadAPCount = nonRadAPCount;
	}

	/**
	 * Gets the decommissioned AP count.
	 *
	 * @return the decommissioned AP count
	 */
	public Long getDecommissionedAPCount() {
		return decommissionedAPCount;
	}

	/**
	 * Sets the decommissioned AP count.
	 *
	 * @param decommissionedAPCount the new decommissioned AP count
	 */
	public void setDecommissionedAPCount(Long decommissionedAPCount) {
		this.decommissionedAPCount = decommissionedAPCount;
	}

	/**
	 * Gets the building name.
	 *
	 * @return the building name
	 */
	public String getBuildingName() {
		return buildingName;
	}

	/**
	 * Sets the building name.
	 *
	 * @param buildingName the new building name
	 */
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getExternalIp() {
		return externalIp;
	}

	public void setExternalIp(String externalIp) {
		this.externalIp = externalIp;
	}

	public String getBuildingRJID() {
		return buildingRJID;
	}

	public void setBuildingRJID(String buildingRJID) {
		this.buildingRJID = buildingRJID;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WifiSiteDetail [id=");
		builder.append(id);
		builder.append(", networkElement=");
		builder.append(networkElement);
		builder.append(", rjNetworkEntityId=");
		builder.append(rjNetworkEntityId);
		builder.append(", address=");
		builder.append(address);
		builder.append(", siteLocation=");
		builder.append(siteLocation);
		builder.append(", decommissionDate=");
		builder.append(decommissionDate);
		builder.append(", emsLiveDate=");
		builder.append(emsLiveDate);
		builder.append(", emsLiveCounter=");
		builder.append(emsLiveCounter);
		builder.append(", buildingRJID=");
		builder.append(buildingRJID);
		builder.append(", emsIPv6Address=");
		builder.append(emsIPv6Address);
		builder.append(", emsHostname=");
		builder.append(emsHostname);
		builder.append(", equipmentStatus=");
		builder.append(equipmentStatus);
		builder.append(", onairAPCount=");
		builder.append(onairAPCount);
		builder.append(", plannedAPCount=");
		builder.append(plannedAPCount);
		builder.append(", nonRadAPCount=");
		builder.append(nonRadAPCount);
		builder.append(", decommissionedAPCount=");
		builder.append(decommissionedAPCount);
		builder.append(", buildingName=");
		builder.append(buildingName);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", port=");
		builder.append(port);
		builder.append(", channel=");
		builder.append(channel);
		builder.append(", serial=");
		builder.append(serial);
		builder.append(", externalIp=");
		builder.append(externalIp);
		builder.append("]");
		return builder.toString();
	}

}
