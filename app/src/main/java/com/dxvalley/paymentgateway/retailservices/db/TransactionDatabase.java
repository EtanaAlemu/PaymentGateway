package com.dxvalley.paymentgateway.retailservices.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dxvalley.paymentgateway.retailservices.dao.ItemDao;
import com.dxvalley.paymentgateway.retailservices.dao.TransactionDao;
import com.dxvalley.paymentgateway.retailservices.models.Item;
import com.dxvalley.paymentgateway.retailservices.models.Transaction;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

@Database(entities = Transaction.class, exportSchema = false, version = 1)
public abstract class TransactionDatabase extends RoomDatabase {
    private static final String DB_NAME = "transaction_db";
    private static TransactionDatabase instance;

    public static synchronized TransactionDatabase getInstance(Context context){
        if(instance == null){
            final byte[] passphrase = SQLiteDatabase.getBytes("5bcfe932d620d88f6c382880e1b8826f".toCharArray());
            final SupportFactory factory = new SupportFactory(passphrase);
            instance = Room.databaseBuilder(context.getApplicationContext(), TransactionDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().openHelperFactory(factory).build();
        }
        return instance;
    }

    public abstract TransactionDao transactionDao();
}
