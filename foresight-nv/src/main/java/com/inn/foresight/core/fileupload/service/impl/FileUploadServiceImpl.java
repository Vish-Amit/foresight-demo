package com.inn.foresight.core.fileupload.service.impl;

import java.util.concurrent.ConcurrentHashMap;
import com.inn.product.um.module.utils.wrapper.ModuleDataWrapper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.http.HttpRequest;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.fileupload.dao.IFileUploadDao;
import com.inn.foresight.core.fileupload.model.FileUpload;
import com.inn.foresight.core.fileupload.model.FileUpload.Status;
import com.inn.foresight.core.fileupload.service.IFileUploadService;
import com.inn.foresight.core.fileupload.utils.FileUploadConstant;
import com.inn.foresight.core.fileupload.utils.FileUploadUtils;
import com.inn.foresight.core.fileupload.utils.wrapper.FileUploadWrapper;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.product.security.authentication.GenerateReportURL;
import com.inn.product.security.spring.userdetails.CustomerInfo;
import com.inn.product.um.module.dao.ModuleDao;
import com.inn.product.um.module.model.Module;
import com.inn.product.um.permission.dao.PermissionDao;
import com.inn.product.um.permission.model.Permission;
import com.inn.product.um.permission.utils.UmPermissionConstants;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;
import com.inn.product.um.user.utils.UmUtils;

@Service("FileUploadServiceImpl")
public class FileUploadServiceImpl  extends AbstractService<Integer, FileUpload> implements IFileUploadService
{

	@Autowired
	IFileUploadDao iFileUploadDao;

	@Autowired
	ModuleDao iModuleDao;

	@Autowired
	PermissionDao permissionDao;
	private Logger logger = LogManager.getLogger(FileUploadServiceImpl.class);

	@Autowired
	public void setDao(IFileUploadDao genericFileUploadDao) {
		super.setDao(genericFileUploadDao);
		this.iFileUploadDao = genericFileUploadDao;
	}
	
	@Autowired
	CustomerInfo customerInfo;

   private List<Integer> getModuleIdList(Set<Permission> permissionSet)
   {
	   logger.info("Inside getModuleIdList");
	   List<Integer> moduleIdList=new ArrayList<>();
	   for(Permission permission :permissionSet)
	   {
		   if(permission.getModule()!=null) {
			   
			   moduleIdList.add(permission.getModule().getModuleid());
		   }
		 
	   }
	   logger.info("moduleIdList : {} ",moduleIdList);
	   return moduleIdList;
   }

   @Override
   public List<FileUploadWrapper> getFileUploadList( String fileSearch, Integer llimit, Integer ulimit) 
   {
	   logger.info("Inside getFileUploadList ");
	   List<FileUploadWrapper> uploadedFileList = new ArrayList<>();
	   try {
		   User user = UserContextServiceImpl.getUserInContext();

		   if (fileSearch != null)
			   iFileUploadDao.enableFileSearchFilter(fileSearch);
		   Set<String> permissionSet = customerInfo.getPermissions();//UmUtils.getPermissionList(user.getActiveRole().getPermission());

		   if (permissionSet.contains(UmPermissionConstants.ROLE_ADMIN_UM_SUPER_ADMIN_ROLE)) 
		   {  
			   logger.info("Going to fetch list for super admin");
			   uploadedFileList=iFileUploadDao.getFileUploadList(llimit, ulimit);
		   }
		   else
		   {
			   logger.info("Going to fetch list for other user");
			   uploadedFileList=iFileUploadDao.getFileUploadListForOtherUser(llimit, ulimit, getModuleIdList(user.getActiveRole().getPermission()));
		   }

		   if(uploadedFileList!=null)
			   logger.info("uploadedFileList size : {} ",uploadedFileList.size());
         
	   }
	   catch(Exception e) {
		   logger.error("Error while getting  file uploaded List : {} ",Utils.getStackTrace(e));
	   }
	   return uploadedFileList;
   }



	@Override
	public Long getFileUploadCount() 
	{
		logger.info("Inside getFileUploadCount ");
		Long count=0L;
		try {
			 User user = UserContextServiceImpl.getUserInContext();

			   Set<String> permissionSet = customerInfo.getPermissions();//UmUtils.getPermissionList(user.getActiveRole().getPermission());

			   if (permissionSet.contains(UmPermissionConstants.ROLE_ADMIN_UM_SUPER_ADMIN_ROLE)) 
			   {  
				   logger.info("Going to fetch count for super admin");
				   count=	iFileUploadDao.getFileUploadCount();
				   logger.info("Total count for uploadedFile : {} ", count);
					
			   }
			   else
			   {
				   logger.info("Going to fetch count for other user");
				   count=	iFileUploadDao.getFileUploadCountForOtherUser( getModuleIdList(user.getActiveRole().getPermission())) ;
				   logger.info("Total count for uploadedFile : {} ", count);
					
			   }
			
			

		} catch (Exception e) {
			logger.error("Error while getting file uploaded count  {} ", Utils.getStackTrace(e));
		}
		return count;
	}

	@Transactional
	@Override
	public  Map<String,String> deletefilebyFileId(List<Integer> fileId) 
	{
		logger.info("Inside deletefilebyFileId ");
		Map<String,String>  message=new HashMap<>();
		try {
			int value=	iFileUploadDao.deletefilebyFileId(fileId);
			if (value > 0)
				message.put(FileUploadConstant.MESSAGE, FileUploadConstant.FILE_DELETED_SUCCESSFULLY);
			else
				message.put(FileUploadConstant.MESSAGE, FileUploadConstant.FILE_DELETION_FAILED);

		} catch (Exception e) {
			logger.error("Error while deleting file by id  {} ", Utils.getStackTrace(e));
		}
		return message;
	}




	private   List<FileItem> extractFileItemsFromHttpRequest(HttpServletRequest request)
			throws FileUploadException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		return fileUpload.parseRequest(request);
	}

	private  String getFilePath(Module module) { 
		String modulepath=	module.getName().replace(' ', '_')+ForesightConstants.FORWARD_SLASH; 
		if(module.getModule()!=null && module.getModule().getName()!=null && !module.getModule().getName().isEmpty())
		{ 
			modulepath=module.getModule().getName().replace(' ', '_')+ForesightConstants.FORWARD_SLASH+modulepath; 
		} 
		return ConfigUtils.getString("FILE_UPLOAD_PATH")+modulepath+DateUtil.parseDateToString(FileUploadConstant.DDMMYYYY, new Date())+"/"; 
	}


	@Transactional
	public List<String> uploadFile(HttpServletRequest request)
	{
		logger.info("Inside uploadFile");
		List<String> message=new ArrayList<>();
		try {
			logger.info("request.getParameter(FileUploadConstant.MODULEID) : {}",request.getParameter(FileUploadConstant.MODULEID));
			Integer moduleId = Integer.parseInt(request.getParameter(FileUploadConstant.MODULEID));
			logger.info("uploadFile :  moduleId={}",moduleId);
			List<FileItem> fileItemList = null;
			fileItemList = extractFileItemsFromHttpRequest(request);
			logger.info("moduleId : {} fileItemlist:{}  ",moduleId,fileItemList);
			Module module=iModuleDao.findByPk(moduleId);

			for (FileItem f:fileItemList) 
			{

				logger.info("name :{} ",f.getName());
				logger.info("size :{} ",f.getSize());

				InputStream inputStream=  f.getInputStream();

				String []fileNameAndType= f.getName().split(FileUploadConstant.DOT_SEPARATOR);

				FileUpload fileUpload=new FileUpload();

				fileUpload.setDeleted(false);
				fileUpload.setName(fileNameAndType[0]+DateUtil.parseDateToString(FileUploadConstant.DATE_FORMAT, new Date()));
				fileUpload.setSize(f.getSize());
				fileUpload.setStatus(Status.UN_PROCESSED);
				fileUpload.setType(fileNameAndType[1]);
				fileUpload.setModule(module);
				fileUpload.setUploadedBy(UserContextServiceImpl.getUserInContext());
				fileUpload.setUploadedOn(new Date());
				fileUpload.setPath(getFilePath(module));
				try {  
					ContentBody 	content = new InputStreamBody(inputStream, "file");		
					MultipartEntity reqEntity = new MultipartEntity();				
					reqEntity.addPart("file", content);	


					String attachmentUrI = ConfigUtils.getString(ConfigEnum.ATTACHMENT_SERVICE_BASE_URL)+ConfigUtils.getString(ConfigEnum.ATTACHMENT_SERVICE_SAVE_ATTACHMENT_URL)+getFilePath(module)+fileUpload.getName()+FileUploadConstant.DOT+fileUpload.getType();
						
					logger.info("attachmentUrI : {} ",attachmentUrI);
					HttpRequest http = new HttpPostRequest(attachmentUrI, reqEntity);				
					http.setConnectionTimeout(Duration.seconds(120));
					String jsonResponse=http.getString();
					logger.info("Response while Uploading attachment:{}",jsonResponse);

					if(jsonResponse.toLowerCase().indexOf("success")>=0)
					{
						FileUpload fileuploadNew = iFileUploadDao.create(fileUpload);	
						message.add( fileNameAndType[0]+FileUploadConstant.UPLOADED_SUCCESSFULLY);
						message.add("ID_"+fileuploadNew.getId());
					}
					else 
					{
						message.add(FileUploadConstant.FAIL_TO_UPLOAD+fileUpload.getName());
					}

				}
				catch (Exception e) 
				{
					logger.error("File upload fail for filename :{} Exception :  {}  ",fileUpload.getName(),Utils.getStackTrace(e));

					message.add(FileUploadConstant.FAIL_TO_UPLOAD+fileUpload.getName());
				}


			}

		}

		catch(Exception e)
		{
			logger.error("Error while uploadFile {} ",Utils.getStackTrace(e));
		}
		return message;	
	}

	@Override
	public String downloadFile(String fileName)  {
		String finalURL=ForesightConstants.BLANK_STRING;
		try {

			String []fileNameAndType= fileName.split(FileUploadConstant.DOT_SEPARATOR);
			logger.info("filename : {} ",fileNameAndType[0]);
			String filePath=	iFileUploadDao.getFilePathByFileName(fileNameAndType[0]);
			logger.info("filePath : {} ",filePath);
			String path=ConfigUtils.getString(ConfigEnum.ATTACHMENT_SERVICE_PATH)+filePath+fileName;

			logger.info("path : {} ",path);
			String generateReportURL = new GenerateReportURL(path).toString();
			finalURL=ConfigUtils.getString(ConfigEnum.APACHE_DOWNLOAD_BASE_URL)+generateReportURL;
			logger.info("final url : {}",finalURL);

		} catch (Exception e) {
			logger.error("Error while downloadfile {} ", Utils.getStackTrace(e));
		}
		return finalURL;
	}

	@Override
	public String getTemplateFilePath(String modulePath,String subModulePath)
	{
		
	  String filePath=ForesightConstants.BLANK_STRING; 
	  try {
		filePath= new GenerateReportURL(ForesightConstants.FORWARD_SLASH+FileUploadConstant.ATTACHMENT+ForesightConstants.FORWARD_SLASH+FileUploadConstant.FILE_UPLOAD+ForesightConstants.FORWARD_SLASH+FileUploadUtils.getFilePath(modulePath)+ForesightConstants.FORWARD_SLASH+FileUploadUtils.getFilePath(subModulePath)+ForesightConstants.FORWARD_SLASH+FileUploadConstant.REPORT_TEMPLATE+ForesightConstants.FORWARD_SLASH+FileUploadUtils.getFilePath(subModulePath)+"_SampleTemplate"+ForesightConstants.XLSX_EXTENSION).toUriString();
	} catch (Exception e) {
		logger.error("NoSuchAlgorithmException {}",e.getStackTrace());
	}	  
	  logger.info("filePath : {}",filePath);
	  return filePath;
	  
	}

	@Override
	public String downloadFileByPath(String path) {
		String url =ForesightConstants.BLANK_STRING;
		try {
			path=ConfigUtils.getString(ConfigEnum.ATTACHMENT_SERVICE_PATH)+path;
			url= ConfigUtils.getString(ConfigEnum.APACHE_DOWNLOAD_BASE_URL)+new GenerateReportURL(path).toString();
		} catch (Exception e) {
			logger.error("Error while downloadFileByPath, err : {} ", Utils.getStackTrace(e));
		}
		return url;
	}
	
	@Override
	public String getAbsoluteFilePath(String path) {
		try {
			return ConfigUtils.getString(ConfigEnum.APACHE_DOWNLOAD_BASE_URL)+new GenerateReportURL(path).toString();
		} catch (Exception e) {
			logger.error("Error while getAbsoluteFilePath, err : {} ", Utils.getStackTrace(e));
		}
		return ForesightConstants.BLANK_STRING;
	}
	
	@Override
	public byte[] downloadFromHDFS(String filePath) {
		logger.info("Inside downloadFromHDFS in FileUploadServiceImpl");
		String attachmentServiceUrl = ConfigUtils.getString(ConfigEnum.ATTACHMENT_SERVICE_BASE_URL)
				+ FileUploadUtils.ATTACHMENT_SERVICE_DOWNLOAD_HDFS_URL + filePath;

		attachmentServiceUrl = attachmentServiceUrl.replace(Symbol.SPACE_STRING, ForesightConstants.URL_SPACE_REPLACER);
		logger.info("Attachment Service URL {}", attachmentServiceUrl);
		byte[] byteArray = null;
		try {
			byteArray = new HttpGetRequest(attachmentServiceUrl).getByteArray();
		} catch (HttpException ex) {
			logger.error("Getting Exception while getting data from HDFS due to {}", ex);
		}
		return byteArray;
	}
	
	
	private ModuleDataWrapper convertModuleDataWrapperForFileUpload(Module m)
	{
		ModuleDataWrapper md = new ModuleDataWrapper();
		md.setModuleId(m.getModuleid());
		md.setName(m.getName());
		md.setIconName(m.getIconName());
		md.setDescription(m.getDescription());
		md.setDisplayName(m.getDisplayname());
		if (m.getModule() != null) {
			md.setParentId(m.getModule().getModuleid());
		}
		return md;
	}
	
	public <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	
	@Override
	public List<ModuleDataWrapper>getFileUPloadModule()
	{  
		
		List<ModuleDataWrapper> moduleDataWrapperList = new ArrayList<ModuleDataWrapper>();
		
		List<Module> modules = iModuleDao.findModuleByModuleName(FileUploadConstant.FILE_UPLOAD_MODULE);
		Module m = modules.get(0);
		if(m!=null)
		{
			Integer id = m.getModuleid();
			try {
				
				
					Set<String> setList = customerInfo.getPermissions();
					if((setList==null) ||(setList!=null && !setList.isEmpty()) )
					{
						logger.info("permission list is null or empty");
					}
					
					
						List<Module> moduleList = permissionDao.getDistinctModuleListByPermission(setList);
						
					for(Module module :moduleList)
					{
						if(module.getModule()!=null)
						{
							Module parentmodule = module.getModule();
							if(parentmodule.getModule()!=null)
							{
								if(parentmodule.getModuleid().equals(id))
								{
									moduleDataWrapperList.add(convertModuleDataWrapperForFileUpload(module));
								}	
								else if(parentmodule.getModule().getModuleid().equals(id))
								{
									moduleDataWrapperList.add(convertModuleDataWrapperForFileUpload(parentmodule));
								}
							}
							
							moduleDataWrapperList = moduleDataWrapperList.stream().filter(distinctByKey(moduleDataWrapper -> moduleDataWrapper.getModuleId()))
									.collect(Collectors.toList());
						}
					}
					
			}catch (Exception e) {
				logger.error("Error in getting  module data : {}", Utils.getStackTrace(e));
			}
		}	
		return moduleDataWrapperList;
	}
}
