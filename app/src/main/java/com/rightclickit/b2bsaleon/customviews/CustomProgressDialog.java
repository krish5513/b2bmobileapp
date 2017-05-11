package com.rightclickit.b2bsaleon.customviews;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

import com.rightclickit.b2bsaleon.R;

/**
 * @author venkat
 */
public class CustomProgressDialog {
    private static ProgressDialog progressDialog;
    public static boolean isProgressDialogShown = false;

    /**
     * @param ctx Context
     * @param msg message
     */
    public static void showProgressDialog(Context ctx, String msg) {
        try {
            progressDialog = new ProgressDialog(ctx, R.style.ProgressDialogTheme);
            progressDialog.setMessage(msg);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            progressDialog.show();
            isProgressDialogShown = true;
        } catch (Exception e) {
            isProgressDialogShown = false;
            e.printStackTrace();
        }
    }

    /**
     * Method to hide the dialog.
     */
    public static void hideProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                isProgressDialogShown = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
