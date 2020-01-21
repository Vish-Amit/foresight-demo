package com.inn.foresight.core.fileupload.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.fileupload.model.FileUpload;
import com.inn.foresight.core.fileupload.utils.wrapper.FileUploadWrapper;

public interface IFileUploadDao extends  IGenericDao<Integer, FileUpload>{

	List<FileUploadWrapper> getFileUploadList(Integer llimit, Integer ulimit);

	void enableFileSearchFilter(String fileSearch);

	Long getFileUploadCount();

	int deletefilebyFileId(List<Integer>  fileId);

	String getFilePathByFileName(String fileName);

	List<FileUploadWrapper> getFileUploadListForOtherUser(Integer llimit, Integer ulimit, List<Integer> moduleId);

	Long getFileUploadCountForOtherUser(List<Integer> moduleId);

}
