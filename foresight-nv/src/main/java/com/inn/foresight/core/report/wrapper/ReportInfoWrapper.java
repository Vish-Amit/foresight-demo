package com.inn.foresight.core.report.wrapper;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.inn.foresight.core.report.TemplateType;

// TODO: Auto-generated Javadoc
/**
 * The Class ReportInfoWrapper.
 */
public class ReportInfoWrapper {


	/** The target level. */
	private String targetLevel;
	
	/** The level. */
	private String level;
	
	/** The level id. */
	private Integer levelId;
	
	/** The week no. */
	private String weekNo;
	
	/** The start time. */
	private Long startTime;
	
	/** The end time. */
	private Long endTime;
	
	/** The start date. */
	private Date startDate;
	
	/** The end date. */
	private Date endDate;
	
	/** The target report. */
	private TemplateType targetReport;
	
	/** The report measure. */
	private String reportMeasure;
	
	/** The cluster select. */
	private Integer[] clusterSelect;
	
	/** The circle select. */
	private Integer[] circleSelect;
	
	/** The zone select. */
	private Integer[] zoneSelect;
	
	/** The city select. */
	private Integer[] citySelect;
	
	/** The cycle number. */
	private Integer cycleNumber;
	
	/** The test area. */
	private String testArea;
	
	/** The problem type. */
	private String problemType;
	
	/** The problem sub type. */
	private String problemSubType;

	/** The operator. */
	private String operator;
	
	/** The technology. */
	private String technology;
	
	/** The handset make. */
	private String handsetMake;
	
	/** The os. */
	private String os;
	
	/** The imei. */
	private String imei;
	
	/** The imsi. */
	private String imsi;
	
	/** The band. */
	private String band;
	
	/** The network element. */
	private String networkElement;
	
	/** The vendor. */
	private String vendor;
	
	/** The report widget id. */
	private Integer reportWidgetId;

	/** The target area. */
	private String targetArea;
	
	/** The report creation time. */
	private Date reportCreationTime;
	
	/** The geo type. */
	private String geoType;
	
	/** The geo value. */
	private String geoValue;
	
	/** The e node B select. */
	private String[] eNodeBSelect;
	
	/** The target E node B select. */
	private String[] targetENodeBSelect;
	
	/** The geo level. */
	private String geoLevel;
	
	/** The date selected. */
	private Long dateSelected;
	
	/** The source software version. */
	private String sourceSoftwareVersion;
	
	/** The start date str. */
	private String startDateStr;
	
	/** The end date str. */
	private String endDateStr;

	/** The cluster name select. */
	private String[] clusterNameSelect;
	
	/** The circle name select. */
	private String[] circleNameSelect;
	
	/** The zone name select. */
	private String[] zoneNameSelect;
	
	/** The city name select. */
	private String[] cityNameSelect;
	
	/** The is outdoor. */
	private Boolean isOutdoor;
	
	/** The is indoor. */
	private Boolean isIndoor;
	
	/** The coverage layer type. */
	private String coverageLayerType;
	
	/** The year select. */
	private String yearSelect;
	
	/** The month select. */
	private String monthSelect;
	
	/** The fort night select. */
	private String fortNightSelect;
	
	/** The all zone selected. */
	private boolean allZoneSelected;
	
	/** The ne type. */
	private String[] neType;

	/** The phase. */
	private String phase;
	
	/** The status. */
	private String status;
	
	/** The xmlpld. */
	private String[] xmlpld;
	
	/** The ran site type. */
	private String ranSiteType;
	
	/** The direction. */
	private String[] direction;
	
	/** The parameter. */
	private String parameter;
	
	/** The command. */
	private String command;
	
	/** The commands. */
	private String[] commands;
	
	/** The identifires. */
	private String[] identifires;
	
	/** The hour. */
	private String hour;
	
	/** The compare file id. */
	private Integer compareFileId;

	/** The ageing. */
	private String[] ageing;
	
	/** The tagging. */
	private String tagging;

	/** The is daily. */
	private Boolean isDaily;
	
	/** The is hourly. */
	private Boolean isHourly;
	
	/** The is subscribed. */
	private Boolean isSubscribed = false;
	
	/** The domain. */
	private String domain;
	
	/** The geography L 1. */
	private Integer geographyL1;
	
	/** The geography L 2. */
	private Integer geographyL2;
	
	/** The geography L 3. */
	private Integer geographyL3;
	
	/** The geography L 4. */
	private Integer geographyL4;

	/** The ne name. */
	private List<String> neName;
	
	private String generationType;
	
	private String frequency;
	
	/**
	 * Gets the geography L 1.
	 *
	 * @return the geography L 1
	 */
	public Integer getGeographyL1() {
		return geographyL1;
	}

	/**
	 * Sets the geography L 1.
	 *
	 * @param geographyL1 the new geography L 1
	 */
	public void setGeographyL1(Integer geographyL1) {
		this.geographyL1 = geographyL1;
	}

	/**
	 * Gets the geography L 2.
	 *
	 * @return the geography L 2
	 */
	public Integer getGeographyL2() {
		return geographyL2;
	}

	/**
	 * Sets the geography L 2.
	 *
	 * @param geographyL2 the new geography L 2
	 */
	public void setGeographyL2(Integer geographyL2) {
		this.geographyL2 = geographyL2;
	}

	/**
	 * Gets the geography L 3.
	 *
	 * @return the geography L 3
	 */
	public Integer getGeographyL3() {
		return geographyL3;
	}

	/**
	 * Sets the geography L 3.
	 *
	 * @param geographyL3 the new geography L 3
	 */
	public void setGeographyL3(Integer geographyL3) {
		this.geographyL3 = geographyL3;
	}

	/**
	 * Gets the geography L 4.
	 *
	 * @return the geography L 4
	 */
	public Integer getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4 the new geography L 4
	 */
	public void setGeographyL4(Integer geographyL4) {
		this.geographyL4 = geographyL4;
	}

	
	/**
	 * Gets the ne name.
	 *
	 * @return the ne name
	 */
	public List<String> getNeName() {
		return neName;
	}

	/**
	 * Sets the ne name.
	 *
	 * @param neName the new ne name
	 */
	public void setNeName(List<String> neName) {
		this.neName = neName;
	}

	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 *
	 * @param domain the new domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Gets the checks if is daily.
	 *
	 * @return the checks if is daily
	 */
	public Boolean getIsDaily() {
		return isDaily;
	}

	/**
	 * Sets the checks if is daily.
	 *
	 * @param isDaily the new checks if is daily
	 */
	public void setIsDaily(Boolean isDaily) {
		this.isDaily = isDaily;
	}

	/**
	 * Gets the checks if is hourly.
	 *
	 * @return the checks if is hourly
	 */
	public Boolean getIsHourly() {
		return isHourly;
	}

	/**
	 * Sets the checks if is hourly.
	 *
	 * @param isHourly the new checks if is hourly
	 */
	public void setIsHourly(Boolean isHourly) {
		this.isHourly = isHourly;
	}

	/**
	 * Gets the checks if is subscribed.
	 *
	 * @return the checks if is subscribed
	 */
	public Boolean getIsSubscribed() {
		return isSubscribed;
	}

	/**
	 * Sets the checks if is subscribed.
	 *
	 * @param isSubscribed the new checks if is subscribed
	 */
	public void setIsSubscribed(Boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
	}

	/**
	 * Gets the ageing.
	 *
	 * @return the ageing
	 */
	public String[] getAgeing() {
		return ageing;
	}

	/**
	 * Sets the ageing.
	 *
	 * @param ageing the new ageing
	 */
	public void setAgeing(String[] ageing) {
		this.ageing = ageing;
	}

	/**
	 * Gets the tagging.
	 *
	 * @return the tagging
	 */
	public String getTagging() {
		return tagging;
	}

	/**
	 * Sets the tagging.
	 *
	 * @param tagging the new tagging
	 */
	public void setTagging(String tagging) {
		this.tagging = tagging;
	}

	/**
	 * Gets the xmlpld.
	 *
	 * @return the xmlpld
	 */
	public String[] getXmlpld() {
		return xmlpld;
	}
	
	/**
	 * Gets the vendor.
	 *
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * Sets the vendor.
	 *
	 * @param vendor the new vendor
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * Sets the xmlpld.
	 *
	 * @param xmlpld the new xmlpld
	 */
	public void setXmlpld(String[] xmlpld) {
		this.xmlpld = xmlpld;
	}

	/**
	 * Gets the ne type.
	 *
	 * @return the ne type
	 */
	public String[] getNeType() {
		return neType;
	}

	/**
	 * Sets the ne type.
	 *
	 * @param neType the new ne type
	 */
	public void setNeType(String[] neType) {
		this.neType = neType;
	}

	/**
	 * Gets the imei.
	 *
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * Gets the date selected.
	 *
	 * @return the date selected
	 */
	public Long getDateSelected() {
		return dateSelected;
	}

	/**
	 * Sets the date selected.
	 *
	 * @param dateSelected the new date selected
	 */
	public void setDateSelected(Long dateSelected) {
		this.dateSelected = dateSelected;
	}

	/**
	 * Sets the imei.
	 *
	 * @param imei the new imei
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * Gets the problem type.
	 *
	 * @return the problem type
	 */
	public String getProblemType() {
		return problemType;
	}

	/**
	 * Sets the problem type.
	 *
	 * @param problemType the new problem type
	 */
	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}

	/**
	 * Gets the test area.
	 *
	 * @return the test area
	 */
	public String getTestArea() {
		return testArea;
	}

	/**
	 * Sets the test area.
	 *
	 * @param testArea the new test area
	 */
	public void setTestArea(String testArea) {
		this.testArea = testArea;
	}

	/**
	 * Gets the problem sub type.
	 *
	 * @return the problem sub type
	 */
	public String getProblemSubType() {
		return problemSubType;
	}

	/**
	 * Sets the problem sub type.
	 *
	 * @param problemSubType the new problem sub type
	 */
	public void setProblemSubType(String problemSubType) {
		this.problemSubType = problemSubType;
	}

	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * Sets the level.
	 *
	 * @param level the new level
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * Gets the level id.
	 *
	 * @return the level id
	 */
	public Integer getLevelId() {
		return levelId;
	}

	/**
	 * Sets the level id.
	 *
	 * @param levelId the new level id
	 */
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	/**
	 * Gets the week no.
	 *
	 * @return the week no
	 */
	public String getWeekNo() {
		return weekNo;
	}

	/**
	 * Sets the week no.
	 *
	 * @param weekNo the new week no
	 */
	public void setWeekNo(String weekNo) {
		this.weekNo = weekNo;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public Long getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime the new start time
	 */
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public Long getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime the new end time
	 */
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date.
	 *
	 * @param startDate the new start date
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate the new end date
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Gets the target report.
	 *
	 * @return the target report
	 */
	public TemplateType getTargetReport() {
		return targetReport;
	}

	/**
	 * Sets the target report.
	 *
	 * @param targetReport the new target report
	 */
	public void setTargetReport(TemplateType targetReport) {
		this.targetReport = targetReport;
	}

	/**
	 * Gets the report measure.
	 *
	 * @return the report measure
	 */
	public String getReportMeasure() {
		return reportMeasure;
	}

	/**
	 * Sets the report measure.
	 *
	 * @param reportMeasure the new report measure
	 */
	public void setReportMeasure(String reportMeasure) {
		this.reportMeasure = reportMeasure;
	}

	/**
	 * Gets the cluster select.
	 *
	 * @return the cluster select
	 */
	public Integer[] getClusterSelect() {
		return clusterSelect;
	}

	/**
	 * Sets the cluster select.
	 *
	 * @param clusterSelect the new cluster select
	 */
	public void setClusterSelect(Integer[] clusterSelect) {
		this.clusterSelect = clusterSelect;
	}

	/**
	 * Gets the circle select.
	 *
	 * @return the circle select
	 */
	public Integer[] getCircleSelect() {
		return circleSelect;
	}

	/**
	 * Sets the circle select.
	 *
	 * @param circleSelect the new circle select
	 */
	public void setCircleSelect(Integer[] circleSelect) {
		this.circleSelect = circleSelect;
	}

	/**
	 * Gets the zone select.
	 *
	 * @return the zone select
	 */
	public Integer[] getZoneSelect() {
		return zoneSelect;
	}

	/**
	 * Sets the zone select.
	 *
	 * @param zoneSelect the new zone select
	 */
	public void setZoneSelect(Integer[] zoneSelect) {
		this.zoneSelect = zoneSelect;
	}

	/**
	 * Gets the city select.
	 *
	 * @return the city select
	 */
	public Integer[] getCitySelect() {
		return citySelect;
	}

	/**
	 * Sets the city select.
	 *
	 * @param citySelect the new city select
	 */
	public void setCitySelect(Integer[] citySelect) {
		this.citySelect = citySelect;
	}

	/**
	 * Gets the cycle number.
	 *
	 * @return the cycle number
	 */
	public Integer getCycleNumber() {
		return cycleNumber;
	}

	/**
	 * Sets the cycle number.
	 *
	 * @param cycleNumber the new cycle number
	 */
	public void setCycleNumber(Integer cycleNumber) {
		this.cycleNumber = cycleNumber;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 *
	 * @param operator the new operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 *
	 * @param technology the new technology
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * Gets the handset make.
	 *
	 * @return the handset make
	 */
	public String getHandsetMake() {
		return handsetMake;
	}

	/**
	 * Sets the handset make.
	 *
	 * @param handsetMake the new handset make
	 */
	public void setHandsetMake(String handsetMake) {
		this.handsetMake = handsetMake;
	}

	/**
	 * Gets the os.
	 *
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * Sets the os.
	 *
	 * @param os the new os
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * Gets the band.
	 *
	 * @return the band
	 */
	public String getBand() {
		return band;
	}

	/**
	 * Sets the band.
	 *
	 * @param band the new band
	 */
	public void setBand(String band) {
		this.band = band;
	}

	/**
	 * Gets the network element.
	 *
	 * @return the network element
	 */
	public String getNetworkElement() {
		return networkElement;
	}

	/**
	 * Sets the network element.
	 *
	 * @param networkElement the new network element
	 */
	public void setNetworkElement(String networkElement) {
		this.networkElement = networkElement;
	}

	/**
	 * Gets the vender.
	 *
	 * @return the vender
	 */
	public String getVender() {
		return vendor;
	}

	/**
	 * Sets the vender.
	 *
	 * @param vender the new vender
	 */
	public void setVender(String vender) {
		this.vendor = vender;
	}

	/**
	 * Gets the report widget id.
	 *
	 * @return the report widget id
	 */
	public Integer getReportWidgetId() {
		return reportWidgetId;
	}

	/**
	 * Sets the report widget id.
	 *
	 * @param reportWidgetId the new report widget id
	 */
	public void setReportWidgetId(Integer reportWidgetId) {
		this.reportWidgetId = reportWidgetId;
	}

	/**
	 * Gets the target area.
	 *
	 * @return the target area
	 */
	public String getTargetArea() {
		return targetArea;
	}

	/**
	 * Sets the target area.
	 *
	 * @param targetArea the new target area
	 */
	public void setTargetArea(String targetArea) {
		this.targetArea = targetArea;
	}

	/**
	 * Gets the geo type.
	 *
	 * @return the geo type
	 */
	public String getGeoType() {
		return geoType;
	}

	/**
	 * Sets the geo type.
	 *
	 * @param geoType the new geo type
	 */
	public void setGeoType(String geoType) {
		this.geoType = geoType;
	}

	/**
	 * Gets the geo value.
	 *
	 * @return the geo value
	 */
	public String getGeoValue() {
		return geoValue;
	}

	/**
	 * Sets the geo value.
	 *
	 * @param geoValue the new geo value
	 */
	public void setGeoValue(String geoValue) {
		this.geoValue = geoValue;
	}

	/**
	 * Gets the e node B select.
	 *
	 * @return the e node B select
	 */
	public String[] geteNodeBSelect() {
		return eNodeBSelect;
	}

	/**
	 * Sets the e node B select.
	 *
	 * @param eNodeBSelect the new e node B select
	 */
	public void seteNodeBSelect(String[] eNodeBSelect) {
		this.eNodeBSelect = eNodeBSelect;
	}

	/**
	 * Gets the checks if is outdoor.
	 *
	 * @return the checks if is outdoor
	 */
	public Boolean getIsOutdoor() {
		return isOutdoor;
	}

	/**
	 * Sets the checks if is outdoor.
	 *
	 * @param isOutdoor the new checks if is outdoor
	 */
	public void setIsOutdoor(Boolean isOutdoor) {
		this.isOutdoor = isOutdoor;
	}

	/**
	 * Gets the checks if is indoor.
	 *
	 * @return the checks if is indoor
	 */
	public Boolean getIsIndoor() {
		return isIndoor;
	}

	/**
	 * Sets the checks if is indoor.
	 *
	 * @param isIndoor the new checks if is indoor
	 */
	public void setIsIndoor(Boolean isIndoor) {
		this.isIndoor = isIndoor;
	}

	/**
	 * To configuration json.
	 *
	 * @return the string
	 */
	public String toConfigurationJson() {
		try {
			Gson gson = new Gson();
			return gson.toJson(this);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Gets the report creation time.
	 *
	 * @return the report creation time
	 */
	public Date getReportCreationTime() {
		return reportCreationTime;
	}

	/**
	 * Sets the report creation time.
	 *
	 * @param reportCreationTime the new report creation time
	 */
	public void setReportCreationTime(Date reportCreationTime) {
		this.reportCreationTime = reportCreationTime;
	}

	/**
	 * Gets the geo level.
	 *
	 * @return the geo level
	 */
	public String getGeoLevel() {
		return geoLevel;
	}

	/**
	 * Sets the geo level.
	 *
	 * @param geoLevel the new geo level
	 */
	public void setGeoLevel(String geoLevel) {
		this.geoLevel = geoLevel;
	}

	/**
	 * Gets the start date str.
	 *
	 * @return the start date str
	 */
	public String getStartDateStr() {
		return startDateStr;
	}

	/**
	 * Sets the start date str.
	 *
	 * @param startDateStr the new start date str
	 */
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	/**
	 * Gets the end date str.
	 *
	 * @return the end date str
	 */
	public String getEndDateStr() {
		return endDateStr;
	}

	/**
	 * Sets the end date str.
	 *
	 * @param endDateStr the new end date str
	 */
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	/**
	 * Gets the cluster name select.
	 *
	 * @return the cluster name select
	 */
	public String[] getClusterNameSelect() {
		return clusterNameSelect;
	}

	/**
	 * Sets the cluster name select.
	 *
	 * @param clusterNameSelect the new cluster name select
	 */
	public void setClusterNameSelect(String[] clusterNameSelect) {
		this.clusterNameSelect = clusterNameSelect;
	}

	/**
	 * Gets the year select.
	 *
	 * @return the year select
	 */
	public String getYearSelect() {
		return yearSelect;
	}

	/**
	 * Sets the year select.
	 *
	 * @param yearSelect the new year select
	 */
	public void setYearSelect(String yearSelect) {
		this.yearSelect = yearSelect;
	}

	/**
	 * Gets the month select.
	 *
	 * @return the month select
	 */
	public String getMonthSelect() {
		return monthSelect;
	}

	/**
	 * Sets the month select.
	 *
	 * @param monthSelect the new month select
	 */
	public void setMonthSelect(String monthSelect) {
		this.monthSelect = monthSelect;
	}

	/**
	 * Gets the fort night select.
	 *
	 * @return the fort night select
	 */
	public String getFortNightSelect() {
		return fortNightSelect;
	}

	/**
	 * Sets the fort night select.
	 *
	 * @param fortNightSelect the new fort night select
	 */
	public void setFortNightSelect(String fortNightSelect) {
		this.fortNightSelect = fortNightSelect;
	}

	/**
	 * Gets the all zone selected.
	 *
	 * @return the all zone selected
	 */
	public boolean getAllZoneSelected() {
		return allZoneSelected;
	}

	/**
	 * Sets the all zone selected.
	 *
	 * @param allZoneSelected the new all zone selected
	 */
	public void setAllZoneSelected(boolean allZoneSelected) {
		this.allZoneSelected = allZoneSelected;
	}

	/**
	 * Gets the circle name select.
	 *
	 * @return the circle name select
	 */
	public String[] getCircleNameSelect() {
		return circleNameSelect;
	}

	/**
	 * Sets the circle name select.
	 *
	 * @param circleNameSelect the new circle name select
	 */
	public void setCircleNameSelect(String[] circleNameSelect) {
		this.circleNameSelect = circleNameSelect;
	}

	/**
	 * Gets the zone name select.
	 *
	 * @return the zone name select
	 */
	public String[] getZoneNameSelect() {
		return zoneNameSelect;
	}

	/**
	 * Sets the zone name select.
	 *
	 * @param zoneNameSelect the new zone name select
	 */
	public void setZoneNameSelect(String[] zoneNameSelect) {
		this.zoneNameSelect = zoneNameSelect;
	}

	/**
	 * Gets the city name select.
	 *
	 * @return the city name select
	 */
	public String[] getCityNameSelect() {
		return cityNameSelect;
	}

	/**
	 * Sets the city name select.
	 *
	 * @param cityNameSelect the new city name select
	 */
	public void setCityNameSelect(String[] cityNameSelect) {
		this.cityNameSelect = cityNameSelect;
	}

	/**
	 * Gets the coverage layer type.
	 *
	 * @return the coverage layer type
	 */
	public String getCoverageLayerType() {
		return coverageLayerType;
	}

	/**
	 * Sets the coverage layer type.
	 *
	 * @param coverageLayerType the new coverage layer type
	 */
	public void setCoverageLayerType(String coverageLayerType) {
		this.coverageLayerType = coverageLayerType;
	}

	/**
	 * Gets the phase.
	 *
	 * @return the phase
	 */
	public String getPhase() {
		return phase;
	}

	/**
	 * Sets the phase.
	 *
	 * @param phase the new phase
	 */
	public void setPhase(String phase) {
		this.phase = phase;
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
	 * Gets the target level.
	 *
	 * @return the target level
	 */
	public String getTargetLevel() {
		return targetLevel;
	}

	/**
	 * Sets the target level.
	 *
	 * @param targetLevel the new target level
	 */
	public void setTargetLevel(String targetLevel) {
		this.targetLevel = targetLevel;
	}

	/**
	 * Gets the ran site type.
	 *
	 * @return the ran site type
	 */
	public String getRanSiteType() {
		return ranSiteType;
	}

	/**
	 * Sets the ran site type.
	 *
	 * @param siteType the new ran site type
	 */
	public void setRanSiteType(String siteType) {
		this.ranSiteType = siteType;
	}

	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public String[] getDirection() {
		return direction;
	}

	/**
	 * Sets the direction.
	 *
	 * @param direction the new direction
	 */
	public void setDirection(String[] direction) {
		this.direction = direction;
	}

	/**
	 * Gets the target E node B select.
	 *
	 * @return the target E node B select
	 */
	public String[] getTargetENodeBSelect() {
		return targetENodeBSelect;
	}

	/**
	 * Sets the target E node B select.
	 *
	 * @param targetENodeBSelect the new target E node B select
	 */
	public void setTargetENodeBSelect(String[] targetENodeBSelect) {
		this.targetENodeBSelect = targetENodeBSelect;
	}

	/**
	 * Gets the imsi.
	 *
	 * @return the imsi
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * Sets the imsi.
	 *
	 * @param imsi the new imsi
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * Gets the parameter.
	 *
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * Sets the parameter.
	 *
	 * @param parameter the new parameter
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Gets the command.
	 *
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Sets the command.
	 *
	 * @param command the new command
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * Gets the identifires.
	 *
	 * @return the identifires
	 */
	public String[] getIdentifires() {
		return identifires;
	}

	/**
	 * Sets the identifires.
	 *
	 * @param identifires the new identifires
	 */
	public void setIdentifires(String[] identifires) {
		this.identifires = identifires;
	}

	/**
	 * Gets the commands.
	 *
	 * @return the commands
	 */
	public String[] getCommands() {
		return commands;
	}

	/**
	 * Sets the commands.
	 *
	 * @param commands the new commands
	 */
	public void setCommands(String[] commands) {
		this.commands = commands;
	}

	/**
	 * Gets the source software version.
	 *
	 * @return the source software version
	 */
	public String getSourceSoftwareVersion() {
		return sourceSoftwareVersion;
	}

	/**
	 * Sets the source software version.
	 *
	 * @param sourceSoftwareVersion the new source software version
	 */
	public void setSourceSoftwareVersion(String sourceSoftwareVersion) {
		this.sourceSoftwareVersion = sourceSoftwareVersion;
	}

	/**
	 * Gets the compare file id.
	 *
	 * @return the compare file id
	 */
	public Integer getCompareFileId() {
		return compareFileId;
	}

	/**
	 * Sets the compare file id.
	 *
	 * @param compareFileId the new compare file id
	 */
	public void setCompareFileId(Integer compareFileId) {
		this.compareFileId = compareFileId;
	}

	/**
	 * Gets the hour.
	 *
	 * @return the hour
	 */
	public String getHour() {
		return hour;
	}

	/**
	 * Sets the hour.
	 *
	 * @param hour the new hour
	 */
	public void setHour(String hour) {
		this.hour = hour;
	}
	
	public String getGenerationType() {
		return generationType;
	}

	public void setGenerationType(String generationType) {
		this.generationType = generationType;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportInfoWrapper [targetLevel=");
		builder.append(targetLevel);
		builder.append(", level=");
		builder.append(level);
		builder.append(", levelId=");
		builder.append(levelId);
		builder.append(", weekNo=");
		builder.append(weekNo);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", targetReport=");
		builder.append(targetReport);
		builder.append(", reportMeasure=");
		builder.append(reportMeasure);
		builder.append(", clusterSelect=");
		builder.append(Arrays.toString(clusterSelect));
		builder.append(", circleSelect=");
		builder.append(Arrays.toString(circleSelect));
		builder.append(", zoneSelect=");
		builder.append(Arrays.toString(zoneSelect));
		builder.append(", citySelect=");
		builder.append(Arrays.toString(citySelect));
		builder.append(", cycleNumber=");
		builder.append(cycleNumber);
		builder.append(", testArea=");
		builder.append(testArea);
		builder.append(", problemType=");
		builder.append(problemType);
		builder.append(", problemSubType=");
		builder.append(problemSubType);
		builder.append(", operator=");
		builder.append(operator);
		builder.append(", technology=");
		builder.append(technology);
		builder.append(", handsetMake=");
		builder.append(handsetMake);
		builder.append(", os=");
		builder.append(os);
		builder.append(", imei=");
		builder.append(imei);
		builder.append(", imsi=");
		builder.append(imsi);
		builder.append(", band=");
		builder.append(band);
		builder.append(", networkElement=");
		builder.append(networkElement);
		builder.append(", vendor=");
		builder.append(vendor);
		builder.append(", reportWidgetId=");
		builder.append(reportWidgetId);
		builder.append(", targetArea=");
		builder.append(targetArea);
		builder.append(", reportCreationTime=");
		builder.append(reportCreationTime);
		builder.append(", geoType=");
		builder.append(geoType);
		builder.append(", geoValue=");
		builder.append(geoValue);
		builder.append(", eNodeBSelect=");
		builder.append(Arrays.toString(eNodeBSelect));
		builder.append(", targetENodeBSelect=");
		builder.append(Arrays.toString(targetENodeBSelect));
		builder.append(", geoLevel=");
		builder.append(geoLevel);
		builder.append(", dateSelected=");
		builder.append(dateSelected);
		builder.append(", sourceSoftwareVersion=");
		builder.append(sourceSoftwareVersion);
		builder.append(", startDateStr=");
		builder.append(startDateStr);
		builder.append(", endDateStr=");
		builder.append(endDateStr);
		builder.append(", clusterNameSelect=");
		builder.append(Arrays.toString(clusterNameSelect));
		builder.append(", circleNameSelect=");
		builder.append(Arrays.toString(circleNameSelect));
		builder.append(", zoneNameSelect=");
		builder.append(Arrays.toString(zoneNameSelect));
		builder.append(", cityNameSelect=");
		builder.append(Arrays.toString(cityNameSelect));
		builder.append(", isOutdoor=");
		builder.append(isOutdoor);
		builder.append(", isIndoor=");
		builder.append(isIndoor);
		builder.append(", coverageLayerType=");
		builder.append(coverageLayerType);
		builder.append(", yearSelect=");
		builder.append(yearSelect);
		builder.append(", monthSelect=");
		builder.append(monthSelect);
		builder.append(", fortNightSelect=");
		builder.append(fortNightSelect);
		builder.append(", allZoneSelected=");
		builder.append(allZoneSelected);
		builder.append(", neType=");
		builder.append(Arrays.toString(neType));
		builder.append(", phase=");
		builder.append(phase);
		builder.append(", status=");
		builder.append(status);
		builder.append(", xmlpld=");
		builder.append(Arrays.toString(xmlpld));
		builder.append(", ranSiteType=");
		builder.append(ranSiteType);
		builder.append(", direction=");
		builder.append(Arrays.toString(direction));
		builder.append(", parameter=");
		builder.append(parameter);
		builder.append(", command=");
		builder.append(command);
		builder.append(", commands=");
		builder.append(Arrays.toString(commands));
		builder.append(", identifires=");
		builder.append(Arrays.toString(identifires));
		builder.append(", hour=");
		builder.append(hour);
		builder.append(", compareFileId=");
		builder.append(compareFileId);
		builder.append(", ageing=");
		builder.append(Arrays.toString(ageing));
		builder.append(", tagging=");
		builder.append(tagging);
		builder.append(", isDaily=");
		builder.append(isDaily);
		builder.append(", isHourly=");
		builder.append(isHourly);
		builder.append(", isSubscribed=");
		builder.append(isSubscribed);
		builder.append(", domain=");
		builder.append(domain);
		builder.append(", geographyL1=");
		builder.append(geographyL1);
		builder.append(", geographyL2=");
		builder.append(geographyL2);
		builder.append(", geographyL3=");
		builder.append(geographyL3);
		builder.append(", geographyL4=");
		builder.append(geographyL4);
		builder.append(", neName=");
		builder.append(neName);
		builder.append(", generationType=");
		builder.append(generationType);
		builder.append(", frequency=");
		builder.append(frequency);
		builder.append("]");
		return builder.toString();
	}
}