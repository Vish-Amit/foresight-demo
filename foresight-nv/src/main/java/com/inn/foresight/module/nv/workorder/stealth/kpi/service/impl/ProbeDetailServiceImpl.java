package com.inn.foresight.module.nv.workorder.stealth.kpi.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpPostRequest;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskResultDao;
import com.inn.foresight.module.nv.workorder.stealth.kpi.dao.ProbeDetailDao;
import com.inn.foresight.module.nv.workorder.stealth.kpi.dao.ProbeDetailHbaseDao;
import com.inn.foresight.module.nv.workorder.stealth.kpi.model.ProbeDetail;
import com.inn.foresight.module.nv.workorder.stealth.kpi.service.ProbeDetailService;
import com.inn.foresight.module.nv.workorder.stealth.kpi.wrapper.ProbeDetailWrapper;

@Service("ProbeDetailServiceImpl")
public class ProbeDetailServiceImpl implements ProbeDetailService {

	@Autowired
	ProbeDetailDao probeDetailDao;

	@Autowired
	ProbeDetailHbaseDao probeDetailHbaseDao;
	
	@Autowired
	IStealthTaskResultDao stealthTaskResultDao;

	/** The logger. */
	private Logger logger = LogManager.getLogger(ProbeDetailServiceImpl.class);

	@Override
	@Transactional
	public String updateEventOccurance(ProbeDetailWrapper wrapper) {
	
		try {
			if (wrapper != null && Utils.hasValidValue(wrapper.getKpi())) {
				ProbeDetail probeDetail = probeDetailDao.findProbeDetailByKpi(wrapper.getStealthTaskResultId(),
						wrapper.getKpi());
				probeDetail = updateValues(probeDetail, wrapper);
				logger.info("Data of Probe {}", probeDetail);
				probeDetailDao.update(probeDetail);
				if (probeDetail.getKpi().equalsIgnoreCase(StealthConstants.CONNECTIVITY_KPI)) {
					return persistDataIntoHbase(wrapper);
				} else {
					return ForesightConstants.SUCCESS_JSON;
				}
			} else {
				return ForesightConstants.FAILURE_JSON;
			}
		} catch (Exception e) {
			logger.error("Exception in updateEventOccurance {}", Utils.getStackTrace(e));
		}
		return ForesightConstants.FAILURE_JSON;
	}

	ProbeDetail updateValues(ProbeDetail probeDetail, ProbeDetailWrapper wrapper) {
		if (probeDetail == null) {
			probeDetail = new ProbeDetail();
			probeDetail.setCreationTime(new Date());
			probeDetail.setTaskResult(stealthTaskResultDao.findByPk(wrapper.getStealthTaskResultId()));
		}
		probeDetail.setModificationTime(new Date(wrapper.getTimeStamp()));

		if(wrapper.getKpi()!=null && !wrapper.getKpi().equalsIgnoreCase(StealthConstants.CONNECTIVITY_KPI)) {
			probeDetail.setIsNotified(Boolean.TRUE);
		}
		else {
			if(probeDetail.getIsNotified()) {
			}
			else {
				probeDetail.setLastNotifiedTime(new Date());		
			}
		}
		probeDetail.setCounter(0);
		probeDetail.setKpi(wrapper.getKpi());
		if(wrapper.getLatitude()!=null) {
		probeDetail.setLatitude(wrapper.getLatitude());
		}
		if(wrapper.getLongitude()!=null) {
		probeDetail.setLongitude(wrapper.getLongitude());
		}
		probeDetail.setCgi(wrapper.getCgi());
		probeDetail.setModificationTime(new Date());
		probeDetail.setPci(wrapper.getPci());
		probeDetail.setValue(wrapper.getValue());
		probeDetail.setRsrp(wrapper.getRsrp());
		probeDetail.setSinr(wrapper.getSinr());
		if(wrapper.getAddress()!=null) {
			probeDetail.setAddress(wrapper.getAddress());			
		}

		probeDetail.setEnodeBId(wrapper.getEnodeBId());
		probeDetail.setCellId(wrapper.getCellId());
		return probeDetail;
	}

	@Override
	public String persistDataIntoHbaseMicroService(ProbeDetailWrapper wrapper) {
		return probeDetailHbaseDao.persistDataForProbe(wrapper);
	}

	public String persistDataIntoHbase(ProbeDetailWrapper wrapper){
		try {
			String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue()) + StealthConstants.PERSIST_PROBE_DATA_MICROSERVICE_URL;

			logger.info("Micro Service URL {}", url);
			StringEntity stringEntity = new StringEntity(new Gson().toJson(wrapper),
					ContentType.APPLICATION_JSON);
			return new HttpPostRequest(url, stringEntity).getString();
		} catch (HttpException e) {
			logger.error(ForesightConstants.LOG_EXCEPTION + Utils.getStackTrace(e));

		}
		return ForesightConstants.FAILURE_JSON;
	
	}
}
