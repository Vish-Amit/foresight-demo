package com.inn.foresight.module.nv.report.wrapper.stealth;

public class StealthWOGraphItemWrapper {

	String range;
	Integer count;

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "StealthWOGraphItemWrapper [range=" + range + ", count=" + count + "]";
	}

}
