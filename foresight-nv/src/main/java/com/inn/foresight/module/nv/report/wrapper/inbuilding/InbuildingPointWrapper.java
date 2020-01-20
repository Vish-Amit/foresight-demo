package com.inn.foresight.module.nv.report.wrapper.inbuilding;

import java.util.List;

public class InbuildingPointWrapper {

	private List<String[]> arlist;
	private Integer disX;
	private Integer disY;
	private String imageFloorPalnPath;
	public List<String[]> getArlist() {
		return arlist;
	}
	public void setArlist(List<String[]> arlist) {
		this.arlist = arlist;
	}
	public Integer getDisX() {
		return disX;
	}
	public void setDisX(Integer disX) {
		this.disX = disX;
	}
	public Integer getDisY() {
		return disY;
	}
	public void setDisY(Integer disY) {
		this.disY = disY;
	}
	
	public String getImageFloorPalnPath() {
		return imageFloorPalnPath;
	}
	public void setImageFloorPalnPath(String imageFloorPalnPath) {
		this.imageFloorPalnPath = imageFloorPalnPath;
	}
	
	@Override
	public String toString() {
		return "InbuildingPointWrapper [arlist="  + ", disX=" + disX + ", disY=" + disY
				+ ", imageFloorPalnPath=" + imageFloorPalnPath + "]";
	}
	
}
