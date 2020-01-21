package com.inn.foresight.core.infra.wrapper;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.wrapper.RestWrapper;
@RestWrapper
public class KPISummaryDataWrapper {

	Map kpiMap;
	List<String> geographyList;
	List polyList;
	public Map getKpiMap() {
		return kpiMap;
	}
	public void setKpiMap(Map kpiMap) {
		this.kpiMap = kpiMap;
	}
	public List<String> getGeographyList() {
		return geographyList;
	}
	public void setGeographyList(List<String> geographyList) {
		this.geographyList = geographyList;
	}
	public List getPolyList() {
		return polyList;
	}
	public void setPolyList(List polyList) {
		this.polyList = polyList;
	}
	@Override
	public String toString() {
		return "KPISummaryDataWrapper [kpiMap=" + kpiMap + ", geographyList=" + geographyList + ", polyList=" + polyList
				+ "]";
	}

}
