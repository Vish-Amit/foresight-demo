package com.inn.foresight.core.mylayer.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.mylayer.model.KmlProcessor;
import com.inn.foresight.core.mylayer.utils.KmlProcessorWrapper;

public interface IKmlProcessorDao extends IGenericDao<Integer, KmlProcessor>{

	KmlProcessor getKmlDataById(Integer id);

	List<KmlProcessorWrapper> getKmlData(Integer userid, Integer upperLimit, Integer lowerLimit);

	List<KmlProcessor> getListOfKMLBySearchTerm(Integer userid, Integer lowerLimit, Integer upperLimit,
			String kmlSearch);

	Long getCountsOfKML(Integer userid, String kmlSearch);

	boolean deleteKMLDetails(Integer userid, Integer id);

	KmlProcessor getKMLById(Integer userid, Integer id);

	boolean isKMLExist(Integer userid, String kmlName);

	List<String> getAllKMLNames(Integer userid, Integer id);

}
