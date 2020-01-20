package com.inn.foresight.module.nv.reportgeneration.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.reportgeneration.constants.NVReportGenerationConstants;


public class NVReportGenerationUtils {

	private static Logger logger = LogManager.getLogger(NVReportGenerationUtils.class);

	public static HttpResponse sendGetRequestWithoutTimeOut(String uri) {
		try(CloseableHttpClient httpClient = HttpClients.createDefault();) {
			HttpGet httpGet = new HttpGet(uri);
			logger.info("Going to hit URL : {}",uri);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new RestException("Failed with HTTP error code : " + statusCode);
			}else{
				logger.info("Return Response Code : {}",statusCode);
				return response;
			} 
		} catch (Exception e) {
			throw new RestException(NVReportGenerationConstants.EXCEPTION_ON_CONNECTION);
		} 
	}



	/**
	 * Write response.
	 *
	 * @param resp the resp
	 * @param fileStream the file stream
	 * @return the http servlet response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static HttpServletResponse writeResponse(HttpServletResponse resp,
			InputStream fileStream) throws IOException {
		ServletOutputStream outStream = null;
		try {
			int inByte;
			resp.setStatus(NVReportGenerationConstants.INDEX_TW0_HUNDRED);
			outStream = resp.getOutputStream();
			while ((inByte = fileStream.read()) != -1) {
				outStream.write(inByte);
			}
		} catch (Exception e) {
			logger.error("getting error on writing data to file");
		} finally {
			try {
				fileStream.close();
				if(outStream!=null) {
					outStream.close();
				}
			} catch (Exception e2) {
				logger.error("Something went wrong during response writing {} ",e2.getMessage());
			}
		}
		return resp;
	}



	/**
	 * Write response to excel file.
	 *
	 * Used in reports.jsp
	 * @param resp the resp
	 * @param fileStream the file stream
	 * @param fileName the file name
	 * @return the http servlet response
	 * @throws RestException the rest exception
	 */

	public static HttpServletResponse writeResponseToExcelFile(HttpServletResponse resp, InputStream fileStream, String contentDisposition,String contentType) {
		try {
			logger.debug("going to write this response =  {}", contentDisposition);
			writeResponse(resp, fileStream);
			resp.setHeader(NVReportGenerationConstants.CONTENT_TYPE, contentType);
			resp.setHeader("Content-Disposition", contentDisposition);
		} catch (Exception e) {
			logger.error("getting error on writing data to file");
			throw new RestException("Error on converting into json");
		}
		return resp;
	}

	public static void main(String[] args) throws IOException {
		HttpResponse is = sendGetRequestWithoutTimeOut(
				"http://192.168.4.235:9008/foresight/rest/NVReport/getNVReport?rowkey=46");
		try (FileOutputStream fos = new FileOutputStream(createDestinationFile("/home/ist/testing.csv"))) {
			byte[] bytesArray = IOUtils.toByteArray(is.getEntity().getContent());
			fos.write(bytesArray);
		}
	}


	private static File createDestinationFile(String destinatioFile) throws IOException {
		File modifiedQmdlFile = new File(destinatioFile);
		Path path = Paths.get(destinatioFile);
		if (modifiedQmdlFile.exists()) {
			Files.delete(path);
			logger.info("Old file removed SuccessFully ");
		}
		if(modifiedQmdlFile.createNewFile()){
			return modifiedQmdlFile;
		}
		throw new IOException("Error in Creating file");
	}

}
