package com.example.ibudget.ui.pay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.ibudget.database.AppDatabase;
import com.example.ibudget.database.Payee;
import com.example.ibudget.database.Payment;
import com.example.ibudget.databinding.FragmentPayBinding;

import java.util.List;

public class PayFragment extends Fragment {

    private FragmentPayBinding binding;

    private AppDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get app database
        db = AppDatabase.getInstance(this.getContext());

        List<Payee> payees = db.payeeDao().getAll();

        // Payee dropdown
        String[] payeeNames = new String[payees.size()];
        for (int i = 0; i < payees.size(); i++) {
            payeeNames[i] = payees.get(i).name;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, payeeNames);
        Spinner payeeDropdown = binding.payeeDropdown;
        payeeDropdown.setAdapter(adapter);

        // Other Payee input field
        EditText otherPayee = binding.othersInput;

        // Amount input field
        EditText amountInput = binding.amountInput;
        payeeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Payee payee = db.payeeDao().getByName(payeeNames[i]);

                if (payee.type.equals("Bill") || payee.type.equals("Recurrent")) {
                    amountInput.setText(Double.toString(payee.billAmount * -1));
                } else {
                    amountInput.setText(Double.toString(payee.billAmount));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        // Date input field
        EditText dateInput = binding.dateInput;

        // Notes input field
        EditText notesInput = binding.notesInput;

        // Confirm button
        Button confirmButton = binding.addPaymentBtn;
        confirmButton.setOnClickListener(view -> {
            // Extract current input values
            String selected = payeeDropdown.getSelectedItem().toString();
            String other = otherPayee.getText().toString();
            double amount = Double.parseDouble(amountInput.getText().toString());
            String date = dateInput.getText().toString();
            String notes = notesInput.getText().toString();

            // Persist new payment into database
            String payeePaidTo = selected.equals("Other") ? other : selected;
            db.paymentDao().insert(new Payment(payeePaidTo, amount, date, notes));

            // Navigate to Home fragment
            NavDirections action = PayFragmentDirections.actionNavigationPayToNavigationHome();
            Navigation.findNavController(view).navigate(action);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}