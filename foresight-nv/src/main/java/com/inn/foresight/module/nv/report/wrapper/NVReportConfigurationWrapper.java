package com.inn.foresight.module.nv.report.wrapper;

/** The Class NVReportConfigurationWrapper. */
public class NVReportConfigurationWrapper {

	/** The vendor. */
	private String vendor;

	/** The operator. */
	private String operator;

	/** The kpi. */
	private String kpi;

	/** The configuration. */
	private String configuration;

	/** The report type. */
	private String reportType;

	/** The targetvalue. */
	private String targetvalue;



	public NVReportConfigurationWrapper() {
		super();
	}

	public NVReportConfigurationWrapper(String vendor, String operator, String kpi, String configuration,
			 String targetvalue) {
		super();
		this.vendor = vendor;
		this.operator = operator;
		this.kpi = kpi;
		this.configuration = configuration;
		this.targetvalue = targetvalue;
	}

	/**
	 * Gets the vendor.
	 *
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * Sets the vendor.
	 *
	 * @param vendor the new vendor
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
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
	 * Gets the kpi.
	 *
	 * @return the kpi
	 */
	public String getKpi() {
		return kpi;
	}

	/**
	 * Sets the kpi.
	 *
	 * @param kpi the new kpi
	 */
	public void setKpi(String kpi) {
		this.kpi = kpi;
	}

	/**
	 * Gets the configuration.
	 *
	 * @return the configuration
	 */
	public String getConfiguration() {
		return configuration;
	}

	/**
	 * Sets the configuration.
	 *
	 * @param configuration the new configuration
	 */
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	/**
	 * Gets the report type.
	 *
	 * @return the report type
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * Sets the report type.
	 *
	 * @param reportType the new report type
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * Gets the targetvalue.
	 *
	 * @return the targetvalue
	 */
	public String getTargetvalue() {
		return targetvalue;
	}

	/**
	 * Sets the targetvalue.
	 *
	 * @param targetvalue the new targetvalue
	 */
	public void setTargetvalue(String targetvalue) {
		this.targetvalue = targetvalue;
	}


	@Override
	public String toString() {
		return "NVReportConfigurationWrapper [vendor=" + vendor + ", operator=" + operator + ", kpi=" + kpi
				+ ", configuration=" + configuration + ", reportType=" + reportType + ", targetvalue=" + targetvalue
				+ "]";
	}

}
