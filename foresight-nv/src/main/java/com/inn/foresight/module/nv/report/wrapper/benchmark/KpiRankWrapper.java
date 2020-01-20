package com.inn.foresight.module.nv.report.wrapper.benchmark;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class KpiRankWrapper {

	String rank;
	String operatorName;
	String avgKpi;
	String kpiPercent;

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getAvgKpi() {
		return avgKpi;
	}

	public void setAvgKpi(String avgKpi) {
		this.avgKpi = avgKpi;
	}

	public String getKpiPercent() {
		return kpiPercent;
	}

	public void setKpiPercent(String kpiPercent) {
		this.kpiPercent = kpiPercent;
	}

	@Override
	public String toString() {
		return "KpiRankWrapper [rank=" + rank + ", operatorName=" + operatorName + ", avgKpi=" + avgKpi
				+ ", kpiPercent=" + kpiPercent + "]";
	}

}
