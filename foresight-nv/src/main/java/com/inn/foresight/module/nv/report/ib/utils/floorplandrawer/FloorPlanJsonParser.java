package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer;

import com.inn.commons.lang.NumberUtils;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.ib.image.PointF;
import com.inn.foresight.module.nv.report.ib.image.RectF;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.components.Component;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.components.*;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.Label;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.Polygon;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.Shape;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.*;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.Window;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes.ShapeUtils.ThickNess;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.InbuildingPointWrapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FloorPlanJsonParser {
	private Logger logger = LogManager.getLogger(FloorPlanJsonParser.class);
	public static final  FloorPlanJsonParser instance = new FloorPlanJsonParser();

	public List<Component> getComponentListFromJson(String json) throws JSONException  {
		List<Component> tempList = new ArrayList<>();
		JSONObject obj;
		obj = new JSONObject(json);
		JSONArray jsonArray = obj.getJSONArray("componentList");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject componentJsonObject = jsonArray.getJSONObject(i);
			Component component = null;
			if (ShapeConstants.AP.equalsIgnoreCase(componentJsonObject.getString("type"))) {
				component = new ComponentAP(getPointF(componentJsonObject, ShapeConstants.POINTF));
			} else if (ShapeConstants.CABINET.equalsIgnoreCase(componentJsonObject.getString("type"))) {
				if (ShapeConstants.CABINET_TYPE_1.equalsIgnoreCase(componentJsonObject.getString("cabinetType"))) {
					component = new ComponentCabinet(getPointsFromJsonArray(componentJsonObject, "vertices"));
				} else {
					component = new ComponentCabinet(getPointsFromJsonArray(componentJsonObject, "verticesSizeTwo"));
				}
			} else if (ShapeConstants.SWITCH.equalsIgnoreCase(componentJsonObject.getString("type"))) {
				component = new ComponentSwitch(getPointF(componentJsonObject, ShapeConstants.POINTF));
			} else if (ShapeConstants.CABLE.equalsIgnoreCase(componentJsonObject.getString("type"))) {
				component = new Cable(getCablePointsList(componentJsonObject));
			} else if (ShapeConstants.SMALL_CELL.equalsIgnoreCase(componentJsonObject.getString("type"))) {
				component = new ComponentSmallCell(getPointF(componentJsonObject, ShapeConstants.POINTF));
			}
			if (component != null) {
				tempList.add(component);
			}
		}
		return tempList;
	}

	/** IsBackgroundImageAvailable. 
	 * @throws JSONException */
	public static boolean isImagePickedFromGallery(String json) throws JSONException  {
		JSONObject obj = new JSONObject(json);
		return obj.getBoolean("isBackgroundImageAvailable");
	}

	private List<PointF> getCablePointsList(JSONObject componentJsonObject) throws JSONException  {
		List<PointF> tempList = new ArrayList<>();
		JSONArray arr = componentJsonObject.getJSONArray("walls");
		for (int i = 0; i < arr.length(); i++) {
			tempList.add(getPointF(arr.getJSONObject(i), "startPoint"));
			tempList.add(getPointF(arr.getJSONObject(i), "endPoint"));
		}
		return tempList;
	}

	private List<PointF> getPointsFromJsonArray(JSONObject obj, String key) throws JSONException  {
		List<PointF> tempList = new ArrayList<>();
		JSONArray arr = obj.getJSONArray(key);
		for (int i = 0; i < arr.length(); i++) {
			JSONObject vertex = arr.getJSONObject(i);
			PointF point = new PointF(vertex.getDouble("x"), vertex.getDouble("y"));
			tempList.add(point);
		}
		return tempList;
	}

	public List<Shape> getShapeListFromJson(String json) throws JSONException {
		List<Shape> tempList = new ArrayList<>();
		JSONObject obj = new JSONObject(json);
		JSONArray jsonArray = obj.getJSONArray("shapeList");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject shapeJsonObject = jsonArray.getJSONObject(i);
			Shape shape = null;
			if (ShapeConstants.WALL.equals(shapeJsonObject.getString("type"))) {
				shape = new Wall(getPointsForWall(shapeJsonObject), getThickness(shapeJsonObject));
			} else if (ShapeConstants.SQUARE.equalsIgnoreCase(shapeJsonObject.getString("type")) || ShapeConstants.LSHAPE.equalsIgnoreCase(shapeJsonObject.getString("type"))) {
				shape = new Polygon(getPointsFromJsonArray(shapeJsonObject.getJSONArray("walls"), "startPoint"),
						getThickness(shapeJsonObject));
			} else if (ShapeConstants.CIRCLE.equalsIgnoreCase(shapeJsonObject.getString("type"))) {
				shape = new Circle(
						getPointsFromJsonArray(shapeJsonObject.getJSONArray("selectionWalls"), "startPointSelected"));
			} else if (ShapeConstants.POLYGON.equalsIgnoreCase(shapeJsonObject.getString("type"))) {
				shape = new Polygon(getPointsFromJsonArray(shapeJsonObject.getJSONArray("walls"), "startPoint"),
						getThickness(shapeJsonObject));
			} else if (ShapeConstants.STAIRE.equalsIgnoreCase(shapeJsonObject.getString("type"))) {
				shape = new StairCase(getPointsFromJsonArray(shapeJsonObject.getJSONArray("walls"), "startPoint"),
						getThickness(shapeJsonObject));
			} else if (ShapeConstants.WINDOW.equalsIgnoreCase(shapeJsonObject.getString("type"))) {
				shape = getWindowObject(shapeJsonObject);
			} else if (ShapeConstants.DOOR.equalsIgnoreCase(shapeJsonObject.getString("type"))) {
				shape = getDoorObject(shapeJsonObject);
			} else if (ShapeConstants.LABEL.equalsIgnoreCase(shapeJsonObject.getString("type"))) {
				shape = getLabelObject(shapeJsonObject);
			}
			if (shape != null) {
				tempList.add(shape);
			}
		}
		return tempList;
	}

	private Shape getLabelObject(JSONObject shapeJsonObject) throws JSONException  {
		PointF startPoint = new PointF(shapeJsonObject.getDouble("x1"), shapeJsonObject.getDouble("y1"));
		PointF endPoint = new PointF(shapeJsonObject.getDouble("x2"), shapeJsonObject.getDouble("y2"));
		return new Label(startPoint, endPoint, shapeJsonObject.getString("labelText"),
				shapeJsonObject.getString("color"), shapeJsonObject.getDouble("rotatecanvas"),
				shapeJsonObject.getInt("size"));
	}

	private Shape getDoorObject(JSONObject shapeJsonObject) throws JSONException  {
		Door door = new Door();
		door.setStartPoint(getPointF(shapeJsonObject, "startPoint"));
		door.setEndPoint(getPointF(shapeJsonObject, "endPoint"));
		door.setFacingAngle(shapeJsonObject.getDouble("facingAngle"));
		door.setRotationAngle((int) shapeJsonObject.getDouble("rotationAngle"));
		door.setStartAngle(shapeJsonObject.getInt("startAngle"));
		JSONObject rectJson = shapeJsonObject.getJSONObject("rect");
		RectF rect = new RectF(rectJson.getDouble("bottom"), rectJson.getDouble("left"), rectJson.getDouble("right"),
				rectJson.getDouble("top"));
		door.setRect(rect);
		String rectCenterSelected = shapeJsonObject.getString("rectCenterSelected");
		if ("START_POINT".equalsIgnoreCase(rectCenterSelected)) {
			door.setBackLineEndPoint(door.getStartPoint());
			door.setOtherPoint(getPointF(shapeJsonObject, "endPoint"));
		} else {
			door.setOtherPoint(getPointF(shapeJsonObject, "startPoint"));
			door.setBackLineEndPoint(door.getEndPoint());
		}
		door.setBackLineStartPoint(getPointF(shapeJsonObject, "rotationPoint"));
		try {
			door.setThickNess(getThickness(shapeJsonObject));
		} catch (JSONException e) {
			logger.error("JSONException in getDoorObject {} ",ExceptionUtils.getStackTrace(e));
		}
		return door;
	}

	private Shape getWindowObject(JSONObject shapeJsonObject) throws JSONException  {
		Window window = new Window();
		window.setStartPoint(getPointF(shapeJsonObject, "startPoint"));
		window.setEndPoint(getPointF(shapeJsonObject, "endPoint"));
		JSONObject associatedWall = shapeJsonObject.getJSONObject("associatedWall");
		window.setAssociatedWallEndPoint(getPointF(associatedWall, "endPoint"));
		window.setAssociatedWallStartPoint(getPointF(associatedWall, "startPoint"));
		return window;
	}

	private PointF getPointF(JSONObject obj, String key) throws JSONException  {
		float x;
		float y;
		x = (float) obj.getJSONObject(key).getDouble("x");
		y = (float) obj.getJSONObject(key).getDouble("y");
		return new PointF(x, y);
	}
	/**
	 * InnPoint startPoint = new InnPoint((float)
	 * object.getJSONObject("startPoint").getDouble("x"), (float)
	 * object.getJSONObject("startPoint").getDouble("y")); InnPoint endPoint = new
	 * InnPoint((float) object.getJSONObject("endPoint").getDouble("x"), (float)
	 * object.getJSONObject("endPoint").getDouble("y"));
	 * @throws JSONException 
	 */

	private ThickNess getThickness(JSONObject shapeJsonObject) throws JSONException {
		String thickNess = shapeJsonObject.getString("thickness");
		if (ThickNess.THICK.toString().equalsIgnoreCase(thickNess)) {
			return ThickNess.THICK;
		} else if (ThickNess.THIN.toString().equalsIgnoreCase(thickNess)) {
			return ThickNess.THIN;
		} else if (ThickNess.MEDIUM.toString().equalsIgnoreCase(thickNess)) {
			return ThickNess.MEDIUM;
		}
		return null;
	}

	private List<PointF> getPointsForWall(JSONObject shapeJsonObject) throws JSONException  {
		List<PointF> tempList = new ArrayList<>();
		JSONObject startPoint = shapeJsonObject.getJSONObject("startPoint");
		PointF point = new PointF(startPoint.getDouble("x"), startPoint.getDouble("y"));
		tempList.add(point);

		JSONObject endPoint = shapeJsonObject.getJSONObject("endPoint");
		PointF pointEnd = new PointF(endPoint.getDouble("x"), endPoint.getDouble("y"));
		tempList.add(pointEnd);

		return tempList;
	}

	private List<PointF> getPointsFromJsonArray(JSONArray arr, String key) throws JSONException  {
		List<PointF> tempList = new ArrayList<>();
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			JSONObject startPoint = obj.getJSONObject(key);
			PointF point = new PointF(startPoint.getDouble("x"), startPoint.getDouble("y"));
			tempList.add(point);
		}
		return tempList;
	}

	public int getWidth(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return obj.getInt("floorBitmapWidth");
		} catch (JSONException e) {
			logger.error("Exception in getting floor plan image width {}",
					Utils.getStackTrace(e));
		}
		return 4000;
	}

	public Float[] getMinimumXYPoint(List<Shape> shapeList, List<Component> componentList, BufferedImage localBitmap) {
		float bitmapMinX = 0;
		float bitmapMinY = 0;
		float bitmapMaxX = 0;
		float bitmapMaxY = 0;
		float minX = 0;
		float minY = 0;
		float maxX = 0;
		float maxY = 0;
		int boundaryDiff = 100;
		List<Float> pointListX = new ArrayList<>();
		List<Float> pointListY = new ArrayList<>();
		if (shapeList.isEmpty() && componentList.isEmpty()) {
			boundaryDiff = 0;
		}
		if ( !shapeList.isEmpty()) {
			for (Shape shape : shapeList) {
				pointListX.addAll(shape.getAllX());
				pointListY.addAll(shape.getAllY());
			}
		}
		if (!componentList.isEmpty()) {
			for (Component component : componentList) {
				pointListX.addAll(component.getAllX());
				pointListY.addAll(component.getAllY());
			}
		}

		if (!pointListX.isEmpty()&& !pointListY.isEmpty() ) {
			Collections.sort(pointListX);
			Collections.sort(pointListY);
			minX = pointListX.get(0);
			minY = pointListY.get(0);
			maxX = pointListX.get(pointListX.size() - 1);
			maxY = pointListY.get(pointListY.size() - 1);
		}
		if (localBitmap != null) {
			boundaryDiff = 100;
			bitmapMinX = 0;
			bitmapMinY = 0;
			bitmapMaxX = localBitmap.getWidth();
			bitmapMaxY = localBitmap.getHeight();
			if (minX > bitmapMinX) {
				minX = bitmapMinX;
			}
			if (minY > bitmapMinY) {
				minY = bitmapMinY;
			}
			if (maxX < bitmapMaxX) {
				maxX = bitmapMaxX;
			}
			if (maxY < bitmapMaxY) {
				maxY = bitmapMaxY;
			}

			maxX += boundaryDiff;
			maxY += boundaryDiff;
		}
		logger.debug("Minimum x and y are {} and {}", minX, minY);
		return new Float[] { minX, minY, maxX, maxY };
	}

	public int getHeight(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return obj.getInt("floorBitmapHeight");
		} catch (JSONException e) {
			logger.error("Exception in getting floor plan image height {}",
					Utils.getStackTrace(e));
		}
		return 4000;
	}

	public Double[] getMinimumXYPoint(List<String[]> arlist, Integer xPointIndex, Integer yPointIndex) {
		double minX = 0f;
		double minY = 0f;
		double maxX = 0f;
		double maxY = 0f;
		for (String[] param : arlist) {
			try {
				if(param.length>xPointIndex && ReportUtil.isValidPoint(param[xPointIndex],
						param[yPointIndex])) {
					if (NumberUtils.toDouble(param[xPointIndex]) < minX) {
						minX = NumberUtils.toDouble(param[xPointIndex]);
					} else if (NumberUtils.toDouble(param[xPointIndex]) > maxX) {
						maxX = NumberUtils.toDouble(param[xPointIndex]);
					}
					if (NumberUtils.toDouble(param[yPointIndex]) < minY) {
						minY = NumberUtils.toDouble(param[yPointIndex]);
					} else if (NumberUtils.toDouble(param[yPointIndex]) > maxY) {
						maxY = NumberUtils.toDouble(param[yPointIndex]);
					}
				}
			} catch (Exception e) {
				logger.error("Exception inside the method {}", Utils.getStackTrace(e));
			}
		}
		return new Double[] { minX, minY, maxX, maxY };
	}

	public Double[] getMinimumXYPoint(List<String[]> arlist) {
		double minX = 0f;
		double minY = 0f;
		double maxX = 0f;
		double maxY = 0f;
		for (String[] param : arlist) {
			try {
				if(param.length>DriveHeaderConstants.INDEX_XPOINT&&ReportUtil.isValidPoint(param[DriveHeaderConstants.INDEX_XPOINT],
						param[DriveHeaderConstants.INDEX_YPOINT])) {
					if (NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_XPOINT]) < minX) {
						minX = NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_XPOINT]);
					} else if (NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_XPOINT]) > maxX) {
						maxX = NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_XPOINT]);
					}
					if (NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_YPOINT]) < minY) {
						minY = NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_YPOINT]);
					} else if (NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_YPOINT]) > maxY) {
						maxY = NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_YPOINT]);
					}
				}
			} catch (Exception e) {
				logger.error("Exception inside the method {}", Utils.getStackTrace(e));
			}
		}
		return new Double[] { minX, minY, maxX, maxY };
	}


	public Double[] getMinMaxShiftedPoint(InbuildingPointWrapper pointWrapper) {
		double minX = 0f;
		double minY = 0f;
		double maxX = 0f; 
		double maxY = 0f;
		if (pointWrapper != null && !pointWrapper.getArlist().isEmpty()) {
			for (String[] param : pointWrapper.getArlist()) {
				try {
					if (NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_XPOINT]) < minX) {
						minX = NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_XPOINT]);
					} else if (NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_XPOINT]) > maxX) {
						maxX = NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_XPOINT]);
					}
					if (NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_YPOINT]) < minY) {
						minY = NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_YPOINT]);
					} else if (NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_YPOINT]) > maxY) {
						maxY = NumberUtils.toDouble(param[DriveHeaderConstants.INDEX_YPOINT]);
					}
				} catch (Exception e) {
					logger.error("Exception inside the method {}", Utils.getStackTrace(e));
				}
			}
		}
		return new Double[] { minX, minY, maxX, maxY };
	}

	public Float[] getBitmapWidthAndHeight(String json) {
		Float[] tmp = new Float[2];
		try {
			JSONObject obj = new JSONObject(json);
			tmp[0] = (float) obj.getDouble("localBitmapWidth");
			tmp[1] = (float) obj.getDouble("localBitmapHeight");
		} catch (JSONException e) {
			logger.error("Exception inside the method getBitmapWidthAndHeight {}", Utils.getStackTrace(e));
		}
		return tmp;
	}

	public boolean isPortraitImage(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return obj.has("isPortraitImage") ? obj.getBoolean("isPortraitImage") : false;
		} catch (JSONException e) {
			logger.error("Exception inside the method getBitmapWidthAndHeight {}", Utils.getStackTrace(e));
		}
		return false;
	}

	public String[] getScalePixelPerUnitAndUnit(String json) throws JSONException {
		JSONObject obj;
		obj = new JSONObject(json);
		return new String[] { obj.getString("scalePixelPerUnit"), obj.getString("unit") };
	}

	public static BufferedImage resizeImage(int width, int height, BufferedImage originalImage, int type,
			boolean isPortraitImage) {
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		BufferedImage image = null;
		if(isPortraitImage) {
			image = rotateImageByDegrees(originalImage, 90.0);
		} else{
			image = originalImage;
		}
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}
	
	public static BufferedImage rotate(BufferedImage bufferedImage) {
	 AffineTransform at = new AffineTransform();
	 at.rotate(90,bufferedImage.getWidth() /2d, bufferedImage.getHeight() / 2d);
	 AffineTransformOp op = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
	 return op.filter(bufferedImage,null);
	}
	public static BufferedImage rotate(BufferedImage img, int rotation,int width, int height) 
	{
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage newImage = new BufferedImage(width, height, img.getType());
		Graphics2D g2 = newImage.createGraphics();
		g2.rotate(Math.toRadians(rotation), w / 2d, h / 2d);
		g2.drawImage(img, null, 0, 0);
		return newImage;
	}

	public static BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {

		double rads = Math.toRadians(angle);
		double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
		int w = img.getWidth();
		int h = img.getHeight();
		int newWidth = (int) Math.floor(w * cos + h * sin);
		int newHeight = (int) Math.floor(h * cos + w * sin);

		BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = rotated.createGraphics();
		AffineTransform at = new AffineTransform();
		at.translate((newWidth - w) / 2.0, (newHeight - h) / 2.0);

		int x = w / 2;
		int y = h / 2;

		at.rotate(rads, x, y);
		g2d.setTransform(at);
		g2d.drawImage(img, 0, 0, null);
		g2d.setColor(Color.RED);
		g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
		g2d.dispose();

		return rotated;
	}
}
