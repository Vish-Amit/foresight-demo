package com.inn.foresight.module.tribe.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.exceptions.application.ValidationFailedException;
import com.inn.foresight.module.tribe.dao.ITRBBannerDao;
import com.inn.foresight.module.tribe.dao.ITRBBannerViewDao;
import com.inn.foresight.module.tribe.exceptions.BannerAlreadyExistException;
import com.inn.foresight.module.tribe.exceptions.BannerCreationException;
import com.inn.foresight.module.tribe.model.TRBBanner;
import com.inn.foresight.module.tribe.model.TRBBanner.BannerStatus;
import com.inn.foresight.module.tribe.service.ITRBBannerService;
import com.inn.foresight.module.tribe.utils.BannerContentWrapper;
import com.inn.foresight.module.tribe.utils.TRBConstants;
import com.inn.foresight.module.tribe.utils.TRBUtils;
import com.inn.foresight.module.tribe.utils.TRBUtils.TRBFileTypes;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/**
 * The Class TRBBannerServiceImpl.
 */

@Service("TRBBannerServiceImpl")
public class TRBBannerServiceImpl extends AbstractService<Integer, TRBBanner> implements ITRBBannerService {

	private static final String FILE = "FILE";
	private static final String URL = "URL";

	/** The logger. */
	private Logger logger = LogManager.getLogger(TRBBannerServiceImpl.class);

	/** The TRBBannerDao dao. */
	private ITRBBannerDao trbbannerDao;

	/** The t RB banner view dao. */
	@Autowired
	private ITRBBannerViewDao tRBBannerViewDao;

	/**
	 * Sets the dao.
	 *
	 * @param dao the new dao
	 */
	@Autowired
	public void setDao(ITRBBannerDao dao) {
		super.setDao(dao);
		this.trbbannerDao = dao;
	}

	/**
	 * Creates the.
	 *
	 * @param trbbanner the trbbanner
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 */
	@Transactional
	public TRBBanner create(@Valid TRBBanner trbbanner) throws RestException {
		logger.info("@class" + this.getClass().getName() + "@Method create() Create record by TRBBanner :"
				+ trbbanner.getId());
		try {
			trbbanner.setCreator(UserContextServiceImpl.getUserInContext());
			trbbanner.setLastModifier(UserContextServiceImpl.getUserInContext());
			trbbanner.setCreatedTime(new Date());
			trbbanner.setModifiedTime(new Date());
			return super.create(trbbanner);
		} catch (DataIntegrityViolationException ex) {
			logger.error("Error occored Inside @class" + this.getClass().getName() + "@Method create()", ex);
			throw ex;
		} catch (ConstraintViolationException ex) {
			logger.error("Error occored Inside @class" + this.getClass().getName() + "@Method create()", ex);
			throw new ValidationFailedException(ex);
		}
	}

	/**
	 * Update.
	 *
	 * @param trbbanner the trbbanner
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 */
	@Transactional
	public TRBBanner update(@Valid TRBBanner trbbanner) throws RestException {
		logger.info("Update record by TRBBanner :" + trbbanner.getId());
		try {
			trbbanner.setLastModifier(UserContextServiceImpl.getUserInContext());
			trbbanner.setModifiedTime(new Date());
			return trbbannerDao.update(trbbanner);
		} catch (DataIntegrityViolationException ex) {
			logger.error("Inside @class" + this.getClass().getName() + "@Method update", ex);
			throw ex;
		} catch (ConstraintViolationException ex) {
			logger.error("Inside @class" + this.getClass().getName() + "@Method update", ex);
			throw new ValidationFailedException(ex);
		}
	}

	/**
	 * Removes the.
	 *
	 * @param trbbanner the trbbanner
	 * @throws RestException the rest exception
	 */
	@Transactional
	public void remove(TRBBanner trbbanner) throws RestException {
		logger.info("Remove record by TRBBanner :" + trbbanner.getId());
		try {
			super.remove(trbbanner);
		} catch (DataIntegrityViolationException ex) {
			logger.error("Inside @class" + this.getClass().getName() + "@Method remove", ex);

			throw new ValidationFailedException(ex);
		} catch (ConstraintViolationException ex) {
			logger.error("Inside @class" + this.getClass().getName() + "@Method remove", ex);

			throw ex;
		}
	}

	/**
	 * Removes the by id.
	 *
	 * @param primaryKey the primary key
	 * @throws RestException the rest exception
	 */
	@Transactional
	public void removeById(Integer primaryKey) throws RestException {
		logger.info("Remove record by primaryKey :" + primaryKey);
		try {
			super.removeById(primaryKey);
		} catch (DataIntegrityViolationException ex) {
			logger.error("Inside @class" + this.getClass().getName() + "@Method removeById", ex);
			throw new ValidationFailedException(ex);
		} catch (ConstraintViolationException ex) {
			logger.error("Inside @class" + this.getClass().getName() + "@Method removeById", ex);
			throw ex;
		}
	}

	
	/**
	 * Find by id.
	 *
	 * @param primaryKey the primary key
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 */
	@Transactional
	public TRBBanner findById(Integer primaryKey) throws RestException {
		logger.info("find record by primaryKey :" + primaryKey);
		try {
			return super.findById(primaryKey);
		} catch (DataIntegrityViolationException ex) {
			logger.error("Inside @class" + this.getClass().getName() + "@Method findById", ex);
			throw new ValidationFailedException(ex);

		} catch (ConstraintViolationException ex) {
			logger.error("error occored Inside @class" + this.getClass().getName() + "@Method findById", ex);
			throw ex;
		}
	}

	/**
	 * Upload banner attachments.
	 *
	 * @param newBanner the new banner
	 * @param buffBannerStream the buff banner stream
	 * @param fileStream the file stream
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	@Transactional
	public TRBBanner uploadBannerAttachments(TRBBanner newBanner, BufferedInputStream buffBannerStream,
			BufferedInputStream fileStream) throws RestException, DaoException {
		logger.info("Inside @class" + this.getClass().getName() + "@Method uploadBannerAttachments");
		if (FILE.equalsIgnoreCase(newBanner.getBannerType())) {
			logger.info("Inside @class" + this.getClass().getName()
					+ "@Method uploadBannerAttachments :BannerType.FILE.equals(newBanner.getBannerType())==true");

			try {
				if (fileStream != null && fileStream.available() > 0) {
					if (TRBUtils.checkInputFileSize(fileStream)) {
						String newFileName = TRBUtils.generateRandomString() + "."
								+ FilenameUtils.getExtension(newBanner.getFileName());
						String bannerFileDir = TRBUtils.PATH_FOR_TRIBE_BANNER_FILES + TRBUtils.BANNER_ATTACHMENT + "/"
								+ newBanner.getId();
						String response = TRBUtils.storeFilesInFolder(fileStream, bannerFileDir, newFileName);
						if (TRBConstants.SUCCESS.equalsIgnoreCase(response)) {
							newBanner.setFileName(newFileName);
							newBanner.setFilePath(TRBUtils.TRIBE_FILES + TRBUtils.BANNERS + TRBUtils.BANNER_ATTACHMENT
									+ "/" + newBanner.getId() + "/" + newFileName);
						}
					}
				}
			} catch (Exception e) {
				logger.error(
						"error occored Inside @class" + this.getClass().getName() + "@Method uploadBannerAttachments",
						e);
			}
		}

		try {
			if (buffBannerStream != null && buffBannerStream.available() > 0) {
				if (TRBUtils.checkInputFileSize(buffBannerStream)) {
					/*if (TRBUtils.checkContentTypeFromFileTypes(buffBannerStream, TRBFileTypes.IMAGE) || 
							TRBUtils.checkContentTypeFromFileTypes(buffBannerStream, TRBFileTypes.HTML)) {*/
					if(TRBUtils.checkContentTypeFromFileTypes(buffBannerStream, TRBFileTypes.HTML)) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(buffBannerStream));
			             StringBuilder sb = new StringBuilder();
			             String line = null;
			             while ((line = reader.readLine()) != null) {
			                 sb.append(line + "\n");
			             }
			             String kpn;
			             kpn = sb.toString();
			             Document doc = Jsoup.parse(kpn);
			             newBanner.setBannerContent(doc.toString());
			         	InputStream iostream = new ByteArrayInputStream(kpn.getBytes(StandardCharsets.UTF_8));
			         	buffBannerStream = new BufferedInputStream(iostream);
					}
						String newImageName = TRBUtils.generateRandomString() + "."
								+ FilenameUtils.getExtension(newBanner.getImageName());
						String bannerImageDir = TRBUtils.PATH_FOR_TRIBE_BANNER_FILES + TRBUtils.BANNER_IMAGE + "/"
								+ newBanner.getId();
						logger.info("bannerImageDir ::::::: " + bannerImageDir);
						String response = TRBUtils.storeFilesInFolder(buffBannerStream, bannerImageDir, newImageName);
						if (TRBConstants.SUCCESS.equalsIgnoreCase(response)) {
							newBanner.setImageName(newImageName);
							newBanner.setImagePath(TRBUtils.TRIBE_FILES + TRBUtils.BANNERS + TRBUtils.BANNER_IMAGE + "/"
									+ newBanner.getId() + "/" + newImageName);
						}
				/*	}*/
				}
			} else {
				logger.info("banner image does not contain any data");
			}
		} catch (IOException e) {
			logger.error("Inside @class inside @Method uploadBannerAttachments", e);
		}
		if (FILE.equalsIgnoreCase(newBanner.getBannerType()) || URL.equalsIgnoreCase(newBanner.getBannerType())) {
			newBanner.setEnable(true);
		}
		return trbbannerDao.update(newBanner);
	}

	/**
	 * Creates the banner.
	 *
	 * @param bannerStream the banner stream
	 * @param fileStream the file stream
	 * @param bannerStr the banner str
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	@Transactional
	public TRBBanner createBanner(InputStream bannerStream, InputStream fileStream, String bannerStr)
			throws RestException, DaoException {
		logger.info("Inside @class" + this.getClass().getName() + "@Method createBanner");
		Gson gson = new Gson();
		TRBBanner banner = gson.fromJson(bannerStr, TRBBanner.class);
		if (banner != null) {
			TRBBanner oldBanner = trbbannerDao.findByBannerTitle(banner.getTitle());
			if (oldBanner != null) {
				logger.info("Inside  @class" + this.getClass().getName() + "@Method createBanner oldBanner != null ");
				throw new BannerAlreadyExistException("banner with specified title already exist.");
			} else {
				BufferedInputStream buffBannerStream = new BufferedInputStream(bannerStream);
				BufferedInputStream buffFileStream = new BufferedInputStream(fileStream);
				return uploadBannerAttachments(trbbannerDao.create(banner), buffBannerStream, buffFileStream);
			}
		} else {
			throw new BannerCreationException("banner cannot be null");
		}
	}

	/**
	 * Update existing banner.
	 *
	 * @param bannerStream the banner stream
	 * @param fileStream the file stream
	 * @param banner the banner
	 * @param persistedBanner the persisted banner
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	@Transactional
	public TRBBanner updateExistingBanner(InputStream bannerStream, InputStream fileStream, TRBBanner banner,
			TRBBanner persistedBanner) throws RestException, DaoException {
		logger.info("Inside @class" + this.getClass().getName() + "@Method updateExistingBanner");

		BufferedInputStream buffBannerStream = new BufferedInputStream(bannerStream);
		try {
			logger.info("buffBannerStream updateExistingBanner : " + buffBannerStream.available());
			if (buffBannerStream != null && buffBannerStream.available() > 0) {
				if (TRBUtils.checkInputFileSize(buffBannerStream)) {
					if (TRBUtils.checkContentTypeFromFileTypes(buffBannerStream, TRBFileTypes.IMAGE)) {
						String bannerImageDir = TRBUtils.PATH_FOR_TRIBE_BANNER_FILES + TRBUtils.BANNER_IMAGE + "/"
								+ banner.getId();
						String deleteResponse = TRBUtils.deleteFilesFromFolder(persistedBanner.getImagePath());
						if (TRBConstants.SUCCESS.equalsIgnoreCase(deleteResponse)) {
							String newImageName = TRBUtils.generateRandomString() + "."
									+ FilenameUtils.getExtension(banner.getImageName());
							String response = TRBUtils.storeFilesInFolder(buffBannerStream, bannerImageDir,
									newImageName);
							if (TRBConstants.SUCCESS.equalsIgnoreCase(response)) {
								persistedBanner.setImageName(newImageName);
								persistedBanner.setImagePath(TRBUtils.TRIBE_FILES + TRBUtils.BANNERS
										+ TRBUtils.BANNER_IMAGE + "/" + banner.getId() + "/" + newImageName);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("error occored in @class" + this.getClass().getName() + "@Method updateExistingBanner");
		}

		String bannerFileDir = TRBUtils.PATH_FOR_TRIBE_BANNER_FILES + TRBUtils.BANNER_ATTACHMENT + "/" + banner.getId();
		TRBUtils.deleteFilesFromFolder(persistedBanner.getFilePath());
		persistedBanner.setTitle(banner.getTitle());
		persistedBanner.setDescription(banner.getDescription());
		persistedBanner.setUrl(banner.getUrl());
		persistedBanner.setBannerType(banner.getBannerType());
		persistedBanner.setStatus(banner.getStatus());
		if (FILE.equalsIgnoreCase(banner.getBannerType())) {
			BufferedInputStream buffFileStream = new BufferedInputStream(fileStream);
			try {
				if (buffFileStream != null && buffFileStream.available() > 0) {
					if (TRBUtils.checkInputFileSize(buffFileStream)) {
						String newFileName = TRBUtils.generateRandomString() + "."
								+ FilenameUtils.getExtension(banner.getFileName());
						String response = TRBUtils.storeFilesInFolder(buffFileStream, bannerFileDir, newFileName);
						if (TRBConstants.SUCCESS.equalsIgnoreCase(response)) {
							persistedBanner.setFileName(newFileName);
							persistedBanner.setFilePath(TRBUtils.TRIBE_FILES + TRBUtils.BANNERS
									+ TRBUtils.BANNER_ATTACHMENT + "/" + banner.getId() + "/" + newFileName);
						}
					}
				}
			} catch (Exception e) {
				logger.error("Error occored Inside @class" + this.getClass().getName() + "@Method updateExistingBanner",
						e);
			}
		} else {
			persistedBanner.setFileName(null);
			persistedBanner.setFilePath(null);
		}
		return trbbannerDao.update(persistedBanner);
	}

	/**
	 * Update banner.
	 *
	 * @param bannerStream the banner stream
	 * @param fileStream the file stream
	 * @param bannerStr the banner str
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	@Transactional
	public TRBBanner updateBanner(InputStream bannerStream, InputStream fileStream, String bannerStr)
			throws RestException, DaoException {
		logger.info("error occored in @class" + this.getClass().getName() + "@Method updateBanner");

		Gson gson = new Gson();
		TRBBanner banner = gson.fromJson(bannerStr, TRBBanner.class);
		if (banner != null) {
			TRBBanner persistedBanner = trbbannerDao.findByPk(banner.getId());
			if (persistedBanner != null) {
				logger.info("Inside  @class" + this.getClass().getName() + "@Method updateBanner");
				if (banner.getTitle().equalsIgnoreCase(persistedBanner.getTitle())) {
					return updateExistingBanner(bannerStream, fileStream, banner, persistedBanner);
				} else {
					TRBBanner oldBanner = trbbannerDao.findByBannerTitle(banner.getTitle());
					if (oldBanner != null) {
						throw new BannerAlreadyExistException("banner with specified title already exist.");
					} else {
						return updateExistingBanner(bannerStream, fileStream, banner, persistedBanner);
					}
				}
			} else {
				throw new RestException("no banner exists with specified id");
			}
		} else {
			throw new BannerCreationException("banner cannot be null");
		}
	}

	/**
	 * Update banner status.
	 *
	 * @param bannerId the banner id
	 * @param status the status
	 * @return the string
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	@Transactional
	public String updateBannerStatus(Integer bannerId, BannerStatus status) throws RestException, DaoException {
		logger.error("error occored in @class" + this.getClass().getName() + "@Method updateBannerStatus" + bannerId);

		TRBBanner banner = trbbannerDao.findByPk(bannerId);
		if (banner != null) {
			if (BannerStatus.ACTIVE.equals(status)) {
				if (BannerStatus.ACTIVE.equals(banner.getStatus())) {
					throw new RestException("banner is already published");
				} else {
					banner.setStatus(BannerStatus.ACTIVE);
					trbbannerDao.update(banner);
					return TRBConstants.SUCCESS_JSON;
				}
			} else if (BannerStatus.DRAFT.equals(status)) {
				if (BannerStatus.DRAFT.equals(banner.getStatus())) {
					throw new RestException("banner is already drafted");
				} else {
					banner.setStatus(BannerStatus.DRAFT);
					trbbannerDao.update(banner);
					return TRBConstants.SUCCESS_JSON;
				}
			} else if (BannerStatus.INACTIVE.equals(status)) {
				if (BannerStatus.INACTIVE.equals(banner.getStatus())) {
					throw new RestException("banner is already inactive");
				} else {
					banner.setStatus(BannerStatus.INACTIVE);
					trbbannerDao.update(banner);
					return TRBConstants.SUCCESS_JSON;
				}
			}
		} else {
			throw new RestException("no banner found with specified id");
		}
		return TRBConstants.FAILURE_JSON;
	}

	/**
	 * Delete banner.
	 *
	 * @param bannerId the banner id
	 * @return the string
	 * @throws RestException the rest exception
	 */
	@Transactional
	public String deleteBanner(Integer bannerId) throws RestException {
		logger.error("error occored in @class" + this.getClass().getName() + "@Method deleteBanner" + bannerId);

		try {
			TRBBanner banner = trbbannerDao.findByPk(bannerId);
			tRBBannerViewDao.deleteBannerViewsByBannerId(bannerId);
			try {
				TRBUtils.deleteFilesFromFolder(banner.getImagePath());
				if (banner.getFileName() != null && banner.getFilePath() != null) {
					TRBUtils.deleteFilesFromFolder(banner.getFilePath());
				}
			} catch (Exception e) {
				logger.error(
						"problem in deleting banner files  @class" + this.getClass().getName() + "@Method deleteBanner",
						e.getMessage());
			}
			super.remove(banner);
			return TRBConstants.SUCCESS_JSON;
		} catch (Exception e) {
			logger.error("@class" + this.getClass().getName()
					+ "@Method TRBBannerServiceImpl some problem in deleting banner views : " + e);
			logger.info("some problem in deleting banner views : " + e.getMessage());
		}
		return TRBConstants.FAILURE_JSON;
	}

	/**
	 * Gets the search record count.
	 *
	 * @param context the context
	 * @return the search record count
	 */
	@Transactional
	public Integer getSearchRecordCount(SearchContext context) {
		logger.info("@class" + this.getClass().getName() + "@Method getSearchRecordCount");
		try {
			return trbbannerDao.getSearchRecordCount(context);
		} catch (Exception ex) {
			logger.error("Error occored Inside @class" + this.getClass().getName() + "@Method getSearchRecordCount ",
					ex);
			return 0;
		}
	}

	/**
	 * Gets the existed banner ids.
	 *
	 * @return the existed banner ids
	 */
	@Transactional
	public List<Integer> getExistedBannerIds() {
		logger.info(" @class" + this.getClass().getName() + "@Method getExistedBannerIds");
		try {
			return trbbannerDao.getExistedBannerIds();
		} catch (Exception ex) {
			logger.error("Error  occurred  @class" + this.getClass().getName() + "@Method getExistedBannerIds", ex);
			logger.error("Error  occurred  @class" + this.getClass().getName(), ex);
		}
		return new ArrayList<Integer>();
	}

	@Override
	public Map<String,Integer> getBannerCountByStatus() {
		logger.info("@class" + this.getClass().getName() + "@Method getBannerCountByStatus");
		return trbbannerDao.getBannerCountByStatus();
	}

	@Override
	@Transactional
	public List<TRBBanner> updateBannerContent(List<BannerContentWrapper> bannerContentWrappers) {
		logger.info(" @class" + this.getClass().getName() + "@Method updateBannerContent :" +bannerContentWrappers.toString() );
		List<TRBBanner> banners = new ArrayList<>();
		try {
			for (BannerContentWrapper bannerContentWrapper : bannerContentWrappers) {
				
				VelocityEngine ve = new VelocityEngine();
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
				ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		        ve.init();
		        VelocityContext context = new VelocityContext();
		        JSONObject jsonObj = new JSONObject(bannerContentWrapper.getBannerContent());
		        JSONArray arr = jsonObj.getJSONArray("customRow");
		        jsonObj.put("size",arr.length());
		        context.put(TRBUtils.BANNER_DATA,jsonObj);
		        //Template t = ve.getTemplate("tribeBanner.vm");
		        Template t = ve.getTemplate(TRBUtils.TRIBE_BANNER_VM);
		        StringWriter writer = new StringWriter();
		        t.merge( context, writer );
		        String bannerContent = writer.toString();
		        
				TRBBanner trbBanner = trbbannerDao.findByPk(bannerContentWrapper.getBannerId());
				if (trbBanner.getStartTime() != null && trbBanner.getEndTime() != null) {
					if (new Date().getTime() >= trbBanner.getStartTime().getTime()
							&& new Date().getTime() < trbBanner.getEndTime().getTime()) {
						logger.info("Bannerid : {}, enable:{}", trbBanner.getId(), true);
						trbBanner.setEnable(true);
					} else {
						logger.info("Bannerid : {}, enable:{}", trbBanner.getId(), false);
						trbBanner.setEnable(false);
					}
				} else {
					trbBanner.setEnable(true);
				}
				trbBanner.setBannerContent(bannerContent);
				trbBanner.setModifiedTime(new Date());
				banners.add(trbbannerDao.update(trbBanner));
			}
			
		} catch (Exception ex) {
			logger.error("Error  occurred  @class" + this.getClass().getName() + "@Method updateBannerContent", ex);
			logger.error("Error  occurred  @class" + this.getClass().getName(), ex);
		}
		return banners;
		
	}

	@Override
	@Transactional
	public void markExpiredBanners() {
		try {
		logger.info("@class" + this.getClass().getName() + "@Method markExpiredBanners");
		
		trbbannerDao.markExpiredBanners();
		} catch (Exception e) {
		      logger.error("Exception in markExpiredBanners {}",e);
		          
		    }
	}
}
