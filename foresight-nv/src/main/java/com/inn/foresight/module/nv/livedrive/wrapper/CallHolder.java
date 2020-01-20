package com.inn.foresight.module.nv.livedrive.wrapper;

/** The type Call holder. */
public class CallHolder {
	private String callState;
	private String duration;
	private Long callTime;
	private String callType;

	/**
	 * Gets call state.
	 *
	 * @return the call state
	 */
	public String getCallState() {
		return callState;
	}

	/**
	 * Sets call state.
	 *
	 * @param callState the call state
	 */
	public void setCallState(String callState) {
		this.callState = callState;
	}

	/**
	 * Gets duration.
	 *
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * Sets duration.
	 *
	 * @param duration the duration
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

	/**
	 * Gets call time.
	 *
	 * @return the call time
	 */
	public Long getCallTime() {
		return callTime;
	}

	/**
	 * Sets call time.
	 *
	 * @param callTime the call time
	 */
	public void setCallTime(Long callTime) {
		this.callTime = callTime;
	}

	/**
	 * Gets call type.
	 *
	 * @return the call type
	 */
	public String getCallType() {
		return callType;
	}

	/**
	 * Sets call type.
	 *
	 * @param callType the call type
	 */
	public void setCallType(String callType) {
		this.callType = callType;
	}
	@Override
	public String toString() {
		return "CallHolder [callState=" + callState + ", duration=" + duration
				+ ", callTime=" + callTime + ", callType=" + callType + "]";
	}
}
