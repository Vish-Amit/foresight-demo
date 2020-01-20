package com.inn.foresight.module.nv.report.optimizedImage;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;

public class GoogleMapCache {
	private final HashMap<String, BufferedImage> imageMap;
	private int cacheSize = ReportConstants.THOUSNAND;
	private int tilePixelCount = ReportConstants.TWO_FIVE_SIX;
	private static final Logger logger = LogManager.getLogger(GoogleMapCache.class);
	private GoogleMaps googlemaps;
	int zoom;

	public GoogleMapCache(int zoom) {
		imageMap = new HashMap<>(cacheSize);
		googlemaps = new GoogleMaps();
		this.zoom = zoom;
	}

	public BufferedImage getImage(int tx, int ty, String mapType) throws IOException{
		BufferedImage bufferedImage = new BufferedImage(GoogleMaps.IMAGESIZE, GoogleMaps.IMAGESIZE, BufferedImage.TYPE_INT_ARGB);
		try {
			String key = tx + ForesightConstants.UNDERSCORE + ty;
			/*bufferedImage = imageMap.get(key);
			if (bufferedImage == null) {

				bufferedImage = googlemaps.getGoogleMapImageForTile(tx, ty, zoom, mapType);
				imageMap.put(key, bufferedImage);
			}
			return bufferedImage;*/
			return imageMap.computeIfAbsent(key, k -> getBufferedImage(tx, ty, zoom, mapType));
		} 
		catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}
		return bufferedImage;
	}

	private BufferedImage getBufferedImage(int tx, int ty, int zoom, String mapType) {
		try {
			return googlemaps.getGoogleMapImageForTile(tx, ty, zoom, mapType);
		} catch (Exception e) {
			logger.info("Unable to get BifferedImage for tx {} ,ty {} ,zoomLevel {} ",tx,ty,zoom);
		}
		return null;
	}

	public BufferedImage getMergedImage(int rows, int cols, int[] googleTile_min, int[] googleTile_max) {
		BufferedImage result = new BufferedImage(tilePixelCount, tilePixelCount, BufferedImage.TYPE_INT_ARGB);
		try {
			result = new BufferedImage(rows * tilePixelCount, cols * tilePixelCount, BufferedImage.TYPE_INT_ARGB);
			Graphics g = result.getGraphics();

			int x = -24;
			int y = -24;

			for (int tx = googleTile_min[0]; tx <= googleTile_max[0]; tx++) {
				for (int ty = googleTile_min[1]; ty <= googleTile_max[1]; ty++) {
					BufferedImage bi;
					try {
						bi = imageMap.get(tx + "_" + ty);
						g.drawImage(bi, x, y, null);
					} catch (Exception e) {
						logger.error("error in getting image for " + tx + "_" + ty + "  " + Utils.getStackTrace(e));
					}
					y += tilePixelCount;
				}
				x += tilePixelCount;
				y = -24;
			}
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}
		return result;
	}
}
