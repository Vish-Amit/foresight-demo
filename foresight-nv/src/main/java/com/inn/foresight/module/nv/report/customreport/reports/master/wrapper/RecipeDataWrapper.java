package com.inn.foresight.module.nv.report.customreport.reports.master.wrapper;

import java.util.List;

public class RecipeDataWrapper {
	private List<String[]> driveDataList;
	private String sectorName;
	private String recipeId;
	private String operatorName;

	public List<String[]> getDriveDataList() {
		return driveDataList;
	}

	public void setDriveDataList(List<String[]> driveDataList) {
		this.driveDataList = driveDataList;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public String getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

}
