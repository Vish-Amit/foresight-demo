package com.inn.foresight.module.nv.report.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.Utils;

public class JsonMapParser<K, V> {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(JsonMapParser.class);

	public JsonMapParser() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Map<K, V> convertJsonToMap(String json) {
		if (!StringUtils.isBlank(json)) {
			try {
				return new ObjectMapper().readValue(json, HashMap.class);
			} catch (JsonParseException e) {
				logger.error("JsonParseException inside convertJsonToMap method in JsonMapParser {} ",
						Utils.getStackTrace(e));
			} catch (JsonMappingException e) {
				logger.error("JsonMappingException inside convertJsonToMap method in JsonMapParser {} ",
						Utils.getStackTrace(e));
			} catch (IOException e) {
				logger.error("IOException inside convertJsonToMap method in JsonMapParser {} ", Utils.getStackTrace(e));
			}
		}
		return new HashMap<>();
	}

}