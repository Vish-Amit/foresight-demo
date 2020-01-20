package com.inn.foresight.module.nv.wpt.analytics.utils.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

/** The Class WPTDashboardKpiWrapper. */
@JpaWrapper
public class WPTDashboardKpiWrapper {
		
	/** The date. */
	private String date;
	
	/** The operator. */
	private String operator;
	
	/** The category. */
	private String category;
	
	/** The ip version. */
	private String ipVersion;
	
	/** The geography L 1. */
	private Integer geographyL1;
	
	/** The geography L 2. */
	private Integer geographyL2;

	/** The geography L 3. */
	private Integer geographyL3;

	/** The geography L 4. */
	private Integer geographyL4;

	/** The avg kpi A. */
	private Double avgKpiA;
	
	/** The avg kpi B. */
	private Double avgKpiB;
		
	/** The min TTFB. */
	private Double minTTFB;
	
	/** The max TTFB. */
	private Double maxTTFB;

	
	/**
	 * Instantiates a new WPT dashboard kpi wrapper.
	 *
	 * @param date the date
	 * @param avgKpiA the avg kpi A
	 * @param avgKpiB the avg kpi B
	 * @param operator the operator
	 * @param ipVersion the ip version
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 */
	public WPTDashboardKpiWrapper(String date, Double avgKpiA, Double avgKpiB, String operator, String ipVersion,
			Integer geographyL1, Integer geographyL2, Integer geographyL3, Integer geographyL4){
		this.date = date;
		this.avgKpiB = avgKpiA;
		this.avgKpiA = avgKpiB;
		this.operator = operator;
		this.ipVersion = ipVersion;
		this.geographyL1 = geographyL1;
		this.geographyL2 = geographyL2;
		this.geographyL3 = geographyL3;
		this.geographyL4 = geographyL4;
	}
	
	/**
	 * Instantiates a new WPT dashboard kpi wrapper.
	 *
	 * @param date the date
	 * @param minTTFB the min TTFB
	 * @param maxTTFB the max TTFB
	 * @param category the category
	 */
	public WPTDashboardKpiWrapper(String date, Double minTTFB, Double maxTTFB, String category){
		this.date = date;
		this.minTTFB = minTTFB;
		this.maxTTFB = maxTTFB;
		this.category = category;
	}
	
	/**
	 * Instantiates a new WPT dashboard kpi wrapper.
	 *
	 * @param date the date
	 * @param avgKpiA the avg kpi A
	 */
	public WPTDashboardKpiWrapper(String date, Double avgKpiA){
		this.date = date;
		this.avgKpiA = avgKpiA;
	}
	

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(String date) {
		this.date = date;
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
	 * Gets the ip version.
	 *
	 * @return the ip version
	 */
	public String getIpVersion() {
		return ipVersion;
	}

	/**
	 * Sets the ip version.
	 *
	 * @param ipVersion the new ip version
	 */
	public void setIpVersion(String ipVersion) {
		this.ipVersion = ipVersion;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Gets the geography L 1.
	 *
	 * @return the geography L 1
	 */
	public Integer getGeographyL1() {
		return geographyL1;
	}

	/**
	 * Sets the geography L 1.
	 *
	 * @param geographyL1 the new geography L 1
	 */
	public void setGeographyL1(Integer geographyL1) {
		this.geographyL1 = geographyL1;
	}

	/**
	 * Gets the geography L 2.
	 *
	 * @return the geography L 2
	 */
	public Integer getGeographyL2() {
		return geographyL2;
	}

	/**
	 * Sets the geography L 2.
	 *
	 * @param geographyL2 the new geography L 2
	 */
	public void setGeographyL2(Integer geographyL2) {
		this.geographyL2 = geographyL2;
	}

	/**
	 * Gets the geography L 3.
	 *
	 * @return the geography L 3
	 */
	public Integer getGeographyL3() {
		return geographyL3;
	}

	/**
	 * Sets the geography L 3.
	 *
	 * @param geographyL3 the new geography L 3
	 */
	public void setGeographyL3(Integer geographyL3) {
		this.geographyL3 = geographyL3;
	}

	/**
	 * Gets the geography L 4.
	 *
	 * @return the geography L 4
	 */
	public Integer getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4 the new geography L 4
	 */
	public void setGeographyL4(Integer geographyL4) {
		this.geographyL4 = geographyL4;
	}

	/**
	 * Gets the avg kpi A.
	 *
	 * @return the avg kpi A
	 */
	public Double getAvgKpiA() {
		return avgKpiA;
	}

	/**
	 * Sets the avg kpi A.
	 *
	 * @param avgKpiA the new avg kpi A
	 */
	public void setAvgKpiA(Double avgKpiA) {
		this.avgKpiA = avgKpiA;
	}

	/**
	 * Gets the avg kpi B.
	 *
	 * @return the avg kpi B
	 */
	public Double getAvgKpiB() {
		return avgKpiB;
	}

	/**
	 * Sets the avg kpi B.
	 *
	 * @param avgKpiB the new avg kpi B
	 */
	public void setAvgKpiB(Double avgKpiB) {
		this.avgKpiB = avgKpiB;
	}

	/**
	 * Gets the min TTFB.
	 *
	 * @return the min TTFB
	 */
	public Double getMinTTFB() {
		return minTTFB;
	}

	/**
	 * Sets the min TTFB.
	 *
	 * @param minTTFB the new min TTFB
	 */
	public void setMinTTFB(Double minTTFB) {
		this.minTTFB = minTTFB;
	}

	/**
	 * Gets the max TTFB.
	 *
	 * @return the max TTFB
	 */
	public Double getMaxTTFB() {
		return maxTTFB;
	}

	/**
	 * Sets the max TTFB.
	 *
	 * @param maxTTFB the new max TTFB
	 */
	public void setMaxTTFB(Double maxTTFB) {
		this.maxTTFB = maxTTFB;
	}
	
	
}
