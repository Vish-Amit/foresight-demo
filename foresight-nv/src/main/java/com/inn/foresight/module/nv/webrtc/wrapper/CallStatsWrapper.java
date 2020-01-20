package com.inn.foresight.module.nv.webrtc.wrapper;

import java.util.Map;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class CallStatsWrapper {
	
	Map<String,Map<String,Map<String,Object>>> callTypeData;

	Double avgTotalMos;

	
	public Map<String, Map<String, Map<String, Object>>> getCallTypeData() {
		return callTypeData;
	}

	public void setCallTypeData(Map<String, Map<String, Map<String, Object>>> callTypeData) {
		this.callTypeData = callTypeData;
	}

	public Double getAvgTotalMos() {
		return avgTotalMos;
	}

	public void setAvgTotalMos(Double avgTotalMos) {
		this.avgTotalMos = avgTotalMos;
	}

	@Override
	public String toString() {
		return "CallStatsWrapper [callTypeData=" + callTypeData + ", avgTotalMos=" + avgTotalMos + "]";
	}
}
