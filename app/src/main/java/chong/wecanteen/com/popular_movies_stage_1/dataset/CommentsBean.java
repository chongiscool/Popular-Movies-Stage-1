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
 * Android can't pass object reference to activity and fragment.
 * This class implements {@link Parcelable} which is Serializable of Java version in Android.
 * So, class can put and get in an {@link android.content.Intent} and {@link android.os.Bundle}.
 */
public class CommentsBean implements Parcelable{

    private String author;
    private String content;
    private String url;

    /**
     * author of comments at specific movie.
     * content of comments.
     * this comment address of the web.
     * */
    public CommentsBean(String author, String content, String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }

    protected CommentsBean(Parcel in) {
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public static final Creator<CommentsBean> CREATOR = new Creator<CommentsBean>() {
        @Override
        public CommentsBean createFromParcel(Parcel in) {
            return new CommentsBean(in);
        }

        @Override
        public CommentsBean[] newArray(int size) {
            return new CommentsBean[size];
        }
    };

    @Override
    public String toString() {
        return author + " : "+ url + "\n" + content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeString(url);
    }
}
