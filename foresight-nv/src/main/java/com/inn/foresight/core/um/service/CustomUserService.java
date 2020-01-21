package com.inn.foresight.core.um.service;

import java.io.InputStream;
import java.util.Map;

public interface CustomUserService {

	Map<String, String> createBulkUser(InputStream inputStream,String filePath,String type);

	Map<String, String> enableDisableBulkUser(InputStream inputStream, String actionType,String filePath);

}
