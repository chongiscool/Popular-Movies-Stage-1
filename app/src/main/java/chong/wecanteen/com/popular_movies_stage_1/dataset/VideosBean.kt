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
 * Same of [CommentsBean], store trailers related data.
 * Fast 10 times than Serializable
 */
class VideosBean : Parcelable {
    var key /* this key used to build a youtube url*/: String?
        private set
    var name: String?
        private set

    /** trailer name, like: "Trailer one" */
    var type /* there are these type value "Trailer, Teaser"*/: String?
        private set

    constructor(key: String?, name: String?, type: String?) {
        this.key = key
        this.name = name
        this.type = type
    }

    private constructor(`in`: Parcel) {
        key = `in`.readString()
        name = `in`.readString()
        type = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(key)
        parcel.writeString(name)
        parcel.writeString(type)
    }

    companion object {
        val CREATOR: Parcelable.Creator<VideosBean?> = object : Parcelable.Creator<VideosBean?> {
            override fun createFromParcel(`in`: Parcel): VideosBean? {
                return VideosBean(`in`)
            }

            override fun newArray(size: Int): Array<VideosBean?> {
                return arrayOfNulls(size)
            }
        }
    }
}