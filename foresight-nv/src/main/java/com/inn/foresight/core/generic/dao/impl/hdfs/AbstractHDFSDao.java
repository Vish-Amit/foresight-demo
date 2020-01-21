package com.inn.foresight.core.generic.dao.impl.hdfs;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.Validate;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.HdfsClient;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;

/**
 * The Class AbstractHDFSDao for performing HDFS operations.
 *
 * @author Nimit Agrawal
 */
public abstract class AbstractHDFSDao extends HdfsClient {

    /** The logger. */
    private static Logger logger = LogManager.getLogger(AbstractHDFSDao.class);

    /** HDFS configuration. */
    private static Configuration configuration;

    /**
     * Instantiates a new HDFS dao.
     */
    public AbstractHDFSDao() {
        super(getConfiguration());
    }

    /**
     * Gets the HDFS Configuration.
     *
     * @return This method makes and return hdfs Configuration object.
     */
    private static synchronized Configuration getConfiguration() {
        if (configuration == null) {
            configuration = new Configuration();
            String hdfsConfPath = getHDFSConfPath();
            logger.info("HDFS configuration filepath is {}", hdfsConfPath);
            if (!hdfsConfPath.equals(ForesightConstants.LOCAL)) {
                configuration.addResource(new Path(hdfsConfPath + ForesightConstants.CORE_SITE_XML));
                configuration.addResource(new Path(hdfsConfPath + ForesightConstants.HDFS_SITE_XML));
                configuration.set(CommonConfigurationKeys.FS_FILE_IMPL_KEY, LocalFileSystem.class.getName());
                logger.info("Connecting to {}", configuration.get(CommonConfigurationKeys.FS_DEFAULT_NAME_KEY));
            }
        }
        return configuration;
    }

    /**
     * Gets the HDFS conf path.
     *
     * @return the HDFS conf path
     */
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
    
  
}
