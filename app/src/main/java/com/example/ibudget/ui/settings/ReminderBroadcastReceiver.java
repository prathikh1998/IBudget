package com.example.ibudget.ui.settings;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.ibudget.MainActivity;
import com.example.ibudget.R;

import java.util.Calendar;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        // Get the notification data from the intent
        Notification notification = intent.getParcelableExtra("notification");

        // Create a notification manager to handle showing the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Check if the app has permission to post notifications
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            // If the app doesn't have permission, we should request it before continuing
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);

            // We can't show the notification without permission, so just return for now
            return;
        }

        // If we get here, we have permission to post notifications

        // Get the notification ID from the intent (this should be a unique integer)
        int notificationId = intent.getIntExtra("notificationId", 0);

        // Create a pending intent for the notification
        Intent notificatiointent = new Intent(context, MainActivity.class);
        notificatiointent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(notificatiointent);
        // Example of creating a PendingIntent with FLAG_IMMUTABLE
        int requestCode = 1;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationManagerCompat.from(context);
        // Set up the notification builder with the appropriate data
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Reminder")
                .setContentText("It's time to check your budget!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("It's time to check your budget!"));

        // Show the notification
        notificationManager.notify(notificationId, builder.build());

        // If we want to schedule the next reminder, we can do that here
        scheduleNextReminder(context, notificationId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void scheduleNextReminder(Context context, int notificationId) {

        // Get the current time
        Calendar calendar = Calendar.getInstance();

        // Set the time for the next reminder (here, we're scheduling it for 24 hours from now)
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        // Create an intent for the reminder
        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        intent.putExtra("notificationId", notificationId);

        // Create a pending intent for the reminder
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // Get the alarm manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Schedule the reminder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
