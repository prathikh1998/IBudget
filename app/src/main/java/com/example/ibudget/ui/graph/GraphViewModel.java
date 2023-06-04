package com.example.ibudget.ui.graph;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GraphViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GraphViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("I am Graph screen");
    }

    public LiveData<String> getText() {
        return mText;
    }
}