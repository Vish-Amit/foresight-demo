package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

import com.inn.foresight.module.fm.core.wrapper.AlarmDataWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.SSVTCellWiseWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.HandOverDataWrappr;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.LiveDriveVoiceAndSmsWrapper;

/** The Class SSVTReportSubWrapper. */
public class SSVTReportSubWrapper {

/** The site info list. */
private List<SiteInformationWrapper>siteInfoList;

/** The kpi info list. */
private List<KPISummaryDataWrapper>kpiInfoList;
private List<GraphWrapper>rsrpList;
private List<GraphWrapper>sinrList;
private List<GraphWrapper>dlList;
private List<GraphWrapper>ulList;
private List<GraphWrapper>httpRsrpDlList;
private List<GraphWrapper>ftpRsrpDlList;
private List<GraphWrapper>httpRsrpUlList;
private List<GraphWrapper>ftpRsrpUlList;
private List<GraphWrapper>ftpSinrDlList;
private List<GraphWrapper>httpSinrDlList;
private List<GraphWrapper>ftpSinrUlList;
private List<GraphWrapper>httpSinrUlList;
private List<SectorSwapWrapper> sectorSwapList;


private List<GraphDataWrapper>earfcnList;
private List<GraphDataWrapper>earfcnPostDataList;
private List<LiveDriveVoiceAndSmsWrapper> handoverPlotDataList;
private List<LiveDriveVoiceAndSmsWrapper> postHandoverPlotDataList;

private List<LiveDriveVoiceAndSmsWrapper> callDataList;
private List<LiveDriveVoiceAndSmsWrapper> callPlotDataList;
/** The avg rsrp. */
private Double avgRsrp;

/** The avg sinr. */
private Double avgSinr;

/** The avg dl. */
private Double avgDl;

/** The avg ul. */
private Double avgUl;

/** The test date. */
private String testDate;

private Double rrcReqSuccessRate;
private Double rrcAttempt;
private Double rrcConnect;
private Double postRrcReqSuccessRate;
private Double postRrcAttempt;
private Double postRrcConnect;

private List<AlarmDataWrapper> listOfNeHaveAlarm;

List<LiveDriveVoiceAndSmsWrapper> listOfVoiceSmSData;
List<HandOverDataWrappr> handOverIdelList;
private List<KPIImgDataWrapper>imgList;

private List<ComparisoGraphWrapper>graphDataList;

private String tempFilePath;

private String heading ;

// SSVT IBC REPORT Field

private List<IBCReportWrapper>ibcRsrpList;
private List<IBCReportWrapper>ibcRRCList;
private List<IBCReportWrapper>ibcPingList;
private List<IBCReportWrapper>ibcVolteList;
private List<IBCReportWrapper>ibcErabList;
private List<IBCReportWrapper>ibcPacketLossList;
private List<IBCReportWrapper>ibcDLList;
private List<IBCReportWrapper>ibcULList;

private List<HandOverDataWrappr>handoverInList;
private List<HandOverDataWrappr>handoverOutList;
private List<HandOverDataWrappr>volteHOInList;
private List<HandOverDataWrappr>voletHOOutList;

/// SSVT EXCEL//////////////////
List<SSVTCellWiseWrapper> cellList;
List<SSVTCellWiseWrapper> volteDataList;
String siteId;
String siteName;
String cellId;
private List<GraphWrapper> graphplotList;
private List<GraphWrapper> voltePlotList;
private List<GraphWrapper> stationaryDataList;
private List<SectorSwapWrapper> atSectorWiseMessagList;
private List<SectorSwapWrapper> dtSectorWiseMessagList;
private List<SectorSwapWrapper> csfbMOSectorWiseMessageList;
private List<SectorSwapWrapper> csfbMTSectorWiseMessageList;
private List<SectorSwapWrapper> fRTSectorWiseMessagList;
private List<SectorSwapWrapper> volteMtSectorWiseMessagList;
private List<SectorSwapWrapper> volteMoSectorWiseMessagList;
private List<SectorSwapWrapper> rrcWiseMessagList;



	

public String getTempFilePath() {
	return tempFilePath;
}

public void setTempFilePath(String tempFilePath) {
	this.tempFilePath = tempFilePath;
}

public List<HandOverDataWrappr> getHandOverIdelList() {
	return handOverIdelList;
}

public void setHandOverIdelList(List<HandOverDataWrappr> handOverIdelList) {
	this.handOverIdelList = handOverIdelList;
}

public List<LiveDriveVoiceAndSmsWrapper> getListOfVoiceSmSData() {
	return listOfVoiceSmSData;
}

public void setListOfVoiceSmSData(List<LiveDriveVoiceAndSmsWrapper> listOfVoiceSmSData) {
	this.listOfVoiceSmSData = listOfVoiceSmSData;
}

public List<AlarmDataWrapper> getListOfNeHaveAlarm() {
	return listOfNeHaveAlarm;
}

public void setListOfNeHaveAlarm(List<AlarmDataWrapper> listOfNeHaveAlarm) {
	this.listOfNeHaveAlarm = listOfNeHaveAlarm;
}

/**
 * Gets the test date.
 *
 * @return the test date
 */
public String getTestDate() {
	return testDate;
}

/**
 * Sets the test date.
 *
 * @param testDate the new test date
 */
public void setTestDate(String testDate) {
	this.testDate = testDate;
}

/**
 * Gets the site info list.
 *
 * @return the site info list
 */
public List<SiteInformationWrapper> getSiteInfoList() {
	return siteInfoList;
}

/**
 * Sets the site info list.
 *
 * @param siteInfoList the new site info list
 */
public void setSiteInfoList(List<SiteInformationWrapper> siteInfoList) {
	this.siteInfoList = siteInfoList;
}

/**
 * Gets the kpi info list.
 *
 * @return the kpi info list
 */
public List<KPISummaryDataWrapper> getKpiInfoList() {
	return kpiInfoList;
}

/**
 * Sets the kpi info list.
 *
 * @param kpiInfoList the new kpi info list
 */
public void setKpiInfoList(List<KPISummaryDataWrapper> kpiInfoList) {
	this.kpiInfoList = kpiInfoList;
}


/**
 * Gets the avg rsrp.
 *
 * @return the avg rsrp
 */
public Double getAvgRsrp() {
	return avgRsrp;
}

/**
 * Sets the avg rsrp.
 *
 * @param avgRsrp the new avg rsrp
 */
public void setAvgRsrp(Double avgRsrp) {
	this.avgRsrp = avgRsrp;
}

/**
 * Gets the avg sinr.
 *
 * @return the avg sinr
 */
public Double getAvgSinr() {
	return avgSinr;
}

/**
 * Sets the avg sinr.
 *
 * @param avgSinr the new avg sinr
 */
public void setAvgSinr(Double avgSinr) {
	this.avgSinr = avgSinr;
}

/**
 * Gets the avg dl.
 *
 * @return the avg dl
 */
public Double getAvgDl() {
	return avgDl;
}

/**
 * Sets the avg dl.
 *
 * @param avgDl the new avg dl
 */
public void setAvgDl(Double avgDl) {
	this.avgDl = avgDl;
}

/**
 * Gets the avg ul.
 *
 * @return the avg ul
 */
public Double getAvgUl() {
	return avgUl;
}

/**
 * Sets the avg ul.
 *
 * @param avgUl the new avg ul
 */
public void setAvgUl(Double avgUl) {
	this.avgUl = avgUl;
}


public List<GraphWrapper> getRsrpList() {
	return rsrpList;
}

public void setRsrpList(List<GraphWrapper> rsrpList) {
	this.rsrpList = rsrpList;
}

public List<GraphWrapper> getSinrList() {
	return sinrList;
}

public void setSinrList(List<GraphWrapper> sinrList) {
	this.sinrList = sinrList;
}

public List<GraphWrapper> getDlList() {
	return dlList;
}

public void setDlList(List<GraphWrapper> dlList) {
	this.dlList = dlList;
}

public List<GraphWrapper> getUlList() {
	return ulList;
}

public void setUlList(List<GraphWrapper> ulList) {
	this.ulList = ulList;
}

public List<GraphWrapper> getHttpRsrpDlList() {
	return httpRsrpDlList;
}

public void setHttpRsrpDlList(List<GraphWrapper> httpRsrpDlList) {
	this.httpRsrpDlList = httpRsrpDlList;
}

public List<GraphWrapper> getFtpRsrpDlList() {
	return ftpRsrpDlList;
}

public void setFtpRsrpDlList(List<GraphWrapper> ftpRsrpDlList) {
	this.ftpRsrpDlList = ftpRsrpDlList;
}

public List<GraphWrapper> getHttpRsrpUlList() {
	return httpRsrpUlList;
}

public void setHttpRsrpUlList(List<GraphWrapper> httpRsrpUlList) {
	this.httpRsrpUlList = httpRsrpUlList;
}

public List<GraphWrapper> getFtpRsrpUlList() {
	return ftpRsrpUlList;
}

public void setFtpRsrpUlList(List<GraphWrapper> ftpRsrpUlList) {
	this.ftpRsrpUlList = ftpRsrpUlList;
}

public List<GraphWrapper> getFtpSinrDlList() {
	return ftpSinrDlList;
}

public void setFtpSinrDlList(List<GraphWrapper> ftpSinrDlList) {
	this.ftpSinrDlList = ftpSinrDlList;
}

public List<GraphWrapper> getHttpSinrDlList() {
	return httpSinrDlList;
}

public void setHttpSinrDlList(List<GraphWrapper> httpSinrDlList) {
	this.httpSinrDlList = httpSinrDlList;
}

public List<GraphWrapper> getFtpSinrUlList() {
	return ftpSinrUlList;
}

public void setFtpSinrUlList(List<GraphWrapper> ftpSinrUlList) {
	this.ftpSinrUlList = ftpSinrUlList;
}

public List<GraphWrapper> getHttpSinrUlList() {
	return httpSinrUlList;
}

public void setHttpSinrUlList(List<GraphWrapper> httpSinrUlList) {
	this.httpSinrUlList = httpSinrUlList;
}


public List<SectorSwapWrapper> getSectorSwapList() {
	return sectorSwapList;
}

public void setSectorSwapList(List<SectorSwapWrapper> sectorSwapList) {
	this.sectorSwapList = sectorSwapList;
}


public List<GraphDataWrapper> getEarfcnList() {
	return earfcnList;
}

public void setEarfcnList(List<GraphDataWrapper> earfcnList) {
	this.earfcnList = earfcnList;
}


public List<LiveDriveVoiceAndSmsWrapper> getHandoverPlotDataList() {
	return handoverPlotDataList;
}

public void setHandoverPlotDataList(List<LiveDriveVoiceAndSmsWrapper> handoverPlotDataList) {
	this.handoverPlotDataList = handoverPlotDataList;
}


public Double getRrcReqSuccessRate() {
	return rrcReqSuccessRate;
}

public void setRrcReqSuccessRate(Double rrcReqSuccessRate) {
	this.rrcReqSuccessRate = rrcReqSuccessRate;
}

public Double getRrcAttempt() {
	return rrcAttempt;
}

public void setRrcAttempt(Double rrcAttempt) {
	this.rrcAttempt = rrcAttempt;
}

public Double getRrcConnect() {
	return rrcConnect;
}

public void setRrcConnect(Double rrcConnect) {
	this.rrcConnect = rrcConnect;
}


public List<LiveDriveVoiceAndSmsWrapper> getCallDataList() {
	return callDataList;
}

public void setCallDataList(List<LiveDriveVoiceAndSmsWrapper> callDataList) {
	this.callDataList = callDataList;
}

public List<LiveDriveVoiceAndSmsWrapper> getCallPlotDataList() {
	return callPlotDataList;
}

public void setCallPlotDataList(List<LiveDriveVoiceAndSmsWrapper> callPlotDataList) {
	this.callPlotDataList = callPlotDataList;
}


public List<KPIImgDataWrapper> getImgList() {
	return imgList;
}

public void setImgList(List<KPIImgDataWrapper> imgList) {
	this.imgList = imgList;
}


public List<ComparisoGraphWrapper> getGraphDataList() {
	return graphDataList;
}

public void setGraphDataList(List<ComparisoGraphWrapper> graphDataList) {
	this.graphDataList = graphDataList;
}


public List<GraphDataWrapper> getEarfcnPostDataList() {
	return earfcnPostDataList;
}

public void setEarfcnPostDataList(List<GraphDataWrapper> earfcnPostDataList) {
	this.earfcnPostDataList = earfcnPostDataList;
}


public List<LiveDriveVoiceAndSmsWrapper> getPostHandoverPlotDataList() {
	return postHandoverPlotDataList;
}

public void setPostHandoverPlotDataList(List<LiveDriveVoiceAndSmsWrapper> postHandoverPlotDataList) {
	this.postHandoverPlotDataList = postHandoverPlotDataList;
}


public Double getPostRrcReqSuccessRate() {
	return postRrcReqSuccessRate;
}

public void setPostRrcReqSuccessRate(Double postRrcReqSuccessRate) {
	this.postRrcReqSuccessRate = postRrcReqSuccessRate;
}

public Double getPostRrcAttempt() {
	return postRrcAttempt;
}

public void setPostRrcAttempt(Double postRrcAttempt) {
	this.postRrcAttempt = postRrcAttempt;
}

public Double getPostRrcConnect() {
	return postRrcConnect;
}

public void setPostRrcConnect(Double postRrcConnect) {
	this.postRrcConnect = postRrcConnect;
}


public String getHeading() {
	return heading;
}

public void setHeading(String heading) {
	this.heading = heading;
}

public List<IBCReportWrapper> getIbcRsrpList() {
	return ibcRsrpList;
}

public void setIbcRsrpList(List<IBCReportWrapper> ibcRsrpList) {
	this.ibcRsrpList = ibcRsrpList;
}

public List<IBCReportWrapper> getIbcRRCList() {
	return ibcRRCList;
}

public void setIbcRRCList(List<IBCReportWrapper> ibcRRCList) {
	this.ibcRRCList = ibcRRCList;
}

public List<IBCReportWrapper> getIbcPingList() {
	return ibcPingList;
}

public void setIbcPingList(List<IBCReportWrapper> ibcPingList) {
	this.ibcPingList = ibcPingList;
}

public List<IBCReportWrapper> getIbcVolteList() {
	return ibcVolteList;
}

public void setIbcVolteList(List<IBCReportWrapper> ibcVolteList) {
	this.ibcVolteList = ibcVolteList;
}

public List<IBCReportWrapper> getIbcErabList() {
	return ibcErabList;
}

public void setIbcErabList(List<IBCReportWrapper> ibcErabList) {
	this.ibcErabList = ibcErabList;
}

public List<IBCReportWrapper> getIbcPacketLossList() {
	return ibcPacketLossList;
}

public void setIbcPacketLossList(List<IBCReportWrapper> ibcPacketLossList) {
	this.ibcPacketLossList = ibcPacketLossList;
}

public List<IBCReportWrapper> getIbcDLList() {
	return ibcDLList;
}

public void setIbcDLList(List<IBCReportWrapper> ibcDLList) {
	this.ibcDLList = ibcDLList;
}

public List<IBCReportWrapper> getIbcULList() {
	return ibcULList;
}

public void setIbcULList(List<IBCReportWrapper> ibcULList) {
	this.ibcULList = ibcULList;
}

public List<HandOverDataWrappr> getHandoverInList() {
	return handoverInList;
}

public void setHandoverInList(List<HandOverDataWrappr> handoverInList) {
	this.handoverInList = handoverInList;
}

public List<HandOverDataWrappr> getHandoverOutList() {
	return handoverOutList;
}

public void setHandoverOutList(List<HandOverDataWrappr> handoverOutList) {
	this.handoverOutList = handoverOutList;
}

public List<HandOverDataWrappr> getVolteHOInList() {
	return volteHOInList;
}

public void setVolteHOInList(List<HandOverDataWrappr> volteHOInList) {
	this.volteHOInList = volteHOInList;
}

public List<HandOverDataWrappr> getVoletHOOutList() {
	return voletHOOutList;
}

public void setVoletHOOutList(List<HandOverDataWrappr> voletHOOutList) {
	this.voletHOOutList = voletHOOutList;
}

public List<SSVTCellWiseWrapper> getCellList() {
	return cellList;
}

public void setCellList(List<SSVTCellWiseWrapper> cellList) {
	this.cellList = cellList;
}

public List<SSVTCellWiseWrapper> getVolteDataList() {
	return volteDataList;
}

public void setVolteDataList(List<SSVTCellWiseWrapper> volteDataList) {
	this.volteDataList = volteDataList;
}

public String getSiteId() {
	return siteId;
}

public void setSiteId(String siteId) {
	this.siteId = siteId;
}

public String getSiteName() {
	return siteName;
}

public void setSiteName(String siteName) {
	this.siteName = siteName;
}

public String getCellId() {
	return cellId;
}

public void setCellId(String cellId) {
	this.cellId = cellId;
}

public List<GraphWrapper> getGraphplotList() {
	return graphplotList;
}

public void setGraphplotList(List<GraphWrapper> graphplotList) {
	this.graphplotList = graphplotList;
}

public List<GraphWrapper> getStationaryDataList() {
	return stationaryDataList;
}

public void setStationaryDataList(List<GraphWrapper> stationaryDataList) {
	this.stationaryDataList = stationaryDataList;
}

public List<GraphWrapper> getVoltePlotList() {
	return voltePlotList;
}

public void setVoltePlotList(List<GraphWrapper> voltePlotList) {
	this.voltePlotList = voltePlotList;
}

public List<SectorSwapWrapper> getAtSectorWiseMessagList() {
	return atSectorWiseMessagList;
}

public void setAtSectorWiseMessagList(List<SectorSwapWrapper> atSectorWiseMessagList) {
	this.atSectorWiseMessagList = atSectorWiseMessagList;
}

public List<SectorSwapWrapper> getDtSectorWiseMessagList() {
	return dtSectorWiseMessagList;
}

public void setDtSectorWiseMessagList(List<SectorSwapWrapper> dtSectorWiseMessagList) {
	this.dtSectorWiseMessagList = dtSectorWiseMessagList;
}

public List<SectorSwapWrapper> getCsfbMOSectorWiseMessageList() {
	return csfbMOSectorWiseMessageList;
}

public void setCsfbMOSectorWiseMessageList(List<SectorSwapWrapper> csfbMOSectorWiseMessageList) {
	this.csfbMOSectorWiseMessageList = csfbMOSectorWiseMessageList;
}

public List<SectorSwapWrapper> getCsfbMTSectorWiseMessageList() {
	return csfbMTSectorWiseMessageList;
}

public void setCsfbMTSectorWiseMessageList(List<SectorSwapWrapper> csfbMTSectorWiseMessageList) {
	this.csfbMTSectorWiseMessageList = csfbMTSectorWiseMessageList;
}

public List<SectorSwapWrapper> getfRTSectorWiseMessagList() {
	return fRTSectorWiseMessagList;
}

public void setfRTSectorWiseMessagList(List<SectorSwapWrapper> fRTSectorWiseMessagList) {
	this.fRTSectorWiseMessagList = fRTSectorWiseMessagList;
}

public List<SectorSwapWrapper> getVolteMtSectorWiseMessagList() {
	return volteMtSectorWiseMessagList;
}

public void setVolteMtSectorWiseMessagList(List<SectorSwapWrapper> volteMtSectorWiseMessagList) {
	this.volteMtSectorWiseMessagList = volteMtSectorWiseMessagList;
}

public List<SectorSwapWrapper> getVolteMoSectorWiseMessagList() {
	return volteMoSectorWiseMessagList;
}

public void setVolteMoSectorWiseMessagList(List<SectorSwapWrapper> volteMoSectorWiseMessagList) {
	this.volteMoSectorWiseMessagList = volteMoSectorWiseMessagList;
}

public List<SectorSwapWrapper> getRrcWiseMessagList() {
	return rrcWiseMessagList;
}

public void setRrcWiseMessagList(List<SectorSwapWrapper> rrcWiseMessagList) {
	this.rrcWiseMessagList = rrcWiseMessagList;
}
}
