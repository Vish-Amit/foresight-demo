package com.inn.foresight.indexlucene.hdfs;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;

import com.inn.commons.Validate;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;

public class LuceneConfiguration {
	
	 private static Configuration configuration;
	 public LuceneConfiguration() {
	        getConfiguration();
	 }
	
	 private static Configuration getConfiguration() {
	        if (configuration == null) {
	            configuration = new Configuration();
	            String hdfsConfPath = getHDFSConfPath();
	            if (!hdfsConfPath.equals(ForesightConstants.LOCAL)) {
	                configuration.addResource(new Path(hdfsConfPath + ForesightConstants.CORE_SITE_XML));
	                configuration.addResource(new Path(hdfsConfPath + ForesightConstants.HDFS_SITE_XML));
	                configuration.set(CommonConfigurationKeys.FS_FILE_IMPL_KEY, LocalFileSystem.class.getName());
	            }
	        }
	        return configuration;
	    }
	 
	 private static String getHDFSConfPath() {
	        String destinationDir = ConfigUtils.getString("HDFS_CONF_PATH", true);
	        if (!destinationDir.equals(ForesightConstants.LOCAL)) {
	            Validate.checkFileExists(destinationDir);
	        }
	        if (!StringUtils.endsWith(destinationDir, File.separator)) {
	            destinationDir += File.separator;
	        }
	        return destinationDir;
	    }

	 protected FileSystem getFileSystem() throws IOException {
	            return FileSystem.newInstance(configuration);
	 }
	 
	
	        
}
