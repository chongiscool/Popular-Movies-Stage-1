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

import chong.wecanteen.com.popular_movies_stage_1.dataset.DetailsBean;

/**
 * Created by Chong on 8/8/2016.
 *
 * TODO: use this loader I am thinking how to use it and whether use it or not
 */
public class DetailsAsyncTaskLoader extends AsyncTaskLoader<DetailsBean> {

    // this id is specified movie
    private int mId;
    public DetailsAsyncTaskLoader(Context context, int id) {
        super(context);
        mId = id;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public DetailsBean loadInBackground() {

        return Utility.fetchDetailJsonData(mId);
    }
}
