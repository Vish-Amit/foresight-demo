package com.inn.foresight.core.um.utils.wrapper;

import java.io.Serializable;
import java.util.Date;

public class ForesightTeamWrapper implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private String creator;
	private String lastModifier;
	private String displayName;
	private String applicationName;
	private String departmentName;
	
	
	
	public ForesightTeamWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ForesightTeamWrapper(Integer id, String name, Date creationTime, Date modificationTime, String creator,
			String lastModifier, String displayName,String applicationName,String departmentName) {
		super();
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.lastModifier = lastModifier;
		this.displayName = displayName;
		this.applicationName = applicationName;
		this.departmentName = departmentName;
	}


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
	
	public String getCreator() {
		return creator;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}


	public String getLastModifier() {
		return lastModifier;
	}


	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getApplicationName() {
		return applicationName;
	}


	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}


	public String getDepartmentName() {
		return departmentName;
	}


	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}


	@Override
	public String toString() {
		return "ForesightTeamWrapper [id=" + id + ", name=" + name + ", creator=" + creator + ", lastModifier="
				+ lastModifier + ", displayName=" + displayName + ", applicationName=" + applicationName
				+ ", departmentName=" + departmentName + "]";
	}
	
	
	
	
}