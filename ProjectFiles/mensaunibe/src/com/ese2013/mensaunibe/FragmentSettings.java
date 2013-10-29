package com.ese2013.mensaunibe;

import java.io.UnsupportedEncodingException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class FragmentSettings extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	
//    private ActivityMain main;
//	private EditTextPreference username;
//	private ListPreference homescreen;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
    	
//    	this.main = (ActivityMain) this.getActivity();

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.fragment_settings);
        
        // Get a reference to the preferences
//        username = (EditTextPreference) getPreferenceScreen().findPreference("setting_username");
//        homescreen = (ListPreference) getPreferenceScreen().findPreference("setting_homescreen");
    }

    @Override
	public void onResume() {
        super.onResume();
        // Setup the initial values
//        username.setSummary(sharedPreferences.getBoolean(key, false) ? "Disable this setting" : "Enable this setting");
//        homescreen.setSummary("Current value is " + sharedPreferences.getValue(key, ""));
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
	public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
//
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Let's do something a preference value changes
    	if ( key.equals("setting_username") ) {
    		Toast.makeText(getActivity(), "Username updated, sending to server...", Toast.LENGTH_LONG).show();
    		try {
				((ActivityMain) getActivity()).updateUserName(sharedPreferences.getString(key, "Mensa UniBE User"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
//        if (key.equals(KEY_CHECKBOX_PREFERENCE)) {
//            mCheckBoxPreference.setSummary(sharedPreferences.getBoolean(key,
//                    false) ? "Disable this setting" : "Enable this setting");
//        } else if (key.equals(KEY_LIST_PREFERENCE)) {
//            mListPreference.setSummary("Current value is "
//                    + sharedPreferences.getValue(key, ""));
//        }
    }
}
