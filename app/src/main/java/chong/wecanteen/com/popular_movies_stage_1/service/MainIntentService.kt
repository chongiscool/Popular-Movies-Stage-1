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
package chong.wecanteen.com.popular_movies_stage_1.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import chong.wecanteen.com.popular_movies_stage_1.Utility.fetchMovieData

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Use this IntentService after popular movies stage 1
 * helper methods.
 */
class MainIntentService : IntentService("MainIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_POPULAR == action) {
                val popular_page = intent.getIntExtra(EXTRA_PAGE, -1)
                handleActionPopular(popular_page)
            } else if (ACTION_TOP_RATED == action) {
                val top_rated_page = intent.getIntExtra(EXTRA_PAGE, -1)
                handleActionTopRated(top_rated_page)
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionPopular(page: Int) {
        fetchMovieData(0, page)
        // throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionTopRated(page: Int) {
        fetchMovieData(1, page)
        // throw new UnsupportedOperationException("Not yet implemented");
    }

    companion object {
        // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
        private const val ACTION_POPULAR = "chong.wecanteen.com.popular_movies_stage_1.service.action.POPULAR"
        private const val ACTION_TOP_RATED = "chong.wecanteen.com.popular_movies_stage_1.service.action.TOP_RATED"
        private const val EXTRA_PAGE = "chong.wecanteen.com.popular_movies_stage_1.service.extra.PAGE"

        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        fun startActionPopular(context: Context, page: Int) {
            val intent = Intent(context, MainIntentService::class.java)
            intent.action = ACTION_POPULAR
            intent.putExtra(EXTRA_PAGE, page)
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        fun startActionTopRated(context: Context, page: Int) {
            val intent = Intent(context, MainIntentService::class.java)
            intent.action = ACTION_TOP_RATED
            intent.putExtra(EXTRA_PAGE, page)
            context.startService(intent)
        }
    }
}