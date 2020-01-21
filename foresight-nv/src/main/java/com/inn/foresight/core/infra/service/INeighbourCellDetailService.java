package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.NeighbourCellDetail;

public interface INeighbourCellDetailService extends IGenericService<Integer, NeighbourCellDetail>{

	public Map<String, List<NeighbourCellDetail>> getNeighbourCellDetailsForSourceCells(List<String> cellName, Integer weekno);

	List<Map> getNeighbourCellDetails(String sourceSiteId, Integer sourceCellId, String sourceFrequencyBand,String vendor, String domain);

}
