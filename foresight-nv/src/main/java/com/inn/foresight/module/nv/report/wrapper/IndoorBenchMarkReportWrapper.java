package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

import com.inn.foresight.module.nv.report.wrapper.benchmark.BenchMarksubwrapper;

public class IndoorBenchMarkReportWrapper {
	private String daysOfMesurment;
	private String dateOfMesurment;
	private String timeOfMesurment;
    private String fileName;
    private Integer totalMos;
    private Double totalMosPercent;
    
	List<BenchMarksubwrapper>subList;

	
	public List<BenchMarksubwrapper> getSubList() {
		return subList;
	}

	public void setSubList(List<BenchMarksubwrapper> subList) {
		this.subList = subList;
	}

	public String getDaysOfMesurment() {
		return daysOfMesurment;
	}

	public void setDaysOfMesurment(String daysOfMesurment) {
		this.daysOfMesurment = daysOfMesurment;
	}

	public String getDateOfMesurment() {
		return dateOfMesurment;
	}

	public void setDateOfMesurment(String dateOfMesurment) {
		this.dateOfMesurment = dateOfMesurment;
	}

	public String getTimeOfMesurment() {
		return timeOfMesurment;
	}

	public void setTimeOfMesurment(String timeOfMesurment) {
		this.timeOfMesurment = timeOfMesurment;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getTotalMos() {
		return totalMos;
	}

	public void setTotalMos(Integer totalMos) {
		this.totalMos = totalMos;
	}

	public Double getTotalMosPercent() {
		return totalMosPercent;
	}

	public void setTotalMosPercent(Double totalMosPercent) {
		this.totalMosPercent = totalMosPercent;
	}
	
	
} 
