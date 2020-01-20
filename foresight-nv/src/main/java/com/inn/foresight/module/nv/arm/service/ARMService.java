package com.inn.foresight.module.nv.arm.service;

import java.io.IOException;
import java.io.InputStream;

import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.module.nv.arm.wrapper.ARMRequestWrapper;

public interface ARMService {

	/**
	 * @param wrapper
	 * @param uLimit
	 * @param lLimit
	 * @return
	 * @throws ValueNotFoundException
	 */
	public Object getAppData(ARMRequestWrapper wrapper,Integer lLimit,Integer uLimit) throws ValueNotFoundException;

	/**
	 * @param wrapper
	 * @return
	 * @throws IOException 
	 */
	public String updateAppDetails(ARMRequestWrapper wrapper) throws IOException;
	
	
	/**
	 * @param id
	 * @param inputFile
	 * @param appType
	 * @return
	 */
	public String uploadAPK(Integer id,InputStream inputFile,String appType);

}
