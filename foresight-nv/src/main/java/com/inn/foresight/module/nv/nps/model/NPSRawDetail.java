package com.inn.foresight.module.nv.nps.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.model.User;

/** Class NPSRawDetail. */
@XmlRootElement(name = "NPSRawDetail")
@Entity
@Table(name = "NPSRawDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NPSRawDetail {

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "npsrawdetailid_pk")
	private Integer id;

	/** The cluster. */
	@ManyToOne
	@JoinColumn(name = "geographyl4id_fk", nullable = false)
	private GeographyL4 geographyL4;
	
	@ManyToOne
	@JoinColumn(name = "userid_fk", nullable = false)
	private User userId;
	
	/** The capturedon. */
	@Basic
	@Column(name = "capturedon")
	private String capturedOn;

	/** The ingestdate. */
	@Basic
	@Column(name = "ingestdate")
	private String ingestdate;

	/** The event. */
	@Basic
	@Column(name = "event")
	private String event;

	/** The make. */
	@Basic
	@Column(name = "manufacturer")
	private String make;

	/** The networkTypeWifiOn. */
	@Basic
	@Column(name = "networktypewhenwifi")
	private String networkTypeWhenWifi;

	/** The model. */
	@Basic
	@Column(name = "model")
	private String model;

	/** The deviceOS. */
	@Basic
	@Column(name = "deviceOS")
	private String deviceOS;

	/** The operatorName. */
	@Basic
	@Column(name = "operatorName")
	private String operatorName;

	/** The networkType. */
	@Basic
	@Column(name = "networkType")
	private String networkType;

	/** The mcc. */
	@Basic
	@Column(name = "mcc")
	private Long mcc;

	/** The mnc. */
	@Basic
	@Column(name = "mnc")
	private Long mnc;

	/** The latitude. */
	@Basic
	@Column(name = "latitude")
	private Double latitude;

	/** The longitude. */
	@Basic
	@Column(name = "longitude")
	private Double longitude;

	/** The netPromoterRating. */
	@Basic
	@Column(name = "netPromoterRating")
	private Integer netPromoterRating;

	/** The pci. */
	@Column(name = "pci")
	private Integer pci;

	/** The cellId. */
	@Column(name = "cellId")
	private Integer cellId;

	/** The tac. */
	@Basic
	@Column(name = "tac")
	private Integer tac;

	/** The wifiName. */
	@Basic
	@Column(name = "wifiName")
	private String wifiName;

	/** The versionName. */
	@Column(name = "versionName")
	private String versionName;

	/** The baseband. */
	@Basic
	@Column(name = "baseband")
	private String baseband;

	/** The buildNumber. */
	@Basic
	@Column(name = "buildNumber")
	private String buildNumber;

	/** The rsrp. */
	@Basic
	@Column(name = "rsrp")
	private Integer rsrp;

	/** The rsrq. */
	@Basic
	@Column(name = "rsrq")
	private Integer rsrq;

	/** The rssi. */
	@Basic
	@Column(name = "rssi")
	private Integer rssi;

	/** The sinr. */
	@Basic
	@Column(name = "sinr")
	private Double sinr;

	/** The rxLevel. */
	@Basic
	@Column(name = "rxLevel")
	private Integer rxLevel;

	/** The rxQuality. */
	@Basic
	@Column(name = "rxQuality")
	private Integer rxQuality;

	/** The rscp. */
	@Basic
	@Column(name = "rscp")
	private Double rscp;

	/** The ecno. */
	@Basic
	@Column(name = "ecno")
	private Integer ecno;

	/** The band. */
	@Basic
	@Column(name = "band")
	private String band;

	/** The deviceId. */
	@Column(name = "deviceId")
	private String deviceId;

	/** The androidId. */
	@Column(name = "androidId")
	private String androidId;

	/** The isEnterprise. */
	@Column(name = "enterprise")
	private Integer enterprise;

	/** The isLayer3Enabled. */
	@Basic
	@Column(name = "layer3enabled")
	private Integer layer3enabled;

	/** The locationAltitude. */
	@Basic
	@Column(name = "locationAltitude")
	private Double locationAltitude;

	/** The locationAccuracy. */
	@Basic
	@Column(name = "locationAccuracy")
	private Float locationAccuracy;

	/** The macAddress. */
	@Basic
	@Column(name = "macAddress")
	private String macAddress;

	/** The isPhoneDualSim. */
	@Basic
	@Column(name = "dualsim")
	private Integer dualsim;

	/** The networkSubType. */
	@Basic
	@Column(name = "networkSubType")
	private String networkSubType;

	/** The isAutoDateTimeEnabled. */
	@Basic
	@Column(name = "autodatetimeenabled")
	private Integer autodatetimeenabled;

	/** The isGpsEnabled. */
	@Basic
	@Column(name = "gpsenabled")
	private Integer gpsenabled;

	/** The neighbourInfo. */
	@Basic
	@Column(name = "neighbourInfo")
	private String neighbourInfo;

	/** The cgiLTE. */
	@Basic
	@Column(name = "cgiLTE")
	private Integer cgiLTE;

	/** The enodeBLTE. */
	@Basic
	@Column(name = "eNodeBLTE")
	private Integer enodeBLTE;

	/** The internalIp. */
	@Basic
	@Column(name = "internalIp")
	private String internalIp;

	/** The bssid. */
	@Basic
	@Column(name = "bssid")
	private String bssid;

	/** The avgLinkSpeed. */
	@Basic
	@Column(name = "avgLinkSpeed")
	private Double avgLinkSpeed;

	/** The operatorNameWhenWifi. */
	@Basic
	@Column(name = "operatorNameWhenWifi")
	private String operatorNameWhenWifi;

	/** The wifiRssi. */
	@Basic
	@Column(name = "wifiRssi")
	private Integer wifiRssi;

	/** The wifiSnr. */
	@Basic
	@Column(name = "wifiSnr")
	private Integer wifiSnr;

	/** The is gps enabled. */
	@Transient
	private Boolean isGpsEnabled;

	/** The is auto date time enabled. */
	@Transient
	private Boolean isAutoDateTimeEnabled;

	/** The is phone dual sim. */
	@Transient
	private Boolean isPhoneDualSim;

	/** The is layer 3 enabled. */
	@Transient
	private Boolean isLayer3Enabled;

	/** The is enterprise. */
	@Transient
	private Boolean isEnterprise;

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public String getCapturedOn() {
		return capturedOn;
	}

	public void setCapturedOn(String capturedOn) {
		this.capturedOn = capturedOn;
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
	 * Gets the geography L 4.
	 *
	 * @return the geography L 4
	 */
	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4 the new geography L 4
	 */
	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}

	/**
	 * Gets the ingestdate.
	 *
	 * @return the ingestdate
	 */
	public String getIngestdate() {
		return ingestdate;
	}

	/**
	 * Sets the ingestdate.
	 *
	 * @param ingestdate the new ingestdate
	 */
	public void setIngestdate(String ingestdate) {
		this.ingestdate = ingestdate;
	}

	/**
	 * Gets the event.
	 *
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * Sets the event.
	 *
	 * @param event the new event
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * Gets the make.
	 *
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Sets the make.
	 *
	 * @param make the new make
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * Gets the network type when wifi.
	 *
	 * @return the network type when wifi
	 */
	public String getNetworkTypeWhenWifi() {
		return networkTypeWhenWifi;
	}

	/**
	 * Sets the network type when wifi.
	 *
	 * @param networkTypeWhenWifi the new network type when wifi
	 */
	public void setNetworkTypeWhenWifi(String networkTypeWhenWifi) {
		this.networkTypeWhenWifi = networkTypeWhenWifi;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model the new model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Gets the device OS.
	 *
	 * @return the device OS
	 */
	public String getDeviceOS() {
		return deviceOS;
	}

	/**
	 * Sets the device OS.
	 *
	 * @param deviceOS the new device OS
	 */
	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}

	/**
	 * Gets the operator name.
	 *
	 * @return the operator name
	 */
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * Sets the operator name.
	 *
	 * @param operatorName the new operator name
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * Gets the network type.
	 *
	 * @return the network type
	 */
	public String getNetworkType() {
		return networkType;
	}

	/**
	 * Sets the network type.
	 *
	 * @param networkType the new network type
	 */
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	/**
	 * Gets the mcc.
	 *
	 * @return the mcc
	 */
	public Long getMcc() {
		return mcc;
	}

	/**
	 * Sets the mcc.
	 *
	 * @param mcc the new mcc
	 */
	public void setMcc(Long mcc) {
		this.mcc = mcc;
	}

	/**
	 * Gets the mnc.
	 *
	 * @return the mnc
	 */
	public Long getMnc() {
		return mnc;
	}

	/**
	 * Sets the mnc.
	 *
	 * @param mnc the new mnc
	 */
	public void setMnc(Long mnc) {
		this.mnc = mnc;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the net promoter rating.
	 *
	 * @return the net promoter rating
	 */
	public Integer getNetPromoterRating() {
		return netPromoterRating;
	}

	/**
	 * Sets the net promoter rating.
	 *
	 * @param netPromoterRating the new net promoter rating
	 */
	public void setNetPromoterRating(Integer netPromoterRating) {
		this.netPromoterRating = netPromoterRating;
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
	 * Gets the tac.
	 *
	 * @return the tac
	 */
	public Integer getTac() {
		return tac;
	}

	/**
	 * Sets the tac.
	 *
	 * @param tac the new tac
	 */
	public void setTac(Integer tac) {
		this.tac = tac;
	}

	/**
	 * Gets the wifi name.
	 *
	 * @return the wifi name
	 */
	public String getWifiName() {
		return wifiName;
	}

	/**
	 * Sets the wifi name.
	 *
	 * @param wifiName the new wifi name
	 */
	public void setWifiName(String wifiName) {
		this.wifiName = wifiName;
	}

	/**
	 * Gets the version name.
	 *
	 * @return the version name
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * Sets the version name.
	 *
	 * @param versionName the new version name
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * Gets the baseband.
	 *
	 * @return the baseband
	 */
	public String getBaseband() {
		return baseband;
	}

	/**
	 * Sets the baseband.
	 *
	 * @param baseband the new baseband
	 */
	public void setBaseband(String baseband) {
		this.baseband = baseband;
	}

	/**
	 * Gets the builds the number.
	 *
	 * @return the builds the number
	 */
	public String getBuildNumber() {
		return buildNumber;
	}

	/**
	 * Sets the builds the number.
	 *
	 * @param buildNumber the new builds the number
	 */
	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	/**
	 * Gets the rsrp.
	 *
	 * @return the rsrp
	 */
	public Integer getRsrp() {
		return rsrp;
	}

	/**
	 * Sets the rsrp.
	 *
	 * @param rsrp the new rsrp
	 */
	public void setRsrp(Integer rsrp) {
		this.rsrp = rsrp;
	}

	/**
	 * Gets the rsrq.
	 *
	 * @return the rsrq
	 */
	public Integer getRsrq() {
		return rsrq;
	}

	/**
	 * Sets the rsrq.
	 *
	 * @param rsrq the new rsrq
	 */
	public void setRsrq(Integer rsrq) {
		this.rsrq = rsrq;
	}

	/**
	 * Gets the rssi.
	 *
	 * @return the rssi
	 */
	public Integer getRssi() {
		return rssi;
	}

	/**
	 * Sets the rssi.
	 *
	 * @param rssi the new rssi
	 */
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}

	/**
	 * Gets the sinr.
	 *
	 * @return the sinr
	 */
	public Double getSinr() {
		return sinr;
	}

	/**
	 * Sets the sinr.
	 *
	 * @param sinr the new sinr
	 */
	public void setSinr(Double sinr) {
		this.sinr = sinr;
	}

	/**
	 * Gets the rx level.
	 *
	 * @return the rx level
	 */
	public Integer getRxLevel() {
		return rxLevel;
	}

	/**
	 * Sets the rx level.
	 *
	 * @param rxLevel the new rx level
	 */
	public void setRxLevel(Integer rxLevel) {
		this.rxLevel = rxLevel;
	}

	/**
	 * Gets the rx quality.
	 *
	 * @return the rx quality
	 */
	public Integer getRxQuality() {
		return rxQuality;
	}

	/**
	 * Sets the rx quality.
	 *
	 * @param rxQuality the new rx quality
	 */
	public void setRxQuality(Integer rxQuality) {
		this.rxQuality = rxQuality;
	}

	/**
	 * Gets the rscp.
	 *
	 * @return the rscp
	 */
	public Double getRscp() {
		return rscp;
	}

	/**
	 * Sets the rscp.
	 *
	 * @param rscp the new rscp
	 */
	public void setRscp(Double rscp) {
		this.rscp = rscp;
	}

	/**
	 * Gets the ecno.
	 *
	 * @return the ecno
	 */
	public Integer getEcno() {
		return ecno;
	}

	/**
	 * Sets the ecno.
	 *
	 * @param ecno the new ecno
	 */
	public void setEcno(Integer ecno) {
		this.ecno = ecno;
	}

	/**
	 * Gets the band.
	 *
	 * @return the band
	 */
	public String getBand() {
		return band;
	}

	/**
	 * Sets the band.
	 *
	 * @param band the new band
	 */
	public void setBand(String band) {
		this.band = band;
	}

	/**
	 * Gets the device id.
	 *
	 * @return the device id
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 *
	 * @param deviceId the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Gets the android id.
	 *
	 * @return the android id
	 */
	public String getAndroidId() {
		return androidId;
	}

	/**
	 * Sets the android id.
	 *
	 * @param androidId the new android id
	 */
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}

	/**
	 * Gets the enterprise.
	 *
	 * @return the enterprise
	 */
	public Integer getEnterprise() {
		return enterprise;
	}

	/**
	 * Sets the enterprise.
	 *
	 * @param enterprise the new enterprise
	 */
	public void setEnterprise(Integer enterprise) {
		this.enterprise = enterprise;
	}

	/**
	 * Gets the layer 3 enabled.
	 *
	 * @return the layer 3 enabled
	 */
	public Integer getLayer3enabled() {
		return layer3enabled;
	}

	/**
	 * Sets the layer 3 enabled.
	 *
	 * @param layer3enabled the new layer 3 enabled
	 */
	public void setLayer3enabled(Integer layer3enabled) {
		this.layer3enabled = layer3enabled;
	}

	/**
	 * Gets the location altitude.
	 *
	 * @return the location altitude
	 */
	public Double getLocationAltitude() {
		return locationAltitude;
	}

	/**
	 * Sets the location altitude.
	 *
	 * @param locationAltitude the new location altitude
	 */
	public void setLocationAltitude(Double locationAltitude) {
		this.locationAltitude = locationAltitude;
	}

	/**
	 * Gets the location accuracy.
	 *
	 * @return the location accuracy
	 */
	public Float getLocationAccuracy() {
		return locationAccuracy;
	}

	/**
	 * Sets the location accuracy.
	 *
	 * @param locationAccuracy the new location accuracy
	 */
	public void setLocationAccuracy(Float locationAccuracy) {
		this.locationAccuracy = locationAccuracy;
	}

	/**
	 * Gets the mac address.
	 *
	 * @return the mac address
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * Sets the mac address.
	 *
	 * @param macAddress the new mac address
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	/**
	 * Gets the dualsim.
	 *
	 * @return the dualsim
	 */
	public Integer getDualsim() {
		return dualsim;
	}

	/**
	 * Sets the dualsim.
	 *
	 * @param dualsim the new dualsim
	 */
	public void setDualsim(Integer dualsim) {
		this.dualsim = dualsim;
	}

	/**
	 * Gets the network sub type.
	 *
	 * @return the network sub type
	 */
	public String getNetworkSubType() {
		return networkSubType;
	}

	/**
	 * Sets the network sub type.
	 *
	 * @param networkSubType the new network sub type
	 */
	public void setNetworkSubType(String networkSubType) {
		this.networkSubType = networkSubType;
	}

	/**
	 * Gets the autodatetimeenabled.
	 *
	 * @return the autodatetimeenabled
	 */
	public Integer getAutodatetimeenabled() {
		return autodatetimeenabled;
	}

	/**
	 * Sets the autodatetimeenabled.
	 *
	 * @param autodatetimeenabled the new autodatetimeenabled
	 */
	public void setAutodatetimeenabled(Integer autodatetimeenabled) {
		this.autodatetimeenabled = autodatetimeenabled;
	}

	/**
	 * Gets the gpsenabled.
	 *
	 * @return the gpsenabled
	 */
	public Integer getGpsenabled() {
		return gpsenabled;
	}

	/**
	 * Sets the gpsenabled.
	 *
	 * @param gpsenabled the new gpsenabled
	 */
	public void setGpsenabled(Integer gpsenabled) {
		this.gpsenabled = gpsenabled;
	}

	/**
	 * Gets the neighbour info.
	 *
	 * @return the neighbour info
	 */
	public String getNeighbourInfo() {
		return neighbourInfo;
	}

	/**
	 * Sets the neighbour info.
	 *
	 * @param neighbourInfo the new neighbour info
	 */
	public void setNeighbourInfo(String neighbourInfo) {
		this.neighbourInfo = neighbourInfo;
	}

	/**
	 * Gets the cgi LTE.
	 *
	 * @return the cgi LTE
	 */
	public Integer getCgiLTE() {
		return cgiLTE;
	}

	/**
	 * Sets the cgi LTE.
	 *
	 * @param cgiLTE the new cgi LTE
	 */
	public void setCgiLTE(Integer cgiLTE) {
		this.cgiLTE = cgiLTE;
	}

	/**
	 * Gets the enode BLTE.
	 *
	 * @return the enode BLTE
	 */
	public Integer getEnodeBLTE() {
		return enodeBLTE;
	}

	/**
	 * Sets the enode BLTE.
	 *
	 * @param enodeBLTE the new enode BLTE
	 */
	public void setEnodeBLTE(Integer enodeBLTE) {
		this.enodeBLTE = enodeBLTE;
	}

	/**
	 * Gets the internal ip.
	 *
	 * @return the internal ip
	 */
	public String getInternalIp() {
		return internalIp;
	}

	/**
	 * Sets the internal ip.
	 *
	 * @param internalIp the new internal ip
	 */
	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
	}

	/**
	 * Gets the bssid.
	 *
	 * @return the bssid
	 */
	public String getBssid() {
		return bssid;
	}

	/**
	 * Sets the bssid.
	 *
	 * @param bssid the new bssid
	 */
	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	/**
	 * Gets the avg link speed.
	 *
	 * @return the avg link speed
	 */
	public Double getAvgLinkSpeed() {
		return avgLinkSpeed;
	}

	/**
	 * Sets the avg link speed.
	 *
	 * @param avgLinkSpeed the new avg link speed
	 */
	public void setAvgLinkSpeed(Double avgLinkSpeed) {
		this.avgLinkSpeed = avgLinkSpeed;
	}

	/**
	 * Gets the operator name when wifi.
	 *
	 * @return the operator name when wifi
	 */
	public String getOperatorNameWhenWifi() {
		return operatorNameWhenWifi;
	}

	/**
	 * Sets the operator name when wifi.
	 *
	 * @param operatorNameWhenWifi the new operator name when wifi
	 */
	public void setOperatorNameWhenWifi(String operatorNameWhenWifi) {
		this.operatorNameWhenWifi = operatorNameWhenWifi;
	}

	/**
	 * Gets the wifi rssi.
	 *
	 * @return the wifi rssi
	 */
	public Integer getWifiRssi() {
		return wifiRssi;
	}

	/**
	 * Sets the wifi rssi.
	 *
	 * @param wifiRssi the new wifi rssi
	 */
	public void setWifiRssi(Integer wifiRssi) {
		this.wifiRssi = wifiRssi;
	}

	/**
	 * Gets the wifi snr.
	 *
	 * @return the wifi snr
	 */
	public Integer getWifiSnr() {
		return wifiSnr;
	}

	/**
	 * Sets the wifi snr.
	 *
	 * @param wifiSnr the new wifi snr
	 */
	public void setWifiSnr(Integer wifiSnr) {
		this.wifiSnr = wifiSnr;
	}

	/**
	 * Gets the checks if is gps enabled.
	 *
	 * @return the checks if is gps enabled
	 */
	public Boolean getIsGpsEnabled() {
		return isGpsEnabled;
	}

	/**
	 * Sets the checks if is gps enabled.
	 *
	 * @param isGpsEnabled the new checks if is gps enabled
	 */
	public void setIsGpsEnabled(Boolean isGpsEnabled) {
		this.isGpsEnabled = isGpsEnabled;
	}

	/**
	 * Gets the checks if is auto date time enabled.
	 *
	 * @return the checks if is auto date time enabled
	 */
	public Boolean getIsAutoDateTimeEnabled() {
		return isAutoDateTimeEnabled;
	}

	/**
	 * Sets the checks if is auto date time enabled.
	 *
	 * @param isAutoDateTimeEnabled the new checks if is auto date time enabled
	 */
	public void setIsAutoDateTimeEnabled(Boolean isAutoDateTimeEnabled) {
		this.isAutoDateTimeEnabled = isAutoDateTimeEnabled;
	}

	/**
	 * Gets the checks if is phone dual sim.
	 *
	 * @return the checks if is phone dual sim
	 */
	public Boolean getIsPhoneDualSim() {
		return isPhoneDualSim;
	}

	/**
	 * Sets the checks if is phone dual sim.
	 *
	 * @param isPhoneDualSim the new checks if is phone dual sim
	 */
	public void setIsPhoneDualSim(Boolean isPhoneDualSim) {
		this.isPhoneDualSim = isPhoneDualSim;
	}

	/**
	 * Gets the checks if is layer 3 enabled.
	 *
	 * @return the checks if is layer 3 enabled
	 */
	public Boolean getIsLayer3Enabled() {
		return isLayer3Enabled;
	}

	/**
	 * Sets the checks if is layer 3 enabled.
	 *
	 * @param isLayer3Enabled the new checks if is layer 3 enabled
	 */
	public void setIsLayer3Enabled(Boolean isLayer3Enabled) {
		this.isLayer3Enabled = isLayer3Enabled;
	}

	/**
	 * Gets the checks if is enterprise.
	 *
	 * @return the checks if is enterprise
	 */
	public Boolean getIsEnterprise() {
		return isEnterprise;
	}

	/**
	 * Sets the checks if is enterprise.
	 *
	 * @param isEnterprise the new checks if is enterprise
	 */
	public void setIsEnterprise(Boolean isEnterprise) {
		this.isEnterprise = isEnterprise;
	}

	@Override
	public String toString() {
		return "NPSRawDetail [id=" + id + ", geographyL4=" + geographyL4 + ", ingestdate=" + ingestdate + ", event="
				+ event + ", make=" + make + ", networkTypeWhenWifi=" + networkTypeWhenWifi + ", model=" + model
				+ ", deviceOS=" + deviceOS + ", operatorName=" + operatorName + ", networkType=" + networkType
				+ ", mcc=" + mcc + ", mnc=" + mnc + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", netPromoterRating=" + netPromoterRating + ", pci=" + pci + ", cellId=" + cellId + ", tac=" + tac
				+ ", wifiName=" + wifiName + ", versionName=" + versionName + ", baseband=" + baseband
				+ ", buildNumber=" + buildNumber + ", rsrp=" + rsrp + ", rsrq=" + rsrq + ", rssi=" + rssi + ", sinr="
				+ sinr + ", rxLevel=" + rxLevel + ", rxQuality=" + rxQuality + ", rscp=" + rscp + ", ecno=" + ecno
				+ ", band=" + band + ", deviceId=" + deviceId + ", androidId=" + androidId + ", enterprise="
				+ enterprise + ", layer3enabled=" + layer3enabled + ", locationAltitude=" + locationAltitude
				+ ", locationAccuracy=" + locationAccuracy + ", macAddress=" + macAddress + ", dualsim=" + dualsim
				+ ", networkSubType=" + networkSubType + ", autodatetimeenabled=" + autodatetimeenabled
				+ ", gpsenabled=" + gpsenabled + ", neighbourInfo=" + neighbourInfo + ", cgiLTE=" + cgiLTE
				+ ", enodeBLTE=" + enodeBLTE + ", internalIp=" + internalIp + ", bssid=" + bssid + ", avgLinkSpeed="
				+ avgLinkSpeed + ", operatorNameWhenWifi=" + operatorNameWhenWifi + ", wifiRssi=" + wifiRssi
				+ ", wifiSnr=" + wifiSnr + ", isGpsEnabled=" + isGpsEnabled + ", isAutoDateTimeEnabled="
				+ isAutoDateTimeEnabled + ", isPhoneDualSim=" + isPhoneDualSim + ", isLayer3Enabled=" + isLayer3Enabled
				+ ", isEnterprise=" + isEnterprise + "]";
	}


}