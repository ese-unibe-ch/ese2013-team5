package com.mensaunibe.app.views;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.lib.supportpreference.PreferenceFragment;
import com.mensaunibe.util.gui.CustomListPreference;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.widget.Toast;

public class FragmentSettings extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	private Controller mController;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
    	
    	mController = Controller.getController();

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.fragment_settings);
        
        CustomListPreference language = (CustomListPreference) getPreferenceScreen().findPreference("setting_language");
        language.setSummary(Controller.getLanguage());
        
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
//
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Let's do something a preference value changes
    	if ( key.equals("setting_username") ) {
    		// TODO: remove dev toast, update status instead
    		Toast.makeText(getActivity(), "Username updated, sending to server...", Toast.LENGTH_SHORT).show();

//			((ActivityMain) getActivity()).updateUser(sharedPreferences.getString(key, "Mensa UniBE User"));

    	}
    	
    	if (key.equals("setting_language")) {
    		// save the language and update the app configuration
    		mController.setDefaultLocale();
    		// manually refresh the settings view
//    		Controller.getNavigationDrawer().selectItem(6);
    		
    		// unfortunately it's necessary to restart the controller
    		// couldn't find a better way to re-instantiate the already inflated fragments
    		// as it would need much more code and complexity in all the different fragments
    		// and a quick reboot is acceptable, I think
    	    Intent intent = new Intent(mController, Controller.class);
    	    startActivity(intent);
    	}
    }
}
