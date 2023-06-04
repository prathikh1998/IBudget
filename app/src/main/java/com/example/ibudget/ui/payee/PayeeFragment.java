package com.example.ibudget.ui.payee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ibudget.R;
import com.example.ibudget.database.AppDatabase;
import com.example.ibudget.database.Payee;
import com.example.ibudget.databinding.FragmentPayeeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PayeeFragment extends Fragment {

    private FragmentPayeeBinding binding;
    private static AppDatabase db;
    private static List<Payee> payeeData;
    private PayeeListAdapter payeeListAdapter;

    public static void refreshPayeeData(){
        payeeData = db.payeeDao().getAll();
    }

    private void displayPrompt() {
        if (payeeData.size() > 0) {
            binding.addPayeePrompt.setVisibility(View.INVISIBLE);
        } else {
            binding.addPayeePrompt.setVisibility(View.VISIBLE);
        }
    }

    private void enableSwipeToDeletePayee() {
        DeletePayeeCallback deletePayeeCallback =
                new DeletePayeeCallback(this.getContext(), payeeListAdapter,
                        binding.payeeScreenLayout);

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(deletePayeeCallback);
        itemTouchhelper.attachToRecyclerView(binding.recPayeeList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(this.getContext());
        payeeData = db.payeeDao().getAll();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPayeeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Check if remove prompt
        displayPrompt();

        // Set payee list to vertical linear layout
        binding.recPayeeList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create and set adapter for recycle view
        payeeListAdapter = new PayeeListAdapter(payeeData, getContext());
        binding.recPayeeList.setAdapter(payeeListAdapter);

        enableSwipeToDeletePayee();

        FloatingActionButton addPayeeButton = binding.addPayeeButton;
        addPayeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddPayeeActivity.class);
                startActivity(i);
            }
        });

        return root;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        refreshPayeeData();
        payeeListAdapter.setPayeeData(payeeData);
        payeeListAdapter.notifyDataSetChanged();
        displayPrompt();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}