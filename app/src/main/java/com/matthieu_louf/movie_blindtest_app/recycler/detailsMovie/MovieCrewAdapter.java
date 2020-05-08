package com.matthieu_louf.movie_blindtest_app.recycler.detailsMovie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.matthieu_louf.movie_blindtest_app.models.detailsMovie.Crew;

import java.util.ArrayList;
import java.util.List;

public class MovieCrewAdapter extends RecyclerView.Adapter<MovieCrewHolder> {
    private final List<Crew> crewList;
    private final int layout;

    public MovieCrewAdapter(List<Crew> crewList, int layout) {
        Predicate<Crew> byOrder = person -> person.getJob().equals("Screenplay") ||
                person.getJob().equals("Director") ||
                person.getJob().equals("Producer") ||
                person.getJob().equals("Director of Photography") ||
                person.getJob().equals("Story") ||
                person.getJob().equals("Music") ||
                person.getJob().equals("Executive Producer");
        List<Crew> filteredCrewList = Stream.of(crewList).filter(byOrder)
                .collect(Collectors.toList());

        this.crewList = new ArrayList<Crew>();
        for(Crew crew : filteredCrewList)
        {
            Crew crewInList = Stream.of(this.crewList)
                    .filter(person -> crew.getId().equals(person.getId()))
                    .findFirst()
                    .orElse(null);
            if(crewInList==null)
            {
                this.crewList.add(crew);
            }
            else{
                crew.setJob(crewInList.getJob()+", " +crew.getJob());
                this.crewList.remove(crewInList);
                this.crewList.add(crew);
            }
        }
        this.layout=layout;
    }

    @NonNull
    @Override
    public MovieCrewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup,false);
        return new MovieCrewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCrewHolder viewHolder, int i) {
        viewHolder.bind(crewList.get(i));
    }

    @Override
    public int getItemCount() {
        return this.crewList == null ? 0 : this.crewList.size();
    }
}