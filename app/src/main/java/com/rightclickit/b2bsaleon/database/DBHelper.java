package com.rightclickit.b2bsaleon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by SekharKuppa.
 */
public class DBHelper extends SQLiteOpenHelper {
    // LogCat tag
    private static final String TAG = DBHelper.class.getSimpleName();

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "B2BSaleOn";

    // DeviceDetails Table - This table contains all device specific details
    private final String TABLE_USERDETAILS = "userdetails";

    // Column names
    private final String KEY_USER_ID = "user_id";
    private final String KEY_USER_CODE = "user_code";
    private final String KEY_NAME = "name";
    private final String KEY_EMAIL = "email";
    private final String KEY_PHONE_NUMBER = "phone_number";
    private final String KEY_AVATAR = "profile_pic";
    private final String KEY_STAKEHOLDER_ID = "stakeholder_id";
    private final String KEY_ADRESS = "address"; // progressive fare will effect after this distance
    private final String KEY_DEVICE_SYNC = "device_sync";
    private final String KEY_ACCESS_DEVICE = "access_device";
    private final String KEY_BACKUP = "backup";

    // Userdetails Table Create Statements
    private final String CREATE_TABLE_USERDETAILS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USERDETAILS + "(" + KEY_USER_ID + " VARCHAR,"
            + KEY_USER_CODE + " VARCHAR," + KEY_NAME + " VARCHAR," + KEY_EMAIL + " VARCHAR,"
            + KEY_PHONE_NUMBER + " VARCHAR," + KEY_AVATAR + " VARCHAR,"
            + KEY_STAKEHOLDER_ID + " VARCHAR," + KEY_ADRESS + " VARCHAR," + KEY_DEVICE_SYNC
            + " VARCHAR, " + KEY_ACCESS_DEVICE
            + " VARCHAR, " + KEY_BACKUP + " VARCHAR)";

    // DeviceTracking Table - This table contains all location data which we captured on time intervals and before start a trip
    private final String TABLE_DEVICE_TRACKING_DETAILS = "devicetrackingdetails";

    private final String C_DEVICE_TRACKING_ID = "id";
    private final String C_DEVICE_TRACKING_LAT = "lattitude";
    private final String C_DEVICE_TRACKING_LONG = "longitude";
    private final String C_DEVICE_TRACKING_ALTITUDE = "altitude";
    private final String C_DEVICE_TRACKING_SPEED = "speed";
    private final String C_DEVICE_TRACKING_ADDRESS = "address";
    private final String C_DEVICE_TRACKING_TIME = "time";

    private final String CREATE_TABLE_DEVICE_TRACKING = "CREATE TABLE IF NOT EXISTS " + TABLE_DEVICE_TRACKING_DETAILS +
            "(" + C_DEVICE_TRACKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + C_DEVICE_TRACKING_LAT + " TEXT, " + C_DEVICE_TRACKING_LONG + " TEXT, " +
            C_DEVICE_TRACKING_ALTITUDE + " TEXT, "+ C_DEVICE_TRACKING_SPEED + " TEXT, " + C_DEVICE_TRACKING_ADDRESS + " TEXT, " + C_DEVICE_TRACKING_TIME + " TEXT)";

    // Trips Table - This table contains all trips
    private final String TABLE_TRIPS = "trips";

    private final String C_TRIP_ID = "id";
    private final String C_TRIP_SOURCE = "source";
    private final String C_TRIP_DESTINATION = "destination";
    private final String C_TRIP_ST_TIME = "sttime";
    private final String C_TRIP_END_TIME = "endtime";
    private final String C_TRIP_SOURCE_LAT = "sourcelat";
    private final String C_TRIP_SOURCE_LONG = "sourcelong";
    private final String C_TRIP_DEST_LAT = "destlat";
    private final String C_TRIP_DEST_LONG = "destlong";
    private final String C_TRIP_DISTANCE = "distance";
    private final String C_TRIP_DURATION = "duration"; // in minutes
    private final String C_TRIP_TOTAL_FARE = "totalfare";
    private final String C_TRIP_DESTINATION_IMAGE = "destinationimage";
    private final String C_TRIP_SENSOR_IMAGE = "sensorimage";
    private final String C_TRIP_STATUS = "status"; // in order to maintain trip status i.e. completed or on trip and default value 0
    private final String C_TRIP_HAS_UPLOADED_TO_SERVER = "uploaded"; // in order to check weather this trip has uploaded to server or not
    private final String C_TRIP_BASEFARE = "basefare";
    private final String C_TRIP_PROGRESSIVEFARE = "progressivefare";
    private final String C_TRIP_AFTER_DISTANCE = "afterdistance";
    private final String C_TRIP_MEASUREMENT = "measurement"; // in order to get trip measurement in future also. bcoz there may be a chance to change that by admin.
    private final String C_TRIP_IS_DESTINATION_IMAGE_UPLOADED = "isDestinationImageUploaded";
    private final String C_TRIP_IS_SENSOR_IMAGE_UPLOADED = "isSensorImageUploaded";
    private final String C_TRIP_EDEST_NAME = "edestname";
    private final String C_TRIP_EDEST_ADDRESS = "edestaddress";
    private final String C_TRIP_EDEST_LAT = "edestlat";
    private final String C_TRIP_EDEST_LONG = "edestlong";
    private final String C_TRIP_EDISTANCE = "edistance";
    private final String C_TRIP_EFARE = "efare";

    private final String CREATE_TABLE_TRIPS = "CREATE TABLE IF NOT EXISTS " + TABLE_TRIPS +
            "(" + C_TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + C_TRIP_SOURCE + " TEXT, " +
            C_TRIP_DESTINATION + " TEXT, " + C_TRIP_ST_TIME + " TEXT, "+ C_TRIP_END_TIME + " TEXT, " +
            C_TRIP_SOURCE_LAT + " TEXT, " + C_TRIP_SOURCE_LONG + " TEXT, "+ C_TRIP_DEST_LAT + " TEXT, " + C_TRIP_DEST_LONG + " TEXT, " +
            C_TRIP_DISTANCE + " TEXT, " + C_TRIP_DURATION + " TEXT, " + C_TRIP_TOTAL_FARE + " TEXT, " + C_TRIP_DESTINATION_IMAGE + " TEXT, " +
            C_TRIP_SENSOR_IMAGE + " TEXT, " + C_TRIP_STATUS + " INTEGER DEFAULT 0, " + C_TRIP_HAS_UPLOADED_TO_SERVER + " INTEGER DEFAULT 0, " +
            C_TRIP_BASEFARE + " TEXT, " + C_TRIP_PROGRESSIVEFARE + " TEXT, " + C_TRIP_AFTER_DISTANCE + " TEXT, " + C_TRIP_MEASUREMENT + " TEXT, " +
            C_TRIP_IS_DESTINATION_IMAGE_UPLOADED + " INTEGER DEFAULT 0, " + C_TRIP_IS_SENSOR_IMAGE_UPLOADED + " INTEGER DEFAULT 0, " +
            C_TRIP_EDEST_NAME + " TEXT, " + C_TRIP_EDEST_ADDRESS + " TEXT, "+ C_TRIP_EDEST_LAT + " TEXT, " + C_TRIP_EDEST_LONG + " TEXT, " +
            C_TRIP_EDISTANCE + " TEXT, " + C_TRIP_EFARE + " TEXT)";

    //Trip Details Table - This table contains all location data which we captured on time interval once trip started i.e. after starting a trip
    private final String TABLE_TRIP_DETAILS = "tripdetails";

    private final String C_TRIP_DETAILS_ID = "id";
    private final String C_TRIP_DETAILS_TRIP_ID = "tripid";
    private final String C_TRIP_DETAILS_LAT = "lattitude";
    private final String C_TRIP_DETAILS_LONG = "longitude";
    private final String C_TRIP_DETAILS_ALTITUDE = "altitude";
    private final String C_TRIP_DETAILS_SPEED = "speed";
    private final String C_TRIP_DETAILS_ADDRESS = "address";
    private final String C_TRIP_DETAILS_TIME = "time";

    private final String CREATE_TABLE_TRIP_DETAILS = "CREATE TABLE IF NOT EXISTS " + TABLE_TRIP_DETAILS + "(" + C_TRIP_DETAILS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            C_TRIP_DETAILS_TRIP_ID + " INTEGER, " + C_TRIP_DETAILS_LAT + " TEXT, " + C_TRIP_DETAILS_LONG + " TEXT, " + C_TRIP_DETAILS_ALTITUDE + " TEXT, " +
            C_TRIP_DETAILS_SPEED + " TEXT, " + C_TRIP_DETAILS_ADDRESS + " TEXT, " + C_TRIP_DETAILS_TIME + " TEXT)";

    //Trip Distance Table - This table contains distance b/w 2 consecutive latitude longitude after trip started
    private final String TABLE_DISTANCES = "tripdistances";

    private final String C_DISTANCES_ID = "id";
    private final String C_DISTANCES_TRIP_ID = "tripid";
    private final String C_DISTANCES_LAT = "lattitude";
    private final String C_DISTANCES_LONG = "longitude";
    private final String C_DISTANCE = "distance";
    private final String C_DISTANCES_CREATEDTIME = "createdtime";

    private final String CREATE_TABLE_DISTANCES = "CREATE TABLE IF NOT EXISTS " + TABLE_DISTANCES + "(" + C_DISTANCES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            C_DISTANCES_TRIP_ID + " INTEGER, " + C_DISTANCES_LAT + " TEXT, " + C_DISTANCES_LONG + " TEXT, " + C_DISTANCE + " FLOAT, "+ C_DISTANCES_CREATEDTIME + " TEXT)";

    //Panic Data Table - This table contains all auto captured images & recorded voices when we click on panic button
    private final String TABLE_PANIC_DATA = "panicdata";

    private final String C_PANIC_DATA_ID = "id";
    private final String C_PANIC_DATA_TRIP_ID = "tripid"; //any active trip id when we click on panic button o/w this value is 0
    private final String C_PANIC_DATA_FILENAME = "filename";
    private final String C_PANIC_DATA_FILETYPE = "filetype"; //image or audio
    private final String C_PANIC_DATA_HAS_UPLOADED = "uploaded"; // in order to check weather this file has uploaded to server or not
    private final String C_PANIC_DATA_TIMESTAMP = "createdtime";

    private final String CREATE_TABLE_PANIC_DATA = "CREATE TABLE IF NOT EXISTS " + TABLE_PANIC_DATA + "(" + C_PANIC_DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            C_PANIC_DATA_TRIP_ID + " INTEGER DEFAULT 0, " + C_PANIC_DATA_FILENAME + " TEXT, " + C_PANIC_DATA_FILETYPE + " TEXT, " + C_PANIC_DATA_TIMESTAMP + " TEXT, " + C_PANIC_DATA_HAS_UPLOADED + " INTEGER DEFAULT 0)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_USERDETAILS);
            db.execSQL(CREATE_TABLE_DEVICE_TRACKING);
            db.execSQL(CREATE_TABLE_TRIPS);
            db.execSQL(CREATE_TABLE_TRIP_DETAILS);
            db.execSQL(CREATE_TABLE_DISTANCES);
            db.execSQL(CREATE_TABLE_PANIC_DATA);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            /*db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_DEVICEDETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_DEVICE_TRACKING);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TRIPS);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TRIP_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_DISTANCES);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PANIC_DATA);

            // create new tables
            onCreate(db); */
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method to insert user details
     *
     * @param id
     * @param userCode
     * @param userName
     * @param email
     * @param phone
     * @param profilrPic
     * @param stakeHolder
     * @param address
     * @param deviceSync
     * @param accessDevice
     * @param backUp
     */
    public void insertUserDetails(String id, String userCode, String userName, String email, String phone, String profilrPic, String stakeHolder, String address, String deviceSync, String accessDevice, String backUp) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID, id);
            values.put(KEY_USER_CODE, userCode);
            values.put(KEY_NAME, userName);
            values.put(KEY_EMAIL, email);
            values.put(KEY_PHONE_NUMBER, phone);
            values.put(KEY_AVATAR, profilrPic);
            values.put(KEY_STAKEHOLDER_ID, stakeHolder);
            values.put(KEY_ADRESS, address);
            values.put(KEY_DEVICE_SYNC, deviceSync);
            values.put(KEY_ACCESS_DEVICE, accessDevice);
            values.put(KEY_BACKUP, backUp);

            // insert row
            db.insert(TABLE_USERDETAILS, null, values);
            values.clear();
        } catch (Exception e){
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to get device unique id
     *
     * @return uniqueid
     */
    public String getDeviceUniqueId() {
        String uniqueId = "";
        try {
            String query = "SELECT  * FROM " + TABLE_USERDETAILS;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    uniqueId = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return uniqueId;
    }

    /**
     * Method to get count of the device details table
     */
    public int getDeviceDetailsTableCount() {
        int noOfEvents = 0;
        try {
            String countQuery = "SELECT * FROM " + TABLE_USERDETAILS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                noOfEvents = cursor.getCount();
                cursor.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return noOfEvents;
    }

    /**
     * Method to clear values in device details table
     */
    public void deleteValuesFromDeviceDetailsTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_USERDETAILS, null, null);
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='" + TABLE_USERDETAILS + "'");
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method to update the device details
     */
//    public long updateDeviceDetails(String deviceUDID, String name, String baseFare,String baseFareMeasure, String fareInr, String measures, String afterDistance, String serverip, String status) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        long effectedRows = 0;
//        try {
//            ContentValues values = new ContentValues();
//            //values.put(KEY_DEVICE_ID, deviceUDID);
//            values.put(KEY_NAME, name);
//            values.put(KEY_BASEFARE, baseFare);
//            values.put(KEY_BASEFARE_MEASURE, baseFareMeasure);
//            values.put(KEY_FARE, fareInr);
//            values.put(KEY_MEASUREMENT, measures);
//            values.put(KEY_AFTER_DISTANCE, afterDistance);
//            values.put(KEY_SERVERIP, serverip);
//            values.put(KEY_STATUS, status);
//
//            // update row
//            effectedRows = db.update(TABLE_USERDETAILS, values, KEY_DEVICE_ID + " = ?", new String[]{String.valueOf(deviceUDID)});
//            values.clear();
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        db.close();
//        return effectedRows;
//    }

    /**
     * Method to get device details from the db.
     */
//    public DeviceDetailsBean getDeviceDetailsFromDb() {
//        DeviceDetailsBean deviceDetailsBean = null;
//        try {
//            // Select All Query
//            String selectQuery = "SELECT  * FROM " + TABLE_DEVICEDETAILS;
//
//            SQLiteDatabase db = this.getWritableDatabase();
//            Cursor cursor = db.rawQuery(selectQuery, null);
//
//            deviceDetailsBean = new DeviceDetailsBean();
//
//            if (cursor.moveToFirst()) {
//                do {
//                    deviceDetailsBean.setmDeviceNumber(cursor.getString(0));
//                    deviceDetailsBean.setmDeviceName(cursor.getString(1));
//                    deviceDetailsBean.setmDeviceFare(cursor.getString(2));
//                    deviceDetailsBean.setmDistaceType(cursor.getString(3));
//                    deviceDetailsBean.setmFare(cursor.getString(4));
//                    deviceDetailsBean.setmFareDistaceType(cursor.getString(5));
//                    deviceDetailsBean.setmAfterDistace(cursor.getString(6));
//                    deviceDetailsBean.setmServeripaddress(cursor.getString(7));
//                    deviceDetailsBean.setmStatus(cursor.getString(8));
//
//                } while (cursor.moveToNext());
//            }
//
//            cursor.close();
//            db.close();
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return deviceDetailsBean;
//    }
//
//    /**
//     * Method to insert values into device tracking details table
//     */
//    public void insertIntoDeviceTrackingDetails(DeviceLocationBean dldBean){
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put(C_DEVICE_TRACKING_LAT, dldBean.getLatitude());
//            values.put(C_DEVICE_TRACKING_LONG, dldBean.getLongitude());
//            values.put(C_DEVICE_TRACKING_ALTITUDE, dldBean.getAltitude());
//            values.put(C_DEVICE_TRACKING_SPEED, dldBean.getSpeed());
//            values.put(C_DEVICE_TRACKING_ADDRESS, dldBean.getAddress());
//            values.put(C_DEVICE_TRACKING_TIME, dldBean.getTime());
//
//            db.insert(TABLE_DEVICE_TRACKING_DETAILS, null, values);
//            values.clear();
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Method to delete from device tracking details table
//     */
//    public void deleteFromDeviceTrackingDetails(int id){
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            db.delete(TABLE_DEVICE_TRACKING_DETAILS, C_DEVICE_TRACKING_ID + " = ?", new String[]{String.valueOf(id)});
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Method to fetch all records from device tracking details table
//     */
//    public List<DeviceLocationBean> fetchAllRecordsFromDeviceTrackingDetails(){
//        List<DeviceLocationBean> allDeviceTrackRecords = new ArrayList<DeviceLocationBean>();
//        try {
//            String selectQuery = "SELECT  * FROM " + TABLE_DEVICE_TRACKING_DETAILS;
//
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor c = db.rawQuery(selectQuery, null);
//
//            // looping through all rows and adding to list
//            if (c.moveToFirst()) {
//                do {
//                    DeviceLocationBean dlBean = new DeviceLocationBean();
//                    dlBean.setId(c.getInt((c.getColumnIndex(C_DEVICE_TRACKING_ID))));
//                    dlBean.setLatitude((c.getString(c.getColumnIndex(C_DEVICE_TRACKING_LAT))));
//                    dlBean.setLongitude(c.getString(c.getColumnIndex(C_DEVICE_TRACKING_LONG)));
//                    dlBean.setAltitude(c.getDouble(c.getColumnIndex(C_DEVICE_TRACKING_ALTITUDE)));
//                    dlBean.setSpeed((c.getString(c.getColumnIndex(C_DEVICE_TRACKING_SPEED))));
//                    dlBean.setAddress((c.getString(c.getColumnIndex(C_DEVICE_TRACKING_ADDRESS))));
//                    dlBean.setTime(c.getString(c.getColumnIndex(C_DEVICE_TRACKING_TIME)));
//
//                    allDeviceTrackRecords.add(dlBean);
//                } while (c.moveToNext());
//            }
//
//            c.close();
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return allDeviceTrackRecords;
//    }
//
//    /**
//     * Method to insert values into trips table on trip started
//     */
//    public long insertIntoTripsOnTripStarted(TripBean tripBean){
//        long trip_id = 0;
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put(C_TRIP_SOURCE, tripBean.getSource());
//            values.put(C_TRIP_SOURCE_LAT, tripBean.getSourcelat());
//            values.put(C_TRIP_SOURCE_LONG, tripBean.getSourcelong());
//            values.put(C_TRIP_ST_TIME, tripBean.getSttime());
//            values.put(C_TRIP_BASEFARE, tripBean.getBasefare());
//            values.put(C_TRIP_AFTER_DISTANCE, tripBean.getAfterdistance());
//            values.put(C_TRIP_MEASUREMENT, tripBean.getMeasurement());
//            values.put(C_TRIP_STATUS, 0);
//            values.put(C_TRIP_IS_DESTINATION_IMAGE_UPLOADED, 0);
//            values.put(C_TRIP_IS_SENSOR_IMAGE_UPLOADED, 0);
//            values.put(C_TRIP_EDEST_NAME, tripBean.getEdestname());
//            values.put(C_TRIP_EDEST_ADDRESS, tripBean.getEdestaddress());
//            values.put(C_TRIP_EDEST_LAT, tripBean.getEdestlat());
//            values.put(C_TRIP_EDEST_LONG, tripBean.getEdestlong());
//            values.put(C_TRIP_EDISTANCE, tripBean.getEdistance());
//            values.put(C_TRIP_EFARE, tripBean.getEfare());
//
//            trip_id = db.insert(TABLE_TRIPS, null, values);
//
//            values.clear();
//            db.close();
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return trip_id;
//    }
//
//    /**
//     * Method to update values in trips table on trip end
//     */
//    public int updateTripsOnTripEnd(TripBean tripBean){
//        int updatedNoOfRows = 0;
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put(C_TRIP_DESTINATION, tripBean.getDestination());
//            values.put(C_TRIP_DEST_LAT, tripBean.getDestlat());
//            values.put(C_TRIP_DEST_LONG, tripBean.getDestlong());
//            values.put(C_TRIP_END_TIME, tripBean.getEndtime());
//            values.put(C_TRIP_DISTANCE, tripBean.getDistance());
//            values.put(C_TRIP_DURATION, tripBean.getDuration());
//            values.put(C_TRIP_TOTAL_FARE, tripBean.getTotalfare());
//            values.put(C_TRIP_PROGRESSIVEFARE, tripBean.getProgressivefare());
//            values.put(C_TRIP_DESTINATION_IMAGE, tripBean.getDestinationimage());
//            values.put(C_TRIP_SENSOR_IMAGE, tripBean.getSensorimage());
//            values.put(C_TRIP_STATUS, 1);
//
//            updatedNoOfRows = db.update(TABLE_TRIPS, values, C_TRIP_ID + " = ?", new String[]{String.valueOf(tripBean.getId())});
//
//            values.clear();
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return updatedNoOfRows;
//    }
//
//    /**
//     * Method to fetch all records from trips table
//     */
//    public List<TripBean> fetchAllRecordsFromTrips(String columnName, int status){
//        List<TripBean> allTripsList = new ArrayList<TripBean>();
//        try {
//            String selectQuery = "SELECT * FROM " + TABLE_TRIPS + " WHERE "+ columnName +" = " + status;
//
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor c = db.rawQuery(selectQuery, null);
//
//            //Log.d("********", "##### TRIP ##### c = " + DatabaseUtils.dumpCursorToString(c));
//
//            // looping through all rows and adding to list
//            if (c.moveToFirst()) {
//                do {
//                    TripBean trip = new TripBean();
//                    trip.setId(c.getInt((c.getColumnIndex(C_TRIP_ID))));
//                    trip.setSource((c.getString(c.getColumnIndex(C_TRIP_SOURCE))));
//                    trip.setDestination(c.getString(c.getColumnIndex(C_TRIP_DESTINATION)));
//                    trip.setSttime((c.getString(c.getColumnIndex(C_TRIP_ST_TIME))));
//                    trip.setEndtime(c.getString(c.getColumnIndex(C_TRIP_END_TIME)));
//                    trip.setSourcelat(c.getString(c.getColumnIndex(C_TRIP_SOURCE_LAT)));
//                    trip.setSourcelong(c.getString(c.getColumnIndex(C_TRIP_SOURCE_LONG)));
//                    trip.setDestlat(c.getString(c.getColumnIndex(C_TRIP_DEST_LAT)));
//                    trip.setDestlong(c.getString(c.getColumnIndex(C_TRIP_DEST_LONG)));
//                    trip.setDistance((c.getString(c.getColumnIndex(C_TRIP_DISTANCE))));
//                    trip.setDuration(c.getString(c.getColumnIndex(C_TRIP_DURATION)));
//                    trip.setTotalfare(c.getString(c.getColumnIndex(C_TRIP_TOTAL_FARE)));
//                    trip.setDestinationimage((c.getString(c.getColumnIndex(C_TRIP_DESTINATION_IMAGE))));
//                    trip.setSensorimage(c.getString(c.getColumnIndex(C_TRIP_SENSOR_IMAGE)));
//                    trip.setStatus(c.getInt((c.getColumnIndex(C_TRIP_STATUS))));
//                    trip.setUploaded(c.getInt((c.getColumnIndex(C_TRIP_HAS_UPLOADED_TO_SERVER))));
//                    trip.setMeasurement(c.getString(c.getColumnIndex(C_TRIP_MEASUREMENT)));
//                    trip.setBasefare(c.getString(c.getColumnIndex(C_TRIP_BASEFARE)));
//                    trip.setProgressivefare(c.getString(c.getColumnIndex(C_TRIP_PROGRESSIVEFARE)));
//                    trip.setAfterdistance(c.getString(c.getColumnIndex(C_TRIP_AFTER_DISTANCE)));
//                    trip.setDiUploaded(c.getInt(c.getColumnIndex(C_TRIP_IS_DESTINATION_IMAGE_UPLOADED)));
//                    trip.setSiUploaded(c.getInt(c.getColumnIndex(C_TRIP_IS_SENSOR_IMAGE_UPLOADED)));
//
//                    allTripsList.add(trip);
//                } while (c.moveToNext());
//            }
//
//            c.close();
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return allTripsList;
//    }
//
//    /**
//     * Method to fetch trip by id
//     */
//    public TripBean fetchTripById(long tripId){
//        TripBean trip = null;
//        try {
//            String selectQuery = "SELECT  * FROM " + TABLE_TRIPS + " WHERE "+ C_TRIP_ID +" = " + tripId;
//
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor c = db.rawQuery(selectQuery, null);
//
//             if (c.moveToFirst()) {
//                do {
//                    trip = new TripBean();
//                    trip.setId(c.getInt((c.getColumnIndex(C_TRIP_ID))));
//                    trip.setSource((c.getString(c.getColumnIndex(C_TRIP_SOURCE))));
//                    trip.setDestination(c.getString(c.getColumnIndex(C_TRIP_DESTINATION)));
//                    trip.setSttime((c.getString(c.getColumnIndex(C_TRIP_ST_TIME))));
//                    trip.setEndtime(c.getString(c.getColumnIndex(C_TRIP_END_TIME)));
//                    trip.setSourcelat(c.getString(c.getColumnIndex(C_TRIP_SOURCE_LAT)));
//                    trip.setSourcelong(c.getString(c.getColumnIndex(C_TRIP_SOURCE_LONG)));
//                    trip.setDestlat(c.getString(c.getColumnIndex(C_TRIP_DEST_LAT)));
//                    trip.setDestlong(c.getString(c.getColumnIndex(C_TRIP_DEST_LONG)));
//                    trip.setDistance((c.getString(c.getColumnIndex(C_TRIP_DISTANCE))));
//                    trip.setDuration(c.getString(c.getColumnIndex(C_TRIP_DURATION)));
//                    trip.setTotalfare(c.getString(c.getColumnIndex(C_TRIP_TOTAL_FARE)));
//                    trip.setDestinationimage((c.getString(c.getColumnIndex(C_TRIP_DESTINATION_IMAGE))));
//                    trip.setSensorimage(c.getString(c.getColumnIndex(C_TRIP_SENSOR_IMAGE)));
//                    trip.setStatus(c.getInt((c.getColumnIndex(C_TRIP_STATUS))));
//                    trip.setUploaded(c.getInt((c.getColumnIndex(C_TRIP_HAS_UPLOADED_TO_SERVER))));
//                    trip.setMeasurement(c.getString(c.getColumnIndex(C_TRIP_MEASUREMENT)));
//                    trip.setBasefare(c.getString(c.getColumnIndex(C_TRIP_BASEFARE)));
//                    trip.setProgressivefare(c.getString(c.getColumnIndex(C_TRIP_PROGRESSIVEFARE)));
//                    trip.setAfterdistance(c.getString(c.getColumnIndex(C_TRIP_AFTER_DISTANCE)));
//                    trip.setDiUploaded(c.getInt(c.getColumnIndex(C_TRIP_IS_DESTINATION_IMAGE_UPLOADED)));
//                    trip.setSiUploaded(c.getInt(c.getColumnIndex(C_TRIP_IS_SENSOR_IMAGE_UPLOADED)));
//                    trip.setEdestname(c.getString(c.getColumnIndex(C_TRIP_EDEST_NAME)));
//                    trip.setEdestaddress(c.getString(c.getColumnIndex(C_TRIP_EDEST_ADDRESS)));
//                    trip.setEdestlat(c.getString(c.getColumnIndex(C_TRIP_EDEST_LAT)));
//                    trip.setEdestlong(c.getString(c.getColumnIndex(C_TRIP_EDEST_LONG)));
//                    trip.setEdistance(c.getString(c.getColumnIndex(C_TRIP_EDISTANCE)));
//                    trip.setEfare(c.getString(c.getColumnIndex(C_TRIP_EFARE)));
//
//                } while (c.moveToNext());
//            }
//
//            c.close();
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return trip;
//    }
//
//    /**
//     * Method to update any particular value in trips table by passing trip id, column name & it's value
//     */
//    public void updateTripsTable(long tripId, String columnName, String value){
//        SQLiteDatabase db = this.getWritableDatabase();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(columnName, value);
//
//            int status = db.update(TABLE_TRIPS, values, C_TRIP_ID + " = ?", new String[]{String.valueOf(tripId)});
//
//            values.clear();
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        db.close();
//    }
//
//
//    /**
//     * Method to insert values into device trip details table
//     */
//    public void insertIntoTripDetails(DeviceLocationBean tdBean){
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put(C_TRIP_DETAILS_TRIP_ID, tdBean.getTripid());
//            values.put(C_TRIP_DETAILS_LAT, tdBean.getLatitude());
//            values.put(C_TRIP_DETAILS_LONG, tdBean.getLongitude());
//            values.put(C_TRIP_DETAILS_ALTITUDE, tdBean.getAltitude());
//            values.put(C_TRIP_DETAILS_SPEED, tdBean.getSpeed());
//            values.put(C_TRIP_DETAILS_ADDRESS, tdBean.getAddress());
//            values.put(C_TRIP_DETAILS_TIME, tdBean.getTime());
//
//            db.insert(TABLE_TRIP_DETAILS, null, values);
//            values.clear();
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Method to delete from device trip details table
//     */
//    public void deleteFromTripDetails(int id){
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            db.delete(TABLE_TRIP_DETAILS, C_TRIP_DETAILS_ID + " = ?", new String[]{String.valueOf(id)});
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Method to fetch all records from trips details table
//     */
//    public List<DeviceLocationBean> fetchAllRecordsFromTripDetails(){
//        List<DeviceLocationBean> allTripDetails = new ArrayList<DeviceLocationBean>();
//        try {
//            String selectQuery = "SELECT  * FROM " + TABLE_TRIP_DETAILS;
//
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor c = db.rawQuery(selectQuery, null);
//
//            // looping through all rows and adding to list
//            if (c.moveToFirst()) {
//                do {
//                    DeviceLocationBean tripDetailsBean = new DeviceLocationBean();
//                    tripDetailsBean.setId(c.getInt((c.getColumnIndex(C_TRIP_DETAILS_ID))));
//                    tripDetailsBean.setTripid(c.getInt((c.getColumnIndex(C_TRIP_DETAILS_TRIP_ID))));
//                    tripDetailsBean.setLatitude((c.getString(c.getColumnIndex(C_TRIP_DETAILS_LAT))));
//                    tripDetailsBean.setLongitude(c.getString(c.getColumnIndex(C_TRIP_DETAILS_LONG)));
//                    tripDetailsBean.setAltitude((c.getDouble(c.getColumnIndex(C_TRIP_DETAILS_ALTITUDE))));
//                    tripDetailsBean.setSpeed((c.getString(c.getColumnIndex(C_TRIP_DETAILS_SPEED))));
//                    tripDetailsBean.setAddress((c.getString(c.getColumnIndex(C_TRIP_DETAILS_ADDRESS))));
//                    tripDetailsBean.setTime(c.getString(c.getColumnIndex(C_TRIP_DETAILS_TIME)));
//
//                    allTripDetails.add(tripDetailsBean);
//                } while (c.moveToNext());
//            }
//
//            c.close();
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return allTripDetails;
//    }
//
//    /**
//     * Method to get count of the device tracking table
//     */
//    public int getDeviceTrackingTableCount() {
//        int noOfEvents = 0;
//        try{
//            String countQuery = "SELECT * FROM " + TABLE_DEVICE_TRACKING_DETAILS;
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(countQuery, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//                noOfEvents = cursor.getCount();
//                cursor.close();
//            }
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return noOfEvents;
//    }
//
//    /**
//     * Method to get count of the trips table
//     */
//    public int getTripsTableCount(String columnName, int status) {
//        int noOfEvents = 0;
//        try {
//            String countQuery = "SELECT * FROM " + TABLE_TRIPS + " WHERE " + columnName + " = " + status;
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(countQuery, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//                noOfEvents = cursor.getCount();
//                cursor.close();
//            }
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return noOfEvents;
//    }
//
//    /**
//     * Method to get count of the unuploaded trips
//     */
//    public int getUnuploadedTripsTableCount() {
//        int noOfEvents = 0;
//        try {
//            String countQuery = "SELECT * FROM " + TABLE_TRIPS + " WHERE status = 1 AND uploaded = 0";
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(countQuery, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//                noOfEvents = cursor.getCount();
//                cursor.close();
//            }
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return noOfEvents;
//    }
//
//    /**
//     * Method to get count of the trip details table
//     */
//    public int getTripDetailsTableCount() {
//        int noOfEvents = 0;
//        try {
//            String countQuery = "SELECT * FROM " + TABLE_TRIP_DETAILS;
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(countQuery, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//                noOfEvents = cursor.getCount();
//                cursor.close();
//            }
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return noOfEvents;
//    }
//
//    /**
//     * Method to get count of unuploaded images from trips table
//     */
//    public int getUnuploadedImagesCount() {
//        int noOfEvents = 0;
//        try {
//            String countQuery = "SELECT * FROM " + TABLE_TRIPS + " WHERE status = 1 AND (" + C_TRIP_IS_DESTINATION_IMAGE_UPLOADED + " = 0 OR " + C_TRIP_IS_SENSOR_IMAGE_UPLOADED + " = 0)";
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(countQuery, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//                noOfEvents = cursor.getCount();
//                cursor.close();
//            }
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return noOfEvents;
//    }
//
//    /**
//     * Method to fetch all unuploaded images from trips table
//     */
//    public List<TripImagesBean> fetchUnuploadedImagesFromTrips(){
//        List<TripImagesBean> allTripsList = new ArrayList<TripImagesBean>();
//        try {
//            String selectQuery = "SELECT id,isDestinationImageUploaded,isSensorImageUploaded,endtime FROM " + TABLE_TRIPS + " WHERE status = 1 AND (" + C_TRIP_IS_DESTINATION_IMAGE_UPLOADED + " = 0 OR " + C_TRIP_IS_SENSOR_IMAGE_UPLOADED + " = 0)";
//
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor c = db.rawQuery(selectQuery, null);
//
//            //System.out.println("##### Unuploaded Images ##### c = " + DatabaseUtils.dumpCursorToString(c));
//
//            // looping through all rows and adding to list
//            if (c.moveToFirst()) {
//                do {
//                    TripImagesBean tripImagesBean = new TripImagesBean();
//                    tripImagesBean.setId(c.getInt((c.getColumnIndex(C_TRIP_ID))));
//                    tripImagesBean.setDiUploaded(c.getInt(c.getColumnIndex(C_TRIP_IS_DESTINATION_IMAGE_UPLOADED)));
//                    tripImagesBean.setSiUploaded(c.getInt(c.getColumnIndex(C_TRIP_IS_SENSOR_IMAGE_UPLOADED)));
//                    tripImagesBean.setTimestamp(c.getString(c.getColumnIndex(C_TRIP_END_TIME)));
//
//                    allTripsList.add(tripImagesBean);
//                } while (c.moveToNext());
//            }
//
//            c.close();
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return allTripsList;
//    }
//
//    /**
//     * Method to update uploaded image status in trips table by passing trip id
//     */
//    public void updateUploadedImagesStatus(long tripId){
//        SQLiteDatabase db = this.getWritableDatabase();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(C_TRIP_IS_DESTINATION_IMAGE_UPLOADED, 1);
//            values.put(C_TRIP_IS_SENSOR_IMAGE_UPLOADED, 1);
//
//            int status = db.update(TABLE_TRIPS, values, C_TRIP_ID + " = ?", new String[]{String.valueOf(tripId)});
//
//            values.clear();
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        db.close();
//    }
//
//    /** 
//     * Method to calculate distance(s) b/w 2 consecutive lat long after trip started  by fetching from local db
//     */
//    public void calculateDistance(DeviceLocationBean deviceLocationBean, String measurement){
//        double preLat, preLong;
//        float distance = 0.0f;
//
//        SQLiteDatabase rDB = this.getReadableDatabase();
//        SQLiteDatabase wDB = this.getWritableDatabase();
//
//        try {
//            String selectQuery = "SELECT  * FROM " + TABLE_DISTANCES + " WHERE " + C_DISTANCES_TRIP_ID + " = " + deviceLocationBean.getTripid() + " ORDER BY " + C_DISTANCES_ID +" DESC LIMIT 1";
//
//            Cursor cursor = rDB.rawQuery(selectQuery, null);
//
//            if(cursor.moveToFirst()){
//                preLat = Double.parseDouble(cursor.getString(cursor.getColumnIndex(C_DISTANCES_LAT)));
//                preLong = Double.parseDouble(cursor.getString(cursor.getColumnIndex(C_DISTANCES_LONG)));
//
//                distance = Utils.getDistanceBWLatLong(preLat, preLong, Double.parseDouble(deviceLocationBean.getLatitude()), Double.parseDouble(deviceLocationBean.getLongitude()), measurement);
//            } else{
//                //wDB.delete(TABLE_DISTANCES, null, null);
//
//                distance = 0.0f;
//            }
//
//            rDB.close();
//            wDB.close();
//
//            insertIntoDistanceTable(deviceLocationBean, distance);
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /** 
//     * Method to insert calculated distance(s) b/w 2 consecutive lat long after trip started 
//     */
//    public void insertIntoDistanceTable(DeviceLocationBean dldBean, float distance) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        try {
//            ContentValues values = new ContentValues();
//            values.put(C_DISTANCES_TRIP_ID, dldBean.getTripid());
//            values.put(C_DISTANCES_LAT, dldBean.getLatitude());
//            values.put(C_DISTANCES_LONG, dldBean.getLongitude());
//            values.put(C_DISTANCE, distance);
//            values.put(C_DISTANCES_CREATEDTIME, dldBean.getTime());
//
//            db.insert(TABLE_DISTANCES, null, values);
//            values.clear();
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        db.close();
//    }
//
//    /** 
//     * Method to get calculated distance(s) for trip 
//     */
//    public float getTripTotalDistance(long tripNo){
//        float distance = 0.0f;
//        SQLiteDatabase rDB = this.getReadableDatabase();
//
//        try{
//            String selectQuery = "SELECT  SUM(" + C_DISTANCE +") FROM " + TABLE_DISTANCES + " WHERE " + C_DISTANCES_TRIP_ID + " = " + tripNo;
//
//            Cursor cursor = rDB.rawQuery(selectQuery, null);
//
//            if(cursor.moveToFirst()){
//                if(cursor.getString(0) != null)
//                    distance = Float.parseFloat(cursor.getString(0));
//            }
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        rDB.close();
//
//        return distance;
//    }
//
//    /**
//     * Method to insert values into panic data table
//     */
//    public void insertIntoPanicData(PanicDataBean panicDataBean){
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put(C_PANIC_DATA_TRIP_ID, panicDataBean.getTripid());
//            values.put(C_PANIC_DATA_FILENAME, panicDataBean.getFilename());
//            values.put(C_PANIC_DATA_FILETYPE, panicDataBean.getFiletype());
//            values.put(C_PANIC_DATA_TIMESTAMP, panicDataBean.getCreatedtime());
//
//            long nor = db.insert(TABLE_PANIC_DATA, null, values);
//
//            values.clear();
//            db.close();
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Method to fetch all records from panic data table
//     */
//    public List<PanicDataBean> fetchAllRecordsFromPanicDataTable(){
//        List<PanicDataBean> allFilesList = new ArrayList<PanicDataBean>();
//        try {
//            String selectQuery = "SELECT * FROM " + TABLE_PANIC_DATA;
//
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor c = db.rawQuery(selectQuery, null);
//
//            //Log.d("**** CURSOR ****", "##### PANIC DATA #####" + DatabaseUtils.dumpCursorToString(c));
//
//            // looping through all rows and adding to list
//            if (c.moveToFirst()) {
//                do {
//                    PanicDataBean panicDataBean = new PanicDataBean();
//                    panicDataBean.setId(c.getInt((c.getColumnIndex(C_PANIC_DATA_ID))));
//                    panicDataBean.setTripid(c.getLong((c.getColumnIndex(C_PANIC_DATA_TRIP_ID))));
//                    panicDataBean.setFilename((c.getString(c.getColumnIndex(C_PANIC_DATA_FILENAME))));
//                    panicDataBean.setFiletype(c.getString(c.getColumnIndex(C_PANIC_DATA_FILETYPE)));
//                    panicDataBean.setCreatedtime(c.getLong(c.getColumnIndex(C_PANIC_DATA_TIMESTAMP)));
//                    panicDataBean.setUploaded(c.getInt(c.getColumnIndex(C_PANIC_DATA_HAS_UPLOADED)));
//
//                    allFilesList.add(panicDataBean);
//
//                } while (c.moveToNext());
//            }
//
//            c.close();
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return allFilesList;
//    }
//
//    /**
//     * Method to fetch all records from panic data table
//     */
//    public List<PanicDataBean> fetchUnuploadedRecordsFromPanicDataTable(){
//        List<PanicDataBean> filesList = new ArrayList<PanicDataBean>();
//        try {
//            String selectQuery = "SELECT * FROM " + TABLE_PANIC_DATA + " WHERE "+ C_PANIC_DATA_HAS_UPLOADED + " = 0" ;
//
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor c = db.rawQuery(selectQuery, null);
//
//            // looping through all rows and adding to list
//            if (c.moveToFirst()) {
//                do {
//                    PanicDataBean panicDataBean = new PanicDataBean();
//                    panicDataBean.setId(c.getInt((c.getColumnIndex(C_PANIC_DATA_ID))));
//                    panicDataBean.setTripid(c.getLong((c.getColumnIndex(C_PANIC_DATA_TRIP_ID))));
//                    panicDataBean.setFilename((c.getString(c.getColumnIndex(C_PANIC_DATA_FILENAME))));
//                    panicDataBean.setFiletype(c.getString(c.getColumnIndex(C_PANIC_DATA_FILETYPE)));
//                    panicDataBean.setCreatedtime(c.getLong(c.getColumnIndex(C_PANIC_DATA_TIMESTAMP)));
//                    panicDataBean.setUploaded(c.getInt(c.getColumnIndex(C_PANIC_DATA_HAS_UPLOADED)));
//
//                    filesList.add(panicDataBean);
//
//                } while (c.moveToNext());
//            }
//
//            c.close();
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return filesList;
//    }
//
//    /**
//     * Method to update uploaded file status in panic data table by passing id
//     */
//    public void updateUploadedPanicDataFileStatus(long id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(C_PANIC_DATA_HAS_UPLOADED, 1);
//
//            int status = db.update(TABLE_PANIC_DATA, values, C_PANIC_DATA_ID + " = ?", new String[]{String.valueOf(id)});
//
//            values.clear();
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        db.close();
//    }
//
//    /**
//     * Method to get count of unuploaded panic data files
//     */
//    public int getUnuploadedPanicDataFilesCount() {
//        int noOfEvents = 0;
//        try {
//            String countQuery = "SELECT * FROM " + TABLE_PANIC_DATA + " WHERE " + C_PANIC_DATA_HAS_UPLOADED + " = 0";
//
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(countQuery, null);
//
//            if (cursor != null) {
//                cursor.moveToFirst();
//                noOfEvents = cursor.getCount();
//                cursor.close();
//            }
//            db.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return noOfEvents;
//    }
}
