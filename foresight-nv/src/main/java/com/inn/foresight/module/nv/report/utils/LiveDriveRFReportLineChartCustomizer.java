package com.inn.foresight.module.nv.report.utils;

import java.awt.BasicStroke;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class LiveDriveRFReportLineChartCustomizer implements JRChartCustomizer {

	@Override
	public void customize(JFreeChart arg0, JRChart arg1) {
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) ((CategoryPlot) arg0.getPlot()).getRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(1));

		CategoryPlot categoryPlot = arg0.getCategoryPlot();
		categoryPlot.setRangeGridlinesVisible(ReportConstants.IS_GRIDLINES_VISIBLE_FOR_CHART_RANGES);
		categoryPlot.setDomainGridlinesVisible(ReportConstants.IS_GRIDLINES_VISIBLE_FOR_CHART_DOMAIN);
	}

}
