package com.inn.foresight.core.mylayer.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.mylayer.model.UserPolygon;
import com.inn.product.um.user.model.User;

public interface IUserPolygonService  extends IGenericService<Integer, UserPolygon>{


	UserPolygon insertPolygonDetail(User user, UserPolygon userPolygon);

	boolean deletePolygonById(Integer userid, Integer id);

	Map getPolygonById(Integer userid, Integer id);

	String exportPolygonInKML(Integer id);

	List<Map> getListOfPolygon(Integer userid, Integer lowerLimit, Integer upperLimit, String polygonSearch);

	Long getCountsOfPolygon(Integer userid, String searchTerm);

	String getKmlFileName(Integer id);

	UserPolygon updatePolygonPoint(User user, UserPolygon userPolygon);

	UserPolygon updatePolygonProperties(Integer userid, UserPolygon userPolygon);

}
