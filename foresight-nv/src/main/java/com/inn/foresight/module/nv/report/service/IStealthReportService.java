package com.inn.foresight.module.nv.report.service;

import java.io.File;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.protobuf.ServiceException;

public interface IStealthReportService {

	File getPDFReportForStealthWO(String workorderId, List<Integer> taskIdList, Long startDate, Long endDate,Integer analyticsRepositoryId) throws ServiceException;

	String processWOCsvDump(String workorderId);
	
	Response getFileForDownloadFromHDFS(String filePath);
	
	String processStealthWOPdfForTaskId(String workorderId, List<Integer> taskIdList, Long startDate, Long endDate);
	
	Response execute(String json);
	
}
