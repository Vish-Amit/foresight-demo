package com.inn.foresight.module.nv.report.wrapper;

public class OssKpiWrapper {
	private Integer cgi;
	private Integer pci;
	private Integer cellNum;
    private String  neName;
    private String parentneName;
    private String date;
    
    private Double hoSuccessRate;
    private Double rrcSetupSuccessRate;
    private Double erabDropRate;
    private Double dlThroughPut;
    private Double ulThroughPut;
    private Double avgCQI;
	public Integer getCgi() {
		return cgi;
	}
	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}
	public Integer getPci() {
		return pci;
	}
	public void setPci(Integer pci) {
		this.pci = pci;
	}
	public Integer getCellNum() {
		return cellNum;
	}
	public void setCellNum(Integer cellNum) {
		this.cellNum = cellNum;
	}
	public String getNeName() {
		return neName;
	}
	public void setNeName(String neName) {
		this.neName = neName;
	}
	public String getParentneName() {
		return parentneName;
	}
	public void setParentneName(String parentneName) {
		this.parentneName = parentneName;
	}
	public Double getHoSuccessRate() {
		return hoSuccessRate;
	}
	public void setHoSuccessRate(Double hoSuccessRate) {
		this.hoSuccessRate = hoSuccessRate;
	}
	public Double getRrcSetupSuccessRate() {
		return rrcSetupSuccessRate;
	}
	public void setRrcSetupSuccessRate(Double rrcSetupSuccessRate) {
		this.rrcSetupSuccessRate = rrcSetupSuccessRate;
	}
	public Double getErabDropRate() {
		return erabDropRate;
	}
	public void setErabDropRate(Double erabDropRate) {
		this.erabDropRate = erabDropRate;
	}
	public Double getDlThroughPut() {
		return dlThroughPut;
	}
	public void setDlThroughPut(Double dlThroughPut) {
		this.dlThroughPut = dlThroughPut;
	}
	public Double getUlThroughPut() {
		return ulThroughPut;
	}
	public void setUlThroughPut(Double ulThroughPut) {
		this.ulThroughPut = ulThroughPut;
	}
	public Double getAvgCQI() {
		return avgCQI;
	}
	public void setAvgCQI(Double avgCQI) {
		this.avgCQI = avgCQI;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "OssKpiWrapper [cgi=" + cgi + ", pci=" + pci + ", cellNum=" + cellNum + ", neName=" + neName
				+ ", parentneName=" + parentneName + ", date=" + date + ", hoSuccessRate=" + hoSuccessRate
				+ ", rrcSetupSuccessRate=" + rrcSetupSuccessRate + ", erabDropRate=" + erabDropRate + ", dlThroughPut="
				+ dlThroughPut + ", ulThroughPut=" + ulThroughPut + ", avgCQI=" + avgCQI + "]";
	}
    
}
