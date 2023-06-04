package com.example.ibudget.ui.payee;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PayeeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PayeeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("I am Payee screen");
    }

    public LiveData<String> getText() {
        return mText;
    }
}