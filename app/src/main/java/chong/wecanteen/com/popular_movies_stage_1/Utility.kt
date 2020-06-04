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
import android.net.Uri
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import chong.wecanteen.com.popular_movies_stage_1.dataset.Bean
import chong.wecanteen.com.popular_movies_stage_1.dataset.CommentsBean
import chong.wecanteen.com.popular_movies_stage_1.dataset.DetailsBean
import chong.wecanteen.com.popular_movies_stage_1.dataset.VideosBean
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Created by Chong on 7/9/2016.
 *
 *
 * This is a help class which have many static method and final static constant.
 * In this application, another class can access these method for their:
 * 1. build youtube and movie poster url
 * 2. fetch Movie data for MainFragment
 * 3. fetch Detail data, Trailer data and Reviews data for [DetailFragment]
 */
object Utility {
    private val LOG_TAG = Utility::class.java.simpleName

    // example like this: https://api.themoviedb.org/3/movie/550?api_key=XXXX
    const val MOVIE_SORT_POPULAR = 0
    const val MOVIE_SORT_TOP_RATED = 1
    const val MOVIE_SORT_FAVORITE = 2
    const val BASE_URL = "https://api.themoviedb.org/3/movie"
    const val MOVIE_POPULAR = "popular"
    const val MOVIE_TOP_RATED = "top_rated"
    const val MOVIE_FAVORITE = "favorite"
    const val API_KEY_PARAM = "api_key"

    // TODO, for swipeRefreshLayout page plus one, but use it later.
    const val PAGE_PARAM = "page"

    // For movie poster
    // example like this: http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
    const val IMAGE_BASE_URL = "http://image.tmdb.org/t/p"
    const val IMAGE_RECOMMEND_SIZE185 = "w185"

    // For trailers and reviews
    // example like this: https://www.youtube.com/watch?v=dU1xS07N-FA
    const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch"
    const val YOUTUBE_KEY_PARAM = "v"
    const val VIDEOS = "videos"
    const val REVIEWS = "reviews"

    // Get movie sort value from SharedPreferences
    @JvmStatic
    fun getPreferredSortMovie(context: Context): Int {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val sortString = sharedPreferences.getString(context.getString(R.string.sort), "0")
        return sortString!!.toInt()
    }

    // Call the method, return a string of sort movie url with spec sort
    // now, there are two sort: top_rated, popular.
    private fun buildURL(sort: Int, page: Int): String? {
        when (sort) {
            MOVIE_SORT_POPULAR -> {
                val uri1 = Uri.parse(BASE_URL).buildUpon()
                        .appendEncodedPath(MOVIE_POPULAR)
                        .appendQueryParameter(PAGE_PARAM, Integer.toString(page))
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                        .build()
                return uri1.toString()
            }
            MOVIE_SORT_TOP_RATED -> {
                val uri2 = Uri.parse(BASE_URL).buildUpon()
                        .appendEncodedPath(MOVIE_TOP_RATED)
                        .appendQueryParameter(PAGE_PARAM, Integer.toString(page))
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                        .build()
                return uri2.toString()
            }
        }
        return null
    }

    // Create URL for main fragment
    private fun createURL(urlString: String?): URL? {
        var url: URL? = null
        try {
            url = URL(urlString)
        } catch (e: MalformedURLException) {
            Log.e(LOG_TAG, "createURL: Problem building url", e)
            e.printStackTrace()
        }
        return url
    }

    /**
     * Use [URL] open connection, and invoke extractFromStream
     * method, get jsonResponse. This process might be consume more time, especially
     * request for image and video stream, so better place this process to background thread.
     *
     * @param url From createURL method
     * @return jsonResponse from web server
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun makeHttpResponse(url: URL?): String? {
        var jsonResponse: String? = ""
        if (url == null) {
            return jsonResponse
        }
        var urlConnection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        try {
            // make HTTP request
            urlConnection = url.openConnection() as HttpURLConnection
            // set request method
            urlConnection.requestMethod = "GET"
            // set read and connect timeout
            urlConnection!!.readTimeout = 10 * 1000
            urlConnection.connectTimeout = 15 * 1000
            urlConnection.connect()

            // check server response, if response code is 200, then do some work
            // else request failed
            if (urlConnection.responseCode == 200) {
                inputStream = urlConnection.inputStream
                // get json data form input stream, which invoke extractFromReader method
                jsonResponse = readFromStream(inputStream)
            } else {
                Log.e(LOG_TAG, "makeHttpResponse: Bad Response Code: " + urlConnection.responseCode)
            }
        } catch (e: MalformedURLException) {
            Log.d(LOG_TAG, "makeHTTPResponse: ", e)
            e.printStackTrace()
        } catch (e: IOException) {
            Log.e(LOG_TAG, "makeHTTPResponse: ", e)
            e.printStackTrace()
        } finally {
            // close stream and disconnect url
            inputStream?.close()
            urlConnection?.disconnect()
        }
        return jsonResponse
    }

    // extract the string type of json data from binary type of input stream
    @Throws(IOException::class)
    private fun readFromStream(inputStream: InputStream?): String? {
        if (inputStream != null) {
            val isr = InputStreamReader(inputStream, StandardCharsets.UTF_8)
            val reader = BufferedReader(isr)
            val builder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            return builder.toString()
        } else {
            Log.e(LOG_TAG, "readFromStream: inputStream is null.")
        }
        return null
    }

    /***
     * Parse jsonString, and store data to List
     *
     * @param jsonString downloaded after invoked makeHttpResponse method
     * @return [<]
     */
    private fun extractFeatureFromJson(jsonString: String?): List<Bean>? {
        if (TextUtils.isEmpty(jsonString)) {
            return null
        }
        val beans: MutableList<Bean> = ArrayList()
        var bean: Bean
        try {
            val jsonObject = JSONObject(jsonString!!)
            val jsonArray = jsonObject.getJSONArray("results")
            // store total pages for page reference at the proccess of swipeRefreshLayout.
            Bean.total_pages = jsonObject.getInt("total_pages")
            // add this page to store Bean.ResultsBean
            for (i in 0 until jsonArray.length()) {
                val element = jsonArray.getJSONObject(i)
                val id = element.getInt("id")
                val posterPath = element.getString("poster_path")
                val backdropPath = element.getString("backdrop_path")
                // store these data into a List<Bean>
                bean = Bean(id, posterPath, backdropPath)
                beans.add(bean)
            }
        } catch (e: JSONException) {
            Log.e(LOG_TAG, "extractFeatureFromJson: failed", e)
            e.printStackTrace()
        }
        return beans
    }

    /***
     * This method is public, by invoking buildURL, createURL,
     * makeHttpResponse(which invoked extraFromStream at the internal), extractFeatureJson methods.
     * Got a [<]  for MainAdapter.
     *
     * @param sort: three type: top_rated, pop and favorite.
     * @param page: require specific page.
     * @return [<]
     */
    @JvmStatic
    fun fetchMovieData(sort: Int, page: Int): List<Bean>? {
        val urlString = buildURL(sort, page)
        val url = createURL(urlString)
        var jsonResponse: String? = null
        try {
            jsonResponse = makeHttpResponse(url)
        } catch (e: IOException) {
            Log.e(LOG_TAG, "fetchMovieData: problem making the HTTP request.", e)
            e.printStackTrace()
        }
        // Extract relevant fields from the JSON response and got a List<Bean>
        val beans = extractFeatureFromJson(jsonResponse)
        // return
        Log.i(LOG_TAG, "TEST: fetchMovieData() called... ")
        return beans
    }

    // build reviews request url, so can fetch more movie comments
    private fun buildRequestCommentsURL(id: Int): String {
        val uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(id.toString())
                .appendEncodedPath(REVIEWS)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .build()
        return uri.toString()
    }

    private fun createKindsOfURL(urlString: String): URL? {
        var url: URL? = null
        try {
            url = URL(urlString)
        } catch (e: MalformedURLException) {
            Log.e(LOG_TAG, "createKindsOfURL: url create failed", e)
            e.printStackTrace()
        }
        return url
    }

    private fun extractCommentFromJson(jsonString: String?): List<CommentsBean>? {
        if (TextUtils.isEmpty(jsonString)) {
            return null
        }
        val commentsBeans: MutableList<CommentsBean> = ArrayList()
        try {
            val jsonObject = JSONObject(jsonString!!)
            val jsonArray = jsonObject.getJSONArray("results")
            for (i in 0 until jsonArray.length()) {
                val element = jsonArray.getJSONObject(i)
                val author = element.getString("author")
                val content = element.getString("content")
                val url = element.getString("url")
                commentsBeans.add(CommentsBean(author, content, url))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return commentsBeans
    }

    // Fetch comments json data from web server
    @JvmStatic
    fun fetchCommentsJsonData(id: Int): List<CommentsBean>? {
        val urlString = buildRequestCommentsURL(id)
        val url = createKindsOfURL(urlString)
        var jsonResponse: String? = null
        try {
            jsonResponse = makeHttpResponse(url)
        } catch (e: IOException) {
            Log.e(LOG_TAG, "fetchCommentsJsonData: problems making the Http request", e)
            e.printStackTrace()
        }
        val commentsBeans = extractCommentFromJson(jsonResponse)
        if (commentsBeans == null) {
            Log.i(LOG_TAG, "TEST:fetchCommentsJsonData() called commentsBean is null")
        }
        return commentsBeans
    }

    // Build trailer request url, so can fetch more trailer videos
    fun buildRequestVideosURL(id: Int): String {
        val uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(Integer.toString(id))
                .appendEncodedPath(VIDEOS)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .build()
        return uri.toString()
    }

    // Extract data from json string, store them in a List
    private fun extractVideoFromJson(jsonString: String?): List<VideosBean>? {
        if (TextUtils.isEmpty(jsonString)) {
            return null
        }
        val videosBeans: MutableList<VideosBean> = ArrayList()
        var videosBean: VideosBean
        try {
            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray("results")
            for (i in 0 until jsonArray.length()) {
                val element = jsonArray.getJSONObject(i)
                val type = element.getString("type")
                if (type == "Trailer") {
                    videosBean = VideosBean(
                            element.getString("key"),
                            element.getString("name"),
                            type
                    )
                    videosBeans.add(videosBean)
                }
            }
        } catch (e: JSONException) {
            Log.e(LOG_TAG, "extractVideoFromJson: ", e)
            e.printStackTrace()
        }
        return videosBeans
    }

    // Fetch json data for specified movie which contain some film trailers,
    // perform this method,
    @JvmStatic
    fun fetchVideosJsonData(id: Int): List<VideosBean>? {
        val urlString = buildRequestVideosURL(id)
        val url = createKindsOfURL(urlString)
        var jsonResponse: String? = null
        try {
            jsonResponse = makeHttpResponse(url)
        } catch (e: IOException) {
            Log.e(LOG_TAG, "fetchVideosJsonData: Problem in making http request", e)
            e.printStackTrace()
        }
        val videosBeans = extractVideoFromJson(jsonResponse)
        Log.i(LOG_TAG, "TEST:fetchVideosJsonData() called... ")
        return videosBeans
    }

    // Build details request url for detail fragment
    private fun buildRequestDetailsURL(id: Int): String {
        val uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(Integer.toString(id))
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .build()
        return uri.toString()
    }

    // Extract data from json string and store it.
    private fun extractDetailFromJson(jsonString: String?): DetailsBean? {
        if (TextUtils.isEmpty(jsonString)) {
            return null
        }
        val bean = DetailsBean()
        try {
            val jsonObject = JSONObject(jsonString)
            bean.title = jsonObject.getString("title")
            bean.runtime = jsonObject.getInt("runtime")
            bean.vote_average = jsonObject.getDouble("vote_average")
            bean.release_date = jsonObject.getString("release_date")
            bean.poster_path = jsonObject.getString("poster_path")
            bean.overview = jsonObject.getString("overview")
        } catch (e: JSONException) {
            Log.e(LOG_TAG, "extractDetailFromJson: Problem in parsing json data", e)
            e.printStackTrace()
        }
        return bean
    }

    // Fetch json data for specified movie detail
    @JvmStatic
    fun fetchDetailJsonData(id: Int): DetailsBean? {
        val urlString = buildRequestDetailsURL(id)
        val url = createKindsOfURL(urlString)
        var jsonResponse: String? = null
        try {
            jsonResponse = makeHttpResponse(url)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val bean = extractDetailFromJson(jsonResponse)
        Log.i(LOG_TAG, "TEST:fetchDetailJsonData() called... ")
        return bean
    }

    // Build image url, so can view directly.
    @JvmStatic
    fun fetchImageURL(image: String?): String {
        val uri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendEncodedPath(IMAGE_RECOMMEND_SIZE185)
                .appendEncodedPath(image)
                .build()
        return uri.toString()
    }

    // Build youtube url, so can view directly.
    @JvmStatic
    fun fetchYoutubeURL(video: String?): String {
        val uri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_KEY_PARAM, video)
                .build()
        return uri.toString()
    }
}