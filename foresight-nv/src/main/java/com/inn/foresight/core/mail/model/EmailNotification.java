package com.inn.foresight.core.mail.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.core.mail.utils.NotificationStatus;

/**
 * The Class EmailNotification.
 */

@NamedQuery(name = "getQueueDataToSchedule", query = "from EmailNotification s WHERE s.status=:status AND s.type = :type")
@NamedQuery(name = "getQueueDataToMail", query = "from EmailNotification s WHERE s.status=:status")
@NamedQuery(name = "updateStatusById", query = "update EmailNotification s set s.status=(:status) where s.id in (:ids)")
@NamedQuery(name = "deleteNotificationsBeforeDaysByStatus", query = "delete from EmailNotification s where s.status=(:status) and trunc(s.creationtime)<(:date)")
@NamedQuery(name = "findByNotificationId", query = "from EmailNotification s WHERE s.id=:id")

@XmlRootElement(name = "EmailNotification")
@Entity
@Table(name = "EmailNotification")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class EmailNotification implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4935559156644442396L;

	/** The notification id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "emailnotificationid_pk")
	private Long id;

	@Basic
	private String type;

	@Column(name = "message", length = 1000)
	private String message;

	@Column(name = "sendTo", length = 1500)
	private String sendTo;

	@Basic
	private String subject;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private NotificationStatus status;

	@Basic
	private String cc;

	@Basic
	private String bcc;

	@Basic
	private String attachmentPath;

	@Basic
	private String downloadFileName;

	@Basic
	private String inlineImagePath;

	@Basic
	private String inlineImageId;

	@Basic
	private String fromEmail;

	@Basic
	private Integer retryCount;

	@Basic
	private String errorDetail;

	@Column(name = "creationtime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationtime;

	@Column(name = "modificationtime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationtime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public NotificationStatus getStatus() {
		return status;
	}

	public void setStatus(NotificationStatus status) {
		this.status = status;
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

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public String getInlineImagePath() {
		return inlineImagePath;
	}

	public void setInlineImagePath(String inlineImagePath) {
		this.inlineImagePath = inlineImagePath;
	}

	public String getInlineImageId() {
		return inlineImageId;
	}

	public void setInlineImageId(String inlineImageId) {
		this.inlineImageId = inlineImageId;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}

	public Date getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(Date creationtime) {
		this.creationtime = creationtime;
	}

	public Date getModificationtime() {
		return modificationtime;
	}

	public void setModificationtime(Date modificationtime) {
		this.modificationtime = modificationtime;
	}

	@Override
	public String toString() {
		return "EmailNotification [id=" + id + ", type=" + type + ", message=" + message + ", sendTo=" + sendTo
				+ ", subject=" + subject + ", status=" + status + ", cc=" + cc + ", bcc=" + bcc + ", attachmentPath="
				+ attachmentPath + ", downloadFileName=" + downloadFileName + ", inlineImagePath=" + inlineImagePath
				+ ", inlineImageId=" + inlineImageId + ", fromEmail=" + fromEmail + ", retryCount=" + retryCount
				+ ", errorDetail=" + errorDetail + ", creationtime=" + creationtime + ", modificationtime="
				+ modificationtime + "]";
	}

}
