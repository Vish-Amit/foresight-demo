package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;

public class Label implements Shape {
	PointF startPoint, endPoint;
	Float rotateAngle;
	String text;
	Color color;
	int fontSize;

	public Label(PointF point, PointF endPoint, String text, String colorStr,Double rotateAngle,int fontSize) {
		this.startPoint = point;
		this.endPoint = endPoint;
		this.rotateAngle = rotateAngle.floatValue();
		this.text = text;
		this.color = hex2Rgb(colorStr);
		this.fontSize = fontSize;
	}

	@Override
	public void draw(BufferedImageEditor editor) {
		PointF midPoint = getMidPoint(startPoint, endPoint);
		if(rotateAngle ==0){
			editor.drawText(new PointF(startPoint.getX()+30,startPoint.getY()+50), text, color,fontSize);
		}
		else{
			editor.drawText(rotateAngle,text,midPoint.getX().intValue(),midPoint.getY().intValue(),color,fontSize);
		}
	}

	@Override
	public void shift(int x, int y) {
		startPoint = new PointF(startPoint.getX() + x, startPoint.getY() + y);
		endPoint = new PointF(endPoint.getX() + x, endPoint.getY() + y);
	}

	private PointF getMidPoint(PointF tempStart, PointF tempEnd) {
		float tempMid = (float) ((tempEnd.getX() - tempStart.getX()) / 2);
		return new PointF(tempStart.getX() + tempMid, tempStart.getY());
	}

	@Override
	public List<Float> getAllX() {
		List<Float> tempList = new ArrayList<>();
		tempList.add(startPoint.getX().floatValue());
		tempList.add(endPoint.getX().floatValue());
		return tempList;
	}

	@Override
	public List<Float> getAllY() {
		List<Float> tempList = new ArrayList<>();
		tempList.add(startPoint.getY().floatValue());
		tempList.add(endPoint.getY().floatValue());
		return tempList;
	}

	/**
	 * @param colorStr
	 *            e.g. "#FFFFFF"
	 * @return
	 */
	public Color hex2Rgb(String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16),
				Integer.valueOf(colorStr.substring(5, 7), 16));
	}
}
