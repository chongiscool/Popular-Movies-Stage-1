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

import android.net.Uri;
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
 */
public class Utility {
    /**
     * This Helper class aim to build four request url and their parse
     */

    private static final String LOG_TAG = Utility.class.getSimpleName();

    // for ImageAdapter
    // example like this: https://api.themoviedb.org/3/movie/550?api_key=XXXX
    // https://api.themoviedb.org/3/movie/popular?api_key=XXXX
    // https://api.themoviedb.org/3/movie/top_rated?api_key=XXXX
    public static final int MOVIE_SORT_POPULAR = 0;
    public static final int MOVIE_SORT_TOP_RATED = 1;
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    public static final String MOVIE_POPULAR = "popular";
    public static final String MOVIE_TOP_RATED = "top_rated";

    // for movie poster
    // example like this: http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
    public static final String IMAGE_RECOMMEND_SIZE185 = "w185";
    public static final String IMAGE_SIZE92 = "w92";
    public static final String IMAGE_SIZE154 = "w154";

    public static final String API_KEY_PARAM = "api_key";
    public static final String PAGE_PARAM = "page";

    // for trailers and reviews
    // example like this: https://www.youtube.com/watch?v=dU1xS07N-FA
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    public static final String YOUTUBE_KEY = "v";
    public static final String VIDEOS = "videos";
    public static final String REVIEWS = "reviews";

    // call the method, return a string of sort movie url with spec sort
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

    // create URL for main fragment
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

    // parse jsonString, and store data to List
    private static List<Bean> extractFeatureFromJson(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        List<Bean> beans = new ArrayList<>();
        Bean bean;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            JSONArray jsonArray = jsonObject.getJSONArray("results");
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
        // Extract relevant fields from the JSON response and create a list of {@link Bean.ResultBean}s
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
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject element = jsonArray.getJSONObject(i);
                String author = element.getString("author");
                String content = element.getString("content");
                String url = element.getString("url");

                commentsBean = new CommentsBean(author, content, url);
                commentsBeans.add(commentsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return commentsBeans;
    }

    // fetch comments json data from web
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
        Log.i(LOG_TAG, "TEST:fetchCommentsJsonData() called.. ");
        return commentsBeans;
    }


    // build trailer request url, so can fetch more trailer videos
    public static String buildRequestVideosURL(int id) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(Integer.toString(id))
                .appendEncodedPath(VIDEOS)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .build();
        return uri.toString();
    }

    // extract data from json string, store it in a List<VideosBean.ResultBean>
    // for recent usage, just store three variable, "key", "type", "name"
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
                    videosBeans.add(videosBean);
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "extractVideoFromJson: ", e);
            e.printStackTrace();
        }
        return videosBeans;
    }

    // fetch json data for specified movie which contain some film trailers,
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


    // built details request url for detail fragment
    private static String buildRequestDetailsURL(int id) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(Integer.toString(id))
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .build();
        return uri.toString();
    }

    // extract data from json string, store it in a Detail class.
    // for stage 1 usage, just store these six variable, "title", "runtime",
    // "vote_average", "release_data", "poster_path", "overview".
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

    // fetch json data for specified movie detail
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


    // build image url, so can view directly in browser
    public static String fetchImageURL(String image) {
        Uri uri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendEncodedPath(IMAGE_RECOMMEND_SIZE185)
                .appendEncodedPath(image)
                .build();
        return uri.toString();
    }

    // build youtube url, so can view directly in youtube
    public static String fetchYoutubeURL(String video) {
        Uri uri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_KEY, video)
                .build();
        return uri.toString();
    }
}
