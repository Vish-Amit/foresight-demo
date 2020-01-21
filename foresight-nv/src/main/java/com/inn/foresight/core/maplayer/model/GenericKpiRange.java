package com.inn.foresight.core.maplayer.model;

/**
 * The Class GenericKpiRange.
 */
public class GenericKpiRange {

	/** The colour. */
	private String colour;

	/** The min. */
	private Double min;

	/** The max. */
	private Double max;

	/** The value. */
	private Double value=0.0;

	/** The rangeNumber. */
	private Integer rangeNumber;


	@Override
	public String toString() {
		return "GenericKpiRange [colour=" + colour + ", min=" + min + ", max=" + max + ", value=" + value
				+ ", rangeNumber=" + rangeNumber + "]";
	}

	/**
	 * Gets the colour.
	 *
	 * @return the colour
	 */
	public String getColour() {
		return colour;
	}

	/**
	 * Sets the colour.
	 *
	 * @param colour the new colour
	 */
	public void setColour(String colour) {
		this.colour = colour;
	}

	/**
	 * Gets the min
	 *
	 * @return the min
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * Sets the min.
	 *
	 * @param min the new min
	 */
	public void setMin(Double min) {
		this.min = min;
	}

	/**
	 * Gets the max.
	 *
	 * @return the max
	 */
	public Double getMax() {
		return max;
	}

	/**
	 * Sets the max.
	 *
	 * @param max the new max
	 */
	public void setMax(Double max) {
		this.max = max;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * check point within range.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean inRange(Double value) {
		if (min != null && max != null) {
			if (value >= min && value < max) {
				return true;
			}
		}
		return false;
	}

	/**
	 * update count with input value.
	 *
	 * @param count the count
	 */
	public void updateCount(Long count) {
		if (value == null) {
			value = (double)count;
		} else {
			value += count;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */


	public Integer getRangeNumber() {
		return rangeNumber;
	}

	public void setRangeNumber(Integer rangeNumber) {
		this.rangeNumber = rangeNumber;
	}
}
