package com.inn.foresight.module.nv.report.wrapper.inbuilding;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class LiveDriveVoiceAndSmsWrapper {

	private String test;
	private String testType;
	private String testedKpiName;
	private String kpiStatus;
	private String targetValue;
	private String isTargetAchived;
	private String imgPath;

	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public String getTestedKpiName() {
		return testedKpiName;
	}
	public void setTestedKpiName(String testedKpiName) {
		this.testedKpiName = testedKpiName;
	}
	public String getTargetValue() {
		return targetValue;
	}
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}
	public String getKpiStatus() {
		return kpiStatus;
	}
	public void setKpiStatus(String kpiStatus) {
		this.kpiStatus = kpiStatus;
	}
	public String getIsTargetAchived() {
		return isTargetAchived;
	}
	public void setIsTargetAchived(String isTargetAchived) {
		this.isTargetAchived = isTargetAchived;
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	
}
