package com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark;

import java.util.List;

public class IBLteThreeGDataWrapper {
	List<IBKPIWrapper> lteDataList ;
	List<IBKPIWrapper> threeGDataList;
	
	public List<IBKPIWrapper> getLteDataList() {
		return lteDataList;
	}
	public void setLteDataList(List<IBKPIWrapper> lteDataList) {
		this.lteDataList = lteDataList;
	}
	public List<IBKPIWrapper> getThreeGDataList() {
		return threeGDataList;
	}
	public void setThreeGDataList(List<IBKPIWrapper> threeGDataList) {
		this.threeGDataList = threeGDataList;
	}
	@Override
	public String toString() {
		return "LteThreeGDataWrapper [lteDataList=" + lteDataList + ", threeGDataList=" + threeGDataList + "]";
	}
	
}
