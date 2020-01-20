package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.ShapeUtils.ThickNess;


public class Wall implements Shape {
	private Logger logger = LogManager.getLogger(Wall.class);
	private ThickNess thickNess;
	private List<PointF> vertices = new ArrayList<>();
	public Wall(List<PointF> vertices,ThickNess thickNess) {
		logger.info("Going to make wall of vertices{}",vertices.size());
		this.vertices = vertices;
		this.thickNess = thickNess;
	}

	public ThickNess getThickNess() {
		return thickNess;
	}

	public void setThickNess(ThickNess thickNess) {
		this.thickNess = thickNess;
	}

	@Override
	public void draw(BufferedImageEditor editor) {
		logger.info("Going to make wall of vertices{}",vertices.size());
		editor.drawLine(vertices, ShapeUtils.getWidthAsPerThickness(thickNess),false);
	}
	@Override
	public void shift(int x, int y) {
		int size = vertices.size();
		for(int i=0;i<size;i++){
			PointF point = vertices.get(i);
			point = new PointF(point.getX()+x,point.getY()+y);
			vertices.set(i, point);
		}
	}
	@Override
	public List<Float> getAllX() {
		return ShapeUtils.getAllX(vertices);
	}
	@Override
	public List<Float> getAllY() {
		return ShapeUtils.getAllY(vertices);
	}

}
