package com.inn.foresight.core.mail.utils.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;
@RestWrapper
public class Email {
		
	private String to;
		
	private String cc;
		
	private String bcc;
		
	private String subject;
		
	private String message;
		
	private List<EmailAttachment> emailAttachments;
	
	private String remark;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<EmailAttachment> getEmailAttachments() {
		return emailAttachments;
	}

	public void setEmailAttachments(List<EmailAttachment> emailAttachments) {
		this.emailAttachments = emailAttachments;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	@Override
	public String toString() {
		return "Email [to=" + to + ", cc=" + cc + ", bcc=" + bcc + ", subject=" + subject + ", message=" + message
				+ ", emailAttachments=" + emailAttachments + ", remark=" + remark + "]";
	}
	
	
}
