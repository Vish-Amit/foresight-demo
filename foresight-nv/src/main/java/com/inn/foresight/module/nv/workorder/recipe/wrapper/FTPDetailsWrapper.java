package com.inn.foresight.module.nv.workorder.recipe.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;

/**
 * FTPDetailsWrapper.
 * @author 
 */
@RestWrapper
public class FTPDetailsWrapper {
	
	private String hostName;
	
	private String userName;
	
	private String password;
	
	private String downloadFilePath;
	
	private String downloadFileName;
	
	private String uploadFilePath;
	
	private String uploadFileName;

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the downloadFilePath
	 */
	public String getDownloadFilePath() {
		return downloadFilePath;
	}

	/**
	 * @param downloadFilePath the downloadFilePath to set
	 */
	public void setDownloadFilePath(String downloadFilePath) {
		this.downloadFilePath = downloadFilePath;
	}

	/**
	 * @return the downloadFileName
	 */
	public String getDownloadFileName() {
		return downloadFileName;
	}

	/**
	 * @param downloadFileName the downloadFileName to set
	 */
	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/**
	 * @return the uploadFilePath
	 */
	public String getUploadFilePath() {
		return uploadFilePath;
	}

	/**
	 * @param uploadFilePath the uploadFilePath to set
	 */
	public void setUploadFilePath(String uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
	}

	/**
	 * @return the uploadFileName
	 */
	public String getUploadFileName() {
		return uploadFileName;
	}

	/**
	 * @param uploadFileName the uploadFileName to set
	 */
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public FTPDetailsWrapper(String hostName, String userName, String password, String downloadFilePath,
			String downloadFileName, String uploadFilePath, String uploadFileName) {
		super();
		this.hostName = hostName;
		this.userName = userName;
		this.password = password;
		this.downloadFilePath = downloadFilePath;
		this.downloadFileName = downloadFileName;
		this.uploadFilePath = uploadFilePath;
		this.uploadFileName = uploadFileName;
	}

	public FTPDetailsWrapper() {
		
	}

	@Override
	public String toString() {
		return "FTPDetailsWrapper [hostName=" + hostName + ", userName=" + userName + ", password=" + password
				+ ", downloadFilePath=" + downloadFilePath + ", downloadFileName=" + downloadFileName
				+ ", uploadFilePath=" + uploadFilePath + ", uploadFileName=" + uploadFileName + "]";
	}
	
	

}
