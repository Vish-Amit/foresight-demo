package com.inn.foresight.module.nv.report.wrapper.stealth;

public class StealthWOWPTItemWrapper {

	String date;
	String ttl;
	String ttfb;
	String dns;
	String totalDns;
	
	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	public String getTtfb() {
		return ttfb;
	}

	public void setTtfb(String ttfb) {
		this.ttfb = ttfb;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getTotalDns() {
		return totalDns;
	}

	public void setTotalDns(String totalDns) {
		this.totalDns = totalDns;
	}
	
	String json;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	@Override
	public String toString() {
		return "StealthWOWPTItemWrapper [date=" + date + ", ttl=" + ttl + ", ttfb=" + ttfb + ", dns=" + dns
				+ ", totalDns=" + totalDns + ", json=" + json + "]";
	}

	
}
