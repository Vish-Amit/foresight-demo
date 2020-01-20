package com.inn.foresight.module.nv.livedrive.wrapper;

/** The Class PingValueHolder. */
public class PingValueHolder {

	/** The min latency. */
	private Double minLatency;
	/** The max latency. */
	private Double maxLatency;
	/** The avg latency. */
	private Double avgLatency;
	/** The pckt transmitted. */
	private Double pcktTransmitted;
	/** The pckt received. */
	private Double pcktReceived;
	/** The pckt loss. */
	private Double pcktLoss;
	/** The time. */
	private Double time;
	/** The avg jitter. */
	private Double avgJitter;
	/** The host. */
	private String host;

	/**
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the host.
	 *
	 * @param host the new host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Gets the avg jitter.
	 *
	 * @return the avg jitter
	 */
	public Double getAvgJitter() {
		return avgJitter;
	}

	/**
	 * Sets the avg jitter.
	 *
	 * @param avgJitter the new avg jitter
	 */
	public void setAvgJitter(Double avgJitter) {
		this.avgJitter = avgJitter;
	}

	/**
	 * Gets the min latency.
	 *
	 * @return the min latency
	 */
	public Double getMinLatency() {
		return minLatency;
	}

	/**
	 * Sets the min latency.
	 *
	 * @param minLatency the new min latency
	 */
	public void setMinLatency(Double minLatency) {
		this.minLatency = minLatency;
	}

	/**
	 * Gets the max latency.
	 *
	 * @return the max latency
	 */
	public Double getMaxLatency() {
		return maxLatency;
	}

	/**
	 * Sets the max latency.
	 *
	 * @param maxLatency the new max latency
	 */
	public void setMaxLatency(Double maxLatency) {
		this.maxLatency = maxLatency;
	}

	/**
	 * Gets the avg latency.
	 *
	 * @return the avg latency
	 */
	public Double getAvgLatency() {
		return avgLatency;
	}

	/**
	 * Sets the avg latency.
	 *
	 * @param avgLatency the new avg latency
	 */
	public void setAvgLatency(Double avgLatency) {
		this.avgLatency = avgLatency;
	}

	/**
	 * Gets the pckt transmitted.
	 *
	 * @return the pckt transmitted
	 */
	public Double getPcktTransmitted() {
		return pcktTransmitted;
	}

	/**
	 * Sets the pckt transmitted.
	 *
	 * @param pcktTransmitted the new pckt transmitted
	 */
	public void setPcktTransmitted(Double pcktTransmitted) {
		this.pcktTransmitted = pcktTransmitted;
	}

	/**
	 * Gets the pckt received.
	 *
	 * @return the pckt received
	 */
	public Double getPcktReceived() {
		return pcktReceived;
	}

	/**
	 * Sets the pckt received.
	 *
	 * @param pcktReceived the new pckt received
	 */
	public void setPcktReceived(Double pcktReceived) {
		this.pcktReceived = pcktReceived;
	}

	/**
	 * Gets the pckt loss.
	 *
	 * @return the pckt loss
	 */
	public Double getPcktLoss() {
		return pcktLoss;
	}

	/**
	 * Sets the pckt loss.
	 *
	 * @param pcktLoss the new pckt loss
	 */
	public void setPcktLoss(Double pcktLoss) {
		this.pcktLoss = pcktLoss;
	}

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public Double getTime() {
		return time;
	}

	/**
	 * Sets the time.
	 *
	 * @param time the new time
	 */
	public void setTime(Double time) {
		this.time = time;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "PingValueHolder [minLatency=" + minLatency + ", maxLatency=" + maxLatency + ", avgLatency=" + avgLatency + ", pcktTransmitted=" + pcktTransmitted + ", pcktReceived=" + pcktReceived
				+ ", pcktLoss=" + pcktLoss + ", time=" + time + "]";
	}

}
