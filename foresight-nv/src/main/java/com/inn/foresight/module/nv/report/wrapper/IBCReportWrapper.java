package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class IBCReportWrapper {

	private String  heading;
	private List<IBCTableDataHolder>table;
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public List<IBCTableDataHolder> getTable() {
		return table;
	}
	public void setTable(List<IBCTableDataHolder> table) {
		this.table = table;
	}
	
	
}
