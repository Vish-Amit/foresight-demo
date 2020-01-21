package com.inn.foresight.core.infra.wrapper;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.wrapper.RestWrapper;
@RestWrapper
public class IBSSelectionLayer {
	private Map<String, List<Map>> filters;

	private Map<String, List<String>> projection;
	private Map value;

	private boolean isGroupBy;
	private boolean isDistinct;

	public Map<String, List<Map>> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, List<Map>> filters) {
		this.filters = filters;
	}

	public Map<String, List<String>> getProjection() {
		return projection;
	}

	public void setProjection(Map<String, List<String>> projection) {
		this.projection = projection;
	}

	public boolean getGroupBy() {
		return isGroupBy;
	}

	public void setGroupBy(boolean isGroupBy) {
		this.isGroupBy = isGroupBy;
	}

	public boolean getDistinct() {
		return isDistinct;
	}

	public Map getValue() {
		return value;
	}

	public void setValue(Map value) {
		this.value = value;
	}

	public boolean isGroupBy() {
		return isGroupBy;
	}

	public boolean isDistinct() {
		return isDistinct;
	}

	public void setDistinct(boolean isDistinct) {
		this.isDistinct = isDistinct;
	}

	@Override
	public String toString() {
		return "IBSSelectionLayer [filters=" + filters + ", projection=" + projection + ", value=" + value + ", isGroupBy=" + isGroupBy + ", isDistinct=" + isDistinct + "]";
	}

	
}
