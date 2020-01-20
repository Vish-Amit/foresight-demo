package com.inn.foresight.module.nv.wpt.analytics.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** The Class WPTLocation. */
@Entity
@Table(name = "WPTLocation")
@XmlRootElement(name = "WPTLocation")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class WPTLocation implements Serializable{
	
/** The Constant serialVersionUID. */
private static final long serialVersionUID = 1L;
	
	/** The id. */
	@Id
	@Column(name = "wptlocationid_pk")
	private Integer id;
	
	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modified time. */
	@Column(name = "modifiedtime")
	private Date modifiedTime;
	
	/** The name. */
	@Column(name = "name")
	private String name;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creation time
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime the new creation time
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the modified time.
	 *
	 * @return the modified time
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * Sets the modified time.
	 *
	 * @param modifiedTime the new modified time
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
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
	
}
