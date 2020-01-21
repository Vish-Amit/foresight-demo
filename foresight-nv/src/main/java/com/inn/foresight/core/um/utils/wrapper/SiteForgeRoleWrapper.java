package com.inn.foresight.core.um.utils.wrapper;

import java.io.Serializable;

public class SiteForgeRoleWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String description;
	private Integer roleid;
	private String rolename;
	private Integer geoHierarchyId;
	private String geoHierarchyCategory;
	private String workspaceName;
	private Integer workspaceId;
	private String geoHierarchyLevel;
	
	
	
	public String getGeoHierarchyLevel() {
		return geoHierarchyLevel;
	}
	public void setGeoHierarchyLevel(String geoHierarchyLevel) {
		this.geoHierarchyLevel = geoHierarchyLevel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getRoleid() {
		return roleid;
	}
	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public Integer getGeoHierarchyId() {
		return geoHierarchyId;
	}
	public void setGeoHierarchyId(Integer geoHierarchyId) {
		this.geoHierarchyId = geoHierarchyId;
	}
	public String getGeoHierarchyCategory() {
		return geoHierarchyCategory;
	}
	public void setGeoHierarchyCategory(String geoHierarchyCategory) {
		this.geoHierarchyCategory = geoHierarchyCategory;
	}
	public String getWorkspaceName() {
		return workspaceName;
	}
	public void setWorkspaceName(String workspaceName) {
		this.workspaceName = workspaceName;
	}
	public Integer getWorkspaceId() {
		return workspaceId;
	}
	public void setWorkspaceId(Integer workspaceId) {
		this.workspaceId = workspaceId;
	}
	@Override
	public String toString() {
		return "SiteForgeRoleWrapper [description=" + description + ", roleid=" + roleid + ", rolename=" + rolename
				+ ", geoHierarchyId=" + geoHierarchyId + ", geoHierarchyCategory=" + geoHierarchyCategory
				+ ", workspaceName=" + workspaceName + ", workspaceId=" + workspaceId + ", geoHierarchyLevel="
				+ geoHierarchyLevel + "]";
	}
	
	
}
