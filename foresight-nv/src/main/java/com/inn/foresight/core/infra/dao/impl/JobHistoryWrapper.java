package com.inn.foresight.core.infra.dao.impl;

import java.io.Serializable;

public class JobHistoryWrapper implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String weekno;
	private String name;
	private String value;
	private Long sum;
	private Integer min;
	private Integer max;
	
	public JobHistoryWrapper() {
		super();
	}
	
	public JobHistoryWrapper(Integer id, String weekno, String name, String value) {
		super();
		this.id = id;
		this.weekno = weekno;
		this.name = name;
		this.value = value;
	}
	
	public JobHistoryWrapper(Long id) {
		super();
		this.sum=id;
	}
	
	
	
	public JobHistoryWrapper(Integer min, Integer max, String name) {
		super();
		this.min=min;
		this.max=max;
		this.name=name;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getWeekno() {
		return weekno;
	}
	public void setWeekno(String weekno) {
		this.weekno = weekno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "JobHistoryWrapper [id=" + id + ", weekno=" + weekno + ", name=" + name + ", value=" + value + ", sum=" + sum + ", min=" + min + ", max=" + max + "]";
	}


}
