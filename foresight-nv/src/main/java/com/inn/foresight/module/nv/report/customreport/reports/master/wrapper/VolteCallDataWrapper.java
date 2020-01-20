package com.inn.foresight.module.nv.report.customreport.reports.master.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class VolteCallDataWrapper {

	List<VolteCallData> volteCallDataList;

	/**
	 * @return the volteCallDataList
	 */
	public List<VolteCallData> getVolteCallDataList() {
		return volteCallDataList;
	}

	/**
	 * @param volteCallDataList the volteCallDataList to set
	 */
	public void setVolteCallDataList(List<VolteCallData> volteCallDataList) {
		this.volteCallDataList = volteCallDataList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VolteCallDataWrapper [volteCallDataList=" + volteCallDataList + "]";
	}

}
