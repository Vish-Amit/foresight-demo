package com.inn.foresight.core.infra.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class GeographyMapping.
 */
@NamedQueries({ @NamedQuery(name = "getAllGeographyNameByGeographyName", query = "select g.geographyName from GeographyMapping g where g.geographyName=:geographyName") })

@Entity
@Table(name = "GeographyMapping")
@XmlRootElement(name = "GeographyMapping")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class GeographyMapping implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3279742085496737297L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "geographyMappingid_pk")
	private Integer id;

	/** The geography name. */
	private String geographyName;

	/** The duplicate name. */
	private String duplicateName;

	/** The type. */
	private String type;

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
	 * Gets the geography name.
	 *
	 * @return the geography name
	 */
	public String getGeographyName() {
		return geographyName;
	}

	/**
	 * Sets the geography name.
	 *
	 * @param geographyName the new geography name
	 */
	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
	}

	/**
	 * Gets the duplicate name.
	 *
	 * @return the duplicate name
	 */
	public String getDuplicateName() {
		return duplicateName;
	}

	/**
	 * Sets the duplicate name.
	 *
	 * @param duplicateName the new duplicate name
	 */
	public void setDuplicateName(String duplicateName) {
		this.duplicateName = duplicateName;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "GeographyMapping [id=" + id + ", geographyName=" + geographyName + ", duplicateName=" + duplicateName + ", type=" + type + "]";
	}
}
