package com.inn.foresight.core.maplayer.model;

public class ReportWrapper {
	private Double median;
	private Double percentile5;
	private Double percentile95;

	public Double getMedian() {
		return median;
	}
	public void setMedian(Double median) {
		this.median = median;
	}
	public Double getPercentile5() {
		return percentile5;
	}
	public void setPercentile5(Double percentile5) {
		this.percentile5 = percentile5;
	}
	public Double getPercentile95() {
		return percentile95;
	}
	public void setPercentile95(Double percentile95) {
		this.percentile95 = percentile95;
	}

	@Override
	public String toString() {
		return "ReportWrapper [median=" + median + ", percentile5=" + percentile5 + ", percentile95=" + percentile95
				+ "]";
	}
}
