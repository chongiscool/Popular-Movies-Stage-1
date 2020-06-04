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

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import chong.wecanteen.com.popular_movies_stage_1.MainActivity

class MainActivity : AppCompatActivity() {
    private var mSort = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSort = Utility.getPreferredSortMovie(this)
        Log.i(TAG, "TEST:onCreate() called ")
    }

    override fun onResume() {
        super.onResume()
        val sort = Utility.getPreferredSortMovie(this)
        Log.i(TAG, "TEST:onResume() called ")
        when (sort) {
            Utility.MOVIE_SORT_POPULAR -> supportActionBar!!.setTitle(R.string.pop_movies)
            Utility.MOVIE_SORT_TOP_RATED -> supportActionBar!!.setTitle(R.string.top_movies)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // popular the setting to menu
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            // go to Setting
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}