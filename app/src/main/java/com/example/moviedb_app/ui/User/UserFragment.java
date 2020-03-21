package com.example.moviedb_app.ui.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moviedb_app.R;
import com.example.moviedb_app.userdata.UserLikeService;

import java.util.List;

public class UserFragment extends Fragment {

    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user, container, false);

        textView = root.findViewById(R.id.text_user);

        getLikesIds();
        return root;
    }

    public void getLikesIds() {
        UserLikeService userLikeService = new UserLikeService(this.getActivity());
        List<Integer> userLikesIds = userLikeService.getLikes();
        String text = "";
        if (userLikesIds != null) {
            for (int id : userLikesIds) {
                text += id +"\n";
            }
        }
        else{
            Toast.makeText(getActivity(), "There is no liked Movies", Toast.LENGTH_LONG).show();
        }
        textView.setText(text);
    }


}