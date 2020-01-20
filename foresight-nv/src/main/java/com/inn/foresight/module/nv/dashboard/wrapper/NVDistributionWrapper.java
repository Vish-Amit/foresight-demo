package com.inn.foresight.module.nv.dashboard.wrapper;


import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;

/** The Class NVDistributionWrapper. */
@JpaWrapper
@RestWrapper
public class NVDistributionWrapper {

	/** The name. */
	private String name;

	/** The sample count. */
	private Long sampleCount;

	/** The share. */
	private Double share;

	private Long activeSampleCount;
	private Long passiveSampleCount;

	public NVDistributionWrapper(String name, Long activeSampleCount, Long passiveSampleCount) {
		super();
		this.name = name;
		this.activeSampleCount = activeSampleCount;
		this.passiveSampleCount = passiveSampleCount;
	}

	public NVDistributionWrapper(Long activeSampleCount, Long passiveSampleCount) {
		super();
		this.activeSampleCount = activeSampleCount;
		this.passiveSampleCount = passiveSampleCount;
	}
	public Long getActiveSampleCount() {
		return activeSampleCount;
	}

	public void setActiveSampleCount(Long activeSampleCount) {
		this.activeSampleCount = activeSampleCount;
	}

	public Long getPassiveSampleCount() {
		return passiveSampleCount;
	}

	public void setPassiveSampleCount(Long passiveSampleCount) {
		this.passiveSampleCount = passiveSampleCount;
	}

	/**
	 * Instantiates a new NV distribution wrapper.
	 *
	 * @param name
	 *            the name
	 * @param sampleCount
	 *            the sample count
	 */
	public NVDistributionWrapper(String name, Long sampleCount) {
		super();
		this.name = name;
		this.sampleCount = sampleCount;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the sample count.
	 *
	 * @return the sampleCount
	 */
	public Long getSampleCount() {
		return sampleCount;
	}

	/**
	 * Sets the sample count.
	 *
	 * @param sampleCount
	 *            the sampleCount to set
	 */
	public void setSampleCount(Long sampleCount) {
		this.sampleCount = sampleCount;
	}

	/**
	 * Gets the share.
	 *
	 * @return the share
	 */
	public Double getShare() {
		return share;
	}

	/**
	 * Sets the share.
	 *
	 * @param share
	 *            the share to set
	 */
	public void setShare(Double share) {
		this.share = share;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "NVDistributionWrapper [name=" + name + ", sampleCount=" + sampleCount + ", share=" + share + "]";
	}

}
