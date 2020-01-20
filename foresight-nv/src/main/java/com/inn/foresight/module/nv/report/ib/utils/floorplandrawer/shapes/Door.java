package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;
import com.inn.foresight.module.nv.report.ib.image.RectF;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.ShapeUtils.ThickNess;

public class Door implements Shape {
	private Logger logger = LogManager.getLogger(Door.class);
	private static final int INNER_LINE_WIDTH = 1;
	private PointF startPoint;
	private PointF endPoint;
	private PointF backLineStartPoint;
	private PointF backLineEndPoint;
	private PointF otherPoint;
	private int rotationAngle;
	private int startAngle;
	private RectF rect;
	private PointF oppositeLineStartPoint;
	private PointF oppositeLineEndPoint;
	private ThickNess thickNess = ThickNess.THIN;
	private double facingAngle;
	private int mLength;

	public PointF getBackLineStartPoint() {
		return backLineStartPoint;
	}

	public void setBackLineStartPoint(PointF backLineStartPoint) {
		this.backLineStartPoint = backLineStartPoint;
	}

	public PointF getBackLineEndPoint() {
		return backLineEndPoint;
	}

	public void setBackLineEndPoint(PointF backLineEndPoint) {
		this.backLineEndPoint = backLineEndPoint;
	}

	public int getStartAngle() {
		return startAngle;
	}

	public void setStartAngle(int startAngle) {
		this.startAngle = startAngle;
	}

	public RectF getRect() {
		return rect;
	}

	public void setRect(RectF rect) {
		this.rect = rect;
	}

	public ThickNess getThickNess() {
		return thickNess;
	}

	public void setThickNess(ThickNess thickNess) {
		this.thickNess = thickNess;
	}

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

	public int getRotationAngle() {
		return rotationAngle;
	}

	public void setRotationAngle(int rotationAngle) {
		this.rotationAngle = rotationAngle;
	}

	public double getFacingAngle() {
		return facingAngle;
	}

	public void setFacingAngle(double facingAngle) {
		this.facingAngle = facingAngle;
	}

	@Override
	public void draw(BufferedImageEditor editor) {
		logger.info("Going to make door  of startPoint{} endPoint{} startAngle{} length{}",startPoint,endPoint,startAngle,mLength);
		editor.drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(), Color.WHITE,ShapeUtils.getWidthAsPerThickness(thickNess));
		drawDoorLine(editor);
		int intialAngle = 180 - (int) ShapeUtils.calculateAngleBetweenPoints(backLineEndPoint,otherPoint ) ;
		int compareAngle = 180 - (int) ShapeUtils.calculateAngleBetweenPoints(backLineEndPoint,backLineStartPoint ) ;
		int biggerAngle = intialAngle>compareAngle ? intialAngle:compareAngle;
		int smallerAngle = intialAngle>compareAngle ? compareAngle:intialAngle;
		if(biggerAngle - smallerAngle >90){
			intialAngle = biggerAngle;
		}
		else{
			intialAngle = smallerAngle;
		}
		editor.drawArc(backLineEndPoint.getX().intValue()-mLength, backLineEndPoint.getY().intValue()-mLength, mLength*2,  mLength*2,intialAngle, 90, Color.GRAY, INNER_LINE_WIDTH*5);
		editor.drawLine(backLineStartPoint.getX(), backLineStartPoint.getY(), backLineEndPoint.getX(), backLineEndPoint.getY(), Color.GRAY,INNER_LINE_WIDTH*5);
	}
	public double getArcLength(double radius){
		return 2 * 3.14 * radius * (90f/360);
	}

	private void drawDoorLine(BufferedImageEditor editor) {
		mLength = (int) Math.abs(ShapeUtils.distanceBetween(startPoint, endPoint));
		 List<PointF> tempList = ShapeUtils.getPerpendicularToLineAtDistance(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(), ShapeUtils.getWidthAsPerThickness(thickNess)/3.0f);
         List<PointF> tempList2 = ShapeUtils.getPerpendicularToLineAtDistance(endPoint.getX(), endPoint.getY(), startPoint.getX(), startPoint.getY(), ShapeUtils.getWidthAsPerThickness(thickNess)/3.0f);

		editor.drawLine(tempList.get(0).getX(), tempList.get(0).getY(), tempList.get(1).getX(), tempList.get(1).getY(), Color.GRAY,INNER_LINE_WIDTH);
		editor.drawLine(tempList2.get(0).getX(), tempList2.get(0).getY(), tempList2.get(1).getX(), tempList2.get(1).getY(), Color.GRAY,INNER_LINE_WIDTH);
		editor.drawLine(tempList.get(0).getX(), tempList.get(0).getY(), tempList2.get(1).getX(), tempList2.get(1).getY(), Color.GRAY,INNER_LINE_WIDTH);
		editor.drawLine(tempList.get(1).getX(), tempList.get(1).getY(), tempList2.get(0).getX(), tempList2.get(0).getY(), Color.GRAY,INNER_LINE_WIDTH);
	}
	@Override
	public void shift(int x, int y) {
		startPoint = new PointF(startPoint.getX()+x,startPoint.getY()+y);
		endPoint = new PointF(endPoint.getX()+x,endPoint.getY()+y);
		otherPoint = new PointF(otherPoint.getX()+x,otherPoint.getY()+y);
		backLineStartPoint = new PointF(backLineStartPoint.getX()+x,backLineStartPoint.getY()+y);
		backLineEndPoint = new PointF(backLineEndPoint.getX()+x,backLineEndPoint.getY()+y);
	}
	@Override
	public List<Float> getAllX() {
		List<PointF> vertices = new ArrayList<>();
		vertices.add(endPoint);
		vertices.add(startPoint);
		vertices.add(otherPoint);
		vertices.add(backLineStartPoint);
		vertices.add(backLineEndPoint);
		return ShapeUtils.getAllX(vertices);
	}
	@Override
	public List<Float> getAllY() {
		List<PointF> vertices = new ArrayList<>();
		vertices.add(endPoint);
		vertices.add(startPoint);
		vertices.add(otherPoint);
		vertices.add(backLineStartPoint);
		vertices.add(backLineEndPoint);
		return ShapeUtils.getAllY(vertices);
	}

	/**
	 * @return the otherPoint
	 */
	public PointF getOtherPoint() {
		return otherPoint;
	}

	/**
	 * @param otherPoint the otherPoint to set
	 */
	public void setOtherPoint(PointF otherPoint) {
		this.otherPoint = otherPoint;
	}

	/**
	 * @return the oppositeLineStartPoint
	 */
	public PointF getOppositeLineStartPoint() {
		return oppositeLineStartPoint;
	}

	/**
	 * @param oppositeLineStartPoint the oppositeLineStartPoint to set
	 */
	public void setOppositeLineStartPoint(PointF oppositeLineStartPoint) {
		this.oppositeLineStartPoint = oppositeLineStartPoint;
	}

	/**
	 * @return the oppositeLineEndPoint
	 */
	public PointF getOppositeLineEndPoint() {
		return oppositeLineEndPoint;
	}

	/**
	 * @param oppositeLineEndPoint the oppositeLineEndPoint to set
	 */
	public void setOppositeLineEndPoint(PointF oppositeLineEndPoint) {
		this.oppositeLineEndPoint = oppositeLineEndPoint;
	}

	/**
	 * @return the mLength
	 */
	public int getmLength() {
		return mLength;
	}

	/**
	 * @param mLength the mLength to set
	 */
	public void setmLength(int mLength) {
		this.mLength = mLength;
	}
	
}
