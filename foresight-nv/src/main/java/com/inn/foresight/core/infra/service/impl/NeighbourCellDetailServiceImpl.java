package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.exceptions.application.ValidationFailedException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.INeighbourCellDetailDao;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.model.NeighbourCellDetail;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.service.INeighbourCellDetailService;
import com.inn.foresight.core.infra.utils.InfraConstants;

/**
 * The Class NeighbourCellDetailServiceImpl.
 */
@Service("NeighbourCellDetailServiceImpl")
public class NeighbourCellDetailServiceImpl extends AbstractService<Integer, NeighbourCellDetail> 
		implements INeighbourCellDetailService{

	/** The logger. */
	private static Logger logger = LogManager.getLogger(NeighbourCellDetailServiceImpl.class);
	
	@Autowired
	private INeighbourCellDetailDao neighbourCellDetailDao;
	
	@Autowired
	private INetworkElementDao iNetworkElementDao;

	/**
	 * Sets the dao.
	 * @param dao the new dao
	 */
	@Autowired
	public void setDao(INeighbourCellDetailDao dao) {
		super.setDao(dao);
		this.neighbourCellDetailDao = dao;
	}
	
	@Override
	public List<NeighbourCellDetail> search(NeighbourCellDetail neighbourCellDetail) {
		logger.info("Finding neighbourCellDetail list by neighbourCellDetail:{}", neighbourCellDetail);
		return super.search(neighbourCellDetail);
	}

	@Override
	public NeighbourCellDetail findById(Integer primaryKey) {
		return super.findById(primaryKey);
	}

	@Override
	public List<NeighbourCellDetail> findAll() {
		try {
			return super.findAll();
		} catch (EmptyResultDataAccessException | NoResultException ex) {
			logger.error(ex.getMessage());
			throw new RestException(ex);
		}
	}

	@Override
	public NeighbourCellDetail create(NeighbourCellDetail neighbourCellDetail) {
		logger.info("Creating neighbourCellDetail by an neighbourCellDetail:{}", neighbourCellDetail);
		try {
			return super.create(neighbourCellDetail);
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			throw new ValidationFailedException(ex);

		} catch (ConstraintViolationException ex) {
			logger.error(ex.getMessage());
			throw new RestException(ex);
		}
	}

	@Override
	public void remove(NeighbourCellDetail anEntity) {
		logger.info("Removing neighbourCellDetail by an neighbourCellDetail :{}", anEntity);
		super.remove(anEntity);

	}

	@Override
	public void removeById(Integer primaryKey) {
		logger.info("Removing neighbourCellDetail by primaryKey:{}", primaryKey);
		try {
			super.removeById(primaryKey);
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			throw new ValidationFailedException(ex);
		} catch (ConstraintViolationException ex) {
			logger.error(ex.getMessage());
			throw new RestException(ex);
		}
	}
	
	@Override
	public Map<String, List<NeighbourCellDetail>> getNeighbourCellDetailsForSourceCells(List<String> cellName, Integer weekno){
		logger.info("Get neighbour cells list for source cells {}, weekNo {}",cellName, weekno);
		Map<String, List<NeighbourCellDetail>> neighbourCellMap = null;
		List<NeighbourCellDetail> neighbourCellList = neighbourCellDetailDao.getNeighbourCellDetailsForSourceCells(cellName, weekno);
		return getCellNamewiseNeigbhours(neighbourCellMap, neighbourCellList);
	}

	private Map<String, List<NeighbourCellDetail>> getCellNamewiseNeigbhours(Map<String, List<NeighbourCellDetail>> neighbourCellMap,
			List<NeighbourCellDetail> neighbourCellList) {
		if(neighbourCellList !=null && !neighbourCellList.isEmpty()) {
			neighbourCellMap = new HashMap<>();
			 for(NeighbourCellDetail neighbourCellDetail : neighbourCellList) {
				 if(neighbourCellMap.get(neighbourCellDetail.getCellName()) != null) {
					 neighbourCellMap.get(neighbourCellDetail.getCellName()).add(neighbourCellDetail);
				 }else {
					 List<NeighbourCellDetail> list = new ArrayList<> ();
					 list.add(neighbourCellDetail);
					 neighbourCellMap.put(neighbourCellDetail.getCellName(), list);
				 }
			 }
		 }else {
			 logger.warn("NeighbourCells not found for input cells");
		 }
		return neighbourCellMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getNeighbourCellDetails(String sourceSiteId, Integer sourceCellId, String sourceFrequencyBand,	String vendor, String domain) {
		List<Map> finalMap = new ArrayList<>();
		try {
			List<RANDetail> sourceDataList = iNetworkElementDao.getNetworkElementIdByNameAndCellNum(sourceSiteId, sourceCellId,sourceFrequencyBand, vendor, domain);
			List<Integer> networkElementList = new ArrayList<>();
			if (sourceDataList != null && !(sourceDataList.isEmpty())) {
				for (RANDetail sourceData : sourceDataList) {
					if(sourceData.getNetworkElement().getId() != null)
					networkElementList = neighbourCellDetailDao.getNeighbourCellDetailIdByNEId(sourceData.getNetworkElement().getId());
				    logger.info("neighbourid size: {}",networkElementList.size());
					if (networkElementList != null && !(networkElementList.isEmpty())) {
					List<RANDetail> neighbourCellDetailList = neighbourCellDetailDao.getNeighbourCellDetails(networkElementList,vendor,domain);
					Map map = null;
					Map sourceMap = null;
					Map neighbourMap = null;
					if (neighbourCellDetailList != null && !(neighbourCellDetailList.isEmpty())) {
						for (RANDetail neighbourCellDetail : neighbourCellDetailList) {
							try {
								map = new HashMap();
								sourceMap = new HashMap();
								neighbourMap = new HashMap();
								neighbourMap.put(InfraConstants.SITEID,neighbourCellDetail.getNetworkElement().getNetworkElement().getNeName());
								neighbourMap.put(InfraConstants.CNUM_UPPERCASE, neighbourCellDetail.getNetworkElement().getCellNum());
								neighbourMap.put(InfraConstants.PCI, neighbourCellDetail.getPci());
								neighbourMap.put(ForesightConstants.FREQUENCY_BAND,neighbourCellDetail.getNetworkElement().getNeFrequency());
								neighbourMap.put(InfraConstants.AZIMUTH_KEY, neighbourCellDetail.getAzimuth());
								neighbourMap.put(InfraConstants.NE_LATITUDE_KEY, neighbourCellDetail.getNetworkElement().getLatitude());
								neighbourMap.put(InfraConstants.NE_LONGITUDE_KEY, neighbourCellDetail.getNetworkElement().getLongitude());
								neighbourMap.put("SiteType", neighbourCellDetail.getNetworkElement().getNeType());
								neighbourMap.put("SiteStatus", neighbourCellDetail.getNetworkElement().getNeStatus());
								if (sourceData != null) {
									sourceMap.put(InfraConstants.SITEID,sourceData.getNetworkElement().getNetworkElement().getNeName());
									sourceMap.put(InfraConstants.CNUM_UPPERCASE, sourceData.getNetworkElement().getCellNum());
									sourceMap.put(InfraConstants.PCI, sourceData.getPci());
									sourceMap.put(ForesightConstants.FREQUENCY_BAND, sourceData.getNetworkElement().getNeFrequency());
									sourceMap.put(InfraConstants.AZIMUTH_KEY, sourceData.getAzimuth());
									sourceMap.put(InfraConstants.NE_LATITUDE_KEY, sourceData.getNetworkElement().getLatitude());
									sourceMap.put(InfraConstants.NE_LONGITUDE_KEY, sourceData.getNetworkElement().getLongitude());
								}
								map.put(InfraConstants.SOURCE, sourceMap);
								map.put(InfraConstants.NEIGHBOUR, neighbourMap);
								finalMap.add(map);
								map = null;
								sourceMap = null;
								neighbourMap = null;
							} catch (Exception exception) {
								logger.error("unable to get Map for Neighbour Cell. Exception: {}",exception.getMessage());
							}
						}
					}
				}
			}
			}
		} catch (Exception exception) {
			logger.error("unable to get Neghbour cell detail data for source cell . Exception: {}",exception.getMessage());
		}
		logger.info("finalMap size: {}",finalMap.size());
		return finalMap;
	}
}
