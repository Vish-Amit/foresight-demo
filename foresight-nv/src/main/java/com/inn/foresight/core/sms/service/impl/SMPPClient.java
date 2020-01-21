package com.inn.foresight.core.sms.service.impl;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.encoder.AESUtils;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.sms.service.ISMPPClient;
import com.inn.foresight.core.sms.utils.SMPPConstants;
import com.inn.foresight.core.subscriber.service.ISubscriberSearch;

@Service("SMPPClient")
public class SMPPClient extends AbstractService<Integer, Object> implements ISMPPClient {

	private Logger logger = LogManager.getLogger(SMPPClient.class);

	@Override
	public String sendSMSByMsisdn(String msisdn, String message) {
		logger.info("Going to send sms by msisdn : {}", msisdn);
		String messageId = null;

		SMPPSession session = createSMSCSession();
		if (ISubscriberSearch.checkNullObject(session)) {
			try {
				messageId = session.submitShortMessage(SMPPConstants.CMT, TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, ConfigUtils.getString(SMPPConstants.SOURCE_NUMBER),
						TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, msisdn, new ESMClass(), (byte) 52, (byte) 1, null, null, new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT),
						(byte) 0, new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte) 0, message.getBytes(), new OptionalParameter[0]);
				logger.info("MESSAGE ID : {}", messageId);
			} catch (Exception e) {
				logger.error("Error in sending sms by msisdn : {}", Utils.getStackTrace(e));
			} finally {
				logger.info("In finally block ");
				closeSMPPSession(session);
				logger.info("after finally block");
			}
		} else {
			logger.info("Session object is null " + session);
		}
		return messageId;
	}

	private SMPPSession createSMSCSession() {
		logger.info("Creating SMSC session : {} ", new Date());
		SMPPSession sessionObj = new SMPPSession();
		String userInsession = null;
		try {
			userInsession = sessionObj.connectAndBind(AESUtils.decrypt(ConfigUtils.getString(SMPPConstants.SMSC_HOST)),
					Integer.valueOf(AESUtils.decrypt(ConfigUtils.getString(SMPPConstants.SMSC_PORT))),
					new BindParameter(BindType.BIND_TRX, AESUtils.decrypt(ConfigUtils.getString(SMPPConstants.SMSC_USERNAME)),
							AESUtils.decrypt(ConfigUtils.getString(SMPPConstants.SMSC_CHECKSUM)), null, TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, null));
			sessionObj.setTransactionTimer(ConfigUtils.getInteger(SMPPConstants.SMSC_TRANSACTION_TIMER));
			
			logger.info("SESSION CREATED FOR USER " + userInsession);
		} catch (Exception e) {
			logger.error("Error creating session : {}", Utils.getStackTrace(e));
		}
		return sessionObj;
	}
	
	private void closeSMPPSession(SMPPSession session) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				if (session != null) {
					session.unbindAndClose();
					logger.info("SMPP Session closed");
				}
			}
		};
		thread.start();
	}
}
