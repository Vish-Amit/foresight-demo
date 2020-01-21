package com.inn.foresight.core.infra.wrapper;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;

/** The Class WingWrapper. */
@JpaWrapper
public class WingWrapper {

	/** The wing id. */
	private Integer wingId;

	/** The wing name. */
	private String wingName;


	/** The creation time. */
	private Long creationTime;

	private Long modificationTime;

	/**
	 * Instantiates a new wing wrapper.
	 *
	 * @param wingId
	 *            the wing id
	 * @param wingName
	 *            the wing name
	 */
	public WingWrapper(Integer wingId, String wingName, Date creationTime, Date modificationTime) {
		super();
		this.wingId = wingId;
		this.wingName = wingName;
		if (creationTime != null) {
			this.creationTime = creationTime.getTime();
		}
		if (modificationTime != null) {
			this.modificationTime = modificationTime.getTime();
		}
	}

	/** Instantiates a new wing wrapper. */
	public WingWrapper() {

	}

	/** The floor list. */
	private List<FloorWrapper> floorList;

	/**
	 * Gets the wing id.
	 *
	 * @return the wingId
	 */
	public Integer getWingId() {
		return wingId;
	}

	/**
	 * Sets the wing id.
	 *
	 * @param wingId
	 *            the wingId to set
	 */
	public void setWingId(Integer wingId) {
		this.wingId = wingId;
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
	 * @param wingName
	 *            the wingName to set
	 */
	public void setWingName(String wingName) {
		this.wingName = wingName;
	}

	/**
	 * Gets the floor list.
	 *
	 * @return the floorList
	 */
	public List<FloorWrapper> getFloorList() {
		return floorList;
	}

	/**
	 * Sets the floor list.
	 *
	 * @param floorList
	 *            the floorList to set
	 */
	public void setFloorList(List<FloorWrapper> floorList) {
		this.floorList = floorList;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WingWrapper [wingId=" + wingId + ", wingName=" + wingName + ", creationTime=" + creationTime
				+ ", modificationTime=" + modificationTime + ", floorList=" + floorList + "]";
	}
	

}
