package com.inn.foresight.core.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;

/**
 * The Class PropertyFilesMelder.
 */
public class PropertyFilesMelder {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(PropertyFilesMelder.class);

	/** The Constant UPDATE_EXISTING_FIELDS. */
	public static final boolean UPDATE_EXISTING_FIELDS = true;

	/** The Constant DO_NOT_UPDATE_EXISTING_PROPERTIES. */
	public static final boolean DO_NOT_UPDATE_EXISTING_PROPERTIES = false;

	/** The new count. */
	public static int newCount = 0;

	/** The blank count. */
	public static int blankCount = 0;

	/** The update. */
	public static int update = 0;

	/**
	 * Instantiates a new property files melder.
	 */
	public PropertyFilesMelder() {
		super();
	}

	/**
	 * Read property file.
	 *
	 * @param inputFile
	 *            the input file
	 * @return the hash map
	 */
	public static HashMap<String, String> readPropertyFile(File inputFile) {
		Properties prop = new Properties();
		InputStream input = null;
		HashMap<String, String> propertyMap = new HashMap<String, String>();
		try {
			input = FileUtils.openInputStream(inputFile);
			prop.load(input);
			Set<String> propertyNames = prop.stringPropertyNames();
			for (String Property : propertyNames) {
				propertyMap.put(Property, prop.getProperty(Property));
			}
		} catch (FileNotFoundException e) {
			logger.error(Utils.getStackTrace(e));
		} catch (IOException e) {
			logger.error(Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				logger.error(Utils.getStackTrace(e));
			}
		}
		return propertyMap;
	}

	/**
	 * Gets the newly added properties.
	 *
	 * @param oldFile
	 *            the old file
	 * @param newFile
	 *            the new file
	 * @return the newly added properties
	 */
	public static List<String> getNewlyAddedProperties(File oldFile, File newFile) {
		Map<String, String> oldMap = readPropertyFile(oldFile);
		Map<String, String> newMap = readPropertyFile(newFile);
		return getExtraKeys(oldMap, newMap);

	}

	/**
	 * Gets the extra keys.
	 *
	 * @param oldMap
	 *            the old map
	 * @param newMap
	 *            the new map
	 * @return the extra keys
	 */
	private static List<String> getExtraKeys(Map<String, String> oldMap, Map<String, String> newMap) {
		List<String> extraKeys = new ArrayList<String>();
		for (String newKey : getSortedList(newMap.keySet())) {
			if (!oldMap.containsKey(newKey)) {
				extraKeys.add(newKey);
			}
		}
		return extraKeys;
	}

	/**
	 * Gets the keys having no value.
	 *
	 * @param file
	 *            the file
	 * @return the keys having no value
	 */
	public static List<String> getKeysHavingNoValue(File file) {
		List<String> emptyProperties = new ArrayList<String>();
		Map<String, String> map = readPropertyFile(file);
		for (String key : map.keySet()) {
			if (!Utils.isValidString(map.get(key))) {
				emptyProperties.add(key);
			}
		}
		return emptyProperties;
	}

	/**
	 * Gets the keys having no value.
	 *
	 * @param file1
	 *            the file 1
	 * @param file2
	 *            the file 2
	 * @return the keys having no value
	 */
	public static List<String> getKeysHavingNoValue(File file1, File file2) {
		List<String> emptyProperties = getKeysHavingNoValue(file1);
		emptyProperties.addAll(getKeysHavingNoValue(file2));
		return emptyProperties;
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {
		File oldFile = new File("/home/ist/Downloads/configprop/config_app_prod.properties");
		File newFile = new File("/home/ist/Downloads/configprop/config_app_staging.properties");
		Map<String, String> newProperties = PropertyFilesMelder.getNewlyAddedPropertiesWithValue(oldFile, newFile);
		System.out.println(newProperties);
	}

	/**
	 * Gets the newly added properties with value.
	 *
	 * @param oldFile
	 *            the old file
	 * @param newFile
	 *            the new file
	 * @return the newly added properties with value
	 */
	public static Map<String, String> getNewlyAddedPropertiesWithValue(File oldFile, File newFile) {
		Map<String, String> oldMap = readPropertyFile(oldFile);
		Map<String, String> newMap = readPropertyFile(newFile);
		return getExtraKeysWithValue(oldMap, newMap);
	}

	/**
	 * Gets the extra keys with value.
	 *
	 * @param oldMap
	 *            the old map
	 * @param newMap
	 *            the new map
	 * @return the extra keys with value
	 */
	private static Map<String, String> getExtraKeysWithValue(Map<String, String> oldMap, Map<String, String> newMap) {

		Map<String, String> extraKeys = new HashMap<String, String>();
		for (String newKey : newMap.keySet()) {
			if (!oldMap.containsKey(newKey)) {
				extraKeys.put(newKey, newMap.get(newKey));
			}
		}
		return extraKeys;

	}

	/**
	 * Display new key with values.
	 *
	 * @param oldFile
	 *            the old file
	 * @param newFile
	 *            the new file
	 * @return the string
	 */
	public static String displayNewKeyWithValues(File oldFile, File newFile) {
		newCount = 0;
		StringBuilder builder = new StringBuilder(ForesightConstants.BLANK_STRING);
		try {
			Map<String, String> map = getNewlyAddedPropertiesWithValue(oldFile, newFile);
			if (map.size() == 0) {
				System.out.println("No new properties");
			}
			for (String key : map.keySet()) {
				String display = key + "=" + map.get(key);
				newCount++;
				builder.append("\n" + display);
			}
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}
		return builder.toString();
	}

	/**
	 * Gets the properties with defferent value.
	 *
	 * @param oldFile
	 *            the old file
	 * @param newFile
	 *            the new file
	 * @param isOnlyNew
	 *            the is only new
	 * @return the properties with defferent value
	 */
	private static Map<String, String> getPropertiesWithDefferentValue(File oldFile, File newFile, boolean isOnlyNew) {
		Map<String, String> oldMap = readPropertyFile(oldFile);
		Map<String, String> newMap = readPropertyFile(newFile);
		Map<String, String> extraKeys = new HashMap<String, String>();
		boolean found = false;
		for (String newKey : newMap.keySet()) {
			if (!oldMap.get(newKey).equals(newMap.get(newKey))) {
				found = true;
				String display = newKey + ": oldValue=" + oldMap.get(newKey) + " & newValue=" + newMap.get(newKey);
				if (isOnlyNew) {
					display = newKey + "=" + newMap.get(newKey);
					extraKeys.put(newKey, newMap.get(newKey));
					if (!Utils.isValidString(newMap.get(newKey))) {
						extraKeys.put(newKey, oldMap.get(newKey));
					}
				}
				System.out.println(display);
			}
		}
		if (!found)
			System.out.println("NO KEYS WITH DIFFERENT VALUES");
		return extraKeys;
	}

	/**
	 * Adds the new properties.
	 *
	 * @param inputFile
	 *            the input file
	 * @param propertyMap
	 *            the property map
	 */
	public static void addNewProperties(String inputFile, Map<String, String> propertyMap) {
		BufferedWriter bufferWritter = null;
		try {
			FileWriter fileWritter = new FileWriter(inputFile, true);
			bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.append("\n\n#-----------------------------NEW PROPERTIES ADDED ON: " + new Date());
			for (String key : propertyMap.keySet()) {
				bufferWritter.append("\n" + key + "=" + propertyMap.get(key));
			}
			bufferWritter.append("\n#-----------------------------NEW PROPERTIES ENDS HERE.");
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		} finally {
			try {
				bufferWritter.close();
			} catch (IOException e) {
				logger.error(Utils.getStackTrace(e));
			}
		}

	}

	/**
	 * Merge.
	 *
	 * @param resultFilePath
	 *            the result file path
	 * @param oldFile
	 *            the old file
	 * @param newFile
	 *            the new file
	 */
	public static void merge(String resultFilePath, File oldFile, File newFile) {
		if (popup("Going to merge files without doing nay changes on already existing properties\n"
				+ "The old properties on the Old file will contain Same values as they did.\n\n"
				+ "Are you sure want to merge(Y/N)?..")) {
			merge(resultFilePath, oldFile, newFile, DO_NOT_UPDATE_EXISTING_PROPERTIES);
		}
	}

	/**
	 * Merge.
	 *
	 * @param resultFilePath
	 *            the result file path
	 * @param oldFile
	 *            the old file
	 * @param newFile
	 *            the new file
	 * @param updateExisting
	 *            the update existing
	 */
	public static void merge(String resultFilePath, File oldFile, File newFile, boolean updateExisting) {
		BufferedWriter bufferWritter = null;
		try {
			Map<String, String> oldMap = readPropertyFile(oldFile);
			Map<String, String> differentValueMap = getExistingProprtyWithDifferentValue(oldFile, newFile);
			FileWriter fileWritter = new FileWriter(resultFilePath, true);
			bufferWritter = new BufferedWriter(fileWritter);
			System.out.println("Writing existing properties...");
			for (String key : oldMap.keySet()) {
				if (!differentValueMap.containsKey(key) && Utils.isValidString(oldMap.get(key))) {
					bufferWritter.append("\n" + key + "=" + oldMap.get(key));
				}
			}

			if (updateExisting) {
				if (popup(
						"Going to update existing propertis. The old properties on the Old file will be updated with new values.\n\n"
								+ "Are you sure want to update(Y/N)?..")) {
					bufferWritter.append("\n\n#---updated properties starts here: " + new Date());
					for (String key : differentValueMap.keySet()) {
						if (Utils.isValidString(differentValueMap.get(key))) {
							bufferWritter.append("\n" + key + "=" + differentValueMap.get(key));
							update++;
						}
					}
					bufferWritter.append("\n#---updated properties ends here.[total updated = " + update + "]");
					updateExisting = true;
				}
			} else {
				updateExisting = false;
				for (String key : differentValueMap.keySet()) {
					if (Utils.isValidString(oldMap.get(key))) {
						bufferWritter.append("\n" + key + "=" + oldMap.get(key));
					}
				}
			}

			System.out.println("Writing new properties...");
			bufferWritter.append("\n\n#---new properties starts here: " + new Date());
			Map<String, String> newMap = getNewlyAddedPropertiesWithValue(oldFile, newFile);

			for (String key : newMap.keySet()) {
				if (Utils.isValidString(newMap.get(key))) {
					bufferWritter.append("\n" + key + "=" + newMap.get(key));
					newCount++;
				}
			}
			bufferWritter.append("\n#---new properties ends here.[total new added = " + newCount + "]");

			System.out.println("Writing blank properties...");
			bufferWritter.append("\n\n#---blank properties starts here: " + new Date());
			Map<String, String> blankMap = new HashMap<String, String>();
			updateBlanKMap(blankMap, oldMap, differentValueMap, newMap, updateExisting);
			for (String key : blankMap.keySet()) {
				blankCount++;
				bufferWritter.append("\n" + key + "=" + blankMap.get(key));
			}
			bufferWritter.append("\n#---blank properties ends here.[total blank = " + blankCount + "]");
			bufferWritter.close();
			System.out.println("merged sucessfully!");
		} catch (FileNotFoundException e) {
			logger.error(Utils.getStackTrace(e));
		} catch (IOException e) {
			logger.error(Utils.getStackTrace(e));
		} finally {
			try {
				if (bufferWritter != null)
					bufferWritter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Update blan K map.
	 *
	 * @param blankMap
	 *            the blank map
	 * @param oldMap
	 *            the old map
	 * @param differentValueKeys
	 *            the different value keys
	 * @param newMap
	 *            the new map
	 * @param isUpdatedExisting
	 *            the is updated existing
	 * @return the map
	 */
	private static Map<String, String> updateBlanKMap(Map<String, String> blankMap, Map<String, String> oldMap,
			Map<String, String> differentValueKeys, Map<String, String> newMap, boolean isUpdatedExisting) {
		for (String key : oldMap.keySet()) {
			if (!Utils.isValidString(oldMap.get(key)) && !isUpdatedExisting) {
				blankMap.put(key, ForesightConstants.BLANK_STRING);
			}
			if (!Utils.isValidString(oldMap.get(key)) && !Utils.isValidString(differentValueKeys.get(key))) {
				blankMap.put(key, ForesightConstants.BLANK_STRING);
			}
		}

		for (String key : newMap.keySet()) {
			if (!Utils.isValidString(newMap.get(key))) {
				blankMap.put(key, ForesightConstants.BLANK_STRING);
			}
		}
		return blankMap;

	}

	/**
	 * Gets the existing proprty with different value.
	 *
	 * @param oldFile
	 *            the old file
	 * @param newFile
	 *            the new file
	 * @return the existing proprty with different value
	 */
	public static Map<String, String> getExistingProprtyWithDifferentValue(File oldFile, File newFile) {
		Map<String, String> oldMap = readPropertyFile(oldFile);
		Map<String, String> newMap = readPropertyFile(newFile);
		Map<String, String> extraKeys = new HashMap<String, String>();
		boolean found = false;
		for (String oldKey : oldMap.keySet()) {
			if (oldMap.get(oldKey) != null && !oldMap.get(oldKey).equals(newMap.get(oldKey))) {
				if (newMap.containsKey(oldKey)) {
					found = true;
					extraKeys.put(oldKey, newMap.get(oldKey));
					if (!Utils.isValidString(newMap.get(oldKey))) {
						extraKeys.put(oldKey, oldMap.get(oldKey));
					}
				}
			}
		}
		if (!found)
			System.out.println("NO KEYS WITH DIFFERENT VALUES");
		return extraKeys;
	}

	/**
	 * Popup.
	 *
	 * @param msg
	 *            the msg
	 * @return true, if successful
	 */
	public static boolean popup(String msg) {
		try {
			JFrame frame = new JFrame("PROPERTY MELDER:");
			int result = JOptionPane.showConfirmDialog(frame, msg, "Property file Melder", JOptionPane.YES_OPTION);
			frame.dispose();
			return result == JOptionPane.OK_OPTION;
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}
		return false;
	}

	/**
	 * Display properties with blank values.
	 *
	 * @param file
	 *            the file
	 * @return the string
	 */
	public static String displayPropertiesWithBlankValues(File file) {
		blankCount = 0;
		StringBuilder builder = new StringBuilder(ForesightConstants.BLANK_STRING);
		try {
			Map<String, String> map = readPropertyFile(file);
			if (map.size() == 0) {
				popup("No properties availablle in the file");
			}
			for (String key : getSortedList(map.keySet())) {
				if (!Utils.isValidString(map.get(key))) {
					String display = key + "=";
					blankCount++;
					builder.append("\n" + display);
				}

			}
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}
		return builder.toString();
	}

	/**
	 * Sort.
	 *
	 * @param file
	 *            the file
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String sort(File file) throws IOException {
		String path = file.getAbsolutePath();
		Map<String, String> map = readPropertyFile(file);
		List<String> sortedKeys = getSortedList(map.keySet());
		file.delete();
		FileWriter fileWritter = new FileWriter(path, true);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		for (String key : sortedKeys) {
			bufferWritter.append("\n" + key + "=" + map.get(key));
		}
		bufferWritter.close();
		return path;
	}

	/**
	 * Gets the sorted list.
	 *
	 * @param keySet
	 *            the key set
	 * @return the sorted list
	 */
	private static List<String> getSortedList(Set<String> keySet) {
		List<String> list = new ArrayList<String>();
		for (String s : keySet) {
			list.add(s);
		}
		Collections.sort(list);
		return list;
	}

}