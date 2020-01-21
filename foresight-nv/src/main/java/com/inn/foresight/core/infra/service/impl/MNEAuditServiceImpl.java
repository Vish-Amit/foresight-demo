package com.inn.foresight.core.infra.service.impl;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.MNEAuditDao;
import com.inn.foresight.core.infra.model.MNEAudit;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.service.MNEAuditService;
import com.inn.product.um.user.model.User;

@Service("MNEAuditServiceImpl")
public class MNEAuditServiceImpl extends AbstractService<Long, MNEAudit> implements MNEAuditService{
	/** The logger. */
	private static Logger logger = LogManager
			.getLogger(MNEAuditServiceImpl.class);
	
	@Autowired
	private MNEAuditDao mNEAuditDao;

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void createMNEAuditParameter(NetworkElement networkElement, String remark, User user) {
		try {
				MNEAudit mneAudit = new MNEAudit();
				if (user != null) {
					mneAudit.setCreatedBy(user);
					mneAudit.setModifiedBy(user);
				}
				if (networkElement != null) {
					mneAudit.setNetworkElement(networkElement);
					if(!remark.contains("Deleted")) {
					mneAudit.setHistory(getJsonFromObject(networkElement));
					}
				}
				mneAudit.setCreationTime(new Date());
				mneAudit.setModificationTime(new Date());
				mneAudit.setRemark(remark);
				mNEAuditDao.create(mneAudit);
		} catch (Exception e) {
			logger.error("EXception inside createMNEAuditParameter : {}", Utils.getStackTrace(e));
		}
	
		
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void createMNEAuditParameterForStation(Object station, String remark, User user) {
		try {
				MNEAudit mneAudit = new MNEAudit();
				if (user != null) {
					mneAudit.setCreatedBy(user);
					mneAudit.setModifiedBy(user);
				}
				if (station != null&&!remark.contains("Deleted")) {
					mneAudit.setHistory(getJsonFromObject(station));
				}
				mneAudit.setCreationTime(new Date());
				mneAudit.setModificationTime(new Date());
				mneAudit.setRemark(remark);
				mNEAuditDao.create(mneAudit);
		} catch (Exception e) {
			logger.error("EXception inside createMNEAuditParameterForStation : {}", Utils.getStackTrace(e));
		}
	
		
	}
	private String getJsonFromObject(Object networkElement) {
		String oldNetworkElementData = null;
		if (networkElement != null) {
			ObjectMapper obj = new ObjectMapper();
			obj.enable(SerializationFeature.INDENT_OUTPUT);
			try {
				oldNetworkElementData = obj.writeValueAsString(networkElement);
			} catch (Exception e) {
				logger.error("Exception inside getJsonFromObject : {}", Utils.getStackTrace(e));
			}
		}
		return oldNetworkElementData;
	}
}
