package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.components;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.DrawFloorConstant;

public class ComponentAP implements Component {
	private Logger logger = LogManager.getLogger(ComponentAP.class);

	private PointF centerPoint;
	private BufferedImage img;
	public ComponentAP(PointF point) {
		this.centerPoint = point;
	}
	@Override
	public void initializeImage() {
		 try {
			 String path =ConfigUtils.getString(NVConfigUtil.INBUILDING_COMPONENT_IMAGE_PATH);
				img = ImageIO.read(new File(path+DrawFloorConstant.WIFI_ICON_IMAGE));
			} catch (IOException e) {
				logger.error("Exception inside the method initializeImage {}",Utils.getStackTrace(e));
			}
	}

	@Override
	public void draw(BufferedImageEditor editor) {
		if(img == null){
			initializeImage();
		}
		editor.drawImage(img, centerPoint);
	}
	@Override
	public void shift(int x, int y) {
		centerPoint = new PointF(centerPoint.getX()+x,centerPoint.getY()+y);
	}
	@Override
	public List<Float> getAllX() {
		List<Float> tempList = new ArrayList<>();
		tempList.add(centerPoint.getX().floatValue());
		return tempList;
	}
	@Override
	public List<Float> getAllY() {
		List<Float> tempList = new ArrayList<>();
		tempList.add(centerPoint.getY().floatValue());
		return tempList;
	}

}
