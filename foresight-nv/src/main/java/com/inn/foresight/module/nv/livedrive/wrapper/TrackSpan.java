package com.inn.foresight.module.nv.livedrive.wrapper;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.core.generic.wrapper.RestWrapper;

/** The Class TrackSpan. */
@JsonIgnoreProperties(ignoreUnknown = true)
@RestWrapper
public class TrackSpan {

	/** The mspanid. */
	private Integer mspanid;
	/** The positions. */
	private List<TrackPosition> positions;

	/**
	 * Gets the mspanid.
	 *
	 * @return the mspanid
	 */
	public Integer getMspanid() {
		return mspanid;
	}

	/**
	 * Sets the mspanid.
	 *
	 * @param mspanid the new mspanid
	 */
	public void setMspanid(Integer mspanid) {
		this.mspanid = mspanid;
	}

	/**
	 * Gets the positions.
	 *
	 * @return the positions
	 */
	public List<TrackPosition> getPositions() {
		return positions;
	}

	/**
	 * Sets the positions.
	 *
	 * @param positions the new positions
	 */
	public void setPositions(List<TrackPosition> positions) {
		this.positions = positions;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "TrackSpan [mspanid=" + mspanid + ", positions=" + positions + "]";
	}

}
