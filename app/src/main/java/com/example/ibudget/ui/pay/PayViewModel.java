package com.example.ibudget.ui.pay;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PayViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("I am Pay screen");
    }

    public LiveData<String> getText() {
        return mText;
    }
}