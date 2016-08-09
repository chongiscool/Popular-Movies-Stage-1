package chong.wecanteen.com.popular_movies_stage_1;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import chong.wecanteen.com.popular_movies_stage_1.dataset.DetailsBean;

/**
 * Created by Chong on 8/8/2016.
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
