package com.inn.foresight.core.infra.constants;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;

import com.inn.foresight.core.generic.utils.DisplayName;

/** The Class InBuildingConstants. */
public class InBuildingConstants {

	// Response Message
	/** The exception something went wrong. */
	public static final String EXCEPTION_SOMETHING_WENT_WRONG = "There is some problem in completing the request.";

	/** The invalid parameter json. */
	public static final String INVALID_PARAMETER_JSON = "{\"message\":\"Parameters given are invalid.\"}";
	public static final String FILENAME = "fileName";

	public static final String INVALID_BUILDING_STRUCTURE = "Invalid Building Structure.";
	
	/** The success json. */
	public static final String SUCCESS_JSON = "{\"message\":\"Success\"}";

	/** The failure json. */
	public static final String FAILURE_JSON = "{\"message\":\"Failure\"}";

	// Response Message

	/** The Constant GET_BUILDINGS_BY_CLUSTER. */
	public static final String GET_BUILDINGS_BY_CLUSTER = "getBuildingsByCluster";

	public static final String NO_RECORD_FOUND_LOGGER = "No record found in table for the given details.";
	// Query Constant

	/** The Constant GET_BUILDINGS_BY_CLUSTER. */
	public static final String GET_BUILDING_BY_ID = "getBuildingById";

	/** The Constant GET_ALL_WING_FOR_BUILDING. */
	public static final String GET_ALL_WING_FOR_BUILDING = "getAllWingForBuilding";

	/** The Constant GET_ALL_FLOOR_FOR_WING. */
	public static final String GET_ALL_FLOOR_FOR_WING = "getAllFloorForWing";

	/** The Constant GET_ALL_UNIT_FOR_FLOOR. */
	public static final String GET_ALL_UNIT_FOR_FLOOR = "getAllUnitForFloor";
	
	/** The Constant GET_BUILDINGS_BY_NAME. */
	public static final String GET_BUILDINGS_BY_NAME = "getBuildingsByName";

	/** The Constant GET_ALL_BUILDINGS. */
	public static final String GET_ALL_BUILDINGS = "getAllBuildings";
	
	public static final String GET_BUILDING_BY_NAME_AND_ADDRESS = "getBuildingsByNameAndAddress";
	

	// Query Constant

	/** The Constant USER_NOT_FOUND. */
	public static final String USER_NOT_FOUND = "User not found";

	/** The Constant CLUSTER_NOT_FOUND. */
	public static final String CLUSTER_NOT_FOUND = "Cluster not found";

	/** The Constant BUILDINGS_NOT_AVAILABLE. */
	public static final String BUILDINGS_NOT_AVAILABLE = "Buildings not available";

	/** The Constant BUILDINGS_NOT_AVAILABLE. */
	public static final String BUILDINGS_NOT_FOUND = "Building not found";

	/** The Constant WING_NOT_FOUND. */
	public static final String WING_NOT_FOUND = "Wing not found";

	/** The Constant BUILDINGS_NOT_AVAILABLE. */
	public static final String BUILDING_ID_CANNOT_NULL = "Building id can't be null";

	/** The Constant BUILDINGS_NOT_AVAILABLE. */
	public static final String BUILDING_ID_CANNOT_GREATER_THAN_ZERO = "Building id can't be greater than zero";

	/** The Constant ZOOM_LEVEL. */
	public static final Integer ZOOM_LEVEL = 8;

	/** The Constant GEOGRAPHY_L4_PLACE_HOLDER. */

	public static final String GEOGRAPHY_L4_PLACE_HOLDER = "geographyL4PlaceHolder";

	/** The Constant BUILDING_PLACE_HOLDER. */
	public static final String BUILDING_PLACE_HOLDER = "buildingPlaceHolder";

	/** The Constant WING_PLACE_HOLDER. */
	public static final String WING_PLACE_HOLDER = "wingPlaceHolder";

	/** The Constant FLOOR_PLACE_HOLDER. */
	public static final String FLOOR_PLACE_HOLDER = "floorPlaceHolder";

	/** The Constant EXCEPTION_LOGGER. */
	// Loggers
	public static final String EXCEPTION_LOGGER = "Getting Exception {}";

	/** The Constant UNIT_ALREADY_EXISTS. */
	public static final String UNIT_ALREADY_EXISTS = "Unit already exists";

	/** The Enum Status. */
	public enum Status {
		/** The pending. */
		CREATE,
		/** The completed. */
		UPDATE,
		/** The started. */
		NONE
	}

	/** The Enum BildingType. */
	public enum BuildingType implements DisplayName  {
		/** The Enterprise. */
	    Enterprise("Enterprise","Enterprise"),
		
		/** The Residential. */
	    Residential("Residential","Residential"),
		
	    MALL_SHOPPING_CENTER("Mall/Shopping Center","mall-shopping-center"),
		
	    AIRPORT("Airport","airport"),
		
		STADIUM("Stadium","stadium"),
		
		HOTEL("Hotel","hotel"),
		
		TAIN_STATION("Tain Station","tain-station"),
		
		GOVERNMENT("Government","government"),
		
		COMMERICAL_OFFICE("Commerical Office","commerical-office"),
		
		EDUCATIONAL_CENTER("Educational Center", "educational-center"),
		
		CONVENTIONAL_CENTER("Conventional Center","conventional-center"),
		
		SOHO("SOHO","SOHO"),
		
		HOSPITAL("Hospital","hospital"),
		
		TUNNEL("tunnel","tunnel"),
		
		VIP_BUILDING("VIP Building","VIP-building");

	    private String displayName;
	    
	    private String mappingName;

        private BuildingType(String displayName, String mappingName) {
            this.displayName = displayName;
            this.mappingName = mappingName;
        }

        @Override
        public String displayName() {
            return displayName;
        }

        public String mappingName() {
            return mappingName;
        }

        public static String getBuildingTypeByMappingName(String mappingName){
            
            for(BuildingType buildingType : values()){
                if(StringUtils.equalsIgnoreCase(buildingType.mappingName(), mappingName)){
                    return buildingType.name();
                }
            }
            throw new IllegalArgumentException("No enum found");
        }
	}

	/** The Enum UnitType. */
	public enum UnitType {

		/** The 1BHK. */
		_1BHK("1BHK"),
		/** The 2BHK. */
		_2BHK("2BHK"),
		/** The 3BHK. */
		_3BHK("3BHK"),
		/** The 4BHK. */
		_4BHK("4BHK"),
		/** The 5BHK. */
		_5BHK("5BHK"),
		/** The Unit. */
		UNIT("Unit");
		/** The value. */
		private final String value;

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Instantiates a new UnitType enum.
		 *
		 * @param value
		 *            the value
		 */
		private UnitType(String value) {
			this.value = value;
		}

	}
		/** The invalid parameter json. */
	
	/** The Constant NEAREST_BUILDING_COUNT. */
	public static final Integer NEAREST_BUILDING_COUNT = 6;

	/** The Constant NEAREST_BUILDING_RADIUS. */
	public static final Number NEAREST_BUILDING_RADIUS = 100;

	/** Hbase table constants. */
	
	/** The Constant GET_BUILDINGS_BY_NAME. */
	public static final String BUILDING_NAME_PLACEHOLDER = "name";

	/** The Constant GET_UPLOAD_FLOOR_PLAN_URI. */
	public static final String GET_UPLOAD_FLOOR_PLAN_URI = "uploadFloorPlan/";

	/** The Constant GET_UPLOAD_FLOOR_PLAN_DATA_URI. */
	public static final String GET_UPLOAD_FLOOR_PLAN_DATA_URI = "uploadFloorPlanData/";

	/** The Constant GET_UPLOAD_MULTIPLE_FLOOR_PLAN_URI. */
	public static final String GET_UPLOAD_MULTIPLE_FLOOR_PLAN_URI = "uploadFloorPlans";

	/** The Constant GET_SYNC_RESULT_FILE_URI. */
	public static final String GET_SYNC_RESULT_FILE_URI = "syncInBuildingResultFile";

	/** The Constant GET_DOWNLOAD_FLOOR_PLAN_URI. */
	public static final String GET_DOWNLOAD_FLOOR_PLAN_URI = "downloadFloorPlan/";
	public static final String FILE_EXTENSION_ZIP = ".zip";
	/** The Constant GET_DOWNLOAD_FLOOR_PLAN_DATA_URI. */
	public static final String GET_DOWNLOAD_FLOOR_PLAN_DATA_URI = "downloadFloorPlanData/";

	

	/** The Constant FIRST_INDEX. */
	public static final int FIRST_INDEX = 0;

	/** The Constant SECOND_INDEX. */
	public static final int SECOND_INDEX = 1;


	public static final int THIRD_INDEX = 2;


	/** The Constant KEY_HEADER_FILENAME. */
	public static final String KEY_HEADER_FILENAME = "FileName";
	public static final String FLOOR_PLAN_DOES_NOT_EXIST_FOR = "Floorplan does not exist for ";

	/** The Constant BUILDING_ID_PLACEHOLDER. */
	public static final String BUILDING_ID_PLACEHOLDER = "id";

	/** The Constant MULTIPART_LIST_MINIMUM_LENGTH. */
	public static final int MULTIPART_LIST_MINIMUM_LENGTH = 1;
	
	/** The Constant FLOOR_PLAN_FILE_NAME. */
	public static final String FLOOR_PLAN_FILE_NAME = "IB_FLOOR_PLAN_";
	
	/** The Constant FLOOR_PLAN_FILE_SEPERATOR. */
	public static final String FLOOR_PLAN_FILE_SEPERATOR = "_";
	
	/** The Constant FLOOR_PLAN_FILE_EXTENSION. */
	public static final String FLOOR_PLAN_FILE_EXTENSION = ".zip";
	
	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL1. */
	public static final String GET_BUILDING_COUNT_GROUP_BY_GEOL1 = "getBuildingCountGroupByGeol1";
	
	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL2. */
	public static final String GET_BUILDING_COUNT_GROUP_BY_GEOL2 = "getBuildingCountGroupByGeol2";
	
	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL3. */
	public static final String GET_BUILDING_COUNT_GROUP_BY_GEOL3 = "getBuildingCountGroupByGeol3";
	
	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL4. */
	public static final String GET_BUILDING_COUNT_GROUP_BY_GEOL4 = "getBuildingCountGroupByGeol4";
	
	
	
	public static final String GET_BUILDING_COUNT_GROUP_BY_GEOL2_TABLE_VIEW = "getBuildingCountGroupByGeol2TableView";
	
	public static final String GET_BUILDING_COUNT_GROUP_BY_GEOL3_TABLE_VIEW = "getBuildingCountGroupByGeol3TableView";
	
	public static final String GET_BUILDING_COUNT_GROUP_BY_GEOL4_TABLE_VIEW = "getBuildingCountGroupByGeol4TableView";
	
	/** The Constant GET_BUILDING_BY_VIEWPORT. */
	public static final String GET_BUILDING_BY_VIEWPORT = "getBuildingByViewPort";
	
	/** The Constant GET_WING_BY_BUILDING. */
	public static final String GET_WING_BY_BUILDING = "getWingByBuilding";
	
	/** The Constant GET_FLOOR_BY_WING. */
	public static final String GET_FLOOR_BY_WING = "getFloorByWing";
	
	/** The Constant GET_UNIT_BY_FLOOR. */
	public static final String GET_UNIT_BY_FLOOR = "getUnitByFloor";
	
	/** The Constant GET_RECIPE_FILE_NAME_BY_UNIT. */
	public static final String GET_RECIPE_FILE_NAME_BY_UNIT = "getRecipeFileNameByUnit";
	
	public static final String GET_BUILDINGS_BY_GEOGRAPHY_L4="getBuildingsByGeographyL4";
	
	/** The Constant BUILDING_ID. */
	public static final String BUILDING_ID = "buildingId";
	
	/** The Constant WING_ID. */
	public static final String WING_ID = "wingId";
	
	/** The Constant FLOOR_ID. */
	public static final String FLOOR_ID = "floorId";
	
	/** The Constant UNIT_ID. */
	public static final String UNIT_ID = "unitId";
	
	/** The Constant TECHNOLOGY. */
	public static final String TECHNOLOGY = "technology";
	
	/** The Constant TECHNOLOGY. */
	public static final String GEOGRAPHY_L4_NAME_PLACEHOLDER= "name";
	
	/** The Constant BUILDING_TYPE. */
	public static final String BUILDING_TYPE = "buildingType";
	
	/** The Constant ZOOMLEVEL. */
	public static final String ZOOMLEVEL = "zoomLevel";
	
	public static final String GEOGRAPHY = "geography";
	
	public static final String GEOGRAPHY_ID = "geographyId";
	
	public static final String GEOGRAPHY_L4_NAME="geographyL4Name";
	
	/** The Constant NORTH_EAST_LAT. */
	public static final String NORTH_EAST_LAT = "NELat";
	
	/** The Constant NORTH_EAST_LONG. */
	public static final String NORTH_EAST_LONG = "NELng";
	
	/** The Constant SOUTH_WEST_LAT. */
	public static final String SOUTH_WEST_LAT = "SWLat";
	
	/** The Constant SOUTH_WEST_LONG. */
	public static final String SOUTH_WEST_LONG = "SWLng";
	
	public static final String GEOGRAPHY_L1_ZOOM = "GeographyL1Zoom";
	
	public static final String GEOGRAPHY_L2_ZOOM = "GeographyL2Zoom";
	
	public static final String GEOGRAPHY_L3_ZOOM = "GeographyL3Zoom";
	
	public static final String GEOGRAPHY_L4_ZOOM = "GeographyL4Zoom";

	public static final String BUILDING_ZOOM = "BuildingZoom";
	
	public static final String BUILDING = "Building";
	
	public static final Integer BUILDING_ADVANCE_SEARCH_PRIORITY_VALUE = 5;
	
	public static final String KEY_LATITUDE = "latitude";
	
	public static final String KEY_LONGITUDE = "longitude";
	
	public static final String KEY_ADDRESS = "address";
	
	public static final String KEY_NAME = "name";
	
	public static final String KEY_TYPE = "type";
	
	public static final String KEY_BUILDING = "BUILDING";
	
	/** The Constant GEOGRAPHYL1. */
	public static final String GEOGRAPHYL1 = "geographyL1";
	
	/** The Constant GEOGRAPHYL2. */
	public static final String GEOGRAPHYL2 = "geographyL2";
	
	/** The Constant GEOGRAPHYL3. */
	public static final String GEOGRAPHYL3 = "geographyL3";
	
	/** The Constant GEOGRAPHYL4. */
	public static final String GEOGRAPHYL4 = "geographyL4";

	public static final String GEOGRAPHY_ID_PLACE_HOLDER = "geographyId";

	public static final String BUILDING_NAME_ADDRESS_ALREADY_EXIST = "Building name and address already exist";

	public static final String BUILDING_WRAPPER_NOT_EXIST = "Building wrapper not exist";
	//FLOOR-PLAN  TABLE COLUMNS
		/** The Constant COLUMN_FLOOR_PLAN_IMAGE. */
			public static final String KEY_DATA = "data";
		/** The Constant FILE. */
		public static final String FILE = "file";
		public static final String CONSTANT_UNDER_SCORE = "_";
		/** The invalid parameter json. */
	
		/** The Constant PADDING_CHAR. */
		public static final String PADDING_CHAR = "0";

		/** The Constant ROW_KEY_LENGTH. */
		public static final int ROW_KEY_LENGTH = 8;

		/** Hbase table constants. */
		public static final String TABLE_FLOOR_PLAN = "FloorPlan";
		public static final String TABLE_TEST_RESULT = "TestResult";
		/** The Constant COLUMN_FAMILY. */
		public static final String COLUMN_FAMILY = "r";

		//FLOOR-PLAN  TABLE COLUMNS
		/** The Constant COLUMN_FLOOR_PLAN_IMAGE. */
		public static final String COLUMN_FLOOR_PLAN_IMAGE="fpimage";

		public static final String COLUMN_BG_IMAGE="bgimage";

		public static final String COLUMN_FLOOR_PLAN_JSON="fpjson";

		public static final String COLUMN_BG_IMAGE_NAME="bgimagename";

		/** TEST RESULT COLUMNS. */
		public static final String COLUMN_TEST_RESULT_JSON = "resultjson";
		public static final String COLUMN_TEST_START_TIME="teststarttime";
		public static final String COLUMN_TEST_END_TIME="testendtime";
		public static final String COLUMN_TEST_TYPE="testtype";
		public static final String COLUMN_UNIT_ID = "unitid";

		public static final String UNIT_INFO_FILE_NAME = "UNIT_INFO.txt";

		public static final String FLOOR_PLAN_JSON_FILE_NAME = "FLOOR_PLAN_JSON.txt";

		public static final String TEST_RESULT_JSON_FILE_NAME = "TEST_RESULT_JSON.txt";

		public static final String ENCODING_UTF_8 = "UTF-8";

		public static final Integer FILE_BUFFER_SIZE = 1024;

		public static final Integer OUTPUT_STREAM_START_OFFSET = 0;

		public static final Integer DATA_STREAM_END_VALUE = 0;

		public static final String FLOOR_PLAN_JSON_NAME_IDENTIFIER = ".txt";

		public static final String FLOOR_PLAN_IMAGE_NAME_IDENTIFIER = "FloorPlanImage";

		public static final String FLOOR_PLAN_ZIP_NAME = "IbFloorPlan_";

		public static final String FLOOR_PLAN_BACKGROUND_NAME_IDENTIFIER = "Background";
		public static final String PROBLEM_IN_UPLOADING_FLOOR_PLANS = "Problem in uploading floorplan in dropwizard";


		
		public static final String DEFAULT_BG_IMAGE_FILE_NAME = "backgroundImage.jpeg";

		public static final String GET_IB_UNIT_RESULT_FOR_RECIPE = "getIBUnitResultForRecipe";

		public static final String KEY_WO_RECIPE_MAPPING_ID = "woRecipeMappingId";

		public static final String DOWNLOAD_FLOOR_PLAN_FOR_LAYR3_URL = "DOWNLOAD_FLOOR_PLAN_FOR_LAYR3_URL";

		public static final String NONDRIVE = "NonDrive";

		public static final String DRIVE = "Drive";

		public static final String GET_FLOOR_DETAIL_BUILDINGWISE = "getFloorPlanDetailBuildingWise";
		public static final String QUERY_PARAM_IS_TO_SAVE_BUILDING="isToSaveBuilding";
		public static final String TEMPLATE_ADHOC="ADHOC";
		public static final String TEMPLATE_WORKORDER="WORKORDER";
		public static final String WO_RECIPE_MAPPING="woRecipeMapping";
		public static final String GENERIC_WORKORDER="genericWorkorder";
		public static final String FLOOR="floor";
		public static final String CRITERIA_BUILDING="building";
		public static final String GET_BUILDING_BY_NAME_AND_LATLONG = "getBuildingsByNameAndLatLong";
		public static final String GET_BUILDING_BY_BUILDING_ID = "getBuildingsByBuildingId";
		public static final String TABLE_IBFlOORPLAN = "IBFloorPlan";
		public static final byte[] CF_IBFlOORPLAN = Bytes.toBytes("r");
		public static final String FP_IMAGE = "fpimage";
		public static final String PTYPE = "ptype";
		public static final String FPB = "fpb";
		public static final String IMAGE = "i";
		public static final String LEGEND = "l";
		

}
