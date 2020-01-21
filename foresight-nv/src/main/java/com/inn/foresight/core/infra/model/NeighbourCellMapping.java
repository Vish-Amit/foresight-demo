package com.inn.foresight.core.infra.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement(name = "NeighbourCellMapping")
@Entity
@Table(name = "NeighbourCellMapping")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class NeighbourCellMapping implements Serializable   {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2024066279663377115L;

	@Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "neighbourcellmappingid_pk")
    private Integer id;
	
	@JoinColumn(name = "networkelementid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NetworkElement networkelementid_fk;
	
	@JoinColumn(name = "neighbourid_fk", nullable = true)
	@OneToOne(fetch = FetchType.LAZY)
	private NetworkElement neighbourid_fk;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NetworkElement getNetworkelementid_fk() {
		return networkelementid_fk;
	}

	public void setNetworkelementid_fk(NetworkElement networkelementid_fk) {
		this.networkelementid_fk = networkelementid_fk;
	}

	public NetworkElement getNeighbourid_fk() {
		return neighbourid_fk;
	}

	public void setNeighbourid_fk(NetworkElement neighbourid_fk) {
		this.neighbourid_fk = neighbourid_fk;
	}

	@Override
	public String toString() {
		return "NeighbourCellMapping [id=" + id + ", networkelementid_fk=" + networkelementid_fk + ", neighbourid_fk="
				+ neighbourid_fk + "]";
	}

}
