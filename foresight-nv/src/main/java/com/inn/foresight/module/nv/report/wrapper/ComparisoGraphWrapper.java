package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class ComparisoGraphWrapper {
	private String kpiName;
	private String unit;
	private List<GraphWrapper>preDriveList;
	private List<GraphWrapper>postDriveList;
	public List<GraphWrapper> getPreDriveList() {
		return preDriveList;
	}
	public void setPreDriveList(List<GraphWrapper> preDriveList) {
		this.preDriveList = preDriveList;
	}
	public List<GraphWrapper> getPostDriveList() {
		return postDriveList;
	}
	public void setPostDriveList(List<GraphWrapper> postDriveList) {
		this.postDriveList = postDriveList;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	

}
