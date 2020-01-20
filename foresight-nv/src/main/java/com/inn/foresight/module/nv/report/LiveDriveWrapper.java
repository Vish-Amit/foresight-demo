package com.inn.foresight.module.nv.report;

import java.util.ArrayList;
import java.util.List;

import com.inn.foresight.module.nv.report.wrapper.ChartBeanWrapper;
import com.inn.foresight.module.nv.report.wrapper.RangeScoreWrapper;

public class LiveDriveWrapper {
	
	private Double min;
	private Double max;
	private Double mean;
	private Double avg;
	private Double per90;
	private Double per10;
	private Double stdev;
	private String KPI;
	private String chartTitle;
	private Double medKPI;
	private String score;
	private String chartType;
	private String KPIUnit;
	private String graphHeading;
	private Double totalCount;
	private Integer countRange1 = 0;
	private Integer countRange2 = 0;
	private Integer countRange3 = 0;
	private Integer countRange4 = 0;
	private Double pdfRange1 = 0.0;
	private Double pdfRange2 = 0.0;
	private Double pdfRange3 = 0.0;
	private Double pdfRange4 = 0.0;

	private List<Double> values = new ArrayList<>();

	List<RangeSlab> rangeSlabs;

	private List<RangeScoreWrapper> rangeScoreWrapperList;

	private List<ChartBeanWrapper> chartList;
	

	public List<Double> getValues() {
		return values;
	}

	public void setValues(List<Double> values) {
		this.values = values;
	}

	public List<RangeScoreWrapper> getRangeScoreWrapperList() {
		return rangeScoreWrapperList;
	}

	public void setRangeScoreWrapperList(List<RangeScoreWrapper> rangeScoreWrapperList) {
		this.rangeScoreWrapperList = rangeScoreWrapperList;
	}

	public void addValue(Double value) {
		if (value != null && !value.equals(Double.NaN)) {
			values.add(value);
		}
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMean() {
		return mean;
	}

	public void setMean(Double mean) {
		this.mean = mean;
	}

	public Double getAvg() {
		return avg;
	}

	public void setAvg(Double avg) {
		this.avg = avg;
	}

	public Double getPer90() {
		return per90;
	}

	public void setPer90(Double per90) {
		this.per90 = per90;
	}

	public Double getPer10() {
		return per10;
	}

	public void setPer10(Double per10) {
		this.per10 = per10;
	}

	public Double getStdev() {
		return stdev;
	}

	public void setStdev(Double stdev) {
		this.stdev = stdev;
	}

	public String getKPI() {
		return KPI;
	}

	public void setKPI(String kPI) {
		KPI = kPI;
	}

	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public Double getMedKPI() {
		return medKPI;
	}

	public void setMedKPI(Double medKPI) {
		this.medKPI = medKPI;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getKPIUnit() {
		return KPIUnit;
	}

	public void setKPIUnit(String kPIUnit) {
		KPIUnit = kPIUnit;
	}

	public String getGraphHeading() {
		return graphHeading;
	}

	public void setGraphHeading(String graphHeading) {
		this.graphHeading = graphHeading;
	}

	public List<RangeSlab> getRangeSlabs() {
		return rangeSlabs;
	}

	public void setRangeSlabs(List<RangeSlab> rangeSlabs) {
		this.rangeSlabs = rangeSlabs;
	}

	public Double getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Double totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getCountRange1() {
		return countRange1;
	}

	public void setCountRange1(Integer countRange1) {
		this.countRange1 = countRange1;
	}

	public Integer getCountRange2() {
		return countRange2;
	}

	public void setCountRange2(Integer countRange2) {
		this.countRange2 = countRange2;
	}

	public Integer getCountRange3() {
		return countRange3;
	}

	public void setCountRange3(Integer countRange3) {
		this.countRange3 = countRange3;
	}

	public Integer getCountRange4() {
		return countRange4;
	}

	public void setCountRange4(Integer countRange4) {
		this.countRange4 = countRange4;
	}

	public Double getPdfRange1() {
		return pdfRange1;
	}

	public void setPdfRange1(Double pdfRange1) {
		this.pdfRange1 = pdfRange1;
	}

	public Double getPdfRange2() {
		return pdfRange2;
	}

	public void setPdfRange2(Double pdfRange2) {
		this.pdfRange2 = pdfRange2;
	}

	public Double getPdfRange3() {
		return pdfRange3;
	}

	public void setPdfRange3(Double pdfRange3) {
		this.pdfRange3 = pdfRange3;
	}

	public Double getPdfRange4() {
		return pdfRange4;
	}

	public void setPdfRange4(Double pdfRange4) {
		this.pdfRange4 = pdfRange4;
	}

	public List<ChartBeanWrapper> getChartList() {
		return chartList;
	}

	public void setChartList(List<ChartBeanWrapper> chartList) {
		this.chartList = chartList;
	}

	public void incrCountRange1() {
		this.countRange1++;
	}

	public void incrCountRange2() {
		this.countRange2++;
	}

	public void incrCountRange3() {
		this.countRange3++;
	}

	public void incrCountRange4() {
		this.countRange4++;
	}

	@Override
	public String toString() {
		return "LiveDriveWrapper [min=" + min + ", max=" + max + ", mean=" + mean + ", avg=" + avg + ", per90=" + per90
				+ ", per10=" + per10 + ", stdev=" + stdev + ", KPI=" + KPI + ", chartTitle=" + chartTitle + ", medKPI="
				+ medKPI + ", score=" + score + ", chartType=" + chartType + ", KPIUnit=" + KPIUnit + ", graphHeading="
				+ graphHeading + ", totalCount=" + totalCount + ", countRange1=" + countRange1 + ", countRange2="
				+ countRange2 + ", countRange3=" + countRange3 + ", countRange4=" + countRange4 + ", pdfRange1="
				+ pdfRange1 + ", pdfRange2=" + pdfRange2 + ", pdfRange3=" + pdfRange3 + ", pdfRange4=" + pdfRange4
				+ ", values=" + values + ", rangeSlabs=" + rangeSlabs + ", rangeScoreWrapperList="
				+ rangeScoreWrapperList + ", chartList=" + chartList + "]";
	}

}
