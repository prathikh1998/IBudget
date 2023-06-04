package com.example.ibudget.ui.settings;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.ibudget.MainActivity;
import com.example.ibudget.R;
import com.example.ibudget.database.AppDatabase;
import com.example.ibudget.database.Payee;
import com.example.ibudget.database.Payment;
import com.example.ibudget.databinding.FragmentPayBinding;
import com.example.ibudget.databinding.FragmentSettingsBinding;
import com.example.ibudget.ui.pay.PayFragmentDirections;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SettingNotification extends AppCompatActivity{

    private static AppDatabase db;

    private TextView selectedDateTextView, selectedTimeTextView;
    private Button setDateButton, setTimeButton, addReminderButton;
    private Spinner payeeSpinner;
    private EditText notesEditText;

    private String selectedPayee;
    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_addnotification);

        // initialize views
        selectedDateTextView = findViewById(R.id.date_text);
        selectedTimeTextView = findViewById(R.id.time_text);
        DatePicker datePicker = findViewById(R.id.date_picker);
        TimePicker timePicker = findViewById(R.id.time_picker);


        addReminderButton = findViewById(R.id.add_reminder_button);
        payeeSpinner = findViewById(R.id.payee_dropdown);
        notesEditText = findViewById(R.id.notes_input);


        // initialize database
        db = AppDatabase.getInstance(getApplicationContext());

        // initialize payee dropdown
        String[] payeeItems = new String[]{
                "Choose a Payee",
                "Starbucks",
                "Walmart",
                "Salary",
                "Electricity Bill",
                "Water Bill",
                "Other"
        };
        ArrayAdapter<String> payeeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, payeeItems);
        payeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Store the selected payee in the selectedPayee variable
                selectedPayee = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        payeeSpinner.setAdapter(payeeAdapter);

        // initialize calendar with current date and time
        selectedDateTime = Calendar.getInstance();

        // set click listeners for date and time buttons



        // set click listener for add reminder button


        addReminderButton.setOnClickListener(view -> setReminder(selectedDateTime, notesEditText.getText().toString(),selectedPayee));

        // set default text for date and time views
        updateSelectedDateTimeViews();
    }


    private void showDatePickerDialog() {
        // Get the current date as default date for the picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create the date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    // Update the date input field with the selected date
                    String selectedDate = String.format("%02d/%02d/%d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                    selectedDateTextView.setText(selectedDate);
                }, year, month, day);

        // Show the date picker dialog
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Do something with the selected time

                    }
                },
                // Set default time
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void setReminder(Calendar calendar, String notes, String payee) {
        // Create a new notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("iBudget Reminder")
                .setContentText("Don't forget to pay " + payee + ": " + notes)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(
                        PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE)
                );

        // Schedule the notification using the AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        intent.putExtra("notification", builder.build());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
       // alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        // Show a toast to indicate the reminder was set
        Toast.makeText(this, "Reminder set for " + payee, Toast.LENGTH_SHORT).show();
    }


    private void updateSelectedDateTimeViews() {
        // Update the date text view
        TextView dateTextView = findViewById(R.id.date_text);
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        dateTextView.setText(sdf.format(selectedDateTime.getTime()));

        // Update the time text view
        TextView timeTextView = findViewById(R.id.time_text);
        String timeFormat = "hh:mm a";
        SimpleDateFormat stf = new SimpleDateFormat(timeFormat, Locale.US);
        timeTextView.setText(stf.format(selectedDateTime.getTime()));
    }



}