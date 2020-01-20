package com.inn.foresight.module.nv.report.optimizedImage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xerces.impl.io.MalformedByteSequenceException;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.tiles.Tile;
import com.inn.commons.maps.tiles.TileUtils;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.utils.UrlSigner;

public class GoogleMaps implements Cloneable {
	private static final Logger logger = LogManager.getLogger(GoogleMaps.class);

	private String googleMapKey = ConfigUtils.getString(ConfigUtil.GOOGLE_MAP_KEY);
	private String googleMapClientId = ConfigUtils.getString(ReportConstants.GOOGLE_MAP_CLIENT_ID);
	private String googleMapPrivateKey= ConfigUtils.getString(ReportConstants.GOOGLE_MAP_PRIVATE_KEY);

	public static final String MAP_SATELLITE = ReportConstants.MAP_SATELLITE;
	public static final String MAP_ROAD = ReportConstants.MAP_ROAD;

	public static final int IMAGESIZE = ReportConstants.THREE_ZERO_FOUR;

	public static final String MAP_TERRAIN = "terrain";
	public static final String MAP_HYBRID = "hybrid";

	int[] googleTileMin;
	int[] googleTileMax;
	double[] bounds;
	int zoom;
	private GoogleMapCache cache;
	int pixelLength;
	private String type;
	private BufferedImage img;
	
	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public int[] getGoogleTileMin() {
		return googleTileMin;
	}

	public void setGoogleTileMin(int[] googleTileMin) {
		this.googleTileMin = googleTileMin;
	}

	public int[] getGoogleTileMax() {
		return googleTileMax;
	}

	public void setGoogleTileMax(int[] googleTileMax) {
		this.googleTileMax = googleTileMax;
	}

	public double[] getBounds() {
		return bounds;
	}

	public void setBounds(double[] bounds) {
		this.bounds = bounds;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public int getPixelLength() {
		return pixelLength;
	}

	public void setPixelLength(int pixelLength) {
		this.pixelLength = pixelLength;
	}

	public GoogleMaps() {
	}

    GoogleMaps(Tile googleTileMin, Tile googleTileMax, int zoom, String mapType) {
        this.googleTileMax = new int[] {googleTileMax.getTx(),googleTileMax.getTy()};
        this.googleTileMin = new int[] {googleTileMin.getTx(),googleTileMin.getTy()};
        type = mapType;
        this.zoom = zoom;
        cache = new GoogleMapCache(zoom);
        Corner cornerForMinMaxTile = TileUtils.getCornerForMinMaxTile(googleTileMin, googleTileMax, zoom);
        bounds = new double[] { cornerForMinMaxTile.getMinLatitude(), cornerForMinMaxTile.getMinLongitude(),
                cornerForMinMaxTile.getMaxLatitude(), cornerForMinMaxTile.getMaxLongitude() };
        pixelLength = getPixelLength(googleTileMin, googleTileMax);
    }
    /** --. */

	private int getPixelLength(Tile googleTileMin2, Tile googleTileMax2) {
		return ((googleTileMax2.getTx() - googleTileMin2.getTx()) + 1) * ReportConstants.TWO_FIVE_SIX;
	}

	public BufferedImage getMapImage() throws IOException {
		for (int tx = googleTileMin[0]; tx <= googleTileMax[0]; tx++) {
			for (int ty = googleTileMin[1]; ty <= googleTileMax[1]; ty++) {
				cache.getImage(tx, ty, type);
			}
		}
		int rows = ReportUtil.getImageRows(googleTileMin, googleTileMax);

		img = cache.getMergedImage(rows, rows, googleTileMin, googleTileMax);
		return img;
	}

	public GoogleMaps getClone() {
		GoogleMaps newGMobj = new GoogleMaps();
		newGMobj.bounds = getBounds();
		newGMobj.pixelLength = getPixelLength();
		newGMobj.googleTileMax = getGoogleTileMax();
		newGMobj.googleTileMin = getGoogleTileMin();
		newGMobj.zoom = getZoom();
		BufferedImage image = new BufferedImage(this.img.getHeight(), this.img.getWidth(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.drawImage(this.img, 0, 0, null);
		newGMobj.img = image;

		return newGMobj;
	}

	public void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		try (InputStream is = url.openStream()) {

			byte[] b = new byte[ReportConstants.TWO_ZERO_FOUR_EIGHT];
			int length;
			try (OutputStream os = new FileOutputStream(destinationFile)) {
				while ((length = is.read(b)) != -1) {
					os.write(b, 0, length);
				}
			}
		} catch (Exception e) {
			logger.error("error in getting image from google api" + Utils.getStackTrace(e));
		}
	}

	public BufferedImage getBufferredImageFromURL(String imageUrl) throws MalformedByteSequenceException,IOException {
		//logger.error("Inside method getBufferredImageFromURL; URL: {} ", imageUrl);
		BufferedImage image = new BufferedImage(IMAGESIZE, IMAGESIZE, BufferedImage.TYPE_INT_ARGB);
		try {
			image = ReportUtil.sendUnSecureHttpsPostRequest(imageUrl);
//			counter++;
//			if(counter%ReportConstants.INDEX_HUNDRED==ReportConstants.INDEX_ZER0){
//				logger.error("No of hits count is {} , url {} ",counter,imageUrl);
//			}
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}
		return image;
	}

	public BufferedImage getGoogleMapImageForTile(int tx, int ty, int tz, String imageType) throws MalformedByteSequenceException, IOException {
		return getBufferredImageFromURL(getURLForTile(tx, ty, tz, imageType));
	}

	private String getURLForTile(int tx, int ty, int tz, String imageType) {
		String imageUrl = ReportConstants.BLANK_STRING;
		try {
			LatLng centroid = new Tile(tx, ty, tz).getCentroid();
			Boolean usePrivateKey = ConfigUtils.getBoolean(ReportConstants.USE_MAP_PRIVATE_KEY);
			if(usePrivateKey == null || usePrivateKey) {
				imageUrl = ReportConstants.GOOGLE_MAP_API_URL + centroid.getLatitude() + ReportConstants.COMMA + centroid.getLongitude() + ReportConstants.AMPERSAND
						+ ReportConstants.ZOOM + ForesightConstants.EQUALS + tz + ReportConstants.AMPERSAND
						+ ReportConstants.SIZE
						+ ForesightConstants.EQUALS + IMAGESIZE + "x" + IMAGESIZE + ReportConstants.AMPERSAND
						+ ReportConstants.MAPTYPE.toLowerCase() + ForesightConstants.EQUALS + imageType
						+ googleMapClientId;
				return UrlSigner.getfinalUrlForStaticMap(imageUrl, googleMapPrivateKey);
			} else {
				imageUrl = ReportConstants.GOOGLE_MAP_API_URL + centroid.getLatitude() + ReportConstants.COMMA + centroid.getLongitude() + ReportConstants.AMPERSAND
						+ ReportConstants.ZOOM + ForesightConstants.EQUALS + tz + ReportConstants.AMPERSAND
						+ ReportConstants.SIZE
						+ ForesightConstants.EQUALS + IMAGESIZE + "x" + IMAGESIZE + ReportConstants.AMPERSAND
						+ ReportConstants.MAPTYPE.toLowerCase() + ForesightConstants.EQUALS + imageType + ReportConstants.AMPERSAND
						+ ReportConstants.API_KEY + ForesightConstants.EQUALS + googleMapKey;
				return UrlSigner.getfinalUrlForStaticMap(imageUrl, googleMapPrivateKey);
			}
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}
		return imageUrl;
	}
}
