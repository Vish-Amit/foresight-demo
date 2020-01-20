package com.inn.foresight.module.nv.dashboard.passive.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.dashboard.passive.utils.NVPassiveConstants;
import com.inn.product.um.geography.model.GeographyL4;

/** The Class NVPassiveDashboard. */

@NamedQuery(name = "getL1WisePassiveData", query = "select p from NVPassiveDashboard p where p.geographyl4.geographyL3.geographyL2.geographyL1.id=:geographyId and p.date=:sampleDate")
@NamedQuery(name = "getL2WisePassiveData", query = "select p from NVPassiveDashboard p where p.geographyl4.geographyL3.geographyL2.id=:geographyId and p.date=:sampleDate")
@NamedQuery(name = "getL3WisePassiveData", query = "select p from NVPassiveDashboard p where p.geographyl4.geographyL3.id=:geographyId and p.date=:sampleDate")
@NamedQuery(name = "getL4WisePassiveData", query = "select p from NVPassiveDashboard p where p.geographyl4.id=:geographyId and p.date=:sampleDate")
@NamedQuery(name = "getAllWisePassiveData", query = "select p from NVPassiveDashboard p where p.date=:sampleDate")

@NamedQuery(name = "getL1UCWeekData", query = "select p from NVPassiveDashboard p where p.geographyl4.geographyL3.geographyL2.geographyL1.id=:geographyId and p.date between :startDate and :endDate")
@NamedQuery(name = "getL2UCWeekData", query = "select p from NVPassiveDashboard p where p.geographyl4.geographyL3.geographyL2.id=:geographyId and p.date between :startDate and :endDate")
@NamedQuery(name = "getL3UCWeekData", query = "select p from NVPassiveDashboard p where p.geographyl4.geographyL3.id=:geographyId and p.date between :startDate and :endDate")
@NamedQuery(name = "getL4UCWeekData", query = "select p from NVPassiveDashboard p where p.geographyl4.id=:geographyId and p.date between :startDate and :endDate")
@NamedQuery(name = "getUCWeekDataALL", query = "select p from NVPassiveDashboard p where p.date between :startDate and :endDate")

@FilterDef(name = NVPassiveConstants.FILTER_PAN_LEVEL_DATA)
@FilterDef(name = NVPassiveConstants.FILTER_DUPLEX_TYPE, parameters = {
		@ParamDef(name = NVPassiveConstants.DUPLEX_TYPE, type = "java.lang.String") })
@FilterDef(name = NVPassiveConstants.FILTER_TAG_TYPE, parameters = {
		@ParamDef(name = NVPassiveConstants.TAG_TYPE, type = "java.lang.String") })
@FilterDef(name = NVPassiveConstants.FILTER_DUPLEX_TAG_TYPE, parameters = {
		@ParamDef(name = NVPassiveConstants.DUPLEX_TYPE, type = "java.lang.String"),
		@ParamDef(name = NVPassiveConstants.TAG_TYPE, type = "java.lang.String") })
@FilterDef(name = NVPassiveConstants.FILTER_APPLICATION_SOURCE, parameters = {
		@ParamDef(name = NVPassiveConstants.APP_NAME, type = "java.lang.String") })

@Filter(name = NVPassiveConstants.FILTER_PAN_LEVEL_DATA, condition = " duplextype like 'ALL' and tagtype like 'ALL'")
@Filter(name = NVPassiveConstants.FILTER_DUPLEX_TYPE, condition = " duplextype= :duplexType and tagtype like 'ALL'")
@Filter(name = NVPassiveConstants.FILTER_TAG_TYPE, condition = " tagtype= :tagType and  duplextype like 'ALL'")
@Filter(name = NVPassiveConstants.FILTER_DUPLEX_TAG_TYPE, condition = " duplextype= :duplexType and tagtype= :tagType")
@Filter(name = NVPassiveConstants.FILTER_APPLICATION_SOURCE, condition = " appname=:appName")

@Entity
@Table(name = "NVPassiveDashboard")
@XmlRootElement(name = "PassiveDashboard")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NVPassiveDashboard implements Serializable {

	private static final long serialVersionUID = 7864050929895679132L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nvpassivedashboardid_pk")
	private Integer id;

	@Basic
	@Column(name = "creationtime")
	private Date date;

	@Basic
	@Column(name = "samplecount")
	private String sampleCount;

	@Basic
	@Column(name = "slot1count")
	private Integer slot1Count;

	@Basic
	@Column(name = "slot2count")
	private Integer slot2Count;

	@Basic
	@Column(name = "incomingcount")
	private Integer inComingCount;

	@Basic
	@Column(name = "outgoingCount")
	private Integer outGoingCount;

	@Basic
	@Column(name = "gpscount")
	private Integer gpsCount;

	@Basic
	@Column(name = "nongpscount")
	private Integer nonGpsCount;

	@Basic
	@Column(name = "coveragecount")
	private Integer coverageCount;

	@Basic
	@Column(name = "noncoveragecount")
	private Integer nonCoverageCount;

	@Basic
	@Column(name = "indoorcount")
	private Integer indoorCount;

	@Basic
	@Column(name = "outdoorcount")
	private Integer outdoorCount;

	@Basic
	@Column(name = "rsrp")
	private String rsrpStats;

	@Basic
	@Column(name = "sinr")
	private String sinrStats;

	@Basic
	@Column(name = "rsrq")
	private String rsrqStats;

	@Basic
	@Column(name = "os")
	private String osStats;

	@Basic
	@Column(name = "devicedistribution")
	private String deviceDistribution;

	@Basic
	@Column(name = "datacount")
	private Integer dataCount;

	@Basic
	@Column(name = "datavoicecount")
	private Integer dataVoiceCount;

	@Basic
	@Column(name = "roamingcount")
	private Integer roamingCount;

	@Basic
	@Column(name = "wificount")
	private Integer wificount;

	@Basic
	@Column(name = "ltecount")
	private Integer lteCount;

	@Basic
	@Column(name = "mifirsrp")
	private String mifiRsrp;

	@Basic
	@Column(name = "mifisinr")
	private String mifiSinr;

	@Basic
	@Column(name = "mifirsrq")
	private String mifiRsrq;

	@Basic
	@Column(name = "mifitdd")
	private Integer mifiTDD;

	@Basic
	@Column(name = "mififdd")
	private Integer mifiFDD;

	@Basic
	@Column(name = "nvcount")
	private Integer nvCount;

	@Basic
	@Column(name = "sfcount")
	private Integer sfCount;

	@Basic
	@Column(name = "fpcount")
	private Integer fpCount;

	@Basic
	@Column(name = "duplextype")
	private String duplexType;

	@Basic
	@Column(name = "tagtype")
	private String tagType;
	
	@Basic
	@Column(name = "appname")
	private String appName;


	public String getDuplexType() {
		return duplexType;
	}

	public void setDuplexType(String duplexType) {
		this.duplexType = duplexType;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public Integer getNvCount() {
		return nvCount;
	}

	public void setNvCount(Integer nvCount) {
		this.nvCount = nvCount;
	}

	public Integer getSfCount() {
		return sfCount;
	}

	public void setSfCount(Integer sfCount) {
		this.sfCount = sfCount;
	}

	public Integer getFpCount() {
		return fpCount;
	}

	public void setFpCount(Integer fpCount) {
		this.fpCount = fpCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl4id_fk", nullable = true)
	private GeographyL4 geographyl4;

	@Basic
	@Column(name = "uniqueuc")
	private Integer uniqueUC;

	@Basic
	@Column(name = "gpsuniqueuc")
	private Integer gpsUniqueUC;
	
	public Integer getGpsUniqueUC() {
		return gpsUniqueUC;
	}

	public void setGpsUniqueUC(Integer gpsUniqueUC) {
		this.gpsUniqueUC = gpsUniqueUC;
	}

	public GeographyL4 getGeographyl4() {
		return geographyl4;
	}

	public void setGeographyl4(GeographyL4 geographyl4) {
		this.geographyl4 = geographyl4;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getUniqueUC() {
		return uniqueUC;
	}

	public void setUniqueUC(Integer uniqueUC) {
		this.uniqueUC = uniqueUC;
	}

	public Integer getSlot1Count() {
		return slot1Count;
	}

	public void setSlot1Count(Integer slot1Count) {
		this.slot1Count = slot1Count;
	}

	public Integer getSlot2Count() {
		return slot2Count;
	}

	public void setSlot2Count(Integer slot2Count) {
		this.slot2Count = slot2Count;
	}

	public Integer getInComingCount() {
		return inComingCount;
	}

	public void setInComingCount(Integer inComingCount) {
		this.inComingCount = inComingCount;
	}

	public Integer getOutGoingCount() {
		return outGoingCount;
	}

	public void setOutGoingCount(Integer outGoingCount) {
		this.outGoingCount = outGoingCount;
	}

	public Integer getGpsCount() {
		return gpsCount;
	}

	public void setGpsCount(Integer gpsCount) {
		this.gpsCount = gpsCount;
	}

	public Integer getNonGpsCount() {
		return nonGpsCount;
	}

	public void setNonGpsCount(Integer nonGpsCount) {
		this.nonGpsCount = nonGpsCount;
	}

	public Integer getCoverageCount() {
		return coverageCount;
	}

	public void setCoverageCount(Integer coverageCount) {
		this.coverageCount = coverageCount;
	}

	public Integer getNonCoverageCount() {
		return nonCoverageCount;
	}

	public void setNonCoverageCount(Integer nonCoverageCount) {
		this.nonCoverageCount = nonCoverageCount;
	}

	public Integer getIndoorCount() {
		return indoorCount;
	}

	public void setIndoorCount(Integer indoorCount) {
		this.indoorCount = indoorCount;
	}

	public Integer getOutdoorCount() {
		return outdoorCount;
	}

	public void setOutdoorCount(Integer outdoorCount) {
		this.outdoorCount = outdoorCount;
	}

	public String getRsrpStats() {
		return rsrpStats;
	}

	public void setRsrpStats(String rsrpStats) {
		this.rsrpStats = rsrpStats;
	}

	public String getSinrStats() {
		return sinrStats;
	}

	public void setSinrStats(String sinrStats) {
		this.sinrStats = sinrStats;
	}

	public String getRsrqStats() {
		return rsrqStats;
	}

	public void setRsrqStats(String rsrqStats) {
		this.rsrqStats = rsrqStats;
	}

	public String getOsStats() {
		return osStats;
	}

	public void setOsStats(String osStats) {
		this.osStats = osStats;
	}

	public String getDeviceDistribution() {
		return deviceDistribution;
	}

	public void setDeviceDistribution(String deviceDistribution) {
		this.deviceDistribution = deviceDistribution;
	}

	public Integer getDataCount() {
		return dataCount;
	}

	public void setDataCount(Integer dataCount) {
		this.dataCount = dataCount;
	}

	public Integer getDataVoiceCount() {
		return dataVoiceCount;
	}

	public void setDataVoiceCount(Integer dataVoiceCount) {
		this.dataVoiceCount = dataVoiceCount;
	}

	public Integer getRoamingCount() {
		return roamingCount;
	}

	public void setRoamingCount(Integer roamingCount) {
		this.roamingCount = roamingCount;
	}

	public Integer getWificount() {
		return wificount;
	}

	public void setWificount(Integer wificount) {
		this.wificount = wificount;
	}

	public Integer getLteCount() {
		return lteCount;
	}

	public void setLteCount(Integer lteCount) {
		this.lteCount = lteCount;
	}

	public String getMifiRsrp() {
		return mifiRsrp;
	}

	public void setMifiRsrp(String mifiRsrp) {
		this.mifiRsrp = mifiRsrp;
	}

	public String getMifiSinr() {
		return mifiSinr;
	}

	public void setMifiSinr(String mifiSinr) {
		this.mifiSinr = mifiSinr;
	}

	public String getMifiRsrq() {
		return mifiRsrq;
	}

	public void setMifiRsrq(String mifiRsrq) {
		this.mifiRsrq = mifiRsrq;
	}

	public Integer getMifiTDD() {
		return mifiTDD;
	}

	public void setMifiTDD(Integer mifiTDD) {
		this.mifiTDD = mifiTDD;
	}

	public Integer getMifiFDD() {
		return mifiFDD;
	}

	public void setMifiFDD(Integer mifiFDD) {
		this.mifiFDD = mifiFDD;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getSampleCount() {
		return sampleCount;
	}

	public void setSampleCount(String sampleCount) {
		this.sampleCount = sampleCount;
	}

	@Override
	public String toString() {
		return "NVPassiveDashboard [id=" + id + ", date=" + date + ", sampleCount=" + sampleCount + ", slot1Count="
				+ slot1Count + ", slot2Count=" + slot2Count + ", inComingCount=" + inComingCount + ", outGoingCount="
				+ outGoingCount + ", gpsCount=" + gpsCount + ", nonGpsCount=" + nonGpsCount + ", coverageCount="
				+ coverageCount + ", nonCoverageCount=" + nonCoverageCount + ", indoorCount=" + indoorCount
				+ ", outdoorCount=" + outdoorCount + ", rsrpStats=" + rsrpStats + ", sinrStats=" + sinrStats
				+ ", rsrqStats=" + rsrqStats + ", osStats=" + osStats + ", deviceDistribution=" + deviceDistribution
				+ ", dataCount=" + dataCount + ", dataVoiceCount=" + dataVoiceCount + ", roamingCount=" + roamingCount
				+ ", wificount=" + wificount + ", lteCount=" + lteCount + ", mifiRsrp=" + mifiRsrp + ", mifiSinr="
				+ mifiSinr + ", mifiRsrq=" + mifiRsrq + ", mifiTDD=" + mifiTDD + ", mifiFDD=" + mifiFDD + ", nvCount="
				+ nvCount + ", sfCount=" + sfCount + ", fpCount=" + fpCount + ", duplexType=" + duplexType
				+ ", tagType=" + tagType + ", appName=" + appName + ", geographyl4=" + geographyl4 + ", uniqueUC="
				+ uniqueUC + ", gpsUniqueUC=" + gpsUniqueUC + "]";
	}
}
