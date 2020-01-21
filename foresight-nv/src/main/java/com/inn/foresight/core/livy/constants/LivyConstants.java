package com.inn.foresight.core.livy.constants;

/**
 * The Class LivyConstants.
 * 
 * @author Zafar
 */
public class LivyConstants {

	/**
	 * Instantiates a new livy constants.
	 */
	private LivyConstants() {
		super();
	}

	/** The Constant ID. */
	public static final String ID = "id";
	
	/** The Constant STATE. */
	public static final String STATE = "state";
	
	/** The Constant OUTPUT. */
	public static final String OUTPUT = "output";
	
	/** The Constant USER. */
	public static final String USER = "user";
	
	/** The Constant PASSWORD. */
	public static final String KEY_PASSWORD = "password";
	
	/** The Constant CONNECTION. */
	public static final String CONNECTION = "connection";
	
	/** The Constant DRIVER. */
	public static final String DRIVER = "driver";
	
	/** The Constant DATAFRAME. */
	public static final String DATAFRAME = "dataframe";
	
	/** The Constant CALL_TYPE. */
	public static final String CALL_TYPE = "type";
	
	/** The Constant HBASE_TEMPLATE. */
	public static final String HBASE_TEMPLATE = "hbase";
	
	/** The Constant JDBC_TEMPLATE. */
	public static final String JDBC_TEMPLATE = "jdbc";
	
	/** The Constant SPARK_SQL_TEMPLATE. */
	public static final String SPARK_SQL_TEMPLATE = "sql";
	
	/** The Constant PARQUET_TEMPLATE. */
	public static final String PARQUET_TEMPLATE = "parquet";
	
	/** The Constant CELLSTARTTIME. */
	public static final String CELLSTARTTIME = "cellStartTime";
	
	/** The Constant CELLENDTIME. */
	public static final String CELLENDTIME = "cellEndTime";
	
	/** The Constant MILLISTOADD. */
	public static final String MILLISTOADD = "millisToAdd";
	
	/** The Constant LASTUPDATIONDATE. */
	public static final String LASTUPDATIONDATE = "lastUpdationDate";
	
	/** The Constant SOMETHING_WENT_WRONG_MESSAGE. */
	public static final String SOMETHING_WENT_WRONG_MESSAGE ="Something went wrong";
	
	/** The Constant INVALID_DATA_SOURCE_MESSAGE. */
	public static final String INVALID_DATA_SOURCE_MESSAGE  = "invalid data source";
	
	/** The Constant IP. */
	public static final String IP = "ip";
	
	/** The Constant NO_LIVE_SESSION_MESSAGE. */
	public static final String NO_LIVE_SESSION_MESSAGE= "No live session server found";
	
	/** The Constant NAME. */
	public static final String NAME = "name";
	
	/** The Constant FORWARD_SLASH. */
	public static final String  FORWARD_SLASH = "/";
	
	/** The Constant URL. */
	public static final String URL ="url";
	
	/** The Constant DBTABLE. */
	public static final String DBTABLE = "dbtable";
	
	/** The Constant DOT. */
	public static final String DOT = ".";
	
	/** The Constant TYPE. */
	public static final String TYPE = "type";
	
	/** The Constant PARQUETDATAPATH. */
	public static final String PARQUETDATAPATH = "parquetDataPath";
	
	/** The Constant SOURCENAME. */
	public static final String SOURCENAME= "sourceName";
	
	/** The Constant CACHING. */
	public static final String  CACHING ="caching";
	
	/** The Constant APPNAME. */
	public static final String  APPNAME = "appName";
	
	/** The Constant QUERY. */
	public static final String QUERY = "query";
	
	/** The Constant TEMPTABLE. */
	public static final String TEMPTABLE = "tempTable";
	
	/** The Constant SHOW. */
	public static final String SHOW = "show";
	
	/** The Constant SOURCE. */
	public static final String SOURCE = "source";
	
	/** The Constant ATRATE_SYMBOL. */
	public static final String ATRATE_SYMBOL = "@";
	
	/** The Constant COMMA. */
	public static final String COMMA = ",";
	
	/** The timerange syntax. */
	public static final String TIME_RANGE_SYNTAX = "Map(HBaseRelation.MIN_STAMP -> \\\"startTS\\\", HBaseRelation.MAX_STAMP -> \\\"endTS\\\")";
	
	/** The create temp view syntax. */
	public static final String CREATE_TEMPVIEW_SYNTAX = ".createOrReplaceTempView(\\\"viewName\\\");";
	
	/** The spark sql syntex. */
	public static final String SPARK_SQL_SYNTEX = "val dataframe = spark.sql(\\\"query\\\");";
	
	/** The add option syntax. */
	public static final String ADD_OPTION_SYNTAX = "option(\\\"param\\\",\\\"value\\\")";
	
	/** The read jdbc syntax. */
	public static final String READ_JDBC_SYNTAX = "val dataframe = spark.read.format(\\\"jdbc\\\")";
	
	/** The read parquet syntax. */
	public static final String READ_PARQUET_SYNTAX = "val dataframe = spark.read.parquet(\\\"source\\\");"; 
	
	/** The add catalog def syntax. */
	public static final String ADD_CATALOG_DEF_SYNTAX = "def varName = tableCatalog";
	
	/** The load catalog syntax. */
	public static final String LOAD_CATALOG_SYNTAX = "val dataframe = withCatalog(catalogParams);";
	
	/** The caching syntax. */
	public static final String CACHING_SYNTAX = ".cache();";
	
	/** The load datasource syntax. */
	public static final String LOAD_DATA_SOURCE_SYNTAX = ".load();";
	
	/** The pagination syntax. */
	public static final String PAGINATION_SYNTAX = "val dataframeWithLimit = dataframe.limit(limitVal); val dataframeWithPaging = dataframe.except(dataframeWithLimit);";

        /** The Constant SHC_TEMPLATE. */
        public static final String SHC_TEMPLATE= "import org.apache.spark.sql.execution.QueryExecution;import org.apache.spark.sql.execution.datasources.hbase._;import org.apache.spark.sql.{DataFrame, SparkSession};import org.apache.spark.sql.Row;val sqlContext = spark.sqlContext;import sqlContext.implicits._;sc.addFile(\\\"/usr/hdp/current/hbase-client/conf/hbase-site.xml\\\");val hbaseSitexmlPath =\\\"/usr/hdp/current/hbase-client/conf/hbase-site.xml\\\";def withCatalog(cat: String, options: Map[String, String] = Map.empty): DataFrame ={sqlContext.read.options(Map(HBaseTableCatalog.tableCatalog->cat,HBaseRelation.HBASE_CONFIGFILE->hbaseSitexmlPath) ++ options).format(\\\"org.apache.spark.sql.execution.datasources.hbase\\\").load()};";

	/** The Constant FS_PARAM_NAME. */
	public static final String FS_PARAM_NAME = "fs.defaultFS";
	
	/** The Constant CORE_SITE_XML. */
	public static final String CORE_SITE_XML = "core-site.xml";
	
	/** The Constant HDFS_SITE_XML. */
	public static final String HDFS_SITE_XML = "hdfs-site.xml";
	
	/** The Constant FORMAT_DDMMYY. */
	public static final String FORMAT_DDMMYY ="ddMMyy";
}