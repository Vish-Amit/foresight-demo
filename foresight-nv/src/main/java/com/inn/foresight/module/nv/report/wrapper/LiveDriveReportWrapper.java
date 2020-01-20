package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.module.nv.livedrive.wrapper.DeviceDataWapper;

@JpaWrapper
public class LiveDriveReportWrapper {

	private String testDate;
	private List<LiveDriveSubWrapper> nhi2DataList;

	private List<LiveDriveSubWrapper> rsrpSinrChartBeanList;
	private List<LiveDriveSubWrapper> dlUlChartBeanList;
	private List<LiveDriveSubWrapper> dlUlHttpChartBeanList;
	private List<LiveDriveSubWrapper> dlUlFtpChartBeanList;
	private List<DeviceDataWapper> handoverDataList;

	private String fileName;
	private String youTubePage;
	private String callPage;
	private String smsPage;
	private String quicTestPage;
	private String geograhyName;
	private String geographyType;
	private String httpLinkDlPage;
	private List<Double[]> pinPointList;

	private String handOverPage;
	
	private List<PSDataWrapper> listPsDataWrapper;

	public String getHttpLinkDlPage() {
		return httpLinkDlPage;
	}

	public void setHttpLinkDlPage(String httpLinkDlPage) {
		this.httpLinkDlPage = httpLinkDlPage;
	}

	public String getHandOverPage() {
		return handOverPage;
	}

	public void setHandOverPage(String handOverPage) {
		this.handOverPage = handOverPage;
	}

	public String getTestDate() {
		return testDate;
	}

	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	public List<LiveDriveSubWrapper> getNhi2DataList() {
		return nhi2DataList;
	}

	public void setNhi2DataList(List<LiveDriveSubWrapper> nhi2DataList) {
		this.nhi2DataList = nhi2DataList;
	}

	public List<LiveDriveSubWrapper> getRsrpSinrChartBeanList() {
		return rsrpSinrChartBeanList;
	}

	public void setRsrpSinrChartBeanList(List<LiveDriveSubWrapper> rsrpSinrChartBeanList) {
		this.rsrpSinrChartBeanList = rsrpSinrChartBeanList;
	}

	public List<LiveDriveSubWrapper> getDlUlChartBeanList() {
		return dlUlChartBeanList;
	}

	public void setDlUlChartBeanList(List<LiveDriveSubWrapper> dlUlChartBeanList) {
		this.dlUlChartBeanList = dlUlChartBeanList;
	}

	public List<LiveDriveSubWrapper> getDlUlHttpChartBeanList() {
		return dlUlHttpChartBeanList;
	}

	public void setDlUlHttpChartBeanList(List<LiveDriveSubWrapper> dlUlHttpChartBeanList) {
		this.dlUlHttpChartBeanList = dlUlHttpChartBeanList;
	}

	public List<LiveDriveSubWrapper> getDlUlFtpChartBeanList() {
		return dlUlFtpChartBeanList;
	}

	public void setDlUlFtpChartBeanList(List<LiveDriveSubWrapper> dlUlFtpChartBeanList) {
		this.dlUlFtpChartBeanList = dlUlFtpChartBeanList;
	}

	public List<DeviceDataWapper> getHandoverDataList() {
		return handoverDataList;
	}

	public void setHandoverDataList(List<DeviceDataWapper> handoverDataList) {
		this.handoverDataList = handoverDataList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getYouTubePage() {
		return youTubePage;
	}

	public void setYouTubePage(String youTubePage) {
		this.youTubePage = youTubePage;
	}

	public String getCallPage() {
		return callPage;
	}

	public void setCallPage(String callPage) {
		this.callPage = callPage;
	}

	public String getSmsPage() {
		return smsPage;
	}

	public void setSmsPage(String smsPage) {
		this.smsPage = smsPage;
	}

	public String getQuicTestPage() {
		return quicTestPage;
	}

	public void setQuicTestPage(String quicTestPage) {
		this.quicTestPage = quicTestPage;
	}

	public String getGeograhyName() {
		return geograhyName;
	}

	public void setGeograhyName(String geograhyName) {
		this.geograhyName = geograhyName;
	}

	public String getGeographyType() {
		return geographyType;
	}

	public void setGeographyType(String geographyType) {
		this.geographyType = geographyType;
	}

	public List<Double[]> getPinPointList() {
		return pinPointList;
	}

	public void setPinPointList(List<Double[]> pinPointList) {
		this.pinPointList = pinPointList;
	}

	public List<PSDataWrapper> getListPsDataWrapper() {
		return listPsDataWrapper;
	}

	public void setListPsDataWrapper(List<PSDataWrapper> listPsDataWrapper) {
		this.listPsDataWrapper = listPsDataWrapper;
	}

	@Override
	public String toString() {
		return "LiveDriveReportWrapper [testDate=" + testDate + ", nhi2DataList=" + nhi2DataList
				+ ", rsrpSinrChartBeanList=" + rsrpSinrChartBeanList + ", dlUlChartBeanList=" + dlUlChartBeanList
				+ ", dlUlHttpChartBeanList=" + dlUlHttpChartBeanList + ", dlUlFtpChartBeanList=" + dlUlFtpChartBeanList
				+ ", handoverDataList=" + handoverDataList + ", fileName=" + fileName + ", youTubePage=" + youTubePage
				+ ", callPage=" + callPage + ", smsPage=" + smsPage + ", quicTestPage=" + quicTestPage
				+ ", geograhyName=" + geograhyName + ", geographyType=" + geographyType + ", httpLinkDlPage="
				+ httpLinkDlPage + ", pinPointList=" + pinPointList + ", handOverPage=" + handOverPage
				+ ", psDataWrapper=" + listPsDataWrapper + "]";
	}

}
