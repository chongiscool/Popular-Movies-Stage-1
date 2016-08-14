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
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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

import chong.wecanteen.com.popular_movies_stage_1.adapter.MainAdapter;
import chong.wecanteen.com.popular_movies_stage_1.dataset.Bean;

/**
 * Created by Chong on 8/5/2016.
 *
 * Encapsulates fetching the movie poster and displaying it as a {@link GridView} layout.
 */
public class MainFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, /* TODO: really use it later */
        AdapterView.OnItemClickListener, /** for listening item of adapter view, here is a GridView*/
        LoaderManager.LoaderCallbacks<List<Bean>> {

    private static final int MAIN_LOADER = 0;
    private static final String LOG_TAG = MainFragment.class.getSimpleName();
    public static final String EXTRA_ID = "movie_detail_id";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private TextView mTextView;

    private MainAdapter mAdapter;
    private int mSort;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        mSort = Utility.getPreferredSortMovie(getContext());

        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mGridView = (GridView) root.findViewById(R.id.movie_gridview);
        mTextView = (TextView) root.findViewById(R.id.textView);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        // when GridView didn't display, then show this Textview
        mGridView.setEmptyView(mTextView);

        mAdapter = new MainAdapter(getContext(), new ArrayList<Bean>());

        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(this);

        Log.i(LOG_TAG, "TEST:onCreateView() called ");
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        Log.i(LOG_TAG, "TEST:onActivityCreated() called ");
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), R.string.refresh_friendly_reminder, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.purple, R.color.green, R.color.orange);
    }

    // For supporting Activity associated communicate to this Fragment.
    void onMovieSortChanged(int sort) {
        mSort = sort;
        getActivity().getSupportLoaderManager().restartLoader(MAIN_LOADER, null, this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Bean bean = mAdapter.getItem(position);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(EXTRA_ID, bean.getId());
        startActivity(intent);
    }

    @Override
    public Loader<List<Bean>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST: onCreateLoader() called... ");
        return new MainAsyncTaskLoader(getContext(), mSort);
    }

    @Override
    public void onLoadFinished(Loader<List<Bean>> loader, List<Bean> data) {

        mTextView.setText(R.string.no_found_movie);
        mProgressBar.setVisibility(View.GONE);
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
        Log.i(LOG_TAG, "TEST: onLoadFinished() called... ");
    }

    @Override
    public void onLoaderReset(Loader<List<Bean>> loader) {
        Log.i(LOG_TAG, "TEST: onLoaderReset() called... ");
        mAdapter.addAll(new ArrayList<Bean>());
    }

}
