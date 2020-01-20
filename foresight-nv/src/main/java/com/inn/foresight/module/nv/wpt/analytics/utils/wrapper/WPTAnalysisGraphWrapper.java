package com.inn.foresight.module.nv.wpt.analytics.utils.wrapper;

import java.util.HashMap;
import java.util.Map;

import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class WPTAnalysisGraphWrapper.
 *
 * @author innoeye
 * date - 14-Dec-2017 1:25:09 PM
 */
@RestWrapper
public class WPTAnalysisGraphWrapper {

	/** The fdns. */
	private Map<String, Map<String,Object>> fdns = new HashMap<>();
	
	/** The tdns. */
	private Map<String, Map<String,Object>> tdns = new HashMap<>();
	
	/** The ttfb. */
	private Map<String, Map<String,Object>> ttfb = new HashMap<>();
	
	/** The ttl. */
	private Map<String, Map<String,Object>> ttl = new HashMap<>();
	
	/** The ping. */
	private Map<String, Map<String,Object>> ping  = new HashMap<>();
	
	/** The trace route. */
	private Map<String, Map<String,Object>> traceRoute = new HashMap<>();
	
	/**
	 * Gets the fdns.
	 *
	 * @return the fdns
	 */
	public Map<String, Map<String,Object>> getFdns() {
		return fdns;
	}
	
	/**
	 * Sets the fdns.
	 *
	 * @param fdns the fdns
	 */
	public void setFdns(Map<String, Map<String,Object>> fdns) {
		this.fdns = fdns;
	}
	
	/**
	 * Gets the tdns.
	 *
	 * @return the tdns
	 */
	public Map<String, Map<String,Object>> getTdns() {
		return tdns;
	}
	
	/**
	 * Sets the tdns.
	 *
	 * @param tdns the tdns
	 */
	public void setTdns(Map<String, Map<String,Object>> tdns) {
		this.tdns = tdns;
	}
	
	/**
	 * Gets the ttfb.
	 *
	 * @return the ttfb
	 */
	public Map<String, Map<String,Object>> getTtfb() {
		return ttfb;
	}
	
	/**
	 * Sets the ttfb.
	 *
	 * @param ttfb the ttfb
	 */
	public void setTtfb(Map<String, Map<String,Object>> ttfb) {
		this.ttfb = ttfb;
	}
	
	/**
	 * Gets the ttl.
	 *
	 * @return the ttl
	 */
	public Map<String, Map<String,Object>> getTtl() {
		return ttl;
	}
	
	/**
	 * Sets the ttl.
	 *
	 * @param ttl the ttl
	 */
	public void setTtl(Map<String, Map<String,Object>> ttl) {
		this.ttl = ttl;
	}
	
	/**
	 * Gets the ping.
	 *
	 * @return the ping
	 */
	public Map<String, Map<String,Object>> getPing() {
		return ping;
	}
	
	/**
	 * Sets the ping.
	 *
	 * @param ping the ping
	 */
	public void setPing(Map<String, Map<String,Object>> ping) {
		this.ping = ping;
	}
	
	/**
	 * Gets the trace route.
	 *
	 * @return the trace route
	 */
	public Map<String, Map<String,Object>> getTraceRoute() {
		return traceRoute;
	}
	
	/**
	 * Sets the trace route.
	 *
	 * @param traceRoute the trace route
	 */
	public void setTraceRoute(Map<String, Map<String,Object>> traceRoute) {
		this.traceRoute = traceRoute;
	}
}
