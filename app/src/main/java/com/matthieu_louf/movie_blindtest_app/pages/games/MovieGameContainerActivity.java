package com.matthieu_louf.movie_blindtest_app.pages.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.matthieu_louf.movie_blindtest_app.R;

import com.matthieu_louf.movie_blindtest_app.firebase.FirebaseLog;
import com.matthieu_louf.movie_blindtest_app.models.GameType;
import com.matthieu_louf.movie_blindtest_app.models.blindtest.GameParameters;
import com.matthieu_louf.movie_blindtest_app.models.movie.Movie;

import com.matthieu_louf.movie_blindtest_app.api.MovieAPIHelper;
import com.matthieu_louf.movie_blindtest_app.pages.games.blindtestGame.FinishPageFragment;
import com.matthieu_louf.movie_blindtest_app.pages.games.blindtestGame.BlindTestFragment;
import com.matthieu_louf.movie_blindtest_app.pages.games.goodOrBadGame.GoodOrBadGameFragment;
import com.matthieu_louf.movie_blindtest_app.sharedPreferences.ThemePlayedService;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieGameContainerActivity extends AppCompatActivity {
    private String TAG = "BlindtestMovieActivity";

    private static final String PARAMETERS_KEY = "PARAMETERS_KEY";

    private GameParameters gameParameters;

    private List<Movie> movieList = new ArrayList<Movie>();

    boolean firstTime = true;

    Integer movie_count = 0;
    Integer error_count = 0;
    Integer good_responses_count = 0;
    Integer score_total = 0;


    FragmentContainerView fragmentContainerView;

    ProgressBar progressBar;
    TextView loadingText;
    ConstraintLayout loading_constraint_layout;
    ProgressBar progressBar_ad;
    TextView loadingText_ad;
    ConstraintLayout loading_constraint_layout_ad;
    AdView adView;

    private FirebaseLog firebaseLog;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragmentLoaded;

    MovieAPIHelper movieAPIHelper;

    private YouTubePlayerFragment youTubePlayerFragment;
    public YouTubePlayer youTubePlayer;
    View fragmentYoutubePlayer;

    private ThemePlayedService themePlayedService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_game_container);
        movieAPIHelper = new MovieAPIHelper(this);
        themePlayedService = new ThemePlayedService(this);
        checkIfConnected();

        adView = findViewById(R.id.ad_view_loading_next_movie);
        fragmentContainerView = findViewById(R.id.one_movie_fragment_container);
        fragmentYoutubePlayer = findViewById(R.id.youtube_player_fragment);
        progressBar = findViewById(R.id.loading_progress);
        loadingText = findViewById(R.id.loading_textview);
        loading_constraint_layout = findViewById(R.id.blindtest_sub_constraint);
        progressBar_ad = findViewById(R.id.loading_progress_ad);
        loadingText_ad = findViewById(R.id.loading_textview_ad);
        loading_constraint_layout_ad = findViewById(R.id.blindtest_sub_constraint_ad);

        firebaseLog = new FirebaseLog(getApplicationContext());
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();


        Intent intent = getIntent();
        if (intent != null) {
            gameParameters = (GameParameters) intent.getSerializableExtra(PARAMETERS_KEY);
        }
        initializeYoutubePlayer();
        showLoading(false);
    }

    private void loadNewAd() {
        Log.d(TAG, "Load new Add !");
        Bundle extras = new Bundle();
        extras.putString("npa", "1");

        AdRequest request = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();

        adView.loadAd(request);
    }

    private void initializeYoutubePlayer() {
        youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
                .findFragmentById(R.id.youtube_player_fragment);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(getString(R.string.google_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
                if (player != null) {
                    loadList();
                    youTubePlayer = player;
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                    Log.d(TAG, "Youtube Player View initialization succeed");
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.video_player_error), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    Log.d(TAG, "Youtube Player View initialization failed");
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "Youtube Player View initialization failed");
                Toast.makeText(getBaseContext(), getString(R.string.video_player_error), Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    public void loadList() {
        Log.d(TAG, "enter getRandomMovie");

        movieAPIHelper.loadList(this, gameParameters, new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.body() != null) {
                    Log.d(TAG, "Got a sub list of movies, movieList as length :" + movieList.size());
                    movieList.addAll(response.body());
                    getRandomMovie(false);
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {

            }
        });
    }

    public void getRandomMovie(boolean loadingFail) {
        checkIfConnected();
        showLoading(loadingFail);
        Movie movie = movieAPIHelper.chooseMovieInList(movieList, loadingFail);
        if (movie == null) {
            Log.d(TAG, "Error while getting a random movie, error count : " + error_count);
            if (error_count < (gameParameters.getMaximumPage() * 20) + 1) {
                Log.d(TAG, "Restart getRandom Movie");
                error_count++;
                getRandomMovie(loadingFail);
            } else {
                Log.d(TAG, "Finish activity");
                Toast.makeText(getBaseContext(), getString(R.string.load_error_not_enough_movies), Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        } else {
            startFragment(movie, loadingFail);
            error_count = 0;
        }
    }

    public void startFragment(Movie movie, boolean loadingFail) {
        Log.d(TAG, "Starting a new OneMovieFragment, firstTime = " + firstTime);
        if (!loadingFail) {
            movie_count++;
        } else {
            this.themePlayedService.incrementExpectedMovieNumber(-1, gameParameters.getId(), gameParameters.getMaximumPage());
        }
        if (movie_count == 1) {
            firebaseLog.startBlindtestEvent(gameParameters.getIdName());
        }
        if (movie_count <= mFirebaseRemoteConfig.getLong("movie_number_in_one_blindtest") && !fragmentManager.isDestroyed()) {
            Fragment fragment = getGameFragment(movie);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (firstTime) {
                fragmentTransaction.add(R.id.one_movie_fragment_container, fragment);
                firstTime = false;
            } else {
                fragmentTransaction.replace(R.id.one_movie_fragment_container, fragment);
            }
            fragmentTransaction.commit();
            fragmentLoaded = fragment;
        } else {
            startFinishFragment();
        }
    }

    public Fragment getGameFragment(Movie movie) {
        Fragment fragment = null;
        if (gameParameters.getGameType() == GameType.BLIND_TEST) {
            fragment = BlindTestFragment.newInstance(movie.getId(),
                    getString(gameParameters.getIdName()) + " : " + movie_count + "/" + mFirebaseRemoteConfig.getLong("movie_number_in_one_blindtest"), movie_count);
        } else if (gameParameters.getGameType() == GameType.GOOD_OR_BAD) {
            fragment = GoodOrBadGameFragment.newInstance(movie.getId(),
                    getString(gameParameters.getIdName()) + " : " + movie_count + "/" + mFirebaseRemoteConfig.getLong("movie_number_in_one_blindtest"), movie_count);
        }

        return fragment;
    }

    public static void start(Context context, GameParameters parameters) {
        Intent intent = new Intent(context, MovieGameContainerActivity.class);
        intent.putExtra(PARAMETERS_KEY, parameters);
        context.startActivity(intent);
    }

    public void showLoading(boolean loadingFail) {
        changeLoadingText(false);
        loading_constraint_layout.setVisibility(View.VISIBLE);
        loading_constraint_layout_ad.setVisibility(View.INVISIBLE);

        if (((movie_count + 1) % 3 == 0 && !loadingFail) || ((movie_count) % 3 == 0 && loadingFail)) {
            loading_constraint_layout_ad.setVisibility(View.VISIBLE);
            loading_constraint_layout.setVisibility(View.INVISIBLE);
        } else if ((movie_count) % 3 == 0 && !loadingFail) {
            loadNewAd();
        }
        fragmentContainerView.setVisibility(View.INVISIBLE);
        fragmentYoutubePlayer.setVisibility(View.INVISIBLE);
    }

    public void hideLoading() {
        loading_constraint_layout.setVisibility(View.INVISIBLE);
        loading_constraint_layout_ad.setVisibility(View.INVISIBLE);
        fragmentContainerView.setVisibility(View.VISIBLE);
        fragmentYoutubePlayer.setVisibility(View.VISIBLE);
    }

    public void changeLoadingText(boolean isVideoLoading) {
        if (isVideoLoading) {
            loadingText.setText(R.string.loading_trailer);
        } else {
            loadingText.setText(R.string.loading);
        }
    }

    public void newResponse(boolean right_guessed, float score) {
        if (right_guessed) {
            good_responses_count++;
            score_total += (int) score;
        }
        this.themePlayedService.incrementPlayedMovieNumber(1, gameParameters.getId(), gameParameters.getMaximumPage());
    }

    public void startFinishFragment() {
        Log.d(TAG, "Starting a new FinishPageFragment");
        fragmentContainerView.setVisibility(View.VISIBLE);
        loading_constraint_layout.setVisibility(View.INVISIBLE);
        loading_constraint_layout_ad.setVisibility(View.INVISIBLE);

        themePlayedService.finishBlindTest(gameParameters.getId(), score_total, good_responses_count, gameParameters.getMaximumPage());

        if (!fragmentManager.isDestroyed()) {
            ConstraintLayout constraintLayout = findViewById(R.id.blindtest_parent_constraint);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                constraintSet.connect(R.id.one_movie_fragment_container, ConstraintSet.START, R.id.blindtest_parent_constraint, ConstraintSet.START, 0);
            } else {
                constraintSet.connect(R.id.one_movie_fragment_container, ConstraintSet.TOP, R.id.blindtest_parent_constraint, ConstraintSet.TOP, 0);
            }
            constraintSet.applyTo(constraintLayout);

            FinishPageFragment fragment = FinishPageFragment.newInstance(good_responses_count, score_total, gameParameters);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.one_movie_fragment_container, fragment);
            fragmentTransaction.commit();
            firebaseLog.endBlindtestEvent(good_responses_count, score_total, gameParameters.getIdName());
            fragmentLoaded = fragment;
        }
    }

    public void checkIfConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Log.d(TAG, "Finish activity");
            Toast.makeText(getBaseContext(), getString(R.string.not_connection), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (fragmentManager != null && fragmentLoaded != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentLoaded);
            fragmentTransaction.commitAllowingStateLoss();
        }
        adView.removeAllViews();
        hideLoading();
        super.onDestroy();
    }
}
