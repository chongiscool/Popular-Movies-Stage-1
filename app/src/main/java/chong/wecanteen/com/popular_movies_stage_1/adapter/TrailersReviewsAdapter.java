package chong.wecanteen.com.popular_movies_stage_1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import chong.wecanteen.com.popular_movies_stage_1.Data;
import chong.wecanteen.com.popular_movies_stage_1.R;

/**
 * Created by Chong on 7/8/2016.
 */
public class TrailersReviewsAdapter extends ArrayAdapter<Data> {
    public static final String TAG =
            TrailersReviewsAdapter.class.getSimpleName();

    private Context mContext;
    ArrayList<Data> trailersReviewsData;

    // three item type
    public final int TYPE_OVERVIEW = 0;
    public final int TYPE_TRAILERS = 1;
    public final int TYPE_REVIEWS = 2;

    public static class ViewHolder {
        public TextView movie_overview;
        public TextView movie_trailers;
        public TextView movie_reviews_user;
        public TextView movie_reviews_comment;
    }

    public TrailersReviewsAdapter(Context context,
                                  ArrayList<Data> trailersReviewsData) {
        super(context, 0);
        mContext = context;
        this.trailersReviewsData = trailersReviewsData;
    }

    @Override
    public int getItemViewType(int position) {
        Data fakeData = trailersReviewsData.get(position);

        String fake_overview = fakeData.getFake_overview();
        String fake_trailer = fakeData.getFake_trailer();
        String fake_reviews_author = fakeData.getFake_reviews_user();

        if (fake_overview != null) {
            return 0;
        }

        if (fake_trailer != null) {
            return 1;
        }
        // cause this is fake data, suppose have author as same as comment
        if (fake_reviews_author != null) {
            return 2;
        }

        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return trailersReviewsData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder viewHolder;

        // populate overview, then trailers, lastly reviews
        if (convertView == null) {
            viewHolder = new ViewHolder();
            switch (type) {
                case TYPE_OVERVIEW:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_overview, parent, false);
                    viewHolder.movie_overview = (TextView) convertView.findViewById(R.id.movie_overview);
                    break;
                case TYPE_TRAILERS:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_trailer, parent, false);
                    viewHolder.movie_trailers = (TextView) convertView.findViewById(R.id.movie_trailer);
                    break;
                case TYPE_REVIEWS:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_reviews, parent, false);
                    viewHolder.movie_reviews_user = (TextView) convertView.findViewById(R.id.movie_reviews_user);
                    viewHolder.movie_reviews_comment = (TextView) convertView.findViewById(R.id.movie_reviews_comment);
                    break;
                default:
            }
            // store view in the viewHolder
            convertView.setTag(viewHolder);
        } else {
            // If exist, then use it directly
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate each item_list
        Data fakeData = trailersReviewsData.get(position);
        switch (type) {
            case TYPE_OVERVIEW:
                viewHolder.movie_overview.setText(fakeData.getFake_overview());
                break;
            case TYPE_TRAILERS:
                viewHolder.movie_trailers.setText(fakeData.getFake_trailer());
                break;
            case TYPE_REVIEWS:
                viewHolder.movie_reviews_user.setText(fakeData.getFake_reviews_user());
                viewHolder.movie_reviews_comment.setText(fakeData.getFake_reviews_comment());
                break;
        }
        return convertView;
    }
}
