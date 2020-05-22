package com.matthieu_louf.movie_blindtest_app.pages.userPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.matthieu_louf.movie_blindtest_app.firebase.FirebaseLog;
import com.matthieu_louf.movie_blindtest_app.pages.MainActivity;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.recycler.languagePreference.LanguagePreferenceAdapter;
import com.matthieu_louf.movie_blindtest_app.recycler.languagePreference.LanguagePreferenceItemTouchHelper;
import com.matthieu_louf.movie_blindtest_app.sharedPreferences.BugMoviesService;
import com.matthieu_louf.movie_blindtest_app.sharedPreferences.SeenMoviesService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
/*import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;*/

public class UserSettingsActivity extends AppCompatActivity {

    private MaterialButton remove_seen_movies_button;
    // private Button delete_button;
    // private Button logout_button;
    // private RecyclerView recyclerView;
    // private LanguagePreferenceAdapter adapter;
    // private List<String> languagePreferencesTexts = new ArrayList<String>();

    AppCompatActivity userSettingsActivity;
    SeenMoviesService seenMoviesService;
    BugMoviesService bugMoviesService;

    FirebaseLog firebaseLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        seenMoviesService = new SeenMoviesService(this);
        bugMoviesService = new BugMoviesService(this);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        firebaseLog = new FirebaseLog(getApplicationContext());

        userSettingsActivity = this;
        //logout_button = findViewById(R.id.user_settings_logout);
        //delete_button = findViewById(R.id.user_settings_delete);
        remove_seen_movies_button = findViewById(R.id.user_settings_remove_seen_movies);

        remove_seen_movies_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seenMoviesService.removeAllSeenMovies();
                bugMoviesService.removeAllBugMovies();
                firebaseLog.removeSeenMovies();
                Toast.makeText(getApplicationContext(),getString(R.string.all_seen_movies_removed),Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpTo(userSettingsActivity,new Intent(userSettingsActivity, MainActivity.class));
            }
        });
        //handleLanguagePreferences();
    }

    /*private void handleLanguagePreferences()
    {
        languagePreferencesTexts.add("VOST");
        languagePreferencesTexts.add("VF");
        languagePreferencesTexts.add("VO");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /recyclerView = findViewById(R.id.recycler_view_language_preferences);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new LanguagePreferenceAdapter(languagePreferencesTexts, R.layout.preview_language_preference,this);
        recyclerView.setAdapter(adapter);
        LanguagePreferenceItemTouchHelper itemTouchHelper = new LanguagePreferenceItemTouchHelper(adapter,languagePreferencesTexts,this,recyclerView);
        ItemTouchHelper helper = new ItemTouchHelper(itemTouchHelper);
        helper.attachToRecyclerView(recyclerView);
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /*private void logout()
    {
        AuthUI.getInstance()
                .signOut(userSettingsActivity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(userSettingsActivity, R.string.logout, Toast.LENGTH_LONG).show();
                        NavUtils.navigateUpTo(userSettingsActivity,new Intent(userSettingsActivity, MainActivity.class));
                    }
                });
    }

    private void delete()
    {
        AuthUI.getInstance()
                .delete(userSettingsActivity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(userSettingsActivity, R.string.delete_user, Toast.LENGTH_LONG).show();
                        NavUtils.navigateUpTo(userSettingsActivity,new Intent(userSettingsActivity, MainActivity.class));
                    }
                });
    }*/
}
