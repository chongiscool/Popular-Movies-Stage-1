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
package chong.wecanteen.com.popular_movies_stage_1.dataset

/**
 * Created by Chong on 8/5/2016.
 *
 * This class will be used to store data which MainFragment needed.
 * For simplify, just store a few properties of a movie in one sort movie.
 * Actually, all of Bean or suffix Bean, they aim to store movie's data related.
 */
class Bean(var id :Int = 0,var  poster_path: String? = null,  var backdrop_path: String? = null) {
    /* poster path of specific movie, like:"/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg" */

    /* specific movie id, use this id request detail, reviews and trailers of this movie */

    companion object {
        // record total pages of this sort movie,
        // for adding swipeRefreshLayout to supply page reference.
        @JvmField
        var total_pages = 0
    }
}