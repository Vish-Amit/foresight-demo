package com.inn.foresight.core.gallery.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.gallery.dao.IGalleryDetailDao;
import com.inn.foresight.core.gallery.model.GalleryDetail;
import com.inn.foresight.core.gallery.service.IGalleryDetailService;
import com.inn.foresight.core.gallery.utils.GalleryDetailConstants;
import com.inn.foresight.core.gallery.utils.OrganizationType;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IAdvanceSearchConfigurationDao;
import com.inn.foresight.core.infra.dao.IAdvanceSearchDao;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.model.AdvanceSearchConfiguration;
import com.inn.foresight.core.infra.service.IAdvanceSearchProvider;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.UserContextService;

@Service("GalleryDetailServiceImpl")
public class GalleryDetailServiceImpl extends AbstractService<Integer, GalleryDetail> implements IGalleryDetailService,IAdvanceSearchProvider {

	private Logger logger = LogManager.getLogger(GalleryDetailServiceImpl.class);

	@Autowired
	private IGalleryDetailDao iGalleryDetailDao;

	@Autowired
	private IAdvanceSearchDao iAdvanceSearchDao;
	
	@Autowired
	private IAdvanceSearchConfigurationDao iAdvanceSearchConfigurationDao;
	
	@Autowired
	private UserContextService userInContext;

	@Autowired
	public void setDao(IGalleryDetailDao galleryDetailDao) {
		super.setDao(galleryDetailDao);
		this.iGalleryDetailDao = galleryDetailDao;
	}

	@Override
	@Transactional
	public List<GalleryDetail> getGallerySmileForVisualization(Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat) {
		logger.info("Going to get Gallery and Smile for visualization by southWestLong {} southWestLat {} northEastLong {} northEastLat {} ", southWestLong, southWestLat, northEastLong, northEastLat);
		List<GalleryDetail> galleryDetails = new ArrayList<>();
		try {
			List<GalleryDetail> details = iGalleryDetailDao.getGallerySmileForVisualization(southWestLong, southWestLat, northEastLong, northEastLat);
		if (details != null && !details.isEmpty()) {
				logger.info("Total Gallery and smile found {} ", details.size());
				details.forEach(galleryDetail -> setGalleryDetailInConstructor(galleryDetail, galleryDetails));
			}
		} catch (Exception exception) {
			logger.error("Unable to get Gallery and Smile Data for layer {} ", ExceptionUtils.getStackTrace(exception));
		}
		return galleryDetails;
	}

	private void setGalleryDetailInConstructor(GalleryDetail galleryDetail, List<GalleryDetail> galleryDetails) {
		galleryDetails.add(new GalleryDetail(galleryDetail.getRegional() != null ? galleryDetail.getRegional() : null, galleryDetail.getName() != null ? galleryDetail.getName() : null,
				galleryDetail.getAddress() != null ? galleryDetail.getAddress() : null, galleryDetail.getLatitude() != null ? galleryDetail.getLatitude() : null,
				galleryDetail.getLongitude() != null ? galleryDetail.getLongitude() : null,
				galleryDetail.getL2Manager() != null && galleryDetail.getL2Manager().getName() != null ? galleryDetail.getL2Manager().getName() :null,
				galleryDetail.getMondayToFridayTime() != null ? galleryDetail.getMondayToFridayTime() : null, galleryDetail.getSaturdayTime() != null ? galleryDetail.getSaturdayTime() : null,
				galleryDetail.getSundayTime() != null ? galleryDetail.getSundayTime() : null, galleryDetail.getType() != null ? galleryDetail.getType() : null));
	}

	@Override
	public List<GalleryDetail> getGallerySmileDataList(Integer llimit, Integer ulimit) {
		logger.info("Going to fetch Gallery and Smile Data List");
		return iGalleryDetailDao.getGallerySmileDataList(llimit, ulimit);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Map<String, String> createGallerySmile(GalleryDetail gallery) {
		logger.info("Going to create Gallery and Smile");
		String message = ForesightConstants.BLANK_STRING;
		Map<String, String> map = new HashMap<>();
		try {
			if (validateGallerySmileData(gallery)) {
				User user = userInContext.getUserInContextnew();
				gallery.setLastModifier(user);
				gallery.setCreator(user);
				gallery.setCreationTime(new Date());
				gallery.setModificationtime(new Date());
				gallery.setEnabled(true);
				GalleryDetail createdGallery = iGalleryDetailDao.create(gallery);

				if (createdGallery != null) {
					message = GalleryDetailConstants.GALLERY_CREATE_SUCCESSSFULLY;
					logger.info(GalleryDetailConstants.GALLERY_CREATE_SUCCESSSFULLY);
					AdvanceSearch search = new AdvanceSearch();
					search.setCreationTime(new Date());
					search.setModificationTime(new Date());
					if (iAdvanceSearchDao.create(createAdvanceSearchData(createdGallery, search)) != null) {
						logger.info("AdvanceSearch data created successfully");
					} else {
						logger.info("AdvanceSearch data creation failed");
					}
				}
			} else {
				message = GalleryDetailConstants.GALLERY_CREATE_FAILED;
			}
		} catch (Exception e) {
			message = GalleryDetailConstants.GALLERY_CREATE_FAILED;
			logger.error(" Error while creating Gallery and Smile {}", ExceptionUtils.getStackTrace(e));
		}
		map.put(ForesightConstants.MESSAGE, message);
		return map;
	}

	private AdvanceSearch setAdvanceSearchData(GalleryDetail galleryDetail, AdvanceSearch search) {
		logger.info("Going to set AdvanceSearchData");
		try {
			if (galleryDetail.getName() != null)
				search.setName(galleryDetail.getName() + ForesightConstants.DOLLER + galleryDetail.getId());
			search.getAdvanceSearchConfiguration().setType(galleryDetail.getType());
			if (galleryDetail.getId() != null)
				search.setTypereference(galleryDetail.getId());
		} catch (Exception e) {
			logger.error("Error while setting AdvanceSearchData : {} ", ExceptionUtils.getStackTrace(e));
		}
		return search;
	}

	private AdvanceSearch createAdvanceSearchData(GalleryDetail galleryDetail, AdvanceSearch search) {
		logger.info("Going to set AdvanceSearchData");
		try {
			if (galleryDetail.getName() != null)
				search.setName(galleryDetail.getName() + ForesightConstants.DOLLER + galleryDetail.getId());
			if (Utils.checkForValueInString(galleryDetail.getType())) {
				AdvanceSearchConfiguration advanceSearchConfig = iAdvanceSearchConfigurationDao
						.getAdvanceSearchConfigurationByType(galleryDetail.getType());
				search.setAdvanceSearchConfiguration(advanceSearchConfig);
			}
			if (galleryDetail.getId() != null)
				search.setTypereference(galleryDetail.getId());
		} catch (Exception e) {
			logger.error("Error while setting AdvanceSearchData : {} ", ExceptionUtils.getStackTrace(e));
		}
		return search;
	}
	
	@Transactional
	@Override
	public Map<String, String> updateGallerySmile(GalleryDetail gallery) {
		logger.info("Going to update Gallery and Smile");
		String message = ForesightConstants.BLANK_STRING;
		Map<String, String> map = new HashMap<>();
		try {
			if (validateGallerySmileData(gallery) && gallery.getId() != null) {
				GalleryDetail gd = iGalleryDetailDao.findByPk(gallery.getId());
				User user = userInContext.getUserInContextnew();
				updateGalleryDetail(gallery, gd, user);
				GalleryDetail createdGallery = iGalleryDetailDao.update(gallery);

				if (createdGallery != null) {
					message = GalleryDetailConstants.GALLERY_UPDATE_SUCCESSFULLY;
					logger.info(GalleryDetailConstants.GALLERY_UPDATE_SUCCESSFULLY);
					AdvanceSearch advanceSearch = null;
					advanceSearch = iAdvanceSearchDao.getAdvanceSearchByTypeReference(createdGallery.getName() + ForesightConstants.DOLLER + createdGallery.getId());
					if (advanceSearch != null) {
						updateGalleryDetailWithAdvanceSearch(createdGallery, advanceSearch);
					} else {
						logger.info("advancesearch is null");
					}
				}
			} else {
				message = GalleryDetailConstants.GALLERY_UPDATE_FAILED;
			}
		} catch (Exception e) {
			message = GalleryDetailConstants.GALLERY_UPDATE_FAILED;
			logger.error(" Error while updating Gallery and Smile : {}", ExceptionUtils.getStackTrace(e));
		}
		map.put(ForesightConstants.MESSAGE, message);
		logger.info("Message {}",message);
		return map;
	}

	private void updateGalleryDetailWithAdvanceSearch(GalleryDetail createdGallery, AdvanceSearch advanceSearch) {
		logger.info("Advancesearch is not null {}", advanceSearch);
		try {
			if (iAdvanceSearchDao.update(setAdvanceSearchData(createdGallery, advanceSearch)) != null) {
				logger.info("Advancesearch data updated successfully");
			} else {
				logger.info("Advancesearch data updation failed");
			}
		} catch (Exception e) {
			logger.error("Error while updating advance search data : ", ExceptionUtils.getStackTrace(e));
		}
	}

	private void updateGalleryDetail(GalleryDetail gallery, GalleryDetail gd, User user) {
		gallery.setCreator(gd.getCreator());
		gallery.setCreationTime(gd.getCreationTime());
		gallery.setLastModifier(user);
		gallery.setModificationtime(new Date());
		gallery.setEnabled(gd.getEnabled());
	}

	@Override
	public Map<String, Long> getGallerySmileCount() {
		logger.info("Going to count Gallery and Smile");
		Map<String, Long> map = new HashMap<>();
		Long count = iGalleryDetailDao.getGallerySmileCount();
		map.put(ForesightConstants.COUNT, count);
		return map;
	}

	@Override
	public Map<String, Long> getGallerySmileCountBySearchName(String name) {
		logger.info("Going to count Gallery and Smile by searching through name");
		Map<String, Long> map = new HashMap<>();
		Long count = iGalleryDetailDao.getGallerySmileCountBySearchName(name);
		map.put(ForesightConstants.COUNT, count);
		return map;
	}

	@Override
	@Transactional
	public GalleryDetail getGallerySmileById(Integer id) {
		logger.info("Going to fetch Gallery and Smile Data by Id");
		GalleryDetail gallery = new GalleryDetail();
		try {
			gallery = iGalleryDetailDao.findByPk(id);
			if(gallery !=null && gallery.getLastModifier()!=null) {
				gallery.setModifiername(gallery.getLastModifier()!=null && gallery.getLastModifier().getUserName()!=null?gallery.getLastModifier().getUserName():null);
			}
		} catch (Exception e) {
			logger.error(" Error while geting Gallery and Smile by Id {}", ExceptionUtils.getStackTrace(e));
		}
		return gallery;
	}

	@Override
	public List<GalleryDetail> searchWithLimit(SearchContext ctx, Integer maxLimit, Integer minLimit) {
		logger.info("Inside searchWithLimit method");
		return iGalleryDetailDao.search(ctx, maxLimit, minLimit);
	}

	@Override
	public List<GalleryDetail> searchByName(String name, Integer llimit, Integer ulimit) {
		logger.info("Inside searchByName method");
		return iGalleryDetailDao.searchByName(name, llimit, ulimit);
	}

	@Transactional
	@Override
	public Map<String, String> deleteGallerySmileById(Integer id) {
		logger.info(" Going to delete Gallery and Smile by Id");
		Map<String, String> map = new HashMap<>();
		String message = ForesightConstants.BLANK_STRING;
		try {
			iGalleryDetailDao.deleteByPk(id);
			message = " Gallery Deleted Successfully";
		} catch (DaoException e) {
			logger.error("Error while deleting Gallery and Smile {}", ExceptionUtils.getStackTrace(e));
		}
		map.put(ForesightConstants.MESSAGE, message);
		return map;
	}

	private boolean validateGallerySmileData(GalleryDetail gallery) {
		logger.info("Going to validate Gallery and Smile Data");
		return (gallery.getName() != null && gallery.getLatitude() != null && gallery.getLongitude() != null);
	}


	@Override
	public Map<String, String> searchGallerySmileForAdvanceSearch(Integer galleryId) {
		logger.info("Going to search Gallery and Smile for Advance Search");
		Map<String, String> galleryMap = new HashMap<>();
		try {
			if (galleryId != null) {
				GalleryDetail galleryDetail = iGalleryDetailDao.findByPk(galleryId);
				galleryMap.put(GalleryDetailConstants.GALLERY_NAME, galleryDetail.getName()!=null?galleryDetail.getName():null);
				galleryMap.put(GalleryDetailConstants.GALLERY_REGIONAL, galleryDetail.getRegional()!=null?galleryDetail.getRegional():null);
				galleryMap.put(GalleryDetailConstants.GALLERY_ADDRESS, galleryDetail.getAddress()!=null?galleryDetail.getAddress():null);
				galleryMap.put(GalleryDetailConstants.GALLERY_CONTACT_NAME, galleryDetail.getL2Manager()!=null && galleryDetail.getL2Manager().getName()!=null?galleryDetail.getL2Manager().getName():null);
				galleryMap.put(GalleryDetailConstants.GALLERY_LATITUDE, String.valueOf(galleryDetail.getLatitude()));
				galleryMap.put(GalleryDetailConstants.GALLERY_LONGITUDE, String.valueOf(galleryDetail.getLongitude()));
			}
		} catch (Exception exception) {
			logger.error("Unable to search Gallery and Smile for Id {} {} ", galleryId, ExceptionUtils.getStackTrace(exception));
		}
		return galleryMap;
	}

	@Override
	public String getGallerySmileManagerByManagerType(String managerType) {
		logger.info("Going to get Gallery and Smile Manager By Manager type {} ", managerType);
		StringBuilder managerFinalData = new StringBuilder(ForesightConstants.BLANK_STRING);
		List<String> managerList = null;
		try {
			if (managerType != null && !managerType.isEmpty()) {
				managerList = new ArrayList<>();
				if (managerType.equalsIgnoreCase(OrganizationType.GALLERY.toString())) {
					managerList = ConfigUtils.getStringList(GalleryDetailConstants.GALLERY_MANAGER);
				} else if (managerType.equalsIgnoreCase(OrganizationType.SMILE.toString())) {
					managerList = ConfigUtils.getStringList(GalleryDetailConstants.SMILE_MANAGER);
				}
				for (String str : managerList) {
					managerFinalData.append(str).append(ForesightConstants.COMMA);
				}
			} else {
				throw new RestException("Manager Type can not be null " + managerType);
			}
		} catch (Exception exception) {
			logger.error("Unable to get Manager By ManagerType {} Exception {} ", managerType, ExceptionUtils.getStackTrace(exception));
			 throw new RestException(exception.getMessage());
		}
		return managerFinalData.substring(0, managerFinalData.length() - 1);
	}
	
	@Override
	public Map<String, Object> getSearchData(Map<String, Object> map) {
		logger.info("Going to search Gallery and Smile for Advance Search");
		Map<String, Object> galleryMap = new HashMap<>();
		try {
			if (map.get("id") != null) {
				AdvanceSearch search = (AdvanceSearch)map.get("advanceSearch");
				GalleryDetail galleryDetail = iGalleryDetailDao.findByPk(search.getTypereference());
				galleryMap.put(GalleryDetailConstants.GALLERY_NAME, galleryDetail.getName()!=null?galleryDetail.getName():null);
				galleryMap.put(GalleryDetailConstants.GALLERY_REGIONAL, galleryDetail.getRegional()!=null?galleryDetail.getRegional():null);
				galleryMap.put(GalleryDetailConstants.GALLERY_ADDRESS, galleryDetail.getAddress()!=null?galleryDetail.getAddress():null);
				if(galleryDetail.getL2Manager()!=null && galleryDetail.getL2Manager().getName()!=null)
					galleryMap.put(GalleryDetailConstants.GALLERY_CONTACT_NAME,galleryDetail.getL2Manager().getName());
				else
					galleryMap.put(GalleryDetailConstants.GALLERY_CONTACT_NAME,null);
				galleryMap.put(GalleryDetailConstants.GALLERY_LATITUDE, String.valueOf(galleryDetail.getLatitude()));
				galleryMap.put(GalleryDetailConstants.GALLERY_LONGITUDE, String.valueOf(galleryDetail.getLongitude()));
			}
		} catch (Exception exception) {
			logger.error("Unable to search Gallery and Smile for Id {} {} ", (Integer)map.get("id"), ExceptionUtils.getStackTrace(exception));
		}
		return galleryMap;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public Map<String, String> updateGalleryStatus(List<Integer> galleryPk, Boolean enabled) {
		logger.info("Going to update Gallery and Smile status for id {} ", galleryPk, enabled);
		Map<String, String> map = new HashMap<>();
		try {
			if(galleryPk!=null && !galleryPk.isEmpty()) {
				iGalleryDetailDao.updateGalleryStatus(galleryPk, enabled);
				for (Integer id : galleryPk) {
					GalleryDetail galleryDetail = iGalleryDetailDao.findByPk(id);
					if(galleryDetail!=null && galleryDetail.getEnabled()) {
						AdvanceSearch search = new AdvanceSearch();
						search.setCreationTime(galleryDetail.getCreationTime());
						search.setModificationTime(new Date());
						iAdvanceSearchDao.create(createAdvanceSearchData(galleryDetail, search));
					}
				}
				if(!enabled) {
					iAdvanceSearchDao.deleteGalleryById(galleryPk);
				}
			}
			map.put(ForesightConstants.MESSAGE, ForesightConstants.GALLERY_STATUS_UPDATED_SUCCESSFULLY);
		} catch (Exception e) {
			logger.error("Exception occured while updating Gallery and Smile status ,Message {}", ExceptionUtils.getStackTrace(e));
			throw new RestException("Unable to update gallery status");
		}
		return map;
	}



}
