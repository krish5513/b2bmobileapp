package com.rightclickit.b2bsaleon.constants;

import android.os.Environment;

/**
 * Created by venkat
 *
 * @author venkat
 */
public class Constants {
    public static final String databasePath = Environment.getExternalStorageDirectory().toString() + "/B2bSaleOn/Database/";
    public static final String shopImagesPath = Environment.getExternalStorageDirectory().toString() + "/B2bSaleOn/ShopImages/";
    public static final String TripSheetPaymentChequesPath = Environment.getExternalStorageDirectory().toString() + "/B2bSaleOn/TripsSheetPaymentCheques/";

    public static final String LOADING_MESSAGE = "Please wait..";

    // SHARED PREFERENCE CONSTANTS
    public static final String SHARED_PREFERENCE = "B2BSaleOnPrefs";

    // Bundle Constants
    public static final String BUNDLE_REQUEST_FROM = "RequestFrom";
    public static final String BUNDLE_REQUEST_FROM_TDC_CUSTOMER_SELECTION = "TDCCustomersSelection";
    public static final String BUNDLE_REQUEST_FROM_TDC_SALES_LIST = "TDCSalesList";
    public static final String BUNDLE_REQUEST_FROM_RETAILER_PAYMENTS_LIST = "TDCCustomerPayments";
    public static final String BUNDLE_TDC_SALE_ORDER = "TDCSaleOrder";
    public static final String BUNDLE_TDC_SALE_ORDER_SALE_QUNAT = "TDCSaleOrderQuantity";
    public static final String BUNDLE_TDC_SALE_CURRENT_ORDER_PREVIEW = "TDCSaleCurrentOrder_Preview";
    public static final String BUNDLE_TDC_SALE_QUANTITY = "TDCSaleCurrentOrder_SaleQuantity";
    public static final String BUNDLE_TDC_SALE_CURRENT_ORDER = "TDCSaleCurrentOrder";
    public static final String BUNDLE_TDC_SALE_CURRENT_SALEQUNATITY = "TDCSaleCurrentSaleQuantity";
    public static final String BUNDLE_TDC_CUSTOMER = "TDCCustomer";
    public static final String BUNDLE_SELECTED_CUSTOMER_ID = "CustomerId";

    public static final int VOLLEY_SOCKET_TIMEOUT = 30000; // 120 seconds.

    // Fonts
    public static final String FONT_NAME_REGULAR = "fonts/Swiss-721-BT-Roman.ttf";
    public static final String FONT_NAME_BOLD = "fonts/Swiss-721-BT-Bold.ttf";

    public static final String TDC_SALE_INFO_DATE_DISPLAY_FORMAT = "yyyy-MM-dd HH:mm a";
    public static final String TDC_SALES_LIST_DATE_DISPLAY_FORMAT = "yyyy-MM-dd";
    public static final String TDC_SALES_ORDER_DATE_SAVE_FORMAT = "yyyy-MM-dd";
    public static final String SEND_DATA_TO_SERVICE_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SEND_DATA_TO_SERVICE_DATE_FORMAT = "yyyy-MM-dd";
    public static final String TRIP_SHEETS_DELIVERY_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String GOOGLE_DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s";

    public enum RequestCode {
        DEFAULT
    }

    public enum RequestFrom {
        LOGIN_PAGE
    }

    /* All Application Service URL's Listed Below */
   /*
     *//* New port numbers *//*
     public static final String PORT_LOGIN = ":3211";

    Remaining all
     ":3210";*/
    /**
     * Port number
     */
  /* public static final String PORT_LOGIN = ":3003";
    public static final String PORT_ADD = ":3007";


    public static final String PORT_ROUTES_MASTER_DATA = ":3000";


    public static final String PORT_USER_PREVILEGES = ":3001";


    public static final String PORT_AGENTS_LIST = ":3004";


    public static final String PORT_PRODUCTSLIST = ":3006";

    public static final String SYNC_TAKE_ORDERS_PORT = ":3008";

    public static final String SYNC_TRIPSHEETS_PORT = ":3009";

    //Port for notifications list
    public static final String SYNC_NOTIFICATIONS_PORT = ":3010";*/
















/* Quality Server*/




public static final String PORT_LOGIN = ":3211";
    public static final String PORT_ADD = ":3210";


    public static final String PORT_ROUTES_MASTER_DATA = ":3210";


    public static final String PORT_USER_PREVILEGES = ":3210";


    public static final String PORT_AGENTS_LIST = ":3210";


    public static final String PORT_PRODUCTSLIST = ":3210";

    public static final String SYNC_TAKE_ORDERS_PORT = ":3210";

    public static final String SYNC_TRIPSHEETS_PORT = ":3210";

    //Port for notifications list
    public static final String SYNC_NOTIFICATIONS_PORT = ":3210";


















/*Live Server*/






/*
 public static final String PORT_LOGIN = ":4001";
    public static final String PORT_ADD = ":4000";


    public static final String PORT_ROUTES_MASTER_DATA = ":4000";


    public static final String PORT_USER_PREVILEGES = ":4000";


    public static final String PORT_AGENTS_LIST = ":4000";


    public static final String PORT_PRODUCTSLIST = ":4000";

    public static final String SYNC_TAKE_ORDERS_PORT = ":4000";

    public static final String SYNC_TRIPSHEETS_PORT = ":4000";

    //Port for notifications list
    public static final String SYNC_NOTIFICATIONS_PORT = ":4000";
*/








   /*Amazon Server*/
/*

    public static final String PORT_LOGIN = ":3001";
    public static final String PORT_ADD = ":3000";


    public static final String PORT_ROUTES_MASTER_DATA = ":3000";


    public static final String PORT_USER_PREVILEGES = ":3000";


    public static final String PORT_AGENTS_LIST = ":3000";


    public static final String PORT_PRODUCTSLIST = ":3000";

    public static final String SYNC_TAKE_ORDERS_PORT = ":3000";

    public static final String SYNC_TRIPSHEETS_PORT = ":3000";

    //Port for notifications list
    public static final String SYNC_NOTIFICATIONS_PORT = ":3000";
*/







  /*  //Production
    public static final String MAIN_URL = "http://111.93.17.12/tmppl_live";
*/



    //Production
    public static final String MAIN_URL = "http://111.93.17.12";


    //Production for Agent stock
    public static final String MAIN_URL_STOCK = "http://111.93.17.12";

    //Development
    //public static final String MAIN_URL = "http://52.10.51.54";



    public static final String APP_TYPE = "A";

    //Service Response Codes
    public static final int SUCCESS = 200; // Operation Successful. Please use the response object.
    public static final int FAILURE = 401; // Operation Successful. Please use the response object.
    public static final int ERROR = 400; // Bad Request. Error occurred while processing the request.

    // Privilege static code
    public static final String PRIVILEGE_CODE = "&code=590b0e02a9fc1b13fcf967cf";

    //Log In
    public static final String LOGIN_SERVICE = "/user/login";
    // Routes List master
    public static final String ROUTEID_SERVICE = "/route/list/by_user_routes";
    // User previleges 4&\n

    //Quality
   public static final String GET_USER_PREVILEGES_SERVICE = "/b2b_quality/common/getactions_mobile?stake_id=";
    // Dashboard nextdayindent url
    public static final String GET_DASHBOARD_REPORTS_URL = "/b2b_quality/common/mobile_report";
    // Dashboard Deliveries url
    public static final String GET_DASHBOARD_DELIVERIES_URL = "/b2b_quality/common/mobile_report1";
    // Dashboard Payments url
    public static final String GET_DASHBOARD_PAYMENTS_URL = "/b2b_quality/common/mobile_report2";

    public static final String GET_DASHBOARD_NEXTINDENTMOREINFO_URL = "/b2b_quality/common/pending_pr_detailed_report";


    //Live

  /* public static final String GET_USER_PREVILEGES_SERVICE = "/tmppl_live/common/getactions_mobile?stake_id=";
    // Dashboard nextdayindent url
    public static final String GET_DASHBOARD_REPORTS_URL = "/tmppl_live/common/mobile_report";
    // Dashboard Deliveries url
    public static final String GET_DASHBOARD_DELIVERIES_URL = "/tmppl_live/common/mobile_report1";
    // Dashboard Payments url
    public static final String GET_DASHBOARD_PAYMENTS_URL = "/tmppl_live/common/mobile_report2";

    public static final String GET_DASHBOARD_NEXTINDENTMOREINFO_URL = "/tmppl_live/common/pending_pr_detailed_report";*/


    // public static final String GET_USER_PREVILEGES_SERVICE = "/b2b_demo/common/getactions_mobile?stake_id=";
    // Chnage Password {pass user id parameter in url}
    public static final String CHANGE_PASSWORD_SERVICE = "/user/edit/";
    //Get routeid and transporter
    public static final String SAVE_DEVICE_DETAILS = "/device/add";
    // Get Stake holders ids list Url
    public static final String GET_STAKE_HOLDERS_LIST = "/usergroup/stakeholder_list";
    // Get Customers list Url
    public static final String GET_CUSTOMERS_LIST = "/customer/list";
    // Get Customers list Url
    public static final String GET_CUSTOMERS_ADD = "/customer/add";

    // Products api url
    public static final String PRODUCTSLIST_SERVICE = "/product/document_region";
    // Sync take orders data url
    public static final String SYNC_TAKE_ORDERS_SERVICE = "/pending_order/add?token=";
    //Special Price url
    public static final String SPECIAL_PRICE_SERVICE = "/user_sprice_price/list";
    //TDC Sales Order Add URL
    public static final String TDC_SALES_ORDER_ADD = "/tdc_customer_order/add";

    //Tripsheets List URL
    public static final String GET_TRIPSHEETS_LIST = "/tripsheet/list_history";

    //Tripsheets Stock List
    public static final String GET_TRIPSHEETS_STOCK_LIST = "/tripsheet/stock";

    //Tripsheets Sale Orders List
    public static final String GET_TRIPSHEETS_SO_LIST = "/tripsheet/so";

    //Get Notifications List  URL
    public static final String GET_NOTIFICATIONS_LIST = "/notification/list/";

    // TripsheetsPaymentsURL
    public static final String TRIPSHEETS_PAYMENTS_URL = "/payment_orders/add";

    // Tripsheets Deliveries URL
    public static final String TRIPSHEETS_DELIVERIES_URL = "/delivery_orders/add/";

    // Tripsheets Returns URL
    public static final String TRIPSHEETS_RETURNS_URL = "/return_orders/add";

    // Agents Stock URL
    public static final String AGENTS_STOCK_URL = "/customer/stock";

    // Agents Stock Delivery URL
    public static final String AGENTS_STOCK_DELIVERY_URL = "/delivery_orders_no_tripid/add/";

    // Agents approved orders
    public static final String AGENTS_APPROVED_ORDERS = "/approved_orders/byid";

    public static final String AGENTS_PENDING_ORDERS = "/pending_orders/byid";

    // Retailers tdc sales list
    public static final String RETAILERS_TDC_SALESLIST = "/tdc_sales/list";

    // Agent Deliveries List
    public static final String AGENT_DELIVERIES_LIST = "/delivery_orders/byid";


    // Agent Returns list
    public static final String AGENT_RETURNS = "/return_orders/byid";



    // Agent Payments list
    public static final String AGENT_PAYMENTS = "/payment_orders/byid";
    // Close trip API URL
    public static final String CLOSE_TRIP_API_URL = "/tripsheet/stock/update1";

    // Device location details url
    public static final String DEVICE_LOCATION_DETAILS_URL = "/device/log_add";


}
