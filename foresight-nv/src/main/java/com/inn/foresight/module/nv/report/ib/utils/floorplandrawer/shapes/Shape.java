package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes;

import java.util.List;

import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;


public interface Shape {
	void draw(BufferedImageEditor editor);
	void shift(int x,int y);
	List<Float> getAllX();
	List<Float> getAllY();
}
