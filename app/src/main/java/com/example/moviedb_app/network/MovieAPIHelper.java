package com.example.moviedb_app.network;

import android.app.Activity;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.model.Video;
import com.example.moviedb_app.model.VideoPageResult;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieAPIHelper extends Activity {

    Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    GetMovieService retrofitService = retrofit.create(GetMovieService.class);

    public MovieAPIHelper() {

    }


}
