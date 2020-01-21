package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.INELocationParameterDao;
import com.inn.foresight.core.infra.model.NELocationParameter;
import com.inn.foresight.core.infra.service.INELocationParameterService;

/**
 * The Class NELocationParameterServiceImpl.
 */
@Service("NELocationParameterServiceImpl")
public class NELocationParameterServiceImpl extends AbstractService<Integer, NELocationParameter>
		implements INELocationParameterService {

	

	/** The logger. */
	private static Logger logger = LogManager.getLogger(NELocationParameterServiceImpl.class);

	/** The n E location dao. */
	@Autowired
	private INELocationParameterDao neLocationParameterDao;

	/**
	 * Sets the dao.
	 *
	 * @param nELocationDao
	 *            the new dao
	 */
	public void setDao(INELocationParameterDao nELocationDao) {
		super.setDao(nELocationDao);
		this.neLocationParameterDao = nELocationDao;
	}

	@Override
	public Map<String, Object> getParameterAndKeyByNeLocationId(Integer nelocationId) {
		Map<String, Object> neLocationParameterKeyValue = new HashMap<>();
		try {
			logger.info("Going top get nelocation key value parameter for nelocationId {} ", nelocationId);
			List<Object[]> parameterAndKeyByNeLocationId = neLocationParameterDao
					.getParameterAndKeyByNeLocationId(nelocationId);
			for (Object[] objects : parameterAndKeyByNeLocationId) {
				neLocationParameterKeyValue.put(String.valueOf(objects[0]), objects[1]);
			}

		} catch (Exception exception) {
			logger.error("Unable to get nelocation key value parameter  For nelocationId {} due to exception : {}",
					nelocationId, Utils.getStackTrace(exception));
		}

		return neLocationParameterKeyValue;
	}

	@Override
	public List<Map<String, Object>> getAllGCList(String name,String status,Integer llimit, Integer ulimit) {
		List<Map<String, Object>> gcListOfMap = new ArrayList<>();
		try {
			logger.info("Going top get nelocation key value parameter for nelocationId {}");
			List<Object[]> gcList = neLocationParameterDao.getGCList(name,status,llimit, ulimit);
			for (Object[] gcObject : gcList) {
				Map<String, Object> gcListMap = new HashMap<>();
				gcListMap.put("gcType", gcObject[0]);
				gcListMap.put(ForesightConstants.NEL_ID, gcObject[1]);
				gcListMap.put(ForesightConstants.TECHNOLOGY, gcObject[2]);
				gcListMap.put(ForesightConstants.VENDOR, gcObject[3]);
				gcListMap.put(ForesightConstants.DOMAIN, gcObject[4]);
				gcListMap.put(ForesightConstants.ADDRESS, gcObject[5]);
				gcListMap.put(ForesightConstants.PINCODE, gcObject[6]);
				gcListMap.put(ForesightConstants.LOCATION_CODE, gcObject[7]);
				gcListMap.put(ForesightConstants.FRIENDLY_NAME, gcObject[8]);
				gcListMap.put(ForesightConstants.ID, gcObject[9]);
				gcListMap.put(ForesightConstants.STATUS, gcObject[10]);
				gcListMap.put(ForesightConstants.CLUSTER, gcObject[11]);
				gcListMap.put(ForesightConstants.CREATED_TIME, gcObject[12]);
				gcListMap.put(ForesightConstants.MODIFIEDTIME, gcObject[13]);
				gcListMap.put("managedByCdc", gcObject[14]);
				gcListOfMap.add(gcListMap);
			}

		} catch (Exception exception) {
			logger.error("Unable to getAllGCList   due to exception : {}",
					Utils.getStackTrace(exception));
		}
		return gcListOfMap;
	}
	
	@Override
	public Long getAllGCTotalCount(String name,String status) {
		
		try {
			logger.info("Going top get nelocation key value parameter for nelocationId {}");
			return neLocationParameterDao.getGCCount(name,status);

		} catch (Exception exception) {
			logger.error("Unable to getAllGCList   due to exception : {}",
					Utils.getStackTrace(exception));
			 throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
		
	}
	
	@Override
	public Map<String,NELocationParameter> getNELocationParameterMapByParameterList(Integer id, List<String> parameterList) {
		Map<String, NELocationParameter> gcListOfMap = new HashMap<>();
		try {
			logger.info("Going top get nelocation key value parameter for nelocationId {}");
			List<NELocationParameter> neLocationParameterByParameterList = neLocationParameterDao.getValuesByIdAndParameterList(id,parameterList);
            for (NELocationParameter neLocationParameter : neLocationParameterByParameterList) {
            	gcListOfMap.put(neLocationParameter.getParameterName(), neLocationParameter);
			}
		} catch (Exception exception) {
			logger.error("Unable to getAllGCList   due to exception : {}",
					Utils.getStackTrace(exception));
			 throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
		return gcListOfMap;
	}

	
	
	
	

}