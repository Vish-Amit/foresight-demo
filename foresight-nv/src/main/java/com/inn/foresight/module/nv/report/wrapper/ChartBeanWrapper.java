package com.inn.foresight.module.nv.report.wrapper;

public class ChartBeanWrapper {

	private String rateKPI;
	private Double perKPI;
	private Double cdfKPI;

	private String rateKPI_R;
	private Double perKPI_R;
	private Double cdfKPI_R;

	public String getRateKPI() {
		return rateKPI;
	}

	public void setRateKPI(String rateKPI) {
		this.rateKPI = rateKPI;
	}

	public Double getPerKPI() {
		return perKPI;
	}

	public void setPerKPI(Double perKPI) {
		this.perKPI = perKPI;
	}

	public Double getCdfKPI() {
		return cdfKPI;
	}

	public void setCdfKPI(Double cdfKPI) {
		this.cdfKPI = cdfKPI;
	}

	public String getRateKPI_R() {
		return rateKPI_R;
	}

	public void setRateKPI_R(String rateKPI_R) {
		this.rateKPI_R = rateKPI_R;
	}

	public Double getPerKPI_R() {
		return perKPI_R;
	}

	public void setPerKPI_R(Double perKPI_R) {
		this.perKPI_R = perKPI_R;
	}

	public Double getCdfKPI_R() {
		return cdfKPI_R;
	}

	public void setCdfKPI_R(Double cdfKPI_R) {
		this.cdfKPI_R = cdfKPI_R;
	}



	@Override
	public String toString() {
		return "ChartBeanWrapper [rateKPI=" + rateKPI + ", perKPI=" + perKPI + ", cdfKPI=" + cdfKPI + ", rateKPI_R=" + rateKPI_R + ", perKPI_R=" + perKPI_R + ", cdfKPI_R=" + cdfKPI_R + "]";
	}


}
