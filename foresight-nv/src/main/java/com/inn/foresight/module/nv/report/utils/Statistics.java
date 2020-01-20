package com.inn.foresight.module.nv.report.utils;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Statistics {
	
	/** The logger. */
	private static Logger logger = LogManager.getLogger(Statistics.class);
	
	
	private List<Double> data;
	int size;
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	private DecimalFormat decimalFormat = new DecimalFormat("####.##");

	public Statistics(List<Double> data) {
		if(data!=null) {
		Collections.sort(data);
		this.data = data;
		size = data.size();
		}
	}

	public Double getMean() {
		double sum = 0.0;
		if (data != null && size > 0) {
			for (double a : data) {
                sum += a;
            }
			Double mean = sum / size;
			return Double.parseDouble(decimalFormat.format(mean));
		}
		return null;
	}

	public Double getVariance() {
		if (data != null && size > 0) {
			double mean = getMean();
			if (getMean()!=null) {
				double temp = 0;
				for (double a : data) {
                    temp += (mean - a) * (mean - a);
                }
				return temp / (size - 1);
			}
		}
		return null;
	}

	public Double getStdDev() {
		try {
			if (data != null && size > 0) {
				Double variance = getVariance();
				if(variance!=null) {
			        return Double.parseDouble(decimalFormat.format(Math.sqrt(variance)));
			    }
			}
		} catch (Exception e) {
			logger.info("Unable to get the standard deviation for data {}  ,msg {} ",data,e.getMessage());
		}
		return null;
	}

	public Double getMedian() {
		try {
			if (data != null && size > 0) {
				if (data.size() % 2 == 0) {
					return Double.parseDouble(decimalFormat.format((data.get((data.size() / 2) - 1) + data.get(data.size() / 2)) / 2.0));
				} else {
					return data.get(data.size() / 2);
				}
			}
		} catch (Exception e) {
			logger.info("Unable to caculate the median for data {}, msg {} ",data,e.getMessage());
		}
		return null;
	}

	public Double getMax() {
		if (data != null && size > 0) {
			return Double.parseDouble(decimalFormat.format(data.get(data.size() - 1)));
		}
		return null;

	}

	public Double getMin() {
		if (data != null && size > 0) {
			return Double.parseDouble(decimalFormat.format(data.get(0)));
		}
		return null;
	}

	public Double getPercentileInclusive(Double desiredPercentile) {
		try {
			if (data != null && !data.isEmpty() && desiredPercentile <= 1) {
				if (data.size() == 1) {
					return Double.parseDouble(decimalFormat.format(data.get(0)));
				}
				Double index = desiredPercentile * (data.size() - 1) + 1;
				int numericIndex = index.intValue();
				if (index == data.size() * 1.0) {
					return Double.parseDouble(decimalFormat.format(data.get(numericIndex - 1)));
				}
				double decimalIndex = index - numericIndex;
				if (numericIndex >= 1) {
					numericIndex = numericIndex - 1;
				}
				Double part1 = data.get(numericIndex);
				Double part2 = 0.0;

				if (data.size() > (numericIndex + 1)) {
					part2 = (data.get(numericIndex + 1) - data.get(numericIndex)) * decimalIndex;
				}
				Double percentile = part1 + part2;
				return Double.parseDouble(decimalFormat.format(percentile));
			}
			return null;
		} catch (Exception e) {
			logger.error("Exception inside method getPercentileInclusive for desiredPercentile {} , errMsg {} "
					,desiredPercentile,e.getMessage());
		}
		return null;
	}


}
