package com.inn.foresight.module.nv.core.maplayer.utils;

import java.util.List;

/* The Class LegendWrapper. */

	/** The legend. */
public class LegendWrapper {

	private Integer id;

	private String legendName;

	private String kpiName;
	
	private String appliedTo;

	private Double opacity;
	
	private String colorCode;
	
	private Double minValue;
	
	private Double maxValue;
	
	private Integer creatorId;
	
	private Boolean defaultKpi;

	private List<LegendWrapper> legendRangeList;
	
	private String configuration;
	
	public LegendWrapper() {
		super();
	}


	public LegendWrapper(Integer id, String kpiName, Double minValue , Double maxValue) {
		super();
		this.id = id;
		this.kpiName = kpiName;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}


	/**
	 * Instantiates a new legends ranges.
	 *
	 * @param id the id
	 * @param lengend the lengend
	 * @param colorCode the color code
	 * @param minValue the min value
	 * @param maxValue the max value
	 */
	public LegendWrapper(Integer id, String kpiName,Double opacity,String legendName, String appliedTo,String colorCode, Double minValue, Double maxValue,String configuration) {
		super();
		this.id = id;
		this.kpiName=kpiName;
		this.opacity=opacity;
		this.legendName=legendName;
		this.appliedTo=appliedTo;
		this.colorCode = colorCode;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.configuration=configuration;
	}
	
	/**
	 * Instantiates a new legends.
	 *
	 * @param id the id
	 * @param legendName the legend name
	 * @param kpiName the kpi name
	 */
	public LegendWrapper(Integer id, String legendName, String kpiName) {
		super();
		this.id = id;
		this.legendName = legendName;
		this.kpiName = kpiName;
	}
	
	/**
	 * Instantiates a new legends.
	 *
	 * @param id the id
	 * @param legendName the legend name
	 * @param kpiName the kpi name
	 * @param creatorId the creator id
	 */
	public LegendWrapper(Integer id, String legendName, String kpiName,Integer creatorId,Boolean defaultKpi) {
		super();
		this.id = id;
		this.legendName = legendName;
		this.kpiName = kpiName;
		this.creatorId=creatorId;
		this.defaultKpi=defaultKpi;
	}

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getColorCode() {
		return colorCode;
	}


	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}


	public Double getMinValue() {
		return minValue;
	}


	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}


	public Double getMaxValue() {
		return maxValue;
	}


	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}


	public String getLegendName() {
		return legendName;
	}


	public void setLegendName(String legendName) {
		this.legendName = legendName;
	}


	public String getKpiName() {
		return kpiName;
	}


	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}


	public Integer getCreatorId() {
		return creatorId;
	}


	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}


	public String getAppliedTo() {
		return appliedTo;
	}


	public void setAppliedTo(String appliedTo) {
		this.appliedTo = appliedTo;
	}


	public Double getOpacity() {
		return opacity;
	}


	public void setOpacity(Double opacity) {
		this.opacity = opacity;
	}


	public List<LegendWrapper> getLegendRangeList() {
		return legendRangeList;
	}


	public void setLegendRangeList(List<LegendWrapper> legendRangeList) {
		this.legendRangeList = legendRangeList;
	}

	public Boolean getDefaultKpi() {
		return defaultKpi;
	}


	public void setDefaultKpi(Boolean defaultKpi) {
		this.defaultKpi = defaultKpi;
	}


	public String getConfiguration() {
		return configuration;
	}


	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}


	@Override
	public String toString() {
		return "LegendWrapper [id=" + id + ", legendName=" + legendName + ", kpiName=" + kpiName + ", appliedTo="
				+ appliedTo + ", opacity=" + opacity + ", colorCode=" + colorCode + ", minValue=" + minValue
				+ ", maxValue=" + maxValue + ", creatorId=" + creatorId + ", defaultKpi=" + defaultKpi
				+ ", legendRangeList=" + legendRangeList + ", configuration=" + configuration + "]";
	}

}
