package com.example.moviedb_app.ui.blindtest;

public class ScrappingMovie {

        /*private void scrapTopMovies() {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GetMovieService retrofitService = retrofit.create(GetMovieService.class);
        for (int j = 2010; j <= 2020; j++) {
            for (int i = 0; i < 10; i++) {
                retrofitService.getDiscoverMovies(i, KEY_API, "en-US", "vote_count.desc", "FR",
                        "false", j+"-01-01", j+"-12-31").enqueue(getMoviePageCallback());
            }
        }
    }

    private Callback<MoviePageResult> getMoviePageCallback() {
        Callback<MoviePageResult> callback = new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                MoviePageResult res = response.body();
                if (res != null) {
                    List<Movie> list = res.getResults();

                    for (Movie movie : list
                    ) {
                        try {
                            createMovie(movie);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
            }

        };
        return callback;
    }

    private void createMovie(Movie movie) throws ParseException {

        db.collection("movies").document(movie.getId().toString())
                .set(movie)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        try {
                            db.collection("movies").document(movie.getId().toString())
                                    .update("releaseDate", new Timestamp(formatter.parse(movie.getReleaseDate())))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }*/

    /*private void scrapGenres() {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GetMovieService retrofitService = retrofit.create(GetMovieService.class);
        retrofitService.getGenres(KEY_API, "en-US").enqueue(getGenrePageCallback());
    }

    private Callback<GenrePageResult> getGenrePageCallback() {
        Callback<GenrePageResult> callback = new Callback<GenrePageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenrePageResult> call, @NonNull Response<GenrePageResult> response) {
                GenrePageResult res = response.body();
                if (res != null) {
                    List<Genre> list = res.getGenres();

                    for (Genre genre : list
                    ) {
                        try {
                            createGenre(genre);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GenrePageResult> call, Throwable t) {
            }

        };
        return callback;
    }

    private void createGenre(Genre genre) throws ParseException {

        db.collection("genres").document(genre.getId().toString())
                .set(genre)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }*/
}
