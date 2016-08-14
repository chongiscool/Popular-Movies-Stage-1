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

package chong.wecanteen.com.popular_movies_stage_1.dataset;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chong on 8/5/2016.
 *
 * This class aim to store movie detail for usage of DetailFragment.
 */
public class DetailsBean implements Parcelable {

    private String backdrop_path;  /** like poster_path, this is for backdrop image */
    private int id;  /** movie id, for further requesting revews and trailers */
    private String overview;  /** plot synopsis of specific movie */
    private double popularity;
    /* poster path of specific movie, like:"/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg" */
    private String poster_path;
    private String release_date;  /** movie release date */
    private int runtime;  /** movie duration */
    private String title;  /** movie title */
    private double vote_average;  /** vote average at one platform */

    public DetailsBean() {
    }

    protected DetailsBean(Parcel in) {
        backdrop_path = in.readString();
        id = in.readInt();
        overview = in.readString();
        popularity = in.readDouble();
        poster_path = in.readString();
        release_date = in.readString();
        runtime = in.readInt();
        title = in.readString();
        vote_average = in.readDouble();
    }

    public static final Creator<DetailsBean> CREATOR = new Creator<DetailsBean>() {
        @Override
        public DetailsBean createFromParcel(Parcel in) {
            return new DetailsBean(in);
        }

        @Override
        public DetailsBean[] newArray(int size) {
            return new DetailsBean[size];
        }
    };

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(backdrop_path);
        parcel.writeInt(id);
        parcel.writeString(overview);
        parcel.writeDouble(popularity);
        parcel.writeString(poster_path);
        parcel.writeString(release_date);
        parcel.writeInt(runtime);
        parcel.writeString(title);
        parcel.writeDouble(vote_average);
    }
}
