package com.inn.foresight.module.nv.report.utils;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

import java.awt.*;

public class BarChartCustomizer implements JRChartCustomizer {

	@Override
    public void customize(JFreeChart jFreeChart, JRChart jasperChart) {
		CategoryPlot categoryplot = jFreeChart.getCategoryPlot();
		BarRenderer br = (BarRenderer) categoryplot.getRenderer();
	    br.setItemMargin(-1.37);
	    br.setMaximumBarWidth(0.05);
        br.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		br.setBaseItemLabelsVisible(true);
//		br.setItemLabelsVisible(true);
		br.setBaseItemLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN,7));

	}

}
