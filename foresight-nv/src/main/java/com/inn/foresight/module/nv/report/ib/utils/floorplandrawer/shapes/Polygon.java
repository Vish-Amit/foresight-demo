package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.ShapeUtils.ThickNess;

public class Polygon implements Shape {
	private Logger logger = LogManager.getLogger(Polygon.class);
	private List<PointF> vertices = new ArrayList<>();
	private ThickNess thickness;
	private String type;

	public Polygon(List<PointF> vertices,ThickNess thickNess) {
		this.vertices = vertices;
		this.thickness = thickNess;
	}

	public List<PointF> getVertices() {
		return vertices;
	}

	public void setVertices(List<PointF> vertices) {
		this.vertices = vertices;
	}
	public ThickNess getThickness() {
		return thickness;
	}
	public void setThickness(ThickNess thickness) {
		this.thickness = thickness;
	}

	@Override
	public void draw(BufferedImageEditor editor) {
		editor.drawLine(vertices, ShapeUtils.getWidthAsPerThickness(thickness),true);
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
