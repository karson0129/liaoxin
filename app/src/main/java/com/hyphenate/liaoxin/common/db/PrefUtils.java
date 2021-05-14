package com.hyphenate.liaoxin.common.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * @description Preference 工具类
 */
public class PrefUtils {

    /**
     * @param context
     * @param key
     * @param defaultValue
     * @return
     * @description 取string类
     */
    public static String getString(Context context, String key,
                                   final String defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    /**
     * @param context
     * @param key
     * @param value
     * @description 存string
     */
    public static void setString(Context context, final String key,
                                 final String value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putString(key, value).commit();
    }

    /**
     * @param context
     * @param key
     * @param defaultValue
     * @return
     * @description 取boolean
     */
    public static boolean getBoolean(Context context, final String key,
                                     final boolean defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * @param context
     * @param key
     * @return
     * @description 判断是否存在某个key
     */
    public static boolean hasKey(Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    /**
     * @param context
     * @param key
     * @param value
     * @description 存boolean
     */
    public static void setBoolean(Context context, final String key,
                                  final boolean value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key, value).commit();
    }

    /**
     * @param context
     * @param key
     * @param value
     * @description 存int
     */
    public static void setInt(Context context, final String key,
                              final int value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putInt(key, value).commit();
    }

    /**
     * @param context
     * @param key
     * @param defaultValue
     * @return
     * @description 取int
     */
    public static int getInt(Context context, final String key,
                             final int defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getInt(key, defaultValue);
    }

    /**
     * @param context
     * @param key
     * @param value
     * @description 存float
     */
    public static void setFloat(Context context, final String key,
                                final float value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putFloat(key, value).commit();
    }

    /**
     * @param context
     * @param key
     * @param defaultValue
     * @return
     * @description 取float
     */
    public static float getFloat(Context context, final String key,
                                 final float defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * @param context
     * @param key
     * @param value
     * @description 存long
     */
    public static void setLong(Context context, final String key,
                               final long value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putLong(key, value).commit();
    }

    /**
     * @param context
     * @param key
     * @param defaultValue
     * @return
     * @description 取long
     */
    public static long getLong(Context context, final String key,
                               final long defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getLong(key, defaultValue);
    }

    /**
     * @param context
     * @param p
     * @description 清除所有存储
     */
    public static void clearPreference(Context context,
                                       final SharedPreferences p) {
        final Editor editor = p.edit();
        editor.clear();
        editor.commit();
    }
}
