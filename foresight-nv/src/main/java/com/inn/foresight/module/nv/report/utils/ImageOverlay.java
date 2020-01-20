package com.inn.foresight.module.nv.report.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ImageOverlay {
	
	private ImageOverlay() {
		super();
	}

	/** The logger. */
	private  static Logger logger = LogManager.getLogger(ImageOverlay.class);

	/**
	 * Method to overlay Images.
	 *
	 * @param bgImage --> The background Image
	 * @param fgImage --> The foreground Image
	 * @return --> overlayed image (fgImage over bgImage)
	 */
	public static BufferedImage overlayImages(BufferedImage bgImage, BufferedImage fgImage) {
		if (fgImage.getHeight() > bgImage.getHeight() || fgImage.getWidth() > bgImage.getWidth()) {
			JOptionPane.showMessageDialog(null, "Foreground Image Is Bigger In One or Both Dimensions"
					+ "nCannot proceed with overlay." + "nn Please use smaller Image for foreground");
			return null;
		}
		BufferedImage overlay = new BufferedImage(bgImage.getWidth(), bgImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		float alpha = 0.5f;
		int compositeRule = AlphaComposite.SRC_OVER;
		AlphaComposite ac;
		Graphics2D g = overlay.createGraphics();
		ac = AlphaComposite.getInstance(compositeRule, alpha);
		g.drawImage(bgImage, 0, 0, null);
		g.setComposite(ac);
		g.drawImage(fgImage, 0,0, null);
		g.dispose(); 
		return overlay;
	}

	/**
	 * This method reads an image from the file
	 * @param fileLocation -- > eg. "C:/testImage.jpg"
	 * @return BufferedImage of the file read
	 */
	public static BufferedImage readImage(String fileLocation) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(fileLocation));
		} catch (IOException e) {
			logger.error("Exception occured during readImage {} ",e.getMessage());
		}
		return img;
	}

	/**
	 * This method writes a buffered image to a file
	 * @param img -- > BufferedImage
	 * @param fileLocation --> e.g. "C:/testImage.jpg"
	 * @param extension --> e.g. "jpg","gif","png"
	 */
	public static void writeImage(BufferedImage img, String fileLocation,
			String extension) {
		try {
			BufferedImage bi = img;
			File outputfile = new File(fileLocation);
			ImageIO.write(bi, extension, outputfile);
		} catch (IOException e) {
			logger.error("Exception occured during writeImage {} ",e.getMessage());
		}
	}

}
