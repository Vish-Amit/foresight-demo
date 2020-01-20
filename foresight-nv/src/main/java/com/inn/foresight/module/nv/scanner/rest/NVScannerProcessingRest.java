package com.inn.foresight.module.nv.scanner.rest;

import javax.ws.rs.core.Response;

public interface NVScannerProcessingRest {	
	public Response getCSVDumpFromHDFS(Integer woId,Integer recipeId);
}
