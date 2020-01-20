package com.inn.foresight.module.nv.report.wrapper.benchmark;

import java.io.InputStream;
import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.module.nv.report.wrapper.IndoorBenchMarkSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.OssKpiWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBBenchmarkGraphDataWrapper;

@JpaWrapper
public class BenchMarksubwrapper {

	private List<BenchMarkOperatorInfo> rsrpImgList;
	private List<BenchMarkOperatorInfo> sinrImgList;
	private List<BenchMarkOperatorInfo> rsrqImgList;
	private List<BenchMarkOperatorInfo> bandWisersrpImgList;
	private List<BenchMarkOperatorInfo> bandWisesinrImgList;

	private List<BenchMarkOperatorInfo> dlImgList;
	private List<BenchMarkOperatorInfo> ulImgList;
	private List<BenchMarkOperatorInfo> mcsImgList;
	private List<BenchMarkOperatorInfo> cqiImgList;
	private List<BenchMarkOperatorInfo> mimoImgList;
	private List<BenchMarkOperatorInfo> mosImgList;
	private List<BenchMarkOperatorInfo> pciImgList;
	private List<IndoorBenchMarkSubWrapper> measurementList;
	private List<IndoorBenchMarkSubWrapper> servingDataList;
	private List<IndoorBenchMarkSubWrapper> mosDataList;
	private List<IndoorBenchMarkSubWrapper> callFailDetail;
	private List<IndoorBenchMarkSubWrapper> callDropDetail;

	private List<IndoorBenchMarkSubWrapper>dataList;
	private List<IndoorBenchMarkSubWrapper>voiceList;
	private List<BenchMarkOperatorInfo> callFailImgList;
	private List<BenchMarkOperatorInfo> callDropImgList;
	private List<BenchMarkOperatorInfo> voltersrpImgList;
	private List<BenchMarkOperatorInfo> voltesinrImgList;
	private List<BenchMarkOperatorInfo> voltersrqImgList;

	private List<BenchMarkComparison> benchmarkComparisonList;

	private List<VoiceStatsWrapper> voiceKpiList;
	private List<LegendListWrapper> rsrpLegendList;
	private List<KpiRankWrapper> rsrpRankList;
	private List<LegendListWrapper> sinrLegendList;
	private List<KpiRankWrapper> sinrRankList;
	private List<LegendListWrapper> dlLegendList;
	private List<KpiRankWrapper> dlRankList;
	private List<LegendListWrapper> ulLegendList;
	private List<KpiRankWrapper> ulRankList;
	private List<BenchMarkOperatorInfo> servingSystemImgList;
	private List<BenchMarkOperatorInfo> carrierAggImgList;
	private List<LegendListWrapper> mimoLegendList;
	private List<BenchMarkOperatorInfo> dlBandwidthImgList;
	private List<LegendListWrapper> dlBandwidthLegendList;
	private List<LegendListWrapper> cqiLegendList;
	private List<LegendListWrapper> mosLegendList;

	private String operator1;
	private String operator2;
	private String operator3;
	private String operator4;
	private InputStream routeImage;

	private String routeLength;
	private String plannedSitesImage;
	private String plannedSitesLegend;
	private String kpiName;
	private List<IBBenchmarkGraphDataWrapper> rsrpGraphList;
	private List<IBBenchmarkGraphDataWrapper> rsrqGraphList;
	private List<IBBenchmarkGraphDataWrapper> sinrGraphList;
	private List<IBBenchmarkGraphDataWrapper> dlGraphList;
	private List<IBBenchmarkGraphDataWrapper> ulGraphList;
	private List<IBBenchmarkGraphDataWrapper> cstGraphList;
	private List<IBBenchmarkGraphDataWrapper> mosGraphList;

	private List<IBBenchmarkGraphDataWrapper> voiceTechnolgyGraphList;
	private List<IBBenchmarkGraphDataWrapper> dataTechnolgyGraphList;
	private List<IBBenchmarkGraphDataWrapper> techVsDlGraphList;
	private List<IBBenchmarkGraphDataWrapper> bandVsDlGraphList;
	private List<IBBenchmarkGraphDataWrapper> frequencyBandGraphList;
    private List<OssKpiWrapper>ossDataList;
	public String getOperator1() {
		return operator1;
	}

	public void setOperator1(String operator1) {
		this.operator1 = operator1;
	}

	public String getOperator2() {
		return operator2;
	}

	public void setOperator2(String operator2) {
		this.operator2 = operator2;
	}

	public String getOperator3() {
		return operator3;
	}

	public void setOperator3(String operator3) {
		this.operator3 = operator3;
	}

	public String getOperator4() {
		return operator4;
	}

	public void setOperator4(String operator4) {
		this.operator4 = operator4;
	}

	public List<VoiceStatsWrapper> getVoiceKpiList() {
		return voiceKpiList;
	}

	public void setVoiceKpiList(List<VoiceStatsWrapper> voiceKpiList) {
		this.voiceKpiList = voiceKpiList;
	}

	public List<BenchMarkComparison> getBenchmarkComparisonList() {
		return benchmarkComparisonList;
	}

	public void setBenchmarkComparisonList(List<BenchMarkComparison> benchmarkComparisonList) {
		this.benchmarkComparisonList = benchmarkComparisonList;
	}

	public List<BenchMarkOperatorInfo> getRsrpImgList() {
		return rsrpImgList;
	}

	public void setRsrpImgList(List<BenchMarkOperatorInfo> rsrpImgList) {
		this.rsrpImgList = rsrpImgList;
	}

	public List<BenchMarkOperatorInfo> getSinrImgList() {
		return sinrImgList;
	}

	public void setSinrImgList(List<BenchMarkOperatorInfo> sinrImgList) {
		this.sinrImgList = sinrImgList;
	}

	public List<BenchMarkOperatorInfo> getDlImgList() {
		return dlImgList;
	}

	public void setDlImgList(List<BenchMarkOperatorInfo> dlImgList) {
		this.dlImgList = dlImgList;
	}

	public List<BenchMarkOperatorInfo> getUlImgList() {
		return ulImgList;
	}

	public void setUlImgList(List<BenchMarkOperatorInfo> ulImgList) {
		this.ulImgList = ulImgList;
	}

	public List<BenchMarkOperatorInfo> getMcsImgList() {
		return mcsImgList;
	}

	public void setMcsImgList(List<BenchMarkOperatorInfo> mcsImgList) {
		this.mcsImgList = mcsImgList;
	}

	public List<BenchMarkOperatorInfo> getCqiImgList() {
		return cqiImgList;
	}

	public void setCqiImgList(List<BenchMarkOperatorInfo> cqiImgList) {
		this.cqiImgList = cqiImgList;
	}

	public List<BenchMarkOperatorInfo> getMimoImgList() {
		return mimoImgList;
	}

	public void setMimoImgList(List<BenchMarkOperatorInfo> mimoImgList) {
		this.mimoImgList = mimoImgList;
	}

	public List<BenchMarkOperatorInfo> getMosImgList() {
		return mosImgList;
	}

	public void setMosImgList(List<BenchMarkOperatorInfo> mosImgList) {
		this.mosImgList = mosImgList;
	}

	public List<BenchMarkOperatorInfo> getPciImgList() {
		return pciImgList;
	}

	public void setPciImgList(List<BenchMarkOperatorInfo> pciImgList) {
		this.pciImgList = pciImgList;
	}

	public List<LegendListWrapper> getRsrpLegendList() {
		return rsrpLegendList;
	}

	public void setRsrpLegendList(List<LegendListWrapper> rsrpLegendList) {
		this.rsrpLegendList = rsrpLegendList;
	}

	public InputStream getRouteImage() {
		return routeImage;
	}

	public void setRouteImage(InputStream routeImage) {
		this.routeImage = routeImage;
	}

	public String getRouteLength() {
		return routeLength;
	}

	public void setRouteLength(String routeLength) {
		this.routeLength = routeLength;
	}

	public List<KpiRankWrapper> getRsrpRankList() {
		return rsrpRankList;
	}

	public void setRsrpRankList(List<KpiRankWrapper> rsrpRankList) {
		this.rsrpRankList = rsrpRankList;
	}

	public String getPlannedSitesImage() {
		return plannedSitesImage;
	}

	public void setPlannedSitesImage(String plannedSitesImage) {
		this.plannedSitesImage = plannedSitesImage;
	}

	public String getPlannedSitesLegend() {
		return plannedSitesLegend;
	}

	public void setPlannedSitesLegend(String plannedSitesLegend) {
		this.plannedSitesLegend = plannedSitesLegend;
	}

	public List<LegendListWrapper> getSinrLegendList() {
		return sinrLegendList;
	}

	public void setSinrLegendList(List<LegendListWrapper> sinrLegendList) {
		this.sinrLegendList = sinrLegendList;
	}

	public List<KpiRankWrapper> getSinrRankList() {
		return sinrRankList;
	}

	public void setSinrRankList(List<KpiRankWrapper> sinrRankList) {
		this.sinrRankList = sinrRankList;
	}

	public List<LegendListWrapper> getDlLegendList() {
		return dlLegendList;
	}

	public void setDlLegendList(List<LegendListWrapper> dlLegendList) {
		this.dlLegendList = dlLegendList;
	}

	public List<KpiRankWrapper> getDlRankList() {
		return dlRankList;
	}

	public void setDlRankList(List<KpiRankWrapper> dlRankList) {
		this.dlRankList = dlRankList;
	}

	public List<LegendListWrapper> getUlLegendList() {
		return ulLegendList;
	}

	public void setUlLegendList(List<LegendListWrapper> ulLegendList) {
		this.ulLegendList = ulLegendList;
	}

	public List<KpiRankWrapper> getUlRankList() {
		return ulRankList;
	}

	public void setUlRankList(List<KpiRankWrapper> ulRankList) {
		this.ulRankList = ulRankList;
	}

	public List<BenchMarkOperatorInfo> getServingSystemImgList() {
		return servingSystemImgList;
	}

	public void setServingSystemImgList(List<BenchMarkOperatorInfo> servingSystemImgList) {
		this.servingSystemImgList = servingSystemImgList;
	}

	public List<BenchMarkOperatorInfo> getCarrierAggImgList() {
		return carrierAggImgList;
	}

	public void setCarrierAggImgList(List<BenchMarkOperatorInfo> carrierAggImgList) {
		this.carrierAggImgList = carrierAggImgList;
	}

	public List<LegendListWrapper> getMimoLegendList() {
		return mimoLegendList;
	}

	public void setMimoLegendList(List<LegendListWrapper> mimoLegendList) {
		this.mimoLegendList = mimoLegendList;
	}

	public List<BenchMarkOperatorInfo> getDlBandwidthImgList() {
		return dlBandwidthImgList;
	}

	public void setDlBandwidthImgList(List<BenchMarkOperatorInfo> dlBandwidthImgList) {
		this.dlBandwidthImgList = dlBandwidthImgList;
	}

	public List<LegendListWrapper> getDlBandwidthLegendList() {
		return dlBandwidthLegendList;
	}

	public void setDlBandwidthLegendList(List<LegendListWrapper> dlBandwidthLegendList) {
		this.dlBandwidthLegendList = dlBandwidthLegendList;
	}

	public List<LegendListWrapper> getCqiLegendList() {
		return cqiLegendList;
	}

	public void setCqiLegendList(List<LegendListWrapper> cqiLegendList) {
		this.cqiLegendList = cqiLegendList;
	}

	public List<LegendListWrapper> getMosLegendList() {
		return mosLegendList;
	}

	public void setMosLegendList(List<LegendListWrapper> mosLegendList) {
		this.mosLegendList = mosLegendList;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public List<IndoorBenchMarkSubWrapper> getMeasurementList() {
		return measurementList;
	}

	public void setMeasurementList(List<IndoorBenchMarkSubWrapper> measurementList) {
		this.measurementList = measurementList;
	}

	public List<BenchMarkOperatorInfo> getVoltersrpImgList() {
		return voltersrpImgList;
	}

	public void setVoltersrpImgList(List<BenchMarkOperatorInfo> voltersrpImgList) {
		this.voltersrpImgList = voltersrpImgList;
	}

	public List<BenchMarkOperatorInfo> getVoltesinrImgList() {
		return voltesinrImgList;
	}

	public void setVoltesinrImgList(List<BenchMarkOperatorInfo> voltesinrImgList) {
		this.voltesinrImgList = voltesinrImgList;
	}

	public List<IBBenchmarkGraphDataWrapper> getRsrpGraphList() {
		return rsrpGraphList;
	}

	public void setRsrpGraphList(List<IBBenchmarkGraphDataWrapper> rsrpGraphList) {
		this.rsrpGraphList = rsrpGraphList;
	}

	public List<BenchMarkOperatorInfo> getRsrqImgList() {
		return rsrqImgList;
	}

	public void setRsrqImgList(List<BenchMarkOperatorInfo> rsrqImgList) {
		this.rsrqImgList = rsrqImgList;
	}

	public List<BenchMarkOperatorInfo> getVoltersrqImgList() {
		return voltersrqImgList;
	}

	public void setVoltersrqImgList(List<BenchMarkOperatorInfo> voltersrqImgList) {
		this.voltersrqImgList = voltersrqImgList;
	}

	public List<IBBenchmarkGraphDataWrapper> getRsrqGraphList() {
		return rsrqGraphList;
	}

	public void setRsrqGraphList(List<IBBenchmarkGraphDataWrapper> rsrqGraphList) {
		this.rsrqGraphList = rsrqGraphList;
	}

	public List<IBBenchmarkGraphDataWrapper> getSinrGraphList() {
		return sinrGraphList;
	}

	public void setSinrGraphList(List<IBBenchmarkGraphDataWrapper> sinrGraphList) {
		this.sinrGraphList = sinrGraphList;
	}

	public List<IBBenchmarkGraphDataWrapper> getDlGraphList() {
		return dlGraphList;
	}

	public void setDlGraphList(List<IBBenchmarkGraphDataWrapper> dlGraphList) {
		this.dlGraphList = dlGraphList;
	}

	public List<IBBenchmarkGraphDataWrapper> getUlGraphList() {
		return ulGraphList;
	}

	public void setUlGraphList(List<IBBenchmarkGraphDataWrapper> ulGraphList) {
		this.ulGraphList = ulGraphList;
	}

	public List<IndoorBenchMarkSubWrapper> getServingDataList() {
		return servingDataList;
	}

	public void setServingDataList(List<IndoorBenchMarkSubWrapper> servingDataList) {
		this.servingDataList = servingDataList;
	}

	public List<IBBenchmarkGraphDataWrapper> getCstGraphList() {
		return cstGraphList;
	}

	public void setCstGraphList(List<IBBenchmarkGraphDataWrapper> cstGraphList) {
		this.cstGraphList = cstGraphList;
	}

	public List<IBBenchmarkGraphDataWrapper> getMosGraphList() {
		return mosGraphList;
	}

	public void setMosGraphList(List<IBBenchmarkGraphDataWrapper> mosGraphList) {
		this.mosGraphList = mosGraphList;
	}

	public List<BenchMarkOperatorInfo> getBandWisersrpImgList() {
		return bandWisersrpImgList;
	}

	public void setBandWisersrpImgList(List<BenchMarkOperatorInfo> bandWisersrpImgList) {
		this.bandWisersrpImgList = bandWisersrpImgList;
	}

	public List<BenchMarkOperatorInfo> getBandWisesinrImgList() {
		return bandWisesinrImgList;
	}

	public void setBandWisesinrImgList(List<BenchMarkOperatorInfo> bandWisesinrImgList) {
		this.bandWisesinrImgList = bandWisesinrImgList;
	}

	public List<IBBenchmarkGraphDataWrapper> getVoiceTechnolgyGraphList() {
		return voiceTechnolgyGraphList;
	}

	public void setVoiceTechnolgyGraphList(List<IBBenchmarkGraphDataWrapper> voiceTechnolgyGraphList) {
		this.voiceTechnolgyGraphList = voiceTechnolgyGraphList;
	}

	public List<IBBenchmarkGraphDataWrapper> getDataTechnolgyGraphList() {
		return dataTechnolgyGraphList;
	}

	public void setDataTechnolgyGraphList(List<IBBenchmarkGraphDataWrapper> dataTechnolgyGraphList) {
		this.dataTechnolgyGraphList = dataTechnolgyGraphList;
	}

	public List<IBBenchmarkGraphDataWrapper> getTechVsDlGraphList() {
		return techVsDlGraphList;
	}

	public void setTechVsDlGraphList(List<IBBenchmarkGraphDataWrapper> techVsDlGraphList) {
		this.techVsDlGraphList = techVsDlGraphList;
	}

	public List<IBBenchmarkGraphDataWrapper> getBandVsDlGraphList() {
		return bandVsDlGraphList;
	}

	public void setBandVsDlGraphList(List<IBBenchmarkGraphDataWrapper> bandVsDlGraphList) {
		this.bandVsDlGraphList = bandVsDlGraphList;
	}

	public List<IBBenchmarkGraphDataWrapper> getFrequencyBandGraphList() {
		return frequencyBandGraphList;
	}

	public void setFrequencyBandGraphList(List<IBBenchmarkGraphDataWrapper> frequencyBandGraphList) {
		this.frequencyBandGraphList = frequencyBandGraphList;
	}

	public List<IndoorBenchMarkSubWrapper> getDataList() {
		return dataList;
	}

	public void setDataList(List<IndoorBenchMarkSubWrapper> dataList) {
		this.dataList = dataList;
	}

	public List<IndoorBenchMarkSubWrapper> getVoiceList() {
		return voiceList;
	}

	public void setVoiceList(List<IndoorBenchMarkSubWrapper> voiceList) {
		this.voiceList = voiceList;
	}

	public List<BenchMarkOperatorInfo> getCallFailImgList() {
		return callFailImgList;
	}

	public void setCallFailImgList(List<BenchMarkOperatorInfo> callFailImgList) {
		this.callFailImgList = callFailImgList;
	}

	public List<BenchMarkOperatorInfo> getCallDropImgList() {
		return callDropImgList;
	}

	public void setCallDropImgList(List<BenchMarkOperatorInfo> callDropImgList) {
		this.callDropImgList = callDropImgList;
	}

	public List<IndoorBenchMarkSubWrapper> getMosDataList() {
		return mosDataList;
	}

	public void setMosDataList(List<IndoorBenchMarkSubWrapper> mosDataList) {
		this.mosDataList = mosDataList;
	}

	public List<IndoorBenchMarkSubWrapper> getCallFailDetail() {
		return callFailDetail;
	}

	public void setCallFailDetail(List<IndoorBenchMarkSubWrapper> callFailDetail) {
		this.callFailDetail = callFailDetail;
	}

	public List<IndoorBenchMarkSubWrapper> getCallDropDetail() {
		return callDropDetail;
	}

	public void setCallDropDetail(List<IndoorBenchMarkSubWrapper> callDropDetail) {
		this.callDropDetail = callDropDetail;
	}

	public List<OssKpiWrapper> getOssDataList() {
		return ossDataList;
	}

	public void setOssDataList(List<OssKpiWrapper> ossDataList) {
		this.ossDataList = ossDataList;
	}

	
 

	
	
}
