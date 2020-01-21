package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "TowerProvider")
@XmlRootElement(name = "TowerProvider")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class TowerProvider implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3150479301754887502L;
	
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "towerproviderid_pk")
	private Integer id;

	@Basic
	@Column(name = "name")
	private String name;
	
	@Basic
	@Column(name = "servicetype")
	private String serviceType;
	
	@Basic
	@Column(name = "legendcolor")
	private String legendColor;
	
	@Basic
	@Column(name = "providerrank")
	private Integer providerRank;
	
	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	@Basic
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getLegendColor() {
		return legendColor;
	}

	public void setLegendColor(String legendColor) {
		this.legendColor = legendColor;
	}

	public Integer getProviderRank() {
		return providerRank;
	}

	public void setProviderRank(Integer providerRank) {
		this.providerRank = providerRank;
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
		return "TowerProvider [id=" + id + ", name=" + name + ", serviceType=" + serviceType + ", legendColor="
				+ legendColor + ", providerRank=" + providerRank + ", creationTime=" + creationTime
				+ ", modificationTime=" + modificationTime + "]";
	}

}
