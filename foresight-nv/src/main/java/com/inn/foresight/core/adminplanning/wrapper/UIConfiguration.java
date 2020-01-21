package com.inn.foresight.core.adminplanning.wrapper;

import java.io.Serializable;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;

@JpaWrapper
@RestWrapper
public class UIConfiguration implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8434384237895739304L;

	private String type;
	
	private String name;
	
	private String groupName;
	
	private String inputValueUnit;
	
	private String algorithmDisplayName;
	
	private String metaData;

	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getGroupName() {
		return groupName;
	}



	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}



	public String getInputValueUnit() {
		return inputValueUnit;
	}



	public void setInputValueUnit(String inputValueUnit) {
		this.inputValueUnit = inputValueUnit;
	}



	public String getAlgorithmDisplayName() {
		return algorithmDisplayName;
	}



	public void setAlgorithmDisplayName(String algorithmDisplayName) {
		this.algorithmDisplayName = algorithmDisplayName;
	}



	public String getMetaData() {
		return metaData;
	}



	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}



	@Override
	public String toString() {
		return "UIConfiguration [type=" + type + ", name=" + name + ", groupName=" + groupName + ", inputValueUnit="
				+ inputValueUnit + ", algorithmDisplayName=" + algorithmDisplayName + ", metaData=" + metaData + "]";
	}
}
