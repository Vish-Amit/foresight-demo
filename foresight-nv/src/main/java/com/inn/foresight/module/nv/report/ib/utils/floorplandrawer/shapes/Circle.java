package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;

public class Circle implements Shape {
	private List<PointF> vertices = new ArrayList<>();
	public Circle(List<PointF> vertices) {
		this.vertices = vertices;
	}
	@Override
	public void draw(BufferedImageEditor editor) {
		PointF startPoint = getMinXYValue();
		PointF endPoint = getMaxXYValue();
		editor.drawOval(startPoint.getX().intValue(), startPoint.getY().intValue(), endPoint.getX().intValue() - startPoint.getX().intValue(), endPoint.getY().intValue() - startPoint.getY().intValue(), Color.GRAY);
	}

    public PointF getMinXYValue() {
        Double minX = vertices.get(0).getX();
        Double minY = vertices.get(0).getY();
        for (int i = 0; i < vertices.size(); i++) {
            if (minX > vertices.get(i).getX()) {
                minX = vertices.get(i).getX();
            }
            if (minY > vertices.get(i).getY()) {
                minY = vertices.get(i).getY();
            }
        }
        return new PointF(minX, minY);
    }

    public PointF getMaxXYValue() {
        Double maxX = vertices.get(0).getX();
        Double maxY = vertices.get(0).getY();
        for (int i = 0; i < vertices.size(); i++) {
            if (maxX < vertices.get(i).getX()) {
                maxX = vertices.get(i).getX();
            }
            if (maxY < vertices.get(i).getY()) {
                maxY = vertices.get(i).getY();
            }
        }
        return new PointF(maxX, maxY);
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
