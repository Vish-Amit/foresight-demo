package com.inn.foresight.module.nv.workorder.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class WOFileDetail.
 *
 * @author innoeye date - 30-Dec-2017 12:46:42 PM
 */
@NamedQueries({
		@NamedQuery(name = "findWORecipeMapping", query = "select new com.inn.foresight.module.nv.workorder.model.WOFileDetail(w.id,w.woRecipeMapping.id) from WOFileDetail w where (w.isDeleted='false' OR  w.isDeleted=null) and w.filePath = :filePath"),
		@NamedQuery(name = "findAllUnprocessFile", query = "select w from WOFileDetail w where w.isProcessed='false' and w.woRecipeMapping.genericWorkorder.status='COMPLETED' ORDER BY w.filetype DESC, w.fileName"),
		@NamedQuery(name = "findFileByRecipeMappingId", query = "select w from WOFileDetail w where w.woRecipeMapping.id=(:woRecipeMappingId) and (w.isDeleted='false' OR  w.isDeleted=null)"),
		@NamedQuery(name = "getFileDetailByIds", query = "select w from WOFileDetail w where w.id in (:idList) and (w.isDeleted='false' OR  w.isDeleted=null)"),
		@NamedQuery(name = "findFileByRecipeMappingIdAndFilePart", query = "select w from WOFileDetail w where w.woRecipeMapping.id=(:woRecipeMappingId) and w.fileName like CONCAT('%', :fileNamePart, '%')"),
		@NamedQuery(name = "findFileByRecipeMappingIdList", query = "select w from WOFileDetail w where w.woRecipeMapping.id in (:woRecipeMappingId)"),
		@NamedQuery(name = "findAllUnprocessWorkOrder", query = "select w.woRecipeMapping.genericWorkorder.id from WOFileDetail w where w.isProcessed='false' and (w.isDeleted='false' OR  w.isDeleted=null) and w.woRecipeMapping.genericWorkorder.status='COMPLETED' ORDER BY w.filetype DESC, w.fileName"),
		
		@NamedQuery(name = "findFileByWorkOrderId", query = "select w from WOFileDetail w where (w.isDeleted='false' OR  w.isDeleted=null) and w.woRecipeMapping.genericWorkorder.id=(:workorderId)"),
		@NamedQuery(name = "findProcessedFileByWorkOrderId", query = "select w from WOFileDetail w where (w.isDeleted='false' OR  w.isDeleted=null) and w.woRecipeMapping.genericWorkorder.id=(:workorderId) and w.isProcessed=true"),
		
		@NamedQuery(name = "findAllUnprocessFileByWoId", query = "select w from WOFileDetail w where (w.isDeleted='false' OR  w.isDeleted=null) and w.isProcessed='false' and  w.woRecipeMapping.genericWorkorder.status='COMPLETED'  and  w.woRecipeMapping.genericWorkorder.id=(:workorderId)"),
		@NamedQuery(name = "findAllUnprocessFileByRecipeId", query = "select w from WOFileDetail w where (w.isDeleted='false' OR  w.isDeleted=null) and w.isProcessed='false' and  w.woRecipeMapping.status='COMPLETED'  and  w.woRecipeMapping.genericWorkorder.id=(:workrorderId) and  w.woRecipeMapping.id =(:recipeId)"),
		
		@NamedQuery(name = "findFileByWorkOrderIdList", query = "select w from WOFileDetail w where  (w.isDeleted='false' OR  w.isDeleted=null)  and  w.woRecipeMapping.genericWorkorder.id in (:workrorderIdList)"),
		@NamedQuery(name = "findFileDetailByWORecipeMappingAndFileName", query = "select w from WOFileDetail w where w.woRecipeMapping.id=:recipeMappingId and w.fileName=:fileName"),
		@NamedQuery(name = "findFileListByWorkorderId", query = "select w from WOFileDetail w where w.woRecipeMapping.genericWorkorder.id=(:workrorderId)"),
		@NamedQuery(name = "findFileByWorkOrderIdAndRecipeCategory", query = "select w from WOFileDetail w where (w.isDeleted='false' OR  w.isDeleted=null) and w.woRecipeMapping.genericWorkorder.id=(:workrorderId) and w.woRecipeMapping.recipe.category IN (:category)"),
		@NamedQuery(name = "getProccesedLogFileMappingList", query = "select w.woRecipeMapping.id from WOFileDetail w where  w.woRecipeMapping.genericWorkorder.id=:woId and w.isProcessed=true AND  w.processedLogFilePath is not null"),
		@NamedQuery(name = "findFileDetailByWORecipeMappingId", query = "select w from WOFileDetail w where w.woRecipeMapping.id=:recipeMappingId"),
		@NamedQuery(name = "findCompletedFileByRecipeMappingId", query = "select w from WOFileDetail w where w.woRecipeMapping.id=(:recipeId) and (w.isDeleted='false' OR  w.isDeleted=null) and w.woRecipeMapping.genericWorkorder.status='COMPLETED'"),
		@NamedQuery(name = "findProcessedFilesByWoId", query = "select new com.inn.foresight.module.nv.workorder.wrapper.WOFileDetailWrapper(w.id,w.woRecipeMapping.id) from WOFileDetail w where  w.woRecipeMapping.genericWorkorder.id=:woId and w.isProcessed=true AND  w.processedLogFilePath is not null"),
		@NamedQuery(name = "findFileDetailByRecipeMappingId", query = "select w from WOFileDetail w where w.woRecipeMapping.id=(:recipeId) and (w.isDeleted='false' OR  w.isDeleted=null)"),

		})


@Entity
@Table(name = "WOFileDetail")
@XmlRootElement(name = "WOFileDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class WOFileDetail implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wofiledetailid_pk")
	private Integer id;

	/** The wo recipe mapping. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "worecipemappingid_fk", nullable = true)
	private WORecipeMapping woRecipeMapping;

	/** The file name. */
	@Column(name = "filename")
	private String fileName;

	/** The file path. */
	@Column(name = "filepath")
	private String filePath;

	/** The is processed. */
	@Column(name = "processed")
	private Boolean isProcessed;

	/** The is deleted. */
	@Column(name = "deleted")
	private Boolean isDeleted;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The wo recipe mapping id. */
	@Transient
	private Integer woRecipeMappingId;

	/** File Type. */
	@Column(name = "filetype")
	private String filetype;

	/** Processed Log File Path. */
	@Column(name = "processedlogfilepath")
	private String processedLogFilePath;
	
	
	

	@Column(name = "filesize")
	private Long fileSize;

	/**
	 * Instantiates a new WO file detail.
	 */
	public WOFileDetail() {
		super();
	}
	
	

	/**
	 * Instantiates a new WO file detail.
	 *
	 * @param id                the id
	 * @param woRecipeMappingId the wo recipe mapping id
	 */
	public WOFileDetail(Integer id, Integer woRecipeMappingId) {
		this.id = id;
		this.woRecipeMappingId = woRecipeMappingId;
	}

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
	 * Gets the wo recipe mapping.
	 *
	 * @return the wo recipe mapping
	 */
	public WORecipeMapping getWoRecipeMapping() {
		return woRecipeMapping;
	}

	/**
	 * Sets the wo recipe mapping.
	 *
	 * @param woRecipeMapping the new wo recipe mapping
	 */
	public void setWoRecipeMapping(WORecipeMapping woRecipeMapping) {
		this.woRecipeMapping = woRecipeMapping;
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
	 * Gets the checks if is processed.
	 *
	 * @return the checks if is processed
	 */
	public Boolean getIsProcessed() {
		return isProcessed;
	}

	/**
	 * Sets the checks if is processed.
	 *
	 * @param isProcessed the new checks if is processed
	 */
	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creation time
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime the new creation time
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the modification time.
	 *
	 * @return the modification time
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * Sets the modification time.
	 *
	 * @param modificationTime the new modification time
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * Gets the wo recipe mapping id.
	 *
	 * @return the wo recipe mapping id
	 */
	public Integer getWoRecipeMappingId() {
		return woRecipeMappingId;
	}

	/**
	 * Sets the wo recipe mapping id.
	 *
	 * @param woRecipeMappingId the new wo recipe mapping id
	 */
	public void setWoRecipeMappingId(Integer woRecipeMappingId) {
		this.woRecipeMappingId = woRecipeMappingId;
	}

	/**
	 * Get File Type.
	 * 
	 * @return file type
	 */
	public String getFiletype() {
		return filetype;
	}

	/**
	 * Set file type name.
	 * 
	 * @param filetype
	 */
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	/**
	 * Gets the checks if is deleted.
	 *
	 * @return the checks if is deleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * Sets the checks if is deleted.
	 *
	 * @param isDeleted the new checks if is deleted
	 */

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * Gets HDFS file path for processed log file.
	 *
	 * @return HDFS file path for processed log file.
	 */
	public String getProcessedLogFilePath() {
		return processedLogFilePath;
	}

	/**
	 * Sets HDFS file path for processed log file.
	 *
	 * @param HDFS file path for processed log file.
	 */
	public void setProcessedLogFilePath(String processedLogFilePath) {
		this.processedLogFilePath = processedLogFilePath;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "WOFileDetail{" + "id=" + id + ", woRecipeMapping=" + woRecipeMapping + ", fileName='" + fileName + '\''
				+ ", filePath='" + filePath + '\'' + ", isProcessed=" + isProcessed + ", isDeleted=" + isDeleted
				+ ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + ", woRecipeMappingId="
				+ woRecipeMappingId + ", filetype='" + filetype + '\'' + ", processedLogFilePath='"
				+ processedLogFilePath + '\'' + ", fileSize=" + fileSize + '}';
	}

}
