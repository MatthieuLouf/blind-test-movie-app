package com.example.moviedb_app.ui.blindtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.moviedb_app.R;

import com.example.moviedb_app.model.Movie;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class BlindtestMovieActivity extends AppCompatActivity {
    private String KEY_API = "5b061cba26b441ddec657d88428cc9fc";
    private String TAG = "create movie";
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private int max_movie_id = 700000;
    Random rnd = new Random();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Movie blind_movie;
    private Gson gson;

    private TextView movie_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blindtest_movie_page);

        movie_title = findViewById(R.id.blindtest_movie_title);
        gson = new Gson();

        ActionBar ab = getSupportActionBar();

        //rnd.setSeed(700363);

    }

    private void getRandomMovie() {
        int random = rnd.nextInt(50);
        Log.d(TAG, "test" + random);

        db.collection("movies")
                .whereEqualTo("originalLanguage", "en")
                .whereGreaterThanOrEqualTo("voteCount", 500)
                .orderBy("voteCount", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                movie_title.setText(document.getData().toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



}
