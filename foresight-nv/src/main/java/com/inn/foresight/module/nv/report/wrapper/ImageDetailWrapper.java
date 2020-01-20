package com.inn.foresight.module.nv.report.wrapper;

import java.io.InputStream;

public class ImageDetailWrapper {
	
	private String imageCategory;
	private String imageName;
	private Long captureTime;
	private InputStream img;
	private String imgPath;
	
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getImageCategory() {
		return imageCategory;
	}
	public void setImageCategory(String imageCategory) {
		this.imageCategory = imageCategory;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public Long getCaptureTime() {
		return captureTime;
	}
	public void setCaptureTime(Long captureTime) {
		this.captureTime = captureTime;
	}
	public InputStream getImg() {
		return img;
	}
	public void setImg(InputStream img) {
		this.img = img;
	}
	@Override
	public String toString() {
		return "ImageDetailWrapper [imageCategory=" + imageCategory + ", imageName=" + imageName + ", captureTime="
				+ captureTime + ", img=" + img + ", imgPath=" + imgPath + "]";
	}

		
}
