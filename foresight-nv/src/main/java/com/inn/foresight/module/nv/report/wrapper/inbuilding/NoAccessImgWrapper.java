package com.inn.foresight.module.nv.report.wrapper.inbuilding;

import java.util.List;

public class NoAccessImgWrapper {
	private List<NoAccessWrapper> imageList;

	public List<NoAccessWrapper> getImageList() {
		return imageList;
	}



	public void setImageList(List<NoAccessWrapper> imageList) {
		this.imageList = imageList;
	}



	@Override
	public String toString() {
		return "NoAccessImgWrapper [noAccessImgList=" + imageList + "]";
	}

}
