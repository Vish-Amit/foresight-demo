
package com.inn.foresight.module.nv.feedback.wrapper;

import java.util.Date;

import com.inn.core.generic.wrapper.JpaWrapper;

/** The Class ConsumerFeedbackWrapper. */
@JpaWrapper
public class ConsumerFeedbackWrapper {

	/** The when happened. */
	private Long feedbacktime;

	/** The where happened. */
	private String feedbacklocation;

	/** The test area. */
	private String testArea;

	/** The problem type. */
	private String problemType;

	/** The problem subtype. */
	private String problemSubtype;

	/** The notes. */
	private String notes;

	/** The imsi. */
	private String imsi;

	/** The imei. */
	private String imei;

	/** The make. */
	private String make;

	/** The model. */
	private String model;

	/** The device OS. */
	private String deviceOS;

	/** The operator name. */
	private String operatorName;

	/** The network type. */
	private String networkType;

	/** The mcc. */
	private Integer mcc;

	/** The mnc. */
	private Integer mnc;

	/** The latitude. */
	private Double latitude;

	/** The longitude. */
	private Double longitude;

	/** The feedback type. */
	private String feedbackType;
	
	/** The star rating data. */
	private Integer starRatingData;
	
	/** The star rating coverage. */
	private Integer starRatingCoverage;  

	/** The voice unable to make call. */
	private Boolean voiceUnableToMakeCall;

	/** The voice call drop. */
	private Boolean voiceCallDrop;

	/** The voice poor audio. */
	private Boolean voicePoorAudio;

	/** The voice mute. */
	private Boolean voiceMute;

	/** The voice one way audio. */
	private Boolean voiceOneWayAudio;

	/** The data slow speed. */
	private Boolean dataSlowSpeed;

	/** The data unable to connect. */
	private Boolean dataUnableToConnect;

	/** The coverage poor coverage indoor. */
	private Boolean coveragePoorCoverageIndoor;

	/** The coverage poor coverage outdoor. */
	private Boolean coveragePoorCoverageOutdoor;

	/** The battery level. */
	private String batteryLevel;

	/** The voltage. */
	private String voltage;

	/** The temperature. */
	private String temperature;

	/** The band. */
	private String band;

	/** The pci. */
	private Integer pci;

	/** The cell id. */
	private Integer cellId;

	/** The tac. */
	private Integer tac;

	/** The wifi name. */
	private String wifiName;

	/** The rssi wifi. */
	private Integer rssiWifi;

	/** The snr wifi. */
	private Integer snrWifi;

	/** The ul rate. */
	private Double ulRate;

	/** The dl rate. */
	private Double dlRate;

	/** The version name. */
	private String versionName;

	/** The nv module. */
	private String nvModule;

	/** The baseband. */
	private String baseband;

	/** The build number. */
	private String buildNumber;

	/** The mobile number. */
	private String mobileNumber;

	/** The rsrp LTE. */
	/* LTE Signal Parameters */
	private Integer rsrpLTE;

	/** The rsrq LTE. */
	private Integer rsrqLTE;

	/** The rssi LTE. */
	private Integer rssiLTE;

	/** The sinr LTE. */
	private Double sinrLTE;

	/** The rx level 2 G. */
	/* 2G Signal Parameters */
	private Integer rxLevel2G;

	/** The rx quality 2 G. */
	private Integer rxQuality2G;

	/** The rscp 3 G. */
	/* 3G Signal Parameters */
	private Integer rscp3G;

	/** The ecno 3 G. */
	private Integer ecno3G;

	/** The deviceId. */
	private String deviceId;

	/** The androidId. */
	private String androidId;

	/** The is enterprise. */
	private Boolean isEnterprise;

	/** The is layer 3 enabled. */
	private Boolean isLayer3Enabled;

	/** The chipset. */
	private String chipset;

	/** The serial number. */
	private String serialNumber;

	/** The soc model. */
	private String socModel;

	/** The core architecture. */
	private String coreArchitecture;

	/** The device fingerprint. */
	private String deviceFingerprint;

	/** The network sub type. */
	private String networkSubType;

	/** The mac address. */
	private String macAddress;

	/** The bssid. */
	private String bssid;

	/** The cgi LTE. */
	private Integer cgiLTE;

	/** The e node BLTE. */
	private Integer eNodeBLTE;

	/** The internal ip. */
	private String internalIp;

	/** The neighbour info. */
	private String neighbourInfo;

	/** The network type when wifi. */
	private String networkTypeWhenWifi;

	private String avgLinkSpeed;
	private String wifiConnected;
	private String chargerConnectedStatus;
	private String locationAccuracy;
	private String locationAltitude;
	private String isPhoneDualSim;
	private String isAutoDateTimeEnabled;
	private String isGpsEnabled;
	private String profileId;
	private Long count;
	private Date date;
	private Double avgStarRating;
	private Long starRatingCount;
	private Long starRatingDataCount;
	private Long starRatingCvgCount;
	private Long unableToMakeCallCount;
	private Long callDropCount;
	private Long poorAudioCallCount;
	private Long muteCallCount;
	private Long oneWayAudioCount;
	private Long dataSlowSpeedCount;
	private Long dataUnAvailCount;
	private Long poorCvgIndoorCount;
	private Long poorCvgOutdoorCount;
	private Double testAreaPercent;
	private Double starRatingVal;
	private Double starRatingDataVal;
	private Double starRatingCvgVal;
	private Long eventFeedbackCount;
	private Long campaignFeedbackCount;
	private String geographyName;
	
	private Float starRatingVoIp;
	private boolean voIpSkypeUnableToMakeCall;
	private boolean voIpSkypeCallDrop;
	private boolean voIpSkypePoorAudio;
	private boolean voIpSkypeMute;
	private boolean voIpSkypeOneWayAudio;
	private boolean voIpViberUnableToMakeCall;
	private boolean voIpViberCallDrop;
	private boolean voIpViberPoorAudio;
	private boolean voIpViberMute;
	private boolean voIpViberOneWayAudio;
	private boolean voIpWhatsAppUnableToMakeCall;
	private boolean voIpWhatsAppCallDrop;
	private boolean voIpWhatsAppPoorAudio;
	private boolean voIpWhatsAppMute;
	private boolean voIpWhatsAppOneWayAudio;
	
	private String floorNo;
	
	private Boolean voIpRcsUTMCToRcs;
	private Boolean voIpRcsUTMCToNonRcs;
	private Boolean voIpRcsUTMGroupCall;
	private Boolean voIpRcsCallDisconnect;
	private Boolean voIpRcsPoorAudio;
	private Boolean voIpRcsUTSMsgToRcs;
	private Boolean voIpRcsUTSMsgToNonRcs;
	private Boolean voIpRcsUTMGroupChat;
	private Boolean voIpRcsUTSMultimedia;
	
	private Boolean highSpeedData;
	private Boolean excellentCoverage;
	private Boolean excellentAudioQuality;
	private Boolean poorCoverage;
	private String  apnName;
	
	private Float starRatingVoiceLine;
	private Float starRatingVideoLine;
	private Float starRatingMessagingLine;
	private Float starRatingVoiceRcs;
	private Float starRatingVideoRcs;
	private Float starRatingMessagingRcs;
	private Float starRatingVoiceSkype;
	private Float starRatingVideoSkype;
	private Float starRatingMessagingSkype;
	private Float starRatingVoiceWhatsapp;
	private Float starRatingVideoWhatsapp;
	private Float starRatingMessagingWhatsapp;
	private Float starRatingVoiceViber;
	private Float starRatingVideoViber;
	private Float starRatingMessagingViber;
	private Float starRatingRakutenTv;
	private Float starRatingRakutenLive;
	

	private String data;
	private String sdkVersion;
	
	public ConsumerFeedbackWrapper(Date feedbackDate, String feedbackType, Long count) {
		super();
		this.date = feedbackDate;
		this.feedbackType = feedbackType;
		this.count = count;
	}
	
	public ConsumerFeedbackWrapper(Long count, String feedbackType, Integer starRating) {
		super();
		this.count = count;
		this.feedbackType = feedbackType;
		this.starRating = starRating;
	}
	
	public ConsumerFeedbackWrapper(Long count, String feedbackType, Double avgStarRating) {
		super();
		this.count = count;
		this.feedbackType = feedbackType;
		this.avgStarRating = avgStarRating;
	}
	
	public ConsumerFeedbackWrapper(Long count, String testArea, Date feedbackDate) {
		super();
		this.count = count;
		this.testArea = testArea;
		this.date = feedbackDate;
	}
	
	public ConsumerFeedbackWrapper(Long count, Integer starrating, Integer starratingdata, Integer starratingcvg, Date feedbackDate) {
		super();
		this.count = count;
		this.starRating = starrating;
		this.starRatingData = starratingdata;
		this.starRatingCoverage = starratingcvg;
		this.date = feedbackDate;
	}
	
	public ConsumerFeedbackWrapper(Long starratingCount, Long starratingdataCount, Long starratingcvgCount, Date feedbackDate) {
		super();
		this.starRatingCount = starratingCount;
		this.starRatingDataCount = starratingdataCount;
		this.starRatingCvgCount = starratingcvgCount;
		this.date = feedbackDate;
	}
	
	public ConsumerFeedbackWrapper( Date feedbackDate, Long unableToMakeCall, Long callDropCount, Long poorAudioCall, Long muteCall, Long oneWayAudio) {
		super();
		this.date = feedbackDate;
		this.unableToMakeCallCount = unableToMakeCall;
		this.callDropCount = callDropCount;
		this.poorAudioCallCount = poorAudioCall;
		this.muteCallCount = muteCall;
		this.oneWayAudioCount = oneWayAudio;
	}
	
	public ConsumerFeedbackWrapper( Date feedbackDate, Long dataSlowSpeedCount, Long dataUnAvailCount) {
		super();
		this.date = feedbackDate;
		this.dataSlowSpeedCount = dataSlowSpeedCount;
		this.dataUnAvailCount = dataUnAvailCount;
	}
	
	public ConsumerFeedbackWrapper(Long poorCvgIndoorCount, Long poorCvgOutdoorCount, Date feedbackDate) {
		super();
		this.poorCvgIndoorCount = poorCvgIndoorCount;
		this.poorCvgOutdoorCount = poorCvgOutdoorCount;
		this.date = feedbackDate;
	}
	
	public ConsumerFeedbackWrapper(Double testAreaPercent, String testArea) {
		super();
		this.testAreaPercent = testAreaPercent;
		this.testArea = testArea;
	}
	
	public ConsumerFeedbackWrapper(Long count, String testArea) {
		super();
		this.count = count;
		this.testArea = testArea;
	}
	
	public ConsumerFeedbackWrapper(Double starRating, Double starRatingData, Double starRatingCvg) {
		super();
		this.starRatingVal = starRating;
		this.starRatingDataVal = starRatingData;
		this.starRatingCvgVal = starRatingCvg;
	} 
	/** 2nd wrong. */
	public ConsumerFeedbackWrapper(Long starRating, Long starRatingData, Long starRatingCvg) {
		super();
		this.starRatingCount = starRating;
		this.starRatingDataCount = starRatingData;
		this.starRatingCvgCount = starRatingCvg;
	}
	
	public ConsumerFeedbackWrapper(Long totalCount, Long campaignFeedbackCount, Long eventFeedbackCount, String geographyName, Double latitude, Double longitude) {
		super();
		this.count = totalCount;
		this.campaignFeedbackCount = campaignFeedbackCount;
		this.eventFeedbackCount = eventFeedbackCount;
		this.geographyName = geographyName;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public ConsumerFeedbackWrapper(Long totalCount, String geographyName, Double latitude, Double longitude, String feedbackType) {
		super();
		this.count = totalCount;
		this.geographyName = geographyName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.feedbackType = feedbackType;
	}
	
	public ConsumerFeedbackWrapper(Long starRatingCount, Long starRatingDataCount, Long starRatingCvgCount, 
			String feedbackType, Integer starRating,  Integer starRatingData, Integer starRatingCvg) {
		super();
		this.starRatingCount = starRatingCount;
		this.starRatingDataCount = starRatingDataCount;
		this.starRatingCvgCount = starRatingCvgCount;
		this.feedbackType = feedbackType;
		this.starRating = starRating;
		this.starRatingData = starRatingData;
		this.starRatingCoverage = starRatingCvg;
	}
	
	/**
	 * Gets the battery level.
	 *
	 * @return the battery level
	 */
	public String getBatteryLevel() {
		return batteryLevel;
	}

	/**
	 * Sets the battery level.
	 *
	 * @param batteryLevel
	 *            the new battery level
	 */
	public void setBatteryLevel(String batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	/**
	 * Gets the voltage.
	 *
	 * @return the voltage
	 */
	public String getVoltage() {
		return voltage;
	}

	/**
	 * Sets the voltage.
	 *
	 * @param voltage
	 *            the new voltage
	 */
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	/**
	 * Gets the temperature.
	 *
	 * @return the temperature
	 */
	public String getTemperature() {
		return temperature;
	}

	/**
	 * Sets the temperature.
	 *
	 * @param temperature
	 *            the new temperature
	 */
	public void setTemperature(String temperature) {
		this.temperature = temperature;
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
	 * @param band
	 *            the new band
	 */
	public void setBand(String band) {
		this.band = band;
	}

	/**
	 * Gets the voice unable to make call.
	 *
	 * @return the voice unable to make call
	 */
	public Boolean getVoiceUnableToMakeCall() {
		return voiceUnableToMakeCall;
	}

	/**
	 * Sets the voice unable to make call.
	 *
	 * @param voiceUnableToMakeCall
	 *            the new voice unable to make call
	 */
	public void setVoiceUnableToMakeCall(Boolean voiceUnableToMakeCall) {
		this.voiceUnableToMakeCall = voiceUnableToMakeCall;
	}

	/**
	 * Gets the voice call drop.
	 *
	 * @return the voice call drop
	 */
	public Boolean getVoiceCallDrop() {
		return voiceCallDrop;
	}

	/**
	 * Sets the voice call drop.
	 *
	 * @param voiceCallDrop
	 *            the new voice call drop
	 */
	public void setVoiceCallDrop(Boolean voiceCallDrop) {
		this.voiceCallDrop = voiceCallDrop;
	}

	/**
	 * Gets the voice poor audio.
	 *
	 * @return the voice poor audio
	 */
	public Boolean getVoicePoorAudio() {
		return voicePoorAudio;
	}

	/**
	 * Sets the voice poor audio.
	 *
	 * @param voicePoorAudio
	 *            the new voice poor audio
	 */
	public void setVoicePoorAudio(Boolean voicePoorAudio) {
		this.voicePoorAudio = voicePoorAudio;
	}

	/**
	 * Gets the voice mute.
	 *
	 * @return the voice mute
	 */
	public Boolean getVoiceMute() {
		return voiceMute;
	}

	/**
	 * Sets the voice mute.
	 *
	 * @param voiceMute
	 *            the new voice mute
	 */
	public void setVoiceMute(Boolean voiceMute) {
		this.voiceMute = voiceMute;
	}

	/**
	 * Gets the voice one way audio.
	 *
	 * @return the voice one way audio
	 */
	public Boolean getVoiceOneWayAudio() {
		return voiceOneWayAudio;
	}

	/**
	 * Sets the voice one way audio.
	 *
	 * @param voiceOneWayAudio
	 *            the new voice one way audio
	 */
	public void setVoiceOneWayAudio(Boolean voiceOneWayAudio) {
		this.voiceOneWayAudio = voiceOneWayAudio;
	}

	/**
	 * Gets the data slow speed.
	 *
	 * @return the data slow speed
	 */
	public Boolean getDataSlowSpeed() {
		return dataSlowSpeed;
	}

	/**
	 * Sets the data slow speed.
	 *
	 * @param dataSlowSpeed
	 *            the new data slow speed
	 */
	public void setDataSlowSpeed(Boolean dataSlowSpeed) {
		this.dataSlowSpeed = dataSlowSpeed;
	}

	/**
	 * Gets the data unable to connect.
	 *
	 * @return the data unable to connect
	 */
	public Boolean getDataUnableToConnect() {
		return dataUnableToConnect;
	}

	/**
	 * Sets the data unable to connect.
	 *
	 * @param dataUnableToConnect
	 *            the new data unable to connect
	 */
	public void setDataUnableToConnect(Boolean dataUnableToConnect) {
		this.dataUnableToConnect = dataUnableToConnect;
	}

	/**
	 * Gets the coverage poor coverage indoor.
	 *
	 * @return the coverage poor coverage indoor
	 */
	public Boolean getCoveragePoorCoverageIndoor() {
		return coveragePoorCoverageIndoor;
	}

	/**
	 * Sets the coverage poor coverage indoor.
	 *
	 * @param coveragePoorCoverageIndoor
	 *            the new coverage poor coverage indoor
	 */
	public void setCoveragePoorCoverageIndoor(Boolean coveragePoorCoverageIndoor) {
		this.coveragePoorCoverageIndoor = coveragePoorCoverageIndoor;
	}

	/**
	 * Gets the coverage poor coverage outdoor.
	 *
	 * @return the coverage poor coverage outdoor
	 */
	public Boolean getCoveragePoorCoverageOutdoor() {
		return coveragePoorCoverageOutdoor;
	}

	/**
	 * Sets the coverage poor coverage outdoor.
	 *
	 * @param coveragePoorCoverageOutdoor
	 *            the new coverage poor coverage outdoor
	 */
	public void setCoveragePoorCoverageOutdoor(Boolean coveragePoorCoverageOutdoor) {
		this.coveragePoorCoverageOutdoor = coveragePoorCoverageOutdoor;
	}

	/**
	 * Gets the feedback type.
	 *
	 * @return the feedback type
	 */
	public String getFeedbackType() {
		return feedbackType;
	}

	/**
	 * Sets the feedback type.
	 *
	 * @param feedbackType
	 *            the new feedback type
	 */
	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}

	/** The star rating. */
	private Integer starRating;

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
	 * @param operatorName
	 *            the new operator name
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
	 * @param networkType
	 *            the new network type
	 */
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	/**
	 * Gets the mcc.
	 *
	 * @return the mcc
	 */
	public Integer getMcc() {
		return mcc;
	}

	/**
	 * Sets the mcc.
	 *
	 * @param mcc
	 *            the new mcc
	 */
	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}

	/**
	 * Gets the mnc.
	 *
	 * @return the mnc
	 */
	public Integer getMnc() {
		return mnc;
	}

	/**
	 * Sets the mnc.
	 *
	 * @param mnc
	 *            the new mnc
	 */
	public void setMnc(Integer mnc) {
		this.mnc = mnc;
	}

	/**
	 * Gets the test area.
	 *
	 * @return the test area
	 */
	public String getTestArea() {
		return testArea;
	}

	/**
	 * Sets the test area.
	 *
	 * @param testArea
	 *            the new test area
	 */
	public void setTestArea(String testArea) {
		this.testArea = testArea;
	}

	/**
	 * Gets the notes.
	 *
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * Sets the notes.
	 *
	 * @param notes
	 *            the new notes
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * Gets the imsi.
	 *
	 * @return the imsi
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * Sets the imsi.
	 *
	 * @param imsi
	 *            the new imsi
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * Gets the imei.
	 *
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * Sets the imei.
	 *
	 * @param imei
	 *            the new imei
	 */
	public void setImei(String imei) {
		this.imei = imei;
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
	 * @param make
	 *            the new make
	 */
	public void setMake(String make) {
		this.make = make;
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
	 * @param model
	 *            the new model
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
	 * @param deviceOS
	 *            the new device OS
	 */
	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
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
	 * @param latitude
	 *            the new latitude
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
	 * @param longitude
	 *            the new longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
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
	 * @param pci
	 *            the new pci
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
	 * @param cellId
	 *            the new cell id
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
	 * @param tac
	 *            the new tac
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
	 * @param wifiName
	 *            the new wifi name
	 */
	public void setWifiName(String wifiName) {
		this.wifiName = wifiName;
	}

	/**
	 * Gets the rssi wifi.
	 *
	 * @return the rssi wifi
	 */
	public Integer getRssiWifi() {
		return rssiWifi;
	}

	/**
	 * Sets the rssi wifi.
	 *
	 * @param rssiWifi
	 *            the new rssi wifi
	 */
	public void setRssiWifi(Integer rssiWifi) {
		this.rssiWifi = rssiWifi;
	}

	/**
	 * Gets the snr wifi.
	 *
	 * @return the snr wifi
	 */
	public Integer getSnrWifi() {
		return snrWifi;
	}

	/**
	 * Sets the snr wifi.
	 *
	 * @param snrWifi
	 *            the new snr wifi
	 */
	public void setSnrWifi(Integer snrWifi) {
		this.snrWifi = snrWifi;
	}

	/**
	 * Gets the ul rate.
	 *
	 * @return the ul rate
	 */
	public Double getUlRate() {
		return ulRate;
	}

	/**
	 * Sets the ul rate.
	 *
	 * @param ulRate
	 *            the new ul rate
	 */
	public void setUlRate(Double ulRate) {
		this.ulRate = ulRate;
	}

	/**
	 * Gets the dl rate.
	 *
	 * @return the dl rate
	 */
	public Double getDlRate() {
		return dlRate;
	}

	/**
	 * Sets the dl rate.
	 *
	 * @param dlRate
	 *            the new dl rate
	 */
	public void setDlRate(Double dlRate) {
		this.dlRate = dlRate;
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
	 * @param versionName
	 *            the new version name
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * Gets the nv module.
	 *
	 * @return the nv module
	 */
	public String getNvModule() {
		return nvModule;
	}

	/**
	 * Sets the nv module.
	 *
	 * @param nvModule
	 *            the new nv module
	 */
	public void setNvModule(String nvModule) {
		this.nvModule = nvModule;
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
	 * @param baseband
	 *            the new baseband
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
	 * @param buildNumber
	 *            the new builds the number
	 */
	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	/**
	 * Gets the mobile number.
	 *
	 * @return the mobile number
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * Sets the mobile number.
	 *
	 * @param mobileNumber
	 *            the new mobile number
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * Gets the rsrp LTE.
	 *
	 * @return the rsrp LTE
	 */
	public Integer getRsrpLTE() {
		return rsrpLTE;
	}

	/**
	 * Sets the rsrp LTE.
	 *
	 * @param rsrpLTE
	 *            the new rsrp LTE
	 */
	public void setRsrpLTE(Integer rsrpLTE) {
		this.rsrpLTE = rsrpLTE;
	}

	/**
	 * Gets the rsrq LTE.
	 *
	 * @return the rsrq LTE
	 */
	public Integer getRsrqLTE() {
		return rsrqLTE;
	}

	/**
	 * Sets the rsrq LTE.
	 *
	 * @param rsrqLTE
	 *            the new rsrq LTE
	 */
	public void setRsrqLTE(Integer rsrqLTE) {
		this.rsrqLTE = rsrqLTE;
	}

	/**
	 * Gets the rssi LTE.
	 *
	 * @return the rssi LTE
	 */
	public Integer getRssiLTE() {
		return rssiLTE;
	}

	/**
	 * Sets the rssi LTE.
	 *
	 * @param rssiLTE
	 *            the new rssi LTE
	 */
	public void setRssiLTE(Integer rssiLTE) {
		this.rssiLTE = rssiLTE;
	}

	/**
	 * Gets the sinr LTE.
	 *
	 * @return the sinr LTE
	 */
	public Double getSinrLTE() {
		return sinrLTE;
	}

	/**
	 * Sets the sinr LTE.
	 *
	 * @param sinrLTE
	 *            the new sinr LTE
	 */
	public void setSinrLTE(Double sinrLTE) {
		this.sinrLTE = sinrLTE;
	}

	/**
	 * Gets the rx level 2 G.
	 *
	 * @return the rx level 2 G
	 */
	public Integer getRxLevel2G() {
		return rxLevel2G;
	}

	/**
	 * Sets the rx level 2 G.
	 *
	 * @param rxLevel2G
	 *            the new rx level 2 G
	 */
	public void setRxLevel2G(Integer rxLevel2G) {
		this.rxLevel2G = rxLevel2G;
	}

	/**
	 * Gets the rx quality 2 G.
	 *
	 * @return the rx quality 2 G
	 */
	public Integer getRxQuality2G() {
		return rxQuality2G;
	}

	/**
	 * Sets the rx quality 2 G.
	 *
	 * @param rxQuality2G
	 *            the new rx quality 2 G
	 */
	public void setRxQuality2G(Integer rxQuality2G) {
		this.rxQuality2G = rxQuality2G;
	}

	/**
	 * Gets the rscp 3 G.
	 *
	 * @return the rscp 3 G
	 */
	public Integer getRscp3G() {
		return rscp3G;
	}

	/**
	 * Sets the rscp 3 G.
	 *
	 * @param rscp3g
	 *            the new rscp 3 G
	 */
	public void setRscp3G(Integer rscp3g) {
		rscp3G = rscp3g;
	}

	/**
	 * Gets the ecno 3 G.
	 *
	 * @return the ecno 3 G
	 */
	public Integer getEcno3G() {
		return ecno3G;
	}

	/**
	 * Sets the ecno 3 G.
	 *
	 * @param ecno3g
	 *            the new ecno 3 G
	 */
	public void setEcno3G(Integer ecno3g) {
		ecno3G = ecno3g;
	}

	/**
	 * Get feedback time.
	 *
	 * @return feedbacktime
	 */
	public Long getFeedbacktime() {
		return feedbacktime;
	}

	/**
	 * Set feedback time.
	 *
	 * @param feedbacktime
	 *            the new feedbacktime
	 */
	public void setFeedbacktime(Long feedbacktime) {
		this.feedbacktime = feedbacktime;
	}

	/**
	 * Get feedback location.
	 *
	 * @return feedback location
	 */
	public String getFeedbacklocation() {
		return feedbacklocation;
	}

	/**
	 * Set feedback location.
	 *
	 * @param feedbacklocation
	 *            the new feedbacklocation
	 */
	public void setFeedbacklocation(String feedbacklocation) {
		this.feedbacklocation = feedbacklocation;
	}

	/**
	 * Gets the device id.
	 *
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 *
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Gets the android id.
	 *
	 * @return the androidId
	 */
	public String getAndroidId() {
		return androidId;
	}

	/**
	 * Sets the android id.
	 *
	 * @param androidId
	 *            the androidId to set
	 */
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}

	/**
	 * Gets the problem type.
	 *
	 * @return the problem type
	 */
	public String getProblemType() {
		return problemType;
	}

	/**
	 * Sets the problem type.
	 *
	 * @param problemType
	 *            the new problem type
	 */
	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}

	/**
	 * Gets the problem subtype.
	 *
	 * @return the problem subtype
	 */
	public String getProblemSubtype() {
		return problemSubtype;
	}

	/**
	 * Sets the problem subtype.
	 *
	 * @param problemSubtype
	 *            the new problem subtype
	 */
	public void setProblemSubtype(String problemSubtype) {
		this.problemSubtype = problemSubtype;
	}

	/**
	 * Gets the checks if is enterprise.
	 *
	 * @return the isEnterprise
	 */
	public Boolean getIsEnterprise() {
		return isEnterprise;
	}

	/**
	 * Sets the checks if is enterprise.
	 *
	 * @param isEnterprise
	 *            the isEnterprise to set
	 */
	public void setIsEnterprise(Boolean isEnterprise) {
		this.isEnterprise = isEnterprise;
	}

	/**
	 * Gets the checks if is layer 3 enabled.
	 *
	 * @return the isLayer3Enabled
	 */
	public Boolean getIsLayer3Enabled() {
		return isLayer3Enabled;
	}

	/**
	 * Sets the checks if is layer 3 enabled.
	 *
	 * @param isLayer3Enabled
	 *            the isLayer3Enabled to set
	 */
	public void setIsLayer3Enabled(Boolean isLayer3Enabled) {
		this.isLayer3Enabled = isLayer3Enabled;
	}


	/**
	 * Gets the chipset.
	 *
	 * @return the chipset
	 */
	public String getChipset() {
		return chipset;
	}

	/**
	 * Sets the chipset.
	 *
	 * @param chipset
	 *            the chipset to set
	 */
	public void setChipset(String chipset) {
		this.chipset = chipset;
	}

	/**
	 * Gets the serial number.
	 *
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Sets the serial number.
	 *
	 * @param serialNumber
	 *            the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * Gets the soc model.
	 *
	 * @return the socModel
	 */
	public String getSocModel() {
		return socModel;
	}

	/**
	 * Sets the soc model.
	 *
	 * @param socModel
	 *            the socModel to set
	 */
	public void setSocModel(String socModel) {
		this.socModel = socModel;
	}

	/**
	 * Gets the core architecture.
	 *
	 * @return the coreArchitecture
	 */
	public String getCoreArchitecture() {
		return coreArchitecture;
	}

	/**
	 * Sets the core architecture.
	 *
	 * @param coreArchitecture
	 *            the coreArchitecture to set
	 */
	public void setCoreArchitecture(String coreArchitecture) {
		this.coreArchitecture = coreArchitecture;
	}

	/**
	 * Gets the device fingerprint.
	 *
	 * @return the deviceFingerprint
	 */
	public String getDeviceFingerprint() {
		return deviceFingerprint;
	}

	/**
	 * Sets the device fingerprint.
	 *
	 * @param deviceFingerprint
	 *            the deviceFingerprint to set
	 */
	public void setDeviceFingerprint(String deviceFingerprint) {
		this.deviceFingerprint = deviceFingerprint;
	}


	/**
	 * Gets the network sub type.
	 *
	 * @return the networkSubType
	 */
	public String getNetworkSubType() {
		return networkSubType;
	}

	/**
	 * Sets the network sub type.
	 *
	 * @param networkSubType
	 *            the networkSubType to set
	 */
	public void setNetworkSubType(String networkSubType) {
		this.networkSubType = networkSubType;
	}


	/**
	 * Gets the mac address.
	 *
	 * @return the macAddress
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * Sets the mac address.
	 *
	 * @param macAddress
	 *            the macAddress to set
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
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
	 * @param bssid
	 *            the bssid to set
	 */
	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	/**
	 * Gets the neighbour info.
	 *
	 * @return the neighbourInfo
	 */
	public String getNeighbourInfo() {
		return neighbourInfo;
	}

	/**
	 * Sets the neighbour info.
	 *
	 * @param neighbourInfo
	 *            the neighbourInfo to set
	 */
	public void setNeighbourInfo(String neighbourInfo) {
		this.neighbourInfo = neighbourInfo;
	}

	/**
	 * Gets the cgi LTE.
	 *
	 * @return the cgiLTE
	 */
	public Integer getCgiLTE() {
		return cgiLTE;
	}

	/**
	 * Sets the cgi LTE.
	 *
	 * @param cgiLTE
	 *            the cgiLTE to set
	 */
	public void setCgiLTE(Integer cgiLTE) {
		this.cgiLTE = cgiLTE;
	}

	/**
	 * Gets the e node BLTE.
	 *
	 * @return the eNodeBLTE
	 */
	public Integer geteNodeBLTE() {
		return eNodeBLTE;
	}

	/**
	 * Sets the e node BLTE.
	 *
	 * @param eNodeBLTE
	 *            the eNodeBLTE to set
	 */
	public void seteNodeBLTE(Integer eNodeBLTE) {
		this.eNodeBLTE = eNodeBLTE;
	}

	/**
	 * Gets the internal ip.
	 *
	 * @return the internalIp
	 */
	public String getInternalIp() {
		return internalIp;
	}

	/**
	 * Sets the internal ip.
	 *
	 * @param internalIp
	 *            the internalIp to set
	 */
	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
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
	 * @param networkTypeWhenWifi
	 *            the new network type when wifi
	 */
	public void setNetworkTypeWhenWifi(String networkTypeWhenWifi) {
		this.networkTypeWhenWifi = networkTypeWhenWifi;
	}

	/**
	 * @return the avgLinkSpeed
	 */
	public String getAvgLinkSpeed() {
		return avgLinkSpeed;
	}

	/**
	 * @param avgLinkSpeed the avgLinkSpeed to set
	 */
	public void setAvgLinkSpeed(String avgLinkSpeed) {
		this.avgLinkSpeed = avgLinkSpeed;
	}

	/**
	 * @return the wifiConnected
	 */
	public String getWifiConnected() {
		return wifiConnected;
	}

	/**
	 * @param wifiConnected the wifiConnected to set
	 */
	public void setWifiConnected(String wifiConnected) {
		this.wifiConnected = wifiConnected;
	}

	/**
	 * @return the chargerConnectedStatus
	 */
	public String getChargerConnectedStatus() {
		return chargerConnectedStatus;
	}

	/**
	 * @param chargerConnectedStatus the chargerConnectedStatus to set
	 */
	public void setChargerConnectedStatus(String chargerConnectedStatus) {
		this.chargerConnectedStatus = chargerConnectedStatus;
	}

	/**
	 * @return the locationAccuracy
	 */
	public String getLocationAccuracy() {
		return locationAccuracy;
	}

	/**
	 * @param locationAccuracy the locationAccuracy to set
	 */
	public void setLocationAccuracy(String locationAccuracy) {
		this.locationAccuracy = locationAccuracy;
	}

	/**
	 * @return the locationAltitude
	 */
	public String getLocationAltitude() {
		return locationAltitude;
	}

	/**
	 * @param locationAltitude the locationAltitude to set
	 */
	public void setLocationAltitude(String locationAltitude) {
		this.locationAltitude = locationAltitude;
	}

	/**
	 * @return the isPhoneDualSim
	 */
	public String getIsPhoneDualSim() {
		return isPhoneDualSim;
	}

	/**
	 * @param isPhoneDualSim the isPhoneDualSim to set
	 */
	public void setIsPhoneDualSim(String isPhoneDualSim) {
		this.isPhoneDualSim = isPhoneDualSim;
	}

	/**
	 * @return the isAutoDateTimeEnabled
	 */
	public String getIsAutoDateTimeEnabled() {
		return isAutoDateTimeEnabled;
	}

	/**
	 * @param isAutoDateTimeEnabled the isAutoDateTimeEnabled to set
	 */
	public void setIsAutoDateTimeEnabled(String isAutoDateTimeEnabled) {
		this.isAutoDateTimeEnabled = isAutoDateTimeEnabled;
	}

	/**
	 * @return the isGpsEnabled
	 */
	public String getIsGpsEnabled() {
		return isGpsEnabled;
	}

	/**
	 * @param isGpsEnabled the isGpsEnabled to set
	 */
	public void setIsGpsEnabled(String isGpsEnabled) {
		this.isGpsEnabled = isGpsEnabled;
	}

	/**
	 * @return the profileId
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * @return the avgStarRating
	 */
	public Double getAvgStarRating() {
		return avgStarRating;
	}

	/**
	 * @param avgStarRating the avgStarRating to set
	 */
	public void setAvgStarRating(Double avgStarRating) {
		this.avgStarRating = avgStarRating;
	}

	/**
	 * @return the starRatingCount
	 */
	public Long getStarRatingCount() {
		return starRatingCount;
	}

	/**
	 * @param starRatingCount the starRatingCount to set
	 */
	public void setStarRatingCount(Long starRatingCount) {
		this.starRatingCount = starRatingCount;
	}

	/**
	 * @return the starRatingDataCount
	 */
	public Long getStarRatingDataCount() {
		return starRatingDataCount;
	}

	/**
	 * @param starRatingDataCount the starRatingDataCount to set
	 */
	public void setStarRatingDataCount(Long starRatingDataCount) {
		this.starRatingDataCount = starRatingDataCount;
	}

	/**
	 * @return the starRatingCvgCount
	 */
	public Long getStarRatingCvgCount() {
		return starRatingCvgCount;
	}

	/**
	 * @param starRatingCvgCount the starRatingCvgCount to set
	 */
	public void setStarRatingCvgCount(Long starRatingCvgCount) {
		this.starRatingCvgCount = starRatingCvgCount;
	}

	/**
	 * @return the starRatingData
	 */
	public Integer getStarRatingData() {
		return starRatingData;
	}

	/**
	 * @param starRatingData the starRatingData to set
	 */
	public void setStarRatingData(Integer starRatingData) {
		this.starRatingData = starRatingData;
	}

	/**
	 * @return the starRatingCoverage
	 */
	public Integer getStarRatingCoverage() {
		return starRatingCoverage;
	}

	/**
	 * @param starRatingCoverage the starRatingCoverage to set
	 */
	public void setStarRatingCoverage(Integer starRatingCoverage) {
		this.starRatingCoverage = starRatingCoverage;
	}

	/**
	 * @return the starRating
	 */
	public Integer getStarRating() {
		return starRating;
	}

	/**
	 * @param starRating the starRating to set
	 */
	public void setStarRating(Integer starRating) {
		this.starRating = starRating;
	}

	/**
	 * @return the unableToMakeCallCount
	 */
	public Long getUnableToMakeCallCount() {
		return unableToMakeCallCount;
	}

	/**
	 * @param unableToMakeCallCount the unableToMakeCallCount to set
	 */
	public void setUnableToMakeCallCount(Long unableToMakeCallCount) {
		this.unableToMakeCallCount = unableToMakeCallCount;
	}

	/**
	 * @return the callDropCount
	 */
	public Long getCallDropCount() {
		return callDropCount;
	}

	/**
	 * @param callDropCount the callDropCount to set
	 */
	public void setCallDropCount(Long callDropCount) {
		this.callDropCount = callDropCount;
	}

	/**
	 * @return the poorAudioCallCount
	 */
	public Long getPoorAudioCallCount() {
		return poorAudioCallCount;
	}

	/**
	 * @param poorAudioCallCount the poorAudioCallCount to set
	 */
	public void setPoorAudioCallCount(Long poorAudioCallCount) {
		this.poorAudioCallCount = poorAudioCallCount;
	}

	/**
	 * @return the muteCallCount
	 */
	public Long getMuteCallCount() {
		return muteCallCount;
	}

	/**
	 * @param muteCallCount the muteCallCount to set
	 */
	public void setMuteCallCount(Long muteCallCount) {
		this.muteCallCount = muteCallCount;
	}

	/**
	 * @return the oneWayAudioCount
	 */
	public Long getOneWayAudioCount() {
		return oneWayAudioCount;
	}

	/**
	 * @param oneWayAudioCount the oneWayAudioCount to set
	 */
	public void setOneWayAudioCount(Long oneWayAudioCount) {
		this.oneWayAudioCount = oneWayAudioCount;
	}

	/**
	 * @return the dataSlowSpeedCount
	 */
	public Long getDataSlowSpeedCount() {
		return dataSlowSpeedCount;
	}

	/**
	 * @param dataSlowSpeedCount the dataSlowSpeedCount to set
	 */
	public void setDataSlowSpeedCount(Long dataSlowSpeedCount) {
		this.dataSlowSpeedCount = dataSlowSpeedCount;
	}

	/**
	 * @return the dataUnAvailCount
	 */
	public Long getDataUnAvailCount() {
		return dataUnAvailCount;
	}

	/**
	 * @param dataUnAvailCount the dataUnAvailCount to set
	 */
	public void setDataUnAvailCount(Long dataUnAvailCount) {
		this.dataUnAvailCount = dataUnAvailCount;
	}

	/**
	 * @return the poorCvgIndoorCount
	 */
	public Long getPoorCvgIndoorCount() {
		return poorCvgIndoorCount;
	}

	/**
	 * @param poorCvgIndoorCount the poorCvgIndoorCount to set
	 */
	public void setPoorCvgIndoorCount(Long poorCvgIndoorCount) {
		this.poorCvgIndoorCount = poorCvgIndoorCount;
	}

	/**
	 * @return the poorCvgOutdoorCount
	 */
	public Long getPoorCvgOutdoorCount() {
		return poorCvgOutdoorCount;
	}

	/**
	 * @param poorCvgOutdoorCount the poorCvgOutdoorCount to set
	 */
	public void setPoorCvgOutdoorCount(Long poorCvgOutdoorCount) {
		this.poorCvgOutdoorCount = poorCvgOutdoorCount;
	}

	/**
	 * @return the testAreaPercent
	 */
	public Double getTestAreaPercent() {
		return testAreaPercent;
	}

	/**
	 * @param testAreaPercent the testAreaPercent to set
	 */
	public void setTestAreaPercent(Double testAreaPercent) {
		this.testAreaPercent = testAreaPercent;
	}

	/**
	 * @return the starRatingVal
	 */
	public Double getStarRatingVal() {
		return starRatingVal;
	}

	/**
	 * @param starRatingVal the starRatingVal to set
	 */
	public void setStarRatingVal(Double starRatingVal) {
		this.starRatingVal = starRatingVal;
	}

	/**
	 * @return the starRatingDataVal
	 */
	public Double getStarRatingDataVal() {
		return starRatingDataVal;
	}

	/**
	 * @param starRatingDataVal the starRatingDataVal to set
	 */
	public void setStarRatingDataVal(Double starRatingDataVal) {
		this.starRatingDataVal = starRatingDataVal;
	}

	/**
	 * @return the starRatingCvgVal
	 */
	public Double getStarRatingCvgVal() {
		return starRatingCvgVal;
	}

	/**
	 * @param starRatingCvgVal the starRatingCvgVal to set
	 */
	public void setStarRatingCvgVal(Double starRatingCvgVal) {
		this.starRatingCvgVal = starRatingCvgVal;
	}

	/**
	 * @return the eventFeedbackCount
	 */
	public Long getEventFeedbackCount() {
		return eventFeedbackCount;
	}

	/**
	 * @param eventFeedbackCount the eventFeedbackCount to set
	 */
	public void setEventFeedbackCount(Long eventFeedbackCount) {
		this.eventFeedbackCount = eventFeedbackCount;
	}

	/**
	 * @return the campaignFeedbackCount
	 */
	public Long getCampaignFeedbackCount() {
		return campaignFeedbackCount;
	}

	/**
	 * @param campaignFeedbackCount the campaignFeedbackCount to set
	 */
	public void setCampaignFeedbackCount(Long campaignFeedbackCount) {
		this.campaignFeedbackCount = campaignFeedbackCount;
	}

	/**
	 * @return the geographyName
	 */
	public String getGeographyName() {
		return geographyName;
	}

	/**
	 * @param geographyName the geographyName to set
	 */
	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
	}

	public Float getStarRatingVoIp() {
		return starRatingVoIp;
	}

	public void setStarRatingVoIp(Float starRatingVoIp) {
		this.starRatingVoIp = starRatingVoIp;
	}

	public boolean isVoIpSkypeUnableToMakeCall() {
		return voIpSkypeUnableToMakeCall;
	}

	public void setVoIpSkypeUnableToMakeCall(boolean voIpSkypeUnableToMakeCall) {
		this.voIpSkypeUnableToMakeCall = voIpSkypeUnableToMakeCall;
	}

	public boolean isVoIpSkypeCallDrop() {
		return voIpSkypeCallDrop;
	}

	public void setVoIpSkypeCallDrop(boolean voIpSkypeCallDrop) {
		this.voIpSkypeCallDrop = voIpSkypeCallDrop;
	}

	public boolean isVoIpSkypePoorAudio() {
		return voIpSkypePoorAudio;
	}

	public void setVoIpSkypePoorAudio(boolean voIpSkypePoorAudio) {
		this.voIpSkypePoorAudio = voIpSkypePoorAudio;
	}

	public boolean isVoIpSkypeMute() {
		return voIpSkypeMute;
	}

	public void setVoIpSkypeMute(boolean voIpSkypeMute) {
		this.voIpSkypeMute = voIpSkypeMute;
	}

	public boolean isVoIpSkypeOneWayAudio() {
		return voIpSkypeOneWayAudio;
	}

	public void setVoIpSkypeOneWayAudio(boolean voIpSkypeOneWayAudio) {
		this.voIpSkypeOneWayAudio = voIpSkypeOneWayAudio;
	}

	public boolean isVoIpViberUnableToMakeCall() {
		return voIpViberUnableToMakeCall;
	}

	public void setVoIpViberUnableToMakeCall(boolean voIpViberUnableToMakeCall) {
		this.voIpViberUnableToMakeCall = voIpViberUnableToMakeCall;
	}

	public boolean isVoIpViberCallDrop() {
		return voIpViberCallDrop;
	}

	public void setVoIpViberCallDrop(boolean voIpViberCallDrop) {
		this.voIpViberCallDrop = voIpViberCallDrop;
	}

	public boolean isVoIpViberPoorAudio() {
		return voIpViberPoorAudio;
	}

	public void setVoIpViberPoorAudio(boolean voIpViberPoorAudio) {
		this.voIpViberPoorAudio = voIpViberPoorAudio;
	}

	public boolean isVoIpViberMute() {
		return voIpViberMute;
	}

	public void setVoIpViberMute(boolean voIpViberMute) {
		this.voIpViberMute = voIpViberMute;
	}

	public boolean isVoIpViberOneWayAudio() {
		return voIpViberOneWayAudio;
	}

	public void setVoIpViberOneWayAudio(boolean voIpViberOneWayAudio) {
		this.voIpViberOneWayAudio = voIpViberOneWayAudio;
	}

	public boolean isVoIpWhatsAppUnableToMakeCall() {
		return voIpWhatsAppUnableToMakeCall;
	}

	public void setVoIpWhatsAppUnableToMakeCall(boolean voIpWhatsAppUnableToMakeCall) {
		this.voIpWhatsAppUnableToMakeCall = voIpWhatsAppUnableToMakeCall;
	}

	public boolean isVoIpWhatsAppCallDrop() {
		return voIpWhatsAppCallDrop;
	}

	public void setVoIpWhatsAppCallDrop(boolean voIpWhatsAppCallDrop) {
		this.voIpWhatsAppCallDrop = voIpWhatsAppCallDrop;
	}

	public boolean isVoIpWhatsAppPoorAudio() {
		return voIpWhatsAppPoorAudio;
	}

	public void setVoIpWhatsAppPoorAudio(boolean voIpWhatsAppPoorAudio) {
		this.voIpWhatsAppPoorAudio = voIpWhatsAppPoorAudio;
	}

	public boolean isVoIpWhatsAppMute() {
		return voIpWhatsAppMute;
	}

	public void setVoIpWhatsAppMute(boolean voIpWhatsAppMute) {
		this.voIpWhatsAppMute = voIpWhatsAppMute;
	}

	public boolean isVoIpWhatsAppOneWayAudio() {
		return voIpWhatsAppOneWayAudio;
	}

	public void setVoIpWhatsAppOneWayAudio(boolean voIpWhatsAppOneWayAudio) {
		this.voIpWhatsAppOneWayAudio = voIpWhatsAppOneWayAudio;
	}

	public String getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(String floorNo) {
		this.floorNo = floorNo;
	}

	public Boolean getVoIpRcsUTMCToRcs() {
		return voIpRcsUTMCToRcs;
	}

	public void setVoIpRcsUTMCToRcs(Boolean voIpRcsUTMCToRcs) {
		this.voIpRcsUTMCToRcs = voIpRcsUTMCToRcs;
	}

	public Boolean getVoIpRcsUTMCToNonRcs() {
		return voIpRcsUTMCToNonRcs;
	}

	public void setVoIpRcsUTMCToNonRcs(Boolean voIpRcsUTMCToNonRcs) {
		this.voIpRcsUTMCToNonRcs = voIpRcsUTMCToNonRcs;
	}

	public Boolean getVoIpRcsUTMGroupCall() {
		return voIpRcsUTMGroupCall;
	}

	public void setVoIpRcsUTMGroupCall(Boolean voIpRcsUTMGroupCall) {
		this.voIpRcsUTMGroupCall = voIpRcsUTMGroupCall;
	}

	public Boolean getVoIpRcsCallDisconnect() {
		return voIpRcsCallDisconnect;
	}

	public void setVoIpRcsCallDisconnect(Boolean voIpRcsCallDisconnect) {
		this.voIpRcsCallDisconnect = voIpRcsCallDisconnect;
	}

	public Boolean getVoIpRcsPoorAudio() {
		return voIpRcsPoorAudio;
	}

	public void setVoIpRcsPoorAudio(Boolean voIpRcsPoorAudio) {
		this.voIpRcsPoorAudio = voIpRcsPoorAudio;
	}

	public Boolean getVoIpRcsUTSMsgToRcs() {
		return voIpRcsUTSMsgToRcs;
	}

	public void setVoIpRcsUTSMsgToRcs(Boolean voIpRcsUTSMsgToRcs) {
		this.voIpRcsUTSMsgToRcs = voIpRcsUTSMsgToRcs;
	}

	public Boolean getVoIpRcsUTSMsgToNonRcs() {
		return voIpRcsUTSMsgToNonRcs;
	}

	public void setVoIpRcsUTSMsgToNonRcs(Boolean voIpRcsUTSMsgToNonRcs) {
		this.voIpRcsUTSMsgToNonRcs = voIpRcsUTSMsgToNonRcs;
	}

	public Boolean getVoIpRcsUTMGroupChat() {
		return voIpRcsUTMGroupChat;
	}

	public void setVoIpRcsUTMGroupChat(Boolean voIpRcsUTMGroupChat) {
		this.voIpRcsUTMGroupChat = voIpRcsUTMGroupChat;
	}

	public Boolean getVoIpRcsUTSMultimedia() {
		return voIpRcsUTSMultimedia;
	}

	public void setVoIpRcsUTSMultimedia(Boolean voIpRcsUTSMultimedia) {
		this.voIpRcsUTSMultimedia = voIpRcsUTSMultimedia;
	}

	/**
	 * @return the highSpeedData
	 */
	public Boolean getHighSpeedData() {
		return highSpeedData;
	}

	/**
	 * @param highSpeedData the highSpeedData to set
	 */
	public void setHighSpeedData(Boolean highSpeedData) {
		this.highSpeedData = highSpeedData;
	}

	/**
	 * @return the excellentCoverage
	 */
	public Boolean getExcellentCoverage() {
		return excellentCoverage;
	}

	/**
	 * @param excellentCoverage the excellentCoverage to set
	 */
	public void setExcellentCoverage(Boolean excellentCoverage) {
		this.excellentCoverage = excellentCoverage;
	}

	/**
	 * @return the excellentAudioQuality
	 */
	public Boolean getExcellentAudioQuality() {
		return excellentAudioQuality;
	}

	/**
	 * @param excellentAudioQuality the excellentAudioQuality to set
	 */
	public void setExcellentAudioQuality(Boolean excellentAudioQuality) {
		this.excellentAudioQuality = excellentAudioQuality;
	}

	/**
	 * @return the poorCoverage
	 */
	public Boolean getPoorCoverage() {
		return poorCoverage;
	}

	/**
	 * @param poorCoverage the poorCoverage to set
	 */
	public void setPoorCoverage(Boolean poorCoverage) {
		this.poorCoverage = poorCoverage;
	}

	/**
	 * @return the apnName
	 */
	public String getApnName() {
		return apnName;
	}

	/**
	 * @param apnName the apnName to set
	 */
	public void setApnName(String apnName) {
		this.apnName = apnName;
	}

	/**
	 * @return the starRatingVoiceLine
	 */
	public Float getStarRatingVoiceLine() {
		return starRatingVoiceLine;
	}

	/**
	 * @param starRatingVoiceLine the starRatingVoiceLine to set
	 */
	public void setStarRatingVoiceLine(Float starRatingVoiceLine) {
		this.starRatingVoiceLine = starRatingVoiceLine;
	}

	/**
	 * @return the starRatingVideoLine
	 */
	public Float getStarRatingVideoLine() {
		return starRatingVideoLine;
	}

	/**
	 * @param starRatingVideoLine the starRatingVideoLine to set
	 */
	public void setStarRatingVideoLine(Float starRatingVideoLine) {
		this.starRatingVideoLine = starRatingVideoLine;
	}

	/**
	 * @return the starRatingMessagingLine
	 */
	public Float getStarRatingMessagingLine() {
		return starRatingMessagingLine;
	}

	/**
	 * @param starRatingMessagingLine the starRatingMessagingLine to set
	 */
	public void setStarRatingMessagingLine(Float starRatingMessagingLine) {
		this.starRatingMessagingLine = starRatingMessagingLine;
	}

	/**
	 * @return the starRatingVoiceRcs
	 */
	public Float getStarRatingVoiceRcs() {
		return starRatingVoiceRcs;
	}

	/**
	 * @param starRatingVoiceRcs the starRatingVoiceRcs to set
	 */
	public void setStarRatingVoiceRcs(Float starRatingVoiceRcs) {
		this.starRatingVoiceRcs = starRatingVoiceRcs;
	}

	/**
	 * @return the starRatingVideoRcs
	 */
	public Float getStarRatingVideoRcs() {
		return starRatingVideoRcs;
	}

	/**
	 * @param starRatingVideoRcs the starRatingVideoRcs to set
	 */
	public void setStarRatingVideoRcs(Float starRatingVideoRcs) {
		this.starRatingVideoRcs = starRatingVideoRcs;
	}

	/**
	 * @return the starRatingMessagingRcs
	 */
	public Float getStarRatingMessagingRcs() {
		return starRatingMessagingRcs;
	}

	/**
	 * @param starRatingMessagingRcs the starRatingMessagingRcs to set
	 */
	public void setStarRatingMessagingRcs(Float starRatingMessagingRcs) {
		this.starRatingMessagingRcs = starRatingMessagingRcs;
	}

	/**
	 * @return the starRatingVoiceSkype
	 */
	public Float getStarRatingVoiceSkype() {
		return starRatingVoiceSkype;
	}

	/**
	 * @param starRatingVoiceSkype the starRatingVoiceSkype to set
	 */
	public void setStarRatingVoiceSkype(Float starRatingVoiceSkype) {
		this.starRatingVoiceSkype = starRatingVoiceSkype;
	}

	/**
	 * @return the starRatingVideoSkype
	 */
	public Float getStarRatingVideoSkype() {
		return starRatingVideoSkype;
	}

	/**
	 * @param starRatingVideoSkype the starRatingVideoSkype to set
	 */
	public void setStarRatingVideoSkype(Float starRatingVideoSkype) {
		this.starRatingVideoSkype = starRatingVideoSkype;
	}

	/**
	 * @return the starRatingMessagingSkype
	 */
	public Float getStarRatingMessagingSkype() {
		return starRatingMessagingSkype;
	}

	/**
	 * @param starRatingMessagingSkype the starRatingMessagingSkype to set
	 */
	public void setStarRatingMessagingSkype(Float starRatingMessagingSkype) {
		this.starRatingMessagingSkype = starRatingMessagingSkype;
	}

	/**
	 * @return the starRatingVoiceWhatsapp
	 */
	public Float getStarRatingVoiceWhatsapp() {
		return starRatingVoiceWhatsapp;
	}

	/**
	 * @param starRatingVoiceWhatsapp the starRatingVoiceWhatsapp to set
	 */
	public void setStarRatingVoiceWhatsapp(Float starRatingVoiceWhatsapp) {
		this.starRatingVoiceWhatsapp = starRatingVoiceWhatsapp;
	}

	/**
	 * @return the starRatingVideoWhatsapp
	 */
	public Float getStarRatingVideoWhatsapp() {
		return starRatingVideoWhatsapp;
	}

	/**
	 * @param starRatingVideoWhatsapp the starRatingVideoWhatsapp to set
	 */
	public void setStarRatingVideoWhatsapp(Float starRatingVideoWhatsapp) {
		this.starRatingVideoWhatsapp = starRatingVideoWhatsapp;
	}

	/**
	 * @return the starRatingMessagingWhatsapp
	 */
	public Float getStarRatingMessagingWhatsapp() {
		return starRatingMessagingWhatsapp;
	}

	/**
	 * @param starRatingMessagingWhatsapp the starRatingMessagingWhatsapp to set
	 */
	public void setStarRatingMessagingWhatsapp(Float starRatingMessagingWhatsapp) {
		this.starRatingMessagingWhatsapp = starRatingMessagingWhatsapp;
	}

	/**
	 * @return the starRatingVoiceViber
	 */
	public Float getStarRatingVoiceViber() {
		return starRatingVoiceViber;
	}

	/**
	 * @param starRatingVoiceViber the starRatingVoiceViber to set
	 */
	public void setStarRatingVoiceViber(Float starRatingVoiceViber) {
		this.starRatingVoiceViber = starRatingVoiceViber;
	}

	/**
	 * @return the starRatingVideoViber
	 */
	public Float getStarRatingVideoViber() {
		return starRatingVideoViber;
	}

	/**
	 * @param starRatingVideoViber the starRatingVideoViber to set
	 */
	public void setStarRatingVideoViber(Float starRatingVideoViber) {
		this.starRatingVideoViber = starRatingVideoViber;
	}

	/**
	 * @return the starRatingMessagingViber
	 */
	public Float getStarRatingMessagingViber() {
		return starRatingMessagingViber;
	}

	/**
	 * @param starRatingMessagingViber the starRatingMessagingViber to set
	 */
	public void setStarRatingMessagingViber(Float starRatingMessagingViber) {
		this.starRatingMessagingViber = starRatingMessagingViber;
	}

	/**
	 * @return the starRatingRakutenTv
	 */
	public Float getStarRatingRakutenTv() {
		return starRatingRakutenTv;
	}

	/**
	 * @param starRatingRakutenTv the starRatingRakutenTv to set
	 */
	public void setStarRatingRakutenTv(Float starRatingRakutenTv) {
		this.starRatingRakutenTv = starRatingRakutenTv;
	}

	/**
	 * @return the starRatingRakutenLive
	 */
	public Float getStarRatingRakutenLive() {
		return starRatingRakutenLive;
	}

	/**
	 * @param starRatingRakutenLive the starRatingRakutenLive to set
	 */
	public void setStarRatingRakutenLive(Float starRatingRakutenLive) {
		this.starRatingRakutenLive = starRatingRakutenLive;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}
}
