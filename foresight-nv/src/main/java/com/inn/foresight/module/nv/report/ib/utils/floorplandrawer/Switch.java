package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.components.Component;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.ShapeUtils;

public class Switch implements Component {
	private static final  Logger logger = LogManager.getLogger(Switch.class);

	private PointF centerPoint;
	private BufferedImage mImg=null;

	public Switch(PointF pointF) {
		this.centerPoint = pointF;
	}

	@Override
	public void draw(BufferedImageEditor editor) {

	}

	@Override
	public void initializeImage() {
		try {
			mImg = ImageIO
					.read(new File("home/ist/webtesting/images/drawable/drawable-xxxhdpi/switch_icon.png"));
		} catch (IOException e) {
			logger.error("Exception inside the method initializeImage {}",Utils.getStackTrace(e));
		}
	}
	
	
	
	public PointF getCenterPoint() {
		return centerPoint;
	}

	public void setCenterPoint(PointF centerPoint) {
		this.centerPoint = centerPoint;
	}

	public BufferedImage getmImg() {
		return mImg;
	}

	public void setmImg(BufferedImage mImg) {
		this.mImg = mImg;
	}

	@Override
	public void shift(int x, int y) {
		centerPoint = new PointF(centerPoint.getX()+x,centerPoint.getY()+y);
	}

	@Override
	public List<Float> getAllX() {
		List<PointF> tempList = new ArrayList<>();
		tempList.add(centerPoint);
		return ShapeUtils.getAllX(tempList);
	}

	@Override
	public List<Float> getAllY() {
		List<PointF> tempList = new ArrayList<>();
		tempList.add(centerPoint);
		return ShapeUtils.getAllY(tempList);
	}

}
