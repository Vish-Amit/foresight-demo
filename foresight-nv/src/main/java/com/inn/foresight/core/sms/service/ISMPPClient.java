package com.inn.foresight.core.sms.service;

import com.inn.core.generic.service.IGenericService;

public interface ISMPPClient extends IGenericService<Integer, Object> {

	String sendSMSByMsisdn(String msisdn, String message);

}
