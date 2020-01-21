package com.inn.foresight.core.um.utils.wrapper;

import java.io.Serializable;
import java.util.List;

public class SiteForgeUserDetailWrapper implements Serializable
{
/**
	 * 
	 */
private static final long serialVersionUID = 1L;
private String roleName;
private List<String> geographyL1Name;
private List<String> geographyL2Name;
private List<String> geographyL3Name;
private List<String> geographyL4Name;
private String workspace;
private String level;




public String getLevel() {
	return level;
}
public void setLevel(String level) {
	this.level = level;
}
public String getWorkspace() {
	return workspace;
}
public void setWorkspace(String workspace) {
	this.workspace = workspace;
}
public String getRoleName() {
	return roleName;
}
public void setRoleName(String roleName) {
	this.roleName = roleName;
}
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




}
