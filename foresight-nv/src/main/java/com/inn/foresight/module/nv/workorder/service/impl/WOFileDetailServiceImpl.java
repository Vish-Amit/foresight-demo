package com.inn.foresight.module.nv.workorder.service.impl;

import java.util.Date;
import java.util.List;

import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.dao.impl.NVLayer3HDFSDaoImpl;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWOFileDetailDao;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping.Status;
import com.inn.foresight.module.nv.workorder.service.IWOFileDetailService;

/**
 * The Class WOFileDetailServiceImpl.
 *
 * @author innoeye
 * date - 30-Dec-2017 1:26:51 PM
 */
@Service("WOFileDetailServiceImpl")
public class WOFileDetailServiceImpl extends AbstractService<Integer, WOFileDetail>
		implements
			IWOFileDetailService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(WOFileDetailServiceImpl.class);
	
	/** The wo file detail dao. */
	@Autowired
	private IWOFileDetailDao woFileDetailDao;

	@Autowired
	private INVLayer3HDFSDao nvLayer3HDFSDao;
	
	/**
	 * Sets the dao.
	 * @param woFileDetailDao the new dao
	 */
	@Autowired
	public void setDao(IWOFileDetailDao woFileDetailDao) {
		super.setDao(woFileDetailDao);
		this.woFileDetailDao = woFileDetailDao;
	}
	
	/** The wo recipe mapping dao. */
	@Autowired
	private IWORecipeMappingDao woRecipeMappingDao;
	


	/**
	 * Inserts the information of synced QMDL File in WOFileDetail and Update Status of corresponding Recipe and Workorder.
	 * (Example : File path in HDFS, Filename etc).
	 *
	 * @param woRecipeMappingId the wo recipe mapping id
	 * @param fileName the name of file which got save in hdfs
	 * @param hdfsFilePath the hdfs path of file
	 * @param isRetried the is retried
	 * @return created instance of WOFileDetail
	 * @throws DaoException Signals that an Dao exception has occurred.
	 * @throws RestException Signals that an Business exception has occurred.
	 */
	@Override
	@Transactional
	public WOFileDetail insertWOFileDetail(Integer woRecipeMappingId, String fileName, String hdfsFilePath,
			Boolean isRetried,String fileType) {
		logger.info("Going to insert WO File Detail");
		Date creationDate = new Date();
		WORecipeMapping woRecipeMapping = woRecipeMappingDao.findByPk(woRecipeMappingId);
		if(woRecipeMapping != null) {
			logger.info("Going to create Wofile detail for recipe {}",woRecipeMapping.getId());
			//if (!woRecipeMapping.getStatus().equals(Status.COMPLETED)) {
				WOFileDetail woFileDetail = new WOFileDetail();
				woFileDetail.setFileName(fileName);
				woFileDetail.setFilePath(hdfsFilePath);
				woFileDetail.setIsProcessed(Boolean.FALSE);
				woFileDetail.setCreationTime(creationDate);
				woFileDetail.setModificationTime(creationDate);
				woFileDetail.setWoRecipeMapping(woRecipeMapping);
				woFileDetail.setFiletype(fileType);
				woFileDetail.setFileSize(nvLayer3HDFSDao.getHDFSFileSize(hdfsFilePath));
				woFileDetail = woFileDetailDao.create(woFileDetail);
				logger.info("Done update WO File Detail");
				return woFileDetail;
			/*} else {
				throw new RestException(NVLayer3Constants.INVALID_WORECIPEMAPPING_ID.replace(NVLayer3Constants.ID,
						String.valueOf(woRecipeMappingId)));
			}*/
		} else {
			throw new RestException(NVLayer3Constants.INVALID_WORECIPEMAPPING_ID.replace(NVLayer3Constants.ID,
					String.valueOf(woRecipeMappingId)));
		}
	}
	
	/**
	 * Find WO file detail.
	 *
	 * @param filePath the file path
	 * @return the WO file detail
	 * @throws RestException the rest exception
	 */
	@Override
	public WOFileDetail findWOFileDetail(String filePath){
		logger.info("Going to find WO File Detail for filePath : {}",filePath);
		try {
			WOFileDetail woFileDetail = woFileDetailDao.findWOFileDetail(filePath);
			logger.info("Done @findWOFileDetail");
			return woFileDetail;
		} catch (RestException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, "WOFileDetail", e));
		}
	}
	
	/**
	 * Get All unprocess Files from mySql.
	 *
	 * @return the unprocessed file detail
	 * @throws RestException the rest exception
	 */
	@Override
	public List<WOFileDetail> getUnprocessedFileDetail(){
		try {
			return woFileDetailDao.getUnprocessedFileDetail();
		} catch (RestException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, "WOFileDetail", e));
		}
	}

	/**
	 * Update status of file processed.
	 *
	 * @param woFileDetail the wo file detail
	 * @return the string
	 * @throws RestException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String updateStatusOfFileProcessed(WOFileDetail woFileDetail) {
		woFileDetail.setIsProcessed(true);		
		try {
			woFileDetailDao.updateWoRecord(woFileDetail);
			return QMDLConstant.SUCCESS;
		} catch (RestException e) {
			logger.error("Unable to update  status for file {}   Error {}  ",woFileDetail.getFilePath(),Utils.getStackTrace(e));
			throw new RestException("Error in Updating File status");
		}catch(Exception e) {
			logger.error("Exception inside method updateStatusOfFileProcessed {} ",Utils.getStackTrace(e));
		}
		return QMDLConstant.FAILURE;
	}
	
	
	/**
	 * Update path of file processed.
	 *
	 * @param woFileDetail the wo file detail
	 * @return the string
	 * @throws RestException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String updatePathofFileProcessed(WOFileDetail woFileDetail,String filepath) {
		woFileDetail.setProcessedLogFilePath(filepath);
		try {
			woFileDetailDao.updateWoRecord(woFileDetail);
			return QMDLConstant.SUCCESS;
		} catch (RestException e) {
			logger.error("Unable to update  status for file {}   Error {}  ",woFileDetail.getFilePath(),Utils.getStackTrace(e));
			throw new RestException("Error in Updating File status");
		}catch(Exception e) {
			logger.error("Exception inside method updateStatusOfFileProcessed {} ",Utils.getStackTrace(e));
		}
		return QMDLConstant.FAILURE;
	}

	@Override
	public List<WOFileDetail> getFileDetailByWorkOrderId(Integer workrorderId) {
		return woFileDetailDao.getFileDetailByWorkOrderId(workrorderId);
		
	}



	@Override
	public List<WOFileDetail> getUnProcessFileDetailByWorkOrderId(Integer woId,Integer recipeId) {
		if(recipeId!=null){
			return woFileDetailDao.getUnProcessFileDetailByRecipeMappingId(woId,recipeId);
		}else{
			return woFileDetailDao.getUnProcessFileDetailByWorkOrderId(woId);
		}
	}

	@Override
	public List<Integer> getUnprocessedWorkOrder() {
		return woFileDetailDao.getUnprocessedWorkOrder();

	}


	@Override
	public List<WOFileDetail> getFileDetailByWorkOrderIdList(List<Integer> workorderIdList) {
		return woFileDetailDao.getFileDetailByWorkOrderId(workorderIdList);
	}


	@Override
	public List<WOFileDetail> getFileDetailByWorkOrderId(Integer workrorderId, List<String> recipeCategory) {
		return woFileDetailDao.getFileDetailByWorkOrderId(workrorderId,recipeCategory);
	}
	
	@Override
	public List<WOFileDetail> getFileDetailListByWorkOrderId(Integer workrorderId) {
		return woFileDetailDao.getFileDetailListByWorkOrderId(workrorderId);
	}

	@Override
	public List<WOFileDetail> getFileDetailByRecipeMappingId(Integer recipeId) {
		return woFileDetailDao.getFileDetailByRecipeMappingId(recipeId);
	}
}
