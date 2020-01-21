package com.inn.foresight.core.infra.service;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.HbaseRowPrefix;

@Deprecated
public interface HbaseRowPrefixService extends IGenericService<Integer, HbaseRowPrefix> {

  void refreshHbaseRowPrefix();

}
