package com.inn.foresight.module.nv.layer3.dao.impl;

//public class Layer3PresetDaoImpl {
//}


import java.util.List;

import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.dao.ILayer3PresetDao;
import com.inn.foresight.module.nv.layer3.model.Layer3Preset;;

/**
 * The Class Layer3PresetDaoImpl.
 *
 * @author innoeye
 */
@Repository("Layer3PresetDaoImpl")
public class Layer3PresetDaoImpl extends HibernateGenericDao<Integer, Layer3Preset> implements ILayer3PresetDao{

	
	/** The logger. */
	private Logger logger = LogManager.getLogger(Layer3PresetDaoImpl.class);

	/** Instantiates a new layer 3 preset dao impl. */
	public Layer3PresetDaoImpl() {
		super(Layer3Preset.class);
	}	
	
	
	/**
	 * Create  Layer3 Preset Data.
	 * 
	 * @param Layer3Preset
	 *              layer3PresetData
	 * @return the Layer3 Preset Data
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public Layer3Preset createLayer3Preset(Layer3Preset layer3PresetData) {
		try {
			return create(layer3PresetData);
		} catch (Exception e){
			logger.error("Exception while creating Layer3Presest Record:{}",Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}	
	}

	/**
	 * Gets the get All Layer3 Preset Data By user ID.
	 *
	 *
	 * @param userid
	 *              User Id
	 * @return the All Layer3 Preset Data List
	 * @throws DaoException
	 *              the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Layer3Preset> getLayer3PresetByUserId(Integer userid,String presetId) {
		try {
			logger.info("In getLayer3PresetByUserId {}",userid);
			Query  query = getEntityManager().createNamedQuery(NVLayer3Constants.GET_LAYER3PRESET_BY_USERID)
					.setParameter("userid",userid).setParameter("presetId", presetId);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception while getting Layer3Presest Record by useri_fk :{} ",Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}
	
	
	/**
	 * Gets the get All Layer3 Preset Data By Preset primary key.
	 *
	 * @param id
	 *           the Id
	 * @return the All Layer3 Preset Data List
	 * @throws DaoException
	 *              the dao exception
	 */
	@Override
	public String getLayer3PresetDataByPK(Integer id) {
		logger.info("In getLayer3PresetDataByPK {}",id);
		try {
			Query  	query = getEntityManager().createNamedQuery(NVLayer3Constants.GET_LAYER3VISUALIZATION_BY_ID)
					.setParameter("id",id);
			return  query.getSingleResult().toString();
		} catch (Exception e) {
			logger.error("Exception while getting Layer3Presest Record by id :{}",Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}

}

