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

package chong.wecanteen.com.popular_movies_stage_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private int mSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSort = Utility.getPreferredSortMovie(this);
        Log.i(LOG_TAG, "TEST:onCreate() called ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        int sort  = Utility.getPreferredSortMovie(this);
        Log.i(LOG_TAG, "TEST:onResume() called ");
        // update this activity title according to sort value
        switch (sort) {
            case Utility.MOVIE_SORT_POPULAR:
                getSupportActionBar().setTitle(R.string.pop_movies);
                break;
            case Utility.MOVIE_SORT_TOP_RATED:
                getSupportActionBar().setTitle(R.string.top_movies);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // popular the setting to menu
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            // go to Setting
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
