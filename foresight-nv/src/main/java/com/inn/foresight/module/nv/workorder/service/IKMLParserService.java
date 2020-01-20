package com.inn.foresight.module.nv.workorder.service;

import javax.servlet.http.HttpServletRequest;

public interface IKMLParserService {

	String parseKMLAndGetBoundary(HttpServletRequest request);

	String parseTabAndKMLAndGetBoundary(HttpServletRequest httpServletRequest);

	
}
