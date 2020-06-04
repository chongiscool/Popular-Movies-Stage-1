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

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Chong on 8/5/2016.
 *
 * This class aim to store movie detail for usage of DetailFragment.
 */
class DetailsBean : Parcelable {
    var backdrop_path /* like poster_path, this is for backdrop image */: String? = null
    var id /* movie id, for further requesting revews and trailers */ = 0
    var overview /* plot synopsis of specific movie */: String? = null
    var popularity = 0.0

    /* poster path of specific movie, like:"/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg" */
    var poster_path: String? = null
    var release_date /* movie release date */: String? = null
    var runtime /* movie duration */ = 0
    var title /* movie title */: String? = null
    var vote_average /* vote average at one platform */ = 0.0

    constructor() {}
    protected constructor(`in`: Parcel) {
        backdrop_path = `in`.readString()
        id = `in`.readInt()
        overview = `in`.readString()
        popularity = `in`.readDouble()
        poster_path = `in`.readString()
        release_date = `in`.readString()
        runtime = `in`.readInt()
        title = `in`.readString()
        vote_average = `in`.readDouble()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(backdrop_path)
        parcel.writeInt(id)
        parcel.writeString(overview)
        parcel.writeDouble(popularity)
        parcel.writeString(poster_path)
        parcel.writeString(release_date)
        parcel.writeInt(runtime)
        parcel.writeString(title)
        parcel.writeDouble(vote_average)
    }

    companion object {
        val CREATOR: Parcelable.Creator<DetailsBean?> = object : Parcelable.Creator<DetailsBean?> {
            override fun createFromParcel(`in`: Parcel): DetailsBean? {
                return DetailsBean(`in`)
            }

            override fun newArray(size: Int): Array<DetailsBean?> {
                return arrayOfNulls(size)
            }
        }
    }
}