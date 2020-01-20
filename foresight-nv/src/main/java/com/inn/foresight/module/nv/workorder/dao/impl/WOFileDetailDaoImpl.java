package com.inn.foresight.module.nv.workorder.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.foresight.module.nv.workorder.wrapper.WOFileDetailWrapper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWOFileDetailDao;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;

/**
 * The Class WOFileDetailDaoImpl.
 *
 * @author innoeye date - 30-Dec-2017 1:19:00 PM
 */
@Repository("WOFileDetailDaoImpl")
public class WOFileDetailDaoImpl extends HibernateGenericDao<Integer, WOFileDetail> implements IWOFileDetailDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(WOFileDetailDaoImpl.class);

	/** Construct WOFileDetailDaoImpl object. */
	public WOFileDetailDaoImpl() {
		super(WOFileDetail.class);
	}

	/**
	 * Creates the.
	 *
	 * @param woFileDetail
	 *            the wo file detail
	 * @return the WO file detail
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public WOFileDetail create(WOFileDetail woFileDetail) {
		logger.info("Going to create WOFileDetail : {}", woFileDetail);
		return super.create(woFileDetail);
	}

	/**
	 * Updates the.
	 *
	 * @param woFileDetail
	 *            the wo file detail
	 * @return the WO file detail
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public WOFileDetail update(WOFileDetail woFileDetail) {
		logger.info("Going to update WOFileDetail : {}", woFileDetail);
		return super.update(woFileDetail);
	}

	/**
	 * Find WO file detail.
	 *
	 * @param filePath
	 *            the file path
	 * @return the WO file detail
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public WOFileDetail findWOFileDetail(String filePath) {
		logger.info("Going to find WO File Detail for filePath : {}", filePath);
		Query query = getEntityManager().createNamedQuery("findWORecipeMapping");
		query.setParameter("filePath", filePath);
		try {

			List<WOFileDetail> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list.get(0);
		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	/**
	 * Get list of all Unprocess File list.
	 *
	 * @return the unprocessed file detail
	 * @throws RestException
	 *             the rest exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> getUnprocessedFileDetail() {
		Query query = getEntityManager().createNamedQuery("findAllUnprocessFile");
		try {
			List<WOFileDetail> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> getFileDetailByWorkOrderId(Integer workorderId) {
		Query query = getEntityManager().createNamedQuery("findFileByWorkOrderId")
				.setParameter(NVWorkorderConstant.WORKORDER_ID, workorderId);
		try {

			List<WOFileDetail> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@Override
	public WOFileDetail getFileDetailByWORecipeMappingAndFileName(Integer recipeMappingId, String fileName) {
		logger.debug("inside the method getFileDetailByWORecipeMappingAndFileName recipeMappingId fileName {} {}",
				recipeMappingId, fileName);
		Query query = getEntityManager().createNamedQuery("findFileDetailByWORecipeMappingAndFileName")
				.setParameter("recipeMappingId", recipeMappingId).setParameter("fileName", fileName);
		try {
			WOFileDetail woFileDetail = (WOFileDetail) query.getSingleResult();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, woFileDetail);
			return woFileDetail;
		} catch (NoResultException ne) {
			logger.error("NoResultException inside the method getFileDetailByWORecipeMappingAndFileName {}",
					ne.getMessage());

			throw ne;
		} catch (NonUniqueResultException ne) {
			logger.error("NonUniqueResultException inside the method getFileDetailByWORecipeMappingAndFileName {}",
					ne.getMessage());

		} catch (QueryTimeoutException q) {
			logger.error("QueryTimeoutException inside the method getFileDetailByWORecipeMappingAndFileName {}",
					q.getMessage());

		} catch (Exception e) {
			logger.error("Exception inside the method getFileDetailByWORecipeMappingAndFileName {}",
					Utils.getStackTrace(e));
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> findFileByRecipeMappingId(Integer woRecipeMappingId) {
		Query query = getEntityManager().createNamedQuery("findFileByRecipeMappingId")
				.setParameter(NVWorkorderConstant.WO_RECIPE_MAPPING_ID, woRecipeMappingId);
		try {
			List<WOFileDetail> list = query.getResultList();

			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		} catch (Exception e) {
			logger.error("Exception inside the method findFileByRecipeMappingId {}", e.getMessage());
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> findFileByRecipeMappingIdAndFilePart(Integer woRecipeMappingId, String fileNamePart) {
		Query query = getEntityManager().createNamedQuery("findFileByRecipeMappingIdAndFilePart");
		query.setParameter("woRecipeMappingId", woRecipeMappingId);
		query.setParameter("fileNamePart", fileNamePart);
		try {
			List<WOFileDetail> list = query.getResultList();

			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> findFileByRecipeMappingIdList(List<Integer> woIdRecipeMappingList) {
		Query query = getEntityManager().createNamedQuery("findFileByRecipeMappingIdList")
				.setParameter("woRecipeMappingId", woIdRecipeMappingList);
		try {
			List<WOFileDetail> list = query.getResultList();

			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@Override
	public void updateWoRecord(WOFileDetail woFileDetail) {
		try {
			update(woFileDetail);
		} catch (DaoException e) {
			logger.error("Error while updating woFileDetail {} ", Utils.getStackTrace(e));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> getUnProcessFileDetailByWorkOrderId(Integer woId) {

		try {
			Query query = getEntityManager().createNamedQuery("findAllUnprocessFileByWoId")
					.setParameter(NVWorkorderConstant.WORKORDER_ID, woId);
			return query.getResultList();
		} catch (NoResultException e) {
			throw new DaoException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getUnprocessedWorkOrder() {

		try {
			Query query = getEntityManager().createNamedQuery("findAllUnprocessWorkOrder");
			return query.getResultList();
		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> getFileDetailByWorkOrderId(List<Integer> workorderIdList) {

		Query query = getEntityManager().createNamedQuery("findFileByWorkOrderIdList").setParameter("workrorderIdList",
				workorderIdList);
		try {
			return query.getResultList();
		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> getFileDetailByWorkOrderId(Integer workrorderId, List<String> recipeCategory) {
		try {
			Query query = getEntityManager().createNamedQuery("findFileByWorkOrderIdAndRecipeCategory")
					.setParameter("workrorderId", workrorderId).setParameter("category", recipeCategory);
			return query.getResultList();
		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> getUnProcessFileDetailByRecipeMappingId(Integer woId, Integer recipeId) {

		try {
			Query query = getEntityManager().createNamedQuery("findAllUnprocessFileByRecipeId")
					.setParameter("workrorderId", woId).setParameter("recipeId", recipeId);
			return query.getResultList();
		} catch (NoResultException e) {
			throw new DaoException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> getCompletedFileByRecipeMappingId(Integer recipeId) {

		try {
			Query query = getEntityManager().createNamedQuery("findCompletedFileByRecipeMappingId")
					.setParameter("recipeId", recipeId);
			return query.getResultList();
		} catch (NoResultException e) {
			throw new DaoException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> getFileDetailByRecipeMappingId(Integer recipeId) {

		try {
			Query query = getEntityManager().createNamedQuery("findFileDetailByRecipeMappingId")
					.setParameter("recipeId", recipeId);
			return query.getResultList();
		} catch (NoResultException e) {
			throw new DaoException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> getFileDetailByIds(List<Integer> idList) {
		try {
			Query query = getEntityManager().createNamedQuery("getFileDetailByIds").setParameter("idList", idList);
			return query.getResultList();
		} catch (NoResultException e) {
			throw new DaoException(NVWorkorderConstant.DATA_NOT_FOUND);
		} catch (Exception e) {
			throw new DaoException(NVWorkorderConstant.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getProccesedLogFileMappingList(Integer woId) {
		try {
			logger.info("Going to get WORecipeMapping list for processed log file ");
			Query query = getEntityManager().createNamedQuery("getProccesedLogFileMappingList");
			query.setParameter("woId", woId);
			List<Integer> proccesedLogFileMappingList = query.getResultList();
			logger.info("Returning total: {} wo receipe mapping for processed log file",
					proccesedLogFileMappingList.size());
			return proccesedLogFileMappingList;
		} catch (Exception e) {
			logger.error("Error while getting WORecipeMapping list for processed log file: {}",
					ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}
	}


	@Override
	public List<WOFileDetailWrapper> getProccesedFilesWithRecipe(Integer woId) {
		try {
			logger.info("Going to get WORecipeMapping list for processed log file ");
			Query query = getEntityManager().createNamedQuery("findProcessedFilesByWoId");
			query.setParameter("woId", woId);
			List<WOFileDetailWrapper> proccesedLogFileMappingList = query.getResultList();
			logger.info("Returning total: {} wo receipe  for processed log file",
					proccesedLogFileMappingList.size());
			return proccesedLogFileMappingList;
		} catch (Exception e) {
			logger.error("Error inside getProccesedFilesWithRecipe while getting  for processed log file: {}",
					ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WOFileDetail> getFileDetailListByWorkOrderId(Integer workrorderId) {
		try {
			Query query = getEntityManager().createNamedQuery("findFileListByWorkorderId").setParameter("workrorderId",
					workrorderId);
			return query.getResultList();
		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@Override
	public List<WOFileDetail> getFileDetailByWORecipeMappingId(Integer recipeMappingId) {

		logger.debug("inside the method getFileDetailByWORecipeMappingId recipeMappingId {}", recipeMappingId);
		Query query = getEntityManager().createNamedQuery("findFileDetailByWORecipeMappingId")
				.setParameter("recipeMappingId", recipeMappingId);
		try {
			List<WOFileDetail> resultList = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, resultList.size());
			return resultList;
		} catch (NoResultException e) {
			logger.error("NoResultException inside the method getFileDetailByWORecipeMappingId {}", e.getMessage());
		} catch (QueryTimeoutException q) {
			logger.error("QueryTimeoutException inside the method getFileDetailByWORecipeMappingId {}", q.getMessage());

		} catch (Exception e) {
			logger.error("Exception inside the method getFileDetailByWORecipeMappingId {}", Utils.getStackTrace(e));
		}
		return null;

	}

	@Override
	public List<AnalyticsRepository> getAnalyticsForWOAndRecipeId(Integer workOrderId, Integer recipeId) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<AnalyticsRepository> criteriaQuery = criteriaBuilder.createQuery(AnalyticsRepository.class);
		Root<AnalyticsRepository> root = criteriaQuery.from(AnalyticsRepository.class);
		List<Predicate> predicateList = getPredicateList(workOrderId, null, criteriaBuilder, root);
		addPredicateInCreateriaQuery(criteriaQuery, predicateList);
		List<AnalyticsRepository> result = getEntityManager().createQuery(criteriaQuery).getResultList();
		logger.info("Result ::: {}", result.size());
		return result;
	}

	public static CriteriaQuery<AnalyticsRepository> addPredicateInCreateriaQuery(
			CriteriaQuery<AnalyticsRepository> criteriaQuery, List<Predicate> predicates) {
		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		return criteriaQuery;
	}

	private List<Predicate> getPredicateList(Integer workOrderId, Integer recipeId, CriteriaBuilder criteriaBuilder,
			Root<AnalyticsRepository> root) {
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(criteriaBuilder.notEqual(root.get("progress"), AnalyticsRepository.progress.In_Progress));
		predicates.add(criteriaBuilder.like(root.get("reportConfig"),
				"%" + NVWorkorderConstant.WOID_JSON_KEY + workOrderId + "%"));
		if (recipeId != null) {
			predicates.add(criteriaBuilder.like(root.get("reportConfig"),
					"%" + NVWorkorderConstant.RECIPEID_JSON_KEY + recipeId + "%"));
		}
		return predicates;
	}

	@Override
	public List<WOFileDetail> findWOFileDetailwithCustomFilters(Integer lowerLimit, Integer upperLimit,
			WOFileDetailWrapper fileDetailWrapper) throws DaoException {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<WOFileDetail> criteriaQuery = criteriaBuilder.createQuery(WOFileDetail.class);
		Root<WOFileDetail> root = criteriaQuery.from(WOFileDetail.class);
		List<Predicate> filtersList = getPredicateListForWOFileDetail(criteriaBuilder, root, fileDetailWrapper);
		if(Utils.isValidList(filtersList)) {
			criteriaQuery.where(filtersList.toArray(new Predicate[] {}));
		}
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("modificationTime")));
		TypedQuery<WOFileDetail> typedQuery = getEntityManager().createQuery(criteriaQuery);
		if (lowerLimit != null && upperLimit != null) {
			typedQuery.setFirstResult(lowerLimit).setMaxResults(upperLimit - lowerLimit + 1);
		}
		return typedQuery.getResultList();
	}

	private List<Predicate> getPredicateListForWOFileDetail(CriteriaBuilder criteriaBuilder, Root<WOFileDetail> root,
			WOFileDetailWrapper fileDetailWrapper) {
		List<Predicate> list = new ArrayList();
		if (fileDetailWrapper != null) {
			list.add(fileDetailWrapper.getFileName() != null ?
					criteriaBuilder.like(root.get("fileName"),
							ForesightConstants.MODULUS + NVWorkorderUtils.escapeSpecialCharactersForCriteria(fileDetailWrapper.getFileName()) + ForesightConstants.MODULUS) :
					null);
			list.add(fileDetailWrapper.getDeleted() != null ?
					criteriaBuilder.equal(root.get("isDeleted"), fileDetailWrapper.getDeleted()) :
					null);
			list.add(fileDetailWrapper.getDeleted() != null && fileDetailWrapper.getDeleted() ?
					criteriaBuilder.isNull(root.get("isDeleted")) :
					null);
			if (Utils.isValidList(fileDetailWrapper.getProcessedStatusList())) {
				List<String> processedList = fileDetailWrapper.getProcessedStatusList();
				if(processedList.size() == ForesightConstants.ONE && processedList.contains(
						WORecipeMapping.ProcessStatus.COMPLETED.getValue())){
					list.add(criteriaBuilder.isTrue(root.get("isProcessed")));
				} else {
					List<WORecipeMapping.ProcessStatus> processStatusList = getProcessStatusList(
							fileDetailWrapper.getProcessedStatusList());
					list.add(Utils.isValidList(processStatusList) ?
							root.get("woRecipeMapping").get("fileProcessStatus").in(processStatusList) :
							null);
				}
			}
			list.add(fileDetailWrapper.getRecipeName() != null ?
					criteriaBuilder.like(root.get("woRecipeMapping").get("recipe").get("name"),
							ForesightConstants.MODULUS + NVWorkorderUtils.escapeSpecialCharactersForCriteria(fileDetailWrapper.getRecipeName()) + ForesightConstants.MODULUS) :
					null);
			list.add(fileDetailWrapper.getWoName() != null ?
					criteriaBuilder.like(root.get("woRecipeMapping").get("genericWorkorder").get("workorderName"),
							ForesightConstants.MODULUS + NVWorkorderUtils.escapeSpecialCharactersForCriteria(fileDetailWrapper.getWoName()) + ForesightConstants.MODULUS) :
					null);
			list.add(fileDetailWrapper.getStartSyncTime() != null ?
					criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("creationTime"),
							new java.util.Date(fileDetailWrapper.getStartSyncTime())) :
					null);
			list.add(fileDetailWrapper.getEndSyncTime() != null ?
					criteriaBuilder.lessThanOrEqualTo(root.<Date>get("creationTime"),
							new java.util.Date(fileDetailWrapper.getEndSyncTime())) :
					null);
		}
		list.removeIf(Objects::isNull);
		return list;
	}

	private List<WORecipeMapping.ProcessStatus> getProcessStatusList(List<String> processedStatusList) {
		List<WORecipeMapping.ProcessStatus> statusList = new ArrayList<>();
		for (String status : processedStatusList) {
			statusList.add(WORecipeMapping.ProcessStatus.valueOf(status));
		}
		return statusList;
	}

	@Override
	public Long getAllFilesCount(WOFileDetailWrapper wrapper) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<WOFileDetail> root = criteriaQuery.from(WOFileDetail.class);
		List<Predicate> filtersList = getPredicateListForWOFileDetail(criteriaBuilder, root, wrapper);
		criteriaQuery.select(criteriaBuilder.count(root));
		if(Utils.isValidList(filtersList)) {
			criteriaQuery.where(filtersList.toArray(new Predicate[] {}));
		}
		return getEntityManager().createQuery(criteriaQuery).getSingleResult();
	}

}
