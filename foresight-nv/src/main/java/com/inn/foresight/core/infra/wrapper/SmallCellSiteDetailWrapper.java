package com.inn.foresight.core.infra.wrapper;

import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.core.infra.utils.enums.Vendor;
@RestWrapper
public class SmallCellSiteDetailWrapper {
	private String address;
	private Double latitude;
	private Double longitude;
	private String buidlingrjid;
	private String neName;
	private String currentStage;
	private String siteName;
	private String siteCategory;
	private Vendor vendor;
	private Integer mcc;
	private Integer mnc;
	private String backHaulMedia;
	private String ContactName;
	private String ContactNumber;
	private String enodeBType;
	private Integer totalSMCCount;
	private Integer plannedSMCCount;
	private Integer onAirSMCCount;
	private String oamVlan;
	private String bearerVlan;
	private String signalVlan;
	private String emsLive;
	private String adminStatus;
	private Integer emsLiveCounter;
	private String towerType;
	private Double height;
	private String cellName;
	private String cabinetType;
	private Integer earfcn;
	private Integer enodeBId;
	private String ecgi;
	private Integer pci;
	private String txpower;
	private Integer azimuth;
	private Integer electricalTilt;
	private Integer mechTilt;
	private String enodeBPackage;
	private String trackingArea;
	private String siteId;
	private  String siteStatus;
	private  String smallCellStatus;
	private String antennaType;
	private Double antennaHeight;
	private String antennaModel;
	private Double VerticalBeamWidth;
	private Double horizontalBeamWidth;
	private String antennaGain;
	private String emsname;
	private String emsid;
	private String ip;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getBuidlingrjid() {
		return buidlingrjid;
	}
	public void setBuidlingrjid(String buidlingrjid) {
		this.buidlingrjid = buidlingrjid;
	}
	public String getNeName() {
		return neName;
	}
	public void setNeName(String neName) {
		this.neName = neName;
	}
	public String getCurrentStage() {
		return currentStage;
	}
	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteCategory() {
		return siteCategory;
	}
	public void setSiteCategory(String siteCategory) {
		this.siteCategory = siteCategory;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public Integer getMcc() {
		return mcc;
	}
	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}
	public Integer getMnc() {
		return mnc;
	}
	public void setMnc(Integer mnc) {
		this.mnc = mnc;
	}
	public String getBackHaulMedia() {
		return backHaulMedia;
	}
	public void setBackHaulMedia(String backHaulMedia) {
		this.backHaulMedia = backHaulMedia;
	}
	public String getContactName() {
		return ContactName;
	}
	public void setContactName(String contactName) {
		ContactName = contactName;
	}
	public String getContactNumber() {
		return ContactNumber;
	}
	public void setContactNumber(String contactNumber) {
		ContactNumber = contactNumber;
	}
	public String getEnodeBType() {
		return enodeBType;
	}
	public void setEnodeBType(String enodeBType) {
		this.enodeBType = enodeBType;
	}
	public Integer getTotalSMCCount() {
		return totalSMCCount;
	}
	public void setTotalSMCCount(Integer totalSMCCount) {
		this.totalSMCCount = totalSMCCount;
	}
	public Integer getPlannedSMCCount() {
		return plannedSMCCount;
	}
	public void setPlannedSMCCount(Integer plannedSMCCount) {
		this.plannedSMCCount = plannedSMCCount;
	}
	public Integer getOnAirSMCCount() {
		return onAirSMCCount;
	}
	public void setOnAirSMCCount(Integer onAirSMCCount) {
		this.onAirSMCCount = onAirSMCCount;
	}
	public String getOamVlan() {
		return oamVlan;
	}
	public void setOamVlan(String oamVlan) {
		this.oamVlan = oamVlan;
	}
	public String getBearerVlan() {
		return bearerVlan;
	}
	public void setBearerVlan(String bearerVlan) {
		this.bearerVlan = bearerVlan;
	}
	public String getSignalVlan() {
		return signalVlan;
	}
	public void setSignalVlan(String signalVlan) {
		this.signalVlan = signalVlan;
	}
	public String getEmsLive() {
		return emsLive;
	}
	public void setEmsLive(String emsLive) {
		this.emsLive = emsLive;
	}
	public String getAdminStatus() {
		return adminStatus;
	}
	public void setAdminStatus(String adminStatus) {
		this.adminStatus = adminStatus;
	}
	public Integer getEmsLiveCounter() {
		return emsLiveCounter;
	}
	public void setEmsLiveCounter(Integer emsLiveCounter) {
		this.emsLiveCounter = emsLiveCounter;
	}
	public String getTowerType() {
		return towerType;
	}
	public void setTowerType(String towerType) {
		this.towerType = towerType;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public String getCabinetType() {
		return cabinetType;
	}
	public void setCabinetType(String cabinetType) {
		this.cabinetType = cabinetType;
	}
	public Integer getEarfcn() {
		return earfcn;
	}
	public void setEarfcn(Integer earfcn) {
		this.earfcn = earfcn;
	}
	public Integer getEnodeBId() {
		return enodeBId;
	}
	public void setEnodeBId(Integer enodeBId) {
		this.enodeBId = enodeBId;
	}
	public String getEcgi() {
		return ecgi;
	}
	public void setEcgi(String ecgi) {
		this.ecgi = ecgi;
	}
	public Integer getPci() {
		return pci;
	}
	public void setPci(Integer pci) {
		this.pci = pci;
	}
	public Integer getAzimuth() {
		return azimuth;
	}
	public void setAzimuth(Integer azimuth) {
		this.azimuth = azimuth;
	}
	public Integer getElectricalTilt() {
		return electricalTilt;
	}
	public void setElectricalTilt(Integer electricalTilt) {
		this.electricalTilt = electricalTilt;
	}
	public Integer getMechTilt() {
		return mechTilt;
	}
	public void setMechTilt(Integer mechTilt) {
		this.mechTilt = mechTilt;
	}
	public String getEnodeBPackage() {
		return enodeBPackage;
	}
	public void setEnodeBPackage(String enodeBPackage) {
		this.enodeBPackage = enodeBPackage;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteStatus() {
		return siteStatus;
	}
	public void setSiteStatus(String siteStatus) {
		this.siteStatus = siteStatus;
	}
	public String getSmallCellStatus() {
		return smallCellStatus;
	}
	public void setSmallCellStatus(String smallCellStatus) {
		this.smallCellStatus = smallCellStatus;
	}
	public String getTxpower() {
		return txpower;
	}
	public void setTxpower(String txpower) {
		this.txpower = txpower;
	}
	public String getTrackingArea() {
		return trackingArea;
	}
	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}
	public String getAntennaType() {
		return antennaType;
	}
	public void setAntennaType(String antennaType) {
		this.antennaType = antennaType;
	}
	public Double getAntennaHeight() {
		return antennaHeight;
	}
	public void setAntennaHeight(Double antennaHeight) {
		this.antennaHeight = antennaHeight;
	}
	public String getAntennaModel() {
		return antennaModel;
	}
	public void setAntennaModel(String antennaModel) {
		this.antennaModel = antennaModel;
	}
	public Double getVerticalBeamWidth() {
		return VerticalBeamWidth;
	}
	public void setVerticalBeamWidth(Double verticalBeamWidth) {
		VerticalBeamWidth = verticalBeamWidth;
	}
	public Double getHorizontalBeamWidth() {
		return horizontalBeamWidth;
	}
	public void setHorizontalBeamWidth(Double horizontalBeamWidth) {
		this.horizontalBeamWidth = horizontalBeamWidth;
	}
	public String getAntennaGain() {
		return antennaGain;
	}
	public void setAntennaGain(String antennaGain) {
		this.antennaGain = antennaGain;
	}
	public String getEmsname() {
		return emsname;
	}
	public void setEmsname(String emsname) {
		this.emsname = emsname;
	}
	public String getEmsid() {
		return emsid;
	}
	public void setEmsid(String emsid) {
		this.emsid = emsid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Override
	public String toString() {
		return "SmallCellSiteDetailWrapper [address=" + address + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", buidlingrjid=" + buidlingrjid + ", neName=" + neName + ", currentStage=" + currentStage
				+ ", siteName=" + siteName + ", siteCategory=" + siteCategory + ", vendor=" + vendor + ", mcc=" + mcc
				+ ", mnc=" + mnc + ", backHaulMedia=" + backHaulMedia + ", ContactName=" + ContactName
				+ ", ContactNumber=" + ContactNumber + ", enodeBType=" + enodeBType + ", totalSMCCount=" + totalSMCCount
				+ ", plannedSMCCount=" + plannedSMCCount + ", onAirSMCCount=" + onAirSMCCount + ", oamVlan=" + oamVlan
				+ ", bearerVlan=" + bearerVlan + ", signalVlan=" + signalVlan + ", emsLive=" + emsLive
				+ ", adminStatus=" + adminStatus + ", emsLiveCounter=" + emsLiveCounter + ", towerType=" + towerType
				+ ", height=" + height + ", cellName=" + cellName + ", cabinetType=" + cabinetType + ", earfcn="
				+ earfcn + ", enodeBId=" + enodeBId + ", ecgi=" + ecgi + ", pci=" + pci + ", txpower=" + txpower
				+ ", azimuth=" + azimuth + ", electricalTilt=" + electricalTilt + ", mechTilt=" + mechTilt
				+ ", enodeBPackage=" + enodeBPackage + ", trackingArea=" + trackingArea + ", siteId=" + siteId
				+ ", siteStatus=" + siteStatus + ", smallCellStatus=" + smallCellStatus + ", antennaType=" + antennaType
				+ ", antennaHeight=" + antennaHeight + ", antennaModel=" + antennaModel + ", VerticalBeamWidth="
				+ VerticalBeamWidth + ", horizontalBeamWidth=" + horizontalBeamWidth + ", antennaGain=" + antennaGain
				+ ", emsname=" + emsname + ", emsid=" + emsid + ", ip=" + ip + "]";
	}
}
