package com.inn.foresight.module.nv.report.utils;

import com.inn.commons.encoder.AESUtils;
import com.inn.commons.io.IOUtils;
import com.inn.foresight.core.generic.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MergePDF {

	private static Logger logger = LogManager.getLogger(MergePDF.class);

	public static void mergeAllFilesInFolder(String folderPath, String destinationFilePath) {
		try {
			PDFMergerUtility mergePdf = new PDFMergerUtility();
			File folder = new File(folderPath);
			File[] filesInFolder;
			filesInFolder = folder.listFiles();
			for (File string : filesInFolder) {
				mergePdf.addSource(string);
			}
			mergePdf.setDestinationFileName(destinationFilePath);

			mergePdf.mergeDocuments(null);
		} catch (Exception e) {
			logger.error("Error while merging files {}",e);
		}

	}

	public static void mergeFiles(String destinationFile, File... files) {
		try {
			logger.info("going to merge files: destinationFile==>" + destinationFile + " and files : " + files);
			PDFMergerUtility mergePdf = new PDFMergerUtility();
			for (File string : files) {
				mergePdf.addSource(string);
			}
			mergePdf.setDestinationFileName(destinationFile);
			mergePdf.mergeDocuments(null);
		} catch (Exception e) {
			logger.info("Exception on merging files: " + Utils.getStackTrace(e));
		}

	}

	public static void mergeFiles(String destinationFile, List<File> files) {
		try {
			logger.info("going to merge files: destinationFile==>" + destinationFile + " and files : " + files);
			PDFMergerUtility mergePdf = new PDFMergerUtility();
			for (File file : files) {
				if(file !=null) {
                    mergePdf.addSource(file);
                }
			}
			getPushBackSize();
			mergePdf.setDestinationFileName(destinationFile);
			mergePdf.mergeDocuments(null);
		} catch (Exception e) {
			logger.error("Exception on merging files: " + Utils.getStackTrace(e));
		}
	}

	private static void getPushBackSize() {
		
		try {
			logger.debug("pushBackSize {} ", System.getProperty("org.apache.pdfbox.baseParser.pushBackSize"));
		} catch (Exception e) {
			logger.error("Unable to find the pushback Size {} ",e.getMessage());
		}
		
	}

	public static void main(String[] args) {
		try {
			addPage(new File("/home/ist/Desktop/SSVT/UPDATED/WO-SSVT-422019-TDD40-2_283507.pdf"), new File("/home/ist/Desktop/dest.pdf"));
		} catch (IOException e) {
			logger.error("Error inside maon method {} ",e.getMessage());
		}
	}

	public static File addPage(File sourcePdf, File destinationPdf) throws IOException {
		PDDocument sourceDoc = PDDocument.load(sourcePdf);
		System.out.println("Number of source pages: " + sourceDoc.getPages().getCount());
		PDPage firstPage = sourceDoc.getPage(0);
		PDDocument destDoc = PDDocument.load(destinationPdf);
		System.out.println("Number of destination pages: " + destDoc.getPages().getCount());
		PDPage destPage = destDoc.getPage(0);
		sourceDoc.getDocumentCatalog().getPages().insertAfter(destPage, firstPage);
		sourceDoc.save(destinationPdf);
		sourceDoc.close();
		return destinationPdf;
	}

}
