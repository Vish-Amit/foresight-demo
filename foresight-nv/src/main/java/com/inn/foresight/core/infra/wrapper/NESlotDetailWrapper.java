package com.inn.foresight.core.infra.wrapper;

import java.util.Date;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Vendor;
@JpaWrapper
public class NESlotDetailWrapper {
	// EquipmentDetail
	private String elementEquipmentName;
	private String elementName;
	private String slotNumber;
	private String shelfNumber;
	private String rackNumber;
	private String position;
	private String serialNumber;
	private String version;

	private String nodeIdSideType;
	private String kernelRelease;
	private String physicalIp;
	private String virtualIp;
	private String fabricIp;
	private String ovldLevel;
	private String versionNumber;
	private String application;
	private String timeOfUpdate;

	private String inventoryUnitId;
	private String inventoryUnitType;
	private String vendorUnitFamilyType;
	private String vendorUnitTypeNumber;

	private Date dateOfManufacture;
	private Date dateOfLastService;
	private String manufacturer;
	private String holderState;

	private String deviceId;
	private String backhaulNEType;
	private String vendor;
	private String installedSerialNumber;

	private String installedEquipmentType;
	private String installedPartNumber;
	private String installedVendorPartNumber;
	private String installedVendorMaterialNumber;
	private String installedVersion;
	private String elementId;
	private String chassis;
	private String installedModel;
	private String status;
	private String locationName;
	private Boolean userdefinedState;
	private Boolean administrativeState;
	private Boolean operationalState;
	private String hardwarePlateform;
	private String pvFlag;
	private String vmId;
	private String exmoc;
	private String subnetId;
	private String assetTag;
	private String info;
	private String idDescription;

	// Slot Detail
	private String slotDisplay;
	private String portNumber;
	private String avStatus;
	private String opStatus;
	private String oid;
	private String slotName;
	private String boardtype;
	private String description;
	private String poid;
	private Integer slotId;
	private String boardName;

	// Subslot detail
	private Integer subSlotId;
	private String subSlotOid;
	private String subSlotName;
	private String subSlotDisplayName;
	private String subSlotDescription;
	private String subSlotPoid;
	private String subSlotAvStatus;
	private String subSlotOpStatus;
	private String subSlotPortNumber;
	private String subSlotType;
	private String subSlotNumber;
	private String subBoardName;

	List<NESlotDetailWrapper> subSlotDetailList;

	// NetworkElement
	private String neName;
	private String neType;
	private String neIpV4;
	private String neId;
	private String model;
	private String pmEmsId;

	public NESlotDetailWrapper() {
		super();
	}

	// ElementEquipmentDetail
	// ed.name,ed.elementName,ed.slotNumber,ed.shelfNumber,ed.rackNumber,ed.position
	// ,ed.networkElement.neName,ed.networkElement.neType,ed.networkElement.ipv4,ed.networkElement.neId,ed.networkElement.model
	public NESlotDetailWrapper(String elementEquipmentName, String elementName, String slotNumber, String shelfNumber,
			String rackNumber, String position, String version, String naName, NEType neType, String neIpV4,
			String neId, String model, String nodeIdSideType, String kernelRelease, String physicalIp, String virtualIp,
			String fabricIp, String ovldLevel, String versionNumber, String application, String timeOfUpdate,
			String inventoryUnitId, String inventoryUnitType, String vendorUnitFamilyType, String vendorUnitTypeNumber,
			Date dateOfManufacture, Date dateOfLastService, String manufacturer, String holderState, String deviceId,
			String backhaulNEType, Vendor vendor, String installedSerialNumber, String installedEquipmentType,
			String installedPartNumber, String installedVendorPartNumber, String installedVendorMaterialNumber,
			String installedVersion, String elementId, String chassis, String installedModel, String status,
			String locationName, Boolean userdefinedState, Boolean administrativeState, Boolean operationalState,
			String hardwarePlateform, String pvFlag, String vmId, String exmoc, String subnetId, String assetTag,
			String info,String idDescription) {

		super();
		this.elementEquipmentName = elementEquipmentName;
		this.elementName = elementName;
		this.slotNumber = slotNumber;
		this.shelfNumber = shelfNumber;
		this.rackNumber = rackNumber;
		this.position = position;
		this.version = version;
		this.serialNumber = rackNumber + "_" + shelfNumber + "_" + slotNumber;
		this.neName = naName;
		this.neType = neType != null ? neType.toString() : null;
		this.neIpV4 = neIpV4;
		this.neId = neId;
		this.model = model;

		this.nodeIdSideType = nodeIdSideType;
		this.kernelRelease = kernelRelease;
		this.physicalIp = physicalIp;
		this.virtualIp = virtualIp;
		this.fabricIp = fabricIp;
		this.ovldLevel = ovldLevel;
		this.versionNumber = versionNumber;
		this.application = application;
		this.timeOfUpdate = timeOfUpdate;

		this.holderState = holderState;
		this.inventoryUnitId = inventoryUnitId;
		this.inventoryUnitType = inventoryUnitType;
		this.vendorUnitFamilyType = vendorUnitFamilyType;
		this.vendorUnitTypeNumber = vendorUnitTypeNumber;
		this.manufacturer = manufacturer;
		this.dateOfLastService = dateOfLastService;
		this.dateOfManufacture = dateOfManufacture;
		this.deviceId = deviceId;
		this.backhaulNEType = backhaulNEType;
		this.vendor = vendor != null ? vendor.displayName() : "";
		this.installedSerialNumber = installedSerialNumber;

		this.installedEquipmentType = installedEquipmentType;
		this.installedPartNumber = installedPartNumber;
		this.installedVendorPartNumber = installedVendorPartNumber;
		this.installedVendorMaterialNumber = installedVendorMaterialNumber;
		this.installedVersion = installedVersion;
		this.elementId = elementId;
		this.chassis = chassis;
		this.installedModel = installedModel;
		this.status = status;
		this.locationName = locationName;
		this.userdefinedState = userdefinedState;
		this.administrativeState = administrativeState;
		this.operationalState = operationalState;
		this.hardwarePlateform = hardwarePlateform;
		this.pvFlag = pvFlag;
		this.vmId = vmId;
		this.exmoc = exmoc;
		this.subnetId = subnetId;
		this.assetTag = assetTag;
		this.info = info;
		this.idDescription=idDescription;
	}

	// ns.id,ns.rackNumber,ns.shelfNumber,ns.slotDisplay,ns.portNumber,ns.slotNumber,ns.avStatus,ns.opStatus,ns.oid,ns.slotName,ns.boardtype,ns.boadName
	// nss.id,nss.oid,nss.slotName,nss.displayName,nss.description,nss.poid,nss.avStatus,nss.opStatus,nss.subSlotPortNumber,nss.subSlotType,nss.subSlotNumber,nss.subBoardName
	//// ,ns.networkElement.neName,ns.networkElement.neType,ns.networkElement.ipv4,ns.networkElement.neId,ns.networkElement.model
	// Slot and sub-slot detail
	public NESlotDetailWrapper(Integer nsId, String rackNumber, String shelfNumber, String slotDisplay,
			String portNumber, String slotNumber, String avStatus, String opStatus, String oid, String slotName,
			String boardtype, String boardName, Integer nssId, String subSlotOid, String subSlotName,
			String subSlotDisplayName, String subSlotDescription, String subSlotPoid, String subSlotAvStatus,
			String subSlotOpStatus, String subSlotPortNumber, String subSlotType, String subSlotNumber,
			String subBoardName, String naName, NEType neType, String neIpV4, String neId, String model,
			String pmEmsId) {
		super();
		this.slotId = nsId;
		this.rackNumber = rackNumber;
		this.shelfNumber = shelfNumber;
		this.slotDisplay = slotDisplay;
		this.portNumber = portNumber;
		this.slotNumber = slotNumber;
		this.avStatus = avStatus;
		this.opStatus = opStatus;
		this.oid = oid;
		this.slotName = slotName;
		this.boardtype = boardtype;
		this.boardName = boardName;

		this.subSlotId = nssId;
		this.subSlotOid = subSlotOid;
		this.subSlotName = subSlotName;
		this.subSlotDisplayName = subSlotDisplayName;
		this.subSlotDescription = subSlotDescription;
		this.subSlotPoid = subSlotPoid;
		this.subSlotAvStatus = subSlotAvStatus;
		this.subSlotOpStatus = subSlotOpStatus;
		this.subSlotPortNumber = subSlotPortNumber;
		this.subSlotType = subSlotType;
		this.subSlotNumber = subSlotNumber;
		this.subBoardName = subBoardName;

		this.neName = naName;
		this.neType = neType != null ? neType.toString() : null;
		this.neIpV4 = neIpV4;
		this.neId = neId;
		this.model = model;
		this.pmEmsId = pmEmsId;
	}

	/**
	 * @return the elementEquipmentName
	 */
	public String getElementEquipmentName() {
		return elementEquipmentName;
	}

	/**
	 * @param elementEquipmentName
	 *            the elementEquipmentName to set
	 */
	public void setElementEquipmentName(String elementEquipmentName) {
		this.elementEquipmentName = elementEquipmentName;
	}

	/**
	 * @return the elementName
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * @param elementName
	 *            the elementName to set
	 */
	public void setElementName(String elementName) {
		this.elementName = elementName;
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
	 * @return the racknumber
	 */
	public String getRacknumber() {
		return rackNumber;
	}

	/**
	 * @param racknumber
	 *            the racknumber to set
	 */
	public void setRacknumber(String racknumber) {
		this.rackNumber = racknumber;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
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
	 * @param poid
	 *            the poid to set
	 */
	public void setPoid(String poid) {
		this.poid = poid;
	}

	/**
	 * @return the subSlotPortNumber
	 */
	public String getSubSlotPortNumber() {
		return subSlotPortNumber;
	}

	/**
	 * @param subSlotPortNumber
	 *            the subSlotPortNumber to set
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
	 * @param subSlotType
	 *            the subSlotType to set
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
	 * @param subSlotNumber
	 *            the subSlotNumber to set
	 */
	public void setSubSlotNumber(String subSlotNumber) {
		this.subSlotNumber = subSlotNumber;
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
	 * @return the slotId
	 */
	public Integer getSlotId() {
		return slotId;
	}

	/**
	 * @param slotId
	 *            the slotId to set
	 */
	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	/**
	 * @return the subSlotId
	 */
	public Integer getSubSlotId() {
		return subSlotId;
	}

	/**
	 * @param subSlotId
	 *            the subSlotId to set
	 */
	public void setSubSlotId(Integer subSlotId) {
		this.subSlotId = subSlotId;
	}

	/**
	 * @return the subSlotOid
	 */
	public String getSubSlotOid() {
		return subSlotOid;
	}

	/**
	 * @param subSlotOid
	 *            the subSlotOid to set
	 */
	public void setSubSlotOid(String subSlotOid) {
		this.subSlotOid = subSlotOid;
	}

	/**
	 * @return the subSlotName
	 */
	public String getSubSlotName() {
		return subSlotName;
	}

	/**
	 * @param subSlotName
	 *            the subSlotName to set
	 */
	public void setSubSlotName(String subSlotName) {
		this.subSlotName = subSlotName;
	}

	/**
	 * @return the subSlotDisplayName
	 */
	public String getSubSlotDisplayName() {
		return subSlotDisplayName;
	}

	/**
	 * @param subSlotDisplayName
	 *            the subSlotDisplayName to set
	 */
	public void setSubSlotDisplayName(String subSlotDisplayName) {
		this.subSlotDisplayName = subSlotDisplayName;
	}

	/**
	 * @return the subSlotDescription
	 */
	public String getSubSlotDescription() {
		return subSlotDescription;
	}

	/**
	 * @param subSlotDescription
	 *            the subSlotDescription to set
	 */
	public void setSubSlotDescription(String subSlotDescription) {
		this.subSlotDescription = subSlotDescription;
	}

	/**
	 * @return the subSlotPoid
	 */
	public String getSubSlotPoid() {
		return subSlotPoid;
	}

	/**
	 * @param subSlotPoid
	 *            the subSlotPoid to set
	 */
	public void setSubSlotPoid(String subSlotPoid) {
		this.subSlotPoid = subSlotPoid;
	}

	/**
	 * @return the subSlotAvStatus
	 */
	public String getSubSlotAvStatus() {
		return subSlotAvStatus;
	}

	/**
	 * @param subSlotAvStatus
	 *            the subSlotAvStatus to set
	 */
	public void setSubSlotAvStatus(String subSlotAvStatus) {
		this.subSlotAvStatus = subSlotAvStatus;
	}

	/**
	 * @return the subSlotOpStatus
	 */
	public String getSubSlotOpStatus() {
		return subSlotOpStatus;
	}

	/**
	 * @param subSlotOpStatus
	 *            the subSlotOpStatus to set
	 */
	public void setSubSlotOpStatus(String subSlotOpStatus) {
		this.subSlotOpStatus = subSlotOpStatus;
	}

	/**
	 * @return the subSlotDetailList
	 */
	public List<NESlotDetailWrapper> getSubSlotDetailList() {
		return subSlotDetailList;
	}

	/**
	 * @param subSlotDetailList
	 *            the subSlotDetailList to set
	 */
	public void setSubSlotDetailList(List<NESlotDetailWrapper> subSlotDetailList) {
		this.subSlotDetailList = subSlotDetailList;
	}

	/**
	 * @return the neName
	 */
	public String getNeName() {
		return neName;
	}

	/**
	 * @param neName
	 *            the neName to set
	 */
	public void setNeName(String neName) {
		this.neName = neName;
	}

	/**
	 * @return the neType
	 */
	public String getNeType() {
		return neType;
	}

	/**
	 * @param neType
	 *            the neType to set
	 */
	public void setNeType(String neType) {
		this.neType = neType;
	}

	/**
	 * @return the neIpV4
	 */
	public String getNeIpV4() {
		return neIpV4;
	}

	/**
	 * @param neIpV4
	 *            the neIpV4 to set
	 */
	public void setNeIpV4(String neIpV4) {
		this.neIpV4 = neIpV4;
	}

	/**
	 * @return the neId
	 */
	public String getNeId() {
		return neId;
	}

	/**
	 * @param neId
	 *            the neId to set
	 */
	public void setNeId(String neId) {
		this.neId = neId;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber
	 *            the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the boardName
	 */
	public String getBoardName() {
		return boardName;
	}

	/**
	 * @param boardName
	 *            the boardName to set
	 */
	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	/**
	 * @return the subBoardName
	 */
	public String getSubBoardName() {
		return subBoardName;
	}

	/**
	 * @param subBoardName
	 *            the subBoardName to set
	 */
	public void setSubBoardName(String subBoardName) {
		this.subBoardName = subBoardName;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the pmEmsId
	 */
	public String getPmEmsId() {
		return pmEmsId;
	}

	/**
	 * @param pmEmsId
	 *            the pmEmsId to set
	 */
	public void setPmEmsId(String pmEmsId) {
		this.pmEmsId = pmEmsId;
	}

	/**
	 * @return the nodeIdSideType
	 */
	public String getNodeIdSideType() {
		return nodeIdSideType;
	}

	/**
	 * @param nodeIdSideType
	 *            the nodeIdSideType to set
	 */
	public void setNodeIdSideType(String nodeIdSideType) {
		this.nodeIdSideType = nodeIdSideType;
	}

	/**
	 * @return the kernelRelease
	 */
	public String getKernelRelease() {
		return kernelRelease;
	}

	/**
	 * @param kernelRelease
	 *            the kernelRelease to set
	 */
	public void setKernelRelease(String kernelRelease) {
		this.kernelRelease = kernelRelease;
	}

	/**
	 * @return the physicalIp
	 */
	public String getPhysicalIp() {
		return physicalIp;
	}

	/**
	 * @param physicalIp
	 *            the physicalIp to set
	 */
	public void setPhysicalIp(String physicalIp) {
		this.physicalIp = physicalIp;
	}

	/**
	 * @return the virtualIp
	 */
	public String getVirtualIp() {
		return virtualIp;
	}

	/**
	 * @param virtualIp
	 *            the virtualIp to set
	 */
	public void setVirtualIp(String virtualIp) {
		this.virtualIp = virtualIp;
	}

	/**
	 * @return the fabricIp
	 */
	public String getFabricIp() {
		return fabricIp;
	}

	/**
	 * @param fabricIp
	 *            the fabricIp to set
	 */
	public void setFabricIp(String fabricIp) {
		this.fabricIp = fabricIp;
	}

	/**
	 * @return the ovldLevel
	 */
	public String getOvldLevel() {
		return ovldLevel;
	}

	/**
	 * @param ovldLevel
	 *            the ovldLevel to set
	 */
	public void setOvldLevel(String ovldLevel) {
		this.ovldLevel = ovldLevel;
	}

	/**
	 * @return the versionNumber
	 */
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @param versionNumber
	 *            the versionNumber to set
	 */
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * @return the application
	 */
	public String getApplication() {
		return application;
	}

	/**
	 * @param application
	 *            the application to set
	 */
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
	 * @param timeOfUpdate
	 *            the timeOfUpdate to set
	 */
	public void setTimeOfUpdate(String timeOfUpdate) {
		this.timeOfUpdate = timeOfUpdate;
	}

	/**
	 * @return the inventoryUnitId
	 */
	public String getInventoryUnitId() {
		return inventoryUnitId;
	}

	/**
	 * @param inventoryUnitId
	 *            the inventoryUnitId to set
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
	 * @param inventoryUnitType
	 *            the inventoryUnitType to set
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
	 * @param vendorUnitFamilyType
	 *            the vendorUnitFamilyType to set
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
	 * @param vendorUnitTypeNumber
	 *            the vendorUnitTypeNumber to set
	 */
	public void setVendorUnitTypeNumber(String vendorUnitTypeNumber) {
		this.vendorUnitTypeNumber = vendorUnitTypeNumber;
	}

	/**
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer
	 *            the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the dateOfManufacture
	 */
	public Date getDateOfManufacture() {
		return dateOfManufacture;
	}

	/**
	 * @param dateOfManufacture
	 *            the dateOfManufacture to set
	 */
	public void setDateOfManufacture(Date dateOfManufacture) {
		this.dateOfManufacture = dateOfManufacture;
	}

	/**
	 * @return the dateOfLastService
	 */
	public Date getDateOfLastService() {
		return dateOfLastService;
	}

	/**
	 * @param dateOfLastService
	 *            the dateOfLastService to set
	 */
	public void setDateOfLastService(Date dateOfLastService) {
		this.dateOfLastService = dateOfLastService;
	}

	/**
	 * @return the holderState
	 */
	public String getHolderState() {
		return holderState;
	}

	/**
	 * @param holderState
	 *            the holderState to set
	 */
	public void setHolderState(String holderState) {
		this.holderState = holderState;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the backhaulNEType
	 */
	public String getBackhaulNEType() {
		return backhaulNEType;
	}

	/**
	 * @param backhaulNEType
	 *            the backhaulNEType to set
	 */
	public void setBackhaulNEType(String backhaulNEType) {
		this.backhaulNEType = backhaulNEType;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the installedSerialNumber
	 */
	public String getInstalledSerialNumber() {
		return installedSerialNumber;
	}

	/**
	 * @param installedSerialNumber
	 *            the installedSerialNumber to set
	 */
	public void setInstalledSerialNumber(String installedSerialNumber) {
		this.installedSerialNumber = installedSerialNumber;
	}

	/**
	 * @return the installedEquipmentType
	 */
	public String getInstalledEquipmentType() {
		return installedEquipmentType;
	}

	/**
	 * @param installedEquipmentType
	 *            the installedEquipmentType to set
	 */
	public void setInstalledEquipmentType(String installedEquipmentType) {
		this.installedEquipmentType = installedEquipmentType;
	}

	/**
	 * @return the installedPartNumber
	 */
	public String getInstalledPartNumber() {
		return installedPartNumber;
	}

	/**
	 * @param installedPartNumber
	 *            the installedPartNumber to set
	 */
	public void setInstalledPartNumber(String installedPartNumber) {
		this.installedPartNumber = installedPartNumber;
	}

	/**
	 * @return the installedVendorPartNumber
	 */
	public String getInstalledVendorPartNumber() {
		return installedVendorPartNumber;
	}

	/**
	 * @param installedVendorPartNumber
	 *            the installedVendorPartNumber to set
	 */
	public void setInstalledVendorPartNumber(String installedVendorPartNumber) {
		this.installedVendorPartNumber = installedVendorPartNumber;
	}

	/**
	 * @return the installedVendorMaterialNumber
	 */
	public String getInstalledVendorMaterialNumber() {
		return installedVendorMaterialNumber;
	}

	/**
	 * @param installedVendorMaterialNumber
	 *            the installedVendorMaterialNumber to set
	 */
	public void setInstalledVendorMaterialNumber(String installedVendorMaterialNumber) {
		this.installedVendorMaterialNumber = installedVendorMaterialNumber;
	}

	/**
	 * @return the installedVersion
	 */
	public String getInstalledVersion() {
		return installedVersion;
	}

	/**
	 * @param installedVersion
	 *            the installedVersion to set
	 */
	public void setInstalledVersion(String installedVersion) {
		this.installedVersion = installedVersion;
	}

	/**
	 * @return the elementId
	 */
	public String getElementId() {
		return elementId;
	}

	/**
	 * @param elementId
	 *            the elementId to set
	 */
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	/**
	 * @return the chassis
	 */
	public String getChassis() {
		return chassis;
	}

	/**
	 * @param chassis
	 *            the chassis to set
	 */
	public void setChassis(String chassis) {
		this.chassis = chassis;
	}

	/**
	 * @return the installedModel
	 */
	public String getInstalledModel() {
		return installedModel;
	}

	/**
	 * @param installedModel
	 *            the installedModel to set
	 */
	public void setInstalledModel(String installedModel) {
		this.installedModel = installedModel;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @param locationName
	 *            the locationName to set
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
	 * @param userdefinedState
	 *            the userdefinedState to set
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
	 * @param administrativeState
	 *            the administrativeState to set
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
	 * @param operationalState
	 *            the operationalState to set
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
	 * @param hardwarePlateform
	 *            the hardwarePlateform to set
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
	 * @param pvFlag
	 *            the pvFlag to set
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
	 * @param vmId
	 *            the vmId to set
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
	 * @param exmoc
	 *            the exmoc to set
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
	 * @param subnetId
	 *            the subnetId to set
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
	 * @param assetTag
	 *            the assetTag to set
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
	 * @param info
	 *            the info to set
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
