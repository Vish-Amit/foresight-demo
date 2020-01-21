package com.inn.foresight.core.infra.wrapper;

import java.util.Date;

import com.inn.foresight.core.infra.constants.InBuildingConstants.UnitType;
import com.inn.core.generic.wrapper.JpaWrapper;

/** The Class UnitWrapper. */
@JpaWrapper
public class UnitWrapper {

	/** The unit id. */
	private Integer unitId;

	/** The unit name. */
	private String unitName;

	/** The unit type. */
	private UnitType unitType;

	/** The is floor plan uploaded. */
	private boolean isFloorPlanUploaded;

	/** The is floor plan available. */
	private boolean isFloorPlanAvailable;

	/** The creation time. */
	private Long creationTime;

	private Long modificationTime;

	private Boolean isFloorPlanApproved;
	/**
	 * Instantiates a new unit wrapper.
	 *
	 * @param unitId
	 *            the unit id
	 * @param unitName
	 *            the unit name
	 * @param unitType
	 *            the unit type
	 * @param isFloorPlanAvailable
	 *            the is floor plan available
	 */
	public UnitWrapper(Integer unitId, String unitName, UnitType unitType, boolean isFloorPlanAvailable,
			Date creationTime, Date modificationTime,Boolean isFloorPlanApproved) {
		super();
		this.unitId = unitId;
		this.unitName = unitName;
		this.unitType = unitType;
		this.isFloorPlanAvailable = isFloorPlanAvailable;
		this.isFloorPlanUploaded = isFloorPlanAvailable;
		if (creationTime != null) {
			this.creationTime = creationTime.getTime();
		}
		if (modificationTime != null) {
			this.modificationTime = modificationTime.getTime();
		}
		this.isFloorPlanApproved = isFloorPlanApproved;
		
	}
	
	public UnitWrapper(Integer unitId, String unitName) {
		super();
		this.unitId = unitId;
		this.unitName = unitName;
	}

	/** Instantiates a new unit wrapper. */
	public UnitWrapper() {
	}

	/**
	 * Gets the unit id.
	 *
	 * @return the unitId
	 */
	public Integer getUnitId() {
		return unitId;
	}

	/**
	 * Sets the unit id.
	 *
	 * @param unitId
	 *            the unitId to set
	 */
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	 * @param unitName
	 *            the unitName to set
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
	 * @param unitType
	 *            the unitType to set
	 */
	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	/**
	 * Checks if is floor plan uploaded.
	 *
	 * @return the isFloorPlanUploaded
	 */
	public boolean isFloorPlanUploaded() {
		return isFloorPlanUploaded;
	}

	/**
	 * Sets the floor plan uploaded.
	 *
	 * @param isFloorPlanUploaded
	 *            the isFloorPlanUploaded to set
	 */
	public void setFloorPlanUploaded(boolean isFloorPlanUploaded) {
		this.isFloorPlanUploaded = isFloorPlanUploaded;
	}

	/**
	 * Checks if is floor plan available.
	 *
	 * @return the isFloorPlanAvailable
	 */
	public boolean isFloorPlanAvailable() {
		return isFloorPlanAvailable;
	}

	/**
	 * Sets the floor plan available.
	 *
	 * @param isFloorPlanAvailable
	 *            the isFloorPlanAvailable to set
	 */
	public void setFloorPlanAvailable(boolean isFloorPlanAvailable) {
		this.isFloorPlanAvailable = isFloorPlanAvailable;
	}

	/**
	 * @return the creationTime
	 */
	public Long getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime
	 *            the creationTime to set
	 */
	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the modificationTime
	 */
	public Long getModificationTime() {
		return modificationTime;
	}

	/**
	 * @param modificationTime
	 *            the modificationTime to set
	 */
	public void setModificationTime(Long modificationTime) {
		this.modificationTime = modificationTime;
	}

	public Boolean getIsFloorPlanApproved() {
		return isFloorPlanApproved;
	}

	public void setIsFloorPlanApproved(Boolean isFloorPlanApproved) {
		this.isFloorPlanApproved = isFloorPlanApproved;
	}
/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UnitWrapper [unitId=" + unitId + ", unitName=" + unitName + ", unitType=" + unitType
				+ ", isFloorPlanUploaded=" + isFloorPlanUploaded + ", isFloorPlanAvailable=" + isFloorPlanAvailable
				+ ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + ", isFloorPlanApproved="
				+ isFloorPlanApproved + "]";
	}
	
}
