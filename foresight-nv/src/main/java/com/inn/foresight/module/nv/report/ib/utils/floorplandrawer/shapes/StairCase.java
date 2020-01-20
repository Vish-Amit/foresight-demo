package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.ShapeUtils.ThickNess;

public class StairCase implements Shape {
	private Logger logger = LogManager.getLogger(StairCase.class);
	private List<PointF> vertices = new ArrayList<>();
	private static final int INNER_LINE_WIDTH = 5;
	private ThickNess thickness;
	private int verticalLineMar = 12;
	private int horizontalLineMar = 8;

	public StairCase(List<PointF> vertices, ThickNess thickNess) {
		this.vertices = vertices;
		this.thickness = thickNess;
	}

	@Override
	public void draw(BufferedImageEditor editor) {
		logger.info("Going to draw stairs of vertices{}", vertices);
		editor.drawLine(vertices, ShapeUtils.getWidthAsPerThickness(thickness), true);
		drawInnerLines(editor);
	}

	private void drawInnerLines(BufferedImageEditor editor) {
		float dx1 = (float) (vertices.get(0).getX() - vertices.get(1).getX());
		float dy1 = (float) (vertices.get(0).getY() - vertices.get(1).getY());

		float dx2 = (float) (vertices.get(1).getX() - vertices.get(2).getX());
		float dy2 = (float) (vertices.get(1).getY() - vertices.get(2).getY());

		float dx3 = (float) (vertices.get(3).getX() - vertices.get(2).getX());
		float dy3 = (float) (vertices.get(3).getY() - vertices.get(2).getY());

		float dx4 = (float) (vertices.get(0).getX() - vertices.get(3).getX());
		float dy4 = (float) (vertices.get(0).getY() - vertices.get(3).getY());

		// VERTICAL LINES START
		PointF firstPoint = ShapeUtils.getCenterPointOfLine(vertices.get(0), vertices.get(1));
		PointF secondPoint = ShapeUtils.getCenterPointOfLine(vertices.get(3), vertices.get(2));
		firstPoint.setX(firstPoint.getX() - dx1 / verticalLineMar);
		firstPoint.setY(firstPoint.getY() - dy1 / verticalLineMar);
		secondPoint.setX(secondPoint.getX() - dx3 / verticalLineMar);
		secondPoint.setY( secondPoint.getY() - dy3 / verticalLineMar);
		editor.drawLine(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY(), Color.GRAY, INNER_LINE_WIDTH);
		///////// ---------------Custom commented code------------------//
		// // NEW CODE TO DRAW HORIZONTAL LINES
		float dx5 = (float) (firstPoint.getX() - secondPoint.getX());
		float dy5 = (float) (firstPoint.getY() - secondPoint.getY());

		PointF rightWall = ShapeUtils.getCenterPointOfLine(vertices.get(1), vertices.get(2));
		PointF firstPointOfLeftWall = ShapeUtils.getCenterPointOfLine(new PointF(firstPoint.getX(), firstPoint.getY()),
				new PointF(secondPoint.getX(), secondPoint.getY()));

		editor.drawLine(rightWall.getX(), rightWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);

		rightWall.setX(rightWall.getX() - dx2 / horizontalLineMar);
		rightWall.setY(rightWall.getY() - dy2 / horizontalLineMar);
		firstPointOfLeftWall.setX(firstPointOfLeftWall.getX() - dx5 / horizontalLineMar);
		firstPointOfLeftWall.setY(firstPointOfLeftWall.getY() - dy5 / horizontalLineMar);
		// Middle Line of Right Part
		editor.drawLine(rightWall.getX(), rightWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);
		rightWall.setX(rightWall.getX() - dx2 / horizontalLineMar);
		rightWall.setY(rightWall.getY() - dy2 / horizontalLineMar);
		firstPointOfLeftWall.setX(firstPointOfLeftWall.getX() - dx5 / horizontalLineMar);
		firstPointOfLeftWall.setY(firstPointOfLeftWall.getY() - dy5 / horizontalLineMar);

		editor.drawLine(rightWall.getX(), rightWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);

		rightWall.setX(rightWall.getX() - dx2 / horizontalLineMar);
		rightWall.setY(rightWall.getY() - dy2 / horizontalLineMar);
		firstPointOfLeftWall.setX(firstPointOfLeftWall.getX() - dx5 / horizontalLineMar);
		firstPointOfLeftWall.setY(firstPointOfLeftWall.getY() - dy5 / horizontalLineMar);

		editor.drawLine(rightWall.getX(), rightWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);
		// Upper Part From Middle line
		rightWall.setX(rightWall.getX() + (dx2 / horizontalLineMar) * 4);
		rightWall.setY(rightWall.getY() + (dy2 / horizontalLineMar) * 4);
		firstPointOfLeftWall.setX(firstPointOfLeftWall.getX() + (dx5 / horizontalLineMar) * 4);
		firstPointOfLeftWall.setY(firstPointOfLeftWall.getY() + (dy5 / horizontalLineMar) * 4);

		editor.drawLine(rightWall.getX(), rightWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);

		rightWall.setX(rightWall.getX() + dx2 / horizontalLineMar);
		rightWall.setY(rightWall.getY() + dy2 / horizontalLineMar);
		firstPointOfLeftWall.setX( firstPointOfLeftWall.getX() + dx5 / horizontalLineMar);
		firstPointOfLeftWall.setY(firstPointOfLeftWall.getY() + dy5 / horizontalLineMar);

		editor.drawLine(rightWall.getX(), rightWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);

		rightWall.setX( rightWall.getX() + dx2 / horizontalLineMar);
		rightWall.setY(rightWall.getY() + dy2 / horizontalLineMar);
		firstPointOfLeftWall.setX(firstPointOfLeftWall.getX() + dx5 / horizontalLineMar);
		firstPointOfLeftWall.setY(firstPointOfLeftWall.getY() + dy5 / horizontalLineMar);

		editor.drawLine(rightWall.getX(), rightWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);
		// //Second Left Part
		firstPoint.setX(firstPoint.getX() + (dx1 / verticalLineMar) + (dx1 / verticalLineMar));
		firstPoint.setY(firstPoint.getY() + (dy1 / verticalLineMar) + (dy1 / verticalLineMar));
		secondPoint.setX(secondPoint.getX() + (dx3 / verticalLineMar) + (dx3 / verticalLineMar));
		secondPoint.setY(secondPoint.getY() + (dy3 / verticalLineMar) + (dy3 / verticalLineMar));
		editor.drawLine(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY(), Color.GRAY, INNER_LINE_WIDTH);
		float dx6 = (float) (firstPoint.getX() - secondPoint.getX());
		float dy6 = (float) (firstPoint.getY() - secondPoint.getY());
		PointF leftWall = ShapeUtils.getCenterPointOfLine(vertices.get(3), vertices.get(0));
		firstPointOfLeftWall = ShapeUtils.getCenterPointOfLine(new PointF(firstPoint.getX(), firstPoint.getY()),
				new PointF(secondPoint.getX(), secondPoint.getY()));
		// //Middle Line
		editor.drawLine(leftWall.getX(), leftWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);

		leftWall.setX(leftWall.getX() - dx4 / horizontalLineMar);
		leftWall.setY(leftWall.getY() - dy4 / horizontalLineMar);
		firstPointOfLeftWall.setX(firstPointOfLeftWall.getX() - dx6 / horizontalLineMar);
		firstPointOfLeftWall.setY(firstPointOfLeftWall.getY() - dy6 / horizontalLineMar);

		editor.drawLine(leftWall.getX(), leftWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);

		leftWall.setX(leftWall.getX() - dx4 / horizontalLineMar);
		leftWall.setY(leftWall.getY() - dy4 / horizontalLineMar);
		firstPointOfLeftWall.setX( firstPointOfLeftWall.getX() - dx6 / horizontalLineMar);
		firstPointOfLeftWall.setY(firstPointOfLeftWall.getY() - dy6 / horizontalLineMar);

		editor.drawLine(leftWall.getX(), leftWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);

		leftWall.setX(leftWall.getX() - dx4 / horizontalLineMar);
		leftWall.setY(leftWall.getY() - dy4 / horizontalLineMar);
		firstPointOfLeftWall.setX(firstPointOfLeftWall.getX() - dx6 / horizontalLineMar);
		firstPointOfLeftWall.setY(firstPointOfLeftWall.getY() - dy6 / horizontalLineMar);

		editor.drawLine(leftWall.getX(), leftWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);
		// Upper part form Middle

		leftWall.setX(leftWall.getX() + (dx4 / horizontalLineMar) * 4);
		leftWall.setY(leftWall.getY() + (dy4 / horizontalLineMar) * 4);
		firstPointOfLeftWall.setX(firstPointOfLeftWall.getX() + (dx6 / horizontalLineMar) * 4);
		firstPointOfLeftWall.setY(firstPointOfLeftWall.getY() + (dy6 / horizontalLineMar) * 4);

		editor.drawLine(leftWall.getX(), leftWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);

		leftWall.setX(leftWall.getX() + dx4 / horizontalLineMar);
		leftWall.setY(leftWall.getY() + dy4 / horizontalLineMar);
		firstPointOfLeftWall.setX( firstPointOfLeftWall.getX() + dx6 / horizontalLineMar);
		firstPointOfLeftWall.setY(firstPointOfLeftWall.getY() + dy6 / horizontalLineMar);

		editor.drawLine(leftWall.getX(), leftWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);

		leftWall.setX(leftWall.getX() + dx4 / horizontalLineMar);
		leftWall.setY(leftWall.getY() + dy4 / horizontalLineMar);
		firstPointOfLeftWall.setX(firstPointOfLeftWall.getX() + dx6 / horizontalLineMar);
		firstPointOfLeftWall.setY(firstPointOfLeftWall.getY() + dy6 / horizontalLineMar);

		editor.drawLine(leftWall.getX(), leftWall.getY(), firstPointOfLeftWall.getX(), firstPointOfLeftWall.getY(), Color.GRAY,
				INNER_LINE_WIDTH);
		// VERTICAL LINES END

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
