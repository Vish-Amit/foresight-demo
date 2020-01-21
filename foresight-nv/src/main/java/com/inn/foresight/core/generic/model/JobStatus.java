package com.inn.foresight.core.generic.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.core.generic.model.JobSatatusConstant.ProcessType;

/**
 * The Class JobStatus.
 */
@NamedQueries({ 
	@NamedQuery(name = "getDetailForConstant", query = "select l from JobStatus l where UPPER(l.typeconstant) = UPPER(:typeconstant)"),
	@NamedQuery(name = "getTypeconstantDetails", query = "select l from JobStatus l where l.typeconstant in (:typeconstant)"),
})


@Entity
@Table(name = "JobStatus")
@XmlRootElement(name = "JobStatus")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Audited
public class JobStatus implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "jobstatusid_pk")
	private long id;

	/** The typeconstant. */
	@Enumerated(value=EnumType.STRING)
	@Column(name = "typeconstant")
	private ProcessType typeconstant;
	
	/** The value. */
	@Column(name = "value")
	private long value;
	
	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modificationtime. */
	@Column(name = "modificationTime")
	private Date modificationtime;


	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
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
	 * Gets the typeconstant.
	 *
	 * @return the typeconstant
	 */
	public ProcessType getTypeconstant() {
		return typeconstant;
	}

	/**
	 * Sets the typeconstant.
	 *
	 * @param typeconstant the new typeconstant
	 */
	public void setTypeconstant(ProcessType typeconstant) {
		this.typeconstant = typeconstant;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public long getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(long value) {
		this.value = value;
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
	 * Gets the modificationtime.
	 *
	 * @return the modificationtime
	 */
	public Date getModificationtime() {
		return modificationtime;
	}

	/**
	 * Sets the modificationtime.
	 *
	 * @param modificationtime the new modificationtime
	 */
	public void setModificationtime(Date modificationtime) {
		this.modificationtime = modificationtime;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "JobStatus [id=" + id + ", typeconstant=" + typeconstant
				+ ", value=" + value + ", creationTime=" + creationTime
				+ ", modificationtime=" + modificationtime + "]";
	}
	

}
