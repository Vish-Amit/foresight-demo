package com.inn.foresight.module.nv.core.workorder.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.core.workorder.constants.GenericWorkorderConstants;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status;
import com.inn.foresight.module.nv.core.workorder.service.IGenericWorkorderService;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/** The Class GenericWorkorderServiceImpl. */
@Service("GenericWorkorderServiceImpl")
public class GenericWorkorderServiceImpl extends AbstractService<Integer, GenericWorkorder> implements IGenericWorkorderService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(GenericWorkorderServiceImpl.class);

	/** The dao. */
	@Autowired
	private IGenericWorkorderDao dao;
	
	/**
	 * Inserts a new Data In GenericWorkorder after adding some common Data (Like
	 * Creating Date and User in context).
	 *
	 * @param workorder the workorder
	 * @return Newly created GenericWorkorder
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public GenericWorkorder createWorkorder(GenericWorkorder workorder) {
		try {
			if(workorder.getCreatedBy() == null) {
				User user = UserContextServiceImpl.getUserInContext();
				workorder.setCreatedBy(user);
				workorder.setModifiedBy(user);
			}
			workorder.setStatus(Status.NOT_STARTED);
			workorder.setDeleted(false);
			Date date = new Date();
			workorder.setCreationTime(date);
			workorder.setModificationTime(date);
			workorder.setCompletionPercentage(NumberUtils.DOUBLE_ZERO);
			return dao.create(workorder);
		} catch (Exception e) {
			logger.error("Inside @class GenericWorkorderServiceImpl @Method createWorkorder Exception {}",
					ExceptionUtils.getStackTrace(e));
			throw new RestException(
					ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, GenericWorkorderConstants.GENERIC_WORKORDER, e));
		}
	}

	/**
	 * Gets the List of all Generic Workorder Id which contains the passed partial
	 * workorder Id.
	 * 
	 * @param partialWorkorderId the workorder name
	 * @return List<String> Workorder names from Db which contains(workorderName)
	 * @throws RestException the rest exception
	 */
	@Override
	public List<String> getWorkorderIdListByWOId(String partialWorkorderId) {
		try {
			return dao.getWorkorderIdListFromWOId(partialWorkorderId);
		} catch (Exception e) {
			logger.error("Inside @class GenericWorkorderServiceImpl @Method getWorkorderNameListByWOName Exception {}",
					ExceptionUtils.getStackTrace(e));
			throw new RestException(
					ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, "GenericWorkorder", e));
		}
	}

	/**
	 * Gets GenericWorkorder object in which the workorderName contains the passed
	 * Workorder Name.
	 *
	 * @param workorderId the workorder name
	 * @return GenericWorkorder object from Db which contains(workorderName)
	 * @throws RestException the rest exception
	 */
	@Override
	public GenericWorkorder getWorkorderByWOId(String workorderId) {
		try {
			return dao.getWorkorderByWOId(workorderId);
		} catch (Exception e) {
			logger.error("Inside @class GenericWorkorderServiceImpl @Method getWorkorderByWOName Exception {}",
					ExceptionUtils.getStackTrace(e));
			throw new RestException(
					ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, "GenericWorkorder", e));
		}
	}

}
