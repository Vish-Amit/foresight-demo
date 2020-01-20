package com.inn.foresight.module.nv.report.stealthdashboard.wrapper;

import java.awt.*;
import java.util.List;

public class StealthGeographyDataWrapper {

	Double latitude;
	Double longitude;
	List<List<List<List<Double>>>> boundaryData;
	Color rsrpPlotColor;
	Color dlPlotColor;
	Double rsrpSum;
	Double dlSum;
	Double sinrSum;
	Double ulSum;
	Double rsrpCount;
	Double dlCount;
	Double sinrCount;
	Double ulCount;
	Double nocoverageCount;
	String geographyName;
	Double avgRSRP;
	Double avgDl;

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public List<List<List<List<Double>>>> getBoundaryData() {
		return boundaryData;
	}

	public void setBoundaryData(List<List<List<List<Double>>>> boundaryData) {
		this.boundaryData = boundaryData;
	}

	public Color getRsrpPlotColor() {
		return rsrpPlotColor;
	}

	public void setRsrpPlotColor(Color rsrpPlotColor) {
		this.rsrpPlotColor = rsrpPlotColor;
	}

	public Color getDlPlotColor() {
		return dlPlotColor;
	}

	public void setDlPlotColor(Color dlPlotColor) {
		this.dlPlotColor = dlPlotColor;
	}

	public String getGeographyName() {
		return geographyName;
	}

	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
	}

	public Double getRsrpSum() {
		return rsrpSum;
	}

	public void setRsrpSum(Double rsrpSum) {
		this.rsrpSum = rsrpSum;
	}

	public Double getDlSum() {
		return dlSum;
	}

	public void setDlSum(Double dlSum) {
		this.dlSum = dlSum;
	}

	public Double getSinrSum() {
		return sinrSum;
	}

	public void setSinrSum(Double sinrSum) {
		this.sinrSum = sinrSum;
	}

	public Double getUlSum() {
		return ulSum;
	}

	public void setUlSum(Double ulSum) {
		this.ulSum = ulSum;
	}

	public Double getRsrpCount() {
		return rsrpCount;
	}

	public void setRsrpCount(Double rsrpCount) {
		this.rsrpCount = rsrpCount;
	}

	public Double getDlCount() {
		return dlCount;
	}

	public void setDlCount(Double dlCount) {
		this.dlCount = dlCount;
	}

	public Double getSinrCount() {
		return sinrCount;
	}

	public void setSinrCount(Double sinrCount) {
		this.sinrCount = sinrCount;
	}

	public Double getUlCount() {
		return ulCount;
	}

	public void setUlCount(Double ulCount) {
		this.ulCount = ulCount;
	}

	public Double getNocoverageCount() {
		return nocoverageCount;
	}

	public void setNocoverageCount(Double nocoverageCount) {
		this.nocoverageCount = nocoverageCount;
	}

	public Double getAvgRSRP() {
		return avgRSRP;
	}

	public void setAvgRSRP(Double avgRSRP) {
		this.avgRSRP = avgRSRP;
	}

	public Double getAvgDl() {
		return avgDl;
	}

	public void setAvgDl(Double avgDl) {
		this.avgDl = avgDl;
	}
}
