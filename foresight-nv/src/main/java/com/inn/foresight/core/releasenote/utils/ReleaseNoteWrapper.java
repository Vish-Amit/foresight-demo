package com.inn.foresight.core.releasenote.utils;

import java.util.Date;

import com.inn.core.generic.wrapper.JpaWrapper;
@JpaWrapper
public class ReleaseNoteWrapper {

    private Integer releaseId;
    private String name;
    private String downloadPath;
    private String comment;
    private String version;
    private Date releaseDate;
    private Date modifiedTime;
    private Date createdTime;
    private Boolean deleted;
    private Integer createdBy;
    private Integer modifiedBy;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public ReleaseNoteWrapper(Integer releaseId, String name, String downloadPath, String version, Date releaseDate, Integer createdBy, Integer modifiedBy, String comment) {
        this.releaseId = releaseId;
        this.name = name;
        this.downloadPath = downloadPath;
        this.version = version;
        this.releaseDate = releaseDate;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.comment = comment;
    }

    public Integer getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(Integer releaseId) {
        this.releaseId = releaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public String toString() {
        return "ReleaseNoteWrapper{" +
                "releaseId=" + releaseId +
                ", name='" + name + '\'' +
                ", downloadPath='" + downloadPath + '\'' +
                ", comment='" + comment + '\'' +
                ", version='" + version + '\'' +
                ", releaseDate=" + releaseDate +
                ", modifiedTime=" + modifiedTime +
                ", createdTime=" + createdTime +
                ", deleted=" + deleted +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                '}';
    }
}
