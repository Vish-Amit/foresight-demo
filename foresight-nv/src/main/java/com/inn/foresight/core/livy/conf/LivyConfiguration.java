package com.inn.foresight.core.livy.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The Class LivyConfiguration.
 * 
 * @author Zafar
 */
/**
 * 
 * HBase LivyConfiguration
 * 
 *  @author Harsh
 */
@Service("LivyConfiguration")
public class LivyConfiguration {

    /** The hdfs connection url. */
    @Value("${livy.hdfsConnectionUrl}")
    private String hdfsConnectionUrl;

    /** The code template path. */
    @Value("${livy.codeTemplatePath:#{null}}")
    private String codeTemplatePath;

    /** The shc template name. */
    @Value("${livy.shcTemplateName}")
    private String shcTemplateName;

    /** The session url. */
    @Value("${livy.sessionUrl}")
    private String sessionUrl;
	

    /** The zk address. */
    @Value("${livy.zkAddress}")
    private String zkAddress;
	
    /** The parent node path. */
    @Value("${livy.parentNodePath}")
    private String parentNodePath;
	
	
    /** The jar path. */
    @Value("${livy.jarPath}")
    private String jarPath;
	
	
    /** The catalog query. */
    @Value("${livy.catalogQuery}")
    private String catalogQuery;
	
	/**
	 * Gets the catalog query.
	 *
	 * @return the catalog query
	 */
	public String getCatalogQuery() {
		return catalogQuery;
	}

	/**
	 * Sets the catalog query.
	 *
	 * @param catalogQuery the new catalog query
	 */
	public void setCatalogQuery(String catalogQuery) {
		this.catalogQuery = catalogQuery;
	}

	/**
	 * Gets the parent node path.
	 *
	 * @return the parent node path
	 */
	public String getParentNodePath() {
		return parentNodePath;
	}

	/**
	 * Sets the parent node path.
	 *
	 * @param parentNodePath the new parent node path
	 */
	public void setParentNodePath(String parentNodePath) {
		this.parentNodePath = parentNodePath;
	}

	/**
	 * Gets the zk address.
	 *
	 * @return the zk address
	 */
	public String getZkAddress() {
		return zkAddress;
	}

	/**
	 * Sets the zk address.
	 *
	 * @param zkAddress the new zk address
	 */
	public void setZkAddress(String zkAddress) {
		this.zkAddress = zkAddress;
	}

	/**
	 * Gets the hdfs connection url.
	 *
	 * @return the hdfs connection url
	 */
	public String getHdfsConnectionUrl() {
		return hdfsConnectionUrl;
	}

	/**
	 * Sets the hdfs connection url.
	 *
	 * @param hdfsConnectionUrl the new hdfs connection url
	 */
	public void setHdfsConnectionUrl(String hdfsConnectionUrl) {
		this.hdfsConnectionUrl = hdfsConnectionUrl;
	}


	/**
	 * Gets the jar path.
	 *
	 * @return the jar path
	 */
	public String getJarPath() {
		return jarPath;
	}

	/**
	 * Sets the jar path.
	 *
	 * @param jarPath the new jar path
	 */
	public void setJarPath(String jarPath) {
		this.jarPath = jarPath;
	}

	/**
	 * Gets the session url.
	 *
	 * @return the session url
	 */
	public String getSessionUrl() {
		return sessionUrl;
	}

	/**
	 * Sets the session url.
	 *
	 * @param sessionUrl the new session url
	 */
	public void setSessionUrl(String sessionUrl) {
		this.sessionUrl = sessionUrl;
	}

	/**
	 * Gets the code template path.
	 *
	 * @return the code template path
	 */
	public String getCodeTemplatePath() {
		return codeTemplatePath;
	}

	/**
	 * Sets the code template path.
	 *
	 * @param codeTemplatePath the new code template path
	 */
	public void setCodeTemplatePath(String codeTemplatePath) {
		this.codeTemplatePath = codeTemplatePath;
	}

	/**
	 * Gets the shc template name.
	 *
	 * @return the shc template name
	 */
	public String getShcTemplateName() {
		return shcTemplateName;
	}

	/**
	 * Sets the shc template name.
	 *
	 * @param shcTemplateName the new shc template name
	 */
	public void setShcTemplateName(String shcTemplateName) {
		this.shcTemplateName = shcTemplateName;
	}

	@Override
	public String toString() {
		return "LivyConfiguration [hdfsConnectionUrl=" + hdfsConnectionUrl + ", codeTemplatePath=" + codeTemplatePath
				+ ", shcTemplateName=" + shcTemplateName + ", sessionUrl=" + sessionUrl + ", jarPath=" + jarPath + "]";
	}
}