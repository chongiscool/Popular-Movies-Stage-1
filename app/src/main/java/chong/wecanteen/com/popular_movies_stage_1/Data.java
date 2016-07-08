package chong.wecanteen.com.popular_movies_stage_1;

/**
 * Created by Chong on 7/8/2016.
 */
public class Data {
    private String fake_overview = null;
    private String fake_trailer = null;
    private String fake_reviews_user = null;
    private String fake_reviews_comment = null;

    public Data() {
    }

    public Data(String fake_overview, String fake_trailer, String fake_reviews_user,
                String fake_reviews_comment) {
        this.fake_overview = fake_overview;
        this.fake_trailer = fake_trailer;
        this.fake_reviews_user = fake_reviews_user;
        this.fake_reviews_comment = fake_reviews_comment;
    }

    public String getFake_overview() {
        return fake_overview;
    }

    public void setFake_overview(String fake_overview) {
        this.fake_overview = fake_overview;
    }

    public String getFake_trailer() {
        return fake_trailer;
    }

    public void setFake_trailer(String fake_trailer) {
        this.fake_trailer = fake_trailer;
    }

    public String getFake_reviews_user() {
        return fake_reviews_user;
    }

    public void setFake_reviews_user(String fake_reviews_user) {
        this.fake_reviews_user = fake_reviews_user;
    }

    public String getFake_reviews_comment() {
        return fake_reviews_comment;
    }

    public void setFake_reviews_comment(String fake_reviews_comment) {
        this.fake_reviews_comment = fake_reviews_comment;
    }
}
