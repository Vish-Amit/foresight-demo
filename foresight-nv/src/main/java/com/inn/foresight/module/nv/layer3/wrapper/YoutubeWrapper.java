package com.inn.foresight.module.nv.layer3.wrapper;

import java.util.Set;

public class YoutubeWrapper {

	
	private String videoURL;
	private Set<String> videoResolution;
	private Long videoDuration;
	private Double noOfStalling;
	private  Double  totalBufferTime;
	private String freezingRatio;
	private  Double[]  avgRSRP;
	private  Double[]  avgSINR;
	
	
	public String getVideoURL() {
		return videoURL;
	}
	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}
	public Set<String> getVideoResolution() {
		return videoResolution;
	}
	public void setVideoResolution(Set<String> videoResolution) {
		this.videoResolution = videoResolution;
	}
	public Long getVideoDuration() {
		return videoDuration;
	}
	public void setVideoDuration(Long videoDuration) {
		this.videoDuration = videoDuration;
	}
	
	public Double getNoOfStalling() {
		return noOfStalling;
	}
	public void setNoOfStalling(Double noOfStalling) {
		this.noOfStalling = noOfStalling;
	}
	public Double getTotalBufferTime() {
		return totalBufferTime;
	}
	public void setTotalBufferTime(Double totalBufferTime) {
		this.totalBufferTime = totalBufferTime;
	}
	public String getFreezingRatio() {
		return freezingRatio;
	}
	public void setFreezingRatio(String freezingRatio) {
		this.freezingRatio = freezingRatio;
	}
	public Double[] getAvgRSRP() {
		return avgRSRP;
	}
	public void setAvgRSRP(Double[] avgRSRP) {
		this.avgRSRP = avgRSRP;
	}
	public Double[] getAvgSINR() {
		return avgSINR;
	}
	public void setAvgSINR(Double[] avgSINR) {
		this.avgSINR = avgSINR;
	}
	
	
}
