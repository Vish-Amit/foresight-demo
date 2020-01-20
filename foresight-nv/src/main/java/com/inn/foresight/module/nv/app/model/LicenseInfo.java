package com.inn.foresight.module.nv.app.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.app.constants.AppConstants;

/**
 * The Class LicenseInfo.
 *
 * @author innoeye
 * date - 16-Nov-2017 3:12:50 PM
 */
	@NamedQuery(name = AppConstants.GET_LICENSEINFO_BY_IMEI_APPNAME_CLIENTID , query = "select l from LicenseInfo l where l.imei = :imei and l.licenseMaster.appName = :appName and l.licenseMaster.clientName = :clientName")

	@NamedQuery(name = AppConstants.GET_LICENSEINFO_COUNT_BY_LICENSEMASTER , query = "select count(l) from LicenseInfo l where l.licenseMaster = :licenseMaster")

	@NamedQuery(name = AppConstants.GET_LICENSEINFO_FROM_ID , query = "select l from LicenseInfo l where l.licenceId = :licenceId")
	
	@NamedQuery(name = "getLicenceCountGroupByLicenceMaster" , query = "select new  com.inn.foresight.module.nv.app.wrapper.DeviceInfoWrapper(count(licenceId),l.licenseMaster.id) from LicenseInfo l group by l.licenseMaster.id")
	
@XmlRootElement(name = "LicenseInfo")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "LicenceInfo")
public class LicenseInfo implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@Id
	@GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY) 
	@Column(name = "licenceinfoid_pk")
	private Integer id;
	
	/** The licence id. */
	@Column(name = "licenceid")
	private String licenceId;
	
	/** The licence issue date. */
	@Column(name = "licenceissuedate")
	private Date  licenceIssueDate;
	
	/** The license master. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "licencemasterid_fk", nullable = false)
	private LicenseMaster licenseMaster;
	
	/** The imei. */
	private String imei;
	
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
	 * Gets the licence id.
	 *
	 * @return the licence id
	 */
	public String getLicenceId() {
		return licenceId;
	}
	
	/**
	 * Sets the licence id.
	 *
	 * @param licenceId the new licence id
	 */
	public void setLicenceId(String licenceId) {
		this.licenceId = licenceId;
	}
	
	/**
	 * Gets the licence issue date.
	 *
	 * @return the licence issue date
	 */
	public Date getLicenceIssueDate() {
		return licenceIssueDate;
	}
	
	/**
	 * Sets the licence issue date.
	 *
	 * @param licenceIssueDate the new licence issue date
	 */
	public void setLicenceIssueDate(Date licenceIssueDate) {
		this.licenceIssueDate = licenceIssueDate;
	}
		
	/**
	 * Gets the license master.
	 *
	 * @return the license master
	 */
	public LicenseMaster getLicenseMaster() {
		return licenseMaster;
	}

	/**
	 * Sets the license master.
	 *
	 * @param licenseMaster the new license master
	 */
	public void setLicenseMaster(LicenseMaster licenseMaster) {
		this.licenseMaster = licenseMaster;
	}
	
	/**
	 * Gets the imei.
	 *
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}
	
	/**
	 * Sets the imei.
	 *
	 * @param imei the new imei
	 */
	public void setImei(String imei) {
		this.imei = imei;
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

}
