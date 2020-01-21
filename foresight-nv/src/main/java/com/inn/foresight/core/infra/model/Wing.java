package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

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
import com.inn.foresight.core.infra.constants.InBuildingConstants;

/** The Class Wing. */
@NamedQueries({
	@NamedQuery(name = InBuildingConstants.GET_ALL_WING_FOR_BUILDING , query ="select new com.inn.foresight.core.infra.wrapper.WingWrapper(w.id,w.wingName,w.creationTime,w.modificationTime) from Wing w where w.building=:buildingPlaceHolder and w.isDeleted=false"),
})

@Entity
@XmlRootElement(name = "Wing")
@Table(name = "Wing")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Wing  implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "wingid_pk")
	private Integer id;

	/** The wing name. */
	@Column(name = "name")
	private String wingName;

	/** The building. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buildingid_fk")
	private Building building;
	
	/** The is deleted. */
	@Column(name="deleted")
	private boolean isDeleted;
	
	/** The creation time. */
	@Column(name="modificationtime")
	private Date modificationTime;
	

	/** The creation time. */
	@Column(name="creationtime")
	private Date creationTime;

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
	 * @param id            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the wing name.
	 *
	 * @return the wingName
	 */
	public String getWingName() {
		return wingName;
	}

	/**
	 * Sets the wing name.
	 *
	 * @param wingName            the wingName to set
	 */
	public void setWingName(String wingName) {
		this.wingName = wingName;
	}

	/**
	 * Gets the building.
	 *
	 * @return the buildingData
	 */
	public Building getBuilding() {
		return building;
	}

	/**
	 * Sets the building.
	 *
	 * @param buildingData            the buildingData to set
	 */
	public void setBuilding(Building buildingData) {
		this.building = buildingData;
	}
	
	/**
	 * Gets the checks if is deleted.
	 *
	 * @return the isDeleted
	 */
	public boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * Sets the checks if is deleted.
	 *
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
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

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
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

}
