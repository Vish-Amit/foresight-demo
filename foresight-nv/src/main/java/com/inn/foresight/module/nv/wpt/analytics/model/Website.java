package com.inn.foresight.module.nv.wpt.analytics.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/** The Class Website. */
@Entity
@Table(name = "Website")
@XmlRootElement(name = "Website")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Website implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@Id
	@GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY) 
	@Column(name = "websiteid_pk")
	private Integer id;

	/** The site name. */
	@Column(name = "sitename")
	private String siteName;
	
	/** The is https. */
	@Column(name = "https")
	private Boolean isHttps;

	/** The category. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "webcategoryid_fk", nullable = false)
	private WebCategory category;

	
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
	 * Gets the site name.
	 *
	 * @return the site name
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * Sets the site name.
	 *
	 * @param siteName the new site name
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * Gets the checks if is https.
	 *
	 * @return the checks if is https
	 */
	public Boolean getIsHttps() {
		return isHttps;
	}

	/**
	 * Sets the checks if is https.
	 *
	 * @param isHttps the new checks if is https
	 */
	public void setIsHttps(Boolean isHttps) {
		this.isHttps = isHttps;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public WebCategory getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(WebCategory category) {
		this.category = category;
	}

}
