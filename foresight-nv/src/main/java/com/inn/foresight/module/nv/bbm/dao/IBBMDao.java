package com.inn.foresight.module.nv.bbm.dao;

import java.util.List;

import com.inn.foresight.module.nv.bbm.utils.wrapper.BBMDetailWrapper;

public interface IBBMDao {

	List<BBMDetailWrapper>  getBBMDetailByDeviceIdPrefix(String deviceIdPrefix, Long minTimeRange, Long maxTimeRange);

}
