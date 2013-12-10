package com.mensaunibe.util.gui;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;

public class CustomListPreference extends ListPreference {

//    private final static String TAG = CustomListPreference.class.getName();

    public CustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomListPreference(Context context) {
        super(context);
        init();
    }

    private void init() {

        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference arg0, Object arg1) {
                arg0.setSummary(getEntry());
                return true;
            }
        });
    }

    @Override
    public CharSequence getSummary() {
        return super.getEntry();
    }
}
