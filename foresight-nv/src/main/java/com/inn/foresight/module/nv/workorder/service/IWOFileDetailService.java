package com.inn.foresight.module.nv.workorder.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;


/**
 * The Interface IWOFileDetailService.
 *
 * @author innoeye
 * date - 30-Dec-2017 1:23:46 PM
 */
public interface IWOFileDetailService extends IGenericService<Integer, WOFileDetail> {
	
	/**
	 * Inserts the information of synced Qmdl File in WOFileDetail 
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
	WOFileDetail insertWOFileDetail(Integer woRecipeMappingId, String fileName, String hdfsFilePath, Boolean isRetried, String fileType);

	/**
	 * Find WO file detail.
	 *
	 * @param filePath the file path
	 * @return the WO file detail
	 * @throws RestException the rest exception
	 */
	WOFileDetail findWOFileDetail(String filePath);

	
	/**
	 * Update status of file processed.
	 *
	 * @param woFileDetail the wo file detail
	 * @return the string
	 * @throws RestException 
	 */
	/**
	 * Gets the unprocessed file detail.
	 *
	 * @return the unprocessed file detail
	 * @throws RestException the rest exception
	 */
	List<WOFileDetail> getUnprocessedFileDetail();

String updateStatusOfFileProcessed(WOFileDetail woFileDetail);

	List<WOFileDetail> getFileDetailByWorkOrderId(Integer workrorderId);
	List<WOFileDetail> getUnProcessFileDetailByWorkOrderId(Integer woId, Integer recipeId);
	List<Integer> getUnprocessedWorkOrder();

	List<WOFileDetail> getFileDetailByWorkOrderIdList(List<Integer> workorderIdList);

	List<WOFileDetail> getFileDetailByWorkOrderId(Integer workrorderId, List<String> recipeCategory);

	String updatePathofFileProcessed(WOFileDetail woFileDetail, String filepath);
	
	 List<WOFileDetail> getFileDetailListByWorkOrderId(Integer workrorderId);

	List<WOFileDetail> getFileDetailByRecipeMappingId(Integer recipeId);

	
}
