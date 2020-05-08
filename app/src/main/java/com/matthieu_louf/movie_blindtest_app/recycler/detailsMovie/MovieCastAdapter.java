package com.matthieu_louf.movie_blindtest_app.recycler.detailsMovie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.matthieu_louf.movie_blindtest_app.models.detailsMovie.Cast;

import java.util.List;

public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastHolder> {
    private final List<Cast> castList;
    private final int layout;

    public MovieCastAdapter(List<Cast> castList, int layout) {
        Predicate<Cast> byOrder = person -> person.getOrder() <15;
        this.castList = Stream.of(castList).filter(byOrder).collect(Collectors.toList());
        this.layout=layout;
    }

    @NonNull
    @Override
    public MovieCastHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup,false);
        return new MovieCastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCastHolder viewHolder, int i) {
        viewHolder.bind(castList.get(i));
    }

    @Override
    public int getItemCount() {
        return this.castList == null ? 0 : this.castList.size();
    }
}