package com.matthieu_louf.movie_blindtest_app.pages.detailsPage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.api.GetMovieService;
import com.matthieu_louf.movie_blindtest_app.api.MovieAPIHelper;
import com.matthieu_louf.movie_blindtest_app.api.RetrofitInstance;
import com.matthieu_louf.movie_blindtest_app.firebase.FirebaseLog;
import com.matthieu_louf.movie_blindtest_app.models.video.Video;
import com.matthieu_louf.movie_blindtest_app.models.detailsMovie.Credits;
import com.matthieu_louf.movie_blindtest_app.recycler.detailsMovie.MovieCastAdapter;
import com.matthieu_louf.movie_blindtest_app.recycler.detailsMovie.MovieCrewAdapter;
import com.matthieu_louf.movie_blindtest_app.recycler.detailsMovie.MovieProductionAdapter;
import com.matthieu_louf.movie_blindtest_app.models.detailsMovie.Genre;
import com.matthieu_louf.movie_blindtest_app.models.detailsMovie.MovieDetails;
import com.matthieu_louf.movie_blindtest_app.sharedPreferences.UserLikeService;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String MOVIE_ID = "";
    private String TAG = "MovieDetailsActivity";
    private String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w342/";
    private String movieId;
    private ImageView image;
    private TextView original_title;
    private TextView rate;
    private TextView release_date;
    private TextView language;
    private TextView genres;
    private TextView synopsis;
    private TextView collection_name;
    private TextView collection_separator;
    private ImageView collection_image;
    private TextView production_separator;
    private RecyclerView recyclerView_production_company;
    private RecyclerView recyclerView_cast;
    private RecyclerView recyclerView_crew;


    private ImageView isLikedIcon;
    private UserLikeService userLikeService;
    private boolean isLiked;

    MovieDetails res;
    Video current_video;

    MovieAPIHelper movieAPIHelper;
    List<String> video_id_error_list = new ArrayList<String>();

    private YouTubePlayerFragment youTubePlayerFragment;
    public com.google.android.youtube.player.YouTubePlayer youTubePlayer;
    View fragmentYoutubePlayer;

    FirebaseLog firebaseLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        firebaseLog = new FirebaseLog(getApplicationContext());

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            movieId = intent.getStringExtra(MOVIE_ID);
        }

        userLikeService = new UserLikeService(this);
        isLiked = userLikeService.isLiked(Integer.parseInt(movieId));
        this.isLikedIcon = findViewById(R.id.movie_is_liked_icon);
        if (isLiked) {
            setLikedIconFromDrawable(R.drawable.ic_star_black_24dp, getResources().getColor(R.color.colorAccent));
        } else {
            setLikedIconFromDrawable(R.drawable.ic_star_border_black_24dp, Color.GRAY);
        }

        this.image = findViewById(R.id.image_details);
        this.original_title = findViewById(R.id.title_details);
        this.rate = findViewById(R.id.rating_details);
        this.release_date = findViewById(R.id.release_date_details);
        this.language = findViewById(R.id.language_details);
        this.genres = findViewById(R.id.genre_details);
        this.synopsis = findViewById(R.id.synopsis_details);
        this.collection_image = findViewById(R.id.collection_details_image);
        this.collection_name = findViewById(R.id.collection_details_name);
        this.collection_separator = findViewById(R.id.collection_details_separator);
        this.production_separator = findViewById(R.id.production_details_separator);

        recyclerView_production_company = findViewById(R.id.recycler_view_details_production);
        recyclerView_crew = findViewById(R.id.recycler_view_crew);
        recyclerView_cast = findViewById(R.id.recycler_view_cast);

        startSearch(movieId);

        this.isLikedIcon.setOnClickListener(v -> {
            if (isLiked) {
                userLikeService.removeLike(Integer.parseInt(movieId));
                setLikedIconFromDrawable(R.drawable.ic_star_border_black_24dp, Color.GRAY);
            } else {
                userLikeService.addLike(Integer.parseInt(movieId));
                setLikedIconFromDrawable(R.drawable.ic_star_black_24dp, getResources().getColor(R.color.colorAccent));
            }
            isLiked = !isLiked;
        });

        initializeYoutubePlayer();

        movieAPIHelper = new MovieAPIHelper(this);
        fragmentYoutubePlayer = findViewById(R.id.youtube_player_fragment);
    }

    private void initializeYoutubePlayer() {
        youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
                .findFragmentById(R.id.youtube_player_fragment);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize("AIzaSyBeRW9uCifWJtoceLeeaRy6rcKkzWAaJoQ", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
                if (player != null) {
                    youTubePlayer = player;
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {

                        }

                        @Override
                        public void onLoaded(String s) {

                        }

                        @Override
                        public void onAdStarted() {

                        }

                        @Override
                        public void onVideoStarted() {

                        }

                        @Override
                        public void onVideoEnded() {

                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {
                            if (errorReason != YouTubePlayer.ErrorReason.UNKNOWN) {
                                video_id_error_list.add(current_video.getKey());
                                getBestTrailer(res.getId().toString(), res);
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "Youtube Player View initialization failed");
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "Youtube Player View initialization failed");
            }
        });
    }

    private void startVideo(Video video) {
        current_video = video;
        if (youTubePlayer != null) {
            youTubePlayer.cueVideo(current_video.getKey());
            Log.d(TAG, "Youtube Player View initialization succeed");
        }
    }

    private void setLikedIconFromDrawable(int drawable, int color) {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, drawable);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        isLikedIcon.setImageDrawable(wrappedDrawable);
    }

    public void startSearch(String query) {

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getMovieDetails(query, getString(R.string.tmdb_api_key), getString(R.string.api_language_key)).enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetails> call, @NonNull Response<MovieDetails> response) {
                res = response.body();

                firebaseLog.seeMovieDetails(res);

                getBestTrailer(res.getId().toString(), res);

                Glide.with(MovieDetailsActivity.this).load(BASE_URL_IMAGE + res.getPosterPath()).into(image);
                original_title.setText(res.getTitle());
                rate.setText(getString(R.string.average_rate) + " : " + res.getVoteAverage().toString() + "/10");

                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = ft.parse(res.getReleaseDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String dateText = DateUtils.formatDateTime(getApplicationContext(), date.getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);
                release_date.setText(getString(R.string.release_date) + " : " + dateText);

                Locale locale = new Locale(res.getOriginalLanguage());
                Locale local_language = new Locale(getResources().getString(R.string.language_iso639));
                language.setText(getString(R.string.language_is) + " : " + locale.getDisplayLanguage(local_language));

                String genre_comment = "";
                if (res.getGenres() != null) {
                    for (Genre x : res.getGenres()) {
                        genre_comment = genre_comment + " " + x.getName();
                    }
                }
                genres.setText(genre_comment);

                synopsis.setText(res.getOverview());

                if (res.getBelongsToCollection() != null) {
                    collection_name.setText(res.getBelongsToCollection().getName());
                    collection_separator.setText(getString(R.string.collection));
                    Glide.with(MovieDetailsActivity.this).load(BASE_URL_IMAGE + res.getBelongsToCollection().getPosterPath()).into(collection_image);
                } else {
                    collection_name.setVisibility(View.INVISIBLE);
                    collection_separator.setVisibility(View.INVISIBLE);
                }
                if (res.getProductionCompanies() != null) {
                    production_separator.setText(getString(R.string.production_companies) + " :");
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView_production_company.setLayoutManager(linearLayoutManager);
                    MovieProductionAdapter movieProductionAdapter = new MovieProductionAdapter(res.getProductionCompanies(), R.layout.preview_movie_details_production);
                    recyclerView_production_company.setAdapter(movieProductionAdapter);
                } else {
                    production_separator.setVisibility(View.INVISIBLE);
                    recyclerView_production_company.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
            }

        });

        retrofitService.getMovieCredits(movieId, getString(R.string.tmdb_api_key)).enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Credits res = response.body();
                    if (res.getCast() != null) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerView_cast.setLayoutManager(linearLayoutManager);
                        MovieCastAdapter movieCastAdapter = new MovieCastAdapter(res.getCast(), R.layout.preview_cast_or_crew);
                        recyclerView_cast.setAdapter(movieCastAdapter);
                    }
                    if (res.getCrew() != null) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerView_crew.setLayoutManager(linearLayoutManager);
                        MovieCrewAdapter movieCrewAdapter = new MovieCrewAdapter(res.getCrew(), R.layout.preview_cast_or_crew);
                        recyclerView_crew.setAdapter(movieCrewAdapter);
                    }
                }

            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t) {

            }
        });

    }

    public void getBestTrailer(String movie_id, MovieDetails movieDetails) {
        movieAPIHelper.getBestTrailer(this, movie_id, movieDetails.getOriginalLanguage(), video_id_error_list,
                new Callback<Video>() {
                    @Override
                    public void onResponse(Call<Video> call, Response<Video> response) {
                        Video video = response.body();
                        if (video != null) {
                            Log.d(TAG, "Got best trailer not null, init video");
                            startVideo(video);
                        } else {
                            Log.d(TAG, "Null video");
                        }
                    }

                    @Override
                    public void onFailure(Call<Video> call, Throwable t) {
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static void start(Context context, String movieId) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_ID, movieId);
        context.startActivity(intent);
    }
}
