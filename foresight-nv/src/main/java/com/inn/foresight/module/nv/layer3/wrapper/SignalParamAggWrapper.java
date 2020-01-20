package com.inn.foresight.module.nv.layer3.wrapper;


/**
 * The Class SignalParamAggWrapper.
 *
 * @author innoeye
 * date - 16-Jan-2018 7:16:26 PM
 */
public class SignalParamAggWrapper {
	
	/** The value. */
	private Object value;
	
	/** The avg. */
	private Double avg;
	
	/** The count. */
	private Long count;
	
	/** Instantiates a new signal param agg wrapper. */
	public SignalParamAggWrapper() {
		super();
	}

	/**
	 * Instantiates a new signal param agg wrapper.
	 *
	 * @param value the value
	 */
	public SignalParamAggWrapper(String value) {
		super();
		this.value = value;
	}
	
	/**
	 * Instantiates a new signal param agg wrapper.
	 *
	 * @param value the value
	 */
	public SignalParamAggWrapper(Integer value) {
		super();
		this.value = value;
	}
	
	/**
	 * Instantiates a new signal param agg wrapper.
	 *
	 * @param value the value
	 */
	public SignalParamAggWrapper(Long value) {
		super();
		this.value = value;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Gets the avg.
	 *
	 * @return the avg
	 */
	public Double getAvg() {
		return avg;
	}
	
	/**
	 * Sets the avg.
	 *
	 * @param avg the new avg
	 */
	public void setAvg(Double avg) {
		this.avg = avg;
	}
	
	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}
	
	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(Long count) {
		this.count = count;
	}

	
}
