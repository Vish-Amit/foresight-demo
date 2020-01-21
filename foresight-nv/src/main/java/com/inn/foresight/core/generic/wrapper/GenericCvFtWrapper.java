package com.inn.foresight.core.generic.wrapper;

/**
 * The Class GenericClusterViewFtWrapper.
 *
 * @author ist
 */
public class GenericCvFtWrapper {

	/** The geography name. */
	public String geographyName;

	/** The latitude. */
	public Double latitude;

	/** The Longitude. */
	public Double longitude;
	
	
	public String displayName;

	/** The kpi name. */
	public String kpiName;

	/** The avg value. */
	public Long sum;

	/** The avg value. */
	public Double avgvalue;

	public Double totalevents;

	public Double validevents;

	/** The range 0. */
	public Long range0;

	/** The range 1. */
	public Long range1;

	/** The range 2. */
	public Long range2;

	public String band;

	public String date;
	
	public String technology;
	
	public String operatorName;

	public GenericCvFtWrapper(String geographyName, Double latitude, Double Longitude,
			String displayName,
			Double totalevents,
			Double validevents,Double avgvalue, Long range0, Long range1, Long range2,String technology,String opeatorName) {
		this.geographyName = geographyName;
		this.latitude = latitude;
		this.longitude = Longitude;
		this.displayName=displayName;
		this.totalevents = totalevents;
		this.validevents = validevents;
		this.range0 = range0;
		this.range1 = range1;
		this.range2 = range2;
		this.avgvalue = avgvalue;
		this.technology = technology;
		this.operatorName=opeatorName;
	}

	public GenericCvFtWrapper(String geographyName, Double latitude, Double Longitude,
			Double totalevents,
			Double validevents,Double avgvalue, Long range0, Long range1, Long range2,String technology,String opeatorName) {
		this.geographyName = geographyName;
		this.latitude = latitude;
		this.longitude = Longitude;
		this.totalevents = totalevents;
		this.validevents = validevents;
		this.range0 = range0;
		this.range1 = range1;
		this.range2 = range2;
		this.avgvalue = avgvalue;
		this.technology = technology;
		this.operatorName=opeatorName;
	}
	public String getGeographyName() {
		return geographyName;
	}

	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
	}

	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public Long getSum() {
		return sum;
	}

	public void setSum(Long sum) {
		this.sum = sum;
	}

	public Double getAvgvalue() {
		return avgvalue;
	}

	public void setAvgvalue(Double avgvalue) {
		this.avgvalue = avgvalue;
	}

	public Double getTotalevents() {
		return totalevents;
	}

	public void setTotalevents(Double totalevents) {
		this.totalevents = totalevents;
	}

	public Double getValidevents() {
		return validevents;
	}

	public void setValidevents(Double validevents) {
		this.validevents = validevents;
	}

	public Long getRange0() {
		return range0;
	}

	public void setRange0(Long range0) {
		this.range0 = range0;
	}

	public Long getRange1() {
		return range1;
	}

	public void setRange1(Long range1) {
		this.range1 = range1;
	}

	public Long getRange2() {
		return range2;
	}

	public void setRange2(Long range2) {
		this.range2 = range2;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getOpeatorName() {
		return operatorName;
	}

	public void setOpeatorName(String opeatorName) {
		this.operatorName = opeatorName;
	}

	@Override
	public String toString() {
		return "GenericCvFtWrapper [geographyName=" + geographyName + ", latitude=" + latitude + ", longitude="
				+ longitude + ", kpiName=" + kpiName + ", sum=" + sum + ", avgvalue=" + avgvalue + ", totalevents="
				+ totalevents + ", validevents=" + validevents + ", range0=" + range0 + ", range1=" + range1
				+ ", range2=" + range2 + ", band=" + band + ", date=" + date + ", technology=" + technology
				+ ", opeatorName=" + operatorName + "]";
	}

}
