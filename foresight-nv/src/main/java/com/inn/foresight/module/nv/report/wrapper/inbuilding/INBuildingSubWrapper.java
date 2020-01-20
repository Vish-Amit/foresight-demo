package com.inn.foresight.module.nv.report.wrapper.inbuilding;

import java.util.List;

import com.inn.foresight.module.nv.report.wrapper.benchmark.VoiceStatsWrapper;

public class INBuildingSubWrapper {

	private String testDate;
	private String buildingName;
	private String floorName;
	private String wingName;
	private String unitName ;
	private String fileName;
	private String recipeName;
	private String operatorName;
	private Boolean showCallPage;
	List<WalkTestSummaryWrapper>sumaaryList;
    List<NoAccessImgWrapper>noAccessimglist;
    List<VoiceStatsWrapper> callPlotDataList;
    
    private String isNoAccessAvailable;
    
	public String getFileName() {
		return fileName;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getWingName() {
		return wingName;
	}
	public void setWingName(String wingName) {
		this.wingName = wingName;
	}
	public String getTestDate() {
		return testDate;
	}
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getFloorName() {
		return floorName;
	}
	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public List<WalkTestSummaryWrapper> getSumaaryList() {
		return sumaaryList;
	}
	public void setSumaaryList(List<WalkTestSummaryWrapper> sumaaryList) {
		this.sumaaryList = sumaaryList;
	}


	public List<NoAccessImgWrapper> getNoAccessimglist() {
		return noAccessimglist;
	}

	public void setNoAccessimglist(List<NoAccessImgWrapper> noAccessimglist) {
		this.noAccessimglist = noAccessimglist;
	}

	
	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	

	public String getIsNoAccessAvailable() {
		return isNoAccessAvailable;
	}

	public void setIsNoAccessAvailable(String isNoAccessAvailable) {
		this.isNoAccessAvailable = isNoAccessAvailable;
	}

	public List<VoiceStatsWrapper> getCallPlotDataList() {
		return callPlotDataList;
	}

	public void setCallPlotDataList(List<VoiceStatsWrapper> callPlotDataList) {
		this.callPlotDataList = callPlotDataList;
	}

	public Boolean getShowCallPage() {
		return showCallPage;
	}

	public void setShowCallPage(Boolean showCallPage) {
		this.showCallPage = showCallPage;
	}

	

}
