package de.blinkt.openvpn.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Preference Interactor class
 * <p>
 * - Accesses Shared Preferences and returns a (Matrix) Cursor Object
 */
final class PreferenceInteractor {

    private static final String DEFAULT_STRING = "";
    private static final int DEFAULT_INT = -1;
    private static final long DEFAULT_LONG = -1L;
    private static final boolean DEFAULT_BOOLEAN = false;

    private SharedPreferences sharedPreferences;

    PreferenceInteractor(Context context, String preferenceName) {
        this.sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    boolean hasKey(String key) {
        return sharedPreferences.contains(key);
    }

    String getString(String key) {
        return sharedPreferences.getString(key, DEFAULT_STRING);
    }

    void setString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    int getInt(String key) {
        return sharedPreferences.contains(key) ? sharedPreferences.getInt(key, DEFAULT_INT) : null;
    }

    void setInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    long getLong(String key) {
        return sharedPreferences.getLong(key, DEFAULT_LONG);

    }

    void setLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, DEFAULT_BOOLEAN);
    }

    void setBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    void removePref(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    void clearPreference() {
        sharedPreferences.edit().clear().apply();
    }
}