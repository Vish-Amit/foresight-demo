package com.inn.foresight.module.nv.workorder.stealth.kpi.wrapper;

/** The Class DriveTestKPIWrapper. */
public class DriveTestKPIWrapper {

	/** The device WO test id. */
	private Integer deviceWOTestId;
	
	/** The device id. */
	private String deviceId;
	
	/** The test start time. */
	private Long testStartTime;
	
	/** The test end time. */
	private Long testEndTime;
	
	/** The test result. */
	private String testResult;
	
	/** The status. */
	private String status;
	
	/** The total iteration. */
	private Integer totalIteration;
	
	/** The completed iteration. */
	private Integer completedIteration;
	
	/** The remark. */
	private String remark;
	
	/** The operator. */
	private String operator;
	
	/** The technology. */
	private String technology;

	/** Instantiates a new drive test KPI wrapper. */
	public DriveTestKPIWrapper() {
		super();
	}

	/**
	 * Instantiates a new drive test KPI wrapper.
	 *
	 * @param deviceWOTestId the device WO test id
	 */
	public DriveTestKPIWrapper(Integer deviceWOTestId) {
		super();
		this.deviceWOTestId = deviceWOTestId;
	}

	/**
	 * Gets the device WO test id.
	 *
	 * @return the device WO test id
	 */
	public Integer getDeviceWOTestId() {
		return deviceWOTestId;
	}
	
	/**
	 * Sets the device WO test id.
	 *
	 * @param deviceWOTestId the new device WO test id
	 */
	public void setDeviceWOTestId(Integer deviceWOTestId) {
		this.deviceWOTestId = deviceWOTestId;
	}
	
	/**
	 * Gets the test start time.
	 *
	 * @return the test start time
	 */
	public Long getTestStartTime() {
		return testStartTime;
	}
	
	/**
	 * Sets the test start time.
	 *
	 * @param testStartTime the new test start time
	 */
	public void setTestStartTime(Long testStartTime) {
		this.testStartTime = testStartTime;
	}
	
	/**
	 * Gets the test end time.
	 *
	 * @return the test end time
	 */
	public Long getTestEndTime() {
		return testEndTime;
	}
	
	/**
	 * Sets the test end time.
	 *
	 * @param testEndTime the new test end time
	 */
	public void setTestEndTime(Long testEndTime) {
		this.testEndTime = testEndTime;
	}
	
	/**
	 * Gets the test result.
	 *
	 * @return the test result
	 */
	public String getTestResult() {
		return testResult;
	}
	
	/**
	 * Sets the test result.
	 *
	 * @param testResult the new test result
	 */
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	
	/**
	 * Gets the total iteration.
	 *
	 * @return the total iteration
	 */
	public Integer getTotalIteration() {
		return totalIteration;
	}
	
	/**
	 * Sets the total iteration.
	 *
	 * @param totalIteration the new total iteration
	 */
	public void setTotalIteration(Integer totalIteration) {
		this.totalIteration = totalIteration;
	}
	
	/**
	 * Gets the completed iteration.
	 *
	 * @return the completed iteration
	 */
	public Integer getCompletedIteration() {
		return completedIteration;
	}
	
	/**
	 * Sets the completed iteration.
	 *
	 * @param completedIteration the new completed iteration
	 */
	public void setCompletedIteration(Integer completedIteration) {
		this.completedIteration = completedIteration;
	}

	/**
	 * Gets the remark.
	 *
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * Sets the remark.
	 *
	 * @param remark the new remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 *
	 * @param operator the new operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 *
	 * @param technology the new technology
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * Gets the device id.
	 *
	 * @return the device id
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 *
	 * @param deviceId the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "DriveTestKPIWrapper [deviceWOTestId=" + deviceWOTestId + ", deviceId=" + deviceId + ", testStartTime=" + testStartTime
				+ ", testEndTime=" + testEndTime + ", testResult=" + testResult + ", status=" + status + ", totalIteration=" + totalIteration
				+ ", completedIteration=" + completedIteration + ", remark=" + remark + ", operator=" + operator + ", technology=" + technology + "]";
	}

	
}