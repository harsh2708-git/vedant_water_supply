package com.production.vedantwatersupply.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.production.vedantwatersupply.BuildConfig;

public class SharedPreferenceUtil {
    private static final String SP_NAME = BuildConfig.APPLICATION_ID;

    public static void setPreference(Context context, String key, String value) {
        setPreference(context, SP_NAME, key, value);
    }

    public static void setPreference(Context context, String prefName, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }


    public static void setPreference(Context context, String key, int value) {
        setPreference(context, SP_NAME, key, value);
    }

    public static void setPreference(Context context, String prefName, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }


    public static void setPreference(Context context, String key, boolean value) {
        setPreference(context, SP_NAME, key, value);
    }

    public static void setPreference(Context context, String prefName, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }


    public static String getPreference(Context context, String key, String defaultValue) {

        return getPreference(context, SP_NAME, key, defaultValue);
    }

    public static String getPreference(Context context, String prefName, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }


    public static int getPreference(Context context, String key, int defaultValue) {
        return getPreference(context, SP_NAME, key, defaultValue);
    }

    public static int getPreference(Context context, String prefName, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static boolean getPreference(Context context, String key, boolean defaultValue) {
        return getPreference(context, SP_NAME, key, defaultValue);
    }

    public static boolean getPreference(Context context, String prefName, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void clearData(Context context) {
        clearData(context, SP_NAME);
    }

    public static void clearData(Context context, String prefName) {
        SharedPreferences sp = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}