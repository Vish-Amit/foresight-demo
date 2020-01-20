package com.inn.foresight.module.nv.fcm.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.notification.PushNotificationException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.fcm.service.IFCMServerService;
import com.inn.foresight.module.nv.pushnotification.constants.PushNotificationConstants;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.OS;

/**
 * @author ist
 */
@Service("FCMServerServcieImpl")
public class FCMServerServcieImpl implements IFCMServerService {

	private static Logger logger = LogManager.getLogger(FCMServerServcieImpl.class);

	/* private final static String FCM_REGISTRATION_KEY =
	 "APA91bE_8JbejQXHrKMWiq56DEU2E-WCSuCrwBnJYCadqS1wyCzmn4ttpDMU3Im-VyvbBpLF2odKkO12AxhUw2Q-bfKp1C1a3SXIprXOCSsC76VOAW3Ylu2XD-mD615BojHn3r98HdDh";
	 public static void main(String[] args) throws Exception {
	 JSONArray keysArray = new JSONArray();
	 keysArray.put(FCM_REGISTRATION_KEY);
	 JSONObject json = new JSONObject();
	 JSONObject info = new JSONObject();
	 info.put("message", "Rate your experience"); // Notification title
	 info.put("body", "Hello"); // Notification body
	 info.put("type", "message");
	 json.put("data", info);
	 json.put("registration_ids", keysArray);
	 System.out.println("json:" + json.toString());
	 new FCMServerServcieImpl().sendNotification(json,OS.ANDROID);
	 }*/
	/**
	 * Send notification.
	 *
	 * @param regIdList
	 *            the reg id list
	 * @param mapData
	 *            the map data
	 * @return the string
	 * @throws PushNotificationException
	 *             the push notification exception
	 */
	@Override
	public Boolean sendNotification(JSONObject dataPayload, OS osType) throws PushNotificationException {
		try {
			logger.info("Going to sendNotification ");
			
			
			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                   return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                	 if ((certs != null) && (certs.length == 1)) {
                		 certs[0].checkValidity();
                	 } 
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                	 if ((certs != null) && (certs.length == 1)) {
                		 certs[0].checkValidity();
                	} 
                }
                
            }
        };
 
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					if (Utils.hasValidValue(hostname) && session != null) {
						return true;
					}
					return false;
				}
        };
 
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        HttpURLConnection conn = null;
        
        URL url = new URL(ConfigUtils.getString(ConfigEnum.FCM_SERVER_HOST.getValue()));
		conn = (HttpURLConnection) url.openConnection();
		setConnectionParams(conn, osType);
		try(OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())){
		wr.write(dataPayload.toString());
		wr.flush();
		try (BufferedReader bR = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			String line = "";
			StringBuilder responseStrBuilder = new StringBuilder();
			while ((line = bR.readLine()) != null) {
				responseStrBuilder.append(line);
			}
			return returnMessage(conn, responseStrBuilder);
		}
		}			
		} catch (Exception e) {
			logger.error("Exception in sending notification:{} ", ExceptionUtils.getStackTrace(e));
			return false;
		}
	}  

	
	public static HttpResponse sendUnSecureHttpsPostRequest(String url, OS osType) throws Exception {
		HttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		
		
		if(OS.IOS.equals(osType)) {
			httpPost.setHeader(PushNotificationConstants.HEADER_KEY_AUTHORIZATION,
					"key=" + ConfigUtils.getString(NVConfigUtil.FCM_IOS_SERVER_KEY));
		}else {
			httpPost.setHeader(PushNotificationConstants.HEADER_KEY_AUTHORIZATION,
				"key=" + ConfigUtils.getString(ConfigEnum.FCM_SERVER_KEY.getValue()));
		}
		httpPost.setHeader(ForesightConstants.CONTENT_TYPE, ForesightConstants.CONTENT_APPLICATION_JSON);

		try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()){
			response = httpclient.execute(httpPost);
			return response;
		} catch (Exception e) {
			logger.error("Error while sending Request on: {},  Error: {}", url, ExceptionUtils.getStackTrace(e));
			throw e;
		}
		
	}
	
	private boolean returnMessage(HttpURLConnection conn, StringBuilder responseStrBuilder)
			throws  IOException, JSONException {
		JSONObject result = new JSONObject(responseStrBuilder.toString());
		if (conn.getResponseCode() == ForesightConstants.RESPONSE_200) {
			logger.info("Response from fcm:{} " , result);
			JsonElement element = JsonParser.parseString(result.toString());
			JsonObject jobj = element.getAsJsonObject();
			if (jobj.get((ForesightConstants.SUCCESS).toLowerCase())
					.getAsInt() > ForesightConstants.ZERO) {
				logger.info("Notification sent successfully");
				return true;
			}
		} else {
			logger.warn("Notification Failed: {}", result);
			return false;
		}
		return false;
	}
	private void closeConnection(HttpURLConnection conn) {
		if (conn != null) {
			conn.disconnect();
		}
	}

	private void setConnectionParams(HttpURLConnection conn, OS osType) throws ProtocolException {
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);

		conn.setRequestMethod(PushNotificationConstants.METHOD_POST);
		if(OS.IOS.equals(osType)) {
			conn.setRequestProperty(PushNotificationConstants.HEADER_KEY_AUTHORIZATION,
					"key=" + ConfigUtils.getString(NVConfigUtil.FCM_IOS_SERVER_KEY));
		}else {
		conn.setRequestProperty(PushNotificationConstants.HEADER_KEY_AUTHORIZATION,
				"key=" + ConfigUtils.getString(ConfigEnum.FCM_SERVER_KEY.getValue()));
		}
		conn.setRequestProperty(ForesightConstants.CONTENT_TYPE, ForesightConstants.CONTENT_APPLICATION_JSON);
	}

}