package com.inn.foresight.module.nv.report.wrapper;

public class NVDashboardPlotWrapper {

	private String kpiImage;
	private String kpiLegend;
	private String title;
	public String getKpiImage() {
		return kpiImage;
	}
	public void setKpiImage(String kpiImage) {
		this.kpiImage = kpiImage;
	}
	public String getKpiLegend() {
		return kpiLegend;
	}
	public void setKpiLegend(String kpiLegend) {
		this.kpiLegend = kpiLegend;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "NVDashboardPlotWrapper [kpiImage=" + kpiImage + ", kpiLegend=" + kpiLegend + ", title=" + title + "]";
	}
	
	
		
}
