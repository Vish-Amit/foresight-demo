package com.inn.foresight.core.generic.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.service.IMD5GenerateService;
import com.inn.foresight.core.generic.utils.Utils;

@Service("MD5GenerateServiceImpl")
public class MD5GenerateServiceImpl implements IMD5GenerateService{

	@Override
	public Map<String, String> getMd5HashForUri(String uri) {
		return Utils.getMd5HashForUri(uri);
	}

}
