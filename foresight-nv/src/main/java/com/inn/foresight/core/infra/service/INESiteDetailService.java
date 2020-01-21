package com.inn.foresight.core.infra.service;

import java.util.Map;

public interface INESiteDetailService {
	Map<String, Double> getBBULatLongOfSite(String neName, String neType);

}
