package com.example.ibudget.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ibudget.R;
import com.example.ibudget.database.AppDatabase;
import com.example.ibudget.database.Payment;
import com.example.ibudget.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static FragmentHomeBinding binding;

    private static AppDatabase db;

    private EditText searchBox;

    private PaymentListAdapter paymentListAdapter;

    private static List<Payment> paymentData;

    private static void refreshPaymentData() {
        paymentData = db.paymentDao().getAll();
    }

    @SuppressLint("DefaultLocale")
    public static void updateTotalBalance() {
        refreshPaymentData();
        double balance = 0;
        for(int i = 0; i < paymentData.size(); i++) {
            balance += paymentData.get(i).amount;
        }

        binding.balanceValue.setText(String.format("$%.2f", balance > 0 ? balance : 0));
    }

    private void displayPrompt() {
        if (paymentData.size() > 0) {
            binding.addPaymentPrompt.setVisibility(View.INVISIBLE);
        } else {
            binding.addPaymentPrompt.setVisibility(View.VISIBLE);
        }
    }

    private void enableSwipeToDeletePayment() {
        DeletePaymentCallback deletePaymentCallback =
                new DeletePaymentCallback(this.getContext(), paymentListAdapter,
                        binding.homeScreenLayout);

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(deletePaymentCallback);
        itemTouchhelper.attachToRecyclerView(binding.recentPaymentList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(this.getContext());
        paymentData = db.paymentDao().getAll();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Check if remove prompt
        displayPrompt();

        // Set total balance
        updateTotalBalance();

        // Set recent payment list to vertical linear layout
        binding.recentPaymentList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create and set adapter for recycle view
        paymentListAdapter = new PaymentListAdapter(paymentData, getContext());
        binding.recentPaymentList.setAdapter(paymentListAdapter);

        enableSwipeToDeletePayment();

        searchBox = binding.searchBox;

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the paymentData list
                List<Payment> filteredList = new ArrayList<>();
                for (Payment payment : paymentData) {
                    if (payment.payeePaidTo.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(payment);
                    }
                }
                paymentListAdapter.setSearchQuery(charSequence.toString());
                paymentListAdapter.setPaymentData(filteredList);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do nothing
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        displayPrompt();
        paymentData = db.paymentDao().getAll();
        paymentListAdapter.setPaymentData(paymentData);
        updateTotalBalance();
        String searchText = searchBox.getText().toString();
        List<Payment> filteredList = new ArrayList<>();
        for (Payment payment : paymentData) {
            if (payment.payeePaidTo.toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(payment);
            }
        }
        paymentListAdapter.setPaymentData(filteredList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}