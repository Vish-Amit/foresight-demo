package com.inn.foresight.core.mylayer.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.mylayer.model.UserPolygon;

public interface IUserPolygonDao extends IGenericDao<Integer, UserPolygon> {

	boolean deletePolygonById(Integer userid, Integer id);

	UserPolygon getPolygonById(Integer userid, Integer id);

	Long getCountsOfPolygon(Integer userid,String polygonSearch);

	boolean isPolygonExist(Integer userid, String polygonName);

	List<UserPolygon> getListOfPolygon(Integer userid, Integer lowerLimit, Integer upperLimit, String polygonSearch);

	boolean isNewPolygonNameAlreadyExist(Integer userid, String polygonName, Integer id);

}
