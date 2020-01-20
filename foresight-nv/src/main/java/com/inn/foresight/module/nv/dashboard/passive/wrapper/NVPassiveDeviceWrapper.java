package com.inn.foresight.module.nv.dashboard.passive.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;

/** The Class NVPassiveDeviceWrapper. */
@JpaWrapper
@RestWrapper
public class NVPassiveDeviceWrapper {

	public NVPassiveDeviceWrapper() {
		
	}
	
	public NVPassiveDeviceWrapper(String id, String name, long value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public NVPassiveDeviceWrapper(String parent, String id, String name, long value) {
		this.parent = parent;
		this.id = id;
		this.name = name;
		this.value = value;
	}

	
	
	
	/** The id. */
	String id;
	
	/** The name. */
	String name;
	
	/** The parent. */
	String parent;
	
	/** The value. */
	Long value;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 *
	 * @param parent the new parent
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Long getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(Long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "NVPassiveDeviceWrapper [id=" + id + ", name=" + name + ", parent=" + parent + ", value=" + value + "]";
	}
	
}
