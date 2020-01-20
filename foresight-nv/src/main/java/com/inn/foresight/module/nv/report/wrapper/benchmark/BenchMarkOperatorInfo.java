package com.inn.foresight.module.nv.report.wrapper.benchmark;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.HandoverDataWrapper;

@JpaWrapper
public class BenchMarkOperatorInfo {
	private String operatorName;
	private String kpiName;
	private String kpiImg;
	private String kpiLegendImg;
	private Double minValue;
	private Double maxValue;
	private Double avgValue;
    private String heading;
    
    private List<VoiceStatsWrapper> callPlotDataList;
    private List<HandoverDataWrapper>hoDataList;
	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Double getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(Double avgValue) {
		this.avgValue = avgValue;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public String getKpiImg() {
		return kpiImg;
	}

	public void setKpiImg(String kpiImg) {
		this.kpiImg = kpiImg;
	}

	public String getKpiLegendImg() {
		return kpiLegendImg;
	}

	public void setKpiLegendImg(String kpiLegendImg) {
		this.kpiLegendImg = kpiLegendImg;
	}

	
	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}
	

	
	public List<VoiceStatsWrapper> getCallPlotDataList() {
		return callPlotDataList;
	}

	public void setCallPlotDataList(List<VoiceStatsWrapper> callPlotDataList) {
		this.callPlotDataList = callPlotDataList;
	}

	
	public List<HandoverDataWrapper> getHoDataList() {
		return hoDataList;
	}

	public void setHoDataList(List<HandoverDataWrapper> hoDataList) {
		this.hoDataList = hoDataList;
	}

	@Override
	public String toString() {
		return "BenchMarkOperatorInfo [operatorName=" + operatorName + ", kpiName=" + kpiName + ", kpiImg=" + kpiImg
				+ ", kpiLegendImg=" + kpiLegendImg + ", minValue=" + minValue + ", maxValue=" + maxValue + ", avgValue="
				+ avgValue + ", heading=" + heading + "]";
	}

}
