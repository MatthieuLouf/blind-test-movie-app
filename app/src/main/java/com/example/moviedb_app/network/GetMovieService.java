package com.example.moviedb_app.network;

import com.example.moviedb_app.model.MoviePageResult;
import com.example.moviedb_app.ui.detail_movie_activity.model.MovieDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@SuppressWarnings("ALL")
public interface GetMovieService {
    @GET("movie/popular")
    Call<MoviePageResult> getPopularMovies(@Query("page") int page, @Query("api_key") String userkey);

    @GET("movie/top_rated")
    Call<MoviePageResult> getTopRatedMovies(@Query("page") int page, @Query("api_key") String userkey);

    @GET("search/movie")
    Call<MoviePageResult> getSearchResult(@Query("api_key") String userkey,@Query("query") String query);

    @GET("movie/{id}")
    Call<MovieDetails> getMovieDetails(@Path("id") String id, @Query("api_key") String userkey);
}