package com.example.moviedb_app.ui.blindtest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.network.GetMovieService;
import com.example.moviedb_app.network.RetrofitInstance;
import com.example.moviedb_app.ui.detail_movie_activity.model.MovieDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OneMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneMovieFragment extends Fragment {
    private String KEY_API = "5b061cba26b441ddec657d88428cc9fc";

    private static final String ARG_PARAM1 = "movie_id";

    private Integer movie_id;
    private Movie searched_movie;

    private TextView movie_title;

    public OneMovieFragment() {
        // Required empty public constructor
    }

    public static OneMovieFragment newInstance(Integer movie_id) {
        OneMovieFragment fragment = new OneMovieFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, movie_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie_id = getArguments().getInt(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_one_movie, container, false);

        movie_title = root.findViewById(R.id.one_movie_title);

        fetchMovieDetails();

        return root;
    }

    public void fetchMovieDetails()
    {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getMovie(movie_id.toString(), KEY_API, getString(R.string.api_language_key)).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                searched_movie = response.body();
                movie_title.setText(searched_movie.getTitle());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }
}
