package com.inn.foresight.module.nv.wpt.analytics.utils.wrapper;

import java.util.HashMap;
import java.util.Map;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;

/** The Class WPTStatesWrapper. */
@JpaWrapper
@RestWrapper
public class WPTStatesWrapper {
	
	/** The values. */
	String values;
	
	/** The creation date. */
	String creationDate;
	
	/** The category avg. */
	Map<String, Float> categoryAvg;
	
	/** The category count. */
	Map<String, Integer> categoryCount;
	
	/** Instantiates a new WPT states wrapper. */
	public WPTStatesWrapper() {
		super();
	}

	/**
	 * Instantiates a new WPT states wrapper.
	 *
	 * @param categoryName the category name
	 * @param avg the avg
	 * @param count the count
	 * @param creationDate the creation date
	 */
	public WPTStatesWrapper(String categoryName, Float avg, Integer count, String creationDate) {		
		this.categoryAvg = new HashMap<>();
		this.categoryCount = new HashMap<>();
		categoryAvg.put(categoryName, avg);
		categoryCount.put(categoryName, count);
		this.creationDate = creationDate;
	}

	/**
	 * Instantiates a new WPT states wrapper.
	 *
	 * @param values the values
	 * @param creationDate the creation date
	 */
	public WPTStatesWrapper(String values, String creationDate) {
		this.values = values;
		this.creationDate = creationDate;
	}
	
	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	public String getValues() {
		return values;
	}

	/**
	 * Sets the values.
	 *
	 * @param values the new values
	 */
	public void setValues(String values) {
		this.values = values;
	}

	/**
	 * Gets the category avg.
	 *
	 * @return the category avg
	 */
	public Map<String, Float> getCategoryAvg() {
		return categoryAvg;
	}

	/**
	 * Sets the category avg.
	 *
	 * @param categoryAvg the category avg
	 */
	public void setCategoryAvg(Map<String, Float> categoryAvg) {
		this.categoryAvg = categoryAvg;
	}

	/**
	 * Gets the creation date.
	 *
	 * @return the creation date
	 */
	public String getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creation date.
	 *
	 * @param creationDate the new creation date
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets the category count.
	 *
	 * @return the category count
	 */
	public Map<String, Integer> getCategoryCount() {
		return categoryCount;
	}

	/**
	 * Sets the category count.
	 *
	 * @param categoryCount the category count
	 */
	public void setCategoryCount(Map<String, Integer> categoryCount) {
		this.categoryCount = categoryCount;
	}
	
}
