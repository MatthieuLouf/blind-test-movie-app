package com.matthieu_louf.movie_blindtest_app.ui.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.matthieu_louf.movie_blindtest_app.MainActivity;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.data.SeenMoviesService;
import com.google.android.material.button.MaterialButton;
/*import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;*/

public class UserSettingsActivity extends AppCompatActivity {

    MaterialButton remove_seen_movies_button;
    Button delete_button;
    Button logout_button;

    AppCompatActivity userSettingsActivity;
    SeenMoviesService seenMoviesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        seenMoviesService = new SeenMoviesService(this);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        userSettingsActivity = this;
        //logout_button = findViewById(R.id.user_settings_logout);
        //delete_button = findViewById(R.id.user_settings_delete);
        remove_seen_movies_button = findViewById(R.id.user_settings_remove_seen_movies);

        remove_seen_movies_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seenMoviesService.removeAllSeenMovies();
                Toast.makeText(getApplicationContext(),getString(R.string.all_seen_movies_removed),Toast.LENGTH_LONG).show();
                NavUtils.navigateUpTo(userSettingsActivity,new Intent(userSettingsActivity, MainActivity.class));
            }
        });
        /*logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });*/
    }

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
