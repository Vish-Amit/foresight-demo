package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.components;

import java.util.ArrayList;
import java.util.List;

import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.ShapeUtils;


public class Cabinet implements Component {
	private  static final int CABLE_WIDTH = 5;
	private List<PointF> vertices = new ArrayList<>();
	public Cabinet(List<PointF> vertices){
		this.vertices = vertices;
	}
	public Cabinet() {
	}
	@Override
	public void initializeImage() {
		  throw new UnsupportedOperationException();

	}

	@Override
	public void draw(BufferedImageEditor editor) {
		editor.drawLine(vertices, CABLE_WIDTH,true);
	}
	@Override
	public List<Float> getAllX() {
		return ShapeUtils.getAllX(vertices);
	}
	@Override
	public List<Float> getAllY() {
		return ShapeUtils.getAllY(vertices);
	}
	@Override
	public void shift(int x, int y) {
		for(int i=0;i<vertices.size();i++){
			PointF point = vertices.get(i);
			point = new PointF(point.getX()+x,point.getY()+y);
			vertices.add(i, point);
		}
	}

}
