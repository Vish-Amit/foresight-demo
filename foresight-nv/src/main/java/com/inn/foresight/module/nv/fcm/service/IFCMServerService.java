package com.inn.foresight.module.nv.fcm.service;

import org.json.JSONObject;

import com.inn.commons.notification.PushNotificationException;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.OS;

/** The Interface IGCMServerService. */
public interface IFCMServerService {

    /**
     * Send notification.
     * @param os 
     *
     * @param regIdList the reg id list
     * @param mapData the map data
     * @return the string
     * @throws PushNotificationException the push notification exception
     */
    Boolean sendNotification(JSONObject dataPayload, OS os) throws PushNotificationException;

}