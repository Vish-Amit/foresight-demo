package com.inn.foresight.module.nv.report.wrapper.benchmark;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class BenchMarkReportWrapper {

	private String testDate;
	private List<BenchMarksubwrapper> subList;
	private boolean showRsrpPage;
	private boolean showSinrPage;
	private boolean showDLPage;
	private boolean showUlPage;
	private boolean showMcsPage;
	private boolean showcqiPage;
	private boolean showMimoPage;
	private boolean showpciPage;
	private boolean showSitesPage;
	private boolean showServingSystemPage;
	private boolean showCaPage;
	private boolean showDlBandwidthPage;
	private boolean showMosPage;

	public boolean isShowRsrpPage() {
		return showRsrpPage;
	}

	public void setShowRsrpPage(boolean showRsrpPage) {
		this.showRsrpPage = showRsrpPage;
	}

	public boolean isShowSinrPage() {
		return showSinrPage;
	}

	public void setShowSinrPage(boolean showSinrPage) {
		this.showSinrPage = showSinrPage;
	}

	public boolean isShowDLPage() {
		return showDLPage;
	}

	public void setShowDLPage(boolean showDLPage) {
		this.showDLPage = showDLPage;
	}

	public boolean isShowUlPage() {
		return showUlPage;
	}

	public void setShowUlPage(boolean showUlPage) {
		this.showUlPage = showUlPage;
	}

	public boolean isShowMcsPage() {
		return showMcsPage;
	}

	public void setShowMcsPage(boolean showMcsPage) {
		this.showMcsPage = showMcsPage;
	}

	public boolean isShowcqiPage() {
		return showcqiPage;
	}

	public void setShowcqiPage(boolean showcqiPage) {
		this.showcqiPage = showcqiPage;
	}

	public boolean isShowMimoPage() {
		return showMimoPage;
	}

	public void setShowMimoPage(boolean showMimoPage) {
		this.showMimoPage = showMimoPage;
	}

	public boolean isShowpciPage() {
		return showpciPage;
	}

	public void setShowpciPage(boolean showpciPage) {
		this.showpciPage = showpciPage;
	}

	public String getTestDate() {
		return testDate;
	}

	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	public List<BenchMarksubwrapper> getSubList() {
		return subList;
	}

	public void setSubList(List<BenchMarksubwrapper> subList) {
		this.subList = subList;
	}

	public boolean isShowSitesPage() {
		return showSitesPage;
	}

	public void setShowSitesPage(boolean showSitesPage) {
		this.showSitesPage = showSitesPage;
	}

	public boolean isShowServingSystemPage() {
		return showServingSystemPage;
	}

	public void setShowServingSystemPage(boolean showServingSystemPage) {
		this.showServingSystemPage = showServingSystemPage;
	}

	public boolean isShowCaPage() {
		return showCaPage;
	}

	public void setShowCaPage(boolean showCaPage) {
		this.showCaPage = showCaPage;
	}

	public boolean isShowDlBandwidthPage() {
		return showDlBandwidthPage;
	}

	public void setShowDlBandwidthPage(boolean showDlBandwidthPage) {
		this.showDlBandwidthPage = showDlBandwidthPage;
	}

	public boolean isShowMosPage() {
		return showMosPage;
	}

	public void setShowMosPage(boolean showMosPage) {
		this.showMosPage = showMosPage;
	}

	@Override
	public String toString() {
		return "BenchMarkReportWrapper [testDate=" + testDate + ", subList=" + subList + ", showRsrpPage="
				+ showRsrpPage + ", showSinrPage=" + showSinrPage + ", showDLPage=" + showDLPage + ", showUlPage="
				+ showUlPage + ", showMcsPage=" + showMcsPage + ", showcqiPage=" + showcqiPage + ", showMimoPage="
				+ showMimoPage + ", showpciPage=" + showpciPage + ", showSitesPage=" + showSitesPage
				+ ", showServingSystemPage=" + showServingSystemPage + ", showCaPage=" + showCaPage
				+ ", showDlBandwidthPage=" + showDlBandwidthPage + ", showMosPage=" + showMosPage + "]";
	}

}
