package com.inn.foresight.module.nv.report.wrapper.inbuilding;

import java.util.Date;
import java.util.List;

import com.inn.foresight.core.generic.utils.Utils;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIDataWrapper;

@JpaWrapper
public class WalkTestSummaryWrapper {
	private Double avgRSRP;
	private Double greaterThanMinus100RSRP;
	private Double avgSINR;
	private Double greaterThan12SINR;
	private Double avgDL;
	private Double avgUL;
	private Integer strongestPCI;
	private Double avgCQI;
	private Double mimoPercent;
	private Double rrcSuccessRate;
	private Double erabDropRate;
	private Double handoverSuccessRate;
	private Double fddPercent;
	private Double tddPercent;
	private String buildingName;
	private String wingName;
	private String floorNumber;
	private String unitName;
	private String recipeName;
	private Integer recipeId;
	private Integer woRecipeMappingId;
	private String date;
	private Double avgMos;
	private Double callSetupSuccessRate;
	private Double callDropRate;
	private Double avgPUSCHUL;
	private Double avgPDSCHDL;
	private String attachSuccessRate;
	private String detachSuccessRate;
	private Double attachLatency;
	private String rachSuccessRate;
	private String callSetupSuccess;
	private Double imsRegistrationSetupTime;
	private String rtpPacketLossRate;
	private Double callConnectionSetupTime;
	private Double voLTEJitter;
	private Double voLTEPacketLoss;
	private String callDroppedRate;
	private Double minLatency;
	private Double maxLatency;
	private Double avgLatency;
	private String callInitCount;
	private String callDropCount;
	private String callFailCount;
	private String callSuccessCount;
	private Boolean showPingRecipe;
	List<KPIDataWrapper> bufferWiseKpiList;
	

	public WalkTestSummaryWrapper(Double totalRSRP, Integer countRSRP, Integer countRSRPGreaterThan100,
			Double totalSINR, Integer countSINR, Integer countSINRGreaterThan12, Double totalDL, Integer countDL,
			Double totalUL, Integer countUL, Integer strongestPCI, Double totalCQI, Integer countCQI, Integer totalMIMO,
			Integer countMIMO, Integer rrcInitiate, Integer rrcSuccess, Integer erabDrop, Integer erabSuccess,
			Integer handoverSuccess, Integer handoverInitiate, Integer countTDD, Integer countFDD, String buildingName,
			String wingName, String floorNumber, String unitName, Integer recipeId, String recipeName,
			Integer woRecipeMappingId, Date date, Integer countMos, Double totalMos, Integer callInitiateCount,
			Integer callSetupSuccessCount, Integer callDropCount, Double totalpuschul, Integer countpuschul,
			Double totalpdschdl, Integer countpdschdl) {

		Double avgR = ReportUtil.getAverage(totalRSRP, countRSRP);
		if (avgR != null) {
			this.avgRSRP = ReportUtil.parseToFixedDecimalPlace(avgR, ReportConstants.INDEX_THREE);
		}
		this.greaterThanMinus100RSRP = ReportUtil.getPercentage(countRSRPGreaterThan100, countRSRP);
		Double avgS = ReportUtil.getAverage(totalSINR, countSINR);
		if (avgS != null) {
			this.avgSINR = ReportUtil.parseToFixedDecimalPlace(avgS, ReportConstants.INDEX_THREE);
		}

		this.greaterThan12SINR = ReportUtil.getPercentage(countSINRGreaterThan12, countSINR);
		Double avgD = ReportUtil.getAverage(totalDL, countDL);
		if (avgD != null) {
			this.avgDL = ReportUtil.parseToFixedDecimalPlace(avgD, ReportConstants.INDEX_THREE);
		}

		Double avgU = ReportUtil.getAverage(totalUL, countUL);
		if (avgU != null) {
			this.avgUL = ReportUtil.parseToFixedDecimalPlace(avgU, ReportConstants.INDEX_THREE);
		}
		this.strongestPCI = strongestPCI;
		this.avgCQI = ReportUtil.getAverage(totalCQI, countCQI);
		this.mimoPercent = ReportUtil.getPercentage(countMIMO, totalMIMO);
		this.rrcSuccessRate = ReportUtil.getPercentage(rrcSuccess, rrcInitiate);
		this.erabDropRate = ReportUtil.getPercentage(erabDrop, erabSuccess);
		this.handoverSuccessRate = ReportUtil.getPercentage(handoverSuccess, handoverInitiate);
		if (countTDD != null && countFDD != null) {
			this.tddPercent = ReportUtil.getPercentage(countTDD, countFDD + countTDD);
			this.fddPercent = ReportUtil.getPercentage(countFDD, countFDD + countTDD);
		}
		if ((countTDD != null && countFDD == null) || (countFDD != null && countTDD == null)) {
			this.tddPercent = ReportUtil.getPercentage(countTDD, countTDD);
			this.fddPercent = ReportUtil.getPercentage(countFDD, countFDD);
		}
		this.buildingName = buildingName;
		this.wingName = wingName;
		this.floorNumber = floorNumber;
		this.unitName = unitName;
		this.recipeId = recipeId;
		this.recipeName = recipeName;
		this.woRecipeMappingId = woRecipeMappingId;
		this.date = Utils.parseDateToString(date, ReportConstants.DATE_FORMAT_YYYYMMDD);

		Double avgM = ReportUtil.getAverage(totalMos, countMos);
		if (avgM != null) {
			this.avgMos = ReportUtil.parseToFixedDecimalPlace(avgM, ReportConstants.INDEX_THREE);
		}
		this.callSetupSuccessRate = ReportUtil.getPercentage(callSetupSuccessCount, callInitiateCount);
		this.callDropRate = ReportUtil.getPercentage(callDropCount, callInitiateCount);
		//Double totalpdschdl, Integer countpdschdl
		Double avgpdl = ReportUtil.getAverage(totalpdschdl, countpdschdl);
		if (avgpdl != null) {
			this.avgPDSCHDL = ReportUtil.parseToFixedDecimalPlace(avgpdl, ReportConstants.INDEX_THREE);
		}
		// Double totalpuschul, Integer countpuschul
		Double avgpul = ReportUtil.getAverage(totalpuschul, countpuschul);
		if (avgpul != null) {
			this.avgPUSCHUL = ReportUtil.parseToFixedDecimalPlace(avgpul, ReportConstants.INDEX_THREE);
		}
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getWoRecipeMappingId() {
		return woRecipeMappingId;
	}

	public void setWoRecipeMappingId(Integer woRecipeMappingId) {
		this.woRecipeMappingId = woRecipeMappingId;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public Integer getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Integer recipeId) {
		this.recipeId = recipeId;
	}

	public Double getAvgRSRP() {
		return avgRSRP;
	}

	public void setAvgRSRP(Double avgRSRP) {
		this.avgRSRP = avgRSRP;
	}

	public Double getGreaterThanMinus100RSRP() {
		return greaterThanMinus100RSRP;
	}

	public void setGreaterThanMinus100RSRP(Double greaterThanMinus100RSRP) {
		this.greaterThanMinus100RSRP = greaterThanMinus100RSRP;
	}

	public Double getAvgSINR() {
		return avgSINR;
	}

	public void setAvgSINR(Double avgSINR) {
		this.avgSINR = avgSINR;
	}

	public Double getAvgDL() {
		return avgDL;
	}

	public void setAvgDL(Double avgDL) {
		this.avgDL = avgDL;
	}

	public Double getAvgUL() {
		return avgUL;
	}

	public void setAvgUL(Double avgUL) {
		this.avgUL = avgUL;
	}

	public Integer getStrongestPCI() {
		return strongestPCI;
	}

	public void setStrongestPCI(Integer strongestPCI) {
		this.strongestPCI = strongestPCI;
	}

	public Double getAvgCQI() {
		return avgCQI;
	}

	public void setAvgCQI(Double avgCQI) {
		this.avgCQI = avgCQI;
	}

	public Double getHandoverSuccessRate() {
		return handoverSuccessRate;
	}

	public void setHandoverSuccessRate(Double handoverSuccessRate) {
		this.handoverSuccessRate = handoverSuccessRate;
	}

	public Double getFddPercent() {
		return fddPercent;
	}

	public void setFddPercent(Double fddPercent) {
		this.fddPercent = fddPercent;
	}

	public Double getTddPercent() {
		return tddPercent;
	}

	public void setTddPercent(Double tddPercent) {
		this.tddPercent = tddPercent;
	}

	public Double getGreaterThan12SINR() {
		return greaterThan12SINR;
	}

	public void setGreaterThan12SINR(Double greaterThan12SINR) {
		this.greaterThan12SINR = greaterThan12SINR;
	}

	public Double getMimoPercent() {
		return mimoPercent;
	}

	public void setMimoPercent(Double mimoPercent) {
		this.mimoPercent = mimoPercent;
	}

	public Double getRrcSuccessRate() {
		return rrcSuccessRate;
	}

	public void setRrcSuccessRate(Double rrcSuccessRate) {
		this.rrcSuccessRate = rrcSuccessRate;
	}

	public Double getErabDropRate() {
		return erabDropRate;
	}

	public void setErabDropRate(Double erabDropRate) {
		this.erabDropRate = erabDropRate;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getWingName() {
		return wingName;
	}

	public void setWingName(String wingName) {
		this.wingName = wingName;
	}

	public String getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(String floorNumber) {
		this.floorNumber = floorNumber;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * @return the avgMos
	 */
	public Double getAvgMos() {
		return avgMos;
	}

	/**
	 * @param avgMos the avgMos to set
	 */
	public void setAvgMos(Double avgMos) {
		this.avgMos = avgMos;
	}

	/**
	 * @return the callSetupSuccessRate
	 */
	public Double getCallSetupSuccessRate() {
		return callSetupSuccessRate;
	}

	/**
	 * @param callSetupSuccessRate the callSetupSuccessRate to set
	 */
	public void setCallSetupSuccessRate(Double callSetupSuccessRate) {
		this.callSetupSuccessRate = callSetupSuccessRate;
	}

	/**
	 * @return the callDropRate
	 */
	public Double getCallDropRate() {
		return callDropRate;
	}

	/**
	 * @param callDropRate the callDropRate to set
	 */
	public void setCallDropRate(Double callDropRate) {
		this.callDropRate = callDropRate;
	}

	public Double getAvgPUSCHUL() {
		return avgPUSCHUL;
	}

	public void setAvgPUSCHUL(Double avgPUSCHUL) {
		this.avgPUSCHUL = avgPUSCHUL;
	}

	public Double getAvgPDSCHDL() {
		return avgPDSCHDL;
	}

	public void setAvgPDSCHDL(Double avgPDSCHDL) {
		this.avgPDSCHDL = avgPDSCHDL;
	}

	public String getAttachSuccessRate() {
		return attachSuccessRate;
	}

	public void setAttachSuccessRate(String attachSuccessRate) {
		this.attachSuccessRate = attachSuccessRate;
	}

	public String getDetachSuccessRate() {
		return detachSuccessRate;
	}

	public void setDetachSuccessRate(String detachSuccessRate) {
		this.detachSuccessRate = detachSuccessRate;
	}

	public Double getAttachLatency() {
		return attachLatency;
	}

	public void setAttachLatency(Double attachLatency) {
		this.attachLatency = attachLatency;
	}

	public String getRachSuccessRate() {
		return rachSuccessRate;
	}

	public void setRachSuccessRate(String rachSuccessRate) {
		this.rachSuccessRate = rachSuccessRate;
	}

	public String getCallSetupSuccess() {
		return callSetupSuccess;
	}

	public void setCallSetupSuccess(String callSetupSuccess) {
		this.callSetupSuccess = callSetupSuccess;
	}

	public Double getImsRegistrationSetupTime() {
		return imsRegistrationSetupTime;
	}

	public void setImsRegistrationSetupTime(Double imsRegistrationSetupTime) {
		this.imsRegistrationSetupTime = imsRegistrationSetupTime;
	}

	public Double getCallConnectionSetupTime() {
		return callConnectionSetupTime;
	}

	public void setCallConnectionSetupTime(Double callConnectionSetupTime) {
		this.callConnectionSetupTime = callConnectionSetupTime;
	}

	public Double getVoLTEJitter() {
		return voLTEJitter;
	}

	public void setVoLTEJitter(Double voLTEJitter) {
		this.voLTEJitter = voLTEJitter;
	}

	public Double getVoLTEPacketLoss() {
		return voLTEPacketLoss;
	}

	public void setVoLTEPacketLoss(Double voLTEPacketLoss) {
		this.voLTEPacketLoss = voLTEPacketLoss;
	}

	public String getCallDroppedRate() {
		return callDroppedRate;
	}

	public void setCallDroppedRate(String callDroppedRate) {
		this.callDroppedRate = callDroppedRate;
	}

	public Double getMinLatency() {
		return minLatency;
	}

	public void setMinLatency(Double minLatency) {
		this.minLatency = minLatency;
	}

	public Double getMaxLatency() {
		return maxLatency;
	}

	public void setMaxLatency(Double maxLatency) {
		this.maxLatency = maxLatency;
	}

	public Double getAvgLatency() {
		return avgLatency;
	}

	public void setAvgLatency(Double avgLatency) {
		this.avgLatency = avgLatency;
	}

	public String getCallInitCount() {
		return callInitCount;
	}

	public void setCallInitCount(String callInitCount) {
		this.callInitCount = callInitCount;
	}

	public String getCallDropCount() {
		return callDropCount;
	}

	public void setCallDropCount(String callDropCount) {
		this.callDropCount = callDropCount;
	}

	public String getCallFailCount() {
		return callFailCount;
	}

	public void setCallFailCount(String callFailCount) {
		this.callFailCount = callFailCount;
	}

	public String getCallSuccessCount() {
		return callSuccessCount;
	}

	public void setCallSuccessCount(String callSuccessCount) {
		this.callSuccessCount = callSuccessCount;
	}

	public List<KPIDataWrapper> getBufferWiseKpiList() {
		return bufferWiseKpiList;
	}

	public void setBufferWiseKpiList(List<KPIDataWrapper> bufferWiseKpiList) {
		this.bufferWiseKpiList = bufferWiseKpiList;
	}

	public Boolean getShowPingRecipe() {
		return showPingRecipe;
	}

	public void setShowPingRecipe(Boolean showPingRecipe) {
		this.showPingRecipe = showPingRecipe;
	}

	public String getRtpPacketLossRate() {
		return rtpPacketLossRate;
	}

	public void setRtpPacketLossRate(String rtpPacketLossRate) {
		this.rtpPacketLossRate = rtpPacketLossRate;
	}

	@Override
	public String toString() {
		return "WalkTestSummaryWrapper [avgRSRP=" + avgRSRP + ", greaterThanMinus100RSRP=" + greaterThanMinus100RSRP
				+ ", avgSINR=" + avgSINR + ", greaterThan12SINR=" + greaterThan12SINR + ", avgDL=" + avgDL + ", avgUL="
				+ avgUL + ", strongestPCI=" + strongestPCI + ", avgCQI=" + avgCQI + ", mimoPercent=" + mimoPercent
				+ ", rrcSuccessRate=" + rrcSuccessRate + ", erabDropRate=" + erabDropRate + ", handoverSuccessRate="
				+ handoverSuccessRate + ", fddPercent=" + fddPercent + ", tddPercent=" + tddPercent + ", buildingName="
				+ buildingName + ", wingName=" + wingName + ", floorNumber=" + floorNumber + ", unitName=" + unitName
				+ ", recipeName=" + recipeName + ", recipeId=" + recipeId + ", woRecipeMappingId=" + woRecipeMappingId
				+ ", date=" + date + ", avgMos=" + avgMos + ", callSetupSuccessRate=" + callSetupSuccessRate
				+ ", callDropRate=" + callDropRate + ", avgPUSCHUL=" + avgPUSCHUL + ", avgPDSCHDL=" + avgPDSCHDL
				+ ", attachSuccessRate=" + attachSuccessRate + ", detachSuccessRate=" + detachSuccessRate
				+ ", attachLatency=" + attachLatency + ", rachSuccessRate=" + rachSuccessRate + ", callSetupSuccess="
				+ callSetupSuccess + ", imsRegistrationSetupTime=" + imsRegistrationSetupTime + ", rtpPacketLossRate="
				+ rtpPacketLossRate + ", callConnectionSetupTime=" + callConnectionSetupTime + ", voLTEJitter="
				+ voLTEJitter + ", voLTEPacketLoss=" + voLTEPacketLoss + ", callDroppedRate=" + callDroppedRate
				+ ", minLatency=" + minLatency + ", maxLatency=" + maxLatency + ", avgLatency=" + avgLatency
				+ ", callInitCount=" + callInitCount + ", callDropCount=" + callDropCount + ", callFailCount="
				+ callFailCount + ", callSuccessCount=" + callSuccessCount + ", showPingRecipe=" + showPingRecipe
				+ ", bufferWiseKpiList=" + bufferWiseKpiList + "]";
	}

	

}
