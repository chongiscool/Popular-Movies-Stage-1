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
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import chong.wecanteen.com.popular_movies_stage_1.Utility.fetchCommentsJsonData
import chong.wecanteen.com.popular_movies_stage_1.Utility.fetchDetailJsonData
import chong.wecanteen.com.popular_movies_stage_1.Utility.fetchImageURL
import chong.wecanteen.com.popular_movies_stage_1.Utility.fetchVideosJsonData
import chong.wecanteen.com.popular_movies_stage_1.Utility.fetchYoutubeURL
import chong.wecanteen.com.popular_movies_stage_1.dataset.CommentsBean
import chong.wecanteen.com.popular_movies_stage_1.dataset.DetailsBean
import chong.wecanteen.com.popular_movies_stage_1.dataset.VideosBean
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.*
import java.util.*

/**
 * Created by Chong on 8/5/2016.
 */
class DetailFragment : Fragment() {
    // For fragment detail

    /*var tv_movie_title: TextView? = null
    var iv_movie_poster: ImageView? = null
    var tv_movie_runtime: TextView? = null
    var tv_movie_rating: TextView? = null
    var tv_movie_date: TextView? = null
    var btn_movie_favorite: Button? = null*/

    // From content fragment detail

    /*var tv_movie_overview: TextView? = null
    var linear_trailer: LinearLayout? = null
    var iv_movie_play_arrow: ImageView? = null
    var tv_movie_trailer: TextView? = null
    var tv_movie_reviews_user: TextView? = null
    var tv_movie_reviews_comment: TextView? = null
    var mProgressBar: ProgressBar? = null*/

    private var mId = 0

    /**
     * Detail Fragment will use these three data which implemented
     * [android.os.Parcelable] to update UI.
     */
    var detailsBean: DetailsBean? = null
    var videosBean: VideosBean? = null
    var commentsBean: CommentsBean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailMovieFavorite.visibility = View.GONE
        // Check internet whether is available or not
        val cm = context
                ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
            // Have access to internet
            // by default movie is 'Star Wars: The Force Awakens'
            mId = activity!!.intent.getIntExtra(MainFragment.EXTRA_ID, 140607)
            DetailAsyncTask().execute(mId)
        } else {
            // No internet access, hide these two view and show a toast
            detailProgressbar!!.visibility = View.GONE
            detailMovieFavorite!!.visibility = View.GONE
            Toast.makeText(activity, "No Internet Available", Toast.LENGTH_LONG).show()
        }
    }

    inner class DetailAsyncTask : AsyncTask<Int?, Void?, Bundle>() {

        private val TAG = "DetailAsyncTask"

        override fun doInBackground(vararg params: Int?): Bundle {
            val args = Bundle()
            val id = params[0]
            val detailsBean = fetchDetailJsonData(id!!)
            val videosBeanList = fetchVideosJsonData(id)
            val commentsBeanList = fetchCommentsJsonData(id)
            args.putParcelable(EXTRA_DETAILS_BEAN, detailsBean)
            // try-catch statement handle IndexOutOfBoundsException if exist
            try {
                args.putParcelable(EXTRA_COMMENTS_BEAN, commentsBeanList!![0])
            } catch (e: IndexOutOfBoundsException) {
                Log.e(TAG, "doInBackground: no comments", e)
                e.printStackTrace()
            }
            try {
                args.putParcelable(EXTRA_VIDEOS_BEAN, videosBeanList!![0])
            } catch (e: IndexOutOfBoundsException) {
                Log.e(TAG, "doInBackground: no treailers", e)
                e.printStackTrace()
            }
            return args

        }

        override fun onPostExecute(bundle: Bundle) {
            detailProgressbar!!.visibility = View.GONE
            detailsBean = bundle.getParcelable(EXTRA_DETAILS_BEAN)
            if (bundle.containsKey(EXTRA_COMMENTS_BEAN)) {
                commentsBean = bundle.getParcelable(EXTRA_COMMENTS_BEAN)
                if (commentsBean != null) {
                    detailReviewsUser!!.text = commentsBean!!.author
                    detailReviewsComment!!.text = commentsBean!!.content
                }
            }
            if (bundle.containsKey(EXTRA_VIDEOS_BEAN)) {
                videosBean = bundle.getParcelable(EXTRA_VIDEOS_BEAN)
                if (videosBean != null) {
                    detailLinearTrailer!!.setOnClickListener {
                        val key = videosBean!!.key
                        val uriString = fetchYoutubeURL(key)
                        val uri = Uri.parse(uriString)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                    detailPlayArrow!!.setImageResource(R.drawable.ic_play_arrow_black_48dp)
                    detailTrailer!!.text = videosBean!!.name
                }
            } else {
                detailLinearTrailer!!.visibility = View.GONE
            }
            if (detailsBean != null) {
                detailMovieTitle!!.text = detailsBean!!.title
                val image = detailsBean!!.poster_path
                Picasso.get().load(fetchImageURL(image))
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(detailMoviePoster)
                val format_runtime = String.format(Locale.US, context!!.getString(R.string.format_runtime), detailsBean!!.runtime)
                val format_rating = String.format(Locale.US, context!!.getString(R.string.format_rating), detailsBean!!.vote_average)
                val format_date = String.format(Locale.US, context!!.getString(R.string.format_date), detailsBean!!.release_date)
                detailMovieRuntime!!.text = format_runtime
                detailMovieRating!!.text = format_rating
                detailMovieDate!!.text = format_date
                detailOverview!!.text = detailsBean!!.overview
                detailMovieFavorite!!.visibility = View.VISIBLE
                detailMovieFavorite!!.setText(R.string.mark_as_favorite)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        private const val DETAILS_LOADER = 0
        private const val EXTRA_DETAILS_BEAN = "detailsBean"
        private const val EXTRA_VIDEOS_BEAN = "videosBean"
        private const val EXTRA_COMMENTS_BEAN = "commentsBean"
    }
}