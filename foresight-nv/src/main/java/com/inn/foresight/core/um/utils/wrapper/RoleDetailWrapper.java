package com.inn.foresight.core.um.utils.wrapper;

import java.io.Serializable;

public class RoleDetailWrapper implements Serializable {

	
	private static final long serialVersionUID = 1L;

	private Integer roleId;
	private String roleName;
	private String description;

	private ForesightTeamWrapper team;

	private String levelType;

	private String geoType;
	private WorkspaceCountryWrapper workSpace;
	
	public RoleDetailWrapper() {
		super();
	}



	public RoleDetailWrapper(Integer roleId, String roleName, String description, ForesightTeamWrapper team, String levelType,
			String geoType,WorkspaceCountryWrapper workspace) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.description = description;
		this.team = team;
		this.levelType = levelType;
		this.geoType=geoType;
		this.workSpace = workspace;
	}



	public Integer getRoleId() {
		return roleId;
	}



	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}



	public String getRoleName() {
		return roleName;
	}



	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public ForesightTeamWrapper getTeam() {
		return team;
	}



	public void setTeam(ForesightTeamWrapper team) {
		this.team = team;
	}



	public String getLevelType() {
		return levelType;
	}



	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}



	public String getGeoType() {
		return geoType;
	}



	public void setGeoType(String geoType) {
		this.geoType = geoType;
	}



	public WorkspaceCountryWrapper getWorkSpace() {
		return workSpace;
	}



	public void setWorkSpace(WorkspaceCountryWrapper workSpace) {
		this.workSpace = workSpace;
	}

}
