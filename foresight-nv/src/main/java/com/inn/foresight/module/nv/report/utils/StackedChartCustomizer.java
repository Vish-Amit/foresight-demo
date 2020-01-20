package com.inn.foresight.module.nv.report.utils;

import java.awt.Color;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class StackedChartCustomizer implements JRChartCustomizer{
	@Override
    public void customize(JFreeChart jFreeChart, JRChart jasperChart) {
		CategoryPlot categoryplot = jFreeChart.getCategoryPlot();
		StackedBarRenderer br = (StackedBarRenderer) categoryplot.getRenderer();
		br.setBaseItemLabelsVisible(true);
		br.setBaseItemLabelPaint(Color.black);
		/*br.setItemMargin(-1.37);
	    br.setMaximumBarWidth(0.05);*/
	    
	}
    
}
