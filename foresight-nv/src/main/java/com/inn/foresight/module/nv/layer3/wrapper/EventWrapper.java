package com.inn.foresight.module.nv.layer3.wrapper;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class EventWrapper {
	
	private String name;
	
	@SerializedName("list")
	private List<ListWrapper> listwrapper;	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ListWrapper> getListwrapper() {
		return listwrapper;
	}
	public void setListwrapper(List<ListWrapper> listwrapper) {
		this.listwrapper = listwrapper;
	}
	
	

}
