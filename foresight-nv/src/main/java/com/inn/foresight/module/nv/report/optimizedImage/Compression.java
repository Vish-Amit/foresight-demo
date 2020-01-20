package com.inn.foresight.module.nv.report.optimizedImage;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;


public class Compression {
	
	private Compression() {
	    throw new IllegalStateException("Compression Utility class");
	  }

	public static BufferedImage getOptimizedImage(BufferedImage image, int pixelLength) {
		BufferedImage img = new BufferedImage(pixelLength, pixelLength, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.drawImage(image, 0, 0, pixelLength, pixelLength, null);
		organizeGraphics(g);
		return img;
	}

	private static void organizeGraphics(Graphics2D g) {
		g.dispose();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

}
