package com.inn.foresight.core.mylayer.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.mylayer.dao.IPushPinDao;
import com.inn.foresight.core.mylayer.model.PushPin;
import com.inn.foresight.core.mylayer.wrapper.DataWrapper;

/**
 *     This class is used for MyLayer for SitePlanning Activity functionality.
 *
 * @author innoeye
 */
@Repository("PushPinDaoImpl")
public class PushPinDaoImpl extends HibernateGenericDao<Integer, PushPin> implements IPushPinDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(PushPinDaoImpl.class);

	/**
	 * Instantiates a new push pin dao impl.
	 */
	public PushPinDaoImpl() {
		super(PushPin.class);
	}

	/**
	 * Creates the entity.
	 *
	 * @param anEntity
	 *            the an entity
	 * @return the push pin
	 * @throws DaoException
	 */
	@Override
	public PushPin create(PushPin anEntity) {
		return super.create(anEntity);
	}

	/**
	 * Update.
	 *
	 * @param anEntity
	 *            the an entity
	 * @return the push pin
	 * @throws DaoException
	 */
	@Override
	public PushPin update(PushPin anEntity) {
		return super.update(anEntity);
	}

	/**
	 * Delete entity.
	 *
	 * @param anEntity
	 *            the an entity
	 * @throws DaoException
	 */
	@Override
	public void delete(PushPin anEntity) {
		super.delete(anEntity);
	}

	/**
	 * Delete by pk.
	 *
	 * @param entityPk
	 *            the entity pk
	 * @throws DaoException
	 */
	@Override
	public void deleteByPk(Integer entityPk) {
		super.deleteByPk(entityPk);
	}

	/**
	 * Find by pk.
	 *
	 * @param entityPk
	 *            the entity pk
	 * @return the push pin
	 * @throws DaoException
	 */
	@Override
	public PushPin findByPk(Integer entityPk) {
		return super.findByPk(entityPk);
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws DaoException
	 */
	@Override
	public List<PushPin> findAll() {
		return super.findAll();
	}

	@Override
	public boolean updateGroupName(Integer userid, String colorCode,DataWrapper dataWrapper) {
		try {
			logger.info("Going to update existing group name {} to new group name {} for userid {}",
					dataWrapper.getOldGroupName(), dataWrapper.getNewGroupName(), userid);
			String dynamicQuery = "update PushPin p set p.modifiedTime =:modifiedTime,";
			if (dataWrapper.getNewGroupName() != null)
				dynamicQuery += "  p.groupName =:groupName ";
			if (dataWrapper.getOpacity() != null)
				dynamicQuery += "  ,p.opacity =:opacity ";
			if (dataWrapper.getLabelProperty() != null)
				dynamicQuery += "  ,p.labelProperty =:labelProperty ";
			dynamicQuery += " where p.user.userid =:userId ";
			if (dataWrapper.getOldGroupName() != null)
				dynamicQuery += " and p.groupName =:existingGroupName  ";
			Query query = getEntityManager().createQuery(dynamicQuery);
			query.setParameter(ForesightConstants.MODIFIEDTIME, new Date());
			if (dataWrapper.getNewGroupName() != null)
				query.setParameter(InfraConstants.GROUPNAME, dataWrapper.getNewGroupName());
			if (dataWrapper.getOpacity() != null)
				query.setParameter(InfraConstants.OPACITY, dataWrapper.getOpacity());
			if (dataWrapper.getLabelProperty() != null)
				query.setParameter("labelProperty", dataWrapper.getLabelProperty());
			query.setParameter(ForesightConstants.USERID, userid);
			if (dataWrapper.getOldGroupName() != null)
				query.setParameter(InfraConstants.EXISTING_GROUPNAME, dataWrapper.getOldGroupName());
			query.executeUpdate();
			updatePinDetails(userid, dataWrapper.getOpacity(), colorCode, dataWrapper.getImageName(),
					dataWrapper.getOldPinStatus(), dataWrapper.getNewPinStatus(), dataWrapper.getNewGroupName());
			return true;
		} catch (QueryTimeoutException queryTimeoutException) {
			logger.error("QueryTimeoutException Message {} ", queryTimeoutException.getMessage());
			throw new RestException("Unable to update group name.");
		} catch (Exception exception) {
			logger.error("Error in updating group name {} for userid {} Exception {} ", dataWrapper.getNewGroupName(),
					userid, ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to update group name.");
		}
	}

	/**
	 * Gets the group names by user id.
	 *
	 * @param userid
	 *            the userid
	 * @return the group names by user id
	 * @throws RestException
	 *             the rest exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getGroupNamesByUserId(Integer userid) {
		List<String> groupNames = new ArrayList<>();
		try {
			logger.info("Going to get group name for userid {}", userid);
			Query query = getEntityManager().createNamedQuery("getGroupNamesByUserId");
			query.setParameter(ForesightConstants.USERID, userid);
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Error in getting group names for userid {} Exception {} ", userid,
					ExceptionUtils.getStackTrace(exception));
		}
		return groupNames;
	}

	/**
	 * Delete group name.
	 *
	 * @param userid
	 *            the userid
	 * @param groupName
	 *            the group name
	 * @return true, if successful
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@Transactional
	public boolean deleteGroupName(Integer userid, String groupName) {
		try {
			logger.info("Going to delete group name {} for userid {}", groupName, userid);
			Query query = getEntityManager().createNamedQuery("deleteGroupName");
			query.setParameter(InfraConstants.GROUPNAME, groupName.toUpperCase());
			query.setParameter(ForesightConstants.USERID, userid);
			query.executeUpdate();
			return true;
		} catch (Exception exception) {
			logger.error("Error in deleting group name {} for userid {} Exception {} ", groupName, userid,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to delete group name.");
		}
	}

	/**
	 * Delete pin from group.
	 *
	 * @param userid
	 *            the userid
	 * @param id
	 *            the id
	 * @return true, if successful
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@Transactional
	public boolean deletePinFromGroup(Integer userid, Integer id) {
		try {
			logger.info("Going to delete pin id {} for userid {}", id, userid);
			Query query = getEntityManager().createNamedQuery("deletePinFromGroup");
			query.setParameter(ForesightConstants.ID, id);
			query.setParameter(ForesightConstants.USERID, userid);
			query.executeUpdate();
			return true;
		} catch (Exception exception) {
			logger.error("Error in deleting pin id {} for userid {} Exception {} ", userid, id,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to delete pin from group.");
		}
	}

	/**
	 * Gets the group names.
	 *
	 * @param userid
	 *            the userid
	 * @return the group names
	 * @throws RestException
	 *             the rest exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getGroupNames(Integer userid) {
		try {
			logger.info("Going to get group names for userid {}", userid);
			Query query = getEntityManager().createNamedQuery("getGroupNameByUserId");
			query.setParameter(ForesightConstants.KEY_USER_ID, userid);
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting group names for userid {} Exception {} ", userid,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get group names.");
		}
	}

	/**
	 * Gets the pin names for group names.
	 *
	 * @param groupNames
	 *            the group names
	 * @param userid
	 *            the userid
	 * @return the pin names for group names
	 * @throws RestException
	 *             the rest exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPinNamesForGroupNames(List<String> groupNames, Integer userid) {
		try {
			logger.info("Going to get pins names for userid {}", userid);
			Query query = getEntityManager().createNamedQuery("getPinNamesForGroupNames");
			query.setParameter(ForesightConstants.USERID, userid);
			query.setParameter(InfraConstants.GROUPNAMES, groupNames);
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting pins names for userid {} Exception {} ", userid,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get group names.");
		}
	}

	/**
	 * Gets the pin details.
	 *
	 * @param userid
	 *            the userid
	 * @param groupName
	 *            the group name
	 * @return the pin details
	 * @throws RestException
	 *             the rest exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DataWrapper> getPinGroupDetails(String groupName, Integer lowerLimit, Integer upperLimit,
			Integer userid) {
		try {
			logger.info("Going to get pin details for userid {} ", userid);
			String dynamicQuery = "select new com.inn.foresight.core.mylayer.wrapper.DataWrapper(p.groupName,p.pinStatus,p.imageName,p.colorCode,p.opacity,min(p.createdTime),"
					+ "max(p.modifiedTime),p.sharedBy,p.sharedDate,p.labelProperty) from PushPin p where p.user.userid =:userid ";

			if (groupName != null) {
				dynamicQuery += " and upper(p.groupName) like :groupName";
			}
			dynamicQuery += " group by p.groupName order by max(p.modifiedTime) desc";

			Query query = getEntityManager().createQuery(dynamicQuery);
			query.setParameter(ForesightConstants.KEY_USER_ID, userid);
			if (groupName != null) {
				query.setParameter(InfraConstants.GROUPNAME, groupName.toUpperCase());
			}

			if (lowerLimit != null && upperLimit != null && lowerLimit >= 0 && upperLimit > 0) {
				query.setMaxResults(upperLimit - lowerLimit);
				query.setFirstResult(lowerLimit);
			}
			return query.getResultList();
		} catch (QueryTimeoutException queryTimeoutException) {
			logger.error("QueryTimeoutException Message {} ", queryTimeoutException.getMessage());
			throw new RestException("Unable to get pin details.");
		} catch (Exception exception) {
			logger.error("Error in getting pin details for group name {} ,Exception {} ", groupName,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pin details.");
		}
	}

	/**
	 * Delete pins by pin status.
	 *
	 * @param userid
	 *            the userid
	 * @param groupName
	 *            the group name
	 * @param pinStatus
	 *            the pin status
	 * @return true, if successful
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@Transactional
	public boolean deletePinsByPinStatus(Integer userid, String groupName, String pinStatus) {
		try {
			logger.info("Going to delete pins from group {} having pinstatus {} for userid {}", groupName, pinStatus,
					userid);
			Query query = getEntityManager().createNamedQuery("deletePinsByPinStatus");
			query.setParameter(ForesightConstants.USERID, userid);
			query.setParameter(InfraConstants.GROUPNAME, groupName.toUpperCase());
			query.setParameter(InfraConstants.PINSTATUS, pinStatus.toUpperCase());
			query.executeUpdate();
			return true;
		} catch (Exception exception) {
			logger.error("Error in deleting from group {} having pinstatus {} for userid {} Exception {} ", groupName,
					pinStatus, userid, ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to delete pins from group.");
		}
	}

	@Override
	public PushPin getPushPinById(Integer id) {
		try {
			logger.info("Going to get pushpin by id {}", id);
			Query query = getEntityManager().createNamedQuery("getPushPinById");
			query.setParameter(InfraConstants.ID, id);
			logger.info("existing pin details: {}" + query.getSingleResult());
			return (PushPin) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting id {} Exception {} ", id, ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get id.");
		}
	}

	@Override
	public Long getPinGroupListCount(Integer userid, String searchTerm) {
		try {
			logger.info("Going to get counts of pin for userid {}", userid);
			String dynamicQuery = "select count(distinct p.groupName) from PushPin p where p.user.userid =:userId ";
			if (searchTerm != ForesightConstants.BLANK_STRING && searchTerm != null)
				dynamicQuery += " and p.groupName like :searchTerm ";
			Query query = getEntityManager().createQuery(dynamicQuery);
			query.setParameter(ForesightConstants.USERID, userid);
			if (searchTerm != ForesightConstants.BLANK_STRING && searchTerm != null)
				query.setParameter(InfraConstants.SEARCHTERM,
						ForesightConstants.MODULUS + searchTerm + ForesightConstants.MODULUS);

			return (Long) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No group found");
		} catch (Exception exception) {
			logger.error("Error in getting counts of pins for userid {} Exception {} ", userid,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get group name of pins");
		}
	}

	@Override
	@Transactional
	public List<PushPin> getPinGroupData(Integer userid, String groupName, Integer lowerLimit, Integer upperLimit) {
		try {
			logger.info("Going to get pin data for userid {},groupName {}", userid, groupName);
			Query query = getEntityManager().createNamedQuery("getPinGroupData");
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			query.setParameter(ForesightConstants.USERID, userid);
			if (lowerLimit != null && upperLimit != null && lowerLimit >= 0 && upperLimit > 0) {
				query.setMaxResults(upperLimit - lowerLimit);
				query.setFirstResult(lowerLimit);
			}
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting data for groupName {} Exception {} ", groupName,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pin group data.");
		}
	}

	@Override
	public List<DataWrapper> searchPinDetails(Integer userid, List<String> groupName, String pinName) {
		List<DataWrapper> dataWrapperList=new ArrayList<>();
		try {
			logger.info("Going to get pin data for pinName {},groupName {},userid {}", pinName, groupName, userid);
			Query query = getEntityManager().createNamedQuery("searchPinDetails");
			query.setParameter(ForesightConstants.USERID, userid);
			query.setParameter(InfraConstants.PINNAME, pinName);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			List<PushPin> pushPinList=query.getResultList();
			return getWrapperForPins(pushPinList);
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting data for pin : {} Exception {} ", pinName, ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pin data.");
		}
	}
	private List<DataWrapper> getWrapperForPins(List<PushPin> pushPinList) {
		List<DataWrapper> dataWrapperList=new ArrayList<>();
		DataWrapper dataWrapper=null;
		try {
		if(pushPinList != null) {
		for (PushPin pushPin : pushPinList) {
		try {
			dataWrapper=new DataWrapper();
			if(pushPin.getId() != null)
				dataWrapper.setId(pushPin.getId());
			dataWrapper.setPinName(pushPin.getPinName() != null ? pushPin.getPinName() : ForesightConstants.HIPHEN);
			if(pushPin.getLatitude() != null)
				dataWrapper.setPinLatitude(pushPin.getLatitude());
			if(pushPin.getLongitude() != null)
				dataWrapper.setPinLongitude(pushPin.getLongitude());
			dataWrapper.setComments(pushPin.getComments() != null ? pushPin.getComments() : ForesightConstants.HIPHEN);
			dataWrapper.setGroupName(pushPin.getGroupName() != null ? pushPin.getGroupName() : ForesightConstants.HIPHEN);
			if(pushPin.getCreatedTime() != null)
				dataWrapper.setCreatedTime(pushPin.getCreatedTime());
			if(pushPin.getModifiedTime() != null)
				dataWrapper.setModifiedTime(pushPin.getModifiedTime());
			dataWrapper.setPinStatus(pushPin.getPinStatus() != null ? pushPin.getPinStatus() : ForesightConstants.HIPHEN);
			dataWrapper.setAdditionalInfo(pushPin.getAdditionalInfo() != null ? pushPin.getAdditionalInfo() : ForesightConstants.HIPHEN);
			if(pushPin.getZoomLevel() != null)
				dataWrapper.setZoomLevel(pushPin.getZoomLevel());
			if(pushPin.getColorCode() != null)
				dataWrapper.setColorCode(pushPin.getColorCode());
			dataWrapper.setImageName(pushPin.getImageName() != null ? pushPin.getImageName() : ForesightConstants.HIPHEN);
			dataWrapper.setSharedBy(pushPin.getSharedBy() != null ? pushPin.getSharedBy() : ForesightConstants.HIPHEN);
			if(pushPin.getSharedDate() != null)
				dataWrapper.setSharedDate(pushPin.getSharedDate());
			dataWrapper.setLabelProperty(pushPin.getLabelProperty() != null ? pushPin.getLabelProperty() : ForesightConstants.HIPHEN);
			if(pushPin.getGeographyL4() != null)	{
				dataWrapper.setGeographyL4(pushPin.getGeographyL4().getDisplayName() != null ? pushPin.getGeographyL4().getDisplayName() : ForesightConstants.HIPHEN);
				if(pushPin.getGeographyL4().getGeographyL3() != null) {
					dataWrapper.setGeographyL3(pushPin.getGeographyL4().getGeographyL3().getDisplayName() != null ? pushPin.getGeographyL4().getGeographyL3().getDisplayName() : ForesightConstants.HIPHEN);
					if(pushPin.getGeographyL4().getGeographyL3().getGeographyL2() != null) {
						dataWrapper.setGeographyL2(pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getDisplayName() != null ? pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getDisplayName() : ForesightConstants.HIPHEN);
						if(pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1() != null) 
							dataWrapper.setGeographyL1(pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1().getDisplayName() != null ? pushPin.getGeographyL4().getGeographyL3().getGeographyL2().getGeographyL1().getDisplayName() : ForesightConstants.HIPHEN);
							
					}
				}
			}
			
			dataWrapperList.add(dataWrapper);
			dataWrapper=null;
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting wrapper for pin : {} Exception {} ",ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pin data.");
		}
		}
		}
		}catch(Exception exception) {
			logger.error("Error in getting wrapper Exception {} ",ExceptionUtils.getStackTrace(exception));
		}
		return dataWrapperList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPinStatusList(Integer userid) {
		try {
			logger.info("Going to get pin status ");
			Query query = getEntityManager().createNamedQuery("getPinStatusList");
			query.setParameter(ForesightConstants.USERID, userid);
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting pin status Exception {} ", ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pin status.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataWrapper> getPinStatusByGroupName(Integer userid, String groupName) {
		try {
			logger.info("Going to get pin status ");
			Query query = getEntityManager().createNamedQuery("getColorCodeAndImageNameByPinStatus");
			query.setParameter(ForesightConstants.USERID, userid);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting pin status Exception {} ", ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pin status.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPinGroupNameList(Integer userid) {
		try {
			Query query = getEntityManager().createNamedQuery("getPinGroupNameList");
			query.setParameter(ForesightConstants.USERID, userid);
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting pin group name Exception {} ", ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pin group name.");
		}
	}

	@Override
	public boolean isPinExistInGroup(String pinName, String groupName, Integer userid) {
		boolean result = false;
		Long count = 0L;
		try {
			logger.info("Going to check if pin {} exist in group {}", pinName, groupName);
			Query query = getEntityManager().createNamedQuery("isPinExistInGroup");
			query.setParameter(InfraConstants.PINNAME, pinName);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			query.setParameter(ForesightConstants.KEY_USER_ID, userid);
			count = (Long) query.getSingleResult();
			if (count > 0) {
				result = true;
			}
			logger.info("is Pin Exist In Group: {}", result);
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Error in getting pin detail for {},group {} Exception {} ", pinName, groupName,
					ExceptionUtils.getStackTrace(exception));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPinNameByGroupName(Integer userid, List<String> groupName) {
		try {
			logger.info("Going to get all pins for group name {} and userid {}", groupName, userid);
			Query query = getEntityManager().createNamedQuery("getPinNameByGroupName");
			query.setParameter(ForesightConstants.USERID, userid);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting pins for group name {} for userid {} Exception {} ", groupName, userid,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pins.");
		}
	}

	@Transactional
	@Override
	public List<DataWrapper> getListOfPinGroupsBySearchTerm(Integer userid, Integer lowerLimit, Integer upperLimit,
			String groupName) {
		try {
			logger.info("Going to get list of group name  {}", groupName);
			String dynamicQuery = "select new com.inn.foresight.core.mylayer.wrapper.DataWrapper(p.latitude,p.longitude,p.comments,"
					+ "p.groupName,p.createdTime,p.modifiedTime,p.pinStatus,p.additionalInfo,p.zoomLevel,p.colorCode,p.labelProperty) from PushPin p where p.user.userid =:userid and ";
			if (groupName != ForesightConstants.BLANK_STRING)
				dynamicQuery += " p.groupName like :groupName ";
			dynamicQuery += "  group by p.groupName order by p.modifiedTime desc ";
			Query query = getEntityManager().createQuery(dynamicQuery);
			query.setParameter(InfraConstants.GROUPNAME,
					ForesightConstants.MODULUS + groupName + ForesightConstants.MODULUS);
			query.setParameter(ForesightConstants.KEY_USER_ID, userid);
			if (lowerLimit != null && upperLimit != null && lowerLimit >= 0 && upperLimit > 0) {
				query.setMaxResults(upperLimit - lowerLimit);
				query.setFirstResult(lowerLimit);
			}

			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No pins found");
		} catch (Exception exception) {
			logger.error("Error in getting pins Exception {} ", ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pins");
		}
	}

	@Transactional
	@Override
	public List<DataWrapper> getListOfPinBySearchTerm(Integer userid, Integer lowerLimit, Integer upperLimit,String pinName, String groupName) {
		try {
			logger.info("Going to get list of pinName  {}", pinName);
			String dynamicQuery = "select new com.inn.foresight.core.mylayer.wrapper.DataWrapper(p.id,p.pinName,p.latitude,p.longitude,p.comments,p.groupName,p.createdTime,p.modifiedTime,p.pinStatus,p.additionalInfo,p.zoomLevel,p.colorCode,p.labelProperty) from PushPin p where  p.groupName =:groupName and p.user.userid =:userid ";
			if (pinName != ForesightConstants.BLANK_STRING)
				dynamicQuery += " and p.pinName like :pinName  ";
			dynamicQuery += "  order by p.modifiedTime desc ";
			Query query = getEntityManager().createQuery(dynamicQuery);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			query.setParameter(ForesightConstants.KEY_USER_ID, userid);
			query.setParameter(InfraConstants.PINNAME,
					ForesightConstants.MODULUS + pinName + ForesightConstants.MODULUS);
			if (lowerLimit != null && upperLimit != null && lowerLimit >= 0 && upperLimit > 0) {
				query.setMaxResults(upperLimit - lowerLimit);
				query.setFirstResult(lowerLimit);
			}

			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No pins found");
		} catch (Exception exception) {
			logger.error("Error in getting pins Exception {} ", ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pins");
		}
	}

	@Override
	public Long getCountsOfPinsByGroupName(Integer userid, String groupName, String searchTerm) {
		try {
			logger.info("Going to get counts of pins for userid {} groupName {}", userid, groupName);
			String dynamicQuery = "select count(distinct p.pinName) from PushPin p where p.user.userid =:userId ";
			if (groupName != ForesightConstants.BLANK_STRING && groupName != null)
				dynamicQuery += " and p.groupName =:groupName ";
			if (searchTerm != ForesightConstants.BLANK_STRING && searchTerm != null)
				dynamicQuery += " and p.pinName like :searchTerm ";
			Query query = getEntityManager().createQuery(dynamicQuery);
			query.setParameter(ForesightConstants.USERID, userid);
			if (searchTerm != ForesightConstants.BLANK_STRING && searchTerm != null)
				query.setParameter(InfraConstants.SEARCHTERM,
						ForesightConstants.MODULUS + searchTerm + ForesightConstants.MODULUS);
			if (groupName != ForesightConstants.BLANK_STRING && groupName != null)
				query.setParameter(InfraConstants.GROUPNAME, groupName);
			return (Long) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No group found");
		} catch (Exception exception) {
			logger.error("Error in getting counts of pins for userid {} Exception {} ", userid,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get group name of pins");
		}
	}

	@Override
	public PushPin getPinDetailsForGroupName(Integer userid, String groupName, Integer id) {
		try {
			logger.info("Going to get pin data for groupName {},userid {}", groupName, userid);
			Query query = getEntityManager().createNamedQuery("getPinDetailsForGroupName");
			query.setParameter(ForesightConstants.KEY_USER_ID, userid);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			query.setParameter(InfraConstants.ID, id);
			return (PushPin) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting data Exception {} ", ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pin data.");
		}
	}

	@Override
	public boolean isPinExistForGroupName(Integer userid, String pinName, String groupName, Integer id) {
		boolean result = false;
		Long count = 0L;
		try {
			logger.info("Going to get pins for userid {}, pinName {},groupName {} ", userid, pinName, groupName);
			Query query = getEntityManager().createNamedQuery("isPinExistForGroupName");
			query.setParameter(ForesightConstants.KEY_USER_ID, userid);
			query.setParameter(InfraConstants.PINNAME, pinName);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			query.setParameter(InfraConstants.ID, id);
			count = (Long) query.getSingleResult();
			if (count > 0) {
				result = true;
			}
			logger.info("is pin exist in groupName {}, Result : {}", groupName, result);
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Error in finding pins names for userid {} Exception {} ", userid,
					ExceptionUtils.getStackTrace(exception));
		}
		return result;
	}

	@Override
	public List<String> getAllGroupNames(Integer userid, String groupName) {
		try {
			logger.info("Going to get group names for userid {}", userid);
			String dynamicQuery = "select distinct p.groupName from PushPin p where p.user.userid =:userId ";
			if (groupName != null)
				dynamicQuery += " and p.groupName !=(:groupName) ";
			Query query = getEntityManager().createQuery(dynamicQuery);
			query.setParameter(ForesightConstants.USERID, userid);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting group names for userid {} Exception {} ", userid,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get group names.");
		}
	}

	@Override
	public boolean isGroupExistForUser(String groupName, Integer userid) {
		boolean result = false;
		Long count = 0L;
		try {
			logger.info("Going to check if group {} exist for user {}", groupName, userid);
			Query query = getEntityManager().createNamedQuery("isGroupExistForUser");
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			query.setParameter(ForesightConstants.KEY_USER_ID, userid);
			count = (Long) query.getSingleResult();
			if (count > 0) {
				result = true;
			}
			logger.info("is Group Exist for User: {}", result);
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Error in getting group detail for {},user {} Exception {} ", groupName, userid,
					ExceptionUtils.getStackTrace(exception));
		}
		return result;
	}

	public boolean updatePinDetails(Integer userid, Double opacity, String colorCode, String imageName,
			String oldPinStatus, String newPinStatus, String newGroupName) {
		try {
			logger.info("Going to update pin details for group name {} for userid {}", newGroupName, userid);
			String dynamicQuery=" update PushPin p set ";
			if(newPinStatus != null)
				dynamicQuery+=" p.pinStatus =:newPinStatus ";
			if(opacity != null)
				dynamicQuery+=" ,p.opacity =:opacity ";
			if(colorCode != null)
				dynamicQuery+=",p.colorCode =:colorCode ";
			if(imageName != null)
				dynamicQuery+=" ,p.imageName =:imageName ";
			dynamicQuery+="  where p.groupName =:newGroupName and p.user.userid =:userid and p.pinStatus =:oldPinStatus ";
			Query query = getEntityManager().createQuery(dynamicQuery);
			if(newPinStatus != null)
			query.setParameter(InfraConstants.NEWPINSTATUS, newPinStatus);
			if(opacity != null)
			query.setParameter(InfraConstants.OPACITY, opacity);
			if(colorCode != null)
			query.setParameter(InfraConstants.COLORCODE, colorCode);
			if(imageName != null)
			query.setParameter(InfraConstants.IMAGENAME, imageName);
			query.setParameter(InfraConstants.NEWGROUPNAME, newGroupName.toUpperCase());
			query.setParameter(ForesightConstants.KEY_USER_ID, userid);
			query.setParameter(InfraConstants.OLDPINSTATUS, oldPinStatus.toUpperCase());
			query.executeUpdate();
			return true;
		} catch (QueryTimeoutException queryTimeoutException) {
			logger.error("QueryTimeoutException Message {} ", queryTimeoutException.getMessage());
			throw new RestException("Unable to update pin details.");
		} catch (Exception exception) {
			logger.error("Error in updating pin details for group name {} for userid {} Exception {} ", newGroupName,
					userid, ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to update pin details.");
		}
	}

	@Override
	public Long getNumberOfPinsInGroup(String groupName, Integer userid) {
		Long count = 0L;
		try {
			logger.info("Going to get number of pins in group for userid {}", userid);
			Query query = getEntityManager().createNamedQuery("getNumberOfPinsInGroup");
			query.setParameter(ForesightConstants.KEY_USER_ID, userid);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			logger.info("Total Pins in group: {}" + query.getSingleResult());
			return (Long) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting number of pins in group {} Exception {} ", groupName,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get number of pins in group .");
		}
	}
	@Override
	public PushPin getPinData(Integer userid, String groupName, String pinName) {
		try {
			logger.info("Going to get pin data for pinName {},groupName {},userid {}", pinName, groupName, userid);
			Query query = getEntityManager().createNamedQuery("getPinData");
			query.setParameter(ForesightConstants.USERID, userid);
			query.setParameter(InfraConstants.PINNAME, pinName);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			PushPin pushPinData=(PushPin) query.getSingleResult();
			logger.info("getPinData result: {}",pushPinData);
			return pushPinData;
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (NonUniqueResultException nonUniqueResultException) {
			logger.error("No unique result found Message {} ", nonUniqueResultException.getMessage());
			throw new RestException("No result found.");
		}  catch (Exception exception) {
			logger.error("Error in getting data for pin : {} Exception {} ", pinName, ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pin data.");
		}
	}
	@Override
	public List<DataWrapper> getColorCodeAndImageNameByPinStatus(Integer userid, String groupName) {
		try {
			logger.info("Going to get pin data for groupName {},userid {}",  groupName, userid);
			Query query = getEntityManager().createNamedQuery("getColorCodeAndImageNameByPinStatus");
			query.setParameter(ForesightConstants.USERID, userid);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			List<DataWrapper> groupList= query.getResultList();
			logger.info("getPinData result: {}",groupList);
			return groupList;
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (NonUniqueResultException nonUniqueResultException) {
			logger.error("No unique result found Message {} ", nonUniqueResultException.getMessage());
			throw new RestException("No result found.");
		}  catch (Exception exception) {
			logger.error("Error in getting data  Exception {} ", ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get pin data.");
		}
	}
	
	@Override
	public Long getNumberOfPinStatusInGroup(Integer userid,String groupName) {
		try {
			logger.info("Going to get number of pinstatus in group for userid {},groupname {}", userid,groupName);
			Query query = getEntityManager().createNamedQuery("getNumberOfPinStatusInGroup");
			query.setParameter(ForesightConstants.KEY_USER_ID, userid);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			logger.info("Total Pin status in group: {}" + query.getSingleResult());
			return (Long) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting number of pinstatus in group {} Exception {} ", groupName,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get number of pinstatus in group .");
		}
	}
	@Override
	public List<String> getPinStatusByGroupNameAndUserid(Integer userid,String groupName) {
		try {
			logger.info("Going to get distinct pinstatus for userid {},groupname {}", userid,groupName);
			Query query = getEntityManager().createNamedQuery("getPinStatusByGroupNameAndUserid");
			query.setParameter(ForesightConstants.KEY_USER_ID, userid);
			query.setParameter(InfraConstants.GROUPNAME, groupName);
			logger.info("Total Pin status in group: {}" + query.getResultList());
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Error in getting pinstatus in group {} Exception {} ", groupName,
					ExceptionUtils.getStackTrace(exception));
		}
		return null;
	}
}
