package com.inn.foresight.module.nv.sitesuggestion.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.sitesuggestion.model.FriendlySiteSuggestion;

public interface ISiteSuggestionDao extends IGenericDao<Integer, FriendlySiteSuggestion>{

	List<FriendlySiteSuggestion> getSiteAcquisitionLayerData(String fromDate, String toDate, String buildingType,
			String siteType);

}
