package chong.wecanteen.com.popular_movies_stage_1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import chong.wecanteen.com.popular_movies_stage_1.adapter.TrailersReviewsAdapter;

/**
 * Created by Chong on 7/8/2016.
 */
public class DetailActivity extends AppCompatActivity {
    public static final String TAG = DetailActivity.class.getSimpleName();
    // define UI component
    private TextView tv_movie_title;
    private ImageView imageView_movie_poster;
    private TextView tv_movie_runtime;
    private TextView tv_movie_rating;
    private TextView tv_movie_date;
    private ToggleButton tb_movie_favorite;

    public ListView listView_movie_trailers_reviews;

    //
    private ArrayList<Data> listData;
    public TrailersReviewsAdapter adapter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_movie_title = (TextView) findViewById(R.id.movie_title);
        imageView_movie_poster = (ImageView) findViewById(R.id.movie_poster);
        tv_movie_runtime = (TextView) findViewById(R.id.movie_runtime);
        tv_movie_rating = (TextView) findViewById(R.id.movie_rating);
        tv_movie_date = (TextView) findViewById(R.id.movie_date);
        tb_movie_favorite = (ToggleButton) findViewById(R.id.movie_favorite);

        listView_movie_trailers_reviews =
                (ListView) findViewById(R.id.trailers_reviews);

        listData = new ArrayList<>();
        loadFakeData();
        loadFakeData1();

        adapter = new TrailersReviewsAdapter(DetailActivity.this, listData);

        listView_movie_trailers_reviews.setAdapter(adapter);
    }

    String youtube_base_url = "https://www.youtube.com/watch?";
    private void loadFakeData() {
        Data fakeData = new Data();
        fakeData.setFake_overview("This is a overview of movie!");
        listData.add(fakeData);

        fakeData = new Data();
        fakeData.setFake_trailer(youtube_base_url + "KD988dhn");
        listData.add(fakeData);

        fakeData = new Data();
        fakeData.setFake_reviews_user("Cake Wang");
        fakeData.setFake_reviews_comment("This film is awsome, love that beauty girl!");
        listData.add(fakeData);
    }

    private void loadFakeData1() {
        Data fakeData = new Data();

        fakeData = new Data();
        fakeData.setFake_trailer(youtube_base_url + "JD9818hX");
        listData.add(fakeData);

        fakeData = new Data();
        fakeData.setFake_reviews_user("use9627");
        fakeData.setFake_reviews_comment("I am waitting for this film, that's great!");
        listData.add(fakeData);
    }
}
