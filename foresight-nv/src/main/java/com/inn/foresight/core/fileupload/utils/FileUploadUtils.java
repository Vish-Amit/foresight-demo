package com.inn.foresight.core.fileupload.utils;

public class FileUploadUtils {
	
	private FileUploadUtils()
	{
		super();
	}
	public static final String ATTACHMENT_SERVICE_DOWNLOAD_HDFS_URL = "AttachmentData/downloadFromHDFS?filePath=";
	
	public static String getFilePath(String module)
	{	
	 	String modulepath="";
		if(module!=null  && !module.isEmpty())	
		{			
		  modulepath=module.replace(' ', '_');		
		}				
	   return modulepath;	
	}
}
