package com.matthieu_louf.movie_blindtest_app.recycler.languagePreference;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.matthieu_louf.movie_blindtest_app.R;

import java.util.Collections;
import java.util.List;

public class LanguagePreferenceItemTouchHelper extends ItemTouchHelper.Callback {

    private final LanguagePreferenceAdapter mAdapter;
    private List<String> languagePreferencesTexts;
    private Context context;
    private RecyclerView recyclerView;

    public LanguagePreferenceItemTouchHelper(LanguagePreferenceAdapter adapter, List<String> languagePreferencesTexts, Context context, RecyclerView recyclerView) {
        mAdapter = adapter;
        this.languagePreferencesTexts = languagePreferencesTexts;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        LanguagePreferenceHolder languagePreferenceHolder = (LanguagePreferenceHolder) viewHolder;
        languagePreferenceHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.lt_grey));
        updateViewPositions();
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            LanguagePreferenceHolder languagePreferenceHolder = (LanguagePreferenceHolder) viewHolder;
            languagePreferenceHolder.cardView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder dragged, RecyclerView.ViewHolder target) {
        int position_dragged = dragged.getAdapterPosition();
        int position_target = target.getAdapterPosition();

        Collections.swap(languagePreferencesTexts, position_dragged, position_target);
        mAdapter.notifyItemMoved(position_dragged, position_target);

        return false;
    }

    public void updateViewPositions() {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            LanguagePreferenceHolder languagePreferenceHolder = (LanguagePreferenceHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            languagePreferenceHolder.updatePosition(i + 1);
        }
    }


}
