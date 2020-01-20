package com.inn.foresight.module.nv.report;

public class RangeSlab implements Comparable<RangeSlab> {

	Double lowerLimit;
	Double upperLimit;
	int color;

	String colorCode;

	Integer count;

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public Double getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public Double getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Double upperLimit) {
		this.upperLimit = upperLimit;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public RangeSlab(Double lowerLimit, Double upperLimit, int color) {
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.color = color;

	}

	public RangeSlab() {
		super();
	}

	@Override
	public String toString() {
		return "RangeSlab [lowerLimit=" + lowerLimit + ", upperLimit=" + upperLimit + ", color=" + color + ", count="
				+ count + "]";
	}

	@Override
	public int compareTo(RangeSlab o) {

		if (this.lowerLimit - o.lowerLimit == 0.0) {
			return 0;
		} else if (this.lowerLimit - o.lowerLimit > 0) {
			return 1;
		} else if (this.lowerLimit - o.lowerLimit < 0) {
			return -1;
		} else {
			return 0;
		}

	}

}
