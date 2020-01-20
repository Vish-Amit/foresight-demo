package com.inn.foresight.module.nv.dashboard.passive.wrapper;

import java.util.HashMap;
import java.util.Map;

/** The Class NVPassiveWrapper. */
public class NVPassiveWrapper {

	/** Instantiates a new NV passive wrapper. */
	public NVPassiveWrapper() {

	}

	public NVPassiveWrapper(String simSlotDistribution, String technologyDistribution) {
		this.simSlotDistribution = simSlotDistribution;
		this.technologyDistribution = technologyDistribution; 
	}

	
	/**
	 * Instantiates a new NV passive wrapper.
	 *
	 * @param coveragesc
	 *            the coveragesc
	 * @param ncoveragesc
	 *            the ncoveragesc
	 * @param indoorsc
	 *            the indoorsc
	 * @param outdoorsc
	 *            the outdoorsc
	 * @param slot1uc
	 *            the slot 1 uc
	 * @param slot2uc
	 *            the slot 2 uc
	 * @param technologyDistribution
	 *            the technology distribution
	 * @param incomingcc
	 *            the incomingcc
	 * @param outgoingcc
	 *            the outgoingcc
	 */
	public NVPassiveWrapper(long coveragesc, long ncoveragesc, long indoorsc, long outdoorsc, long incomingcc, long outgoingcc) {
		this.coverageSampleCount = coveragesc;
		this.nonCoverageSampleCount = ncoveragesc;
		this.indoorSampleCount = indoorsc;
		this.outdoorSampleCount = outdoorsc;
		this.incomingCount = incomingcc;
		this.outgoingCount = outgoingcc;
	}

	/**
	 * Instantiates a new NV passive wrapper.
	 *
	 * @param sampleDate
	 *            the sample date
	 * @param uniqueUserCount
	 *            the unique user count
	 * @param gpsSampleCount
	 *            the gps sample count
	 * @param nonGpsSampleCount
	 *            the non gps sample count
	 */
	public NVPassiveWrapper(String sampleDate, long uniqueUserCount, long gpsSampleCount, long nonGpsSampleCount) {
		this.sampleDate = sampleDate;
		this.uniqueUserCount = uniqueUserCount;
		this.gpsSampleCount = gpsSampleCount;
		this.nonGpsSampleCount = nonGpsSampleCount;
	}

	public NVPassiveWrapper(String rsrpStats, String rsrqStats, String sinrStats){
		this.rsrpStats = rsrpStats;
		this.rsrqStats = rsrqStats;
		this.sinrStats = sinrStats;
	}
	
	/** The unique user count. */
	private Long uniqueUserCount;

	/** The gps sample count. */
	private Long gpsSampleCount;

	/** The non gps sample count. */
	private Long nonGpsSampleCount;

	/** The coverage sample count. */
	private Long coverageSampleCount;

	/** The non coverage sample count. */
	private Long nonCoverageSampleCount;

	/** The incoming count. */
	private Long incomingCount;

	/** The outgoing count. */
	private Long outgoingCount;

	/** The indoor sample count. */
	private Long indoorSampleCount;

	/** The outdoor sample count. */
	private Long outdoorSampleCount;

	/** The rsrp stats. */
	private String rsrpStats;

	/** The rsrq stats. */
	private String rsrqStats;

	/** The sinr stats. */
	private String sinrStats;

	/** The os stats. */
	private String osStats;

	/** The service distribution. */
	private String serviceDistribution;

	/** The source. */
	private String source;

	private String simSlotDistribution;

	private String mifiOperationalMode;

	/** The technology distribution. */
	private String technologyDistribution;

	private Map<String , Map<String,Long>> aggregatedJsonMap = new HashMap<>();
	
	/** The data source counts. */
	private NVDataSourceCountWrapper dataSourceCounts;

	
	
	/** The sample date. */
	private String sampleDate;

	private Map<String, Integer> simSlotDistributionMap;
	
	private Map<String, Integer> technologyDistributionMap;
	/**
	 * Gets the unique user count.
	 *
	 * @return the unique user count
	 */
	public Long getUniqueUserCount() {
		return uniqueUserCount;
	}

	/**
	 * Sets the unique user count.
	 *
	 * @param uniqueUserCount
	 *            the new unique user count
	 */
	public void setUniqueUserCount(Long uniqueUserCount) {
		this.uniqueUserCount = uniqueUserCount;
	}

	/**
	 * Gets the gps sample count.
	 *
	 * @return the gps sample count
	 */
	public Long getGpsSampleCount() {
		return gpsSampleCount;
	}

	/**
	 * Sets the gps sample count.
	 *
	 * @param gpsSampleCount
	 *            the new gps sample count
	 */
	public void setGpsSampleCount(Long gpsSampleCount) {
		this.gpsSampleCount = gpsSampleCount;
	}

	/**
	 * Gets the non gps sample count.
	 *
	 * @return the non gps sample count
	 */
	public Long getNonGpsSampleCount() {
		return nonGpsSampleCount;
	}

	/**
	 * Sets the non gps sample count.
	 *
	 * @param nonGpsSampleCount
	 *            the new non gps sample count
	 */
	public void setNonGpsSampleCount(Long nonGpsSampleCount) {
		this.nonGpsSampleCount = nonGpsSampleCount;
	}

	/**
	 * Gets the coverage sample count.
	 *
	 * @return the coverage sample count
	 */
	public Long getCoverageSampleCount() {
		return coverageSampleCount;
	}

	/**
	 * Sets the coverage sample count.
	 *
	 * @param coverageSampleCount
	 *            the new coverage sample count
	 */
	public void setCoverageSampleCount(Long coverageSampleCount) {
		this.coverageSampleCount = coverageSampleCount;
	}

	/**
	 * Gets the non coverage sample count.
	 *
	 * @return the non coverage sample count
	 */
	public Long getNonCoverageSampleCount() {
		return nonCoverageSampleCount;
	}

	/**
	 * Sets the non coverage sample count.
	 *
	 * @param nonCoverageSampleCount
	 *            the new non coverage sample count
	 */
	public void setNonCoverageSampleCount(Long nonCoverageSampleCount) {
		this.nonCoverageSampleCount = nonCoverageSampleCount;
	}

	/**
	 * Gets the incoming count.
	 *
	 * @return the incoming count
	 */
	public Long getIncomingCount() {
		return incomingCount;
	}

	/**
	 * Sets the incoming count.
	 *
	 * @param incomingCount
	 *            the new incoming count
	 */
	public void setIncomingCount(Long incomingCount) {
		this.incomingCount = incomingCount;
	}

	/**
	 * Gets the outgoing count.
	 *
	 * @return the outgoing count
	 */
	public Long getOutgoingCount() {
		return outgoingCount;
	}

	/**
	 * Sets the outgoing count.
	 *
	 * @param outgoingCount
	 *            the new outgoing count
	 */
	public void setOutgoingCount(Long outgoingCount) {
		this.outgoingCount = outgoingCount;
	}

	/**
	 * Gets the indoor sample count.
	 *
	 * @return the indoor sample count
	 */
	public Long getIndoorSampleCount() {
		return indoorSampleCount;
	}

	/**
	 * Sets the indoor sample count.
	 *
	 * @param indoorSampleCount
	 *            the new indoor sample count
	 */
	public void setIndoorSampleCount(Long indoorSampleCount) {
		this.indoorSampleCount = indoorSampleCount;
	}

	/**
	 * Gets the outdoor sample count.
	 *
	 * @return the outdoor sample count
	 */
	public Long getOutdoorSampleCount() {
		return outdoorSampleCount;
	}

	/**
	 * Sets the outdoor sample count.
	 *
	 * @param outdoorSampleCount
	 *            the new outdoor sample count
	 */
	public void setOutdoorSampleCount(Long outdoorSampleCount) {
		this.outdoorSampleCount = outdoorSampleCount;
	}

	/**
	 * Gets the rsrp stats.
	 *
	 * @return the rsrp stats
	 */
	public String getRsrpStats() {
		return rsrpStats;
	}

	/**
	 * Sets the rsrp stats.
	 *
	 * @param rsrpStats
	 *            the new rsrp stats
	 */
	public void setRsrpStats(String rsrpStats) {
		this.rsrpStats = rsrpStats;
	}

	/**
	 * Gets the rsrq stats.
	 *
	 * @return the rsrq stats
	 */
	public String getRsrqStats() {
		return rsrqStats;
	}

	/**
	 * Sets the rsrq stats.
	 *
	 * @param rsrqStats
	 *            the new rsrq stats
	 */
	public void setRsrqStats(String rsrqStats) {
		this.rsrqStats = rsrqStats;
	}

	/**
	 * Gets the sinr stats.
	 *
	 * @return the sinr stats
	 */
	public String getSinrStats() {
		return sinrStats;
	}

	/**
	 * Sets the sinr stats.
	 *
	 * @param sinrStats
	 *            the new sinr stats
	 */
	public void setSinrStats(String sinrStats) {
		this.sinrStats = sinrStats;
	}

	/**
	 * Gets the os stats.
	 *
	 * @return the os stats
	 */
	public String getOsStats() {
		return osStats;
	}

	/**
	 * Sets the os stats.
	 *
	 * @param osStats
	 *            the new os stats
	 */
	public void setOsStats(String osStats) {
		this.osStats = osStats;
	}

	/**
	 * Gets the service distribution.
	 *
	 * @return the service distribution
	 */
	public String getServiceDistribution() {
		return serviceDistribution;
	}

	/**
	 * Sets the service distribution.
	 *
	 * @param serviceDistribution
	 *            the new service distribution
	 */
	public void setServiceDistribution(String serviceDistribution) {
		this.serviceDistribution = serviceDistribution;
	}

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 *
	 * @param source
	 *            the new source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	public String getSimSlotDistribution() {
		return simSlotDistribution;
	}

	public void setSimSlotDistribution(String simSlotDistribution) {
		this.simSlotDistribution = simSlotDistribution;
	}

	public String getMifiOperationalMode() {
		return mifiOperationalMode;
	}

	public void setMifiOperationalMode(String mifiOperationalMode) {
		this.mifiOperationalMode = mifiOperationalMode;
	}

	/**
	 * Gets the technology distribution.
	 *
	 * @return the technology distribution
	 */
	public String getTechnologyDistribution() {
		return technologyDistribution;
	}

	/**
	 * Sets the technology distribution.
	 *
	 * @param technologyDistribution
	 *            the new technology distribution
	 */
	public void setTechnologyDistribution(String technologyDistribution) {
		this.technologyDistribution = technologyDistribution;
	}

	/**
	 * Gets the data source counts.
	 *
	 * @return the data source counts
	 */
	public NVDataSourceCountWrapper getDataSourceCounts() {
		return dataSourceCounts;
	}

	/**
	 * Sets the data source counts.
	 *
	 * @param dataSourceCounts
	 *            the new data source counts
	 */
	public void setDataSourceCounts(NVDataSourceCountWrapper dataSourceCounts) {
		this.dataSourceCounts = dataSourceCounts;
	}

	/**
	 * Gets the sample date.
	 *
	 * @return the sample date
	 */
	public String getSampleDate() {
		return sampleDate;
	}

	/**
	 * Sets the sample date.
	 *
	 * @param sampleDate
	 *            the new sample date
	 */
	public void setSampleDate(String sampleDate) {
		this.sampleDate = sampleDate;
	}

	
	
	
	public Map<String, Map<String, Long>> getAggregatedJsonMap() {
		return aggregatedJsonMap;
	}

	public void setAggregatedJsonMap(Map<String, Map<String, Long>> aggregatedJsonMap) {
		this.aggregatedJsonMap = aggregatedJsonMap;
	}

	public Map<String, Integer> getSimSlotDistributionMap() {
		return simSlotDistributionMap;
	}

	public void setSimSlotDistributionMap(
			Map<String, Integer> simSlotDistributionMap) {
		this.simSlotDistributionMap = simSlotDistributionMap;
	}

	public Map<String, Integer> getTechnologyDistributionMap() {
		return technologyDistributionMap;
	}

	public void setTechnologyDistributionMap(
			Map<String, Integer> technologyDistributionMap) {
		this.technologyDistributionMap = technologyDistributionMap;
	}

	@Override
	public String toString() {
		return "NVPassiveWrapper [uniqueUserCount=" + uniqueUserCount + ", gpsSampleCount=" + gpsSampleCount + ", nonGpsSampleCount="
				+ nonGpsSampleCount + ", coverageSampleCount=" + coverageSampleCount + ", nonCoverageSampleCount=" + nonCoverageSampleCount
				+ ", incomingCount=" + incomingCount + ", outgoingCount=" + outgoingCount + ", indoorSampleCount=" + indoorSampleCount
				+ ", outdoorSampleCount=" + outdoorSampleCount + ", rsrpStats=" + rsrpStats + ", rsrqStats=" + rsrqStats + ", sinrStats=" + sinrStats
				+ ", osStats=" + osStats + ", serviceDistribution=" + serviceDistribution + ", source=" + source + ", simSlotDistribution="
				+ simSlotDistribution + ", mifiOperationalMode=" + mifiOperationalMode + ", technologyDistribution=" + technologyDistribution
				+ ", dataSourceCounts=" + dataSourceCounts + ", sampleDate=" + sampleDate + "]";
	}

}
