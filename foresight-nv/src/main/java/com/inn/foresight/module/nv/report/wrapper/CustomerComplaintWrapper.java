package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class CustomerComplaintWrapper {
	private String fileName;


    List<CustomerComplaintSubWrapper>subList;

    public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<CustomerComplaintSubWrapper> getSubList() {
		return subList;
	}

	public void setSubList(List<CustomerComplaintSubWrapper> subList) {
		this.subList = subList;
	}

	@Override
	public String toString() {
		return "CustomerComplaintWrapper [subList=" + subList + "]";
	}


	

}