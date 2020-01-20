package com.inn.foresight.module.nv.pushnotification.notificationfile.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.pushnotification.constants.PushNotificationConstants;
import com.inn.product.um.user.model.User;

/** The Class PushNotification. */

	@NamedQuery(name = PushNotificationConstants.GET_ALL_NOTIFICATION_FILE_LIST , query ="select n from NotificationFile n where n.isDeleted=false  order by n.creationTime desc")
    @NamedQuery(name=PushNotificationConstants.GET_FEEDBACK_NOTIFICATION_FILE_COUNT, query="select count (*) from NotificationFile where isDeleted=false")


@Entity
@XmlRootElement(name = "NotificationFile")
@Table(name = "NotificationFile")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NotificationFile {
	/** The id. */
	@Id
	@GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY) 
	@Column(name = "notificationfileid_pk")
	private Integer id;
	
	
	@Column(name = "filename")
	private String fileName;
	
	@Column(name="filepath")
	private String filePath;
	
	@Column(name="creationtime")
	private Date creationTime;
	
	@Column(name="modificationtime")
	private Date modificationTime;
	
    @Column(name="deleted")
	boolean isDeleted;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk", nullable = true)
	private User createdBy;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the modificationTime
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * @param modificationTime the modificationTime to set
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * @return the isDeleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the createdBy
	 */
	public User getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

}
