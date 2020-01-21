package com.inn.foresight.core.mail.utils.wrapper;

public class EmailAttachment {
	
	private String attachment;
	
	private String filePath;
	
	private String fileName;
	
	private Integer emailId;

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getEmailId() {
		return emailId;
	}

	public void setEmailId(Integer emailId) {
		this.emailId = emailId;
	}

	@Override
	public String toString() {
		return "EmailAttachment [attachment=" + attachment + ", filePath=" + filePath + ", fileName="
				+ fileName + ", emailId=" + emailId + "]";
	}
	
	
}
