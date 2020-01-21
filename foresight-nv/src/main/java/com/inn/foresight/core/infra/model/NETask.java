package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NETask")
public class NETask implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -397030262718925023L;
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)

	@Column(name = "netaskid_pk")
	private Integer id;
	
	@Column(name = "executionorder")
	private String executionOrder;

	@Column(name = "taskname")
	private String taskName;

	@Column(name = "subtaskname")
	private String subTaskName;

	@Column(name = "creationtime")
	private Date creationTime;

	@Column(name = "modificationtime")
	private Date modificationTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExecutionOrder() {
		return executionOrder;
	}

	public void setExecutionOrder(String executionOrder) {
		this.executionOrder = executionOrder;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getSubTaskName() {
		return subTaskName;
	}

	public void setSubTaskName(String subTaskName) {
		this.subTaskName = subTaskName;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public NETask() {
		super();
	}

	@Override
	public String toString() {
		return "NeTaskConfiguration [id=" + id + ", executionOrder=" + executionOrder + ", taskName=" + taskName
				+ ", subTaskName=" + subTaskName + ", creationTime=" + creationTime + ", modificationTime="
				+ modificationTime + "]";
	}

}
