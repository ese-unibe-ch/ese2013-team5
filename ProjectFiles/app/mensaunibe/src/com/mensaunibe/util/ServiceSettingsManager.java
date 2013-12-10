package com.mensaunibe.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;

public class ServiceSettingsManager {
	
	private static final String TAG = ServiceSettingsManager.class.getSimpleName();
	
	private static Controller mController;

	private SharedPreferences mSettings;
	private Editor mSettingsEditor;
	
	public ServiceSettingsManager(Controller controller) {
		mController = controller;
		
		// set up the shared prefs, just use the default ones
		PreferenceManager.setDefaultValues(mController, R.layout.fragment_settings, false);
		mSettings = PreferenceManager.getDefaultSharedPreferences(mController);
		mSettingsEditor = mSettings.edit();
	}
	
	public Object getData(String type, String key) {
		Log.i(TAG, "getData(" + type + ", " + key + ")");
		
		Object setting = null;
		
		if (type == "string") {
			setting = mSettings.getString(key, null);
		} else if (type == "boolean") {
			setting = mSettings.getBoolean(key, false);
		} else if (type == "integer") {
			setting = mSettings.getInt(key, 0);
		} else if (type == "float") {
			setting = mSettings.getFloat(key, 0);
		} else {
			Log.e(TAG, "getData(): Type could not be resolved");
		}
		
		return setting;
	}
	
	/**
	 * Writes data to the shared prefs
	 * @param type the type of data to be saved
	 * @param key the key for the setting/data
	 * @param value the actual data to persist
	 */
	public void setData(String type, String key, Object value) {
		Log.i(TAG, "setData(" + type + ", " + key + ", value)");
		if (type == "string") {
			mSettingsEditor.putString(key, (String) value);
		} else if (type == "boolean") {
			mSettingsEditor.putBoolean(key, (Boolean) value);
		} else if (type == "integer") {
			Log.i(TAG, "setData(" + type + ", " + key + ", " + value + ")");
			mSettingsEditor.putInt(key, (Integer) value);
		} else if (type == "float") {
			mSettingsEditor.putFloat(key, (Float) value);
		} else {
			Log.e(TAG, "setData(): Type could not be resolved");
		}
		// write persistent data
		mSettingsEditor.commit();
	}
}
