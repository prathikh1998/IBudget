package com.example.ibudget.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Payment.class, Payee.class}, version = 2, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract PaymentDao paymentDao();
    public abstract PayeeDao payeeDao();
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
