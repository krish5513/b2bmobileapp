package com.rightclickit.b2bsaleon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.rightclickit.b2bsaleon.beanclass.ProductsObj;

import java.util.ArrayList;
import java.util.HashMap;
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

    // UserDetails Table - This table contains all user specific details
    private final String TABLE_USERDETAILS = "userdetails";

    // Routes Details Table - This table contains all Routes details
    private final String TABLE_ROUTESDETAILS = "routesdetails";

    // User previleges - User Activity table
    private final String TABLE_PREVILEGES_USER_ACTIVITY = "user_activity";

    //Products Table -This table contains all products details
    public static final String TABLE_PRODUCTS = "products";

    // Column names for User Table
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
    private final String KEY_ROUTEIDS = "route_ids";
    private final String KEY_DEVICE_UDID = "device_udid";
    private final String KEY_VEHICLE_NUMBER = "vehicle_number";
    private final String KEY_TRANSPORTER_NAME = "transporter_name";
    private final String KEY_LATITUDE = "latitude";
    private final String KEY_LONGITUDE = "longitude";

    // Column names for Routes  Table
    private final String KEY_ROUTE_ID = "route_id";
    private final String KEY_ROUTE_NAME = "route_name";
    private final String KEY_REGION_NAME = "region_name";
    private final String KEY_OFFICE_NAME = "office_name";

    // Column names for User activity  Table
    private final String KEY_USER_ACTIVITY_ID = "user_activity_id";
    private final String KEY_USER_ACTIVITY_USER_ID = "user_activity_id";
    private final String KEY_USER_ACTIVITY_TAG = "user_activity_tag";
    private final String KEY_USER_ACTIVITY_STATUS = "user_activity_status";

    //Column names for Produts activity Table
    public static String KEY_ID = "id";
    public static String KEY_SYNC_STATUS = "syncStatus";
    public static String KEY_MATERIAL_CODE = "materialCode";
    public static String KEY_MATERIAL_DISC = "materialDisc";
    public static String KEY_MATERIAL_UNIT_DISC = "materialUnitDisc";
    public static String KEY_MATERIAL_MRP = "materialUnitMRP";
    public static String KEY_MATERIAL_MOQ = "materialMOQ";
    public static String KEY_MATERIAL_TAX = "materialTAX";
    public static String KEY_MATERIAL_TAXType = "materialTAXType";
    public static String KEY_MATERIAL_SP = "materialUnitSP";
    public static String KEY_MATERIAL_VALIDFROM = "materialValidFrom";
    public static String KEY_MATERIAL_VALIDTO = "materialValidTo";
    public static String KEY_MATERIAL_IMAGE = "materialImage";
    public static String KEY_MATERIAL_STATUS = "materialStatus";
    public static String KEY_MATERIAL_RETURNABLE = "materialReturnable";


    // Userdetails Table Create Statements
    private final String CREATE_TABLE_USERDETAILS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USERDETAILS + "(" + KEY_USER_ID + " VARCHAR,"
            + KEY_USER_CODE + " VARCHAR," + KEY_NAME + " VARCHAR," + KEY_EMAIL + " VARCHAR,"
            + KEY_PHONE_NUMBER + " VARCHAR," + KEY_AVATAR + " VARCHAR,"
            + KEY_STAKEHOLDER_ID + " VARCHAR," + KEY_ADRESS + " VARCHAR," + KEY_DEVICE_SYNC
            + " VARCHAR, " + KEY_ACCESS_DEVICE
            + " VARCHAR, " + KEY_BACKUP + " VARCHAR, " + KEY_ROUTEIDS + " VARCHAR, " + KEY_DEVICE_UDID
            + " VARCHAR, " + KEY_VEHICLE_NUMBER  + " VARCHAR, " + KEY_TRANSPORTER_NAME  + " VARCHAR, " + KEY_LATITUDE
            + " VARCHAR, " + KEY_LONGITUDE + " VARCHAR)";

    // Routes Table Create Statements
    private final String CREATE_TABLE_ROUTES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ROUTESDETAILS + "(" + KEY_ROUTE_ID + " VARCHAR," + KEY_ROUTE_NAME + " VARCHAR,"
            + KEY_REGION_NAME + " VARCHAR," + KEY_OFFICE_NAME + " VARCHAR)";

    // User activity Table Create Statements
    private final String CREATE_TABLE_USER_ACTIVITY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PREVILEGES_USER_ACTIVITY + "(" + KEY_USER_ACTIVITY_ID + " VARCHAR," + KEY_USER_ACTIVITY_USER_ID + " VARCHAR,"
            + KEY_USER_ACTIVITY_TAG + " VARCHAR," + KEY_USER_ACTIVITY_STATUS + " VARCHAR)";
    //Products Table Create Statements
    String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MATERIAL_CODE + " TEXT,"
            + KEY_MATERIAL_DISC + " TEXT," + KEY_MATERIAL_UNIT_DISC + " TEXT," + KEY_MATERIAL_RETURNABLE + " TEXT," +
            KEY_MATERIAL_MOQ + " TEXT," + KEY_MATERIAL_TAX + " TEXT," + KEY_MATERIAL_TAXType + " TEXT," + KEY_MATERIAL_MRP + " TEXT," + KEY_MATERIAL_SP + " TEXT," +
            KEY_MATERIAL_VALIDFROM + " TEXT," + KEY_MATERIAL_VALIDTO + " TEXT," + KEY_MATERIAL_IMAGE + " TEXT," + KEY_MATERIAL_STATUS + " TEXT" +  "," + KEY_SYNC_STATUS + " INTEGER"+")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_USERDETAILS);
            db.execSQL(CREATE_TABLE_ROUTES);
            db.execSQL(CREATE_PRODUCTS_TABLE);
            //   db.execSQL(CREATE_TABLE_USER_ACTIVITY);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USERDETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ROUTES);
            db.execSQL("DROP TABLE IF EXISTS"  +CREATE_PRODUCTS_TABLE);
            //  db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USER_ACTIVITY);

            // create new tables
            onCreate(db);
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
    public void insertUserDetails(String id, String userCode, String userName, String email, String phone, String profilrPic, String stakeHolder, String address, String deviceSync, String accessDevice, String backUp,String routeArrayListString,
                                  String deviceId,String transporterName,String vehicleNumber,String latitude,String longitude) {
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
            values.put(KEY_ROUTEIDS,routeArrayListString);
            values.put(KEY_DEVICE_UDID,deviceId);
            values.put(KEY_TRANSPORTER_NAME,transporterName);
            values.put(KEY_VEHICLE_NUMBER,vehicleNumber);
            values.put(KEY_LATITUDE,latitude);
            values.put(KEY_LONGITUDE,longitude);

            // insert row
            db.insert(TABLE_USERDETAILS, null, values);
            values.clear();
        } catch (Exception e){
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to get the users data
     * @return user data in list form.
     */
    public HashMap<String,String> getUsersData(){
        HashMap<String,String> userData = new HashMap<String,String>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_USERDETAILS;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    userData.put(KEY_USER_ID,(c.getString(c.getColumnIndex(KEY_USER_ID))));
                    userData.put(KEY_NAME,(c.getString(c.getColumnIndex(KEY_NAME))));
                    userData.put(KEY_EMAIL,(c.getString(c.getColumnIndex(KEY_EMAIL))));
                    userData.put(KEY_PHONE_NUMBER,(c.getString(c.getColumnIndex(KEY_PHONE_NUMBER))));
                    userData.put(KEY_ADRESS,(c.getString(c.getColumnIndex(KEY_ADRESS))));
                    userData.put(KEY_ACCESS_DEVICE,(c.getString(c.getColumnIndex(KEY_ACCESS_DEVICE))));
                    userData.put(KEY_DEVICE_SYNC,(c.getString(c.getColumnIndex(KEY_DEVICE_SYNC))));
                    userData.put(KEY_BACKUP,(c.getString(c.getColumnIndex(KEY_BACKUP))));
                    userData.put(KEY_ROUTEIDS,(c.getString(c.getColumnIndex(KEY_ROUTEIDS))));
                    userData.put(KEY_DEVICE_UDID,(c.getString(c.getColumnIndex(KEY_DEVICE_UDID))));
                    userData.put(KEY_TRANSPORTER_NAME,(c.getString(c.getColumnIndex(KEY_TRANSPORTER_NAME))));
                    userData.put(KEY_VEHICLE_NUMBER,(c.getString(c.getColumnIndex(KEY_VEHICLE_NUMBER))));
                    userData.put(KEY_LATITUDE,(c.getString(c.getColumnIndex(KEY_LATITUDE))));
                    userData.put(KEY_LONGITUDE,(c.getString(c.getColumnIndex(KEY_LONGITUDE))));

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return userData;
    }

    /**
     * Method to get count of the user details table
     */
    public int getUserDetailsTableCount() {
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
     * Method to get the user id
     * @return route id
     */
    public int getUserId(String emailId){
        int routeId = 0;
        try {
            String query = "SELECT  * FROM " + TABLE_USERDETAILS+ " WHERE "+ KEY_EMAIL +" = " + "'"+emailId+"'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    routeId = Integer.parseInt(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return routeId;
    }

    /**
     * Method to get the user device id
     * @return route id
     */
    public String getUserDeviceId(String emailId){
        String deviceId = "";
        try {
            String query = "SELECT  * FROM " + TABLE_USERDETAILS+ " WHERE "+ KEY_EMAIL +" = " + "'"+emailId+"'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    deviceId = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_UDID));
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return deviceId;
    }

    /**
     * Method to clear values in user details table
     */
    public void deleteValuesFromUserDetailsTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_USERDETAILS, null, null);
            // db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='" + TABLE_USERDETAILS + "'");
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method to insert user details
     *
     * @param routeId
     * @param routeName
     */
    public void insertRoutesDetails(String routeId, String routeName,String regionName,String officeName) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ROUTE_ID, routeId);
            values.put(KEY_ROUTE_NAME, routeName);
            values.put(KEY_REGION_NAME, regionName);
            values.put(KEY_OFFICE_NAME, officeName);

            // insert row
            db.insert(TABLE_ROUTESDETAILS, null, values);
            System.out.println("F*********** INSERTED***************88");
            values.clear();
        } catch (Exception e){
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to get the routes data
     * @return list of routes data
     */
    public List<String> getRoutesMasterData(){
        List<String> routesMasterData = new ArrayList<String>();
        String dbValue = "";
        System.out.println("Routes Data Size::: "+routesMasterData.size());
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_ROUTESDETAILS;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    System.out.println("Routes Data Size::: ====== ==== ===== ====== ===== ====");
                    System.out.println("Routes Data Size::: ====== ==== ===== ====== ===== ===="+c.getString(0));
                    System.out.println("Routes Data Size::: ====== ==== ===== ====== ===== ===="+c.getString(1));
                    System.out.println("Routes Data Size::: ====== ==== ===== ====== ===== ===="+c.getString(2));
                    System.out.println("Routes Data Size::: ====== ==== ===== ====== ===== ===="+c.getString(3));
                    routesMasterData.add((c.getString(c.getColumnIndex(KEY_ROUTE_ID))));
                    routesMasterData.add((c.getString(c.getColumnIndex(KEY_ROUTE_NAME))));
                    routesMasterData.add((c.getString(c.getColumnIndex(KEY_REGION_NAME))));
                    routesMasterData.add((c.getString(c.getColumnIndex(KEY_OFFICE_NAME))));

                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Routes Data Final Size::: "+routesMasterData.size());
        return routesMasterData;
    }

    /**
     * Method to get the route id
     * @return route id
     */
    public String getRouteId(){
        String routeId = "";
        try {
            String query = "SELECT  * FROM " + TABLE_ROUTESDETAILS;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    routeId = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return routeId;
    }

    /**
     * Method to clear values in routes table
     */
    public void deleteValuesFromRoutesTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_ROUTESDETAILS, null, null);
            // db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='" + TABLE_ROUTESDETAILS + "'");
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method to fetch trip by id
     */
    public List<String> getRouteDataByRouteId(String routeId){
        List<String> routeDetailsById = new ArrayList<String>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_ROUTESDETAILS + " WHERE "+ KEY_ROUTE_ID +" = " + "'"+routeId+"'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    routeDetailsById.add((c.getString(c.getColumnIndex(KEY_ROUTE_ID))));
                    routeDetailsById.add((c.getString(c.getColumnIndex(KEY_ROUTE_NAME))));
                    routeDetailsById.add((c.getString(c.getColumnIndex(KEY_REGION_NAME))));
                    routeDetailsById.add((c.getString(c.getColumnIndex(KEY_OFFICE_NAME))));

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("ASDDDDDDDDAD"+routeDetailsById.size());
        return routeDetailsById;
    }

    /**
     * Method to insert the user activity data.
     * @param userActId
     * @param userId
     * @param usetActTag
     * @param userActStatus
     */
    public void insertUserActivityDetails(String userActId, String userId,String usetActTag,String userActStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ACTIVITY_ID, userActId);
            values.put(KEY_USER_ACTIVITY_USER_ID, userId);
            values.put(KEY_USER_ACTIVITY_TAG, usetActTag);
            values.put(KEY_USER_ACTIVITY_STATUS, userActStatus);

            // insert row
            db.insert(TABLE_PREVILEGES_USER_ACTIVITY, null, values);
            System.out.println("F*********** INSERTED***************88");
            values.clear();
        } catch (Exception e){
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to fetch user activity by user id
     */
    public String getUserActivityDetailsByUserId(String userId){
        String userActivitySetupStatus="";
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_PREVILEGES_USER_ACTIVITY + " WHERE "+ KEY_USER_ACTIVITY_USER_ID +" = " + "'"+userId+"'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    userActivitySetupStatus = c.getString(c.getColumnIndex(KEY_USER_ACTIVITY_STATUS));
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return userActivitySetupStatus;
    }










    public void addProducts(ProductsObj productsObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("in prod insert", productsObj.getMaterialDisc());
        ContentValues values = new ContentValues();
        values.put(KEY_MATERIAL_CODE, productsObj.getMaterialCode());
        values.put(KEY_MATERIAL_DISC, productsObj.getMaterialDisc());
        values.put(KEY_MATERIAL_UNIT_DISC, productsObj.getMaterialUnit());
        values.put(KEY_MATERIAL_MOQ, productsObj.getMaterialMOQ());
        values.put(KEY_MATERIAL_TAX, productsObj.getMaterialTAX());
        values.put(KEY_MATERIAL_TAXType, productsObj.getMaterialTAXType());
        values.put(KEY_MATERIAL_MRP, productsObj.getMaterialMRP());
        values.put(KEY_MATERIAL_SP, productsObj.getMaterialSP());
        values.put(KEY_MATERIAL_VALIDFROM, productsObj.getMaterialValidFrom());
        values.put(KEY_MATERIAL_VALIDTO, productsObj.getMaterialValidTo());
        values.put(KEY_MATERIAL_IMAGE, productsObj.getMaterialImage());
        values.put(KEY_MATERIAL_RETURNABLE, productsObj.getIsReturnAble());
        values.put(KEY_SYNC_STATUS, 0);
        // Inserting Row

        try {
            db.insertOrThrow(TABLE_PRODUCTS, null, values);
        } catch (SQLiteConstraintException e) {
            Log.d(TAG, "failure to insert word,", e);
        }
    }


    public List<ProductsObj> getAllProducts() {
        List<ProductsObj> productsObjs = new ArrayList<ProductsObj>();
        // Select All Query

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Log.e("material name", cursor.getString(2));
                ProductsObj productsObj = new ProductsObj();
                productsObj.setMaterialCode(cursor.getString(1));
                productsObj.setMaterialDisc(cursor.getString(2));
                productsObj.setMaterialUnit(cursor.getString(3));
                productsObj.setMaterialMOQ(cursor.getString(5));
                productsObj.setMaterialTAX(cursor.getString(6));
                productsObj.setMaterialTAXType(cursor.getString(7));
                productsObj.setMaterialMRP(cursor.getString(8));
                productsObj.setMaterialSP(cursor.getString(9));
                productsObj.setMaterialValidFrom(cursor.getString(10));
                productsObj.setMaterialValidTo(cursor.getString(11));
                productsObj.setMaterialImage(cursor.getString(12));
                // Adding customer to list
                productsObjs.add(productsObj);
            } while (cursor.moveToNext());
        }

        // return contact list
        return productsObjs;
    }








    public void updateProduct(ProductsObj productsObj, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("in prod update", productsObj.getMaterialDisc());
        ContentValues values = new ContentValues();
        values.put(KEY_MATERIAL_CODE, productsObj.getMaterialCode());
        values.put(KEY_MATERIAL_DISC, productsObj.getMaterialDisc());
        values.put(KEY_MATERIAL_UNIT_DISC, productsObj.getMaterialUnit());
        values.put(KEY_MATERIAL_MOQ, productsObj.getMaterialMOQ());
        values.put(KEY_MATERIAL_TAX, productsObj.getMaterialTAX());
        values.put(KEY_MATERIAL_TAXType, productsObj.getMaterialTAXType());
        values.put(KEY_MATERIAL_MRP, productsObj.getMaterialMRP());
        values.put(KEY_MATERIAL_SP, productsObj.getMaterialSP());
        values.put(KEY_MATERIAL_VALIDFROM, productsObj.getMaterialValidFrom());
        values.put(KEY_MATERIAL_VALIDTO, productsObj.getMaterialValidTo());
        values.put(KEY_MATERIAL_IMAGE, productsObj.getMaterialImage());
        values.put(KEY_MATERIAL_RETURNABLE, productsObj.getIsReturnAble());
        // Inserting Row
        Log.e("code", productsObj.getMaterialCode());
        Log.e("id", String.valueOf(productsObj.getId()));
        try {
//            String query =
//                    "UPDATE "+ TABLE_PRODUCTS + " SET "   + KEY_MATERIAL_DISC + " = \""+productsObj.getMaterialDisc()+"\" WHERE id=\""+productsObj.getId()+"\"";
//            Log.i(TAG, query);
//            db.execSQL(query);
            Log.e("que", KEY_ID + "= '" + productsObj.getId() + "'");
            db.update(TABLE_PRODUCTS, values, KEY_ID + " = '" + id + "'", null);
//            db.update(TABLE_PRODUCTS, values, KEY_ID+" = "+productsObj.getId(),null);
        } catch (Exception e) {
            Log.e("exception", e.toString());

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
