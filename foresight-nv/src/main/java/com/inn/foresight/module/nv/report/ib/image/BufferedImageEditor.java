package com.inn.foresight.module.nv.report.ib.image;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.report.utils.ReportConstants;

/***********************************************************************
 * This class is to edit BufferedImage and draw shapes on it Currently it
 * supports Lines,Circles,Texts,Arcs and Images.One instance of this class must
 * be used for editing one image.
 *
 * @author Anand
 *
 **********************************************************************/
public class BufferedImageEditor {
	public static final String OUTPUT_FORMAT_PNG = "png";
	public static final String OUTPUT_FORMAT_JPEG = "jpeg";

	private Logger logger = LogManager.getLogger(BufferedImageEditor.class);
	/**
	 * Global BufferedImage Object.
	 */
	private BufferedImage mImage;
	/**
	 * Object used to paint on bufferedImage.
	 */
	private Graphics2D mGraphics;
	/**
	 * Default Width of line drawn and default radius of circle. If not supplied
	 * externally than this value is used.
	 */
	private static final int DEFAULT_WIDTH = 10;

	/**
	 * Default width of dashed line
	 */
	private static final int DEFAULT_DASH_WIDTH = 5;

	/*********************************************************
	 * Public constuctor
	 *
	 * @param filePath - Path of unedited source image.
	 ********************************************************/
	public BufferedImageEditor(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				throw new IllegalArgumentException("Supplied image file does not exists");
			}
			mImage = ImageIO.read(new File(filePath));
			mGraphics = mImage.createGraphics();
		} catch (IOException e) {
			logger.error("Exception inside the method BufferedImageEditor {}", Utils.getStackTrace(e));
		}
	}

	/***************************************************************************
	 * Public constructor, creates an empty image of specified width and height
	 *
	 * @param width  Desired width
	 * @param height Desired height
	 *******************************************************************/
	public BufferedImageEditor(int width, int height) {
		mImage = getBlankImage(width, height);
		mGraphics = mImage.createGraphics();
	}

	/********************************************************************
	 * Public constuctor
	 *
	 * @param image - Initialized BufferedImage object
	 *******************************************************************/
	public BufferedImageEditor(BufferedImage image) {
		if (image == null) {
			throw new NullPointerException("Can not assign null Image");
		}
		this.mImage = image;
		mGraphics = mImage.createGraphics();
	}

	/********************************************************************
	 * Public constructor
	 *
	 * @param file - File object of source image.
	 *******************************************************************/
	public BufferedImageEditor(File file) {
		this(file.getAbsolutePath());
		mGraphics = mImage.createGraphics();
	}

	/*************************************************************************
	 * Draws line between (x1,y1) and (x2,y2).
	 *
	 * @param x1    fromX
	 * @param y1    fromY
	 * @param x2    toX
	 * @param y2    toY
	 * @param color Color of Line
	 *************************************************************************/
	public void drawLine(int x1, int y1, int x2, int y2, Color color) {
		drawLine(x1, y1, x2, y2, color, DEFAULT_WIDTH);
	}

	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle, Color color,
			int strokeWidth) {
		mGraphics.setColor(color);
		mGraphics.setStroke(new BasicStroke(strokeWidth));
		mGraphics.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	/*************************************************************************
	 * Draws line between (x1,y1) and (x2,y2).
	 *
	 * @param x1    fromX
	 * @param y1    fromY
	 * @param x2    toX
	 * @param y2    toY
	 * @param color Color of Line
	 *************************************************************************/
	public void drawLine(Double x1, Double y1, Double x2, Double y2, Color color, int lineWidth) {
		mGraphics.setColor(color);
		drawLine(x1.intValue(), y1.intValue(), x2.intValue(), y2.intValue(), color, lineWidth);
	}

	/*************************************************************************
	 * Draws line between (x1,y1) and (x2,y2) of specified width.
	 *
	 * @param x1        fromX
	 * @param y1        fromY
	 * @param x2        toX
	 * @param y2        toY
	 * @param color     Color of Line
	 * @param lineWidth Width of the Line
	 *************************************************************************/
	public void drawLine(int x1, int y1, int x2, int y2, Color color, int lineWidth) {
		mGraphics.setColor(color);
		mGraphics.setStroke(new BasicStroke(lineWidth));
		mGraphics.drawLine(x1, y1, x2, y2);
	}

	public void drawLineWithArrow(int x1, int y1, int x2, int y2, Color color, int lineWidth) {
		mGraphics.setColor(color);
		mGraphics.setStroke(new BasicStroke(lineWidth));
		Polygon arrowHead = new Polygon();
		mGraphics.drawLine(x1, y1, x2, y2);
		arrowHead.addPoint(NVConstant.ZERO_INT, NVConstant.TEN_INT);
		arrowHead.addPoint(NVConstant.NEGATIVE_TEN, NVConstant.NEGATIVE_TEN);
		arrowHead.addPoint(NVConstant.TEN_INT, NVConstant.NEGATIVE_TEN);
		AffineTransform tx = new AffineTransform();
		tx.setToIdentity();

		double angle = Math.atan2((double) y1 - y2, (double) x1 - x2);
		tx.translate(x1, y1);
		tx.rotate((angle - Math.PI / 2d));
		mGraphics.create();
		mGraphics.setTransform(tx);
		mGraphics.fill(arrowHead);

	}

	/*************************************************************************
	 * Draws dashed line between (x1,y1) and (x2,y2) of specified width.
	 *
	 * @param x1        fromX
	 * @param y1        fromY
	 * @param x2        toX
	 * @param y2        toY
	 * @param color     Color of Line
	 * @param lineWidth Width of the Line
	 *************************************************************************/
	public void drawDashedLine(int x1, int y1, int x2, int y2, Color color, int lineWidth) {
		Stroke previous = mGraphics.getStroke();
		Stroke dashed = new BasicStroke(ReportConstants.INDEX_THREE, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, ReportConstants.INDEX_ZER0, new float[] { ReportConstants.INDEX_NINE }, ReportConstants.INDEX_ZER0);
		mGraphics.setColor(color);
		mGraphics.setStroke(dashed);
		mGraphics.drawLine(x1, y1, x2, y2);
		mGraphics.setStroke(previous);
	}

	/***************************************************************************
	 * Draws circle of specified radius centering at (centerX,centerY)
	 *
	 * @param centerX x center co-ordinate
	 * @param centerY y center co-ordinate
	 * @param color   Fill color of circle
	 * @param radius  Radius of circle
	 **************************************************************************/
	public void drawCircle(int centerX, int centerY, Color color, int radius) {
		Shape theCircle = new Ellipse2D.Double(Double.valueOf(centerX) - radius, Double.valueOf(centerY) - radius, 2.0 * radius, 2.0 * radius);
		mGraphics.setColor(color);
		mGraphics.fill(theCircle);

	}

	/***************************************************************************
	 * Draws circle of specified radius centering at (centerX,centerY)
	 *
	 * @param centerX x center co-ordinate
	 * @param centerY y center co-ordinate
	 * @param color   Only Drwa a Cicle with Border
	 * @param radius  Radius of circle
	 **************************************************************************/
	public void drawCircleWithOutFill(int centerX, int centerY, Color color, int radius) {
		Shape theCircle = new Ellipse2D.Double(Double.valueOf(centerX) - radius, Double.valueOf(centerY) - radius, 2.0 * radius, 2.0 * radius);
		mGraphics.setColor(color);
		mGraphics.setStroke(new BasicStroke(4));
		mGraphics.draw(theCircle);

	}

	/***************************************************************************
	 * Draws ellipse at specified points
	 *
	 * @param centerX x center co-ordinate
	 * @param centerY y center co-ordinate
	 * @param color   Fill color of circle
	 * @param radius  Radius of circle
	 **************************************************************************/
	public void drawEllipse(int startX, int startY, int endX, int endY, Color color) {
		Shape theCircle = new Ellipse2D.Double(startX, startY, endX, endY);
		mGraphics.setColor(color);
		mGraphics.fill(theCircle);
	}

	/***************************************************************************
	 * Draws ellipse at specified points
	 *
	 * @param centerX x center co-ordinate
	 * @param centerY y center co-ordinate
	 * @param color   Fill color of circle
	 * @param radius  Radius of circle
	 **************************************************************************/
	public void drawOval(int startX, int startY, int width, int height, Color color) {
		mGraphics.setColor(color);
		mGraphics.drawOval(startX, startY, width, height);
	}

	/***************************************************************************
	 * Draws circle of default radius(20px) centering at (centerX,centerY)
	 *
	 * @param centerX x center co-ordinate
	 * @param centerY y center co-ordinate
	 * @param color   Fill color of circle
	 **************************************************************************/
	public void drawCircle(int x1, int y1, Color color) {
		mGraphics.setColor(color);
		drawCircle(x1, y1, color, DEFAULT_WIDTH);
	}

	/***************************************************************************
	 * Draws circle of specified radius centering at (point.getX().intValue(),
	 * point.getY().intValue())
	 *
	 * @param point  Center point of circle
	 * @param color  Fill color of circle
	 * @param radius Radius of circle
	 **************************************************************************/
	public void drawCircle(PointF point, Color color, int radius) {
		drawCircle(point.getX().intValue(), point.getY().intValue(), color, radius);
	}

	/***************************************************************************
	 * Draws circle of default radius(20px) centering at (centerX,centerY)
	 *
	 * @param point Center point of circle
	 * @param color Fill color of circle
	 **************************************************************************/
	public void drawCircle(PointF point, Color color) {
		drawCircle(point.getX().intValue(), point.getY().intValue(), color, DEFAULT_WIDTH);
	}

	/**************************************************************************
	 * Draw line through supplied List of points and draw circle of double line
	 * width at each point.
	 *
	 * @param points    List List of points through which lines is to be drawn
	 * @param color     Color of drawn lines
	 * @param lineWidth Width of drawn lines
	 *************************************************************************/
	public void drawLine(List<PointF> pointsList, int lineWidth, boolean isToJoinEnds) {
		for (int i = 0; i < pointsList.size() - 1; i++) {
			PointF startPoint = pointsList.get(i);
			PointF destinationPoint = pointsList.get(i + 1);
			drawLine(startPoint.getX().intValue(), startPoint.getY().intValue(), destinationPoint.getX().intValue(),
					destinationPoint.getY().intValue(), Color.GRAY, lineWidth);
		}
		if (isToJoinEnds) {
			try {

				PointF startPoint = pointsList.get(0);
				PointF destinationPoint = pointsList.get(pointsList.size() - 1);
				drawLine(startPoint.getX().intValue(), startPoint.getY().intValue(), destinationPoint.getX().intValue(),
						destinationPoint.getY().intValue(), Color.GRAY, lineWidth);
			} catch (Exception e) {
				logger.error("Error in drawLine");
			}
		}
	}

	/**************************************************************************
	 * Draw dashed line through supplied List of points and draw circle of default
	 * line width at each point.
	 *
	 * @param points    List List of points through which lines is to be drawn
	 * @param color     Color of drawn lines
	 * @param lineWidth Width of drawn lines
	 *************************************************************************/
	public void drawPath(List<PointF> pointsList, List<Color> colorList) {
		if (pointsList.size() == 1 && colorList.size() > 0) {
			try {
				drawCircle(pointsList.get(0).getX().intValue(), pointsList.get(0).getY().intValue(), colorList.get(0));
			} catch (Exception e) {
				logger.info("Exception in drawing path of list having 1 item:" + e.getMessage());
			}
			return;
		}
		for (int i = 0; i < pointsList.size() - 1; i++) {
			try {
				if (colorList.size() >= i) {
					drawDashedLine(pointsList.get(i).getX().intValue(), pointsList.get(i).getY().intValue(),
							pointsList.get(i + 1).getX().intValue(), pointsList.get(i + 1).getY().intValue(), colorList.get(i),
							DEFAULT_DASH_WIDTH);
					drawCircle(pointsList.get(i).getX().intValue(), pointsList.get(i).getY().intValue(), colorList.get(i));
				}
			} catch (Exception e) {
				logger.info("Exception in drawing path:" + e.getMessage());
			}

		}
		try {
			drawCircle(pointsList.get(0).getX().intValue(), pointsList.get(0).getY().intValue(), colorList.get(0));
			drawCircle(pointsList.get(pointsList.size() - 1).getX().intValue(),
					pointsList.get(pointsList.size() - 1).getY().intValue(), colorList.get(colorList.size() - 1));
		} catch (Exception e) {
			logger.error("Exception inside the method drawPath {}", Utils.getStackTrace(e));
		}

	}
	
	public void drawPath(List<PointF> pointsList, List<Color> colorList,int radius) {
		if (pointsList.size() == 1) {
			try {
				drawCircle(pointsList.get(0).getX().intValue(), pointsList.get(0).getY().intValue(), colorList.get(0),radius);
			} catch (Exception e) {
				logger.info("Exception in drawing path of list having 1 item:" + e.getMessage());
			}
			return;
		}
		for (int i = 0; i < pointsList.size() - 1; i++) {
			try {
				drawDashedLine(pointsList.get(i).getX().intValue(), pointsList.get(i).getY().intValue(),
						pointsList.get(i + 1).getX().intValue(), pointsList.get(i + 1).getY().intValue(), colorList.get(i),
						DEFAULT_DASH_WIDTH);
				drawCircle(pointsList.get(i).getX().intValue(), pointsList.get(i).getY().intValue(), colorList.get(i),radius);
			} catch (Exception e) {
				logger.info("Exception in drawing path:" + e.getMessage());
			}

		}
		try {
			drawCircle(pointsList.get(0).getX().intValue(), pointsList.get(0).getY().intValue(), colorList.get(0),radius);
			drawCircle(pointsList.get(pointsList.size() - 1).getX().intValue(),
					pointsList.get(pointsList.size() - 1).getY().intValue(), colorList.get(colorList.size() - 1),radius);
		} catch (Exception e) {
			logger.error("Exception inside the method drawPath {}", Utils.getStackTrace(e));
		}

	}

	/************************************************************************
	 * Draws a blank Image of specified width and height.
	 * 
	 * @param sizeX Width of desired Image.
	 * @param sizeY Height of desired Image.
	 * @return
	 ************************************************************************/
	private BufferedImage getBlankImage(int sizeX, int sizeY) {
		final BufferedImage res = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				res.setRGB(x, y, Color.WHITE.getRGB());
			}
		}
		return res;
	}

	/*************************************************************************
	 * Finalize the whole editing and export it to specified filePath
	 *
	 * @param filePath     Output file Path
	 * @param outputFormat Format of the output file
	 * @return File object of edited Image.
	 *************************************************************************/

	public File finalizeImageToOutput(String filePath, String outputFormat) {
		File outputfile = new File(filePath);
		if (outputfile.exists()) {
			boolean delete = outputfile.delete();
			logger.info("Delete file {}",delete);
		}
		try {
			ImageIO.write(mImage, outputFormat, outputfile);
		} catch (IOException e) {
			logger.error("Exception inside the method finalizeImageToOutput {}", Utils.getStackTrace(e));
		}
		return outputfile;
	}

	/****************************************************************
	 * Draws Image with centerPoint as center of Image
	 *
	 * @param image       Image to draw
	 * @param centerPoint Center point of image
	 *********************************************************************/
	public void drawImage(BufferedImage image, PointF centerPoint) {
		logger.info("Going to draw Image {}", centerPoint);
		int height = image.getHeight();
		int width = image.getWidth();
		drawImageWithPointAsTopLeft(image,
				new PointF(centerPoint.getX().intValue() - (width / 2.0), centerPoint.getY().intValue() - (height / 2.0)));
	}

	/******************************************************************
	 * Draws Image with centerPoint as top-left of Image
	 *
	 * @param image       Image to draw
	 * @param centerPoint Top left point of image
	 *******************************************************************/
	public void drawImageWithPointAsTopLeft(BufferedImage image, PointF centerPoint) {
		logger.info("Going to draw Image {}", centerPoint);
		mGraphics.drawImage(image, centerPoint.getX().intValue(), centerPoint.getY().intValue(), null);
	}

	/********************************************************************
	 * Draw text starting from point
	 *
	 * @param point    Starting point of text
	 * @param string   Text to draw
	 * @param Desired  color of text
	 * @param fontSize Desired font size
	 *******************************************************************/
	public void drawText(PointF point, String string, Color color, int fontSize) {
		Font font = new Font("Arial", Font.PLAIN, fontSize);
		mGraphics.setFont(font);
		mGraphics.setColor(color);
		mGraphics.drawString(string, point.getX().intValue(), point.getY().intValue());
	}

	/********************************************************************
	 * Draw text starting from point and rotate it with specified angle
	 *
	 * @param x           x position of point
	 * @param y           y position of point
	 * @param rotateAngle Desired angle of text rotation
	 * @param string      Text to draw
	 * @param Desired     color of text
	 * @param fontSize    Desired font size
	 *******************************************************************/
	public void drawText(float angle, String text, int x, int y, Color color, int fontSize) {
		mGraphics.rotate(Math.toRadians(angle), x, y);
		drawText(new PointF(x, y), text, color, fontSize);
		mGraphics.rotate(-Math.toRadians(angle), x, y);
	}

	/*********************************************************************
	 * Draw two color gradient between points
	 *
	 * @param srcPoint  Starting point of Gradient
	 * @param destPoint End point of Gradient
	 * @param srcColor  Starting color of gradient
	 * @param destColor End color of gradient
	 ********************************************************************/
	public void drawGradient(PointF srcPoint, PointF destPoint, Color srcColor, Color destColor) {
		Paint prevPaint = mGraphics.getPaint();
		GradientPaint paint = new GradientPaint(srcPoint.getX().floatValue(), srcPoint.getY().floatValue(), srcColor,
				destPoint.getX().floatValue(), destPoint.getY().floatValue(), destColor);
		mGraphics.setPaint(paint);
		mGraphics.setStroke(new BasicStroke(DEFAULT_WIDTH * 2f));
		mGraphics.drawLine(srcPoint.getX().intValue(), srcPoint.getY().intValue(), destPoint.getX().intValue(),
				destPoint.getY().intValue());
		mGraphics.setPaint(prevPaint);
	}

}
