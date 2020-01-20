package com.inn.foresight.module.nv.report.wrapper;

public class RemarkDataWrapper {
  private String remarkData;
  private String testSkipData;
  private String remark;
  private String testType;
  private String recipeName;
  private String faliuerCause;
  private String testStatus;


public String getRemarkData() {
	return remarkData;
}
public void setRemarkData(String remarkData) {
	this.remarkData = remarkData;
}
public String getTestSkipData() {
	return testSkipData;
}
public void setTestSkipData(String testSkipData) {
	this.testSkipData = testSkipData;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
public String getTestType() {
	return testType;
}
public void setTestType(String testType) {
	this.testType = testType;
}
public String getRecipeName() {
	return recipeName;
}
public void setRecipeName(String recipeName) {
	this.recipeName = recipeName;
}
public String getFaliuerCause() {
	return faliuerCause;
}
public void setFaliuerCause(String faliuerCause) {
	this.faliuerCause = faliuerCause;
}
public String getTestStatus() {
	return testStatus;
}
public void setTestStatus(String testStatus) {
	this.testStatus = testStatus;
}

@Override
public String toString() {
	return "RemarkDataWrapper [remarkData=" + remarkData + ", testSkipData=" + testSkipData + ", remark=" + remark
			+ ", testType=" + testType + ", recipeName=" + recipeName + ", faliuerCause=" + faliuerCause
			+ ", testStatus=" + testStatus + "]";
}
  
}
