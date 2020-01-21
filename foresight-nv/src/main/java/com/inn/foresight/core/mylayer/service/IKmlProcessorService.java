package com.inn.foresight.core.mylayer.service;

import java.io.InputStream;
import java.util.List;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.mylayer.model.KmlProcessor;
import com.inn.foresight.core.mylayer.utils.KmlProcessorWrapper;

public interface IKmlProcessorService  extends IGenericService<Integer, KmlProcessor>
{

	KmlProcessor uploadFileAtDropwizard(InputStream inputStream, String fileName, String colorCode, Integer userid);

	KmlProcessor getKmlDataById(Integer id);

	List<KmlProcessorWrapper> getKmlData(Integer userid, Integer upperLimit, Integer lowerLimit);

	List<KmlProcessor> getListOfKMLBySearchTerm(Integer userid, Integer lowerLimit, Integer upperLimit,	String kmlSearch);

	Long getCountsOfKMLBySearchTerm(Integer userid, String searchTerm);

	Long getCountsOfKML(Integer userid);

	boolean deleteKMLDetailsByID(Integer userid, Integer id);

	KmlProcessor updateKMLDetails(Integer userId, KmlProcessor kmlProcessor);

	String getFileNameForKML(Integer id);

	String exportFileForKML(Integer id);

	boolean isKMLFileExistForUser(String fileName, Integer userid);

	String uploadKmlFile(String kmlName, String colorCode, Integer userid);

}
