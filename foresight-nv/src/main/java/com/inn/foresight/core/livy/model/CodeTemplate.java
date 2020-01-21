package com.inn.foresight.core.livy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class CodeTemplate.
 * 
 * @author Zafar
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CodeTemplate {
	
	/** The type. */
	private String type;
	
	/** The source name. */
	private String sourceName;
	
	/** The caching. */
	private boolean caching;
	
	/** The app name. */
	private String appName;
	
	/** The parquet data path. */
	private String parquetDataPath;
	
	/** The query. */
	private String query;
	
	/** The temp table. */
	private String tempTable;
	
	/** The show. */
	private boolean show;
	
	
	/**
	 * Gets the source name.
	 *
	 * @return the source name
	 */
	public String getSourceName() {
		return sourceName;
	}
	
	/**
	 * Sets the source name.
	 *
	 * @param sourceName the new source name
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	/**
	 * Gets the query.
	 *
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}
	
	/**
	 * Sets the query.
	 *
	 * @param query the new query
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	
	/**
	 * Gets the temp table.
	 *
	 * @return the temp table
	 */
	public String getTempTable() {
		return tempTable;
	}
	
	/**
	 * Sets the temp table.
	 *
	 * @param tempTable the new temp table
	 */
	public void setTempTable(String tempTable) {
		this.tempTable = tempTable;
	}
	
	/**
	 * Checks if is show.
	 *
	 * @return true, if is show
	 */
	public boolean isShow() {
		return show;
	}
	
	/**
	 * Sets the show.
	 *
	 * @param show the new show
	 */
	public void setShow(boolean show) {
		this.show = show;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Checks if is caching.
	 *
	 * @return true, if is caching
	 */
	public boolean isCaching() {
		return caching;
	}
	
	/**
	 * Sets the caching.
	 *
	 * @param caching the new caching
	 */
	public void setCaching(boolean caching) {
		this.caching = caching;
	}
	
	/**
	 * Gets the app name.
	 *
	 * @return the app name
	 */
	public String getAppName() {
		return appName;
	}
	
	/**
	 * Gets the parquet data path.
	 *
	 * @return the parquet data path
	 */
	public String getParquetDataPath() {
		return parquetDataPath;
	}
	
	/**
	 * Sets the parquet data path.
	 *
	 * @param parquetDataPath the new parquet data path
	 */
	public void setParquetDataPath(String parquetDataPath) {
		this.parquetDataPath = parquetDataPath;
	}
	
	/**
	 * Sets the app name.
	 *
	 * @param appName the new app name
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	@Override
	public String toString() {
		return "CodeTemplate [type=" + type + ", sourceName=" + sourceName + ", caching=" + caching + ", appName=" + appName + ", query=" + query + ", tempTable=" + tempTable + ", show=" + show + "]";
	}
}