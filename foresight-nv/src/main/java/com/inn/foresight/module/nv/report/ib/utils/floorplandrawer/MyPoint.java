package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer;

public class MyPoint  {
	private Float x;
	private Float y;
	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public MyPoint(float x1,float y1) {
		this.x = x1;
		this.y = y1;
	}

	public MyPoint(){
	}
	public Float getX() {
		return x;
	}

	public Float getY() {
		return y;
	}
}
