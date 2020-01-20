package com.inn.foresight.module.nv.report.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;  // JDK 1.8 only - older versions may need to use Apache Commons or similar.

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class UrlSigner {
	
	private static byte[] key;

	public static String getfinalUrlForStaticMap(String inputUrl, String inputKey)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
		URL url = new URL(inputUrl);
		UrlSigner signer = new UrlSigner(inputKey);
		String request = signer.signRequest(url.getPath(), url.getQuery());
		return url.getProtocol() + "://" + url.getHost() + request;
	}

	public UrlSigner(String keyString) {
		keyString = keyString.replace('-', '+');
		keyString = keyString.replace('_', '/');
		this.key = Base64.getDecoder().decode(keyString);
	}

	public String signRequest(String path, String query) throws NoSuchAlgorithmException,
	InvalidKeyException {
		String resource = path + '?' + query;
		SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(sha1Key);
		byte[] sigBytes = mac.doFinal(resource.getBytes());
		String signature = Base64.getEncoder().encodeToString(sigBytes);
		signature = signature.replace('+', '-');
		signature = signature.replace('/', '_');
		return resource + "&signature=" + signature;
	}

}