package com.inn.foresight.module.nv.report.wrapper.stealth;

public class StealthWOPingItemWrapper {

	String date;
	String pingLatency;
	String pingJitter;
	String pingPacketLoss;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPingLatency() {
		return pingLatency;
	}

	public void setPingLatency(String pingLatency) {
		this.pingLatency = pingLatency;
	}

	public String getPingJitter() {
		return pingJitter;
	}

	public void setPingJitter(String pingJitter) {
		this.pingJitter = pingJitter;
	}

	public String getPingPacketLoss() {
		return pingPacketLoss;
	}

	public void setPingPacketLoss(String pingPacketLoss) {
		this.pingPacketLoss = pingPacketLoss;
	}

	@Override
	public String toString() {
		return "StealthWOPingItemWrapper [date=" + date + ", pingLatency=" + pingLatency + ", pingJitter=" + pingJitter
				+ ", pingPacketLoss=" + pingPacketLoss + "]";
	}

}
