package com.rightclickit.b2bsaleon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rightclickit.b2bsaleon.beanclass.AgentDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.AgentPaymentsBean;
import com.rightclickit.b2bsaleon.beanclass.AgentReturnsBean;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.AgentsStockBean;
import com.rightclickit.b2bsaleon.beanclass.DashboardPendingIndentBean;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;

import com.rightclickit.b2bsaleon.beanclass.DeviceDetails;
import com.rightclickit.b2bsaleon.beanclass.Nextdayindent_moreinfoBeen;
import com.rightclickit.b2bsaleon.beanclass.NotificationBean;
import com.rightclickit.b2bsaleon.beanclass.PaymentsBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.SaleOrderDeliveredProducts;
import com.rightclickit.b2bsaleon.beanclass.SaleOrderReturnedProducts;
import com.rightclickit.b2bsaleon.beanclass.SpecialPriceBean;
import com.rightclickit.b2bsaleon.beanclass.StakeHolderTypes;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.beanclass.TDCSalesOrderProductBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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

    // This table contains agents stock list
    private final String TABLE_AGENTS_STOCK_LIST = "agentd_stock_list";

    // This table contains tripsheets deliveries list
    private final String TABLE_AGENT_STOCK_DELIVERIES_LIST = "agent_stock_deliveries_list";

    // This table contains products and its details for sorting
    private final String TABLE_PRODUCTS_SORT_LIST = "products_sort";

    // This table contains device/user lat and lang for storing
    private final String TABLE_DEVICE_LATLANG_TABLE = "latlang_device";


    // This table contains dashboard pending indents
    private final String TABLE_DASHBOARD_PENDINGINDENTLIST = "dashboard_pendingindent";
    // This table contains dashboard deliveries
    private final String TABLE_DASHBOARD_DELIVERYLIST  = "dashboard_deliveries";
    // This table contains dashboard payments
    private final String TABLE_DASHBOARD_PAYMENTLIST  = "dashboard_payments";


    // This table contains nextindent moreinfo details
    private final String TABLE_NEXTINDENT_MOREINFO  = "nextindent_moreinfo";



    // Column names for DashboardPendingIndent Table
    private final String KEY_ID = "increment_id";
    private final String KEY_DATE = "date";
    private final String KEY_TIME = "time";
    private final String KEY_CREATED_DATE = "created_date";
    private final String KEY_CATEGORY = "category";
    private final String KEY_LITRES = "litres";
    private final String KEY_PENDING_COUNT = "pending_count";
    private final String KEY_APPROVED_COUNT = "approved_count";


    // Column names for DashboardTodaydelivery Table
    private final String KEY_DASHBOARD_DELIVERY_ID = "division_id";
    private final String KEY_DASHBOARD_DELIVERY_DATE= "delivery_date";
    private final String KEY_DASHBOARD_DELIVERY_TIME = "time";
    private final String KEY_DASHBOARD_DELIVERY_CREATED_DATE = "created_date";
    private final String KEY_DASHBOARD_DELIVERY_CATEGORY = "delivery_category";
    private final String KEY_DASHBOARD_DELIVERY_ORDERVALUE= "ordervalue";
    private final String KEY_DASHBOARD_DELIVERY_DELIVERYVALUE = "deliveryvalue";
    private final String KEY_DASHBOARD_DELIVERY_PERCENTAGEVALUE = "percentagevalue";

    // Column names for DashboardTodaypayments Table
    private final String KEY_DASHBOARD_PAYMENTS_ID = "payments_id";
    private final String KEY_DASHBOARD_PAYMENTS_DATE= "payment_date";
    private final String KEY_DASHBOARD_PAYMENT_TIME = "time";
    private final String KEY_DASHBOARD_PAYMENT_CREATED_DATE = "created_date";
    private final String KEY_DASHBOARD_PAYMENTS_DELIVERYVALUE= "payment_deliveryvalue";
    private final String KEY_DASHBOARD_PAYMENTS_RECEIVEDVALUE = "payment_receivedvalue";
    private final String KEY_DASHBOARD_PAYMENTS_DUEVALUE= "payment_duevalue";



    // Column names for NextindentMoreInfo Table
    private final String KEY_NEXTINDENT_MOREINFO_ID = "moreinfo_id";
    private final String KEY_NEXTINDENT_MOREINFO_MILKDATE= "milk_date";
    private final String KEY_NEXTINDENT_MOREINFO_MILKVOL = "milk_vol";
    private final String KEY_NEXTINDENT_MOREINFO_CURDDATE = "curd_date";
    private final String KEY_NEXTINDENT_MOREINFO_CURDVOL = "curd_vol";
    private final String KEY_NEXTINDENT_MOREINFO_OTHRESDATE= "others_date";
    private final String KEY_NEXTINDENT_MOREINFO_OTHERSVOL= "others_vol";
    private final String KEY_NEXTINDENT_MOREINFO_SELDATE= "sel_date";
    private final String KEY_NEXTINDENT_MOREINFO_BACKDATE= "back_date";






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
    private final String KEY_VEHICLE_NUMBER = "vehicle_no";
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
    public final String KEY_PRODUCT_UOM = "product_uom";
    public final String KEY_PRODUCT_AGENT_PRICE = "product_agent_price";
    public final String KEY_PRODUCT_CONSUMER_PRICE = "product_consumer_price";
    public final String KEY_PRODUCT_RETAILER_PRICE = "product_retailer_price";
    public final String KEY_PRODUCT_GST_PRICE = "product_gst_price";
    public final String KEY_PRODUCT_VAT_PRICE = "product_vat_price";
    public final String KEY_PRODUCT_CONTROL_CODE = "control_code";

    // Column names for Agents Table
    private final String KEY_AGENT_UNIQUE_ID = "agent_unique_id";
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
    private final String KEY_AGENT_UPLOAD_STATUS = "agent_upload_status";
    private final String KEY_AGENT_LOGIN_USER_ID = "agent_login_user_id";


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
    private final String KEY_UOM = "uom";
    private final String KEY_TO_AGENTCODE = "to_agent_code";
    private final String KEY_TO_UPLOAD_STATUS = "to_upload_status";


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
    private final String KEY_TDC_CUSTOMER_ROUTECODE = "tdc_customer_routecode";
    private final String KEY_TDC_CUSTOMER_SHOP_IMAGE_UPLOAD_STATUS = "tdc_customer_shop_image_upload_status";
    private final String KEY_TDC_CUSTOMER_CODE = "tdc_customer_code";
    private final String KEY_TDC_CUSTOMER_CHECK_UNIQUE_ID = "tdc_customer_check_unique_id";


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
    private final String KEY_TDC_SALES_ORDER_CUSTOMER_CODE = "tdc_sale_order_customer_code";
    private final String KEY_TDC_SALES_ORDER_CUSTOMER_NAME = "tdc_sale_order_customer_name";
    private final String KEY_TDC_SALES_ORDER_CUSTOMER_TYPE = "tdc_sale_order_customer_type";
    private final String KEY_TDC_SALES_ORDER_BILL_NUMBER = "tdc_sale_order_bill_number";

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
    private final String KEY_TDC_SOP_PRODUCT_CODE = "tdc_sop_product_code";
    private final String KEY_TDC_SOP_UOM = "tdc_sop_uom";

    // Column names for Stakeholder types
    private final String KEY_STAKEHOLDER_TYPE_ID = "stakeholder_type_id";
    private final String KEY_STAKEHOLDER_TYPE_NAME = "stakeholder_type_name";
    private final String KEY_STAKEHOLDER_TYPE = "stakeholder_type";

    // Column names for Tripsheets List  Table
    private final String KEY_TRIPSHEET_UNIQUE_ID = "tripsheet_unique_id";
    private final String KEY_TRIPSHEET_ID = "tripsheet_id";
    private final String KEY_TRIPSHEET_CODE = "tripsheet_code";
    private final String KEY_TRIPSHEET_MY_ID = "tripsheet_my_id";
    private final String KEY_TRIPSHEET_DATE = "tripsheet_date";
    private final String KEY_TRIPSHEET_STATUS = "tripsheet_status";
    private final String KEY_TRIPSHEET_OB_AMOUNT = "tripsheet_ob_amount";
    private final String KEY_TRIPSHEET_ORDERED_AMOUNT = "tripsheet_ordered_amount";
    private final String KEY_TRIPSHEET_RECEIVED_AMOUNT = "tripsheet_received_amount";
    private final String KEY_TRIPSHEET_DUE_AMOUNT = "tripsheet_due_amount";
    private final String KEY_TRIPSHEET_ROUTE_CODE = "tripsheet_route_code";
    private final String KEY_TRIPSHEET_SALESMEN_CODE = "tripsheet_salesmen_code";
    private final String KEY_TRIPSHEET_TRANSPORTER_NAME = "tripsheet_transporter_name";
    private final String KEY_TRIPSHEET_VEHICLE_NUMBER = "tripsheet_vehicle_number";
    private final String KEY_TRIPSHEET_VERIFY_STATUS = "tripsheet_verify_status"; // 0 is not verify and 1 is verify
    private final String KEY_TRIPSHEET_IS_TRIP_SHEET_CLOSED = "is_tripsheet_closed"; // o means not closed and 1 means closed
    private final String KEY_TRIPSHEET_APPROVED_BY = "tripsheet_approvedby";
    private final String KEY_TRIPSHEET_TRIP_ROUTE_ID = "tripsheet_trip_route_id";
    private final String KEY_TRIPSHEET_TRIP_ROUTE_CODE = "tripsheet_trip_route_code";

    // Column names for Tripsheets stocks List  Table
    private final String KEY_TRIPSHEET_STOCK_UNIQUE_ID = "tripsheet_stock_unique_id";
    private final String KEY_TRIPSHEET_STOCK_ID = "tripsheet_stock_id";
    private final String KEY_TRIPSHEET_STOCK_TRIPSHEET_ID = "tripsheet_stock_tripsheet_id";
    private final String KEY_TRIPSHEET_STOCK_PRODUCT_CODE = "tripsheet_stock_product_code";
    private final String KEY_TRIPSHEET_STOCK_PRODUCT_NAME = "tripsheet_stock_product_name";
    private final String KEY_TRIPSHEET_STOCK_PRODUCT_ID = "tripsheet_stock_product_id";
    private final String KEY_TRIPSHEET_STOCK_ORDER_QUANTITY = "tripsheet_stock_order_quantity";
    private final String KEY_TRIPSHEET_STOCK_DISPATCH_QUANTITY = "tripsheet_stock_dispatch_quantity";
    private final String KEY_TRIPSHEET_STOCK_DISPATCH_DATE = "tripsheet_stock_dispatch_date";
    private final String KEY_TRIPSHEET_STOCK_DISPATCH_BY = "tripsheet_stock_dispatch_by";
    private final String KEY_TRIPSHEET_STOCK_VERIFY_QUANTITY = "tripsheet_stock_verify_quantity";
    private final String KEY_TRIPSHEET_STOCK_VERIFY_DATE = "tripsheet_stock_verify_date";
    private final String KEY_TRIPSHEET_STOCK_VERIFY_BY = "tripsheet_stock_verify_by";
    private final String KEY_TRIPSHEET_STOCK_IN_STOCK_QUANTITY = "tripsheet_stock_in_stock_quantity";
    private final String KEY_TRIPSHEET_STOCK_EXTRA_QUANTITY = "tripsheet_stock_extra_quantity";
    private final String KEY_TRIPSHEET_STOCK_IS_DISPATCHED = "tripsheet_stock_is_dispatched";
    private final String KEY_TRIPSHEET_STOCK_IS_VERIFIED = "tripsheet_stock_is_verified";
    private final String KEY_TRIPSHEET_STOCK_DISPATCHED_UPLOAD_STATUS = "tripsheet_stock_dispatch_upload_status";
    private final String KEY_TRIPSHEET_STOCK_VERIFIED_UPLOAD_STATUS = "tripsheet_stock_verified_upload_status";
    // Added by Sekhar for close trip functionality
    private final String KEY_TRIPSHEET_STOCK_DELIVERY_QUANTITY = "tripsheet_stock_delivery_quantity";
    private final String KEY_TRIPSHEET_STOCK_RETURN_QUANTITY = "tripsheet_stock_return_quantity";
    private final String KEY_TRIPSHEET_STOCK_CB_QUANTITY = "tripsheet_stock_cb_quantity";
    private final String KEY_TRIPSHEET_STOCK_LEAK_QUANTITY = "tripsheet_stock_leak_quantity";
    private final String KEY_TRIPSHEET_STOCK_OTHER_QUANTITY = "tripsheet_stock_other_quantity";
    private final String KEY_TRIPSHEET_STOCK_ROUTE_RETURN_QUANTITY = "tripsheet_stock_route_return_quantity";
    private final String KEY_TRIPSHEET_STOCK_CLOSETRIP_UPLOAD_STATUS = "tripsheet_stock_closetrip_upload_status";

    // Column names for Tripsheets deliveries List  Table
    private final String KEY_TRIPSHEET_DELIVERY_NO = "tripsheet_delivery_no";
    private final String KEY_TRIPSHEET_DELIVERY_NUMBER = "tripsheet_delivery_number";
    private final String KEY_TRIPSHEET_DELIVERY_TRIP_ID = "tripsheet_delivery_trip_id";
    private final String KEY_TRIPSHEET_DELIVERY_SO_ID = "tripsheet_delivery_so_id";
    private final String KEY_TRIPSHEET_DELIVERY_SO_CODE = "tripsheet_delivery_so_code";
    private final String KEY_TRIPSHEET_DELIVERY_USER_ID = "tripsheet_delivery_user_id";
    private final String KEY_TRIPSHEET_DELIVERY_USER_CODES = "tripsheet_delivery_user_codes";
    private final String KEY_TRIPSHEET_DELIVERY_ROUTE_ID = "tripsheet_delivery_route_id";
    private final String KEY_TRIPSHEET_DELIVERY_ROUTE_CODES = "tripsheet_delivery_route_codes";
    private final String KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS = "tripsheet_delivery_product_ids";
    private final String KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES = "tripsheet_delivery_product_codes";
    private final String KEY_TRIPSHEET_DELIVERY_TAXPERCENT = "tripsheet_delivery_taxpercent";
    private final String KEY_TRIPSHEET_DELIVERY_UNITPRICE = "tripsheet_delivery_unitprice";
    private final String KEY_TRIPSHEET_DELIVERY_QUANTITY = "tripsheet_delivery_quantity";
    private final String KEY_TRIPSHEET_DELIVERY_AMOUNT = "tripsheet_delivery_amount";
    private final String KEY_TRIPSHEET_DELIVERY_TAXAMOUNT = "tripsheet_delivery_taxamount";
    private final String KEY_TRIPSHEET_DELIVERY_TAXTOTAL = "tripsheet_delivery_taxtotal";
    private final String KEY_TRIPSHEET_DELIVERY_SALEVALUE = "tripsheet_delivery_salevalue";
    private final String KEY_TRIPSHEET_DELIVERY_STATUS = "tripsheet_delivery_status";
    private final String KEY_TRIPSHEET_DELIVERY_DELETE = "tripsheet_delivery_delete";
    private final String KEY_TRIPSHEET_DELIVERY_CREATEDBY = "tripsheet_delivery_createdby";
    private final String KEY_TRIPSHEET_DELIVERY_CREATEDON = "tripsheet_delivery_createdon";
    private final String KEY_TRIPSHEET_DELIVERY_UPDATEDON = "tripsheet_delivery_updatedon";
    private final String KEY_TRIPSHEET_DELIVERY_UPDATEDBY = "tripsheet_delivery_updatedby";
    private final String KEY_TRIPSHEET_DELIVERY_UPLOAD_STATUS = "tripsheet_delivery_upload_status";
    private final String KEY_TRIPSHEET_DELIVERY_PRODUCT_TYPE = "tripsheet_delivery_product_type";
    private final String KEY_TRIPSHEET_DELIVERY_UOM = "tripsheet_delivery_uom";

    // Column names for Tripsheets returns List  Table
    private final String KEY_TRIPSHEET_RETURNS_RETURN_NO = "tripsheet_returns_return_no";
    private final String KEY_TRIPSHEET_RETURNS_RETURN_NUMBER = "tripsheet_returns_return_number";
    private final String KEY_TRIPSHEET_RETURNS_TRIP_ID = "tripsheet_returns_trip_id";
    private final String KEY_TRIPSHEET_RETURNS_SO_ID = "tripsheet_returns_so_id";
    private final String KEY_TRIPSHEET_RETURNS_SO_CODE = "tripsheet_returns_so_code";
    private final String KEY_TRIPSHEET_RETURNS_USER_ID = "tripsheet_returns_user_id";
    private final String KEY_TRIPSHEET_RETURNS_USER_CODES = "tripsheet_returns_user_codes";
    private final String KEY_TRIPSHEET_RETURNS_ROUTE_ID = "tripsheet_returns_route_id";
    private final String KEY_TRIPSHEET_RETURNS_ROUTE_CODES = "tripsheet_returns_route_codes";
    private final String KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS = "tripsheet_returns_product_ids";
    private final String KEY_TRIPSHEET_RETURNS_PRODUCT_CODES = "tripsheet_returns_product_codes";
    private final String KEY_TRIPSHEET_RETURNS_QUANTITY = "tripsheet_returns_quantity";
    private final String KEY_TRIPSHEET_RETURNS_TYPE = "tripsheet_returns_type"; // either L or R
    private final String KEY_TRIPSHEET_RETURNS_STATUS = "tripsheet_returns_status";
    private final String KEY_TRIPSHEET_RETURNS_DELETE = "tripsheet_returns_delete";
    private final String KEY_TRIPSHEET_RETURNS_CREATED_BY = "tripsheet_returns_created_by";
    private final String KEY_TRIPSHEET_RETURNS_CREATED_ON = "tripsheet_returns_created_on";
    private final String KEY_TRIPSHEET_RETURNS_UPDATED_ON = "tripsheet_returns_updated_on";
    private final String KEY_TRIPSHEET_RETURNS_UPDATED_BY = "tripsheet_returns_updated_by";
    private final String KEY_TRIPSHEET_RETURNS_UPLOAD_STATUS = "tripsheet_returns_upload_status";

    // Column names for Tripsheets payments List  Table
    private final String KEY_TRIPSHEET_PAYMENTS_PAYMENT_NO = "tripsheet_payments_payment_no";
    private final String KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER = "tripsheet_payments_payment_number";
    private final String KEY_TRIPSHEET_PAYMENTS_TRIP_ID = "tripsheet_payments_trip_id";
    private final String KEY_TRIPSHEET_PAYMENTS_USER_ID = "tripsheet_payments_user_id";
    private final String KEY_TRIPSHEET_PAYMENTS_USER_CODES = "tripsheet_payments_user_codes";
    private final String KEY_TRIPSHEET_PAYMENTS_ROUTE_ID = "tripsheet_payments_route_id";
    private final String KEY_TRIPSHEET_PAYMENTS_ROUTE_CODES = "tripsheet_payments_route_codes";
    private final String KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID = "tripsheet_payments_che_trans_id";
    private final String KEY_TRIPSHEET_PAYMENTS_AC_CA_NO = "tripsheet_payments_ac_ca_no";
    private final String KEY_TRIPSHEET_PAYMENTS_ACOUNT_NAME = "tripsheet_payments_account_name";
    private final String KEY_TRIPSHEET_PAYMENTS_BANK_NAME = "tripsheet_payments_bank_name";
    private final String KEY_TRIPSHEET_PAYMENTS_TRANS_DATE = "tripsheet_payments_trans_date";
    private final String KEY_TRIPSHEET_PAYMENTS_DATE = "tripsheet_payments_date";
    private final String KEY_TRIPSHEET_PAYMENTS_TRANS_CLEAR_DATE = "tripsheet_payments_trans_clear_date";
    private final String KEY_TRIPSHEET_PAYMENTS_RECEIVER_NAME = "tripsheet_payments_receiver_name";
    private final String KEY_TRIPSHEET_PAYMENTS_TRANS_STATUS = "tripsheet_payments_trans_status";
    private final String KEY_TRIPSHEET_PAYMENTS_TAX_TOTAL = "tripsheet_payments_tax_total";
    private final String KEY_TRIPSHEET_PAYMENTS_SALE_VALUE = "tripsheet_payments_sale_value";
    private final String KEY_TRIPSHEET_PAYMENTS_TYPE = "tripsheet_payments_type";
    private final String KEY_TRIPSHEET_PAYMENTS_STATUS = "tripsheet_payments_status";
    private final String KEY_TRIPSHEET_PAYMENTS_DELETE = "tripsheet_payments_delete";
    private final String KEY_TRIPSHEET_PAYMENTS_SO_ID = "tripsheet_payments_so_id";
    private final String KEY_TRIPSHEET_PAYMENTS_SO_CODE = "tripsheet_payments_so_code";
    private final String KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT = "tripsheet_payments_received_amount";
    private final String KEY_TRIPSHEET_PAYMENTS_UPLOAD_STATUS = "tripsheet_payments_upload_status";
    private final String KEY_TRIPSHEET_PAYMENTS_CHEQUE_PATH = "tripsheet_payments_cheque_path";
    private final String KEY_TRIPSHEET_PAYMENTS_CHEQUE_UPLOAD_STATUS = "tripsheet_payments_cheque_upload_status";
    private final String KEY_PAYMENT_FIRST_NAME = "tripsheet_payments_first_name";

    // Column names for Tripsheets so List  Table
    private final String KEY_TRIPSHEET_SO_UNIQUE_ID = "tripsheet_so_unique_id";
    private final String KEY_TRIPSHEET_SO_ID = "tripsheet_so_id";
    private final String KEY_TRIPSHEET_SO_TRIPID = "tripsheet_so_tripid";
    private final String KEY_TRIPSHEET_SO_AGENTCODE = "tripsheet_so_agentcode";
    private final String KEY_TRIPSHEET_SO_CODE = "tripsheet_so_code";
    private final String KEY_TRIPSHEET_SO_DATE = "tripsheet_so_date";
    private final String KEY_TRIPSHEET_SO_VALUE = "tripsheet_so_value";
    private final String KEY_TRIPSHEET_SO_OPAMOUNT = "tripsheet_so_opamount";
    private final String KEY_TRIPSHEET_SO_CBAMOUNT = "tripsheet_so_cbamount";
    private final String KEY_TRIPSHEET_SO_AGENTID = "tripsheet_so_agentid";
    private final String KEY_TRIPSHEET_SO_AGENTFIRSTNAME = "tripsheet_so_agentfirstname";
    private final String KEY_TRIPSHEET_SO_AGENTLASTNAME = "tripsheet_so_agentlastname";
    private final String KEY_TRIPSHEET_SO_PRODUCTCODE = "tripsheet_so_vehiclenumber";
    private final String KEY_TRIPSHEET_SO_PRODUCTORDER_QUANTITY = "tripsheet_so_productorder_quantity";
    private final String KEY_TRIPSHEET_SO_PRODUCT_VALUE = "tripsheet_so_product_value";
    private final String KEY_TRIPSHEET_SO_APPROVEDBY = "tripsheet_so_approvedby";
    private final String KEY_TRIPSHEET_SO_AGENTLATITUDE = "tripsheet_so_agentlatitude";
    private final String KEY_TRIPSHEET_SO_AGENTLONGITUDE = "tripsheet_so_agentlongitude";
    private final String KEY_TRIPSHEET_SO_PRODUCTS_COUNT = "tripsheet_so_products_count";
    private final String KEY_TRIPSHEET_SO_CANS_DUE = "tripsheet_so_cans_due";
    private final String KEY_TRIPSHEET_SO_CRATES_DUE = "tripsheet_so_crates_due";
    private final String KEY_TRIPSHEET_SO_ITEM_TYPE = "tripsheet_so_item_type";
    private final String KEY_TRIPSHEET_SO_UOM = "tripsheet_so_uom";
    private final String KEY_TRIPSHEET_SO_UNIT_PRICE = "tripsheet_so_unit_price";

    // Column names for Notifications List  Table
    private final String KEY_NOTIFICATIONS_ID = "notification_id";
    private final String KEY_NOTIFICATIONS_DATE = "notification_date";
    private final String KEY_NOTIFICATIONS_NAME = "notification_name";
    private final String KEY_NOTIFICATIONS_DESCRIPTION = "notification_description";

    // Column names for Agent Stock  Table
    private final String KEY_AGENT_STOCK_UNIQUE_ID = "agent_stock_unique_id";
    private final String KEY_AGENT_STOCK_AGENT_ID = "agent_stock_agent_id";
    private final String KEY_AGENT_STOCK_PRODUCT_NAME = "agent_stock_product_name";
    private final String KEY_AGENT_STOCK_PRODUCT_CODE = "agent_stock_product_code";
    private final String KEY_AGENT_STOCK_PRODUCT_ID = "agent_stock_product_id";
    private final String KEY_AGENT_STOCK_PRODUCT_UOM = "agent_stock_product_uom";
    private final String KEY_AGENT_STOCK_PRODUCT_STOCK_QUNATITY = "agent_stock_product_stock_qunatity";
    private final String KEY_AGENT_STOCK_PRODUCT_DELIVERY_QUNATITY = "agent_stock_product_delivery_qunatity";
    private final String KEY_AGENT_STOCK_PRODUCT_CNQUANTITY = "agent_stock_product_cb_qunatity";

    // Column names for Agent Stock deliveries List  Table
    private final String KEY_AGENT_STOCK_DELIVERY_NO = "agent_stock_delivery_no";
    private final String KEY_AGENT_STOCK_DELIVERY_NUMBER = "agent_stock_delivery_number";
    private final String KEY_AGENT_STOCK_DELIVERY_TRIP_ID = "agent_stock_delivery_trip_id";
    private final String KEY_AGENT_STOCK_DELIVERY_SO_ID = "agent_stock_delivery_so_id";
    private final String KEY_AGENT_STOCK_DELIVERY_SO_CODE = "agent_stock_delivery_so_code";
    private final String KEY_AGENT_STOCK_DELIVERY_USER_ID = "agent_stock_delivery_user_id"; // Agent id
    private final String KEY_AGENT_STOCK_DELIVERY_USER_CODES = "agent_stock_delivery_user_codes"; // Agent code
    private final String KEY_AGENT_STOCK_DELIVERY_ROUTE_ID = "agent_stock_delivery_route_id";
    private final String KEY_AGENT_STOCK_DELIVERY_ROUTE_CODES = "agent_stock_delivery_route_codes";
    private final String KEY_AGENT_STOCK_DELIVERY_PRODUCT_IDS = "agent_stock_delivery_product_ids";
    private final String KEY_AGENT_STOCK_DELIVERY_PRODUCT_CODES = "agent_stock_delivery_product_codes";
    private final String KEY_AGENT_STOCK_DELIVERY_TAXPERCENT = "agent_stock_delivery_taxpercent";
    private final String KEY_AGENT_STOCK_DELIVERY_UNITPRICE = "agent_stock_delivery_unitprice";
    private final String KEY_AGENT_STOCK_DELIVERY_QUANTITY = "agent_stock_delivery_quantity";
    private final String KEY_AGENT_STOCK_DELIVERY_AMOUNT = "agent_stock_delivery_amount";
    private final String KEY_AGENT_STOCK_DELIVERY_TAXAMOUNT = "agent_stock_delivery_taxamount";
    private final String KEY_AGENT_STOCK_DELIVERY_TAXTOTAL = "agent_stock_delivery_taxtotal";
    private final String KEY_AGENT_STOCK_DELIVERY_SALEVALUE = "agent_stock_delivery_salevalue";
    private final String KEY_AGENT_STOCK_DELIVERY_STATUS = "agent_stock_delivery_status";
    private final String KEY_AGENT_STOCK_DELIVERY_DELETE = "agent_stock_delivery_delete";
    private final String KEY_AGENT_STOCK_DELIVERY_CREATEDBY = "agent_stock_delivery_createdby";
    private final String KEY_AGENT_STOCK_DELIVERY_CREATEDON = "agent_stock_delivery_createdon";
    private final String KEY_AGENT_STOCK_DELIVERY_UPDATEDON = "agent_stock_delivery_updatedon";
    private final String KEY_AGENT_STOCK_DELIVERY_UPDATEDBY = "agent_stock_delivery_updatedby";
    private final String KEY_AGENT_STOCK_DELIVERY_UPLOAD_STATUS = "agent_stock_delivery_upload_status";

    //Column names for Produts sort activity Table
    public final String KEY_PRODUCT_SORT_ID = "product_sort_id";
    public final String KEY_PRODUCT_SORT_CODE = "product_sort_code";
    public final String KEY_PRODUCT_SORT_TITLE = "product_sort_title";
    public final String KEY_PRODUCT_SORT_DESCRIPTION = "product_sort_description";
    public final String KEY_PRODUCT_SORT_IMAGE_URL = "product_sort_image_url";
    public final String KEY_PRODUCT_SORT_RETURNABLE = "product_sort_returnable";
    public final String KEY_PRODUCT_SORT_MOQ = "product_sort_moq";
    public final String KEY_PRODUCT_SORT_UOM = "product_sort_uom";
    public final String KEY_PRODUCT_SORT_AGENT_PRICE = "product_sort_agent_price";
    public final String KEY_PRODUCT_SORT_CONSUMER_PRICE = "product_sort_consumer_price";
    public final String KEY_PRODUCT_SORT_RETAILER_PRICE = "product_sort_retailer_price";
    public final String KEY_PRODUCT_SORT_GST_PRICE = "product_sort_gst_price";
    public final String KEY_PRODUCT_SORT_VAT_PRICE = "product_sort_vat_price";
    public final String KEY_PRODUCT_SORT_CONTROL_CODE = "control_sort_code";

    // Column names for Device/User latlang  Table
    private final String KEY_DEVICE_ID_LATLANG = "latlang_device_id";
    private final String KEY_DEVICE_USERID_LATLANG = "latlang_device_userid";
    private final String KEY_DEVICE_DATE_LATLANG = "latlang_device_date";
    private final String KEY_DEVICE_TIME_LATLANG = "latlang_device_time";
    private final String KEY_DEVICE_LAT_LATLANG = "latlang_device_latitude";
    private final String KEY_DEVICE_LANG_LATLANG = "latlang_device_longitude";
    private final String KEY_DEVICE_SPEED_LATLANG = "latlang_device_speed";
    private final String KEY_DEVICE_UPLOAD_FLAG_LATLANG = "latlang_device_uploaded_flag";

    // Dashboard Pending Indent Table Create Statements
    private final String CREATE_TABLE_DASHBOARD_PENDINGINDENT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DASHBOARD_PENDINGINDENTLIST + "(" + KEY_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " VARCHAR,"
            + KEY_CREATED_DATE + " VARCHAR,"
            + KEY_TIME + " VARCHAR,"
            + KEY_CATEGORY + " VARCHAR,"
            + KEY_LITRES + " VARCHAR,"
            + KEY_APPROVED_COUNT + " VARCHAR,"
            + KEY_PENDING_COUNT + " VARCHAR)";

    // Dashboard Deliveries Table Create Statements
    private final String CREATE_TABLE_DASHBOARD_DELIVERIES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DASHBOARD_DELIVERYLIST + "(" + KEY_DASHBOARD_DELIVERY_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DASHBOARD_DELIVERY_DATE + " VARCHAR,"
            + KEY_DASHBOARD_DELIVERY_TIME + " VARCHAR,"
            + KEY_DASHBOARD_DELIVERY_CREATED_DATE + " VARCHAR,"
            + KEY_DASHBOARD_DELIVERY_CATEGORY + " VARCHAR,"
            + KEY_DASHBOARD_DELIVERY_ORDERVALUE + " VARCHAR,"
            + KEY_DASHBOARD_DELIVERY_DELIVERYVALUE + " VARCHAR,"
            + KEY_DASHBOARD_DELIVERY_PERCENTAGEVALUE + " VARCHAR)";


    // Dashboard Payments Table Create Statements
    private final String CREATE_TABLE_DASHBOARD_PAYMENTS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DASHBOARD_PAYMENTLIST + "(" + KEY_DASHBOARD_PAYMENTS_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DASHBOARD_PAYMENTS_DATE + " VARCHAR,"
            + KEY_DASHBOARD_PAYMENT_TIME + " VARCHAR,"
            + KEY_DASHBOARD_PAYMENT_CREATED_DATE + " VARCHAR,"
            + KEY_DASHBOARD_PAYMENTS_DELIVERYVALUE + " VARCHAR,"
            + KEY_DASHBOARD_PAYMENTS_RECEIVEDVALUE + " VARCHAR,"

            + KEY_DASHBOARD_PAYMENTS_DUEVALUE + " VARCHAR)";


    // Dashboard Next Indent MOREINFO Table Create Statements
    private final String CREATE_TABLE_NEXTINDENT_MOREINFO = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NEXTINDENT_MOREINFO + "(" + KEY_NEXTINDENT_MOREINFO_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NEXTINDENT_MOREINFO_MILKDATE + " VARCHAR,"
            + KEY_NEXTINDENT_MOREINFO_MILKVOL + " VARCHAR,"
            + KEY_NEXTINDENT_MOREINFO_CURDDATE + " VARCHAR,"
            + KEY_NEXTINDENT_MOREINFO_CURDVOL + " VARCHAR,"
            + KEY_NEXTINDENT_MOREINFO_OTHRESDATE + " VARCHAR,"
            + KEY_NEXTINDENT_MOREINFO_SELDATE + " VARCHAR,"
            + KEY_NEXTINDENT_MOREINFO_BACKDATE+ " VARCHAR,"
            + KEY_NEXTINDENT_MOREINFO_OTHERSVOL + " VARCHAR)";


    // Agents Table Create Statements
    private final String CREATE_TABLE_AGENTS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_AGENTS + "(" + KEY_AGENT_UNIQUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_AGENT_ID + " VARCHAR,"
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
            + KEY_AGENT_ACCESSDEVICE + " VARCHAR," + KEY_AGENT_BACKUP + " VARCHAR," + KEY_AGENT_UPLOAD_STATUS + " VARCHAR,"
            + KEY_AGENT_LOGIN_USER_ID + " VARCHAR)";

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
            + KEY_PRODUCT_DESCRIPTION + " VARCHAR," + KEY_PRODUCT_IMAGE_URL + " VARCHAR," + KEY_PRODUCT_RETURNABLE + " VARCHAR,"
            + KEY_PRODUCT_MOQ + " VARCHAR," + KEY_PRODUCT_AGENT_PRICE + " VARCHAR," + KEY_PRODUCT_CONSUMER_PRICE + " VARCHAR,"
            + KEY_PRODUCT_UOM + " VARCHAR,"
            + KEY_PRODUCT_RETAILER_PRICE + " VARCHAR," + KEY_PRODUCT_GST_PRICE + " VARCHAR,"
            + KEY_PRODUCT_VAT_PRICE + " VARCHAR," + KEY_PRODUCT_CONTROL_CODE + " VARCHAR)";

    //TO Products Table Create Statements
    private final String CREATE_PRODUCTS_TABLE_TO = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TO_PRODUCTS + "(" + KEY_TO_PRODUCT_ID + " VARCHAR," + KEY_TO_PRODUCT_CODE + " VARCHAR,"
            + KEY_TO_PRODUCT_NAME + " VARCHAR," + KEY_TO_PRODUCT_ROUTE_ID + " VARCHAR,"
            + KEY_TO_FROM_DATE + " VARCHAR," + KEY_TO_TO_DATE + " VARCHAR," + KEY_TO_ORDER_TYPE + " VARCHAR,"
            + KEY_TO_QUANTITY + " VARCHAR," + KEY_TO_STATUS + " VARCHAR,"
            + KEY_TO_ENQID + " VARCHAR," + KEY_TO_AGENTID + " VARCHAR," + KEY_TAKEORDER_DATE + " VARCHAR," + KEY_PRICE + " VARCHAR," + KEY_VAT + " VARCHAR," + KEY_GST + " VARCHAR," + KEY_UOM + " VARCHAR,"
            + KEY_TO_UPLOAD_STATUS + " INTEGER DEFAULT 0, " + KEY_TO_AGENTCODE + " VARCHAR)";


    // User privilege actions Table Create Statements
    private final String CREATE_USER_PRIVILEGE_ACTIONS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PREVILEGE_ACTIONS + "(" + KEY_USER_PRIVILEGE_ACTION_ID + " VARCHAR," + KEY_USER_PRIVILEGE_ID + " VARCHAR,"
            + KEY_USER_PRIVILEGE_ACTION_NAME + " VARCHAR," + KEY_USER_PRIVILEGE_ACTION_STATUS + " VARCHAR)";

    // TDC Customers Table Create Statement
    private final String CREATE_TDC_CUSTOMERS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TDC_CUSTOMERS + "(" + KEY_TDC_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TDC_CUSTOMER_USER_ID + " VARCHAR, " + KEY_TDC_CUSTOMER_CHECK_UNIQUE_ID + " VARCHAR, " + KEY_TDC_CUSTOMER_ROUTECODE + " VARCHAR, " + KEY_TDC_CUSTOMER_TYPE + " INTEGER, "
            + KEY_TDC_CUSTOMER_NAME + " VARCHAR, " + KEY_TDC_CUSTOMER_MOBILE_NO + " VARCHAR, " + KEY_TDC_CUSTOMER_BUSINESS_NAME + " VARCHAR, "
            + KEY_TDC_CUSTOMER_ADDRESS + " TEXT, " + KEY_TDC_CUSTOMER_LATITUDE + " TEXT, " + KEY_TDC_CUSTOMER_LONGITUDE + " TEXT, " + KEY_TDC_CUSTOMER_SHOP_IMAGE + " VARCHAR, "
            + KEY_TDC_CUSTOMER_IS_ACTIVE + " INTEGER DEFAULT 1, " + KEY_TDC_CUSTOMER_SHOP_IMAGE_UPLOAD_STATUS + " INTEGER DEFAULT 0, " + KEY_TDC_CUSTOMER_CODE + " VARCHAR, "
            + KEY_TDC_CUSTOMER_UPLOAD_STATUS + " INTEGER DEFAULT 0)";

    // SpecialPrice Table Create Statement
    private final String CREATE_TABLE_SPECIALPRICE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SPECIALPRICE + "(" + KEY_USER_SPECIALID + " VARCHAR, " + KEY_PRODUCT_SPECIALID + " INTEGER, "
            + KEY_SPECIALPRICE + " VARCHAR)";
    // TDC Sales Orders Table Create Statement
    private final String CREATE_TDC_SALES_ORDERS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TDC_SALES_ORDERS + "(" + KEY_TDC_SALES_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TDC_SALES_ORDER_NO_OF_ITEMS + " INTEGER, "
            + KEY_TDC_SALES_ORDER_TOTAL_AMOUNT + " VARCHAR, " + KEY_TDC_SALES_ORDER_TOTAL_TAX_AMOUNT + " VARCHAR, " + KEY_TDC_SALES_ORDER_SUB_TOTAL + " VARCHAR, "
            + KEY_TDC_SALES_ORDER_CUSTOMER_ID + " INTEGER, " + KEY_TDC_SALES_ORDER_CUSTOMER_USER_ID + " VARCHAR, " + KEY_TDC_SALES_ORDER_DATE + " TEXT, "
            + KEY_TDC_SALES_ORDER_CREATED_ON + " TEXT, " + KEY_TDC_SALES_ORDER_CREATED_BY + " VARCHAR, " + KEY_TDC_SALES_ORDER_UPLOAD_STATUS + " INTEGER DEFAULT 0, "
            + KEY_TDC_SALES_ORDER_CUSTOMER_CODE + " VARCHAR, " + KEY_TDC_SALES_ORDER_CUSTOMER_NAME + " VARCHAR, " + KEY_TDC_SALES_ORDER_BILL_NUMBER + " VARCHAR, " + KEY_TDC_SALES_ORDER_CUSTOMER_TYPE + " VARCHAR)";

    // TDC Sales Order Products Table Create Statement
    private final String CREATE_TDC_SALES_ORDER_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TDC_SALES_ORDER_PRODUCTS + "(" + KEY_TDC_SOP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TDC_SOP_ORDER_ID + " INTEGER, "
            + KEY_TDC_SOP_PRODUCT_ID + " VARCHAR, " + KEY_TDC_SOP_PRODUCT_NAME + " VARCHAR, " + KEY_TDC_SOP_PRODUCT_MRP + " VARCHAR, "
            + KEY_TDC_SOP_PRODUCT_QUANTITY + " VARCHAR, " + KEY_TDC_SOP_PRODUCT_AMOUNT + " VARCHAR, " + KEY_TDC_SOP_PRODUCT_TAX + " TEXT, "
            + KEY_TDC_SOP_PRODUCT_TAX_AMOUNT + " TEXT, " + KEY_TDC_SOP_PRODUCT_CODE + " TEXT, " +
            KEY_TDC_SOP_UOM + " TEXT, " + KEY_TDC_SOP_UPLOAD_STATUS + " INTEGER DEFAULT 0)";

    // Stakeholder types Table Create Statements
    private final String CREATE_TABLE_STAKEHOLDER_TYPES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_STAKEHOLDER_TYPES + "(" + KEY_STAKEHOLDER_TYPE_ID + " VARCHAR," + KEY_STAKEHOLDER_TYPE_NAME + " VARCHAR,"
            + KEY_STAKEHOLDER_TYPE + " VARCHAR)";

    // Tripsheets Table Create Statements
    private final String CREATE_TRIPSHEETS_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRIPSHEETS_LIST + "(" + KEY_TRIPSHEET_UNIQUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TRIPSHEET_ID + " VARCHAR,"
            + KEY_TRIPSHEET_CODE + " VARCHAR,"
            + KEY_TRIPSHEET_MY_ID + " VARCHAR,"
            + KEY_TRIPSHEET_APPROVED_BY + " VARCHAR,"
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
            + KEY_TRIPSHEET_VERIFY_STATUS + " VARCHAR,"
            + KEY_TRIPSHEET_TRIP_ROUTE_ID + " VARCHAR,"
            + KEY_TRIPSHEET_TRIP_ROUTE_CODE + " VARCHAR,"
            + KEY_TRIPSHEET_IS_TRIP_SHEET_CLOSED + " INTEGER DEFAULT 0)";
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
            + KEY_TRIPSHEET_STOCK_VERIFIED_UPLOAD_STATUS + " INTEGER DEFAULT 0,"
            + KEY_TRIPSHEET_STOCK_DELIVERY_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_RETURN_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_CB_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_LEAK_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_OTHER_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_STOCK_CLOSETRIP_UPLOAD_STATUS + " INTEGER DEFAULT 0,"
            + KEY_TRIPSHEET_STOCK_ROUTE_RETURN_QUANTITY + " VARCHAR)";

    // Tripsheets Deliveries list Table Create Statements
    private final String CREATE_TRIPSHEETS_DELIVERIES_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRIPSHEETS_DELIVERIES_LIST + "(" + KEY_TRIPSHEET_DELIVERY_NO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TRIPSHEET_DELIVERY_NUMBER + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_SO_ID + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_SO_CODE + " VARCHAR,"
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
            + KEY_TRIPSHEET_DELIVERY_UPDATEDBY + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_PRODUCT_TYPE + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_UOM + " VARCHAR,"
            + KEY_TRIPSHEET_DELIVERY_UPLOAD_STATUS + " INTEGER DEFAULT 0)";

    // Tripsheets Returns list Table Create Statements
    private final String CREATE_TRIPSHEETS_RETURNS_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRIPSHEETS_RETURNS_LIST + "(" + KEY_TRIPSHEET_RETURNS_RETURN_NO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TRIPSHEET_RETURNS_RETURN_NUMBER + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_TRIP_ID + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_SO_ID + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_SO_CODE + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_USER_ID + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_USER_CODES + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_ROUTE_ID + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_ROUTE_CODES + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_PRODUCT_CODES + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_QUANTITY + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_TYPE + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_STATUS + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_DELETE + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_CREATED_BY + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_CREATED_ON + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_UPDATED_ON + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_UPDATED_BY + " VARCHAR,"
            + KEY_TRIPSHEET_RETURNS_UPLOAD_STATUS + " INTEGER DEFAULT 0)";

    // Tripsheets Payments list Table Create Statements
    private final String CREATE_TRIPSHEETS_PAYMENTS_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRIPSHEETS_PAYMENTS_LIST + "(" + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER + " VARCHAR,"
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
            + KEY_TRIPSHEET_PAYMENTS_DATE + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_TRANS_CLEAR_DATE + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_RECEIVER_NAME + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_TRANS_STATUS + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_TAX_TOTAL + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_SALE_VALUE + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_TYPE + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_STATUS + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_DELETE + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_SO_ID + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_SO_CODE + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_CHEQUE_PATH + " VARCHAR,"
            + KEY_PAYMENT_FIRST_NAME + " VARCHAR,"
            + KEY_TRIPSHEET_PAYMENTS_CHEQUE_UPLOAD_STATUS + " INTEGER DEFAULT 0,"
            + KEY_TRIPSHEET_PAYMENTS_UPLOAD_STATUS + " INTEGER DEFAULT 0)";

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
            + KEY_TRIPSHEET_SO_AGENTLONGITUDE + " VARCHAR,"
            + KEY_TRIPSHEET_SO_PRODUCTS_COUNT + " VARCHAR,"
            + KEY_TRIPSHEET_SO_CRATES_DUE + " VARCHAR,"
            + KEY_TRIPSHEET_SO_ITEM_TYPE + " VARCHAR,"
            + KEY_TRIPSHEET_SO_UOM + " VARCHAR,"
            + KEY_TRIPSHEET_SO_UNIT_PRICE + " VARCHAR,"
            + KEY_TRIPSHEET_SO_CANS_DUE + " INTEGER)";


    // Notifications Table Create Statements
    private final String CREATE_NOTIFICATIONS_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NOTIFICATION_LIST + "(" + KEY_NOTIFICATIONS_ID + " VARCHAR," + KEY_NOTIFICATIONS_DATE + " VARCHAR,"
            + KEY_NOTIFICATIONS_NAME + " VARCHAR,"
            + KEY_NOTIFICATIONS_DESCRIPTION + " VARCHAR)";

    // Agent Stock Table Create Statements
    private final String CREATE_AGENT_STOCK_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_AGENTS_STOCK_LIST + "(" + KEY_AGENT_STOCK_UNIQUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_AGENT_STOCK_AGENT_ID + " VARCHAR,"
            + KEY_AGENT_STOCK_PRODUCT_NAME + " VARCHAR,"
            + KEY_AGENT_STOCK_PRODUCT_CODE + " VARCHAR,"
            + KEY_AGENT_STOCK_PRODUCT_ID + " VARCHAR,"
            + KEY_AGENT_STOCK_PRODUCT_UOM + " VARCHAR,"
            + KEY_AGENT_STOCK_PRODUCT_STOCK_QUNATITY + " VARCHAR,"
            + KEY_AGENT_STOCK_PRODUCT_DELIVERY_QUNATITY + " VARCHAR,"
            + KEY_AGENT_STOCK_PRODUCT_CNQUANTITY + " VARCHAR)";

    // Agent stock Deliveries list Table Create Statements
    private final String CREATE_AGENT_STOCK_DELIVERIES_LIST_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_AGENT_STOCK_DELIVERIES_LIST + "(" + KEY_AGENT_STOCK_DELIVERY_NO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_AGENT_STOCK_DELIVERY_NUMBER + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_TRIP_ID + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_SO_ID + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_SO_CODE + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_USER_ID + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_USER_CODES + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_ROUTE_ID + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_ROUTE_CODES + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_PRODUCT_IDS + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_PRODUCT_CODES + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_TAXPERCENT + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_UNITPRICE + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_QUANTITY + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_AMOUNT + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_TAXAMOUNT + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_TAXTOTAL + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_SALEVALUE + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_STATUS + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_DELETE + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_CREATEDBY + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_CREATEDON + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_UPDATEDON + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_UPDATEDBY + " VARCHAR,"
            + KEY_AGENT_STOCK_DELIVERY_UPLOAD_STATUS + " INTEGER DEFAULT 0)";

    //Products Sort Table Create Statements
    private final String CREATE_PRODUCTS_SORT_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PRODUCTS_SORT_LIST + "(" + KEY_PRODUCT_SORT_ID + " VARCHAR,"
            + KEY_PRODUCT_SORT_CODE + " VARCHAR,"
            + KEY_PRODUCT_SORT_TITLE + " VARCHAR,"
            + KEY_PRODUCT_SORT_DESCRIPTION + " VARCHAR,"
            + KEY_PRODUCT_SORT_IMAGE_URL + " VARCHAR,"
            + KEY_PRODUCT_SORT_RETURNABLE + " VARCHAR,"
            + KEY_PRODUCT_SORT_MOQ + " VARCHAR,"
            + KEY_PRODUCT_SORT_AGENT_PRICE + " VARCHAR,"
            + KEY_PRODUCT_SORT_CONSUMER_PRICE + " VARCHAR,"
            + KEY_PRODUCT_SORT_UOM + " VARCHAR,"
            + KEY_PRODUCT_SORT_RETAILER_PRICE + " VARCHAR,"
            + KEY_PRODUCT_SORT_GST_PRICE + " VARCHAR,"
            + KEY_PRODUCT_SORT_VAT_PRICE + " VARCHAR,"
            + KEY_PRODUCT_SORT_CONTROL_CODE + " VARCHAR)";

    // Device/User LatLang Table Create Statements
    private final String CREATE_TABLE_DEVICE_LATLANG = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DEVICE_LATLANG_TABLE + "(" + KEY_DEVICE_ID_LATLANG + " VARCHAR,"
            + KEY_DEVICE_USERID_LATLANG + " VARCHAR,"
            + KEY_DEVICE_DATE_LATLANG + " VARCHAR,"
            + KEY_DEVICE_TIME_LATLANG + " VARCHAR,"
            + KEY_DEVICE_LAT_LATLANG + " VARCHAR,"
            + KEY_DEVICE_LANG_LATLANG + " VARCHAR,"
            + KEY_DEVICE_SPEED_LATLANG + " VARCHAR,"
            + KEY_DEVICE_UPLOAD_FLAG_LATLANG + " VARCHAR)";


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
            db.execSQL(CREATE_AGENT_STOCK_LIST_TABLE);
            db.execSQL(CREATE_AGENT_STOCK_DELIVERIES_LIST_TABLE);
            db.execSQL(CREATE_PRODUCTS_SORT_TABLE);
            db.execSQL(CREATE_TABLE_DEVICE_LATLANG);
            db.execSQL(CREATE_TABLE_DASHBOARD_PENDINGINDENT);
            db.execSQL(CREATE_TABLE_DASHBOARD_DELIVERIES);
            db.execSQL(CREATE_TABLE_DASHBOARD_PAYMENTS);
            db.execSQL(CREATE_TABLE_NEXTINDENT_MOREINFO);
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
     * Method to get count of the agents table
     */
    public int getDeliveriesTableCount() {
        int noOfEvents = 0;
        try {
            String countQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST;
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
    public long insertAgentDetails(ArrayList<AgentsBean> mAgentsBeansList, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;
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
                values.put(KEY_AGENT_LOGIN_USER_ID, userId);
                values.put(KEY_AGENT_UPLOAD_STATUS, mAgentsBeansList.get(i).getmUploadStatus());

                if (mAgentsBeansList.get(i).getmAgentId().equals("") && mAgentsBeansList.get(i).getmIsAgentUpdate().equals("false")) {
                    // Always insert
                    id = db.insert(TABLE_AGENTS, null, values);
                } else if (mAgentsBeansList.get(i).getmAgentId().equals("") && mAgentsBeansList.get(i).getmIsAgentUpdate().equals("true")) {
                    // Update condition 1
                    id = db.update(TABLE_AGENTS, values, KEY_AGENT_STOCK_UNIQUE_ID + " = ?",
                            new String[]{String.valueOf(mAgentsBeansList.get(i).getmAgentUniqueId())});

                } else if (!mAgentsBeansList.get(i).getmAgentId().equals("") && mAgentsBeansList.get(i).getmIsAgentUpdate().equals("true")) {
                    // Insert condition 1
                    int checkConditionVal = checkAgentIsExistsOrNot(mAgentsBeansList.get(i).getmAgentId(), userId);
                    if (checkConditionVal == 0) {
                        id = db.insert(TABLE_AGENTS, null, values);
                    } else {
                        id = db.update(TABLE_AGENTS, values, KEY_AGENT_ID + " = ?",
                                new String[]{String.valueOf(mAgentsBeansList.get(i).getmAgentId())});
                    }
                } else {
                    // Update condition 2
                    int checkConditionVal = checkAgentIsExistsOrNot(mAgentsBeansList.get(i).getmAgentId(), userId);
                    id = db.update(TABLE_AGENTS, values, KEY_AGENT_ID + " = ?",
                            new String[]{String.valueOf(mAgentsBeansList.get(i).getmAgentId())});
                }
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return id;
    }

    /**
     * This method is used to check the agent exists or not.
     *
     * @param agentId,loggedInUserId
     * @return
     */
    public int checkAgentIsExistsOrNot(String agentId, String loggedInUserId) {
        int maxID = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_AGENTS + " WHERE " + KEY_AGENT_ID + "='" + agentId + "'"
                + " AND " + KEY_AGENT_LOGIN_USER_ID + "='" + loggedInUserId + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                maxID = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        return maxID;
    }


    public long updateAgentDetails(ArrayList<AgentsBean> mAgentsBeansList, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;
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
                values.put(KEY_AGENT_LOGIN_USER_ID, userId);
                values.put(KEY_AGENT_UPLOAD_STATUS, mAgentsBeansList.get(i).getmUploadStatus());

                System.out.println("AGENT UPDATED+++++");
                db.update(TABLE_AGENTS, values, KEY_AGENT_ID + " = ?",
                        new String[]{String.valueOf(userId)});

                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return id;
    }

    /**
     * Method to fetch all records from agents table
     */
    public ArrayList<AgentsBean> fetchAllRecordsFromAgentsTable(String userId) {
        ArrayList<AgentsBean> allDeviceTrackRecords = new ArrayList<AgentsBean>();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_AGENTS + " WHERE " + KEY_AGENT_LOGIN_USER_ID + " = " + "'" + userId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    AgentsBean agentsBean = new AgentsBean();

                    agentsBean.setmAgentUniqueId((c.getString(c.getColumnIndex(KEY_AGENT_UNIQUE_ID))));
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
            values.put(KEY_EMAIL, email);
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
                    userData.put(KEY_USER_CODE, (c.getString(c.getColumnIndex(KEY_USER_CODE))));

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

    public String getRouteNameByRouteId(String routeId) {
        String rName = "";
        try {
            String selectQuery = "SELECT  " + KEY_ROUTE_NAME + " FROM " + TABLE_ROUTESDETAILS + " WHERE " + KEY_ROUTE_ID + " = " + "'" + routeId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    rName = c.getString(c.getColumnIndex(KEY_ROUTE_NAME));
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rName;
    }

    public String getRouteNameByRouteCode(String routeCode) {
        String routeDetailsById = "";
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_ROUTESDETAILS + " WHERE " + KEY_ROUTE_CODE + " = " + "'" + routeCode + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    routeDetailsById = ((c.getString(c.getColumnIndex(KEY_ROUTE_NAME))));

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


    public int getNotificationsTableCount() {
        int noOfEvents = 0;
        try {
            String countQuery = "SELECT * FROM " + TABLE_NOTIFICATION_LIST;
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
            long effectedRows = 0;
            for (int i = 0; i < mProductsBeansList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_PRODUCT_ID, mProductsBeansList.get(i).getProductId());
                values.put(KEY_PRODUCT_CODE, mProductsBeansList.get(i).getProductCode());
                values.put(KEY_PRODUCT_TITLE, mProductsBeansList.get(i).getProductTitle());
                values.put(KEY_PRODUCT_DESCRIPTION, mProductsBeansList.get(i).getProductDescription());
                values.put(KEY_PRODUCT_IMAGE_URL, mProductsBeansList.get(i).getProductImageUrl());
                values.put(KEY_PRODUCT_RETURNABLE, mProductsBeansList.get(i).getProductReturnable());
                values.put(KEY_PRODUCT_MOQ, mProductsBeansList.get(i).getProductMOQ());
                values.put(KEY_PRODUCT_UOM, mProductsBeansList.get(i).getProductUOM());
                values.put(KEY_PRODUCT_AGENT_PRICE, mProductsBeansList.get(i).getProductAgentPrice());
                values.put(KEY_PRODUCT_CONSUMER_PRICE, mProductsBeansList.get(i).getProductConsumerPrice());
                values.put(KEY_PRODUCT_RETAILER_PRICE, mProductsBeansList.get(i).getProductRetailerPrice());
                values.put(KEY_PRODUCT_GST_PRICE, mProductsBeansList.get(i).getProductgst());
                values.put(KEY_PRODUCT_VAT_PRICE, mProductsBeansList.get(i).getProductvat());
                values.put(KEY_PRODUCT_CONTROL_CODE, mProductsBeansList.get(i).getControlCode());

                int productExists = checkProductIsExistsOrNot(mProductsBeansList.get(i).getProductId());
                if (productExists == 0) {
                    // Insert row
                    effectedRows = db.insert(TABLE_PRODUCTS, null, values);
                } else {
                    // Update row
                    effectedRows = db.update(TABLE_PRODUCTS, values, KEY_PRODUCT_ID + " = ?",
                            new String[]{String.valueOf(mProductsBeansList.get(i).getProductId())});
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
                    productsBean.setProductUOM((c.getString(c.getColumnIndex(KEY_PRODUCT_UOM))));
                    productsBean.setProductAgentPrice((c.getString(c.getColumnIndex(KEY_PRODUCT_AGENT_PRICE))));
                    productsBean.setProductConsumerPrice((c.getString(c.getColumnIndex(KEY_PRODUCT_CONSUMER_PRICE))));
                    productsBean.setProductRetailerPrice((c.getString(c.getColumnIndex(KEY_PRODUCT_RETAILER_PRICE))));
                    productsBean.setProductgst((c.getString(c.getColumnIndex(KEY_PRODUCT_GST_PRICE))));
                    productsBean.setProductvat((c.getString(c.getColumnIndex(KEY_PRODUCT_VAT_PRICE))));
                    productsBean.setControlCode((c.getString(c.getColumnIndex(KEY_PRODUCT_CONTROL_CODE))));

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
                    productsBean.setProductUOM((c.getString(c.getColumnIndex(KEY_PRODUCT_UOM))));
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
     * Method to get product unit by productcode
     */
    public String getProductUnitByProductCode(String productCode) {
        String productUnit = "";
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_CODE + " = " + "'" + productCode + "'";
            //   String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    productUnit = c.getString(c.getColumnIndex(KEY_PRODUCT_UOM));
                } while (c.moveToNext());
                //c.close();
                //db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productUnit;
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
                values.put(KEY_UOM, mProductsBeansList.get(i).getUom());
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
                    toBean.setUom((c.getString(c.getColumnIndex(KEY_UOM))));
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
                values.put(KEY_UOM, takeOrderBeanArrayList.get(b).getUom());
                values.put(KEY_TO_UPLOAD_STATUS, takeOrderBeanArrayList.get(b).getMuploadStatus());


                int ccc = checkProductExistsOrNot(takeOrderBeanArrayList.get(b).getmProductId(), takeOrderBeanArrayList.get(b).getmAgentId());
                System.out.println("Product Exists:::: " + ccc);
                if (ccc == 0) {
                    effectedRows = db.insert(TABLE_TO_PRODUCTS, null, values);
                    System.out.println("IFFFFFF::: " + effectedRows);
                } else {
                    effectedRows = db.update(TABLE_TO_PRODUCTS, values, KEY_TO_PRODUCT_ID + " = ?" + " AND " + KEY_TO_AGENTID + " = ? ",
                            new String[]{String.valueOf(takeOrderBeanArrayList.get(b).getmProductId()), String.valueOf(takeOrderBeanArrayList.get(b).getmAgentId())});
                    System.out.println("ELSEEE::: " + effectedRows);
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
    public long insertIntoTDCCustomers(TDCCustomer customer, String loginId) {
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
            values.put(KEY_TDC_CUSTOMER_ROUTECODE, customer.getRoutecode());
            values.put(KEY_TDC_CUSTOMER_CODE, customer.getCode());
            values.put(KEY_TDC_CUSTOMER_CHECK_UNIQUE_ID, loginId);

            values.put(KEY_TDC_CUSTOMER_SHOP_IMAGE_UPLOAD_STATUS, customer.getIsShopImageUploaded());

            values.put(KEY_TDC_CUSTOMER_UPLOAD_STATUS, customer.getIsUploasStatus());

            if (customer.getUserId().equals("") && customer.getIsCustUpdate().equals("false")) {
                customerId = db.insert(TABLE_TDC_CUSTOMERS, null, values);
            } else if (customer.getUserId().equals("") && customer.getIsCustUpdate().equals("true")) {
                customerId = db.update(TABLE_TDC_CUSTOMERS, values, KEY_TDC_CUSTOMER_ID + " = ?",
                        new String[]{String.valueOf(customer.getId())});
            } else {
                int val = checkRetailerExistsOrNot(customer.getUserId(), loginId);

                if (val == 0) {
                    customerId = db.insert(TABLE_TDC_CUSTOMERS, null, values);
                } else {
                    customerId = db.update(TABLE_TDC_CUSTOMERS, values, KEY_TDC_CUSTOMER_USER_ID + " = ?",
                            new String[]{String.valueOf(customer.getUserId())});
                }
            }
            values.clear();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customerId;



       /*     if (customer.getUserId().equals("")) {
         //Log.i("saveKey...",saveKey+"");
               // if (saveKey.equals("Save")) {
                System.out.println("RETAILER INSERTED+++++");
                customerId = db.insert(TABLE_TDC_CUSTOMERS, null, values);
            } else {
                int val = checkRetailerExistsOrNot(customer.getUserId(), loginId);
                System.out.println("VAL IS::: " + val);

                if (val == 0) {
                    System.out.println("RETAILER INSERTED 111+++++");
                    customerId = db.insert(TABLE_TDC_CUSTOMERS, null, values);
                } else {
                    System.out.println("RETAILER UPDATED+++++");
                    customerId = db.update(TABLE_TDC_CUSTOMERS, values, KEY_TDC_CUSTOMER_USER_ID + " = ?",
                            new String[]{String.valueOf(customer.getUserId())});
                }

            }

            values.clear();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customerId;*/

    }

    /**
     * Method to fetch all records from TDC Customers Table
     */
    public List<TDCCustomer> fetchAllRecordsFromTDCCustomers(String loginId) {
        List<TDCCustomer> allTDCCustomersList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TDC_CUSTOMERS + " WHERE " + KEY_TDC_CUSTOMER_IS_ACTIVE + " = 1"
                    + " AND " + KEY_TDC_CUSTOMER_CHECK_UNIQUE_ID + " = '" + loginId + "'";

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
                    customer.setRoutecode(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_ROUTECODE)));
                    customer.setCode(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_CODE)));
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
    public List<TDCCustomer> fetchRecordsFromTDCCustomers(int customerType, String loginId) {
        List<TDCCustomer> allRetailersList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TDC_CUSTOMERS + " WHERE " + KEY_TDC_CUSTOMER_IS_ACTIVE + " = 1 AND " + KEY_TDC_CUSTOMER_TYPE + " = " + customerType
                    + " AND " + KEY_TDC_CUSTOMER_CHECK_UNIQUE_ID + " = '" + loginId + "'";

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
                    customer.setRoutecode(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_ROUTECODE)));
                    customer.setCode(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_CODE)));
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
                    customer.setRoutecode(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_ROUTECODE)));
                    customer.setIsShopImageUploaded(c.getInt(c.getColumnIndex(KEY_TDC_CUSTOMER_SHOP_IMAGE_UPLOAD_STATUS)));
                    customer.setCode(c.getString(c.getColumnIndex(KEY_TDC_CUSTOMER_CODE)));

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
    public List<TDCSaleOrder> fetchTDCSalesOrdersForSelectedCustomer(String customerId) {
        List<TDCSaleOrder> allOrdersList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TDC_SALES_ORDERS + " WHERE " + KEY_TDC_SALES_ORDER_CUSTOMER_USER_ID + " = " + "'" + customerId + "'";

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
                    order.setSelectedCustomerCode(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_CUSTOMER_CODE)));
                    order.setSelectedCustomerName(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_CUSTOMER_NAME)));
                    order.setOrderBillNumber(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_BILL_NUMBER)));
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
    public List<TDCSaleOrder> fetchAllTDCSalesOrdersForSelectedDuration(String startDate, String endDate, String customerId) {
        List<TDCSaleOrder> allOrdersList = new ArrayList<>();

        try {
            Cursor c = null;
            SQLiteDatabase db = this.getReadableDatabase();
            if (customerId != null && !(customerId.equals(""))) {
                c = db.rawQuery("SELECT * FROM " + TABLE_TDC_SALES_ORDERS + " WHERE " + KEY_TDC_SALES_ORDER_CREATED_BY + " = " + "'" + customerId + "'" + "  AND " + KEY_TDC_SALES_ORDER_DATE + " BETWEEN ? AND ?", new String[]{startDate, endDate});
            } else {
                c = db.rawQuery("SELECT * FROM " + TABLE_TDC_SALES_ORDERS + " WHERE " + KEY_TDC_SALES_ORDER_DATE + " BETWEEN ? AND ?", new String[]{startDate, endDate});
            }

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
                    order.setSelectedCustomerCode(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_CUSTOMER_CODE)));
                    order.setSelectedCustomerName(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_CUSTOMER_NAME)));
                    order.setSelectedCustomerType(Long.parseLong(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_CUSTOMER_TYPE))));
                    order.setOrderBillNumber(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_BILL_NUMBER)));

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
                    productsBean.setProductCode(c.getString(c.getColumnIndex(KEY_TDC_SOP_PRODUCT_CODE)));
                    productsBean.setProductUOM(c.getString(c.getColumnIndex(KEY_TDC_SOP_UOM)));

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
            values.put(KEY_TDC_SALES_ORDER_CUSTOMER_CODE, order.getSelectedCustomerCode());
            values.put(KEY_TDC_SALES_ORDER_CUSTOMER_NAME, order.getSelectedCustomerName());
            values.put(KEY_TDC_SALES_ORDER_CUSTOMER_TYPE, order.getSelectedCustomerType());
            values.put(KEY_TDC_SALES_ORDER_BILL_NUMBER, order.getOrderBillNumber());
            values.put(KEY_TDC_SALES_ORDER_UPLOAD_STATUS, order.getIsUploaded());

//            orderId = db.insert(TABLE_TDC_SALES_ORDERS, null, values);
//            System.out.println("INSERT:::::" + orderId);
//            for (Map.Entry<String, ProductsBean> productsBeanEntry : order.getProductsList().entrySet()) {
//                this.insertIntoTDCSalesOrderProductsTable(orderId, productsBeanEntry.getValue());
//            }

            int val = checkTdcSaleIsExistsOrNot(order.getOrderBillNumber());
            System.out.println("BILL EXISTS::: " + val);
            if (val == 0) {
                orderId = db.insert(TABLE_TDC_SALES_ORDERS, null, values);
                System.out.println("INSERT:::::" + orderId);
            } else {
                System.out.println("UPDATE:::::");
                orderId = db.update(TABLE_TDC_SALES_ORDERS, values, KEY_TDC_SALES_ORDER_BILL_NUMBER + " = ?", new String[]{String.valueOf(order.getOrderBillNumber())});
                System.out.println("UPDATE::::::::::" + orderId);
            }
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
     * Method to get count of the tdc customers table
     */
    public int getTDCCustomersTableCount() {
        int noOfEvents = 0;
        try {
            String countQuery = "SELECT * FROM " + TABLE_TDC_CUSTOMERS;
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
            values.put(KEY_TDC_SOP_PRODUCT_CODE, orderProduct.getProductCode());
            values.put(KEY_TDC_SOP_UOM, orderProduct.getProductUOM());

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
                    order.setOrderBillNumber(c.getString(c.getColumnIndex(KEY_TDC_SALES_ORDER_BILL_NUMBER)));

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
                values.put(KEY_TRIPSHEET_MY_ID, mTripsheetsList.get(i).getMy_Id());
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
                values.put(KEY_TRIPSHEET_APPROVED_BY, mTripsheetsList.get(i).getApproved_by());
                values.put(KEY_TRIPSHEET_TRIP_ROUTE_ID, mTripsheetsList.get(i).getmTripRouteId());
                values.put(KEY_TRIPSHEET_TRIP_ROUTE_CODE, mTripsheetsList.get(i).getmTripRouteCode());

                int checkVal = checkTripsheetExistsOrNot(mTripsheetsList.get(i).getmTripshhetId());
                if (checkVal == 0) {
                    System.out.println("+++++++++++++++++++++++++ TRIP SHEET INSERTED++++++++++++++++++++++" + mTripsheetsList.get(i).getmTripshhetId());
                    values.put(KEY_TRIPSHEET_VERIFY_STATUS, "0");
                    db.insert(TABLE_TRIPSHEETS_LIST, null, values);
                } else {
                    System.out.println("+++++++++++++++++++++++++ TRIP SHEET UPDATED++++++++++++++++++++++" + mTripsheetsList.get(i).getmTripshhetId());
                    db.update(TABLE_TRIPSHEETS_LIST, values, KEY_TRIPSHEET_ID + " = ?", new String[]{String.valueOf(mTripsheetsList.get(i).getmTripshhetId())});
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
    public ArrayList<TripsheetsList> fetchTripsheetsList(String date) {
        ArrayList<TripsheetsList> alltripsheets = new ArrayList<TripsheetsList>();

        try {
            //String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_LIST + " WHERE " + KEY_TRIPSHEET_STATUS + " = " + "'" + "A" + "'";
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_LIST + " WHERE " + KEY_TRIPSHEET_DATE + " >= " + "'" + date + "'"
                    + " ORDER BY " + KEY_TRIPSHEET_DATE + " DESC ";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripsheetsList tripsheetsListBean = new TripsheetsList();
                    tripsheetsListBean.setmTripshhetId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_ID)));
                    tripsheetsListBean.setmTripshhetCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_CODE)));
                    tripsheetsListBean.setMy_Id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_MY_ID)));
                    tripsheetsListBean.setmTripshhetDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DATE)));
                    tripsheetsListBean.setmTripshhetStatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STATUS)));
                    tripsheetsListBean.setmTripshhetOBAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_OB_AMOUNT)));
                    tripsheetsListBean.setmTripshhetOrderedAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_ORDERED_AMOUNT)));
                    tripsheetsListBean.setApproved_by(c.getString(c.getColumnIndex(KEY_TRIPSHEET_APPROVED_BY)));
                    //tripsheetsListBean.setmTripshhetReceivedAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RECEIVED_AMOUNT)));
                    //tripsheetsListBean.setmTripshhetDueAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DUE_AMOUNT)));
                    tripsheetsListBean.setmTripshhetRouteCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_ROUTE_CODE)));
                    tripsheetsListBean.setmTripshhetSalesMenCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_SALESMEN_CODE)));
                    tripsheetsListBean.setmTripshhetVehicleNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_VEHICLE_NUMBER)));
                    tripsheetsListBean.setmTripshhetTrasnsporterName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_TRANSPORTER_NAME)));
                    tripsheetsListBean.setmTripshhetVerifyStatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_VERIFY_STATUS)));
                    tripsheetsListBean.setIsTripshhetClosed(c.getInt(c.getColumnIndex(KEY_TRIPSHEET_IS_TRIP_SHEET_CLOSED)));
                    tripsheetsListBean.setmTripRouteId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_TRIP_ROUTE_ID)));
                    tripsheetsListBean.setmTripRouteCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_TRIP_ROUTE_CODE)));

                    double receivedAmount = fetchTripSheetReceivedAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_ID)));
                    receivedAmount = Double.parseDouble(String.valueOf(receivedAmount).replace(",", ""));
                    double dueAmount = (Double.parseDouble(tripsheetsListBean.getmTripshhetOBAmount().replace(",", "")) + Double.parseDouble(tripsheetsListBean.getmTripshhetOrderedAmount().replace(",", ""))) - receivedAmount;

                    tripsheetsListBean.setmTripshhetReceivedAmount(String.valueOf(receivedAmount).replace(",", ""));
                    tripsheetsListBean.setmTripshhetDueAmount(String.valueOf(dueAmount).replace(",", ""));

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
            String selectQuery = "SELECT  * FROM " + TABLE_SPECIALPRICE + " WHERE " + KEY_USER_SPECIALID + " = '" + userId + "'";

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
                values.put(KEY_TRIPSHEET_STOCK_DELIVERY_QUANTITY, mTripsheetsStockList.get(i).getmDeliveryQuantity());
                values.put(KEY_TRIPSHEET_STOCK_RETURN_QUANTITY, mTripsheetsStockList.get(i).getmReturnQuantity());
                values.put(KEY_TRIPSHEET_STOCK_IS_DISPATCHED, mTripsheetsStockList.get(i).getIsStockDispatched());
                values.put(KEY_TRIPSHEET_STOCK_IS_VERIFIED, mTripsheetsStockList.get(i).getIsStockVerified());
                values.put(KEY_TRIPSHEET_STOCK_IN_STOCK_QUANTITY, mTripsheetsStockList.get(i).getInStockQuantity());
                values.put(KEY_TRIPSHEET_STOCK_EXTRA_QUANTITY, mTripsheetsStockList.get(i).getExtraQuantity());
                // Added by Sekhar for close trip
                values.put(KEY_TRIPSHEET_STOCK_CB_QUANTITY, mTripsheetsStockList.get(i).getmCBQuantity());
                values.put(KEY_TRIPSHEET_STOCK_DELIVERY_QUANTITY, mTripsheetsStockList.get(i).getmDeliveryQuantity());
                values.put(KEY_TRIPSHEET_STOCK_ROUTE_RETURN_QUANTITY, mTripsheetsStockList.get(i).getmRouteReturnQuantity());
                values.put(KEY_TRIPSHEET_STOCK_LEAK_QUANTITY, mTripsheetsStockList.get(i).getmLeakQuantity());
                values.put(KEY_TRIPSHEET_STOCK_OTHER_QUANTITY, mTripsheetsStockList.get(i).getmOtherQuantity());

                long l;
                int checkVal = checkTripsheetStockExistsOrNot(mTripsheetsStockList.get(i).getmTripsheetStockTripsheetId(),
                        mTripsheetsStockList.get(i).getmTripsheetStockId(), mTripsheetsStockList.get(i).getmTripsheetStockProductId());
                if (checkVal == 0) {
                    l = db.insert(TABLE_TRIPSHEETS_STOCK_LIST, null, values);
                    System.out.println("+++++++++++++++++++++++++ TRIP SHEET STOCK INSERTED++++++++++++++++++++++" + l);
                } else {
                    l = db.update(TABLE_TRIPSHEETS_STOCK_LIST, values, KEY_TRIPSHEET_STOCK_TRIPSHEET_ID + " = ?" + " AND " + KEY_TRIPSHEET_STOCK_ID + " = ?" + " AND " + KEY_TRIPSHEET_STOCK_PRODUCT_ID + " = ?",
                            new String[]{String.valueOf(mTripsheetsStockList.get(i).getmTripsheetStockTripsheetId()),
                                    String.valueOf(mTripsheetsStockList.get(i).getmTripsheetStockId()), String.valueOf(mTripsheetsStockList.get(i).getmTripsheetStockProductId())});
                    System.out.println("+++++++++++++++++++++++++ TRIP SHEET STOCK UPDATED++++++++++++++++++++++" + l);
                }
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

                    tripStockBean.setmDeliveryQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_DELIVERY_QUANTITY)));
                    tripStockBean.setmReturnQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_RETURN_QUANTITY)));
                    tripStockBean.setmLeakQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_LEAK_QUANTITY)));
                    tripStockBean.setmOtherQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_OTHER_QUANTITY)));
                    tripStockBean.setmRouteReturnQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_ROUTE_RETURN_QUANTITY)));

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
        try {
            for (TripSheetDeliveriesBean tripSheetDeliveriesBean : mTripsheetsDeliveriesList) {
                ContentValues values = new ContentValues();
                values.put(KEY_TRIPSHEET_DELIVERY_NUMBER, tripSheetDeliveriesBean.getmTripsheetDeliveryNumber());
                values.put(KEY_TRIPSHEET_DELIVERY_TRIP_ID, tripSheetDeliveriesBean.getmTripsheetDelivery_tripId());
                values.put(KEY_TRIPSHEET_DELIVERY_SO_ID, tripSheetDeliveriesBean.getmTripsheetDelivery_so_id());
                values.put(KEY_TRIPSHEET_DELIVERY_SO_CODE, tripSheetDeliveriesBean.getmTripsheetDelivery_so_code());
                values.put(KEY_TRIPSHEET_DELIVERY_USER_ID, tripSheetDeliveriesBean.getmTripsheetDelivery_userId());
                values.put(KEY_TRIPSHEET_DELIVERY_USER_CODES, tripSheetDeliveriesBean.getmTripsheetDelivery_userCodes());
                values.put(KEY_TRIPSHEET_DELIVERY_ROUTE_ID, tripSheetDeliveriesBean.getmTripsheetDelivery_routeId());
                values.put(KEY_TRIPSHEET_DELIVERY_ROUTE_CODES, tripSheetDeliveriesBean.getmTripsheetDelivery_routeCodes());
                if (tripSheetDeliveriesBean.getmTripsheetDelivery_productCodes().endsWith("_F")) {
                    String[] parts = tripSheetDeliveriesBean.getmTripsheetDelivery_productCodes().split("_");
                    values.put(KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES, parts[0]);
                    System.out.println(" IF INSERTED CODES::: " + tripSheetDeliveriesBean.getmTripsheetDelivery_productCodes().split("_"));
                } else {
                    values.put(KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES, tripSheetDeliveriesBean.getmTripsheetDelivery_productCodes());
                    System.out.println(" ELSE INSERTED CODES::: " + tripSheetDeliveriesBean.getmTripsheetDelivery_productCodes());
                }


                values.put(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS, tripSheetDeliveriesBean.getmTripsheetDelivery_productId());
                values.put(KEY_TRIPSHEET_DELIVERY_TAXPERCENT, tripSheetDeliveriesBean.getmTripsheetDelivery_TaxPercent());
                values.put(KEY_TRIPSHEET_DELIVERY_UNITPRICE, tripSheetDeliveriesBean.getmTripsheetDelivery_UnitPrice());
                values.put(KEY_TRIPSHEET_DELIVERY_QUANTITY, tripSheetDeliveriesBean.getmTripsheetDelivery_Quantity());
                values.put(KEY_TRIPSHEET_DELIVERY_AMOUNT, tripSheetDeliveriesBean.getmTripsheetDelivery_Amount());
                values.put(KEY_TRIPSHEET_DELIVERY_TAXAMOUNT, tripSheetDeliveriesBean.getmTripsheetDelivery_TaxAmount());
                values.put(KEY_TRIPSHEET_DELIVERY_TAXTOTAL, tripSheetDeliveriesBean.getmTripsheetDelivery_TaxTotal());
                values.put(KEY_TRIPSHEET_DELIVERY_SALEVALUE, tripSheetDeliveriesBean.getmTripsheetDelivery_SaleValue());
                values.put(KEY_TRIPSHEET_DELIVERY_STATUS, tripSheetDeliveriesBean.getmTripsheetDelivery_Status());
                values.put(KEY_TRIPSHEET_DELIVERY_DELETE, tripSheetDeliveriesBean.getmTripsheetDelivery_Delete());
                values.put(KEY_TRIPSHEET_DELIVERY_CREATEDBY, tripSheetDeliveriesBean.getmTripsheetDelivery_CreatedBy());
                values.put(KEY_TRIPSHEET_DELIVERY_CREATEDON, tripSheetDeliveriesBean.getmTripsheetDelivery_CreatedOn());
                values.put(KEY_TRIPSHEET_DELIVERY_UPDATEDON, tripSheetDeliveriesBean.getmTripsheetDelivery_UpdatedOn());
                values.put(KEY_TRIPSHEET_DELIVERY_UPDATEDBY, tripSheetDeliveriesBean.getmTripsheetDelivery_UpdatedBy());
                values.put(KEY_TRIPSHEET_DELIVERY_PRODUCT_TYPE, tripSheetDeliveriesBean.getmTripsheetDelivery_productType());
                values.put(KEY_TRIPSHEET_DELIVERY_UOM, tripSheetDeliveriesBean.getmTripsheetDelivery_productUOM());
                values.put(KEY_TRIPSHEET_DELIVERY_UPLOAD_STATUS, 0);

                int noOfRecordsExisted = checkProductExistsInTripSheetDeliveryTable(tripSheetDeliveriesBean.getmTripsheetDelivery_so_id(), tripSheetDeliveriesBean.getmTripsheetDelivery_userId(), tripSheetDeliveriesBean.getmTripsheetDelivery_productId());

                SQLiteDatabase db = this.getWritableDatabase();
                long status;

                if (noOfRecordsExisted == 0) {
                    status = db.insert(TABLE_TRIPSHEETS_DELIVERIES_LIST, null, values);
                } else {
                    status = db.update(TABLE_TRIPSHEETS_DELIVERIES_LIST, values, KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = ? AND " + KEY_TRIPSHEET_DELIVERY_SO_ID + " = ? AND " + KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS + " = ?", new String[]{tripSheetDeliveriesBean.getmTripsheetDelivery_tripId(), tripSheetDeliveriesBean.getmTripsheetDelivery_so_id(), tripSheetDeliveriesBean.getmTripsheetDelivery_productId()});
                }

                values.clear();
                db.close();

                if (status > 0) {
                    updateProductStockInTripSheetStockTable(tripSheetDeliveriesBean.getmTripsheetDelivery_tripId(), tripSheetDeliveriesBean.getmTripsheetDelivery_productCodes(), tripSheetDeliveriesBean.getProductRemainingInStock(), tripSheetDeliveriesBean.getProductRemainingExtraStock());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> fetchUnUploadedUniqueDeliverySoIds() {
        ArrayList<String> tripSheetIds = new ArrayList<>();

        try {
            String selectQuery = "SELECT DISTINCT " + KEY_TRIPSHEET_DELIVERY_SO_ID + " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_UPLOAD_STATUS + " = 0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    tripSheetIds.add(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SO_ID)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripSheetIds;
    }

    public ArrayList<String> fetchUnUploadedUniqueReturnsSoIds() {
        ArrayList<String> tripSheetIds = new ArrayList<>();

        try {
            String selectQuery = "SELECT DISTINCT " + KEY_TRIPSHEET_RETURNS_SO_ID + " FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_UPLOAD_STATUS + " = 0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    tripSheetIds.add(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_SO_ID)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripSheetIds;
    }


    /**
     * Method to fetch all tripsheets deliveries list
     */
    public ArrayList<TripSheetDeliveriesBean> fetchAllTripsheetsDeliveriesList(String tripsheetId, String soId) {
        ArrayList<TripSheetDeliveriesBean> alltripsheetsDeliveries = new ArrayList<TripSheetDeliveriesBean>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = " + "'" + tripsheetId + "'"
                    + " AND " + KEY_TRIPSHEET_DELIVERY_SO_ID + "= '" + soId + "'" + " AND " + KEY_TRIPSHEET_DELIVERY_UPLOAD_STATUS + " = 0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripSheetDeliveriesBean tripDeliveriesBean = new TripSheetDeliveriesBean();

                    tripDeliveriesBean.setmTripsheetDeliveryNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NO)));
                    tripDeliveriesBean.setmTripsheetDeliveryNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NUMBER)));
                    tripDeliveriesBean.setmTripsheetDelivery_tripId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TRIP_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_so_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SO_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_so_code(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SO_CODE)));
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
                    tripDeliveriesBean.setmTripsheetDelivery_productType(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_TYPE)));
                    tripDeliveriesBean.setmTripsheetDelivery_productUOM(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_UOM)));

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
     * Method to fetch all tripsheets deliveries list
     */
    public ArrayList<TripSheetDeliveriesBean> fetchAllTripsheetsDeliveriesListForAgents(String tripsheetId) {
        ArrayList<TripSheetDeliveriesBean> alltripsheetsDeliveries = new ArrayList<TripSheetDeliveriesBean>();

        try {
//            String selectQuery = "SELECT DISTINCT(tripsheet_delivery_number) AS Tripsheet_Delivery_Number" +
//                    ", COUNT(tripsheet_delivery_number) AS Total_COUNT  FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = '" + tripsheetId + "'" + " GROUP BY " + KEY_TRIPSHEET_DELIVERY_NUMBER + " ORDER BY " + KEY_TRIPSHEET_DELIVERY_CREATEDON + " DESC ";
//            String selectQuery1 = "SELECT DISTINCT " + KEY_TRIPSHEET_DELIVERY_NUMBER
//                    + " , count("+ KEY_TRIPSHEET_DELIVERY_NUMBER + ")"
//                    +  " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = '" + tripsheetId + "'"
//                    + " GROUP BY "+ KEY_TRIPSHEET_DELIVERY_NUMBER;
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor c = db.rawQuery(selectQuery, null);
//            System.out.println("Deliver Items Count:: " + c.getCount());
//            HashMap<String, String> records = new HashMap<>();
//            if (c.moveToFirst()) {
//                do {
//                    records.put(c.getString(c.getColumnIndex("Tripsheet_Delivery_Number")), c.getString(c.getColumnIndex("Total_COUNT")));
//                } while (c.moveToNext());
//            }

            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = " + "'" + tripsheetId + "'"
                    + " GROUP BY " + KEY_TRIPSHEET_DELIVERY_NUMBER + ", " + KEY_TRIPSHEET_DELIVERY_CREATEDON
                    + " ORDER BY " + KEY_TRIPSHEET_DELIVERY_CREATEDON + " DESC ";
            //  + " AND " + KEY_TRIPSHEET_DELIVERY_UPLOAD_STATUS + " = 0"

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

//            for (Map.Entry<String, String> entry : records.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue();
//                selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE tripsheet_delivery_number  = '" + key + "'" + " ORDER BY " + KEY_TRIPSHEET_DELIVERY_CREATEDON + " DESC ";
//                c = db.rawQuery(selectQuery, null);
//                boolean rerun = true;
//                if (c.moveToFirst()) {
//                    do {
//                        if (rerun) {
//                            TripSheetDeliveriesBean tripDeliveriesBean = new TripSheetDeliveriesBean();
//
//                            tripDeliveriesBean.setmTripsheetDeliveryNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NO)));
//                            tripDeliveriesBean.setmTripsheetDeliveryNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NUMBER)));
//                            tripDeliveriesBean.setmTripsheetDelivery_tripId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TRIP_ID)));
//                            tripDeliveriesBean.setmTripsheetDelivery_so_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SO_ID)));
//                            tripDeliveriesBean.setmTripsheetDelivery_so_code(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SO_CODE)));
//                            tripDeliveriesBean.setmTripsheetDelivery_userId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_USER_ID)));
//                            tripDeliveriesBean.setmTripsheetDelivery_userCodes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_USER_CODES)));
//                            tripDeliveriesBean.setmTripsheetDelivery_routeId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_ROUTE_ID)));
//                            tripDeliveriesBean.setmTripsheetDelivery_routeCodes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_ROUTE_CODES)));
//                            tripDeliveriesBean.setmTripsheetDelivery_productId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)));
//                            tripDeliveriesBean.setmTripsheetDelivery_productCodes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES)));
//                            tripDeliveriesBean.setmTripsheetDelivery_TaxPercent(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXPERCENT)));
//                            tripDeliveriesBean.setmTripsheetDelivery_UnitPrice(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_UNITPRICE)));
//                            tripDeliveriesBean.setmTripsheetDelivery_Quantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_QUANTITY)));
//                            tripDeliveriesBean.setmTripsheetDelivery_Amount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_AMOUNT)));
//                            tripDeliveriesBean.setmTripsheetDelivery_TaxAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXAMOUNT)));
//                            tripDeliveriesBean.setmTripsheetDelivery_TaxTotal(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXTOTAL)));
//                            tripDeliveriesBean.setmTripsheetDelivery_SaleValue(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SALEVALUE)));
//                            tripDeliveriesBean.setmTripsheetDelivery_Status(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_STATUS)));
//                            tripDeliveriesBean.setmTripsheetDelivery_Delete(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_DELETE)));
//                            tripDeliveriesBean.setmTripsheetDelivery_CreatedBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDBY)));
//                            tripDeliveriesBean.setmTripsheetDelivery_CreatedOn(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDON)));
//                            tripDeliveriesBean.setmTripsheetDelivery_UpdatedOn(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_UPDATEDON)));
//                            tripDeliveriesBean.setmTripsheetDelivery_UpdatedBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_UPDATEDBY)));
//                            tripDeliveriesBean.setDeliveredItemsCount(value);
//
//                            alltripsheetsDeliveries.add(tripDeliveriesBean);
//                            rerun = false;
//                        }
//                    } while (c.moveToNext());
//                }
//
//            }

            if (c.moveToFirst()) {
                do {
                    TripSheetDeliveriesBean tripDeliveriesBean = new TripSheetDeliveriesBean();

                    tripDeliveriesBean.setmTripsheetDeliveryNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NO)));
                    tripDeliveriesBean.setmTripsheetDeliveryNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NUMBER)));
                    tripDeliveriesBean.setmTripsheetDelivery_tripId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TRIP_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_so_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SO_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_so_code(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SO_CODE)));
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

    public ArrayList<TripSheetReturnsBean> fetchAllTripsheetsReturnsListForAgents(String userId) {
        ArrayList<TripSheetReturnsBean> agentsreturnsBean = new ArrayList<>();

        try {


           /* String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_USER_ID + " = " + "'" + userId + "'"
                    + " GROUP BY " + KEY_TRIPSHEET_RETURNS_RETURN_NUMBER + ", " + KEY_TRIPSHEET_RETURNS_CREATED_ON
                    + " ORDER BY " + KEY_TRIPSHEET_RETURNS_CREATED_ON + " DESC ";
            */


            // String selectQuery = "SELECT DISTINCT(tripsheet_returns_return_number) AS Tripsheet_Return_Number" +
            //         ", COUNT(tripsheet_returns_return_number) AS Total_COUNT  FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_USER_ID + " = '" + userId + "'" + " GROUP BY " + KEY_TRIPSHEET_RETURNS_RETURN_NUMBER + " ORDER BY " + KEY_TRIPSHEET_RETURNS_CREATED_ON + " DESC ";
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_USER_ID + " = " + "'" + userId + "'" + " ORDER BY " + KEY_TRIPSHEET_RETURNS_CREATED_ON + " DESC ";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            // HashMap<String, String> records = new HashMap<>();
            // if (c.moveToFirst()) {
            //     do {
            //         records.put(c.getString(c.getColumnIndex("Tripsheet_Return_Number")), c.getString(c.getColumnIndex("Total_COUNT")));
//                    AgentDeliveriesBean returndeliveriesBean = new AgentDeliveriesBean();
//                    returndeliveriesBean.setTripNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NUMBER)));
//                    returndeliveriesBean.setTripDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDON)));
//                    returndeliveriesBean.setDeliverdstatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_STATUS)));
//                    returndeliveriesBean.setDeliveredItems(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SALEVALUE)));
//                    returndeliveriesBean.setDeliveredBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDBY)));
//                    deliveriesBean.add(returndeliveriesBean);
            // } while (c.moveToNext());
            // }
            //for (Map.Entry<String, String> entry : records.entrySet()) {
            //  String key = entry.getKey();
            //  String value = entry.getValue();
            // selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE tripsheet_returns_return_number  = '" + key + "'" + " ORDER BY " + KEY_TRIPSHEET_RETURNS_CREATED_ON + " DESC ";
            //  c = db.rawQuery(selectQuery, null);
            //  boolean rerun = true;
            if (c.moveToFirst()) {
                do {
                    //  if (rerun) {
                    TripSheetReturnsBean tripReturnsBean = new TripSheetReturnsBean();
                    tripReturnsBean.setmTripshhetReturnsReturn_no(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_RETURN_NO)));
                    tripReturnsBean.setmTripshhetReturnsReturn_number(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_RETURN_NUMBER)));
                    tripReturnsBean.setmTripshhetReturnsTrip_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_TRIP_ID)));
                    tripReturnsBean.setmTripshhetReturns_so_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_SO_ID)));
                    tripReturnsBean.setmTripshhetReturns_so_code(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_SO_CODE)));
                    tripReturnsBean.setmTripshhetReturnsUser_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_USER_ID)));
                    tripReturnsBean.setmTripshhetReturnsUser_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_USER_CODES)));
                    tripReturnsBean.setmTripshhetReturnsRoute_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_ROUTE_ID)));
                    tripReturnsBean.setmTripshhetReturnsRoute_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_ROUTE_CODES)));
                    tripReturnsBean.setmTripshhetReturnsProduct_ids(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS)));
                    tripReturnsBean.setmTripshhetReturnsProduct_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES)));
                    tripReturnsBean.setmTripshhetReturnsQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_QUANTITY)));
                    tripReturnsBean.setmTripshhetReturnsType(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_TYPE)));
                    tripReturnsBean.setmTripshhetReturnsStatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_STATUS)));
                    tripReturnsBean.setmTripshhetReturnsDelete(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_DELETE)));
                    tripReturnsBean.setmTripshhetReturnsCreated_by(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_CREATED_BY)));
                    tripReturnsBean.setmTripshhetReturnsCreated_on(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_CREATED_ON)));
                    tripReturnsBean.setmTripshhetReturnsUpdated_on(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_UPDATED_ON)));
                    tripReturnsBean.setmTripshhetReturnsUpdated_by(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_UPDATED_BY)));
                    //  tripReturnsBean.setReturnsItemsCount(value);
                    agentsreturnsBean.add(tripReturnsBean);
                    //  rerun = false;
                    //   }
                } while (c.moveToNext());
            }

            //}
            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return agentsreturnsBean;
    }


    /**
     * Method to update the mTripsheetsDeliveriesList.
     *
     * @param mTripsheetsDeliveriesList
     */
    public void updateTripsheetsDeliveriesListData(ArrayList<TripSheetDeliveriesBean> mTripsheetsDeliveriesList) {
        try {
            for (TripSheetDeliveriesBean tripSheetDeliveriesBean : mTripsheetsDeliveriesList) {
                ContentValues values = new ContentValues();
                ContentValues values1 = new ContentValues();
                values.put(KEY_TRIPSHEET_DELIVERY_NUMBER, tripSheetDeliveriesBean.getmTripsheetDeliveryNumber());
                values.put(KEY_TRIPSHEET_DELIVERY_TRIP_ID, tripSheetDeliveriesBean.getmTripsheetDelivery_tripId());
                values.put(KEY_TRIPSHEET_DELIVERY_SO_ID, tripSheetDeliveriesBean.getmTripsheetDelivery_so_id());
                values.put(KEY_TRIPSHEET_DELIVERY_SO_CODE, tripSheetDeliveriesBean.getmTripsheetDelivery_so_code());
                values.put(KEY_TRIPSHEET_DELIVERY_USER_ID, tripSheetDeliveriesBean.getmTripsheetDelivery_userId());
                values.put(KEY_TRIPSHEET_DELIVERY_USER_CODES, tripSheetDeliveriesBean.getmTripsheetDelivery_userCodes());
                values.put(KEY_TRIPSHEET_DELIVERY_ROUTE_ID, tripSheetDeliveriesBean.getmTripsheetDelivery_routeId());
                values.put(KEY_TRIPSHEET_DELIVERY_ROUTE_CODES, tripSheetDeliveriesBean.getmTripsheetDelivery_routeCodes());
                values.put(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS, tripSheetDeliveriesBean.getmTripsheetDelivery_productId());
                values.put(KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES, tripSheetDeliveriesBean.getmTripsheetDelivery_productCodes());
                values.put(KEY_TRIPSHEET_DELIVERY_TAXPERCENT, tripSheetDeliveriesBean.getmTripsheetDelivery_TaxPercent());
                values.put(KEY_TRIPSHEET_DELIVERY_UNITPRICE, tripSheetDeliveriesBean.getmTripsheetDelivery_UnitPrice());
                values.put(KEY_TRIPSHEET_DELIVERY_QUANTITY, tripSheetDeliveriesBean.getmTripsheetDelivery_Quantity());
                values.put(KEY_TRIPSHEET_DELIVERY_AMOUNT, tripSheetDeliveriesBean.getmTripsheetDelivery_Amount());
                values.put(KEY_TRIPSHEET_DELIVERY_TAXAMOUNT, tripSheetDeliveriesBean.getmTripsheetDelivery_TaxAmount());
                values.put(KEY_TRIPSHEET_DELIVERY_TAXTOTAL, tripSheetDeliveriesBean.getmTripsheetDelivery_TaxTotal());
                values.put(KEY_TRIPSHEET_DELIVERY_SALEVALUE, tripSheetDeliveriesBean.getmTripsheetDelivery_SaleValue());
                values.put(KEY_TRIPSHEET_DELIVERY_STATUS, tripSheetDeliveriesBean.getmTripsheetDelivery_Status());
                values.put(KEY_TRIPSHEET_DELIVERY_DELETE, tripSheetDeliveriesBean.getmTripsheetDelivery_Delete());
                values.put(KEY_TRIPSHEET_DELIVERY_CREATEDBY, tripSheetDeliveriesBean.getmTripsheetDelivery_CreatedBy());
                values.put(KEY_TRIPSHEET_DELIVERY_CREATEDON, tripSheetDeliveriesBean.getmTripsheetDelivery_CreatedOn());
                values.put(KEY_TRIPSHEET_DELIVERY_UPDATEDON, tripSheetDeliveriesBean.getmTripsheetDelivery_UpdatedOn());
                values.put(KEY_TRIPSHEET_DELIVERY_UPDATEDBY, tripSheetDeliveriesBean.getmTripsheetDelivery_UpdatedBy());

                int noOfRecordsExisted = checkProductExistsInTripSheetDeliveryTable(tripSheetDeliveriesBean.getmTripsheetDelivery_so_id(), tripSheetDeliveriesBean.getmTripsheetDelivery_userId(), tripSheetDeliveriesBean.getmTripsheetDelivery_productId());
                System.out.println("AGENT DEL RECORD EXISTS++++++++" + noOfRecordsExisted);
                SQLiteDatabase db = this.getWritableDatabase();
                long status;

                if (noOfRecordsExisted == 0) {
                    values.put(KEY_TRIPSHEET_DELIVERY_UPLOAD_STATUS, 0);
                    status = db.insert(TABLE_TRIPSHEETS_DELIVERIES_LIST, null, values);
                    System.out.println("AGENT DEL INSERTED++++++++" + status);
                } else {
                    values.put(KEY_TRIPSHEET_DELIVERY_UPLOAD_STATUS, 1);
                    //values1.put(KEY_TRIPSHEET_DELIVERY_NUMBER, tripSheetDeliveriesBean.getmTripsheetDeliveryNumber());
                    //values1.put(KEY_TRIPSHEET_DELIVERY_QUANTITY, tripSheetDeliveriesBean.getmTripsheetDelivery_Quantity());
                    status = db.update(TABLE_TRIPSHEETS_DELIVERIES_LIST, values, KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = ? AND "
                                    + KEY_TRIPSHEET_DELIVERY_SO_ID + " = ? AND " + KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS + " = ? AND "
                                    + KEY_TRIPSHEET_DELIVERY_USER_ID + " = ?",
                            new String[]{tripSheetDeliveriesBean.getmTripsheetDelivery_tripId(),
                                    tripSheetDeliveriesBean.getmTripsheetDelivery_so_id(),
                                    tripSheetDeliveriesBean.getmTripsheetDelivery_productId(),
                                    tripSheetDeliveriesBean.getmTripsheetDelivery_userId()});
                    System.out.println("AGENT DEL UPDATED++++++++" + status);
                }

                values.clear();
                values1.clear();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to insert the mTripsheetsReturnsList.
     *
     * @param mTripsheetsReturnsList
     */
    public void insertTripsheetsReturnsListData(ArrayList<TripSheetReturnsBean> mTripsheetsReturnsList) {
        try {
            for (TripSheetReturnsBean tripSheetReturnsBean : mTripsheetsReturnsList) {
                ContentValues values = new ContentValues();
                values.put(KEY_TRIPSHEET_RETURNS_RETURN_NUMBER, tripSheetReturnsBean.getmTripshhetReturnsReturn_number());
                values.put(KEY_TRIPSHEET_RETURNS_TRIP_ID, tripSheetReturnsBean.getmTripshhetReturnsTrip_id());
                values.put(KEY_TRIPSHEET_RETURNS_SO_ID, tripSheetReturnsBean.getmTripshhetReturns_so_id());
                values.put(KEY_TRIPSHEET_RETURNS_SO_CODE, tripSheetReturnsBean.getmTripshhetReturns_so_code());
                values.put(KEY_TRIPSHEET_RETURNS_USER_ID, tripSheetReturnsBean.getmTripshhetReturnsUser_id());
                values.put(KEY_TRIPSHEET_RETURNS_USER_CODES, tripSheetReturnsBean.getmTripshhetReturnsUser_codes());
                values.put(KEY_TRIPSHEET_RETURNS_ROUTE_ID, tripSheetReturnsBean.getmTripshhetReturnsRoute_id());
                values.put(KEY_TRIPSHEET_RETURNS_ROUTE_CODES, tripSheetReturnsBean.getmTripshhetReturnsRoute_codes());
                values.put(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS, tripSheetReturnsBean.getmTripshhetReturnsProduct_ids());
                values.put(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES, tripSheetReturnsBean.getmTripshhetReturnsProduct_codes());
                values.put(KEY_TRIPSHEET_RETURNS_QUANTITY, tripSheetReturnsBean.getmTripshhetReturnsQuantity());
                values.put(KEY_TRIPSHEET_RETURNS_TYPE, tripSheetReturnsBean.getmTripshhetReturnsType());
                values.put(KEY_TRIPSHEET_RETURNS_STATUS, tripSheetReturnsBean.getmTripshhetReturnsStatus());
                values.put(KEY_TRIPSHEET_RETURNS_DELETE, tripSheetReturnsBean.getmTripshhetReturnsDelete());
                values.put(KEY_TRIPSHEET_RETURNS_CREATED_BY, tripSheetReturnsBean.getmTripshhetReturnsCreated_by());
                values.put(KEY_TRIPSHEET_RETURNS_CREATED_ON, tripSheetReturnsBean.getmTripshhetReturnsCreated_on());
                values.put(KEY_TRIPSHEET_RETURNS_UPDATED_ON, tripSheetReturnsBean.getmTripshhetReturnsUpdated_on());
                values.put(KEY_TRIPSHEET_RETURNS_UPDATED_BY, tripSheetReturnsBean.getmTripshhetReturnsUpdated_by());
                values.put(KEY_TRIPSHEET_RETURNS_UPLOAD_STATUS, 0);

                int noOfRecordsExisted = checkProductExistsInTripSheetReturnsTable(tripSheetReturnsBean.getmTripshhetReturns_so_id(), tripSheetReturnsBean.getmTripshhetReturnsUser_id(), tripSheetReturnsBean.getmTripshhetReturnsProduct_ids());

                SQLiteDatabase db = this.getWritableDatabase();

                long status;

                if (noOfRecordsExisted == 0) {
                    status = db.insert(TABLE_TRIPSHEETS_RETURNS_LIST, null, values);
                } else {
                    status = db.update(TABLE_TRIPSHEETS_RETURNS_LIST, values, KEY_TRIPSHEET_RETURNS_TRIP_ID + " = ? AND " + KEY_TRIPSHEET_RETURNS_SO_ID + " = ? AND " + KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS + " = ?", new String[]{tripSheetReturnsBean.getmTripshhetReturnsTrip_id(), tripSheetReturnsBean.getmTripshhetReturns_so_id(), tripSheetReturnsBean.getmTripshhetReturnsProduct_ids()});
                }

                values.clear();
                db.close();


                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to fetch all tripsheets returns list baed on tripsheet id from Tripsheets returns list table
     */
    public ArrayList<TripSheetReturnsBean> fetchAllTripsheetsReturnsList(String tripsheetId, String soId) {
        ArrayList<TripSheetReturnsBean> alltripsheetsReturns = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_TRIP_ID + " = " + "'" + tripsheetId + "'"
                    + " AND " + KEY_TRIPSHEET_RETURNS_SO_ID + " = '" + soId + "'" + " AND " + KEY_TRIPSHEET_RETURNS_UPLOAD_STATUS + " = 0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripSheetReturnsBean tripReturnsBean = new TripSheetReturnsBean();

                    tripReturnsBean.setmTripshhetReturnsReturn_no(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_RETURN_NO)));
                    tripReturnsBean.setmTripshhetReturnsReturn_number(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_RETURN_NUMBER)));
                    tripReturnsBean.setmTripshhetReturnsTrip_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_TRIP_ID)));
                    tripReturnsBean.setmTripshhetReturns_so_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_SO_ID)));
                    tripReturnsBean.setmTripshhetReturns_so_code(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_SO_CODE)));
                    tripReturnsBean.setmTripshhetReturnsUser_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_USER_ID)));
                    tripReturnsBean.setmTripshhetReturnsUser_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_USER_CODES)));
                    tripReturnsBean.setmTripshhetReturnsRoute_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_ROUTE_ID)));
                    tripReturnsBean.setmTripshhetReturnsRoute_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_ROUTE_CODES)));
                    tripReturnsBean.setmTripshhetReturnsProduct_ids(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS)));
                    tripReturnsBean.setmTripshhetReturnsProduct_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES)));
                    tripReturnsBean.setmTripshhetReturnsQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_QUANTITY)));
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
     * Method to insert the mTripsheetsReturnsList.
     *
     * @param mTripsheetsReturnsList
     */
    /*public void updateTripsheetsReturnsListData(ArrayList<TripSheetReturnsBean> mTripsheetsReturnsList) {
        try {
            for (TripSheetReturnsBean tripSheetReturnsBean : mTripsheetsReturnsList) {
                ContentValues values = new ContentValues();
                values.put(KEY_TRIPSHEET_RETURNS_TRIP_ID, tripSheetReturnsBean.getmTripshhetReturnsTrip_id());
                values.put(KEY_TRIPSHEET_RETURNS_SO_ID, tripSheetReturnsBean.getmTripshhetReturns_so_id());
                values.put(KEY_TRIPSHEET_RETURNS_SO_CODE, tripSheetReturnsBean.getmTripshhetReturns_so_code());
                values.put(KEY_TRIPSHEET_RETURNS_USER_ID, tripSheetReturnsBean.getmTripshhetReturnsUser_id());
                values.put(KEY_TRIPSHEET_RETURNS_USER_CODES, tripSheetReturnsBean.getmTripshhetReturnsUser_codes());
                values.put(KEY_TRIPSHEET_RETURNS_ROUTE_ID, tripSheetReturnsBean.getmTripshhetReturnsRoute_id());
                values.put(KEY_TRIPSHEET_RETURNS_ROUTE_CODES, tripSheetReturnsBean.getmTripshhetReturnsRoute_codes());
                values.put(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS, tripSheetReturnsBean.getmTripshhetReturnsProduct_ids());
                values.put(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES, tripSheetReturnsBean.getmTripshhetReturnsProduct_codes());
                values.put(KEY_TRIPSHEET_RETURNS_QUANTITY, tripSheetReturnsBean.getmTripshhetReturnsQuantity());
                values.put(KEY_TRIPSHEET_RETURNS_TYPE, tripSheetReturnsBean.getmTripshhetReturnsType());
                values.put(KEY_TRIPSHEET_RETURNS_STATUS, tripSheetReturnsBean.getmTripshhetReturnsStatus());
                values.put(KEY_TRIPSHEET_RETURNS_DELETE, tripSheetReturnsBean.getmTripshhetReturnsDelete());
                values.put(KEY_TRIPSHEET_RETURNS_CREATED_BY, tripSheetReturnsBean.getmTripshhetReturnsCreated_by());
                values.put(KEY_TRIPSHEET_RETURNS_CREATED_ON, tripSheetReturnsBean.getmTripshhetReturnsCreated_on());
                values.put(KEY_TRIPSHEET_RETURNS_UPDATED_ON, tripSheetReturnsBean.getmTripshhetReturnsUpdated_on());
                values.put(KEY_TRIPSHEET_RETURNS_UPDATED_BY, tripSheetReturnsBean.getmTripshhetReturnsUpdated_by());
                values.put(KEY_TRIPSHEET_RETURNS_UPLOAD_STATUS, 0);

                int noOfRecordsExisted = checkProductExistsInTripSheetReturnsTable(tripSheetReturnsBean.getmTripshhetReturns_so_id(), tripSheetReturnsBean.getmTripshhetReturnsUser_id(), tripSheetReturnsBean.getmTripshhetReturnsProduct_ids());

                SQLiteDatabase db = this.getWritableDatabase();

                long status;

                if (noOfRecordsExisted == 0) {
                    status = db.insert(TABLE_TRIPSHEETS_RETURNS_LIST, null, values);
                } else {
                    status = db.update(TABLE_TRIPSHEETS_RETURNS_LIST, values, KEY_TRIPSHEET_RETURNS_TRIP_ID + " = ? AND " + KEY_TRIPSHEET_RETURNS_SO_ID + " = ? AND " + KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS + " = ?", new String[]{tripSheetReturnsBean.getmTripshhetReturnsTrip_id(), tripSheetReturnsBean.getmTripshhetReturns_so_id(), tripSheetReturnsBean.getmTripshhetReturnsProduct_ids()});
                }

                values.clear();
                db.close();


                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    public void updateTripsheetsReturnsListData(ArrayList<TripSheetReturnsBean> mTripsheetsReturnsList) {
        try {
            for (TripSheetReturnsBean tripSheetReturnsBean : mTripsheetsReturnsList) {
                ContentValues values = new ContentValues();
                ContentValues values1 = new ContentValues();
                values.put(KEY_TRIPSHEET_RETURNS_RETURN_NUMBER, tripSheetReturnsBean.getmTripshhetReturnsReturn_number());
                values.put(KEY_TRIPSHEET_RETURNS_TRIP_ID, tripSheetReturnsBean.getmTripshhetReturnsTrip_id());
                values.put(KEY_TRIPSHEET_RETURNS_SO_ID, tripSheetReturnsBean.getmTripshhetReturns_so_id());
                values.put(KEY_TRIPSHEET_RETURNS_SO_CODE, tripSheetReturnsBean.getmTripshhetReturns_so_code());
                values.put(KEY_TRIPSHEET_RETURNS_USER_ID, tripSheetReturnsBean.getmTripshhetReturnsUser_id());
                values.put(KEY_TRIPSHEET_RETURNS_USER_CODES, tripSheetReturnsBean.getmTripshhetReturnsUser_codes());
                values.put(KEY_TRIPSHEET_RETURNS_ROUTE_ID, tripSheetReturnsBean.getmTripshhetReturnsRoute_id());
                values.put(KEY_TRIPSHEET_RETURNS_ROUTE_CODES, tripSheetReturnsBean.getmTripshhetReturnsRoute_codes());
                values.put(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS, tripSheetReturnsBean.getmTripshhetReturnsProduct_ids());
                values.put(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES, tripSheetReturnsBean.getmTripshhetReturnsProduct_codes());
                values.put(KEY_TRIPSHEET_RETURNS_QUANTITY, tripSheetReturnsBean.getmTripshhetReturnsQuantity());
                values.put(KEY_TRIPSHEET_RETURNS_TYPE, tripSheetReturnsBean.getmTripshhetReturnsType());
                values.put(KEY_TRIPSHEET_RETURNS_STATUS, tripSheetReturnsBean.getmTripshhetReturnsStatus());
                values.put(KEY_TRIPSHEET_RETURNS_DELETE, tripSheetReturnsBean.getmTripshhetReturnsDelete());
                values.put(KEY_TRIPSHEET_RETURNS_CREATED_BY, tripSheetReturnsBean.getmTripshhetReturnsCreated_by());
                values.put(KEY_TRIPSHEET_RETURNS_CREATED_ON, tripSheetReturnsBean.getmTripshhetReturnsCreated_on());
                values.put(KEY_TRIPSHEET_RETURNS_UPDATED_ON, tripSheetReturnsBean.getmTripshhetReturnsUpdated_on());
                values.put(KEY_TRIPSHEET_RETURNS_UPDATED_BY, tripSheetReturnsBean.getmTripshhetReturnsUpdated_by());

                int noOfRecordsExisted = checkProductExistsInTripSheetReturnsTable(tripSheetReturnsBean.getmTripshhetReturns_so_id(), tripSheetReturnsBean.getmTripshhetReturnsUser_id(), tripSheetReturnsBean.getmTripshhetReturnsProduct_ids());
                System.out.println("AGENT RET RECORD EXISTS++++++++" + noOfRecordsExisted);
                SQLiteDatabase db = this.getWritableDatabase();
                long status;

                if (noOfRecordsExisted == 0) {
                    values.put(KEY_TRIPSHEET_RETURNS_UPLOAD_STATUS, 0);
                    status = db.insert(TABLE_TRIPSHEETS_RETURNS_LIST, null, values);
                    System.out.println("AGENT RETURN INSERTED++++++++" + status);
                } else {
                    values.put(KEY_TRIPSHEET_RETURNS_UPLOAD_STATUS, 1);
                    // values1.put(KEY_TRIPSHEET_RETURNS_RETURN_NUMBER, tripSheetReturnsBean.getmTripshhetReturnsReturn_number());
                    //values1.put(KEY_TRIPSHEET_RETURNS_QUANTITY, tripSheetReturnsBean.getmTripshhetReturnsQuantity());
                    status = db.update(TABLE_TRIPSHEETS_RETURNS_LIST, values, KEY_TRIPSHEET_RETURNS_TRIP_ID + " = ? AND "
                                    + KEY_TRIPSHEET_RETURNS_SO_ID + " = ? AND " + KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS + " = ? AND "
                                    + KEY_TRIPSHEET_RETURNS_USER_ID + " = ?",
                            new String[]{tripSheetReturnsBean.getmTripshhetReturnsTrip_id(),
                                    tripSheetReturnsBean.getmTripshhetReturns_so_id(),
                                    tripSheetReturnsBean.getmTripshhetReturnsProduct_ids(),
                                    tripSheetReturnsBean.getmTripshhetReturnsUser_id()});
                    System.out.println("AGENT RETURN UPDATED++++++++" + status);
                }

                values.clear();
                // values1.clear();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTripsheetsPaymentsListData(ArrayList<PaymentsBean> mTripsheetsPaymentsList) {
        try {
            for (PaymentsBean paymentsBean : mTripsheetsPaymentsList) {
                ContentValues values = new ContentValues();

                values.put(KEY_TRIPSHEET_PAYMENTS_TRIP_ID, paymentsBean.getPayments_tripsheetId());
                values.put(KEY_TRIPSHEET_PAYMENTS_USER_ID, paymentsBean.getPayments_userId());
                values.put(KEY_TRIPSHEET_PAYMENTS_USER_CODES, paymentsBean.getPayments_userCodes());
                values.put(KEY_TRIPSHEET_PAYMENTS_ROUTE_ID, paymentsBean.getPayments_routeId());
                values.put(KEY_TRIPSHEET_PAYMENTS_ROUTE_CODES, paymentsBean.getPayments_routeCodes());

                values.put(KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID, paymentsBean.getPayments_chequeNumber());
                values.put(KEY_TRIPSHEET_PAYMENTS_AC_CA_NO, paymentsBean.getPayments_accountNumber());
                values.put(KEY_TRIPSHEET_PAYMENTS_ACOUNT_NAME, paymentsBean.getPayments_accountName());
                values.put(KEY_TRIPSHEET_PAYMENTS_BANK_NAME, paymentsBean.getPayments_bankName());
                values.put(KEY_TRIPSHEET_PAYMENTS_TRANS_DATE, paymentsBean.getPayments_chequeDate());
                values.put(KEY_TRIPSHEET_PAYMENTS_DATE, paymentsBean.getPayment_date());
                values.put(KEY_TRIPSHEET_PAYMENTS_TRANS_CLEAR_DATE, paymentsBean.getPayments_chequeClearDate());
                values.put(KEY_TRIPSHEET_PAYMENTS_RECEIVER_NAME, paymentsBean.getPayments_receiverName());
                values.put(KEY_TRIPSHEET_PAYMENTS_TRANS_STATUS, paymentsBean.getPayments_transActionStatus());

                values.put(KEY_TRIPSHEET_PAYMENTS_TAX_TOTAL, paymentsBean.getPayments_taxTotal());
                values.put(KEY_TRIPSHEET_PAYMENTS_SALE_VALUE, paymentsBean.getPayments_saleValue());
                values.put(KEY_TRIPSHEET_PAYMENTS_TYPE, paymentsBean.getPayments_type());
                values.put(KEY_TRIPSHEET_PAYMENTS_STATUS, paymentsBean.getPayments_status());
                values.put(KEY_TRIPSHEET_PAYMENTS_DELETE, paymentsBean.getPayments_delete());
                values.put(KEY_TRIPSHEET_PAYMENTS_SO_ID, paymentsBean.getPayments_saleOrderId());
                values.put(KEY_TRIPSHEET_PAYMENTS_SO_CODE, paymentsBean.getPayments_saleOrderCode());
                values.put(KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT, paymentsBean.getPayments_receivedAmount());
                values.put(KEY_TRIPSHEET_PAYMENTS_CHEQUE_PATH, paymentsBean.getPayments_cheque_image_path());
                values.put(KEY_TRIPSHEET_PAYMENTS_CHEQUE_UPLOAD_STATUS, 1);
                values.put(KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER, paymentsBean.getPayments_paymentsNumber());
                values.put(KEY_TRIPSHEET_PAYMENTS_UPLOAD_STATUS, "1");
                values.put(KEY_PAYMENT_FIRST_NAME, paymentsBean.getFirst_name());

                int noOfRecordsExisted = checkProductExistsInTripSheetPaymentsTable(paymentsBean.getPayments_saleOrderId(),
                        paymentsBean.getPayments_userId(), paymentsBean.getPayments_paymentsNumber());
                System.out.println("AGENT PAY RECORD EXISTS++++++++" + noOfRecordsExisted);
                SQLiteDatabase db = this.getWritableDatabase();
                long status;

                if (noOfRecordsExisted == 0) {
                    status = db.insert(TABLE_TRIPSHEETS_PAYMENTS_LIST, null, values);
                    System.out.println("AGENT PAY INSERTED++++++++" + status);
                } else {
                    status = db.update(TABLE_TRIPSHEETS_PAYMENTS_LIST, values, KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " = ? AND "
                                    + KEY_TRIPSHEET_PAYMENTS_SO_ID + " = ? AND " + KEY_TRIPSHEET_PAYMENTS_ROUTE_ID + " = ? AND "
                                    + KEY_TRIPSHEET_PAYMENTS_USER_ID + " = ?",
                            new String[]{paymentsBean.getPayments_tripsheetId(),
                                    paymentsBean.getPayments_saleOrderId(),
                                    paymentsBean.getPayments_routeId(),
                                    paymentsBean.getPayments_userId()});
                    System.out.println("AGENT PAY UPDATED++++++++" + status);
                }

                values.clear();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to insert the mTripsheetsPaymentsList.
     *
     * @param paymentsBean
     */
    public void insertTripsheetsPaymentsListData(PaymentsBean paymentsBean) {
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER, paymentsBean.getPayments_paymentsNumber());
            values.put(KEY_TRIPSHEET_PAYMENTS_TRIP_ID, paymentsBean.getPayments_tripsheetId());
            values.put(KEY_TRIPSHEET_PAYMENTS_USER_ID, paymentsBean.getPayments_userId());
            values.put(KEY_TRIPSHEET_PAYMENTS_USER_CODES, paymentsBean.getPayments_userCodes());
            values.put(KEY_TRIPSHEET_PAYMENTS_ROUTE_ID, paymentsBean.getPayments_routeId());
            values.put(KEY_TRIPSHEET_PAYMENTS_ROUTE_CODES, paymentsBean.getPayments_routeCodes());

            values.put(KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID, paymentsBean.getPayments_chequeNumber());
            values.put(KEY_TRIPSHEET_PAYMENTS_AC_CA_NO, paymentsBean.getPayments_accountNumber());
            values.put(KEY_TRIPSHEET_PAYMENTS_ACOUNT_NAME, paymentsBean.getPayments_accountName());
            values.put(KEY_TRIPSHEET_PAYMENTS_BANK_NAME, paymentsBean.getPayments_bankName());
            values.put(KEY_TRIPSHEET_PAYMENTS_TRANS_DATE, paymentsBean.getPayments_chequeDate());
            values.put(KEY_TRIPSHEET_PAYMENTS_DATE, paymentsBean.getPayment_date());
            values.put(KEY_TRIPSHEET_PAYMENTS_TRANS_CLEAR_DATE, paymentsBean.getPayments_chequeClearDate());
            values.put(KEY_TRIPSHEET_PAYMENTS_RECEIVER_NAME, paymentsBean.getPayments_receiverName());
            values.put(KEY_TRIPSHEET_PAYMENTS_TRANS_STATUS, paymentsBean.getPayments_transActionStatus());

            values.put(KEY_TRIPSHEET_PAYMENTS_TAX_TOTAL, paymentsBean.getPayments_taxTotal());
            values.put(KEY_TRIPSHEET_PAYMENTS_SALE_VALUE, paymentsBean.getPayments_saleValue());
            values.put(KEY_TRIPSHEET_PAYMENTS_TYPE, paymentsBean.getPayments_type());
            values.put(KEY_TRIPSHEET_PAYMENTS_STATUS, paymentsBean.getPayments_status());
            values.put(KEY_TRIPSHEET_PAYMENTS_DELETE, paymentsBean.getPayments_delete());
            values.put(KEY_TRIPSHEET_PAYMENTS_SO_ID, paymentsBean.getPayments_saleOrderId());
            values.put(KEY_TRIPSHEET_PAYMENTS_SO_CODE, paymentsBean.getPayments_saleOrderCode());
            values.put(KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT, paymentsBean.getPayments_receivedAmount());
            values.put(KEY_TRIPSHEET_PAYMENTS_CHEQUE_PATH, paymentsBean.getPayments_cheque_image_path());
            values.put(KEY_TRIPSHEET_PAYMENTS_CHEQUE_UPLOAD_STATUS, paymentsBean.getPayments_cheque_upload_status());
            values.put(KEY_PAYMENT_FIRST_NAME, paymentsBean.getFirst_name());

            //int noOfRecordsExisted = checkTripsheetPaymentsRecordExistsOrNot(paymentsBean.getPayments_tripsheetId(), paymentsBean.getPayments_saleOrderId());

            SQLiteDatabase db = this.getWritableDatabase();

            long insertedId = db.insert(TABLE_TRIPSHEETS_PAYMENTS_LIST, null, values);

        /*if (noOfRecordsExisted == 0) {
            db.insert(TABLE_TRIPSHEETS_PAYMENTS_LIST, null, values);
        } else {
            db.update(TABLE_TRIPSHEETS_PAYMENTS_LIST, values, KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " = ? AND " + KEY_TRIPSHEET_PAYMENTS_SO_ID + " = ? ", new String[]{paymentsBean.getPayments_tripsheetId(), paymentsBean.getPayments_saleOrderId()});
        }*/

            values.clear();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to fetch all tripsheets payments list baed on tripsheet id from Tripsheets payments list table
     */
    public ArrayList<PaymentsBean> fetchAllTripsheetsPaymentsList(String tripId) {
        ArrayList<PaymentsBean> alltripsheetsPayments = new ArrayList<PaymentsBean>();

        try {
            String selectQuery = "";
            if (tripId.equals("")) {
                selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_UPLOAD_STATUS + " = 0";
            } else {
                selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_UPLOAD_STATUS + " = 0"
                        + " AND " + KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " = '" + tripId + "'";
            }

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    PaymentsBean tripPaymentsBean = new PaymentsBean();
                    tripPaymentsBean.setPayments_paymentsNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_PAYMENT_NO)));
                    tripPaymentsBean.setPayments_paymentsNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER)));

                    tripPaymentsBean.setPayments_tripsheetId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRIP_ID)));
                    tripPaymentsBean.setPayments_userId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_USER_ID)));
                    tripPaymentsBean.setPayments_userCodes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_USER_CODES)));
                    tripPaymentsBean.setPayments_routeId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_ROUTE_ID)));
                    tripPaymentsBean.setPayments_routeCodes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_ROUTE_CODES)));

                    tripPaymentsBean.setPayments_chequeNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID)));
                    tripPaymentsBean.setPayments_accountNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_AC_CA_NO)));
                    tripPaymentsBean.setPayments_accountName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_ACOUNT_NAME)));
                    tripPaymentsBean.setPayments_bankName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_BANK_NAME)));
                    tripPaymentsBean.setPayments_chequeDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_DATE)));
                    tripPaymentsBean.setPayment_date(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_DATE)));
                    tripPaymentsBean.setPayments_chequeClearDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_CLEAR_DATE)));
                    tripPaymentsBean.setPayments_cheque_image_path(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CHEQUE_PATH)));
                    tripPaymentsBean.setPayments_receiverName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_RECEIVER_NAME)));
                    tripPaymentsBean.setPayments_transActionStatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_STATUS)));

                    tripPaymentsBean.setPayments_taxTotal(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TAX_TOTAL))));
                    tripPaymentsBean.setPayments_saleValue(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_SALE_VALUE))));
                    tripPaymentsBean.setPayments_type(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TYPE)));
                    tripPaymentsBean.setPayments_status(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_STATUS)));
                    tripPaymentsBean.setPayments_delete(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_DELETE)));
                    tripPaymentsBean.setPayments_saleOrderId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_SO_ID)));
                    tripPaymentsBean.setPayments_saleOrderCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_SO_CODE)));
                    tripPaymentsBean.setPayments_receivedAmount(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT))));
                    tripPaymentsBean.setPayments_cheque_upload_status(c.getInt(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CHEQUE_UPLOAD_STATUS)));
                    tripPaymentsBean.setFirst_name(c.getString(c.getColumnIndex(KEY_PAYMENT_FIRST_NAME)));

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
                values.put(KEY_TRIPSHEET_SO_PRODUCTS_COUNT, mTripsheetsList.get(i).getmTripshetSOProductsCount());
                values.put(KEY_TRIPSHEET_SO_CANS_DUE, mTripsheetsList.get(i).getmTripshetSOCansDue());
                values.put(KEY_TRIPSHEET_SO_CRATES_DUE, mTripsheetsList.get(i).getmTripshetSOCratesDue());
                values.put(KEY_TRIPSHEET_SO_ITEM_TYPE, mTripsheetsList.get(i).getmTripsheetSOitemType());
                values.put(KEY_TRIPSHEET_SO_UOM, mTripsheetsList.get(i).getmTripshetSOuom());
                values.put(KEY_TRIPSHEET_SO_UNIT_PRICE, mTripsheetsList.get(i).getmTripshetSOUnitprice());

                int noOfRecords = checkTripsheetSOExistsOrNot(mTripsheetsList.get(i).getmTripshetSOTripId(), mTripsheetsList.get(i).getmTripshetSOId());

                SQLiteDatabase db = this.getWritableDatabase();

                if (noOfRecords == 0) {
                    db.insert(TABLE_TRIPSHEETS_SO_LIST, null, values);
                    System.out.println("*********** SALE ORDER RECORD INSERTED***************");
                } else {
                    db.update(TABLE_TRIPSHEETS_SO_LIST, values, KEY_TRIPSHEET_SO_TRIPID + " = ? AND " + KEY_TRIPSHEET_SO_ID + " = ?", new String[]{mTripsheetsList.get(i).getmTripshetSOTripId(), mTripsheetsList.get(i).getmTripshetSOId()});
                    System.out.println("*********** SALE ORDER RECORD UPDATED***************");
                }

                values.clear();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProductsBean fetchProductDetailsByProductCode(String productCode) {
        ProductsBean productsBean = null;

        try {
            String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_CODE + " = " + productCode;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    productsBean = new ProductsBean();
                    productsBean.setProductId((c.getString(c.getColumnIndex(KEY_PRODUCT_ID))));
                    productsBean.setProductTitle((c.getString(c.getColumnIndex(KEY_PRODUCT_TITLE))));
                    productsBean.setProductAgentPrice((c.getString(c.getColumnIndex(KEY_PRODUCT_AGENT_PRICE))));
                    productsBean.setProductgst((c.getString(c.getColumnIndex(KEY_PRODUCT_GST_PRICE))));
                    productsBean.setProductvat((c.getString(c.getColumnIndex(KEY_PRODUCT_VAT_PRICE))));
                    productsBean.setProductReturnable((c.getString(c.getColumnIndex(KEY_PRODUCT_RETURNABLE))));
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
            values.put(KEY_TRIPSHEET_STOCK_DISPATCHED_UPLOAD_STATUS, 0);

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
            values.put(KEY_TRIPSHEET_STOCK_VERIFIED_UPLOAD_STATUS, 0);

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

                    tripStockBean.setmDeliveryQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_DELIVERY_QUANTITY)));
                    tripStockBean.setmRouteReturnQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_ROUTE_RETURN_QUANTITY)));
                    tripStockBean.setmCBQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_CB_QUANTITY)));
                    tripStockBean.setmOtherQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_OTHER_QUANTITY)));
                    tripStockBean.setmLeakQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_LEAK_QUANTITY)));

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
                    productsBean.setProductUom(c.getString(c.getColumnIndex(KEY_PRODUCT_UOM)));
                    // productsBean.setProductType("S");
                    String inStQ = c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_IN_STOCK_QUANTITY));
                    Log.i("InStock", inStQ + "@@@");
                    if (inStQ != null) {
                        productsBean.setProductStock(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_IN_STOCK_QUANTITY))));
                    } else {
                        productsBean.setProductStock(0.0);
                    }
                    String extraStQ = c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_EXTRA_QUANTITY));
                    if (extraStQ != null) {
                        productsBean.setProductExtraQuantity(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STOCK_EXTRA_QUANTITY))));
                    } else {
                        productsBean.setProductExtraQuantity(0.0);
                    }
                    productsBean.setProductReturnableUnit(c.getString(c.getColumnIndex(KEY_PRODUCT_RETURNABLE)));

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

    public List<String> getAgentRouteId(String agentId) {
        List<String> routeIdsList = new ArrayList<>();

        try {
            String selectQuery = "SELECT " + KEY_AGENT_ROUTE_ID + " FROM " + TABLE_AGENTS + " WHERE " + KEY_AGENT_ID + "='" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    String routeId = c.getString(c.getColumnIndex(KEY_AGENT_ROUTE_ID));
                    routeId = routeId.replace("[\"", "").replace("\"]", "");
                    routeIdsList = new ArrayList<>(Arrays.asList(routeId.split(",")));
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return routeIdsList;
    }

    public int checkProductExistsInTripSheetDeliveryTable(String saleOrderId, String agentId, String productId) {
        int noOfRecords = 0;

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_DELIVERY_NO + " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = '" + agentId + "' AND " + KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS + " = '" + productId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                noOfRecords = cursor.getCount();
                cursor.close();
            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return noOfRecords;
    }

    public void updateProductStockInTripSheetStockTable(String tripSheetId, String productCode, String inStockQuantity, String extraQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TRIPSHEET_STOCK_IN_STOCK_QUANTITY, inStockQuantity);
            values.put(KEY_TRIPSHEET_STOCK_EXTRA_QUANTITY, extraQuantity);

            int status = db.update(TABLE_TRIPSHEETS_STOCK_LIST, values, KEY_TRIPSHEET_STOCK_TRIPSHEET_ID + " = ? AND " + KEY_TRIPSHEET_STOCK_PRODUCT_CODE + " = ?", new String[]{tripSheetId, productCode});

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to check weather the tripsheet Sale order exists or not using tripsheetid and so id.
     *
     * @param tripsheetId
     * @return integer value
     */
    public int checkTripsheetSOExistsOrNot(String tripsheetId, String saleOrderId) {
        int noOfRecords = 0;

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_SO_UNIQUE_ID + " FROM " + TABLE_TRIPSHEETS_SO_LIST + " WHERE " + KEY_TRIPSHEET_SO_TRIPID + " = '" + tripsheetId + "' AND " + KEY_TRIPSHEET_SO_ID + " = '" + saleOrderId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null) {
                cursor.moveToFirst();
                noOfRecords = cursor.getCount();
                cursor.close();
            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return noOfRecords;
    }

    /**
     * Method to update tripsheet payments upload status 1 after updating to the server.
     */
    public void updateTripsheetsPaymentsUploadStatus(String paymentNumber, String tripSheetId, String saleOrderId, String paymentNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            System.out.println("UPDATE PAYMENT NUM:: " + paymentNumber);
            ContentValues values = new ContentValues();
            values.put(KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER, paymentNumber);
            values.put(KEY_TRIPSHEET_PAYMENTS_UPLOAD_STATUS, "1");

            int status = db.update(TABLE_TRIPSHEETS_PAYMENTS_LIST, values, KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " = ? AND " + KEY_TRIPSHEET_PAYMENTS_SO_ID + " = ? AND " + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NO + " = ?", new String[]{tripSheetId, saleOrderId, paymentNo});

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    public ArrayList<String> fetchUnUploadedUniqueDeliveryTripSheetIds(String tripId) {
        ArrayList<String> tripSheetIds = new ArrayList<>();

        try {
            String selectQuery = "";
            if (tripId.equals("")) {
                selectQuery = "SELECT DISTINCT " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_UPLOAD_STATUS + " = 0";
            } else {
                selectQuery = "SELECT DISTINCT " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_UPLOAD_STATUS + " = 0"
                        + " AND " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = '" + tripId + "'";
            }

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    tripSheetIds.add(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TRIP_ID)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripSheetIds;
    }

    public void updateTripSheetDeliveriesTable(String deliveryNumber, String tripSheetId, String saleOrderId, String agentId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            //values.put(KEY_TRIPSHEET_DELIVERY_NUMBER, deliveryNumber);
            values.put(KEY_TRIPSHEET_DELIVERY_UPLOAD_STATUS, 1);

            int status = db.update(TABLE_TRIPSHEETS_DELIVERIES_LIST, values, KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = ? AND " + KEY_TRIPSHEET_DELIVERY_SO_ID + " = ? AND " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = ?", new String[]{tripSheetId, saleOrderId, agentId});

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }


    public int checkProductExistsInTripSheetReturnsTable(String saleOrderId, String agentId, String productId) {
        int noOfRecords = 0;

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_RETURNS_RETURN_NO + " FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_RETURNS_USER_ID + " = '" + agentId + "' AND " + KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS + " = '" + productId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null) {
                cursor.moveToFirst();
                noOfRecords = cursor.getCount();
                cursor.close();
            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return noOfRecords;
    }

    public ArrayList<String> fetchUnUploadedUniqueReturnsTripSheetIds(String tripId) {
        ArrayList<String> tripSheetIds = new ArrayList<>();

        try {
            String selectQuery = "";
            if (tripId.equals("")) {
                selectQuery = "SELECT DISTINCT " + KEY_TRIPSHEET_RETURNS_TRIP_ID + " FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_UPLOAD_STATUS + " = 0";
            } else {
                selectQuery = "SELECT DISTINCT " + KEY_TRIPSHEET_RETURNS_TRIP_ID + " FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_UPLOAD_STATUS + " = 0"
                        + " AND " + KEY_TRIPSHEET_RETURNS_TRIP_ID + " = '" + tripId + "'";
            }

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    tripSheetIds.add(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_TRIP_ID)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripSheetIds;
    }


    public void updateTripSheetReturnsTable(String returnNumber, String tripSheetId, String saleOrderId, String agentId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            //values.put(KEY_TRIPSHEET_RETURNS_RETURN_NUMBER, returnNumber);
            values.put(KEY_TRIPSHEET_RETURNS_UPLOAD_STATUS, 1);

            int status = db.update(TABLE_TRIPSHEETS_RETURNS_LIST, values, KEY_TRIPSHEET_RETURNS_TRIP_ID + " = ? AND " + KEY_TRIPSHEET_RETURNS_SO_ID + " = ? AND " + KEY_TRIPSHEET_RETURNS_USER_ID + " = ?", new String[]{tripSheetId, saleOrderId, agentId});

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }


    /**
     * Method to check weather the tripsheet payments record exists or not using tripsheetid.
     *
     * @param tripsheetId
     * @return integer value
     */
    public int checkTripsheetPaymentsRecordExistsOrNot(String tripsheetId, String saleOrderId) {
        int noOfRecords = 0;

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NO + " FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " = '" + tripsheetId + "' AND " + KEY_TRIPSHEET_PAYMENTS_SO_ID + " = '" + saleOrderId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null) {
                cursor.moveToFirst();
                noOfRecords = cursor.getCount();
                cursor.close();
            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return noOfRecords;
    }

    public Map<String, String> fetchDeliveriesListByTripSheetId(String tripsheetId, String saleOrderId, String agentId) {
        Map<String, String> tripsheetsDeliveries = new HashMap<>();

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS + ", " + KEY_TRIPSHEET_DELIVERY_QUANTITY + " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = " + "'" + tripsheetId + "' AND " + KEY_TRIPSHEET_DELIVERY_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = '" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    tripsheetsDeliveries.put(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)), c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_QUANTITY)));
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripsheetsDeliveries;
    }

    public ArrayList<String> getAgentOrderedProductsQuantityFromSaleOrderTable(String tripsheetId, String saleOrderId, String agentId) {
        ArrayList<String> productOrderQuantities = new ArrayList<>();

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_SO_PRODUCTCODE + ", " + KEY_TRIPSHEET_SO_PRODUCTORDER_QUANTITY + ", " + KEY_TRIPSHEET_SO_ITEM_TYPE + ", " + KEY_TRIPSHEET_SO_UOM + "," + KEY_TRIPSHEET_SO_UNIT_PRICE + " FROM " + TABLE_TRIPSHEETS_SO_LIST + " WHERE " + KEY_TRIPSHEET_SO_TRIPID + " = '" + tripsheetId + "' AND " + KEY_TRIPSHEET_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_SO_AGENTID + " = '" + agentId + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {

                productOrderQuantities.add(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_PRODUCTCODE)));
                productOrderQuantities.add(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_PRODUCTORDER_QUANTITY)));
                productOrderQuantities.add(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_ITEM_TYPE)));
                productOrderQuantities.add(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_UOM)));
                productOrderQuantities.add(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_UNIT_PRICE)));
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return productOrderQuantities;
    }

    public Map<String, String> getAgentPreviouslyDeliveredProductsList(String tripSheetId, String saleOrderId, String agentId) {
        Map<String, String> deliveredProductsList = new HashMap<>();

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS + ", " + KEY_TRIPSHEET_DELIVERY_QUANTITY + " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = '" + tripSheetId + "' AND " + KEY_TRIPSHEET_DELIVERY_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = '" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    deliveredProductsList.put(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)), c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_QUANTITY)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return deliveredProductsList;
    }

    public Map<String, String> getAgentPreviouslyReturnsProductsList(String tripSheetId, String saleOrderId, String agentId) {
        Map<String, String> allReturnsProductsList = new HashMap<>();

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS + ", " + KEY_TRIPSHEET_RETURNS_QUANTITY + " FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_TRIP_ID + " = '" + tripSheetId + "' AND " + KEY_TRIPSHEET_RETURNS_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_RETURNS_USER_ID + " = '" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    allReturnsProductsList.put(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS)), c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_QUANTITY)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allReturnsProductsList;
    }

    public ArrayList<TripSheetDeliveriesBean> fetchOrderPaymentFromDeliveriesTable(String tripSheetId, String saleOrderId, String agentId) {
        ArrayList<TripSheetDeliveriesBean> allTripSheetsDeliveries = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = '" + tripSheetId + "' AND " + KEY_TRIPSHEET_DELIVERY_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = '" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripSheetDeliveriesBean tripDeliveriesBean = new TripSheetDeliveriesBean();
                    tripDeliveriesBean.setmTripsheetDelivery_TaxPercent(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXPERCENT)));
                    tripDeliveriesBean.setmTripsheetDelivery_UnitPrice(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_UNITPRICE)));
                    tripDeliveriesBean.setmTripsheetDelivery_Amount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_AMOUNT)));
                    tripDeliveriesBean.setmTripsheetDelivery_TaxAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXAMOUNT)));
                    tripDeliveriesBean.setmTripsheetDelivery_TaxTotal(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXTOTAL)));
                    tripDeliveriesBean.setmTripsheetDelivery_SaleValue(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SALEVALUE)));

                    allTripSheetsDeliveries.add(tripDeliveriesBean);

                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allTripSheetsDeliveries;
    }

    public double fetchTripSheetReceivedAmount(String tripSheetId) {
        double receivedAmount = 0.0;

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT + " FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " = '" + tripSheetId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    receivedAmount = receivedAmount + Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return receivedAmount;
    }


    public String fetchTripSheetSaleorderNo(String tripSheetId) {
        String no = "";

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_SO_ID + " FROM " + TABLE_TRIPSHEETS_SO_LIST + " WHERE " + KEY_TRIPSHEET_SO_TRIPID + " = '" + tripSheetId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    no = (c.getString(c.getColumnIndex(KEY_TRIPSHEET_SO_ID)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return no;
    }


    public String fetchTripSheetagentid(String tripSheetId) {
        String no = "";

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_SO_AGENTID + " FROM " + TABLE_TRIPSHEETS_SO_LIST + " WHERE " + KEY_TRIPSHEET_SO_TRIPID + " = '" + tripSheetId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    no = (c.getString(c.getColumnIndex(KEY_TRIPSHEET_SO_AGENTID)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return no;
    }

    public ArrayList<TripsheetSOList> getTripSheetSaleOrderDetails(String tripSheetId) {
        ArrayList<TripsheetSOList> saleOrdersList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_SO_LIST + " WHERE " + KEY_TRIPSHEET_SO_TRIPID + " = '" + tripSheetId + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    TripsheetSOList saleOrderBean = new TripsheetSOList();

                    saleOrderBean.setmTripshetSOId(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_ID)));
                    saleOrderBean.setmTripshetSOTripId(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_TRIPID)));
                    saleOrderBean.setmTripshetSOAgentCode(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_AGENTCODE)));
                    saleOrderBean.setmTripshetSOCode(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_CODE)));
                    saleOrderBean.setmTripshetSODate(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_DATE)));
                    saleOrderBean.setmTripshetSOValue(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_VALUE)));
                    saleOrderBean.setmTripshetSOOpAmount(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_OPAMOUNT)));
                    saleOrderBean.setmTripshetSOCBAmount(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_CBAMOUNT)));
                    saleOrderBean.setmTripshetSOAgentId(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_AGENTID)));
                    saleOrderBean.setmTripshetSOAgentFirstName(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_AGENTFIRSTNAME)));
                    saleOrderBean.setmTripshetSOAgentLastName(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_AGENTLASTNAME)));
                    saleOrderBean.setmTripshetSOProductCode(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_PRODUCTCODE)));
                    saleOrderBean.setmTripshetSOProductOrderQuantity(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_PRODUCTORDER_QUANTITY)));
                    saleOrderBean.setmTripshetSOProductValue(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_PRODUCT_VALUE)));
                    saleOrderBean.setmTripshetSOApprovedBy(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_APPROVEDBY)));
                    saleOrderBean.setmTripshetSOAgentLatitude(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_AGENTLATITUDE)));
                    saleOrderBean.setmTripshetSOAgentLongitude(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_AGENTLONGITUDE)));
                    saleOrderBean.setmTripshetSOProductsCount(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_PRODUCTS_COUNT)));
                    saleOrderBean.setmTripsheetSOitemType(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_ITEM_TYPE)));
                    saleOrderBean.setmTripshetSOuom(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_UOM)));
                    saleOrderBean.setmTripshetSOUnitprice(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_UNIT_PRICE)));
                    double saleOrderValue = fetchTripSheetSaleOrderValueForAgentId(tripSheetId, cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_ID)), cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_AGENTID)));

                    if (saleOrderValue > 0)
                        saleOrderBean.setmTripshetSOValue(String.valueOf(saleOrderValue));

                    double receivedAmount = fetchTripSheetSaleOrderReceivedAmount(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_TRIPID)), cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_ID)));
                    double dueAmount = (Double.parseDouble(saleOrderBean.getmTripshetSOOpAmount()) + Double.parseDouble(saleOrderBean.getmTripshetSOValue())) - receivedAmount;

                    saleOrderBean.setmTripshetSOReceivedAmount(String.valueOf(receivedAmount));
                    saleOrderBean.setmTripshetSODueAmount(String.valueOf(dueAmount));

                    saleOrdersList.add(saleOrderBean);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return saleOrdersList;
    }

    public double fetchTripSheetSaleOrderReceivedAmount(String tripSheetId, String saleOrderId) {
        double receivedAmount = 0.0;
        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT + " FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " = '" + tripSheetId + "' AND " + KEY_TRIPSHEET_PAYMENTS_SO_ID + " = '" + saleOrderId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    receivedAmount = receivedAmount + Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT)));
                } while (c.moveToNext());
            } else {
                receivedAmount = -0.00000001;
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return receivedAmount;
    }

    /**
     * Method to get the count of existing deliveries.
     *
     * @return deliveries count
     */
    public String getTripsheetDeliveriesMaxOrderNumber() {
        String orderId = "";

        try {
            String countQuery = "SELECT " + KEY_TRIPSHEET_DELIVERY_NUMBER + " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " ORDER BY " + KEY_TRIPSHEET_DELIVERY_NUMBER + " DESC LIMIT 1";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            if (cursor.moveToFirst()) {
                orderId = cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NUMBER));
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderId;
    }

    public TripsheetsList fetchTripSheetDetails(String tripSheetId) {
        TripsheetsList tripSheetDetails = new TripsheetsList();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_LIST + " WHERE " + KEY_TRIPSHEET_ID + " = '" + tripSheetId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                tripSheetDetails.setmTripshhetId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_ID)));
                tripSheetDetails.setmTripshhetCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_CODE)));
                tripSheetDetails.setmTripshhetDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DATE)));
                tripSheetDetails.setmTripshhetStatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_STATUS)));
                tripSheetDetails.setmTripshhetOBAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_OB_AMOUNT)));
                tripSheetDetails.setmTripshhetOrderedAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_ORDERED_AMOUNT)));
                tripSheetDetails.setmTripshhetRouteCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_ROUTE_CODE)));
                tripSheetDetails.setmTripshhetSalesMenCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_SALESMEN_CODE)));
                tripSheetDetails.setmTripshhetVehicleNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_VEHICLE_NUMBER)));
                tripSheetDetails.setmTripshhetTrasnsporterName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_TRANSPORTER_NAME)));
                tripSheetDetails.setmTripshhetVerifyStatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_VERIFY_STATUS)));
                tripSheetDetails.setIsTripshhetClosed(c.getInt(c.getColumnIndex(KEY_TRIPSHEET_IS_TRIP_SHEET_CLOSED)));
                tripSheetDetails.setApproved_by(c.getString(c.getColumnIndex(KEY_TRIPSHEET_APPROVED_BY)));

                double receivedAmount = fetchTripSheetReceivedAmount(tripSheetDetails.getmTripshhetId());
                receivedAmount = Double.parseDouble(String.valueOf(receivedAmount).replace(",", ""));
                double dueAmount = (Double.parseDouble(tripSheetDetails.getmTripshhetOBAmount().replace(",", "")) + Double.parseDouble(tripSheetDetails.getmTripshhetOrderedAmount().replace(",", ""))) - receivedAmount;

                tripSheetDetails.setmTripshhetReceivedAmount(String.valueOf(receivedAmount));
                tripSheetDetails.setmTripshhetDueAmount(String.valueOf(dueAmount));

                // CASH
                double cashVal = 0, cheqVal = 0;
                ArrayList<String> paymentTypeArray = fetchTripSheetReceivedAmounttypeCash(tripSheetDetails.getmTripshhetId());
                if (paymentTypeArray.size() > 0) {
                    for (int b = 0; b < paymentTypeArray.size(); b++) {
                        cashVal = cashVal + Double.parseDouble(paymentTypeArray.get(b).toString());
                    }
                    tripSheetDetails.setmCashPayment(String.valueOf(cashVal));
                } else {
                    tripSheetDetails.setmCashPayment("0");
                }
                // CHEQUE
                ArrayList<String> paymentTypeArraych = fetchTripSheetReceivedAmounttypeCheque(tripSheetDetails.getmTripshhetId());
                if (paymentTypeArraych.size() > 0) {
                    for (int b = 0; b < paymentTypeArraych.size(); b++) {
                        cheqVal = cheqVal + Double.parseDouble(paymentTypeArraych.get(b).toString());
                    }
                    tripSheetDetails.setmChequePayment(String.valueOf(cheqVal));
                } else {
                    tripSheetDetails.setmChequePayment("0");
                }
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripSheetDetails;
    }

    public double fetchTripSheetSaleOrderValueForAgentId(String tripSheetId, String saleOrderId, String agentId) {
        double saleOrderValue = 0.0;

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_DELIVERY_SALEVALUE + " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = '" + tripSheetId + "' AND " + KEY_TRIPSHEET_DELIVERY_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = '" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                saleOrderValue = Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SALEVALUE)));
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return saleOrderValue;
    }

    public double fetchTripSheetSaleOrderOpeningBalance(String tripSheetId, String saleOrderId) {
        double openingBalance = 0.0;

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_SO_OPAMOUNT + " FROM " + TABLE_TRIPSHEETS_SO_LIST + " WHERE " + KEY_TRIPSHEET_SO_TRIPID + " = '" + tripSheetId + "' AND " + KEY_TRIPSHEET_SO_ID + " = '" + saleOrderId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                openingBalance = Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_OPAMOUNT)));
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return openingBalance;
    }

    public TripsheetSOList fetchTripSheetSaleOrderDetails(String tripSheetId, String saleOrderId, String agentId) {
        TripsheetSOList saleOrdersDetails = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_SO_LIST + " WHERE " + KEY_TRIPSHEET_SO_TRIPID + " = '" + tripSheetId + "' AND " + KEY_TRIPSHEET_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_SO_AGENTID + " = '" + agentId + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    saleOrdersDetails = new TripsheetSOList();
                    saleOrdersDetails.setmTripshetSOCode(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_CODE)));
                    saleOrdersDetails.setmTripshetSODate(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_DATE)));
                    //saleOrdersDetails.setmTripshetSOValue(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_VALUE)));
                    saleOrdersDetails.setmTripshetSOOpAmount(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_OPAMOUNT)));
                    //saleOrdersDetails.setmTripshetSOCBAmount(cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_SO_CBAMOUNT)));

                    double saleOrderValue = fetchTripSheetSaleOrderValueForAgentId(tripSheetId, saleOrderId, agentId);
                    double receivedAmount = fetchTripSheetSaleOrderReceivedAmount(tripSheetId, saleOrderId);
                    double dueAmount = (Double.parseDouble(saleOrdersDetails.getmTripshetSOOpAmount()) + saleOrderValue) - receivedAmount;

                    saleOrdersDetails.setmTripshetSOValue(String.valueOf(saleOrderValue));
                    saleOrdersDetails.setmTripshetSOReceivedAmount(String.valueOf(receivedAmount));
                    saleOrdersDetails.setmTripshetSODueAmount(String.valueOf(dueAmount));
                    saleOrdersDetails.setmTripshetSOCBAmount(String.valueOf(dueAmount));

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return saleOrdersDetails;
    }

    public ArrayList<SaleOrderDeliveredProducts> getDeliveredProductsListForSaleOrder(String tripSheetId, String saleOrderId, String agentId) {
        ArrayList<SaleOrderDeliveredProducts> deliveredProductsList = new ArrayList<>();

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_DELIVERY_NO + ", " + KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS + ", " + KEY_PRODUCT_TITLE + ", " + KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES + ", "
                    + KEY_TRIPSHEET_DELIVERY_QUANTITY + ", " + KEY_TRIPSHEET_DELIVERY_UNITPRICE + ", " + KEY_TRIPSHEET_DELIVERY_TAXAMOUNT + ", " + KEY_TRIPSHEET_DELIVERY_AMOUNT + ", "
                    + KEY_TRIPSHEET_DELIVERY_TAXTOTAL + ", " + KEY_TRIPSHEET_DELIVERY_SALEVALUE + ", " + KEY_TRIPSHEET_DELIVERY_CREATEDON + ", " + KEY_PRODUCT_RETURNABLE + ", " + KEY_TRIPSHEET_DELIVERY_NUMBER
                    + " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " D LEFT JOIN " + TABLE_PRODUCTS + " P ON D." + KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES + " = P." + KEY_PRODUCT_CODE
                    + " WHERE " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = '" + tripSheetId + "' AND " + KEY_TRIPSHEET_DELIVERY_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = '" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    SaleOrderDeliveredProducts deliveredProduct = new SaleOrderDeliveredProducts();
                    deliveredProduct.setDeliveryNo(c.getInt(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NO)));
                    deliveredProduct.setId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)));
                    deliveredProduct.setName(c.getString(c.getColumnIndex(KEY_PRODUCT_TITLE)));
                    deliveredProduct.setCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES)));
                    deliveredProduct.setQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_QUANTITY)));
                    deliveredProduct.setUnitRate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_UNITPRICE)));
                    deliveredProduct.setProductTax(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXAMOUNT)));
                    deliveredProduct.setProductAmount(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_AMOUNT)));
                    deliveredProduct.setTotalTax(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXTOTAL)));
                    deliveredProduct.setSubTotal(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SALEVALUE)));
                    deliveredProduct.setCreatedTime(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDON)));
                    deliveredProduct.setProductReturnable(c.getString(c.getColumnIndex(KEY_PRODUCT_RETURNABLE)));
                    deliveredProduct.setDeliveryNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NUMBER)));

                    if (Double.parseDouble(deliveredProduct.getQuantity()) > 0)
                        deliveredProductsList.add(deliveredProduct);

                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return deliveredProductsList;
    }

    public ArrayList<SaleOrderReturnedProducts> getReturnsProductsListForSaleOrder(String tripSheetId, String saleOrderId, String agentId) {
        ArrayList<SaleOrderReturnedProducts> returnedProductsList = new ArrayList<>();

        try {
            String obamount = "0.0";
            Map<String, String> deliveredProductsHashMap = fetchDeliveriesListByTripSheetId(tripSheetId, saleOrderId, agentId);

            String selectQuery = "SELECT " + KEY_TRIPSHEET_RETURNS_RETURN_NO + ", " + KEY_TRIPSHEET_RETURNS_CREATED_ON + ", " + KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS + ", " + KEY_PRODUCT_TITLE + ", " + KEY_TRIPSHEET_RETURNS_PRODUCT_CODES + ", " + KEY_TRIPSHEET_RETURNS_QUANTITY + ", " + KEY_PRODUCT_RETURNABLE
                    + " FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " R LEFT JOIN " + TABLE_PRODUCTS + " P ON R." + KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS + " = P." + KEY_PRODUCT_ID
                    + " WHERE P." + KEY_PRODUCT_RETURNABLE + " = 'Y' AND " + KEY_TRIPSHEET_RETURNS_TRIP_ID + " = '" + tripSheetId + "' AND " + KEY_TRIPSHEET_RETURNS_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_RETURNS_USER_ID + " = '" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    SaleOrderReturnedProducts returnedProduct = new SaleOrderReturnedProducts();
                    returnedProduct.setReturnno(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_RETURN_NO)));
                    returnedProduct.setReturndate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_CREATED_ON)));
                    returnedProduct.setId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS)));
                    returnedProduct.setName(c.getString(c.getColumnIndex(KEY_PRODUCT_TITLE)));
                    returnedProduct.setCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES)));
                    String cc = c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES));
                    if (cc.equals("2600005")) {
                        obamount = fetchCansorCratesDueByIds(tripSheetId, saleOrderId, agentId, cc, "crates");
                        // CRATES DUE
                        returnedProduct.setOpeningBalance(obamount);
                    } else if (cc.equals("2600006")) {
                        obamount = fetchCansorCratesDueByIds(tripSheetId, saleOrderId, agentId, cc, "cans");
                        // CANS DUE
                        returnedProduct.setOpeningBalance(obamount);
                    } else {
                        returnedProduct.setOpeningBalance(obamount);
                    }
                    returnedProduct.setReturned(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_QUANTITY)));
                    returnedProduct.setDelivered(deliveredProductsHashMap.get(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS))));

                    double closingBalance = Double.parseDouble(returnedProduct.getOpeningBalance()) + Double.parseDouble(returnedProduct.getDelivered()) - Double.parseDouble(returnedProduct.getReturned());
                    returnedProduct.setClosingBalance(String.valueOf(closingBalance));

                    returnedProductsList.add(returnedProduct);

                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedProductsList;
    }


  /*  public AgentPaymentsBean getAgentPaymentDetails(String tripSheetId, String saleOrderId,String aId) {
        AgentPaymentsBean paymentsBean = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " = '" + tripSheetId + "' AND " + KEY_TRIPSHEET_PAYMENTS_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_PAYMENTS_USER_ID + " = '" + aId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    paymentsBean = new AgentPaymentsBean();
                    paymentsBean.setPayment_mop(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TYPE)));
                    paymentsBean.setPayment_checkno(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID)));
                    paymentsBean.setPayment_checkDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_DATE)));
                    paymentsBean.setPayment_bankName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_BANK_NAME)));

                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return paymentsBean;
    }*/


    public PaymentsBean getSaleOrderPaymentDetails(String tripSheetId, String saleOrderId) {
        PaymentsBean paymentsBean = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " = '" + tripSheetId + "' AND " + KEY_TRIPSHEET_PAYMENTS_SO_ID + " = '" + saleOrderId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    paymentsBean = new PaymentsBean();
                    paymentsBean.setPayments_type(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TYPE)));
                    paymentsBean.setPayments_chequeNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID)));
                    paymentsBean.setPayments_chequeDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_DATE)));
                    paymentsBean.setPayments_bankName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_BANK_NAME)));

                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return paymentsBean;
    }

    public int updateTripSheetClosingStatus(String tripSheetId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TRIPSHEET_IS_TRIP_SHEET_CLOSED, 1);

            status = db.update(TABLE_TRIPSHEETS_LIST, values, KEY_TRIPSHEET_ID + " = ?", new String[]{tripSheetId});

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();

        return status;
    }

    public boolean isTripSheetClosed(String tripSheetId) {
        boolean isTripSheetClosed = false;

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_IS_TRIP_SHEET_CLOSED + " FROM " + TABLE_TRIPSHEETS_LIST + " WHERE " + KEY_TRIPSHEET_ID + " = '" + tripSheetId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                isTripSheetClosed = (cursor.getInt(cursor.getColumnIndex(KEY_TRIPSHEET_IS_TRIP_SHEET_CLOSED)) == 0) ? false : true;
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isTripSheetClosed;
    }

    public String getDeliveryName(String userId) {
        String username = "";

        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_USERDETAILS + " WHERE " + KEY_USER_ID + " = '" + userId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                username = (cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return username;
    }

    public String getProductName(String productId, String type) {
        String productname = "";

        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_ID + " = '" + productId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                productname = (cursor.getString(cursor.getColumnIndex(type)));
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return productname;
    }

    public double getSoDetails(String agentId, String type) {
        double obamount = 0;

        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_TRIPSHEETS_SO_LIST + " WHERE " + KEY_TRIPSHEET_SO_AGENTID + " = '" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                obamount = Double.parseDouble((cursor.getString(cursor.getColumnIndex(type))));
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obamount;
    }

    public double getReceivedAmountDetails(String agentId, String type) {
        double receivedAmount = 0;

        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_USER_ID + " = '" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                receivedAmount = Double.parseDouble((cursor.getString(cursor.getColumnIndex(type))));
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return receivedAmount;
    }

    public ArrayList<String> getDeliverynumber(String agentId, String type) {
        ArrayList<String> deliverynumber = new ArrayList<>();

        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = '" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                deliverynumber.add((cursor.getString(cursor.getColumnIndex(type))));
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return deliverynumber;
    }


    public ArrayList<String> getTripId(String agentId, String type) {
        ArrayList<String> tripid = new ArrayList<>();

        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = '" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                tripid.add((cursor.getString(cursor.getColumnIndex(type))));
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripid;
    }

    public ArrayList<String> getReturnnumber(String agentId, String type) {
        ArrayList<String> returnnumber = new ArrayList<>();

        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_USER_ID + " = '" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                returnnumber.add((cursor.getString(cursor.getColumnIndex(type))));
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnnumber;
    }
    /*public double getReceivedAmountDetails(String agentId){

        double receivedAmount = 0;
        *//*SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST +
                        " LEFT JOIN " + TABLE_TRIPSHEETS_SO_LIST + " ON " + TABLE_TRIPSHEETS_PAYMENTS_LIST + "." + KEY_TRIPSHEET_PAYMENTS_USER_ID + " = " +
                TABLE_TRIPSHEETS_SO_LIST + "." +  KEY_TRIPSHEET_SO_AGENTID + " WHERE " + TABLE_TRIPSHEETS_PAYMENTS_LIST + "." + KEY_TRIPSHEET_PAYMENTS_USER_ID + " = ?",
                new String[]{"1"});
*//*
        String selectQuery = "SELECT  * FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " tp LEFT JOIN " + TABLE_TRIPSHEETS_SO_LIST + " top ON tp." + KEY_TRIPSHEET_PAYMENTS_USER_ID
                + "=top." + KEY_TRIPSHEET_SO_AGENTID + " AND top." + KEY_TRIPSHEET_PAYMENTS_USER_ID + "='" + agentId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null){

            while(cursor.moveToNext()){
                receivedAmount= Double.parseDouble((cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT))));
            }
        }
        return receivedAmount;
    }*/

    public ArrayList<AgentDeliveriesBean> getdeliveryDetails(String userId) {
        ArrayList<AgentDeliveriesBean> deliveriesBean = new ArrayList<>();

        try {
            String selectQuery = "SELECT DISTINCT(tripsheet_delivery_number) AS Tripsheet_Delivery_Number" +
                    ", COUNT(tripsheet_delivery_number) AS Total_COUNT  FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = '" + userId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            HashMap<String, String> records = new HashMap<>();
            if (c.moveToFirst()) {
                do {
                    records.put(c.getString(c.getColumnIndex("Tripsheet_Delivery_Number")), c.getString(c.getColumnIndex("Total_COUNT")));
//                    AgentDeliveriesBean returndeliveriesBean = new AgentDeliveriesBean();
//                    returndeliveriesBean.setTripNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NUMBER)));
//                    returndeliveriesBean.setTripDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDON)));
//                    returndeliveriesBean.setDeliverdstatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_STATUS)));
//                    returndeliveriesBean.setDeliveredItems(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SALEVALUE)));
//                    returndeliveriesBean.setDeliveredBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDBY)));
//                    deliveriesBean.add(returndeliveriesBean);
                } while (c.moveToNext());
            }
            for (Map.Entry<String, String> entry : records.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE tripsheet_delivery_number  = '" + key + "'";
                c = db.rawQuery(selectQuery, null);
                boolean rerun = true;
                if (c.moveToFirst()) {
                    do {
                        if (rerun) {
                            AgentDeliveriesBean returndeliveriesBean = new AgentDeliveriesBean();
                            returndeliveriesBean.setTripNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NUMBER)));
                            returndeliveriesBean.setTripDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDON)));
                            returndeliveriesBean.setDeliverdstatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_STATUS)));
                            returndeliveriesBean.setDeliveredItems(value);
                            returndeliveriesBean.setDeliveredBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDBY)));
                            deliveriesBean.add(returndeliveriesBean);
                            rerun = false;
                        }
                    } while (c.moveToNext());
                }

            }
            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return deliveriesBean;
    }


    public String getProductCode(String productId, String type) {
        String productcode = "";

        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_ID + " = '" + productId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                productcode = (cursor.getString(cursor.getColumnIndex(type)));
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return productcode;
    }


    public String getProductUOM(String productId, String type) {
        String productuom = "";

        try {
            String selectQuery = "SELECT  *  FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_ID + " = '" + productId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                productuom = (cursor.getString(cursor.getColumnIndex(type)));
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return productuom;
    }

    public ArrayList<String[]> getdeliveryDetailsPreview(String userId, String tripId) {

        ArrayList<String[]> arList = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_NUMBER + " = " + "'" + userId + "'"
                    + " AND " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = " + "'" + tripId + "'";
            // + " AND " + KEY_TRIPSHEET_DELIVERY_SO_ID + " = " + "'" + agentSoId + "'";
            //  String selectQuery = "SELECT  * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_NUMBER + " = " + userId + " AND " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = " + tripId + "";
            // String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE tripsheet_delivery_number  = '" + userId + "'" + " AND " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + "";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            arList = new ArrayList<>(c.getCount());
            // c = db.rawQuery(selectQuery, null);
            // boolean rerun=true;
            if (c.moveToFirst()) {
                do {
                    String[] temp = new String[8];
                    temp[0] = getProductName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)), KEY_PRODUCT_TITLE);
                    temp[1] = c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_QUANTITY));
                    temp[2] = c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_UNITPRICE));
                    temp[3] = c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_AMOUNT));
                    temp[4] = c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXAMOUNT));

                    temp[5] = getProductCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)), KEY_PRODUCT_CODE);
                    temp[6] = getProductUOM(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)), KEY_PRODUCT_UOM);
                    // temp[5] = getHSSNNUMBERByProductId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)));
                    // temp[6] = String.valueOf(getGSTByProductId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS))));
                    // temp[7] = String.valueOf(getVATByProductId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS))));
                    arList.add(temp);


                } while (c.moveToNext());
            }


            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return arList;
    }

    /**
     * Method to fetch all tripsheets deliveries list with comb of trip id, agentid and agent so id
     */
    public ArrayList<TripSheetDeliveriesBean> fetchAllTripsheetsDeliveriesListCombo(String tripsheetId, String agentId, String agentSoId) {
        ArrayList<TripSheetDeliveriesBean> alltripsheetsDeliveries = new ArrayList<TripSheetDeliveriesBean>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = " + "'" + tripsheetId + "'"
                    + " AND " + KEY_TRIPSHEET_DELIVERY_USER_ID + " = " + "'" + agentId + "'"
                    + " AND " + KEY_TRIPSHEET_DELIVERY_SO_ID + " = " + "'" + agentSoId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripSheetDeliveriesBean tripDeliveriesBean = new TripSheetDeliveriesBean();

                    tripDeliveriesBean.setmTripsheetDeliveryNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NO)));
                    tripDeliveriesBean.setmTripsheetDeliveryNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NUMBER)));
                    tripDeliveriesBean.setmTripsheetDelivery_tripId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TRIP_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_so_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SO_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_so_code(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SO_CODE)));
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


    public ArrayList<AgentReturnsBean> getreturnsDetails(String userId) {
        ArrayList<AgentReturnsBean> agentsreturnsBean = new ArrayList<>();

        try {
            String selectQuery = "SELECT DISTINCT(tripsheet_returns_return_number) AS Tripsheet_Return_Number" +
                    ", COUNT(tripsheet_returns_return_number) AS Total_COUNT  FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_USER_ID + " = '" + userId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            HashMap<String, String> records = new HashMap<>();
            if (c.moveToFirst()) {
                do {
                    records.put(c.getString(c.getColumnIndex("Tripsheet_Return_Number")), c.getString(c.getColumnIndex("Total_COUNT")));
//                    AgentDeliveriesBean returndeliveriesBean = new AgentDeliveriesBean();
//                    returndeliveriesBean.setTripNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NUMBER)));
//                    returndeliveriesBean.setTripDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDON)));
//                    returndeliveriesBean.setDeliverdstatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_STATUS)));
//                    returndeliveriesBean.setDeliveredItems(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SALEVALUE)));
//                    returndeliveriesBean.setDeliveredBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_CREATEDBY)));
//                    deliveriesBean.add(returndeliveriesBean);
                } while (c.moveToNext());
            }
            for (Map.Entry<String, String> entry : records.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE tripsheet_returns_return_number  = '" + key + "'";
                c = db.rawQuery(selectQuery, null);
                boolean rerun = true;
                if (c.moveToFirst()) {
                    do {
                        if (rerun) {
                            AgentReturnsBean returnsBean = new AgentReturnsBean();
                            returnsBean.setReturnNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_RETURN_NUMBER)));
                            returnsBean.setReturnDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_CREATED_ON)));
                            returnsBean.setReturnStatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_STATUS)));
                            returnsBean.setReturnedItems(value);
                            returnsBean.setReturnedBy(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_CREATED_BY)));
                            agentsreturnsBean.add(returnsBean);
                            rerun = false;
                        }
                    } while (c.moveToNext());
                }

            }
            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return agentsreturnsBean;
    }

    public ArrayList<String[]> getreturnDetailsPreview(String userId, String tripId) {
        String obamount = "0.0";
        ArrayList<String[]> arList = null;
        try {

            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_RETURN_NUMBER + " = " + "'" + userId + "'"
                    + " AND " + KEY_TRIPSHEET_RETURNS_TRIP_ID + " = " + "'" + tripId + "'";
            // String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE tripsheet_returns_return_number  = '" + userId + "'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            arList = new ArrayList<>(c.getCount());
            // c = db.rawQuery(selectQuery, null);
            // boolean rerun=true;
            if (c.moveToFirst()) {
                do {
                    String[] temp = new String[4];
                    temp[0] = getProductName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS)), KEY_PRODUCT_TITLE);
                    temp[1] = getProductName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS)), KEY_PRODUCT_UOM);
                    temp[2] = c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_QUANTITY));
                    //temp[3] = c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_TYPE));
                    //  temp[4] = fetchCansorCratesDueByIds(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS)), KEY_PRODUCT_UOM);
/*
                    String cc = c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES));
                    if (cc.equals("2600005")) {
                        obamount = fetchCansorCratesDueByIds1(userId, cc, "cans");
                        // CANS DUE
                        temp[5] = obamount;
                    } else if (cc.equals("2600006")) {
                        obamount = fetchCansorCratesDueByIds1(userId, cc, "crates");
                        // CRATES DUE
                        temp[5] = obamount;
                    } else {
                        temp[5] = obamount;
                    }

                    Map<String, String> deliveredProductsHashMap = fetchDeliveriesListByTripSheetId(userId, c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_USER_CODES)), c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES)));
                    temp[6] = (deliveredProductsHashMap.get(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS))));*/
                    arList.add(temp);


                } while (c.moveToNext());
            }


            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return arList;
    }


    public ArrayList<AgentPaymentsBean> getpaymentDetails(String userId) {
        ArrayList<AgentPaymentsBean> paymentsBean = new ArrayList<>();

        try {
            String selectQuery = "SELECT DISTINCT(tripsheet_payments_payment_number) AS Tripsheet_Payments_Number" +
                    ", COUNT(tripsheet_payments_payment_number) AS Total_COUNT  FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_USER_ID + " = '" + userId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            HashMap<String, String> records = new HashMap<>();
            if (c.moveToFirst()) {
                do {
                    records.put(c.getString(c.getColumnIndex("Tripsheet_Payments_Number")), c.getString(c.getColumnIndex("Total_COUNT")));
                } while (c.moveToNext());
            }
            for (Map.Entry<String, String> entry : records.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE tripsheet_payments_payment_number  = '" + key + "'";
                c = db.rawQuery(selectQuery, null);
                boolean rerun = true;
                if (c.moveToFirst()) {
                    do {
                        if (rerun) {
                            AgentPaymentsBean agentpaymentsBean = new AgentPaymentsBean();
                            agentpaymentsBean.setPayment_Number(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER)));
                            agentpaymentsBean.setPayment_date(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_DATE)));
                            agentpaymentsBean.setPayment_status(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_STATUS)));
                            agentpaymentsBean.setPayment_amount("0.000");
                            agentpaymentsBean.setPayment_mop(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TYPE)));
                            agentpaymentsBean.setPayment_checkno(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID)));
                            agentpaymentsBean.setPayment_checkDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_DATE)));
                            agentpaymentsBean.setPayment_bankName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_BANK_NAME)));
                            paymentsBean.add(agentpaymentsBean);
                            rerun = false;
                        }
                    } while (c.moveToNext());
                }

            }
            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return paymentsBean;
    }


    public ArrayList<String[]> getpaymentDetailsPreview(String userId, String tripId) {
        //ArrayList<AgentPaymentsBean> paymentsBean = new ArrayList<>();
        ArrayList<String[]> arList = null;
        try {
            //   String selectQuery = "SELECT DISTINCT(tripsheet_payments_payment_number) AS Tripsheet_Payments_Number" +
            //           ", COUNT(tripsheet_payments_payment_number) AS Total_COUNT  FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_USER_ID + " = '" + userId + "'";
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER + " = " + "'" + userId + "'"
                    + " AND " + KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " = " + "'" + tripId + "'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            arList = new ArrayList<>(c.getCount());
            // HashMap<String, String> records = new HashMap<>();
            // if (c.moveToFirst()) {
            //     do {
            //         records.put(c.getString(c.getColumnIndex("Tripsheet_Payments_Number")), c.getString(c.getColumnIndex("Total_COUNT")));
            //     } while (c.moveToNext());
            // }
            // for (Map.Entry<String, String> entry : records.entrySet()) {
            //    String key = entry.getKey();
            //    String value = entry.getValue();
            //    selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE tripsheet_payments_payment_number  = '" + key + "'";
            //    c = db.rawQuery(selectQuery, null);
            //     boolean rerun = true;
            if (c.moveToFirst()) {
                do {


                    String[] temp = new String[8];
                    temp[0] = getProductName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)), KEY_PRODUCT_TITLE);
                    temp[1] = c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_QUANTITY));
                    temp[2] = c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_UNITPRICE));
                    temp[3] = c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_AMOUNT));
                    temp[4] = c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TAXAMOUNT));

                    temp[5] = getProductCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)), KEY_PRODUCT_CODE);
                    temp[6] = getProductUOM(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)), KEY_PRODUCT_UOM);
                    // temp[5] = getHSSNNUMBERByProductId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)));
                    // temp[6] = String.valueOf(getGSTByProductId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS))));
                    // temp[7] = String.valueOf(getVATByProductId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS))));
                    arList.add(temp);

                  /*  //if (rerun) {
                    AgentPaymentsBean agentpaymentsBean = new AgentPaymentsBean();
                    agentpaymentsBean.setPayment_Number(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER)));
                    agentpaymentsBean.setPayment_date(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_DATE)));
                    agentpaymentsBean.setPayment_status(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_STATUS)));
                    agentpaymentsBean.setPayment_amount("0.000");
                    agentpaymentsBean.setPayment_mop(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TYPE)));
                    agentpaymentsBean.setPayment_checkno(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID)));
                    agentpaymentsBean.setPayment_checkDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_DATE)));
                    agentpaymentsBean.setPayment_bankName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_BANK_NAME)));
                    paymentsBean.add(agentpaymentsBean);
                    // rerun = false;*/

                } while (c.moveToNext());
            }


            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return arList;
    }


    public ArrayList<PaymentsBean> getpaymentDetailsForAgents(String userId) {
        ArrayList<PaymentsBean> paymentsBean = new ArrayList<>();

        try {
            // By Sekhar
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_USER_ID + " = '" + userId + "'" + " ORDER BY " + KEY_TRIPSHEET_PAYMENTS_DATE + " DESC ";
/*
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_USER_ID
                    + " = '" + userId + "'";*/
            System.out.println("SQ::: " + selectQuery);
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    PaymentsBean tripPaymentsBean = new PaymentsBean();
                    tripPaymentsBean.setPayments_paymentsNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_PAYMENT_NO)));
                    tripPaymentsBean.setPayments_paymentsNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER)));

                    tripPaymentsBean.setPayments_tripsheetId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRIP_ID)));
                    tripPaymentsBean.setPayments_userId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_USER_ID)));
                    tripPaymentsBean.setPayments_userCodes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_USER_CODES)));
                    tripPaymentsBean.setPayments_routeId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_ROUTE_ID)));
                    tripPaymentsBean.setPayments_routeCodes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_ROUTE_CODES)));

                    tripPaymentsBean.setPayments_chequeNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CHE_TRANS_ID)));
                    tripPaymentsBean.setPayments_accountNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_AC_CA_NO)));
                    tripPaymentsBean.setPayments_accountName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_ACOUNT_NAME)));
                    tripPaymentsBean.setPayments_bankName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_BANK_NAME)));
                    tripPaymentsBean.setPayments_chequeDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_DATE)));
                    tripPaymentsBean.setPayments_chequeClearDate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_CLEAR_DATE)));
                    tripPaymentsBean.setPayments_cheque_image_path(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CHEQUE_PATH)));
                    tripPaymentsBean.setPayments_receiverName(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_RECEIVER_NAME)));
                    tripPaymentsBean.setPayments_transActionStatus(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TRANS_STATUS)));

                    tripPaymentsBean.setPayments_taxTotal(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TAX_TOTAL))));
                    tripPaymentsBean.setPayments_saleValue(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_SALE_VALUE))));
                    tripPaymentsBean.setPayments_type(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_TYPE)));
                    tripPaymentsBean.setPayments_status(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_STATUS)));
                    tripPaymentsBean.setPayments_delete(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_DELETE)));
                    tripPaymentsBean.setPayments_saleOrderId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_SO_ID)));
                    tripPaymentsBean.setPayments_saleOrderCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_SO_CODE)));
                    tripPaymentsBean.setPayments_receivedAmount(Double.parseDouble(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT))));
                    tripPaymentsBean.setPayments_cheque_upload_status(c.getInt(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_CHEQUE_UPLOAD_STATUS)));
                    tripPaymentsBean.setPayment_date(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_DATE)));
                    tripPaymentsBean.setFirst_name(c.getString(c.getColumnIndex(KEY_PAYMENT_FIRST_NAME)));

                    paymentsBean.add(tripPaymentsBean);

                } while (c.moveToNext());
            }
            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return paymentsBean;
    }

    /**
     * This method is used to check the product exists or not.
     *
     * @param productId
     * @return
     */
    public int checkProductIsExistsOrNot(String productId) {
        int maxID = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_ID + "='" + productId + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                maxID = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        return maxID;
    }

    /**
     * Method to get count of the agents stock table
     */
    public int getAgentsStockTableCount() {
        int noOfEvents = 0;
        try {
            String countQuery = "SELECT * FROM " + TABLE_AGENTS_STOCK_LIST;
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
     * @param mStockList
     * @param agentId
     */
    public void insertAgentsStockListData(ArrayList<AgentsStockBean> mStockList, String agentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mStockList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_AGENT_STOCK_AGENT_ID, agentId);
                values.put(KEY_AGENT_STOCK_PRODUCT_NAME, mStockList.get(i).getmProductName());
                values.put(KEY_AGENT_STOCK_PRODUCT_CODE, mStockList.get(i).getmProductCode());
                values.put(KEY_AGENT_STOCK_PRODUCT_ID, mStockList.get(i).getmProductId());
                values.put(KEY_AGENT_STOCK_PRODUCT_UOM, mStockList.get(i).getmProductUOM());
                values.put(KEY_AGENT_STOCK_PRODUCT_STOCK_QUNATITY, mStockList.get(i).getmProductStockQunatity());
                values.put(KEY_AGENT_STOCK_PRODUCT_DELIVERY_QUNATITY, mStockList.get(i).getmProductDeliveryQunatity());
                values.put(KEY_AGENT_STOCK_PRODUCT_CNQUANTITY, mStockList.get(i).getmProductCBQuantity());


                int checkVal = checkAgentStockExistsOrNot(mStockList.get(i).getmProductId(), agentId);
                if (checkVal == 0) {
                    System.out.println("+++++++++++++++++++++++++ STOCK INSERTED++++++++++++++++++++++");
                    db.insert(TABLE_AGENTS_STOCK_LIST, null, values);
                } else {
                    System.out.println("+++++++++++++++++++++++++ STOCK UPDATED++++++++++++++++++++++");
                    db.update(TABLE_AGENTS_STOCK_LIST, values, KEY_AGENT_STOCK_AGENT_ID + " = ?" + " AND " + KEY_AGENT_STOCK_PRODUCT_ID + " = ?", new String[]{String.valueOf(agentId), String.valueOf(mStockList.get(i).getmProductId())});
                }

                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to check weather the Agent Stock exists or not using product id and agentid.
     *
     * @param productId
     * @param agentId
     * @return integer value
     */
    public int checkAgentStockExistsOrNot(String productId, String agentId) {
        int maxID = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_AGENTS_STOCK_LIST + " WHERE " + KEY_AGENT_STOCK_AGENT_ID + "='" + agentId + "'"
                + " AND " + KEY_AGENT_STOCK_PRODUCT_ID + "='" + productId + "'";
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
     * Method to get all stock list by agentid
     *
     * @param agentId
     * @return
     */
    public ArrayList<AgentsStockBean> fetchAllStockByAgentId(String agentId) {
        ArrayList<AgentsStockBean> stockList = new ArrayList<AgentsStockBean>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_AGENTS_STOCK_LIST + " WHERE " + KEY_AGENT_STOCK_AGENT_ID + " = '" + agentId + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    AgentsStockBean agentsStockBean = new AgentsStockBean();

                    agentsStockBean.setmProductId(cursor.getString(cursor.getColumnIndex(KEY_AGENT_STOCK_PRODUCT_ID)));
                    agentsStockBean.setmProductName(cursor.getString(cursor.getColumnIndex(KEY_AGENT_STOCK_PRODUCT_NAME)));
                    agentsStockBean.setmProductCode(cursor.getString(cursor.getColumnIndex(KEY_AGENT_STOCK_PRODUCT_CODE)));
                    agentsStockBean.setmProductUOM(cursor.getString(cursor.getColumnIndex(KEY_AGENT_STOCK_PRODUCT_UOM)));
                    agentsStockBean.setmProductStockQunatity(cursor.getString(cursor.getColumnIndex(KEY_AGENT_STOCK_PRODUCT_STOCK_QUNATITY)));
                    agentsStockBean.setmProductDeliveryQunatity(cursor.getString(cursor.getColumnIndex(KEY_AGENT_STOCK_PRODUCT_DELIVERY_QUNATITY)));
                    Double sq = Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_AGENT_STOCK_PRODUCT_STOCK_QUNATITY)));
                    Double dq = Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_AGENT_STOCK_PRODUCT_DELIVERY_QUNATITY)));
                    Double cbq = sq - dq;
                    agentsStockBean.setmProductCBQuantity(String.valueOf(cbq));

                    stockList.add(agentsStockBean);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stockList;
    }

    /**
     * Method to check weather the trip sheet Stock exists or not using trip sheet id and stockid and product id.
     *
     * @param tripsheetId
     * @param stockId
     * @return integer value
     */
    public int checkTripsheetStockExistsOrNot(String tripsheetId, String stockId, String productId) {
        int maxID = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_TRIPSHEETS_STOCK_LIST + " WHERE " + KEY_TRIPSHEET_STOCK_TRIPSHEET_ID + "='" + tripsheetId + "'"
                + " AND " + KEY_TRIPSHEET_STOCK_ID + "='" + stockId + "'" + " AND " + KEY_TRIPSHEET_STOCK_PRODUCT_ID + "='" + productId + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                maxID = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        return maxID;
    }

    /**
     * Method to update the stock after tdc sale has been done.
     */
    public void updateAgentStockAfterTDCSales(String mAgentId, String mProdId, String deliveryQunatity) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_AGENT_STOCK_PRODUCT_DELIVERY_QUNATITY, deliveryQunatity);

            int status = db.update(TABLE_AGENTS_STOCK_LIST, values, KEY_AGENT_STOCK_AGENT_ID + " = ?" + " AND " + KEY_AGENT_STOCK_PRODUCT_ID + " = ?", new String[]{String.valueOf(mAgentId), String.valueOf(mProdId)});

            System.out.println("AgentStockUpdated:: " + status);

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to get sale quantity by agent and product id.
     *
     * @param agentId
     * @return
     */
    public String getSaleQuantityByAgentAndProductId(String agentId, String productId) {
        String saleQuantity = "";

        try {
            String selectQuery = "SELECT * FROM " + TABLE_AGENTS_STOCK_LIST + " WHERE " + KEY_AGENT_STOCK_AGENT_ID + " = '" + agentId + "'"
                    + " AND " + KEY_AGENT_STOCK_PRODUCT_ID + " = '" + productId + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    saleQuantity = cursor.getString(cursor.getColumnIndex(KEY_AGENT_STOCK_PRODUCT_DELIVERY_QUNATITY));
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return saleQuantity;
    }

    /**
     * Method to update the stock after tdc sale has been done.
     */
    public void updateAgentStockAfterAgentDeliverFromStock(String mAgentId, ArrayList<String> productIdsList, ArrayList<String> productValuesList) {
        try {
            for (int j = 0; j < productIdsList.size(); j++) {
                ContentValues values = new ContentValues();
                int checkVal = checkAgentStockExistsOrNot(productIdsList.get(j).toString(), mAgentId);
                if (checkVal == 0) {
                    String productCode = "", productUOM = "", productName = "";
                    // Insert
                    String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_ID + " = '" + productIdsList.get(j).toString() + "'";
                    // SQLiteDatabase db1 = this.getWritableDatabase();
                    SQLiteDatabase db = this.getWritableDatabase();
                    Cursor cursor1 = db.rawQuery(selectQuery, null);

                    if (cursor1.moveToFirst()) {
                        do {
                            productCode = cursor1.getString(cursor1.getColumnIndex(KEY_PRODUCT_CODE));
                            productUOM = cursor1.getString(cursor1.getColumnIndex(KEY_PRODUCT_UOM));
                            productName = cursor1.getString(cursor1.getColumnIndex(KEY_PRODUCT_TITLE));
                        } while (cursor1.moveToNext());
                    }

                    cursor1.close();

                    System.out.println("Product Code:::: " + productCode + "\n");
                    System.out.println("Product UOM:::: " + productUOM + "\n");
                    System.out.println("Product Id:::: " + productIdsList.get(j).toString() + "\n");
                    System.out.println("Product Name:::: " + productName + "\n");
                    System.out.println("Product Stock Quantity:::: " + productValuesList.get(j).toString() + "\n");

                    values.put(KEY_AGENT_STOCK_AGENT_ID, mAgentId);
                    values.put(KEY_AGENT_STOCK_PRODUCT_NAME, productName);
                    values.put(KEY_AGENT_STOCK_PRODUCT_CODE, productCode);
                    values.put(KEY_AGENT_STOCK_PRODUCT_ID, productIdsList.get(j).toString());
                    values.put(KEY_AGENT_STOCK_PRODUCT_UOM, productUOM);
                    values.put(KEY_AGENT_STOCK_PRODUCT_STOCK_QUNATITY, productValuesList.get(j).toString());
                    values.put(KEY_AGENT_STOCK_PRODUCT_DELIVERY_QUNATITY, "0");
                    values.put(KEY_AGENT_STOCK_PRODUCT_CNQUANTITY, "0");

                    long status1 = db.insert(TABLE_AGENTS_STOCK_LIST, null, values);
                    System.out.println("+++++++++++++++++++++++++ AGENT STOCK NEW INSERTED++++++++++++++++++++++" + status1);
                    db.close();
                } else {
                    // Update
                    String existQuantity = getStockQuantityByProdIdandAgentId(productIdsList.get(j).toString(), mAgentId);
                    System.out.println("EXISTING QUAN VAL::: " + existQuantity);
                    Double newstockVal = Double.parseDouble(existQuantity) + Double.parseDouble(productValuesList.get(j).toString());
                    values.put(KEY_AGENT_STOCK_PRODUCT_STOCK_QUNATITY, String.valueOf(newstockVal));
                    SQLiteDatabase db1 = this.getWritableDatabase();
                    int status = db1.update(TABLE_AGENTS_STOCK_LIST, values, KEY_AGENT_STOCK_AGENT_ID + " = ?" + " AND " + KEY_AGENT_STOCK_PRODUCT_ID + " = ?",
                            new String[]{String.valueOf(mAgentId), String.valueOf(productIdsList.get(j).toString())});

                    System.out.println("++++ AgentStock Updated ++++ :: " + status);
                    db1.close();
                }
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to insert the mTripsheetsDeliveriesList.
     *
     * @param mTripsheetsDeliveriesList
     */
    public void insertAgentStockDeliveriesListData(ArrayList<TripSheetDeliveriesBean> mTripsheetsDeliveriesList) {
        try {
            for (TripSheetDeliveriesBean tripSheetDeliveriesBean : mTripsheetsDeliveriesList) {
                ContentValues values = new ContentValues();
                values.put(KEY_AGENT_STOCK_DELIVERY_TRIP_ID, tripSheetDeliveriesBean.getmTripsheetDelivery_tripId());
                values.put(KEY_AGENT_STOCK_DELIVERY_SO_ID, tripSheetDeliveriesBean.getmTripsheetDelivery_so_id());
                values.put(KEY_AGENT_STOCK_DELIVERY_SO_CODE, tripSheetDeliveriesBean.getmTripsheetDelivery_so_code());
                values.put(KEY_AGENT_STOCK_DELIVERY_USER_ID, tripSheetDeliveriesBean.getmTripsheetDelivery_userId());
                values.put(KEY_AGENT_STOCK_DELIVERY_USER_CODES, tripSheetDeliveriesBean.getmTripsheetDelivery_userCodes());
                values.put(KEY_AGENT_STOCK_DELIVERY_ROUTE_ID, tripSheetDeliveriesBean.getmTripsheetDelivery_routeId());
                values.put(KEY_AGENT_STOCK_DELIVERY_ROUTE_CODES, tripSheetDeliveriesBean.getmTripsheetDelivery_routeCodes());
                values.put(KEY_AGENT_STOCK_DELIVERY_PRODUCT_IDS, tripSheetDeliveriesBean.getmTripsheetDelivery_productId());
                values.put(KEY_AGENT_STOCK_DELIVERY_PRODUCT_CODES, tripSheetDeliveriesBean.getmTripsheetDelivery_productCodes());
                values.put(KEY_AGENT_STOCK_DELIVERY_TAXPERCENT, tripSheetDeliveriesBean.getmTripsheetDelivery_TaxPercent());
                values.put(KEY_AGENT_STOCK_DELIVERY_UNITPRICE, tripSheetDeliveriesBean.getmTripsheetDelivery_UnitPrice());
                values.put(KEY_AGENT_STOCK_DELIVERY_QUANTITY, tripSheetDeliveriesBean.getmTripsheetDelivery_Quantity());
                values.put(KEY_AGENT_STOCK_DELIVERY_AMOUNT, tripSheetDeliveriesBean.getmTripsheetDelivery_Amount());
                values.put(KEY_AGENT_STOCK_DELIVERY_TAXAMOUNT, tripSheetDeliveriesBean.getmTripsheetDelivery_TaxAmount());
                values.put(KEY_AGENT_STOCK_DELIVERY_TAXTOTAL, tripSheetDeliveriesBean.getmTripsheetDelivery_TaxTotal());
                values.put(KEY_AGENT_STOCK_DELIVERY_SALEVALUE, tripSheetDeliveriesBean.getmTripsheetDelivery_SaleValue());
                values.put(KEY_AGENT_STOCK_DELIVERY_STATUS, tripSheetDeliveriesBean.getmTripsheetDelivery_Status());
                values.put(KEY_AGENT_STOCK_DELIVERY_DELETE, tripSheetDeliveriesBean.getmTripsheetDelivery_Delete());
                values.put(KEY_AGENT_STOCK_DELIVERY_CREATEDBY, tripSheetDeliveriesBean.getmTripsheetDelivery_CreatedBy());
                values.put(KEY_AGENT_STOCK_DELIVERY_CREATEDON, tripSheetDeliveriesBean.getmTripsheetDelivery_CreatedOn());
                values.put(KEY_AGENT_STOCK_DELIVERY_UPDATEDON, tripSheetDeliveriesBean.getmTripsheetDelivery_UpdatedOn());
                values.put(KEY_AGENT_STOCK_DELIVERY_UPDATEDBY, tripSheetDeliveriesBean.getmTripsheetDelivery_UpdatedBy());
                //values.put(KEY_AGENT_STOCK_DELIVERY_UPLOAD_STATUS, 0);

                //int noOfRecordsExisted = checkProductExistsInAgentStockDeliveryTableByAgentIdProductId(tripSheetDeliveriesBean.getmTripsheetDelivery_so_id(), tripSheetDeliveriesBean.getmTripsheetDelivery_userId(), tripSheetDeliveriesBean.getmTripsheetDelivery_productId());

                SQLiteDatabase db = this.getWritableDatabase();
                long status;

//                if (noOfRecordsExisted == 0) {
//                    values.put(KEY_AGENT_STOCK_DELIVERY_UPLOAD_STATUS, 0);
//                    status = db.insert(TABLE_AGENT_STOCK_DELIVERIES_LIST, null, values);
//                    System.out.println("Agent Stock Delivery API Record Inserted=================" + status);
//                } else {
//                    status = db.update(TABLE_AGENT_STOCK_DELIVERIES_LIST, values, KEY_AGENT_STOCK_DELIVERY_TRIP_ID + " = ? AND "
//                            + KEY_AGENT_STOCK_DELIVERY_SO_ID + " = ? AND " + KEY_AGENT_STOCK_DELIVERY_PRODUCT_IDS + " = ?", new String[]{tripSheetDeliveriesBean.getmTripsheetDelivery_tripId(), tripSheetDeliveriesBean.getmTripsheetDelivery_so_id(), tripSheetDeliveriesBean.getmTripsheetDelivery_productId()});
//                    System.out.println("Agent Stock Delivery API Record Updated=================" + status);
//                }

                status = db.insert(TABLE_AGENT_STOCK_DELIVERIES_LIST, null, values);
                System.out.println("Agent Stock Delivery API Record Inserted=================" + status);

                values.clear();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get the existing stock quantity by using product id and agent id.
     *
     * @param productId
     * @param agentId
     */
    private String getStockQuantityByProdIdandAgentId(String productId, String agentId) {
        String existingQuantity = "";
        try {
            String selectQuery = "SELECT * FROM " + TABLE_AGENTS_STOCK_LIST + " WHERE " + KEY_AGENT_STOCK_PRODUCT_ID
                    + " = '" + productId + "'" + " AND " + KEY_AGENT_STOCK_AGENT_ID
                    + " = '" + agentId + "'";
            SQLiteDatabase db1 = this.getWritableDatabase();
            Cursor cursor1 = db1.rawQuery(selectQuery, null);

            if (cursor1.moveToFirst()) {
                do {
                    existingQuantity = cursor1.getString(cursor1.getColumnIndex(KEY_AGENT_STOCK_PRODUCT_STOCK_QUNATITY));
                } while (cursor1.moveToNext());
            }

            cursor1.close();
            db1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existingQuantity;
    }

    /**
     * Method to check the product is existing or not using by soid,agentId and productid.
     *
     * @param soId
     * @param agentId
     * @param productId
     * @return
     */
    public int checkProductExistsInAgentStockDeliveryTableByAgentIdProductId(String soId, String agentId, String productId) {
        int noOfRecords = 0;

        try {
            String selectQuery = "SELECT " + KEY_AGENT_STOCK_DELIVERY_NO + " FROM " + TABLE_AGENT_STOCK_DELIVERIES_LIST
                    + " WHERE " + KEY_AGENT_STOCK_DELIVERY_SO_ID + " = '" + soId + "' AND " + KEY_AGENT_STOCK_DELIVERY_USER_ID
                    + " = '" + agentId + "' AND " + KEY_AGENT_STOCK_DELIVERY_PRODUCT_IDS + " = '" + productId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                noOfRecords = cursor.getCount();
                cursor.close();
            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return noOfRecords;
    }

    /**
     * Method to get the unuploaded unique agent stock deliveries.
     *
     * @param agentId
     * @return
     */
    public ArrayList<String> fetchUnUploadedUniqueAgentStockDeliverys() {
        ArrayList<String> tripSheetIds = new ArrayList<>();

        try {
            String selectQuery = "SELECT " + KEY_AGENT_STOCK_DELIVERY_PRODUCT_IDS + " FROM " +
                    TABLE_AGENT_STOCK_DELIVERIES_LIST + " WHERE " + KEY_AGENT_STOCK_DELIVERY_UPLOAD_STATUS + " = 0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    tripSheetIds.add(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_PRODUCT_IDS)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripSheetIds;
    }

    /**
     * Method to fetch all agent stock deliveries list by agent id and product id.
     */
    public ArrayList<TripSheetDeliveriesBean> fetchAllAgentStockDeliveriesList(String agentId, String productId) {
        ArrayList<TripSheetDeliveriesBean> alltripsheetsDeliveries = new ArrayList<TripSheetDeliveriesBean>();
// + KEY_AGENT_STOCK_DELIVERY_USER_ID + " = " + "'" + agentId + "'" + " AND "
        try {
            String selectQuery = "SELECT * FROM " + TABLE_AGENT_STOCK_DELIVERIES_LIST + " WHERE "
                    + KEY_AGENT_STOCK_DELIVERY_PRODUCT_IDS + " = " + "'" + productId + "'"
                    + " AND " + KEY_AGENT_STOCK_DELIVERY_UPLOAD_STATUS + " = 0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripSheetDeliveriesBean tripDeliveriesBean = new TripSheetDeliveriesBean();

                    tripDeliveriesBean.setmTripsheetDeliveryNo(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_NO)));
                    tripDeliveriesBean.setmTripsheetDeliveryNumber(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_NUMBER)));
                    tripDeliveriesBean.setmTripsheetDelivery_tripId(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_TRIP_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_so_id(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_SO_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_so_code(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_SO_CODE)));
                    tripDeliveriesBean.setmTripsheetDelivery_userId(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_USER_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_userCodes(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_USER_CODES)));
                    tripDeliveriesBean.setmTripsheetDelivery_routeId(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_ROUTE_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_routeCodes(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_ROUTE_CODES)));
                    tripDeliveriesBean.setmTripsheetDelivery_productId(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_PRODUCT_IDS)));
                    tripDeliveriesBean.setmTripsheetDelivery_productCodes(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_PRODUCT_CODES)));
                    tripDeliveriesBean.setmTripsheetDelivery_TaxPercent(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_TAXPERCENT)));
                    tripDeliveriesBean.setmTripsheetDelivery_UnitPrice(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_UNITPRICE)));
                    tripDeliveriesBean.setmTripsheetDelivery_Quantity(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_QUANTITY)));
                    tripDeliveriesBean.setmTripsheetDelivery_Amount(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_AMOUNT)));
                    tripDeliveriesBean.setmTripsheetDelivery_TaxAmount(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_TAXAMOUNT)));
                    tripDeliveriesBean.setmTripsheetDelivery_TaxTotal(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_TAXTOTAL)));
                    tripDeliveriesBean.setmTripsheetDelivery_SaleValue(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_SALEVALUE)));
                    tripDeliveriesBean.setmTripsheetDelivery_Status(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_STATUS)));
                    tripDeliveriesBean.setmTripsheetDelivery_Delete(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_DELETE)));
                    tripDeliveriesBean.setmTripsheetDelivery_CreatedBy(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_CREATEDBY)));
                    tripDeliveriesBean.setmTripsheetDelivery_CreatedOn(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_CREATEDON)));
                    tripDeliveriesBean.setmTripsheetDelivery_UpdatedOn(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_UPDATEDON)));
                    tripDeliveriesBean.setmTripsheetDelivery_UpdatedBy(c.getString(c.getColumnIndex(KEY_AGENT_STOCK_DELIVERY_UPDATEDBY)));

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
     * Method to update the agent stock table uploaded status after calling API.
     *
     * @param deliveryNumber
     * @param tripSheetId
     * @param saleOrderId
     * @param agentId
     */
    public void updateAgentStockDeliveriesTable(String deliveryNumber, String tripSheetId, String saleOrderId, String agentId, String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        //System.out.println("AGENT ID:: " + agentId);
        //System.out.println("PRODUCT ID:: " + productId);
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_AGENT_STOCK_DELIVERY_NUMBER, deliveryNumber);
            values.put(KEY_AGENT_STOCK_DELIVERY_UPLOAD_STATUS, 1);

            int status = db.update(TABLE_AGENT_STOCK_DELIVERIES_LIST, values,
                    KEY_AGENT_STOCK_DELIVERY_USER_ID + " = ? AND " + KEY_AGENT_STOCK_DELIVERY_PRODUCT_IDS + " = ?",
                    new String[]{agentId, productId});

            //System.out.println("AGENT STOCK INSERT NUMBER UPDATED===== " + status);
            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }


    /**
     * Method to get the gst by product id
     *
     * @param productId
     * @return GST
     */
    public double getGSTByProductId(String productId) {
        double gst = 0.0;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_ID
                    + " = '" + productId + "'";
            SQLiteDatabase db1 = this.getWritableDatabase();
            Cursor cursor1 = db1.rawQuery(selectQuery, null);

            if (cursor1.moveToFirst()) {
                do {
                    String gsts = cursor1.getString(cursor1.getColumnIndex(KEY_PRODUCT_GST_PRICE));
                    if (gsts != null) {
                        gst = Double.parseDouble(cursor1.getString(cursor1.getColumnIndex(KEY_PRODUCT_GST_PRICE)));
                    } else {
                        gst = 0.0;
                    }
                } while (cursor1.moveToNext());
            }

            cursor1.close();
            db1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gst;
    }

    /**
     * Method to get the vat by product id
     *
     * @param productId
     * @return VAT
     */
    public double getVATByProductId(String productId) {
        double vat = 0.0;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_ID
                    + " = '" + productId + "'";
            SQLiteDatabase db1 = this.getWritableDatabase();
            Cursor cursor1 = db1.rawQuery(selectQuery, null);

            if (cursor1.moveToFirst()) {
                do {
                    String gsts1 = cursor1.getString(cursor1.getColumnIndex(KEY_PRODUCT_VAT_PRICE));
                    if (gsts1 != null) {
                        vat = Double.parseDouble(cursor1.getString(cursor1.getColumnIndex(KEY_PRODUCT_VAT_PRICE)));
                    } else {
                        vat = 0.0;
                    }

                } while (cursor1.moveToNext());
            }

            cursor1.close();
            db1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vat;
    }

    /**
     * Method to get the hssn number by product id
     *
     * @param productId
     * @return HSSN NUMBER
     */
    public String getHSSNNUMBERByProductId(String productId) {
        String hssn = "";
        try {
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_ID
                    + " = '" + productId + "'";
            SQLiteDatabase db1 = this.getWritableDatabase();
            Cursor cursor1 = db1.rawQuery(selectQuery, null);

            if (cursor1.moveToFirst()) {
                do {
                    hssn = cursor1.getString(cursor1.getColumnIndex(KEY_PRODUCT_CONTROL_CODE));
                } while (cursor1.moveToNext());
            }

            cursor1.close();
            db1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hssn;
    }

    /**
     * Method to get the name by id
     *
     * @param
     * @param selectedCustomerType
     * @return HSSN NUMBER
     */
    public String getNameById(String Id, long selectedCustomerType) {
        String hssn = "";
        System.out.println("AAAA:: " + selectedCustomerType);
        try {
            String selectQuery = "SELECT * FROM " + TABLE_TDC_CUSTOMERS + " WHERE " + KEY_TDC_CUSTOMER_USER_ID
                    + " = '" + Id + "'";
            SQLiteDatabase db1 = this.getWritableDatabase();
            Cursor cursor1 = db1.rawQuery(selectQuery, null);

            if (cursor1.moveToFirst()) {
                do {
                    if (selectedCustomerType == 1) {
                        hssn = cursor1.getString(cursor1.getColumnIndex(KEY_TDC_CUSTOMER_BUSINESS_NAME));
                    } else {
                        hssn = cursor1.getString(cursor1.getColumnIndex(KEY_TDC_CUSTOMER_NAME));
                    }
                } while (cursor1.moveToNext());
            }

            cursor1.close();
            db1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hssn;
    }


    public String getcodeById(String Id, long selectedCustomerType) {
        String hssn = "";
        System.out.println("AAAA:: " + selectedCustomerType);
        try {
            String selectQuery = "SELECT * FROM " + TABLE_TDC_CUSTOMERS + " WHERE " + KEY_TDC_CUSTOMER_USER_ID
                    + " = '" + Id + "'";
            SQLiteDatabase db1 = this.getWritableDatabase();
            Cursor cursor1 = db1.rawQuery(selectQuery, null);

            if (cursor1.moveToFirst()) {
                do {

                    hssn = cursor1.getString(cursor1.getColumnIndex(KEY_TDC_CUSTOMER_USER_ID));

                } while (cursor1.moveToNext());
            }

            cursor1.close();
            db1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hssn;
    }

    /**
     * Method to fetch all tripsheets deliveries list with comb of trip id, agentid and agent so id
     */
    public ArrayList<TripSheetDeliveriesBean> fetchAllTripsheetsDeliveriesListByTripAndProductId(String tripsheetId, String productId) {
        ArrayList<TripSheetDeliveriesBean> alltripsheetsDeliveries = new ArrayList<TripSheetDeliveriesBean>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = " + "'" + tripsheetId + "'";
            /*+ " AND " + KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS + " = " + "'" + productId + "'";*/

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripSheetDeliveriesBean tripDeliveriesBean = new TripSheetDeliveriesBean();

                    tripDeliveriesBean.setmTripsheetDeliveryNo(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NO)));
                    tripDeliveriesBean.setmTripsheetDeliveryNumber(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NUMBER)));
                    tripDeliveriesBean.setmTripsheetDelivery_tripId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_TRIP_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_so_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SO_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_so_code(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_SO_CODE)));
                    tripDeliveriesBean.setmTripsheetDelivery_userId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_USER_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_userCodes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_USER_CODES)));
                    tripDeliveriesBean.setmTripsheetDelivery_routeId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_ROUTE_ID)));
                    tripDeliveriesBean.setmTripsheetDelivery_routeCodes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_ROUTE_CODES)));
                    String pId = c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS));
                    if (pId.endsWith("_F")) {
                        String[] parts = pId.split("_");
                        tripDeliveriesBean.setmTripsheetDelivery_productId(parts[0]);
                    } else {
                        tripDeliveriesBean.setmTripsheetDelivery_productId(pId);
                    }
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
                    tripDeliveriesBean.setmTripsheetDelivery_productType(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_TYPE)));
                    tripDeliveriesBean.setmTripsheetDelivery_productUOM(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_UOM)));

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
     * Method to fetch all tripsheets returns list baed on tripsheet id from Tripsheets returns list table
     */
    public ArrayList<TripSheetReturnsBean> fetchAllTripsheetsReturnsListByTripId(String tripsheetId) {
        ArrayList<TripSheetReturnsBean> alltripsheetsReturns = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_TRIP_ID + " = " + "'" + tripsheetId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    TripSheetReturnsBean tripReturnsBean = new TripSheetReturnsBean();

                    tripReturnsBean.setmTripshhetReturnsReturn_no(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_RETURN_NO)));
                    tripReturnsBean.setmTripshhetReturnsReturn_number(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_RETURN_NUMBER)));
                    tripReturnsBean.setmTripshhetReturnsTrip_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_TRIP_ID)));
                    tripReturnsBean.setmTripshhetReturns_so_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_SO_ID)));
                    tripReturnsBean.setmTripshhetReturns_so_code(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_SO_CODE)));
                    tripReturnsBean.setmTripshhetReturnsUser_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_USER_ID)));
                    tripReturnsBean.setmTripshhetReturnsUser_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_USER_CODES)));
                    tripReturnsBean.setmTripshhetReturnsRoute_id(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_ROUTE_ID)));
                    tripReturnsBean.setmTripshhetReturnsRoute_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_ROUTE_CODES)));
                    tripReturnsBean.setmTripshhetReturnsProduct_ids(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS)));
                    tripReturnsBean.setmTripshhetReturnsProduct_codes(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES)));
                    tripReturnsBean.setmTripshhetReturnsQuantity(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_QUANTITY)));
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

    public int checkRetailerExistsOrNot(String s, String loginId) {
        int maxID = 0;
        Log.i("customer id@@@" + s, "loginId@@@" + loginId);
        String selectQuery = "SELECT  * FROM " + TABLE_TDC_CUSTOMERS
                + " WHERE " + KEY_TDC_CUSTOMER_USER_ID + "='" + s + "'"
                + " AND " + KEY_TDC_CUSTOMER_CHECK_UNIQUE_ID + "='" + loginId + "'";
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

    public String fetchCansorCratesDueByIds(String tripsheetId, String saleOrderId, String agentId, String prodCode, String type) {
        String due = "0.0";

        try {
            String selectQuery = null;
            SQLiteDatabase db = this.getReadableDatabase();
            if (type.equals("cans")) {
                selectQuery = "SELECT " + KEY_TRIPSHEET_SO_CANS_DUE + " FROM " + TABLE_TRIPSHEETS_SO_LIST
                        + " WHERE " + KEY_TRIPSHEET_SO_TRIPID + " = " + "'" + tripsheetId + "' AND "
                        + KEY_TRIPSHEET_SO_ID + " = '" + saleOrderId + "' AND " + KEY_TRIPSHEET_SO_AGENTID + " = '" + agentId + "'";


                Cursor c = db.rawQuery(selectQuery, null);
                if (c.moveToFirst()) {
                    do {
                        due = c.getString(c.getColumnIndex(KEY_TRIPSHEET_SO_CANS_DUE));
                    } while (c.moveToNext());
                }
                c.close();

            } else if (type.equals("crates")) {
                selectQuery = "SELECT " + KEY_TRIPSHEET_SO_CRATES_DUE + " FROM " + TABLE_TRIPSHEETS_SO_LIST
                        + " WHERE " + KEY_TRIPSHEET_SO_TRIPID + " = " + "'" + tripsheetId + "' AND "
                        + KEY_TRIPSHEET_SO_ID + " = '" + saleOrderId + "' AND " +
                        KEY_TRIPSHEET_SO_AGENTID + " = '" + agentId + "'"; // + "' AND " + KEY_TRIPSHEET_SO_AGENTID + " = '" + agentId KEY_TRIPSHEET_SO_PRODUCTCODE
                Cursor c = db.rawQuery(selectQuery, null);
                if (c.moveToFirst()) {
                    do {
                        due = c.getString(c.getColumnIndex(KEY_TRIPSHEET_SO_CRATES_DUE));
                    } while (c.moveToNext());
                }
                c.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return due;
    }

    /**
     * Method to insert the mTripsheetsStockList.
     *
     * @param mTripsheetsStockList
     */
    public void updateTripsheetsStockListDataForCloseTrips(ArrayList<TripsheetsStockList> mTripsheetsStockList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (int i = 0; i < mTripsheetsStockList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_TRIPSHEET_STOCK_CB_QUANTITY, mTripsheetsStockList.get(i).getmCBQuantity());
                values.put(KEY_TRIPSHEET_STOCK_DELIVERY_QUANTITY, mTripsheetsStockList.get(i).getmDeliveryQuantity());
                values.put(KEY_TRIPSHEET_STOCK_ROUTE_RETURN_QUANTITY, mTripsheetsStockList.get(i).getmRouteReturnQuantity());
                values.put(KEY_TRIPSHEET_STOCK_LEAK_QUANTITY, mTripsheetsStockList.get(i).getmLeakQuantity());
                values.put(KEY_TRIPSHEET_STOCK_OTHER_QUANTITY, mTripsheetsStockList.get(i).getmOtherQuantity());

                values.put(KEY_TRIPSHEET_STOCK_CLOSETRIP_UPLOAD_STATUS, 2); // 0 is default and 1 is close trip and 2 is temp close


                long l = db.update(TABLE_TRIPSHEETS_STOCK_LIST, values, KEY_TRIPSHEET_STOCK_TRIPSHEET_ID + " = ?" + " AND " + KEY_TRIPSHEET_STOCK_ID + " = ?" + " AND " + KEY_TRIPSHEET_STOCK_PRODUCT_ID + " = ?",
                        new String[]{String.valueOf(mTripsheetsStockList.get(i).getmTripsheetStockTripsheetId()),
                                String.valueOf(mTripsheetsStockList.get(i).getmTripsheetStockId()), String.valueOf(mTripsheetsStockList.get(i).getmTripsheetStockProductId())});
                System.out.println("+++++++++++++++++++++++++ TRIP SHEET STOCK UPDATED CLOSE TRIP++++++++++++++++++++++" + l);

                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    /**
     * Method to fetch un uploaded trip sheets stock list group by stock id
     */
    public ArrayList<String> fetchUnUploadedTripSheetUniqueStockIdsForCloseTrip() {
        ArrayList<String> stockIds = new ArrayList<>();

        try {
            String selectQuery;

            selectQuery = "SELECT DISTINCT " + KEY_TRIPSHEET_STOCK_ID + " FROM " + TABLE_TRIPSHEETS_STOCK_LIST
                    + " WHERE " + KEY_TRIPSHEET_STOCK_CLOSETRIP_UPLOAD_STATUS + " = 2";
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

    public void updateTripSheetStockTableCloseTrip(String stockId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            values.put(KEY_TRIPSHEET_STOCK_CLOSETRIP_UPLOAD_STATUS, 1);

            int status = db.update(TABLE_TRIPSHEETS_STOCK_LIST, values, KEY_TRIPSHEET_STOCK_ID + " = ?", new String[]{String.valueOf(stockId)});

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to get the all sale orders by tripid
     *
     * @param tripSheetId
     * @return
     */
    public ArrayList<SaleOrderReturnedProducts> getReturnsProductsListForSaleOrder1(String tripSheetId) {
        ArrayList<SaleOrderReturnedProducts> returnedProductsList = new ArrayList<>();

        try {
            String obamount = "0.0";

            String selectQuery = "SELECT " + KEY_TRIPSHEET_RETURNS_RETURN_NO + ", " + KEY_TRIPSHEET_RETURNS_CREATED_ON + ", " + KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS + ", " + KEY_PRODUCT_TITLE + ", " + KEY_TRIPSHEET_RETURNS_PRODUCT_CODES + ", " + KEY_TRIPSHEET_RETURNS_QUANTITY + ", " + KEY_PRODUCT_RETURNABLE + ", " + KEY_TRIPSHEET_RETURNS_USER_CODES + ", " + KEY_TRIPSHEET_RETURNS_SO_ID
                    + " FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " R LEFT JOIN " + TABLE_PRODUCTS + " P ON R." + KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS + " = P." + KEY_PRODUCT_ID
                    + " WHERE P." + KEY_PRODUCT_RETURNABLE + " = 'Y' AND " + KEY_TRIPSHEET_RETURNS_TRIP_ID + " = '" + tripSheetId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            Log.i("count", +c.getCount() + "");
            if (c.moveToFirst()) {
                do {
                    SaleOrderReturnedProducts returnedProduct = new SaleOrderReturnedProducts();
                    returnedProduct.setReturnno(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_RETURN_NO)));
                    returnedProduct.setReturndate(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_CREATED_ON)));
                    returnedProduct.setId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS)));
                    returnedProduct.setName(c.getString(c.getColumnIndex(KEY_PRODUCT_TITLE)));
                    returnedProduct.setCode(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES)));
                    String cc = c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES));
                    String soId = c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_SO_ID));
                    Log.i("cc detail", cc + "");
                    if (cc.equals("2600005")) {
                        obamount = fetchCansorCratesDueByIds1(tripSheetId, cc, soId, "crates");
                        Log.i("obamount detail", obamount + "");
                        // CANS DUE
                        returnedProduct.setOpeningBalance(obamount);
                    } else if (cc.equals("2600006")) {
                        obamount = fetchCansorCratesDueByIds1(tripSheetId, cc, soId, "cans");
                        // CRATES DUE
                        returnedProduct.setOpeningBalance(obamount);
                    } else {
                        returnedProduct.setOpeningBalance(obamount);
                    }
                    Map<String, String> deliveredProductsHashMap = fetchDeliveriesListByTripSheetId1(tripSheetId, c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_USER_CODES)), c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCT_CODES)));
                    returnedProduct.setReturned(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_QUANTITY)));
                    returnedProduct.setDelivered(deliveredProductsHashMap.get(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_PRODUCTS_IDS))));

                    double closingBalance = Double.parseDouble(returnedProduct.getOpeningBalance()) + Double.parseDouble(returnedProduct.getDelivered()) - Double.parseDouble(returnedProduct.getReturned());
                    returnedProduct.setClosingBalance(String.valueOf(closingBalance));
                    returnedProduct.setAgentId(c.getString(c.getColumnIndex(KEY_TRIPSHEET_RETURNS_USER_CODES)));

                    returnedProductsList.add(returnedProduct);

                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedProductsList;
    }

    /*
    Method to get the deliveires
     */
    public Map<String, String> fetchDeliveriesListByTripSheetId1(String tripsheetId, String agentId, String proCode) {
        Map<String, String> tripsheetsDeliveries = new HashMap<>();

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS + ", " + KEY_TRIPSHEET_DELIVERY_QUANTITY + ", " + KEY_TRIPSHEET_DELIVERY_USER_CODES + " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST
                    + " WHERE " + KEY_TRIPSHEET_DELIVERY_TRIP_ID + " = " + "'" + tripsheetId + "' AND " +
                    KEY_TRIPSHEET_DELIVERY_USER_CODES + " = " + "'" + agentId + "' AND " +
                    KEY_TRIPSHEET_DELIVERY_PRODUCT_CODES + " = " + "'" + proCode + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    tripsheetsDeliveries.put(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)), c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_QUANTITY)));
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripsheetsDeliveries;
    }

    public String fetchCansorCratesDueByIds1(String tripsheetId, String prodCode, String soId, String type) {
        String due = "0.0";

        try {
            String selectQuery = null;
            SQLiteDatabase db = this.getReadableDatabase();
            if (type.equals("cans")) {
                selectQuery = "SELECT " + KEY_TRIPSHEET_SO_CANS_DUE + " FROM " + TABLE_TRIPSHEETS_SO_LIST
                        + " WHERE " + KEY_TRIPSHEET_SO_TRIPID + " = " + "'" + tripsheetId + "' AND " +
                        KEY_TRIPSHEET_SO_ID + " = '" + soId + "'";
                ;


                Cursor c = db.rawQuery(selectQuery, null);
                Log.i("canary count", c.getCount() + "");
                if (c.moveToFirst()) {
                    do {
                        due = c.getString(c.getColumnIndex(KEY_TRIPSHEET_SO_CANS_DUE));
                    } while (c.moveToNext());
                }
                c.close();

            } else if (type.equals("crates")) {
                selectQuery = "SELECT " + KEY_TRIPSHEET_SO_CRATES_DUE + " FROM " + TABLE_TRIPSHEETS_SO_LIST
                        + " WHERE " + KEY_TRIPSHEET_SO_TRIPID + " = " + "'" + tripsheetId + "' AND " +
                        KEY_TRIPSHEET_SO_ID + " = '" + soId + "'";

                Cursor c = db.rawQuery(selectQuery, null);
                Log.i("crates count2", c.getCount() + "");
                if (c.moveToFirst()) {
                    do {
                        due = c.getString(c.getColumnIndex(KEY_TRIPSHEET_SO_CRATES_DUE));
                    } while (c.moveToNext());
                }
                c.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return due;
    }

    /**
     * Method to get the agent name by id
     *
     * @param agentId
     * @return
     */
    public String getAgentNameById(String agentId) {
        String name = "";

        try {
            String selectQuery = "SELECT " + KEY_AGENT_FIRSTNAME + " FROM " + TABLE_AGENTS + " WHERE " + KEY_AGENT_CODE + "='" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    name = c.getString(c.getColumnIndex(KEY_AGENT_FIRSTNAME));
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return name;
    }


    /**
     * Method to get the agent name by id
     *
     * @param agentId
     * @return
     */
    public String getAgentCodeById(String agentId) {
        String code = "";

        try {
            String selectQuery = "SELECT " + KEY_AGENT_CODE + " FROM " + TABLE_AGENTS + " WHERE " + KEY_AGENT_CODE + "='" + agentId + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    code = c.getString(c.getColumnIndex(KEY_AGENT_CODE));
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return code;
    }

    /**
     * Method to check the tadc sale is exists or not
     *
     * @param billNumber
     * @return
     */
    public int checkTdcSaleIsExistsOrNot(String billNumber) {
        int maxID = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_TDC_SALES_ORDERS
                + " WHERE " + KEY_TDC_SALES_ORDER_BILL_NUMBER + "='" + billNumber + "'";
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
     * Method to get all the delivered product ids list
     *
     * @param userId
     * @return
     */
    public ArrayList<String> getdeliveryDetailsPreviewProdIdsList(String userId) {

        ArrayList<String> arList = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE tripsheet_delivery_number  = '" + userId + "'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            arList = new ArrayList<>(c.getCount());
            if (c.moveToFirst()) {
                do {
                    arList.add(c.getString(c.getColumnIndex(KEY_TRIPSHEET_DELIVERY_PRODUCT_IDS)));

                } while (c.moveToNext());
            }


            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return arList;
    }

    public int checkProductExistsInTripSheetPaymentsTable(String saleOrderId, String agentId, String paymentNumber) {
        int noOfRecords = 0;

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NO + " FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST
                    + " WHERE " + KEY_TRIPSHEET_PAYMENTS_SO_ID + " = '" + saleOrderId + "' AND "
                    + KEY_TRIPSHEET_PAYMENTS_USER_ID + " = '" + agentId + "' AND "
                    + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER + " = '" + paymentNumber + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null) {
                cursor.moveToFirst();
                noOfRecords = cursor.getCount();
                cursor.close();
            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("PAY RECORDS::: " + noOfRecords);
        return noOfRecords;
    }


    /**
     * Method to get the count of existing deliveries.
     *
     * @return deliveries count
     */
    public String getTripsheetDeliveriesMaxOrderNumber(String tripsheetId, String soId, String isFrom) {
        String orderId = "";

        try {
            String countQuery = "";
            if (isFrom.equals("first")) {
                countQuery = "SELECT " + KEY_TRIPSHEET_DELIVERY_NUMBER + " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " WHERE " + KEY_TRIPSHEET_DELIVERY_TRIP_ID
                        + "='" + tripsheetId + "'" + " AND " + KEY_TRIPSHEET_DELIVERY_SO_ID + "='" + soId + "'" + " ORDER BY " + KEY_TRIPSHEET_DELIVERY_NUMBER + " DESC LIMIT 1";
            } else if (isFrom.equals("second")) {
                countQuery = "SELECT " + KEY_TRIPSHEET_DELIVERY_NUMBER + " FROM " + TABLE_TRIPSHEETS_DELIVERIES_LIST + " ORDER BY " + KEY_TRIPSHEET_DELIVERY_NUMBER + " DESC LIMIT 1";
            }

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            if (cursor.moveToFirst()) {
                orderId = cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_DELIVERY_NUMBER));
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderId;
    }

    /**
     * Method to get the count of existing deliveries.
     *
     * @return deliveries count
     */
    public String getTripsheetReturnsMaxOrderNumber(String tripsheetId, String soId, String isFrom) {
        String orderId = "";

        try {
            String countQuery = "";
            if (isFrom.equals("first")) {
                countQuery = "SELECT " + KEY_TRIPSHEET_RETURNS_RETURN_NUMBER + " FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " WHERE " + KEY_TRIPSHEET_RETURNS_TRIP_ID
                        + "='" + tripsheetId + "'" + " AND " + KEY_TRIPSHEET_RETURNS_SO_ID + "='" + soId + "'" + " ORDER BY " + KEY_TRIPSHEET_RETURNS_RETURN_NUMBER + " DESC LIMIT 1";
            } else if (isFrom.equals("second")) {
                countQuery = "SELECT " + KEY_TRIPSHEET_RETURNS_RETURN_NUMBER + " FROM " + TABLE_TRIPSHEETS_RETURNS_LIST + " ORDER BY " + KEY_TRIPSHEET_RETURNS_RETURN_NUMBER + " DESC LIMIT 1";
            }

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            if (cursor.moveToFirst()) {
                orderId = cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_RETURNS_RETURN_NUMBER));
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderId;
    }

    /**
     * Method to get the count of existing deliveries.
     *
     * @return deliveries count
     */
    public String getTripsheetPaymentsMaxOrderNumber(String tripsheetId, String soId, String isFrom) {
        String orderId = "";

        try {
            //String countQuery = "SELECT " + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER + " FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " ORDER BY " + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER + " DESC LIMIT 1";

            String countQuery = "";
            if (isFrom.equals("first")) {
                countQuery = "SELECT " + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER + " FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_TRIP_ID
                        + "='" + tripsheetId + "'" + " AND " + KEY_TRIPSHEET_PAYMENTS_SO_ID + "='" + soId + "'" + " ORDER BY " + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER + " DESC LIMIT 1";
            } else if (isFrom.equals("second")) {
                countQuery = "SELECT " + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER + " FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " ORDER BY " + KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER + " DESC LIMIT 1";
            }

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            if (cursor.moveToFirst()) {
                orderId = cursor.getString(cursor.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_PAYMENT_NUMBER));
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderId;
    }


    /**
     * Method to get the received amount type cash
     *
     * @param tripSheetId
     * @return
     */
    public ArrayList<String> fetchTripSheetReceivedAmounttypeCash(String tripSheetId) {
        ArrayList<String> cashTypeData = new ArrayList<String>();

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT + " FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " = '" + tripSheetId + "'"
                    + " AND " + KEY_TRIPSHEET_PAYMENTS_TYPE + "=0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            System.out.println("PAYMENTS TYPE ARRAY:: COUNT" + c.getCount());
            if (c.moveToFirst()) {
                do {
                    System.out.println("PAYMENTS TYPE ARRAY:: 1111");
                    cashTypeData.add(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("PAYMENTS TYPE ARRAY:: " + cashTypeData.size());
        return cashTypeData;
    }

    /**
     * Method to get the received amount type cheque
     *
     * @param tripSheetId
     * @return
     */
    public ArrayList<String> fetchTripSheetReceivedAmounttypeCheque(String tripSheetId) {
        ArrayList<String> cashTypeData = new ArrayList<String>();

        try {
            String selectQuery = "SELECT " + KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT + " FROM " + TABLE_TRIPSHEETS_PAYMENTS_LIST + " WHERE " + KEY_TRIPSHEET_PAYMENTS_TRIP_ID + " = '" + tripSheetId + "'"
                    + " AND " + KEY_TRIPSHEET_PAYMENTS_TYPE + "=1";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            System.out.println("PAYMENTS TYPE ARRAY CHEQ:: COUNT" + c.getCount());
            if (c.moveToFirst()) {
                do {
                    System.out.println("PAYMENTS TYPE ARRAY CHEQ:: 1111");
                    cashTypeData.add(c.getString(c.getColumnIndex(KEY_TRIPSHEET_PAYMENTS_RECEIVED_AMOUNT)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("PAYMENTS TYPE ARRAY CHEQ:: " + cashTypeData.size());
        return cashTypeData;
    }

    public ArrayList<String> fetchAllPendingTakeOrderAgentIds() {
        ArrayList<String> tripSheetIds = new ArrayList<>();

        try {
            String selectQuery = "SELECT DISTINCT " + KEY_TO_AGENTID + " FROM " + TABLE_TO_PRODUCTS + " WHERE " + KEY_TO_UPLOAD_STATUS + " = 0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    tripSheetIds.add(c.getString(c.getColumnIndex(KEY_TO_AGENTID)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripSheetIds;
    }


    /**
     * Method to fetch all records from take products table
     *
     * @param isSync
     * @param agentId
     */
    public ArrayList<TakeOrderBean> fetchAllPendingRecordsFromTakeOrderProductsTableByAgentId(String isSync, String agentId) {
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
                    toBean.setUom((c.getString(c.getColumnIndex(KEY_UOM))));
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

    /**
     * Method to insert device location details
     *
     * @param
     */
    public void insertDeviceOrUserLocationDetails(String deviceId, String userId, String date, String time, String latitude,
                                                  String longitude, String speed, String uploadFlag) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_DEVICE_ID_LATLANG, deviceId);
            values.put(KEY_DEVICE_USERID_LATLANG, userId);
            values.put(KEY_DEVICE_DATE_LATLANG, date);
            values.put(KEY_DEVICE_TIME_LATLANG, time);
            values.put(KEY_DEVICE_LAT_LATLANG, latitude);
            values.put(KEY_DEVICE_LANG_LATLANG, longitude);
            values.put(KEY_DEVICE_SPEED_LATLANG, speed);
            values.put(KEY_DEVICE_UPLOAD_FLAG_LATLANG, uploadFlag);
            // insert row
            db.insert(TABLE_DEVICE_LATLANG_TABLE, null, values);
            System.out.println("Device Location Details*********** INSERTED***************");
            values.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    /**
     * Method to get the unuploaded device location details.
     *
     * @param
     * @return
     */
    public ArrayList<String> fetchUnUploadedDeviceDetails() {
        ArrayList<String> deviceIdsList = new ArrayList<String>();
        try {
            String selectQuery = "SELECT " + KEY_DEVICE_ID_LATLANG + " FROM " +
                    TABLE_DEVICE_LATLANG_TABLE + " WHERE " + KEY_DEVICE_UPLOAD_FLAG_LATLANG + " = 0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    deviceIdsList.add(c.getString(c.getColumnIndex(KEY_DEVICE_ID_LATLANG)));
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DEVICE ID LIST::: " + deviceIdsList.size());
        return deviceIdsList;
    }

    /**
     * Method to get the unuploaded device location details.
     *
     * @param
     * @return
     */
    public ArrayList<DeviceDetails> fetchUnUploadedDeviceDetails1() {
        ArrayList<DeviceDetails> deviceIdsList = new ArrayList<DeviceDetails>();
        try {
            String selectQuery = "SELECT * FROM " +
                    TABLE_DEVICE_LATLANG_TABLE + " WHERE " + KEY_DEVICE_UPLOAD_FLAG_LATLANG + " = 0";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    DeviceDetails dbean = new DeviceDetails();

                    dbean.setmDeviceId(c.getString(c.getColumnIndex(KEY_DEVICE_ID_LATLANG)));
                    dbean.setmDeviceUserId(c.getString(c.getColumnIndex(KEY_DEVICE_USERID_LATLANG)));
                    dbean.setmDeviceDate(c.getString(c.getColumnIndex(KEY_DEVICE_DATE_LATLANG)));
                    dbean.setmDeviceTime(c.getString(c.getColumnIndex(KEY_DEVICE_TIME_LATLANG)));
                    dbean.setmDeviceLatitude(c.getString(c.getColumnIndex(KEY_DEVICE_LAT_LATLANG)));
                    dbean.setmDeviceLongitude(c.getString(c.getColumnIndex(KEY_DEVICE_LANG_LATLANG)));
                    dbean.setmDeviceSpeed(c.getString(c.getColumnIndex(KEY_DEVICE_SPEED_LATLANG)));
                    dbean.setmDeviceuploadFlag(c.getString(c.getColumnIndex(KEY_DEVICE_UPLOAD_FLAG_LATLANG)));

                    deviceIdsList.add(dbean);
                } while (c.moveToNext());
            }

            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceIdsList;
    }

    /**
     * Method to update the device location  uploaded status after calling API.
     */
    public void updateDeviceLocationUploadFlag(String userId, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_DEVICE_UPLOAD_FLAG_LATLANG, 1);

            int status = db.update(TABLE_DEVICE_LATLANG_TABLE, values,
                    KEY_DEVICE_USERID_LATLANG + " = ? AND " + KEY_DEVICE_DATE_LATLANG + " = ? AND " + KEY_DEVICE_TIME_LATLANG + " = ?",
                    new String[]{userId, date, time});

            System.out.println("DEVICE LOCATION TABLE UPDATED===== " + status);
            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }


    /**
     * Method to fetch all un uploaded records from Agents Table
     */
    public ArrayList<AgentsBean> fetchAllUnUploadedRecordsFromAgents() {
        ArrayList<AgentsBean> allTDCCustomersList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_AGENTS + " WHERE " + KEY_AGENT_UPLOAD_STATUS
                    + "='" + 0 + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    AgentsBean aBean = new AgentsBean();

                    aBean.setmAgentUniqueId(c.getString(c.getColumnIndex(KEY_AGENT_UNIQUE_ID)));
                    aBean.setmAgentId(c.getString(c.getColumnIndex(KEY_AGENT_ID)));
                    aBean.setmAgentName(c.getString(c.getColumnIndex(KEY_AGENT_NAME)));
                    aBean.setmObAmount(c.getString(c.getColumnIndex(KEY_OB_AMOUNT)));
                    aBean.setmOrderValue(c.getString(c.getColumnIndex(KEY_ORDER_VALUE)));
                    aBean.setmTotalAmount(c.getString(c.getColumnIndex(KEY_TOTAL_AMOUNT)));
                    aBean.setmDueAmount(c.getString(c.getColumnIndex(KEY_DUE_AMOUNT)));
                    aBean.setmStatus(c.getString(c.getColumnIndex(KEY_AGENT_STATUS)));
                    aBean.setmAgentPic(c.getString(c.getColumnIndex(KEY_AGENT_PIC)));
                    aBean.setmLatitude(c.getString(c.getColumnIndex(KEY_AGENT_LATITUDE)));
                    aBean.setmLongitude(c.getString(c.getColumnIndex(KEY_AGENT_LONGITUDE)));
                    aBean.setmAgentCode(c.getString(c.getColumnIndex(KEY_AGENT_CODE)));
                    aBean.setmFirstname(c.getString(c.getColumnIndex(KEY_AGENT_FIRSTNAME)));
                    aBean.setmLastname(c.getString(c.getColumnIndex(KEY_AGENT_LASTNAME)));
                    aBean.setMphoneNO(c.getString(c.getColumnIndex(KEY_AGENT_PHONENO)));
                    aBean.setmAgentRouteId(c.getString(c.getColumnIndex(KEY_AGENT_ROUTE_ID)));
                    aBean.setmPoiImage(c.getString(c.getColumnIndex(KEY_POI_IMAGE)));
                    aBean.setmPoaImage(c.getString(c.getColumnIndex(KEY_POA_IMAGE)));
                    aBean.setmAgentEmail(c.getString(c.getColumnIndex(KEY_AGENT_EMAIL)));
                    aBean.setmAgentPassword(c.getString(c.getColumnIndex(KEY_AGENT_PASSWORD)));
                    aBean.setmAgentStakeid(c.getString(c.getColumnIndex(KEY_AGENT_STAKEHOLDERID)));
                    aBean.setmAgentReprtingto(c.getString(c.getColumnIndex(KEY_AGENT_REPORTINGTO)));
                    aBean.setmAgentVerifycode(c.getString(c.getColumnIndex(KEY_AGENT_VERIFYCODE)));
                    aBean.setmAgentDelete(c.getString(c.getColumnIndex(KEY_AGENT_DELETE)));
                    aBean.setmAgentCreatedBy(c.getString(c.getColumnIndex(KEY_AGENT_CREATEDBY)));
                    aBean.setmAgentCreatedOn(c.getString(c.getColumnIndex(KEY_AGENT_CREATEDON)));
                    aBean.setmAgentUpdatedBy(c.getString(c.getColumnIndex(KEY_AGENT_UPDATEDBY)));
                    aBean.setmAgentUpdatedOn(c.getString(c.getColumnIndex(KEY_AGENT_UPDATEDON)));
                    aBean.setmAgentRoutecode(c.getString(c.getColumnIndex(KEY_AGENT_ROUTECODE)));
                    aBean.setmAgentDeviceSync(c.getString(c.getColumnIndex(KEY_AGENT_DEVICESYNC)));
                    aBean.setmAgentAccessDevice(c.getString(c.getColumnIndex(KEY_AGENT_ACCESSDEVICE)));
                    aBean.setmAgentBackUp(c.getString(c.getColumnIndex(KEY_AGENT_BACKUP)));

                    allTDCCustomersList.add(aBean);
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
     * Method to update the uploaded agent status
     *
     * @param customerId
     * @param userId
     */
    public void updateAgentUploadStatus(String customerId, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_AGENT_UPLOAD_STATUS, "1");
            values.put(KEY_AGENT_ID, userId);

            int status = db.update(TABLE_AGENTS, values, KEY_AGENT_UNIQUE_ID + " = ?", new String[]{String.valueOf(customerId)});

            values.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }


    /**
     * Pending indents insert method
     *
     * @param categories,liters,pendingcount,approvedcount,indentdate,indentTIme,createdDate
     */

    public void insertDashboardPendingIndentDetails(String categories, String ltrs, String pendingCount, String approvedCount, String indentDate, String indentTime,String createdDate) {
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_DATE, indentDate);
            values.put(KEY_CREATED_DATE, createdDate);
            values.put(KEY_TIME, indentTime);
            values.put(KEY_CATEGORY, categories);
            values.put(KEY_LITRES, ltrs);
            if (pendingCount == null) {
                values.put(KEY_PENDING_COUNT, "0");
            } else {
                values.put(KEY_PENDING_COUNT, pendingCount);
            }
            if (approvedCount == null) {
                values.put(KEY_APPROVED_COUNT, "0");
            } else {
                values.put(KEY_APPROVED_COUNT, approvedCount);
            }
            long rec = checkIndentExistsOrNot(indentDate);
            SQLiteDatabase db = this.getWritableDatabase();
            if (rec == 0) {
                // Insert row
                long l = db.insert(TABLE_DASHBOARD_PENDINGINDENTLIST, null, values);
                //System.out.println("DASHBOARD DATA INSERTED....." + l);
            } else {
                // Update row
                long l = db.update(TABLE_DASHBOARD_PENDINGINDENTLIST, values, KEY_DATE + " = ?",
                        new String[]{indentDate});
                //System.out.println("DASHBOARD DATA UPDATED....." + l);
            }
            values.clear();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to fetch all un uploaded records from Agents Table
     */
    public ArrayList<String> fetchDashboardPendingIndentDetails(String curDate) {
        ArrayList<String> allTDCCustomersList = new ArrayList<String>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_DASHBOARD_PENDINGINDENTLIST + " WHERE " + KEY_DATE
                    + "='" + curDate + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_CATEGORY)));//0 categories
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_LITRES)));//1 liters
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_PENDING_COUNT)));//2 pendingcount
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_APPROVED_COUNT)));//3 approvedcount
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_CREATED_DATE)));//4 date
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_TIME)));//5 time

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allTDCCustomersList;
    }

    public int checkIndentExistsOrNot(String date) {
        int noOfRecords = 0;

        try {
            String selectQuery = "SELECT " + KEY_DATE + " FROM " + TABLE_DASHBOARD_PENDINGINDENTLIST
                    + " WHERE " + KEY_DATE + " = '" + date + "'";

            SQLiteDatabase db1 = this.getReadableDatabase();
            Cursor cursor = db1.rawQuery(selectQuery, null);

            if (cursor != null) {
                cursor.moveToFirst();
                noOfRecords = cursor.getCount();
                cursor.close();
            }

            db1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfRecords;
    }



    /**
     * dashboard deliveries insert method
     *
     * @param categories,ordered,delivered,percentage,indentdate,indentTIme,createdDate
     */

    public void insertDashboardDeliveryDetails(String categories, String ordered, String delivered, String percentage, String indentDate, String indentTime,String createdDate) {
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_DASHBOARD_DELIVERY_DATE, indentDate);
            values.put(KEY_DASHBOARD_DELIVERY_CREATED_DATE, createdDate);
            values.put(KEY_DASHBOARD_DELIVERY_TIME, indentTime);
            values.put(KEY_DASHBOARD_DELIVERY_CATEGORY, categories);
            values.put(KEY_DASHBOARD_DELIVERY_ORDERVALUE, ordered);
            values.put(KEY_DASHBOARD_DELIVERY_DELIVERYVALUE,delivered);
            values.put(KEY_DASHBOARD_DELIVERY_PERCENTAGEVALUE,percentage);

            long rec = checkDeliveriesExistsOrNot(indentDate);
            SQLiteDatabase db = this.getWritableDatabase();
            if (rec == 0) {
                // Insert row
                long l = db.insert(TABLE_DASHBOARD_DELIVERYLIST, null, values);
                //System.out.println("DASHBOARD DATA INSERTED....." + l);
            } else {
                // Update row
                long l = db.update(TABLE_DASHBOARD_DELIVERYLIST, values, KEY_DASHBOARD_DELIVERY_DATE + " = ?",
                        new String[]{indentDate});
                //System.out.println("DASHBOARD DATA UPDATED....." + l);
            }
            values.clear();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to fetch all un uploaded records from Agents Table
     */
    public ArrayList<String> fetchDashboardDeliveriesDetails(String curDate) {
        ArrayList<String> allTDCCustomersList = new ArrayList<String>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_DASHBOARD_DELIVERYLIST + " WHERE " + KEY_DASHBOARD_DELIVERY_DATE
                    + "='" + curDate + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_DASHBOARD_DELIVERY_CATEGORY)));//0 categories
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_DASHBOARD_DELIVERY_ORDERVALUE)));//1 liters
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_DASHBOARD_DELIVERY_DELIVERYVALUE)));//2 pendingcount
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_DASHBOARD_DELIVERY_PERCENTAGEVALUE)));//3 approvedcount
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_DASHBOARD_DELIVERY_CREATED_DATE)));//4 date
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_DASHBOARD_DELIVERY_TIME)));//5 time

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allTDCCustomersList;
    }

    public int checkDeliveriesExistsOrNot(String date) {
        int noOfRecords = 0;

        try {
            String selectQuery = "SELECT " + KEY_DASHBOARD_DELIVERY_DATE + " FROM " + TABLE_DASHBOARD_DELIVERYLIST
                    + " WHERE " + KEY_DASHBOARD_DELIVERY_DATE + " = '" + date + "'";

            SQLiteDatabase db1 = this.getReadableDatabase();
            Cursor cursor = db1.rawQuery(selectQuery, null);

            if (cursor != null) {
                cursor.moveToFirst();
                noOfRecords = cursor.getCount();
                cursor.close();
            }

            db1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfRecords;
    }


    /**
     * dashboard payments insert method
     *
     * @param delivered,received,due,indentdate,indentTIme,createdDate
     */

    public void insertDashboardPaymentsDetails(String delivered, String received, String due, String indentDate, String indentTime,String createdDate) {
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_DASHBOARD_PAYMENTS_DATE, indentDate);
            values.put(KEY_DASHBOARD_PAYMENT_CREATED_DATE, createdDate);
            values.put(KEY_DASHBOARD_PAYMENT_TIME, indentTime);

            if (delivered == null) {
                values.put(KEY_DASHBOARD_PAYMENTS_DELIVERYVALUE, "0");
            } else {
                values.put(KEY_DASHBOARD_PAYMENTS_DELIVERYVALUE, delivered);
            }
            if (received == null) {
                values.put(KEY_DASHBOARD_PAYMENTS_RECEIVEDVALUE, "0");
            } else {
                values.put(KEY_DASHBOARD_PAYMENTS_RECEIVEDVALUE, received);
            }

            if (due == null) {
                values.put(KEY_DASHBOARD_PAYMENTS_DUEVALUE, "0");
            } else {
                values.put(KEY_DASHBOARD_PAYMENTS_DUEVALUE, due);
            }
            long rec = checkPaymentsExistsOrNot(indentDate);
            SQLiteDatabase db = this.getWritableDatabase();
            if (rec == 0) {
                // Insert row
                long l = db.insert(TABLE_DASHBOARD_PAYMENTLIST, null, values);
                //System.out.println("DASHBOARD DATA INSERTED....." + l);
            } else {
                // Update row
                long l = db.update(TABLE_DASHBOARD_PAYMENTLIST, values, KEY_DASHBOARD_PAYMENTS_DATE + " = ?",
                        new String[]{indentDate});
                //System.out.println("DASHBOARD DATA UPDATED....." + l);
            }
            values.clear();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to fetch all un uploaded records from Agents Table
     */
    public ArrayList<String> fetchDashboardPaymentsDetails(String curDate) {
        ArrayList<String> allTDCCustomersList = new ArrayList<String>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_DASHBOARD_PAYMENTLIST + " WHERE " + KEY_DASHBOARD_PAYMENTS_DATE
                    + "='" + curDate + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_DASHBOARD_PAYMENTS_DELIVERYVALUE)));//0 categories
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_DASHBOARD_PAYMENTS_RECEIVEDVALUE)));//1 liters
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_DASHBOARD_PAYMENTS_DUEVALUE)));//2 pendingcount

                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_DASHBOARD_PAYMENT_CREATED_DATE)));//4 date
                    allTDCCustomersList.add(c.getString(c.getColumnIndex(KEY_DASHBOARD_PAYMENT_TIME)));//5 time

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allTDCCustomersList;
    }

    public int checkPaymentsExistsOrNot(String date) {
        int noOfRecords = 0;

        try {
            String selectQuery = "SELECT " + KEY_DASHBOARD_PAYMENTS_DATE + " FROM " + TABLE_DASHBOARD_PAYMENTLIST
                    + " WHERE " + KEY_DASHBOARD_PAYMENTS_DATE + " = '" + date + "'";

            SQLiteDatabase db1 = this.getReadableDatabase();
            Cursor cursor = db1.rawQuery(selectQuery, null);

            if (cursor != null) {
                cursor.moveToFirst();
                noOfRecords = cursor.getCount();
                cursor.close();
            }

            db1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfRecords;
    }



    public void insertPendingIndentMoreInfoDetails(Nextdayindent_moreinfoBeen moreinfoBeen, String date){
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NEXTINDENT_MOREINFO_MILKVOL, moreinfoBeen.getMilkVol());
            values.put(KEY_NEXTINDENT_MOREINFO_CURDVOL, moreinfoBeen.getCurdVol());
            values.put(KEY_NEXTINDENT_MOREINFO_OTHERSVOL, moreinfoBeen.getOtherVol());
            values.put(KEY_NEXTINDENT_MOREINFO_SELDATE, moreinfoBeen.getDate());
            long rec = checkmoreinfoIndentExistsOrNot(moreinfoBeen.getDate());
            SQLiteDatabase db = this.getWritableDatabase();
            long long_response=0;
            if (rec == 0) {
                long_response = db.insert(TABLE_NEXTINDENT_MOREINFO, null, values);
            }
            else
            {
                long_response = db.update(TABLE_NEXTINDENT_MOREINFO, values, KEY_NEXTINDENT_MOREINFO_SELDATE + " = ?",
                        new String[]{moreinfoBeen.getDate()});
            }
            System.out.println("TABLE_NEXTINDENT_MOREINFO DATA INSERTED....."+long_response);
            values.clear();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int checkmoreinfoIndentExistsOrNot(String sdate) {
        int noOfRecords = 0;

        try {
            Cursor c = null;
            SQLiteDatabase db = this.getReadableDatabase();
            c = db.rawQuery("SELECT * FROM " + TABLE_NEXTINDENT_MOREINFO + " WHERE " + KEY_NEXTINDENT_MOREINFO_SELDATE   + "='" + sdate + "'", null);
           if (c != null) {
                c.moveToFirst();
                noOfRecords = c.getCount();
                c.close();
            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfRecords;
    }
    public ArrayList<Nextdayindent_moreinfoBeen> fetchPendingIndentMoreInfoDetails() {
        ArrayList<Nextdayindent_moreinfoBeen> moreInfoList = new ArrayList<>();

        try {

            String selectQuery = "SELECT * FROM " + TABLE_NEXTINDENT_MOREINFO ;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    Nextdayindent_moreinfoBeen moreinfoBeen = new Nextdayindent_moreinfoBeen();
                    moreinfoBeen.setDate(c.getString(c.getColumnIndex(KEY_NEXTINDENT_MOREINFO_SELDATE)));
                    moreinfoBeen.setMilkVol(c.getString(c.getColumnIndex(KEY_NEXTINDENT_MOREINFO_MILKVOL)));
                    moreinfoBeen.setCurdVol(c.getString(c.getColumnIndex(KEY_NEXTINDENT_MOREINFO_CURDVOL)));
                    moreinfoBeen.setOtherVol(c.getString(c.getColumnIndex(KEY_NEXTINDENT_MOREINFO_OTHERSVOL)));

                    moreInfoList.add(moreinfoBeen);

                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return moreInfoList;
    }
}
