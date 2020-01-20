package com.inn.foresight.module.nv.report.wrapper;

import java.util.Date;

public class GraphDataWrapper implements Comparable<GraphDataWrapper>{

	private Double cdfValue;
	private Double pdfValue;
	private Double from;
	private Double to;
	private Integer count;
	private String kpiName;

	private Double value;
	private Date time;
	private Double value2;
	
	private Double deviceCount;
	
	public Double getValue2() {
		return value2;
	}
	public void setValue2(Double value2) {
		this.value2 = value2;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
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
	public Double getCdfValue() {
		return cdfValue;
	}
	public void setCdfValue(Double cdfValue) {
		this.cdfValue = cdfValue;
	}
	public Double getPdfValue() {
		return pdfValue;
	}
	public void setPdfValue(Double pdfValue) {
		this.pdfValue = pdfValue;
	}

	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	
	public Double getDeviceCount() {
		return deviceCount;
	}
	public void setDeviceCount(Double deviceCount) {
		this.deviceCount = deviceCount;
	}
	
	@Override
	public String toString() {
		return "GraphDataWrapper [cdfValue=" + cdfValue + ", pdfValue=" + pdfValue + ", from=" + from + ", to=" + to
				+ ", count=" + count + ", kpiName=" + kpiName + ", value=" + value + ", time=" + time + ", value2="
				+ value2 + ", deviceCount=" + deviceCount + "]";
	}
	
	@Override
	public int compareTo(GraphDataWrapper o) {
		if (this.from - o.from == 0.0) {
            return 0;
        } else if (this.from - o.from > 0) {
            return 1;
        } else if (this.from - o.from < 0) {
            return -1;
        } else {
            return 0;
        }
	}

}
