package com.inn.foresight.module.nv.core.workorder.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

	@NamedQuery(name = "findMetaByGenericWorkOrderId" , query = "select g from GWOMeta g where g.genericWorkOrder.id=:genericWorkOrderId and g.entityType=:entityType")

@Entity

@XmlRootElement(name = "GWOMeta")
@Table(name = "GWOMeta")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class GWOMeta implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "gwometaid_pk")
	 private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "genericworkorderid_fk", nullable = true)
	private GenericWorkorder genericWorkOrder;

	@Column(name = "entitytype")
	private String entityType;

	
	@Column(name = "entityvalue")
	private String entityValue;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public GenericWorkorder getGenericWorkOrder() {
		return genericWorkOrder;
	}


	public void setGenericWorkOrder(GenericWorkorder genericWorkOrder) {
		this.genericWorkOrder = genericWorkOrder;
	}


	public String getEntityType() {
		return entityType;
	}


	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}


	public String getEntityValue() {
		return entityValue;
	}


	public void setEntityValue(String entityValue) {
		this.entityValue = entityValue;
	}


	@Override
	public String toString() {
		return "GWOMeta [id=" + id + ", genericWorkOrder=" + genericWorkOrder + ", entityType=" + entityType
				+ ", entityValue=" + entityValue + "]";
	}


}
