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

    private static final String LOG_TAG = "MainAsyncTaskLoader";

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
        return Utility.fetchMovieData(mSort, mPage);
    }
}
