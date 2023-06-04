package com.example.ibudget.ui.payee;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ibudget.database.AppDatabase;
import com.example.ibudget.database.Payee;
import com.example.ibudget.databinding.ActivityAddPayeeFormBinding;


public class AddPayeeActivity extends AppCompatActivity {

    private ActivityAddPayeeFormBinding binding;

    private AppDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add a new Payee");

        // Setting view for activity
        binding = ActivityAddPayeeFormBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Get DB instance
        db = AppDatabase.getInstance(getApplicationContext());

        // Type dropdown input
        String[] items = new String[]{
                "Choose Payee Type",
                "Recurrent",
                "One-time",
                "Income"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, items);
        Spinner typeInput = binding.payeeTypeInput;
        typeInput.setAdapter(adapter);

        // Access all inputs
        EditText nameInput = binding.payeeNameInput;
        EditText addressInput = binding.payeeAddressInput;
        EditText phoneInput = binding.payeePhoneInput;
        EditText accountInput = binding.payeeAccountInput;
        EditText dueAmountInput = binding.payeeBillAmountInput;
        EditText dueDateInput = binding.payeeBillDuedateInput;
        EditText websiteInput = binding.payeeWebsiteInput;
        EditText emailInput = binding.payeeEmailInput;
        EditText termLengthInput = binding.payeeTermLengthInput;
        EditText notesInput = binding.payeeNotesInput;
        EditText tagsInput = binding.payeeTagsInput;

        binding.addPayeeConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new Payee in DB
                String name = nameInput.getText().toString();
                String address = addressInput.getText().toString();
                String phone = phoneInput.getText().toString();
                String account = accountInput.getText().toString();
                Double dueAmount = Double.parseDouble(dueAmountInput.getText().toString());
                String dueDate = dueDateInput.getText().toString();
                String website = websiteInput.getText().toString();
                String email = emailInput.getText().toString();
                String termLength = termLengthInput.getText().toString();
                String type = typeInput.getSelectedItem().toString();
                String notes = notesInput.getText().toString();
                String tags = tagsInput.getText().toString();

                Payee newPayee = new Payee(name, address, phone, account, dueAmount, dueDate,
                        website, email, termLength, type, tags, notes);

                // Add into DB
                db.payeeDao().insert(newPayee);

                // Go back to List of Payees screen
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}