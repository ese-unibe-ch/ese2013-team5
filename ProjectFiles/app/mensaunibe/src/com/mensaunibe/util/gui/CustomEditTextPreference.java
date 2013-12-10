package com.mensaunibe.util.gui;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.AttributeSet;

public class CustomEditTextPreference extends EditTextPreference {

    public CustomEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditTextPreference(Context context) {
        super(context);
        init();
    }

    private void init() {

        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference arg0, Object arg1) {
                arg0.setSummary((CharSequence)arg1);
                return true;
            }
        });
    }

    @Override
    public CharSequence getSummary() {
        return super.getText();
    }
}
