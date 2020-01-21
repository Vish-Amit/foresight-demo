package com.inn.foresight.core.infra.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.IRANDetailDao;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.service.IRANDetailService;

@Service("RANDetailServiceImpl")
public class RANDetailServiceImpl extends AbstractService<Integer, RANDetail> implements IRANDetailService {

    private Logger logger = LogManager.getLogger(RANDetailServiceImpl.class);

    @Autowired
    private IRANDetailDao iranDetailDao;

    /**
     * Method to update RAN Detail CGI by ECGI
     * 
     * @param ecgi
     * @param rrhno
     */
    @Override
    public void updateRanDetailCgiByEcgi(String ecgi, String rrhno) {
        logger.info("Going to update RAN Detail by ecgi: {},rrh no: {}", ecgi, rrhno);
        try {
            if (ecgi != null && rrhno != null && !ecgi.isEmpty() && !rrhno.isEmpty()) {
                RANDetail ranDetailObj = iranDetailDao.searchByRrhSerialNo(rrhno);
                if (ranDetailObj != null) {
                    Integer ecgino = Integer.valueOf(StringUtils.substring(ecgi, ForesightConstants.FIVE));
                    logger.info("ecgi no: {}", ecgino);
                    ranDetailObj.setCgi(ecgino);
                    iranDetailDao.update(ranDetailObj);
                    logger.info("Updation Successfull for RRH Number: {} corresponding to ECGI Number: {}", rrhno,
                            ecgino);
                } else {
                    throw new RestException("No result found for given RRH number:{}", rrhno);
                }
            } else {
                throw new RestException("RRH number and ecgi can not be null ");
            }

        } catch (Exception e) {
            logger.error("Error while updating RAN Detail due to: {}", e.getMessage());
        }
    }

}
