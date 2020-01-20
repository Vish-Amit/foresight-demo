package com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark;

import java.util.Map;

public class IBMappingwrapper implements Comparable<IBMappingwrapper> {
	private String operatorName;
	private Double avgValue;
	private String kpi;
	private Map<KPI, Double> kpiWiseAverage;
	private Double operator1Score;
	private Double operator2Score;
	private Double operator3Score;
	private Double operator4Score;
	private String operator1;
	private String operator2;
	private String operator3;
	private String operator4;

	public String getOperator1() {
		return operator1;
	}

	public void setOperator1(String operator1) {
		this.operator1 = operator1;
	}

	public String getOperator2() {
		return operator2;
	}

	public void setOperator2(String operator2) {
		this.operator2 = operator2;
	}

	public String getOperator3() {
		return operator3;
	}

	public void setOperator3(String operator3) {
		this.operator3 = operator3;
	}

	public String getOperator4() {
		return operator4;
	}

	public void setOperator4(String operator4) {
		this.operator4 = operator4;
	}

	@Override
	public int compareTo(IBMappingwrapper other) {

		if (other.avgValue != null) {
			try {
				return this.avgValue > other.avgValue ? -1 : 1;
			} catch (Exception e) {
				return 0;
			}
		}
		return 0;
	}

	public Double getoperator1Score() {
		return operator1Score;
	}

	public void setoperator1Score(Double operator1Score) {
		this.operator1Score = operator1Score;
	}

	public Double getoperator2Score() {
		return operator2Score;
	}

	public void setoperator2Score(Double operator2Score) {
		this.operator2Score = operator2Score;
	}

	public Double getoperator3Score() {
		return operator3Score;
	}

	public void setoperator3Score(Double operator3Score) {
		this.operator3Score = operator3Score;
	}

	public Double getoperator4Score() {
		return operator4Score;
	}

	public void setoperator4Score(Double operator4Score) {
		this.operator4Score = operator4Score;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Double getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(Double avgValue) {
		this.avgValue = avgValue;
	}

	public String getKpi() {
		return kpi;
	}

	public void setKpi(String kpi) {
		this.kpi = kpi;
	}

	public Map<KPI, Double> getKpiWiseAverage() {
		return kpiWiseAverage;
	}

	public void setKpiWiseAverage(Map<KPI, Double> kpiWiseAverage) {
		this.kpiWiseAverage = kpiWiseAverage;
	}

	@Override
	public String toString() {
		return "Mappingwrapper [opreatorName=" + operatorName + ", avgValue=" + avgValue + ", kpi=" + kpi
				+ ", kpiWiseAverage=" + kpiWiseAverage + ", operator1Score=" + operator1Score + ", operator2Score=" + operator2Score
				+ ", operator3Score=" + operator3Score + ", operator4Score=" + operator4Score + "]";
	}

}
