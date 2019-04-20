package com.medicalwale.gniapp.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class PreferenceManager {


    public static final String MY_PREFS_NAME = "Medicalwale";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private final Gson gson;

    private Context context;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;


    //User Details

    public PreferenceManager(Context context) {
        this.context = context;
        this.editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
        this.prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void removeKey(String key) {
        editor.remove(key).apply();
    }

    public void putPreferenceBoolValues(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putPreferenceValues(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }


    public void putPreferenceIntValues(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void putPreferenceValuesCustomObject(String key, Object value) {

        String json;

        json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }


    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return prefs.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    /* *//* public void putPreferencefloatValues(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getPreferencefloatValues(String key) {
        return prefs.getFloat(key, 0);
    }
*/
    public boolean getPreferenceBoolValues(String key) {
        return prefs.getBoolean(key, false);
    }

    public boolean isContainKey(String key) {
        return prefs.contains(key);
    }

    public int getPreferenceIntValues(String key) {
        return prefs.getInt(key, 0);
    }

    public String getPreferenceValues(String key) {
        return prefs.getString(key, "");
    }

    public void clearSharedPreferance() {
        prefs.edit().remove(MY_PREFS_NAME).apply();
        prefs.edit().clear().apply();

    }

    public void putPreferencDoubleValues(String key, double value) {
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.commit();
    }

    public double getPreferencDoubleValues(String key, double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    public void deletePreference(String key) {
        prefs.edit().remove(key).commit();

    }
}
