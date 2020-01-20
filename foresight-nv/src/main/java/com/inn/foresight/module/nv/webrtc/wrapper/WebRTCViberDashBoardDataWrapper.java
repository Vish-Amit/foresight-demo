package com.inn.foresight.module.nv.webrtc.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.module.nv.dashboard.passive.wrapper.NVPassiveDeviceWrapper;

@RestWrapper
public class WebRTCViberDashBoardDataWrapper {


	Map<String,Long> totalSubcriber;
	Map<String,Long> totalCalls;
	Map<String,Long> dropCalls;
	Map<String,Long> failedCalls;
	Map<String,Double> mos;
	Map<String,Map<String,Map<String,Long>>> osDistribution;
	Map<String,Map<String,Long>> kpiDistribution;
	List<NVPassiveDeviceWrapper> deviceDistribution = new ArrayList<>();
	public Map<String, Long> getTotalSubcriber() {
		return totalSubcriber;
	}
	public void setTotalSubcriber(Map<String, Long> totalSubcriber) {
		this.totalSubcriber = totalSubcriber;
	}
	public Map<String, Long> getTotalCalls() {
		return totalCalls;
	}
	public void setTotalCalls(Map<String, Long> totalCalls) {
		this.totalCalls = totalCalls;
	}
	public Map<String, Long> getDropCalls() {
		return dropCalls;
	}
	public void setDropCalls(Map<String, Long> dropCalls) {
		this.dropCalls = dropCalls;
	}
	public Map<String, Long> getFailedCalls() {
		return failedCalls;
	}
	public void setFailedCalls(Map<String, Long> failedCalls) {
		this.failedCalls = failedCalls;
	}
	public Map<String, Double> getMos() {
		return mos;
	}
	public void setMos(Map<String, Double> mos) {
		this.mos = mos;
	}
	public Map<String, Map<String, Map<String, Long>>> getOsDistribution() {
		return osDistribution;
	}
	public void setOsDistribution(Map<String, Map<String, Map<String, Long>>> osDistribution) {
		this.osDistribution = osDistribution;
	}
	public Map<String, Map<String, Long>> getKpiDistribution() {
		return kpiDistribution;
	}
	public void setKpiDistribution(Map<String, Map<String, Long>> kpiDistribution) {
		this.kpiDistribution = kpiDistribution;
	}
	public List<NVPassiveDeviceWrapper> getDeviceDistribution() {
		return deviceDistribution;
	}
	public void setDeviceDistribution(List<NVPassiveDeviceWrapper> deviceDistribution) {
		this.deviceDistribution = deviceDistribution;
	}
}
