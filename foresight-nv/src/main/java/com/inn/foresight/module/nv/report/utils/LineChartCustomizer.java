package com.inn.foresight.module.nv.report.utils;

import java.awt.BasicStroke;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class LineChartCustomizer implements JRChartCustomizer {

	@Override
	public void customize(JFreeChart arg0, JRChart arg1) {

		LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) arg0.getCategoryPlot().getRenderer();
		lineRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineRenderer.setBaseItemLabelsVisible(true);
		lineRenderer.setSeriesStroke(0, new BasicStroke(2));

	}

}
