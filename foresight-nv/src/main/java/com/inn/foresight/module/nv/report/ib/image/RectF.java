package com.inn.foresight.module.nv.report.ib.image;

public class RectF {
	private Float bottom;
	private Float left;
	private Float right;
	private Float top;
	public RectF(float bottom,float left,float right,float top){
		this.bottom = bottom;
		this.left = left;
		this.right = right;
		this.top = top;
	}

	public RectF(double bottom,double left,double right,double top){
		this.bottom = (float) bottom;
		this.left = (float)left;
		this.right = (float)right;
		this.top = (float)top;
	}
	public Float getBottom() {
		return bottom;
	}
	public void setBottom(Float bottom) {
		this.bottom = bottom;
	}
	public Float getLeft() {
		return left;
	}
	public void setLeft(Float left) {
		this.left = left;
	}
	public Float getRight() {
		return right;
	}
	public void setRight(Float right) {
		this.right = right;
	}
	public Float getTop() {
		return top;
	}
	public void setTop(Float top) {
		this.top = top;
	}
}
