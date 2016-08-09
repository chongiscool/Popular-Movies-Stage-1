package chong.wecanteen.com.popular_movies_stage_1.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import chong.wecanteen.com.popular_movies_stage_1.Utility;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Use this IntentService after popular movies stage 1
 * helper methods.
 */
public class MainIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_POPULAR = "chong.wecanteen.com.popular_movies_stage_1.service.action.POPULAR";
    private static final String ACTION_TOP_RATED = "chong.wecanteen.com.popular_movies_stage_1.service.action.TOP_RATED";

    private static final String EXTRA_PAGE = "chong.wecanteen.com.popular_movies_stage_1.service.extra.PAGE";

    public MainIntentService() {
        super("MainIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionPopular(Context context, int page) {
        Intent intent = new Intent(context, MainIntentService.class);
        intent.setAction(ACTION_POPULAR);
        intent.putExtra(EXTRA_PAGE, page);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionTopRated(Context context, int page) {
        Intent intent = new Intent(context, MainIntentService.class);
        intent.setAction(ACTION_TOP_RATED);
        intent.putExtra(EXTRA_PAGE, page);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POPULAR.equals(action)) {
                final int popular_page = intent.getIntExtra(EXTRA_PAGE, -1);
                handleActionPopular(popular_page);
            } else if (ACTION_TOP_RATED.equals(action)) {
                final int top_rated_page = intent.getIntExtra(EXTRA_PAGE, -1);
                handleActionTopRated(top_rated_page);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPopular(int page) {
        Utility.fetchMovieData(0, page);
        // throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionTopRated(int page) {
        Utility.fetchMovieData(1, page);
        // throw new UnsupportedOperationException("Not yet implemented");
    }
}
