package com.inn.foresight.module.nv.report.wrapper.benchmark;

import java.io.InputStream;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class LegendListWrapper {

	String legendValue;
	InputStream colorImage;
	String remark;
	String operator1Value;
	String operator2Value;
	String operator3Value;
	String operator4Value;
	String kpiName;

	public String getLegendValue() {
		return legendValue;
	}

	public void setLegendValue(String legendValue) {
		this.legendValue = legendValue;
	}

	public InputStream getColorImage() {
		return colorImage;
	}

	public void setColorImage(InputStream colorImage) {
		this.colorImage = colorImage;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOperator1Value() {
		return operator1Value;
	}

	public void setOperator1Value(String operator1Value) {
		this.operator1Value = operator1Value;
	}

	public String getOperator2Value() {
		return operator2Value;
	}

	public void setOperator2Value(String operator2Value) {
		this.operator2Value = operator2Value;
	}

	public String getOperator3Value() {
		return operator3Value;
	}

	public void setOperator3Value(String operator3Value) {
		this.operator3Value = operator3Value;
	}

	public String getOperator4Value() {
		return operator4Value;
	}

	public void setOperator4Value(String operator4Value) {
		this.operator4Value = operator4Value;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	@Override
	public String toString() {
		return "LegendListWrapper [legendValue=" + legendValue + ", colorImage=" + colorImage + ", remark=" + remark
				+ ", operator1Value=" + operator1Value + ", operator2Value=" + operator2Value + ", operator3Value="
				+ operator3Value + ", operator4Value=" + operator4Value + ", kpiName=" + kpiName + "]";
	}

}
