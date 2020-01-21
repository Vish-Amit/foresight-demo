package com.inn.foresight.core.fileupload.utils.wrapper;

import java.io.Serializable;
import java.util.Date;

import com.inn.foresight.core.fileupload.model.FileUpload.Status;
import com.inn.core.generic.wrapper.JpaWrapper;
@JpaWrapper
public class FileUploadWrapper implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String fileName;
	private	String moduleName;
	private	String userName;
	private Date uploadedOn;
	private String fileType;
	private Long fileSize;
	private Status fileStatus;
	
	
	
	
	
	public FileUploadWrapper(Integer id, String fileName, String moduleName, String userName, Date uploadedOn,
			String fileType, Long fileSize,Status fileStatus) {
		this.id = id;
		this.fileName = fileName;
		this.moduleName = moduleName;
		this.userName = userName;
		this.uploadedOn = uploadedOn;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.fileStatus=fileStatus;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getUploadedOn() {
		return uploadedOn;
	}
	public void setUploadedOn(Date uploadedOn) {
		this.uploadedOn = uploadedOn;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public Status getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(Status fileStatus) {
		this.fileStatus = fileStatus;
	}
	
	
	
	

}
