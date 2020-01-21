package com.inn.foresight.core.infra.service;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.RANDetail;

public interface IRANDetailService extends IGenericService<Integer, RANDetail> {

    void updateRanDetailCgiByEcgi(String ecgi, String rrhno);

}
