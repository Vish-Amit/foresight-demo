package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer;

import java.util.ArrayList;
import java.util.List;

import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.Shape;


public class FloorPlanWrapper {
	private String buildingRjid;
	private String unit;
	private double scalePixelPerUnit;
	private double scaleValue;
	private long timeStamp;
	private int floorBitmapHeight;
	private int floorBitmapWidth;
	private List<Shape> shapeList = new ArrayList<>();
	public List<Shape> getShapeList() {
		return shapeList;
	}
	public void setShapeList(List<Shape> shapeList) {
		this.shapeList = shapeList;
	}
	public String getBuildingRjid() {
		return buildingRjid;
	}
	public void setBuildingRjid(String buildingRjid) {
		this.buildingRjid = buildingRjid;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getScalePixelPerUnit() {
		return scalePixelPerUnit;
	}
	public void setScalePixelPerUnit(double scalePixelPerUnit) {
		this.scalePixelPerUnit = scalePixelPerUnit;
	}
	public double getScaleValue() {
		return scaleValue;
	}
	public void setScaleValue(double scaleValue) {
		this.scaleValue = scaleValue;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public int getFloorBitmapHeight() {
		return floorBitmapHeight;
	}
	public void setFloorBitmapHeight(int floorBitmapHeight) {
		this.floorBitmapHeight = floorBitmapHeight;
	}
	public int getFloorBitmapWidth() {
		return floorBitmapWidth;
	}
	public void setFloorBitmapWidth(int floorBitmapWidth) {
		this.floorBitmapWidth = floorBitmapWidth;
	}



}
