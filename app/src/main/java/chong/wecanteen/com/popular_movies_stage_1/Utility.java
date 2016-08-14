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
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import chong.wecanteen.com.popular_movies_stage_1.dataset.Bean;
import chong.wecanteen.com.popular_movies_stage_1.dataset.CommentsBean;
import chong.wecanteen.com.popular_movies_stage_1.dataset.DetailsBean;
import chong.wecanteen.com.popular_movies_stage_1.dataset.VideosBean;

/**
 * Created by Chong on 7/9/2016.
 *
 * This is a help class which have many static method and final static constant.
 * In this application, another class can access these method for their:
 * 1. build youtube and movie poster url
 * 2. fetch Movie data for MainFragment
 * 3. fetch Detail data, Trailer data and Reviews data for {@link DetailFragment}
 */
public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();

    // example like this: https://api.themoviedb.org/3/movie/550?api_key=XXXX
    public static final int MOVIE_SORT_POPULAR = 0;
    public static final int MOVIE_SORT_TOP_RATED = 1;
    public static final int MOVIE_SORT_FAVORITE = 2;
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    public static final String MOVIE_POPULAR = "popular";
    public static final String MOVIE_TOP_RATED = "top_rated";
    public static final String MOVIE_FAVORITE = "favorite";
    public static final String API_KEY_PARAM = "api_key";
    // TODO, for swipeRefreshLayout page plus one, but use it later.
    public static final String PAGE_PARAM = "page";

    // For movie poster
    // example like this: http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
    public static final String IMAGE_RECOMMEND_SIZE185 = "w185";
    // For trailers and reviews
    // example like this: https://www.youtube.com/watch?v=dU1xS07N-FA
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    public static final String YOUTUBE_KEY_PARAM = "v";
    public static final String VIDEOS = "videos";
    public static final String REVIEWS = "reviews";

    // Get movie sort value from SharedPreferences
    public static int getPreferredSortMovie(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        String sortString = sharedPreferences.getString(context.getString(R.string.sort), "0");
        return Integer.parseInt(sortString);
    }

    // Call the method, return a string of sort movie url with spec sort
    // now, there are two sort: top_rated, popular.
    private static String buildURL(int sort, int page) {
        switch (sort) {
            case MOVIE_SORT_POPULAR:
                Uri uri1 = Uri.parse(BASE_URL).buildUpon()
                        .appendEncodedPath(MOVIE_POPULAR)
                        .appendQueryParameter(PAGE_PARAM, Integer.toString(page))
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                        .build();
                return uri1.toString();
            case MOVIE_SORT_TOP_RATED:
                Uri uri2 = Uri.parse(BASE_URL).buildUpon()
                        .appendEncodedPath(MOVIE_TOP_RATED)
                        .appendQueryParameter(PAGE_PARAM, Integer.toString(page))
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                        .build();
                return uri2.toString();
            // TODO: favorite
        }
        return null;
    }

    // Create URL for main fragment
    private static URL createURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "createURL: Problem building url", e);
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Use {@link URL} open connection, and invoke extractFromStream
     * method, get jsonResponse. This process might be consume more time, especially
     * request for image and video stream, so better place this process to background thread.
     * @param url From createURL method
     * @return jsonResponse from web server
     * @throws IOException
     */
    private static String makeHttpResponse(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            // make HTTP request
            urlConnection = (HttpURLConnection) url.openConnection();
            // set request method
            urlConnection.setRequestMethod("GET");
            // set read and connect timeout
            urlConnection.setReadTimeout(10 * 1000  /* unit is Millisecond*/);
            urlConnection.setConnectTimeout(15 * 1000);
            urlConnection.connect();

            // check server response, if response code is 200, then do some work
            // else request failed
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                // get json data form input stream, which invoke extractFromReader method
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "makeHttpResponse: Bad Response Code: " + urlConnection.getResponseCode());
            }

        } catch (MalformedURLException e) {
            Log.d(LOG_TAG, "makeHTTPResponse: ", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "makeHTTPResponse: ", e);
            e.printStackTrace();

        } finally {
            // close stream and disconnect url
            if (inputStream != null) {
                inputStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonResponse;
    }

    // extract the string type of json data from binary type of input stream
    private static String readFromStream(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            InputStreamReader isr =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(isr);

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } else {
            Log.e(LOG_TAG, "readFromStream: inputStream is null.");
        }
        return null;
    }

    /***
     * Parse jsonString, and store data to List
     * @param jsonString downloaded after invoked makeHttpResponse method
     * @return {@link List<Bean>}
     */
    private static List<Bean> extractFeatureFromJson(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        List<Bean> beans = new ArrayList<>();
        Bean bean;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            JSONArray jsonArray = jsonObject.getJSONArray("results");
            // store total pages for page reference at the proccess of swipeRefreshLayout.
            Bean.total_pages = jsonObject.getInt("total_pages");
            // add this page to store Bean.ResultsBean
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject element = jsonArray.getJSONObject(i);
                int id = element.getInt("id");
                String poster_path = element.getString("poster_path");
                String backdrop_path = element.getString("backdrop_path");
                // store these data into a List<Bean>
                bean = new Bean();
                bean.setId(id);
                bean.setPoster_path(poster_path);
                bean.setBackdrop_path(backdrop_path);
                beans.add(bean);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "extractFeatureFromJson: failed",e);
            e.printStackTrace();
        }
        return beans;
    }

    /***
     * This method is public, by invoking buildURL, createURL,
     * makeHttpResponse(which invoked extraFromStream at the internal), extractFeatureJson methods.
     * Got a {@link List<Bean>}  for MainAdapter.
     *
     * @param sort: three type: top_rated, pop and favorite.
     * @param page: require specific page.
     * @return {@link List<Bean>}
     */
    public static List<Bean> fetchMovieData(int sort, int page) {
        String urlString = buildURL(sort, page);
        URL url = createURL(urlString);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpResponse(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "fetchMovieData: problem making the HTTP request.", e);
            e.printStackTrace();
        }
        // Extract relevant fields from the JSON response and got a List<Bean>
        List<Bean> beans = extractFeatureFromJson(jsonResponse);
        // return
        Log.i(LOG_TAG, "TEST: fetchMovieData() called... ");
        return beans;
    }

    // build reviews request url, so can fetch more movie comments
    private static String buildRequestCommentsURL(int id) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(Integer.toString(id))
                .appendEncodedPath(REVIEWS)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .build();
        return uri.toString();
    }

    private static URL createKindsOfURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "createKindsOfURL: url create failed", e);
            e.printStackTrace();
        }
        return url;
    }

    private static List<CommentsBean> extractCommentFromJson(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        List<CommentsBean> commentsBeans = new ArrayList<>();
        CommentsBean commentsBean;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject element = jsonArray.getJSONObject(i);
                    String author = element.getString("author");
                    String content = element.getString("content");
                    String url = element.getString("url");

                    commentsBean = new CommentsBean(author, content, url);
                    commentsBeans.add(commentsBean);
                }
            } else {
                Log.e(LOG_TAG, "extractCommentFromJson: jsonArray have no element");
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return commentsBeans;
    }

    // Fetch comments json data from web server
    public static List<CommentsBean> fetchCommentsJsonData(int id) {
        String urlString = buildRequestCommentsURL(id);
        URL url = createKindsOfURL(urlString);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpResponse(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "fetchCommentsJsonData: problems making the Http request", e);
            e.printStackTrace();
        }
        List<CommentsBean> commentsBeans = extractCommentFromJson(jsonResponse);
        if (commentsBeans == null) {
            Log.i(LOG_TAG, "TEST:fetchCommentsJsonData() called commentsBean is null" );
        }
        return commentsBeans;
    }


    // Build trailer request url, so can fetch more trailer videos
    public static String buildRequestVideosURL(int id) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(Integer.toString(id))
                .appendEncodedPath(VIDEOS)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .build();
        return uri.toString();
    }

    // Extract data from json string, store them in a List
    private static List<VideosBean> extractVideoFromJson(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        List<VideosBean> videosBeans = new ArrayList<>();
        VideosBean videosBean;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject element = jsonArray.getJSONObject(i);
                String type = element.getString("type");
                if (type.equals("Trailer")) {
                    videosBean = new VideosBean(
                            element.getString("key"),
                            element.getString("name"),
                            type
                    );
                    // add element to this list object
                    videosBeans.add(videosBean);
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "extractVideoFromJson: ", e);
            e.printStackTrace();
        }
        return videosBeans;
    }

    // Fetch json data for specified movie which contain some film trailers,
    // perform this method,
    public static List<VideosBean> fetchVideosJsonData(int id) {
        String urlString = buildRequestVideosURL(id);
        URL url = createKindsOfURL(urlString);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpResponse(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "fetchVideosJsonData: Problem in making http request", e);
            e.printStackTrace();
        }
        List<VideosBean> videosBeans = extractVideoFromJson(jsonResponse);
        Log.i(LOG_TAG, "TEST:fetchVideosJsonData() called... ");
        return videosBeans;
    }


    // Build details request url for detail fragment
    private static String buildRequestDetailsURL(int id) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(Integer.toString(id))
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .build();
        return uri.toString();
    }

    // Extract data from json string and store it.
    private static DetailsBean extractDetailFromJson(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        DetailsBean bean = new DetailsBean();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            bean.setTitle(jsonObject.getString("title"));
            bean.setRuntime(jsonObject.getInt("runtime"));
            bean.setVote_average(jsonObject.getDouble("vote_average"));
            bean.setRelease_date(jsonObject.getString("release_date"));
            bean.setPoster_path(jsonObject.getString("poster_path"));
            bean.setOverview(jsonObject.getString("overview"));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "extractDetailFromJson: Problem in parsing json data", e);
            e.printStackTrace();
        }
        return bean;
    }

    // Fetch json data for specified movie detail
    public static DetailsBean fetchDetailJsonData(int id) {
        String urlString = buildRequestDetailsURL(id);
        URL url = createKindsOfURL(urlString);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DetailsBean bean = extractDetailFromJson(jsonResponse);
        Log.i(LOG_TAG, "TEST:fetchDetailJsonData() called... ");
        return bean;
    }

    // Build image url, so can view directly.
    public static String fetchImageURL(String image) {
        Uri uri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendEncodedPath(IMAGE_RECOMMEND_SIZE185)
                .appendEncodedPath(image)
                .build();
        return uri.toString();
    }

    // Build youtube url, so can view directly.
    public static String fetchYoutubeURL(String video) {
        Uri uri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_KEY_PARAM, video)
                .build();
        return uri.toString();
    }
}
