package com.inn.foresight.module.nv.wpt.analytics.utils.wrapper;

import com.inn.commons.lang.NumberUtils;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class WPTDetailedViewWrapper.
 *
 * @author innoeye
 * date - 30-Oct-2017 8:30:39 PM
 */
@JpaWrapper
@RestWrapper
public class WPTDetailedViewWrapper {
	
	/** The site. */
	String site; 
	
	/** The category id. */
	Integer categoryId;
	
	/** The total count. */
	Long totalCount;
	
	/** The first dns total count. */
	Long firstDnsTotalCount;
	
	/** The first byte total count. */
	Long firstByteTotalCount;
	
	/** The dns total count. */
	Long dnsTotalCount;
	
	/** The ping total count. */
	Long pingTotalCount;
	
	/** The first DNS resolution time. */
	Double firstDNSResolutionTime;
	
	/** The total DNS resolution time. */
	Double totalDNSResolutionTime;
	
	/** The first byte response time. */
	Double firstByteResponseTime;
	
	/** The total response time. */
	Double totalResponseTime;
	
	/** The pingtime. */
	Double pingtime;
	
	/**
	 * Instantiates a new WPT detailed view wrapper.
	 *
	 * @param categoryId the category id
	 * @param totalCount the total count
	 * @param firstDNSResolutionTime the first DNS resolution time
	 * @param firstByteResponseTime the first byte response time
	 * @param totalResponseTime the total response time
	 * @param totalDNSResolutionTime the total DNS resolution time
	 * @param pingtime the pingtime
	 */
	public WPTDetailedViewWrapper(Integer categoryId, Long totalCount, Double firstDNSResolutionTime,
			Double firstByteResponseTime, Double totalResponseTime, Double totalDNSResolutionTime, Double pingtime) {
		super();
		this.categoryId = categoryId;
		this.totalCount = totalCount == null ? NumberUtils.LONG_ZERO : totalCount;
		this.firstDnsTotalCount =  NumberUtils.LONG_ZERO;
		this.firstByteTotalCount = NumberUtils.LONG_ZERO;
		this.dnsTotalCount = NumberUtils.LONG_ZERO;
		this.pingTotalCount = NumberUtils.LONG_ZERO;
		this.firstDNSResolutionTime = firstDNSResolutionTime;
		this.totalDNSResolutionTime = totalDNSResolutionTime;
		this.firstByteResponseTime = firstByteResponseTime;
		this.totalResponseTime = totalResponseTime;
		this.pingtime = pingtime;
	}

	/**
	 * Instantiates a new WPT detailed view wrapper.
	 *
	 * @param site the site
	 * @param totalCount the total count
	 * @param firstDNSResolutionTime the first DNS resolution time
	 * @param firstByteResponseTime the first byte response time
	 * @param totalResponseTime the total response time
	 * @param totalDNSResolutionTime the total DNS resolution time
	 * @param pingtime the pingtime
	 */
	public WPTDetailedViewWrapper(String site, Long totalCount, Double firstDNSResolutionTime, Double firstByteResponseTime,
			Double totalResponseTime, Double totalDNSResolutionTime, Double pingtime) {
		super();
		this.site = site;
		this.totalCount = totalCount == null ? NumberUtils.LONG_ZERO : totalCount;
		this.firstDnsTotalCount =  NumberUtils.LONG_ZERO;
		this.firstByteTotalCount = NumberUtils.LONG_ZERO;
		this.dnsTotalCount = NumberUtils.LONG_ZERO;
		this.pingTotalCount = NumberUtils.LONG_ZERO;
		this.firstDNSResolutionTime = firstDNSResolutionTime;
		this.totalDNSResolutionTime = totalDNSResolutionTime;
		this.firstByteResponseTime = firstByteResponseTime;
		this.totalResponseTime = totalResponseTime;
		this.pingtime = pingtime;
	}

	/**
	 * Gets the site.
	 *
	 * @return the site
	 */
	public String getSite() {
		return site;
	}
	
	/**
	 * Sets the site.
	 *
	 * @param site the new site
	 */
	public void setSite(String site) {
		this.site = site;
	}
	
	/**
	 * Gets the total count.
	 *
	 * @return the total count
	 */
	public Long getTotalCount() {
		return totalCount;
	}
	
	/**
	 * Sets the total count.
	 *
	 * @param totalCount the new total count
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	
	/**
	 * Gets the first DNS resolution time.
	 *
	 * @return the first DNS resolution time
	 */
	public Double getFirstDNSResolutionTime() {
		return firstDNSResolutionTime;
	}
	
	/**
	 * Sets the first DNS resolution time.
	 *
	 * @param firstDNSResolutionTime the new first DNS resolution time
	 */
	public void setFirstDNSResolutionTime(Double firstDNSResolutionTime) {
		this.firstDNSResolutionTime = firstDNSResolutionTime;
	}
	
	/**
	 * Gets the total DNS resolution time.
	 *
	 * @return the total DNS resolution time
	 */
	public Double getTotalDNSResolutionTime() {
		return totalDNSResolutionTime;
	}
	
	/**
	 * Sets the total DNS resolution time.
	 *
	 * @param totalDNSResolutionTime the new total DNS resolution time
	 */
	public void setTotalDNSResolutionTime(Double totalDNSResolutionTime) {
		this.totalDNSResolutionTime = totalDNSResolutionTime;
	}
	
	/**
	 * Gets the first byte response time.
	 *
	 * @return the first byte response time
	 */
	public Double getFirstByteResponseTime() {
		return firstByteResponseTime;
	}
	
	/**
	 * Sets the first byte response time.
	 *
	 * @param firstByteResponseTime the new first byte response time
	 */
	public void setFirstByteResponseTime(Double firstByteResponseTime) {
		this.firstByteResponseTime = firstByteResponseTime;
	}
	
	/**
	 * Gets the total response time.
	 *
	 * @return the total response time
	 */
	public Double getTotalResponseTime() {
		return totalResponseTime;
	}
	
	/**
	 * Sets the total response time.
	 *
	 * @param totalResponseTime the new total response time
	 */
	public void setTotalResponseTime(Double totalResponseTime) {
		this.totalResponseTime = totalResponseTime;
	}
	
	/**
	 * Gets the pingtime.
	 *
	 * @return the pingtime
	 */
	public Double getPingtime() {
		return pingtime;
	}
	
	/**
	 * Sets the pingtime.
	 *
	 * @param pingtime the new pingtime
	 */
	public void setPingtime(Double pingtime) {
		this.pingtime = pingtime;
	}

	/**
	 * Gets the category id.
	 *
	 * @return the category id
	 */
	public Integer getCategoryId() {
		return categoryId;
	}

	/**
	 * Sets the category id.
	 *
	 * @param categoryId the new category id
	 */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Gets the first dns total count.
	 *
	 * @return the first dns total count
	 */
	public Long getFirstDnsTotalCount() {
		return firstDnsTotalCount;
	}

	/**
	 * Sets the first dns total count.
	 *
	 * @param firstDnsTotalCount the new first dns total count
	 */
	public void setFirstDnsTotalCount(Long firstDnsTotalCount) {
		this.firstDnsTotalCount = firstDnsTotalCount;
	}

	/**
	 * Gets the first byte total count.
	 *
	 * @return the first byte total count
	 */
	public Long getFirstByteTotalCount() {
		return firstByteTotalCount;
	}

	/**
	 * Sets the first byte total count.
	 *
	 * @param firstByteTotalCount the new first byte total count
	 */
	public void setFirstByteTotalCount(Long firstByteTotalCount) {
		this.firstByteTotalCount = firstByteTotalCount;
	}

	/**
	 * Gets the dns total count.
	 *
	 * @return the dns total count
	 */
	public Long getDnsTotalCount() {
		return dnsTotalCount;
	}

	/**
	 * Sets the dns total count.
	 *
	 * @param dnsTotalCount the new dns total count
	 */
	public void setDnsTotalCount(Long dnsTotalCount) {
		this.dnsTotalCount = dnsTotalCount;
	}

	/**
	 * Gets the ping total count.
	 *
	 * @return the ping total count
	 */
	public Long getPingTotalCount() {
		return pingTotalCount;
	}

	/**
	 * Sets the ping total count.
	 *
	 * @param pingTotalCount the new ping total count
	 */
	public void setPingTotalCount(Long pingTotalCount) {
		this.pingTotalCount = pingTotalCount;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "WPTDetailedViewWrapper [site=" + site + ", categoryId=" + categoryId + ", totalCount=" + totalCount
				+ ", firstDnsTotalCount=" + firstDnsTotalCount + ", firstByteTotalCount=" + firstByteTotalCount
				+ ", dnsTotalCount=" + dnsTotalCount + ", pingTotalCount=" + pingTotalCount
				+ ", firstDNSResolutionTime=" + firstDNSResolutionTime + ", totalDNSResolutionTime="
				+ totalDNSResolutionTime + ", firstByteResponseTime=" + firstByteResponseTime + ", totalResponseTime="
				+ totalResponseTime + ", pingtime=" + pingtime + "]";
	}
	
}
