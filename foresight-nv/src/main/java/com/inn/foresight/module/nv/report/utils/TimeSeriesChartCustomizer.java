package com.inn.foresight.module.nv.report.utils;

import java.awt.BasicStroke;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
public class TimeSeriesChartCustomizer implements JRChartCustomizer{

	@Override
	public void customize(JFreeChart jFreeChart, JRChart arg1) {
		XYPlot categoryplot = jFreeChart.getXYPlot();
		XYItemRenderer renderer=categoryplot.getRenderer();
		XYDataset xyDs=categoryplot.getDataset();
		 for (int i = 0; i < xyDs.getSeriesCount(); i++) {
			renderer.setSeriesStroke(i, new BasicStroke(3));
	 }		
	}

}
