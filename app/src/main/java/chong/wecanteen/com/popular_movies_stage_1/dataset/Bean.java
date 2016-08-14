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

/**
 * Created by Chong on 8/5/2016.
 *
 * This class will be used to store data which MainFragment needed.
 * For simplify, just store a few properties of a movie in one sort movie.
 * Actually, all of Bean or suffix Bean, they aim to store movie's data related.
 */
public class Bean {
    // record total pages of this sort movie,
    // for adding swipeRefreshLayout to supply page reference.
    public static int total_pages;
    /* poster path of specific movie, like:"/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg" */
    private String poster_path;
    /** specific movie id, use this id request detail, reviews and trailers of this movie*/
    private int id;
    private String backdrop_path;

    public Bean() {
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}
