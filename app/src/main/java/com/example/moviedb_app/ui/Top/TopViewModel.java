package com.example.moviedb_app.ui.Top;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TopViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TopViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is top fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}