package chong.wecanteen.com.popular_movies_stage_1;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.Toast;

import chong.wecanteen.com.popular_movies_stage_1.adapter.GridViewAdapter;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridView mGridView;
    private GridViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(MainActivity.this);

        mGridView = (GridView) findViewById(R.id.movie_gridview);
        adapter = new GridViewAdapter(this);

        mGridView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(MainActivity.this, "Refreshing...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.purple, R.color.green, R.color.orange);
    }


}
