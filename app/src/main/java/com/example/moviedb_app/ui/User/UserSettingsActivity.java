package com.example.moviedb_app.ui.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.moviedb_app.MainActivity;
import com.example.moviedb_app.R;
/*import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;*/

public class UserSettingsActivity extends AppCompatActivity {

    Button delete_button;
    Button logout_button;

    Activity userSettingsActivity;
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        userSettingsActivity = this;
        logout_button = findViewById(R.id.user_settings_logout);
        delete_button = findViewById(R.id.user_settings_delete);

        logout_button.setOnClickListener(new View.OnClickListener() {
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
        });
    }

    private void logout()
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
