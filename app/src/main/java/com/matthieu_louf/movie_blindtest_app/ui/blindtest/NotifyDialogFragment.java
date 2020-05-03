package com.matthieu_louf.movie_blindtest_app.ui.blindtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.model.Movie;
import com.matthieu_louf.movie_blindtest_app.model.Video;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifyDialogFragment extends DialogFragment {
    private String TAG = "NotifyDialogFragment";
    private String video_db_table = "videos";

    List<Integer> selectedItems;
    String[] error_key = new String[]{"error_title_at_start_count", "error_inappropriate_trailer_count","error_other_count"};

    Video video;
    Movie movie;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public NotifyDialogFragment(Video video, Movie movie) {
        this.video = video;
        this.movie = movie;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        selectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.notify_trailer_bug)
                .setIcon(R.drawable.ic_flag_black_24dp)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.notify_array, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    selectedItems.add(which);
                                } else if (selectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    selectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.send_notify, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        initFirebaseVideo();
                    }
                })
        .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //your code here
            }
        });
        return builder.create();
    }

    private void initFirebaseVideo() {
        DocumentReference docRef = db.collection(video_db_table).document(video.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        updateFirebaseVideo();
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        createFirebaseVideo();
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void createFirebaseVideo() {
        Map<String, Object> video_db = new HashMap<>();
        video_db.put("id", video.getId());
        video_db.put("key", video.getKey());
        video_db.put("site", video.getSite());
        video_db.put("iso6391", video.getIso6391());
        video_db.put("iso31661", video.getIso31661());
        video_db.put("name", video.getName());
        video_db.put("size", video.getSize());
        video_db.put("type", video.getType());
        video_db.put("id_movie", movie.getId());
        video_db.put("name_movie", movie.getTitle());

        video_db.put("start_time", 0);

        for (int i = 0; i < error_key.length; i++) {
            video_db.put(error_key[i], 0);
        }

        db.collection(video_db_table).document(video.getId())
                .set(video_db)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateFirebaseVideo();
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void updateFirebaseVideo() {
        db.collection(video_db_table).document(video.getId())
                .update(
                        error_key[0], FieldValue.increment(selectedItems.contains(0) ? 1 : 0),
                        error_key[1], FieldValue.increment(selectedItems.contains(1) ? 1 : 0),
                        error_key[2], FieldValue.increment(selectedItems.contains(2) ? 1 : 0)
                )
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
    }


}
