package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.ShapeUtils.ThickNess;

public class Window implements Shape {
	private Logger logger = LogManager.getLogger(Window.class);
	private static final int INNER_LINE_WIDTH = 1;
	private PointF startPoint;
	private PointF endPoint;
	private PointF associatedWallStartPoint;
	private PointF associatedWallEndPoint;
	private PointF windowStartPoint;
	private PointF windowEndPoint;
	public PointF getAssociatedWallStartPoint() {
		return associatedWallStartPoint;
	}

	public void setAssociatedWallStartPoint(PointF associatedWallStartPoint) {
		this.associatedWallStartPoint = associatedWallStartPoint;
	}

	public PointF getAssociatedWallEndPoint() {
		return associatedWallEndPoint;
	}

	public void setAssociatedWallEndPoint(PointF associatedWallEndPoint) {
		this.associatedWallEndPoint = associatedWallEndPoint;
	}

	public PointF getWindowStartPoint() {
		return windowStartPoint;
	}

	public void setWindowStartPoint(PointF windowStartPoint) {
		this.windowStartPoint = windowStartPoint;
	}

	public PointF getWindowEndPoint() {
		return windowEndPoint;
	}

	public void setWindowEndPoint(PointF windowEndPoint) {
		this.windowEndPoint = windowEndPoint;
	}

	private ThickNess thickNess = ThickNess.THIN;

	public PointF getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(PointF startPoint) {
		this.startPoint = startPoint;
	}

	public PointF getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(PointF endPoint) {
		this.endPoint = endPoint;
	}

	public ThickNess getThickNess() {
		return thickNess;
	}

	public void setThickNess(ThickNess thickNess) {
		this.thickNess = thickNess;
	}

	@Override
	public void draw(BufferedImageEditor editor) {
		List<PointF> windowPointList = getUpdatedPoint();
		logger.info("Making object of window with vertices {}",windowPointList.size());
		if (windowPointList.size() > 0) {
			if (windowPointList.get(0) != null) {
				windowStartPoint = windowPointList.get(0);
			} else {
				if (windowPointList.get(1) != null) {
					windowEndPoint = windowPointList.get(1);
				}
			}
		}
		editor.drawLine(startPoint.getX(),startPoint.getY(),endPoint.getX(), endPoint.getY(), Color.WHITE,ShapeUtils.getWidthAsPerThickness(thickNess));
		drawWindowLines(editor,startPoint,endPoint,ShapeUtils.getWidthAsPerThickness(thickNess)/3.0f,thickNess);
		drawWindowLines(editor,startPoint,endPoint,ShapeUtils.getWidthAsPerThickness(thickNess),thickNess);
	}

	private void drawWindowLines(BufferedImageEditor editor, PointF startPoint, PointF endPoint,float pExtraWidth,ThickNess pThickNess ) {
		List<PointF> tempList = ShapeUtils.getPerpendicularToLineAtDistance(startPoint.getX(), startPoint.getY(), endPoint.getX(),
				endPoint.getY(), pExtraWidth);
		List<PointF> tempList2 = ShapeUtils.getPerpendicularToLineAtDistance(endPoint.getX(), endPoint.getY(), startPoint.getX(),
				startPoint.getY(), pExtraWidth);
		editor.drawLine(tempList.get(0).getX(), tempList.get(0).getY(), tempList.get(1).getX(), tempList.get(1).getY(), Color.GRAY,INNER_LINE_WIDTH);
		editor.drawLine(tempList2.get(0).getX(), tempList2.get(0).getY(), tempList2.get(1).getX(), tempList2.get(1).getY(), Color.GRAY,INNER_LINE_WIDTH);
		editor.drawLine(tempList.get(0).getX(), tempList.get(0).getY(), tempList2.get(1).getX(), tempList2.get(1).getY(), Color.GRAY,INNER_LINE_WIDTH);
		editor.drawLine(tempList.get(1).getX(), tempList.get(1).getY(), tempList2.get(0).getX(), tempList2.get(0).getY(), Color.GRAY,INNER_LINE_WIDTH);
		List<PointF> circlePointList = ShapeUtils.getCircleLineIntersectionPoint(tempList.get(0), tempList2.get(1),
				tempList.get(0), 10);
		List<PointF> circlePointList2 = ShapeUtils.getCircleLineIntersectionPoint(tempList.get(1), tempList2.get(0),
				tempList.get(1), 10);
		editor.drawLine(circlePointList.get(1).getX(), circlePointList.get(1).getY(), circlePointList2.get(1).getX(),
				circlePointList2.get(1).getY(), Color.GRAY,INNER_LINE_WIDTH);
		circlePointList = ShapeUtils.getCircleLineIntersectionPoint(tempList2.get(1), tempList.get(0), tempList2.get(1),
				10);
		circlePointList2 = ShapeUtils.getCircleLineIntersectionPoint(tempList2.get(0), tempList.get(1), tempList2.get(0),
				10);

		editor.drawLine(circlePointList.get(1).getX(), circlePointList.get(1).getY(), circlePointList2.get(1).getX(),
				circlePointList2.get(1).getY() , Color.GRAY,INNER_LINE_WIDTH);
	}

	private List<PointF> getUpdatedPoint() {
		PointF startPoint = null;
		PointF endPoint = null;
		List<PointF> tempPointList = new ArrayList<>();
		if (ShapeUtils.isPointOnLine(associatedWallStartPoint, this.startPoint, this.endPoint)) {
			startPoint = associatedWallStartPoint;
			tempPointList.add(0, startPoint);
			tempPointList.add(1, endPoint);
		}
		if (ShapeUtils.isPointOnLine(associatedWallEndPoint, this.startPoint, this.endPoint)) {
			endPoint = associatedWallEndPoint;
			tempPointList.add(0, startPoint);
			tempPointList.add(1, endPoint);
		}
		return tempPointList;
	}

	@Override
	public void shift(int x, int y) {
		//startPoint, endPoint, associatedWallStartPoint, associatedWallEndPoint, windowStartPoint,windowEndPoint
		startPoint = new PointF(startPoint.getX()+x,startPoint.getY()+y);
		endPoint = new PointF(endPoint.getX()+x,endPoint.getY()+y);
		associatedWallStartPoint = new PointF(associatedWallStartPoint.getX()+x,associatedWallStartPoint.getY()+y);
		associatedWallEndPoint = new PointF(associatedWallEndPoint.getX()+x,associatedWallEndPoint.getY()+y);
	}

	@Override
	public List<Float> getAllX() {
		List<PointF> vertices = new ArrayList<>();
		vertices.add(endPoint);
		vertices.add(associatedWallStartPoint);
		vertices.add(associatedWallEndPoint);
		return ShapeUtils.getAllX(vertices);
	}
	@Override
	public List<Float> getAllY() {
		List<PointF> vertices = new ArrayList<>();
		vertices.add(endPoint);
		vertices.add(associatedWallStartPoint);
		vertices.add(associatedWallEndPoint);
		return ShapeUtils.getAllY(vertices);
	}

}
