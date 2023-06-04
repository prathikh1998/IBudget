package com.example.ibudget.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Payment payment);

    @Delete
    void delete(Payment payment);

    @Query("SELECT * FROM payment WHERE paymentId = :paymentId LIMIT 1")
    Payment getOne(int paymentId);

    @Query(("SELECT * FROM payment ORDER BY paymentId DESC"))
    List<Payment> getAll();
}
