package com.inn.foresight.module.nv.livedrive.wrapper;

import java.util.List;

import org.json.JSONArray;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** The Class TrackPosition. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackPosition {
	/** The network provider. */
	private String networkProvider;

	/** The lat. */
	private Double lat;

	/** The lng. */
	private Double lng;

	/** The sinr. */
	private Double sinr;

	/** The rsrq. */
	private Integer rsrq;

	/** The rsrp. */
	private Integer rsrp;

	/** The rssi. */
	private Integer rssi;

	/** The ul avg. */
	private Double ulAvg;

	/** The dl avg. */
	private Double dlAvg;

	/** The battery per. */
	private Integer batteryPer;

	/** The speed. */
	private Float speed;

	/** The trav distance. */
	private Float travDistance;

	/** The network type. */
	private String networkType;

	/** The trav time. */
	private String travTime;

	/** The psc. */
	private Integer psc;

	/** The lac. */
	private Integer lac;

	/** The rscp. */
	private Integer rscp;

	/** The ec no. */
	private Integer ecNo;

	/** The rx level. */
	private Integer rxLevel;

	/** The rx quality. */
	private Integer rxQuality;

	/** The mcc. */
	private Integer mcc;

	/** The mnc. */
	private Integer mnc;

	/** The cell id. */
	private Integer cellId;

	/** The tac. */
	private Integer tac;

	/** The pci. */
	private Integer pci;

	/** The browse holder. */
	private BrowseHolder browseHolder;

	/** The ping value holder. */
	private PingValueHolder pingValueHolder;

	/** The capturing time. */
	private long capturingTime;

	/** The mobile number. */
	private String mobileNumber;

	/** Network band. */
	private Integer band;

	private CallHolder callHolder;

	private List<NeighbourInfo> neighboursCellInfoList;

	private String oldOrAttemptedPci;

	private String oldOrAttemptedCellId;

	private String eventLog;

	private String eventLogStatus;

	private String eventCause;

	private JSONArray jsonArray;

	private String imsEventCodeBean;

	private String sipEventCodeBean;

	private String volteEventCodeBean;

	private Double dlLinkAvg;

	private Double ulLinkAvg;

	private String script;

	private Long smsSentTime;

	private Long smsStatusRecivedTime;

	private String smsStatus;

	private Integer smsId;

	private Integer cgi;

	private Integer enodeb;

	private Long totalBufferingTime;

	private Double videoAvgDlSpeed;

	private Integer videoStallingCount;

	private Integer videoFreezingRatio;

	private Long videoLoadTime;

	private Long videoDuration;

	private Integer iteration;

	private String videoLink;

	private Long rrcConnectionSetupTime;

	private Integer voiceTotalCallCount;

	private Integer voiceDropCallCount;

	private Integer voiceFailCallCount;

	private String voiceCallType;
	
	private Integer videoResolution;

	private String httpLinkUrl;
	
	private String testStatus;
	
	private String failureCause;
	
	private Long totalDlUlTime;
	
	private Long smsTotalTime;
	
	private String netStatus;
	
	private Integer ecIo;
	
	public String getOldOrAttemptedPci() {
		return oldOrAttemptedPci;
	}

	public void setOldOrAttemptedPci(String oldOrAttemptedPci) {
		this.oldOrAttemptedPci = oldOrAttemptedPci;
	}

	public String getOldOrAttemptedCellId() {
		return oldOrAttemptedCellId;
	}

	public void setOldOrAttemptedCellId(String oldOrAttemptedCellId) {
		this.oldOrAttemptedCellId = oldOrAttemptedCellId;
	}

	public Integer getEcIo() {
		return ecIo;
	}

	public void setEcIo(Integer ecIo) {
		this.ecIo = ecIo;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * Gets the network provider.
	 *
	 * @return the network provider
	 */
	public String getNetworkProvider() {
		return networkProvider;
	}

	/**
	 * Sets the network provider.
	 *
	 * @param networkProvider
	 *            the new network provider
	 */
	public void setNetworkProvider(String networkProvider) {
		this.networkProvider = networkProvider;
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
	 * @param rxLevel
	 *            the new rx level
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
	 * @param rxQuality
	 *            the new rx quality
	 */
	public void setRxQuality(Integer rxQuality) {
		this.rxQuality = rxQuality;
	}

	/**
	 * Gets the psc.
	 *
	 * @return the psc
	 */
	public Integer getPsc() {
		return psc;
	}

	/**
	 * Sets the psc.
	 *
	 * @param psc
	 *            the new psc
	 */
	public void setPsc(Integer psc) {
		this.psc = psc;
	}

	/**
	 * Gets the lac.
	 *
	 * @return the lac
	 */
	public Integer getLac() {
		return lac;
	}

	/**
	 * Sets the lac.
	 *
	 * @param lac
	 *            the new lac
	 */
	public void setLac(Integer lac) {
		this.lac = lac;
	}

	/**
	 * Gets the rscp.
	 *
	 * @return the rscp
	 */
	public Integer getRscp() {
		return rscp;
	}

	/**
	 * Sets the rscp.
	 *
	 * @param rscp
	 *            the new rscp
	 */
	public void setRscp(Integer rscp) {
		this.rscp = rscp;
	}

	/**
	 * Gets the ec no.
	 *
	 * @return the ec no
	 */
	public Integer getEcNo() {
		return ecNo;
	}

	/**
	 * Sets the ec no.
	 *
	 * @param ecNo
	 *            the new ec no
	 */
	public void setEcNo(Integer ecNo) {
		this.ecNo = ecNo;
	}

	/**
	 * Gets the lat.
	 *
	 * @return the lat
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * Sets the lat.
	 *
	 * @param lat
	 *            the new lat
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * Gets the lng.
	 *
	 * @return the lng
	 */
	public Double getLng() {
		return lng;
	}

	/**
	 * Sets the lng.
	 *
	 * @param lng
	 *            the new lng
	 */
	public void setLng(Double lng) {
		this.lng = lng;
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
	 * @param sinr
	 *            the new sinr
	 */
	public void setSinr(Double sinr) {
		this.sinr = sinr;
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
	 * @param rsrq
	 *            the new rsrq
	 */
	public void setRsrq(Integer rsrq) {
		this.rsrq = rsrq;
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
	 * @param rsrp
	 *            the new rsrp
	 */
	public void setRsrp(Integer rsrp) {
		this.rsrp = rsrp;
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
	 * @param rssi
	 *            the new rssi
	 */
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}

	/**
	 * Gets the ul avg.
	 *
	 * @return the ul avg
	 */
	public Double getUlAvg() {
		return ulAvg;
	}

	/**
	 * Sets the ul avg.
	 *
	 * @param ulAvg
	 *            the new ul avg
	 */
	public void setUlAvg(Double ulAvg) {
		this.ulAvg = ulAvg;
	}

	/**
	 * Gets the dl avg.
	 *
	 * @return the dl avg
	 */
	public Double getDlAvg() {
		return dlAvg;
	}

	/**
	 * Sets the dl avg.
	 *
	 * @param dlAvg
	 *            the new dl avg
	 */
	public void setDlAvg(Double dlAvg) {
		this.dlAvg = dlAvg;
	}

	/**
	 * Gets the battery per.
	 *
	 * @return the battery per
	 */
	public Integer getBatteryPer() {
		return batteryPer;
	}

	/**
	 * Sets the battery per.
	 *
	 * @param batteryPer
	 *            the new battery per
	 */
	public void setBatteryPer(Integer batteryPer) {
		this.batteryPer = batteryPer;
	}

	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public Float getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed.
	 *
	 * @param speed
	 *            the new speed
	 */
	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	/**
	 * Gets the trav distance.
	 *
	 * @return the trav distance
	 */
	public Float getTravDistance() {
		return travDistance;
	}

	/**
	 * Sets the trav distance.
	 *
	 * @param travDistance
	 *            the new trav distance
	 */
	public void setTravDistance(Float travDistance) {
		this.travDistance = travDistance;
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
	 * Gets the trav time.
	 *
	 * @return the trav time
	 */
	public String getTravTime() {
		return travTime;
	}

	/**
	 * Sets the trav time.
	 *
	 * @param travTime
	 *            the new trav time
	 */
	public void setTravTime(String travTime) {
		this.travTime = travTime;
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
	 * Gets the browse holder.
	 *
	 * @return the browse holder
	 */
	public BrowseHolder getBrowseHolder() {
		return browseHolder;
	}

	/**
	 * Sets the browse holder.
	 *
	 * @param browseHolder
	 *            the new browse holder
	 */
	public void setBrowseHolder(BrowseHolder browseHolder) {
		this.browseHolder = browseHolder;
	}

	/**
	 * Gets the ping value holder.
	 *
	 * @return the ping value holder
	 */
	public PingValueHolder getPingValueHolder() {
		return pingValueHolder;
	}

	/**
	 * Sets the ping value holder.
	 *
	 * @param pingValueHolder
	 *            the new ping value holder
	 */
	public void setPingValueHolder(PingValueHolder pingValueHolder) {
		this.pingValueHolder = pingValueHolder;
	}

	/**
	 * Gets the capturing time.
	 *
	 * @return the capturing time
	 */
	public long getCapturingTime() {
		return capturingTime;
	}

	/**
	 * Sets the capturing time.
	 *
	 * @param capturingTime
	 *            the new capturing time
	 */
	public void setCapturingTime(long capturingTime) {
		this.capturingTime = capturingTime;
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

	/** Instantiates a new track position. */
	public TrackPosition() {
		super();
	}

	public Integer getBand() {
		return band;
	}

	public void setBand(Integer band) {
		this.band = band;
	}

	public CallHolder getCallHolder() {
		return callHolder;
	}

	public void setCallHolder(CallHolder callHolder) {
		this.callHolder = callHolder;
	}

	public List<NeighbourInfo> getNeighboursCellInfoList() {
		return neighboursCellInfoList;
	}

	public void setNeighboursCellInfoList(List<NeighbourInfo> neighboursCellInfoList) {
		this.neighboursCellInfoList = neighboursCellInfoList;
	}

	public String getEventLog() {
		return eventLog;
	}

	public void setEventLog(String eventLog) {
		this.eventLog = eventLog;
	}

	public String getEventLogStatus() {
		return eventLogStatus;
	}

	public void setEventLogStatus(String eventLogStatus) {
		this.eventLogStatus = eventLogStatus;
	}

	public String getEventCause() {
		return eventCause;
	}

	public void setEventCause(String eventCause) {
		this.eventCause = eventCause;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public String getImsEventCodeBean() {
		return imsEventCodeBean;
	}

	public void setImsEventCodeBean(String imsEventCodeBean) {
		this.imsEventCodeBean = imsEventCodeBean;
	}

	public String getSipEventCodeBean() {
		return sipEventCodeBean;
	}

	public void setSipEventCodeBean(String sipEventCodeBean) {
		this.sipEventCodeBean = sipEventCodeBean;
	}

	public String getVolteEventCodeBean() {
		return volteEventCodeBean;
	}

	public void setVolteEventCodeBean(String volteEventCodeBean) {
		this.volteEventCodeBean = volteEventCodeBean;
	}

	public Double getDlLinkAvg() {
		return dlLinkAvg;
	}

	public void setDlLinkAvg(Double dlLinkAvg) {
		this.dlLinkAvg = dlLinkAvg;
	}

	public Double getUlLinkAvg() {
		return ulLinkAvg;
	}

	public void setUlLinkAvg(Double ulLinkAvg) {
		this.ulLinkAvg = ulLinkAvg;
	}

	public Long getSmsSentTime() {
		return smsSentTime;
	}

	public void setSmsSentTime(Long smsSentTime) {
		this.smsSentTime = smsSentTime;
	}

	public Long getSmsStatusRecivedTime() {
		return smsStatusRecivedTime;
	}

	public void setSmsStatusRecivedTime(Long smsStatusRecivedTime) {
		this.smsStatusRecivedTime = smsStatusRecivedTime;
	}

	public String getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}

	public Integer getSmsId() {
		return smsId;
	}

	public void setSmsId(Integer smsId) {
		this.smsId = smsId;
	}

	public Integer getCgi() {
		return cgi;
	}

	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}

	public Integer getEnodeb() {
		return enodeb;
	}

	public void setEnodeb(Integer enodeb) {
		this.enodeb = enodeb;
	}

	public Long getTotalBufferingTime() {
		return totalBufferingTime;
	}

	public void setTotalBufferingTime(Long totalBufferingTime) {
		this.totalBufferingTime = totalBufferingTime;
	}

	public Double getVideoAvgDlSpeed() {
		return videoAvgDlSpeed;
	}

	public void setVideoAvgDlSpeed(Double videoAvgDlSpeed) {
		this.videoAvgDlSpeed = videoAvgDlSpeed;
	}

	public Integer getVideoStallingCount() {
		return videoStallingCount;
	}

	public void setVideoStallingCount(Integer videoStallingCount) {
		this.videoStallingCount = videoStallingCount;
	}

	public Integer getVideoFreezingRatio() {
		return videoFreezingRatio;
	}

	public void setVideoFreezingRatio(Integer videoFreezingRatio) {
		this.videoFreezingRatio = videoFreezingRatio;
	}

	public Long getVideoLoadTime() {
		return videoLoadTime;
	}

	public void setVideoLoadTime(Long videoLoadTime) {
		this.videoLoadTime = videoLoadTime;
	}

	public Long getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(Long videoDuration) {
		this.videoDuration = videoDuration;
	}

	public Integer getIteration() {
		return iteration;
	}

	public void setIteration(Integer iteration) {
		this.iteration = iteration;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public Long getRrcConnectionSetupTime() {
		return rrcConnectionSetupTime;
	}

	public void setRrcConnectionSetupTime(Long rrcConnectionSetupTime) {
		this.rrcConnectionSetupTime = rrcConnectionSetupTime;
	}

	public Integer getVoiceTotalCallCount() {
		return voiceTotalCallCount;
	}

	public void setVoiceTotalCallCount(Integer voiceTotalCallCount) {
		this.voiceTotalCallCount = voiceTotalCallCount;
	}

	public Integer getVoiceDropCallCount() {
		return voiceDropCallCount;
	}

	public void setVoiceDropCallCount(Integer voiceDropCallCount) {
		this.voiceDropCallCount = voiceDropCallCount;
	}

	public Integer getVoiceFailCallCount() {
		return voiceFailCallCount;
	}

	public void setVoiceFailCallCount(Integer voiceFailCallCount) {
		this.voiceFailCallCount = voiceFailCallCount;
	}

	public String getVoiceCallType() {
		return voiceCallType;
	}

	public void setVoiceCallType(String voiceCallType) {
		this.voiceCallType = voiceCallType;
	}

	public Integer getVideoResolution() {
		return videoResolution;
	}

	public void setVideoResolution(Integer videoResolution) {
		this.videoResolution = videoResolution;
	}

	public String getHttpLinkUrl() {
		return httpLinkUrl;
	}

	public void setHttpLinkUrl(String httpLinkUrl) {
		this.httpLinkUrl = httpLinkUrl;
	}

	public String getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}

	public String getFailureCause() {
		return failureCause;
	}

	public void setFailureCause(String failureCause) {
		this.failureCause = failureCause;
	}

	public Long getTotalDlUlTime() {
		return totalDlUlTime;
	}

	public void setTotalDlUlTime(Long totalDlUlTime) {
		this.totalDlUlTime = totalDlUlTime;
	}

	public Long getSmsTotalTime() {
		return smsTotalTime;
	}

	public void setSmsTotalTime(Long smsTotalTime) {
		this.smsTotalTime = smsTotalTime;
	}

	public String getNetStatus() {
		return netStatus;
	}

	public void setNetStatus(String netStatus) {
		this.netStatus = netStatus;
	}

	@Override
	public String toString() {
		return "TrackPosition [networkProvider=" + networkProvider + ", lat=" + lat + ", lng=" + lng + ", sinr=" + sinr
				+ ", rsrq=" + rsrq + ", rsrp=" + rsrp + ", rssi=" + rssi + ", ulAvg=" + ulAvg + ", dlAvg=" + dlAvg
				+ ", batteryPer=" + batteryPer + ", speed=" + speed + ", travDistance=" + travDistance
				+ ", networkType=" + networkType + ", travTime=" + travTime + ", psc=" + psc + ", lac=" + lac
				+ ", rscp=" + rscp + ", ecNo=" + ecNo + ", rxLevel=" + rxLevel + ", rxQuality=" + rxQuality + ", mcc="
				+ mcc + ", mnc=" + mnc + ", cellId=" + cellId + ", tac=" + tac + ", pci=" + pci + ", browseHolder="
				+ browseHolder + ", pingValueHolder=" + pingValueHolder + ", capturingTime=" + capturingTime
				+ ", mobileNumber=" + mobileNumber + ", band=" + band + ", callHolder=" + callHolder
				+ ", neighboursCellInfoList=" + neighboursCellInfoList + ", oldOrAttemptedPci=" + oldOrAttemptedPci
				+ ", oldOrAttemptedCellId=" + oldOrAttemptedCellId + ", eventLog=" + eventLog + ", eventLogStatus="
				+ eventLogStatus + ", eventCause=" + eventCause + ", jsonArray=" + jsonArray + ", imsEventCodeBean="
				+ imsEventCodeBean + ", sipEventCodeBean=" + sipEventCodeBean + ", volteEventCodeBean="
				+ volteEventCodeBean + ", dlLinkAvg=" + dlLinkAvg + ", ulLinkAvg=" + ulLinkAvg + ", script=" + script
				+ ", smsSentTime=" + smsSentTime + ", smsStatusRecivedTime=" + smsStatusRecivedTime + ", smsStatus="
				+ smsStatus + ", smsId=" + smsId + ", cgi=" + cgi + ", enodeb=" + enodeb + ", totalBufferingTime="
				+ totalBufferingTime + ", videoAvgDlSpeed=" + videoAvgDlSpeed + ", videoStallingCount="
				+ videoStallingCount + ", videoFreezingRatio=" + videoFreezingRatio + ", videoLoadTime=" + videoLoadTime
				+ ", videoDuration=" + videoDuration + ", iteration=" + iteration + ", videoLink=" + videoLink
				+ ", rrcConnectionSetupTime=" + rrcConnectionSetupTime + ", voiceTotalCallCount=" + voiceTotalCallCount
				+ ", voiceDropCallCount=" + voiceDropCallCount + ", voiceFailCallCount=" + voiceFailCallCount
				+ ", voiceCallType=" + voiceCallType + ", videoResolution=" + videoResolution + ", httpLinkUrl="
				+ httpLinkUrl + ", testStatus=" + testStatus + ", failureCause=" + failureCause + ", totalDlUlTime="
				+ totalDlUlTime + ", smsTotalTime=" + smsTotalTime + ", netStatus=" + netStatus + ", ecIo=" + ecIo
				+ "]";
	}
	
}