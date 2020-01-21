package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ProcessConfiguration")
@XmlRootElement(name = "ProcessConfiguration")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })

@NamedQueries({
	@NamedQuery(name = "getProcessConfiguration", query = "select p from ProcessConfiguration p order by p.modificationTime desc, id"),
	@NamedQuery(name = "getProcessConfigurationByType", query = "select p from ProcessConfiguration p where p.type = :type"),
	@NamedQuery(name = "getProcessConfigurationByNameList", query = "select p from ProcessConfiguration p where p.name in(:nameList)  "),
	@NamedQuery(name = "getProcessConfigurationCount" , query = "select count(p.id) from ProcessConfiguration p"),
	@NamedQuery(name = "getAllProcessConfiguration", query = "select p from ProcessConfiguration p"),

})

public class ProcessConfiguration implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2985096302981403558L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "processconfigurationid_pk")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "type")
	private String type;

	@Column(name = "value")
	private String value;
	
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
		return "ProcessConfiguration [id=" + id + ", name=" + name + ", type=" + type + ", creationTime=" + creationTime
				+ ", modificationTime=" + modificationTime + "]";
	}

	public ProcessConfiguration() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
