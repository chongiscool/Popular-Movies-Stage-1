package chong.wecanteen.com.popular_movies_stage_1.dataset;

/**
 * Created by Chong on 8/5/2016.
 */
public class Bean {
    public static int total_pages;
    private String poster_path;
    private int id;
    private String backdrop_path;

    public Bean() {
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}
