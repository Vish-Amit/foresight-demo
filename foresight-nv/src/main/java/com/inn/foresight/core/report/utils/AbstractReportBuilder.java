package com.inn.foresight.core.report.utils;
import org.json.JSONObject;

public interface AbstractReportBuilder {
	void configureReport(JSONObject json);
	void execute();

}
