package com.inn.foresight.core.report.utils;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class Statistics {
	private List<Double> data;
	private int size;
	private DecimalFormat decimalFormat = new DecimalFormat("####.##");

	public Statistics(List<Double> data) {
		Collections.sort(data);
		this.data = data;
		size = data.size();
	}

	public Double getMean() {
		double sum = 0.0;
		if (data != null && size > 0) {
			for (double a : data)
				sum += a;
			Double mean = sum / size;
			return Double.parseDouble(decimalFormat.format(mean));
		}
		return 0.0;
	}

	private Double getVariance() {
		if (data != null && size > 0) {
			double mean = getMean();
			double temp = 0;
			for (double a : data)
				temp += (mean - a) * (mean - a);

			return temp / (size - 1);
		}
		return 0.0;
	}

	public Double getStdDev() {
		if (data != null && size > 0) {
			return Double.parseDouble(decimalFormat.format(Math.sqrt(getVariance())));
		}
		return 0.0;
	}

	public Double getMedian() {
		if (data != null && size > 0) {
			if (data.size() % 2 == 0) {
				return Double.parseDouble(decimalFormat.format((data.get((data.size() / 2) - 1) + data.get(data.size() / 2)) / 2.0));
			} else {
				return data.get(data.size() / 2);
			}
		}
		return 0.0;
	}

	public Double getMax() {
		if (data != null && size > 0) {
			return Double.parseDouble(decimalFormat.format(data.get(data.size() - 1)));
		}
		return 0.0;

	}

	public Double getMin() {
		if (data != null && size > 0) {
			return Double.parseDouble(decimalFormat.format(data.get(0)));
		}
		return 0.0;
	}

	public Double getPercentileInclusive(Double desiredPercentile) {
		try {
			if (data != null && data.size() > 0 && desiredPercentile <= 1) {
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
			return 0.0;
		} catch (Exception e) {
		}
		return null;
	}
}