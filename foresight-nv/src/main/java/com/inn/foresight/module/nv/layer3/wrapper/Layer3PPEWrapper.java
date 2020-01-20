package com.inn.foresight.module.nv.layer3.wrapper;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class Layer3PPEWrapper {

	private Integer nvl3kpibuildermetaid_pk;

	private String aggType;

	private Boolean isProcessingReq;
	
	private String beanName;

	private String kpiName;

	private String chartType;

	private String unit;

	private Integer legendid_fk;

	private List<String> columnNameList;
	
	private Map<String,List<String>> summaryMap;
	
	private List<String> rowPrefixList;
	
	private Integer workorderId;
	private List<Integer> workorderIds;
	private Integer recipeId;
	
	private String layerType;
		
    private String hbaseColumnName;
    
    private String sourceType;
	
	public Layer3PPEWrapper(Integer workorderId, String layerType) {
		this.workorderId = workorderId;
		this.layerType = layerType;
	}

	public Layer3PPEWrapper(List<Integer> workorderIds, String layerType) {
		this.workorderIds = workorderIds;
		this.layerType = layerType;
	}

	public Layer3PPEWrapper() {
		
	}

	public Integer getNvl3kpibuildermetaid_pk() {
		return nvl3kpibuildermetaid_pk;
	}

	public void setNvl3kpibuildermetaid_pk(Integer nvl3kpibuildermetaid_pk) {
		this.nvl3kpibuildermetaid_pk = nvl3kpibuildermetaid_pk;
	}

	public String getAggType() {
		return aggType;
	}

	public void setAggType(String aggType) {
		this.aggType = aggType;
	}

	public Boolean getIsProcessingReq() {
		return isProcessingReq;
	}

	public void setIsProcessingReq(Boolean isProcessingReq) {
		this.isProcessingReq = isProcessingReq;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getLegendid_fk() {
		return legendid_fk;
	}

	public void setLegendid_fk(Integer legendid_fk) {
		this.legendid_fk = legendid_fk;
	}

	public List<String> getColumnNameList() {
		return columnNameList;
	}

	public void setColumnNameList(List<String> columnNameList) {
		this.columnNameList = columnNameList;
	}

	public Map<String, List<String>> getSummaryMap() {
		return summaryMap;
	}

	public void setSummaryMap(Map<String, List<String>> summaryMap) {
		this.summaryMap = summaryMap;
	}

	public List<String> getRowPrefixList() {
		return rowPrefixList;
	}

	public void setRowPrefixList(List<String> rowPrefixList) {
		this.rowPrefixList = rowPrefixList;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public Integer getWorkorderId() {
		return workorderId;
	}

	public void setWorkorderId(Integer workorderId) {
		this.workorderId = workorderId;
	}

	public Integer getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Integer recipeId) {
		this.recipeId = recipeId;
	}

	public String getLayerType() {
		return layerType;
	}

	public void setLayerType(String layerType) {
		this.layerType = layerType;
	}

	public String getHbaseColumnName() {
		return hbaseColumnName;
	}

	public void setHbaseColumnName(String hbaseColumnName) {
		this.hbaseColumnName = hbaseColumnName;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public List<Integer> getWorkorderIds() {
		return workorderIds;
	}

	public void setWorkorderIds(List<Integer> workorderIds) {
		this.workorderIds = workorderIds;
	}
	
	

}
