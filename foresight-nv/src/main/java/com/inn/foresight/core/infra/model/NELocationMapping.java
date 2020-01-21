package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class NELocationMapping.
 */

@NamedQueries({
	@NamedQuery(name = "getParentNELocationByNELType", query = "select ne.parentNELocation from NELocationMapping ne where ne.neLocation.id = :nelocationid_fk and ne.parentNELocation.nelType=:nelType"),
	
	})

@XmlRootElement(name = "NELocationMapping")
@Entity
@Table(name = "NELocationMapping")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NELocationMapping implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7716188767441692595L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nelocationmappingid_pk")
	private Integer id;

	/** The NELocation. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nelocationid_fk", nullable = true)
	private NELocation neLocation;
	
	/** The parent NELocation. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentnelocationid_fk",nullable = true,insertable = false,updatable = false)
	private NELocation parentNELocation;
	
	/** The created time. */
	@Basic
	@Column(name = "creationtime")
	private Date createdTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NELocation getNeLocation() {
		return neLocation;
	}

	public void setNeLocation(NELocation neLocation) {
		this.neLocation = neLocation;
	}

	public NELocation getParentNELocation() {
		return parentNELocation;
	}

	public void setParentNELocation(NELocation parentNELocation) {
		this.parentNELocation = parentNELocation;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public String toString() {
		return "NELocationMapping [id=" + id + ", neLocation=" + neLocation + ", parentNELocation=" + parentNELocation
				+ "]";
	}

	

}
