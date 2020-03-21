package com.example.moviedb_app.ui.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.network.GetMovieService;
import com.example.moviedb_app.network.RetrofitInstance;
import com.example.moviedb_app.recycler.MovieAdapter;
import com.example.moviedb_app.userdata.UserLikeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserFragment extends Fragment {

    private String API_KEY = "5b061cba26b441ddec657d88428cc9fc";

    private TextView textView;
    private List<Movie> movieList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user, container, false);

        textView = root.findViewById(R.id.text_user);
        recyclerView = root.findViewById(R.id.recycler_view_user);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        textView.setText("Liked Movies :");

        loadMovies();
        return root;
    }

    private void loadMovies() {
        UserLikeService userLikeService = new UserLikeService(this.getActivity());
        List<Integer> userLikesIds = userLikeService.getLikes();
        if (userLikesIds.size() > 0) {
            for (int id : userLikesIds) {
                loadOneMovie(id);
            }
        } else {
            Toast.makeText(getActivity(), "There is no liked Movies", Toast.LENGTH_LONG).show();
        }
    }

    private void loadOneMovie(int movieId) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getMovie(String.valueOf(movieId), API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                Movie res = response.body();
                movieList.add(res);
                if(movieList.size()==1)
                {
                    movieAdapter =new MovieAdapter(movieList,R.layout.preview_movie_user,"grid_view");

                    recyclerView.setAdapter(movieAdapter);
                }
                else {
                    movieAdapter.notifyItemInserted(movieList.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }

        });
    }

}