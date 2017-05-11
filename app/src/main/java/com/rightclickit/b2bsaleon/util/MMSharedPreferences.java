package com.rightclickit.b2bsaleon.util;

import android.content.Context;

import com.rightclickit.b2bsaleon.constants.Constants;

/**
 * @author venkat
 *         <p/>
 *         Used to maintain user session using SharedPreference.
 */
public class MMSharedPreferences {

    private android.content.SharedPreferences sp;
    private android.content.SharedPreferences.Editor editor;

    /**
     * @param context
     */

    public MMSharedPreferences(Context context) {
        if (this.sp == null)
            this.sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    /**
     * Constructor
     *
     * @param context              Activity class Context
     * @param shared_pref_filename Filename of the shared preference
     */
    public MMSharedPreferences(Context context, String shared_pref_filename) {
        if (this.sp == null)
            this.sp = context.getSharedPreferences(shared_pref_filename, Context.MODE_PRIVATE);
    }

    /**
     * @return Boolean
     */
    public boolean isLogin() {
        String user_id = this.getString("user_id");
        if (!user_id.equals("No Data found")) {
            return true;
        }
        return false;
    }

    /**
     * Method to store the inputs to shared preference
     *
     * @param key   Stored key
     * @param value Stored value
     */
    public void putString(String key, String value) {
        this.editor = this.sp.edit();
        this.editor.putString(key, value);
        this.editor.commit();
    }

    /**
     * Method to get the stored value based on a input key
     *
     * @param key Stored key
     * @return String
     */
    public String getString(String key) {
        return this.sp.getString(key, "");
    }

    public void putInt(String key, int value) {
        this.editor = this.sp.edit();
        this.editor.putInt(key.toString(), value);
        this.editor.commit();
    }

    public int getInt(String key) {
        return this.sp.getInt(key, -1);
    }

    /**
     * Method to remove specific stored value based on key
     *
     * @param key Stored key
     */
    public void remove(String key) {
        this.editor = this.sp.edit();
        this.editor.remove(key);
        this.editor.commit();
    }

    /**
     * Method to clear all the stored data in shared preference
     */
    public void clear() {
        this.editor = this.sp.edit();
        this.editor.clear();
        this.editor.commit(); // commit changes
    }

    public boolean getBoolean(String key) {
        return this.sp.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        this.editor = this.sp.edit();
        this.editor.putBoolean(key, value);
        this.editor.commit();
    }
}
