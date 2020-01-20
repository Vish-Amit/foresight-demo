package com.inn.foresight.module.nv.layer3.wrapper;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class KpiAdvanceWrapper {	
	
	private String name;
	
	@SerializedName("list")
	private List<KpiWrapper> kpiListwrapper;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<KpiWrapper> getKpiListwrapper() {
		return kpiListwrapper;
	}
	public void setKpiListwrapper(List<KpiWrapper> kpiListwrapper) {
		this.kpiListwrapper = kpiListwrapper;
	}
}
