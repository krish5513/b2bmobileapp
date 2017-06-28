package com.rightclickit.b2bsaleon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;

import org.json.JSONArray;
import org.json.JSONObject;

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

    //User Privilege Actions Table -This table contains all user privilege actions details
    public static final String TABLE_USER_PRIVILEGE_DETAILS = "products";

    //Agents table
    public static final String TABLE_AGENTS = "agents";

    // Take Order Products Table
    public static final String TABLE_TO_PRODUCTS = "take_order_products";

    // User previleges actions - User Activity table
    private final String TABLE_PREVILEGE_ACTIONS = "user_privilege_actions";

    // This table contains TDC Customers i.e. either Retailers or Consumers
    private final String TABLE_TDC_CUSTOMERS = "tdc_customers";

    // Column names for User Table
    private final String KEY_USER_ID = "user_id";
    private final String KEY_USER_CODE = "user_code";
    private final String KEY_COMPANYNAME = "companyname";
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
    private final String KEY_PASSWORD = "password";

    // Column names for Routes  Table
    private final String KEY_ROUTE_ID = "route_id";
    private final String KEY_ROUTE_NAME = "route_name";
    private final String KEY_REGION_NAME = "region_name";
    private final String KEY_OFFICE_NAME = "office_name";

    // Column names for User activity  Table
    private final String KEY_USER_ACTIVITY_ID = "user_activity_id";
    private final String KEY_USER_ACTIVITY_USER_ID = "user_activity_user_id";
    private final String KEY_USER_ACTIVITY_TAG = "user_activity_tag";
    private final String KEY_USER_ACTIVITY_STATUS = "user_activity_status";

    //Column names for Produts activity Table
    public final String KEY_PRODUCT_ID = "product_id";
    public final String KEY_PRODUCT_CODE = "product_code";
    public final String KEY_PRODUCT_TITLE = "product_title";
    public final String KEY_PRODUCT_DESCRIPTION = "product_description";
    public final String KEY_PRODUCT_IMAGE_URL = "product_image_url";
    public final String KEY_PRODUCT_RETURNABLE = "product_returnable";
    public final String KEY_PRODUCT_MOQ = "product_moq";
    public final String KEY_PRODUCT_AGENT_PRICE = "product_agent_price";
    public final String KEY_PRODUCT_CONSUMER_PRICE = "product_consumer_price";
    public final String KEY_PRODUCT_RETAILER_PRICE = "product_retailer_price";
    public final String KEY_PRODUCT_GST_PRICE = "product_gst_price";
    public final String KEY_PRODUCT_VAT_PRICE = "product_vat_price";


    // Column names for Agents Table
    private final String KEY_AGENT_ID = "agent_id";
    private final String KEY_AGENT_NAME = "agent_name";
    private final String KEY_OB_AMOUNT = "ob_value";
    private final String KEY_ORDER_VALUE = "order_value";
    private final String KEY_TOTAL_AMOUNT = "total_amount";
    private final String KEY_DUE_AMOUNT = "due_amount";
    private final String KEY_AGENT_PIC = "agent_pic_url";
    private final String KEY_AGENT_STATUS = "agent_status";
    private final String KEY_AGENT_LATITUDE = "agent_latitude";
    private final String KEY_AGENT_LONGITUDE = "agent_longitude";
    private final String KEY_AGENT_CODE = "agent_code";
    private final String KEY_AGENT_FIRSTNAME = "agent_firstname";
    private final String KEY_AGENT_LASTNAME = "agent_lastname";
    private final String KEY_AGENT_PHONENO = "agent_mobile";
    private final String KEY_AGENT_ADDRESS = "agent_address";
    private final String KEY_AGENT_ROUTE_ID = "agent_route_ids";
    private final String KEY_POI_IMAGE = "agent_poi";
    private final String KEY_POA_IMAGE = "agent_poa";
    private final String KEY_AGENT_EMAIL = "agent_email";
    private final String KEY_AGENT_PASSWORD = "agent_password";
    private final String KEY_AGENT_STAKEHOLDERID = "agent_stakeid";
    private final String KEY_AGENT_REPORTINGTO = "agent_reportingto";
    private final String KEY_AGENT_VERIFYCODE = "agent_verifycode";
    private final String KEY_AGENT_DELETE = "agent_dele";
    private final String KEY_AGENT_CREATEDBY = "agent_createdby";
    private final String KEY_AGENT_CREATEDON = "agent_createdon";
    private final String KEY_AGENT_UPDATEDBY = "agent_updatedby";
    private final String KEY_AGENT_UPDATEDON = "agent_updatedon";


    // Column names for Products with take order values
    private final String KEY_TO_PRODUCT_ID = "to_product_id";
    private final String KEY_TO_PRODUCT_NAME = "to_product_name";
    private final String KEY_TO_PRODUCT_ROUTE_ID = "to_product_route_id";
    private final String KEY_TO_FROM_DATE = "to_from_date";
    private final String KEY_TO_TO_DATE = "to_to_date";
    private final String KEY_TO_ORDER_TYPE = "to_order_type";
    private final String KEY_TO_QUANTITY = "to_quantity";
    private final String KEY_TO_STATUS = "to_status";
    private final String KEY_TO_ENQID = "to_enquiry_id";
    private final String KEY_TO_AGENTID = "to_agent_id";

    // Column names for User privilege actions  Table
    private final String KEY_USER_PRIVILEGE_ID = "user_privilege_id";
    private final String KEY_USER_PRIVILEGE_ACTION_ID = "user_privilege_action_id";
    private final String KEY_USER_PRIVILEGE_ACTION_NAME = "user_privilege_action_name";
    private final String KEY_USER_PRIVILEGE_ACTION_STATUS = "user_privilege_action_status";

    // Column names for TDC Customers Table
    private final String KEY_TDC_CUSTOMER_ID = "tdc_customer_id";
    private final String KEY_TDC_CUSTOMER_TYPE = "tdc_customer_type"; // 0 for consumer & 1 for Retailer
    private final String KEY_TDC_CUSTOMER_NAME = "tdc_customer_name";
    private final String KEY_TDC_CUSTOMER_MOBILE_NO = "tdc_customer_mobile_no";
    private final String KEY_TDC_CUSTOMER_BUSINESS_NAME = "tdc_customer_business_name";
    private final String KEY_TDC_CUSTOMER_ADDRESS = "tdc_customer_address";
    private final String KEY_TDC_CUSTOMER_LAT_LONG = "tdc_customer_lat_long";
    private final String KEY_TDC_CUSTOMER_SHOP_IMAGE = "tdc_customer_shop_image";
    private final String KEY_TDC_CUSTOMER_IS_ACTIVE = "tdc_customer_is_active";
    private final String KEY_TDC_CUSTOMER_UPLOAD_STATUS = "tdc_customer_upload_status";

    // Userdetails Table Create Statements
    private final String CREATE_TABLE_AGENTS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_AGENTS + "(" + KEY_AGENT_ID + " VARCHAR,"
            + KEY_AGENT_NAME + " VARCHAR," + KEY_OB_AMOUNT + " VARCHAR," + KEY_ORDER_VALUE + " VARCHAR,"
            + KEY_TOTAL_AMOUNT + " VARCHAR," + KEY_DUE_AMOUNT + " VARCHAR," + KEY_AGENT_PIC + " VARCHAR,"
            + KEY_AGENT_STATUS + " VARCHAR,"
            + KEY_AGENT_LATITUDE + " VARCHAR," + KEY_AGENT_LONGITUDE + " VARCHAR,"
            + KEY_AGENT_CODE + " VARCHAR," + KEY_AGENT_FIRSTNAME + " VARCHAR," + KEY_AGENT_LASTNAME + " VARCHAR,"
            + KEY_AGENT_PHONENO + " VARCHAR," + KEY_AGENT_ADDRESS + " VARCHAR," + KEY_AGENT_ROUTE_ID + " VARCHAR,"
            + KEY_POI_IMAGE + " VARCHAR," + KEY_POA_IMAGE + " VARCHAR,"
            + KEY_AGENT_EMAIL + " VARCHAR," + KEY_AGENT_PASSWORD + " VARCHAR,"
            + KEY_AGENT_STAKEHOLDERID + " VARCHAR," + KEY_AGENT_REPORTINGTO + " VARCHAR," + KEY_AGENT_VERIFYCODE + " VARCHAR,"
            + KEY_AGENT_DELETE + " VARCHAR," + KEY_AGENT_CREATEDBY + " VARCHAR," + KEY_AGENT_CREATEDON + " VARCHAR,"
            + KEY_AGENT_UPDATEDBY + " VARCHAR," + KEY_AGENT_UPDATEDON + " VARCHAR)";


    // Userdetails Table Create Statements
    private final String CREATE_TABLE_USERDETAILS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USERDETAILS + "(" + KEY_USER_ID + " VARCHAR,"
            + KEY_USER_CODE + " VARCHAR," + KEY_COMPANYNAME + " VARCHAR," + KEY_NAME + " VARCHAR," + KEY_EMAIL + " VARCHAR,"
            + KEY_PHONE_NUMBER + " VARCHAR," + KEY_AVATAR + " VARCHAR,"
            + KEY_STAKEHOLDER_ID + " VARCHAR," + KEY_ADRESS + " VARCHAR," + KEY_DEVICE_SYNC
            + " VARCHAR, " + KEY_ACCESS_DEVICE
            + " VARCHAR, " + KEY_BACKUP + " VARCHAR, " + KEY_ROUTEIDS + " VARCHAR, " + KEY_DEVICE_UDID
            + " VARCHAR, " + KEY_VEHICLE_NUMBER + " VARCHAR, " + KEY_TRANSPORTER_NAME + " VARCHAR, " + KEY_LATITUDE
            + " VARCHAR, " + KEY_LONGITUDE
            + " VARCHAR, " + KEY_PASSWORD + " VARCHAR)";

    // Routes Table Create Statements
    private final String CREATE_TABLE_ROUTES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ROUTESDETAILS + "(" + KEY_ROUTE_ID + " VARCHAR," + KEY_ROUTE_NAME + " VARCHAR,"
            + KEY_REGION_NAME + " VARCHAR," + KEY_OFFICE_NAME + " VARCHAR)";

    // User activity Table Create Statements
    private final String CREATE_USER_ACTIVITY_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PREVILEGES_USER_ACTIVITY + "(" + KEY_USER_ACTIVITY_ID + " VARCHAR," + KEY_USER_ACTIVITY_USER_ID + " VARCHAR,"
            + KEY_USER_ACTIVITY_TAG + " VARCHAR," + KEY_USER_ACTIVITY_STATUS + " VARCHAR)";

    //Products Table Create Statements
    private final String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PRODUCTS + "(" + KEY_PRODUCT_ID + " VARCHAR,"
            + KEY_PRODUCT_CODE + " VARCHAR," + KEY_PRODUCT_TITLE + " VARCHAR,"
            + KEY_PRODUCT_DESCRIPTION + " VARCHAR," + KEY_PRODUCT_IMAGE_URL + " VARCHAR," + KEY_PRODUCT_RETURNABLE + " VARCHAR," +
            KEY_PRODUCT_MOQ + " VARCHAR," + KEY_PRODUCT_AGENT_PRICE + " VARCHAR," + KEY_PRODUCT_CONSUMER_PRICE + " VARCHAR,"
            + KEY_PRODUCT_RETAILER_PRICE + " VARCHAR," + KEY_PRODUCT_GST_PRICE + " VARCHAR,"
            + KEY_PRODUCT_VAT_PRICE + " VARCHAR)";

    //TO Products Table Create Statements
    private final String CREATE_PRODUCTS_TABLE_TO = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TO_PRODUCTS + "(" + KEY_TO_PRODUCT_ID + " VARCHAR PRIMARY KEY,"
            + KEY_TO_PRODUCT_NAME + " VARCHAR," + KEY_TO_PRODUCT_ROUTE_ID + " VARCHAR,"
            + KEY_TO_FROM_DATE + " VARCHAR," + KEY_TO_TO_DATE + " VARCHAR," + KEY_TO_ORDER_TYPE + " VARCHAR,"
            + KEY_TO_QUANTITY + " VARCHAR," + KEY_TO_STATUS + " VARCHAR,"
            + KEY_TO_ENQID + " VARCHAR," + KEY_TO_AGENTID + " VARCHAR)";

    // User privilege actions Table Create Statements
    private final String CREATE_USER_PRIVILEGE_ACTIONS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PREVILEGE_ACTIONS + "(" + KEY_USER_PRIVILEGE_ACTION_ID + " VARCHAR," + KEY_USER_PRIVILEGE_ID + " VARCHAR,"
            + KEY_USER_PRIVILEGE_ACTION_NAME + " VARCHAR," + KEY_USER_PRIVILEGE_ACTION_STATUS + " VARCHAR)";

    // TDC Customers Table Create Statement
    private final String CREATE_TDC_CUSTOMERS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TDC_CUSTOMERS + "(" + KEY_TDC_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TDC_CUSTOMER_TYPE + " INTEGER, "
            + KEY_TDC_CUSTOMER_NAME + " VARCHAR, " + KEY_TDC_CUSTOMER_MOBILE_NO + " VARCHAR, " + KEY_TDC_CUSTOMER_BUSINESS_NAME + " VARCHAR, "
            + KEY_TDC_CUSTOMER_ADDRESS + " TEXT, " + KEY_TDC_CUSTOMER_LAT_LONG + " TEXT, " + KEY_TDC_CUSTOMER_SHOP_IMAGE + " VARCHAR, "
            + KEY_TDC_CUSTOMER_IS_ACTIVE + " INTEGER DEFAULT 1, " + KEY_TDC_CUSTOMER_UPLOAD_STATUS + " INTEGER DEFAULT 0)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_USERDETAILS);
            db.execSQL(CREATE_TABLE_ROUTES);
            db.execSQL(CREATE_PRODUCTS_TABLE);
            db.execSQL(CREATE_USER_ACTIVITY_TABLE);
            db.execSQL(CREATE_TABLE_AGENTS);
            db.execSQL(CREATE_PRODUCTS_TABLE_TO);
            db.execSQL(CREATE_USER_PRIVILEGE_ACTIONS_TABLE);
            db.execSQL(CREATE_TDC_CUSTOMERS_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USERDETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ROUTES);
            db.execSQL("DROP TABLE IF EXISTS" + CREATE_PRODUCTS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_USER_ACTIVITY_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_AGENTS);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_PRODUCTS_TABLE_TO);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_USER_PRIVILEGE_ACTIONS_TABLE);

            // create new tables
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get count of the agents table
     */
    public int getAgentsTableCount() {
        int noOfEvents = 0;
        try {
            String countQuery = "SELECT * FROM " + TABLE_AGENTS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                noOfEvents = cursor.getCount();
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfEvents;
    }

    /**
     * Method to clear values in agents table
     */
    public void deleteValuesFromAgentsTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_AGENTS, null, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to insert user details
     *
     * @param mAgentsBeansList
     */
    public void insertAgentDetails(ArrayList<AgentsBean> mAgentsBeansList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mAgentsBeansList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_AGENT_ID, mAgentsBeansList.get(i).getmAgentId());
                values.put(KEY_AGENT_NAME, mAgentsBeansList.get(i).getmAgentName());
                values.put(KEY_OB_AMOUNT, mAgentsBeansList.get(i).getmObAmount());
                values.put(KEY_ORDER_VALUE, mAgentsBeansList.get(i).getmOrderValue());
                values.put(KEY_TOTAL_AMOUNT, mAgentsBeansList.get(i).getmTotalAmount());
                values.put(KEY_DUE_AMOUNT, mAgentsBeansList.get(i).getmDueAmount());
                values.put(KEY_AGENT_PIC, mAgentsBeansList.get(i).getmAgentPic());
                values.put(KEY_AGENT_STATUS, mAgentsBeansList.get(i).getmStatus());
                values.put(KEY_AGENT_LATITUDE, mAgentsBeansList.get(i).getmLatitude());
                values.put(KEY_AGENT_LONGITUDE, mAgentsBeansList.get(i).getmLongitude());
                values.put(KEY_AGENT_CODE, mAgentsBeansList.get(i).getmAgentCode());
                values.put(KEY_AGENT_FIRSTNAME, mAgentsBeansList.get(i).getmFirstname());
                values.put(KEY_AGENT_LASTNAME, mAgentsBeansList.get(i).getmLastname());
                values.put(KEY_AGENT_PHONENO, mAgentsBeansList.get(i).getMphoneNO());
                values.put(KEY_AGENT_ADDRESS, mAgentsBeansList.get(i).getMaddress());
                values.put(KEY_AGENT_ROUTE_ID, mAgentsBeansList.get(i).getmAgentRouteId());
                values.put(KEY_POI_IMAGE, mAgentsBeansList.get(i).getmPoiImage());
                values.put(KEY_POA_IMAGE, mAgentsBeansList.get(i).getmPoaImage());
                values.put(KEY_AGENT_EMAIL, mAgentsBeansList.get(i).getmAgentEmail());
                values.put(KEY_AGENT_PASSWORD, mAgentsBeansList.get(i).getmAgentPassword());
                values.put(KEY_AGENT_STAKEHOLDERID, mAgentsBeansList.get(i).getmAgentStakeid());
                values.put(KEY_AGENT_REPORTINGTO, mAgentsBeansList.get(i).getmAgentReprtingto());
                values.put(KEY_AGENT_VERIFYCODE, mAgentsBeansList.get(i).getmAgentVerifycode());
                values.put(KEY_AGENT_DELETE, mAgentsBeansList.get(i).getmAgentDelete());
                values.put(KEY_AGENT_CREATEDBY, mAgentsBeansList.get(i).getmAgentCreatedBy());
                values.put(KEY_AGENT_CREATEDON, mAgentsBeansList.get(i).getmAgentCreatedOn());
                values.put(KEY_AGENT_UPDATEDBY, mAgentsBeansList.get(i).getmAgentUpdatedBy());
                values.put(KEY_AGENT_UPDATEDON, mAgentsBeansList.get(i).getmAgentUpdatedOn());
                // insert row
                db.insert(TABLE_AGENTS, null, values);
                System.out.println("F*********** INSERTED***************88");
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to fetch all records from agents table
     */
    public ArrayList<AgentsBean> fetchAllRecordsFromAgentsTable() {
        ArrayList<AgentsBean> allDeviceTrackRecords = new ArrayList<AgentsBean>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_AGENTS;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    AgentsBean agentsBean = new AgentsBean();

                    agentsBean.setmAgentId((c.getString(c.getColumnIndex(KEY_AGENT_ID))));
                    agentsBean.setmLatitude((c.getString(c.getColumnIndex(KEY_AGENT_LATITUDE))));
                    agentsBean.setmLongitude((c.getString(c.getColumnIndex(KEY_AGENT_LONGITUDE))));
                    agentsBean.setmAgentName((c.getString(c.getColumnIndex(KEY_AGENT_NAME))));
                    agentsBean.setmObAmount((c.getString(c.getColumnIndex(KEY_OB_AMOUNT))));
                    agentsBean.setmOrderValue((c.getString(c.getColumnIndex(KEY_ORDER_VALUE))));
                    agentsBean.setmTotalAmount((c.getString(c.getColumnIndex(KEY_TOTAL_AMOUNT))));
                    agentsBean.setmDueAmount((c.getString(c.getColumnIndex(KEY_DUE_AMOUNT))));
                    agentsBean.setmAgentCode((c.getString(c.getColumnIndex(KEY_AGENT_CODE))));
                    agentsBean.setmAgentPic((c.getString(c.getColumnIndex(KEY_AGENT_PIC))));
                    agentsBean.setmStatus((c.getString(c.getColumnIndex(KEY_AGENT_STATUS))));
                    agentsBean.setmFirstname((c.getString(c.getColumnIndex(KEY_AGENT_FIRSTNAME))));
                    agentsBean.setmLastname((c.getString(c.getColumnIndex(KEY_AGENT_LASTNAME))));
                    agentsBean.setMphoneNO((c.getString(c.getColumnIndex(KEY_AGENT_PHONENO))));
                    agentsBean.setMaddress((c.getString(c.getColumnIndex(KEY_AGENT_ADDRESS))));
                    agentsBean.setmAgentRouteId((c.getString(c.getColumnIndex(KEY_AGENT_ROUTE_ID))));
                    agentsBean.setmPoiImage((c.getString(c.getColumnIndex(KEY_POI_IMAGE))));
                    agentsBean.setmPoaImage((c.getString(c.getColumnIndex(KEY_POA_IMAGE))));
                    agentsBean.setmAgentEmail((c.getString(c.getColumnIndex(KEY_AGENT_EMAIL))));
                    agentsBean.setmAgentPassword((c.getString(c.getColumnIndex(KEY_AGENT_PASSWORD))));
                    agentsBean.setmAgentStakeid((c.getString(c.getColumnIndex(KEY_AGENT_STAKEHOLDERID))));
                    agentsBean.setmAgentReprtingto((c.getString(c.getColumnIndex(KEY_AGENT_REPORTINGTO))));
                    agentsBean.setmAgentVerifycode((c.getString(c.getColumnIndex(KEY_AGENT_VERIFYCODE))));
                    agentsBean.setmAgentDelete((c.getString(c.getColumnIndex(KEY_AGENT_DELETE))));
                    agentsBean.setmAgentCreatedBy((c.getString(c.getColumnIndex(KEY_AGENT_CREATEDBY))));
                    agentsBean.setmAgentCreatedOn((c.getString(c.getColumnIndex(KEY_AGENT_CREATEDON))));
                    agentsBean.setmAgentUpdatedBy((c.getString(c.getColumnIndex(KEY_AGENT_UPDATEDBY))));
                    agentsBean.setmAgentUpdatedOn((c.getString(c.getColumnIndex(KEY_AGENT_UPDATEDON))));
                    allDeviceTrackRecords.add(agentsBean);
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allDeviceTrackRecords;
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
    public void insertUserDetails(String id, String userCode, String companyname, String userName, String email, String phone, String profilrPic, String stakeHolder, String address, String deviceSync, String accessDevice, String backUp, String routeArrayListString,
                                  String deviceId, String transporterName, String vehicleNumber, String latitude, String longitude, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID, id);
            values.put(KEY_USER_CODE, userCode);
            values.put(KEY_COMPANYNAME, companyname);
            values.put(KEY_NAME, userName);
            values.put(KEY_EMAIL, email.toLowerCase());
            values.put(KEY_PHONE_NUMBER, phone);
            values.put(KEY_AVATAR, profilrPic);
            values.put(KEY_STAKEHOLDER_ID, stakeHolder);
            values.put(KEY_ADRESS, address);
            values.put(KEY_DEVICE_SYNC, deviceSync);
            values.put(KEY_ACCESS_DEVICE, accessDevice);
            values.put(KEY_BACKUP, backUp);
            values.put(KEY_ROUTEIDS, routeArrayListString);
            values.put(KEY_DEVICE_UDID, deviceId);
            values.put(KEY_TRANSPORTER_NAME, transporterName);
            values.put(KEY_VEHICLE_NUMBER, vehicleNumber);
            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_PASSWORD, password);

            // insert row
            db.insert(TABLE_USERDETAILS, null, values);
            values.clear();
            System.out.println("USER DATA INSERTED.....");
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to update user details
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
    public long updateUserDetails(String id, String companyname, String userCode, String userName, String email, String phone, String profilrPic, String stakeHolder, String address, String deviceSync, String accessDevice, String backUp, String routeArrayListString,
                                  String deviceId, String transporterName, String vehicleNumber, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        long effectedRows = 0;
        try {
            ContentValues values = new ContentValues();
            //values.put(KEY_USER_ID, id);
            //values.put(KEY_USER_CODE, userCode);
            values.put(KEY_COMPANYNAME, companyname);
            // values.put(KEY_NAME, userName);
            //values.put(KEY_EMAIL, email);
            // values.put(KEY_PHONE_NUMBER, phone);
            // values.put(KEY_AVATAR, profilrPic);
            // values.put(KEY_STAKEHOLDER_ID, stakeHolder);
            // values.put(KEY_ADRESS, address);
            // values.put(KEY_DEVICE_SYNC, deviceSync);
            // values.put(KEY_ACCESS_DEVICE, accessDevice);
            // values.put(KEY_BACKUP, backUp);
            // values.put(KEY_ROUTEIDS,routeArrayListString);

            values.put(KEY_DEVICE_UDID, deviceId);
            values.put(KEY_TRANSPORTER_NAME, transporterName);
            values.put(KEY_VEHICLE_NUMBER, vehicleNumber);
            //values.put(KEY_LATITUDE,latitude);
            // values.put(KEY_LONGITUDE,longitude);

            // update row
            effectedRows = db.update(TABLE_USERDETAILS, values, KEY_USER_ID + " = ?", new String[]{String.valueOf(id)});
            values.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return effectedRows;
    }

    /**
     * Method to get the users data
     *
     * @return user data in list form.
     */
    public HashMap<String, String> getUsersData() {
        HashMap<String, String> userData = new HashMap<String, String>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_USERDETAILS;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    userData.put(KEY_USER_ID, (c.getString(c.getColumnIndex(KEY_USER_ID))));
                    userData.put(KEY_COMPANYNAME, (c.getString(c.getColumnIndex(KEY_COMPANYNAME))));
                    userData.put(KEY_NAME, (c.getString(c.getColumnIndex(KEY_NAME))));
                    userData.put(KEY_EMAIL, (c.getString(c.getColumnIndex(KEY_EMAIL))));
                    userData.put(KEY_PHONE_NUMBER, (c.getString(c.getColumnIndex(KEY_PHONE_NUMBER))));
                    userData.put(KEY_AVATAR, (c.getString(c.getColumnIndex(KEY_AVATAR))));
                    userData.put(KEY_ADRESS, (c.getString(c.getColumnIndex(KEY_ADRESS))));
                    userData.put(KEY_ACCESS_DEVICE, (c.getString(c.getColumnIndex(KEY_ACCESS_DEVICE))));
                    userData.put(KEY_DEVICE_SYNC, (c.getString(c.getColumnIndex(KEY_DEVICE_SYNC))));
                    userData.put(KEY_BACKUP, (c.getString(c.getColumnIndex(KEY_BACKUP))));
                    userData.put(KEY_ROUTEIDS, (c.getString(c.getColumnIndex(KEY_ROUTEIDS))));
                    userData.put(KEY_DEVICE_UDID, (c.getString(c.getColumnIndex(KEY_DEVICE_UDID))));
                    userData.put(KEY_TRANSPORTER_NAME, (c.getString(c.getColumnIndex(KEY_TRANSPORTER_NAME))));
                    userData.put(KEY_VEHICLE_NUMBER, (c.getString(c.getColumnIndex(KEY_VEHICLE_NUMBER))));
                    userData.put(KEY_LATITUDE, (c.getString(c.getColumnIndex(KEY_LATITUDE))));
                    userData.put(KEY_LONGITUDE, (c.getString(c.getColumnIndex(KEY_LONGITUDE))));
                    userData.put(KEY_PASSWORD, (c.getString(c.getColumnIndex(KEY_PASSWORD))));

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userData;
    }

    /**
     * Method to get the users route ids data
     *
     * @return user data in list form.
     */
    public HashMap<String, String> getUserRouteIds() {
        HashMap<String, String> userData = new HashMap<String, String>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_USERDETAILS;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    userData.put(KEY_USER_ID, (c.getString(c.getColumnIndex(KEY_USER_ID))));
                    userData.put(KEY_ROUTEIDS, (c.getString(c.getColumnIndex(KEY_ROUTEIDS))));

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfEvents;
    }

    /**
     * Method to get the user id
     *
     * @return route id
     */
    public String getUserId(String emailId, String password) {
        int routeId = 0;
        String s = "";
        try {
            String query = "SELECT  * FROM " + TABLE_USERDETAILS + " WHERE " + KEY_EMAIL + " = " + "'" + emailId + "'" + " AND " + KEY_PASSWORD + " = " + "'" + password + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    s = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("F:::" + s);
        return s;
    }

    /**
     * Method to get the user device id
     *
     * @return route id
     */
    public String getUserDeviceId(String emailId) {
        String deviceId = "";
        try {
            String query = "SELECT  * FROM " + TABLE_USERDETAILS + " WHERE " + KEY_EMAIL + " = " + "'" + emailId + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    deviceId = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_UDID));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to insert user details
     *
     * @param routeId
     * @param routeName
     */
    public void insertRoutesDetails(String routeId, String routeName, String regionName, String officeName) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to get the routes data
     *
     * @return list of routes data
     */
    public List<String> getRoutesMasterData() {
        List<String> routesMasterData = new ArrayList<String>();
        String dbValue = "";
        System.out.println("Routes Data Size::: " + routesMasterData.size());
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_ROUTESDETAILS;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    System.out.println("Routes Data Size::: ====== ==== ===== ====== ===== ====");
                    System.out.println("Routes Data Size::: ====== ==== ===== ====== ===== ====" + c.getString(0));
                    System.out.println("Routes Data Size::: ====== ==== ===== ====== ===== ====" + c.getString(1));
                    System.out.println("Routes Data Size::: ====== ==== ===== ====== ===== ====" + c.getString(2));
                    System.out.println("Routes Data Size::: ====== ==== ===== ====== ===== ====" + c.getString(3));
                    routesMasterData.add((c.getString(c.getColumnIndex(KEY_ROUTE_ID))));
                    routesMasterData.add((c.getString(c.getColumnIndex(KEY_ROUTE_NAME))));
                    routesMasterData.add((c.getString(c.getColumnIndex(KEY_REGION_NAME))));
                    routesMasterData.add((c.getString(c.getColumnIndex(KEY_OFFICE_NAME))));

                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Routes Data Final Size::: " + routesMasterData.size());
        return routesMasterData;
    }

    /**
     * Method to get the route id
     *
     * @return route id
     */
    public String getRouteId() {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to fetch trip by id
     */
    public List<String> getRouteDataByRouteId(String routeId) {
        List<String> routeDetailsById = new ArrayList<String>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_ROUTESDETAILS + " WHERE " + KEY_ROUTE_ID + " = " + "'" + routeId + "'";

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ASDDDDDDDDAD" + routeDetailsById.size());
        return routeDetailsById;
    }

    /**
     * Method to insert the user activity data.
     *
     * @param userId
     * @param status
     * @param idsList
     * @param nameslist
     */
    public void insertUserActivityDetails(String userId, String status, ArrayList<String> idsList, ArrayList<String> nameslist) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < idsList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_USER_ACTIVITY_ID, idsList.get(i).toString());
                values.put(KEY_USER_ACTIVITY_USER_ID, userId);
                values.put(KEY_USER_ACTIVITY_TAG, nameslist.get(i).toString());
                values.put(KEY_USER_ACTIVITY_STATUS, status);

                // insert row
                db.insert(TABLE_PREVILEGES_USER_ACTIVITY, null, values);
                System.out.println("F*********** INSERTED***************88");
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to fetch user activity by user id
     */
    public ArrayList<String> getUserActivityDetailsByUserId(String userId) {
        String userActivitySetupStatus = "";
        ArrayList<String> userPrivileges = new ArrayList<String>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_PREVILEGES_USER_ACTIVITY + " WHERE " + KEY_USER_ACTIVITY_USER_ID + " = " + "'" + userId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    userPrivileges.add(c.getString(c.getColumnIndex(KEY_USER_ACTIVITY_TAG)));
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("***COUNT === " + userPrivileges.size());
        return userPrivileges;
    }


    /**
     * Method to get count of the user privileges details table
     */
    public int getUserPrivilegesTableCount() {
        int noOfEvents = 0;
        try {
            String countQuery = "SELECT * FROM " + TABLE_PREVILEGES_USER_ACTIVITY;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                noOfEvents = cursor.getCount();
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfEvents;
    }

    /**
     * Method to clear values in user activity table
     */
    public void deleteValuesFromUserActivityTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_PREVILEGES_USER_ACTIVITY, null, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method to insert the user activity actions data.
     *
     * @param idsList
     * @param actionslist
     */
    public void insertUserActivityActionsDetails(ArrayList<String> idsList, HashMap<String, Object> actionslist) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (String actionId : idsList) {
                JSONArray ja = (JSONArray) actionslist.get(actionId);
                for (int g = 0; g < ja.length(); g++) {
                    JSONObject j = ja.getJSONObject(g);
                    ContentValues values = new ContentValues();
                    values.put(KEY_USER_PRIVILEGE_ID, actionId);
                    if (j.has("_id")) {
                        values.put(KEY_USER_PRIVILEGE_ACTION_ID, j.getString("_id"));
                    } else {
                        values.put(KEY_USER_PRIVILEGE_ACTION_ID, "");
                    }
                    if (j.has("tag")) {
                        values.put(KEY_USER_PRIVILEGE_ACTION_NAME, j.getString("tag"));
                    } else {
                        values.put(KEY_USER_PRIVILEGE_ACTION_NAME, "");
                    }
                    values.put(KEY_USER_PRIVILEGE_ACTION_STATUS, "A");

                    // insert row
                    db.insert(TABLE_PREVILEGE_ACTIONS, null, values);
                    System.out.println("F*********** ACTIONS INSERTED***************88");
                    values.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to fetch user activity by user id
     */
    public ArrayList<String> getUserActivityActionsDetailsByPrivilegeId(String privilegeId) {
        String userActivitySetupStatus = "";
        ArrayList<String> userPrivileges = new ArrayList<String>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_PREVILEGE_ACTIONS + " WHERE " + KEY_USER_PRIVILEGE_ID + " = '" + privilegeId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    userPrivileges.add(c.getString(c.getColumnIndex(KEY_USER_PRIVILEGE_ACTION_NAME)));
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("======== User Privileges === " + userPrivileges);
        return userPrivileges;
    }


    /**
     * Method to get count of the user privileges details table
     */
    public int getUserPrivilegesActionsTableCount() {
        int noOfEvents = 0;
        try {
            String countQuery = "SELECT * FROM " + TABLE_PREVILEGE_ACTIONS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                noOfEvents = cursor.getCount();
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfEvents;
    }

    /**
     * Method to clear values in user activity table
     */
    public void deleteValuesFromUserActivityActionsTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_PREVILEGE_ACTIONS, null, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get count of the user privileges details table
     */
    public int getProductsTableCount() {
        int noOfEvents = 0;
        try {
            String countQuery = "SELECT * FROM " + TABLE_PRODUCTS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                noOfEvents = cursor.getCount();
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfEvents;
    }

    /**
     * Method to insert product details
     */

    public void insertProductDetails(ArrayList<ProductsBean> mProductsBeansList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mProductsBeansList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_PRODUCT_ID, mProductsBeansList.get(i).getProductId());
                values.put(KEY_PRODUCT_CODE, mProductsBeansList.get(i).getProductCode());
                values.put(KEY_PRODUCT_TITLE, mProductsBeansList.get(i).getProductTitle());
                values.put(KEY_PRODUCT_DESCRIPTION, mProductsBeansList.get(i).getProductDescription());
                values.put(KEY_PRODUCT_IMAGE_URL, mProductsBeansList.get(i).getProductImageUrl());
                values.put(KEY_PRODUCT_RETURNABLE, mProductsBeansList.get(i).getProductReturnable());
                values.put(KEY_PRODUCT_MOQ, mProductsBeansList.get(i).getProductMOQ());
                values.put(KEY_PRODUCT_AGENT_PRICE, mProductsBeansList.get(i).getProductAgentPrice());
                values.put(KEY_PRODUCT_CONSUMER_PRICE, mProductsBeansList.get(i).getProductConsumerPrice());
                values.put(KEY_PRODUCT_RETAILER_PRICE, mProductsBeansList.get(i).getProductRetailerPrice());
                values.put(KEY_PRODUCT_GST_PRICE, mProductsBeansList.get(i).getProductgst());
                values.put(KEY_PRODUCT_VAT_PRICE, mProductsBeansList.get(i).getProductvat());

                // insert row
                db.insert(TABLE_PRODUCTS, null, values);
                System.out.println("F*********** INSERTED***************88");
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to fetch all records from products table
     */
    public ArrayList<ProductsBean> fetchAllRecordsFromProductsTable() {
        ArrayList<ProductsBean> allProductTrackRecords = new ArrayList<ProductsBean>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    ProductsBean productsBean = new ProductsBean();

                    productsBean.setProductId((c.getString(c.getColumnIndex(KEY_PRODUCT_ID))));
                    productsBean.setProductCode((c.getString(c.getColumnIndex(KEY_PRODUCT_CODE))));
                    productsBean.setProductTitle((c.getString(c.getColumnIndex(KEY_PRODUCT_TITLE))));
                    productsBean.setProductDescription((c.getString(c.getColumnIndex(KEY_PRODUCT_DESCRIPTION))));
                    productsBean.setProductImageUrl((c.getString(c.getColumnIndex(KEY_PRODUCT_IMAGE_URL))));
                    productsBean.setProductReturnable((c.getString(c.getColumnIndex(KEY_PRODUCT_RETURNABLE))));
                    productsBean.setProductMOQ((c.getString(c.getColumnIndex(KEY_PRODUCT_MOQ))));
                    productsBean.setProductAgentPrice((c.getString(c.getColumnIndex(KEY_PRODUCT_AGENT_PRICE))));
                    productsBean.setProductConsumerPrice((c.getString(c.getColumnIndex(KEY_PRODUCT_CONSUMER_PRICE))));
                    productsBean.setProductRetailerPrice((c.getString(c.getColumnIndex(KEY_PRODUCT_RETAILER_PRICE))));
                    productsBean.setProductgst((c.getString(c.getColumnIndex(KEY_PRODUCT_GST_PRICE))));
                    productsBean.setProductvat((c.getString(c.getColumnIndex(KEY_PRODUCT_VAT_PRICE))));

                    allProductTrackRecords.add(productsBean);
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allProductTrackRecords;
    }

    /**
     * Method to insert take order product details
     */

    public void insertTakeOrderProductDetails(ArrayList<TakeOrderBean> mProductsBeansList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mProductsBeansList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_TO_PRODUCT_ID, mProductsBeansList.get(i).getmProductId());
                values.put(KEY_TO_PRODUCT_NAME, mProductsBeansList.get(i).getmProductTitle());
                values.put(KEY_TO_PRODUCT_ROUTE_ID, mProductsBeansList.get(i).getmRouteId());
                values.put(KEY_TO_FROM_DATE, mProductsBeansList.get(i).getmProductFromDate());
                values.put(KEY_TO_TO_DATE, mProductsBeansList.get(i).getmProductToDate());
                values.put(KEY_TO_ORDER_TYPE, mProductsBeansList.get(i).getmProductOrderType());
                values.put(KEY_TO_QUANTITY, mProductsBeansList.get(i).getmProductQuantity());
                values.put(KEY_TO_STATUS, mProductsBeansList.get(i).getmProductStatus());
                values.put(KEY_TO_ENQID, mProductsBeansList.get(i).getmEnquiryId());
                values.put(KEY_TO_AGENTID, mProductsBeansList.get(i).getmAgentId());

                // insert row
                db.insert(TABLE_TO_PRODUCTS, null, values);
                System.out.println("*********** INSERTED***************9999");
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to fetch all records from take products table
     *
     * @param isSync
     */
    public ArrayList<TakeOrderBean> fetchAllRecordsFromTakeOrderProductsTable(String isSync) {
        ArrayList<TakeOrderBean> allProductTrackRecords = new ArrayList<TakeOrderBean>();
        try {
            if (allProductTrackRecords.size() > 0) {
                allProductTrackRecords.clear();
            }
            String selectQuery = "";
            if (isSync.equals("yes")) {
                selectQuery = "SELECT  * FROM " + TABLE_TO_PRODUCTS + " WHERE " + KEY_TO_STATUS + " = " + "1";
            } else {
                selectQuery = "SELECT  * FROM " + TABLE_TO_PRODUCTS;
            }

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    TakeOrderBean toBean = new TakeOrderBean();

                    toBean.setmProductId((c.getString(c.getColumnIndex(KEY_TO_PRODUCT_ID))));
                    toBean.setmRouteId((c.getString(c.getColumnIndex(KEY_TO_PRODUCT_ROUTE_ID))));
                    toBean.setmProductTitle((c.getString(c.getColumnIndex(KEY_TO_PRODUCT_NAME))));
                    toBean.setmProductFromDate((c.getString(c.getColumnIndex(KEY_TO_FROM_DATE))));
                    toBean.setmProductToDate((c.getString(c.getColumnIndex(KEY_TO_TO_DATE))));
                    toBean.setmProductOrderType((c.getString(c.getColumnIndex(KEY_TO_ORDER_TYPE))));
                    toBean.setmProductQuantity((c.getString(c.getColumnIndex(KEY_TO_QUANTITY))));
                    toBean.setmProductStatus((c.getString(c.getColumnIndex(KEY_TO_STATUS))));
                    toBean.setmEnquiryId((c.getString(c.getColumnIndex(KEY_TO_ENQID))));
                    toBean.setmAgentId((c.getString(c.getColumnIndex(KEY_TO_AGENTID))));


                    allProductTrackRecords.add(toBean);
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allProductTrackRecords;
    }

    /**
     * Method to update take order details...
     *
     * @param takeOrderBeanArrayList
     * @return
     */
    public long updateTakeOrderDetails(ArrayList<TakeOrderBean> takeOrderBeanArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        long effectedRows = 0;
        try {
            for (int b = 0; b < takeOrderBeanArrayList.size(); b++) {
                ContentValues values = new ContentValues();
                // values.put(KEY_TO_PRODUCT_ID, mProductsBeansList.get(i).getmProductId());
                values.put(KEY_TO_PRODUCT_NAME, takeOrderBeanArrayList.get(b).getmProductTitle());
                values.put(KEY_TO_PRODUCT_ROUTE_ID, takeOrderBeanArrayList.get(b).getmRouteId());
                values.put(KEY_TO_FROM_DATE, takeOrderBeanArrayList.get(b).getmProductFromDate());
                values.put(KEY_TO_TO_DATE, takeOrderBeanArrayList.get(b).getmProductToDate());
                values.put(KEY_TO_ORDER_TYPE, takeOrderBeanArrayList.get(b).getmProductOrderType());
                values.put(KEY_TO_QUANTITY, takeOrderBeanArrayList.get(b).getmProductQuantity());
                values.put(KEY_TO_STATUS, takeOrderBeanArrayList.get(b).getmProductStatus());
                values.put(KEY_TO_ENQID, takeOrderBeanArrayList.get(b).getmEnquiryId());
                values.put(KEY_TO_AGENTID, takeOrderBeanArrayList.get(b).getmAgentId());

                // update row
                effectedRows = db.update(TABLE_TO_PRODUCTS, values, KEY_TO_PRODUCT_ID + " = ?", new String[]{String.valueOf(takeOrderBeanArrayList.get(b).getmProductId())});
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return effectedRows;
    }

    /**
     * Method to insert the user activity privileges data.
     *
     * @param userId
     * @param status
     * @param idsList
     * @param nameslist
     */
    public void insertUserActivityPrivileges(String userId, String status, ArrayList<String> idsList, ArrayList<String> nameslist) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < idsList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_USER_ACTIVITY_ID, idsList.get(i).toString());
                values.put(KEY_USER_ACTIVITY_USER_ID, userId);
                values.put(KEY_USER_ACTIVITY_TAG, nameslist.get(i).toString());
                values.put(KEY_USER_ACTIVITY_STATUS, status);

                // insert row
                db.insert(TABLE_PREVILEGES_USER_ACTIVITY, null, values);
                System.out.println("F*********** INSERTED***************88");
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to fetch record into TDC Customers Table
     */
    public long insertIntoTDCCustomers(TDCCustomer customer) {
        long customerId = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_TDC_CUSTOMER_ID, customer.getId());
            values.put(KEY_TDC_CUSTOMER_TYPE, customer.getCustomerType());
            values.put(KEY_TDC_CUSTOMER_NAME, customer.getName());
            values.put(KEY_TDC_CUSTOMER_MOBILE_NO, customer.getMobileNo());
            values.put(KEY_TDC_CUSTOMER_BUSINESS_NAME, customer.getBusinessName());
            values.put(KEY_TDC_CUSTOMER_ADDRESS, customer.getAddress());
            values.put(KEY_TDC_CUSTOMER_LAT_LONG, customer.getLatLong());
            values.put(KEY_TDC_CUSTOMER_SHOP_IMAGE, customer.getShopImage());
            values.put(KEY_TDC_CUSTOMER_IS_ACTIVE, customer.getIsActive());
            values.put(KEY_TDC_CUSTOMER_UPLOAD_STATUS, customer.getIsUploaded());

            customerId = db.insert(TABLE_TDC_CUSTOMERS, null, values);

            values.clear();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customerId;
    }

    /**
     * Method to fetch all records from TDC Customers Table
     */
    public List<TDCCustomer> fetchAllRecordsFromTDCCustomers() {
        List<TDCCustomer> allTDCCustomersList = new ArrayList<>();

        try {
            String selectQuery = "SELECT  * FROM " + TABLE_TDC_CUSTOMERS + " WHERE " + KEY_TDC_CUSTOMER_IS_ACTIVE + " = 1";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TDCCustomer customer = new TDCCustomer();
                    customer.setId(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_ID)));
                    customer.setCustomerType(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_TYPE)));
                    customer.setName(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_NAME)));
                    customer.setMobileNo(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_MOBILE_NO)));
                    customer.setBusinessName(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_BUSINESS_NAME)));
                    customer.setAddress(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_ADDRESS)));
                    customer.setLatLong(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_LAT_LONG)));
                    customer.setShopImage(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_SHOP_IMAGE)));
                    customer.setIsActive(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_IS_ACTIVE)));
                    customer.setIsUploaded(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_UPLOAD_STATUS)));

                    allTDCCustomersList.add(customer);
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allTDCCustomersList;
    }
}
