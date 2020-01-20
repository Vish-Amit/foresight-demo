package com.inn.foresight.module.nv.wpt.analytics.utils.wrapper;

import java.util.List;

import com.inn.commons.lang.NumberUtils;
import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class TopURLCountWrapper.
 *
 * @author innoeye
 * date - 12-Dec-2017 6:10:19 PM
 */
@RestWrapper
public class TopURLCountWrapper {
	
	
	/** The total hit count. */
	private Long totalHitCount;
	
	/** The url wrappers. */
	private List<TopURLDetailWrapper> urlWrappers;
	
	
	
	/** Instantiates a new top URL count wrapper. */
	public TopURLCountWrapper() {
		totalHitCount = NumberUtils.LONG_ZERO;
	}

	/**
	 * Gets the total hit count.
	 *
	 * @return the total hit count
	 */
	public Long getTotalHitCount() {
		return totalHitCount;
	}

	/**
	 * Sets the total hit count.
	 *
	 * @param totalCount the new total hit count
	 */
	public void setTotalHitCount(Long totalCount) {
		this.totalHitCount = totalCount;
	}

	/**
	 * Gets the url wrappers.
	 *
	 * @return the url wrappers
	 */
	public List<TopURLDetailWrapper> getUrlWrappers() {
		return urlWrappers;
	}
	
	/**
	 * Sets the url wrappers.
	 *
	 * @param list the new url wrappers
	 */
	public void setUrlWrappers(List<TopURLDetailWrapper> list) {
		this.urlWrappers = list;
	}
}
