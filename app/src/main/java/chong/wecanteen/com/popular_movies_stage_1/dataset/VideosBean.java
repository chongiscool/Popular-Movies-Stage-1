package chong.wecanteen.com.popular_movies_stage_1.dataset;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chong on 8/5/2016.
 */
public class VideosBean implements Parcelable {
    private String key;
    private String name;
    private String type;

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
