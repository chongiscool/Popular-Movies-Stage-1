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
 * Android can't pass object reference to activity and fragment.
 * This class implements [Parcelable] which is Serializable of Java version in Android.
 * So, class can put and get in an [android.content.Intent] and [android.os.Bundle].
 */
class CommentsBean : Parcelable {
    var author: String?
        private set
    var content: String?
        private set
    var url: String?
        private set

    /**
     * author of comments at specific movie.
     * content of comments.
     * this comment address of the web.
     */
    constructor(author: String?, content: String?, url: String?) {
        this.author = author
        this.content = content
        this.url = url
    }

    protected constructor(`in`: Parcel) {
        author = `in`.readString()
        content = `in`.readString()
        url = `in`.readString()
    }

    override fun toString(): String {
        return "$author : $url\n$content"
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(author)
        parcel.writeString(content)
        parcel.writeString(url)
    }

    companion object {
        val CREATOR: Parcelable.Creator<CommentsBean?> = object : Parcelable.Creator<CommentsBean?> {
            override fun createFromParcel(`in`: Parcel): CommentsBean? {
                return CommentsBean(`in`)
            }

            override fun newArray(size: Int): Array<CommentsBean?> {
                return arrayOfNulls(size)
            }
        }
    }
}