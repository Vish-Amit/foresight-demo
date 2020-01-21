package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import com.inn.foresight.core.infra.constants.InBuildingConstants.UnitType;

/** The Class Unit. */
@NamedQueries({
	@NamedQuery(name = InBuildingConstants.GET_ALL_UNIT_FOR_FLOOR, query ="select new com.inn.foresight.core.infra.wrapper.UnitWrapper(u.id,u.unitName,u.unitType,u.isFloorPlanAvailable,u.creationTime,u.modificationTime,f.isApproved) from Unit u left join FloorPlanMapping f on u.id=f.unit.id where u.floor=:floorPlaceHolder and u.isDeleted=false"),
	@NamedQuery(name = "getAllAdhocUnitForFloor", query ="select new com.inn.foresight.core.infra.wrapper.UnitWrapper(u.id,u.unitName,u.unitType,u.isFloorPlanAvailable,u.creationTime,u.modificationTime,f.isApproved) from Unit u left join FloorPlanMapping f on u.id=f.unit.id where u.floor=:floorPlaceHolder and u.isDeleted=false and ((f.unit is not null and f.templateType='ADHOC') or (f.unit is null and f.templateType is null))"),
	@NamedQuery(name = "getUnitDetailsByFloorId", query ="SELECT new com.inn.foresight.core.infra.wrapper.UnitWrapper(u.id,u.unitName) FROM Unit u WHERE u.floor.id=:floorId AND u.isDeleted=false"),
})

@Entity
@XmlRootElement(name="Unit")
@Table(name = "Unit")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Unit  implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY) 
	@Column(name = "unitid_pk")
	private Integer id;
	
	
	/** The unit name. */
	@Column(name = "name")
	private String unitName;
	
	/** The unit type. */
	@Enumerated(EnumType.STRING)
	@Column(name="type")
	private UnitType unitType;
	
	/** The floor. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="floorid_fk")
	private Floor floor;
	
	/** The is deleted. */
	@Column(name="deleted")
	private boolean isDeleted;
	


	/** The is floor plan available. */
	@Column(name="floorplanavailable")
	private boolean isFloorPlanAvailable;
	
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
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the unit name.
	 *
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * Sets the unit name.
	 *
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * Gets the unit type.
	 *
	 * @return the unitType
	 */
	public UnitType getUnitType() {
		return unitType;
	}

	/**
	 * Sets the unit type.
	 *
	 * @param unitType the unitType to set
	 */
	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	/**
	 * Gets the floor.
	 *
	 * @return the floorData
	 */
	public Floor getFloor() {
		return floor;
	}

	/**
	 * Sets the floor.
	 *
	 * @param floorData the floorData to set
	 */
	public void setFloor(Floor floorData) {
		this.floor = floorData;
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
	 * @return the isFloorPlanAvailable
	 */
	public boolean isFloorPlanAvailable() {
		return isFloorPlanAvailable;
	}

	/**
	 * @param isFloorPlanAvailable the isFloorPlanAvailable to set
	 */
	public void setFloorPlanAvailable(boolean isFloorPlanAvailable) {
		this.isFloorPlanAvailable = isFloorPlanAvailable;
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
