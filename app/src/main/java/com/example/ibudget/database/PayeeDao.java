package com.example.ibudget.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PayeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Payee payee);

    @Delete
    void delete(Payee payee);

    @Query("SELECT * FROM payee WHERE payeeId = :payeeId LIMIT 1")
    Payee getOne(int payeeId);

    @Query("SELECT * FROM payee WHERE name = :name LIMIT 1")
    Payee getByName(String name);

    @Query(("SELECT * FROM payee ORDER BY payeeId"))
    List<Payee> getAll();
}
