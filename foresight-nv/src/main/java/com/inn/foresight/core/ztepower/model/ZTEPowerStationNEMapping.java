package com.inn.foresight.core.ztepower.model;

import java.io.Serializable;
import java.util.Date;

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
import com.inn.foresight.core.infra.model.NetworkElement;


@Entity
@Table(name = "ZTEPowerStationNEMapping")
@XmlRootElement(name = "ZTEPowerStationNEMapping")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class ZTEPowerStationNEMapping implements Serializable {

	private static final long serialVersionUID = 421920818068534639L;

	  @Id
	  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	  @Column(name = "ztepowerstationnemappingid_pk")
	  private Integer id;

	  @JoinColumn(name = "ztepowerstationid_fk", nullable = true)
  	  @ManyToOne(fetch = FetchType.EAGER)
      private ZTEPowerStation ztePowerStation;
      
      @JoinColumn(name = "networkelementid_fk", nullable = true)
  	  @ManyToOne(fetch = FetchType.EAGER)
      private NetworkElement networkElement;
	  
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
	 * @return the ztePowerStation
	 */
	public ZTEPowerStation getZtePowerStation() {
		return ztePowerStation;
	}

	/**
	 * @param ztePowerStation the ztePowerStation to set
	 */
	public void setZtePowerStation(ZTEPowerStation ztePowerStation) {
		this.ztePowerStation = ztePowerStation;
	}

	/**
	 * @return the networkElement
	 */
	public NetworkElement getNetworkElement() {
		return networkElement;
	}

	/**
	 * @param networkElement the networkElement to set
	 */
	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
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

	@Override
	public String toString() {
		return "ZTEPowerStationNEMapping [id=" + id + ", ztePowerStation=" + ztePowerStation + ", networkElement="
				+ networkElement + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + "]";
	}
	  
}
