package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.components;

import java.util.List;

import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;


public interface Component {
	void draw(BufferedImageEditor editor);
	void initializeImage();
	void shift(int x, int y);
	List<Float> getAllX();
	List<Float> getAllY();
}
