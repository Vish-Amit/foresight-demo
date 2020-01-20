package com.inn.foresight.module.nv.report.utils;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class LiveDriveRFReportBarChartCustomizer implements JRChartCustomizer {
	@Override
    public void customize(JFreeChart jFreeChart, JRChart jasperChart) {
		CategoryPlot categoryplot = jFreeChart.getCategoryPlot();
		BarRenderer br = (BarRenderer) categoryplot.getRenderer();
		br.setMaximumBarWidth(0.06);
		br.setItemMargin(.05);

		categoryplot.setRangeGridlinesVisible(ReportConstants.IS_GRIDLINES_VISIBLE_FOR_CHART_RANGES);
		categoryplot.setDomainGridlinesVisible(ReportConstants.IS_GRIDLINES_VISIBLE_FOR_CHART_DOMAIN);
	}
}
