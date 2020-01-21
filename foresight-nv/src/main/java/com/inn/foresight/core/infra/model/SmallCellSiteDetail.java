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

/**
 * The Class SmallCellSiteDetail.
 */
@NamedQueries({
	@NamedQuery(name = "getSmallSiteBySiteIdSecAndVendor", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(sc.networkElement.id,sc.sector,sc.cellName,sc.networkElement.neName,sc.cellId,sc.networkElement.neFrequency,sc.azimuth,sc.height,sc.mechTilt,sc.electricalTilt,sc.antennaType) from SmallCellSiteDetail sc where sc.networkElement.neStatus=:neStatus and sc.networkElement.isDeleted=false and sc.networkElement.neName=:neName  and sc.networkElement.neFrequency=:band and UPPER(sc.networkElement.vendor)=:vendor and UPPER(sc.networkElement.technology)=:technology and sc.networkElement.neType=:neType and UPPER(sc.networkElement.domain)=:domain"),
	@NamedQuery(name = "getSmallSiteBySapIdAndBand", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(sc.networkElement.id,sc.sector,sc.cellName,sc.networkElement.neName,sc.cellId,sc.networkElement.neFrequency,sc.azimuth,sc.height,sc.mechTilt,sc.electricalTilt,sc.antennaType) from SmallCellSiteDetail sc where sc.networkElement.neStatus='ONAIR' and sc.networkElement.isDeleted=false and sc.networkElement.neName=:neName  and sc.networkElement.neFrequency=:band and sc.sector=:sector "),
	@NamedQuery(name = "getSmallSiteBySapId", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(sc.networkElement.id,sc.sector,sc.cellName,sc.networkElement.neName,sc.cellId,sc.networkElement.neFrequency,sc.azimuth,sc.height,sc.mechTilt,sc.electricalTilt,sc.antennaType) from SmallCellSiteDetail sc where sc.networkElement.neStatus='ONAIR' and sc.networkElement.isDeleted=false and sc.networkElement.neName=:neName"),
	@NamedQuery(name = "getSmallCellNEIdsForTraceport", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(s.networkElement.neName,s.enbId,s.networkElement.mnc,s.networkElement.mcc,s.networkElement.geographyL4.name) from SmallCellSiteDetail s where s.networkElement.geographyL4.id=:geographyL4Id and s.networkElement.neStatus=:neStatus"),
	@NamedQuery(name = "getSmallCellEnbIdByNEId",query = "select s.enbId from SmallCellSiteDetail s where s.networkElement.id=:neId and s.networkElement.networkElement is null"),
	@NamedQuery(name = "getSmallCellDetailByGeographyL4" ,query ="select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.networkElement.neName,n.vendor,n.neFrequency,sc.cellName,sc.cellId, n.neType,n.latitude,n.longitude,n.neStatus,sc.azimuth,sc.sector,n.domain,sc.pci,'NULL')  "
			+ "from NetworkElement n  inner join SmallCellSiteDetail sc on n.id = sc.networkElement.id where n.neStatus = 'ONAIR' and "
			+ " n.domain = :domain and n.vendor=:vendor and n.neFrequency in (:bandList) and n.geographyL4.name =:geographyName and n.neType =:neType and n.geographyL4 is not null"),

	@NamedQuery(name = "getSmallCellSiteDetails",query = "select n from RANDetail n where n.networkElement.networkElement.neName=:neName and n.networkElement.neType=:neType"),
	@NamedQuery(name = "getDistinctSmallCellSiteCellIds",query = "select distinct s.cellId from SmallCellSiteDetail s where s.networkElement.neType=:neType"),
	@NamedQuery(name = "getNetworkElementBySmallCells" ,query ="select n from NetworkElement n  inner join SmallCellSiteDetail sc on n.id = sc.networkElement.id where  "
			+ " n.domain = :domain and n.vendor=:vendor and n.neType =:neType and n.geographyL4 is not null and concat(n.networkElement.neName,'_',sc.cellId) in (:cellsList)"),

})
@Entity
@Table(name = "SmallCellSiteDetail")
@XmlRootElement(name = "SmallCellSiteDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class SmallCellSiteDetail implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5018135264475329844L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "smallcellsitedetailid_pk")
	private Integer id;

	/** The network element. */
	@JoinColumn(name = "networkelementid_fk", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;

	/** The address. */
	@Basic
	@Column(name = "address")
	private String address;

	/** The cabinet type. */
	@Basic
	@Column(name = "cabinettype")
	private String cabinetType;

	/** The total SMC count. */
	@Basic
	@Column(name = "totalsmccount")
	private Integer totalSMCCount;

	/** The planned SMC count. */
	@Basic
	@Column(name = "plannedsmccount")
	private Integer plannedSMCCount;

	/** The on air SMC count. */
	@Basic
	@Column(name = "onairsmccount")
	private Integer onAirSMCCount;

	/** The installed SMC count. */
	@Basic
	@Column(name = "installedsmccount")
	private Integer installedSMCCount;

	/** The azimuth. */
	@Basic
	@Column(name = "azimuth")
	private Integer azimuth;

	/** The height. */
	@Basic
	@Column(name = "height")
	private Double height;

	/** The tower type. */
	@Basic
	@Column(name = "towertype")
	private String towerType;

	/** The mech tilt. */
	@Basic
	@Column(name = "mechtilt")
	private Integer mechTilt;

	/** The antenna type. */
	@Basic
	@Column(name = "antennatype")
	private String antennaType;

	/** The electrical tilt. */
	@Basic
	@Column(name = "electilt")
	private Integer electricalTilt;

	/** The pci. */
	@Basic
	@Column(name = "pci")
	private Integer pci;

	/** The sector. */
	@Basic
	@Column(name = "sector")
	private Integer sector;

	/** The cell id. */
	@Basic
	@Column(name = "cellid")
	private Integer cellId;

	/** The enb id. */
	@Basic
	@Column(name = "enbid")
	private Integer enbId;
	
	/** The operational status. */
	@Basic
	@Column(name = "operationalstatus")
	private String operationalStatus;

	/** The admin state. */
	@Basic
	@Column(name = "adminState")
	private String adminState;
	
	/** The cell name. */
	@Basic
	@Column(name = "cellName")
	private String cellName;
	
	
	/** The site parameter */
	@Basic
	@Column(name = "tac")
	private Integer tac;
	
	
	/** The site parameter */
	@Basic
	@Column(name = "txpower")
	private Integer txpower;
	
	
	/** The site parameter */
	@Basic
	@Column(name = "bandwidth")
	private String bandwidth;
	
	
	/** The site parameter */
	@Basic
	@Column(name = "earfcn")
	private Integer earfcn;
	
	/** The creation time. */
	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modificationTime;
	
	@JoinColumn(name = "nebanddetailid_fk", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private NEBandDetail neBandDetail;
	
	/** The scope. */
	@Basic
	@Column(name = "ecgi")
	private String ecgi;
	
	/**
	 * Gets the azimuth.
	 *
	 * @return the azimuth
	 */
	public Integer getAzimuth() {
		return azimuth;
	}

	/**
	 * Sets the azimuth.
	 *
	 * @param azimuth the new azimuth
	 */
	public void setAzimuth(Integer azimuth) {
		this.azimuth = azimuth;
	}

	/**
	 * Gets the tower type.
	 *
	 * @return the tower type
	 */
	public String getTowerType() {
		return towerType;
	}

	/**
	 * Sets the tower type.
	 *
	 * @param towerType the new tower type
	 */
	public void setTowerType(String towerType) {
		this.towerType = towerType;
	}

	/**
	 * Gets the antenna type.
	 *
	 * @return the antenna type
	 */
	public String getAntennaType() {
		return antennaType;
	}

	/**
	 * Sets the antenna type.
	 *
	 * @param antennaType the new antenna type
	 */
	public void setAntennaType(String antennaType) {
		this.antennaType = antennaType;
	}

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
	 * Gets the cabinet type.
	 *
	 * @return the cabinet type
	 */
	public String getCabinetType() {
		return cabinetType;
	}

	/**
	 * Sets the cabinet type.
	 *
	 * @param cabinetType the new cabinet type
	 */
	public void setCabinetType(String cabinetType) {
		this.cabinetType = cabinetType;
	}

	/**
	 * Gets the total SMC count.
	 *
	 * @return the total SMC count
	 */
	public Integer getTotalSMCCount() {
		return totalSMCCount;
	}

	/**
	 * Sets the total SMC count.
	 *
	 * @param totalSMCCount the new total SMC count
	 */
	public void setTotalSMCCount(Integer totalSMCCount) {
		this.totalSMCCount = totalSMCCount;
	}

	/**
	 * Gets the planned SMC count.
	 *
	 * @return the planned SMC count
	 */
	public Integer getPlannedSMCCount() {
		return plannedSMCCount;
	}

	/**
	 * Sets the planned SMC count.
	 *
	 * @param plannedSMCCount the new planned SMC count
	 */
	public void setPlannedSMCCount(Integer plannedSMCCount) {
		this.plannedSMCCount = plannedSMCCount;
	}

	/**
	 * Gets the on air SMC count.
	 *
	 * @return the on air SMC count
	 */
	public Integer getOnAirSMCCount() {
		return onAirSMCCount;
	}

	/**
	 * Sets the on air SMC count.
	 *
	 * @param onAirSMCCount the new on air SMC count
	 */
	public void setOnAirSMCCount(Integer onAirSMCCount) {
		this.onAirSMCCount = onAirSMCCount;
	}

	/**
	 * Gets the installed SMC count.
	 *
	 * @return the installed SMC count
	 */
	public Integer getInstalledSMCCount() {
		return installedSMCCount;
	}

	/**
	 * Sets the installed SMC count.
	 *
	 * @param installedSMCCount the new installed SMC count
	 */
	public void setInstalledSMCCount(Integer installedSMCCount) {
		this.installedSMCCount = installedSMCCount;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public Double getHeight() {
		return height;
	}

	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(Double height) {
		this.height = height;
	}

	/**
	 * Gets the pci.
	 *
	 * @return the pci
	 */
	public Integer getPci() {
		return pci;
	}

	/**
	 * Sets the pci.
	 *
	 * @param pci the new pci
	 */
	public void setPci(Integer pci) {
		this.pci = pci;
	}

	/**
	 * Gets the sector.
	 *
	 * @return the sector
	 */
	public Integer getSector() {
		return sector;
	}

	/**
	 * Sets the sector.
	 *
	 * @param sector the new sector
	 */
	public void setSector(Integer sector) {
		this.sector = sector;
	}

	/**
	 * Gets the cell id.
	 *
	 * @return the cell id
	 */
	public Integer getCellId() {
		return cellId;
	}

	/**
	 * Sets the cell id.
	 *
	 * @param cellId the new cell id
	 */
	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	/**
	 * Gets the enb id.
	 *
	 * @return the enb id
	 */
	public Integer getEnbId() {
		return enbId;
	}

	/**
	 * Sets the enb id.
	 *
	 * @param enbId the new enb id
	 */
	public void setEnbId(Integer enbId) {
		this.enbId = enbId;
	}

	/**
	 * Gets the operational status.
	 *
	 * @return the operational status
	 */
	public String getOperationalStatus() {
		return operationalStatus;
	}

	/**
	 * Sets the operational status.
	 *
	 * @param operationalStatus the new operational status
	 */
	public void setOperationalStatus(String operationalStatus) {
		this.operationalStatus = operationalStatus;
	}

	/**
	 * Gets the admin state.
	 *
	 * @return the admin state
	 */
	public String getAdminState() {
		return adminState;
	}

	/**
	 * Sets the admin state.
	 *
	 * @param adminState the new admin state
	 */
	public void setAdminState(String adminState) {
		this.adminState = adminState;
	}

	/**
	 * Gets the cell name.
	 *
	 * @return the cell name
	 */
	public String getCellName() {
		return cellName;
	}

	/**
	 * Sets the cell name.
	 *
	 * @param cellName the new cell name
	 */
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	/**
	 * Gets the mech tilt.
	 *
	 * @return the mech tilt
	 */
	public Integer getMechTilt() {
		return mechTilt;
	}

	/**
	 * Sets the mech tilt.
	 *
	 * @param mechTilt the new mech tilt
	 */
	public void setMechTilt(Integer mechTilt) {
		this.mechTilt = mechTilt;
	}

	/**
	 * Gets the electrical tilt.
	 *
	 * @return the electrical tilt
	 */
	public Integer getElectricalTilt() {
		return electricalTilt;
	}

	/**
	 * Sets the electrical tilt.
	 *
	 * @param electricalTilt the new electrical tilt
	 */
	public void setElectricalTilt(Integer electricalTilt) {
		this.electricalTilt = electricalTilt;
	}

	public Integer getTac() {
		return tac;
	}

	public void setTac(Integer tac) {
		this.tac = tac;
	}

	public Integer getTxpower() {
		return txpower;
	}

	public void setTxpower(Integer txpower) {
		this.txpower = txpower;
	}

	public Integer getEarfcn() {
		return earfcn;
	}

	public void setEarfcn(Integer earfcn) {
		this.earfcn = earfcn;
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

	public NEBandDetail getNeBandDetail() {
		return neBandDetail;
	}

	public void setNeBandDetail(NEBandDetail neBandDetail) {
		this.neBandDetail = neBandDetail;
	}


	public String getEcgi() {
		return ecgi;
	}

	public void setEcgi(String ecgi) {
		this.ecgi = ecgi;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	@Override
	public String toString() {
		return "SmallCellSiteDetail [id=" + id + ", networkElement=" + networkElement + ", address=" + address
				+ ", cabinetType=" + cabinetType + ", totalSMCCount=" + totalSMCCount + ", plannedSMCCount="
				+ plannedSMCCount + ", onAirSMCCount=" + onAirSMCCount + ", installedSMCCount=" + installedSMCCount
				+ ", azimuth=" + azimuth + ", height=" + height + ", towerType=" + towerType + ", mechTilt=" + mechTilt
				+ ", antennaType=" + antennaType + ", electricalTilt=" + electricalTilt + ", pci=" + pci + ", sector="
				+ sector + ", cellId=" + cellId + ", enbId=" + enbId + ", operationalStatus=" + operationalStatus
				+ ", adminState=" + adminState + ", cellName=" + cellName + ", tac=" + tac + ", txpower=" + txpower
				+ ", bandwidth=" + bandwidth + ", earfcn=" + earfcn + ", creationTime=" + creationTime
				+ ", modificationTime=" + modificationTime + ", neBandDetail=" + neBandDetail + ", ecgi=" + ecgi + "]";
	}

}