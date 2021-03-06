package com.matthieu_louf.movie_blindtest_app.api;

import com.matthieu_louf.movie_blindtest_app.models.genre.GenrePageResult;
import com.matthieu_louf.movie_blindtest_app.models.movie.Movie;
import com.matthieu_louf.movie_blindtest_app.models.movie.MoviePageResult;
import com.matthieu_louf.movie_blindtest_app.models.video.VideoPageResult;
import com.matthieu_louf.movie_blindtest_app.models.detailsMovie.Credits;
import com.matthieu_louf.movie_blindtest_app.models.detailsMovie.MovieDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@SuppressWarnings("ALL")
public interface GetMovieService {
    @GET("movie/popular")
    Call<MoviePageResult> getPopularMovies(@Query("page") int page,
                                           @Query("api_key") String api_key,
                                           @Query("language") String language,
                                           @Query("region") String region);

    @GET("movie/top_rated")
    Call<MoviePageResult> getTopRatedMovies(@Query("page") int page,
                                            @Query("api_key") String api_key,
                                            @Query("language") String language,
                                            @Query("region") String region);

    @GET("discover/movie")
    Call<MoviePageResult> getDiscoverMovies(@Query("page") int page,
                                            @Query("api_key") String api_key,
                                            @Query("language") String language,
                                            @Query("sort_by") String sort_by,
                                            @Query("region") String region,
                                            @Query("include_adult") String include_adult,
                                            @Query("primary_release_date.gte") String greaterThan,
                                            @Query("primary_release_date.lte") String lessThan);

    @GET("search/movie")
    Call<MoviePageResult> getSearchResult(@Query("page") int page,
                                          @Query("api_key") String api_key,
                                          @Query("language") String language,
                                          @Query("query") String query);

    @GET("movie/{id}")
    Call<MovieDetails> getMovieDetails(@Path("id") String id,
                                       @Query("api_key") String api_key,
                                       @Query("language") String language);

    @GET("movie/{id}/credits")
    Call<Credits> getMovieCredits(@Path("id") String id,
                                  @Query("api_key") String api_key);

    @GET("movie/{id}")
    Call<Movie> getMovie(@Path("id") String id,
                         @Query("api_key") String api_key,
                         @Query("language") String language);

    @GET("genre/movie/list")
    Call<GenrePageResult> getGenres(@Query("api_key") String api_key,
                                    @Query("language") String language);

    @GET("movie/{movie_id}/videos")
    Call<VideoPageResult> getVideos(@Path("movie_id") String movie_id,
                                    @Query("api_key") String api_key,
                                    @Query("language") String language);

    @GET("movie/{movie_id}/similar")
    Call<MoviePageResult> getSimilarMovies(@Path("movie_id") String movie_id,
                                           @Query("page") int page,
                                           @Query("api_key") String api_key,
                                           @Query("language") String language);

    @GET("discover/movie")
    Call<MoviePageResult> getParametersMovies(@Query("page") int page,
                                              @Query("api_key") String api_key,
                                              @Query("language") String language,
                                              @Query("region") String region,
                                              @Query("sort_by") String sort_by,
                                              @Query("primary_release_date.gte") String greaterThan,
                                              @Query("primary_release_date.lte") String lessThan,
                                              @Query("with_genres") String with_genres,
                                              @Query("with_original_language") String with_original_language,
                                              @Query("vote_count.gte") String vote_countGreaterThan,
                                              @Query("vote_average.gte") String vote_averageGreaterThan,
                                              @Query("vote_average.lte") String vote_averageLowerThan,
                                              @Query("without_genres") String without_genres,
                                              @Query("with_keywords") String with_keywords,
                                              @Query("without_keywords") String without_keywords);
}