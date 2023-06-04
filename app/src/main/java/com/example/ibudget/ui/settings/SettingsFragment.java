package com.example.ibudget.ui.settings;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.example.ibudget.MainActivity;
import com.example.ibudget.database.AppDatabase;
import com.example.ibudget.database.Payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.Manifest;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ibudget.R;
import com.example.ibudget.databinding.FragmentSettingsBinding;

import java.util.Calendar;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;
    private Button notificationButton;
    private NotificationManager notificationManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Define and initialize the context variable

        SettingsViewModel homeViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSettings;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


       notificationButton = root.findViewById(R.id.notification_button);
        // Get the NotificationManager system service

        notificationButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingNotification.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}