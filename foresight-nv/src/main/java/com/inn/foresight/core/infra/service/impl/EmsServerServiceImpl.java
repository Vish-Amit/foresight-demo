package com.inn.foresight.core.infra.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.encoder.AESUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IEmsServerDao;
import com.inn.foresight.core.infra.model.EmsServer;
import com.inn.foresight.core.infra.service.IEmsServerService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.EMSType;
import com.inn.foresight.core.infra.utils.enums.Vendor;

/**
 * The Class EmsServerServiceImpl.
 */
@Service("EmsServerServiceImpl")
public class EmsServerServiceImpl extends AbstractService<Integer, EmsServer> implements IEmsServerService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(EmsServerServiceImpl.class);

	/** The ems server dao. */
	@Autowired
	private IEmsServerDao emsServerDao;

	/**
	 * Sets the dao.
	 *
	 * @param emsServerDao
	 *            the new dao
	 */
	public void setDao(IEmsServerDao emsServerDao) {
		super.setDao(emsServerDao);
		this.emsServerDao = emsServerDao;
	}

	@Override
	public List<Map<String, Object>> getEmsServer(Integer geographyId, String emsType, String geoType, String domain,
			String vendor, String technology) {
		logger.info(
				"Going to getIpListFromEmsServer for geoType {}, geographyId {}, domain {}, vendor {} and emsType {} and technology {}",
				geoType, geographyId, domain, vendor, emsType, technology);
		List<Map<String, Object>> listMap = new ArrayList<>();
		try {
			List<Object[]> ipObj = emsServerDao.getEmsServer(geographyId, emsType, geoType, domain, vendor, technology);
			for (Object[] obj : ipObj) {
				Map<String, Object> ipTracePortMap = new HashMap<>();
				if (obj[0] != null && obj[1] != null) {
					ipTracePortMap.put(InfraConstants.ID, obj[0]);
					ipTracePortMap.put(InfraConstants.IP, obj[1]);
				}
				listMap.add(ipTracePortMap);
			}
		} catch (Exception e) {
			logger.error("Error in getting getIpListFromEmsServer error {}", Utils.getStackTrace(e));
			throw new RestException(e);
		}
		logger.info("Returing a getIpListFromEmsServer size {}", listMap.size());
		return listMap;
	}

	@Override
	public List<Map<String, String>> getEmsNameAndIpByDomainAndVendor(String domain, String vendor, String technology) {
		logger.info("inside getEmsNameAndIpByDomainAndVendor with domain:{},vendor:{},technology:{}", domain, vendor,
				technology);
		List<Map<String, String>> listOfResultMap = new ArrayList<>();
		try {
			List<Object[]> emsServerList = emsServerDao.getEmsNameAndIpByDomainAndVendor(domain, vendor, technology);
			for (Object[] obj : emsServerList) {
				Map<String, String> resultMap = new HashMap<>();
				if (obj[0] != null && obj[1] != null) {
					String emsName = obj[0].toString();
					String ip = obj[1].toString();
					resultMap.put(ForesightConstants.EMS_NAME_CAMEL, emsName);
					resultMap.put(ForesightConstants.KEY_IP, ip);
					listOfResultMap.add(resultMap);
				}
			}
		} catch (Exception e) {
			logger.error("Error inside getEmsNameAndIpByDomainAndVendor Error:{}", ExceptionUtils.getStackTrace(e));
		}
		return listOfResultMap;
	}

	@Override
	public List<String> getDistinctEmsName() {
		logger.info("Going to get getDistincEmsName {}");

		try {
			return emsServerDao.getDistinctEmsName();
		} catch (Exception e) {

			logger.error("Exception inside the method getDistincEmsName {}", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<String> getDistinctEmsIP() {
		logger.info("Going to get getDistincEmsIP {}");
		try {
			return emsServerDao.getDistinctEmsIP();
		} catch (Exception e) {

			logger.error("Exception inside the method getDistincEmsIP {}", Utils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Gets the list of all ems server.
	 *
	 * @return the list of all ems server
	 */
	@Override
	public List<EmsServer> getEmsServerByVendorDomainAndEmsType(Vendor vendor, Domain domain, EMSType emsType) {
		try {
			logger.info("Going to get All Ems Server");
			return emsServerDao.getEmsServerByVendorDomainAndEmsType(vendor, domain, emsType);
		} catch (Exception e) {
			logger.error("Exception occurred while getting list of ems server :{}", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String createGCcredential(String username, String password, String ip) {
		this.logger.info("going to create new gc details in EMSServer ");
		EmsServer emsServer;
		try {
			emsServer = new EmsServer();
			emsServer.setCreatedTime(new Date(new java.util.Date().getTime()));
			emsServer.setUserName(username);
			emsServer.setPassWord(AESUtils.encrypt(password));
			emsServer.setIp(ip);
			emsServer.setEmsType(EMSType.CVIMMON);
			emsServer.setPort(ConfigUtil.getConfigProp("CVIMMON_PORT"));
			this.logger.info("going to create EMSServer Enteries in DB : {} ", emsServer);
			emsServer = (EmsServer) this.emsServerDao.create(emsServer);
			return ForesightConstants.SUCCESS;
		} catch (DaoException e) {
			logger.error("Exception occurred while create username,passowrd,ip :{}", Utils.getStackTrace(e));
		}
		return ForesightConstants.FAILURE;

	}
}