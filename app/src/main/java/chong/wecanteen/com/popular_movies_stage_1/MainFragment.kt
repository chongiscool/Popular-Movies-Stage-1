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
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import chong.wecanteen.com.popular_movies_stage_1.Utility.getPreferredSortMovie
import chong.wecanteen.com.popular_movies_stage_1.adapter.MainAdapter
import chong.wecanteen.com.popular_movies_stage_1.dataset.Bean
import java.util.*

/**
 * Created by Chong on 8/5/2016.
 *
 *
 * Encapsulates fetching the movie poster and displaying it as a [GridView] layout.
 */
class MainFragment : Fragment(), OnRefreshListener, OnItemClickListener, LoaderManager.LoaderCallbacks<List<Bean>?> {

    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    var mGridView: GridView? = null
    var mTextView: TextView? = null
    var mProgressBar: ProgressBar? = null

    private var mAdapter: MainAdapter? = null
    private var mSort = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        mSwipeRefreshLayout = root.findViewById(R.id.container)
        mGridView = root.findViewById(R.id.movie_gridview)
        mTextView = root.findViewById(R.id.textView)
        mProgressBar = root.findViewById(R.id.progressBar)

        mSort = getPreferredSortMovie(context!!)
        mSwipeRefreshLayout!!.setOnRefreshListener(this)

        // when GridView didn't display, then show this Textview
        mGridView!!.emptyView = mTextView
        mAdapter = MainAdapter(context!!, ArrayList())
        mGridView!!.adapter = mAdapter
        mGridView!!.onItemClickListener = this
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Check internet whether is available or not
        val cm = context
                ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
            // Have access to internet
            activity!!.supportLoaderManager.initLoader(MAIN_LOADER, null, this)
        } else {
            // No Internet access
            mProgressBar!!.visibility = View.GONE
            mTextView!!.setText(R.string.no_internet_connection)
        }
        Log.i(LOG_TAG, "TEST:onActivityCreated() called")
    }

    override fun onRefresh() {
        Toast.makeText(context, R.string.refresh_friendly_reminder, Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ mSwipeRefreshLayout!!.isRefreshing = false }, 2000)
        mSwipeRefreshLayout!!.setColorSchemeResources(R.color.blue, R.color.purple, R.color.green, R.color.orange)
    }

    override fun onResume() {
        super.onResume()
        val sort = getPreferredSortMovie(context!!)
        if (sort != mSort) {
            mSort = sort
            // call this method to restart loader if sort value changed
            activity!!.supportLoaderManager.restartLoader(MAIN_LOADER, null, this)
        }
    }

    override fun onItemClick(adapterView: AdapterView<*>?, view: View, position: Int, l: Long) {
        val bean = mAdapter!!.getItem(position)
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(EXTRA_ID, bean!!.id)
        startActivity(intent)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<Bean>?> {
        Log.i(LOG_TAG, "TEST: onCreateLoader() called... ")
        return MainAsyncTaskLoader(context, mSort)
    }

    override fun onLoadFinished(loader: Loader<List<Bean>?>, data: List<Bean>?) {
        mTextView!!.setText(R.string.no_found_movie)
        mProgressBar!!.visibility = View.GONE
        mAdapter!!.clear()
        if (data != null && !data.isEmpty()) {
            mAdapter!!.addAll(data)
        }
    }

    override fun onLoaderReset(loader: Loader<List<Bean>?>) {
        Log.i(LOG_TAG, "TEST: onLoaderReset() called... ")
        mAdapter!!.addAll(ArrayList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        private const val MAIN_LOADER = 0
        private val LOG_TAG = MainFragment::class.java.simpleName
        const val EXTRA_ID = "movie_detail_id"
    }
}