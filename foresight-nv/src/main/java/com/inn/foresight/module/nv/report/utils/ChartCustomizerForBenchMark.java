package com.inn.foresight.module.nv.report.utils;

import java.awt.Color;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class ChartCustomizerForBenchMark implements JRChartCustomizer {

	private Logger logger = LogManager.getLogger(ChartCustomizerForBenchMark.class);
	public static final Color YELLOW = new Color(252, 241, 10);
	public static final Color BLUE = new Color(90, 54, 125);
	public static final Color RED = new Color(255, 25, 34);
	public static final Color VIOLET = new Color(31, 31, 54);
	public static final Color WHITE = new Color(255, 255, 255);

	public void customize(JFreeChart jFreeChart, JRChart jasperChart) {

		CategoryPlot categoryplot = jFreeChart.getCategoryPlot();
		
		if(categoryplot.getRenderer() instanceof BarRenderer) {
			BarRenderer br = (BarRenderer) categoryplot.getRenderer();
//			br.setItemMargin(-1.37);
			br.setMaximumBarWidth(0.10);
			CategoryDataset cd = categoryplot.getDataset();
			if (cd != null) {
				cd.getGroup();
				List<String> stringlist = cd.getRowKeys();
				Integer i = 0;
				for (String s1 : stringlist) {
					if (s1.equalsIgnoreCase("")) {
						br.setSeriesPaint(i, WHITE, true);
						br.setLegendTextPaint(i, Color.WHITE);
					} else {
						br.setSeriesPaint(i, getColor(i), true);
					}

					i++;
				}
			}

//			br.setItemMargin(-0.35);
//			br.setMaximumBarWidth(0.02);
		} else if(categoryplot.getRenderer() instanceof LineAndShapeRenderer) {
			LineAndShapeRenderer br = (LineAndShapeRenderer) categoryplot.getRenderer();
			CategoryDataset cd = categoryplot.getDataset();
			br.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			br.setBaseItemLabelsVisible(true);
//			br.setItemLabelsVisible(true);
			if (cd != null) {
				cd.getGroup();
				List<String> stringlist = cd.getRowKeys();
				Integer i = 0;
				for (String s1 : stringlist) {
					if (s1.equalsIgnoreCase("")) {
						br.setSeriesPaint(i, WHITE, true);
						br.setLegendTextPaint(i, Color.WHITE);
					} else {
						br.setSeriesPaint(i, getColor(i), true);
					}

					i++;
				}
			}

		}

	}

	private Color getColor(int i) {

		if (i == 0) {
			return YELLOW;

		} else if (i == 1) {
			return BLUE;
		} else if (i == 2) {
			return RED;
		} else if (i == 3) {
			return VIOLET;
		}
		return WHITE;
	}

}