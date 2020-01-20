package com.inn.foresight.module.nv.report.wrapper.stealth;

public class StealthWOKpiItemWrapper {

	String date;
	String rsrp;
	String rsrq;
	String sinr;
	String dl;
	String ul;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRsrp() {
		return rsrp;
	}

	public void setRsrp(String rsrp) {
		this.rsrp = rsrp;
	}

	public String getRsrq() {
		return rsrq;
	}

	public void setRsrq(String rsrq) {
		this.rsrq = rsrq;
	}

	public String getSinr() {
		return sinr;
	}

	public void setSinr(String sinr) {
		this.sinr = sinr;
	}

	public String getDl() {
		return dl;
	}

	public void setDl(String dl) {
		this.dl = dl;
	}

	public String getUl() {
		return ul;
	}

	public void setUl(String ul) {
		this.ul = ul;
	}

	@Override
	public String toString() {
		return "StealthWOKpiItemWrapper [date=" + date + ", rsrp=" + rsrp + ", rsrq=" + rsrq + ", sinr=" + sinr
				+ ", dl=" + dl + ", ul=" + ul + "]";
	}

}
