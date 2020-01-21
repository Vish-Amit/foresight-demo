package com.inn.foresight.core.um.utils.wrapper;

import java.io.Serializable;
import java.util.List;

public class SiteforgeUserWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer roleId;
	private List<String> geographyL1Name;
	private List<String> geographyL2Name;
	private List<String> geographyL3Name;
	private List<String> geographyL4Name;
	
	
	
	
	
	
	public List<String> getGeographyL1Name() {
		return geographyL1Name;
	}
	public void setGeographyL1Name(List<String> geographyL1Name) {
		this.geographyL1Name = geographyL1Name;
	}
	public List<String> getGeographyL2Name() {
		return geographyL2Name;
	}
	public void setGeographyL2Name(List<String> geographyL2Name) {
		this.geographyL2Name = geographyL2Name;
	}
	public List<String> getGeographyL3Name() {
		return geographyL3Name;
	}
	public void setGeographyL3Name(List<String> geographyL3Name) {
		this.geographyL3Name = geographyL3Name;
	}
	public List<String> getGeographyL4Name() {
		return geographyL4Name;
	}
	public void setGeographyL4Name(List<String> geographyL4Name) {
		this.geographyL4Name = geographyL4Name;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	@Override
	public String toString() {
		return "SiteforgeUserWrapper [roleId=" + roleId + ", geographyL1Name=" + geographyL1Name + ", geographyL2Name="
				+ geographyL2Name + ", geographyL3Name=" + geographyL3Name + ", geographyL4Name=" + geographyL4Name
				+ "]";
	}
	
	

}
