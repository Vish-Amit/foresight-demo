package com.inn.foresight.module.nv.app.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.app.constants.AppConstants;

/**
 * The Class LicenseMaster.
 *
 * @author innoeye
 * date - 16-Nov-2017 3:10:53 PM
 */
	@NamedQuery(name = AppConstants.GET_LICENSEMASTER_BY_APPNAME_CLIENTID , query = "select l from LicenseMaster l where l.appName = :appName and l.clientName = :clientName")
@XmlRootElement(name = "LicenseMaster")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "LicenceMaster")
@DynamicUpdate
public class LicenseMaster implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@Id
	@GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY) 
	@Column(name = "licencemasterid_pk")
	private Integer id;
	
	/** The app name. */
	@Column(name = "appname")
//	@Enumerated(javax.persistence.EnumType.ORDINAL)
	private String appName;
	
	/** The client name. */
	@Column(name = "clientname")
	private String  clientName;
	
	/** The max no of users. */
	@Column(name = "maxnoofuser")
	private Long maxNoOfUsers;
	
	/** The license validity. */
	@Column(name = "licencevalidity")
	private Integer licenseValidity;
	
	/** The created time. */
	@Column(name = "creationtime",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date createdTime;
	
	/** The modified time. */
	@Column(name = "modificationtime",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private Date modifiedTime;
		
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
	 * Gets the app name.
	 *
	 * @return the app name
	 */

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	/**
	 * Gets the client name.
	 *
	 * @return the client name
	 */
	public String getClientName() {
		return clientName;
	}
	
	/**
	 * Sets the client name.
	 *
	 * @param clientName the new client name
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	/**
	 * Gets the max no of users.
	 *
	 * @return the max no of users
	 */
	public Long getMaxNoOfUsers() {
		return maxNoOfUsers;
	}
	
	/**
	 * Sets the max no of users.
	 *
	 * @param maxNoOfUsers the new max no of users
	 */
	public void setMaxNoOfUsers(Long maxNoOfUsers) {
		this.maxNoOfUsers = maxNoOfUsers;
	}
	
	/**
	 * Gets the license validity.
	 *
	 * @return the license validity
	 */
	public Integer getLicenseValidity() {
		return licenseValidity;
	}
	
	/**
	 * Sets the license validity.
	 *
	 * @param licenseValidity the new license validity
	 */
	public void setLicenseValidity(Integer licenseValidity) {
		this.licenseValidity = licenseValidity;
	}
	
	/**
	 * Gets the created time.
	 *
	 * @return the created time
	 */
	public Date getCreatedTime() {
		return createdTime;
	}
	
	/**
	 * Sets the created time.
	 *
	 * @param createdTime the new created time
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
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

	@Override
	public String toString() {
		return "LicenseMaster [id=" + id + ", appName=" + appName + ", clientName=" + clientName + ", maxNoOfUsers="
				+ maxNoOfUsers + ", licenseValidity=" + licenseValidity + ", createdTime=" + createdTime
				+ ", modifiedTime=" + modifiedTime + "]";
	}
}
