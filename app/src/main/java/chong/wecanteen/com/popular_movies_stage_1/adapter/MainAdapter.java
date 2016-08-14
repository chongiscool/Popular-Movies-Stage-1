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
 *
 * This MainAdapter aim to server MainFragment.
 * Populating items to GridView.
 * */
public class MainAdapter extends ArrayAdapter<Bean> {
    private Context mContext;
    private List<Bean> mBeanList;

    public MainAdapter(Context context, List<Bean> beanList) {
        super(context, 0, beanList);
        mContext = context;
        mBeanList = beanList;
    }

    /***
     * @param position the item position(start 0, end length-1)
     * @param convertView to be populated view
     * @param parent The parent that this view will eventually be attached to
     * @return an item view, here is a movie poster
     * with the help of {@link Picasso} load specific movie poster async..ly
     */
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
