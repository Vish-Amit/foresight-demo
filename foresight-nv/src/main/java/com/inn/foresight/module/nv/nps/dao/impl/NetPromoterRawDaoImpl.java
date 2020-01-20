package com.inn.foresight.module.nv.nps.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.nps.dao.INetPromoterRawDao;
import com.inn.foresight.module.nv.nps.model.NPSRawDetail;


/**
 * The Class NetPromoterRawDaoImpl.
 * 
 * @author innoeye
 */
@Repository("NetPromoterRawDaoImpl")
public class NetPromoterRawDaoImpl extends HibernateGenericDao<Integer, NPSRawDetail> implements INetPromoterRawDao {

	private Logger logger = LoggerFactory.getLogger(NetPromoterRawDaoImpl.class);
	/** Instantiates a new Net Promoter Raw dao impl. */
	public NetPromoterRawDaoImpl() {
		super(NPSRawDetail.class);
	}

	@Override
	public NPSRawDetail createNPSRawData(NPSRawDetail npswrapper) {
		return super.create(npswrapper);
	}

	@Override
	public List<Object[]> getNPSRawData(String date, String geographyL1, String geographyL2, String geographyL3,
			String geographyL4, List<String> operators) {
		List<Object[]> response = new ArrayList<>();
		try {
			logger.info("Inside Dao method to find NPS Records");
			StringBuilder queryNative=new StringBuilder("select operatorname, ROUND((sum(case when customertype='Promoter' then 1 ELSE 0 end) - sum(case when customertype='Detractor' then  1 ELSE 0 end)) * 100/count(*),2) as TC from (select gl1.geographyl1id_pk as gL1Id,gl2.geographyl2id_pk as gL2Id,gl3.geographyl3id_pk as gL3Id,gl4.geographyl4id_pk  as gL4Id,ingestdate,o.operator as operatorname,case when netpromoterrating > 8 then 'Promoter' when netpromoterrating > 6 AND netpromoterrating <= 8  then 'Passive' else 'Detractor' end as customertype from NPSRawDetail n,OperatorDetail o,GeographyL1 gl1,GeographyL2 gl2,GeographyL3 gl3,GeographyL4 gl4 where n.mcc=o.mcc AND n.mnc = o.mnc AND  gl1.geographyl1id_pk = gl2.geographyl1id_fk AND gl2.geographyl2id_pk = gl3.geographyl2id_fk AND gl3.geographyl3id_pk = gl4.geographyl3id_fk AND n.geographyl4id_fk = gl4.geographyl4id_pk ) as d ");
			createDynamicQuery(queryNative,geographyL1,geographyL2,geographyL3,geographyL4,date,operators);
			Query query = getEntityManager().createNativeQuery(queryNative.toString());
			response = query.getResultList();
			logger.info("Found the response {}",response);
			return response;
		} catch (Exception e) {
			logger.info("Found the exception {}",Utils.getStackTrace(e));
		}
		return null;
	}
	
	private void createDynamicQuery(StringBuilder queryNative, String geographyL1, String geographyL2, String geographyL3,
			String geographyL4, String sampleDate, List<String> operators) {
		logger.info("Going to create Dynamic Query for NPS");
		if (sampleDate != null && operators != null) {
			String operatorNames = "('".concat(String.join("','", operators).concat("')"));
			queryNative = queryNative.
					append("where ingestdate='" + sampleDate + "'").
					append(" and operatorname in " + operatorNames);
			if (Utils.hasValidValue(geographyL1)) {
				queryNative.append(" and gL1Id=".concat(geographyL1));
			}
			if (Utils.hasValidValue(geographyL2)) {
				queryNative.append(" and gL2Id=".concat(geographyL2));
			}
			if (Utils.hasValidValue(geographyL3)) {
				queryNative.append(" and gL3Id=".concat(geographyL3));
			}
			if (Utils.hasValidValue(geographyL4)) {
				queryNative.append(" and gL4Id=".concat(geographyL4));
			}
			queryNative.append(" group by operatorname");
			logger.info("Going to fire Query Query {}", queryNative);
		}
	}
}
