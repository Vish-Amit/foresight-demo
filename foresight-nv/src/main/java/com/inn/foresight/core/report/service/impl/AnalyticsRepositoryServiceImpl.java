
package com.inn.foresight.core.report.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.http.HttpRequest;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IJobHistoryDao;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.dao.IReportTemplateDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.RepositoryType;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.core.report.model.ReportTemplate;
import com.inn.foresight.core.report.service.IAnalyticsRepositoryService;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

@Service("AnalyticsRepositoryServiceImpl")
@Transactional
public class AnalyticsRepositoryServiceImpl extends AbstractService<Integer, AnalyticsRepository> implements IAnalyticsRepositoryService {

	private Logger logger = LogManager
			.getLogger(AnalyticsRepositoryServiceImpl.class);

	@Autowired
	private IAnalyticsRepositoryDao dao;
	@Autowired
	private IReportTemplateDao reportTemplateDao;
	
	@Autowired
	private IJobHistoryDao iJobHistoryDao;
	
	public static final String SOURCE_FILE_FORMAT = "source_file_format";
	public static final String SOURCE_FILE_NAME = "source_file_name";
	public static final String UPLOADING_FILE_ERROR_MSG = "Error while uploading file";
	public static final String UPLOADING_FILE_ERROR_JSON =
		      "{\"errorMsg\": \"Error while uploading file\"}";
	public static final String CSV_FORMAT="csv";
	public static final String XLSX_FORMAT="xlsx";
	public static final String DISK_PATH="diskpath";
	public static final String FILE_EXTENSION = ".";
	public static final String UPLOADING_FILE_SUCCESS_JSON =
		      "{\"successMsg\": \"File uploaded successfully\"}";

	
	public AnalyticsRepositoryServiceImpl() {
		super();
	}

	@Autowired
	public AnalyticsRepositoryServiceImpl(IAnalyticsRepositoryDao dao) {
		super();
		super.setDao(dao);
		this.dao = dao;
	}
	
	@Override
	public List<AnalyticsRepository> search(AnalyticsRepository entity) {
		logger.info("Finding  ReportInstance list by entity :{} ", entity);
		return super.search(entity);
	}


	
	
	 @Override
	    public AnalyticsRepository createCustomReportRepository( Integer templateId,AnalyticsRepository repository){
	    	try {
	    		logger.info("going to create repo for report template id: {}",templateId);
				ReportTemplate template = reportTemplateDao.findByPk(templateId);
				if(repository != null) {
					if(repository.getGeographyConfig()!=null) {
						repository.setGeographyConfig(repository.getGeographyConfig().replace("'", "\""));
					}
					if(repository.getReportConfig()!=null) {
						repository.setReportConfig(repository.getReportConfig().replace("'", "\""));
					}
					repository.setReportTemplate(template);
		    		User user = UserContextServiceImpl.getUserInContext();
					repository.setCreatedTime(new Date());
					repository.setModifiedTime(new Date());
					repository.setTemplateId(templateId);
					repository.setRepositoryType(RepositoryType.CUSTOM_REPORT);
					if(repository.getGeneratedReportPath()!=null) {
						String isFileExist= isFileExistInHDFS(repository.getGeneratedReportPath());
						logger.info("createCustomReportRepository: filePath={} , isFileExist={}",repository.getGeneratedReportPath(),isFileExist);
						if(isFileExist!=null && isFileExist.equalsIgnoreCase(ForesightConstants.TRUE_LOWERCASE)) {
							repository.setFilepath(repository.getGeneratedReportPath());
							repository.setProgress(progress.Generated);
							repository.setDownloadFileName(repository.getName());
						}else {
							repository.setProgress(progress.Failed);
						}						
					}else if(repository.getCachedReportTime()!=null) {
						//compare configuration json with previous entries
					}else {
					repository.setProgress(progress.In_Progress);
					}
					repository.setQueueStatus(false);
					repository.setCreator(user);
					repository.setLastModifier(user);
					repository.setScheduled(false);
					repository.setDeleted(false);
					//setCreateFilePathForRepoCreator(repository,template);
					
				    return dao.create(repository);
				}
			} catch (Exception e) {
				logger.error("Exception in createCustomReportRepository, err = {}",ExceptionUtils.getStackTrace(e));
			}
	    	throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
	    }
	 
	 @Override
		public String hdfsFileUpload(HttpServletRequest request, User user) {

			if (request.getParameter(SOURCE_FILE_FORMAT) != null) {
				try {
					return uploadFile(request,user);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}

		public String uploadFile(HttpServletRequest request, User user) throws JSONException {
			String diskPath = ConfigUtils.getString(ConfigEnum.FILE_UPLOAD_TO_HDFS_FILE_BASE_PATH);

			logger.info("Inside uploadFile method ==> The content inside HttpServletRequest request is : {}", request);

			try {
				String sourceFileFormat = request.getParameter(SOURCE_FILE_FORMAT);
				String sourceFileName = request.getParameter(SOURCE_FILE_NAME);

				List<FileItem> files = getListOfFileItems(request);
				
				
				String relativePath=diskPath+user.getUserid()+ForesightConstants.FORWARD_SLASH
						+System.currentTimeMillis()+ForesightConstants.FORWARD_SLASH;
				
				
				

				boolean isuploaded = isAllFilesUploaded(relativePath, files, user);

				if (isuploaded) {
					sourceFileFormat = getSrcFormatForCSV(sourceFileFormat, files);
					relativePath = relativePath+sourceFileName + sourceFileFormat;
					return "{\"result\":\"" + relativePath + "\"}";
				}
			}

			catch (FileUploadException e) {
				logger.error("found error in upload file method : {}", ExceptionUtils.getStackTrace(e));
			}
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}

		private List<FileItem> getListOfFileItems(HttpServletRequest request) throws FileUploadException {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			return fileUpload.parseRequest(request);
		}

		private boolean isAllFilesUploaded(String diskPath, List<FileItem> files,User user) {
			logger.info("Inside isAllFilesUploaded method");
			boolean isuploaded = false;
			for (FileItem file : files) {
				String fileDiskPath = diskPath+file.getName();
				logger.info("diskPath {}", fileDiskPath);
				logger.info("name :{} , size :{}", file.getName(), file.getSize());
				try (InputStream inputStream = file.getInputStream()) {
					String jsonResponse = null;
					jsonResponse = saveFilesOnHDFS(fileDiskPath, inputStream);
					logger.info("Response while Uploading attachment:{}", jsonResponse);
					if (jsonResponse.toLowerCase().contains("success")) {
						logger.info("file successfully saved on disk on diskpath {}", fileDiskPath);
						isuploaded = true;
					} else {
						logger.info("file upload failed");
						return false;
					}
				} catch (IOException | HttpException e) {
					logger.error("Exeption inside uploadFileConverterFile by microservice due to {}",
							ExceptionUtils.getStackTrace(e));
					return false;
				}
			}
			return isuploaded;
		}
		
		private String getSrcFormatForCSV(String sourceFormat, List<FileItem> files) {
			if (XLSX_FORMAT.equalsIgnoreCase(sourceFormat)) {
				sourceFormat = StringUtils.substringAfterLast(files.get(0).getName(), FILE_EXTENSION);
			}
			return sourceFormat;
		}
		
		private String saveFilesOnHDFS(String diskPath, InputStream inputStream) throws HttpException {
			ContentBody content = new InputStreamBody(inputStream, ForesightConstants.FILE);
			MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
			reqEntity.addPart(ForesightConstants.FILE, content);
			HttpEntity build = reqEntity.build();
			String attachmentUrI = ConfigUtils.getString(ConfigEnum.ATTACHMENT_SERVICE_BASE_URL)
					+ ConfigUtils.getString(ConfigEnum.ATTACHMENT_SERVICE_SAVE_ATTACHMENT_URL) + diskPath + "&isHDFS="
					+ "true";
			logger.info("attachmentUrI : {} ", attachmentUrI);
			HttpRequest http = new HttpPostRequest(attachmentUrI, build);
			return http.getString();
		}
		 
	 public void setCreateFilePathForRepoCreator(AnalyticsRepository repository,ReportTemplate template) {
	    	try {
	    		if (template.getReportType()!=null) {
				Map<String,String> reportType = new HashMap<>();
				reportType=new Gson().fromJson(template.getReportType(), new TypeToken<Map<String,String>>() {
					}.getType());
				
				if(reportType.get("value").equalsIgnoreCase(ForesightConstants.DOWNLOAD_POLYGON)){
					String filePath=ConfigUtils.getString(ConfigEnum.KML_REPORT_HDFS_PATH.getValue());
					
					Map<String,Map<String,String>> geographyList = new HashMap<>();
					geographyList=new Gson().fromJson(repository.getGeographyConfig(), new TypeToken<Map<String,Map<String,String>>>() {
						}.getType());
					
					Map<String,String> reportList = new HashMap<>();
					reportList=new Gson().fromJson(repository.getReportConfig(), new TypeToken<Map<String,String>>() {
						}.getType());
					
					filePath = filePathForKMLRepo(filePath, geographyList, reportList);
					logger.info("file path  for download polygon is {}",filePath);
					repository.setFilepath(filePath);
					repository.setStorageType(ForesightConstants.HDFS_REPO_STORAGE_TYPE);
					repository.setProgress(progress.Generated);
				}
	    	}
				
			} catch (Exception e) {
				logger.error("Exception in setCreateFilePathForRepoCreator, err = {}",ExceptionUtils.getStackTrace(e));
			}
	    }

	private String filePathForKMLRepo(String filePath, Map<String, Map<String, String>> geographyList,Map<String, String> reportList) {
		
		try {
			String weekNum=Utils.getWeekNumberUsingYear(reportList.get(ForesightConstants.START_DATE));
			Date dateInrange=new Date(Long.parseLong(reportList.get(ForesightConstants.START_DATE)));
			logger.info("weekNum @method  {}  dateInrange {}",weekNum,dateInrange);
			
			String date=iJobHistoryDao.getvalueByNameAndWeekNo(ForesightConstants.KML_JOB_HISTORY_KEY+reportList.get(ForesightConstants.BAND),weekNum);
			Date dateFormate=Utils.parseStringToDate(date,ForesightConstants.DATE_DDMMYYYY);
			
			if(date!=null && !date.isEmpty()){
				filePath=filePath+Utils.parseDateToStringFormat(dateFormate,ForesightConstants.DATE_FORMAT_dd_MM_yyyy)+ForesightConstants.FORWARD_SLASH;
			}
			
			if(reportList.get(ForesightConstants.BAND)!=null && !reportList.get(ForesightConstants.BAND).isEmpty()){
				filePath=filePath+reportList.get(ForesightConstants.BAND);
				if(!reportList.get(ForesightConstants.BAND).equalsIgnoreCase(ForesightConstants.COM)){
					filePath=filePath+ForesightConstants.MEGA_HZ;
				}
			}
			if(dateInrange!=null){
				filePath=filePath+ForesightConstants.UNDERSCORE+Utils.getWeekRange(dateInrange);
			}
			
			if(geographyList.get(ForesightConstants.LEVEL_2)!=null && !geographyList.get(ForesightConstants.LEVEL_2).isEmpty()){
				filePath=filePath+ForesightConstants.UNDERSCORE+geographyList.get(ForesightConstants.LEVEL_2).get("name").replace(ForesightConstants.SPACE, ForesightConstants.UNDERSCORE).toUpperCase()
						+ForesightConstants.KML_EXTENSION;
			}
			
		} catch (Exception e) {
			logger.error("Exception in FilePathForKMLRepo, err = {}",ExceptionUtils.getStackTrace(e));
		}
		return filePath;
	}
	
	
	private String isFileExistInHDFS(String filePath) throws HttpException {
	//	ContentBody content = new InputStreamBody(inputStream, ForesightConstants.FILE);
		//MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
		//reqEntity.addPart(ForesightConstants.FILE, content);
		//HttpEntity build = reqEntity.build();
	try {
		logger.info("Inside isFileExistInHDFS:  filePath={} ",filePath);
	
		String url = ConfigUtils.getString(ConfigEnum.ATTACHMENT_SERVICE_BASE_URL)
				+ ConfigUtils.getString(ConfigEnum.ATTACHMENT_SERVICE_EXIST_ATTACHMENT_URL) + filePath ;
		url = url.replace(Symbol.SPACE_STRING, ForesightConstants.URL_SPACE_REPLACER);
		
		logger.info("isFileExistInHDFS url: {} ", url);
		HttpRequest http = new HttpGetRequest(url);
		
		return http.getString();
	} catch (Exception e) {
		logger.error("Exception in isFileExistInHDFS: error={} ",e.getMessage());
	}
	return ForesightConstants.FAILURE;
	}



	@Override
	public List<AnalyticsRepository> getReportInfoByName(String FileName) {
		try {
			return dao.getReportInfoByName(FileName);
		} catch (Exception e) {
			logger.error("Error in fetching filename {}", Utils.getStackTrace(e));
			throw new RestException("Error in fetching filename");
		}
	}

}

/*	@Override
public AnalyticsRepository createCustomReportRepository(Integer reportTemplateId,AnalyticsRepository anEntity) throws RestException {
	logger.info("Create AnalyticsRepository by an entity :{} ", anEntity);
	try {
		if(anEntity!=null) {
			if(anEntity.getGeographyConfig()!=null) {
				anEntity.setGeographyConfig(anEntity.getGeographyConfig().replace("'", "\""));
			}
			if(anEntity.getReportConfig()!=null) {
				anEntity.setReportConfig(anEntity.getReportConfig().replace("'", "\""));
				
			}
			anEntity.setCreatedTime(new Date());
			anEntity.setIsDeleted(false);
			User user = UserContextServiceImpl.getUserInContext();
			anEntity.setCreator(user);
			ReportTemplate reportTemplate = reportTemplateDao.findByPk(reportTemplateId);
			logger.info("In createReportInstance reportTemplate findById obj = {} ",reportTemplate);
			anEntity.setReportTemplate(reportTemplate);
			logger.info("final OBJECT to create AnalyticsRepository by an entity :{} ", anEntity);
			return dao.create(anEntity);
		}
	} catch (DaoException e) {
		throw new RestException(e.getMessage());
	}
	return null;
}
*/


//@Override
//public void deleteReportInstanceById(Integer primaryKey) throws RestException {
//	logger.info("Removing report Instance by primaryKey {} ", + primaryKey);
//	try {
//		dao.deleteReportInstanceById(primaryKey);
//	}  catch (DataIntegrityViolationException ex) {
//		logger.error(ex.getMessage());
//		throw new ValidationFailedException(ex);
//	} catch (ConstraintViolationException ex) {
//		logger.error(ex.getMessage());
//		throw new RestException(ex);
//	}
//}
//@Override
//public List<ReportInstance> findAll() throws RestException {
//	try {
//		return dao.findAll();
//	} catch (EmptyResultDataAccessException ex) {
//		logger.error(ex.getMessage());
//		throw new RestException(ex);
//	} catch (NoResultException ex) {
//		logger.error("No Data Found err={} ",ex.getMessage());
//		throw new RestException(ex);
//	} catch (DaoException ex) {
//			logger.error(ex.getMessage());
//			throw new RestException(ex);
//	}
//}
//
//	
//@Override
//public List<ReportInstance> getAllReportInstance() throws RestException {
//	try {
//		logger.info("Going to getAllReportInstance" );
//		return dao.getAllReportInstance();
//	} catch (Exception e) {
//		logger.error("Exception in getAllReportInstance err={} ",Utils.getStackTrace(e));
//		throw new RestException("unable to get All ReportInstance err={} ",e.getMessage());
//	}
//}
//
//@Override
//public List<ReportInstance> getReportInstanceByFilter(String reportName,String reportMeasure,String reportType,Long date,Integer lowerLimit,Integer upperLimit) throws RestException{
//	List<ReportInstance> reportList = new ArrayList<>();
//	String dateStr= null;
//	try {
//		logger.info("going to get report Instance by reportName {} reportMeasure {} reportType {} date {}",reportName, reportMeasure, reportType, date);
//		User user = UserContextServiceImpl.getUserInContext();
//		if(date!=null) {
//			dateStr= DateUtil.parseDateToString(ForesightConstants.DATEMONTHYEAR_SPACE_SEPRATED, new Date(date));
//		}
//		reportList = dao.getReportInstanceByFilter(reportName,reportMeasure,reportType,user.getUserid(),dateStr,lowerLimit,upperLimit); 
//	} catch (RestException e){
//		throw new RestException(e.getMessage());
//	} catch (Exception e){
//		logger.error("Exception in getReportInstanceByFilter :reportName {} reportMeasure {} reportType {} date {}  err:{}",reportName, reportMeasure, reportType, date,e.getMessage());
//	}
//	return reportList;
//}
//
//@Override
//public Long getReportInstanceCountByFilter(String reportName,String reportMeasure,String reportType,Long date) throws RestException{
//	Long count = 0l ;
//	try {
//		String dateStr=null;
//		logger.info("going to get report Instance by reportName {} reportMeasure {} reportType {} date {}",reportName, reportMeasure, reportType, date);
//		User user = UserContextServiceImpl.getUserInContext();
//		if(date!=null) {
//			dateStr= DateUtil.parseDateToString(ForesightConstants.DATEMONTHYEAR_SPACE_SEPRATED, new Date(date));
//		}
//		count = dao.getReportInstanceCountByFilter(reportName,reportMeasure,reportType,user.getUserid(),dateStr);
//	} catch (RestException e){
//		throw new RestException(e.getMessage());
//	} catch (Exception e){
//		logger.error("Exception in getReportInstanceCountByFilter :reportName {} reportMeasure {} reportType {} date {}  err:{}",reportName, reportMeasure, reportType, date,e.getMessage());
//	}
//	return count;
//}
