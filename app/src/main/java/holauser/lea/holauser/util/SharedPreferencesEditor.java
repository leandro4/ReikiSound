package holauser.lea.holauser.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SharedPreferencesEditor {

    private final SharedPreferences mSharedPreferences;

    public SharedPreferencesEditor(Context context, String prefix) {
        mSharedPreferences = context.getSharedPreferences(prefix + "_prefs", Context.MODE_PRIVATE);
    }

    public void clear() {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.clear();
        e.apply();
    }

    public boolean hasValueForKey(String key) {
        return mSharedPreferences.contains(key);
    }

    public void removeValueForKey(String key) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.remove(key);
        e.apply();
    }

    public void setValueForKey(String key, String value) {
        if (value == null) {
            removeValueForKey(key);
            return;
        }

        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putString(key, value);
        e.apply();
    }

    public void setValueForKey(String key, int value) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putInt(key, value);
        e.apply();
    }

    public String valueForKey(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public String valueForKey(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public int valueForKey(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public void setValueForKey(String key, long value) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putLong(key, value);
        e.apply();
    }

    public long valueForKey(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public void setValueForKey(String key, boolean value) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putBoolean(key, value);
        e.apply();
    }

    public boolean valueForKey(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public void setValueForKey(String key, float value) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putFloat(key, value);
        e.apply();
    }

    public float valueForKey(String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

}
