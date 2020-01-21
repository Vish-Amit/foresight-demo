package com.inn.foresight.core.ztepower.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "ZTEPowerMeteInfo")
@XmlRootElement(name = "ZTEPowerMeteInfo")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class ZTEPowerMeteInfo implements Serializable {

	private static final long serialVersionUID = -3231474107575046670L;


	  @Id
	  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	  @Column(name = "ztepowermeteinfoid_pk")
	  private Integer id;


	  @Column(name = "meteid")
	  private String meteId;
	  
	  @Column(name = "metename")
	  private String meteName;

	  @Column(name = "metekind ")
	  private String meteKind ;
	  
	  @Column(name = "creationtime")
	  private Date creationTime;

	  @Column(name = "modificationtime")
	  private Date modificationTime;
	  
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the meteId
	 */
	public String getMeteId() {
		return meteId;
	}

	/**
	 * @param meteId the meteId to set
	 */
	public void setMeteId(String meteId) {
		this.meteId = meteId;
	}

	/**
	 * @return the meteName
	 */
	public String getMeteName() {
		return meteName;
	}

	/**
	 * @param meteName the meteName to set
	 */
	public void setMeteName(String meteName) {
		this.meteName = meteName;
	}

	/**
	 * @return the meteKind
	 */
	public String getMeteKind() {
		return meteKind;
	}

	/**
	 * @param meteKind the meteKind to set
	 */
	public void setMeteKind(String meteKind) {
		this.meteKind = meteKind;
	}

	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the modificationTime
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * @param modificationTime the modificationTime to set
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}
	
}
