package com.inn.foresight.module.nv.scanner.service;

import java.io.IOException;

import javax.ws.rs.core.Response;

import com.inn.commons.http.HttpException;

public interface NVScannerProcessingService {
	
public Response getCSVDumpFromMicroService(Integer workorderId, Integer recipeId) throws HttpException, IOException;

public byte[] getScannerDumpDataFromHbase(Integer workorderId, Integer recipeId);


}
