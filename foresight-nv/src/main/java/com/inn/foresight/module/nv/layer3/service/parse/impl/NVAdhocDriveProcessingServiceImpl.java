package com.inn.foresight.module.nv.layer3.service.parse.impl;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.hadoop.hbase.client.Put;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;
import com.inn.foresight.module.nv.layer3.service.parse.INVAdhocDriveProcessingService;
import com.inn.foresight.module.nv.layer3.service.parse.INVCsvProcessService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.service.IWOFileDetailService;

@Service("NVAdhocDriveProcessingServiceImpl")
public class NVAdhocDriveProcessingServiceImpl implements INVAdhocDriveProcessingService {

	private static final  Logger logger = LogManager.getLogger(NVAdhocDriveProcessingServiceImpl.class);
	@Autowired
	private IWOFileDetailService woFileDetailService;
	@Autowired
	private INVCsvProcessService nvCsvService;
	@Autowired
	private INVLayer3HDFSDao nvHdfsDao;

	
	


	@Override
	public List<NVL3CsvDataWrapper> processAdhocFileForReport(Integer woId,Layer3SummaryWrapper aggrigateWrapperData ) throws IOException {
		try {
			logger.info("Going to process file for woId {} ", woId);
			List<WOFileDetail> woList = woFileDetailService.getFileDetailByWorkOrderId(woId);
			if (woList == null || woList.isEmpty()) {
				logger.info("No file found for Process");
				return new ArrayList<>();
			}
			Map<String, List<WOFileDetail>> workOrderFileMap = getWorkOrderMapfromList(woList);
			
			return processUnProcessFiles(workOrderFileMap,aggrigateWrapperData);
			
		} catch (RestException e) {
			logger.warn("Getting Error {}  ", Utils.getStackTrace(e));
		} catch (IOException e) {
			throw new IOException(e);
		}
		return new ArrayList<>();
	}
	
	
	private List<NVL3CsvDataWrapper> processUnProcessFiles(Map<String, List<WOFileDetail>> workOrderFileMap, Layer3SummaryWrapper aggrigateWrapperData)
			throws IOException {
		
		List<NVL3CsvDataWrapper> csvDataWrapperList=new ArrayList<>();
		for (Entry<String, List<WOFileDetail>> woFileDetail : workOrderFileMap.entrySet()) {
			try {
				processWoWiseFiles(woFileDetail,csvDataWrapperList,aggrigateWrapperData);
			} catch (Exception e) {
				logger.error("Getting Error in processing Data for Key  {}  {}", woFileDetail.getKey(),Utils.getStackTrace(e));
			}
		}
		logger.info("Getting aggrigate Wrapper {}",new Gson().toJson(aggrigateWrapperData));
		logger.info("All files Processed SuccessFully   ");
		
		return csvDataWrapperList;
	}
	
	
	private void processWoWiseFiles(Entry<String, List<WOFileDetail>> woFileDetail,List<NVL3CsvDataWrapper> woParsedMap, Layer3SummaryWrapper aggrigateWrapperData) throws IOException {
		List<WOFileDetail> woFileList = woFileDetail.getValue();
		processZipFileRecipeWise(woFileDetail, woParsedMap, 
				aggrigateWrapperData, woFileList);
		
	}
	

	private void processZipFileRecipeWise(Entry<String, List<WOFileDetail>> woFileDetail,
			List<NVL3CsvDataWrapper> woParsedMap, 
			Layer3SummaryWrapper aggrigateWrapperData, List<WOFileDetail> woFileList) {
		List<String> tempFiles = new ArrayList<>();
		for (WOFileDetail woFile : woFileList) {
			try {
				processTypeWiseFile(woParsedMap, woFile,aggrigateWrapperData,tempFiles);
			} catch (Exception e) {
				logger.error("Error in processing file for key {}  file {}   error {}", woFileDetail.getKey(),
						woFile.getFileName(), Utils.getStackTrace(e));
			}
		}
		if (QMDLConstant.TRUE.equalsIgnoreCase(ConfigUtils.getString(QMDLConstant.DELETE_LAYER3_TEMP_FILES))) {
			deleteTempFilesFromLocal(tempFiles);
		}
	}
	
	
	private Map<String, List<WOFileDetail>> getWorkOrderMapfromList(List<WOFileDetail> woList) {
		Map<String, List<WOFileDetail>> workOrderMap = new HashMap<>();
		for (WOFileDetail woFile : woList) {
			if (isValidFileEntry(woFile)) {
				logger.info("Going to process {}   ",woFile.getFileName());
				String fileKey=getKeyFromFileName(woFile);
				if (workOrderMap.containsKey(fileKey)) {
					workOrderMap.get(fileKey).add(woFile);
				} else {
					List<WOFileDetail> list = new ArrayList<>();
					list.add(woFile);
					workOrderMap.put(fileKey, list);
				}
			}
		}
		return workOrderMap;
	}
	

	private void deleteTempFilesFromLocal(List<String> tempFiles) {
		for(String tempFilePath:tempFiles) {
			if(!StringUtils.isBlank(tempFilePath)) {
				File tempFile = new File(tempFilePath);
				if(tempFile.exists()) {
					boolean delete = tempFile.delete();
					logger.info("File deleted {}",delete);
				}
			}
		}
	}


	private String getKeyFromFileName(WOFileDetail woFile) {
		 String[] fileNameSplit = woFile.getFileName().split("_");
		return NVLayer3Utils.getRowkeyFromWorkOrderId(woFile.getWoRecipeMapping().getGenericWorkorder().getId())+fileNameSplit[QMDLConstant.OPERATOR_NAME]+NVLayer3Utils.getRowkeyFromWorkOrderId(woFile.getWoRecipeMapping().getId());
	}

	private boolean isValidFileEntry(WOFileDetail woFile) {
		return woFile.getWoRecipeMapping()!=null&&woFile.getFileName()!=null &&woFile.getFiletype()!=null&&woFile.getFilePath()!=null;
	}

	
	private void processTypeWiseFile(List<NVL3CsvDataWrapper> woParsedMap, WOFileDetail woFileDetail,
			Layer3SummaryWrapper aggrigateWrapperData, List<String> tempFiles) throws IOException  {
		String file = copyFileFromHdfsToLocal(woFileDetail);
		tempFiles.add(file);
		ZipFile zipFile = null;
		List<String> processedFiles=new ArrayList<>();
		
		try {
			zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				String fileName = entry.getName();
				logger.info("Going to process file {} ", fileName);
				if (fileName.contains(QMDLConstant.CSV_FILE_TYPE)) {
					String localfilepath=ConfigUtils.getString(NVConfigUtil.QMDL_TEMP_PATH);
					List<NVL3CsvDataWrapper> csvDataHolderList = nvCsvService.processCsvFileFromZip(zipFile, entry, aggrigateWrapperData, new HashMap<String, Put>(), processedFiles, localfilepath);
					
					for(NVL3CsvDataWrapper wrapper:csvDataHolderList){
						if(wrapper.getTimeStamp()==null || wrapper.getLat()==null|| wrapper.getLon()==null){
							continue;
						}
						addPingValuesToAggrigateWrapper(aggrigateWrapperData,wrapper);	
					}
					woParsedMap.addAll(csvDataHolderList);
					
				}
			}
		} finally {
			if (zipFile != null) {
				zipFile.close();
			}
		}
	}
	
	private String copyFileFromHdfsToLocal(WOFileDetail woFileDetail)  {
		try {
			return nvHdfsDao.copyFileFromHdfsToLocalPath(woFileDetail.getFilePath(), ConfigUtils.getString(NVConfigUtil.QMDL_TEMP_PATH), woFileDetail.getFileName());
		} catch (DaoException e) {
			logger.error("Error inside the method {}",e.getMessage());
		}
		return null;
	}
	private void addPingValuesToAggrigateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			NVL3CsvDataWrapper wrapper) {
		aggrigateWrapperData.setPingPacketLoss(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getPingPacketLoss(), wrapper.getPingPacketLoss()));
		aggrigateWrapperData.setDlThroughPutAdhoc(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getDlThroughPutAdhoc(), wrapper.getDlThroughPut()));
		aggrigateWrapperData.setUlThroughPutAdhoc(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getUlThroughPutAdhoc(), wrapper.getUlThroughPut()));
		aggrigateWrapperData.setLatencyAdhoc(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getLatencyAdhoc(),wrapper.getLatency()));
		aggrigateWrapperData.setWptDns(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getWptDns(),wrapper.getWptDns()));
		aggrigateWrapperData.setWptUrl(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getWptUrl(),wrapper.getWptUrl() ));	
		setBufferPercentageForAdhocReport(wrapper, aggrigateWrapperData);
		aggrigateWrapperData.setYoutubeThroughtPut(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getYoutubeThroughtPut(),wrapper.getYoutubeThroughPut()));
		
	}

	private static void setBufferPercentageForAdhocReport(NVL3CsvDataWrapper csvDataHolder,
			Layer3SummaryWrapper aggrigateWrapperData) {
		if(csvDataHolder.getTotalBufferTime()!=null && csvDataHolder.getVideoDuration()!=null && csvDataHolder.getVideoDuration()>0){
			try {
				aggrigateWrapperData.setYoutubeBufferTime(NVLayer3Utils.roundOffDouble(QMDLConstant.DECIMAL_PATTERN_2DIGIT, ((csvDataHolder.getTotalBufferTime()/csvDataHolder.getVideoDuration())*100)));
			} catch (ParseException e) {
				logger.error("Error in getting buffer time ratio {}",Utils.getStackTrace(e));
			}
		}
	}
	
}
