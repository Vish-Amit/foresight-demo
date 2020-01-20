package com.inn.foresight.module.nv.report.wrapper;

public class RangeScoreWrapper {
	
	private String range;
	private String score;
	
	public RangeScoreWrapper (String range, String score) {
		this.range=range;
		this.score = score;
	}
	
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "RangeScoreWrapper [range=" + range + ", score=" + score + "]";
	}
	
	

}
