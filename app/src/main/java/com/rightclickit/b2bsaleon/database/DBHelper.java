package com.rightclickit.b2bsaleon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.beanclass.NotificationBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.SpecialPriceBean;
import com.rightclickit.b2bsaleon.beanclass.StakeHolderTypes;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.beanclass.TDCSalesOrderProductBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetPaymentsBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    // This table contains Special price for the product
    private final String TABLE_SPECIALPRICE = "specialprice";

    // This table contains TDC Sales Orders
    private final String TABLE_TDC_SALES_ORDERS = "tdc_sales_orders";

    // This table contains TDC Sales Order corresponding Products
    private final String TABLE_TDC_SALES_ORDER_PRODUCTS = "tdc_sales_order_products";

    // This table contains Stakeholder type ids
    private final String TABLE_STAKEHOLDER_TYPES = "stake_holder_types";

    // This table contains tripsheets
    private final String TABLE_TRIPSHEETS_LIST = "tripsheets_list";

    // This table contains tripsheets stock list
    private final String TABLE_TRIPSHEETS_STOCK_LIST = "tripsheets_stock_list";

    // This table contains tripsheets deliveries list
    private final String TABLE_TRIPSHEETS_DELIVERIES_LIST = "tripsheets_deliveries_list";

    // This table contains tripsheets returns list
    private final String TABLE_TRIPSHEETS_RETURNS_LIST = "tripsheets_returns_list";

    // This table contains tripsheets payments list
    private final String TABLE_TRIPSHEETS_PAYMENTS_LIST = "tripsheets_payments_list";

    // This table contains tripsheets so list
    private final String TABLE_TRIPSHEETS_SO_LIST = "tripsheets_so_list";

    // This table contains notifications list
    private final String TABLE_NOTIFICATION_LIST = "notification_list";


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
    private final String KEY_ROUTE_CODE = "route_code";

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
    private final String KEY_AGENT_ROUTECODE = "agent_routecode";
    private final String KEY_AGENT_DEVICESYNC = "agent_devicesync";
    private final String KEY_AGENT_ACCESSDEVICE = "agent_accessdevice";
    private final String KEY_AGENT_BACKUP = "agent_backup";

    // Column names for Products with take order values
    private final String KEY_TO_PRODUCT_ID = "to_product_id";
    private final String KEY_TO_PRODUCT_CODE = "to_product_code";
    private final String KEY_TO_PRODUCT_NAME = "to_product_name";
    private final String KEY_TO_PRODUCT_ROUTE_ID = "to_product_route_id";
    private final String KEY_TO_FROM_DATE = "to_from_date";
    private final String KEY_TO_TO_DATE = "to_to_date";
    private final String KEY_TO_ORDER_TYPE = "to_order_type";
    private final String KEY_TO_QUANTITY = "to_quantity";
    private final String KEY_TO_STATUS = "to_status";
    private final String KEY_TO_ENQID = "to_enquiry_id";
    private final String KEY_TO_AGENTID = "to_agent_id";
    private final String KEY_TAKEORDER_DATE = "takeorder_date";
    private final String KEY_PRICE = "price";
    private final String KEY_VAT = "vat";
    private final String KEY_GST = "gst";
    private final String KEY_TO_AGENTCODE = "to_agent_code";


    // Column names for User privilege actions  Table
    private final String KEY_USER_PRIVILEGE_ID = "user_privilege_id";
    private final String KEY_USER_PRIVILEGE_ACTION_ID = "user_privilege_action_id";
    private final String KEY_USER_PRIVILEGE_ACTION_NAME = "user_privilege_action_name";
    private final String KEY_USER_PRIVILEGE_ACTION_STATUS = "user_privilege_action_status";

    // Column names for TDC Customers Table
    private final String KEY_TDC_CUSTOMER_ID = "tdc_customer_id"; // this acts as a primary key & it's auto increment
    private final String KEY_TDC_CUSTOMER_USER_ID = "tdc_customer_user_id"; // this is the mongo db id which we will pull from service & update it in local db
    private final String KEY_TDC_CUSTOMER_TYPE = "tdc_customer_type"; // 0 for consumer & 1 for Retailer
    private final String KEY_TDC_CUSTOMER_NAME = "tdc_customer_name";
    private final String KEY_TDC_CUSTOMER_MOBILE_NO = "tdc_customer_mobile_no";
    private final String KEY_TDC_CUSTOMER_BUSINESS_NAME = "tdc_customer_business_name";
    private final String KEY_TDC_CUSTOMER_ADDRESS = "tdc_customer_address";
    private final String KEY_TDC_CUSTOMER_LATITUDE = "tdc_customer_latitude";
    private final String KEY_TDC_CUSTOMER_LONGITUDE = "tdc_customer_longitude";
    private final String KEY_TDC_CUSTOMER_SHOP_IMAGE = "tdc_customer_shop_image";
    private final String KEY_TDC_CUSTOMER_IS_ACTIVE = "tdc_customer_is_active";
    private final String KEY_TDC_CUSTOMER_UPLOAD_STATUS = "tdc_customer_upload_status";
    private final String KEY_TDC_CUSTOMER_SHOP_IMAGE_UPLOAD_STATUS = "tdc_customer_shop_image_upload_status";

    // Column names for Special price
    private final String KEY_USER_SPECIALID = "userid";
    private final String KEY_PRODUCT_SPECIALID = "productid";
    private final String KEY_SPECIALPRICE = "price";

    // Column names for TDC Sales Orders Table
    private final String KEY_TDC_SALES_ORDER_ID = "tdc_sales_order_id";
    private final String KEY_TDC_SALES_ORDER_NO_OF_ITEMS = "tdc_sales_order_no_of_items";
    private final String KEY_TDC_SALES_ORDER_TOTAL_AMOUNT = "tdc_sales_order_total_amount";
    private final String KEY_TDC_SALES_ORDER_TOTAL_TAX_AMOUNT = "tdc_sales_order_total_tax_amount";
    private final String KEY_TDC_SALES_ORDER_SUB_TOTAL = "tdc_sales_order_sub_total"; // i.e. ORDER TOTAL AMOUNT + ORDER TOTAL TAX AMOUNT
    private final String KEY_TDC_SALES_ORDER_CUSTOMER_ID = "tdc_sales_customer_id";
    private final String KEY_TDC_SALES_ORDER_CUSTOMER_USER_ID = "tdc_sales_customer_user_id"; // i.e. mongo db user id
    private final String KEY_TDC_SALES_ORDER_DATE = "tdc_sales_order_date"; // we are using this column for filtration in tdc sales list like today, weekly or monthly.
    private final String KEY_TDC_SALES_ORDER_CREATED_ON = "tdc_sales_order_created_on"; // this column contains order created date in unix time stamp format
    private final String KEY_TDC_SALES_ORDER_CREATED_BY = "tdc_sales_order_created_by";
    private final String KEY_TDC_SALES_ORDER_UPLOAD_STATUS = "tdc_sale_order_upload_status";

    // Column names for TDC Sales Order Products Table
    private final String KEY_TDC_SOP_ID = "tdc_sop_id";
    private final String KEY_TDC_SOP_ORDER_ID = "tdc_sop_order_id";
    private final String KEY_TDC_SOP_PRODUCT_ID = "tdc_sop_product_id";
    private final String KEY_TDC_SOP_PRODUCT_NAME = "tdc_sop_product_name";
    private final String KEY_TDC_SOP_PRODUCT_MRP = "tdc_sop_product_mrp";
    private final String KEY_TDC_SOP_PRODUCT_QUANTITY = "tdc_sop_product_quantity";
    private final String KEY_TDC_SOP_PRODUCT_AMOUNT = "tdc_sop_product_amount";
    private final String KEY_TDC_SOP_PRODUCT_TAX = "tdc_sop_product_tax";
    private final String KEY_TDC_SOP_PRODUCT_TAX_AMOUNT = "tdc_sop_product_tax_amount";
    private final String KEY_TDC_SOP_UPLOAD_STATUS = "tdc_sop_upload_status";

    // Column names for Stakeholder types
    private final String KEY_STAKEHOLDER_TYPE_ID = "stakeholder_type_id";
    private final String KEY_STAKEHOLDER_TYPE_NAME = "stakeholder_type_name";
    private final String KEY_STAKEHOLDER_TYPE = "stakeholder_type";

    // Column names for Tripsheets List  Table
    private final String KEY_TRIPSHEET_UNIQUE_ID = "tripsheet_unique_id";
    private final String KEY_TRIPSHEET_ID = "tripshhet_id";
    private final String KEY_TRIPSHEET_CODE = "tripshhet_code";
    private final String KEY_TRIPSHEET_DATE = "tripshhet_date";
    private final String KEY_TRIPSHEET_STATUS = "tripsheet_status";
    private final String KEY_TRIPSHEET_OB_AMOUNT = "tripshhet_ob_amount";
    private final String KEY_TRIPSHEET_ORDERED_AMOUNT = "tripshhet_ordered_amount";
    private final String KEY_TRIPSHEET_RECEIVED_AMOUNT = "tripshhet_received_amount";
    private final String KEY_TRIPSHEET_DUE_AMOUNT = "tripshhet_due_amount";
    private final String KEY_TRIPSHEET_ROUTE_CODE = "tripshhet_route_code";
    private final String KEY_TRIPSHEET_SALESMEN_CODE = "tripshhet_salesmen_code";
    private final String KEY_TRIPSHEET_TRANSPORTER_NAME = "tripshhet_transporter_name";
    private final String KEY_TRIPSHEET_VEHICLE_NUMBER = "tripshhet_vehicle_number";
    private final String KEY_TRIPSHEET_VERIFY_STATUS = "tripshhet_verify_status"; // 0 is not verify and 1 is verify

    // Column names for Tripsheets stocks List  Table
    private final String KEY_TRIPSHEET_STOCK_UNIQUE_ID = "tripsheet_stock_unique_id";
    private final String KEY_TRIPSHEET_STOCK_ID = "tripshhet_stock_id";
    private final String KEY_TRIPSHEET_STOCK_TRIPSHEET_ID = "tripshhet_stock_tripsheet_id";
    private final String KEY_TRIPSHEET_STOCK_PRODUCT_CODE = "tripshhet_stock_product_code";
    private final String KEY_TRIPSHEET_STOCK_PRODUCT_NAME = "tripsheet_stock_product_name";
    private final String KEY_TRIPSHEET_STOCK_PRODUCT_ID = "tripshhet_stock_product_id";
    private final String KEY_TRIPSHEET_STOCK_ORDER_QUANTITY = "tripshhet_stock_order_quantity";
    private final String KEY_TRIPSHEET_STOCK_DISPATCH_QUANTITY = "tripshhet_stock_dispatch_quantity";
    private final String KEY_TRIPSHEET_STOCK_DISPATCH_DATE = "tripshhet_stock_dispatch_date";
    private final String KEY_TRIPSHEET_STOCK_DISPATCH_BY = "tripshhet_stock_dispatch_by";
    private final String KEY_TRIPSHEET_STOCK_VERIFY_QUANTITY = "tripshhet_stock_verify_quantity";
    private final String KEY_TRIPSHEET_STOCK_VERIFY_DATE = "tripshhet_stock_verify_date";
    private final String KEY_TRIPSHEET_STOCK_VERIFY_BY = "tripshhet_stock_verify_by";
    private final String KEY_TRIPSHEET_STOCK_IN_STOCK_QUANTITY = "tripshhet_stock_in_stock_quantity";
    private final String KEY_TRIPSHEET_STOCK_EXTRA_QUANTITY = "tripshhet_stock_extra_quantity";
    private final String KEY_TRIPSHEET_STOCK_IS_DISPATCHED = "tripshhet_stock_is_dispatched";
    private final String KEY_TRIPSHEET_STOCK_IS_VERIFIED = "tripshhet_stock_is_verified";
    private final String KEY_TRIPSHEET_STOCK_DISPATCHED_UPLOAD_STATUS = "tripshhet_stock_dispatch_upload_status";
    private final String KEY_TRIPSHEET_STOCK_VERIFIED_UPLOAD_STATUS = "tripshhet_stock_verified_upload_status";

    // Column names for Tripsheets deliveries List  Table
    private final String KEY_TRIPSHEET_DELIVERY_NO = "tripsheet_delivery_no";
    private final String KEY_TRIPSHEET_DELIVERY_TRIP_ID = "tripshhet_delivery_trip_id";
    private final String KEY_TRIPSHEET_DELIVERY_USER_ID = "tripshhet_delivery_user_id";
    private final String KEY_TRIPSHEET_DELIVERY_USER_CODES = "tripshhet_delivery_user_codes";
    private final String KEY_TRIPSHEET_DELIVERY_ROUTE_ID = "tripshhet_delivery_route_id";
    private final String KEY_TRIPSHEET_DELIVERY_ROUTE_CODES = "tripshhet_delivery_route_codes";
    private final String KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS = "tripshhet_delivery_product_ids";
    private final String KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES = "tripshhet_delivery_product_codes";
    private final String KEY_TRIPSHEET_DELIVERY_TAXPERCENT = "tripshhet_delivery_taxpercent";
    private final String KEY_TRIPSHEET_DELIVERY_UNITPRICE = "tripshhet_delivery_unitprice";
    private final String KEY_TRIPSHEET_DELIVERY_QUANTITY = "tripshhet_delivery_quantity";
    private final String KEY_TRIPSHEET_DELIVERY_AMOUNT = "tripshhet_delivery_amount";
    private final String KEY_TRIPSHEET_DELIVERY_TAXAMOUNT = "tripshhet_delivery_taxamount";
    private final String KEY_TRIPSHEET_DELIVERY_TAXTOTAL = "tripshhet_delivery_taxtotal";
    private final String KEY_TRIPSHEET_DELIVERY_SALEVALUE = "tripshhet_delivery_salevalue";
    private final String KEY_TRIPSHEET_DELIVERY_STATUS = "tripshhet_delivery_status";
    private final String KEY_TRIPSHEET_DELIVERY_DELETE = "tripshhet_delivery_delete";
    private final String KEY_TRIPSHEET_DELIVERY_CREATEDBY = "tripshhet_delivery_createdby";
    private final String KEY_TRIPSHEET_DELIVERY_CREATEDON = "tripshhet_delivery_createdon";
    private final String KEY_TRIPSHEET_DELIVERY_UPDATEDON = "tripshhet_delivery_updatedon";
    private final String KEY_TRIPSHEET_DELIVERY_UPDATEDBY = "tripshhet_delivery_updatedby";


    // Column names for Tripsheets returns List  Table
    private final String KEY_TRIPSHEET_RETURNS_RETURN_NO = "tripsheet_returns_return_no";
    private final String KEY_TRIPSHEET_RETURNS_TRIP_ID = "tripshhet_returns_trip_id";
    private final String KEY_TRIPSHEET_RETURNS_USER_ID = "tripshhet_returns_user_id";
    private final String KEY_TRIPSHEET_RETURNS_USER_CODES = "tripshhet_returns_user_codes";
    private final String KEY_TRIPSHEET_RETURNS_ROUTE_ID = "tripshhet_returns_route_id";
    private final String KEY_TRIPSHEET_RETURNS_ROUTE_CODES = "tripsheet_returns_route_codes";
    private final String KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS = "tripshhet_returns_product_ids";
    private final String KEY_TRIPSHEET_RETURNS_PRODUCT_CODES = "tripshhet_returns_product_codes";
    private final String KEY_TRIPSHEET_RETURNS_TAX_PERCENT = "tripshhet_returns_tax_percent";
    private final String KEY_TRIPSHEET_RETURNS_UNIT_PRICE = "tripshhet_returns_unit_price";
    private final String KEY_TRIPSHEET_RETURNS_QUANTITY = "tripshhet_returns_quantity";
    private final String KEY_TRIPSHEET_RETURNS_AMOUNT = "tripshhet_returns_amount";
    private final String KEY_TRIPSHEET_RETURNS_TAX_AMOUNT = "tripshhet_returns_tax_amount";
    private final String KEY_TRIPSHEET_RETURNS_TAX_TOTAL = "tripshhet_returns_tax_total";
    private final String KEY_TRIPSHEET_RETURNS_SALE_VALUE = "tripshhet_returns_sale_value";
    private final String KEY_TRIPSHEET_RETURNS_TYPE = "tripshhet_returns_type";
    private final String KEY_TRIPSHEET_RETURNS_STATUS = "tripshhet_returns_status";
    private final String KEY_TRIPSHEET_RETURNS_DELETE = "tripshhet_returns_delete";
    private final String KEY_TRIPSHEET_RETURNS_CREATED_BY = "tripshhet_returns_created_by";
    private final String KEY_TRIPSHEET_RETURNS_CREATED_ON = "tripshhet_returns_created_on";
    private final String KEY_TRIPSHEET_RETURNS_UPDATED_ON = "tripshhet_returns_updated_on";
    private final String KEY_TRIPSHEET_RETURNS_UPDATED_BY = "tripshhet_returns_updated_by";


    // Column names for Tripsheets payments List  Table
    private final String KEY_TRIPSHEET_PAYMENTS_PAYMENT_NO = "tripsheet_payments_payment_no";
    private final String KEY_TRIPSHEET_PAYMENTS_TRIP_ID = "tripshhet_payments_trip_id";
    private final String KEY_TRIPSHEET_PAYMENTS_USER_ID = "tripshhet_payments_user_id";
    private final String KEY_TRIPSHEET_PAYMENTS_USER_CODES = "tripshhet_payments_user_codes";
    private final String KEY_TRIPSHEET_PAYMENTS_ROUTE_ID = "tripshhet_payments_route_id";
    private final String KEY_TRIPSHEET_PAYMENTS_ROUTE_CODES = "tripsheet_payments_route_codes";
    private final String KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID = "tripshhet_payments_che_trans_id";
    private final String KEY_TRIPSHEET_PAYMENTS_AC_CA_NO = "tripshhet_payments_ac_ca_no";
    private final String KEY_TRIPSHEET_PAYMENTS_ACOUNT_NAME = "tripshhet_payments_account_name";
    private final String KEY_TRIPSHEET_PAYMENTS_BANK_NAME = "tripshhet_payments_bank_name";
    private final String KEY_TRIPSHEET_PAYMENTS_TRANS_DATE = "tripshhet_payments_trans_date";
    private final String KEY_TRIPSHEET_PAYMENTS_TRANS_CLEAR_DATE = "tripshhet_payments_trans_clear_date";
    private final String KEY_TRIPSHEET_PAYMENTS_RECEIVER_NAME = "tripshhet_payments_receiver_name";
    private final String KEY_TRIPSHEET_PAYMENTS_TRANS_STATUS = "tripshhet_payments_trans_status";
    private final String KEY_TRIPSHEET_PAYMENTS_PRODUCTS_IDS = "tripshhet_payments_product_ids";
    private final String KEY_TRIPSHEET_PAYMENTS_PRODUCT_CODES = "tripshhet_payments_product_codes";
    private final String KEY_TRIPSHEET_PAYMENTS_TAX_PERCENT = "tripshhet_payments_tax_percent";
    private final String KEY_TRIPSHEET_PAYMENTS_UNIT_PRICE = "tripshhet_payments_unit_price";
    private final String KEY_TRIPSHEET_PAYMENTS_QUANTITY = "tripshhet_payments_quantity";
    private final String KEY_TRIPSHEET_PAYMENTS_AMOUNT = "tripshhet_payments_amount";
    private final String KEY_TRIPSHEET_PAYMENTS_TAX_AMOUNT = "tripshhet_payments_tax_amount";
    private final String KEY_TRIPSHEET_PAYMENTS_TAX_TOTAL = "tripshhet_payments_tax_total";
    private final String KEY_TRIPSHEET_PAYMENTS_SALE_VALUE = "tripshhet_payments_sale_value";
    private final String KEY_TRIPSHEET_PAYMENTS_TYPE = "tripshhet_payments_type";
    private final String KEY_TRIPSHEET_PAYMENTS_STATUS = "tripshhet_payments_status";
    private final String KEY_TRIPSHEET_PAYMENTS_DELETE = "tripshhet_payments_delete";
    private final String KEY_TRIPSHEET_PAYMENTS_CREATED_BY = "tripshhet_payments_created_by";
    private final String KEY_TRIPSHEET_PAYMENTS_CREATED_ON = "tripshhet_payments_created_on";
    private final String KEY_TRIPSHEET_PAYMENTS_UPDATED_ON = "tripshhet_payments_updated_on";
    private final String KEY_TRIPSHEET_PAYMENTS_UPDATED_BY = "tripshhet_payments_updated_by";

    // Column names for Tripsheets so List  Table
    private final String KEY_TRIPSHEET_SO_UNIQUE_ID = "tripsheet_so_unique_id";
    private final String KEY_TRIPSHEET_SO_ID = "tripshhet_so_id";
    private final String KEY_TRIPSHEET_SO_TRIPID = "tripshhet_so_tripid";
    private final String KEY_TRIPSHEET_SO_AGENTCODE = "tripshhet_so_agentcode";
    private final String KEY_TRIPSHEET_SO_CODE = "tripsheet_so_code";
    private final String KEY_TRIPSHEET_SO_DATE = "tripshhet_so_date";
    private final String KEY_TRIPSHEET_SO_VALUE = "tripshhet_so_value";
    private final String KEY_TRIPSHEET_SO_OPAMOUNT = "tripshhet_so_opamount";
    private final String KEY_TRIPSHEET_SO_CBAMOUNT = "tripshhet_so_cbamount";
    private final String KEY_TRIPSHEET_SO_AGENTID = "tripshhet_so_agentid";
    private final String KEY_TRIPSHEET_SO_AGENTFIRSTNAME = "tripshhet_so_agentfirstname";
    private final String KEY_TRIPSHEET_SO_AGENTLASTNAME = "tripshhet_so_agentlastname";
    private final String KEY_TRIPSHEET_SO_PRODUCTCODE = "tripshhet_so_vehiclenumber";
    private final String KEY_TRIPSHEET_SO_PRODUCTORDER_QUANTITY = "tripshhet_so_productorder_quantity";
    private final String KEY_TRIPSHEET_SO_PRODUCT_VALUE = "tripshhet_so_product_value";
    private final String KEY_TRIPSHEET_SO_APPROVEDBY = "tripshhet_so_approvedby";
    private final String KEY_TRIPSHEET_SO_AGENTLATITUDE = "tripshhet_so_agentlatitude";
    private final String KEY_TRIPSHEET_SO_AGENTLONGITUDE = "tripshhet_so_agentlongitude";


    // Column names for Notifications List  Table
    private final String KEY_NOTIFICATIONS_ID = "notification_id";
    private final String KEY_NOTIFICATIONS_DATE = "notification_date";
    private final String KEY_NOTIFICATIONS_NAME = "notification_name";
    private final String KEY_NOTIFICATIONS_DESCRIPTION = "notification_description";

    // Agents Table Create Statements
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
            + KEY_AGENT_UPDATEDBY + " VARCHAR," + KEY_AGENT_UPDATEDON + " VARCHAR," + KEY_AGENT_ROUTECODE + " VARCHAR," + KEY_AGENT_DEVICESYNC + " VARCHAR,"
            + KEY_AGENT_ACCESSDEVICE + " VARCHAR," + KEY_AGENT_BACKUP + " VARCHAR)";


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
            + KEY_REGION_NAME + " VARCHAR," + KEY_OFFICE_NAME + " VARCHAR," + KEY_ROUTE_CODE + " VARCHAR)";


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
            + TABLE_TO_PRODUCTS + "(" + KEY_TO_PRODUCT_ID + " VARCHAR," + KEY_TO_PRODUCT_CODE + " VARCHAR,"
            + KEY_TO_PRODUCT_NAME + " VARCHAR," + KEY_TO_PRODUCT_ROUTE_ID + " VARCHAR,"
            + KEY_TO_FROM_DATE + " VARCHAR," + KEY_TO_TO_DATE + " VARCHAR," + KEY_TO_ORDER_TYPE + " VARCHAR,"
            + KEY_TO_QUANTITY + " VARCHAR," + KEY_TO_STATUS + " VARCHAR,"
            + KEY_TO_ENQID + " VARCHAR," + KEY_TO_AGENTID + " VARCHAR," + KEY_TAKEORDER_DATE + " VARCHAR," + KEY_PRICE + " VARCHAR," + KEY_VAT + " VARCHAR," + KEY_GST + " VARCHAR," + KEY_TO_AGENTCODE + " VARCHAR)";


    // User privilege actions Table Create Statements
    private final String CREATE_USER_PRIVILEGE_ACTIONS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PREVILEGE_ACTIONS + "(" + KEY_USER_PRIVILEGE_ACTION_ID + " VARCHAR," + KEY_USER_PRIVILEGE_ID + " VARCHAR,"
            + KEY_USER_PRIVILEGE_ACTION_NAME + " VARCHAR," + KEY_USER_PRIVILEGE_ACTION_STATUS + " VARCHAR)";

    // TDC Customers Table Create Statement
    private final String CREATE_TDC_CUSTOMERS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TDC_CUSTOMERS + "(" + KEY_TDC_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TDC_CUSTOMER_USER_ID + " VARCHAR, " + KEY_TDC_CUSTOMER_TYPE + " INTEGER, "
            + KEY_TDC_CUSTOMER_NAME + " VARCHAR, " + KEY_TDC_CUSTOMER_MOBILE_NO + " VARCHAR, " + KEY_TDC_CUSTOMER_BUSINESS_NAME + " VARCHAR, "
            + KEY_TDC_CUSTOMER_ADDRESS + " TEXT, " + KEY_TDC_CUSTOMER_LATITUDE + " TEXT, " + KEY_TDC_CUSTOMER_LONGITUDE + " TEXT, " + KEY_TDC_CUSTOMER_SHOP_IMAGE + " VARCHAR, "
            + KEY_TDC_CUSTOMER_IS_ACTIVE + " INTEGER DEFAULT 1, " + KEY_TDC_CUSTOMER_SHOP_IMAGE_UPLOAD_STATUS + " INTEGER DEFAULT 0, " + KEY_TDC_CUSTOMER_UPLOAD_STATUS + " INTEGER DEFAULT 0)";


    // SpecialPrice Table Create Statement
    private final String CREATE_TABLE_SPECIALPRICE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SPECIALPRICE + "(" + KEY_USER_SPECIALID + " VARCHAR, " + KEY_PRODUCT_SPECIALID + " INTEGER, "
            + KEY_SPECIALPRICE + " VARCHAR)";

    // TDC Sales Orders Table Create Statement
    private final String CREATE_TDC_SALES_ORDERS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TDC_SALES_ORDERS + "(" + KEY_TDC_SALES_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TDC_SALES_ORDER_NO_OF_ITEMS + " INTEGER, "
            + KEY_TDC_SALES_ORDER_TOTAL_AMOUNT + " VARCHAR, " + KEY_TDC_SALES_ORDER_TOTAL_TAX_AMOUNT + " VARCHAR, " + KEY_TDC_SALES_ORDER_SUB_TOTAL + " VARCHAR, "
            + KEY_TDC_SALES_ORDER_CUSTOMER_ID + " INTEGER, " + KEY_TDC_SALES_ORDER_CUSTOMER_USER_ID + " VARCHAR, " + KEY_TDC_SALES_ORDER_DATE + " TEXT, "
            + KEY_TDC_SALES_ORDER_CREATED_ON + " TEXT, " + KEY_TDC_SALES_ORDER_CREATED_BY + " VARCHAR, " + KEY_TDC_SALES_ORDER_UPLOAD_STATUS + " INTEGER DEFAULT 0)";

    // TDC Sales Order Products Table Create Statement
    private final String CREATE_TDC_SALES_ORDER_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TDC_SALES_ORDER_PRODUCTS + "(" + KEY_TDC_SOP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TDC_SOP_ORDER_ID + " INTEGER, "
            + KEY_TDC_SOP_PRODUCT_ID + " VARCHAR, " + KEY_TDC_SOP_PRODUCT_NAME + " VARCHAR, " + KEY_TDC_SOP_PRODUCT_MRP + " VARCHAR, "
            + KEY_TDC_SOP_PRODUCT_QUANTITY + " VARCHAR, " + KEY_TDC_SOP_PRODUCT_AMOUNT + " VARCHAR, " + KEY_TDC_SOP_PRODUCT_TAX + " TEXT, "
            + KEY_TDC_SOP_PRODUCT_TAX_AMOUNT + " TEXT, " + KEY_TDC_SOP_UPLOAD_STATUS + " INTEGER DEFAULT 0)";

    // Stakeholder types Table Create Statements
    private final String CREATE_TABLE_STAKEHOLDER_TYPES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_STAKEHOLDER_TYPES + "(" + KEY_STAKEHOLDER_TYPE_ID + " VARCHAR," + KEY_STAKEHOLDER_TYPE_NAME + " VARCHAR,"
            + KEY_STAKEHOLDER_TYPE + " VARCHAR)";

    // Tripsheets Table Create Statements
    private final String CREATE_TRIPSHEETS_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRIPSHEETS_LIST + "(" + KEY_TRIPSHEET_UNIQUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TRIPSHEET_ID + " VARCHAR,"
            + KEY_TRIPSHEET_CODE + " VARCHAR,"
            + KEY_TRIPSHEET_DATE + " VARCHAR,"
            + KEY_TRIPSHEET_STATUS + " VARCHAR,"
            + KEY_TRIPSHEET_OB_AMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_ORDERED_AMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_RECEIVED_AMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_DUE_AMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_ROUTE_CODE + " VARCHAR,"
            + KEY_TRIPSHEET_SALESMEN_CODE + " VARCHAR,"
            + KEY_TRIPSHEET_TRANSPORTER_NAME + " VARCHAR,"
            + KEY_TRIPSHEET_VEHICLE_NUMBER + " VARCHAR,"
            + KEY_TRIPSHEET_VERIFY_STATUS + " VARCHAR)";

    // Tripsheets Stock list Table Create Statements
    private final String CREATE_TRIPSHEETS_STOCK_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRIPSHEETS_STOCK_LIST + "(" + KEY_TRIPSHEET_STOCK_UNIQUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TRIPSHEET_STOCK_ID + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_TRIPSHEET_ID + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_PRODUCT_CODE + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_PRODUCT_NAME + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_PRODUCT_ID + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_ORDER_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_DISPATCH_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_DISPATCH_DATE + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_DISPATCH_BY + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_VERIFY_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_VERIFY_DATE + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_VERIFY_BY + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_IN_STOCK_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_EXTRA_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_IS_DISPATCHED + " INTEGER DEFAULT 0,"
            + KEY_TRIPSHEET_STOCK_IS_VERIFIED + " INTEGER DEFAULT 0,"
            + KEY_TRIPSHEET_STOCK_DISPATCHED_UPLOAD_STATUS + " INTEGER DEFAULT 0,"
            + KEY_TRIPSHEET_STOCK_VERIFIED_UPLOAD_STATUS + " INTEGER DEFAULT 0)";

    // Tripsheets Deliveries list Table Create Statements
    private final String CREATE_TRIPSHEETS_DELIVERIES_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRIPSHEETS_DELIVERIES_LIST + "(" + KEY_TRIPSHEET_DELIVERY_NO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_USER_ID + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_USER_CODES + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_ROUTE_ID + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_ROUTE_CODES + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_TAXPERCENT + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_UNITPRICE + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_AMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_TAXAMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_TAXTOTAL + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_SALEVALUE + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_STATUS + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_DELETE + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_CREATEDBY + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_CREATEDON + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_UPDATEDON + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_UPDATEDBY + " VARCHAR)";

    // Tripsheets Returns list Table Create Statements
    private final String CREATE_TRIPSHEETS_RETURNS_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRIPSHEETS_RETURNS_LIST + "(" + KEY_TRIPSHEET_RETURNS_RETURN_NO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TRIPSHEET_RETURNS_TRIP_ID + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_USER_ID + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_USER_CODES + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_ROUTE_ID + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_ROUTE_CODES + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_PRODUCT_CODES + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_TAX_PERCENT + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_UNIT_PRICE + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_AMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_TAX_AMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_TAX_TOTAL + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_SALE_VALUE + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_TYPE + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_STATUS + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_DELETE + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_CREATED_BY + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_CREATED_ON + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_UPDATED_ON + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_UPDATED_BY + " VARCHAR)";


    // Tripsheets Returns list Table Create Statements
    private final String CREATE_TRIPSHEETS_PAYMENTS_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRIPSHEETS_PAYMENTS_LIST + "(" + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_USER_ID + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_USER_CODES + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_ROUTE_ID + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_ROUTE_CODES + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_AC_CA_NO + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_ACOUNT_NAME + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_BANK_NAME + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_TRANS_DATE + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_TRANS_CLEAR_DATE + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_RECEIVER_NAME + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_TRANS_STATUS + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_PRODUCTS_IDS + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_PRODUCT_CODES + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_TAX_PERCENT + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_UNIT_PRICE + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_AMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_TAX_AMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_TAX_TOTAL + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_SALE_VALUE + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_TYPE + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_STATUS + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_DELETE + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_CREATED_BY + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_CREATED_ON + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_UPDATED_ON + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_UPDATED_BY + " VARCHAR)";


    // Tripsheets SO Table Create Statements
    private final String CREATE_TRIPSHEETS_SO_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRIPSHEETS_SO_LIST + "(" + KEY_TRIPSHEET_SO_UNIQUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TRIPSHEET_SO_ID + " VARCHAR,"
            + KEY_TRIPSHEET_SO_TRIPID + " VARCHAR,"
            + KEY_TRIPSHEET_SO_AGENTCODE + " VARCHAR,"
            + KEY_TRIPSHEET_SO_CODE + " VARCHAR,"
            + KEY_TRIPSHEET_SO_DATE + " VARCHAR,"
            + KEY_TRIPSHEET_SO_VALUE + " VARCHAR,"
            + KEY_TRIPSHEET_SO_OPAMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_SO_CBAMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_SO_AGENTID + " VARCHAR,"
            + KEY_TRIPSHEET_SO_AGENTFIRSTNAME + " VARCHAR,"
            + KEY_TRIPSHEET_SO_AGENTLASTNAME + " VARCHAR,"
            + KEY_TRIPSHEET_SO_PRODUCTCODE + " VARCHAR,"
            + KEY_TRIPSHEET_SO_PRODUCTORDER_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_SO_PRODUCT_VALUE + " VARCHAR,"
            + KEY_TRIPSHEET_SO_APPROVEDBY + " VARCHAR,"
            + KEY_TRIPSHEET_SO_AGENTLATITUDE + " VARCHAR,"
            + KEY_TRIPSHEET_SO_AGENTLONGITUDE + " VARCHAR)";


    // Notifications Table Create Statements
    private final String CREATE_NOTIFICATIONS_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NOTIFICATION_LIST + "(" + KEY_NOTIFICATIONS_ID + " VARCHAR," + KEY_NOTIFICATIONS_DATE + " VARCHAR,"
            + KEY_NOTIFICATIONS_NAME + " VARCHAR,"
            + KEY_NOTIFICATIONS_DESCRIPTION + " VARCHAR)";


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
            db.execSQL(CREATE_TABLE_SPECIALPRICE);
            db.execSQL(CREATE_TDC_SALES_ORDERS_TABLE);
            db.execSQL(CREATE_TDC_SALES_ORDER_PRODUCTS_TABLE);
            db.execSQL(CREATE_TABLE_STAKEHOLDER_TYPES);
            db.execSQL(CREATE_TRIPSHEETS_LIST_TABLE);
            db.execSQL(CREATE_TRIPSHEETS_STOCK_LIST_TABLE);
            db.execSQL(CREATE_TRIPSHEETS_SO_LIST_TABLE);
            db.execSQL(CREATE_TRIPSHEETS_DELIVERIES_LIST_TABLE);
            db.execSQL(CREATE_TRIPSHEETS_RETURNS_LIST_TABLE);
            db.execSQL(CREATE_TRIPSHEETS_PAYMENTS_LIST_TABLE);
            db.execSQL(CREATE_NOTIFICATIONS_LIST_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            /*db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USERDETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ROUTES);
            db.execSQL("DROP TABLE IF EXISTS" + CREATE_PRODUCTS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_USER_ACTIVITY_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_AGENTS);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_PRODUCTS_TABLE_TO);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_USER_PRIVILEGE_ACTIONS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SPECIALPRICE);*/

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
                values.put(KEY_AGENT_ROUTECODE, mAgentsBeansList.get(i).getmAgentRoutecode());
                values.put(KEY_AGENT_DEVICESYNC, mAgentsBeansList.get(i).getmAgentDeviceSync());
                values.put(KEY_AGENT_ACCESSDEVICE, mAgentsBeansList.get(i).getmAgentAccessDevice());
                values.put(KEY_AGENT_BACKUP, mAgentsBeansList.get(i).getmAgentBackUp());
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
                    agentsBean.setmAgentRoutecode((c.getString(c.getColumnIndex(KEY_AGENT_ROUTECODE))));
                    agentsBean.setmAgentDeviceSync((c.getString(c.getColumnIndex(KEY_AGENT_DEVICESYNC))));
                    agentsBean.setmAgentAccessDevice((c.getString(c.getColumnIndex(KEY_AGENT_ACCESSDEVICE))));
                    // agentsBean.setmAgentBackUp((c.getString(c.getColumnIndex(KEY_AGENT_BACKUP))));
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

    public long insertRoutesDetails(String routeId, String routeName, String regionName, String officeName, String routeCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        long insertRecordCount = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ROUTE_ID, routeId);

            values.put(KEY_ROUTE_NAME, routeName);
            values.put(KEY_REGION_NAME, regionName);
            values.put(KEY_OFFICE_NAME, officeName);
            values.put(KEY_ROUTE_CODE, routeCode);

            // insert row
            insertRecordCount = db.insert(TABLE_ROUTESDETAILS, null, values);
            System.out.println("F*********** ROUTE INSERTED COUNT***************" + insertRecordCount);
            values.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return insertRecordCount;
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
                    routesMasterData.add((c.getString(c.getColumnIndex(KEY_ROUTE_CODE))));

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
     *
     * @param routeId
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
                    routeDetailsById.add((c.getString(c.getColumnIndex(KEY_ROUTE_CODE))));
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
     * Method to fetch all records from products table
     *
     * @param agentId
     */
    public ArrayList<ProductsBean> fetchAllRecordsFromProductsTableForTakeOrders(String agentId) {
        ArrayList<ProductsBean> allProductTrackRecords = new ArrayList<ProductsBean>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " tp LEFT JOIN " + TABLE_TO_PRODUCTS + " top ON tp." + KEY_PRODUCT_ID
                    + "=top." + KEY_TO_PRODUCT_ID + " AND top." + KEY_TO_AGENTID + "='" + agentId + "'";

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

                    productsBean.setmTakeOrderQuantity(c.getString(c.getColumnIndex(KEY_TO_QUANTITY)));
                    productsBean.setmTakeOrderFromDate(c.getString(c.getColumnIndex(KEY_TO_FROM_DATE)));
                    productsBean.setmTakeOrderToDate(c.getString(c.getColumnIndex(KEY_TO_TO_DATE)));

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
                values.put(KEY_TO_PRODUCT_CODE, mProductsBeansList.get(i).getMtakeorderProductCode());
                values.put(KEY_TO_PRODUCT_NAME, mProductsBeansList.get(i).getmProductTitle());
                values.put(KEY_TO_PRODUCT_ROUTE_ID, mProductsBeansList.get(i).getmRouteId());
                values.put(KEY_TO_FROM_DATE, mProductsBeansList.get(i).getmProductFromDate());
                values.put(KEY_TO_TO_DATE, mProductsBeansList.get(i).getmProductToDate());
                values.put(KEY_TO_ORDER_TYPE, mProductsBeansList.get(i).getmProductOrderType());
                values.put(KEY_TO_QUANTITY, mProductsBeansList.get(i).getmProductQuantity());
                values.put(KEY_TO_STATUS, mProductsBeansList.get(i).getmProductStatus());
                values.put(KEY_TO_ENQID, mProductsBeansList.get(i).getmEnquiryId());
                values.put(KEY_TO_AGENTID, mProductsBeansList.get(i).getmAgentId());
                values.put(KEY_TO_AGENTCODE, mProductsBeansList.get(i).getmTakeorderAgentCode());
                values.put(KEY_TAKEORDER_DATE, mProductsBeansList.get(i).getmAgentTakeOrderDate());
                values.put(KEY_PRICE, mProductsBeansList.get(i).getmAgentPrice());
                values.put(KEY_VAT, mProductsBeansList.get(i).getmAgentVAT());
                values.put(KEY_GST, mProductsBeansList.get(i).getmAgentGST());
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
     * @param agentId
     */
    public ArrayList<TakeOrderBean> fetchAllRecordsFromTakeOrderProductsTable(String isSync, String agentId) {
        ArrayList<TakeOrderBean> allProductTrackRecords = new ArrayList<TakeOrderBean>();
        try {
            if (allProductTrackRecords.size() > 0) {
                allProductTrackRecords.clear();
            }
            String selectQuery = "";
            if (isSync.equals("yes")) {
                selectQuery = "SELECT  * FROM " + TABLE_TO_PRODUCTS + " WHERE " + KEY_TO_STATUS + " = " + "1" + " AND " + KEY_TO_AGENTID + "='" + agentId + "'";
            } else {
                selectQuery = "SELECT  * FROM " + TABLE_TO_PRODUCTS + " WHERE " + KEY_TO_AGENTID + "='" + agentId + "'";
            }

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    TakeOrderBean toBean = new TakeOrderBean();

                    toBean.setmProductId((c.getString(c.getColumnIndex(KEY_TO_PRODUCT_ID))));
                    toBean.setMtakeorderProductCode((c.getString(c.getColumnIndex(KEY_TO_PRODUCT_CODE))));
                    toBean.setmRouteId((c.getString(c.getColumnIndex(KEY_TO_PRODUCT_ROUTE_ID))));
                    toBean.setmProductTitle((c.getString(c.getColumnIndex(KEY_TO_PRODUCT_NAME))));
                    toBean.setmProductFromDate((c.getString(c.getColumnIndex(KEY_TO_FROM_DATE))));
                    toBean.setmProductToDate((c.getString(c.getColumnIndex(KEY_TO_TO_DATE))));
                    toBean.setmProductOrderType((c.getString(c.getColumnIndex(KEY_TO_ORDER_TYPE))));
                    toBean.setmProductQuantity((c.getString(c.getColumnIndex(KEY_TO_QUANTITY))));
                    toBean.setmProductStatus((c.getString(c.getColumnIndex(KEY_TO_STATUS))));
                    toBean.setmEnquiryId((c.getString(c.getColumnIndex(KEY_TO_ENQID))));
                    toBean.setmAgentId((c.getString(c.getColumnIndex(KEY_TO_AGENTID))));
                    toBean.setmAgentTakeOrderDate((c.getString(c.getColumnIndex(KEY_TAKEORDER_DATE))));
                    toBean.setmAgentPrice((c.getString(c.getColumnIndex(KEY_PRICE))));
                    toBean.setmAgentVAT((c.getString(c.getColumnIndex(KEY_VAT))));
                    toBean.setmAgentGST((c.getString(c.getColumnIndex(KEY_GST))));
                    toBean.setmTakeorderAgentCode((c.getString(c.getColumnIndex(KEY_TO_AGENTCODE))));
                    // toBean.setMtakeorderProductCode((c.getString(c.getColumnIndex(KEY_TO_PRODUCT_CODE))));


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

    public int checkProductExistsOrNot(String s, String getmAgentId) {
        int maxID = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_TO_PRODUCTS + " WHERE " + KEY_TO_PRODUCT_ID + "='" + s + "'" + " AND " + KEY_TO_AGENTID + "='" + getmAgentId + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //System.out.println("DDDD::: "+ cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                maxID = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        //System.out.println("FGGHH::: "+maxID);
        return maxID;
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
                values.put(KEY_TO_PRODUCT_ID, takeOrderBeanArrayList.get(b).getmProductId());
                values.put(KEY_TO_PRODUCT_CODE, takeOrderBeanArrayList.get(b).getMtakeorderProductCode());
                values.put(KEY_TO_PRODUCT_NAME, takeOrderBeanArrayList.get(b).getmProductTitle());
                values.put(KEY_TO_PRODUCT_ROUTE_ID, takeOrderBeanArrayList.get(b).getmRouteId());
                values.put(KEY_TO_FROM_DATE, takeOrderBeanArrayList.get(b).getmProductFromDate());
                values.put(KEY_TO_TO_DATE, takeOrderBeanArrayList.get(b).getmProductToDate());
                values.put(KEY_TO_ORDER_TYPE, takeOrderBeanArrayList.get(b).getmProductOrderType());
                values.put(KEY_TO_QUANTITY, takeOrderBeanArrayList.get(b).getmProductQuantity());
                values.put(KEY_TO_STATUS, takeOrderBeanArrayList.get(b).getmProductStatus());
                values.put(KEY_TO_ENQID, takeOrderBeanArrayList.get(b).getmEnquiryId());
                values.put(KEY_TO_AGENTID, takeOrderBeanArrayList.get(b).getmAgentId());
                values.put(KEY_TO_AGENTCODE, takeOrderBeanArrayList.get(b).getmTakeorderAgentCode());
                values.put(KEY_TAKEORDER_DATE, takeOrderBeanArrayList.get(b).getmAgentTakeOrderDate());
                values.put(KEY_PRICE, takeOrderBeanArrayList.get(b).getmAgentPrice());
                values.put(KEY_VAT, takeOrderBeanArrayList.get(b).getmAgentVAT());
                values.put(KEY_GST, takeOrderBeanArrayList.get(b).getmAgentGST());


                int ccc = checkProductExistsOrNot(takeOrderBeanArrayList.get(b).getmProductId(), takeOrderBeanArrayList.get(b).getmAgentId());
                //System.out.println("Product Exists:::: "+ ccc);
                if (ccc == 0) {
                    effectedRows = db.insert(TABLE_TO_PRODUCTS, null, values);
                    // System.out.println("IFFFFFF::: "+effectedRows);
                } else {
                    effectedRows = db.update(TABLE_TO_PRODUCTS, values, KEY_TO_PRODUCT_ID + " = ?" + " AND " + KEY_TO_AGENTID + " = ? ",
                            new String[]{String.valueOf(takeOrderBeanArrayList.get(b).getmProductId()), String.valueOf(takeOrderBeanArrayList.get(b).getmAgentId())});
                    // System.out.println("ELSEEE::: "+effectedRows);
                }
                //System.out.println("UpDATE PRICE::: " + takeOrderBeanArrayList.get(b).getmAgentPrice());
                // update row
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        //System.out.println("ASDF::: " + effectedRows);
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
     * Method to insert record into TDC Customers Table
     */
    public long insertIntoTDCCustomers(TDCCustomer customer) {
        long customerId = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_TDC_CUSTOMER_USER_ID, customer.getUserId());
            values.put(KEY_TDC_CUSTOMER_TYPE, customer.getCustomerType());
            values.put(KEY_TDC_CUSTOMER_NAME, customer.getName());
            values.put(KEY_TDC_CUSTOMER_MOBILE_NO, customer.getMobileNo());
            values.put(KEY_TDC_CUSTOMER_BUSINESS_NAME, customer.getBusinessName());
            values.put(KEY_TDC_CUSTOMER_ADDRESS, customer.getAddress());
            values.put(KEY_TDC_CUSTOMER_LATITUDE, customer.getLatitude());
            values.put(KEY_TDC_CUSTOMER_LONGITUDE, customer.getLongitude());
            values.put(KEY_TDC_CUSTOMER_SHOP_IMAGE, customer.getShopImage());
            values.put(KEY_TDC_CUSTOMER_SHOP_IMAGE_UPLOAD_STATUS, customer.getIsShopImageUploaded());

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
            String selectQuery = "SELECT * FROM " + TABLE_TDC_CUSTOMERS + " WHERE " + KEY_TDC_CUSTOMER_IS_ACTIVE + " = 1";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TDCCustomer customer = new TDCCustomer();
                    customer.setId(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_ID)));
                    customer.setUserId(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_USER_ID)));
                    customer.setCustomerType(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_TYPE)));
                    customer.setName(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_NAME)));
                    customer.setMobileNo(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_MOBILE_NO)));
                    customer.setBusinessName(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_BUSINESS_NAME)));
                    customer.setAddress(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_ADDRESS)));
                    customer.setLatitude(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_LATITUDE)));
                    customer.setLongitude(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_LONGITUDE)));
                    customer.setShopImage(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_SHOP_IMAGE)));
                    customer.setIsActive(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_IS_ACTIVE)));
                    customer.setIsUploaded(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_UPLOAD_STATUS)));
                    customer.setIsShopImageUploaded(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_SHOP_IMAGE_UPLOAD_STATUS)));

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

    /**
     * Method to fetch only retailer records from TDC Customers Table
     *
     * @param customerType = 0 for Consumers & 1 for Retailers
     */
    public List<TDCCustomer> fetchRecordsFromTDCCustomers(int customerType) {
        List<TDCCustomer> allRetailersList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TDC_CUSTOMERS + " WHERE " + KEY_TDC_CUSTOMER_IS_ACTIVE + " = 1 AND " + KEY_TDC_CUSTOMER_TYPE + " = " + customerType;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TDCCustomer customer = new TDCCustomer();
                    customer.setId(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_ID)));
                    customer.setUserId(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_USER_ID)));
                    customer.setCustomerType(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_TYPE)));
                    customer.setName(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_NAME)));
                    customer.setMobileNo(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_MOBILE_NO)));
                    customer.setBusinessName(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_BUSINESS_NAME)));
                    customer.setAddress(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_ADDRESS)));
                    customer.setLatitude(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_LATITUDE)));
                    customer.setLongitude(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_LONGITUDE)));
                    customer.setShopImage(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_SHOP_IMAGE)));
                    customer.setIsActive(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_IS_ACTIVE)));
                    customer.setIsUploaded(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_UPLOAD_STATUS)));
                    customer.setIsShopImageUploaded(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_SHOP_IMAGE_UPLOAD_STATUS)));

                    allRetailersList.add(customer);
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allRetailersList;
    }

    /**
     * Method to fetch all un uploaded records from TDC Customers Table
     */
    public List<TDCCustomer> fetchAllUnUploadedRecordsFromTDCCustomers() {
        List<TDCCustomer> allTDCCustomersList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TDC_CUSTOMERS + " WHERE " + KEY_TDC_CUSTOMER_IS_ACTIVE + " = 1 AND " + KEY_TDC_CUSTOMER_UPLOAD_STATUS + " = 0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TDCCustomer customer = new TDCCustomer();
                    customer.setId(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_ID)));
                    customer.setUserId(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_USER_ID)));
                    customer.setCustomerType(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_TYPE)));
                    customer.setName(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_NAME)));
                    customer.setMobileNo(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_MOBILE_NO)));
                    customer.setBusinessName(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_BUSINESS_NAME)));
                    customer.setAddress(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_ADDRESS)));
                    customer.setLatitude(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_LATITUDE)));
                    customer.setLongitude(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_LONGITUDE)));
                    customer.setShopImage(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_SHOP_IMAGE)));
                    customer.setIsActive(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_IS_ACTIVE)));
                    customer.setIsUploaded(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_UPLOAD_STATUS)));
                    customer.setIsShopImageUploaded(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_SHOP_IMAGE_UPLOAD_STATUS)));

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

    public void updateTDCCustomersUploadStatus(long customerId, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TDC_CUSTOMER_UPLOAD_STATUS, 1);
            values.put(KEY_TDC_CUSTOMER_USER_ID, userId);

            int status = db.update(TABLE_TDC_CUSTOMERS, values, KEY_TDC_CUSTOMER_ID + " = ?", new String[]{String.valueOf(customerId)});

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to check weather the special price exists for the combo of userId and productId
     *
     * @param productId
     * @param agentId
     * @return integer value
     */
    public int checkSpecialPriceProductExistsOrNot(String productId, String agentId) {
        int maxID = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_SPECIALPRICE + " WHERE " + KEY_PRODUCT_SPECIALID + "='" + productId + "'" + " AND " + KEY_USER_SPECIALID + "='" + agentId + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //System.out.println("DDDD::: "+ cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                maxID = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        //System.out.println("FGGHH::: "+maxID);
        return maxID;
    }


    public void insertSpecialPriceDetails(ArrayList<SpecialPriceBean> mSpecialPriceBeansList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mSpecialPriceBeansList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_USER_SPECIALID, mSpecialPriceBeansList.get(i).getSpecialUserId());
                values.put(KEY_PRODUCT_SPECIALID, mSpecialPriceBeansList.get(i).getSpecialProductId());
                values.put(KEY_SPECIALPRICE, mSpecialPriceBeansList.get(i).getSpecialPrice());
                int checkVal = checkSpecialPriceProductExistsOrNot(mSpecialPriceBeansList.get(i).getSpecialProductId()
                        , mSpecialPriceBeansList.get(i).getSpecialUserId());
                if (checkVal == 0) {
                    // insert row
                    db.insert(TABLE_SPECIALPRICE, null, values);
                    System.out.println("F*********** INSERTED***************88");
                } else {
                    // Update row
                    db.update(TABLE_SPECIALPRICE, values, KEY_PRODUCT_SPECIALID + " = ?" + " AND " + KEY_USER_SPECIALID + " = ? ",
                            new String[]{String.valueOf(mSpecialPriceBeansList.get(i).getSpecialProductId()),
                                    String.valueOf(mSpecialPriceBeansList.get(i).getSpecialUserId())});
                    System.out.println("F*********** UPDATED***************88");
                }

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
    public ArrayList<SpecialPriceBean> fetchAllRecordsFromSpecialPriceTable() {
        ArrayList<SpecialPriceBean> allSpecialPriceTrackRecords = new ArrayList<SpecialPriceBean>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_SPECIALPRICE;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    SpecialPriceBean specialpriceBean = new SpecialPriceBean();

                    specialpriceBean.setSpecialUserId((c.getString(c.getColumnIndex(KEY_USER_SPECIALID))));
                    specialpriceBean.setSpecialProductId((c.getString(c.getColumnIndex(KEY_PRODUCT_SPECIALID))));
                    specialpriceBean.setSpecialPrice((c.getString(c.getColumnIndex(KEY_SPECIALPRICE))));


                    allSpecialPriceTrackRecords.add(specialpriceBean);
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allSpecialPriceTrackRecords;
    }

    /**
     * Method to fetch customer specific orders from TDC Sales Orders Table
     */
    public List<TDCSaleOrder> fetchTDCSalesOrdersForSelectedCustomer(long customerId) {
        List<TDCSaleOrder> allOrdersList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TDC_SALES_ORDERS + " WHERE " + KEY_TDC_SALES_ORDER_CUSTOMER_ID + " = " + customerId;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TDCSaleOrder order = new TDCSaleOrder();
                    long orderId = c.getLong(c.getColumnIndex(KEY_TDC_SALES_ORDER_ID));
                    order.setOrderId(orderId);
                    order.setNoOfItems(c.getInt(c.getColumnIndex(KEY_TDC_SALES_ORDER_NO_OF_ITEMS)));
                    order.setOrderTotalAmount(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_TOTAL_AMOUNT))));
                    order.setOrderTotalTaxAmount(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_TOTAL_TAX_AMOUNT))));
                    order.setOrderSubTotal(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_SUB_TOTAL))));
                    order.setSelectedCustomerId(c.getLong(c.getColumnIndex(KEY_TDC_SALES_ORDER_CUSTOMER_ID)));
                    order.setSelectedCustomerUserId(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_CUSTOMER_USER_ID)));
                    order.setOrderDate(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_DATE)));
                    order.setCreatedOn(c.getLong(c.getColumnIndex(KEY_TDC_SALES_ORDER_CREATED_ON)));
                    order.setCreatedBy(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_CREATED_BY)));
                    order.setIsUploaded(c.getInt(c.getColumnIndex(KEY_TDC_SALES_ORDER_UPLOAD_STATUS)));
                    order.setProductsList(fetchTDCSalesOrderProductsListForOrderId(orderId));

                    allOrdersList.add(order);

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allOrdersList;
    }

    /**
     * Method to fetch records for selected duration from TDC Sales Orders Table
     */
    public List<TDCSaleOrder> fetchAllTDCSalesOrdersForSelectedDuration(String startDate, String endDate) {
        List<TDCSaleOrder> allOrdersList = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TDC_SALES_ORDERS + " WHERE " + KEY_TDC_SALES_ORDER_DATE + " BETWEEN ? AND ?", new String[]{startDate, endDate});

            if (c.moveToFirst()) {
                do {
                    TDCSaleOrder order = new TDCSaleOrder();
                    long orderId = c.getLong(c.getColumnIndex(KEY_TDC_SALES_ORDER_ID));
                    order.setOrderId(orderId);
                    order.setNoOfItems(c.getInt(c.getColumnIndex(KEY_TDC_SALES_ORDER_NO_OF_ITEMS)));
                    order.setOrderTotalAmount(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_TOTAL_AMOUNT))));
                    order.setOrderTotalTaxAmount(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_TOTAL_TAX_AMOUNT))));
                    order.setOrderSubTotal(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_SUB_TOTAL))));
                    order.setSelectedCustomerId(c.getLong(c.getColumnIndex(KEY_TDC_SALES_ORDER_CUSTOMER_ID)));
                    order.setSelectedCustomerUserId(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_CUSTOMER_USER_ID)));
                    order.setOrderDate(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_DATE)));
                    order.setCreatedOn(c.getLong(c.getColumnIndex(KEY_TDC_SALES_ORDER_CREATED_ON)));
                    order.setCreatedBy(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_CREATED_BY)));
                    order.setIsUploaded(c.getInt(c.getColumnIndex(KEY_TDC_SALES_ORDER_UPLOAD_STATUS)));
                    order.setProductsList(fetchTDCSalesOrderProductsListForOrderId(orderId));

                    allOrdersList.add(order);

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allOrdersList;
    }

    /**
     * Method to fetch all products for particular order from TDC Sales Order Products Table
     */
    public Map<String, ProductsBean> fetchTDCSalesOrderProductsListForOrderId(long orderId) {
        Map<String, ProductsBean> OrderAllProductsList = new HashMap<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TDC_SALES_ORDER_PRODUCTS + " WHERE " + KEY_TDC_SOP_ORDER_ID + " = " + orderId;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    ProductsBean productsBean = new ProductsBean();
                    productsBean.setProductId(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_ID)));
                    productsBean.setProductTitle(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_NAME)));
                    productsBean.setProductRatePerUnit(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_MRP))));
                    productsBean.setSelectedQuantity(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_QUANTITY))));
                    productsBean.setProductAmount(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_AMOUNT))));
                    productsBean.setProductTaxPerUnit(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_TAX))));
                    productsBean.setTaxAmount(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_TAX_AMOUNT))));

                    OrderAllProductsList.put(productsBean.getProductId(), productsBean);

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return OrderAllProductsList;
    }

    /**
     * Method to fetch all products for particular order from TDC Sales Order Products Table
     */
    public List<TDCSalesOrderProductBean> fetchTDCSalesOrderProductsListByOrderId(long orderId) {
        List<TDCSalesOrderProductBean> allOrdersProductsList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TDC_SALES_ORDER_PRODUCTS + " WHERE " + KEY_TDC_SOP_ORDER_ID + " = " + orderId;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TDCSalesOrderProductBean orderProducts = new TDCSalesOrderProductBean();
                    orderProducts.setId(c.getLong(c.getColumnIndex(KEY_TDC_SOP_ID)));
                    orderProducts.setOrderId(c.getLong(c.getColumnIndex(KEY_TDC_SOP_ORDER_ID)));
                    orderProducts.setProductId(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_ID)));
                    orderProducts.setProductName(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_NAME)));
                    orderProducts.setProductMRP(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_MRP))));
                    orderProducts.setProductQuantity(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_QUANTITY))));
                    orderProducts.setProductAmount(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_AMOUNT))));
                    orderProducts.setProductTax(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_TAX))));
                    orderProducts.setProductTaxAmount(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_TAX_AMOUNT))));
                    orderProducts.setIsUploaded(c.getInt(c.getColumnIndex(KEY_TDC_SOP_UPLOAD_STATUS)));

                    allOrdersProductsList.add(orderProducts);

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allOrdersProductsList;
    }

    /**
     * Method to insert record into TDC Sales Orders Table
     */
    public long insertIntoTDCSalesOrdersTable(TDCSaleOrder order) {
        long orderId = 0;

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_TDC_SALES_ORDER_NO_OF_ITEMS, order.getProductsList().size());
            values.put(KEY_TDC_SALES_ORDER_TOTAL_AMOUNT, order.getOrderTotalAmount());
            values.put(KEY_TDC_SALES_ORDER_TOTAL_TAX_AMOUNT, order.getOrderTotalTaxAmount());
            values.put(KEY_TDC_SALES_ORDER_SUB_TOTAL, order.getOrderSubTotal());
            values.put(KEY_TDC_SALES_ORDER_CUSTOMER_ID, order.getSelectedCustomerId());
            values.put(KEY_TDC_SALES_ORDER_CUSTOMER_USER_ID, order.getSelectedCustomerUserId());
            values.put(KEY_TDC_SALES_ORDER_DATE, order.getOrderDate());
            values.put(KEY_TDC_SALES_ORDER_CREATED_ON, order.getCreatedOn());
            values.put(KEY_TDC_SALES_ORDER_CREATED_BY, order.getCreatedBy());

            orderId = db.insert(TABLE_TDC_SALES_ORDERS, null, values);

            for (Map.Entry<String, ProductsBean> productsBeanEntry : order.getProductsList().entrySet()) {
                this.insertIntoTDCSalesOrderProductsTable(orderId, productsBeanEntry.getValue());
            }

            values.clear();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderId;
    }

    /**
     * Method to insert record into TDC Sales Order Products Table
     */
    public long insertIntoTDCSalesOrderProductsTable(long orderId, ProductsBean orderProduct) {
        long orderProductId = 0;

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_TDC_SOP_ORDER_ID, orderId);
            values.put(KEY_TDC_SOP_PRODUCT_ID, orderProduct.getProductId());
            values.put(KEY_TDC_SOP_PRODUCT_NAME, orderProduct.getProductTitle());
            values.put(KEY_TDC_SOP_PRODUCT_MRP, orderProduct.getProductRatePerUnit());
            values.put(KEY_TDC_SOP_PRODUCT_QUANTITY, orderProduct.getSelectedQuantity());
            values.put(KEY_TDC_SOP_PRODUCT_AMOUNT, orderProduct.getProductAmount());
            values.put(KEY_TDC_SOP_PRODUCT_TAX, orderProduct.getProductTaxPerUnit());
            values.put(KEY_TDC_SOP_PRODUCT_TAX_AMOUNT, orderProduct.getTaxAmount());

            orderProductId = db.insert(TABLE_TDC_SALES_ORDER_PRODUCTS, null, values);

            values.clear();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderProductId;
    }

    public long getTDCSalesMaxOrderNumber() {
        long orderId = 0;

        try {
            String countQuery = "SELECT " + KEY_TDC_SALES_ORDER_ID + " FROM " + TABLE_TDC_SALES_ORDERS + " ORDER BY " + KEY_TDC_SALES_ORDER_ID + " DESC LIMIT 1";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            if (cursor.moveToFirst()) {
                orderId = cursor.getLong(cursor.getColumnIndex(KEY_TDC_SALES_ORDER_ID));
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderId;
    }

    /**
     * Method to fetch all un uploaded records from TDC Sales Orders Table
     */
    public List<TDCSaleOrder> fetchAllUnUploadedTDCSalesOrders() {
        List<TDCSaleOrder> allOrdersList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TDC_SALES_ORDERS + " WHERE " + KEY_TDC_SALES_ORDER_UPLOAD_STATUS + "= 0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TDCSaleOrder order = new TDCSaleOrder();

                    long orderId = c.getLong(c.getColumnIndex(KEY_TDC_SALES_ORDER_ID));

                    order.setOrderId(orderId);
                    order.setNoOfItems(c.getInt(c.getColumnIndex(KEY_TDC_SALES_ORDER_NO_OF_ITEMS)));
                    order.setOrderTotalAmount(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_TOTAL_AMOUNT))));
                    order.setOrderTotalTaxAmount(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_TOTAL_TAX_AMOUNT))));
                    order.setOrderSubTotal(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_SUB_TOTAL))));
                    order.setSelectedCustomerId(c.getLong(c.getColumnIndex(KEY_TDC_SALES_ORDER_CUSTOMER_ID)));
                    order.setSelectedCustomerUserId(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_CUSTOMER_USER_ID)));
                    order.setOrderDate(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_DATE)));
                    order.setCreatedOn(c.getLong(c.getColumnIndex(KEY_TDC_SALES_ORDER_CREATED_ON)));
                    order.setCreatedBy(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_CREATED_BY)));
                    order.setIsUploaded(c.getInt(c.getColumnIndex(KEY_TDC_SALES_ORDER_UPLOAD_STATUS)));
                    order.setOrderProductsList(fetchTDCSalesOrderProductsListByOrderId(orderId));

                    allOrdersList.add(order);

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allOrdersList;
    }

    public void updateTDCSalesOrdersTable(long orderId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TDC_SALES_ORDER_UPLOAD_STATUS, 1);

            int status = db.update(TABLE_TDC_SALES_ORDERS, values, KEY_TDC_SALES_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)});

            if (status != -1)
                this.updateTDCSalesOrderProductsTable(orderId);

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    public void updateTDCSalesOrderProductsTable(long orderId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TDC_SOP_UPLOAD_STATUS, 1);

            int status = db.update(TABLE_TDC_SALES_ORDER_PRODUCTS, values, KEY_TDC_SOP_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)});

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to get route code by routeid
     */
    public String getRouteCodeByRouteId(String routeId) {
        String routecode = "";
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_ROUTESDETAILS + " WHERE " + KEY_ROUTE_ID + " = " + "'" + routeId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    routecode = c.getString(c.getColumnIndex(KEY_ROUTE_CODE));
                } while (c.moveToNext());
                c.close();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routecode;
    }

    public void updateUserIdInTDCCustomersTable(long customerId, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TDC_CUSTOMER_USER_ID, userId);

            int status = db.update(TABLE_TDC_CUSTOMERS, values, KEY_TDC_CUSTOMER_ID + " = ?", new String[]{String.valueOf(customerId)});

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to get count of the stake types table
     */
    public int getStakeTypesTableCount() {
        int noOfEvents = 0;
        try {
            String countQuery = "SELECT * FROM " + TABLE_STAKEHOLDER_TYPES;
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
    public void deleteValuesFromStakeTypesTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_STAKEHOLDER_TYPES, null, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to insert stake holder types details
     *
     * @param StakeHolderTypesList
     */
    public void insertStakeTypesDetails(ArrayList<StakeHolderTypes> StakeHolderTypesList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < StakeHolderTypesList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_STAKEHOLDER_TYPE_ID, StakeHolderTypesList.get(i).getmStakeHolderTypeId());
                values.put(KEY_STAKEHOLDER_TYPE_NAME, StakeHolderTypesList.get(i).getmStakeHolderTypeName());
                values.put(KEY_STAKEHOLDER_TYPE, StakeHolderTypesList.get(i).getmStakeHolderType());
                // insert row
                long l = db.insert(TABLE_STAKEHOLDER_TYPES, null, values);
                System.out.println("F*********** INSERTED***************STAKE TYPE" + l);
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to get stake type id by stake type
     * <p>
     * stake_type = 2 for Agents, 3 for Retailers & 4 for Consumer
     */
    public String getStakeTypeIdByStakeType(String stakeType) {
        String stakeTypeId = "";
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_STAKEHOLDER_TYPES + " WHERE " + KEY_STAKEHOLDER_TYPE + " = " + "'" + stakeType + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    stakeTypeId = c.getString(c.getColumnIndex(KEY_STAKEHOLDER_TYPE_ID));
                } while (c.moveToNext());
                c.close();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stakeTypeId;
    }

    /**
     * Method to get count of the tripsheets table
     */
    public int getTripsheetsTableCount() {
        int noOfEvents = 0;
        try {
            String countQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_LIST;
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
     * Method to insert the tripsheets list.
     *
     * @param mTripsheetsList
     */
    public void insertTripsheetsListData(ArrayList<TripsheetsList> mTripsheetsList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mTripsheetsList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_TRIPSHEET_ID, mTripsheetsList.get(i).getmTripshhetId());
                values.put(KEY_TRIPSHEET_CODE, mTripsheetsList.get(i).getmTripshhetCode());
                values.put(KEY_TRIPSHEET_DATE, mTripsheetsList.get(i).getmTripshhetDate());
                values.put(KEY_TRIPSHEET_STATUS, mTripsheetsList.get(i).getmTripshhetStatus());
                values.put(KEY_TRIPSHEET_OB_AMOUNT, mTripsheetsList.get(i).getmTripshhetOBAmount());
                values.put(KEY_TRIPSHEET_ORDERED_AMOUNT, mTripsheetsList.get(i).getmTripshhetOrderedAmount());
                values.put(KEY_TRIPSHEET_RECEIVED_AMOUNT, mTripsheetsList.get(i).getmTripshhetReceivedAmount());
                values.put(KEY_TRIPSHEET_DUE_AMOUNT, mTripsheetsList.get(i).getmTripshhetDueAmount());
                values.put(KEY_TRIPSHEET_ROUTE_CODE, mTripsheetsList.get(i).getmTripshhetRouteCode());
                values.put(KEY_TRIPSHEET_SALESMEN_CODE, mTripsheetsList.get(i).getmTripshhetSalesMenCode());
                values.put(KEY_TRIPSHEET_TRANSPORTER_NAME, mTripsheetsList.get(i).getmTripshhetTrasnsporterName());
                values.put(KEY_TRIPSHEET_VEHICLE_NUMBER, mTripsheetsList.get(i).getmTripshhetVehicleNumber());

                int checkVal = checkTripsheetExistsOrNot(mTripsheetsList.get(i).getmTripshhetId());
                if (checkVal == 0) {
                    System.out.println("+++++++++++++++++++++++++=INSERTED++++++++++++++++++++++"
                            + mTripsheetsList.get(i).getmTripshhetId());
                    values.put(KEY_TRIPSHEET_VERIFY_STATUS, "0");
                    // insert row
                    db.insert(TABLE_TRIPSHEETS_LIST, null, values);
                } else {
                    System.out.println("+++++++++++++++++++++++++=UPDATED++++++++++++++++++++++"
                            + mTripsheetsList.get(i).getmTripshhetId());
                    // Update row
                    db.update(TABLE_TRIPSHEETS_LIST, values, KEY_TRIPSHEET_ID + " = ?",
                            new String[]{String.valueOf(mTripsheetsList.get(i).getmTripshhetId())});
                }

                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to fetch all tripsheets whose status is A from Tripsheets table
     */
    public ArrayList<TripsheetsList> fetchTripsheetsList() {
        ArrayList<TripsheetsList> alltripsheets = new ArrayList<TripsheetsList>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_LIST + " WHERE " + KEY_TRIPSHEET_STATUS + " = " + "'" + "A" + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripsheetsList tripsheetsListBean = new TripsheetsList();
                    tripsheetsListBean.setmTripshhetId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_ID)));
                    tripsheetsListBean.setmTripshhetCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_CODE)));
                    tripsheetsListBean.setmTripshhetDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DATE)));
                    tripsheetsListBean.setmTripshhetStatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STATUS)));
                    tripsheetsListBean.setmTripshhetOBAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_OB_AMOUNT)));
                    tripsheetsListBean.setmTripshhetOrderedAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_ORDERED_AMOUNT)));
                    tripsheetsListBean.setmTripshhetReceivedAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RECEIVED_AMOUNT)));
                    tripsheetsListBean.setmTripshhetDueAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DUE_AMOUNT)));
                    tripsheetsListBean.setmTripshhetRouteCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_ROUTE_CODE)));
                    tripsheetsListBean.setmTripshhetSalesMenCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_SALESMEN_CODE)));
                    tripsheetsListBean.setmTripshhetVehicleNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_VEHICLE_NUMBER)));
                    tripsheetsListBean.setmTripshhetTrasnsporterName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_TRANSPORTER_NAME)));
                    tripsheetsListBean.setmTripshhetVerifyStatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_VERIFY_STATUS)));

                    alltripsheets.add(tripsheetsListBean);

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alltripsheets;
    }

    /**
     * Method to fetch special prices for particular user
     */
    public Map<String, String> fetchSpecialPricesForUserId(String userId) {
        Map<String, String> allSpecialPriceForUser = new HashMap<>();

        try {
            String selectQuery = "SELECT  * FROM " + TABLE_SPECIALPRICE + " WHERE " + KEY_USER_SPECIALID + "='" + userId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    allSpecialPriceForUser.put(c.getString(c.getColumnIndex(KEY_PRODUCT_SPECIALID)), c.getString(c.getColumnIndex(KEY_SPECIALPRICE)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allSpecialPriceForUser;
    }


    /**
     * Method to get count of the tripsheets stock list table
     */
    public int getTripsheetsStockTableCount() {
        int noOfEvents = 0;
        try {
            String countQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_STOCK_LIST;
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
     * Method to insert the mTripsheetsStockList.
     *
     * @param mTripsheetsStockList
     */
    public void insertTripsheetsStockListData(ArrayList<TripsheetsStockList> mTripsheetsStockList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mTripsheetsStockList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_TRIPSHEET_STOCK_ID, mTripsheetsStockList.get(i).getmTripsheetStockId());
                values.put(KEY_TRIPSHEET_STOCK_TRIPSHEET_ID, mTripsheetsStockList.get(i).getmTripsheetStockTripsheetId());
                values.put(KEY_TRIPSHEET_STOCK_PRODUCT_CODE, mTripsheetsStockList.get(i).getmTripsheetStockProductCode());
                values.put(KEY_TRIPSHEET_STOCK_PRODUCT_NAME, mTripsheetsStockList.get(i).getmTripsheetStockProductName());
                values.put(KEY_TRIPSHEET_STOCK_PRODUCT_ID, mTripsheetsStockList.get(i).getmTripsheetStockProductId());
                values.put(KEY_TRIPSHEET_STOCK_ORDER_QUANTITY, mTripsheetsStockList.get(i).getmTripsheetStockProductOrderQuantity());
                values.put(KEY_TRIPSHEET_STOCK_DISPATCH_QUANTITY, mTripsheetsStockList.get(i).getmTripsheetStockDispatchQuantity());
                values.put(KEY_TRIPSHEET_STOCK_DISPATCH_DATE, mTripsheetsStockList.get(i).getmTripsheetStockDispatchDate());
                values.put(KEY_TRIPSHEET_STOCK_DISPATCH_BY, mTripsheetsStockList.get(i).getmTripsheetStockDispatchBy());
                values.put(KEY_TRIPSHEET_STOCK_VERIFY_QUANTITY, mTripsheetsStockList.get(i).getmTripsheetStockVerifiedQuantity());
                values.put(KEY_TRIPSHEET_STOCK_VERIFY_DATE, mTripsheetsStockList.get(i).getmTripsheetStockVerifiedDate());
                values.put(KEY_TRIPSHEET_STOCK_VERIFY_BY, mTripsheetsStockList.get(i).getmTripsheetStockVerifyBy());

                db.insert(TABLE_TRIPSHEETS_STOCK_LIST, null, values);
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    /**
     * Method to fetch all tripsheets stock list baed on tripsheet id from Tripsheets stock list table
     */
    public ArrayList<TripsheetsStockList> fetchAllTripsheetsStockList(String tripsheetId) {
        ArrayList<TripsheetsStockList> alltripsheetsStock = new ArrayList<TripsheetsStockList>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_STOCK_LIST + " WHERE " + KEY_TRIPSHEET_STOCK_TRIPSHEET_ID + " = " + "'" + tripsheetId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripsheetsStockList tripStockBean = new TripsheetsStockList();

                    tripStockBean.setmTripsheetStockTripsheetId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_TRIPSHEET_ID)));
                    tripStockBean.setmTripsheetStockId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_ID)));
                    tripStockBean.setmTripsheetStockProductId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_PRODUCT_ID)));
                    tripStockBean.setmTripsheetStockProductCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_PRODUCT_CODE)));
                    tripStockBean.setmTripsheetStockProductName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_PRODUCT_NAME)));
                    tripStockBean.setmTripsheetStockProductOrderQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_ORDER_QUANTITY)));
                    tripStockBean.setmTripsheetStockDispatchBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_DISPATCH_BY)));
                    tripStockBean.setmTripsheetStockDispatchDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_DISPATCH_DATE)));
                    tripStockBean.setmTripsheetStockDispatchQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_DISPATCH_QUANTITY)));
                    tripStockBean.setmTripsheetStockVerifiedDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_VERIFY_DATE)));
                    tripStockBean.setmTripsheetStockVerifiedQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_VERIFY_QUANTITY)));
                    tripStockBean.setmTripsheetStockVerifyBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_VERIFY_BY)));
                    tripStockBean.setIsStockDispatched(c.getInt(c.getColumnIndex(KEY_TRIPSHEET_STOCK_IS_DISPATCHED)));
                    tripStockBean.setIsStockVerified(c.getInt(c.getColumnIndex(KEY_TRIPSHEET_STOCK_IS_VERIFIED)));
                    tripStockBean.setInStockQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_IN_STOCK_QUANTITY)));
                    tripStockBean.setExtraQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_EXTRA_QUANTITY)));

                    alltripsheetsStock.add(tripStockBean);

                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alltripsheetsStock;
    }


    /**
     * Method to insert the mTripsheetsDeliveriesList.
     *
     * @param mTripsheetsDeliveriesList
     */
    public void insertTripsheetsDeliveriesListData(ArrayList<TripSheetDeliveriesBean> mTripsheetsDeliveriesList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mTripsheetsDeliveriesList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_TRIPSHEET_DELIVERY_TRIP_ID, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_tripId());
                values.put(KEY_TRIPSHEET_DELIVERY_USER_ID, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_userId());
                values.put(KEY_TRIPSHEET_DELIVERY_USER_CODES, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_userCodes());
                values.put(KEY_TRIPSHEET_DELIVERY_ROUTE_ID, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_routeId());
                values.put(KEY_TRIPSHEET_DELIVERY_ROUTE_CODES, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_routeCodes());
                values.put(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_productId());
                values.put(KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_productCodes());
                values.put(KEY_TRIPSHEET_DELIVERY_TAXPERCENT, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_TaxPercent());
                values.put(KEY_TRIPSHEET_DELIVERY_UNITPRICE, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_UnitPrice());
                values.put(KEY_TRIPSHEET_DELIVERY_QUANTITY, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_Quantity());
                values.put(KEY_TRIPSHEET_DELIVERY_AMOUNT, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_Amount());
                values.put(KEY_TRIPSHEET_DELIVERY_TAXAMOUNT, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_TaxAmount());
                values.put(KEY_TRIPSHEET_DELIVERY_TAXTOTAL, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_TaxTotal());
                values.put(KEY_TRIPSHEET_DELIVERY_SALEVALUE, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_SaleValue());
                values.put(KEY_TRIPSHEET_DELIVERY_STATUS, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_Status());
                values.put(KEY_TRIPSHEET_DELIVERY_DELETE, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_Delete());
                values.put(KEY_TRIPSHEET_DELIVERY_CREATEDBY, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_CreatedBy());
                values.put(KEY_TRIPSHEET_DELIVERY_CREATEDON, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_CreatedOn());
                values.put(KEY_TRIPSHEET_DELIVERY_UPDATEDON, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_UpdatedOn());
                values.put(KEY_TRIPSHEET_DELIVERY_UPDATEDBY, mTripsheetsDeliveriesList.get(i).getmTripsheetDelivery_UpdatedBy());

                db.insert(TABLE_TRIPSHEETS_DELIVERIES_LIST, null, values);
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    /**
     * Method to fetch all tripsheets deliveries list
     */
    public ArrayList<TripSheetDeliveriesBean> fetchAllTripsheetsDeliveriesList(String tripsheetId) {
        ArrayList<TripSheetDeliveriesBean> alltripsheetsDeliveries = new ArrayList<TripSheetDeliveriesBean>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = " + "'" + tripsheetId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripSheetDeliveriesBean tripDeliveriesBean = new TripSheetDeliveriesBean();

                    tripDeliveriesBean.setmTripsheetDeliveryNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NO)));
                    tripDeliveriesBean.setmTripsheetDelivery_tripId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TRIP_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_userId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_USER_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_userCodes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_USER_CODES)));
                    tripDeliveriesBean.setmTripsheetDelivery_routeId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_ROUTE_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_routeCodes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_ROUTE_CODES)));
                    tripDeliveriesBean.setmTripsheetDelivery_productId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)));
                    tripDeliveriesBean.setmTripsheetDelivery_productCodes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES)));
                    tripDeliveriesBean.setmTripsheetDelivery_TaxPercent(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXPERCENT)));
                    tripDeliveriesBean.setmTripsheetDelivery_UnitPrice(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_UNITPRICE)));
                    tripDeliveriesBean.setmTripsheetDelivery_Quantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_QUANTITY)));
                    tripDeliveriesBean.setmTripsheetDelivery_Amount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_AMOUNT)));
                    tripDeliveriesBean.setmTripsheetDelivery_TaxAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXAMOUNT)));
                    tripDeliveriesBean.setmTripsheetDelivery_TaxTotal(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXTOTAL)));
                    tripDeliveriesBean.setmTripsheetDelivery_SaleValue(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SALEVALUE)));
                    tripDeliveriesBean.setmTripsheetDelivery_Status(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_STATUS)));
                    tripDeliveriesBean.setmTripsheetDelivery_Delete(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_DELETE)));
                    tripDeliveriesBean.setmTripsheetDelivery_CreatedBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDBY)));
                    tripDeliveriesBean.setmTripsheetDelivery_CreatedOn(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDON)));
                    tripDeliveriesBean.setmTripsheetDelivery_UpdatedOn(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_UPDATEDON)));
                    tripDeliveriesBean.setmTripsheetDelivery_UpdatedBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_UPDATEDBY)));


                    alltripsheetsDeliveries.add(tripDeliveriesBean);

                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alltripsheetsDeliveries;
    }

    /**
     * Method to insert the mTripsheetsReturnsList.
     *
     * @param mTripsheetsReturnsList
     */
    public void insertTripsheetsReturnsListData(ArrayList<TripSheetReturnsBean> mTripsheetsReturnsList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mTripsheetsReturnsList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_TRIPSHEET_RETURNS_RETURN_NO, mTripsheetsReturnsList.get(i).getmTripshhetReturnsReturn_no());
                values.put(KEY_TRIPSHEET_RETURNS_TRIP_ID, mTripsheetsReturnsList.get(i).getmTripshhetReturnsTrip_id());
                values.put(KEY_TRIPSHEET_RETURNS_USER_ID, mTripsheetsReturnsList.get(i).getmTripshhetReturnsUser_id());
                values.put(KEY_TRIPSHEET_RETURNS_USER_CODES, mTripsheetsReturnsList.get(i).getmTripshhetReturnsUser_codes());
                values.put(KEY_TRIPSHEET_RETURNS_ROUTE_ID, mTripsheetsReturnsList.get(i).getmTripshhetReturnsRoute_id());
                values.put(KEY_TRIPSHEET_RETURNS_ROUTE_CODES, mTripsheetsReturnsList.get(i).getmTripshhetReturnsRoute_codes());
                values.put(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS, mTripsheetsReturnsList.get(i).getmTripshhetReturnsProduct_ids());
                values.put(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES, mTripsheetsReturnsList.get(i).getmTripshhetReturnsProduct_codes());
                values.put(KEY_TRIPSHEET_RETURNS_TAX_PERCENT, mTripsheetsReturnsList.get(i).getmTripshhetReturnsTax_percent());
                values.put(KEY_TRIPSHEET_RETURNS_UNIT_PRICE, mTripsheetsReturnsList.get(i).getmTripshhetReturnsUnit_price());
                values.put(KEY_TRIPSHEET_RETURNS_QUANTITY, mTripsheetsReturnsList.get(i).getmTripshhetReturnsQuantity());
                values.put(KEY_TRIPSHEET_RETURNS_AMOUNT, mTripsheetsReturnsList.get(i).getmTripshhetReturnsAmount());
                values.put(KEY_TRIPSHEET_RETURNS_TAX_AMOUNT, mTripsheetsReturnsList.get(i).getmTripshhetReturnsTax_amount());
                values.put(KEY_TRIPSHEET_RETURNS_TAX_TOTAL, mTripsheetsReturnsList.get(i).getmTripshhetReturnsTax_total());
                values.put(KEY_TRIPSHEET_RETURNS_SALE_VALUE, mTripsheetsReturnsList.get(i).getmTripshhetReturnsSale_value());
                values.put(KEY_TRIPSHEET_RETURNS_TYPE, mTripsheetsReturnsList.get(i).getmTripshhetReturnsType());
                values.put(KEY_TRIPSHEET_RETURNS_STATUS, mTripsheetsReturnsList.get(i).getmTripshhetReturnsStatus());
                values.put(KEY_TRIPSHEET_RETURNS_DELETE, mTripsheetsReturnsList.get(i).getmTripshhetReturnsDelete());
                values.put(KEY_TRIPSHEET_RETURNS_CREATED_BY, mTripsheetsReturnsList.get(i).getmTripshhetReturnsCreated_by());
                values.put(KEY_TRIPSHEET_RETURNS_CREATED_ON, mTripsheetsReturnsList.get(i).getmTripshhetReturnsCreated_on());
                values.put(KEY_TRIPSHEET_RETURNS_UPDATED_ON, mTripsheetsReturnsList.get(i).getmTripshhetReturnsUpdated_on());
                values.put(KEY_TRIPSHEET_RETURNS_UPDATED_BY, mTripsheetsReturnsList.get(i).getmTripshhetReturnsUpdated_by());


                db.insert(TABLE_TRIPSHEETS_RETURNS_LIST, null, values);
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    /**
     * Method to fetch all tripsheets returns list baed on tripsheet id from Tripsheets returns list table
     */
    public ArrayList<TripSheetReturnsBean> fetchAllTripsheetsReturnsList(String tripsheetId) {
        ArrayList<TripSheetReturnsBean> alltripsheetsReturns = new ArrayList<TripSheetReturnsBean>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_TRIP_ID + " = " + "'" + tripsheetId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripSheetReturnsBean tripReturnsBean = new TripSheetReturnsBean();

                    tripReturnsBean.setmTripshhetReturnsReturn_no(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_RETURN_NO)));
                    tripReturnsBean.setmTripshhetReturnsTrip_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_TRIP_ID)));
                    tripReturnsBean.setmTripshhetReturnsUser_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_USER_ID)));
                    tripReturnsBean.setmTripshhetReturnsUser_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_USER_CODES)));
                    tripReturnsBean.setmTripshhetReturnsRoute_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_ROUTE_ID)));
                    tripReturnsBean.setmTripshhetReturnsRoute_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_ROUTE_CODES)));
                    tripReturnsBean.setmTripshhetReturnsProduct_ids(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS)));
                    tripReturnsBean.setmTripshhetReturnsProduct_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES)));
                    tripReturnsBean.setmTripshhetReturnsTax_percent(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_TAX_PERCENT)));
                    tripReturnsBean.setmTripshhetReturnsUnit_price(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_UNIT_PRICE)));
                    tripReturnsBean.setmTripshhetReturnsQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_QUANTITY)));
                    tripReturnsBean.setmTripshhetReturnsAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_AMOUNT)));
                    tripReturnsBean.setmTripshhetReturnsTax_amount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_TAX_AMOUNT)));
                    tripReturnsBean.setmTripshhetReturnsTax_total(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_TAX_TOTAL)));
                    tripReturnsBean.setmTripshhetReturnsSale_value(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_SALE_VALUE)));
                    tripReturnsBean.setmTripshhetReturnsType(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_TYPE)));
                    tripReturnsBean.setmTripshhetReturnsStatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_STATUS)));
                    tripReturnsBean.setmTripshhetReturnsDelete(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_DELETE)));
                    tripReturnsBean.setmTripshhetReturnsCreated_by(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_CREATED_BY)));
                    tripReturnsBean.setmTripshhetReturnsCreated_on(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_CREATED_ON)));
                    tripReturnsBean.setmTripshhetReturnsUpdated_on(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_UPDATED_ON)));
                    tripReturnsBean.setmTripshhetReturnsUpdated_by(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_UPDATED_BY)));

                    alltripsheetsReturns.add(tripReturnsBean);

                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alltripsheetsReturns;
    }


    /**
     * Method to insert the mTripsheetsPaymentsList.
     *
     * @param mTripsheetsPaymentsList
     */
    public void insertTripsheetsPaymentsListData(ArrayList<TripSheetPaymentsBean> mTripsheetsPaymentsList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mTripsheetsPaymentsList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_TRIPSHEET_PAYMENTS_PAYMENT_NO, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsPayment_no());

                values.put(KEY_TRIPSHEET_PAYMENTS_TRIP_ID, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsTrip_id());
                values.put(KEY_TRIPSHEET_PAYMENTS_USER_ID, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsUser_id());
                values.put(KEY_TRIPSHEET_PAYMENTS_USER_CODES, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsUser_codes());
                values.put(KEY_TRIPSHEET_PAYMENTS_ROUTE_ID, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsRoute_id());
                values.put(KEY_TRIPSHEET_PAYMENTS_ROUTE_CODES, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsRoute_codes());

                values.put(KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsChe_trans_id());
                values.put(KEY_TRIPSHEET_PAYMENTS_AC_CA_NO, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsAc_ca_no());
                values.put(KEY_TRIPSHEET_PAYMENTS_ACOUNT_NAME, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsAccount_name());
                values.put(KEY_TRIPSHEET_PAYMENTS_BANK_NAME, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsBank_name());
                values.put(KEY_TRIPSHEET_PAYMENTS_TRANS_DATE, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsTrans_date());
                values.put(KEY_TRIPSHEET_PAYMENTS_TRANS_CLEAR_DATE, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsTrans_clear_date());
                values.put(KEY_TRIPSHEET_PAYMENTS_RECEIVER_NAME, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsReceiver_name());
                values.put(KEY_TRIPSHEET_PAYMENTS_TRANS_STATUS, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsTrans_status());


                values.put(KEY_TRIPSHEET_PAYMENTS_PRODUCTS_IDS, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsProduct_ids());
                values.put(KEY_TRIPSHEET_PAYMENTS_PRODUCT_CODES, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsProduct_codes());
                values.put(KEY_TRIPSHEET_PAYMENTS_TAX_PERCENT, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsTax_percent());
                values.put(KEY_TRIPSHEET_PAYMENTS_UNIT_PRICE, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsUnit_price());
                values.put(KEY_TRIPSHEET_PAYMENTS_QUANTITY, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsQuantity());
                values.put(KEY_TRIPSHEET_PAYMENTS_AMOUNT, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsAmount());
                values.put(KEY_TRIPSHEET_PAYMENTS_TAX_AMOUNT, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsTax_amount());
                values.put(KEY_TRIPSHEET_PAYMENTS_TAX_TOTAL, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsTax_total());
                values.put(KEY_TRIPSHEET_PAYMENTS_SALE_VALUE, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsSale_value());
                values.put(KEY_TRIPSHEET_PAYMENTS_TYPE, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsType());
                values.put(KEY_TRIPSHEET_PAYMENTS_STATUS, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsStatus());
                values.put(KEY_TRIPSHEET_PAYMENTS_DELETE, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsDelete());
                values.put(KEY_TRIPSHEET_PAYMENTS_CREATED_BY, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsCreated_by());
                values.put(KEY_TRIPSHEET_PAYMENTS_CREATED_ON, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsCreated_on());
                values.put(KEY_TRIPSHEET_PAYMENTS_UPDATED_ON, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsUpdated_on());
                values.put(KEY_TRIPSHEET_PAYMENTS_UPDATED_BY, mTripsheetsPaymentsList.get(i).getmTripshhetPaymentsUpdated_by());


                db.insert(TABLE_TRIPSHEETS_PAYMENTS_LIST, null, values);
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    /**
     * Method to fetch all tripsheets payments list baed on tripsheet id from Tripsheets payments list table
     */
    public ArrayList<TripSheetPaymentsBean> fetchAllTripsheetsPaymentsList(String tripsheetId) {
        ArrayList<TripSheetPaymentsBean> alltripsheetsPayments = new ArrayList<TripSheetPaymentsBean>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_TRIP_ID + " = " + "'" + tripsheetId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripSheetPaymentsBean tripPaymentsBean = new TripSheetPaymentsBean();

                    tripPaymentsBean.setmTripshhetPaymentsPayment_no(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_PAYMENT_NO)));

                    tripPaymentsBean.setmTripshhetPaymentsTrip_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRIP_ID)));
                    tripPaymentsBean.setmTripshhetPaymentsUser_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_USER_ID)));
                    tripPaymentsBean.setmTripshhetPaymentsUser_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_USER_CODES)));
                    tripPaymentsBean.setmTripshhetPaymentsRoute_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_ROUTE_ID)));
                    tripPaymentsBean.setmTripshhetPaymentsRoute_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_ROUTE_CODES)));

                    tripPaymentsBean.setmTripshhetPaymentsChe_trans_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID)));
                    tripPaymentsBean.setmTripshhetPaymentsAc_ca_no(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_AC_CA_NO)));
                    tripPaymentsBean.setmTripshhetPaymentsAccount_name(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_ACOUNT_NAME)));
                    tripPaymentsBean.setmTripshhetPaymentsBank_name(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_BANK_NAME)));
                    tripPaymentsBean.setmTripshhetPaymentsTrans_date(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_DATE)));
                    tripPaymentsBean.setmTripshhetPaymentsTrans_clear_date(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_CLEAR_DATE)));
                    tripPaymentsBean.setmTripshhetPaymentsReceiver_name(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_RECEIVER_NAME)));
                    tripPaymentsBean.setmTripshhetPaymentsTrans_status(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_STATUS)));


                    tripPaymentsBean.setmTripshhetPaymentsProduct_ids(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_PRODUCTS_IDS)));
                    tripPaymentsBean.setmTripshhetPaymentsProduct_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_PRODUCT_CODES)));
                    tripPaymentsBean.setmTripshhetPaymentsTax_percent(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TAX_PERCENT)));
                    tripPaymentsBean.setmTripshhetPaymentsUnit_price(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_UNIT_PRICE)));
                    tripPaymentsBean.setmTripshhetPaymentsQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_QUANTITY)));
                    tripPaymentsBean.setmTripshhetPaymentsAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_AMOUNT)));
                    tripPaymentsBean.setmTripshhetPaymentsTax_amount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TAX_AMOUNT)));
                    tripPaymentsBean.setmTripshhetPaymentsTax_total(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TAX_TOTAL)));
                    tripPaymentsBean.setmTripshhetPaymentsSale_value(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_SALE_VALUE)));
                    tripPaymentsBean.setmTripshhetPaymentsType(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TYPE)));
                    tripPaymentsBean.setmTripshhetPaymentsStatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_STATUS)));
                    tripPaymentsBean.setmTripshhetPaymentsDelete(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_DELETE)));
                    tripPaymentsBean.setmTripshhetPaymentsCreated_by(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CREATED_BY)));
                    tripPaymentsBean.setmTripshhetPaymentsCreated_on(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CREATED_ON)));
                    tripPaymentsBean.setmTripshhetPaymentsUpdated_on(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_UPDATED_ON)));
                    tripPaymentsBean.setmTripshhetPaymentsUpdated_by(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_UPDATED_BY)));

                    alltripsheetsPayments.add(tripPaymentsBean);

                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alltripsheetsPayments;
    }

    /**
     * Method to fetch agent latitude and longitude based on agentid
     */
    public Map<String, String> getLatLangOfAgentByAgentId(String agentId) {
        Map<String, String> agentLatLangDetails = new HashMap<>();

        try {
            String selectQuery = "SELECT  * FROM " + TABLE_AGENTS + " WHERE " + KEY_AGENT_ID + "='" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    agentLatLangDetails.put(agentId + "_Lat", c.getString(c.getColumnIndex(KEY_AGENT_LATITUDE)));
                    agentLatLangDetails.put(agentId + "_Lang", c.getString(c.getColumnIndex(KEY_AGENT_LONGITUDE)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return agentLatLangDetails;
    }

    /**
     * Method to insert the tripsheets so list.
     *
     * @param mTripsheetsList
     */
    public void insertTripsheetsSOListData(ArrayList<TripsheetSOList> mTripsheetsList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mTripsheetsList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_TRIPSHEET_SO_ID, mTripsheetsList.get(i).getmTripshetSOId());
                values.put(KEY_TRIPSHEET_SO_TRIPID, mTripsheetsList.get(i).getmTripshetSOTripId());
                values.put(KEY_TRIPSHEET_SO_AGENTCODE, mTripsheetsList.get(i).getmTripshetSOAgentCode());
                values.put(KEY_TRIPSHEET_SO_CODE, mTripsheetsList.get(i).getmTripshetSOCode());
                values.put(KEY_TRIPSHEET_SO_DATE, mTripsheetsList.get(i).getmTripshetSODate());
                values.put(KEY_TRIPSHEET_SO_VALUE, mTripsheetsList.get(i).getmTripshetSOValue());
                values.put(KEY_TRIPSHEET_SO_OPAMOUNT, mTripsheetsList.get(i).getmTripshetSOOpAmount());
                values.put(KEY_TRIPSHEET_SO_CBAMOUNT, mTripsheetsList.get(i).getmTripshetSOCBAmount());
                values.put(KEY_TRIPSHEET_SO_AGENTID, mTripsheetsList.get(i).getmTripshetSOAgentId());
                values.put(KEY_TRIPSHEET_SO_AGENTFIRSTNAME, mTripsheetsList.get(i).getmTripshetSOAgentFirstName());
                values.put(KEY_TRIPSHEET_SO_AGENTLASTNAME, mTripsheetsList.get(i).getmTripshetSOAgentLastName());
                values.put(KEY_TRIPSHEET_SO_PRODUCTCODE, mTripsheetsList.get(i).getmTripshetSOProductCode());
                values.put(KEY_TRIPSHEET_SO_PRODUCTORDER_QUANTITY, mTripsheetsList.get(i).getmTripshetSOProductOrderQuantity());
                values.put(KEY_TRIPSHEET_SO_PRODUCT_VALUE, mTripsheetsList.get(i).getmTripshetSOProductValue());
                values.put(KEY_TRIPSHEET_SO_APPROVEDBY, mTripsheetsList.get(i).getmTripshetSOApprovedBy());
                values.put(KEY_TRIPSHEET_SO_AGENTLATITUDE, mTripsheetsList.get(i).getmTripshetSOAgentLatitude());
                values.put(KEY_TRIPSHEET_SO_AGENTLONGITUDE, mTripsheetsList.get(i).getmTripshetSOAgentLongitude());
//                int checkVal = checkSpecialPriceProductExistsOrNot(mSpecialPriceBeansList.get(i).getSpecialProductId()
//                        , mSpecialPriceBeansList.get(i).getSpecialUserId());
//                if (checkVal == 0) {
//                    // insert row
//                    db.insert(TABLE_SPECIALPRICE, null, values);
//                    System.out.println("F*********** INSERTED***************88");
//                } else {
//                    // Update row
//                    db.update(TABLE_SPECIALPRICE, values, KEY_PRODUCT_SPECIALID + " = ?" + " AND " + KEY_USER_SPECIALID + " = ? ",
//                            new String[]{String.valueOf(mSpecialPriceBeansList.get(i).getSpecialProductId()),
//                                    String.valueOf(mSpecialPriceBeansList.get(i).getSpecialUserId())});
//                    System.out.println("F*********** UPDATED***************88");
//                }
                db.insert(TABLE_TRIPSHEETS_SO_LIST, null, values);
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    public ProductsBean fetchProductDetailsByProductCode(String productCode) {
        ProductsBean productsBean = null;

        try {
            String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_CODE + " = " + productCode;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    System.out.println("+++++++ ITERATE++++++++++");
                    productsBean = new ProductsBean();
                    productsBean.setProductId((c.getString(c.getColumnIndex(KEY_PRODUCT_ID))));
                    productsBean.setProductTitle((c.getString(c.getColumnIndex(KEY_PRODUCT_TITLE))));
                    productsBean.setProductAgentPrice((c.getString(c.getColumnIndex(KEY_PRODUCT_AGENT_PRICE))));
                    productsBean.setProductgst((c.getString(c.getColumnIndex(KEY_PRODUCT_GST_PRICE))));
                    productsBean.setProductvat((c.getString(c.getColumnIndex(KEY_PRODUCT_VAT_PRICE))));
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productsBean;
    }


    /**
     * Method to insert the mNotificationsList.
     *
     * @param mNotificationsList
     */
    public void insertNotificationsListData(ArrayList<NotificationBean> mNotificationsList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mNotificationsList.size(); i++) {

                if (!verification(mNotificationsList.get(i).getNotification_id(), db)) {
                    ContentValues values = new ContentValues();
                    values.put(KEY_NOTIFICATIONS_ID, mNotificationsList.get(i).getNotification_id());
                    values.put(KEY_NOTIFICATIONS_DATE, mNotificationsList.get(i).getDate());

                    values.put(KEY_NOTIFICATIONS_NAME, mNotificationsList.get(i).getName());
                    values.put(KEY_NOTIFICATIONS_DESCRIPTION, mNotificationsList.get(i).getDescription());


                    db.insert(TABLE_NOTIFICATION_LIST, null, values);
                    Log.e("inserten", values + "");
                    values.clear();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public boolean verification(String _username, SQLiteDatabase db) {

        Cursor c = db.rawQuery("SELECT 1 FROM " + TABLE_NOTIFICATION_LIST + " WHERE " + KEY_NOTIFICATIONS_ID + "=?", new String[]{_username});
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    /**
     * Method to fetch all notifications list baed on tripsheet id from Notification list table
     */
    public ArrayList<NotificationBean> fetchAllNotificationsList() {
        ArrayList<NotificationBean> notificationsList = new ArrayList<NotificationBean>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NOTIFICATION_LIST;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    NotificationBean notificationsBean = new NotificationBean();

                    notificationsBean.setDate(c.getString(c.getColumnIndex(KEY_NOTIFICATIONS_DATE)));

                    notificationsBean.setName(c.getString(c.getColumnIndex(KEY_NOTIFICATIONS_NAME)));
                    notificationsBean.setDescription(c.getString(c.getColumnIndex(KEY_NOTIFICATIONS_DESCRIPTION)));

                    notificationsList.add(notificationsBean);

                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return notificationsList;
    }

    public void updateTripSheetStockDispatchList(TripsheetsStockList currentStock) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TRIPSHEET_STOCK_DISPATCH_QUANTITY, currentStock.getmTripsheetStockDispatchQuantity());
            values.put(KEY_TRIPSHEET_STOCK_DISPATCH_DATE, currentStock.getmTripsheetStockDispatchDate());
            values.put(KEY_TRIPSHEET_STOCK_DISPATCH_BY, currentStock.getmTripsheetStockDispatchBy());
            values.put(KEY_TRIPSHEET_STOCK_IS_DISPATCHED, 1);

            int status = db.update(TABLE_TRIPSHEETS_STOCK_LIST, values, KEY_TRIPSHEET_STOCK_ID + " = ? AND " + KEY_TRIPSHEET_STOCK_PRODUCT_CODE + " = ?", new String[]{currentStock.getmTripsheetStockId(), currentStock.getmTripsheetStockProductCode()});

            values.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    public void updateTripSheetStockVerifyList(TripsheetsStockList currentStock) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TRIPSHEET_STOCK_VERIFY_QUANTITY, currentStock.getmTripsheetStockVerifiedQuantity());
            values.put(KEY_TRIPSHEET_STOCK_VERIFY_DATE, currentStock.getmTripsheetStockVerifiedDate());
            values.put(KEY_TRIPSHEET_STOCK_VERIFY_BY, currentStock.getmTripsheetStockVerifyBy());
            values.put(KEY_TRIPSHEET_STOCK_IN_STOCK_QUANTITY, currentStock.getInStockQuantity());
            values.put(KEY_TRIPSHEET_STOCK_EXTRA_QUANTITY, currentStock.getExtraQuantity());
            values.put(KEY_TRIPSHEET_STOCK_IS_VERIFIED, 1);

            int status = db.update(TABLE_TRIPSHEETS_STOCK_LIST, values, KEY_TRIPSHEET_STOCK_ID + " = ? AND " + KEY_TRIPSHEET_STOCK_PRODUCT_CODE + " = ?", new String[]{currentStock.getmTripsheetStockId(), currentStock.getmTripsheetStockProductCode()});

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    public void updateTripSheetStockVerifyStatus(String tripSheetId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TRIPSHEET_VERIFY_STATUS, 1);

            db.update(TABLE_TRIPSHEETS_LIST, values, KEY_TRIPSHEET_ID + " = ? ", new String[]{tripSheetId});

            values.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to fetch un uploaded trip sheets stock list
     */
    public ArrayList<TripsheetsStockList> fetchUnUploadedTripSheetStockList(String stockId) {
        ArrayList<TripsheetsStockList> tripsheetsStockLists = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_STOCK_LIST + " WHERE " + KEY_TRIPSHEET_STOCK_ID + " = '" + stockId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripsheetsStockList tripStockBean = new TripsheetsStockList();
                    tripStockBean.setmTripsheetStockTripsheetId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_TRIPSHEET_ID)));
                    tripStockBean.setmTripsheetStockId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_ID)));
                    tripStockBean.setmTripsheetStockProductId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_PRODUCT_ID)));
                    tripStockBean.setmTripsheetStockProductCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_PRODUCT_CODE)));
                    tripStockBean.setmTripsheetStockProductName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_PRODUCT_NAME)));
                    tripStockBean.setmTripsheetStockProductOrderQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_ORDER_QUANTITY)));
                    tripStockBean.setmTripsheetStockDispatchBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_DISPATCH_BY)));
                    tripStockBean.setmTripsheetStockDispatchDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_DISPATCH_DATE)));
                    tripStockBean.setmTripsheetStockDispatchQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_DISPATCH_QUANTITY)));
                    tripStockBean.setmTripsheetStockVerifiedDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_VERIFY_DATE)));
                    tripStockBean.setmTripsheetStockVerifiedQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_VERIFY_QUANTITY)));
                    tripStockBean.setmTripsheetStockVerifyBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_VERIFY_BY)));
                    tripStockBean.setIsStockDispatched(c.getInt(c.getColumnIndex(KEY_TRIPSHEET_STOCK_IS_DISPATCHED)));
                    tripStockBean.setIsStockVerified(c.getInt(c.getColumnIndex(KEY_TRIPSHEET_STOCK_IS_VERIFIED)));

                    tripsheetsStockLists.add(tripStockBean);

                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripsheetsStockLists;
    }

    public void updateTripSheetStockTable(String stockId, String actionType) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            if (actionType.equals("dispatch"))
                values.put(KEY_TRIPSHEET_STOCK_DISPATCHED_UPLOAD_STATUS, 1);
            else
                values.put(KEY_TRIPSHEET_STOCK_VERIFIED_UPLOAD_STATUS, 1);

            int status = db.update(TABLE_TRIPSHEETS_STOCK_LIST, values, KEY_TRIPSHEET_STOCK_ID + " = ?", new String[]{String.valueOf(stockId)});

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to check weather the tripsheet exists or not using tripsheetid.
     *
     * @param tripsheetId
     * @return integer value
     */
    public int checkTripsheetExistsOrNot(String tripsheetId) {
        int maxID = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_TRIPSHEETS_LIST + " WHERE " + KEY_TRIPSHEET_ID + "='" + tripsheetId + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //System.out.println("DDDD::: "+ cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                maxID = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        //System.out.println("FGGHH::: "+maxID);
        return maxID;
    }

    /**
     * Method to fetch all stock quantity from combination od products table and stock table
     *
     * @param tripsheetId
     */
    public ArrayList<DeliverysBean> fetchAllRecordsFromProductsAndStockTableForDeliverys(String tripsheetId) {
        ArrayList<DeliverysBean> deliverysBeanArrayList = new ArrayList<>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_TRIPSHEETS_STOCK_LIST + " s LEFT JOIN " + TABLE_PRODUCTS + " p ON s." + KEY_TRIPSHEET_STOCK_PRODUCT_CODE
                    + "=p." + KEY_PRODUCT_CODE + " WHERE " + KEY_TRIPSHEET_STOCK_TRIPSHEET_ID + " = '" + tripsheetId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    DeliverysBean productsBean = new DeliverysBean();

                    productsBean.setProductId((c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_PRODUCT_ID))));
                    productsBean.setProductCode((c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_PRODUCT_CODE))));
                    productsBean.setProductTitle((c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_PRODUCT_NAME))));
                    productsBean.setProductAgentPrice((c.getString(c.getColumnIndex(KEY_PRODUCT_AGENT_PRICE))));
                    productsBean.setProductConsumerPrice((c.getString(c.getColumnIndex(KEY_PRODUCT_CONSUMER_PRICE))));
                    productsBean.setProductRetailerPrice((c.getString(c.getColumnIndex(KEY_PRODUCT_RETAILER_PRICE))));
                    productsBean.setProductgst((c.getString(c.getColumnIndex(KEY_PRODUCT_GST_PRICE))));
                    productsBean.setProductvat((c.getString(c.getColumnIndex(KEY_PRODUCT_VAT_PRICE))));
                    productsBean.setProductOrderedQuantity(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_ORDER_QUANTITY))));
                    productsBean.setProductStock(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_IN_STOCK_QUANTITY))));
                    productsBean.setProductExtraQuantity(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_EXTRA_QUANTITY))));

                    deliverysBeanArrayList.add(productsBean);
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deliverysBeanArrayList;
    }

    /**
     * Method to fetch un uploaded trip sheets stock list group by stock id
     */
    public ArrayList<String> fetchUnUploadedTripSheetUniqueStockIds(String actionType) {
        ArrayList<String> stockIds = new ArrayList<>();

        try {
            String selectQuery;

            if (actionType.equals("dispatch"))
                selectQuery = "SELECT DISTINCT " + KEY_TRIPSHEET_STOCK_ID + " FROM " + TABLE_TRIPSHEETS_STOCK_LIST + " WHERE " + KEY_TRIPSHEET_STOCK_IS_DISPATCHED + " = 1 AND " + KEY_TRIPSHEET_STOCK_DISPATCHED_UPLOAD_STATUS + " = 0";
            else // verify
                selectQuery = "SELECT DISTINCT " + KEY_TRIPSHEET_STOCK_ID + " FROM " + TABLE_TRIPSHEETS_STOCK_LIST + " WHERE " + KEY_TRIPSHEET_STOCK_IS_VERIFIED + " = 1 AND " + KEY_TRIPSHEET_STOCK_VERIFIED_UPLOAD_STATUS + " = 0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    stockIds.add(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_ID)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stockIds;
    }

    /**
     * Method to get stake type id by stake type
     * <p>
     * stake_type = 2 for Agents, 3 for Retailers & 4 for Consumer
     */
    public ArrayList<String> getStakeTypeIdByStakeTypeForAgents(String stakeType) {
        ArrayList<String> stakeTypesList = new ArrayList<String>();
        String stakeTypeId = "";
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_STAKEHOLDER_TYPES + " WHERE " + KEY_STAKEHOLDER_TYPE + " = " + "'" + stakeType + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    // stakeTypeId = c.getString(c.getColumnIndex(KEY_STAKEHOLDER_TYPE_ID));
                    stakeTypesList.add(c.getString(c.getColumnIndex(KEY_STAKEHOLDER_TYPE_ID)));
                } while (c.moveToNext());
                c.close();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stakeTypesList;
    }

    /**
     * Method to fetch user privile by user id and privilege name
     */
    public int getUserPrivilegeByUserIdAndPrivilegeName(String userId, String privilegeName) {
        int privilege = 0;
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_PREVILEGES_USER_ACTIVITY + " WHERE " +
                    KEY_USER_ACTIVITY_USER_ID + " = " + "'" + userId + "'" + " AND " + KEY_USER_ACTIVITY_TAG + " = " + "'"
                    + privilegeName + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    privilege = c.getInt(0);
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privilege;
    }
}
