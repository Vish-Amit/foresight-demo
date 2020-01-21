package com.inn.foresight.core.infra.service;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.MNEAudit;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.product.um.user.model.User;

public interface MNEAuditService extends IGenericService<Long, MNEAudit> {
	public void createMNEAuditParameter(NetworkElement networkElement,String remark,User user);

	public void createMNEAuditParameterForStation(Object networkElement, String remark, User user);
}
