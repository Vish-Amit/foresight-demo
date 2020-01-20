package com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark;

public class IBBenchmarkGraphDataWrapper {

	String operator1;
	String operator2;
	String operator3;
	String operator4;
	Double operator1CDF;
	Double operator1PDF;
	Double operator2CDF;
	Double operator2PDF;
	Double operator3CDF;
	Double operator3PDF;
	Double operator4CDF;
	Double operator4PDF;
	String rateKpi;
	String xAxisLabel;
	String yAxisLabel;
	private Double from;
	private Double to;
	private Integer count;
	private String kpiName;
	
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
	public Double getOperator1CDF() {
		return operator1CDF;
	}
	public void setOperator1CDF(Double operator1cdf) {
		operator1CDF = operator1cdf;
	}
	public Double getOperator1PDF() {
		return operator1PDF;
	}
	public void setOperator1PDF(Double operator1pdf) {
		operator1PDF = operator1pdf;
	}
	public Double getOperator2CDF() {
		return operator2CDF;
	}
	public void setOperator2CDF(Double operator2cdf) {
		operator2CDF = operator2cdf;
	}
	public Double getOperator2PDF() {
		return operator2PDF;
	}
	public void setOperator2PDF(Double operator2pdf) {
		operator2PDF = operator2pdf;
	}
	public Double getOperator3CDF() {
		return operator3CDF;
	}
	public void setOperator3CDF(Double operator3cdf) {
		operator3CDF = operator3cdf;
	}
	public Double getOperator3PDF() {
		return operator3PDF;
	}
	public void setOperator3PDF(Double operator3pdf) {
		operator3PDF = operator3pdf;
	}
	public Double getOperator4CDF() {
		return operator4CDF;
	}
	public void setOperator4CDF(Double operator4cdf) {
		operator4CDF = operator4cdf;
	}
	public Double getOperator4PDF() {
		return operator4PDF;
	}
	public void setOperator4PDF(Double operator4pdf) {
		operator4PDF = operator4pdf;
	}
	public String getRateKpi() {
		return rateKpi;
	}
	public void setRateKpi(String rateKpi) {
		this.rateKpi = rateKpi;
	}
	public String getxAxisLabel() {
		return xAxisLabel;
	}
	public void setxAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
	}
	public String getyAxisLabel() {
		return yAxisLabel;
	}
	public void setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}
	public Double getFrom() {
		return from;
	}
	public void setFrom(Double from) {
		this.from = from;
	}
	public Double getTo() {
		return to;
	}
	public void setTo(Double to) {
		this.to = to;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	@Override
	public String toString() {
		return "IBBenchmarkGraphDataWrapper [operator1=" + operator1 + ", operator2=" + operator2 + ", operator3="
				+ operator3 + ", operator4=" + operator4 + ", operator1CDF=" + operator1CDF + ", operator1PDF="
				+ operator1PDF + ", operator2CDF=" + operator2CDF + ", operator2PDF=" + operator2PDF + ", operator3CDF="
				+ operator3CDF + ", operator3PDF=" + operator3PDF + ", operator4CDF=" + operator4CDF + ", operator4PDF="
				+ operator4PDF + ", rateKpi=" + rateKpi + ", xAxisLabel=" + xAxisLabel + ", yAxisLabel=" + yAxisLabel
				+ ", from=" + from + ", to=" + to + ", count=" + count + ", kpiName=" + kpiName + "]";
	}
	
	
	
}
