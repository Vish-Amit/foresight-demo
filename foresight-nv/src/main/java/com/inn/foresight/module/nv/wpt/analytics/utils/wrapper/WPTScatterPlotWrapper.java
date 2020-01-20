package com.inn.foresight.module.nv.wpt.analytics.utils.wrapper;

import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class WPTScatterPlotWrapper.
 *
 * @author innoeye
 * date - 03-Nov-2017 6:46:05 PM
 */
@RestWrapper
public class WPTScatterPlotWrapper {
	
	/** The x. */
	Double x ;
	
	/** The y. */
	Double y ;
	
	/** The value. */
	Double value ;
	
	/** The count. */
	Integer count ;
	
	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public Double getX() {
		return x;
	}
	
	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(Double x) {
		this.x = x;
	}
	
	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public Double getY() {
		return y;
	}
	
	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(Double y) {
		this.y = y;
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
	 * Gets the count.
	 *
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}
	
	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "WPTScatterPlotWrapper [x=" + x + ", y=" + y + ", value=" + value + ", count=" + count + "]";
	}


}
