package com.ufxmeng.je.funradio.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JE on 6/4/2016.
 */
public class PrefUtils {

    Context mContext;
    private static PrefUtils sUtils;

    public static final String PREF_FUN_RADIO = "com.ufxmeng.je.funradio";
    public static final String PREF_KEY_CONNECTED = "pref_key_connected";
    public static final String PREF_KEY_CURRENTSTATION = "pref_key_current_station";
    private final SharedPreferences mPreferences;
    private final SharedPreferences.Editor mEditor;

    public static PrefUtils getInstance(Context context) {

        if (sUtils == null) {
            sUtils = new PrefUtils(context);
        }
        return sUtils;
    }

    private PrefUtils(Context context) {
        mContext = context;
        mPreferences = mContext.getSharedPreferences(PREF_FUN_RADIO, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public boolean getIsNetworkConnected() {

        return mPreferences.getBoolean(PREF_KEY_CONNECTED, false);
    }

    public void setIsNetworkConnected(boolean isNetworkConnected) {
        mEditor.putBoolean(PREF_KEY_CONNECTED, isNetworkConnected);
        mEditor.apply();
    }

    public int getCurrentstation() {
        return mPreferences.getInt(PREF_KEY_CURRENTSTATION, 4);
    }

    public void setCurrentstation(int position) {
        mEditor.putInt(PREF_KEY_CURRENTSTATION, position);
        mEditor.apply();
    }
}
