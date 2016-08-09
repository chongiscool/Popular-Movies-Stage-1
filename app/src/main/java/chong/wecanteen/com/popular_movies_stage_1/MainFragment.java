package chong.wecanteen.com.popular_movies_stage_1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import chong.wecanteen.com.popular_movies_stage_1.adapter.ImageAdapter;
import chong.wecanteen.com.popular_movies_stage_1.dataset.Bean;

/**
 * Created by Chong on 8/5/2016.
 */
public class MainFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, /** */
        AdapterView.OnItemClickListener, /** for listening item of adapter view, here is a GridView*/
        LoaderManager.LoaderCallbacks<List<Bean>> { /** */
    private static final int MAIN_LOADER = 0;
    private static final String LOG_TAG = "MainFragment";

    public static final String EXTRA_ID = "movie_detail_id";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private TextView mTextView;

    private ImageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mGridView = (GridView) root.findViewById(R.id.movie_gridview);
        mTextView = (TextView) root.findViewById(R.id.textView);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        // when GridView didn't display, then show this Textview
        mGridView.setEmptyView(mTextView);

        adapter = new ImageAdapter(getContext(), new ArrayList<Bean>());

        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(this);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Check internet whether is available or not
        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            // Have access to internet
            getActivity().getSupportLoaderManager().initLoader(MAIN_LOADER, null, this);
        } else {
            // No Internet access
            mProgressBar.setVisibility(View.GONE);
            mTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "Refreshing...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.purple, R.color.green, R.color.orange);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Bean bean = adapter.getItem(position);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(EXTRA_ID, bean.getId());
        startActivity(intent);
    }

    @Override
    public Loader<List<Bean>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST: onCreateLoader() called... ");
        return new MainAsyncTaskLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Bean>> loader, List<Bean> data) {

        mTextView.setText(R.string.no_found_movie);
        mProgressBar.setVisibility(View.GONE);
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Bean>> loader) {
        adapter.addAll(new ArrayList<Bean>());
    }

}
