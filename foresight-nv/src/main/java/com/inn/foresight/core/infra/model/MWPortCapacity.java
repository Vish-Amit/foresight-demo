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
import javax.persistence.NamedQueries;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class MWPortCapacity.
 */ 


@NamedQueries({
})

@Entity
@XmlRootElement(name = "MWPortCapacity")
@Table(name = "MWPortCapacity")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class MWPortCapacity implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "mwportcapacityid_pk")
	private Integer id;

	/** The networkElement detail. */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "networkelementid_fk")
	private NetworkElement networkElement;

	/** The netmask name. */
	@Basic
	@Column(name = "port")
	private String port;

	/** The creation time. */
	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modificationTime;
	
	/** The capacity. */
	@Basic
	@Column(name = "capacity")
	private Double capacity;
	
	/** The unit. */
	@Basic
	@Column(name = "unit")
	private String unit;

	/** The deleted. */
	@Basic
	@Column(name = "deleted")
	private Boolean isDeleted;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NetworkElement getNetworkElement() {
		return networkElement;
	}

	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
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

	public Double getCapacity() {
		return capacity;
	}

	public void setCapacity(Double capacity) {
		this.capacity = capacity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "MWPortCapacity [id=" + id + ", networkElement="
				+ networkElement + ", port=" + port + ", creationTime="
				+ creationTime + ", modificationTime=" + modificationTime
				+ ", capacity=" + capacity + ", unit=" + unit + ", isDeleted="
				+ isDeleted + "]";
	}

}
