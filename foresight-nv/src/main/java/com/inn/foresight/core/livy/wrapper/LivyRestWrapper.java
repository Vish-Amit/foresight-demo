package com.inn.foresight.core.livy.wrapper;

import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class LivyRestWrapper.
 */
@RestWrapper
public class LivyRestWrapper {
	
	
	private String query;
	
	private String tempTable;
	
	private String appName;
	
	private Boolean show;
	
	private Boolean caching;
	
	
	
	/**
	 * Default Constructor
	 */
	public LivyRestWrapper(){
		
	}
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getTempTable() {
		return tempTable;
	}

	public void setTempTable(String tempTable) {
		this.tempTable = tempTable;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

	public Boolean getCaching() {
		return caching;
	}

	public void setCaching(Boolean caching) {
		this.caching = caching;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CMLivyRestWrapper [query=");
		builder.append(query);
		builder.append(", tempTable=");
		builder.append(tempTable);
		builder.append(", appName=");
		builder.append(appName);
		builder.append(", show=");
		builder.append(show);
		builder.append(", caching=");
		builder.append(caching);
		builder.append("]");
		return builder.toString();
	}

}
