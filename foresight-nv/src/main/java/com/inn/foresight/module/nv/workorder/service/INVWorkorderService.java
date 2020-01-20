package com.inn.foresight.module.nv.workorder.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonObject;
import com.inn.commons.http.HttpException;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.wrapper.NETaskDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.core.workorder.model.GWOMeta;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOParameters;
import com.inn.foresight.module.nv.workorder.wrapper.NVWorkorderWrapper;
import com.inn.product.um.user.model.User;

/** The Interface INVWorkorderService. */
public interface INVWorkorderService   {

	String createWorkorderFromWeb(NVWorkorderWrapper wrapper);
	
	String createWorkorderForDevice(NVWorkorderWrapper wrapper);
	
	Collection<NVWorkorderWrapper> getWorkorderDetailsByUser(String workorderId, String templateType,
			String modifiedTime);

	Object completeWorkorderByTaskId(Integer taskId);

	Long getTotalWorkorderCount(List<Status> statusList,
			List<TemplateType> templateList,String searchString,Boolean isArchived);

	List<NVWorkorderWrapper> findAllWorkorder(Integer lLimit, Integer uLimit, List<Status> statusList,
			List<TemplateType> templateList, String searchString, Boolean isArchive);

	Response getGeographyBoundryForMobile(String strGeoData);

	NVWorkorderWrapper createWorkorderFromMobile(String strWorkorder);
	
	NVWorkorderWrapper getWorkorderDetailsByWOId(Integer workorderId,Integer taskId);

	NVWorkorderWrapper getWorkorderDetailsByWOIdFromMobile(Integer workorderId);
	
	void generateReport(AnalyticsRepository analyticsRepository) throws HttpException;

	/**AnalyticsRepository insertWOReportConfigData(Integer workorderId, TemplateType templateType, String user)
			throws RestException;.*/
	
	List<NetworkElementWrapper> getSitesFromLatLong(String latLongData,String neStatusJson,String neTypeJson) throws JsonParseException, JsonMappingException, IOException;

	@SuppressWarnings("rawtypes")
	List<Map> getWorkorderListByGeography(String geographyLevel, Integer geographyId, List<Status> statusList,
			List<TemplateType> templateList, String remark);

	List<RecipeWrapper> addRecipeIntoWO(NVWorkorderWrapper wrapper);
	
	String findFilePartByRecipeMappingId(String woRecipeMappingId);	

	String deleteFileByRecipeMappingId(String woRecipeMappingId);

	List<String> geKpiAndEventString(Integer workrorderId, List<Integer> recipeList);

	String getWorkOrderDetailByImei(String imei, Long startTime, Long endTime, TemplateType templateType);

	@SuppressWarnings("rawtypes")
	List<Map> getWorkorderListByGeographyOfPeriod(String geographyLevel, Integer geographyId,
			List<GenericWorkorder.Status> statusList, List<TemplateType> templateList, List<Integer> quarterList,
			List<Integer> yearList);

	Object updateWorkorderDetails(NVWorkorderWrapper wrapper);
	
	@SuppressWarnings("rawtypes")
	List<Map> getWorkorderListByGeographyOfPeriod(String geographyLevel, List<Integer> geographyId,
			List<Status> statusList, List<TemplateType> templateList, List<Integer> quarterList,
			List<Integer> yearList,String technology);

	String reassignWorkorder(Integer taskId, String userName, String remark);

	String deleteWorkorder(Integer taskId);

	List<NVWorkorderWrapper> getWorkorderStatusByTaskIdList(String encrTaskIdList);

	AnalyticsRepository insertWOReportConfigData(Integer workorderId, TemplateType templateType, String user,
			GenericWorkorder genericWorkorder);


	NVWorkorderWrapper getWorkorderDetailsByDeviceId(Integer workorderId, String deviceId, Date date);

	List<NVWorkorderWrapper> fetchStealthWorkOrder(List<StealthWOParameters> list);

	Response deleteFilesFromWoFileDetail(List<Integer> idList);

	Response updatePresetIdForWorkorder(Integer presetId, Integer workorderId);

	GenericWorkorder updateWorkorderArchivedStatus(Integer workorderId, Boolean isArchive);
	void sendNotificationToDevice(User user, Boolean isNotificationRequired, GenericWorkorder workorder);

	void completeAllSyncedRecipes(GenericWorkorder genericWorkorder);

	Collection<NVWorkorderWrapper> getWorkorderDetailsByTemplateList(String strWorkorderId, List<String> templateType,
			Long modifiedTime);

	Response checkIfReportAvailabel(String workorderId);
	GenericWorkorder updateStatusInGenericWorkorder(String workorderId);

	Response updateRecipeCustomGeographyMapInGWOMeta(String workOrderId, Integer customGeographyId, String woMapType);

	void insertOrUpdateEntityValueInGWOMeta(String entityType, Object entityValue, Integer workorderId,
			String woMapType);

	Boolean updateHoldStatusInGenericWorkorder(String workorderId,String remark);

	Response WOReopenByWOId(String workorderId);

	Map<String, String> createWorkorderForSSVT(NETaskDetailWrapper neWrapper);

	Object findFilteredWOList(NVWorkorderWrapper wrapper, Integer lLimit, Integer uLimit, Boolean isArchive);

	List<Integer> createWorkorderForUser(NVWorkorderWrapper wrapper, Boolean isNotificationRequired);

	GWOMeta getGWOMetaByRecipeId(Integer recipeId);

	GWOMeta prepareGWOMetaData(GWOMeta gwoMetaResponse, JSONObject jsonObject, GenericWorkorder workorderObj,
			Integer recipeId) throws DaoException;

}
