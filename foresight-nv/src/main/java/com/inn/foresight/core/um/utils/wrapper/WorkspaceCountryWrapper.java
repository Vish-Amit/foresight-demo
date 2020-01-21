package com.inn.foresight.core.um.utils.wrapper;

import java.io.Serializable;

public class WorkspaceCountryWrapper implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String country;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	@Override
	public String toString() {
		return "WorkspaceCountryWrapper [id=" + id + ", name=" + name
				+ ", country=" + country + "]";
	}
	
	public WorkspaceCountryWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WorkspaceCountryWrapper(Integer id, String name, String country) {
		super();
		this.id = id;
		this.name = name;
		this.country = country;
	}

}
