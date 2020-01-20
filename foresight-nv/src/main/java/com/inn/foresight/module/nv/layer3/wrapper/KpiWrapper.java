package com.inn.foresight.module.nv.layer3.wrapper;

public class KpiWrapper {

	private String name;
	private String kpitype;
	private String chartType;
	private Integer index;
	private String columnname;
	private String unit;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKpitype() {
		return kpitype;
	}
	public void setKpitype(String kpitype) {
		this.kpitype = kpitype;
	}
	public String getChartType() {
		return chartType;
	}
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getColumnname() {
		return columnname;
	}
	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	@Override
	public String toString() {
		return "KpiWrapper [name=" + name + ", kpitype=" + kpitype + ", chartType=" + chartType + ", index=" + index
				+ ", columnname=" + columnname + ", unit=" + unit + "]";
	}




	

}
