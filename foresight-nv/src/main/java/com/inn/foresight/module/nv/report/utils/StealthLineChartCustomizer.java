package com.inn.foresight.module.nv.report.utils;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;
import java.util.List;

public class StealthLineChartCustomizer implements JRChartCustomizer {

	@Override
	public void customize(JFreeChart jFreeChart, JRChart arg1) {


		CategoryPlot categoryplot = jFreeChart.getCategoryPlot();

		if(categoryplot.getRenderer() instanceof LineAndShapeRenderer) {
			LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) categoryplot.getRenderer();
			lineRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			lineRenderer.setBaseItemLabelsVisible(true);
			lineRenderer.setBaseItemLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 7));
			lineRenderer.setSeriesStroke(0, new BasicStroke(1));
		} else if(categoryplot.getRenderer() instanceof BarRenderer){
			BarRenderer br = (BarRenderer) categoryplot.getRenderer();
			br.setMaximumBarWidth(0.10);
			CategoryDataset cd = categoryplot.getDataset();
			if (cd != null) {
				cd.getGroup();
				List<String> stringlist = cd.getRowKeys();
				Integer i = 0;
				for (String s1 : stringlist) {
					if (s1.equalsIgnoreCase("")) {
						br.setSeriesPaint(i, Color.WHITE, true);
						br.setLegendTextPaint(i, Color.WHITE);
					}

					i++;
				}
			}
		}

	}

}
