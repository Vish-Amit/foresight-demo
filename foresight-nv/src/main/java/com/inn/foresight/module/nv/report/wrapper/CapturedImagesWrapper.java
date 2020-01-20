package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class CapturedImagesWrapper {

	private List<ImageDetailWrapper> listOfImages;

	public CapturedImagesWrapper(List<ImageDetailWrapper> listOfImages) {
		this.listOfImages = listOfImages;
	}

	public List<ImageDetailWrapper> getListOfImages() {
		return listOfImages;
	}

	public void setListOfImages(List<ImageDetailWrapper> listOfImages) {
		this.listOfImages = listOfImages;
	}

	@Override
	public String toString() {
		return "CapturedImagesWrapper [listOfImages=" + listOfImages + "]";
	}

	
}
