

package com.inn.foresight.core.ztepower.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.service.MNEAuditService;
import com.inn.foresight.core.ztepower.constants.ZTEPowerConstants;
import com.inn.foresight.core.ztepower.dao.IZTEPowerRegionDao;
import com.inn.foresight.core.ztepower.dao.IZTEPowerStationDao;
import com.inn.foresight.core.ztepower.model.ZTEPowerStation;
import com.inn.foresight.core.ztepower.service.IZTEPowerService;
import com.inn.foresight.core.ztepower.wrapper.ZTEPowerRequestWrapper;
import com.inn.foresight.core.ztepower.wrapper.ZTEPowerResultWrapper;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.UserContextService;

@Service("ZTEPowerServiceImpl")
public class ZTEPowerServiceImpl  implements IZTEPowerService {
	Logger logger = LogManager.getLogger(ZTEPowerServiceImpl.class);
	
	@Autowired
	private IZTEPowerRegionDao iZTEPowerRegionDao;
	
	@Autowired 
	private INetworkElementDao iNetworkElementDao;
	
	@Autowired 
	private IZTEPowerStationDao ztePowerStationDaoImpl;
	
	@Autowired
	private MNEAuditService mNEAuditService;
	
	@Autowired
	private UserContextService userInContext;

	@Override
	public List<ZTEPowerResultWrapper> getZTEPowerSummaryCount(ZTEPowerRequestWrapper wrapper) {
		logger.info("Going to getZTEPowerSummaryCount for type : {}",wrapper.getType());
		if(ZTEPowerConstants.TYPE_AREA.equalsIgnoreCase(wrapper.getType())) {
			return getZTEPowerAreaWiseSummaryCount(wrapper);
		}else if(ZTEPowerConstants.TYPE_STATION.equalsIgnoreCase(wrapper.getType())) {
			return getZTEPowerStationWiseSummaryCount(wrapper);
		}else if(ZTEPowerConstants.TYPE_DEVICE.equalsIgnoreCase(wrapper.getType())) {
			return getZTEPowerDeviceWiseSummaryCount(wrapper);
		}
		return Collections.emptyList();
	}

	private List<ZTEPowerResultWrapper> getZTEPowerAreaWiseSummaryCount(ZTEPowerRequestWrapper wrapper) {
		return iZTEPowerRegionDao.getZTEPowerAreaWiseCount(wrapper);
	}
	private List<ZTEPowerResultWrapper> getZTEPowerStationWiseSummaryCount(ZTEPowerRequestWrapper wrapper) {
		return iZTEPowerRegionDao.getZTEPowerStationWiseCount(wrapper);
	}
	private List<ZTEPowerResultWrapper> getZTEPowerDeviceWiseSummaryCount(ZTEPowerRequestWrapper wrapper) {
		return iZTEPowerRegionDao.getZTEPowerDeviceWiseCount(wrapper);
	}

	@Override
	public Map<String, Object> getZTEPowerData(ZTEPowerRequestWrapper wrapper) {
		try {
			return iZTEPowerRegionDao.getZTEPowerData(wrapper);
		} catch (Exception e) {
			logger.error("Exception while getting getting ZTEPowerData : {} ", Utils.getStackTrace(e));
			throw e;
		}
	}

	@Override
	public List<String> searchZTEPowerFields(ZTEPowerRequestWrapper wrapper) {
		try {
				return iZTEPowerRegionDao.searchZTEPowerFields(wrapper);
		} catch (Exception e) {
			logger.error("Exception while searchZTEPowerFields : {} ", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@Override
	@Transactional
	public String deleteZTEPowerData(ZTEPowerRequestWrapper wrapper) {
		String responseToReturn=ForesightConstants.SUCCESS_JSON;
		try {
		if(ZTEPowerConstants.TYPE_STATION.equalsIgnoreCase(wrapper.getType())) {
			deleteSatation(wrapper);
		}else if(ZTEPowerConstants.TYPE_DEVICE.equalsIgnoreCase(wrapper.getType())) {
			deleteDevice(wrapper);
		}
		}catch(Exception e) {
			responseToReturn=ForesightConstants.FAILURE_JSON;
			logger.error("Exception inside deleteZTEPowerData fot type {} :  ,ERROR:",wrapper.getType(),Utils.getStackTrace(e));
		}
		return responseToReturn;
	}

	private void deleteDevice(ZTEPowerRequestWrapper wrapper) {
		User user = userInContext.getUserInContextnew();
		for(Integer id:wrapper.getIdList()) {
			NetworkElement ne = iNetworkElementDao.findByPk(id);
			if(ne!=null) {
				ne.setIsDeleted(Boolean.TRUE);
				mNEAuditService.createMNEAuditParameter(ne, String.format(ZTEPowerConstants.REMARK_DELETED_FOR_DEVICE, ne.getNeName(),ne.getNeId()), user);
				iNetworkElementDao.update(ne);
			}
				
		}
	}

	private void deleteSatation(ZTEPowerRequestWrapper wrapper) {
		User user = userInContext.getUserInContextnew();
		for(Integer id:wrapper.getIdList()) {
			ZTEPowerStation s = ztePowerStationDaoImpl.findByPk(id);
			if(s!=null) {
				s.setIsDeleted(Boolean.TRUE);
				mNEAuditService.createMNEAuditParameterForStation(s, String.format(ZTEPowerConstants.REMARK_DELETED_FOR_STATION, s.getStationName(),s.getStationId()), user);
				ztePowerStationDaoImpl.update(s);
			}
				
		}
	}

	



	


}
