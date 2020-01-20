package com.inn.foresight.module.nv.livedrive.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;

/** The Class TrackSpanSummaryWrapper. */
@RestWrapper
public class TrackSpanSummaryWrapper {
	/** The track span wrappers. */
	private List<TrackSpanWrapper> trackSpanWrappers;
	/** The status. */
	private String status;
	/** The travel distance. */
	private Float travelDistance;

	/**
	 * Gets the track span wrappers.
	 *
	 * @return the track span wrappers
	 */
	public List<TrackSpanWrapper> getTrackSpanWrappers() {
		return trackSpanWrappers;
	}

	/**
	 * Sets the track span wrappers.
	 *
	 * @param trackSpanWrappers the new track span wrappers
	 */
	public void setTrackSpanWrappers(List<TrackSpanWrapper> trackSpanWrappers) {
		this.trackSpanWrappers = trackSpanWrappers;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the travel distance.
	 *
	 * @return the travel distance
	 */
	public Float getTravelDistance() {
		return travelDistance;
	}

	/**
	 * Sets the travel distance.
	 *
	 * @param travelDistance the new travel distance
	 */
	public void setTravelDistance(Float travelDistance) {
		this.travelDistance = travelDistance;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "TrackSpamSummaryWrapper [trackSpanWrappers="
				+ trackSpanWrappers + ", status=" + status
				+ ", travelDistance=" + travelDistance + "]";
	}

}
