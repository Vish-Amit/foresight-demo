package com.inn.foresight.module.nv.inbuilding.wrapper;

import com.inn.commons.maps.LatLng;

/** The Class NearestBuildingWrapper. */
@SuppressWarnings("serial")
public class NearestBuildingWrapper extends LatLng{

	/** The building id. */
	private Integer buildingId;

	/**
	 * Gets the building id.
	 *
	 * @return the building id
	 */
	public Integer getBuildingId() {
		return buildingId;
	}

	/**
	 * Sets the building id.
	 *
	 * @param buildingId the new building id
	 */
	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}

	@Override
	public String toString() {
		return "NearestBuildingWrapper [buildingId=" + buildingId + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}
	
	
	
}