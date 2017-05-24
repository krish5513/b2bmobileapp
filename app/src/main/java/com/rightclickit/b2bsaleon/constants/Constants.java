package com.rightclickit.b2bsaleon.constants;

import android.os.Environment;

/**
 * Created by venkat
 *
 * @author venkat
 */
public class Constants {
    public static final String databasePath = Environment.getExternalStorageDirectory().toString() + "/B2bSaleOn/Database/";

    public static final String LOADING_MESSAGE = "Please wait..";

    // SHARED PREFERENCE CONSTANTS
    public static final String SHARED_PREFERENCE = "B2BSaleOnPrefs";

    // Bundle Constants
    public static final String BUNDLE_REQUEST_FROM = "RequestFrom";

    public static final int VOLLEY_SOCKET_TIMEOUT = 30000; // 120 seconds.

    // Fonts
    public static final String FONT_NAME_REGULAR = "fonts/Swiss-721-BT-Roman.ttf";
    public static final String FONT_NAME_BOLD = "fonts/Swiss-721-BT-Bold.ttf";

    public enum RequestCode {
        DEFAULT
    }

    public enum RequestFrom {
        LOGIN_PAGE
    }

    /* All Application Service URL's Listed Below */

    /**
     * Port number
     */
    public static final String PORT_LOGIN = "3003";

    /**
     * Port number 1
     */
    public static final String PORT_ROUTES_MASTER_DATA = "3000";

    /**
     * Port for user previleges.
     */
    public static final String PORT_USER_PREVILEGES = "3001";

    //Production
    public static final String MAIN_URL = "http://52.10.51.54:";

    public static final String APP_TYPE = "A";

    //Service Response Codes
    public static final int SUCCESS = 200; // Operation Successful. Please use the response object.
    public static final int FAILURE = 401; // Operation Successful. Please use the response object.
    public static final int ERROR = 400; // Bad Request. Error occurred while processing the request.

    //Log In
    public static final String LOGIN_SERVICE = "/user/login";
    // Routes List master
    public static final String ROUTEID_SERVICE = "/route/list";
    // User previleges
    public static final String GET_USER_PREVILEGES_SERVICE = "/usergroup/form_list?";
    // Chnage Password {pass user id parameter in url}
    public static final String CHANGE_PASSWORD_SERVICE = "/user/edit/";

}
