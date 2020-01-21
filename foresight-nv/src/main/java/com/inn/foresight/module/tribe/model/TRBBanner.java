package com.inn.foresight.module.tribe.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;

/**
 * The Class TRBBanner.
 */
@NamedQueries({
		@NamedQuery(name = "findByBannerTitle", query = "select br from TRBBanner br" + " where br.title=:title"),
		@NamedQuery(name = "getExistedBannerIds", query = "select br.id from TRBBanner br") })

@Entity
@Table(name = "Banner")
@XmlRootElement(name = "TRBBanner")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class TRBBanner implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6937090470084466531L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	/*@GeneratedValue(generator = "TRBBanner_gen", strategy = javax.persistence.GenerationType.SEQUENCE)
	  @SequenceGenerator(sequenceName = "TRBBanner_seq", name = "TRBBanner_gen", allocationSize = 1)*/
	@Column(name = "bannerid_pk")
	private Integer id;

	/** The image name. */
	@Basic
	@Column(name = "imagename")
	private String imageName;

	/** The image path. */
	@Basic
	@Column(name = "imagepath")
	private String imagePath;

	/** The file name. */
	@Basic
	@Column(name = "filename")
	private String fileName;

	/** The file path. */
	@Basic
	@Column(name = "filepath")
	private String filePath;

	/** The description. */
	@Basic
	@Column(name = "description", length = 400)
	private String description;

	/** The title. */
	@Basic
	@Column(name = "title")
	private String title;

	/** The url. */
	@Basic
	@Column(name = "url")
	private String url;

	/** The views count. */
	@Basic
	@Column(name = "viewscount")
	private Long viewsCount = 0L;

	/** The status. */
	@Basic
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private BannerStatus status;

	/**
	 * The Enum BannerStatus.
	 */
	public enum BannerStatus {
		
		/** The active. */
		ACTIVE, 
		 /** The inactive. */
		 INACTIVE, 
		 /** The draft. */
		 DRAFT
	}

	/** The banner type. */
	@Basic
	@Column(name = "type")
	private String bannerType;
	/*	@Basic
		@Column(name = "type")
		@Enumerated(EnumType.STRING)
		private BannerType bannerType;*/

	/**
	 * The Enum BannerType.
	 */
	/*	public enum BannerType {
			FILE, 
	 URL
	}*/

	/** The creator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk")
	private User creator;

	/** The last modifier. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastModifierid_fk")
	private User lastModifier;

	/** The modified time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modifiedTime;

	/** The created time. */
	@Basic
	@Column(name = "creationtime")
	private Date createdTime;
	
	@Basic
	@Column(name="configuration")
	@Lob
	private String configuration;
	
	@Basic
	@Column(name="bannercontent")
	@Lob
	private String bannerContent;
	
	@Basic
	@Column(name = "starttime")
	private Date startTime;
	
	@Basic
	@Column(name = "endtime")
	private Date endTime;
	
	@Basic
	@Column(name = "enable")
	private Boolean enable=false;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the image name.
	 *
	 * @return the image name
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * Sets the image name.
	 *
	 * @param imageName the new image name
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * Gets the image path.
	 *
	 * @return the image path
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Sets the image path.
	 *
	 * @param imagePath the new image path
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName the new file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Sets the file path.
	 *
	 * @param filePath the new file path
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public BannerStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(BannerStatus status) {
		this.status = status;
	}

	/**
	 * Gets the creator.
	 *
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * Gets the last modifier.
	 *
	 * @return the last modifier
	 */
	public User getLastModifier() {
		return lastModifier;
	}

	/**
	 * Sets the last modifier.
	 *
	 * @param lastModifier the new last modifier
	 */
	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}

	/**
	 * Gets the modified time.
	 *
	 * @return the modified time
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * Sets the modified time.
	 *
	 * @param modifiedTime the new modified time
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	/**
	 * Gets the created time.
	 *
	 * @return the created time
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * Sets the created time.
	 *
	 * @param createdTime the new created time
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * Gets the views count.
	 *
	 * @return the views count
	 */
	public Long getViewsCount() {
		return viewsCount;
	}

	/**
	 * Sets the views count.
	 *
	 * @param viewsCount the new views count
	 */
	public void setViewsCount(Long viewsCount) {
		this.viewsCount = viewsCount;
	}

	/**
	 * Gets the banner type.
	 *
	 * @return the banner type
	 */
	public String getBannerType() {
		return bannerType;
	}

	/**
	 * Sets the banner type.
	 *
	 * @param bannerType the new banner type
	 */
	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "TRBBanner [id=" + id + ", imageName=" + imageName + ", imagePath=" + imagePath + ", fileName="
				+ fileName + ", filePath=" + filePath + ", description=" + description + ", title=" + title + ", url="
				+ url + ", viewsCount=" + viewsCount + ", status=" + status + ", creator=" + creator + ", lastModifier="
				+ lastModifier + ", modifiedTime=" + modifiedTime + ", createdTime=" + createdTime + "]";
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	public boolean equals(Object obj) {
		boolean returnValue = false;
		if (obj instanceof TRBBanner) {
			TRBBanner trbBanner = (TRBBanner) obj;
			EqualsBuilder equalsBuilder = new EqualsBuilder();
			equalsBuilder.appendSuper(super.equals(obj));
			equalsBuilder.append(id, trbBanner.getId());
			equalsBuilder.append(imageName, trbBanner.getImageName());
			equalsBuilder.append(imagePath, trbBanner.getImagePath());
			equalsBuilder.append(fileName, trbBanner.getFileName());
			equalsBuilder.append(filePath, trbBanner.getFilePath());
			equalsBuilder.append(description, trbBanner.getDescription());
			equalsBuilder.append(title, trbBanner.getTitle());
			equalsBuilder.append(url, trbBanner.getUrl());
			equalsBuilder.append(viewsCount, trbBanner.getViewsCount());
			equalsBuilder.append(status, trbBanner.getStatus());
			equalsBuilder.append(bannerType, trbBanner.getBannerType());
			equalsBuilder.append(creator, trbBanner.getCreator());
			equalsBuilder.append(lastModifier, trbBanner.getLastModifier());
			equalsBuilder.append(modifiedTime, trbBanner.getModifiedTime());
			equalsBuilder.append(createdTime, trbBanner.getCreatedTime());
			returnValue = equalsBuilder.isEquals();
		}
		return returnValue;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17, 37);
		hashCodeBuilder.append(id);
		hashCodeBuilder.append(imageName);
		hashCodeBuilder.append(imagePath);
		hashCodeBuilder.append(fileName);
		hashCodeBuilder.append(filePath);
		hashCodeBuilder.append(description);
		hashCodeBuilder.append(title);
		hashCodeBuilder.append(url);
		hashCodeBuilder.append(viewsCount);
		hashCodeBuilder.append(status);
		hashCodeBuilder.append(bannerType);
		hashCodeBuilder.append(creator);
		hashCodeBuilder.append(lastModifier);
		hashCodeBuilder.append(modifiedTime);
		hashCodeBuilder.append(createdTime);
		return hashCodeBuilder.toHashCode();
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public String getBannerContent() {
		return bannerContent;
	}

	public void setBannerContent(String bannerContent) {
		this.bannerContent = bannerContent;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	
}
