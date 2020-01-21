package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import com.inn.foresight.core.infra.wrapper.IBSSelectionLayer;

public interface IIBSVisualisationService {

	List<Map> getIBSDetail(IBSSelectionLayer ibsSelectionLayer);

	Map getDeviceDetail(IBSSelectionLayer ibsSelectionLayer);

	Map<Object, List<Map>> getDevicePositionDetails(IBSSelectionLayer ibsSelectionLayer);


}
