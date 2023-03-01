package com.dxvalley.paymentgateway.retailservices.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dxvalley.paymentgateway.retailservices.models.Item;
import com.dxvalley.paymentgateway.retailservices.models.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {
    @Query("SELECT * FROM transaction_db")
    List<Transaction> getTransactionList();

    @Query("SELECT * FROM transaction_db WHERE id in (:id)")
    List<Transaction> getTransaction(int[] id);

    @Query("SELECT COUNT(id) FROM transaction_db")
    int getCount();
    @Insert
    void insertTransaction(Transaction transaction);
    @Update
    void updateTransaction(Transaction transaction);
    @Delete
    void deleteTransaction(Transaction transaction);
}
