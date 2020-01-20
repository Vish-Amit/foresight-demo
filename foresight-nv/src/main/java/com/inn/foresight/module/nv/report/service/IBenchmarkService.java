package com.inn.foresight.module.nv.report.service;

import java.io.File;
import java.util.List;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;

public interface IBenchmarkService {

	Response execute(String json) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException;

	Response saveFileAndUpdateStatus(Integer analyticsrepoId, String filePath, GenericWorkorder genericWorkorder,
			File file, List<Integer> workOrderIds);

}
