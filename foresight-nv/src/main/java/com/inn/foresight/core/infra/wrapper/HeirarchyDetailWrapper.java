package com.inn.foresight.core.infra.wrapper;

import java.util.List;

/**
 * The Class HeirarchyDetailWrapper.
 */
public class HeirarchyDetailWrapper {
	
	/** The key. */
	private String key;
	
	/** The value. */
	private String value;
	
	/** The technology. */
	private List<String> technology;
	
	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Sets the key.
	 *
	 * @param key the new key
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public List<String> getTechnology() {
		return technology;
	}
	
	/**
	 * Sets the technology.
	 *
	 * @param technology the new technology
	 */
	public void setTechnology(List<String> technology) {
		this.technology = technology;
	}
}
