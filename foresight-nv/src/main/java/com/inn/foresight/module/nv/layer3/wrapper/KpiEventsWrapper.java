package com.inn.foresight.module.nv.layer3.wrapper;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class KpiEventsWrapper {

	@SerializedName("kpi_lte")
	private List<KpiAdvanceWrapper> kpiAdvanceWrapperlist;
	
	@SerializedName("kpi_3g")
	private List<KpiWrapper> kpi3gwrapperlist;
	
	@SerializedName("kpi_2g")
	private List<KpiWrapper> kpi2gwrapperlist;
	
	@SerializedName("event")
	private List<EventWrapper> eventwrapperlist;

	public List<KpiAdvanceWrapper> getKpiAdvanceWrapperlist() {
		return kpiAdvanceWrapperlist;
	}

	public void setKpiAdvanceWrapperlist(List<KpiAdvanceWrapper> kpiAdvanceWrapperlist) {
		this.kpiAdvanceWrapperlist = kpiAdvanceWrapperlist;
	}

	public List<KpiWrapper> getKpi3gwrapperlist() {
		return kpi3gwrapperlist;
	}

	public void setKpi3gwrapperlist(List<KpiWrapper> kpi3gwrapperlist) {
		this.kpi3gwrapperlist = kpi3gwrapperlist;
	}

	public List<KpiWrapper> getKpi2gwrapperlist() {
		return kpi2gwrapperlist;
	}

	public void setKpi2gwrapperlist(List<KpiWrapper> kpi2gwrapperlist) {
		this.kpi2gwrapperlist = kpi2gwrapperlist;
	}

	public List<EventWrapper> getEventwrapperlist() {
		return eventwrapperlist;
	}

	public void setEventwrapperlist(List<EventWrapper> eventwrapperlist) {
		this.eventwrapperlist = eventwrapperlist;
	}

	

	
	
	
}
