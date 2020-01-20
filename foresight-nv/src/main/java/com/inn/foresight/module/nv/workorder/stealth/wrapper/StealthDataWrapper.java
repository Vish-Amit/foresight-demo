package com.inn.foresight.module.nv.workorder.stealth.wrapper;

public class StealthDataWrapper {
private Double min ;
private Double max;
private Double avg;
private String score;
private String nename;
private String gl4;
private String date;
public Double getMin() {
	return min;
}
public void setMin(Double min) {
	this.min = min;
}
public Double getMax() {
	return max;
}
public void setMax(Double max) {
	this.max = max;
}
public Double getAvg() {
	return avg;
}
public void setAvg(Double avg) {
	this.avg = avg;
}
public String getScore() {
	return score;
}
public void setScore(String score) {
	this.score = score;
}
public String getNename() {
	return nename;
}
public void setNename(String nename) {
	this.nename = nename;
}
public String getGl4() {
	return gl4;
}
public void setGl4(String gl4) {
	this.gl4 = gl4;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
@Override
public String toString() {
	return "StealthDataWrapper [min=" + min + ", max=" + max + ", avg=" + avg + ", score=" + score + ", nename="
			+ nename + ", gl4=" + gl4 + ", date=" + date + "]";
}



}
