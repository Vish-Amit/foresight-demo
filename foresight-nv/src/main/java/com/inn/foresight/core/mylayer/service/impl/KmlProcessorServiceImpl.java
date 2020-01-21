package com.inn.foresight.core.mylayer.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.http.HttpRequest;
import com.inn.commons.io.CommandLineUtils;
import com.inn.commons.io.FileUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.mylayer.dao.IKmlProcessorDao;
import com.inn.foresight.core.mylayer.model.KmlProcessor;
import com.inn.foresight.core.mylayer.service.IKmlProcessorService;
import com.inn.foresight.core.mylayer.utils.KmlProcessorWrapper;
import com.inn.foresight.core.mylayer.utils.MyLayerConstants;
import com.inn.product.um.user.dao.UserDao;
import com.inn.product.um.user.model.User;

@Service("KmlProcessorServiceImpl")
public class KmlProcessorServiceImpl extends AbstractService<Integer, KmlProcessor> implements IKmlProcessorService {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(KmlProcessorServiceImpl.class);

	/** The ikmlprocessor dao. */
	@Autowired
	private IKmlProcessorDao iKmlProcessorDao;

	@Autowired
	private UserDao iUserDao;

	private static final String KML_FILE_ATTACHMENT_PATH = "KML_FILE_ATTACHMENT_PATH";

	private static final String KML_FILE_UPLOAD_PATH = "KML_FILE_UPLOAD_PATH";

	private static final String KML_FILE_DROPWIZARD_URL = "KML_FILE_DROPWIZARD_URL";

	private static final String KML_UPLOAD_URL = "KML_UPLOAD_URL";

	private static final String ATTACHMENT_SERVICE_BASE_URL = "ATTACHMENT_SERVICE_BASE_URL";

	private static final String KML_PATH = "kmlPath";

	private static final String GENERATED_KML_NAME = "generatedKmlName";

	private static final String FILE_SIZE = "fileSize";

	private static final String FILE_CONTENT = "fileContent";

	private static final String COMMENT = "comment";

	
	@Override
	@Transactional
	public KmlProcessor uploadFileAtDropwizard(InputStream inputStream,String fileName,String colorCode,Integer userid)
	{
		try
		{
			logger.info("Going to upload KML file {} with colorCode {} and userid {} at repoting server",userid,fileName,colorCode);
			return saveAttachmentToDropwizard(inputStream, fileName, colorCode, userid);
		}catch(RestException restException)
		{
			logger.error("RestException caught while uploading file in dropwizard Message {}",restException.getMessage());
			throw new RestException(restException.getMessage());
		}
	}

	private String getFileName(String fileName) {
		String date = new SimpleDateFormat(InfraConstants.DATE_FORMAT_YYMMDD).format(new Date());
		fileName=fileName.replaceAll(ForesightConstants.SPACE, ForesightConstants.UNDERSCORE);
		String fileNameWoExtension = null;
		if (fileName.contains(ForesightConstants.DOT)) {
			fileNameWoExtension = StringUtils.substringBeforeLast(fileName, ForesightConstants.DOT);
			String fileExtension = StringUtils.substringAfterLast(fileName, ForesightConstants.DOT).toLowerCase();
			fileName = fileNameWoExtension + date + ForesightConstants.DOT + fileExtension;
		} else {
			fileName = fileName + date;
		}
		return fileName;
	}
	private String sendPOSTRequest(String attachmentUrI,String fileName,MultipartEntity reqEntity) {
		HttpRequest http = null;
		String json=null;
		try {
		attachmentUrI += ConfigUtils.getString(KML_FILE_ATTACHMENT_PATH) + fileName;
		logger.info("attachmentUrI: {}", attachmentUrI);
		http = new HttpPostRequest(attachmentUrI, reqEntity);
		http.setConnectionTimeout(Duration.seconds(120));
		json= http.getString();
		return json;
		}catch(HttpException httpException) {
			logger.error("error in sendPOSTRequest.Exception : {}",Utils.getStackTrace(httpException));
		}
		return json;
	}
	private String getDropwizardURLToGetJson(String fileName,String colorCode, Integer userid) {
		String dropwizardUrl = ConfigUtils.getString(KML_UPLOAD_URL)
				+ ConfigUtils.getString(KML_FILE_DROPWIZARD_URL);
		dropwizardUrl += ForesightConstants.KML_NAME + ForesightConstants.EQUALS + fileName
				+ ForesightConstants.AMPERSAND + ForesightConstants.COLORCODE + ForesightConstants.EQUALS
				+ colorCode + ForesightConstants.AMPERSAND + ForesightConstants.KEY_USER_ID
				+ ForesightConstants.EQUALS + userid;
		return dropwizardUrl;
	}
	@SuppressWarnings("deprecation")
	private KmlProcessor updateKMLProcessor(String attachmentUrI, InputStream inputStream, String fileName,
			String colorCode, Integer userid) {
		ContentBody content = new InputStreamBody(inputStream, ForesightConstants.FILE);
		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart(ForesightConstants.FILE, content);
		KmlProcessor kmlProcessor = new KmlProcessor();
		Long sizeInMB = 10485760L;
		String originalName = StringUtils.substringBeforeLast(fileName, ForesightConstants.DOT);
		fileName=getFileName(fileName);
		Long size = getSizeOfFile(inputStream);
		if (size < sizeInMB) {
			String json = sendPOSTRequest(attachmentUrI, fileName, reqEntity);
			logger.info("json received: {}", json);
			if (json != null && isJSONValid(json)) {
				try {
				JSONObject jsonObject = new JSONObject(json);
				if (jsonObject.has(ForesightConstants.ERRORMSG)) {
					throw new RestException(jsonObject.getString(ForesightConstants.ERRORMSG));
				} else {
					String dropwizardUrl =getDropwizardURLToGetJson(fileName,colorCode,userid);
					logger.info("dropwizard url to get json string: {}", dropwizardUrl);
					String obj = sendHTTPGetRequest(dropwizardUrl);
					if(obj != null) {
					User user = null;
					user = iUserDao.findByPk(userid);
					kmlProcessor.setCreatedTime(new Date());
					kmlProcessor.setModifiedTime(new Date());
					kmlProcessor.setKmlName(originalName);
					kmlProcessor.setGeneratedKmlName(originalName + ForesightConstants.KML_EXTENSION);
					kmlProcessor.setKmlPath(ConfigUtils.getString(KML_FILE_UPLOAD_PATH));
					kmlProcessor.setUserid(user);
					kmlProcessor.setColorCode(colorCode);
					kmlProcessor.setFileSize(size);
					kmlProcessor.setFileContent(obj);
					kmlProcessor = iKmlProcessorDao.create(kmlProcessor);
				}else {
					throw new RestException("Unable To Upload KML File.");
				}
					
				}
			}catch(JSONException jsonException) {
				logger.error("Invalid JSON.Exception : {} " + ExceptionUtils.getStackTrace(jsonException));
			}
			} else {
				throw new RestException("Unable To Upload KML File.");
			}
			return kmlProcessor;
		} else {
			throw new RestException("file size is greater than 10MB.");
		}
	}

	private static Long getSizeOfFile(InputStream inputStream) {
		Long size = 0L;
		try {
			size = (long) (inputStream.available());
			logger.info("size of file: {}", size);
		} catch (IOException ioException) {
			logger.error("unable to get size of file: " + ExceptionUtils.getStackTrace(ioException));
		}
		return size;
	}

	private static String sendHTTPGetRequest(String url) {
		String obj = null;
		try {
			logger.info("http get request is :{}", url);
			if (StringUtils.isNotBlank(url)) {
				HttpRequest http = new HttpGetRequest(url);
				http.setConnectionTimeout(Duration.seconds(120));
				obj = http.getString();
			}
		} catch (Exception e) {
			logger.error("exception while sending HTTP get request :{}", ExceptionUtils.getStackTrace(e));
		}
		return obj;
	}

	private KmlProcessor saveAttachmentToDropwizard(InputStream inputFile, String fileName, String colorCode,
			Integer userid) {
		try {
			logger.info("Going to save attachment in dropwizard.");
			return updateKMLProcessor(
					ConfigUtils.getString(ATTACHMENT_SERVICE_BASE_URL)
							+ ConfigUtils.getString(ConfigEnum.ATTACHMENT_SERVICE_SAVE_ATTACHMENT_URL.getValue()),
					inputFile, fileName, colorCode, userid);
		} catch (RestException restException) {
			logger.error("RestException caught inside saveAttachmentToReporting method Message {}",
					restException.getMessage());
			throw new RestException(restException.getMessage());
		} 
	}

	
	@Override
	public List<KmlProcessorWrapper> getKmlData(Integer userid, Integer upperLimit, Integer lowerLimit) {
		try {
			return iKmlProcessorDao.getKmlData(userid, upperLimit, lowerLimit);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	
	@Override
	@Transactional
	public KmlProcessor updateKMLDetails(Integer userId, KmlProcessor kmlProcessor) {
		logger.info("going to update kml detail for  userid : {}", userId);
		if(kmlProcessor.getId() != null) {
		List<String> existingKMLNameList = iKmlProcessorDao.getAllKMLNames(userId, kmlProcessor.getId());
		logger.info("existing kml list : {}",existingKMLNameList);
		if(!(existingKMLNameList.contains(kmlProcessor.getKmlName()))){
		KmlProcessor kmlProcessorPersisted = iKmlProcessorDao.getKMLById(userId, kmlProcessor.getId());

		if (kmlProcessorPersisted != null) {
			try {
				kmlProcessorPersisted.setModifiedTime(new Date());
				if (kmlProcessor.getKmlName() != null)
					kmlProcessorPersisted.setKmlName(kmlProcessor.getKmlName());
				if (kmlProcessor.getColorCode() != null)
					kmlProcessorPersisted.setColorCode(kmlProcessor.getColorCode());
				if (kmlProcessor.getComment() != null)
					kmlProcessorPersisted.setComment(kmlProcessor.getComment());
				if (kmlProcessor.getProperty() != null)
					kmlProcessorPersisted.setProperty(kmlProcessor.getProperty());
				kmlProcessorPersisted.setModifiedTime(new Date());
				return iKmlProcessorDao.update(kmlProcessorPersisted);
			} catch (Exception e) {
				logger.error("Unable To Update KML Details.Exception : {}",Utils.getStackTrace(e));
			}
		} else {
			throw new RestException("Unable to update KML data");
		}
	}else {
		throw new RestException("KMLName Already Exist.");
	}
	}
		return null;
	}

	
	@Override
	@Transactional
	public boolean deleteKMLDetailsByID(Integer userid, Integer id) {
		try {
			logger.info("Going to delete KML detail for id {} and for userid {}", id, userid);
			return iKmlProcessorDao.deleteKMLDetails(userid, id);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	
	private static boolean isJSONValid(String json) {
		try {
			logger.info("Going to validate JSON received in response");
			ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(json);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public KmlProcessor getKmlDataById(Integer id) {
		try {
			logger.info("Going to get kml data for id {} ", id);
			return iKmlProcessorDao.getKmlDataById(id);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public List<KmlProcessor> getListOfKMLBySearchTerm(Integer userid, Integer lowerLimit, Integer upperLimit,
			String kmlSearch) {
		logger.info("Going to get list of KML for userid {}", userid);
		return iKmlProcessorDao.getListOfKMLBySearchTerm(userid, lowerLimit, upperLimit, kmlSearch);
	}

	@Override
	public Long getCountsOfKML(Integer userid) {
		logger.info("Going to get list of KML for userid {}", userid);
		Long kmlCounts = 0L;
		try {
			kmlCounts = iKmlProcessorDao.getCountsOfKML(userid, null);
				return kmlCounts;
		} catch (Exception exception) {
			logger.error("Error in getting counts of kml Message {}", exception.getMessage());
			throw new RestException("unable to get kml.");
		}
	}

	@Override
	public Long getCountsOfKMLBySearchTerm(Integer userid, String searchTerm) {
		logger.info("Going to get list of KML for userid {} searchTerm {}", userid, searchTerm);
		Long kmlCounts = 0L;
		try {
			kmlCounts = iKmlProcessorDao.getCountsOfKML(userid, searchTerm);
				return kmlCounts;
		} catch (Exception exception) {
			logger.error("Error in getting counts of KML Message {}", exception.getMessage());
			throw new RestException("unable to get KML.");
		}
	}

	@Override
	public String getFileNameForKML(Integer id) {
		logger.info("going to get file name for kml {}", id);
		String fileName = null;
		if (id != null) {
			KmlProcessor kmlProcessor = iKmlProcessorDao.getKmlDataById(id);
			if (kmlProcessor != null) {
				fileName = kmlProcessor.getKmlName();
				logger.info("filename : {}", fileName + ForesightConstants.KML_EXTENSION);
				return fileName + ForesightConstants.KML_EXTENSION;
			} else {
				throw new RestException("Invalid KML");
			}
		} else {
			throw new RestException("Invalid KML ID.");
		}
	}

	@Override
	@Transactional
	public String exportFileForKML(Integer id) {
		logger.info("going to export pins in kml for kmlid {}", id);
		if (id != null) {
			KmlProcessor kmlProcessor = iKmlProcessorDao.getKmlDataById(id);
			if (kmlProcessor != null) {
				String result = generateKMLFile(kmlProcessor);
				if (result != null) {
					return result;
				} else {
					throw new RestException("Something  went wrong");
				}
			} else {
				throw new RestException("Unable to generate KML file");
				
			}
		}
		return null;
	}

	private String generateKMLFile(KmlProcessor kmlProcessor) {
		logger.info("generate kml file for KML: {}", kmlProcessor);
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			TransformerFactory tranFactory = TransformerFactory.newInstance();
			Transformer aTransformer = tranFactory.newTransformer();
			aTransformer.setOutputProperty(OutputKeys.INDENT, InfraConstants.YES_SMALLCASE);

			Document doc = builder.newDocument();
			Element kml = doc.createElement(InfraConstants.KML_SMALLCASE);
			kml.setAttribute(InfraConstants.XMLNS, "http://earth.google.com/kml/2.1");
			doc.appendChild(kml);
			Element document = doc.createElement(InfraConstants.DOCUMENT);
			kml.appendChild(document);
			Element placemark = doc.createElement(ForesightConstants.PLACEMARK);
			document.appendChild(placemark);
			Element name = doc.createElement(ForesightConstants.KML_NAME);
			name.setTextContent(
					kmlProcessor.getKmlName() != null ? kmlProcessor.getKmlName() : ForesightConstants.HIPHEN);
			placemark.appendChild(name);
			Element comment = doc.createElement(COMMENT);
			comment.setTextContent(
					kmlProcessor.getComment() != null ? kmlProcessor.getComment() : ForesightConstants.HIPHEN);
			placemark.appendChild(comment);

			Element colorCode = doc.createElement(ForesightConstants.COLORCODE);
			colorCode.setTextContent(
					kmlProcessor.getColorCode() != null ? kmlProcessor.getColorCode() : ForesightConstants.HIPHEN);
			placemark.appendChild(colorCode);

			Element createdTime = doc.createElement(ForesightConstants.CREATEDTIME);
			createdTime.setTextContent(kmlProcessor.getCreatedTime() != null ? kmlProcessor.getCreatedTime().toString()
					: ForesightConstants.HIPHEN);
			placemark.appendChild(createdTime);

			Element fileContent = doc.createElement(FILE_CONTENT);
			fileContent.setTextContent(
					kmlProcessor.getFileContent() != null ? kmlProcessor.getFileContent() : ForesightConstants.HIPHEN);
			placemark.appendChild(fileContent);

			Element fileSize = doc.createElement(FILE_SIZE);
			fileSize.setTextContent(
					kmlProcessor.getFileSize().toString() != null ? kmlProcessor.getFileSize().toString()
							: ForesightConstants.HIPHEN);
			placemark.appendChild(fileSize);

			Element generatedKmlName = doc.createElement(GENERATED_KML_NAME);
			generatedKmlName
					.setTextContent(kmlProcessor.getGeneratedKmlName() != null ? kmlProcessor.getGeneratedKmlName()
							: ForesightConstants.HIPHEN);
			placemark.appendChild(generatedKmlName);

			Element kmlPath = doc.createElement(KML_PATH);
			kmlPath.setTextContent(
					kmlProcessor.getKmlPath() != null ? kmlProcessor.getKmlPath() : ForesightConstants.HIPHEN);
			placemark.appendChild(kmlPath);

			Element latitude = doc.createElement(ForesightConstants.LATITUDE);
			latitude.setTextContent(kmlProcessor.getLatitude() != null ? kmlProcessor.getLatitude().toString()
					: ForesightConstants.HIPHEN);
			placemark.appendChild(latitude);

			Element longitude = doc.createElement(ForesightConstants.LONGITUDE);
			longitude.setTextContent(kmlProcessor.getLongitude() != null ? kmlProcessor.getLongitude().toString()
					: ForesightConstants.HIPHEN);
			placemark.appendChild(longitude);

			Element modifiedTime = doc.createElement(ForesightConstants.MODIFIEDTIME);
			modifiedTime
					.setTextContent(kmlProcessor.getModifiedTime() != null ? kmlProcessor.getModifiedTime().toString()
							: ForesightConstants.HIPHEN);
			placemark.appendChild(modifiedTime);

			Element zoomLevel = doc.createElement(ForesightConstants.ZOOM_LEVEL);
			zoomLevel.setTextContent(kmlProcessor.getZoomLevel() != null ? kmlProcessor.getZoomLevel().toString()
					: ForesightConstants.HIPHEN);
			placemark.appendChild(zoomLevel);

			Source source = new DOMSource(doc);
			StringWriter out = new StringWriter();
			Result result = new StreamResult(out);

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.transform(source, result);
			return out.toString();

		} catch (TransformerException | ParserConfigurationException e) {
			throw new RestException(e);
		}
	}

	@Override
	public boolean isKMLFileExistForUser(String fileName, Integer userid) {
		try {
			logger.info("Going to check if KMLFile {} exist for user {} ", fileName, userid);
			if (fileName != null && userid != null && !(fileName.equalsIgnoreCase(ForesightConstants.BLANK_STRING)))
				return iKmlProcessorDao.isKMLExist(userid, fileName);
			else
				throw new RestException("Invalid File Name.");
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}
	
	 @Override
		public String uploadKmlFile(String fileName, String colorCode, Integer userid) {
				logger.info("Going to upload KML file {} with userid {} ",fileName,userid);
				try {
					String jsonFileName=null;
					String kmlFileName=null;
					String filePath = ConfigUtils.getString(KML_FILE_UPLOAD_PATH);
					String fileNameWoExtension=getFileNameWOExtension(fileName);
					if(fileNameWoExtension != null) {
					jsonFileName=filePath+fileNameWoExtension + MyLayerConstants.JSON_EXTENSION;
					logger.info("jsonfilename: "+jsonFileName);
					kmlFileName=filePath+fileNameWoExtension + MyLayerConstants.KML_EXTENSION;
					logger.info("kmlFileName: "+kmlFileName);
					}
					return getJSONStringFromFile(jsonFileName, kmlFileName);
				} catch (Exception exception) {
					logger.error("Error in uploading kml file with name {} Exception {}", fileName, ExceptionUtils.getStackTrace(exception));
					throw new RestException("File Upload Failed");
				}
			}
	
	 private String getFileNameWOExtension(String fileName) {
			logger.info("Going to get FileNameWOExtension for filename : {} ",fileName);
			String fileNameWoExtension=null;
			try {
				String date = new SimpleDateFormat("yyMMdd").format(new Date());
				fileName = fileName.replaceAll(ForesightConstants.SPACE, ForesightConstants.UNDERSCORE);
				if (fileName.contains(ForesightConstants.DOT)) {
					fileNameWoExtension = StringUtils.substringBeforeLast(fileName, ForesightConstants.DOT);
					String fileExtension = StringUtils.substringAfterLast(fileName, ForesightConstants.DOT).toLowerCase();
					fileName = fileNameWoExtension + date + ForesightConstants.DOT + fileExtension;
				} else {
					fileName = fileName + date;
				}
				return fileNameWoExtension;
			} catch (Exception exception) {
				logger.error("Error in generating FileNameWOExtension for file {} Exception {}", fileName, ExceptionUtils.getStackTrace(exception));
			}
			return fileNameWoExtension;
			
			
		}
	 	 private String createJsonFile(String jsonFileName, String kmlFileName) {
	 		 try {
	 				String basePath = ConfigUtils.getString(ConfigEnum.OGR2OGR_BASE_PATH.getValue());
	 				basePath=StringUtils.isNotEmpty(basePath)?basePath:Symbol.EMPTY_STRING;
	 				String command=basePath + "ogr2ogr -f GeoJSON " + jsonFileName + ForesightConstants.SPACE + kmlFileName + " -nln test -append";
	 				logger.info("command : {}",command);
	 				String result = CommandLineUtils.execute(command);
	               return result;
	           } catch (Exception e) {
	               logger.error("error while creating geojson file:  " + ExceptionUtils.getStackTrace(e));
	           }
			return null;
			
	 	 }
	 	
   private String getJSONStringFromFile(String jsonFileName, String kmlFileName) {
       logger.info("Going to create json file {} from kml File {}", jsonFileName, kmlFileName);
       String jsonObj = null;
       try {
           String result = createJsonFile(jsonFileName,kmlFileName);
           if (result != null ) {
               jsonObj = FileUtils.readFileToString(new File(jsonFileName), Charset.defaultCharset());
               logger.info("jsonObj : {}",jsonObj);
                removeFile(jsonFileName, kmlFileName);
           } else {
               logger.error("json file not created.");
           }
       } catch (Exception exception) {
           logger.error("Error in creating json file {} Exception {}", jsonFileName, ExceptionUtils.getStackTrace(exception));
       }
       return jsonObj;
   }
    private void removeFile(String jsonFileName, String kmlFileName) {
	   try {
	   logger.info("going to remove json file : {}",jsonFileName);
	   new File(jsonFileName).delete();
	   logger.info("going to remove kml file : {}",kmlFileName);
       new File(kmlFileName).delete();
       logger.info("file deleted successfully.");
	   } catch (Exception exception) {
           logger.error("Error in removing file. Exception {}", ExceptionUtils.getStackTrace(exception));
       }
   }
}
