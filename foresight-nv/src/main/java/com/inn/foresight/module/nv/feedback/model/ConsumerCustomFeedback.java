package com.inn.foresight.module.nv.feedback.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@XmlRootElement(name = "ConsumerCustomFeedback")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Table(name = "ConsumerCustomFeedback")
public class ConsumerCustomFeedback {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "consumercustomfeedbackid_pk")
	private Integer id;

	@Column(name = "zoom5")
	String zoom5;
	
	@Column(name = "zoom6")
	String zoom6;
	
	@Column(name = "zoom7")
	String zoom7;
	
	@Column(name = "zoom8")
	String zoom8;
	
	@Column(name = "zoom9")
	String zoom9;
	
	@Column(name = "zoom10")
	String zoom10;
	
	@Column(name = "zoom11")
	String zoom11;
	
	@Column(name = "zoom12")
	String zoom12;
	
	@Column(name = "zoom13")
	String zoom13;
	
	@Column(name = "zoom14")
	String zoom14;
	
	@Column(name = "feedbackdate")
	Date feedbackDate;
	
	@Column(name = "isdatafeedback")
	Boolean isDataFeedback;
	
	@Column(name = "isvoicefeedback")
	Boolean isVoiceFeedback;
	
	@Column(name = "feedbackaddress")
	String feedbackAddress;
	
	@Column(name = "internetqualityrate")
	Integer internetQualityRate;
	
	@Column(name = "callsqualityrate")
	Integer callsQualityRate;
	
	@Column(name = "dataslowconnection")
	Boolean dataSlowConnection;
	
	@Column(name = "datanoconnection")
	Boolean dataNoConnection;
	
	@Column(name = "voicecallmute")
	Boolean voiceCallMute;
	
	@Column(name = "voicecallfailed")
	Boolean voiceCallFailed;
	
	@Column(name = "voicecalloneway")
	Boolean voiceCallOneWay;
	
	@Column(name = "voicecalldegradation")
	Boolean voiceCallDegradation;
	
	@Column(name = "voicecallnotAvailable")
	Boolean voiceCallNotAvailable;

	@Column(name = "locationTypeIndoor")
	Boolean locationTypeIndoor;

	@Column(name = "locationTypeOutside")
	Boolean locationTypeOutside;

	@Column(name = "locationTypeOnTheMove")
	Boolean locationTypeOnTheMove;

	@OneToOne
	@JoinColumn(name = "consumerfeedbackid_fk", nullable = false)
	private ConsumerFeedback consumerFeedback;

	public String getZoom5() {
		return zoom5;
	}

	public void setZoom5(String zoom5) {
		this.zoom5 = zoom5;
	}

	public String getZoom6() {
		return zoom6;
	}

	public void setZoom6(String zoom6) {
		this.zoom6 = zoom6;
	}

	public String getZoom7() {
		return zoom7;
	}

	public void setZoom7(String zoom7) {
		this.zoom7 = zoom7;
	}

	public String getZoom8() {
		return zoom8;
	}

	public void setZoom8(String zoom8) {
		this.zoom8 = zoom8;
	}

	public String getZoom9() {
		return zoom9;
	}

	public void setZoom9(String zoom9) {
		this.zoom9 = zoom9;
	}

	public String getZoom10() {
		return zoom10;
	}

	public void setZoom10(String zoom10) {
		this.zoom10 = zoom10;
	}

	public String getZoom11() {
		return zoom11;
	}

	public void setZoom11(String zoom11) {
		this.zoom11 = zoom11;
	}

	public String getZoom12() {
		return zoom12;
	}

	public void setZoom12(String zoom12) {
		this.zoom12 = zoom12;
	}

	public String getZoom13() {
		return zoom13;
	}

	public void setZoom13(String zoom13) {
		this.zoom13 = zoom13;
	}

	public String getZoom14() {
		return zoom14;
	}

	public void setZoom14(String zoom14) {
		this.zoom14 = zoom14;
	}

	public Date getFeedbackDate() {
		return feedbackDate;
	}

	public void setFeedbackDate(Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}

	public Boolean getIsDataFeedback() {
		return isDataFeedback;
	}

	public void setIsDataFeedback(Boolean isDataFeedback) {
		this.isDataFeedback = isDataFeedback;
	}

	public Boolean getIsVoiceFeedback() {
		return isVoiceFeedback;
	}

	public void setIsVoiceFeedback(Boolean isVoiceFeedback) {
		this.isVoiceFeedback = isVoiceFeedback;
	}

	public String getFeedbackAddress() {
		return feedbackAddress;
	}

	public void setFeedbackAddress(String feedbackAddress) {
		this.feedbackAddress = feedbackAddress;
	}

	public Integer getInternetQualityRate() {
		return internetQualityRate;
	}

	public void setInternetQualityRate(Integer internetQualityRate) {
		this.internetQualityRate = internetQualityRate;
	}

	public Integer getCallsQualityRate() {
		return callsQualityRate;
	}

	public void setCallsQualityRate(Integer callsQualityRate) {
		this.callsQualityRate = callsQualityRate;
	}

	public Boolean getDataSlowConnection() {
		return dataSlowConnection;
	}

	public void setDataSlowConnection(Boolean dataSlowConnection) {
		this.dataSlowConnection = dataSlowConnection;
	}

	public Boolean getDataNoConnection() {
		return dataNoConnection;
	}

	public void setDataNoConnection(Boolean dataNoConnection) {
		this.dataNoConnection = dataNoConnection;
	}

	public Boolean getDataVoiceCallMute() {
		return voiceCallMute;
	}

	public void setDataVoiceCallMute(Boolean dataVoiceCallMute) {
		this.voiceCallMute = dataVoiceCallMute;
	}

	public Boolean getDataVoiceCallFailed() {
		return voiceCallFailed;
	}

	public void setDataVoiceCallFailed(Boolean dataVoiceCallFailed) {
		this.voiceCallFailed = dataVoiceCallFailed;
	}

	public Boolean getDataVoiceCallOneWay() {
		return voiceCallOneWay;
	}

	public void setDataVoiceCallOneWay(Boolean dataVoiceCallOneWay) {
		this.voiceCallOneWay = dataVoiceCallOneWay;
	}

	public Boolean getDataVoiceCallDegradation() {
		return voiceCallDegradation;
	}

	public void setDataVoiceCallDegradation(Boolean dataVoiceCallDegradation) {
		this.voiceCallDegradation = dataVoiceCallDegradation;
	}

	public Boolean getDataVoiceCallNotAvailable() {
		return voiceCallNotAvailable;
	}

	public void setDataVoiceCallNotAvailable(Boolean dataVoiceCallNotAvailable) {
		this.voiceCallNotAvailable = dataVoiceCallNotAvailable;
	}


	public Boolean getLocationTypeIndoor() {
		return locationTypeIndoor;
	}

	public void setLocationTypeIndoor(Boolean locationTypeIndoor) {
		this.locationTypeIndoor = locationTypeIndoor;
	}

	public Boolean getLocationTypeOutside() {
		return locationTypeOutside;
	}

	public void setLocationTypeOutside(Boolean locationTypeOutside) {
		this.locationTypeOutside = locationTypeOutside;
	}

	public Boolean getLocationTypeOnTheMove() {
		return locationTypeOnTheMove;
	}

	public void setLocationTypeOnTheMove(Boolean locationTypeOnTheMove) {
		this.locationTypeOnTheMove = locationTypeOnTheMove;
	}

	public ConsumerFeedback getConsumerFeedback() {
		return consumerFeedback;
	}

	public void setConsumerFeedback(ConsumerFeedback consumerFeedback) {
		this.consumerFeedback = consumerFeedback;
	}

	@Override
	public String toString() {
		return "ConsumerCustomFeedback [id=" + id + ", zoom5=" + zoom5 + ", zoom6=" + zoom6 + ", zoom7=" + zoom7 + ", zoom8=" + zoom8 + ", zoom9="
				+ zoom9 + ", zoom10=" + zoom10 + ", zoom11=" + zoom11 + ", zoom12=" + zoom12 + ", zoom13=" + zoom13 + ", zoom14=" + zoom14
				+ ", feedbackDate=" + feedbackDate + ", isDataFeedback=" + isDataFeedback + ", isVoiceFeedback=" + isVoiceFeedback
				+ ", feedbackAddress=" + feedbackAddress + ", internetQualityRate=" + internetQualityRate + ", callsQualityRate=" + callsQualityRate
				+ ", dataSlowConnection=" + dataSlowConnection + ", dataNoConnection=" + dataNoConnection + ", voiceCallMute=" + voiceCallMute
				+ ", voiceCallFailed=" + voiceCallFailed + ", voiceCallOneWay=" + voiceCallOneWay + ", voiceCallDegradation=" + voiceCallDegradation
				+ ", voiceCallNotAvailable=" + voiceCallNotAvailable + ", locationTypeIndoor=" + locationTypeIndoor + ", locationTypeOutside="
				+ locationTypeOutside + ", locationTypeOnTheMove=" + locationTypeOnTheMove + ", consumerFeedback=" + consumerFeedback + "]";
	}

}
