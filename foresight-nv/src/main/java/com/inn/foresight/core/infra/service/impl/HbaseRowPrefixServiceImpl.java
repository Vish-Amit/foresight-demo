package com.inn.foresight.core.infra.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.infra.dao.HbaseRowPrefixDao;
import com.inn.foresight.core.infra.model.HbaseRowPrefix;
import com.inn.foresight.core.infra.service.HbaseRowPrefixService;

@Deprecated
@Service("HbaseRowPrefixServiceImpl")
public class HbaseRowPrefixServiceImpl extends AbstractService<Integer, HbaseRowPrefix> implements HbaseRowPrefixService {

  private Logger logger = LogManager.getLogger(HbaseRowPrefixServiceImpl.class);

  @Autowired
  private HbaseRowPrefixDao hbaseRowPrefixDao;

  @Override
  public void refreshHbaseRowPrefix() {
    try {
      logger.info("Going to refresh HbaseRowPrefix ");
      List<HbaseRowPrefix> hbaseRowPrefixData = hbaseRowPrefixDao.getAllHbaseRowPrefixData();
      HbaseRowPrefixUtility.refreshHbaseRowPrefix(hbaseRowPrefixData);
      logger.info("End refresh HbaseRowPrefix ");
    } catch (Exception e) {
      logger.error("Exception in refreshHbaseRowPrefix {}",
          com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
    }
  }

}
