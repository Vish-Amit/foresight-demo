package com.inn.foresight.core.um.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.utils.CoreConstants;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.um.service.CustomUserService;
import com.inn.foresight.core.um.utils.BulkUploadUtils;
import com.inn.foresight.core.um.utils.ExcelReadWriter;
import com.inn.foresight.core.um.utils.wrapper.BulkUserInformationWrapper;
import com.inn.foresight.core.um.utils.wrapper.ForesightTeamWrapper;
import com.inn.foresight.core.um.utils.wrapper.RoleDetailWrapper;
import com.inn.foresight.core.um.utils.wrapper.WorkspaceWrapper;
import com.inn.product.audit.dao.AuditDao;
import com.inn.product.audit.model.Audit;
import com.inn.product.audit.utils.AuditActionName;
import com.inn.product.audit.utils.AuditActionType;
import com.inn.product.security.auth.dao.BasicAuthenticationDetailDao;
import com.inn.product.security.auth.model.BasicAuthenticationDetail;
import com.inn.product.security.idp.idpsync.IDPSyncUtils;
import com.inn.product.security.spring.userdetails.CustomerInfo;
import com.inn.product.security.spring.userdetails.SecuredUserService;
import com.inn.product.security.um.dao.ORGAuthenticationTypeDao;
import com.inn.product.security.um.dao.OrganizationDao;
import com.inn.product.security.um.model.ORGAuthenticationType;
import com.inn.product.security.um.model.Organization;
import com.inn.product.security.wrapper.AccessLevelWrapper;
import com.inn.product.systemconfiguration.service.SystemConfigurationService;
import com.inn.product.um.geography.dao.GeographyL1Dao;
import com.inn.product.um.geography.dao.GeographyL2Dao;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.dao.SalesL1Dao;
import com.inn.product.um.geography.dao.SalesL2Dao;
import com.inn.product.um.geography.dao.SalesL3Dao;
import com.inn.product.um.geography.dao.SalesL4Dao;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.geography.model.OtherGeography;
import com.inn.product.um.geography.model.SalesL1;
import com.inn.product.um.geography.model.SalesL2;
import com.inn.product.um.geography.model.SalesL3;
import com.inn.product.um.geography.model.SalesL4;
import com.inn.product.um.geography.utils.wrapper.GeoGraphyInfoWrapper;
import com.inn.product.um.permission.utils.UmPermissionConstants;
import com.inn.product.um.role.dao.RoleDao;
import com.inn.product.um.role.model.Role;
import com.inn.product.um.role.model.UserRole;
import com.inn.product.um.role.model.UserRoleGeography;
import com.inn.product.um.role.model.WorkSpace;
import com.inn.product.um.role.service.RoleService;
import com.inn.product.um.role.utils.wrapper.UserRoleGeographyDetails;
import com.inn.product.um.team.model.Team;
import com.inn.product.um.user.dao.UserDao;
import com.inn.product.um.user.model.Address;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.UserContextService;
import com.inn.product.um.user.service.UserService;
import com.inn.product.um.user.utils.UmConstants;
import com.inn.product.um.user.utils.UmUtils;
import com.inn.product.um.user.utils.wrapper.UserIntegrationWrapper;
import com.inn.product.um.user.utils.wrapper.UserPersonalDetailWrapper;


@Service("CustomUserServiceImpl")
public class CustomUserServiceImpl implements CustomUserService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(CustomUserServiceImpl.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleService roleService;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private ORGAuthenticationTypeDao oRGAuthenticationTypeDao;

	@Autowired
	private GeographyL1Dao geographyL1Dao;

	@Autowired
	private GeographyL2Dao geographyL2Dao;

	@Autowired
	private GeographyL3Dao geographyL3Dao;

	@Autowired
	private GeographyL4Dao geographyL4Dao;

	@Autowired
	private SalesL1Dao salesL1Dao;

	@Autowired
	private SalesL2Dao salesL2Dao;

	@Autowired
	private SalesL3Dao salesL3Dao;

	@Autowired
	private SalesL4Dao salesL4Dao;

	@Autowired
	private SecuredUserService securedUserService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserContextService userContextService;

	@Autowired
	private BasicAuthenticationDetailDao basicAuthenticationDetailDao;

	@Autowired
	private AuditDao auditDao;
	
	
	@Autowired
	private CustomerInfo customerinfo;
	
	@Override
	public Map<String, String> createBulkUser(InputStream inputStream,String filePath,String type) {

		logger.info("Inside createBulkUser in class : {} ", this.getClass().getName());
		Map<String, String> messageMap = new HashMap<>();
		int maxNoRecords = 0;
		int successRecords = 0;
		List<String> usersList = new ArrayList<>();
		List<String> emailIds=new ArrayList<>();
		boolean containsSearchStr=false;
		List<Audit> audits=new ArrayList<>();
		
		try {
			UserPersonalDetailWrapper userPersonalDetailWrapper = new UserPersonalDetailWrapper();
			List<Map<String, String>> mapData = ExcelReadWriter.getExcelList(inputStream,
					ConfigUtils.getString(UmConstants.BULKUPLOAD_HEADER));
			logger.info("map : {} ", mapData);

			List<BulkUserInformationWrapper> userIntegrationWrapper = setBulkUserData(mapData);
			logger.info("userIntegrationWrapper size : " + userIntegrationWrapper.size());
			logger.info("userIntegrationWrapper : " + userIntegrationWrapper);
			logger.info("userIntegrationWrapper size {} ", userIntegrationWrapper.size());

			if (userIntegrationWrapper != null&&userIntegrationWrapper.size()>0) {
				
				if(!validateFile(userIntegrationWrapper)) {
				
				for (BulkUserInformationWrapper userIntegration : userIntegrationWrapper) {

					logger.info("																																																																																																																															json : {} ", new Gson().toJson(userIntegration));
					BulkUserInformationWrapper finalUserIntegrationWrapper = userIntegration;// (UserIntegrationWrapper)																			// userIntegration;
					userPersonalDetailWrapper = finalUserIntegrationWrapper.getUserPersonalInfo();
					
					if(finalUserIntegrationWrapper.getUserPersonalInfo().getEmail()!=null)
						containsSearchStr = emailIds.stream().anyMatch(finalUserIntegrationWrapper.getUserPersonalInfo().getEmail()::equalsIgnoreCase);
				
						if(!containsSearchStr) {
						emailIds.add(finalUserIntegrationWrapper.getUserPersonalInfo().getEmail());
					
					if (isValidateUser(finalUserIntegrationWrapper)) {
						logger.info("Active Roles  : {} ",userIntegration.getUserPersonalInfo().getActiveRole());
						//if (setGuestRole(userIntegration)) {
						logger.info("FORESIGHT ROLE : {} ", userIntegration.getRoleMap());
						logger.info("json  : " + new Gson().toJson(userIntegration));
						if (!isUserExists(finalUserIntegrationWrapper.getUserPersonalInfo().getUserName(),
								finalUserIntegrationWrapper.getUserPersonalInfo().getEmail())) {
							if (setGuestRole(userIntegration)) {
							try {
								logger.info("persional info=== {}", finalUserIntegrationWrapper.getUserPersonalInfo());
								logger.info("basic authentication info=== {}",
										finalUserIntegrationWrapper.getBasicAuthenticationDetail());

								if (finalUserIntegrationWrapper.getRoleMap() != null) {
									logger.info("roleMap=== {} ", finalUserIntegrationWrapper.getRoleMap());
								}
								if (finalUserIntegrationWrapper.getCustomMap() != null) {
									logger.info("custom map {} ", finalUserIntegrationWrapper.getCustomMap());
								}
								Map<String, String> map2 = userService.createUser(finalUserIntegrationWrapper);
								logger.info("response map value : {} ", map2);
								if (map2.get(CoreConstants.MESSAGE)
										.equalsIgnoreCase(UmConstants.USER_CREATED_SUCCESSFULLY)) {
									successRecords++;
									finalUserIntegrationWrapper.setMessage(UmConstants.USER_CREATED_SUCCESSFULLY);
									userAuditFromBulkupload(finalUserIntegrationWrapper, AuditActionType.CREATE, AuditActionName.USERS_CREATE, "USER_CREATED_BY_BULKUPLOAD");
									
								} else {
									finalUserIntegrationWrapper.setMessage(map2.get(CoreConstants.MESSAGE));
								}
							} catch (Exception e) {
								finalUserIntegrationWrapper.setMessage("Failed to create/update User");
								logger.error("Error while  creating  user request : {}", Utils.getStackTrace(e));
							}
							
						}else {
							finalUserIntegrationWrapper.setMessage("Failed to create/update User.Please Provide permission to Guest Role.");
						}
						} else {
							try {
								finalUserIntegrationWrapper.getUserPersonalInfo().setEnabled(null);
								List<User> fUsersList = userDao.findUserListByUsernameOrEmail(finalUserIntegrationWrapper.getUserPersonalInfo().getUserName(),finalUserIntegrationWrapper.getUserPersonalInfo().getEmail());
								User user=fUsersList.get(0);
								
								BasicAuthenticationDetail basicAuthenticationDetail = findUser(finalUserIntegrationWrapper);
								logger.info("authentication type : {} ",basicAuthenticationDetail.getOrgAuthenticationType().getAuthenticationtype());
                                
							    
								
								
								if(!"ZSmart".equalsIgnoreCase(basicAuthenticationDetail.getOrgAuthenticationType().getAuthenticationtype())) {
								if(finalUserIntegrationWrapper.getRoleMap().containsKey("SP_FORESIGHT")) {
								List<UserRoleGeographyDetails> userRoleGeographies = finalUserIntegrationWrapper.getRoleMap().get("SP_FORESIGHT");
								logger.info("userRoleGeographies~~~~ {} ",userRoleGeographies);
								Role role = roleService.getGuestRole(true);
								logger.info("fetching guest role ~~~~ {}",role);
								Boolean isGuestRole=userRoleGeographies.stream().filter(o -> o.getRole().getRoleName().equalsIgnoreCase(role.getRoleName())).findFirst().isPresent();
								logger.info("isGuestRole=====: {} ",isGuestRole);
								if(isGuestRole) {
									finalUserIntegrationWrapper.setMessage("Please Provide role other than guest role.");
								}else {
								    setCustomerCareRole(finalUserIntegrationWrapper,user);
									successRecords = UpdateExistingUser(successRecords, finalUserIntegrationWrapper,
											user,type);
								}
								}else {
									/*List<UserRoleGeographyDetails> userRoleGeographies = finalUserIntegrationWrapper.getRoleMap().get("SP_FORESIGHT");
									logger.info("userRoleGeographies===>{}",userRoleGeographies);*/
									finalUserIntegrationWrapper.getUserPersonalInfo().getActiveRole().put(ForesightConstants.SP_FORESIGHT, setPreviousActiveRole(user));
									finalUserIntegrationWrapper.setRoleMap(addPreviousUserRole(user,finalUserIntegrationWrapper));
									if (user != null)
										successRecords = UpdateExistingUser(successRecords, finalUserIntegrationWrapper,
												user,type);
								}
							}else {
								finalUserIntegrationWrapper.setMessage("Failed to create/update User.zSmart User can't be update.");
							}
								
							} catch (Exception e) {
								finalUserIntegrationWrapper.setMessage("Failed to create/update User");
								logger.error("Error while  updating  user request : {}", Utils.getStackTrace(e));
							}
						}
					/*}else {
						finalUserIntegrationWrapper.setMessage("Failed to create/update User.Please Provide permission to Guest Role.");
					}*/
					}
					
				}else {
					finalUserIntegrationWrapper.setMessage("Duplicate email id are not allowed.");	
			       }
					
					if (userPersonalDetailWrapper != null && userPersonalDetailWrapper.getEmail() != null
							&& !userPersonalDetailWrapper.getEmail().isEmpty()) {
						maxNoRecords++;
					}
					usersList.add(finalUserIntegrationWrapper.getUserPersonalInfo().getUserName());	
				}
			} else {
				
				logger.info("----------{} : "+userIntegrationWrapper.get(0));
				List<BulkUserInformationWrapper> userIntegration = new ArrayList<>();
				userIntegration.add(userIntegrationWrapper.get(0));
				for (BulkUserInformationWrapper bulkUserInformationWrapper : userIntegration) {
					bulkUserInformationWrapper.setAppType(null);
					UserPersonalDetailWrapper userPersonalInfo=new UserPersonalDetailWrapper();
					bulkUserInformationWrapper.setUserPersonalInfo(userPersonalInfo);
					bulkUserInformationWrapper.setBasicAuthenticationDetail(null);
					bulkUserInformationWrapper.setCreatedTime(null);
					bulkUserInformationWrapper.setStatus(null);
					bulkUserInformationWrapper.setRoleName(null);
					bulkUserInformationWrapper.setRoleName(null);
					bulkUserInformationWrapper.setAppType(null); 
					UserRoleGeographyDetails userRole=new UserRoleGeographyDetails();
					Role role=null;
					List<UserRoleGeographyDetails> listOfRole = userIntegrationWrapper.get(0).getRoleMap().get("SP_FORESIGHT");
					List<UserRoleGeographyDetails> listOfSFRole = userIntegrationWrapper.get(0).getRoleMap().get("SP_SITEFORGE");

					if(listOfSFRole!=null&&!listOfSFRole.isEmpty()) {
					userRole=listOfSFRole.get(0);
					role=userRole.getRole();
					if(role!=null&&role.getRoleName()!=null&&!role.getRoleName().isEmpty()) {
					role.setRoleName(null);
					role.setWorkSpace(new WorkSpace());
					role.setTeam(new Team());
					role.setLevelType(null);
					}
					}
					
					if(listOfRole!=null&&!listOfRole.isEmpty()) {
						 userRole= listOfRole.get(0);
				    role=userRole.getRole();
					if(role!=null&&role.getRoleName()!=null&&!role.getRoleName().isEmpty()) {
					role.setRoleName(null);
					role.setLevelType(null); 
					role.setTeam(new Team());
					role.setWorkSpace(new WorkSpace());
					}
				}
				userIntegrationWrapper = userIntegration;
			}	
			}
			}

			messageMap.put(ForesightConstants.FILENAME, ExcelReadWriter.writeData(userIntegrationWrapper,filePath));
			messageMap.put(UmConstants.BULK_UPLOAD_SUCCESS, String.valueOf(successRecords));
			messageMap.put(UmConstants.BULK_UPLOAD_FAIL, String.valueOf((maxNoRecords - (successRecords))));
		} catch (Exception e) {
			logger.error("Error  while  creating   user request : {}", Utils.getStackTrace(e));
		}
		return messageMap;
	}

		
		private void userAuditFromBulkupload(BulkUserInformationWrapper userIntegrationWrapper,AuditActionType auditActionType,String actionName,String action) {
			try {
			logger.info("Going to audit for create/update user");	
			Audit audit=new Audit();
			audit.setAuditActionType(auditActionType);
			audit.setAuditActionName(actionName);
			audit.setAction(action);
			audit.setParameters(UmUtils.getGsonString(userIntegrationWrapper));
			audit.setDate(new Date());
			
			auditDao.create(audit);
			logger.info("audit created successfully");
			}catch(Exception e) {
				logger.error("Error  while  create user audit by  bulkupload : {}", Utils.getStackTrace(e));
			}
		} 
		
	private void setCustomerCareRole(BulkUserInformationWrapper finalUserIntegrationWrapper, User user) {
		
		logger.info("Inside setCustomerCareRole");
		
		// multirole testing
		List<String> ccRole = roleDao.getRoleNamesByPermissionName(
				ConfigUtils.getString("PERMISSION"));
		        logger.info("ccRoles : {} ", ccRole);
		
				Set<UserRole> ur=user.getUserRole();
				for (UserRole userRole : ur) {
					Role r=userRole.getRole();
					if (ccRole.stream().anyMatch(r.getRoleName()::equalsIgnoreCase)) {
						UserRoleGeographyDetails geographyDetails=new UserRoleGeographyDetails();
						
						Role role = new Role();
						role.setRoleId(r.getRoleId());
						role.setRoleName(r.getRoleName());
						role.setTeam(r.getTeam());
						role.setLevelType(r.getLevelType());
						role.setCreationTime(r.getCreationTime());
						role.setWorkSpace(r.getWorkSpace());
						role.setGeoType(r.getGeoType());
						
						geographyDetails.setRole(role);
						finalUserIntegrationWrapper.getRoleMap().get("SP_FORESIGHT").add(geographyDetails);
					}
				}
		}
	
		// TODO Auto-generated method stub
		
	

	private Role setPreviousActiveRole(User user) {
		logger.info("Inside setPreviousActiveRole");
		Role r = user.getActiveRole();

		Role role = new Role();
		role.setRoleId(r.getRoleId());
		role.setRoleName(r.getRoleName());
		role.setTeam(r.getTeam());
		role.setLevelType(r.getLevelType());
		role.setCreationTime(r.getCreationTime());
		role.setWorkSpace(r.getWorkSpace());
		role.setGeoType(r.getGeoType());

		return role;
	}

	private int UpdateExistingUser(int successRecords, BulkUserInformationWrapper finalUserIntegrationWrapper,
			User user,String type) {
		if (user.getUserName().equalsIgnoreCase(
				finalUserIntegrationWrapper.getUserPersonalInfo().getUserName())
				&& user.getEmail().equalsIgnoreCase(
						finalUserIntegrationWrapper.getUserPersonalInfo().getEmail())) {

			BasicAuthenticationDetail basicAuthenticationDetail = basicAuthenticationDetailDao
					.findByUsername(finalUserIntegrationWrapper.getUserPersonalInfo()
							.getUserName());
			if (!basicAuthenticationDetail.getDeleted()) {
				finalUserIntegrationWrapper.getUserPersonalInfo()
						.setUserName(user.getUserName());
				
				if("AddOn".equalsIgnoreCase(type)) {
					List<UserRoleGeographyDetails> userRole = addOnRole(user);
					finalUserIntegrationWrapper.getRoleMap().get("SP_FORESIGHT").addAll(userRole);
					if(finalUserIntegrationWrapper.getRoleMap().containsKey("SP_SITEFORGE"))
					finalUserIntegrationWrapper.getRoleMap().get("SP_SITEFORGE").addAll(setSFPreviousRole(finalUserIntegrationWrapper));
					
				}
				
				
				Map<String, String> responseMap = userService
						.updateUser(finalUserIntegrationWrapper);
				if (responseMap.get(CoreConstants.MESSAGE)
						.equalsIgnoreCase(UmConstants.USER_UPDATED_SUCCESSFULLY)) {
					successRecords++;
					finalUserIntegrationWrapper
							.setMessage(UmConstants.USER_UPDATED_SUCCESSFULLY);
				
					userAuditFromBulkupload(finalUserIntegrationWrapper, AuditActionType.UPDATE, AuditActionName.USERS_UPDATE, "USER_UPDATED_BY_BULKUPLOAD");

					
				} else {
					finalUserIntegrationWrapper
							.setMessage(responseMap.get(CoreConstants.MESSAGE));
				}
			} else {
				finalUserIntegrationWrapper
						.setMessage("User Already Exist.Unable to update Deleted User.");
			}
		} else {
			finalUserIntegrationWrapper.setMessage(
					"User Already Exist.Please Provide Existing Username or Email Id");
		}
		return successRecords;
	}

	private List<UserRoleGeographyDetails> setSFPreviousRole(BulkUserInformationWrapper finalUserIntegrationWrapper) {
		logger.info("inside setSFPreviousRole :{}");
		List<UserRoleGeographyDetails> userRoles=null;
		
		finalUserIntegrationWrapper=BulkUploadUtils.getRoleInfoByUserName(finalUserIntegrationWrapper.getUserPersonalInfo().getUserName());
		if(finalUserIntegrationWrapper!=null)
		{
			if(finalUserIntegrationWrapper.getRoleMap()!=null&&finalUserIntegrationWrapper.getRoleMap().containsKey("SP_SITEFORGE")) {
				
				userRoles =  finalUserIntegrationWrapper.getRoleMap().get("SP_SITEFORGE");
			}
		}
		
		return userRoles;//finalUserIntegrationWrapper.getRoleMap().get("SP_SITEFORGE");
	}


	private boolean isValidateGuestUser(BulkUserInformationWrapper finalUserIntegrationWrapper) {
		
		
		
		return false;
	}

	private Boolean setGuestRole(BulkUserInformationWrapper userIntegration) {
		logger.info("Inside setGuestRole");
		UserRoleGeographyDetails userRoleGeographyDetails = new UserRoleGeographyDetails();
		List<UserRoleGeographyDetails> listOfUser = new ArrayList<>();
		Role role =null;
		Boolean status=false;
		
        Role r =userIntegration.getUserPersonalInfo().getActiveRole().get(ForesightConstants.SP_FORESIGHT);
        logger.info("role : {} ",role);
		if (!userIntegration.getUserPersonalInfo().getActiveRole().containsKey(ForesightConstants.SP_FORESIGHT)) {
			logger.info("Active role is not available");
		    try {
			role = roleService.getGuestRole(true);
			if(role!=null) {
			logger.info("Guest Role : {} ", role);
			status=true;
			}else {
				status=false;
			}
			r = new Role();
			Team t = new Team();
			t.setName(role.getTeam().getName());
			t.setId(role.getTeam().getId());
              
			r.setRoleName(role.getRoleName());
			r.setRoleId(role.getRoleId());
			r.setGeoType(role.getGeoType());
			r.setLevelType(role.getLevelType());
			r.setTeam(t);
			r.setWorkSpace(role.getWorkSpace());
			r.setPermission(role.getPermission());
			
            userIntegration.getUserPersonalInfo().getActiveRole().put(ForesightConstants.SP_FORESIGHT, r);
			if (role != null && role.getRoleName() != null && !role.getRoleName().isEmpty()) {
				userRoleGeographyDetails.setRole(r);
				listOfUser.add(userRoleGeographyDetails);
			}

			userIntegration.getRoleMap().put(ForesightConstants.SP_FORESIGHT, listOfUser);
			logger.info("=======ROLE MAP AFTER SET=======" + userIntegration.getRoleMap());
		    }catch(Exception e) {
		    	status=false;
		    	logger.error("Error  while  setGuestRole : {}", Utils.getStackTrace(e));
		    }
		}else {
			logger.info("active role is available");
			status=true;
		}
		logger.info("status : {} ",status);
		return status;
	}

	private List<BulkUserInformationWrapper> setFinalUserData(List<BulkUserInformationWrapper> userIntegrationWrapper) {
		logger.info("Inside setFinalUserData");
		List<BulkUserInformationWrapper> finalUserData = new ArrayList<>();
		for (BulkUserInformationWrapper bulkUserInformationWrapper : userIntegrationWrapper) {

			if (finalUserData.contains(bulkUserInformationWrapper.getUserPersonalInfo().getEmail())) {

			}
		}
		return finalUserData;
	}

	public Boolean isUserExists(String username, String emailId) {
		logger.info("Inside isUserExists");
		if (username != null && !username.isEmpty() && emailId != null && !emailId.isEmpty()) {
			List<User> user = userDao.findUserListByUsernameOrEmail(username, emailId);
			if (user != null&&!user.isEmpty())
				return true;
		}
		return false;
	}

	public List<BulkUserInformationWrapper> setBulkUserData(List<Map<String, String>> mapList) {
		logger.info("Inside setBulkUserData");
		List<BulkUserInformationWrapper> userIntegrationWrapperList = new ArrayList<>();
        
		try {
			BulkUserInformationWrapper userIntegrationWrapper = new BulkUserInformationWrapper();
			UserPersonalDetailWrapper userPersonalDetail = null;
			mapList.remove(0);
			for (Map<String, String> mapData : mapList) {
				mapLevelType(mapData, userIntegrationWrapper);
				boolean idExists=false;
				if(mapData.get(ForesightConstants.EMAILD_ID)!=null&&!mapData.get(ForesightConstants.EMAILD_ID).isEmpty())
				idExists = userIntegrationWrapperList.stream().anyMatch(t -> t.getUserPersonalInfo().getEmail().equalsIgnoreCase(mapData.get(ForesightConstants.EMAILD_ID)));
				System.out.println("ID Exists : " + idExists);
				if (idExists) {
                    
					List<UserRoleGeographyDetails> gList = new ArrayList<>();
					BulkUserInformationWrapper matchingObject = userIntegrationWrapperList.stream().filter(
							p -> p.getUserPersonalInfo().getEmail().equalsIgnoreCase(mapData.get(ForesightConstants.EMAILD_ID)))
							.findAny().orElse(null);
                    
					if (mapData.get("Application Type") != null && !mapData.get("Application Type").isEmpty()
							&& mapData.get("Application Type").equalsIgnoreCase("FORESIGHT ROLE")) {

						if (matchingObject.getUserPersonalInfo().getActiveRole().get(ForesightConstants.SP_FORESIGHT) != null) {
							gList = getBulkRoleMapUserRoleGeography(mapData, getRoleByMapRoleAndMapTeam(
									mapData.get(ForesightConstants.ROLE), mapData.get(ForesightConstants.TEAM),
									mapData.get(ForesightConstants.WORKSPACE), mapData.get(ForesightConstants.LEVEL)));
							if (gList != null && !gList.isEmpty()) {
								logger.info("gList : {} ", gList);
								if (matchingObject != null && matchingObject.getRoleMap() != null
										&& matchingObject.getRoleMap().get(ForesightConstants.SP_FORESIGHT) != null) {
									matchingObject.getRoleMap().get(ForesightConstants.SP_FORESIGHT).add(gList.get(0));
								} else {
									matchingObject.getRoleMap().put(ForesightConstants.SP_FORESIGHT, gList);
								}
							} else {
                                 
								// =======================
								List<UserRoleGeographyDetails> gEmptyList = new ArrayList<>();
								UserRoleGeographyDetails details = new UserRoleGeographyDetails();
								Role role = new Role();
								role.setRoleName(mapData.get(ForesightConstants.ROLE));
								role.setLevelType(mapData.get(ForesightConstants.LEVEL));
								Team t = new Team();
								t.setName(mapData.get(ForesightConstants.TEAM));
								role.setTeam(t);
								WorkSpace workSpace = new WorkSpace();
								workSpace.setName(mapData.get(ForesightConstants.WORKSPACE));
								role.setWorkSpace(workSpace);
								details.setRole(role);

								List<GeographyL1> g1List = new ArrayList<>();
								GeographyL1 l1 = new GeographyL1();
								l1.setName(mapData.get(UmConstants.L1));
								g1List.add(l1);
								details.setGeographyL1(g1List);

								List<GeographyL2> g2List = new ArrayList<>();
								GeographyL2 l2 = new GeographyL2();
								l2.setName(mapData.get(UmConstants.L2));
								g2List.add(l2);
								details.setGeographyL2(g2List);

								List<GeographyL3> g3List = new ArrayList<>();
								GeographyL3 l3 = new GeographyL3();
								l3.setName(mapData.get(UmConstants.L3));
								g3List.add(l3);
								details.setGeographyL3(g3List);

								List<GeographyL4> g4List = new ArrayList<>();
								GeographyL4 l4 = new GeographyL4();
								l4.setName(mapData.get(UmConstants.L4));
								g4List.add(l4);
								details.setGeographyL4(g4List);

								gEmptyList.add(details);
								matchingObject.getRoleMap().get(ForesightConstants.SP_FORESIGHT).add(gEmptyList.get(0));
								// ================

							}
						} else {
							
							
							matchingObject.getUserPersonalInfo().getActiveRole().put(ForesightConstants.SP_FORESIGHT, getRoleByMapRoleAndMapTeam(mapData.get(ForesightConstants.ROLE).trim().toLowerCase(),
									mapData.get(ForesightConstants.TEAM).trim().toLowerCase(),
									mapData.get(ForesightConstants.WORKSPACE).trim().toLowerCase(),
									mapData.get(ForesightConstants.LEVEL).trim().toLowerCase()));
							
							
							/*matchingObject.getUserPersonalInfo().setActiveRole(
									setBulkActiveRole(mapData.get(ForesightConstants.ROLE).trim().toLowerCase(),
											mapData.get(ForesightConstants.TEAM).trim().toLowerCase(),
											mapData.get(ForesightConstants.WORKSPACE).trim().toLowerCase(),
											mapData.get(ForesightConstants.LEVEL).trim().toLowerCase(),
											ForesightConstants.SP_FORESIGHT));*/

							gList = getBulkRoleMapUserRoleGeography(mapData, getRoleByMapRoleAndMapTeam(
									mapData.get(ForesightConstants.ROLE), mapData.get(ForesightConstants.TEAM),
									mapData.get(ForesightConstants.WORKSPACE), mapData.get(ForesightConstants.LEVEL)));
							if (gList != null && !gList.isEmpty()) {
								logger.info("gList : {} ", gList);
								matchingObject.getRoleMap().put(ForesightConstants.SP_FORESIGHT, gList);
							} else {
								// ===================
								List<UserRoleGeographyDetails> gEmptyList = new ArrayList<>();
								UserRoleGeographyDetails details = new UserRoleGeographyDetails();
								Role role = new Role();
								role.setRoleName(mapData.get(ForesightConstants.ROLE));
								role.setLevelType(mapData.get(ForesightConstants.LEVEL));
								Team t = new Team();
								t.setName(mapData.get(ForesightConstants.TEAM));
								role.setTeam(t);
								WorkSpace workSpace = new WorkSpace();
								workSpace.setName(mapData.get(ForesightConstants.WORKSPACE));
								role.setWorkSpace(workSpace);
								details.setRole(role);

								List<GeographyL1> g1List = new ArrayList<>();
								GeographyL1 l1 = new GeographyL1();
								l1.setName(mapData.get(UmConstants.L1));
								g1List.add(l1);
								details.setGeographyL1(g1List);

								List<GeographyL2> g2List = new ArrayList<>();
								GeographyL2 l2 = new GeographyL2();
								l2.setName(mapData.get(UmConstants.L2));
								g2List.add(l2);
								details.setGeographyL2(g2List);

								List<GeographyL3> g3List = new ArrayList<>();
								GeographyL3 l3 = new GeographyL3();
								l3.setName(mapData.get(UmConstants.L3));
								g3List.add(l3);
								details.setGeographyL3(g3List);

								List<GeographyL4> g4List = new ArrayList<>();
								GeographyL4 l4 = new GeographyL4();
								l4.setName(mapData.get(UmConstants.L4));
								g4List.add(l4);
								details.setGeographyL4(g4List);

								gEmptyList.add(details);
								if (matchingObject != null && matchingObject.getRoleMap() != null
										&& matchingObject.getRoleMap().get(ForesightConstants.SP_FORESIGHT) != null) {
									matchingObject.getRoleMap().get(ForesightConstants.SP_FORESIGHT).add(gEmptyList.get(0));
								} else {
									matchingObject.getRoleMap().put(ForesightConstants.SP_FORESIGHT, gEmptyList);
								}
								//matchingObject.getRoleMap().get(ForesightConstants.SP_FORESIGHT).add(gEmptyList.get(0));
								
								// ================

							}
						}
					} else if (mapData.get("Application Type") != null && !mapData.get("Application Type").isEmpty()
							&& mapData.get("Application Type").equalsIgnoreCase("SITEFORGE ROLE")) {
						System.out.println("App Type : " + mapData.get("Application Type"));
						gList = new ArrayList<>();

						gList = setSiteforgeRoleAndGeography(mapData,
								getRoleByMapRoleAndMapTeamForSiteForge(mapData.get(ForesightConstants.ROLE),
										mapData.get(ForesightConstants.TEAM), mapData.get(ForesightConstants.WORKSPACE),
										mapData.get(ForesightConstants.LEVEL)));
						
						if (gList != null && !gList.isEmpty()) {
							logger.info("gList : {} ", gList);
							if (matchingObject.getRoleMap().get(ForesightConstants.SP_SITEFORGE) != null
									&& !matchingObject.getRoleMap().get(ForesightConstants.SP_SITEFORGE).isEmpty()) {
								matchingObject.getRoleMap().get(ForesightConstants.SP_SITEFORGE).add(gList.get(0));
								
							} else {
								System.out.println("For Active Role");
								if (mapData.get(ForesightConstants.ROLE) != null
										&& !mapData.get(ForesightConstants.ROLE).isEmpty()
										&& mapData.get(ForesightConstants.TEAM) != null
										&& !mapData.get(ForesightConstants.TEAM).isEmpty()
										&& mapData.get(ForesightConstants.WORKSPACE) != null
										&& !mapData.get(ForesightConstants.WORKSPACE).isEmpty()
										&& mapData.get(ForesightConstants.LEVEL) != null
										&& !mapData.get(ForesightConstants.LEVEL).isEmpty()) {
									matchingObject.getUserPersonalInfo().getActiveRole().put(
											ForesightConstants.SP_SITEFORGE,
											getRoleByMapRoleAndMapTeamForSiteForge(mapData.get(ForesightConstants.ROLE),
													mapData.get(ForesightConstants.TEAM),
													mapData.get(ForesightConstants.WORKSPACE),
													mapData.get(ForesightConstants.LEVEL)));
								}
								matchingObject.getRoleMap().put(ForesightConstants.SP_SITEFORGE,
										setSiteforgeRoleAndGeography(mapData,
												getRoleByMapRoleAndMapTeamForSiteForge(
														mapData.get(ForesightConstants.ROLE),
														mapData.get(ForesightConstants.TEAM),
														mapData.get(ForesightConstants.WORKSPACE),
														mapData.get(ForesightConstants.LEVEL))));
							}
						}else {
							logger.info("++++++++++");
							List<UserRoleGeographyDetails> gEmptyList = new ArrayList<>();
							UserRoleGeographyDetails details = new UserRoleGeographyDetails();
							Role role = new Role();
							role.setRoleName(mapData.get(ForesightConstants.ROLE));
							role.setLevelType(mapData.get(ForesightConstants.LEVEL));
							Team t = new Team();
							t.setName(mapData.get(ForesightConstants.TEAM));
							role.setTeam(t);
							WorkSpace workSpace = new WorkSpace();
							workSpace.setName(mapData.get(ForesightConstants.WORKSPACE));
							role.setWorkSpace(workSpace);
							details.setRole(role);

							List<GeographyL1> g1List = new ArrayList<>();
							GeographyL1 l1 = new GeographyL1();
							l1.setName(mapData.get(UmConstants.L1));
							g1List.add(l1);
							details.setGeographyL1(g1List);

							List<GeographyL2> g2List = new ArrayList<>();
							GeographyL2 l2 = new GeographyL2();
							l2.setName(mapData.get(UmConstants.L2));
							g2List.add(l2);
							details.setGeographyL2(g2List);

							List<GeographyL3> g3List = new ArrayList<>();
							GeographyL3 l3 = new GeographyL3();
							l3.setName(mapData.get(UmConstants.L3));
							g3List.add(l3);
							details.setGeographyL3(g3List);

							List<GeographyL4> g4List = new ArrayList<>();
							GeographyL4 l4 = new GeographyL4();
							l4.setName(mapData.get(UmConstants.L4));
							g4List.add(l4);
							details.setGeographyL4(g4List);
                  
							gEmptyList.add(details);
							
							matchingObject.getRoleMap().get(ForesightConstants.SP_SITEFORGE).add(gEmptyList.get(0));
							// ================
						}
					}

				} else {
					userIntegrationWrapper = new BulkUserInformationWrapper();
					userPersonalDetail = new UserPersonalDetailWrapper();
					
					logger.info("HEADER : {} ",mapData.get("header"));
					if(mapData.get("header") != null && !mapData.get("header").isEmpty()) {
						userIntegrationWrapper.setHeader(mapData.get("header").trim());	
					}
					
					if (mapData.get(ForesightConstants.FIRST_NAME) != null
							&& !mapData.get(ForesightConstants.FIRST_NAME).isEmpty()) {
						userPersonalDetail.setFirstName(mapData.get(ForesightConstants.FIRST_NAME));
					}
					if (mapData.get(ForesightConstants.MIDDLE_NAME) != null
							&& !mapData.get(ForesightConstants.MIDDLE_NAME).isEmpty()) {
						userPersonalDetail.setMiddleName(mapData.get(ForesightConstants.MIDDLE_NAME).trim());
					}

					if (mapData.get(ForesightConstants.LAST_NAME) != null
							&& !mapData.get(ForesightConstants.LAST_NAME).isEmpty()) {
						userPersonalDetail.setLastName(mapData.get(ForesightConstants.LAST_NAME).trim());
					}
					
					userPersonalDetail.setCreatorUsername(ConfigUtils.getString("DEFAULT_USER"));
					if (hasPermission()) {
						if (mapData.get(ForesightConstants.EMAILD_ID) != null
								&& !mapData.get(ForesightConstants.EMAILD_ID).isEmpty()) {
							userPersonalDetail.setEmail(mapData.get(ForesightConstants.EMAILD_ID).trim().toLowerCase());
							userPersonalDetail
									.setUserName(mapData.get(ForesightConstants.EMAILD_ID).trim().toLowerCase());
						}
					} else {
						if (mapData.get(ForesightConstants.EMAILD_ID) != null
								&& !mapData.get(ForesightConstants.EMAILD_ID).isEmpty()) {
							userPersonalDetail.setEmail(mapData.get(ForesightConstants.EMAILD_ID).trim().toLowerCase());
						}

						if (mapData.get(ForesightConstants.USER_NAME) != null
								&& !mapData.get(ForesightConstants.USER_NAME).isEmpty()) {
							userPersonalDetail
									.setUserName(mapData.get(ForesightConstants.USER_NAME).trim().toLowerCase());
						}
					}
                    
					
					
					if ("true".equalsIgnoreCase(ConfigUtils.getString("BULKUPLOAD_ACCESS_LEVEL"))) {
						if (mapData.get("Access Level") != null && !mapData.get("Access Level").isEmpty()) {
							userPersonalDetail.setAccessLevel(
									setBulkAccessLevel(mapData.get("Access Level").trim(), "SP_FORESIGHT"));
						}
					} else {
						userPersonalDetail.setAccessLevel(setBulkAccessLevel("web internet", "SP_FORESIGHT"));
					}

					if (mapData.get(UmConstants.ADDRESS) != null && !mapData.get(UmConstants.ADDRESS).isEmpty()) {
						userPersonalDetail
								.setUserAddress(setBulkAddress(mapData.get(UmConstants.ADDRESS).trim().toLowerCase()));
					}
					if (mapData.get(ForesightConstants.ORGANIZATION_NAME) != null
							&& !mapData.get(ForesightConstants.ORGANIZATION_NAME).isEmpty()
							&& mapData.get(ForesightConstants.AUTHENTICATION_TYPE).trim() != null
							&& !mapData.get(ForesightConstants.AUTHENTICATION_TYPE).trim().isEmpty()) {
						userPersonalDetail.setOrgAuthenticationType(setBulkORGAuthenticationType(
								mapData.get(ForesightConstants.ORGANIZATION_NAME).trim().toLowerCase(),
								mapData.get(ForesightConstants.AUTHENTICATION_TYPE).trim()));
					}
					userIntegrationWrapper.setOrgName(mapData.get(ForesightConstants.ORGANIZATION_NAME)); 
					userIntegrationWrapper.setAuthtype(mapData.get(ForesightConstants.AUTHENTICATION_TYPE));
					userIntegrationWrapper.setRoleName(mapData.get(ForesightConstants.ROLE));
					userIntegrationWrapper.setWorkspace(mapData.get(ForesightConstants.WORKSPACE));
					userIntegrationWrapper.setTeamName(mapData.get(ForesightConstants.TEAM));
					userIntegrationWrapper.setLevel(mapData.get(ForesightConstants.LEVEL));
					userIntegrationWrapper.setL1(mapData.get(UmConstants.L1));
					userIntegrationWrapper.setL2(mapData.get(UmConstants.L2));
					userIntegrationWrapper.setL3(mapData.get(UmConstants.L3));
					userIntegrationWrapper.setL4(mapData.get(UmConstants.L4));
					userIntegrationWrapper.setOtherGeography(mapData.get(ForesightConstants.OTHER));
					
					mapLevelType(mapData, userIntegrationWrapper);

					if(mapData.get("Phone Number")!=null&&!mapData.get("Phone Number").isEmpty()) {
						userPersonalDetail.setContactNumber(mapData.get("Phone Number").trim().toLowerCase());
					}
					
					userPersonalDetail.setInitiator(securedUserService.getinitiator());
					userPersonalDetail.setSpList(systemConfigurationService.getSplist());

					/*
					 * userPersonalDetail.setInitiator("SP_FORESIGHT"); List<String> spList=new
					 * ArrayList(); spList.add("SP_FORESIGHT");
					 * userPersonalDetail.setSpList(spList);
					 */

					userPersonalDetail.setEnabled(true);
					userPersonalDetail.setLocked(false);

					userIntegrationWrapper.setUserPersonalInfo(userPersonalDetail);
					userIntegrationWrapper.setAppType(mapData.get("Application Type"));
					Map<String, List<UserRoleGeographyDetails>> roleMap = new HashMap<>();
					List<UserRoleGeographyDetails> gList = new ArrayList<>();
					if ("FORESIGHT ROLE".equalsIgnoreCase(mapData.get("Application Type"))) {
						gList = new ArrayList<>();
						try {
							if (mapData.get(ForesightConstants.ROLE) != null
									&& !mapData.get(ForesightConstants.ROLE).isEmpty()
									&& mapData.get(ForesightConstants.TEAM) != null
									&& !mapData.get(ForesightConstants.TEAM).isEmpty()
									&& mapData.get(ForesightConstants.WORKSPACE) != null
									&& !mapData.get(ForesightConstants.WORKSPACE).isEmpty()
									&& mapData.get(ForesightConstants.LEVEL) != null
									&& !mapData.get(ForesightConstants.LEVEL).isEmpty()) {
								userPersonalDetail.setActiveRole(
										setBulkActiveRole(mapData.get(ForesightConstants.ROLE).trim().toLowerCase(),
												mapData.get(ForesightConstants.TEAM).trim().toLowerCase(),
												mapData.get(ForesightConstants.WORKSPACE).trim().toLowerCase(),
												mapData.get(ForesightConstants.LEVEL).trim().toLowerCase(),
												ForesightConstants.SP_FORESIGHT));

							}

							logger.info("Role : {} ", mapData.get(ForesightConstants.ROLE));
							logger.info("Team : {} ", mapData.get(ForesightConstants.TEAM));
							logger.info("Workspace : {} ", mapData.get(ForesightConstants.WORKSPACE));
							logger.info("Level : {} ", mapData.get(ForesightConstants.LEVEL));

							gList = getBulkRoleMapUserRoleGeography(mapData, getRoleByMapRoleAndMapTeam(
									mapData.get(ForesightConstants.ROLE), mapData.get(ForesightConstants.TEAM),
									mapData.get(ForesightConstants.WORKSPACE), mapData.get(ForesightConstants.LEVEL)));
						} catch (Exception e) {
							logger.error("Error while  getBulkRoleMapUserRoleGeography   : {}", Utils.getStackTrace(e));
						}
						logger.info("gList : {} ", gList);
						if (gList != null && !gList.isEmpty()) {
							roleMap.put(ForesightConstants.SP_FORESIGHT, gList);
						} else {

							// ===================
							List<UserRoleGeographyDetails> gEmptyList = new ArrayList<>();
							UserRoleGeographyDetails details = new UserRoleGeographyDetails();
							Role role = new Role();
							role.setRoleName(mapData.get(ForesightConstants.ROLE));
							role.setLevelType(mapData.get(ForesightConstants.LEVEL));
							Team t = new Team();
							t.setName(mapData.get(ForesightConstants.TEAM));
							role.setTeam(t);
							WorkSpace workSpace = new WorkSpace();
							workSpace.setName(mapData.get(ForesightConstants.WORKSPACE));
							role.setWorkSpace(workSpace);
							details.setRole(role);

							List<GeographyL1> g1List = new ArrayList<>();
							GeographyL1 l1 = new GeographyL1();
							l1.setName(mapData.get(UmConstants.L1));
							g1List.add(l1);
							details.setGeographyL1(g1List);

							List<GeographyL2> g2List = new ArrayList<>();
							GeographyL2 l2 = new GeographyL2();
							l2.setName(mapData.get(UmConstants.L2));
							g2List.add(l2);
							details.setGeographyL2(g2List);

							List<GeographyL3> g3List = new ArrayList<>();
							GeographyL3 l3 = new GeographyL3();
							l3.setName(mapData.get(UmConstants.L3));
							g3List.add(l3);
							details.setGeographyL3(g3List);

							List<GeographyL4> g4List = new ArrayList<>();
							GeographyL4 l4 = new GeographyL4();
							l4.setName(mapData.get(UmConstants.L4));
							g4List.add(l4);
							details.setGeographyL4(g4List);

							gEmptyList.add(details);

							// ================

							roleMap.put(ForesightConstants.SP_FORESIGHT, gEmptyList);
						}
					} else if ("SITEFORGE ROLE".equalsIgnoreCase(mapData.get("Application Type"))) {
						gList = new ArrayList<>();

						if (mapData.get(ForesightConstants.ROLE) != null
								&& !mapData.get(ForesightConstants.ROLE).isEmpty()
								&& mapData.get(ForesightConstants.TEAM) != null
								&& !mapData.get(ForesightConstants.TEAM).isEmpty()
								&& mapData.get(ForesightConstants.WORKSPACE) != null
								&& !mapData.get(ForesightConstants.WORKSPACE).isEmpty()
								&& mapData.get(ForesightConstants.LEVEL) != null
								&& !mapData.get(ForesightConstants.LEVEL).isEmpty()) {
							userPersonalDetail.setActiveRole(setBulkActiveRoleForSiteForge(
									mapData.get(ForesightConstants.ROLE).trim().toLowerCase(),
									mapData.get(ForesightConstants.TEAM).trim().toLowerCase(),
									mapData.get(ForesightConstants.WORKSPACE).trim().toLowerCase(),
									mapData.get(ForesightConstants.LEVEL).trim().toLowerCase(),
									ForesightConstants.SP_SITEFORGE));
						}

						logger.info("Role : {} ", mapData.get(ForesightConstants.ROLE));
						logger.info("Team : {} ", mapData.get(ForesightConstants.TEAM));
						logger.info("Workspace : {} ", mapData.get(ForesightConstants.WORKSPACE));
						logger.info("Level : {} ", mapData.get(ForesightConstants.LEVEL));

						
						gList = setSiteforgeRoleAndGeography(mapData,
								getRoleByMapRoleAndMapTeamForSiteForge(mapData.get(ForesightConstants.ROLE),
										mapData.get(ForesightConstants.TEAM), mapData.get(ForesightConstants.WORKSPACE),
										mapData.get(ForesightConstants.LEVEL)));
						logger.info("gList : {} ", gList);
						if (gList != null && !gList.isEmpty()) {
							roleMap.put(ForesightConstants.SP_SITEFORGE, gList);
						} else {

							// ===================
							List<UserRoleGeographyDetails> gEmptyList = new ArrayList<>();
							UserRoleGeographyDetails details = new UserRoleGeographyDetails();
							Role role = new Role();
							role.setRoleName(mapData.get(ForesightConstants.ROLE));
							role.setLevelType(mapData.get(ForesightConstants.LEVEL));

							Team t = new Team();
							t.setName(mapData.get(ForesightConstants.TEAM));
							role.setTeam(t);
							WorkSpace workSpace = new WorkSpace();
							workSpace.setName(mapData.get(ForesightConstants.WORKSPACE));
							role.setWorkSpace(workSpace);
							details.setRole(role);

							List<GeographyL1> g1List = new ArrayList<>();
							GeographyL1 l1 = new GeographyL1();
							l1.setName(mapData.get(UmConstants.L1));
							g1List.add(l1);
							details.setGeographyL1(g1List);

							List<GeographyL2> g2List = new ArrayList<>();
							GeographyL2 l2 = new GeographyL2();
							l2.setName(mapData.get(UmConstants.L2));
							g2List.add(l2);
							details.setGeographyL2(g2List);

							List<GeographyL3> g3List = new ArrayList<>();
							GeographyL3 l3 = new GeographyL3();
							l3.setName(mapData.get(UmConstants.L3));
							g3List.add(l3);
							details.setGeographyL3(g3List);

							List<GeographyL4> g4List = new ArrayList<>();
							GeographyL4 l4 = new GeographyL4();
							l4.setName(mapData.get(UmConstants.L4));
							g4List.add(l4);
							details.setGeographyL4(g4List);

							gEmptyList.add(details);
							// ================

							roleMap.put(ForesightConstants.SP_SITEFORGE, gEmptyList);

						}
					}

					userIntegrationWrapper.setRoleMap(roleMap);
					userIntegrationWrapperList.add(userIntegrationWrapper);
				}
				
			}
		} catch (Exception e) {
			logger.error("Error while  creating  user  : {}", Utils.getStackTrace(e));
		}
		return userIntegrationWrapperList;
	}

	private boolean hasPermission() {
		logger.info("Inside hasPermission");
		Boolean status = false;
		Set<String> createUserPermissionList = null;
		User contextUserInfo = userContextService.getUserInContextnew();
		 if(contextUserInfo==null)  
			 contextUserInfo = userDao.findByUserName("system.user");
		
		if (contextUserInfo != null) {
			logger.info("Inside UMUtils contextUserInfo activerrole : {} ",
					contextUserInfo.getActiveRole().getRoleId());
			Role activeRole = contextUserInfo.getActiveRole();
			createUserPermissionList = UmUtils.getPermissionList(activeRole.getPermission());
			if (contextUserInfo.getActiveRole().getPermission() != null
					&& !contextUserInfo.getActiveRole().getPermission().isEmpty()) {
				Set<String> permissionList =customerinfo.getPermissions();
				if (permissionList.contains("ROLE_ADMIN_UM_cannotEditUsername")) {
					status = true;
				}
			}
		}
		return status;
	}

	public void mapLevelType(Map<String, String> mapData, BulkUserInformationWrapper userIntegrationWrapper) {

		logger.info("mapData : {} ",mapData);
		if (mapData.get(ForesightConstants.LEVEL) != null && !mapData.get(ForesightConstants.LEVEL).isEmpty()) {

			if (mapData.get(ForesightConstants.LEVEL)
					.equalsIgnoreCase(ConfigUtils.getString(ForesightConstants.BULKUPLOAD_ZONE).trim())) {
				mapData.put(ForesightConstants.LEVEL, ForesightConstants.L1);
			}
			if (mapData.get(ForesightConstants.LEVEL)
					.equalsIgnoreCase(ConfigUtils.getString(ForesightConstants.BULKUPLOAD_CIRCLE).trim())) {
				mapData.put(ForesightConstants.LEVEL, ForesightConstants.L2);
			}
			if (mapData.get(ForesightConstants.LEVEL)
					.equalsIgnoreCase(ConfigUtils.getString(ForesightConstants.BULKUPLOAD_CITY).trim())) {
				mapData.put(ForesightConstants.LEVEL, ForesightConstants.L3);
			}
			if (mapData.get(ForesightConstants.LEVEL)
					.equalsIgnoreCase(ConfigUtils.getString(ForesightConstants.BULKUPLOAD_NETWORK_CLUSTER).trim())) {
				mapData.put(ForesightConstants.LEVEL, ForesightConstants.L4);
			}

			
			if (mapData.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.SALES_REGION)) {
				mapData.put(ForesightConstants.LEVEL, ForesightConstants.SALESL1);
				userIntegrationWrapper.setL1(mapData.get(ForesightConstants.L1));

			}
			if (mapData.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.SALES_CLUSTER)) {
				mapData.put(ForesightConstants.LEVEL, ForesightConstants.SALESL2);
				userIntegrationWrapper.setL1(mapData.get(ForesightConstants.L1));
				userIntegrationWrapper.setL2(mapData.get(ForesightConstants.L2));

			}
			if (mapData.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.L3)) {
				mapData.put(ForesightConstants.LEVEL, ForesightConstants.SALESL3);
				userIntegrationWrapper.setL1(mapData.get(ForesightConstants.L1));
				userIntegrationWrapper.setL2(mapData.get(ForesightConstants.L2));
				userIntegrationWrapper.setL3(mapData.get(ForesightConstants.L3));

			}
			if (mapData.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.L4)) {
				mapData.put(ForesightConstants.LEVEL, ForesightConstants.SALESL4);
				userIntegrationWrapper.setL1(mapData.get(ForesightConstants.L1));
				userIntegrationWrapper.setL2(mapData.get(ForesightConstants.L2));
				userIntegrationWrapper.setL3(mapData.get(ForesightConstants.L3));
				userIntegrationWrapper.setL4(mapData.get(ForesightConstants.L4));
			}

		}
	}

	private List<UserRoleGeographyDetails> setSiteforgeRoleAndGeography(Map<String, String> map, Role role) {
		logger.info("Inside setSiteforgeRoleAndGeography");
		UserRoleGeographyDetails userRoleGeographyDetails = new UserRoleGeographyDetails();
		List<UserRoleGeographyDetails> listOfUser = new ArrayList<>();
		if (role != null && role.getRoleName() != null && !role.getRoleName().isEmpty()) {
			userRoleGeographyDetails.setRole(role);
			logger.info("level {} : ", map.get(ForesightConstants.LEVEL));
			if (role.getGeoType().equalsIgnoreCase(ForesightConstants.SALES)) {
				if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALES_NHQ)) {
				}
				if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL1)) {
					SalesL1 gL1 = null;
					logger.info("check SALES_L1 Level");
					logger.info("zone value {} ", map.get(ForesightConstants.L1));
					List<SalesL1> sL1 = new ArrayList<>();
					if (map.get("L1") != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("L1 value {} : ", map.get(ForesightConstants.L1));

						List<GeoGraphyInfoWrapper> geoGraphyInfoWrappers = BulkUploadUtils
								.findAllGeographyInfoL1(role.getGeoType());
						GeoGraphyInfoWrapper matchingObject = geoGraphyInfoWrappers.stream()
								.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L1))).findAny()
								.orElse(null);
						logger.info("matchingObject : {} ", matchingObject);
						gL1 = new SalesL1();
						if (matchingObject != null) {
							gL1.setName(matchingObject.getName());
							gL1.setId(matchingObject.getId());
							gL1.setLatitude(matchingObject.getLatitude());
							gL1.setLongitude(matchingObject.getLongitude());
						}
						if (gL1 != null) {
							sL1.add(gL1);
						}
					}

					userRoleGeographyDetails.setSalesl1(sL1);
				}
				if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL2)) {
					logger.info("Inside Level SalesL2");
					SalesL1 salesL1 = null;

					List<SalesL1> geographyL1 = new ArrayList<>();
					List<SalesL2> geographyL2 = new ArrayList<>();

					if (map.get("L1") != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("L1 value {} : ", map.get(ForesightConstants.L1));

						List<GeoGraphyInfoWrapper> geoGraphyInfoWrappers = BulkUploadUtils
								.findAllGeographyInfoL1(role.getGeoType());
						GeoGraphyInfoWrapper matchingObject = geoGraphyInfoWrappers.stream()
								.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L1))).findAny()
								.orElse(null);
						logger.info("matchingObject : {} ", matchingObject);
						SalesL1 gL1 = new SalesL1();
						if (matchingObject != null) {
							gL1.setName(matchingObject.getName());
							gL1.setId(matchingObject.getId());
							gL1.setLatitude(matchingObject.getLatitude());
							gL1.setLongitude(matchingObject.getLongitude());

						}
						if (gL1 != null) {
							logger.info("zone value : {} ", map.get(ForesightConstants.L1));
							geographyL1.add(gL1);
							if (map.get(ForesightConstants.L2) != null && !map.get(ForesightConstants.L2).isEmpty()) {
								logger.info("circle value : " + map.get(ForesightConstants.L2));
								List<GeoGraphyInfoWrapper> gL2List = BulkUploadUtils
										.getGeographyL2ByL1Name(gL1.getName(), role.getGeoType());

								GeoGraphyInfoWrapper gInfoL2 = gL2List.stream()
										.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L2)))
										.findAny().orElse(null);

								if (gInfoL2 != null) {
									SalesL2 gL2 = new SalesL2();
									gL2.setId(gInfoL2.getId());
									gL2.setName(gInfoL2.getName());
									gL2.setLatitude(gInfoL2.getLatitude());
									gL2.setLongitude(gInfoL2.getLongitude());
									gL2.setSalesL1(gL1);
									boolean checkL2 = checkSalesL2withinSalesL1(gL2, gL1);
									if (checkL2) {
										geographyL2.add(gL2);
									}
								}

							}
						}
					}

					userRoleGeographyDetails.setSalesl1(geographyL1);
					userRoleGeographyDetails.setSalesl2(geographyL2);
				}
				if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL3)) {
					logger.info("Inside Level SALES_L3");
					List<SalesL1> geographyL1 = new ArrayList<>();
					List<SalesL2> geographyL2 = new ArrayList<>();
					List<SalesL3> geographyL3 = new ArrayList<>();

					if (map.get("L1") != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("L1 value {} : ", map.get(ForesightConstants.L1));

						List<GeoGraphyInfoWrapper> geoGraphyInfoWrappers = BulkUploadUtils
								.findAllGeographyInfoL1(role.getGeoType());
						GeoGraphyInfoWrapper matchingObject = geoGraphyInfoWrappers.stream()
								.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L1))).findAny()
								.orElse(null);
						logger.info("matchingObject : {} ", matchingObject);
						SalesL1 gL1 = new SalesL1();
						if (matchingObject != null) {
							gL1.setName(matchingObject.getName());
							gL1.setId(matchingObject.getId());
							gL1.setLatitude(matchingObject.getLatitude());
							gL1.setLongitude(matchingObject.getLongitude());
						}
						if (gL1 != null) {
							logger.info("zone value : {} ", map.get(ForesightConstants.L1));
							geographyL1.add(gL1);
							if (map.get(ForesightConstants.L2) != null && !map.get(ForesightConstants.L2).isEmpty()) {
								logger.info("circle value : " + map.get(ForesightConstants.L2));
								List<GeoGraphyInfoWrapper> gL2List = BulkUploadUtils
										.getGeographyL2ByL1Name(gL1.getName(), role.getGeoType());

								GeoGraphyInfoWrapper gInfoL2 = gL2List.stream()
										.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L2)))
										.findAny().orElse(null);

								if (gInfoL2 != null) {
									SalesL2 gL2 = new SalesL2();
									gL2.setId(gInfoL2.getId());
									gL2.setName(gInfoL2.getName());
									gL2.setLatitude(gInfoL2.getLatitude());
									gL2.setLongitude(gInfoL2.getLongitude());
									gL2.setSalesL1(gL1);
									boolean checkL2 = checkSalesL2withinSalesL1(gL2, gL1);
									if (checkL2) {
										geographyL2.add(gL2);

										if (map.get(ForesightConstants.L3) != null
												&& !map.get(ForesightConstants.L3).isEmpty()) {

											List<GeoGraphyInfoWrapper> g3List = BulkUploadUtils
													.getGeoGraphyL3ByL2Name(gL2.getName(), role.getGeoType());

											if (g3List != null) {
												GeoGraphyInfoWrapper gInfoL3 = g3List.stream()
														.filter(p -> p.getName()
																.equalsIgnoreCase(map.get(ForesightConstants.L3)))
														.findAny().orElse(null);

												SalesL3 gL3 = new SalesL3();
												gL3.setId(gInfoL3.getId());
												gL3.setName(gInfoL3.getName());
												gL3.setLatitude(gInfoL3.getLatitude());
												gL3.setLongitude(gInfoL3.getLongitude());
												gL3.setSalesL2(gL2);
												boolean checkL3 = checkSalesL3withinSalesL2(gL3, gL2);
												if (checkL3) {
													geographyL3.add(gL3);

												}
											}
										}
									}
								}

							}
						}
					}

					userRoleGeographyDetails.setSalesl1(geographyL1);
					userRoleGeographyDetails.setSalesl2(geographyL2);
					userRoleGeographyDetails.setSalesl3(geographyL3);
				}
				if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL4)) {

					logger.info("Inside Level sales_L4");
					List<SalesL1> geographyL1 = new ArrayList<>();
					List<SalesL2> geographyL2 = new ArrayList<>();
					List<SalesL3> geographyL3 = new ArrayList<>();
					List<SalesL4> geographyL4 = new ArrayList<>();

					if (map.get("L1") != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("L1 value {} : ", map.get(ForesightConstants.L1));

						List<GeoGraphyInfoWrapper> geoGraphyInfoWrappers = BulkUploadUtils
								.findAllGeographyInfoL1(role.getGeoType());
						GeoGraphyInfoWrapper matchingObject = geoGraphyInfoWrappers.stream()
								.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L1))).findAny()
								.orElse(null);
						logger.info("matchingObject : {} ", matchingObject);
						SalesL1 gL1 = new SalesL1();
						if (matchingObject != null) {
							gL1.setName(matchingObject.getName());
							gL1.setId(matchingObject.getId());
							gL1.setLatitude(matchingObject.getLatitude());
							gL1.setLongitude(matchingObject.getLongitude());
						}
						if (gL1 != null) {
							logger.info("zone value : {} ", map.get(ForesightConstants.L1));
							geographyL1.add(gL1);
							if (map.get(ForesightConstants.L2) != null && !map.get(ForesightConstants.L2).isEmpty()) {
								logger.info("circle value : " + map.get(ForesightConstants.L2));
								List<GeoGraphyInfoWrapper> gL2List = BulkUploadUtils
										.getGeographyL2ByL1Name(gL1.getName(), role.getGeoType());

								GeoGraphyInfoWrapper gInfoL2 = gL2List.stream()
										.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L2)))
										.findAny().orElse(null);

								if (gInfoL2 != null) {
									SalesL2 gL2 = new SalesL2();
									gL2.setId(gInfoL2.getId());
									gL2.setName(gInfoL2.getName());
									gL2.setLatitude(gInfoL2.getLatitude());
									gL2.setLongitude(gInfoL2.getLongitude());
									gL2.setSalesL1(gL1);
									boolean checkL2 = checkSalesL2withinSalesL1(gL2, gL1);
									if (checkL2) {
										geographyL2.add(gL2);

										if (map.get(ForesightConstants.L3) != null
												&& !map.get(ForesightConstants.L3).isEmpty()) {

											List<GeoGraphyInfoWrapper> g3List = BulkUploadUtils
													.getGeoGraphyL3ByL2Name(gL2.getName(), role.getGeoType());
											GeoGraphyInfoWrapper gInfoL3 = g3List.stream().filter(
													p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L3)))
													.findAny().orElse(null);

											if (gInfoL3 != null) {

												SalesL3 gL3 = new SalesL3();
												gL3.setId(gInfoL3.getId());
												gL3.setName(gInfoL3.getName());
												gL3.setLatitude(gInfoL3.getLatitude());
												gL3.setLongitude(gInfoL3.getLongitude());
												gL3.setSalesL2(gL2);
												boolean checkL3 = checkSalesL3withinSalesL2(gL3, gL2);
												if (checkL3) {
													geographyL3.add(gL3);
													if (map.get(ForesightConstants.L4) != null
															&& !map.get(ForesightConstants.L4).isEmpty()) {
														List<GeoGraphyInfoWrapper> gL4Lists = BulkUploadUtils
																.getGeographyL4ByL3Name(gL3.getName(),
																		role.getGeoType());
														GeoGraphyInfoWrapper gInfoL4 = gL4Lists.stream()
																.filter(p -> p.getName().equalsIgnoreCase(
																		map.get(ForesightConstants.L4)))
																.findAny().orElse(null);

														if (gInfoL4 != null) {
															SalesL4 gL4 = new SalesL4();
															gL4.setName(gInfoL4.getName());
															gL4.setId(gInfoL4.getId());
															gL4.setLatitude(gInfoL4.getLatitude());
															gL4.setLongitude(gInfoL4.getLongitude());
															gL4.setSalesL3(gL3);
															boolean checkL4 = checkSalesL4withinSalesL3(gL4, gL3);
															if (checkL4) {
																geographyL4.add(gL4);
															}
														}
													}
												}
											}
										}

									}
								}

							}
						}
					}

					userRoleGeographyDetails.setSalesl1(geographyL1);
					userRoleGeographyDetails.setSalesl2(geographyL2);
					userRoleGeographyDetails.setSalesl3(geographyL3);
					userRoleGeographyDetails.setSalesl4(geographyL4);

				}

			} else {

				if (map.get(ForesightConstants.LEVEL).equalsIgnoreCase("SUPER_ADMIN")) {

				}
				if (map.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.NHQ)) {

				}

				if (map.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.L4)) {
					logger.info("Inside Level L4");
					List<GeographyL1> geographyL1 = new ArrayList<>();
					List<GeographyL2> geographyL2 = new ArrayList<>();
					List<GeographyL3> geographyL3 = new ArrayList<>();
					List<GeographyL4> geographyL4 = new ArrayList<>();

					if (map.get("L1") != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("L1 value {} : ", map.get(ForesightConstants.L1));

						List<GeoGraphyInfoWrapper> geoGraphyInfoWrappers = BulkUploadUtils
								.findAllGeographyInfoL1(role.getGeoType());
						GeoGraphyInfoWrapper matchingObject = geoGraphyInfoWrappers.stream()
								.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L1))).findAny()
								.orElse(null);
						logger.info("matchingObject : {} ", matchingObject);
						GeographyL1 gL1 = new GeographyL1();
						if (matchingObject != null) {
							gL1.setName(matchingObject.getName());
							gL1.setId(matchingObject.getId());
							gL1.setLatitude(matchingObject.getLatitude());
							gL1.setLongitude(matchingObject.getLongitude());
						}
						if (gL1 != null) {
							logger.info("zone value : {} ", map.get(ForesightConstants.L1));
							geographyL1.add(gL1);
							if (map.get(ForesightConstants.L2) != null && !map.get(ForesightConstants.L2).isEmpty()) {
								logger.info("circle value : " + map.get(ForesightConstants.L2));
								List<GeoGraphyInfoWrapper> gL2List = BulkUploadUtils
										.getGeographyL2ByL1Name(gL1.getName(), role.getGeoType());

								GeoGraphyInfoWrapper gInfoL2 = gL2List.stream()
										.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L2)))
										.findAny().orElse(null);

								if (gInfoL2 != null) {
									GeographyL2 gL2 = new GeographyL2();
									gL2.setId(gInfoL2.getId());
									gL2.setName(gInfoL2.getName());
									gL2.setLatitude(gInfoL2.getLatitude());
									gL2.setLongitude(gInfoL2.getLongitude());
									gL2.setGeographyL1(gL1);
									boolean checkL2 = checkL2withinL1(gL2, gL1);
									if (checkL2) {
										geographyL2.add(gL2);

										if (map.get(ForesightConstants.L3) != null
												&& !map.get(ForesightConstants.L3).isEmpty()) {

											List<GeoGraphyInfoWrapper> g3List = BulkUploadUtils
													.getGeoGraphyL3ByL2Name(gL2.getName(), role.getGeoType());
											GeoGraphyInfoWrapper gInfoL3 = g3List.stream().filter(
													p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L3)))
													.findAny().orElse(null);

											if (gInfoL3 != null) {

												GeographyL3 gL3 = new GeographyL3();
												gL3.setId(gInfoL3.getId());
												gL3.setName(gInfoL3.getName());
												gL3.setLatitude(gInfoL3.getLatitude());
												gL3.setLongitude(gInfoL3.getLongitude());
												gL3.setGeographyL2(gL2);
												boolean checkL3 = checkL3withinL2(gL3, gL2);
												if (checkL3) {
													geographyL3.add(gL3);
													if (map.get(ForesightConstants.L4) != null
															&& !map.get(ForesightConstants.L4).isEmpty()) {

														List<GeoGraphyInfoWrapper> gL4Lists = BulkUploadUtils
																.getGeographyL4ByL3Name(gL3.getName(),
																		role.getGeoType());

														GeoGraphyInfoWrapper gInfoL4 = gL4Lists.stream()
																.filter(p -> p.getName().equalsIgnoreCase(
																		map.get(ForesightConstants.L4)))
																.findAny().orElse(null);

														if (gInfoL4 != null) {
															GeographyL4 gL4 = new GeographyL4();
															gL4.setName(gInfoL4.getName());
															gL4.setId(gInfoL4.getId());
															gL4.setLatitude(gInfoL4.getLatitude());
															gL4.setLongitude(gInfoL4.getLongitude());
															gL4.setGeographyL2(gL3);
															boolean checkL4 = checkL4withinL3(gL4, gL3);
															if (checkL4) {
																geographyL4.add(gL4);
															}
														}
													}
												}
											}
										}

									}
								}

							}
						}
					}
					userRoleGeographyDetails.setGeographyL1(geographyL1);
					userRoleGeographyDetails.setGeographyL2(geographyL2);
					userRoleGeographyDetails.setGeographyL3(geographyL3);
					userRoleGeographyDetails.setGeographyL4(geographyL4);
				}
				if (map.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.L3)) {
					logger.info("Inside Level L3");
					List<GeographyL1> geographyL1 = new ArrayList<>();
					List<GeographyL2> geographyL2 = new ArrayList<>();
					List<GeographyL3> geographyL3 = new ArrayList<>();

					if (map.get("L1") != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("L1 value {} : ", map.get(ForesightConstants.L1));

						List<GeoGraphyInfoWrapper> geoGraphyInfoWrappers = BulkUploadUtils
								.findAllGeographyInfoL1(role.getGeoType());
						GeoGraphyInfoWrapper matchingObject = geoGraphyInfoWrappers.stream()
								.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L1))).findAny()
								.orElse(null);
						logger.info("matchingObject : {} ", matchingObject);
						GeographyL1 gL1 = new GeographyL1();
						if (matchingObject != null) {
							gL1.setName(matchingObject.getName());
							gL1.setId(matchingObject.getId());
							gL1.setLatitude(matchingObject.getLatitude());
							gL1.setLongitude(matchingObject.getLongitude());
						}
						if (gL1 != null) {
							logger.info("zone value : {} ", map.get(ForesightConstants.L1));
							geographyL1.add(gL1);
							if (map.get(ForesightConstants.L2) != null && !map.get(ForesightConstants.L2).isEmpty()) {
								logger.info("circle value : " + map.get(ForesightConstants.L2));
								List<GeoGraphyInfoWrapper> gL2List = BulkUploadUtils
										.getGeographyL2ByL1Name(gL1.getName(), role.getGeoType());

								GeoGraphyInfoWrapper gInfoL2 = gL2List.stream()
										.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L2)))
										.findAny().orElse(null);

								if (gInfoL2 != null) {
									GeographyL2 gL2 = new GeographyL2();
									gL2.setId(gInfoL2.getId());
									gL2.setName(gInfoL2.getName());
									gL2.setLatitude(gInfoL2.getLatitude());
									gL2.setLongitude(gInfoL2.getLongitude());
									gL2.setGeographyL1(gL1);
									boolean checkL2 = checkL2withinL1(gL2, gL1);
									if (checkL2) {
										geographyL2.add(gL2);

										if (map.get(ForesightConstants.L3) != null
												&& !map.get(ForesightConstants.L3).isEmpty()) {

											List<GeoGraphyInfoWrapper> g3List = BulkUploadUtils
													.getGeoGraphyL3ByL2Name(gL2.getName(), role.getGeoType());

											if (g3List != null) {
												GeoGraphyInfoWrapper gInfoL3 = g3List.stream()
														.filter(p -> p.getName()
																.equalsIgnoreCase(map.get(ForesightConstants.L3)))
														.findAny().orElse(null);

												GeographyL3 gL3 = new GeographyL3();
												gL3.setId(gInfoL3.getId());
												gL3.setName(gInfoL3.getName());
												gL3.setLatitude(gInfoL3.getLatitude());
												gL3.setLongitude(gInfoL3.getLongitude());
												gL3.setGeographyL2(gL2);
												boolean checkL3 = checkL3withinL2(gL3, gL2);
												if (checkL3) {
													geographyL3.add(gL3);

												}
											}
										}
									}
								}

							}
						}
					}
					userRoleGeographyDetails.setGeographyL1(geographyL1);
					userRoleGeographyDetails.setGeographyL2(geographyL2);
					userRoleGeographyDetails.setGeographyL3(geographyL3);
				}
				if (map.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.L2)) {
					logger.info("Inside Level L2");
					List<GeographyL1> geographyL1 = new ArrayList<>();
					List<GeographyL2> geographyL2 = new ArrayList<>();

					if (map.get("L1") != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("L1 value {} : ", map.get(ForesightConstants.L1));

						List<GeoGraphyInfoWrapper> geoGraphyInfoWrappers = BulkUploadUtils
								.findAllGeographyInfoL1(role.getGeoType());
						GeoGraphyInfoWrapper matchingObject = geoGraphyInfoWrappers.stream()
								.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L1))).findAny()
								.orElse(null);
						logger.info("matchingObject : {} ", matchingObject);
						GeographyL1 gL1 = new GeographyL1();
						if (matchingObject != null) {
							gL1.setName(matchingObject.getName());
							gL1.setId(matchingObject.getId());
							gL1.setLatitude(matchingObject.getLatitude());
							gL1.setLongitude(matchingObject.getLongitude());
						}
						if (gL1 != null) {
							logger.info("zone value : {} ", map.get(ForesightConstants.L1));
							geographyL1.add(gL1);
							if (map.get(ForesightConstants.L2) != null && !map.get(ForesightConstants.L2).isEmpty()) {
								logger.info("circle value : " + map.get(ForesightConstants.L2));
								List<GeoGraphyInfoWrapper> gL2List = BulkUploadUtils
										.getGeographyL2ByL1Name(gL1.getName(), role.getGeoType());

								GeoGraphyInfoWrapper gInfoL2 = gL2List.stream()
										.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L2)))
										.findAny().orElse(null);

								if (gInfoL2 != null) {
									GeographyL2 gL2 = new GeographyL2();
									gL2.setId(gInfoL2.getId());
									gL2.setName(gInfoL2.getName());
									gL2.setLatitude(gInfoL2.getLatitude());
									gL2.setLongitude(gInfoL2.getLongitude());
									gL2.setGeographyL1(gL1);
									boolean checkL2 = checkL2withinL1(gL2, gL1);
									if (checkL2) {
										geographyL2.add(gL2);
									}
								}

							}
						}
					}

					userRoleGeographyDetails.setGeographyL1(geographyL1);
					userRoleGeographyDetails.setGeographyL2(geographyL2);
				}
				if (map.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.L1)) {
					logger.info("check L1 Level");
					logger.info("zone value {} ", map.get(ForesightConstants.L1));
					List<GeographyL1> geographyL1 = new ArrayList<>();
					if (map.get("L1") != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("L1 value {} : ", map.get(ForesightConstants.L1));

						List<GeoGraphyInfoWrapper> geoGraphyInfoWrappers = BulkUploadUtils
								.findAllGeographyInfoL1(role.getGeoType());
						GeoGraphyInfoWrapper matchingObject = geoGraphyInfoWrappers.stream()
								.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.L1))).findAny()
								.orElse(null);
						logger.info("matchingObject : {} ", matchingObject);
						GeographyL1 gL1 = new GeographyL1();
						if (matchingObject != null) {
							gL1.setName(matchingObject.getName());
							gL1.setId(matchingObject.getId());
							gL1.setLatitude(matchingObject.getLatitude());
							gL1.setLongitude(matchingObject.getLongitude());
						}else {
							gL1.setName(map.get(ForesightConstants.L1));
						}
						if (gL1 != null) {
							geographyL1.add(gL1);
						}
					}
					userRoleGeographyDetails.setGeographyL1(geographyL1);
				}  if(map.get(ForesightConstants.LEVEL).equalsIgnoreCase("Unit")||map.get(ForesightConstants.LEVEL).equalsIgnoreCase("Other")) {
					List<OtherGeography> geographies=new ArrayList<>();   
					List<GeoGraphyInfoWrapper> geoGraphyInfoWrappers =BulkUploadUtils.findAllOtherGeography(map.get(ForesightConstants.LEVEL));
					GeoGraphyInfoWrapper matchingObject = geoGraphyInfoWrappers.stream()
							.filter(p -> p.getName().equalsIgnoreCase(map.get(ForesightConstants.OTHER))).findAny()
							.orElse(null);
					
					OtherGeography otherGeography=new OtherGeography();
					otherGeography.setId(matchingObject.getId());
					otherGeography.setName(matchingObject.getName());
					geographies.add(otherGeography);
					userRoleGeographyDetails.setOtherGeography(geographies);
					
				}
			}
			listOfUser.add(userRoleGeographyDetails);
		}
		return listOfUser;
	}

	public Map<String, AccessLevelWrapper> setBulkAccessLevel(String accessLevel, String appType) {

		// logger.info("Inside setBulkAccessLevel ");
		Map<String, AccessLevelWrapper> accessLevelWrapper = new HashMap<>();
		if (appType.equalsIgnoreCase("SP_FORESIGHT")) {
			System.out.println("inside sp foresight");
			AccessLevelWrapper accessMap = new AccessLevelWrapper();

			String[] arr = new String[2];
			arr = accessLevel.split(",");
			List<String> accessLevelList = new ArrayList<>();
			accessLevelList.add(arr[0]);
			System.out.println();
			if (arr.length > 1)
				accessLevelList.add(arr[1]);

			//list.stream().anyMatch("search_value"::equalsIgnoreCase);
			
			if (accessLevelList.stream().anyMatch("web internet"::equalsIgnoreCase)) {
			//if (accessLevelList.contains("web internet")) {
				accessMap.setWeb_internet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setWeb_internet(false);
				accessLevelWrapper.put(appType, accessMap);
			}
			if (accessLevelList.stream().anyMatch("web intranet"::equalsIgnoreCase)) {
			//if (accessLevelList.contains("web intranet")) {
				accessMap.setWeb_intranet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setWeb_intranet(false);
				accessLevelWrapper.put(appType, accessMap);
			}
			if (accessLevelList.stream().anyMatch("mobile internet"::equalsIgnoreCase)) {
			//if (accessLevelList.contains("mobile internet")) {
				accessMap.setMobile_internet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setMobile_internet(false);
				accessLevelWrapper.put(appType, accessMap);
			}
			if (accessLevelList.stream().anyMatch("mobile intranet"::equalsIgnoreCase)) {
			//if (accessLevelList.contains("mobile intranet")) {
				accessMap.setMobile_intranet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setMobile_intranet(false);
				accessLevelWrapper.put(appType, accessMap);
			}

			accessLevelWrapper.put(appType, accessMap);

		} else {

			String[] arr = new String[2];
			arr = accessLevel.split(",");
			List<String> level = new ArrayList<>();
			level.add(arr[0]);
			if (arr.length > 1)
				level.add(arr[1]);

			AccessLevelWrapper accessMap = new AccessLevelWrapper();
			  if (level.stream().anyMatch("web internet"::equalsIgnoreCase)) {
			//if (level.contains("web internet")) {
				accessMap.setWeb_internet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setWeb_internet(false);
				accessLevelWrapper.put(appType, accessMap);
			}
			  if (level.stream().anyMatch("web intranet"::equalsIgnoreCase)) {
			//if (level.contains("web intranet")) {
				accessMap.setWeb_intranet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setWeb_intranet(false);
				accessLevelWrapper.put(appType, accessMap);
			}
			if (level.stream().anyMatch("mobile internet"::equalsIgnoreCase)) {
			//if (level.contains("mobile internet")) {
				accessMap.setMobile_internet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setMobile_internet(false);
				accessLevelWrapper.put(appType, accessMap);
			}
			if (level.stream().anyMatch("mobile intranet"::equalsIgnoreCase)) {
			//if (level.contains("mobile intranet")) {
				accessMap.setMobile_intranet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setMobile_intranet(false);
				accessLevelWrapper.put(appType, accessMap);
			}
			accessLevelWrapper.put("SP_FORESIGHT", accessMap);

		}
		if (appType.equalsIgnoreCase("SP_SITEFORGE")) {
			AccessLevelWrapper accessMap = new AccessLevelWrapper();

			String[] arr = new String[2];
			arr = accessLevel.split(",");
			List<String> accesLevel = new ArrayList<>();
			accesLevel.add(arr[0]);
			System.out.println();
			if (arr.length > 1)
				accesLevel.add(arr[1]);

			if (accesLevel.stream().anyMatch("web internet"::equalsIgnoreCase)) {
			//if (accesLevel.contains("web internet")) {
				System.out.println("====" + accesLevel);
				accessMap.setWeb_internet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setWeb_internet(false);
				accessLevelWrapper.put(appType, accessMap);
			}
			if (accesLevel.stream().anyMatch("web intranet"::equalsIgnoreCase)) {
			//if (accesLevel.contains("web intranet")) {
				accessMap.setWeb_intranet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setWeb_intranet(false);
				accessLevelWrapper.put(appType, accessMap);
			}
			//if (accesLevel.contains("mobile internet")) {
			if (accesLevel.stream().anyMatch("mobile internet"::equalsIgnoreCase)) {
				accessMap.setMobile_internet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setMobile_internet(false);
				accessLevelWrapper.put(appType, accessMap);
			}
			if (accesLevel.stream().anyMatch("mobile intranet"::equalsIgnoreCase)) {
			//if (accesLevel.contains("mobile intranet")) {
				accessMap.setMobile_intranet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setMobile_intranet(false);
				accessLevelWrapper.put(appType, accessMap);
			}

			accessLevelWrapper.put(appType, accessMap);
		} else {
			AccessLevelWrapper accessMap = new AccessLevelWrapper();
			String[] arr = new String[2];
			arr = accessLevel.split(",");
			List<String> accessLevelData = new ArrayList<>();
			accessLevelData.add(arr[0]);
			if (arr.length > 1)
				accessLevelData.add(arr[1]);
			
			if (accessLevelData.stream().anyMatch("web internet"::equalsIgnoreCase)) {
			//if (a.contains("web internet")) {
				accessMap.setWeb_internet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setWeb_internet(true);
				accessLevelWrapper.put(appType, accessMap);
			}
			if (accessLevelData.stream().anyMatch("web intranet"::equalsIgnoreCase)) {
			//if (accessLevelData.contains("web intranet")) {
				accessMap.setWeb_intranet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setWeb_intranet(false);
				accessLevelWrapper.put(appType, accessMap);
			}
			
			//if (accessLevelData.contains("mobile internet")) {
			if (accessLevelData.stream().anyMatch("mobile internet"::equalsIgnoreCase)) {
				accessMap.setMobile_internet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setMobile_internet(true);
				accessLevelWrapper.put(appType, accessMap);
			}
			if (accessLevelData.stream().anyMatch("mobile intranet"::equalsIgnoreCase)) {
			//if (accessLevelData.contains("mobile intranet")) {
				accessMap.setMobile_intranet(true);
				accessLevelWrapper.put(appType, accessMap);
			} else {
				accessMap.setMobile_intranet(false);
				accessLevelWrapper.put(appType, accessMap);
			}
			accessLevelWrapper.put("SP_SITEFORGE", accessMap);

		}
		return accessLevelWrapper;

	}

	public Address setBulkAddress(String addressLine) {
		logger.info("Inside setBulkAddress");
		Address address = new Address();
		if (addressLine != null && !addressLine.isEmpty())
			address.setAddressLine1(addressLine);
		return address;
	}

	public ORGAuthenticationType setBulkORGAuthenticationType(String organizationName, String organizationType) {
		logger.info("Inside setBulkORGAuthenticationType org name : {} orgtype : {} ", organizationName,
				organizationType);
		ORGAuthenticationType orgAuthenticationType = new ORGAuthenticationType();
		if (organizationName != null && !organizationName.isEmpty()) {
			try {
				List<Organization> org = organizationDao.getOrganizationByName(organizationName);
				logger.info("org list : {} ", org);
				List<ORGAuthenticationType> orgAuthenticationTypeList = oRGAuthenticationTypeDao
						.getByOrganizationId(org.get(0).getId());
				logger.info("AuthenticationType List : {} ", orgAuthenticationTypeList);
				if (orgAuthenticationTypeList != null && !orgAuthenticationTypeList.isEmpty()) {

					for (ORGAuthenticationType orgAuthType : orgAuthenticationTypeList) {
						logger.info("org auth type : {} ", orgAuthType.getAuthenticationtype());
						if (orgAuthType.getAuthenticationtype().equalsIgnoreCase(organizationType)) {
							orgAuthenticationType = orgAuthType;
						} else {
							logger.info("invalid org type for current organization");
						}
					}
				}
			} catch (Exception e) {
				logger.error("Error while  setBulkORGAuthenticationType: {}", Utils.getStackTrace(e));
			}
		}
		logger.info("name : {} ", orgAuthenticationType.getAuthenticationtype());
		return orgAuthenticationType;
	}

	public Map<String, Role> setBulkActiveRole(String mapRole, String mapTeam, String mapWorkspace, String mapLevel,
			String appType) {
		logger.info("Inside setBulkActiveRole mapRole : {} mapTeam : {} ", mapRole, mapTeam);
		Map<String, Role> activeRole = null;
		if (mapRole != null && !mapRole.isEmpty() && mapTeam != null && !mapTeam.isEmpty() && mapWorkspace != null
				&& !mapWorkspace.isEmpty() && mapLevel != null && !mapLevel.isEmpty()) {
			activeRole = new HashMap<>();
			activeRole.put(appType, getRoleByMapRoleAndMapTeam(mapRole, mapTeam.trim(), mapWorkspace, mapLevel));
		}
		return activeRole;
	}

	public Map<String, Role> setBulkActiveRoleForSiteForge(String mapRole, String mapTeam, String mapWorkspace,
			String mapLevel, String appType) {
		logger.info("Inside setBulkActiveRoleForSiteForge mapRole : {} mapTeam : {} ", mapRole, mapTeam);
		Map<String, Role> activeRole = null;
		if (mapRole != null && !mapRole.isEmpty() && mapTeam != null && !mapTeam.isEmpty() && mapWorkspace != null
				&& !mapWorkspace.isEmpty() && mapLevel != null && !mapLevel.isEmpty()) {
			activeRole = new HashMap<>();
			activeRole.put(appType, getRoleByMapRoleAndMapTeamForSiteForge(mapRole, mapTeam, mapWorkspace, mapLevel));
		}
		return activeRole;
	}

	public Role getRoleByMapRoleAndMapTeam(String mapRole, String mapTeam, String mapWorkspace, String maplevelType) {
		logger.info("Inside getRoleByMapRoleAndMapTeam ");
		Role role = new Role();
		String level="";
		try {
			if (mapRole != null && !mapRole.isEmpty() && mapTeam != null && !mapTeam.isEmpty()) {
				Role r = roleDao.getRoleByWSNameandLevelTypeAndTeamNameAndRoleName(maplevelType.trim(),
						mapWorkspace.trim(), mapTeam.trim(), mapRole.trim());

				if (r != null && r.getRoleName() != null && !r.getRoleName().isEmpty()) {
					setNewRoleData(role, r);
				}else {
					if (maplevelType.trim().equalsIgnoreCase(UmConstants.SALES_L3))
						level = UmConstants.L3;
					     
					if (maplevelType.trim().equalsIgnoreCase(UmConstants.SALES_L4))
						level = UmConstants.L4;
					 r = roleDao.getRoleByWSNameandLevelTypeAndTeamNameAndRoleName(level,
							mapWorkspace.trim(), mapTeam.trim(), mapRole.trim());
					 
					setNewRoleData(role, r);
				}
				
				logger.info("Role object=={} ", role);
				logger.info("====Role json====>{} ", new Gson().toJson(role));
				if (role.getRoleName() != null && !role.getRoleName().isEmpty()) {
					logger.info("ROLE NAME ==> {} ", role.getRoleName());
				} else {
					logger.info("role name found null");
				}
			}
		} catch (Exception e) {
			logger.error("Error while  getRoleByMapRoleAndMapTeam : {}", Utils.getStackTrace(e));
		}
		return role;
	}


	private void setNewRoleData(Role role, Role r) {
		if(r!=null) {
		role.setRoleId(r.getRoleId());
		role.setRoleName(r.getRoleName());
		role.setTeam(r.getTeam());
		role.setLevelType(r.getLevelType());
		role.setCreationTime(r.getCreationTime());
		role.setWorkSpace(r.getWorkSpace());
		role.setGeoType(r.getGeoType());
		}
	}

	public Role getRoleByMapRoleAndMapTeamForSiteForge(String mapRole, String mapTeam, String mapWorkspace,
			String maplevelType) {
		logger.info("Inside getRoleByMapRoleAndMapTeamForSiteForge ");
		logger.info("mapRole : {} mapTeam : {} mapWorkspace : {} maplevelType : {} ", mapRole, mapTeam, mapWorkspace,
				maplevelType);
		Role roleObj = new Role();
		Team team = new Team();
		WorkSpace workSpace = new WorkSpace();
		try {
			if (mapRole != null && !mapRole.isEmpty() && mapTeam != null && !mapTeam.isEmpty()) {
				List<WorkspaceWrapper> workspaceWrappers = BulkUploadUtils.getAllSiteForgeWorkspace();
				WorkspaceWrapper matchingWorkspace = null;
				logger.info("file workspace name {} ", mapWorkspace);
				logger.info("workspaceWrappers : {} ", workspaceWrappers);
				// if (workspaceWrappers.contains(mapWorkspace)) {
				if (workspaceWrappers != null) {
					matchingWorkspace = workspaceWrappers.stream()
							.filter(p -> p.getName().equalsIgnoreCase(mapWorkspace)).findAny().orElse(null);
				}
				logger.info("matchingWorkspace { } ", matchingWorkspace);
				List<ForesightTeamWrapper> siteforgeTeam = BulkUploadUtils
						.getSiteForgeTeamByWSId(matchingWorkspace.getId());
				logger.info("siteforgeTeam list {} ", siteforgeTeam);
				if (siteforgeTeam != null && !siteforgeTeam.isEmpty()) {
					ForesightTeamWrapper matchingTeam = siteforgeTeam.stream()
							.filter(p -> p.getName().equalsIgnoreCase(mapTeam)).findAny().orElse(null);
					logger.info("matchingTeam : {} ", matchingTeam);
					if (matchingTeam != null) {
						List<String> levels = BulkUploadUtils.getAllLevelTypesByWSIdAndTeamId(matchingWorkspace.getId(),
								matchingTeam.getId());
						logger.info("level list : {} ", levels);
						if (levels != null) {
							/*
							 * String matchingLevel = levels.stream().filter(p ->
							 * p.equals(levels)).findAny() .orElse(null);
							 */
							boolean containsSearchStr = levels.stream().anyMatch(maplevelType::equalsIgnoreCase);
							logger.info(" is matchingLevel : {} ", containsSearchStr);
							if(!containsSearchStr) {
								maplevelType=setSiteForgeLevel(maplevelType);
							    containsSearchStr = levels.stream().anyMatch(maplevelType::equalsIgnoreCase);
							    logger.info("Inside sales level type");
							    logger.info("after sales level {} ",containsSearchStr);
							}
							
							if (containsSearchStr) {
								List<RoleDetailWrapper> detailWrappers = BulkUploadUtils
										.getRolesByWSIdandLevelTypeAndTeam(matchingWorkspace.getId(),
												matchingTeam.getId(), maplevelType);
								
								logger.info("detailWrappers list : {} ", detailWrappers);
								RoleDetailWrapper roleDetailWrapper = detailWrappers.stream()
										.filter(p -> p.getRoleName().equalsIgnoreCase(mapRole)).findAny().orElse(null);
								
								logger.info("roleDetailWrapper : {} ", roleDetailWrapper);
								
								
								if (roleDetailWrapper != null) {
									roleObj.setRoleName(roleDetailWrapper.getRoleName());
									roleObj.setRoleId(roleDetailWrapper.getRoleId());
									roleObj.setGeoType(roleDetailWrapper.getGeoType());
									roleObj.setLevelType(roleDetailWrapper.getLevelType());
									team.setName(roleDetailWrapper.getTeam().getName());
									team.setId(roleDetailWrapper.getTeam().getId());
									workSpace.setId(matchingWorkspace.getId());
									workSpace.setName(matchingWorkspace.getName());
									roleObj.setWorkSpace(workSpace);
									roleObj.setTeam(team);
								}else {
									
									logger.info("else detailWrappers list : {} ", detailWrappers);
									maplevelType=setSiteForgeLevel(maplevelType);
									detailWrappers = BulkUploadUtils
											.getRolesByWSIdandLevelTypeAndTeam(matchingWorkspace.getId(),
													matchingTeam.getId(), maplevelType);
									
									 roleDetailWrapper = detailWrappers.stream()
												.filter(p -> p.getRoleName().equalsIgnoreCase(mapRole)).findAny().orElse(null);
										
									if(detailWrappers!=null) {
									roleObj.setRoleName(roleDetailWrapper.getRoleName());
									roleObj.setRoleId(roleDetailWrapper.getRoleId());
									roleObj.setGeoType(roleDetailWrapper.getGeoType());
									roleObj.setLevelType(roleDetailWrapper.getLevelType());
									team.setName(roleDetailWrapper.getTeam().getName());
									team.setId(roleDetailWrapper.getTeam().getId());
									workSpace.setId(matchingWorkspace.getId());
									workSpace.setName(matchingWorkspace.getName());
									roleObj.setWorkSpace(workSpace);
									roleObj.setTeam(team);
								}
									
										
								}
							}
						}
					}
				}
				// }
				if (roleObj.getRoleName() != null && !roleObj.getRoleName().isEmpty()) {
					logger.info("ROLE NAME ==> {} ", roleObj.getRoleName());
				} else {
					logger.info("role name found null");
				}
			}
		} catch (Exception e) {
			logger.error("Error while  getRoleByMapRoleAndMapTeam : {}", Utils.getStackTrace(e));
		}
		return roleObj;
	}

	private String setSiteForgeLevel(String maplevelType) {
		logger.info("Inside setSiteForgeLevel");
		if (maplevelType.equalsIgnoreCase(ForesightConstants.SALES_REGION))
			return ForesightConstants.SALESL1;

		if (maplevelType.equalsIgnoreCase(ForesightConstants.SALES_CLUSTER))
			return ForesightConstants.SALESL2;

		if (maplevelType.equalsIgnoreCase(ForesightConstants.L3))
			return ForesightConstants.SALESL3;

		if (maplevelType.equalsIgnoreCase(ForesightConstants.L4))
			return ForesightConstants.SALESL4;

		return maplevelType;
	}


	public List<UserRoleGeographyDetails> getBulkRoleMapUserRoleGeography(Map<String, String> map, Role role) {
		logger.info("Inside getBulkRoleMapUserRoleGeography");
		UserRoleGeographyDetails userRoleGeographyDetails = new UserRoleGeographyDetails();
		List<UserRoleGeographyDetails> listOfUser = new ArrayList<>();
		if (role != null && role.getRoleName() != null && !role.getRoleName().isEmpty()) {
			userRoleGeographyDetails.setRole(role);
			map.put(ForesightConstants.LEVEL, role.getLevelType());
			
			logger.info("level {} : ", map.get(ForesightConstants.LEVEL));
			if (role.getGeoType().equalsIgnoreCase(ForesightConstants.SALES)) {
				if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALES_NHQ)) {
				}
				if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL1)) {
					SalesL1 salesL1 = null;
					logger.info("check SALES_L1 Level");
					logger.info("zone value {} ", map.get(ForesightConstants.L1));
					List<SalesL1> sL1 = new ArrayList<>();
					if (map.get("L1") != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("SALES_L1 value {} : ", map.get(ForesightConstants.L1));
						try {
							salesL1 = salesL1Dao.getSalesL1ByDisplayName(map.get(ForesightConstants.L1));
							logger.info("sales l1 name {} ", salesL1.getName());
						} catch (Exception e) {
							logger.error("Error while getL1ByName : {}", Utils.getStackTrace(e));
						}
						if (salesL1 != null) {
							sL1.add(salesL1);
						}
					}
					userRoleGeographyDetails.setSalesl1(sL1);
				}
				if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL2)) {
					logger.info("Inside Level SalesL2");
					SalesL1 salesL1 = null;

					List<SalesL1> salesGeographyL1 = new ArrayList<>();
					List<SalesL2> salesGeographyL2 = new ArrayList<>();
					if (map.get(ForesightConstants.L1) != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("zone value {} : ", map.get(ForesightConstants.L1));
						try {
							salesL1 = salesL1Dao.getSalesL1ByDisplayName(map.get(ForesightConstants.L1));
							logger.info("sales l1 name {} : ", salesL1.getName());
						} catch (Exception e) {
							logger.error("Error  while getL1ByName   : {}", Utils.getStackTrace(e));
						}
						if (salesL1 != null) {
							SalesL2 salesL2 = null;
							salesGeographyL1.add(salesL1);
							if (map.get(ForesightConstants.L2) != null && !map.get(ForesightConstants.L2).isEmpty()) {
								logger.info("circle value : {}  ", map.get(ForesightConstants.L2));
								try {
									salesL2 = salesL2Dao.getSalesL2ByDisplayName(map.get(ForesightConstants.L2),map.get(ForesightConstants.L1));
									logger.info("sales l2 name {} ", salesL2.getName());
								} catch (Exception e) {
									logger.error("Error while getL2ByName   : {}", Utils.getStackTrace(e));
								}
								if (salesL2 != null) {
									boolean checkL2 = checkSalesL2withinSalesL1(salesL2, salesL1);
									if (checkL2) {
										salesGeographyL2.add(salesL2);
									}
								}
							}
						}
					}
					userRoleGeographyDetails.setSalesl1(salesGeographyL1);
					userRoleGeographyDetails.setSalesl2(salesGeographyL2);
				}
				if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL3)) {
					logger.info("Inside Level SALES_L3");
					List<SalesL1> geographyL1 = new ArrayList<>();
					List<SalesL2> geographyL2 = new ArrayList<>();
					List<SalesL3> geographyL3 = new ArrayList<>();
					SalesL1 gL1 = null;
					if (map.get(ForesightConstants.L1) != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("zone value : {}  ", map.get(ForesightConstants.L1));
						try {
							gL1 = salesL1Dao.getSalesL1ByDisplayName(map.get(ForesightConstants.L1));
						} catch (Exception e) {
							logger.error("Error while  getL1ByName   : {}", Utils.getStackTrace(e));
						}
						if (gL1 != null) {
							geographyL1.add(gL1);
							SalesL2 gL2 = null;
							if (map.get(ForesightConstants.L2) != null && !map.get(ForesightConstants.L2).isEmpty()) {
								logger.info(" circle value : {}  ", map.get(ForesightConstants.L2));
								try {
									gL2 = salesL2Dao.getSalesL2ByDisplayName(map.get(ForesightConstants.L2),map.get(ForesightConstants.L1));
								} catch (Exception e) {
									logger.error("Error while  getL2ByName   : {}", Utils.getStackTrace(e));
								}
								if (gL2 != null) {
									boolean checkL2 = checkSalesL2withinSalesL1(gL2, gL1);
									if (checkL2) {
										geographyL2.add(gL2);
										if (map.get(ForesightConstants.L3) != null
												&& !map.get(ForesightConstants.L3).isEmpty()) {
											SalesL3 gL3 = null;
											try {
												gL3 = salesL3Dao.getSalesL3BydisplayName(map.get(ForesightConstants.L3),map.get(ForesightConstants.L2),map.get(ForesightConstants.L1));
											} catch (Exception e) {
												logger.error("Error while  getL3ByName   : {}", Utils.getStackTrace(e));
											}
											if (gL3 != null) {
												boolean checkL3 = checkSalesL3withinSalesL2(gL3, gL2);
												if (checkL3) {
													geographyL3.add(gL3);
												}
											}
										}
									}
								}
							}
						}
					}
					userRoleGeographyDetails.setSalesl1(geographyL1);
					userRoleGeographyDetails.setSalesl2(geographyL2);
					userRoleGeographyDetails.setSalesl3(geographyL3);
				}
				if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL4)) {

					logger.info("Inside Level sales_L4");
					List<SalesL1> geographyL1 = new ArrayList<>();
					List<SalesL2> geographyL2 = new ArrayList<>();
					List<SalesL3> geographyL3 = new ArrayList<>();
					List<SalesL4> geographyL4 = new ArrayList<>();

					if (map.get(ForesightConstants.L1) != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("zone value :{}", map.get(ForesightConstants.L1));
						SalesL1 gL1 = null;
						try {
							gL1 = salesL1Dao.getSalesL1ByDisplayName(map.get(ForesightConstants.L1));
						} catch (Exception e) {
							logger.error("Error while  getL1ByName   : {}", Utils.getStackTrace(e));
						}

						if (gL1 != null) {
							geographyL1.add(gL1);
							SalesL2 gL2 = null;
							if (map.get(ForesightConstants.L2) != null && !map.get(ForesightConstants.L2).isEmpty()) {
								logger.info("circle value : {} ", map.get(ForesightConstants.L2));
								try {
									gL2 = salesL2Dao.getSalesL2ByDisplayName(map.get(ForesightConstants.L2),map.get(ForesightConstants.L1));
								} catch (Exception e) {
									logger.error("Error while  getL2ByName   : {}", Utils.getStackTrace(e));
								}
								if (gL2 != null) {
									boolean checkL2 = checkSalesL2withinSalesL1(gL2, gL1);
									if (checkL2) {
										geographyL2.add(gL2);
										SalesL3 gL3 = null;
										if (map.get(ForesightConstants.L3) != null
												&& !map.get(ForesightConstants.L3).isEmpty()) {
											try {
												gL3 = salesL3Dao.getSalesL3BydisplayName(map.get(ForesightConstants.L3),map.get(ForesightConstants.L2),map.get(ForesightConstants.L1));
											} catch (Exception e) {
												logger.error("Error while  getL3ByName   : {}", Utils.getStackTrace(e));
											}
											if (gL3 != null) {
												boolean checkL3 = checkSalesL3withinSalesL2(gL3, gL2);
												if (checkL3) {
													geographyL3.add(gL3);
													SalesL4 gL4 = null;
													if (map.get(ForesightConstants.L4) != null
															&& !map.get(ForesightConstants.L4).isEmpty()) {
														try {
															gL4 = salesL4Dao
																	.getSalesL4ByDisplayName(map.get(ForesightConstants.L4),map.get(ForesightConstants.L3),map.get(ForesightConstants.L2),map.get(ForesightConstants.L1));
														} catch (Exception e) {
															logger.error("Error while  getL4ByName   : {}",
																	Utils.getStackTrace(e));
														}
														if (gL4 != null) {
															boolean checkL4 = checkSalesL4withinSalesL3(gL4, gL3);
															if (checkL4) {
																geographyL4.add(gL4);
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					userRoleGeographyDetails.setSalesl1(geographyL1);
					userRoleGeographyDetails.setSalesl2(geographyL2);
					userRoleGeographyDetails.setSalesl3(geographyL3);
					userRoleGeographyDetails.setSalesl4(geographyL4);

				}

			} else {

				if (map.get(ForesightConstants.LEVEL).equalsIgnoreCase("SUPER_ADMIN")) {

				}
				if (map.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.NHQ)) {

				}

				if (map.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.L4)) {
					logger.info("Level L4");
					List<GeographyL1> geographyL1 = new ArrayList<>();
					List<GeographyL2> geographyL2 = new ArrayList<>();
					List<GeographyL3> geographyL3 = new ArrayList<>();
					List<GeographyL4> geographyL4 = new ArrayList<>();

					if (map.get(ForesightConstants.L1) != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("zone value : {} ", map.get(ForesightConstants.L1));
						GeographyL1 gL1 = geographyL1Dao.getL1ByDisplayName(map.get(ForesightConstants.L1));
						if (gL1 != null) {
							geographyL1.add(gL1);
							if (map.get(ForesightConstants.L2) != null && !map.get(ForesightConstants.L2).isEmpty()) {
								logger.info("circle value : {} ", map.get(ForesightConstants.L2));
								//GeographyL2 gL2 = geographyL2Dao.getGeographyL2ByName(map.get(ForesightConstants.L2));
								GeographyL2 gL2 = geographyL2Dao.getL2byDisplayName(map.get(ForesightConstants.L2), map.get(ForesightConstants.L1));
								if (gL2 != null) {
									boolean checkL2 = checkL2withinL1(gL2, gL1);
									if (checkL2) {
										geographyL2.add(gL2);
										if (map.get(ForesightConstants.L3) != null
												&& !map.get(ForesightConstants.L3).isEmpty()) {
											//GeographyL3 gL3 = geographyL3Dao.getGeographyL3ByName(map.get(ForesightConstants.L3));
											GeographyL3 gL3 = geographyL3Dao.getL3BydisplayName(map.get(ForesightConstants.L3), map.get(ForesightConstants.L2), map.get(ForesightConstants.L1));

											if (gL3 != null) {
												boolean checkL3 = checkL3withinL2(gL3, gL2);
												if (checkL3) {
													geographyL3.add(gL3);
													if (map.get(ForesightConstants.L4) != null
															&& !map.get(ForesightConstants.L4).isEmpty()) {
														//GeographyL4 gL4 = geographyL4Dao.getGeographyL4ByName(map.get(ForesightConstants.L4));
														GeographyL4 gL4 = geographyL4Dao.getL4ByDisplayName(map.get(ForesightConstants.L4), map.get(ForesightConstants.L3), map.get(ForesightConstants.L2), map.get(ForesightConstants.L1));
														
														if (gL4 != null) {
															boolean checkL4 = checkL4withinL3(gL4, gL3);
															if (checkL4) {
																geographyL4.add(gL4);
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					userRoleGeographyDetails.setGeographyL1(geographyL1);
					userRoleGeographyDetails.setGeographyL2(geographyL2);
					userRoleGeographyDetails.setGeographyL3(geographyL3);
					userRoleGeographyDetails.setGeographyL4(geographyL4);
				}
				if (map.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.L3)) {
					logger.info("Inside Level L3");
					List<GeographyL1> geographyL1 = new ArrayList<>();
					List<GeographyL2> geographyL2 = new ArrayList<>();
					List<GeographyL3> geographyL3 = new ArrayList<>();

					if (map.get(ForesightConstants.L1) != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("zone value : " + map.get(ForesightConstants.L1));
						GeographyL1 gL1 = null;
						try {
							gL1 = geographyL1Dao.getL1ByDisplayName(map.get(ForesightConstants.L1));
							//gL1 = geographyL1Dao.getGeographyL1ByName(map.get(ForesightConstants.L1));
						} catch (Exception e) {
							logger.error("Error while  getL1ByDisplayName: {}", Utils.getStackTrace(e));
						}
						if (gL1 != null) {
							geographyL1.add(gL1);
							if (map.get(ForesightConstants.L2) != null && !map.get(ForesightConstants.L2).isEmpty()) {
								logger.info("circle value : {}  ", map.get(ForesightConstants.L2));
								GeographyL2 gL2 = null;
								try {
									gL2 = geographyL2Dao.getL2byDisplayName(map.get(ForesightConstants.L2), map.get(ForesightConstants.L1));
									//gL2 = geographyL2Dao.getGeographyL2ByName(map.get(ForesightConstants.L2));
								} catch (Exception e) {
									logger.error("Error while  getGeographyL2ByName: {}", Utils.getStackTrace(e));
								}
								if (gL2 != null) {
									boolean checkL2 = checkL2withinL1(gL2, gL1);
									if (checkL2) {
										geographyL2.add(gL2);
										if (map.get(ForesightConstants.L3) != null
												&& !map.get(ForesightConstants.L3).isEmpty()) {
											/*GeographyL3 gL3 = geographyL3Dao.getGeographyL3ByName(map.get(ForesightConstants.L3));*/
											GeographyL3 gL3 = geographyL3Dao.getL3BydisplayName(map.get(ForesightConstants.L3), map.get(ForesightConstants.L2), map.get(ForesightConstants.L1));
											
											if (gL3 != null) {
												boolean checkL3 = checkL3withinL2(gL3, gL2);
												if (checkL3) {
													geographyL3.add(gL3);
												}
											}
										}
									}
								}
							}
						}
					}
					userRoleGeographyDetails.setGeographyL1(geographyL1);
					userRoleGeographyDetails.setGeographyL2(geographyL2);
					userRoleGeographyDetails.setGeographyL3(geographyL3);
				}
				if (map.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.L2)) {
					logger.info("Inside Level L2");
					List<GeographyL1> geographyL1 = new ArrayList<>();
					List<GeographyL2> geographyL2 = new ArrayList<>();
					if (map.get(ForesightConstants.L1) != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("zone value : {} ", map.get(ForesightConstants.L1));
						GeographyL1 gL1 = null;
						try {
							gL1 = geographyL1Dao.getL1ByDisplayName(map.get(ForesightConstants.L1));
						} catch (Exception e) {
							logger.error("Error while  getGeographyL1ByName: {}", Utils.getStackTrace(e));
						}
						if (gL1 != null) {
							geographyL1.add(gL1);
							if (map.get(ForesightConstants.L2) != null && !map.get(ForesightConstants.L2).isEmpty()) {
								logger.info("circle value : " + map.get(ForesightConstants.L2));
								GeographyL2 gL2 = null;
								try {
									
									gL2 =geographyL2Dao.getL2byDisplayName(map.get(ForesightConstants.L2), map.get(ForesightConstants.L1));
									//gL2 = geographyL2Dao.getGeographyL2ByName(map.get(ForesightConstants.L2));
								} catch (Exception e) {
									logger.error("Error while  getGeographyL2ByName: {}", Utils.getStackTrace(e));
								}
								if (gL2 != null) {
									boolean checkL2 = checkL2withinL1(gL2, gL1);
									if (checkL2) {
										geographyL2.add(gL2);
									}
								}
							}
						}
					}
					userRoleGeographyDetails.setGeographyL1(geographyL1);
					userRoleGeographyDetails.setGeographyL2(geographyL2);
				}
				if (map.get(ForesightConstants.LEVEL).equalsIgnoreCase(ForesightConstants.L1)) {
					logger.info("check L1 Level");
					logger.info("zone value {} ", map.get(ForesightConstants.L1));
					List<GeographyL1> geographyL1 = new ArrayList<>();
					GeographyL1 gL1 = null;

					if (map.get("L1") != null && !map.get(ForesightConstants.L1).isEmpty()) {
						logger.info("L1 value {} : ", map.get(ForesightConstants.L1));
						try {
							gL1 = geographyL1Dao.getL1ByDisplayName(map.get(ForesightConstants.L1));
						} catch (Exception e) {

							logger.error("Error while  getGeographyL1ByName : {}", Utils.getStackTrace(e));
						}
						if (gL1 != null) {
							geographyL1.add(gL1);
						}
					}
					userRoleGeographyDetails.setGeographyL1(geographyL1);
				}

			}

			listOfUser.add(userRoleGeographyDetails);
		}
		return listOfUser;
	}

	public Boolean checkL4withinL3(GeographyL4 gL4, GeographyL3 gL3) {
		logger.info("Inside checkL4withinL3");
		if (gL4 != null && gL3 != null) {
			GeographyL3 g3 = gL4.getGeographyL3();
			if (g3.getName().equalsIgnoreCase(gL3.getName())) {
				return true;
			}
		}
		return false;
	}

	public Boolean checkSalesL4withinSalesL3(SalesL4 gL4, SalesL3 gL3) {
		logger.info("Inside checkSalesL4withinSalesL3");
		if (gL4 != null && gL3 != null) {
			SalesL3 g3 = gL4.getSalesL3();
			if (g3.getName().equalsIgnoreCase(gL3.getName())) {
				return true;
			}
		}
		return false;
	}

	public Boolean checkL3withinL2(GeographyL3 gL3, GeographyL2 gL2) {
		logger.info("Inside checkL3withinL2");
		if (gL3 != null && gL2 != null) {
			GeographyL2 g2 = gL3.getGeographyL2();
			if (g2.getName().equalsIgnoreCase(gL2.getName())) {
				return true;
			}
		}
		return false;
	}

	public Boolean checkSalesL3withinSalesL2(SalesL3 gL3, SalesL2 gL2) {
		logger.info("Inside checkSalesL3withinSalesL2");
		if (gL3 != null && gL2 != null) {
			SalesL2 g2 = gL3.getSalesL2();
			if (g2.getName().equalsIgnoreCase(gL2.getName())) {
				return true;
			}
		}
		return false;
	}

	public Boolean checkL2withinL1(GeographyL2 gL2, GeographyL1 gL1) {
		logger.info("Inside checkL2withinL1");
		if (gL2 != null && gL1 != null) {
			GeographyL1 g1 = gL2.getGeographyL1();
			if (g1.getName().equalsIgnoreCase(gL1.getName())) {
				return true;
			}
		}
		return false;
	}

	public Boolean checkSalesL2withinSalesL1(SalesL2 gL2, SalesL1 gL1) {
		logger.info("Inside checkSalesL2withinSalesL1");
		if (gL2 != null && gL1 != null) {
			SalesL1 g1 = gL2.getSalesL1();
			if (g1.getName().equalsIgnoreCase(gL1.getName())) {
				return true;
			}
		}
		return false;
	}

	// public boolean isValidateUser(UserIntegrationWrapper userIntegrationWrapper)
	// {
	public boolean isValidateUser(BulkUserInformationWrapper userIntegrationWrapper) {
		if (!validateUserPersonalInfo(userIntegrationWrapper)) {
			return false;
		} else if (validateBulkUserRoleGeography(userIntegrationWrapper)) {
			return false;
		}else if(hasRolePermission(userIntegrationWrapper)) {
			userIntegrationWrapper.setMessage("Failed to create user.User don't have privilege to create other team users.");
			return false;
		}
		
		return true;
	}


	private boolean hasRolePermission(BulkUserInformationWrapper userIntegrationWrapper) {
		// multirole testing
		List<String> ccRole = roleDao.getRoleNamesByPermissionName(ConfigUtils.getString("PERMISSION"));
		logger.info("ccRoles : {} ", ccRole);
		if (!userIntegrationWrapper.getRoleMap().isEmpty()) {
			Map<String, List<UserRoleGeographyDetails>> userRoleGeography = userIntegrationWrapper.getRoleMap();
			for (String SPs : userIntegrationWrapper.getRoleMap().keySet()) {
				List<UserRoleGeographyDetails> list = userRoleGeography.get(SPs);
				for (UserRoleGeographyDetails userRoleGeographyDetails : list) {
					Role r = userRoleGeographyDetails.getRole();
					if (r != null && !r.getRoleName().isEmpty()) {
						if (ccRole.stream().anyMatch(r.getRoleName()::equalsIgnoreCase))
							return true;
						break;
					}
				}
			}
		}
		return false;
	}


	public boolean validateBulkUserRoleGeography(UserIntegrationWrapper userIntegrationWrapper) {
		// public boolean validateBulkUserRoleGeography(BulkUserInformationWrapper
		// userIntegrationWrapper) {
		logger.info("Inside validateBulkUserRoleGeography");
		if (isNullOrEmpty(userIntegrationWrapper.getUserPersonalInfo().getActiveRole())) {
			userIntegrationWrapper.setMessage("Please Provide Valid Role/Team/Workspace and level type");
			return true;
		} else if (!isValidateGeography(userIntegrationWrapper)) {
			// userIntegrationWrapper.setMessage("Failed to create user.Please Provide valid
			// Geography");
			return true;
		}
		return false;
	}

	public boolean validateUserPersonalInfo(BulkUserInformationWrapper userIntegrationWrapper) {
		// public boolean validateUserPersonalInfo(UserIntegrationWrapper
		// userIntegrationWrapper) {
		Boolean status = true;
		UserPersonalDetailWrapper userPersonalInfo = userIntegrationWrapper.getUserPersonalInfo();
		if (userPersonalInfo != null) {

			if (!UmUtils.isValidFirstName(userPersonalInfo.getFirstName())&&userPersonalInfo.getFirstName().matches(ForesightConstants.SPACE_VALIDATION)) {
				userIntegrationWrapper.setMessage("Please Provide Valid FirstName");
				status = false;
			} else if (!BulkUploadUtils.isValidStringLength(userPersonalInfo.getFirstName())) {
				userIntegrationWrapper.setMessage("Please Provide Valid FirstName(Limit 50)");
				status = false;
			} else if (userPersonalInfo.getLastName() != null && !userPersonalInfo.getLastName().isEmpty()
					&& !UmUtils.isValidLastName(userPersonalInfo.getLastName())&&userPersonalInfo.getFirstName().matches(ForesightConstants.SPACE_VALIDATION)) {
				userIntegrationWrapper.setMessage("Please Provide Valid LastName");
				status = false;
			} else if (userPersonalInfo.getLastName() != null && !userPersonalInfo.getLastName().isEmpty()
					&& !BulkUploadUtils.isValidStringLength(userPersonalInfo.getLastName())&&userPersonalInfo.getFirstName().matches(ForesightConstants.SPACE_VALIDATION)) {
				userIntegrationWrapper.setMessage("Please Provide Valid LastName(Limit 50)");
				status = false;
			} else if (userPersonalInfo.getMiddleName() != null && !userPersonalInfo.getMiddleName().isEmpty()
					&& !UmUtils.isValidLastName(userPersonalInfo.getMiddleName())) {
				userIntegrationWrapper.setMessage("Please Provide Valid Middle Name");
				status = false;
			} else if (userPersonalInfo.getMiddleName() != null && !userPersonalInfo.getMiddleName().isEmpty()
					&& !BulkUploadUtils.isValidStringLength(userPersonalInfo.getMiddleName())) {
				userIntegrationWrapper.setMessage("Please Provide Valid Middle Name(Limit 50)");
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
			} else if (!isValidEmailDomain(userPersonalInfo)) {
				userIntegrationWrapper.setMessage("Please Provide Valid Email Domain Name.");
				status = false;
			} else if (!isValidAppType(userIntegrationWrapper)) {
				userIntegrationWrapper
						.setMessage("Please Provide Valid Application Type(FORESIGHT ROLE/SITEFORGE ROLE).");
				status = false;
			} else if (userPersonalInfo.getContactNumber()!=null&&!isValidContactNumber(userPersonalInfo.getContactNumber())) {
				userIntegrationWrapper.setMessage("Please Provide Valid Contact Number");
				status = false;
			}
		}
		return status;
	}

	private boolean isValidEmailDomain(UserPersonalDetailWrapper userPersonalInfo) {
		logger.info("Inside isValidEmailDomain");
		boolean status = false;
		if (userPersonalInfo.getOrgAuthenticationType() != null) {
			String[] splitDomain = userPersonalInfo.getOrgAuthenticationType().getDomain().split(CoreConstants.COMMA);
			logger.info("user email domain names: {} ", BulkUploadUtils.findDomainName(userPersonalInfo.getEmail()));
			String emailDomain = BulkUploadUtils.findDomainName(userPersonalInfo.getEmail());
			logger.info("emailDomain {} : ", emailDomain);
			for (String domain : splitDomain) {
				logger.info("domain:{} ", domain);
				if (domain.equalsIgnoreCase(emailDomain)) {
					logger.info("match domain : {} emailDomain : {} ", domain, emailDomain);
					status = true;
					break;
				}
			}
		}
		return status;
	}

	private boolean isValidAppType(BulkUserInformationWrapper userIntegrationWrapper) {
		logger.info("Inside isvalidAppType");
		if (userIntegrationWrapper.getAppType() != null && !userIntegrationWrapper.getAppType().isEmpty()
				&& (userIntegrationWrapper.getAppType().equalsIgnoreCase("SITEFORGE ROLE")
						|| userIntegrationWrapper.getAppType().equalsIgnoreCase("FORESIGHT ROLE")))
			return true;

		return false;
	}

	private boolean isEmpytOrNullActiveRole(UserPersonalDetailWrapper userPersonalInfo) {
		if (userPersonalInfo.getActiveRole() != null
				&& userPersonalInfo.getActiveRole().get(ForesightConstants.SP_FORESIGHT) != null) {
			return true;
		}
		return false;
	}

	public Boolean isValidateUserName(String userName) {
		if (userName != null && !userName.isEmpty())
			return true;

		return false;
	}

	public Boolean isValidContactNumber(String phoneNo) {
		if (phoneNo != null && !phoneNo.isEmpty())
			try {
				Pattern pattern = Pattern.compile("([0-9+-]*\\)*\\(*\\s*)+");
			    Matcher matcher = pattern.matcher(phoneNo);
				if (matcher.matches()) {
					return true;
				} else {
					return false;
				}
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
				return false;
			}
		return false;

	}

	public boolean isValidORGAuthenticationType(ORGAuthenticationType orgAuthenticationType) {
		logger.info("Inside isValidORGAuthenticationType");
		if (orgAuthenticationType != null && orgAuthenticationType.getAuthenticationtype() != null)
			return true;
		return false;

	}

	public  boolean isNullOrEmpty(final Map<?, ?> m) {
		logger.info("Inside isValidORGAuthenticationType");
		return m == null || m.isEmpty();
	}

	private boolean isValidateGeography(UserIntegrationWrapper userIntegrationWrapper) {
		// private boolean isValidateGeography(BulkUserInformationWrapper
		// userIntegrationWrapper) {
		logger.info("Inside isValidateGeography");
		Boolean status = false;
		Role role = null;
		boolean previousflag = true;
		Map<String, Role> activeRole = userIntegrationWrapper.getUserPersonalInfo().getActiveRole();

		try {
			if (activeRole != null && !activeRole.isEmpty()) {
				Map<String, List<UserRoleGeographyDetails>> userRoleGeography = userIntegrationWrapper.getRoleMap();

				for (String SPs : userIntegrationWrapper.getRoleMap().keySet()) {
					logger.info("sps name : {} ", SPs);
					List<UserRoleGeographyDetails> userRoleList = userRoleGeography.get(SPs);
					logger.info("list size : {} ", userRoleList.size());
					logger.info("list : {} ", userRoleList);
				//	List<String> duplicateWorkspace = new ArrayList<>();

					for (UserRoleGeographyDetails userRoleGeographyDetails2 : userRoleList) {

						role = userRoleGeographyDetails2.getRole();
						logger.info("role level type  :{} ", role.getLevelType());
						logger.info("ROLE OBJECT : {} ", role);

						if (userRoleGeographyDetails2.getRole() != null && role.getTeam().getName() != null
								&& !role.getTeam().getName().isEmpty() && role.getRoleId() != null) {
							if (role.getGeoType().equalsIgnoreCase(ForesightConstants.SALES)) {
								if (role.getLevelType() != null && !role.getLevelType().isEmpty()) {
									if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALES_NHQ)) {
										status = true;
									} else if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL4)) {
										if (userRoleGeographyDetails2.getSalesl4() != null
												&& !userRoleGeographyDetails2.getSalesl4().isEmpty()
												&& userRoleGeographyDetails2.getSalesl4().get(0).getId() != null) {
											if (userRoleGeographyDetails2.getSalesl4().get(0).getId() != null) {
												status = true;
											} else {
												userIntegrationWrapper.setMessage("Failed to create user.Please Provide valid  Sales L4");
												break;
											}
										} else {
											userIntegrationWrapper.setMessage(
													"Failed to create user.Please Provide valid Sales L3");
											break;
										}
									} else if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL3)) {
										if (userRoleGeographyDetails2.getSalesl3() != null
												&& !userRoleGeographyDetails2.getSalesl3().isEmpty()
												&& userRoleGeographyDetails2.getSalesl3().get(0).getId() != null) {
											if (userRoleGeographyDetails2.getSalesl3().get(0).getId() != null) {
												status = true;
											} else {
												userIntegrationWrapper.setMessage("Failed to create user.Please Provide valid  Sales L3");
												break;
											}
										} else {
											userIntegrationWrapper.setMessage("Failed to create user.Please Provide valid Sales L3");
											break;
										}
									} else if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL2)) {
										if (userRoleGeographyDetails2.getSalesl2() != null
												&& !userRoleGeographyDetails2.getSalesl2().isEmpty()) {
											if (userRoleGeographyDetails2.getSalesl2().get(0).getId() != null) {
												status = true;
											} else {
												userIntegrationWrapper.setMessage("Failed to create user.Please Provide valid  Sales L2");
												break;
											}
										} else {
											userIntegrationWrapper.setMessage("Failed to create user.Please Provide valid Sales L2");
											break;
										}
									} else if (role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL1)) {
										if (userRoleGeographyDetails2.getSalesl1() != null&& !userRoleGeographyDetails2.getSalesl1().isEmpty()
												&& userRoleGeographyDetails2.getSalesl1().get(0).getId() != null) {
											
											if (userRoleGeographyDetails2.getSalesl1().get(0).getId() != null) {
												status = true;
											} else {
												userIntegrationWrapper.setMessage("Failed to create user.Please Provide valid  Sales L4");
												break;
											}
											
										} else {
											userIntegrationWrapper.setMessage(
													"Failed to create user.Please Provide valid  Sales L1");
											break;
										}
									}
								}
							} else {
								if (role.getLevelType() != null && !role.getLevelType().isEmpty()) {
									if (role.getLevelType().equalsIgnoreCase("SUPER_ADMIN")) {
										status = true;

									} else if (role.getLevelType().equalsIgnoreCase(ForesightConstants.NHQ)) {
										status = true;
									} else if (role.getLevelType().equalsIgnoreCase(ForesightConstants.L4)) {
										if (userRoleGeographyDetails2.getGeographyL4() != null
												&& !userRoleGeographyDetails2.getGeographyL4().isEmpty()) {
											if (userRoleGeographyDetails2.getGeographyL4().get(0).getId() != null) {
												status = true;
											} else {
												userIntegrationWrapper.setMessage("Failed to create user.Please Provide valid  Geography L4");
												break;
											}
										} else {
											userIntegrationWrapper.setMessage(
													"Failed to create user.Please Provide valid  Geography L4");
											break;
										}
									} else if (role.getLevelType().equalsIgnoreCase(ForesightConstants.L3)) {
										if (userRoleGeographyDetails2.getGeographyL3() != null
												&& !userRoleGeographyDetails2.getGeographyL3().isEmpty()) {
											if (userRoleGeographyDetails2.getGeographyL3().get(0).getId() != null) {
												status = true;
											} else {
												userIntegrationWrapper.setMessage(
														"Failed to create user.Please Provide valid Geography L3");
												break;
											}
										} else {
											userIntegrationWrapper.setMessage(
													"Failed to create user.Please Provide valid Geography L3");
											break;
										}
									} else if (role.getLevelType().equalsIgnoreCase(ForesightConstants.L2)) {
										logger.info("=========={}", userRoleGeographyDetails2.getGeographyL2());
										if (userRoleGeographyDetails2.getGeographyL2() != null
												&& !userRoleGeographyDetails2.getGeographyL2().isEmpty()) {
											if (userRoleGeographyDetails2.getGeographyL2().get(0).getId() != null) {
												status = true;
											} else {
												userIntegrationWrapper.setMessage(
														"Failed to create user.Please Provide valid Geography L2");
												break;
											}
										} else {
											userIntegrationWrapper.setMessage(
													"Failed to create user.Please Provide valid Geography L2");
											break;
										}
									} else if (role.getLevelType().equalsIgnoreCase(ForesightConstants.L1)) {
										logger.info("VALIDATE L1 GEOGRAPHY : {} ", role.getLevelType());
										List<GeographyL1> gL1 = userRoleGeographyDetails2.getGeographyL1();
										if (gL1 != null && !gL1.isEmpty()) {
											GeographyL1 l1 = gL1.get(0);
											logger.info("GL1=={}", l1.getId());
											if (l1.getId() != null) {
												status = true;
											} else {
												userIntegrationWrapper.setMessage(
														"Failed to create user.Please Provide valid Geography L1");
												break;
											}
										} else {
											userIntegrationWrapper.setMessage(
													"Failed to create user.Please Provide valid Geography L1");
											break;
										}
									}else if(role.getLevelType().equalsIgnoreCase("Unit")||role.getLevelType().equalsIgnoreCase("Other")) {
										   List<OtherGeography> otherGeographies=userRoleGeographyDetails2.getOtherGeography();
										   if(otherGeographies!=null&&!otherGeographies.isEmpty()) {
											 OtherGeography otherGeography=  otherGeographies.get(0);
											 if(otherGeography.getId()!=null) {
												 status = true;
											 }else {
													userIntegrationWrapper.setMessage(
															"Failed to create user.Please Provide valid Other Geography");
													break;
											 }
												 
										   }
										 status=true;
									} else {
										userIntegrationWrapper
												.setMessage("Failed to create user.Please Provide valid Geography");
										break;
									}
								}
							}
						} else {
							status=false;
							logger.info("role detail not found");
							userIntegrationWrapper.setMessage("Please Provide Valid Role/Team/Workspace and level type");
						}

					/*	if (ForesightConstants.SP_SITEFORGE.equalsIgnoreCase(SPs)) {
							boolean containsSearchStr = duplicateWorkspace.stream().anyMatch(
									userRoleGeographyDetails2.getRole().getWorkSpace().getName()::equalsIgnoreCase);
							logger.info("duplicate workspace {} ", containsSearchStr);
							if (!containsSearchStr) {
								duplicateWorkspace.add(userRoleGeographyDetails2.getRole().getWorkSpace().getName());
							} else {
								status = false;
								userIntegrationWrapper.setMessage(
										"Failed to create user.Please select distinct workspace siteforge role.");
							}
						}*/
                        logger.info("status===>{} ",status);
						if (!status)
							previousflag = status;
					}
					logger.info("======> : {} ", status);
				}
			} else {
				userIntegrationWrapper.setMessage("Please Provide Valid Role/Team/Workspace and level type");
			}
		} catch (Exception e) {
			logger.error("Error while  isValidateGeography   : {}", Utils.getStackTrace(e));
		}
		
		if (!previousflag)
			status = previousflag;
		logger.info("return isValidateGeography : {} ", status);
		return status;
	}

	@Override
	public Map<String, String> enableDisableBulkUser(InputStream inputStream, String actionType,String filePath) {

		logger.info("Inside enableDisableBulkUser in class : {}", this.getClass().getName());
		Map<String, String> messageMap = new HashMap<>();
		int maxNoRecords = 0;
		int successRecords = 0;

		try {
			List<Map<String, String>> map = ExcelReadWriter.getExcelList(inputStream,
					ConfigUtils.getString("BULKUPLOAD_ENABLE_DISABLE"));
			logger.info("map size : {} ", map.size());
			logger.info("map===> {} ", map);
			List<BulkUserInformationWrapper> userIntegrationWrapper = setBulkUserDataStatus(map);
			logger.info("userIntegrationWrapper size : {} ", userIntegrationWrapper.size());
			for (BulkUserInformationWrapper userIntegrationWrapper3 : userIntegrationWrapper) {
				
				if(isExistUser(userIntegrationWrapper3)) {
				
				if (validateEnableDisable(userIntegrationWrapper3)) {
					if (validateStatus(userIntegrationWrapper3)) {
						UserPersonalDetailWrapper userPersonalDetailWrapper = userIntegrationWrapper3
								.getUserPersonalInfo();
						logger.info("username :  {} ", userPersonalDetailWrapper.getUserName());
						
						BasicAuthenticationDetail basicAuthenticationDetail = findUser(userIntegrationWrapper3);
                        if(basicAuthenticationDetail!=null) {
						logger.info("authentication username {} : ", basicAuthenticationDetail.getUsername());
						if (actionType.equalsIgnoreCase("ACTIVATE")) {
							if (!"true".equalsIgnoreCase(ConfigUtils.getString("BULKUPLOAD_ACCESS_LEVEL"))) {
								/*if (!basicAuthenticationDetail.getFirsttimelogin()
										&& (!basicAuthenticationDetail.getActivationkey().isEmpty()
												|| basicAuthenticationDetail.getActivationkey() != null)) {*/
									Map<String, String> mapMessage = userService.enableUser(userPersonalDetailWrapper);
									successRecords++;
									userIntegrationWrapper3.setMessage(mapMessage.get(CoreConstants.MESSAGE));
									userAuditFromBulkupload(userIntegrationWrapper3, AuditActionType.ENABLE, AuditActionName.USERS_ENABLE, "USER_ENABLED_BY_BULKUPLOAD");

								/*} else {
									userIntegrationWrapper3.setMessage("User is not Activated.");
								}*/
							} else {
								Map<String, String> mapMessage = userService.enableUser(userPersonalDetailWrapper);
								successRecords++;
								userIntegrationWrapper3.setMessage(mapMessage.get(CoreConstants.MESSAGE));
								userAuditFromBulkupload(userIntegrationWrapper3, AuditActionType.ENABLE, AuditActionName.USERS_ENABLE, "USER_ENABLED_BY_BULKUPLOAD");

							}
						} else if (actionType.equalsIgnoreCase("DISABLE")) {
							Map<String, String> mapMessage = userService.disableUser(userPersonalDetailWrapper);
							userIntegrationWrapper3.setMessage(mapMessage.get(CoreConstants.MESSAGE));
							successRecords++;
							userIntegrationWrapper3.setMessage("User Updated Successfully");
							userAuditFromBulkupload(userIntegrationWrapper3, AuditActionType.DISABLE, AuditActionName.USERS_DISABLE, "USER_DISABLED_BY_BULKUPLOAD");

						}
						
					}else {
						userIntegrationWrapper3.setMessage("User not exist");
					}
						
					} else {
						logger.info(
								"Failed to update user status.User don't have sufficient permission to update user status.");
					}
				} else {
					userIntegrationWrapper3.setMessage("please provide valid username");
				}
				}else {
					userIntegrationWrapper3.setMessage("User not exist");
				}
				maxNoRecords++;
			}
			messageMap.put(ForesightConstants.FILENAME, writeEnableDisableData(userIntegrationWrapper,filePath));
			messageMap.put(UmConstants.BULK_UPLOAD_SUCCESS, String.valueOf(successRecords));
			messageMap.put(UmConstants.BULK_UPLOAD_FAIL, String.valueOf((maxNoRecords - (successRecords))));
		} catch (Exception e) {
			logger.error("Error while  enableDisableBulkUser for zsmart: {}", Utils.getStackTrace(e));
		}
		return messageMap;
	}

	private boolean isExistUser(BulkUserInformationWrapper userIntegrationWrapper3) {
		User user = null;
		try {
			user = userDao.findByUserName(userIntegrationWrapper3.getUserPersonalInfo().getUserName());
			if (user != null)
				return true;
		} catch (Exception e) {
			logger.error("Error while  enableDisableBulkUser for zsmart: {}", Utils.getStackTrace(e));
		}
		return false;
	}

	private BasicAuthenticationDetail findUser(BulkUserInformationWrapper userIntegrationWrapper3) {
		logger.info("Inside isUserExists method");
		BasicAuthenticationDetail basicAuthenticationDetail=null;
		 try {
		 basicAuthenticationDetail = basicAuthenticationDetailDao
				.findByUsername(userIntegrationWrapper3.getUserPersonalInfo().getUserName());
		 } catch (Exception e) {
				logger.error("Error while  isUserExists for zsmart: {}", Utils.getStackTrace(e));
			}
		return basicAuthenticationDetail;
	}

	public List<BulkUserInformationWrapper> setBulkUserDataStatus(List<Map<String, String>> mapList) {
		logger.info("Inside setBulkUserData");
		List<BulkUserInformationWrapper> userIntegrationWrapperList = new ArrayList<>();
		try {
			mapList.remove(0);
			for (Map<String, String> mapData : mapList) {
				UserPersonalDetailWrapper userPersonalDetail = new UserPersonalDetailWrapper();
				BulkUserInformationWrapper userIntegrationWrapper = new BulkUserInformationWrapper();
               
				userPersonalDetail.setCreatorUsername(ConfigUtils.getString("DEFAULT_USER"));
				if (mapData.get(ForesightConstants.USER_NAME) != null
						&& !mapData.get(ForesightConstants.USER_NAME).isEmpty()) {
					userPersonalDetail.setUserName(mapData.get(ForesightConstants.USER_NAME).trim().toLowerCase());
					
				}
				if (mapData.get(ForesightConstants.FIRST_NAME) != null
						&& !mapData.get(ForesightConstants.FIRST_NAME).isEmpty()) {
					userPersonalDetail.setFirstName(mapData.get(ForesightConstants.FIRST_NAME).toLowerCase());
				}
				if (mapData.get(ForesightConstants.MIDDLE_NAME) != null
						&& !mapData.get(ForesightConstants.MIDDLE_NAME).isEmpty()) {
					userPersonalDetail.setMiddleName(mapData.get(ForesightConstants.MIDDLE_NAME).trim().toLowerCase());
				}

				if (mapData.get(ForesightConstants.LAST_NAME) != null
						&& !mapData.get(ForesightConstants.LAST_NAME).isEmpty()) {
					userPersonalDetail.setLastName(mapData.get(ForesightConstants.LAST_NAME).trim().toLowerCase());
				}

				userIntegrationWrapper.setUserPersonalInfo(userPersonalDetail);
				userIntegrationWrapperList.add(userIntegrationWrapper);
			}
		} catch (Exception e) {
			logger.error("Error while  creating  user request : {}", Utils.getStackTrace(e));
		}
		return userIntegrationWrapperList;
	}

	public boolean validateEnableDisable(BulkUserInformationWrapper userIntegrationWrapper) {
		Boolean status = true;
		UserPersonalDetailWrapper userPersonalInfo = userIntegrationWrapper.getUserPersonalInfo();
		if (userPersonalInfo != null) {
			if (!isValidateUserName(userPersonalInfo.getUserName())) {
				userIntegrationWrapper.setMessage("Please Provide Valid User Name");
				status = false;
			}
		}
		return status;
	}

	public Boolean validateStatus(BulkUserInformationWrapper userIntegrationWrapper3) {
		logger.info("Inside validateCustomerCareForStatus");
		Boolean status = false;
		Set<String> createUserPermissionList = null;
		User userInContext = userContextService.getUserInContextnew();
		if (userInContext == null) {
			if (userIntegrationWrapper3.getUserPersonalInfo().getCreatorUsername() != null)
				userInContext = userDao.findByUserName(userIntegrationWrapper3.getUserPersonalInfo().getCreatorUsername());
		}
		
		User user = userDao.findByUserName(userIntegrationWrapper3.getUserPersonalInfo().getUserName());
		
		if (userInContext != null) {
			logger.info("Inside UMUtils contextUserInfo activerrole : {} ",
					userInContext.getActiveRole().getRoleId());
			Role activeRole = userInContext.getActiveRole();
			createUserPermissionList =customerinfo.getPermissions();
			if (customerinfo.getPermissions() != null
					&& !customerinfo.getPermissions().isEmpty()) {
				Set<String> permissionList = customerinfo.getPermissions();
				if (permissionList.contains(UmPermissionConstants.ROLE_ADMIN_UM_SUPER_ADMIN_ROLE)) {
					
					if (permissionList.contains("ROLE_ADMIN_UM_USERROLE_CUSTOMERCARE_adminView")) {
						status = true;
					} else if (!user.getActiveRole().getTeam().getName().equalsIgnoreCase("CUSTOMER CARE")) {
						status = true;
					} else {
						userIntegrationWrapper3.setMessage(
								"Failed to update user status. User don't have privilege to update user status.");
					}
				} else {
					if (!user.getActiveRole().getRoleName().equalsIgnoreCase(activeRole.getRoleName())) {
						status = true;
					} else {
						userIntegrationWrapper3.setMessage(
								"Failed to update user status. User don't have privilege to update user status.");
					}
				}
			}
		}
		return status;
	}

	public String writeEnableDisableData(List<BulkUserInformationWrapper> usersList,String filePath) {
		logger.info("Inside writeEnableDisableData");
		FileOutputStream out=null;
		String headerData = ConfigUtils.getString(ForesightConstants.BULKUPLOAD_ENABLE_DISABLE);
		 String date = new SimpleDateFormat(UmConstants.YYYY_MM_DD_HH_MM).format(new Date());
		 String path="/CREATED_FILE_"+date+".xlsx";
		 String targetPath=System.getProperty("catalina.base")+"/temp/"+path;
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(ForesightConstants.CREATED_USER);
		int rowNum = 1;
		int headerCount = 0;
		List<String> headerList = Arrays.asList(headerData.split(ForesightConstants.COMMA));
		Row header = sheet.createRow(0);
		for (String head : headerList) {
			header.createCell(headerCount++).setCellValue(head);
		}
		header.createCell(headerCount).setCellValue(ForesightConstants.USER_STATUS);
		for (BulkUserInformationWrapper userIntegrationWrapper : usersList) {
			UserPersonalDetailWrapper userPersonalData = userIntegrationWrapper.getUserPersonalInfo();
			if (userPersonalData != null) {
				Row row = sheet.createRow(rowNum);
				row.createCell(0).setCellValue(userPersonalData.getUserName());
				row.createCell(1).setCellValue(userPersonalData.getFirstName());
				row.createCell(2).setCellValue(userPersonalData.getMiddleName());
				row.createCell(3).setCellValue(userPersonalData.getLastName());
				row.createCell(4).setCellValue(userIntegrationWrapper.getMessage());
				rowNum++;
			}
		}
		try {
			if (filePath != null) {
			    out=ExcelReadWriter.writeFileIntoFolder(filePath, path);
			} else {
				out = new FileOutputStream(new File(targetPath));
			}
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			logger.error("Error while  creating  user request : {}", Utils.getStackTrace(e));
		}
		return path;
	}
	
	private boolean validateFile(List<BulkUserInformationWrapper> userIntegrationWrapper) {
		logger.info("Inside validateFile");
		logger.info("HEAD : {}",ConfigUtils.getString(UmConstants.BULKUPLOAD_HEADER));
		logger.info("file size {} ",userIntegrationWrapper.size());
		
		
		 if (!userIntegrationWrapper.get(0).getHeader()
				.contains(ConfigUtils.getString(UmConstants.BULKUPLOAD_HEADER))||userIntegrationWrapper.isEmpty()) {
			userIntegrationWrapper.get(0).setMessage("Failed to create/update user.Invalid Header names.");
			return true;
		}else if ((userIntegrationWrapper.size()-1) > (Integer.parseInt(ConfigUtils.getString("USERS_LIMIT")))) {
			userIntegrationWrapper.get(0).setMessage("Failed to create/update user.More Than "+(Integer.parseInt(ConfigUtils.getString("USERS_LIMIT")))+"  entries are not allowed.");
			return true;
		} 
		return false;
	   	
	}
	
	private Map<String, List<UserRoleGeographyDetails>>  addPreviousUserRole(User fUser,BulkUserInformationWrapper finalUserIntegrationWrapper) {
		logger.info("Inside addPreviousUserRole");
		Map<String, List<UserRoleGeographyDetails>> roleMap=new HashMap<>();
		List<UserRoleGeographyDetails> userRole = addOnRole(fUser);
		
		roleMap.put("SP_FORESIGHT", userRole);
		if(finalUserIntegrationWrapper.getRoleMap().containsKey(ForesightConstants.SP_SITEFORGE)) {
			roleMap.put(ForesightConstants.SP_SITEFORGE, finalUserIntegrationWrapper.getRoleMap().get(ForesightConstants.SP_SITEFORGE));
		}
		logger.info("returning roleMap {} : ",roleMap);
		return roleMap;
	}


	private List<UserRoleGeographyDetails> addOnRole(User fUser) {
		List<UserRoleGeographyDetails> userRole=new ArrayList<>();
		Set<UserRole> ur=fUser.getUserRole();
		List<GeographyL1> gL1=null;
		List<GeographyL2> gL2=null;
		List<GeographyL3> gL3=null;
		List<GeographyL4> gL4=null;
		
		List<SalesL1> sL1=null;
		List<SalesL2> sL2=null;
		List<SalesL3> sL3=null;
		List<SalesL4> sL4=null;
		
		for (UserRole userRoles : ur) {
			UserRoleGeographyDetails geographyDetails=new UserRoleGeographyDetails();
			Set<UserRoleGeography> userRoleSet= userRoles.getUserRoleGeography();
			geographyDetails.setUserRoleId(userRoles.getId());
			Role r=userRoles.getRole();
			Role role = new Role();
			role.setRoleId(r.getRoleId());
			role.setRoleName(r.getRoleName());
			role.setTeam(r.getTeam());
			role.setLevelType(r.getLevelType());
			role.setCreationTime(r.getCreationTime());
			role.setWorkSpace(r.getWorkSpace());
			role.setGeoType(r.getGeoType());
			
			
			geographyDetails.setRole(role);
			
			/*if (!role.getLevelType().equalsIgnoreCase(ForesightConstants.NHQ)
					&& role.getLevelType().equalsIgnoreCase(ForesightConstants.SALES_NHQ)
					&& !role.getLevelType().equalsIgnoreCase(ForesightConstants.SUPER_ADMIN)) {*/
				if (userRoleSet != null && !userRoleSet.isEmpty()) {
					
					for (UserRoleGeography userRoleGeography : userRoleSet) {
                        
						if(role.getLevelType().equalsIgnoreCase(ForesightConstants.L1)) {
						gL1 = new ArrayList<>();
						gL1.add(userRoleGeography.getGeographyL1());
						}
						
						if(role.getLevelType().equalsIgnoreCase(ForesightConstants.L2)) {
						gL2 = new ArrayList<>();
						gL2.add(userRoleGeography.getGeographyL2());
						}
						
						if(role.getLevelType().equalsIgnoreCase(ForesightConstants.L3)) {
						gL3 = new ArrayList<>();
						gL3.add(userRoleGeography.getGeographyL3());
						}
						
						if(role.getLevelType().equalsIgnoreCase(ForesightConstants.L4)) {
						gL4 = new ArrayList<>();
						gL4.add(userRoleGeography.getGeographyL4());
						}
						
						if(role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL1)) {
						sL1 = new ArrayList<>();
						sL1.add(userRoleGeography.getSalesL1());
						}
						if(role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL2)) {
						sL2 = new ArrayList<>();	
						sL2.add(userRoleGeography.getSalesL2());
						}
						
						if(role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL3)) {
						sL3 = new ArrayList<>();	
						sL3.add(userRoleGeography.getSalesL3());
						}
						
						if(role.getLevelType().equalsIgnoreCase(ForesightConstants.SALESL4)) {
						sL4 = new ArrayList<>();
						sL4.add(userRoleGeography.getSalesL4());
						}
					}
				}
			//}
			
			geographyDetails.setGeographyL1(gL1);
			geographyDetails.setGeographyL2(gL2);
			geographyDetails.setGeographyL3(gL3);
			geographyDetails.setGeographyL4(gL4);
			geographyDetails.setSalesl1(sL1);
			geographyDetails.setSalesl2(sL2);
			geographyDetails.setSalesl3(sL3);
			geographyDetails.setSalesl4(sL4);
			userRole.add(geographyDetails);
		}
		/*if(userRoleGeographies!=null&&!userRoleGeographies.isEmpty())
			for (UserRoleGeographyDetails userRoleGeography : userRoleGeographies) {
				Boolean contains=userRole.stream().filter(o -> o.getRole().getRoleName().equalsIgnoreCase(userRoleGeography.getRole().getRoleName())).findFirst().isPresent();
				if(!contains)
					userRole.add(userRoleGeography);
			}*/
		return userRole;
	}
	
	
	/*private void removeDuplicateRole( UserIntegrationWrapper userIntegrationWrapper) {
		List<UserRoleGeographyDetails> listofRole=	userIntegrationWrapper.getRoleMap().get(IDPSyncUtils.getInitiator());
			
		Set<Integer> distinctRoleObj = new HashSet<>();
			List<UserRoleGeographyDetails> nonDuplicatesRole = listofRole.stream()
			            .filter(e -> distinctRoleObj.add(e.getRole().getRoleId()))
			            .collect(Collectors.toList());
			
			userIntegrationWrapper.getRoleMap().put(IDPSyncUtils.getInitiator(), nonDuplicatesRole);
			
		}
	*/
	
}
