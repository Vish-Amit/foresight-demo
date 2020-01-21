package com.inn.foresight.core.attachment.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.core.generic.exceptions.application.RestException;

public class UnzipUtility {

	private static final int BUFFER_SIZE = 4096;

	private static Logger logger = LogManager.getLogger(UnzipUtility.class);

	public static void unzip(String zipFilePath, String destDirectory)
	 {

		logger.info("zipFilePath " + zipFilePath);
		logger.info("destDirectory " + destDirectory);

		try(ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));) {
			File destDir = new File(destDirectory);

			if (!destDir.exists()) {
				destDir.mkdirs();
			}

			ZipEntry entry = zipIn.getNextEntry();
			
			if(entry==null){
				throw new RestException("");
			}

			while (entry != null) {

				logger.info(entry.getName());
				
				String filePath = destDirectory + File.separator+ entry.getName();

				if (!entry.isDirectory()) {
					extractFile(zipIn, filePath);
				} else {
					logger.info("else create Directory");
					File dir = new File(filePath);
					dir.mkdirs();
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}

		} catch (Exception e) {
			logger.error("exception while extracting zip file "
					+ e.getMessage());
			throw new RestException("exception while extracting zip file " + e.getMessage());
		}
		

	}

	private static void extractFile(ZipInputStream zipIn, String filePath)
			throws IOException {
		logger.info("filePath " + filePath);
		try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))){
		
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		}
	}
	
	public static void createZipFileFromFileList(List<String> sourceFiles,String destinationFileName) throws IOException{
	   try
       {
               byte[] buffer = new byte[1024];
                try(FileOutputStream fout = new FileOutputStream(destinationFileName);ZipOutputStream zout = new ZipOutputStream(fout)) {
                for(String fileName: sourceFiles)
                {
                       try(FileInputStream fin = new FileInputStream(fileName)){
                       zout.putNextEntry(new ZipEntry(fileName.lastIndexOf('/')>=0?fileName.substring(fileName.lastIndexOf('/')+1):fileName));
                       int length;
                       while((length = fin.read(buffer)) > 0)
                       {
                          zout.write(buffer, 0, length);
                       }
                        zout.closeEntry();
                }
                }
                }
                 logger.info("Zip file has been created!");
      
       }
       catch(IOException ioe)
       {
               throw new IOException(ioe);
       }
      
	}

}