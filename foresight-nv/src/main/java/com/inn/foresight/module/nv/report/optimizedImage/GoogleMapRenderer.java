package com.inn.foresight.module.nv.report.optimizedImage;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;

public class GoogleMapRenderer implements ColorConstants {
	private static final Logger logger = LogManager.getLogger(GoogleMapRenderer.class);

	private final HashMap<Object, Object> hints;
	BufferedImage pinImg;

	public GoogleMapRenderer() {
		hints = new HashMap<>();
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	}

	public void drawBoundary(BufferedImage image, List<List<Double>> boundary,double[] latLonBounds, Color c, double pixelLength) {
		//logger.info("Inside method pixelLength {} , drawBoundary {} ",pixelLength ,boundary);
		try {
			Graphics2D graphics = organizeGraphics(image, c, 2.0f);
			double latOrigin = latLonBounds[ReportConstants.INDEX_ZER0];
			double lonOrigin = latLonBounds[ReportConstants.INDEX_ONE];
			double latExtent = latLonBounds[ReportConstants.INDEX_TWO] - latLonBounds[ReportConstants.INDEX_ZER0];
			double lonExtent = latLonBounds[ReportConstants.INDEX_THREE] - latLonBounds[ReportConstants.INDEX_ONE];
			boolean flag = true;
			Path2D path = new Path2D.Double();
			for (List<Double> latlon : boundary) {
				double ly1 = (pixelLength * (latlon.get(ReportConstants.INDEX_ONE) - latOrigin)) / latExtent;
				double lx1 = (pixelLength * (latlon.get(ReportConstants.INDEX_ZER0) - lonOrigin)) / lonExtent;
				int ly = (int) ((int) pixelLength - ly1);
				int lx = (int) lx1 - ReportConstants.INDEX_ONE;
				if (flag) {
					path.moveTo(lx, ly);
					flag = false;
				}
				path.lineTo(lx, ly);
			}
			path.closePath();
			graphics.draw(path);
		} catch (Exception e) {
			logger.error("Exception inside method drawBoundary {} ",e.getMessage());
		}
	}

	public void fillBoundary(BufferedImage image, List<List<Double>> boundary,double[] latLonBounds, Color c, double pixelLength) {
		//logger.info("Inside method pixelLength {} , drawBoundary {} ",pixelLength ,boundary);
		try {
			Color alphaColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), 95);
			Graphics2D graphics = organizeGraphics(image, alphaColor, BOUNDARY_THICKNESS);
			double latOrigin = latLonBounds[ReportConstants.INDEX_ZER0];
			double lonOrigin = latLonBounds[ReportConstants.INDEX_ONE];
			double latExtent = latLonBounds[ReportConstants.INDEX_TWO] - latLonBounds[ReportConstants.INDEX_ZER0];
			double lonExtent = latLonBounds[ReportConstants.INDEX_THREE] - latLonBounds[ReportConstants.INDEX_ONE];
			boolean flag = true;
			Path2D path = new Path2D.Double();
			for (List<Double> latlon : boundary) {
				double ly1 = (pixelLength * (latlon.get(ReportConstants.INDEX_ONE) - latOrigin)) / latExtent;
				double lx1 = (pixelLength * (latlon.get(ReportConstants.INDEX_ZER0) - lonOrigin)) / lonExtent;
				int ly = (int) ((int) pixelLength - ly1);
				int lx = (int) lx1 - ReportConstants.INDEX_ONE;
				if (flag) {
					path.moveTo(lx, ly);
					flag = false;
				}
				path.lineTo(lx, ly);
			}
			path.closePath();
			graphics.fill(path);
		} catch (Exception e) {
			logger.error("Exception inside method drawBoundary {} ",Utils.getStackTrace(e));
		}
	}

	private Graphics2D organizeGraphics(BufferedImage image, Color c, float thickness) {
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.addRenderingHints(hints);
		BasicStroke stroke = new BasicStroke(thickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
		graphics.setStroke(stroke);
		graphics.setColor(c);
		return graphics;
	}

	public void drawBubble(BufferedImage image, int lx, int ly, Color c, int size) {
		try {
			Graphics2D graphics = organizeGraphics(image, c, 1.0f);
			graphics.setColor(c);
			graphics.fillOval(lx - size / 2, ly - size / 2, size, size);
		} catch (Exception e) {
			logger.info("error in drawing bubble ");
		}
	}

	public void drawTextOnImage(BufferedImage image, int lx, int ly, Color c, String text){
		Graphics2D graphics = organizeGraphics(image, c, 1.0f);
		graphics.setFont(new Font("Arial", Font.PLAIN, 32));
		graphics.drawString(text, lx - 20, ly);
	}

	public void renderSiteData(BufferedImage image, int lx, int ly, Color color, int azimuth, int size, int fanWidth, int innerCircle, int outerCircle, String siteName) {
		try {
			Graphics2D graphics = organizeGraphics(image, color, 2.0f);
			Color value = color;
			int angle = getAngleFromAzimuth(azimuth);
			drawSiteWithoutBorder(graphics, lx, ly, size, fanWidth, innerCircle, outerCircle, value, angle, siteName);
		} catch (Exception e) {
			//logger.error(Utils.getStackTrace(e));
		}
	}

	private void drawSiteCenter(Graphics2D graphics, int size, int innerCircle, int outerCircle, int y, int x) {
		graphics.setColor(OUTERCIRCLE_COLOR);
		graphics.fillOval(x + size / 2 - outerCircle / 2, y + size / 2 - outerCircle / 2, outerCircle, outerCircle);
		graphics.setColor(INNERCIRCLE_COLOR);
		graphics.fillOval(x + size / 2 - innerCircle / 2, y + size / 2 - innerCircle / 2, innerCircle, innerCircle);
	}

	private int getAngleFromAzimuth(int azimuth) {
		int angle = azimuth;
		if (ReportConstants.FOUR_FIVE_ZERO - angle > ReportConstants.THREE_SIX_ZER0) {
            angle = ReportConstants.FOUR_FIVE_ZERO - angle - ReportConstants.THREE_SIX_ZER0;
        } else {
            angle = ReportConstants.FOUR_FIVE_ZERO - angle;
        }
		return angle;
	}

	private void drawSiteWithoutBorder(Graphics2D graphics, int x, int y, int size, int fanWidth, int innerCircle, int outerCircle, Color color, int angle, String siteName) {
		try {
			int modY = y - size / 2;
			int modX = x - size / 2;
			graphics.setColor(color);
			graphics.fillArc(modX, modY, size, size, angle - ReportConstants.INDEX_THIRTY, fanWidth);
			drawSiteCenter(graphics, size, innerCircle, outerCircle, modY, modX);
			setSiteNameOnMap(graphics, x, y, size, siteName);
		} catch (Exception e) {
			logger.error("Exception inside method drawSiteWithoutBorder {} ",Utils.getStackTrace(e));
		}
	}

	private void setSiteNameOnMap(Graphics2D graphics, int x, int y, int size, String siteName) {
		if (!StringUtils.isBlank(siteName)) {
			y = y + size;
			x = x - size / 2;
			graphics.setColor(Color.BLACK);
			graphics.drawString(siteName, x, y);
		}
	}

	public void drawHoles(BufferedImage image, int lx, int ly, int diameter, Color color, String name) {
		Graphics2D graphics = organizeGraphics(image, color, 3.0f);
		int radius = diameter / 2;
		graphics.setColor(color);
		graphics.drawOval(lx - radius, ly - radius, diameter, diameter);
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Times New Roman", Font.BOLD, ReportConstants.FOURTY));
		graphics.drawString(name.toUpperCase(), lx + radius - ReportConstants.TWENTY, ly + radius);
	}

	public void pastePinOnImage(BufferedImage image, int lx, int ly, String path) {
		logger.info("Going to paste pin on Image {} ",image);
		logger.info("Going to paste pin from Path {}, {}, {}",path,lx,ly);
		try {
			pinImg  = ImageIO.read(new File(path));
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.addRenderingHints(hints);
			Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
			graphics.setComposite(composite);
			graphics.drawImage(pinImg, lx-ReportConstants.TEN, ly-ReportConstants.TWENTY, null);
		}catch (IOException e) {
			logger.info("IOException occured insdie method pastePinOnImage {} ",e.getMessage());
		} 
		catch (Exception e) {
			logger.info("Exception during pasting of Pin Image {} ",e.getMessage());
		}
	}

	public void drawCustomRoute(BufferedImage image, List<List<Double>> boundary, double[] latLonBounds, int zoom, Color c,
			int pixelLength) {
		logger.info("Inside method pixelLength {} , drawBoundary {} ",pixelLength ,boundary);
		try {
			Graphics2D graphics = organizeGraphics(image, c, BOUNDARY_THICKNESS);
			double latOrigin = latLonBounds[ReportConstants.INDEX_ZER0];
			double lonOrigin = latLonBounds[ReportConstants.INDEX_ONE];
			double latExtent = latLonBounds[ReportConstants.INDEX_TWO] - latLonBounds[ReportConstants.INDEX_ZER0];
			double lonExtent = latLonBounds[ReportConstants.INDEX_THREE] - latLonBounds[ReportConstants.INDEX_ONE];
			boolean flag = true;
			Path2D path = new Path2D.Double();
			for (List<Double> latlon : boundary) {
				double ly1 = (pixelLength * (latlon.get(ReportConstants.INDEX_ONE) - latOrigin)) / latExtent;
				double lx1 = (pixelLength * (latlon.get(ReportConstants.INDEX_ZER0) - lonOrigin)) / lonExtent;
				int ly = (int) ((int) pixelLength - ly1);
				int lx = (int) lx1 - 1;
				if (flag) {
					path.moveTo(lx, ly);
					flag = false;
				}
				path.lineTo(lx, ly);
			}
			graphics.draw(path);
		} catch (Exception e) {
			logger.error("Exception inside method drawBoundary {} ",e.getMessage());
		}
	}

	public void renderSiteLocation(BufferedImage image, int x, int y, Color color, int size,int innerCircle, int outerCircle) {
		try {
			Graphics2D graphics = organizeGraphics(image, color, 2.0f);
			y = y - size / 2;
			x = x - size / 2;
			graphics.setColor(color);
			graphics.drawOval(x + size / 2 - outerCircle / 2, y + size / 2 - outerCircle / 2, outerCircle, outerCircle);
		} catch (Exception e) {
		}
	}
}
