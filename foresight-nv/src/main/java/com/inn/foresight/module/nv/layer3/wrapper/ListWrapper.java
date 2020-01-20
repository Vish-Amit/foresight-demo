package com.inn.foresight.module.nv.layer3.wrapper;

public class ListWrapper {

	private String name;
	private String kpitype;
	private String chartType;
	private Integer index;
	private String unit;
	private String columnname;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getColumnname() {
		return columnname;
	}
	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}
	@Override
	public String toString() {
		return "ListWrapper [name=" + name + ", kpitype=" + kpitype + ", chartType=" + chartType + ", index=" + index
				+ ", unit=" + unit + ", columnname=" + columnname + "]";
	}



	
}
