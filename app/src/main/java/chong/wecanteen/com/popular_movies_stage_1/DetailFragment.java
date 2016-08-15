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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chong.wecanteen.com.popular_movies_stage_1.dataset.CommentsBean;
import chong.wecanteen.com.popular_movies_stage_1.dataset.DetailsBean;
import chong.wecanteen.com.popular_movies_stage_1.dataset.VideosBean;

/**
 * Created by Chong on 8/5/2016.
 */
public class DetailFragment extends Fragment {
    private static final int DETAILS_LOADER = 0;
    private static final String EXTRA_DETAILS_BEAN = "detailsBean";
    private static final String EXTRA_VIDEOS_BEAN = "videosBean";
    private static final String EXTRA_COMMENTS_BEAN = "commentsBean";

    // For fragment detail
    @BindView(R.id.movie_title)
    TextView tv_movie_title;
    @BindView(R.id.movie_poster)
    ImageView iv_movie_poster;
    @BindView(R.id.movie_runtime)
    TextView tv_movie_runtime;
    @BindView(R.id.movie_rating)
    TextView tv_movie_rating;
    @BindView(R.id.movie_date)
    TextView tv_movie_date;
    @BindView(R.id.movie_favorite)
    Button btn_movie_favorite;

    // From content fragment detail
    @BindView(R.id.overview)
    TextView tv_movie_overview;
    @BindView(R.id.linear_trailer)
    LinearLayout linear_trailer;
    @BindView(R.id.play_arrow)
    ImageView iv_movie_play_arrow;
    @BindView(R.id.trailer)
    TextView tv_movie_trailer;
    @BindView(R.id.reviews_user)
    TextView tv_movie_reviews_user;
    @BindView(R.id.reviews_comment)
    TextView tv_movie_reviews_comment;

    @BindView(R.id.detail_progress_bar)
    ProgressBar mProgressBar;
    private int mId;
    private Unbinder unbinder;

    /**
     * Detail Fragment will use these three data which implemented
     * {@link android.os.Parcelable} to update UI.
     */
    public DetailsBean detailsBean;
    public VideosBean videosBean;
    public CommentsBean commentsBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        unbinder = ButterKnife.bind(this, root);
        btn_movie_favorite.setVisibility(View.GONE);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Check internet whether is available or not
        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            // Have access to internet
            // by default movie is 'Star Wars: The Force Awakens'
            mId = getActivity().getIntent().getIntExtra(MainFragment.EXTRA_ID, 140607);
            new DetailAsyncTask().execute(mId);

        } else {
            // No internet access, hide these two view and show a toast
            mProgressBar.setVisibility(View.GONE);
            btn_movie_favorite.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "No Internet Available", Toast.LENGTH_LONG).show();
        }
    }

    public class DetailAsyncTask extends AsyncTask<Integer, Void, Bundle> {
        private final String LOG_TAG = DetailAsyncTask.class.getSimpleName();

        @Override
        protected Bundle doInBackground(Integer... integers) {
            Bundle args = new Bundle();
            int id = integers[0];
            DetailsBean detailsBean = Utility.fetchDetailJsonData(id);
            List<VideosBean>
                    videosBeanList = Utility.fetchVideosJsonData(id);
            List<CommentsBean>
                    commentsBeanList = Utility.fetchCommentsJsonData(id);

            args.putParcelable(EXTRA_DETAILS_BEAN, detailsBean);
            // try-catch statement handle IndexOutOfBoundsException if exist
            try {
                args.putParcelable(EXTRA_COMMENTS_BEAN, commentsBeanList.get(0));
            } catch (IndexOutOfBoundsException e) {
                Log.e(LOG_TAG, "doInBackground: no comments", e);
                e.printStackTrace();
            }

            try {
                args.putParcelable(EXTRA_VIDEOS_BEAN, videosBeanList.get(0));
            } catch (IndexOutOfBoundsException e) {
                Log.e(LOG_TAG, "doInBackground: no treailers", e);
                e.printStackTrace();
            }

            return args;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            mProgressBar.setVisibility(View.GONE);
            detailsBean = bundle.getParcelable(EXTRA_DETAILS_BEAN);

            if (bundle.containsKey(EXTRA_COMMENTS_BEAN)) {
                commentsBean = bundle.getParcelable(EXTRA_COMMENTS_BEAN);
                if (commentsBean != null) {
                    tv_movie_reviews_user.setText(commentsBean.getAuthor());
                    tv_movie_reviews_comment.setText(commentsBean.getContent());
                }
            }

            if (bundle.containsKey(EXTRA_VIDEOS_BEAN)) {
                videosBean = bundle.getParcelable(EXTRA_VIDEOS_BEAN);
                if (videosBean != null) {
                    linear_trailer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String key = videosBean.getKey();
                            String uriString = Utility.fetchYoutubeURL(key);
                            Uri uri = Uri.parse(uriString);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });
                    iv_movie_play_arrow.setImageResource(R.drawable.ic_play_arrow_black_48dp);
                    tv_movie_trailer.setText(videosBean.getName());
                }
            } else {
                linear_trailer.setVisibility(View.GONE);
            }

            if (detailsBean != null) {
                tv_movie_title.setText(detailsBean.getTitle());

                String image = detailsBean.getPoster_path();
                Picasso.with(getContext()).load(Utility.fetchImageURL(image))
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(iv_movie_poster);

                String format_runtime = String.format(Locale.US, getContext().getString(R.string.format_runtime), detailsBean.getRuntime());
                String format_rating = String.format(Locale.US, getContext().getString(R.string.format_rating), detailsBean.getVote_average());
                String format_date = String.format(Locale.US, getContext().getString(R.string.format_date), detailsBean.getRelease_date());
                tv_movie_runtime.setText(format_runtime);
                tv_movie_rating.setText(format_rating);
                tv_movie_date.setText(format_date);
                tv_movie_overview.setText(detailsBean.getOverview());
                btn_movie_favorite.setVisibility(View.VISIBLE);
                btn_movie_favorite.setText(R.string.mark_as_favorite);
            }

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
