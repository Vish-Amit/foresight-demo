package com.inn.foresight.module.nv.layer3.wrapper;

import java.util.Arrays;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class Layer3ReportWrapper {

	String fileName;
	byte[] file;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "Layer3ReportWrapper [fileName=" + fileName + ", file=" + Arrays.toString(file) + "]";
	}

}
