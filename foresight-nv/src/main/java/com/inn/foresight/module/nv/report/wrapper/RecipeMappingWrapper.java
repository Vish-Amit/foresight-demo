package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class RecipeMappingWrapper {
	private List<String> recpiList ;
	private List<String>operatorList;
	public List<String> getRecpiList() {
		return recpiList;
	}
	public void setRecpiList(List<String> recpiList) {
		this.recpiList = recpiList;
	}
	public List<String> getOperatorList() {
		return operatorList;
	}
	public void setOperatorList(List<String> operatorList) {
		this.operatorList = operatorList;
	}
	@Override
	public String toString() {
		return "RecipeMappingWrapper [recpiList=" + recpiList + ", operatorList=" + operatorList + "]";
	}
	
}
