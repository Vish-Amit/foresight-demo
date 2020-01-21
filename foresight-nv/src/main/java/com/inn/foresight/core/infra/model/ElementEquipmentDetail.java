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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQueries({
@NamedQuery(name = "getEquipementDetail", query = "select new com.inn.foresight.core.infra.wrapper.NESlotDetailWrapper(ed.name,ed.elementName,ed.slotNumber,ed.shelfNumber,ed.rackNumber,ed.position,ed.version,ed.networkElement.neName,ed.networkElement.neType,ed.networkElement.ipv4,ed.networkElement.neId,ed.networkElement.model,ed.nodeIdSideType,ed.kernelRelease,ed.physicalIp,ed.virtualIp,ed.fabricIp,ed.ovldLevel,ed.versionNumber,ed.application,ed.timeOfUpdate,ed.inventoryUnitId,ed.inventoryUnitType,ed.vendorUnitFamilyType,ed.vendorUnitTypeNumber,ed.dateOfManufacture,ed.dateOfLastService,ed.manufacturer,ed.holderState,b.deviceid,b.neType,ed.networkElement.vendor,ed.installedSerialNumber,ed.installedEquipmentType,ed.installedPartNumber,ed.installedVendorPartNumber,ed.installedVendorMaterialNumber,ed.installedVersion,ed.elementId,ed.chassis,ed.installedModel,ed.status,ed.locationName,ed.userdefinedState,ed.administrativeState,ed.operationalState,ed.hardwarePlateform,ed.pvFlag,ed.vmId,ed.exmoc,ed.subnetId,ed.assetTag,ed.info,ed.idDescription) from ElementEquipmentDetail ed left join ed.networkElement ne left join BackhaulDetail b on b.networkElement.id=ed.networkElement.id where ed.networkElement.neName in(:neName) and ed.networkElement.neId in(:neId) and  ed.networkElement.domain=:domain and ed.isDeleted=0 and ed.networkElement.isDeleted=0 order by inventoryUnitId"),
@NamedQuery(name = "getEquipementByNE", query = "select new com.inn.foresight.core.infra.wrapper.ElementEquipmentDetailWrapper(ed.id,ed.name,ed.networkElement.neId,ed.holderType,ed.holderState,ed.displayName,ed.installedEquipmentType,ed.installedPartNumber,ed.installedVendorPartNumber,ed.installedVendorMaterialNumber,ed.installedSerialNumber,ed.manufacturer,ed.description,ed.installedVersion,ed.elementEquipmentDetail.id,ed.elementId,ed.chassis,ed.installedModel,ed.status) from ElementEquipmentDetail ed  where ed.networkElement.neId=:neId and ed.networkElement.neName=:neName and ed.isDeleted=0 and ed.networkElement.isDeleted=0")})


@XmlRootElement(name = "ElementEquipmentDetail")
@Entity
@Table(name = "ElementEquipmentDetail")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class ElementEquipmentDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1385893521282577722L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "elementequipmentdetailid_pk")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "position")
	private String position;

	@Column(name = "racknumber")
	private String rackNumber;

	@Column(name = "shelfnumber")
	private String shelfNumber;

	@Column(name = "slotnumber")
	private String slotNumber;

	@Column(name = "creationtime")
	private Date createdTime;

	@Column(name = "modificationtime")
	private Date modifiedTime;

	@Column(name = "deleted")
	private Boolean isDeleted;

	@Column(name = "dateofmanufacture")
	private Date dateOfManufacture;

	@Column(name = "dateoflastService")
	private Date dateOfLastService;

	@Column(name = "elementname")
	private String elementName;
	
	@Column(name ="version")
	private String version;

	/** The network element. */
	@JoinColumn(name = "networkelementid_fk", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;

	@Column(name ="holdertype")
	private String holderType;

	@Column(name ="holderstate")
	private String holderState;

	@Column(name ="displayname")
	private String displayName;

	@Column(name ="installedequipmenttype")
	private String installedEquipmentType;

	@Column(name ="installedpartnumber")
	private String installedPartNumber;

	@Column(name ="installedvendorpartnumber")
	private String installedVendorPartNumber;

	@Column(name ="installedvendormaterialnumber")
	private String installedVendorMaterialNumber;

	@Column(name ="installedserialnumber")
	private String installedSerialNumber;

	@Column(name ="manufacturer")
	private String manufacturer;

	@Column(name ="description")
	private String description;

	@Column(name ="installedversion")
	private String installedVersion;

	@Column(name ="elementid")
	private String elementId;
	
	@Column(name ="chassis")
	private String chassis;
	
	@Column(name ="installedmodel")
	private String installedModel;
	
	@JoinColumn(name = "elementequipmentdetailid_fk", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ElementEquipmentDetail elementEquipmentDetail;
	
	@Column(name ="nodeidsidetype")
	private String nodeIdSideType;
	
	@Column(name ="kernelrelease")
	private String kernelRelease;
	
	@Column(name ="physicalip")
	private String physicalIp;
	
	@Column(name ="virtualip")
	private String virtualIp;
	
	@Column(name ="fabricip")
	private String fabricIp;
	
	@Column(name ="ovldlevel")
	private String ovldLevel;
	
	@Column(name ="versionnumber")
	private String versionNumber;
	
	@Column(name ="application")
	private String application;
	
	@Column(name ="timeofupdate")
	private String timeOfUpdate;
	
	@Column(name ="status")
	private String status;
	
	@Column(name ="inventoryunitid")
	private String inventoryUnitId;
	
	@Column(name ="inventoryunittype")
	private String inventoryUnitType;
	
	@Column(name ="vendorunitfamilytype")
	private String vendorUnitFamilyType;
	
	@Column(name ="vendorunittypenumber")
	private String vendorUnitTypeNumber;
	
	@Column(name="locationname")
	private String locationName;
	
	@Column(name="userdefinedstate")
	private Boolean userdefinedState;
	
	@Column(name="administrativestate")
	private Boolean administrativeState;
	
	@Column(name="operationalstate")
	private Boolean operationalState;
	
	@Column(name="hardwareplateform")
	private String hardwarePlateform;
	
	@Column(name="pvflag")
	private String pvFlag;
	
	@Column(name="vmid")
	private String vmId;
	
	@Column(name="exmoc")
	private String exmoc;
	
	@Column(name="subnetid")
	private String subnetId;
	
	@Column(name ="assettag")
	private String assetTag;
	
	@Column(name ="info")
	private String info;
	
	@Column(name="iddescription")
	private String idDescription;
	
	
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRacknumber() {
		return rackNumber;
	}

	public void setRacknumber(String racknumber) {
		this.rackNumber = racknumber;
	}

	public String getShelfNumber() {
		return shelfNumber;
	}

	public void setShelfNumber(String shelfNumber) {
		this.shelfNumber = shelfNumber;
	}

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
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

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getDateOfManufacture() {
		return dateOfManufacture;
	}

	public void setDateOfManufacture(Date dateOfManufacture) {
		this.dateOfManufacture = dateOfManufacture;
	}

	public Date getDateOfLastService() {
		return dateOfLastService;
	}

	public void setDateOfLastService(Date dateOfLastService) {
		this.dateOfLastService = dateOfLastService;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public NetworkElement getNetworkElement() {
		return networkElement;
	}

	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}

	
	public String getRackNumber() {
		return rackNumber;
	}

	public void setRackNumber(String rackNumber) {
		this.rackNumber = rackNumber;
	}

	public String getHolderType() {
		return holderType;
	}

	public void setHolderType(String holderType) {
		this.holderType = holderType;
	}

	public String getHolderState() {
		return holderState;
	}

	public void setHolderState(String holderState) {
		this.holderState = holderState;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getInstalledEquipmentType() {
		return installedEquipmentType;
	}

	public void setInstalledEquipmentType(String installedEquipmentType) {
		this.installedEquipmentType = installedEquipmentType;
	}

	public String getInstalledPartNumber() {
		return installedPartNumber;
	}

	public void setInstalledPartNumber(String installedPartNumber) {
		this.installedPartNumber = installedPartNumber;
	}

	public String getInstalledVendorPartNumber() {
		return installedVendorPartNumber;
	}

	public void setInstalledVendorPartNumber(String installedVendorPartNumber) {
		this.installedVendorPartNumber = installedVendorPartNumber;
	}

	public String getInstalledVendorMaterialNumber() {
		return installedVendorMaterialNumber;
	}

	public void setInstalledVendorMaterialNumber(String installedVendorMaterialNumber) {
		this.installedVendorMaterialNumber = installedVendorMaterialNumber;
	}

	public String getInstalledSerialNumber() {
		return installedSerialNumber;
	}

	public void setInstalledSerialNumber(String installedSerialNumber) {
		this.installedSerialNumber = installedSerialNumber;
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

	public String getInstalledVersion() {
		return installedVersion;
	}

	public void setInstalledVersion(String installedVersion) {
		this.installedVersion = installedVersion;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public ElementEquipmentDetail getElementEquipmentDetail() {
		return elementEquipmentDetail;
	}

	public void setElementEquipmentDetail(ElementEquipmentDetail elementEquipmentDetail) {
		this.elementEquipmentDetail = elementEquipmentDetail;
	}

	
	public String getVersion() {
		return version;
	}

	
	public void setVersion(String version) {
		this.version = version;
	}

	public String getChassis() {
		return chassis;
	}

	public void setChassis(String chassis) {
		this.chassis = chassis;
	}

	public String getInstalledModel() {
		return installedModel;
	}

	public void setInstalledModel(String installedModel) {
		this.installedModel = installedModel;
	}
	
	public String getNodeIdSideType() {
		return nodeIdSideType;
	}


	public void setNodeIdSideType(String nodeIdSideType) {
		this.nodeIdSideType = nodeIdSideType;
	}

	public String getKernelRelease() {
		return kernelRelease;
	}

	public void setKernelRelease(String kernelRelease) {
		this.kernelRelease = kernelRelease;
	}

	
	public String getPhysicalIp() {
		return physicalIp;
	}

	public void setPhysicalIp(String physicalIp) {
		this.physicalIp = physicalIp;
	}

	
	public String getVirtualIp() {
		return virtualIp;
	}

	public void setVirtualIp(String virtualIp) {
		this.virtualIp = virtualIp;
	}

	public String getFabricIp() {
		return fabricIp;
	}

	public void setFabricIp(String fabricIp) {
		this.fabricIp = fabricIp;
	}

	public String getOvldLevel() {
		return ovldLevel;
	}
	public void setOvldLevel(String ovldLevel) {
		this.ovldLevel = ovldLevel;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}

	/**
	 * @return the timeOfUpdate
	 */
	public String getTimeOfUpdate() {
		return timeOfUpdate;
	}

	/**
	 * @param timeOfUpdate the timeOfUpdate to set
	 */
	public void setTimeOfUpdate(String timeOfUpdate) {
		this.timeOfUpdate = timeOfUpdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the inventoryUnitId
	 */
	public String getInventoryUnitId() {
		return inventoryUnitId;
	}

	/**
	 * @param inventoryUnitId the inventoryUnitId to set
	 */
	public void setInventoryUnitId(String inventoryUnitId) {
		this.inventoryUnitId = inventoryUnitId;
	}

	/**
	 * @return the inventoryUnitType
	 */
	public String getInventoryUnitType() {
		return inventoryUnitType;
	}

	/**
	 * @param inventoryUnitType the inventoryUnitType to set
	 */
	public void setInventoryUnitType(String inventoryUnitType) {
		this.inventoryUnitType = inventoryUnitType;
	}

	/**
	 * @return the vendorUnitFamilyType
	 */
	public String getVendorUnitFamilyType() {
		return vendorUnitFamilyType;
	}

	/**
	 * @param vendorUnitFamilyType the vendorUnitFamilyType to set
	 */
	public void setVendorUnitFamilyType(String vendorUnitFamilyType) {
		this.vendorUnitFamilyType = vendorUnitFamilyType;
	}

	/**
	 * @return the vendorUnitTypeNumber
	 */
	public String getVendorUnitTypeNumber() {
		return vendorUnitTypeNumber;
	}

	/**
	 * @param vendorUnitTypeNumber the vendorUnitTypeNumber to set
	 */
	public void setVendorUnitTypeNumber(String vendorUnitTypeNumber) {
		this.vendorUnitTypeNumber = vendorUnitTypeNumber;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * @return the userdefinedState
	 */
	public Boolean getUserdefinedState() {
		return userdefinedState;
	}

	/**
	 * @param userdefinedState the userdefinedState to set
	 */
	public void setUserdefinedState(Boolean userdefinedState) {
		this.userdefinedState = userdefinedState;
	}

	/**
	 * @return the administrativeState
	 */
	public Boolean getAdministrativeState() {
		return administrativeState;
	}

	/**
	 * @param administrativeState the administrativeState to set
	 */
	public void setAdministrativeState(Boolean administrativeState) {
		this.administrativeState = administrativeState;
	}

	/**
	 * @return the operationalState
	 */
	public Boolean getOperationalState() {
		return operationalState;
	}

	/**
	 * @param operationalState the operationalState to set
	 */
	public void setOperationalState(Boolean operationalState) {
		this.operationalState = operationalState;
	}

	/**
	 * @return the hardwarePlateform
	 */
	public String getHardwarePlateform() {
		return hardwarePlateform;
	}

	/**
	 * @param hardwarePlateform the hardwarePlateform to set
	 */
	public void setHardwarePlateform(String hardwarePlateform) {
		this.hardwarePlateform = hardwarePlateform;
	}

	/**
	 * @return the pvFlag
	 */
	public String getPvFlag() {
		return pvFlag;
	}

	/**
	 * @param pvFlag the pvFlag to set
	 */
	public void setPvFlag(String pvFlag) {
		this.pvFlag = pvFlag;
	}

	/**
	 * @return the vmId
	 */
	public String getVmId() {
		return vmId;
	}

	/**
	 * @param vmId the vmId to set
	 */
	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	/**
	 * @return the exmoc
	 */
	public String getExmoc() {
		return exmoc;
	}

	/**
	 * @param exmoc the exmoc to set
	 */
	public void setExmoc(String exmoc) {
		this.exmoc = exmoc;
	}

	/**
	 * @return the subnetId
	 */
	public String getSubnetId() {
		return subnetId;
	}

	/**
	 * @param subnetId the subnetId to set
	 */
	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}

	/**
	 * @return the assetTag
	 */
	public String getAssetTag() {
		return assetTag;
	}

	/**
	 * @param assetTag the assetTag to set
	 */
	public void setAssetTag(String assetTag) {
		this.assetTag = assetTag;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the idDescription
	 */
	public String getIdDescription() {
		return idDescription;
	}

	/**
	 * @param idDescription the idDescription to set
	 */
	public void setIdDescription(String idDescription) {
		this.idDescription = idDescription;
	}

	
}
