package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.components;

import java.util.ArrayList;
import java.util.List;

import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.ShapeUtils;



public class Cable implements Component {
	private List<PointF> vertices = new ArrayList<>();
	private  static final int CABLE_WIDTH = 5;
	public Cable(List<PointF> tempList){
		this.vertices = tempList;
	}

	@Override
	public void draw(BufferedImageEditor editor) {
		editor.drawLine(vertices, CABLE_WIDTH,false);
	}
	@Override
	public void initializeImage() {
		  throw new UnsupportedOperationException();

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
