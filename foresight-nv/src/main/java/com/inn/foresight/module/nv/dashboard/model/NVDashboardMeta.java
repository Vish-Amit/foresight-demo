package com.inn.foresight.module.nv.dashboard.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.dashboard.utils.NVDashboardConstants;

/** The Class NVDashboardMeta. */

@NamedQuery(name = NVDashboardConstants.GET_ALL_NVDASHBOARDMETA_QUERY, query = "select d from NVDashboardMeta d where d.isDeleted=false")

@Entity
@Table(name = "NVDashboardMeta")
@XmlRootElement(name = "NVDashboardMeta")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NVDashboardMeta implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The meta key. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nvdashboardmetaid_pk")
	private Integer metaKey;

	/** The creation time. */
	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The band. */
	@Basic
	@Column(name = "band")
	private String band;

	/** The technology. */
	@Basic
	@Column(name = "technology")
	private String technology;

	/** The operator. */
	@Basic
	@Column(name = "operator")
	private String operator;

	/** The is deleted. */
	@Basic
	@Column(name = "deleted")
	private Boolean isDeleted;

	/**
	 * Gets the meta key.
	 *
	 * @return the id
	 */
	public Integer getMetaKey() {
		return metaKey;
	}

	/**
	 * Sets the meta key.
	 *
	 * @param metaKey
	 *            the new meta key
	 */
	public void setMetaKey(Integer metaKey) {
		this.metaKey = metaKey;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime
	 *            the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the modification time.
	 *
	 * @return the modificationTime
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * Sets the modification time.
	 *
	 * @param modificationTime
	 *            the modificationTime to set
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * Gets the band.
	 *
	 * @return the band
	 */
	public String getBand() {
		return band;

	}

	/**
	 * Sets the band.
	 *
	 * @param band
	 *            the band to set
	 */
	public void setBand(String band) {
		this.band = band;
	}

	/**
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 *
	 * @param technology
	 *            the technology to set
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 *
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Gets the checks if is deleted.
	 *
	 * @return the isDeleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * Sets the checks if is deleted.
	 *
	 * @param isDeleted
	 *            the isDeleted to set
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "NVDashboardMeta [metaKey=" + metaKey + ", creationTime=" + creationTime + ", modificationTime="
				+ modificationTime + ", band=" + band + ", technology=" + technology + ", operator=" + operator
				+ ", isDeleted=" + isDeleted + "]";
	}

}
