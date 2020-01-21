package com.inn.foresight.core.fileupload.rest.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.fileupload.model.FileUpload;
import com.inn.foresight.core.fileupload.service.IFileUploadService;
import com.inn.foresight.core.fileupload.utils.FileUploadConstant;
import com.inn.foresight.core.fileupload.utils.wrapper.FileUploadWrapper;
import com.inn.foresight.core.generic.utils.ForesightConstants;

@Path("/FileUpload")
@Produces("application/json")
@Consumes("application/json")
public class FileUploadRestImpl extends AbstractCXFRestService<Integer, FileUpload> {




	/** The logger. */
	private Logger logger = LogManager.getLogger(FileUploadRestImpl.class);


	/** The context. */
	@Context
	private SearchContext context;

	@Autowired
	private IFileUploadService iFileUploadService;


	public FileUploadRestImpl() {
		super(FileUpload.class);
	}

	@Override
	public List<FileUpload> search(FileUpload entity) {
		return new ArrayList<>();
	}

	@Override
	public FileUpload findById(@NotNull Integer primaryKey) {
		return null;
	}

	@Override
	public List<FileUpload> findAll() {
		return new ArrayList<>();
	}

	@Override
	public FileUpload create(@Valid FileUpload anEntity) {
		return null;
	}

	@Override
	public FileUpload update(@Valid FileUpload anEntity) {
		return null;
	}

	@Override
	public boolean remove(@Valid FileUpload anEntity) {
		return false;
	}

	@Override
	public void removeById(@NotNull Integer primaryKey) {
      // Empty Block
	}

	@Override
	public IGenericService<Integer, FileUpload> getService() {
		return null;
	}

	@Override
	public SearchContext getSearchContext() {
		return null;
	}

	@GET
	@Path("getFileUploadList")
	public List<FileUploadWrapper> getFileUploadList(@QueryParam("fileSearch") String fileSearch, @QueryParam("llimit") Integer llimit,
			@QueryParam("ulimit") Integer ulimit) {
		logger.info("Inside getFileUploadList");
		return iFileUploadService.getFileUploadList(fileSearch, llimit, ulimit);
	}


	@GET
	@Path("getFileUploadCount")
	public Long getFileUploadCount() {
		logger.info("Inside getFileUploadCount");
		return iFileUploadService.getFileUploadCount();
	}


	@POST
	@Path("deletefilebyFileId")
	public Map<String,String> deletefilebyFileId(List<Integer> fileId) {
		logger.info("Inside deletefilebyFileId");
		return iFileUploadService.deletefilebyFileId(fileId);
	}

	
	
	@POST
	@Path("uploadFile")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@Context HttpServletRequest request) {
		logger.info("Inside uploadFile");
		return Response	.ok(iFileUploadService.uploadFile(request)).build();
	}
	
	@GET
	@Path("downloadFile")
	@Produces("application/json")
	public Map<String, String> downloadFile(@QueryParam("fileName") String fileName) {
		logger.info("Going to download file :{}", fileName);
		Map<String, String> result = new HashMap<>();
	
			if(fileName!=null){
				result.put(FileUploadConstant.ATTACHMENT_DOWNLOAD_URL, iFileUploadService.downloadFile(fileName));
				return result;
			} else {
				throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
			}
		
	}

	@GET
	@Path("getTemplateFilePath")
	public Map<String, String> getTemplateFilePath(@QueryParam("modulePath") String modulePath,@QueryParam("subModulePath") String subModulePath)
	{
		logger.info("modulePath {} subModulePath {} ",modulePath,subModulePath);
		Map<String, String> result = new HashMap<>();
		result.put(FileUploadConstant.ATTACHMENT_DOWNLOAD_URL,iFileUploadService.getTemplateFilePath(modulePath, subModulePath));
		return result;
	}	
	
}
