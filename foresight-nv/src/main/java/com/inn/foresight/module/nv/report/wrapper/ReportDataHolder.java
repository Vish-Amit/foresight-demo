package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;
import java.util.Map;

import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;

public class ReportDataHolder {
	private Integer woId;
	private List<KPIWrapper> kpiList;
	private Map<String, Map<String, List<String>>> recipeWiseIDMap;
	private List<String[]> dataList;
	private List<DriveDataWrapper> imageDataList;
	private Map<String, String[]> recipeWiseSummaryMap;
	private List<List<List<Double>>> driveRoute;
	private List<KPISummaryDataWrapper> kpiSummaryDataList;
	private Map<String, Integer> summaryKpiIndexMap ;
	private Map<String, Integer> kpiIndexMap;
	private List<String> fetchKPIList;
	private List<String> fetchSummaryKPIList;

	public Integer getWoId() {
		return woId;
	}
	public void setWoId(Integer woId) {
		this.woId = woId;
	}
	public List<KPIWrapper> getKpiList() {
		return kpiList;
	}
	public void setKpiList(List<KPIWrapper> kpiList) {
		this.kpiList = kpiList;
	}

	public Map<String, Map<String, List<String>>> getRecipeWiseIDMap() {
		return recipeWiseIDMap;
	}
	public void setRecipeWiseIDMap(Map<String, Map<String, List<String>>> recipeWiseIDMap) {
		this.recipeWiseIDMap = recipeWiseIDMap;
	}
	public List<String[]> getDataList() {
		return dataList;
	}
	public void setDataList(List<String[]> dataList) {
		this.dataList = dataList;
	}
	public List<DriveDataWrapper> getImageDataList() {
		return imageDataList;
	}
	public void setImageDataList(List<DriveDataWrapper> imageDataList) {
		this.imageDataList = imageDataList;
	}

	public Map<String, String[]> getRecipeWiseSummaryMap() {
		return recipeWiseSummaryMap;
	}
	public void setRecipeWiseSummaryMap(Map<String, String[]> recipeWiseSummaryMap) {
		this.recipeWiseSummaryMap = recipeWiseSummaryMap;
	}
	public List<List<List<Double>>> getDriveRoute() {
		return driveRoute;
	}
	public void setDriveRoute(List<List<List<Double>>> driveRoute) {
		this.driveRoute = driveRoute;
	}
	public List<KPISummaryDataWrapper> getKpiSummaryDataList() {
		return kpiSummaryDataList;
	}
	public void setKpiSummaryDataList(List<KPISummaryDataWrapper> kpiSummaryDataList) {
		this.kpiSummaryDataList = kpiSummaryDataList;
	}
	
	
	public Map<String, Integer> getSummaryKpiIndexMap() {
		return summaryKpiIndexMap;
	}
	public void setSummaryKpiIndexMap(Map<String, Integer> summaryKpiIndexMap) {
		this.summaryKpiIndexMap = summaryKpiIndexMap;
	}
	public Map<String, Integer> getKpiIndexMap() {
		return kpiIndexMap;
	}
	public void setKpiIndexMap(Map<String, Integer> kpiIndexMap) {
		this.kpiIndexMap = kpiIndexMap;
	}
	
	public List<String> getFetchKPIList() {
		return fetchKPIList;
	}
	public void setFetchKPIList(List<String> fetchKPIList) {
		this.fetchKPIList = fetchKPIList;
	}
	public List<String> getFetchSummaryKPIList() {
		return fetchSummaryKPIList;
	}
	public void setFetchSummaryKPIList(List<String> fetchSummaryKPIList) {
		this.fetchSummaryKPIList = fetchSummaryKPIList;
	}
	public ReportDataHolder(Integer woId, List<KPIWrapper> kpiList,
			Map<String, Map<String, List<String>>> recipeWiseIDMap, List<String[]> dataList,
			List<DriveDataWrapper> imageDataList, Map<String, String[]> recipeWiseSummaryMap,
			List<List<List<Double>>> driveRoute, List<KPISummaryDataWrapper> kpiSummaryDataList) {
		super();
		this.woId = woId;
		this.kpiList = kpiList;
		this.recipeWiseIDMap = recipeWiseIDMap;
		this.dataList = dataList;
		this.imageDataList = imageDataList;
		this.recipeWiseSummaryMap = recipeWiseSummaryMap;
		this.driveRoute = driveRoute;
		this.kpiSummaryDataList = kpiSummaryDataList;
	}
	
	public ReportDataHolder(List<String[]> dataList) {
		super();
		this.dataList = dataList;
	}
	public ReportDataHolder() {
		super();
	}
	@Override
	public String toString() {
		return "ReportDataHolder [woId=" + woId + ", kpiList=" + kpiList + ", recipeWiseIDMap=" + recipeWiseIDMap
				+ ", dataList=" + dataList + ", imageDataList=" + imageDataList + ", recipeWiseSummaryMap="
				+ recipeWiseSummaryMap + ", driveRoute=" + driveRoute + ", kpiSummaryDataList=" + kpiSummaryDataList
				+ ", summaryKpiIndexMap=" + summaryKpiIndexMap + ", kpiIndexMap=" + kpiIndexMap + "]";
	}
	
	
	

}
