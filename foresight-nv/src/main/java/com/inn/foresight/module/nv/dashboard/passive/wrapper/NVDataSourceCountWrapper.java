package com.inn.foresight.module.nv.dashboard.passive.wrapper;

/** The Class NVDataSourceCountWrapper. */
public class NVDataSourceCountWrapper {

	/** Instantiates a new NV data source count wrapper. */
	public NVDataSourceCountWrapper() {

	}

	/**
	 * Instantiates a new NV data source count wrapper.
	 *
	 * @param nvCount
	 *            the nv count
	 * @param mysfCount
	 *            the mysf count
	 * @param featurePhoneCount
	 *            the feature phone count
	 */
	public NVDataSourceCountWrapper(long nvCount, long mysfCount, long featurePhoneCount) {
		this.netvelocityCounts = nvCount;
		this.mysfCounts = mysfCount;
		this.featurePhoneCounts = featurePhoneCount;
		this.combinedCounts = nvCount + mysfCount + featurePhoneCount;
	}

	/** The netvelocity counts. */
	private Long netvelocityCounts;

	/** The mysf counts. */
	private Long mysfCounts;

	/** The feature phone counts. */
	private Long featurePhoneCounts;

	private long combinedCounts;
	/**
	 * Gets the netvelocity count.
	 *
	 * @return the netvelocity count
	 */
	public Long getNetvelocityCount() {
		return netvelocityCounts;
	}

	/**
	 * Sets the netvelocity count.
	 *
	 * @param netvelocityCount
	 *            the new netvelocity count
	 */
	public void setNetvelocityCount(Long netvelocityCount) {
		this.netvelocityCounts = netvelocityCount;
	}

	/**
	 * Gets the mysf count.
	 *
	 * @return the mysf count
	 */
	public Long getMysfCount() {
		return mysfCounts;
	}

	/**
	 * Sets the mysf count.
	 *
	 * @param mysfCount
	 *            the new mysf count
	 */
	public void setMysfCount(Long mysfCount) {
		this.mysfCounts = mysfCount;
	}

	/**
	 * Gets the feature phone count.
	 *
	 * @return the feature phone count
	 */
	public Long getFeaturePhoneCount() {
		return featurePhoneCounts;
	}

	/**
	 * Sets the feature phone count.
	 *
	 * @param featurePhoneCount
	 *            the new feature phone count
	 */
	public void setFeaturePhoneCount(Long featurePhoneCount) {
		this.featurePhoneCounts = featurePhoneCount;
	}

	public long getCombinedCounts() {
		return combinedCounts;
	}

	public void setCombinedCounts(long combinedCounts) {
		this.combinedCounts = combinedCounts;
	}

	@Override
	public String toString() {
		return "NVDataSourceCountWrapper [netvelocityCount=" + netvelocityCounts + ", mysfCount=" + mysfCounts + ", featurePhoneCount="
				+ featurePhoneCounts + "]";
	}

}
