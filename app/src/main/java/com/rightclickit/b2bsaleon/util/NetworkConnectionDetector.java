package com.rightclickit.b2bsaleon.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;

/**
 * @author venkat
 * @version 1.0
 *          <p/>
 *          NetworkConnectionDetector is a class to find the Internet Connectivity
 *          status. The Constructor needs context of the Activity class.
 *          <p/>
 *          Must define the permissions in Application Manifest file
 *          <uses-permission android:name="android.permission.INTERNET" />
 *          <uses-permission
 *          android:name="android.permission.ACCESS_NETWORK_STATE" />
 */
public class NetworkConnectionDetector {

    private Context _context;

    /**
     * Constructor
     *
     * @param activity Activity context
     */
    public NetworkConnectionDetector(Context activity) {
        _context = activity;
    }

    /**
     * Checks the Internet Connectivity status
     *
     * @return Boolean
     */
    public boolean isNetworkConnected() {
        boolean status = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

            if (activeNetwork != null) {
                status = true;
            } else {
                // not connected to the internet
                status = false;
            }
        }

        /*if (!status) {
            displayNoNetworkError(context);
        }*/
        return status;
    }

    /**
     *
     */
    public static void displayNoNetworkError(Context context) {
        if (CustomProgressDialog.isProgressDialogShown)
            CustomProgressDialog.hideProgressDialog();
        CustomAlertDialog.showAlertDialog(context, context.getResources().getString(R.string.error_no_internet_title), context.getResources().getString(R.string.error_no_internet));
    }
}