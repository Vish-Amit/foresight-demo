package com.inn.foresight.module.nv.inbuilding.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.AdvanceSearchResult;
import com.inn.core.generic.utils.QueryObject;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.service.IAdvanceSearchService;
import com.inn.foresight.core.infra.service.IBuildingDataService;

@Service("ModuleSearchServiceImpl")
public class ModuleSearchServiceImpl implements IAdvanceSearchService {

	@Autowired
	private IBuildingDataService iBuildingDataService;

	@Override
	public AdvanceSearchResult<AdvanceSearch> advanceSearch(QueryObject queryObject) {
		return null;
	}

	@Override
	public List<AdvanceSearch> search(AdvanceSearch entity) {
		return Collections.emptyList();
	}

	@Override
	public AdvanceSearch findById(Integer primaryKey) {
		return null;
	}

	@Override
	public List<AdvanceSearch> findAll() {
		return Collections.emptyList();
	}

	@Override
	public AdvanceSearch create(AdvanceSearch anEntity) {
		return null;
	}

	@Override
	public AdvanceSearch update(AdvanceSearch anEntity) {
		return null;
	}

	@Override
	public void remove(AdvanceSearch anEntity) {
	}

	@Override
	public void removeById(Integer primaryKey) {
	}

	@Override
	public List<AdvanceSearch> searchWithLimit(SearchContext context, Integer maxLimit, Integer minLimit) {
		return Collections.emptyList();
	}

	@Override
	public List<AdvanceSearch> searchWithLimitAndOrderBy(SearchContext ctx, Integer maxLimit, Integer minLimit, String orderby, String orderType) {
		return Collections.emptyList();
	}

	@Override
	public List<JSONObject> findAudit(Integer pk) {
		return Collections.emptyList();
	}

	@Override
	public int searchRecordsCount(SearchContext ctx) {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map searchToMapData(Integer buildingId) {
		return iBuildingDataService.getBuildingDetailsForAdvanceSearch(buildingId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getAddressFromGoogle(String arg0) {
		return null;
	}

	@Override
	public List<AdvanceSearch> getAdvanceSearchByName(String arg0) {
		return Collections.emptyList();
	}

	@Override
	public List<AdvanceSearch> getAdvanceSearchByTypeList(String arg0, List<String> arg1) {
		return Collections.emptyList();
	}

	@Override
	public List<AdvanceSearch> getAdvanceSearchForIPTopology(String arg0) {
		return Collections.emptyList();
	}

	@Override
	public Object searchForMapData(Integer arg0, Integer arg1) {
		return null;
	}

	@Override
	public List<String> searchNeIdByNeType(String arg0, String arg1) {
		return Collections.emptyList();
	}

	@Override
	public Map<String, Object> searchDetails(Map<String, Object> map) {
		return null;

	}

	@Override
	public List<AdvanceSearch> getAdvanceSearchByTypeListAndVendor(String name, Map<String, List<String>> map) throws RestException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void createAdvanceSearch(NetworkElement arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<AdvanceSearch> getAdvanceSearchDetails(String name, List<String> typeList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AdvanceSearch> getAdvanceSearchByTypeList(Map map) {
		return new ArrayList<>();
	}

}
