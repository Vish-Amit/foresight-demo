package com.inn.foresight.module.nv.report.wrapper.inbuilding;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class YoutubeTestWrapper {

	private String videoURL;
	private String videoResolution;
	private Integer videoDuration;
	private String noOfStalling;
	private Integer totalBufferTime;
	private String freezingRatio;
	private Double avgRSRP;
	private Double avgSINR;
	
	/** Http DownLink Fields. */
	private Integer httpDlAttempt;
	private Integer httpDlSuccess;
	private Double httpDlSr;
	private Double httpTimeToDownload;
	private Double httpThpAvg;
	
	
	public Integer getHttpDlAttempt() {
		return httpDlAttempt;
	}
	public void setHttpDlAttempt(Integer httpDlAttempt) {
		this.httpDlAttempt = httpDlAttempt;
	}
	public Integer getHttpDlSuccess() {
		return httpDlSuccess;
	}
	public void setHttpDlSuccess(Integer httpDlSuccess) {
		this.httpDlSuccess = httpDlSuccess;
	}
	public Double getHttpDlSr() {
		return httpDlSr;
	}
	public void setHttpDlSr(Double httpDlSr) {
		this.httpDlSr = httpDlSr;
	}
	public Double getHttpTimeToDownload() {
		return httpTimeToDownload;
	}
	public void setHttpTimeToDownload(Double httpTimeToUpload) {
		this.httpTimeToDownload = httpTimeToUpload;
	}
	public Double getHttpThpAvg() {
		return httpThpAvg;
	}
	public void setHttpThpAvg(Double httpThpAvg) {
		this.httpThpAvg = httpThpAvg;
	}
	public String getVideoURL() {
		return videoURL;
	}
	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}
	public String getVideoResolution() {
		return videoResolution;
	}
	public void setVideoResolution(String videoResolution) {
		this.videoResolution = videoResolution;
	}
	public Integer getVideoDuration() {
		return videoDuration;
	}
	public void setVideoDuration(Integer videoDuration) {
		this.videoDuration = videoDuration;
	}
	public String getNoOfStalling() {
		return noOfStalling;
	}
	public void setNoOfStalling(String noOfStalling) {
		this.noOfStalling = noOfStalling;
	}
	public Integer getTotalBufferTime() {
		return totalBufferTime;
	}
	public void setTotalBufferTime(Integer totalBufferTime) {
		this.totalBufferTime = totalBufferTime;
	}
	public String getFreezingRatio() {
		return freezingRatio;
	}
	public void setFreezingRatio(String freezingRatio) {
		this.freezingRatio = freezingRatio;
	}
	public Double getAvgRSRP() {
		return avgRSRP;
	}
	public void setAvgRSRP(Double avgRSRP) {
		this.avgRSRP = avgRSRP;
	}
	public Double getAvgSINR() {
		return avgSINR;
	}
	public void setAvgSINR(Double avgSINR) {
		this.avgSINR = avgSINR;
	}
	
	


}
