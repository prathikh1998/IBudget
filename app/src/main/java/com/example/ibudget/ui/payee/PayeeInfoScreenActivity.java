package com.example.ibudget.ui.payee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ibudget.R;
import com.example.ibudget.databinding.ActivityPayeeInfoScreenBinding;

public class PayeeInfoScreenActivity extends AppCompatActivity {

    private ActivityPayeeInfoScreenBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Extract all extras passed over
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String address = intent.getStringExtra("address");
        String phone = intent.getStringExtra("phone");
        String account = intent.getStringExtra("account");
        Double billAmount = intent.getDoubleExtra("billAmount", 0.0);
        String billDueDate = intent.getStringExtra("billDueDate");
        String website = intent.getStringExtra("website");
        String email = intent.getStringExtra("email");
        String termLength = intent.getStringExtra("termLength");
        String notes = intent.getStringExtra("notes");
        String type = intent.getStringExtra("type");
        String tags = intent.getStringExtra("tags");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);

        // Setting view for activity
        binding = ActivityPayeeInfoScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Type dropdown input
        String[] types = new String[]{
                "Choose Payee Type",
                "Recurrent",
                "One-time",
                "Income"
        };
        int selectedIndex = 0;
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(type)) {
                selectedIndex = i;
                break;
            }
        }
        Spinner typeInput = binding.payeeTypeInput;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, types);
        typeInput.setAdapter(adapter);
        typeInput.setEnabled(false);
        typeInput.setSelection(selectedIndex);

        binding.payeeNameInput.setText(name);
        binding.payeeAddressInput.setText(address);
        binding.payeePhoneInput.setText(phone);
        binding.payeeAccountInput.setText(account);
        binding.payeeBillAmountInput.setText(Double.toString(billAmount));
        binding.payeeBillDuedateInput.setText(billDueDate);
        binding.payeeWebsiteInput.setText(website);
        binding.payeeEmailInput.setText(email);
        binding.payeeTermLengthInput.setText(termLength);
        binding.payeeNotesInput.setText(notes);
        binding.payeeTagsInput.setText(tags);

        // Call phone number
        binding.payeePhoneInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        // Go to website url on browser
        binding.payeeWebsiteInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "";
                if (!website.startsWith("http://") && !website.startsWith("https://")) {
                    url = "http://" + website;
                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        // Send email
        binding.payeeEmailInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendEmail = new Intent(Intent.ACTION_SEND);
                sendEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                sendEmail.setType("message/rfc822");
                startActivity(Intent.createChooser(sendEmail, "Choose an Email client :"));
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
