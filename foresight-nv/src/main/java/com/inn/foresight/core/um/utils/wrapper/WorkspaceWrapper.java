package com.inn.foresight.core.um.utils.wrapper;

import java.io.Serializable;

public class WorkspaceWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "WorkspaceWrapper [id=" + id + ", name=" + name + "]";
	}

}
