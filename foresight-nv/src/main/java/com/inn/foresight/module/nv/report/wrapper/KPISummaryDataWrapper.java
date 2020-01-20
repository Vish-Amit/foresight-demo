package com.inn.foresight.module.nv.report.wrapper;

/** The Class KPISummaryDataWrapper. */
public class KPISummaryDataWrapper {

/** The s no. */
private String sNo;

/** The test name. */
private String testName;

/** The target. */
private String target;

/** The achived. */
private String achived;

private String postAchived;

/** The status. */
private String status;

/** The source. */
private String source;

private String item;

private String kpiName;

private Integer total;
private Integer success;

/**
 * Gets the s no.
 *
 * @return the s no
 */




public String getsNo() {
	return sNo;
}

public String getKpiName() {
	return kpiName;
}

public void setKpiName(String kpiName) {
	this.kpiName = kpiName;
}

public String getItem() {
	return item;
}

public void setItem(String item) {
	this.item = item;
}

/**
 * Sets the s no.
 *
 * @param sNo the new s no
 */
public void setsNo(String sNo) {
	this.sNo = sNo;
}

/**
 * Gets the test name.
 *
 * @return the test name
 */
public String getTestName() {
	return testName;
}

/**
 * Sets the test name.
 *
 * @param testName the new test name
 */
public void setTestName(String testName) {
	this.testName = testName;
}

/**
 * Gets the target.
 *
 * @return the target
 */
public String getTarget() {
	return target;
}

/**
 * Sets the target.
 *
 * @param target the new target
 */
public void setTarget(String target) {
	this.target = target;
}

/**
 * Gets the achived.
 *
 * @return the achived
 */
public String getAchived() {
	return achived;
}

/**
 * Sets the achived.
 *
 * @param achived the new achived
 */
public void setAchived(String achived) {
	this.achived = achived;
}

/**
 * Gets the status.
 *
 * @return the status
 */
public String getStatus() {
	return status;
}

/**
 * Sets the status.
 *
 * @param status the new status
 */
public void setStatus(String status) {
	this.status = status;
}

/**
 * Gets the source.
 *
 * @return the source
 */
public String getSource() {
	return source;
}

/**
 * Sets the source.
 *
 * @param source the new source
 */
public void setSource(String source) {
	this.source = source;
}


public String getPostAchived() {
	return postAchived;
}

public void setPostAchived(String postAchived) {
	this.postAchived = postAchived;
}

public Integer getTotal() {
	return total;
}

public void setTotal(Integer total) {
	this.total = total;
}

public Integer getSuccess() {
	return success;
}

public void setSuccess(Integer success) {
	this.success = success;
}

@Override
public String toString() {
	return "KPISummaryDataWrapper [sNo=" + sNo + ", testName=" + testName + ", target=" + target + ", achived="
			+ achived + ", postAchived=" + postAchived + ", status=" + status + ", source=" + source + ", item=" + item
			+ ", kpiName=" + kpiName + ", total=" + total + ", success=" + success + "]";
}

}
