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

import android.content.Context
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import chong.wecanteen.com.popular_movies_stage_1.MainAsyncTaskLoader
import chong.wecanteen.com.popular_movies_stage_1.Utility.fetchMovieData
import chong.wecanteen.com.popular_movies_stage_1.dataset.Bean

/**
 * Created by Chong on 8/7/2016.
 */
class MainAsyncTaskLoader : AsyncTaskLoader<List<Bean>?> {
    // set default value,which
    var mSort = 0
    var mPage = 1

    // Three constructor meet all kinds of situation
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, sort: Int) : super(context!!) {
        mSort = sort
    }

    constructor(context: Context?, sort: Int, page: Int) : super(context!!) {
        mSort = sort
        mPage = page
    }

    override fun onStartLoading() {
        Log.i(LOG_TAG, "TEST:onStartLoading() called... ")
        // force load in main thread.
        forceLoad()
    }

    // do some work in background thread
    override fun loadInBackground(): List<Bean>? {
        Log.i(LOG_TAG, "TEST:loadInBackground() called... ")
        // this operation consume much time placed background thread.
        return fetchMovieData(mSort, mPage)
    }

    companion object {
        private val LOG_TAG = MainAsyncTaskLoader::class.java.simpleName
    }
}