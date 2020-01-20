package com.inn.foresight.module.nv.report.parse.wrapper;

public class Geography {
	
	String name;
	Integer id;
	String geographyType;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getGeographyType() {
		return geographyType;
	}
	public void setGeographyType(String geographyType) {
		this.geographyType = geographyType;
	}
	@Override
	public String toString() {
		return "Geography [name=" + name + ", id=" + id + ", geographyType=" + geographyType + "]";
	}
	
	
}
