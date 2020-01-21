package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.service.IIBSVisualisationService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.wrapper.IBSSelectionLayer;


@Service("IBSVisualisationServiceImpl")
public class IBSVisualisationServiceImpl  implements IIBSVisualisationService  {
	private Logger logger = LogManager.getLogger(IBSVisualisationServiceImpl.class);
	@Autowired
	private INetworkElementDao networkElementDao;
	
	
	@Override
	public List<Map> getIBSDetail(IBSSelectionLayer ibsSelectionLayer) {
		List<Tuple> ibsInfo = new ArrayList<>();
		List<Map> devices = new ArrayList<>();

		try {
			logger.info("Going to get data Data for IBS Info");
			if (ibsSelectionLayer.getValue().get("nelId").toString() != null) {
				ibsSelectionLayer.setFilters(getFilterForDeviceInfo(ibsSelectionLayer.getValue().get("nelId").toString()));

				ibsSelectionLayer.setProjection(getProjectionForIBSInfo());
				if (ibsSelectionLayer.getProjection() != null) {
					ibsInfo = networkElementDao.getNetworkElementDetails(ibsSelectionLayer.getFilters(), ibsSelectionLayer.getProjection(), null, null, false, false);
				}
				if (ibsInfo != null && !ibsInfo.isEmpty()) {
					devices = getMapFromTupleList(ibsInfo);
				} else {
					return new ArrayList<>();
				}
			} else {
				return new ArrayList<>();

			}
		} catch (Exception exception) {
			logger.info("Unable to get IBS Sites Info due to Exception {}", Utils.getStackTrace(exception));
			return new ArrayList<>();
		}
		return devices;
	}
	
	@Override
	public Map<Object, List<Map>> getDevicePositionDetails(IBSSelectionLayer ibsSelectionLayer) {
		try {
			List<Tuple> ibsInfo = new ArrayList<>();

			Map<Object, List<Map>> devices = new HashMap<>();
			if (ibsSelectionLayer.getValue().get("FloorId").toString() != null) {
				ibsSelectionLayer.setFilters(getFilterForFloorId(ibsSelectionLayer.getValue().get("FloorId").toString()));
			}
            logger.info("getProjectionForFloor :{}",getProjectionForFloor());
			ibsSelectionLayer.setProjection(getProjectionForFloor());
			
			if (ibsSelectionLayer.getProjection() != null) {
				ibsInfo = networkElementDao.getNetworkElementDetails(ibsSelectionLayer.getFilters(),
						ibsSelectionLayer.getProjection(), null, null, false, false);
			}
			List<Map> list = InfraUtils.getMapFromTupleList(ibsInfo);
			logger.info("list=======------:{}",list);
			logger.info("Data recieved for networkElement size  : {} ", ibsInfo);
	       devices = list.stream().collect(Collectors.groupingBy(s -> s.get(InfraConstants.NE_NETYPE_KEY), Collectors.toList()));
			logger.info("Devices found : {} ", devices);

			return devices;
		} catch (Exception exception) {
			logger.error("Unable to get data reagarding floor information due to exception {}", Utils.getStackTrace(exception));
			throw new RestException("something went wrong");
		}
	}
	
	//FloorPlanning
	public Map<String, List<String>> getProjectionForFloor() {
		Map<String, List<String>> projection = new HashMap<>();

			InfraUtils.setProjectionList(projection, "NetworkElement", "neType", "neName");
			InfraUtils.setProjectionList(projection, "CustomNEDetail", "coordinate");
			
		return projection;
	}
	//Floor
		public Map<String, List<Map>> getFilterForFloorId(String value) {
			Map<String, List<Map>> filters = new HashMap<>();
			List<Map> filter1 = new ArrayList<>();
			InfraUtils.setFilterMap(filters, filter1, "Floor", "id", value, "Integer", InfraConstants.EQUALS_OPERATOR);
			return filters;
		}
	
	public Map<String, List<String>> getProjectionForIBSInfo() {
		Map<String, List<String>> projection = new HashMap<>();

			InfraUtils.setProjectionList(projection, "NetworkElement", "neName", "neType", "neStatus", "latitude", "longitude","vendor");
			InfraUtils.setProjectionList(projection, "Floor", "floorNumber");
			InfraUtils.setProjectionList(projection, "CustomNEDetail", "coordinate");
			logger.info("projection :{}",projection);
		return projection;
	}
	
	
	public Map<String, List<Map>> getFilterForIBSInfo(String value) {
		Map<String, List<Map>> filters = new HashMap<>();
		List<Map> filter1 = new ArrayList<>();
		InfraUtils.setFilterMap(filters, filter1, "NetworkElement", "neName", value, "String",
				InfraConstants.EQUALS_OPERATOR);
		return filters;
	}
	
	
	
	public Map<String, List<String>> getProjectionForDeviceInfo() {
		Map<String, List<String>> projection = new HashMap<>();

			InfraUtils.setProjectionList(projection, "NetworkElement", "neName", "neType", "neStatus",InfraConstants.NE_NEID_KEY);
			
		return projection;
	}
	
	public Map<String, List<Map>> getFilterForDeviceInfo(String value) {
		Map<String, List<Map>> filters = new HashMap<>();
		List<Map> filter1 = new ArrayList<>();
		InfraUtils.setFilterMap(filters, filter1, "NELocation", "nelId", value, "String", InfraConstants.EQUALS_OPERATOR);
		return filters;
	}
	
	
	
	
	@Override
	public Map getDeviceDetail(IBSSelectionLayer ibsSelectionLayer) {
		List<Tuple> ibsInfo = new ArrayList<>();
		Map deviceInfo = new HashMap<>();
		try {
			List<Map> devices = new ArrayList<>();

			logger.info("Going to get data Data for IBS Info");
			if (ibsSelectionLayer.getValue().get("nelId").toString() != null) {
				ibsSelectionLayer.setFilters(getFilterForDeviceInfo(ibsSelectionLayer.getValue().get("nelId").toString()));
			} else {
				return new HashMap<>();
			}
			ibsSelectionLayer.setProjection(getProjectionForDeviceInfo());
			if (ibsSelectionLayer.getProjection() != null) {
				ibsInfo = networkElementDao.getNetworkElementDetails(ibsSelectionLayer.getFilters(), ibsSelectionLayer.getProjection(), null, null, true,
						true);
			}

			Map<Object, Long> netypeCount = ibsInfo.stream()
					.collect(Collectors.groupingBy(s -> String.valueOf(s.get(InfraConstants.NE_NETYPE_KEY)) +
							String.valueOf(s.get(InfraConstants.NE_NESTATUS_KEY)), Collectors.counting()));
			Map<Object, Long> nestatusCount = ibsInfo.stream().collect(Collectors.groupingBy(s -> s.get(InfraConstants.NE_NESTATUS_KEY),
					Collectors.counting()));

			// Code
			if (ibsInfo != null && !ibsInfo.isEmpty()) {
				devices = InfraUtils.getMapFromTupleList(ibsInfo);
			} else {
				return new HashMap<>();
			}
			deviceInfo.put("DEVICES", devices);
			deviceInfo.put("NETYPE_WISE_COUNT", netypeCount);
			deviceInfo.put("NESTATUS_WISE_COUNT", nestatusCount);
			deviceInfo.put("TOTAL_COUNT", devices.size());

		} catch (Exception exception) {
			logger.info("Unable to get Device Info due to Exception {}",Utils.getStackTrace(exception));
			throw new RestException("Something went wrong");
		}
		return deviceInfo;
	}
	
	public static List getMapFromTupleList(List<Tuple> tuples) {
		List<Map> resultList = new ArrayList<>();
		for (Tuple tuple : tuples) {
			Map map = new HashMap<>();
			int i = 0;
			for (TupleElement<?> tupleElement : tuple.getElements()) {
				if (tupleElement.getAlias().equalsIgnoreCase(InfraConstants.NE_NETYPE_KEY)) {
					String netype = String.valueOf(tuple.get(i)).substring(0, String.valueOf(tuple.get(i)).lastIndexOf("_"));
					map.put(tupleElement.getAlias(), netype);

				} else {
					map.put(tupleElement.getAlias(), tuple.get(i));
				}
				i++;
			}
			resultList.add(map);

		}
		return resultList;
	}

}
