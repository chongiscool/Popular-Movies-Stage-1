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

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

import chong.wecanteen.com.popular_movies_stage_1.dataset.Bean;

/**
 * Created by Chong on 8/7/2016.
 */
public class MainAsyncTaskLoader extends AsyncTaskLoader<List<Bean>> {

    private static final String LOG_TAG = MainAsyncTaskLoader.class.getSimpleName();
    // set default value,which
    int mSort = 0;
    int mPage = 1;

    // Three constructor meet all kinds of situation
    public MainAsyncTaskLoader(Context context) {
        super(context);
    }

    public MainAsyncTaskLoader(Context context, int sort) {
        super(context);
        mSort = sort;
    }

    public MainAsyncTaskLoader(Context context, int sort, int page) {
        super(context);
        mSort = sort;
        mPage = page;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST:onStartLoading() called... ");
        // force load in main thread.
        forceLoad();
    }

    // do some work in background thread
    @Override
    public List<Bean> loadInBackground() {
        Log.i(LOG_TAG, "TEST:loadInBackground() called... ");
        // this operation consume much time placed background thread.
        return Utility.fetchMovieData(mSort, mPage);
    }
}
