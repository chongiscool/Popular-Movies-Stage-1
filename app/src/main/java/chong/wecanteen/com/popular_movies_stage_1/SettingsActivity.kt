/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package chong.wecanteen.com.popular_movies_stage_1

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.Preference.OnPreferenceChangeListener
import android.preference.PreferenceActivity
import android.preference.PreferenceManager

/**
 * Created by Chong on 7/9/2016.
 */
class SettingsActivity : PreferenceActivity(), OnPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        load the preference from preferences.xml
        addPreferencesFromResource(R.xml.preferences)

        // for all preferences, attach an onPreferenceChangeListener so the UI
        // summary can be updated when the preference changes.
        bindPreferenceSummaryToValue(findPreference(getString(R.string.sort)))
    }

    private fun bindPreferenceSummaryToValue(pref: Preference) {
        // set the listener to watch for value changes.
        pref.onPreferenceChangeListener = this

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(
                pref,
                PreferenceManager.getDefaultSharedPreferences(pref.context)
                        /** this line get SharePreference Object */
                        /** this line get SharePreference Object */
                        .getString(pref.key, "")!!)
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        val stringValue = newValue.toString()
        if (preference is ListPreference) {
            // for list preferences, look up the correct display value in
            // the preference's 'entries' list(since they have separate labels/values).
            val listPreference = preference
            val prefIndex = listPreference.findIndexOfValue(stringValue)
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.entries[prefIndex])
            } else {
                // for other preferences, set the summary to the value's simple string representation
                preference.setSummary(stringValue)
            }
        }
        return true
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun getParentActivityIntent(): Intent? {
        return super.getParentActivityIntent()!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
}