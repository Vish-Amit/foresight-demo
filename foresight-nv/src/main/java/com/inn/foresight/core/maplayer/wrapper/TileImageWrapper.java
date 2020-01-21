package com.inn.foresight.core.maplayer.wrapper;

import java.awt.image.BufferedImage;

public class TileImageWrapper {

	int[] tileId;
	BufferedImage image;
	int[] childGridId;
	int[] parentTileId;
	String childZooom;


	public int[] getChildGridId() {
		return childGridId;
	}

	public void setChildGridId(int[] childGridId) {
		this.childGridId = childGridId;
	}

	public int[] getParentTileId() {
		return parentTileId;
	}

	public void setParentTileId(int[] parentTileId) {
		this.parentTileId = parentTileId;
	}

	public String getChildZooom() {
		return childZooom;
	}

	public void setChildZooom(String childZooom) {
		this.childZooom = childZooom;
	}



	public int[] getTileId() {
		return tileId;
	}

	public void setTileId(int[] tileId) {
		this.tileId = tileId;
	}

	/**
	 * @author ist
	 *
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @author ist
	 *
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
