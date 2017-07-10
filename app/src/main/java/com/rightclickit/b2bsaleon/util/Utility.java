package com.rightclickit.b2bsaleon.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by venkat.
 *
 * @author venkat
 */
public class Utility {

    private static Utility instance;

    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static Utility getInstance() {
        if (instance == null)
            instance = new Utility();
        return instance;
    }

    public static void displayToast(Context context, String message, int length) {
        Toast.makeText(context, message, length).show();
    }

    public static void logResult(String tag, String message) {
        Log.d(tag, message);
    }

    public static boolean isLandscapeOrientation(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return true;
        else
            return false;
    }

    public static int getDeviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    //Method to convert UTC time to Local time
    public static String formatTime(long timeInMillSecs, String format) {
        Date utcTime = new Date(timeInMillSecs);
        DateFormat df = new SimpleDateFormat(format);
        return df.format(utcTime);
    }

    public static String formatDate(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static Date convertStringToDate(String stringDate, String format) {
        Date date = null;
        try {
            DateFormat df = new SimpleDateFormat(format);
            date = df.parse(stringDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public static boolean isValidEmail(String emailId) {
        boolean flag = false;

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailId);

        if (matcher.matches())
            flag = true;
        else
            flag = false;

        return flag;
    }

    /**
     * @param values
     * @param delimiter
     * @return
     */
    public static String convertArrayListToString(ArrayList values, String delimiter) {
        StringBuilder sb = new StringBuilder();

        for (Object obj : values) {
            sb.append(obj);

            if (values.size() > 1)
                sb.append(delimiter);
        }

        if (values.size() > 1)
            sb.setLength(sb.length() - 1);

        return sb.toString();
    }

    /**
     * Method to get md5 hash from the given string.
     *
     * @param s
     * @return md5 hash string
     */

    public static String getMd5String(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getFormattedCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return formatter.format(amount);
    }

    public static String getFormattedNumber(double number) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("en", "IN"));
        return formatter.format(number);
    }
}
