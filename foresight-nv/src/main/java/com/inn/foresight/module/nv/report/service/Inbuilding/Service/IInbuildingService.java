package com.inn.foresight.module.nv.report.service.Inbuilding.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;

public interface IInbuildingService {

	Response createFloorLevelInbuildingReport(Integer workorderId, GenericWorkorder workOrder, Integer analyticsrepoId,
			Map<String, Object> jsonMap);

	Response createBuildingLevelInbuildingReport(Integer BuildingId, List<Integer> workorderList,
			Integer analyticsrepoId, String assignto, Integer projectId, Map<String, Object> jsonMap);

	Response updateStatusAndPersistBuildingReport(Map<String, Object> jsonMap, File file, Integer analyticsId, String destinationFilePath)
			throws UnsupportedCharsetException, IOException;

	Response updateStatusAndPersistReport(GenericWorkorder workorderObj, Map<String, Object> jsonMap, File file,
			Integer analyticsId,String filepath) throws UnsupportedCharsetException, IOException;
}
