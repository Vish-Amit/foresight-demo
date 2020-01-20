package com.inn.foresight.module.nv.workorder.wrapper;

public class EnodeBWrapper {

	String name;
	Double avgDl;
	Double avgRsrp;
	Double avgSinr;
	Double avgUl;
	Double avgRsrq;
	String cgi;
	String score;

	public EnodeBWrapper() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getAvgDl() {
		return avgDl;
	}

	public void setAvgDl(Double avgDl) {
		this.avgDl = avgDl;
	}

	public Double getAvgRsrp() {
		return avgRsrp;
	}

	public void setAvgRsrp(Double avgRsrp) {
		this.avgRsrp = avgRsrp;
	}

	public Double getAvgSinr() {
		return avgSinr;
	}

	public void setAvgSinr(Double avgSinr) {
		this.avgSinr = avgSinr;
	}

	public Double getAvgUl() {
		return avgUl;
	}

	public void setAvgUl(Double avgUl) {
		this.avgUl = avgUl;
	}

	public Double getAvgRsrq() {
		return avgRsrq;
	}

	public void setAvgRsrq(Double avgRsrq) {
		this.avgRsrq = avgRsrq;
	}

	public String getCgi() {
		return cgi;
	}

	public void setCgi(String cgi) {
		this.cgi = cgi;
	}

	/**
	 * @return the score
	 */
	public String getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "EnodeBWrapper [name=" + name + ", avgDl=" + avgDl + ", avgRsrp=" + avgRsrp + ", avgSinr=" + avgSinr
				+ ", avgUl=" + avgUl + ", avgRsrq=" + avgRsrq + ", cgi=" + cgi + "]";
	}

}
