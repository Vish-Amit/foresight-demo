package com.inn.foresight.core.infra.wrapper;

import java.util.Date;

public class SystemConfigurationWrapper {

	private Integer id;

	private String name;

	private String type;

	private String value;

	private String configType;
	
	private Date creationTime;

	private Date modificationTime;
	
	public SystemConfigurationWrapper() {
		super();
	}

	public SystemConfigurationWrapper(Integer id, String name, String type, String value, String configType,
			Date creationTime, Date modificationTime) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.value = value;
		this.configType = configType;
		this.creationTime = creationTime;
		this.modificationTime = modificationTime;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
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

	@Override
	public String toString() {
		return "SystemConfigurationWrapper [id=" + id + ", name=" + name + ", type=" + type + ", value=" + value
				+ ", configType=" + configType + ", creationTime=" + creationTime + ", modificationTime="
				+ modificationTime + "]";
	}
}
