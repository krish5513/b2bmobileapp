package com.rightclickit.b2bsaleon.customviews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.interfaces.AlertDialogSelectionListener;

/**
 * Created by venkat on 22/06/16.
 */
public class CustomAlertDialog {

    public static AlertDialog alertDialog = null;
    public static boolean isAlertDialogShown = false;
    public static AlertDialogSelectionListener listener = null;

    public void setListener(AlertDialogSelectionListener listener) {
        this.listener = listener;
    }

    /**
     * @param title
     * @param message
     */
    public static void showAlertDialog(Context context, String title, String message) {
        try {
           // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

            android.support.v7.app.AlertDialog alertDialog = null;
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    CustomProgressDialog.hideProgressDialog();
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            isAlertDialogShown = true;

        } catch (Exception e) {
            isAlertDialogShown = false;
            e.printStackTrace();
        }
    }

    public static void showAlertDialogWithCancelButton(Context context, String title, String message) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            isAlertDialogShown = true;

            Button cancelButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (cancelButton != null)
                cancelButton.setTextColor(ContextCompat.getColor(context, R.color.gray));

        } catch (Exception e) {
            isAlertDialogShown = false;
            e.printStackTrace();
        }
    }

    public static void showAlertDialogWithCallback(Context context, String title, String message) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    okCallback();
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            isAlertDialogShown = true;

            Button cancelButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (cancelButton != null)
                cancelButton.setTextColor(ContextCompat.getColor(context, R.color.gray));

        } catch (Exception e) {
            isAlertDialogShown = false;
            e.printStackTrace();
        }
    }

    public static void okCallback() {
        if (listener != null) {
            listener.alertDialogCallback();
        }
    }
}
