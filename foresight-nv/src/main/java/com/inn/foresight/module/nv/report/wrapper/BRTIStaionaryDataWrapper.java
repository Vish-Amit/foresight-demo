package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class BRTIStaionaryDataWrapper {
private List<BRTIExcelReportWrapper> stationarySummaryList;

public List<BRTIExcelReportWrapper> getStationarySummaryList() {
	return stationarySummaryList;
}

public void setStationarySummaryList(List<BRTIExcelReportWrapper> stationarySummaryList) {
	this.stationarySummaryList = stationarySummaryList;
}

@Override
public String toString() {
	return "BRTIStaionaryDataWrapper [stationarySummaryList=" + stationarySummaryList + "]";
}


}
