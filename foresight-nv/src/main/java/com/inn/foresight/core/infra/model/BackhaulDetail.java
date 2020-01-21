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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class BackhaulDetail.
 */ 


@NamedQueries({
	@NamedQuery(name = "getHubSiteDetailByNeName", query="select new com.inn.foresight.core.infra.wrapper.BackHaulDetailWrapper(b.hubSiteName,b.neType,b.connectedenb,b.deviceid,b.devicefamily,b.commstate,b.state,b.systemoid,b.netmask) from BackhaulDetail b  where b.networkElement.neName=:neName and b.deleted=0"),
	@NamedQuery(name = "getDeviceLocationByNeName", query="select b.deviceLocation from BackhaulDetail b  where b.networkElement.neName=:neName and b.deleted=0"),
	@NamedQuery(name = "getNECapacityByNEName", query="select new com.inn.foresight.core.infra.wrapper.BackHaulDetailWrapper(b.networkElement.neName,b.capacity) from BackhaulDetail b  where b.networkElement.neName in(:neName) and b.deleted=0"),
	@NamedQuery(name = "getDeviceLocationByNeNameList", query="select b.networkElement.neName, b.deviceLocation, b.networkElement.geographyL4.geographyL3.name from BackhaulDetail b  where b.networkElement.domain =:domain and b.networkElement.vendor=:vendor and b.networkElement.neName in (:neName) and b.deleted=0"),
	@NamedQuery(name = "getBackhaulDetailByNeName", query="select b from BackhaulDetail b  where b.networkElement.neName in(:neName) and b.deleted=0"),
	
	@NamedQuery(name = "getHopTypeByDomainVendor", query="select b.networkElement.id, b.hopType, b.networkElement.geographyL4.geographyL3.name,b.networkElement.geographyL4.geographyL3.geographyL2.name from BackhaulDetail b  where b.networkElement.domain =:domain and b.networkElement.vendor=:vendor and b.hopType in (:hopType) and b.deleted=0"),
	
})

@Entity
@XmlRootElement(name = "BackhaulDetail")
@Table(name = "BackhaulDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class BackhaulDetail implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 786573855338056269L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "backhauldetailid_pk")
	private Integer id;

	/** The networkElement detail. */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "networkelementid_fk")
	private NetworkElement networkElement;

	/** The netmask name. */
	@Basic
	@Column(name = "netmask")
	private String netmask;

	/** The system oid */
	@Basic
	@Column(name = "systemoid")
	private String systemoid;

	/** The state */
	@Basic
	@Column(name = "state")
	private String state;

	/** The comm state. */
	@Basic
	@Column(name = "commstate")
	private String commstate;

	/** The device family. */
	@Basic
	@Column(name = "devicefamily")
	private String devicefamily;

	/** The serial number. */
	@Basic
	@Column(name = "serialnumber")
	private String serialnumber;

	/** The device id. */
	@Basic
	@Column(name = "deviceid")
	private String deviceid;

	/** The config status. */
	@Basic
	@Column(name = "configstatus")
	private String configstatus;

	/** The connected link count. */
	@Basic
	@Column(name = "connectedlinkcount")
	private Integer connectedlinkcount;

	/** The last rechable time. */
	@Basic
	@Column(name = "lastrechabletime")
	private Date lastrechabletime;

	/** The connected enb. */
	@Basic
	@Column(name = "connectedenb")
	private String connectedenb;

	/** The creation time. */
	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The is deleted. */
	@Basic
	@Column(name = "deleted")
	private Boolean deleted;
	
	@Basic
	@Column(name = "netype")
	private String neType;
	
	
	@Column(name ="hubsitename")
	private String hubSiteName;
	
	@Column(name ="hoptype")
	private String hopType;
	
	@Column(name ="ips")
	private String ips;
	
	@Column(name ="usedport")
	private String usedPort;
	
	@Column(name ="nearendsite")
	private String nearEndSite;
	
	@Column(name ="farendsite")
	private String farEndSite;
	
	@Basic
	@Column(name = "devicelocation")
	private String deviceLocation;
	
	@Basic
	@Column(name = "capacity")
	private Double capacity;
	
	@Basic
	@Column(name = "capacityunit")
	private String capacityUnit;
	
	@Basic
	@Column(name = "capacitymodificationtime")
	private Date capacityModificationTime;
	
	@Basic
	@Column(name = "communicationid")
	private String communicationId;
	
	@Column(name = "capacityjson")
	@Lob
	private String capacityJson;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NetworkElement getNetworkElement() {
		return networkElement;
	}

	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getSystemoid() {
		return systemoid;
	}

	public void setSystemoid(String systemoid) {
		this.systemoid = systemoid;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCommstate() {
		return commstate;
	}

	public void setCommstate(String commstate) {
		this.commstate = commstate;
	}

	public String getDevicefamily() {
		return devicefamily;
	}

	public void setDevicefamily(String devicefamily) {
		this.devicefamily = devicefamily;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getConfigstatus() {
		return configstatus;
	}

	public void setConfigstatus(String configstatus) {
		this.configstatus = configstatus;
	}

	public Integer getConnectedlinkcount() {
		return connectedlinkcount;
	}

	public void setConnectedlinkcount(Integer connectedlinkcount) {
		this.connectedlinkcount = connectedlinkcount;
	}

	public Date getLastrechabletime() {
		return lastrechabletime;
	}

	public void setLastrechabletime(Date lastrechabletime) {
		this.lastrechabletime = lastrechabletime;
	}

	public String getConnectedenb() {
		return connectedenb;
	}

	public void setConnectedenb(String connectedenb) {
		this.connectedenb = connectedenb;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	

	public String getNeType() {
		return neType;
	}

	public void setNeType(String neType) {
		this.neType = neType;
	}

	
	public String getHubSiteName() {
		return hubSiteName;
	}

	public void setHubSiteName(String hubSiteName) {
		this.hubSiteName = hubSiteName;
	}

	public void setHopType(String hopType) {
		this.hopType = hopType;
	}
	
	public String getHopType() {
		return hopType;
	}

	public String getIps() {
		return ips;
	}

	public void setIps(String ips) {
		this.ips = ips;
	}

	public String getUsedPort() {
		return usedPort;
	}

	public void setUsedPort(String usedPort) {
		this.usedPort = usedPort;
	}

	public String getNearEndSite() {
		return nearEndSite;
	}

	public void setNearEndSite(String nearEndSite) {
		this.nearEndSite = nearEndSite;
	}

	public String getFarEndSite() {
		return farEndSite;
	}

	public void setFarEndSite(String farEndSite) {
		this.farEndSite = farEndSite;
	}
	
	public String getDeviceLocation() {
		return deviceLocation;
	}

	public void setDeviceLocation(String deviceLocation) {
		this.deviceLocation = deviceLocation;
	}

	public Double getCapacity() {
		return capacity;
	}

	public void setCapacity(Double capacity) {
		this.capacity = capacity;
	}

    public String getCapacityUnit() {
        return capacityUnit;
    }

    public void setCapacityUnit(String capacityUnit) {
        this.capacityUnit = capacityUnit;
    }

    public Date getCapacityModificationTime() {
        return capacityModificationTime;
    }

    public void setCapacityModificationTime(Date capacityModificationTime) {
        this.capacityModificationTime = capacityModificationTime;
    }

	public String getCommunicationId() {
		return communicationId;
	}

	public void setCommunicationId(String communicationId) {
		this.communicationId = communicationId;
	}

	public String getCapacityJson() {
		return capacityJson;
	}

	public void setCapacityJson(String capacityJson) {
		this.capacityJson = capacityJson;
	}

	@Override
	public String toString() {
		return "BackhaulDetail [id=" + id + ", networkElement=" + networkElement + ", netmask=" + netmask
				+ ", systemoid=" + systemoid + ", state=" + state + ", commstate=" + commstate + ", devicefamily="
				+ devicefamily + ", serialnumber=" + serialnumber + ", deviceid=" + deviceid + ", configstatus="
				+ configstatus + ", connectedlinkcount=" + connectedlinkcount + ", lastrechabletime=" + lastrechabletime
				+ ", connectedenb=" + connectedenb + ", creationTime=" + creationTime + ", modificationTime="
				+ modificationTime + ", deleted=" + deleted + ", neType=" + neType + ", hubSiteName=" + hubSiteName
				+ ", hopType=" + hopType + ", ips=" + ips + ", usedPort=" + usedPort + ", nearEndSite=" + nearEndSite
				+ ", farEndSite=" + farEndSite + ", deviceLocation=" + deviceLocation + ", capacity=" + capacity
				+ ", capacityUnit=" + capacityUnit + ", capacityModificationTime=" + capacityModificationTime
				+ ", communicationId=" + communicationId + ", capacityJson=" + capacityJson + "]";
	}

}
