package com.inn.foresight.core.fileupload.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.fileupload.dao.IFileUploadDao;
import com.inn.foresight.core.fileupload.model.FileUpload;
import com.inn.foresight.core.fileupload.utils.wrapper.FileUploadWrapper;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.product.um.user.utils.UmConstants;

@Repository("FileUploadDaoImpl")
public class FileUploadDaoImpl extends HibernateGenericDao<Integer, FileUpload> implements IFileUploadDao
{

	/** The logger. */
	private Logger logger = LogManager.getLogger(FileUploadDaoImpl.class);

	public FileUploadDaoImpl() {
		super(FileUpload.class);
		
	}

	@Override
	public void enableFileSearchFilter(String fileSearch) {
		try {
			logger.info("Inside enableFileSearchFilter");
			if (fileSearch != null) {
				Session session = (Session) getEntityManager().getDelegate();
				session.enableFilter("fileSearchFilter").setParameter("fileSearch", ForesightConstants.MODULUS + fileSearch + ForesightConstants.MODULUS);
			}
		} catch (Exception e) {
			logger.error("Error in enableFileSearchFilter : {}", Utils.getStackTrace(e));
		}
	}
	
	
	@Override
	public List<FileUploadWrapper> getFileUploadList(Integer llimit, Integer ulimit) {
		logger.info("Inside getFileUploadList ");
		List<FileUploadWrapper> uploadedFileList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getFileUploadList");

			if (llimit != null && ulimit != null && llimit >= 0 && ulimit > 0) 
			{
				query.setMaxResults(ulimit - llimit + 1);
				query.setFirstResult(llimit);
			}
			uploadedFileList = query.getResultList();
			logger.info("uploadedFileList size {} ", uploadedFileList.size());
			
		} catch (NoResultException e) {
			logger.error("NoResultException in getFileUploadList  {} ", Utils.getStackTrace(e));
		} catch (NullPointerException e) {
			logger.error("NullPointerException in getFileUploadList  {} ", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception in getFileUploadList  {} ", Utils.getStackTrace(e));
		}
		return uploadedFileList;
	}
	
	
	@Override
	public Long getFileUploadCount() {
		logger.info("Inside getFileUploadCount ");
		Long count=0L;
		try {
			Query query = getEntityManager().createNamedQuery("getFileUploadCount");
           count=(Long) query.getSingleResult();
			logger.info("Total count for uploadedFile : {} ", count);
			
		} catch (NoResultException e) {
			logger.error("NoResultException in getFileUploadCount  {} ", Utils.getStackTrace(e));
		} catch (NullPointerException e) {
			logger.error("NullPointerException in getFileUploadCount  {} ", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception in getFileUploadCount  {} ", Utils.getStackTrace(e));
		}
		return count;
	}
	
	
  @Override
	public int deletefilebyFileId(List<Integer> fileId)
	{
		logger.info("Inside deletefilebyFileId ");
		int value=0;
		try {
			Query query = getEntityManager().createNamedQuery("deleteFileById");
			query.setParameter(UmConstants.ID, fileId);
            value=query.executeUpdate();
			logger.info("value : {} ", value);
			
		} catch (NoResultException e) {
			logger.error("NoResultException in deletefilebyFileId  {} ", Utils.getStackTrace(e));
		} catch (NullPointerException e) {
			logger.error("NullPointerException in deletefilebyFileId  {} ", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception in deletefilebyFileId  {} ", Utils.getStackTrace(e));
		}
		return value;
	}
  
  
  @Override
	public String  getFilePathByFileName(String fileName) {
		logger.info("Inside getFilePathByFileName ");
		String  filePath=ForesightConstants.BLANK_STRING;
		try {
			Query query = getEntityManager().createNamedQuery("getFilePathByFileName").setParameter("name", fileName);
			filePath=(String) query.getSingleResult();
			logger.info("filepath : {} ", filePath);
			
		} catch (NoResultException e) {
			logger.error("NoResultException in getFilePathByFileName  {} ", Utils.getStackTrace(e));
		} catch (NullPointerException e) {
			logger.error("NullPointerException in getFilePathByFileName  {} ", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception in getFilePathByFileName  {} ", Utils.getStackTrace(e));
		}
		return filePath;
	}
	
@Override
	public List<FileUploadWrapper> getFileUploadListForOtherUser(Integer llimit, Integer ulimit,List<Integer> moduleId) {
		logger.info("Inside getFileUploadListForOtherUser ");
		List<FileUploadWrapper> uploadedFileList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getFileUploadListForOtherUser").setParameter("moduleId", moduleId);

			if (llimit != null && ulimit != null && llimit >= 0 && ulimit > 0) 
			{
				query.setMaxResults(ulimit - llimit + 1);
				query.setFirstResult(llimit);
			}
			uploadedFileList = query.getResultList();
			logger.info("uploadedFileList size {} ", uploadedFileList.size());
			
		} catch (NoResultException e) {
			logger.error("NoResultException in getFileUploadListForOtherUser  {} ", Utils.getStackTrace(e));
		} catch (NullPointerException e) {
			logger.error("NullPointerException in getFileUploadListForOtherUser  {} ", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception in getFileUploadListForOtherUser  {} ", Utils.getStackTrace(e));
		}
		return uploadedFileList;
	}


@Override
public Long getFileUploadCountForOtherUser(List<Integer> moduleId) {
	logger.info("Inside getFileUploadCountForOtherUser ");
	Long count=0L;
	try {
		Query query = getEntityManager().createNamedQuery("getFileUploadCountForOtherUser").setParameter("moduleId", moduleId);
       count=(Long) query.getSingleResult();
		logger.info("Total count for uploadedFile : {} ", count);
		
	} catch (NoResultException e) {
		logger.error("NoResultException in getFileUploadCountForOtherUser  {} ", Utils.getStackTrace(e));
	} catch (NullPointerException e) {
		logger.error("NullPointerException in getFileUploadCountForOtherUser  {} ", Utils.getStackTrace(e));
	} catch (Exception e) {
		logger.error("Exception in getFileUploadCountForOtherUser  {} ", Utils.getStackTrace(e));
	}
	return count;
}

}
