package com.inn.foresight.module.nv.customercare.wrapper;

import java.io.Serializable;

import com.inn.commons.maps.LatLng;
import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class NVCustomerCareDataWrapper extends LatLng implements Serializable {
	private static final long serialVersionUID = -7040457804195365930L;
	/** Active & Passive Commons Fields. */
	private String key;
	private String recordType;
	private String imei;
	/** Both. */
	private Long capturedOn;
	/** Both. */
	private String gL1;
	/** Both. */
	private String gL2;
	/** Both. */
	private String gL3;
	/** Both. */
	private String gL4;
	/** Both. */
	private String date;
	/** Both. */
	private String androidId;
	/** Both. */
	private String deviceId;
	/** Both. */
//	private Double latitude;
//	/** Both. */
//	private Double longitude;
	/** Both. */
	private Integer mcc;
	/** Both. */
	private Integer mnc;
	/** Both. */
	private Integer cellId;
	/** Both. */
	private Integer cgi;
	/** Both. */
	private String band;
	/** Both. */
	private Integer pci;
	/** Both. */
	private Integer tac;
	/** Both. */
	private Integer psc;
	/** Both. */
	private Integer lac;
	/** Both. */
	private Integer avgRsrp;
	/** Both. */
	private Double avgSinr;
	/** Both. */
	private Integer avgRsrq;
	/** Both. */
	private String make;
	/** Both. */
	private String model;
	/** Both. */
	private String deviceOS;
	/** Both. */
	private String versionName;
	/** Both. */
	private Boolean dualSimEnable;
	/** Both. */
	private String buildNumber;
	/** Both. */
	private String eNodeB;
	/** Both. */
	private String bssid;
	/** Both. */
	private String batteryLevel;
	/** Both. */
	private Integer avgRssi;
	/** Both. */
	private Double avgDlRate;
	/** Both. */
	private Double avgUlRate;

	/** Both. */
	private String baseband;
	/** Both. */
	private String cpuUsage;
	/** Both. */
	private String memoryUsage;

	/** Active. */

	private String activeTestType;
	private String type;
	private Integer userid;
	private String starRating;
	private Integer minRsrp;
	private Integer maxRsrp;
	private Double minSinr;
	private Double maxSinr;
	private Integer minRsrq;
	private Integer maxRsrq;
	private Integer minRssi;
	private Integer maxRssi;
	private Integer minSnr;
	private Integer maxSnr;
	private Integer avgSnr;
	private Integer minRscp;
	private Integer maxRscp;
	private Integer avgRscp;
	private Integer minEcNo;
	private Integer maxEcNo;
	private Integer avgEcNo;
	private Integer minRxLevel;
	private Integer maxRxLevel;
	private Integer avgRxLevel;
	private Integer minRxquality;
	private Integer maxRxquality;
	private Integer avgRxquality;
	private Double minDlRate;
	private Double maxDlRate;

	private Double minUlRate;
	private Double maxUlRate;
	private Double minLatency;
	private Double maxLatency;
	private Double avgLatency;
	private Double jitter;
	private Double pcktLoss;
	private String clientOpName;
	private Double url1Time;
	private Double url2Time;
	private Double url3Time;
	private String address;
	private String userComment;
	private Boolean chargerConnected;
	private String voltage;
	private String temperature;
	private String nearestCity;
	private String nearestIP;
	private String destPingIpAddr;
	private String speedTestFailure;
	private String mobileNumber;
	private String isManualSync;
	private String enodeBVendor;
	private String enodeBType;
	private Boolean autoDateAndTime;
	private Integer earfcn;
	private String uarfcn;
	private String arfcn;
	private String deviceSerialNo;
	private String dataSimDetail;
	private String voiceSimDetail;
	private String gpsStatus;
	private String deviceIPV4Status;
	private String deviceIPV6Status;

	/** Passive. */

	private String networkType;
	private String networkSubtype;
	private String operatorName;
	private String connectionType;
	private String networkTypeWhenWifi;
	private String operatorNameWhenWiFi;
	private String collectionType;
	private Boolean gpsEnabled;
	private Double locAccuracy;
	private String barometerList;
	private Double altitude;
	private Integer frequency;
	private String macAddress;
	private String ssid;
	private Integer rssi;
	private Integer snr;
	private Integer _3gRscp;
	private Integer _3gEcno;
	private Integer _2Grxlevel;
	private Integer _2gRxQuality;
	private Double linkSpeed;
	private String nvModule;
	private String sdkVersion;
	private String batteryVoltage;
	private String batteryTemp;
	private String socModel;
	private String coreArch;
	private String fingerPrint;
	private String serialNo;
	private String chipSet;
	private Boolean isEnterprise;
	private Boolean autoDateTime;
	private String roamingStatus;
	private Boolean charging;
	private String chargingType;
	private String charger;
	private String profileId;
	private String profileStatus;
	private String simType;
	private String sim1;
	private String sim2;
	private String ipV4;
	private String ipV6;
	private String callDuration;
	private String callType;
	private String callEstStatus;
	private String callReleaseType;
	private Boolean isgwEnabled;
	private Boolean isDciEnabled;
	private String voiceNetworkType;
	private String voiceOperatorName;
	private Integer voiceMcc;
	private Integer voiceMnc;
	private Integer voiceCellId;
	private Integer voicePci;
	private Integer voiceTac;
	private Integer voiceLac;
	private Integer voicePsc;
	private Integer voiceRsrp;
	private Integer voiceRsrq;
	private Double voiceSinr;
	private Integer voiceRscp;
	private Integer voiceRssi;
	private Integer voiceEcno;
	private Integer voiceRxLevel;
	private Integer voiceRxQuality;

	/** Customer care keys. */
	private String band4G;
	private String btsCode;
	private String imageUrl;
	private String neStatus;
	private String lteCompatible;
	private String dualSimEnabled;
	private String firmwareVersion;
	private String deviceCompatiable;
	private String deviceFirmwareVersion;
	private String siteAddress;

	@Override
	public LatLng setLatitude(Double latitude) {
//		this.latitude = latitude;
		return super.setLatitude(latitude);
	}

	@Override
	public LatLng setLongitude(Double longitude) {
//		this.longitude = longitude;
		return super.setLongitude(longitude);
	}

	/**
	 * @return the gL1
	 */
	public String getgL1() {
		return gL1;
	}

	/**
	 * @param gL1
	 *            the gL1 to set
	 */
	public void setgL1(String gL1) {
		this.gL1 = gL1;
	}

	/**
	 * @return the gL2
	 */
	public String getgL2() {
		return gL2;
	}

	/**
	 * @param gL2
	 *            the gL2 to set
	 */
	public void setgL2(String gL2) {
		this.gL2 = gL2;
	}

	/**
	 * @return the gL3
	 */
	public String getgL3() {
		return gL3;
	}

	/**
	 * @param gL3
	 *            the gL3 to set
	 */
	public void setgL3(String gL3) {
		this.gL3 = gL3;
	}

	/**
	 * @return the gL4
	 */
	public String getgL4() {
		return gL4;
	}

	/**
	 * @param gL4
	 *            the gL4 to set
	 */
	public void setgL4(String gL4) {
		this.gL4 = gL4;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the androidId
	 */
	public String getAndroidId() {
		return androidId;
	}

	/**
	 * @param androidId
	 *            the androidId to set
	 */
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
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
	 * @return the cgi
	 */
	public Integer getCgi() {
		return cgi;
	}

	/**
	 * @param cgi
	 *            the cgi to set
	 */
	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}

	/**
	 * @return the band
	 */
	public String getBand() {
		return band;
	}

	/**
	 * @param band
	 *            the band to set
	 */
	public void setBand(String band) {
		this.band = band;
	}

	/**
	 * @return the speedTestFailure
	 */
	public String getSpeedTestFailure() {
		return speedTestFailure;
	}

	/**
	 * @param speedTestFailure
	 *            the speedTestFailure to set
	 */
	public void setSpeedTestFailure(String speedTestFailure) {
		this.speedTestFailure = speedTestFailure;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the isManualSync
	 */
	public String getIsManualSync() {
		return isManualSync;
	}

	/**
	 * @param isManualSync
	 *            the isManualSync to set
	 */
	public void setIsManualSync(String isManualSync) {
		this.isManualSync = isManualSync;
	}

	/**
	 * @return the enodeBVendor
	 */
	public String getEnodeBVendor() {
		return enodeBVendor;
	}

	/**
	 * @param enodeBVendor
	 *            the enodeBVendor to set
	 */
	public void setEnodeBVendor(String enodeBVendor) {
		this.enodeBVendor = enodeBVendor;
	}

	/**
	 * @return the enodeBType
	 */
	public String getEnodeBType() {
		return enodeBType;
	}

	/**
	 * @param enodeBType
	 *            the enodeBType to set
	 */
	public void setEnodeBType(String enodeBType) {
		this.enodeBType = enodeBType;
	}

	/**
	 * @return the earfcn
	 */
	public Integer getEarfcn() {
		return earfcn;
	}

	/**
	 * @param earfcn
	 *            the earfcn to set
	 */
	public void setEarfcn(Integer earfcn) {
		this.earfcn = earfcn;
	}

	/**
	 * @return the uarfcn
	 */
	public String getUarfcn() {
		return uarfcn;
	}

	/**
	 * @param uarfcn
	 *            the uarfcn to set
	 */
	public void setUarfcn(String uarfcn) {
		this.uarfcn = uarfcn;
	}

	/**
	 * @return the arfcn
	 */
	public String getArfcn() {
		return arfcn;
	}

	/**
	 * @param arfcn
	 *            the arfcn to set
	 */
	public void setArfcn(String arfcn) {
		this.arfcn = arfcn;
	}

	/**
	 * @return the capturedOn
	 */
	public Long getCapturedOn() {
		return capturedOn;
	}

	/**
	 * @param capturedOn
	 *            the capturedOn to set
	 */
	public void setCapturedOn(Long capturedOn) {
		this.capturedOn = capturedOn;
	}

	/**
	 * @return the networkType
	 */
	public String getNetworkType() {
		return networkType;
	}

	/**
	 * @param networkType
	 *            the networkType to set
	 */
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	/**
	 * @return the networkSubtype
	 */
	public String getNetworkSubtype() {
		return networkSubtype;
	}

	/**
	 * @param networkSubtype
	 *            the networkSubtype to set
	 */
	public void setNetworkSubtype(String networkSubtype) {
		this.networkSubtype = networkSubtype;
	}

	/**
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * @param operatorName
	 *            the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * @return the connectionType
	 */
	public String getConnectionType() {
		return connectionType;
	}

	/**
	 * @param connectionType
	 *            the connectionType to set
	 */
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	/**
	 * @return the networkTypeWhenWifi
	 */
	public String getNetworkTypeWhenWifi() {
		return networkTypeWhenWifi;
	}

	/**
	 * @param networkTypeWhenWifi
	 *            the networkTypeWhenWifi to set
	 */
	public void setNetworkTypeWhenWifi(String networkTypeWhenWifi) {
		this.networkTypeWhenWifi = networkTypeWhenWifi;
	}

	/**
	 * @return the operatorNameWhenWiFi
	 */
	public String getOperatorNameWhenWiFi() {
		return operatorNameWhenWiFi;
	}

	/**
	 * @param operatorNameWhenWiFi
	 *            the operatorNameWhenWiFi to set
	 */
	public void setOperatorNameWhenWiFi(String operatorNameWhenWiFi) {
		this.operatorNameWhenWiFi = operatorNameWhenWiFi;
	}

	/**
	 * @return the collectionType
	 */
	public String getCollectionType() {
		return collectionType;
	}

	/**
	 * @param collectionType
	 *            the collectionType to set
	 */
	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}

	/**
	 * @return the locAccuracy
	 */
	public Double getLocAccuracy() {
		return locAccuracy;
	}

	/**
	 * @param locAccuracy
	 *            the locAccuracy to set
	 */
	public void setLocAccuracy(Double locAccuracy) {
		this.locAccuracy = locAccuracy;
	}

	/**
	 * @return the barometerList
	 */
	public String getBarometerList() {
		return barometerList;
	}

	/**
	 * @param barometerList
	 *            the barometerList to set
	 */
	public void setBarometerList(String barometerList) {
		this.barometerList = barometerList;
	}

	/**
	 * @return the macAddress
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * @param macAddress
	 *            the macAddress to set
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	/**
	 * @return the ssid
	 */
	public String getSsid() {
		return ssid;
	}

	/**
	 * @param ssid
	 *            the ssid to set
	 */
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	/**
	 * @return the baseband
	 */
	public String getBaseband() {
		return baseband;
	}

	/**
	 * @param baseband
	 *            the baseband to set
	 */
	public void setBaseband(String baseband) {
		this.baseband = baseband;
	}

	/**
	 * @return the cpuUsage
	 */
	public String getCpuUsage() {
		return cpuUsage;
	}

	/**
	 * @param cpuUsage
	 *            the cpuUsage to set
	 */
	public void setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	/**
	 * @return the memoryUsage
	 */
	public String getMemoryUsage() {
		return memoryUsage;
	}

	/**
	 * @param memoryUsage
	 *            the memoryUsage to set
	 */
	public void setMemoryUsage(String memoryUsage) {
		this.memoryUsage = memoryUsage;
	}

	/**
	 * @return the batteryVoltage
	 */
	public String getBatteryVoltage() {
		return batteryVoltage;
	}

	/**
	 * @param batteryVoltage
	 *            the batteryVoltage to set
	 */
	public void setBatteryVoltage(String batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

	/**
	 * @return the batteryTemp
	 */
	public String getBatteryTemp() {
		return batteryTemp;
	}

	/**
	 * @param batteryTemp
	 *            the batteryTemp to set
	 */
	public void setBatteryTemp(String batteryTemp) {
		this.batteryTemp = batteryTemp;
	}

	/**
	 * @return the socModel
	 */
	public String getSocModel() {
		return socModel;
	}

	/**
	 * @param socModel
	 *            the socModel to set
	 */
	public void setSocModel(String socModel) {
		this.socModel = socModel;
	}

	/**
	 * @return the coreArch
	 */
	public String getCoreArch() {
		return coreArch;
	}

	/**
	 * @param coreArch
	 *            the coreArch to set
	 */
	public void setCoreArch(String coreArch) {
		this.coreArch = coreArch;
	}

	/**
	 * @return the fingerPrint
	 */
	public String getFingerPrint() {
		return fingerPrint;
	}

	/**
	 * @param fingerPrint
	 *            the fingerPrint to set
	 */
	public void setFingerPrint(String fingerPrint) {
		this.fingerPrint = fingerPrint;
	}

	/**
	 * @return the serialNo
	 */
	public String getSerialNo() {
		return serialNo;
	}

	/**
	 * @param serialNo
	 *            the serialNo to set
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	/**
	 * @return the chipSet
	 */
	public String getChipSet() {
		return chipSet;
	}

	/**
	 * @param chipSet
	 *            the chipSet to set
	 */
	public void setChipSet(String chipSet) {
		this.chipSet = chipSet;
	}

	/**
	 * @return the roamingStatus
	 */
	public String getRoamingStatus() {
		return roamingStatus;
	}

	/**
	 * @param roamingStatus
	 *            the roamingStatus to set
	 */
	public void setRoamingStatus(String roamingStatus) {
		this.roamingStatus = roamingStatus;
	}

	/**
	 * @return the chargingType
	 */
	public String getChargingType() {
		return chargingType;
	}

	/**
	 * @param chargingType
	 *            the chargingType to set
	 */
	public void setChargingType(String chargingType) {
		this.chargingType = chargingType;
	}

	/**
	 * @return the charger
	 */
	public String getCharger() {
		return charger;
	}

	/**
	 * @param charger
	 *            the charger to set
	 */
	public void setCharger(String charger) {
		this.charger = charger;
	}

	/**
	 * @return the profileId
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId
	 *            the profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the profileStatus
	 */
	public String getProfileStatus() {
		return profileStatus;
	}

	/**
	 * @param profileStatus
	 *            the profileStatus to set
	 */
	public void setProfileStatus(String profileStatus) {
		this.profileStatus = profileStatus;
	}

	/**
	 * @return the simType
	 */
	public String getSimType() {
		return simType;
	}

	/**
	 * @param simType
	 *            the simType to set
	 */
	public void setSimType(String simType) {
		this.simType = simType;
	}

	/**
	 * @return the sim1
	 */
	public String getSim1() {
		return sim1;
	}

	/**
	 * @param sim1
	 *            the sim1 to set
	 */
	public void setSim1(String sim1) {
		this.sim1 = sim1;
	}

	/**
	 * @return the sim2
	 */
	public String getSim2() {
		return sim2;
	}

	/**
	 * @param sim2
	 *            the sim2 to set
	 */
	public void setSim2(String sim2) {
		this.sim2 = sim2;
	}

	/**
	 * @return the ipV4
	 */
	public String getIpV4() {
		return ipV4;
	}

	/**
	 * @param ipV4
	 *            the ipV4 to set
	 */
	public void setIpV4(String ipV4) {
		this.ipV4 = ipV4;
	}

	/**
	 * @return the ipV6
	 */
	public String getIpV6() {
		return ipV6;
	}

	/**
	 * @param ipV6
	 *            the ipV6 to set
	 */
	public void setIpV6(String ipV6) {
		this.ipV6 = ipV6;
	}

	/**
	 * @return the callDuration
	 */
	public String getCallDuration() {
		return callDuration;
	}

	/**
	 * @param callDuration
	 *            the callDuration to set
	 */
	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}

	/**
	 * @return the callType
	 */
	public String getCallType() {
		return callType;
	}

	/**
	 * @param callType
	 *            the callType to set
	 */
	public void setCallType(String callType) {
		this.callType = callType;
	}

	/**
	 * @return the callEstStatus
	 */
	public String getCallEstStatus() {
		return callEstStatus;
	}

	/**
	 * @param callEstStatus
	 *            the callEstStatus to set
	 */
	public void setCallEstStatus(String callEstStatus) {
		this.callEstStatus = callEstStatus;
	}

	/**
	 * @return the callReleaseType
	 */
	public String getCallReleaseType() {
		return callReleaseType;
	}

	/**
	 * @param callReleaseType
	 *            the callReleaseType to set
	 */
	public void setCallReleaseType(String callReleaseType) {
		this.callReleaseType = callReleaseType;
	}

	/**
	 * @return the voiceNetworkType
	 */
	public String getVoiceNetworkType() {
		return voiceNetworkType;
	}

	/**
	 * @param voiceNetworkType
	 *            the voiceNetworkType to set
	 */
	public void setVoiceNetworkType(String voiceNetworkType) {
		this.voiceNetworkType = voiceNetworkType;
	}

	/**
	 * @return the voiceOperatorName
	 */
	public String getVoiceOperatorName() {
		return voiceOperatorName;
	}

	/**
	 * @param voiceOperatorName
	 *            the voiceOperatorName to set
	 */
	public void setVoiceOperatorName(String voiceOperatorName) {
		this.voiceOperatorName = voiceOperatorName;
	}

	/**
	 * @return the latitude
	 */
	@Override
	public Double getLatitude() {
		return latitude;
	}


	/**
	 * @return the longitude
	 */
	@Override
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @return the mcc
	 */
	public Integer getMcc() {
		return mcc;
	}

	/**
	 * @param mcc
	 *            the mcc to set
	 */
	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}

	/**
	 * @return the mnc
	 */
	public Integer getMnc() {
		return mnc;
	}

	/**
	 * @param mnc
	 *            the mnc to set
	 */
	public void setMnc(Integer mnc) {
		this.mnc = mnc;
	}

	/**
	 * @return the cellId
	 */
	public Integer getCellId() {
		return cellId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	/**
	 * @return the pci
	 */
	public Integer getPci() {
		return pci;
	}

	/**
	 * @param pci
	 *            the pci to set
	 */
	public void setPci(Integer pci) {
		this.pci = pci;
	}

	/**
	 * @return the tac
	 */
	public Integer getTac() {
		return tac;
	}

	/**
	 * @param tac
	 *            the tac to set
	 */
	public void setTac(Integer tac) {
		this.tac = tac;
	}

	/**
	 * @return the psc
	 */
	public Integer getPsc() {
		return psc;
	}

	/**
	 * @param psc
	 *            the psc to set
	 */
	public void setPsc(Integer psc) {
		this.psc = psc;
	}

	/**
	 * @return the lac
	 */
	public Integer getLac() {
		return lac;
	}

	/**
	 * @param lac
	 *            the lac to set
	 */
	public void setLac(Integer lac) {
		this.lac = lac;
	}

	/**
	 * @return the avgRsrp
	 */
	public Integer getAvgRsrp() {
		return avgRsrp;
	}

	/**
	 * @param avgRsrp
	 *            the avgRsrp to set
	 */
	public void setAvgRsrp(Integer avgRsrp) {
		this.avgRsrp = avgRsrp;
	}

	/**
	 * @return the avgSinr
	 */
	public Double getAvgSinr() {
		return avgSinr;
	}

	/**
	 * @param avgSinr
	 *            the avgSinr to set
	 */
	public void setAvgSinr(Double avgSinr) {
		this.avgSinr = avgSinr;
	}

	/**
	 * @return the avgRsrq
	 */
	public Integer getAvgRsrq() {
		return avgRsrq;
	}

	/**
	 * @param avgRsrq
	 *            the avgRsrq to set
	 */
	public void setAvgRsrq(Integer avgRsrq) {
		this.avgRsrq = avgRsrq;
	}

	/**
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * @param make
	 *            the make to set
	 */
	public void setMake(String make) {
		this.make = make;
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
	 * @return the deviceOS
	 */
	public String getDeviceOS() {
		return deviceOS;
	}

	/**
	 * @param deviceOS
	 *            the deviceOS to set
	 */
	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}

	/**
	 * @return the versionName
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * @param versionName
	 *            the versionName to set
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * @return the buildNumber
	 */
	public String getBuildNumber() {
		return buildNumber;
	}

	/**
	 * @param buildNumber
	 *            the buildNumber to set
	 */
	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	/**
	 * @return the eNodeB
	 */
	public String geteNodeB() {
		return eNodeB;
	}

	/**
	 * @param eNodeB
	 *            the eNodeB to set
	 */
	public void seteNodeB(String eNodeB) {
		this.eNodeB = eNodeB;
	}

	/**
	 * @return the bssid
	 */
	public String getBssid() {
		return bssid;
	}

	/**
	 * @param bssid
	 *            the bssid to set
	 */
	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	/**
	 * @return the batteryLevel
	 */
	public String getBatteryLevel() {
		return batteryLevel;
	}

	/**
	 * @param batteryLevel
	 *            the batteryLevel to set
	 */
	public void setBatteryLevel(String batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	/**
	 * @return the avgRssi
	 */
	public Integer getAvgRssi() {
		return avgRssi;
	}

	/**
	 * @param avgRssi
	 *            the avgRssi to set
	 */
	public void setAvgRssi(Integer avgRssi) {
		this.avgRssi = avgRssi;
	}

	/**
	 * @return the avgDlRate
	 */
	public Double getAvgDlRate() {
		return avgDlRate;
	}

	/**
	 * @param avgDlRate
	 *            the avgDlRate to set
	 */
	public void setAvgDlRate(Double avgDlRate) {
		this.avgDlRate = avgDlRate;
	}

	/**
	 * @return the avgUlRate
	 */
	public Double getAvgUlRate() {
		return avgUlRate;
	}

	/**
	 * @param avgUlRate
	 *            the avgUlRate to set
	 */
	public void setAvgUlRate(Double avgUlRate) {
		this.avgUlRate = avgUlRate;
	}

	/**
	 * @return the activeTestType
	 */
	public String getActiveTestType() {
		return activeTestType;
	}

	/**
	 * @param activeTestType
	 *            the activeTestType to set
	 */
	public void setActiveTestType(String activeTestType) {
		this.activeTestType = activeTestType;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the userid
	 */
	public Integer getUserid() {
		return userid;
	}

	/**
	 * @param userid
	 *            the userid to set
	 */
	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	/**
	 * @return the starRating
	 */
	public String getStarRating() {
		return starRating;
	}

	/**
	 * @param starRating
	 *            the starRating to set
	 */
	public void setStarRating(String starRating) {
		this.starRating = starRating;
	}

	/**
	 * @return the minRsrp
	 */
	public Integer getMinRsrp() {
		return minRsrp;
	}

	/**
	 * @param minRsrp
	 *            the minRsrp to set
	 */
	public void setMinRsrp(Integer minRsrp) {
		this.minRsrp = minRsrp;
	}

	/**
	 * @return the maxRsrp
	 */
	public Integer getMaxRsrp() {
		return maxRsrp;
	}

	/**
	 * @param maxRsrp
	 *            the maxRsrp to set
	 */
	public void setMaxRsrp(Integer maxRsrp) {
		this.maxRsrp = maxRsrp;
	}

	/**
	 * @return the minSinr
	 */
	public Double getMinSinr() {
		return minSinr;
	}

	/**
	 * @param minSinr
	 *            the minSinr to set
	 */
	public void setMinSinr(Double minSinr) {
		this.minSinr = minSinr;
	}

	/**
	 * @return the maxSinr
	 */
	public Double getMaxSinr() {
		return maxSinr;
	}

	/**
	 * @param maxSinr
	 *            the maxSinr to set
	 */
	public void setMaxSinr(Double maxSinr) {
		this.maxSinr = maxSinr;
	}

	/**
	 * @return the minRsrq
	 */
	public Integer getMinRsrq() {
		return minRsrq;
	}

	/**
	 * @param minRsrq
	 *            the minRsrq to set
	 */
	public void setMinRsrq(Integer minRsrq) {
		this.minRsrq = minRsrq;
	}

	/**
	 * @return the maxRsrq
	 */
	public Integer getMaxRsrq() {
		return maxRsrq;
	}

	/**
	 * @param maxRsrq
	 *            the maxRsrq to set
	 */
	public void setMaxRsrq(Integer maxRsrq) {
		this.maxRsrq = maxRsrq;
	}

	/**
	 * @return the minRssi
	 */
	public Integer getMinRssi() {
		return minRssi;
	}

	/**
	 * @param minRssi
	 *            the minRssi to set
	 */
	public void setMinRssi(Integer minRssi) {
		this.minRssi = minRssi;
	}

	/**
	 * @return the maxRssi
	 */
	public Integer getMaxRssi() {
		return maxRssi;
	}

	/**
	 * @param maxRssi
	 *            the maxRssi to set
	 */
	public void setMaxRssi(Integer maxRssi) {
		this.maxRssi = maxRssi;
	}

	/**
	 * @return the minSnr
	 */
	public Integer getMinSnr() {
		return minSnr;
	}

	/**
	 * @param minSnr
	 *            the minSnr to set
	 */
	public void setMinSnr(Integer minSnr) {
		this.minSnr = minSnr;
	}

	/**
	 * @return the maxSnr
	 */
	public Integer getMaxSnr() {
		return maxSnr;
	}

	/**
	 * @param maxSnr
	 *            the maxSnr to set
	 */
	public void setMaxSnr(Integer maxSnr) {
		this.maxSnr = maxSnr;
	}

	/**
	 * @return the avgSnr
	 */
	public Integer getAvgSnr() {
		return avgSnr;
	}

	/**
	 * @param avgSnr
	 *            the avgSnr to set
	 */
	public void setAvgSnr(Integer avgSnr) {
		this.avgSnr = avgSnr;
	}

	/**
	 * @return the minRscp
	 */
	public Integer getMinRscp() {
		return minRscp;
	}

	/**
	 * @param minRscp
	 *            the minRscp to set
	 */
	public void setMinRscp(Integer minRscp) {
		this.minRscp = minRscp;
	}

	/**
	 * @return the maxRscp
	 */
	public Integer getMaxRscp() {
		return maxRscp;
	}

	/**
	 * @param maxRscp
	 *            the maxRscp to set
	 */
	public void setMaxRscp(Integer maxRscp) {
		this.maxRscp = maxRscp;
	}

	/**
	 * @return the avgRscp
	 */
	public Integer getAvgRscp() {
		return avgRscp;
	}

	/**
	 * @param avgRscp
	 *            the avgRscp to set
	 */
	public void setAvgRscp(Integer avgRscp) {
		this.avgRscp = avgRscp;
	}

	/**
	 * @return the minEcNo
	 */
	public Integer getMinEcNo() {
		return minEcNo;
	}

	/**
	 * @param minEcNo
	 *            the minEcNo to set
	 */
	public void setMinEcNo(Integer minEcNo) {
		this.minEcNo = minEcNo;
	}

	/**
	 * @return the maxEcNo
	 */
	public Integer getMaxEcNo() {
		return maxEcNo;
	}

	/**
	 * @param maxEcNo
	 *            the maxEcNo to set
	 */
	public void setMaxEcNo(Integer maxEcNo) {
		this.maxEcNo = maxEcNo;
	}

	/**
	 * @return the avgEcNo
	 */
	public Integer getAvgEcNo() {
		return avgEcNo;
	}

	/**
	 * @param avgEcNo
	 *            the avgEcNo to set
	 */
	public void setAvgEcNo(Integer avgEcNo) {
		this.avgEcNo = avgEcNo;
	}

	/**
	 * @return the minRxLevel
	 */
	public Integer getMinRxLevel() {
		return minRxLevel;
	}

	/**
	 * @param minRxLevel
	 *            the minRxLevel to set
	 */
	public void setMinRxLevel(Integer minRxLevel) {
		this.minRxLevel = minRxLevel;
	}

	/**
	 * @return the maxRxLevel
	 */
	public Integer getMaxRxLevel() {
		return maxRxLevel;
	}

	/**
	 * @param maxRxLevel
	 *            the maxRxLevel to set
	 */
	public void setMaxRxLevel(Integer maxRxLevel) {
		this.maxRxLevel = maxRxLevel;
	}

	/**
	 * @return the avgRxLevel
	 */
	public Integer getAvgRxLevel() {
		return avgRxLevel;
	}

	/**
	 * @param avgRxLevel
	 *            the avgRxLevel to set
	 */
	public void setAvgRxLevel(Integer avgRxLevel) {
		this.avgRxLevel = avgRxLevel;
	}

	/**
	 * @return the minRxquality
	 */
	public Integer getMinRxquality() {
		return minRxquality;
	}

	/**
	 * @param minRxquality
	 *            the minRxquality to set
	 */
	public void setMinRxquality(Integer minRxquality) {
		this.minRxquality = minRxquality;
	}

	/**
	 * @return the maxRxquality
	 */
	public Integer getMaxRxquality() {
		return maxRxquality;
	}

	/**
	 * @param maxRxquality
	 *            the maxRxquality to set
	 */
	public void setMaxRxquality(Integer maxRxquality) {
		this.maxRxquality = maxRxquality;
	}

	/**
	 * @return the avgRxquality
	 */
	public Integer getAvgRxquality() {
		return avgRxquality;
	}

	/**
	 * @param avgRxquality
	 *            the avgRxquality to set
	 */
	public void setAvgRxquality(Integer avgRxquality) {
		this.avgRxquality = avgRxquality;
	}

	/**
	 * @return the minDlRate
	 */
	public Double getMinDlRate() {
		return minDlRate;
	}

	/**
	 * @param minDlRate
	 *            the minDlRate to set
	 */
	public void setMinDlRate(Double minDlRate) {
		this.minDlRate = minDlRate;
	}

	/**
	 * @return the maxDlRate
	 */
	public Double getMaxDlRate() {
		return maxDlRate;
	}

	/**
	 * @param maxDlRate
	 *            the maxDlRate to set
	 */
	public void setMaxDlRate(Double maxDlRate) {
		this.maxDlRate = maxDlRate;
	}

	/**
	 * @return the minUlRate
	 */
	public Double getMinUlRate() {
		return minUlRate;
	}

	/**
	 * @param minUlRate
	 *            the minUlRate to set
	 */
	public void setMinUlRate(Double minUlRate) {
		this.minUlRate = minUlRate;
	}

	/**
	 * @return the maxUlRate
	 */
	public Double getMaxUlRate() {
		return maxUlRate;
	}

	/**
	 * @param maxUlRate
	 *            the maxUlRate to set
	 */
	public void setMaxUlRate(Double maxUlRate) {
		this.maxUlRate = maxUlRate;
	}

	/**
	 * @return the minLatency
	 */
	public Double getMinLatency() {
		return minLatency;
	}

	/**
	 * @param minLatency
	 *            the minLatency to set
	 */
	public void setMinLatency(Double minLatency) {
		this.minLatency = minLatency;
	}

	/**
	 * @return the maxLatency
	 */
	public Double getMaxLatency() {
		return maxLatency;
	}

	/**
	 * @param maxLatency
	 *            the maxLatency to set
	 */
	public void setMaxLatency(Double maxLatency) {
		this.maxLatency = maxLatency;
	}

	/**
	 * @return the avgLatency
	 */
	public Double getAvgLatency() {
		return avgLatency;
	}

	/**
	 * @param avgLatency
	 *            the avgLatency to set
	 */
	public void setAvgLatency(Double avgLatency) {
		this.avgLatency = avgLatency;
	}

	/**
	 * @return the jitter
	 */
	public Double getJitter() {
		return jitter;
	}

	/**
	 * @param jitter
	 *            the jitter to set
	 */
	public void setJitter(Double jitter) {
		this.jitter = jitter;
	}

	/**
	 * @return the pcktLoss
	 */
	public Double getPcktLoss() {
		return pcktLoss;
	}

	/**
	 * @param pcktLoss
	 *            the pcktLoss to set
	 */
	public void setPcktLoss(Double pcktLoss) {
		this.pcktLoss = pcktLoss;
	}

	/**
	 * @return the clientOpName
	 */
	public String getClientOpName() {
		return clientOpName;
	}

	/**
	 * @param clientOpName
	 *            the clientOpName to set
	 */
	public void setClientOpName(String clientOpName) {
		this.clientOpName = clientOpName;
	}

	/**
	 * @return the url1Time
	 */
	public Double getUrl1Time() {
		return url1Time;
	}

	/**
	 * @param url1Time
	 *            the url1Time to set
	 */
	public void setUrl1Time(Double url1Time) {
		this.url1Time = url1Time;
	}

	/**
	 * @return the url2Time
	 */
	public Double getUrl2Time() {
		return url2Time;
	}

	/**
	 * @param url2Time
	 *            the url2Time to set
	 */
	public void setUrl2Time(Double url2Time) {
		this.url2Time = url2Time;
	}

	/**
	 * @return the url3Time
	 */
	public Double getUrl3Time() {
		return url3Time;
	}

	/**
	 * @param url3Time
	 *            the url3Time to set
	 */
	public void setUrl3Time(Double url3Time) {
		this.url3Time = url3Time;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the userComment
	 */
	public String getUserComment() {
		return userComment;
	}

	/**
	 * @param userComment
	 *            the userComment to set
	 */
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	/**
	 * @return the voltage
	 */
	public String getVoltage() {
		return voltage;
	}

	/**
	 * @param voltage
	 *            the voltage to set
	 */
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	/**
	 * @return the temperature
	 */
	public String getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature
	 *            the temperature to set
	 */
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the nearestCity
	 */
	public String getNearestCity() {
		return nearestCity;
	}

	/**
	 * @param nearestCity
	 *            the nearestCity to set
	 */
	public void setNearestCity(String nearestCity) {
		this.nearestCity = nearestCity;
	}

	/**
	 * @return the nearestIP
	 */
	public String getNearestIP() {
		return nearestIP;
	}

	/**
	 * @param nearestIP
	 *            the nearestIP to set
	 */
	public void setNearestIP(String nearestIP) {
		this.nearestIP = nearestIP;
	}

	/**
	 * @return the destPingIpAddr
	 */
	public String getDestPingIpAddr() {
		return destPingIpAddr;
	}

	/**
	 * @param destPingIpAddr
	 *            the destPingIpAddr to set
	 */
	public void setDestPingIpAddr(String destPingIpAddr) {
		this.destPingIpAddr = destPingIpAddr;
	}

	/**
	 * @return the altitude
	 */
	public Double getAltitude() {
		return altitude;
	}

	/**
	 * @param altitude
	 *            the altitude to set
	 */
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	/**
	 * @return the frequency
	 */
	public Integer getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency
	 *            the frequency to set
	 */
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the rssi
	 */
	public Integer getRssi() {
		return rssi;
	}

	/**
	 * @param rssi
	 *            the rssi to set
	 */
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}

	/**
	 * @return the snr
	 */
	public Integer getSnr() {
		return snr;
	}

	/**
	 * @param snr
	 *            the snr to set
	 */
	public void setSnr(Integer snr) {
		this.snr = snr;
	}

	/**
	 * @return the _3gRscp
	 */
	public Integer get_3gRscp() {
		return _3gRscp;
	}

	/**
	 * @param _3gRscp
	 *            the _3gRscp to set
	 */
	public void set_3gRscp(Integer _3gRscp) {
		this._3gRscp = _3gRscp;
	}

	/**
	 * @return the _3gEcno
	 */
	public Integer get_3gEcno() {
		return _3gEcno;
	}

	/**
	 * @param _3gEcno
	 *            the _3gEcno to set
	 */
	public void set_3gEcno(Integer _3gEcno) {
		this._3gEcno = _3gEcno;
	}

	/**
	 * @return the _2Grxlevel
	 */
	public Integer get_2Grxlevel() {
		return _2Grxlevel;
	}

	/**
	 * @param _2Grxlevel
	 *            the _2Grxlevel to set
	 */
	public void set_2Grxlevel(Integer _2Grxlevel) {
		this._2Grxlevel = _2Grxlevel;
	}

	/**
	 * @return the _2gRxQuality
	 */
	public Integer get_2gRxQuality() {
		return _2gRxQuality;
	}

	/**
	 * @param _2gRxQuality
	 *            the _2gRxQuality to set
	 */
	public void set_2gRxQuality(Integer _2gRxQuality) {
		this._2gRxQuality = _2gRxQuality;
	}

	/**
	 * @return the linkSpeed
	 */
	public Double getLinkSpeed() {
		return linkSpeed;
	}

	/**
	 * @param linkSpeed
	 *            the linkSpeed to set
	 */
	public void setLinkSpeed(Double linkSpeed) {
		this.linkSpeed = linkSpeed;
	}

	/**
	 * @return the nvModule
	 */
	public String getNvModule() {
		return nvModule;
	}

	/**
	 * @param nvModule
	 *            the nvModule to set
	 */
	public void setNvModule(String nvModule) {
		this.nvModule = nvModule;
	}

	/**
	 * @return the sdkVersion
	 */
	public String getSdkVersion() {
		return sdkVersion;
	}

	/**
	 * @param sdkVersion
	 *            the sdkVersion to set
	 */
	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	/**
	 * @return the voiceMcc
	 */
	public Integer getVoiceMcc() {
		return voiceMcc;
	}

	/**
	 * @param voiceMcc
	 *            the voiceMcc to set
	 */
	public void setVoiceMcc(Integer voiceMcc) {
		this.voiceMcc = voiceMcc;
	}

	/**
	 * @return the voiceMnc
	 */
	public Integer getVoiceMnc() {
		return voiceMnc;
	}

	/**
	 * @param voiceMnc
	 *            the voiceMnc to set
	 */
	public void setVoiceMnc(Integer voiceMnc) {
		this.voiceMnc = voiceMnc;
	}

	/**
	 * @return the voiceCellId
	 */
	public Integer getVoiceCellId() {
		return voiceCellId;
	}

	/**
	 * @param voiceCellId
	 *            the voiceCellId to set
	 */
	public void setVoiceCellId(Integer voiceCellId) {
		this.voiceCellId = voiceCellId;
	}

	/**
	 * @return the voicePci
	 */
	public Integer getVoicePci() {
		return voicePci;
	}

	/**
	 * @param voicePci
	 *            the voicePci to set
	 */
	public void setVoicePci(Integer voicePci) {
		this.voicePci = voicePci;
	}

	/**
	 * @return the voiceTac
	 */
	public Integer getVoiceTac() {
		return voiceTac;
	}

	/**
	 * @param voiceTac
	 *            the voiceTac to set
	 */
	public void setVoiceTac(Integer voiceTac) {
		this.voiceTac = voiceTac;
	}

	/**
	 * @return the voiceLac
	 */
	public Integer getVoiceLac() {
		return voiceLac;
	}

	/**
	 * @param voiceLac
	 *            the voiceLac to set
	 */
	public void setVoiceLac(Integer voiceLac) {
		this.voiceLac = voiceLac;
	}

	/**
	 * @return the voicePsc
	 */
	public Integer getVoicePsc() {
		return voicePsc;
	}

	/**
	 * @param voicePsc
	 *            the voicePsc to set
	 */
	public void setVoicePsc(Integer voicePsc) {
		this.voicePsc = voicePsc;
	}

	/**
	 * @return the voiceRsrp
	 */
	public Integer getVoiceRsrp() {
		return voiceRsrp;
	}

	/**
	 * @param voiceRsrp
	 *            the voiceRsrp to set
	 */
	public void setVoiceRsrp(Integer voiceRsrp) {
		this.voiceRsrp = voiceRsrp;
	}

	/**
	 * @return the voiceRsrq
	 */
	public Integer getVoiceRsrq() {
		return voiceRsrq;
	}

	/**
	 * @param voiceRsrq
	 *            the voiceRsrq to set
	 */
	public void setVoiceRsrq(Integer voiceRsrq) {
		this.voiceRsrq = voiceRsrq;
	}

	/**
	 * @return the voiceSinr
	 */
	public Double getVoiceSinr() {
		return voiceSinr;
	}

	/**
	 * @param voiceSinr
	 *            the voiceSinr to set
	 */
	public void setVoiceSinr(Double voiceSinr) {
		this.voiceSinr = voiceSinr;
	}

	/**
	 * @return the voiceRscp
	 */
	public Integer getVoiceRscp() {
		return voiceRscp;
	}

	/**
	 * @param voiceRscp
	 *            the voiceRscp to set
	 */
	public void setVoiceRscp(Integer voiceRscp) {
		this.voiceRscp = voiceRscp;
	}

	/**
	 * @return the voiceRssi
	 */
	public Integer getVoiceRssi() {
		return voiceRssi;
	}

	/**
	 * @param voiceRssi
	 *            the voiceRssi to set
	 */
	public void setVoiceRssi(Integer voiceRssi) {
		this.voiceRssi = voiceRssi;
	}

	/**
	 * @return the voiceEcno
	 */
	public Integer getVoiceEcno() {
		return voiceEcno;
	}

	/**
	 * @param voiceEcno
	 *            the voiceEcno to set
	 */
	public void setVoiceEcno(Integer voiceEcno) {
		this.voiceEcno = voiceEcno;
	}

	/**
	 * @return the voiceRxLevel
	 */
	public Integer getVoiceRxLevel() {
		return voiceRxLevel;
	}

	/**
	 * @param voiceRxLevel
	 *            the voiceRxLevel to set
	 */
	public void setVoiceRxLevel(Integer voiceRxLevel) {
		this.voiceRxLevel = voiceRxLevel;
	}

	/**
	 * @return the voiceRxQuality
	 */
	public Integer getVoiceRxQuality() {
		return voiceRxQuality;
	}

	/**
	 * @param voiceRxQuality
	 *            the voiceRxQuality to set
	 */
	public void setVoiceRxQuality(Integer voiceRxQuality) {
		this.voiceRxQuality = voiceRxQuality;
	}

	/**
	 * @return the dualSimEnable
	 */
	public Boolean getDualSimEnable() {
		return dualSimEnable;
	}

	/**
	 * @param dualSimEnable
	 *            the dualSimEnable to set
	 */
	public void setDualSimEnable(Boolean dualSimEnable) {
		this.dualSimEnable = dualSimEnable;
	}

	/**
	 * @return the chargerConnected
	 */
	public Boolean getChargerConnected() {
		return chargerConnected;
	}

	/**
	 * @param chargerConnected
	 *            the chargerConnected to set
	 */
	public void setChargerConnected(Boolean chargerConnected) {
		this.chargerConnected = chargerConnected;
	}

	/**
	 * @return the autoDateAndTime
	 */
	public Boolean getAutoDateAndTime() {
		return autoDateAndTime;
	}

	/**
	 * @param autoDateAndTime
	 *            the autoDateAndTime to set
	 */
	public void setAutoDateAndTime(Boolean autoDateAndTime) {
		this.autoDateAndTime = autoDateAndTime;
	}

	/**
	 * @return the gpsEnabled
	 */
	public Boolean getGpsEnabled() {
		return gpsEnabled;
	}

	/**
	 * @param gpsEnabled
	 *            the gpsEnabled to set
	 */
	public void setGpsEnabled(Boolean gpsEnabled) {
		this.gpsEnabled = gpsEnabled;
	}

	/**
	 * @return the isEnterprise
	 */
	public Boolean getIsEnterprise() {
		return isEnterprise;
	}

	/**
	 * @param isEnterprise
	 *            the isEnterprise to set
	 */
	public void setIsEnterprise(Boolean isEnterprise) {
		this.isEnterprise = isEnterprise;
	}

	/**
	 * @return the autoDateTime
	 */
	public Boolean getAutoDateTime() {
		return autoDateTime;
	}

	/**
	 * @param autoDateTime
	 *            the autoDateTime to set
	 */
	public void setAutoDateTime(Boolean autoDateTime) {
		this.autoDateTime = autoDateTime;
	}

	/**
	 * @return the charging
	 */
	public Boolean getCharging() {
		return charging;
	}

	/**
	 * @param charging
	 *            the charging to set
	 */
	public void setCharging(Boolean charging) {
		this.charging = charging;
	}

	/**
	 * @return the isgwEnabled
	 */
	public Boolean getIsgwEnabled() {
		return isgwEnabled;
	}

	/**
	 * @param isgwEnabled
	 *            the isgwEnabled to set
	 */
	public void setIsgwEnabled(Boolean isgwEnabled) {
		this.isgwEnabled = isgwEnabled;
	}

	/**
	 * @return the isDciEnabled
	 */
	public Boolean getIsDciEnabled() {
		return isDciEnabled;
	}

	/**
	 * @param isDciEnabled
	 *            the isDciEnabled to set
	 */
	public void setIsDciEnabled(Boolean isDciEnabled) {
		this.isDciEnabled = isDciEnabled;
	}

	/**
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * @param imei
	 *            the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * @return the recordType
	 */
	public String getRecordType() {
		return recordType;
	}

	/**
	 * @param recordType
	 *            the recordType to set
	 */
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the deviceSerialNo
	 */
	public String getDeviceSerialNo() {
		return deviceSerialNo;
	}

	/**
	 * @param deviceSerialNo
	 *            the deviceSerialNo to set
	 */
	public void setDeviceSerialNo(String deviceSerialNo) {
		this.deviceSerialNo = deviceSerialNo;
	}

	/**
	 * @return the dataSimDetail
	 */
	public String getDataSimDetail() {
		return dataSimDetail;
	}

	/**
	 * @param dataSimDetail
	 *            the dataSimDetail to set
	 */
	public void setDataSimDetail(String dataSimDetail) {
		this.dataSimDetail = dataSimDetail;
	}

	/**
	 * @return the voiceSimDetail
	 */
	public String getVoiceSimDetail() {
		return voiceSimDetail;
	}

	/**
	 * @param voiceSimDetail
	 *            the voiceSimDetail to set
	 */
	public void setVoiceSimDetail(String voiceSimDetail) {
		this.voiceSimDetail = voiceSimDetail;
	}

	/**
	 * @return the gpsStatus
	 */
	public String getGpsStatus() {
		return gpsStatus;
	}

	/**
	 * @param gpsStatus
	 *            the gpsStatus to set
	 */
	public void setGpsStatus(String gpsStatus) {
		this.gpsStatus = gpsStatus;
	}

	public String getNeStatus() {
		return neStatus;
	}

	public void setNeStatus(String neStatus) {
		this.neStatus = neStatus;
	}

	public String getBtsCode() {
		return btsCode;
	}

	public void setBtsCode(String btsCode) {
		this.btsCode = btsCode;
	}

	public String getBand4G() {
		return band4G;
	}

	public void setBand4G(String band4g) {
		band4G = band4g;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLteCompatible() {
		return lteCompatible;
	}

	public void setLteCompatible(String lteCompatible) {
		this.lteCompatible = lteCompatible;
	}

	public String getDualSimEnabled() {
		return dualSimEnabled;
	}

	public void setDualSimEnabled(String dualSimEnabled) {
		this.dualSimEnabled = dualSimEnabled;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public String getDeviceCompatiable() {
		return deviceCompatiable;
	}

	public void setDeviceCompatiable(String deviceCompatiable) {
		this.deviceCompatiable = deviceCompatiable;
	}

	public String getDeviceFirmwareVersion() {
		return deviceFirmwareVersion;
	}

	public void setDeviceFirmwareVersion(String deviceFirmwareVersion) {
		this.deviceFirmwareVersion = deviceFirmwareVersion;
	}

	/**
	 * @return the deviceIPV4Status
	 */
	public String getDeviceIPV4Status() {
		return deviceIPV4Status;
	}

	/**
	 * @param deviceIPV4Status
	 *            the deviceIPV4Status to set
	 */
	public void setDeviceIPV4Status(String deviceIPV4Status) {
		this.deviceIPV4Status = deviceIPV4Status;
	}

	/**
	 * @return the deviceIPV6Status
	 */
	public String getDeviceIPV6Status() {
		return deviceIPV6Status;
	}

	/**
	 * @param deviceIPV6Status
	 *            the deviceIPV6Status to set
	 */
	public void setDeviceIPV6Status(String deviceIPV6Status) {
		this.deviceIPV6Status = deviceIPV6Status;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	@Override
	public String toString() {
		return "NVCustomerCareDataWrapper [key=" + key + ", recordType=" + recordType + ", capturedOn=" + capturedOn + ", deviceId=" + deviceId + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", cgi=" + cgi + ", band=" + band + ", pci=" + pci + ", tac=" + tac + ", avgRsrp=" + avgRsrp + ", avgSinr=" + avgSinr + ", avgRsrq=" + avgRsrq + ", avgRssi=" + avgRssi
				+ ", avgDlRate=" + avgDlRate + ", avgUlRate=" + avgUlRate + ", jitter=" + jitter + ", pcktLoss=" + pcktLoss + ", operatorName=" + operatorName + "]";
	}
	
}