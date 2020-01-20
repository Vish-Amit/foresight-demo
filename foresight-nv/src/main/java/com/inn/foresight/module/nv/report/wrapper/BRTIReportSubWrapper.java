package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class BRTIReportSubWrapper {
List<BRTIYearlyReportWrapper>yearlyDataList;

public List<BRTIYearlyReportWrapper> getYearlyDataList() {
	return yearlyDataList;
}

public void setYearlyDataList(List<BRTIYearlyReportWrapper> yearlyDataList) {
	this.yearlyDataList = yearlyDataList;
}

@Override
public String toString() {
	return "BRTIReportSubWrapper [yearlyDataList=" + yearlyDataList + "]";
}


}
