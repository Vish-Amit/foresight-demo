package com.inn.foresight.module.nv.report.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class GeographyWrapper {
	private String geographyLevelName;
	private String geographyName;
	
	
	
	public String getGeographyLevelName() {
		return geographyLevelName;
	}



	public void setGeographyLevelName(String geographyLevelName) {
		this.geographyLevelName = geographyLevelName;
	}



	public String getGeographyName() {
		return geographyName;
	}



	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
	}



	@Override
	public String toString() {
		return "GeographyWrapper [geographyLevelName=" + geographyLevelName + ", geographyName=" + geographyName + "]";
	}
	
	
}
