package com.inn.foresight.module.nv.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.infra.dao.IFloorDataDao;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.module.nv.dao.SiteDetailDao;
import com.inn.foresight.module.nv.utils.SiteDetailConstant;

@Repository("SiteDetailDaoImpl")
public class SiteDetailDaoImpl extends HibernateGenericDao<Integer, NetworkElement> implements SiteDetailDao {

    /** The logger. */
    private Logger logger = LogManager.getLogger(SiteDetailDaoImpl.class);

    /** Construct NVProfileDataDaoImpl object. */
    public SiteDetailDaoImpl() {
        super(NetworkElement.class);
    }

    
    @Autowired
    IFloorDataDao floorDao;

    @Override
    @Transactional(readOnly = true)
    public Map<String, String> getNENameByCgi(Integer cgi) {
        logger.info("going to get site name for cgi: {}", cgi);

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<RANDetail> ranDetailRoot = query.from(RANDetail.class);
        Join<RANDetail, NetworkElement> networkElementJoin = ranDetailRoot.join("networkElement", JoinType.LEFT);
        Join<Object, Object> floorJoin = networkElementJoin.join("floor",JoinType.LEFT);
        Join<NetworkElement, NetworkElement> selfJoin = networkElementJoin.join("networkElement", JoinType.LEFT);
        Join<NetworkElement, NetworkElement> parentJoin = networkElementJoin.join("parentNE", JoinType.LEFT);

        query.multiselect(selfJoin.get("neName"), parentJoin.get("neName"),floorJoin.get("id"));
//        query.select(selfJoin.get("neName"));
        query.where(builder.equal(ranDetailRoot.get("cgi"), cgi));
        List<Tuple> tuple = getEntityManager().createQuery(query).getResultList();

        if (CollectionUtils.isNotEmpty(tuple)) {
			Map<String, String> responseMap = new HashMap<>();
			responseMap.put("siteName", (String) tuple	.get(0)
														.get(0));
			responseMap.put("parentName", (String) tuple.get(0)
														.get(1));
			
			Integer floorId = (Integer) tuple.get(0).get(2);
			
			if(floorId!=null) {
			Floor floor = floorDao.findByPk(floorId);
			responseMap.put(SiteDetailConstant.FLOOR_NAME, floor.getFloorName());
			responseMap.put(SiteDetailConstant.BUILDING_NAME, floor.getWing().getBuilding().getBuildingName());
			
			}
			logger.info("site name : {}", responseMap);
			return responseMap;
		}
        
        return null;
    }
    
    @Override
	public List<Object[]> getTIReadySites(Long modificationTime){
    	if(modificationTime!=null) {
    	String query="select * from (select coalesce(at.siteid,ne.nename) as siteName,coalesce(ne.latitude,at.latitude)  as lat,coalesce(ne.longitude,at.longitude) as lon,CAST(unix_timestamp(at.modificationtime)*1000 as char) as modificationTime,ne.nestatus as neStatus,tiflag from AtollCellDetail at LEFT JOIN  (select * from NetworkElement where deleted=0)  ne on at.siteid=ne.nename where unix_timestamp(at.modificationtime)*1000 > ";
    	List<Object[]> resultList = getEntityManager().createNativeQuery(query+modificationTime+ " group by siteName ) as t1 where (neStatus is null or neStatus='PLANNED')").getResultList();
    	return resultList;
    	}
    	return null;
    }
}
