package com.inn.foresight.core.um.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.um.utils.wrapper.BulkUserInformationWrapper;
import com.inn.foresight.core.um.utils.wrapper.ForesightTeamWrapper;
import com.inn.foresight.core.um.utils.wrapper.RoleDetailWrapper;
import com.inn.foresight.core.um.utils.wrapper.WorkspaceWrapper;
import com.inn.product.security.um.model.ORGAuthenticationType;
import com.inn.product.um.geography.utils.wrapper.GeoGraphyInfoWrapper;
import com.inn.product.um.user.utils.UmConstants;
import com.inn.product.um.user.utils.UmUtils;
import com.inn.product.um.user.utils.wrapper.UserIntegrationWrapper;
import com.inn.product.um.user.utils.wrapper.UserPersonalDetailWrapper;



public class BulkUploadUtils {


	private static Logger logger = LogManager.getLogger(BulkUploadUtils.class);


	public static boolean validateUserPersonalInfo(UserIntegrationWrapper userIntegrationWrapper) {
		Boolean status = true;
		UserPersonalDetailWrapper userPersonalInfo = userIntegrationWrapper.getUserPersonalInfo();
		if (userPersonalInfo != null) {
			if (!UmUtils.isValidFirstName(userPersonalInfo.getFirstName())) {
				userIntegrationWrapper.setMessage("Please Provide Valid FirstName");
				status = false;
			} else if (userPersonalInfo.getLastName() != null && !userPersonalInfo.getLastName().isEmpty()
					&& !UmUtils.isValidLastName(userPersonalInfo.getLastName())) {
				userIntegrationWrapper.setMessage("Please Provide Valid LastName");
				status = false;
			} else if (userPersonalInfo.getMiddleName() != null && !userPersonalInfo.getMiddleName().isEmpty()
					&& !UmUtils.isValidLastName(userPersonalInfo.getMiddleName())) {
				userIntegrationWrapper.setMessage("Please Provide Valid Middle Name");
				status = false;
			} else if (!isValidateUserName(userPersonalInfo.getUserName())) {
				userIntegrationWrapper.setMessage("Please Provide Valid User Name");
				status = false;
			} else if (!UmUtils.isValidEmail(userPersonalInfo.getEmail())) {
				userIntegrationWrapper.setMessage("Please Provide Valid Email Id");
				status = false;
			} else if (!isValidORGAuthenticationType(userPersonalInfo.getOrgAuthenticationType())) {
				userIntegrationWrapper.setMessage("Please Provide Valid Organization Name/Authentication Type");
				status = false;
			} else if (!isValidContactNumber(userPersonalInfo.getContactNumber())) {
				userIntegrationWrapper.setMessage("Please Provide Valid Contact Number(Limit 10)");
				status = false;
			}
		}
		return status;
	}
	public static Boolean isValidateUserName(String userName) {
		if (userName != null && !userName.isEmpty())
			return true;
		return false;
	}

	public static Boolean isValidContactNumber(String phoneNo) {
		if (phoneNo != null && !phoneNo.isEmpty())
			// validate phone numbers of format "1234567890"
			if (phoneNo.matches("\\d{10}"))
				return true;
			
		    	// validating phone number with -, . or spaces
			else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
				return true;
			// validating phone number with extension length from 3 to 5
			else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
				return true;
			// validating phone number where area code is in braces ()
			else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
				return true;
			// return false if nothing matches the input
			else
				return false;

		return false;
	}

	public static boolean isValidORGAuthenticationType(ORGAuthenticationType orgAuthenticationType) {
        logger.info("Inside isValidORGAuthenticationType");
		if (orgAuthenticationType != null && orgAuthenticationType.getAuthenticationtype() != null)
			return true;
		return false;

	}
	public static boolean isNullOrEmpty(final Map<?, ?> m) {
		return m == null || m.isEmpty();
	}
	

	
	public static RoleDetailWrapper getSiteForgeGuestRole() throws RestException {
		logger.info("Going to get getSiteForgeGuestRole ");
		RoleDetailWrapper guestRole = new RoleDetailWrapper();
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");
			headers.put(ConfigUtils.getString("API_USERNAME_KEY"), ConfigUtils.getString("API_USERNAME_VALUE"));
			headers.put(ConfigUtils.getString("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));

			logger.info("username :{} value : {} ", headers.get("API_USERNAME_KEY"),ConfigUtils.getString("API_USERNAME_VALUE"));
			logger.info("verification key :{}  value : {} ", headers.get("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));
			
			String url = ConfigUtils.getString("SITEFORGE_BASE_URL") + ConfigUtils.getString("SITEFORGE_GUEST_ROLE");
			logger.info("url :{} ", url);
			String response = Utils.makeHttpGetWithSSLByPassing(url, headers);
			logger.info("response: {} ", response);
			guestRole = new Gson().fromJson(response, RoleDetailWrapper.class);

		} catch (Exception exception) {
			logger.error("Error while getting getSiteForgeGuestRole: {} ", Utils.getStackTrace(exception));
		}
		return guestRole;
	}
	
	
	
	public static List<WorkspaceWrapper> getAllSiteForgeWorkspace() {

		logger.info("Inside getAllSiteForgeWorkspace {} ");
		List<WorkspaceWrapper> roleList = new ArrayList<>();
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");
			headers.put(ConfigUtils.getString("API_USERNAME_KEY"), ConfigUtils.getString("API_USERNAME_VALUE"));
			headers.put(ConfigUtils.getString("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));

			logger.info("username :{} value : {} ", headers.get("API_USERNAME_KEY"),ConfigUtils.getString("API_USERNAME_VALUE"));
			logger.info("verification key :{}  value : {} ", headers.get("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));
			String url = ConfigUtils.getString("SITEFORGE_BASE_URL") + ConfigUtils.getString("GET_ALL_SITEFORGE_WORKSPACE");
					//+ URLEncoder.encode(teamName, "UTF-8").replace("+", "%20");
			logger.info("url :{} ", url);
			String response = Utils.makeHttpGetWithSSLByPassing(url, headers);
			logger.info("response: {} ", response);
			roleList = new Gson().fromJson(response, new TypeToken<ArrayList<WorkspaceWrapper>>() {
			}.getType());

		} catch (Exception exception) {
			logger.error("Error while getting getAllSiteForgeWorkspace: {} ", Utils.getStackTrace(exception));
		}
		return roleList;

	}
	
	
	public static List<ForesightTeamWrapper> getSiteForgeTeamByWSId(Integer id) {

		logger.info("Inside getSiteForgeTeamByWSId {} ");
		List<ForesightTeamWrapper> roleList = new ArrayList<>();
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");
			headers.put(ConfigUtils.getString("API_USERNAME_KEY"), ConfigUtils.getString("API_USERNAME_VALUE"));
			headers.put(ConfigUtils.getString("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));

			logger.info("username :{} value : {} ", headers.get("API_USERNAME_KEY"),ConfigUtils.getString("API_USERNAME_VALUE"));
			logger.info("verification key :{}  value : {} ", headers.get("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));
			
			String url = ConfigUtils.getString("SITEFORGE_BASE_URL") + ConfigUtils.getString("GET_TEAM_BY_WS_ID")
					+ id;
			logger.info("url :{} ", url);
			String response = Utils.makeHttpGetWithSSLByPassing(url, headers);
			logger.info("response: {} ", response);
			roleList = new Gson().fromJson(response, new TypeToken<ArrayList<ForesightTeamWrapper>>() {
			}.getType());

		} catch (Exception exception) {
			logger.error("Error while getting getSiteForgeTeamByWSId: {} ", Utils.getStackTrace(exception));
		}
		return roleList;

	}
	
	
	public static List<String> getAllLevelTypesByWSIdAndTeamId(Integer wsId,Integer teamId) {

		logger.info("Inside getAllLevelTypesByWSIdAndTeamId {} ");
		List<String> levelTypeList = new ArrayList<>();
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");
			headers.put(ConfigUtils.getString("API_USERNAME_KEY"), ConfigUtils.getString("API_USERNAME_VALUE"));
			headers.put(ConfigUtils.getString("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));

			logger.info("username :{} value : {} ", headers.get("API_USERNAME_KEY"),ConfigUtils.getString("API_USERNAME_VALUE"));
			logger.info("verification key :{}  value : {} ", headers.get("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));
			
			String url = ConfigUtils.getString("SITEFORGE_BASE_URL") + ConfigUtils.getString("GET_ALL_LEVEL_TYPES_BY_WSID_AND_TEAM_ID")
					+wsId+"&teamId="+teamId;
			logger.info("url :{} ", url);
			String response = Utils.makeHttpGetWithSSLByPassing(url, headers);
			logger.info("response: {} ", response);
			levelTypeList = new Gson().fromJson(response, new TypeToken<ArrayList<String>>() {
			}.getType());

		} catch (Exception exception) {
			logger.error("Error while getting getSiteForgeTeamByWSId: {} ", Utils.getStackTrace(exception));
		}
		return levelTypeList;

	}
	
	public static List<RoleDetailWrapper> getRolesByWSIdandLevelTypeAndTeam(Integer wsId,Integer teamId,String levelType) {

		logger.info("Inside getRolesByWSIdandLevelTypeAndTeam {} ");
		List<RoleDetailWrapper> roleDetailWrappers = new ArrayList<>();
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");
			headers.put(ConfigUtils.getString("API_USERNAME_KEY"), ConfigUtils.getString("API_USERNAME_VALUE"));
			headers.put(ConfigUtils.getString("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));

			logger.info("username :{} value : {} ", headers.get("API_USERNAME_KEY"),ConfigUtils.getString("API_USERNAME_VALUE"));
			logger.info("verification key :{}  value : {} ", headers.get("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));
			
			String url = ConfigUtils.getString("SITEFORGE_BASE_URL") + ConfigUtils.getString("GET_ROLES_BY_WS_ID_AND_LEVEL_TYPE_AND_TEAM")
					+wsId+"&teamId="+teamId+"&levelType="+URLEncoder.encode(levelType, "UTF-8").replace("+", "%20");
			logger.info("url :{} ", url);
			String response = Utils.makeHttpGetWithSSLByPassing(url, headers);
			logger.info("response: {} ", response);
			roleDetailWrappers = new Gson().fromJson(response, new TypeToken<ArrayList<RoleDetailWrapper>>() {
			}.getType());

		} catch (Exception exception) {
			logger.error("Error while getting getRolesByWSIdandLevelTypeAndTeam: {} ", Utils.getStackTrace(exception));
		}
		return roleDetailWrappers;

	}
	
	
	public static List<GeoGraphyInfoWrapper> findAllGeographyInfoL1(String geoType) throws RestException  {
		logger.info("Inside Method findAllGeographyInfoL1");
		 logger.info("Inside findAllGeographyInfoL1 :{}  ");
    	 List<GeoGraphyInfoWrapper> geoGraphyInfoWrapper=new ArrayList<>();
    	 String url="";
    	 try {
    		 Map<String, String> headers = new HashMap<String, String>();
    		 headers.put("Content-Type", "application/json");
    			headers.put(ConfigUtils.getString("API_USERNAME_KEY"), ConfigUtils.getString("API_USERNAME_VALUE"));
    			headers.put(ConfigUtils.getString("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));

    			logger.info("username :{} value : {} ", headers.get("API_USERNAME_KEY"),ConfigUtils.getString("API_USERNAME_VALUE"));
    			logger.info("verification key :{}  value : {} ", headers.get("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));
    			if(geoType.equalsIgnoreCase(UmConstants.SALES)) {
  	    		  url =ConfigUtils.getString("SITEFORGE_BASE_URL")+ConfigUtils.getString("SITEFORGE_FIND_ALL_GEOGRAPHYINFO_SALES_L1");

    			}else {
    	    		  url =ConfigUtils.getString("SITEFORGE_BASE_URL")+ConfigUtils.getString("SITEFORGE_FIND_ALL_GEOGRAPHYINFO_L1")+"?geoType="+geoType+"";

    			}
    		 logger.info("url :{} ",url);
    		 String response = Utils.makeHttpGetWithSSLByPassing(url, headers);
    		 logger.info("response: {} ",response);
    		 geoGraphyInfoWrapper=	new Gson().fromJson(response,new TypeToken<ArrayList<GeoGraphyInfoWrapper>>() {	}.getType());
    	 }
    	 catch(Exception exception) 
    	 {

    		 logger.error("Error while getting findAllGeographyInfoL1: {} ",Utils.getStackTrace(exception));	
    	 }
    	 return geoGraphyInfoWrapper;
	}
	
	
	
	public static List<GeoGraphyInfoWrapper> getGeographyL4ByL3Name(String name,String geoType) throws RestException {
		logger.info("Going to get getGeographyL2ByL1Name : {} ", name);
		
		List<GeoGraphyInfoWrapper> geoGraphyInfoWrapper=new ArrayList<>();
		try {
         
   		 Map<String, String> headers = new HashMap<String, String>();
   		 headers.put("Content-Type", "application/json");
   		headers.put(ConfigUtils.getString("API_USERNAME_KEY"), ConfigUtils.getString("API_USERNAME_VALUE"));
		headers.put(ConfigUtils.getString("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));

		logger.info("username :{} value : {} ", headers.get("API_USERNAME_KEY"),ConfigUtils.getString("API_USERNAME_VALUE"));
		logger.info("verification key :{}  value : {} ", headers.get("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));
		

   		 String url =ConfigUtils.getString("SITEFORGE_BASE_URL")+ConfigUtils.getString("SITEFORGE_GEOGRAPHYL4BYL3NAME")+URLEncoder.encode(name, "UTF-8").replace("+", "%20")+"?geoType="+geoType+"";
   		 logger.info("url :{} ",url);
   		 String response = Utils.makeHttpGetWithSSLByPassing(url, headers);
   		 logger.info("response: {} ",response);
   		 geoGraphyInfoWrapper=	new Gson().fromJson(response,new TypeToken<ArrayList<GeoGraphyInfoWrapper>>() {	}.getType());
   		
   	 }
   	 catch(Exception exception) 
   	 {

   		 logger.error("Error while getting findAllGeographyInfoL1: {} ",Utils.getStackTrace(exception));	
   	 }
		return geoGraphyInfoWrapper;
	}
	
	
	public static List<GeoGraphyInfoWrapper> getGeoGraphyL3ByL2Name(String name,String geoType) throws RestException {
		logger.info("Going to get getGeographyL2ByL1Name : {} ", name);
		
		List<GeoGraphyInfoWrapper> geoGraphyInfoWrapper=new ArrayList<>();
		try {
            
   		 Map<String, String> headers = new HashMap<String, String>();
   		 headers.put("Content-Type", "application/json");
   		headers.put(ConfigUtils.getString("API_USERNAME_KEY"), ConfigUtils.getString("API_USERNAME_VALUE"));
		headers.put(ConfigUtils.getString("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));

		logger.info("username :{} value : {} ", headers.get("API_USERNAME_KEY"),ConfigUtils.getString("API_USERNAME_VALUE"));
		logger.info("verification key :{}  value : {} ", headers.get("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));
		

   		 String url =ConfigUtils.getString("SITEFORGE_BASE_URL")+ConfigUtils.getString("SITEFORGE_GEOGRAPHYL3BYL2NAME")+URLEncoder.encode(name, "UTF-8").replace("+", "%20")+"?geoType="+geoType+"";
   		 logger.info("url :{} ",url);
   		 String response = Utils.makeHttpGetWithSSLByPassing(url, headers);
   		 logger.info("response: {} ",response);
   		 geoGraphyInfoWrapper=	new Gson().fromJson(response,new TypeToken<ArrayList<GeoGraphyInfoWrapper>>() {	}.getType());

   	 }
   	 catch(Exception exception) 
   	 {

   		 logger.error("Error while getting findAllGeographyInfoL1: {} ",Utils.getStackTrace(exception));	
   	 }
		return geoGraphyInfoWrapper;
	}
	
	
	public static List<GeoGraphyInfoWrapper> getGeographyL2ByL1Name(String name,String geoType) throws RestException  {
		logger.info("Going to get getGeographyL2ByL1Name : {} ", name);
		
		List<GeoGraphyInfoWrapper> geoGraphyInfoWrapper=new ArrayList<>();
		try {
            
   		 Map<String, String> headers = new HashMap<String, String>();
   		 headers.put("Content-Type", "application/json");
   		headers.put(ConfigUtils.getString("API_USERNAME_KEY"), ConfigUtils.getString("API_USERNAME_VALUE"));
		headers.put(ConfigUtils.getString("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));

		logger.info("username :{} value : {} ", headers.get("API_USERNAME_KEY"),ConfigUtils.getString("API_USERNAME_VALUE"));
		logger.info("verification key :{}  value : {} ", headers.get("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));
		

   		 String url =ConfigUtils.getString("SITEFORGE_BASE_URL")+ConfigUtils.getString("SITEFORGE_GEOGRAPHYL2BYL1NAME")+URLEncoder.encode(name, "UTF-8").replace("+", "%20")+"?geoType="+geoType+"";
   		 logger.info("url :{} ",url);
   		 String response = Utils.makeHttpGetWithSSLByPassing(url, headers);
   		 logger.info("response: {} ",response);
   		 geoGraphyInfoWrapper=	new Gson().fromJson(response,new TypeToken<ArrayList<GeoGraphyInfoWrapper>>() {	}.getType());

   	 }
   	 catch(Exception exception) 
   	 {

   		 logger.error("Error while getting findAllGeographyInfoL1: {} ",Utils.getStackTrace(exception));	
   	 }
		return geoGraphyInfoWrapper;
	}
	
	
	public static String findDomainName(String email)
	{
		if(email!=null) {
		email = email.substring(email.indexOf("@") + 1);
		}
		return email;
	}
	
	public static Boolean isValidStringLength(String name) {
		logger.info("Inside isValidStringLength");
		if(name.length()<=50)
			return true;
		return false;
		
	}
	public static List<GeoGraphyInfoWrapper> findAllOtherGeography(String levelType) {
	
		logger.info("findAllOtherGeography: {} ");
		List<GeoGraphyInfoWrapper> geoGraphyInfoWrapper=new ArrayList<>();
		try {
   		 Map<String, String> headers = new HashMap<String, String>();
   		 headers.put("Content-Type", "application/json");
   		headers.put("Content-Type", "application/json");
   		headers.put(ConfigUtils.getString("API_USERNAME_KEY"), ConfigUtils.getString("API_USERNAME_VALUE"));
		headers.put(ConfigUtils.getString("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));

		logger.info("username :{} value : {} ", headers.get("API_USERNAME_KEY"),ConfigUtils.getString("API_USERNAME_VALUE"));
   		 logger.info("username :{} ",headers.get("FS_API_USERNAME"));
   		 String url =ConfigUtils.getString("SITEFORGE_BASE_URL")+ConfigUtils.getString("SITEFORGE_OTHER_GEOGRAPHY")+URLEncoder.encode(levelType, "UTF-8").replace("+", "%20");
   		 logger.info("url :{} ",url);
   		 String response = Utils.makeHttpGetWithSSLByPassing(url, headers);
   		 logger.info("response: {} ",response);
   		 geoGraphyInfoWrapper=	new Gson().fromJson(response,new TypeToken<ArrayList<GeoGraphyInfoWrapper>>() {	}.getType());

   	 }
   	 catch(Exception exception) 
   	 {
   		 logger.error("Error while getting findAllOtherGeography: {} ",Utils.getStackTrace(exception));	
   	 }
		return geoGraphyInfoWrapper;
	
	}
	
	public static BulkUserInformationWrapper getRoleInfoByUserName(String userName) { 
		BulkUserInformationWrapper userIntegrationWrapper = null;
		logger.info("getRoleInfoByUserName: {} ");
		try {
			 Map<String, String> headers = new HashMap<String, String>();
	   		 headers.put("Content-Type", "application/json");
	   		headers.put(ConfigUtils.getString("API_USERNAME_KEY"), ConfigUtils.getString("API_USERNAME_VALUE"));
			headers.put(ConfigUtils.getString("API_VERIFICATION_KEY"),ConfigUtils.getString("API_VERIFICATION_VALUE"));
			logger.info("username :{} value : {} ", headers.get("API_USERNAME_KEY"),ConfigUtils.getString("API_USERNAME_VALUE"));
	   		 logger.info("username :{} ",headers.get("FS_API_USERNAME"));
	   		String url =ConfigUtils.getString("SITEFORGE_BASE_URL")+ConfigUtils.getString("SITEFORGE_USER_INFO")+URLEncoder.encode(userName, "UTF-8").replace("+", "%20");
	   		 logger.info("url :{} ",url);
	   		String response = Utils.makeHttpGetWithSSLByPassing(url, headers);
	   		 logger.info("response: {} ",response.toString());
	   		ObjectMapper mapperObj = new ObjectMapper();
	   		userIntegrationWrapper = mapperObj.readValue(response,
					new TypeReference<BulkUserInformationWrapper>() { });
					
	   		//userIntegrationWrapper = new Gson().fromJson(response,new TypeToken<UserIntegrationWrapper>() {	}.getType());
			logger.info("Convert to Wrapper {} ",userIntegrationWrapper);
			
		}
		 catch(Exception exception) 
	   	 {
	   		 logger.error("Error while getting getRoleInfoByUserName: {} ",Utils.getStackTrace(exception));	
	   	 }
		
		
		return userIntegrationWrapper;
		
		
		
	}
	
	
}
