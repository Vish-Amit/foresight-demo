package com.inn.foresight.module.nv.workorder.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.wrapper.WOFileDetailWrapper;

/**
 * The Interface IWOFileDetailDao.
 *
 * @author innoeye
 * date - 30-Dec-2017 1:17:22 PM
 */
public interface IWOFileDetailDao extends IGenericDao<Integer, WOFileDetail> {

	/**
	 * Find WO file detail.
	 *
	 * @param filePath the file path
	 * @return the WO file detail
	 * @throws RestException the rest exception
	 */
	WOFileDetail findWOFileDetail(String filePath);

	/**
	 * Gets the unprocessed file detail.
	 *
	 * @return the unprocessed file detail
	 * @throws RestException the rest exception
	 */
	
	List<WOFileDetail> findFileByRecipeMappingId(Integer woRecipeMappingId);

	List<WOFileDetail> findFileByRecipeMappingIdAndFilePart(Integer woRecipeMappingId, String fileNamePart);

	List<WOFileDetail> findFileByRecipeMappingIdList(List<Integer> woIdRecipeMappingList);
/**
	 * Gets the unprocessed file detail.
	 *
	 * @return the unprocessed file detail
	 * @throws RestException the rest exception
	 */
	List<WOFileDetail> getUnprocessedFileDetail();


	void updateWoRecord(WOFileDetail woFileDetail);


	List<WOFileDetail> getFileDetailByWorkOrderId(Integer workrorderId);


	List<WOFileDetail> getUnProcessFileDetailByWorkOrderId(Integer woId);


	List<Integer> getUnprocessedWorkOrder();


	List<WOFileDetail> getUnProcessFileDetailByRecipeMappingId(Integer woId, Integer recipeId);


	List<WOFileDetail> getFileDetailByWorkOrderId(List<Integer> workorderIdList);


	List<WOFileDetail> getFileDetailByWorkOrderId(Integer workrorderId, List<String> recipeCategory);

	List<WOFileDetail> getFileDetailByIds(List<Integer> idList);

	WOFileDetail getFileDetailByWORecipeMappingAndFileName(Integer recipeMappingId, String fileName);

	List<Integer> getProccesedLogFileMappingList(Integer woId);

	List<WOFileDetailWrapper> getProccesedFilesWithRecipe(Integer woId);

	List<WOFileDetail> getFileDetailListByWorkOrderId(Integer workrorderId);

	List<WOFileDetail> getFileDetailByWORecipeMappingId(Integer id);

	List<AnalyticsRepository> getAnalyticsForWOAndRecipeId(Integer workOrderId, Integer recipeId);
	
	List<WOFileDetail> getCompletedFileByRecipeMappingId(Integer recipeId);

	List<WOFileDetail> findWOFileDetailwithCustomFilters(Integer lowerLimit, Integer upperLimit, WOFileDetailWrapper fileDetailWrapper);

	Long getAllFilesCount(WOFileDetailWrapper wrapper);

	List<WOFileDetail> getFileDetailByRecipeMappingId(Integer recipeId);

}
