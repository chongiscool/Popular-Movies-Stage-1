package chong.wecanteen.com.popular_movies_stage_1.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import chong.wecanteen.com.popular_movies_stage_1.SquaredImageView;
import chong.wecanteen.com.popular_movies_stage_1.Utility;
import chong.wecanteen.com.popular_movies_stage_1.dataset.Bean;

/**
 * Created by Chong on 7/7/2016.
 */
public class ImageAdapter extends ArrayAdapter<Bean> {
    private Context mContext;
    private List<Bean> mBeanList;

    public ImageAdapter(Context context, List<Bean> beanList) {
        super(context, 0, beanList);
        mContext = context;
        mBeanList = beanList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SquaredImageView view = (SquaredImageView) convertView;
        if (convertView == null) {
            view = new SquaredImageView(mContext);
            view.setAdjustViewBounds(true);

        }
        Bean bean = mBeanList.get(position);
        String poster_path = bean.getPoster_path();
        String url = Utility.fetchImageURL(poster_path);
        Picasso.with(mContext).load(url).into(view);

        return view;
    }
}
