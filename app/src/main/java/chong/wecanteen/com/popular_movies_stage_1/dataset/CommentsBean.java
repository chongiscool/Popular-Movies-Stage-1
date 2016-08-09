package chong.wecanteen.com.popular_movies_stage_1.dataset;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chong on 8/5/2016.
 */
public class CommentsBean implements Parcelable{
    private String author;
    private String content;
    private String url;

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
