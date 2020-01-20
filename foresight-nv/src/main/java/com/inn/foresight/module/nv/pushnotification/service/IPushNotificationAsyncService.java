package com.inn.foresight.module.nv.pushnotification.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.inn.foresight.module.nv.pushnotification.model.PushNotification;

public interface IPushNotificationAsyncService {
	
	@Async("processExecutor")
	void sendNotificationAndUpdateStatus(PushNotification pushNotification);
	
	@Async("processExecutor")
	void sendNotificationsAndUpdateStatus(List<PushNotification> list);
	
	
	

}
