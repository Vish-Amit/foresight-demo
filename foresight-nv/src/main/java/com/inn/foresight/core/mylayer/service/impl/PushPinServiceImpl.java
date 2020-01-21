package com.inn.foresight.core.mylayer.service.impl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.io.kml.KMLFileConstant;
import com.inn.commons.io.kml.KMLGenerator;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.ApplicationContextProvider;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.mylayer.dao.IPushPinDao;
import com.inn.foresight.core.mylayer.model.PushPin;
import com.inn.foresight.core.mylayer.service.IPushPinService;
import com.inn.foresight.core.mylayer.utils.MyLayerConstants;
import com.inn.foresight.core.mylayer.wrapper.DataWrapper;
import com.inn.foresight.core.report.utils.ReportUtils;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.dao.UserDao;
import com.inn.product.um.user.model.User;

/**
 * This class is used for MyLayer for SitePlanning Activity functionality.
 *
 * @author innoeye
 */
@Service("PushPinServiceImpl")
@Transactional
public class PushPinServiceImpl extends AbstractService<Integer, PushPin> implements IPushPinService {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(PushPinServiceImpl.class);

	/** The ipush pin dao. */
	@Autowired
	private IPushPinDao iPushPinDao;

	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;
	
	@Autowired
	private UserDao iUsersDao;

	public void setDao(IPushPinDao dao) {
		super.setDao(dao);
		iPushPinDao = dao;
	}

	@Override
	public Map<String, String> insertPinsData(User user, List<DataWrapper> pins) {
		Integer userid = user.getUserid();
		if (userid != null) {
			logger.info("Going to insert {} export pins for userid {}", pins.size(), userid);
			int count = 0;
			for (DataWrapper pushPin : pins) {
				try {
					if (pushPin.getPinLatitude() != null && pushPin.getPinLongitude() != null
							&& pushPin.getGroupName() != null) {
						if (pushPin.getId() != null) {
							PushPin existingPin = iPushPinDao.getPushPinById(pushPin.getId());
							if (existingPin != null) {
								existingPin = setDataInPushPin(user, pushPin, existingPin);
								if(pushPin.getPinName() != null) {
									if(!pushPin.getPinName().equalsIgnoreCase(existingPin.getPinName())) {
										boolean isPinExistForGroupName = iPushPinDao.isPinExistForGroupName(userid,
												pushPin.getPinName(), pushPin.getGroupName(), pushPin.getId());
										if (!isPinExistForGroupName)
											existingPin.setPinName(pushPin.getPinName());
										else
											throw new RestException("pin name already exist ,please give another Pin name.");
									}
								}
								iPushPinDao.update(existingPin);
								count ++;
							}
						} else {
							List<String> groupNameForUser=iPushPinDao.getGroupNamesByUserId(user.getUserid());
							if(groupNameForUser.contains(pushPin.getGroupName())) {
							count=createPinInExistingGroup(user,pushPin,count);
							}else {
							count=createPinInNewGroup(user,pushPin,count);
							}
							}
					} else {
						throw new RestException("Invalid parameters");
					}
				} catch (Exception exception) {
					logger.error("Error in inserting pin data for userid {} Message {}", user.getUserid(),
							exception.getMessage());
				}
			}
			Map<String, String> map = new HashMap<>();
			if (count != 0) {
				if (count == pins.size())
					map.put(MyLayerConstants.RESULT, "All Pins in Group Created Successfully.");
				else
					map.put(MyLayerConstants.RESULT, " Out of " + pins.size() + "only " + count + " pins are Created Successfully.");
				logger.info("Total rows inserted : {} ", count);
				return map;
			} else {
				throw new RestException("Pin name already exist");
			}
		} else {
			throw new RestException(MyLayerConstants.INVALID_USER);
		}
	}
	private Integer createPinInNewGroup(User user,DataWrapper pushPin,Integer count) {
		logger.info("going to create pin in new group.");
		PushPin newPushPin = new PushPin();
		newPushPin = setDataInPushPin(user, pushPin, newPushPin);
		newPushPin.setCreatedTime(new Date());
		if (pushPin.getColorCode() != null)
			newPushPin.setColorCode(ForesightConstants.HASH + pushPin.getColorCode());
		if (pushPin.getImageName() != null)
			newPushPin.setImageName(pushPin.getImageName());
		if (pushPin.getPinName() != null)
			newPushPin.setPinName(pushPin.getPinName());
		iPushPinDao.create(newPushPin);
		count ++;
		return count;
	}
	private Integer createPinInExistingGroup(User user,DataWrapper pushPin,Integer count) {
		logger.info("going to create pin in existing group.");
		Long numberOfPinsInGroup = iPushPinDao.getNumberOfPinsInGroup(pushPin.getGroupName(),
				user.getUserid());
		logger.info("groupname: {},numberofPins: {}",pushPin.getGroupName(),numberOfPinsInGroup);
		Long pinLimit = getPinLimitFromSystemConfiguration();
		logger.info("pin Limit: {}",pinLimit);
		if (numberOfPinsInGroup <= pinLimit) {
			boolean isPinExistForGroup = iPushPinDao.isPinExistInGroup(pushPin.getPinName(),
					pushPin.getGroupName(), user.getUserid());
			if (!isPinExistForGroup) {
				PushPin newPushPin = new PushPin();
				newPushPin = setDataInPushPin(user, pushPin, newPushPin);
				newPushPin.setCreatedTime(new Date());
				Map map=getColorCodeAndImageNameByPinStatus(user.getUserid(),pushPin.getGroupName());
				if(map.get(pushPin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  +ForesightConstants.UNDERSCORE +MyLayerConstants.COLORCODE) != null)
					newPushPin.setColorCode(map.get(pushPin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  +ForesightConstants.UNDERSCORE +MyLayerConstants.COLORCODE).toString());
				else 
					newPushPin.setColorCode(ForesightConstants.HASH + pushPin.getColorCode());
				if(map.get(pushPin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  +ForesightConstants.UNDERSCORE +MyLayerConstants.IMAGENAME) != null)
					newPushPin.setImageName(map.get(pushPin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  +ForesightConstants.UNDERSCORE +MyLayerConstants.IMAGENAME).toString());
				else 
					newPushPin.setImageName(pushPin.getImageName());
				if (pushPin.getPinName() != null)
					newPushPin.setPinName(pushPin.getPinName());
				iPushPinDao.create(newPushPin);
				count ++;
			} else {
				logger.error("pin {} already exist in the group {}.", pushPin.getPinName(),
						pushPin.getGroupName());
			}
			logger.info("pin created successfully.");
		}else {
			throw new RestException(MyLayerConstants.PIN_LIMIT_EXCEEDED);
		}
		logger.info("counter: {}",count);
		return count;
	}
	private Long getPinLimitFromSystemConfiguration() {
		logger.info("going to get maximum pin limits from System Configuration.");
		Long pinLimit = 0L;
		String maxPin = iSystemConfigurationDao.getValueByNameAndType(MyLayerConstants.MYLAYER,
				MyLayerConstants.PUSHPIN_LIMIT);
		if (maxPin != null)
			pinLimit = Long.valueOf(maxPin);
		return pinLimit;
	}

	private GeographyL4 getGeographyL4ForPin(Double latitude, Double longitude) {
		if (latitude != null && longitude != null) {
			IGenericMapService iGenericMapService = ApplicationContextProvider.getApplicationContext()
					.getBean(IGenericMapService.class);
			GeographyL4 geography = iGenericMapService.getGeographyL4LatLng(latitude, longitude);
			if (geography != null)
				return geography;

		}
		return null;
	}
	private Map getColorCodeAndImageNameByPinStatus(Integer userid,String groupName) {
		List<DataWrapper> dataWrapperList=iPushPinDao.getColorCodeAndImageNameByPinStatus(userid, groupName);
		Map map=new HashMap();
		for (DataWrapper dataWrapper : dataWrapperList) {
			if(dataWrapper.getPinStatus() != null) {
				if(dataWrapper.getColorCode() != null)
				map.put(dataWrapper.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE + MyLayerConstants.COLORCODE, dataWrapper.getColorCode());
				if(dataWrapper.getImageName() != null)
				map.put(dataWrapper.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  + ForesightConstants.UNDERSCORE + MyLayerConstants.IMAGENAME, dataWrapper.getImageName());
			}
		}
		logger.info("map inside getColorCodeAndImageNameByPinStatus: {}",map);
		return map;
	}
	private PushPin setDataInPushPin(User user, DataWrapper pushPin, PushPin newPushPin) {
		if (user != null)
			newPushPin.setUser(user);
		newPushPin.setIsdeleted(false);
		newPushPin.setModifiedTime(new Date());
		if (pushPin != null) {
			if (pushPin.getPinLatitude() != null && pushPin.getPinLongitude() != null) {
				GeographyL4 geographyL4 = getGeographyL4ForPin(pushPin.getPinLatitude(), pushPin.getPinLongitude());
				if (geographyL4 != null)
					newPushPin.setGeographyL4(geographyL4);
			}
			if (pushPin.getAdditionalInfo() != null)
				newPushPin.setAdditionalInfo(pushPin.getAdditionalInfo());
			if (pushPin.getPinLatitude() != null)
				newPushPin.setLatitude(pushPin.getPinLatitude());
			if (pushPin.getPinLongitude() != null)
				newPushPin.setLongitude(pushPin.getPinLongitude());
			if (pushPin.getComments() != null)
				newPushPin.setComments(pushPin.getComments());
			if (pushPin.getPinStatus() != null)
				newPushPin.setPinStatus(pushPin.getPinStatus());
			if (pushPin.getGroupName() != null)
				newPushPin.setGroupName(pushPin.getGroupName());
			if (pushPin.getOpacity() != null)
				newPushPin.setOpacity(pushPin.getOpacity());
			if (pushPin.getZoomLevel() != null)
				newPushPin.setZoomLevel(pushPin.getZoomLevel());
		}
		return newPushPin;
	}

	@Override
	public boolean updateGroupName(Integer userid, List<DataWrapper> dataWrapperList) {
		try {
			logger.info("Going to update groupName Setting for userid {}", userid);
			if(dataWrapperList != null && !dataWrapperList.isEmpty()) {
			if(dataWrapperList.get(0) != null) {
			List<String> existingGroupNameList = iPushPinDao.getAllGroupNames(userid, dataWrapperList.get(0).getOldGroupName());
			if (!(existingGroupNameList.contains(dataWrapperList.get(0).getNewGroupName()))) {
				for (DataWrapper dataWrapper : dataWrapperList) {
				try {
					
					if(dataWrapper.getNewPinStatus().equalsIgnoreCase(dataWrapper.getOldPinStatus())) {
						
						if (dataWrapper.getColorCode() != null && !(dataWrapper.getColorCode().contains(ForesightConstants.HASH))) {
							dataWrapper.setColorCode(ForesightConstants.HASH + dataWrapper.getColorCode());
						}
						iPushPinDao.updateGroupName(userid, dataWrapper.getColorCode(), dataWrapper);
						
					
						}
					else {
						
					List<String> existingPinStatusList=iPushPinDao.getPinStatusByGroupNameAndUserid(userid, dataWrapper.getNewGroupName());
					if(existingPinStatusList != null && !existingPinStatusList.isEmpty()) {
						if(existingPinStatusList.contains(dataWrapper.getNewPinStatus())){
							Map map=getColorCodeAndImageNameByPinStatus(userid,dataWrapper.getNewGroupName());
							if(map.get(dataWrapper.getNewPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  +ForesightConstants.UNDERSCORE +MyLayerConstants.COLORCODE) != null)
							dataWrapper.setColorCode(map.get(dataWrapper.getNewPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  +ForesightConstants.UNDERSCORE +MyLayerConstants.COLORCODE).toString());
							if(map.get(dataWrapper.getNewPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  +ForesightConstants.UNDERSCORE +MyLayerConstants.IMAGENAME) != null)
							dataWrapper.setImageName(map.get(dataWrapper.getNewPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  +ForesightConstants.UNDERSCORE +MyLayerConstants.IMAGENAME).toString());
							if (dataWrapper.getColorCode() != null && !(dataWrapper.getColorCode().contains(ForesightConstants.HASH))) {
								dataWrapper.setColorCode(ForesightConstants.HASH + dataWrapper.getColorCode());
							}
							iPushPinDao.updateGroupName(userid, dataWrapper.getColorCode(), dataWrapper);
						}else {
				if (dataWrapper.getColorCode() != null && !(dataWrapper.getColorCode().contains(ForesightConstants.HASH))) {
					dataWrapper.setColorCode(ForesightConstants.HASH + dataWrapper.getColorCode());
				}
				iPushPinDao.updateGroupName(userid, dataWrapper.getColorCode(), dataWrapper);
					}
					}
					}
				}catch(Exception exception) {
					logger.error("unable to update pinstatus: {},Exception: {}",dataWrapper,exception.getMessage());
				}
			}
			}else
				throw new RestException("GroupName Already Exist.");
		}
		}
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
		return true;
	}

	@Override
	public boolean deleteGroupName(Integer userid, String groupName) {
		try {
			logger.info("Going to delete groupName {} for userid {} ", groupName, userid);
			return iPushPinDao.deleteGroupName(userid, groupName);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public boolean deletePinFromGroup(Integer userid, Integer id) {
		try {
			logger.info("Going to delete pin id {} for userid {}", id, userid);
			return iPushPinDao.deletePinFromGroup(userid, id);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public  Map uploadFile(InputStream inputStream, String fileName, User user) {
		Integer userId = user.getUserid();
		 Map map = new HashMap<>();
		if (userId == null) {
			throw new RestException(MyLayerConstants.INVALID_USER);
		}
		try {
			String groupName = ForesightConstants.BLANK_STRING;
			String fileFormat = ForesightConstants.BLANK_STRING;

			if (fileName.contains(ForesightConstants.DOT)) {
				groupName = StringUtils.substringBefore(fileName, ForesightConstants.DOT);
				logger.info("Groupname {}", groupName);

				fileFormat = StringUtils.substringAfter(fileName, ForesightConstants.DOT).toLowerCase();
				logger.info("fileFormat {}", fileFormat);
			}

			String path = System.getProperty(ForesightConstants.TOMCAT_PATH) + ForesightConstants.FORWARD_SLASH
					+ ConfigUtils.getString(ConfigEnum.PLANNED_SITE_REPORT_PATH.getValue());
			File file = createPath(path);
			String tempFilePath = path + fileName;
			logger.info("tempFilePath: " + tempFilePath);
			writeFileDataInTemporaryPath(inputStream, tempFilePath);

			logger.info("file write completed.");
			Long pinLimit = getPinLimitFromSystemConfiguration();
			 List<String> imageNames = getSheetNameFromSystemConfiguration("ImageNameForPushPin", "LAYER");
			if (fileFormat.equalsIgnoreCase(ForesightConstants.XLSX)) {
				logger.info("Going to process xlsx file from {}", tempFilePath);
				FileInputStream excelFile = new FileInputStream(tempFilePath);
				XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
				XSSFSheet sheet = workbook.getSheetAt(0);
				logger.info("Going to process push pin from Excel file");
				if (sheet.getLastRowNum() <= pinLimit) {
					map = processPushPinExcelFile(sheet, user, groupName,imageNames);
				}else
					throw new RestException("Pin Limit Exceeded.");
			}

			else if (fileFormat.equalsIgnoreCase(ForesightConstants.XLS)) {
				logger.info("Going to process xls file from {}", tempFilePath);
				FileInputStream excelFile = new FileInputStream(tempFilePath);
				HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
				HSSFSheet sheet = workbook.getSheetAt(0);
				if (sheet.getLastRowNum() <= pinLimit)
					map = processPushpinXLSFile(sheet, user, groupName,imageNames);
				else
					throw new RestException("Pin Limit Exceeded.");
			}
			if(file.delete()) {
			if(new File(path).delete()) {
			}
			}
			return map;
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		} catch (OfficeXmlFileException officeXmlFileException) {
			logger.error("OfficeXmlFileException caught Exception {} ",
					ExceptionUtils.getStackTrace(officeXmlFileException));
			throw new RestException("Invalid file.");
		} catch (FileNotFoundException fileNotFoundException) {
			logger.error("FileNotFoundException caught Exception {} ",
					ExceptionUtils.getStackTrace(fileNotFoundException));
		} catch (IOException ioException) {
			logger.error("IOException caught Exception {} ", ExceptionUtils.getStackTrace(ioException));
		} catch (Exception exception) {
			logger.error("Error in uploading Pushpin file Exception {} ", ExceptionUtils.getStackTrace(exception));
		}
		return map;
	}

	private void writeFileDataInTemporaryPath(InputStream inputStream, String tempFilePath) throws IOException {
		try {
			File file = new File(tempFilePath);
			try(OutputStream out  = new FileOutputStream(file)) {
				byte[] buf = new byte[5242880];
				int len;
				while ((len = inputStream.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			} 
			inputStream.close();
		} catch (IOException ioException) {
			logger.error("IOException caught while writing data in temporary path {} Exception {} ", tempFilePath,
					ExceptionUtils.getStackTrace(ioException));
		}
	}

	private File createPath(String path) {
		File file = new File(path);
		if (!file.isFile() && !file.getParentFile().exists()) {
			file.mkdirs();
			logger.info("Directory has been created successfully, path is :{}", file.getParentFile());
		}
		file = new File(file.getAbsolutePath());
		if (!file.getAbsoluteFile().exists()) {
			file.mkdirs();
			logger.info("Directory has been created successfully, path is :{}", file.getParentFile());
		}
		return file;
	}
	@SuppressWarnings("unchecked")
	private Map processPushPinExcelFile(XSSFSheet sheet, User user, String groupName, List<String> imageNames)
            throws RestException {
        int x = 1;
        int y = 0;
        int z = 0;
        int counter = 0;
        String pinName = ForesightConstants.BLANK_STRING;
        String pinLat = ForesightConstants.BLANK_STRING;
        String pinLong = ForesightConstants.BLANK_STRING;
        String comments = ForesightConstants.BLANK_STRING;
        String pinStatus = ForesightConstants.BLANK_STRING;
        String colorCode = ForesightConstants.BLANK_STRING;
        String image = ForesightConstants.BLANK_STRING;
        Double latitude = new Double(0);
        Double longitude = new Double(0);
        Map pinMap=new HashMap();
        while (z <= sheet.getLastRowNum()) {
            if (z == 0) {
                try {
                    validateHeadersForExcelFile(sheet.getRow(z));
                } catch (RestException RestException) {
                    throw new RestException(RestException.getMessage());
                }
            }
            z++;
        }

        List<String> listOfPinName=new ArrayList<String>();
        List<DataWrapper> listOfPushPin=new ArrayList<>();
        while (x <= sheet.getLastRowNum()) {
            PushPin pushpin = new PushPin();
            DataWrapper wrapper=new DataWrapper();
            try {
                XSSFRow row = sheet.getRow(x);

                y = 0;
                boolean flag = true;
                while (y <= row.getLastCellNum()) {
                    switch (y) {
                        case 0:
                            pinName = row.getCell(y).toString();
                            if (pinName != null && !StringUtils.isBlank(pinName) && !listOfPinName.contains(pinName)) {
                                pushpin.setPinName(pinName);
                                listOfPinName.add(pinName);
                            } else {
                                logger.info("Pinname is blank  for row {} col {}", x, y);
                                flag = false;
                            }
                            break;
                        case 1:
                            pinLat = row.getCell(y).toString();
                            if (pinLat != null) {
                                latitude = getValueAsDouble(pinLat);
                                if (latitude != null) {
                                    pushpin.setLatitude(latitude);
                                } else {
                                    logger.info("PinLat is not valid for row {} col {}", x, y);
                                    flag = false;
                                }
                            } else {
                                logger.info("PinLat is blank for row {} col {}", x, y);
                                flag = false;
                            }
                            break;
                        case 2:
                            pinLong = row.getCell(y).toString();
                            if (pinLong != null) {
                                longitude = getValueAsDouble(pinLong);
                                if (longitude != null) {
                                    pushpin.setLongitude(longitude);
                                } else {
                                    logger.info("PinLong is not valid for row {} col {}", x, y);
                                    flag = false;
                                }
                            } else {
                                logger.info("PinLong is blank for row {} col {}", x, y);
                                flag = false;
                            }
                            break;
                        case 3:
						XSSFCell status = row.getCell(y);
						if (status != null) {
							pinStatus = row.getCell(y).toString();
							if (pinStatus != null && !StringUtils.isBlank(pinStatus)
									&& !pinStatus.equalsIgnoreCase(ForesightConstants.NULL_STRING)) {
								if (pinStatus.equalsIgnoreCase(ForesightConstants.PLANNED_UPPERCASE)) {
									pushpin.setPinStatus(InfraConstants.PLANNED_CAMEL_CASE);
								} else if (pinStatus.equalsIgnoreCase(InfraConstants.INPROGRESS_STATUS)) {
									pushpin.setPinStatus(InfraConstants.INPROGRESS_STATUS_CAMEL);
								} else if (pinStatus.equalsIgnoreCase(InfraConstants.COMPLETED_UPPERCASE)) {
									pushpin.setPinStatus(InfraConstants.STATUS_COMPLETED);
								} else {
									pushpin.setPinStatus(pinStatus);
								}
							} 
						} 
						break;

						 case 4:
                       if(pushpin.getPinStatus() != null) {
                        if(pinMap.get(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.COLORCODE) == null){
						XSSFCell color = row.getCell(y);
						if (color != null) {
							colorCode = row.getCell(y).toString();
							if ((colorCode != null || !StringUtils.isBlank(colorCode)) && validateColorCode(colorCode) ) {
								if(!colorCode.equalsIgnoreCase(ForesightConstants.NULL_STRING)) {
									if(pushpin.getPinStatus() != null) {
										pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.COLORCODE,colorCode);
									}
								}
							} else {
								pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.COLORCODE,MyLayerConstants.GREY_COLORCODE);
							}
						} else {
							pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.COLORCODE,MyLayerConstants.GREY_COLORCODE);
						}
                    }
                    }
						break;
					case 5:
						if(pushpin.getPinStatus() !=  null) {
						 if(pinMap.get(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME) == null){
						XSSFCell imageName = row.getCell(y);
						if (imageName != null) {
							image = row.getCell(y).toString();
							if (image != null || !StringUtils.isBlank(image)) {
								if(!image.equalsIgnoreCase(ForesightConstants.NULL_STRING)){
								String validImageName = validateImageName(imageNames, image);
								if (!validImageName.equalsIgnoreCase(ForesightConstants.FALSE_LOWERCASE)) {
									pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME,validImageName);
								} else {
									pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME,MyLayerConstants.PUSHPIN_PIN);
								}
							}
							} else {
								pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME,MyLayerConstants.PUSHPIN_PIN);
							}
						} else {
							pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME,MyLayerConstants.PUSHPIN_PIN);
						}
                       }
						}
						break;
                        case 6:
                        	 XSSFCell comment = row.getCell(y);
                        	 if (comment != null) {
                        		 row.getCell(y).setCellType(Cell.CELL_TYPE_STRING);
                        		 comments=row.getCell(y).toString();
                                 pushpin.setComments(comments);
                             } else {
                            	 pushpin.setComments(null);
                             }
                        	 
                            break;
                        case 7:
                            break;
                        case 8:
                            break;
                        case 9:
                            break;
                        case 10:
                            break;
                        case 11:
                            XSSFCell cell = row.getCell(y);
                            if (cell == null) {
                                pushpin.setAdditionalInfo(null);
                            } else {
                                row.getCell(y).setCellType(Cell.CELL_TYPE_STRING);
                                row.getCell(y).toString();
                                pushpin.setAdditionalInfo(row.getCell(y).toString());
                            }
                            break;
                    }
                    if (!flag) {
                        logger.info("Row number {} not parsed", x);
                        pushpin = null;
                        break;
                    }
                    y++;
                }

                if (pushpin != null) {
                    pushpin.setGroupName(groupName);
                    pushpin.setModifiedTime(new Date());
                    pushpin.setCreatedTime(new Date());
                    pushpin.setUser(user);
                    pushpin.setOpacity(1.0);
                    if(pushpin.getPinStatus() == null)
						pushpin.setPinStatus(MyLayerConstants.PUSHPINSTATUS_NEW);
					if(pinMap.get(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.COLORCODE) == null) {
						pushpin.setColorCode(MyLayerConstants.GREY_COLORCODE);
					}else {
						pushpin.setColorCode(pinMap.get(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.COLORCODE).toString());
					}
					if(pinMap.get(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME) == null) {
						pushpin.setImageName(MyLayerConstants.PUSHPIN_PIN);
					}else {
						pushpin.setImageName(pinMap.get(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME).toString());
					}
                    if(pushpin.getLatitude() != null && pushpin.getLongitude() != null) {
                        GeographyL4 geographyL4=getGeographyL4ForPin(pushpin.getLatitude(),pushpin.getLongitude());
                        if(geographyL4 != null)
                        	pushpin.setGeographyL4(geographyL4);
                        }
                    iPushPinDao.create(pushpin);
                    wrapper=setPushPinDataInWrapper(pushpin);
                    listOfPushPin.add(wrapper);
                    counter++;
                }
            } catch (Exception exception) {
                logger.error("Error in parsing row number {} column number {} Message {}", x, y,
                        exception.getMessage());
            }
            x++;
        }
        
        if (counter != 0) {
            Map map = new HashMap<>();
            if (counter == sheet.getLastRowNum())
            	map.put("Result", "All Pins Uploaded Successfully.");
            else
            	map.put("Result", "Out of " + sheet.getLastRowNum() + " only " + counter + " pins are plotted.");
            map.put("Data", listOfPushPin);
            logger.info("Total rows inserted {} ", counter);
           
            return map;
        } else {
            throw new RestException("Imported file data is not valid");
        }

    }
	private DataWrapper setPushPinDataInWrapper(PushPin pushpin) {
		DataWrapper wrapper=new DataWrapper();
		 wrapper.setAdditionalInfo(pushpin.getAdditionalInfo());
         wrapper.setColorCode(pushpin.getColorCode());
         wrapper.setComments(pushpin.getComments());
         wrapper.setCreatedTime(pushpin.getCreatedTime());
         wrapper.setGroupName(pushpin.getGroupName());
         wrapper.setId(pushpin.getId());
         wrapper.setImageName(pushpin.getImageName());
         wrapper.setLabelProperty(pushpin.getLabelProperty());
         wrapper.setLatitude(pushpin.getLatitude());
         wrapper.setLongitude(pushpin.getLongitude());
         wrapper.setModifiedTime(pushpin.getModifiedTime());
         wrapper.setOpacity(pushpin.getOpacity());
         wrapper.setPinName(pushpin.getPinName());
         wrapper.setPinStatus(pushpin.getPinStatus());
         wrapper.setSharedBy(pushpin.getSharedBy());
         wrapper.setStatus(pushpin.getStatus());
         wrapper.setZoomLevel(pushpin.getZoomLevel());
		return wrapper;
	}

	private boolean validateOrderOfHeaders(List<String> list) {
		boolean ordered = false;
		Map<String, String> map = getMappingForGeographies();
		if (list != null && !list.isEmpty()) {
			String[] headers = { "Pin Name", "Pin Lat", "Pin Long", "Status", "Color", "Symbol", "Comments",
					map.get(ForesightConstants.GEOGRAPHY_L4), map.get(ForesightConstants.GEOGRAPHY_L3),
					map.get(ForesightConstants.GEOGRAPHY_L2), map.get(ForesightConstants.GEOGRAPHY_L1),
					"Additional Info" };
			int i = 0;
			for (String content : list) {
				if (content.equalsIgnoreCase(headers[i])) {
					ordered = true;
				} else {
					ordered = false;
					break;
				}
				i++;
			}
			return ordered;
		}
		return ordered;
	}

	private Double getValueAsDouble(String fieldName) {
		if (!Strings.isNullOrEmpty(fieldName) && !fieldName.equalsIgnoreCase(ForesightConstants.NULL_STRING)) {
			return Double.parseDouble(fieldName);
		}
		return null;
	}

	private void validateHeadersForExcelFile(XSSFRow row) {
		logger.info("Total no of columns  in excel file " + row.getLastCellNum());
		List<String> list = new ArrayList<>();
		int y = 0;
		if (row.getLastCellNum() != 12) {
			throw new RestException(MyLayerConstants.MANDATORY_COLUMNS_ARE_MISSING);
		} else {
			while (y <= row.getLastCellNum()) {
				if (row.getCell(y) != null) {
					list.add(row.getCell(y).toString());
				}
				y++;
			}
			if (list.contains(ForesightConstants.BLANK_STRING) || list.contains(ForesightConstants.SPACE)) {
				throw new RestException(MyLayerConstants.MANDATORY_COLUMNS_ARE_MISSING);
			}
			if (!validateOrderOfHeaders(list)) {
				throw new RestException("Mandatory columns are not in order.");
			}
		}
	}

	private static boolean validateColorCode(String hexColorCode) {
		String hexPattern = "^#([a-zA-Z0-9]{6})$";
		Pattern pattern = null;
		pattern = Pattern.compile(hexPattern);
		Matcher matcher = pattern.matcher(hexColorCode);
		return matcher.matches();
	}

	@Override
	public List<String> getPinNamesForGroupNames(List<String> groupNames, Integer userid) {
		try {
			logger.info("Going to get pin names for userid {}", userid);
			List<String> groups = InfraUtils.convertListElements(groupNames, InfraConstants.LIST_TOUPPERCASE);
			return iPushPinDao.getPinNamesForGroupNames(groups, userid);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public void sharedPushPinGroup(User user, String groupName, String email) {
		try {
			logger.info("Going to share pushpin group {} to emailid {} ", groupName, email);
			Integer senderId = user.getUserid();
			String senderName = user.getEmail();

			if (senderId != null && senderName != null) {
				logger.info("Pushpin group sender name {}", senderName);

				if (email.equalsIgnoreCase(senderName)) {
					throw new RestException("User cannot share pin group to himself/herself");
				}

				User receiver = iUsersDao.findUserByEmail(email.toLowerCase());

				if (receiver != null) {
					Integer receiverId = receiver.getUserid();
					List<String> groupNames = iPushPinDao.getGroupNamesByUserId(receiverId);
					if (groupNames != null && groupNames.contains(groupName)) {
						throw new RestException("Group name Already Exist for user.");
					} else {
						List<PushPin> senderPinsList = iPushPinDao.getPinGroupData(senderId, groupName, null, null);
						logger.info("Total {} pins received for groupName {} for senderid {}", senderPinsList.size(),
								groupName, senderId);
						insertPinDetailsForReceiver(senderPinsList, receiver, senderName);
					}
				} else {
					throw new RestException("Invalid user");
				}
			} else {
				throw new RestException("Invalid user");
			}
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		} catch (Exception exception) {
			logger.info("Error in sharing pushpin group {} to emailid {} Exception {}", groupName, email,
					ExceptionUtils.getStackTrace(exception));
		}
	}

	private void insertPinDetailsForReceiver(List<PushPin> senderPinsList, User recevier, String senderName) {
		logger.info("Going to insert pushpin group details received from {}", recevier.getEmail());
		for (PushPin pushPin : senderPinsList) {
			PushPin pin = new PushPin();
			pin.setUser(recevier);
			pin.setSharedBy(senderName);
			pin.setSharedDate(new Date());
			pin.setPinName(pushPin.getPinName());
			pin.setPinStatus(pushPin.getPinStatus());
			pin.setAdditionalInfo(pushPin.getAdditionalInfo());
			pin.setComments(pushPin.getComments());
			pin.setColorCode(pushPin.getColorCode());
			pin.setImageName(pushPin.getImageName());
			pin.setOpacity(pushPin.getOpacity());
			pin.setLatitude(pushPin.getLatitude());
			pin.setLongitude(pushPin.getLongitude());
			pin.setModifiedTime(new Date());
			pin.setCreatedTime(new Date());
			pin.setGroupName(pushPin.getGroupName());
			iPushPinDao.create(pin);
		}
	}

	private String validateImageName(List<String> imageNames, String imageName) {
		for (int i = 0; i < imageNames.size(); i++) {
			if (imageName.equalsIgnoreCase(imageNames.get(i))) {
				return imageNames.get(i);
			}
			if (i == imageNames.size() - 1) {
				return ForesightConstants.FALSE_LOWERCASE;
			}
		}
		return ForesightConstants.FALSE_LOWERCASE;
	}

	@Override
	public List<DataWrapper> getPinGroupDetails(Integer userid, Integer lowerLimit, Integer upperLimit) {
		try {
			logger.info("Going to get pin details for userid {}", userid);
			return iPushPinDao.getPinGroupDetails(null, lowerLimit, upperLimit, userid);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public boolean deletePinsByPinStatus(Integer userid, String groupName, String pinStatus) {
		try {
			logger.info("Going to delete pins from group {} having pinstatus {} for userid {}", groupName, pinStatus,
					userid);
			boolean deleted= iPushPinDao.deletePinsByPinStatus(userid, groupName, pinStatus);
			Long numberOfPinStaus=iPushPinDao.getNumberOfPinStatusInGroup(userid, groupName);
			if(numberOfPinStaus != null && numberOfPinStaus == 0) {
				return deleteGroupName(userid, groupName);
			}else {
				return deleted;
			}
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}

	}

	private  Map processPushpinXLSFile(HSSFSheet sheet, User user, String groupName, List<String> imageNames)
            throws RestException {
        int x = 1;
        int y = 0;
        int z = 0;
        int counter = 0;
        String pinName = ForesightConstants.BLANK_STRING;
        String pinLat = ForesightConstants.BLANK_STRING;
        String pinLong = ForesightConstants.BLANK_STRING;
        String comments = ForesightConstants.BLANK_STRING;
        String pinStatus = ForesightConstants.BLANK_STRING;
        String colorCode = ForesightConstants.BLANK_STRING;
        String image = ForesightConstants.BLANK_STRING;
        Double latitude = new Double(0);
        Double longitude = new Double(0);
        Map pinMap=new HashMap();
        while (z <= sheet.getLastRowNum()) {
            if (z == 0) {
                try {
                    validateHeadersForXLSFile(sheet.getRow(z));
                } catch (RestException RestException) {
                    throw new RestException(RestException.getMessage());
                }
            }
            z++;
        }
        List<String> listOfPinName=new ArrayList<String>();
        List<DataWrapper> listOfPushPin=new ArrayList<>();
        while (x <= sheet.getLastRowNum()) {
            PushPin pushpin = new PushPin();
            DataWrapper wrapper=new DataWrapper();
            try {
                HSSFRow row = sheet.getRow(x);

                y = 0;
                boolean flag = true;
                while (y <= row.getLastCellNum()) {
                    switch (y) {
                        case 0:
                            pinName = row.getCell(y).toString();
                            if (pinName != null && !StringUtils.isBlank(pinName) && !listOfPinName.contains(pinName)) {
                                pushpin.setPinName(pinName);
                                listOfPinName.add(pinName);
                            } else {
                                logger.info("Pinname is blank  for row {} col {}", x, y);
                                flag = false;
                            }
                            break;
                        case 1:
                            pinLat = row.getCell(y).toString();
                            if (pinLat != null) {
                                latitude = getValueAsDouble(pinLat);
                                if (latitude != null) {
                                    pushpin.setLatitude(latitude);
                                } else {
                                    logger.info("PinLat is not valid for row {} col {}", x, y);
                                    flag = false;
                                }
                            } else {
                                logger.info("PinLat is blank for row {} col {}", x, y);
                                flag = false;
                            }
                            break;
                        case 2:
                            pinLong = row.getCell(y).toString();
                            if (pinLong != null) {
                                longitude = getValueAsDouble(pinLong);
                                if (longitude != null) {
                                    pushpin.setLongitude(longitude);
                                } else {
                                    logger.info("PinLong is not valid for row {} col {}", x, y);
                                    flag = false;
                                }
                            } else {
                                logger.info("PinLong is blank for row {} col {}", x, y);
                                flag = false;
                            }
                            break;
                        case 3:
						HSSFCell status = row.getCell(y);
						if (status != null) {
							pinStatus = row.getCell(y).toString();
							if (pinStatus != null && !StringUtils.isBlank(pinStatus)
									&& !pinStatus.equalsIgnoreCase(ForesightConstants.NULL_STRING)) {
								if (pinStatus.equalsIgnoreCase(ForesightConstants.PLANNED_UPPERCASE)) {
									pushpin.setPinStatus(InfraConstants.PLANNED_CAMEL_CASE);
								} else if (pinStatus.equalsIgnoreCase(InfraConstants.INPROGRESS_STATUS)) {
									pushpin.setPinStatus(InfraConstants.INPROGRESS_STATUS_CAMEL);
								} else if (pinStatus.equalsIgnoreCase(InfraConstants.COMPLETED_UPPERCASE)) {
									pushpin.setPinStatus(InfraConstants.STATUS_COMPLETED);
								} else {
									pushpin.setPinStatus(pinStatus);
								}
							} else {
								pushpin.setPinStatus(MyLayerConstants.PUSHPINSTATUS_NEW);
							}
						} else {
							pushpin.setPinStatus(MyLayerConstants.PUSHPINSTATUS_NEW);
						}
						break;

						 case 4:
                       if(pushpin.getPinStatus() != null) {
                        if(pinMap.get(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.COLORCODE) == null){
						HSSFCell color = row.getCell(y);
						if (color != null) {
							colorCode = row.getCell(y).toString();
							if ((colorCode != null || !StringUtils.isBlank(colorCode)) && validateColorCode(colorCode) ) {
								if(!colorCode.equalsIgnoreCase(ForesightConstants.NULL_STRING)) {
									if(pushpin.getPinStatus() != null) {
										pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.COLORCODE,colorCode);
									}
								}
							} else {
								pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.COLORCODE,MyLayerConstants.GREY_COLORCODE);
							}
						} else {
							pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.COLORCODE,MyLayerConstants.GREY_COLORCODE);
						}
                    }
                    }
						break;
					case 5:
						if(pushpin.getPinStatus() !=  null) {
						 if(pinMap.get(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME) == null){
						HSSFCell imageName = row.getCell(y);
						if (imageName != null) {
							image = row.getCell(y).toString();
							if (image != null || !StringUtils.isBlank(image)) {
								if(!image.equalsIgnoreCase(ForesightConstants.NULL_STRING)){
								String validImageName = validateImageName(imageNames, image);
								if (!validImageName.equalsIgnoreCase(ForesightConstants.FALSE_LOWERCASE)) {
									pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME,validImageName);
								} else {
									pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME,MyLayerConstants.PUSHPIN_PIN);
								}
							}
							} else {
								pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME,MyLayerConstants.PUSHPIN_PIN);
							}
						} else {
							pinMap.put(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)  +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME,MyLayerConstants.PUSHPIN_PIN);
						}
                       }
						}
						break;
                        case 6:
                        	 HSSFCell comment = row.getCell(y);
                        	 if (comment != null) {
                        		 row.getCell(y).setCellType(Cell.CELL_TYPE_STRING);
                        		 comments=row.getCell(y).toString();
                                 pushpin.setComments(comments);
                             } else {
                            	 pushpin.setComments(null);
                             }
                        	 
                            break;
                        case 7:
                            break;
                        case 8:
                            break;
                        case 9:
                            break;
                        case 10:
                            break;
                        case 11:
                            HSSFCell cell = row.getCell(y);
                            if (cell == null) {
                                pushpin.setAdditionalInfo(null);
                            } else {
                                row.getCell(y).setCellType(Cell.CELL_TYPE_STRING);
                                row.getCell(y).toString();
                                pushpin.setAdditionalInfo(row.getCell(y).toString());
                            }
                            break;
                    }
                    if (!flag) {
                        logger.info("Row number {} not parsed", x);
                        pushpin = null;
                        break;
                    }
                    y++;
                }

                if (pushpin != null) {
                    pushpin.setGroupName(groupName);
                    pushpin.setModifiedTime(new Date());
                    pushpin.setCreatedTime(new Date());
                    pushpin.setUser(user);
                    pushpin.setOpacity(1.0);
                    if(pushpin.getPinStatus() == null)
						pushpin.setPinStatus(MyLayerConstants.PUSHPINSTATUS_NEW);
					if(pinMap.get(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.COLORCODE) == null) {
						pushpin.setColorCode(MyLayerConstants.GREY_COLORCODE);
					}else {
						pushpin.setColorCode(pinMap.get(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.COLORCODE).toString());
					}
					if(pinMap.get(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE)+ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME) == null) {
						pushpin.setImageName(MyLayerConstants.PUSHPIN_PIN);
					}else {
						pushpin.setImageName(pinMap.get(pushpin.getPinStatus().replace(ForesightConstants.SPACE,ForesightConstants.UNDERSCORE) +ForesightConstants.UNDERSCORE+MyLayerConstants.IMAGENAME).toString());
					}
                    if(pushpin.getLatitude() != null && pushpin.getLongitude() != null) {
                        GeographyL4 geographyL4=getGeographyL4ForPin(pushpin.getLatitude(),pushpin.getLongitude());
                        if(geographyL4 != null)
                        	pushpin.setGeographyL4(geographyL4);
                        }
                    iPushPinDao.create(pushpin);
                    wrapper=setPushPinDataInWrapper(pushpin);
                    listOfPushPin.add(wrapper);
                    counter++;
                }
            } catch (Exception exception) {
                logger.error("Error in parsing row number {} column number {} Message {}", x, y,
                        exception.getMessage());
            }
            x++;
        }
        if (counter != 0) {
        	 Map map = new HashMap<>();
            if (counter == sheet.getLastRowNum())
            	map.put("Result", "All Pins Uploaded Successfully.");
            else
            	map.put("Result", "Out of " + sheet.getLastRowNum() + " only " + counter + " pins are plotted.");
            map.put("Data", listOfPushPin);
            logger.info("Total rows inserted {} ", counter);
            return map;
        } else {
            throw new RestException("Imported file data is not valid");
        }

    }
	/**
	 * Validate headers for XLS file.
	 *
	 * @param row
	 *            the row
	 * @throws RestException
	 *             the rest exception
	 */
	private void validateHeadersForXLSFile(HSSFRow row) {
		logger.info("Total no of columns  in excel file " + row.getLastCellNum());
		List<String> list = new ArrayList<>();
		int y = 0;
		if (row.getLastCellNum() != 12) {
			throw new RestException("Headers are not correct.");
		} else {
			while (y <= row.getLastCellNum()) {
				if (row.getCell(y) != null) {
					list.add(row.getCell(y).toString());
				}
				y++;
			}
			if (list.contains(ForesightConstants.BLANK_STRING) || list.contains(ForesightConstants.SPACE)) {
				throw new RestException("Mandatory columns are missing.");
			}
			if (!validateOrderOfHeaders(list)) {
				throw new RestException("Mandatory columns are not in order.");
			}
		}
	}

	@Override
	public String getDataInExcel(List<DataWrapper> wrappers) {
		logger.info("Going to process  Export Pins for excel file generation");
		String fileName = null;
		try {
			if (wrappers != null)
				fileName = generateExcelForExportPins(wrappers);
			return fileName;
		} catch (Exception e) {
			logger.error("Error in processing export pins data to excel file Exception {} ",
					ExceptionUtils.getStackTrace(e));
		}
		return fileName;
	}

	private static Map<String, XSSFCellStyle> createStylesForPushPinReport(Workbook wb) {
		Map<String, XSSFCellStyle> styles = new HashMap<>();
		XSSFCellStyle style;

		// --------------------colors--------------
		Color color1 = Color.decode("#F4B183");
		XSSFColor orange = new XSSFColor(color1);
		// ------------Fonts---------------

		XSSFFont headerFont = (XSSFFont) wb.createFont();
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setFontName(ForesightConstants.FONTNAME);

		XSSFFont dataFont = (XSSFFont) wb.createFont();
		dataFont.setFontHeightInPoints((short) 10);
		dataFont.setFontName(ForesightConstants.FONTNAME);

		XSSFFont boldFont = (XSSFFont) wb.createFont();
		boldFont.setFontHeightInPoints((short) 11);
		boldFont.setFontName(ForesightConstants.FONTNAME);
		boldFont.setBold(true);

		style = (XSSFCellStyle) wb.createCellStyle();
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(orange);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setWrapText(true);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderColor(BorderSide.LEFT, new XSSFColor(new java.awt.Color(0, 0, 0)));
		style.setBorderColor(BorderSide.RIGHT, new XSSFColor(new java.awt.Color(0, 0, 0)));
		style.setBorderColor(BorderSide.TOP, new XSSFColor(new java.awt.Color(0, 0, 0)));
		style.setBorderColor(BorderSide.BOTTOM, new XSSFColor(new java.awt.Color(0, 0, 0)));
		styles.put(ForesightConstants.ORANGEHEADER, style);

		style = (XSSFCellStyle) wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFont(dataFont);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderColor(BorderSide.LEFT, new XSSFColor(new java.awt.Color(0, 0, 0)));
		style.setBorderColor(BorderSide.RIGHT, new XSSFColor(new java.awt.Color(0, 0, 0)));
		style.setBorderColor(BorderSide.TOP, new XSSFColor(new java.awt.Color(0, 0, 0)));
		style.setBorderColor(BorderSide.BOTTOM, new XSSFColor(new java.awt.Color(0, 0, 0)));
		styles.put(ForesightConstants.DATACELL_KEY, style);
		return styles;
	}

	private String generateExcelForExportPins(List<DataWrapper> dataWrappers) {
		logger.info("Going to generate excel file for Export pins ");
		String fileName = ForesightConstants.BLANK_STRING;
		String groupName = null;
		try {
			if (dataWrappers.get(0).getGroupName() != null)
				groupName = dataWrappers.get(0).getGroupName();
			String date = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_yyyy_MM_DD).format(new Date());
			fileName = groupName + ForesightConstants.UNDERSCORE + date + ForesightConstants.EXCEL_EXTENSION;
			if (dataWrappers != null && !dataWrappers.isEmpty()) {
				String path = System.getProperty(ForesightConstants.TOMCAT_PATH) + ForesightConstants.FORWARD_SLASH
						+ ConfigUtils.getString(ConfigEnum.PLANNED_SITE_REPORT_PATH.getValue());
				createPath(path);
				String excelFileName = path + fileName;
				SXSSFWorkbook workbook = new SXSSFWorkbook();
				Map<String, XSSFCellStyle> style = createStylesForPushPinReport(workbook);
				SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(groupName);
				int rowNum = 0;
				SXSSFRow row;
				Map<String, String> map = getMappingForGeographies();
				String[] firstRowData = { "Date", "Pin Name", "Pin Lat", "Pin Long", "Status", "Color", "Symbol",
						"Comments", map.get(ForesightConstants.GEOGRAPHY_L4), map.get(ForesightConstants.GEOGRAPHY_L3),
						map.get(ForesightConstants.GEOGRAPHY_L2), map.get(ForesightConstants.GEOGRAPHY_L1),
						"Additional Info" };
				SXSSFRow firstRow = (SXSSFRow) sheet.createRow(rowNum);
				ReportUtils.createRow(firstRow, style.get(ForesightConstants.ORANGEHEADER), false, sheet, firstRowData);
				firstRow.setHeightInPoints(25);
				rowNum++;
				for (DataWrapper dataWrapper : dataWrappers) {
					try {
						row = (SXSSFRow) sheet.createRow(rowNum);
						ReportUtils.createRow(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet,
								(dataWrapper.getModifiedTime() != null ? dataWrapper.getModifiedTime().toString()
										: ForesightConstants.HIPHEN),
								(dataWrapper.getPinName() != null ? dataWrapper.getPinName()
										: ForesightConstants.HIPHEN),
								(dataWrapper.getPinLatitude() != null ? dataWrapper.getPinLatitude().toString()
										: ForesightConstants.HIPHEN),
								(dataWrapper.getPinLongitude() != null ? dataWrapper.getPinLongitude().toString()
										: ForesightConstants.HIPHEN),
								(dataWrapper.getPinStatus() != null ? dataWrapper.getPinStatus()
										: ForesightConstants.HIPHEN),
								(dataWrapper.getColorCode() != null
										? StringEscapeUtils.unescapeHtml(dataWrapper.getColorCode())
										: ForesightConstants.HIPHEN),
								(dataWrapper.getImageName() != null ? dataWrapper.getImageName()
										: ForesightConstants.HIPHEN),
								(dataWrapper.getComments() != null && !(dataWrapper.getComments()
										.equalsIgnoreCase(ForesightConstants.BLANK_STRING)) ? dataWrapper.getComments()
												: ForesightConstants.HIPHEN),
								(dataWrapper.getGeographyL4() != null ? dataWrapper.getGeographyL4()
										: ForesightConstants.HIPHEN),
								(dataWrapper.getGeographyL3() != null ? dataWrapper.getGeographyL3()
										: ForesightConstants.HIPHEN),
								(dataWrapper.getGeographyL2() != null ? dataWrapper.getGeographyL2()
										: ForesightConstants.HIPHEN),
								(dataWrapper.getGeographyL1() != null ? dataWrapper.getGeographyL1()
										: ForesightConstants.HIPHEN),
								(dataWrapper.getAdditionalInfo() != null
										&& dataWrapper.getAdditionalInfo() != ForesightConstants.BLANK_STRING
												? dataWrapper.getAdditionalInfo()
												: ForesightConstants.HIPHEN));

						if (dataWrapper.getComments() != null) {
							int length = dataWrapper.getComments().length();
							int mod = (int) length / 50;
							row.setHeightInPoints(15 * (mod + 1));
						}

						XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
						Color cellColor = Color.decode(StringEscapeUtils.unescapeHtml(dataWrapper.getColorCode()));
						XSSFColor color = new XSSFColor(cellColor);
						cellStyle.setFillForegroundColor(color);
						cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
						cellStyle.setBorderTop((short) 1);
						cellStyle.setBorderBottom((short) 1);
						cellStyle.setBorderLeft((short) 1);
						cellStyle.setBorderRight((short) 1);
						SXSSFCell cell1 = (SXSSFCell) row.getCell(5);
						cell1.setCellStyle(cellStyle);

						row.setHeightInPoints(20);
						setColumnsWidth(sheet);
						rowNum++;
					} catch (Exception e) {
						logger.error("Error in writing export pins data to excel file Message {}",
								ExceptionUtils.getStackTrace(e));
					}

				}
				setColumnsWidth(sheet);
				FileOutputStream fileOut = new FileOutputStream(excelFileName);
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
				logger.info("Excel FIle generated successfully.");
				return new File(excelFileName).getName();
			}

		} catch (Exception e) {
			logger.error("Error in generating excel report for export pins Message {}", e.getMessage());
		}
		return fileName;
	}

	private void setColumnsWidth(SXSSFSheet sheet) {
		if (sheet != null) {
			logger.info("going to set Column Width for Sheet {}", sheet.getSheetName());
			for (int i = 0; i <= 13; i++) {
				try {
					sheet.setColumnWidth(i, 4000);
				} catch (Exception e) {
					logger.error("error in setting Column Width for RanDetail Sheet Column number {}", i);
				}
			}
		} else {
			logger.error("column width not set due to sheet null");
		}

	}

	@Override
	public Long getPinGroupListCount(Integer userid) {
		logger.info("Going to get counts of pins for userid {}", userid);
		Long pinCounts = 0L;
		try {
			pinCounts = iPushPinDao.getPinGroupListCount(userid, null);
 				return pinCounts;
				
		} catch (Exception exception) {
			logger.error("Error in getting counts of pins Message {}", exception.getMessage());
			throw new RestException("unable to get pins.");
		}
	}

	@Override
	public Long getPinGroupListCountBySearchterm(Integer userid, String searchTerm) {
		logger.info("Going to get list of pins for userid {} searchTerm {}", userid, searchTerm);
		Long pinCounts = 0L;
		try {
			pinCounts = iPushPinDao.getPinGroupListCount(userid, searchTerm);
				return pinCounts;
		} catch (Exception exception) {
			logger.error("Error in getting counts of pins Message {}", exception.getMessage());
			throw new RestException("unable to get pins.");
		}
	}

	@Override
	public List<DataWrapper> getPinGroupDetailsBySearchTerm(Integer userid, Integer lowerLimit, Integer upperLimit,
			String groupName) {
		try {
			logger.info("Going to get pin details for search term {}", groupName);
			return iPushPinDao.getListOfPinGroupsBySearchTerm(userid, lowerLimit, upperLimit, groupName);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public List<DataWrapper> getPinGroupData(Integer userid, String groupName) {
		try {
			logger.info("Going to get pin group data for groupName {} ,userid {}", groupName, userid);
			List<PushPin> listOfPins = iPushPinDao.getPinGroupData(userid, groupName, null, null);
			return getPinsDataInWrapper(listOfPins);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public List<DataWrapper> searchPinDetails(Integer userid, List<String> groupName, String pinName) {
		try {
			logger.info("Going to get pin data for pinName {}, groupName {} ,userid {}", pinName, groupName, userid);
			return iPushPinDao.searchPinDetails(userid, groupName, pinName);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public List<String> getPinStatusList(Integer userid) {
		try {
			logger.info("Going to get pin status ");
			return iPushPinDao.getPinStatusList(userid);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public List<DataWrapper> getPinStatusByGroupName(Integer userid, String groupName) {
		try {
			logger.info("Going to get pin status ");
			return iPushPinDao.getPinStatusByGroupName(userid, groupName);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public List<String> getPinGroupNameList(Integer userid) {
		try {
			logger.info("Going to get group name.");
			return iPushPinDao.getPinGroupNameList(userid);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public byte[] getFileByPath(String fileName) {
		logger.info("Going to get file by name " + fileName);
		byte[] b = null;
		byte[] toReturn = null;
		String filePath = System.getProperty(ForesightConstants.TOMCAT_PATH) + ForesightConstants.FORWARD_SLASH
				+ ConfigUtils.getString(ConfigEnum.PLANNED_SITE_REPORT_PATH.getValue());
		File file = new File(filePath + fileName);
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream();FileInputStream fis = new FileInputStream(file)) {
			b = new byte[(int) file.length()];
			fis.read(b);
			baos.write(b);
			toReturn = baos.toByteArray();
			if(file.delete()) {
				logger.info("File Deleted Successfully.");
			if(new File(filePath).delete()) {
				logger.info("File path Deleted Successfully.");
			}
			}
			return toReturn;
		} catch (Exception e) {
			logger.error("Error in Getting file Message{} Exception {} ", e.getMessage(),
					ExceptionUtils.getStackTrace(e));
		}
		return toReturn;
	}

	private Map<String, String> getMappingForGeographies() {
		Map<String, String> map = new HashMap<>();
		try {
			map.put(ForesightConstants.GEOGRAPHY_L1, iSystemConfigurationDao
					.getValueByNameAndType(ForesightConstants.GEOGRAPHY, ForesightConstants.GEOGRAPHY_L1));
			map.put(ForesightConstants.GEOGRAPHY_L2, iSystemConfigurationDao
					.getValueByNameAndType(ForesightConstants.GEOGRAPHY, ForesightConstants.GEOGRAPHY_L2));
			map.put(ForesightConstants.GEOGRAPHY_L3, iSystemConfigurationDao
					.getValueByNameAndType(ForesightConstants.GEOGRAPHY, ForesightConstants.GEOGRAPHY_L3));
			map.put(ForesightConstants.GEOGRAPHY_L4, iSystemConfigurationDao
					.getValueByNameAndType(ForesightConstants.GEOGRAPHY, ForesightConstants.GEOGRAPHY_L4));
		} catch (Exception exception) {
			logger.error("Exception in getting Mapping for sector Information {} ",
					ExceptionUtils.getStackTrace(exception));

		}
		return map;
	}

	@Override
	public List<String> getPinNameByGroupName(Integer userid, List<String> groupName) {
		try {
			logger.info("Going to get pin name for group name {} ,userid {}", groupName, userid);
			return iPushPinDao.getPinNameByGroupName(userid, groupName);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	@Transactional
	public byte[] exportPinsInKML(Integer userId, String groupName) {
		logger.info("going to export pins in kml for userId {},groupName {}", userId, groupName);
		byte[] kmlContent = null;
		if (groupName != null) {
			List<PushPin> listOfPins = iPushPinDao.getPinGroupData(userId, groupName, null, null);
			if (listOfPins != null) {
			     kmlContent = generateKMLFile(listOfPins, groupName);
				if (kmlContent != null) {
					return kmlContent;
				} else {
					throw new RestException("Something  went wrong");
				}
			} else {
				throw new RestException("Unable to generate KML file");
			}
		}
		return kmlContent;
	}

	@Override
	public String getPushPinFileName(String groupName) {
		if (groupName != null) {
			return groupName + KMLFileConstant.KML_FILE_EXTENSION;
		} else {
			throw new RestException("Invalid Groupname.");
		}
	}

	private byte[] generateKMLFile(List<PushPin> listOfPins, String groupName) {
		
		logger.info("generate kml file for groupName {} no of pins {}", groupName, listOfPins.size());
		try {
			Map<String, String> geographiesName = getMappingForGeographies();
			List<Map<String, Object>> placemarkList = new ArrayList<>();

			for (PushPin pushPin : listOfPins) {
				try {
					if (pushPin != null && pushPin.getLatitude() != null && pushPin.getLongitude() != null) {

						Map<String, Object> placemarkMap = new HashMap<>();

						if (pushPin.getGeographyL4() != null) {
							placemarkMap.put(geographiesName.get(ForesightConstants.GEOGRAPHY_L4),pushPin.getGeographyL4().getDisplayName() != null ? pushPin.getGeographyL4().getDisplayName()	: Symbol.HYPHEN_STRING);

							if (pushPin.getGeographyL4().getGeographyL3() != null) {
								placemarkMap.put(geographiesName.get(ForesightConstants.GEOGRAPHY_L3),pushPin.getGeographyL4().getGeographyL3().getDisplayName() != null ? pushPin.getGeographyL4().getGeographyL3().getDisplayName() : Symbol.HYPHEN_STRING);

								if (pushPin.getGeographyL4().getGeographyL3().getGeographyL2() != null) {
									placemarkMap.put(geographiesName.get(ForesightConstants.GEOGRAPHY_L2),pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getDisplayName() != null	? pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getDisplayName() : Symbol.HYPHEN_STRING);

									if (pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1() != null)
										placemarkMap.put(geographiesName.get(ForesightConstants.GEOGRAPHY_L1),pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1().getDisplayName() != null	? pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1().getDisplayName()	: Symbol.HYPHEN_STRING);
								}
							}
						} else {
							logger.error("No Geography found for latitude {},longitude {}", pushPin.getLatitude(),pushPin.getLongitude());
						}
						placemarkMap.put(KMLFileConstant.PLACEMARK_NAME,pushPin.getPinName() != null ? pushPin.getPinName() : Symbol.HYPHEN_STRING);

						placemarkMap.put(ForesightConstants.DATE_CAMEL,pushPin.getModifiedTime() != null ? pushPin.getModifiedTime().toString()	: Symbol.HYPHEN_STRING);

						placemarkMap.put(InfraConstants.COMMENTS_CAMEL,pushPin.getComments() != null && !(pushPin.getComments().equalsIgnoreCase(Symbol.EMPTY_STRING)) ? pushPin.getComments()	: Symbol.HYPHEN_STRING);

						placemarkMap.put(ForesightConstants.STATUS_CAMEL,pushPin.getPinStatus() != null ? pushPin.getPinStatus() : Symbol.HYPHEN_STRING);

						placemarkMap.put(InfraConstants.ADDITIONAL_INFO,pushPin.getAdditionalInfo() != null	&& !(pushPin.getAdditionalInfo().equalsIgnoreCase(Symbol.EMPTY_STRING))	? pushPin.getAdditionalInfo() : Symbol.HYPHEN_STRING);

						List<Double> point = new ArrayList<>();
						point.add(pushPin.getLongitude());
						point.add(pushPin.getLatitude());

						placemarkMap.put(KMLFileConstant.COORDINATES, point);

						placemarkList.add(placemarkMap);
					} else {
						logger.error("Invalid pushpin {}", pushPin.toString());
					}
				} catch (Exception exception) {
					logger.error("Exception while parsing pin {} Exception {}", pushPin.getPinName(),
							ExceptionUtils.getStackTrace(exception));
				}
			}

			return KMLGenerator.generateKMLForPoint(groupName, placemarkList).toByteArray();

		} catch (Exception exception) {
			logger.error("Exception inside generateKMLFile due to {}", ExceptionUtils.getStackTrace(exception));
		}
		return null;
	}

	@Override
	public List<DataWrapper> getPinsDetailsBySearchTerm(Integer userid, Integer lowerLimit, Integer upperLimit,
			String pinName, String groupName) {
		try {
			logger.info("Going to get pin details for pinName {}", pinName);
			return iPushPinDao.getListOfPinBySearchTerm(userid, lowerLimit, upperLimit, pinName, groupName);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public Long getCountsOfPinsByGroupName(Integer userid, String groupName) {
		logger.info("Going to get counts of pins for userid {}", userid);
		Long pinCounts = 0L;
		try {
			pinCounts = iPushPinDao.getCountsOfPinsByGroupName(userid, groupName, null);
				return pinCounts;
		} catch (Exception exception) {
			logger.error("Error in getting counts of pins Message {}", exception.getMessage());
			throw new RestException("unable to get pins.");
		}
	}

	@Override
	public Long getCountsOfPinsByGroupName(Integer userid, String groupName, String searchTerm) {
		logger.info("Going to get list of pins for userid {} searchTerm {}", userid, searchTerm);
		Long pinCounts = 0L;
		try {
			pinCounts = iPushPinDao.getCountsOfPinsByGroupName(userid, groupName, searchTerm);
				return pinCounts;
		} catch (Exception exception) {
			logger.error("Error in getting counts of pins Message {}", exception.getMessage());
			throw new RestException("unable to get pins.");
		}
	}

	private List<DataWrapper> getPinsDataInWrapper(List<PushPin> listOfPins) {
		logger.info("Going to get pins data in wrapper.");
		if (listOfPins != null && !listOfPins.isEmpty()) {
			List<DataWrapper> listOfWrapper = new ArrayList<>();
			DataWrapper dataWrapper = null;
			for (PushPin pushPin : listOfPins) {
				try {
					if (pushPin != null) {
						dataWrapper = new DataWrapper();
						if (pushPin.getId() != null)
							dataWrapper.setId(pushPin.getId());
						dataWrapper.setPinName(pushPin.getPinName() != null ? pushPin.getPinName() : Symbol.HYPHEN_STRING);
						if (pushPin.getLatitude() != null)
							dataWrapper.setPinLatitude(pushPin.getLatitude());
						if (pushPin.getLongitude() != null)
							dataWrapper.setPinLongitude(pushPin.getLongitude());
						dataWrapper.setComments(pushPin.getComments() != null ? pushPin.getComments() : Symbol.HYPHEN_STRING);
						dataWrapper.setGroupName(pushPin.getGroupName() != null ? pushPin.getGroupName() : Symbol.HYPHEN_STRING);
						if (pushPin.getCreatedTime() != null)
							dataWrapper.setCreatedTime(pushPin.getCreatedTime());
						if (pushPin.getModifiedTime() != null)
							dataWrapper.setModifiedTime(pushPin.getModifiedTime());
						dataWrapper.setPinStatus(pushPin.getPinStatus() != null ? pushPin.getPinStatus() : Symbol.HYPHEN_STRING);
						dataWrapper.setAdditionalInfo(pushPin.getAdditionalInfo() != null ? pushPin.getAdditionalInfo()	: Symbol.HYPHEN_STRING);
						if (pushPin.getZoomLevel() != null)
							dataWrapper.setZoomLevel(pushPin.getZoomLevel());
						dataWrapper.setColorCode(pushPin.getColorCode() != null ? StringEscapeUtils.unescapeHtml(pushPin.getColorCode()) : Symbol.HYPHEN_STRING);
						dataWrapper.setImageName(pushPin.getImageName() != null ? pushPin.getImageName() : Symbol.HYPHEN_STRING);
						dataWrapper.setLabelProperty(pushPin.getLabelProperty() != null ? pushPin.getLabelProperty() : Symbol.HYPHEN_STRING);
						if (pushPin.getGeographyL4() != null) {
							dataWrapper.setGeographyL4(pushPin.getGeographyL4().getDisplayName() != null ? pushPin.getGeographyL4().getDisplayName() : Symbol.HYPHEN_STRING);
							if (pushPin.getGeographyL4().getGeographyL3() != null) {
								dataWrapper.setGeographyL3(pushPin.getGeographyL4().getGeographyL3().getDisplayName() != null ? pushPin.getGeographyL4().getGeographyL3().getDisplayName()	: Symbol.HYPHEN_STRING);
								if (pushPin.getGeographyL4().getGeographyL3().getGeographyL2() != null) {
									dataWrapper.setGeographyL2(pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getDisplayName() != null	? pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getDisplayName() : Symbol.HYPHEN_STRING);
									if (pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1() != null)
										dataWrapper.setGeographyL1(pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1().getDisplayName() != null	? pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1().getDisplayName() : Symbol.HYPHEN_STRING);
								}
							}
						}
						listOfWrapper.add(dataWrapper);
						dataWrapper = null;
					}
				} catch (Exception exception) {
					logger.error("Error while getting data in wrapper for pin {},Exception {}", pushPin.getPinName(),Utils.getStackTrace(exception));
				}

			}
			return listOfWrapper;
		} else {
			logger.error("No pins found in list.");
		}
		return null;
	}

	@Override
	public List<DataWrapper> getPinGroupData(Integer userid, String groupName, Integer lowerLimit, Integer upperLimit) {
		try {
			logger.info("Going to get pin group data for groupName {} ,userid {}", groupName, userid);
			List<PushPin> listOfPins = iPushPinDao.getPinGroupData(userid, groupName, lowerLimit, upperLimit);
			return getPinsDataInWrapper(listOfPins);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public PushPin getPinDetailsForGroupName(Integer userid, String groupName, Integer id) {
		try {
			logger.info("Going to get pin data for groupName {} ,userid {}", groupName, userid);
			return iPushPinDao.getPinDetailsForGroupName(userid, groupName, id);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@Override
	public boolean isGroupExistForUser(String groupName, Integer userid) {
		try {
			logger.info("Going to check if groupname {} exist for user {} ", groupName, userid);
			String specialCharacters = "!#$%&'()*+,-./:;<=>?@[]^`{|}~";
			int specialCharacterCount = 0;
			if(groupName != null) {
			for (int i = 0; i < groupName.length(); i++) {
				boolean isMatches = specialCharacters.contains(Character.toString(groupName.charAt(i)));
				if (isMatches)
					specialCharacterCount++;
			}
			if (specialCharacterCount > 0) {
				throw new RestException("File Name contains Special Characters.");
			} else {

				if ( userid != null) {
					if(!(groupName.equalsIgnoreCase(ForesightConstants.BLANK_STRING) && !(groupName.matches(specialCharacters)))) {
					return iPushPinDao.isGroupExistForUser(groupName, userid);
					}
				}
				else
					throw new RestException("Invalid File Name.");

			}
		}
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
		return false;
	}
	  private List<String> getSheetNameFromSystemConfiguration(String name,String type) {
	      logger.info("get sheet parameters from systemconfiguration name : {} type : {}",name,type);
	      List<String> sheetName=new ArrayList<>();
	      try {
	      SystemConfiguration systemConfiguration= iSystemConfigurationDao.getSystemConfigurationDetailForReport(name, type);
	      String value=systemConfiguration.getValue();
	      value=value.replaceAll("[\\[\\](){}]", ForesightConstants.BLANK_STRING);
	      value=value.replaceAll("\"", ForesightConstants.BLANK_STRING);
	      if(value != null) {
	      sheetName=Arrays.asList(value.split(ForesightConstants.COMMA));
	      logger.info("sheet name for report : {}",sheetName);
	      return sheetName;
	      }else {
	          logger.info("no value is found for name : {} and type : {}",name,type);
	      }
	     
	      }catch(Exception exception) {
	          logger.error("Error in getting Sheet parameters .Exception {}", ExceptionUtils.getStackTrace(exception));
	      }
	    
	    return sheetName;
	    
	   }
}
