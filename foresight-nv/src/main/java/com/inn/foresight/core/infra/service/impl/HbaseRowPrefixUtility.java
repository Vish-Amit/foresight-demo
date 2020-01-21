package com.inn.foresight.core.infra.service.impl;

import static com.inn.foresight.core.infra.utils.InfraConstants.INVALID_GEOTYPE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.model.HbaseRowPrefix;

@Deprecated
public class HbaseRowPrefixUtility {

    private static Logger logger = LogManager.getLogger(HbaseRowPrefixUtility.class);

    private static Map<String, List<String>> rowPrefixMapL1;
    private static Map<String, List<String>> rowPrefixMapL2;
    private static Map<String, List<String>> rowPrefixMapL3;
    private static Map<String, List<String>> rowPrefixMapDomain;
    private static Map<String, List<String>> rowPrefixMapDomL1;
    private static Map<String, List<String>> rowPrefixMapDomL2;
    private static Map<String, List<String>> rowPrefixMapDomL3;
    private static Map<String, List<String>> rowPrefixMapCoreGeo;
	private static Map<String, List<String>> rowPrefixMapDomCoreGeo;

    private static Map<String, Map<String, List<String>>> rowPrefixMapDomWiseL3;
    private static Map<String, Map<String, List<String>>> rowPrefixMapDomWiseL2;
    private static Map<String, Map<String, List<String>>> rowPrefixMapDomWiseL1;

    public static String KEY_SEPARATOR = "##";
    public static String VALUE_SEPARATOR = "$$";
    public static String DATA_SEPARATER = "___";
    public static List<String> codes = null;

    public enum GeoType {
        L1, L2, L3, L0, COREGEOGRAPHY
    }

    public static void refreshHbaseRowPrefix(List<HbaseRowPrefix> hbaseRowPrefixData) {
        try {
            rowPrefixMapL1 = new HashMap<>();
            rowPrefixMapL2 = new HashMap<>();
            rowPrefixMapL3 = new HashMap<>();
            rowPrefixMapDomain = new HashMap<>();
            rowPrefixMapDomL1 = new HashMap<>();
            rowPrefixMapDomL2 = new HashMap<>();
            rowPrefixMapDomL3 = new HashMap<>();
            rowPrefixMapCoreGeo = new HashMap<>();
			rowPrefixMapDomCoreGeo = new HashMap<>();
            rowPrefixMapDomWiseL3 = new HashMap<>();
            rowPrefixMapDomWiseL2 = new HashMap<>();
            rowPrefixMapDomWiseL1 = new HashMap<>();

            for (HbaseRowPrefix hbaseRowPrefix : hbaseRowPrefixData) {
                if (hbaseRowPrefix != null) {
                    initialiseDataIntoMap(rowPrefixMapDomain, hbaseRowPrefix, hbaseRowPrefix.getDomain(),
                            hbaseRowPrefix.getDomain());

                    initialiseDataIntoMap(rowPrefixMapDomain, hbaseRowPrefix,
                            hbaseRowPrefix.getDomain() + KEY_SEPARATOR + hbaseRowPrefix.getVendor(),
                            hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor());

                    initialiseDataIntoMap(rowPrefixMapDomL1, hbaseRowPrefix,
                            hbaseRowPrefix.getDomain() + KEY_SEPARATOR + hbaseRowPrefix.getL1(),
                            hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor());

                    initialiseDataIntoMap(rowPrefixMapDomL1, hbaseRowPrefix,
                            hbaseRowPrefix.getDomain() + KEY_SEPARATOR + hbaseRowPrefix.getVendor() + KEY_SEPARATOR
                                    + hbaseRowPrefix.getL1(),
                            hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor());

                    initialiseDataIntoMap(rowPrefixMapL1, hbaseRowPrefix, hbaseRowPrefix.getL1(),
                            hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor());

                    initialiseDataIntoMap(rowPrefixMapL2, hbaseRowPrefix, hbaseRowPrefix.getL2(),
                            hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor() + VALUE_SEPARATOR
                                    + hbaseRowPrefix.getL1());

                    initialiseDataIntoMap(rowPrefixMapDomL2, hbaseRowPrefix,
                            hbaseRowPrefix.getDomain() + KEY_SEPARATOR + hbaseRowPrefix.getVendor() + KEY_SEPARATOR
                                    + hbaseRowPrefix.getL2(),
                            hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor() + VALUE_SEPARATOR
                                    + hbaseRowPrefix.getL1());

                    initialiseDataIntoMap(rowPrefixMapDomL2, hbaseRowPrefix,
                            hbaseRowPrefix.getDomain() + KEY_SEPARATOR + hbaseRowPrefix.getL2(),
                            hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor() + VALUE_SEPARATOR
                                    + hbaseRowPrefix.getL1());

                    initialiseDataIntoMap(rowPrefixMapL3, hbaseRowPrefix, hbaseRowPrefix.getL3(),
                            hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor() + VALUE_SEPARATOR
                                    + hbaseRowPrefix.getL1());

                    initialiseDataIntoMap(rowPrefixMapDomL3, hbaseRowPrefix,
                            hbaseRowPrefix.getDomain() + KEY_SEPARATOR + hbaseRowPrefix.getL3(),
                            hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor() + VALUE_SEPARATOR
                                    + hbaseRowPrefix.getL1() + VALUE_SEPARATOR + hbaseRowPrefix.getL2());

                    initialiseDataIntoMap(rowPrefixMapDomL3, hbaseRowPrefix,
                            hbaseRowPrefix.getDomain() + KEY_SEPARATOR + hbaseRowPrefix.getVendor() + KEY_SEPARATOR
                                    + hbaseRowPrefix.getL3(),
                            hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor() + VALUE_SEPARATOR
                                    + hbaseRowPrefix.getL1() + VALUE_SEPARATOR + hbaseRowPrefix.getL2());
                    
                    if(hbaseRowPrefix.getOtherGeography() != null) {
						initialiseDataIntoMap(rowPrefixMapCoreGeo, hbaseRowPrefix, hbaseRowPrefix.getOtherGeography().getName(),
								hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor());
						
						initialiseDataIntoMap(rowPrefixMapDomCoreGeo, hbaseRowPrefix,
								hbaseRowPrefix.getDomain() + KEY_SEPARATOR + hbaseRowPrefix.getOtherGeography().getName(),
								hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor());
						
						initialiseDataIntoMap(rowPrefixMapDomCoreGeo, hbaseRowPrefix,
								hbaseRowPrefix.getDomain() + KEY_SEPARATOR + hbaseRowPrefix.getVendor() + KEY_SEPARATOR
								+ hbaseRowPrefix.getOtherGeography().getName(),
								hbaseRowPrefix.getDomain() + VALUE_SEPARATOR + hbaseRowPrefix.getVendor());
					}

                }
            }
            updateRowPrefixMaps(rowPrefixMapL1);
            updateRowPrefixMaps(rowPrefixMapL2);
            updateRowPrefixMaps(rowPrefixMapL3);
            updateRowPrefixMaps(rowPrefixMapDomain);
            updateRowPrefixMaps(rowPrefixMapDomL1);
            updateRowPrefixMaps(rowPrefixMapDomL2);
            updateRowPrefixMaps(rowPrefixMapDomL3);
            updateRowPrefixMaps(rowPrefixMapCoreGeo);
			updateRowPrefixMaps(rowPrefixMapDomCoreGeo);

            initialiseDataIntoMap(rowPrefixMapDomL1, rowPrefixMapDomWiseL1);
            initialiseDataIntoMap(rowPrefixMapDomL2, rowPrefixMapDomWiseL2);
            initialiseDataIntoMap(rowPrefixMapDomL3, rowPrefixMapDomWiseL3);
            logger.info("Sucessfully loaded HbaseRowPrefix.");
        } catch (Exception e) {
            logger.error("Exception in updating hbaseRowPrefix: {}", e);
        }
    }

    private static void initialiseDataIntoMap(Map<String, List<String>> rowPrefixMapDomL12,
            Map<String, Map<String, List<String>>> rowPrefixMapDomWiseL12) {
        try {
            for (Entry<String, List<String>> ent : rowPrefixMapDomL12.entrySet()) {
                String key = ent.getKey();
                if (key != null) {
                    if (key.contains(KEY_SEPARATOR) && key.split(KEY_SEPARATOR).length > 2) {
                        String[] keyArray = key.split(KEY_SEPARATOR);
                        String newKey = keyArray[0] + KEY_SEPARATOR + keyArray[1];
                        if (rowPrefixMapDomWiseL12.get(newKey) != null) {
                            Map<String, List<String>> map = rowPrefixMapDomWiseL12.get(newKey);
                            map.put(keyArray[2], ent.getValue());
                            rowPrefixMapDomWiseL12.put(newKey, map);
                        } else {
                            Map<String, List<String>> map = new HashMap<>();
                            map.put(keyArray[2], ent.getValue());
                            rowPrefixMapDomWiseL12.put(newKey, map);
                        }

                    } else if (key.contains(KEY_SEPARATOR) && key.split(KEY_SEPARATOR).length > 1) {
                        String[] keyArray = key.split(KEY_SEPARATOR);
                        String newKey = keyArray[0];
                        if (rowPrefixMapDomWiseL12.get(newKey) != null) {
                            Map<String, List<String>> map = rowPrefixMapDomWiseL12.get(newKey);
                            map.put(keyArray[1], ent.getValue());
                            rowPrefixMapDomWiseL12.put(newKey, map);
                        } else {
                            Map<String, List<String>> map = new HashMap<>();
                            map.put(keyArray[1], ent.getValue());
                            rowPrefixMapDomWiseL12.put(newKey, map);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception while initialising domain wise geo map {}", Utils.getStackTrace(e));
        }
    }

    private static void updateRowPrefixMaps(Map<String, List<String>> hbaseRowPrefixMap)
            throws Exception {
        for (Entry<String, List<String>> ent : hbaseRowPrefixMap.entrySet()) {
            String key = ent.getKey();

            if (StringUtils.isEmpty(key))
                continue;

            List<String> list = new ArrayList<>();
            for (String value : ent.getValue()) {
                String result = StringUtils.substringAfter(value, DATA_SEPARATER);
                String values[] = result.split(DATA_SEPARATER);

                if (values.length <= 1)
                    continue;

                String nextChar = getNextCharSequence(Integer.parseInt(values[0]));
                String upperLimit = values[2];

                if (values[1].equalsIgnoreCase(upperLimit)) {
                    list.add(values[1]);
                } else {
                    if (nextChar != null) {
                        list.add(values[1] + Symbol.UNDERSCORE_STRING + nextChar);
                    } else {
                        list.add(values[1] + Symbol.UNDERSCORE_STRING + upperLimit);
                    }

                }
            }

            key = key.toUpperCase();
            hbaseRowPrefixMap.put(key, list);
        }
    }

    private static void initialiseDataIntoMap(Map<String, List<String>> map, HbaseRowPrefix hbaseRowPrefix, String key,
            String currentGeo) {
        if (key == null) {
            return;
        }
        key = key.toUpperCase();
        if (map.get(key) != null) {
            List<String> list = map.get(key);
            int index = list.size() - 1;
            String lastString = list.get(index);
            String lastgeo = lastString.split(DATA_SEPARATER)[0];

            int number = Integer.parseInt(lastString.split(DATA_SEPARATER)[1]);

            if (!lastgeo.equalsIgnoreCase(currentGeo)) {
                String Result = currentGeo + DATA_SEPARATER + hbaseRowPrefix.getNumericCode() + DATA_SEPARATER
                        + hbaseRowPrefix.getAlphaNumericCode() + DATA_SEPARATER + hbaseRowPrefix.getAlphaNumericCode();
                list.add(Result);
                map.put(key, list);
            } else {
                String temp = lastString;
                if (number < hbaseRowPrefix.getNumericCode()) {
                    String result = currentGeo + DATA_SEPARATER + hbaseRowPrefix.getNumericCode() + DATA_SEPARATER
                            + temp.split(DATA_SEPARATER)[2] + DATA_SEPARATER + hbaseRowPrefix.getAlphaNumericCode();
                    list.remove(lastString);
                    list.add(result);
                    map.put(key, list);
                } else {
                    String result = currentGeo + DATA_SEPARATER + hbaseRowPrefix.getNumericCode() + DATA_SEPARATER
                            + hbaseRowPrefix.getAlphaNumericCode() + DATA_SEPARATER + temp.split(DATA_SEPARATER)[3];
                    list.remove(lastString);
                    list.add(result);
                    map.put(key, list);
                }
            }

        } else {
            String Result = currentGeo + DATA_SEPARATER + hbaseRowPrefix.getNumericCode() + DATA_SEPARATER
                    + hbaseRowPrefix.getAlphaNumericCode() + DATA_SEPARATER + hbaseRowPrefix.getAlphaNumericCode();
            List<String> list = new ArrayList<>();
            list.add(Result);
            map.put(key, list);
        }
    }

    public static List<String> getPrefix(String domain, String vendor) {
        return rowPrefixMapDomain.get((domain + KEY_SEPARATOR + vendor).toUpperCase());
    }

    public static List<String> getPrefix(String domain, String vendor, GeoType geoType, String geoName) {
        if (geoType == null) {
            throw new RestException(INVALID_GEOTYPE);
        }

        String key = domain + KEY_SEPARATOR + vendor + KEY_SEPARATOR + geoName;
        key = key.toUpperCase();

        switch (geoType) {
            case L1 :
                return rowPrefixMapDomL1.get(key);
            case L2 :
                return rowPrefixMapDomL2.get(key);
            case COREGEOGRAPHY :
                return rowPrefixMapDomCoreGeo.get(key);
            default :
                return rowPrefixMapDomL3.get(key);
        }
    }

    public static Map<String, List<String>> getPrefix(String domain, String vendor, GeoType geoType) {
        if (geoType == null) {
            throw new RestException(INVALID_GEOTYPE);
        }

        String key = domain + KEY_SEPARATOR + vendor;
        key = key.toUpperCase();

        switch (geoType) {
            case L1 :
                return rowPrefixMapDomWiseL1.get(key);
            case L2 :
                return rowPrefixMapDomWiseL2.get(key);
            default :
                return rowPrefixMapDomWiseL3.get(key);
        }
    }

    private static String getNextCharSequence(int index) throws Exception {
        if (codes == null)
            codes = getNextCharSequenceList();

        if (codes.size() > index)
            return codes.get((index + 1));

        throw new Exception("Invalid index " + index + " for rowkey Prefix.");
    }

    private static List<String> getNextCharSequenceList() {
        char[] allowedChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
                'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                'y', 'z'};
        List<String> codes = new ArrayList<>();
		for (char c1 : allowedChar) {
			for (char c2 : allowedChar) {
				for (char c3 : allowedChar) {
					if (StringUtils.equalsIgnoreCase(ConfigUtils.getString("CLIENT", false), "RAKUTEN")) {
						for (char c4 : allowedChar) {
							codes.add(new String(new char[]{c1, c2, c3, c4}));
						}

					} else {
						codes.add(new String(new char[]{c1, c2, c3}));
					}

				}
			}
		}
        return codes;
    }

}
