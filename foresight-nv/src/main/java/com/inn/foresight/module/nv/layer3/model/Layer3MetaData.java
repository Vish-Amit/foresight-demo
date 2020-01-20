package com.inn.foresight.module.nv.layer3.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Layer3MetaData")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Layer3MetaData {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "layer3metadataid_pk")
	private Integer id;
	

	@Column(name = "kpidisplayname")
	private String kpiName;


	@Column(name = "hbasecolumnname")
	private String hbaseColumnName;

	@Column(name = "summaryaggtype")
	private String summaryAggType;

	@Column(name = "driveaggtype")
	private String driveAggType;

	@Column(name = "techhierarchy")
	private String techHierarchy;

	@Column(name = "category")
	private String category;


	@Column(name = "charttype")
	private String chartType;

	@Column(name = "legendid_fk")
	private Integer legendid_fk;

	
	@Column(name = "isrequiredonui")
	private Boolean isRequiredOnUi;

	@Column(name = "eventcolor")
	private String eventColor;


	@Column(name = "sourcetype")
	private String sourceType;

	@Column(name = "valuetype")
	private String valueType;

	@Column(name = "summary")
	private Boolean summary;

	@Column(name = "layername")
	private String layerName;

	@Column(name = "kpihierarchy")
	private String kpiHierarchy;
	
	@Column(name = "unit")
	private String unit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public String getHbaseColumnName() {
		return hbaseColumnName;
	}

	public void setHbaseColumnName(String hbaseColumnName) {
		this.hbaseColumnName = hbaseColumnName;
	}

	public String getSummaryAggType() {
		return summaryAggType;
	}

	public void setSummaryAggType(String summaryAggType) {
		this.summaryAggType = summaryAggType;
	}

	public String getDriveAggType() {
		return driveAggType;
	}

	public void setDriveAggType(String driveAggType) {
		this.driveAggType = driveAggType;
	}

	public String getTechHierarchy() {
		return techHierarchy;
	}

	public void setTechHierarchy(String techHierarchy) {
		this.techHierarchy = techHierarchy;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public Integer getLegendid_fk() {
		return legendid_fk;
	}

	public void setLegendid_fk(Integer legendid_fk) {
		this.legendid_fk = legendid_fk;
	}

	public Boolean getIsRequiredOnUi() {
		return isRequiredOnUi;
	}

	public void setIsRequiredOnUi(Boolean isRequiredOnUi) {
		this.isRequiredOnUi = isRequiredOnUi;
	}

	

	public String getEventColor() {
		return eventColor;
	}

	public void setEventColor(String eventColor) {
		this.eventColor = eventColor;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public Boolean getSummary() {
		return summary;
	}

	public void setSummary(Boolean summary) {
		this.summary = summary;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public String getKpiHierarchy() {
		return kpiHierarchy;
	}

	public void setKpiHierarchy(String kpiHierarchy) {
		this.kpiHierarchy = kpiHierarchy;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "Layer3MetaData [id=" + id + ", kpiName=" + kpiName + ", hbaseColumnName=" + hbaseColumnName
				+ ", summaryAggType=" + summaryAggType + ", driveAggType=" + driveAggType + ", techHierarchy="
				+ techHierarchy + ", category=" + category + ", chartType=" + chartType + ", legendid_fk=" + legendid_fk
				+ ", isRequiredOnUi=" + isRequiredOnUi + ", eventColor=" + eventColor + ", sourceType=" + sourceType
				+ ", valueType=" + valueType + ", summary=" + summary + ", layerName=" + layerName + ", kpiHierarchy="
				+ kpiHierarchy + ", unit=" + unit + "]";
	}

	
	
}
