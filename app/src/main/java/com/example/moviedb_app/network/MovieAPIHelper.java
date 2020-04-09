package com.example.moviedb_app.network;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.BlindtestParameters;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.model.MoviePageResult;
import com.example.moviedb_app.model.Video;
import com.example.moviedb_app.model.VideoPageResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieAPIHelper extends AppCompatActivity {

    Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    GetMovieService retrofitService = retrofit.create(GetMovieService.class);

    Random rnd = new Random();

    public MovieAPIHelper() {

    }

    public void getSimilarMovies(Context context, Integer movie_id, Callback<List<String>> callback) {
        List<String> listSimilarTitles = new ArrayList<String>();

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
                callback.onResponse(newCall(listSimilarTitles), Response.success(listSimilarTitles));
            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {

            }
        });
    }

    public void getBestTrailer(Context context, String movie_id, Callback<Video> callback) {
        retrofitService.getVideos(movie_id, context.getResources().getString(R.string.tmdb_api_key), context.getResources().getString(R.string.api_language_key)).enqueue(new Callback<VideoPageResult>() {
            @Override
            public void onResponse(Call<VideoPageResult> call, Response<VideoPageResult> response) {
                if (response.body() != null) {
                    List<Video> videoList = response.body().getResults();
                    Video video = selectBestTrailer(context, videoList);
                    callback.onResponse(newCall(video), Response.success(video));
                }
            }

            @Override
            public void onFailure(Call<VideoPageResult> call, Throwable t) {
            }
        });
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
        return videoSelected;
    }

    public void loadList(Context context, BlindtestParameters parameters, Callback<Movie> movieCallback) {
        int random_page = rnd.nextInt(parameters.getMaximumPage());
        retrofitService.getParametersMovies(random_page,
                context.getResources().getString(R.string.tmdb_api_key),
                context.getResources().getString(R.string.api_language_key),
                context.getResources().getString(R.string.api_region_key),
                parameters.getSortBy(),
                parameters.getReleaseDateGTE(),
                parameters.getReleaseDateLTE(),
                parameters.getWithGenres(),
                parameters.getWithOriginalLanguage()).enqueue(movieListCallback(movieCallback));
    }

    public Callback<MoviePageResult> movieListCallback(Callback<Movie> movieCallback) {
        return new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                if (response.body() != null) {
                    List<Movie> movieList = response.body().getResults();
                    chooseMovieInList(movieList, movieCallback);
                }
            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
            }

        };
    }

    public void chooseMovieInList(List<Movie> movies, Callback<Movie> movieCallback) {
        int random = rnd.nextInt(20);
        Movie movie = movies.get(random);
        movieCallback.onResponse(newCall(movie), Response.success(movie));
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
}
