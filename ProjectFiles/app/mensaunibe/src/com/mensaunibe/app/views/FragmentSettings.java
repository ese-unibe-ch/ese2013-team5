package com.mensaunibe.app.views;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.lib.supportpreference.PreferenceFragment;
import com.mensaunibe.util.gui.CustomListPreference;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;

/**
 * fragment which shows the settings (username, language, share-text and homescreenconfiguration)
 */
public class FragmentSettings extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = FragmentSettings.class.getSimpleName();

	private Controller mController;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
    	
    	mController = Controller.getController();
    	
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.fragment_settings);
       
        // Dynamically set the value of the language setting
        CustomListPreference language = (CustomListPreference) getPreferenceScreen().findPreference("setting_language");
        language.setValue(Controller.getLanguage());
        
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
	public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
	public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	if ( key.equals("setting_username") ) {
    		// TODO: update status
    	}
    	
    	if (key.equals("setting_language")) {
    		// save the language and update the app configuration
    		mController.setDefaultLocale();    		
    		// unfortunately it's necessary to restart the controller
    		// couldn't find a better way to re-instantiate the already inflated fragments
    		// as it would need much more code and complexity in all the different fragments
    		// and a quick reboot is acceptable, I think
    	    Intent intent = new Intent(mController, Controller.class);
    	    startActivity(intent);
    	}
    }
}
