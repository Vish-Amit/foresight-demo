package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.NeighbourCellDetail;
import com.inn.foresight.core.infra.model.RANDetail;

public interface INeighbourCellDetailDao  extends IGenericDao<Integer, NeighbourCellDetail>{

	public List<NeighbourCellDetail> getNeighbourCellDetailsForSourceCells(List<String> cellName, Integer weekNo);

	List<RANDetail> getNeighbourCellDetails(List<Integer> networkElementidList, String vendor, String domain);

	List<Integer> getNeighbourCellDetailIdByNEId(Integer networkelementid_fk);

}
