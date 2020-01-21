package com.inn.foresight.core.infra.wrapper;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;

/** The Class FloorWrapper. */
@JpaWrapper
public class FloorWrapper {

	/** The floor id. */
	private Integer floorId;

	/** The floor number. 
	 * use to store floor Name 
	 * */
	
	private String floorNumber;
	
	/* 
	 * used to store floor Number 
	 */
	private Integer number;
	
	/** The creation time. */
	private Long creationTime;
	
	private Long modificationTime;


	/** The unit list. */
	private List<UnitWrapper> unitList;

	/**
	 * Instantiates a new floor wrapper.
	 *
	 * @param floorId the floor id
	 * @param floorNumber the floor number
	 */
	public FloorWrapper(Integer floorId, String floorName,Date creationTime,Date modificationTime) {
		super();
		this.floorId = floorId;
		this.floorNumber = floorName;
		if (creationTime != null) {
			this.creationTime = creationTime.getTime();
		}
		if (modificationTime != null) {
			this.modificationTime = modificationTime.getTime();
		}
	}
	
	public FloorWrapper(Integer floorId, Integer number, String floorNumber) {
		super();
		this.floorId = floorId;
		this.number = number;
		this.floorNumber = floorNumber;
	}

	/** Instantiates a new floor wrapper. */
	public FloorWrapper() {

	}

	/**
	 * Gets the floor id.
	 *
	 * @return the floorId
	 */
	public Integer getFloorId() {
		return floorId;
	}

	/**
	 * Sets the floor id.
	 *
	 * @param floorId
	 *            the floorId to set
	 */
	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	/**
	 * Gets the floor number.
	 *
	 * @return the floorNumber
	 */
	
	/**
	 * Gets the unit list.
	 *
	 * @return the unitList
	 */
	public List<UnitWrapper> getUnitList() {
		return unitList;
	}

	/**
	 * Sets the unit list.
	 *
	 * @param unitList
	 *            the unitList to set
	 */
	public void setUnitList(List<UnitWrapper> unitList) {
		this.unitList = unitList;
	}

	/**
	 * @return the creationTime
	 */
	public Long getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
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
	 * @param modificationTime the modificationTime to set
	 */
	public void setModificationTime(Long modificationTime) {
		this.modificationTime = modificationTime;
	}

	public String getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(String floorNumber) {
		this.floorNumber = floorNumber;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "FloorWrapper [floorId=" + floorId + ", floorNumber=" + floorNumber + ", number=" + number
				+ ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + ", unitList=" + unitList
				+ "]";
	}
	
}
