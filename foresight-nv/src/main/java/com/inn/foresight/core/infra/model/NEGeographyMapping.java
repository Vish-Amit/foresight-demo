package com.inn.foresight.core.infra.model;

import java.io.Serializable;

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
import com.inn.product.um.geography.model.OtherGeography;

@NamedQueries({
		@NamedQuery(name = "getNodeDetailByDomainVendor", query = "select n.neName from NEGeographyMapping ne ,NetworkElement n,OtherGeography o where upper(n.domain)=:domain and upper(n.vendor)=:vendor and upper(n.neType)=:node and n.id= ne.networkElement.id and o.id=ne.otherGeography.id "),
		@NamedQuery(name = "getNodeDetailByGeography", query = "select n.neName from NEGeographyMapping ne,NetworkElement n,OtherGeography o where upper(n.domain)=:domain and upper(n.vendor)=:vendor and upper(n.neType)=:node and upper(o.name)=:geography and n.id= ne.networkElement.id and o.id=ne.otherGeography.id "),
		@NamedQuery(name = "getDistinctOtherGeographyDetail", query = "select distinct o.name  from NEGeographyMapping ne , NetworkElement n,OtherGeography o where upper(n.domain)=:domain and upper(n.vendor)=:vendor and n.id= ne.networkElement.id and o.id=ne.otherGeography.id "), 
		@NamedQuery(name = "getNEMappingCountByNEId", query = "select count(*) from NEGeographyMapping neGeo where  neGeo.networkElement.id=:networkElementId and neGeo.otherGeography.id=:otherGeographyId ")
})

@Entity
@Table(name = "NEGeographyMapping")
@XmlRootElement(name = "NEGeographyMapping")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NEGeographyMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5378321656280084789L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "othergeographyid_pk")
	private Integer id;

	/** The NetworkElement . */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "networkelementid_fk", nullable = true)
	private NetworkElement networkElement;

	/** The OtherGeography . */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "othergeographyid_fk", nullable = true)
	private OtherGeography otherGeography;

	public NetworkElement getNetworkElement() {
		return networkElement;
	}

	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}

	public OtherGeography getOtherGeography() {
		return otherGeography;
	}

	public void setOtherGeography(OtherGeography otherGeography) {
		this.otherGeography = otherGeography;
	}

}