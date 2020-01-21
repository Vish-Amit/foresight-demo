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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQuery(name = "getDistinctVIPCategories", query = "select distinct category from NEDetail where category is not null or category='' ")
@NamedQuery(name = "getMorphologyByCode", query = "select distinct nd.morphology from NEDetail nd where nd.networkElement.neName=:btsName")
@NamedQuery(name = "getListOfVIPNetworkElementFromNeNameList", query = "select nd.networkElement.neName from NEDetail nd where nd.networkElement.domain=:domain "
		+ "and nd.networkElement.vendor in (:vendor) and nd.networkElement.neName in (:neNameList) and nd.category in (:category)")

@NamedQuery(name = "getClutterAndRadiusByNeName", query = "select nd.clutter,ne.radius from NEDetail nd  join NetworkElement ne  on nd.networkElement=ne.id where ne.neName=:neName")


@Entity
@Table(name = "NEDetail")
@XmlRootElement(name = "NEDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Audited
public class NEDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6004001949351386829L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nedetailid_pk")
	private Integer id;

	@Basic
	@Column(name = "contactname")
	private String contactName;

	@Basic
	@Column(name = "contactnumber")
	private String contactNumber;
	

	@Basic
	@Column(name = "email")
	private String email;
	
	@Basic
	@Column(name = "category")
	private String category;

	@Basic
	@Column(name = "creationtime")
	private Date creationTime;


	@Basic
	@Column(name = "modificationtime")
	private Date modificationTime;
	

	@Basic
	@Column(name = "morphology")
	private String morphology;

	
	@JoinColumn(name = "networkelementid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;

	@Basic
	@Column(name = "clutter")
	private String clutter;

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getContactName() {
		return contactName;
	}


	public void setContactName(String contactName) {
		this.contactName = contactName;
	}


	public String getContactNumber() {
		return contactNumber;
	}


	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
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


	public String getMorphology() {
		return morphology;
	}


	public void setMorphology(String morphology) {
		this.morphology = morphology;
	}


	public NetworkElement getNetworkElement() {
		return networkElement;
	}


	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}

    public String getClutter() {
		return clutter;
	}

    public void setClutter(String clutter) {
		this.clutter = clutter;
	}


	@Override
	public String toString() {
		return "NEDetail [id=" + id + ", contactName=" + contactName + ", contactNumber=" + contactNumber + ", email="
				+ email + ", category=" + category + ", creationTime=" + creationTime + ", modificationTime="
				+ modificationTime + ", morphology=" + morphology + ", networkElement=" + networkElement + ", clutter="
				+ clutter + "]";
	}


	
	
	
}