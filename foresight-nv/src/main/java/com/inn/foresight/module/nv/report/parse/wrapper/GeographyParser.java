package com.inn.foresight.module.nv.report.parse.wrapper;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeographyParser {


	List<Geography> GeographyL1;
	List<Geography> GeographyL2;
	List<Geography> GeographyL3;
	List<Geography> GeographyL4;
	
	public List<Geography> getGeographyL1() {
		return GeographyL1;
	}
	public void setGeographyL1(List<Geography> geographyL1) {
		GeographyL1 = geographyL1;
	}
	public List<Geography> getGeographyL2() {
		return GeographyL2;
	}
	public void setGeographyL2(List<Geography> geographyL2) {
		GeographyL2 = geographyL2;
	}
	public List<Geography> getGeographyL3() {
		return GeographyL3;
	}
	public void setGeographyL3(List<Geography> geographyL3) {
		GeographyL3 = geographyL3;
	}
	public List<Geography> getGeographyL4() {
		return GeographyL4;
	}
	public void setGeographyL4(List<Geography> geographyL4) {
		GeographyL4 = geographyL4;
	}
	
	@Override
	public String toString() {
		return "GeographyParser [GeographyL1=" + GeographyL1 + ", GeographyL2=" + GeographyL2 + ", GeographyL3="
				+ GeographyL3 + ", GeographyL4=" + GeographyL4 + "]";
	}

}
