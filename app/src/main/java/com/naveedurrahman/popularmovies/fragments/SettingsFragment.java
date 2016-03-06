package com.naveedurrahman.popularmovies.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.naveedurrahman.popularmovies.R;

/**
 * Created by naveed on 17/2/16.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }
}