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

package chong.wecanteen.com.popular_movies_stage_1.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import chong.wecanteen.com.popular_movies_stage_1.Utility;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Use this IntentService after popular movies stage 1
 * helper methods.
 */
public class MainIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_POPULAR = "chong.wecanteen.com.popular_movies_stage_1.service.action.POPULAR";
    private static final String ACTION_TOP_RATED = "chong.wecanteen.com.popular_movies_stage_1.service.action.TOP_RATED";

    private static final String EXTRA_PAGE = "chong.wecanteen.com.popular_movies_stage_1.service.extra.PAGE";

    public MainIntentService() {
        super("MainIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionPopular(Context context, int page) {
        Intent intent = new Intent(context, MainIntentService.class);
        intent.setAction(ACTION_POPULAR);
        intent.putExtra(EXTRA_PAGE, page);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionTopRated(Context context, int page) {
        Intent intent = new Intent(context, MainIntentService.class);
        intent.setAction(ACTION_TOP_RATED);
        intent.putExtra(EXTRA_PAGE, page);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POPULAR.equals(action)) {
                final int popular_page = intent.getIntExtra(EXTRA_PAGE, -1);
                handleActionPopular(popular_page);
            } else if (ACTION_TOP_RATED.equals(action)) {
                final int top_rated_page = intent.getIntExtra(EXTRA_PAGE, -1);
                handleActionTopRated(top_rated_page);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPopular(int page) {
        Utility.fetchMovieData(0, page);
        // throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionTopRated(int page) {
        Utility.fetchMovieData(1, page);
        // throw new UnsupportedOperationException("Not yet implemented");
    }
}
