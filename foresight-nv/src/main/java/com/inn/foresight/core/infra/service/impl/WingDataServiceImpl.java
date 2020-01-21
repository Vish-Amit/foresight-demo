package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.dao.IWingDataDao;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.infra.model.Wing;
import com.inn.foresight.core.infra.service.IBuildingDataService;
import com.inn.foresight.core.infra.service.IFloorDataService;
import com.inn.foresight.core.infra.service.IWingDataService;
import com.inn.foresight.core.infra.utils.InBuildingUtils;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.core.infra.wrapper.WingWrapper;

/** The Class WingDataServiceImpl. */
@Service("WingDataServiceImpl")
public class WingDataServiceImpl extends InBuildingConstants implements IWingDataService {
	/** The logger. */
	private Logger logger = LogManager.getLogger(WingDataServiceImpl.class);

	/** The i wing data dao. */
	@Autowired
	IWingDataDao iWingDataDao;

	/** The i building data dao. */
	@Autowired
	IBuildingDataService iBuildingDataService;

	/** The i floor data service. */
	@Autowired
	IFloorDataService iFloorDataService;

	/**
	 * Creates the wing.
	 *
	 * @param wingData
	 *            the wing data
	 * @return the wing
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public Wing createWing(Wing wingData) {

		try {
			return iWingDataDao.createWing(wingData);
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Find by pk.
	 *
	 * @param id
	 *            the id
	 * @return the wing
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public Wing findByPk(Integer id) {
		try {
			return iWingDataDao.findByPk(id);
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Updates the wing.
	 *
	 * @param wingData
	 *            the wing data
	 * @return the wing
	 * @throws RestException
	 *             the rest exception
	 */

	@Override
	public Wing updateWing(Wing wingData) {
		try {
			return iWingDataDao.updateWing(wingData);
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Delete wings.
	 *
	 * @param wingWrappers
	 *            the wing wrappers
	 */
	@Override
	public void deleteWings(List<WingWrapper> wingWrappers) {
		for (WingWrapper wingWrapper : wingWrappers) {
			try {
				Wing wing = findByPk(wingWrapper.getWingId());
				wing.setIsDeleted(true);
				updateWing(wing);
				iFloorDataService.deleteFloors(wingWrapper.getFloorList());
			} catch (RestException e) {
				logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * Creates the wing list.
	 *
	 * @param buildingData the building data
	 * @param buildingWrapper            the building wrapper
	 * @throws RestException             the rest exception
	 */
	@Override
	public void createOrUpdateWingList(Building buildingData, BuildingWrapper buildingWrapper) {
		logger.info("Going to createWingList");
		if(buildingWrapper != null && buildingWrapper.getWingList() != null && !buildingWrapper.getWingList().isEmpty()){
		for (WingWrapper wingWrapper : buildingWrapper.getWingList()) {
				Wing wingData = null;
				if (wingWrapper.getWingId() != null) {
					wingData = updateWing(buildingData, wingWrapper);
				} else {
					wingData = createWing(buildingData, wingWrapper);
				}
				iFloorDataService.createOrUpdateFloorList(wingData, wingWrapper);
		}
		}
		buildingWrapper.setModificationTime(buildingData.getModificationTime().getTime());
		buildingWrapper.setBuildingId(buildingData.getId());
	}

	/**
	 * Creates the wing.
	 *
	 * @param buildingData the building data
	 * @param wingWrapper the wing wrapper
	 * @return the wing
	 * @throws RestException the rest exception
	 */
	private Wing createWing(Building buildingData, WingWrapper wingWrapper) {
		Wing wingData = new Wing();
		wingData = InBuildingUtils.getWingData(wingData, wingWrapper, buildingData);
		wingData = createWing(wingData);
		wingWrapper.setWingId(wingData.getId());
		return wingData;
	}

	/**
	 * Update wing.
	 *
	 * @param buildingData the building data
	 * @param wingWrapper the wing wrapper
	 * @return the wing
	 * @throws RestException the rest exception
	 */
	private Wing updateWing(Building buildingData, WingWrapper wingWrapper) {
		Wing wingData = findByPk(wingWrapper.getWingId());
		wingData = InBuildingUtils.getWingData(wingData, wingWrapper, buildingData);
		return updateWing(wingData);
	}

	/**
	 * Update wing list.
	 *
	 * @param buildingWrapper            the building wrapper
	 * @throws RestException the rest exception
	 */
	@Override
	public void updateWingList(BuildingWrapper buildingWrapper) {
		for (WingWrapper wingWrapper : buildingWrapper.getWingList()) {
			try {
				Wing wing = findByPk(wingWrapper.getWingId());
				updateWing(InBuildingUtils.getWingData(wing, wingWrapper, null));
				iFloorDataService.updateFloorList(wingWrapper);
			} catch (RestException e) {
				logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * Gets the all wing wrapper for building.
	 *
	 * @param id            the id
	 * @return the all wing wrapper for building
	 */
	@Override
	public List<WingWrapper> getAllWingWrapperForBuilding(Integer id) {
		List<WingWrapper> wingWrapperList = new ArrayList<>();
		try {
			wingWrapperList = iWingDataDao.getAllWingForBuilding(iBuildingDataService.findByPk(id));
		} catch (DaoException e1) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e1));
		}
		for (WingWrapper wingWrapper : wingWrapperList) {
			wingWrapper.setFloorList(iFloorDataService.getAllFloorWrapperForWing(wingWrapper.getWingId()));
		}
		return wingWrapperList;
	}

}
