package com.inn.foresight.module.nv.wpt.analytics.utils.wrapper;

import java.sql.Date;

import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class WPTSummaryWrapper.
 *
 * @author innoeye
 * date - 30-Oct-2017 4:30:01 PM
 */
@RestWrapper
public class WPTSummaryWrapper {
	
	/** The site. */
	String site;
	
	/** The capture time. */
	Date captureTime; 
	
	/** The ipv 4 load time. */
	Double ipv4LoadTime;
	
	/** The ipv 4 success rate. */
	Double ipv4SuccessRate;
	
	/** The ipv 6 load time. */
	Double ipv6LoadTime;
	
	/** The ipv 6 success rate. */
	Double ipv6SuccessRate;
	
	/** The ipv. */
	String ipv;	

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
	 * Gets the capture time.
	 *
	 * @return the capture time
	 */
	public Date getCaptureTime() {
		return captureTime;
	}
	
	/**
	 * Sets the capture time.
	 *
	 * @param captureTime the new capture time
	 */
	public void setCaptureTime(Date captureTime) {
		this.captureTime = captureTime;
	}
	
	/**
	 * Gets the ipv 4 load time.
	 *
	 * @return the ipv 4 load time
	 */
	public Double getIpv4LoadTime() {
		return ipv4LoadTime;
	}
	
	/**
	 * Sets the ipv 4 load time.
	 *
	 * @param ipv4LoadTime the new ipv 4 load time
	 */
	public void setIpv4LoadTime(Double ipv4LoadTime) {
		this.ipv4LoadTime = ipv4LoadTime;
	}
	
	/**
	 * Gets the ipv 4 success rate.
	 *
	 * @return the ipv 4 success rate
	 */
	public Double getIpv4SuccessRate() {
		return ipv4SuccessRate;
	}
	
	/**
	 * Sets the ipv 4 success rate.
	 *
	 * @param ipv4SuccessRate the new ipv 4 success rate
	 */
	public void setIpv4SuccessRate(Double ipv4SuccessRate) {
		this.ipv4SuccessRate = ipv4SuccessRate;
	}
	
	/**
	 * Gets the ipv 6 load time.
	 *
	 * @return the ipv 6 load time
	 */
	public Double getIpv6LoadTime() {
		return ipv6LoadTime;
	}
	
	/**
	 * Sets the ipv 6 load time.
	 *
	 * @param ipv6LoadTime the new ipv 6 load time
	 */
	public void setIpv6LoadTime(Double ipv6LoadTime) {
		this.ipv6LoadTime = ipv6LoadTime;
	}
	
	/**
	 * Gets the ipv 6 success rate.
	 *
	 * @return the ipv 6 success rate
	 */
	public Double getIpv6SuccessRate() {
		return ipv6SuccessRate;
	}
	
	/**
	 * Sets the ipv 6 success rate.
	 *
	 * @param ipv6SuccessRate the new ipv 6 success rate
	 */
	public void setIpv6SuccessRate(Double ipv6SuccessRate) {
		this.ipv6SuccessRate = ipv6SuccessRate;
	}

	
	/**
	 * Gets the ipv.
	 *
	 * @return the ipv
	 */
	public String getIpv() {
		return ipv;
	}


	
	/**
	 * Sets the ipv.
	 *
	 * @param ipv the new ipv
	 */
	public void setIpv(String ipv) {
		this.ipv = ipv;
	}
	
}
