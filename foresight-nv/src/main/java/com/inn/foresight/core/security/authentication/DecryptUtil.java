
package com.inn.foresight.core.security.authentication;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.core.generic.utils.Utils;

/**
 * The Class DecryptUtil.
 */
public class DecryptUtil {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(DecryptUtil.class);

	public static final String PADDING = "AES/CBC/PKCS5Padding";

	private DecryptUtil() {

	}

	/**
	 * Hex string to byte array.
	 *
	 * @param s the s
	 * @return the byte[]
	 */
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * Generate key from password with salt.
	 *
	 * @param password  the password
	 * @param saltBytes the salt bytes
	 * @return the secret key
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws InvalidKeySpecException  the invalid key spec exception
	 */
	public static SecretKey generateKeyFromPasswordWithSalt(String password, byte[] saltBytes)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes, 100, 128);

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		SecretKey secretKey = keyFactory.generateSecret(keySpec);

		return new SecretKeySpec(secretKey.getEncoded(), "AES");
	}

	/**
	 * Decrypt aes encrypt with salt and iv.
	 * 
	 * @param encryptedData the encrypted data
	 * @param key           the key
	 * @param salt          the salt
	 * @param iv            the iv
	 * @return the string
	 * @throws Exception the exception
	 */
	public static String decryptAESEncryptWithSaltAndIV(String encryptedData, String key, String salt, String iv) {

		logger.info(" inside decryptAESEncryptWithSaltAndIV");

		try {
			byte[] saltBytes = hexStringToByteArray(salt);
			byte[] ivBytes = hexStringToByteArray(iv);

			IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

			SecretKeySpec sKey = (SecretKeySpec) generateKeyFromPasswordWithSalt(key, saltBytes);

			Cipher c = Cipher.getInstance(PADDING);
			c.init(Cipher.DECRYPT_MODE, sKey, ivParameterSpec);
			byte[] decordedValue = Base64.decodeBase64(encryptedData.getBytes());
			byte[] decValue = c.doFinal(decordedValue);
			return new String(decValue);
		} catch (Exception e) {
			logger.error(" error while decrypt {} ", Utils.getStackTrace(e));
		}

		return null;
	}

	/**
	 * Genenate random key.
	 *
	 * @param keySize the key size
	 * @return the string
	 */
	public static String genenateRandomKey(int keySize) {
		byte[] bytes = new byte[keySize];
		new SecureRandom().nextBytes(bytes);
		return new String(Hex.encodeHex(bytes));
	}

	/**
	 * Encrypt plain text with salt and IV.
	 *
	 * @param plaintext  the plaintext
	 * @param passphrase the passphrase
	 * @param salt       the salt
	 * @param iv         the iv
	 * @return the string
	 * @throws Exception the exception
	 */
	public static String encryptPlainTextWithSaltAndIV(String plaintext, String passphrase, String salt, String iv)
			throws Exception {
		byte[] saltBytes = hexStringToByteArray(salt);
		byte[] ivBytes = hexStringToByteArray(iv);
		SecretKeySpec key = (SecretKeySpec) generateKeyFromPasswordWithSalt(passphrase, saltBytes);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
		byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));
		return new String(Base64.encodeBase64(encrypted));
	}

	/**
	 * Encrypt string.
	 *
	 * @param plainText the plain text
	 * @return the string
	 */
	public static String encryptString(String plainText) {
		logger.info("Going to encrypt string : {}", plainText);
		String encryptedValue = null;
		try {
			String iv = genenateRandomKey(16);
			String salt = genenateRandomKey(16);
			String passphrase = "productLogin/js/dist/loginLib.min.js";// ConfigUtils.getString(ConfigUtil.APP_ACCESS_URL);
			String encryptData = DecryptUtil.encryptPlainTextWithSaltAndIV(plainText, passphrase, salt, iv);
			encryptedValue = iv + salt + encryptData;
		} catch (Exception e) {
			logger.error("Error in encrypting string : {}", Utils.getStackTrace(e));
		}
		return encryptedValue;
	}

}
