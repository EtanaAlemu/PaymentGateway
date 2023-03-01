package com.dxvalley.paymentgateway.retailservices.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dxvalley.paymentgateway.retailservices.dao.ItemDao;
import com.dxvalley.paymentgateway.retailservices.dao.TransactionDao;
import com.dxvalley.paymentgateway.retailservices.models.Item;
import com.dxvalley.paymentgateway.retailservices.models.Transaction;

@Database(entities = Transaction.class, exportSchema = false, version = 1)
public abstract class TransactionDatabase extends RoomDatabase {
    private static final String DB_NAME = "transaction_db";
    private static TransactionDatabase instance;

    public static synchronized TransactionDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), TransactionDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract TransactionDao transactionDao();
}
