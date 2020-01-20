package com.inn.foresight.module.nv.report.service.Inbuilding.wrapper;

import java.util.List;

import com.inn.foresight.module.nv.report.wrapper.benchmark.BenchMarkOperatorInfo;

public class FloorWiseWrapper {
	String floorName;
	String criteriaPeakDL;
	String criteriaPeakUL;
	String criteriaLatency;
	String criteriaJitter;
	String bandwidth;
	List<InbuildingDataWrapper> list;
	//field required for 1 E Report
	
	private String spRsrp;
	private String  spSinr;
	private String  spPci;

	private String  volteInOutHOSuccessRate;
	private String  inOutHOSuccessRate;
	private String  volteElevHOSuccessRate;
	private String  elevHOSuccessRate;

	private String  volteStHOSuccessRate;
	private String  stHOSuccessRate;

	private String idleRsrpRate;
	private String  macDlThp;
	private String  macUlThp;
	private String   avgMacDl;
	private String  avgMacUl;
	  
	private String   volteSetupSuccessRate;
	private String   volteSetupTime;
	private String  imsRegistrationSetupTime;
	private String   volteJitter;
	private String   volteLatency;
	private String volteCallDropRate;
	private String  rtpPacketLossRate;
	private String  qualitySinrRate;
	private String opServer3Db;
	private String   opServer5Db;
	private String  noOfCqiGT8;
	private String  noOfCqiGT12;
	private String  avgBlerDl;
	private String   avgBlerUl;
	
	private String  macroVolteHOSuccessRate;
	private String   scVolteHOSuccessRate;
	private String volteHoInterruptionTime;
     
	private String  macroDataHOSuccessRate;
	private String  scDataHOSuccessRate;
	private String  dataHoInterruptionTime;
     
	private String pciRateForSc;
	private String cellSelectionRate;
	private String cellReSelectionRate;
	private String atSuccessRate;
	private String dtSuccessRate;
	private String rachSuccessRate;
	
	private List<BenchMarkOperatorInfo>imgKpiList;
	public String getFloorName() {
		return floorName;
	}
	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	public String getCriteriaPeakDL() {
		return criteriaPeakDL;
	}
	public void setCriteriaPeakDL(String criteriaPeakDL) {
		this.criteriaPeakDL = criteriaPeakDL;
	}
	public String getCriteriaPeakUL() {
		return criteriaPeakUL;
	}
	public void setCriteriaPeakUL(String criteriaPeakUL) {
		this.criteriaPeakUL = criteriaPeakUL;
	}
	public String getCriteriaLatency() {
		return criteriaLatency;
	}
	public void setCriteriaLatency(String criteriaLatency) {
		this.criteriaLatency = criteriaLatency;
	}
	public String getCriteriaJitter() {
		return criteriaJitter;
	}
	public void setCriteriaJitter(String criteriaJitter) {
		this.criteriaJitter = criteriaJitter;
	}
	public List<InbuildingDataWrapper> getList() {
		return list;
	}
	public void setList(List<InbuildingDataWrapper> list) {
		this.list = list;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getSpRsrp() {
		return spRsrp;
	}
	public void setSpRsrp(String spRsrp) {
		this.spRsrp = spRsrp;
	}
	public String getSpSinr() {
		return spSinr;
	}
	public void setSpSinr(String spSinr) {
		this.spSinr = spSinr;
	}
	public String getVolteInOutHOSuccessRate() {
		return volteInOutHOSuccessRate;
	}
	public void setVolteInOutHOSuccessRate(String volteInOutHOSuccessRate) {
		this.volteInOutHOSuccessRate = volteInOutHOSuccessRate;
	}
	public String getInOutHOSuccessRate() {
		return inOutHOSuccessRate;
	}
	public void setInOutHOSuccessRate(String inOutHOSuccessRate) {
		this.inOutHOSuccessRate = inOutHOSuccessRate;
	}
	public String getVolteElevHOSuccessRate() {
		return volteElevHOSuccessRate;
	}
	public void setVolteElevHOSuccessRate(String volteElevHOSuccessRate) {
		this.volteElevHOSuccessRate = volteElevHOSuccessRate;
	}
	public String getElevHOSuccessRate() {
		return elevHOSuccessRate;
	}
	public void setElevHOSuccessRate(String elevHOSuccessRate) {
		this.elevHOSuccessRate = elevHOSuccessRate;
	}
	public String getVolteStHOSuccessRate() {
		return volteStHOSuccessRate;
	}
	public void setVolteStHOSuccessRate(String volteStHOSuccessRate) {
		this.volteStHOSuccessRate = volteStHOSuccessRate;
	}
	public String getStHOSuccessRate() {
		return stHOSuccessRate;
	}
	public void setStHOSuccessRate(String stHOSuccessRate) {
		this.stHOSuccessRate = stHOSuccessRate;
	}
	public String getIdleRsrpRate() {
		return idleRsrpRate;
	}
	public void setIdleRsrpRate(String idleRsrpRate) {
		this.idleRsrpRate = idleRsrpRate;
	}
	public String getMacDlThp() {
		return macDlThp;
	}
	public void setMacDlThp(String macDlThp) {
		this.macDlThp = macDlThp;
	}
	public String getMacUlThp() {
		return macUlThp;
	}
	public void setMacUlThp(String macUlThp) {
		this.macUlThp = macUlThp;
	}
	public String getAvgMacDl() {
		return avgMacDl;
	}
	public void setAvgMacDl(String avgMacDl) {
		this.avgMacDl = avgMacDl;
	}
	public String getAvgMacUl() {
		return avgMacUl;
	}
	public void setAvgMacUl(String avgMacUl) {
		this.avgMacUl = avgMacUl;
	}
	public String getVolteSetupSuccessRate() {
		return volteSetupSuccessRate;
	}
	public void setVolteSetupSuccessRate(String volteSetupSuccessRate) {
		this.volteSetupSuccessRate = volteSetupSuccessRate;
	}
	public String getVolteSetupTime() {
		return volteSetupTime;
	}
	public void setVolteSetupTime(String volteSetupTime) {
		this.volteSetupTime = volteSetupTime;
	}
	public String getImsRegistrationSetupTime() {
		return imsRegistrationSetupTime;
	}
	public void setImsRegistrationSetupTime(String imsRegistrationSetupTime) {
		this.imsRegistrationSetupTime = imsRegistrationSetupTime;
	}
	public String getVolteJitter() {
		return volteJitter;
	}
	public void setVolteJitter(String volteJitter) {
		this.volteJitter = volteJitter;
	}
	public String getVolteLatency() {
		return volteLatency;
	}
	public void setVolteLatency(String volteLatency) {
		this.volteLatency = volteLatency;
	}
	public String getVolteCallDropRate() {
		return volteCallDropRate;
	}
	public void setVolteCallDropRate(String volteCallDropRate) {
		this.volteCallDropRate = volteCallDropRate;
	}
	public String getQualitySinrRate() {
		return qualitySinrRate;
	}
	public void setQualitySinrRate(String qualitySinrRate) {
		this.qualitySinrRate = qualitySinrRate;
	}
	public String getOpServer3Db() {
		return opServer3Db;
	}
	public void setOpServer3Db(String opServer3Db) {
		this.opServer3Db = opServer3Db;
	}
	public String getOpServer5Db() {
		return opServer5Db;
	}
	public void setOpServer5Db(String opServer5Db) {
		this.opServer5Db = opServer5Db;
	}
	public String getNoOfCqiGT8() {
		return noOfCqiGT8;
	}
	public void setNoOfCqiGT8(String noOfCqiGT8) {
		this.noOfCqiGT8 = noOfCqiGT8;
	}
	public String getNoOfCqiGT12() {
		return noOfCqiGT12;
	}
	public void setNoOfCqiGT12(String noOfCqiGT12) {
		this.noOfCqiGT12 = noOfCqiGT12;
	}
	public String getAvgBlerDl() {
		return avgBlerDl;
	}
	public void setAvgBlerDl(String avgBlerDl) {
		this.avgBlerDl = avgBlerDl;
	}
	public String getAvgBlerUl() {
		return avgBlerUl;
	}
	public void setAvgBlerUl(String avgBlerUl) {
		this.avgBlerUl = avgBlerUl;
	}
	public String getMacroVolteHOSuccessRate() {
		return macroVolteHOSuccessRate;
	}
	public void setMacroVolteHOSuccessRate(String macroVolteHOSuccessRate) {
		this.macroVolteHOSuccessRate = macroVolteHOSuccessRate;
	}
	public String getScVolteHOSuccessRate() {
		return scVolteHOSuccessRate;
	}
	public void setScVolteHOSuccessRate(String scVolteHOSuccessRate) {
		this.scVolteHOSuccessRate = scVolteHOSuccessRate;
	}
	public String getVolteHoInterruptionTime() {
		return volteHoInterruptionTime;
	}
	public void setVolteHoInterruptionTime(String volteHoInterruptionTime) {
		this.volteHoInterruptionTime = volteHoInterruptionTime;
	}
	public String getMacroDataHOSuccessRate() {
		return macroDataHOSuccessRate;
	}
	public void setMacroDataHOSuccessRate(String macroDataHOSuccessRate) {
		this.macroDataHOSuccessRate = macroDataHOSuccessRate;
	}
	public String getScDataHOSuccessRate() {
		return scDataHOSuccessRate;
	}
	public void setScDataHOSuccessRate(String scDataHOSuccessRate) {
		this.scDataHOSuccessRate = scDataHOSuccessRate;
	}
	public String getDataHoInterruptionTime() {
		return dataHoInterruptionTime;
	}
	public void setDataHoInterruptionTime(String dataHoInterruptionTime) {
		this.dataHoInterruptionTime = dataHoInterruptionTime;
	}
	public String getPciRateForSc() {
		return pciRateForSc;
	}
	public void setPciRateForSc(String pciRateForSc) {
		this.pciRateForSc = pciRateForSc;
	}
	public String getCellSelectionRate() {
		return cellSelectionRate;
	}
	public void setCellSelectionRate(String cellSelectionRate) {
		this.cellSelectionRate = cellSelectionRate;
	}
	public String getCellReSelectionRate() {
		return cellReSelectionRate;
	}
	public void setCellReSelectionRate(String cellReSelectionRate) {
		this.cellReSelectionRate = cellReSelectionRate;
	}
	public String getAtSuccessRate() {
		return atSuccessRate;
	}
	public void setAtSuccessRate(String atSuccessRate) {
		this.atSuccessRate = atSuccessRate;
	}
	public String getDtSuccessRate() {
		return dtSuccessRate;
	}
	public void setDtSuccessRate(String dtSuccessRate) {
		this.dtSuccessRate = dtSuccessRate;
	}
	public String getRachSuccessRate() {
		return rachSuccessRate;
	}
	public void setRachSuccessRate(String rachSuccessRate) {
		this.rachSuccessRate = rachSuccessRate;
	}
	public List<BenchMarkOperatorInfo> getImgKpiList() {
		return imgKpiList;
	}
	public void setImgKpiList(List<BenchMarkOperatorInfo> imgKpiList) {
		this.imgKpiList = imgKpiList;
	}
	public String getRtpPacketLossRate() {
		return rtpPacketLossRate;
	}
	public void setRtpPacketLossRate(String rtpPacketLossRate) {
		this.rtpPacketLossRate = rtpPacketLossRate;
	}
	public String getSpPci() {
		return spPci;
	}
	public void setSpPci(String spPci) {
		this.spPci = spPci;
	}
	

}
