package com.inn.foresight.module.nv.report.ib.image;

import java.awt.Color;

public class PointF {
	private Double x;
	private Double y;
	private Color color;
	public  PointF(double x, double y){
		this.x =  x;
		this.y =  y;
	}
	public  PointF(Double x, Double y,Color color){
		this.x =  x;
		this.y =  y;
		this.color=color;
	}
	public  PointF(float x, float y){
		this.x =  (double) x;
		this.y =  (double) y;
	}
	public Double getX() {
		return x;
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	/** Public PointF(Double x,Double y) { super(); this.x = x; this.y = y; } */
	public PointF() {
	}

	public PointF(PointF point) {
		this.x = point.x;
		this.y = point.y;
	}
	public String getDisplayValue(){
		return "("+x+","+y+")";
	}
	@Override
	public String toString() {
		return "{\"x\":" + x + "," + "\"y\":" + y + "}";
	}
}
