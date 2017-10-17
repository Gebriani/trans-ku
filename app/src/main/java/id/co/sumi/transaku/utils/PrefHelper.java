package id.co.sumi.transaku.utils;

import android.content.SharedPreferences;

import id.co.sumi.transaku.TransakuApplication;


/**
 * Created by MuhammadAbrar on 01/26/2016.
 */
public class PrefHelper {

    private static SharedPreferences preferences;

    private static void initPref() {
        preferences = TransakuApplication.getInstance().getSharedPreferences();
    }

    public static void setString(String key, String value) {
        initPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key.toString(), value);
        editor.apply();
    }

    public static String getString(String key) {
        initPref();
        return preferences.getString(key.toString(), "");
    }

    public static void setInt(String key, int value) {
        initPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key.toString(), value);
        editor.apply();
    }

    public static int getInt(String key) {
        initPref();
        return preferences.getInt(key.toString(), -1);
    }

    public static void setBoolean(String key, boolean value) {
        initPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key.toString(), value);
        editor.apply();
    }

    public static boolean getBoolean(String key) {
        initPref();
        return preferences.getBoolean(key.toString(), false);
    }

    public static void clearPreference(String key) {
        initPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key.toString());
        editor.apply();
    }

    public static void clearAllPreferences() {
        initPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
