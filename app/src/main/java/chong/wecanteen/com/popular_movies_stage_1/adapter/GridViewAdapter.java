package chong.wecanteen.com.popular_movies_stage_1.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import chong.wecanteen.com.popular_movies_stage_1.R;


/**
 * Created by Chong on 7/7/2016.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context mContext;

    public GridViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    // create a new GridView for each item referenced by the Adapter
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            //  imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setAdjustViewBounds(true);
            // FIT_CENTER, CENTER_CROP, CENTER_INSIDE
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mThumbIds[i]);

        return imageView;
    }

    private Integer[] mThumbIds = {
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_3
    };
}
