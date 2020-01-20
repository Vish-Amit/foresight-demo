package com.inn.foresight.module.nv.reportgeneration.constants;

public class NVReportGenerationConstants {
	
	private NVReportGenerationConstants(){
		throw new IllegalStateException("ReportGenerationConstants Utility class");
	}

	public static final String CONTENT_TYPE="Content-Type";
	public static final String CONTENT_DISPOSITION="Content-Disposition";
	public static final String INSTANCE_ID="instanceId";
	public static final String DROPWIZARD_URL="DROPWIZARD_POST_NVACTIVEREPORTDATA_URL";
	public static final String CONTENT_TYPE_EXCEL="application/excel";
	public static final String CONTENT_DISPOSITION_NAME="Content-Disposition";
	public static final String EXCEPTION_ON_CONNECTION = "Unable to connect to the server!";
	public static final String NV_REPORT_HBASE_TABLE_NAME="NVReport";

	public static final String NV_REPORT_HBASE_COL_FAMILY = "r";

	public static final String NV_REPORT_NAME="reportName";

	public static final String NV_REPORT_FORMAT="reportFormat";

	public static final String NV_REPORT_CONTENT="value";

	public static final String NV_REPORT_RESULTID="resultid";

	public static final String NV_REPORT_RESULT_TYPE="type";

	public static final String NO_DATA_MESSAGE = "There is no data to show.";


	/** NamedQuery constants. */
	public static final String GET_ANALYTICS_REPOSITORY_DATA = "getAnalyticsRepositoryData";
	public static final String GET_REPORT_TEMPLATE_DATA ="getReportTemplateData";

	public static final int FILE_EXTENSION_INDEX = 1;
	public static final int INDEX_TW0_HUNDRED=200;
	public static final int INDEX_THREE = 3;
}
