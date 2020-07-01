package com.something.fitnessapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by nazlican on 17.12.2017.
 */

public class Timer_Settings extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
