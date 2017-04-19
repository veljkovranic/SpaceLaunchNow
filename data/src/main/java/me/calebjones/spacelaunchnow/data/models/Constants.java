package me.calebjones.spacelaunchnow.data.models;


public final class Constants {
    public static final String ACTION_SUCCESS_VEHICLE_DETAILS = "SUCCESS_GET_ROCKETS";
    public static final String ACTION_SUCCESS_PREV_LAUNCHES = "SUCCESS_PREV_LAUNCHES";
    public static final String ACTION_SUCCESS_UP_LAUNCHES = "SUCCESS_UP_LAUNCHES";
    public static final String ACTION_SUCCESS_MISSIONS = "SUCCESS_GET_MISSIONS";
    public static final String ACTION_SUCCESS_VEHICLES = "SUCCESS_GET_VEHICLES";
    public static final String ACTION_SUCCESS_AGENCY = "SUCCESS_GET_AGENCY";
    public static final String ACTION_SUCCESS_LOCATION = "SUCCESS_GET_LOCATION";
    public static final String ACTION_SUCCESS_PADS = "SUCCESS_GET_PADS";
    public static final String ACTION_SUCCESS_LAUNCH = "SUCCESS_GET_LAUNCH";
    public static final String ACTION_SUCCESS_VEHICLES_FAMILY = "SUCCESS_GET_VEHICLES_FAMILY";

    public static final String ACTION_FAILURE_PREV_LAUNCHES = "FAILURE_PREV_LAUNCHES";
    public static final String ACTION_FAILURE_UP_LAUNCHES = "FAILURE_UP_LAUNCHES";
    public static final String ACTION_FAILURE_VEHICLE_DETAILS = "FAILURE_GET_ROCKETS";
    public static final String ACTION_FAILURE_MISSIONS = "FAILURE_GET_MISSION";
    public static final String ACTION_FAILURE_AGENCY = "FAILURE_GET_AGENCY";
    public static final String ACTION_FAILURE_LOCATION = "FAILURE_GET_LOCATION";
    public static final String ACTION_FAILURE_VEHICLES = "FAILURE_GET_VEHICLES";
    public static final String ACTION_FAILURE_PADS = "FAILURE_GET_PADS";
    public static final String ACTION_FAILURE_VEHICLES_FAMILY = "FAILURE_GET_VEHICLES_FAMILY";

    public static final String ACTION_FAILURE_LAUNCH = "FAILURE_GET_LAUNCH";
    public static final String ACTION_GET_PREV_LAUNCHES = "GET_PREV_LAUNCHES";
    public static final String ACTION_GET_UP_LAUNCHES = "GET_UP_LAUNCHES";
    public static final String ACTION_GET_VEHICLES_DETAIL = "GET_ROCKETS";
    public static final String ACTION_GET_ALL_DATA = "GET_ALL";
    public static final String ACTION_GET_ALL_NO_WIFI = "GET_ALL_NO_WIFI";
    public static final String ACTION_GET_MISSION = "GET_ALL_MISSIONS";
    public static final String ACTION_GET_AGENCY = "GET_AGENCY";
    public static final String ACTION_GET_LOCATION = "GET_LOCATION";

    public static final String ACTION_GET_ALL_LIBRARY_DATA = "GET_ALL_LIBRARY_DATA";
    public static final String ACTION_GET_VEHICLES = "GET_VEHICLES";
    public static final String ACTION_GET_PADS = "GET_PADS";
    public static final String ACTION_UPDATE_NEXT_LAUNCH = "UPDATE_NEXT_LAUNCH";
    public static final String ACTION_UPDATE_STORED_LAUNCH = "UPDATE_STORED_LAUNCH";
    public static final String ACTION_UPDATE_UP_LAUNCHES = "UPDATE_UP_LAUNCHES";

    public static final String ACTION_UPDATE_BACKGROUND = "UPDATE_UP_LAUNCHES_BACKGROUND";
    public static final String ACTION_UPDATE_PREV_LAUNCHES = "UPDATE_PREV_LAUNCHES";
    public static final String ACTION_UPDATE_LAUNCH = "UPDATE_LAUNCH";
    public static final String ACTION_UPDATE_AGENCY = "UPDATE_GET_AGENCY";
    public static final String ACTION_UPDATE_VEHICLES = "UPDATE_GET_VEHICLES";
    public static final String ACTION_CHECK_NEXT_LAUNCH_TIMER = "CHECK_NEXT_LAUNCH_TIMER";
    public static final String ACTION_PROGRESS = "ACTION_PROGRESS_COUNTER";
    public static final String SYNC_NOTIFIERS = "SYNC_LAUNCH_NOTIFIERS";

    public static int NOTIF_ID = 568975;
    public static int NOTIF_ID_DAY = 568985;
    public static int NOTIF_ID_HOUR = 568995;

    public static int DB_SCHEMA_VERSION = 189;
    public static int DB_SCHEMA_VERSION_1_5_5 = 188;

    public static String FORECAST_IO_BASE_URL = "https://api.forecast.io/";
    public static String API_BASE_URL = "http://calebjones.me/app/";
    public static String LIBRARY_BASE_URL = "https://launchlibrary.net/";
    public static String DEBUG_BASE_URL = "https://launchlibrary.net/";

    //These values are +1'd at runtime.
    public static final int DEFAULT_BLUR = 0;
    public static final int DEFAULT_RADIUS = 24;
    public static final int DEFAULT_DIM = 39;
    public static final int DEFAULT_GREY = 79;


    public static final String NAME_KEY = "me.calebjones.spacelaunchnow.wear.nextname";
    public static final String TIME_KEY = "me.calebjones.spacelaunchnow.wear.nexttime";
    public static final String DATE_KEY = "me.calebjones.spacelaunchnow.wear.nextdate";
    public static final String HOUR_KEY = "me.calebjones.spacelaunchnow.wear.hourmode";
    public static final String DYNAMIC_KEY = "me.calebjones.spacelaunchnow.wear.textdynamic";
    public static final String BACKGROUND_KEY = "me.calebjones.spacelaunchnow.wear.background";

    private Constants() {
    }

}
