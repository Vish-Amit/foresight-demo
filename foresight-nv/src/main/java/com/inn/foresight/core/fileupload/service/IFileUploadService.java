package com.inn.foresight.core.fileupload.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.fileupload.model.FileUpload;
import com.inn.foresight.core.fileupload.utils.wrapper.FileUploadWrapper;
import com.inn.product.um.module.utils.wrapper.ModuleDataWrapper;

public interface IFileUploadService extends IGenericService<Integer, FileUpload>
{

	Long getFileUploadCount();

	List<FileUploadWrapper> getFileUploadList(String fileSearch, Integer llimit, Integer ulimit);

	Map<String, String> deletefilebyFileId(List<Integer> fileId );

	List<String>  uploadFile(HttpServletRequest request);

	String downloadFile(String fileName) ;

	String getTemplateFilePath(String modulePath, String subModulePat);

	String downloadFileByPath(String path);
	
	byte[] downloadFromHDFS(String filePath);

	String getAbsoluteFilePath(String path);

	List<ModuleDataWrapper> getFileUPloadModule();
	
}
