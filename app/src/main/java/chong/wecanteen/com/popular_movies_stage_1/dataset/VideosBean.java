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
 * Same of {@link CommentsBean}, store trailers related data.
 * Fast 10 times than Serializable
 */
public class VideosBean implements Parcelable {
    private String key;  /* this key used to build a youtube url*/
    private String name;  /** trailer name, like: "Trailer one"*/
    private String type;  /* there are these type value "Trailer, Teaser"*/

    public VideosBean(String key, String name, String type) {
        this.key = key;
        this.name = name;
        this.type = type;
    }

    private VideosBean(Parcel in) {
        key = in.readString();
        name = in.readString();
        type = in.readString();
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static final Creator<VideosBean> CREATOR = new Creator<VideosBean>() {
        @Override
        public VideosBean createFromParcel(Parcel in) {
            return new VideosBean(in);
        }

        @Override
        public VideosBean[] newArray(int size) {
            return new VideosBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(type);
    }
}
