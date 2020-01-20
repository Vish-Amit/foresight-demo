package com.inn.foresight.module.nv.workorder.wrapper;

public class FileResponseWrapper {

	private String fileName;
	private String status;

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "FileResponseWrapper [fileName=" + fileName + ", status=" + status + "]";
	}

}
