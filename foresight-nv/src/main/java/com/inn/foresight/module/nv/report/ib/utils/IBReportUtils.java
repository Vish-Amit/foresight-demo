package com.inn.foresight.module.nv.report.ib.utils;

import com.google.gson.Gson;
import com.inn.commons.lang.NumberUtils;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.FloorPlanJsonParser;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.components.Component;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.Shape;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.InbuildingPointWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IBReportUtils {
	private static IBReportUtils instance;

	private Logger logger = LogManager.getLogger(IBReportUtils.class);

	public static IBReportUtils getInstance() {
		if (instance == null) {
			instance = new IBReportUtils();
		}
		return instance;
	}

	private IBReportUtils() {
	}

	public InbuildingPointWrapper drawFloorPlan(String imgFloorPlann, String json,	List<String[]> arlist, Integer xPointIndex, Integer yPointIndex) {
		int disX = 0;
		int disY = 0;
		try {
			FloorPlanJsonParser jsonParser = FloorPlanJsonParser.instance;
			List<Shape> shapeList = null;
			List<Component> componentList = null;
			try {
				if(json!=null){
					shapeList = jsonParser.getShapeListFromJson(json);
					componentList = jsonParser.getComponentListFromJson(json);
				}
			} catch (Exception e) {
				logger.info("Exception while parsing Components {}", e.getMessage());
			}

			BufferedImage resizedImage = null;
			if (FloorPlanJsonParser.isImagePickedFromGallery(json)) {
				try {
					resizedImage = resizedImage(imgFloorPlann, json, jsonParser);
				} catch (IOException e) {
					logger.info("Exception while drawing Image {}", Utils.getStackTrace(e));
				}
			}
			File backGround = new File(imgFloorPlann);
			BufferedImage image = null;
			if (backGround.exists()) {
				image = ImageIO.read(new File(imgFloorPlann));
			}

			Float[] tmp = jsonParser.getMinimumXYPoint(shapeList, componentList, image);
			double minX = tmp[0] != null ? tmp[0] : 0.0f;
			double minY = tmp[1] != null ? tmp[1] : 0.0f;

			double maxX = tmp[2] != null ? tmp[2] : 0.0f;
			double maxY = tmp[3] != null ? tmp[3] : 0.0f;

			Double[] walkTestMinimumXY = jsonParser.getMinimumXYPoint(arlist, xPointIndex, yPointIndex);
			if (walkTestMinimumXY[0] != null && walkTestMinimumXY[1] != null && walkTestMinimumXY[2] != null
					&& walkTestMinimumXY[3] != null) {
				minX = (minX < walkTestMinimumXY[0]) ? minX : walkTestMinimumXY[0];
				minY = (minY < walkTestMinimumXY[1]) ? minY : walkTestMinimumXY[1];
				maxX = (maxX > walkTestMinimumXY[2]) ? maxX : walkTestMinimumXY[2];
				maxY = (maxY > walkTestMinimumXY[3]) ? maxY : walkTestMinimumXY[3];
			}

			// ------------Calculate negative x
			// margin---------------------------------//
			if (minX < 0) {
				disX = (int) Math.abs(minX);
			}
			// ------------Calculate negative y
			// margin---------------------------------//
			if (minY < 0) {
				disY = (int) Math.abs(minY);
			}

			disX += 30;
			disY += 30;

			InbuildingPointWrapper pointWrapper = setPointData(disX, disY, arlist, xPointIndex, yPointIndex);
			// ------------Calculate width and height of
			// image---------------------------------//
			int height = (int) (maxY + disY + ReportConstants.IB_FLOOR_PLAN_IMAGE_EXTRA_MARGIN);
			int width = (int) (maxX + disX + ReportConstants.IB_FLOOR_PLAN_IMAGE_EXTRA_MARGIN);

			BufferedImageEditor editor = new BufferedImageEditor(width, height);

			if (resizedImage != null) {
				drawImagePickedFromGallery(resizedImage, editor, disX, disY);
			}

			drawAllShapes(shapeList, editor, disX, disY);
			drawAllComponents(componentList, editor, disX, disY);
			drawScale(maxX + disX, json, editor);
			editor.finalizeImageToOutput(imgFloorPlann, BufferedImageEditor.OUTPUT_FORMAT_JPEG);
			return pointWrapper;
		} catch (Exception e) {
			logger.info("error inside the method drawFloorPlan {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private InbuildingPointWrapper setPointData(int disX, int disY, List<String[]> arlist, Integer xPointIndex, Integer yPointIndex) {
		List<String[]> shiftedPointList = shiftPointsOfTestParameters(arlist, disX, disY, xPointIndex, yPointIndex);
		InbuildingPointWrapper pointWrapper = new InbuildingPointWrapper();
		pointWrapper.setDisX(disX);
		pointWrapper.setDisY(disY);
		pointWrapper.setArlist(shiftedPointList);
		logger.debug(" pointWrapper {}", new Gson().toJson(pointWrapper));
		return pointWrapper;
	}


	public InbuildingPointWrapper drawFloorPlan(String imgFloorPlann, String json, String floorPlanName,
					List<String[]> arlist) {
		int disX = 0;
		int disY = 0;
		try {
			FloorPlanJsonParser jsonParser = FloorPlanJsonParser.instance;
			List<Shape> shapeList = null;
			List<Component> componentList = null;
			try {
				if(json!=null){
					shapeList = jsonParser.getShapeListFromJson(json);
					componentList = jsonParser.getComponentListFromJson(json);
				}
			} catch (Exception e) {
				logger.info("Exception while parsing Components {}", e.getMessage());
			}

			BufferedImage resizedImage = null;
			if (FloorPlanJsonParser.isImagePickedFromGallery(json)) {
				try {
					resizedImage = resizedImage(imgFloorPlann, json, jsonParser);
				} catch (IOException e) {
					logger.info("Exception while drawing Image {}", Utils.getStackTrace(e));
				}
			}
			File backGround = new File(imgFloorPlann);
			BufferedImage image = null;
			if (backGround.exists()) {
				image = ImageIO.read(new File(imgFloorPlann));
			}

			Float[] tmp = jsonParser.getMinimumXYPoint(shapeList, componentList, image);
			double minX = tmp[0] != null ? tmp[0] : 0.0f;
			double minY = tmp[1] != null ? tmp[1] : 0.0f;

			double maxX = tmp[2] != null ? tmp[2] : 0.0f;
			double maxY = tmp[3] != null ? tmp[3] : 0.0f;

			Double[] walkTestMinimumXY = jsonParser.getMinimumXYPoint(arlist);
			if (walkTestMinimumXY[0] != null && walkTestMinimumXY[1] != null && walkTestMinimumXY[2] != null
					&& walkTestMinimumXY[3] != null) {
				minX = (minX < walkTestMinimumXY[0]) ? minX : walkTestMinimumXY[0];
				minY = (minY < walkTestMinimumXY[1]) ? minY : walkTestMinimumXY[1];
				maxX = (maxX > walkTestMinimumXY[2]) ? maxX : walkTestMinimumXY[2];
				maxY = (maxY > walkTestMinimumXY[3]) ? maxY : walkTestMinimumXY[3];
			}

			// ------------Calculate negative x
			// margin---------------------------------//
			if (minX < 0) {
				disX = (int) Math.abs(minX);
			}
			// ------------Calculate negative y
			// margin---------------------------------//
			if (minY < 0) {
				disY = (int) Math.abs(minY);
			}

			disX += 30;
			disY += 30;

			InbuildingPointWrapper pointWrapper = setPointData(disX, disY, arlist);
			// ------------Calculate width and height of
			// image---------------------------------//
			int height = (int) (maxY + disY + ReportConstants.IB_FLOOR_PLAN_IMAGE_EXTRA_MARGIN);
			int width = (int) (maxX + disX + ReportConstants.IB_FLOOR_PLAN_IMAGE_EXTRA_MARGIN);

			BufferedImageEditor editor = new BufferedImageEditor(width, height);

			if (resizedImage != null) {
				drawImagePickedFromGallery(resizedImage, editor, disX, disY);
			}

			drawAllShapes(shapeList, editor, disX, disY);
			drawAllComponents(componentList, editor, disX, disY);
			drawScale(maxX + disX, json, editor);
			editor.finalizeImageToOutput(imgFloorPlann, BufferedImageEditor.OUTPUT_FORMAT_JPEG);
			return pointWrapper;
		} catch (Exception e) {
			logger.info("error inside the method drawFloorPlan {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private InbuildingPointWrapper setPointData(int disX, int disY, List<String[]> arlist) {
		List<String[]> shiftedPointList = shiftPointsOfTestParameters(arlist, disX, disY);
		InbuildingPointWrapper pointWrapper = new InbuildingPointWrapper();
		pointWrapper.setDisX(disX);
		pointWrapper.setDisY(disY);
		pointWrapper.setArlist(shiftedPointList);
		logger.debug(" pointWrapper {}", new Gson().toJson(pointWrapper));
		return pointWrapper;
	}

	private void drawScale(double xPoint, String json, BufferedImageEditor editor) throws JSONException {
		try {
			String[] scaleValueArray = FloorPlanJsonParser.instance.getScalePixelPerUnitAndUnit(json);
			editor.drawLine((int) xPoint - 100, 10, (int) xPoint, 10, java.awt.Color.GRAY);
			Double pixelPerUnit = Double.parseDouble(scaleValueArray[0]);
			editor.drawText(new PointF(xPoint - 90, 40),
					Utils.uptoTwoDecimal(100 / pixelPerUnit) + " " + scaleValueArray[1], java.awt.Color.GRAY, 25);
		} catch (Exception e) {
			logger.warn("Exception in drawScale {}", e.getMessage());
		}
	}

	private void drawImagePickedFromGallery(BufferedImage resizedImage, BufferedImageEditor editor, int disX, int disY) {
		if (resizedImage != null) {
			PointF imagePoint = new PointF(0f + disX, 0f + disY);
			editor.drawImageWithPointAsTopLeft(resizedImage, imagePoint);
		}
	}

	private BufferedImage resizedImage(String localDirPath, String json,
			FloorPlanJsonParser jsonParser) throws IOException {
		BufferedImage image;
		image = ImageIO.read(new File(localDirPath));
		int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
		Float[] bitmapDimension = jsonParser.getBitmapWidthAndHeight(json);
		boolean isPortraitImage = jsonParser.isPortraitImage(json);
		logger.info("Width and Height of background image {} , {}", bitmapDimension[0], bitmapDimension[1]);
		BufferedImage resizedImage = FloorPlanJsonParser.resizeImage(bitmapDimension[0].intValue(),
				bitmapDimension[1].intValue(), image, type, isPortraitImage);
		ImageIO.write(resizedImage, "jpeg", new File(localDirPath));
		return resizedImage;
	}

	private void drawAllComponents(List<Component> componentList, BufferedImageEditor editor, int disX, int disY) {
		try {
			for (Component component : componentList) {
				component.shift(disX, disY);
				component.draw(editor);
			}
		} catch (Exception e) {
			logger.warn("Exception in  drawAllComponents {}", e.getMessage());
		}
	}

	private void drawAllShapes(List<Shape> shapeList, BufferedImageEditor editor, int disX, int disY) {
		try {
			for (Shape shape : shapeList) {
				shape.shift(disX, disY);
				shape.draw(editor);
			}
		} catch (Exception e) {
			logger.warn("Exception in drawAllShapes {}", e.getMessage());
		}
	}

	private List<String[]> shiftPointsOfTestParameters(List<String[]> originalList, int disX, int disY, Integer xPointIndex, Integer yPointIndex) {
		List<String[]> tempList = new ArrayList<>();
		for (String[] param : originalList) {
			if (param.length > xPointIndex) {
				if (ReportUtil.isValidPoint(param[xPointIndex],
						param[yPointIndex])) {
					double x = NumberUtils.toDouble(param[xPointIndex]) + disX;
					param[xPointIndex] = String.valueOf(x);
					double y = NumberUtils.toDouble(param[yPointIndex]) + disY;
					param[yPointIndex] = String.valueOf(y);
				}
				tempList.add(param);
			}
		}
		return tempList;
	}

	private List<String[]> shiftPointsOfTestParameters(List<String[]> originalList, int disX, int disY) {
		List<String[]> tempList = new ArrayList<>();
		for (String[] param : originalList) {
			if (param.length > DriveHeaderConstants.INDEX_XPOINT) {
				if (ReportUtil.isValidPoint(param[DriveHeaderConstants.INDEX_XPOINT],
						param[DriveHeaderConstants.INDEX_YPOINT])) {
					double x = NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_XPOINT]) + disX;
					param[DriveHeaderConstants.INDEX_XPOINT] = String.valueOf(x);
					double y = NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_YPOINT]) + disY;
					param[DriveHeaderConstants.INDEX_YPOINT] = String.valueOf(y);
				}
				tempList.add(param);
			}
		}
		return tempList;
	}
}
