package com.inn.foresight.core.infra.service;

import com.inn.foresight.core.infra.wrapper.SiteConnectionPointWrapper;

public interface CustomNEVisualizationService {

	SiteConnectionPointWrapper getSiteConnectionPointDetails(String neName, String neType);
}
