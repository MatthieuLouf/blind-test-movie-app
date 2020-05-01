package com.example.moviedb_app.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.BlindtestParameters;
import com.example.moviedb_app.model.Genre;
import com.example.moviedb_app.model.GenrePageResult;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.model.MoviePageResult;
import com.example.moviedb_app.model.Video;
import com.example.moviedb_app.model.VideoPageResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieAPIHelper extends AppCompatActivity {
    private static final String TAG = "MovieAPIHelper";

    Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    GetMovieService retrofitService = retrofit.create(GetMovieService.class);

    Random rnd = new Random();

    Context context;
    SeenMoviesService seenMoviesService;
    BugMoviesService bugMoviesService;

    List<Movie> movieList = new ArrayList<Movie>();

    public MovieAPIHelper(Context ctx) {
        this.context = ctx;
        seenMoviesService = new SeenMoviesService(context);
        bugMoviesService  = new BugMoviesService(context);
    }

    public void getSimilarMovies(Context context, Integer movie_id, Callback<List<String>> callback) {
        List<String> listSimilarTitles = new ArrayList<String>();
        if (context == null) {
            callback.onResponse(newCall(null), Response.success(null));
        } else {
            retrofitService.getSimilarMovies(movie_id.toString(), 1, context.getResources().getString(R.string.tmdb_api_key), context.getResources().getString(R.string.api_language_key)).enqueue(new Callback<MoviePageResult>() {
                @Override
                public void onResponse(Call<MoviePageResult> call, Response<MoviePageResult> response) {
                    MoviePageResult moviePageResult = response.body();
                    if (moviePageResult != null) {
                        List<Movie> movieList = moviePageResult.getResults();
                        for (Movie movie : movieList) {
                            if (movie.getTitle() != null && !movie.getTitle().replaceAll("[^a-zA-Z0-9]", "").equals("")) {
                                listSimilarTitles.add(movie.getTitle());
                            }
                        }
                    }
                    Log.d(TAG, "Return similar movies");

                    callback.onResponse(newCall(listSimilarTitles), Response.success(listSimilarTitles));
                }

                @Override
                public void onFailure(Call<MoviePageResult> call, Throwable t) {
                    callback.onResponse(newCall(null), Response.success(null));
                }
            });
        }
    }

    public void getBestTrailer(Context context, String movie_id, String originalLanguage, Callback<Video> callback) {
        if (context == null) {
            callback.onResponse(newCall(null), Response.success(null));
        } else {

            retrofitService.getVideos(movie_id, context.getResources().getString(R.string.tmdb_api_key), context.getResources().getString(R.string.api_language_key)).enqueue(new Callback<VideoPageResult>() {
                @Override
                public void onResponse(Call<VideoPageResult> call, Response<VideoPageResult> response) {
                    if (response.body() != null) {
                        List<Video> videoList = response.body().getResults();
                        Video video = selectBestTrailer(context, videoList);
                        if (video != null) {
                            Log.d(TAG, "return best trailer in language area");
                            callback.onResponse(newCall(video), Response.success(video));
                        } else {
                            retrofitService.getVideos(movie_id, context.getResources().getString(R.string.tmdb_api_key), originalLanguage).enqueue(new Callback<VideoPageResult>() {
                                @Override
                                public void onResponse(Call<VideoPageResult> call, Response<VideoPageResult> response) {
                                    if (response.body() != null) {
                                        List<Video> videoList = response.body().getResults();
                                        Video video = selectBestTrailer(context, videoList);
                                        Log.d(TAG, "return best trailer in original language");
                                        callback.onResponse(newCall(video), Response.success(video));
                                    }
                                }

                                @Override
                                public void onFailure(Call<VideoPageResult> call, Throwable t) {
                                    callback.onResponse(newCall(null), Response.success(null));
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<VideoPageResult> call, Throwable t) {
                    callback.onResponse(newCall(null), Response.success(null));
                }
            });
        }
    }

    private Video selectBestTrailer(Context context, List<Video> videoList) {
        Video videoSelected = null;
        for (Video video : videoList) {
            if (video.getType().equals("Trailer") && video.getSite().equals("YouTube")) {
                if (context.getResources().getString(R.string.api_region_key).equals("FR")) {
                    if (video.getName().toLowerCase().contains("vost")) {
                        videoSelected = video;
                        break;
                    } else {
                        videoSelected = video;
                    }
                } else {
                    videoSelected = video;
                    break;
                }
            }
        }
        Log.d(TAG, "return selected best trailer");
        return videoSelected;
    }

    public void loadList(Context context, BlindtestParameters parameters, Callback<List<Movie>> movieCallback) {
        Log.d(TAG, "enter loadList");

        for (int i = 1; i <= parameters.getMaximumPage(); i++) {
            retrofitService.getParametersMovies(i,
                    context.getResources().getString(R.string.tmdb_api_key),
                    context.getResources().getString(R.string.api_language_key),
                    context.getResources().getString(R.string.api_region_key),
                    parameters.getSortBy(),
                    parameters.getReleaseDateGTE(),
                    parameters.getReleaseDateLTE(),
                    parameters.getWithGenres(),
                    parameters.getWithOriginalLanguage(),
                    "300",
                    "99"+ (!parameters.getWithOutGenres().equals("") ? ","+parameters.getWithOutGenres() :"")).enqueue(movieListCallback(movieCallback,parameters.getMaximumPage(),i));
        }
    }

    public Callback<MoviePageResult> movieListCallback(Callback<List<Movie>> movieCallback,int maxPage,int page) {
        return new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        movieList.addAll(response.body().getResults());
                        Log.d(TAG, "receive a sub list of movies");
                        if(page==maxPage)
                        {
                            movieCallback.onResponse(newCall(movieList), Response.success(movieList));
                        }
                    }
                } else {
                    Log.d(TAG, "response not successful");
                    movieCallback.onFailure(newCall(null), new Throwable());
                }

            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
            }

        };
    }

    public Movie chooseMovieInList(List<Movie> movies) {
        int random = rnd.nextInt(movies.size());

        Movie movie = movies.get(random);
        if (seenMoviesService.isSeen(movie.getId()) ||bugMoviesService.isBug(movie.getId())) {
            Log.d(TAG, "Already seen movie");
            return null;
        } else {
            Log.d(TAG, "return random movie in list");
            seenMoviesService.addSeenMovies(movie.getId());
        }
        return movie;
    }

    public void scrapGenres(Context context, Callback<List<Genre>> helperCallback) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GetMovieService retrofitService = retrofit.create(GetMovieService.class);
        retrofitService.getGenres(context.getResources().getString(R.string.tmdb_api_key), context.getResources().getString(R.string.api_language_key)).enqueue(getGenrePageCallback(helperCallback));
    }

    private Callback<GenrePageResult> getGenrePageCallback(Callback<List<Genre>> helperCallback) {
        Callback<GenrePageResult> callback = new Callback<GenrePageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenrePageResult> call, @NonNull Response<GenrePageResult> response) {
                GenrePageResult res = response.body();
                if (res != null) {
                    List<Genre> list = res.getGenres();
                    Log.d(TAG, "return genre list");
                    helperCallback.onResponse(newCall(list), Response.success(list));
                }
            }

            @Override
            public void onFailure(Call<GenrePageResult> call, Throwable t) {
            }

        };
        return callback;
    }

    public void setBugMovie(Movie movie)
    {
        seenMoviesService.removeSeenMovies(movie.getId());
        bugMoviesService.addBugMovies(movie.getId());
    }

    private <T> Call<T> newCall(T tes) {
        return new Call<T>() {
            @Override
            public Response<T> execute() throws IOException {
                return null;
            }

            @Override
            public void enqueue(Callback<T> callback) {

            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<T> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
    }

    private ResponseBody newResponseBody() {
        return new ResponseBody() {
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public long contentLength() {
                return 0;
            }

            @Override
            public BufferedSource source() {
                return null;
            }
        };
    }
}
