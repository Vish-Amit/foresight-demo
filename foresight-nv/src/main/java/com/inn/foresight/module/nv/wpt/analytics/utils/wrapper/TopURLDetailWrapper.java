package com.inn.foresight.module.nv.wpt.analytics.utils.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

/**
 * The Class TopURLDetailWrapper.
 *
 * @author innoeye
 * date - 12-Dec-2017 6:11:57 PM
 */
@JpaWrapper
public class TopURLDetailWrapper {
	
	/** The date. */
	private String date;
	
	/** The url. */
	private String url;
	
	/** The count. */
	private Long count;
	
	/** The hop count. */
	private Long hopCount;
	
	/** The avg TTL. */
	private Double avgTTL;
	
	/** The avg TTFB. */
	private Double avgTTFB;
	
	/** The avg FDNS. */
	private Double avgFDNS;
	
	/** The avg TDNS. */
	private Double avgTDNS;
	
	/** The avg ping. */
	private Double avgPing;
	
	/** The avg page size. */
	private Double avgPageSize;
	
	/** The geography L 1. */
	private Integer geographyL1;
	
	/** The geography L 2. */
	private Integer geographyL2;
	
	/** The geography L 3. */
	private Integer geographyL3;
	
	/** The geography L 4. */
	private Integer geographyL4;
	
	/** The operator. */
	private String operator;

	/** The technology. */
	private String technology;

	/** The ip version. */
	private String ipVersion;
	
	/**
	 * Instantiates a new top URL detail wrapper.
	 *
	 * @param count the count
	 * @param url the url
	 */
	public TopURLDetailWrapper(Long count, String url) {
		this.url = url != null ? url.replace("%", "") : url;
		this.count = count;
	}


	/**
	 * Instantiates a new top URL detail wrapper.
	 *
	 * @param count the count
	 * @param url the url
	 * @param hopCount the hop count
	 * @param avgTTL the avg TTL
	 */
	public TopURLDetailWrapper(Long count, String url, Long hopCount, Double avgTTL) {
		this.url = url != null ? url.replace("%", "") : url;
		this.count = count;
		this.hopCount = hopCount;
		this.avgTTL = avgTTL;
	}


	/**
	 * Instantiates a new top URL detail wrapper.
	 *
	 * @param url the url
	 * @param count the count
	 * @param avgTTL the avg TTL
	 * @param avgTTFB the avg TTFB
	 * @param avgFDNS the avg FDNS
	 * @param avgTDNS the avg TDNS
	 * @param avgPing the avg ping
	 * @param avgPageSize the avg page size
	 */
	public TopURLDetailWrapper(String url, Long count, Double avgTTL, Double avgTTFB, Double avgFDNS, Double avgTDNS,
			Double avgPing, Double avgPageSize) {
		super();
		this.url = url != null ? url.replace("%", "") : url;
		this.count = count;
		this.avgTTL = avgTTL;
		this.avgTTFB = avgTTFB;
		this.avgFDNS = avgFDNS;
		this.avgTDNS = avgTDNS;
		this.avgPing = avgPing;
		this.avgPageSize = avgPageSize;
	}

	/**
	 * Instantiates a new top URL detail wrapper.
	 *
	 * @param hopCount the hop count
	 * @param avgTTL the avg TTL
	 * @param avgTTFB the avg TTFB
	 * @param avgFDNS the avg FDNS
	 * @param avgTDNS the avg TDNS
	 * @param avgPing the avg ping
	 */
	public TopURLDetailWrapper(Long hopCount, Double avgTTL, Double avgTTFB, Double avgFDNS, Double avgTDNS,
			Double avgPing) {
		super();
		this.hopCount = hopCount;
		this.avgTTL = avgTTL;
		this.avgTTFB = avgTTFB;
		this.avgFDNS = avgFDNS;
		this.avgTDNS = avgTDNS;
		this.avgPing = avgPing;
	}
	
	
	/**
	 * Instantiates a new top URL detail wrapper.
	 *
	 * @param date the date
	 * @param operator the operator
	 * @param ipVersion the ip version
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param hopCount the hop count
	 * @param avgTTL the avg TTL
	 * @param avgTTFB the avg TTFB
	 * @param avgFDNS the avg FDNS
	 * @param avgTDNS the avg TDNS
	 * @param avgPing the avg ping
	 */
	public TopURLDetailWrapper(String date, String operator, String ipVersion, Integer geographyL1,
			Integer geographyL2, Integer geographyL3, Integer geographyL4, Long hopCount, Double avgTTL,
			Double avgTTFB, Double avgFDNS, Double avgTDNS, Double avgPing) {
		super();
		this.date = date;
		this.hopCount = hopCount;
		this.avgTTL = avgTTL;
		this.avgTTFB = avgTTFB;
		this.avgFDNS = avgFDNS;
		this.avgTDNS = avgTDNS;
		this.avgPing = avgPing;
		this.geographyL1 = geographyL1;
		this.geographyL2 = geographyL2;
		this.geographyL3 = geographyL3;
		this.geographyL4 = geographyL4;
		this.operator = operator;
		this.technology = technology;
		this.ipVersion = ipVersion;
	}
	
	/**
	 * Instantiates a new top URL detail wrapper.
	 *
	 * @param date the date
	 * @param technology the technology
	 * @param operator the operator
	 * @param geographyL3 the geography L 3
	 * @param hopCount the hop count
	 * @param avgTTL the avg TTL
	 * @param avgTTFB the avg TTFB
	 * @param avgFDNS the avg FDNS
	 * @param avgTDNS the avg TDNS
	 * @param avgPing the avg ping
	 */
	public TopURLDetailWrapper(String date, String technology, String operator, 
			Integer geographyL3, Long hopCount, Double avgTTL,
			Double avgTTFB, Double avgFDNS, Double avgTDNS, Double avgPing) {
		super();
		this.date = date;
		this.hopCount = hopCount;
		this.avgTTL = avgTTL;
		this.avgTTFB = avgTTFB;
		this.avgFDNS = avgFDNS;
		this.avgTDNS = avgTDNS;
		this.avgPing = avgPing;
		this.geographyL3 = geographyL3;
		this.operator = operator;
		this.technology = technology;
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
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	
	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	
	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	
	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(Long count) {
		this.count = count;
	}

	
	/**
	 * Gets the hop count.
	 *
	 * @return the hop count
	 */
	public Long getHopCount() {
		return hopCount;
	}

	
	/**
	 * Sets the hop count.
	 *
	 * @param hopCount the new hop count
	 */
	public void setHopCount(Long hopCount) {
		this.hopCount = hopCount;
	}

	
	/**
	 * Gets the avg TTL.
	 *
	 * @return the avg TTL
	 */
	public Double getAvgTTL() {
		return avgTTL;
	}

	
	/**
	 * Sets the avg TTL.
	 *
	 * @param avgTTL the new avg TTL
	 */
	public void setAvgTTL(Double avgTTL) {
		this.avgTTL = avgTTL;
	}


	
	/**
	 * Gets the avg TTFB.
	 *
	 * @return the avg TTFB
	 */
	public Double getAvgTTFB() {
		return avgTTFB;
	}


	
	/**
	 * Sets the avg TTFB.
	 *
	 * @param avgTTFB the new avg TTFB
	 */
	public void setAvgTTFB(Double avgTTFB) {
		this.avgTTFB = avgTTFB;
	}


	
	/**
	 * Gets the avg FDNS.
	 *
	 * @return the avg FDNS
	 */
	public Double getAvgFDNS() {
		return avgFDNS;
	}


	
	/**
	 * Sets the avg FDNS.
	 *
	 * @param avgFDNS the new avg FDNS
	 */
	public void setAvgFDNS(Double avgFDNS) {
		this.avgFDNS = avgFDNS;
	}


	
	/**
	 * Gets the avg TDNS.
	 *
	 * @return the avg TDNS
	 */
	public Double getAvgTDNS() {
		return avgTDNS;
	}


	
	/**
	 * Sets the avg TDNS.
	 *
	 * @param avgTDNS the new avg TDNS
	 */
	public void setAvgTDNS(Double avgTDNS) {
		this.avgTDNS = avgTDNS;
	}


	
	/**
	 * Gets the avg ping.
	 *
	 * @return the avg ping
	 */
	public Double getAvgPing() {
		return avgPing;
	}


	
	/**
	 * Sets the avg ping.
	 *
	 * @param avgPing the new avg ping
	 */
	public void setAvgPing(Double avgPing) {
		this.avgPing = avgPing;
	}


	
	/**
	 * Gets the avg page size.
	 *
	 * @return the avg page size
	 */
	public Double getAvgPageSize() {
		return avgPageSize;
	}


	
	/**
	 * Sets the avg page size.
	 *
	 * @param avgPageSize the new avg page size
	 */
	public void setAvgPageSize(Double avgPageSize) {
		this.avgPageSize = avgPageSize;
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
	
	
}
